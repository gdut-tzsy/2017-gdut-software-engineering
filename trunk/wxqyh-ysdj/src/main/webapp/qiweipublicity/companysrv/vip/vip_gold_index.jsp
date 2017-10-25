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
    <meta name="description" content="金卡VIP尊享数据接口、个性化独立门户、50G超大储存空间">
    <meta name="keywords" content="微信企业号,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,微信企业号第三方,企业号服务商">
    <meta name="renderer" content="webkit">
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <title>金卡VIP，功能更强大、服务更全面|企微云平台</title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_gold_index.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
    <script src="${baseURL}/js/wx/wxAPi.js?ver=<%=jsVer%>"></script>
    <style type="text/css">
        html{min-width: 1200px;}
    </style>
</head>
<body>
<!-- 头部 -->
<div id="header" style="position: fixed;top:0;">
    <div class="header-nav clearfix">
        <div class="header-left">
            <img src="${baseURL}/themes/manager/companysrv/images/img/logo.png">
        </div>
        <div class="header-tab">
            <span class="" onclick="goBuy_vip_single()">开通金卡</span>
            <span class="action">特权一览</span>
            <span class="" onclick="goBuy_vip_single(3)">特权详情</span>
        </div>
        <div class="header-right">
            <input type="button" class="btn topBtn" value="申请体验" id="btn_taste" onclick="goTasteVip()">
            <input type="button" onclick="goBuy_vip_single()" class="btn orangeBtn topBtn" value="马上开通">
        </div>
    </div>
</div>

<!--banner-->
<div class="goldVIPBanner">
    <div class="banner-main">
        <a href="javascript:goBuyVipSingle();" class="btn-gold-open" id="btn_bannerBuyVip">立即开通金卡</a>
    </div>
