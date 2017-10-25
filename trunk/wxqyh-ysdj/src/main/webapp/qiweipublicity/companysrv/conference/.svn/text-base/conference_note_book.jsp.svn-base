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
    <meta name="keywords" content="企业微信,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,企业微信第三方,企业号服务商">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <%--<script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>--%>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <style type="text/css">
        html{min-width: 1200px;}
    </style>
</head>
<body>
<!-- 头部 -->
<div id="header">
    <div class="header-nav clearfix">
        <div class="header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>企微会议平板</span>
        </div>
    </div>
</div>
<!--banner-->
<div class="banner hypb_banner">
</div>
<!-- 主体部分 -->
<div id="content">
    <!-- 购买微信智能考勤轮 -->
    <div class="introduceItem">
        <h3>购买企微会议平板</h3>
        <div class="buySpace clearfix mt30" style="border:1px solid #e4e4e4;">
            <div class="fl w550 hypb_wrap">
                <div class="ml40 mt60 h90">
                    <div class="hypb_msg fl">选择设备：</div>
                    <ul class="item fl equipment_list w400">
                        <li class="on" promprice="1499900">C65ME</li>
                        <li promprice="2699900">C75ME</li>
                        <li promprice="6999900">C84ME</li>
                        <li promprice="30000">智能白板笔</li>
                        <li promprice="99900">无线传屏</li>
                        <li promprice="150000">移动支架</li>
                    </ul>
                </div>
                <div class=" mt40 ml68">
                    <div class=" mb40" style="position:relative;">
                        <span class="hypb_msg">数量：</span>
                        <%@include file="../../../manager/companysrv/include/number.jsp" %>
                        <span class="fz16 mr10 ml5">个</span>
                        <span class="Amount" id="amount">￥0,00</span>
                        <span class="fz14 c999" id="formula" style="vertical-align: middle">（￥199 / 个）</span>
                        <div class="check-tips ml60" id="buyNumTips" style="display:none;"><span>!</span>请正确输入数量</div>
                    </div>
                    <div class="ml60">
                        <div class=" mb40" style="position:relative">
                            <input type="text" class="w385 txt" placeholder="输入企业corpid" id="corpId" onblur="getOrgInfo();"/>
                            <div class="check-tips" id="corpIdTips">企微欢迎你</div>
                        </div>
                        <input type="button" class="o-btn w200 mb30 h40" value="购买" onclick="goBuyhypb()"/>
                        <p class="pb60">购买说明：<br>
                            申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会
                            <br/>在7个工作日内将发票寄出</p>
                    </div>
                </div>
            </div>
            <div class="buySpace-right" style="border:none;">
                <img src="../../../themes/manager/companysrv/images/img/hypb_goods.png" class="ml30" alt="" />
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="introduceItem mt145">
        <div class="hypb_img_wrap">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce1.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce2.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce3.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce4.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce5.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce6.jpg" alt="">
            <img src="../../../themes/manager/companysrv/images/img/hypb_introduce7.jpg" alt="">
        </div>
    </div>
    <%@include file="../include/moresrv.jsp" %>
</div>

<%@include file="../include/footer.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
<script>
    $(function(){
        var header = $("#header")
        $(window).bind("scroll",function() {
            if($(window).scrollTop()>450){
                $("#header").stop().animate({top:0},500);
            }else{
                $("#header").stop().animate({top:-70+"px"},500);
            }
        });
        positiveIpt(calPrice);

        $(".equipment_list").on("click","li",function(){
            $(".equipment_list li").removeClass("on");
            $(this).addClass("on");
            calPrice($("#buyNum").val());
        })
    });

    var vipCode="noVip";//记录VIP等级
    $(document).ready(function(){
        calPrice($("#buyNum").val());
        $.ajax({
            type:"POST",
            url: baseURL+"/accountmgr/accountmgrAction!getOrgInfo.action",
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    //设置corpId
                    var corpId=result.data.corpId;
                    if(corpId){
                        $("#corpId").val(corpId);
                    }
                    $("#corpIdTips").html(result.data.orgName);
                    //设置VIP等级
                    vipCode=result.data.vipCode;
                }
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    });

    function getOrgInfo(callback){
        var corpId=$("#corpId").val();
        $.ajax({
            type:"POST",
            url: baseURL+"/accountmgr/accountOpenAction!getOrgInfoByCorpId.action",
            data:{
                "corpId":corpId
            },
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    //设置corpId
                    var VipVO=result.data.VipVO;
                    if(VipVO){
                        vipCode=VipVO.vipCode;
                    }
                    $("#corpIdTips").html(result.data.orgName);
                    if(callback){
                        callback.call(callback, null, null);
                    }
                }else{
                    $("#corpIdTips").html("<span>!</span>"+result.desc);
                    $("#corpIdTips").show();
                    vipCode="noVip";
                }
                calPrice($("#buyNum").val());
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    }

    function calPrice(buyNum){
        //价差空间输入
        //var buyNum= $.trim($("#buyNum").val());

        if(!checkNum(buyNum)){
            $("#amount").hide();
            //$("#formula").hide();
            $("#buyNumTips").show();
            return;
        }
        $("#buyNumTips").hide();

        var unitPrice=1499900;
        var len = $(".equipment_list li").length;
        for(var i=0;i<len;i++){
            if($(".equipment_list li").eq(i).hasClass("on")){
                unitPrice =parseInt( $(".equipment_list li").eq(i).attr("promprice"));
            }
        }
        var sumAmount=unitPrice*buyNum;
        $("#amount").html("￥ "+formatMoneyMethod(sumAmount,0,100));
        $("#amount").show();
        $("#formula").html("（￥"+(unitPrice/100)+" / 个）");
        /*        $("#formula").html("("+formatMoneyMethod(unitPrice,0,100)+"X"+buyNum+"个)");
         $("#formula").show();*/
    }

    function goBuyhypb(){
        //验证corpId的真确性才提交
        getOrgInfo(function(){
            //先验证参数是否正确才提交
            var buyNum= $.trim($("#buyNum").val());
            if(!checkNum(buyNum)){
                $("#amount").hide();
                $("#buyNumTips").show();
                return;
            }
            $("#buyNumTips").hide();

            var len = $(".equipment_list li").length;
            var serviceId="cvtouch";
            var priceCode="cvtouch_65";
            var buyNum=$("#buyNum").val();
            var corpId=$("#corpId").val();
            var currentIndex = 0;
            for(var i=0;i<len;i++){
                if($(".equipment_list li").eq(i).hasClass("on")){
                    currentIndex = i;
                }
            }
            switch(currentIndex){
                case 0:
                    serviceId = "cvtouch";
                    priceCode = "cvtouch_65";
                    break;
                case 1:
                    serviceId = "cvtouch";
                    priceCode = "cvtouch_75";
                    break;
                case 2:
                    serviceId = "cvtouch";
                    priceCode = "cvtouch_84";
                    break;
                case 3:
                    serviceId = "cvtouchpen";
                    priceCode = "cvtouchpen";
                    break;
                case 4:
                    serviceId = "cvtouchwifi";
                    priceCode = "cvtouchwifi";
                    break;
                case 5:
                    serviceId = "cvtouchsupport";
                    priceCode = "cvtouchsupport";
                    break;
            }

            document.location.href=baseURL+"/qiweipublicity/companysrv/order/order_confirm.jsp?" +
                    "serviceId="+serviceId+"&buyNum="+buyNum+"&corpId="+corpId+"&priceCode="+priceCode;
        });
    }
</script>