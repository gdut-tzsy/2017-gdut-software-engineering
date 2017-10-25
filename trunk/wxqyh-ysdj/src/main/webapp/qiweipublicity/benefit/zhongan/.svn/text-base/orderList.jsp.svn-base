<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <script src="${baseURL}/js/do1/common/jquery-1.6.3.min.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
</head>
<body>
 <form action="${baseURL}/zhongan/zhonganAction!getOrderPager.action" method="post" id="57a3d3ae613b4778a90b4fe725e883ba" onsubmit="return false;" templateId="default" dqdpCheckPoint="query_form">
	<div class="topMainBar clearfix" style="margin-top:0px;">
		<div class="fr frMainBar">
                <div class="searchBox fl">
                    <input placeholder="搜索订单号" class="inputSearchBox" type="text" name="searchValue.orderId" id="title" onkeydown="keyDown13();">
                    <input class="submitSearchBtn" name="input" type="button" onclick="func_57a3d3ae613b4778a90b4fe725e883ba(1);  _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2');loadPageList();" value="">
                </div>
              <!--   <a class="senior_search_btn">高级搜索</a> -->
        </div>
	</div>
	<input type="hidden" name="searchValue.payStatuses" id="payStatuses"/>
</form>

	<div class="divTable" datasrc="local:var_57a3d3ae613b4778a90b4fe725e883ba" id="cc94a9ec68ff4d93a7fd5934c581b647">
        <div class="divItem divTh">
                <div class="bDiv1">保险产品</div>
                <div class="bDiv2">保障期限</div>
                <div class="bDiv3">金额（元) </div>
                <div class="bDiv4">
                	<span class="arrow">状态</span>
               		<div class="zf">
               			<span>全部</span>
               			<span>已支付</span>
               			<span>未支付</span>
               			<span>已取消</span>
               		</div>
                </div>
                <div class="bDiv5">操作</div>
        </div>
           
       <div id="noDate" class="noData" style="display:none;">暂无订单，去 <a href="">团险首页</a> 看看吧~</div>
       
       <div name="pageData" templateId="default" dqdpCheckPoint="list_value" style="display:none;">
       		<div class="divItem">
	            <div class="t">
	                <div class="tDiv1" name="createTime"></div>
	                <div class="tDiv2">订单号：<span name="orderId"></span></div>
	                <div class="tDiv3">投保单号：<span name="policyId"></span></div>
	                <div class="tDiv4" showon="'@{payStatuses}'=='S'">支付时间：<span name="payTime"></span></div>
	                <div class="tDiv4" showon="'@{payStatuses}'!='S'"></div>
	            </div>
	            <div class="b">
	                <div class="bDiv1" name="taocanName"></div>
	                <div class="bDiv2"><span name="effectiveDate"></span> 至  <span name="ineffectiveDate"></span></div>
	                <div class="bDiv3" name="amount"></div>
	                <div class="bDiv4" name="payDesc"></div>
	                <div class="bDiv5">
	                	<a showon="'@{payStatuses}'!='S' && '@{payStatuses}'!='-1'" class="btn" href="javascript:doPay('@{orderId}','@{taocanName}','@{amount}','1');">立即付款</a>
	                	<div class="mt5">
	                		<a class="" href="javascript:doDetail('@{orderId}');">投保单详情</a>
	                		<a class="qx" showon="'@{payStatuses}'!='S' && '@{payStatuses}'!='-1'" href="javascript:doCancel('@{orderId}')">取消</a>
	                	</div>
	                </div>
	            </div>
        	</div>
       </div>
    </div>
    <!-- 分页 -->
    <div class="bottomPage"  datasrc="local:var_57a3d3ae613b4778a90b4fe725e883ba" id="af612ff2d20641c6b2a6d920f1bd8fc2" templateId="default">   
        <div class="pager-nav">
            <ul id="pageList">
            </ul>
        </div>
    </div>
 <script>
 $(function(){
	 bingding();
 });
 
 //绑定按钮事件
 function bingding(){
	 $('.arrow').click(function(){
	     	var $div=$(this).next('.zf');
	     	//$div.toggle(); 
	     	$div.show();
	     	$div.find('span').click(function(){
	     		var tmpHtml=$(this).html();
	     		if(tmpHtml=="全部"){
	     			$("#payStatuses").val("");
	     		}else if(tmpHtml=="已支付"){
	     			$("#payStatuses").val("S");
	     		}else if(tmpHtml=="未支付"){
	     			$("#payStatuses").val("0");
	     		}else if(tmpHtml=="已取消"){
	     			$("#payStatuses").val("-1");
	     		}
	     		$div.hide();
	     		//刷新选择的参数
	     	});
	     }); 
 }
 </script>

