<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>登录</title>
<meta charset="UTF-8">
<link rel="stylesheet" href="../css/login.css" />
<script src='../js/jquery-1.10.2.min.js'></script>
<script src='../js/jquery.form.js'></script>
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
					<img src="logo.png" class="logo"/>
					<span class="logo_text">企微云平台</span>
				</div>
			</div>

			<div id="login_box">
				<ul class="login_tab" id="login_tab">
					<li class="curr">管理平台</li>
					<li>个人网页</li>
				</ul>
				<div id="tab_content">
					<!--管理平台-->
					<!--
						1、form表单不能更改
						2、表单中账号id和name属性必须为 name="j_username" id="j_username"
						3、表单中密码id和name属性必须为 name="j_password" id="j_password"
						4、登录按钮提交到自己的后台根据接口验证账号密码是否正确
					-->
					<form id="id_form_login_manager" action="" method="post"
						autocomplete="off">
						<ul class="login_content">
							<li>
								<input type="text" placeholder="请输入账号" class="accout" name="j_username" id="j_username" />
							</li>
							<li>
								<input type="password" placeholder="请输入密码"class="password" name="j_password" id="j_password" />
								<a href="${baseURL}/qiweipublicity/experience2/Retrieve1.jsp" class="forget_pwd">忘记密码？</a>
							</li>
							<li>
								<input type="button" onclick="checkManagerUser()" value="登录" class="submit_btn">
							</li>
						</ul>
					</form>
					<!--个人网页-->
					<!--
						1、form表单不能更改
						2、表单中账号id和name属性必须为 name="j_username" id="j_username_pc"
						3、表单中密码id和name属性必须为 name="j_password" id="j_password_pc"
						4、登录按钮提交到自己的后台根据接口验证账号密码是否正确
					-->
					<form id="id_form_login_pc" action="" method="post"
						autocomplete="off">
						<ul class="login_content" style="display:none">
							<li>
								<input type="text" placeholder="请输入账号" class="accout" name="j_username" id="j_username_pc" />
							</li>
							<li>
								<input type="password" placeholder="请输入密码" class="password" name="j_password" id="j_password_pc" />
								<!--  <a href="#" class="forget_pwd">忘记密码？</a> -->
							</li>
							<li>
								<input type="button" onclick="checkPcUser()" value="登录" class="submit_btn">
							</li>
						</ul>
					</form>
				</div>
			</div>
			</form>
			<!-- <div id="footer">
				<p class="copyright">Copyright©2014-2015 DO1.com.cn All Rights
					Reserved 粤B2-2006201</p>
			</div> -->
		</div>
	</div>
	<script type="text/javascript">
    	//处理登录块
		var baseURL="http://localhost:8080/wxqyh";//本地服务器：
		var qwyURL=qwyURL;//后台：配置转发的请用转发的地址,必须是域名+"/qwy"
		var webURL=qwwebURL;//个人网页版：配置转发的请用转发的地址,必须是域名+"/web"
    	//前台登录
    	function checkPcUser(){
    		var j_username=$("#j_username_pc").val();
    		var j_password=$("#j_password_pc").val();
    	 	$.ajax({
    			url : baseURL+"/portal/cooperationOpen/cooperationOpenAction!checkWebUser.action",
    			data : {
    				'uid':'eeeb898d-a683-4ad1-9685-d92fca25c0d4',//这里的uid是我们分配你们的唯一uid
    				'j_username':j_username,
    				'j_password':j_password,
    				'tips':'person',
					'testType':'1'//接口调试参数，和部署无关
    			},
    			type : "POST",
    			async : false,
    			dataType : "json",
    			success : function(result) {
					if(result.data.resultCode=="100"){
    	 				var personLoginUrl=webURL+"/portal/userLoginAction!weixinWebLogin.action";//这是个人版网页的登录地址
    					$("#id_form_login_pc").attr("action", personLoginUrl);
    					$("#id_form_login_pc").submit();
    				}else{
						alert(result.data.resultMsg);
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
    			url : baseURL+"/portal/cooperationOpen/cooperationOpenAction!checkManagerUser.action",//登录检验地址
    			data : {
    				'uid':'eeeb898d-a683-4ad1-9685-d92fca25c0d4',//这里的uid是我们分配你们的唯一uid
    				'j_username':j_username,
    				'j_password':j_password,
    				'tips':'user',
					'testType':'1'//接口调试参数，和部署无关
    			},
    			type : "POST",
    			async : false,
    			dataType : "json",
    			success : function(result) {
					if(result.data.resultCode=="100"){
    					//验证通过后，必须以    $("#id_form_login").submit()   这样的表单方式提交到对应企微的地址中
    					//表单提交后会自动跳转到相应的登录成功页面
    					var userUrl=qwyURL+"/j_spring_security_check";
    					$("#id_form_login_manager").attr("action", userUrl);
    					$("#id_form_login_manager").submit();
    				}else{
						alert(result.data.resultMsg);
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
        function centerBox(elm){
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
        } 

        

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