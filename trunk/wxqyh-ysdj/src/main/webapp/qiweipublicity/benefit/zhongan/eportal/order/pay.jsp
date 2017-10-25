<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value="benefitMenu,remindMenu,flowMenu,flowgroupMenu,moveMenu,askMenu,groupMenu,crmMenu,configMenu,contactMenu,sysmgrMenu,activityMenu,dynamicinfoMenu,infoMenu,outworkMenu,taskTemMenu,meetingRoomMenu,explainMenu,countMenu,productMenu,formMenu,topicMenu,diaryMenu,surveyMenu,settingmenu,settingusermenu,settingrolemenu,cooperationMenu,dimissionmenu,checkworkMenu,currencySettingMenu,qyPaySettingMenu,settingaccountmenu"></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script type="text/javascript">
    	var BASE_URL = "${baseURL}";
    	var yuming="<%=Configuration.QIWEI_WEB_LOCAL_PORT%>";
    	//alert(yuming);
    </script>
</head>
<body>
    
	<script src="../../web/js/common/common.js?ver=<%=jsVer%>"></script>
	<script src="../../js/md5.js"></script>
	<script src="../../web/js/eportal/order/pay.js?ver=<%=jsVer%>"></script>
    <%@include file="/manager/fooder.jsp" %>
</body>
</html>
