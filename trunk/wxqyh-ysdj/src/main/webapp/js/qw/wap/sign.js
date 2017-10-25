(function(){
	var sign={
			init:function(){},
			submit:function(){
				var imageUrl=canvas.toDataURL("image/png");

				/*
				 * data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAACWCAYAAAD32pUcAAAAp0lEQVR4nO3BAQEAAACCIP+vbkhAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPBrSqQAAZytjkAAAAAASUVORK5CYII=
				 */
				/*
				 * data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAACWCAYAAAD32pUcAAAAp0lEQVR4nO3BAQEAAACCIP+vbkhAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPBrSqQAAZytjkAAAAAASUVORK5CYII=
				 */
				//alert(imageUrl.length+" "+imageUrl);
				if(imageUrl.length==342)
				{
					alert("请输入签名！");
				}
				else
				{
					document.getElementById("imageData").value = imageUrl;
					$.ajax({
						url:baseURL+"/portal/dynamicinfomanage/dynamicinfomanageAction!signForMobile.action",
						type:"post",
						data:{
							"imageData":document.getElementById("imageData").value
						},
						dataType:"json",
						success:function(result){
							if("0" != result.code)
							{

								//showMsg("温馨提示",data.desc,1,{ok:function(result){WeixinJS.back();}});
								return;
							}

							alert("签名成功");

						},
						error: function() {  
							showMsg("温馨提示","网络打了个盹，请重试",1,{ok:function(result){WeixinJS.back();}});
						}
					});
				}

			}


	}


	window.sign=sign;

})(window);
$(document).ready(function(){
	$("#btnConfirm1").on("click",function(){
		sign.submit();
	})
	$("#btnCancel1").on("click",function(){
		var canvas= document.getElementById("canvas");
		canvas.width=canvas.width;
	})

});