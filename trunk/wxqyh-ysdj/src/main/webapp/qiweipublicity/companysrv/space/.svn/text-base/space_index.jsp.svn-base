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
    <meta name="description" content="企微云空间，解决海量数据储存|企微云平台">
    <meta name="keywords" content="企业微信,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,企业微信第三方,企业号服务商">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/fw.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
    <script src="${baseURL}/js/wx/wxAPi.js?ver=<%=jsVer%>"></script>
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
            <span>企微云空间</span>
        </div>
    </div>
</div>--%>
<!--banner-->
<div class="banner cloudspace-banner">
    <div class="banner-main">
        <div class="spaceUsage">
            <div class="spaceUsage-top">
                <div class="ring-left"></div>
                <div class="ring-right"></div>
                <div class="rings-left"></div>
                <div class="rings-right"></div>
                <div class="ring-info">
                    <p class="fz20">空间使用量(%)</p>
                    <p class="fz100" id = "spaceVolume">0</p>
                    <%--<span class="fz36">%</span>--%>
                </div>
            </div>
        <span class="cfff fz20">
          总空间 <span class="orange" id="sumSpace">0</span> G，剩余 <span class="orange" id="leftSpace">0</span> G
        </span>
        </div>
    </div>

</div>
<!-- 主体部分 -->
<div id="content">
    <!-- 购买空间 -->
    <div class="introduceItem">
        <h3>购买空间</h3>
        <p class="fz16 c999" style="text-align:center">（价格：100元/G/年）</p>
        <div class="buySpace clearfix mt30" style="border:1px solid #e4e4e4;">
            <div class="buySpace-left mt40 ml60">
                <div class=" mb30" style="position:relative">
                    <input type="text" class="w400 txt" placeholder="输入企业CorpID" id="corpId" onblur="getOrgInfo();"/>
                    <div class="check-tips none" id="corpIdTips"></div>
                </div>
                <div>
                    <a href="javascript:showAcountBox();" class="fz14 ml10 mr10" style="text-decoration:underline;">使用企微管理账号获取</a><span class="c999 fz14">（也可在企业微信或企微管理后台中复制）</span>
                </div>
                <div class=" mb40" style="position:relative;">
                    <%@include file="../../../manager/companysrv/include/number.jsp" %>
                    <span class="Amount none" id="amount">￥ 0,00</span>
                    <span class="fz14 c999 none" id="formula">（每购买10G空间立送1G空间）</span>
                    <div class="check-tips none" id="spaceTips"><span>!</span>空间数必须大于 <em>0</em> 的整数</div>
                </div>
                <input type="button" class="o-btn w200 mb40 h40" value="购买" onclick="goBuySpace()" />
            </div>
            <div class="buySpace-right">
                <p>购买说明：<br>
                    1. 购买成功后，空间将会自动增加到您的企微帐号<br>
                    2. 储存空间有效期为一年，到期后数据仍保留，需续费方可继续使用<br>
                    3. 每购买10G空间立送1G空间，多买多送。赠送空间将在1个工作日内到账<br>
                    4. 申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会在7个工作<br>
                        &nbsp;&nbsp;&nbsp;&nbsp;日内将发票寄出<br>
                </p>
            </div>
        </div>
    </div>
    <div class="introduceItem">
        <h3>其他途径获得空间</h3>
        <div class="channel clearfix" style="margin-left:80px;">
            <img src="../../../themes/manager/companysrv/images/img/ic-bg1.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt20 fz18">成为银卡VIP</p>
                <p class="fz14 c999 mb15">获取4G额外储存空间</p>
                <a href="http://wbg.do1.com.cn/ask/pingtaifeiyong/2015/0410/249.html" target="_blank" class="bf7774a">了解详细</a>
            </div>
        </div>
        <div class="channel clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-bg2.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt20 fz18">分享使用案例</p>
                <p class="fz14 c999 mb15">获取2G额外储存空间</p>
                <a href="http://mp.weixin.qq.com/s?__biz=MzA3ODk1MDcwMw==&mid=215781677&idx=1&sn=27022eab7bd7659b5e43bfa6f23225f3#rd" target="_blank" class="b92cf68">了解详细</a>
            </div>
        </div>
        <div class="channel clearfix">
            <img src="../../../themes/manager/companysrv/images/img/ic-bg3.png" alt="" class="fl" />
            <div class="fl">
                <p class="mt20 fz18">参加企微各类活动</p>
                <p class="fz14 c999 mb15">赢取免费额外空间</p>
                <a href="http://wbg.do1.com.cn/QWinformation/News/" target="_blank" class="b58b5e1">了解详细</a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <%@include file="../include/moresrv.jsp" %>
