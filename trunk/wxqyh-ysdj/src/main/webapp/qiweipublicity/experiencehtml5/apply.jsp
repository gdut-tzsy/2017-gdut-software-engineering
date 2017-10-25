<%@page language="java" contentType="text/html; charset=UTF-8" %>
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
<link rel="stylesheet" href="css/style.css">
<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/jquery-ui-1.10.3.min.js"></script>
<script src="js/jquery.fullPage.min.js"></script>

<!--组件依赖css begin-->
	    <link rel="stylesheet" type="text/css" href="${baseURL}/jsp/wap/demo/calendar/assets/calendar.css" />
<!--组件依赖css end-->
</head>

<body style="background: #ebebeb;">

<header class="appleTop">
	<a class="backIndex" href="html5.html">首页</a>
	开通使用
</header>

<p class="tips">请填写真实信息，以便我们开通账号。感谢您对道一微信办公产品的关注！</p>

<section class="setionTop">
<form  id="form" onsubmit="return false;">
<article class="wrapBox">
	<dl class="DL-list">
		<dt><em>*</em>企业名称</dt>
		<dd><input class="inputPacel" type="text" name="tbQyExperienceApplicationPO.enterpriseName" id="cnname" placeholder="请输入企业名称" /></dd>
	</dl>
	<dl class="DL-list">
		<dt><em>*</em>企业规模</dt>
		<dd>
			<select class="inputPacel selectInfo"  id="select" name="tbQyExperienceApplicationPO.staffNumber">
				  <option value="1">请选择企业员工规模</option>
				  <option value="2">0~10人</option>
				  <option value="3">11~50人</option>
				  <option value="4">51~100人</option>
				  <option value="5">101~200人</option>
				  <option value="6">201~500人</option>
				  <option value="7">500人以上</option>			
			</select>
		</dd>
	</dl>
	<dl class="DL-list">
		<dt><em>*</em>联  系  人</dt>
		<dd><input class="inputPacel" type="text" name="tbQyExperienceApplicationPO.contactName"  id="name" placeholder="请输入联系人" /></dd>
	</dl>
	<dl class="DL-list">
		<dt><em>*</em>联系手机</dt>
		<dd><input class="inputPacel" type="text" name="tbQyExperienceApplicationPO.tel"  id="sj" placeholder="请输入手机号码" /></dd>
	</dl>
	<dl class="DL-list">
		<dt><em>*</em>邮        箱</dt>
		<dd><input class="inputPacel" type="text" name="tbQyExperienceApplicationPO.email" id="email" placeholder="请输入常用邮箱地址" /></dd>
	</dl>
	<dl class="DL-list">
		<dt>所在省份</dt>
		<dd>
			<select class="inputPacel selectInfo"  id="selectpro" name="tbQyExperienceApplicationPO.province">
				<option value="">请选择省份</option>
				<option value="北京">北京</option>
				<option value="广东">广东</option><option value="湖北">湖北</option><option value="湖南">湖南</option>
				<option value="广西">广西</option><option value="福建">福建</option><option value="安徽">安徽</option>
				<option value="云南">云南</option><option value="四川">四川</option><option value="贵州">贵州</option>
				<option value="澳门">澳门</option><option value="香港">香港</option><option value="台湾">台湾</option>
				<option value="江西">江西</option><option value="浙江">浙江</option><option value="江苏">江苏</option>
				<option value="河南">河南</option><option value="河北">河北</option><option value="山东">山东</option>
				<option value="山西">山西</option><option value="陕西">陕西</option><option value="甘肃">甘肃</option>
				<option value="青海">青海</option><option value="宁夏">宁夏</option><option value="辽宁">辽宁</option>
				<option value="新疆">新疆</option><option value="西藏">西藏</option><option value="海南">海南</option>
				<option value="吉林">吉林</option><option value="黑龙江">黑龙江</option><option value="内蒙古">内蒙古</option>
				<option value="天津">天津</option><option value="上海">上海</option>
				<option value="重庆">重庆</option>
			</select>
		</dd>
	</dl>
	
