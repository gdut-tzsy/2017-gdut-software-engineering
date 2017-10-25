<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext"%>
<%@ page import="cn.com.do1.component.core.WxqyhAppContext" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@ page import="cn.com.do1.component.addressbook.contact.service.IContactService"%>
<%@ page import="cn.com.do1.component.addressbook.contact.service.impl.ContactServiceImpl"%>
<%@ page import="cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO"%>
<%@ page import="cn.com.do1.component.addressbook.contact.vo.UserInfoVO" %>
<%@ page import="cn.com.do1.common.exception.BaseException"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="cn.com.do1.component.errcodedictionary.errcode.service.IErrcodeService"%>
<%
	Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
	//String mobile="";
	String userId="";
	try{
		UserInfoVO userInfoVO = WxqyhAppContext.getCurrentUserForWeb(ServletActionContext.getRequest());
		if(null != userInfoVO){
			IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
			TbQyUserInfoVO vo =  contactService.findUserInfoByUserId(userInfoVO.getUserId());
			if(null==vo){
				return;
			}
			//mobile=vo.getMobile();
			userId=vo.getUserId();
			/*  if(mobile.length()==0)
				 mobile=userId; */
			
			String url="http://liuliang.do1.net.cn:8088/do1service/jsp/weixin/buyflow/index.jsp?showwxpaytitle=1&mobile="+userId;
			//response.getWriter().write("电话号码："+mobile);
			response.getWriter().write("<script>window.location.href='"+url+"'</script>");
		}
	} catch (BaseException e) {
		logger.error("liuliang.jsp",e);
	} catch (Exception e) {
		logger.error("liuliang.jsp",e);
	}
%>