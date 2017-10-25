<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

<head>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>获奖名单</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <script type="text/javascript" src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
</head>

<body>
    <div id="lotteryDetail" class="wrap">
        <div class="wrap_inner">
            <div class="awardTit"><span>获奖名单</span></div>
            <div class="footheight"></div>
        </div>
        <div class="awardFoot">
            <div class="awardFootInner flexbox">
                <a id="activityDetail" style="display: none;" href="javascript:showDetail('${param.id}');" class="flexItem"><i class="awardFoot-icon1"></i><span>活动详情</span></a>
                <a href="javascript:showLotteryDetail();" class="flexItem"><i
                        class="awardFoot-icon2"></i><span>刷新</span></a>
                <a href="javascript:showShareTip();" class="flexItem"><i class="awardFoot-icon3"></i><span>分享</span></a>
            </div>
        </div>
        <div class="awardShare">
            <div class="awardShare1"></div>
        </div>
    </div>
    <%@include file="/open/include/showMsg.jsp"%>
</body>

</html>
<script type="text/javascript">
    var id = "${param.id}";
    var lotteryInfoTemp='<div class="awardList">' +
                            '<div class="awardListTit"> ' +
                            '<div class="clearfix"><span class="fl">@lotteryName</span><i class="fr">@lotteryCount</i></div>' +
                            '<p>@reMark</p> ' +
                             '</div>'+
                            '<ul></ul>'+
                        '</div>';
    var lotteryTempHtml='<li> ' +
                            '<span class="award-col1">@index</span> '+
                             '<span class="award-col2"><img src="@headPic" onerror="javascript:replaceUserHeadImage(this)"/></span>'+
                             '<span class="award-col3">' +
                                '<p class="c333 fz16">@personName</p>' +
                                '<p class="mt5 c999 fz14">' +
                                    '<span>@mobile</span>' +
                                    '<span class="ml5">@departmentName</span>' +
                                '</p>' +
                             '</span> '+
                        '</li>';
    var shareType="";
    $(document).ready(function(){
        showLoading();
        showLotteryDetail();
        $(".awardShare").click(function(){
            $(".awardShare").hide();
        })

    });

    function showLotteryDetail() {
        $.ajax({
            type:"POST",
            url: "${baseURL}/open/activity/openActivityAction!getLotteryUserList.action",
            dataType: "json",
            data: {id: id},
            success : function(result) {
                if ("0" == result.code) {
                    var lotteryList=result.data.lotteryList;
                    var lotterUserList = result.data.lotterUserList;
                    $(".awardList").remove();
                    if(lotteryList&&lotteryList.length>0){
                        for(var i=0;i<lotteryList.length;i++){
                            item=lotteryInfoTemp;
                            item=item.replace("@lotteryName",lotteryList[i].name);
                            item=item.replace("@lotteryCount",lotteryList[i].lotteryCount+'人');
                            item=item.replace("@reMark",lotteryList[i].remark);
                            $(".footheight").before(item);
                            var index=1;
                            for(var j=0;j<lotterUserList.length;j++){
                                if(lotteryList[i].id==lotterUserList[j].lotteryId){
                                    var vo = lotterUserList[j];
                                    item = lotteryTempHtml;
                                    item = item.replace("@index",index);
                                    item = item.replace("@personName", vo.userName.replaceAll(/\?{4}/, ""));
                                    item = item.replace("@headPic",vo.headPic);
                                    item = item.replace("@mobile",vo.mobile==""?"无":vo.mobile);
                                    item = item.replace("@departmentName",vo.departmentName);
                                    $(".wrap_inner>.awardList:last>ul").append(item);
                                    lotterUserList.splice(j,1);
                                    index++;
                                    j--;
                                }
                            }
                        }
                    }
                    shareType=result.data.shareType;
                    if(shareType!='0'){
                        $("#activityDetail").show();
                    }
                    //设置分享title,img,summary,shareUrl
                    var title = result.data.title + "中奖名单";
                    var img = "${compressURL}" + result.data.coverImage;
                    if ("" == result.data.coverImage) {
                        img = result.data.logo;
                    }
                    var summary = result.data.summary;
                    var shareUrl = window.location.href;
                    setDataForWeixinValue(title, img, summary, shareUrl);
                    hideLoading();
                } else{
                    hideLoading();
                    _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                }
            },
            error : function() {
                hideLoading();
                _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
            }
        });
    }

    function showDetail(id){
        var referrer = document.referrer;
        if (-1 != referrer.indexOf("activity_detail.jsp?id=" + id)) {
            window.history.back(-1);
        }else if("2"==shareType){
            window.location.href = '${openURL}' + '/open/activity/detail2.jsp?id=' + id;
        } else {
            window.location.href = '${openURL}' + '/open/activity/detail.jsp?id=' + id;
        }
    }

    function showShareTip() {
        $(".awardShare").show();
    }
</script>
