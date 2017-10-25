(function($){
	$.fn.page = function(options){
		var defaults = {
			total  : "",   //数据总数
			pages  : "",   //一页的数据条数
			number : false,
			first  : false,
			last   : false,
			prev   : false,
			next   : false 
		};
		
		var options = $.extend(defaults,options);
		
		$(this).each(function(){
			var total   = options.total,
				pages   = options.pages,
				content = ""
				num     = "",
				page    = "",
				obj     = $(this);
			content = options.first ? content + "<a href='#' id='firstpage'>首页</a>|" : "";
			content = options.prev ? content + "<a href='#' id='nextpage'>上一页</a>|" : "";
			content = options.next ? content + "<a href='#' id='lastpage'>下一页</a>|" : "";
			content = options.last ? content + "<a href='#' id='prevpage'>尾页</a>" : "";
		
			num  = Math.ceil(total / pages);
			page = "<div class='pageDown'>" + content + "<span >到<input class='form24px' type='text' value='' />页<input type='button' value='GO' class='btnGo' />，共" + num + "页</span></div>" +
					"<div class='both'></div>";
			
			$(obj).append(page);
		});
		$(".btnGo").click(function(){
			alert($(".form24px").val());
		});
	};
})(jQuery);