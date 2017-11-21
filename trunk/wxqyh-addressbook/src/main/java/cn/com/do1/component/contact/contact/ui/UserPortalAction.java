package cn.com.do1.component.contact.contact.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.util.ErrorCodeModule;
import cn.com.do1.component.util.User2CodeUtil;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class UserPortalAction extends WxqyhPortalBaseAction {
	private final static transient Logger logger = LoggerFactory.getLogger(UserPortalAction.class);

	/**
	 * 生成用户信息的code
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void doGetCode() throws Exception, BaseException{
		UserInfoVO user = getUser();
		addJsonObj("authCode", User2CodeUtil.managerToCode(user.getUserId()));
		addJsonObj("isVip", isVip(user));
	}
}
