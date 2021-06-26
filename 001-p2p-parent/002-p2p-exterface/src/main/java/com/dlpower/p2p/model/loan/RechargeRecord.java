package com.dlpower.p2p.model.loan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值记录表
 * @TableName b_recharge_record
 */
@Data
public class RechargeRecord implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 充值订单号
     */
    private String rechargeNo;

    /**
     * 充值订单状态（0充值中，1充值成功，2充值失败）
     */
    private String rechargeStatus;

    /**
     * 充值金额
     */
    private Double rechargeMoney;

    /**
     * 充值时间
     */
    private Date rechargeTime;

    /**
     * 充值描述
     */
    private String rechargeDesc;

    private static final long serialVersionUID = 1L;
}