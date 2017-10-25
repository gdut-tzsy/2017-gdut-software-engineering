<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%
    pageContext.setAttribute("endpoint", Configuration.OSS_END_POINT);
    pageContext.setAttribute("bucket", Configuration.VOD_INPUT_BUCKET);
    pageContext.setAttribute("objectPre", Configuration.VOD_INPUT__PATH);
    pageContext.setAttribute("durationSecond", Configuration.DURATION_SECOND);
%>
<%@include file="/jsp/common/dqdp_common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title>Title</title>
    <script src="aliyun-sdk.min.js"></script>
    <script src="vod-sdk-upload-1.0.5.min.js"></script>
    <script src="uploadVideo.js"></script>
    <script src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
</head>
<body>
上传视频
<hr/>
<table frame=void width="400px">

    <tr>
        <td>endpoint:</td>
        <td><input type="text" id="endpoint" size="40" value="http://oss-cn-hangzhou.aliyuncs.com"></td>
        <td>bucket:</td>
        <td><input type="text" id="bucket" size="20" value="qwy-vod-input"></td>
    </tr>
    <tr>
        <td>object路径:</td>
        <td><input type="text" id="objectPre" size="20" value="vodin/"></td>
    </tr>
    <tr>
        <td>标题:</td>
        <td><input type="text" id="title" size="20" value=""></td>
        <td>简介:</td>
        <td><input type="text" id="blurb" size="20" value=""></td>
    </tr>
</table>
<input type="text" id="videoId"  value="">
<hr/>

文件管理
<hr/>
<input type="file" name="file" id="files" multiple/>
<button type="button" onclick="deleteFile()">删除文件</button>
<input type="text" id="deleteIndex" size="3" value="0">
<button type="button" onclick="cancelFile()">取消文件</button>
<input type="text" id="cancelIndex" size="3" value="0">

<button type="button" onclick="resumeFile()">恢复文件</button>
<input type="text" id="resumeIndex" size="3" value="0">

<hr/>
列表管理
<hr/>
<button type="button" onclick="getList()">获取上传列表</button>
<button type="button" onclick="clearList()">清理上传列表</button>
<hr/>
上传管理
<hr/>
<button type="button" onclick="start()">开始上传</button>
<button type="button" onclick="stop()">停止上传</button>
<button type="button" onclick="resumeWithToken()">Token恢复上传</button>
<hr/>
列表管理
<hr/>
<td>mediaId:</td>
<td><input type="text" id="mediaId" size="40" value=""></td>
<button type="button" onclick="testDel()">mediaId删除</button>
<hr/>
<select multiple="multiple" id="textarea"
        style="position:relative; width:620px; height:450px; vertical-align:top; border:1px solid #cccccc;"></select>
<button type="button" onclick="clearLog()">清日志</button>

<script>
    var endpoint = "${endpoint}";
    var bucket = "${bucket}";
    var objectPre = "${objectPre}";
    var durationSecond = "${durationSecond}";
    document.getElementById("files")
            .addEventListener('change', function (event) {
                var title = document.getElementById("title").value;
                var blurb = document.getElementById("blurb").value;
                videoId = uuid();
                document.getElementById("videoId").value = videoId;
                var userData = '{"Vod":{"Title":"'+title+'","Description":"'+ blurb +'","CateId":"19","Tags":"tag1,tag2,标签3","UserData":"'+videoId+'"}}';

                for (var i = 0; i < event.target.files.length; i++) {
                    log("add file: " + event.target.files[i].name);
                    upLoadFileName = event.target.files[i].name;
                    uploader.addFile(event.target.files[i], endpoint, bucket, objectPre + event.target.files[i].name, userData);
                }
            });

    var textarea = document.getElementById("textarea");
   </script>

</body>
</html>
