<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="">
    <meta name="baidu-site-verification" content="Vup9CGlqbA"/> <!-- 百度检测-->
    <meta name="msvalidate.01" content="6C48EF31F67FA6E09A742953ACAB5A39"/><!-- 必应搜索-->
    <title>企业微信接入企微云服务</title>
    <link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src="js/jquery-1.11.1.js"></script>
    <script src="js/common.js?ver=<%=jsVer%>"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?6abcc5eeee320072f7a9ed10e79be5c1";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
</head>
<style>
    .yun .form-group {
        margin-left: -50px;
        margin-bottom: 25px
    }

    .container {
        font-size: 16px
    }

    .subsucceed_l {
        border-right: 1px solid #ddd;
        width: 50%;
        float: left;
    }

    .subsucceed_r {
        float: left;
        width: 48%;
        padding-left: 50px;
    }

    a.subsucceedBtn {
        color: white;
        display: block;
        width: 280px;
        height: 47px;
        background-color: #ff9600;
        text-align: center;
        line-height: 48px;
        font-size: 18px;
        margin-top: 20px;
        border-radius: 5px;
    }

    a.subsucceedBtn:hover {
        background-color: #ffa500
    }

    .subsucceed {
        background-color: #fff;
        padding: 60px 0;
        width: 760px;
        margin: 0 auto;
        border-radius: 5px;
        border: 1px solid #ddd;
        margin-top: 60px;
    }

    .subsucceed_r p {
        padding-left: 24px
    }

    .subsucceed_r .c999 {
        color: #999;
    }

    .subsucceed_r .mt40 {
        margin-top: 30px
    }

    .subsucceed_l p {
        padding: 0;
        margin: 0;
    }

    .subsucceed_l img {
        width: 160px;
    }

    .subsucceed_l .c999 {
        color: #999;
    }

    .haveqyh {
        text-align: center;
        font-size: 14px;
        margin-top: 40px;
        padding-bottom: 20px
    }

    .haveqyh a:hover {
        color: #ffa500;
        text-decoration: underline;
    }

    div.error {
        font-size: 14px;
        margin-top: 0px;
        padding-left: 35px;
    }

    .form-control {
        height: 40px;
        vertical-align: middle;
    }

    h2 {
        margin: 5px;
        font-size: 28px;
    }

    input {
        border-radius: 5px
    }

    input[type="text"], [type="password"] {
        font-size: 16px;
    }

    input::-webkit-input-placeholder {
        color: #aaa !important;
    }

    input:-moz-placeholder {
        color: #aaa !important;
    / Mozilla Firefox 4 to 18 /
    }

    input::-moz-placeholder {
        color: #aaa !important;
    / Mozilla Firefox 19 + /
    }

    input:-ms-input-placeholder {
        color: #aaa !important;
    / Internet Explorer 10 + * /
    }

    .yun .form-horizontal .control-label {
        padding-top: 10px
    }

    .form {
        margin-top: 15px;
        border-radius: 5px;
        border: 1px solid #ddd;
    }

    .ce {
        color: #ff9600
    }

    .ce:hover {
        color: #ffa500;
        text-decoration: underline;
    }

    .yun .form .submit {
        width: 400px;
        height: 48px;
        margin: 10px 0 30px 20%;
        border-radius: 5px;
        font-size: 18px;
        background: #ff9600;
    }

    .yun .form .submit:hover {
        background: #ffa500
    }

    #usertops {
        color: #666
    }

    .textstep {
        margin-top: 15px;
        text-align: left;
    }

    .tlist {
        margin-left: -20px;
        text-align: left;
        margin-top: 20px
    }

    .tlist li {
        border-left: 1px solid #d9d9d9;
        padding-left: 15px;
        margin-top: 15px
    }

    .tlist li:before {
        content: '';
        border: 6px solid #fff;
        background-color: #ff9600;
        display: inline-block;
        width: 5px;
        height: 5px;
        border-radius: 17px;
        margin-left: -27px;
        margin-bottom: -5px;
        margin-right: 6px;
        padding: 5px;
    }
