package com.xhw.applypay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @Classname 支付宝支付的参数配置
 * @Description notify_url 和 return_url 需要外网可以访问，建议natapp 内网穿透
 * @Date 2019/3/21 20:40
 * @Created by xhw
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "pay.alipay")
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public  String appId;//在后台获取（必须配置）

    // 商户私钥，您的PKCS8格式RSA2私钥
    public  String merchantPrivateKey;//教程查看获取方式（必须配置）

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.html 对应APPID下的支付宝公钥。
    public  String alipayPublicKey;

    // 支付宝网关    注意：沙箱测试环境，正式环境为：https://openapi.alipay.com/gateway.do
    public  String gatewayUrl;

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public  String notifyUrl;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public  String returnUrl;

    // 签名方式
    public  String signType = "RSA2";

    // 字符编码格式
    public  String charset = "utf-8";

    //格式
    private String formate = "json";

    /**
     * 最大查询次数
     */
    private static int maxQueryRetry = 5;
    /**
     * 查询间隔（毫秒）
     */
    private static long queryDuration = 5000;
    /**
     * 最大撤销次数
     */
    private static int maxCancelRetry = 3;
    /**
     * 撤销间隔（毫秒）
     */
    private static long cancelDuration = 3000;
    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(this.getGatewayUrl(),
                this.getAppId(),
                this.getMerchantPrivateKey(),
                this.getFormate(),
                this.getCharset(),
                this.getAlipayPublicKey(),
                this.getSignType());
    }
}
