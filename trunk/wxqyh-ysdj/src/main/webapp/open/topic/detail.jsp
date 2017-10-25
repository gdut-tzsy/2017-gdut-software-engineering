<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>

<head>
	
	<title>话题详情</title>
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
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
	<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	<!-- 上传媒体文件（手机端页面）引入  start -->
	<script src="${resourceURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
	<!-- 上传媒体文件（手机端页面）引入  end -->	
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
</head>


<body>
	<div id="wrap_main" class="wrap">
         <div id="main" class="wrap_inner">
             <div class="article_detail">
				 <div class="article-detail">
					 <div class="detail-title" id="title"></div>
					 <div class="detail-small-title flexbox">
						 <span id="publisherTime" class="mr10"></span>
						 <span id="publisher"></span>
					 </div>
					 <div class="detail-content article_content detail_body" id="imageDiv"></div>
					 <div class="lastEdit mb15" id="lastEditId" style="display:none">本话题最后由 <span class="name" id="lastEditor">某某某</span> 于<span class="time" id="lastEditTime">2015-11-12 00:55</span> 编辑
					 </div>
					 <div class="detail-type-bar">
						 <i class="icon-tips mr5"></i>
						 <span class="text" id="typeId"></span>
					 </div>
					 <!-- 上传媒体文件（手机端页面）引入  start -->
					 <div class="form-style mb20" id="medialist" style="display:none;">
						 <div class="letter_bar file_top borderBottommNone"><span class="file_top_tit">附件(0)</span></div>
					 </div>
					 <!-- 上传媒体文件（手机端页面）引入  end -->
				 </div>
				<div class="comments-box" id="getmore_comment">
					<div class="letter_bar first_top">回复(<span id="commentCount">0</span>)</div>
					 <div class="comment_list clearfix"  id="commentList">
						 <ul id="comments">
						 </ul>
					 </div>
				</div>
			 </div>
			 <%@include file="/open/include/footer.jsp"%>
		 </div>
  	<div class="share_bg"></div>
   <div class="share_box">
       <div class="share1"></div>
       <div class="share2"></div>
   </div>
	</div>
	    <!-- 需要在此，引入 以下两个jsp，特别注意必须在<div id="main" class="wrap_inner">下加入以下include-->
		<%@include file="/open/include/showMsg.jsp"%>
</body>
<script type="text/javascript">