<%@include file="../../msgBoxs.jsp" %>
</body>
</html>

	<script type="text/javascript">
       var var_57a3d3ae613b4778a90b4fe725e883ba;

       var refreshEvent = {ok: function () {
           func_57a3d3ae613b4778a90b4fe725e883ba(var_57a3d3ae613b4778a90b4fe725e883ba.currPage);
             _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2');loadPageList();
       }
       }; //,fail:,error:

       function func_57a3d3ae613b4778a90b4fe725e883ba(pageIndex) {
        $('#57a3d3ae613b4778a90b4fe725e883ba').ajaxSubmit({
            dataType: 'json',
            data: {page: pageIndex, 'dqdp_csrf_token': dqdp_csrf_token},
            async: false,
            success: function (result) {
                if ("0" == result.code) {
                    var_57a3d3ae613b4778a90b4fe725e883ba = result.data;
                } else {
                    $("#tip").error({title: "信息提示层", content: result.desc, button: [
                        {text: "确定", events: "test"},
                        {text: "取消", events: "test"}
                    ]});
                }
            },
            error: function () {
                _alert("信息提示层", "网络异常");
            }
        });
       }
       func_57a3d3ae613b4778a90b4fe725e883ba(1);
       _redraw('cc94a9ec68ff4d93a7fd5934c581b647'); 
       _redraw('af612ff2d20641c6b2a6d920f1bd8fc2');
   </script>
    
    
    <script type="text/javascript">
        function keyDown13(){
        	var event = arguments.callee.caller.arguments[0] || window.event;
        	if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13，想看其他代码请猛戳这里。
        		func_57a3d3ae613b4778a90b4fe725e883ba(1); 
        		_redraw('cc94a9ec68ff4d93a7fd5934c581b647'); 
        		_redraw('af612ff2d20641c6b2a6d920f1bd8fc2');
        		loadPageList();
        	}
        }

        function loadPageList(){
            $('#pageList').html('');
            var maxPage = var_57a3d3ae613b4778a90b4fe725e883ba.maxPage;
            var currPage = var_57a3d3ae613b4778a90b4fe725e883ba.currPage;
            var totalRows = var_57a3d3ae613b4778a90b4fe725e883ba.totalRows;
            //没有记录
            if(totalRows<=0){
            	$("#noDate").show();
            }else{
            	$("#noDate").hide();
            }
            if(maxPage <= 1 ){
            	$('#pageList').append("<li><a>总计"+totalRows+"条</a></li>");
            }else{
            	$('#pageList').append("<li><a>总计"+totalRows+"条</a></li>");
                //有记录
                if(currPage == 1){
                    $('#pageList').append("<li class=\"disabled pager-prev\"><a href=\"#\">上一页</a></li>");
                }else{
                    $('#pageList').append("<li class=\"pager-prev\"><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+currPage+"-1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList();\">上一页</a></li>");
                }

                if(maxPage < 11){
                    for (var i=1; i<= maxPage; i++) {
                        if(currPage == i){
                            $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                        }else{
                            $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                        }
                    }
                }else{
                    if(currPage <= 5){
                        for (var i=1; i<= 8; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                        $('#pageList').append("<li><span>...</span></li>");
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+maxPage+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+maxPage+"</a></li>");
                    }else if(maxPage - currPage <= 4 ){
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba(1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">1</a></li>");
                        $('#pageList').append("<li><span>...</span></li>");
                        for (var i =  currPage - 3; i<= maxPage; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                    }else{
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba(1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">1</a></li>");
                        $('#pageList').append("<li><span>...</span></li>");
                        for (var i = currPage -3; i<= currPage+3; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                        $('#pageList').append("<li><span>...</span></li>");
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+maxPage+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+maxPage+"</a></li>");
                    }

                }

                if(currPage == maxPage){
                    $('#pageList').append("<li class=\"disabled pager-next\"><a href=\"#\">下一页</a></li>");
                }else{
                    $('#pageList').append("<li class=\"pager-next\"><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+currPage+"+1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">下一页</a></li>");
                }
            } 
            $('.lasttd').on({
				mouseover:function(){
					if(navigator.userAgent.toUpperCase().indexOf("MSIE")>-1){				
						$(this).find('.lasttdpop').css({'display':'block'});$(this).children('.contact_action').addClass('after');					
					}else if(navigator.userAgent.toUpperCase().indexOf("SAFARI")>-1){
						$(this).find('.lasttdpop').css({'display':'-webkit-flex'});$(this).children('.contact_action').addClass('after');
					}
					else{
						$(this).find('.lasttdpop').css({'display':'flex'});$(this).children('.contact_action').addClass('after');}},
				mouseout:function(){$(this).find('.lasttdpop').hide();$(this).children('.contact_action').attr('class','contact_action');}
			});  
            _resetFrameHeight();
            
            //重新绑定按钮事件
            bingding();
        }

        $(document).ready(function () {
            loadPageList();
        });
        
        //支付
        function doPay(orderId,taocanName,amt,repay){
        	var backURL="";
        	var payURL="eportal/order/pay.jsp"
        		+"?orderId="+orderId
        		+"&taocanName="+encodeURIComponent(taocanName)
        		+"&amt="+amt
        		+"&backUrl="+encodeURIComponent(backURL)
        		+"&repay="+repay;
        	window.open(payURL);
        }
        //跳转到详细
        function doDetail(orderId){
        	window.open(baseURL+"/manager/benefit/zhongan/eportal/order/order-detail.jsp?orderId="+orderId);
        }
        //取消
        function doCancel(orderId){
        	$.ajax({
        		url : "${baseURL}/zhongan/zhonganAction!cancelOrder.action",
        		data:{
        			'orderId':orderId
        		},
        		type : "get",
        		async : false,
        		dataType : "json",
        		success : function(result) {
        			if (result.code == "0") {
        				window.location.href=window.location.href;
        			}else{
        				_alert("提示消息",result.desc);
        			}
        		},
        		error : function() {
        			_alert("错误提示", "系统繁忙！");
        		}
        	});
        }
    </script>
