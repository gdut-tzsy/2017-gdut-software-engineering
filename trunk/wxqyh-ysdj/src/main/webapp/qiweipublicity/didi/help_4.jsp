<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    
	<link href="${baseURL}/themes/manager/companysrv/css/style_company.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    
   <script src='${baseURL}/themes/qiweipublicity/didi/help.js?ver=<%=jsVer%>'></script>
    
    <style type="text/css">
        body{background: #eee;}
        .header{background: #fcfcfc;border-bottom:1px #eee solid;}
    </style>
</head>
<body>
	<div class="header">
	    <div class="navbar w1200">
	                    <span title="企业微信应用，首选企微云平台" class="logo fl">
	                    <img src="../../themes/manager/companysrv/images/logo.png" alt="" style="width:125px;height:35px;">
	                    </span>
	        <a title="企业微信应用，首选企微云平台" href="#" class="logo fl fz20">企业叫车</a>
	        <ul class="nav fr">
	            <li><a href="didi_index.jsp">首页</a></li>
	            <li><a href="../../manager/companysrv/order_list.jsp">用车订单</a></li>
	            <li><a href="didi_index.jsp?#sectionFive">用车攻略</a></li>
	            <li><a href="#" class="active">帮助</a></li>
	        </ul>
	        <div class="clear"></div>
	    </div>
	</div>
	<div class="mainContent">
	    <div class="sideBar fl">
	        <ul>
	            <li id="aq_li_1"><a href="#">如何为企业账户充值</a></li>
	            <li id="aq_li_2"><a href="#">如何分配用车权限</a></li>
	            <li id="aq_li_3" style="display:none;"><a href="#">计费方式</a></li>
	            <li id="aq_li_4" class="sideActive"><a href="#">如何叫车</a></li>
	            <li id="aq_li_5"><a href="#">支付方式</a></li>
	            <li id="aq_li_6"><a href="#">如何查看订单</a></li>
	            <li id="aq_li_7"><a href="#">如何申请用车发票</a></li>
	        </ul>
	    </div>
	    <div class="rightContent fr">
	        <h3 class="mb10">如何叫车</h3>
	        <h5>员工在微信上进入企业微信的<font class="f-orange">【车辆管理】</font>应用 , 点击<font class="f-orange">【我要用车】</font>-<font class="f-orange">【企业叫车】</font> , 输入叫车信息即可向企业叫车发起叫车请求</h5>
	        <img src="../../themes/manager/companysrv/images/help_4.jpg" height="538" width="902" class="mt35">
	    </div>
	    <div class="clear"></div>
	</div>
	<%@include file="footer.jsp" %>
</body>
</html>