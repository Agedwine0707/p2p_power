package com.dlpower.p2p.mapper.loan;


import com.dlpower.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 获取平台历史平均年化收益率
     * @return
     */
    Double selectHistoryAvgRate();


    List<LoanInfo> selectLoanInfoByProductType(Map<String, Object> paramMap);

    Long selectLoanInfoTotalSize(Map<String, Object> paramMap);
}