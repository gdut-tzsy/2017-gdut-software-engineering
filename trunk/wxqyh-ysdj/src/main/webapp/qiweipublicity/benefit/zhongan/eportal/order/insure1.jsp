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
		
 		.tableFixed{
			table-layout:fixed;
		}
		/*文字太长就出现...样式  */
		.ellipsis {
	    	text-overflow: ellipsis;
	  		white-space: nowrap;
	  		word-wrap: normal;
	  		overflow:hidden;
		}
	</style>
	<!-- 众安样式 end -->
	<link rel="stylesheet" type="text/css" href="${baseURL}/manager/benefit/zhongan/css/zn-main.css?ver=<%=jsVer%>">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript">
    	var BASE_URL = "${baseURL}";
//		var BASE_URL = "http://192.168.3.11:8080/wxqyh";//测试配置
    </script>
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
						<p class="za-ins-step-img za-ins-step-bg1">
						</p>
						<ul class="za-ins-stepnum za-ins-stepnum_update">
							<li class="za-ins-stepnum-old za-ins-li-1">
								<span class="za-ins-stepnum-t">填写投保信息</span>
							</li>
							<li class="za-ins-li-2">
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
							<input type="hidden" name="_fm.ente._0.contac" value="" />
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
											<a href="javascript:;" class="security" id="details">保障详情</a>
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

										<div class="qw-price tcenter f18">
											企微优惠价：<span class="cor-orange font25">0</span>&nbsp;元
										</div>
									</div>

									<div class="za-qw-hd f-lh40 g-pad-l25 mt60 ">
										被保人信息
									</div>
									<div class="g-mar-t15 ">
										<!-- <div class="m-form-item g-pad-l25">
								<span class="f-ib g-pad-l25">上传人员清单，获取精准报价</span>
								</div>-->
										<!--<div class="m-file-step f-cb f-oh">-->
											<!--<div class="m-spitem m-spitem0">
														<span>通讯录选择：</span>
														<a id="toAddPerson" href="javascript:;" class="s-lku">
															<i class="u-icon in-down qw-txl">模版下载</i>
															<span>从企微通讯录选择</span> 
														</a>
												 <div class="za-b-or">或</div> 
											</div>-->
											<!-- <div class="m-spitem m-spitem1">
												<span class="qw-za-step-num">1</span>
												<dl>
													<dt>
														下载名单模版
													</dt>
													<dd>
														<a href="/open/common/downloadFileScreen/downloadFile?filename=zaGroupApplyTemplate.xlsx" target="_blank" class="s-lku">
															<i class="u-icon in-down">
																模版下载
															</i> 被保人名单模版下载
														</a>
													</dd>
												</dl>
											</div>
											<div class="m-spitem m-spitem2">
												<span class="qw-za-step-num">2</span>
												<dl class="dl2">
													<dt>
														填写模版
													</dt>
													<dd>
														<a href="/open/common/downloadFileScreen/downloadFile?filename=zaGroupApplyExample.xlsx" target="_blank" class="s-lku">
															<i class="u-icon in-head">
																被保人名单样例
															</i> 被保人名单样例
														</a>
													</dd>
												</dl>
											</div>
											<div class="m-spitem m-spitem3">
												<span class="qw-za-step-num">3</span>
												<dl class="dl3">
													<dt>
														上传名单
													</dt>
													<dd>
														<div class="u-file-box s1">
															<div class="u-file-text">
																浏览
															</div>
															<input id="uploadFile" name="uploadFile" type="file" class="u-file" fileformat=".xls,.xlsx" filefn="fnUploadFile">
														</div>
													</dd>
												</dl>
											</div> -->
										<!--</div>-->
										<fieldset class="g-mar-t15">
											<legend>
												被保人明细
											</legend>
											<div id="za-grid" class="m-instab g-mar-t25 txtable">
												<table class="ins-table-1 tableFixed">
													<thead>
														<tr>
															<th width="50">
																选择
															</th>
															<th width="50">
																序号
															</th>
															<th width="120">
																姓名
															</th>
															<th>
																证件类型
															</th>
															<th width="200">
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
															<th>
																操作
															</th>
														</tr>
													</thead>
													<tbody id="order-table">
															
														<tr za-id="xxx">
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td class="idCard"></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
														</tr>
														<tr za-id="xxx">
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td class="idCard"></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
														</tr>
														
													</tbody>
													<tfoot>
														<tr>
															<td class="align-left" colspan="5">
																<input class="ml5 check-all-tr" type="checkbox">
																<span class="delete deleteAll" onclick="delChoose()">
																	删除
																</span>
															</td>
															<td colspan="5" class="align-right">
																<a id="toAddPerson" href="javascript:;" class="s-lku">
																	<i class="u-icon in-down qw-txl"></i>
																	<span>从企微通讯录选择</span> 
																</a>
																<span class="add-ins" onclick="addins();">
																	<i>
																	</i>
																	新增被保人
																</span>
																<span class="add-all-ins" style="display:none;">
																	<input id="" name="uploadFile" type="file" class="">
																	<i>
																	</i>
																	批量上传新增被保人
																</span>
															</td>
														</tr>
													</tfoot>
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
										</fieldset>
										<div class="insure_headright1"><i class="insurehead"></i>如对投保流程或保险产品有任何疑问，您可拨打众安客服热线4009999595进行咨询</div>
										<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 mr-t29">
											投保人信息
										</div>
										<div class="g-mar-t15 g-pad-l25">
											<div class="m-form-item w_80">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											企业名称：
										</span>
												<input name="_fm.ente._0.co" value="" type="text" class="u-text g-wid280" data-rules="{required:[true,'企业名称必须填写'],maxlength:50}" rules-type="1" style="width:297px;" id="companyName" />
												<span class="companyTip">
											请填写真实完整的企业名称，该名称默认为发票抬头
										</span>
											</div>
											<!--<div class="m-form-item w50 db">
									<span class="f-ib g-wid120"><font class="required">*</font>企业类型：</span>
									<div id="za-sel1" class="u-select g-wid260">
									<input name="" value="" type="hidden" id="za-sel1-hide" data-rules="{required:[true,'企业类型必须选择'],industry:0}" rules-type="1">
									</div>
									</div>-->
											<div class="m-form-item w50">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											企业类型：
										</span>
												<div id="industry" class="u-select g-wid120">
													<input id="industry-hide" name="_fm.ente._0.comp" value="" type="hidden">
												</div>
												<div id="industry2" class="u-select g-wid120">
													<input id="industry2-hide" name="_fm.ente._0.compan" value="" type="hidden">
												</div>
												<span class="x-field-error x-field-error1" id="companyerror" style="display:none;" data-title="企业类型必须选择！">
													<span class="x-icon x-icon-mini x-icon-error">!</span>
												</span>
											</div>
											<div class="m-form-item">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											证件号码：
										</span>
												<div id="za-sel2" class="u-select g-wid120">
													<input name="_fm.ente._0.company" value="" type="hidden" id="za-sel2-hide">
												</div>
												<input name="_fm.ente._0.companyc" value="" id="companyCertiNo" class="u-text  w297" type="text" data-rules="{required:true,maxlength:20}" rules-type="1">
											</div>
											<div class="m-form-item">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											企业所在地：
										</span>
												<div id="selprovince" class="u-select g-wid120">
													<input id="selprovince-hide" name="_fm.ente._0.companypr" value="" type="hidden">
												</div>
												<div id="selcity" class="u-select g-wid120">
													<input id="selcity-hide" name="_fm.ente._0.companycit" value="" type="hidden">
												</div>
												<div id="selarea" class="u-select g-wid120">
													<input id="selarea-hide" name="_fm.ente._0.companydi" value="" type="hidden">
												</div>
												<input name="_fm.ente._0.companya" value="" class="u-text  w451" type="text" data-rules="{required:true,maxlength:50}" rules-type="1">
											</div>
										</div>
										<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 g-mar-t15">
											企业联系人信息
										</div>
										<div class="g-mar-t15 g-pad-l25">
											<div class="m-form-item w50">
												<span class="f-ib w121">
											<font class="required">
												*
											</font>
											联系人姓名：
										</span>
												<input name="_fm.ente._0.con" value="" class="u-text w300" type="text" data-rules="{regexp:/^[\u0391-\uFFE5A-Za-z]+$/,required:[true,'联系人姓名必须填写'],maxlength:20}" data-messages="{regexp:'只能输入中英文'}" rules-type="1">
											</div>
											<div class="m-form-item w50">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											证件号码：
										</span>
												<div id="za-sel3" class="u-select g-wid100">
													<input name="_fm.ente._0.cont" value="" type="hidden" id="za-sel3-hide">
												</div>
												<input name="_fm.ente._0.conta" value="" class="u-text w241" type="text" data-rules="{required:true,idcard:true}" rules-type="1">
											</div>
											<div class="m-form-item w50" style="position: relative;">
												<span class="f-ib w121">
											<font class="required">
												*
											</font>
											电子邮件：
										</span>
												<input id="za-qymail" data-rules="{regexp:[/\w+((-w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/,' ']}" rules-type="1" value="" class="u-text w300" type="text">
												<!--<span style="bottom: -6px; position: absolute;left:170px;" class="f-fs12
										s-fc-tc">注：电子邮件用于接收电子保单</span>-->
											</div>
											<div class="m-form-item w50">
												<span class="f-ib g-wid120">
											<font class="required">
												*
											</font>
											手机号码：
										</span>
												<input name="_fm.ente._0.contact" value="" class="u-text w300" type="text" data-rules="{regexp:[/^1[3|4|5|8|7|9][0-9]\d{8}$/,'手机号码为11位数字']}" rules-type="1">
											</div>
										</div>
									</div>
									<div class="za-qw-hd s-bgc-gray f-lh40 g-pad-l25 g-mar-t15">
										保险期限
									</div>
									<div class="f-cb g-mar-t25 g-pad-l25">
										<div class="m-form-item w45">
											<span class="f-ib w130">
										<font class="required">
											*
										</font>
										保障开始时间：
									</span>
											<input id="timelimit" name="_fm.ente._0.d" value="" class="u-calendar w300" readonly="readonly" type="text" data-rules="{required:true}" rules-type="1">

										</div>
										<div class="m-form-item w10 tc">
											<span class="f-ib">
										0时起
									</span>
										</div>
										<div class="m-form-item w45">
											<span class="f-ib w130">
										保障结束时间：
											</span>
											<span id="timelimittxt">
											</span>
										</div>
									</div>
									<hr class="u-hr dashed g-mar-t25" />
									<div class="g-mar-t25">
										<a id="subbtn" class="u-btn bg-orange next-step-btn" href="javascript:;">下一步</a>
									</div>
								</dd>
							</dl>
						</form>
					</div>
				</div>

				<div class="bui-dialog bui-overlay bui-ext-position x-align-cc-cc hide" id="deleAlert">
					<div class="bui-stdmod-header">
						<div class="header-title">提示消息</div>
					</div>
					<div class="bui-stdmod-body">
						<br>
						<br>
						<div>
							<div class="x-icon me-error"></div>
							<span class="me-bd f20" id="dialogMsg">确定要删除被保险人吗</span>
						</div>
						<br>
						<br>
					</div>
					<div class="bui-stdmod-footer">
						<button id="dialog_ok" class="button button-primary" onclick="doDel();">
							确定
						</button>
						<button id="dialog_cancel" class="button button-block" onclick="closedelDialog();">
							取消
						</button>
					</div>
					<a tabindex="0" href='javascript:void("关闭")' role="button" class="bui-ext-close" style="">
						<span class="bui-ext-close-x x-icon x-icon-normal" id="closedelDialog">
					×
				</span>
					</a>
				</div>

				<div class="bui-dialog bui-overlay bui-ext-position x-align-cc-cc hide" id="content2">
					<div class="bui-stdmod-header">
						<div class="header-title">
						</div>
					</div>
					<div class="bui-stdmod-body">
						<form class="form-horizontal" id="addMar">
							<input type="hidden" id="dataIndex" name="" />
							<div class="row">
								<div class="control-group span8">
									<label class="control-label">
										<font class="required">
									*
								</font> 姓名：
									</label>
									<div class="controls" style="height:initial;">
										<input name="name" type="text" id="zaName" class="input-normal control-text" maxlength="20">
										<div id="nameError" class="txerror">
										</div>
									</div>
								</div>
								<div style="clear:both;">
								</div>
								<div class="control-group span8">
									<label class="control-label">
										<font class="required">
									*
								</font> 证件类型：
									</label>
									<div class="controls" style="height:initial;">
										<select id="selecs" name="certificateType" class="input-normal">
											
											<option value="I">
												身份证
											</option>
											
										</select>
										<div id="selecsError" class="txerror">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div style="clear:both;">
								</div>
								<div class="control-group span8 ">
									<label class="control-label">
										<font class="required">
									*
								</font> 证件号码：
									</label>
									<div id="range1" class="controls" style="height:initial;">
										<input id="zhengj" name="certificateNo" class="input-normal control-text" type="text" data-rules="{required:true,maxlength:18}">
										<div id="zhengjError" class="txerror">
										</div>
									</div>
								</div>
								<div style="clear:both;">
								</div>
								<div class="control-group span8">
									<label class="control-label">
										<font class="required">
									*
								</font> 出生日期:
									</label>
									<div class="controls" style="height:initial;">
										<input id="personlimit" name="_fm.ente._0.d" value="" readonly="readonly" class="u-calendar w300" type="text" data-rules="{required:true}" rules-type="1">
										<div id="personlimitError" class="txerror">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div style="clear:both;">
								</div>
								<div class="control-group span8 ">
									<label class="control-label">
										<font class="required">
									*
								</font> 性别：
									</label>
									<div class="controls" style="height:initial;">
										<select id="range" name="gender" class="input-normal" data-rules="{required:true}">
											<option value="">
												请选择
											</option>
											<option value="M">
												男
											</option>
											<option value="F">
												女
											</option>
										</select>
										<div id="rangelimitError" class="txerror">
										</div>
									</div>
								</div>
								<div style="clear:both;">
								</div>
								<div class="control-group span8">
									<label class="control-label">
										<font class="required">
									*
								</font> 手机：
									</label>
									<div class="controls">
										<input name="phone" id="phone" type="text" data-rules="{regexp:[/^1[3|4|5|8|7|9][0-9]\d{8}$/,'手机号码为11位数字']}" maxlength="11" class="input-normal control-text">
										<div id="phoneError" class="txerror">
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="bui-stdmod-footer">
						<button class="button button-primary" onclick="subitDialog();">
							确定
						</button>
						<button class="button button-block" onclick="closeDialog();">
							取消
						</button>
					</div>
					<a tabindex="0" href='javascript:void("关闭")' role="button" class="bui-ext-close" style="">
						<span class="bui-ext-close-x x-icon x-icon-normal" id="closeDialog">
					×
				</span>
					</a>
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
	<div id="addPersonPop" class="za-nt-pop">
		<a class="btn-close" href="javascript:;"></a>
		<iframe id="personFrame" width="1050" height="600"></iframe>
	</div>
	<script src="../../resource/res/js/jquery-1.8.1.min.js"></script>
	<script src="../../resource/res/js/bui-min.js"></script>
	<script src="../../resource/res/js/common.js"></script>
	<script src="../../resource/res/js/entcommon.js"></script>
	<script src="../../resource/res/js/ajaxfileupload.js"></script>
	<script src="../../resource/res/js/amountFormat.js"></script>
	<script src="../../resource/res/js/zaupload.min.js"></script>
	<script type="text/javascript" src="../../js/za-index.js?ver=<%=jsVer%>"></script>
	<script src="../../web/js/eportal/order/insure1.js?ver=<%=jsVer%>"></script>
	<script src="../../js/flow_node.js?ver=<%=jsVer%>"></script>
	
	<!--<script type="text/javascript" src="../../js/order1.js"></script>-->
    <%@include file="/manager/fooder.jsp" %>
  
</body>
</html>
