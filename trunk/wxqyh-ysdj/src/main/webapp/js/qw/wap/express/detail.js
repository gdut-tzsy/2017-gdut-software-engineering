var template2 = ''
   	+'<li class="ccli" style="display:@Show;height: 45px;"> '
   	+'    <input type="hidden" id="relatives" name="relatives" value="@UserId" /> '
   	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'cc\',@ArrayValue)" ></a> '
   	+'    <p class="img"> '
   	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
   	+'    </p> '
   	+'    <p class="name">@UserName</p> '
   	+'</li>';
var creator;			//创建人
var status="";
var userType="";
//加载任务详情内容
$(document).ready(function () {
		var id=$("#expressid").val();
    	showLoading();
        $.ajax({
            type:"POST",
            url: baseURL+"/portal/expressAction!getDetail.action",
            data:{id:id},
            cache:false,
            dataType:"json",
			success : function(result) {
				if(result.code!='0'){
					hideLoading();
					_alert('提示',result.desc,"确认",function(){WeixinJS.back(); restoreSubmit();});
				}
				var detail = result.data.detail;
				$("#detailPic").attr("src",detail.headPic);
				$("#detailPic").attr("onclick","atThisPersonUtil('"+detail.creator+"','"+detail.personName+"','"+detail.headPic+"')");					
				$("#detailPerson").html(detail.personName);
				var ct = detail.createTime;
				if(ct.substring(0,10)==new Date().Format("yyyy-MM-dd")){
					$("#detailTime").html(ct.substring(11,16));
				}else{
					$("#detailTime").html(detail.createTime);
				}
				$("#detailTitle").html(detail.title);
				
				//显示图片
				var picList = detail.picList;
				if(picList && picList.length>0){
					$("#picdiv").show();
					for(var i=0;i<picList.length;i++){
						$("#topImage").append("<img src=\""+compressURL+picList[i].picPath+"\" style=\"width:100%\"/>");
					}
				}
				creator=detail.creator;
				var content=detail.content;
				//显示备注
				if(content!=''){
					$("#remarkdiv").show();
					$("#detailContent").html(checkURL(content,true));
				}
				//显示收件人
				$("#to_user_div").show();
				$("#to_user").html(detail.receiveUser);
				if(detail.receiveRefUser!=""){
					//代领人
					$("#cc_user_div").show();
					$("#cc_user").html(detail.receiveRefUser);
				}
				if(detail.trackingNumber){
					$("#expressiddiv").show();//已领取快递显示快递单号
					$("#expresslist").html("<span >"+detail.trackingNumber+"</span>");
				}
				if(detail.payMoney && detail.payMoney>0){
					$("#pay_money_div").show();//显示到付金额
					$("#pay_money").html("<span >"+detail.payMoney+"</span>");
				}
				if(detail.refRemarks){
					$("#refremarkdiv").show();//显示代领留言
					$("#refDetailContent").html("<span >"+detail.refRemarks+"</span>");
				}
				status=detail.status;
				userType=result.data.duser;
				if(detail.status=='0'){//未领取
					$("#btndiv").show();
					if(result.data.duser=='1'){//收件人进入
						if(detail.receiveRefUser==""){
							$("#addRecipient").show();
						}
						$("#updateExpress").show();
					}else if(result.data.duser=='0'){
						//创建人
						//按钮处理
						if(result.data.closepowe){
							$("#addRecipient").show();
						}
						$("#addRecipient").html("关闭通知单");
						$("#addRecipient").attr("href","javascript:close()");
						$("#addRecipient").addClass("red_btn");
						$("#updateExpress").html("催领快递");
						$("#updateExpress").attr("href","javascript:againSendMsg('"+detail.id+"')");
						$("#updateExpress").show();
					}else if(result.data.duser=='2'){
						$("#updateExpress").show();
					}
				}else if(detail.status=='1'){
						if(detail.lastType=='1'){
							$("#to_user_div").addClass("kuaiti_lq");
							$("#isCloseTime").html("领取时间：<span >"+detail.receiveTime+"</span>");
						}else if(detail.lastType=='2'){
							$("#cc_user_div").addClass("kuaiti_dl");
							$("#isCloseTime").html("领取时间：<span >"+detail.receiveTime+"</span>");
						}else{
							//创建人关闭
							$("#closediv").show();
							$("#closeremark").html(detail.remarks);
							$("#closediv").addClass("kuaiti_gb");
							$("#isCloseTime").html("关闭时间：<span >"+detail.receiveTime+"</span>");
						}
						$("#isClosediv").show();
				}
				//最后的下划线处理
				$($(".s_item_new").not($(".s_item_new:hidden")).eq(-1)).addClass('borderBottommNone');
				hideLoading();
			},
			error : function() {
				hideLoading();
				_alert('提示',internetErrorMsg,"确认",function(){WeixinJS.back(); restoreSubmit();});
			}
	});
});
//添加代领人
function addRecipient(){
	$("#wrap_main").hide();
	$("#selectCcUser_div").show();
	showSelectUser('taskcc');
}
//添加代领人后事件
function combackDetail(){
	var person = selectedCcUser[0].split("|");
	var addUserId=person[0];
//	var personName=person[1];
	if(addUserId==''){
		_alert("提示","请添加一个代领人","确认",function(){restoreSubmit();});
		return ;
	}else{
		resetUsersCc();
			$("#selectCcUser_div").hide();
			$("#wrap_main").show();
			$(".overlay").show();
			$("#close_div2").show();
			$("#closeTip2").hide();
	}
	/*var expressid=$("#expressid").val();
	$.ajax({
		url:baseURL+"/portal/expressAction!addRecipient.action",
		type:"POST",
		data:{addUserId:addUserId,expressid:expressid},
		dataType:"json",
		async:false,
		success:function(result){
			hideLoading();
			if(result.code!="0"){
			}else{
				$("#cc_user_div").show();
				$("#cc_user").html(personName);
				$("#addRecipient").hide();
				$("#addRecipient").attr("href","");
				//最后的下划线处理
				$(".s_item_new").removeClass('borderBottommNone');
				$($(".s_item_new").not($(".s_item_new:hidden")).eq(-1)).addClass('borderBottommNone');
				//huhao
				window.location.href=baseURL+"/jsp/wap/express/list.jsp?status=0&agentCode='express'";
			}
		},
		error:function(){
			hideLoading();
		}
	});*/
}
//确认领取
function updateExpressStatus(obj){//obj区分创建人关闭还是领取,0是创建人关闭
	var id=$("#expressid").val();
	var remark="";
	if($("#closecontent").val()!=''){
		remark=$("#closecontent").val();
	}
	showLoading("正在请求...");
	$.ajax({
		url:baseURL+"/portal/expressAction!updateExpressStatus.action",
		type:"POST",
		data:{id:id,remark:remark},
		dataType:"json",
//		async:false,
		success:function(result){
			hideLoading();
			if(result.code!="0"){
				_alert('提示',result.desc,"确认",function(){WeixinJS.back(); restoreSubmit();});
			}else{
				$("#addRecipient").hide();
				$("#updateExpress").hide();
				if(obj=='0'){//创建人关闭
					$("#closediv").show();
					$("#closeremark").html(remark);
					$("#closediv").addClass("kuaiti_gb");
				}else{//领取的
					if(userType=='1'){
						$("#to_user_div").addClass("kuaiti_lq");
					}else if(userType=='2'){
						$("#cc_user_div").addClass("kuaiti_dl");
					}
					$("#isClosediv").show();
					$("#isCloseTime").html(result.data.receiveTime);
				}
				//最后的下划线处理
				$(".s_item_new").removeClass('borderBottommNone');
				$($(".s_item_new").not($(".s_item_new:hidden")).eq(-1)).addClass('borderBottommNone');
			}
			$("#bclosediv").hide();
		},
		error:function(){
			hideLoading();
			$("#bclosediv").hide();
			_alert('提示',internetErrorMsg,"确认",function(){WeixinJS.back(); restoreSubmit();});
		}
	});
}
//创建人关闭表单
function close(){
	$(".overlay").show();
	$("#bclosediv").show();
}
//创建人关闭表单 确定按钮事件
function submitclose(){
	if($("#closecontent").val()==""){
		$(".popBox_error").show();
		return ;
	}else{
		$(".popBox_error").hide();
	}
	updateExpressStatus('0');
}
//创建人关闭表单 取消按钮事件
function bclosebtn(){
	$(".overlay").hide();
	$("#bclosediv").hide();
}
//催单
function againSendMsg(id){
	showLoading("正在发送...");
	$.ajax({
		url:baseURL+"/portal/expressAction!againSendMsg.action",
		type:"POST",
		data:{id:id},
		dataType:"json",
		success:function(result){
			hideLoading();
			if(result.code!="0"){
				_alert('提示',result.desc,"确认",function(){WeixinJS.back(); restoreSubmit();});
			}else{
				_alert("提示","催单成功","确认",function(){restoreSubmit();});
			}
		},
		error:function(){
			hideLoading();
			_alert('提示',internetErrorMsg,"确认",function(){restoreSubmit();});
		}
	});
}
//返回列表
function combackList(){
	window.location.href=baseURL+"/jsp/wap/express/list.jsp?status="+status+'&agentCode=express&abc=1';
}
//删除历史选择联系人
function deletePersons(type){
	if(type=="taskto"){
		$("#toPersonList").html("<li class=\"f-user-add\" style=\"margin-bottom:20px\" onclick=\"showSelectUser('taskto')\"></li><li class=\"f-user-remove\" style=\"margin-bottom:20px\" show=\"0\" onclick=\"removePersons(this,'taskto')\"></li>");
	}else{
		$("#ccPersonList").html("<li class=\"f-user-add\" style=\"margin-bottom:20px\" onclick=\"showSelectUser('taskcc')\"></li><li class=\"f-user-remove\" style=\"margin-bottom:20px\" show=\"0\" onclick=\"removePersons(this,'taskcc')\"></li>");
	}
}

