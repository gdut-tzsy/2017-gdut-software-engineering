<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="">
<title>问答</title>
<link href="css/bootstrap.css?ver=<%=jsVer%>" rel="stylesheet">
<link href="css/do1.css?ver=<%=jsVer%>" rel="stylesheet">
<script src="js/jquery-1.3.2.js"></script>
<script src="js/common.js?ver=<%=jsVer%>"></script>
<script src=" http://hm.baidu.com/h.js%3F519281b7063399b3740aeca92f1f624f" type="text/javascript"></script>
</head>
<body>
<div class="navbar navbar-default">
  <div class="container">
    <div class="content">
      <div class="logo navbar-left"><a href="http://wbg.do1.com.cn" target="_blank"><img src="images/logo.jpg"></a></div>
      <ul class="nav nav-pills navbar-right" role="tablist">
        <li role="presentation"><a href="http://wbg.do1.com.cn/index.html">首页</a></li>
        <li role="presentation"><a href="http://wbg.do1.com.cn/scenario1.html">场景</a></li>
        <li role="presentation"><a href="http://wbg.do1.com.cn/open.html">开通</a></li>
        <li role="presentation"  class="active"><a href="http://wbg.do1.com.cn/About.html">关于</a></li>
       <li role="presentation" class="login"><a href="${qwyURL}" target="_blank">管理后台</a></li>
      </ul>
    </div>
  </div>
</div>
<div class="banner" id="banner"><img src="images/banner7.jpg"></div>
<div class="nav center">
  <div class="container">
    <div class="row nav summary">
     <a class="col-md-3" href="http://wbg.do1.com.cn/About.html">关于</a>
     <a class="col-md-3" href="http://wbg.do1.com.cn/security.html">安全</a>
     <a class="col-md-3" href="http://wbg.do1.com.cn/cooperation.html">合作</a>
     <a class="col-md-3 active" href="${qwyURL}/qiweipublicity/experience2/Question.jsp">问答</a>
    </div>
  </div>
</div>
<div class="container">
  <div class="content question mt25">
  	<div id="content"></div>
      <script>
      $(function(){
        $('.details').click(function(){
          $(this).parents('span').siblings('.answer').css({
            'height': 'auto',
            'overflow': 'auto'
          })
          $(this).css('display','none');
        })
      // $('li.center').click(function(event){
    	//   event.stopPropagation();
    	//   $('#dl-hover').show();
       //})
       $('li.center').live('click',function(event){
    	  
    	   if($('#dl-hover').is(':hidden')){
    		   event.stopPropagation();
    		   $('#dl-hover').show();
    	   }else{
    		   event.stopPropagation();
    		   $('#dl-hover').hide()}
       })
       $('body').click(function(){
    	  if($('#dl-hover').is(':hidden')==false){
    		   $('#dl-hover').hide();
    	   }
       })
      });
      </script>
    <div class="pages">
    <div class="pages1">
        <dl class="dl-hover" id="dl-hover">          
         </dl>
      <ul class="ul" id="indexul">
      </ul>

    </div></div>

    
    
  </div>
</div>
<div class="clearfix"></div>

