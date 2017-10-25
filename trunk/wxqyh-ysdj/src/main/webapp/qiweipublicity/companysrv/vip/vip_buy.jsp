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
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_buy.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <style type="text/css">
      html{min-width: 1200px;}
    </style>
</head>
<body>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span id="headName">银卡VIP</span>
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
                <div class="buyvip-main mb0">
                    <h1 class="buyvip-title" id="buyName">开通银卡VIP</h1>
                    <form method="post">
                        <div class="item mb30">
                            <span class="dt">VIP套餐：</span>
                            <span class="dd" id="level">
                                <span class="opt vippackageClass" myValue="silver_class"  style="display:none;padding: 0 28px;">
                                    标准VIP服务
                                    <div class="opt-tip">
                                        1：一年期银卡VIP服务<br/>
                                        2：4G企微云储存空间<br/>
                                        3：新功能优先使用权<br/>
                                      <%--  4：限量送：100元京东购物卡--%>
                                    </div>
                                </span>
                                <span class="opt vippackageClass" myValue="silver_onebyone" style="display:none;padding: 0 28px;">
                                    一对一VIP套餐<i class="FW-icon-hot">推荐</i>
                                    <div class="opt-tip">
                                        1：一年期银卡VIP服务<br/>
                                        2：一年期一对一技术服务<br/>
                                        3：20G企微云储存空间<br/>
                                        4：新功能优先使用权<br/>
                                        5：限量送：300元京东购物卡<br/>
                                        6：限量送：699元QW1考勤机
                                    </div>
                                </span>
                                <span class="opt vippackageClass" myValue="silver_distoffice" style="display:none;padding: 0 28px;">
                                    多地办公VIP套餐
                                    <div class="opt-tip">
                                        1：一年期银卡VIP服务<br/>
                                        2：一年期一对一技术服务<br/>
                                        3：一套视频会议直播设备<br/>
                                        4：50G企微云储存空间<br/>
                                        5：新功能优先使用权<br/>
                                        6：限量送：500元京东购物卡
                                    </div>
                                </span>
                              <div class="check-tips none" id="levelTips"><span>!</span>请选择购买类型</div>
                            </span>
                        </div>
                        <div class="onLine">
                            <div class="item mb30">
                                <span class="dt">购买年数：</span>
                                <span class="dd relative" id="years">
                                    <span class="opt" myValue="1">一年</span>
                                    <span class="opt" myValue="2">两年<%--<span class="orange">(惠)</span>--%></span>
                                    <span class="opt" myValue="3" id="activeTips">三年<%--<span class="orange">(惠)--%></span>
                                        <%--<img src="../../../themes/manager/companysrv/images/img/tips-act.png"/>--%>
                                    </span>
                                    <div class="check-tips none" id="yearsTips"><span>!</span>请选择购买年数</div>
                                </span>
                            </div>
                            <div class="item mb30">
                                <span class="dt"><span class="orange">*</span>CorpID：</span>
                                <span class="dd" style="position:relative">
                                    <input class="fl" type="text" id="corpId" onblur="getOrgMember()">
                                    <span class="fl ml10 dd-inline">
                                        <a href="javascript:showAcountBox();" class="fz14" style="text-decoration:underline;">使用企微管理账号获取</a><br>
                                        <span class="c999 fz14">也可在企业微信或企微管理后台中复制</span>
                                    </span>
                                    <div class="check-tips none" id="corpIdTips" style="width: 320px;"><span>!</span>CorpID不存在</div>
                                </span>
                            </div>

                            <div class="item mb15">
                                <span class="dt">购买人数：</span>
                                 <span class="dd relative" id="members">

                                     <span class="positiveIpt fl">
                                         <input type="button" class="positiveIpt_l p_btn" value="-" />
                                         <input type="text" class="positiveIpt_t" id="buyMember" value="1"/>
                                         <input type="button" class="positiveIpt_r p_btn" value="+" />
                                     </span>

<%--                                      <span class="opt on" myValue="500">1-500人</span>
                                      <span class="opt" myValue="1000">501-1000人</span>
                                      <span class="opt" myValue="max">不限人数</span>--%>
                                        <span class="fl ml10 dd-inline">
                                            <span class="c999 fz14">通讯录人数</span><br>
                                            <span class="FW_orange fz14"><span id="userMemberNum">0</span>人</span>
                                        </span>
                                      <%--<span class="ml20 c999">(通讯录人数：<span class="orange" id="userMemberNum">0</span>人)</span>--%>
                                      <div class="check-tips none" id="memeberNumTips"><span>!</span>购买人数必须是大于<em class="orange"> 0 </em>的整数</div>
                                </span>
                            </div>

                            <div class="item mb40" id="beneficiary_div" style="display:none;">
                                <span class="dt">购买方式：</span>
                                 <span class="dd relative">
                                     <label>
                                         <input type="checkbox" id="beneficiary" name="beneficiary" style="border: none;padding: 0;line-height: 0"/>服务转赠
                                     </label>
                                </span>
                            </div>

                            <div class="item" id="estimateAmount_div">
                                <span class="dt">&nbsp;</span>
                                 <span class="dd relative">
                                     <span class="Amount" id="estimateAmount">￥0.00</span>
                                     <div class="dd-inline mt5 ml5">
                                         <span class="price_red" id="everyPersonAmount"></span>
                                     </div>
                                     <div class="dd-inline mt10 ml5">
                                         <span class="c555" id="concessionalRate">￥2,888.00</span>
                                     </div>
                                </span>
                            </div>

                            <div class="item mt15 mb40">
                                <p id="priceTips" class="ml115"></p>
                            </div>

                            <div class="item">
                                <a class="btn orangeBtn ml115" onclick="buynow()" style="padding: 4px 25px;font-size: 16px;">立即购买</a>
                                <p class="ml115 mt10" id="btn_xieyi">购买即视为同意<a href="javascript:showAgreeBox('1');">《企业账户服务协议》</a></p>
                            </div>
                            <div class="item mt50 pt30 bte4e4e4">
                                <p>购买说明：</p>
                                <p class="mt10">1. 购买成功后，将马上为您开通VIP服务；VIP过期后需续费才能继续享有原有功能。</p>
                                <p class="mt5">2. 申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会在7个工作日内将发票寄出。</p>
                               <%-- <p>3. 购买1年VIP，赠送1张面值100元京东购物卡；购买2年VIP，赠送1张面值200元京东购物卡；购买三年VIP，赠送1张面值300元京东购物卡。</p>--%>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <%@include file="../../../manager/companysrv/accountAgreement.jsp" %>
        </div>
    </div>
    <%@include file="../include/footer.jsp" %>
</div>
<%@include file="../include/accountMsgBox.jsp" %>
<!-- 获取企业Corpid弹框 -结束-->
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
