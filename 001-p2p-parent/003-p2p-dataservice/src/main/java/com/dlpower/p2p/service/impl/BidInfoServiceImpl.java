package com.dlpower.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.mapper.loan.BidInfoMapper;
import com.dlpower.p2p.model.loan.BidInfo;
import com.dlpower.p2p.service.BidInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenlanjiang
 * @date 2021/6/26
 */
@Component
@Service(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 15000)
public class BidInfoServiceImpl implements BidInfoService {

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;


    /**
     * 获取平台累计投资金额
     * @return
     */
    @Override
    public Double getInvestmentAmountSum() {
        // 先从redis数据库中获取
        Double investMoney = (Double) redisTemplate.opsForValue().get(Constants.INVEST_MONEY_SUM);
        // 如果为空，进入同步代码块再次获取判断
        if (!ObjectUtils.allNotNull(investMoney)) {
            synchronized (this){
                investMoney = (Double) redisTemplate.opsForValue().get(Constants.INVEST_MONEY_SUM);
                // 同步代码块中还是为空，则请求数据库获取，同时存储一份到redis
                if (!ObjectUtils.allNotNull(investMoney)) {
                    investMoney = bidInfoMapper.selectSumBidMoney();
                    redisTemplate.opsForValue().set(Constants.INVEST_MONEY_SUM, investMoney, 7, TimeUnit.DAYS);

                }
            }
        }

        return investMoney;
    }

    /**
     * 查询对应id产品的最近10条记录
     * @param paramMap
     * @return
     */
    @Override
    public List<BidInfo> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoByLoanId(paramMap);

    }
}
