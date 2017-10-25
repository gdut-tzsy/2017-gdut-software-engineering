<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="canshare" id="canshareId" content="1">
	<meta name="collectUrl" id="collectUrlId" content="${localport}"></meta>
	<title>详情</title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta name="shareImgUrl" id="shareImgUrlId" content="">
		<link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

		<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
		<style>
		.inputTaskStop{
		      border: 1px solid #ccc;
  			  padding: 0 10px;
		}
		.icon_editTask::after {
		  content: "";
		  font-family: FontAwesome;

		}
		.icon_editTask {
		  display: inline-block;
		  height: 15px;
		  right: 0;
		  text-align: right;
		  top: 0;
		  width: 30px;
		  z-index: 12;
		  text-decoration: none;
		  color: #999;
		  font-size: 18px;
		}
		</style>
</head>

<body>
	<div id="wrap_main" class="wrap">
		<div id="main" class="wrap_inner">
			<div class="article_detail">
				<div class="article-detail">
					<div class="detail-title" id="detailTitle"></div>
					<div class="detail-small-title">
						<span id="createtimetxt"></span>
						<span id="personNametxt"></span>
					</div>
					<div class="shuxing-box shuxing-box-size4">
						<div class="shuxing-item flexbox">
							<div class="shuxing-title">问卷类型</div>
							<div class="shuxing-value flexItem"><span id="surveyType"></span><span class="applyStatus status_gray" id="close_time" style="display: none;"></span></div>
						</div>
						<div class="shuxing-item flexbox">
							<div class="shuxing-title">查看权限</div>
							<div class="shuxing-value flexItem" id="permission"></div>
						</div>
						<div class="shuxing-item flexbox">
							<div class="shuxing-title">截止时间</div>
							<div class="shuxing-value flexItem">
								<span class="detailStop" id="endtimetxt"></span>
								<a class="icon_detail_edit ml5" id="showStopBtn" href="javascript:showStop()" style="display: none;"></a>
							</div>
						</div>
						<div class="shuxing-item flexbox">
							<div class="shuxing-title">题目数量</div>
							<div class="shuxing-value flexItem">
								<span class="detailStop" id="questioncount"></span>
							</div>
						</div>
						<div class="shuxing-item flexbox" id="departmentDiv" style="display:none;" onclick="openDetail();">
							<div class="shuxing-title">调查对象</div>
							<div class="shuxing-value flexItem" id="rangeId">所有人</div>
						</div>
					</div>
					<div class="detail-content article_content">
						<div id="detailContent"></div>
						<div id="topImage" style="display:none"></div>
					</div>
					<div class="white-bar mt15" id="joinCount"></div>
				</div>
				<div class="form_btns mt20 mb20">
					<div class="inner_form_btns">
						<div class="fbtns flexbox">
							<a id="toAnswerId" class="flexItem btn" href="javascript:toAnswer();" style="display: none;">开始答题/投票</a>
							<a id="hasAnswerId" class="flexItem btn qwui-btn_default" href="javascript:searchMyAnswer();" style="display: none;">查看我的答题</a>
						</div>
						<div class="fbtns flexbox mt10">
							<a class="flexItem btn qwui-btn_default" href="javascript:searchAnswer();" id="searchAnswerId" style="display: none;" >查看结果</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%@include file="/open/include/footer.jsp"%>
	</div>
	<!-- 关闭确认框 -->     <!--修改截止时间  maquanyang 20154 0722  -->
<%--<div class="text_tips" id="task_stop_div"
	style="display: none; position: fixed; left: 50%;width:280px;">
	<div class="inner_text_tips">
		<div class="textarea_tips_content" >
		<div class="f-item" id="scoreAlert" style="">
                <div class="pt10 pl5 pb15">新的截止时间：<input type="text" name="taskStop" style="width:50%;" id="taskStop" value="" placeholder="请选择日期" readonly="readonly" class="item-input inputStyle inputTaskStop" /></div>
        </div>
		<p style="display:none;color: red;" id="closeTipTaskStop">新的截止时间不能小于当前时间</p>
		</div>

		<div class="text_tips_btns flexbox">
			<a id="taskStopBtnConfirm1" class="btn tips_submit_btn flexItem" href="javascript:editTaskStop()">确定</a>
			<a id="taskStopBtnCancel1" class="btn tips_cancel_btn  flexItem" href="javascript:cancelShowStop()">取消</a>
		</div>
	</div>
