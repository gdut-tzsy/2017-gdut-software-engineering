<%@page import="org.apache.struts2.components.Include"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext"%>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp"%>
<head>
<title>租赁详情</title>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="Pragma" content="no-cache">
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/dy_w.css">
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>"/>

	
	<script type="text/javascript" src="${baseURL}/jsp/xentwap/convenience/js/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/xentwap/convenience/js/swiper-3.3.1.jquery.min.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/xentwap/convenience/js/dy_w.js"></script>
	
	<script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>

    <style>
        .overlay {
            background-color: rgba(0, 0, 0, 0.2);
            height: 500%;
            left: 0;
            position: fixed;
            top: -100%;
            width: 100%;
            z-index: 1100;
        }
        .img_preview{position:absolute;top:0;z-index:1201;width:100%;}
        .inner_img_preview{position:relative;z-index:99;}
        .inner_img_preview img{position:fixed;top:50%;left:50%;max-height:100%;max-width:100%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);}

    </style>
</head>

 <body class="">
        <div id="wrap_main" class="wrap">
            <div id="main" class="wrap_inner">
                <div class="swiper-container w" id="bannerText">
                    <div class="swiper-wrapper boxModal" id="bannerImage">
                        
                    </div>
                    <div class="swiper_num">
                        <span class="fwb f16" id="js_page">1</span> / <span class="f12" id="total">4</span>
                    </div>
                </div>
                <div class="pt10 pl15 pr15 pb15 bgfff">
                    <p class="f20 fwb" id="title">标题</p>
                    <div class="mt10">
                        <div class="fr"><i class="eyes_ico"></i><span class="pl5 ib vm" id="browser">0</span></div>
                        <p class="c999">发布：<span class="pl5 ib vm" id="creator_time">0000-00-00</span></p>
                    </div>
                </div>
                <section class="mt10 bgfff">
                    <div class="flexbox pt15 pb15 lh20 border_b">
                        <div class="flexItem tc pr line_r">
                            <p class="f12 c666">厅室</p>
                            <p class="f15 pt5" id="room">0房0厅0卫</p>
                        </div>
                        <div class="flexItem tc">
                            <p class="f12 c666">租金</p>
                            <p class="f15 pt5"><span class="f15 fwb c_red" id="rent">0000</span>元/月</p>
                        </div>
                    </div>
                    <ul class="table_list" id="js_table">
                    </ul>
                </section>
                <section class="mt10 bgfff">
                    <div class="title_line">房屋配置及描述</div>
                    <div class="pt10 pl25 pb10 pr15 lh20">
                        <span class="fwb">房屋亮点: </span>
                        <span id="house_light"> xx平x房x厅x卫，朝x向，客厅出阳台，住家xx。 </span>
                    </div>
                    <div class="pt10 pl25 pb10 pr15 lh20">
                        <span class="fwb">周边配套: </span>
                        <span id="periphery"> 周边配套xx，xxxxx站台，停车场。</span>
                    </div>
                </section>
            </div>
            <div id="mask" style="display:none;"></div>
        </div>
       
       <%@include file="showMsge.jsp" %>
       <script type="text/javascript">
       $(document).ready(function() {
			showLoading("正在加载...");
			var id = '${param.id}';

			$.ajax({
				url : '${baseURL}/open/lease/openLeaseInfoAction!getLeaseInfo.action?id='+ id,
				dataType : 'json',
				data : {'dqdp_csrf_token' : dqdp_csrf_token},
				success : function(result) {
				if ("0" == result.code) {
					hideLoading();
                    //轮播图
                    var images_div = $('#bannerImage');
                    var hottestInfo = result.data.hottestInfos;
                    //对图片进行判断，如果没有图片则用默认的图片
                    if(hottestInfo == "" || hottestInfo == null){
                        var image_item = '<div class="swiper-slide">'
                            + '<img src="'
                            + '${baseURL}/jsp/xentwap/convenience/images/no-image2.jpg'
                            + '" width="640" height="240"/>'
                            + '</div>';
                        images_div.append(image_item);
                    }else{
                        for (var i = 0; i < hottestInfo.length; i++) {
                            var image_item = '<div class="swiper-slide">'
                                + '<img onclick="showImage(\''+'${compressURL}'+hottestInfo[i]+'\')" src="'
                                + '${compressURL}'
                                + hottestInfo[i]
                                + '" width="640" height="240"/>'
                                + '</div>';
                            images_div.append(image_item);
                        }
                    }
					
			         var mySwiper = new Swiper ('#bannerText', {
			            onInit:function(){
			                $('#total').html($('#bannerText').find('.swiper-slide').length);
			                // $('.swiper-slide img').css({
			                //     'width':'100%',
			                //     'height':$('.swiper-slide').width() *0.45
			                // })
			            },
			            pagination: '.swiper-pagination-square',
			            paginationClickable: true,
			            loop:false,
			            autoplay:'3000',
			            autoplayDisableOnInteraction:false,
			            onSlideChangeStart: function(mySwiper){
			               var num = mySwiper.activeIndex;
			              $('#js_page').html(++num);
			            }
			       });
			         
			         var tbYsjdLeaseInfoPO = result.data.tbYsjdLeaseInfoPO;
			         $("#title").html(tbYsjdLeaseInfoPO.title);
			         $("#browser").html(tbYsjdLeaseInfoPO.browser);
			         $("#creator_time").html(tbYsjdLeaseInfoPO.creatorTime);
			         $("#room").html(tbYsjdLeaseInfoPO.room);
			         $("#rent").html(tbYsjdLeaseInfoPO.rent);
			         $("#house_light").html(tbYsjdLeaseInfoPO.houseLight);
			         $("#periphery").html(tbYsjdLeaseInfoPO.periphery);
			         
			         var community = tbYsjdLeaseInfoPO.community;
                    var area = tbYsjdLeaseInfoPO.area===0?"":tbYsjdLeaseInfoPO.area+'m<sup>2</sup>';
			         var floor = tbYsjdLeaseInfoPO.floor;
			         var payment_method = tbYsjdLeaseInfoPO.paymentMethod;
			         var type = tbYsjdLeaseInfoPO.type;
			         var renovation = tbYsjdLeaseInfoPO.renovation;
			         var rental_mode = tbYsjdLeaseInfoPO.rentalMode;
			         var phone = tbYsjdLeaseInfoPO.phone;
			         var address = tbYsjdLeaseInfoPO.address;
			         var list = [{
			             name:'社区',
			             detail:community
			          },{
			             name:'面积',
			             detail:area
			          },{
			             name:'楼层',
			             detail:floor
			          },{
			             name:'付款',
			             detail:payment_method
			          },{
			             name:'类型',
			             detail:type
			          },{
			             name:'装修',
			             detail:renovation
			          },{
			             name:'出租方式',
			             detail:rental_mode
			          },{
			             name:'联系电话',
			             detail:phone
			          },{
			             name:'地址',
			             detail:address
			          }]
			          tableList(list);
			       //房屋类型   
			         function tableList(array){
			            var len = $(array).length;
			            var str = '';
			            for(var i = 0 ;i < len ;i++){
			                str +='<li class="table_item">'
			                str +=     '<span class="ib w60 c666">'+$(array)[i].name+'</span>'
			                str +=      '<span class="ib vm">'+$(array)[i].detail+'</span>'
			                str +=  '</li>'
			            }
			            $('#js_table').append(str)
			         }
			} else {
				hideLoading();
				_alert("信息提示层", result.desc,"确认",function(){WeixinJS.back();});
			}
		},
			error : function() {
				hideLoading();
				_alert('提示',"网络打了个盹，请重试","确认",function(){WeixinJS.back();});
			}
		});
	});
         //滚动加载
         $(document).on('ready',function(){
           scrollLoadData('#main');
         })
       </script>
    </body>
</html>
