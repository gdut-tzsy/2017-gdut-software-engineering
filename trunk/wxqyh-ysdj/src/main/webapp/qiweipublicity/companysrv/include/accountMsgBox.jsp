<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 获取企业Corpid弹框 -->
<div class="Corpid-box" style="display:none;" id="accountBox" okCallback="">
	<form id="accountPassForm" action="" method="post" onsubmit="return false;" templateId="default" dqdpCheckPoint="add_form">
		<div class="SS_tit" style="text-align: center;">获取企业微信CorpID<i onclick="closeAccountBox()">×</i></div>
		<div class="item">
			<span class="dt">企微账号</span>
        <span class="dd ml10">
          <input type="text" placeholder="输入企微管理登录账号" id="userName" name="userName" />
        </span>
		</div>
		<div class="item">
			<span class="dt">密码</span>
        <span class="dd ml10" style="position:relative">
			<input type="password" id="password" name="password"/>
			<div class="check-tips none" id="accountTips" style="bottom:-38px;"><span>!</span>账号密码错误</div>
        </span>
		</div>
		<p class="mt30">也可登录企业微信或企微管理后台，手动复制CorpID</p>
		<input type="button" id="jcAccountPwdBtn" name="" value="获取" class="FW_orangeBtn">
	</form>
</div>

<script language="javascript" type="text/javascript">
	$(document).ready(function(){
		$('#accountPassForm').jCryption({
			submitElement: $('#jcAccountPwdBtn'),
			beforeEncryption: function () {
				return true;
			},
			finishEncryption: function ($formElement, encrypted) {
				getLoginCorpID($formElement);
			}
		});
	});

	function closeAccountBox(){
		$("#accountBox").hide();
		$("#overlayDiv").hide();
	}

	function getLoginCorpID($jcForm){
		var userName= $.trim($("#userName").val());
		var password=$.trim($("#password").val());
		if(""==userName || ""==password){
			$("#accountTips").show();
			return;
		}
		$jcForm.attr("action",baseURL+"/accountmgr/accountOpenAction!getOrgInfoByNameAndPass.action");
		$jcForm.ajaxSubmit({
			type:"POST",
			cache:false,
			dataType: "json",
			success: function(result){
				if ("0" == result.code) {
					$("#accountTips").hide();
					var okCallback=$("#accountBox").attr("okCallback");
					if(okCallback){
						eval(okCallback+"('"+result.data.corpId+"')");
					}
					closeAccountBox();
				}else{
					$("#accountTips").show();
				}
			},
			error:function(){
				_alert("","网络错误",1);
			}
		});
	}
</script>