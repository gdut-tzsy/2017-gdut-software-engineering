<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <title>外部表单详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/zepto.min.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/main.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/jsp/wap/js/zepto.form.js"></script>
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <!-- 上传媒体文件（手机端页面）引入  start -->
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
    <!-- 上传媒体文件（手机端页面）引入  end -->
    <script src="${baseURL}/js/util/capital.js?ver=<%=jsVer%>"></script>
    <style>
        .taskBtn{
            margin-right: 5px;
            padding-left: 10px;
            padding-right: 10px;
        }
    </style>
</head>

<body class="sForm">
<div id="wrap_main" class="wrap" style="display:none">
    <div id="main" class="wrap_inner">
        <div class="p5">
            <div class="question_detail">
                <input type="hidden" id="lag" name="lag" value=""/>
                <input type="hidden" id="lat" name="lat" value=""/>
                <%--<div class="article_detail" id="ttitle">
                    <div class="form_title">
                        <h3 id="title"></h3>
                    </div>
                </div>--%>
                <div class="form_title" id="ttitle">
                    <h3 id="title"></h3>
                </div>
                <div class="avator_title">
                    <div class="title flexbox clearfix">
                        <div class="user_info flexItem">
                            <p id="detailTime" style="margin-top: 8px;"></p>
                        </div>
                    </div>
                </div>

                <div class="article_content" style="display:none" id="tcontent">
                    <p id="content"></p>
                </div>
                <div class="suxing" id="form_time" style="display:none">
                    <i class="fa fa-clock-o"></i><span id="formStop"></span>
                </div>
                <div id="content2" class="form_content"></div>
                <div id="imageDiv" style="display:none">
                    <div class="letter_bar">图片附件</div>
                    <div class="f-item">
                        <div class="loadImg mb10 clearfix" id="upPicDiv"
                             style="padding-left: 10px;">
                            <div class="f-add-user-list">
                                <ul id="imglist" class="impression">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 上传媒体文件（手机端页面）引入  start -->
                <div class="form-style" id="medialist" style="display:none;">
                    <div class="letter_bar file_top"><span class="file_top_tit">附件(0)</span><span class="file_top_btn" style="display:none;">
				               		<input type="file" name="file" id="fileFile1" fileName="mediaIds" class="upload_file_input" /><i>+</i>上传</span>
                    </div>
                </div>
                <!-- 上传媒体文件（手机端页面）引入  end -->

                <div class="comments-box" id="comments-box">
                    <div class="letter_bar" id="commentCount">回复(0)</div>
                    <div class="lastComment" id="commentCount0" style="display:none">暂无回复</div>
                    <div class="comment_list clearfix" id="comments">
                        <ul id="commentList">
                            <!-- 模板数据 -->
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="all_pull" id="getmore_comment" >
            <p class="lastComment"></p>
        </div>
      <%--  <div class="fbtns_desc" style="padding:10px 0 20px 0;" id="openFooterContent">
            <a href="http://wbg.do1.com.cn" target="_blank" style="color:#999;text-decoration: none;">
                <img src="../images/smallLogo.png" alt="" style="width:18px;vertical-align: middle;margin: 0 5px;" />企微云平台
            </a>提供技术支持
        </div>--%>
        <%@include file="/open/include/footer.jsp"%>
    </div>
</div>

