﻿<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="">
<title>企微-绑定登录账号</title>
<link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
<link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
<script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
<script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
<script src="js/common.js?ver=<%=jsVer%>"></script>
</head>
<body>
<%@include file="./header.jsp" %>  
   <div class="container"style="margin-top:50px">

    <div class="content">
    <div class="password_box">
  <h2 class="title">验证登录密码</h2>
  <form id="from1" action="${baseURL}/portal/userLoginAction!weixinLoginReset.action" method="post" autocomplete="off" onsubmit="return false;">
    <input type='hidden' name='loginId' value="${param.id}"/>
    <input type='hidden' name='state' value="${param.state}"/>
	  <div id="step1">
	    <div class="ps_con" >
	      <p class="p_con"> 您的账号密码已经修改，为了账号的安全，请输入密码重新绑定 </p>
	      <p style="margin-top:10px;" class="p_con">密码：</p>
	      <input type="password" size="20" name="j_password" id="password" placeholder='请输入登录密码'>
	      <p class="tips_con1" id="tips_con2"><img src="./images/error.png"><span class="tips_con">请输入登录密码</span></p>
	    </div>
	      <div class="tool_bar">
	      <input type="submit" value="确定" class="bt_action" id="submit">    
	      </div>
	    </div>
  </form>
  <form id="id_form_login" action="${baseURL}/j_spring_security_check" method="post" autocomplete="off">
  	<input type='hidden' name='_spring_security_remember_me' value="true"/>
	<input type='hidden' name="j_username" id="j_username" value="${param.state}"/>
	<input type='hidden' name="j_password" id="j_password">
    <button id="btn_login" type="button" style="display: none;"></button>
  </form>
</div></div></div>
<%@include file="./footer.jsp" %>  
  <script type="text/javascript">
  	$(function(){
  		$('#from1').jCryption({
  	        submitElement: $('#submit'),
  	        beforeEncryption: function () {
  	          if($('#password').val()==""){
  	            $("#tips_con2").show();
  	            return false;
  	          }
  	          else{
  	          	$("#tips_con2").hide();
  	          }
  	          return true;
  	        },
  		    finishEncryption: function ($formElement, encrypted) {
  		    	$formElement.ajaxSubmit({
  					dataType:'json',
  		   			async:false,
  					success:function(result){
  						if(result.code=="0"){
  						   //验证通过，需要form提交到后台登录并重定向页面
  				    	    //$("#btn_login").trigger("click");//模拟执行id=a的事件
							$("#j_username").val(result.data.code);
							$("#id_form_login").submit();
  						}
  						else{
  							$("#j_password").val("");
  				        	_alert("提示信息",result.desc);
  						}
  					},
  					error:function(){
  			        	_alert("提示信息","网络错误");
  					}
  				});
  		    }
  	    });
  })

  </script>
 <%@include file="../../manager/msgBoxs.jsp" %> 
 </body>
</html>