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
    <meta name="description" content="VIP尊享分支流程、大屏幕3D签到、抽奖、考试、排班、人员邀请等多项功能特权">
    <meta name="keywords" content="微信企业号,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,微信企业号第三方,企业号服务商">
    <title>银卡VIP限时特惠，二十余项功能特权全面升级|企微云平台</title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_index.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/qwCountInit.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
    <script src="${baseURL}/js/wx/wxAPi.js?ver=<%=jsVer%>"></script>
    <style type="text/css">
      html{min-width: 1300px;}
    </style>
</head>
<body>
<!-- 头部 -->
<div id="header" style="position: fixed;top:0;">
    <div class="header-nav clearfix">
        <div class="header-left">
            <img src="../../../themes/manager/companysrv/images/img/logo.png">

            <span>银卡VIP</span>
        </div>
        <div class="header-right">
            <input type="button" value="申请体验" class="btn topBtn" onclick="goTasteVip()" id="btn_headTasteVip"/>
            <input type="button" value="立即开通VIP" class="btn orangeBtn topBtnOther" onclick="goBuyVip()" id="btn_headBuyVip"/>
        </div>
    </div>
</div>
<div class="banner vip-index-banner" style="margin-top: 70px">
    <div class="banner-main">
        <div class="banner-timer" style="display: none"><i class="mr10">//////////////////</i>倒计时<span id="days"></span>天<span id="hours"></span>小时<span id="minutes"></span>分<i class="ml10">//////////////////</i></div>
        <div class="banner-info">
            <a href="javascript:goTasteVip();" class="link-b" style="display:none;" id="btn_bannerTasteVip">申请免费体验</a>
            <a href="javascript:goBuyVip();" class="buy-btn" style="display:none;" id="btn_bannerBuyVip">立即开通</a>
        </div>
    </div>
