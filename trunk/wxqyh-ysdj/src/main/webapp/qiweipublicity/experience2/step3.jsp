﻿﻿﻿﻿<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
  <head>
    <meta charset="utf-8">
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
         <div class="content text-left tiyan" id="content_tiyyan">
         <p>企微，是我司作为微信第一批优秀第三方应用合作伙伴，基于企业微信开放平台开发的第三方应用产品，该产品采用云服务的模式为广大企业提供应用服务，永久免费，欢迎各企业开通使用。</p>
                <div class="stepdiv mt25">
           <div class="step first active"><i>1</i>填写基本资料</div><div class="steppic"><img src="images/jt-active.png"></div>
           <div class="step active"><i>2</i>验证邮箱</div><div class="steppic"><img src="images/jt-active.png"></div>
           <div class="step active"><i>3</i>设置体验号</div><div class="steppic"><img id="step4" src="images/jt-active.jpg"></div>
           <div class="step last" id="step4c"><i>4</i>邮箱通知使用</div>
          </div>
          <div class="submitsucceed text-center"id="submitsucceed" style="margin-top: 40px;">
				<table style="width: 640px; margin: 0 auto;text-align: left;" cellspacing="0" cellpadding="0">
					<tr>
						<td style=" font-size: 18px; color: #333;">恭喜你！提交申请成功，你已经成功开通企微云平台，欢迎加入企微大家庭！
						</td>
					</tr>
					<tr>
						<td style="padding-top: 18px; font-size: 14px; color: #666;">你的企微云平台管理员体验账号（仅供体验使用）如下：</td>
					</tr>
					<tr>
						<td style="font-size: 14px; color: #333; padding: 10px 0;">
							如果贵司已经申请了独立的企业微信，推荐直接将【企微云平台】接入自己的企业微信中（免费接入使用） 具体可参见
							<span style="color: #ff9600">
								<a href="http://wbg.do1.com.cn/More/handbook/handbook5/2014/1211/78.html" style="color: #ff9600; text-decoration: none;">【独立企业微信如何免费接入第三方应用】</a>
							</span>
						</td>
					</tr>
					<tr><td style="background: #f4f5f4; padding: 10px; margin-top: 10px; display: block;" >
						<table width="100%">
							<tr>
								<td style="font-size: 16px; width: 60%; line-height: 25px;">
								管理员登录账号：<span style='color: #ff9600' id="userId">账号</span><br />
								管理员登录密码：<span style='color: #ff9600' id="upassword">密码</span><br />
								<span style="color: #FF1D00; font-size: 10px;">为了确保数据安全，登录管理后台之后请尽快修改密码。</span></td>
								<td width="40%">" <a href="${qwyURL}"
									style="font-size: 14px; color: #fff; background: #ff9600; width: 205px; height: 48px; text-align: center; line-height: 48px; text-decoration: none; display: block">管理员登录入口</a></td>
							</tr>
						</table>
					</td>
					</tr>
					<tr>
						<td style="font-size: 12px; color: #999; padding: 10px 0;">备注：如果【管理员登录入口】无法正常打开，请复制右边链接到浏览器中打开：http://qy.do1.com.cn"。
						<br></br>系统处理可能需要5~10分钟，勿重复提交申请，稍后留意你的邮箱。如果长时间没有收到邮件消息，请直接发送邮件给我们：qinzhangbo@do1.com.cn
						</td>
					<tr>
						<td style="font-size: 14px; color: #666; padding: 30px 0px 10px;">【企微管理员后台操作指引】</td>
					</tr>
					<tr>
						<td><img src="http://wbg.do1.com.cn/templets/default/images/emailstep2.jpg" /></td>
					</tr>
					<tr>
						<td style="font-size: 12px; color: #999;">小提示：企微提供个人网页版，员工可以在电脑上使用企微云平台。</td>
					</tr>
					<tr>
						<td style="font-size: 12px; color: #999;">
						 更多操作指引请查看：<a href="http://wbg.do1.com.cn/More/handbook/handbook1" style="color: #ff9600;"></a>
						 http://wbg.do1.com.cn
						 </td>
					</tr>
					<tr>
						<td style="padding-top: 50px;">
						<p style="float: left; padding: 0px; margin: 0px;">
							<img src="http://wbg.do1.com.cn/templets/default/images/emailWX.jpg" />
						</p>
						<p style="padding: 0px 0px 0px 130px; margin: 0px; font-size: 14px; color: #666">
							<b>企微云平台二维码</b><br /><b>员工关注方法：</b><br /><b>方法1：</b>将左边二维码发送给员工，使用微信扫描二维码关注即可；<br /><b>方法2：</b>把企微云平台的微信名片在微信上转发给员工，员工点击后关注即可；<br />
							<span style="font-size: 12px">（如出现提示要求输入手机号验证，请输入注册用户对应的手机号码即可）</span>
						</p></td>
					</tr>
					<tr> <td style="padding-top: 50px; padding-bottom: 30px;">
						<table width="100%">
							<tr>
								<td style="background: #3d5866; color: #fff; font-size: 14px; width: 50%; padding: 20px; line-height: 30px;">企微QQ交流群<br />
								<span id="spanqq">群5：437937934&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;群4：57650844<br />群3：154835442&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;群2：220629533<br />群1：53863114</span>
								<br />
								<span style="font-size: 12px; color: #b0b8bd;">（工作日专人实时解答企微使用过程中的各种疑问）</span></td>
								<td style="background: #3d5866; color: #fff; font-size: 14px; width: 50%; padding: 20px 10px 20px 20px; line-height: 30px;">"
									<p style="float: left; width: 160px;">
										道一企微公众号<br />
										<span style="font-size: 12px; color: #b0b8bd;">扫描右边二维码关注公众号<br>（随时查看产品问答社区）</span>
									</p>
									<img src="http://wbg.do1.com.cn/templets/default/images/qr_home.png" width="110px" height="110px;" />
								</td>
							</tr>
						</table>
					</td>
					</tr>
				</table>
				<!-- <h2>提交申请成功</h2>

              <p>系统处理可能需要5~10分钟，勿重复提交申请，稍后留意你的邮箱。</p>

              <p>如果长时间没有收到邮件消息，请直接发送邮件给我们：</p>

              <p>chenhanyu@do1.com.cn</p> -->

         </div>
          <div id="form_con">
           <h2 class="text-center mt50">设置体验账号</h2>
          <div class="form">
         
         
            <form action="" method="post" accept-charset="utf-8" id="form" onsubmit="return false;">
            <input type="hidden" name="id" id="regix" value="${param.regix}"/>
            <div class="form-item mt35">管理员账号<span>*</span><span class='error' id="adminname">请填写字母、数字组合，作为管理员登录账号</span></div>
               <input type="text" name="adminaccount" value="" class="form-control" id="adminaccount" placeholder="支持邮箱/手机号码">
               <p>英文字母、数字组合，区分大小写,20以内</p>
               <div class="form-item">登录密码 <span>*</span><span class='error' id="password1">密码填写不正确</span></div>
               <input type="password" name="password" value="" class="form-control" id="password">
               <p>必须同时包含字母和数字，最短6位，区分大小写,字母开头</p>
               <div class="form-item">确认密码 <span>*</span><span class='error'>确认密码与登录密码不一致</span></div>
               <input type="password" name="Confirm" value="" class="form-control" id="Confirm">
               <p>必须同时包含字母和数字，最短6位，区分大小写,字母开头</p>
               
               <input type="submit" name="submit" value="完成" class="submit" id="submita" onclick="submitform()">
               <input type="submit" name="submit" value="完成" class="submit" id="submitb" style="display:none" >
            </form>