<div class="footer">
	<div class="container">
		<div class="content">
			<div class="row mt25">
				<div class="col-md-9">
					<div class="row">
						<div class="col-md-3 rsolid">
							<dl>
								<dt>关于</dt>
								<dd class='mt10'>
									<a href="http://wbg.do1.com.cn/About.html">公司简介</a>
								</dd>
								<dd>
									<a href="http://wbg.do1.com.cn/About.html#001">产品团队</a>
								</dd>
								<dd>
									<a href="http://wbg.do1.com.cn/About.html#002">联系我们</a>
								</dd>
							</dl>
						</div>
						<div class="col-md-3 rsolid">
							<dt>安全</dt>
							<dd class='mt10'>
								<a href="http://wbg.do1.com.cn/security.html">数据安全</a></a>
							</dd>
							
						</div>
						<div class="col-md-3 rsolid">
							<dt>合作</dt>
							<dd class='mt10'>
								<a href="http://wbg.do1.com.cn/cooperation.html">战略合作</a>
							</dd>
						</div>
						<div class="col-md-3">
							<dt>链接</dt>
							<dd class='mt10'>
								<a href="http://www.do1.com.cn" target="_blank">道一官网</a>
							</dd>
							<dd>
								<a href="http://dj.do1.com.cn/" target="_blank">智慧党建</a>
							</dd>
							<dd>
								<a href="http://qiwei.do1.com.cn/" target="_blank">企微APP</a>
							</dd>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="row text-right">
						<div class="col-md-7">
							<a href="###"><img src="./images/weix.jpg"></a>
						</div>
						<div class="col-md-5">
							<a>+关注微信</a>
						</div>
					</div>
				</div>
			</div>
			<div class="row mt25">
				<div class="col-md-9">
					<p>广东道一信息技术股份有限公司 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;产品支持Q群：53863114 </p>
					<p>咨询热线：400-111-2626  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公司总部：广州市海珠区广州大道南228号经典居29楼整层</p>
					<p class="color">Copyright©2014 DO1.com.cn All Rights Reserved
						粤B2-20062018号</p>
				</div>
				<div class="col-md-3">
					<div class="row text-right">
						<div class="col-md-7">
							<a href="http://weibo.com/do1ltd"><img
								src="./images/weibo.jpg"></a>
						</div>
						<div class="col-md-5">
							<a href="http://weibo.com/do1ltd" target="_blank">+关注微博</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

 <script>
 	var temphtml=''
 	+'<li>'
    +'<h3>@question</h3>'
    +'<div class="answer">@anwei</div>';
 	
    var pageIndex = 1;
        
     $(document).ready(function (){
     	gotoindex(1);
     	
     });
     function gotoindex(pageIndex){
     	$.ajax({
 			url:"${baseURL}/portal/experienceapplication/expappAction!getQuestion.action",
 			type:"POST",
 			dataType:'json',
 			data : {
 				'page' : pageIndex
 			},
 			success:function (result) {
 				if(result.code=="0"){
 					$("#content").html("");
 					$("#dl-hover").html("");
 					$("#indexul").html("");
 					var List = result.data.pageData;
 					for(var i=0;i<List.length;i++){
 						var question=List[i];
 						var item=temphtml;
     					item = item.replace("@question",question.question );
 						item = item.replace("@anwei",question.answer);
 						$("#content").append(item);
 					}
 					var maxPage=result.data.maxPage;
 					for(var x=1;x<=maxPage;x++){
 						if(x==pageIndex){
   					$("#dl-hover").append("<dd><a href=\"javascript:gotoindex("+x+")\" class=\"dd-hover\">第"+x+"页</a></dd>");
 						}else{
  						$("#dl-hover").append("<dd><a href=\"javascript:gotoindex("+x+")\">第"+x+"页</a></dd>");
 						}
 					}
 					$("#indexul").append("<li><a href=\"javascript:gotoindex(1)\"><img src=\"images/left1.png\"></a></li>");
 					if(pageIndex>1){
  					$("#indexul").append(" <li><a href=\"javascript:gotoindex("+(pageIndex-1)+")\"><img src=\"images/left2.png\"></a></li>");
 					}else{
 						$("#indexul").append(" <li><a href=\"javascript:gotoindex(1)\"><img src=\"images/left2.png\"></a></li>");
 					}
 					$("#indexul").append("<li class=\"center\">第"+pageIndex+"页<img src=\"images/pageb.png\"></li>");
			if(pageIndex<maxPage-1){
  					$("#indexul").append("<li><a href=\"javascript:gotoindex("+(pageIndex+1)+")\"><img src=\"images/right2.png\"></a></li>");
 					}else{
 						$("#indexul").append("<li><a href=\"javascript:gotoindex("+maxPage+")\"><img src=\"images/right2.png\"></a></li>");
 					}
 					$("#indexul").append("<li><a href=\"javascript:gotoindex("+maxPage+")\"><img src=\"images/right1.png\"></a></li>");
 				}
 			}
 		});
     }
        
    </script>
        
</body>
</html>