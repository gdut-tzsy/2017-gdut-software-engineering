var template2 = ''
	+'<li class="ccli" style="display:@Show;height: 45px;"> '
	+'    <input type="hidden" name="relatives" value="@UserId" /> '
	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'cc\',@ArrayValue)" ></a> '
	+'    <p class="img"> '
	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
	+'    </p> '
	+'    <p class="name">@UserName</p> '
	+'</li>';

var picMust;

	function init(){
		//判断当前人是否有权限录入快递
		showLoading("正在加载中...");
		$.ajax({
			url:baseURL+"/portal/expressAction!getIsPower.action",
			type:"POST",
			dataType:"json",
			async:false,
			success:function(result){
				hideLoading();
				if(result.code!="0"){
					_alert('提示',result.desc,"确认",function(){WeixinJS.back(); restoreSubmit();});
				}
				 if(!result.data.ispower){
					 _alert('提示',"亲爱的用户，你没有权限录入快递，请联系管理员","确认",function(){WeixinJS.back(); restoreSubmit();});
	        	 }
				 //是否需要上传图片
				 picMust = result.data.picmust;
				
				 //默认备注
				var po = result.data.po;
				if(po && po!=null){
					$("#title").html(po.remark);
				}
				$('textarea.inputStyle').trigger('click');
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
}
//删除历史选择联系人
function deletePersons(type){
	if(type=="taskto"){
		$("#toPersonList").html("<li class=\"f-user-add\" style=\"margin-bottom:20px\" onclick=\"showSelectUser('taskto')\"></li><li class=\"f-user-remove\" style=\"margin-bottom:20px\" show=\"0\" onclick=\"removePersons(this,'taskto')\"></li>");
	}else{
		$("#ccPersonList").html("<li class=\"f-user-add\" style=\"margin-bottom:20px\" onclick=\"showSelectUser('taskcc')\"></li><li class=\"f-user-remove\" style=\"margin-bottom:20px\" show=\"0\" onclick=\"removePersons(this,'taskcc')\"></li>");
	}
}
//删除全部图片
function removeAllPic(){
	var html = '<div class="f-add-user-list"><ul id="imglist" class="impression">'
				+'<li class="f-user-add" id="addimage">'
				+' <input id="imageFileInput" class="imageFileInput" type="file"></li></ul></div>';
	$("#upPicDiv").html(html);
}
//显示全部审批人
function showAllto(){
	var p = $("#toPersonList").find("li");
	for(var i=0;i<p.length;i++){
		$(p[i]).show();
	}
	$("#tomoreDiv").hide();
}
//显示全部相关人
function showAllcc(){
	var p = $("#ccPersonList").find("li");
	for(var i=0;i<p.length;i++){
		$(p[i]).show();
	}
	$("#ccmoreDiv").hide();
}
//提交任务
function commitTask(){
	if(selectedCcUser.length==0){
    	_alert("提示","请选择收件人","确认",function(){restoreSubmit();});
    	return ;
    }
	
	//是否必须上传图片
	if(picMust==1){
		if($("#imglist li").length<=2){
			_alert("提示","请添加图片","确认",function(){restoreSubmit();});
			return;
		}
	}
	//检查输入到付金额
	var $money=$('#money').val();
	if(isNaN($money)){
		_alert("提示","金额只能为数字","确认",function(){restoreSubmit();});
		return;
	}
	var charIndex = $money.indexOf(".");
	if(charIndex>-1 && $money.substring(charIndex).length>3){
		_alert("提示","金额精确到小数点后两位","确认",function(){restoreSubmit();});
		$money = $money.substring(0,charIndex+3);
		return ;
	}
	showLoading("正在提交...");
	$.ajax({
		url:baseURL+"/portal/expressAction!ajaxAdd.action",
		type:"POST",
		data:$("#taskform").serialize(),
		dataType:"json",
		success:function(result){
			if(result.code=="0"){
				/*$(".kuaiti_succeed").show();
				setTimeout(function (){$('.kuaiti_succeed').fadeOut()},1000);*/
				_tooltips_succeed('操作成功');
				//清空表单数据
				document.getElementById("taskform").reset(); 
				selectedCcUser.length=0;
				$("#ccalreadySelected").html("");
				$(".search-box-height-cc").css("height","50px");
				$("#ccselected_user").hide();
				$("#ccmoreDiv").hide();
				//清空已选择群组 chenfeixiong 2014/08/26
				ccgroupArray = new Array();
				deletePersons("taskcc");
				//删除图片
				  $("#imglist li").each(function(){
					  if($(this).attr("class")==''||$(this).attr("class")==undefined){
						  $(this).remove();
					  }
				 });
				//$("#imglist").html("<li class=\"f-user-add\" id=\"addimage\" style=\"margin-bottom:14px\"><input id=\"imageFileInput\" class=\"imageFileInput\" accept=\"image/*\" type=\"file\"></li>"+
				//  "<li class=\"f-user-remove\" show=\"0\" onclick=\"removeImage(this,'imglist')\"></li>");
    			//window.location.href=baseURL+"/jsp/wap/express/list.jsp?status=0";
			}else{
				_alert("提示",result.desc,"确认",function(){restoreSubmit();});
			}
			hideLoading();
		},
		error:function(){
			hideLoading();
			_alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
		}
	});
}
//添加代领人后事件
/*function combackDetail(){
	
}*/

//检查输入到付金额
/*function checkMoney(obj){
	if(obj){
		var v = $.trim(obj.value);
		if(isNaN(v)){
			_alert("提示","金额只能为数字","确认",function(){restoreSubmit();});
			obj.value = "0.0";
		}
		var m = obj.value;
		var charIndex = m.indexOf(".");
		if(charIndex>-1 && m.substring(charIndex).length>3){
			_alert("提示","金额精确到小数点后两位","确认",function(){restoreSubmit();});
			obj.value = m.substring(0,charIndex+3);
			return ;
		}
	}
}*/


$(document).ready(function(){
	wxqyh.agent="express";
init();
});