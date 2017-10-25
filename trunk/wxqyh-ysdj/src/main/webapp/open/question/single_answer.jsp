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
    <form action="${baseURL}/open/questionnair/openQuestionnairAction!saveAnswer.action" id="questionform">
        <input name="id" type="hidden" value="${param.id}"/>
        <div class="wrap_inner">
            <div id="main" class="article_detail"></div>
            <div class="footheight mt30"></div>
        </div>
    </form>
    <%@include file="/open/include/footer.jsp"%>
    <div class="foot_bar">
        <div class="foot_bar_inner flexbox" id="btn_div" style="padding: 8px 15px;">
            <a class="flexItem btn" href="javascript:nextQuestion();">下一题</a>
        </div>
    </div>

</div>
<%@include file="/open/include/showMsg.jsp"%>
<script type="text/javascript">
    showFooter(true);
    var tempRadioHtml = ''
            + '<div class="question_detail" id="question@index" style="display: none"> '
            + '	<div class="inner_question_detail">'
            + '		<div class="ask_info">'
            + '			<div class="fz14">第@questionNum题 <span class="new-tag">单选</span></div>'
            + '			<p>@tile</p>'
            + '			<div><span class="choice_error"></span></div>'
            + '			<div class="choiceBox">'
            + '				<div class="img ohidden" id="imglist@index">'
            + '				</div>'
            + '			</div>'
            + '		</div>'
            + '		<div class="letter_bar">选项</div>'
            + '		<div class="radio_btn">'
            + '			<ul class="choiceBox">@radioOption</ul>'
            + '		</div>'
            + '	</div>'
            + '</div>';
    var tempRadioOption = ''
            + '<li class="flexbox" onclick="radioSelect(this);">'
            + '	<input type="radio" id="@optionId" value="@optionId" name="searchValue.radio_@questionId" style="display:none">'
            + '	<div class="xian_option">'
            + '		<i class="fa"></i>'
            + '	</div>'
            + '	<div class="ml10 flexItem">'
            + '		<div class="text">@optionName</div>'
            + '		<div class="img ohidden" id="radio_@optionId"></div>'
            + '		<div class="mt10" style="@display"><input type="text"  class="input"  onblur="upperCase(this,\'@optionId\')" /></div>'
            + '	</div>'
            + '</li>';
    var tempMultipleHtml = ''
            + '<div class="question_detail" id="question@index" style="display: none"> '
            + '	<div class="inner_question_detail">'
            + '		<div class="ask_info">'
            + '			<div class="fz14">第@questionNum题 <span class="new-tag">多选</span></div>'
            + '			<p>@tile</p>'
            + '			<div><span class="choice_error"></span></div>'
            + '			<div class="choiceBox">'
            + '				<div class="img ohidden" id="imglist@index">'
            + '				</div>'
            + '			</div>'
            + '		</div>'
            + '		<div class="letter_bar">选项（@optionNumber）</div>'
            + '		<div class="multiple_btn">'
            + '		<input name="optionRange" type="hidden" id="optionRange@index" value="@optionMin,@optionMax,0" />'
            + '			<ul class="choiceBox">@radioOption</ul>'
            + '		</div>'
            + '	</div>'
            + '</div>';
    var tempMultipleOption = ''
            + '<li class="flexbox" onclick="mutipleSelect(this);"> '
            + '	<input type="checkbox" id="@optionId" value="@optionId" name="searchValue.multiple_@questionId"style="display:none">'
            + '	<div class="xian_option">'
            + '		<i class="fa"></i>'
            + '	</div>'
            + '	<div class="ml10 flexItem">'
            + '		<div class="text">@optionName</div>'
            + '		<div class="img ohidden" id="radio_@optionId" ></div>'
            + '		<div class="mt10" style="@display"><input type="text"  class="input" onblur="upperCase(this,\'@optionId\')" onclick="disabledInput()"/></div>'
            + '	</div>'
            + '</li>';
    var tempCommentHtml = ''
            + '<div class="question_detail" id="question@index" style="display: none">'
            + '	<div class="inner_question_detail">'
            + '		<div class="ask_info">'
            + '			<div class="fz14">第@questionNum题 <span class="new-tag">问答</span></div>'
            + '			<p>@tile</p>'
            + '			<div class="choiceBox">'
            + '				<div class="img ohidden" id="imglist@index">'
            + '				</div>'
            + '			</div>'
            + '			<div><span class="choice_error"></span></div>'
            + '		</div>'
            + '		<div class="letter_bar">答案</div>'
            + '		<div class="ask_textarea_content">'
            + '			<div class="ask_textarea_content_inner">'
            + '				<textarea class="item-select inputStyle" name="searchValue.comment_@questionId" onkeyup="hasContent(this)"  style="width: 100%; height: 120px;" placeholder="请输入回答..."></textarea>'
            + '			</div>'
            + '		</div>'
            + '	</div>'
            + '</div>';
    var backBtn = '<a class="flexItem btn qwui-btn_default mr10" href="javascript:backQuestion();">上一题</a>';
    var nextBtn = '<a class="flexItem btn" href="javascript:nextQuestion();">下一题</a>';
    var saveBtn = '<a class="flexItem btn" href="javascript:saveQuestion();">提交</a>';
    var reg33 = new RegExp("\r\n", "g");
    var reg11 = new RegExp("\r", "g");
    var reg22 = new RegExp("\n", "g");
    var questionCount = 0;//总的题目数量
    var currentIndex = 1;//当前题目
    var questionLock = true;//提交锁
    var lessThanMin = false;//是否最少
    var id="${param.id}"
    $(document).ready(function () {
        setInterval("getUserInfo();", 10 * 60 * 1000);//10分钟执行一次
        wxqyh.agent = "survey";
        searchData();
    });
</script>
<script src="${baseURL}/themes/open/question/js/single_answer.js?ver=<%=jsVer%>"></script>
</body>
</html>
