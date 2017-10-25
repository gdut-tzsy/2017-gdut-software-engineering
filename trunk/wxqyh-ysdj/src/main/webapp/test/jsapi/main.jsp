<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试主页</title>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="false"></jsp:param>
</jsp:include>
<body>
<ul>
    <li><a href="test_jsapi_support.jsp">JS支持情况</a></li>
    <li><a href="test_share.jsp">分享</a></li>
    <li><a href="test_photo.jsp">图片</a></li>
    <li><a href="test_audio.jsp">音频</a></li>
    <li><a href="test_smart.jsp">智能接口</a></li>
    <li><a href="test_device.jsp">设备信息</a></li>
    <li><a href="test_geo.jsp">地理位置</a></li>
    <li><a href="test_surface.jsp">界面操作</a></li>
    <li><a href="test_scaner.jsp">扫一扫</a></li>
    <li><a href="test_pay.jsp">微信支付</a></li>
</ul>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>