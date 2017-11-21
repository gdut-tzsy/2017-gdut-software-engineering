package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;
import cn.com.do1.component.addressbook.contact.vo.ExtOrgVO;
import cn.com.do1.component.contact.contact.service.IRegisterService;
import cn.com.do1.component.contact.contact.service.IWxLoginService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationTempPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceApplicationPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.ExperienceAgentStatusUtil;
import cn.com.do1.component.qwinterface.orginfo.OrgRegistNotifier;
import cn.com.do1.component.sms.sendsms.util.MD5Util;
import cn.com.do1.component.systemmgr.user.service.IUserService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.EmailUtil;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 用户注册基础类，用于用户注册的公用方法
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 *
 * @author sunqinghai
 * @date 2017 -4-27
 */
public class RegistBaseUtil {
    private final static transient Logger logger = LoggerFactory.getLogger(RegistBaseUtil.class);
	public static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
	public static IWxLoginService wxLoginService = DqdpAppContext.getSpringContext().getBean("wxLoginService", IWxLoginService.class);
	public static IRegisterService registerService = DqdpAppContext.getSpringContext().getBean("registerService", IRegisterService.class);
	public static IUserService userService = DqdpAppContext.getSpringContext().getBean("dqdpUserService", IUserService.class);

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
	public static TbQyUserWxloginInfoPO autoRegistFromWeiXinUser(String personName, String accessToken, TbDqdpOrganizationTempPO tbDqdpOrganizationTempPO) throws Exception, BaseException {
		//如果不启动自动注册
		if (!Configuration.IS_AUTO_REGIST) {
			return null;
		}
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
			ExceptionCenter.addException(e, "RegistBaseUtil autoRegistFromWeiXinUser @sqh", tbDqdpOrganizationTempPO.getId());
			EmailUtil.sendWarnEmail("自动注册企微云平台账号" + tbDqdpOrganizationTempPO.getCorpId(), tbDqdpOrganizationTempPO.getId());
		} catch (BaseException e) {
			logger.error("RegistBaseAction autoRegistFromWeiXinUser " + tbDqdpOrganizationTempPO.getCorpId(), e);
			ExceptionCenter.addException(e, "RegistBaseUtil autoRegistFromWeiXinUser @sqh", tbDqdpOrganizationTempPO.getId());
			EmailUtil.sendWarnEmail("自动注册企微云平台账号" + tbDqdpOrganizationTempPO.getCorpId(), tbDqdpOrganizationTempPO.getId());
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
	public static TbQyUserWxloginInfoPO registBase(String userName, String password, TbDqdpOrganizationTempPO po,TbQyExperienceApplicationPO tbQyExperienceApplicationPO) throws Exception, BaseException {
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

			//String ip = WxqyhAppContext.getSourceIP(request);		//记录用户接入的IP
			//更新corp为已经使用状态
			experienceapplicationService.augmentOrganizationInsertCount(po.getOrgId());
			if(!StringUtil.isNullEmpty(tbQyExperienceApplicationPO.getRecommend())){
				tbQyExperienceApplicationPO.setRecommend(tbQyExperienceApplicationPO.getRecommend().toUpperCase());
			}
			//tbQyExperienceApplicationPO.setEnterpriseIp(ip);
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
