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
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <!-- 众安样式 -->
    <link href="resource/stylesheets/common.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/za-common.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/slider-min.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/za-add.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="resource/stylesheets/za-ent.css?ver=<%=jsVer%>" rel="stylesheet" />
	<link href="resource/stylesheets/newEnterprise/index.css?ver=<%=jsVer%>" rel="stylesheet" />
	
	<!--[if IE 9]>
    <style type="text/css">
    :root .m-adv-item:hover{
        width: 350px;
        margin-top: 0;
    }

    :root .m-adv-item:hover .m-adv-bd{
        padding-top: 0;
        padding-bottom: 0;
    }
    :root .m-adv-item:hover .m-adv-itemtd{
        height: 40px;
        padding-top: 20px;
    }
    :root .m-adv-item:hover .m-adv-itemfd{
        height: 80px;
        line-height: 80px;
    }
    </style>
    <![endif]-->
	<!-- 众安样式 end -->
	<link rel="stylesheet" type="text/css" href="${baseURL}/manager/benefit/zhongan/css/zn-main.css?ver=<%=jsVer%>">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript">var BASE_URL = "${baseURL}";</script>
<!--     <style type="text/css">
    	#container{width: 100%;}
    </style> -->
</head>
<body>
    <c:set var="titleActiveCLass" value="8" scope="request"/>
    <c:set var="leftMenuActiveCLass" value="${param.classClick }" scope="request"/>
    <%@include file="../../head.jsp" %>
    
    <div class="wrap-container">
        <div id="container" class="clearfix">
        	<div class="za-guide">
					<div>
						<a href="../main.jsp">员工福利</a>
						<i>></i>
						<a href="../zhongan/za-main.jsp">员工商业保险</a>
						<i>></i>
						<a class="active" href="javascript:;" id="contentGuide">我的订单</a>
					</div>
				</div>
        
            <div id="aside_first" class="aside">
                <div class="inner-aside">
                    <div class="sideMenu">
                        <ul class="sideMenu-nav" id="leftMenu_ul">
                            <%@include file="value_left_menu.jsp" %>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="main">
                <div class="inner-main">
                    <div class="main_content">
                        <iframe  name="personFrame" id="personFrame" src="" width="100%" height="400px;" frameborder="0" style="overflow-x:hidden;overflow-y:auto" >
                        </iframe>
                    </div>

                </div>
            </div>
        </div>
    </div>
    
	<script src="resource/res/js/jquery-1.8.1.min.js"></script>
	<script src="resource/res/js/bui-min.js"></script>
	<script src="resource/res/js/slider-min.js"></script>
	<script src="resource/res/js/common.js"></script>
	<script src="resource/res/js/unslider.min.js"></script>
	<script type="text/javascript" src="js/za-index.js"></script>
	<script src="web/js/common/common.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
    <%@include file="../../fooder.jsp" %>
   

</body>
</html>