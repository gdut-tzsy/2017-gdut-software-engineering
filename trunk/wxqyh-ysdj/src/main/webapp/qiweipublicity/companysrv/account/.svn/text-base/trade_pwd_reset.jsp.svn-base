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
    <link href="../../experience2/css/bootstrap.css" rel="stylesheet">
    <link href="../../experience2/css/do1.css" rel="stylesheet">
    <link href="../../experience2/css/pwd.css" rel="stylesheet">
    <script src="../../experience2/js/jquery-1.11.1.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="../../experience2/js/common.js?ver=<%=jsVer%>"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
</head>

<body>
<%@include file="../../experience2/header.jsp" %>
<div class="pwd_box">
    <h2>找回支付密码</h2>
    <ul class="step_line step_2 clearfix">
        <li>验证方式</li>
        <li style="margin: 0 120px;">重置密码</li>
        <li>完成</li>
    </ul>
    <form method="post" class="pwd_form" id="pwdForm">
        <input type="hidden" id="code" name="code"/>
        <span class="form_span">账号</span>
        <p>
            <select id="userName" name="userName">
            </select>
        </p>
        <span class="form_span">输入新密码（6位数字组合）</span>
        <p>
            <input type="password" id="password" name="password"  size="20" class="pwdInput pwd_w340" placeholder="6位数字组合" />
        </p>
        <span class="form_span">确认新密码</span>
        <p>
            <input type="password" id="password2" size="20" class="pwdInput pwd_w340" placeholder="请再输一遍新密码" />
        </p>
        <span class="pwd_msg pwd_msg_error"></span>
        <p>
            <input type="button" value="下一步" class="pwd_btn btn orangeBtn" id="submitBtn" />
        </p>
    </form>
</div>
<%@include file="../../experience2/footer.jsp" %>
<script type="text/javascript">
    $(function() {
        initLoginAccount();

        $('#pwdForm').jCryption({
            submitElement: $('#submitBtn'),
            beforeEncryption: function () {
                return true;
            },
            finishEncryption: function ($formElement, encrypted) {
                submitPwd($formElement);
            }
        });
    });

    //初始化账号
    function initLoginAccount(){
        var code="${param.code}";
        $("#code").val(code);
        $.ajax({
            url:"${baseURL}/portal/experienceapplication/expappAction!getUserByEmail.action",
            type:"POST",
            async:false,
            data:{
                code:code,
                "resetType":"PAY"
            },
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
    }

    //提交
    function submitPwd($jcForm){
        if($jcForm){
            var userName = $('#userName option:selected').val();
            if(!userName){
                $('.pwd_msg').show();
                $('.pwd_msg').html('无效链接，请重试');
                $('#password').addClass('errInput');
                return false;
            }

            var user_pwd = $('#password').val();
            var user_twopwd = $('#password2').val();
            if (user_pwd.length != 6 || !(/[0-9]/).test(user_pwd)) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('请输入6位数字的密码');
                $('#password').addClass('errInput');
                return false;
            } else if (user_pwd != user_twopwd) {
                $('.pwd_msg').show();
                $('.pwd_msg').html('密码不一致');
                $('#password2').removeClass('errInput');
                $('#password2').addClass('errInput');
                return false;
            }
            $('.pwd_msg').hide();


            $("#pwdForm").attr("action",baseURL+"/accountmgr/accountOpenAction!RetrievePassWordByEmailCode.action");
            var url=$("#pwdForm").attr("action");
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
</script>
</body>

</html>
