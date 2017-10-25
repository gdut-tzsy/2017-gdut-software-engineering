<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/18
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>考试</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${resourceURL}/themes/wap/examination/css/exam.css?ver=<%=jsVer%>">
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${resourceURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>

</head>
<body>
<div id="show_questions">
    <div class="wrap">
        <input type="hidden" id="answer_stauts" value="0">
        <form action="${baseURL}/open/examination/openExamAction!saveExamAnswer.action" id="questionform">
            <input name="examId" type="hidden" value="${param.id}">
            <input name="answerId" type="hidden" value="${param.answerId}">
            <div class="answer-progress">
                <div class="progress">
                    <span class="fz18" id="questionNum"></span>
                    <span class="fz16 c666" style="position: absolute;right: 15px;display: none" id="examtimer"><i id="examHour">00</i>:<i id="examMinute">00</i>:<i id="examSecond">00</i></span>
                </div>
                <div class="progress-bar">
                    <span style="width: 0%"></span>
                </div>
            </div>
            <div class="answer-detail" id="questionlist_div">
            </div>
        </form>
        <div class="footheight"></div>
    </div>
    <div class="answer-bottom flexbox" id="btn_div">
        <a href="javascript:void(0);" class="flexItem btn_pre cgray">上一题</a>
        <a href="javascript:showAnswerCard();" class="flexItem c666">答题卡</a>
        <a href="javascript:nextQuestion();" class="flexItem btn_next">下一题</a>
    </div>
</div>
<%@include file="answer_card.jsp" %>
<%@include file="/jsp/wap/include/showMsg.jsp"%>
<%@include file="/open/include/uploadImage.jsp"%>
</body>
<script type="text/javascript">
    var examId="${param.id}";
    var answerId="${param.answerId}";
    $(document).ready(function () {
        if($("#answer_stauts").val()=="1"){
            _alert("", "你已完成考试", "确定", function () {
                window.location.href = "${baseURL}/open/examination/exam_detail.jsp?id="+examId;
            });
        }else {
            searchExamQestion(examId,answerId);
        }
    })
//    chooseImageTypes=['camera'];
</script>
<script src="${baseURL}/themes/open/examination/js/single_answer.js?ver=<%=jsVer%>"></script>
</html>
