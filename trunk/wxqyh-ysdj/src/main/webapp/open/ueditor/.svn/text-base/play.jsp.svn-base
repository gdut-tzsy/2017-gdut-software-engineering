<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>播放页面</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" id="meta" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/invitation.css?ver=<%=jsVer%>"/>
	     <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
	    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	    <script src="${resourceURL}/js/wx/wx.js?ver=<%=jsVer%>"></script>
	    <script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="${resourceURL}/js/do1/common/checkCard.js?ver=<%=jsVer%>"></script>
    </head>
	<body>
    <iframe height="auto" width="100%" src="" frameborder="0" allowfullscreen="" id="video_iframe" style=""></iframe>
    <%@include file="/open/include/footer.jsp"%>
	<%@include file="/open/include/showMsg.jsp"%>
    <script>
        var url = location.search;
        url=url.substring(url.indexOf('=')+1,url.length);
        $("#video_iframe").attr("src",url);
    </script>
	</body>
</html>