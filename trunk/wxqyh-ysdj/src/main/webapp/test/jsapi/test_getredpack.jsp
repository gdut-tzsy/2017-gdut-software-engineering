<%@page import="org.apache.struts2.components.Include"%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>发红包</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
		<script type="text/javascript" src="${baseURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

		<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
        <script>

      //评论模板
      var redpack_temple = ''
      	+' <li class="flexbox"> '
      	+' 	<div class="avator"> '
      	+' 	    <img src="@HeadPic" alt="" onerror="javascript:replaceUserHeadImage(this);"> '
      	+' 	</div> '
      	+' 	<div class="comment_content flexItem"> '
      	+' 	<h3 class="clearfix"> '
      	+' 	    <span class="title">@PersonName</span> '
      	+' 	    <span class="time">@Content元</span> '
      	+' 	</h3> '
      	+' 	<p>@Time<font color="red">@zuijia</font></p> '
      	+' 	</div> '
      	+' </li> ';
      	
      //加载任务详情内容
      $(document).ready(function () {
          	showLoading();
            $.ajax({
                type:"POST",
                url: "${baseURL}/portal/qiweiredpack/qiweiredpackAction!viewReadpack.action",
        		data:{"id":"${param.id}"},
                cache:false,
                dataType:"json",
      			success : function(result) {
  					hideLoading();
      				if(result.code=="0"){
      					if(result.data.isOK){
      						$("#getButton").show();
      					}
      					else{
          					//显示评论
          					var comments = result.data.list;
          					if(result.data.time && result.data.time!=""){
              		            $("#commentCount").html("<font>"+result.data.totalCount+"</font>个红包，"+result.data.time+"被抢光");
          					}
          					else{
              		            $("#commentCount").html("<font>"+comments.length+"/"+result.data.totalCount+"</font>个红包");
          					}
          					appComments(comments);
      						$("#myGet").show();
      						$("#comments-box").show();
      					}
      				}else{
      					hideLoading();
      					showMsg("",result.desc,1,{ok:function(result){WeixinJS.back();}});
      				}
      			},
      			error : function() {
      				hideLoading();
      				showMsg("",internetErrorMsg,1,{ok:function(result){WeixinJS.back();}});
      			}
      	});
      });
      
      function appComments(comments){
			if(comments&&comments.length>0){
				var temp ="";
		        for (var i = 0; i < comments.length; i++) {
		            var item = redpack_temple;
		            item = item.replace("@HeadPic",comments[i].headPic);
		   			item = item.replace("@PersonName",comments[i].personName);
		   			item = item.replace("@Time",comments[i].createrTime);
		   			item = item.replace("@Content",(comments[i].amount/100.00).toFixed(2));
		   			item = item.replace("@zuijia",comments[i].status=="1"?"手气最佳":"");
		   			temp += item;
		        }
	            $("#commentList").append(temp);
			}
      }

        	//提交
        	function commitTask(){
        		showLoading("正在抢红包...");
        		document.getElementById("activityform").action ="${baseURL}/portal/qiweiredpack/qiweiredpackAction!getReadpack.action";            				           		
        		$("#activityform").ajaxSubmit({
        			type:"POST",
	        		dataType:"json",
	        		success:function(result){
	        			hideLoading();
	        			if(result.code=="0"){
          					//显示评论
          					var comments = result.data.list;
          					if(result.data.time && result.data.time!=""){
              		            $("#commentCount").html("<font>"+result.data.totalCount+"</font>个红包，"+result.data.time+"被抢光");
          					}
          					else{
              		            $("#commentCount").html("领取<font>"+comments.length+"/"+result.data.totalCount+"</font>个");
          					}
          					appComments(comments);
          					$("#getButton").hide();
      						$("#myGet").show();
      						$("#comments-box").show();
	        			}else if(result.code == "17004"){
	            			showMsg("",result.desc,1);
          					var comments = result.data.list;
          					if(result.data.time && result.data.time!=""){
              		            $("#commentCount").html("<font>"+result.data.totalCount+"</font>个红包，"+result.data.time+"被抢光");
          					}
          					else{
              		            $("#commentCount").html("领取<font>"+comments.length+"/"+result.data.totalCount+"</font>个");
          					}
          					appComments(comments);
          					$("#getButton").hide();
      						$("#myGet").show();
      						$("#comments-box").show();
	        			}
	        			else{
	            			showMsg("",result.desc,1);
	        			}
	        		},
	        		error:function(){
	        			showMsg("",internetErrorMsg,1);
	        			hideLoading();
	        		}
        		});
        	}
        </script>
        <style>
        .inputStyle{
	        text-align: left;
			line-height: 18px; 
			border: none;
			font-size: 14px;
			vertical-align: top;
        }
        </style>
    </head>
    
    <body>
        <div id="tips_main" class="wrap">
            <div id="main" class="wrap_inner">
            	<form action="" id="activityform" onsubmit="return false;">
	                <input name="id" type="hidden" value="${param.id}" />
	                <div class="form-style" id="getButton" style="display: none;">
	                    <div class="form_btns mt10">
	                        <div class="inner_form_btns">
	                            <div class="fbtns flexbox"> 
	 								<a class="fbtn btn flexItem" style="margin-left: 5px;" href="javascript:commitTask()">抢红包</a>
	                            </div>
	                            <div class="fbtns_desc">抢到红包，请记得去拆哦</div>
	                        </div>
	                    </div>
	                </div>
                </form>
                <div class="form-style" id="myGet" style="display: none;">
                    <div class="form_btns mt10">
                        <div class="inner_form_btns">
                            <div class="fbtns_desc" id="num"></div>
                        </div>
                    </div>
                </div>
                <div class="comments-box" id="comments-box" style="display: none;">
                    <div class="letter_bar" id="commentCount"></div>
                    <div class="comment_list clearfix" id="comments">
                        <ul id="commentList">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        
        <%@include file="/jsp/wap/include/showMsg.jsp"%>
		<%@include file="/jsp/wap/include/uploadImage.jsp" %>
		<%@include file="/jsp/wap/include/target_select.jsp"%>
    </body>
</html>