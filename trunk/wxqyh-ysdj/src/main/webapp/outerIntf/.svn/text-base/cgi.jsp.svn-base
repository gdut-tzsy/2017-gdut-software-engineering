<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext"%>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@ page import="cn.com.do1.component.addressbook.contact.service.IContactService"%>
<%@ page import="cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO"%>
<%@ page import="cn.com.do1.component.outerinterface.userinterface.service.IUserinterfaceService"%>
<%@ page import="cn.com.do1.common.exception.BaseException"%>
<%@page import="cn.com.do1.component.outerinterface.userinterface.service.impl.UserinterfaceServiceImpl"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="cn.com.do1.component.errcodedictionary.errcode.service.IErrcodeService"%>
<%
	Logger logger = LoggerFactory.getLogger(UserinterfaceServiceImpl.class);
	//IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
	//	TbQyUserInfoVO vo =  contactService.findUserInfoByUserId("do1qy_13631344127");
	
	String urlId=request.getParameter("urlId");
	if(null==urlId)
	return;
	String code = "";
	String []str =new String[2];
	try{
		IUserinterfaceService userService = DqdpAppContext.getSpringContext()
				.getBean("userinterfaceService", IUserinterfaceService.class);
		 userService.disposeURL(urlId).toArray(str);
	} catch (BaseException e) {
		logger.error("",e);
	} catch (Exception e) {
		logger.error("",e);
	}
	//获取session中的userID
	///String userId = session.get("userId");
	String url=str[0];
		if(null==url)
	return;
	//System.out.println(url);
	if(url.indexOf("?")>=0){
		url = url + "&code=" + str[1];
	}else{
		url = url + "?code=" + str[1];
	}

	response.getWriter().write("<script>window.location.href='"+url+"'</script>");
%>