<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>提交成功</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" id="meta" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
         <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
        <meta name="description" id="descriptionId" content="">
        <meta name="shareImgUrl" id="shareImgUrlId" content="">
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
		<script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
		<script type="text/javascript" src="${resourceURL}/jsp/wap/js/zepto.min.js"></script>
	    
    </head>
    <script type="text/javascript">
        (function(){
             var w=document.documentElement.clientWidth;
             var h=document.documentElement.clientHeight;
             var scale=h>505?1:h/505;
              document.querySelector('#meta').content="width=320, initial-scale="+scale+", minimum-scale="+scale+", maximum-scale="+scale+", user-scalable=no"
             }())
        $(window).on('resize load',function(){
            var h=document.documentElement.clientHeight;
            var w=document.documentElement.clientWidth;
            if(h>605&&w>=800){
                $('.yaoqing,.wrap').height(h-100)
            }else{
                $('.yaoqing,.wrap').height(h)
            }
        })
     </script>
     <style type="text/css">
			@media screen and (min-height: 605px) and (min-width: 800px){
	        .yaoqing {background: #ffffff; margin: 0 auto;border: 1px solid #dfdfdf;min-height: 505px;}
	        .wrap{padding:50px 0}
	        }
		}
    </style>
	<body>
		<div id="wrap_main" class="wrap">
             <div class="submitSucceed yaoqing" id="yaoqing">
                 <div class="head"></div>
                 <div class="wx"><img id="imgUrl" src="../images/wx.png" width="158px" /></div>
                 <div class="s">提交成功，请扫码关注</div>
                 <div class="text">或长按二维码识别，快速加入该企业微信</div>
                <%-- <div class="foot">
                 	<div class="footcon">
                 		<img src="${resourceURL}/jsp/wap/images/smallLogo.png" width="18px" />企微云平台 提供技术支持
                		</div>
            		</div>--%>
				 <%@include file="/open/include/footer.jsp"%>
             </div>
     	</div>
	</body>
	<script type="text/javascript">
		 $(function(){
			$.ajax({
				url:"${baseURL}/open/memberAction!getMemberSet.action",
				type:"POST",
				data:{id:'${param.id}'},
				dataType:"json",
				success:function(result){
					if(result.code=="1999" || result.code!="0"){
						hideLoading();
						showMsg("",result.desc,1,{ok:function(result){WeixinJS.back();}});
						return;
					}
					var imgUrl=result.data.wxqrcode;
					$("#imgUrl").attr("src",imgUrl);
					var config=result.data.config;
					if(config){
						$(".head").html(config.orgName);
					}
				},
				error:function(){
					hideLoading();
					showMsg("",internetErrorMsg,1);
				}
			});
		 });
	</script>
</html>