<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/ztree/js/jquery.ztree.exedit-3.5.js"></script>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/ztree/js/jquery.ztree.excheck-3.5.js"></script>
    
    
    <style type="text/css">
        .ztree li button.add {
            margin-left: 2px;
            margin-right: -1px;
            background-position: -144px 0;
            vertical-align: top;
            vertical-align: middle
        }

        div.ztree {
  background: none repeat scroll 0 0 #f6f6f6;
  border: 1px solid #d9dadc;
  border-radius: 5px;
  max-height: 100000px;
  min-height: 552px;
  overflow: hidden;
  padding-top: 20px;
}

		div.content_wrap {
  min-height: 200px;
  overflow: hidden;
  width: 100%;
}
		
        div.content_wrap div.left {
            float: left;
            width: 21%;
            margin-top:20px;
        }

        div.content_wrap div.right {
            float: left;
            width: 78%;
            margin-left: 1%;
        }

        div.zTreeDemoBackground {
            width: 250px;
           
            text-align: left;
        }

        .fr.frMainBar .searchBox {
  background: none repeat scroll 0 0 #fff;
  border: 1px solid #cccccc;
  border-radius: 4px;
  height: 35px;
  margin-left: 250px;
  position: relative;
  width: 240px;
}
#chooseUsers .span_return {
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
  display: inline-block;
  float: left;
  padding: 8px 35px;
}
#chooseUsers .add_btn {
  float: left;
  font-size: 16px;
  font-weight: bold;
  margin-left: 10px;
  padding: 8px;
}
#chooseUsers .add_wrap {
  clear: both;
  margin-top: 20px;
  overflow: hidden;
}
#chooseUsers .add_btn span {
  color: #ff9600;
  margin-left: 3px;
}
.foot_wrap {
  border-top: 1px solid #eeeeee;
  clear: both;
  position: relative;
}
#nodeDetail{
overflow:hidden;
min-height:760px;
}
    </style>
</head>
<body>
<div id="chooseUsers" style="display:none">
	<div class='add_wrap'><span class='span_return' onclick="combackUser()">返回</span><div class='add_btn'>添加特定人员<span class='selected_num2' id="userCount1">(0)</span></div></div>
	<div class="content_wrap">
	    <div id="left_w" class="zTreeDemoBackground left">
	        <div id="chose_orgTree" class="ztree"></div>
	    </div>
	    <div id="right_w"  class="right">
	    	<input name="userId" type="hidden" value="" id="chooseIds"/>
       		<input name="userName" type="hidden" value="" id="chooseNames"/>
       		<input name="pic" type="hidden" value="" id="picIds"/>
	        <div id="nodeDetail">
	        	 <input name="chooseDepartId" type="hidden" value="" id="chooseDepartId"/>
	             <iframe name="personFrame" src="${baseURL}/manager/include/personList.jsp" id="personFrame"  width="100%" height="750px" frameborder="0" style="overflow-x:hidden;overflow-y:auto;min-height: 760px;" scrolling="no" >
	             </iframe>  	
	        </div>
	    </div>	    
	</div>
	<div class="mt20 foot_wrap" align="center" style="position:relative">
	     <p  style="position:absolute;top:40px;left:45%">
	     	<input type="button" class="btn orangeBtn twoBtn" value="确定" onclick="chooseuserok();"> 
	     	 <input type="button" class="btn writeBtn twoBtn ml10" value="取消" onclick="combackUser()"> 
	     </p>
    </div>
