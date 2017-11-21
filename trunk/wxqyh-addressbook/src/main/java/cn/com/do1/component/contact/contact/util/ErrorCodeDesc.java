/*
 * Copyright © 2014 广州市道一信息技术有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.util.ErrorCodeModule;

/**
 * 错误码
 * @author Sun Qinghai
 * @ 16-5-30
 */
public enum ErrorCodeDesc {

    /**
     * 通讯录模块
     */
	USER_NULL(ErrorCodeModule.ADDRESS_BOOK+"001","用户不存在"),
	USER_UN_VISIBLE(ErrorCodeModule.ADDRESS_BOOK+"002","尊敬的用户，你不在应用的可见范围，请联系管理员！"),


	TAG_NULL(ErrorCodeModule.ADDRESS_BOOK+"301","标签不存在"),
	TAG_IS_REPEAT(ErrorCodeModule.ADDRESS_BOOK+"302","该名称已存在，请使用其它名称。"),
	TAG_STATUS_ERROR(ErrorCodeModule.ADDRESS_BOOK+"303","标签状态不合法。"),
	TAG_REF_NOT_NULL(ErrorCodeModule.ADDRESS_BOOK+"304","不能删除非空的标签。"),
	TAG_NOT_USE(ErrorCodeModule.ADDRESS_BOOK+"305","体验号不能使用此功能。"),
	TAG_RANG_ERROR(ErrorCodeModule.ADDRESS_BOOK+"306","标签可使用范围不合法。"),
	TAG_SYNC_OUT(ErrorCodeModule.ADDRESS_BOOK+"320","今日同步次数已用完，vip用户每天可以同步"+TagStaticUtil.TAG_SYNC_TIME_VIP+"次"),
	TAG_SYNC_OUT_VIP(ErrorCodeModule.ADDRESS_BOOK+"321","今日同步次数已用完"),
	TAG_SYNC_UNBINDING(ErrorCodeModule.ADDRESS_BOOK+"322","系统正在从企业微信同步标签，你当天仅余%s次同步操作</br>该账号还未绑定通讯录用户，可在右上角账号信息里为该账户绑定通讯录，绑定通讯录用户后可以收到同步完成通知"),
	TAG_SYNC_BINDING(ErrorCodeModule.ADDRESS_BOOK+"323","系统正在从企业微信同步标签，你当天仅余%s次同步操作</br>同步完成后会向该账号绑定的通讯录用户推送消息，请等待"),
	//私有化提示，不需要过滤次数
	TAG_SYNC_BINDING_PRIVATE(ErrorCodeModule.ADDRESS_BOOK+"323","系统正在从企业微信同步标签</br>同步完成后会向该账号绑定的通讯录用户推送消息，请等待"),
	TAG_SYNC_ING(ErrorCodeModule.ADDRESS_BOOK+"324","系统正在从企业微信同步标签，同步完成后会向该账号绑定的通讯录用户推送消息，请等待"),
	TAG_SYNC_SUCCEED(ErrorCodeModule.ADDRESS_BOOK+"325","标签已同步完成"),
	TAG_SYNC_ERROR(ErrorCodeModule.ADDRESS_BOOK+"326","标签同步出现异常"),

	MEMBER_IS_DELE(ErrorCodeModule.ADDRESS_BOOK+"401","对不起,该邀请已删除"),
	MEMEBER_IS_NO_FIND(ErrorCodeModule.ADDRESS_BOOK+"402","对不起,您还没有成员邀请设置"),
	MEMBER_IS_DISABLE(ErrorCodeModule.ADDRESS_BOOK+"403","当前邀请单已被禁用，请联系相关人员操作！"),
	MEMBER_IS_ONLY_VIP(ErrorCodeModule.ADDRESS_BOOK+"404","邀请单功能只限VIP用户使用！"),

	ORG_IS_NO_FIND(ErrorCodeModule.ADDRESS_BOOK+"501","查询到机构不存在！"),

	HAS_HAND_WRITE(ErrorCodeModule.ADDRESS_BOOK+"601","该用户的手写签名已存在"),

	USER_NO_FIND(ErrorCodeModule.ADDRESS_BOOK+"701","查询不到该用户"),

	POSITION_EXIST(ErrorCodeModule.ADDRESS_BOOK+"801","职位已存在"),
	POSITION_TYPE_EXIST(ErrorCodeModule.ADDRESS_BOOK+"802","职位类型已存在"),
	POSITION_HAS_EXIST_PERSON(ErrorCodeModule.ADDRESS_BOOK+"803","职位下已存在人员，无法删除"),
	POSITION_IS_NULL(ErrorCodeModule.ADDRESS_BOOK+"804","职位不存在"),
	POSITION_REPEAT(ErrorCodeModule.ADDRESS_BOOK+"805","职位名称重复"),
	POSITION_REPEAT_MAX_LENGTH(ErrorCodeModule.ADDRESS_BOOK+"806","职位名称超过最大长度"),
	POSITION_DESCRIBE_MAX_LENGTH(ErrorCodeModule.ADDRESS_BOOK+"807","职位描述超过最大长度"),

	ORG_IS_NULL(ErrorCodeModule.ADDRESS_BOOK+"901", "机构为空"),
	BEYOND_MAX_SIZE(ErrorCodeModule.ADDRESS_BOOK+"902", "pagesize超出了最大长度"),

	system_error(ErrorCodeModule.system_error,ErrorCodeModule.system_error_msg)
	;

	// 定义私有变量
    private String code ;
    private String desc ;

    // 构造函数，枚举类型只能为私有
    private ErrorCodeDesc(String _nCode, String _nDesc) {
        this.code = _nCode;
        this.desc = _nDesc;
    }

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 要设置的 code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc 要设置的 desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
