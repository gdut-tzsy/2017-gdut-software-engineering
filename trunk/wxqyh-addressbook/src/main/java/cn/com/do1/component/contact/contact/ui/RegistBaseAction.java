package cn.com.do1.component.contact.contact.ui;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.contact.vo.ExtOrgVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IRegisterService;
import cn.com.do1.component.contact.contact.service.IWxLoginService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.managesetting.managesetting.service.IManagesettingService;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationTempPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceApplicationPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.ExperienceAgentStatusUtil;
import cn.com.do1.component.qwinterface.orginfo.OrgRegistNotifier;
import cn.com.do1.component.sms.sendsms.util.MD5Util;
import cn.com.do1.component.systemmgr.person.model.PersonVO;
import cn.com.do1.component.systemmgr.role.model.RoleVO;
import cn.com.do1.component.systemmgr.user.model.UserVO;
import cn.com.do1.component.systemmgr.user.service.IUserService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.EmailUtil;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.WxqyhBaseAction;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.component.wxcgiutil.login.AgentInfo;
import cn.com.do1.component.wxcgiutil.login.LoginInfo;
import cn.com.do1.component.wxcgiutil.suite.Department;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户注册基础类，用于用户注册的公用方法
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 *
 * @author sunqinghai
 * @date 2017 -4-27
 */
