﻿<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="">
<title>企微-找回密码</title>
<link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
<link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
<script src="js/jquery-1.11.1.js"></script>
<script src="js/common.js?ver=<%=jsVer%>"></script>
<script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
</head>
<body>
<%@include file="./header.jsp" %>  
<div class="container"style="margin-top:50px">

    <div class="content">
    <div class="password_box">
      <h2 class="title">找回密码</h2>
      <form id="from1" method="post" onsubmit="return false;">
  <div id="step1">    
        <div class="ps_con">
          <p class="p_con">
            填写您绑定的邮箱地址：
          </p>
          <input name="step" value="2" type="hidden"/>
          <input name="method" value="mail" type="hidden"/>
          <input name="check" value="false" type="hidden"/>
          <input name="lang" value="zh_CN" type="hidden"/>
          <input name="t" value="w_password" type="hidden"/>
          <input name="username" value="" type="hidden"/>
          <input name="user_email" size="20" type="text"/>
          <p class="tips_con" style="display:none"><img src="./images/error.png"> 请输入正确的邮箱地址</p>
        </div>

        <div class="tool_bar">
          <input class="bt_action" value="下一步" type="submit" id="step1submit"/>
          
        </div>
      </div>
<div id="step2">
        <div class="ps_con step2" >
          <div class="left_con">
            <img alt="成功" src="./images/true.png" class="ico_suc"/>
          </div>
          <div class="right_con">
            <h3 class="p_con"></h3>
            <p class="p_con">
              请访问邮件中给出的网页链接地址，<br/>根据页面提示完成密码重设。
            </p>
          </div>
        </div>
        <div class="tool_bar">
          
          <a class="bt_default" href="">返回</a>
          <a class="bt_action" href="${qwyURL}">确定</a>
        </div>
</div>
      </form>
    </div> </div> </div>
<%@include file="./footer.jsp" %>  
<script type="text/javascript">
$(function(){
  $('#step1submit').click(function(){
	var email=$('input[name="user_email"]').val();
    if(email==0){
    $('.tips_con').show();
    $('.tips_con').html('请输入邮箱地址');
    return false;
    }
    if(!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test(email)){
    $('.tips_con').show();
    return false;
    }
	$.ajax({
	   url:"${baseURL}/portal/experienceapplication/expappAction!RetrievePassWord.action",
	   type:"POST",
	   async:false,
	   data:{email:email},
	   dataType:"json",
	   success:function(result){
		   if(result.code!="0"){
			   $('.tips_con').html(result.desc);
			   $('.tips_con').show();
		   }else{
			   $('#step1').hide();
			   $('#step2').show();
		   }
	   },
	   error:function(){
		   _alert("","系统繁忙！");
	   }
	});
  });
})
  </script>
 <%@include file="../../manager/msgBoxs.jsp" %> 
</body>
</html>