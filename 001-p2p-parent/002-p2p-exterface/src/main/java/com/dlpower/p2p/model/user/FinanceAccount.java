package com.dlpower.p2p.model.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户财务资金账户表
 * @TableName u_finance_account
 */
@Data
public class FinanceAccount implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 用户可用资金
     */
    private Double availableMoney;

    private static final long serialVersionUID = 1L;
}