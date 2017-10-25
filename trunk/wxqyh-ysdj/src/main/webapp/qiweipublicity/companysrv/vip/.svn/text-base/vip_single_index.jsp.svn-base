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
    <meta name="description" content="金卡VIP尊享数据接口、个性化独立门户、一对一技术服务、50G超大储存空间">
    <meta name="keywords" content="微信企业号,企业微信,企微,企微云平台,微信办公平台,企业号第三方应用,微信企业号第三方,企业号服务商">
    <title>金卡VIP，功能更强大、服务更全面|企微云平台</title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/vip.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_single_index.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/manager/companysrv/js/service.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
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
        <div class="header-right" style="margin-top: 15px;">
               <input type="button" onclick="goBuy_vip_single()" class="orangeBtn btn" style="font-size: 18px;padding: 5px 20px;" value="马上开通">
               <input type="button" class="btn ml5" style="font-size: 18px;" value="申请体验" id="btn_taste" onclick="goTasteVip()">
        </div>
    </div>
</div>

<!--banner-->
<div class="goldVIPBanner"><div class="banner-main"><a href="javascript:goBuyVipSingle();" class="btn-gold-open" id="btn_bannerBuyVip">立即开通金卡</a></div></div>
<!-- 主体部分 -->
<div class="goldContent">
    <%--<div class="goldVipItem">
        <div class="Tit">
            <h3>金卡VIP特权一览</h3>
            <span>金卡VIP在20余项VIP基础特权上，专享6大个性化功能特权</span>
        </div>
        <div class="goldVipPrivilege">
            <ul class="clearfix">
                <li><a href="javascript:void(0);">企微数据接口</a><i class="iconGold"></i></li>
                <li style="margin: 0 113px 0 75px;"><a target="_blank" href="javascript:goDo1OemLogin();">个性化登录门户</a><i class="iconGold"></i></li>
                <li><a href="javascript:void(0);">个性化应用欢迎语</a><i class="iconGold"></i></li>
                <li><a href="javascript:void(0);">50G超大储存空间</a><i class="iconGold"></i></li>
                <li style="margin: 0 113px 0 75px;"><a href="javascript:void(0);">一对一技术服务</a><i class="iconGold"></i></li>
                <li><a href="javascript:void(0);">支持付费定制开发</a><i class="iconGold"></i></li>
            </ul>
        </div>
        <div class="vip-privilege clearfix" style="border:1px #f0ebd7 solid;border-radius:0;">
            <%@include file="../include/vip_privilege.jsp" %>
        </div>
    </div>--%>
    <div class="goldVipItem" style="margin-top: 120px">
        <div class="goldIntroduce clearfix">
            <div class="fl goldIntroduceWord ml30 mt30">
                <h4>企微数据接口</h4>
                <p>通过企微标准数据接口，客户可以把企微
                    <br>与原有的OA、业务系统打通。</p>
                <dl class="goldDataDl mb30">
                    <dt class="c60c35a">目前已开放的数据接口：</dt>
                    <dd class="c60c35a">考勤数据读取接口</dd>
                    <dd class="c60c35a">通讯录读取接口</dd>
                    <dd class="c60c35a">表单流程的数据接口</dd>
                    <dd class="c60c35a">其他业务接口</dd>
                </dl>
                <%--<a href="javascript:void(0);">进一步了解服务流程与服务内容</a>--%>
            </div>
            <div class="fr golgIntroduceImg mr30">
                <img src="${baseURL}/themes/manager/companysrv/images/img/goldVipImg04.png?ver1.0" alt="" class="" />
            </div>
        </div>
    </div>
    <div class="goldVipItem" style="padding: 40px 0 80px;border-bottom: 1px #e6e6e6 solid">
        <div class="Tit">
            <h3>个性化独立门户</h3>
            <span>为金卡客户提供彰显企业品牌的专属独立门户及管理平台，可自由替换Logo、 企业信息</span>
            <a href="javascript:goDo1OemLogin();" class="goldBtn mt30">点我预览效果</a>
        </div>
        <div class="golgIntroduceBanner golgIntroduceBanner1"></div>
    </div>
    <div class="goldVipItem">
        <div class="Tit">
            <h3>个性化应用欢迎语</h3>
            <span>金卡客户可替换企微应用的默认推送内容，可实现个性化推送公司相关通知信息<br>让新员工进入企业微信后进一步了解公司</span>
        </div>
        <div class="golgIntroduceBanner golgIntroduceBanner2"></div>
    </div>
    <div class="goldVipItem">
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
    <div class="goldVipItem" style="margin-top: 110px">
        <div class="goldIntroduce clearfix">
            <div class="fl goldIntroduceWord ml30 mt100">
                <h4>50G超大储存空间</h4>
                <p>金卡VIP客户享有50G超大储存空间，满足您各类
                    <br>数据文档的存档管理需要。</p>
            </div>
            <div class="fr golgIntroduceImg mr30">
                <img src="${baseURL}/themes/manager/companysrv/images/img/goldVipImg02.jpg" />
            </div>
        </div>
    </div>
    <div class="goldVipItem" style="padding: 110px 0 80px;border-bottom: 1px #e6e6e6 solid">
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

<%@include file="../include/footer.jsp" %>
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>