public class RegistBaseAction extends WxqyhBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(RegistBaseAction.class);
	@Resource(name = "experienceapplicationService")
	public IExperienceapplicationService experienceapplicationService;
	@Resource(name = "wxLoginService")
	public IWxLoginService wxLoginService;
	@Resource(name = "registerService")
	public IRegisterService registerService;
	public IUserService userService;
	@Resource(name = "managesettingService")
	private IManagesettingService managesettingService;
	@Resource
	public void setUserService(IUserService userService)
	{
		this.userService = userService;
	}
	@Resource(name = "departmentService")
	public IDepartmentService departmentService;
	@Resource(name = "contactService")
	public IContactService contactService;

	/**
	 * 从微信的用户自动注册
	 *
	 * @param personName 人员姓名
	 * @param accessToken 企业token
	 *@param tbDqdpOrganizationTempPO  @return 返回数据
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -4-27
	 */
	public TbQyUserWxloginInfoPO autoRegistFromWeiXinUser(String personName, String accessToken, TbDqdpOrganizationTempPO tbDqdpOrganizationTempPO) throws Exception, BaseException {
		try {
			//判断是否有手机号码，如果有手机号码，自动完成注册
			if (StringUtil.isNullEmpty(tbDqdpOrganizationTempPO.getMobile())) {
				return null;
			}
			String userName;
			if (!AssertUtil.isEmpty(tbDqdpOrganizationTempPO.getEmail()) && !userService.isUserAlreadyExist(tbDqdpOrganizationTempPO.getEmail())) {
				userName = tbDqdpOrganizationTempPO.getEmail();
			}
			else if (!AssertUtil.isEmpty(tbDqdpOrganizationTempPO.getMobile()) && !userService.isUserAlreadyExist(tbDqdpOrganizationTempPO.getMobile())) {
				userName = tbDqdpOrganizationTempPO.getMobile();
			}
			else if (!AssertUtil.isEmpty(tbDqdpOrganizationTempPO.getUserid()) && !userService.isUserAlreadyExist(tbDqdpOrganizationTempPO.getUserid())) {
				userName = tbDqdpOrganizationTempPO.getUserid();
			}
			else {
				return null;
			}
			if (StringUtil.isNullEmpty(personName)) {
				//如果token不为空，并且用户wxuserid也不为空，调用微信接口获取用户名
				if (!StringUtil.isNullEmpty(accessToken) && !StringUtil.isNullEmpty(tbDqdpOrganizationTempPO.getUserid())) {
					try {
                        WxUser wxUser = WxUserService.getUserByAccessToken(tbDqdpOrganizationTempPO.getUserid(), tbDqdpOrganizationTempPO.getCorpId(), tbDqdpOrganizationTempPO.getOrgId(), accessToken);
                        if (wxUser != null) {
                            personName = wxUser.getName();
                        }
                        else {
                            personName = tbDqdpOrganizationTempPO.getName();
                        }
					} catch (Exception e) {
						logger.error("RegistBaseAction getUserByAccessToken " + tbDqdpOrganizationTempPO.getCorpId(), e);
						ExceptionCenter.addException(e, "RegistBaseAction getUserByAccessToken @sqh", tbDqdpOrganizationTempPO.getCorpId() + tbDqdpOrganizationTempPO.getUserid());
						personName = tbDqdpOrganizationTempPO.getName();
					} catch (BaseException e) {
						logger.error("RegistBaseAction getUserByAccessToken " + tbDqdpOrganizationTempPO.getCorpId(), e);
						ExceptionCenter.addException(e, "RegistBaseAction getUserByAccessToken @sqh", tbDqdpOrganizationTempPO.getCorpId() + tbDqdpOrganizationTempPO.getUserid());
						personName = tbDqdpOrganizationTempPO.getName();
					}
				}
				else {
					personName = tbDqdpOrganizationTempPO.getName();
				}
			}
			TbQyExperienceApplicationPO tbQyExperienceApplicationPO = new TbQyExperienceApplicationPO();
			if (personName.length() > 200) {
				tbQyExperienceApplicationPO.setContactName(personName.substring(0, 200));
			}
			else {
				tbQyExperienceApplicationPO.setContactName(personName);
			}
			tbQyExperienceApplicationPO.setEmail(tbDqdpOrganizationTempPO.getEmail());
			tbQyExperienceApplicationPO.setSource("自动注册");
			tbQyExperienceApplicationPO.setTel(tbDqdpOrganizationTempPO.getMobile());
			tbQyExperienceApplicationPO.setUserid(tbDqdpOrganizationTempPO.getUserid());
			tbQyExperienceApplicationPO.setSourceType(tbDqdpOrganizationTempPO.getSourceType() == null ? ExperienceAgentStatusUtil.SOURCE_TYPE_UNDEFINED : tbDqdpOrganizationTempPO.getSourceType());
			return registBase(userName, null, tbDqdpOrganizationTempPO, tbQyExperienceApplicationPO);
		} catch (Exception e) {
			logger.error("RegistBaseAction autoRegistFromWeiXinUser " + tbDqdpOrganizationTempPO.getCorpId(), e);
			ExceptionCenter.addException(e, this);
			EmailUtil.sendWarnEmail("自动注册企微云平台账号" + tbDqdpOrganizationTempPO.getCorpId(), tbDqdpOrganizationTempPO.getId());
		} catch (BaseException e) {
			logger.error("RegistBaseAction autoRegistFromWeiXinUser " + tbDqdpOrganizationTempPO.getCorpId(), e);
			ExceptionCenter.addException(e, this);
			EmailUtil.sendWarnEmail("自动注册企微云平台账号" + tbDqdpOrganizationTempPO.getCorpId(), tbDqdpOrganizationTempPO.getId());
		}
		return null;
	}
	/**
	 * 从微信的用户自动创建子账号
	 * @param li
	 * @param wx
	 * @return 返回数据
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -4-27
	 */
	public TbQyUserWxloginInfoPO autoAddChildAccount(LoginInfo li, TbQyUserWxloginInfoPO wx) throws Exception, BaseException {
		/**
		 * 自动注册子账号
		 * 1、判断是否符合自动创建的条件
		 * 2、生成用户信息
		 * 3、根据用户的管理部门生成子账号的管理范围
		 * 4、根据用户的管理应用情况自动生成角色
		 */
		try {
			//如果手机号和邮箱不存在，或者用户管理的应用和部门信息都为空，跳过，不自动创建角色和用户信息
			if (li.getUser_info() == null ||
					(AssertUtil.isEmpty(li.getUser_info().getEmail()) && AssertUtil.isEmpty(li.getUser_info().getMobile()) && AssertUtil.isEmpty(li.getUser_info().getUserid()))
					|| AssertUtil.isEmpty(li.getAgent()) || li.getAuth_info() == null || AssertUtil.isEmpty(li.getAuth_info().getDepartment())) {
				return null;
			}
			//机构信息为空，或者机构为企微云平台
			if (li.getCorp_info() == null || StringUtil.isNullEmpty(li.getCorp_info().getCorpid()) || Configuration.AUTO_CORPID.equals(li.getCorp_info().getCorpid())) {
				return null;
			}
			// 	登录用户的类型：1.企业微信创建者 2.企业微信内部系统管理员 3.企业微信外部系统管理员 4.企业微信分级管理员 5. 企业微信成员
			if (li.getUsertype() > 4) {
				return null;
			}
			String userName;
			if (!AssertUtil.isEmpty(li.getUser_info().getEmail()) && !userService.isUserAlreadyExist(li.getUser_info().getEmail())) {
				userName = li.getUser_info().getEmail();
			}
			else if (!AssertUtil.isEmpty(li.getUser_info().getMobile()) && !userService.isUserAlreadyExist(li.getUser_info().getMobile())) {
				userName = li.getUser_info().getMobile();
			}
			else if (!AssertUtil.isEmpty(li.getUser_info().getUserid()) && !userService.isUserAlreadyExist(li.getUser_info().getUserid())) {
				userName = li.getUser_info().getUserid();
			}
			else {
				return null;
			}
			DqdpOrgVO dqdpOrgVO = OrgUtil.getOrgByCorpId(li.getCorp_info().getCorpid());
			if (dqdpOrgVO == null) {
				return null;
			}
			PersonVO personVO = new PersonVO();
			personVO.setOrgId(dqdpOrgVO.getOrgId());
			personVO.setAge(String.valueOf(ManageUtil.CHILD_ADMIN));
			personVO.setPersonId(UUID32.getID());
			personVO.setPersonName(StringUtil.isNullEmpty(li.getUser_info().getName()) ? userName : li.getUser_info().getName());
			UserVO userVO = new UserVO();
			userVO.setUserId(UUID32.getID());
			userVO.setUserName(userName);
			RoleVO roleVO = new RoleVO();
			roleVO.setRoleDescription("微信账号角色");
			List<String> agentCodes = new ArrayList<String>();
			boolean isAll = false;//是否管理所有通讯录
			boolean isSuper = false;//是否微信侧的系统管理组账号
			String weixinRoleName;
			List<String> deptIdList = null;
			if (li.getUsertype() < 4) { //表明是系统管理组内的管理员，拥有最高权限
				isSuper = true;
				isAll = true;
				weixinRoleName = "超级管理组";
				roleVO.setRoleName(dqdpOrgVO.getCorpId() + "|" + weixinRoleName);
			}
			else {
				weixinRoleName = userName;
				roleVO.setRoleName(dqdpOrgVO.getCorpId() + "|" + userName);
				String agentCode;
				for (AgentInfo agentInfo : li.getAgent()) {
					agentCode = WxAgentUtil.getAgentCode(dqdpOrgVO.getCorpId(), String.valueOf(agentInfo.getAgentid()));
					if (!StringUtil.isNullEmpty(agentCode)) {
						agentCodes.add(agentCode);
					}
				}
				List<String> wxDeptIds = new ArrayList<String>(li.getAuth_info().getDepartment().length);
				for (Department department : li.getAuth_info().getDepartment()){
					if (dqdpOrgVO.getWxId().equals(department.getId())) {
						isAll = true;
						break;
					}
					wxDeptIds.add(department.getId());
				}
				if (!isAll) {
					deptIdList = departmentService.getDeptIdsByWxIds(dqdpOrgVO.getOrgId(), wxDeptIds);
				}
			}

			UserInfoVO userInfoVO = null;
			if (!StringUtil.isNullEmpty(li.getUser_info().getUserid())) {
				userInfoVO = contactService.getUserInfoByWxUserId(li.getUser_info().getUserid(), li.getCorp_info().getCorpid());
			}
			boolean isSuccess = managesettingService.addPersonAndRole(dqdpOrgVO, userVO, personVO, roleVO, agentCodes, isAll, isSuper, deptIdList, userInfoVO, weixinRoleName);
			if (isSuccess) {
				if (wx == null) {
					wx = new TbQyUserWxloginInfoPO();
					wx.setId(UUID32.getID());
					wx.setCorpId(dqdpOrgVO.getCorpId());
					wx.setEmail(li.getUser_info().getEmail());
					wx.setWxUserId(li.getUser_info().getUserid());
					wx.setUserAccount(userName);
					wx.setCreateTime(new Date());
					wx.setUserAccount(userName);
					wx.setPasswordType(ExperienceAgentStatusUtil.PASSWORD_TYPE_MD5);
					wx.setUsertype(li.getUsertype());
					experienceapplicationService.insertPO(wx, false);
				}
				else {
					wx.setCorpId(li.getCorp_info().getCorpid());
					//wx.setCorpName(li.getCorpName());
					wx.setEmail(li.getUser_info().getEmail());
					wx.setWxUserId(li.getUser_info().getUserid());
					//wx.setName(li.getName());
					wx.setCreateTime(new Date());
					wx.setUsertype(li.getUsertype());
					experienceapplicationService.updatePO(wx, true);
				}
				return wx;
			}
		} catch (Exception e) {
			logger.error("RegistBaseAction autoAddChildAccount " + li.getCorp_info().getCorpid(), e);
			ExceptionCenter.addException(e, this);
			EmailUtil.sendWarnEmail("自动生成子账号失败" + li.getCorp_info().getCorpid(), JSONObject.fromObject(li).toString());
		} catch (BaseException e) {
			logger.error("RegistBaseAction autoAddChildAccount " + li.getCorp_info().getCorpid(), e);
			ExceptionCenter.addException(e, this);
			EmailUtil.sendWarnEmail("自动生成子账号失败" + li.getCorp_info().getCorpid(), JSONObject.fromObject(li).toString());
		}
		return null;
	}

	/**
	 * 自动注册方法
	 * @param userName
	 * @param password
	 * @param po
	 * @param tbQyExperienceApplicationPO
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -4-26
	 */
	public TbQyUserWxloginInfoPO registBase(String userName, String password, TbDqdpOrganizationTempPO po,TbQyExperienceApplicationPO tbQyExperienceApplicationPO) throws Exception, BaseException {
		HttpServletRequest request=ServletActionContext.getRequest();
		tbQyExperienceApplicationPO.setIsautoregis(ExperienceAgentStatusUtil.REGISTER_TYPE_OWN);
		//后台添加用户角色等
		ExtOrgVO orgVO = registerService.autoAbutmentFormPage(userName,password,tbQyExperienceApplicationPO.getIsautoregis(),po);
		TbQyUserWxloginInfoPO wx = null;
		//绑定微信扫码登陆
		try {
			/**
			 * 关联自动用户账号
			 */
			if(!AssertUtil.isEmpty(po.getEmail()) || !AssertUtil.isEmpty(po.getUserid())){
				wx = wxLoginService.findWxAccountByUserIdAndEmail(po.getUserid(),po.getEmail(),po.getCorpId());
				String aesPassword = StringUtil.isNullEmpty(password) ? null : MD5Util.encrypt(password);
				if(wx==null){
					//绑定登陆账号并自动登陆
					wx = new TbQyUserWxloginInfoPO();
					wx.setId(UUID32.getID());
					wx.setCorpId(po.getCorpId());
					wx.setEmail(po.getEmail());
					wx.setWxUserId(po.getUserid());
					wx.setUserAccount(userName);
					wx.setCreateTime(new Date());
					wx.setPassword(aesPassword);
					wx.setPasswordType(ExperienceAgentStatusUtil.PASSWORD_TYPE_MD5);
					experienceapplicationService.insertPO(wx, false);
				}
				else if(!userName.equals(wx.getUserAccount()) || !StringUtil.strCstr(password, aesPassword)){
					wx.setCorpId(po.getCorpId());
					wx.setEmail(po.getEmail());
					wx.setWxUserId(po.getUserid());
					wx.setUserAccount(userName);
					wx.setCreateTime(new Date());
					wx.setPassword(aesPassword);
					wx.setPasswordType(ExperienceAgentStatusUtil.PASSWORD_TYPE_MD5);
					experienceapplicationService.updatePO(wx, true);
				}
			}

			String ip = WxqyhAppContext.getSourceIP(request);		//记录用户接入的IP
			//更新corp为已经使用状态
			experienceapplicationService.augmentOrganizationInsertCount(po.getOrgId());
			if(!StringUtil.isNullEmpty(tbQyExperienceApplicationPO.getRecommend())){
				tbQyExperienceApplicationPO.setRecommend(tbQyExperienceApplicationPO.getRecommend().toUpperCase());
			}
			tbQyExperienceApplicationPO.setEnterpriseIp(ip);
			tbQyExperienceApplicationPO.setIsautoregis(ExperienceAgentStatusUtil.REGISTER_TYPE_OWN);
			tbQyExperienceApplicationPO.setEnterpriseName(StringUtil.isNullEmpty(po.getCorpFullName()) ? po.getName() : po.getCorpFullName());
			tbQyExperienceApplicationPO.setOrgId(po.getOrgId());
			tbQyExperienceApplicationPO.setCreateTime(new Date());
			tbQyExperienceApplicationPO.setId(UUID32.getID());
			tbQyExperienceApplicationPO.setSubjectType(po.getSubjectType());
			experienceapplicationService.insertPO(tbQyExperienceApplicationPO, false);//将用户信息插入企微体验申请

		} catch (Exception e) {
			logger.error("绑定微信扫描登录失败"+po.getCorpId()+",发送邮件或绑定登陆账号失败", e);
			ExceptionCenter.addException(e,"接入套件自动对接处理,绑定登陆账号失败 @sqh"+po.getCorpId(),",userid"+po.getUserid()+",email"+po.getEmail());
			EmailUtil.sendWarnEmail("入套件自动对接处理,绑定登陆账号失败 @sqh"+po.getCorpId(), JSONObject.fromObject(wx).toString());
		} catch (BaseException e) {
			logger.error("绑定微信扫描登录失败"+po.getCorpId()+",发送邮件或绑定登陆账号失败", e);
			ExceptionCenter.addException(e,"接入套件自动对接处理,绑定登陆账号失败 @sqh"+po.getCorpId(),",userid"+po.getUserid()+",email"+po.getEmail());
			EmailUtil.sendWarnEmail("入套件自动对接处理,绑定登陆账号失败 @sqh"+po.getCorpId(), JSONObject.fromObject(wx).toString());
		}
		OrgRegistNotifier.registSucceed(userName,orgVO, po,tbQyExperienceApplicationPO, ExperienceAgentStatusUtil.REGISTER_TYPE_OWN);
		return wx;
	}
}
