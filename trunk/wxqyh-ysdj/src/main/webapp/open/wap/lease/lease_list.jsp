<%@page import="org.apache.struts2.components.Include" %>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.DqdpAppContext" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
<%@include file="/jsp/wap/include/storage.jsp" %>
<head>
    <meta charset="utf-8">
    <title id="title">租赁信息</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/wt.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${baseURL}/themes/wap/css/base.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="${baseURL}/themes/wap/css/list.css?ver=<%=jsVer%>" />
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/dy_w.css">
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/flipsnap.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/wechat.js"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>

    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <style>
        .declare-item {
            position: relative;
            border-radius: 3px;
            border: 1px solid #e4e4e4;
            background-color: #fff;
        }
        .declare-article{
            padding: 0 10px 0;
        }
        .declare-item_info {
            padding-left: 0;
            line-height: inherit;
        }
        .lease_img_holder {
            display: inline-block;
            width: 80px;
            padding-right: 10px;
            height: 74px;
            padding-bottom: 10px;
            overflow: hidden;
            vertical-align: middle;
        }
        #wrap_main,
        #main {position:absolute;left:0;top:0;bottom:0;width:100%;}
        #wrap_main {overflow:hidden;}
        #main {overflow-y:auto}
    </style>
</head>

<body>

<div id="wrap_main" class="">
    <div class="search-box fixed">
        <form method="" action="" id="submit_search" onsubmit="return false;">
            <ul class="wl_search_wrap flexbox">
                <li class="" ><div class="search_pic" onclick="searchData()"></div></li>
                <li class="flexItem"><input id="titleSearch" type="search" placeholder="请输入关键字搜索" class="wl_search bordera_r5" onkeydown="keyDown13();"></li>
                <li class="flexCenter pl10"  onclick="viewModal(true,'','.ui_aside_page')"><img class="w20h20" src="images/icon/search2.png" alt="" /></li>
            </ul>
        </form>
    </div>
    <div id="main" class="">
        <div id="top_height" style="padding-top: 50px"></div>
        <ul class="show_list" id="declareinfos">

        </ul>
        <div id="listMore">
            <div>
                <!-- <a href="javascript:listMore();">test</a> -->
                <p class="lastComment">下拉更多动态信息</p>
            </div>
        </div>
        <div id="mask" style="display:none;"></div>
    </div>

    <div class="ui_aside_page modal_out">
        <div class="ui_aside_page_inner mb20 wrap">
            <div class="wrap_inner" style="z-index:99">
                <ul class="search_list_r">
                    <li class="flexbox f15">
                        <span class="pr8 flexCenter"><p>价格</p></span>
                        <div class="flexItem"><input type="number" id="rentStart" class="w100p ipt" /></div>
                        <span class="pl8 pr8 flexCenter">至</span>
                        <div class="flexItem"><input type="number" id="rentEnd" class="w100p ipt" /></div>
                    </li>
                    <li>
                        <p class="pb10 f17">厅室</p>
                        <div class="checked_box" id="room">
                            <span onclick="changeRoom(this,'一室')">一室</span>
                            <span onclick="changeRoom(this,'二室')">二室</span>
                            <span onclick="changeRoom(this,'三室')">三室</span>
                            <span onclick="changeRoom(this,'四室')">四室</span>
                            <span onclick="changeRoom(this,'四室以上')">四室以上</span>
                        </div>
                    </li>
                    <li>
                        <p class="pb10 f17">方式</p>
                        <div class="checked_box" id="rentalMode">
                            <span onclick="changeRentalMode(this,'单间出租')">单间出租</span>
                            <span onclick="changeRentalMode(this,'整套出租')">整套出租</span>
                        </div>
                    </li>
                </ul>
            </div>
            <footer class="abs_bottom tc" style="z-index:1000">
                <ul class="flexbox">
                    <li class="flexItem btn btn_white bordera0" onclick="resetPage('.ui_aside_page')">重置</li>
                    <li class="flexItem btn bordera0" onclick="searchData()">确定</li>
                </ul>
            </footer>
        </div>
    </div>
