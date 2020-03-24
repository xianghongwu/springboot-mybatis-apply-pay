package com.xhw.applypay.service;


import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.model.Orders;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @Classname AlipayController
 * @Description TODO
 * @Date 2019/3/21 20:40
 * @Created by xhw
 */
public interface OrdersService {

	/**
	 * 新增订单
	 * @param order
	 */
	 void saveOrder(Orders order);

	/**
	 * 修改订单状态，改为 支付成功，已付款; 同时新增支付流水
	 * @param orderId 订单号
	 * @param alpayFlowNum 支付宝交易号
	 * @param paidAmount 金额
	 */
	 void updateOrderStatus(String orderId, String alpayFlowNum, double paidAmount);

	/**
	 * 修改订单状态
	 * @param orderId
	 * @param orderStatus 订单状态
	 */
	 void updateOrderStatus(String orderId, OrderStatusEnum orderStatus);
	/**
	 * 获取订单
	 * @param orderNum
	 * @return
	 */
	 Orders getOrderById(String orderNum);

	/**
	 * 获取所有的订单
	 * @return
	 */
	 List<Orders> getOrderList();
	
}
