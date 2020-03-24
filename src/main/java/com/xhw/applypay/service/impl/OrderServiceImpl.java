package com.xhw.applypay.service.impl;

import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.mapper.IFlowDao;
import com.xhw.applypay.mapper.IOrderDao;
import com.xhw.applypay.model.Flow;
import com.xhw.applypay.model.Orders;
import com.xhw.applypay.service.OrdersService;
import idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Classname OrderServiceImpl
 * @Description TODO
 * @Date 2019/3/21 20:47
 * @Created by xhw
 */
@Service
public class OrderServiceImpl implements OrdersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IFlowDao iFlowDao;


    @Override
    public void saveOrder(Orders order) {
        iOrderDao.insert(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, String alpayFlowNum, double paidAmount) {
        Orders order = getOrderById(orderId);
        //如果是 待付款 修改订单状态 并且 并且 新增  流水
        if (order.getOrderStatus().equals(OrderStatusEnum.WAIT_PAY.key)) {
            order = new Orders();
            order.setOrderNum(orderId);
            order.setOrderStatus(OrderStatusEnum.PAID.key);
            order.setPaidTime(new Date());
            order.setPaidAmount(paidAmount);

            iOrderDao.updateById(order);

            //查询    修改后最新的order
            order = getOrderById(orderId);
            Flow flow = new Flow();
            flow.setFlowNum(alpayFlowNum);
            flow.setBuyCounts(order.getBuyCounts());
            flow.setCreateTime(new Date());
            flow.setOrderNum(orderId);
            flow.setPaidAmount(paidAmount);
            flow.setPaidMethod(1);
            flow.setProductId(order.getProductId());
            //插入 流水表
            iFlowDao.insert(flow);
        }

    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatusEnum orderStatus) {
        Orders order =new Orders();
        order.setOrderNum(orderId);
        order.setOrderStatus(orderStatus.key);
        if(orderStatus.key.equals("40")){//交易关闭
            order.setCloseTime(new Date());
        }else if(orderStatus.key.equals("50")){//已退款
            order.setRefundTime(new Date());
        }
        iOrderDao.updateById(order);
    }

    @Override
    public Orders getOrderById(String orderNum) {
        LOGGER.info("开始查询订单，订单orderId:{}", orderNum);
        return iOrderDao.getOrderById(orderNum);
    }

    @Override
    public List<Orders> getOrderList() {
        return iOrderDao.getOrderList();
    }
}
