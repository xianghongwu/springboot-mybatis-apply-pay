package com.xhw.applypay.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Flow  implements Serializable {
    private Integer id;
    //流水号
    private String flowNum;
    //订单号
    private String orderNum;
    //产品主键ID
    private String productId;
    //支付金额
    private double paidAmount;
    //支付方式 1：支付宝   2：微信
    private Integer paidMethod;
    //购买个数
    private Integer buyCounts;
    //创建时间
    private Date createTime;

}