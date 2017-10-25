<%@page import="cn.com.do1.common.util.AssertUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html>
    <head>
        <meta charset="utf-8">
        <title>提示</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="css/ku.css" />
        <link rel="stylesheet" href="css/font-awesome.min.css" />
    </head>
    <body>
        <div class="wrap">
            <div class="wrap_inner">
                <div class="jump_tips">
                    <div class="j_tips_img"> <i class="fa error fa-times-circle"></i></div>
                    <div class="j_tips_text">包含敏感字符：
                    	<%
                    	String validates = (String)request.getAttribute("infoData");
                    	if(!AssertUtil.isEmpty(validates)){
                    		//StringBuffer sb = new StringBuffer();
                    		//sb.append(b)
                    		out.print(validates);
                    	}
                    	%>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>