    
	var var_form_list;
    
    $(document).ready(function(){
    	if($("#judge").val()=="1"){
    		ajaxSubmit(null);	//异步提交请求
    		return;
    	}else{
    		ajaxSearch();
    	}
    })
	
    function ajaxSubmit(data){
		var t = $('#type').val();
    	$.ajax({
    		url:baseURL+"/portal/wifi/appAction!sendMappingMsg.action",
    		type:"POST",
    		data:{"month":data,"type":t},
    		dataType:"json",
    		success:function(result){
    			_alert("","你的查询请求已提交，请稍候！","确认",function(){WeixinJS.back();});
    		},
    		error:function(){
    			_alert("",result.desc,"确认",function(){WeixinJS.back();});
    		}
    	})
    }
    
    function ajaxSearch(){
    	$.ajax({
    		url:baseURL+"/api/wifi/callBackAction/back.action?reqId=${param.reqId }",
    		type:"POST",
//     		data:{"month" : data},
    		dataType:"json",
    		success:function(result){
                var_form_list = result.data;
    		},
    		error:function(){
    			_alert("",result.desc,"确认",function(){WeixinJS.back();});
    		}
    	})
    }