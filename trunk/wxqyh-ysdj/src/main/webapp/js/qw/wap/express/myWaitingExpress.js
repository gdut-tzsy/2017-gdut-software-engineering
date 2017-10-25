//初始化基本数据
var pageIndex = 1;

var temp='<div class="settings-item">'
	+        '<div class="link_row">'
	+            '<div class="inner_link_row flexbox">'
	+                '<div class="a_select_wrap" style="display:none" id="selectDiv">'
	+                     '<a class="a_select" href="javascript:selectExpress(@id);"></a>'
	+       	 	 '</div>'
	+                '<div class="flexItem a_link_wrap">'
	+					'<a class="a_link" href="javascript:opentdetail(@id)"></a>'
	+       	 	 '</div>'
	+       	 '</div>'
	+        '</div>'
	+		 '<span class="time"></span>'      
    +		 '<div class="inner-settings-item flexbox" id=@id>'
    +            	'<div class="user_select_icon actived" style="display:none" id="selectItem"><i class="fa"></i></div>'
    +				'<div class="title description_title flexItem">' 
    +						'<p class="name">@title</p>' 
    +						'<p class="description description_ellipsis">@content</p>' 
    +				'</div>'       
    +		 '</div>'
    +	'</div> ';

$(document).ready(function(){
		var html = "<div class=\"searchTitleIcon faSortDown\"><div class=\"searchTitleSpan\"><span></span></div><ul class=\"searchTitlePop\" id=\"schValueId\"><li id=\"property_title_liid\"><a href=\"javascript:changeSearchProperty('','')\">收件人</a></li>"
						+"<li id=\"property_title_liid\"><a href=\"javascript:changeSearchProperty('cTime','')\">创建时间</a></li><li id=\"property_title_liid\" class=\"cInput\" style=\"display:none\"><a href=\"javascript:changeSearchProperty('cInput','')\">我录入的</a></li></ul></div>";
		document.title='未领取的快递';
		if($("#ctype").val()=="1"){
			html += "<input class=\"search-input pl30\" type=\"text\" placeholder=\"搜索创建时间\" name=\"keyWord\" id=\"keyWord\" />";
		}else{
			html += "<input class=\"search-input pl30\" type=\"text\" placeholder=\"搜索收件人\" name=\"keyWord\" id=\"keyWord\" />";
		} 
		$("#temp").html(html);
		if($("#ctype").val()=="1"){
			$("#ctype").val("1");
			opt.preset='date';//调用日历显示  日期时间
	    	$("#keyWord").mobiscroll(opt);//直接调用日历 插件
	    	$("#keyWord").attr("readonly","readonly");
	        $("#keyWord").on("focus", function(){
	        	//关闭输入法
	        	$("#title").css("ime-mode","disabled");
	        	$("#content").css("ime-mode","disabled");
	        });
		}
		$('.search-input').keydown(function(e){
			if(e.keyCode==13){
				enterSearch();
			}
		});
		if($("#csource").val()== "search"){
			//搜索关键字
			var keyWord = $("#ckeyWord").val();
			$("#keyWord").val(keyWord);
			document.getElementById("orginfo_search").action=baseURL+"/portal/expressAction!myWaitingExpress.action";
		}else{
			$("#keyWord").val("");
		}
		$('.searchTitleIcon').on('click',function(){
		    if($(this).hasClass('faSortDown')){
		        $(this).removeClass('faSortDown').addClass('faSortUp');
		    }else if($(this).hasClass('faSortUp')){
		        $(this).removeClass('faSortUp').addClass('faSortDown');
		    }
		});
		doSearch();
	});
	
	function enterSearch(){
		if("lastAuditTime"==$("#paTypeName").val()){
			var keyWord = $("#keyWord").val();
			if(null != keyWord && "" != keyWord){
				if(!/^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$/.test(keyWord)){
					_alert("提示","按时间搜索只能搜索时间格式如：2015-07-06","确认",function(){restoreSubmit();});
					return false;
				}
			}
			
		}
		var keyWord = $('.search-input').val();
	   if(keyWord != ''){
		   document.getElementById("orginfo_search").action = "myWaitingExpress.jsp?status="+$("#cstatus").val()+"&source=search&ctype="+$("#ctype").val();
	   }else{
		   document.getElementById("orginfo_search").action = "myWaitingExpress.jsp?status="+$("#cstatus").val();
	   }
	   document.getElementById("orginfo_search").submit();
	}

	/**
	 * 加载数据
	 */
	 //showLoading("正在加载中...");
	function doSearch() {
		var status=$("#cstatus").val();
		var type=$("#ctype").val();
		$('#orginfo_search').ajaxSubmit({
			data : {
				page: pageIndex,status:status,toptypes:type
			},
			dataType : 'json',
			success : function(result) {
				 lock_roll=false;
				if ("0" == result.code) {
					if(result.data.currPage>=result.data.maxPage){
						 $("#moreTip").html("已没有更多");
						hasMore=false;
					} else{
						hasMore=true;
					}
					var taskList = result.data.pageData;
					if(taskList&&taskList.length>0){
						for(var i=0;i<taskList.length;i++){
							var task = taskList[i];
							var item=temp;
		               		item = item.replace("@title",task.title);
		               		item = item.replace("@content",task.content);
		               		item = item.replaceAll("@id","'"+task.id+"'");
							$(".address_list").append(item);
						}
						$(".wrap_inner1").height($(document).height());
					}else{
						$("#showMoreDiv").hide();
						$(".address_list").append("<p style='text-align:center;margin-top: 10px;'>没有搜索到任何数据，换别的关键字试试</p>");
					}
				} 
			
			//只有录入权限的才显示我录入的
			if(result.data.ispower){
				$(".cInput").show();
			}else{
				$(".cInput").hide();
			}
			if($("#cstatus").val()=='0'&&$("#ctype").val()=='2'){//未领取快递 我录入的显示选择
				$("div[id='selectDiv']").css("display","block");
				$("div[id='selectItem']").css("display","block");
				if(express.length>0){
					$("#footDiv").css("display","block");
					$("#footheight").show();
				}else{
					$("#footDiv").css("display","none");
					$("#footheight").hide();
				}
			}else{
				$("div[id='selectDiv']").css("display","none");
				$("div[id='selectItem']").css("display","none");
				$("#footDiv").css("display","none");
				$("#footheight").hide();
			}
				hideLoading();
			},
			error : function() {
				hideLoading();
				_alert('提示',internetErrorMsg,"确认",function(){WeixinJS.back(); restoreSubmit();});
			}
		});
	}

	
	var hasMore;
	var lock_roll=false;
    $(function() {
         // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
         var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
         var nScrollTop = 0; //滚动到的当前位置
         var $frame = $("#main");
         $frame.on("scroll touchmove click", function() {
             var nDivHight = $frame.height();
             nScrollHight = $(this)[0].scrollHeight;
             nScrollTop = $(this)[0].scrollTop;
             if (nScrollTop + nDivHight >= nScrollHight) {
                 // 触发事件，这里可以用AJAX拉取下页的数数据
                 if(!lock_roll){
                	 lock_roll=true;
 					if(hasMore){
                         pageIndex++;
                         doSearch();
 					}
				}
             };
         });
     });
    
