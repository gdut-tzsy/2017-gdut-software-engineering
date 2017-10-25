<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value="GOLD_INTERFACE_CLASS"></jsp:param>
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
    <script src='${baseURL}/themes/qiweipublicity/companysrv/order/order_mulit_confirm.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/order/shoppingCart.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/ajaxfileupload-2.0.js"></script>
    <script src="${baseURL}/manager/js/uploadfile.js?ver=<%=jsVer%>"></script>
    <style>
        #orgInfoExtDiv{
            transform: initial;
            margin-top: -300px;
            margin-left: -350px;
        }
    </style>
</head>
<body>
<div class="loading_bg" id="msgshow_bg" style="display:none"></div>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>结算中心</span>
        </div>
    </div>
</div>
<div class="wrap-container FW">
    <div id="container" class="clearfix">
        <div class="place tleft fz14">

        </div>
        <div class="FW_content">
            <!-- 提交订单 -->
            <div class="tab-main">
                <form id="orderConfirmForm" action="" method="post" onsubmit="return false;" templateId="default" dqdpCheckPoint="add_form">
                    <!-- 订单信息 -->
                    <input type="hidden" value="" id="corpId" name="corpId"/>
                    <input type="hidden" value="" id="orderId" name="orderId"/>
                    <input type="hidden" value="" id="amount" name="amount"/>
                    <!--地址信息-->
                    <input type="hidden" value="" name="postInf.recName" id="recName"/>
                    <input type="hidden" value="" name="postInf.recMobile" id="recMobile"/>
                    <input type="hidden" value="" name="postInf.recAddress" id="recAddress"/>
                    <!--转增信息-->
                    <input type="hidden" value="false" name="beneficiary" id="beneficiary"/>
                    <!--购买额外属性-->
                    <div id="extInfDiv">

                    </div>

                    <div class="buyvip-main">
                        <div class="buy-title">
                            <h1 class="buyvip-title">订单确认</h1>
                            <div class="buy-title-right fz16 c999">
                                <span class="orange">确认订单</span>  >  付款  >  填写发票
                            </div>
                        </div>

                        <div id="addressSelectId" style="display:none;">
                            <div class="fz18 c666 mb20">
                                收货人信息
                                <div class="clearfix relative mb5" style="display:none;">
                                    <div class="check-tips" id="inputAddressTips"><span>!</span>请选择地址</div>
                                </div>
                            </div>

                            <div id="addressDiv">
                                <ul class="tab-address clearfix" id="addressList">
                                    <li class="add-address" onclick="doAddAddress();">
                                        <img src="../../../themes/manager/companysrv/images/img/ic-add.png" alt="" class="mt50"/>
                                        <p class="mt20 c999 fz16">添加地址</p>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <!-- 支付方式 -->
                        <%@include file="../include/payWay.jsp" %>

                        <div>
                            <%--<div class="orderDetails-tit" style="display:none;">
                                <span>发票信息</span>
                            </div>
                            <div class="c666 ml20" style="display:none;">
                                <div>开票类型：<span id="invoiceType"></span></div>
                                <div class="mt5">工商注册全称：<span id="corpName"></span></div>
                                <div class="mt5 none corpClass">纳税人识别号：<span id="corpBankNo"></span></div>
                                <div class="mt5 none corpClass">一般纳税人资格证明文件：<span id="corpLicensePic"></span></div>
                                <div class="mt5 none corpClass">地址、电话：<span id="addrPhone"></span></div>
                                <div class="mt5 none corpClass">开户行及账号：<span id="corpBankNameAndNo"></span></div>
                                <div class="mt5 none corpClass">
                                    <div class="mt5">收件信息：<span id="addressInfo"></span></div>
                                </div>
                                <div class="mt5">
                                    <div class="mt5"><a href="javascript:goOrgInfoExt();">修改并完善</a></div>
                                </div>
                            </div>--%>


                            <div class="item mb30 mt30">
                                <span class="dl" style="width:125px;"><span class="orange mr5">*</span>工商注册全称：</span>
                                <span class="dd">
                                    <input id="corpName" name="corpName" type="text" class="FW_input w350" valid="{must:true,length:60,tip:'工商注册全称'}"/>
                                    <div class="tipsBox">
                                        <i class="fa-question-circle"></i>
                                        <div class="tipsItem">
                                            <div class="tipsContent">
                                                工商注册全称用于保障订单合法有效性和发票开具，请准确填写。设置后不能自行修改，如要修改，请联系企微工作人员
                                            </div>
                                        </div>
                                    </div>
                                </span>
                            </div>

                        </div>


                        <!-- 订单详情 -->
                        <div class="orderDetails-tit">
                            <span>订单详情</span>
                            <a href="javascript:goBackProductSelect();" class="fr orange fz16" style="display:none;" id="goBackBtn">返回修改商品信息</a>
                        </div>
                        <table class="orderDetails" cellpadding="0" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>商品名称</th>
                                    <th>商品属性</th>
                                    <th>单价</th>
                                    <th>数量</th>
                                </tr>
                            </thead>
                            <tbody id="orderInfoTable">
    <%--                            <tr>
                                    <td>银卡VIP会员</td>
                                    <td>有效期至：2017-10-10</td>
                                    <td>￥1888.00 / 年</td>
                                    <td>
                                        购买年数：   3 年<br>
                                        购买人数：300人
                                    </td>
                                </tr>--%>
                            </tbody>
                        </table>

                        <div class="ml20 mt10 red" style="display:none;" id="beneficiaryTips"></div>

                        <!-- 应付金额 -->
                        <div class="paymentAmount">
                            <div class="item mb20">
                                <span>商品金额：</span>
                                <span id="orderAmount">￥0.00</span>
                            </div>
                            <div class="item mb20">
                                <span>优惠金额：</span>
                                <span id="concessionalRate">￥0.00</span>
                            </div>
                            <div class="item mb20" style="display:none;">
                                <span>抵扣金额：</span>
                                <span id="deductionAmount">-￥0.00</span>
                            </div>
                            <div class="item">
                                <span>应付金额：</span>
                                <span class="Amount" id="tradeAmount">￥0.00</span>
                            </div>
                            <div class="popup" style="display:none;" id="popupTips">

                            </div>
                        </div>
                        <div class="clear" style="border-top:1px solid #e4e4e4;"></div>
                        <input type="button" class="payment-btn" value="提交订单" id="formSubmitBtn" onclick="submitOrder();"/>
                    </div>
                </form>
                <%@include file="../pay/payMsgBox.jsp" %>
                <%@include file="../../../manager/companysrv/include/address.jsp" %>
            </div>
        </div>
    </div>
    <%@include file="../include/footer.jsp" %>
    <%@include file="../include/orgInfoExt.jsp" %>
</div>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>