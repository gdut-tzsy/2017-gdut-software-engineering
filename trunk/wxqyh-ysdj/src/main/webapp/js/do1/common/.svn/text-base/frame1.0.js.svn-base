(function($){
	$.fn.frame = function(options){
		var defaults = {
			framehead  : "",
			frameleft  : "",
			frameright : ""
		};
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var framehead  = options.framehead ? options.framehead : "";
			var frameleft  = options.frameleft ? options.frameleft : "";
			var frameright = options.frameright ? options.frameright : "";
			
			var h = $(window).height() - 90;
			$(window).resize(function(){
				$("#cen2 .shrink").height($(window).height() - 90);
			});
			
			var frame = framehead + "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr>" +
				"<td valign='top' class='left' id='left'>" + frameleft + "</td>" +
				"<td width='5' valign='top' class='showLeft' id='cen2'><div class='shrink' style='height:" + h + "px;'><a href='javascript:showLeft();' title='点击可收缩左侧'  >点击可收缩左侧</a></div></td>" +
				"<td valign='top' class='right'>" + frameright + "</td></tr></table>" +
				"<div class='footer'><div class='copyRight'><img src='"+baseURL+"/themes/default/images/copyRight.gif' /></div></div>";
			
			$(this).html(frame);
		});
	}
})(jQuery);