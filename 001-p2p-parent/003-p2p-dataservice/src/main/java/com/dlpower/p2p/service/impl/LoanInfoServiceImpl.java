package com.dlpower.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.mapper.loan.LoanInfoMapper;
import com.dlpower.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Component
@Service(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 15000)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 如何解决缓存击穿问题：
     * 同步代码块+双重验证
     *
     * @return
     */
    @Override
    public Double queryHistoryAvgRate() {

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //1. 从redis中获取数据
        Double historyAvgRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVG_RATE);


        //2. 判断是否存在值
        if (!ObjectUtils.allNotNull(historyAvgRate)) {
            synchronized (this) {
                //2.2 不存在，则先从数据库中查询，保存到redis中一份
                historyAvgRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVG_RATE);
                if (!ObjectUtils.anyNotNull(historyAvgRate)) {
                    historyAvgRate = loanInfoMapper.selectHistryAvgRate();
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVG_RATE, historyAvgRate, 7, TimeUnit.DAYS);
                }
            }

        }

        //3. 返回数据
        return historyAvgRate;
    }


}
