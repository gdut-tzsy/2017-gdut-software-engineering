<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html> 
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    	<meta name="canshare" id="canshareId" content="1">
        <title>活动详情</title>
        <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
        <meta name="description" id="descriptionId" content="">
        <meta name="shareImgUrl" id="shareImgUrlId" content="">
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>

		<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<!-- 评论发表图片所需要         start  -->
		<script src='${baseURL}/jsp/wap/js/comment.js?ver=<%=jsVer%>'></script>
		<!-- 评论发表图片所需要         end  -->
		<!-- 上传媒体文件（手机端页面）引入  start -->
	    <script src="${resourceURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
		<!-- 上传媒体文件（手机端页面）引入  end -->
    	<script type="text/javascript" src="${resourceURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
</head>
<body>

    <div id="main" class="wrap_inner">
        <div class="article_detail" style="background: #f5f5f5">
			<div class="article-detail" style="padding-bottom: 5px">
				<div class="detail-title"><span class="" id="title"></span><span class="checkStatus statusimg" style="margin-top: 5px" id="title_state">进行中</span></div>
				<div class="detail-small-title">
					<span id="detailTime"></span>
					<span id="actvityCreator"></span>
				</div>
				<div class="shuxing-box shuxing-box-size4">
					<div class="shuxing-item flexbox">
						<div class="shuxing-title">活动时间</div><div class="shuxing-value flexItem"><span id="activityStart"></span> 至 <span id="activityStop"></span></div>
					</div>
					<div class="shuxing-item flexbox">
						<div class="shuxing-title">报名截止</div><div class="shuxing-value flexItem" ><span id="time"></span></div>
					</div>
					<div class="shuxing-item flexbox">
						<div class="shuxing-title">活动地点</div><div class="shuxing-value flexItem" ><span id="activityLocation"></span></div>
					</div>
					<div class="shuxing-item flexbox">
						<div class="shuxing-title">参与对象</div><div class="shuxing-value flexItem" ><span id="rangeId">所有人</span></div>
					</div>
					<div class="shuxing-item flexbox">
						<div class="shuxing-title">活动费用</div><div class="shuxing-value flexItem" ><span id="paySpanId"></span></div>
					</div>
					<div class="shuxing-item flexbox" id="registry_remark" style="display: none">
						<div class="shuxing-title">报名备注</div><div class="shuxing-value flexItem" ><span ></span></div>
					</div>
				</div>

				<div class="detail-content article_content">
					<p id="topImage">
						<!--<img id="coverImage" src="" width="100%" />  -->
					</p>
					<p id="content"></p>
					<div class="prizeList" id="lotteryList" style="display:none; margin-bottom: 0"></div>
				</div>
				<!-- 上传媒体文件（手机端页面）引入  start -->
				<div class="form-style" id="medialist" style="display:none; margin-bottom: 20px">
					<div class="letter_bar file_top borderBottommNone"><span class="file_top_tit">附件(0)</span></div>
				</div>
				<div class="process_tit" id="openLottery" onclick="openLotteryDetail()" style="display:none;width: 100%;" ><i class="icon-lottery"></i>查看获奖名单</div>
				<div class="process_tit" id="shareDiv" onclick="share('${param.id}');" style="display:none;width: 100%;">分享到外部</div>

				<!-- 上传媒体文件（手机端页面）引入  end -->
			</div>
			<input id="longitude" type="hidden"/>
			<input id="latitude" type="hidden"/>
			<input id="payType" type="hidden" value="0" />
			<input id="payCost" type="hidden" value="0" />
			<input id="payWay" type="hidden" value="0" />
			<input id="anonymous" type="hidden" value="0" />
            <div class="comments-box" id="comments-box">
                <div class="letter_bar" id="commentCount" style="display:none">回复(0)</div>
                <div class="comment_list clearfix">
                    <ul id="comments">
                    </ul>
                </div>
            </div>
        </div>
        <%@include file="/open/include/footer.jsp"%>
    </div>
<div class="share_bg"></div>
	    <div class="share_box">
	        <div class="share1"></div>
	        <div class="share2"></div>
	    </div>
<%@include file="/open/include/showMsg.jsp"%>
<%@include file="/manager/baidumap/selectLocation.jsp"%>
<%@include file="/jsp/wap/include/target_detail.jsp" %>
    </body>
</html>