var topicId;
var pageIndex=1; 

	document.documentElement.addEventListener('touchend',function(){
	    $('.share_bg,.share_box').hide();
	})
	$(".share_bg,.share_box").on("click",function(){
		$('.share_bg,.share_box').hide();
	})
	
 $(document).ready(function () {
	 var isOpentype='${param.type}';
 	/*if("1"==isOpentype){
 		$('.share_bg,.share_box').show();
 	}*/
    wxqyh_uploadfile.agent="topic";//应用code
    wxqyh_uploadfile.groupId="${param.id}";
    
	showLoading();
    $.ajax({
        type:"POST",
        url: "${baseURL}/open/topic/topicAction!ajaxView.action",
        data: "id=${param.id}",
        cache:false,
        dataType: "json",
        success: function(data){

            if ("0" != data.code) {
            	hideLoading();
                showMsg("", data.desc, 1,{ok:function(result){WeixinJS.back();}});
               
            }

            var info = data.data;
            
            if(info.isShowFooter==true){
            	showFooter(true);
            }
            
            $("title").html(info.tbXyhTopicPO.title);
			//分享信息
			var title=info.tbXyhTopicPO.title;
			var img="${compressURL}"+info.tbXyhTopicPO.coverImage;
			if(info.tbXyhTopicPO.coverImage==""){
				img = info.logo;
			}
			var summary=info.tbXyhTopicPO.summary;
			var shareUrl=window.location.href;
			if(shareUrl.indexOf("&")>0){
				var str=shareUrl.split("&");
				shareUrl=str[0];
			}
			//alert(summary);
			setDataForWeixinValue(title,img,summary,shareUrl);
            var coverImage = info.tbXyhTopicPO.coverImage; 
            var title = info.tbXyhTopicPO.title.replaceAll("\r\n","");
            title = title.replaceAll(/\s/g,"&nbsp;");
            title = trimLR(title);
            $("#title").html(title);
            var typeName=info.tbXyhTopicPO.typeName;
            if(typeName==""||typeName=='null'||typeName==null){
            	$("#typeId").html('未选择');
            }else{
            	$("#typeId").html(typeName);
            }
            var content = info.tbXyhTopicPO.content;
            if(content!=null &&content!=""){
				content=content.replace(/@fileweb@/g,localport);
			}
            if(info.tbXyhTopicPO.lastEditor==''){
    			content=checkURL(content,true);
			}else{
				content=checkURL(content,false);
			}
			if (coverImage == "" || coverImage == null) {
				$("#imageDiv").append("<p id=content>" +content +"</p>");
			} else {
				var imgnum;
				var imgindex=0;
				var imgarray = info.imagesList.slice(0);
				$("#imageDiv").append("<p id=content></p>");
				$("#imageDiv").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhTopicPO.coverImage
						+ "\" width=\"100%\" />");
				var content_str = content;
				checkIMG();
				function checkIMG(){
					if(content_str.indexOf("[IMG")!=-1){
						var index = content_str.indexOf("[IMG");
						var img_pd = true;
						if(content_str.substr(index+4,1)=="]"){
							imgindex++;
							imgnum = imgindex;
							img_pd = false;
						}else{
							imgnum = content_str.substr(index+4,1);
						}
						var imgstr="";
						if(imgnum==1){
							imgstr='<img src="${compressURL}'+info.tbXyhTopicPO.coverImage+'">'
							$("#imageDiv img").remove();
						}else{
							imgstr='<img src="${compressURL}'+info.imagesList[imgnum-2].path+'">'
							for(var i = 0;i<imgarray.length;i++){
								if(imgarray[i].path==info.imagesList[imgnum-2].path){
									imgarray.splice(i,1);
									break;
								}
							}
						}
						var bz_str;
						if(img_pd){
							bz_str = "[IMG"+imgnum+"]";
						}else{
							bz_str = "[IMG]";
						}
						content_str = content_str.replace(bz_str,imgstr);
						checkIMG();
					}
				}
				$("#content").html(content_str);
				var imagesList = info.imagesList;
				if(imgnum){
					imagesList = imgarray;
				}
				if (imagesList != null && imagesList.length > 0) {
					for (var i = 0; i < imagesList.length; i ++) {
						$("#imageDiv").append("<img id=\"coverImage" + i + "\" src=\"${compressURL}" + imagesList[i].path
								+ "\" width=\"100%\" />");
					}
				}
			}
            
            $("#publisher").html(info.tbXyhTopicPO.userName);
            $("#publisherTime").html(info.tbXyhTopicPO.createTime);
            $("#viewcount").html("<i class=\"fa fa-eye\">"+info.tbXyhTopicPO.viewCount+"</i>");
            if(info.isUpdate){
            	$("#lastEditId").show();
                $("#lastEditTime").html(info.tbXyhTopicPO.lastEditTime);
                if(info.tbXyhTopicPO.editorName){
                	$("#lastEditor").html(info.tbXyhTopicPO.editorName);
                }else{
                	$("#lastEditor").html(info.tbXyhTopicPO.userName);
                }
            }
            
            $("#id").val(info.tbXyhTopicPO.topicId);
            if(info.mediaList.length>0){
				$("#medialist").show();
				previewFiles(info.mediaList,"medialist","mediaIds");
			}
            
            if(info.tbXyhTopicPO.shareComment=='1'){
            	var commentCount = "0";
                if(info.tbXyhTopicPO.commentCount>0){
                	$("#getmore_comment").show(); 
                	commentCount = info.tbXyhTopicPO.commentCount;
                }else{
                	$("#getmore_comment").hide();
                }
                $("#commentCount").html(commentCount);
            	var comments = info.comments;
                var commentsDiv = $("#comments");
                appendCommnets(commentsDiv, comments,"1");
                hasMore = info.hasMore;
                if(hasMore){
                	$('.lastComment').html('向上滑加载更多');
                }else{
                	$('.lastComment').html('没有更多评论啦');
                }
				var h =$('#main').height() - ($('.article_detail').height()+76);
				if(h>0){
					$('.article_detail').after('<div style="height:'+h+'px;"></div>')
				}
            }else{
            	hasMore=false;
            	$("#getmore_comment").hide();
            }
            
            topicId = info.tbXyhTopicPO.topicId;
			hideLoading();
			if("${param.src}"!=""){
				$("#down_comment").click();
			}
        }
    });  
  //如果是ios客户端，需要刷新一下底部按钮的高度，防止点击输入框后低部输入框上漂
    //android不可以刷新，刷新会出现三星手机的该页面无法滚动的问题
    if (/ipad|iphone|mac/i.test(navigator.userAgent)){
		setTimeout(function(){
	    	$(".wrapHeight").height($(window).height());
		},500);
    }
}); 

