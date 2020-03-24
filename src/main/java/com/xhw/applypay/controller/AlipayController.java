package com.xhw.applypay.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.sun.corba.se.spi.orb.ParserData;
import com.xhw.applypay.config.AlipayConfig;
import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.model.Orders;
import com.xhw.applypay.model.Product;
import com.xhw.applypay.service.AlipayService;
import com.xhw.applypay.service.OrdersService;
import com.xhw.applypay.service.ProductService;
import com.xhw.applypay.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @Classname AlipayController
 * @Description TODO
 * @Date 2019/3/21 20:40
 * @Created by xhw
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayController.class);


    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    AlipayService alipayService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 对应官方例子   alipay.trade.page.pay.jsp
     *
     * @Description: 前往支付宝第三方网关进行支付
     * @Description notify_url 和 return_url 需要外网可以访问，建议natapp 内网穿透
     * @Date 2019/3/21 20:40
     * @Created by xhw
     */
    @PostMapping("goAlipay")
    @ResponseBody
    public String goAlipay(String orderId) throws Exception {
        Orders order = ordersService.getOrderById(orderId);
        Product product = productService.getProductById(order.getProductId());
        //获取当前时间后的1分钟
        /*LocalDateTime localDateTimeTwo = LocalDateTime.now().plusMinutes(1);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = localDateTimeTwo.format(f);*/

        String result = alipayService.createOrder(orderId, order.getOrderAmount(), "用户订购商品个数:" + order.getBuyCounts() + "个。"
                        , product.getName(), "4m");
        //设置缓存
        redisUtil.set(orderId,orderId,60*4);
        return result;
    }
    /**
     * @Title: AlipayController.     对应官方例子return_url.jsp    return_url必须放在公网上  回跳页面    get
     * @Description: 支付宝同步通知页面
     * @Description TODO
     * @Date 2019/3/22 01:31
     * @Created by xhw
     */
    @RequestMapping("alipayReturnNotice")
    public String alipayReturnNotice(HttpServletRequest request, Map map) throws Exception {

        LOGGER.info("支付成功, 进入同步通知接口...");
        //验证签名
        boolean signVerified = alipayService.rsaCheckV1(request);
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水  这里放在 异步 业务 处理
//            ordersService.updateOrderStatus(out_trade_no, trade_no, total_amount);

            //页面  展示
            Orders order = ordersService.getOrderById(out_trade_no);
            Product product = productService.getProductById(order.getProductId());

            LOGGER.info("********************** 支付成功(支付宝同步通知) **********************");
            LOGGER.info("* 订单号: {}", out_trade_no);
            LOGGER.info("* 支付宝交易号: {}", trade_no);
            LOGGER.info("* 实付金额: {}", total_amount);
            LOGGER.info("* 购买产品: {}", product.getName());
            LOGGER.info("***************************************************************");

            map.put("out_trade_no", out_trade_no);
            map.put("trade_no", trade_no);
            map.put("total_amount", total_amount);
            map.put("productName", product.getName());
        } else {
            LOGGER.info("支付, 验签失败...");
        }
        //前后分离形式  直接返回页面 记得加上注解@ResponseBody
        // http://login.calidray.com你要返回的网址，再页面初始化时候让前端调用你其他接口，返回信息
//        String result = "<form action=\"http://login.calidray.com/?#/index/depreciation-scrap/depreciation\"  method=\"get\" name=\"form1\">\n" +
//                "</form>\n" +
//                "<script>document.forms[0].submit();</script>";
//
//        return result;
        //前后不分离的形式，直接返回jsp页面
        return "alipaySuccess";
    }


    /**
     * @Description: 支付宝异步 通知  制作业务处理 对应官方例子 notify_url.jsp    post
     * @Description TODO
     * @Date 2019/3/22 01:45
     * @Created by xhw
     */
    @RequestMapping(value = "/alipayNotifyNotice")
    @ResponseBody
    public String alipayNotifyNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

        LOGGER.info("支付成功, 进入异步通知接口...");

        //验证签名
        boolean signVerified = alipayService.rsaCheckV1(request);
        //——请在这里编写您的程序（以下代码仅作参考）——

		/* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if (signVerified) {//签名验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
                ordersService.updateOrderStatus(out_trade_no, trade_no, Double.valueOf(total_amount));

                //支付成功删除缓存key,避免过期通知
                redisUtil.del(out_trade_no);

                //这里不用 查  只是为了 看日志 查的方法应该卸载 同步回调 页面 中
                Orders order = ordersService.getOrderById(out_trade_no);
                Product product = productService.getProductById(order.getProductId());

                LOGGER.info("********************** 支付成功(支付宝异步通知)查询 只是为了 看日志  **********************");
                LOGGER.info("* 订单号: {}", out_trade_no);
                LOGGER.info("* 支付宝交易号: {}", trade_no);
                LOGGER.info("* 实付金额: {}", total_amount);
                LOGGER.info("* 购买产品: {}", product.getName());
                LOGGER.info("***************************************************************");

            }
            LOGGER.info("支付成功...");
        } else {//验证失败
            LOGGER.info("支付, 验签失败...");
        }
        return "success";
    }


}



