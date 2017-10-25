<%@ page contentType="text/html; charset=UTF-8" %>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="canshare" id="canshareId" content="1">
    <meta name="collectUrl" id="collectUrlId" content="${localport}"></meta>
    <title>答题/投票</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <meta name="shareImgUrl" id="shareImgUrlId" content=""/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>"/>
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>

</head>
<body>
<div id="wrap_main" class="wrap">
    <div id="mainId" class="wrap_inner">
        <div id="main" class="article_detail"></div>
        <div class="footheight mt30"></div>
    </div>
    <%@include file="/open/include/footer.jsp"%>
    <div class="foot_bar">
        <div class="foot_bar_inner flexbox" id="btn_div" style="padding: 8px 15px;">
            <a class="flexItem btn" href="javascript:nextQuestion();">下一题</a>
        </div>
    </div>
</div>
<div id="option_wrap_main" class="wrap" style="display: none;">
    <div class="wrap_inner">
        <div id="option_main"></div>
        <div class="footheight mt30"></div>
    </div>
    <div class="foot_bar">
        <div class="foot_bar_inner flexbox" id="option_btn_div" style="padding: 8px 15px;">
        </div>
    </div>
</div>
</body>
<%@include file="/open/include/showMsg.jsp"%>
<script type="text/javascript">
    showFooter(true);
    var tempRadioHtml = ''
            + '<div class="question_detail" id="question@index" style="display: none"> '
            + '	<div class="inner_question_detail">'
            + '		<div class="ask_info">'
            + '			<div class="fz14">第@questionNum题 <span class="new-tag">@type</span></div>'
            + '			<p>@tile</p>'
            + '			<div class="choiceBox">'
            + '				<div class="img ohidden" id="imglist@index">'
            + '				</div>'
            + '			</div>'
            + '			<p >调查总人数： @allCount人</p>'
            + '		</div>'
            + '		<div class="letter_bar">选项</div>'
            + '		<div class="radio_btn">'
            + '			<ul class="choiceBox">@radioOption</ul>'
            + '		</div>'
            + '	</div>'
            + '</div>';
    var tempRadioOption = ''
            + '<li >'
            + '	<div class="" >'
            + '		<div class="text" onclick="showOptionChooseUser(\'@questionId\',\'@optionId\')" >@optionName</div>'
            + '		<div class="img ohidden" id="radio_@optionId" ></div>'
            + '	</div>'
            + '	<div class="new-progress"><span style="width:@percent%"></span></div>'
            + '	<div class="">选择人数： <span>@userCount；</span>占 <span>@percent%</span> </div>'
            + '</li>';

    var tempCommentHtml = ''
            + '<div class="question_detail" id="question@index" style="display: none">'
            + '	<input id="hasMore@index" type="hidden" value="@index|@questionId|@pageIndex">'
            + '	<div class="inner_question_detail">'
            + '		<div class="ask_info">'
            + '			<div class="fz14">第@questionNum题 <span class="new-tag">问答</span></div>'
            + '			<p>@tile</p>'
            + '			<div class="choiceBox">'
            + '				<div class="img ohidden" id="imglist@index">'
            + '				</div>'
            + '			</div>'
            + '			<p >调查总人数： @allCount人</p>'
            + '		</div>'
            + '		<div class="letter_bar">答案</div>'
            + '		<div class="comment_list clearfix">'
            + '			<ul>@radioOption</ul>'
            + '		</div>'
            + '	</div>'
            + '	<div class="all_pull">@loadMore</div>'
            + '</div>';

    var tempCommentOption = '<li class="flexbox">'
            + '<div class="avator">'
            + '<img src="@headImage" onerror="javascript:replaceUserHeadImage(this);">'
            + '</div>'
            + '<div class="comment_content flexItem">'
            + '<h3 class="clearfix">'
            + '<span class="title">@personName</span><span class="time">@answerTime</span>'
            + '</h3><p>@content</p></div>'
            + '</li>';

    var backBtn = '<a class="flexItem btn qwui-btn_default mr10" href="javascript:backQuestion();">上一题</a>';
    var nextBtn = '<a class="flexItem btn" href="javascript:nextQuestion();">下一题</a>';
    var saveBtn = '<a class="flexItem btn qwui-btn_default" href="javascript:history.back();">返回</a>';
    //var saveBtn='<a class="flexItem btn white_btn ask_btn next_ask_btn" style="background: none repeat scroll 0 0 #ff6000;" href="javascript:history.back();">关闭</a>';
    var reg33 = new RegExp("\r\n", "g");
    var reg11 = new RegExp("\r", "g");
    var reg22 = new RegExp("\n", "g");
    var questionCount = 0;//总的题目数量
    var currentIndex = 1;
    var joinNum = '${param.joinNum}';//答卷人数
    var surveyType = '${param.type}';//是否为匿名问卷
    var id ="${param.id}"

    $(document).ready(function () {
        agentCode = "survey";
        searchData();
    });
</script>
<script src="${baseURL}/themes/open/question/js/single_answer_detail.js?ver=<%=jsVer%>"></script>
</html>