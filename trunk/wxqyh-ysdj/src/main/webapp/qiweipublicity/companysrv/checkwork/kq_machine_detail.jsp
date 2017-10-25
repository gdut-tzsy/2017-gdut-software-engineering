<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<%@include file="/jsp/common/dqdp_common.jsp" %>
<jsp:include page="/jsp/common/dqdp_vars.jsp">
    <jsp:param name="dict" value=""></jsp:param>
    <jsp:param name="permission" value=""></jsp:param>
    <jsp:param name="mustPermission" value=""></jsp:param>
</jsp:include>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="企微,企业微信,移动办公">
    <meta name="description" content="">
    <meta name="keywords" content="微信企业号,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,微信企业号第三方,企业号服务商">
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <link href="${baseURL}/themes/manager/companysrv/css/fw.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/order/shoppingCart.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <style type="text/css">
      html{min-width: 1200px;}
    </style>
</head>
<body>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">
            <span>企微云考勤机</span>
        </div>
    </div>
</div>
<div class="wrap-container FW">
    <div id="container" class="clearfix">
        <div class="place tleft fz14">

        </div>
        <div class="FW_content">
            <!-- 考勤机详情页 -->
            <input type="hidden" id="serviceId" value=""/>
            <div class="tab-main">
                <div class="kqMain">
                    <div class="kqImg">
                        <img id="machineImg" src="" width="364" height="323">
<%--                        <div class="popup">
                            <span><em></em></span>前50名下单立返200元京东购物卡
                        </div>--%>
                    </div>
                    <div class="kqdetail" style="margin-left: 50px;">
                        <dl>
                            <dt id="">
                                <div class="qw1Class none">
                            企微云考勤机 QW1<br>
                            <span>支持多点考勤，超大容量，指纹5000枚，人脸1000张</span>
                        </div>
                            <div class="qw2Class none">
                                企微云考勤机 QW2<br>
                                <span>支持多点考勤，超大容量，指纹5000枚，人脸500张</span>
                            </div>
                            </dt>
                            <dd class="t-item">
                                <label>联网：</label>
                                <span class="opt qw1Class" id="cable" style="display:none;" value="cable">网线</span>
                                <span class="opt qw2Class" id="wifi" style="display:none;" value="wifi">WIFI+网线</span>
                            </dd>
                            <dd>
                                <label>颜色：</label>银黑色</dd>
                            <dd>
                                <label>型号：</label><label class="qw1Class">QW1</label><label class="qw2Class">QW2</label>
                            </dd>
                            <dd>
                                <label>首发价：</label>
                                <label class="qw1Class">
                                    <span class="Amount">￥<b class="kqprice">699</b></span><%--<span class="scx">￥899</span>--%>
                                </label>
                                <label class="qw2Class">
                                    <span class="Amount">￥<b class="kqprice">999</b></span><%--<span class="scx">￥899</span>--%>
                                </label>
                            </dd>
                            <dd>
                            	<label style="vertical-align: bottom;">购买数量：</label>
                                <%@include file="../../../manager/companysrv/include/number.jsp" %>
                            </dd>
                            <dd style="margin-top: 20px;">
                                <label>
                                    <span class="fRed">*</span>
                                    CorpID：
                                </label>
                                <input type="text" class="w200" style="border: 1px solid #e4e4e4;padding: 6px;" id="corpId">
                                <div class="c999 ml110 pl5 fz14" id="corpIdTips"></div>
                                <div>
                                    <a href="javascript:showAcountBox();" class="fz12 ml10 mr10" style="text-decoration:underline;">登录获取CorpId</a><span class="c999 fz14">（也可从企业微信或企微管理后台中复制）</span>
                                </div>
                            </dd>
                            <dd>
                                <input type="button" class="gm-btn mt10" value="立即购买" onclick="goBuyCwmachine()" />
                            </dd>
                        </dl>
                        <p class="fz14">付款后3个工作日内发货</p>
                    </div>
                </div>

                <div class="kqparam">
                    <table width="1000">
                        <caption>详细参数</caption>
                        <tbody>
                        <tr>
                            <td width="300" class="tright">显示屏</td>
                            <td width="700" class="none qw1Class">2.8英寸TFT彩屏（320*240）按键</td>
                            <td width="700" class="none qw2Class">4.3英寸电阻彩屏（480*272）触摸屏</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹采集器</td>
                            <td>光学指纹采集器(采集面积18*15)</td>
                        </tr>
                        <tr>
                            <td class="tright">CPU</td>
                            <td class="none qw1Class">A33 处理器 4核，运行频率1.2GHz</td>
                            <td class="none qw2Class">ATMEL AT91SAM9635, 运行频率400MHz</td>
                        </tr>
                        <tr>
                            <td class="tright">识别角度</td>
                            <td>全角度识别</td>
                        </tr>
                        <tr>
                            <td class="tright">识别速度</td>
                            <td>≤1.0秒（满人脸指纹时）</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹（FRR/FAR）</td>
                            <td>0.00001/ 0.1(%)</td>
                        </tr>
                        <tr>
                            <td class="tright">认证方式</td>
                            <td>人脸、指纹</td>
                        </tr>
                        <tr>
                            <td class="tright">人脸存储容量</td>
                            <td class="none qw1Class">1000人</td>
                            <td class="none qw2Class">500人</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹存储容量</td>
                            <td>5000枚</td>
                        </tr>
                        <tr>
                            <td class="tright">验证记录/管理记录</td>
                            <td>300,000/10,000</td>
                        </tr>
                        <tr>
                            <td class="tright">通讯方式</td>
                            <td>TCP/IP</td>
                        </tr>
                        <tr>
                            <td class="tright">语言显示</td>
                            <td>中（简、繁）、英</td>
                        </tr>
                        <tr>
                            <td class="tright">语音提示</td>
                            <td>有（支持音量调节）</td>
                        </tr>
                        <tr>
                            <td class="tright">电源管理功能</td>
                            <td class="none qw1Class">屏保</td>
                            <td class="none qw2Class">休眠</td>
                        </tr>
                        <tr>
                            <td class="tright">工作电压/电流</td>
                            <td class="none qw1Class">DC 12V/600mA</td>
                            <td class="none qw2Class">DC 9V/2A</td>
                        </tr>
                        <tr>
                            <td class="tright">工作环境</td>
                            <td>温度：0℃~+45℃，湿度(RH)：20%~80%</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

