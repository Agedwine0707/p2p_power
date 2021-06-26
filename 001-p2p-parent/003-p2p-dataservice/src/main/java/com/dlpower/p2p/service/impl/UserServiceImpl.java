package com.dlpower.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.mapper.user.UserMapper;
import com.dlpower.p2p.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Service(interfaceClass = UserService.class, version = "1.0.0", timeout = 15000)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


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
