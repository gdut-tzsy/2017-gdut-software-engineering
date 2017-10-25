<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
    <meta charset="utf-8">
    <title>微信支付页面</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <style type="text/css">
    * { padding: 0; margin: 0}
    .zhifu { overflow: hidden}
    .zhifu_t { text-align: center; font-size: 22px; color: #fff; height: 100%; background-color: #32CD32; position: relative; }
    .zhifu_t span { margin-top: 70px; display: inline-block; }
    .center { position: absolute; left: 50%; margin-left: -120px; height: 100px; top: 50%; margin-top: -50px; background: url(${baseURL}/jsp/wap/images/zhifu.png) no-repeat center top; background-size: 60px 56px; }
    </style>
</head>

<body>
    <div class="zhifu">
        <div class="zhifu_t">
            <div class="center">
                <span>正在启动微信安全支付...</span>
            </div>
        </div>
    </div>
    <%@include file="/jsp/wap/include/showMsg.jsp"%>
</body>
</html>


<script type="text/javascript">
	var isPay=true;
	$(function(){
		$('.zhifu').height(document.documentElement.clientHeight);
		if(!isWeChatApp()){
			showMsg("", "请用手机微信打开支付", 1,
				{
					ok:function(result){
						WeixinJS.back();
					}
				}
			);
		} 
		//$('.zhifu').height($(window).height()); 
		/*$("#btn_pay").click(function(){
			if(isPay){
				isPay=false;
				doMyPay();
			}
		});*/
		wx.ready(function(){
			if(isPay){
				isPay=false;
				doMyPay();
			}
		});
	}); 
	var orderNo="";
	var type="payResultNotify";
	function doMyPay(){
		var productDesc=decodeURIComponent(getParam("productDesc"));
		doWechatPay("${param.payCost}",productDesc,"${param.agentCode}","${param.groupId}");
	}
	//调用支付接口
	function doWechatPay(payCost,productDesc,agentCode,groupId){
		//showLoading();
		$.ajax({
	        type:"POST",
	        url: baseURL+"/portal/payport/payportAction!ajaxAddOrder.action",
	        data:{
	        	'payCost':payCost,
	        	'productDesc':productDesc,
	        	'agentCode':agentCode,
	        	'groupId':groupId
	        },
	        async: false,
	        cache:false,
	        dataType: "json",
	        success: function(result){
				//hideLoading();
	        	if(result.code=="0"){
	        		//返回成功，调用微信支付
	        		//alert("支付订单增加:"+JSON.stringify(result.data.orderInfo));
	        		orderNo=result.data.orderNo;
	        		param = result.data.orderInfo;
	        		wx.chooseWXPay({
	    	    		appId:param.appId,
	    	    	    timestamp: param.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	    	    	    nonceStr: param.nonceStr, // 支付签名随机串，不长于 32 位
	    	    	    package: param.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	    	    	    signType: param.signType,// 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	    	    	    paySign: param.paySign, // 支付签名
	    	    	    success: function (res) {
	    	    	    	//alert("微信接口支付成功:"+JSON.stringify(res));
							//hideLoading();
	    	    	    	receivePayNotify(orderNo,type);//支付成功，查询状态
	    	    	    },
		        		fail:function(res){
		        			//支付失败
							//hideLoading();
							//alert("微信接口支付失败:"+JSON.stringify(res));
			    	    	showMsg(JSON.stringify(res));
			    	    },
			    	    cancel:function(res){
			    	    	WeixinJS.back();
			    	    }
	        	    });
	        	}else{
	        		showMsg("", result.desc, 1);
	        	}
	        },
	        error:function(){
				//hideLoading();
	        	showMsg("", "网络异常", 1);
	        }
	    });
	}
	//获取支付订单实时状态
	function receivePayNotify(orderNo,type){
		$.ajax({
	        type:"POST",
	        url: baseURL+"/portal/payport/payportAction!receivePayNotify.action",
	        data:{
	        	'orderNo':orderNo,
	        	'type':type
	        },
	        async: false,
	        cache:false,
	        dataType: "json",
	        success: function(result){
	        	if(result.code=="0"){
	        		payCallBack();//回调
	        	}else{
	        		showMsg("", result.desc, 1);
	        	}
	        },
	        error:function(){
	        	showMsg("", "网络异常", 1);
	        }
	    });
	}
	//回调方法，让引用页重构的
	function payCallBack(){
		var payCallbackURL=decodeURIComponent(GetQueryString("payCallbackURL"));
		if(""!=payCallbackURL && undefined!=payCallbackURL && null!=payCallbackURL){
			//回调函数
			if(payCallbackURL.toLowerCase().indexOf(".action")>0){//理论上不在再第一位，所以不会=0
				$.ajax({
					type:"POST",
					url: payCallbackURL,
					async: false,
					cache:false,
					dataType: "json",
					success: function(result){
						if(result.code=="0"){
							showMsg("提示信息", result.desc, 1,{ok:function(){WeixinJS.back();}});
							
						}else{
							showMsg("", result.desc, 1);
						}
					},
					error:function(){
						showMsg("", "网络异常", 1);
					}
				});
			}else{
				WeixinJS.back();//返回刷新页面
			}
		}else{
			//如果没有回到函数，直接返回刷新页面
			WeixinJS.back();
		}
	}
</script>