<%@include file="/jsp/wap/include/showMsg.jsp"%>
<%--<%@include file="/manager/include/printPage.jsp" %>--%>
<%@include file="/manager/baidumap/selectLocation.jsp"%>
</body>
</html>
<script>

    //评论模板
    var temple = ''
            +' <li class="flexbox"> '
            +' 	<div class="avator"> '
            +' 	    <img src="@HeadPic" alt="" onerror="javascript:replaceUserHeadImage(this);"> '
            +' 	</div> '
            +' 	<div class="comment_content flexItem"> '
            +' 	<h3 class="clearfix"> '
            +' 	    <span class="title">@PersonName</span> '
            +' 	    <span class="time">@Time</span> '
            +' 	    <input type="hidden" value="@CommentId" /> '
            +' 	</h3> '
            +' 	<p class="@contentclass" onclick="@addressMethod">@Content</p> '
            +' <p class="comment_action" style="@display"></p> '
            +' 	</div> '
            +' </li> ';
    var yiyueHtml='<span class="file_top_btn" style="position: absolute;right: 10px;border-radius:2px" onclick="hideAndShowYiYue()"; id="isShowYiYue">@yiyue</span>';
    var pageIndex = "2";
    var pageSize=10;//每页显示条数
    var yiyueStatus=1;//1显示 2不显示;
    var fromType="";
    showFooter(true);
    //加载任务详情内容
    $(document).ready(function () {
        showLoading();
        $("#closeReason").bind("focus",function(){
            $("#closeTip").hide();
        });
        $.ajax({
            type:"GET",
            url: "${baseURL}/open/openFormAction!detailGetFroms.action",
            data: "id=${param.id}&size="+pageSize,
            cache:false,
            dataType:"json",
            success : function(result) {
                if(result.code!="0"){
                    hideLoading();
                    showMsg("",result.desc,1,{ok:function(result){history.go(-1);}});
                    return;
                }
                //是否有权查看表单
                var isSearchForm=result.data.controlPO.isSearchForm;
                var organizationName=result.data.organizationName;
                if(isSearchForm!="1"){
                    $("#comments-box").hide();
                    $(".p5").hide();
                    hideLoading();
                    showMsg("",organizationName+"暂未开启对该表单的查询功能");
                    return;
                }
                $("#wrap_main").show();
                //是否查看评论
                var isSearchComment = result.data.controlPO.isSearchComment;
                if(!isSearchComment == "1"){
                    $("#comments-box").hide();
                }
                var ct = result.data.detail.createaTime;
                if(ct.substring(0,10)==new Date().Format("yyyy-MM-dd")){
                    //当天发布的任务只显示时分
                    $("#detailTime").html("创建时间："+ct.substring(11,16));
                }else{
                    $("#detailTime").html("创建时间："+ct);
                }
                var detailsPO=result.data.detailsPO;
                var controlPO=result.data.controlPO;
                definitionId=controlPO.definitionId;
                quotaJson = result.data.quota;
                fromType=result.data.controlPO.isTask;
                document.title = detailsPO.formName+"详情";
                $("#title").html(result.data.instanceTitle);
                if(detailsPO.content!=null &&detailsPO.content!=""){
                    var content=detailsPO.content.replace(/@fileweb@/g,localport);
                    $("#content").html(content);
                    $("#tcontent").show();
                }
                if(controlPO.stopTime!=null&&controlPO.stopTime!=""){
                    $("#formStop").html("截止时间："+controlPO.stopTime);
                    $("#form_time").show();
                }
                if(controlPO.isPic=="1"){
                    //预览图片，改为公用方法
                    previewImages(result.data.picList);
                    $("#imageDiv").show();//显示图片
                }
                wxqyh_uploadfile.agent="form";//应用code
                wxqyh_uploadfile.groupId="${param.id}";
                if(result.data.mediaList.length>0){
                    $("#medialist").show();
                    previewFiles(result.data.mediaList,"medialist","mediaIds",1);
                }
                $("#content2").html(result.data.definitions);
                $("#content2").find(".form_item_notes").each(function(i) {
                    //  和$("#orderedlist li")一样效果
                    $(this).html(checkURL($(this).html(),false));
                });
                _initChildDetail("content2", result.data.parentKey);
                var itemMap = result.data.itemMap;
                if(itemMap){
                    getProvince();
                    _doEleValueDispathc(document.getElementById("content2"), itemMap);

                }

                //地理位置字段添加一个JS方法
                addShowMap(result);

                //显示评论
                var comments = result.data.comments;
                hasMore = result.data.hasMore;
                if(comments&&comments.length>0){
                    var yiyue=yiyueHtml.replace("@yiyue",yiyueStatus==1?"只看评论":"查看全部");
                    $("#commentCount").html("回复(<font>"+result.data.commentCount+yiyue+"</font>)");
                    appendCommnets(comments);
                    /* if (hasMore) {
                     $("#getmore_comment").html("<div class=\"load_more_user\"><a href=\"javascript:listMoreReply()\">点击查看更多回复</a></div>");
                     } */
                }else{
                    $("#commentCount0").show();
                    $("#getmore_comment").hide();
                }
                pageIndex = "2";
                emojify.run($('.comment_list')[0]);
                hideLoading();

                /* if("${param.src}"!=""){
                 $("#down_comment").click();
                 } */
            },
            error : function() {
                $("#comments-box").hide();
                $(".p5").hide();
                hideLoading();
                showMsg("","系统繁忙，请稍后再试！",1);
            }
        });
    });

    function loadNew(){
        pageIndex = 1;
    }

