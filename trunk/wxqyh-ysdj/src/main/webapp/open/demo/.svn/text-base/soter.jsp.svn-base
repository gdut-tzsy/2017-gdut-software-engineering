<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
	<title>测试指纹验证</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="">
	<meta name="HandheldFriendly" content="True">
	<meta name="MobileOptimized" content="320">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta content="telephone=no" name="format-detection" />
	<meta content="email=no" name="format-detection" />
	<link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
	<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	<script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	<script type="text/javascript" src="${jweixin}"></script>
	<script type="text/javascript" src="${baseURL}/jsp/wap/js/CheckJSApi.js?ver=<%=jsVer%>"></script>
</head>
<body class="sForm">
</body>
</html>
<script type="text/javascript">
	if(!isWeChatApp()){//手机端
		showMsg("提示","请在手机端进行指纹验证","确定");
	}else{
		wx.ready(function () {
			wx.invoke("getSupportSoter", {},function(res){
				if(res.support_mode=='0'){
					showMsg("提示","该手机不支持指纹审批功能，处理时必须使用手写签名，否则流程无法继续流转！",1);
				}else{
					showMsg("提示","该手机支持指纹审批，可启用指纹审批！",1);
				}
			});
		});
	}
</script>