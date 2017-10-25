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
<body ontouchstart>
<!-- <div id="wrap_main" class="wrap"> -->
<form action="${baseURL}/open/questionnair/openQuestionnairAction!saveAnswer.action" id="questionform">
    <input name="id" type="hidden" value="${param.id}"/>
    <div id="wrap_main" class="wrap">
        <div class="wrap_inner">
            <div id="main"></div>
            <div class="footheight mt30"></div>
        </div>
        <div class="foot_bar" style="position:fixed;">
            <div class="foot_bar_inner flexbox" id="btn_div" style="padding: 8px 15px;">
                <a class="flexItem btn" href="javascript:nextPage()">下一页</a>
            </div>
        </div>
    </div>
    <%@include file="/open/include/footer.jsp"%>
</form>
<!--  </div>-->
<%@include file="/open/include/showMsg.jsp"%>
<script type="text/javascript">
    showFooter(true);
    var pagesHtml = ''
            + '<div class="question_detail" id="pageIndex@index" style="display: none;">'
            + '	<input type="hidden" value="@isMust"  id="isMust@index" style="display:none">'
            + '	<input type="hidden" value="@multiple"  id="multiple@index" style="display:none">'
            + '	<div class="inner_question_detail">'
            + '		<div class="fz14 p10 pb0">共@count题   当前@questionNum题</div>'
            + '		<div>@questionHtml</div>'
            + '	</div>'
            + '</div>';
    var tempRadioHtml = ''
            + '<div class="choiceItem"> '
            + '	<div class="choiceTit">@index、@tile<span class="red" style="@display">*</span><span class="new-tag ml10">单选</span>'
            + '		<div class="choiceBox"><div class="img ohidden"> @ImgList </div></div>'
            + '		<div><span class="choice_error"></span></div>'
            + '	</div>'
            + '	<div class="radio_btn" id="question@index" >'
            + '		<ul class="choiceBox many"  >@radioOption</ul>'
            + '	</div>'
            + '</div>';
    var tempRadioOption = ''
            + '<li class="flexbox" onclick="radioSelect(this);" >'
            + '	<input type="radio" id="@optionId" value="@optionId" name="searchValue.radio_@questionId" style="display:none">'
            + '	<div class="xian_option"><i class="fa"></i></div>'
            + '	<div class="ml10 flexItem">'
            + '		<div class="text">@optionName</div>'
            + '		<div class="img ohidden" id="radio_@optionId" > @ImgList </div>'
            + '		<div class="mt10" style="@display"><input type="text" class="input" onblur="upperCase(this,\'@optionId\')" /></div>'
            + '	</div>'
            + '</li>';
    var tempMultipleHtml = ''
            + '<div class="choiceItem"> '
            + '	<div class="choiceTit">@index、@tile（@optionNumber）<span class="red" style="@display">*</span><span class="new-tag ml10">多选</span>'
            + '		<div class="choiceBox"><div class="img ohidden"> @ImgList </div></div>'
            + '		<div><span class="choice_error"></span></div>'
            + '	</div>'
            + '	<div class="multiple_btn" id="question@index">'
            + ' 		<input name="optionRange" type="hidden" id="optionRange@index" value="@optionMin,@optionMax,0" />'
            + '		<ul class="choiceBox many">@radioOption</ul>'
            + '	</div>'
            + '</div>';
    var tempMultipleOption = ''
            + '<li class="flexbox" onclick="mutipleSelect(this);">'
            + '	<input type="checkbox" id="@optionId" value="@optionId" name="searchValue.multiple_@questionId"style="display:none">'
            + '	<div class="xian_option"><i class="fa"></i></div>'
            + '	<div class="ml10 flexItem">'
            + '		<div class="text">@optionName</div>'
            + '		<div class="img ohidden" id="radio_@optionId"> @ImgList </div>'
            + '		<div class="mt10" style="@display"><input type="text" class="input" onblur="upperCase(this,\'@optionId\')" onclick="disabledInput()"/></div>'
            + '	</div>'
            + '</li>';
    var tempCommentHtml = ''
            + '<div class="choiceItem"> '
            + '	<div class="choiceTit">@index、@tile<span class="red" style="@display">*</span><span class="new-tag ml10">问答</span>'
            + '		<div class="choiceBox"><div class="img ohidden"> @ImgList </div></div>'
            + '		<div><span class="choice_error"></span></div>'
            + '	</div>'
            + '	<div class="ask_textarea_content">'
            + '		<div class="ask_textarea_content_inner">'
            + '			<textarea class="item-select inputStyle" id="question@index"  onkeyup="hasContent(this,@isMust)" name="searchValue.comment_@questionId" style="width: 100%; height: 120px;" placeholder="请输入回答..."></textarea>'
            + '		</div>'
            + '	</div>'
            + '</div>';
    var backBtn = '<a class="flexItem btn qwui-btn_default mr10" href="javascript:backPage();">上一页</a>';
    var nextBtn = '<a class="flexItem btn" href="javascript:nextPage();">下一页</a>';
    var saveBtn = '<a class="flexItem btn" href="javascript:saveQuestion();">提交</a>';
    var reg33 = new RegExp("\r\n", "g");
    var reg11 = new RegExp("\r", "g");
    var reg22 = new RegExp("\n", "g");
    var pagesCount = 0;//总页数
    var pagesIndex = 1;//当前页数
    var questionCount = 0;//总的题目数量
    var questionLock = true;//提交锁
    var openId="${session.openId}";//外部用户id
    var code="${param.code}";
    var id="${param.id}";

    $(document).ready(function () {
        setInterval("getUserInfo();", 10 * 60 * 1000);//10分钟执行一次
        wxqyh.agent = "survey";
        searchData();
    });
</script>
<script src="${baseURL}/themes/open/question/js/more_answer.js?ver=<%=jsVer%>"></script>
</body>
</html>