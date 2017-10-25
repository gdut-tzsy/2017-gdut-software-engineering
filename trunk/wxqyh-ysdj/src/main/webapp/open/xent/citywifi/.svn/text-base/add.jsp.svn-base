<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>企微云平台丨城市热点</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection">
        <meta content="email=no" name="format-detection">
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/selfService.css"/>
        <link rel="stylesheet" href="${baseURL}/xentthemes/citywifi/wap/css/base.css">
        <script src="${baseURL}/xentthemes/citywifi/wap/js/jquery-1.7.2.min.js"></script>
        <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
        <script src='${baseURL}/jsp/wap/js/wechat.js'></script>
    </head>
    <style>
       .btn{
            background: #ff9600 none repeat scroll 0 0;
            border: 1px solid #ee8c00;
            border-radius: 5px;
            color: #fff;
            font-size: 0.9rem;
            height: 2.25rem;
            line-height: 2.25rem;
            text-align: center;
            width: 13rem;
       }
    </style>
    <body class="login-bg">
        <div class="wrap">
            <h1 class="title">关注应用</h1>
            <div class="step">
                <img src="${baseURL}/xentthemes/citywifi/wap/images/selfsv05.png" alt="" class="" />
                <div class="clearfix">
                    <div class="fl orange">提交资料</div>
                    <div class="fl">扫码关注</div>
                </div>
            </div>
            <div class="register-item bg-eee">
                <span class="">账号</span>
                <input type="hidden" id="portalid" value="${param.portalid }"/>
                <input type="text" class="" readonly="readonly" id="account" value="${param.account }"/>
            </div>
            <div class="register-item border-orange">
                <span class="">手机<i class="">*</i></span>
                <input type=" number" class="" placeholder="请输入" id="mobile"/>
            </div>
            <div class="register-item">
                <span class="">姓名</span>
                <input type="text" class="" id="name"/>
            </div>
            <div class="btn-div ma" style="width:13rem; margin-top: 1.5rem;"><input type="button" onclick="submit()" class="btn" value="下一步" /></div>
            <div class="footer relative">
                <i class=""></i>
                <a href="" class="">企微云平台</a>&<a href="" class="">城市热点</a>联合提供
            </div>
        </div>
	<%@include file="/jsp/wap/include/showMsg.jsp"%>
    </body>
    
    <script type="text/javascript">
        $(document).ready(function(){
    	    showLoading();
    	    setTimeout(hideLoading, 1000);
        })

    	function submit(){
    	    showLoading();
    		//防止按钮连续点击
    		$(".btn").attr("disabled", "disabled");
    		$(".btn").css("pointer-events","none");
    		var n = $("#name").val();
    		var a = $("#account").val();
    		var m = $("#mobile").val();
    		var p = $("#portalid").val();
    		if(m==""){
    			enableSubmitButton();
    			_alert("未填写手机号码！","请填写后提交","确认");
    			return;
    		}
    		if(!(/^1[3|4|5|7|8]\d{9}$/.test(m))){
    			enableSubmitButton();
    			_alert("手机号码格式错误！","请填写正确的手机号码","确认");
    			return;
    		}
    		if(n.length > 20){
    			enableSubmitButton();
    			_alert("姓名长度超出限制！","姓名不能超过20字符","确认");
    			return;
    		}
    		$.ajax({
    			url:"${baseURL}/open/wifi/wifiAction!checkSubmitInfo.action",
    			type:"post",
    			data:{
    				"name":n,
    				"account":a,
    				"mobile":m,
    				"portalid":p
    			},
    			dataType:"json",
    			success:function(result){
    				if(result.code=='0'){
    					var val = result.data;
    					if(val.type == '0'){
    						//跳转到关注成功页面
    						window.location.href="${baseURL}/open/xent/citywifi/success.jsp";
    					}else{
    						//跳转到扫码关注页面
    						window.location.href="${baseURL}/open/xent/citywifi/Qr_code.jsp?portalid="+p;
    					}
    				}else{
    			        enableSubmitButton();
                        _alert("",result.desc,"确认");
    				}
    			}
    		})
    	}
    	
    	function enableSubmitButton(){
            hideLoading();
    		$(".btn").removeAttr("disabled");
    		$(".btn").css("pointer-events","auto");
    	}
    </script>
</html>