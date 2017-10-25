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
	            <li id="aq_li_3" class="sideActive"><a href="#">计费方式</a></li>
	            <li id="aq_li_4"><a href="#">如何叫车</a></li>
	            <li id="aq_li_5"><a href="#">支付方式</a></li>
	            <li id="aq_li_6"><a href="#">如何查看订单</a></li>
	            <li id="aq_li_7"><a href="#">如何申请用车发票</a></li>
	        </ul>
	    </div>
		<div class="rightContent fr">
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
	        <p>问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述问题答案描述。</p>
    	</div>
	    <div class="clear"></div>
	</div>
	<%@include file="footer.jsp" %>
</body>
</html>