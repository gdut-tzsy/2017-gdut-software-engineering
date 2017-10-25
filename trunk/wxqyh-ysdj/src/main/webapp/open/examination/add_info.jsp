<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/18
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
    <title>考试评测</title>
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
<body>
<div class="wrap">
    <div id="main" class="wrap_inner">
        <div class="externalExam">
            <form  id="infoForm" onsubmit="return false;">
            <input type="hidden" name="examId" value="${param.id}"/>
                <input type="hidden" id="isAgain" name="isAgain" value="0"/>
            <div class="externalExamTit">
                <h3 id="examTitle"></h3>
                <span id="examTips"></span>
            </div>
            <div class="externalExamBox" id="addInfoDiv">
                <div class="externalExamItem">
                    <div class="externalExamItemTit"><span class="mustInput">*</span><span>姓名</span></div>
                    <div class="externalExamItemInput">
                        <input type="text" name="personName" id="personName" placeholder="请输入姓名" class="examInput">
                    </div>
                </div>
                <div class="externalExamItem">
                    <div class="externalExamItemTit"><span class="mustInput">*</span><span>手机号</span></div>
                    <div class="externalExamItemInput">
                        <input type="text" name="mobile" id="mobile" placeholder="请输入手机号码" class="examInput">
                    </div>
                </div>
            </div>
            </form>
        </div>
        <div class="footheight"></div>
    </div>
    <div class="foot_bar">
        <div class="foot_bar_inner flexbox">
            <a href="javascript:commitInfo()" class="form-btn form-btn-blue flexItem">确定</a>
            <a href="javascript:cancel();" class="ml20 form-btn blackBtn flexItem">取消</a>
        </div>
    </div>
</div>
</body>

<%@include file="/jsp/wap/include/showMsg.jsp"%>
</body>
<script type="text/javascript">
    function cancel(){
        WeixinJS.back();
    }
    var tempLate='<div class="externalExamItem"><div class="externalExamItemTit">@itemName</div>'
            +'<div class="externalExamItemInput"><input type="text" name="@poName" class="examInput"></div></div>';
    var examId="${param.id}";
    var infoType="${param.type}";
    $(document).ready(function () {
        if(infoType=='0'){
            getOpenInfo();
            $("#examTips").html("登记信息，开始考试");
        }else{
            $("#examTips").html("输入姓名和手机号，查看考试结果");
        }
        $("#examTitle").html(decodeURIComponent(getParam("examName")));
    });
    function getOpenInfo(){
        $.ajax({
            url: baseURL+"/open/examination/openExamAction!getOpenInfo.action",
            type: "post",
            data: {"examId": examId},
            dataType: "json",
            success: function (result) {
                if(result.code!="0"){
                    _alert('提示', result.desc, "确认", function () {
                        //WeixinJS.back();
                    });
                    return;
                }
                var openInfoList=result.data.openInfoList;
                if(openInfoList){
                    var temp="";
                    $("#personName").attr("name","answerItemDesVO.personName");
                    $("#mobile").attr("name","answerItemDesVO.mobile");
                    if(openInfoList.length>0){
                        for(var i=0;i<openInfoList.length;i++){
                            if(openInfoList[i].isMust=='1'){
                                temp+=tempLate.replace("@itemName","<span class=\"mustInput\">*</span><span>"+openInfoList[i].content+"</span>").replace("@poName","answerItemDesVO.item"+(i+1));
                            }else{
                                temp+=tempLate.replace("@itemName",openInfoList[i].content).replace("@poName","answerItemDesVO.item"+(i+1));
                            }
                        }
                    }
                    $("#addInfoDiv").append(temp);
                }
            },
            error:function(){
                _alert('错误提示', "网络错误", "确认", function () {
                    WeixinJS.back();
                });
            }
        });
    }
    function commitInfo(){
        var isPass=true;
        $("#addInfoDiv").find(".mustInput").each(function(){
            if($(this).parent().next().find("input").val()==""){
                _alert('提示', "请输入"+$(this).next().html(),"确认");
                isPass=false;
                return false;
            }
        });
        if(!isPass){
            return;
        }
        if(!(/^[0-9-]+$/).test($('#mobile').val())){
            _alert('提示', "手机号码格式不正确","确认");
            return;
        }
        var url=baseURL+"/open/examination/openExamAction!getAnswerInfo.action";
        if(infoType=="0"){
            url=baseURL+"/open/examination/openExamAction!saveOpenInfo.action";
        }
        $.ajax({
            url:url ,
            type: "post",
            dataType: "json",
            data:$("#infoForm").serialize(),
            success: function (result) {
                if(result.code=='350013'){
                    _alert('提示', result.desc, "取消|查看结果",{'ok':function(){
                        var histroyAnswerId=result.data.histroyAnswerId;
                        window.location.href=baseURL+"/open/examination/exam_result.jsp?id="+examId+"&answerId="+histroyAnswerId;
                    },'fail':function(){
                    }});
                    return ;
                }
                if(result.code!="0"){
                    _alert('提示', result.desc, "确认", function () {
                        //WeixinJS.back();
                    });
                }else{
                    if(infoType=="0"){
                        if($("#isAgain").val()=='1'){
                            var answerId=result.data.answerId;
                            window.location.href=baseURL+"/open/examination/single_answer.jsp?id="+examId+"&answerId="+answerId;
                        }else{
                            var isExit=result.data.isExit;
                            if(isExit && isExit=='1'){
                                _alert('提示','你已提交了试卷',"再考一次|查看结果",{'ok':function(){
                                    var histroyAnswerId=result.data.histroyAnswerId;
                                    window.location.href=baseURL+"/open/examination/exam_result.jsp?id="+examId+"&answerId="+histroyAnswerId;
                                },'fail':function(){
                                    $("#isAgain").val("1");
                                    commitInfo();
                                }});
                            }else{
                                var answerId=result.data.answerId;
                                window.location.href=baseURL+"/open/examination/single_answer.jsp?id="+examId+"&answerId="+answerId;
                            }
                        }
                    }else{
                        var answerId=result.data.answerId;
                        window.location.href=baseURL+"/open/examination/exam_result.jsp?id="+examId+"&answerId="+answerId;
                    }
                }
            },
            error:function(){
                _alert('错误提示', "网络错误", "确认", function () {
                    WeixinJS.back();
                });
            }
        })
    }
</script>
</html>
