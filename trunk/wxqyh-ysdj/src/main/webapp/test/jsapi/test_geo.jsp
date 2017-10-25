<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 15/5/20
  Time: 12:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>地理位置</title>
    <script type="text/javascript" src="${apiMapBaidu}"></script>
		<script type="text/javascript" src="${convertorBaidu}"></script>
		
</head>
<jsp:include page="test_common.jsp">
    <jsp:param name="useWeixinJsApi" value="true"></jsp:param>
</jsp:include>
<body id="id_body" style="display: none">
<div>
    <ul>
        <li><a href="javascript:getWechatGeoInfo();">获取weixin地理位置接口 </a></li>
        <li><a href="javascript:getBaiduGeoInfo();">获取baidu地理位置接口 </a></li>
        <li><a href="javascript:mapViewer();">使用微信内置地图查看位置接口 </a></li>
    </ul>
</div>

	<div id="allmap" style="width:700;height:700;background-color: green"></div>

</body>
</html>
<jsp:include page="test_tail.jsp"></jsp:include>
<script type="text/javascript">
    var latitude = null; // 纬度，浮点数，范围为90 ~ -90
    var longitude = null; // 经度，浮点数，范围为180 ~ -180。
    var speed = null; // 速度，以米/每秒计
    var accuracy = null; // 位置精度
    var address=null;
    var name=null;
    function mapViewer() {
    	
         wx.openLocation({
            latitude: latitude,//23.104109, // 纬度，浮点数，范围为90 ~ -90
            longitude: longitude,//113.309219, // 经度，浮点数，范围为180 ~ -180。
            name: '', // 位置名
            address: '', // 地址详情说明
            scale: 15, // 地图缩放级别,整形值,范围从1~28。默认为最大
            infoUrl: 'www.baidu.com' // 在查看位置界面底部显示的超链接,可点击跳转
        }); 
    }
    function getBaiduGeoInfo(){
    	
    	var geolocation = new BMap.Geolocation();
    	alert("a");
    	geolocation.getCurrentPosition(function(r){
    		
    		if(this.getStatus() == BMAP_STATUS_SUCCESS){
    			
    			alert('您的位置：'+r.point.lng+','+r.point.lat);
    			var pt = r.point;
    			BMap.Convertor.translate(pt,0,function(point) {
    			var geoc = new BMap.Geocoder(); 
    			geoc.getLocation(point, function(rs){
    				latitude = point.latitude; // 纬度，浮点数，范围为90 ~ -90
                    longitude = point.longitude;
    				var addComp = rs.addressComponents;
    				alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
    				showBaidu(point,"地理信息获取成功");
    			});   
    			});
    		}
    		else {
    			alert('failed'+this.getStatus());
    		}        
    	},{enableHighAccuracy: true,type:"gcj02"});
    }
    function getWechatGeoInfo() {
        wx.getLocation({
        	//type: "gcj02",
            success: function (res) {
                latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                speed = res.speed; // 速度，以米/每秒计
                accuracy = res.accuracy; // 位置精度
                
                //mapViewer();
                  alert("0:"+longitude+"|"+latitude);
                var point=new BMap.Point(longitude,latitude);
                alert("1:"+point.lng);
				BMap.Convertor.translate(point,0,function(point) {
					latitude = point.lng; // 纬度，浮点数，范围为90 ~ -90
	                longitude = point.lat; 
					alert("2:"+point.lng);
					var geoc = new BMap.Geocoder(); 
	    			geoc.getLocation(point, function(rs){
	    				var addComp = rs.addressComponents;
	    				name=(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
	    				alert(name);
	    				showBaidu(point,name,addComp.city);
	    			});   
	    			
					
			    });   
                //alert("地理信息获取成功，现在可以查看地图了");
            },
            fail: function (err) {
                alert("地理信息获取失败：" + err.errMsg);
            }
        });
    }
    
  //显示百度地图
    function showBaidu(point,address,city)
    {
	  alert(city);
    	var map = new BMap.Map("allmap");    // 创建Map实例
    	map.centerAndZoom(point, 15);  // 初始化地图,设置中心点坐标和地图级别
    	var marker = new BMap.Marker(point);
    	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    	map.setCurrentCity(city);          // 设置地图显示的城市 此项是必须设置的
    	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    	map.addOverlay(marker); 
    }
</script>