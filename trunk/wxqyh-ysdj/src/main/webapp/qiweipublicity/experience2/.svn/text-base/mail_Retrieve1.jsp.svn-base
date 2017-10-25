<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>找回密码</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    
	<link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="css/pwd.css" rel="stylesheet">
	<script src="js/jquery-1.11.1.js"></script>
	<script src="js/common.js?ver=<%=jsVer%>"></script>
	<script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
	<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
	<script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
</head>
<body>
<%@include file="./header.jsp" %>
	<!-- 通过邮箱找回密码 -->
	<div id="email_div" class="pwd_box">
        <h2 class="pageTitleClass"></h2>
        <ul class="step_line step_1 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <form method="post" class="pwd_form">
            <p class="mb15">
                <label style="margin-right: 70px;">
                    <input type="radio" checked="checked" name="emailType" onclick="changeType(1);" id="email_check1"><a href="javascript:void(0);" onclick="changeType(1);">使用邮箱找回</a></label>
                <label>
                    <input type="radio" name="emailType" onclick="changeType(2);" id="email_check2"><a href="javascript:void(0);" onclick="changeType(2);">使用手机号码找回</a></label>
            </p>
            <p>
                <input type="text" name="email" id="email" size="20" class="pwdInput pwd_w340" placeholder="请输入绑定的邮箱地址" />
            </p>
            <span class="pwd_msg pwd_msg_error"></span>
            <p>
                <input type="button" value="下一步" class="pwd_btn btn orangeBtn" id="step1submit" />
            </p>
        </form>
    </div>
    
    <!-- 通过手机号找回密码 -->
    <div id="phone_div" class="pwd_box" style="display:none;">
        <h2 class="pageTitleClass"></h2>
        <ul class="step_line step_1 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <form method="post" class="pwd_form">
            <p class="mb15">
                <label style="margin-right: 70px;">
                    <input type="radio" name="mobileType" id="mobile_check1" onclick="changeType(1);"><a href="javascript:void(0);" onclick="changeType(1);">使用邮箱找回</a></label>
                <label>
                    <input type="radio" name="mobileType" id="mobile_check2" onclick="changeType(2);"><a href="javascript:void(0);" onclick="changeType(2);">使用手机号码找回</a></label>
            </p>
            <p class="mb10">
                <input type="text" name="user_phone" size="20" class="pwdInput pwd_w340" placeholder="请输入绑定的手机号码" />
            </p>
            <p>
                <input type="text" name="user_yanz" size="6" maxlength="4" class="pwdInput pwd_w180" placeholder="请输入验证码" />
                <input type="button" value="获取短信验证码" class="btn_yanz btn orangeBtn" onclick="sendMsm(this);" />
            </p>
            <span class="pwd_msg pwd_msg_error"></span>
            <p>
                <input type="button" value="下一步" class="pwd_btn btn orangeBtn" id="step1submit2" />
            </p>
        </form>
    </div>
    
    <!-- 提示找回密码邮件发送成功 -->
    <div id="email_tip" class="pwd_box" style="display: none;">
        <h2 class="pageTitleClass"></h2>
        <ul class="step_line step_1 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <div class="pwd_mail">
            <img src="images/ic_mail.png">
            <p class="c333">一封确认邮件已发往<b> <a id="emailHref" href="javascript:void(0);"></a></b><br>请按邮件中的提示进行下一步操作</p>
            <span class="c999">（如收件箱里未找到邮件，可能被误判为垃圾邮件）</span>
            <a href="javascript:void(0);" onclick="javascript:location.reload();" class="pwd_btn btn two_btn mt30">返回</a>
        </div>
    </div>
    
    <!-- 第二步：短信找回登录密码验证成功 -->
    <div class="pwd_box" id="modifyPassword" style="display: none;">
        <h2 class="pageTitleClass"></h2>
        <ul class="step_line step_2 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <form method="post" class="pwd_form" id="smsEditPwdForm">
            <span class="form_span">账号</span>
            <p>
                <select id="userName">
                </select>
            </p>
            <span id="pwdTips" class="form_span"></span>
            <p>
                <input id="user_pwd" type="password" name="user_pwd" size="20" class="pwdInput pwd_w340" placeholder=""/>
            </p>
            <span class="form_span">确认新密码</span>
            <p>
                <input type="password" name="user_twopwd" size="20" class="pwdInput pwd_w340" placeholder="请再输一遍新密码" />
            </p>
            <span class="pwd_msg pwd_msg_error"></span>
            <p>
                <input type="button" value="下一步" class="pwd_btn btn orangeBtn" id="step2submit" onclick="checkSmsPwd()"/>
				<input type="button" value="下一步" class="pwd_btn btn orangeBtn" style="display:none;" id="smsEditPwdBtn" />
            </p>

			<input type="hidden" name="smsVerCode.mobile" id="smsMobile"/>
			<input type="hidden" name="smsVerCode.verCode" id="smsVerCode"/>
			<input type="hidden" name="userName" id="smsUserName"/>
			<input type="hidden" name="password" id="smsPassword"/>

        </form>
    </div>

