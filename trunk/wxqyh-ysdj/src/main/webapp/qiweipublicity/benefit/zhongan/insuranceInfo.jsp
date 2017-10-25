<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <script src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
</head>
<body>
	<form action="${baseURL}/zhongan/zhonganAction!addAndUpdateEnterpriseInfo.action" method="post" id="enterpriseInfoForm">
		<div class="i-from-item"><span class="tit">企业联系人信息</span> </div>
        <div class="i-from-item"> 
            <span class="iblock w120"><font class="red">* </font>联系人姓名：</span>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.contactPeople" id="contactPeople"/>
            <span class="iblock w120 ml50"><font class="red">* </font>身份证 :</span>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.contactCertino" id="contactCertino"/>
		</div>
		<div class="i-from-item"> 
            <span class="iblock w120"><font class="red">* </font>电子邮件：</span>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.contactEmail" id="contactEmail"/>
            <span class="iblock w120 ml50"><font class="red">* </font>手机号码：</span>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.contactPhone" id="contactPhone"/>
		</div>	
		<div class="i-from-item mt30"><span class="tit">企业基本信息</span> </div>
		<div class="i-from-item"> 
            <span class="iblock w120"><font class="red">* </font>企业名称：</span>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.companyName" id="companyName"/>
            <span class="companyTip">请填写真实完整的企业名称，该名称默认为发票抬头</span>
        </div>
        <div class="i-from-item"> 
            <span class="iblock w120"><font class="red">* </font>企业类型：</span>
            <select class='iSelect w150' id="enterpriseType_one">
            	<option value=''>请选择</option>
            	<option>请选择1</option>
            	<option>请选择2</option>
            </select>
         	<select class='iSelect w150' id="enterpriseType_two">
            	<option>请选择</option>
            	<option>请选择1</option>
            	<option>请选择2</option>
            </select>
            <input type="hidden" value="" name="tbZhonganEnterpriseInfoPO.companyTypeOne" id="companyTypeOne">
            <input type="hidden" value="" name="tbZhonganEnterpriseInfoPO.companyTypeTwo" id="companyTypeTwo">
        </div>
        <div class="i-from-item">
       		<span class="iblock w120"><font class="red">* </font>证件号码：</span>
        	<select class='iSelect w150'>
            	<option>组织机构代码证</option>
            </select>
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.orgCode" id="orgCode"/>
            <!-- <span class="x-field-error x-field-error1" id="companyerror" style="" data-title="企业类型必须选择！"><span class="x-icon x-icon-mini x-icon-error">!</span></span> -->
        </div>
        <div class="i-from-item">
        	<span class="iblock w120"><font class="red">* </font>企业所在地：</span>
        	<select class='iSelect w150' id="area_one">
            	<option>请选择</option>
            	<option>请选择1</option>
            	<option>请选择2</option>
            </select>
            <select class='iSelect w150' id="area_two">
            	<option>请选择</option>
            	<option>请选择1</option>
            	<option>请选择2</option>
            </select>
            <select class='iSelect w150' id="area_three">
            	<option>请选择</option>
            	<option>请选择1</option>
            	<option>请选择2</option>
            </select>
            <input type="hidden" value="" name="tbZhonganEnterpriseInfoPO.companyProvinceCode" id="companyProvinceCode">
            <input type="hidden" value="" name="tbZhonganEnterpriseInfoPO.companyCityCode" id="companyCityCode">
            <input type="hidden" value="" name="tbZhonganEnterpriseInfoPO.companyCountryCode" id="companyCountryCode">
            
            <input type="text" class="iText w300" name="tbZhonganEnterpriseInfoPO.companyAddress" id="companyAddress"/>
        </div>
        <div class="tc mt50"><div class="iBtn" onclick="saveEnterpriseInfo();">提交</div></div>
