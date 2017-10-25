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
    <title>开通</title>
    <link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src="js/jquery-1.11.1.js"></script>
    <script src="js/common.js?ver=<%=jsVer%>"></script>
    <script src=" http://hm.baidu.com/h.js%3F519281b7063399b3740aeca92f1f624f" type="text/javascript"></script>
  </head>
    <body>
 <%@include file="./header.jsp" %> 
        <div class="container">
         <div class="content text-left tiyan yun">
         <div class="yun_top">
           
<div class="yun_top_l"><h2>企业微信接入企微云服务</h2>

<div>操作步骤比较多，请认真按照《接入企微云服务手册》进行操作。</div></div>

<a href="http://wbg.do1.com.cn/handbook.html"  class="yun_top_r" target="_blank">查看接入企微云服务手册</a>


         </div>
                <div class="stepdiv mt25">
           <div class="step first active"><i>1</i>提交企业微信信息</div><div class="steppic"><img src="images/jt-active.png"></div>
           <div class="step active"><i>2</i>注册企微平台账号</div><div class="steppic"><img src="images/jt-active.png"></div>
           <div class="step active"><i>3</i>获取应用配置信息</div><div class="steppic"><img src="images/jt-active.jpg"></div>
           <div class="step"><i>4</i>企业微信应用配置</div>
           <div class="step last"><i>5</i>企微应用中心</div>
          </div>
          
          <div class="form">
     
<div class="succeed">
<h2>成功注册账号</h2>

<p style="color: black; margin-bottom: 20px;">我们已经将账号信息和应用配置信息发送到你对应的邮箱，稍后查看邮件。</p>

<p style="margin-bottom: 20px;">请按照《接入企微云服务手册》，按照如下的配置信息，在<a href="https://qy.weixin.qq.com/" style="color:#FF9B0C;text-decoration: underline">企业微信后台</a>修改配置信息，接入企微应用。（相应的配置信息也可以查看）</p>
<table>
<tr><td width="28%">URL：</td><td style="color:#000;" >${param.u}</td></tr>
<tr><td>Token：</td><td style="color:#000;">${param.t}</td></tr>
<tr><td>EncodingAESKey：</td><td style="color:#000;">${param.e}</td></tr>
<tr><td>可信域名：</td><td style="color:#000;">qy.do1.com.cn</td></tr></table>
<a class="submit" id="submit" href="https://qy.weixin.qq.com/" target="_blank">应用配置(微信后台),下一步</a>

</div>
 <div id="showbg">
 
         </div>
          <div class="showbg-con" id="showbg-con">
   <h3>企业微信应用配置是否完成？</h3>

<p>如果你已经按照《接入企微云服务手册》在企业微信后台完成应用配置，可以点击以下【继续，企微应用绑定】登录企微后台完成应用绑定。</p>
<div id="btn"><a href="${qwyURL}">继续，企微应用绑定</a></div> </div>
 </div> 
         </div>
         </div>

 </div> 
          <div class="clearfix"></div>
          <script type="text/javascript">
         var submit=document.getElementById('submit');
         submit.onclick=function(){
          var showbgcon=document.getElementById('showbg-con');
          var showbg=document.getElementById('showbg');          
          setTimeout(function(){
            showbg.style.display='block';
            showbgcon.style.display='block';
            document.body.style.overflow='hidden';            
          }, 500)
          }

</script>
 <%@include file="./footer.jsp" %>  
  </body>
</html>