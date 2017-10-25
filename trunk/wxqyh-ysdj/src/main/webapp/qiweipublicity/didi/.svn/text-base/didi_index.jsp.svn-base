<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="cn.com.do1.dqdp.core.ConfigMgr" %>
<%@ page import="cn.com.do1.common.util.DateUtil" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<%
    pageContext.setAttribute("moneyForDidi", ConfigMgr.getIntCfg("wxqyhtrade","moneyForDidi",500));
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <title><%=qwManagerTitle%></title>

    <link href="${baseURL}/themes/manager/companysrv/css/style_company.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>

    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <style type="text/css">
      html{min-width: 1200px;}
    </style>
</head>
<body>

<section class="page-section banner" data-anchor="banner">
    <div class="w1200">
        <div class="header header_before">
            <div class="navbar w1200">
                    <span title="企业微信应用，首选企微云平台" class="logo fl">
                    <img src="../../themes/manager/companysrv/images/logo.png" alt="" style="width:125px;height:35px;">
                    </span>
                <a title="企业微信应用，首选企微云平台" href="#" class="logo fl fz20">企业叫车</a>
                <ul class="nav fr">
                    <li><a href="didi_index.jsp" class="active">首页</a></li>
                    <li><a href="../../manager/companysrv/order_list.jsp" target="_blank">用车订单</a></li>
                    <li><a href="#sectionFive">用车攻略</a></li>
                    <li><a href="help_1.jsp">帮助</a></li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <div class="header header_after">
            <div class="navbar w1200">
                    <span title="企业微信应用，首选企微云平台" class="logo fl">
                    <img src="../../themes/manager/companysrv/images/logo.png" alt="" style="width:125px;height:35px;">
                    </span>
                <a title="企业微信应用，首选企微云平台" href="#" class="logo fl fz20">企业叫车</a>
                <ul class="nav fr">
                    <li><a href="didi_index.jsp" class="active">首页</a></li>
                    <li><a href="../../manager/companysrv/order_list.jsp" target="_blank">用车订单</a></li>
                    <li><a href="#sectionFive">用车攻略</a></li>
                    <li><a href="help_1.jsp">帮助</a></li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
        <div class="one_1">
            <img src="../../themes/manager/companysrv/images/img_1.png?var=2" class="img_1">
            <img src="../../themes/manager/companysrv/images/img_2.png?ver=3" class="img_2">
            <p>
                <span id="yueSpan" style="display:none;">您的企业账户可用余额：<i id="amountAvalible"></i>，</span>
                账户可用余额须大于${moneyForDidi}元才能使用用车服务
            </p>
            <a href="../../manager/companysrv/charge.jsp" target="_blank" class="btn_Recharge fz20">立即充值</a>
        </div>
    </div>
</section>
<section class="page-section step" data-anchor="step">
        <div class="w1200">
            <div class="tit">
                <h1>用企微企业叫车，领1G储存空间</h1>
            </div>
            <div class="setpImg">
                <a href="../../manager/companysrv/charge.jsp" target="_blank"></a>
            </div>
        </div>
    </section>
<section class="page-section one" data-anchor="one">
    <div class="w1200">
        <div class="tit">
            <h1>企业用车  费用更节省</h1>
            <p>比企业买车、养车、租车费用更节省，为企业节省更多出行成本</p>
        </div>
    </div>
</section>
<section class="page-section two" data-anchor="two">
    <div class="w1200">
        <div class="tit">
            <h1>员工商务出行  企业账户支付</h1>
            <p>被授权的员工可在企业微信的 "车辆管理" 应用中直接呼叫企业叫车快车、专车, 行程结束后自动从企业账<br>户中扣款无须个人垫付费用, 省去繁杂的交通报销环节，大大提高工作效率</p>
        </div>
    </div>
</section>
<section class="page-section three" data-anchor="three">
    <div class="w1200">
        <div class="tit">
            <h1>用车权限管理  确保用车合规</h1>
            <p>您可在企微管理平台中的车辆管理中给员工设置用车权限, 同时也可以查看到每一次行程的详细用车<br>信息, 帮助企业有效管控企业出行成本</p>
        </div>
    </div>
</section>
<section class="page-section four" data-anchor="four">
    <div class="w1200">
        <div class="tit">
            <h1>企业微信一键叫车  叫车更高效</h1>
            <p>快车、专车、加班叫车、商务出行叫车、企业接待客户叫车, 企业叫车可以满足不同企业的用车需求和用车<br>场景，你只需选择想要的用车方式, 输入简单信息即可一键叫车</p>
        </div>
    </div>
</section>
<section class="page-section five" data-anchor="five" id="sectionFive" name="sectionFive"></section>
<%@include file="footer.jsp" %>
<div class="rightToolBar">
    <div class="didiQQ" id="didiQQ">
        加群<br>咨询
    </div>
</div>
<script>
    $(window).scroll(function (){
        var st = $(this).scrollTop();
        if(st>77){
            $('body').addClass('scrolled');
        }else {
            $('body').removeClass('scrolled');
        }
    });
</script>

</body>
</html>

<script>
    $(document).ready(function(){
        loadAccount();
        $("#didiQQ").on("click",function(){
            window.open("http://jq.qq.com/?_wv=1027&k=2AnfGIb");
        });
    });

    function loadAccount(){
        $.ajax({
            type:"POST",
            url: baseURL+"/accountmgr/accountmgrAction!viewServiceAmountAvalible.action",
            data:{
                "serviceId":"didi"
            },
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    var amountAvalible = result.data.amountAvalible;
                    if(result.data.account){
                        $("#yueSpan").show();
                        $("#amountAvalible").html(formatMoney(amountAvalible,2)+"元");
                    }else{
                        //没有同意企业服务
                    }
                }else{
                    //_alert("错误提示",result.desc);
                }
            },
            error:function(){
                //_alert("","网络错误",1);
            }
        });
    }
</script>