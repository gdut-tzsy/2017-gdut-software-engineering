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
<script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
<script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>

<script>
		$(document).ready(function () {
			WeixinJS.back();
		});
	
</script>
