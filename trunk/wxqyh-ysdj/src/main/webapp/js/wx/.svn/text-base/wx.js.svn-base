function barCode(obj,type){
		wx.scanQRCode({
		    desc: 'scanQRCode desc',
		    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		    success: function (res) {
		    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		    if(type=="imageRadioButton"){
		   //	$(obj.previousSibling).val(result);
		    	$($(obj).next()).val(result);
				$(obj).parent().find('.textarea-close').show()
		    }else if(type=="expresslist"){//快递单据扫描
		    	result = $($(obj).next()).val()+result+'\n';
		    	if(result.length>500){return;}
		    	$($(obj).next()).val(result);
		    	$('textarea.inputStyle').trigger('click');
			} else if (type == "meetingSummary") {
				scanCodeToSummary(result);
		    }
		 }
		});
	 }