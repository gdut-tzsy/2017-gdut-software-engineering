<%@page import="cn.com.do1.component.common.util.JSONUtil" contentType="text/html; charset=UTF-8"%>
<%@page language="java" import="java.util.*,cn.com.do1.component.wxcgiutil.message.NewsMessageVO
,cn.com.do1.component.wxcgiutil.WxMessageUtil,cn.com.do1.component.wxcgiutil.WxAgentUtil,cn.com.do1.component.util.Configuration
,cn.com.do1.component.util.org.OrgUtil
" %><%
Map map = request.getParameterMap();
for(Object key :map.keySet()){
	System.out.println(key.toString()+"\n"+request.getParameter(key.toString()));
	
	
}
map = JSONUtil.stringToObject(request.getParameter("data"), Map.class);

NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();

newsMessageVO.setTouser(request.getParameter("toUser"));
String corpId = request.getParameter("corpId");



newsMessageVO.setTitle("[" + map.get("instanceTitle")+ "]回调通知");
newsMessageVO.setDescription("表单["+ map.get("instanceTitle")+"]流程结束，请查看。");
newsMessageVO.setUrl(Configuration.WX_PORT + "/jsp/wap/form/detail.jsp?id=" + map.get("id"));

newsMessageVO.setCorpId(corpId);
newsMessageVO.setAgentCode(WxAgentUtil.getFormCode());
newsMessageVO.setOrgId(OrgUtil.getOrgIdByCorpId(corpId));

newsMessageVO.setDuration("0");
WxMessageUtil.sendNewsMessage(newsMessageVO);
%>{"code":0,"desc":"success"}