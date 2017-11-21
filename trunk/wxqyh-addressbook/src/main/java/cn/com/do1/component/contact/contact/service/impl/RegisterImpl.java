package cn.com.do1.component.contact.contact.service.impl;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.ExtOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.service.IRegisterService;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.dao.IExperienceapplicationDAO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationInsertPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationTempPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceApplicationPO;
import cn.com.do1.component.systemmgr.org.service.IOrgService;
import cn.com.do1.component.systemmgr.person.model.PersonVO;
import cn.com.do1.component.systemmgr.person.service.IPersonService;
import cn.com.do1.component.systemmgr.user.model.UserVO;
import cn.com.do1.component.systemmgr.user.service.IUserService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
@Service("registerService")
public class RegisterImpl extends BaseService implements IRegisterService {
    private final static transient Logger logger = LoggerFactory.getLogger(RegisterImpl.class);

    private IExperienceapplicationDAO experienceapplicationDAO;
	@Resource
    private IOrgService orgService;
	@Resource
    private IPersonService myPersonService;
	@Resource
    private IUserService userService;
	@Resource(name = "contactService")
    private IContactService contactService;

    @Resource
    public void setExperienceapplicationDAO(IExperienceapplicationDAO experienceapplicationDAO) {
        this.experienceapplicationDAO = experienceapplicationDAO;
        setDAO(experienceapplicationDAO);
    }

