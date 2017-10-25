<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    
    <!-- 众安样式 -->
    <link href="resource/stylesheets/common.css?myver=123&ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/za-common.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/slider-min.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/za-add.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="resource/stylesheets/za-ent.css?ver=<%=jsVer%>" rel="stylesheet" />
	 <link href="resource/stylesheets/newEnterprise/index.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${baseURL}/manager/benefit/zhongan/css/zn-main.css?ver=<%=jsVer%>">
	
	<!-- 众安样式 end -->
	
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript">var BASE_URL = "${baseURL}";</script>
    
 
    <style type="text/css">
    	#container{width: 100%;}
    </style>
    
   <!--  <link href="../../experience2/css/bootstrap.css" rel="stylesheet"> -->
    <link href="../../experience2/css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    
</head>
<body>

    <%@include file="../../experience2/header.jsp" %>
    
    <div class="wrap-container">
        <div id="container" class="clearfix">
        
<!--         	<div class="za-guide w1200" style="display:none;">
        		<div>
					<a href="../main.jsp">员工福利</a>
					<i>></i>
					<a class="active" href="javascript:;">员工商业保险</a>
				</div>
				<div class="za-r-guide">
					<a href="content-main.jsp?target=insurenceInfo_li">投保资料</a>
					<i></i>
					<a href="content-main.jsp?target=orderList_li">我的订单</a>
				</div>
			</div> -->
			<!-- banner -->
			
			<div class="g-doc-w g-mar-auto w-100">
			
				<div class="m-sld">
					<!--<div class="eportal_banner_word1 animation_word1"></div>
					<div class="eportal_banner_word2 animation_word2"></div>
					<div class="eportal_banner_word3 animation_word3"></div>
					<div class="eportal_banner_icon_rm">
						<span class="eportal_banner_icon1 animation"></span>
						<span class="eportal_banner_icon2 animation1"></span>
						<span class="eportal_banner_icon3 animation2"></span>
						<span class="eportal_banner_icon4 animation3"></span>
					</div>		-->
				</div>
		
			</div>
		
			<!-- banner end -->
			
			<!-- main -->
			<div class="main">
			
				<div class="s-bgc-gray m-adv-min900 za-clearfix" style="overflow:hidden;" name="bz-jh">
					<div class="za-tc-hd f666">
						<div class="w1200">
							<h1 class="f333">选择套餐</h1>
							<div class="za-r-guide" id="za-b-guide">
								<a href="javascript:;"><span class="za-bx-icon-1"></span>投保须知</a>
								<a href="javascript:;"><span class="za-bx-icon-2"></span>理赔流程</a>
								<a href="javascript:;"><span class="za-bx-icon-3"></span>我们的优势</a>
								<a href="javascript:;"><span class="za-bx-icon-4"></span>产品特点</a>
							</div>
						</div>
					</div>
					<ul class="tc-list tc-ul" id="zaTcList">
						 <li class="active">
							<div class="tc-p">¥68</div>
							<div class="tc-t">68元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥188</div>
							<div class="tc-t">188元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥248</div>
							<div class="tc-t">248元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥368</div>
							<div class="tc-t">368元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥398</div>
							<div class="tc-t">398元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥518</div>
							<div class="tc-t">518元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥578</div>
							<div class="tc-t">578元套餐</div>
						</li>
						<li class="">
							<div class="tc-p ">¥698</div>
							<div class="tc-t">698元套餐</div>
						</li> 
					</ul>
					<table class="m-adv-tab">
						<tr>
							<td class="wid350">
								<div class="m-adv-item2 b1 border-r-3 active" id="eportal_product_01">
									<div class="m-adv-itemtd">
										<i class="bg-ins ins1"></i>
										<div class="m-adv-tt">商旅意外</div>
										<p class="m-adv-mt">适用人群：18-60周岁</p>
										<i class="btn-select"></i>
									</div>
									<ul class="m-plan orange">
										<li class="td"><span>保障项目</span><span class="fr fs">保障额度</span></li>
										<li><span>飞机意外身故/伤残</span><span class="fr">1,000,000元</span></li>
										<li><span>其他营运交通工具意外身故/伤残</span><span class="fr">100,000元</span></li>
										<li><span>意外身故/残疾/烧烫伤</span><span class="fr">50,000元</span></li>
										<li><span>意外医疗<span class="tips-t">（次免赔额100元，赔付比例90%）</span></span><span
											class="fr">10,000元</span></li>
									</ul>
								</div>
							</td>
							<td>
								<div class="m-adv-item2 b2 border-r-3" id="eportal_product_02">
									<div class="m-adv-itemtd">
										<i class="bg-ins ins2"></i>
										<div class="m-adv-tt">重疾及疾病身故</div>
										<p class="m-adv-mt">适用人群：18-49周岁</p>
										<i class="btn-select"></i>
									</div>
									<ul class="m-plan orange">
										<li class="td"><span>保障项目</span><span class="fr fs">保障额度</span></li>
										<li><span>重大疾病</span><span class="fr">50,000元</span></li>
										<li><span>疾病身故</span><span class="fr">50,000元</span></li>
									</ul>
								</div>
							</td>
							<td rowspan="2" class="wid350">
								<div class="m-adv-item b2" style="height:497px;">
									<div class="m-adv-itemtd">保费试算</div>
		
									<div class="m-adv-bd">
										<div class="m-adv-pre">
											<p class="f-s18">
												<span class="pre">￥</span><font id="pri1" class="pre">68</font>/起
											</p>
											<p class="g-mar-t10" id="costExplain1" style="display:none;">
												<span>18-39周岁：<span id="smallCost">188</span>元/份</span><span class="g-mar-l20">40-49周岁：<span id="bigCost">470</span>元/份</span>
											</p>
										</div>
										<hr class="m-plan-hr" />
										<div class="m-input-box za-clearfix m-t20">
											<span id="age18">人数：</span>
											<div id="" class="f16">
												<input id="insNum1" class="u-text insNum"> 
												<font id="ageperson"></font>
											</div>
										</div>
										<div class="m-input-box za-clearfix" id="bigage">
											<span>40-49周岁</span>
											<div id="" class="f16">
												<input id="insNum2" class="u-text insNum"> 人
											</div>
										</div>
										<div class="m-plan-txt f-s14 heightAuto" id="costExplain2" style="display:none;">
											<p>40-49周岁员工占比不超过投保总人数30%</p>
										</div>
										<p class="pricesum">
											总保费：
											<span id="pricesum1" class="pricesum" style="font-size:30px;">￥0</span>
										</p>
									</div>
									<div class="m-adv-itemfd" style="display:none;">
										<a class="m-adv-buy u-btn btn-orange" href="javascript:;">立即购买</a>
									</div>
								</div>
							<td>
						</tr>
						<tr class="">
							<td class="wid350">
								<div class="m-adv-item2 b1 border-r-3" id="eportal_product_03">
									<div class="m-adv-itemtd">
										<i class="bg-ins ins3"></i>
										<div class="m-adv-tt">门急诊</div>
										<p class="m-adv-mt">适用人群：18-49周岁</p>
										<i class="btn-select"></i>
									</div>
									<ul class="m-plan orange">
										<li class="td"><span>保障项目</span><span class="fr fs">保障额度</span></li>
										<li><span>门诊急诊医疗</span><span class="fr">5,000元</span></li>
										<li class="m-plan-txt"><p>次免赔额100元，使用社保赔付70%，不使用社保赔付60%</p></li>
									</ul>
								</div>
							</td>
							<td class="">
								<div class="m-adv-item2 b2 border-r-3" id="eportal_product_04">
									<div class="m-adv-itemtd">
										<i class="bg-ins ins4"></i>
										<div class="m-adv-tt">住院及住院津贴</div>
										<p class="m-adv-mt">适用人群：18-49周岁</p>
										<i class="btn-select"></i>
									</div>
									<ul class="m-plan orange">
										<li class="td"><span>保障项目</span><span class="fr fs">保障额度</span></li>
										<li><span>住院津贴（最长90天）</span><span class="fr">50元/天</span></li>
										<li><span>疾病住院</span><span class="fr">5000元</span></li>
										<li class="m-plan-txt"><p>次免赔额300元，使用社保赔付80%，不使用社保赔付70%</p></li>
									</ul>
								</div>
							</td>
						</tr>
					</table>
					<!--<div class="l-j-bj">
						<i class="ic-fj"></i>想要定制专属的个性化方案，请告诉我们您的需求<a
							class="u-btn m-adv-buy2" href="javascript:;">立即报价</a>
					</div>-->
				</div>
				<div class="za-ins-hd"></div>
				<div class="l-j-bj za-ins-c-green">
					<i class="icon_notice"></i>本网页保险产品通过【企微云平台】进行销售，由众安在线财产保险股份有限公司（简称“众安保险”）承保。
				</div>
				<div class="w1200">
					<div class="za-tc-hd f666">
						<h1 class="f333">常见问题</h1>
					</div>
				</div>
				<div class="g-main za-ques-cont" name="qu-an">
					<div class="qa">
						<div class="qa-scroll">
							<div class="qa-inner">
								<div class="qa-list">
									<i class="ic-qa ic-qa-0"></i>
									<div class="ques">
										Q：众安员工商业险与企微云平台的关系？
									</div>
									<p class="ans">A：本网页的“众安员工商业险”是通过【企微云平台】进行销售的互联网保险服务，由“众安在线财产保险股份有限公司”承保。（该企业由蚂蚁金服、中国平安、腾讯等公司联手设立）</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-1"></i>
									<div class="ques">
										Q：有了社保，还需要这个保险吗？
									</div>
									<p class="ans">A：根据您选择的保障范围，我们提供因意外事故造成意外伤害给付身故、残疾以及意外医疗保险金，重疾、疾病身故保险金、 门急诊和住院费用，还有住院津贴，是社保的极好补充，是企业团体为员工提供的最好福利。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-3"></i>
									<div class="ques">
										Q：如何获取发票和保单？
									</div>
									<p class="ans">A： 支付成功后，您只需提供发票寄送地址，我们将免费寄送纸质发票。您也同时在线下载电子保单。根据《中华人民共和国合同法》规定，电子保单属合法的合同。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-4"></i>
									<div class="ques">
										Q： 航空意外险保什么？
									</div>
									<p class="ans">A： 离港机场通过安全检查时开始承保，至抵达航班目的港走出所乘航班班机的舱门时止；适用于全球航空。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-5"></i>
									<div class="ques">
										Q：其他营运交通工具有哪些？
									</div>
									<p class="ans">A： 指火车（含地铁、轻轨）、汽车（含电车、有轨电车）、轮船（含客船、渡船、游船）。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-6"></i>
									<div class="ques">
										Q：重大疾病责任保什么？
									</div>
									<p class="ans">A：对于合同约定的30种重大疾病，若不幸身患其中的任意一种，即可获得理赔金。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-7"></i>
									<div class="ques">
										Q： 什么是等待期？
									</div>
									<p class="ans">A： 又称观察期，或免责期，是指在指定时期内，即使发生保险事故，受益人也不能获得保险赔偿，这段时期称为等待期。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-8"></i>
									<div class="ques">
										Q：为什么要设置等待期？
									</div>
									<p class="ans">A：重疾和疾病身故的等待期均为60天，疾病门诊和疾病住院的等待期均为30天。续保不受等待期的限制。意外责任无等待期。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-9"></i>
									<div class="ques">
										Q： 这个产品的等待期是几天？
									</div>
									<p class="ans">A：又称责任免除，指保险人依照法律规定或合同约定，不承担保险责任的范围，是对保险责任的限制。详见条款。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa-10"></i>
									<div class="ques">
										Q： 什么是除外责任？
									</div>
									<p class="ans">A： 等待期是为了防止投保人明知道将发生保险事故，而马上投保获利的行为，也就是所说的逆选择。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa1 ic-qa-11"></i>
									<div class="ques">
										Q： 如何变更保险合同？
									</div>
									<p class="ans">A：如需更新您的客户信息或变更保险合同，请拨打众安客服热线4009999595。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa1 ic-qa-12"></i>
									<div class="ques">
										Q：我的隐私安全吗？
									</div>
									<p class="ans">A： 请放心！我们严格遵守现行的关于个人信息、数据及隐私保护的法律法规，采取充分的技术手段和制 度管理，保护您提供给我们的个人信 息、数据和隐私不受到非法的泄露或披露给未获授权的第三方。</p>
								</div>
								<div class="qa-list">
									<i class="ic-qa ic-qa1 ic-qa-13"></i>
									<div class="ques">
										Q：退保流程是怎样的？
									</div>
									<p class="ans">A： 在本保险合同成立后，您可以以书面形式通知众安保险解除本保险合同，您解除本保险合同时，应提供下列证明文件和资料： a. 退保申请书（可通过拨打客服热线获得） b.投保人身份证明； －您要求解除本保险合同，自众安保险接到保险合同解除申请书之日起，保险合同的效力终止。众安保险收到上述证明文件和资料之日起10 个工作日内退还保险单的未满期净保险费； －退保金将支付至支付保费的账户。</p>
								</div>
							</div>
						</div>
					</div>
					
					<div>
						<a href="javascript:;" class="qa-pre"></a>
						<a href="javascript:;" class="qa-next"></a>
					</div>
					
					<div class="qa-pager">
						<a href="javascript:;" class="active"></a>
						<a href="javascript:;"></a>
						<a href="javascript:;"></a>
						<a href="javascript:;"></a>
						<a href="javascript:;"></a>
					</div>
				</div>
			</div>
			<!-- main end -->
			<div id="m-dig" class="f-dn">
				<form id="m-dig-fm" method="post" autocomplete="off"
					class="p-form s-fc-tp f-fs16">
					<div class="m-form-item f-cb">
						<div class="f-fl">
							<img width="72" height="72"
								src="resource/images/enterprise/ent_success.png">
						</div>
						<div id="m-dig-con" class="f-fl g-mar-l15 g-mar-t15">
							<p>尊敬的客户您好，您的投保单尚需人工审核。请在下方</p>
							<p class="g-mar-t15">留下您的联系方式，我们的客服会尽快致电联系您。</p>
						</div>
					</div>
					<hr class="u-hr dashed" />
					<div class="m-form-item g-mar-l40">
						<span class="f-ib g-wid100 f-tar">企业名称：</span> <input
							class="u-text g-wid220" type="text" id="companyName"
							name="companyName"
							data-rules="{required:[true,'企业名称必须填写'],maxlength:50}"
							rules-type="1">
					</div>
					<div class="m-form-item g-mar-l40">
						<span class="f-ib g-wid100 f-tar">企业联系人：</span> <input
							class="u-text g-wid220" type="text" id="contactName"
							name="contactName"
							data-rules="{required:[true,'企业联系人必须填写'],maxlength:20}"
							rules-type="1">
					</div>
					<div class="m-form-item g-mar-l40">
						<span class="f-ib g-wid100 f-tar">手机号：</span> <input
							class="u-text g-wid220" type="text" id="contactPhone" name="contactPhone"
							data-rules="{regexp:[/^1[3|4|5|8|7|9][0-9]\d{8}$/,'请输入11位有效数字']}"
							rules-type="1">
					</div>
					<div class="m-form-item g-mar-l40">
						<span class="f-ib g-wid100 f-tar" style="vertical-align: top;">备注：</span>
						<textarea class="u-text g-wid220" rows="3" id="notes" name="notes"
							data-rules="{maxlength:500}" rules-type="1" value="请输入您的需求"
							onFocus="if (value =='请输入您的需求'){value ='';this.style.color=''}"
							style="color: #ccc;"
							onBlur="if (value ==''){value='请输入您的需求';this.style.color='#ccc'}"></textarea>
					</div>
				</form>
			</div>
        </div>
    </div>
    <div class="za-mask"></div>
	<div class="za-nt-pop">
		<a class="btn-close" href="javascript:;"></a>
		<div class="s-bgc-gray" name="tb-lp">
			<div class="g-main">
				
				<ul class="tc-list tc-list2 gray-img">
					<li class="active"><i class="ic ic-3"></i><div class="tc-t">投保须知</div></li>
					<li><i class="ic ic-4"></i><div class="tc-t">理赔流程</div></li>
					<li><i class="ic ic-1"></i><div class="tc-t">我们的优势</div></li>
					<li><i class="ic ic-2"></i><div class="tc-t">产品特点</div></li>
				</ul>
				
				<div class="m-flw f-cb s-fc-tp show1">
					<div class="m-flw-item">
						<div class="m-flw-td b1"></div>
						<div class="m-flw-bd f-cb">
							<div class="m-flw-step"></div>
							<ul>
								<li>填写投保信息</li>
								<li>确认投保信息</li>
								<li>支付</li>
								<li>投保成功</li>
							</ul>
						</div>
					</div>
					<div class="m-flw-item g-mar-l40">
						<div class="m-flw-td b2"></div>
						<div class="m-flw-bd f-cb m-flw-bd1">
							<div class="m-flw-step m-l30 m-flw-step4"></div>
							<ul class="add_width">
								<li>68元套餐适用年龄18-60周岁的身体健康、能正常工作和生活企业在职人，其余套餐适用年龄18-49周岁</li>
								<li >
									被保险人职业类别为1-4类，具体详见
									<!-- https://www.zhongan.com/open/common/downloadFileScreen/downloadFile?filename=zaJobSort.xls -->
									<a class="s-lku" href="${baseURL}/manager/benefit/zhongan/file/职业分类表.pdf" target="_blank">《职业类别表》</a>
								</li>
								<li>本保险计划保险期间为1年</li>
								<li>被保险人必须3人及以上同时投保</li>
								<li>本保险计划中意外医疗、门急诊医疗、住院医疗的承保区域为中华人民共和国境内（不含港澳台地区）</li>
								<li>确认投保本保险计划前，请您仔细阅读并充分理解和同意接受本《保险须知》和各方案对应的保险条款，特别是其中的保险责任、等待期、退保规则、责任免除以及投保人和被保险人义务的内容等重大事项约定</li>
								<li>如需咨询保险产品相关事宜，请联系众安保险客服热线：400-999-9595 </li>
							</ul>
						</div>
					</div>
				</div>
				<div class="m-flw f-cb s-fc-tp show2">
				<p class="za-fz14 tex_cent">发生保险事故后，众安保险将为您提供7x12小时理赔申请服务。请参考以下流程申请理赔：</p>
					<div class="m-flw-item">
						<div class="m-flw-td b3"></div>
						<div class="m-flw-bd f-cb">
							<div class="m-flw-step m-l30 m-flw-step3"></div>
							<ul>
								<li>填写理赔申请书</li>
								<li>寄送理赔资料</li>
								<li>理赔资料审核</li>
								<li>10个工作日内理赔款到账</li>
								<li>赔款资金将支付至被保险人账户或受益人账户</li>
							</ul>
						</div>
					</div>
					<div class="m-flw-item g-mar-l40">
						<div class="m-flw-td b4"></div>
						<div class="m-flw-bd f-cb">
							<div class="m-flw-step m-l30"></div>
							<ul class="">
								<li>众安官网上传理赔资料</li>
								<li>理赔资料审核</li>
								<li>3个工作日内理赔款到账</li>
								<li>赔款资金将支付至被保险人账户或受益人账户</li>
							</ul>
						</div>
					</div>
				</div>
				
				<div class="m-flw f-cb s-fc-tp show3">
					<div class="m-bg-1"></div>
				</div>
				
				<div class="m-flw f-cb s-fc-tp show4">
					<div class="m-bg-2"></div>
				</div>
				
				
			</div>	
		</div>
	</div>
	<script src="resource/res/js/jquery-1.8.1.min.js"></script>
	<script src="resource/res/js/bui-min.js"></script>
	<script src="resource/res/js/slider-min.js"></script>
	<script src="resource/res/js/common.js"></script>
	<script src="resource/res/js/unslider.min.js"></script>
	<script type="text/javascript" src="js/za-index.js"></script>
	<script src="web/js/common/common.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	
    <%@include file="../footer.jsp" %>
   
</body>
</html>