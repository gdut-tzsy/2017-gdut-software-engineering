<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>活动报名</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>

    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <%-- 百度统计 --%>
    <script type="text/javascript">
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?6abcc5eeee320072f7a9ed10e79be5c1";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
    <style>
        /*放在本页*/
        /*@media screen and (min-width: 800px){
            .wrap{
                width: 600px;
                border: 1px #ddd solid;
                margin: 20px auto 0;
                background: #fff;
            }
            #openFooter{
                width: 600px;
                margin: 0 auto 0;
            }
        }*/
    </style>
</head>

<body style="background-color: #fff; ">
    <div class="wrap" style="display: none;">
        <div id="main" class="wrap_inner">
            <div class="applyContent" id="open_detail">
                <div class="applyTitle">
                    <h3 id="title" ></h3>
                    <span>活动时间：<span id="activityStart"></span> 至 <span id="activityStop"></span></span>
                </div>
                <div class="applyBox">
                    <div class="applyItem flexbox">
                        <span>微信名</span>
                        <div id="nickname" class="flexItem c999 fz15"></div>
                    </div>
                    <div class="applyItem mb10 flexbox">
                        <span><i class="mustInput">*</i>姓名</span>
                        <div class="flexItem">
                            <input type="text" id="name" class="applyInput" placeholder="请输入姓名">
                        </div>
                    </div>
                    <div class="applyItem mb10 flexbox">
                        <span><i class="mustInput">*</i>手机</span>
                        <div class="flexItem">
                            <input type="text" id="mobile" class="applyInput" placeholder="请输入手机号码">
                        </div>
                    </div>
                    <div class="applyItem mb10 flexbox">
                        <span>邮箱</span>
                        <div class="flexItem">
                            <input type="text" id="email" class="applyInput" placeholder="请输入电子邮件地址">
                        </div>
                    </div>
                    <div class="applyItem flexbox">
                        <span>备注</span>
                        <div class="flexItem">
                            <textarea id="remind" class="applyText" style="height: 100px;"></textarea>
                        </div>
                    </div>
                    <div class="form_btns" style="padding: 0;margin-top: 15px;">
                        <div class="inner_form_btns">
                            <div class="fbtns flexbox">
                                <a class="btn flexItem" href="javascript:registry();" style="border-radius: 2px">提交报名</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="openFooter">
                    <%@include file="/open/include/footer.jsp"%>
                </div>
            </div>
        </div>
    </div>
</body>
<%@include file="/open/include/showMsg.jsp"%>
</html>
<script type="text/javascript">
    var isAnonymous="0";
    var isMonkey="0";
    var activityLock=true;
    function footerPosition(){
        var win_height = $(window).height();
        var h_open_detail = $("#open_detail").height();
        $("#main").height(win_height);
        if (h_open_detail > win_height) {
            $("#openFooter").css({
                "position": "relative"
            });
        } else {

            $("#openFooter").css({
                "position": "absolute",
                "bottom": "0"
            });
        }
    }
    $(document).ready(function(){
        showLoading();
        showFooter(true);
        $.ajax({
            type:'post',
            url:'${baseURL}/open/activity/openActivityAction!getOpenUserId.action',
            dataType:'json',
            data:{
                id:'${param.id}',
                code:'${param.code}'
            },
            success:function(result){
                if("0"==result.code){
                    var hasRegistry=result.data.hasRegistry;
                    if(hasRegistry){//已报名跳转到凭证页面
                        window.location.href="${openURL}/open/activity/success.jsp?id=${param.id}";
                    }else{
                        //加载活动信息
                        $(".wrap").show();
                        $("#title").html(result.data.activity.title);
                        $("#activityStart").html(result.data.activity.activityStart);
                        $("#activityStop").html(result.data.activity.activityStop);
                        $("#nickname").html(result.data.nickname);
                        $("#name").val(result.data.nickname.replaceAll(/\?{4}/, ""));
                        hideLoading();
                    }
                }else{
                    _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                    hideLoading();
                }
                footerPosition();
            },
            error:function(){
                _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
                hideLoading();
            }
        });
    });

    //活动报名
    function registry(){
        var mobile=$("#mobile").val().trim();
        var remind=$("#remind").val();
        var realname=$("#name").val();
        var email=$("#email").val().trim();
        if(""==$("#name").val()){
            _alert("提示","姓名为必填信息，请完善后提交","确认",function(){restoreSubmit();});
            return;
        }
        if(!(/^\d{5,15}$/).test($("#mobile").val())){
            _alert("提示","手机号码未填写或格式有误，请重新输入","确认",function(){restoreSubmit();});
            return;
        }
        if($("#email").val()!="" && !(/^\w+([-+.]\w+)*@\w+([-.]\w+)*.\w+([-.]\w+)*$/).test($("#email").val())){
            _alert("提示","邮箱格式有误，请重新输入","确认",function(){restoreSubmit();});
            return;
        }
        if(activityLock){
            activityLock=false;
            showLoading();
            $.ajax({
                type:'post',
                url:'${baseURL}/open/activity/openActivityAction!addActivityRegistry.action',
                data:{
                    id:'${param.id}',
                    realname:realname,
                    mobile:mobile,
                    email:email,
                    content:remind,
                    isAnonymous:isAnonymous,
                    isMoney:isMonkey
                },
                dataType:'json',
                success:function(result){
                    if("0"==result.code){
                        activityLock=true;
                        window.location.href="${openURL}/open/activity/success.jsp?id=${param.id}";
                        hideLoading();
                    }else{
                        _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                        activityLock=true;
                        hideLoading();
                    }
                },
                error:function(){
                    _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
                    activityLock=true;
                    hideLoading();
                }
            });
        }
    }
</script>