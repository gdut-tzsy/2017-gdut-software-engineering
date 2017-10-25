<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="keywords" content="微信办公、业务沟通、消息联接、组织通讯录、会议室管理、移动CRM、客户拜访">
    <meta name="description" content="新时代微信办公，随时随地业务沟通、业务协作，外勤管理，客户拜访日志。”产品简介：十年专注企业信息化，企业微信办公云服务领军者；专注提升企业沟通、协作、管理效率，解决移动信息化建设成本高、安装难、使用不便等问题。">
    <title>开通</title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${baseURL}/manager/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link href="css/base.css" rel="stylesheet">
	<link href="css/bootstrap.css" rel="stylesheet">
    <!-- Add custom CSS here -->
    <link href="css/modern-business.css" rel="stylesheet">
    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="js/style.js"></script>
     <script src="js/jquery-1.10.2.js" type="text/javascript"></script> <!--jquery必须库-->
    <script src="js/formValidator-4.0.1.min.js" type="text/javascript"></script> <!--表单验证必须库-->
    <script src="js/formValidatorRegex.js" type="text/javascript"></script> <!--表单验证扩展库-->
    <link href="css/validator.css" rel="stylesheet" type="text/css" /><!--表单验证样式表-->
     <script type="text/javascript" src="js/jquery.alertWindow.js"></script>
</head>

<body style="background:#f3f5f5;">
<div class="container">
	<div class="row">
		<div class="">
			<div style="height:157px;background:url() no-repeat;">
				<img src="img/C1-03.png" />
			</div>
		     <form class="form-horizontal wpm" id="form">
				<fieldset>
				<!-- Text input-->
				<div class="form-group">
				  <label class="col-md-4 control-label"style="" for="textinput"><span class="ce">*</span>&nbsp;企业名称</label>  
				  <div class="col-md-5">
				  <input id="cnname" name="tbQyExperienceApplicationPO.enterpriseName" type="text" class="form-control input-md"style="margin-left:-20PX;">
				  </div>
				  <div class="col-md-3" id="cnnameTip"></div>
				</div>

				<!-- Select Basic -->
				<div class="form-group">
				  <label class="col-md-4 control-label" for="selectbasic"><span class="ce">*</span>&nbsp;企业规模</label>
				  <div class="col-md-5">
					<select id="select" name="tbQyExperienceApplicationPO.staffNumber" class="form-control"style="margin-left:-20PX;">
					  <option value="1">请选择企业员工规模</option>
					  <option value="2">0~10人</option>
					  <option value="3">11~50人</option>
					  <option value="4">51~100人</option>
					  <option value="5">101~200人</option>
					  <option value="6">201~500人</option>
					  <option value="7">500人以上</option>					  					  
					</select>
				  </div>
				</div>
				<div class="form-group">
				  <label class="col-md-4 control-label" for="selectbasic">&nbsp;所在省份</label>
				  <div class="col-md-5">
			                    <select id="selectpro" name="tbQyExperienceApplicationPO.province" class="form-control" style="margin-left:-20PX;">
			                    <option value="">请选择省份</option>
								<option value="北京">北京</option>
								<option value="广东">广东</option><option value="湖北">湖北</option><option value="湖南">湖南</option>
								<option value="广西">广西</option><option value="福建">福建</option><option value="安徽">安徽</option>
								<option value="云南">云南</option><option value="四川">四川</option><option value="贵州">贵州</option>
								<option value="澳门">澳门</option><option value="香港">香港</option><option value="台湾">台湾</option>
								<option value="江西">江西</option><option value="浙江">浙江</option><option value="江苏">江苏</option>
								<option value="河南">河南</option><option value="河北">河北</option><option value="山东">山东</option>
								<option value="山西">山西</option><option value="陕西">陕西</option><option value="甘肃">甘肃</option>
								<option value="青海">青海</option><option value="宁夏">宁夏</option><option value="辽宁">辽宁</option>
								<option value="新疆">新疆</option><option value="西藏">西藏</option><option value="海南">海南</option>
								<option value="吉林">吉林</option><option value="黑龙江">黑龙江</option><option value="内蒙古">内蒙古</option>
								<option value="天津">天津</option><option value="上海">上海</option>
								<option value="重庆">重庆</option>
								</select>
	                      </div>
                        </div>

				<!-- Text input-->
				<div class="form-group">
				  <label class="col-md-4 control-label" for="textinput"><span class="ce">*</span>&nbsp;联系人</label>  
				  <div class="col-md-5">
				  <input id="name" name="tbQyExperienceApplicationPO.contactName" type="text"class="form-control input-md"style="margin-left:-20PX;" />
				  </div>
				  <div class="col-md-3" id="nameTip"></div>
				</div>

				<!-- Text input-->
				<div class="form-group">
				  <label class="col-md-4 control-label" for="textinput"><span class="ce">*</span>&nbsp;联系手机</label>  
				  <div class="col-md-5">
				  <input id="sj" name="tbQyExperienceApplicationPO.tel" type="text" placeholder="真实填写手机号，以便审核资料过程中有疑问时联系" class="form-control input-md"style="margin-left:-20PX;" />
				  </div>
				  <div class="col-md-3" id="sjTip"></div>
				</div>
				<div class="form-group">
				  <label class="col-md-4 control-label" for="textinput"><span class="ce">*</span>&nbsp;E-Mail</label>  
				  <div class="col-md-5">
				  	<input id="email" name="tbQyExperienceApplicationPO.email" type="text" placeholder="填写常用邮箱，以便我们发送体验账号" class="form-control input-md"style="margin-left:-20PX;" />
				  </div>
				  <div class="col-md-3" id="emailTip"></div>
				</div>
                    <div class="form-group">
					  <label class="col-md-4 control-label" for="textinput">微&nbsp;&nbsp;信&nbsp;&nbsp;号</label>  
					  <div class="col-md-5">
					  	<input name="tbQyExperienceApplicationPO.weixin" type="text" placeholder="填写微信号，以便我们发送体验账号" class="form-control input-md"style="margin-left:-20PX;" />
					  </div>
					</div>
				<div class="form-group">
				   <label class="col-md-4 control-label" for="textinput"></label> 
				      <div class="col-md-5 checkbox">
				        <label>
				          <input type="checkbox" id="ckProtocal" />我已经详细阅读并同意《<a href="service.html" target="_black" class="ce">企微服务条款</a>》
				        </label>
				      </div>
				    </div>
				    <!-- Button -->
					<div class="form-group btn_submit">
					  <label class="col-md-12 control-label" for="singlebutton"></label>
					  <label class="col-md-12 control-label" for="singlebutton"></label>
					  <label class="col-md-4 control-label" for="singlebutton"></label>
					  <div class="col-md-6">
					    <a class="btn btn1" href="#" role="button" id="submita" onclick="addApplication();" style="display:block">提交申请</a>
					    <a class="btn btn1" href="#" role="button" id="submitb" style="display:none">提交申请</a>
					  </div>
					</div>
				 
				</fieldset>
				</form>
		</div>
	</div>
