<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/20
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<div class="wrap" id="answerAnalysis_div" style="display: none">
    <div class="return_bar" id="answerType_div">

    </div>
    <div class="answer-detail">
        <div id="questionlist_div">

        </div>
        <div class="footheight"></div>
        <div class="answer-bottom flexbox" id="btn_div">
            <a href="javascript:void(0);" class="flexItem btn_pre cgray">上一题</a>
            <a href="javascript:showAnswerResutl();" class="flexItem c666">答题卡</a>
            <a href="javascript:nextAnswer();" class="flexItem btn_next">下一题</a>
        </div>
    </div>
</div>

<script type="text/javascript">

var weida='<span class="c999"><i class="icon icon_weida"></i>未答</span>';
var dadui='<span class="cblue"><i class="icon icon_dadui"></i>答对了</span>';
var dacuo='<span class="cred"><i class="icon icon_dacuo"></i>答错了</span>';
var bufenzhengque='<span class="cred"><i class="icon icon_dacuo"></i>部分正确</span>';
var bukejian='<span class="c999"><i class="icon icon_weida"></i></span>';
var tempQuestionHtml=''
        +'<div class="answer-item" id="question@index"> '
        +'  <div class="answerTit">'
        +'      <i>@QuestionType</i>@QuestionTitle（）'
        +'      <div class="answerTitImg" id="imglist@QuestionId"></div>'
        +'  </div>'
        +'  <ul class="answerOptions">@answerOptions</ul>'
        +'  <div class="answer" style="@display">'
        +'      <h5 class="cblue">正确答案 @answerIndex</h5>'
        +'      <h5>答题解析</h5>'
        +'      <p>@answerRemark</p>'
        +'  </div>'
        +'</div>';
var tempOptionHtml=''
        +'<li class="flexbox@class">'//@class:active_right(答对);active_error(答错)
        +'  <div class="xian_option"><i class="ic_option">@letter</i></div>'
        +'  <div class="ml10 flexItem">'
        +'      <div class="text">@optionName</div>'
        +'      <div class="answerTitImg" id="radio_@optionId"></div>'
        +'  </div>'
        +'</li>';
//问答题
var tempCommentHtml=''
        +'<div class="answer-item" id="question@index">'
        +'  <div class="answerTit">'
        +'     <i>问答题</i>@QuestionTitle '
        +'      <div class="answerTitImg" id="imglist@questionId"></div>'
        +'   </div>'
        +'  <div class="ask_textarea_content">'
        +'      <textarea class="item-select inputStyle" readonly="readonly" id="@questionId" style="width: 100%; height: 145px;" placeholder="未答"></textarea>'
        +'      <div class="answerTitImg" id="answer_@questionId"></div>'
        +'  </div>'
        +'  <div class="answer" style="@display">'
        //+'      <h5 class="cblue">正确答案 @answerIndex</h5>'
        +'      <h5>答案</h5>'
        +'      <p>@answerRemark</p>'
        +'  </div>'
        +'</div>';
var fillQuestionHtml = ''
        +'<div class="answer-item" id="questionIndex@index">'
        +'  <div class="answerTit">'
        +'      <i>填空题</i>@QuestionTitle'
        +'      <div class="answerTitImg" id="imglist_@questionId"></div>'
//        +'      <input type="hidden" name="searchValue.fill_@questionId" id="fill_@questionId" readonly="readonly">'
        +'  </div>'
        +'  <div class="" id="fillOption_@questionId" style="padding: 0 15px;">@fillOption'
        +'  </div>'
        +'  <div class="answer" style="@display">'
        +'      <h5 class="cblue">正确答案</h5> @answerIndex'
        +'      <h5>答案解析</h5>'
        +'      <p>@answerRemark</p>'
        +'  </div>'
        +'</div>';
var fillOptionHtml = '' +
        '  <div class="externalExamItemTit" style="margin-bottom: 5px">'
        +'     <div class="externalExamItemTit">填空项@index</div>'
        +'     <div class="externalExamItemInput">'
        +'         <input type="text" readonly="readonly" name="fillOntion" class="examInput" onblur="fill(this,\'@number\')">'
        +'     </div>'
        +'  </div>';

</script>