<%@page language="java" contentType="text/html; charset=UTF-8" %>
<script type="text/javascript" charset="utf-8" src="${baseURL}/jsp/wap/uploadImage/js/global.js"></script>
<script type="text/javascript" charset="utf-8" src="${baseURL}/jsp/wap/uploadImage/js/global1.js?ver=<%=jsVer%>"></script>
<script type="text/javascript" charset="utf-8" id="sea" src="${baseURL}/jsp/wap/uploadImage/js/seajs.js"></script>
<script type="text/javascript" charset="utf-8" src="${baseURL}/jsp/wap/uploadImage/js/register.js?ver1=<%=jsVer%>"></script>
<script type="text/javascript" charset="utf-8" src="${baseURL}/jsp/wap/uploadImage/js/mobileBUGFix.mini.js"></script>
<script type="text/javascript" charset="utf-8" src="${baseURL}/jsp/wap/uploadImage/js/uploadImage2.js?ver1=<%=jsVer%>"></script>

<form action="${baseURL}/portal/imageupload/imageUploadAction!doOpenUploadImageBase64.action" method="post" id="imgFileBaseForm" encType="multipart/form-data">
	<input type="hidden" name="imgFileBase" id="imgFileBase" value="">
	<input type="hidden" name="agent" id="uploadImgAgent" value="">
</form>

<script type="text/javascript">
$(function() {
	//alert(wxqyh.agent);
	if(null!=wxqyh.agent && "undefined"!=wxqyh.agent && ""!=$.trim(wxqyh.agent)){
		$("#uploadImgAgent").val(wxqyh.agent);
	}
	
    seajs.use(['register'], function(register) {
        register.init();
        //city.init();
    });
});
</script>
    