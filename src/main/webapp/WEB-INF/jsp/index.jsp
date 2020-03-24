<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <title>洪武商城</title>

    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.css">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>
</head>
<style>
    h3 {
        text-align: center;
        color: orange;
    }
</style>
<body>


<nav class="navbar navbar-default " role="navigation">
   <%-- <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="<%=request.getContextPath() %>/"></a></li>
        <li role="presentation"><a href="<%=request.getContextPath() %>/order/orderList"></a></li>
    </ul>--%>
    <ul id="myTab" class="nav nav-tabs">
        <li class="active">
            <a href="#index" data-toggle="tab">洪武商城</a>
        </li>
        <li><a href="#orderInfo" data-toggle="tab">订单信息</a></li>
    </ul>
</nav>


<div id="myTabContent" class="tab-content">
    <div class="tab-pane fade in active" id="index">
        <h3>商品列表</h3>
        <table class="table">
            <tr>
                <td>产品编号</td>
                <td>产品名称</td>
                <td>产品价格</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${pList }" var="p">
                <tr>
                    <td>${p.id }</td>
                    <td>${p.name }</td>
                    <td>${p.price }</td>
                    <td>
                        <a class="btn btn-warning"
                           href="<%=request.getContextPath() %>/goConfirm?productId=${p.id }">购买</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="tab-pane fade" id="orderInfo">
        <h3>订单列表</h3>
        <table class="table">
            <tr>
                <td>订单号</td>
                <td>订单状态</td>
                <td>订单金额</td>
                <td>实际支付金额</td>
                <td>商品id</td>
                <td>购买个数</td>
                <td>创建时间</td>
                <td>支付时间</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${oList }" var="o">
                <tr>
                    <td>${o.orderNum }</td>
                    <td>${o.orderStatus==10?"待付款":o.orderStatus==20?"已付款":o.orderStatus==30?"已取消":o.orderStatus==40?"交易关闭":"已退款" }</td>
                    <td>${o.orderAmount }</td>
                    <td>${o.paidAmount }</td>
                    <td>${o.productId }</td>
                    <td>${o.buyCounts }</td>
                    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${o.createTime}" /></td>
                    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${o.paidTime}" /></td>
                    <td>

                        <a class="btn btn-warning" href="<%=request.getContextPath() %>/order/alipayOrderInfo?orderNum=${o.orderNum }">查看订单详情</a>
                        <c:if test="${o.orderStatus==10}">
                            <a class="btn btn-warning" href="<%=request.getContextPath() %>/goPay?orderId=${o.orderNum }">去付款</a>
                            <a class="btn btn-warning" href="<%=request.getContextPath() %>/order/clouseAlipayOrder?orderNum=${o.orderNum }">关闭交易</a>
                        </c:if>
                        <c:if test="${o.orderStatus==20}">
                            <a class="btn btn-warning" href="<%=request.getContextPath() %>/goRefund?orderNo=${o.orderNum }">退款</a>
                        </c:if>
                        <c:if test="${o.orderStatus==50}">
                            <a class="btn btn-warning" href="<%=request.getContextPath() %>/order/refundQuery?orderNo=${o.orderNum }">退款查询</a>
                        </c:if>


                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
