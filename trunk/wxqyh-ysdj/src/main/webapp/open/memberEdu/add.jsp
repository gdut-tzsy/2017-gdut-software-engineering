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
        <meta name="viewport" id="meta" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <%--<link rel="stylesheet" href="${resourceURL}/jsp/wap/css/invitation.css?ver=<%=jsVer%>"/>--%>
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/invitation.css?ver=<%=jsVer%>"/>
	     <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
	    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script type="text/javascript" src="${resourceURL}/js/do1/common/checkCard.js?ver=<%=jsVer%>"></script>
    </head>
	<body>
	<div id="wrap_main" class="wrap interview_wrap1">
		<div class="i_inter_top">
            <img src="${resourceURL}/jsp/wap/images/tab.png" alt ="" class="i_liucheng1" />
        </div>
        <form class="question_detail" id="question_detail2" style="display:none" onsubmit="return false;">
            <div class="childrenDivList" style="padding: 10px 0;">
                <div class="childrenList none" id="0">
                    <div class="childrenListTitle p10 none">
                        <span class="f-item-title">孩子信息<span class="index">1</span></span>
                        <div class="fr"><a class="delete" href="javascript:;">- 删除</a><i class="icon-slide"> </i></div>
                    </div>
                    <div class="childrenListContent">
                        <div class="childrenDiv childrenName isMust" data-id="personName">
                            <div class="letter_bar"><span style="color: red">*&nbsp;</span><span
                                    class="optionName">孩子姓名</span></div>
                            <div class="pos_relative">
                                <input type="text" placeholder="请输入" maxlength="30"
                                       class="item-select textInput search-input">
                            </div>
                        </div>
                        <div class="childrenDiv childrenRegisterTel isMust" data-id="enrolTel">
                            <div class="letter_bar"><span style="color: red">&nbsp;*</span><span class="optionName">入学登记手机号</span>
                            </div>
                            <div class="pos_relative">
                                <input type="tel" maxlength="30" class="item-select textInput search-input"
                                       data-valid="mobile">
                            </div>
                        </div>
                        <div class="childrenDiv isMust childrenClass" data-id="selectDeptId">
                            <div class="letter_bar"><span style="color: red">*&nbsp;</span><span
                                    class="optionName">孩子班级</span></div>
                            <div class="f-item">
                                <div class="item-text flexbox">
                                    <div class="flexItem">
                                        <select class="item-select textInput selectDeptId">
                                            <option value="">请选择班级</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="childrenDiv isMust" data-id="relation">
                            <div class="letter_bar"><span style="color: red">*&nbsp;</span><span class="optionName">与孩子的关系</span>
                            </div>
                            <div class="f-item">
                                <select class="textInput item-select">
                                    <option value="">请选择</option>
                                    <option value="0">父亲</option>
                                    <option value="1">母亲</option>
                                    <option value="2">爷爷</option>
                                    <option value="3">奶奶</option>
                                    <option value="4">外公</option>
                                    <option value="5">外婆</option>
                                    <option value="6">其他监护人</option>
                                </select>
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="sex">
                            <div class="letter_bar">孩子的性别</div>
                            <div class="answer-list radio_btn table">
                                <ul class="table-row row3 sexHook">
                                    <input type="hidden" class="textInput">
                                    <li>
                                        <input type="radio" style="display:none"
                                               checked="checked" value="1">
                                        <div class="xian_option"><i><span></span></i>男</div>
                                    </li>
                                    <li>
                                        <input type="radio" style="display:none" value="2">
                                        <div class="xian_option"><i><span></span></i>女</div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="card">
                            <div class="letter_bar">孩子的身份证号码</div>
                            <div class="pos_relative">
                                <input type="text" maxlength="30" placeholder="请输入"
                                       class="item-select textInput search-input" data-valid="identity">
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="year">
                            <div class="letter_bar">孩子出生年月日</div>
                            <div class="pos_relative">
                                <input type="text" maxlength="100" placeholder="请选择"
                                       class="item-select textInput dateInput" readonly="readonly">
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="tel">
                            <div class="letter_bar">孩子手机号</div>
                            <div class="pos_relative">
                                <input type="tel" maxlength="30" class="item-select textInput search-input"
                                       data-valid="mobile">
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="wx">
                            <div class="letter_bar">孩子微信号</div>
                            <div class="pos_relative">
                                <input type="text" maxlength="30" class="item-select textInput search-input"
                                       data-valid="weixinNum">
                            </div>
                        </div>
                        <div class="childrenDiv none" data-id="email">
                            <div class="letter_bar">孩子邮箱</div>
                            <div class="pos_relative">
                                <input type="email" placeholder="请输入" class="item-select textInput search-input"
                                       data-valid="email">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="f-item" id="addChildren" style="background: #f1f0f6;">
                <div class="item-text " style="text-align:center;">
                    <a class="add_reimburse_num" style="color: #0099FF;cursor: pointer;width:100%">+ 添加另一个孩子信息</a>
                </div>
            </div>
            <div class="form_btns mt10" id="childrenSubmit">
                <div class="pb10">
                    <a class="btn nextHook mt10" href="javascript:;">下一步：扫码关注</a>
                    <a class="btn white_btn mt10 returnHook" href="javascript:;">返回上一步</a>
                </div>
            </div>
        </form>
        <form id="question_detail" class="question_detail" onsubmit="return false;">
			<input type="hidden" name="id" id="id" value="${param.id}" />
			<input type="hidden" name="type" id="type" value=""/>
            <input type="hidden" name="listJson" id="listJson" value="">
            <div class="form_btns mt10" id="btn_gray" style="display:none">
                <div class="inner_form_btns pb10">
                    <div class="fbtns flexbox">
                        <a class="fbtn gray_btn flexItem" href="#">该邀请页面已过期</a>
                    </div>
                </div>
            </div>
            <h1 id="detail_title" class="title" style=""></h1>
            <div class="titletxet" id="content">企业微信成员邀请</div>
            <div class="form_content border">
                <div class="letter_bar">用户类型</div>
                <div class="item-select" id="targetType"></div>
                <div class="isMust">
                    <div class="letter_bar"><span class="optionName">姓名</span></div>
                    <div class="pos_relative">
                        <input type="text" id="personName" placeholder="请输入" maxlength="30" value=""
                               name="tbQyMemberInfoPO.personName" class="item-select textInput search-input">
                    </div>
                </div>
                <div id="weixin" style="display: none">
                    <div class="letter_bar"><span class="optionName">微信号</span>（在“微信 > 我”页面第一栏可找到）</div>
                    <input type="text" placeholder="填写微信号可直接关注，跳过手机验证" maxlength="30" id="weixinNum"
                           name="tbQyMemberInfoPO.weixinNum" class="item-select textInput" data-valid="weixinNum">
                </div>
                <div id="tel" style="display: none">
                    <div class="letter_bar"><span class="optionName">手机号</span></div>
                    <input type="tel" maxlength="30" id="mobile" placeholder="填写正确手机号码，接收关注验证短信"
                           name="tbQyMemberInfoPO.mobile" class="item-select textInput" data-valid="mobile">
                </div>
                <div id="account" style="display: none">
                    <div class="letter_bar"><span class="optionName">账号</span> （成员唯一标识，不支持中文<span id="msgSpan">，默认为手机号或微信号</span>
                    </div>
                    <div class="pos_relative">
                        <input type="text" maxlength="100" placeholder="请输入" id="wxUserId"
                               name="tbQyMemberInfoPO.wxUserId" class="item-select textInput search-input"
                               data-valid="wxUserId">
                    </div>
                </div>
                <div id="emailId" style="display: none">
                    <div class="letter_bar"><span class="optionName">邮箱</span></div>
                    <div class="pos_relative">
                        <input type="email" maxlength="50" placeholder="请输入" id="email" name="tbQyMemberInfoPO.email"
                               class="item-select textInput search-input" data-valid="email">
                    </div>
                </div>
                <div id="deptNo" style="display: none">
                    <div class="letter_bar"><span class="optionName">班级</span></div>
                    <div class="f-item">
                        <div class="item-text flexbox">
                            <div class="flexItem">
                                <input type="hidden" name="tbQyMemberInfoPO.selectDept" id="deptName" value=""/>
                                <input type="hidden" name="deptId" id="deptId" value=""/>
                                <select class="flexItem item-select selectDeptId textInput" name="tbQyMemberInfoPO.selectDeptId"
                                        id="selectDeptId">
                                    <option value='' selected='selected'>请选择班级</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="sexID" style="display: none">
                    <div class="letter_bar"><span class="optionName">性别</span></div>
                    <div class="answer-list radio_btn table">
                        <ul class="table-row row3 sexHook">
                            <li>
                                <input type="radio" style="display:none" name="tbQyMemberInfoPO.sex" checked="checked"
                                       value="1">
                                <div class="xian_option"><i><span></span></i>男</div>
                            </li>
                            <li>
                                <input type="radio" style="display:none" name="tbQyMemberInfoPO.sex" value="2">
                                <div class="xian_option"><i><span></span></i>女</div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="nikeName" style="display: none">
                    <div class="letter_bar"><span class="optionName">昵称</span></div>
                    <div class="pos_relative">
                        <input type="text" maxlength="100" placeholder="请输入" value="" name="tbQyMemberInfoPO.nickName"
                               class="item-select textInput search-input">
                    </div>
                </div>
                <div id="idCard" style="display: none">
                    <div class="letter_bar"><span class="optionName">身份证</span></div>
                    <div class="pos_relative">
                        <input type="text" maxlength="30" id="identity" placeholder="请输入"
                               name="tbQyMemberInfoPO.identity" class="item-select textInput search-input"
                               data-valid="identity">
                    </div>
                </div>
                <div id="position" class="mutiCheckBox" data-id="positionList" style="display: none">
                    <div class="letter_bar"><span class="optionName">职位</span></div>
                    <input type="text" maxlength="50" placeholder="请选择" value="" name="tbQyMemberInfoPO.position"
                           class="item-select textInput" readonly="readonly">
                </div>
                <div id="qqNum" style="display: none">
                    <div class="letter_bar"><span class="optionName">QQ</span></div>
                    <div class="pos_relative">
                        <input type="number" maxlength="20" placeholder="请输入" value="" name="tbQyMemberInfoPO.qqNum"
                               class="item-select textInput search-input">
                    </div>
                </div>
                <div id="phone" style="display: none">
                    <div class="letter_bar"><span class="optionName">电话</span></div>
                    <div class="pos_relative">
                        <input type="number" maxlength="30" placeholder="请输入" value="" name="tbQyMemberInfoPO.shorMobile"
                               class="item-select textInput search-input">
                    </div>
                </div>
                <div id="phone1" style="display: none">
                    <div class="letter_bar"><span class="optionName">电话2</span></div>
                    <div class="pos_relative">
                        <input type="number" maxlength="30" placeholder="请输入" value="" name="tbQyMemberInfoPO.phone"
                               class="item-select textInput search-input">
                    </div>
                </div>
                <div id="address" style="display: none">
                    <div class="letter_bar"><span class="optionName">地址</span></div>
                    <div class="pos_relative">
                        <input type="text" maxlength="100" placeholder="请输入" value="" name="tbQyMemberInfoPO.address"
                               class="item-select textInput search-input">
                    </div>
                </div>
                <div id="birthday" style="display: none">
                    <div class="letter_bar"><span class="optionName">阳历生日</span></div>
                    <div class="pos_relative">
                        <input type="text" maxlength="100" placeholder="请选择" value="" name="tbQyMemberInfoPO.birthday"
                               class="item-select dateInput">
                    </div>
                </div>
                <div id="birthday2" style="display: none">
                    <div class="letter_bar"><span class="optionName">农历生日</span></div>
                    <div class="pos_relative">
                        <input type="text" maxlength="100" placeholder="请选择" name="tbQyMemberInfoPO.lunarCalendar"
                               class="item-select timeInput">
                    </div>
                </div>
                <div id="company" style="display: none">
                    <div class="letter_bar"><span class="optionName">单位名称</span></div>
                    <div class="pos_relative">
                        <input type="text" id="companyName" placeholder="请输入" maxlength="200" value=""
                               name="tbQyMemberInfoPO.companyName" class="item-select textInput search-input">
                    </div>
                </div>
                <div id="remarkId" style="display: none">
                    <div class="letter_bar"><span class="optionName">备注</span></div>
                    <div class="ask_textarea_content">
                        <div class="ask_textarea_content_inner">
                            <textarea placeholder="请输入备注信息"
                                      style="width: 100%; height: 120px; padding: 0px; max-height: 379.8px; min-height: 32px;"
                                      name="tbQyMemberInfoPO.remark" class="item-select textInput" index="0"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form_btns mt10" id="btn_div" style="display:none">
                <div class="inner_form_btns pb10">
                    <div class="fbtns flexbox">
                        <a class="fbtn btn flexItem" id="formBtn">下一步：扫码关注</a>
                    </div>
                </div>
            </div>
            <div class="form_btns mt10" id="btn_divChildren" style="display:none">
                <div class="inner_form_btns pb10">
                    <div class="fbtns flexbox">
                        <a id="showAndHide" class="fbtn btn flexItem" href="javascript:;">下一步：填写孩子信息</a>
                    </div>
                </div>
            </div>

            <div class="form_btns mt10" id="btn_red" style="display:none">
                <div class="inner_form_btns pb10">
                    <div class="fbtns flexbox">
                        <a class="fbtn gray_btn flexItem" href="#">该邀请页面已过期</a>
                    </div>
                </div>
            </div>
            <div id="remind" class="mt20 mb20">
                <div class="suxing">
                    <i class="fa fa-clock-o"></i>有效期至：<span class="detailStart" id="stopTime" style="width:45%;"></span>
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
                    <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20" width="20">资料已提交，请扫码关注</p>
					<p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
               </div>
               <div class="inter_success2 mb20" style="display:none;">
                    <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20" width="20">资料已提交，请扫码关注</p>
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
	<script>
        var resourceURL = "${resourceURL}";
        var CommitFormUrl = "${baseURL}/open/memberAction!ajaxAddEdu.action";
        var isEdu = true;
	</script>
    <script src="${baseURL}/themes/open/member/add.js?ver=<%=jsVer%>"></script>
</body>
</html>