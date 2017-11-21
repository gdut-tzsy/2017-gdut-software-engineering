/*
 * Copyright © 2015 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.errcodedictionary.ErrorTip;
import cn.com.do1.component.managesetting.managesetting.service.IManagesettingService;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.LoginCodeUtil;
import cn.com.do1.component.systemmgr.permission.service.impl.NonePasswordSupport;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhAuthUtil;
import cn.com.do1.component.util.org.OrgUtil;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.systemmgr.user.service.IUserService;
import cn.com.do1.component.util.LoginFailUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.ILoginProcesser;

/**
 * <p>Title: 登录验证</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-8-18
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
@ProcesserUnit(name = "logincheck")
public class LoginCheckUtil implements ILoginProcesser {
    private transient final static Logger logger = Logger.getLogger(LoginCheckUtil.class);
    private final static String wxqyhmanagerPrefix = "wxqyhmanager_";//后台管理员账号前缀
    private static IUserService userService = null;
	private final static String ADMIN = "admin";
	private static IManagesettingService managesettingService = DqdpAppContext.getSpringContext().getBean("managesettingService", IManagesettingService.class);

	/* （非 Javadoc）
	 * @see cn.com.do1.dqdp.core.permission.ILoginProcesser#afterProcess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void afterProcess(HttpServletRequest request, HttpServletResponse response,
			Authentication arg2) throws AuthenticationException {
		String j_username = arg2.getName();
		try {
			LoginFailUtil.removeLoginFailInfor(wxqyhmanagerPrefix+j_username, LoginFailUtil.USER_LOGIN_DATE, request, response);
		} catch (Exception e) {
			logger.error("LoginCheckUtil afterProcess error",e);
		}
		UserOrgVO userInfoVO;
		try {
			if(userService == null){
				userService = DqdpAppContext.getSpringContext().getBean("dqdpUserService", IUserService.class);
			}
			userService.updateUserLastLoginTime(j_username);
		} catch (Exception e) {
			logger.error("LoginCheckUtil afterProcess updateUserLastLoginTime error",e);
		} catch (BaseException e) {
			logger.error("LoginCheckUtil afterProcess updateUserLastLoginTime error",e);
		} finally {
			//更新企业最后操作日期
			userInfoVO = OrgUtil.updateOrgLastUseDayByMgrUser(j_username, WxqyhAppContext.getFistSourceIP(request));
		}
		if (userInfoVO != null) {
			//企微的体验号
			if (Configuration.IS_QIWEIYUN && Configuration.AUTO_CORPID.equals(userInfoVO.getCorpId())
					&& !Configuration.COMPANY_ORG_ID.equals(userInfoVO.getOrgId()) && !Configuration.EXPERIENCE_LOGIN_WHITE_USER.contains(j_username)) {
				throw new BadCredentialsException(Configuration.EXPERIENCE_LOGIN_TIPS);
			}
			//登录成功后发送登录消息
			managesettingService.loginSendMsg(userInfoVO);
		}
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.dqdp.core.permission.ILoginProcesser#exceptionProcess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Throwable)
	 */
	@Override
	public void exceptionProcess(HttpServletRequest request,
			HttpServletResponse response, Throwable arg2) {
		String j_username = request.getParameter("j_username");
		if (StringUtil.isNullEmpty(j_username)) {
			j_username = (String) request.getAttribute("_dqdp_login_username");
		}
		try {
			LoginFailUtil.resetLoginFailInfor(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
		} catch (Exception e) {
			logger.error("LoginCheckUtil exceptionProcess error",e);
		}
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.dqdp.core.permission.ILoginProcesser#preProcess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void preProcess(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String j_username = request.getParameter("j_username");
		//验证私有化的密钥是否合法，admin免过滤
		if (!Configuration.IS_QIWEIYUN && !ADMIN.equals(j_username) && !WxqyhAuthUtil.isAuthed()) {
			throw new BadCredentialsException(ErrorTip.AUTHORIZE_ERROR.toString());
		}
		HttpSession session = request.getSession();
		if(StringUtil.isNullEmpty(request.getParameter("j_password"))){
			try {
				request.setAttribute("_dqdp_login_username", LoginCodeUtil.getLoginCode(j_username, null));
				//request.setAttribute("_dqdp_login_password",session.getAttribute("weixin_login_password"));
				//session.removeAttribute("weixin_login_username");
				//session.removeAttribute("weixin_login_password");
				NonePasswordSupport.setUseNonePassword(true);
				return;
			} catch (BaseException e) {
				logger.error("LoginCheckUtil preProcess",e);
				ExceptionCenter.addException(e, "LoginCheckUtil preProcess @sqh ", j_username);
				throw new BadCredentialsException(e.getErrMsg());
			} catch (Exception e) {
				logger.error("LoginCheckUtil preProcess",e);
				ExceptionCenter.addException(e, "LoginCheckUtil preProcess @sqh ", j_username);
				throw new BadCredentialsException("请输入账号密码");
			}
		}
		/*else {
			request.setAttribute("_dqdp_login_username", j_username);
			NonePasswordSupport.setUseNonePassword(true);
		}*/
		String msg = null;
		try {
			msg = LoginFailUtil.authenLoginTime(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
		} catch (Exception e) {
			logger.error("LoginCheckUtil preProcess error",e);
		}
		if(!StringUtil.isNullEmpty(msg)){
			logger.error("LoginCheckUtil preProcess error "+msg);
			throw new BadCredentialsException(msg);
		}
	}

}
