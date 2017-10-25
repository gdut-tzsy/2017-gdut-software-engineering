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
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/manager/companysrv/js/product.js?ver=<%=jsVer%>'></script>
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
            <!-- 订单支付 -->
            <div class="tab-main">
                <div class="buyvip-main">
                    <div class="buy-title">
                        <h1 class="buyvip-title">订单支付</h1>
                        <div class="buy-title-right fz16 c999">
                            <span class="orange">确认订单  >  付款  >  </span>填写发票
                        </div>
                    </div>

                   <%-- <h2 class="fz22">订单提交成功，请您尽快付款！</h2>--%>
                    <p class="fz18 c666 mb20">商品信息</p>
                    <div class='orderPay clearfix'>
                        <span>订单号：<span class="ml10" id="orderIdHtml"></span></span>
                        <span class="ml80">商品名称：<span class="ml10" id="productName"></span></span>
                        <span class="fr">应付金额：<span class="ml10 Amount" id="tradeAmount">￥0.00</span></span>
                    </div>

                    <form id="payForm" action="" method="post" onsubmit="return false;" templateId="default" dqdpCheckPoint="add_form">
                        <input type="hidden" id="orderId" name="orderId" value=""/>
                        <input type="hidden" id="amount" name="amount" value=""/>
                        <input type="hidden" id="priceCode" name="priceCode" value=""/>
                        <!-- 支付方式 -->
                        <%@include file="../include/payWay.jsp" %>
                    </form>
                    <!--支付按钮 -->
                    <div class="clear mt40" style="border-top:1px solid #e4e4e4;"></div>
                    <input type="button" class="payment-btn" value="立即支付" onclick="doPay();"/>

                    <%@include file="payMsgBox.jsp" %>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../include/footer.jsp" %>
</div>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>

