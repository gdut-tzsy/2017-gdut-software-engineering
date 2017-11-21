/*
 * Copyright © 2015 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import java.util.List;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.dqdp.core.DqdpAppContext;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyFieldSettingVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;
import cn.com.do1.component.wxcgiutil.message.SendMessageConditionVO;

/**
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Ma Quanyang
 * @2015-7-28
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class SendAddressBookMessage {
	private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
	/**
	 * 发送通讯录查询人员信息
	 * @param conditionVO
	 * @param page
	 * @param userInfo
	 * @throws Exception
	 * @author Ma Quanyang
	 * @2015-7-28
	 * @version 1.0
	 */
	public static void sendPersonMessage(SendMessageConditionVO conditionVO, Pager page, String keyword, UserInfoVO userInfo) throws Exception, BaseException {
		if(AssertUtil.isEmpty(conditionVO)){
			throw new Exception("发送通讯录查询人员信息失败，基础发送条件为空。");
		}
		if(page.getTotalRows()==0){
			throw new Exception("发送通讯录查询人员信息失败，查询的人员信息为空。");
		}
		String url = conditionVO.getUrl();
		String summary = conditionVO.getSummary();
		String wxUserId = conditionVO.getWxUserId();
		String corpId = conditionVO.getCorpId();
		String agentCode = conditionVO.getAgentCode();
		String title = conditionVO.getTitle();
		String content = conditionVO.getContent();
		
		if(page.getTotalRows() == 1){//只发一个人的详细信息，直接连接进入详情页面
			TbQyUserInfoVO userVO = ((List<TbQyUserInfoVO>) page.getPageData()).get(0);
			if(userVO == null){
				throw new Exception("发送通讯录查询人员信息失败，查询的人员信息为空。");
			}
			if(AssertUtil.isEmpty(title)){//处理标题
				//title = "您正在查询的是："+userVOList.get(0).getPersonName();
				title = "您正在查询的是："+keyword;
			}
			if(AssertUtil.isEmpty(url)){//处理图文连接
				url = Configuration.WX_PORT + "/jsp/wap/addressbook/user_detail.jsp?targetUser="+userVO.getUserId();
			}
			if(AssertUtil.isEmpty(summary)){//处理正文
				List<TbQyFieldSettingVO> fieldList = contactService.findTbQyFieldSettingVOListByOrgId(userInfo.getOrgId());
				conditionVO.setFieldList(fieldList);
				UserInfoVO vo = contactService.getUserInfoByUserId(userVO.getUserId());
				summary = setSummaryByIsDisplay(userVO,vo,fieldList);
			}
		}else{//查询到多个时
			if(AssertUtil.isEmpty(title)){//处理标题
				title = "通讯录搜索结果";
			}
			if(AssertUtil.isEmpty(url)){//处理图文连接
				url = Configuration.WX_PORT + "/jsp/wap/addressbook/user_list.jsp?type=search&keyWordForSearch="+ java.net.URLEncoder.encode(content,"utf-8");
				//url = Configuration.WX_PORT + "/jsp/wap/addressbook/user_list.jsp?type=search&keyWordForList="+content;
				
			}
			if(AssertUtil.isEmpty(summary)){//处理正文
				StringBuffer userName = new StringBuffer();
				List<TbQyUserInfoVO> userVOList = (List<TbQyUserInfoVO>) page.getPageData();
				for(int i =0; i < userVOList.size(); i++){
					TbQyUserInfoVO userVOSummaryInfoVO = userVOList.get(i);
					if(!AssertUtil.isEmpty(userVOSummaryInfoVO)){
						userName.append("、" + userVOSummaryInfoVO.getPersonName());
					}
				}
				if (userName.length() > 0) {
					userName = userName.deleteCharAt(0);
				}
				long total = page.getTotalRows();
				summary = "共搜索到"+userName.toString()+(total>page.getPageSize()?"等":"共")+total+"个结果，请点击查看详情";
			}
		}
		try{
			NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
			newsMessageVO.setTouser(wxUserId);
			newsMessageVO.setDuration("0");
			newsMessageVO.setTitle(title);
			newsMessageVO.setDescription(summary);
			newsMessageVO.setUrl(url);
			newsMessageVO.setCorpId(corpId);
			newsMessageVO.setAgentCode(agentCode);
			//newsMessageVO.setOrgId(agentCode);
			WxMessageUtil.sendNewsMessage(newsMessageVO);
		} catch (Exception e) {
			throw new Exception("发送通讯录查询人员信息失败，corpId："+"corpId", e);
		}
		
	}

	/**
	 * 只发送信息
	 * @param conditionVO
	 * @throws Exception
	 * @author Ma Quanyang
	 * @2015-7-30
	 * @version 1.0
	 */
	public static void sendFailMessage(SendMessageConditionVO conditionVO) throws Exception{
		if(AssertUtil.isEmpty(conditionVO)){
			throw new Exception("发送通讯录查询人员信息失败，基础发送条件为空。");
		}
		String url = conditionVO.getUrl();
		String summary = conditionVO.getSummary();
		String wxUserId = conditionVO.getWxUserId();
		String corpId = conditionVO.getCorpId();
		String agentCode = conditionVO.getAgentCode();
		String title = conditionVO.getTitle();
		try{
			NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
			newsMessageVO.setTouser(wxUserId);
			newsMessageVO.setDuration("0");
			newsMessageVO.setTitle(title);
			newsMessageVO.setDescription(summary);
			newsMessageVO.setUrl(url);
			newsMessageVO.setCorpId(corpId);
			newsMessageVO.setAgentCode(agentCode);
			//newsMessageVO.setOrgId(agentCode);
			WxMessageUtil.sendNewsMessage(newsMessageVO);
		} catch (Exception e) {
			throw new Exception("发送通讯录查询人员失败信息失败，corpId："+"corpId", e);
		}
	}
	
	/**
	 * 判断判断手机号码、电话、邮箱哪些可以显示
	 * @param userVO
	 * @param vo
	 *@param fieldList  @return
	 * @author Ma Quanyang
	 * @2015-8-5
	 * @version 1.0
	 */
	public static String setSummaryByIsDisplay(TbQyUserInfoVO userVO, UserInfoVO vo, List<TbQyFieldSettingVO> fieldList){
		StringBuffer summary = new StringBuffer("姓名："+(!AssertUtil.isEmpty(userVO.getPersonName())?userVO.getPersonName():""));
		/*String nickNameIsEdit = ContactUtil.IS_EDIT_0;
		String addressIsEdit = ContactUtil.IS_EDIT_0;*/
		String mobileIsDisplay = ContactUtil.IS_DISPLAY_0;
		String shorMobileIsDisplay = ContactUtil.IS_DISPLAY_0;
//		String phoneIsEdit = ContactUtil.IS_EDIT_0;
		String emailIsDisplay = ContactUtil.IS_DISPLAY_0;
		/*String qqNumIsEdit = ContactUtil.IS_EDIT_0;
		String birthdayIsEdit = ContactUtil.IS_EDIT_0;
		String lunarCalendarIsEdit = ContactUtil.IS_EDIT_0;
		String remindTypeIsEdit = ContactUtil.IS_EDIT_0;*/
		if(!AssertUtil.isEmpty(fieldList) && fieldList.size() > 0){
			for(TbQyFieldSettingVO fieldVO : fieldList){
				//控制那些内容在页面上需要
				/*if(ContactUtil.FIELD_NAME_1.equals(fieldVO.getField())){//昵称
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						nickNameIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_2.equals(fieldVO.getField())){//地址
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						addressIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else*/
				if(ContactUtil.FIELD_NAME_3.equals(fieldVO.getField())){//手机号码
					if(ContactUtil.IS_DISPLAY_1.equals(fieldVO.getIsDisplay())){//不可编辑
						mobileIsDisplay = ContactUtil.IS_DISPLAY_1;
					}
				}else if(ContactUtil.FIELD_NAME_4.equals(fieldVO.getField())){//电话1
					if(ContactUtil.IS_DISPLAY_1.equals(fieldVO.getIsDisplay())){//不可编辑
						shorMobileIsDisplay = ContactUtil.IS_DISPLAY_1;
					}
				}else if(ContactUtil.FIELD_NAME_6.equals(fieldVO.getField())){//邮箱可编辑
					if(ContactUtil.IS_DISPLAY_1.equals(fieldVO.getIsDisplay())){//不可编辑
						emailIsDisplay = ContactUtil.IS_DISPLAY_1;
					}
				}
				/*else if(ContactUtil.FIELD_NAME_5.equals(fieldVO.getField())){//电话2
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						phoneIsEdit = ContactUtil.IS_EDIT_1;
					}
				}
				else if(ContactUtil.FIELD_NAME_7.equals(fieldVO.getField())){//qq号码可编辑
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						qqNumIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_8.equals(fieldVO.getField())){//阳历生日可编辑
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						birthdayIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_9.equals(fieldVO.getField())){//农历生日
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						lunarCalendarIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_10.equals(fieldVO.getField())){//生日提醒
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						remindTypeIsEdit = ContactUtil.IS_EDIT_1;
					}
				}*/
			}
		}
		/*if(ContactUtil.IS_EDIT_0.equals(nickNameIsEdit)){//昵称
			
		}
		if(ContactUtil.IS_EDIT_0.equals(addressIsEdit)){//地址
			
		}*/
		TbQyUserInfoVO user = null;
		if(ContactUtil.IS_DISPLAY_0.equals(mobileIsDisplay)){//手机号码
			try{
				if(user == null){
					user = contactService.findUserInfoByUserId(userVO.getUserId());
					//保密
					user = SecrecyUserUtil.secrecyTbUserInfo(user);
				}
			} catch (BaseException e) {
			} catch (Exception e) {
			}
			if(user != null){
				summary.append("\r\n手机："+(!AssertUtil.isEmpty(user.getMobile())?user.getMobile():""));
			}

		}

		if(ContactUtil.IS_DISPLAY_0.equals(shorMobileIsDisplay)){//电话1
			try{
				if(user == null){
					user = contactService.findUserInfoByUserId(userVO.getUserId());
					//保密
					user = SecrecyUserUtil.secrecyTbUserInfo(user);
				}
			} catch (BaseException e) {
			} catch (Exception e) {
			}
			if(user != null){
				summary.append("\r\n电话："+(!AssertUtil.isEmpty(user.getShorMobile())?user.getShorMobile():""));
			}
		}
		/*if(ContactUtil.IS_EDIT_0.equals(phoneIsEdit)){//电话2
			
		}*/
		if(ContactUtil.IS_DISPLAY_0.equals(emailIsDisplay)){//邮箱
			try{
				if(user == null){
					user = contactService.findUserInfoByUserId(userVO.getUserId());
					//保密
					user = SecrecyUserUtil.secrecyTbUserInfo(user);
				}
			} catch (BaseException e) {
			} catch (Exception e) {
			}
			if(user != null){
				summary.append("\r\n邮箱："+(!AssertUtil.isEmpty(user.getEmail())?user.getEmail():""));
			}
		}
		/*if(ContactUtil.IS_EDIT_0.equals(qqNumIsEdit)){//qq号码
			
		}
		if(ContactUtil.IS_EDIT_0.equals(birthdayIsEdit)){//阳历生日
			
		}
		if(ContactUtil.IS_EDIT_0.equals(lunarCalendarIsEdit)){//农历生日
			
		}
		if(ContactUtil.IS_EDIT_0.equals(remindTypeIsEdit)){//生日提醒
			
		}*/
		summary.append("\r\n职位："+(!AssertUtil.isEmpty(userVO.getPosition())?userVO.getPosition():"")+
				"\r\n部门："+(!AssertUtil.isEmpty(vo.getDepartmentNames())?vo.getDepartmentNames():""));
		return summary.toString();
	}
}
