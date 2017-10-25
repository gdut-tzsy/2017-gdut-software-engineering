<%--
  Created by IntelliJ IDEA.
  User: eimserver
  Date: 2015/6/9
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<%@include file="../../jsp/common/dqdp_common.jsp" %>
<head>
    <title></title>
    <script src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
    
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <link href="${baseURL}/js/3rd-plug/jquery-ui-1.8/css/smoothness/jquery-ui-1.8.custom.css" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-ui-1.8.custom.min.js"></script>
	<script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body>
<div>
		<br/>
		金额：<div id="payCost"></div><br/>
		商品名称:<div id="productDesc"></div><br/>
		agentCode:<div id="agentCode"></div><br/>
		groupId:<div id="groupId"></div><br/>
		
		<a href="javascript:doMyPay();">参数支付</a><br/><br/><br/>
		
		<a href="javascript:doPayTest();">直接支付测试1</a><br/><br/><br/>
		
		<a href="javascript:doReceivePayNotify();">获取支付状态</a><br/><br/><br/>
</div>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>

<script type="text/javascript">
	var orderNo="";
	var type="payResultNotify";
	
	function doMyPay(){
		var productDesc=decodeURIComponent(getParam("productDesc"));
		doWechatPay("${param.payCost}",productDesc,"${param.agentCode}","${param.groupId}");
	}
	
	function doPayTest(){
		doWechatPay("1","activityPaytest","activity","1e2074a1-2971-43c8-9eb3-7066ec929463");
	}
	
	function doReceivePayNotify(){
		alert("获取支付状态");
		receivePayNotify("384f165f3641409981962ea6f18551c6",type);
	}
	
	//调用支付接口
	function doWechatPay(payCost,productDesc,agentCode,groupId){
		alert("调用支付接口:"+payCost+"_"+productDesc+"_agentCode:"+agentCode+"_groupId:"+groupId);
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
	        	if(result.code=="0"){
	        		//返回成功，调用微信支付
	        		alert(JSON.stringify(result.data.orderInfo));
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
	    	    	    	alert(JSON.stringify(res));
	    	    	    	receivePayNotify(orderNo,type);
	    	    	    },
		        		fail:function(res){
		        			//支付失败
			    	    	alert(JSON.stringify(res));
			    	    }
	        	    });
	        	}else{
	        		showMsg("", result.desc, 1);
	        	}
	        },
	        error:function(){
	        	alert("网络异常");
	        }
	    });
	}

	//获取支付订单实时状态
	function receivePayNotify(orderNo,type){
		alert("获取订单支付状态:"+orderNo+"_type:"+type);
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
	        		//返回成功，调用微信支付
	        		alert(JSON.stringify(result));
	        		//showMsg("", result.desc, 1);
	        		payCallBack();
	        	}else{
	        		alert(JSON.stringify(result));
	        		//showMsg("", result.desc, 1);
	        	}
	        },
	        error:function(){
	        	alert("网络异常");
	        }
	    });
	}
	
	//回调方法，让引用页重构的
	function payCallBack(){
		alert("支付成功,刷新报名");
		//WeixinJS.back();
	}
	
	$(document).ready(function () {
		$("#payCost").html("${param.payCost}");
		var productDesc=decodeURIComponent(getParam("productDesc"));
		$("#productDesc").html(productDesc);
		$("#agentCode").html("${param.agentCode}");
		$("#groupId").html("${param.groupId}");
		doMyPay();
	});
</script>