</style>
<body style="background:#eee">
<%@include file="./header.jsp" %>
<div class="container">
    <div class="content text-left yun w760" style="margin-top:45px">
        <div class="yun_top" id="topdiv">
            <h2 class="text-center mt10 c333">欢迎注册企微云平台账户</h2>
            <div id="usertops" class="tcenter fz18 c333">完成注册即可免费领取一个企业微信</div>
        </div>
        <div class="form" style="background:#fff" id="fromdiv">
            <form class="form-horizontal" onsubmit="return false;" method="post" accept-charset="utf-8" id="form">
                <input type="hidden" value="${param.id}" name="id"/>
                <div class="form-group mt50">
                    <label class="col-md-3 control-label">企业名称<span class="red">*</span></label>
                    <div class="col-md-9"><input type="text" class="form-control" id="companyname" name="companyname"
                                                 placeholder="请填写企业全称"></div>
                    <div class="col-md-9 col-md-offset-3 error" id="qiyename">企业名称不能为空；最少4个中文字符或者8个英文字符</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">管理员账号<span class="red">*</span></label>
                    <div class="col-md-9">
                        <input type="text" class="form-control" id="companyaccount" name="companyaccount" value=""
                               placeholder="推荐使用邮箱、手机号；用于登录管理平台"></div>
                    <div class="col-md-9 col-md-offset-3 error" id="adminname">管理员账号不能为空</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">登录密码<span class="red">*</span></label>
                    <div class="col-md-9"><input type="password" class="form-control" name="password" id="password"
                                                 value="" placeholder="字母、数字或者英文符号，最短6位，区分大小写"></div>
                    <div class="col-md-9 col-md-offset-3 error" id="password1">请正确填写密码</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">确认密码<span class="red">*</span></label>
                    <div class="col-md-9"><input type="password" class="form-control" name="enterpassword"
                                                 id="enterpassword" value="" placeholder="确认密码与登录密码必须一致"></div>
                    <div class="col-md-9 col-md-offset-3 error">确认密码与登录密码不一致</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">联系人<span class="red">*</span></label>
                    <div class="col-md-9">
                        <input type="text" id="linkman" class="form-control"
                               name="tbQyExperienceApplicationPO.contactName" value="" placeholder="请输入联系人姓名"></div>
                    <div class="col-md-9 col-md-offset-3 error">联系人不能为空</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">常用邮箱<span class="red">*</span></label>
                    <div class="col-md-9">
                        <input type="text" id="companyemail" class="form-control"
                               name="tbQyExperienceApplicationPO.email" value="" placeholder="建议填写企业邮箱；可用于找回密码"></div>
                    <div class="col-md-9 col-md-offset-3 error">请输入企业邮箱</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">手机号码<span class="red">*</span></label>
                    <div class="col-md-9">
                        <input type="text" id="phone" class="form-control" name="tbQyExperienceApplicationPO.tel"
                               value="" placeholder="请输入正确的手机号才能正常使用"></div>
                    <div class="col-md-9 col-md-offset-3 error">请正确填写手机号码</div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">验证码<span class="red">*</span></label>
                    <div class="col-md-9">
                        <input type="text" id="user_yanz" name="user_yanz" size="6" maxlength="6" class="form-control"
                               style="width: 266px;" placeholder="请输入验证码"/>
                        <input type="button" value="获取短信验证码" class="btn_yanz btn orangeBtn" style="height: 40px;" onclick="sendMsm(this);"/>
                    </div>
                    <div class="col-md-9 col-md-offset-3 error" id="user_yanz_tip">请输入验证码</div>
                </div>
                <%--<div class="form-group">
                    <label class="col-md-3 control-label">QQ</label>
                    <div class="col-md-9">
                        <input type="text" id="companyeqq" class="form-control" name="tbQyExperienceApplicationPO.qqNum"
                               value="" placeholder="请输入QQ"></div>
                    <div class="col-md-9 col-md-offset-3 error">QQ号码应为纯数字</div>
                </div>--%>
                <div class="form-group">
                    <input type="submit" name="submit" value="提交" class="submit col-md-9 col-md-offset-3" id="submita"
                           onclick="submitform()">
                    <input type="submit" name="submit" value="提交" class="submit col-md-9 col-md-offset-3" id="submitb"
                           style="display:none">
                </div>
            </form>

            <form id="id_form_login" action="${baseURL}/j_spring_security_check" method="post" autocomplete="off">
                <input type='hidden' name='_spring_security_remember_me' value="true"/>
                <input type='hidden' name='dqdp_csrf_token' value="${sessionScope.dqdp_csrf_token}"/>
                <input type='hidden' name="j_username" id="j_username">
                <input type='hidden' name="j_password" id="j_password">
            </form>
        </div>
        <div class="subsucceed" style="display:none" id="submitsucceed">
            <h3 class="tcenter">恭喜！你已开通企微云平台账户</h3>
            <div class="subsucceed_con ohidden mt50">
                <div class="subsucceed_l tcenter">

                    <p class="c333">扫描以下二维码关注企业微信</p>
                    <img id="logo_url" src="" >
                    <p class="fz14 c999">（后台导入员工通讯录后可邀请员工关注）</p>

                </div>
                <div class="subsucceed_r">
                    <p class="c333">使用以下账号登录管理平台</p>
                    <div class="fz14 mt40" style="line-height:26px;">
                        <div>登录账号：<span style='color: #ff9600' id="userId">账号</span></div>
                        <div>登录密码：<span style='color: #ff9600' id="upassword">密码</span></div>
                        <div class="c999">该账户信息已通过邮件发送至你的邮箱，为确保数据安全，请定期修改登录密码</div>
                        <a class="subsucceedBtn" id="btn_login" href="javascript:void(0)">马上登录</a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="clearfix"></div>