</div>
<!-- 主体部分 -->
<div class="nianhui">
    <div class="nianhui_main">
        <div class="nianhui_content pb100">
            <div class="introduceItem center">
                <img src="../../../themes/manager/companysrv/images/content_1.png" alt="40多项特权" class=" m-center">
                <a target="_blank" href="https://qy.do1.com.cn/qwy/qiweipublicity/companysrv/vip/vip_single_buy.jsp?tabCode=3" class="moreVipBtn">查看更多特权</a>
            </div>
            <div id="nav_gn" class="nianhui_mid clearfix">
                <div class="fl nianhui_mid_info">
                    <h4 class="mb15">3D签到墙</h4>
                    <p class="c333 fz18">炫酷的3D年会签到墙，可以组成各种星球、<br>万花筒、自定义文本等图多种图案。</p>
                    <ul class="nianhui_ul mt15">
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui_ul_img01.png" alt="" />
                            <p>星球式</p>
                        </li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui_ul_img02.png" alt="" />
                            <p>放射式</p>
                        </li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui_ul_img03.png" alt="" />
                            <p>螺旋式</p>
                        </li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui_ul_img04.png" alt="" />
                            <p>自定义文本</p>
                        </li>
                    </ul>
                </div>
                <div class="fr tcenter">
                    <p><img src="../../../themes/manager/companysrv/images/img/vip-3d-show.gif" width="520" height="352" /></p>
                    <a href="${openURL}/open/3d/3dcheckin.html?vip" target="_blank" class="btn-nianhui mt25">立即体验</a>
                </div>
            </div>
            <div class="introduceItem" style="z-index: 10;">
                <h3>互动大屏幕</h3>
                <p class="fz18 c333 mb30" style="text-align:center">大型活动会议签到、抽奖、互动整体解决方案</p>
                <div class="vip-carousel">
                    <ul>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui-vip-lb1.png" alt="" /></li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui-vip-lb2.png" alt="" /></li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui-vip-lb3.png" alt="" /></li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui-vip-lb4.png" alt="" /></li>
                        <li><img src="../../../themes/manager/companysrv/images/img/nianhui-vip-lb5.png" alt="" /></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="content"  class="content">
    <!-- VIP精彩特权 -->
    <div class="introduceItem">
        <h3>固定流程与分支流程</h3>
        <p class="fz18 c999 mb30" style="text-align:center">让审批/报销/协作流程化，内外表单均支持根据条件自动流转</p>
        <img src="../../../themes/manager/companysrv/images/img/vip-index-lc.png" alt="" class="m-center" />
    </div>
    <div class="introduceItem">
        <h3>考勤排班</h3>
        <p class="fz18 c999 mb30" style="text-align:center">一键搞定员工排班，无须重复配置考勤规则</p>
        <img src="../../../themes/manager/companysrv/images/img/vip-index-kq.png" alt="" class="m-center" />
    </div>
    <div class="introduceItem">
        <h3>考试评测</h3>
        <p class="fz18 c999 mb30" style="text-align:center">在微信上随时组织评测，搞定培训考试</p>
        <img src="../../../themes/manager/companysrv/images/img/vip-index-ks.png" alt="" class="m-center" />
    </div>
    <div class="introduceItem">
        <h3>数据统计可视化图表</h3>
        <p class="fz18 c999 mb30" style="text-align:center">提供多种数据分析图表，让你的数据更有说服力</p>
        <img src="../../../themes/manager/companysrv/images/img/vip-index-tb.png" alt="" class="m-center" />
    </div>
    <div class="introduceItem">
        <h3>关怀红包</h3>
        <p class="fz18 c999 mb30" style="text-align:center">在生日祝福、周年祝福为员工送上一点额外的关怀福利</p>
        <img src="../../../themes/manager/companysrv/images/img/vip-index-hb.png" alt="" class="m-center" />
    </div>
    <!-- 手写签名 -->
    <div class="introduceItem clearfix mt65">
        <div class="fl introduceItem-info ml150 mt240">
            <h4>手写签名</h4>
            <p>自由流程固定流程均支持手写签名，让流程更规范，保障审批效力</p>
        </div>
        <div class="fr mr100">
            <img src="../../../themes/manager/companysrv/images/img/vip-index04.jpg" alt="" class="" />
        </div>
    </div>
    <!-- 人员邀请 -->
    <div class="introduceItem clearfix mt150">
        <div class="fl ml100">
            <img src="../../../themes/manager/companysrv/images/img/vip-index05.jpg" alt="" class="" />
        </div>
        <div class="fr introduceItem-info mr150 mt200">
            <h4>人员邀请</h4>
            <p>让你的团队及客户可以自主加入到企业微信中，简化企业微信关注流程，使用更方便</p>
        </div>
    </div>
    <!-- 额外储存空间 -->
    <div id="nav_fw" class="introduceItem clearfix" style="padding-top: 65px;">
        <div class="fl introduceItem-info ml150 mt130">
            <h4>额外储存空间</h4>
            <p>VIP用户享有更大的储存空间，满足您各类数据文档的存档管理需要</p>
        </div>
        <div class="fr mr100">
            <img src="../../../themes/manager/companysrv/images/img/vip-index06.png" alt="" class="" />
        </div>
    </div>
    <!-- 7*12小时技术支持 -->
    <div class="introduceItem clearfix mt150">
        <div class="fl">
            <img src="../../../themes/manager/companysrv/images/img/vip-index07.png" alt="" class="" />
        </div>
        <div class="fr introduceItem-info mr150 mt130">
            <h4>7*12小时技术支持</h4>
            <p>提供7*12小时值班的多渠道（电话、QQ、微信、邮件）技术支持服务，VIP更有专属服务微信群</p>
        </div>
    </div>
</div>
<div class="security">
    <h4>八大安全保障 用得放心</h4>
    <ul class="clearfix">
        <li style="margin-left: 0;">
            <i class="security-ico-1"></i>
            <div class="">ISO27001信息安全认证 <a href="http://wbg.do1.com.cn/ask/shujuanquan/2016/0318/663.html" target="_blank" class=""> 查看证书</a></div>
        </li>
        <li>
            <i class="security-ico-2"></i>
            <div class="">核心数据加密储存</div>
        </li>
        <li>
            <i class="security-ico-3"></i>
            <div class="">数据文件隔离储存</div>
        </li>
        <li>
            <i class="security-ico-4"></i>
            <div class="">多地机房容灾备份</div>
        </li>
        <li style="margin-left: 0;">
            <i class="security-ico-5"></i>
            <div class="">专业运维团队特聘安全专家</div>
        </li>
        <li>
            <i class="security-ico-6"></i>
            <div class="">入侵检测系统入侵防御系统</div>
        </li>
        <li>
            <i class="security-ico-7"></i>
            <div class="">系统访问口令识别机制</div>
        </li>
        <li>
            <i class="security-ico-8"></i>
            <div class="">完善的用户生命周期管理</div>
        </li>
    </ul>