	/* （非 Javadoc）
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService#autoRegis(cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceRegisterPO, java.util.Map)
	 */
	@Override
	public ExtOrgVO autoRegis(String name, String password, TbQyExperienceApplicationPO po)
			throws Exception, BaseException {
		String wxDeptId = null;
		String id = UUID32.getID();
		try {
			//添加组织
			ExtOrgVO orgVO=new ExtOrgVO();					
			orgVO.setOrganizationId(id);
			orgVO.setOrganizationName(po.getEnterpriseName());
			orgVO.setOrganizationDescription(po.getEnterpriseName());
			orgVO.setParentId(Configuration.AUTO_PARENT_ID);
			orgVO.setCorpId(Configuration.AUTO_CORPID);
			orgVO.setCorpSecret(Configuration.AUTO_CORPSECRE);
			orgVO.setToken(Configuration.AUTO_TOKEN);
			orgVO.setStep(OrgUtil.getThisDayForString());
			orgVO.setType(po.getIsautoregis());
			orgService.addOrganization(orgVO);
			logger.error("dpdp添加机构---orgVO"+orgVO.getOrganizationName());
			//新增机构同步到微信
			wxDeptId = addOrgToWx(orgVO,Configuration.AUTO_PARENT_ID);
			logger.error("dpdp同步机构到微信---"+po.getEnterpriseName());
			//添加用户角色
			PersonVO personVO=new PersonVO();
			personVO.setPersonName(po.getEnterpriseName());
			personVO.setOrgId(id);
			personVO.setAge("0");
			UserVO userVO=new UserVO();
			userVO.setUserName(name);
			userVO.setPassword(password);
			userVO.setRoleIds(Configuration.AUTO_ROLE_ID);		//设置普通管理员权限
			myPersonService.addPerson(personVO,userVO,new HashMap());
			logger.error("dpdp添加用户角色"+personVO+"-----"+userVO);
			
			//添加管理团队部门
			TbDepartmentInfoPO tbDepartmentInfoPO = new TbDepartmentInfoPO();
			tbDepartmentInfoPO.setDepartmentName("体验账号");
			tbDepartmentInfoPO.setDeptFullName("体验账号");
			tbDepartmentInfoPO.setParentDepart("");
			//tbDepartmentInfoPO.setCreatePerson(orgCode);
			tbDepartmentInfoPO.setCreateTime(new Date());
			tbDepartmentInfoPO.setOrgId(id);
			tbDepartmentInfoPO.setShowOrder(1);
			logger.error("dpdp添加部门"+tbDepartmentInfoPO);
			//同步部门到微信
			ExtOrgPO OrgPO=this.searchByPk(ExtOrgPO.class, id);
			WxDept wxDept = new WxDept();
			wxDept.setName(tbDepartmentInfoPO.getDepartmentName());
			wxDept.setParentid(OrgPO.getWxId());
			wxDept = WxDeptService.addDept(wxDept, OrgPO.getCorpId(),OrgPO.getOrganizationId());
			logger.error("dpdp同步部门到微信"+wxDept);
			tbDepartmentInfoPO.setWxId(wxDept.getId());
			tbDepartmentInfoPO.setWxParentid(wxDept.getParentid());
			String deptId = DepartmentUtil.getDeptId(OrgPO.getCorpId(), wxDept.getId());
			tbDepartmentInfoPO.setId(deptId);
	    	this.insertPO(tbDepartmentInfoPO, false);
	    	//将联系人加入团队
	    	String user_Id = UUID32.getID();
			TbQyUserInfoPO tbQyUserInfoPO=new TbQyUserInfoPO();
			tbQyUserInfoPO.setId(user_Id);
			tbQyUserInfoPO.setPersonName(po.getContactName());
			tbQyUserInfoPO.setUserId(user_Id.replace("-", ""));
			//tbQyUserInfoPO.setWxUserId(po.getTel());
			tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(po.getContactName()));
			tbQyUserInfoPO.setCreateTime(new Date());
			tbQyUserInfoPO.setOrgId(id);
			tbQyUserInfoPO.setMobile(po.getTel());
			tbQyUserInfoPO.setEmail(po.getEmail());
			tbQyUserInfoPO.setCreatePerson("admin");
			tbQyUserInfoPO.setUserStatus("0");
			tbQyUserInfoPO.setIsConcerned("0");
			tbQyUserInfoPO.setWeixinNum(po.getWeixin());
			tbQyUserInfoPO.setCorpId(OrgPO.getCorpId());
			tbQyUserInfoPO.setHeadPic("0");
			//同步联系人到微信后台
			try{
				WxUser user = new WxUser();
				tbQyUserInfoPO.setWxUserId("qy"+tbQyUserInfoPO.getMobile());
				user.setUserid(tbQyUserInfoPO.getWxUserId());
				user.setName(tbQyUserInfoPO.getPersonName());
				user.setEmail(tbQyUserInfoPO.getEmail());
				user.setGender(tbQyUserInfoPO.getSex());
				user.setMobile(tbQyUserInfoPO.getMobile());
				user.setPosition(tbQyUserInfoPO.getPosition());
				//user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
				List<String> d = new ArrayList<String>();
				List<String> detId = new ArrayList<String>();
				//d.add(wxDept.getParentid());
				d.add(wxDept.getId());
				detId.add(tbDepartmentInfoPO.getId());
				user.setDepartment(d);
				WxUserService.addUser(user,OrgPO.getCorpId(),tbQyUserInfoPO.getOrgId());
				logger.error("dpdp同步联系人到微信"+tbQyUserInfoPO.toString());
				//this.insertPO(tbQyUserInfoPO, false);
				contactService.insertUser(tbQyUserInfoPO, detId);
			}catch(BaseException e){
				logger.error(e.toString()+"---tbQyUserInfoPO"+tbQyUserInfoPO.toString());
				ExceptionCenter.addException(e, "RegisterImpl autoRegis WxUserService.addUser @sqh", tbQyUserInfoPO.toString());
			}catch(Exception e){
				logger.error(e.toString()+"---tbQyUserInfoPO"+tbQyUserInfoPO.toString());
				ExceptionCenter.addException(e, "RegisterImpl autoRegis WxUserService.addUser @sqh", tbQyUserInfoPO.toString());
			}
			return orgVO;
			//注册成功后将注册链接状态更新
			/*TbQyExperienceRegisterPO registerPO = experienceapplicationDAO.getRegisByExperienceId(po.getId());
			registerPO.setIsregister(2);
			registerPO.setCreateTime(new Date());
			this.updatePO(registerPO, false);
			experienceapplicationDAO.updateRegister(po.getId(),2);
			logger.error("dpdp更新注册链接");*/
		} catch (Exception e) {
			logger.error("体验号注册异常"+po.getEnterpriseName(),e);
			if(wxDeptId!=null){
				WxDeptService.delDept(wxDeptId, Configuration.AUTO_CORPID,id);
			}
			throw e;
		} catch (BaseException e) {
			logger.error("体验号注册异常"+po.getEnterpriseName(),e);
			if(wxDeptId!=null){
				WxDeptService.delDept(wxDeptId, Configuration.AUTO_CORPID,id);
			}
			throw e;
		}
	}
	
	/**
	 * 添加机构到微信
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	private String addOrgToWx(ExtOrgVO orgVO,String orgPid) throws Exception, BaseException{
		/*List<ExtOrgVO> listOrg = contactDAO.searchOrgByOrgName(orgName,orgPid);
		if(listOrg==null || listOrg.size()!=1){
			throw new BaseException("添加到微信失败");
		}
		ExtOrgVO orgVO = listOrg.get(0);*/
		//如果父节点有微信部门id，只新建本机构
		WxDept wxDept = new WxDept();
		wxDept.setName(orgVO.getOrganizationName());
		wxDept.setParentid("1");
		wxDept = WxDeptService.addDept(wxDept,orgVO.getCorpId(),orgVO.getOrganizationId());
		
		//更新本地的机构信息
		ExtOrgPO orgPO = new ExtOrgPO();
		orgPO.setOrganizationId(orgVO.getOrganizationId());
		orgPO.setWxId(wxDept.getId());
		orgPO.setWxParentid(wxDept.getParentid());
		this.updatePO(orgPO, false);
		return wxDept.getId();
	}
	
	/**
	 * 新增机构和用户信息
	 * @param po
	 * @param name
	 * @param password
	 * @param type 接入类型
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-5-4
	 * @version 1.0
	 */
	private synchronized ExtOrgVO autoAbutment(TbDqdpOrganizationTempPO po, String name, String password, Integer type) throws Exception, BaseException {
		//添加组织
		ExtOrgVO orgVO=new ExtOrgVO();
		orgVO.setOrganizationId(po.getOrgId());
		orgVO.setOrganizationName(po.getName()+"_"+po.getCorpId());
		orgVO.setOrganizationDescription(po.getName());
		orgVO.setParentId(Configuration.AUTO_PARENT_ID);
		orgVO.setWxId("1");//默认机构为根节点
		orgVO.setWxParentid("0");
		orgVO.setCorpId(po.getCorpId());
		orgVO.setCorpSecret(po.getCorpSecret());
		orgVO.setToken(po.getToken());
		orgVO.setStep(OrgUtil.getThisDayForString());
		orgVO.setType(type);

		//orgVO.setStep(String.valueOf(2));
		//orgVO.setOrgCode(orgCode);
		orgService.addOrganization(orgVO);
		logger.info("dpdp添加机构---orgVO"+orgVO.toString());

		PersonVO personVO=new PersonVO();
		personVO.setPersonName(orgVO.getOrganizationDescription());
		personVO.setOrgId(orgVO.getOrganizationId());
		personVO.setAge("0");
		UserVO userVO=new UserVO();
		userVO.setUserName(name);
		userVO.setPassword(password);
		userVO.setRoleIds(Configuration.AUTO_ROLE_ID2);		//设置接入管理员权限
		//myPersonMap=new HashMap();
		myPersonService.addPerson(personVO,userVO,new HashMap());
		logger.info("dpdp添加用户角色"+ JSONObject.fromObject(personVO).toString()+"-----"+JSONObject.fromObject(userVO).toString());
		return orgVO;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService#autoAbutmentFormPage(java.util.Map)
	 */
	@Override
	public ExtOrgVO autoAbutmentFormPage(String name, String password, Integer type, TbDqdpOrganizationTempPO tempPO) throws Exception,
			BaseException {
		if (userService.isUserAlreadyExist(name)){
			throw new NonePrintException("41","用户已存在,请换一个账号");
		}
		//插入机构信息
		ExtOrgVO po = autoAbutment(tempPO,name, password, type);
		logger.info("自动接入开始");
    	//插入机构接入表
		TbDqdpOrganizationInsertPO insertPO=new TbDqdpOrganizationInsertPO();
		try {
			insertPO.setOrgId(po.getOrganizationId());
			insertPO.setCorpId(po.getCorpId());
			insertPO.setCreateTime(new Date());
			insertPO.setCount(0);
			insertPO.setIscofig(0);
			insertPO.setIs_sync_user(0);
			this.insertPO(insertPO,true);
			logger.info("插入机构接入表"+insertPO.toString());
		} catch(Exception e){
			logger.error("插入机构接入表"+insertPO.toString());
			ExceptionCenter.addException(e, "RegisterImpl autoAbutmentFormPage insertPO TbDqdpOrganizationInsertPO @sqh " + po.getOrganizationId(), insertPO.toString());
		}
    	//更新企业微信号自动接入进度
    	/*ExtOrgPO orgPO=this.searchByPk(ExtOrgPO.class, po.getOrganizationId());
    	orgPO.setStep(2);
		this.updatePO(orgPO, false);*/
		logger.info("自动接入结束");
		return po;
	}

}