//打开详情
function opentdetail(obj){
	window.location.href=baseURL+"/jsp/wap/express/waitDetail.jsp?id="+obj;
}
//搜索框显示
function changeSearchProperty(tips,valType){
	var html1 = "<div class=\"searchTitleIcon faSortDown\"><div class=\"searchTitleSpan\"><span></span></div><ul class=\"searchTitlePop\" id=\"schValueId\"><li id=\"property_title_liid\"><a href=\"javascript:changeSearchProperty('','')\">收件人</a></li>"+
	"<li id=\"property_title_liid\"><a href=\"javascript:changeSearchProperty('cTime','')\">创建时间</a></li><li id=\"property_title_liid\" class=\"cInput\" style=\"display:none\"><a href=\"javascript:changeSearchProperty('cInput','')\">我录入的</a></li></ul></div>";
	if(tips=="cTime"){
		 html1 += "<input class=\"search-input pl30\" type=\"text\" placeholder=\"搜索创建时间\" name=\"keyWord\" id=\"keyWord\" />";
	}else if("cInput"==tips){//我录入的
		html1 += "<input class=\"search-input pl30\" type=\"text\" placeholder=\"搜索收件人\" name=\"keyWord\" id=\"keyWord\" />";
	}else{
		html1 += "<input class=\"search-input pl30\" type=\"text\" placeholder=\"搜索收件人\" name=\"keyWord\" id=\"keyWord\" />";
	} 
	$("#temp").html(html1);
	
	var $target=$("#schValueId");
	//先去除所有的样式
	for(var i=0;i<$target.children("li").length;i++){
		$target.children("li").eq(i).attr("style","");
	}
	express = [];//清空
	//设置样式
	if(tips!=""){
		$("#"+tips).attr("style","background:#e5e5e5;");
	}
	pageIndex=1;
	document.getElementById('comments').innerHTML="<div class=\"search-box-height\"></div>"; 
	$("#keyWord").val("");
	if("cTime"==tips){
		$("#ctype").val("1");
		$("#keyWord").attr("placeholder","按创建时间搜索");
		opt.preset='date';//调用日历显示  日期时间
    	$("#keyWord").mobiscroll(opt);//直接调用日历 插件
    	$("#keyWord").attr("readonly","readonly");
        $("#keyWord").on("focus", function(){
        	//关闭输入法
        	$("#title").css("ime-mode","disabled");
        	$("#content").css("ime-mode","disabled");
        });
	}else if("cInput"==tips){//我录入的
		$("#ctype").val("2");
	}else{ 
		$("#ctype").val("0");
	}
	$('.searchTitleIcon').on('click',function(){
	    if($(this).hasClass('faSortDown')){
	        $(this).removeClass('faSortDown').addClass('faSortUp');
	    }else if($(this).hasClass('faSortUp')){
	        $(this).removeClass('faSortUp').addClass('faSortDown');
	    }
	})
	doSearch();
}


