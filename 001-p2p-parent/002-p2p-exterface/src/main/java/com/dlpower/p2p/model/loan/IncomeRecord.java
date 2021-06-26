package com.dlpower.p2p.model.loan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 收益记录表
 * @TableName b_income_record
 */
@Data
public class IncomeRecord implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 产品ID
     */
    private Integer loanId;

    /**
     * 投标记录ID
     */
    private Integer bidId;

    /**
     * 投资金额
     */
    private Double bidMoney;

    /**
     * 收益时间
     */
    private Date incomeDate;

    /**
     * 收益金额
     */
    private Double incomeMoney;

    /**
     * 收益状态（0未返，1已返）
     */
    private Integer incomeStatus;

    private static final long serialVersionUID = 1L;
}