<script type="text/javascript">

    var isPass = "1";
    var password_num = 0;
    $(function () {
        //表单验证
        $('#companyname').blur(function () {
            var name = $('#companyname').val();
            if (name.length < 4) {
                $(this).parent('.col-md-9').siblings('.error').show();
                $(this).parent('.col-md-9').siblings('.error').html('企业名称不能为空；最少4个字符');
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
            if ($('#companyname').val().length > 20) {
                $(this).parent('.col-md-9').siblings('.error').show();
                $(this).parent('.col-md-9').siblings('.error').html('公司名字最多为20个字符长度');
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
        });
        $('#companyaccount').blur(function () {
            if ($('#companyaccount').val().length == 0) {
                $(this).parent('.col-md-9').siblings('.error').show();
                $('#adminname').html('管理员账号不能为空');
                return false;
            }
            if ($('#companyaccount').val().indexOf(' ') > -1) {
                $(this).parent('.col-md-9').siblings('.error').show();
                $('#adminname').html('管理员账号不能有空格');
                return false;
            }
            if (!(/^[0-9a-zA-Z]([a-zA-Z0-9_@\.]{4,30})+$/).test($('#companyaccount').val())) {
                $(this).parent('.col-md-9').siblings('.error').show();
                $('#adminname').html('管理员账号必须字母、数字或者英文符号组合');
                return false;
            }

            $(this).parent('.col-md-9').siblings('.error').hide();
            //验证账号
            var name = $('#companyaccount').val();
            $.ajax({
                url: "${baseURL}/portal/experienceapplication/expappAction!checkName.action",
                type: "POST",
                data: {name: name},
                dataType: 'json',
                async: false,
                success: function (result) {
                    if (result.code == "4") {
                        $("#adminname").html(result.desc);
                        $("#adminname").show();
                        isPass = "0";
                    } else {
                        isPass = "1";
                    }
                }
            });
        });
        $('#linkman').blur(function () {
            if ($('#linkman').val().length == 0) {
                $(this).parent('.col-md-9').siblings('.error').show();
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
        });

        $('#companyemail').blur(function () {
            if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test($('#companyemail').val())) {
                $(this).parent('.col-md-9').siblings('.error').show();
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
        });
        $('#phone').blur(function () {
            if (!(/^[0-9-]+$/).test($('#phone').val())) {
                $(this).parent('.col-md-9').siblings('.error').show();
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
        });
        $('#password').blur(function () {
            var password = $('#password').val();

            if (password.length < 6) {
                $("#password1").html('密码长度不能少于6位');
                $("#password1").show();
                return false;
            }
            var count = 1;
            var chars = password.split("");
            for (var i = 1; i < password.length; i++) {
                if (chars[i] == chars[i - 1]) {
                    count++;
                    if (count > 2) {
                        $("#password1").html('密码中不能含有连续相同的3个字符');
                        $("#password1").show();
                        return false;
                    }
                } else {
                    count = 1;
                }
            }
            if (!(/(\d+.*[a-zA-z]+)|([a-zA-z]+.*\d+)/).test($('#password').val())) {
                for (var j = 0; j < password.length; j++) {
                    if ((/^[A-Za-z]$/).test(chars[j])) {
                        $("#password1").show();
                        $("#password1").html('密码不能是纯字母');
                        return false;
                    } else if ((/^[0-9]$/).test(chars[j])) {
                        $("#password1").show();
                        $("#password1").html('密码不能是纯数字');
                        return false;
                    }
                }

                return false;
            } else {
                $("#password1").hide();
                password_num++;
            }
        });
        $('#enterpassword').blur(function () {
            if (($('#enterpassword').val() != $('#password').val())) {
                $(this).parent('.col-md-9').siblings('.error').show();
                return false;
            } else {
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
        });
        $('#id_form_login').jCryption({
            submitElement: $('#btn_login')
        });

    });
    /*$('#companyeqq').blur(function () {
        if (!(/^[0-9-]*$/).test($('#companyeqq').val())) {
            $(this).parent('.col-md-9').siblings('.error').show();
            return false;
        } else {
            $(this).parent('.col-md-9').siblings('.error').hide();
        }
    });*/
    $('#user_yanz').blur(function () {
        if ($('#user_yanz').val() == "") {
            $("#user_yanz_tip").show();
            return false;
        }
        else {
            $("#user_yanz_tip").hide();
        }
    });
    function connit() {
        document.getElementById("id_form_login").submit();
    }
    function submitform() {
        $("#submita").attr("disabled", "disabled");
        if (isPass == "0") {
            _alert("", '管理账号已存在,请换一个账号');
            /* $("#submitb").hide();
             $("#submita").show(); */
            $("#submita").removeAttr("disabled");
            return false;
        }
        if ($('#companyaccount').val().length == 0) {
            $('#companyaccount').parent('.col-md-9').siblings('.error').show();
            $('#adminname').html('管理员账号不能为空');
            $('#companyaccount').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if ($('#companyaccount').val().indexOf(' ') > -1) {
            $('#companyaccount').parent('.col-md-9').siblings('.error').show();
            $('#adminname').html('管理员账号不能有空格');
            $('#companyaccount').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if (!(/^[0-9a-zA-Z]([a-zA-Z0-9_@\.]{4,30})+$/).test($('#companyaccount').val())) {
            $('#companyaccount').parent('.col-md-9').siblings('.error').show();
            $('#adminname').html('管理员账号必须字母、数字或者英文符号组合');
            $('#companyaccount').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        /* if($('#companyaccount').val().length==0){
         $('#companyaccount').focus();
         $("#submita").removeAttr("disabled");
         return false;
         } */
        if ($('#linkman').val().length == 0) {
            $('#linkman').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test($('#companyemail').val())) {
            $('#companyemail').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if (!(/^[0-9-]+$/).test($('#phone').val())) {
            $('#phone').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if ($('#user_yanz').val() == "") {//验证是否输入验证码
            $('#user_yanz').focus();
            $("#user_yanz_tip").show();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if (password_num == 0) {
            $('#password').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        if (($('#enterpassword').val() != $('#password').val())) {
            $('#enterpassword').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }
        /*if (!(/^[0-9]*$/).test($("#companyeqq").val())) {
            $('#companyeqq').focus();
            $("#submita").removeAttr("disabled");
            return false;
        }*/
        showLoading("正在接入中，请稍等...");
        $.ajax({
            url: "${baseURL}/portal/experienceapplication/expappAction!applyCorp.action",
            type: "POST",
            data: $("#form").serialize(),
            dataType: "json",
            success: function (result) {
                if (result.code == "0") {
                    $("#userId").html($("#companyaccount").val());
                    $("#upassword").html($("#password").val());
                    $("#j_username").val($("#companyaccount").val());
                    $("#j_password").val($("#password").val());
                    $("#companyaccount").val("");
                    $("#password").val("");
                    $("#enterpassword").val("");
                    $("#linkman").val("");
                    $("#companyemail").val("");
                    $("#phone").val("");
                    if(result.data.wxqrcode){
                        $("#logo_url").attr("src",baseURL+"/portal/errcode/errcodeAction!loadImage.action?imgUrl="+encodeURIComponent(result.data.wxqrcode));
                    }

                    hideLoading();
                    $("#topdiv").hide();
                    $("#fromdiv").hide();
                    $("#submitsucceed").show();
                    //_alert("接入提醒", "接入成功，账号密码已发到您的邮箱，请注意保密！",function(){window.location.href="http://qy.do1.com.cn/qwy/manager/login.jsp";});
                    //_alert("接入提醒", "接入成功，账号密码已发到您的邮箱，请注意保密！",function(){document.getElementById("id_form_login").submit();});
                } else {
                    $("#submita").removeAttr("disabled");
                    _alert("", result.desc);
                }
                hideLoading();
                isPass = "1";
            },
            error: function () {
                hideLoading();
                _alert("", "系统繁忙");
                $("#submita").removeAttr("disabled");
                isPass = "1";
            }
        });
    }

    function sendMsm(obj){
        var user_phone = $("#phone").val();
        if (!user_phone) {
            _alert("", '请先输入手机号码');
            return false;
        } else if (!(/^1\d{10}$/).test(user_phone)) {
            _alert("", '请输入正确的手机号码');
            return false;
        }
        settime(obj);
        $.ajax({
            url:"${baseURL}/sms/sendsmsAction!sendSmsCode.action",
            type:"POST",
            data:{"smsVerCode.mobile":user_phone},
            dataType:"json",
            success:function(result){
                if(result.code == "0"){

                }else{
                    _alert("", result.desc);
                    countdown = 3;
                }
            },
            error:function(){
                _alert("", "系统繁忙！");
                countdown = 3;
            }
        });
    }
    var countdown = 60;
    function settime(val) {
        if (countdown == 0) {
            val.removeAttribute('class', 'btn_yaned');
            val.setAttribute('class', 'btn_yanz btn orangeBtn');
            val.removeAttribute('disabled');
            val.value = '获取短信验证码';
            countdown = 60;
        } else {
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
<%@include file="./footer.jsp" %>
<%@include file="../../manager/msgBoxs.jsp" %>
</body>
</html>