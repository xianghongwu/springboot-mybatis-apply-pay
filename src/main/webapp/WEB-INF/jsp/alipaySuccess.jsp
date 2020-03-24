<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="<%=request.getContextPath() %>/static/js/jquery.min.js" type="text/javascript"></script>

<html>

<head>
    <title>支付成功</title>

    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.css">

</head>
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

<body>
<h1 style="color: green;">购买成功  同步回调</h1>
<table class="table">
    <tr>
        <td>
            订单编号: ${out_trade_no }
        </td>
    </tr>
    <td>
        支付宝交易号: ${trade_no }
    </td>
    <tr>
    </tr>
    <td>
        实付金额: ${total_amount }
    </td>
    <tr>
    </tr>
    <td>
        购买产品：${productName }
    </td>
    </tr>
</table>


    <a class="btn btn-warning"
       href="<%=request.getContextPath() %>/">再去逛逛</a>
</body>

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
</html>


