<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="<%=request.getContextPath() %>/static/js/jquery.min.js" type="text/javascript"></script>

<html>

<head>
    <title>退款页面</title>
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

<form id="myForm" action="<%=request.getContextPath() %>/order/refund" method="post">
    <input type="hidden" id="orderNo" name="orderNo" value="${order.orderNum }"/>

    <table class="table">
        <tr>
            <td>退款金额: <input id="amount" name="amount" value="${order.paidAmount}" readonly/></td>
        </tr>
        <tr>
            <td>退款原因: <input id="refundReason" name="refundReason"/></td>
        </tr>
        <tr>
            <td>
                <input class="btn btn-warning" type="button" value="ajax提交，生成订单" onclick="createOrder()"/>
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
    var hdnContextPath = $("#hdnContextPath").val();
    var ajaxUrl= $("#myForm").attr("action");
    function createOrder() {
        $.ajax({
            url: ajaxUrl,
            type: "POST",
            data: $("#myForm").serialize(),
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    // 提交订单成功后, 进入购买页面
                    alert("退款提交成功，详情请查看退款详情");
                    window.location.href = hdnContextPath + "/index";
                } else {
                    alert(data.message);
                    console.log(JSON.stringify(data));
                }
            }
        });
    }

</script>

