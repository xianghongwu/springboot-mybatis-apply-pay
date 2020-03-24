package com.xhw.applypay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xhw.applypay.config.AlipayConfig;
import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.service.OrdersService;
import com.xhw.applypay.util.AjaxResult;
import com.xhw.applypay.util.AjaxResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrdersService ordersService;

    @Autowired
    AlipayClient alipayClient;
    @Autowired
    AlipayConfig alipayConfig;

    /**
     * 查看订单在支付宝中的详情
     * @return
     */
    @GetMapping("/alipayOrderInfo")
    @ResponseBody
    public AjaxResult queryAlipayOrderInfo(String orderNum){
        AlipayTradeQueryRequest alipayTradeQueryRequesty=new AlipayTradeQueryRequest();
        AlipayTradeQueryModel alipayTradeQueryModel=new AlipayTradeQueryModel();
        alipayTradeQueryModel.setOutTradeNo(orderNum);
        alipayTradeQueryRequesty.setBizModel(alipayTradeQueryModel);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequesty);
            return AjaxResultUtils.getInfoMessage(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return AjaxResultUtils.getInfoMessage(null);
    }

    /**
     * 关闭订单在支付宝中的详情
     * @param orderNum 商户订单号
     * @return
     */
    @GetMapping("/clouseAlipayOrder")
    @ResponseBody
    public AjaxResult clouseAlipayOrder(String orderNum){
        AlipayTradeCloseRequest alipayTradeCloseRequest=new AlipayTradeCloseRequest();
        alipayTradeCloseRequest.setNotifyUrl(alipayConfig.notifyUrl);
        AlipayTradeCloseModel alipayTradeCloseModel=new AlipayTradeCloseModel();
        alipayTradeCloseModel.setOutTradeNo(orderNum);
        alipayTradeCloseRequest.setBizModel(alipayTradeCloseModel);
        String body=null;
        try {
            AlipayTradeCloseResponse closeResponse = alipayClient.execute(alipayTradeCloseRequest);
            body = closeResponse.getBody();
            if(closeResponse.getCode().equals("10000")){
                logger.info("********************** 交易支付关闭成功**********************");
                ordersService.updateOrderStatus(orderNum, OrderStatusEnum.CLOSED);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return AjaxResultUtils.getInfoMessage(body);
    }

    /**
     * 退款
     * @param orderNo 订单号
     * @param amount 退款金额
     * @param refundReason 退款原因
     * @return
     */
    @PostMapping("/refund")
    @ResponseBody
    public AjaxResult refund(String orderNo,double amount,String refundReason) {
        if(StringUtils.isBlank(orderNo)){
            return AjaxResultUtils.resultMessage("订单编号不能为空");
        }
        if(amount <= 0){
            return AjaxResultUtils.resultMessage("退款金额必须大于0");
        }
        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount(String.valueOf(amount));
        // 退款原因
        model.setRefundReason(refundReason);
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
        // model.setOutRequestNo(UUID.randomUUID().toString());

        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizModel(model);
        AlipayTradeRefundResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            logger.error("订单退款失败，异常原因:{}", e);
        }
        if(alipayResponse != null){
            String code = alipayResponse.getCode();
            String subCode = alipayResponse.getSubCode();
            String subMsg = alipayResponse.getSubMsg();
            if("10000".equals(code)
                    && StringUtils.isBlank(subCode)
                    && StringUtils.isBlank(subMsg)){
                // 表示退款申请接受成功，结果通过退款查询接口查询
                // 修改用户订单状态为退款
                ordersService.updateOrderStatus(orderNo,OrderStatusEnum.REFUND);
                return AjaxResultUtils.getInfoMessage("订单退款成功");
            }
            return AjaxResultUtils.resultMessage(subCode + ":" + subMsg);
        }
        return AjaxResultUtils.resultMessage("订单退款失败");
    }

    /**
     * 退款查询
     * @param orderNo 订单号
     * @return
     */
    @GetMapping("/refundQuery")
    @ResponseBody
    public AjaxResult refundQuery(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return AjaxResultUtils.resultMessage("订单编号不能为空");
        }
        AlipayTradeFastpayRefundQueryModel model=new AlipayTradeFastpayRefundQueryModel();
        //订单支付时传入的商户订单号
        model.setOutTradeNo(orderNo);
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
        model.setOutRequestNo(orderNo);

        AlipayTradeFastpayRefundQueryRequest alipayRequest=new AlipayTradeFastpayRefundQueryRequest();
        alipayRequest.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            logger.error("订单退款查询失败，异常原因:{}", e);
        }
        return AjaxResultUtils.getInfoMessage(alipayResponse);
    }
}
