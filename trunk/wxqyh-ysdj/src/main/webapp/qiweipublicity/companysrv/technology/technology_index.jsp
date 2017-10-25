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
    <meta name="description" content="一对一技术服务，专属顾问为你服务|企微云平台">
    <meta name="keywords" content="企业微信,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,企业微信第三方,企业号服务商">
    <title>企微云平台-一对一技术服务 | 企业微信官方推荐企业云办公第一品牌 | 微信办公</title>
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
</head>
<body>
<!-- 头部 -->
<div id="header">
    <div class="header-nav clearfix">
        <div class="header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>企微技术服务</span>
        </div>
    </div>
</div>
<!--banner-->
<div class="banner technicalService-banner">


</div>
<div class="technicalService-flow"><img src="../../../themes/manager/companysrv/images/img/technicalService-flow.png" width="1200" height="119" /></div>
<!-- 主体部分 -->
<div id="linkMark"></div>
<div id="content">
    <!-- 购买一对一远程技术服务 -->
    <div class="introduceItem pb30">
        <h3>购买一对一远程技术服务</h3>
        <p class="fz16 c999" style="text-align:center">（单位：元 / 年）</p>
        <%--<table width="1100px">
            <tr height="50px">
                <td></td>
                <td>普通会员</td>
                <td>银卡VIP会员</td>
                <td>金卡VIP会员</td>
                <td>白金VIP会员</td>
                <td>钻石VIP会员</td>
                <td>白钻VIP会员</td>
            </tr>
            <tr height="50px">
                <td>价格</td>
                <td class="cff4e00">￥5,999.00</td>
                <td class="cff4e00">￥4,999.00</td>
                <td class="cff4e00">￥3,999.00</td>
                <td class="cff4e00">￥2,999.00</td>
                <td class="cff4e00">免费</td>
                <td class="cff4e00">免费</td>
            </tr>
        </table>--%>
        <div class="buySpace clearfix" style="border: none;border-bottom: 1px #ddd solid;">
            <div class="buySpace-left mt40 ml50">
                <div class="mb30" style="position:relative">
                    <input type="text" class="w242 txt" placeholder="输入企业CorpID" id="corpId" onblur="getOrgInfo();"/>
                    <div class="ml10 check-tips none"  id="corpIdTips"></div>
                </div>
                <div>
                    <a href="javascript:showAcountBox();" class="fz14 ml10 mr10" style="text-decoration:underline;">使用企微管理账号获取</a><span class="c999 fz14">（也可在企业微信或企微管理后台中复制）</span>
                </div>
                <div class="mt20 mb15 ml10" style="position:relative;">
                    <%@include file="../../../manager/companysrv/include/number.jsp" %>
                    <span class="Amount" id="amount">￥ 0,00</span>
                    <div class="check-tips none" id="buyNumTips"><span>!</span>购买数量必须大于 <em>0</em> 的整数</div>
                </div>
                <input type="button" class="o-btn w200 mb30 h40" value="购买" onclick="goBuy()"/>
            </div>
            <div class="buySpace-right" style="margin-top: 55px;">
                <%--<p>使用说明：<br>
                    1. 购买成功后，企微工作人员将在第一时间联系您，进行相关服务工作的对接。<br>
                    2. 申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会在7个工作<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;日内将发票寄出<br>--%>
                <p>
                    使用说明：<br>
                    1. 购买成功后，企微技术顾问将在一个工作日内联系您，进行相关服务工作的对接，期间请保持联系方式的畅通。<br>
                    2. 如需发票，请在企业服务模块下的发票管理页面，填写相关信息，企微工作人员会在三个工作日内将发票寄出。<br>
                    3. 本服务为远程在线技术服务，不涉及VIP功能特权、现场指导及定制部署等服务内容。
                </p>
            </div>
        </div>
    </div>
    <%--<div class="introduceItem">
        <h3>四大服务内容</h3>
        <p class="fz16 c999" style="text-align:center">本服务为远程非定制技术服务，不涉及VIP服务，现场指导及定制部署等服务内容</p>
        <ul class="service-list clearfix">
            <li class="mr85">
                <img src="../../../themes/manager/companysrv/images/img/ic-bg7.png" alt="" class="" />
                <p class="fz18 mt20 pb5">企业微信代申请及认证</p>
                <span class="fz14 c999">解决申请认证流程麻烦，手续繁多的问题</span>
            </li>
            <li class="mr85">
                <img src="../../../themes/manager/companysrv/images/img/ic-bg6.png" alt="" class="" />
                <p class="fz18 mt20 pb5">应用安装上线</p>
                <span class="fz14 c999">远程为您的企业安装合适的功能应用</span>
            </li>
            <li class="mr85">
                <img src="../../../themes/manager/companysrv/images/img/ic-bg5.png" alt="" class="" />
                <p class="fz18 mt20 pb5">管理员远程在线培训</p>
                <span class="fz14 c999">平台应用功能深入浅出的远程培训指导</span>
            </li>
            <li class="">
                <img src="../../../themes/manager/companysrv/images/img/ic-bg4.png" alt="" class="" />
                <p class="fz18 mt20 pb5">一年专职技术支持服务</p>
                <span class="fz14 c999">专职技术人员365天全方位的平台疑难解决</span>
            </li>
        </ul>
    </div>
    <div class="introduceItem">
        <h3>服务流程</h3>
        <div class="flow-list clearfix">
            <div class="s-flow clearfix">
                <div class="serialNumber">1</div>
                <p class="fz18 pt10 pb10">购买服务</p>
                <span class="fz14 c999">通过在线购买此服务</span>
            </div>
            <div class="s-flow clearfix">
                <div class="serialNumber">2</div>
                <p class="fz18 pt10 pb10">企微顾问对接</p>
                <span class="fz14 c999">企微安排技术顾问与客户对接</span>
            </div>
            <div class="s-flow clearfix mr0">
                <div class="serialNumber">3</div>
                <p class="fz18 pt10 pb10">实施远程服务</p>
                <span class="fz14 c999">企微技术顾问与客户深入沟通，实施服务</span>
            </div>
        </div>
    </div>--%>
    <div class="introduceItem">
        <h3>四大服务内容</h3>
        <div class="technicalServiceItem clearfix">
            <div class="technicalServiceImg fl" style="margin-left: -45px;"><img src="../../../themes/manager/companysrv/images/img/technicalServiceItemImg1.png"/></div>
            <div class="technicalServiceText mt15 fr">
                <h4 style="color: #2ab3f3;">企业微信代申请及认证</h4>
                <h5 class="fz18 c666 mt5">由专人协助完成企业微信申请及认证、人员扩容等相关服务</h5>
                <h5 class="fz18 mt30" style="color: #3fbff8;">认证企业微信有什么好处？</h5>
                <p>· 企业微信人数可突破200人</p>
                <p>· 可使用企业微信APP</p>
                <p>· 可使用摇一摇签到</p>
                <p>· 可使用微信支付发放红包及收款</p>
                <p>· 企业微信认证专属标识</p>
            </div>
        </div>
        <div class="technicalServiceItem clearfix">
            <div class="technicalServiceText technicalServiceTextFl fl">
                <h4 style="color: #ffaa1f;">平台配置及应用上线</h4>
                <h5 class="fz18 c666 mt5">根据你的需求提供完整的企微云平台的使用方案<br>并由专人完成平台基础配置和应用安装设置</h5>
                <h5 class="fz18 mt30" style="color: #ffa91e;">服务内容：</h5>
                <p>· 企微云平台应用托管与设置</p>
                <p>· 通讯录设置与人员导入</p>
                <p>· 各级管理员的角色与账户设置</p>
                <p>· 企微云考勤机及摇一摇考勤的配置</p>
            </div>
            <div class="technicalServiceImg technicalServiceImgFl fr"><img src="../../../themes/manager/companysrv/images/img/technicalServiceItemImg2.png"/></div>
        </div>
        <div class="technicalServiceItem clearfix">
            <div class="technicalServiceImg fl" style="margin-left: -45px;"><img src="../../../themes/manager/companysrv/images/img/technicalServiceItemImg3.png"/></div>
            <div class="technicalServiceText fr" style="margin-top: 45px;">
                <h4 style="color: #26ae7b;">管理员远程在线培训</h4>
                <h5 class="fz18 c666 mt5">由专人为你的企微管理员提供深入浅出的远程培训指导</h5>
                <h5 class="fz18 mt30" style="color: #26ae7b;">我们为你提供以下多种培训方式：</h5>
                <p>· 完整版操作手册使用教学</p>
                <p>· 远程在线使用讲解</p>
                <p>· 在线视频培训讲座</p>
            </div>
        </div>
        <div class="technicalServiceItem clearfix" style="border: none;">
            <div class="technicalServiceText technicalServiceTextFl fl" style="margin-top: 85px;">
                <h4 style="color: #ec5d5c;">365天专属服务顾问</h4>
                <h5 class="fz18 c666 mt5">在服务期间，将由专属服务顾问为您提供365天全方位<br>全平台的技术支持服务，为您优先解决各种使用问题。</h5>
            </div>
            <div class="technicalServiceImg technicalServiceImgFl fr"><img src="../../../themes/manager/companysrv/images/img/technicalServiceItemImg4.png"/></div>
        </div>
    </div>
    <div class="technicalServiceBottom">
        <a href="#linkMark">立即购买</a>
        <p>获得365天的全方位一对一技术服务</p>
    </div>
    <div class="technical_service">
        <h3>更多选择：企微定制培训服务</h3>
        <ul class="clearfix">
            <li>
                <p>提交培训需求</p>
                <span>安排对接讲师</span>
            </li>
            <li>
                <p>定制培训内容</p>
                <span>包括但不限于：员工培<br>训、管理员培训、IT培训</span>
            </li>
            <li>
                <p>签订培训合同</p>
                <span>确认培训计划</span>
            </li>
            <li>
                <p>正式培训</p>
                <span>包含后续支持</span>
            </li>
        </ul>
    </div>
