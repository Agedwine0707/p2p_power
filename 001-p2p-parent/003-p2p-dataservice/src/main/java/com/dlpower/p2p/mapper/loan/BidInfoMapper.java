package com.dlpower.p2p.mapper.loan;


import com.dlpower.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


    Double selectSumBidMoney();

    List<BidInfo> selectRecentlyBidInfoByLoanId(Map<String, Object> paramMap);
}