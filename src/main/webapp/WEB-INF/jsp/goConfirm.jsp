<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="<%=request.getContextPath() %>/static/js/jquery.min.js" type="text/javascript"></script>

<html>

<head>
    <title>订单确认页面</title>

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

<form action="<%=request.getContextPath() %>/alipay/createOrder" method="post">
    <input type="hidden" id="productId" name="productId" value="${p.id }"/>

    <table class="table">
        <tr>
            <td>产品编号: ${p.id }</td>
        </tr>
        <tr>
            <td>产品名称: ${p.name }</td>
        </tr>
        <td>产品价格: ${p.price }</td>
        <tr>
        </tr>
        <td>
            购买个数： <input id="buyCounts" name="buyCounts"/>
        </td>
        </tr>
        </tr>
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

    function createOrder() {
        $.ajax({
            url: hdnContextPath + "/createOrder",
            type: "POST",
            data: {
                "productId": $("#productId").val(),
                "buyCounts": $("#buyCounts").val()
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    // 提交订单成功后, 进入购买页面
                    window.location.href = hdnContextPath + "goPay?orderId=" + data.data;
                } else {
                    alert(data.msg);
                    console.log(JSON.stringify(data));
                }
            }
        });
    }

</script>

