package com.xhw.applypay.controller;

import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.model.Orders;
import com.xhw.applypay.model.Product;
import com.xhw.applypay.service.OrdersService;
import com.xhw.applypay.service.ProductService;
import com.xhw.applypay.util.AjaxResult;
import com.xhw.applypay.util.AjaxResultUtils;
import idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname ProductController
 * @Description TODO
 * @Date 2019/3/21 21:56
 * @Created by xhw
 */
@Controller
@RequestMapping
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService ordersService;

    /**
     * 获取产品列表
     * 获取订单列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(path = {"/index","/"})
    public String products(Map map) {
        List<Product> pList = productService.getProducts();
        List<Orders> orderList = ordersService.getOrderList();
        map.put("pList", pList);
        map.put("oList",orderList);
        return "index";
    }

    /**
     * 进入确认页面
     *
     * @param productId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goConfirm")
    public String goConfirm(String productId, Map map) {
        Product p = productService.getProductById(productId);
        map.put("p", p);
        return "goConfirm";
    }

    /**
     * 进入退款页面
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goRefund")
    public String goRefund(String orderNo, Map map) {
        Orders order = ordersService.getOrderById(orderNo);
        map.put("order", order);
        return "goRefund";
    }

    /**
     * 分段提交
     * 第一段：保存订单
     *
     * @param order
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createOrder")
    @ResponseBody
    public AjaxResult createOrder(Orders order) throws Exception {

        Product p = productService.getProductById(order.getProductId());
        Sid sid = new Sid();
        String orderId = sid.nextShort();
        order.setOrderNum(orderId);
        order.setCreateTime(new Date());
        order.setOrderAmount(p.getPrice() * order.getBuyCounts());
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.key);
        ordersService.saveOrder(order);
        return AjaxResultUtils.getInfoMessage(orderId);
    }

    /**
     * 分段提交
     * 第二段
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goPay")
    public String goPay(String orderId, Map map) {

        Orders order = ordersService.getOrderById(orderId);

        Product p = productService.getProductById(order.getProductId());

        map.put("order", order);
        map.put("p", p);

        return "goPay";
    }

}
