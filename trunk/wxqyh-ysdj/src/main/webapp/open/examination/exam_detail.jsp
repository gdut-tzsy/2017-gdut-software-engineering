<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/18
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>考试详情</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="${resourceURL}/themes/wap/examination/css/exam.css?ver=<%=jsVer%>">
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
</head>
<body>
    <div class="wrap">
        <div class="examTit" id="examName"></div>
        <div class="exam-total">
        	<div class="flexbox">
                <div class="flexItem exam-total-item">
                    <i class="ic_total ic_zfen"></i>
                    <div class="fl exam-total-txt">
                        <h5 id="totalScore"></h5>
                        <span>考试总分</span>
                    </div>
                </div>
                <div class="flexItem exam-total-item">
                    <i class="ic_total ic_jige"></i>
                    <div class="fl exam-total-txt">
                        <h5 id="passGrade"></h5>
                        <span>及格分</span>
                    </div>
                </div>
            </div>
            <div class="flexbox">
                <div class="flexItem exam-total-item">
                    <i class="ic_total ic_tishu"></i>
                    <div class="fl exam-total-txt">
                        <h5 id="questcount"></h5>
                        <span>总题数</span>
                    </div>
                </div>
                <div class="flexItem exam-total-item">
                    <i class="ic_total ic_time"></i>
                    <div class="fl exam-total-txt">
                        <h5 id="duration"></h5>
                        <span>考试时长</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="examDetail">
            <div class="exam-item clearfix">
                <p>
                    <label>考试时间：</label>
                    <span id="startTime"></span> 至 <span id="lastStartTime"></span>
                </p>
                <p>
                    <label>可考次数：</label>
                    <span id="maxTimes"></span>
                </p>
            </div>
            <div class="exam-bottom">
                <p id="examExplain"></p>
                <div id="images">

                </div>
            </div>
            <div class="footheight"></div>
        </div>
        <div class="foot_bar">
            <div class="foot_bar_inner flexbox">
                <a href="javascript:gotoAnswer();" class="form-btn form-btn-blue flexItem" id="gotoExam_div" style="display: none">考试</a>
                <%--<a href="javascript:gotoAnswer();" class="form-btn form-btn-red flexItem" id="goingExam_div" style="display: none"></a>--%>
                <a href="javascript:void(0);" class="form-btn form-btn-gray flexItem" id="noneExam_div" style="display: none">考试未开始</a>
                <a href="javascript:void(0);" class="form-btn form-btn-gray flexItem" id="ExamIsEnd_div" style="display: none">考试已结束</a>
                <a href="javascript:gotoResutl();" class="form-btn blueBtn flexItem" id="answerExam_div" style="display: none">查看结果</a>
                <a href="javascript:gotoAnswer();" class="form-btn blackBtn flexItem" id="goonExam_div" style="display: none">再次考试</a>
                <a href="javascript:gotoResutl();" class="ml20 form-btn blueBtn flexItem" id="answerExam1_div">查看结果</a>
            </div>
        </div>
    </div>
    <%@include file="/jsp/wap/include/showMsg.jsp"%>
</body>
<script type="text/javascript">
    var examId="${param.id}";
</script>
<script src="${baseURL}/themes/open/examination/js/exam_detail.js?ver=<%=jsVer%>"></script>
</html>
