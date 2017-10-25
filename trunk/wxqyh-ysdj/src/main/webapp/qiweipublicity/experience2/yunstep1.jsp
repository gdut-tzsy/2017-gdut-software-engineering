<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
  <head>
 <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="">
    <meta name="baidu-site-verification" content="Vup9CGlqbA" /> <!-- 百度检测-->
<meta name="msvalidate.01"content="6C48EF31F67FA6E09A742953ACAB5A39" /><!-- 必应搜索-->
    <title>开通:企业微信接入企微云服务</title>
    <link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src="js/jquery-1.11.1.js"></script>
    <script src="js/common.js?ver=<%=jsVer%>"></script>
    <script src=" http://hm.baidu.com/h.js%3F519281b7063399b3740aeca92f1f624f" type="text/javascript"></script>
  </head>
    <body>
 <%@include file="./header.jsp" %> 
        <div class="container">
         <div class="content text-left yun">
         <div class="yun_top">
           
<div class="yun_top_l"><h2>企业微信接入企微云服务</h2>

<div>操作步骤比较多，请认真按照《接入企微云服务手册》进行操作。</div></div>

<a href="http://wbg.do1.com.cn/handbook.html"  class="yun_top_r" target="_blank">查看接入企微云服务手册</a>
         </div>
               <div class="stepdiv mt25">
           <div class="step first active"><i>1</i>提交企业微信信息</div><div class="steppic"><img src="images/jt-active.jpg"></div>
           <div class="step"><i>2</i>注册企微平台账号</div><div class="steppic"><img src="images/jt.jpg"></div>
           <div class="step"><i>3</i>获取应用配置信息</div><div class="steppic"><img src="images/jt.jpg"></div>
           <div class="step"><i>4</i>企业微信应用配置</div><div class="steppic"><img src="images/jt.jpg"></div>
           <div class="step last"><i>5</i>企微应用中心</div>
          </div>
           <h2 class="text-center mt50">提交企业微信信息</h2>
          <div class="form">
            <form class="form-horizontal" onsubmit="return false;" method="post" accept-charset="utf-8" id="form">
                <div class="form-group mt50">
                  <label class="col-md-3 control-label">企业名称<span class="red">*</span></label>
                  <div class="col-md-9"><input type="text" class="form-control" id="companyname" name="companyname" value="" placeholder="请填写企业真实名称，注册成功后此名称将在企业微信管理后台显示"></div>
                  <div class="col-md-9 col-md-offset-3 error" id="qiyename">请正确填写企业微信名称</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">企业微信CorpID<span class="red">*</span></label>
                  <div class="col-md-9"><input type="text" class="form-control" name="companyCorpID" id="companyCorpID" value="" placeholder="通过企业微信后台 设置-权限管理-新建管理组-开发者凭据获得CorpID"></div>
                  <div class="col-md-9 col-md-offset-3 error">请正确填写企业微信CorpID</div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">企业微信Secret<span class="red">*</span></label>
                  <div class="col-md-9"><input type="text" class="form-control" id="companySecret" name="companySecret" value="" placeholder="通过企业微信后台 设置-权限管理-新建管理组-开发者凭据获得Secret"></div>
                  <div class="col-md-9 col-md-offset-3 error">请正确填写企业微信Secret</div>
                </div>
                <div class="col-md-9 col-md-offset-3" style="display: block; font-weight: normal;">
  <label style="font-weight: normal; margin-left: 30px; color: black;"><input type="checkbox" id="ckProtocal" value="" name="ckProtocal" style="display: inline-block; margin: 0px 10px 0px 0px; vertical-align: middle;">我已经详细阅读并同意《
               <a href="service.html" target="_blank" class="ce">企微服务条款</a>》</label>
               <a target="_blank" style="display: inline-block; color: red; margin-left: 30px; cursor: pointer; font-weight: bold;" href="http://wbg.do1.com.cn/handbook.html">点击查看帮助手册</a></div>
                <div class="form-group">
                <input type="submit" name="submit" value="提交，下一步" class="submit col-md-9 col-md-offset-3" id="submita" style="display:block" onclick="submitform()">
                <input type="submit" name="submit" value="提交，下一步" class="submit col-md-9 col-md-offset-3" id="submitb" style="display:none" >
                </div>
                  </form>        
         </div>
         </div>
         </div>
 <script>
$(function(){
    $('#companyname').blur(function(){
         if($('#companyname').val().length==0){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('公司名字不能为空');
            return false;}else{
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
         if($('#companyname').val().length>20){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('公司名字最多为20个字符长度');
            return false;}else{
                $(this).parent('.col-md-9').siblings('.error').hide();
            }
    });
   $('#companyCorpID').blur(function(){
        if($('#companyCorpID').val().length==0){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('企业微信CorpID不能为空');
            return false; 
        }else{$(this).parent('.col-md-9').siblings('.error').hide();}
        if($('#companyCorpID').val().indexOf(' ')>-1){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('企业微信CorpID中间不能有空格');
            return false; 
        }else{$(this).parent('.col-md-9').siblings('.error').hide();}
   });
   $('#companySecret').blur(function(){
        if($('#companySecret').val().length==0){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('企业微信Secret不能为空');
            return false; 
        }else{$(this).parent('.col-md-9').siblings('.error').hide();}
        if($('#companySecret').val().indexOf(' ')>-1){
            $(this).parent('.col-md-9').siblings('.error').show();
            $(this).parent('.col-md-9').siblings('.error').html('企业微信Secret中间不能有空格');
            return false; 
        }else{$(this).parent('.col-md-9').siblings('.error').hide();}
   });
});
function submitform(){
	$("#submita").hide();
	$("#submitb").show();
	if($('#companyname').val().length>20||$('#companyname').val().length==0){
		$("#submitb").hide();
  		$("#submita").show();
        $('#companyname').focus();
        return false;
    }
    if($('#companyCorpID').val().length==0||$('#companyCorpID').val().indexOf(' ')>-1){
    	$("#submitb").hide();
  		$("#submita").show();
        $('#companyCorpID').focus();
        return false;
    }
    if($('#companySecret').val().length==0||$('#companySecret').val().indexOf(' ')>-1){
    	$("#submitb").hide();
  		$("#submita").show();
        $('#companySecret').focus();
        return false;
    }
     if(!$("#ckProtocal").prop("checked")){
    	$("#submitb").hide();
   		$("#submita").show();
      _alert("", "请认真阅读并同意《企微服务条款》！");
      return false;
      } 
	
	$.ajax({
  		url:"${baseURL}/portal/experienceapplication/expappAction!autoAbutment.action",
  		type:"POST",
  		data:$("#form").serialize(),
  		dataType:"json",
  		async:false,
  		success:function(result){
  			if(result.code=="0"){ 
  				var id=result.data.id;
  				window.location.href="${baseURL}/qiweipublicity/experience2/yunstep2.jsp?id="+id;
  			}else{
  				_alert("", result.desc);
  			}
  			$("#submitb").hide();
  		 	$("#submita").show();
  		},
  		error:function(){
  			_alert("", "系统繁忙");
  			$("#submitb").hide();
  		 	$("#submita").show();
  		}
	}); 
}
</script>
          <div class="clearfix"></div>
         
 <%@include file="./footer.jsp" %> 
          <%@include file="../../manager/msgBoxs.jsp" %> 
  </body>
</html>