</form>
<%@include file="../../msgBoxs.jsp" %>
</body>
</html>
<script>
	$(document).ready(function() {
		//========================公司类型=========================
		//类型事件
		$("#enterpriseType_one").change(function(){
			var val=$(this).val();
			if(""==val){
				$("#enterpriseType_two").html("");
				$("#enterpriseType_two").append("<option value=''>请选择</option>");
			}else{
				//改变下级
				getCompanyType(val,"2");
			}
			$("#companyTypeOne").val($(this).val());
			$("#companyTypeTwo").val("");
		});
		$("#enterpriseType_two").change(function(){
			$("#companyTypeTwo").val($(this).val());
		});
		
		//================================地区========================================
		//区域时间
		$("#area_one").change(function(){
			var val=$(this).val();
			if(""==val){
				$("#area_two").html("");
				$("#area_two").append("<option value=''>请选择</option>");
				$("#area_three").html("");
				$("#area_three").append("<option value=''>请选择</option>");
			}else{
				$("#area_three").html("");
				$("#area_three").append("<option value=''>请选择</option>");
				//改变下级
				getArea(val,"2");
			}
			$("#companyProvinceCode").val($("#area_one").find("option:selected").text());
			$("#companyCityCode").val("");
			$("#companyCountryCode").val("");
		});
		$("#area_two").change(function(){
			var val=$(this).val();
			if(""==val){
				$("#area_three").html("");
				$("#area_three").append("<option value=''>请选择</option>");
			}else{
				//改变下级
				getArea(val,"3");
			}
			$("#companyCityCode").val($(this).find("option:selected").text());
			$("#companyCountryCode").val("");
		});
		$("#area_three").change(function(){
			$("#companyCountryCode").val($("#area_three").find("option:selected").text());
		});
		//=========================加载数据信息========================
		loadEnterprise("1");
	});
	
	function loadEnterprise(isFirstLoad){
		$.ajax({
    		url : "${baseURL}/zhongan/zhonganAction!getEnterpriseInfo.action",
    		data:{
    			"isFirstLoad":isFirstLoad//1的时候加载地区信息
    		},
    		type : "get",
    		async : false,
    		dataType : "json",
    		success : function(result) {
    			if (result.code == "0") {
    				if("1"==isFirstLoad){
						//加载类型select
						var typeListOne=result.data.typeListOne;
						setCompanyTypeSelect(typeListOne,"1");
						var typeListTwo=result.data.typeListTwo;
						setCompanyTypeSelect(typeListTwo,"2");
						//加载区域select
						var areaListOne=result.data.areaListOne;
						setAreaSelect(areaListOne,"1");
						var areaListTwo=result.data.areaListTwo;
						setAreaSelect(areaListTwo,"2");
						var areaListThree=result.data.areaListThree;
						setAreaSelect(areaListThree,"3");
    				}
    				var vo=result.data.TbZhonganEnterpriseInfoPO;
    				if(null==vo || ""==vo){
    					return;
    				}
					$("#contactPeople").val(vo.contactPeople);
					$("#contactCertino").val(vo.contactCertino);
					$("#contactEmail").val(vo.contactEmail);
					$("#contactPhone").val(vo.contactPhone);
					$("#companyName").val(vo.companyName);
					$("#orgCode").val(vo.orgCode);
					//公司类型
					$("#companyTypeOne").val(vo.companyTypeOne);
					$("#companyTypeTwo").val(vo.companyTypeTwo);
					$("#enterpriseType_one").val(vo.companyTypeOne);
					$("#enterpriseType_two").val(vo.companyTypeTwo);
					//设置地区和选中
					$("#companyProvinceCode").val(vo.companyProvinceCode);
					$("#companyCityCode").val(vo.companyCityCode);
					$("#companyCountryCode").val(vo.companyCountryCode);
					$("#companyAddress").val(vo.companyAddress);
					$("#area_one").find("option").each(function(){
						if($(this).html()==vo.companyProvinceCode){
							$("#area_one").val($(this).attr("value"));
							return false;
						}
					});
					$("#area_two").find("option").each(function(){
						if($(this).html()==vo.companyCityCode){
							$("#area_two").val($(this).attr("value"));
							return false;
						}
					});
					$("#area_three").find("option").each(function(){
						if($(this).html()==vo.companyCountryCode){
							$("#area_three").val($(this).attr("value"));
							return false;
						}
					});
    			}else{
    				_alert("错误提示",result.desc);
    			}
    		},
    		error : function() {
    			_alert("错误提示", "系统繁忙！");
    		}
    	});
	}
	//获取公司类型
	function getCompanyType(parentId,level){
		$.ajax({
    		url : "${baseURL}/zhongan/zhonganAction!getEnterPriseType.action",
    		data:{
    			"parentId":parentId
    		},
    		type : "get",
    		async : true,
    		dataType : "json",
    		success : function(result) {
    			if (result.code == "0") {
    				var list=result.data.typeList;
    				setCompanyTypeSelect(list,level);
    			}else{
    				_alert("提示消息",result.desc);
    			}
    		},
    		error : function() {
    			_alert("错误提示", "系统繁忙！");
    		}
    	});
	}
	//设置类型select
	function setCompanyTypeSelect(list,level){
		if("1"==level){
			//需要改变自己内容
			$("#enterpriseType_one").html("");
			$("#enterpriseType_one").append("<option value=''>请选择</option>");
			if(null!=list && ""!=list && list.length>0){
				for(var i=0;i<list.length;i++){
					var vo=list[i];
					$("#enterpriseType_one").append("<option value='"+vo.id+"'>"+vo.name+"</option>")
				}
			}
			$("#enterpriseType_two").html("");
			$("#enterpriseType_two").append("<option value=''>请选择</option>");
		}else if("2"==level){
			$("#enterpriseType_two").html("");
			$("#enterpriseType_two").append("<option value=''>请选择</option>");
			if(null!=list && ""!=list && list.length>0){
				for(var i=0;i<list.length;i++){
					var vo=list[i];
					$("#enterpriseType_two").append("<option value='"+vo.id+"'>"+vo.name+"</option>");
				}
			}
		}
	}
	
	//获取区域
	function getArea(parentId,level){
		$.ajax({
    		url : "${baseURL}/zhongan/zhonganAction!getArea.action",
    		data:{
    			"parentId":parentId
    		},
    		type : "get",
    		async : true,
    		dataType : "json",
    		success : function(result) {
    			if (result.code == "0") {
    				var list=result.data.areaList;
    				setAreaSelect(list,level);
    			}else{
    				_alert("提示消息",result.desc);
    			}
    		},
    		error : function() {
    			_alert("错误提示", "系统繁忙！");
    		}
    	});
	}
	//设置区域select
	function setAreaSelect(list,level){
		if("1"==level){
			//需要改变自己内容
			$("#area_one").html("");
			$("#area_one").append("<option value=''>请选择</option>");
			if(null!=list && ""!=list && list.length>0){
				for(var i=0;i<list.length;i++){
					var vo=list[i];
					$("#area_one").append("<option value='"+vo.id+"'>"+vo.name+"</option>");
				}
			}
			$("#area_two").html("");
			$("#area_two").append("<option value=''>请选择</option>");
			$("#area_three").html("");
			$("#area_three").append("<option value=''>请选择</option>");
		}else if("2"==level){
			$("#area_two").html("");
			$("#area_two").append("<option value=''>请选择</option>");
			if(null!=list && ""!=list && list.length>0){
				for(var i=0;i<list.length;i++){
					var vo=list[i];
					$("#area_two").append("<option value='"+vo.id+"'>"+vo.name+"</option>");
				}
			}
			$("#area_three").html("");
			$("#area_three").append("<option value=''>请选择</option>");
		}else if("3"==level){
			$("#area_three").html("");
			$("#area_three").append("<option value=''>请选择</option>");
			if(null!=list && ""!=list && list.length>0){
				for(var i=0;i<list.length;i++){
					var vo=list[i];
					$("#area_three").append("<option value='"+vo.id+"'>"+vo.name+"</option>");
				}
			}
		}
	}
	
    function saveEnterpriseInfo() {
    	_doCommonSubmit("enterpriseInfoForm", "", {
    		ok:function(result){
    				//document.location.href = document.location.href;
    				document.location.href =document.location.href;
    			}
    	});
    }
</script>
