(function($){
	$.fn.searchinput = function(options){
		var defaults = {
			text : ""
		};
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var text = options.text;
			var obj = $(this);
			obj.attr("value",text);
			obj.blur(function(){
				this.value = text;
			});
			$(this).focus(function(){
				this.value = "";
			});
		});
	}
})(jQuery);

(function($){
	$.fn.showinput = function(options){
		var defaults = {
			inputwidth  : "",
			top : "",
			left : "",
			datalist : ""
		};
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var inputwidth  = options.inputwidth,
			    inputheight = options.inputheight,
				top         = options.top,
				left        = options.left;
				obj         = $(this),
				datalist    = options.datalist,
				lists = "";
			for(var list in datalist){
				if(list == datalist.length - 1){
					lists += "<a href='#' style='padding-left:10px; display:block; margin-left:5px; margin-right:5px;'>" + datalist[list] + "</a>";
					break;
				}
				lists += "<a href='#' style='border-bottom:1px dashed #ccc; padding-left:10px; display:block; margin-left:5px; margin-right:5px;'>" + datalist[list] + "</a>";
			}
			var inputframe = $("#showinput");
			var showinput = inputframe.attr("style","border:1px solid #999; line-height:24px; width:" + inputwidth + "px; position:absolute; left:" + left + "px; top:" + top + "px;").html(lists);
			obj.keyup(function(){
				inputframe.fadeIn("slow");
			});
			
			$("a",inputframe).click(function(){
				obj.val($(this).text());
			});
			
			obj.blur(function(){
				obj.next().fadeOut("slow");
			});
		});
	}
})(jQuery);