package com.xhw.applypay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.xhw.applypay.config.AlipayConfig;
import com.xhw.applypay.service.AlipayService;
import com.xhw.applypay.util.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    private final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    AlipayClient alipayClient;

    @Override
    public String createOrder(String orderNo, double amount, String body, String subject, String timeout_express) throws AlipayApiException {
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //notify_url 和 return_url 需要外网可以访问，建议natapp 内网穿透
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

        AlipayTradePagePayModel alipayTradePagePayModel=new AlipayTradePagePayModel();

        alipayTradePagePayModel.setOutTradeNo(orderNo);
        alipayTradePagePayModel.setTotalAmount(String.valueOf(amount));
        alipayTradePagePayModel.setBody(body);
        alipayTradePagePayModel.setSubject(subject);
        alipayTradePagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        //这个绝对时间注意：    服务器时间可能会和支付宝服务器时间不一致
        //alipayTradePagePayModel.setTimeExpire(time_expire);
        alipayTradePagePayModel.setTimeoutExpress(timeout_express);

        alipayRequest.setBizModel(alipayTradePagePayModel);
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }

    @Override
    public boolean notify(String tradeStatus, String orderNo, String tradeNo) {
        return false;
    }

    @Override
    public boolean rsaCheckV1(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            return verifyResult;
        } catch (AlipayApiException e) {
            logger.debug("验证签名失败, exception is:{}", e);
            return false;
        }
    }

    @Override
    public AjaxResult refund(String orderNo, double amount, String refundReason) {
        return null;
    }
}
