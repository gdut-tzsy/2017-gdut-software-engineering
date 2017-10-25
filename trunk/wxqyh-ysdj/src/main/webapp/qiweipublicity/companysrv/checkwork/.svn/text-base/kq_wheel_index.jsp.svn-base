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
    <meta name="keywords" content="微信企业号,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,微信企业号第三方,企业号服务商">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <script>document.location.href = baseURL+"/qiweipublicity/companysrv/checkwork/kq_machine_index.jsp"</script>
    <style type="text/css">
      html{min-width: 1200px;}
    </style>
</head>
<body>
<!-- 头部 -->
<%--<div id="header">
    <div class="header-nav clearfix">
        <div class="header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>企微技术服务</span>
        </div>
    </div>
</div>--%>
<!--banner-->
<div class="banner checkWork-l-banner">
    <div class="banner-main">
        <p class="cw-b-info">使用更精准的iBeacon微定位技术，智能硬件配套免费云服务应用，微信摇一摇即可
            <br/>会议签到、考勤打卡，现购买额外赠送1G储存空间</p>
    </div>
</div>
<!-- 主体部分 -->
<div id="content">
    <!-- 购买微信智能考勤轮 -->
    <div class="introduceItem">
        <h3>购买微信智能考勤轮</h3>
        <div class="buySpace clearfix mt30" style="border:1px solid #e4e4e4;">
            <div class="buySpace-left mt40 ml50">
                <div class=" mb40" style="position:relative;">
                    <%@include file="../../../manager/companysrv/include/number.jsp" %>
                    <span class="fz16 mr20 ml5">个</span>
                    <span class="Amount" id="amount">￥0,00</span>
                    <span class="fz14 c999" id="formula">（优惠价 ￥199 / 个）</span>
                    <div class="check-tips" id="buyNumTips" tyle="display:none;"><span>!</span>请正确输入数量</div>
                </div>
                <div class=" mb40" style="position:relative">
                    <input type="text" class="w400 txt" placeholder="输入企业corpid" id="corpId" onblur="getOrgInfo();"/>
                    <div class="check-tips" id="corpIdTips"></div>
                </div>
                <input type="button" class="o-btn w200 mb30 h40" value="购买" onclick="goBuyCwwheel()"/>
                <p class="pb60">购买说明：<br>
                    1. 需要认证的企业微信才能使用考勤轮<br>
                    2. 每购买一个考勤轮，赠送1G存储空间<br>
                    3. 申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会
                    <br/>&nbsp;&nbsp;&nbsp;&nbsp;在7个工作日内将发票寄出<br>
                    4. 目前微信智能考勤轮仅支持原有企业号用户使用，下单前请咨询企微技术支持确认是否能够使用
                </p>
            </div>
            <div class="buySpace-right" style="border:none;">
                <img src="../../../themes/manager/companysrv/images/img/ic-kql.png?ver=2" class="ml30" alt="" />
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="introduceItem">
        <h3>应用场景</h3>
        <p class="fz16 c999" style="text-align:center">基于iBeacon微定位技术的新一代智能办公硬件</p>
        <div class=" clearfix pl50 pr50 mt40">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql01.jpg" alt="" class="fl" />
            <img src="../../../themes/manager/companysrv/images/img/ic-kql02.jpg" alt="" class="fr" />
        </div>
    </div>
    <div class="introduceItem">
        <h3>产品特点</h3>
        <p class="fz16 c999" style="text-align:center">颠覆传统考勤，新一代智能签到神器</p>
        <div class="prd-td clearfix" style="margin-left:70px;">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql01.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">摇一摇考勤</p>
                <p class="fz14 c999 mb15">可互动的固定区域打卡<br />方式</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql02.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">门店统一管理</p>
                <p class="fz14 c999 mb15">多门店打卡，总部统一<br />管理</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql03.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">复杂班次设计</p>
                <p class="fz14 c999 mb15">多班次考勤后台设置<br />更方便</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql04.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">多地点考勤</p>
                <p class="fz14 c999 mb15">外勤人员移动考勤管理<br />更简单</p>
            </div>
        </div>
        <div class="prd-td clearfix" style="margin-left:70px;">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql05.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">个性化考勤规则</p>
                <p class="fz14 c999 mb15">考勤规则可自定义，设置<br />更灵活</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql06.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">签到签退提醒</p>
                <p class="fz14 c999 mb15">上下班考勤提醒，避免<br />忘打卡</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql07.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">查看打卡记录</p>
                <p class="fz14 c999 mb15">员工可在微信端查看个人<br />打卡记录</p>
            </div>
        </div>
        <div class="prd-td clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-kql08.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt5 fz18">自动生成报表</p>
                <p class="fz14 c999 mb15">数据后台实时统计，自动<br />生成考勤报表</p>
            </div>
        </div>
        <div class="clear"></div>
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

        var unitPrice=19900;
        var sumAmount=unitPrice*buyNum;
        $("#amount").html("￥ "+formatMoneyMethod(sumAmount,0,100));
        $("#amount").show();
/*        $("#formula").html("("+formatMoneyMethod(unitPrice,0,100)+"X"+buyNum+"个)");
        $("#formula").show();*/
    }

    function goBuyCwwheel(){
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

            var serviceId="cwwheel";
            var buyNum=$("#buyNum").val();
            var corpId=$("#corpId").val();
            var priceCode="cwwheel_normal";
            document.location.href=baseURL+"/qiweipublicity/companysrv/order/order_confirm.jsp?" +
                    "serviceId="+serviceId+"&buyNum="+buyNum+"&corpId="+corpId+"&priceCode="+priceCode;
        });
    }
</script>