<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/20
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>考试结果</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${resourceURL}/themes/wap/examination/css/exam.css?ver=<%=jsVer%>">
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
</head>
<body>
<div class="wrap" id="examResult_div">
    <div class="result_top" id="result_title">
        <img src="" width="232" alt="" id="result_img">

        <ul class="flexbox mt10">
            <li class="flexItem" id="showRight">
                <span class="c333" id="rightCount"><i class="cblue"></i></span>
                <p>答对</p>
            </li>
            <li class="flexItem" id="show_score" style="display:none">
                <span class="cred" id="score"></span>
                <p>得分</p>
            </li>
            <li class="flexItem">
                <span class="c333" id="times"></span>
                <p>用时</p>
            </li>
        </ul>
    </div>
    <div class="result_card">
        <p>答题卡</p>
        <ul class="cardList clearfix" id="resutl_list">
        </ul>
    </div>
</div>
<%@include file="answer_analysis.jsp" %>
<%@include file="/jsp/wap/include/showMsg.jsp"%>
<%@include file="/open/include/footer.jsp"%>
</body>
<script type="text/javascript">
    var examId="${param.id}";
    var answerId="${param.answerId}";
</script>
<script src="${baseURL}/themes/open/examination/js/exam_result.js?ver=<%=jsVer%>"></script>
</html>
