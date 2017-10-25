<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>答题结束</title>
		<meta name="description" content="">
		<meta name="HandheldFriendly" content="True">
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
		<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
		<%@include file="/open/include/showMsg.jsp"%>

		<script type="text/javascript" language="javascript">
			$(document).ready(function(){
				showLoading();
				//获取问卷填写完成提示语
				$.ajax({
					type:"POST",
					url: "${baseURL}/open/questionnair/openQuestionnairAction!searchQuestionnair.action",
					data: {id:"${param.id}"},
					cache:false,
					dataType: "json",
					success: function(data){
						//问卷已删除
						if("1999" == data.code){
							_alert("提示", data.desc, "确认",function(){WeixinJS.back();});
							return;
						}
						if ("0" != data.code) {
							hideLoading();
							_alert("提示", data.desc,"确认",function(){WeixinJS.back();});
							return;
						}

						var info = data.data.tbQuestionnaireInfoPO;
						//设置问卷填写完成提示语
						if(info.answerOverMsg==undefined||info.answerOverMsg==""){
							$("#questionnaireAnswerOverMsg").text("问卷已提交，感谢您的参与");
						}else{
							$("#questionnaireAnswerOverMsg").text(info.answerOverMsg);
						}

						hideLoading();
					},
					error:function() {
						hideLoading();
						_alert("提示", "系统繁忙，请稍后再试！","确认", function(result){});
					}
				});
			});
		</script>
	</head>
	<body>
		<div id="wrap_main" class="wrap bgfff">
			<div id="main" class="wrap_inner">
				<div class="wj_center">
					<div class="">
						<img src="images/ic_questionnaire_end.png" width="169px" />
						<div class="fz15 c666 mt10" id="questionnaireAnswerOverMsg"></div>
					</div>
					<div class="btn qwui-btn_default mt25" onclick="searchQuestionnaire();">返回问卷详情</div>
					<div class="btn qwui-btn_default mt10" onclick="searchMyAnswer();">查看我的答题</div>
				</div>
			</div>
			<%@include file="/open/include/footer.jsp"%>
		</div>
		<script type="text/javascript">
		function searchQuestionnaire(){
			window.location.href = "${baseURL}/open/question/detail.jsp?id=${param.id}";
		}
		function searchMyAnswer(){
			window.location.href = "${baseURL}/open/question/my_answer.jsp?id=${param.id}";
		}
		showFooter(true);
		</script>
	</body>
</html>