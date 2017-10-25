<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>企微云平台丨城市热点</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection">
        <meta content="email=no" name="format-detection">
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/selfService.css"/>
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/base.css">
        <script src="${baseURL}/xentthemes/citywifi/wap/js/jquery-1.7.2.min.js"></script>
        <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
        <script src='${baseURL}/jsp/wap/js/wechat.js'></script>
    </head>
    <body class="login-bg">
		<input type="hidden" id="portalid" value="${param.portalid }"/>
        <div class="wrap">
            <h1 class="title">关注应用</h1>
            <div class="step">
                <img src="${baseURL}/xentthemes/citywifi/wap/images/selfsv06.png" alt="" class="" />
                <div class="clearfix">
                    <div class="fl orange">提交资料</div>
                    <div class="fl orange">扫码关注</div>
                </div>
            </div>
            <div class="qr-code">
				<img src="" alt="" class="" id="wxqrcode" style="display:none;"/>
				<div id="welcomeImgDiv_id" style="display:none;" class="welcome_imgBg"></div>
				<a id="wxqrcode_refresh" href="javascript:wxqrcodeRefresh(this);" class="imgArea" style="z-index: 1; display:none;"></a>
				<img id="erweima_id" style="display:none;" class="imgArea" src="${baseURL}/manager/images/bt_qrcode.png" >
                <p class=""><span id="qrCode_remain">扫一扫二维码，即可关注！</span></p>
            </div>
            <div class="footer">
                <i class=""></i>
                <a href="" class="">企微云平台</a>&<a href="" class="">城市热点</a>联合提供
            </div>
        </div>
    </body>
	<%@include file="/jsp/wap/include/showMsg.jsp"%>
    
    <script type="text/javascript">
	$(document).ready(function(){
    	showLoading();
		$.ajax({
	        type:"POST",
	        url: "${baseURL}/open/wifi/wifiAction!srhCooperationLogo.action",
	        data: {"portalid":$("#portalid").val()},
	        cache:false,
	        async : false,
	        dataType: "json",
	        success:function(result){
                hideLoading();
	            if("0" == result.code){
                    var qrUrl = result.data.qrUrl;
                    if(("" != qrUrl) && (undefined != qrUrl)){
                        //判断如果是手机端就立刻跳转到url地址
                        browserRedirect(qrUrl);
                    }
                    if("" != result.data.logo){
                        $("#wxqrcode").attr('src',result.data.logo);
                        $("#wxqrcode").show();
                    }
				}
				else{
                    _alert("提示",result.desc+"重试请刷新本页面获取二维码！","确认");
				}
	        }
    	});
	})
	
	function browserRedirect(url) { 
		var sUserAgent= navigator.userAgent.toLowerCase(); 
		var bIsIpad= sUserAgent.match(/ipad/i) == "ipad"; 
		var bIsIphoneOs= sUserAgent.match(/iphone os/i) == "iphone os"; 
		var bIsMidp= sUserAgent.match(/midp/i) == "midp"; 
		var bIsUc7= sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4"; 
		var bIsUc= sUserAgent.match(/ucweb/i) == "ucweb"; 
		var bIsAndroid= sUserAgent.match(/android/i) == "android"; 
		var bIsCE= sUserAgent.match(/windows ce/i) == "windows ce"; 
		var bIsWM= sUserAgent.match(/windows mobile/i) == "windows mobile"; 
		 
		if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) { 
			$("qrCode_remain").html("请保存二维码图片，并在微信打开二维码图片，长按识别二维码即可关注！");
			window.location.href= url; //手机
		}
	} 
    </script>
</html>