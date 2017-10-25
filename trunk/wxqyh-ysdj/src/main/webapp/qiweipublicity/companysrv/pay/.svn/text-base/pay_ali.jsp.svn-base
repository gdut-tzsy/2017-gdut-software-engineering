<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/fw.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/manager/js/jquery.qrcode.min.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
</head>
<body>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">

            <span>支付中心</span>
        </div>
    </div>
</div>
<div class="wrap-container FW">
    <div id="container" class="clearfix">
        <div class="place tleft fz14">

        </div>
        <div class="FW_content">
            <!-- 支付宝扫码支付 -->
            <div class="tab-main">
                <div class="buyvip-main" style="margin-bottom:40px;">
                    <div class="buy-title">
                        <h1 class="buyvip-title" style="margin-bottom:0px;">支付宝扫码支付</h1>
                        <%--                        <a class="buy-title-left fz16 orange" href="">
                                                    <span class="mr5">&lt;</span>选择其他支付方式
                                                </a>--%>
                        <div class="buy-title-right fz16 c999">
                            <span class="orange">确认订单</span><span class="orange ml5 mr5">  &gt;  付款  &gt;  </span>填写发票
                        </div>
                    </div>
                    <div class="WeChat-pay clearfix">
                        <div class="WeChat-pay-left mt20">
                            <div class="WeChat-pay-info">
                                <div class="mb10">
                                    <span>订单编号：</span>
                                    <span id="orderId"></span>
                                </div>
                                <div class="mb5">
                                    <span>商品名称：</span>
                                    <span class="red" id="productName"></span>
                                </div>
                                <div class="">
                                    <span>支付金额： </span>
                                    <span class="fz24 red" id="amount">￥0.00</span>
                                </div>
                            </div>
                            <div class="weixin-qrcode-content"></div>
                            <%-- <p class="fz14 c666">请使用<a href="" class="">支付宝</a>扫描二维码完成支付</p>--%>
                            <p class="fz14 c666">如已支付成功，可<a href="javascript:goOrderList();" style="text-decoration:underline">查询订单状态</a></p>
                        </div>
                        <div class="WeChat-pay-right">
                            <img src=" ../../../themes/manager/companysrv/images/img/ic_bigphone1.png" alt="" />
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <%@include file="../include/footer.jsp" %>
</div>
</body>
</html>


<script>
    $(function(){
        var payURL=decodeURIComponent("${param.payURL}");
        $('.weixin-qrcode-content').qrcode({width: 210,height: 210,text: payURL});
        var amount=getParam("amount");
        $("#amount").html(formatMoneyMethod(amount,2,100));
        var orderId=getParam("orderId");
        $("#orderId").html(orderId);
        var productName=decodeURIComponent(getParam("productName"));
        if(productName){
            $("#productName").html(productName);
        }else{
            $("#productName").html("");
        }
    });

    /**
     * 跳转到开票
     */
    function goInvoiceRequest(){
        document.location.href=baseURL+"/manager/companysrv/invoice_request.jsp?orderId="+getParam("orderId")+"&payWay=3&amount="+getParam("amount");
    }

    /**
     * 返回订单列表
     */
    function goOrderList(){
        document.location.href=baseURL+"/manager/companysrv/order_list.jsp";
    }
</script>