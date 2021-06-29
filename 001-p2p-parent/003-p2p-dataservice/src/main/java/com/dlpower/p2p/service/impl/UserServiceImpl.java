package com.dlpower.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.exception.UserOperationException;
import com.dlpower.p2p.mapper.user.FinanceAccountMapper;
import com.dlpower.p2p.mapper.user.UserMapper;
import com.dlpower.p2p.model.user.FinanceAccount;
import com.dlpower.p2p.model.user.User;
import com.dlpower.p2p.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("all")
@Component
@Service(interfaceClass = UserService.class, version = "1.0.0", timeout = 15000)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    /**
     * 根据手机号码查询对应用户
     * @param phone
     * @return
     */
    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    /**
     * 注册用户
     * @param phone
     * @param loginPassword
     * @return
     */
    @Override
    public User register(String phone, String loginPassword) throws Exception {
        // 用户注册
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());

        int addUserRows = userMapper.insertSelective(user);
        if (addUserRows == 0) {
            throw new UserOperationException("添加用户失败");
        }

        // 添加账户
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(user.getId());
        financeAccount.setAvailableMoney(888.0);

        int rows = financeAccountMapper.insertSelective(financeAccount);
        if (rows <= 0) {
            throw new UserOperationException("注册用户后分配账户失败");
        }
        return user;

    }

    /**
     * 查询平台总用户数
     * @return
     */
    @Override
    public Long queryAllUserCount() {
        //1. 查询redis
        Long allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
        //2. 判断返回结果
        if (!ObjectUtils.allNotNull(allUserCount)) {
            synchronized (this) {
                allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
                if (!ObjectUtils.allNotNull(allUserCount)) {
                    //1 redis中没有则上数据库中查询，然后再保存到redis中一份
                    allUserCount = userMapper.selectAllUserCount();
                    redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT,allUserCount,7, TimeUnit.DAYS);
                }
            }
        }
        //3. 返回结果
        return allUserCount;
    }
}
