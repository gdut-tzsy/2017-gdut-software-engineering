<%@page import="cn.com.do1.component.bizcard.bizcard.model.TbQyUserTokenPO"%>
<%@page import="cn.com.do1.component.addressbook.contact.vo.UserInfoVO"%>
<%@page import="cn.com.do1.common.util.AssertUtil"%>
<%@page import="cn.com.do1.component.bizcard.bizcard.model.TbQyCcCardInfoPO"%>
<%@page import="cn.com.do1.component.bizcard.bizcard.service.IBizcardService"%>
<%@page import="cn.com.do1.dqdp.core.DqdpAppContext"%>
<%@page import="cn.com.do1.component.bizcard.bizcard.service.impl.BizcardServiceImpl"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.common.exception.BaseException"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%@ page import="org.slf4j.Logger"%>
<%@ include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>
        <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
	    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
	
		<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
<%
	Logger logger = LoggerFactory.getLogger(BizcardServiceImpl.class);
	try{
		IBizcardService bizcardService = DqdpAppContext.getSpringContext().getBean("bizcardService", IBizcardService.class);
		
		//获取名片信息
		TbQyCcCardInfoPO cardInfo = bizcardService.searchByPk(TbQyCcCardInfoPO.class, request.getParameter("id"));
		if(null != cardInfo && !AssertUtil.isAllEmpty(cardInfo.getUrl())){
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
			//生成有效验证token
			TbQyUserTokenPO token = bizcardService.generateToken(userInfo);
			if(null == token){
				return;
			}
			//url编码：& %26,	= %3d
			String url = cardInfo.getUrl() + "%26qw_id%3D"+token.getUserId()+"%26token%3D"+token.getToken()+"&qw_id="+token.getUserId()+"&token="+token.getToken();
			//跳转
			response.sendRedirect(url);
		}
	} catch (BaseException e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	} catch (Exception e) {
		response.getWriter().write(e.getMessage());
		logger.error("cc.jsp",e);
	}
%>
