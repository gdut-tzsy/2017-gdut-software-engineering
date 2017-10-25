 var soterTipHtml="<div class='tipsBox'>"
	 +"<i class='fa-question-circle'></i>"
	 +"<div class='tipsItem right' style='top: 615.5px; left: initial; right: 83px; display: none;'>"
	 +" <div class='tipsContent'>"
	 +" <b style='color:#666'>使用指纹验证(<a href='javascript:soterQRCode()'>获取手机是否支持指纹验证测试二维码</a>)</b><br>勾选后，必须通过手机指纹验证才能进行审批<br><br>"
	 +"<b style='color:red'>注意：<br>"
	 +"微信将在近期逐步开放小米、三星、华为手机的指纹识别支持<br>"
	 +"目前暂只支持Iphone / 金立 / Hisense / HTC / IVVI / Meitu /<br>MOTO/ OPPO / SUGAR / TCL / vivo / ZUK / 乐视 / 魅族 /<br> 努比亚 / 一加/ 中国移动 / 中兴 / 富士康 / 酷派  / 锤子 / 华硕 /"
	 +"</b></div></div></div> ";
    //选择审核用户
    function getUser(num) {
        var top=(window.screen.height-630)/2;
        var left=(window.screen.width-580)/2;
        window.open("flow_choose_user_main.jsp?num="+num, '节点审批人', 'left='+left+',top='+top+',height=500, width=820, toolbar=no, menubar=no, resizable=yes,location=no, status=no,scrollbars=yes,directories=no,menubar=no');
    }
    //选择审核用户组
    function getGroup(num) {
        var top=(window.screen.height-630)/2;
        var left=(window.screen.width-580)/2;
        window.open("flow_choose_usergroup_main.jsp?num="+num, '用户组', 'left='+left+',top='+top+',height=500, width=820, toolbar=no, menubar=no, resizable=yes,location=no, status=no,scrollbars=yes,directories=no,menubar=no');
    }
    //删除审核用户
    function liRemoveUser(obj,num,userid){
    	var userIds=$("#userId"+num).val();
	    var userNames=$("#nodeUserName"+num).val();
	    var nodePics=$("#nodePic"+num).val();
	    if($(obj).parent('li').attr('class')=='selected_personnel_item pl'){
	    	  var userList=userIds.split("\|");
	    	  var userNameList=userNames.split("\|");
	    	  var nodePicList=nodePics.split("\|");
		   	  for(var i=0;i<userList.length;i++){
		   		  if(userList[i]==userid){
		   			 userNames=userNames.replace(userNameList[i]+"|","");
		   			 nodePics=nodePics.replace(nodePicList[i]+"|","");
		   			  break;
		   		  }
		   	  }
		      userIds=userIds.replace(userid+"|","");
		      $("#userId"+num).val(userIds);
		      $("#nodeUserName"+num).val(userNames);
			  $("#nodePic"+num).val(nodePics); 
			  /*$('#nodeUserImgs'+num).find('.selected_personnel_item').eq(index).remove();*/
			  $(obj).parent('.selected_personnel_item').remove();
	    }
	    
    }
   //删除审核用户组
    function removeGroup(obj,groupNum,groupId){
    	var groupIds=$("#groupId"+groupNum).val();
    	var groupAray=groupIds.split("|");
    	if(groupAray.length==1)
    		$("#groupId"+groupNum).val("");
    	else{
    		var groupStr="";
    		for(var i=0;i<groupAray.length;i++){
    			if(groupAray[i]!=groupId){
    				if(groupStr!="")
    					groupStr+="|";
    				groupStr+=groupAray[i];
    			}
    		}
    		$("#groupId"+groupNum).val(groupStr);
    	}
    	$(obj).parent('li').remove();
	   	//groupIds=groupIds.replace(groupIds+"|","");
	   	
    }
   
    function showSaveDiv(){//加上这方法可以防止选择审核用户时，因高度的改变保存按钮不显示问题
    	$("#saveDiv").click();
    }
    //点击节点处理方式时触发的事件
    function checkRadioInfo(num){
    	var nodeChecks=document.getElementsByName("radio"+num);
    	for(var i=0;i<nodeChecks.length;i++){
    		if(nodeChecks[i].checked){
    			if(i==0){//提单人处理  或者科室处理人时清除审批用户所选的用户
    				clearAuditInfo(num);
					$("#isQuery"+num).attr("checked", false);
    				$("#isQuery"+num).attr("disabled",true);
    				$("#isLading"+num).val("1");
					
    			}
    			if(i==1){//如果是科室则自动勾选自动查询上级以及显示 否则屏蔽
    				clearAuditInfo(num);
    				$("#isLading"+num).val("3");
    				$("#isQuery"+num).attr("disabled",false);
					
    			}else if(i==2){//如果是用户
				    $("#isQuery"+num).attr("checked", false);
    				$("#isQuery"+num).attr("disabled",true);
    				$("#isLading"+num).val("2");
    				$("#getGroupBut"+num).attr("onmouseup","getGroup("+num+")"); //添加相关事件
    			    $("#getUserBut"+num).attr("onmouseup","getUser("+num+")");  //添加相关事件
					
    			}
    			
    		}
    	}
    }
	 //点击节点处理方式时触发的事件
    function checkNodeTypeRadioInfo(num){
    	var nodeChecks=document.getElementsByName("nodetype_radio"+num);
    	for(var i=0;i<nodeChecks.length;i++){
    		if(nodeChecks[i].checked){
    			if(i==0){//审批
    				$("#nodetype"+num).val("1");
    			}
    			else if(i==1){//知会
    				$("#nodetype"+num).val("2");
    			}else if(i==2){//承办
    				$("#nodetype"+num).val("3");
    			}
    			
    		}
    	}
    }
    //清空已选用户及群组
     function clearAuditInfo(num){
    	$("#getGroupBut"+num).attr("onmouseup",""); //禁用选人按钮
	    $("#getUserBut"+num).attr("onmouseup","");  //禁用选组按钮
	  //清空该节点下所选用户以及组
		$("#groupId"+num).val("");
		$("#userId"+num).val("");
		$("#nodeUserName"+num).val("");
		$("#nodePic"+num).val("");
		$("#nodeUserImgs"+num).html("");
		$("#nodeGroup"+num).html("");
		$("#isLading"+num).val("1");
    }
    //保存时处理节点信息
    function getFlowNodeInfo(){
		$("#flow_node_div_info").html('');
		var nodeUserIds=document.getElementsByName("node_userIds");
		var flowNodeNames=document.getElementsByName("node_names");
		var isLadings=document.getElementsByName("isLading");
		var isQuery=document.getElementsByName("isQuery");
		var nodeGroupIds=document.getElementsByName("group_ids");
		var nodetype=document.getElementsByName("nodetype");
		var isSign=document.getElementsByName("isSign");
		var isChoice=document.getElementsByName("isChoice");
		var isCanEnd=document.getElementsByName("isCanEnd");
		var nodeId=document.getElementsByName("node_id");
		var isSoter=document.getElementsByName("isSoter");//<label class="block"><input name="isAutomaticAudit" type="checkbox">自动审批</label>
		var isAutomaticAudit=document.getElementsByName("isAutomaticAudit");
		var num=1;
		var flag=false;
		var nodeChecks="";
		for(var i=0;i<flowNodeNames.length;i++){
			var nodeInfo="";
			var ranges="1";
			if(flowNodeNames[i].value==""){//处理节点名称
				flowNodeNames[i].focus();
				_alert("提示信息","请填写第"+num+"步节点名称");
				flag=true;
				break;
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].nodeName' value='"+flowNodeNames[i].value+"'/>";
			}
			if(nodetype[i].value==""){//节点类型
			   _alert("提示信息","请选择第"+num+"步节点类型");
				flag=true;
				break;
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].nodeType' value='"+nodetype[i].value+"'/>";
			}
			if(isLadings[i].value=="1"){//提单人处理
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isLading' value='1'/>";
			}else if(isLadings[i].value=="2"){//用户
				var flag2=true;
				if(nodeUserIds[i].value!=""&&nodeGroupIds[i].value!=""){
					nodeInfo+="<input type='hidden' name='nodeList["+i+"].handleAccounts' value='"+nodeUserIds[i].value+"'/>";
					nodeInfo+="<input type='hidden' name='nodeList["+i+"].userGroupIds' value='"+nodeGroupIds[i].value+"'/>";
					flag2=false;
					ranges="3";
				}else {
					if(nodeUserIds[i].value!=""){
						nodeInfo+="<input type='hidden' name='nodeList["+i+"].handleAccounts' value='"+nodeUserIds[i].value+"'/>";
						flag2=false;
					}
					if(nodeGroupIds[i].value!=""){
						nodeInfo+="<input type='hidden' name='nodeList["+i+"].userGroupIds' value='"+nodeGroupIds[i].value+"'/>";
						ranges="2";
						flag2=false;
					}
				}
				if(flag2){
				  _alert("提示信息","请选择第"+num+"步处理用户");
				  flag=true;
				  break;
				}
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isLading' value='2'/>";
			}else{//组织审批
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isLading' value='3'/>";
				if(isQuery[i].checked){
					nodeInfo+="<input type='hidden' name='nodeList["+i+"].isQuery' value='1'/>";
				}else{
					nodeInfo+="<input type='hidden' name='nodeList["+i+"].isQuery' value='2'/>";
				}
			}
			if(isSign[i].checked){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isSign' value='0'/>";	
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isSign' value='1'/>";	
			}
			if(isChoice[i].checked){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isChoice' value='0'/>";	
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isChoice' value='1'/>";	
			}
			if(isCanEnd[i].checked){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isCanEnd' value='0'/>";
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isCanEnd' value='1'/>";
			}
			if(isSoter[i].checked){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isSoter' value='1'/>";
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isSoter' value='0'/>";
			}
			if(isAutomaticAudit[i].checked){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isAutomaticAudit' value='1'/>";
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].isAutomaticAudit' value='0'/>";
			}
			if(nodeId[i].value!=""){
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].id' value='"+nodeId[i].value+"'/>";	
			}
			nodeInfo+="<input type='hidden' name='nodeList["+i+"].ranges' value='"+ranges+"'/>";
			
			$("#flow_node_div_info").append(nodeInfo);
			num++;
		}
	    return flag;
	}
