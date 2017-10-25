<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
<head>
    <meta charset="utf-8">
    <title>邀请您加入企业微信</title>
    <meta name="description" content="">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" id="meta"
          content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>"/>
    <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/invitation.css?ver=<%=jsVer%>"/>
    <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
    <script type="text/javascript" src="${resourceURL}/js/do1/common/checkCard.js?ver=<%=jsVer%>"></script>
</head>
<body>
<div id="wrap_main" class="wrap interview_wrap1">
    <div class="i_inter_top">
        <img src="${baseURL}/jsp/wap/images/tab.png" alt="" class="i_liucheng1"/>
        <span class="i_form-write"></span>
        <span class="i_form-saoma"></span>
    </div>
    <form onsubmit="return false;" id="question_detail" class="question_detail">
        <input type="hidden" name="id" id="id" value="${param.id}"/>
        <input type="hidden" name="type" id="type" value=""/>
        <div class="form_btns mt10" id="btn_gray" style="display:none">
            <div class="inner_form_btns pb10">
                <div class="fbtns flexbox">
                    <a class="fbtn gray_btn" href="#">该邀请页面已过期</a>
                </div>
            </div>
        </div>
        <h1 id="detail_title" class="title" style=""></h1>
        <div class="titletxet" id="content">企业微信成员邀请</div>
        <div class="form_content border">
            <div class="isMust">
                <div class="letter_bar"><span class="optionName">姓名</span></div>
                <input type="text" id="personName" placeholder="请输入" maxlength="30" value=""
                       name="tbQyMemberInfoPO.personName" class="textInput item-select">
            </div>
            <div id="weixin" style="display: none">
                <div class="letter_bar"><span class="optionName">微信号</span>（在“微信 > 我”页面第一栏可找到）</div>
                <input type="text" placeholder="填写微信号可直接关注，跳过手机验证" maxlength="30" id="weixinNum"
                       name="tbQyMemberInfoPO.weixinNum" class="textInput item-select" data-valid="weixinNum">
            </div>
            <div id="tel" style="display: none">
                <div class="letter_bar"><span class="optionName">手机号</span></div>
                <input type="tel" maxlength="30" id="mobile" placeholder="填写正确手机号码接收关注验证短信" value=""
                       name="tbQyMemberInfoPO.mobile" class="textInput item-select" data-valid="mobile">
            </div>
            <div id="account" style="display: none">
                <div class="letter_bar"><span class="optionName">账号</span> （成员唯一标识，不支持中文）<span
                        id="msgSpan"></span>
                </div>
                <input type="text" maxlength="64" placeholder="请输入" id="wxUserId"
                       name="tbQyMemberInfoPO.wxUserId" class="textInput item-select" data-valid="wxUserId">
            </div>
            <div id="emailId" style="display: none">
                <div class="letter_bar"><span class="optionName">邮箱</span></div>
                <input type="text" maxlength="50" placeholder="请输入" id="email"
                       name="tbQyMemberInfoPO.email" class="textInput item-select" data-valid="email">
            </div>
            <div id="deptNo" style="display: none">
                <div class="letter_bar"><span class="optionName">部门</span></div>
                <div class="f-item">
                    <div class="item-text flexbox">
                        <input type="hidden" name="tbQyMemberInfoPO.selectDept" id="deptName" value=""/>
                        <input type="hidden" name="deptId" id="deptId" class="textInput item-select" value=""/>
                        <select class="textInput item-select selectDeptId" value="" name="tbQyMemberInfoPO.selectDeptId"
                                id="selectDeptId">
                            <option value='' selected='selected'>请选择部门</option>
                        </select>
                    </div>
                </div>
            </div>
            <div id="sexID" style="display: none">
                <div class="letter_bar"><span class="optionName">性别</span></div>
                <div class="answer-list radio_btn table">
                    <ul class="table-row row3 sexHook">
                        <li class="">
                            <input type="radio" style="display:none" name="tbQyMemberInfoPO.sex"
                                   checked="checked" value="1">
                            <div class="xian_option"><i><span></span></i>男</div>
                        </li>
                        <li class="">
                            <input type="radio" style="display:none" name="tbQyMemberInfoPO.sex" value="2">
                            <div class="xian_option"><i><span></span></i>女</div>
                        </li>
                    </ul>
                </div>
            </div>
            <div id="nikeName" style="display: none">
                <div class="letter_bar"><span class="optionName">昵称</span></div>
                <input type="text" maxlength="100" placeholder="请输入" value="" name="tbQyMemberInfoPO.nickName"
                       class="textInput item-select">
            </div>
            <div id="idCard" style="display: none">
                <div class="letter_bar"><span class="optionName">身份证</span></div>
                <input type="text" maxlength="30" id="identity" placeholder="请输入" value=""
                       name="tbQyMemberInfoPO.identity" class="textInput item-select" data-valid="identity">
            </div>
            <div id="position" class="mutiCheckBox" style="display: none"  data-id="positionList">
                <div class="letter_bar"><span class="optionName">职位</span></div>
                <input type="text" maxlength="50" placeholder="请选择" value="" name="tbQyMemberInfoPO.position"
                       class="item-select textInput" readonly="readonly">
            </div>
            <div id="qqNum" style="display: none">
                <div class="letter_bar"><span class="optionName">QQ</span></div>
                <input type="number" maxlength="20" placeholder="请输入" value="" name="tbQyMemberInfoPO.qqNum"
                       class="textInput item-select">
            </div>
            <div id="phone" style="display: none">
                <div class="letter_bar"><span class="optionName">电话</span></div>
                <div class="">
                    <input type="text" maxlength="30" placeholder="请输入" value=""
                           name="tbQyMemberInfoPO.shorMobile" class="textInput item-select">
                </div>
            </div>
            <div id="phone1" style="display: none">
                <div class="letter_bar"><span class="optionName">电话2</span></div>
                <input type="text" maxlength="30" placeholder="请输入" value="" name="tbQyMemberInfoPO.phone"
                       class="textInput item-select">
            </div>
            <div id="address" style="display: none">
                <div class="letter_bar"><span class="optionName">地址</span></div>
                <input type="text" maxlength="100" placeholder="请输入" value="" name="tbQyMemberInfoPO.address"
                       class="textInput item-select">
            </div>
            <div id="birthday" style="display: none">
                <div class="letter_bar"><span class="optionName">阳历生日 </span></div>
                <input type="text" maxlength="100" placeholder="请选择" value="" name="tbQyMemberInfoPO.birthday"
                       class="dateInput item-select">
            </div>
            <div id="birthday2" style="display: none">
                <div class="letter_bar"><span class="optionName">农生日历</span></div>
                <input type="text" maxlength="100" placeholder="请选择" value=""
                       name="tbQyMemberInfoPO.lunarCalendar" class="timeInput item-select">
            </div>
            <div id="company" style="display: none">
                <div class="letter_bar"><span class="optionName">单位名称</span></div>
                <input type="text" id="companyName" placeholder="请输入" maxlength="200" value=""
                       name="tbQyMemberInfoPO.companyName" class="textInput item-select">
            </div>
            <div id="remarkId" style="display: none">
                <div class="letter_bar"><span class="optionName">备注</span></div>
                <textarea placeholder="请输入备注信息" name="tbQyMemberInfoPO.remark"
                          class="ask_textarea_content textInput item-select" index="0"></textarea>
            </div>
        </div>
        <div class="form_btns mt10" id="btn_div" style="display:block">
            <div class="inner_form_btns pb10">
                <div class="fbtns flexbox">
                    <a class="fbtn btn" id="formBtn">下一步：扫码关注</a>
                </div>
            </div>
        </div>

        <div class="form_btns mt10" id="btn_red" style="display:none">
            <div class="inner_form_btns pb10">
                <div class="fbtns flexbox">
                    <a class="fbtn gray_btn" href="#">该邀请页面已过期</a>
                </div>
            </div>
        </div>
        <div id="remind" class="mt20 mb20">
            <div class="suxing">
                有效期至：<span class="detailStart" id="stopTime" style="width:45%;"></span>
            </div>
        </div>
    </form>
    <div class="inter_saoma">
        <p class="inter_saoma_tit">企业微信名称企业微信名称</p>

        <div class="inter_saoma_img">
            <img id="inter_img" src="${resourceURL}/jsp/wap/images/inter_sao_img.png" height="170" width="170">
        </div>

        <div class="inter_nosum mb20">
            <p class="inter_sum">如果你已提交过资料，可直接扫码关注 </p>
            <p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
        </div>
        <div class="inter_success1 mb20" style="display:none;">
            <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20"
                                           width="20">资料已提交，请扫码关注</p>
            <p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
        </div>
        <div class="inter_success2 mb20" style="display:none;">
            <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20"
                                           width="20">资料已提交，请扫码关注</p>
            <p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
            <p class="inter_sum3">关注后不需再操作，请等待管理员审批通过。</p>
        </div>
    </div>
</div>
<div id="showMutiCheckBox" class="none">
    <div class="content">
        <div class="buttonDiv">
            <a href="javascript:;" class="btn white_btn cancel">取消</a>
            <a href="Fjavascript:;" class="btn save">确定</a>
        </div>
        <div class="list">
        </div>
        <div style="height: 85px"></div>
    </div>
</div>
<%@include file="/open/include/footer.jsp" %>
<%@include file="/open/include/showMsg.jsp" %>
<%--
删除了HTML内联的 valid 输入验证
删除了多于的js
--%>
<script>
    var resourceURL = "${resourceURL}";
    var CommitFormUrl = "${baseURL}/open/memberAction!ajaxAdd.action";
    var isEdu = false;
</script>
<script src="${baseURL}/themes/open/member/add.js?ver=<%=jsVer%>"></script>
</body>
</html>