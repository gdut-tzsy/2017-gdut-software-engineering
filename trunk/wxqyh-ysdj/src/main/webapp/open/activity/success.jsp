<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>活动凭证</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <link rel="stylesheet" href="${baseURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
    <script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/emojify.min.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="${baseURL}/jsp/wap/js/ku-jquery.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
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

<body style="background-color: #424957">
    <div class="wrap">
        <div id="main" class="wrap_inner" style="display:none;">
            <div class="apply-prove-body" id="open_detail">
                <div class="apply-prove-head">
                    <div class="apply-prove-head-box"><i class="icon_sure"></i>已报名</div>
                    <div class="apply-prove-head-ft"></div>
                </div>
                <div class="apply-prove-content">
                    <div class="apply-prove-title">
                        <h3 id="title"></h3>
                        <p>主办方：<span id="managerName"></span></p>
                        <p>活动时间：<span id="activityStart"></span>&nbsp;<span id="days">还有0天</span></p>
                    </div>
                    <div class="separate-box">
                        <div class="separate-left"></div>
                        <div class="separate-line"></div>
                        <div class="separate-right"></div>
                    </div>
                    <div class="apply-prove-box">
                        <div class="apply-prove-item flexbox">
                            <span>参加人</span>
                            <p id="personName" class="flexItem" style="color: #32cd32;font-size: 18px;"></p>
                        </div>
                        <div class="apply-prove-item flexbox">
                            <span>微信名</span>
                            <p id="nickname" class="flexItem"></p>
                        </div>
                        <div class="apply-prove-item flexbox">
                            <span>邮箱</span>
                            <p id="email" class="flexItem"></p>
                        </div>
                        <div class="apply-prove-item flexbox">
                            <span>手机</span>
                            <p id="mobile" class="flexItem"></p>
                        </div>
                        <div class="apply-prove-item flexbox">
                            <span>备注</span>
                            <p id="remind" class="flexItem"></p>
                        </div>
                    </div>
                    <div class="separate-line" style="margin: 0 15px;"></div>
                    <div class="form_btns" style="padding: 20px 15px;">
                        <div class="inner_form_btns">
                            <div class="fbtns flexbox">
                                <a class="btn-basic btn-white flexItem" href="javascript:cancelRegistry();">取消报名</a>
                                <a class="btn-basic btn-white ml10 flexItem" href="javascript:save();">保存凭证</a>
                            </div>
                            <div class="fbtns flexbox mt10">
                                <a class="btn flexItem" href="javascript:detail();">活动详情</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="openFooter">
                    <%@include file="/open/include/footer.jsp"%>
                </div>
            </div>
        </div>
        <div class="layer-collect">
            <div class="bg-layer-collect"></div>
        </div>
    </div>
</body>
<%@include file="/open/include/showMsg.jsp"%>
</html>
<script type="text/javascript">
    var code="${param.code}";
    function footerPosition(){
        var win_height = $(window).height();
        var h_open_detail = $("#open_detail").height();
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
        showFooter(true);
        $.ajax({
            type:'post',
            url:'${baseURL}/open/activity/openActivityAction!getRegistryName.action',
            data:{
                id:'${param.id}',
                code:code
            },
            dataType:'json',
            success:function(result){
                if("1996"==result.code){
                    document.location.href=result.data.url;
                    return;
                }
                if("0"==result.code){
                    $("#main").show();
                    var info=result.data;

                    //活动信息
                    var activityStart=info.activity.activityStart;
                    $("#title").html(info.activity.title);
                    $("#activityStart").html(activityStart.substring(5,16));
                    $("#managerName").html(info.activity.personName);

                    //计算距离活动开始剩余天数
                    var now =new Date().getTime();
                    var start =new Date(activityStart.toString().replaceAll("-","/")).getTime();
                    var days=parseInt((start-now)/(1000*60*60*24));
                    $("#days").html(days<0?"":"还有"+(days+1)+"天");

                    //报名信息
                    $("#nickname").html(info.registryName.personName);
                    $("#personName").html(info.registryName.realname);
                    var mobile=info.registryName.mobile;
                    $("#mobile").html(mobile==""?"无":mobile);
                    var remark=info.registryName.remark;
                    $("#remind").html(remark==""?"无":remark);
                    var email=info.registryName.email;
                    $("#email").html(email==""?"无":email);

                    //设置分享信息
                    var title="【活动凭证】"+info.activity.title;
                    var img="${compressURL}"+info.activity.coverImage;
                    if(info.activity.coverImage==""){
                        img = info.logo;
                    }
                    var summary=info.activity.summary;
                    var shareUrl=window.location.href;
                    if(shareUrl.indexOf("&")>0){
                        var str=shareUrl.split("&");
                        shareUrl=str[0];
                    }
                    setDataForWeixinValue(title,img,summary,shareUrl);
                    footerPosition();
                }else{
                    _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                }
            },
            error:function(){
                _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
            }
        });

        $(".layer-collect").click(function(){
            $(".layer-collect").hide();
        });
    });

    //取消报名
    function cancelRegistry(){
        _alert("提示","是否取消报名","取消|确认",{'ok':function(){
            $.ajax({
                type:"post",
                url:"${baseURL}/open/activity/openActivityAction!cancelActivityRegistry.action",
                data:{
                    id:"${param.id}",
                },
                dataType:"json",
                success:function(result){
                    if("0"==result.code){
                        _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                        window.location.href="${openURL}/open/activity/detail2.jsp?id=${param.id}";
                    }else{
                        _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                    }
                },
                error:function(){
                    _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
                }
            })},'fail':function(){restoreSubmit();}}
        );
    }

    function detail(){
        window.location.href="${openURL}/open/activity/detail2.jsp?id=${param.id}";
    }

    function save(){
        $(".layer-collect").show();
    }
</script>