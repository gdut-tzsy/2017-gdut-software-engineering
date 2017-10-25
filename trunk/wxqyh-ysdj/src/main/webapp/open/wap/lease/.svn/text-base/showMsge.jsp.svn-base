<%@page language="java" contentType="text/html; charset=UTF-8"%>
<script type="text/javascript" src="${jweixin}"></script>
<script type="text/javascript" src="${baseURL}/jsp/wap/js/CheckJSApi.js?ver=<%=jsVer%>"></script>
<script type="text/javascript" language="javascript">

    var operationNeedHandle = "";

    var width = $(this).width(),
        height = $(this).height(),
        win_width = $(window).width(),
        win_height = ($(window).height())*0.9;//需要减去微信上方的头的2倍高度
    var imageObject; //图片参数对象
    var initialImage; //初始图片对象
    var imgUrlArr=[]; //全部图片url
    var hasLoad=false; //是否已加载

    /**
     * 关闭消息窗口 operationRCD{0:确定, -1:取消，1:关闭}
     */
    function closeMsgBox(flag) {
        $("#hmsgTitle").html("");
        $("#pmsgContent").html("");
        $(".overlay").hide();
        document.getElementById("showMsg_div").style.display = "none";

        if (operationNeedHandle == undefined || operationNeedHandle == "") {
            return;
        } else if (operationNeedHandle == 1) {
            if (0 == flag) {
                try {
                    handle();
                } catch (e) {

                }
            }
        } else {
            //执行操作事件
            if (0 == flag) {
                e = operationNeedHandle.ok;
            } else {
                e = operationNeedHandle.fail;
            }
            if (e != null) {
                e && e.call(e, null, null);
            }
        }
    }

    /**
     *显示加载页面
     *msgContent 加载页面显示的内容，如果不传，默认为"加载中..."
     */
    function showLoading(msgContent) {
        //传入的信息为空
        if(msgContent == undefined || msgContent==""){
            $("#loading_text").html("加载中...");
        }
        else{
            $("#loading_text").html(msgContent);
        }
        $(".overlay").show();
        win_height = document.getElementById("overlayImage").offsetHeight/5;
        win_width = document.getElementById("overlayImage").offsetWidth;

        document.getElementById("loading_simple_div").style.display = "block";
        //让加载中剧中对齐
        var tips_width = document.getElementById("loading_simple_div").offsetWidth,
            tips_height= document.getElementById("loading_simple_div").offsetHeight,
            top = (win_height-tips_height)/2,
            left = (win_width-tips_width)/2;
        $("#loading_simple_div").css({
            'top' : top + "px",
            'left' : left + "px"
        });
    }

    /**
     *隐藏加载页面
     */
    function hideLoading() {
        $(".overlay").hide();
        document.getElementById("loading_simple_div").style.display = "none";
    }

    /**
     * show original image
     */
    function showImage(url) {
        $("#overlayImage").show();
        $("#loading_image_div").show();
        imageOld(url);
        var totalIMG=$(".wrap_inner img");
        imgUrlArr=[];
        for(var i=0;i<totalIMG.length;i++){
            var url=totalIMG.eq(i).attr("src");
            if(url && url!="" && url.indexOf("/upload")!= -1){
                imgUrlArr.push(url);
            }
        }
    }
    //预览图片旋转方法
    function imageOld(url){ //初始化
        $(".inner_img_preview").children().remove();
        $(".inner_img_preview").append('<img  id="src_image"  src="'+url+'" onclick="hideImage();" alt=""/>');
        $(".icon-download a").attr("href",url);		//下载图片链接
        var h=$(window).height();
        var w=$(window).width();
        var footerHeight=$(".pcimg-preview-footer").height(); //底部高度
        var viewerHeight =Math.max(h-footerHeight, footerHeight);//可视高度
        imageObject={};
        var $image=$("#src_image");
        $("#src_image").on("load",function(event){
            if(hasLoad){
                return;
            }
            var aspectRatio = $image[0].naturalWidth / $image[0].naturalHeight;
            var mtH=$image.height()/2,mlW=$image.width()/2;
            if(mtH>=h) {
                mtH = h/2;
            }
            if(mlW>=w){
                mlW=w/2;
            }
            imageObject = {
                naturalWidth:$image[0].naturalWidth,
                naturalHeight:$image[0].naturalHeight,
                aspectRatio:aspectRatio,
                ratio:$image.width() / $image[0].naturalWidth,
                width: $image.width(),
                height: $image.height(),
                marginLeft: -mlW,
                marginTop: -mtH,
                rotateDeg:0,
            };
            if(imageObject.height<viewerHeight){
                imageObject.marginTop=-mtH-(footerHeight/2);
            }
            initialImage = $.extend({}, imageObject); //保存原图对象
            this.imageObject = imageObject;
            this.initialImage = initialImage;
            $(".pcimg-operate-list .icon-resize").parent().addClass("disable");
            renderImage();
            hasLoad=true;
        })
        $(".pcimg-operate-list li").off('click').on("click",function(e){
            var $target = $(e.target);
            var action = $target.attr('data-id');
            $("#src_image").removeClass("rotateTransition");
            if($(this).hasClass("disable")){
                return;
            }
            switch (action) {
                case 'reset':
                    reset($image);
                    break;
                case 'rotate':
                    rotate(90,$image);
                    break;
                case 'zoomIn':
                    zoom(0.1,true);
                    break;
                case 'zoomOut':
                    zoom(-0.1,true);
                    break;
                case "prev":
                    view("prev");
                    break;
                case "next":
                    view("next");
                    break;
            }
        })
        $("#loading_image_div").off("wheel mousewheel DOMMouseScroll").on("wheel mousewheel DOMMouseScroll",function(event){ //滑轮
            event.preventDefault();
            var e = event.originalEvent;
            var delta = 1;
            var ratio=0.1;

            if (e.deltaY) {
                delta = e.deltaY > 0 ? 1 : -1;
            } else if (e.wheelDelta) {
                delta = -e.wheelDelta / 120;
            } else if (e.detail) {
                delta = e.detail > 0 ? 1 : -1;
            }
            zoom(-delta * ratio,true);
        });
    }

    function rotate(degrees,$image){ //旋转
        $("#src_image").addClass("rotateTransition");
        if(isNumber(degrees)) {
            imageObject.rotateDeg = ((imageObject.rotateDeg || 0) + degrees);
        }
        renderImage();
        if(imageObject.rotateDeg>0){
            $(".pcimg-operate-list .icon-resize").parent().removeClass("disable");
        }
    }

    function isNumber(n) {
        return typeof n === 'number' && !isNaN(n);
    }

    function reset($image){ //重置
        imageObject.rotateDeg=0;
        $(".pcimg-operate-list .icon-resize").parent().addClass("disable");
        this.imageObject = $.extend({}, this.initialImage);
        renderImage();
    }
    function zoom(ratio,hasZoom){ //放大放小
        var minZoomRatio = Math.max(0.01, 0.01),maxZoomRatio = Math.min(100, 100);
        var width,height,h=$(window).height();
        var footerHeight=$(".pcimg-preview-footer").height();
        var viewerHeight =Math.max(h-footerHeight, footerHeight);
        ratio=parseFloat(ratio);
        if (isNumber(ratio)){
            if (ratio < 0) {
                ratio =  1 / (1 - ratio);
            } else {
                ratio = 1 + ratio;
            }
        }
        width = imageObject.width * ratio;
        height = imageObject.height * ratio;
        ratio = width / imageObject.naturalWidth;
        ratio = Math.min(Math.max(ratio, minZoomRatio), maxZoomRatio);

        imageObject.marginLeft = -(width/ 2);
        imageObject.marginTop =-(height/ 2);
        imageObject.width = width;
        imageObject.height = height;
        imageObject.ratio = ratio;
        if(imageObject.height<viewerHeight){
            imageObject.marginTop=-(height+footerHeight)/ 2;
        }
        renderImage();
        if(ratio!=1){
            $(".pcimg-operate-list .icon-resize").parent().removeClass("disable");
        }
    }
    function renderImage(){//设置图片样式
        var endImage=this.imageObject;
        $("#src_image").css({
            maxWidth: "",
            maxHeight: "",
            minWidth: "",
            minHeight: "",
        })
        if(endImage.width>=endImage.naturalWidth) {
            $("#src_image").css({
                maxWidth: endImage.width,
                maxHeight: endImage.height,
                minWidth: "",
                minHeight: "",
            })
        }else{
            $("#src_image").css({
                maxWidth: endImage.width,
                maxHeight: endImage.height,
                minWidth: "",
                minHeight: "",
            })
        }
        $("#src_image").css({
            width:endImage.width,
            height:endImage.height,
            marginLeft: endImage.marginLeft,
            marginTop: endImage.marginTop,
            transform:'rotate(' + endImage.rotateDeg + 'deg)',
        });
    }
    //上下张
    function view(type){
        var nowUrl=$("#src_image").attr("src"),sub;
        var num=nowUrl.lastIndexOf("\/");
        nowUrl=nowUrl.substring(num + 1,nowUrl.length);
        for(var i=0;i<imgUrlArr.length;i++){
            if(imgUrlArr[i].indexOf(nowUrl)!=-1){
                sub=i;
            }
        }
        var imgLength=imgUrlArr.length;
        if(sub>0 && type=="prev"){
            imgUrlArr[sub-1] && imgUrlArr[sub-1]!=""? imageOld(lastImageUrl(sub-1)):"";
        }
        if(sub<imgLength && type=="next"){
            imgUrlArr[sub+1] && imgUrlArr[sub+1]!=""? imageOld(lastImageUrl(sub+1)):"";
        }
    }
    function lastImageUrl(sub){  //下一张要显示的图片url
        hasLoad=false;
        var nowCheckUrl=imgUrlArr[sub];
        if(nowCheckUrl.indexOf("/compress/upload/img/")!=-1){
            nowCheckUrl=nowCheckUrl.split("compress/").join("");
        }
        return nowCheckUrl;
    }

    /**
     * hide original image
     */
    function hideImage() {
        $("#overlayImage").hide();
        $("#loading_image_div").hide();
        hasLoad=false;
    }


    /**
     * option 选项
     * msgTitle 标题
     * needHandle 传入成功或者失败的处理函数，如下{ok:function(result){},fail:function(result){}}
     */
    function showChooseBox(option, msgTitle, needHandle,flowId) {
        if(1<arguments.length && msgTitle != ""){
            $("#chooseMsgTital").html("<p>"+msgTitle+"</p>");
        }
        else{
            $("#chooseMsgTital").hide();
        }
        if(option.length>0){
            var temp = "";
            if(flowId){
                for(var i=0;i<option.length;i++){
                    if(flowId==option[i].id){
                        temp += '<li class="active"><input type="radio" style="display:none" name="radio_choose" checked="checked" vname="'+option[i].name+'" value="'+option[i].id+'">'+
                            '<div class="xian_option"><i class="fa"></i>'+option[i].name+'</div></li>';
                    }
                    else{
                        temp += '<li><input type="radio" style="display:none" name="radio_choose" vname="'+option[i].name+'" value="'+option[i].id+'">'+
                            '<div class="xian_option"><i class="fa"></i>'+option[i].name+'</div></li>';
                    }
                }
            }
            else{
                for(var i=0;i<option.length;i++){
                    temp += '<li><input type="radio" style="display:none" name="radio_choose" vname="'+option[i].name+'" value="'+option[i].id+'">'+
                        '<div class="xian_option"><i class="fa"></i>'+option[i].name+'</div></li>';
                }
            }
            $("#chooseMsgUl").html(temp);
        }

        $(".overlay").show();
        win_height = document.getElementById("overlayImage").offsetHeight/5;
        win_width = document.getElementById("overlayImage").offsetWidth;
        //$("#showMsg_div").show();
        document.getElementById("chooseMsg_div").style.display = "block";
        //让加载中剧中对齐
        var tips_width = document.getElementById("chooseMsg_div").offsetWidth,
            tips_height= document.getElementById("chooseMsg_div").offsetHeight,
            top = (win_height-tips_height)/2,
            left = (win_width-tips_width)/2;
        $("#chooseMsg_div").css({
            'top' : top + "px",
            'left' : left + "px"
        });
        if(2<arguments.length){
            operationNeedHandle = needHandle;
        }
        $('#chooseMsgUl').on('click','li',function(event){
            var self = $(this),
                context = self.parent(),
                siblings = $('.active',context);

            siblings.removeClass('active');
            self.addClass('active');

            context.find("input[type='radio']").attr("checked", false);
            self.find("input[type='radio']").attr("checked", true);
        });
    }
    /**
     * 关闭消息窗口 operationRCD{0:确定, -1:取消，1:关闭}
     */
    function closeChooseMsgBox(flag) {
        //console.log($("#chooseMsgUl input[name='radio_choose'][checked]"));
        var choose =  $("#chooseMsgUl input[name='radio_choose'][checked]");
        $("#chooseMsgTital").html("");
        $("#chooseMsgUl").html("");
        $(".overlay").hide();
        document.getElementById("chooseMsg_div").style.display = "none";

        if (operationNeedHandle == "") {
            return;
        }
        else {
            //执行操作事件
            if (0 == flag) {
                e = operationNeedHandle.ok;
            } else {
                e = operationNeedHandle.fail;
            }
            if (e != null) {
                var chooseValue = {};
                chooseValue.id = choose.val();
                chooseValue.name = choose.attr("vname");
                e && e.call(e, chooseValue, null);
            }
        }
    }

