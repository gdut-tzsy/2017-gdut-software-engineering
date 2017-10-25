function head(name,url_first,text_first,url_second,text_second,time){
	var name = name ? name : "";
	var url_first = url_first ? url_first : "";
	var text_first = text_first ? text_first : "";
	var url_second = url_second ? url_second : "";
	var text_second = text_second ? text_second : "";
	var time = time ? " | 登录时间：" + time : "";
	var str = "<div class='head'><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td width='40%' class='headLeft' ><img src='"+baseURL+"/themes/default/images/logo.gif' title='道一内部信息化平台' /></td>" +
      		"<td class='headRight'><div class='loginmsg'>" +
			"欢迎您：" + name + " |<a href='" + url_first + "'>" + text_first + "</a>|<a href='" + url_second + "' class='exit'>" + text_second + "</a>" + time + "</div>" +
			"<div class='state'>这里是操作提示，已将所选提交 | <a href='#'>撤消操作</a></div></td></tr></table></div>";
	return str;
};