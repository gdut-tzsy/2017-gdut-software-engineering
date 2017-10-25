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
                        <img id="machineImg" src="" >
                    </div>
                    <div class="kqdetail" style="margin-left: 50px;">
                        <dl>
                            <dt id="">
                            <div class="qw1Class" id="productName">

                            </div>
                            </dt>
                            <dd class="t-item">
                                <label>联网：</label>
                                <span class="opt" id="cable" value="cable" price="549">网线</span>
                            </dd>
<%--                            <dd>
                                <label>颜色：</label>银黑色</dd>--%>
                            <dd>
                                <label>型号：</label><label class="qw1Class">K1</label>
                            </dd>
                            <dd>
                                <label>首发价：</label>
                                <label class="qw1Class">
                                    <span class="Amount">￥<b class="kqprice" id="kqprice">0</b></span>
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
                        <caption>技术参数</caption>
                        <tbody>
                        <tr>
                            <td width="300" class="tright">处理器</td>
                            <td width="700" class="none qw1Class">ARM9 400MHz</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹采集器</td>
                            <td>CMOS 光学指纹采集器，耐磨，安全</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹采集面积</td>
                            <td class="none qw1Class">20.5*18MM</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹采集角度</td>
                            <td>360°</td>
                        </tr>
                        <tr>
                            <td class="tright">用户容量</td>
                            <td>2000</td>
                        </tr>
                        <tr>
                            <td class="tright">指纹容量</td>
                            <td>2000</td>
                        </tr>
                        <tr>
                            <td class="tright">密码容量</td>
                            <td>2000</td>
                        </tr>
                        <tr>
                            <td class="tright">ID卡容量</td>
                            <td class="none qw1Class">2000（选配）</td>
                        </tr>
                        <tr>
                            <td class="tright">记录容量</td>
                            <td>16万</td>
                        </tr>
                        <tr>
                            <td class="tright">后背电池 </td>
                            <td>≥2小时（选配）</td>
                        </tr>
                        <tr>
                            <td class="tright">验证速度</td>
                            <td>≤0.5秒</td>
                        </tr>
                        <tr>
                            <td class="tright">拒登率</td>
                            <td>≤1%</td>
                        </tr>
                        <tr>
                            <td class="tright">误判率</td>
                            <td>≤0.0001%</td>
                        </tr>
                        <tr>
                            <td class="tright">按  键</td>
                            <td class="none qw1Class">4*3按键+4个功能键</td>
                        </tr>
                        <tr>
                            <td class="tright">显示方式</td>
                            <td class="none qw1Class">2.8寸TFT彩屏</td>
                        </tr>
                        <tr>
                            <td class="tright">报表功能</td>
                            <td>支持自动生成报表</td>
                        </tr>
                        <tr>
                            <td class="tright">联网功能</td>
                            <td>USB 2.0; TCP/IP; U盘</td>
                        </tr>
                        <tr>
                            <td class="tright">支持语言</td>
                            <td>中文简体</td>
                        </tr>
                        <tr>
                            <td class="tright">工作电压</td>
                            <td>DC5V1A</td>
                        </tr>
                        <tr>
                            <td class="tright">工作电流</td>
                            <td>240mA</td>
                        </tr>
                        <tr>
                            <td class="tright">外观尺寸</td>
                            <td>195*136*47MM</td>
                        </tr>
                        <tr>
                            <td class="tright">装箱规格</td>
                            <td>10台/箱</td>
                        </tr>
                        <tr>
                            <td class="tright">工作温度</td>
                            <td>0℃--45℃</td>
                        </tr>
                        <tr>
                            <td class="tright">工作湿度</td>
                            <td>20%-80%RH</td>
                        </tr>
                        <tr>
                            <td class="tright">安装方式</td>
                            <td>挂装</td>
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
                if($(this).attr("value")=="cable") {
                    $("#serviceId").val("cwmachine5");
                    $("#kqprice").html($(this).attr("price"));
                    $("#productName").html("企微云考勤机 K1 <br/><span>支持指纹识别，2000条指纹容量</span>");
                }
            });
        });
        positiveIpt(calPrice);
    });

    $(document).ready(function(){
        $("#machineImg").attr("src",baseURL+"/themes/manager/companysrv/images/img/qwK1-3_18.png?var=1234");
        $("#cable").click();
        $(".qw1Class").show();
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

    function getServiceId(){
        return $("#serviceId").val();
    }

    /**
     * 购买考勤机
     */
    function goBuyCwmachine(){
        var serviceId=getServiceId();

        var buyNum=$("#buyNum").val();
        var corpId=$("#corpId").val();
        if(""==corpId){
            _alert("提示", "请输入corpId");
            return;
        }

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