</div>
<div class="introduceItemMore" style="margin: -15px 0 20px;">
    <%@include file="../include/moresrv.jsp" %>
</div>
<%@include file="../include/footer.jsp" %>
<%@include file="../include/accountMsgBox.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
<script>
    $(function(){
        var header = $("#header");
        $(window).bind("scroll",function() {
            if($(window).scrollTop()>450){
                $("#header").stop().animate({top:0},500);
            }else{
                $("#header").stop().animate({top:-70+"px"},500);
            }
        });
    });

    $(document).ready(function(){
        init();
        positiveIpt(calPrice);
        calPrice();
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
                    //设置corpId
                    var corpId=result.data.corpId;
                    if(corpId){
                        $("#corpId").val(corpId);
                    }
                    $("#corpIdTips").html(result.data.orgName);
                    $("#corpIdTips").show();
                    //设置VIP等级
                    vipCode=result.data.vipCode;
                }
            },
            error:function(){
                _alert("","网络错误",1);
            }
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
                    $("#corpIdTips").html(result.data.orgName);
                    $("#corpIdTips").show();
                    if(callback){
                        callback.call(callback, null, null);
                    }
                }else{
                    $("#corpIdTips").html("<span>!</span>"+result.desc);
                    $("#corpIdTips").show();
                }
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    }

    function calPrice(){
        var buyNum=$("#buyNum").val();
        if(!checkNum(buyNum)){
            $("#buyNumTips").show();
            $("#amount").hide();
            $("#buyNumTips").html("<span>!</span>购买数量必须大于 <em>0</em> 的整数");
            return;
        }

        var unitPrice=899900;
        var sumAmount=unitPrice*buyNum;
        $("#amount").html("￥ "+formatMoneyMethod(sumAmount,0,100));
        $("#amount").show();
        //判断赠送了多少空间
        var giveNum=Math.floor(buyNum/10);
        if(giveNum>0){
            $("#spaceTips").html("额外赠送<em class='orange'>"+giveNum+"G</em>空间");
            $("#spaceTips").show();
        }else{
            $("#spaceTips").hide();
        }
    }

    function goBuy(){
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

            var serviceId="rmservice";
            var corpId=$("#corpId").val();
            var priceCode="rmservice_novip";
            document.location.href=baseURL+"/qiweipublicity/companysrv/order/order_confirm.jsp?" +
                    "serviceId="+serviceId+"&buyNum="+buyNum+"&corpId="+corpId+"&priceCode="+priceCode;
        });
    }
</script>