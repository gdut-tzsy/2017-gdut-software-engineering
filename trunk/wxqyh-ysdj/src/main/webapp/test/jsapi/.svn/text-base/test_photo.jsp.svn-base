<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 12:23
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
    <li><a href="javascript:choosePhoto();">本地选图或拍照接口 </a></li>
    <li><a href="javascript:previewPhoto();">图片预览接口 </a></li>
    <%--<li><a href="javascript:uploadPhoto();">上传图片接口 </a></li>--%>
    <li><a href="javascript:downPhoto();">下载图片接口 </a></li>
</ul>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    var localIds = null;
    var downId = null;
    function choosePhoto() {
        wx.chooseImage({
            success: function (res) {
                localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                if (confirm("这些图片想来应该是极好的，确认上传么？"))
                    uploadPhoto();
            },
            fail: function (err) {
                localIds = null;
                alert("失败呢种嘢呢，大家都唔想嘎，不如食碗面之后再来过？失败原因" + err.errMsg);
            }
        });
    }
    function previewPhoto() {
        wx.previewImage({
            current: 'http://img2.cache.netease.com/photo/0001/2015-05-21/AQ4NCS1300AP0001.jpg', // 当前显示的图片链接
            urls: ['http://img2.cache.netease.com/photo/0001/2015-05-21/AQ4NCS1300AP0001.jpg',
                'http://img3.cache.netease.com/photo/0001/2015-05-21/900x600_AQ4NCRM000AP0001.jpg',
                'http://img3.cache.netease.com/photo/0001/2015-05-21/900x600_AQ4NCU8U00AP0001.jpg',
                'http://img5.cache.netease.com/photo/0001/2015-05-21/900x600_AQ4NCSQK00AP0001.jpg'] // 需要预览的图片链接列表
        });
    }
    function uploadPhoto() {
        if (localIds == null || localIds.length < 1) {
            alert("图片都没选，臣妾做不到啊");
            return;
        }
        if (localIds.length > 1) {
            alert("图片这么多，臣妾做不到啊");
            return;
        }
        alert("id" + localIds[0]);
        wx.uploadImage({
            localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 1,// 默认为1，显示进度提示
            success: function (res) {
                alert("图片已收到，赏赐稍后就到");
                downId = res.serverId; // 返回图片的服务器端ID
            },
            fail: function (err) {
                alert("失败呢种嘢呢，大家都唔想嘎，不如食碗面之后再来过？失败原因" + err.errMsg);
            }
        });
    }
    function downPhoto() {
        if (downId == null) {
            alert("图片没上传，臣妾做不到啊");
            return;
        }
        wx.downloadImage({
            serverId: downId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
            isShowProgressTips: 1,// 默认为1，显示进度提示
            success: function (res) {
                var localId = res.localId; // 返回图片下载后的本地ID
                alert("图片已下载，id:" + localId);
            },
            fail: function (err) {
                alert("失败呢种嘢呢，大家都唔想嘎，不如食碗面之后再来过？失败原因" + err.errMsg);
            }
        });
    }

</script>