<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>WiFi服务</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection">
        <meta content="email=no" name="format-detection">
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/selfService.css">
        <script src="${baseURL}/xentthemes/citywifi/wap/js/jquery-1.7.2.min.js"></script>
        <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    </head>
    <body class="login-bg">
    	<input type="hidden" id="portalId" value="test"/>
    	<input type="hidden" id="account" value="1240112272"/>
        <div class="wrap">
            <div class="tip-ico">
                <div class="ico-login"></div>
            </div>
            <p class="tip-words1">登录成功</p>
            <p class="tip-words2">点击下方按钮，前往关注微信企业号应用<br>实时掌握WiFi账号信息</p>
            <div class="btn-div"><input type="button" class="btn" value="立即关注" /></div>
        </div>
    </body>
    
    <script type="text/javascript">
    		$(".btn").click(function(){
//     			alert("");
    			$.ajax({
    				url:"${baseURL}/open/wifi/wifiAction!checkIsConcern4Account.action",
    				data:{
    					"portalid":$("#portalId").val(),
    					"account":$("#account").val()
    				},
    				type:"post",
//     				dataType:"json",
	        		success:function(result){
	        			window.location.href="${baseURL}/open/wap/citywifi/Qr_code.jsp";
	        		},
    				error:function(){
    					alert("");
    				}
    			})
    		})
    </script>
</html>