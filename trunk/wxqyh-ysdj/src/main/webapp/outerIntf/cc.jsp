<%@page import="cn.com.do1.component.bizcard.bizcard.model.TbQyBizcardSettingPO"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.component.bizcard.bizcard.model.TbQyUserTokenPO"%>
<%@ page import="cn.com.do1.component.bizcard.bizcard.service.impl.BizcardServiceImpl"%>
<%@ page import="cn.com.do1.component.bizcard.bizcard.service.IBizcardService"%>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext"%>
<%@ page import="cn.com.do1.common.exception.BaseException"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%@ page import="org.slf4j.Logger"%>
<%@ include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>


<%
	Logger logger = LoggerFactory.getLogger(BizcardServiceImpl.class);

	String url="";
	boolean enableCC=true;
	try{
		IBizcardService bizcardService = DqdpAppContext.getSpringContext().getBean("bizcardService", IBizcardService.class);
		TbQyUserTokenPO token = bizcardService.generateToken(WxqyhAppContext.getCurrentUser(request));
		if(null == token){
			return;
		}
		TbQyBizcardSettingPO setting = bizcardService.getBizcardSettingByOrgId(token.getOrgId());
		if(null == setting || !"1".equals(setting.getVal())){//'是否禁用,1：启用，0：禁用',
			enableCC=false;
		}else{
			//CC名片的链接地址
			url=Configuration.SAVE_CARD_URL_CC+"/card?qw_id="+token.getUserId()+"&token="+token.getToken();
		}
	} catch (BaseException e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	} catch (Exception e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	}
%>

<!DOCTYPE html>
<html>
<head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        
        <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
		<link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
		<script type="text/javascript" src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script type="text/javascript" src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>

    </head>
<body>
<%
	/*
	Logger logger = LoggerFactory.getLogger(BizcardServiceImpl.class);

	try{
		IBizcardService bizcardService = DqdpAppContext.getSpringContext().getBean("bizcardService", IBizcardService.class);
		TbQyUserTokenPO token = bizcardService.generateToken(WxqyhAppContext.getCurrentUser(request));
		if(null == token){
			return;
		}
		TbQyBizcardSettingPO setting = bizcardService.getBizcardSettingByOrgId(token.getOrgId());
		if(null == setting || !"1".equals(setting.getVal())){//'是否禁用,1：启用，0：禁用',
			response.getWriter().write("<script type=\"text/javascript\">window.onload=function(){_alert('提示','管理员未启用名片功能，暂不能使用。','确认',{'ok':function(){WeixinJS.back();},'fail':function(){}})}</script>");
		}else{
			//CC名片的链接地址
			String url=Configuration.SAVE_CARD_URL_CC+"/card?qw_id="+token.getUserId()+"&token="+token.getToken();
			response.getWriter().write("<script type=\"text/javascript\">window.location.href='"+url+"'</script>");
		}
	} catch (BaseException e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	} catch (Exception e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	}
	*/
%>
<script type="text/javascript">
	$(function(){
		if(<%=!enableCC%>){
			_alert('提示','管理员未启用名片功能，暂不能使用。','确认',{'ok':function(){WeixinJS.back();},'fail':function(){}});
		}else {
			window.location.href='<%=url%>';
		}
	});
</script>
</body>
</html>
