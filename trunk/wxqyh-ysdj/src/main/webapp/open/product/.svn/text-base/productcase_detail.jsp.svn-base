<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
    <head>
        <title>案例详情</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
	
		<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    </head> 
    <body>
        <div id="wrap_main" class="wrap">
            <div id="main" class="wrap_inner">
                <div class="article_detail">
                    <div class="article_content">
                    	<div style="text-align:center;font-weight: bold;">
			                <span id="title"></span>
			            </div>
			            <div id="caseUrl_divId" style="color:grey;font-size: 12px;display:none;">
							<span>案例链接：</span>
							<span style="margin-left: 5px;"></span>
							<span id="caseUrl"></span>
						</div>
						<div>
			                <p id="topImage">
			                    <!-- <img id="coverImage" src=""  width="100%"/> -->
			                </p>
			                <p id="content"></p>
			            </div>                       
                    </div>
					<!-- 上传媒体文件（手机端页面）引入  start -->
					<div class="form-style" id="medialist" style="display:none;">
						<div class="letter_bar file_top"><span class="file_top_tit">附件(0)</span></div>
					</div>
					<!-- 上传媒体文件（手机端页面）引入  end -->
                </div>
            </div>
        </div>
		<!-- 需要在此，引入 以下两个jsp，特别注意必须在<div id="main" class="wrap_inner">下加入以下include-->
        <%@include file="/open/include/showMsg.jsp" %>
    </body>

</html>
<script type="text/javascript">

    $(document).ready(function () {
    	showLoading();
         
    	init();
    	
    	
        //如果是ios客户端，需要刷新一下底部按钮的高度，防止点击输入框后低部输入框上漂
        //android不可以刷新，刷新会出现三星手机的该页面无法滚动的问题
        if (/ipad|iphone|mac/i.test(navigator.userAgent)){
    		setTimeout(function(){
    	    	$(".wrapHeight").height($(window).height());
    		},500);
        }
        /* $("input").on('focus', function(){
        	$(".wrapHeight").height($(window).height());
    	}); */

		/**上传媒体文件引入  start*/
	    wxqyh_uploadfile.agent="productinfo";//应用code
	    wxqyh_uploadfile.groupId="${param.id}";
		/**上传媒体文件引入  end*/
    });
    
    function init(){
    	$.ajax({
            type:"POST",
            url: "${baseURL}/open/product/productAction!getProductProductcase.action",
            data: "id=${param.id}",
            cache:false,
            dataType: "json",
            success: function(data){
                if ("0" != data.code) {
                	hideLoading();
                    showMsg("",data.desc,1,{ok:function(result){WeixinJS.back();}});
                    return;
                }
                var info = data.data;  
                
                //标题
                $("#title").html(info.tbXyhProductCasePO.title);
                //案例链接
                if(""!=info.tbXyhProductCasePO.caseUrl){
                	 $("#caseUrl_divId").show();
                	 $("#caseUrl").html(checkURL(info.tbXyhProductCasePO.caseUrl,false));
                }
                //案例图片
                if(info.tbXyhProductCasePO.caseImg!=""){
	                $("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhProductCasePO.caseImg 
                			+ "\" width=\"100%\" />");  	
                }
                //附件
				if(info.mediaList.length>0){
					$("#medialist").show();
					previewFiles(info.mediaList,"medialist","mediaIds");
				}
				
                hideLoading();
            },
            error : function() {
				hideLoading();
				showMsg("","网络打了个盹，请重试",1,{ok:function(result){WeixinJS.back();}});
			}
        }); 
    }
    
</script>