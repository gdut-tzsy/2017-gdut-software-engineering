<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>活动详情</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <!-- 评论发表图片所需要         start  -->
    <script src='${baseURL}/jsp/wap/js/comment.js?ver=<%=jsVer%>'></script>
    <!-- 评论发表图片所需要         end  -->
    <!-- 上传媒体文件（手机端页面）引入  start -->
    <script src="${baseURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
    <!-- 上传媒体文件（手机端页面）引入  end -->
    <%--生成二维码的js --%>
    <script type="text/javascript" src="${baseURL}/themes/qw/js/qrcode.js"></script>
    <%--复制的js --%>

    <script type="text/javascript" src="${apiMapBaidu}"></script>
    <script type="text/javascript" src="${convertorBaidu}"></script>
    <%-- 百度统计 --%>
    <script type="text/javascript">
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?6abcc5eeee320072f7a9ed10e79be5c1";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
    <style>
        .suxing{
            padding: 3px 0;
        }
        /*@media screen and (min-width: 800px){
            .wrap{
                width: 600px;
                border: 1px #ddd solid;
                margin: 20px auto 0;
                background: #fff;
            }
            #openFooter{
                width: 600px;
                margin: 0 auto 0;
            }
        }*/
    </style>
</head>

<body>
    <div id="wrap_main" class="wrap">
        <div id="main" class="wrap_inner">
            <div class="article_detail" id="open_detail">
                <div class="open_avator_title flexCenter" id="baoming" style="display: none;">
                    <h3 id="finish">距离活动报名截止时间：</h3>
                    <p id="time"></p>
                    <%--<div class="title flexbox clearfix">--%>
                        <%--<div class="flexItem flexCenter">--%>
                            <%----%>
                        <%--</div>--%>
                    <%--</div>--%>
                </div>
                <div class="article_content openContent">
                    <h3 id="title" class="mb10 mt10"></h3>
                    <p id="topImage"></p>
                    <p id="content"></p>
                    <div class="prizeList" id="lotteryList" style="display:none"></div>
                </div>
                <!-- 上传媒体文件（手机端页面）引入  start -->
                <div class="form-style" id="medialist" style="display:none;">
                    <div class="letter_bar file_top"><span class="file_top_tit">附件(0)</span></div>
                </div>
                <!-- 上传媒体文件（手机端页面）引入  end -->
                <div class="suxing" style="padding-top: 15px;border-top: 1px #eee solid">
                    <i class="icon-suxing icon-suxing-user"></i>
                    <span>主&ensp;办&ensp;方：<span id="actvityCreator"></span></span>
                </div>
                <div class="suxing">
                    <i class="icon-suxing icon-suxing-clock"></i>
                    <span>活动时间：<span id="activityStart"></span> 至 <span id="activityStop"></span></span>
                </div>
                <div class="suxing" id="registryStopDiv" style="margin-left: 28px">
                    <span>报名时间：<span id="registryStop"></span>&ensp;<font color="#999">截止</font></span>
                </div>
                <div class="suxing" id="cancelRegistryStopDiv" style="margin-left: 28px">
                    <span>取消报名：<span id="cancelRegistryStop"></span>&ensp;<font color="#999">截止</font></span>
                </div>
                <div class="suxing" id="registry_remark" style="display: none"></div>
                <div class="suxing relative">
                    <i class="icon-suxing icon-suxing-map ml5"></i>
                    <span>地&ensp;&ensp;&ensp;&ensp;点：<span id="activityLocation" class="ellipsis" style="width: 50%;vertical-align: middle;margin-top: -2px"></span></span>
                    <div id="btn_openmapId" class="mapBtnSp" onclick="openMap();" style="position: absolute;right: 10px;top: 3px;background-position:5px 3px;display: none;">地图</div>
                    <input id="lag" type="hidden" />
                    <input id="lat" type="hidden" />
                </div>
                <div class="letter_bar" id="active_userlist_count" style="margin-top:15px;padding: 15px 10px 5px;border-bottom: none">已报名(0/0人)</div>
                <div class="f-item" id="registry_list" style="">
                    <a href="javascript:showRegistryDetail();" class="quick_tel w45 ku_arrow2" id="registryA" style="text-decoration: none;"></a>
                    <div class="f-add-user clearfix">
                        <div class="inner-f-add-user">
                            <div class="f-add-user-list">
                                <ul id="registryNames">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="lotteryDiv" class="form_btns" style="display:none;padding: 15px 10px;">
                    <%--<div class="inner_form_btns" id="timeout_div" style="">
                        <div id="registryBtn" class="fbtns flexbox" style="display: none;">
                            <a class="btn flexItem" id="regLink" style="border-radius: 1px" href="javascript:registryOrNot()">我要报名</a>
                        </div>
                        <p class="timeout-bar" id="close_time" style="display: none;margin-top: 8px;"></p>
                         <p class="timeout-bar" id="close_time" style="">报名结束：06-06 18:00</p>
                        <p class="timeout-bar" id="close_time" style="">活动取消：06-06 18:00</p>
                        <p class="timeout-bar" id="close_time" style="">活动结束：06-06 18:00</p>
                    </div--%>
                    <%--<div class="process_tit" id="shareActivity" onclick="share();" style="width:auto;margin-top: 8px;">分享活动</div>--%>
                    <div class="process_tit" id="openQuestion" onclick="openQuestionDetail()" style="width:auto;display:none;margin-top:8px;"><i class="icon-vote"></i>参与现场投票</div>
                    <div class="process_tit" id="openLottery" onclick="openLotteryDetail()" style="width:auto;display:none;margin-top:8px;"><i class="icon-lottery"></i>查看获奖名单</div>
                </div>
                <div class="comments-box" id="comments-box">
                    <div class="letter_bar" id="commentCount" style="padding: 15px 10px 5px;border-bottom: none">回复(0)</div>
                    <div class="comment_list clearfix">
                        <ul id="comments">
                        </ul>
                    </div>
                </div>
            </div>
            <div id="openFooter"  style="background-color: #fff;padding-top: 10px;">
                <%@include file="/open/include/footer.jsp"%>
            </div>
            <div class="footheight" id="height_div" style="height: 63px;"></div>
        </div>
        <%--<div class="foot_bar commentBtnBox" id="isComment_div" style="display: none;">
            <form id="form1" method="post" action="${baseURL}/open/activity/openActivityAction!addActivityComment.action" onsubmit="return false">
                <input type="hidden" name="userId" value="${session.userId}" />
                <input type="hidden" name="id" value="${param.id}" />
                <input type="hidden" id="sendComment" onkeydown="keyDown13();" name="tbQyActivityCommentPO.content" placeholder="说点什么吧">
                <input type="hidden" id="userIds" name="userIds">
                <input type="hidden" id="type" name="tbQyActivityCommentPO.type" value="1">
            </form>
            <%@include file="/jsp/wap/include/emoji2.jsp"%>
        </div>--%>
        <div class="open_foot_bar">
            <din class="inner_open_foot_bar flexbox flexCenter">
                <%--<div class="open_foot_icon_div" style="border-right: 1px #d9d9d9 solid;"><a class="open_foot_icon icon_share" onclick="share();"></a></div>--%>
                <div class="open_foot_button flexItem">
                    <a class="btn" id="regLink" href="javascript:registryOrNot()" style="border-radius: 0;">我要报名</a>
                    <a class="btn" id="stopRegLink" href="javascript:;" style="display:none;border-radius: 0;background-color: #ccc;">报名已截止</a>
                </div>
                <div class="open_foot_icon_div"><a class="open_foot_icon icon_comment" href="javascript:comment()"></a></div>
            </din>

            <%--<din class="inner_open_foot_bar flexbox flexCenter">
                <a class="open_foot_icon icon_share" onclick="share();"></a>
                <a class="btn flexItem" id="regLink" href="javascript:registryOrNot()" style="margin:0 35px;">我要报名</a>
                <a class="btn flexItem" id="stopRegLink"  style="display:none;margin:0 35px;background-color: #ccc;">报名已截止</a>
                <a class="open_foot_icon icon_comment" href="javascript:comment()"></a>
            </din>--%>
        </div>
        <div class="fixed return_top" id="return_top">
            <div class="return_top_inner">
                <div class="up_top" id="up_top"> <i class="fa fa-arrow-up"></i>
                </div>
                <div class="down_comment" id="down_comment"> <i class="fa fa-comment"></i>
                </div>
                <div class="down_refresh" id="down_refresh"> <i class="fa fa-refresh"></i>
                </div>
            </div>
        </div>
    </div>
    <!-- 取消活动确认框 -->
    <!--maquanyang 2015-10-27 新增  -->
    <div class="text_tips" id="cancelactivity_div" style="display: none; position: fixed; left: 50%;width:280px;">
        <div class="inner_text_tips">
            <div class="textarea_tips_content">
                <textarea id="cancelReason" cols="30" rows="2" style="width:260px;" maxlength="1000">活动取消了。</textarea>
                <p style="display:none;color: red;" id="cancelTip">取消活动理由不能为空</p>
            </div>
            <br/>
            <div class="text_tips_btns flexbox">
                <a id="btnConfirm1" class="btn tips_submit_btn flexItem" href="javascript:updataActivityStatus()">确定</a>
                <a id="btnCancel1" class="btn tips_cancel_btn  flexItem" href="javascript:closeMyMsgBox()">取消</a>
            </div>
        </div>
    </div>
    <div id="tips_main" class="wrap" style="display: none">
        <div class="wrap_inner">
            <div class="article_detail">
                <div class="letter_bar" id="active_userlist_count_tip">已报名(0/0人)</div>
                <div class="f-item">
                    <div class="f-add-user clearfix">
                        <div class="inner-f-add-user">
                            <div class="f-add-user-list" id="registryNames_tip">
                            </div>
                            <div class="all_pull" id="showMoreDiv" >
                                <p class="lastComment" id="moreTip" onclick="listMoreRegistry()">点击加载更多</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="footheight"></div>
        </div>
        <div class="foot_bar">
            <div class="foot_bar_inner flexbox">
                <a class="flexItem btn red_btn ask_btn prev_ask_btn" style="margin-left: 5px;margin-right: 5px;color: #ffffff" href="javascript:closeTip()">关闭</a>
            </div>
        </div>
    </div>
    <%--<div id="lotteryDetail" class="wrap" style="display:none">
        <div class="wrap_inner">
            <div class="weui_cells_title m0 bgfff">
                <div class="clearfix huojiangBox"><span class="huojiang">获奖名单</span> <a href="javascript:openLotteryDetail();" class="shuaxin">刷新</a></div>
            </div>
            <div class="weui_cells"></div>
            <div class="footheight" id="footheight" style="height: 52px;"></div>
        </div>
        <div class="foot_bar">
            <div class="foot_bar_inner flexbox">
                <a class="flexItem btn white_btn ask_btn prev_ask_btn" href="javascript: goBack();" style="margin-left: 5px;margin-right: 5px;">返回</a>
            </div>
        </div>
    </div>--%>
    <div id="qrcode-main" class="wrap" style="display: none;background-color: #fff">
        <div class="wrap_inner" >
            <div class="applyBox">
                <div class="applyPromt">
                    <div class="applyCavens" id="showQrcodeId"></div>
                </div>
                <div class="applyPromtTxt c888 mt15">活动报名页面只能在<font style="color: #37ce37;">微信客户端</font>打开<br>请用微信扫二维码打开</div>
            </div>
        </div>
    </div>
    <form id="form1" method="post" action="${baseURL}/open/activity/openActivityAction!addActivityComment.action" onsubmit="return false">
        <input type="hidden" name="userId" value="${session.userId}" />
        <input type="hidden" name="id" value="${param.id}" />
        <input type="hidden" id="sendComment" onkeydown="keyDown13();" name="tbQyActivityCommentPO.content" placeholder="说点什么吧">
        <input type="hidden" id="userIds" name="userIds">
        <input type="hidden" id="type" name="tbQyActivityCommentPO.type" value="1">
    </form>
    <%@include file="/open/activity/emoji3.jsp"%>
    <div class="awardShare">
        <div class="awardShare1"></div>
    </div>
