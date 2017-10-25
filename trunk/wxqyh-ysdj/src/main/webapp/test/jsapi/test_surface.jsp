<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试界面操作</title>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body id="id_body" style="display: none">
<div>
    <ul>
        <li><a href="javascript:hideMenu();">隐藏右上角菜单接口 </a></li>
        <li><a href="javascript:showMenu();">显示右上角菜单接口 </a></li>
        <li><a href="javascript:closeWindow();">关闭当前网页窗口接口 </a></li>
        <li><a href="javascript:hideFuncations();">批量隐藏功能按钮接口 </a></li>
        <li><a href="javascript:showFuncations();">批量显示功能按钮接口 </a></li>
        <li><a href="javascript:hideNoneBasic();">隐藏所有非基础按钮接口 </a></li>
        <li><a href="javascript:showAllFuncations();">显示所有功能按钮接口 </a></li>
    </ul>
</div>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    function hideMenu() {
        wx.hideOptionMenu();
    }
    function showMenu() {
        wx.showOptionMenu();
    }
    function closeWindow() {
        wx.closeWindow();
    }
    function hideFuncations() {
        wx.hideMenuItems({
            menuList: ['menuItem:exposeArticle' , 'menuItem:setFont'], // 要隐藏的菜单项，所有menu项见附录3
            success: function () {
                alert("已隐藏举报和调整字体两个功能");
            },
            fail: function (err) {
                alert(err.errMsg);
            }
        });

    }
    function showFuncations() {
        wx.showMenuItems({
            menuList: ['menuItem:exposeArticle' , 'menuItem:setFont'] // 要显示的菜单项，所有menu项见附录3
        });
        alert("已开启举报和调整字体两个功能");
    }
    function hideNoneBasic() {
        wx.hideAllNonBaseMenuItem();
    }
    function showAllFuncations() {
        wx.showAllNonBaseMenuItem();
    }
</script>