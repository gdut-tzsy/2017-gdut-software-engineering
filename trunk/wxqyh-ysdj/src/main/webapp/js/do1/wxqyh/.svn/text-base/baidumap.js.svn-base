var baiduMapJson;
var _baiduInited=false;
var parentPage;
function _initBaiduMap(){

}
function baiduMap(type){
    if(!_baiduInited){
        $.getScript(_baiduMapUrl,function(rsp,status){
            _baiduInited=true;
            if(type=="kq")
                _kqBaiduMapShow();
            else
            _baiduMapShow();
        });
    }else
    {
        if(type=="kq")
            _kqBaiduMapShow();
        else
            _baiduMapShow();
    }

}
function _baiduMapShow(){

    function G(id) {
        return document.getElementById(id);
    }
    var map = new BMap.Map("map");
    // 初始化地图,设置城市和地图级别。

    if(baiduMapJson){
        var json=baiduMapJson;
        map.centerAndZoom(new BMap.Point(json.x,json.y), json.zoom);
        map.removeOverlay(map.getOverlays()[0]);//清除上一个标注
        map.addOverlay(new BMap.Marker(new BMap.Point(json.x,json.y)));            //增加点
    }else{
        map.centerAndZoom("广州市",12);
    }
    // 添加带有定位的导航控件
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);
    // 添加定位控件
    var geolocationControl = new BMap.GeolocationControl();
    geolocationControl.addEventListener("locationSuccess", function(e){
        // 定位成功事件
        var address = '';
        address += e.addressComponent.province;
        address += e.addressComponent.city;
        address += e.addressComponent.district;
        address += e.addressComponent.street;
        address += e.addressComponent.streetNumber;
        alert("当前定位地址为：" + address);
    });
    geolocationControl.addEventListener("locationError",function(e){
        // 定位失败事件
        alert(e.message);

    });
    map.addControl(geolocationControl);

    /* map.centerAndZoom(new BMap.Point(106.544433,29.564965), 18); */
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });


    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    var x,y,zoom;//经度,纬度,缩放级别;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;

        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;



        /* var jsonStr={'省':_value.province,'市':_value.city,'地区':_value.district,'街道':_value.street,'地点名称':_value.business,'门牌号':addComp.streetNumber,'经度':x,'纬度':y}; */

        jsonStr={'province':_value.province,'city':_value.city,'district':_value.district,'street':_value.street,'business':_value.business};


        setPlace();
    });
    /*    G('suggestId').addEventListener('keyup', function(e){
     if(e.which==13){
     myValue= G('suggestId').value;
     G("searchResultPanel").innerHTML ="onconfirm<br />index = " + 0 + "<br />myValue = " +myValue;
     setPlace();
     }
     }, false) */

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            /*  map.addOverlay(new BMap.Marker(pp));    //添加标注 */

            x=pp.lng;//经度
            y=pp.lat;//纬度
            zoom=map.Ba;//缩放级别
            map.addOverlay(new BMap.Marker(new BMap.Point(x,y))); //增加点
            jsonStr.x=x;
            jsonStr.y=y;
            jsonStr.zoom=zoom;
            G('maphidden').value=JSON.stringify(jsonStr);

        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });

        local.search(myValue);
    }

    // 百度地图API功能
    // var map = new BMap.Map("allmap");
    // var point = new BMap.Point(116.404, 39.915);
    // map.centerAndZoom(point,15);
    var menu = new BMap.ContextMenu();
    var txtMenuItem = [
        {
            text:'放大',
            callback:function(){map.zoomIn()}
        },
        {
            text:'缩小',
            callback:function(){map.zoomOut()}
        }
    ];
    for(var i=0; i < txtMenuItem.length; i++){
        menu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
    }
    map.addContextMenu(menu);
    map.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
    map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    var geoc = new BMap.Geocoder();
    map.addEventListener("click", function(e){
        var pt = e.point;
        x=e.point.lng;//经度
        y=e.point.lat;//纬度
        zoom=e.target.Ba;//缩放级别
        map.clearOverlays();//清除上一个标注
        map.addOverlay(new BMap.Marker(new BMap.Point(x,y))); //增加点
        geoc.getLocation(pt, function(rs){
            var addComp = rs.addressComponents;
            G('suggestId').value=addComp.province  + addComp.city  + addComp.district  + addComp.street  + addComp.streetNumber;
            /*   var jsonStr={'省':addComp.province,'市':addComp.city,'地区':addComp.district,'街道':addComp.street,'门牌号':addComp.streetNumber,'经度':x,'纬度':y}; */
            var jsonStr={'province':addComp.province,'city':addComp.city,'district':addComp.district,'street':addComp.street,'streetNumber':addComp.streetNumber,'x':x,'y':y,'zoom':zoom};
            G('maphidden').value=JSON.stringify(jsonStr);
        });
    });

}
/*百度地图--考勤范围限制  */
function baiduMapfanwei(obj){
    if((!(/^[0-9]*[1-9][0-9]*$/).test($(obj).val()))||($(obj).val().length>1&&$(obj).val().substr(0,1)=="0")){
        $(obj).val(200)
    }else if($(obj).val()<200){
        $(obj).val(200)
    }
}

