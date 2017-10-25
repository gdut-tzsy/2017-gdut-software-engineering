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
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/do1.css" rel="stylesheet">
    <link href="css/pwd.css" rel="stylesheet">
    <script src="js/jquery-1.11.1.js"></script>
    <script src="js/common.js?ver=<%=jsVer%>"></script>
</head>

<body>
<%@include file="./header.jsp" %>
    <div class="pwd_box">
        <h2>找回登录密码</h2>
        <ul class="step_line step_2 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <form method="post" class="pwd_form" id="from1">
            <span class="form_span">账号</span>
            <p>
                <select id="userName">
                </select>
            </p>
            <span class="form_span">输入新密码（6位以上数字英文组合，区分大小写）</span>
            <p>
                <input type="password" name="user_pwd" size="20" class="pwdInput pwd_w340" placeholder="6位以上数字英文组合，区分大小写" />
            </p>
            <span class="form_span">确认新密码</span>
            <p>
                <input type="password" name="user_twopwd" size="20" class="pwdInput pwd_w340" placeholder="请再输一遍新密码" />
            </p>
            <span class="pwd_msg pwd_msg_error"></span>
            <p>
                <input type="button" value="下一步" class="pwd_btn btn orangeBtn" id="step1submit" />
            </p>
        </form>
    </div>
<%@include file="./footer.jsp" %>  
    <script type="text/javascript">
    $(function() {
    	var code="${param.code}";
		$.ajax({
			url:"${baseURL}/portal/experienceapplication/expappAction!getUserByEmail.action",
		   	type:"POST",
		   	async:false,
		   	data:{code:code},
		   	dataType:"json",
		   	success:function(result){
			   	if(result.code=="0"){
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
			   	}else{
				   	$('.pwd_msg').show();
		           	$('.pwd_msg').html(result.desc);
			   	}
		   	},
		});
    
        $('#step1submit').click(function() {
	        var userName = $('#userName option:selected').val();
	        if(!userName){
                $('.pwd_msg').show();
                $('.pwd_msg').html('无效链接，请重试');
                $('input[name="user_pwd"]').addClass('errInput');
                return false;
	        }
        
            var user_pwd = $('input[name="user_pwd"]').val();
            var user_twopwd = $('input[name="user_twopwd"]').val();
            if (user_pwd.length < 6) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('请输入6位以上的密码');
                $('input[name="user_pwd"]').addClass('errInput');
                return false;
            } else if (!(/[A-Za-z0-9]/).test(user_pwd)) {
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
			
			//修改密码
        	$.ajax({
			   url:"${baseURL}/portal/experienceapplication/expappAction!RetrievePassWord2.action",
			   type:"POST",
			   async:false,
			   data:{"code" : code,
			   		 "userName" : userName,
			   		 "password" : user_pwd},
			   dataType:"json",
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
        });
    })
    </script>
</body>

</html>