</div>

<%@include file="../include/footer.jsp" %>
<%@include file="../include/accountMsgBox.jsp" %>
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
        lessNum = 5; //最低为5G
        $("#buyNum").val(10);//初始化10G
        positiveIpt(calPrice);
    });
    function spaceUsage(nb){
        $(".rings-right").attr("style","transform:rotateZ(-"+nb/50*180+"deg)")
        if(nb>50){
            $(".rings-right").attr("style","transform:rotateZ(-"+180+"deg)")
            //$(".rings-left").css({"z-index":21})
            $(".rings-left").attr("style","transform:rotateZ(-"+(nb-50)/50*180+"deg);z-index:21")
        }
    }


    $(document).ready(function(){
        init();
        calPrice($("#buyNum").val());
    });

    var vipCode="noVip";
    function init(){
        $.ajax({
            type:"POST",
            url: baseURL+"/accountmgr/accountmgrAction!getOrgSpace.action",
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    //设置空间
                    setOrgSpace(result.data.FileStatisticsVO);
                    //设置corpId
                    var corpId=result.data.corpId;
                    if(corpId){
                        $("#corpId").val(corpId);
                    }
                    $("#corpIdTips").html(result.data.orgName);
                    //设置VIP等级
                    vipCode=result.data.vipCode;
                }else{
                    //如果报错，显示空间为0
                    if("800000003"==result.code){
                        //没有登录
                        $("#spaceVolume").text(0);
                        spaceUsage($("#spaceVolume").text());
                    }
                }
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    }

    //设置空间
    function setOrgSpace(FileStatisticsVO){
        if(FileStatisticsVO){
            $("#sumSpace").html((FileStatisticsVO.sumSpace/1024).toFixed(2));
            $("#leftSpace").html((FileStatisticsVO.canuseSpace/1024).toFixed(2));
            if(FileStatisticsVO.cannotusePercent>=0){
                $("#spaceVolume").text(FileStatisticsVO.cannotusePercent.toFixed(0));
            }else{
                $("#spaceVolume").text("100");
            }
            spaceUsage($("#spaceVolume").text());
            $("#corpIdTips").show();
        }else{
            $("#spaceVolume").text(0);
            spaceUsage($("#spaceVolume").text());
        }
    }

    //获取corpId等级
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
                    setOrgSpace(result.data.FileStatisticsVO);
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
        //var buyNum=$("#buyNum").val();
        if(!checkNum(buyNum)){
            $("#spaceTips").show();
            $("#amount").hide();
            $("#spaceTips").html("<span>!</span>空间数必须大于 <em>0</em> 的整数");
            return;
        }

        var unitPrice=10000;
        var sumAmount=unitPrice*buyNum;
        $("#amount").html("￥ "+formatMoneyMethod(sumAmount,0,100));
        $("#amount").show();
        //$("#formula").html("("+formatMoneyMethod(unitPrice,0,100)+"X"+buyNum+"G)");
        $("#formula").show();
        //判断赠送了多少空间
        var giveNum=Math.floor(buyNum/10);
        if(giveNum>0){
            $("#spaceTips").html("额外赠送<em class='orange'>"+giveNum+"G</em>空间");
            $("#spaceTips").show();
        }else{
            $("#spaceTips").hide();
        }
    }

    function goBuySpace(){
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

            var serviceId="space";
            var corpId=$("#corpId").val();
            var priceCode="space";
            document.location.href=baseURL+"/qiweipublicity/companysrv/order/order_confirm.jsp?" +
                    "serviceId="+serviceId+"&buyNum="+buyNum+"&corpId="+corpId+"&priceCode="+priceCode;
        });
    }

    function showAcountBox(){
        $("#accountBox").attr("okCallback","searchCorpIdByAccountOKCallback");
        $("#overlayDiv").show();
        $("#accountBox").show();
    }
    function searchCorpIdByAccountOKCallback(corpId){
        $("#corpId").val(corpId);
        getOrgInfo();
    }
</script>