<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="cn.com.do1.component.addressbook.contact.vo.UserInfoVO" %>
<%@ page import="cn.com.do1.common.util.DateUtil" %>
<%@ page import="java.util.Date" %>
<%@ page import="cn.com.do1.common.exception.BaseException" %>
<%@page import="org.slf4j.LoggerFactory" %>
<%@page import="org.slf4j.Logger" %>
<%@page import="cn.com.do1.common.util.security.AESEncryptUtil" %>
<%@ page import="cn.com.do1.component.util.HardwareUtil" %>
<%
    Logger logger = LoggerFactory.getLogger(WxqyhAppContext.class);
    //StringBuilder url = new StringBuilder("http://ss.1234.gov.cn/wap.php?c=Home&a=mianzhuce&host=yl");
    StringBuilder param = new StringBuilder();
    String departNames = "";
    String tm=DateUtil.format(new Date(), "yyyyMMddHHmmss");
    param.append("&tm=" + tm);
    try {
        UserInfoVO infoVO = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        param.append("&qdid=" + infoVO.getWxDeptIds());
        if (infoVO.getDeptFullNames().length() > 100) {
            departNames = infoVO.getDepartmentNames();
        } else {
            departNames = infoVO.getDeptFullNames();
        }
        String password = infoVO.getOrgId() + tm;
        String tarurl = request.getParameter("tarurl");
        if(tarurl.indexOf("www.idoofen.cn")>-1){
            password = "c67f7ab76c6446629bf1789b0e2cc4a0"+tm;
        }
        String encryptResultStr = HardwareUtil.parseByte2HexStr(AESEncryptUtil.encrypt(departNames.getBytes("utf-8"), password));
        param.append("&qdnm=" + encryptResultStr);
        param.append("&wxu=" + infoVO.getWxUserId());
        String mobile=infoVO.getMobile();
        if(!AssertUtil.isEmpty(mobile)){
            param.append("&m=" + HardwareUtil.parseByte2HexStr(AESEncryptUtil.encrypt(mobile.getBytes("utf-8"), password)));
        }else{
            param.append("&m=0");
        }
        //response.getWriter().write("<script>window.location.href='" + url.toString() + "'</script>");
    } catch (BaseException e) {
        response.getWriter().write(e.getMessage());
        logger.error("user_dp_agent.jsp", e);
    } catch (Exception e) {
        response.getWriter().write(e.getMessage());
        logger.error("user_dp_agent.jsp", e);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>"/>
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>

</head>
<body>
<script type="text/javascript">
    $(function () {
        var str_url = "http://" + decodeURI("${param.tarurl}");
        var url_Regex = /http:\/\/([^\/]+)/i;
        var hostname = str_url.match(url_Regex);
        if (/1234\.gov\.cn/.test(hostname)) {
            window.location.href = str_url + "<%=param.toString()%>";
        } else if(/www\.idoofen\.cn/.test(hostname)){
            if(str_url.indexOf("?")==-1){
                window.location.href = str_url + "?i=1" + "<%=param.toString()%>";
            }else{
                window.location.href = str_url + "<%=param.toString()%>";
            }
            //window.location.href = str_url + "<%=param.toString()%>";
        } else{
            _alert('提示', '你的链接不合法', '确认', function () {
                WeixinJS.back();
            });
        }
    });
</script>
</body>
</html>
