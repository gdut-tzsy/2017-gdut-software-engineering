<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <title>新闻公告</title>
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
		<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script type="text/javascript" src="${resourceURL}/jsp/wap/js/emojify.min.js?ver=16.6.24.12"></script>
		<!-- 评论发表图片所需要         end  -->
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
						<div class="detail-small-title">
							<span id="publisherTime"></span>
							<span id="publisher"></span>
						</div>
						<div class="detail-content article_content detail_body">
							<div id="topImage"></div>
							<div id="content"></div>
						</div>
						<div class="detail-type-bar">
							<span id="openViewCounts"></span>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  start -->
						<div class="form-style mb20" id="medialist" style="display:none;">
							<div class="letter_bar file_top borderBottommNone"><span class="file_top_tit">附件(0)</span></div>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  end -->
						<div class="zanNumList mt10 mb20" id="hasPraise" style="display:none">
							<div class="zanNumList_t"><i class="faThumbsUp"></i><div class="zanNum"></div><span id="zhankai">展开</span></div>
							<div class="zanNumList_c" id="praiseId"></div>
						</div>
					</div>
					<div class="comments-box" id="comments-box">
						<div class="letter_bar first_top" id="commentCount">回复(0)</div>
						<div class="lastComment" id="commentCount0">暂无回复</div>
						<div class="comment_list clearfix" id="comments" >
							<ul id="commentList"></ul>
						</div>
					</div>
                </div>
				<div class="all_pull"  id="getmore_comment">
					<p id="hasMore" class="lastComment" onclick="showMoreComments()"></p>
				</div>
                <%@include file="/open/include/footer.jsp"%>
            </div>
        </div>
        
        <div class="share_bg"></div>
	    <div class="share_box">
	        <div class="share1"></div>
	        <div class="share2"></div>
	    </div>
		<!-- 需要在此，引入 以下两个jsp，特别注意必须在<div id="main" class="wrap_inner">下加入以下include-->
		<%@include file="/open/include/showMsg.jsp"%>
    </body>

