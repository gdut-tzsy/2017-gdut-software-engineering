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
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_buy_old.js?ver=<%=jsVer%>'></script>
</head>
<body>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>银卡VIP</span>
        </div>
    </div>
</div>
<div class="wrap-container FW">
    <div id="container" class="clearfix">
        <div class="place tleft fz14">

        </div>
        <div class="FW_content">
            <!-- 账户安全 -->
            <div class="tab-main">
                <div class="buyvip-main">
                    <h1 class="buyvip-title">开通VIP会员</h1>
                    <form method="post">
                        <div class="item mb30">
                            <span class="dt"><span class="orange">*</span>VIP级别：</span>
                            <span class="dd relative" id="level">
                              <span class="opt" myValue="silver">银卡VIP</span>
                              <span class="opt" myValue="gold">金卡VIP</span>
                              <span class="opt" myValue="platinum">白金VIP</span>
                              <span class="opt" myValue="diamond">钻石VIP</span>
                              <span class="opt">白钻VIP</span>
                              <div class="check-tips none" id="levelTips"><span>!</span>请选择VIP级别</div>
                            </span>
                        </div>
                        <div class="onLine">
                            <div class="item mb30">
                                <span class="dt"><span class="orange">*</span>购买年数：</span>
                                <span class="dd relative" id="years">
                                    <span class="opt" myValue="1">一年</span>
                                    <span class="opt" myValue="2">两年<%--<span class="orange">(惠)</span>--%></span>
                                    <span class="opt" myValue="3" id="activeTips">三年<%--<span class="orange">(惠)--%></span>
                                        <%--<img src="../../../themes/manager/companysrv/images/img/tips-act.png"/>--%>
                                    </span>
                                    <div class="check-tips none" id="yearsTips"><span>!</span>请选择购买年数</div>
                                </span>
                            </div>
                            <div class="item mb40">
                                <span class="dt"><span class="orange">*</span>企业corpid：</span>
                                <span class="dd" style="position:relative">
                                    <input type="text" id="corpId" onblur="getOrgMember()">
                                    <div class="check-tips none" id="corpIdTips"><span>!</span>企业corpid不存在</div>
                                </span>
                            </div>
                            <div class="item mb40">
                                <span class="dt"><span class="orange">*</span>购买人数：</span>
                                 <span class="dd relative" id="members">
                                      <span class="opt" myValue="200">1-200人</span>
                                      <span class="opt" myValue="500">200-500人</span>
                                      <span class="opt" myValue="max">500人以上</span>
                                      <span class="ml20 c999">(通讯录人数：<span class="orange" id="userMemberNum">0</span>人)</span>
                                      <div class="check-tips none" id="memeberNumTips"><span>!</span>购买人数必须是大于<em class="orange"> 0 </em>的整数</div>
                                </span>
                            </div>
                            <div class="item">
                                <input type="button" name="" value="立即购买" class="tab-main-btn ml115" onclick="buynow()">
                                <span class="ml30" id="btn_xieyi">点击立即购买即同意<a href="javascript:showAgreeBox('1');" class="orange">«企业账户服务协议»</a></span>
                            </div>
                            <div class="item mt50 pt30 bte4e4e4">
                                <p>购买说明：</p>
                                <p>1. 购买成功后，将马上为您开通VIP服务；VIP过期后需续费才能继续享有原有功能。</p>
                                <p>2. 如需发票，请在企业服务模块下的发票管理页面，填写相关信息，企微工作人员会在3个工作日内将发票寄出。</p>
                               <%-- <p>3. 购买1年VIP，赠送1张面值100元京东购物卡；购买2年VIP，赠送1张面值200元京东购物卡；购买三年VIP，赠送1张面值300元京东购物卡。</p>--%>
                            </div>
                        </div>
                        <!-- 定制开发服务联系人表 -->
                        <div class="ml30 none not-onLine">
                            <div class="contactTilte">
                                该VIP会员包含定制开发服务，请联系商务经理沟通完整项目报价
                            </div>
                            <table style="border:1px solid #ddd;width:940px;" cellpadding="0" cellspacing="0">
                                <tbody>
                                </tbody>
                                <thead>
                                    <tr style="height:70px;background:#f5f5f5;color:#ff9600;font-size: 20px;">
                                        <th style="width: 140px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            <img src="../../../themes/manager/companysrv/images/img/ic_people.png" style="vertical-align: middle;margin-right: 8px;">
                                            <span style="vertical-align:middle;font-weight: normal">商务经理</span>
                                        </th>
                                        <th style="width: 300px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            <img src="../../../themes/manager/companysrv/images/img/ic_didian.png" style="vertical-align: middle;margin-right: 8px;">
                                            <span style="vertical-align:middle;font-weight: normal">负责区域</span>
                                        </th>
                                        <th style="width: 140px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            <img src="../../../themes/manager/companysrv/images/img/ic_phone.png" style="vertical-align: middle;margin-right: 8px;">
                                            <span style="vertical-align:middle;font-weight: normal">联系电话</span>
                                        </th>
                                        <th style="width: 200px;border-bottom:1px solid #ddd;">
                                            <img src="../../../themes/manager/companysrv/images/img/ic_mails.png" style="vertical-align: middle;margin-right: 8px;">
                                            <span style="vertical-align:middle;font-weight:normal">联系邮箱</span>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr style="height:70px;">
                                        <td style="font-size: 16px;text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;color: #333;">
                                            王先生
                                        </td>
                                        <td style="padding: 0 30px;line-height: 22px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            黑龙江、辽宁、吉林、内蒙古、北京、天津、<br>河北、河南、山东、山西
                                        </td>
                                        <td style="text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;font-size: 16px;">
                                            13602715172
                                        </td>
                                        <td style="padding-left:20px ;line-height: 22px;font-size: 16px;border-bottom:1px solid #ddd;">
                                            wangjingbo@do1.com.cn
                                        </td>
                                    </tr>
                                    <tr style="height:70px">
                                        <td style="font-size: 16px;text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;color: #333;">
                                            王先生
                                        </td>
                                        <td style="padding: 0 30px;line-height: 22px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            上海、江苏、安徽、江西、浙江、福建
                                        </td>
                                        <td style="text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;font-size: 16px;">
                                            13509056590
                                        </td>
                                        <td style="padding-left:20px ;line-height: 22px;font-size: 16px;border-bottom:1px solid #ddd;">
                                            wangyi@do1.com.cn
                                        </td>
                                    </tr>
                                    <tr style="height:70px">
                                        <td style="font-size: 16px;text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;color: #333;">
                                            彭先生
                                        </td>
                                        <td style="padding: 0 30px;line-height: 22px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            广东、海南
                                        </td>
                                        <td style="text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;font-size: 16px;">
                                            13539937795
                                        </td>
                                        <td style="padding-left:20px ;line-height: 22px;font-size: 16px;border-bottom:1px solid #ddd;">
                                            pengyongxiang@do1.com.cn
                                        </td>
                                    </tr>
                                    <tr style="height:70px">
                                        <td style="font-size: 16px;text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;color: #333;">
                                            张小姐
                                        </td>
                                        <td style="padding: 0 30px;line-height: 22px;font-size: 14px;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">
                                            湖北、湖南、广西、贵州、云南、四川、重庆、<br>宁夏、陕西、甘肃、新疆、西藏、青海
                                        </td>
                                        <td style="text-align: center;border-bottom:1px solid #ddd;border-right:1px solid #ddd;font-size: 16px;">
                                            13548541227
                                        </td>
                                        <td style="padding-left:20px ;line-height: 22px;font-size: 16px;border-bottom:1px solid #ddd;">
                                            zhangshuimei@do1.com.cn
                                        </td>
                                    </tr>
                                    <tr style="height:70px">
                                        <td style="font-size: 16px;text-align: center;border-right:1px solid #ddd;color: #333;">
                                            敖先生
                                        </td>
                                        <td style="padding: 0 30px;line-height: 22px;font-size: 14px;border-right:1px solid #ddd;">
                                            渠道合作咨询
                                        </td>
                                        <td style="text-align: center;border-right:1px solid #ddd;font-size: 16px;">
                                            18520006663
                                        </td>
                                        <td style="padding-left:20px ;line-height: 22px;font-size: 16px;">
                                            aoyongning@do1.com.cn
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <a href="" class="orang fz16 mt30 ml360">详细了解深度定制与私有部署»</a>
                        </div>
                    </form>
                </div>
            </div>
            <%@include file="../../../manager/companysrv/accountAgreement.jsp" %>
        </div>
    </div>
</div>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>