</div>
<div id="nav_buy" class="content">
    <!-- 价格方案 -->
    <div class="introduceItem">
        <h3>价格方案</h3>
        <p class="fz16 c999 mb30" style="text-align:center">活动期间购买VIP或VIP套餐，领取最多500元京东购物卡</p>
        <ul class="vipPrice clearfix">
            <li class="">
                <h5 class="">标准VIP服务</h5>
                <ul class="vipPrice-list">
                    <li class="ico-g">一年期银卡VIP服务</li>
                    <li class="ico-g">4G企微云储存空间</li>
                    <li class="ico-g">新功能优先使用权</li>
                </ul>
                <div class="price clearfix">
                    <div class="price-l">
                        <span class="">￥</span><span class="fz48">2888</span><span class="">起</span>
                    </div>
                    <div class="price-r">
                        <span class="price-r-t">省2511</span>
                        <span class="price-r-b">原价5399</span>
                    </div>
                </div>
                <a href="javascript:goBuyVip('silver_class');" class="vipPrice-btn">立即购买</a>
            </li>
            <li class="">
                <h5 class="">一对一VIP套餐</h5>
                <ul class="vipPrice-list">
                    <li class="ico-g">一年期银卡VIP服务</li>
                    <li class="ico-g">一年期一对一技术服务</li>
                    <li class="ico-g">20G企微云储存空间</li>
                    <li class="ico-g">新功能优先使用权</li>
                    <li class="ico-l">限量送：300元京东购物卡</li>
                    <li class="ico-l">限量送：699元QW1考勤机</li>
                </ul>
                <div class="price clearfix">
                    <div class="price-l">
                        <span class="">￥</span><span class="fz48">8888</span><span class="">起</span>
                    </div>
                    <div class="price-r">
                        <span class="price-r-t">省4110</span>
                        <span class="price-r-b">原价12998</span>
                    </div>
                </div>
                <a href="javascript:goBuyVip('silver_onebyone');" class="vipPrice-btn">立即购买</a>
            </li>
            <li class="">
                <h5 class="">多地办公VIP套餐</h5>
                <ul class="vipPrice-list">
                    <li class="ico-g">一年期银卡VIP服务</li>
                    <li class="ico-g">一年期一对一技术服务</li>
                    <li class="ico-g">一套视频会议直播设备</li>
                    <li class="ico-g">50G企微云储存空间</li>
                    <li class="ico-g">新功能优先使用权</li>
                    <li class="ico-l">限量送：500元京东购物卡</li>
                </ul>
                <div class="price clearfix">
                    <div class="price-l">
                        <span class="">￥</span><span class="fz48">16888</span><span class="">起</span>
                    </div>
                    <div class="price-r">
                        <span class="price-r-t">省4990</span>
                        <span class="price-r-b">原价21878</span>
                    </div>
                </div>
                <a href="javascript:goBuyVip('silver_distoffice');" class="vipPrice-btn">立即购买</a>
            </li>
        </ul>
    </div>

</div>
<div class="more_detail">
    <p>还是满足不了您的需求？功能更强大 ，服务更全面 <a href="https://qy.do1.com.cn/qwy/qiweipublicity/companysrv/vip/vip_gold_index.jsp" target="_blank" class="more_detail_btn">进一步了解金卡VIP</a></p>
</div>
    <%@include file="../include/moresrv.jsp" %>


<%@include file="../include/footer.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
<script>
    $(window).scroll(function (){
        var st = $(this).scrollTop();
        if(st>77){
            $(".banner-light").show();
        }else {
            $(".banner-light").hide();
        }
    });
    $(document).ready(function() {
        qwinitcount.orgConfigInfo = {
            corpId: wxqyhConfig.orgConfigInfo.corpId,
            creator: wxqyhConfig.orgConfigInfo.orgId,
            personName: wxqyhConfig.orgConfigInfo.orgName,
            type: 'vip_index.jsp',
        }
    })

</script>
</body>
</html>