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
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/selfService.css">
        <script src="${baseURL}/xentthemes/citywifi/wap/js/jquery-1.7.2.min.js"></script>
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
                <img src="${baseURL}/xentthemes/citywifi/wap/images/test_img.jpg" alt="" class="" id="test_qrcode"/>
                <img src="" alt="" class="" id="wxqrcode" style="display:none;"/>
				<div id="welcomeImgDiv_id" style="display:none;" class="welcome_imgBg"></div>
				<a id="wxqrcode_refresh" href="javascript:wxqrcodeRefresh(this);" class="imgArea" style="z-index: 1; display:none;"></a>
				<img id="erweima_id" style="display:none;" class="imgArea" src="${baseURL}/manager/images/bt_qrcode.png" >
                <p class="">长按保存二维码，扫一扫关注</p>
            </div>
            <div class="footer">
                <i class=""></i>
                <a href="" class="">企微云平台</a>&<a href="" class="">城市热点</a>联合提供
            </div>
        </div>
    </body>
    
<%-- 	<script src="${baseURL}/js/qw/companymsg.js?ver=<%=jsVer%>"></script> --%>
    <script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
	        type:"POST",
	        url: "${baseURL}/open/wifi/wifiAction!getCooperationLogo.action",
	        data: {"portalid":$("#portalid").val()},
	        cache:false,
	        async : false,
	        dataType: "json",
	        success:function(result){
				if(result.data.logo != ""){
					$("#wxqrcode").attr('src',result.data.logo);
					$("#wxqrcode").show();
					$("#test_qrcode").hide();
				}
	        }
    });
	})
    </script>
</html>