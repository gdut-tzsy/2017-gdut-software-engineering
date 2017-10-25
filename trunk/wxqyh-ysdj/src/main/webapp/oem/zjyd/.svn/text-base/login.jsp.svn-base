<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/jsp/common/dqdp_common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>登录</title>
<meta charset="UTF-8">
<link rel="stylesheet" href="${baseURL}/oem/css/login.css?ver=<%=jsVer%>" />
<script src='${baseURL}/oem/js/jquery-1.10.2.min.js'></script>
<script src='${baseURL}/oem/js/jquery.form.js'></script>
<style type="text/css">
input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0px 1000px white inset;
}
body {
    background-image: url(bg_zjyd.jpg);
    background-position: center center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-size: cover;
}
#wrap {
    background: none;
}
.logo {
    width: 184px;
    height: 34px;
}
.nav {
    position: absolute;
    top: 30px;
    right: 100px;
    line-height: 34px;
}
.nav a {
    margin-left: 35px;
    width: 65px;
    text-align: center;
    display: inline-block;
}
.nav .more {
    background: url(bg_more.png) no-repeat right 15px;
}
#login_box {
    padding-top: 5px;
    border-radius: 2px;
    border: 1px #ccc solid;
    background-color: #eff2f4;
}
.login_tab {
    border-bottom: 1px #ccc solid
}
.login_tab li.curr {
    color: #333;
    border-bottom-color: #0085d0;
}
.accout, .password {
    float: left;
    width: 240px;
    height: 38px;
    line-height: 40px\9;
    padding-left: 0;
    font-size: 14px;
}
.login-item{
    width: 278px;
    height: 38px;
    border: 1px #dbdbdb solid;
    border-radius: 2px;
    background-color: #fff;
}
.accout {
    background: none;
}
.password {
    background: none;
}
.bg_accout{
    float: left;
    width: 38px;
    height: 38px;
    background: url(../images/user.png) no-repeat 10px;
}
.bg_password{
    float: left;
    width: 38px;
    height: 38px;
    background: url(../images/pwd.png) no-repeat 10px;
}
.submit_btn{
    background-color: #0085d0;
}
#footer{
    position: absolute;
    bottom:20px;
    width: 100%;
}
</style>
</head>
<body>
	<div id="wrap">
		<div id="container">
			<div id="top">
				<!-- <div class="top_right">
					<a href="#">公司官网</a> <span class="more" id="more"> <a
						href="javascript:;">更多 ▼</a>
						<ul class="cat_tree" id="cat_tree">
							<li><a href="#">产品介绍</a></li>
							<li><a href="#">关于我们</a></li>
						</ul>
					</span>
				</div> -->
				<div>
					<img src="logo2.png" class="logo"/>
					<span class="logo_text">浙江移动教育云平台</span>
				</div>
				<div class="nav" style="display: none;">
                    <a href="javascript:void(0);">公司官网</a>
                    <a href="javascript:void(0);" class="more">更多</a>
                </div>
			</div>

			<div id="login_box">
				<ul class="login_tab" id="login_tab">
					<li class="curr">管理平台</li>
					<li>个人网页</li>
				</ul>
				<div id="tab_content">
					<!--管理平台-->
					<form id="id_form_login_manager" action="" method="post"
						autocomplete="off">
						<ul class="login_content">
							<li><div class="login-item"><b class="bg_accout"></b><input type="text" placeholder="请输入账号" class="accout" name="j_username" id="j_username" />
                            </div></li>
							<li><div class="login-item"><b class="bg_password"></b><input type="password" placeholder="请输入密码"
								class="password" name="j_password" id="j_password" /></div> <a
									href="${baseURL}/qiweipublicity/experience2/Retrieve1.jsp" class="forget_pwd">忘记密码？</a></li>
							<li><input type="button" onclick="checkManagerUser()"
								value="登录" class="submit_btn"></li>
						</ul>
					</form>
					<!--个人网页-->
					<form id="id_form_login_pc" action="" method="post"
						autocomplete="off">
						<ul class="login_content" style="display:none">
							<li><div class="login-item"><b class="bg_accout"></b><input type="text" placeholder="请输入账号" class="accout"
								name="j_username" id="j_username_pc" /></b></li>
							<li><div class="login-item"><b class="bg_password"></b><input type="password" placeholder="请输入密码"
								class="password" name="j_password" id="j_password_pc" /></div><!--  <a
								href="#" class="forget_pwd">忘记密码？</a> --></li>
							<li><input type="button" onclick="checkPcUser()" value="登录"
								class="submit_btn"></li>
						</ul>
					</form>
				</div>
			</div>
			</form>
		</div>
	</div>
    <div id="footer">
        <p class="copyright">Copyright©2014-2015 DO1.com.cn All Rights Reserved 粤B2-20062018号</p>
    </div>
	<script type="text/javascript">
    	//处理登录块
    	//前台登录
    	function checkPcUser(){
    		var j_username=$("#j_username_pc").val();
    		var j_password=$("#j_password_pc").val();
    	 	$.ajax({
    			url : "${baseURL}/portal/cooperationOpen/cooperationOpenAction!checkUserByTips.action",
    			data : {
    				'uid':'eeeb898d-a683-4ad1-9685-d92fca25c0d4',//这里的uid是我们分配你们的唯一uid
    				'j_username':j_username,
    				'j_password':j_password,
    				'tips':'person'
    			},
    			type : "POST",
    			async : false,
    			dataType : "json",
    			success : function(result) {
    				if(result.code=="100"){

    	 				var personLoginUrl="${baseURL}/portal/userLoginAction!weixinWebLogin.action";//这是个人版网页的登录地址
    					$("#id_form_login_pc").attr("action", personLoginUrl);
    					$("#id_form_login_pc").submit();
    				}else{
    					alert(result.desc);
    				}

    			},
    			error : function() {
    				alert("系统繁忙");
    			}
    		});
    	}
    	//后台登录
    	function checkManagerUser(){
    		var j_username=$("#j_username").val();
    		var j_password=$("#j_password").val();
    	 	$.ajax({
    			url : "${baseURL}/portal/cooperationOpen/cooperationOpenAction!checkUserByTips.action",//登录检验地址
    			data : {
    				'uid':'eeeb898d-a683-4ad1-9685-d92fca25c0d4',//这里的uid是我们分配你们的唯一uid
    				'j_username':j_username,
    				'j_password':j_password,
    				'tips':'user'

    			},
    			type : "POST",
    			async : false,
    			dataType : "json",
    			success : function(result) {
    				if(result.code=="100"){
    					//验证通过后，必须以    $("#id_form_login").submit()   这样的表单方式提交到对应企微的地址中
    					//表单提交后会自动跳转到相应的登录成功页面
    					var userUrl="${baseURL}/j_spring_security_check";
    					$("#id_form_login_manager").attr("action", userUrl);
    					$("#id_form_login_manager").submit();
    				}else{
    					alert(result.desc);
    				}
    			},
    			error : function() {
    				alert("系统繁忙");
    			}
    		});
    	}
    </script>
	<script type="text/javascript">
        function $$(id){
            return document.getElementById(id)
        }
        //垂直居中
        /* function centerBox(elm){
            if(winH <= boxH){
                return
            }else{
                var winH = document.body.clientHeight,
                    boxH = $$(elm).clientHeight;
                $$(elm).style.top =  '50%';
                $$(elm).style.marginTop =  -boxH/2+'px';
            }
        }
        window.onload = function(){
            centerBox('wrap');
        }
        window.onresize = function(){
            centerBox('wrap');
            console.log('resize');
        } */



        //tab切换
        var login_tab = $$('login_tab'),
            li = login_tab.getElementsByTagName('li'),
            tab_content = $$('tab_content'),
            login_content = tab_content.getElementsByTagName('ul');
        for (var i = 0; i < li.length; i++) {
            li[i].index = i;
            li[i].onclick = function(){
                for (var j = 0; j < login_content.length; j++) {
                    li[j].className = '';
                    login_content[j].style.display = 'none'
                };
                li[this.index].className = 'curr';
                login_content[this.index].style.display = 'block'
            }
        };
    </script>
</body>
</html>
