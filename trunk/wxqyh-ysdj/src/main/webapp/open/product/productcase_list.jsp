<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta charset="utf-8"/>
        <title>案例列表</title>
        <meta name="description" content=""/>
        <meta name="HandheldFriendly" content="True"/>
        <meta name="MobileOptimized" content="320"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/wt.css" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
        <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>

		<script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
        <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	    <style>
	    	/*放在本页*/
			@media screen and (min-width: 800px){
			    .question_detail {
				    background: #ffffff;
				    width: 600px;
				    margin: 50px auto 0 auto;
				    border: 1px solid #dfdfdf;
				    padding: 50px 100px;
				}
			}
			.inner-f-item:last-child{ border-bottom: 1px solid #dfdfdf;}
			.inner_img_preview img{
			    position: fixed;
			    left: 50%;
			    top: 50%;
			    transform: translate(-50%,-50%);
				-webkit-transform: translate(-50%,-50%);
			}
	    </style>
    </head>
    
        <body>
        <%@include file="/open/include/showMsg.jsp" %>
        <div id="wrap_main" class="wrap">
        	<div class="question_detail">
	            <div class="search-box fixed">
	                <div class="inner-search-box">
		            <div class="search-box-o flexbox">
	                	<div class="flexItem">
		                    <div class="search-input-box flexbox">
		                        <div class="flexItem">
		                            <input class="search-input" type="text" placeholder="搜索" name="" id="keyword" />
		                        </div>
		                    </div>
		                </div>
		                <div class="search_more"> 
		                	<a href="javascript:enterSearch()" class="search_letter_btn"><i class="fa fa-search"></i></a>
	                	</div>
	                </div>
	                </div>
	            </div>
	            <div id="main" class="wrap_inner" >
	                <div class="address_list" id="comments">
	                	<div class="search-box-height"></div>
	                </div>
	                <div class="all_pull">
	                    <p class="lastComment" id="hasMore">向上滑加载更多</p>
	                </div>
	            </div>
            </div>
        </div>
    </body>
    
</html>

<script type="text/javascript">
	var hasMore=false;
	var lock_roll=true;
	
	var currentPage=1;
	var pageSize=10;

    $(document).ready(function () {
    	showLoading();
    	doSearch();
        //如果是ios客户端，需要刷新一下底部按钮的高度，防止点击输入框后低部输入框上漂
        //android不可以刷新，刷新会出现三星手机的该页面无法滚动的问题
        if (/ipad|iphone|mac/i.test(navigator.userAgent)){
    		setTimeout(function(){
    	    	$(".wrapHeight").height($(window).height());
    		},500);
        }
        /* $("input").on('focus', function(){
        	$(".wrapHeight").height($(window).height());
    	}); */
    });
    
    function doSearch(){
		var keyword =$.trim($("#keyword").val());
		var productId="${param.productId}";
    	$.ajax({
            type:"POST",
            url: "${baseURL}/open/product/productAction!getProductCasePager.action",
            data:{
            	"currentPage": currentPage,
            	"pageSize":pageSize,
            	"productId":productId,
            	"keyword":keyword
            },
            cache:false,
            dataType: "json",
            success: function(data){
            	lock_roll=false;
                if ("0" != data.code) {
                	hideLoading();
                    showMsg("", data.desc, 1);
                    return;
                }
            
                var info = data.data;
                if(info.pageData.length>0){
                	$("#getmore_comment").show();
                }else{
                	$("#getmore_comment").hide();
                }
                	
                
                var comments = info.pageData;
                var commentsDiv = $("#comments");
                appendCommnets(commentsDiv, comments);
                
                if(info.currPage<info.maxPage){
                	hasMore = true;
                }
                if (!hasMore) {
                	$("#hasMore").html("<p class='lastComment'>没有更多案例啦</p>");
				}
                hideLoading();
            }
        });  
    }

    function listMore() {
    	currentPage=currentPage+1;
    	doSearch();
    }
    
    function appendCommnets(commentsDiv, comments) {
       for (var i = 0; i < comments.length; i++) {
            var item = ""
					    + "<div class=\"settings-item\">"
					    +        "<a class=\"a_link\" href=\"productcase_detail.jsp?id="+ comments[i].caseId + "\"></a>"
					    +        "<div class=\"inner-settings-item flexbox\">"
					    +            "<div class=\"avator\">"
					    +            "<img src=\"${compressURL}" + comments[i].caseImg + "\" alt=\"\" onerror=\"javascript:replaceUserHeadImage(this);\"/>"
					    +            "</div>"
					    +            "<div class=\"title description_title flexItem\">"
					    +            "<p class=\"name\">"+ comments[i].title +"</p>"
					    +            "<p class=\"description description_ellipsis\">"+comments[i].summary+"</p>"
					    +            "<input type=\"hidden\" value="+comments[i].caseId+" />"
					    +            "</div>"
					    +        "</div>"
					    + "</div>";
					    
            commentsDiv.append(item);
            
        }
    }
    
	function autoSearch(){
		var event = arguments.callee.caller.arguments[0] || window.event;
		if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13，想看其他代码请猛戳这里。
			document.getElementById('comments').innerHTML="<div class=\"search-box-height\"></div>";  
			currentPage=1;
			doSearch();
		}
	}
	function enterSearch(){
		document.getElementById('comments').innerHTML="<div class=\"search-box-height\"></div>"; 
		currentPage=1;
		doSearch();
	}
	
    $(function() {
         // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
         var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
         var nScrollTop = 0; //滚动到的当前位置
         var $frame = $("#main");
         $frame.on("scroll touchmove", function() {
             var nDivHight = $frame.height();
             nScrollHight = $(this)[0].scrollHeight;
             nScrollTop = $(this)[0].scrollTop;
             if (nScrollTop + nDivHight >= nScrollHight) {
                 // 触发事件，这里可以用AJAX拉取下页的数数据
                 if(!lock_roll){
                	 lock_roll=true;
 					if(hasMore){
 						currentPage=currentPage+1;
                        doSearch();
 					}
				}
             };
         });
     });
</script>