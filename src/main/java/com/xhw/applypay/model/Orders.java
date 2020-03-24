package com.xhw.applypay.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Orders implements Serializable {
    private Integer id;
    //订单号
    private String orderNum;
    //订单状态  WAIT_PAY("10", "待付款"),PAID("20", "已付款"),CANCELED("30", "已取消"),CLOSED("40", "交易关闭"),REFUND("50","已退款");
    private String orderStatus;
    //订单金额
    private double orderAmount;
    //实际支付金额
    private double paidAmount;
    //商品id
    private String productId;
    //购买个数
    private Integer buyCounts;
    //创建时间
    private Date createTime;
    //支付时间
    private Date paidTime;
    //关闭时间
    private Date closeTime;
    //退款时间
    private Date refundTime;

}