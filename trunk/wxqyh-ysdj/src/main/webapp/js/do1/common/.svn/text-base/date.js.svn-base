$(function($){
	$.fn.date = function(options){
		var defaults = {
			datewidth  : "",
			dateheight : "",
			id         : ""
		};
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var datewidth  = options.datewidth ? "width:" + options.datewidth + "px;" : "";
			var dateheight = options.dateheight ? "padding:" + options.dateheight + "px;" : "";
			var id         = options.id;
			var date = "<input name='selDate' id='" + id + "' onclick='WdatePicker();' readonly='readonly' type='text' style='" + datewidth + dateheight + "' />";
			//return date;
			$("#date").html(date);
		});
	};
})(jQuery);