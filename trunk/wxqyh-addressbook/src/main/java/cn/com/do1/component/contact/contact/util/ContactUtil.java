/*
 * Copyright © 2015 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.util.ContactDictUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.sms.sendsms.util.MD5Util;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Ma Quanyang
 * @version 1.0 修订历史： 日期       作者  :马权阳      参考         描述:广州市道一信息技术有限公司
 * @date 2016 -12-28
 * @2015-6-1
 */
public class ContactUtil extends ContactDictUtil {

	/**
	 * 生日祝福提醒方式(0:按农历)
	 */
	public final static String REMIND_TYPE_ZERO = "0";
	/**
	 * 生日祝福提醒方式(1:按阳历)
	 */
	public final static String REMIND_TYPE_ONE = "1";
	
	/**
	 * 备注
	 */
	public final static String FIELD_NAME_0 = "mark";
	/**
	 * 昵称
	 */
	public final static String FIELD_NAME_1 = "nickName";
	/**
	 * 地址
	 */
	public final static String FIELD_NAME_2 = "address";
	/**
	 * 手机号码
	 */
	public final static String FIELD_NAME_3 = "mobile";
	/**
	 * 电话1
	 */
	public final static String FIELD_NAME_4 = "shorMobile";
	/**
	 * 电话2
	 */
	public final static String FIELD_NAME_5 = "phone";
	/**
	 * 邮箱
	 */
	public final static String FIELD_NAME_6 = "email";
	/**
	 * qq号码
	 */
	public final static String FIELD_NAME_7 = "qqNum";
	/**
	 * 阳历生日
	 */
	public final static String FIELD_NAME_8 = "birthday";
	/**
	 * 农历生日
	 */
	public final static String FIELD_NAME_9 = "lunarCalendar";
	/**
	 * 生日提醒
	 */
	public final static String FIELD_NAME_10 = "remindType";
	/**
	 * 入职时间
	 */
	public final static String FIELD_NAME_11 = "entryTime";
	/**
	 * 微信号
	 */
	public final static String FIELD_NAME_12 = "weixinNum";
	/**
	 * 职位
	 */
	public final static String FIELD_NAME_13 = "position";
	
	/**
	 * 显示
	 */
	public final static String IS_DISPLAY_0 = "0";
	/**
	 * 不显示
	 */
	public final static String IS_DISPLAY_1 = "1";
	/**
	 * 允许员工可自行修改
	 */
	public final static String IS_EDIT_0 = "0";
	/**
	 * 不允许员工可自行修改
	 */
	public final static String IS_EDIT_1 = "1";

	/**
	 * 用户id分隔符
	 */
	private final static String USER_ID_SPLIT = "_";

	/**
	 * 用户头像默认值
	 */
	public final static String DEFAULT_HEAD_PIC = "0";
	/**
	 * 获取userId
	 * @param corpId 企业信息
	 * @param wxUserId 账号
	 * @return 返回数据
	 * @throws UnsupportedEncodingException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -10-24
	 */
	public static String getUserId(String corpId, String wxUserId) throws UnsupportedEncodingException {
		return MD5Util.encrypt(corpId + USER_ID_SPLIT + wxUserId);
	}

	/**
	 * 获取用户部门管理id
	 * @param userId 用户id
	 * @param deptId 部门id
	 * @return 返回数据
	 * @throws UnsupportedEncodingException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	public static String getUserDeptRefId(String userId, String deptId) throws UnsupportedEncodingException {
		return MD5Util.encrypt(userId + USER_ID_SPLIT + deptId);
	}
	/**
	 * 获取两个人员组的交集，如果第二个为空，直接返回第一个
	 * @param one
	 * @param two
	 * @return
	 * @throws Exception
	 * @author sunqinghai
	 * @date 2017 -2-14
	 */
	public static String[] getIntersectionByTwo(String one, String two) throws Exception {
		if(StringUtil.isNullEmpty(one)){
			return null;
		}
		else if(StringUtil.isNullEmpty(two)){
			return one.split("\\|");
		}

		String[] oneUserIds = one.split("\\|");
		String[] twoUserIds = two.split("\\|");
		Set oneSet = new HashSet(one.length());
		for (String id : oneUserIds) {
			oneSet.add(id);
		}
		List<String> ids = new ArrayList<String>(one.length());
		for (String id : twoUserIds) {
			if (oneSet.contains(id)) {
				ids.add(id);
			}
		}
		return ListUtil.collToArrays(ids);
	}

