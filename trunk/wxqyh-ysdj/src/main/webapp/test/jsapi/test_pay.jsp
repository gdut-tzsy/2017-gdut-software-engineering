<%@page import="org.apache.struts2.components.Include"%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>发红包</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
		<script type="text/javascript" src="${baseURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js"></script>
        <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

		<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
	       
        <script>
        
			var range = "2";//消息推送范围，1所有人   2部门   3特定对象
        	var template1 = ''
	        	+'<li class="toli" style="display:@Show"> '
	        	+'    <input type="hidden" name="incharges" value="@UserId" /> '
	        	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'to\',@ArrayValue)" ></a> '
	        	+'    <p class="img"> '
	        	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
	        	+'    </p> '
	        	+'    <p class="name">@UserName</p> '
	        	+'</li>';
        	var template2 = ''
            	+'<li class="ccli" style="display:@Show"> '
            	+'    <input type="hidden" name="relatives" value="@UserId" /> '
            	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'cc\',@ArrayValue)" ></a> '
            	+'    <p class="img"> '
            	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
            	+'    </p> '
            	+'    <p class="name">@UserName</p> '
            	+'</li>';
            
        	function init(){
        		//初始化默认目标对象
        		showTargetRange();
        	}
    		//加载默认目标对象
        	function showTargetRange(){
	        	$.ajax({
	        		url:"${baseURL}/portal/managesetting!getTarget.action",
	        		type:"post",
	        		dataType:"json",
	        		success:function(result){
	        			if(result.code=="0"){
        					isVisitAllTarget = result.data.isVisitAll;
	        				//设置对象
	           				if(result.data.range=="2"){
	        					$("#rangeId").html("本部门");
	        					range="2";
	        					activeRange("2");
	        				}
	        				else{
	        					$("#rangeId").html("所有人");
	        					range="1";
	        					activeRange("1");
	        				}
	        			}
	        		},
	        		error:function(){
	        			showMsg("",internetErrorMsg,1);
	        		}
	        	});
        	}
        	//提交
        	function commit(){
        		if($("#totalCount").val()==""){
        			showMsg("","请输入红包个数",1);
        			return ;
        		}
        		if($("#totalAmount").val()==""){
        			showMsg("","请输入红包总金额",1);
        			return ;
        		}
        		if($("#title").val()==""){
        			$("#title").val("恭喜发财，大吉大利");
        		}
        		else if($("#title").val().length>60){
        			showMsg("","祝福语过长，请重新编辑",1);
        			return ;
        		}
                $("#range").val(range);
                if($("#userCount").val()==""){
                	$("#userCount").val("0");
                }
        		showLoading("付款后须等待结果...");
        		document.getElementById("activityform").action ="${baseURL}/portal/qiweiredpack/qiweiredpackAction!addReadpack.action";            				           		
        		$("#activityform").ajaxSubmit({
        			type:"POST",
	        		dataType:"json",
	        		success:function(result){
	        			if(result.code=="0"){
    	        			hideLoading();
	        				var param = result.data.orderInfo;
	        				wx.chooseWXPay({
	        		    		appId:param.appId,
	        		    	    timestamp: param.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	        		    	    nonceStr: param.nonceStr, // 支付签名随机串，不长于 32 位
	        		    	    package: param.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	        		    	    signType: param.signType,// 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	        		    	    paySign: param.paySign, // 支付签名
	        		    	    success: function (res) {
	    	                    	sendReadpack(result.data.id);
	        		    	    },
		    	        		error:function(){
		    	        			showMsg("",res.errMsg,1);
		    	        		}
	        		    	});
	        			}else{
	            			showMsg("",result.desc,1);
	        			}
	        			hideLoading();
	        		},
	        		error:function(){
	        			showMsg("",internetErrorMsg,1);
	        			hideLoading();
	        		}
        		});
        	}
        	function sendReadpack(id){
	        	$.ajax({
	        		url:"${baseURL}/portal/qiweiredpack/qiweiredpackAction!sendReadpack.action",
	        		type:"post",
	        		data:{"id":id},
	        		dataType:"json",
	        		success:function(result){
	        			showMsg("",result.desc,1);
	        		},
	        		error:function(){
	        			showMsg("",internetErrorMsg,1);
	        		}
	        	});
        	}
        	function showDelete(obj){
        		$($(obj).find("a")[0]).toggle();
        	}
        	function deleteUser(obj,type,value){
        		$(obj).parent().remove();
        		if("to"==type){
        			selectedToUser.remove(value);
        		}else{
        			selectedCcUser.remove(value);
        		}
        		
        	}
        	//关闭查看报名信息
        	function comfirm(){
        		
        		if (range == "1") {
        			$("#rangeId").html("所有人");
        		}
        		if (range == "2") {
        			$("#rangeId").html("本部门");
        		}
        		if (range == "3") {//特定对象，判断是否选人
        			$("#rangeId").html("特定对象");
        			var userIds = $("#userIds").val();
        			
        			if (userIds == "") {
        				
        				showMsg("提示信息","你未选择任何人",1,null);
        				return ;
        			}
        			var userIdscount=userIds.split("\|");
        			$("#userCount").val("");
        			$("#userCount").val(userIdscount.length);
        		}
        		
        		$("#wrap_main").hide();
        		$("#tips_main").show();
        	}
        	
        	function choseRange(type) {
        		range = type;
        		if (type == "3") {
        			userSelect('${session.userId}');
        		}
        	}
        </script>
        <style>
        .inputStyle{
	        text-align: left;
			line-height: 18px; 
			border: none;
			font-size: 14px;
			vertical-align: top;
        }
        </style>
    </head>
    
    <body onload="init()">
        <div id="tips_main" class="wrap">
            <div id="main" class="wrap_inner">
				
            	<form action="" id="activityform" onsubmit="return false;">
                <input name="userId" type="hidden" value="${param.userId}" />
                <div class="form-style">
                    <div class="f-item">
                        <div class="inner-f-item item-text flexbox"><span class="f-item-title">红包个数</span>
                        	 <div class="flexItem" style="margin-right:20px;">
	                            <input class="item-select item-text-right" id="totalCount" name="tbQyRedpackSend.totalCount" placeholder="请输入红包个数" placeholder="0"/>
                             </div>
                        </div>
                    </div>
                    <div class="f-item">
                        <div class="inner-f-item item-text flexbox"><span class="f-item-title">红包总金额(元)</span>
                        	 <div class="flexItem" style="margin-right:20px;">
	                            <input class="item-select item-text-right" id="totalAmount" name="totalAmount" placeholder="请输入红包个数" placeholder="0"/>
                             </div>
                        </div>
                    </div>
                    <div class="f-item">
                        <div class="inner-f-item item-text flexbox"><span class="f-item-title">是否拼手气红包</span>
                        	 <div class="flexItem" style="margin-right:20px;">
                        	 	<input type="radio" name="tbQyRedpackSend.isEqual" value="1" checked="checked" />拼手气红包
								<input type="radio" name="tbQyRedpackSend.isEqual" value="0" />等额红包
                             </div>
                        </div>
                    </div>
                    <div class="f-item">
                        <div class="inner-f-item item-text flexbox"><span class="f-item-title">红包类型</span>
                        	 <div class="flexItem" style="margin-right:20px;">
                        	 	<input type="radio" name="tbQyRedpackSend.type" value="1" checked="checked" />拼抢红包
								<input type="radio" name="tbQyRedpackSend.type" value="2" />定向红包
                             </div>
                        </div>
                    </div>
                	<div class="f-item ">
                        <div class="inner-f-item"> 
	                        <div class="flexItem">
                        		<input type="" name="tbQyRedpackSend.wishing" value="" maxlength="30" id="title" placeholder="恭喜发财，大吉大利" class="item-select inputStyle" />
                        	</div>
                        </div>
                    </div>
                    <div class="f-item ku_arrow" onclick="searchRange();">
                        <div class="inner-f-item item-text flexbox"> 
                        	<span class="f-item-title">红包参与对象</span>
                            <p class="flexItem item-right item-text-right pr20">
                            	<span id="rangeId">本部门</span>
                            	<input type="hidden" id="userIds" name="tbQyRedpackSend.sendTarget"/>
                				<input type="hidden" id="deptIds" name="tbQyRedpackSend.deptIds"/>
                            	<input type="hidden" id="range" name="tbQyRedpackSend.ranges"/>
                            	<span id="userCountHtml" style="display:none;"></span>
                            </p>
                        </div>
                    </div>
                    <div class="form_btns mt10">
                        <div class="inner_form_btns">
                            <div class="fbtns flexbox"> 
 								<a class="fbtn btn flexItem" style="margin-left: 5px;" href="javascript:commit()">塞钱进红包</a>
                            </div>
                            <div class="fbtns_desc">请耐心等待塞钱过程。</div>
                        </div>
                    </div>
                </div>
                </form>
            </div>
        </div>
        
        <%@include file="/jsp/wap/include/showMsg.jsp"%>
		<%@include file="/jsp/wap/include/target_select.jsp"%>
    </body>
</html>