var myLat="";
var myLng="";
var myLocation="";
/*百度地图确认关闭取消操作  */
function removeMap(obj){
    if(!$(obj).hasClass('quxiao')){//确认 关闭,按钮
        var val=$('#suggestId',window.top.document).val();
        if(val){
        	
        	
        	var $targetPage=$(parentPage);
/*        	if(!$(window.frames['personFrame'].window.frames['frame'].document.body).find("#baiduMapBtn")){
        		$targetPage=$(window.frames['personFrame'].window.frames['frame'].document.body);
        	}else{
        		$targetPage=$(window.frames['personFrame'].document.body);
        	}*/
            var btn=$targetPage.find('#baiduMapBtn');
            btn.next('.appendDiv').remove();
            /* $('#baiduMapBtn').next('.appendDiv').remove(); */
            var json=eval('('+$('#maphidden',window.top.document).val()+')');
            /* json.x//经度
             json.y;//纬度
             json.zoom;//缩放级别
             json.province//省份
             json.city//市
             json.district//地区
             json.street//街道
             json.business//地点名称(有些时候这个不存在)
             json.streetNumber//门牌号
             */
            baiduMapJson=json;
            /* baiduMapVal=val;
             hiddenHtml=$('#searchResultPanel',window.top.document).html(); */
            var longitude = json.x;//经度
            var latitude =  json.y;//纬度
            var area = json.province + "," + json.city + "," + json.district;
            var signScope =  $('#fanwei',window.top.document).val();//考勤范围
            
            myLat=latitude;
            myLng=longitude;
            myLocation=area;
            
        	if(myLat!="" && myLng!=""){
        		//$targetPage.find("#location").val(myLocation);
        		$targetPage.find("#location").val($("#suggestId").val());
        		$targetPage.find("#latitude").val(myLat);
        		$targetPage.find("#longitude").val(myLng);
        		//清除活动显示的字数span
        		//removeSpanNum($targetPage.find("#location"));
        	}
            
            //alert(area);
            
            //console.log(signScope);
            btn.parent('.form-des').append('<div class="appendDiv iblock"><li><span>'+val+'</span><i onclick="liclose(this)"></i><input type=\"hidden\" name=\"tbCheckWorkRolePO.address\" value=\"'+val+'"\/><input type=\"hidden\"class=\"fanweiNum\" name=\"tbCheckWorkRolePO.signScope\" value=\"'+signScope+'"\/><input type=\"hidden\" name=\"tbCheckWorkRolePO.longitude\" value=\"'+longitude+'"\/><input type=\"hidden\" name=\"tbCheckWorkRolePO.latitude\" value=\"'+latitude+'"\/><input type=\"hidden\" name=\"tbCheckWorkRolePO.area\" value=\"'+area+'"\/><input id=\"test\" type=\"hidden\" value="'+$('#maphidden',window.top.document).val()+'"></li></div>');
        }
    }
    $(obj).parents('.baiduMap').remove();
    baiduMapJson=null;
    $('#overlayDiv',window.top.document).hide();
}

//百度地图确认按钮
function mapOK(obj){
	//alert("location:"+myLocation);	
	removeMap(obj);
	
}

