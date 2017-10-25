(function($){
	$.fn.showdialog = function(options){
		var defaults = {
			title   : "",
			content : "",
			mothod  : "",
			top     : "",
			left    : "",
			popwidth  : "",//弹出框宽度
			popheight : ""//弹出框高度
		};
		var options = $.extend(defaults,options);
		
		$(this).each(function(){
			var obj          = $(this),
				title        = options.title,
				content      = options.content,
				mothod       = options.mothod,
				contents     = "",
				lines        = "";
			title   = "<table class='tipsBox'  border='0' cellspacing='0' cellpadding='0'><tr><th><h2>" + title + 
					  "</h2><a href='javascript:window.close()' class='close' title='关闭'>关闭</a></th></tr>";
			for(var list in content){
				var secondtitle = content[list].secondtitle ? "<div class='timeList'><h3>" + content[list].secondtitle + "</h3><ul>" : "";
				var lists = content[list].list;
				for(var list in lists){
					contents = contents + "<li><a href='javascript:void(0)' onClick=\""+mothod+"('" + lists[list].url + "');return false;\">" + lists[list].listtitle + "</a></li>";
					lines = lines + contents;
					contents = "";
				}
			}		  
			lines = secondtitle + lines + "</ul></div>";	  
			contents = title + "<tr><td>" + lines + "</td></tr></table>";
			$(obj).addClass("w600").append(contents).show();
			
			var	popwidth     = options.popwidth ? options.popwidth : $(obj).width(),
				popheight    = options.popheight ? options.popheight : $(obj).height(),
				client       = document.documentElement,
				clientwidth  = client.clientWidth,
				clientheight = client.clientHeight,
				poptop       = Math.abs(((clientheight - popheight) / 2 > 0) ? ((clientheight - popheight) / 2) : 0),
				popleft      = Math.abs(((clientwidth - popwidth) / 2  > 0) ? ((clientwidth - popwidth) / 2)  : 0);
			$(obj).css({top:poptop + "px",left:popleft + "px"});
			function doSize(){
				clientwidth  = client.clientWidth;
				clientheight = client.clientHeight + client.scrollTop + document.body.scrollTop;
				newtop       = Math.abs(((clientheight - popheight) / 2 > 0) ? ((clientheight - popheight) / 2) : 0);
				newleft      = Math.abs(((clientwidth - popwidth) / 2  > 0) ? ((clientwidth - popwidth) / 2) : 0);
				$(obj).css({top:newtop + "px",left:newleft + "px"});
			}
			window.onresize = function(){
				doSize();
			};
		});
	}
})(jQuery);