	/**
	 * 根据微信用户设置本地用户信息
	 * @param tbQyUserInfoPO
	 * @param user
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user) {
		setUserInfoByWxUser(tbQyUserInfoPO, user, tbQyUserInfoPO);
	}

	/**
	 * 根据微信用户设置本地用户信息，如果微信的手机/邮箱为空，默认使用源用户信息的手机/邮箱
	 * @param tbQyUserInfoPO 用户信息
	 * @param user 微信用户
	 * @param sourceUser 源数据
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, TbQyUserInfoPO sourceUser) {
		setUserInfoByWxUser(tbQyUserInfoPO, user, sourceUser.getMobile(), sourceUser.getEmail(), sourceUser.getWeixinNum(), sourceUser.getUserStatus());
	}

	/**
	 * 根据微信用户设置本地用户信息，如果微信的手机/邮箱为空，默认使用源用户信息的手机/邮箱
	 * @param tbQyUserInfoPO 用户信息
	 * @param user 微信用户
	 * @param sourceUser 源数据
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, TbQyUserInfoVO sourceUser) {
		setUserInfoByWxUser(tbQyUserInfoPO, user, sourceUser.getMobile(), sourceUser.getEmail(), sourceUser.getWeixinNum(), sourceUser.getUserStatus());
	}

	/**
	 * 根据微信用户设置本地用户信息，如果微信的手机/邮箱为空，默认使用源用户信息的手机/邮箱
	 * @param tbQyUserInfoPO 用户信息
	 * @param user 微信用户
	 * @param sourceUser 源数据
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, UserInfoVO sourceUser) {
		setUserInfoByWxUser(tbQyUserInfoPO, user, sourceUser.getMobile(), null, null, sourceUser.getUserStatus());
	}

	/**
	 * 根据微信用户设置本地用户信息，如果微信的手机/邮箱/微信号为空，默认使用源用户信息的手机/邮箱/微信号
	 * @param tbQyUserInfoPO
	 * @param user
	 * @param mobile
	 * @param email
	 * @param weixinId
	 * @param userStatus
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, String mobile, String email, String weixinId, String userStatus) {
		//如果用户的性别不符合规则 性别。1表示男性，2表示女性
		if (!StringUtil.isNullEmpty(user.getGender()) && !USER_SEX_MAN.equals(user.getGender()) && !USER_SEX_WOMAN.equals(user.getGender())) {
			user.setGender(null);
		}
		tbQyUserInfoPO.setPersonName(user.getName());
		tbQyUserInfoPO.setWxUserId(user.getUserid());
		tbQyUserInfoPO.setSex(user.getGender());
		if (AssertUtil.isEmpty(user.getMobile())) {
			tbQyUserInfoPO.setMobile(mobile);
		}
		else {
			tbQyUserInfoPO.setMobile(user.getMobile());
		}
		if (AssertUtil.isEmpty(user.getEmail())) {
			tbQyUserInfoPO.setEmail(email);
		}
		else {
			tbQyUserInfoPO.setEmail(user.getEmail());
		}
		if (AssertUtil.isEmpty(user.getWeixinid())) {
			tbQyUserInfoPO.setWeixinNum(weixinId);
		}
		else {
			tbQyUserInfoPO.setWeixinNum(user.getWeixinid());
		}
		tbQyUserInfoPO.setPosition(user.getPosition());
		setUserStatus(tbQyUserInfoPO, user, userStatus);
		tbQyUserInfoPO.setHeadPic(getWeixinUserHeadPic(user.getAvatar()));
	}

	/**
	 * 根据微信用户设置本地用户信息，如果微信的手机/邮箱为空，默认使用源用户信息的手机/邮箱
	 * 忽略为空的数据
	 * @param tbQyUserInfoPO 用户信息
	 * @param user 微信用户
	 * @param sourceUser 源数据
	 * @author sunqinghai
	 * @date 2017 -4-13
	 */
	public static void setUserInfoIgnoreEmptyByWxUser (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, TbQyUserInfoPO sourceUser) {
		setUserInfoByWxUser(tbQyUserInfoPO, user, sourceUser.getMobile(), sourceUser.getEmail(), sourceUser.getWeixinNum(), sourceUser.getUserStatus());
		if (StringUtil.isNullEmpty(tbQyUserInfoPO.getPersonName())) {
			tbQyUserInfoPO.setPersonName(sourceUser.getPersonName());
		}
		if (StringUtil.isNullEmpty(tbQyUserInfoPO.getSex())) {
			tbQyUserInfoPO.setSex(sourceUser.getSex());
		}
		if (StringUtil.isNullEmpty(tbQyUserInfoPO.getPosition())) {
			tbQyUserInfoPO.setPosition(sourceUser.getPosition());
		}
		if (StringUtil.isNullEmpty(user.getAvatar())) {
			tbQyUserInfoPO.setHeadPic(sourceUser.getHeadPic());
		}
	}

	/**
	 * 设置微信的状态信息
	 * @param tbQyUserInfoPO 存放转换后用户状态的对象
	 * @param user 微信用户信息
	 * @param localUserStatus 原始的用户状态
	 * @author sunqinghai
	 * @date 2017 -6-14
	 */
	public static void setUserStatus (TbQyUserInfoPO tbQyUserInfoPO, WxUser user, String localUserStatus) {
		if (StringUtil.isNullEmpty(user.getStatus())) {
			tbQyUserInfoPO.setUserStatus(StringUtil.isNullEmpty(localUserStatus) ? USER_STAtUS_INIT : localUserStatus);
		}
		//微信数据关注状态: 1=已关注，2=已冻结，4=未关注
		//本地数据0表示新增数据，2表示已关注，1表述取消关注
		else if(WX_USER_STAtUS_FOLLOW.equals(user.getStatus())){
			if (StringUtil.isNullEmpty(localUserStatus)) {
				tbQyUserInfoPO.setUserStatus(USER_STAtUS_INIT);
			}
			else if(!USER_STAtUS_FOLLOW.equals(localUserStatus)){
				tbQyUserInfoPO.setUserStatus(USER_STAtUS_FOLLOW);
				tbQyUserInfoPO.setFollowTime(new Date());
			}
		}
		else{
			if (StringUtil.isNullEmpty(localUserStatus)) {
				tbQyUserInfoPO.setUserStatus(USER_STAtUS_INIT);
			}
			else if(USER_STAtUS_FOLLOW.equals(localUserStatus)){
				tbQyUserInfoPO.setUserStatus(USER_STAtUS_CANCEL);
				tbQyUserInfoPO.setCancelTime(new Date());
			}
		}
	}
	/**
	 * 获取用户的头像，统一转为100
	 * @param avatar
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2017 -5-11
	 */
	public static String getWeixinUserHeadPic(String avatar) {
		return WxUserService.getWeixinUserHeadPic(avatar);
	}
}