</div>
<!-- 主体部分 -->
<div class="goldContent">
    <div class="goldVipItem pt70">
        <div class="Tit">
            <h3>VIP特权一览</h3>
            <span>金卡VIP享有7大金卡专享权益，60余项功能特权，并持续增加中...</span>
        </div>
        <div class="vip-privilege clearfix">
            <%@include file="../include/vip_privilege.jsp" %>
        </div>
    </div>
    <div class="goldVipItem pt170" id="titleA1">
        <div class="goldIntroduce clearfix">
            <div class="fl goldIntroduceWord">
                <h4>企微数据接口</h4>
                <p>通过企微标准数据接口，客户可以把企微
                    <br>与原有的OA、业务系统打通。</p>
                <dl class="goldDataDl">
                    <dt class="c60c35a">目前已开放的数据接口：</dt>
                    <dd class="c60c35a">考勤数据读取接口</dd>
                    <dd class="c60c35a">通讯录读取接口</dd>
                    <dd class="c60c35a">表单流程的数据接口</dd>
                    <dd class="c60c35a">其他业务接口</dd>
                    <dd class="c60c35a">数字标牌接口</dd>
                    <dd class="c60c35a">移动CRM数据接口</dd>
                    <dd class="c60c35a">SAP微信集成接口</dd>
                </dl>
            </div>
            <div class="fr golgIntroduceImg mr30">
                <img src="${baseURL}/themes/manager/companysrv/images/img/goldVipImg04.png?ver1.0" alt="" class="" />
            </div>
        </div>
    </div>
    <div class="goldVipItem goldVipTab pt80">
        <div id="titleA2" class="pt80"></div>
        <ul class="item">
            <li class="active" id="wechat_title"><i class="wechat_icon"></i><p>一键生成微信小程序</p><b class="triangle_icon active"></b></li>
            <li id="print_title"><i class="print_icon"></i><p>自定义打印设置</p><b class="triangle_icon"></b></li>
            <li id="pie_title"><i class="pie_icon"></i><p>图形统计报表</p><b class="triangle_icon"></b></li>
            <li id="signature_title"><i class="signature_icon"></i><p>存档签名批示</p><b class="triangle_icon"></b></li>
            <li id="attachment_title"><i class="attachment_icon"></i><p>批量导出明细附件/图片</p><b class="triangle_icon none"></b></li>
        </ul>
        <div class="item-content">
            <h3>外部表单支持一键生成微信小程序，无须技术开发即可拥有</h3>
            <img src="../../../themes/manager/companysrv/images/img/A01.png" alt="">
        </div>
        <div class="item-content none">
            <h3>自定义设置纸质表单打印格式，简单易操作，方便存档</h3>
            <img src="../../../themes/manager/companysrv/images/img/A02.png" alt="">
        </div>
        <div class="item-content none">
            <h3>针对不同字段提供多种数据分析图表，让你的数据更有说服力</h3>
            <img src="../../../themes/manager/companysrv/images/img/A03.png" alt="">
        </div>
        <div class="item-content none">
            <h3>自由/固定流程均支持手写签名存档，直接调用保障审批效力</h3>
            <img src="../../../themes/manager/companysrv/images/img/A04.png" alt="">
        </div>
        <div class="item-content none">
            <h3>批量导出省时省力  文件夹目录清晰明了</h3>
            <img src="../../../themes/manager/companysrv/images/img/A05.png" alt="">
        </div>
    </div>
    <div class="goldVipItem pt70 center" id="titleB5">
        <div class="Tit">
            <h3>考勤打卡导入排班</h3>
            <span>批量导入员工排班信息，无须重复配置考勤规则</span>
        </div>
        <img src="${baseURL}/themes/manager/companysrv/images/img/B01.png?" alt="">
    </div>
    <div class="goldVipItem_new clearfix" id="titleB2">
        <div class="item-content">
            <div class="pos_absolute goldIntroduceWord">
                <h4>报销直连微信支付</h4>
                <p>可通过微信转账方式给员工发报销费用
                    <br>让报销快到飞起来</p>
                <dl class="goldDataDl">
                    <dt class="c60c35a ml-10">「移动报销」还有以下特权：</dt>
                    <dd class="c60c35a">收款账户信息打印设置特权</dd>
                    <dd class="c60c35a"> 报销字段设置- 收款账户信息</dd>
                </dl>
            </div>
            <img src="${baseURL}/themes/manager/companysrv/images/img/C01.png" alt="微信支付" class="fr">
        </div>
    </div>

    <div class="goldVipItem pt100" id="titleC1">
        <div class="goldIntroduce clearfix">
            <div class="fr goldIntroduceWord ml30 mt100">
                <h4>50G超大储存空间</h4>
                <p>享有50G超大储存空间
                    <br>满足您各类数据文档的存档管理需要</p>
                <dl class="goldDataDl">
                    <dt class="c60c35a ml-10">「企业云盘」还有以下特权：</dt>
                    <dd class="c60c35a">仅特定对象下载特权</dd>
                    <dd class="c60c35a">10层级文件目录特权</dd>
                    <dd class="c60c35a">突破文件图片上传限制特权</dd>
                </dl>
            </div>

            <div class="fl golgIntroduceImg mr30">
                <img src="${baseURL}/themes/manager/companysrv/images/img/goldVipImg02.jpg" />
            </div>
        </div>
    </div>

    <div class="goldVipItem_new clearfix mt80" id="titleB6">
        <div class="item-content">
            <div class="pos_absolute goldIntroduceWord">
                <h4>人员邀请自定义字段</h4>
                <p>通过自定义字段设置外部邀请单
                    <br>外部成员扫一扫直接加入企业微信通讯录</p>
                <dl class="goldDataDl">
                    <dt class="c60c35a ml-10">「通讯录」还有以下特权：</dt>
                    <dd class="c60c35a">更多通讯录同步次数特权 </dd>
                    <dd class="c60c35a"> 更多标签同步次数特权</dd>
                </dl>
            </div>
            <img src="${baseURL}/themes/manager/companysrv/images/img/D01.png" alt="人员邀请自定义字段" class="fr">
        </div>
    </div>

    <div class="goldVipItem pt80" id="titleC2">
        <div class="Tit">
            <h3>自定义应用推送</h3>
            <span>可替换企微应用的默认推送内容<br>可实现个性化推送公司相关通知信息，让新员工进入企业微信后进一步了解公司</span>
        </div>
        <div class="golgIntroduceBanner golgIntroduceBanner2"></div>
    </div>

    <div class="goldVipItem pt80" id="titleC5">
        <div class="Tit">
            <h3>个性化独立门户</h3>
            <span>为金卡客户提供彰显企业品牌的专属独立门户及管理平台，可自由替换Logo、 企业信息</span>
        </div>
        <div class="golgIntroduceBanner golgIntroduceBanner1"></div>
    </div>

    <%--<div class="goldVipItem pt80" id="titleB1">
        <div class="bgf7f7f7 pt30">
            <div class="Tit">
                <h3>一对一技术服务</h3>
                <span>为金卡客户提供一对一的企业微信申请认证、应用安装使用、管理员培训等服务</span>
            </div>
            <div class="goldService">
                <ul class="clearfix mt10">
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/ic-bg7.png" />
                        <p>企业微信代申请及认证</p>
                        <span>解决申请认证流程麻烦，手续繁<br>多的问题</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/ic-bg6.png" />
                        <p>应用安装上线</p>
                        <span>远程为您的企业安装合适的功能<br>应用</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/ic-bg5.png" />
                        <p>管理员远程在线培训</p>
                        <span>平台应用功能深入浅出的远程培<br>训指导</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/ic-bg4.png" />
                        <p>一年专职技术支持服务</p>
                        <span>专职技术人员365天全方位的平台<br>疑难解决</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>--%>

    <div class="goldVipItem bgf7f7f7" style="padding: 110px 0 80px;">
        <div class="goldIntroduce clearfix">
            <div class="fl golgIntroduceImg mr30">
                <img src="${baseURL}/themes/manager/companysrv/images/img/goldVipImg03.jpg" />
            </div>
            <div class="fr goldIntroduceWord ml50 mt20" style="width: 450px">
                <h4>企微安全保障与保密承诺</h4>
                <p>道一信息通过了ISO27001：2013的信息安全管理体系
                    <br>国际认证，保证企微云平台的安全可靠。</p>
                <p>除此以外，金卡客户可与企微申请签订信息保密承诺协
                    <br>议，进一步确保对客户信息安全的保障。</p>
                <h5>八大安全保障，用得放心</h5>
                <ul class="goldSafyUl mt30">
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg01.png" />
                        <span>ISO27001<br>信息安全认证</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg02.png" />
                        <span>核心数据<br>加密储存</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg03.png" />
                        <span>数据文件<br>隔离储存</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg04.png" />
                        <span>多地机房<br>容灾备份</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg05.png" />
                        <span>专业运维团队<br>特聘安全专家</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg06.png" />
                        <span>入侵检测系统<br>入侵防御系统</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg07.png" />
                        <span>系统访问口令<br>识别机制</span>
                    </li>
                    <li>
                        <img src="${baseURL}/themes/manager/companysrv/images/img/goldSafyImg08.png" />
                        <span>完善的用户<br>生命周期管理</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="introduceItemMore">
        <%@include file="../include/moresrv.jsp" %>
    </div>
</div>

<div class="menu_float">
    <a href="#titleA1">数据接口</a>
    <a href="#titleA2" data-id="wechat_title">表单流程</a>
    <a href="#titleB5">考勤排班</a>
    <a href="#titleB2">移动报销</a>
    <a href="#titleC1">企业云盘</a>
    <a href="#titleB6">通讯录</a>
    <a href="#titleC2">高级设置</a>
    <a href="#titleB1">其它特权</a>
</div>
<%@include file="../include/footer.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>