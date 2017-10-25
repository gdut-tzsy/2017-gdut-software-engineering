<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script type="text/javascript">
    var _weixinInitied = false;
    if (_useWeixinJsApi) {
        alert("准备验证");
        $.ajax({
            url: baseURL + "/portal/weixinjs/weixinjsAction!authorize.action",
            type: "POST",
            data: {url: window.location.href},
            dataType: "json",
            success: function (result) {
                var apijs = result.data.apijs;
                if (!apijs) {
                    alert("这位施主，只能在指定的机器上调用验证接口哦");
                    return;
                }
                var appid = apijs.corpId;
                var timestamp = apijs.timestamp;
                var nonceStr = apijs.noncestr;
                var signature = apijs.signature;
                alert(signature);
                wx.config({
                    debug: true,
                    appId: appid,
                    timestamp: timestamp,
                    nonceStr: nonceStr,
                    signature: signature,
                    jsApiList: [
                        'checkJsApi',
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
                    ]
                });

            },
            error: function () {
                alert("粗大事了，找不到微信验证接口！");
            }
        });
    }
    wx.error(function (res) {
        alert("纳尼，竟然config失败了？");
        alert("你这脸得有多丑，人品得有多低！");
        alert("地球太危险，赶紧回火星！");
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

    });
    wx.ready(function () {
       $("#id_wait").hide();
       $("#id_body").show();
    });
</script>