</div>
</body>
  <script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzkzODA1OTYyOV8xNzU2MjZfNDAwMTExMjYyNl8"></script>
   
    <script>
		$(function(){
			//alert(1);
			$(window).resize(function(){
				if($(window).height() < 800){
					$('.footer').css('position','relative');
				}else {
					$('.footer').css('position','fixed');
				}
			})
			
		});
		
		$(function(){
	if($(window).height( )< 650){
		$('.footer').css('position','relative');
	}else{
		$(window).resize(function(){
				if($(window).height() < 800){
					$('.footer').css('position','relative');
				}else {
					$('.footer').css('position','fixed');
				}
			})
	}
});

  $(document).ready(function () {
	   $.formValidator.initConfig({ formID: "form", onError: function () { alert("校验没有通过，具体错误请看错误提示") } });

	            $("#cnname").formValidator({ onShow: "", onFocus: "", onCorrect: "" }).inputValidator({ min: 1,  onError: "请输入企业名称" });
	            // $("#email").formValidator({ onShow: "", onfocus: "", onCorrect: "" }).regexValidator({ regExp: "email", dataType: "enum", onError: "请填写正确的邮箱地址" });
	             $("#name").formValidator({ onShow: "", onFocus: "", onCorrect: "" }).inputValidator({ min: 1, max: 16, onError: "请输入正确的联系人" });
	              //$("#sj").formValidator({ onShow: "", onfocus: "", onCorrect: "" }).regexValidator({ regExp: "mobile", dataType: "enum", onError: "请填写正确的手机号码" });
	        	
  });

	function addApplication() {
		if($("#cnname").val()==""){
			jQuery.alertWindow("提示","请输入企业名称！");
			return ;
		}
		if($("#select").find("option:selected").val()=='1'){
			jQuery.alertWindow("提示","请选择企业规模！");
			return ;
		}
		var email=$("#email").val();
		if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email) && email!=""){
			   jQuery.alertWindow("提示","请输入正确的邮箱");
			   return ;
		   }
		if($("#name").val()==""){
			jQuery.alertWindow("提示","请输入联系人！");
			return ;
		}
		var sj=$("#sj").val();
		if(sj!="" && !/^\d*$/.test(sj)){
			   jQuery.alertWindow("提示","请输入正确的手机号码");
			   return ;
		}
		if($("#sj").val()==""){
			jQuery.alertWindow("提示","请输入手机号码！");
			return ;
		}
		if($("#email").val()==""){
			jQuery.alertWindow("提示","请输入邮箱！");
			return ;
			//$("#email").val("未填写");
		}
		if(!$("#ckProtocal").prop("checked")){
			jQuery.alertWindow("提示","请认真阅读并同意《企微服务条款》！");
			return;
		}
		$.formValidator.reloadAutoTip(); //重新加载表单验证样式
		if(!$.formValidator.pageIsValid('1')){return false; }//如果不通过则不提交
		$("#submita").hide();
		$("#submitb").show();
    	$.ajax({
    		url:"${baseURL}/portal/experienceapplication/expappAction!ajaxAdd.action",
    		type:"POST",
    		data:$("#form").serialize(),
    		dataType:"json",
    		success:function(result){
    			if(result.code=="0"){
    				//alert("你已经成功提交申请，我们将会尽快审核并与你联系。");
    				jQuery.alertWindow("提示","感谢你的支持，你已经成功提交申请，我们会尽快开通账号发送到登记的邮箱，请留意查收。");
					//清空表单数据
    				$(':input','#form')
    				 .not(':button, :submit, :reset, :hidden')
    				 .val('')
    				 .removeAttr('checked')
    				 .removeAttr('selected');
    			}else if(result.code=="1888"){
    				jQuery.alertWindow("提示","提交过于频繁，请稍后再试！");
    			}else{
    				jQuery.alertWindow("提示","系统繁忙");
    			}
    			$("#email").val("");
    			$("#submita").show();
    			$("#submitb").hide();
    		},
    		error:function(){
    			$("#submita").show();
    			$("#submitb").hide();
    			hideLoading();
    			showMsg("",result.desc,1);
    		}
    	}); 
	}
	
    </script>
</html>