/*    function appendCommnets(comments) {
        for (var i = 0; i < comments.length; i++) {
            var item = temple;
            item = item.replace("@HeadPic",comments[i].headPic);
            item = item.replace("@PersonName",comments[i].personName);
            item = item.replace("@Time",comments[i].time);
            if(comments[i].type=='2'){
                item = item.replace("@contentclass","commentImg");
                item = item.replace("@Content","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
                item = item.replace("@content",comments[i].content);
            }else if((fromType=="2"||fromType=="1")&&comments[i].status=="2"){
                item = item.replace("<p class=\"@contentclass\">@Content</p>","<div class=\"meetingIcon icon7\"></div>");
                item = item.replace("@display","display:none");
            }else{
                item = item.replace("@contentclass","");
                item = item.replace("@Content",checkURL(comments[i].content,true));
                <%--item = item.replace("@Content",checkimgURL(checkURL(comments[i].content,true),"${compressURL}"));--%>
            }

            item = item.replace("@CommentId",comments[i].commentId);
            item = item.replace("@UserId","'"+comments[i].userId+"'");
            item = item.replace("@Name","'"+comments[i].personName+"'");
            item = item.replace("@headPic","'"+comments[i].headPic+"'");
            if(comments[i].userId=='${session.userId}'&&comments[i].status!='0'){
                item = item.replace("@CommentId","'"+comments[i].commentId+"'");
                item = item.replace("@display","display:block");
            }else{
                item = item.replace("@display","display:none");
            }
            $("#commentList").append(item);
        }
    }*/
    function appendCommnets(comments) {
        for (var i = 0; i < comments.length; i++) {
            var item = temple;
            item = item.replace("@HeadPic",comments[i].headPic);
            item = item.replace("@PersonName",comments[i].personName);
            item = item.replace("@Time",comments[i].time);
            if(comments[i].type=='2'){
                item = item.replace("@contentclass","commentImg");
                item = item.replace("@Content","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
                item = item.replace("@content",comments[i].content);
            }else if(comments[i].type=='4'){//地理位置
                item = item.replace("@addressMethod","openMapForMore(this)");
                item = item.replace("@contentclass","positionText");
                item = item.replace("@Content",comments[i].content);
            }
            else if(comments[i].status=="2"){
                item = item.replace("<p class=\"@contentclass\" onclick=\"@addressMethod\">@Content</p>","<div class=\"meetingIcon icon7\"></div>");
                item = item.replace("@display","display:none");
            }else{
                item = item.replace("@contentclass","");
                item = item.replace("@Content",checkimgURL(checkURL(comments[i].content,true),"${compressURL}"));
            }
            item = item.replace("@CommentId",comments[i].commentId);
            item = item.replace("@UserId","'"+comments[i].creator+"'");
            item = item.replace("@Name","'"+comments[i].personName+"'");
            item = item.replace("@headPic","'"+comments[i].headPic+"'");
            item = item.replace("@display","display:none");
            $("#commentList").append(item);
        }
        emojify.run($('.comment_list')[0]);
    }
    function radioSelect(obj){
        var self = $(obj),
                context = self.parent(),
                siblings = $('.active',context);

        siblings.removeClass('active');
        self.addClass('active');
    }
    function getImageField(obj,name,imgVal){
        var self = $(obj);

        var html="<li><input type=\"hidden\" name=\""+name+"\" value=\""+imgVal+"\" />"+
                "<a class=\"remove_icon\" onclick=\"doDelLi(this);\" href=\"javascript:void(0)\" style=\"display: none;\"></a>" +
                "<p class=\"img\"><img onclick=\"viewImage('${compressURL}"+imgVal+"');\" src=\"${compressURL}"+imgVal+"\"/></p></li>";
        obj.before(html);
    }
    var hasMore; //是否还有评论数据
    var lock_roll=false;
    $(function() {
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
            };
        });
    });

    function listMoreReply() {
        id = "${param.id}";
        $.ajax({
            type:"POST",
            url: "${baseURL}/open/openFormAction!listComment.action",
            data: "page="+pageIndex+"&id=" + id + "&size="+pageSize+"&status="+yiyueStatus,
            cache:false,
            dataType: "json",
            success: function(data){
                lock_roll=false;
                if ("0" != data.code) {
                    _alert("提示",data.desc,"确认",function(){restoreSubmit();});
                    return;
                }
                pageIndex = data.data.currentPage+1;

                var commentCount = "暂无回复";
                var yiyue=yiyueHtml.replace("@yiyue",yiyueStatus==1?"只看评论":"查看全部");
                if(data.data.commentCount>0 || pageIndex>2){
                    //$("#getmore_comment").show();
                    $("#commentCount0").hide();

                    commentCount = "回复(<font>"+data.data.commentCount+yiyue+"</font>) ";
                    $("#commentCount").html(commentCount);
                }
                else{
                    //	$("#getmore_comment").hide();
                    $("#commentCount0").show();
                    commentCount = "回复(<font>0"+yiyue+"</font>)";
                    $("#commentCount").html(commentCount);
                }
                var comments = data.data.comments;
                appendCommnets(comments);

                hasMore = data.data.hasMore;
                /* if (!hasMore) {
                 $("#getmore_comment").html("<p class=\"lastComment\">已没有更多</p>");
                 }
                 else{
                 $("#getmore_comment").html("<div class=\"load_more_user\"><a href=\"javascript:listMoreReply()\">点击查看更多回复</a></div>");
                 } */
            }
        });
    }
    function hideAndShowYiYue(){
        hasMore=false;
        if(yiyueStatus==1){
            yiyueStatus=2;
            pageIndex=1;
            $("#commentList").html("");
            listMoreReply();

        }else{
            yiyueStatus=1;
            pageIndex=1;
            $("#commentList").html("");
            listMoreReply();

        }
    }
    function showCreateDpartInfo(){
        var html="<h3>"+createUserNmae+"</h3><br/>所属部门:<br/>"+createDepartmentName;
        showMsg("",html,1);
    }
</script>
<script type="text/javascript" src="${baseURL}/themes/wap/js/form/form.js?ver=<%=jsVer%>"></script>
<script type="text/javascript">
    isOpen=true;
</script>