var express = [];
function selectExpress(expressId){

	if($("#"+expressId).find(".user_select_icon").hasClass("active")){
		$("#"+expressId).find(".user_select_icon").removeClass("active");
		$("#"+expressId).find(".user_select_icon").addClass("actived"); 
		express.remove(expressId);
	}else{
		$("#"+expressId).find(".user_select_icon").removeClass("actived");
		$("#"+expressId).find(".user_select_icon").addClass("active");
		express.push(expressId);
	}
	if(express.length==0){
		$("#footDiv").css("display","none");
		$("#footheight").hide();
	} else {
		$("#footDiv").css("display","block");
		$("#footheight").show();
	}
}

function doSure(){
	var expressIds = "";
	for ( var i = 0; i < express.length; i++) {
		expressIds += express[i] + "#";
	}
	if (expressIds != "") {
		expressIds = expressIds.substring(0, expressIds.length -1);
	}else{
		return;
	}
	showLoading("正在发送...");
	$.ajax({
		url:baseURL+"/portal/expressAction!againBatchSendMsg.action",
		type:"post",
		data:{"ids":expressIds},
		dataType:"json",
		success:function(result){
			 	hideLoading();
				 if ("0" != result.code) {
					 hideLoading();
					 _alert("提示",result.desc,"确认",function(){restoreSubmit();});
	                 return;
	             }
				 _alert("提示","催单成功","确认",function(){restoreSubmit();});
			},
			error: function() {  hideLoading();}
		});
	
}