</div>--%>
	<div class="popBox2" id="task_stop_div" style="display: none;">
		<div class="task_etexta">
			<div class="" style="text-align: left;">
				<div class="pt10 pl5 pb15">新的截止时间：<input type="text" name="taskStop" style="width:50%;" id="taskStop" value="" placeholder="请选择日期" readonly="readonly" class="item-input inputStyle inputTaskStop" /></div>
			</div>
			<p style="display:none;color: red;" id="closeTipTaskStop">新的截止时间不能小于当前时间</p>
		</div>

		<div class="popBox_foot flexbox">
			<a id="taskStopBtnCancel1" class="popBox_cancel_btn flexItem" href="javascript:cancelShowStop()">取消</a>
			<a id="taskStopBtnConfirm1" class="popBox_submit_btn flexItem" href="javascript:editTaskStop()">确定</a>
		</div>
	</div>
	<!-- 需要在此，引入 以下两个jsp，特别注意必须在<div id="main" class="wrap_inner">下加入以下include-->
	<%@include file="/open/include/showMsg.jsp"%>
</body>

</html>
<script type="text/javascript">
var activitylock=true;//上锁
var hasAnswered = false;
var type="";//问卷是否为匿名文件
var authEncodeUrl;//报名认证地址
var moreAnswerUrl;//验证回调地址
var openId="${session.openId}";//外部用户id
var code="${param.code}";
var id="${param.id}";

var imageURl = '<img src="@imageURl" alt="" onerror="javascript:replaceUserHeadImage(this);">';

showFooter(true);

var template2 = ''
	+'<li class="ccli" style="display:@Show;height: 45px;line-height:15px;margin-bottom:20px"> '
	+'    <a class="remove_icon" href="javascript:void()"></a> '
	+'    <p class="img"> '
	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
	+'    </p> '
	+'    <p class="name">@UserName</p> '
	+'</li>';
	var joinNum=0;
	var answerType="";