//找人代领
function findRecipient(){
	selectedCcUser.length=0;
	$("#ccalreadySelected").html("");
	$(".search-box-height-cc").css("height","50px");
	$("#ccselected_user").hide();
	$("#ccmoreDiv").hide();
	$("#closeTip2").hide();
	//清空已选择群组 chenfeixiong 2014/08/26
	ccgroupArray = new Array();
		deletePersons("taskcc");
	//$(".overlay").height($(window).height()*10);
	$("#reason2").html("请帮忙代领快递! ");
	$(".overlay").show();
	$("#close_div2").show();
}
function closeMyMsgBox2(){
	$(".overlay").hide();
	$("#close_div2").hide();
	//restoreSubmit();
}
//确定同意并转审
function confirmRecipient(){
	var reason2 = $("#closeReason2").val();
	var relatives=$("#relatives").val();
	if(relatives=="" || relatives==undefined){
		$("#closeTip2").html("代领人不能为空");
		$("#closeTip2").show();
		return ;
	}
	var person = selectedCcUser[0].split("|");
	var addUserId=person[0];
//	var personName=person[1];
	if(addUserId==$('#userId').val()){
		$("#closeTip2").html("代领人不能是收件人");
		$("#closeTip2").show();
		return ;
	}
	var expressid=$("#expressid").val();
	showLoading("正在请求...");
	$.ajax({
		url:baseURL+"/portal/expressAction!addRecipient.action",
		type:"POST",
		data:{addUserId:addUserId,expressid:expressid,refRemarks:reason2},
		dataType:"json",
		success:function(result){
			hideLoading();
			if(result.code!="0"){
				_alert('提示',result.desc,"确认",function(){WeixinJS.back(); restoreSubmit();});
			}else{
			/*	$("#cc_user_div").show();
				$("#cc_user").html(personName);
				$("#addRecipient").hide();
				$("#addRecipient").attr("href","");
				//最后的下划线处理
				$(".s_item_new").removeClass('borderBottommNone');
				$($(".s_item_new").not($(".s_item_new:hidden")).eq(-1)).addClass('borderBottommNone');*/
				//huhao
				hideLoading();
				window.location.href=baseURL+"/jsp/wap/express/list.jsp?status=0&agentCode=express";
			}
		},
		error:function(){
			hideLoading();
			_alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
		}
	});
}
//显示联系人按钮
function removePersons(obj,type){
	var arry;
	if(type=="taskto"){
		arry = $("#toPersonList").find("a");
	}else{
		arry = $("#ccPersonList").find("a");
	}
	var show = $(obj).attr("show");
	if(show==0){
		$(obj).attr("show","1");
		for(var i=0;i<arry.length;i++){
			$(arry[i]).show();
		}
	}else{
		$(obj).attr("show","0");
		for(var i=0;i<arry.length;i++){
			$(arry[i]).hide();
		}
	}
	$("#closeTip2").hide();
}
//删除联系人
function deleteUser(obj,type,value){
	$(obj).parent().remove();
	selectedCcUser.remove(value);
	ccrefreshSelected("1",value);
}
//添加应用可见范围
wxqyh.agent = "express";