</div>
<%@include file="../include/footer.jsp" %>
<%@include file="../include/accountMsgBox.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
<script>
    $(function(){
        $(".t-item .opt").each(function(){
            $(this).click(function(){
                $(this).parent().find('.opt').removeClass('on')
                $(this).addClass('on')
            });
        });
        positiveIpt(calPrice);
    });

    $(document).ready(function(){
        if("qw1"==GetQueryString("type")){
            $("#serviceId").val("cwmachine1");
            $("#machineImg").attr("src",baseURL+"/themes/manager/companysrv/images/img/kqj11.png?var=123");
            $("#wifi").removeClass("on");
            $("#cable").addClass("on");
            $(".qw1Class").show();
            $(".qw2Class").hide();
        }else if("qw2"==GetQueryString("type")){
            $("#serviceId").val("cwmachine2");
            $("#machineImg").attr("src",baseURL+"/themes/manager/companysrv/images/img/kqj21.png?var=123");
            $("#wifi").addClass("on");
            $("#cable").removeClass("on");
            $(".qw1Class").hide();
            $(".qw2Class").show();
        }
        initOrg();
     });

    //初始化机构名称和CorpId
    function initOrg(){
        wxqyhConfig.ready(function(){
            if(wxqyhConfig.orgConfigInfo){
                if(wxqyhConfig.orgConfigInfo.corpId){
                    $("#corpId").val(wxqyhConfig.orgConfigInfo.corpId);
                }
                if(wxqyhConfig.orgConfigInfo.orgName){
                    $("#corpIdTips").html(wxqyhConfig.orgConfigInfo.orgName);
                }
            }
        });
    }

    function calPrice(){

    }

    /**
     * 购买考勤机
     */
    function goBuyCwmachine(){
        var serviceId=$("#serviceId").val();
        var buyNum=$("#buyNum").val();
        var corpId=$("#corpId").val();
        if(""==corpId){
            _alert("提示", "请输入corpId");
            return;
        }

        //serviceId="cwmachine4"; //测试数据

        var priceCode="";
        if(serviceId=="cwmachine1"){
            priceCode="cwmachine1_cable";
        }else if(serviceId=="cwmachine2"){
            priceCode="cwmachine2_wifi";
        }else if(serviceId=="cwmachine3"){
            priceCode="cwmachine3";
        }else if(serviceId=="cwmachine4"){
            priceCode="cwmachine4";
        }else if(serviceId=="cwmachine5"){
            priceCode="cwmachine5";
        }else{
            return;
        }


        //设置购买的商品
        var myServices = []; //商品数组

        //购买容量
        var vipPersonService = {"serviceId":serviceId,"priceCode":priceCode,"buyNum":buyNum};
        myServices.push(vipPersonService);
        //设置到购物车中
        var wxqyhShoopingCart = {
            "services":myServices
        }
        addNewShoppingCart(wxqyhShoopingCart);
        document.location.href=baseURL+"/qiweipublicity/companysrv/order/order_mulit_confirm.jsp?corpId="+corpId;
    }

    function showAcountBox(){
        $("#accountBox").attr("okCallback","searchCorpIdByAccountOKCallback");
        $("#overlayDiv").show();
        $("#accountBox").show();
    }
    function searchCorpIdByAccountOKCallback(corpId){
        $("#corpId").val(corpId);
        getOrgInfo();
    }

    //获取corpId等级
    function getOrgInfo(callback){
        var corpId=$("#corpId").val();
        $.ajax({
            type:"POST",
            url: baseURL+"/accountmgr/accountOpenAction!getOrgInfoByCorpId.action",
            data:{
                "corpId":corpId
            },
            cache:false,
            dataType: "json",
            success: function(result){
                if ("0" == result.code) {
                    $("#corpIdTips").html(result.data.orgName);
                }else{
                    $("#corpIdTips").html("<span>!</span>"+result.desc);
                }
                $("#corpIdTips").show();
            },
            error:function(){
                _alert("","网络错误",1);
            }
        });
    }
</script>