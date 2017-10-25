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
</head>

<body style="background-color: #fff; ">
    <div class="wrap">
        <div id="main" class="wrap_inner">
            <div class="applyBox">
                <div class="applyTitle1">
                    <h3>活动凭证</h3>
                </div>
                <div class="applyProveItem mt10 flexbox">
                    <span class="c888">微信名</span>
                    <p id="nickname" class="flexItem c333"></p>
                </div>
                <div class="applyProveItem flexbox">
                    <span class="c888">姓名</span>
                    <p id="personName" class="flexItem c333"></p>
                </div>
                <div class="applyProveItem flexbox">
                    <span class="c888">手机</span>
                    <p id="mobile" class="flexItem c333"></p>
                </div>
                <div class="applyProveItem flexbox">
                    <span class="c888">备注</span>
                    <p id="remark" class="flexItem c333"></p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function(){
        $.ajax({
            type:'post',
            url:'${baseURL}/open/activity/openActivityAction!getRegistryName.action',
            data:{
                id:'${param.id}',
                openId:'${param.openId}'
            },
            dataType:'json',
            success:function(result){
                if("0"==result.code){
                    //报名信息
                    $("#nickname").html(result.data.registryName.personName);
                    $("#personName").html(result.data.registryName.realname);
                    var mobile=result.data.registryName.mobile;
                    if(""==mobile){
                        mobile="无";
                    }
                    $("#mobile").html(mobile);
                    var remark=result.data.registryName.remark;
                    if(""==remark){
                        remark="无";
                    }
                    $("#remark").html(remark);

                }else{
                    _alert("提示",result.desc,"确认",function(){restoreSubmit();});
                }
            },
            error:function(){
                _alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
            }
        });
    });
</script>