<script language="javascript" type="text/javascript">
    $(document).ready(function(){
        $('#payForm').jCryption({
            submitElement: $('#jcAccountPayBtn'),
            beforeEncryption: function () {
                return true;
            },
            finishEncryption: function ($formElement, encrypted) {
                accountPaySubmit($formElement);
            }
        });
        getAccountInfo(orderDetail);
    });

    //查找订单详细
    function orderDetail(){
        var orderId=GetQueryString("orderId");
        $.ajax({
            type:"POST",
            url: baseURL+"/trademgr/trademgrAction!searchOrderInfo.action?orderId="+orderId,
            asyn: false,
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    var orderInfo=result.data.orderInfo;
                    var itemVO=orderInfo.itemList[0];
                    var productName = "";
                    for(var i=0;i<orderInfo.itemList.length;i++){
                        if(0==i){
                            productName = productName + orderInfo.itemList[i].itemName;
                        }else{
                            productName = productName + "；" + orderInfo.itemList[i].itemName;
                        }
                    }
                    //订单属性
                    $("#orderIdHtml").html(orderInfo.orderId);
                    $("#productName").html(productName);
                    $("#priceCode").val(itemVO.priceCode);
                    $("#tradeAmount").html("￥"+formatMoneyMethod(orderInfo.tradeAmount,2,100));
                    //设置隐藏属性
                    $("#orderId").val(orderInfo.orderId);
                    $("#amount").val(orderInfo.tradeAmount);
                    if(isLogin){
                        if(parseInt($("#amountAvalibleInput").val())<orderInfo.tradeAmount){
                            $("#payMethod_account").addClass("not-enough");
                            $(".not-enough-tips").show();
                        }
                    }
                }else{
                    _alert("错误提示",result.desc);
                }
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    }

    //检查支付参数
    function checkParams(){
        return true;
    }

    //直接支付
    function doPay(){
        //如果是账户支付
        if(!checkPayWay() | !checkParams()){
            return;
        }

        if($("#payWay").val()=="ACCOUNT"){
            //如果是账号支付,要输入支付密码
            //显示支付密码框
            cleanPw("passW");
            showPayInputBox($("#tradeAmount").html());
            $('#payPwdInputBox_ok').unbind("click");
            $("#payPwdInputBox_ok").on("click",function(){
                //验证密码并提交
                var pwd=getPw("passW");
                if (!(/^\d{6}$/).test(pwd)) {
                    $("#inputPwdTips").html("请输入6位数字密码");
                    $("#inputPwdTips").show();
                    return;
                }
                $("#payPwd").val(pwd);
                $("#payPwdInputBox").hide();
                $("#jcAccountPayBtn").click();
            });
        }else if($("#payWay").val()=="UNIONBANK" || $("#payWay").val()=="WECHAT" || $("#payWay").val()=="ALI"){
            window.open(baseURL+"/qiweipublicity/companysrv/pay/payTransfer.jsp");
            _alert("提示","请在新打开的页面中完成支付");
        }else{

        }
    }

    //弹出页面支付的回调函数
    function doOpenPay(openPage){
        var orderId=$("#orderId").val();
        var amount=$("#amount").val();
        var priceCode=$("#priceCode").val();
        if($("#payWay").val()=="UNIONBANK"){
            //成功直接调用银联接口
            var bizType=$("#bizType").val();
            unionPayURL=baseURL+"/trademgr/trademgrAction!payOrder.action?payParam.orderId="+orderId+"&payParam.amount="+amount+"&payParam.bizType="+bizType;
            openPage.location.href=unionPayURL;
        }else{
            $.ajax({
                type:"POST",
                url: baseURL+"/trademgr/trademgrAction!rePayOrder.action",
                data:{
                    "orderId":orderId,
                    "payWay":$("#payWay").val()
                },
                asyn: false,
                cache:false,
                dataType: "json",
                success: function(result){
                    if ("0" == result.code) {
                        if( $("#payWay").val()=="WECHAT"){
                            //微信支付要处理微信支付的链接
                            var wechatPayURL=baseURL+"/qiweipublicity/companysrv/pay/pay_wechat.jsp" +
                                    "?orderId="+orderId+"&amount="+amount+"&payURL="+encodeURIComponent(result.data.codeUrl)+
                                    "&productName="+encodeURIComponent(getProductName(priceCode));
                            openPage.location.href=wechatPayURL;
                        }else if( $("#payWay").val()=="ALI"){
                            //支付宝支付处理
                            var aliPayURL=baseURL+"/qiweipublicity/companysrv/pay/pay_ali.jsp" +
                                    "?orderId="+orderId+"&amount="+amount+"&payURL="+encodeURIComponent(result.data.codeUrl)+
                                    "&productName="+encodeURIComponent(getProductName(priceCode));
                            openPage.location.href=aliPayURL;
                        }
                    }else{
                        _alert("错误提示",result.desc);
                    }
                },
                error:function(){
                    _alert("","网络错误",1);
                }
            });
        }
    }

    //账户支付加密提交
    function accountPaySubmit($jcForm){
        if($jcForm){
            showLoading("正在提交...");
            $("#payForm").attr("action",baseURL+"/trademgr/trademgrAction!rePayOrder.action");
            var url=$("#payForm").attr("action");
            $jcForm.attr("action",url);
            $jcForm.ajaxSubmit({
                dataType:'json',
                async:false,
                success:function(result){
                    hideLoading();
                    if(result.code=="0"){
                        showPayResult("OK");
                    }else{
                        if("您的支付密码不正确"==result.desc){
                            showPayResult("FAIL",result.desc);
                            $("#resultOk").on("click",function(){
                                $("#payResultBox").hide();
                                showPayInputBox($("#tradeAmount").html());
                            });
                        }else{
                            _alert("提示",result.desc);
                        }
                    }
                },
                error:function(){
                    hideLoading();
                    _alert("提示信息","网络错误");
                }
            });
        }
    }
</script>