<%@include file="./footer.jsp" %>
<script type="text/javascript">
	var resetType="LOGIN";
	$(function() {
		if("${param.resetType}"=="PAY"){
			resetType="PAY";
			$(".pageTitleClass").html("找回支付密码");
			$("#pwdTips").html('输入新密码（6位数字组合）');
			$("#user_pwd").attr("placeholder","6位数字组合");
		}else{
			$(".pageTitleClass").html("找回登录密码");
			$("#pwdTips").html('输入新密码（6位以上数字英文组合，区分大小写）');
			$("#user_pwd").attr("placeholder","6位以上数字英文组合，区分大小写");
		}

		$("#email_check1").prop("checked", true);
		//邮箱找回密码
	    $('#step1submit').click(function() {
	        var email = $('input[name="email"]').val();
	        if (email == "") {
	            $('.pwd_msg').show();
	            $('.pwd_msg').html('请输入邮箱地址');
	            $('input[name="email"]').addClass('errInput');
	            return false;
	        }
	        if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test(email)) {
	            $('.pwd_msg').show();
	            $('.pwd_msg').html('邮箱格式错误');
	            $('input[name="user_email"]').addClass('errInput');
	            return false;
	        }
			
	        //找回密码邮件
	        $.ajax({
			   url:"${baseURL}/portal/experienceapplication/expappAction!RetrievePassWord.action",
			   type:"POST",
			   async:false,
			   data:{
				   email:email,
				   "resetType":resetType
			   },
			   dataType:"json",
			   success:function(result){
				   if(result.code == "0"){
					   $('#emailHref').html(email);
					   //$('#emailHref').attr("href", "mailto: "+email);
					   $('#email_tip').show();
					   $("#email_div").hide();
					   $("#phone_div").hide();
				   }else{
		                $('.pwd_msg').show();
		                $('.pwd_msg').html(result.desc);
				   }
			   },
			   error:function(){
				   $('.pwd_msg').show();
		           $('.pwd_msg').html("系统繁忙！");
			   }
			});
	    });
	    
	    //手机找回密码
	    $('#step1submit2').click(function() {
            var user_phone = $('input[name="user_phone"]').val();
            var user_yanz = $('input[name="user_yanz"]').val();
            if (user_phone == 0) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('请输入手机号码');
                $('input[name="user_phone"]').addClass('errInput');
                return false;
            } else if (!(/^1\d{10}$/).test(user_phone)) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('请输入正确的手机号码');
                $('input[name="user_phone"]').addClass('errInput');
                return false;
            } else if (user_yanz == 0) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('请输入验证码');
                $('input[name="user_phone"]').removeClass('errInput');
                $('input[name="user_yanz"]').addClass('errInput');
                return false;
            }
            
            //验证短信验证码
        	$.ajax({
			   url:"${baseURL}/sms/sendsmsAction!validation.action",
			   type:"POST",
			   async:false,
			   data:{
				   "smsVerCode.mobile" : user_phone,
				   "smsVerCode.verCode" : user_yanz
			   },
			   dataType:"json",
			   success:function(result){
				   if(result.code == "0"){
				  		var userNames = result.data.userNames;
				   		if(userNames.length < 1){
				   			$('.pwd_msg').show();
		                	$('.pwd_msg').html("找不到要找回的账号，请重试");
		                	return;
				   		}else{
					  		for(var i in userNames){
						  		if('' != userNames[i]){
						  			$("#userName").append("<option value='"+userNames[i]+"'>"+userNames[i]+"</option>");
						  		}
					  		}
				   		}
						$("#modifyPassword").show();
						$("#email_tip").hide();
						$("#email_div").hide();
						$("#phone_div").hide();
		                $('.pwd_msg').hide();
				   }else{
		                $('.pwd_msg').show();
		                $('.pwd_msg').html(result.desc);
				   }
			   },
			   error:function(){
				   $('.pwd_msg').show();
		           $('.pwd_msg').html("系统繁忙！");
			   }
			});
        });

		$('#smsEditPwdForm').jCryption({
			submitElement: $('#smsEditPwdBtn'),
			beforeEncryption: function () {
				return true;
			},
			finishEncryption: function ($formElement, encrypted) {
				submitPwd($formElement);
			}
		});
	});

	function checkSmsPwd(){
		var user_phone = $('input[name="user_phone"]').val();
	 	var user_yanz = $('input[name="user_yanz"]').val();
	 	var user_pwd = $('input[name="user_pwd"]').val();
	 	var user_twopwd = $('input[name="user_twopwd"]').val();
		var userName = $('#userName option:selected').val();

/*		var user_phone = "15113020772";
		var user_yanz = "6310";
		var user_pwd = "test13";
		var user_twopwd = "test13";
		var userName="test13";*/

		if("${param.resetType}"=="PAY"){
			if (user_pwd.length !=6 || !(/[0-9]/).test(user_pwd) ) {
				$('.pwd_msg').show();
				$('.pwd_msg').html('请输入6位数字组合的密码');
				$('input[name="user_pwd"]').addClass('errInput');
				return false;
			} else if (user_pwd != user_twopwd) {
				$('.pwd_msg').show();
				$('.pwd_msg').html('密码不一致');
				$('input[name="user_pwd"]').removeClass('errInput');
				$('input[name="user_twopwd"]').addClass('errInput');
				return false;
			}
			$('.pwd_msg').hide();
		}else{
			if (user_pwd.length < 6) {
				$('.pwd_msg').show();
				$('.pwd_msg').html('请输入6位以上的密码');
				$('input[name="user_pwd"]').addClass('errInput');
				return false;
			} else if (!(/^[0-9a-zA-Z]*$/g).test(user_pwd)) {
				$('.pwd_msg').show();
				$('.pwd_msg').html('请输入6位以上数字英文组合的密码');
				$('input[name="user_pwd"]').addClass('errInput');
				return false;
			} else if (user_pwd != user_twopwd) {
				$('.pwd_msg').show();
				$('.pwd_msg').html('密码不一致');
				$('input[name="user_pwd"]').removeClass('errInput');
				$('input[name="user_twopwd"]').addClass('errInput');
				return false;
			}
			var chars = user_pwd.split("");
			if(!(/(\d+.*[a-zA-z]+)|([a-zA-z]+.*\d+)/).test(user_pwd)){
				for(var j=0;j<user_pwd.length;j++){
					if((/^[A-Za-z]$/).test(chars[j])){
						$('.pwd_msg').html('密码不能是纯字母');
						$('input[name="user_pwd"]').addClass('errInput');
						return false;
					}else if((/^[0-9]$/).test(chars[j])){
						$('.pwd_msg').html('密码不能是纯数字');
						$('input[name="user_pwd"]').addClass('errInput');
						return false;
					}
				}
				return false;
			}
			$('.pwd_msg').hide();
		}

		$("#smsMobile").val(user_phone);
		$("#smsVerCode").val(user_yanz);
		$("#smsPassword").val(user_pwd);
		$("#smsUserName").val(userName);

		$('#smsEditPwdBtn').click();
	}

	//短信验证后修改密码提交
	function submitPwd($formElement){
		if("${param.resetType}"=="PAY"){
			resetPayPwd($formElement);
		}else{
			resetLoginPwd($formElement);
		}
	}

	//找回登录密码
	function resetLoginPwd($jcForm){
		if($jcForm){
			//修改密码
			var userName=$("#smsUserName").val();
			$("#smsEditPwdForm").attr("action",baseURL+"/sms/sendsmsAction!resetPassword.action");
			var url=$("#smsEditPwdForm").attr("action");
			$jcForm.attr("action",url);
			$jcForm.ajaxSubmit({
				dataType:'json',
				async:false,
				success:function(result){
					if(result.code == "0"){
						//修改密码成功
						window.location.href = "${baseURL}/qiweipublicity/experience2/success_Retrieve3.jsp?userName=" + userName;
					}else{
						//修改密码失败
						window.location.href = "${baseURL}/qiweipublicity/experience2/fail_Retrieve3.jsp?userName=" + userName;
					}
				},
				error:function(){
					$('.pwd_msg').show();
					$('.pwd_msg').html("系统繁忙！");
				}
			});
		}
	}

	//找回支付密码
	function resetPayPwd($jcForm){
		if($jcForm){
			//修改密码
			var userName=$("#smsUserName").val();
			$("#smsEditPwdForm").attr("action",baseURL+"/accountmgr/accountOpenAction!RetrievePassWordBySmsCode.action");
			var url=$("#smsEditPwdForm").attr("action");
			$jcForm.attr("action",url);
			$jcForm.ajaxSubmit({
				dataType:'json',
				async:false,
				success:function(result){
					if(result.code == "0"){
						//修改密码成功
						window.location.href = "${baseURL}/qiweipublicity/companysrv/account/trade_pwd_reset_result.jsp?result=SUCCESS&userName=" + userName;
					}else{
						//修改密码失败
						window.location.href = "${baseURL}/qiweipublicity/companysrv/account/trade_pwd_reset_result.jsp?result=FAIL&userName=" + userName;
					}
				},
				error:function(){
					$('.pwd_msg').show();
					$('.pwd_msg').html("系统繁忙！");
				}
			});
		}
	}

	function changeType(type){
        $('.pwd_msg').html('');
		if("1" == type){
			$("#email_check1").prop("checked", true);
			$("#email_check2").removeAttr("checked");
			$("#email_div").show();
			$("#phone_div").hide();
		}else if("2" == type){
			$("#mobile_check1").removeAttr("checked");
			$("#mobile_check2").prop("checked", true);
			$("#email_div").hide();
			$("#phone_div").show();
		}
	}

	//发送短信
	function sendMsm(obj){
        var user_phone = $('input[name="user_phone"]').val();
		$('.pwd_msg').html('');
		if (user_phone == 0) {
            $('.pwd_msg').show();
            $('.pwd_msg').html('请输入手机号码');
            $('input[name="user_phone"]').addClass('errInput');
            return false;
        } else if (!(/^1\d{10}$/).test(user_phone)) {
            $('.pwd_msg').show();
            $('.pwd_msg').html('请输入正确的手机号码');
            $('input[name="user_phone"]').addClass('errInput');
            return false;
        }
		settime(obj);
		$.ajax({
		   	url:"${baseURL}/sms/sendsmsAction!findPassword.action",
		   	type:"POST",
		   	data:{"smsVerCode.mobile":user_phone},
		   	dataType:"json",
		   	success:function(result){
				if(result.code == "0"){
					
				}else{
		           	$('.pwd_msg').show();
		        	$('.pwd_msg').html(result.desc);
		        	countdown = 3;
				}
			},
		   	error:function(){
			   $('.pwd_msg').show();
	           $('.pwd_msg').html("系统繁忙！");
	           countdown = 3;
		   	}
		});
	}

    var countdown = 60;
    function settime(val) {
        if (countdown == 0) {
            $('input[name="user_phone"]').removeClass('errInput');
            //$('.pwd_msg').show();
            //$('.pwd_msg').html('');
            val.removeAttribute('class', 'btn_yaned');
            val.setAttribute('class', 'btn_yanz btn orangeBtn');
            val.removeAttribute('disabled');
            val.value = '获取短信验证码';
            countdown = 60;
        } else {
            $('input[name="user_phone"]').removeClass('errInput');
            //$('.pwd_msg').show();
            //$('.pwd_msg').html('');
            val.setAttribute('class', 'btn_yaned');
            val.setAttribute('disabled', true);
            val.value = countdown + 's 后重新获取';
            countdown--;
            setTimeout(function() {
                settime(val)
            }, 1000)
        }
    }
    
</script>

 <%@include file="../../manager/msgBoxs.jsp" %> 
</body>
</html>