<script>
var isPass="0";
 var password_num=0;
$(function(){
	
  $('#adminaccount').blur(function(){
  	if($('#adminaccount').val().length>20){
  			$('#adminname').html('管理员账号长度不能超过20位');
  			$(this).prev().children('.error').show();
      	return false;
  		}
      //if(!(/^[0-9a-zA-Z]([a-zA-Z0-9_@\.]{4,15})+$/).test($('#adminaccount').val())){
    if(!(/^[0-9a-zA-Z]([a-zA-Z0-9_@\.]{4,15})+$/).test($('#adminaccount').val())){
     $(this).prev().children('.error').show();
      return false;
    }else{$(this).prev().children('.error').hide();
 
    var name=$('#adminaccount').val();
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
    }   pass();
     }
  )
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
          }
		  else{
			count=1;
		  }
		}
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
             $("#password1").hide();
			$(this).prev().children('.error').show();
			return false;
           }else{$(this).prev().children('.error').hide();
           password_num++;

		pass();
    }


     
     });
  $('#Confirm').keyup(function(){
      if($('#password').val()!=($('#Confirm').val())){
     $(this).prev().children('.error').show();

    }else{$(this).prev().children('.error').hide();
    
    }
      pass();
     });
     
 })
   function pass(){

        if(((/^\w{3,15}$/).test($('#companyname').val()))&&
          (password_num!=0)&&
          ($('#password').val()==$('#Confirm').val())
          ){
          $('#submita').css('background','#ffc412')         
      }else{$('#submita').css('background','#b3b3b3');return false}}
  function init(){
	  var id=$("#regix").val();
	  $.ajax({
			url:"${baseURL}/portal/experienceapplication/expappAction!getCount.action",
			type:"POST",
			data:{id:id},
			dataType:'json',
			success:function (result) {
				
			}
		});
  }
  function submitform(){
	  $("#submita").hide();
	  $("#submitb").show();
	
	if($('#adminaccount').val().length>20){
      	_alert("", '管理员账号长度不能超过20位');
     	$("#submitb").hide();
			$("#submita").show();
      return false;
  		}
    if(!(/^[0-9a-zA-Z]([a-zA-Z0-9_@\.]{4,15})+$/).test($('#adminaccount').val())){
     _alert("", '请填写正确的账号');
     $("#submitb").hide();
		$("#submita").show();
      return false;
    }
 
  if(!(/(\d+.*[a-zA-z]+)|([a-zA-z]+.*\d+)/).test($('#password').val())){
     _alert("", '请填写正确的密码');
     $("#submitb").hide();
		$("#submita").show();
      return false;
    }
  if($('#password').val()!=($('#Confirm').val())){
     _alert("", '确认密码与登录密码不一致');
     $("#submitb").hide();
	$("#submita").show();
      return false;
    }
  var password=$('#password').val();
  if(password.length<6){
	  $("#submitb").hide();
		$("#submita").show();
	  _alert("", '密码长度不能少于6位');
      return false;
  }
  if(isPass=="0"){
		_alert("", '用户已存在,请换一个账号');
		$("#submitb").hide();
  		$("#submita").show();
	    return false;
	}
  var count = 1;
  var chars=password.split("");
  for (var i = 1; i < password.length; i++) {
      if (chars[i] == chars[i-1]) {
        count++;
		if (count > 2){
			$("#submitb").hide();
			$("#submita").show();
			_alert("", '密码中不能含有连续相同的3个字符');
			return false;
		}
      }
	  else{
		count=1;
	  }
	}
   $.ajax({
		url:"${baseURL}/portal/experienceapplication/expappAction!autoRegis.action",
		type:"POST",
		data:$("#form").serialize(),
		dataType:'json',
		success:function (result) {
			if(result.code!="0"){
				_alert("", result.desc);
			}
			if(result.code=="0"){
				//window.location.href="${baseURL}/qiweipublicity/experience2/success2.jsp";
				//_alert("", "注册成功，相关资料已发送到注册邮箱中，请注意查收。");
				$("#userId").html(result.data.name);
				$("#upassword").html(result.data.password);
				$("#spanqq").html(result.data.qqNum);
				$("#step4").attr("src","images/jt-active.png");
				$("#step4c").removeClass("last").addClass("active");
				$("#form_con").hide();
				
				$('#content_tiyyan').removeClass(".content.text-left.tiyan");
				$('#content_tiyyan').addClass(".content.text-left.tiyan2");
				$('#submitsucceed').show();
				
				return false;
			}
			$("#submitb").hide();
	  		$("#submita").show();
		},
		error:function(){
			$("#submitb").hide();
	  		$("#submita").show();
			_alert("", "系统繁忙！");
		}
		}); 
		
		
  }
</script>
       
         </div></div>
         
         </div>
                  
         </div>

          <div class="clearfix"></div>
 <%@include file="./footer.jsp" %> 
<%@include file="../../manager/msgBoxs.jsp" %> 
  </body>
</html>