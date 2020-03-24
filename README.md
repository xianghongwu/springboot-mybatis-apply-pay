# springboot-mybatis-apply-pay
springboot+mybatis+druid+mysql+redis集成支付宝支付

添加了支付时间限制，超时关闭交易，并且修改本地数据库状态，使用的是redis 键过期通知  
  需要修改redis配置文件中   notify-keyspace-events Ex
  
注意，1.支付宝提交订单，没有用手机扫码或者电脑登录，支付宝服务器是没有创建订单的。
      2.pc端支付：只有交易成功才有异步回调；手机支付和app支付：交易成功和交易关闭是有异步回调的。详细介绍看有道云笔记
      3.异步回调接口，需要使用公网地址，可以使用内网穿透。
      
