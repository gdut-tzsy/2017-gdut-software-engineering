<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div class="baiduMapBox relative" style="display:none;">
         <input type="hidden" value="" id="maphidden">
         <div id="baiduMap" style="height:100%;width:100%"></div>
         
<!--           <div class="suggestIdBox"><input type="text" id="suggestId" /></div>
          <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
          <div class="btn" style="position:absolute;bottom:0;z-index:999999;display:none">确定</div>   -->
          
          <div class="foot_bar" style="position: fixed">
             <div class="foot_bar_inner flexbox">
                 <a class="btn flexItem" style="margin:5px 10px;" href="javascript:;" id="baiduMapBtn">返回</a>
             </div>
         </div>
         <input id="myLag" type="hidden"/>
         <input id="myLat" type="hidden"/>
     </div>
<script type="text/javascript">
	var lat="";
	var lag="";
  	function openMap(){
  		lat=$("#latitude").val();
  		lag=$("#longitude").val();
  		
  		if(lat=="" || lag==""){
     		 showMsg("","经纬度不存在",1);
     		return;
     	}
  		//alert(lat+"_"+lag);
  		$('#wrap_main').hide();
		$('#main').hide();
        $('.baiduMapBox').height($(window).height()).show();
        if(navigator.geolocation){
        	//navigator.geolocation.getCurrentPosition(showPosition,showError);//定位
       		loadJScript();
        }else{
       	 //alert("浏览器不支持地理定位");
            showMsg("","浏览器不支持地理定位。",1);
        };
	}
  	
  	//页面多地址使用此方法
  	function openMapForMore(ele){
		var lat = $(ele).closest(".mapItem").find("input.lat").val();
		var lag = $(ele).closest(".mapItem").find("input.lag").val();

  		if(lat=="" || lag==""){
     		 showMsg("","经纬度不存在",1);
     		return;
     	}
  		//alert(lat+"_"+lag);
		$('#main').hide();
        $('.baiduMapBox').height($(window).height()).show();
        if(navigator.geolocation){
        	//navigator.geolocation.getCurrentPosition(showPosition,showError);//定位
       		loadJScript();
        }else{
       	 //alert("浏览器不支持地理定位");
            showMsg("","浏览器不支持地理定位。",1);
        };
	}
  	
  	
     $(function(){
	     $('#baiduMapBtn').click(function(){
	         $('.baiduMapBox').hide();
	         $('#main').show();
	         $('#wrap_main').show();
	     });
    });

     function loadJScript() { 
        if($('body').children('script#baiduMapScript').length==0){
            //alert('正在加载地图中...'); 
            showLoading();
            var script = document.createElement("script"); 
            script.id='baiduMapScript'; 
            script.type = "text/javascript"; 
            script.src = "${apiMapBaidu}&callback=baiduMap";
            document.body.appendChild(script);
        }
    }
  	function showError(error){ 
	    switch(error.code) { 
	      case error.PERMISSION_DENIED: 
	        //alert("定位失败,用户拒绝请求地理定位"); 
	        showMsg("","定位失败,用户拒绝请求地理定位。",1);
	        break; 
	      case error.POSITION_UNAVAILABLE: 
	        //alert("定位失败,位置信息是不可用"); 
	        showMsg("","定位失败,位置信息是不可用。",1);
	        break; 
	      case error.TIMEOUT: 
	        //alert("定位失败,请求获取用户位置超时"); 
	        showMsg("","定位失败,请求获取用户位置超时。",1);
	        break; 
	      case error.UNKNOWN_ERROR: 
	        //alert("定位失败,定位系统失效"); 
	        showMsg("","定位失败,定位系统失效。",1);
	        break; 
	    } 
 	}
  	
  	//设置位置
    function showPosition(position){ 
	    lat = position.coords.latitude; //纬度 
	    lag = position.coords.longitude; //经度 
	    loadJScript();
    }
  	
    //百度地图API功能
   function baiduMap(){
	   function G(id) {
	       return document.getElementById(id);
	   }
	  	// 百度地图API功能
	   var ggPoint = new BMap.Point(lag,lat);
	   var map = new BMap.Map("baiduMap");  
	   map.centerAndZoom(ggPoint, 15);
	   map.addOverlay(new BMap.Marker(ggPoint));
	   map.addEventListener("tilesloaded",function(){
		   //alert("地图加载完毕");
		   hideLoading();
	   	});
    }
   
</script>