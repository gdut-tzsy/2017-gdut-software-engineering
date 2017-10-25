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
    <title><%=qwManagerTitle%></title>
    <link rel="stylesheet" type="text/css" href="${baseURL}/manager/css/style.css?ver=<%=jsVer%>">
    <link href="${baseURL}/themes/manager/companysrv/css/fw.css?ver=<%=jsVer%>" rel="stylesheet">
    <script src='${baseURL}/manager/js/jquery-1.10.2.min.js'></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.form.js"></script>
    <script src="${baseURL}/js/3rd-plug/jquery/jquery.jcryption/jquery.jcryption.3.1.0.js"></script>
    <script src="${baseURL}/js/do1/common/common.js?ver=<%=jsVer%>"></script>
    <script src='${baseURL}/themes/qw/js/stringFormat.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/manager/companysrv/js/product.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qiweipublicity/companysrv/vip/vip_single_buy.js?ver=<%=jsVer%>'></script>
    <script src='${baseURL}/themes/qw/js/growingio.js?ver=<%=jsVer%>'></script>
    <style type="text/css">
      html{min-width: 1200px;}
        #Pay-header{
            border:none;
        }
    </style>
</head>
<body>
<!-- 头部 -->
<div id="Pay-header">
    <div class="Pay-header-nav clearfix">
        <div class="Pay-header-left">
            <img src="${baseURL}/themes/manager/companysrv/images/img/logo.png">
        </div>
        <div class="header-tab">
            <span class="">开通金卡</span>
            <span class="">特权一览</span>
            <span class="">特权详情</span>
        </div>
        <div class="fr mt15">
            <input type="button" class="btn topBtn" value="申请体验" onclick="goTasteVip()">
            <input type="button" id="gobuyvip" class="btn topBtn orangeBtn" value="马上开通">
        </div>
    </div>
</div>
<div class="wrap-container FW">
    <div id="container" class="clearfix">
        <div class="place tleft fz14">

        </div>
        <div class="FW_content">
            <!-- 账户安全 -->
            <div class="tab-main">
                <div class="buyvip-main mb0">
                    <form method="post" id="buyvipForm" style="display: none">
                        <div class="item mb30 mt10">
                            <span class="dt" style="vertical-align: top;">金卡服务：</span>
                            <span class="dd" id="buyService">
                                <span class="opt goldTCClass" id="prime" myValue="prime" oriPrice="12500000" promPrice="2000000" style="width:150px;">
                                    金卡套餐
                                </span>
                                <div class="fw_type" id="singleProductDiv">
                                </div>
                            </span>
                        </div>
                        <div class="onLine">
                            <div class="item mb30">
                                <span class="dt">购买年数：</span>
                                <span class="dd relative" id="years">
                                    <span class="opt" myValue="1">一年</span>
                                    <span class="opt" myValue="2">两年<%--<span class="orange">(惠)</span>--%></span>
                                    <span class="opt" myValue="3" id="activeTips">三年<%--<span class="orange">(惠)--%></span>
                                        <%--<img src="../../../themes/manager/companysrv/images/img/tips-act.png"/>--%>
                                    </span>
                                    <div class="check-tips none" id="yearsTips"><span>!</span>请选择购买年数</div>
                                </span>
                            </div>
                            <div class="item mb40">
                                <span class="dt"><span class="orange">*</span> CorpID：</span>
                                <span class="dd" style="position:relative">
                                    <input class="fl" type="text" id="corpId" onblur="getOrgMember()">
                                    <span class="fl ml10 dd-inline">
                                        <a href="javascript:showAcountBox();" class="fz14" style="text-decoration:underline;">使用企微管理账号获取</a><br>
                                        <span class="c999 fz14">也可在企业微信或企微管理后台中复制</span>
                                    </span>
                                    <div class="check-tips none" id="corpIdTips" style="width: 320px;"><span>!</span>CorpID不存在</div>
                                </span>
                            </div>
                            <div class="item mb15">
                                <span class="dt">购买人数：</span>
                                <span class="dd relative" id="members">

                                    <span class="positiveIpt fl">
                                        <input type="button" class="positiveIpt_l p_btn" value="-" />
                                        <input type="text" class="positiveIpt_t" id="buyMember" value="1"/>
                                        <input type="button" class="positiveIpt_r p_btn" value="+" />
                                    </span>