</div>
<div class="overlay" id="overlayImage2" style="display:none;"></div>
<%@include file="/jsp/wap/include/showMsg.jsp" %>
</body>
</html>
<script type="text/javascript" src="js/dy_w.js"></script>
<script>
    $('.searchTitleIcon').on('click',function(){
        if($(this).hasClass('faSortDown')){
            $(this).removeClass('faSortDown').addClass('faSortUp');
        }else if($(this).hasClass('faSortUp')){
            $(this).removeClass('faSortUp').addClass('faSortDown');
        }
    });


    //	简化版搜索框
    $(".search_text").on('click',function(){
        $(".search_text").hide();
    });

    $(".search_Title").on('click',function(){
        $(".search_Popsan").toggle();
        $(".search_Pop").toggle();
    });
    $(".search_inner .icon_del").on('click',function(){
        $('.search_input').val('').focus();
    });
    $(".search_input").focus(function(){
        $(".search_Popsan").hide();
        $(".search_Pop").hide();
    });
    $(".search_Pop").on("click","li",function(){
        $('.onetitle').text($(this).text());
    });

    /*点击外部让弹框消失*/
    $('#mask').click(function(e){
        viewModal(false,'','.ui_aside_page');
    });

    //	选择厅室
    var room="";
    function changeRoom(obj, type){
        $("#room").find("span").removeClass('on');
        $(obj).addClass('on');
        room=type;
    }


    //选择出租方式
    var rental_mode="";
    function changeRentalMode(obj, type){
        $("#rentalMode").find("span").removeClass('on');
        $(obj).addClass('on');
        rental_mode=type;
    }

    var pageIndex = 1;
    var page=0;
    var lastLoadCreateUser;

    var hasMore = true;
    var lock=false;

    function loadPager(){
        $('.searchTitleIcon').removeClass('faSortUp').addClass('faSortDown');
        showLoading("正在加载...");
        $("#listMore").find("p").html("努力加载中...");
        $("#listMore").css({"display":"block"});
        var strDate="page="+pageIndex+"&pageSize=10&searchValue.status=1";

        var title=$.trim($("#titleSearch").val());
        if(title!==""){
            strDate=strDate+"&searchValue.title="+title;
        }

        if ($("#rentStart").val()!==""){
            strDate=strDate+"&searchValue.rentStart="+$("#rentStart").val();
        }

        if ($("#rentEnd").val()!==""){
            strDate=strDate+"&searchValue.rentEnd="+$("#rentEnd").val();
        }

        if (room!==""){
            strDate=strDate+"&searchValue.room="+room;
        }

        if (rental_mode!==""){
            strDate=strDate+"&searchValue.rentalMode="+rental_mode;
        }


        $.ajax({
            type:"POST",
            url: "${baseURL}/open/lease/openLeaseInfoAction!ajaxSearch.action",
            data: strDate,
            cache:false,
            dataType: "json",
            success: function(data){
                room="";
                rental_mode="";
                $("#titleSearch").val("");
                $('.ui_aside_page').find('input').val('');
                $('.ui_aside_page').find('.checked_box span').removeClass('on');
                lock=false;
                hideLoading();
                if ("0" !== data.code) {
                    _alert("提示",data.desc,"确认",function(){restoreSubmit();});
                    return;
                }

                var pageData = data.data.pageData;
                if(typeof (pageData)!=='undefined'){
                    appendLeaseInfos(pageData);
                }

                //判断是否还有数据
                if(data.data.hasMore){
                    $("#listMore").find("p").html("下拉更多动态信息");
                    $("#listMore").css({"display":"block"});
                    hasMore=true;
                }else{
                    $("#listMore").find("p").html("已没有更多");
                    $("#listMore").css({"display":"block"});
                    hasMore=false;
                }
            },
            error: function() {
                lock=false;
                hideLoading();
                $("#listMore").find("p").html("已没有更多");
                $("#listMore").css({"display":"block"});
            }
        });
    }

    $(document).ready(function () {

        $("#titleSearch").val("");
        loadPager();
    });

    //13键按下
    function keyDown13(){
        var event = arguments.callee.caller.arguments[0] || window.event;
        if(event.keyCode === 13){//判断是否按了回车，enter的keycode代码是13，想看其他代码请猛戳这里。
            searchData();
        }
    }
    function searchData() {
        $('#mask').hide();
        $('.ui_aside_page').hide();
        $("#declareinfos").html("");
        pageIndex = 1;
        page=0;
        loadPager();
    }

    function listMore() {
        //加锁避免重复加载 非1的时候锁住不让进行查询
        if(lock==false){
            lock=true;
        }else{
            return;
        }

        if(hasMore==false){
            return;
        }
        pageIndex=pageIndex+1;
        loadPager();
    }

    var str='';
    str +='<li class="show_item flexbox" onclick="hrefTo(\'@gotoURL\')">';
    str +=    '<img src="@image" width="115" height="85" alt="" />';
    str +=    '<div class="flexItem break_all pl10">';
    str +=        '<p class="ellipsis_line2 f15">@TITLE</p>';
    str +=        '<div class="flexbox lh20">';
    str +=            '<div class="flexItem f12 c999">';
    str +=                '<span>@room</span><span class="pl15">@area</span>';
    str +=                '<p>来自个人房源：@creator_user_name</p>';
    str +=            '</div>';
    str +=            '<div class="flexCenter f15 c_red">'
    str +=                '<span class="fwb">@rent/月</span>';
    str +=            '</div>';
    str +=        '</div>';
    str +=    '</div>';
    str +='</li>';

    function appendLeaseInfos(pageData) {
        if (pageData.length > 0) {
            lastLoadCreateUser = pageData[pageData.length - 1].create_user;
        }
        for (var i = 0; i < pageData.length; i++) {
            page++;
            var vo = pageData[i];

            var itemHtml;
            itemHtml=str;

            //因为数据中的image字段是包含多张图片的路径（用逗号分隔），页面展示只需要一张图片即可。
            var image = vo.image.split(",");
            //对图片进行判断，无图片则显示默认图片
            if(image == "" || image == null){
                itemHtml=itemHtml.replaceAll("@image","${baseURL}/open/wap/lease/images/no-image2.jpg");
            }else{
                itemHtml=itemHtml.replaceAll("@image",'${compressURL}'+image[0]);
            }

            var title_rent='';
            if (vo.rentalMode!==""){
                title_rent+=vo.rentalMode+' | ';
            }
            title_rent+=vo.title;
            itemHtml=itemHtml.replaceAll("@TITLE",title_rent);
            itemHtml=itemHtml.replaceAll("@creator_user_name",vo.creatorUserName);
            itemHtml=itemHtml.replaceAll("@rent",vo.rent);
            itemHtml=itemHtml.replaceAll("@room",vo.room);
            itemHtml=itemHtml.replaceAll("@area",vo.area==0?"":vo.area+"m <sup>2</sup>");

            itemHtml=itemHtml.replaceAll("@gotoURL","${baseURL}/open/wap/lease/lease_detail.jsp?id="+vo.id);

            $('#declareinfos').append(itemHtml);
        }
    }

    $(function() {
        // 下拉获取数据事件，请在引用的页面里使用些事件，这为示例
        var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
        var nScrollTop = 0; //滚动到的当前位置
        var $frame = $("#main");
        var nDivHight = $frame.height();
        $frame.on("scroll touchmove click", function() {
            nScrollHight = $(this)[0].scrollHeight;
            nScrollTop = $(this)[0].scrollTop;
            if (nScrollTop + nDivHight >= nScrollHight) {
                // 触发事件，这里可以用AJAX拉取下页的数据
                listMore();
            }
        });
    });
</script>
