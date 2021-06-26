package com.dlpower.p2p.mapper.loan;


import com.dlpower.p2p.model.loan.BidInfo;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


    Double selectSumBidMoney();
}