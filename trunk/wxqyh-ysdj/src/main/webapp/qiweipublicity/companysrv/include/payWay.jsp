<%@page language="java" contentType="text/html; charset=UTF-8"%>

<p class="fz18 c666 mb20">选择支付方式</p>
<div class="PaymentMethod clearfix relative">
	<div class="Pay-method clearfix on" id="payMethod_ali" payWay="ALI">
		<img src="../../../themes/manager/companysrv/images/img/ic-pm2.png"/>
		<div class="fl">
			<div class="tit">支付宝支付</div>
			<div class="introduce">推荐有支付宝用户使用</div>
		</div>
	</div>
	<div class="Pay-method clearfix" id="payMethod_wechat" payWay="WECHAT">
		<img src="../../../themes/manager/companysrv/images/img/ic-pm1.png"/>
		<div class="fl">
			<div class="tit">微信支付</div>
			<div class="introduce">扫一扫支付方便快捷</div>
		</div>
	</div>
	<div class="Pay-method clearfix" id="payMethod_union" payWay="UNIONBANK">
		<img src="../../../themes/manager/companysrv/images/img/ic-pm3.png" class="mt5" />
		<div class="fl">
			<div class="tit">银联支付</div>
			<div class="introduce">支持储蓄卡/信用卡</div>
		</div>
	</div>
	<div class="Pay-method clearfix" style="display:none;" id="payMethod_account" payWay="ACCOUNT">
		<div class="not-enough-tips" style="display:none;">余额不足</div>
		<div class="tit tl-c">企业账户支付</div>
		<div class="introduce tl-c">（余额<span id="amountAvalible">￥0.00</span>）</div>
		<input type="hidden" value="" id="amountAvalibleInput"/>
	</div>
	<div class="Pay-method clearfix" id="payMethod_wechatPhone" payWay="WECHATPHONE" style="display:none;">
		<img src="../../../themes/manager/companysrv/images/img/ic-pm1.png" class="mt5" />
		<div class="fl">
			<div class="tit">手机微信支付</div>
			<div class="introduce">微信直接支付方便快捷</div>
		</div>
	</div>
	<div class="check-tips" style="display:none;" id="payWayTips"><span>!</span>请选择支付方式</div>
</div>
<div class="bankType">
	<span>银行卡类型：</span>
	<input class="unionPayTypeRadioClass" name="" type="radio" checked="true" value="000201"/><span>个人银行</span>
	<%--<input type="radio" /><span>企业银行</span>--%>
	<input id="bizType" type="hidden" value=""/>
</div>
<input type="hidden" value="" id="payWay" name="payWay"/>
<input type="hidden" value="" id="payPwd" name="payPwd"/>

<script language="javascript" type="text/javascript">
	$(function(){
		$(".Pay-method").click(function(){
			if(!$(this).hasClass('not-enough')){
				$(".Pay-method").removeClass('on')
				$(this).addClass('on')
				if($(this).attr("id")=="payMethod_union"){
					$(".bankType").show()
				}else{
					$(".bankType").hide();
				}
			}
		});

		$("#bizType").val("000201");
		$(".bankClass").click(function(){
			if($(this).attr("id")=="personBank"){
				$("#bizType").val("000201");
			}else if($(this).attr("id")=="companyBank"){
				$("#bizType").val("000202");
			}
		});

/*		if(isWeChatApp()) {
			//如果是微信端打开，默认支付方式为WECHATPHONE-手机微信支付
			$(".Pay-method").hide();
			$(".Pay-method").removeClass("on");
			$("#payMethod_wechatPhone").show();
			$("#payMethod_wechatPhone").addClass("on");
		}else{
			//默认显示
			$(".Pay-method").hide();
			$(".Pay-method").removeClass("on");
			$("#payMethod_wechatPhone").show();
			$("#payMethod_wechatPhone").addClass("on");
		}*/
	});


	//加载账户余额
	var isLogin=false;
	function getAccountInfo(callback){
		$.ajax({
			type:"POST",
			url: baseURL+"/accountmgr/accountmgrAction!viewMyAccount.action",
			cache:false,
			dataType: "json",
			success: function(result){
				if ("0" == result.code) {
					//已登录
					isLogin=true;
					//如果账户信息存在，输出可用金额
					if(result.data.account){
						var account=result.data.account;
						var amountAvalible=account.amountAvalible;
						$("#amountAvalibleInput").val(amountAvalible);
						$("#amountAvalible").html("￥"+formatMoney(amountAvalible,2));
						if(callback){
							if("${param.serviceId}"!="cwmachine1" && "${param.serviceId}"!="cwmachine2" && "${param.serviceId}"!="cwwheel"){
								$("#payMethod_account").show();
							}
						}
					}else{
						$("#payMethod_account").hide();
					}
				}
				if(callback){
					callback.call(callback,null,null);
				}
			},
			error:function(){
				_alert("","网络错误",1);
			}
		});
	}

	//验证选择支付类型
	function checkPayWay(){
		var hasSelectMethod=false;
		$(".Pay-method").each(function (i) {
			if($(this).hasClass("on")){
				$("#payWay").val($(this).attr("payWay"));
				hasSelectMethod=true;
				return false;
			}
		});
		if(!hasSelectMethod){
			$("#payWay").val("");
			$("#payWayTips").show();
			return false;
		}
		$("#payWayTips").hide();
		return true;
	}
</script>