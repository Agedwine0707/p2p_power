package com.dlpower.p2p.service;

import com.dlpower.p2p.model.loan.LoanInfo;
import com.dlpower.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {

    /**
     * 根据产品类型获取对应产品列表
     * @param paramMap 查询参数
     * @return
     */
    List<LoanInfo> queryLoanInfoByProductType(Map<String, Object> paramMap);

    /**
     * 获取历史平均年化收益率
     * @return
     */
    Double queryHistoryAvgRate();


    /**
     * 分页查询产品
     * @param paramMap
     * @return
     */
    PaginationVo<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据id查询产品信息
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}
