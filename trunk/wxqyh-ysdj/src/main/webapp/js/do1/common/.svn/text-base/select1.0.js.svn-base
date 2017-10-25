(function($){
	$.fn.showselect = function(options){
		var defaults = {
			firstdata  : false,
			seconddata : false,
			thirddata  : false
		};
		var options = $.extend(defaults,options);
		$(this).each(function(){
			var first  = options.firstdata,
				second = options.seconddata,
				third  = options.thirddata;
				obj    = $(this);
			
			if(first){
				obj.append("<select name='first'></select>");
				$.each(first,function(i,n){
					$("select[name=first]",obj)[0].options[i] = new Option(n,i);
				});
			}
			if(second){
				obj.append("<select name='second'></select>");
				$.each(second[0],function(i,n){
					$("select[name=second]",obj)[0].options[i] = new Option(n,i);
				});
			}
			if(third){
				obj.append("<select name='third'></select>");
				$.each(third[0],function(i,n){
					$("select[name=third]",obj)[0].options[i] = new Option(n,i);
				});
			}
			$("select[name=first]",obj).change(function(){
				if(!second) return false;
				var num = parseInt($(this).val());
				$.each((second[num]), function(i, n){
					$('select[name=second]', obj)[0].options[i] = new Option(n,i);				
				});
				if(!third) return false;
				$.each((third[num]), function(i, n){
					$('select[name=third]', obj)[0].options[i] = new Option(n,i);				
				});
			});
			$("select[name=second]",obj).change(function(){
				if(!third) return false;
				var num = parseInt($(this).val());
				$.each((third[num]), function(i, n){
					$('select[name=third]', obj)[0].options[i] = new Option(n,i);				
				});
			});
		});
	}
})(jQuery);