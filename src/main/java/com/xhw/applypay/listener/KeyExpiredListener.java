package com.xhw.applypay.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.xhw.applypay.enums.OrderStatusEnum;
import com.xhw.applypay.service.OrdersService;
import com.xhw.applypay.util.AjaxResultUtils;
import com.xhw.applypay.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(KeyExpiredListener.class);

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    @Autowired
    AlipayClient alipayClient;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OrdersService ordersService;
    /**
     * 这里会收到过期键的通知
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(),StandardCharsets.UTF_8);
        logger.info("redis key 过期：pattern={},channel={},key={}",new String(pattern),channel,key);
        //查询订单状态
        AlipayTradeQueryRequest alipayTradeQueryRequesty=new AlipayTradeQueryRequest();
        AlipayTradeQueryModel alipayTradeQueryModel=new AlipayTradeQueryModel();
        alipayTradeQueryModel.setOutTradeNo(key);
        alipayTradeQueryRequesty.setBizModel(alipayTradeQueryModel);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequesty);
            if(response==null){
                logger.info("查询订单详情网络出错");
                //将当前key继续存入redis中
                redisUtil.set(key,key,60*2);
            }else {
                if(response.getCode().equals("10000")){
                    String tradeStatus = response.getTradeStatus();
                    if(tradeStatus.equals("WAIT_BUYER_PAY")){
                        //将当前key继续存入redis中
                        logger.info("订单还在待付款中。。。。继续放入redis中1");
                        redisUtil.set(key,key,60*2);
                    }else if(tradeStatus.equals("TRADE_CLOSED")){
                        //修改订单状态为关闭
                        logger.info("订单关闭，修改订单状态");
                        ordersService.updateOrderStatus(key, OrderStatusEnum.CLOSED);
                    }else if(tradeStatus.equals("TRADE_SUCCESS")){
                        //成功无须做什么，因为成功会有回调方法进行业务逻辑的运行
                        logger.info("订单状态，支付成功，无须执行任何代码");
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


    }
}