//判断是否选中该节点的会签功能
function getIsSignVal(numVal,obj){
	if(obj.checked){
		$("#isSign"+numVal).val("0");
	}else{
		$("#isSign"+numVal).val("1");
	}
}
/***获取指纹验证的二维码**/
 function soterQRCode(){
	 var url=baseHttpURL +"/open/demo/soter.jsp";
	 var tmp='<div class="TipLayer"><div class="TipLayerTit"><i id="popCloseQ"">×</i></div>'+
		 '<div class="TipLayerMain shareImgList" id="showQrcodeId" ></div><div class="TipLayerBtn">' +
		 '请使用处理人的手机扫描二维码确认是否支持指纹功能</div>';
	 with(window.top){
		 $('body').append(tmp);
		 if ($("#showQrcodeId").attr("title") == undefined) {
			 var qrcode = new QRCode(document.getElementById("showQrcodeId"), {
				 width: 220, //设置宽高
				 height: 220
			 });
			 qrcode.makeCode(url);
		 }
		 $('.overlay').show();
		 $('#popCloseQ',window.top.document).on('click',function(){//取消
			 $('.overlay').hide();
			 $('.TipLayer').remove();
		 });
		 $('#downQrcode',window.top.document).on('click',function(){//下载二维码
			 html2canvas(this).then(function (canvas) {
				 canvas.id = "html2canvas";
				 var image = new Image();
				 image.id  = "image";
				 image.src = canvas.toDataURL("image/png");
			 })

		 });
	 }
 }
 function removeUrl(){
	 $("#showQrcodeId").removeAttr("title");
	 $("#showQrcodeId").html("");
	 $('.TipLayer').hide();
	 $('.overlay').hide();
 }