</div>
<script type="text/javascript">
	/* var ids =$("#deptIds").val(); */
	
	var userIds;
    var department = {
        data: {
            simpleData: {
                enable: true,
                idKey: "nodeId",
                pIdKey: "parentId",
                root: 0
            },
            key: {
                name: "nodeName"
            }
        },
        view: {
            showLine: false,
            showIcon: true
        },
        edit: {
            enable: false
        },
        async: {
            enable: true,
            url: baseURL+"/contact/contactAction!listOrgNodeByParent.action",
            autoParam: ["orgId","orgId"],
            dataFilter: ajaxDataFilter3
        },
        callback: {
            onClick: seachOrgNode,
            beforeClick: beforNodeClick1,
           //onCheck: checkOrgNode1
        },
       /* check: {
            enable: false,
            /* chkStyle: "checkbox",
            autoCheckTrigger: true,
            chkboxType: { "Y": "", "N": "" },
            nocheckInherit: true 
        }  */
    };

    //var orgNodes1;
    var allowClick1 = true; //标记节点是否能被点击

     $(document).ready(function () {
     	

		var ids = window.opener.checkHistory.ids;
		
    	var userId="userId"+"${param.num}";
    	var userName="nodeUserName"+"${param.num}";
    	var nodePic="nodePic"+"${param.num}";
    	
    	
    	//alert(ids.substring(0,1));
    	//alert(1);
    	if(ids.length>0){
    		if(ids.substring(0,1)==","){//如果第一个字符为","
    			ids=ids.substring(1,ids.length);
    		}
    		if(ids.substring(ids.length-1,ids.length)==","){//如果最后一个字符为","
    			ids=ids.substring(0,ids.length-1);
    		}
    	}
    	
    	
    	var chooseIds=ids.split(",").join("|")+"|";
    	//2015-12-4 lisehgntao 修复选中的字数统计错误问题
		if(chooseIds=="|"){
			chooseIds="";
		}
    	
		
		
/* 		alert(chooseIds);
		alert(window.opener.checkHistory.names.split(",").join("|")+"|");
		alert(window.opener.document.getElementById(nodePic).value); */
		
    	$("#chooseIds").val(chooseIds);
    	$("#chooseNames").val(window.opener.checkHistory.names.split(",").join("|")+"|");
    	//$("#picIds").val(window.opener.document.getElementById(nodePic).value);
    	
    	if(chooseIds!=""&&chooseIds!=null){
    		var users=chooseIds.split("\|");
    		var num=users.length-1;
        	$(window.parent.$("#userCount1").html("("+num+")"));
    	}
    	
    	
//  	var userId="userId"+"${param.num}";
//  	var userName="nodeUserName"+"${param.num}";
//  	var nodePic="nodePic"+"${param.num}";
//  	var chooseIds=window.opener.document.getElementById(userId).value;
//  	$("#chooseIds").val(chooseIds);
//  	$("#chooseNames").val(window.opener.document.getElementById(userName).value);
//  	$("#picIds").val(window.opener.document.getElementById(nodePic).value);
//  	if(chooseIds!=""&&chooseIds!=null){
//  		var users=chooseIds.split("\|");
//  		var num=users.length-1;
//      	$(window.parent.$("#userCount1").html("("+num+")"));
//  	}
    	
    }); 

    /**
     * 初始化组织机构树
     */
    function usersinit() {
        $.ajax({
            url: baseURL+'/contact/contactAction!getRootNodeByUser.action',
            type: 'post',
            dataType: 'json',
            data: {"orgId": $("#orgid").val()},
            success: function (result) {
                if ('0' == result.code) {
                	 var orgNodes1 = result.data.orgList;
                    $.each(orgNodes1, function (i, node) {
                    	node.icon = baseURL+"/manager/images/company_icon.png";
                    });
                    if (orgNodes1.length > 0) {
                        $.fn.zTree.init($('#chose_orgTree'), department, orgNodes1);
                        // 展开机构树的根节点
                        var orgTree = $.fn.zTree.getZTreeObj("chose_orgTree");
                        var rootNodes = orgTree.getNodes();
                        if (rootNodes.length > 0)
                            orgTree.expandNode(rootNodes[0], true, false, true);
                    }
                } else {
                    alert(result.desc);
                }
            }
        });
    }

    /**
     * 展开父节点
     * @param event
     * @param orgTreeId
     * @param orgTreeNode
     */
    function expandParentOrg(event, orgTreeId, orgTreeNode) {
        var orgTree = $.fn.zTree.getZTreeObj("chose_orgTree");
//    orgTree.reAsyncChildNodes(orgTreeNode, "refresh");
    }

    /**
     * 异步加载节点数据后的处理函数
     * @param treeId
     * @param parent
     * @param result
     */
    function ajaxDataFilter3(treeId, parent, result) {
        if ('0' == result.code) {
            var data = result.data.orgList;
            /* if (data) {            	
            	ids=$("#deptIds").val();
                for (var i = 0; i < data.length; i++) {
                        if (ids.indexOf(data[i].nodeId) > -1){
                            	data[i].checked = "true";        	
                        }
                }
            }  */
            $.each(data, function (i, node) {
                if (node.isParent && node.isParent == "true") {
                    node.iconOpen = "${baseURL}/manager/images/twopeople_icon.png";
                    node.iconClose = "${baseURL}/manager/images/threepeople_icon.png";
                } else {
                    node.icon = "${baseURL}/manager/images/twopeople_icon.png";
                }
                if(node.parentId!=""){
                    node.nocheck="true";                        	
                } 
            });
            return data;
        }
    }

    /**
     * 查看组织机构具体信息
     * @param event
     * @param treeId
     * @param treeNode
     */
    function seachOrgNode(event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("chose_orgTree");
        zTree.expandNode(treeNode); 
        $("#personFrame").attr("src","${baseURL}/manager/include/personList.jsp?nodeId="+treeNode.nodeId);
    }

    /**
     * 控制节点是否能被选中
     * @param treeId
     * @param treeNode
     * @param clickFlag
     */
    function beforNodeClick1(treeId, treeNode, clickFlag) {
        if (!allowClick1) {
            allowClick1 = true;
            return false;
        } else {
            return true;
        }
    }
    var template = '<li class="selected_personnel_item pl"><span class="img"><img src="@pic"></span><span class="name">@userName</span><i onmouseup=liRemoveUser(this,"@paramNum","@userId")></i></li>';
    //确定按钮点击，调用回调函数
    	function chooseuserok(){
   
    		var choose_user_id=$("#chooseIds").val();
    		var choose_person_name=$("#chooseNames").val();
    		var picIds=$("#picIds").val();
    		var userId="userId"+"${param.num}";
    	    var userName="nodeUserName"+"${param.num}";
    	    var nodeUserImgs="nodeUserImgs"+"${param.num}";
    	    var nodePic="nodePic"+"${param.num}";
    	    
    	    var json = {
    	    	ids:choose_user_id,
    	    	names:choose_person_name
    	    }
    	    json.ids = $.trim(json.ids.replace(/\|/g," ")).split(" ").join(",");
    	    json.names = $.trim(json.names.replace(/\|/g," ")).split(" ").join(",");
    	    window.opener.getUserCallBack(json);
    	   
    		window.close();
    	
    }
    		
    //取消
    function combackUser(){
    	window.close();
	}

  
</script>

</body>
</html>