//maquanyang 20160509 增加考勤的百度地图
function _kqBaiduMapShow(){

    function G(id) {
        return document.getElementById(id);
    }
    var map = new BMap.Map("map");
    // 初始化地图,设置城市和地图级别。
    var _lat;
    var _lon;
    var place = false;
    if($("#Lat").text()==""){
    	_lat = 113.269068;
    	_lon = 23.136538;
    }else{
    	var _lat = parseFloat($("#Lat").text());
        var _lon = parseFloat($("#Lon").text());
        place = true;
    }
    var point = new BMap.Point(_lat,_lon); 
    var marker = new BMap.Marker(point);        // 创建标注  
    map.addOverlay(marker);
    if(place){
    	var geoc = new BMap.Geocoder();
        geoc.getLocation(point, function(rs){
            var addComp = rs.addressComponents;
            G('suggestId').value=addComp.province  + addComp.city  + addComp.district  + addComp.street  + addComp.streetNumber;
            /*   var jsonStr={'省':addComp.province,'市':addComp.city,'地区':addComp.district,'街道':addComp.street,'门牌号':addComp.streetNumber,'经度':x,'纬度':y}; */
            var jsonStr={'province':addComp.province,'city':addComp.city,'district':addComp.district,'street':addComp.street,'streetNumber':addComp.streetNumber,'x':x,'y':y,'zoom':zoom};
            jsonStr.x=_lat;
            jsonStr.y=_lon;
            G('maphidden').value=JSON.stringify(jsonStr);
        });
    }
    if(baiduMapJson){
        var json=baiduMapJson;
        map.centerAndZoom(new BMap.Point(json.x,json.y), json.zoom);
        map.removeOverlay(map.getOverlays()[0]);//清除上一个标注
        map.addOverlay(new BMap.Marker(new BMap.Point(json.x,json.y)));            //增加点
    }else{
        map.centerAndZoom(point,16);
    }

    //地图比例控件
    var ScaleControl = new BMap.ScaleControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_RIGHT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(ScaleControl);
    //地图类型控件
    var MapTypeControl = new BMap.MapTypeControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(MapTypeControl);
    // 添加带有定位的导航控件
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_RIGHT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });


    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    var x,y,zoom;//经度,纬度,缩放级别;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;

        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        jsonStr={'province':_value.province,'city':_value.city,'district':_value.district,'street':_value.street,'business':_value.business};

        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            //经纬度显示
            Latandlon(local.getResults().getPoi(0).point);
            var newpoint = new BMap.Point(pp.lng,pp.lat);
            var marker = new BMap.Marker(newpoint);
            geoc.getLocation(pp, function(rs){ 
                showLocationInfo(map,marker,pp, rs);  
            });
            map.centerAndZoom(pp, 18);
            x=pp.lng;//经度
            y=pp.lat;//纬度
            zoom=map.Ba;//缩放级别
            map.addOverlay(new BMap.Marker(new BMap.Point(x,y))); //增加点
            jsonStr.x=x;
            jsonStr.y=y;
            jsonStr.zoom=zoom;
            G('maphidden').value=JSON.stringify(jsonStr);
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });

        local.search(myValue);
    }

    // 百度地图API功能
    var menu = new BMap.ContextMenu();
    var txtMenuItem = [
        {
            text:'放大',
            callback:function(){map.zoomIn()}
        },
        {
            text:'缩小',
            callback:function(){map.zoomOut()}
        }
    ];
    for(var i=0; i < txtMenuItem.length; i++){
        menu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
    }
    map.addContextMenu(menu);
    map.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
    map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    var geoc = new BMap.Geocoder();
    map.addEventListener("click", function(e){
        var pt = e.point;
        x=e.point.lng;//经度
        y=e.point.lat;//纬度
        zoom=e.target.Ba;//缩放级别
        map.clearOverlays();//清除上一个标注
        var newpoint = new BMap.Point(e.point.lng,e.point.lat);
        var marker = new BMap.Marker(newpoint);
        map.addOverlay(marker); //增加点
        geoc.getLocation(pt, function(rs){
            var addComp = rs.addressComponents;
            G('suggestId').value=addComp.province  + addComp.city  + addComp.district  + addComp.street  + addComp.streetNumber;
            /*   var jsonStr={'省':addComp.province,'市':addComp.city,'地区':addComp.district,'街道':addComp.street,'门牌号':addComp.streetNumber,'经度':x,'纬度':y}; */
            var jsonStr={'province':addComp.province,'city':addComp.city,'district':addComp.district,'street':addComp.street,'streetNumber':addComp.streetNumber,'x':x,'y':y,'zoom':zoom};
            G('maphidden').value=JSON.stringify(jsonStr);
        });
        Latandlon(e.point);
        geoc.getLocation(e.point, function(rs){ 
            showLocationInfo(map,marker,e.point, rs);  
        });
        marker.enableDragging();    
        marker.addEventListener("dragend", function(e){ 
            var pt = e.point;
            x=e.point.lng;//经度
            y=e.point.lat;//纬度
            zoom=e.target.Ba;//缩放级别
            geoc.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                G('suggestId').value=addComp.province  + addComp.city  + addComp.district  + addComp.street  + addComp.streetNumber;
                /*   var jsonStr={'省':addComp.province,'市':addComp.city,'地区':addComp.district,'街道':addComp.street,'门牌号':addComp.streetNumber,'经度':x,'纬度':y}; */
                var jsonStr={'province':addComp.province,'city':addComp.city,'district':addComp.district,'street':addComp.street,'streetNumber':addComp.streetNumber,'x':x,'y':y,'zoom':zoom};
                G('maphidden').value=JSON.stringify(jsonStr);
            });  
            Latandlon(e.point);
            geoc.getLocation(e.point, function(rs){ 
                showLocationInfo(map,marker,e.point, rs);  
            });    
        })
    });
    


}
//百度地图确认关闭取消操作  
function kqRemoveMap(obj,type,addressDivId){
    if(!$(obj).hasClass('quxiao') && 1 == type){//确认 关闭,按钮;1：确定；0：取消或x
        var val=$('#suggestId',window.top.document).val();
        if(val){
        	//校验范围
          var signScope =  $('#fanwei',window.top.document).val();//考勤范围
          if(!(/^(([0-9]\d{0,5})|([0-9]\d{0,5}(\.\d{1,4})))$/).test(signScope)){
        	  $("#signScopeCheck",window.top.document).show();
        	  $('#fanwei',window.top.document).focus();
        	  return;
          }
          $("#signScopeCheck",window.top.document).hide();
          var $targetPage=$(parentPage);
          var btn=$targetPage.find('#baiduMapBtn');
          //btn.next('.appendDiv').remove();
          var json=eval('('+$('#maphidden',window.top.document).val()+')');
          baiduMapJson=json;
          var longitude = json.x;//经度
          var latitude =  json.y;//纬度
          var area = json.province + "," + json.city + "," + json.district;
          var isOutrangecheckObj = $('#isOutrangecheckMap',window.top.document);//是否允许考勤地址范围外可签到/签退(0：否；1：是)
          var isOutrangecheck = "1";
          if(!isOutrangecheckObj.is(':checked')){
        	  isOutrangecheck = "0";
          }
            
          myLat=latitude;
          myLng=longitude;
          myLocation=area;
            
          if(myLat!="" && myLng!=""){
            $targetPage.find("#location").val($("#suggestId").val());
            $targetPage.find("#latitude").val(myLat);
            $targetPage.find("#longitude").val(myLng);
          }
          if(addressDivId){
        	  var addressInfo = $("#" + addressDivId,$targetPage);
        	  $("#addressKQ",addressInfo).val(val);
        	  $("#showAddressName",addressInfo).html(val);
        	  $(".fanweiNum",addressInfo).val(signScope);
        	  $("#longitudeKQ",addressInfo).val(longitude);
        	  $("#latitudeKQ",addressInfo).val(latitude);
        	  $("#isOutrangecheck",addressInfo).val(area);
          }else{
        	  var timestamp = new Date().getTime()
        	  btn.parent('.form-des').append('<div class="appendDiv iblock" id="addressDiv'+timestamp+'">' + 
             		 '<li><span class="ico" onclick="editMapInfoKQ(this)" id="showAddressName">'+val+'</span><i onclick="licloseQK(this)"></i>' +
             		 '<input type=\"hidden\" name=\"addresss\" id=\"addressKQ\" value=\"'+val+'"\/>' +
             		 '<input type=\"hidden\" class=\"fanweiNum\" name=\"signScopes\" value=\"'+signScope+'"\/>' +
               		 '<input type=\"hidden\" name=\"longitudes\" id=\"longitudeKQ\" value=\"'+longitude+'"\/>' +
               		 '<input type=\"hidden\" name=\"latitudes\" id=\"latitudeKQ\" value=\"'+latitude+'"\/>' +
               		 '<input type=\"hidden\" name=\"areas\" id=\"areaKQ\" value=\"'+area+'"\/>' +
               		 //'<input type=\"hidden\" name=\"tbCheckWorkRolePO.isOutrangecheck\" id="isOutrangecheck" value=\"'+isOutrangecheck+'"\/>' +
               		 '<input id=\"test\" type=\"hidden\" value="'+$('#maphidden',window.top.document).val()+'">' +
               		 '</li></div>'); 
        	  window.frames['personFrame'].frames['frame']._resetFrameHeight();
          }
          $("#isOutrangecheckDiv",$targetPage).show();
        }
    }
    $(obj).parents('.baiduMap1').remove();
    baiduMapJson=null;
    $('#overlayDiv',window.top.document).hide();
}
//经纬显示
function Latandlon(e){
        var Lat = $("#Lat");
        var Lon = $("#Lon");
        Lat.text(e.lng);
        Lon.text(e.lat);
  }
//信息窗口
function showLocationInfo(map,marker,pt, rs){  
    var addComp = rs.addressComponents;
    var sContent = '<div class="signDiv"><div class="popdiv" style="margin-right:10px;margin-left:10px;"><p style="line-height:20px;">当前位置</p><p style="line-height:20px;">当前位置：'+ addComp.city + addComp.district + addComp.street  + addComp.streetNumber +'</p><p style="line-height:20px;">经度：'+pt.lng+'，纬度：'+pt.lat+'</p>   </div>  </div>';
    var point = new BMap.Point(pt.lng,pt.lat);//默认  
    var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象  
    map.openInfoWindow(infoWindow,point);
/*    marker.addEventListener("mouseover", function(event) {
        map.openInfoWindow(infoWindow,point);
    });
    marker.addEventListener("mouseout", function(event) {
        map.closeInfoWindow(infoWindow,point);
    });*/
}