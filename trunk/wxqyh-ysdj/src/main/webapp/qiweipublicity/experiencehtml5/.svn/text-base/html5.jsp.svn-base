<%@page import="org.apache.struts2.components.Include"%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">    
<%@include file="/jsp/common/dqdp_common_dgu.jsp"%>
<head>
<meta charset="utf-8">
<title>开启微信办公新时代</title>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
<link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
<script type="text/javascript" src="${baseURL}/jsp/wap/js/zepto.min.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/detect.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/ku.js?ver=<%=jsVer%>"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/main.js?ver=<%=jsVer%>"></script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
<script src="${baseURL}/jsp/wap/js/zepto.form.js"></script>
<!--组件依赖css begin-->
	    <link rel="stylesheet" type="text/css" href="${baseURL}/jsp/wap/demo/calendar/assets/calendar.css" />
<!--组件依赖css end-->

<link rel="stylesheet" href="css/fullPage.css">
<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/jquery-ui-1.10.3.min.js"></script>
<script src="js/fullPage.min.js"></script>
<script>

$(document).ready(function() {
	
	$.fn.fullpage({
		slidesColor: ['#ef8472', '#008cb3', '#17ae57', '#ffba5f', '#ff6752','#ff8b4b', '#007ab3', '#573895', '#cc3d27'],
		anchors: ['page1', 'page2', 'page3', 'page4', 'page5','page6', 'page7', 'page8', 'page9'],
		afterLoad: function(anchorLink, index){
			if(index == 1){
				$('.section1').find('hgroup.LogoT').addClass('animation');
				$('.section1').find('section').addClass('F-midPic');
				$('.section1').find('aside em').addClass('clound');
				$('.section1').find('.F-botNoteBg div').addClass('boxPancel');				
				$('.section1').find('aside div').addClass('F-bomNote');
				$('.section1').find('nav div').addClass('light');
				
			}
			if(index == 2){
				$('.section2').find('section div').addClass('air');
				$('.section2').find('article div').addClass('band');
				
				$('.section2').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 3){
				$('.section3').find('section div').addClass('member');
				$('.section3').find('aside div').addClass('circleBg');
				$('.section3').find('header div').fadeIn();
				$('.section3').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 4){
				$('.section4').find('section div').addClass('member email');
				$('.section4').find('aside div').addClass('circleBg circleBgYellow');
				$('.section4').find('header div').fadeIn();
				$('.section4').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 5){
				$('.section5').find('section div').addClass('member phone');
				$('.section5').find('aside div').addClass('circleBg phoneBg');
				$('.section5').find('header div').fadeIn();
				$('.section5').find('nav div').addClass('letter');
				$('.section5').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 6){
				$('.section6').find('section div').addClass('member applyLeft');
				$('.section6').find('aside div').addClass('circleBg arrors');
				$('.section6').find('header div').fadeIn();
				$('.section6').find('nav div').addClass('member applyRight');
				$('.section6').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 7){
				$('.section7').find('section div').addClass('box member applyLeft');
				$('.section7').find('aside div').addClass('circleBg arrors book');
				$('.section7').find('header div').fadeIn();
				$('.section7').find('nav div').addClass('member package');
				$('.section7').find('article div').addClass('member pig');
				$('.section7').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 8){
				$('.section8').find('section div').addClass('member map');
				$('.section8').find('aside div').addClass('circleBg location');
				$('.section8').find('header div').fadeIn();			
				$('.section8').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
			if(index == 9){
				$('.section9').find('section div').addClass('member user');
				$('.section9').find('hgroup').addClass('footNote boundFade').fadeIn();
				$('.section9').find('header div').fadeIn();				
				$('.section9').find('p').animate({
					left: '0'
				}, 1500, 'easeOutExpo');
			}
		},
		onLeave: function(index, direction){
			if(index == 1){
				$('.section1').find('hgroup.LogoT').removeClass('animation');
				$('.section1').find('section').removeClass('F-midPic');
				$('.section1').find('aside em').removeClass('clound');
				$('.section1').find('.F-botNoteBg div').removeClass('boxPancel');				
				$('.section1').find('aside div').removeClass('F-bomNote');
				$('.section1').find('nav div').removeClass('light');
			}
			if(index == '2'){
				$('.section2').find('section div').removeClass('air');
				$('.section2').find('article div').removeClass('band');
				$('.section2').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == '3'){
				$('.section3').find('section div').removeClass('member');
				$('.section3').find('aside div').removeClass('circleBg');
				$('.section3').find('header div').fadeOut();
				$('.section3').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == '4'){
				$('.section4').find('section div').removeClass('member email');
				$('.section4').find('aside div').removeClass('circleBg circleBgYellow');
				$('.section4').find('header div').fadeOut();
				$('.section4').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == '5'){
				$('.section5').find('section div').removeClass('member phone');
				$('.section5').find('aside div').removeClass('circleBg phoneBg');
				$('.section5').find('header div').fadeOut();
				$('.section5').find('nav div').removeClass('letter');
				$('.section5').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == '6'){
				$('.section6').find('section div').removeClass('member applyLeft');
				$('.section6').find('aside div').removeClass('circleBg arrors');
				$('.section6').find('header div').fadeOut();
				$('.section6').find('nav div').removeClass('member applyRight');
				$('.section6').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == '7'){
				$('.section7').find('section div').removeClass('box member applyLeft');
				$('.section7').find('aside div').removeClass('circleBg arrors book');
				$('.section7').find('header div').fadeOut();
				$('.section7').find('nav div').removeClass('member package');
				$('.section7').find('article div').removeClass('member pig');
				$('.section7').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == 8){
				$('.section8').find('section div').removeClass('member map');
				$('.section8').find('aside div').removeClass('circleBg location');
				$('.section8').find('header div').fadeOut();			
				$('.section8').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
			if(index == 9){
				$('.section9').find('section div').removeClass('member user');
				$('.section9').find('hgroup').removeClass('footNote boundFade').fadeOut();	
				$('.section9').find('header div').fadeOut();				
				$('.section9').find('p').animate({
					left: '-120%'
				}, 1500, 'easeOutExpo');
			}
		}
	});
});
$(function(){
	   dataForWeixin.url= qwyURL + "/qiweipublicity/experiencehtml5/html5.jsp";
	   dataForWeixin.title="移动办公原来可以在微信上这么玩，完全免费使用哦！";
	   dataForWeixin.desc="请填写您企业的名称，以及联系人信息。感谢您关注道一企业微信产品。";
	   dataForWeixin.MsgImg="http://wx.qlogo.cn/mmhead/Q3auHgzwzM5PiaLhMITIwJdSuic6skG1UnRAMtxeC2HMxXtMYIeRjtxA/0";
	   dataForWeixin.TLImg="http://wx.qlogo.cn/mmhead/Q3auHgzwzM5PiaLhMITIwJdSuic6skG1UnRAMtxeC2HMxXtMYIeRjtxA/0";
	   dataForWeixin.fakeid="do1";

});
</script>
</head>

<body>
<div class="section section1">	
	<div class="animation">
		<hgroup class="LogoT">
			<h3>“微信办公之门，</h3>
			<h3>此刻为你而开”</h3>
		</hgroup>		
     </div>
     
     <section class="F-midPic"></section>
     
     <aside>
     	<em class="clound"></em>
     </aside> 
     
     <article class="F-botNoteBg">
          	<div class="boxPancel"></div>
     </article>
     
     <aside class="F-bomNote">
     	<div>
	     	<p>企微</p>
	     	<p>基于企业微信，开启微信办公新时代</p>
     	</div>
     </aside>
          
     <nav>
     	<div class="light"></div>
     </nav>	
             
	<a class="godown" href="#page2"><span></span></a>	
</div>

<div class="section section2" data-anchor="page2">
	<div class="S-topNote">
		<p>在这些"碎片化"场景下</p>
		    <p>机场的候机厅、车站的候车室、咖啡厅、客户现场……</p>
		<p>工作起来更爽,更轻松</p>
	</div>
	
	<div class="S-Middle"></div>
	
	<section>
		<div></div>
	</section>
	
	<article>
		<div></div>
			<div class="band-second"></div>
				<div class="band-third"></div>
				<div class="band-fouth"></div>
			<div class="band-fifth"></div>
	</article>
	
	<div class="grayBom"></div>
	
	<a class="godown" href="#page3"><span></span></a>
</div>

<div class="section section3" data-anchor="page3">
	<div class="T-topNote">
		<p><span>会议助手</span>让会议预订更简单</p>
			<p>组织内部平时开会少不了，可临时开会，出差在外，<br />找会议室可麻烦</p>
			     <p>直接微信上查看公司的会议室占用状态</p>
			<p>发起后实时通知与会人和相关人</p>
		<p>还可以上传会议纪要，事后跟踪</p>
		<header>
			<div class="btnBoxVisible">
				<a href="apply.jsp" class="btn apply">开通使用</a>
			</div>			
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>	
	
	<a class="godown" href="#page4"><span></span></a>
</div>

<div class="section section4" data-anchor="page4">
	<div class="T-topNote">
		<p><span>组织通讯录</span></p>
	      	<p>随时在微信上找到同事，即使TA不是你的通讯录好友</p>
		<p>发消息、打电话，就这么轻易的实现跨部门沟通</p>		
		<header>
			<div class="btnBoxVisible">
			    <a href="apply.jsp" class="btn apply red">开通使用</a>
			</div>
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>	
	
	<a class="godown" href="#page5"><span></span></a>
</div>

<div class="section section5" data-anchor="page5">
	<div class="T-topNote">
		<p><span>重要动态</span></p>
		     <p>第一时间向你传达组织重要消息</p>
		<p>高层动态、部门活动、小组近况、员工福利，还<br />有很多我们都没有想到的重要消息</p>		
		<header>
			<div class="btnBoxVisible">
			    <a href="apply.jsp" class="btn apply deepRed">开通使用</a>
			</div>			
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>
	
	<nav>
		<div></div>
	</nav>	
	
	<a class="godown" href="#page6"><span></span></a>
</div>

<div class="section section6" data-anchor="page6">
	<div class="T-topNote">
		<p><span>任务转派</span></p>
			<p>业务上的临时协作，不需要复杂的OA流程，让<br />业务团队的协作沟通更简单和顺畅</p>
			<p>随时发起协作任务，到期自动关闭任务</p>
		<p>强大的后台任务统计跟踪，日常零散的、非标<br />准化的工作更加规范、有效跟踪</p>		
		<header>
			<div class="btnBoxVisible">
			   <a href="apply.jsp" class="btn apply deepRed">开通使用</a>
			</div>				
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>
	
	<nav>
		<div></div>
	</nav>	
	
	<a class="godown" href="#page7"><span></span></a>
</div>

<div class="section section7" data-anchor="page7">
	<div class="T-topNote">
		<p><span>知识百科</span></p>
				<p>随时携带的产品知识库，外出拜访的好伴侣</p>
				<p>随时搜索公司产品信息，产品概况、应用场<br />景、功能价值、应用案例应有尽有</p>
		<p>还可以随时学习掌握产品，把握营销方向</p>		
		<header>
			<div class="btnBoxVisible">
			   <a href="apply.jsp" class="btn apply deepBlue">开通使用</a>
			</div>				
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>
	
	<nav>
		<div></div>
	</nav>
	
	<article>
		<div></div>
	</article>
	
	<a class="godown" href="#page8"><span></span></a>
</div>

<div class="section section8" data-anchor="page8">
	<div class="T-topNote">
		<p><span>移动外勤</span></p>
		   <p>随时记录你的拜访情况</p>
		<p>强大的拜访统计，直观展示你的拜访频率、销售<br />状态，让你更合理的安排客户回访，推进销售进度</p>
		
		<header>
			<div class="btnBoxVisible">
			    <a href="apply.jsp" class="btn apply zSE">开通使用</a>
		    </div>				
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<aside>
		<div></div>
	</aside>	
	
	<a class="godown" href="#page9"><span></span></a>
</div>

<div class="section section9" data-anchor="page9">
	<div class="bottomNote">	
		<p>微信第一批优秀企业微信合作商 </p>
		<p>近<span>500</span>家企业正在使用我们的企微</p>		
		<header>
			<div class="btnBoxVisible">
			    <a href="apply.jsp" class="btn apply RedI">开通使用</a>
		    </div>				
		</header>		
	</div>
	
	<section>
		<div></div>
	</section>
	
	<hgroup>
		<h3>道一公司&企微产品团队</h3>
		<h3>产品官网：wbg.do1.com.cn</h3>
	</hgroup>	
</div>
</body>
</html>