<%--                                      <span class="opt on" myValue="500">1-500人</span>
                                      <span class="opt" myValue="1000">501-1000人</span>
                                      <span class="opt" myValue="max">不限人数</span>--%>
                                        <span class="fl ml10 dd-inline">
                                            <span class="c999 fz14">通讯录人数</span><br>
                                            <span class="FW_orange fz14"><span id="userMemberNum">0</span>人</span>
                                        </span>
                                      <%--<span class="ml20 c999">(通讯录人数：<span class="orange" id="userMemberNum">0</span>人)</span>--%>
                                      <div class="check-tips none" id="memeberNumTips"><span>!</span>购买人数必须是大于<em class="orange"> 0 </em>的整数</div>
                                </span>
                            </div>
                            <div class="item mb40" id="beneficiary_div" style="display:none;">
                                <span class="dt">购买方式：</span>
                                 <span class="dd relative">
                                     <label>
                                         <input type="checkbox" id="beneficiary" name="beneficiary" style="border: none;padding: 0;line-height: 0"/>服务转赠
                                     </label>
                                </span>
                            </div>

                            <div class="item" id="estimateAmount_div">
                                <span class="dt">&nbsp;</span>
                                 <span class="dd relative">
                                     <span class="Amount" id="estimateAmount">￥0.00</span>
                                     <div class="dd-inline mt5 ml5">
                                         <span class="price_red" id="everyPersonAmount"></span>
                                     </div>
                                     <div class="dd-inline mt10 ml5">
                                         <span class="c555" id="concessionalRate"></span>
                                     </div>
                                </span>
                            </div>

                            <div class="item ml115 mt15 mb40">
                                <p id="priceTips">服务价格：100人以内按规定价格购买；人数超出部分，所选功能每50人增加2000元；购买人数超过1000人，可另外申请折扣优惠</p>
                            </div>

                            <div class="item">
                                <a class="btn orangeBtn ml115" onclick="buynow()" style="padding: 4px 25px;font-size: 16px;">立即购买</a>
                                <p class="ml115 mt10" id="btn_xieyi">购买即视为同意<a href="javascript:showAgreeBox('1');">《企业账户服务协议》</a></p>
                            </div>
                            <div class="item mt50 pt30 bte4e4e4">
                                <p>购买说明：</p>
                                <p class="mt10">1. 购买成功后，将马上为您开通该项服务（送银卡VIP服务，已是银卡VIP不再重复赠送）；过期后需续费才能继续享有原有功能。</p>
                                <p class="mt5">2.  若多次购买，则将最后一次购买的金卡特权服务期限作为赠送的银卡VIP服务期限。</p>
                                <p class="mt5">3. 申请发票请前往<a href="${baseURL}/manager/companysrv/order_list.jsp">订单管理页面</a>，提交相关信息，企微工作人员会在7个工作日内将发票寄出。</p>
                                <p class="mt5">4. 当购买人数超过2000人，可另外申请折扣优惠。</p>
                            </div>
                        </div>
                    </form>
                    <div class="buyvip-info" style="display: none;">
                        <p class="fz28 tac mb5">企微云平台版本介绍</p>
                        <p class="cec6519 fz16 tac">温馨提示：新用户注册后即享受15天金卡VIP免费使用体验</p>
                        <table>
                            <thead>
                                <tr>
                                    <th colspan="2">版本功能</th>
                                    <th width="160px">免费版</th>
                                    <th width="160px">银卡VIP</th>
                                    <th width="160px">金卡VIP</th>
                                </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td rowspan="21">表单流程</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">固定流程与分支流程</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">指纹验证审批</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">手写签名批示</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">关联CRM、人员数据</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">定时循环提醒</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">记录审批修改轨迹</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义字段查询</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">Excel导入生成表单</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">审批超时处理规则</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">流程加签</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">表单导入明细</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">按标签（审批角色）设置处理人</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">根据字段自动生成表单标题</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">一键生成微信小程序</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义打印设置</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">图形统计报表</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">批量导出明细附件/图片</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">微信端导出数据明细</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">关联其他表单数据</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">导入明细发起任务/流程</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%-- 培训学习--%>
                            <tr>
                                <td rowspan="4" class="bcfff">培训学习</td>
                                <td class="tal">初始空间</td>
                                <td>500M</td>
                                <td>2G</td>
                                <td>10G</td>
                            </tr>
                            <tr>
                                <td class="tal">初始流量</td>
                                <td>500M</td>
                                <td>5G</td>
                                <td>10G</td>
                            </tr>
                            <tr>
                                <td class="tal">上传音视频课件大小</td>
                                <td>100M</td>
                                <td>500M</td>
                                <td>1000M</td>
                            </tr>
                            <tr>
                                <td class="tal">单次学习最大人数</td>
                                <td>100人</td>
                                <td>5000人</td>
                                <td>5000人</td>
                            </tr>
                            <%--企业云盘--%>
                            <tr>
                                <td rowspan="8" class="bcfff">企业云盘</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">指纹鉴权</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">在线预览</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">文件上传限制</td>
                                <td>图片1M
                                    文件10M</td>
                                <td>图片10M
                                    文件20M</td>
                                <td>图片10M
                                    文件50M</td>
                            </tr>
                            <tr>
                                <td class="tal">文件目录层级</td>
                                <td>5层</td>
                                <td>5层</td>
                                <td>10层</td>
                            </tr>
                            <tr>
                                <td class="tal">50G超大储存空间</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">仅特定对象下载功能</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">文件操作日志管理</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--移动报销--%>
                            <tr>
                                <td rowspan="8" class="bcfff">移动报销</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">固定流程与分支流程</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">报销时间设置</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">报销单打印设置</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">收款账户信息打印设置</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">报销字段设置- 收款账户信息</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">移动报销关联请假出差数据</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">微信支付自动转账报销</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--通讯录--%>
                            <tr>
                                <td rowspan="11" class="bcfff">通讯录</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">多邀请单自定义</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义字段设置</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">领导通讯录排序</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">隐藏敏感信息</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">部门权限指定特定对象</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">离职工作指派交接</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">人员邀请单自定义字段</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">存档签名批示</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">更多通讯录同步次数</td>
                                <td>3次</td>
                                <td>10次</td>
                                <td>10次</td>
                            </tr>
                            <tr>
                                <td class="tal">更多标签同步次数</td>
                                <td>3次</td>
                                <td>10次</td>
                                <td>10次</td>
                            </tr>
                            <%--请假出差--%>
                            <tr>
                                <td rowspan="2" class="bcfff">请假出差</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">固定流程与分支流程</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--审批请示--%>
                            <tr>
                                <td rowspan="2" class="bcfff">审批请示</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">固定流程</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--考勤打卡--%>
                            <tr>
                                <td rowspan="4" class="bcfff">考勤打卡</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">批量排班</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">导入排班</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">屏蔽云考勤机宣传信息</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--企业用车--%>
                            <tr>
                                <td rowspan="2" class="bcfff">企业用车</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">固定流程</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--任务分派--%>
                            <tr>
                                <td rowspan="2" class="bcfff">任务分派</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">子任务管理</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--会议助手--%>
                            <tr>
                                <td rowspan="5" class="bcfff">会议助手</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">大屏幕签到上墙</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">会议请假审批</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">会议室合并使用</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">会议人数限制</td>
                                <td>25人</td>
                                <td>100人</td>
                                <td>1000人</td>
                            </tr>
                            <%--我的待办--%>
                            <tr>
                                <td rowspan="1" class="bcfff">我的待办</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--拿快递--%>
                            <tr>
                                <td rowspan="1" class="bcfff">拿快递</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--新闻公告--%>
                            <tr>
                                <td rowspan="8" class="bcfff">新闻公告</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">新建新闻公告-定向红包</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">新建新闻公告-第三方页面</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">新建新闻公告-超大正文内容</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">关怀红包</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">多图文推送</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">私人信箱动态密码</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">添加语音音频</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--企业活动--%>
                            <tr>
                                <td rowspan="5" class="bcfff">企业活动</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">现场活动：<br>
                                    3D签到、大屏抽奖、投票、评论弹幕</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">外部现场活动</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">活动评论审核</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">外部活动授权公众号</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--同事社区--%>
                            <tr>
                                <td rowspan="2" class="bcfff">同事社区</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">敏感字过滤</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--问卷投票--%>
                            <tr>
                                <td rowspan="2" class="bcfff">问卷投票</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">统计报表</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--人事管理--%>
                            <tr>
                                <td rowspan="2" class="bcfff">人事管理</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">人事提醒设置</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--考试评测--%>
                            <tr>
                                <td rowspan="5" class="bcfff">考试评测</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">考试过程中修改考试成员</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">考试统计</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">批量导入试题数</td>
                                <td>100条/次</td>
                                <td>200条/次</td>
                                <td>2000条/次</td>
                            </tr>
                            <tr>
                                <td class="tal">单次考试最大人数</td>
                                <td>100人</td>
                                <td>500人</td>
                                <td>5000人</td>
                            </tr>
                            <%--移动CRM--%>
                            <tr>
                                <td rowspan="3" class="bcfff">移动CRM</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义导出全景数据</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">批量导入记录数</td>
                                <td>100条</td>
                                <td>500条</td>
                                <td>2000条</td>
                            </tr>
                            <%--知识百科--%>
                            <tr>
                                <td rowspan="2" class="bcfff">知识百科</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">新建知识百科-超大正文</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--合同管理--%>
                            <tr>
                                <td rowspan="2" class="bcfff">合同管理</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">附件上传</td>
                                <td>10M</td>
                                <td>20M</td>
                                <td>50M</td>
                            </tr>
                            <%--移动外勤--%>
                            <tr>
                                <td rowspan="1" class="bcfff">移动外勤</td>
                                <td class="tal">基础功能</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--统计管理--%>
                            <tr>
                                <td rowspan="1" class="bcfff">统计管理</td>
                                <td class="tal">打包下载</td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--设置中心--%>
                            <tr>
                                <td rowspan="8" class="bcfff">高级设置</td>
                                <td class="tal">流程管理</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">隐藏未安装应用</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">隐藏企微LOGO标识</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义消息发送对象</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">敏感字库设置</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">关闭企业服务菜单</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">自定义应用推送</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">个性化登陆门户</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--数据接口--%>
                            <tr>
                                <td rowspan="6" class="bcfff">数据接口</td>
                                <td class="tal">考勤打卡数据接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">通讯录读取接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">数字标牌接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">表单流程数据接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">流程引擎集成接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <tr>
                                <td class="tal">移动CRM数据接口</td>
                                <td></td>
                                <td></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--套餐服务--%>
                            <tr>
                                <td rowspan="2" class="bcfff">套餐服务</td>
                                <td class="tal">全国企微培训课</td>
                                <td></td>
                                <td>一个免费名额</td>
                                <td>四个免费名额</td>
                            </tr>
                            <tr>
                                <td class="tal">后续会员特权功能</td>
                                <td></td>
                                <td><i class="opt"></i></td>
                                <td><i class="opt"></i></td>
                            </tr>
                            <%--增值服务--%>
                            <tr>
                                <td rowspan="4" class="bcfff" style="border-bottom: 1px solid #ffcbad;">增值服务</td>
                                <td class="tal">一对一远程技术服务（元/年）</td>
                                <td>￥8,999.00</td>
                                <td>￥6,999.00</td>
                                <td>￥5,999.00</td>
                            </tr>
                            <tr>
                                <td class="tal">云储存空间（元/G/年）</td>
                                <td colspan="3">￥100.00</td>
                            </tr>
                            <tr>
                                <td class="tal">音视频储存空间（元/100G/年）</td>
                                <td colspan="3">￥299.00</td>
                            </tr>
                            <tr>
                                <td class="tal">音视频储流量（元/100G）</td>
                                <td colspan="3">￥99.00</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <%@include file="../../../manager/companysrv/accountAgreement.jsp" %>
        </div>
    </div>
    <%@include file="../include/footer.jsp" %>
</div>
<%@include file="../include/accountMsgBox.jsp" %>
<!-- 获取企业Corpid弹框 -结束-->
<%@include file="../../../manager/msgBoxs.jsp" %>
</body>
</html>