</html>
<script type="text/javascript">

	//关闭窗口：因为外部分享是没有back的，所以只能直接关闭
	function closeWin(){
		var isOpentype='${param.type}';
		if("1"==isOpentype){
			WeixinJS.back();
		}else{
			window.opener=null;
			window.open('','_self');
			window.close();
		}
	}

	document.documentElement.addEventListener('touchend',function(){
	    $('.share_bg,.share_box').hide();
	})
	$(".share_bg,.share_box").on("click",function(){
		$('.share_bg,.share_box').hide();
	})
	
	var isSecret="0";

	var secretName="保密";
	function shuiying(){//添加水印功能
		$('.article_detail').css('-webkit-user-select','none');
    	var h=$(window).height();
	    var w=$(window).width()-150;
	    var t=h/3+100;
	    var div=$('.article_detail');
		var h1=div.height();
	    for(var i=0;i<=Math.ceil(h1/t);i++){
	    	div.append($('<div class="nameAfter" data-content="'+secretName+'" style="top:'+(Math.round(t*i+100))+'px;left:'+w/2+'px"></div>'));
	    }
		$(window).on('touchstart',function(){
			var top=parseInt($('.nameAfter:last').css('top'));
			var h1=div.height();
			if(h1-top>=(t+100)){
				for(var i=1;i<Math.ceil((h1-top)/t);i++){
			    	div.append($('<div class="nameAfter" data-content="'+secretName+'" style="top:'+(Math.round(t*i+100+top))+'px;left:'+w/2+'px"></div>'));
			    }	
			}
			
		})
	}
	
    var dynamicInfoId;
    var hasMore; //是否还有评论数据
    var comcount=0;//评论条数
    var count=0;   //评论的第几条，用于标识重发
	var page = 1;  //分页的当前页数
    $(document).ready(function () {
    	var isOpentype='${param.type}';
    	/*if("1"==isOpentype){
    		$('.share_bg,.share_box').show();
    	}*/
    	showLoading();
        $.ajax({
            type:"POST",
            url: "${baseURL}/open/dynamicinfo/dynamicinfoAction!dynamicinfoDetail.action",
            data: "id=${param.id}&size=10",
            cache:false,
            dataType: "json",
            success: function(data){
                if ("0" != data.code) {
                	hideLoading();
                    showMsg("",data.desc,1,{ok:function(result){closeWin();}});
                    return;
                }
                var info = data.data;
                
                if(info.isShowFooter==true){
                	showFooter(true);
                }
                
                if(info.tbXyhDynamicinfoPO.creator=="${session.userId}"){//创建者
					$("#copyFrom_div").css("display","inline");
				}
                var content = info.tbXyhDynamicinfoPO.content;
                content = content.replace(/http\:\/\/qy\.do1\.com\.cn\:6090\/fileweb/g,localport).replace(/@fileweb@/g,localport);
                	
                $("#title").html(info.tbXyhDynamicinfoPO.title);
                if("0"==info.tbXyhDynamicinfoPO.isDisplayCoverImage || null==info.tbXyhDynamicinfoPO.isDisplayCoverImage){
                	//显示封面
                	if(info.tbXyhDynamicinfoPO.coverImage!="" && info.tbXyhDynamicinfoPO.coverImage!=null){
                		if("2"==info.tbXyhDynamicinfoPO.sendType){
                			//周年祝福处理
                			if(info.tbXyhDynamicinfoPO.ext2=="1"){
                				//使用默认模板
                				if(info.tbXyhDynamicinfoPO.coverImage!=null && info.tbXyhDynamicinfoPO.coverImage.indexOf("/manager/images/")>=0){
                					$("#topImage").append("<img id=\"coverImage\" src=\"${baseURL}" + info.tbXyhDynamicinfoPO.coverImage + "\" width=\"100%\" />");
                				}else{
                					$("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhDynamicinfoPO.coverImage + "\" width=\"100%\" />");
                				}
                			}else{
                				$("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhDynamicinfoPO.coverImage + "\" width=\"100%\" />");
                			}
                		}else{
                			if(info.tbXyhDynamicinfoPO.coverImage.indexOf("/upload/img/birthday/birthday")>=0){
                				$("#topImage").append("<img id=\"coverImage\" src=\"${baseURL}" + info.tbXyhDynamicinfoPO.coverImage + "\" width=\"100%\" />");
                			}else{
                				$("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhDynamicinfoPO.coverImage + "\" width=\"100%\" />");
                			}
                		}
                	}
                }
				if(info.videoPO){
					$("#topImage").after('<audio id="audio" style="height:0;width:0;display:none"  src="'+localport+info.videoPO.url+'"></audio></div>')
					var currentTime = 0;
					var timer = null;
					var durationTime = 60
					$("#audio")[0].addEventListener("canplay", function() {
						durationTime = parseInt($("#audio")[0].duration);
						$(".share_audio_length_total").text(getVideoTime(durationTime))
					})
					var videoStr='<div id="voice_frame" class="share_audio_context flex_context">' +
							'<div id="voice_play" class="share_audio_switch">' +
							'<em class="icon_share_audio_switch"></em>' +
							'</div>' +
							'<div class="share_audio_info flex_bd">' +
							'<strong class="share_audio_title">'+info.videoPO.fileName+'</strong>' +
								/*'<div class="share_audio_progress">' +
								 '<div id="voice_progress" style="width: 0%;" class="share_audio_progress_inner">' +
								 '</div></div>' +
								 '<div class="share_audio_desc">' +
								 '<em id="voice_playtime" class="share_audio_length_current">00:00</em>' +
								 '<em class="share_audio_length_total">'+getVideoTime(durationTime)+'</em>' +
								 '</div>*/
							'</div>'
					$("#topImage").after(videoStr);
					$("#voice_frame .share_audio_switch").click(function(){
						if(!$("#voice_frame").hasClass("share_audio_playing")){
							$("#voice_frame").addClass("share_audio_playing");
							$("#audio")[0].play();
							timer = setInterval(function(){
								currentTime++
								$("#voice_progress").width(currentTime/durationTime*100+"%");
								$("#voice_playtime").text(getVideoTime(currentTime));
							},1000)
						}else{
							$("#voice_frame").removeClass("share_audio_playing");
							$("#audio")[0].load();
							clearInterval(timer);
							$("#voice_progress").width(0);
							$("#voice_playtime").text("00:00");
							currentTime = 0;
						}
					})
				}

				function getVideoTime(time){
					var hour = Math.floor(time/60);
					hour = hour>9?hour:"0"+hour;
					var Minute = time%60;
					Minute = Minute>9?Minute:"0"+Minute;
					return hour+':'+Minute
				}
				if(info.tbXyhDynamicinfoPO.isComment!="1"){
					$("#comments-box").show();
					$("#getmore_comment").show();
					$(".foot_bar").show();
					$(".footheight").show();
				}
				//如果不开启外部分享显示评论，则不显示评论和点赞信息
				if(info.tbXyhDynamicinfoPO.shareComment != "1"){
					$("#hasPraise").hide();
					$("#comments-box").hide();
					$("#getmore_comment").hide();
				}
				else{
					if(info.praisePager) {
						var praisePager = info.praisePager
						var praiseList = praisePager.pageData;
						var $div=$('.zanNumList_t').next('.zanNumList_c');
						$(".zanNum").html(praisePager.totalRows+"赞");
						if (praiseList != null && praiseList.length > 0) {
							var praiseNames="";
							for (var i = 0; i < praiseList.length; i ++) {
								if(praiseList[i].creator=='${session.userId}'){
									$('.zanClick span').addClass('pulse');
									$('.zanCount').removeClass('c999');
								}
								if(i == 0){
									praiseNames=praiseList[i].personName;
								}else{
									praiseNames+="，"+praiseList[i].personName;
									if($(this).hasClass('down')){
										$("#zhankai").text("展开");
										$(this).removeClass('down');
									}else{
										if($div[0].scrollHeight>$div.height()){
											$("#zhankai").text("收起");
											$(this).addClass('down');

										}
									}
								}

							}
							$('#hasPraise').show();
							$('#praiseId').html(praiseNames);
							if($('.zanNumList_c')[0].scrollHeight<=$('.zanNumList_c').height()){
								$("#zhankai").hide();
							}
							$('.zanCount').removeClass('c999').html("已有<span >"+praisePager.totalRows+"</span>人点赞");
/*							if(praisePager.currentPage<praisePager.totalPages){
								$('#praiseId').html($('#praiseId').html()+"<a class='ml5 red' href='../public/praise_list.jsp?id=${param.id}&agentCode=dynamic'>查看更多<a>");
							}*/
						}
					}
					$('.zanClick').on('click',function(){
						if($(this).children().hasClass('pulse')){
							$(this).children().removeClass('pulse');
							$(this).removeClass("afterZan");
						}else{
							$(this).children().addClass('pulse');
							$(this).addClass("afterZan");
						}

						setPraise();
					});
					$('.zanNumList_t span').on('click',function(){
						if($(this).hasClass('down')){
							$(this).removeClass('down');
							$div.attr('style','');
							$("#zhankai").text("展开");
						}else{
							if($div[0].scrollHeight>$div.height()){
								$(this).addClass('down');
								$div.css('max-height','none');
								$("#zhankai").text("收起");

							}
						}
					});
					var commentCount = "暂无回复";
					comcount=info.tbXyhDynamicinfoPO.commentCount;
					if(info.tbXyhDynamicinfoPO.commentCount>0){
						$("#getmore_comment").show();
						$("#commentCount").show();
						$("#commentCount0").hide();
						commentCount = "回复(<font>"+info.tbXyhDynamicinfoPO.commentCount+"</font>)";
					}
					else{
						$("#getmore_comment").hide();
						commentCount = "回复(<font>"+info.tbXyhDynamicinfoPO.commentCount+"</font>)";
						$("#commentCount").html(commentCount);
						$("#commentCount0").show();
					}
					$("#commentCount").html(commentCount);
					$("#dynamicinfoId").val(info.tbXyhDynamicinfoPO.dynamicInfoId);
					var comments = info.comments;
					var commentsDiv = $("#comments");
					appendComments(commentsDiv, comments,"1");
					hasMore = info.hasMore;
					if (!hasMore) {
						$("#hasMore").html("<span style=\"color: #999999;\">已没有更多</span>");
						$("#hasMore").attr("onclick","javascript:void(0);");
					}else{
						$("#hasMore").html("<span style=\"color: #999999;\">点击加载更多</span>");
					}
				}


				//分享信息
				var title=info.tbXyhDynamicinfoPO.title;
				var img=compressURL+info.tbXyhDynamicinfoPO.coverImage;
				if(info.tbXyhDynamicinfoPO.coverImage==""){
					img = info.logo;
				}

				var summary=info.tbXyhDynamicinfoPO.summary;
				var shareUrl=window.location.href;
				if(shareUrl.indexOf("&")>0){
					var str=shareUrl.split("&");
					shareUrl=str[0];
				}
				document.title = title;
				setDataForWeixinValue(title,img,summary,shareUrl);

				$("#publisher").html(info.tbXyhDynamicinfoPO.publisher);
	            $("#publisherTime").html(info.tbXyhDynamicinfoPO.publishTime);
				var imgnum;
				var imgindex=0;
				var imgarray = info.images.slice(0);
	            //手机端发布的动态替换换行 其实是用contentType,不是这个info.tbXyhDynamicinfoPO.source=="手机"
	            if(info.tbXyhDynamicinfoPO.contentType=="来自手机"){
					var content_str = checkURL(content,true);
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
								imgstr='<img src="${compressURL}'+info.tbXyhDynamicinfoPO.coverImage+'">'
								$("#topImage img").remove();
							}else{
								imgstr='<img src="${compressURL}'+info.images[imgnum-2].path+'">'
								for(var i = 0;i<imgarray.length;i++){
									if(imgarray[i].path==info.images[imgnum-2].path){
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
	            }else{
	            	if(info.tbXyhDynamicinfoPO.sendType=="2" && info.tbXyhDynamicinfoPO.mergePeopleCount=="1"){
	            		//周年祝福特殊处理
	            		var contentHtml="<div class=\"templets\">"
			            				    +"<div id=\"contentText\" class=\"text\">"
			            					+info.tbXyhDynamicinfoPO.content
			            					+"</div>"
	            						+"</div>";
	            		$("#content").html(checkURL(contentHtml,false));
	            		//处理背景图片和文字样式
	            		var contentImage="";
	            		if(info.tbXyhDynamicinfoPO.contentImage!=null && info.tbXyhDynamicinfoPO.contentImage.indexOf("/manager/images/")>=0){
	            			//默认图片
	            			contentImage="${baseURL}"+info.tbXyhDynamicinfoPO.contentImage;
	            		}else{
	            			//上传的图片
	            			contentImage="${compressURL}"+info.tbXyhDynamicinfoPO.contentImage;
	            		}
	            		var img=new Image();
	            	    img.src=contentImage;
	            	  	img.onload=function(){
	            	    	$('.templets').css('background-image','url('+img.src+')');
	            	    	$('.templets').height(($(window).width()-20)*img.height/img.width);
	            	    };
	            	}else{
	            		$("#content").html(checkURL(content,false));
	            	}
	            }
				videoPlayback();

				
                $("#dynamicinfoId").val(info.tbXyhDynamicinfoPO.dynamicInfoId);
                
                //替换图片url
                /* $(".article_content").find("img").each(function(){
                	//this.src=this.src.replace("do1.com.cn/","do1.com.cn:7090/");
                	this.style.width="100%";
                }); */
                  
                var images = info.images;
				if(imgnum){
					images = imgarray;
				}
                if(images !=null && images.length > 0){
/*                	if(info.tbXyhDynamicinfoPO.contentType=="来自手机"){
                		//手机端发布需用倒叙
    	                for (var i = images.length-1; i >=0; i--) {
    	                    var img = "<p><img src=\"${compressURL}" + images[i].path + "\" width=\"100%\" /></p>"
    	                    $("#topImage").after(img);
    	                }    
                	}else{
    	                for (var i = 0; i < images.length; i++) {
    	                    var img = "<p><img src=\"${compressURL}" + images[i].path + "\" width=\"100%\" /></p>"
    	                    $("#topImage").after(img);
    	                }    
                	}*/
					for (var i = 0; i < images.length; i++) {
						var img = "<p><img src=\"${compressURL}" + images[i].path + "\" width=\"100%\" /></p>"
						$("#topImage").append(img);
					}
				}
				
				if(info.mediaList.length>0){
					$("#medialist").show();
					previewFiles(info.mediaList,"medialist","mediaIds");
				}
                dynamicInfoId = info.tbXyhDynamicinfoPO.dynamicInfoId;
                /* $("#aLink").attr("href","minaim-xyh://url.do1.com/${localport}/wap/component/dynamicinfo/dynamicinfo/dynamicinfo_detail.jsp?id="+info.tbXyhDynamicinfoPO.dynamicInfoId);
				downloadUrl="${localport}/wap/about/index.jsp?openurl="+$("#aLink").attr("href"); */
				hideLoading();
				
				if("${param.src}"!=""){
					$("#down_comment").click();
				}
				
/* 				$(".article_content img").each(function(){
					$(this).css("pointer-events:none");
				}); */
				
				isSecret=info.tbXyhDynamicinfoPO.isSecret;
 				if(info.tbXyhDynamicinfoPO.isSecret=="1"){
					shuiying();//调用水印功能
				}
 				if(isSecret=="1"){
 					//alert(isSecret);
 	 				$(".article_content img").each(function(){
 	 					$(this).css("pointer-events","none");
 	 				});
 				}
 				
 				$("#openViewCounts").html("已阅("+info.openViewCounts+")");
				
            },
            error : function() {
				hideLoading();
				//window.location.href="${baseURL}/jsp/wap/tips/error.jsp?msg=此动态已被删除";
				showMsg("","网络打了个盹，请重试",1,{ok:function(result){closeWin();}});
			}
        });  
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
	    wxqyh_uploadfile.agent="dynamic";//应用code
	    wxqyh_uploadfile.groupId="${param.id}";
		/**上传媒体文件引入  end*/
    });
    
    //跳转到查看阅读人列表
    function gotoViewUserList(id){
    	//alert("跳转到查看阅读人列表");
    	window.location.href="read_list.jsp?id="+id;
    }
    
    
    //13键按下
    function keyDown13(){
		var event = arguments.callee.caller.arguments[0] || window.event;
		if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13，想看其他代码请猛戳这里。
			commitComment("0","");
		}
    }
 	//点击图片后回掉函数
    function commentBack(){
    	$("#type").val("2");
    	$("#sendComment").val($("#imageInput").val());
    	commitComment("0","");
    }
    
    
    /**
     * 提供是否可以分享
     */
    function canShare(){
    	return 1;
    }
    
    function init(){
		var source = '${param.source}';
		if(source=="menu"){
			//从菜单进入，清空选择的联系人数据
			delCookie("taskformdata");
		}
   		var toPersons = getCookie("dynamic");
   		var persons="";
   		var userIds="";
		if(toPersons!=""&&toPersons!=undefined){
			var toArray = toPersons.split(",");
			$.each(toArray,function(index,value){
				var person = value.split("|");
				$("#sendComment").val();
				persons += "@"+person[1]+" ";
				userIds += person[0]+"~";
			});
			$("#sendComment").val(persons);
			$("#userIds").val(userIds);
			delCookie("dynamic");
		}
	}
	function listMoreReply(page) {
		var list = $("#commentList").find("input");
		//var lastCommentId = $("#commentList input:last").val();
		$.ajax({
			type:"POST",
			url: "${baseURL}/open/dynamicinfo/dynamicinfoAction!listComment.action",
			data:  "id=${param.id}&size=10&page="+page,
			async:false,
			cache:false,
			dataType: "json",
			success: function(data){
				if ("0" != data.code) {
					return;
				}

				var comments = data.data.comments;
				var commentsDiv = $("#comments");
				appendComments(commentsDiv, comments,"1");

				hasMore = data.data.hasMore;
				 if (!hasMore) {
					 $("#hasMore").html("<span style=\"color: #999999;\">已没有更多</span>");
					 $("#hasMore").attr("onclick","javascript:void(0);");
				 }
				emojify.run($('.comment_list')[0]);
			}
		});
	}
	function showMoreComments(){
		page+=1;
		listMoreReply(page);
	}
    function loadNew(){
    	$("#sendComment").val("");
    	$("#down_comment").click();
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
			+' 	<p class="@contentclass">@Content</p><div id="@circle" class="abc" style="@circledisplay">'
			+'  <div id="circle_1" class="circleItem"></div><div id="circle_2" class="circleItem"></div>'
			+'  <div id="circle_3" class="circleItem"></div>'
			+'  </div> '
			+'  <input type="hidden" value="@userids">'
			+'  <input type="hidden" value="@type">'
			+'  <input type="hidden" value="@content">'
			+' 	</div> '
			+' </li> ';
	function appendComments(commentsDiv, comments,type) {
		for (var i = 0; i < comments.length; i++) {
			var item = temple;
			item = item.replace("@HeadPic",comments[i].headPic);
			item = item.replace("@PersonName",comments[i].personName);
			item = item.replace("@Time",comments[i].time);
			//item = item.replace("@Content",comments[i].content.replace(/\s/g,"&nbsp;"));
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

			item = item.replace("@CommentId",comments[i].commentId);
			item = item.replace("@UserId","'"+comments[i].userId+"'");
			item = item.replace("@Name","'"+comments[i].personName+"'");
			item = item.replace("@headPic","'"+comments[i].headPic+"'");
			if(comments[i].time=="刚刚"){
				if(comments[i].userId=='${session.userId}'&&comments[i].commentStatus!='1'&&comments[i].commentStatus!='2'){
					item = item.replace("@CommentId","'"+comments[i].commentId+"'");
					item = item.replace("@display","display:block");
				}else{
					item = item.replace("@display","display:none");
				}
			}else{
				var time=comments[i].time.substring(0,comments[i].time.length-3);
				var timeType=comments[i].time.substring(comments[i].time.length-3,comments[i].time.length);
				if(comments[i].userId=='${session.userId}'&&comments[i].status!='1'&&time<30&&timeType=="分钟前"){
					item = item.replace("@CommentId","'"+comments[i].commentId+"'");
					item = item.replace("@display","display:block");
				}else{
					item = item.replace("@display","display:none");
				}
			}

			//记录历史数据
			item = item.replace("@userids",$("#userIds").val());
			item = item.replace("@type",$("#type").val());

			if(type=='0'){
//				item = item.replace("@comresult","<a style=\"display:none\"id=\"a_"+count+"\" onclick=\"againComment(this)\">重发</a>");
				item = item.replace("@circle","circles_"+count);
				item = item.replace("@circledisplay","display:block");
				$("#commentList").prepend(item);
			}else{
				item = item.replace("@circle","circles");
				item = item.replace("@circledisplay","display:none");
//				item = item.replace("@comresult","<a onclick=\"deleteComment('"+comments[i].commentId+"')\">删除</a>");
				$("#commentList").append(item);
			}
		}
		emojify.run($('.comment_list')[0]);
		return count;
	}
</script>