/**
	* 模态框
	* 2016-05-05
*/
;(function($){
	$.dialog = {
		alert:function(options){
			var defaults = {
				title:'提示',
				content:'这是一个封装好的内容容器',
				buttonText:'确认',
			};
			var obj = $.extend(defaults,options);
			if(! $('#mask').length){
				var mask = '<div id="mask"></div>';
				$('body').append(mask);
			}else{
				$('#mask').show();
			}
			if(! $('#js_dialog').length){
				var str = '';
					str +=		  '<div class="pop_up bgWhite" id="js_dialog">';
					str +=		   '<h2 class="pop_title">'+obj.title+'</h2>';
					str +=		          '<div class="pop_content"><p class="tc f16 c666" id="js_pop_content">'+obj.content+'</p></div>';
					str +=		          '<div class="pop_bottom"><button type="button" class="dialog_btn w100p" id="js_cancel_btn">'+obj.buttonText+'</button></div>';
					str +=		  '</div>';
					$('body').append(str);
			}else{
				$('#js_dialog').show();
				$('#js_pop_content').html(obj.content);
				$('#js_cancel_btn').text(obj.buttonText);	
			}

			//关闭弹窗
			$('#js_cancel_btn').off().on('click',function(){
				$('#mask, #js_dialog').hide();
			});
		},
	}
})(jQuery)
function dialog(_content){
	$.dialog.alert({
        content:_content,
    });
}

/**
	* confirm框
	* 2016-05-05
*/
;(function($){
	$.confirm = {
		alert:function(options,callback){
			var defaults = {
				title:'提示',
				content:'这是一个封装好的内容容器',
			};
			var obj = $.extend(defaults,options);
			if(! $('#mask').length){
				var mask = '<div id="mask"></div>';
				$('body').append(mask);
			}else{
				$('#mask').show();
			}
			if(! $('#js_confirm').length){
				var str = '';
					str +=		  '<div class="pop_up bgWhite" id="js_confirm">';
					str +=		   '<h2 class="pop_title">'+obj.title+'</h2>';
					str +=		          '<div class="pop_content"><p class="tc f16 c666" id="js_pop_content">'+obj.content+'</p></div>';
					str +=		          '<div class="pop_bottom"><ul class="flexbox dialog_btn_group"><li class="flexItem"><button type="button" class="dialog_gray_btn w100p" id="js_failed_btn">取消</button></li><li class="flexItem"><button type="button" class="dialog_btn w100p" id="js_succeed_btn">确定</button></li></ul></div>';
					str +=		  '</div>';
					$('body').append(str);
			}else{
				$('#js_confirm').show();
				$('#js_pop_content').html(obj.content);
			}

			//取消
			var cancelMask = function(){
				$('#js_failed_btn').off().on('click',function(){
					$('#mask, #js_confirm').hide();
				});
			}

			//确定
			var okMask = function(){
				$('#js_succeed_btn').off().on('click',function(){
					$('#mask, #js_confirm').hide();
					if (typeof (callback) == 'function') {
		                callback();
		            }
				});
			}
			cancelMask();
			okMask(obj.callback);
		},
	}
})(jQuery)
function _confirm(_content){
	$.confirm.alert({
        content:_content,
    });
}

/**
	* loading
	* 2016-05-05
*/
;(function($){
	$.Loading = {
		show:function(options){
			var defaults = {
				position:'fixed',
				left:'50%',
				top:'50%',
				transform:'translate(-50%,-50%)',
				webkitTransform:'translate(-50%,-50%)',
				zIndex:'10000',
				width:'160px',
				height:'60px',
				padding:'10px',
				text:"努力加载中...",
				background:'rgba(0,0,0,.68)',
				color:'white',
				fontSize:'16px',
				borderRadius:'5px',
			};
			var obj = $.extend(defaults,options);
			var str = '<div id="loadingMask"><div style="width:'+obj.width+';height:'+obj.height+';padding:'+obj.padding+';background:'+obj.background+';position:'+obj.position+';left:'+obj.left+';top:'+obj.top+';transform:'+obj.transform+';-webkit-transform:'+obj.webkitTransform+';z-index:'+obj.zIndex+';color:'+obj.color+';font-size:'+obj.fontSize+';border-radius:'+obj.borderRadius+'"><div style="text-align:center"><span class="sk-spinner sk-spinner-three-bounce"><span class="sk-bounce1"></span><span class="sk-bounce2"></span><span class="sk-bounce3"></span></span></div><p style="text-align:center;padding-top:5px;">'+obj.text+'</p></div></div>';
			$('body').append(str);
		},
		hide:function(){
			$('#loadingMask').remove();
		}
	}
})(jQuery)
function showLoading(_text){
	 $.Loading.show({
         text:_text
     });
}
function closeLoading(){
	$.Loading.hide();
}

//动态设置高度
function setHeight(item,elem){
    $(item).height($(elem).height());
}

//tab切换
function tabUI(active,tab,_this){
    var $index = _this.index();
    _this.addClass(active).
    siblings().removeClass(active);
    $(tab).children().eq($index).show()
    .siblings().hide();
    console.log($index);
}

//控制元素比例
function setScale(elem,num){
    var itemList = document.querySelectorAll(elem);
    for (var i = 0; i < itemList.length; i++) {
        itemList[i].style.height = itemList[i].offsetWidth * num +'px';
    };
}
//设置图片宽高比列
function imgW(elem,num){
    // var team_list_img = document.querySelectorAll(elem);
    // for (var i = 0; i < team_list_img.length; i++) {
    //     team_list_img[i].style.height = team_list_img[i].offsetWidth * num +'px';
    // };
    $(elem).height($(elem).width() * num);
}
