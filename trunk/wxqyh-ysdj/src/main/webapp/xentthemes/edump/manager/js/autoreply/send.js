//tab切换
function tabUI(active,tab,_this){
    var $index = $(_this).index();
    $(_this).addClass(active).
    siblings().removeClass(active);
    $(tab).children().eq($index).show()
    .siblings().hide();
}

/**
	* 表情包
	@method showEmotions 显示QQ表情包
	@method hideEmotions 隐藏QQ表情包
	@method appendEmotions 添加QQ表情
*/
function QQemotions(){
	_this = this;
	//显示QQ表情包
	_this.showEmotions = function(){	
		$('#js_emotionArea').toggle();
		$.ajax({
			url: 'emotions.html',
			type: 'GET',
			dataType: 'html',
			cache: true,
		})
		.done(function(html) {
			$('#js_emotions').html(html);
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
	}
	//隐藏QQ表情包
	_this.hideEmotions = function(){
		$('#js_emotionArea').hide();
	}
	//获取gif表情文字
	_this.getGifEmotions = function(o,type){
		var $this = $(o),
		title = $this.find('.js_emotion_i').attr('data-'+type+'');
		return title;
	}

	//字符串转图片
	_this.strToPhoto = function(){
		var $v = $('#msg_content').val();
		var faceElm = document.querySelectorAll('#js_emotions li i'),
			len = faceElm.length;
        for (var i = 0; i < len; i++) {
            var faceElmR = faceElm[i].getAttribute('data-title'),
            	dataSrc =  faceElm[i].getAttribute('data-gifurl');
            $v = $v.replace('['+faceElmR+']','<img width="22" height="22" src="'+dataSrc+'">');
        };
        
        // $('.textarea_wrap').append('<div>'+$v+'</div>');
        return $v;
	}

	//清除文字和表情
	_this.clearContent = function(){
		$('#msg_content').html('');
	}

	//输入的字数
	_this.inputNum = function(o){
		var $this  = $(o),
		$v = $.trim($this.val()),
		$num = 600,
		len = $v.length;
		return $num - len;
	}

	//显示gif表情
	var $js_emotions = $('#js_emotions'),
		js_emotionPreviewArea = $('#js_emotionPreviewArea');
	$js_emotions.on('mouseenter','li.emotions_item',function(){
		js_emotionPreviewArea.html('<img src="'+_this.getGifEmotions(this,'gifurl')+'">');	
	})
	.on('mouseleave','li.emotions_item',function(){
		js_emotionPreviewArea.html('');	
	})

	// 插入gif表情
	$js_emotions.on('click','li.emotions_item',function(){
        var t = document.getElementById('msg_content');
        t.value = t.value.substr(0, t.selectionStart) + '['+_this.getGifEmotions(this,'title')+']' + t.value.substring(t.selectionEnd);
        _this.inputNum('#msg_content');
	})

	//点击document隐藏QQ表情
	document.addEventListener('click',function(e){
		if (e.target.className != 'face_ico'){
			_this.hideEmotions();
		}
	},false)


	//监听输入字数 
	$('#msg_content').on('input propertychange',function(){
		$('#enable_write').text(_this.inputNum(this));
	})
}

var qqEmotions = new QQemotions();