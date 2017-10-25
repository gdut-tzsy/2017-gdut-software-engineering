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
		.order-info span{
			padding-left: 40px;
		}
		.green{
			color: #32cd32;
		}
		.yellow{
			color: #ff9600;
		}
		.bold{font-weight: bold;}
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
				
				<div class=" za-l-w za-margin-auto s-bgc-write">
					<form id="J_Form" class="p-form" method="post" autocomplete="off" enctype="multipart/form-data">
					
						<dl class="p-intruduction s-fc-tp za-fz16">
							<dt>
								
							</dt>
							<dd>
								<div class="za-qw-hd s-bgc-gray f-lh40">
									保单信息
								</div>
								<div class="order-info f-lh40">
								
									<p class="mt10 bold">企业员工商业保险</p>
									<p>订单日期<span id="orderDate"></span></p>
									<p>订单状态<span id="statues" class="yellow">未支付</span></p>
									<p>支付时间<span id="payTime"></span></p>
									<p>订单号&nbsp;&nbsp;&nbsp;<span id="orderNo"></span></p>
									<p>投保单号<span id="policyId"></span></p>
									<p>保障期限<span id="orderTime"></span></p>
								
								</div>
								<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
									保障信息
								</div>
								<div class="g-mar-t15 g-pad-l25 check-b-1">
									<p class="font16 za-security">
										保障期限：1年
										<a href="javascript:;" class="security" id="details">
											保障详情
										</a>
									</p>
									保障范围：
									<div class="dp-inline" id="za-tc-label">
										<!--<input checked="checked" disabled="disabled" type="checkbox" zatype="check" zatxt="商旅意外" />-->
									</div>
								</div>
						
						<div class="f-cb g-mar-t25 f-fs18 s-fc-tp bgef h106 pd-t26">

							<table style="margin:0 auto;" id="tipTable">
								<tr class="f-tac s-fc-tc t68line">
									<td>总保费 <span id="t68person">(共0人)</span></td>
									<td></td>
									<td id="za_bigage"> 40-49岁员工保费 </td>
									<td class="t68"></td>
									<td class="t68"> 18-39岁员工保费 </td>
								</tr>
								<tr class="t68line">
									<td colspan="5">&nbsp;</td>
								</tr>
								<tr class="f-tac">
									<td><font class="s-fc-tp cor-orange font25" id="total"> 0 </font> 元 <span id="t68person2">(共 <font id="num"> 0 </font> 人)</span></td>
									<td><span class="pr smalltop"> <img src="../../resource/images/newEnterprice/newent/equals.png" /> </span></td>
									<td><font class="g-mar-r10" id="bigCost"> 0 </font> 元/人 <span class="g-mar-lr5 f-fs12"> X </span> <font id="seniornum"> 0 </font> 人 <span> = </span> <font id="senior" class="cor-orange font25"> 0 </font> 元 </td>
									<td id="img68"><span class="pr smalltop"> <img src="../../resource/images/newEnterprice/newent/add.png" /> </span></td>
									<td id="strong68"><font class="s-fc-tp g-mar-r10" id="smallCost"> 0 </font> 元/人 <span class="g-mar-lr5 f-fs12"> X </span> <font id="juniornum"> 0 </font> 人 <span> = </span><font id="junior" class="cor-orange font25"> 0 </font> 元 </td>
								</tr>
								<tr class="t68line">
									<td colspan="5">&nbsp;</td>
								</tr>
							</table>

							<div class="qw-price tcenter f18 hide">
								企微优惠价：<span class="cor-orange font25">0</span>&nbsp;元
							</div>
						</div>
						<!--被保人信息-->
						<div class="za-qw-hd f-lh40 g-pad-l25 mt60 ">
							被保人信息
						</div>
						<div class="g-mar-t15">
						
							<h2 class="mt30">被保人明细</h2>	
								<div id="za-grid" class="m-instab g-mar-t25 txtable">
									<table class="ins-table-1">
										<thead>
											<tr>
												<th width="120">
													姓名
												</th>
												<th>
													证件类型
												</th>
												<th>
													证件号码
												</th>
												<th>
													出生日期
												</th>
												<th>
													性别
												</th>
												<th>
													手机
												</th>
												<th>
													保费（元）
												</th>
											</tr>
										</thead>
										<tbody id="order-table">
<!-- 											<tr za-index="1241056">
												<td>leo</td>
												<td>身份证</td>
												<td class="idCard">44092119900511831X</td>
												<td>1990-05-11</td>
												<td>男</td>
												<td>13800138000</td>
												<td>368.00</td>
											</tr> -->
										</tbody>
										
									</table>
								</div>
								<div class="text-centet pad-t22 pos-r" id="pageNum">
									<div class="totalperson">当前共有<font id="cust-num">0</font>位被保险人</div>
									<div class="qw-za-page">
										第<span id="current-page">1</span>/<span id="total-page">1</span>页
										<a href="javascript:;" id="page-prev">上一页</a>
										<a href="javascript:;" id="page-next">下一页</a>
										<input type="text" id="select-page"/>
										<a id="go-to-page" href="javascript:;">跳转</a>
									</div>
								</div>
							</div>		
								
					</div>							
				</div>							
			</div>							
		</div>							
	</div>
	<div class="Dialogmask" id="mask"></div>
			<div id="za-ins-buyform" class="za-dialog-hide">
				<form class="p-form" autocomplete="off">
					<div class="za-ins-item tp za-com-ta-cen" style="height:600px; overflow-y:auto;">
						<table class="u-table lh24 g-mar-t15 center za-info-table">
							<tr>
								<td class="s-bgc-gray g-wid120">
									保障范围
								</td>
								<td class="s-bgc-gray g-wid140">
									保障项目
								</td>
								<td class="s-bgc-gray">
									保障内容
								</td>
								<td class="s-bgc-gray g-wid100">
									保额
								</td>
							</tr>
							<!-- <tr>
								<td rowspan="4">
									商旅意外
								</td>
								<td>
									公共意外（飞机）
								</td>
								<td class="tal">
									在保险期间内，被保险人搭乘飞机时，因意外事故导致身故或伤残，除获以上意外身故或伤残保险金，我们将再给付本责任下保险金。
								</td>
								<td>
									1,000,000元
								</td>
							</tr> -->
							
						</table>
					</div>
				</form>
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
	<script src="../../web/js/common/common.js"></script>
	<script src="../../js/md5.js"></script>
	<script src="../../web/js/eportal/order/order-detail.js"></script>
    <%@include file="/manager/fooder.jsp" %>
  
</body>
</html>
