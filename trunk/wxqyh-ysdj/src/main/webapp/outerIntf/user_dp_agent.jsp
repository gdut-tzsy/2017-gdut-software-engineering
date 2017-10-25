<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/common/dqdp_common_dgu.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>"/>
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
</head>
</head>
<body>
<script type="text/javascript">
    $(function () {
        var url = decodeURI("${param.tarurl}");
        showLoading("跳转中...");
        $.ajax({
            url: "${baseURL}/portal/qwuser/portalQwuserAction!adduserinfo.action",
            type: "post",
            data: {"url": url},
            async: false,
            dataType: "json",
            success: function (result) {
                if (result.code != "0") {
                    hideLoading();
                    _alert('提示', result.desc, '确认', function () {
                        WeixinJS.back();
                    });
                    return;
                }
                var code = result.data.CODE;
                var str_url = "http://" + decodeURI("${param.tarurl}");
                if (str_url.indexOf("?") == -1) {
                    window.location.href = str_url + "?code=" + code;
                } else {
                    window.location.href = str_url + "&code=" + code;
                }
            }
        })
    });
</script>
<%@include file="/jsp/wap/include/showMsg.jsp" %>
</body>
</html>
