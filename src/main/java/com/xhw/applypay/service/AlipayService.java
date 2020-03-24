package com.xhw.applypay.service;

import com.alipay.api.AlipayApiException;
import com.xhw.applypay.util.AjaxResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝服务接口
 */
public interface AlipayService {
    /**
     * @Description: 创建支付宝订单
     * @param orderNo: 订单编号
     * @param amount: 实际支付金额
     * @param body: 订单描述
     * @param subject 订单标题
     * @param timeout_express 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
     * @return
     * @throws AlipayApiException
     */
    String createOrder(String orderNo, double amount, String body,String subject,String timeout_express) throws AlipayApiException;

    /**
     * @Description:
     * @param tradeStatus: 支付宝交易状态
     * @param orderNo: 订单编号
     * @param tradeNo: 支付宝订单号
     * @Author:
     * @Date: 2019/8/1
     * @return
     */
    boolean notify(String tradeStatus, String orderNo, String tradeNo);

    /**
     * @Description: 校验签名
     * @param request
     * @Author:
     * @Date: 2019/8/1
     * @return
     */
    boolean rsaCheckV1(HttpServletRequest request);

    /**
     * @Description: 退款
     * @param orderNo: 订单编号
     * @param amount: 实际支付金额
     * @param refundReason: 退款原因
     * @Author: XCK
     * @Date: 2019/8/6
     * @return
     */
    AjaxResult refund(String orderNo, double amount, String refundReason);
}