</article>
<input type="hidden" value="1" name="phonetype"/>
<a class="btnGet mt20" href="javascript:addApplication()" id="submit">提交申请</a>
</form>
<div class="fbtns_desc">已经申请了企业微信的企业，请通过电脑在 http://wbg.do1.com.cn/ 申请免费接入企微应用。</div>
</section>
</body>
<%@include file="/jsp/wap/include/showMsg.jsp"%>
<script>
var isPass="0";
		$(function(){
		
					   dataForWeixin.url= qwyURL + "/qiweipublicity/experiencehtml5/html5.html";
					   dataForWeixin.title="移动办公原来可以在微信上这么玩，完全免费使用哦！";
					   dataForWeixin.desc="请填写您企业的名称，以及联系人信息。感谢您关注道一企业微信产品。";
					   dataForWeixin.MsgImg="http://wx.qlogo.cn/mmhead/Q3auHgzwzM5PiaLhMITIwJdSuic6skG1UnRAMtxeC2HMxXtMYIeRjtxA/0";
					   dataForWeixin.TLImg="http://wx.qlogo.cn/mmhead/Q3auHgzwzM5PiaLhMITIwJdSuic6skG1UnRAMtxeC2HMxXtMYIeRjtxA/0";
					   dataForWeixin.fakeid="do1";
			$(window).resize(function(){
				if($(window).height() < 800){
					$('.footer').css('position','relative');
				}else {
					$('.footer').css('position','fixed');
				}
			})
			
		});
		
	$("#email").blur(function(){
		check();
	});
	$("#sj").blur(function(){
		check();
	});
	$("#cnname").blur(function(){
		var name=$('#cnname').val();
   		$.ajax({
   			url:"${baseURL}/portal/experienceapplication/expappAction!checkOrgName.action",
   			type:"POST",
   			data:{name:name},
   			dataType:'json',
   			async:false,
   			success:function (result) {
   				if(result.code=="2"){
   					showMsg("","企业名称已存在,请联系管理员",1);
   				}else{
   					isPass="1";
   				}
   			}
   		});
	});
	function check(){		//验证邮箱格式、手机格式
	   var email=$("#email").val();
	   var sj=$("#sj").val();
	   if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email) && email!=""){
		   showMsg("","请输入正确的邮箱",1);
		   return ;
	   }
	   if(!/^\d+$/.test(sj) && sj!=""){
		   showMsg("","请输入正确的手机号码",1);
		   return ;
	   }
	}
	function addApplication() {
		$("#submit").attr("href","#");
		if(isPass=="0"){
			showMsg("","企业名称已存在,请联系管理员",1);
			$("#submit").attr("href","javascript:addApplication()");
		    return ;
		}
		if($("#cnname").val()==""){
			showMsg("","请输入企业名称",1);
			$("#submit").attr("href","javascript:addApplication()");
			return ;
		}
		if($("#select").val()=='1'){
			showMsg("","请选择企业规模！",1);
			$("#submit").attr("href","javascript:addApplication()");
			return ;
		}
		if($("#name").val()==""){
			showMsg("","请输入联系人！",1);
			$("#submit").attr("href","javascript:addApplication()");
			return ;
		}
		if($("#sj").val()==""){
			showMsg("","请输入手机号码！",1);
			$("#submit").attr("href","javascript:addApplication()");
			return ;
		}
		if($("#email").val()==""){
			showMsg("","请输入邮箱！",1);
			$("#submit").attr("href","javascript:addApplication()");
			return ;
		}
    	$.ajax({
    		url:"${baseURL}/portal/experienceapplication/expappAction!ajaxAdd.action",
    		type:"POST",
    		data:$("#form").serialize(),
    		dataType:"json",
    		success:function(result){
    			if(result.code=="0"){
    				//alert("你已经成功提交申请，我们将会尽快审核并与你联系。");
    				window.location.href="${baseURL}/qiweipublicity/experiencehtml5/applySucces.html";
					//清空表单数据
    				$(':input','#form')
    				 .not(':button, :submit, :reset, :hidden')
    				 .val('')
    				 .removeAttr('checked')
    				 .removeAttr('selected');
    			}else if(result.code=="1888"){
    				showMsg("","提交过于频繁，请稍后再试！",1);
    			}else{
    				showMsg("","系统繁忙",1);
    			}
    			$("#email").val("");
    			$("#submit").attr("href","javascript:addApplication()");
    		},
    		error:function(){
    			$("#submit").attr("href","javascript:addApplication()");
    			hideLoading();
    			showMsg("",result.desc,1);
    		}
    	}); 
	}
    </script>
</html>