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
					<p class="za-ins-step-img za-ins-step-bg2">
					</p>
					<ul class="za-ins-stepnum za-ins-stepnum_update">
						<li class="za-ins-stepnum-old za-ins-li-1">
							<span class="za-ins-stepnum-t">填写投保信息</span>
						</li>
						<li class="za-ins-stepnum-old za-ins-li-2">
							<span class="za-ins-stepnum-t">确认投保信息</span>
						</li>
						<li class="za-ins-li-3">
							<span class="za-ins-stepnum-t za-ins-stepnum-t_update">保费支付</span>
						</li>
						<li class="za-ins-li-4">
							<span class="za-ins-stepnum-t">完成</span>
						</li>
					</ul>
				</div>
				<div class=" za-l-w za-margin-auto s-bgc-write">
					<form id="J_Form" class="p-form" method="post" autocomplete="off" enctype="multipart/form-data">
						<input name='_csrf_token' type='hidden' value='ko81LJ8N1SCuebd6NtVhLB'>
						<input type="hidden" name="action" value="/order/eportal/OrderAction" />
						<!-- action执行value=1的方法-->
						<input type="hidden" id="doConfirmPolicyInfo" name="event_submit_doConfirmPolicyInfo" />
						<input type="hidden" id="campaignId" name="_fm.ente._0.c" value="20075005" />
						<input type="hidden" id="packageDefId" name="_fm.ente._0.p" value="2120010" />
						<input type="hidden" id="tokenId" name="_fm.ente._0.t" value="75cdd426a48f0d84" />
						<input type="hidden" id="companyType1Name" name="_fm.ente._0.com" value="" />
						<input type="hidden" id="companyType2Name" name="_fm.ente._0.compa" value="" />
						<input type="hidden" name="_fm.ente._0.contac" value="a245338061@sina.cn" />
						<input type="hidden" id="companyProvinceName" name="_fm.ente._0.companyp" value="" />
						<input type="hidden" id="companyCityName" name="_fm.ente._0.companyci" value="" />
						<input type="hidden" id="companyDistrictName" name="_fm.ente._0.companyd" value="" />
						<span class="za-fz18 xm hidden" id="ordermsgtxt"></span>
						<dl class="p-intruduction s-fc-tp za-fz16">
							<dt>
								<p class="za-com-ta-cen za-padding-bottom za-border-dashed za-pd-b25">
									<span class="tb za-fz24">
										员工保&nbsp;&nbsp;企业员工商业保险
									</span>
								</p>
							</dt>
							<dd>
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
											<!--<tr za-index="1241056">
												
												<td>leo</td>
												<td>身份证</td>
												<td class="idCard">44092119900511831X</td>
												<td>1990-05-11</td>
												<td>男</td>
												<td>13800138000</td>
												<td>368.00</td>
												
											</tr>-->
											
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
								
						<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
							投保人信息
						</div>
				
						<div class="g-mar-t15 pt20">
							<table class="u-table lh24 center">
								<tr>
									<td class="td-h">企业名称</td>
									<td class="td-b" id="companyName"></td>
									<td class="td-h">企业类型</td>
									<td class="td-b" id="companyType"></td>
								</tr>
								<tr>
									<td class="td-h">证件号码</td>
									<td class="td-b" id="orgCode"></td>
									<td class="td-h">企业所在地</td>
									<td class="td-b" id="companyAddress"></td>
								</tr>
							</table>
						</div>
						
						<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
							企业联系人信息
						</div>
				
						<div class="g-mar-t15 pt20">
							<table class="u-table lh24 center">
								<tr>
									<td class="td-h">联系人姓名</td>
									<td class="td-b" id="contactPeople"></td>
									<td class="td-h">证件号码</td>
									<td class="td-b" id="contactCertiNo"></td>
								</tr>
								<tr>
									<td class="td-h">电子邮件</td>
									<td class="td-b" id="contactEmail"></td>
									<td class="td-h">手机号码</td>
									<td class="td-b" id="contactPhone"></td>
								</tr>
							</table>
						</div>
						
						<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
							保险期限
						</div>
						<div class="g-mar-t15 pt20">
							保障期限 : <span class="start-time"></span> 零时 至<span class="end-time"></span> 二十四时
						</div>
						
						<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
							保险须知
						</div>
						<div class="g-mar-t15 za-anouce">
							
							<p>
								<strong style="color:#000;font-weight:bold;">1.&nbsp; 本人通过合法途径获得投保单位（投保人）授权，在众安官网代表投保单位进行投保操作，所有投保操作和内容已获得投保单位认可，所有被保险人均为与投保单位有劳动关系的劳动者。本人已就该产品的保障内容以及保险金额向被保险人进行了明确说明，并征得其同意。</strong>
							</p>
							<p class="mr-t23">
								<strong style="color:#000;font-weight:bold;">2. &nbsp;本单位已就保险事宜与全部被保险人进行了宣导和沟通，凡参与该保险的全部被保险人均符合险种条款所约定的投保条件，并了解保障内容且同意由本单位统一办理投保事项；本单位已认真阅读并正确理解 
								<a class="s-lk" href="https://www.zhongan.com/channel/agreement/agreelist_20075005.html" target="_blank">
									《众安员工保企业员工商业保险适用条款》
								</a>
								和投保须知的各项内容，保险公司已在投保网页就保险责任条款、免除保险人责任的条款、合同解除条款进行了明确说明；对投保险种条款尤其是保险责任条款、免除保险人责任的条款、合同解除条款、投保须知、特别约定等，本单位均已认真阅读、理解并同意遵守。</strong>
							</p>
							<p class="mr-t23">
								<strong style="color:#000;font-weight:bold;">3. &nbsp; 本投保单填写以及所附的被保险人清单的各项内容均属真实，并作为本保险合同的组成部分，如有隐瞒或不实告知，本单位愿意承担由此带来的法律后果；</strong>
							</p>
							<p class="mr-t23">
								4. &nbsp; <strong style="color:#000;font-weight:bold;">我们确认：</strong>①本单位投保人员均为与本单位有劳动关系，18-60岁（商旅意外）、18-49岁（其他保障范围），身体健康、能正常工作和生活、无残疾，且从事1～4类职业的劳动者，若从事5类及5类以上职业或拒保职业的工作期间发生意外事故的，不属于保险责任范围，详见
								<!-- 
									href="${baseURL}/manager/benefit/zhongan/file/职业分类表.pdf" target="_blank"
									
									https://www.zhongan.com/open/common/downloadFileScreen/downloadGroupFileFromStaticServer?filename=classifications.pdf
								 -->
								<a class="s-lk" href="${baseURL}/manager/benefit/zhongan/file/职业分类表.pdf" target="_blank">
									《职业分类表》
								</a>
								。本单位投保人员均为在中国大陆地区有固定居住地的人士投保。②本单位投保人员按其所属职业类别的投保规则投保，若投保人员的职业类别发生变更，本单位将在五个工作日内通知保险公司；③本单位投保人员均满足健康告知的要求。
							</p>
							<div class="gaozhi mr-t23">
								<h2>
									健康告知
								</h2>
								<p>
									1)、近一年内无健康检查异常（如血液、超声、影像检查、病理检查等）或2年内住院的被保险人
								</p>
								<p>
									2)、过去两年内被保险人投保人身保险或健康保险时，没有出现被保险公司拒保、延期或者附加相关条件承保
								</p>
								<p>
									3)、被保险人目前或过往未患有下列疾病：急性心肌梗塞、癌症/恶性肿瘤/白血病、慢性肝炎/肝硬化、心脏病（心功能不全二级以上、高血压病II期以上）、冠心病、心肌病、瘫痪、慢性肾功能衰竭晚期（尿毒症期）、急/慢性肾炎、脑中风、糖尿病、慢性支气管炎、肺气肿严重烧伤、急性重症肝炎（暴发型肝炎）、帕金森氏病、重大器官移植术、冠状动脉旁路移植手术、主动脉疾病手术、心脏瓣膜置换术及其他严重慢性疾病,无健康检查异常（如血液、超声、影像检查、病理检查等）或2年内住院。
								</p>
							</div>
							<p class="mr-t23">
								5. &nbsp;同时请您了解，在投保本产品前您应履行相应的如实告知义务，具体如下：
								投保人或被保险人应如实填写投保信息，并就保险公司提出的询问据实告知,否则保险公司有权根据《中华人民共和国保险法》第十六条的规定解除保险合同且不承担赔偿责任：订立保险合同时，保险公司就保险标的或者被保险人的有关情况提出询问的，投保人应当如实告知。<br>
								投保人故意或者因重大过失未履行前款规定的如实告知义务，足以影响保险公司决定是否同意承保或者提高保险费率的，保险公司有权解除合同。<br>
								投保人故意不履行如实告知义务的，保险公司对于合同解除前发生的保险事故，不承担赔偿责任，并不退还保险费。<br>
								投保人因重大过失未履行如实告知义务，对保险事故的发生有严重影响的，保险公司对于合同解除前发生的保险事故，不承担赔偿责任，但退还保险费。</p>
							<p></p>
							<p class="mr-t23">6.根据《中华人民共和国合同法》第十一条规定，数据电文是合法的合同表现形式。本人接受以众安在线财产保险股份有限公司提供的电子保单作为本投保书成立的合法有效凭证，具有完全证据效力。</p>
							<p class="mr-t23">
								7.  如需咨询保险产品相关事宜，请联系众安客户服务热线：400-999-9595
							</p>
										
						</div>
						<div class="mt20 dash-btm">
							<label id="check-license" class="za-ins-ck-label za-ins-ck-check">我同意以上声明</label>
						</div>
						<div class="mt20">
							<div class="f-ib">保费：<span class="cor-orange bold" id="totalPrice"></span> </div> 
							<!--<div class="f-ib ml10">企微用户优惠价：<span class="cor-orange bold">￥999元</span> </div> -->
						</div>
						<div class="g-mar-t25">
							<a id="submitBtn" class="u-btn next-step-btn za-ins-ck-check" target="_blank">
								立即支付
							</a>
							<a href="insure1.jsp" class="s-lku g-mar-l20 btn-to-edit">
								返回修改
							</a>
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
	<div class="za-mask"></div>
	<div id="pay-dialog" class="bui-dialog bui-overlay bui-ext-position pay-dialog">
		<div class="bui-stdmod-header">
			<div class="header-title">提示</div>
		</div>
		<div class="bui-stdmod-body">
			<div class="pay-d-inner">
				<div class="pay-d-hd tcenter">请在新开的保费支付页面完成支付后选择：</div>
				<div class="mt20 pl20">
					<img src="../../img/r_icon.png">
					<span class="pay-d-r">已支付</span>
					<span class="mr5">|</span>
					<span>您可以选择：</span>
					<a class="check-result-btn" href="javascript:;" class="mr5">查看投保状态</a>
					<a href="../../za-main.jsp">继续投保</a>
				</div>
				<div class="dash-line mt20"></div>
				<div class="mt20 pl20">
					<img src="../../img/w_icon.png">
					<span class="pay-d-w">未支付</span>
					<span class="mr5">|</span>
					<span>建议您选择：</span>
					<a href="javascript:;" class="repay-btn" target="_blank">重新支付</a>
				</div>
			</div>
		</div>
		<a tabindex="0" href='javascript:void("关闭")' role="button" class="bui-ext-close" style="">
			<span class="bui-ext-close-x x-icon x-icon-normal">×</span>
		</a>
	</div>
	<script src="../../resource/res/js/jquery-1.8.1.min.js"></script>
	<script src="../../resource/res/js/bui-min.js"></script>
	<script src="../../resource/res/js/common.js?ver=<%=jsVer%>"></script>
	<script src="../../resource/res/js/entcommon.js"></script>
	<script src="../../resource/res/js/ajaxfileupload.js"></script>
	<script src="../../resource/res/js/amountFormat.js"></script>
	<script src="../../resource/res/js/zaupload.min.js"></script>
	<script src="../../web/js/common/common.js?ver=<%=jsVer%>"></script>
	<script src="../../js/md5.js"></script>
	<script src="../../web/js/eportal/order/insure2.js?ver=<%=jsVer%>"></script>
    <%@include file="/manager/fooder.jsp" %>
  
</body>
</html>
