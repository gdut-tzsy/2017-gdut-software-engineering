package cn.com.do1.component.contact.contact.service;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.vo.ExtOrgVO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationTempPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceApplicationPO;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IRegisterService extends IBaseService{
	/**
	 * 用户体验自动注册
	 * @param po
	 * @author Chen Feixiong
	 * 2014-10-14
	 * @return 
	 */
	ExtOrgVO autoRegis(String name, String password, TbQyExperienceApplicationPO po)throws Exception, BaseException;
	/**
	 * 企业微信号自动对接创建组织
	 * @param po
	 * @author Chen Feixiong
	 * 2014-10-20
	 */
	//void autoAbutment(TbDqdpOrganizationTempPO po)throws Exception, BaseException;
	/**
	 * 企业微信号自动对接第2步处理数据
	 * @author Chen Feixiong
	 * 2014-10-21
	 */
	ExtOrgVO autoAbutmentFormPage(String name, String password, Integer type, TbDqdpOrganizationTempPO po)throws Exception, BaseException;
}