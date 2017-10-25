<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
  <head>
 <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="">
    <meta name="baidu-site-verification" content="Vup9CGlqbA" /> <!-- 百度检测-->
<meta name="msvalidate.01"content="6C48EF31F67FA6E09A742953ACAB5A39" /><!-- 必应搜索-->
    <title>开通</title>
    <link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src="js/jquery-1.11.1.js"></script>
    <script src="js/common.js?ver=<%=jsVer%>"></script>
    <script src=" http://hm.baidu.com/h.js%3F519281b7063399b3740aeca92f1f624f" type="text/javascript"></script>
  </head>
    <body onload="init()">
 <%@include file="./header.jsp" %> 
        <div class="container">
         <div class="content text-left yun">
         <div class="yun_top">
           
<div class="yun_top_l"><h2>企业微信接入企微云服务</h2>

<div>操作步骤比较多，请认真按照《接入企微云服务手册》进行操作。</div></div>

<a href="http://wbg.do1.com.cn/handbook.html"  class="yun_top_r" target="_blank">查看接入企微云服务手册</a>


         </div>
                <div class="stepdiv mt25">
           <div class="step first active"><i>1</i>提交企业微信信息</div><div class="steppic"><img src="images/jt-active.png"></div>
           <div class="step active"><i>2</i>注册企微平台账号</div><div class="steppic"><img src="images/jt-active.jpg"></div>
           <div class="step "><i>3</i>获取应用配置信息</div><div class="steppic"><img src="images/jt.jpg"></div>
           <div class="step"><i>4</i>企业微信应用配置</div><div class="steppic"><img src="images/jt.jpg"></div>
           <div class="step last"><i>5</i>企微应用中心</div>
          </div>
           <h2 class="text-center mt50">注册企微平台账号</h2>
          <div class="form">
            <form class="form-horizontal" onsubmit="return false;" method="post" accept-charset="utf-8" id="form">
            	<input type="hidden" value="${param.id}" name="id"></input>
                <div class="form-group mt50">
                  <label class="col-md-3 control-label">企业名称<span class="red">*</span></label>
                  <p class="form-control-static col-md-9" id="name" ></p>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">企业账号<span class="red">*</span></label>
                  <div class="col-md-9">
                  <input type="text" class="form-control" id="companyaccount" name="companyaccount" value="" placeholder="必须字母开头、数字或者英文符号"></div>
                  <div class="col-md-9 col-md-offset-3 error" id="adminname">企业账号不能为空</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">联系人<span class="red">*</span></label>
                  <div class="col-md-9">
                  <input type="text" id="linkman" class="form-control" name="tbQyExperienceApplicationPO.contactName" value="" placeholder="请输入联系人姓名"></div>
                  <div class="col-md-9 col-md-offset-3 error">联系人不能为空</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">常用邮箱<span class="red">*</span></label>
                  <div class="col-md-9">
                  <input type="text" id="companyemail" class="form-control" name="tbQyExperienceApplicationPO.email" value="" placeholder="请输入企业邮箱"></div>
                  <div class="col-md-9 col-md-offset-3 error">请正确填写邮箱地址</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">手机号码<span class="red">*</span></label>
                  <div class="col-md-9">
                  <input type="text" id="phone" class="form-control" name="tbQyExperienceApplicationPO.tel" value="" placeholder="请输入手机号码"></div>
                  <div class="col-md-9 col-md-offset-3 error">请正确填写手机号码</div>
                </div>   
                <div class="form-group">
                  <label class="col-md-3 control-label">登录密码<span class="red">*</span></label>
                  <div class="col-md-9"><input type="password" class="form-control" name="password" id="password" value="" placeholder="字母、数字或者英文符号，最短6位，区分大小写"></div>
                  <div class="col-md-9 col-md-offset-3 error" id="password1">请正确填写密码</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">确认密码<span class="red">*</span></label>
                  <div class="col-md-9"><input type="password" class="form-control" name="enterpassword" id="enterpassword" value="" placeholder="确认密码与登录密码必须一致"></div>
                  <div class="col-md-9 col-md-offset-3 error">确认密码与登录密码不一致</div>
                </div>
                <div class="form-group">
                  <input type="submit" name="submit" value="注册，下一步" class="submit col-md-9 col-md-offset-3" id="submita" onclick="submitform()">
                  <input type="submit" name="submit" value="注册，下一步" class="submit col-md-9 col-md-offset-3" id="submitb" style="display:none" >
                </div>
                  </form>        
         </div>
         </div>
         </div>
 
          <div class="clearfix"></div>
          <script type="text/javascript">

         var isPass="0";
         var password_num=0;
         function init(){
       	  var id="${param.id}";
       	  $.ajax({
       			url:"${baseURL}/portal/experienceapplication/expappAction!autoAbutmentTOPage.action",
       			type:"POST",
       			data:{id:id},
       			dataType:'json',
       			success:function (result) {
       				var po=result.data.po;
       				if(result.code=="0"){
	       				$("#name").html(po.name);
       				}else{
       					_alert("", "系统繁忙");
       				}
       			}
       		});
         }
         $(function(){//表单验证
          $('#companyaccount').blur(function(){
            if($('#companyaccount').val().length==0){
              $(this).parent('.col-md-9').siblings('.error').show();
              $('#adminname').html('企业账号不能为空')
              return false;}
            if($('#companyaccount').val().indexOf(' ')>-1){
                  $(this).parent('.col-md-9').siblings('.error').show();
                  $('#adminname').html('企业账号不能有空格');
                  return false;
                }
            if(!(/^[a-zA-Z]\w*@*\.*$/).test($('#companyaccount').val())){
              $(this).parent('.col-md-9').siblings('.error').show();
              $('#adminname').html('请正确填写企业账号')
              return false;
            }
              
              $(this).parent('.col-md-9').siblings('.error').hide();
            
            var name=$('#companyaccount').val();
            $.ajax({
        			url:"${baseURL}/portal/experienceapplication/expappAction!checkName.action",
        			type:"POST",
        			data:{name:name},
        			dataType:'json',
        			async:false,
        			success:function (result) {
        				if(result.code=="4"){
        					$("#adminname").html(result.desc);
        					$("#adminname").show();
        				}else{
        					isPass="1";
        				}
        			}
        		});
          })
          $('#linkman').blur(function(){
            if($('#linkman').val().length==0){
              $(this).parent('.col-md-9').siblings('.error').show();
              return false;}else{
              $(this).parent('.col-md-9').siblings('.error').hide();
            }
          })

          $('#companyemail').blur(function(){
            if(!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test($('#companyemail').val())){
              $(this).parent('.col-md-9').siblings('.error').show();
              return false;}else{
              $(this).parent('.col-md-9').siblings('.error').hide();
            }
          })
          $('#phone').blur(function(){
            if(!(/^[0-9-]+$/).test($('#phone').val())){
              $(this).parent('.col-md-9').siblings('.error').show();
              return false;}else{
              $(this).parent('.col-md-9').siblings('.error').hide();
            }
          })

          $('#password').blur(function(){
              var password=$('#password').val();
          
              if(password.length<6){
                  $("#password1").html('密码长度不能少于6位');
                  $("#password1").show();
                  return false;
              }
                  var count = 1;
                  var chars=password.split("");
                  for (var i = 1; i < password.length; i++) {
		              if (chars[i] == chars[i-1]) {
		                  count++;
			              if (count > 2){         
			                 $("#password1").html('密码中不能含有连续相同的3个字符');
			                 $("#password1").show();
			                 return false;
			               }
		              }else{
		                  count=1;
	              }}
              if(!(/(\d+.*[a-zA-z]+)|([a-zA-z]+.*\d+)/).test($('#password').val())){
                    for(var j=0;j<password.length;j++){
                  if((/^[A-Za-z]$/).test(chars[j])){
                        $("#password1").show();
                        $("#password1").html('密码不能是纯字母');
                        return false;
                  }else if((/^[0-9]$/).test(chars[j])){
                        $("#password1").show();
                        $("#password1").html('密码不能是纯数字');return false;}
                    }
                                  
              return false;
                   }else{ $("#password1").hide();
                   password_num++;
            }         
             });
          $('#enterpassword').blur(function(){
            if(($('#enterpassword').val()!=$('#password').val())){
              $(this).parent('.col-md-9').siblings('.error').show();
              return false;}else{
              $(this).parent('.col-md-9').siblings('.error').hide();
            }
          });
         
         });
         function submitform(){
        	 $("#submita").hide();
	       	 $("#submitb").show();
	       	if(isPass=="0"){
	       		_alert("", '用户已存在,请换一个账号');
	       		$("#submitb").hide();
	         	$("#submita").show();
	       	    return false;
	       	}
             if($('#companyaccount').val().length==0){
              $('#companyaccount').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             if($('#linkman').val().length==0){
              $('#linkman').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             if(!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test($('#companyemail').val())){
              $('#companyemail').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             if(!(/^[0-9-]+$/).test($('#phone').val())){
              $('#phone').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             if(password_num==0){
              $('#password').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             if(($('#enterpassword').val()!=$('#password').val())){
              $('#enterpassword').focus();
            	 $("#submitb").hide();
 	         	$("#submita").show();
              return false;
             }
             showLoading("正在接入中，请稍等...");
             $.ajax({
           		url:"${baseURL}/portal/experienceapplication/expappAction!autoAbutmentFormPage.action",
           		type:"POST",
           		data:$("#form").serialize(),
           		dataType:"json",
           		success:function(result){
           			if(result.code=="0"){
           				var URL=result.data.URL;
           				var Token=result.data.Token;
           				var EncodingAESKey=result.data.EncodingAESKey;
           				window.location.href="${baseURL}/qiweipublicity/experience2/yunstep3.jsp?u="+URL+"&t="+Token+"&e="+EncodingAESKey;
           			}else{
           				hideLoading();
           				_alert("", result.desc);
           			}
           			$("#submitb").hide();
           		 	$("#submita").show();
           		 	isPass=="1";
           		},
           		error:function(){
           			hideLoading();
           			_alert("", "系统繁忙");
           			$("#submitb").hide();
           		 	$("#submita").show();
           		 	isPass=="1";
           		}
         	}); 
         }
</script>
 <%@include file="./footer.jsp" %>  
<%@include file="../../manager/msgBoxs.jsp" %> 
  </body>
</html>