$(document).ready(function () {
	agentCode = "survey";
	var id="${param.id}";
	showLoading();
    $.ajax({
        type:"POST",
        url: "${baseURL}/open/questionnair/openQuestionnairAction!searchQuestionnairDetail.action",
        data: {id:id,code:code},
		async : false,
        dataType: "json",
        success: function(data){
        	//已删除
        	if("1999" == data.code){
				hideLoading();
				_alert("提示", data.desc, "确认",function(result){WeixinJS.back();});
                return;
        	}
			if("10001" == data.code){
				hideLoading();
				window.location.replace(baseURL+"/open/question/detail.jsp?id="+id);
				return;
			}
            if ("0" != data.code) {
				hideLoading();
                _alert("提示", data.desc,"确认", function(result){WeixinJS.back();});
                return;
            }

            var info = data.data.detail;
			openId = data.data.openId;
			var picList = data.data.picList;
            $("title").html(info.title);
			//分享信息时，若有消息封面，则使用封面，若无且号为vip时，封面图为该企业logo，否则，默认为企微logo
			var title=info.title;
			var img="";
			if(undefined!=picList[0]&&undefined!=picList[0].picPath){
				img="${compressURL}"+picList[0].picPath;
			}else{
				var logo=data.data.logo;
				if(data.data.isVip&&undefined!=logo) {
					img = logo;
				}
			}
			var summary=info.summary;
			var shareUrl = window.location.href.split("&code")[0];
			moreAnswerUrl = data.data.moreAnswerUrl;
			setDataForWeixinValue(title,img,summary,shareUrl);

            type=info.type;
            answerType=info.answerType;
            //设置信息本身的信息
            if(type=="1"){
            	$("#surveyType").html("匿名问卷");
            }else{
            	$("#surveyType").html("非匿名问卷");
            }
            if(info.permission=="0"){
            	$("#permission").html("仅发布人查看结果");
            }else{
            	$("#permission").html("所有人查看结果");
            }
            if(info.headPic){
            	imageURl = imageURl.replace("@imageURl",info.headPic);
            }else{
            	imageURl = imageURl.replace("@imageURl",data.data.logo);
            }

        	$("#personNametxt").html(info.personName);
            $(".avator").html(imageURl);
            $("#createtimetxt").html(info.lastEditTime);
            $("#detailTitle").html(info.title);

			var summary = info.summary.toString();
            $("#detailContent").html(checkURL(summary,true));
            $("#questioncount").html(info.questionNum+"题");
            $("#endtimetxt").html(info.endTime);
            $("#taskStop").val(info.endTime);

            $("#joinCount").html("目前参与人数："+info.joinNum);
            joinNum=info.joinNum;
            $("#active_userlist_count").html("问卷调查对象("+info.ext2+"人)");
            $("#active_userlist_count_tip").html("问卷调查对象("+info.ext2+"人)");
          	//显示图片

			if(picList&&picList.length>0){
				$("#topImage").show();
				for(var i=0;i<picList.length;i++){
					$("#topImage").append("<img src=\"${compressURL}"+picList[i].picPath+"\" style=\"width:100%\" />");
					//	+ " onclick='showImage(\"${localport}" + picList[i].picPath + "\");' style=\"width:100%\" />");
				}
			}
            var isEndTimeOut = info.isEndTimeOut;
            var hasAnswered = info.answer == "1" ? true : false;
            var isCreateUser = info.createUser;
            var isSurveyUser = info.surveyUser;
            var hasPermission = info.permission == "1" ? true : false;

            if(isCreateUser){
            	$("#showStopBtn").show();//设置关闭时间
            	$("#copyQuestionnair_div").show();	//显示复制问卷按钮
            	$("#searchAnswerId").show();//查询问卷详情
            	if(isEndTimeOut){

            		$("#close_time").show();
            		if(info.status=="2"){
						$("#close_time").html("已关闭");
            		}else{
						$("#close_time").html("已结束");
            		}
        			if(hasAnswered){
        				$("#hasAnswerId").show();
        			}
            	}else{
            		$("#remind_btn").show();	//显示提醒未参与人按钮
            		$("#close_btn").show(); //显示关闭问卷按钮
            		if(hasAnswered){
        				$("#hasAnswerId").show();
        			}else if(isSurveyUser){
        				$("#toAnswerId").show();
        			}
            	}
            	$("#departmentDiv").show();
            	var range = info.ext1;
          	    if (range == "1") {
		        	$("#rangeId").html("所有人");
		        } else if (range == "2") {
		        	$("#rangeId").html("本部门");
		        } else {
		        	$("#rangeId").html("特定对象");
		        }
            	showTargetDetail(info.deptIds, info.userIds, info.ext1);
            }else if(isSurveyUser){
            	if(isEndTimeOut){
            		$("#close_time").show();
            		if(info.status=="2"){
						$("#close_time").html("已关闭");
            		}else{
						$("#close_time").html("已结束");
            		}
        			if(hasAnswered){
        				$("#hasAnswerId").show();
        				if(hasPermission){
            				$("#searchAnswerId").show();//查询问卷详情
            			}
        			}
            	}else{
            		if(hasAnswered){
            			$("#hasAnswerId").show();
            			if(hasPermission){
            				$("#searchAnswerId").show();//查询问卷详情
            			}
            		}else{
            			$("#toAnswerId").show();
            		}
            	}
            }

			hideLoading();
        },
		error : function() {
			hideLoading();
			_alert("提示",internetErrorMsg,"确认",function(){var cc;});
		}
    });

  	//hejinjiao 2016-01-14 截止时间控件
    opt.preset='datetime';//调用日历显示  日期时间
    $('#taskStop').mobiscroll(opt);//直接调用日历 插件
    $('#taskStop').on('focus', function(){
    	//关闭输入法
    	$("#title").css("ime-mode","disabled");
    	$("#content").css("ime-mode","disabled");

    });
});
</script>
<script src="${baseURL}/themes/open/question/js/detail.js?ver=<%=jsVer%>"></script>