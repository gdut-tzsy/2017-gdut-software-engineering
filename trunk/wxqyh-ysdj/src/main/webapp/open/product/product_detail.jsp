<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    
    <head>
    <meta name="HandheldFriendly" content="True"/>
    <meta name="MobileOptimized" content="320"/>
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="canshare" id="canshareId" content="1"/>
	<title></title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="description" id="descriptionId" content=""/>
	<meta name="shareImgUrl" id="shareImgUrlId" content=""/>
	<meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>

	<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	
	<script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
	
	<!-- 上传媒体文件（手机端页面）引入  start -->
    <script src="${resourceURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
	<!-- 上传媒体文件（手机端页面）引入  end -->
    </head>
    <style>
    	/*放在本页*/
		@media screen and (min-width: 800px){
		    .article_detail {
				background: #ffffff;
				width: 600px;
				margin: 50px auto 0 auto;
				border: 1px solid #dfdfdf;
				padding: 50px 40px!important;
			}
			.article-detail{
				padding: 0;
			}
		}
    </style>
    
    <body>
        <%@include file="/open/include/showMsg.jsp" %>
        <div id="wrap_main_product" class="wrap" style="display:none;">
            <div id="main" class="wrap_inner">
            	<div class="article_detail">
					<div class="article-detail">
						<div class="detail-title" id="title"></div>
						<div class="detail-small-title">
							<span id="publisherTime"></span>
							<span id="publisher"></span>
						</div>
						<div class="detail-content detail_body article_content">
							<div id="content"></div>
							<div id="imagesDiv"></div>
						</div>
						<div id="yanshi_id" class="fz14 c666 mb15" style="display:none;">
							<input type="hidden" id="productUrl" name="productUrl"/>
							<span>演示链接：</span>
							<span id="yanshiUrl"></span>
						</div>
						<div class="detail-type-bar">
							<i class="icon-tips mr10"></i><span id="type_name" class="mr10"></span>
							<span id="openViewCounts"></span>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  start -->
						<div class="form-style mb20" id="medialist" style="display: none;">
							<div class="letter_bar file_top borderBottommNone"><span class="file_top_tit">附件(0)</span>
							</div>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  end -->
						<div class="mt20 mb10" style="display:none;" id="btn_caseDiv">
							<a class="btn flexItem" href="javascript:productCaseList()">点击查看知识百科案例</a>
						</div>
					</div>
                	<div class="comments-box" id="comments-box" style="display:none">
                        <div class="letter_bar first_top" id="commentCount">回复(0)</div>
                        <div class="comment_list clearfix" id="comments" >
                            <ul id="commentList"></ul>
                        </div>
                    </div>
            	</div>
				<div class="all_pull"  id="getmore_comment" style="display:none">
                    <p id="hasMore" class="lastComment" onclick="javascript:listMoreReply()">点击查看更多回复</p>
               </div>
               
	       <%@include file="/open/include/footer.jsp"%>
        </div>
            
       </div>
       
       <!--分享到外部覆盖层 -->
       <div class="share_bg"></div>
	    <div class="share_box">
	        <div class="share1"></div>
	        <div class="share2"></div>
	    </div>
        <script type="text/javascript">
        
        //分享外部样式
        document.documentElement.addEventListener('touchend',function(){
	    	$('.share_bg,.share_box').hide();
		});
        $(".share_bg,.share_box").on("click",function(){
    		$('.share_bg,.share_box').hide();
    	})
        
        //跳转到知识百科案例列表
        function productCaseList(){
        	var productId="${param.id}";
        	document.location.href="productcase_list.jsp?productId="+productId;
        }
        
	        $(function(){
	        	
	        	var isOpentype='${param.type}';
	        	/*if("1"==isOpentype){
	        		$('.share_bg,.share_box').show();
	        	}*/
	        	
	        	/**上传媒体文件引入  start*/
	        	//根据所属功能，参数设置为：activity、dynamic、topic、addressBook、task、outsidework、meetingassistant、survey、productinfo、crm、form、diary、ask、moveapprove
	            wxqyh_uploadfile.agent="productinfo";//应用code
	            wxqyh_uploadfile.init();
	        	/**上传媒体文件引入  end*/
	        });
        
        	var lock=true;//动态上锁
        	
            $(function() {
                initData(); 
            });
            
        	
            var pageSize=10;
            var currPage=1;
            
            function initData() {
            	showLoading();
                $.ajax({
                    type:"POST",
                    url: "${baseURL}/open/product/productAction!ajaxView.action",
                    data: "id=${param.id}&size="+pageSize+"&currPage="+currPage,
                    cache:false,
                    dataType: "json",
                    success: function(data){
                        if ("0" != data.code) {
                        	hideLoading();
                            showMsg("", data.desc, 1,{ok:function(result){WeixinJS.back();}});
                            return;
                        }
                        var info = data.data;
                        
                        if(info.isShowFooter==true){
	                    	showFooter(true);
	                    }
                        
                        $("title").html(info.tbXyhProductPO.title);
        				//分享信息
        				var title=info.tbXyhProductPO.title;
        				var img=compressURL+info.tbXyhProductPO.coverImage;
        				if(info.tbXyhProductPO.coverImage==""){
        					img = info.logo;
        				}
        				var summary=info.tbXyhProductPO.summary;
        				var shareUrl=window.location.href;
        				if(shareUrl.indexOf("&")>0){
        					var str=shareUrl.split("&");
        					shareUrl=str[0];
        				}
        				document.title = title;
						setDataForWeixinValue(title,img,summary,shareUrl);
        				
        				//百科详细
                        var title = info.tbXyhProductPO.title.replaceAll("\r\n","<br/>");
                        title = title.replaceAll(/\s/g,"&nbsp;");
                        $("#title").html(title);
                        $("title").html(title);//修改页面标题
                       /* $("#summary").html(info.tbXyhProductPO.summary);*/

                        //$("#publisher").html(info.tbXyhProductPO.publisher);
                        $("#publisher").html(info.orgName);

                        $("#publisherTime").html(info.publishTime);
                        //$("#viewcount").html("<i class=\"fa fa-eye\">"+info.tbXyhProductPO.viewCount+"</i>");

            			var content = info.tbXyhProductPO.content.replace(/http\:\/\/qy\.do1\.com\.cn\:6090\/fileweb/g,localport).replace(/@fileweb@/g,localport);

                        $("#content").html(checkURL(content,false));
                        /* $("#content").find("img").each(function(){
                        	this.style.width="100%";
                        }); */
                      	if(info.tbXyhProductPO.productUrl!=""){
                      		$("#yanshi_id").show();
                      	}
                      	$("#yanshiUrl").html(checkURL(info.tbXyhProductPO.productUrl,false));

                        $("#id").val(info.tbXyhProductPO.productId);

/*                         var caseListDiv = $("#caseList");
                        appendProductCase(caseListDiv, caseList);
                        hasMoreCase = info.hasMoreCase;
                        if (!hasMoreCase) {
                    		$("#hasMoreCase").html("<p class='lastComment'>没有更多案例啦</p>");
            			}  */
                        
                        //图片
                        var imagesList = info.imagesList;
                        if (imagesList != null) {
                        	var html = "";
                        	for (var i = 0; i < imagesList.length; i ++) {
                        		html += "<div class='item'>"
                        		       + "<img width='100%' src='${compressURL}" +imagesList[i].path + "' alt=''/>"
                        		       + "</div>";
                        	}
                            $("#imagesDiv").append(html); 
                        }
                        
                        //显示封面图片 huanggaodeng 
                   		if(info.tbXyhProductPO.isShowCoverIntext=="1"){
                            var coverImage = info.tbXyhProductPO.coverImage;
                            if(coverImage != null && coverImage!=""){
                            	var html = "<div class='item'>"
                     		       + "<img width='100%' style='margin-top:5px;' src='${compressURL}" +coverImage + "' alt=''/>"
                    		       + "</div>";
                            	 $("#imagesDiv").append(html);
                            }
                   		}
                        
                        var typeName = info.typeName;
                        $('#type_name').html(typeName);

                        //附件 
                        var mediaInfo = info.mediaList;
                        if(mediaInfo.length > 0){
                        	$('#medialist').show();
                        	previewFiles(mediaInfo,"medialist","mediaIds",1);                          	
                        }
            			
            			//$("#wrap_main").css("display","block");
            			$("#wrap_main_product").show();
            			
            			$("#openViewCounts").html("已阅("+info.openViewCounts+")");

            			//显示评论
            			if("1"==info.tbXyhProductPO.shareComment){
            				$("#comments-box").show();
        	            	$("#getmore_comment").show();
        	            	$(".foot_bar").show();
        	            	$(".footheight").show();
            				
            				var comments = info.comments;
                            var commentsDiv = $("#comments");
                            appendCommnets(commentsDiv, comments);
                             
                            hasMore = info.hasMore;
                            if (!hasMore) {
                        		$("#hasMore").html("<span style=\"color: #999999;\">已没有更多</span>");
                             	document.getElementById("hasMore").style.cursor="default";
                        		$("#hasMore").attr("onclick","javascript:void(0)");
            				}else{
            					document.getElementById("hasMore").style.cursor="pointer";
            				}
            			}

						if(info.tbXyhProductPO.isWatermark=="1"){
							secretName=info.orgName;
							shuiying();
						}
						hideLoading();
                    },
            		error : function() {
            			hideLoading();
            		}
                });  
                //如果是ios客户端，需要刷新一下底部按钮的高度，防止点击输入框后低部输入框上漂
                //android不可以刷新，刷新会出现三星手机的该页面无法滚动的问题
                if (/ipad|iphone|mac/i.test(navigator.userAgent)){
            		setTimeout(function(){
            	    	$(".wrapHeight").height($(window).height());
            		},500);
                }
            }

            function productDetail() {
            	var productUrl = $("#productUrl").val();
            	window.location.href = productUrl;
            }

            function listMoreCase() {
            	var listCase = $("#caseList").find("input");
            	var lastCaseId = $(listCase[listCase.length-1]).val();
                $.ajax({
                    type:"POST",
                    url: "${baseURL}/open/product/productAction!searchMoreProductCase.action",
                    data: "lastCaseId=" + lastCaseId,
                    async: false,
                    cache:false,
                    dataType: "json",
                    success: function(data){
                    	lock_roll=false;
                       if ("0" != data.code) {
                           showMsg("", data.desc, 1);
                           return;
                       }
                       
                        var caseList = data.data.caseList;
                        var caseListDiv = $("#caseList");
                    }
                });

            }

            var hasMore; //是否还有评论数据
            var lock_roll=false;
            $(function() {
                 // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
                 var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
                 var nScrollTop = 0; //滚动到的当前位置
                 var $frame = $("#main");
                 $frame.on("scroll", function() {
                     var nDivHight = $frame.height();
                     nScrollHight = $(this)[0].scrollHeight;
                     nScrollTop = $(this)[0].scrollTop;
                     if (nScrollTop + nDivHight >= nScrollHight) {
                         // 触发事件，这里可以用AJAX拉取下页的数数据
                         if(!lock_roll){
                        	 lock_roll=true;
                        	 if (hasMore) {
                           		 listMoreReply();
                        	 }
            			}
                     };
                 });
             });
            
            
            function listMoreReply() {
            	currPage=currPage+1;
            	var list = $("#commentList").find("input");
            	var lastCommentId = $(list[list.length-1]).val();
                //var lastCommentId = $("#commentList input:last").val();
                $.ajax({
                    type:"POST",
                    url: "${baseURL}/open/product/productAction!getProductCommentPager.action",
                    data: "id=${param.id}&size="+pageSize+"&currPage="+currPage,
                    async: false,
                    cache:false,
                    dataType: "json",
                    success: function(data){
                       if ("0" != data.code) {
                           //showMsg("", data.desc, 1);
                           return;
                       }
                       lock_roll=false;
                    
                        var comments = data.data.comments;
                        var commentsDiv = $("#comments");
                        appendCommnets(commentsDiv, comments);
                        
                        var hasMore = data.data.hasMore;
                        if (!hasMore) {
                    		$("#hasMore").html("<span style=\"color: #999999;\">已没有更多</span>");
                         	document.getElementById("hasMore").style.cursor="default";
                    		$("#hasMore").attr("onclick","javascript:void(0)");
        				}else{
        					document.getElementById("hasMore").style.cursor="pointer";
        				}
                        emoCommentId_contentjify.run($('.comment_list')[0]);
                    }
                });
            }
            
          //评论模板
            var temple = ''
            	+' <li class="flexbox"> '
            	+' 	<div class="avator" onclick="atThisPersonUtil(@UserId,@Name,@headPic)"> '
            	+' 	    <img src="@HeadPic" alt="" onerror="javascript:replaceUserHeadImage(this);"> '
            	+' 	</div> '
            	+' 	<div class="comment_content flexItem"> '
            	+' 	<h3 class="clearfix"> '
            	+' 	    <span class="title">@PersonName</span> '
            	+' 	    <span class="time">@Time</span> '
            	+' 	    <input type="hidden" value="@CommentId" /> '
            	+' 	</h3> '
            	+' 	<p id="@CommentId_content" class="@contentclass" >@Content</p> '
            	//+'  <p id="@CommentId_del" class="comment_action" style="@display"><a href="javascript:deleteComment(@CommentId,this)">删除</a></p> '
            	+' 	</div> '
            	+' </li> '; 
            	
            function appendCommnets(commentsDiv, comments) {
               for (var i = 0; i < comments.length; i++) {
                    var item = temple;
                    item = item.replace("@HeadPic",comments[i].headPic);
        			item = item.replace("@PersonName",comments[i].personName);
        			item = item.replace("@Time",comments[i].time);
        			//item = item.replace("@Content",comments[i].content.replace(/\s/g,"&nbsp;"));
        			//item = item.replace("@Content",comments[i].content.replace(reg_url, '<a href="$1$2">$1$2</a>'));
        			item = item.replace("@CommentId",comments[i].commentId);
        			item = item.replace("@CommentId",comments[i].commentId);//内容p
        			//item = item.replace("@CommentId",comments[i].commentId);//删除p
        			item = item.replaceAll("@UserId","'"+comments[i].userId+"'");
        			item = item.replace("@Name","'"+comments[i].personName+"'");
        			item = item.replace("@headPic","'"+comments[i].headPic+"'");
        			
        			if(comments[i].type=='2'){
        				item = item.replace("@contentclass","commentImg");
        				if(comments[i].content.indexOf('已被管理员')==0){
        					item = item.replace("@Content",comments[i].content);
        				}else{
        					item = item.replace("@Content","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
        				}
        			}else{
        				item = item.replace("@Content",comments[i].content.replace(reg_url, '<a href="$1$2">$1$2</a>'));
        			}
        			if("-1"==comments[i].commentStatus){
        				item = item.replace("@display","display:none");
        			}else{
        				item = item.replace("@display","display:block");
        			}
        			item = item.replace("@CommentId","'"+comments[i].commentId+"'");
                    $("#commentList").append(item);
                }
               emojify.run($('.comment_list')[0]);
            }
            
        </script>
    </body>

</html>