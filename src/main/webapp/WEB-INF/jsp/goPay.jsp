<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="<%=request.getContextPath() %>/static/js/jquery.min.js" type="text/javascript"></script>

<html>

<head>
    <title>付款页面</title>

    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.css">
</head>

<body>
<nav class="navbar navbar-default " role="navigation">
<div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#" style="background: pink;">洪武商城</a>
    </div>

</div>
</nav>

<form id="payForm" action="<%=request.getContextPath() %>/alipay/goAlipay" method="post">
    <input type="hidden" name="orderId" value="${order.orderNum }"/>
    <table class="table">
        <tr>
            <td>
                订单编号: ${order.id }
            </td>
        </tr>
        <tr>
            <td>
                订单id: ${order.orderNum }
            </td>
        </tr>
        <td>
            产品名称: ${p.name }
        </td>
        <tr>
        </tr>
        <td>
            订单价格: ${order.orderAmount }
        </td>
        <tr>
        </tr>
        <td>
            购买个数：${order.buyCounts }
        </td>
        </tr>
        </tr>
        <td>
            <input  class="tn btn-info" type="submit" value="前往支付宝进行支付">

            <input class="tn btn-success"  type="button" value="微信扫码支付" onclick="wxpayDisplay()">
        </td>
        </tr>
    </table>
</form>


<input type="hidden" id="hdnContextPath" name="hdnContextPath" value="<%=request.getContextPath() %>"/>


<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">

            <a class="navbar-brand" href="#" style="background: pink;">Burst No Ball</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="background: cornsilk;">

            <div style="line-height: 50px; text-align: center;font-size: 30px;">
                洪武商城
            </div>
        </div>
    </div>
</nav>
</body>

</html>


<script type="text/javascript">

    function wxpayDisplay() {
        debugger;

        var hdnContextPath = $("#hdnContextPath").val();

        $("#payForm").attr("action", hdnContextPath + "/wxpay/createPreOrder");
        $("#payForm").submit();
    }

    // 	$(document).ready(function() {

    // 		var hdnContextPath = $("#hdnContextPath").val();

    // 	});


</script>

