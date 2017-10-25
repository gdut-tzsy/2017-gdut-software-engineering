<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value="benefitMenu,remindMenu,flowMenu,flowgroupMenu,moveMenu,askMenu,groupMenu,crmMenu,configMenu,contactMenu,sysmgrMenu,activityMenu,dynamicinfoMenu,infoMenu,outworkMenu,taskTemMenu,meetingRoomMenu,explainMenu,countMenu,productMenu,formMenu,topicMenu,diaryMenu,surveyMenu,settingmenu,settingusermenu,settingrolemenu,cooperationMenu,dimissionmenu,checkworkMenu,currencySettingMenu,qyPaySettingMenu,settingaccountmenu"></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <!-- 众安样式 -->
    <link href="../../resource/stylesheets/common.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/za-common.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/calendar-min.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/select-min.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/grid-min.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/za-add.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/za-ent.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../resource/stylesheets/newEnterprise/newent.css?ver=<%=jsVer%>" rel="stylesheet">
	<style>
		.w45 {
			width: 44%;
			display: inline-block;
		}
		.w10 {
			width: 10%;
			display: inline-block;
		}
		.tc {
			text-align: center;
		}
		html,
		body,
		#container {
			background: #fcfcfc;
		}
		.span8 {
			width: auto;
		}
	</style>
	<!-- 众安样式 end -->
	<link rel="stylesheet" type="text/css" href="${baseURL}/manager/benefit/zhongan/css/zn-main.css?ver=<%=jsVer%>">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript">var BASE_URL = "${baseURL}";</script>
</head>
<body>
    <c:set var="titleActiveCLass" value="8" scope="request"/>
    <c:set var="leftMenuActiveCLass" value="${param.classClick }" scope="request"/>
    <%@include file="/manager/head.jsp" %>
	<div class="wrap-container">
		<div id="container" class="clearfix">
			<div class="za-buy-product2 f-oh za-buy">
				<div class="za-guide">
					<div>
						<a href="../../../main.jsp">员工福利</a>
						<i>></i>
						<a href="../../../zhongan/za-main.jsp">员工商业保险</a>
						<i>></i>
						<a class="active" href="javascript:;">投保</a>
					</div>
				</div>
				<div class="za-ins-step tp za-fz16 ">
					<p class="za-ins-step-img za-ins-step-bg4">
					</p>
					<ul class="za-ins-stepnum za-ins-stepnum_update">
						<li class="za-ins-stepnum-old za-ins-li-1">
							<span class="za-ins-stepnum-t">填写投保信息</span>
						</li>
						<li class="za-ins-stepnum-old za-ins-li-2">
							<span class="za-ins-stepnum-t">确认投保信息</span>
						</li>
						<li class="za-ins-stepnum-old za-ins-li-3">
							<span class="za-ins-stepnum-t za-ins-stepnum-t_update">保费支付</span>
						</li>
						<li class="za-ins-stepnum-old za-ins-li-4">
							<span class="za-ins-stepnum-t">完成</span>
						</li>
					</ul>
				</div>
				
				<div class="result-page">
					<div>
						<img id="statuesIcon" src="../../img/w_icon.png" width="16">
						<span id="statuesText" class="tlt yellow">订单未支付！</span>
					</div>
					<div class="mt15">支付时间：<span id="payTime"></span></div>
					<div class="mt15">支付状态：<span id="statues" class="yellow">未支付</span></div>
					<div class="mt30">
						<a href="../../za-main.jsp" class="back-btn">确认并返回首页</a>
						<!--<a href="order-list.jsp" class="order-list-btn">查看订单列表</a>-->
					</div>
					<div class="mt40 order-details">
						<div class="dt">
							订单信息
							<div class="fr">订单号：<span id="orderNo"></span></div>
						</div>
						<div class="dd">
							<div class="mt20">商品名称：<span class="yellow" id="orderName">企业员工商业保险</span></div>
							<div class="mt20">保障期限：<span class="yellow" id="orderTime">2015-09-07 至 2015-12-14</span></div>
							<div class="mt20">被保人数：<span class="yellow" id="orderCount">100人</span></div>
							<div class="mt20">保费 (元)：<span class="yellow" id="orderFee">900.00</span></div>						
						</div>
					</div>
				</div>
				
				
		</div>							
	</div>
	
	<script src="../../resource/res/js/jquery-1.8.1.min.js"></script>
	<script src="../../resource/res/js/bui-min.js"></script>
	<script src="../../resource/res/js/common.js"></script>
	<script src="../../resource/res/js/entcommon.js"></script>
	<script src="../../resource/res/js/ajaxfileupload.js"></script>
	<script src="../../resource/res/js/amountFormat.js"></script>
	<script src="../../resource/res/js/zaupload.min.js"></script>
	<script src="../../web/js/common/common.js?ver=<%=jsVer%>"></script>
	<script src="../../web/js/eportal/order/result.js?ver=<%=jsVer%>"></script>
    <%@include file="/manager/fooder.jsp" %>
  
</body>
</html>
