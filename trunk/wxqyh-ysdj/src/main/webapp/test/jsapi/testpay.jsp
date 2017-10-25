<%--
  Created by IntelliJ IDEA.
  User: eimserver
  Date: 2015/6/9
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<%@include file="../../jsp/common/dqdp_common.jsp" %>
<head>
    <title></title>
    <script src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
    <script src="${baseURL}/js/do1/common/common.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <link href="${baseURL}/js/3rd-plug/jquery-ui-1.8/css/smoothness/jquery-ui-1.8.custom.css" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-ui-1.8.custom.min.js"></script>

</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body>
<div>
    <input type="button" value="发起支付" onclick="doPrePay()">
    <br/>
    <br/>
    <input type="button" value="发红包" onclick="doRedPack()">
</div>
</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    function doRedPack(){
        $.ajax({
            'type': "post",
            'url': baseURL + '/wechatpay/wechatpayAction!sendRedPack.action',
            'data': {
                'packParam.accountId': 'd260be6c-a9de-4b05-bb5e-690c353bade8',
                'packParam.mch_billno': 'test_billno',
                'packParam.re_openid': 'oLw-Ft_oetAd5OS3sUqr4_p76UCU',
                'packParam.total_amount':100,
                'packParam.wishing': '祝好',
                'packParam.act_name': 'test_actname',
                'packParam.remark': 'remark',
                'packParam.nick_name': 'n2',
                'packParam.send_name': 's2'
            },
            'dataType': 'json',
            success: function (obj) {
                alert(obj.code + "|" + obj.desc);

            },
            fail: function (obj) {
                alert("请求过程中出现异常，未能成功完成请求");
            }

        });
    }
    function doPrePay() {
        $.ajax({
            'type': "post",
            'url': baseURL + '/wechatpay/wechatpayAction!prePayJS.action',
            'data': {
                'payParam.accountId': 'd260be6c-a9de-4b05-bb5e-690c353bade8',
                'payParam.amount': 10,
                'payParam.sign': 'xxx',
                'payParam.time': 'xxx',
                'payParam.productDesc': 'abc',
                'payParam.remark': 'ddd',
                'payParam.openId': 'oLw-Ft_oetAd5OS3sUqr4_p76UCU'
            },
            'dataType': 'json',
            success: function (obj) {
                //alert(obj.code + "|" + obj.desc);
                onBridgeReady(obj.data.payParam);
            },
            fail: function (obj) {
                alert("请求过程中出现异常，未能成功完成请求");
            }

        });
    }
    function onBridgeReady(param) {
    	alert(param.paySign);
    	wx.chooseWXPay({
    		appId:param.appId,
    	    timestamp: param.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
    	    nonceStr: param.nonceStr, // 支付签名随机串，不长于 32 位
    	    package: param.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
    	    signType: param.signType,// 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
    	    paySign: param.paySign, // 支付签名
    	    success: function (res) {
    	    	alert(res.err_msg);
    	    }
    	});
        
    }
    
</script>
