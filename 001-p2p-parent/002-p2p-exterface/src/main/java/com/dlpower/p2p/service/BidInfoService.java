package com.dlpower.p2p.service;

import com.dlpower.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

/**
 * @author chenlanjiang
 * @date 2021/6/26
 */
public interface BidInfoService {
    /**
     * 获取平台累计投资金额
     * @return
     */
    Double getInvestmentAmountSum();

    /**
     * 根据loanID查询最近的10条投资记录
     * @param paramMap
     * @return
     */
    List<BidInfo> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap);
}