var hasMore; //是否还有评论数据
var lock_roll=false;
$(function() {
	agentCode = "topic";
     // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
     var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
     var nScrollTop = 0; //滚动到的当前位置
     var $frame = $("#main");
     $frame.on("scroll touchmove", function() {
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
	      }
     });
 });
function listMoreReply() {
    pageIndex++;
    $.ajax({
        type:"POST",
        url: "${baseURL}/open/topic/topicAction!listMoreComment.action",
        data : "id=${param.id}&size=10&page="+pageIndex,
        async: false,
        cache:false,
        dataType: "json",
        success: function(data){
        	lock_roll=false;
           if ("0" != data.code) {
               //showMsg("", data.desc, 1);
               return;
           }
        
            var comments = data.data.comments;
            var commentsDiv = $("#comments");
            appendCommnets(commentsDiv, comments,"1");
            hasMore = data.data.hasMore;
            if(hasMore){
            	$('.lastComment').html('向上滑加载更多');
            }else{
            	$('.lastComment').html('没有更多评论啦');
            }
            emojify.run($('.comment_list')[0]);
        }
    });
}
function appendCommnets(commentsDiv, comments,type) {
	   for (var i = 0; i < comments.length; i++) {
		   var headPic;
		   var item ;
		   if(comments[i].personName=='匿名'){
			   headPic="../images/anonymous.png";
		   }else{
			   headPic=comments[i].headPic;
		   }
		   item = ''
	        	+'<li class="flexbox"> '
	        	+'<div class="avator" > '
	        	+'	<img src="' + headPic + "" + '" alt="" onerror="javascript:replaceUserHeadImage(this);" />'
	        	+'</div>'
	        	+'<div class="comment_content flexItem">'
	        	+'	<h3 class="clearfix">'
	        	+'		<span class="title">' + comments[i].personName + '</span> <span class="time">' + comments[i].createTime + '</span> '
	        	+'      <input type="hidden" value="' + comments[i].commentId  + '" />'
	        	+'	</h3>'
	        	+'	<p class="@contentclass">@Content</p><div id="@circle" class="abc" style="@circledisplay">'
	    	 	+'  <div id="circle_1" class="circleItem"></div><div id="circle_2" class="circleItem"></div>'
	    	    +'  <div id="circle_3" class="circleItem"></div>'
	    	    +'  </div> '
	        	+'  <input type="hidden" value="@userids">'
	        	+'  <input type="hidden" value="@type">'
	        	+'  <input type="hidden" value="@content">'
	        	+'  <input type="hidden" value="@isAnony">'
	        	+' <p class="comment_action" style="@display">@comresult</p> '
	        	+'</div>'
	            +'</li>';
		   
		        if(comments[i].type=='2'){
					item = item.replace("@contentclass","commentImg");
					if(comments[i].content.indexOf('已被管理员')==0){
						item = item.replace("@Content",comments[i].content);
						item = item.replace("@content","");
					}else{
						item = item.replace("@Content","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
						item = item.replace("@content",comments[i].content);
					}
				}else{
					item = item.replace("@contentclass","");
					item = item.replace("@Content",checkURL(comments[i].content,true));
					item = item.replace("@content",checkURL(comments[i].content,true));
				}
		        item = item.replace("@UserId","'"+comments[i].userId+"'");
	            item = item.replace("@Name","'"+comments[i].personName+"'");
	            item = item.replace("@headPic","'"+comments[i].headPic+"'");
	            item = item.replace("@display","display:none");
	           
	          //记录历史数据
				item = item.replace("@userids",$("#userIds").val());
				item = item.replace("@type",$("#type").val());
				item = item.replace("@isAnony",$("#isAnony").val());
				if(type=='0'){
					item = item.replace("@comresult","<a style=\"display:none\"id=\"a_"+count+"\" onclick=\"againComment(this)\">重发</a>");
					 item = item.replace("@circle","circles_"+count);
					 item = item.replace("@circledisplay","display:block");
					 commentsDiv.prepend(item);
				}else{
					item = item.replace("@circle","circles");
					item = item.replace("@circledisplay","display:none");
					item = item.replace("@comresult","<a onclick=\"deleteComment('"+comments[i].commentId+"')\">删除</a>");
					commentsDiv.append(item);
				}
	    }
	   emojify.run($('.comment_list')[0]);
	}
</script>
</html>


