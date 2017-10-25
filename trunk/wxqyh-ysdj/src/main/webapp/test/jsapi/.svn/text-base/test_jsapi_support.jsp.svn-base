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

<script type="text/javascript">
    wx.ready(function () {
        wx.checkJsApi({
            jsApiList: [ 'checkJsApi',
                'onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'chooseImage',
                'previewImage',
                'uploadImage',
                'downloadImage',
                'openLocation',
                'getLocation',
                'chooseCard',
                'openCard',
                'startRecord',
                'stopRecord',
                'onVoiceRecordEnd',
                'playVoice',
                'pauseVoice',
                'stopVoice',
                'uploadVoice',
                'downloadVoice',
                'onVoicePlayEnd',
                'translateVoice',
                'getNetworkType',
                'hideOptionMenu',
                'showOptionMenu',
                'closeWindow',
                'hideMenuItems',
                'showMenuItems',
                'hideAllNonBaseMenuItem',
                'showAllNonBaseMenuItem',
                'scanQRCode'
            ], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function (res) {
                var supported = res.checkResult;
                document.write("<ul>");
                document.write("<li>获取“分享到朋友圈”按钮点击状态及自定义分享内容接口:" + supported.onMenuShareTimeline + "</li>");
                document.write("<li>获取“分享给朋友”按钮点击状态及自定义分享内容接口:" + supported.onMenuShareAppMessage + "</li>");
                document.write("<li>获取“分享到QQ”按钮点击状态及自定义分享内容接口:" + supported.onMenuShareQQ + "</li>");
                document.write("<li>获取“分享到腾讯微博”按钮点击状态及自定义分享内容接口:" + supported.onMenuShareWeibo + "</li>");
                document.write("<li>拍照或从手机相册中选图接口:" + supported.chooseImage + "</li>");
                document.write("<li>预览图片接口:" + supported.previewImage + "</li>");
                document.write("<li>上传图片接口:" + supported.uploadImage + "</li>");
                document.write("<li>下载图片接口:" + supported.downloadImage + "</li>");
                document.write("<li>使用微信内置地图查看位置接口:" + supported.openLocation + "</li>");
                document.write("<li>获取地理位置接口:" + supported.getLocation + "</li>");
                document.write("<li>chooseCard:" + supported.chooseCard + "</li>");
                document.write("<li>openCard:" + supported.openCard + "</li>");
                document.write("<li>开始录音接口:" + supported.startRecord + "</li>");
                document.write("<li>停止录音接口:" + supported.stopRecord + "</li>");
                document.write("<li>监听录音自动停止接口:" + supported.onVoiceRecordEnd + "</li>");
                document.write("<li>播放语音接口:" + supported.playVoice + "</li>");
                document.write("<li>暂停播放接口:" + supported.pauseVoice + "</li>");
                document.write("<li>停止播放接口:" + supported.stopVoice + "</li>");
                document.write("<li>上传语音接口:" + supported.uploadVoice + "</li>");
                document.write("<li>下载语音接口:" + supported.downloadVoice + "</li>");
                document.write("<li>监听语音播放完毕接口:" + supported.onVoicePlayEnd + "</li>");
                document.write("<li>识别音频并返回识别结果接口:" + supported.translateVoice + "</li>");
                document.write("<li>获取网络状态接口:" + supported.getNetworkType + "</li>");
                document.write("<li>隐藏右上角菜单接口:" + supported.hideOptionMenu + "</li>");
                document.write("<li>显示右上角菜单接口:" + supported.showOptionMenu + "</li>");
                document.write("<li>关闭当前网页窗口接口:" + supported.closeWindow + "</li>");
                document.write("<li>批量隐藏功能按钮接口:" + supported.hideMenuItems + "</li>");
                document.write("<li>批量显示功能按钮接口:" + supported.showMenuItems + "</li>");
                document.write("<li>隐藏所有非基础按钮接口:" + supported.hideAllNonBaseMenuItem + "</li>");
                document.write("<li>显示所有功能按钮接口:" + supported.showAllNonBaseMenuItem + "</li>");
                document.write("<li>调起微信扫一扫接口:" + supported.scanQRCode + "</li>");
                document.write("</ul>");
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            },
            fail: function(err){
                alert("你的人品再一次打败了服务器！"+err.errMsg);
            }
        });
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    });
</script>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>