<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 12:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Simple jsp page</title>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body id="id_body" style="display: none">
<ul>
    <li><a href="javascript:shareToCycle()">获取“分享到朋友圈”按钮点击状态及设置分享内容接口</a></li>
    <li><a href="javascript:shareToFriend()">获取“分享给朋友”按钮点击状态及设置分享内容接口 </a></li>
    <li><a href="javascript:shareToQQ()">获取“分享到QQ”按钮点击状态及设置分享内容接口 </a></li>
    <li><a href="javascript:shareToTCWeibo()">获取“分享到腾讯微博”按钮点击状态及设置分享内容接 </a></li>
</ul>
<div id="div_content">
    <table width="100%">
        <tr style="display: none">
            <td>title</td>
            <td><input type="text" name="title" value="测试分享"></td>
        </tr>
        <tr style="display: none">
            <td>link</td>
            <td><input type="text" name="link" value="www.baidu.com"></td>
        </tr>
        <tr style="display: none">
            <td>imgUrl</td>
            <td><input type="text" name="imgUrl" value="http://qy.do1.com.cn/qwy/manager/images/logo.png"></td>
        </tr>
        <tr style="display: none">
            <td>desc</td>
            <td><input type="text" name="desc" value="仅仅是测试"></td>
        </tr>
        <tr style="display: none">
            <td>type</td>
            <td><input type="text" name="type"></td>
        </tr>
        <tr style="display: none">
            <td>dataUrl</td>
            <td><input type="text" name="dataUrl"></td>
        </tr>
        <tr style="display: none" id="btn">
            <td colspan="2">
                <button name="btn_submit" type="button" onClick="share()">分享</button>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    var shareFunc;
    var title;
    var link;
    var imgUrl;
    var type;
    var desc;
    var dataUrl;
    function shareToCycle() {
        showContent(["title", "link", "imgUrl"]);
        shareFunc = function () {
            alert("share to cycle");
            wx.onMenuShareTimeline({
                title: title, // 分享标题
                link: link, // 分享链接
                imgUrl: imgUrl, // 分享图标
                success: function () {
                    alert("有文化、懂分享，小伙子，看好你哦");
                    // 用户确认分享后执行的回调函数
                },
                fail:function(result){
                    alert("纳尼？连分享都不会，你家里人造吗？失败原因："+result.errMsg);
                },
                cancel: function () {

                    // 用户取消分享后执行的回调函数
                }
            });
        }
    }
    function shareToFriend() {
        showContent(["title", "link", "imgUrl", "desc", "type", "dataUrl"]);
        shareFunc = function () {
            wx.onMenuShareAppMessage({
                title: title, // 分享标题
                desc: desc, // 分享描述
                link: link, // 分享链接
                imgUrl: imgUrl, // 分享图标
                type: type, // 分享类型,music、video或link，不填默认为link
                dataUrl: dataUrl, // 如果type是music或video，则要提供数据链接，默认为空
                success: function () {
                    alert("有文化、懂分享，小伙子，看好你哦");
                    // 用户确认分享后执行的回调函数
                },
                fail: function () {
                    alert("纳尼？连分享都不会，你家里人造吗？失败原因："+result.errMsg);
                    // 用户取消分享后执行的回调函数
                }
            });
        }
    }
    function shareToQQ() {
        showContent(["title", "link", "imgUrl", "desc"]);
        shareFunc = function () {
            wx.onMenuShareQQ({
                title: title, // 分享标题
                desc: desc, // 分享描述
                link: link, // 分享链接
                imgUrl: imgUrl, // 分享图标
                success: function () {
                    alert("有文化、懂分享，小伙子，看好你哦");
                    // 用户确认分享后执行的回调函数
                },
                fail: function () {
                    alert("纳尼？连分享都不会，你家里人造吗？失败原因："+result.errMsg);
                    // 用户取消分享后执行的回调函数
                }
            });
        }
    }
    function shareToTCWeibo() {
        showContent(["title", "link", "imgUrl", "desc"]);
        shareFunc = function () {
            wx.onMenuShareWeibo({
                title: title, // 分享标题
                desc: desc, // 分享描述
                link: link, // 分享链接
                imgUrl: imgUrl, // 分享图标
                success: function () {
                    alert("有文化、懂分享，小伙子，看好你哦");
                    // 用户确认分享后执行的回调函数
                },
                fail: function () {
                    alert("纳尼？连分享都不会，你家里人造吗？失败原因："+result.errMsg);
                    // 用户取消分享后执行的回调函数
                }
            });
        }
    }
    function share() {
        title = document.getElementsByName("title")[0].value;
        link = document.getElementsByName("link")[0].value;
        imgUrl = document.getElementsByName("imgUrl")[0].value;
        type = document.getElementsByName("type")[0].value;
        desc = document.getElementsByName("desc")[0].value;
        dataUrl = document.getElementsByName("dataUrl")[0].value;

        shareFunc.call(shareFunc, null);
    }
    function showContent(showNames) {
        var inputEles = document.getElementsByTagName("INPUT");
        for (var i = 0; i < inputEles.length; i++) {
            var name = inputEles[i].getAttribute("name");
            if (showNames.indexOf(name) > -1)
                inputEles[i].parentNode.parentNode.style.display = "block";
            else
                inputEles[i].parentNode.parentNode.style.display = "none";
        }
        document.getElementById("btn").style.display = "block";
    }
</script>