</script>
<div class="text_tips" id="showMsg_div"
     style="display: none; position: fixed; left: 50%;">
    <div class="inner_text_tips">
        <div class="text_tips_content" id="pmsgContent"></div>
        <div class="text_tips_btns flexbox">
            <a id="btnConfirm" class="btn tips_submit_btn flexItem" href="javascript:closeMsgBox(0);">确定</a>
            <a id="btnCancel" class="btn tips_cancel_btn  flexItem" href="javascript:closeMsgBox(-1);">取消</a>
        </div>
    </div>
    <input type="hidden" value="" id="operationNeedHandle" />
</div>

<div class="simple_tips" id="loading_simple_div" style="display: none; position: fixed;">
    <div class="simple_tips_content text-center">
        <div id="loading" class="loading ma">
            <div><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span>
            </div>
        </div>
        <div class="simple_tips_text mt10">
            <p id="loading_text">加载中...</p>
        </div>
    </div>
</div>

<div class="overlay" id="overlayImage" style="display: none;"></div>
<div class="commentBtnBoxBg"></div>
<!--图片预览-->
<div class="img_preview" id="loading_image_div" style="display:none;height: 100%;">
    <div class="inner_img_preview">
        <img id="src_image" src="" onclick="hideImage();" alt="">
    </div>


    <div class="pcimg-preview-footer">
        <ul class="pcimg-operate-list clearfix">
            <li class="item"><i class="icon icon-prev" data-id="prev"></i><p class="tip">上一张</p></li>
            <li class="item"><i class="icon icon-zoomIn" data-id="zoomIn"></i><p class="tip">放大</p></li>
            <li class="item"><i class="icon icon-zoomOut" data-id="zoomOut"></i><p class="tip">放小</p></li>
            <li class="item disable"><i class="icon icon-resize" data-id="reset"></i><p class="tip">重置</p></li>
            <li class="item"><i class="icon icon-rotate" data-id="rotate"></i><p class="tip">旋转</p></li>
            <li class="item"><i class="icon icon-download" data-id="download"><a href="" download=""></a></i><p class="tip">下载</p></li>
            <li class="item"><i class="icon icon-next" data-id="next"></i><p class="tip">下一张</p></li>
        </ul>
        <div class="small-pcimg-list">
            <div class="small-pcimg-list-wrap">
                <ul class="small-pcimg-list-ul"> <li><img src="" alt=""></li> </ul>
            </div>
            <div class="small-pcimg-list-prev">
            </div>
            <div class="small-pcimg-list-next"></div>
        </div>
        <div class="ft-maskP"></div>
    </div>
</div>

<div class="text_tips" id="chooseMsg_div" style="display: none;position: fixed; left: 40%;">
    <div class="inner_text_tips">
        <div id="question1" class="question_detail" style="">
            <div class="inner_question_detail">
                <div class="ask_info" id="chooseMsgTital"></div>
                <div class="ask_info">
                    <div class="answer-list radio_btn">
                        <ul id="chooseMsgUl">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="text_tips_btns flexbox">
            <a id="btnConfirm" class="btn tips_submit_btn flexItem" href="javascript:closeChooseMsgBox(0);">确定</a>
            <a id="btnCancel" class="btn tips_cancel_btn  flexItem" href="javascript:closeChooseMsgBox(-1);">取消</a>
        </div>
    </div>
</div>