/*
 * Copyright © 2015 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import java.util.Date;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserSyncPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationInsertPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhAuthUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * <p>Title: 第一次同步通讯录</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2016-3-16
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class FirstLoginSyncUserUtil {
	private final static transient Logger logger = LoggerFactory.getLogger(FirstLoginSyncUserUtil.class);
	private static IExperienceapplicationService experienceapplicationService= DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
	private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);

	/**
	 * 
	 */
	private FirstLoginSyncUserUtil() {
		// TODO 自动生成的构造函数存根
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService#isConfig(java.lang.String)
	 */
	public static String isConfig(String userName){
		return "";
		/*try{
			UserOrgVO userInfoVO = contactService.getOrgByUserId(userName);
			if(userInfoVO == null){
				return "";
			}
			String orgid=userInfoVO.getOrgId();
			ExtOrgPO orgPO=experienceapplicationService.searchByPk(ExtOrgPO.class,orgid);
			logger.debug("登录配置检测"+orgid+",corpId:"+orgPO.getCorpId());
			//默默同步通讯录
			int count=experienceapplicationService.getCountByCorpID(orgPO.getCorpId());
			if(count==1){
				if(!Configuration.IS_QIWEIYUN && !WxqyhAuthUtil.isAuthed()){
					//throw new BaseException("该版本的授权许可信息不正确，请联系企微管理员!");
					logger.error("同步失败，"+orgid+",corpId:"+orgPO.getCorpId()+"该版本的授权许可信息不正确，请联系企微管理员!");
					return "";
				}
				TbDqdpOrganizationInsertPO insertPO=experienceapplicationService.getOrgInsert(orgid);
				if(insertPO!=null && !AssertUtil.isEmpty(insertPO.getIs_sync_user()) && insertPO.getIs_sync_user()==0){
					*//*String token;
					try {
						token = WeixinUtils.getAccessToken(orgPO.getCorpId(),null,null);
					}catch(Exception e){
						logger.error("默默同步通讯录getAccessToken失败",e);
						token = null;
					}catch(BaseException e){
						logger.error("默默同步通讯录getAccessToken失败",e);
						token = null;
					}
					if(StringUtil.isNullEmpty(token)){
						return "设置通讯录管理权限未选择组织架构中的一级目录，将会造成通讯录无法正确同步;<br/>请在企业微信->设置->权限管理->第三方管理组->将相关套件的'通讯录权限'设置为最上面的目录(根节点)";
					}*//*
					logger.debug("准备进入同步通讯录线程");
					TbQyUserSyncPO syncPO=new TbQyUserSyncPO();
					String syncPOId=UUID.randomUUID().toString();
					syncPO.setId(syncPOId);
					syncPO.setCreatePerson(userName);
					syncPO.setOrgId(orgid);
					syncPO.setSyncTime(new Date());
					syncPO.setSyncIp(WxqyhAppContext.getSourceIP(ServletActionContext.getRequest()));
					syncPO.setSyncCount(0);
					syncPO.setExt1("0");
					syncPO.setCreateTime(new Date());
					contactService.insertPO(syncPO, false);
					AddressbookSyncTask.insertUserTask(userInfoVO.getCorpId(),orgid,userName,syncPOId);
					//更新组织为已经同步过通讯录
					TbDqdpOrganizationInsertPO inPO = new TbDqdpOrganizationInsertPO();
					inPO.setId(insertPO.getId());
					inPO.setIs_sync_user(1);
					contactService.updatePO(inPO, false);
				}
			}
			//使用套件接入后，登录不需要检测是否跳转到应用中心 chenfeixiong 2014/12/16
			//return experienceapplicationDAO.isConfig(orgid);	
			return "";
		}catch(Exception e){
			logger.error("默默同步通讯录失败",e);
			return "";
		}catch(BaseException e){
			logger.error("默默同步通讯录失败",e);
			return "";
		}*/
	}
}
