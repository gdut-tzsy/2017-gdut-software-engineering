<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>找回支付密码</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
	<link href="../../experience2/css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
	<link href="../../experience2/css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="../../experience2/css/pwd.css" rel="stylesheet">
	<script src="../../experience2/js/jquery-1.11.1.js"></script>
	<script src="../../experience2/js/common.js?ver=<%=jsVer%>"></script>
	<script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
</head>
<body>
<%@include file="../../experience2/header.jsp" %>
    <div class="pwd_box">
        <h2>找回支付密码</h2>
        <ul class="step_line step_3 clearfix">
            <li>验证方式</li>
            <li style="margin: 0 120px;">重置密码</li>
            <li>完成</li>
        </ul>
        <div class="pwd_3">
            <img id="resultImg" src="../../experience2/images/ic_ok.png" alt="">
            <p class="c333 mt15"><b>${param.userName }</b></p>
            <div id="resultTipsDiv"></div>
            <div id="resultBtnDiv"></div>
        </div>
    </div>
<%@include file="../../experience2/footer.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
<script type="text/javascript">
$(function(){
    if("${param.result}"=="SUCCESS"){
        $("#resultImg").attr("src","../../experience2/images/ic_ok.png");
        $("#resultTipsDiv").html('<p class="c333">重置密码成功，请妥善保管</p>');
       /* $("#resultBtnDiv").html('<a href="${baseURL}" class="pwd_btn btn orangeBtn mt50">前往登录</a>');*/
    }else{
        $("#resultImg").attr("src","../../experience2/images/ic_fail.png");
        $("#resultTipsDiv").html('<p class="fRed">重置密码失败了，请返回重试</p>');
        $("#resultBtnDiv").html('<a href="${baseURL}/qiweipublicity/experience2/mail_Retrieve1.jsp?resetType=PAY" class="pwd_btn btn two_btn mt50">返回重试</a>');
    }
});
</script>
