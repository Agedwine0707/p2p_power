package com.dlpower.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.mapper.loan.LoanInfoMapper;
import com.dlpower.p2p.model.loan.LoanInfo;
import com.dlpower.p2p.model.vo.PaginationVo;
import com.dlpower.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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
     * 根据产品类型获取产品列表
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public List<LoanInfo> queryLoanInfoByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoByProductType(paramMap);
    }

    /**
     * 查询历史收益率
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
                    historyAvgRate = loanInfoMapper.selectHistoryAvgRate();
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVG_RATE, historyAvgRate, 7, TimeUnit.DAYS);
                }
            }

        }

        //3. 返回数据
        return historyAvgRate;
    }

    /**
     * 分页查询产品信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public PaginationVo<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        PaginationVo<LoanInfo> paginationVo = new PaginationVo<>();

        // 查询商品列表的总条数
        Long total = loanInfoMapper.selectLoanInfoTotalSize(paramMap);

        paginationVo.setTotal(total);
        List<LoanInfo> list = loanInfoMapper.selectLoanInfoByProductType(paramMap);
        paginationVo.setList(list);
        return paginationVo;
    }

    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }


}
