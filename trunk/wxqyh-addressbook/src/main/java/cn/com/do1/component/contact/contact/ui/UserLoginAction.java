package cn.com.do1.component.contact.contact.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.cooperation.cooperation.util.CoopUtil;
import cn.com.do1.component.cooperation.cooperation.vo.CooperationSettingVO;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.ExperienceAgentStatusUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.LoginCodeUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.component.util.org.OrgUtil;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.security.SecurityUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserLoginInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.cooperation.cooperation.model.TbQyCooperationUserPO;
import cn.com.do1.component.cooperation.cooperation.service.ICooperationService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.credit.credit.utils.CreditUtil;
import cn.com.do1.component.errcodedictionary.ErrorTip;
import cn.com.do1.component.loginlog.loginlog.model.TbLoginLogPO;
import cn.com.do1.component.loginlog.loginlog.thread.LoginlogThread;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationInsertPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.systemmgr.user.model.TbDqdpUserPO;
import cn.com.do1.component.systemmgr.user.service.IUserService;
import cn.com.do1.component.util.memcached.CacheCookieTokenManager;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

public class UserLoginAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4903862582507575309L;
	private final static transient Logger logger = LoggerFactory
			.getLogger(UserLoginAction.class);
    private final static String wxqyhPortPrefix = "wxqyhport_";//个人网页版用户账号前缀
    private final static String wxqyhmanagerPrefix = "wxqyhmanager_";//后台管理员账号前缀
	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IExperienceapplicationService experienceapplicationService;
    private IUserService userService;							//操作用户属性类
	private String userAccount;//登录账号
	private String password; //密码，未加密
	//登录验证
	private String j_username;
	private String j_password;
	
	//微信登录绑定信息
	private String userName;
	//sessionid
	private String state;
	
	private ICooperationService cooperationService;
	
	public ICooperationService getCooperationService() {
		return cooperationService;
	}

	public void setCooperationService(ICooperationService cooperationService) {
		this.cooperationService = cooperationService;
	}

	public IContactService getContactService() {
		return contactService;
	}

    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    /**
	 * @param contactMgrService 要设置的 contactMgrService
	 */
    @Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}

	@Resource
    public void setUserService(IUserService userService) {
    	this.userService = userService;
    }
	
	/**
	 * 获取当前用户的账号信息
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-15
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "查询成功", faileMsg = "查询失败"))
	public void viewUserLoginInfo() throws Exception,BaseException{
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		TbQyUserInfoVO userinfo = contactService.findUserInfoByUserId(userInfo.getUserId());
		if(userinfo == null){
			throw new NonePrintException("200",ErrorTip.USER_NULL.toString());
		}
		addJsonObj("userInfo", userinfo);

		TbQyUserLoginInfoPO loginInfoPO = contactMgrService.getUserLoginByUserId(userInfo.getUserId());
		String userAccount = userinfo.getWxUserId();
		if(loginInfoPO != null){
			userAccount = loginInfoPO.getUserAccount();
		}
		addJsonObj("userAccount", userAccount);
		addJsonObj("localPort", Configuration.WEB_PAGE_PORT);
	}
	/**
	 * 验证登录账号信息
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "验证通过", faileMsg = "无效账号"))
	public void checkAccount() throws Exception,BaseException{
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());

		if(AssertUtil.isEmpty(userAccount)){
			throw new NonePrintException("200","请输入账号");
		}
		
		TbQyUserLoginInfoPO loginInfoPO = contactMgrService.getUserLoginByUserId(userInfo.getUserId());
		TbQyUserLoginInfoPO accountPO = contactService.getUserLoginByAccount(userAccount);
		if(loginInfoPO == null){
			if(accountPO != null ){
				throw new NonePrintException("200","已有人抢先了");
			}
		}
		else{
			if(accountPO != null && !userInfo.getUserId().equals(accountPO.getUserId())){
				throw new NonePrintException("200","已有人抢先了");
			}
		}
		setActionResult("0", "验证通过");
	}

	/**
	 * 编辑登录信息
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "设置成功", faileMsg = "设置失败"))
	public void editUserLoginInfo() throws Exception,BaseException{
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		String userId = userInfo.getUserId();
		if(AssertUtil.isEmpty(password)){
			throw new NonePrintException("200","请输入密码");
		}
		if(password.length()<6){
			throw new NonePrintException("200","请至少输入6个字符或者数字");
		}
		userService.validatePassword(password);
		
		if(userAccount==null){
			throw new NonePrintException("200","请输入账号");
		}
		userAccount = userAccount.trim();
		if(AssertUtil.isEmpty(userAccount)){
			throw new NonePrintException("200","请输入账号");
		}

		/*TbDqdpUserPO user = contactService.getDqdpUserByUserName(userAccount);
		if(user != null){
			throw new NonePrintException("200","此账号已经有人抢先使用了！");
		}*/
		TbQyUserLoginInfoPO loginInfoPO = contactMgrService.getUserLoginByUserId(userId);
		TbQyUserLoginInfoPO accountPO = contactService.getUserLoginByAccount(userAccount);
		String md5Password=SecurityUtil.getMd5Code(password);
		if(loginInfoPO == null){
			if(accountPO != null){
				throw new NonePrintException("200","此账号已经有人抢先使用了！");
			}
			loginInfoPO = new TbQyUserLoginInfoPO();
			loginInfoPO.setUserAccount(userAccount);
			loginInfoPO.setUserId(userId);
			loginInfoPO.setPassword(md5Password);
			loginInfoPO.setCreateTime(new Date());
			contactService.insertPO(loginInfoPO, true);//不用重新设置id
		}
		else{
			if(accountPO != null && !userId.equals(accountPO.getUserId())){
				throw new NonePrintException("200","此账号已经有人抢先使用了！");
			}
			loginInfoPO.setUserAccount(userAccount);
			loginInfoPO.setPassword(md5Password);
			loginInfoPO.setCreateTime(new Date());
			contactService.updatePO(loginInfoPO, true);
		}
		setActionResult("0", userAccount);
	}
	/**
	 * 验证企业微信网页版用户登录信息
	 * 
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void checkUser() throws Exception, BaseException {
		if(AssertUtil.isEmpty(j_username) || AssertUtil.isEmpty(j_password)){
			setActionResult("101", "亲爱的用户，请输入用户名和密码！");
			return;
		}
		userService.validatePassword(j_password);

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		//验证登录失败的次数
		String loginInfo = LoginFailUtil.authenLoginTime(wxqyhPortPrefix+j_username, "wxqyh_web_login_date", request, response);
		//如果不为空
		if(!AssertUtil.isEmpty(loginInfo)){
			logger.error("登录超过规定次数"+loginInfo);
			setActionResult("1", loginInfo);
			return;
		}
		//验证私有化的密钥是否合法
		if (!Configuration.IS_QIWEIYUN && !WxqyhAuthUtil.isAuthed()) {
			setActionResult(ErrorCodeModule.system_error, ErrorTip.AUTHORIZE_ERROR.toString());
			return;
		}

		//读取配置项 机构内人数超过配置则只允许搜索
		TbQyUserLoginInfoPO loginInfoPO = contactService.getUserLoginByAccount(j_username);
		if(loginInfoPO == null){
			//为了防止管理员账号因经过此步无法登录，如果记录登录失败信息，导致无法登录，因此此处不记录错误登录信息
			//LoginFailUtil.resetLoginFailInfor(wxqyhPortPrefix+j_username, "wxqyh_web_login_date", request, response);
			setActionResult("100", "亲爱的用户，请先在企微-->通讯录-->我的资料-->WEB账号管理 中设置您的登录信息！");
			return;
		}
		String md5Password=SecurityUtil.getMd5Code(j_password);
		if(!md5Password.equals(loginInfoPO.getPassword())){
			//只有密码错误时才会记录错误登录次数
			LoginFailUtil.resetLoginFailInfor(wxqyhPortPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
			setActionResult("103", "亲爱的用户，账号或者密码不正确，如果忘记了账号或者密码请直接在企业微信中重置密码！");
			return;
		}

		HttpSession session = ServletActionContext.getRequest().getSession();

		UserInfoVO userInfo = contactService.getUserInfoNoCacheByUserId(loginInfoPO.getUserId());
		if(userInfo == null){
			//只有密码错误时才会记录错误登录次数
			LoginFailUtil.resetLoginFailInfor(wxqyhPortPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
			setActionResult("104", ErrorTip.USER_NULL.toString());
			return;
		}
		//如果使用了集群
		session.setAttribute("userId", userInfo.getUserId());
		session.setAttribute("userName", userInfo.getPersonName());
		if(Configuration.IS_USE_MEMCACHED){//如果使用缓存
			String sessionToken = UUID32.getID();
			CookieUtil.addCookie(request, response, CacheCookieTokenManager.cookieKey, sessionToken, CacheCookieTokenManager.cookieAge);
			CacheCookieTokenManager.set(sessionToken, userInfo.getUserId());
			UserInfoVO cacheUser = CacheSessionManager.get(userInfo.getUserId());
			if(null == cacheUser){
				CacheSessionManager.set(userInfo.getUserId(), userInfo);
			}
			else{
				userInfo.setDeviceId(cacheUser.getDeviceId());
				CacheSessionManager.set(userInfo.getUserId(), userInfo);
			}
		}
		else{
			session.setAttribute("wxCorpId", userInfo.getCorpId());
		}
		try {
			LoginFailUtil.removeLoginFailInfor(wxqyhPortPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);

			CreditUtil.addCredits(userInfo, CreditUtil.LOGIN_AGNET_CODE, CreditUtil.LOGIN_OPERATE);//登陆添加积分
			//保存访问日志
			TbLoginLogPO po = new TbLoginLogPO();
			po.setIp(WxqyhAppContext.getSourceIP(request));
			po.setUserId(loginInfoPO.getUserId());
			po.setOrgId(userInfo.getOrgId());
			po.setPersonName(userInfo.getPersonName());
			po.setExt1(request.getHeader("User-Agent"));
			po.setType("web");
			LoginlogThread.add(po);
			//启动线程用start chenfeixiong 20141030
			//login.run();

			//更新企业最后操作日期
			OrgUtil.updateOrgLastUseDay(userInfo.getOrgId());

			//如果托管了通讯录才登录提醒
			if(WxAgentUtil.isTrustAgent(userInfo.getCorpId(), WxAgentUtil.getAddressBookCode())){
				StringBuffer content = new StringBuffer(userInfo.getPersonName());
				content.append("，你于").append(DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss")).append("登录了");
				if(Configuration.IS_QIWEIYUN){
					content.append("企微云平台");
				}
				else{
					DqdpOrgVO dqdpOrgVO = contactMgrService.getOrgByOrgId(userInfo.getOrgId());
					content.append(dqdpOrgVO.getOrgName());
				}
				content.append("个人网页版，如果不是本人操作请注意密码安全。");
				WxMessageUtil.sendTextMessage(userInfo.getWxUserId(),content.toString(),WxAgentUtil.getAddressBookCode(),
						userInfo.getCorpId(),userInfo.getOrgId());
			}
		} catch (Exception e) {
			logger.error("企微网页版用户登录验证，获取用户姓名失败", e);
			ExceptionCenter.addException(e,this);
		} catch (BaseException e) {
			logger.error("企微网页版用户登录验证，获取用户姓名失败", e);
			ExceptionCenter.addException(e,this);
		}
	}
	
	/**
	 * 单单重定向的方法
	 * 2014-1-27 李盛滔
	 * @throws Exception
	 * @throws BaseException
	 */
	public  void weixinWebLogin2()throws Exception,BaseException{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.sendRedirect(request.getContextPath()+"/qwweb/main.jsp");
	}
	
	/**
	 * 微信网页版登录方法(合作商登陆方法)
	 * 2014-1-27 李盛滔
	 * @throws Exception
	 * @throws BaseException
	 */
	public  void weixinWebLogin()throws Exception,BaseException{
		if(AssertUtil.isEmpty(j_username) || AssertUtil.isEmpty(j_password)){
			setActionResult("101", "亲爱的用户，请输入用户名和密码！");
			doJsonOut();
			return;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		userService.validatePassword(j_password);
		
		//验证登录失败的次数
		String loginInfo = LoginFailUtil.authenLoginTime(wxqyhPortPrefix+j_username, "wxqyh_web_login_date", request, response);
		//如果不为空
		if(!AssertUtil.isEmpty(loginInfo)){
			logger.error("登录超过规定次数"+loginInfo);
			setActionResult("1", loginInfo);
			doJsonOut();
			return;
		}
		
		//理论上合作商经过此方法后，账号密码都是正确 的
		
		//读取配置项 机构内人数超过配置则只允许搜索
		TbQyUserLoginInfoPO loginInfoPO = contactService.getUserLoginByAccount(j_username);
		String md5Password=SecurityUtil.getMd5Code(j_password);
		if(AssertUtil.isEmpty(loginInfoPO) || AssertUtil.isEmpty(md5Password) || !md5Password.equals(loginInfoPO.getPassword())){
			//如果万一错误，重定向到合作商登陆页面
			response.sendRedirect(request.getContextPath());
			return;
		}

    	HttpSession session = ServletActionContext.getRequest().getSession();
    	UserInfoVO userInfo = contactService.getUserInfoNoCacheByUserId(loginInfoPO.getUserId());
		//如果使用了集群
		//如果使用了集群
		session.setAttribute("userId", userInfo.getUserId());
		session.setAttribute("userName", userInfo.getPersonName());
		if(Configuration.IS_USE_MEMCACHED){//如果使用缓存
			String sessionToken = UUID32.getID();
			CookieUtil.addCookie(request, response, CacheCookieTokenManager.cookieKey, sessionToken, CacheCookieTokenManager.cookieAge);
			CacheCookieTokenManager.set(sessionToken, userInfo.getUserId());
			UserInfoVO cacheUser = CacheSessionManager.get(userInfo.getUserId());
			if(null == cacheUser){
				CacheSessionManager.set(userInfo.getUserId(), userInfo);
			}
			else{
				userInfo.setDeviceId(cacheUser.getDeviceId());
				CacheSessionManager.set(userInfo.getUserId(), userInfo);
			}
		}
		else{
			session.setAttribute("wxCorpId", userInfo.getCorpId());
		}
		setActionResult("0", "登录成功");
		LoginFailUtil.removeLoginFailInfor(wxqyhPortPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);

		//保存访问日志
		TbLoginLogPO po = new TbLoginLogPO();
		po.setIp(WxqyhAppContext.getSourceIP(request));
		po.setUserId(loginInfoPO.getUserId());
		po.setOrgId(userInfo.getOrgId());
		po.setPersonName(userInfo.getPersonName());
		po.setExt1(request.getHeader("User-Agent"));
		po.setType("web");
		LoginlogThread.add(po);
		//更新企业最后操作日期
		OrgUtil.updateOrgLastUseDay(userInfo.getOrgId());
		
		//如果托管了通讯录才登录提醒
		if(WxAgentUtil.isTrustAgent(userInfo.getCorpId(), WxAgentUtil.getAddressBookCode())){
			StringBuffer content = new StringBuffer(userInfo.getPersonName());
			content.append("，你于").append(DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss")).append("登录了");
			if(Configuration.IS_QIWEIYUN){
				content.append("企微云平台");
			}
			else{
				DqdpOrgVO dqdpOrgVO = contactMgrService.getOrgByOrgId(userInfo.getOrgId());
				content.append(dqdpOrgVO.getOrgName());
			}
			WxMessageUtil.sendTextMessage(userInfo.getWxUserId(),content.toString(),WxAgentUtil.getAddressBookCode(),
					userInfo.getCorpId(),userInfo.getOrgId());
		}
		response.sendRedirect(request.getContextPath()+"/qwweb/main.jsp");
	}
	/**
	 * 微信登录绑定账号
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-2-1
	 * @version 1.0
	 */
	public  void weixinLoginBinding(@InterfaceParam(name="loginId")String loginId)throws Exception,BaseException{
		try {
			if(StringUtil.isNullEmpty(loginId)){
				setActionResult("100", "亲爱的用户，你的请求不合法，请重试！");
				doJsonOut();
				return;
			}
			if(AssertUtil.isEmpty(j_username) || AssertUtil.isEmpty(j_password)){
				setActionResult("101", "亲爱的用户，请输入用户名和密码！");
				doJsonOut();
				return;
			}
			HttpSession session = ServletActionContext.getRequest().getSession();
			if(!session.getId().equals(state)){
				setActionResult("100", "亲爱的用户，页面已过期，请重新进入！");
				doJsonOut();
				return;
			}
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();

			TbQyUserWxloginInfoPO loginInfoPO = contactService.searchByPk(TbQyUserWxloginInfoPO.class, loginId);
			if(loginInfoPO == null){
				setActionResult("100", "亲爱的用户，你的请求不合法，请重试！");
				doJsonOut();
				return;
			}
			//验证登录失败的次数
			String loginInfo = LoginFailUtil.authenLoginTime(wxqyhmanagerPrefix+j_username, "wxqyh_web_login_date", request, response);
			//如果不为空
			if(!AssertUtil.isEmpty(loginInfo)){
				logger.error("登录超过规定次数"+loginInfo);
				setActionResult("1", loginInfo);
				doJsonOut();
				return;
			}

			TbDqdpUserPO user = contactService.getDqdpUserByUserName(j_username);
			if(user == null){
				logger.error("您还没有企微登录账号，请联系公司管理人员"+j_username);
				setActionResult("1", "您输入的企微登录账号不正确，如果您还没有企微账号，请联系公司管理员，或者将您的企业微信绑定到企微");
				doJsonOut();
				return;
			}
			String md5Password=SecurityUtil.getMd5Code(j_password);
			if(!md5Password.equals(user.getPassword())){
				//只有密码错误时才会记录错误登录次数
				LoginFailUtil.resetLoginFailInfor(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
				setActionResult("103", "亲爱的用户，账号或者密码不正确，请重试！");
				doJsonOut();
				return;
			}
			else{
				LoginFailUtil.removeLoginFailInfor(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
			}
			UserOrgVO org = contactService.getOrgByUserId(j_username);
			if(org == null){
				setActionResult("104", "亲爱的用户，你输入的账号不存在，请先创建此账号后再重新绑定！");
				doJsonOut();
				return;
			}
			if(!org.getCorpId().equals(loginInfoPO.getCorpId())){
				setActionResult("104", "亲爱的用户，你输入的账号不属于登录的企业微信，请重新输入此企业微信的账号，或者选择正确的企业微信登录！");
				doJsonOut();
				return;
			}
			
			/*byte[] encryptResult = AESEncryptUtil.encrypt(j_password.getBytes("utf-8"), Configuration.PASSWORD_KEY);
			String encryptResultStr = HardwareUtil.parseByte2HexStr(encryptResult);*/
			loginInfoPO.setUserAccount(j_username);
			loginInfoPO.setCreateTime(new Date());
			loginInfoPO.setPassword(md5Password);
			loginInfoPO.setPasswordType(ExperienceAgentStatusUtil.PASSWORD_TYPE_MD5);
			contactService.updatePO(loginInfoPO, true);

			//session.setAttribute("weixin_login_username", j_username);
			//session.setAttribute("weixin_login_password", j_password);
			addJsonObj("code", LoginCodeUtil.putLoginCode(j_username, null));
			setActionResult("0", "绑定成功");
		} catch (Exception e) {
			logger.error("绑定微信扫描登录失败", e);
			ExceptionCenter.addException(e,this);
			setActionResult("100", "系统出现异常，绑定登录账号失败，请稍后重试！");
		} catch (BaseException e) {
			logger.error("绑定微信扫描登录失败", e);
			ExceptionCenter.addException(e,this);
			setActionResult("100", "系统出现异常，绑定登录账号失败，请稍后重试！");
		}
		doJsonOut();
	}
	
	/**
	 * 微信登录账号密码重置
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-2-1
	 * @version 1.0
	 */
	public  void weixinLoginReset(@InterfaceParam(name="loginId")String loginId)throws Exception,BaseException{
		try {
			if(StringUtil.isNullEmpty(loginId)){
				setActionResult("100", "亲爱的用户，你的请求不合法，请重试！");
				doJsonOut();
				return;
			}
			if(AssertUtil.isEmpty(j_password)){
				setActionResult("101", "亲爱的用户，请输入密码！");
				doJsonOut();
				return;
			}
			HttpSession session = ServletActionContext.getRequest().getSession();
			if(!session.getId().equals(state)){
				setActionResult("100", "亲爱的用户，页面已过期，请重新进入！");
				doJsonOut();
				return;
			}
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			
			TbQyUserWxloginInfoPO loginInfoPO = contactService.searchByPk(TbQyUserWxloginInfoPO.class, loginId);
			if(loginInfoPO == null || StringUtil.isNullEmpty(loginInfoPO.getUserAccount())){
				setActionResult("100", "亲爱的用户，你的请求不合法，请重试！");
				doJsonOut();
				return;
			}
			j_username = loginInfoPO.getUserAccount();
			//验证登录失败的次数
			String loginInfo = LoginFailUtil.authenLoginTime(wxqyhmanagerPrefix+j_username, "wxqyh_web_login_date", request, response);
			//如果不为空
			if(!AssertUtil.isEmpty(loginInfo)){
				logger.error("登录超过规定次数"+loginInfo);
				setActionResult("1", loginInfo);
				doJsonOut();
				return;
			}

			TbDqdpUserPO user = contactService.getDqdpUserByUserName(j_username);
			if(user == null){
				logger.error("企业微信已经从企微云平台中删除"+loginInfoPO.getCorpId());
				setActionResult("1", "您的企业微信已经从企微云平台中删除，请重新将您的账号绑定到企微云平台");
				doJsonOut();
				return;
			}
			String md5Password=SecurityUtil.getMd5Code(j_password);
			if(!md5Password.equals(user.getPassword())){
				//只有密码错误时才会记录错误登录次数
				LoginFailUtil.resetLoginFailInfor(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
				setActionResult("103", "亲爱的用户，密码不正确，请重试！");
				doJsonOut();
				return;
			}
			else{
				LoginFailUtil.removeLoginFailInfor(wxqyhmanagerPrefix+j_username,  LoginFailUtil.USER_LOGIN_DATE, request, response);
			}
			//更新新的密码
			
			/*byte[] encryptResult = AESEncryptUtil.encrypt(j_password.getBytes("utf-8"), Configuration.PASSWORD_KEY);
			String encryptResultStr = HardwareUtil.parseByte2HexStr(encryptResult);
			loginInfoPO.setPassword(encryptResultStr);*/
			loginInfoPO.setPassword(md5Password);
			loginInfoPO.setPasswordType(ExperienceAgentStatusUtil.PASSWORD_TYPE_MD5);
			loginInfoPO.setCreateTime(new Date());
			contactService.updatePO(loginInfoPO, true);
			//addJsonObj("userName", user.getUserName());
			//session.setAttribute("weixin_login_username", user.getUserName());
			//session.setAttribute("weixin_login_password", j_password);
			addJsonObj("code", LoginCodeUtil.putLoginCode(j_username, null));
			setActionResult("0", "绑定成功");
		} catch (Exception e) {
			logger.error("绑定微信扫描登录失败", e);
			ExceptionCenter.addException(e,this);
			setActionResult("100", "系统出现异常，验证登录密码失败，请稍后重试！");
		} catch (BaseException e) {
			logger.error("绑定微信扫描登录失败", e);
			ExceptionCenter.addException(e,this);
			setActionResult("100", "系统出现异常，验证登录密码失败，请稍后重试！");
		}
		doJsonOut();
	}
	
	/**
	 * 个人版PC端应用菜单显示检测
	 * @throws Exception
	 * @throws BaseException
	 * @author Chen Feixiong
	 * 2014-11-12
	 */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
    public void checkApp() throws Exception, BaseException{
	 	UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		//放到session里面
		List<TbQyExperienceAgentPO> list;
		HttpSession session = ServletActionContext.getRequest().getSession();
		String experienceAgentKey="experienceAgent";
		list=(List<TbQyExperienceAgentPO>)session.getAttribute(experienceAgentKey);
		if (list== null) {//list== null
			//list=contactService.getAppByUserId(userId);//没有验证课示范的显示方法
			/**
			 * @author lishengtao
			 * 2015-6-9
			 * 修改根据可见范围显示
			 */
			Map<String,Object> map=new HashMap<String,Object>();
/*				map.put("corpId", userInfo.getCorpId());
			map.put("userinfos", userInfo.getWxUserId());//可见对象
			map.put("partys", userInfo.getWxDeptIds());//可见部门
*/				map.put("userInfo", userInfo);
			list=experienceapplicationService.getApp(map);
			session.setAttribute(experienceAgentKey, list);
		}

		if(list==null || list.size()==0){
			addJsonObj("isExist","1");
		}else{
			addJsonObj("isExist","2");
			addJsonObj("list",list);
		}
    }
 /**
	 * 后台应用菜单显示检测
	 * @throws Exception
	 * @throws BaseException
	 * @author Chen Feixiong
	 * 2014-11-13
	 */
@JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
public void checkAppByDqdp() throws Exception, BaseException{
	String user = DqdpAppContext.getCurrentUser().getUsername();
	UserOrgVO org = contactService.getOrgByUserId(user);
	//UserOrgVO org =  contactService.getOrgByUserId(user);
	if(user==null){
		addJsonObj("isExist","0");
	}else{
		TbDqdpOrganizationInsertPO po=experienceapplicationService.getOrgInsert(org.getOrgId());
		if(po==null){	//如果接入表没有该机构就直接显示全部
			addJsonObj("isExist","0");
		}else{
			List<TbQyExperienceAgentPO> list=experienceapplicationService.getAppByDqdpOrg(org.getCorpId());
			if(list==null || list.size()==0){
				addJsonObj("isExist","1");
			}else{
				addJsonObj("isExist","2");
				addJsonObj("list",list);
			}
		}
	}
}
	/**
	 * 退出登录
	 * 
	 * @author Sun Qinghai
	 * @2014-8-15
	 * @version 1.0
	 */
	public void logout()throws Exception,BaseException{
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
        	UserInfoVO userInfo = WxqyhAppContext.logoutCurrentUser(ServletActionContext.getRequest());
			
			CooperationSettingVO cooperationSettingVO=null;
			if(null != userInfo){	
				/**
				 * 2015-3-31 李盛滔
				 * 这里先获取信息后再移除
				 * 并且增加了异常抛出 throws Exception,BaseException
				 */
				//根据用户的orgId查找出所属的企业
				//获取登录用户属于的企业微信
				//判断企业微信是否属于合作商
				TbQyCooperationUserPO cupo=this.cooperationService.getCooperationUserByCorpid(userInfo.getCorpId());			
				if(!AssertUtil.isEmpty(cupo)){
					//如果不为空,获取合作商的信息
					cooperationSettingVO = cooperationService.getCooperationSettingVO(cupo.getUid());
				}
			}
			setActionResult("0", "退出登录成功");
			
			/**
			 * 2015-3-31 李盛滔
			 * 因为集群问题，修改不用session管理
			 * 这里先获取信息后再移除
			 */
			if(cooperationSettingVO!=null && CoopUtil.coopStatus_1.equals(cooperationSettingVO.getStatus())
					&& !AssertUtil.isEmpty(cooperationSettingVO.getPersonwebLoginurl())){
				response.sendRedirect(cooperationSettingVO.getPersonwebLoginurl());
			}else{
				response.sendRedirect(WxqyhAppContext.getWebLoginUrl(ServletActionContext.getRequest().getContextPath()));
			}
			
		} catch (Exception e) {
			logger.error("企微网页版用户登录验证失败", e);
			setActionResult("100", "企微网页版用户登录验证失败！");
		}
		doJsonOut();
	}
	
	/**
	 * @author lishengtao
	 * 获取当前登录人的信息
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getLoginUser()throws Exception,BaseException{
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	TbQyUserInfoVO userVO = new TbQyUserInfoVO();
    	userVO.setPersonName(userInfo.getPersonName());
    	addJsonObj("userVO",userVO);
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password 要设置的 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return j_username
	 */
	public String getJ_username() {
		return j_username;
	}

	/**
	 * @param j_username 要设置的 j_username
	 */
	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	/**
	 * @return j_password
	 */
	public String getJ_password() {
		return j_password;
	}

	/**
	 * @param j_password 要设置的 j_password
	 */
	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}

	/**
	 * @return userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount 要设置的 userAccount
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public IExperienceapplicationService getExperienceapplicationService() {
		return experienceapplicationService;
	}
    @Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(
			IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
	}

	/**
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName 要设置的 userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state 要设置的 state
	 */
	public void setState(String state) {
		this.state = state;
	}
}
