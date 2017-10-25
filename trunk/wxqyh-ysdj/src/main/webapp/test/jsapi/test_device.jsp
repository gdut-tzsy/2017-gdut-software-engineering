<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 12:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>设备信息获取</title>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body id="id_body" style="display: none">
<div>
    <ul>
        <li><a href="javascript:getNetType();">获取网络类型 </a></li>
    </ul>
</div>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    function getNetType() {
        wx.getNetworkType({
            success: function (res) {
                var networkType = res.networkType; // 返回网络类型2g，3g，4g，wifi
                alert("网络类型：" + networkType);
            }
        });
    }
</script>