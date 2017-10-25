<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script type="text/javascript">
    var dqdp_csrf_token = "";
    var _useWeixinJsApi = ("true" == "${param.useWeixinJsApi}");
    var baseURL = "<%=request.getContextPath()%>";
    <%
    pageContext.setAttribute("baseURL",request.getContextPath());
    %>
</script>
<style type="text/css">
    button {
        font-size: 36px;
    }

    tr td {
        font-size: 36px;
    }

    body {
        font-family: arial, sans-serif;
        font-size: 36px;
    }

    ul li {
        font-size: 48px;
        margin-top: 10px;
        margin-bottom: 10px;
    }
</style>
<script type="text/javascript" src="${jweixin}"></script>
<script type="text/javascript" src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
<%
    if ("true".equals(request.getParameter("useWeixinJsApi"))) {
%>
<div id="id_wait" style="float: left">
    正在为页面配置接口数据，请稍候。。。。。
</div>
<%
    }
%>