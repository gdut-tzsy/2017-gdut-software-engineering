<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value="flowadd"></jsp:param>
    <jsp:param name="mustPermission" value="flowadd"></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" href="${baseURL}/manager/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/ku.css">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
	<%-- <script src="${baseURL}/js/3rd-plug/jquery/ajaxfileupload.js"></script> --%>
	<script src="${baseURL}/js/3rd-plug/jquery/ajaxfileupload-2.0.js"></script>
	<script src="${baseURL}/js/3rd-plug/kindeditor/kindeditor.js"></script>
	<script src="${baseURL}/js/3rd-plug/kindeditor/lang/zh_CN.js"></script>
	<!-- 上传媒体文件引入  start -->
    <script src="${baseURL}/manager/js/uploadfile.js?ver=<%=jsVer%>"></script>
	<!-- 上传媒体文件引入  end -->
</head>
<body>

<div class="topMainBar clearfix" id="back">
    <div class="fl flMainBar"><a href="javascript:history.back();" class="btn twoBtn">返回</a>
    </div>
    <div class="fr frMainBar"></div>
</div>
<div class="cPage">
   
</div>



<script type="text/javascript">
function checkLading(obj){
	   if(obj.checked){
		   $("#audit_users_div").hide();
		}
		else{
			$("#audit_users_div").show();
		}
}
 
    $(function(){
    	   AddPerson(); 
		init();
		usersinit();
		
		//groupInit();
	
$('#more-pop-close').click(function(){
	$('#more-pop').hide();
	$('#more-pop-bg').hide();
})
$('.more-footer span').click(function(){
	$('#more-pop').hide();
	$('#more-pop-bg').hide();
})
$('.more-pop-add').click(function(){
	$('#more-pop').hide();
	$('#more-pop-bg').hide();
	AddPerson();
})
})


function AddPerson(){
$("#chooseDeptAndUs").hide();
$("#back2").hide();
$(".cPage").hide();
$("#back").hide();
$("#chooseUsers").show();
}

function AddGroup(){
$("#chooseDeptAndUs").hide();
$("#back2").hide();
$(".cPage").hide();
$("#back").hide();
$("#chooseGroup").show();
} 

function liclose(obj){
var num;
if($(obj).parent('li').attr('class')=='selected_personnel_item'){
	  var user=$(obj).parent('li').find('input').val().split("\|");
	  var userId=user[0];
	  var userName=user[1];
	  var pic=user[2];
	  var usersIds=$("#userIds").val();
	  var userList=usersIds.split("\|");
	  var index=0;
	  for(var i=0;i<userList.length;i++){
		  if(userList[i]==userId){
			  index=i; 
		  }
	  }
	  usersIds=usersIds.replace(userId+"|","");
	  $("#userIds").val(usersIds);
	  $("#chooseIds").val(usersIds);
	  var person_name=$("#chooseNames").val();
	  person_name=person_name.replace(userName+"|","");
	  $("#chooseNames").val(person_name);
	  var person_pic=$("#picIds").val();
	  person_pic=person_pic.replace(pic+"|","");
	  $("#picIds").val(person_pic);
	  num=parseFloat($('.selected_num2').html().replace(/\(|\)/g,''))-1;
	  $('#selected_personnel').find('.selected_personnel_item').eq(index).remove();
	  $('#more-pop-con').find('.selected_personnel_item').eq(index).remove();
	  if(num<=0){num=0}
	  if(num>=13){
			$('#amore').css('display','block');
		}else{
			$('#amore').hide();
		}
	 $('.selected_num2').html('('+num+')');
	 $('.more-pop-tit span.num').html('('+num+')');
	 var depart=$("#chooseDepartId").val();
	// $("#personFrame").attr("src","${baseURL}/manager/activity/personList.jsp?nodeId="+depart);
	 
}
} 
  //刷新高度
  $(document).click(function(){
	 
	  _resetFrameHeight();
  });
  
</script>
<%@include file="flow_node_chooseUsers.jsp"%> 
<%@include file="../../../include/chooseDept.jsp" %>
<%@include file="../../../msgBoxs.jsp" %>
</body>
</html>
