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
    <title>音频测试</title>
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body id="id_body" style="display: none">
<ul>
    <li><a href="javascript:startRecord();">开始录音接口 </a></li>
    <li><a href="javascript:stopRecord();">停止录音接口 </a></li>
    <li><a href="javascript:playRecord();">播放音频接口 </a></li>
    <li><a href="javascript:pausePlay();">暂停播放接口 </a></li>
    <li><a href="javascript:stopPlay();">停止播放接口 </a></li>
    <li><a href="javascript:uplaodRecord();">上传语音接口 </a></li>
    <li><a href="javascript:downRecord();">下载语音接口 </a></li>
    <li><a href="javascript:listenRecordAutoStop();">监听录音自动停止 </a></li>
    <li><a href="javascript:listenPlayAutoStop();">监听播放自动停止 </a></li>
    <li><a href="javascript:identifyAudio();">智能语音识别 </a></li>
</ul>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    var localId = null;
    var downId = null;
    var recording = false;
    var playing = false;
    function startRecord() {
        recording = true;
        wx.startRecord();
    }
    function stopRecord() {
        wx.stopRecord({
            success: function (res) {
                recording = false;
                localId = res.localId;
                alert("录音已停止");
            },
            fail: function (err) {
                alert("录音根本就停不下来：" + err.errMsg);
            }
        });
    }
    function playRecord() {
        if (localId == null) {
            alert("请先录音");
            return;
        }
        playing = true;
        wx.playVoice({
            localId: localId // 需要播放的音频的本地ID，由stopRecord接口获得
        });
    }
    function pausePlay() {
        if (!playing) {
            alert("请先开始播放录音");
            return;
        }
        wx.pauseVoice({
            localId: localId // 需要暂停的音频的本地ID，由stopRecord接口获得
        });
    }
    function stopPlay() {
        if (!playing) {
            alert("请先开始播放录音");
            return;
        }
        playing = false;
        wx.stopVoice({
            localId: localId // 需要停止的音频的本地ID，由stopRecord接口获得
        });
    }
    function uplaodRecord() {
        if (localId == null) {
            alert("请先录音");
            return;
        }
        wx.uploadVoice({
            localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
            isShowProgressTips: 1,// 默认为1，显示进度提示
            success: function (res) {
                downId = res.serverId; // 返回音频的服务器端ID
                alert("音频已上传成功");
            },
            fail: function (err) {
                alert("音频上传失败:" + err.errMsg);
            }
        });
    }
    function downRecord() {
        if (downId == null) {
            alert("请先上传录音");
            return;
        }
        wx.downloadVoice({
            serverId: downId, // 需要下载的音频的服务器端ID，由uploadVoice接口获得
            isShowProgressTips: 1,// 默认为1，显示进度提示
            success: function (res) {
                localId = res.localId; // 返回音频的本地ID
                alert("音频已下载成功");
            },
            fail: function (err) {
                alert("音频下载失败:" + err.errMsg);
            }
        });
    }
    function listenRecordAutoStop() {
        if (!recording)
            startRecord();
        wx.onVoiceRecordEnd({
            // 录音时间超过一分钟没有停止的时候会执行 complete 回调
            complete: function (res) {
                recording = false;
                localId = res.localId;
                alert("当前录音已自动停止");
            },
            fail: function (err) {
                alert("当前录音自动停止失败:" + err.errMsg);
            }
        });
    }
    function listenPlayAutoStop() {
        if (!playing)
            playRecord();
        wx.onVoicePlayEnd({
            serverId: localId, // 需要下载的音频的服务器端ID，由uploadVoice接口获得
            success: function (res) {
                playing = false;
                var localId = res.localId; // 返回音频的本地ID
                alert("当前播放已自动停止");
            },
            fail: function (err) {
                alert("当前播放自动停止失败:" + err.errMsg);
            }
        });
    }
    function identifyAudio() {
        if (localId == null) {
            alert("请先录音");
            return;
        }
        wx.translateVoice({
            localId: localId, // 需要识别的音频的本地Id，由录音相关接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (res) {
                alert("识别结果" + res.translateResult); // 语音识别的结果
            }
        });
    }
</script>