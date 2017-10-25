﻿﻿<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="">
<title>企微-重设密码</title>
<link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
<link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
<script src="js/jquery-1.11.1.js"></script>
<script src="js/common.js?ver=<%=jsVer%>"></script>
<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
<script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
</head>
<body>
<%@include file="./header.jsp" %>  
   <div class="container"style="margin-top:50px">

    <div class="content">
    <div class="password_box">
  <h2 class="title">重设密码</h2>
  <form id="from1" action="${baseURL}/portal/experienceapplication/expappAction!RetrievePassWord2.action" method="post" onsubmit="return false;">
  	<input type='hidden' name="code" value="${param.code}" id="code">
  <div id="step3">
    <div class="ps_con" >
      <p class="p_con"> 现在您可以重新设定您的企微密码。 </p>
      <div style="display:none" id="username">
	      <p style="margin-top:10px;" class="p_con" id="userId">账号：</p>
	      <input type="text" size="20" name="userName" id="user" value="" style="display:none"/>
      </div>
      <p style="margin-top:10px;" class="p_con">新密码：</p>
      <input type="password" size="20" name="password" id="password" placeholder='6-16个字符，必须字母数字组合'>
      <p class="tips_con1"><img src="./images/error.png"><span class="tips_con">您输入的密码不符合规则</span></p>
      <p style="margin-top:10px;" class="p_con"> 重复输入新密码：</p>
      <input type="password" size="20" name="confirmpwd" id="enterpassword" placeholder='6-16个字符，必须字母数字组合'>
      <p class="tips_con1"><img src="./images/error.png"><span class="tips_con">您输入的密码不符合规则</span></p>
    </div>
      <div class="tool_bar">
      <input type="submit" value="确定" class="bt_action" id="submit">    
      </div>
    </div>
    <div id="step4">
    <div class="ps_con">
      <div class="left_con"> <img alt="成功" src="./images/true.png" class="ico_suc"></div>
      <div class="right_con">
        <h3 class="p_con">您的企微云平台登录密码已重设成功</h3>
        <p class="p_con">
          现在您可以用新密码登录您的微信账号。
        </p>
      </div>
    </div>
    <div class="tool_bar">
      
      <a class="bt_action" href="${qwyURL}">完成</a>
    </div>
    </div>
  </form>
</div></div></div>
<%@include file="./footer.jsp" %>  
  <script type="text/javascript">
  	$(function(){
  	  var code="${param.code}";
	  $.ajax({
		   url:"${baseURL}/portal/experienceapplication/expappAction!getUserByEmail.action",
		   type:"POST",
		   async:false,
		   data:{code:code},
		   dataType:"json",
		   success:function(result){
			   if(result.code=="0"){
				   if(result.data.isExit=="0"){
						$("#username").show();
					  	$("#user").show();
				   }else{
					   $("#username").show();
					   $("#userId").html("账号："+result.data.userName);
					   
				   }
			   }else{
				   _alert("",result.desc);
			   }
		   },
	});
    var password_num=0;
	$('#password').blur(function(){
	var password=$('#password').val();
	if(password.length<6){
	 $(this).next('.tips_con1').find('span').html('密码长度不能少于6位');
	$(this).next(".tips_con1").show();
	return false;
	}
	if(password.length>16){
	 $(this).next('.tips_con1').find('span').html('密码长度不能大于16位');
	$(this).next(".tips_con1").show();
	return false;
	}
	var count = 1;
	var chars=password.split("");
	for (var i = 1; i < password.length; i++) {
	if (chars[i] == chars[i-1]) {
	count++;
	if (count > 2){
	 $(this).next('.tips_con1').find('span').html('密码中不能含有连续相同的3个字符');
	$(this).next(".tips_con1").show();
	return false;
	}
	}else{
	count=1;
	}}
	if(!(/(\d+.*[a-zA-z]+)|([a-zA-z]+.*\d+)/).test($('#password').val())){
	for(var j=0;j<password.length;j++){
	if((/^[A-Za-z]$/).test(chars[j])){
	$(this).next(".tips_con1").show();
	 $(this).next('.tips_con1').find('span').html('密码不能是纯字母');
	return false;
	}else if((/^[0-9]$/).test(chars[j])){
	$(this).next(".tips_con1").show();
	 $(this).next('.tips_con1').find('span').html('密码不能是纯数字');return false;}
	}
	return false;
	}else{ $(this).next(".tips_con1").hide();
	password_num++;
	}
	}); 
    $('#enterpassword').blur(function(){
      if($('#enterpassword').val()!=$('#password').val()){
         $(this).next('.tips_con1').find('span').html('二次输入的密码不一致');
        $(this).next(".tips_con1").show();
        return false
      }
      
      $(this).next(".tips_con1").hide();
    });
    
    $('#from1').jCryption({
        submitElement: $('#submit'),
        beforeEncryption: function () {
            if(password_num==0){
              $('#password').blur();
              $('#password').focus();     
              return false;
            }
            if($('#enterpassword').val()!=$('#password').val()){
               $('#enterpassword').blur();
              $('#enterpassword').focus();
              return false;
            }
            //transform();
            return true;
        },
	    finishEncryption: function ($formElement, encrypted) {
	    	$formElement.ajaxSubmit({
				dataType:'json',
	   			async:false,
				success:function(result){
					   if(result.code=="0"){
					      $('#step3').hide();
					      $('#step4').show();
					   }else{
						   _alert("",result.desc);
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