</body>
<%@include file="/open/include/showMsg.jsp"%>
<%@include file="/manager/baidumap/selectLocation.jsp"%>
</html>
<script type="text/javascript">
    var pageSize=10; //每页显示条数
    var pageIndex=1;
    var timeid;
    var secondsBeforeFinish;
    var hasMore;
    var hasRegistry;
    var registryHasMore=false;
    var authEncodeUrl;//报名认证地址
    var detailEncodeUrl;//验证回调地址
    var openId="${session.openId}";//外部用户id
    var activitylock=true;//锁
    var activityStuct="";
    var isStart=false;
    var creator = "";
    var logo = "";
    var questionId="";
    var isCommentTip=true;
    var lotteryTemplate='<div class="item"><div class="rank">@name</div>'
            +'<img src="@picPath" alt="" /><div class="name">@remark '
            +'</div><div class="num">名额：<span>@count</span></div></div>';
    var tmplate='<li >'
            +'<p class="img"><img src=\"@headPic\" alt="" onerror=\"javascript:replaceUserHeadImage(this);\"></p>'
            +'<p class="name">@personName</p>'
            +'</li>';
    var registryTemp = ' <div class="settings-item"> '
            +'     <span class="time">@CreateTime</span> '
            +'     <div class="inner-settings-item flexbox"> '
            +'         <div class="avator">  '
            +'             <img src="@personImage" alt="" onerror="javascript:replaceUserHeadImage(this);"/> '
            +'         </div> '
            +'         <div class="title description_title flexItem ohidden"> '
            +'             <p class="name">@CreatePerson</p> '
            +'             <p class="description">@remark</p> '
            +'         </div> '
            +'     </div> '
            +' 	</div> ';
    $(document).ready(function(){
        var isComment="${param.isComment}";
        if(isComment==1){
            $("#wrap_main").hide();
            $("#open_comment_page").show();
        }
        showLoading();
        var id="${param.id}";
        var code="${param.code}";
        var isTargetActivityId="${param.isTargetActivityId}";
        showFooter(true);
        $.ajax({
            type:'post',
            url:'${baseURL}/open/activity/openActivityAction!ajaxView.action',
            data:{id:id,code:code,isTargetActivityId:isTargetActivityId,size:pageSize},
            dataType:'json',
            success:function(result){
                if("0"==result.code){
                    var info=result.data;
                    //标题
                    $("#title").html(info.tbXyhActivityPO.title);

                    //内容
                    $("#content").html(info.tbXyhActivityPO.content);
                    videoPlayback();

                    //封面
                    if(info.tbXyhActivityPO.isCover=="0"){//显示封面图片
                        if (info.tbXyhActivityPO.coverImage != "" && info.tbXyhActivityPO.coverImage != null) {
                            $("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhActivityPO.coverImage
                                    + "\" width=\"100%\" />");
                        }
                    }

                    var content=info.tbXyhActivityPO.content.replace(/http\:\/\/qy\.do1\.com\.cn\:6090\/fileweb/g,localport).replace(/@fileweb@/g,localport);
                    $("#content").html(checkURL(content,false));

                    //附件
                    if(info.mediaList.length>0){
                        $("#medialist").show();
                        previewFiles(info.mediaList,"medialist","mediaIds");
                    }

                    //发布人
                    $("#actvityCreator").html(info.actvityCreator);

                    //地点
                    $("#activityLocation").html(info.tbXyhActivityPO.activityLocation==""?"发布人暂未填写地址":info.tbXyhActivityPO.activityLocation);
                    var longitude=info.tbXyhActivityPO.longitude;
                    var latitude=info.tbXyhActivityPO.latitude;
                    if(""!=longitude&&""!=latitude){
                        $("#lag").val(longitude);
                        $("#lat").val(latitude);
                        $("#btn_openmapId").show();
                    }

                    //时间
                    var activityStart=info.tbXyhActivityPO.activityStart;
                    var activityStop=info.tbXyhActivityPO.activityStop;
                    var registryStop=info.tbXyhActivityPO.registryStop;
                    var cancelRegistryStop=info.tbXyhActivityPO.cancelRegistryStop;
                    var nowYear=new Date().toLocaleDateString().substring(0,4);
                    var startYear=activityStart.substring(0,4);
                    var stopYesr=activityStop.substring(0,4);
                    if(startYear==stopYesr&&startYear==nowYear){
                        activityStart=activityStart.substring(5,16);
                        activityStop=activityStop.substring(5,16);
                        registryStop=registryStop.substring(5,16);
                        cancelRegistryStop=cancelRegistryStop.substring(5,16);
                    }
                    $("#activityStart").html(activityStart);
                    $("#activityStop").html(activityStop);
                    $("#registryStop").html(registryStop);
                    $("#cancelRegistryStop").html(cancelRegistryStop);

                    //倒计时
                    secondsBeforeFinish = parseInt(info.secondsBeforeFinish);
                    $("#baoming").css("display", "block");
                    timeid = window.setInterval("clock()", 1000);

                    creator = info.tbXyhActivityPO.creator;
                    if(info.logo){
                        logo = info.logo;
                    }else{
                        logo = resourceURL + "/themes/qw/images/logo/logo400.png"
                    }

                    //加载评论
                    var commentCount = "暂无回复";
                    if(info.tbXyhActivityPO.commentCount>0 && info.tbXyhActivityPO.isOpenComment=='1'){
                        commentCount = "回复(<font>" + info.totalRows + "</font>)";
                        var comments = info.comments;
                        hasMore=info.hasMore;
                        appendCommnets(comments);
                        pageIndex=2;
                    }
                    $("#commentCount").html(commentCount);

                    //加载报名人数
                    var regisSum=info.tbXyhActivityPO.regis_sum;
                    $("#active_userlist_count_tip").html("已报名("+info.registryCount+"/"+regisSum+"人)");
                    $("#active_userlist_count").html("已报名("+info.registryCount+"/"+regisSum+"人)");
                    if(regisSum=="0"||regisSum==""){
                        $("#active_userlist_count_tip").html("已报名("+info.registryCount+"/不限)");
                        $("#active_userlist_count").html("已报名("+info.registryCount+"/不限)");
                    }
                    //我要报名按钮
                    /*
                    *$("#activityStart").html(info.tbXyhActivityPO.activityStart);
                     $("#activityStop").html(info.tbXyhActivityPO.activityStop);
                     $("#registryStop").html(info.tbXyhActivityPO.registryStop);
                     $("#cancelRegistryStop").html(info.tbXyhActivityPO.cancelRegistryStop);
                    */

                    var registryStop=info.tbXyhActivityPO.registryStop;
                    var activityStop=info.tbXyhActivityPO.activityStop;
                    var regisRes=info.tbXyhActivityPO.regis_res;
                    var activitySatrtStr = new Date(info.tbXyhActivityPO.activityStartStr);
                    isStart=activitySatrtStr<new Date();
                    if(info.isRegistryStop=="0"){
                        $("#regLink").hide();
                        $("#stopRegLink").show();
                    }
                    if("取消"==info.tbXyhActivityPO.activityStatus){
                        $("#finish").html("活动已取消");
                        activityStuct="取消";
                    }else if(info.isActivityStop=="0"){
                        $("#finish").html("活动已结束");
                        activityStuct="结束";
                    }/*else if("0"!=regisRes||""!=regisRes){
                        if(info.tbXyhActivityPO.regis_sum=="0"){
                            $("#regLink").html("我要报名");
                        }else{
                            $("#regLink").html("我要报名(剩余"+regisRes+"人)");
                        }
                        $("#registryBtn").show();
                    } else{
                        $("#regLink").hide();
                        $("#stopRegLink").html("报名人数已满");
                        $("#stopRegLink").show();
                    }*/


                    //加载报名人员信息
                    var registryNames=info.registryNames;
                    if(registryNames&&registryNames.length>0){
                        for(var i=0;i<registryNames.length;i++){
                            var temp=tmplate;
                            temp=temp.replace("@personName",registryNames[i].personName);
                            temp=temp.replace("@headPic",registryNames[i].headPic);
                            $("#registryNames").append(temp);
                        }
                    }else{
                        $("#registry_list").hide();
                    }

                    //开启评论框
                    if("0"==info.tbXyhActivityPO.isComment && isWeChat()){
                        /*$("#atPersonId").hide();
                        $("#imageId").hide();
                        $("#isComment_div").show();*/
                        isCommentTip=false;
                    }

                    //分享信息
                    var title=info.tbXyhActivityPO.title;
                    var img="${compressURL}"+info.tbXyhActivityPO.coverImage;
                    if(info.tbXyhActivityPO.coverImage==""){
                        img = info.logo;
                    }
                    var summary=info.tbXyhActivityPO.summary;
                    var shareUrl=window.location.href;
                    if(shareUrl.indexOf("&")>0){
                        var str=shareUrl.split("&");
                        shareUrl=str[0];
                    }
                    setDataForWeixinValue(title,img,summary,shareUrl);

                    //抽奖活动加载奖品
                    var lotteryList=info.lotteryList;
                    if(lotteryList && lotteryList.length>0){
                        //显示抽奖按钮
                        if(isStart){
                            $("#lotteryDiv").show();
                        }
                        $("#lotteryList").show();
                        $("#openLottery").show();//显示已获奖人员
                        var temp='<div class="img"><img src="${baseURL}/jsp/wap/images/prizeList_bg.png" alt="" class="vt" /></div>';
                        for(var i=0;i<lotteryList.length;i++){
                            var tempstr=lotteryTemplate;
                            tempstr=tempstr.replace("@name",lotteryList[i].name);
                            if(lotteryList[i].picPath.indexOf("images/lottery.jpg")>-1||lotteryList[i].picPath==""){
                                tempstr=tempstr.replace("@picPath","${baseURL}/jsp/wap/images/lottery.jpg");
                            }else{
                                tempstr=tempstr.replace("@picPath",compressURL+lotteryList[i].picPath);
                            }
                            tempstr=tempstr.replace("@remark",lotteryList[i].remark);
                            var count=lotteryList[i].count;
                            if(count==0){
                                count="不限";
                            }else{
                                count=count+"人";
                            }
                            tempstr=tempstr.replace("@count",count);
                            temp+=tempstr;
                        }
                        $("#lotteryList").html(temp);
                    }

                    /*生成二维码*/
                    var url=document.location.href;
                    var qrcode = new QRCode(document.getElementById("showQrcodeId"), {
                        width: 320, // 设置宽高
                        height: 320
                    });
                    qrcode.makeCode(url);
                    //$("#showQrcodeId").find("img").attr("style","margin-top:50px;margin-left:auto;margin-right:auto;");

                    //加载认证链接
                    if("2"==info.tbXyhActivityPO.isShare){
                        authEncodeUrl=info.authEncodeUrl;
                        detailEncodeUrl=info.detailEncodeUrl;
                    }

                    //判断是否已经报名
                    if(info.hasRegistry){
                        hasRegistry=info.hasRegistry;
                    }

                    //判断是否有问卷
                    if(info.questionId!=""){
                        questionId=info.questionId;
                        $("#lotteryDiv").show();
                        $("#openQuestion").show();
                    }
                    hideLoading();
                }else{
                    hideLoading();
                    _alert('提示',result.desc,"确认",function(){WeixinJS.back();});
                    restoreSubmit();
                    return;
                }
            },
            error:function(){
                hideLoading();
                _alert("提示","网络打了个盹，请重试","确认",function(){restoreSubmit();});
            }
        });

        /**上传媒体文件引入  start*/
        wxqyh.agent="activity";//应用code
        wxqyh.groupId="${param.id}";
        wxqyh.orderId="${param.id}";
        wxqyh_uploadfile.agent="activity";//应用code
        wxqyh_uploadfile.groupId="${param.id}";
        wxqyh_uploadfile.orderId="${param.id}";
        /**上传媒体文件引入  end*/


        //判断是否微信端
        if(isWeChatApp()){
            $("#positionId").show();
        }

        $(".awardShare").on("click",function(){
            $('.awardShare').hide();
        });

    });

    //倒计时
    function clock() {
        var secs, day = 0, hour = 0, min = 0, sec = 0;
        var secsOfDay = 24 * 60 * 60;
        var secsOfHour = 60 * 60;
        if (secondsBeforeFinish > 0) {
            secondsBeforeFinish--;
            secs = secondsBeforeFinish;

            day = parseInt(secs / secsOfDay);
            secs = secs % secsOfDay;

            hour = parseInt(secs / secsOfHour);
            secs = secs % secsOfHour;

            min = parseInt(secs / 60);
            sec = secs % 60;
            if(activityStuct=="取消"){
                $(".open_avator_title").css("background-color","#999");
                $(".open_avator_title h3").css("line-height","2.4");
                $("#finish").html("活动已取消");
                $("#time").remove();
            }else if(isStart){
                $(".open_avator_title h3").css("line-height","2.4");
                $("#finish").html("活动进行中");
                $("#time").remove();
            }
        } else {
            clearInterval(timeid);
            $("#finish").html("活动报名已截止");
            $("#time").remove();
            if(activityStuct=="取消"){
                $(".open_avator_title").css("background-color","#999");
                $(".open_avator_title h3").css("line-height","2.4");
                $("#finish").html("活动已取消");
            }else if(activityStuct=="结束"){
                $(".open_avator_title").css("background-color","#999");
                $(".open_avator_title h3").css("line-height","2.4");
                $("#finish").html("活动已结束");
            }
        }

        var msg = "";
        msg += day + "天";
        msg += (hour == 0 ? "00" : (hour >= 10 ? hour : ("0" + hour))) + "时";
        msg += (min == 0 ? "00" : (min >= 10 ? min : ("0" + min))) + "分";
        msg += (sec == 0 ? "00" : (sec >= 10 ? sec : ("0" + sec))) + "秒";

        $("#time").html(msg);
    }

    //加载更多
    function listMoreReply() {
        activityId = "${param.id}";
        $.ajax({
            type:"POST",
            url: "${baseURL}/open/activity/openActivityAction!listOtherActivityComments.action",
            data: "page="+pageIndex+"&id=" + activityId + "&size="+pageSize,
            cache:false,
            dataType: "json",
            success: function(data){
                lock_roll=false;
                if ("0" != data.code) {
                    showMsg("", data.desc, 1);
                    return;
                }
                var comments = data.data.comments;
                var commentCount = "回复(<font>"+data.data.totalRows+"</font>)";
                $("#commentCount").html(commentCount);
                appendCommnets(comments);
                pageIndex = data.data.currentPage+1;
                hasMore = data.data.hasMore;
                emojify.run($('.comment_list')[0]);
            }
        });
    }

    //加载评论
    function appendCommnets(comments) {
        var tempHtml='<li class="flexbox">'
                +'<div class="avator">'
                +'<img src="@headImage" width="100%" alt=""  onerror="javascript:replaceUserHeadImage(this);">'
                +'</div>'
                +'<div class="comment_content flexItem">'
                +'<h3 class="clearfix">'
                +'<span class="title">@commentUser</span>'
                +'<span class="time">@commentTime</span>'
                +'<input type="hidden" value="@commentId" />'
                +'</h3>'
                +'<p class="@class mapItem" onclick="@addressMethod">@commentContent</p></div></li>';
        var temp = "";
        for (var i = 0; i < comments.length; i++) {
            temp = tempHtml;
            var headPic = comments[i].headPic;
            if (creator == comments[i].userId) {
                headPic = logo;
            }
            temp = temp.replaceAll("@headImage", headPic);
            temp =temp.replaceAll("@userId",comments[i].userId);
            temp = temp.replaceAll("@commentUser", comments[i].userName.replaceAll(/\?{4}/, ""));
            temp =temp.replace("@commentTime",comments[i].time);
            temp =temp.replaceAll("@commentId",comments[i].commentId);
            if(comments[i].type=='2'){
                temp =temp.replace("@class","commentImg");
                temp = temp.replace("@commentContent","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
            }else if(comments[i].type=='4'){
                temp = temp.replace("@class","positionText");
                temp = temp.replace("@commentContent",comments[i].content);
                temp = temp.replace("@addressMethod","openMapForMore(this)");
            }else{
                if(comments[i].commentStatus=='4'){
                    temp =temp.replace("@class","meetingIcon icon7");
                    temp =temp.replace("@commentContent","");
                }else{
                    temp =temp.replace("@class","");
                    temp =temp.replace("@commentContent",checkURL(comments[i].content,true));
                }
            }
            $("#comments").append(temp);
        }
        emojify.run($('.comment_list')[0]);
    }

    //查看报名详情
    var registryIndex=1;
    function showRegistryDetail(){
        $("#wrap_main").hide();
        $("#tips_main").show();
        $("#registryNames_tip").html("");
        $("#moreTip").html("点击加载更多");
        registryIndex=1;
        registryHasMore=true;
        listMoreRegistry();
    }

    //加载报名人员信息
    function listMoreRegistry(){
        if(registryHasMore){
            $.ajax({
                type:"POST",
                url: "${baseURL}/open/activity/openActivityAction!getActivityRegistryPager.action",
                data: {
                    'id':"${param.id}",
                    'page' : registryIndex
                },
                cache:false,
                dataType: "json",
                success:function(result){
                    if("0"==result.code){
                        var registryNames=result.data.registryNames;
                        if(registryNames && registryNames.length>0){
                            for(var i=0;i<registryNames.length;i++){
                                var temp=registryTemp;
                                temp=temp.replace("@CreateTime",registryNames[i].createTime);
                                temp=temp.replace("@personImage",registryNames[i].headPic);
                                temp = temp.replace("@CreatePerson", registryNames[i].personName.replaceAll(/\?{4}/, ""));
                                temp=temp.replace("@remark",registryNames[i].remark);
                                $("#registryNames_tip").append(temp);
                            }
                        }
                        registryIndex++;
                        registryHasMore=result.data.hasMore;
                        if(!registryHasMore){
                            $("#moreTip").html("已没有更多");
                        }
                    }else{
                        _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                    }
                },
                error:function(){
                    _alert("提示","网络打了个盹，请重试","确认",function(){restoreSubmit();});
                }
            });
        }
    }

    //关闭报名详情
    function closeTip(){
        $("#tips_main").hide();
        $("#wrap_main").show();
    }

    //查看中奖名单
    function openLotteryDetail() {
        window.location.href = "${openURL}/open/activity/lotteryDetail.jsp?id=${param.id}&shareType=2";
    }

    //我要报名
    function registryOrNot(){
        if(isWeChat()){
            if(hasRegistry){
                //window.location.href="${openURL}/open/activity/success.jsp?id=${param.id}";
                window.location.href="${openURL}/open/activity/success.jsp?id=${param.id}";
            }else{
                if(openId){
                    window.location.href="${openURL}/open/activity/registryList.jsp?id=${param.id}";
                }else{
                    window.location.href=authEncodeUrl;
                }
            }
        }else{
            $("#wrap_main").hide();
            $("#qrcode-main").show();
        }
    }

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

        $("#qrcode-main").click(function(){
            $(this).hide();
            $("#wrap_main").show();
        });

        $("#btn_openmapId").click(function(){
            $("#openFooterContent").hide();
        });

        $(".mapItem").live('click',function(){
            $("#openFooterContent").hide();
        });

        $("#baiduMapBtn").click(function(){
            $("#openFooterContent").show();
        });

    });


    //发表评论输入框的回车键操作
    function keyDown13(){
        var event = arguments.callee.caller.arguments[0] || window.event;
        if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13。
            commitComment();
        }
    }
    //点击图片后回掉函数
    function commentBack(){
        $("#type").val("2");
        $("#sendComment").val($("#imageInput").val());
        //$("#inputDiv").val($("#imageInput").val());
        commitComment();
    }

    /**
     * 评论
     */
    function comment(){
        if(isWeChat()){
            if(isCommentTip){
                _alert("提示","活动发布人已关闭评论","确认",function(){restoreSubmit();});
                return;
            }
            if(""!=openId){
                $("#sendmsg").show();
                $("#sendmsg_spare").hide();
                $("#wrap_main").hide();
                $("#open_comment_page").show();
                closeEmoji();
            }else{
                window.location.href=detailEncodeUrl;
            }
        }
    }

    /**
     * 提交评论
     */
    function commitComment() {
        if(activitylock){
            activitylock=false;
            $("#sendmsg").hide();
            $("#sendmsg_spare").show();
            if($("#type").val()!="4"){
                var html = imgToCode();
                $("#sendComment").val(html);
            }
            var content = $("#sendComment").val();
            if (content == "") {
                activitylock=true;//
                $("#sendmsg").show();
                $("#sendmsg_spare").hide();
                return;
            }
            $("#sendComment").val(content);
            if (content.length > 300) {
                activitylock=true;//
                $("#sendmsg").show();
                $("sendmsg_spare").hide();
                showMsg("", "评论框内容长度不能超过300个字", 1);
                return;
            }
            $("#form1").ajaxSubmit({
                dataType:'json',
                data: {size:pageSize},
                type:'POST',
                async: false,
                success:function(json){
                    lock_roll=false;
                    $("#sendmsg").show();
                    $("#sendmsg_spare").hide();
                    if (json.code == "0") {
                        //清空输入框
                        $("#inputDiv").val("");
                        $("#sendComment").val("");
                        $("#userIds").val("");
                        //修改回复数
                        if(json.data.commentCount>0){
                            $("#commentCount").html("回复(<font>"+json.data.commentCount+"</font>)");
                        }
                        else{
                            $("#commentCount").html("回复(<font>0</font>)");
                        }
                        $("#comments").html("");
                        var comments = json.data.comments;
                        appendCommnets(comments);
                        pageIndex = "2";
                        hasMore = json.data.hasMore;
                        $("#down_comment").click();
                        //删除@的人员信息
                        //removeAllActived();
                        //activeRange(ranges);
                        activitylock=true;//
                        closeEmoji();
                        $("#open_comment_page").hide();
                        $("#wrap_main").show();
                        if(json.data.isApprove){
                            _alert("提示","评论正在审核中，审核通过后将会显示评论","确认",function(){restoreSubmit();});
                        }
                    } else {
                        activitylock=true;//
                        showMsg("", json.desc, 1);
                    }
                    $("#type").val("0");
                    hideLoading();
                },
                error:function() {
                    hideLoading();
                    $("#type").val("0");
                    $("#sendmsg").show();
                    $("sendmsg_spare").hide();
                    activitylock=true;//
                    $("#sendmsg").removeClass("green_btn").addClass("white_btn");
                    showMsg("", "系统繁忙，请稍后再试！", 1);
                }
            })
        }
    }

    function showPosition(){
        $(".positionInfo").show();
        getWxLocation();
    }

    function getWxLocation() {
        $("#type").val("4")
        $("#sendComment").val("未知位置<input class='lat' style='display:none' value='未知位置'/><input class='lag' style='display:none' value=''/>");
        _getLocation({ok:successCallback,fail:failCallback});

    }

    function successCallback(longitude, latitude, address, addComp){//成功获取到地理位置的方法
        $("#sendComment").val(address+"<input class='lat' style='display:none' value='"+latitude+"'/><input class='lag' style='display:none' value='"+longitude+"'/>");
        commitComment();
        $(".positionInfo ").hide();
    }
    function failCallback(error) {//获取地理位置失败后的处理方法，error为中文错误提示信息
        $(".positionInfo ").hide();
    }

    function share() {
        //$(".awardShare").show();
    }

    function openQuestionDetail(){
        document.location.href="${openURL}/open/question/detail.jsp?id="+questionId;
    }
</script>