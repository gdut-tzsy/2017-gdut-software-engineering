 
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
    //点击节点处理方式时触发的时间
    function checkRadioInfo(num){
    	var nodeChecks=document.getElementsByName("radio"+num);
    	for(var i=0;i<nodeChecks.length;i++){
    		if(nodeChecks[i].checked){
    			if(i==0){//提单人处理  或者科室处理人时清除审批用户所选的用户
    				clearAuditInfo(num);
    				$("#isLading"+num).val("1");
    			}
    			if(i==1){//如果是科室则自动勾选自动查询上级以及显示 否则屏蔽
    				clearAuditInfo(num);
    				$("#isLading"+num).val("3");
    				$("#isQuery"+num).attr("disabled",false);
    				//$("#isQuery"+num).attr("checked", true); 
    			}else{
    				$("#isQuery"+num).attr("checked", false);
    				$("#isQuery"+num).attr("disabled",true);
    			}
    			if(i==2){//如果是用户
    				$("#isLading"+num).val("2");
    				$("#getGroupBut"+num).attr("onmouseup","getGroup("+num+")"); //添加相关事件
    			    $("#getUserBut"+num).attr("onmouseup","getUser("+num+")");  //添加相关事件
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
		var num=1;
		var flag=false;
		var nodeChecks="";
		for(var i=0;i<flowNodeNames.length;i++){
			var nodeInfo="";
			var ranges="1";
			if(flowNodeNames[i].value==""){
				flowNodeNames[i].focus();
				_alert("提示信息","请填写第"+num+"步节点名称");
				flag=true;
				break;
			}else{
				nodeInfo+="<input type='hidden' name='nodeList["+i+"].nodeName' value='"+flowNodeNames[i].value+"'/>";
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
			nodeInfo+="<input type='hidden' name='nodeList["+i+"].ranges' value='"+ranges+"'/>";
			$("#flow_node_div_info").append(nodeInfo);
			num++;
		}
	    return flag;
	}