<script type="text/javascript">
	document.documentElement.addEventListener('touchend',function(){
	    $('.share_bg,.share_box').hide();
	})

	$(".share_bg,.share_box").on("click",function(){
		$('.share_bg,.share_box').hide();
	})

	var activitylock=true;//我要报名上锁
	var goodlock=true;//赞上锁
	var secondsBeforeFinish;
	var activityId;
	var hasRegistry;
	var timeid;
	var hasFavour;
	var pageIndex = "2";
	var pageSize=10;//每页显示条数
	var ranges="";
	var commentSum=0;
	var hasMore;
	var address="";
	var payStatus=false;//支付状态
	var logo="";
	var creator="";
	var isManager="";
	$(document).ready(function () {
		var isOpentype='${param.type}';
    	/*if("1"==isOpentype){
    		$('.share_bg,.share_box').show();
    	}*/
		agentCode = "activity";
		showLoading();
    	var id="${param.id}";
        $.ajax({
            type:"POST",
            url: "${baseURL}/open/activity/openActivityAction!ajaxView.action",
            data: {id:id,size:pageSize},
            dataType: "json",
            success: function(data){
            	//活动已删除
            	if("1999" == data.code){
            		hideLoading();
            		showMsg("", data.desc, 1,{ok:function(result){WeixinJS.back();}});
                    return;
            	}
                if ("0" != data.code) {
    				hideLoading();
                    showMsg("", data.desc, 1,{ok:function(result){WeixinJS.back();}});
                    return;
                }
            
                var info = data.data;
                
                if(info.isShowFooter==true){
                	showFooter(true);
                }
				var newDate=new Date().Format("yyyy-MM-dd hh:mm");
				var actDate=new Date(info.tbXyhActivityPO.activityStart).Format("yyyy-MM-dd hh:mm");
				var stopDate=new Date(info.tbXyhActivityPO.activityStop).Format("yyyy-MM-dd hh:mm");
				if(stopDate<newDate){
					$("#title_state").removeClass("statusimg").addClass("statused").html("已结束");
				}
                //抽奖活动勾选了不需要报名，处理与报名相关的内容
                var isLottery=0;
            	if(info.tbXyhActivityPO.activityType=='1' && info.tbXyhActivityPO.lotteryUser=='0'){
            		isLottery=1;
            	}else{
            		$("#registryStopDiv").show();
					$("#cancelRegistryStopDiv").show();
            		$("#active_userlist_count").show();
            		$("#timeout_div").show();
            	}
                if(info.tbXyhActivityPO.status=="1"){
                	hideLoading();
                	showMsg("", "该活动已删除", 1,{ok:function(result){WeixinJS.back();}});
                	return;
                }
                document.getElementById("descriptionId").content="活动:" + info.tbXyhActivityPO.summary;
                
                var commentCount = "暂无回复";
                if(info.tbXyhActivityPO.commentCount>0 && info.tbXyhActivityPO.isOpenComment=='1'){
                	$("#commentCount").show();
                	$("#getmore_comment").show();
                    $("#commentCount2").html(info.tbXyhActivityPO.commentCount);
					commentCount = "回复(<font>" + info.totalRows + "</font>)";
                }else{
                	$("#getmore_comment").hide();
                }
                $("#title").html(info.tbXyhActivityPO.title);


				//处理跨年活动
				var activityStart=info.tbXyhActivityPO.activityStart;
				var activityStop=info.tbXyhActivityPO.activityStop;
				var registryStop=info.tbXyhActivityPO.registryStop;
				var cancelRegistryStop=info.tbXyhActivityPO.cancelRegistryStop;
				var nowYear=new Date().toLocaleDateString().substring(0,4);
				var startYear=activityStart.substring(0,4);
				var stopYesr=activityStop.substring(0,4);
				if(startYear==stopYesr&&startYear==nowYear){
					activityStart=activityStart.substring(5,16);
					activityStop=activityStop.substring(5,16);
					registryStop=registryStop.substring(5,16);
					cancelRegistryStop=cancelRegistryStop.substring(5,16);
				}
				if(info.tbXyhActivityPO.isSend=="1"){
					$("#registryStop").html(registryStop+" | 截止前提醒");
				}else{
					$("#registryStop").html(registryStop);
				}
				$("#activityStart").html(activityStart);
				$("#activityStop").html(activityStop);
				$("#cancelRegistryStop").html(cancelRegistryStop);
                if(info.tbXyhActivityPO.isComment!="1"){
					$("#comments-box").show();
	            	$("#getmore_comment").show();
	            	$("#isComment_div").show();
	            	$("#height_div").show();
				}
                
                if(info.tbXyhActivityPO.isCover=="0"){//显示封面图片
	                if (info.tbXyhActivityPO.coverImage != "" && info.tbXyhActivityPO.coverImage != null) {
	                	$("#topImage").append("<img id=\"coverImage\" src=\"${compressURL}" + info.tbXyhActivityPO.coverImage 
	                			+ "\" width=\"100%\" />");
	                }
                }	
               	var imagesList = info.images;
               	if (imagesList != null && imagesList.length > 0) {
               		for (var i = 0; i < imagesList.length; i ++) {
               			$("#topImage").append("<img id=\"coverImage" + i + "\" src=\"${compressURL}" + imagesList[i].path 
               					+ "\" width=\"100%\" />");
               		}
               	}
				
               	$("title").html(info.tbXyhActivityPO.title);
              //分享信息
				var title=info.tbXyhActivityPO.title;
				var img="${compressURL}"+info.tbXyhActivityPO.coverImage;
				if(info.tbXyhActivityPO.coverImage==""){
					img = info.logo;
				}
				var summary=info.tbXyhActivityPO.summary;
				var shareUrl=window.location.href;
				if(shareUrl.indexOf("&")>0){
					var str=shareUrl.split("&");
					shareUrl=str[0];
				}
				setDataForWeixinValue(title,img,summary,shareUrl);
				var content=info.tbXyhActivityPO.content.replace(/http\:\/\/qy\.do1\.com\.cn\:6090\/fileweb/g,localport).replace(/@fileweb@/g,localport);
                /* 手机端发布的 */
				if(info.tbXyhActivityPO.contentType=='来自手机'){
					$("#content").html(checkURL(content,true));
				}else{
					$("#content").html(checkURL(content,false));
				}
				videoPlayback();
                $("#anonymous").val(info.tbXyhActivityPO.isAnonymous);
                if(info.tbXyhActivityPO.isAnonymous=='1'){
	                $("#isAnonymousSet").show();
                }
                //显示活动目标对象
                ranges=info.tbXyhActivityPO.ranges;
                if(ranges=='1'){
                	$("#rangeId").html("所有人");
                }else if(ranges=='2'){
                	$("#rangeId").html("本部门");
                }else if(ranges=='3'){
                	$("#rangeId").html("特定对象");
                } 
                showTargetDetail(info.tbXyhActivityPO.departids,info.tbXyhActivityPO.sendTarget,ranges);
                $("#actvityCreator").html(data.data.actvityCreator);
				$("#detailTime").html(data.data.tbXyhActivityPO.createTime);
                $("#activityLocation").html(info.tbXyhActivityPO.activityLocation==""?"发布人暂未填写地址":info.tbXyhActivityPO.activityLocation);
                address=info.tbXyhActivityPO.activityLocation;
                $("#latitude").val(info.tbXyhActivityPO.latitude);
                $("#longitude").val(info.tbXyhActivityPO.longitude);
                
                if( $("#latitude").val()=="" || $("#longitude").val()==""){
                	$("#btn_openmapId").hide();
                }else{
                	$("#btn_openmapId").show();
                }
                $("#commentCount").html(commentCount);
                
                //支付设置
                $("#payType").val(info.tbXyhActivityPO.payType);
                $("#payWay").val(info.tbXyhActivityPO.payWay);
                $("#payCost").val(info.tbXyhActivityPO.payCost);
                var strPay="";
                if("1"==info.tbXyhActivityPO.payType){
                	strPay="AA付款";
                	strPay=strPay+"<span class='payPrice'>"+info.tbXyhActivityPO.payCost/100+"元</span>";
                	$("#registryBox_payCost").html(info.tbXyhActivityPO.payCost/100+"元");
                	if("1"==info.tbXyhActivityPO.payWay){
                		strPay=strPay+" | "+"微信支付";
						$("#registry_AAXianxia").hide();
						$("#btn_registry").html("立即报名（微信支付）");
                	}else{
                		strPay=strPay+" | "+"线下支付";
						$("#registry_AAXianxia").show();
                	}
					$("#registryBox_AAPay").show();	
					$("#payDetailDiv").show();
					$("#isMoneySet").show();
                }else if("2"==info.tbXyhActivityPO.payType){
                	strPay="自由付款";
                	if("1"==info.tbXyhActivityPO.payWay){
                		strPay=strPay+" | "+"微信支付";
						$("#registry_FreeXianxia").hide();
						if(info.actvityRegistry && info.actvityRegistry.payCost!='0'){
							$("#payCost").val(info.actvityRegistry.payCost);
						}
						$("#registry_payCostInputDiv").show();
						$("#btn_registry").html("立即报名（微信支付）");
                	}else{
                		strPay=strPay+" | "+"线下支付";
						$("#registry_FreeXianxia").show();
						$("#registry_payCostInputDiv").hide();
					}
					$("#registryBox_FreePay").show();
					$("#isMoneySet").show();
                }else{
                	strPay="不需费用";
                }
                $("#paySpanId").html(strPay);
                if(data.data.payStatus && data.data.payStatus==true){
                	 payStatus=true;
                }
                if(isLottery!='1'){//抽奖活动不需要报名信息
                	var strBaoMing="";                
                    if(info.tbXyhActivityPO.regis_sum=='0'){ //当选择参与对象为所有人的时候
                    	strBaoMing="("+info.registryCount+"/"+info.regis_sum+"人)";
                    	$("#active_userlist_count").html("已报名("+info.registryCount+"/"+info.regis_sum+"人)");
                        $("#active_userlist_count_tip").html("已报名("+info.registryCount+"/"+info.regis_sum+"人)");
                    }else{ 
                    	strBaoMing="("+info.registryCount+"/"+info.tbXyhActivityPO.regis_sum+"人)";
                    	$("#active_userlist_count").html("已报名("+info.registryCount+"/"+info.tbXyhActivityPO.regis_sum+"人)");
                    	$("#active_userlist_count_tip").html("已报名("+info.registryCount+"/"+info.tbXyhActivityPO.regis_sum+"人)");
                    } 
                    if(info.registryCount==0){
                    	$("#registry_list").hide();
                    	$("#btn_registryList").hide();
                    }else{
                    	$("#registry_list").show();
                    	$("#btn_registryList").show();
                        /* var registryNames = info.registryNames;
                        var ulEle = $("#registryNames");
                        var ulEleTip = $("#registryNames_tip");
                        var tempTip = "";
                        if(registryNames.length>4){
                        	$("#registryA").show();
                        }
                        for (var i = 0; i < registryNames.length; i++) {
                        	 if(registryNames[i].isAnonymous=='1'){
	                        	tempTip = "<li>"+
	                        	"<p class=\"img\"><img src=\""+registryNames[i].headPic+"\" alt=\"\"   onerror=\"javascript:replaceUserHeadImage(this);\"/></p>"+
	                    		"<p class=\"name\">"+registryNames[i].userName+"</p></li>";
                        	 }else{
                        		 tempTip = "<li onclick=\"atThisPersonUtil('"+registryNames[i].userId+"','"+registryNames[i].userName+"','"+registryNames[i].headPic+"')\">"+
 	                        	"<p class=\"img\"><img src=\""+registryNames[i].headPic+"\" alt=\"\"   onerror=\"javascript:replaceUserHeadImage(this);\"/></p>"+
 	                    		"<p class=\"name\">"+registryNames[i].userName+"</p></li>";
                        	 }
                        	if(i<4){
                                ulEle.append(tempTip);
                        	}
                        	ulEleTip.append(tempTip);
                        } */
                    }
                }

				creator = info.tbXyhActivityPO.creator;
				isManager = info.isManager;
				if(info.logo){
					logo = info.logo;
				}else{
					logo = resourceURL + "/themes/qw/images/logo/logo400.png"
				}

                var comments = info.comments;
                appendCommnets(comments);
                pageIndex = "2";
                
                hasMore = info.hasMore;
                secondsBeforeFinish = parseInt(info.secondsBeforeFinish);
                if("取消" != info.tbXyhActivityPO.activityStatus && isLottery!='1'){//maquanyang 2015-10-27 已取消的活动不能再报名
	                if (secondsBeforeFinish > 0) {
	                    $("#baoming").css("display", "block");
	                    timeid = window.setInterval("clock()", 1000);
	
	                    activityId = info.tbXyhActivityPO.activityId;
	                    hasRegistry = info.hasRegistry ;
	                   $("#regLink").show();//maquanyang 2015-10-29 报名按钮默认隐藏，使用格式才显示
	                    if(info.isActionObj=="0"){
	                    	if (hasRegistry=='0') {//取得名额，但未支付,显示支付按钮
	                    		$("#gotopay").show();
	                    	}
	                        if (hasRegistry=='0' || hasRegistry=='1') {
	                    		$("#regLink").attr("href","javascript:registryOrNot()");
	                    		$("#regLink").addClass("red_btn");
	                            $("#regLink").html("取消报名");
	                            
	                            $("#registry_remark").show();
	                            var tem = info.actvityRegistry.remark==""?"您未填写报名备注信息":info.actvityRegistry.remark;
	                            $("#registry_remark").html("<i class=\"fa fa-edit\"></i><span>	报名备注："+tem+"</span>");
	                        } else {
	                        	if(info.tbXyhActivityPO.regis_sum=='0'){
	                        		$("#regLink").html("我要报名");
	                        		$("#regLink").removeClass("red_btn");
	                        		$("#regLink").attr("href","javascript:registryOrNot()");
	                        	}
	                        	else{
	                        		if(info.tbXyhActivityPO.regis_res!='0'){
		                            	$("#regLink").html("我要报名(剩余"+info.tbXyhActivityPO.regis_res+"人)");
		                            	$("#regLink").removeClass("red_btn");
	                        			$("#regLink").attr("href","javascript:registryOrNot()");                       			
	                        		}
	                        		else{
	                        			$("#regLink").html("报名名额已满");                        			
	                        		}
	                        	}
	                        }
	                    }else if(info.isActionObj=="88"){
	                    	$("#regLink").html("你是该活动发起者");
	                    	$("#regLink").hide();
	                    }else{
	                    	$("#regLink").html("你不是该活动的目标对象");
	                    }
	                    
	                }
	                else{
	                   $("#baoming").css("display", "block");
						$("#time").html("活动报名已截止");
	        	       //$("#time").parent().remove();
	                   $("#timeoutfrom_div").html("<div class=\"timeout-bar\">活动报名已截止："+info.tbXyhActivityPO.registryStop+"</div>");
	                }
                }else if(isLottery!='1'){//隐藏报名按钮,显示活动已取消
                	$("#baoming").css("display", "block");
					//$("#finish").html("<font color=\"red\">活动已取消</font>");
					("#title_state").removeClass("statusimg").addClass("statused").html("已取消");
                	$("#regLink").hide(); 
                	$("#time").parent().remove();
                    $("#timeoutfrom_div").html("<div class=\"timeout-bar\">活动已取消</div>");
                }

				if(info.mediaList.length>0){
					$("#medialist").show();
					previewFiles(info.mediaList,"medialist","mediaIds");
				}
                $("#aLink").attr("href","minaim-xyh://url.do1.com/${localport}/wap/component/activity/activity/activity_detail.jsp?id=${param.id}");
				downloadUrl="${localport}/wap/about/index.jsp?openurl="+$("#aLink").attr("href");
				hideLoading();
				
				if("${param.src}"!=""){
					$("#down_comment").click();
				} 
				
				//maquanyang 2015-10-27 判断是否显示取消活动按钮
				if(info.tbXyhActivityPO.creator=="${session.userId}" && "取消" != info.tbXyhActivityPO.activityStatus){//创建人才能取消并且未取消的才能取消
					var nowDate = new Date();
				    var activityStart = info.tbXyhActivityPO.activityStartStr;
				    if(activityStart && null != activityStart && "" != activityStart){
				    	var activityStartDate = new Date(activityStart.replace("-", "/").replace("-", "/"));
				    	if(nowDate.getTime() < activityStartDate.getTime()){
				    		if("none" == $("#regLink").css("display")){
				    			$("#cancelActivity").css("margin-left","0px");
				    		}
				    		$("#cancelActivity").show();
				    		$("#cancelActivity").attr("href","javascript:showBox();");
				    	}
				    }
				}
				if ('1' == isLottery) {
					$("#openLottery").show();//显示查看获奖人员
				}
            },
            error:function() {
				hideLoading();
                showMsg("", "网络打了个盹，请重试", 1);
            }
        });    

		/**上传媒体文件引入  start*/
	    wxqyh_uploadfile.agent="activity";//应用code
		/**上传媒体文件引入  end*/
    });
	function openDetail() {
			$("#wrap_main").hide();
			$("#target_main").show();
	}
	//显示全部相关人
	function showAllcc(){
		var p = $("#ccPersonList").find("li");
		for(var i=0;i<p.length;i++){
			$(p[i]).show();
		}
		$("#ccmoreDiv").hide();
	}
	//显示全部部门
	function showAlldepart(){
		var p = $("#toDepartmentList").find("li");
		for(var i=0;i<p.length;i++){
			$(p[i]).show();
		}
		$("#departmoreDiv").hide();
	}
	//显示全部特定人员
	function showAllperson(){
		var p = $("#toPersonList").find("li");
		for(var i=0;i<p.length;i++){
			$(p[i]).show();
		}
		$("#personmoreDiv").hide();
	}
	//查看报名信息
	function searchRegistry(){
		$("#wrap_main").hide();
		$("#tips_main").show();
	}
	//关闭查看报名信息
	function closeTip(){
		$("#wrap_main").show();
		$("#tips_main").hide();
	}
	function clock() {
	    var secs, day = 0, hour = 0, min = 0, sec = 0;
	    var secsOfDay = 24 * 60 * 60;
	    var secsOfHour = 60 * 60;
	    if (secondsBeforeFinish > 0) {
	        secondsBeforeFinish--;
	        secs = secondsBeforeFinish;
	        
	        day = parseInt(secs / secsOfDay);
	        secs = secs % secsOfDay;
	        
	        hour = parseInt(secs / secsOfHour);
	        secs = secs % secsOfHour;
	        
	        min = parseInt(secs / 60);
	        sec = secs % 60;
	    } else {
	       clearInterval(timeid);
			$("#time").html("活动报名已截止");
	       //$("#time").parent().remove();
           $("#timeoutfrom_div").html("<div class=\"timeout-bar\">活动报名已截止："+info.tbXyhActivityPO.registryStop+"</div>");
	    }
	
	    var msg = "";
	    msg += day + "天";
	    msg += (hour == 0 ? "00" : (hour >= 10 ? hour : ("0" + hour))) + "时";
	    msg += (min == 0 ? "00" : (min >= 10 ? min : ("0" + min))) + "分";
	    msg += (sec == 0 ? "00" : (sec >= 10 ? sec : ("0" + sec))) + "秒";

		$("#time").html("剩余 "+msg);
	}
	
	//发表评论输入框的回车键操作
	function keyDown13(){
		var event = arguments.callee.caller.arguments[0] || window.event;
		if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13。
			commitComment();
		}
	}
	//点击图片后回掉函数
	function commentBack(){
		$("#type").val("2");
		//$("#sendComment").val($("#imageInput").val());
		$("#inputDiv").val($("#imageInput").val());
		commitComment();
	}
	/**
	 * 评论
	 */
	function commitComment() {
    	if(activitylock){
    		activitylock=false;
    		$("#sendmsg").hide();
    		$("sendmsg_spare").show();
	    	var html = imgToCode();
	    	$("#sendComment").val(html);
	        var content = $("#sendComment").val();
	        if (content == "") {
                activitylock=true;//
                $("#sendmsg").show();
        		$("sendmsg_spare").hide();
	            return;
	        }
	        $("#sendComment").val(content);
	        if (content.length > 300) {
                activitylock=true;//
                $("#sendmsg").show();
        		$("sendmsg_spare").hide();
                showMsg("", "评论框内容长度不能超过300个字", 1);
	            return;
	        }
	        $("#form1").ajaxSubmit({
	            dataType:'json',
	            data: {size:pageSize},
	            type:'POST',
	            async: false,
	            success:function(json){
	            	lock_roll=false;
           		 	$("#sendmsg").show();
         			$("#sendmsg_spare").hide();
	                if (json.code == "0") {
	                	//清空输入框
						$("#inputDiv").val("");
	        			$("#sendComment").val("");
	           			$("#userIds").val("");
	           			//修改回复数
	           			if(json.data.commentCount>0){
		                    $("#commentCount").html("回复(<font>"+json.data.commentCount+"</font>)");
	           			}
	           			else{
		                    $("#commentCount").html("回复(<font>0</font>)");
	           			}
	                    $("#comments").html("");
	                    var comments = json.data.comments;
	                    appendCommnets(comments);
	                    pageIndex = "2";
	                    hasMore = json.data.hasMore;
	                    $("#down_comment").click();
	                    //删除@的人员信息
	                    removeAllActived();
	                    activeRange(ranges);
		                activitylock=true;//
		    		    closeEmoji();
	                } else {
		                activitylock=true;//
	                    showMsg("", json.desc, 1);
	                }
	                $("#type").val("0");
	    		    $("#sendmsg").removeClass("green_btn").addClass("white_btn");
	    		    hideLoading();
	            },
	            error:function() {
	            	hideLoading();
	            	$("#type").val("0");
           		 	$("#sendmsg").show();
         			$("sendmsg_spare").hide();
	                activitylock=true;//
	    		    $("#sendmsg").removeClass("green_btn").addClass("white_btn");
	                showMsg("", "系统繁忙，请稍后再试！", 1);
	            }
	        })
    	}
	}
    function loadNew(){
    	$("#sendComment").val("");
    	pageIndex = 1;
        $("#comments").html("");
        listMoreReply();
    	$("#down_comment").click();
    	removeAllActived();
    	activeRange(ranges);
    }
	function listMoreReply() {
		activityId = "${param.id}";
	    $.ajax({
            type:"POST",
            url: "${baseURL}/open/activity/openActivityAction!listOtherActivityComments.action",
            data: "page="+pageIndex+"&id=" + activityId + "&size="+pageSize,
            cache:false,
            dataType: "json",
            success: function(data){
            	lock_roll=false;
               if ("0" != data.code) {
                   showMsg("", data.desc, 1);
                   return;
               }
              	var comments = data.data.comments;
                var commentCount = "回复(<font>"+data.data.totalRows+"</font>)";
                $("#commentCount").html(commentCount); 
                appendCommnets(comments);
                pageIndex = data.data.currentPage+1;
                hasMore = data.data.hasMore;
                emojify.run($('.comment_list')[0]);
            }
        });
	}
	function appendCommnets(comments) {
    	var tempHtml='<li class="flexbox">'
    		+'<div class="avator" onclick="atThisPersonUtil(\'@userId\',\'@commentUser\',\'@headImage\')">'
    		+'<img src="@headImage" width="100%" alt=""  onerror="javascript:replaceUserHeadImage(this);">'
    		+'</div>'
    		+'<div class="comment_content flexItem">'
    		+'<h3 class="clearfix">'
    		+'<span class="title">@commentUser</span>'
    		+'<span class="time">@commentTime</span>'
    		+'<input type="hidden" value="@commentId" />'
    		+'</h3>'
    		+'<p class="@class mapItem" onclick="@addressMethod">@commentContent</p></div></li>';
    	var temp = "";
	   for (var i = 0; i < comments.length; i++) {
		    temp = tempHtml;
		   	var headPic = comments[i].headPic;
		   	if (creator == comments[i].userId && isManager) {
			   headPic = logo;
		   	}
            temp =temp.replace("@headImage",headPic);
            temp =temp.replace("@headImage",headPic);
            temp =temp.replaceAll("@userId",comments[i].userId);
            temp =temp.replaceAll("@commentUser",comments[i].userName);
            temp =temp.replace("@commentTime",comments[i].time);
            temp =temp.replaceAll("@commentId",comments[i].commentId);
            if(comments[i].type=='2'){
            	temp =temp.replace("@class","commentImg");
            	temp = temp.replace("@commentContent","<img onclick=\"comPreviewImg(this)\" src=\"${compressURL}" + comments[i].content + "\" />");
            }else if(comments[i].type=='4'){
				temp = temp.replace("@class","positionText");
				temp = temp.replace("@commentContent",comments[i].content);
				temp = temp.replace("@addressMethod","openMapForMore(this)");
			}else{
            	temp =temp.replace("@class","");
            	temp =temp.replace("@commentContent",checkURL(comments[i].content,true));
            }
            $("#comments").append(temp);
        }
       emojify.run($('.comment_list')[0]);
	}
	function cancelActivityRegistry(){
		showLoading("正在取消报名...");
		var requestUrl = "${baseURL}/activity/activityAction!cancelActivityRegistry.action";var activityId="${param.id}";
        $.ajax({
            type:"POST",
            url: requestUrl,
            async: false,
            data: {id:'${param.id}',userId:'${param.userId}'},
            cache:false,
            dataType: "json",
            success: function(data){
            	if(data.code =="0"){
            		hideLoading();
            		$("#gotopay").hide();
            		hasRegistry = '-1';
            		var count = data.data.registryCount;
            		var regis_sum=data.data.regis_sum;
            		var countAll=data.data.countAll;
            		var regis_res=data.data.regis_res;
            		if(regis_sum=='0'){
                		$("#regLink").html("我要报名");
                	}else{                       		
                        $("#regLink").html("我要报名(剩"+regis_res+"人)");
                	}
            		$("#regLink").removeClass("red_btn");
            		$("#registry_remark").hide();
            		var strBaoMing="";
                    if(regis_sum=='0'){ //当选择参与对象为所有人的时候
                    	strBaoMing="("+count+"/"+countAll+"人)";
                    	$("#active_userlist_count").html("已报名("+count+"/"+countAll+"人)");
                        $("#active_userlist_count_tip").html("已报名("+count+"/"+countAll+"人)");
                    }else{
                    	strBaoMing="("+count+"/"+regis_sum+"人)";
                    	$("#active_userlist_count").html("已报名("+count+"/"+regis_sum+"人)");
                    	$("#active_userlist_count_tip").html("已报名("+count+"/"+regis_sum+"人)");
                    }
                    $("#btn_registryList").html("查看报名详情"+strBaoMing);
                    if(count==0){
                    	$("#registry_list").hide();
                    	$("#btn_registryList").hide();
                    }else{
                    	$("#registry_list").show();
                    	$("#btn_registryList").show();
                        var registryNames = data.data.registryNames;
                        var ulEle = $("#registryNames");
                        var ulEleTip = $("#registryNames_tip");
                        ulEle.html("");
                    	ulEleTip.html("");
                        var tempTip = "";
                        if(registryNames.length>3){
                        	$("#registryA").show();
                        }
                        for (var i = 0; i < registryNames.length; i++) {
                        	if(registryNames[i].isAnonymous=='1'){
                        		tempTip = "<li>"+
                            	"<p class=\"img\"><img src=\""+registryNames[i].headPic+"\" alt=\"\"   onerror=\"javascript:replaceUserHeadImage(this);\"/></p>"+
                        		"<p class=\"name\">"+registryNames[i].personName+"</p></li>";
                        	}else{
                        		tempTip = "<li onclick=\"atThisPersonUtil('"+registryNames[i].APPLIER+"','"+registryNames[i].personName+"','')\">"+
                            	"<p class=\"img\"><img src=\""+registryNames[i].headPic+"\" alt=\"\"   onerror=\"javascript:replaceUserHeadImage(this);\"/></p>"+
                        		"<p class=\"name\">"+registryNames[i].personName+"</p></li>";
                        	}
                        	if(i<4){
                                ulEle.append(tempTip);
                        	}
                        	ulEleTip.append(tempTip);
                        }
                    }
            	}
            	else{
            		hideLoading();
                    showMsg("提示信息", data.desc, 1,{ok:function(result){window.location.reload();}});
            	}
            	activitylock=true;
            }
        }); 	
	}
	function registryOrNot() {
		//上锁
		if(activitylock){
			activitylock=false;
			// 检查是否从校友会APP链接过来的
			/* if(judgeClient("${param.w}")){
	        	activitylock=true;
	        	return;
			} */
			if(hasRegistry=='0' || hasRegistry=='1'){
				if(payStatus){
					showMsg("提示","取消报名后，报名费将在2~3个工作日内原路返回。",2,
							{
								ok:function(result){
									cancelActivityRegistry();
								},
								fail:function(result){
									$("#showMsg_div").hide();
									$(".overlay").hide();
								}
							});
				}else{
					cancelActivityRegistry();
				}
			}else{
				hideLoading();
            	activitylock=true;
				$(".overlay").show();
				$("#registryBox").show();
			}
		}
	}
    
	//提示信息回调函数
	function handle(){
        window.location.reload();
	}
    /**
     * 提供是否可以分享
     */
    function canShare(){
    	return 1;
    }
    
    var lock_roll=false;
    $(function() {
         // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
         var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
         var nScrollTop = 0; //滚动到的当前位置
         var $frame = $("#main");
         $frame.on("scroll touchmove", function() {
             var nDivHight = $frame.height();
             nScrollHight = $(this)[0].scrollHeight;
             nScrollTop = $(this)[0].scrollTop;
             if (nScrollTop + nDivHight >= nScrollHight) {
                 // 触发事件，这里可以用AJAX拉取下页的数数据
                 if(!lock_roll){
                	 lock_roll=true;
                	 if (hasMore) {
                   		 listMoreReply();
                	 }
				}
             };
         });
     });
    function closeMyMsgBox(){
    	restoreSubmit();
    	$(".overlay").hide();
    	$("#cancelactivity_div").hide();
    }
	function openLotteryDetail() {
		window.location.href = "${openURL}/open/activity/lotteryDetail.jsp?id=${param.id}";
	}
</script>