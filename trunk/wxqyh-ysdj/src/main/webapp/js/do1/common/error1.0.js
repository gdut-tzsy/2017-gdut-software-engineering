(function($){
	$.fn.error = function(options){
		var defaults = {
			title   : "",
			content : "",
			button  : "",
			left    : "",
			top     : ""
		}
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var title   = options.title,
				content = options.content,
				obj     = $(this),
				button  = options.button,
				btnlist = "",
				left    = "",
				top     = "";
			for(var list in button){
				btnlist += "<input type='button' class='btn2 mr30' value='" + button[list].text + "' onclick='" + button[list].events + "()' />";
			}
			
			var contents = "<div class='tips'>" +
						   "<table class='tipsBox'  border='0' cellspacing='0' cellpadding='0'>" +
						   "<tr><th> <h2>" + title + "</h2><a href='#' class='close' title='关闭'>关闭</a></th></tr>" +
						   "<tr><td><table class='tipsText' border='0' align='center' cellpadding='0' cellspacing='0'>" +
	        			   "<tr><td rowspan='2' valign='top' class='tipsImg'><img src='"+baseURL+"/themes/default/images/icon/iconTips.png' /></td>" +
            			   "<td valign='top'>" + content + "</td></tr><tr><td align='center' valign='middle'>" +
						   btnlist + "</td></tr></table></td></tr></table></div>";
			obj.pop_up();
			obj.append(contents);
			obj.css({position : 'absolute',zIndex : 100});
			$(".close",obj).click(function(){
				$(".tips").remove();
				($("#pop_back").length > 0) ? $("#pop_back").remove() : "";
			});
			var client       = document.documentElement,
				clientwidth  = client.clientWidth,
				clientheight = client.clientHeight,
				popwidth     = parseInt($(".tips").width()),
				popheight    = parseInt($(".tips").height()),
				poptop       = Math.abs(((clientheight - popheight) / 2 > 0) ? ((clientheight - popheight) / 2) : 0),
				popleft      = Math.abs(((clientwidth - popwidth) / 2  > 0) ? ((clientwidth - popwidth) / 2)  : 0);
			obj.css({top : poptop + 'px',left : popleft + 'px'});
			function doSize(){
				clientwidth  = client.clientWidth;
				clientheight = client.clientHeight + client.scrollTop + document.body.scrollTop;
				newtop       = Math.abs(((clientheight - popheight) / 2 > 0) ? ((clientheight - popheight) / 2) : 0);
				newleft      = Math.abs(((clientwidth - popwidth) / 2  > 0) ? ((clientwidth - popwidth) / 2) : 0);
				obj.css({top:newtop + "px",left:newleft + "px"});
			}
			window.onresize = function(){
				doSize();
			};
		});
	}
})(jQuery);

function _showTip($tipContent){
    $("#id_dqdp_tip").error({title:"提示",content:$tipContent,button : [{text : "确定",events : "test"}]});
}
function _showConfirm($tipContent){
    $("#id_dqdp_tip").error({title:"提示",content:$tipContent,button : [{text : "确定",events : "test"}]});
}