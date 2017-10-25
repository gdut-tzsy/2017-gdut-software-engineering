(function($){
	$.fn.pop_up = function(options){
		var defaults = {
			clientwidth  : "",//遮罩层宽度
			clientheight : ""//遮罩层高度
		};
		
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var client       = document.documentElement,
				clientwidth  = options.clientwidth ? options.clientwidth : client.clientWidth,
				clientheight = options.clientheight ? options.clientheight : client.scrollHeight,
				popframe     = "";
			
			var str = $(this).html();
			popframe = "<iframe id='pop_back' frameborder='0' scrolling='no' style='position:absolute; left:0; top:0; opacity:0.6; filter:alpha(opacity=60); height:" + clientheight + "px; width:" + clientwidth + "px;background-color:#dbdbdb; z-index:10;'></iframe>";
			
			
			function doSize(){
				clientwidth  = client.clientWidth;
				clientheight = client.clientHeight + client.scrollTop + document.body.scrollTop;
				$("#pop_back").css({"width":clientwidth + "px","height":clientheight + "px","position":"absolute","top":0,"left":0});
			}
			window.onresize = function(){
				doSize();
			};
			window.onscroll = function(){
				doSize();
			};
			$("body").append(popframe);
		});
	};
})(jQuery);