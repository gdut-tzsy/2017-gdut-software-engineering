package cn.com.do1.component.contact.contact.ui;

import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.util.ErrorCodeModule;
import cn.com.do1.component.util.User2CodeUtil;
import cn.com.do1.component.util.WxqyhBaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class ManagerUserAction extends WxqyhBaseAction {
	private final static transient Logger logger = LoggerFactory.getLogger(ManagerUserAction.class);

	/**
	 * 生成用户信息的code
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void doGetCode() throws Exception, BaseException{
		UserOrgVO org = getUser();
		String userId = org.getUserName();
		addJsonObj("authCode", User2CodeUtil.managerToCode(userId));
		addJsonObj("isVip", isVip(org));
	}
}
