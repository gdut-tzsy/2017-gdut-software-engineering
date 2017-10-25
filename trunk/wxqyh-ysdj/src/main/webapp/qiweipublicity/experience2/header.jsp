<%@page language="java" contentType="text/html; charset=UTF-8" %>
<div id='header'>
    <div class="navbar w960">
        <span title="企业微信应用，首选企微云平台" class="logo" target="_blank" style="top:2px;cursor:default">
            <img src="http://wbg.do1.com.cn/templets/default/images/daoyilogo.png" alt="" style="width:105px;height:65px;">
        </span>
        <span class="logo" style="width:1px;height:30px;background:#e5e5e5;left:110px;top:22px;"></span>
        <a title="企业微信应用，首选企微云平台"href="http://wbg.do1.com.cn/"class="logo" style="left:115px;top:2px;" >
            <img src="http://wbg.do1.com.cn/templets/default/images/1512/logo.png?v0113" alt="企业微信" style="width:125px;height:65px;">
        </a>
        <a href="https://qy.weixin.qq.com/cgi-bin/3rd_info?action=getinfo&t=3rd_isp_cert&uin=578000001" target="_blank" style="position: absolute;left: 255px;top: 16px;">
            <img style="vertical-align:top;" src="https://res.wx.qq.com/mmocbiz/zh_CN/tmt/home/dist/img/cert_logo_white_m_2cb55b0b.png" alt="企业微信登录">
        </a>
        <ul id="nav1">
            <li><a href="http://wbg.do1.com.cn/">首页</a></li>
            <li><a href="http://wbg.do1.com.cn/suite/WX_suite/">产品</a></li>
            <li><a href="http://wbg.do1.com.cn/case/">案例</a></li>
            <li><a href="http://wbg.do1.com.cn/School/">学院</a></li>
            <li class="i" id="i"><a href="https://qy.do1.com.cn/qwy/qiweipublicity/companysrv/vip/vip_index.jsp?utm_source=pc&utm_medium=nav" target="_blank">服务</a><i></i>
                <div class="more_nav" class="i">
                    <a href="https://qy.do1.com.cn/qwy/qiweipublicity/companysrv/vip/vip_index.jsp?utm_source=pc&utm_medium=nav" target="_blank">银卡VIP</a>
                    <a href="https://qy.do1.com.cn/qwy/qiweipublicity/companysrv/vip/vip_gold_index.jsp?utm_source=pc&utm_medium=nav" target="_blank">金卡VIP</a>
                    <a href="https://item.taobao.com/item.htm?id=534212690160&qq-pf-to=pcqq.c2c" target="_blank">云考勤机</a>
                    <a href="http://wbg.do1.com.cn/liteapp/" target="_blank">定制小程序</a>
                </div>
            <li><a href="http://wbg.do1.com.cn/Experience/" class="kt">免费开通</a></li>
            <li class="login" style="margin-left:10px"><a href="${qwyURL}" target="_blank" rel="nofollow">管理平台</a></li>
        </ul>
    </div>
</div>
<script>
(function($) {
    $('li.i').mouseover(function() {
        $(this).children('.more_nav').show();
    }).mouseout(function() {
        $(this).children('.more_nav').hide();
    })
}(jQuery));
</script>