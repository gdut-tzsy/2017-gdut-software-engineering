package cn.com.do1.component.contact.contact.ui;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.*;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.contact.student.util.StudentSynUtil;
import cn.com.do1.component.contact.tag.util.TagSyncTask;
import cn.com.do1.component.errcodedictionary.ErrorTip;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.managesetting.managesetting.vo.UserInfoMgrRefCache;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.vo.WaitingTaskVO;
import cn.com.do1.component.qwinterface.addressbook.IHandoverMatter;
import cn.com.do1.component.qwinterface.addressbook.IhandoverMatterManager;
import cn.com.do1.component.qwinterface.qyservice.IWaitingTask;
import cn.com.do1.component.qwinterface.qyservice.WaitingTaskManager;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.FileUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.common.util.ExcelUtil;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.thread.BatchDeparturesThread;
import cn.com.do1.component.contact.contact.thread.BatchImportThread;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.contact.tag.thread.TagThread;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.managesetting.managesetting.service.IManagesettingService;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationInsertPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qiweipublicity.experienceapplication.vo.TbQyExperienceAgentAllVO;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformManager;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class ContactAction extends WxqyhBaseAction {
	private final static transient Logger logger = LoggerFactory.getLogger(ContactAction.class);
	/**
	 * 是否是设置(0:不是)
	 */
	private final static String IS_SETTING_ZERO = "0";
	/**
	 * 是否是设置(2:是)
	 */
	private final static String IS_SETTING_ONE= "1";

	/**
	 * 登陆对象(0:超级管理员)
	 */
	public final static String IS_ORDINARY_MANAGER_ZERO = "0";
	/**
	 * 登陆对象(1:普通管理员)
	 */
	public final static String IS_ORDINARY_MANAGER_ONE = "1";

	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IDepartmentService departmentService;
    private IDepartmentMgrService departmentMgrService;
	private TbQyUserInfoPO tbQyUserInfoPO;
	private String ids[];
	private String id;
	private String idStr;
	private String keyWord; // 搜索关键字：支持姓名&手机号

	private File upFile;
	private String upFileFileName;
	private Integer pageSize;
	private String agentCode;
	private TbQyUserInfoCertificateTypePO tbQyUserInfoCertificateTypePO;
	private IContactCustomMgrService contactCustomMgrService;
	private IManagesettingService managesettingService;
	private IExperienceapplicationService experienceapplicationService;

	public TbQyUserInfoCertificateTypePO getTbQyUserInfoCertificateTypePO() {
		return tbQyUserInfoCertificateTypePO;
	}

	public void setTbQyUserInfoCertificateTypePO(
			TbQyUserInfoCertificateTypePO tbQyUserInfoCertificateTypePO) {
		this.tbQyUserInfoCertificateTypePO = tbQyUserInfoCertificateTypePO;
	}

	@Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(
			IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
	}

    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    @Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}
    @Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	/**
	 * 列表查询时，页面要传递的参数
	 * @SearchValueType(name = "personName", type = "string", format = "%s"),
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "position", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxSearch() throws Exception, BaseException {
		if(pageSize == null || pageSize<1){
			pageSize = getPageSize();
		}
		if(pageSize>1000){
			setActionResult("1100","每页个数不能超过1000条");
			return;
		}
		
		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		Map<String, Object> searchMap = getSearchValue();
		if(searchMap==null){
			searchMap = new HashMap<String, Object>();
		}
		//生日
		if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && AssertUtil.isEmpty(searchMap.get("endBirthday"))){
			searchMap.put("endBirthday", "12-31");
		}
		if(!AssertUtil.isEmpty(searchMap.get("endBirthday")) && AssertUtil.isEmpty(searchMap.get("startBirthday"))){
			searchMap.put("startBirthday", "01-01");
		}
		if(org!=null){
			searchMap.put("orgId",org!=null?org.getOrgId():"");
		}else{
			//此用户没有机构信息，不查询数据
			addJsonPager("pageData", pager);
			return ;
		}
		searchMap.put("aliveStatus", "-1");//非-1表示非离职用户列表

		//是设置的话，普通管理员也可以看到组织机构下所有人员
		String isSettingUser = (String) searchMap.get("isSettingUser");
		if(!AssertUtil.isEmpty(isSettingUser)){//去掉条件，因为不需要查询
			searchMap.remove("isSettingUser");
		}else{//为空则默认不是设置
			isSettingUser = IS_SETTING_ZERO;
		}
		
		if(!AssertUtil.isEmpty(searchMap.get("personName"))){
			String personName = searchMap.get("personName").toString();
			personName = personName.replaceAll("%", "");
			/*
			 * 修改按拼音查询
			 */
			Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
			Matcher ma = patter.matcher(personName);
			StringBuilder sb = new StringBuilder("");
			// 在字母中间添加%
			if (ma.find()) {
				for (char iterable_element : personName.toCharArray()) {
					sb.append(iterable_element).append("%");
				}
			}
			if (sb.length() > 0)
				sb = sb.replace(sb.length() - 1, sb.length(), "");

			if (sb.length() > 0)
				personName = sb.toString();

			if (!AssertUtil.isEmpty(personName)) {
				searchMap.put("personName", "%" + personName.toLowerCase() + "%");

			}
		}
		
		/** 2016年3月28日修改部门可见范围  **/
		List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
		//是否显示所有部门
		boolean isShowAll = true;
		//应用code不为空，检查应用的可见范围
		if(!AssertUtil.isEmpty(agentCode)){	// 根据应用可见范围查询用户
			AgentCacheInfo aci = WxAgentUtil.getAgentCache(org.getCorpId(),agentCode);
			if(aci == null || !aci.isTrust()){
				addJsonPager("pageData", pager);
				return;
			}
			if(!aci.isAllUserUsable()){
				if(!AssertUtil.isEmpty(aci.getPartys())){
					deptList = departmentService.getDeptByWxIds(org.getOrgId(), aci.getPartys().split("\\|"));
				}
				if(deptList == null || deptList.size()==0){
					addJsonPager("pageData", pager);
					return;
				}
				isShowAll = false;//不显示所有部门
			}
		}

		List<TbDepartmentInfoPO> deptMgrList = new ArrayList<TbDepartmentInfoPO>();
		//普通 管理员
		String isOrdinaryManager = IS_ORDINARY_MANAGER_ZERO;//超级管理员 
		if(!isShowAll || (!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingUser))){//普通管理员并且不是设置
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(),org.getOrgId());
    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())){//选择了管理 的 部门 
    			String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){//普通管理员管理部门不为空
					if(AssertUtil.isEmpty(deptList)){ //可见范围部门为空，直接取管理的所有部门
						deptList = this.departmentService.getDeptInfo(departIds);
					}else{//处理管理部门和可见范围的部门
						deptMgrList = this.departmentService.getDeptInfo(departIds);
						Map<String, String> departMap = new HashMap<String, String>();
						for(TbDepartmentInfoPO dept : deptList){
							departMap.put(dept.getId(), dept.getId());
						}
						deptList = new ArrayList<TbDepartmentInfoPO>();
						for(TbDepartmentInfoPO mgrDept : deptMgrList){
							if(null != departMap.get(mgrDept.getId())){
								deptList.add(mgrDept);
							}
						}
					}
				}
				if(AssertUtil.isEmpty(searchMap.get("deptId"))){
					searchMap.put("deptList", deptList);
				}
				isOrdinaryManager = IS_ORDINARY_MANAGER_ONE;//普通管理员 
    		}
    		pager = contactMgrService.searchContactManager(searchMap, pager);
		}else{//超级管理员
			pager = contactMgrService.searchContact(searchMap, pager);
		}
		
		addJsonObj("isOrdinaryManager", isOrdinaryManager);
		addJsonPager("pageData", pager);
	}
	/**
	 * 列表查询时，页面要传递的参数    用于流程转派获取含离职用户信息
	 * @SearchValueType(name = "personName", type = "string", format = "%s"),
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxAllSearch() throws Exception, BaseException {
		
		if(!Configuration.IS_QIWEIYUN){
			if(!WxqyhAuthUtil.isAuthed()){
				setActionResult("1011", ErrorTip.AUTHORIZE_ERROR.toString());
				return;
				//throw new BaseException("该版本的授权许可信息不正确，请联系企微管理员!");
			}
			int sumUser=contactService.countAllPerson();
			if(WxqyhAuthUtil.getAuthUserCount()<sumUser){
				setActionResult("1012", "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
				return;
			}
		}
		if(pageSize == null || pageSize<1){
			pageSize = getPageSize();
		}
		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		Map<String, Object> searchMap = getSearchValue();
		if(searchMap==null){
			searchMap = new HashMap<String, Object>();
		}
		if(org!=null){
			searchMap.put("orgId",org!=null?org.getOrgId():"");
		}else{
			//此用户没有机构信息，不查询数据
			addJsonPager("pageData", pager);
			return ;
		}
		
		//是设置的话，普通管理员也可以看到组织机构下所有人员
		String isSettingUser = (String) searchMap.get("isSettingUser");
		if(!AssertUtil.isEmpty(isSettingUser)){//去掉条件，因为不需要查询
			searchMap.remove("isSettingUser");
		}else{//为空则默认不是设置
			isSettingUser = IS_SETTING_ZERO;
		}
		
		//普通 管理员
		String isOrdinaryManager = IS_ORDINARY_MANAGER_ZERO;//超级管理员 
		if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingUser)){//普通管理员并且不是设置
			List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(),org.getOrgId());
    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())){//选择了管理 的 部门 
    			String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){
					 deptList = this.departmentService.getDeptInfo(departIds);
				}
				if(AssertUtil.isEmpty(searchMap.get("deptId"))){
					searchMap.put("deptList", deptList);
				}
				isOrdinaryManager = IS_ORDINARY_MANAGER_ONE;//普通管理员 
    		}
    		pager = contactMgrService.searchAllContactManager(searchMap, pager);
		}else{//超级管理员
			pager = contactMgrService.searchContact(searchMap, pager);
		}
		
		addJsonObj("isOrdinaryManager", isOrdinaryManager);
		addJsonPager("pageData", pager);
	}
	/**
	 * 列表查询时，页面要传递的参数
	 * @SearchValueType(name = "personName", type = "string", format = "%s"),
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "startLeaveTime", type = "date", format = "yyyy-MM-dd :start"),
			@SearchValueType(name = "endLeaveTime", type = "date", format = "yyyy-MM-dd :end")
			})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxSearchDimission() throws Exception, BaseException {
		
/*		if(!WxqyhAuthUtil.isAuthed()){
			throw new BaseException("该版本的授权许可信息不正确，请联系企微管理员!");
		}*/
		
		if(pageSize == null || pageSize<1){
			pageSize = getPageSize();
		}
		if(pageSize>1000){
			setActionResult("1100","每页个数不能超过1000条");
			return;
		}
		
		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		Map<String, Object> searchMap = getSearchValue();
		if(searchMap==null){
			searchMap = new HashMap<String, Object>();
		}
		if(org!=null){
			searchMap.put("orgId",org!=null?org.getOrgId():"");
		}else{
			//此用户没有机构信息，不查询数据
			addJsonPager("pageData", pager);
			return ;
		}
		/**
		 * 查询所有
		 */
/*		searchMap.put("leaveStatus", "-1");
		pager = contactService.searchContact(searchMap, pager);
		addJsonPager("pageData", pager);*/
		
		/**
		 * @author lishengtao
		 * 2015-8-13
		 * 修改为超级管理员和普通管理员的权限控制
		 */
		searchMap.put("leaveStatus", "-1");//离职用户

		//是设置的话，普通管理员也可以看到组织机构下所有人员
		String isSettingUser = (String) searchMap.get("isSettingUser");
		if(!AssertUtil.isEmpty(isSettingUser)){//去掉条件，因为不需要查询
			searchMap.remove("isSettingUser");
		}else{//为空则默认不是设置
			isSettingUser = IS_SETTING_ZERO;
		}
		
		//maquanyang 2015-8-13 离职人员列表查询按离职时间降序查询
		//searchMap.put("sortType", "1");
				
		//普通 管理员
		String isOrdinaryManager = IS_ORDINARY_MANAGER_ZERO;//超级管理员 
		if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingUser)){//普通管理员并且不是设置
			List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(),org.getOrgId());
    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())){//选择了管理 的 部门 
/*    			String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){
					 deptList = this.departmentService.getDeptInfo(departIds);
				}
				if(AssertUtil.isEmpty(searchMap.get("deptId"))){
					searchMap.put("deptList", deptList);
				}*/
    			
    			//离职用户和管理部门没关系，直接搜索所有
				isOrdinaryManager = IS_ORDINARY_MANAGER_ONE;//普通管理员 
    		}
    		//pager = contactService.searchContactManager(searchMap, pager);
		}else{//超级管理员
			//pager = contactService.searchContact(searchMap, pager);
		}
		//后台高级搜索条件优化
		searchMap = UserSeniorSearchUtil.manageSearchCondition(searchMap);
		
		/**
		 * @author lishengtao
		 * 2015-9-24
		 * 因为离职会把和对应部门的关联删除，所以这里没有数据权限控制
		 */
		pager = contactMgrService.searchContact(searchMap, pager);
		if(Configuration.IS_QIWEIYUN) {
			//判断是不是超过1W人的超大型企业
			if (Configuration.BIG_ORG_NUMBER < OrgUtil.getUserTotal(org.getOrgId(), OrgUtil.USER_MEMBER)) {
				addJsonObj("superBigOrg", true);
			} else {
				addJsonObj("superBigOrg", false);
			}
		}else {
			addJsonObj("superBigOrg", false);
		}
		addJsonObj("isOrdinaryManager", isOrdinaryManager);
		addJsonPager("pageData", pager);
	}

	@JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
	public void ajaxAdd(@InterfaceParam(name = "secrecy")@Validation(must = true, name = "secrecy")String secrecy,
						@InterfaceParam(name = "listJson")@Validation(must = false, name = "listJson")String listJson) throws Exception, BaseException {
		if(!Configuration.IS_QIWEIYUN){
			if(!WxqyhAuthUtil.isAuthed()){
				setActionResult("1021", ErrorTip.AUTHORIZE_ERROR.toString());
				return;
			}
			int sumUser=contactService.countAllPerson();
			if(WxqyhAuthUtil.getAuthUserCount()<sumUser){
				setActionResult("1022", "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
				return;
			}
		}
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		String orgId = org!=null?org.getOrgId():"";
		//验证用户信息
		String msg = contactMgrService.verifyUserInfo(org.getCorpId(),org.getOrgId(),tbQyUserInfoPO,"1", false);
		if(msg!=null){
			setActionResult("1001", msg);
			return;
		}
		/*if(StringUtil.isNullEmpty(tbQyUserInfoPO.getDeptId())){
			setActionResult("1001", "请选择用户所在部门");
			return;
		}*/
		String[] dept = tbQyUserInfoPO.getDeptId().split(",");
		tbQyUserInfoPO.setDeptId(null);
		List<String> d = new ArrayList<String>(dept.length);
		List<String> detId = new ArrayList<String>(dept.length);
		//判断部门是否存在，把部门id放入对应的list里面
		judgeDepartAndAddDept(d, detId, dept);
		tbQyUserInfoPO.setId(ContactUtil.getUserId(org.getCorpId(), tbQyUserInfoPO.getWxUserId()));
		tbQyUserInfoPO.setPersonName(tbQyUserInfoPO.getPersonName().trim());
		tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
		tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
		tbQyUserInfoPO.setCreateTime(new Date());
		tbQyUserInfoPO.setOrgId(orgId);
		tbQyUserInfoPO.setCreatePerson(users);
		tbQyUserInfoPO.setUserStatus("0");
		tbQyUserInfoPO.setIsConcerned("0");
		tbQyUserInfoPO.setCorpId(org.getCorpId());
		tbQyUserInfoPO.setHeadPic("0");
		//如果是vip而且值为空
		if(isVip(org)){
			if(AssertUtil.isEmpty(tbQyUserInfoPO.getIsTop()))
				tbQyUserInfoPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
		}
		else{
			tbQyUserInfoPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
		}
		publishToWx(tbQyUserInfoPO,d,org.getCorpId());
		contactService.insertUser(tbQyUserInfoPO, detId);
		//更新部门人数
		UserInfoChangeNotifier.addUser(org, tbQyUserInfoPO, detId,null, UserInfoChangeInformType.USER_MGR);
		//判断是否保密，0，不需要保密，1，需要保密
		if("1".equals(secrecy)){
			addSecrecy(tbQyUserInfoPO.getOrgId(),tbQyUserInfoPO.getUserId());
		}

		if(null != listJson) {
			JSONObject jsonList = JSONObject.fromObject(listJson);
			contactCustomMgrService.addUserItem(jsonList, tbQyUserInfoPO.getUserId(), org.getOrgId());
		}
		addJsonObj("userId", tbQyUserInfoPO.getUserId());
	}


	//同步数据到微信后台
	public void publishToWx(TbQyUserInfoPO tbQyUserInfoPO,List<String> d,String corpId) throws Exception, BaseException{
		WxUser user = new WxUser();
		user.setUserid(tbQyUserInfoPO.getWxUserId());
		user.setName(tbQyUserInfoPO.getPersonName());
		user.setEmail(tbQyUserInfoPO.getEmail());
		user.setGender(tbQyUserInfoPO.getSex());
		user.setMobile(tbQyUserInfoPO.getMobile());
		user.setPosition(tbQyUserInfoPO.getPosition());
		user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
		user.setDepartment(d);
		WxUserService.addUser(user,corpId,tbQyUserInfoPO.getOrgId());
	}
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
	public void ajaxUpdate(@InterfaceParam(name = "secrecy")@Validation(must = true,name = "secrecy")String secrecy,
						   @InterfaceParam(name = "listJson")@Validation(must = false, name = "listJson")String listJson ) throws Exception, BaseException {
		TbQyUserInfoPO history = contactService.searchByPk(TbQyUserInfoPO.class, tbQyUserInfoPO.getId());
		if(history == null){
			throw new NonePrintException("1001","该用户不存在或已被删除");
		}
		TbQyUserInfoPO old = new TbQyUserInfoPO();
		BeanHelper.copyProperties(old, history);
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		String orgId = org!=null?org.getOrgId():"";
		boolean isUpdate=false;//是否修改过冗余字段，以便记录数据更新
		if(!org.getCorpId().equals(history.getCorpId())){
			setActionResult("1001", "用户信息有误，请联系企微系统管理员！");
			return;
		}
		if("-1".equals(history.getUserStatus())){
			setActionResult("1001", "不能修改离职人员部门信息");
			return;
		}
		tbQyUserInfoPO.setUserId(history.getUserId());
		tbQyUserInfoPO.setWxUserId(history.getWxUserId());
		List<String> detId = null;
		if (WxAgentUtil.isTrustAgent(org.getCorpId(), WxAgentUtil.getAddressBookCode())) {
			String personName = tbQyUserInfoPO.getPersonName().trim();
			if(!personName.equals(history.getPersonName())){
				isUpdate=true;
				history.setPersonName(personName);
				history.setPinyin(PingYinUtil.getPingYin(personName));
			}
			//验证用户信息
			String msg = contactMgrService.verifyUserInfo(org.getCorpId(),orgId,tbQyUserInfoPO,"1", true);
			if(msg!=null){
				setActionResult("1001", msg);
				return;
			}
			String[] dept = tbQyUserInfoPO.getDeptId().split(",");
			tbQyUserInfoPO.setDeptId(null);
			List<String> d = new ArrayList<String>(dept.length);
			detId = new ArrayList<String>(dept.length);
			if (ManageUtil.superAdmin != org.getAge()) {//如果不是超级管理员
				TbManagerPersonVO tbManagerPersonVO = contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(), org.getOrgId());
				if (!AssertUtil.isEmpty(tbManagerPersonVO)) {
					if (ManageUtil.RANGE_THREE.equals(tbManagerPersonVO.getRanges())) {//普通管理员目标对象不是所有人
						List<TbDepartmentInfoVO> deptList = contactService.getDeptInfoByUserId(history.getOrgId(),history.getUserId());
						if(deptList.size() > 1) {//如果是多部门
							for (TbDepartmentInfoVO departmentInfoVO : deptList) {
								d.add(departmentInfoVO.getWxId());
								detId.add(departmentInfoVO.getId());
							}
						}else{//如果之前是单部门，允许新增部门
							//判断部门是否存在，把部门id放入对应的list里面
							judgeDepartAndAddDept(d, detId, dept);
						}
					} else {//普通管理员目标对象是所有人
						//判断部门是否存在，把部门id放入对应的list里面
						judgeDepartAndAddDept(d, detId, dept);
					}
				}
			} else {//如果是超级管理员
				//判断部门是否存在，把部门id放入对应的list里面
				judgeDepartAndAddDept(d, detId, dept);
			}
			WxUser user = WxUserService.getUser(history.getWxUserId(),org.getCorpId(),orgId,WxAgentUtil.getAddressBookCode());
			boolean wxUserIsNull = false;
			if(user == null){
				wxUserIsNull = true;
				user = new WxUser();
				user.setUserid(history.getWxUserId());
			}
			user.setMobile(tbQyUserInfoPO.getMobile());
			user.setEmail(tbQyUserInfoPO.getEmail());
			user.setName(history.getPersonName());
			user.setPosition(tbQyUserInfoPO.getPosition());
			user.setGender(tbQyUserInfoPO.getSex());
			setUserStatus(user, history);
			user.setDepartment(d);
			//更新微信的用户信息，错误就抛异常
			updateUser(wxUserIsNull, user, org, orgId);
			String imgUrl = ContactUtil.getWeixinUserHeadPic(user.getAvatar());
			if(history!=null && !imgUrl.equals(history.getHeadPic())){
				isUpdate=true;
			}
			history.setHeadPic(imgUrl);
			history.setMobile(tbQyUserInfoPO.getMobile());
			history.setEmail(tbQyUserInfoPO.getEmail());
			history.setWeixinNum(tbQyUserInfoPO.getWeixinNum());
			history.setSex(tbQyUserInfoPO.getSex());
			history.setPosition(tbQyUserInfoPO.getPosition());
		}
		else {
			history.setMobile(tbQyUserInfoPO.getMobile());
			history.setEmail(tbQyUserInfoPO.getEmail());
			history.setWeixinNum(tbQyUserInfoPO.getWeixinNum());
		}
		history.setMark(tbQyUserInfoPO.getMark());
		history.setAddress(tbQyUserInfoPO.getAddress());
		history.setShorMobile(tbQyUserInfoPO.getShorMobile());
		history.setBirthday(tbQyUserInfoPO.getBirthday());
		history.setQqNum(tbQyUserInfoPO.getQqNum());
		history.setLunarCalendar(tbQyUserInfoPO.getLunarCalendar());
		history.setNickName(tbQyUserInfoPO.getNickName());
		history.setPhone(tbQyUserInfoPO.getPhone());
		//新增按阳历还是按农历发送生日祝福提醒  maquanyang 2015-6-5
		history.setRemindType(tbQyUserInfoPO.getRemindType());
		//增加入职时间  maquanyang 2015-6-9
		history.setEntryTime(tbQyUserInfoPO.getEntryTime());
		//新增证件类型记录
		history.setCertificateType(tbQyUserInfoPO.getCertificateType());
		history.setCertificateContent(tbQyUserInfoPO.getCertificateContent());
		//身份证
		history.setIdentity(tbQyUserInfoPO.getIdentity());
		history.setAttribute(tbQyUserInfoPO.getAttribute());
		history.setHasChild(tbQyUserInfoPO.getHasChild());
		//更新部门人数
		List<String> oldDeptId = departmentService.getDeptUserRefByUserId(history.getUserId());
		//如果是vip
		if(isVip(org)){
			if(null == tbQyUserInfoPO.getIsTop()) {
				history.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
			}
			else {
				history.setIsTop(tbQyUserInfoPO.getIsTop());
			}
		}
		contactService.updateUser(history, detId, true);
		addJsonObj("nodeId", history.getId());
		//更新部门人数
		UserInfoChangeNotifier.updateUser(org,old, oldDeptId, history, (detId == null ? oldDeptId : detId), null, UserInfoChangeInformType.USER_MGR);
		//0表示不需要保密，1，表示需要保密
		if("0".equals(secrecy)){
			deleSecrecy(history.getOrgId(),history.getUserId());

		}
		else{
			addSecrecy(history.getOrgId(),history.getUserId());
		}
		//修改缓存中的人员数据
		CacheSessionManager.update(tbQyUserInfoPO.getUserId());
		if(null != listJson) {
			JSONObject jsonList = JSONObject.fromObject(listJson);
			contactCustomMgrService.updateUserItem(jsonList, history.getUserId(), history.getOrgId());
		}
		addJsonObj("userId", tbQyUserInfoPO.getUserId());
		//记录用户更新的数据，以便更新冗余字段 chenfeixiong 2015/06/30
		try {
			if(isUpdate){
				Map<String,String> map=new HashMap<String,String>();
				map.put("creator", users);
				map.put("userId", history.getUserId());
				map.put("orgId", history.getOrgId());
				map.put("item1", history.getPersonName());
				map.put("item2", history.getHeadPic());
				map.put("item3", history.getWxUserId());
				map.put("is_manager","0");
				contactService.addUdpatePersonInfo(map);
			}
		} catch (Exception e) {
			logger.debug("更新冗余字段",e);
		}catch (BaseException e) {
			logger.debug("更新冗余字段",e);
		}
		
	}
	/**
	 * 批量移动部门
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-11
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "移动成功", faileMsg = "移动失败"))
	public void moveDepartment() throws Exception, BaseException {
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		String orgId = org!=null?org.getOrgId():"";
		if(ids==null || ids.length==0){
			setActionResult("1001", "请先选择需要移动的人员");
			return;
		}
		if(id==null || id.isEmpty()){
			setActionResult("1001", "请先选择需要移动到的部门");
			return;
		}
		TbDepartmentInfoPO dept = contactService.searchByPk(TbDepartmentInfoPO.class, id);
		if(AssertUtil.isEmpty(dept.getWxId())){//如果部门的微信部门id为空
			setActionResult("1001", "目标部门存在异常，请刷新后再试！");
			return;
		}
		List<String> d = new ArrayList<String>(1);
		List<String> detId = new ArrayList<String>(1);
		d.add(dept.getWxId());
		detId.add(dept.getId());
		List<String> userIds = new ArrayList<String>(10);
		for(String userid:ids){
			TbQyUserInfoPO po = contactService.searchByPk(TbQyUserInfoPO.class, userid);
			//如果此人的机构不在登陆用户机构下，跳过
			if(!orgId.equals(po.getOrgId())){
				continue;
			}
			if("-1".equals(po.getUserStatus())){
				setActionResult("1001", "不能移动离职人员部门信息");
				return;
			}
			WxUser user = WxUserService.getUser(po.getWxUserId(),org.getCorpId(),orgId,WxAgentUtil.getAddressBookCode());
			if(user == null){
				continue;
			}
			user.setDepartment(d);
			//如果更新失败
			if(!WxUserService.updateUser(user,org.getCorpId(),orgId)){
				setActionResult("1001", "更新用户信息失败");
				return;
			}
			TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
			tbQyUserInfoPO.setId(userid);
			tbQyUserInfoPO.setDeptId(id);
			//同步微信的微信号
			ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, po);
			tbQyUserInfoPO.setOrgId(orgId);
			tbQyUserInfoPO.setUserId(po.getUserId());
			contactService.updateUser(tbQyUserInfoPO, detId, false);
			userIds.add(po.getUserId());
			//修改缓存中的人员数据
			CacheSessionManager.update(po.getUserId());
		}
		//更新部门人数
		UserInfoChangeNotifier.batchMoveEnd(org, userIds, detId);
	}
	
	/**
	 * 批量添加部门成员
	 * @throws Exception
	 * @throws BaseException
	 * @2015-11-30
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "添加成功", faileMsg = "添加失败"))
	public void addDeptUsers() throws Exception, BaseException {
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		String orgId = org!=null?org.getOrgId():"";
		ids = idStr.split("\\|");
		if(ids==null || ids.length==0){
			setActionResult("1001", "请先选择需要添加的人员");
			return;
		}
		if(id==null || id.isEmpty()){
			setActionResult("1001", "请先选择需要添加到的部门");
			return;
		}
		TbDepartmentInfoPO dept = contactService.searchByPk(TbDepartmentInfoPO.class, id);
		if(null == dept || AssertUtil.isEmpty(dept.getWxId())){//如果部门的微信部门id为空
			setActionResult("1001", "目标部门存在异常，请刷新后再试！");
			return;
		}
		List<String> d;
		List<String> detId = null;
		List<String> updateUsers = new ArrayList<String>(10);
		boolean hasDept = false;
		for(String userid:ids){
			d = new ArrayList<String>(1);
			detId = new ArrayList<String>(1);
			UserInfoVO po = contactService.getUserInfoNoCacheByUserId(userid);
			if(null == po){
				continue;
			}
			//如果此人的机构不在登陆用户机构下，跳过
			if(!orgId.equals(po.getOrgId())){
				continue;
			}
			if("-1".equals(po.getUserStatus())){
				setActionResult("1001", "不能添加离职人员部门信息");
				return;
			}
			WxUser user = WxUserService.getUser(po.getWxUserId(),org.getCorpId(),orgId,WxAgentUtil.getAddressBookCode());
			if(user == null){
				continue;
			}
			hasDept = false;
			if(null != user.getDepartment() && user.getDepartment().size() > 0){
				for (Integer dId : (List<Integer>)user.getDepartment()) {
					if(dept.getWxId().equals(dId+"")){
						hasDept = true;
					}
				}
			}
			d = user.getDepartment();
			if(!AssertUtil.isEmpty(po.getDeptIds())){
				for (String dId : po.getDeptIds().split(";")) {
					detId.add(dId);
				}
			}
			//如果当前用户不存在要添加的部门里面才允许添加
			if(!hasDept){
				d.add(dept.getWxId());
				detId.add(dept.getId());
			}
			user.setDepartment(d);
			//如果更新失败
			if(!WxUserService.updateUser(user,org.getCorpId(),orgId)){
				setActionResult("1001", "更新用户信息失败");
				return;
			}
			TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
			tbQyUserInfoPO.setId(userid);
			tbQyUserInfoPO.setDeptId(id);
			//同步微信的微信号
			ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, po);
			tbQyUserInfoPO.setOrgId(orgId);
			tbQyUserInfoPO.setUserId(po.getUserId());
			contactService.updateUser(tbQyUserInfoPO, detId, false);
			updateUsers.add(po.getUserId());
			//修改缓存中的人员数据
			CacheSessionManager.update(po.getUserId());
		}
		//回调
		UserInfoChangeNotifier.batchShiftIntUser(org,updateUsers,detId);
	}
	
	/**
	 * 根据部门id获取当前部门下的用户（不包含子部门）
	 * @throws Exception
	 * @throws BaseException
	 * @2016-3-1
	 * @version 1.0
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "position", type = "string", format = "%%%s%%")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserByDeptId() throws Exception, BaseException {
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		Map<String, Object> searchMap = getSearchValue();
		searchMap.put("orgId", org.getOrgId());
		searchMap.put("aliveStatus", "-1");// 过滤离职
		if(AssertUtil.isEmpty(pageSize)){
			pageSize = 10;
		}
		if(!AssertUtil.isEmpty(searchMap.get("personName"))){
			String personName = searchMap.get("personName").toString();
			personName = personName.replaceAll("%", "");
			/*
			 * 修改按拼音查询
			 */
			Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
			Matcher ma = patter.matcher(personName);
			StringBuilder sb = new StringBuilder("");
			// 在字母中间添加%
			if (ma.find()) {
				for (char iterable_element : personName.toCharArray()) {
					sb.append(iterable_element).append("%");
				}
			}
			if (sb.length() > 0)
				sb = sb.replace(sb.length() - 1, sb.length(), "");

			if (sb.length() > 0)
				personName = sb.toString();

			if (!AssertUtil.isEmpty(personName)) {
				searchMap.put("personName", "%" + personName.toLowerCase() + "%");

			}
		}
		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		pager = contactMgrService.searchUsersByDepartId(pager, searchMap);
		addJsonPager("pageData", pager);
	}

	/**
	 * 批量移除部门成员
	 * @throws Exception
	 * @throws BaseException
	 * @2015-11-30
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1022", successMsg = "移除成功", faileMsg = "移除失败"))
	public void delDeptUsers() throws Exception, BaseException {
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		String orgId = org!=null?org.getOrgId():"";
		ids = idStr.split("\\|");
		if(ids==null || ids.length==0){
			setActionResult("1001", "请先选择需要移除的人员");
			return;
		}
		if(id==null || id.isEmpty()){
			setActionResult("1001", "请先选择对应的部门");
			return;
		}
		
		//当前部门
		TbDepartmentInfoPO dept = contactService.searchByPk(TbDepartmentInfoPO.class, id);
		
		if(null == dept || AssertUtil.isEmpty(dept.getWxId())){//如果部门的微信部门id为空
			setActionResult("1001", "目标部门存在异常，请刷新后再试！");
			return;
		}
		List<String> d;
		List<String> detId = null;
		//未移除用户集合
		List<UserInfoErrorVO> errorList = new ArrayList<UserInfoErrorVO>();
		List<String> updateUsers = new ArrayList<String>(10);
		UserInfoErrorVO error;
		for(String userid:ids){
			d = new ArrayList<String>(1);
			detId = new ArrayList<String>(1);
			error = new UserInfoErrorVO();
			UserInfoVO po = contactService.getUserInfoNoCacheByUserId(userid);
			if(null == po){
				continue;
			}
			//如果此人的机构不在登陆用户机构下，跳过
			if(!orgId.equals(po.getOrgId())){
				continue;
			}
			if("-1".equals(po.getUserStatus())){
				setActionResult("1001", "不能移除离职人员部门信息");
				return;
			}
			WxUser user = WxUserService.getUser(po.getWxUserId(),org.getCorpId(),orgId,WxAgentUtil.getAddressBookCode());
			if(user == null){
				//暂不提示处理
				error.setUserInfo(po);
				error.setErrorCode("-1");
				error.setErrorDesc("微信上找不到对应的用户");
				errorList.add(error);
				continue;
			}
			if(user.getDepartment().size() == 1 && dept.getWxId().equals(user.getDepartment().get(0)+"")){
				error.setUserInfo(po);
				error.setErrorCode("1");
				error.setErrorDesc("只有一个部门，不能移除。");
				errorList.add(error);
				continue;
			}
			//判断当前用户是否存在选择的部门中
			if(null != user.getDepartment() && user.getDepartment().size() > 0){
				for (Integer dId : (List<Integer>)user.getDepartment()) {
					//移除的部门
					if(!(dId+"").equals(dept.getWxId())){
						d.add(dId.toString());
					}
				}
			}
			//当前用户没有部门信息，
			if(!AssertUtil.isEmpty(po.getDeptIds())){
				for (String dId : po.getDeptIds().split(";")) {
					//如果不是需要移除的部门
					if(!(dId+"").equals(dept.getId())){
						detId.add(dId);
					}
				}
			}
			user.setDepartment(d);
			//如果更新失败
			if(!WxUserService.updateUser(user,org.getCorpId(),orgId)){
				setActionResult("1001", "更新用户信息失败");
				return;
			}
			TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
			tbQyUserInfoPO.setId(userid);
			tbQyUserInfoPO.setDeptId(id);
			//同步微信的微信号
			ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, po);
			tbQyUserInfoPO.setOrgId(orgId);
			tbQyUserInfoPO.setUserId(po.getUserId());
			contactService.updateUser(tbQyUserInfoPO, detId, false);
			updateUsers.add(po.getUserId());
			//修改缓存中的人员数据
			if(Configuration.IS_USE_MEMCACHED){
				CacheSessionManager.update(po.getUserId());
			}
		}
		//回调
		UserInfoChangeNotifier.batchShiftOutUser(org,updateUsers,detId);
		if(errorList.size() > 0){
			addJsonArray("errorList", errorList);
		}
	}
	
	
	/**
	 * 离职
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-3-10
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "离职成功", faileMsg = "离职失败"))
	public void userLeave(@InterfaceParam(name = "leaveTime")@Validation(must = false, name = "leaveTime")String leaveTime,
						  @InterfaceParam(name = "leaveRemark")@Validation(must = false, name = "leaveRemark")String leaveRemark,
						  @InterfaceParam(name = "matterVOs")@Validation(must = false, name = "matterVOs")String matterVOs,
						  @InterfaceParam(name = "number")@Validation(must = false, name = "number")Integer number) throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyUserInfoPO po = contactService.searchByPk(TbQyUserInfoPO.class, id);
		if(AssertUtil.isEmpty(po)){
			setActionResult(ErrorCodeDesc.USER_NO_FIND.getCode(), ErrorCodeDesc.USER_NO_FIND.getDesc());
			return;
		}
		if(!AssertUtil.isEmpty(leaveTime)){//选了入职时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
			po.setLeaveTime(sdf.parse(leaveTime));
		}else{//没选离职时间，默认当前时间
			po.setLeaveTime(new Date());
		}
		Map<String, List<HandoverMatterVO>> matterMap = new HashMap<String, List<HandoverMatterVO>>();
		if(VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK) && !AssertUtil.isEmpty(number) && 1 == number){//如果是金卡vip用户,而且是选择离职并交接
			JSONArray jsonArray = JSONArray.fromObject(matterVOs);
			//从jsp页面传来的matters转换成的数组
			HandoverMatterVO[] matterJspVOs = (HandoverMatterVO[]) JSONArray.toArray(jsonArray, HandoverMatterVO.class);
			//从jsp页面传来的matters转换成的list
			List<HandoverMatterVO> matterJspVOList = Arrays.asList(matterJspVOs);
			List<HandoverMatterVO> matterVOList = null;
			for(HandoverMatterVO matterVO : matterJspVOList){
				if(AssertUtil.isEmpty(matterMap.get(matterVO.getAgentCode()))){
					matterVOList = new ArrayList<HandoverMatterVO>(1);
					matterVOList.add(matterVO);
					matterMap.put(matterVO.getAgentCode(), matterVOList);
				}else {
					matterVOList = matterMap.get(matterVO.getAgentCode());
					matterVOList.add(matterVO);
					matterMap.put(matterVO.getAgentCode(), matterVOList);
				}
			}
		}
		po.setLeaveRemark(leaveRemark);
		//maquanyang 2015-7-20 新增离职时间和离职备注 --end
		List<String> oldDeptId = departmentService.getDeptUserRefByUserId(po.getUserId());
		contactService.leaveUser(po,org.getUserName(),true, null);
		//更新部门人数
		UserInfoChangeNotifier.leaveUser(org,po, oldDeptId, UserInfoChangeInformType.USER_MGR, matterMap);
	}
	
	/**
	 *复职
	 *1、修改编辑信息；2、同步到wx(离职时wx的数据整个删除，需要重新插入)
	 * @throws Exception
	 * @throws BaseException
	 * @author lishengtao
	 * @2015-5-19
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "复职成功", faileMsg = "复职失败"))
	public void userFuZhi() throws Exception, BaseException {
		TbQyUserInfoPO history = contactService.searchByPk(TbQyUserInfoPO.class, tbQyUserInfoPO.getId());
		String users = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(users);
		if(!org.getCorpId().equals(history.getCorpId())){
			setActionResult("1001", "用户信息有误，请联系企微系统管理员！");
			return;
		}
		String orgId = org!=null?org.getOrgId():"";

		tbQyUserInfoPO.setUserId(history.getUserId());
		String personName = tbQyUserInfoPO.getPersonName().trim();
		if(!personName.equals(history.getPersonName())){
			history.setPersonName(personName);
			history.setPinyin(PingYinUtil.getPingYin(personName));
		}
		//验证用户信息
		String msg = contactMgrService.verifyUserInfo(org.getCorpId(),orgId,tbQyUserInfoPO,"1", true);
		if(msg!=null){
			setActionResult("1001", msg);
			return;
		}
		/*if (!AssertUtil.isEmpty(history.getMobile())&&!"0".equals(history.getUserStatus())) {
			tbQyUserInfoPO.setMobile(history.getMobile());
		}*/
		//如果没有改手机号码，不需要判断用户是否已经存在
		/*if(!history.getMobile().equals(tbQyUserInfoPO.getMobile())){
			//验证本机构下是否已存在此用户（机构内手机号不能重复）
			List<TbQyUserInfoPO> list = contactService.findUsersByUserNameOrPhone(orgId, tbQyUserInfoPO.getMobile());
			if(!AssertUtil.isEmpty(list)){
				throw new BaseException("本机构下已存在此手机号码");
			}
		}*/
		String[] dept = tbQyUserInfoPO.getDeptId().split(",");
		tbQyUserInfoPO.setDeptId(null);
		List<String> d = new ArrayList<String>(dept.length);
		List<String> detId = new ArrayList<String>(dept.length);
		//判断部门是否存在，把部门id放入对应的list里面
		judgeDepartAndAddDept(d, detId, dept);
		//理论上这里获取wx端的用户为空，因为离职了的用户已经在wx端删除
		WxUser user = WxUserService.getUser(tbQyUserInfoPO.getWxUserId(),org.getCorpId(),orgId,WxAgentUtil.getAddressBookCode());
		
		boolean wxUserIsNull = false;
		if(user == null){
			wxUserIsNull = true;
			user = new WxUser();
			user.setUserid(tbQyUserInfoPO.getWxUserId());
		}
		user.setMobile(tbQyUserInfoPO.getMobile());
		user.setEmail(tbQyUserInfoPO.getEmail());
		user.setName(history.getPersonName());
		user.setPosition(tbQyUserInfoPO.getPosition());	
		user.setGender(tbQyUserInfoPO.getSex());
		
		//设置用户关注状态
		//微信数据关注状态: 1=已关注，2=已冻结，4=未关注 
		//本地数据0表示新增数据：-1离职；0新增；1取消关注；2关注
		
		//修改为新增状态
		history.setUserStatus("0");
		//user.setStatus("4");//微信端默认为未关注
		
		if("1".equals(user.getStatus())){
			if(!"2".equals(history.getUserStatus())){
				history.setUserStatus("2");
				history.setFollowTime(new Date());
			}
		}
		else{
			//取消关注的用户
			if("2".equals(history.getUserStatus())){
				history.setUserStatus("1");
				history.setCancelTime(new Date());
			}
		}
		user.setWeixinid(tbQyUserInfoPO.getWeixinNum());

		history.setWeixinNum(user.getWeixinid());
		user.setDepartment(d);
		//因为离职了的用户会把数据从微信端删除，理论上这里只有插入同步数据
		//更新微信的用户信息，错误就抛异常
		updateUser(wxUserIsNull, user, org, orgId);
		history.setHeadPic(ContactUtil.getWeixinUserHeadPic(user.getAvatar()));

		history.setMobile(tbQyUserInfoPO.getMobile());
		history.setMark(tbQyUserInfoPO.getMark());
		history.setSex(tbQyUserInfoPO.getSex());
		history.setEmail(tbQyUserInfoPO.getEmail());
		history.setAddress(tbQyUserInfoPO.getAddress());
		history.setShorMobile(tbQyUserInfoPO.getShorMobile());
		history.setBirthday(tbQyUserInfoPO.getBirthday());
		history.setPosition(tbQyUserInfoPO.getPosition());
		history.setQqNum(tbQyUserInfoPO.getQqNum());
		history.setLunarCalendar(tbQyUserInfoPO.getLunarCalendar());
		history.setNickName(tbQyUserInfoPO.getNickName());
		history.setPhone(tbQyUserInfoPO.getPhone());
		history.setCertificateType(tbQyUserInfoPO.getCertificateType());
		history.setCertificateContent(tbQyUserInfoPO.getCertificateContent());
		history.setIdentity(tbQyUserInfoPO.getIdentity());
		history.setWxUserId(tbQyUserInfoPO.getWxUserId());
		//contactService.updatePO(history, true);
		
		//maquanyang 2015-7-20 复职加入复职时间(存入入职时间)和生日提醒方式
		history.setEntryTime(tbQyUserInfoPO.getEntryTime());
		if(ContactUtil.REMIND_TYPE_ZERO.equals(tbQyUserInfoPO.getRemindType())){//若选择提醒方式为按农历
			history.setRemindType(tbQyUserInfoPO.getRemindType());
		}else{//其他都默认按阳历
			history.setRemindType(ContactUtil.REMIND_TYPE_ONE);
		}
		
		contactService.updateUser(history, detId, true);
		//复职后删除用户部门离职管理信息
		List<String> userIds = new ArrayList<String>(1);
		userIds.add(history.getUserId());
		contactService.deleteUserLeaveDeptRef(org.getOrgId(),userIds);
		//更新部门人数
		UserInfoChangeNotifier.recoverUser(org,history, detId, null, UserInfoChangeInformType.USER_MGR);
		//修改缓存中的人员数据
		CacheSessionManager.update(history.getUserId());
		addJsonObj("nodeId", history.getId());
	}
	
	@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
	public void ajaxBatchDelete() throws Exception, BaseException {
		//同步删除微信后台通讯录
		if(!AssertUtil.isEmpty(ids)){
			String users = DqdpAppContext.getCurrentUser().getUsername();
			UserOrgVO org =  contactService.getOrgByUserId(users);
			String[] userIds = contactService.batchDeleteUser(ids,org);
			//更新部门人数
			UserInfoChangeNotifier.batchDelEnd(org, userIds, UserInfoChangeInformType.USER_MGR);
/*			contactCustomMgrService.deleBatchUser(userIds);
			if(userIds.length > 0) {
				List<String> secrecyIds = new ArrayList<String>();
				for (int i = 0; i < userIds.length; i++) {
					secrecyIds.add(userIds[i]);
				}
				QwtoolUtil.delBatchList(TbQyUserSecrecyPO.class, secrecyIds);
			}*/
			//更新学生信息
			StudentSynUtil.updateStudentByUserIds(userIds, org.getOrgId());
		}
		//contactService.batchDel(TbQyUserInfoPO.class, ids);

	}

	/**
	 * 验证联系人是否已存在
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void isExist() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String name = request.getParameter("name");
		try {
			String user = DqdpAppContext.getCurrentUser().getUsername();
			UserOrgVO org =  contactService.getOrgByUserId(user);
			String orgId = org!=null?org.getOrgId():"";
			List<TbQyUserInfoPO> list = contactMgrService.searchPersonByName(orgId,name);
			addJsonObj("exist", (list != null && list.size() > 0) ? "1" : "0");
		} catch (Exception e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void viewImportProcess(){
		/*addJsonArray("errorlist", BatchImportThread.errorlist);
		addJsonArray("repeatList", BatchImportThread.repeatList);
		addJsonObj("processNum", BatchImportThread.processNum);
		addJsonObj("totalNum", BatchImportThread.totalNum);
		addJsonArray("errorNum", BatchImportThread.errorNum);*/
		
		ResultVO resultvo= ImportResultUtil.getResultObject(id);
		if(resultvo !=null){
			addJsonObj("processNum", resultvo.getProcessNum());
			addJsonObj("totalNum", resultvo.getTotalNum());
			addJsonObj("isFinish", resultvo.isFinish());
			addJsonObj("tips", resultvo.getTips());
			if(resultvo.isFinish()){
				addJsonArray("errorlist", resultvo.getErrorlist());
				addJsonArray("repeatList", resultvo.getRepeatList());
				if(!AssertUtil.isEmpty(resultvo.getTips())){
					addJsonObj("tips", resultvo.getTips());
				}
				ImportResultUtil.removeResult(id);
			}
		}
	}
	/**
	 * 同步微信通讯录
	 * @throws Exception
	 * @throws BaseException
	 * @author Chen Feixiong
	 * 2014-11-3
	 */
	@ActionRoles({"configSync"})
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void UserListSyncThread() throws Exception, BaseException{
		if(!Configuration.IS_QIWEIYUN){
			if(!WxqyhAuthUtil.isAuthed()){
				setActionResult("1051", ErrorTip.AUTHORIZE_ERROR.toString());
				return;
			}
			int sumUser=contactService.countAllPerson();
			if(WxqyhAuthUtil.getAuthUserCount()<sumUser){
				setActionResult("1022", "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
				return;
			}
		}
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userId = user.getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
		if(userInfoVO == null){
        	logger.error("获取登陆人"+userId+"orgId的信息为空");
        	setActionResult("1000", "登陆人的信息为空");
			return;
    	}
		String orgid=userInfoVO.getOrgId();
		if(Configuration.AUTO_CORPID.equals(userInfoVO.getCorpId())){
        	logger.error("获取登陆人"+userId+"是体验用户");
        	setActionResult("1000", "你不是接入用户，不能同步通讯录");
			return;
		}
		addJsonObj("isSuperAdmin", ManageUtil.superAdmin == userInfoVO.getAge());
		boolean isAllUsable = true;
		String syncPOId=UUID32.getID();
		String newDate = DateUtil.format(new Date(), "yyyy-MM-dd");
		List<TbQyUserSyncPO> syncList= contactMgrService.getUserSync(orgid,newDate);
		if(syncList!=null && syncList.size()>0){
			TbQyUserSyncPO syncPO=syncList.get(0);
			if("0".equals(syncPO.getExt1())){
				UserInfoMgrRefCache vo = managesettingService.getMgrAndUserIdRef(userId,userInfoVO.getOrgId());
				if(vo!=null){
					addJsonObj("isMgrUser", true);
				}
				else {
					addJsonObj("isMgrUser", false);
				}
				addJsonObj("taskId", syncPO.getId());
				setActionResult("1000", "后台正在同步中，请稍后再试！");
				return;
			}
		}
		if(Configuration.IS_QIWEIYUN){
			int times;
			DqdpOrgVO org = WxqyhAppContext.getOrgExtInfo(orgid);
			if(org.isVip()){
				times = 10;//vip用户10次同步机会
			}
			else{
				times = Configuration.SYNC_USER_TIMES;
			}
			if(syncList!=null && syncList.size()>0){
				if(syncList.size()>=times){
					setActionResult("1000", "今天的同步次数已用完，每天只能同步"+times+"次微信通讯录，请明天再次同步！");
					return;
				}else{
					isAllUsable = WxAgentUtil.isAllUserUsable(userInfoVO.getCorpId(),WxAgentUtil.getAddressBookCode());
					addJsonObj("remainingSyn", times - syncList.size() - 1);
				}
			}else{
				isAllUsable = WxAgentUtil.isAllUserUsable(userInfoVO.getCorpId(),WxAgentUtil.getAddressBookCode());
				addJsonObj("remainingSyn", times - 1);
			}
			addJsonObj("hasLimit", true);
		}
		else{
			addJsonObj("hasLimit", false);
		}
		UserInfoMgrRefCache vo = managesettingService.getMgrAndUserIdRef(userId,userInfoVO.getOrgId());
		if(vo!=null){
			addJsonObj("isMgrUser", true);
		}
		else {
			addJsonObj("isMgrUser", false);
		}
		logger.debug("准备进入同步通讯录线程");
		AddressbookSyncTask.insertUserTask(userInfoVO.getCorpId(),orgid,userId,syncPOId);

		addJsonObj("isAllUsable", isAllUsable);

		TbQyUserSyncPO syncPO=new TbQyUserSyncPO();
		syncPO.setId(syncPOId);
		syncPO.setCreatePerson(userId);
		syncPO.setOrgId(orgid);
		syncPO.setSyncTime(new Date());
		syncPO.setSyncIp(WxqyhAppContext.getSourceIP(ServletActionContext.getRequest()));
		syncPO.setSyncCount(0);
		syncPO.setExt1("0");
		syncPO.setCreateTime(new Date());
		contactService.insertPO(syncPO, false);
		try{
			if(!"no".equals(Configuration.ISSENDMSG)){
				HttpUtil.get(Configuration.SENDMESURL+Configuration.USERACTIVITY);    						
			}
		}catch(Exception e){}
		addJsonObj("taskId", syncPOId);
		CacheWxqyhObject.set("synctask", orgid, syncPOId, false, 600);
	}

	/**
	 * 获取同步任务是否执行完成
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -6-2
	 */
	@ActionRoles({"configSync"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void synUserStatus(@InterfaceParam(name="taskId")@Validation(must=true,name="同步标识",code= ErrorCodeModule.null_params_error)String taskId) throws Exception, BaseException{
		UserOrgVO org = getUser();
		boolean isSucceed = true;
		//判断没有在等待执行的数据
		if (!StringUtil.isNullEmpty(taskId)) {
			Object object = CacheWxqyhObject.get("synctask", org.getOrgId(), taskId);
			if (object != null) {
				if (!(Boolean) object) {
					isSucceed = false;
				}
				addJsonObj("isSucceed", isSucceed);
				return;
			}
		}

		String newDate = DateUtil.format(new Date(), "yyyy-MM-dd");
		List<TbQyUserSyncPO> syncList= contactMgrService.getUserSync(org.getOrgId(), newDate);
		if(syncList!=null && syncList.size()>0) {
			TbQyUserSyncPO syncPO = syncList.get(0);
			if ("0".equals(syncPO.getExt1())) {
				CacheWxqyhObject.set("synctask", org.getOrgId(), syncPO.getId(), false, 600);
				isSucceed = false;
			}
		}
		addJsonObj("isSucceed", isSucceed);
	}

	/**
	 * 批量导入通讯录
	 * @throws Exception 
	 * @throws BaseException 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void batchImport() throws Exception, BaseException{
		if(!Configuration.IS_QIWEIYUN){
			if(!WxqyhAuthUtil.isAuthed()){
				//throw new BaseException("该版本的授权许可信息不正确，请联系企微管理员!");
				setActionResult("1051", ErrorTip.AUTHORIZE_ERROR.toString());
				return;
			}
			int sumUser=contactService.countAllPerson();
			if(WxqyhAuthUtil.getAuthUserCount()<sumUser){
				setActionResult("1052", "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
				return;
			}
		}
		String filePath = DqdpAppContext.getAppRootPath()+File.separator+"uploadFiles";
		File newFile = new File(filePath+File.separator+upFileFileName);
		FileUtil.copy(upFile, newFile, true);
		
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org = contactService.getOrgByUserId(loginuser);
		String orgId = org!=null?org.getOrgId():"";
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		String id=UUID32.getID();
		HttpServletRequest request = ServletActionContext.getRequest();
		String type = request.getParameter("type");
		List<TbQyUserCustomOptionVO> optionVOs;
		if(VipUtil.isQwVip(orgId)){
			optionVOs = contactCustomMgrService.getUseingOptionByorgId(orgId);
		}else{
			optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
		}
		BatchImportThread thread = new BatchImportThread(id,loginuser,orgId,null,newFile,upFileFileName,orgPO.getCorpId(),type,optionVOs);
		Thread t = new Thread(thread);
		t.start();
		addJsonObj("start", "0");
		addJsonObj("id",id);
	}
	
	/**
	 * 批量离职
	 * @throws Exception 
	 * @throws BaseException 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void batchDepartures() throws Exception, BaseException{
		String filePath = DqdpAppContext.getAppRootPath()+File.separator+"uploadFiles";
		File newFile = new File(filePath+File.separator+upFileFileName);
		FileUtil.copy(upFile, newFile, true);
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org = contactService.getOrgByUserId(loginuser);
		String orgId = org!=null?org.getOrgId():"";
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		String id=UUID32.getID();
		HttpServletRequest request = ServletActionContext.getRequest();
		String type = request.getParameter("type");
		//批量离职线程
		BatchDeparturesThread thread = new BatchDeparturesThread(id,loginuser,orgId,null,newFile,upFileFileName,orgPO.getCorpId(),type,ServletActionContext.getRequest().getRemoteAddr());
		Thread t = new Thread(thread);
		t.start();
		addJsonObj("start", "0");
		addJsonObj("id",id);
	}
	
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void viewDeparturesProcess(){
		ResultVO resultvo = ImportResultUtil.getResultObject(id);
		if(resultvo !=null){
			addJsonObj("processNum", resultvo.getProcessNum());
			addJsonObj("totalNum", resultvo.getTotalNum());
			addJsonObj("isFinish", resultvo.isFinish());
			addJsonObj("tips", resultvo.getTips());
			if(resultvo.isFinish()){
				addJsonArray("errorlist", resultvo.getErrorlist());
				addJsonArray("repeatList", resultvo.getRepeatList());
				if(!AssertUtil.isEmpty(resultvo.getTips())){
					addJsonObj("tips", resultvo.getTips());
				}
				ImportResultUtil.removeResult(id);
			}
		}
		
	}
	
	/**
	 * 导出错误的批量离职数据
	 * @throws BaseException
	 * @throws Exception
	 * @version 1.0
	 */
	public void exportErrorDepartures() throws BaseException, Exception{
		DeparturesErrorVO error=(DeparturesErrorVO) ImportResultUtil.getErrorObject(id);
		List<DeparturesVO> list=new ArrayList<DeparturesVO>();
		if(!AssertUtil.isEmpty(error)){
			list=error.getErrorlist();
		}
		ExportErrorVO evoPosc = new ExportErrorVO();
		List<ExportErrorVO> listEvo=new ArrayList<ExportErrorVO>(list.size());
		SimpleDateFormat dfymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		for(DeparturesVO iVo : list){
//			DeparturesVO evo = (DeparturesVO) evoPosc.clone();
//			evo.setWxUserId(iVo.getWxUserId());
//			evo.setMobile(iVo.getMobile());
//			evo.setPersonName(iVo.getPersonName());
//			evo.setl(iVo.getLeaveTime());
//			evo.setShorMobile(iVo.getShorMobile());
//			evo.setDeptFullName(iVo.getDeptFullName());
//			evo.setSex(iVo.getSex());
//			evo.setPosition(iVo.getPosition());
//			String birthday = "";
//			if(!AssertUtil.isEmpty(iVo.getBirthday())){
//				birthday = dfymdhms.format(iVo.getBirthday());
//			}else{
//				birthday = iVo.getBirthdayStr();
//			}
//			evo.setBirthday(birthday);
//			evo.setAddress(iVo.getAddress());
//			evo.setQqNum(iVo.getQqNum());
//			evo.setMark(iVo.getMark());
//			evo.setLunarCalendar(iVo.getLunarCalendar());
//			evo.setNickName(iVo.getNickName());
//			evo.setPhone(iVo.getPhone());
//			String entryTime = "";
//			if(!AssertUtil.isEmpty(iVo.getEntryTime())){
//				entryTime = dfymdhms.format(iVo.getEntryTime());
//			}else{
//				entryTime = iVo.getEntryTimeStr();
//			}
//			evo.setEntryTime(entryTime);
//			if(ContactUtil.REMIND_TYPE_ZERO.equals(iVo.getRemindType())){
//				evo.setRemindType("按农历");
//			}else{
//				evo.setRemindType("按阳历");
//			}
//			evo.setError(iVo.getError());
//			listEvo.add(evo);
//		}
		String[] header = {"账号","手机号码","姓名","离职时间","离职原因","错误提示"};
		File file = ExcelUtil.exportForExcel(header, list,DeparturesVO.class, "错误数据.xls");
		HttpServletResponse response = ServletActionContext.getResponse();
		if (file.exists()) {
			String fileName = file.getName();
			InputStream is = null;
			OutputStream os = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				is=new FileInputStream(file);
				os=response.getOutputStream();
				bis=new BufferedInputStream(is);
				bos=new BufferedOutputStream(os);
				HttpServletRequest request = ServletActionContext.getRequest();
				String agent = request .getHeader("User-Agent");
				boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
				if (isMSIE) {
				    fileName = URLEncoder.encode(fileName, "UTF-8");
				} else {
				    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

				response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				}
			} catch (Exception e) {
				throw new BaseException("文件流出错！");
			}finally{
				try {
					if(bos!=null){
						bos.flush();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(bis!=null){
						bis.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(bos!=null){
						bos.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(is!=null){
						is.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
			}
		}
	}
	
	/**
	 * 导入通讯录模板
	 * @throws BaseException
	 * @throws Exception
	 * @author Hejinjiao
	 * @2014-10-15
	 * @version 1.0
	 */
	/*public void exportTemplate() throws BaseException, Exception{
		String daoLoade ="D:\\wxqyh\\wxqyh\\manager\\addressbook\\contact\\template.xls";
		//String daoLoade ="D:\\wxqyh\\template.xls";
		File exportFile = new File(daoLoade);
		HttpServletResponse response = ServletActionContext.getResponse();
		String fileName = "通讯录导入模板.xls";
		InputStream is = null;
		OutputStream os = null;
		BufferedInputStream bis =null;
		BufferedOutputStream bos = null;
		try {
			is=new FileInputStream(exportFile);
			os=response.getOutputStream();
			bis= new BufferedInputStream(is);
			bos=new BufferedOutputStream(os);
			HttpServletRequest request = ServletActionContext.getRequest();
			String agent = request .getHeader("User-Agent");
			boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
			if (isMSIE) {
			    fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
			    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			//fileName = java.net.URLEncoder.encode(fileName, "UTF-8");// 处理中文文件名的问题
			//fileName = new String(fileName.getBytes("UTF-8"), "GBK");// 处理中文文件名的问题
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

			response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			}
		} catch (Exception e) {
			throw new BaseException("文件流出错！");
		}finally{
			try {
				if(bos!=null){
					bos.flush();
				}
			} catch (Exception e2) {
				logger.error("exportTemplate关闭流失败",e2);
			}
			try {
				if(bis!=null){
					bis.close();
				}
			} catch (Exception e2) {
				logger.error("exportTemplate关闭流失败",e2);
			}
			try {
				if(bos!=null){
					bos.close();
				}
			} catch (Exception e2) {
				logger.error("exportTemplate关闭流失败",e2);
			}
			try {
				if(is!=null){
					is.close();
				}
			} catch (Exception e2) {
				logger.error("exportTemplate关闭流失败",e2);
			}
		}
	}*/
	
	
	
	/**
	 * 导出通讯录
	 * @throws Exception
	 * @throws BaseException
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%")
	})
	public void exportAddressBook() throws BaseException, Exception{
		//查询本机构下所有用户
		UserOrgVO org =  getUser();
		List<ExportUserInfo> list = new ArrayList<ExportUserInfo>();
		
		Map<String,Object> params=this.getSearchValue();
		if(null==params){
			params=new HashMap<String,Object>();
		}
		params.put("orgId", org.getOrgId());
		
		if(!AssertUtil.isEmpty(params.get("isDimission")) && "1".equals(params.get("isDimission"))){
			//搜索离职
			params.put("leaveStatus", "-1");
			params.remove("isDimission");
		}else{
			//搜索非离职
			params.put("aliveStatus", "-1");//非-1表示非离职用户列表
		}
		
		
		//普通 管理员
		if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge()){//普通管理员
			List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(),org.getOrgId());
    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())){
    			String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){
					 deptList = this.departmentService.getDeptInfo(departIds);
				}
    		}
    		//list = contactService.findUsersManagerByOrgIdForExport(orgId, deptList);
    		list = contactService.findUsersManagerForExport(params, deptList, org.getOrgId());
		}else{//超级管理员
			//list = contactService.findUsersByOrgIdForExport(orgId);
			list = contactService.findUsersForExport(params,  org.getOrgId());
		}
		
		
		String[] header = {"姓名","账号","微信号","手机号码","邮箱","电话","工作部门","性别","工作职位","阳历生日","地址","QQ号","备注","农历生日","昵称","电话2","入职时间","生日提醒","状态"};
		File file = ExcelUtil.exportForExcel(header, list,ExportUserInfo.class, "通讯录.xls");
		HttpServletResponse response = ServletActionContext.getResponse();
		if (file.exists()) {
			String fileName = file.getName();
			InputStream is = null;
			OutputStream os = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				is=new FileInputStream(file);
				os=response.getOutputStream();
				bis=new BufferedInputStream(is);
				bos=new BufferedOutputStream(os);
				HttpServletRequest request = ServletActionContext.getRequest();
				String agent = request .getHeader("User-Agent");
				boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
				if (isMSIE) {
				    fileName = URLEncoder.encode(fileName, "UTF-8");
				} else {
				    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
				//fileName = java.net.URLEncoder.encode(fileName, "UTF-8");// 处理中文文件名的问题
				//fileName = new String(fileName.getBytes("UTF-8"), "GBK");// 处理中文文件名的问题
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

				response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				}
			} catch (Exception e) {
				throw new BaseException("文件流出错！");
			}finally{
				try {
					if(bos!=null){
						bos.flush();
					}
				} catch (Exception e2) {
					logger.error("exportAddressBook关闭流失败",e2);
				}
				try {
					if(bis!=null){
						bis.close();
					}
				} catch (Exception e2) {
					logger.error("exportAddressBook关闭流失败",e2);
				}
				try {
					if(bos!=null){
						bos.close();
					}
				} catch (Exception e2) {
					logger.error("exportAddressBook关闭流失败",e2);
				}
				try {
					if(is!=null){
						is.close();
					}
				} catch (Exception e2) {
					logger.error("exportAddressBook关闭流失败",e2);
				}
			}
		}
	}
	
	/**
	 * 导出错误的数据
	 * @throws BaseException
	 * @throws Exception
	 * @author Hejinjiao
	 * @2014-11-24
	 * @version 1.0
	 */
	public void exportErrorAddressBook() throws BaseException, Exception{
		UserOrgVO org = getUser();
		ImportErrorVO error=(ImportErrorVO) ImportResultUtil.getErrorObject(id);
		List<ImportVO> list=new ArrayList<ImportVO>();
		if(!AssertUtil.isEmpty(error)){
			list=error.getErrorlist();
		}
		//maquanayng 2015-7-13 修改不正确的时间格式时时间类型为空
		ExportErrorVO evoPosc = new ExportErrorVO();
		if(null == list){
			list = new ArrayList<ImportVO>();
		}
		List<ExportErrorVO> listEvo=new ArrayList<ExportErrorVO>(list.size());
		SimpleDateFormat dfymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(ImportVO iVo : list){
			ExportErrorVO evo = (ExportErrorVO) evoPosc.clone();
			evo.setPersonName(iVo.getPersonName());
			evo.setWxUserId(iVo.getWxUserId());
			evo.setWeixinNum(iVo.getWeixinNum());
			evo.setMobile(iVo.getMobile());
			evo.setEmail(iVo.getEmail());
			evo.setShorMobile(iVo.getShorMobile());
			evo.setDeptFullName(iVo.getDeptFullName());
			evo.setSex(iVo.getSex());
			evo.setPosition(iVo.getPosition());
			String birthday = "";
			if(!AssertUtil.isEmpty(iVo.getBirthday())){
				birthday = dfymdhms.format(iVo.getBirthday());
			}else{
				birthday = iVo.getBirthdayStr();
			}
			evo.setBirthday(birthday);
			evo.setAddress(iVo.getAddress());
			evo.setQqNum(iVo.getQqNum());
			evo.setMark(iVo.getMark());
			evo.setLunarCalendar(iVo.getLunarCalendar());
			evo.setNickName(iVo.getNickName());
			evo.setPhone(iVo.getPhone());
			evo.setIdentity(iVo.getIdentity());
			evo.setCertificateTypeTitle(iVo.getCertificateTypeTitle());
			evo.setCertificateContent(iVo.getCertificateContent());
			String entryTime = "";
			if(!AssertUtil.isEmpty(iVo.getEntryTime())){
				entryTime = dfymdhms.format(iVo.getEntryTime());
			}else{
				entryTime = iVo.getEntryTimeStr();
			}
			evo.setEntryTime(entryTime);
			if(ContactUtil.REMIND_TYPE_ZERO.equals(iVo.getRemindType())){
				evo.setRemindType("按农历");
			}else{
				evo.setRemindType("按阳历");
			}
			evo.setError(iVo.getError());
			evo.setAttribute(iVo.getAttribute());
			evo.setSecrecy(iVo.getSecrecy());
			evo.setIsTop(iVo.getIsTop());
			listEvo.add(evo);
		}
		List<TbQyUserCustomOptionVO> optionVOs = contactCustomMgrService.getUseingOptionByorgId(org.getOrgId());
		String[] header = ContactImportUtil.setHead(org.getOrgId(), optionVOs, true);
		File file = ExcelUtil.exportForExcel(header, listEvo,ExportErrorVO.class, "错误数据.xls");
		HttpServletResponse response = ServletActionContext.getResponse();
		if (file.exists()) {
			String fileName = file.getName();
			InputStream is = null;
			OutputStream os = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				is=new FileInputStream(file);
				os=response.getOutputStream();
				bis=new BufferedInputStream(is);
				bos=new BufferedOutputStream(os);
				HttpServletRequest request = ServletActionContext.getRequest();
				String agent = request .getHeader("User-Agent");
				boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
				if (isMSIE) {
				    fileName = URLEncoder.encode(fileName, "UTF-8");
				} else {
				    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

				response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				}
			} catch (Exception e) {
				throw new BaseException("文件流出错！");
			}finally{
				try {
					if(bos!=null){
						bos.flush();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(bis!=null){
						bis.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(bos!=null){
						bos.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				try {
					if(is!=null){
						is.close();
					}
				} catch (Exception e2) {
					logger.error("exportErrorAddressBook关闭流失败",e2);
				}
				ImportResultUtil.exportSucceed(id);
			}
		}
	}
	

	/**
	 * 组织机构树
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "1006", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getRootNodeByUser() throws Exception, BaseException{
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org = contactService.getOrgByUserId(loginuser);
		String orgId = org!=null?org.getOrgId():"";
		String orgName = org!=null?org.getOrgName():"";
		TbQyOrganizeInfo orginfo = new TbQyOrganizeInfo();
		orginfo.setOrgId(orgId);
		orginfo.setNodeId("");
		orginfo.setNodeName(orgName);
		orginfo.setIsParent("true");
		List<TbQyOrganizeInfo> list = new ArrayList<TbQyOrganizeInfo>();
		list.add(orginfo);
		logger.info("list:"+list);
		addJsonArray("orgList", list);
		//判断管理员权限，是否有权限新增一级部门
		if (imSuperManager()) {
			addJsonObj("isRange", true);
		} else {
			List<String> depts = UserHelper.listChildManagerDepts(org);
			if (depts != null && depts.size() > 0) {
				addJsonObj("isRange", false);
			} else {
				addJsonObj("isRange", true);
			}
		}
	}
	
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listOrgNodeByParent(@InterfaceParam(name="deptIds")String deptIds) throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String nodeId = request.getParameter("nodeId");
		String isSettingDepqrt = request.getParameter("isSettingDepqrt");
		if(AssertUtil.isEmpty(isSettingDepqrt)){//为空则默认不是设置
			isSettingDepqrt = IS_SETTING_ZERO;
			
		}
		UserOrgVO org = getUser();
		String orgId = org.getOrgId();
		//是否显示所有部门
		boolean isShowAll = true;
		String partys = null;
		//应用code不为空，检查应用的可见范围
		if(!AssertUtil.isEmpty(agentCode) && AssertUtil.isEmpty(nodeId)){	// 没选择部门
			AgentCacheInfo aci = WxAgentUtil.getAgentCache(org.getCorpId(), agentCode);
			// 如果不是所有人可见
			if (aci == null) {
				//如果未托管通讯录应用,需要显示全部部门人员
				if (!WxAgentUtil.getAddressBookCode().equals(agentCode)) {
					addJsonArray("orgList", new ArrayList<TbQyOrganizeInfo>());
					return;
				}
			}
			else if(!aci.isAllUserUsable()){
				partys = aci.getPartys();
				if(StringUtil.isNullEmpty(partys)){
					addJsonArray("orgList", new ArrayList<TbQyOrganizeInfo>());
					return;
				}
				isShowAll = false;//不显示所有部门
			}
		}

		List<TbQyOrganizeInfo> list;
		if(ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingDepqrt)){//普通管理员并且不是设置
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(),orgId);
			if(vo == null){
				list = new ArrayList<TbQyOrganizeInfo>(0);
			}
			else if (!StringUtil.isNullEmpty(vo.getMsgCodeIds()) && ("|" + vo.getMsgCodeIds() + "|").contains("|" + agentCode + "|")){
				if(ManageUtil.RANGE_ONE_INT.equals(vo.getMsgRange())){//如果全公司可见
					if(isShowAll){//如果应用可见范围是全公司
						if (StringUtil.isNullEmpty(deptIds)) {//如果页面上没有传入部门id
							list = departmentMgrService.getlistDeptNodeByOrgid(orgId,nodeId);
						}
						else {
							list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId, null, null, deptIds)));
						}
					}
					else{
						list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId, partys, null, deptIds)));
					}
				}
				else{
					list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId, partys, vo.getMsgDeptIds(), deptIds)));
				}
			}
			else if(ManageUtil.RANGE_ONE.equals(vo.getRanges())){//如果全公司可见
				if(isShowAll){//如果应用可见范围是全公司
					if (StringUtil.isNullEmpty(deptIds)) {
						list = departmentMgrService.getlistDeptNodeByOrgid(orgId,nodeId);
					}
					else {
						list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId, null, null, deptIds)));
					}
				}
				else{
					list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId,partys, null, deptIds)));
				}
			}
			else{
				list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId,partys,vo.getDepartids(), deptIds)));
			}
		}
		else if(!isShowAll){
			list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId,partys, null, deptIds)));
		}
		else{//超级管理员
			if (StringUtil.isNullEmpty(deptIds)) {
				list = departmentMgrService.getlistDeptNodeByOrgid(orgId,nodeId);
			}
			else {
				list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, DepartmentUtil.getIntersectionDeptPO(DepartmentUtil.getIntersectionDeptVOByTwo(orgId, null, null, deptIds)));
			}
		}
		if (IndustryUtil.isEduVersion(orgId)) { //组装教育版部门
			list = SchoolClassUtil.assembleEduDepartList(list);
		}
		addJsonArray("orgList", list);
	}


	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listOrgNodeByParentAndAgentCode() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String nodeId = request.getParameter("nodeId");
		String orgId = request.getParameter("orgId");
		String isSettingDepqrt = request.getParameter("isSettingDepqrt");
		List<TbQyOrganizeInfo> list = new ArrayList<TbQyOrganizeInfo>();
		
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		UserOrgVO org =  contactService.getOrgByUserId(user.getUsername());
		TbQyExperienceAgentAllVO agent = null;
		if(!AssertUtil.isEmpty(agentCode)){
			agent = experienceapplicationService.getAgentByAgentCode(org.getCorpId(), agentCode);
		}
		// 所有人可见
		if (null == agent || "1".equals(agent.getIsRangeAll())) {
			if(AssertUtil.isEmpty(isSettingDepqrt)){//为空则默认不是设置
				isSettingDepqrt = IS_SETTING_ZERO;
			}
			if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingDepqrt)){//普通管理员并且不是设置
				List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
				TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(user.getPersonId(),orgId);
	    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges())  && !AssertUtil.isEmpty(vo.getDepartids())){
	    			String[] departIds = vo.getDepartids().split("\\|");
					if(!AssertUtil.isEmpty(departIds)){
						 deptList = this.departmentService.getDeptInfo(departIds);
					}
	    		}
//	    		list = contactService.listOrgNodeByParentAndDepartName(orgId, nodeId, deptList);
			}else{//超级管理员
//				list = contactMgrService.listOrgNodeByParent(orgId,nodeId);
			}
		}else{
			
			
		}
		
		addJsonArray("orgList", list);
	}
	
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listOrgNodeByAndAgentCode() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String nodeId = request.getParameter("nodeId");
		String orgId = request.getParameter("orgId");
		String isSettingDepqrt = request.getParameter("isSettingDepqrt");
		if(AssertUtil.isEmpty(isSettingDepqrt)){//为空则默认不是设置
			isSettingDepqrt = IS_SETTING_ZERO;
			
		}
		List<TbQyOrganizeInfo> list = new ArrayList<TbQyOrganizeInfo>();
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		UserOrgVO org =  contactService.getOrgByUserId(user.getUsername());
		

		TbQyExperienceAgentAllVO agent = experienceapplicationService
				.getAgentByAgentCode(org.getCorpId(), agentCode);
		// 所有人可见
		if (null == agent || "1".equals(agent.getIsRangeAll())) {
			if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingDepqrt)){//普通管理员并且不是设置
				List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
				TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(user.getPersonId(),orgId);
	    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges())  && !AssertUtil.isEmpty(vo.getDepartids())){
	    			String[] departIds = vo.getDepartids().split("\\|");
					if(!AssertUtil.isEmpty(departIds)){
						 deptList = this.departmentService.getDeptInfo(departIds);
					}
	    		}
	    		list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, deptList);
			}else{//超级管理员
				list = departmentMgrService.getlistDeptNodeByOrgid(orgId,nodeId);
			}
		}else{
			if(!AssertUtil.isEmpty(agent.getPartys())){	//可见范围配置了部门
				if(!AssertUtil.isEmpty(org) && ManageUtil.superAdmin!=org.getAge() && IS_SETTING_ZERO.equals(isSettingDepqrt)){//普通管理员并且不是设置
					List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
					TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(user.getPersonId(),orgId);
		    		if(!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges())  && !AssertUtil.isEmpty(vo.getDepartids())){
		    			String[] departIds = vo.getDepartids().split("\\|");
						if(!AssertUtil.isEmpty(departIds)){
							 deptList = this.departmentService.getDeptInfo(departIds);
						}
		    		}
		    		list = departmentMgrService.getListDeptNodeByDeparts(orgId, nodeId, deptList);
				}else{//超级管理员
					//departmentService.getDeptsByWxIds(agent.getPartys(), org.getOrgId());
//					list = contactMgrService.getParentIdsByAgentCode(orgId, agentCode);
				}
			}else{
				list = new ArrayList<TbQyOrganizeInfo>();
			}
		}
		addJsonArray("orgList", list);
	}
	
	/**
	 * 校验组织代码是否重复
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
	public void validOrgCode() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgName = request.getParameter("orgName");
		String orgPid = request.getParameter("orgPid");
		String corpID = request.getParameter("corpID");
		if(!AssertUtil.isEmpty(orgName)){
			if(contactService.isExitsOrgName(orgName,orgPid)){
				addJsonObj("repeat", "1");
				return;
			}
		}
		//如果父机构为顶级机构
		if(!Configuration.TOP_ORG_ID.equals(orgPid)){
			ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgPid);
			if(orgPO==null){
				addJsonObj("repeat", "-1");
				return;
			}
			//如果新增的corpID和父级机构不同，不能新增
			if(!orgPO.getCorpId().equals(corpID)){
				addJsonObj("repeat", "3");
				return;
			}
		}
		String orgCode = request.getParameter("orgCode");
		if(!AssertUtil.isEmpty(orgCode)){
			List<ExtOrgVO> list = contactMgrService.searchOrgByCode(orgCode);
			if(list==null || list.size()>0){
				addJsonObj("repeat", "2");
				return;
			}
		}
		
		addJsonObj("repeat", "0");
	}

	/**
	 * 获得父节点的corp信息
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "新增成功", faileMsg = "新增失败"))
	public void viewParentCorp() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgPid = request.getParameter("orgPid");
		ExtOrgPO orgVO = null;
		if(!Configuration.TOP_ORG_ID.equals(orgPid)){
			ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgPid);
			orgVO = new ExtOrgPO();
			orgVO.setParentId(orgPO.getOrganizationId());
			orgVO.setCorpId(orgPO.getCorpId());
			orgVO.setCorpSecret(orgPO.getCorpSecret());
			orgVO.setToken(orgPO.getToken());
		}
		addJsonObj("orgVO", orgVO);
	}
	
	/**
	 * 添加机构到微信
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "新增成功", faileMsg = "新增失败"))
	public void addOrgToWx() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgName = request.getParameter("orgName");
		String orgPid = request.getParameter("orgPid");
		List<ExtOrgVO> listOrg = contactMgrService.searchOrgByOrgName(orgName,orgPid);
		if(listOrg==null || listOrg.size()!=1){
			throw new NonePrintException("200","机构不存在，请确认，如果新建成功，请删除后重新新建");
		}
		ExtOrgVO orgVO = listOrg.get(0);
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgVO.getParentId());
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		String wxOrgid = orgPO.getWxId();
		if (Configuration.AUTO_CORPID.equals(orgVO.getCorpId())) {//如果是超级企业微信（可以分很多的机构）
			//如果父节点没有在微信中部门id，迭代新建
			if(AssertUtil.isEmpty(orgPO.getWxId())){
				throw new NonePrintException("200","添加到微信，父机构没有设置微信部门信息");
			}
			else{
				//如果父节点有微信部门id，只新建本机构
				WxDept wxDept = new WxDept();
				wxDept.setName(orgVO.getOrganizationName());
				wxDept.setParentid(wxOrgid);
				wxDept = WxDeptService.addDept(wxDept,orgVO.getCorpId(),orgVO.getOrganizationId());
				
				//更新本地的机构信息
				orgPO = new ExtOrgPO();
				orgPO.setOrganizationId(orgVO.getOrganizationId());
				orgPO.setWxId(wxDept.getId());
				orgPO.setWxParentid(wxDept.getParentid());
				contactService.updatePO(orgPO, false);
			}
		}
		else{
			//如果非道一
			//更新本地的机构信息
			orgPO = new ExtOrgPO();
			orgPO.setOrganizationId(orgVO.getOrganizationId());
			orgPO.setWxId("1");
			orgPO.setWxParentid("0");
			contactService.updatePO(orgPO, false);
			
	    	//插入机构接入表
	    	TbDqdpOrganizationInsertPO insertPO=new TbDqdpOrganizationInsertPO();
	    	insertPO.setOrgId(orgVO.getOrganizationId());
	    	insertPO.setCorpId(orgVO.getCorpId());
	    	insertPO.setCreateTime(new Date());
	    	insertPO.setCount(0);
	    	insertPO.setIscofig(0);
	    	insertPO.setIs_sync_user(0);
	    	contactService.insertPO(insertPO,true);
		}
	}

	/**
	 * 更新机构到微信
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "修改成功", faileMsg = "修改失败"))
	public void updateOrgToWx() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		String historyCorpSecret = request.getParameter("historyCorpSecret");
		String historyToken = request.getParameter("historyToken");
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		//如果修改了CorpSecret或者Token
		if(!orgPO.getCorpSecret().equals(historyCorpSecret) || !orgPO.getToken().equals(historyToken)){
			List<ExtOrgPO> orgList = contactService.getOrgPOByCorpId(orgPO.getCorpId());
			if(orgList!=null && orgList.size()>0){
				for (ExtOrgPO extOrgPO : orgList) {
					ExtOrgPO po = new ExtOrgPO();
					po.setOrganizationId(extOrgPO.getOrganizationId());
					po.setCorpSecret(orgPO.getCorpSecret());
					po.setToken(orgPO.getToken());
					contactService.updatePO(po, false);
				}
			}
		}
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		String wxOrgid = orgPO.getWxId();
		if (Configuration.AUTO_CORPID.equals(orgPO.getCorpId())) {//如果是超级企业微信（可以分很多的机构）
			//如果机构没有设置微信部门的id，递归新建
			if(AssertUtil.isEmpty(wxOrgid)){
				throw new NonePrintException("200","添加到微信，父机构没有设置微信部门信息");
			}
			else{
				//更新到微信
				WxDept wd = new WxDept();
				wd.setId(wxOrgid);
				wd.setName(orgPO.getOrganizationDescription());
				wd.setParentid(orgPO.getWxParentid());
				WxDeptService.updateDept(wd, orgPO.getCorpId(),orgPO.getOrganizationId());
			}
		}
	}

	/**
	 * 验证是否可以移动机构
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "验证成功", faileMsg = "验证失败"))
	public void validDropOrg() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		String newParentId = request.getParameter("newParentId");
		
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		/*if(AssertUtil.isEmpty(orgPO.getOrgCode())){
			addJsonObj("repeat", "1");
			return;
		}*/

		ExtOrgPO newOrgPO = contactService.searchByPk(ExtOrgPO.class, newParentId);
		//如果新的父机构不为根机构
		if(!newParentId.equals(Configuration.TOP_ORG_ID)){
			//如果新的机构corpid和本机构corpid不同，不能移动
			if(!newOrgPO.getCorpId().equals(orgPO.getCorpId())){
				addJsonObj("repeat", "2");
				return;
			}
		}
		addJsonObj("repeat", "0");
	}
	
	/**
	 * 移动机构到微信
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-14
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "移动成功", faileMsg = "移动失败"))
	public void dropOrgToWx() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);

		ExtOrgPO newOrgPO = contactService.searchByPk(ExtOrgPO.class, orgPO.getParentId());

		List<WxDept> wxDeptList = WxDeptService.getDept(orgPO.getCorpId(),orgPO.getWxId(),orgPO.getOrganizationId(), WxAgentUtil.getAddressBookCode());
		if(AssertUtil.isEmpty(wxDeptList)){
			WxDept wd = new WxDept();
			wd.setName("企业微信");
			wd.setParentid("0");
			wd = WxDeptService.addDept(wd,orgPO.getCorpId(),orgPO.getOrganizationId());
			wxDeptList = new ArrayList<WxDept>();
			wxDeptList.add(wd);
		}
		
		//获得此用户机构ID
		String loginuser = DqdpAppContext.getCurrentUser().getUsername();
		BatchImportThread c = new BatchImportThread(loginuser,orgPO.getOrganizationId(),orgPO.getCorpId());
		c.setWxDeptList(wxDeptList);
		String wxOrgid = orgPO.getWxId();
		//如果没有在微信中部门id，迭代新建
		if(AssertUtil.isEmpty(orgPO.getWxId())){
			wxOrgid = c.iterationAddDept(orgPO.getOrganizationId());
			orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		}
		else if(AssertUtil.isEmpty(newOrgPO.getWxId())){
			c.iterationAddDept(newOrgPO.getOrganizationId());
		}
		else{
			//更新到微信
			WxDept wd = new WxDept();
			wd.setId(wxOrgid);
			wd.setName(orgPO.getOrganizationDescription());
			wd.setParentid(orgPO.getWxParentid());
			WxDeptService.updateDept(wd, orgPO.getCorpId(),orgPO.getOrganizationId());
		}
	}
	
	/**
	 * 删除机构，同步到微信
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-7
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "删除成功", faileMsg = "删除失败"))
	public void delOrgToWx() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
		WxDeptService.delDept(orgPO.getWxId(), orgPO.getCorpId(),orgPO.getOrganizationId());
	}
	
	public void setTbQyUserInfoPO(TbQyUserInfoPO tbQyUserInfoPO) {
		this.tbQyUserInfoPO = tbQyUserInfoPO;
	}

	public TbQyUserInfoPO setTbQyUserInfoPO() {
		return this.tbQyUserInfoPO;
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxView() throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyUserInfoView vo = contactMgrService.viewUserById(id);
		if (vo == null) {
			throw new NonePrintException(ErrorCodeDesc.USER_NULL.getCode(), ErrorCodeDesc.USER_NULL.getDesc());
		}
		//如果是离职用户
		List<TbDepartmentInfoVO> deptList;
		if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {
			deptList = contactService.getDeptInfoByLeaveUserId(vo.getUserId());
		}else{
			deptList = contactService.getDeptInfoByUserId(vo.getOrgId(),vo.getUserId());
		}
		//查询该用户是否保密
		TbQyUserSecrecyPO secrecyPO = selectSecrecy(vo.getOrgId(),vo.getUserId());
		List<TbQyUserCustomOptionVO> optionVOs;
		if(VipUtil.isQwVip(vo.getOrgId())) {
			optionVOs = contactCustomMgrService.getUserItemList(vo.getUserId(), vo.getOrgId());
		}else{
			optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
		}
		if (ManageUtil.superAdmin != org.getAge()) {//如果不是超级管理员
			TbManagerPersonVO tbManagerPersonVO = contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(), org.getOrgId());
			if (!AssertUtil.isEmpty(tbManagerPersonVO)) {
				if (ManageUtil.RANGE_THREE.equals(tbManagerPersonVO.getRanges())) {//普通管理员目标对象不是所有人
					addJsonObj("hasPower","0");
				} else {//普通管理员目标对象是所有人
					addJsonObj("hasPower","1");
				}
			}
		} else {//如果是超级管理员
			addJsonObj("hasPower","1");
		}
		addJsonObj("secrecyPO",secrecyPO);
		addJsonObj("tbQyUserInfoPO",vo);
		addJsonObj("deptList",deptList);
		addJsonObj("optionVOs", optionVOs);
	}

	/**
	 * 获取当前登录用户的机构信息
	 * @throws BaseException
	 * @throws Exception
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getOrgInfo() throws Exception, BaseException{
		UserOrgVO org = new UserOrgVO();
		String users = DqdpAppContext.getCurrentUser().getUsername();
		org =  contactService.getOrgByUserId(users);
		addJsonObj("org", org);
	}

	public TbQyUserInfoPO getTbQyUserInfoPO() {
		return this.tbQyUserInfoPO;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getUpFileFileName() {
		return upFileFileName;
	}

	public void setUpFileFileName(String upFileFileName) {
		this.upFileFileName = upFileFileName;
	}

    @Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * @param pageSize 要设置的 pageSize
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	

	/**
	 * 判断同步错误的条数
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "新增成功", faileMsg = "新增失败"))
	public void countUserSyncError()throws Exception,BaseException{
		//获取当前登陆账号的orgId
		UserOrgVO userInfoVO = getUser();
		String orgId=userInfoVO.getOrgId();
		
		//根据orgId获取错误的信息条数
		int userSyncErrorCount=contactMgrService.getSyncErrorUserCountByOrgId(orgId);
		addJsonObj("userSyncErrorCount",userSyncErrorCount);
	}
	
	
	/**
	 * 导出同步错误人员信息
	 * @throws Exception
	 * @throws BaseException
	 */
	public void exportUserSyncError()throws Exception,BaseException{
		//获取当前登陆账号的orgId
		UserOrgVO userInfoVO = getUser();
		String orgId=userInfoVO.getOrgId();
		
		//获取同步错误信息
		List<ExportUserSyncVO> list=new ArrayList<ExportUserSyncVO>();
		list=contactMgrService.getSyncErrorUserByOrgId(orgId);
		
		String publishName = "同步错误数据.xls";
		publishName = publishName.replace(" ", "").replace(":", "").replace("-", "");

		String[] header = {"姓名","账号","微信号","手机号码","邮箱","工作职位","同步时间","错误报告"};
		File file = ExcelUtil.exportForExcel(header, list,ExportUserSyncVO.class, publishName);
		HttpServletResponse response = ServletActionContext.getResponse();
		if (file.exists()) {
			String fileName = file.getName();
			InputStream is = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				String agent = request .getHeader("User-Agent");
				boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
				if (isMSIE) {
				    fileName = URLEncoder.encode(fileName, "UTF-8");
				} else {
				    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
				response.reset();
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

				response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				}
			} catch (Exception e) {
				throw new BaseException("文件流出错！");
			}finally{
				bos.flush();
				bis.close();
				bos.close();
				is.close();
				os.close();
			}
		}
	}
	
	public IManagesettingService getManagesettingService() {
        return managesettingService;
    }

    @Resource(name = "managesettingService")
    public void setManagesettingService(IManagesettingService managesettingService) {
        this.managesettingService = managesettingService;
    }
    
    /**
     * 查询设置字段
     * @throws Exception
     * @throws BaseException
     * @author Ma Quanyang
     * @2015-6-17
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void listField() throws Exception, BaseException{
    	//获取当前登陆账号的orgId
		UserOrgVO userInfoVO = getUser();
		String orgId=userInfoVO.getOrgId();
		List<TbQyFieldSettingVO> fieldList = this.contactService.findTbQyFieldSettingVOListByOrgId(orgId);
		List<TbQyUserCustomOptionVO> optionVOs = contactCustomMgrService.getOptionByorgId(userInfoVO.getOrgId());
		//如果没有初始化30条数据
		if (null == optionVOs || 0 == optionVOs.size()) {
			optionVOs = contactCustomMgrService.batchAddOption(userInfoVO.getOrgId());
		}
		addJsonArray("fieldList",fieldList);
		addJsonObj("optionVOs", optionVOs);
    }
    
    /**
     *  新增设置字段
     * @throws Exception
     * @throws BaseException
     * @author Chen Feixiong
     * 2014-12-16
     */
   @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "保存成功", faileMsg = "保存失败"))
   public void saveField(@InterfaceParam(name = "customJson")@Validation(must = false, name = "customJson")String customJson) throws Exception, BaseException{
	 //获取当前登陆账号的orgId
   	IUser user = (IUser) DqdpAppContext.getCurrentUser();
	String userId = user.getUsername();
	UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
	if (userInfoVO == null) {
		logger.error("获取登陆人" + userId + "orgId的信息为空");
	    setActionResult("1005", "登录人信息为空，请重新登录！");
	    return;
	}
	String orgId=userInfoVO.getOrgId();
	HttpServletRequest request = ServletActionContext.getRequest();
    String strJson = request.getParameter("strJson");
    JSONObject json = JSONObject.fromObject(strJson);

    JSONObject jData = (JSONObject)json.get("list");
    if ((jData == null) || (!(jData.has("list")))) {
      setActionResult("1002", "数据异常，请重新进入页面！");
      return;
    }
    this.contactMgrService.saveField(jData, orgId, userId);
	   JSONObject saveJson = JSONObject.fromObject(customJson);
	   JSONObject jsonList = (JSONObject) saveJson.get("list");
	   contactCustomMgrService.batchUpdateOptionAndDes(jsonList, orgId);
   }

	/**
	 * 获取离职的用户名称
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-7-21
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxGetPersonName() throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyUserInfoView vo = contactMgrService.viewUserById(id);
		TbQyUserInfoView voInfoView = new TbQyUserInfoView();
		if(null != vo){
			voInfoView.setPersonName(vo.getPersonName());
		}
		addJsonObj("tbQyUserInfoVO",voInfoView);

	}
	
	/**
	 * @author lishengtao
	 * 2015-8-31
	 * 置顶通信录人员
	 * @throws Exception
	 * @throws BaseException
	 */
	@ActionRoles({"userTop"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "置顶成功", faileMsg = "置顶失败"))
	public void updateTop()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId=request.getParameter("id");
		TbQyUserInfoPO userInfoPO=contactService.searchByPk(TbQyUserInfoPO.class, userId);
		if(null ==userInfoPO){
		    setActionResult("1002", "用户不存在!");
		    return;
		}
		String type=request.getParameter("type");
		if(!"0".equals(type) && !"1".equals(type)){
		    setActionResult("1002", "置顶类型超出范围");
		    return;
		}
		contactMgrService.updateTop(userId, type, userOrgVO);
	}
	
	/**
	 * 分页查找证件类型
	 * @author lishengtao
	 * 2015-9-2
	 * @throws Exception
	 * @throws BaseException
	 */
	@SearchValueTypes(nameFormat = "false", value = {
		@SearchValueType(name = "title", type = "string", format = "%%%s%%")
	})
	@ActionRoles({"certificateTypeList"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getCertificateTypePager()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		Map<String,Object> params=getSearchValue();
		if(null==params){
			params=new HashMap<String,Object>();
		}
		params.put("orgId", userOrgVO.getOrgId());
		
		if(pageSize == null || pageSize<1){
			pageSize = getPageSize();
		}
		Pager pager = new Pager(ServletActionContext.getRequest(),pageSize);
		pager = contactMgrService.getUserInfoCertificateTypePager(params, pager);
		if(pager.getCurrentPage()<=pager.getTotalPages()){
			List<CertificateTypeVO> list=(List<CertificateTypeVO>)pager.getPageData();
			UserOrgVO tmpUserOrgVO;
			for(CertificateTypeVO vo:list){
				tmpUserOrgVO=contactService.getUserOrgVOByUserName(userOrgVO.getUserName());
				vo.setCreator(tmpUserOrgVO.getPersonName());
				if("1".equals(vo.getStatus())){//1-启用；其它-禁用
					vo.setStatus("启用");
				}else{
					vo.setStatus("禁用");
				}
			}
		}
		addJsonPager("pageData", pager);
	}
	
	/**
	 * 查找证件类型列表
	 * @author lishengtao
	 * 2015-9-2
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getCertificateTypeList()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgId", userOrgVO.getOrgId());
		params.put("status", 1);
		List<TbQyUserInfoCertificateTypePO> list=contactMgrService.getUserInfoCertificateTypeList(params);
		addJsonArray("certificateTypeList", list);
		List<TbQyUserCustomOptionVO> optionVOs;
		if(VipUtil.isQwVip(userOrgVO.getOrgId())) {
			optionVOs = contactCustomMgrService.getUseingOptionByorgId(userOrgVO.getOrgId());

		}else{
			optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
		}
		addJsonObj("optionVOs", optionVOs);
	}
	
	/**
	 * @author lishengtao
	 * 2015-9-2
	 * 增加证件类型
	 * @throws Exception
	 * @throws BaseException
	 */
	@ActionRoles({"certificateTypeAdd"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "新增成功", faileMsg = "新增失败"))
	public void addCertificateType()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		if(null==tbQyUserInfoCertificateTypePO){
		    setActionResult("1002", "新建对象为空！");
		    return;
		}
		//判断title是否重复
		List<TbQyUserInfoCertificateTypePO> list=contactMgrService.getCertificateTypeListByTitle(tbQyUserInfoCertificateTypePO.getTitle(), userOrgVO.getOrgId());
		if(null!= list && list.size()>0){
		    setActionResult("1003", "证件类型标题已存在！");
		    return;
		}
		
		
		tbQyUserInfoCertificateTypePO.setCreator(userOrgVO.getUserName());
		tbQyUserInfoCertificateTypePO.setCreateTime(new Date());
		tbQyUserInfoCertificateTypePO.setOrgId(userOrgVO.getOrgId());
		contactService.insertPO(tbQyUserInfoCertificateTypePO, true);
	}
	
	/**
	 * @author lishengtao
	 * 2015-9-2
	 * 编辑证件类型
	 * @throws Exception
	 * @throws BaseException
	 */
	@ActionRoles({"certificateTypeEdit"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getCertificateType()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		HttpServletRequest request = ServletActionContext.getRequest();
		String id=request.getParameter("id");
		tbQyUserInfoCertificateTypePO=contactService.searchByPk(TbQyUserInfoCertificateTypePO.class, id);
		if(null==tbQyUserInfoCertificateTypePO){
		    setActionResult("1002", "数据不存在");
		    return;
		}
		if(!tbQyUserInfoCertificateTypePO.getOrgId().equals(userOrgVO.getOrgId())){
		    setActionResult("1003", "非本机构的成员不能访问");
		}
		addJsonObj("tQyUserInfoCertificateTypePO",tbQyUserInfoCertificateTypePO);
	}
	
	
	/**
	 * @author lishengtao
	 * 2015-9-2
	 * 编辑证件类型
	 * @throws Exception
	 * @throws BaseException
	 */
	@ActionRoles({"certificateTypeEdit"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "编辑成功", faileMsg = "编辑失败"))
	public void updateCertificateType()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		if(null==tbQyUserInfoCertificateTypePO){
		    setActionResult("1002", "编辑对象为空！");
		    return;
		}
		
		TbQyUserInfoCertificateTypePO po=contactService.searchByPk(TbQyUserInfoCertificateTypePO.class, tbQyUserInfoCertificateTypePO.getId());
		if(null==po || !po.getOrgId().equals(userOrgVO.getOrgId())){
		    setActionResult("1003", "非本机构的成员不能访问");
		}
		
		//判断title是否重复
		List<TbQyUserInfoCertificateTypePO> list=contactMgrService.getCertificateTypeListByTitle(tbQyUserInfoCertificateTypePO.getTitle(), userOrgVO.getOrgId());
		if(null!= list && list.size()>0 && !list.get(0).getId().equals(po.getId())){
		    setActionResult("1004", "证件类型标题已存在！");
		    return;
		}
		
		contactService.updatePO(tbQyUserInfoCertificateTypePO, false);
	}
	
	/**
	 * @author lishengtao
	 * 2015-9-6
	 * @throws Exception
	 * @throws BaseException
	 */
	@ActionRoles({"certificateTypeDel"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "删除成功", faileMsg = "删除失败"))
	public void batchDelCertificateType()throws Exception,BaseException{
		UserOrgVO userOrgVO = getUser();
		String msg=contactMgrService.delCertificateType(ids, userOrgVO);
		setActionResult("0",msg);
	}
	/**
	 * 根据姓名查询
	 * @throws Exception
	 * @throws BaseException
	 * @author Hejinjiao
	 * @2015-9-23
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxSearchUserByPersonName() throws Exception, BaseException {
		UserOrgVO userOrgVO = getUser();
		List<UserOrgVO> orgList=new ArrayList<UserOrgVO>();//机构列表
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgId", userOrgVO.getOrgId());
		if (StringUtil.isNullEmpty(keyWord)) {
			return;
		}
		//判断是否含有拼音
		Pattern patter = Pattern.compile("(?i)[a-z]");
		Matcher ma = patter.matcher(keyWord);
		StringBuilder sb = new StringBuilder("");
		// 在字母中间添加%
		if (ma.find()) {
			String pinyin=PingYinUtil.getPingYin(keyWord);
			for (char iterable_element : pinyin.toCharArray()) {
				sb.append(iterable_element);
			}
		}
		if (sb.length() > 0){
			params.put("pinyin",  "%"+sb+"%");
		}else {
			params.put("personName", "%"+keyWord+"%");
		}
		Pager pager = new Pager(ServletActionContext.getRequest(),pageSize);
		if( ManageUtil.superAdmin!=userOrgVO.getAge()){
			List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(userOrgVO.getPersonId(),userOrgVO.getOrgId());
			if (!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())) {
				String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){
					 deptList = this.departmentService.getDeptInfo(departIds);
				}
				params.put("deptList", deptList);
			}
			orgList.add(userOrgVO);
		}else{
			params.put("personName", "%"+keyWord+"%");
			params.remove("pinyin");
			orgList=this.contactMgrService.getOrgByPerson(params);
		}
		pager=contactMgrService.searchUserByPersonName(params,pager);
		List<TbQyUserInfoVO>list=(List<TbQyUserInfoVO>) pager.getPageData();
		addJsonArray("orgList",orgList);
		addJsonArray("userList",list);
		
	}
	
	/**
	 * 根据部门名称查询部门
	 * @throws Exception
	 * @throws BaseException
	 * @author Hejinjiao
	 * @2015-9-29
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询部门失败"))
	public void ajaxSearchDeptByDeptName() throws Exception, BaseException {
		UserOrgVO userOrgVO = getUser();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgId", userOrgVO.getOrgId());
		if (StringUtil.isNullEmpty(keyWord)) {
			return;
		}
		params.put("deptName", "%"+keyWord+"%");
		params.put("pageSize", pageSize);
		if( ManageUtil.superAdmin!=userOrgVO.getAge()){
			List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
			TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(userOrgVO.getPersonId(),userOrgVO.getOrgId());
			if (!AssertUtil.isEmpty(vo) && ManageUtil.RANGE_THREE.equals(vo.getRanges()) && !AssertUtil.isEmpty(vo.getDepartids())) {
				String[] departIds = vo.getDepartids().split("\\|");
				if(!AssertUtil.isEmpty(departIds)){
					 deptList = this.departmentService.getDeptInfo(departIds);
				}
				params.put("deptList", deptList);
			}
		}
		List<TbDepartmentInfoVO>list=contactMgrService.searchDeptByDeptName(params);
		addJsonArray("departList",list);
	}

	/**
	 *查询该用户是否保密
	 * @throws BaseException
	 * @throws Exception
	 */
	private TbQyUserSecrecyPO selectSecrecy(String orgId, String userId)throws Exception, BaseException{
		TbQyUserSecrecyPO secrecyPO = new TbQyUserSecrecyPO();
		boolean boo ;
		boo = contactMgrService.getSecrecyByUserId(userId, orgId);
		//如果该人员需要保密
		if (boo) {
			secrecyPO = new TbQyUserSecrecyPO();
			secrecyPO.setUserId(userId);
			secrecyPO.setOrgId(orgId);
		}
		return secrecyPO;

	}

	/**
	 *新增该用户是否保密
	 * @throws BaseException
	 * @throws Exception
	 */
	private TbQyUserSecrecyPO addSecrecy(String orgId, String userId)throws Exception, BaseException{
		TbQyUserSecrecyPO secrecyPO = new TbQyUserSecrecyPO();
		boolean boo ;
		secrecyPO.setOrgId(orgId);
		secrecyPO.setUserId(userId);
		boo = contactMgrService.getSecrecyByUserId(userId, orgId);
		if(!boo){
			contactMgrService.addSecrecy(secrecyPO);
		}
		return secrecyPO;

	}

	/**
	 *删除该用户是否保密
	 * @throws BaseException
	 * @throws Exception
	 */
	private TbQyUserSecrecyPO deleSecrecy(String orgId, String userId)throws Exception, BaseException{
		TbQyUserSecrecyPO secrecyPO = new TbQyUserSecrecyPO();
		boolean boo ;
		secrecyPO.setOrgId(orgId);
		secrecyPO.setUserId(userId);
		//查询数据库这个用户有没有保密
		boo = contactMgrService.getSecrecyByUserId(userId, orgId);
		if(boo){
			contactMgrService.deleSecrecy(secrecyPO);
		}
		return secrecyPO;
	}

	/**
	 * 查询自定义字段
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-10-25
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void searchCustom() throws BaseException, Exception {
		UserOrgVO userOrgVO = getUser();
		List<TbQyUserCustomOptionVO> optionVOs = contactCustomMgrService.getOptionByorgId(userOrgVO.getOrgId());
		//如果没有初始化30条数据
		if (null == optionVOs || 0 == optionVOs.size()) {
			optionVOs = contactCustomMgrService.batchAddOption(userOrgVO.getOrgId());
		}
		addJsonObj("optionVOs", optionVOs);
	}

	/**
	 * 更新自定义字段
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-1
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "编辑成功,请重新下载导入模板", faileMsg = "编辑失败"))
	public void updateCustom(@InterfaceParam(name = "strJson")@Validation(must = false, name = "strJson")String strJson) throws BaseException, Exception {
		UserOrgVO userOrgVO = getUser();
		JSONObject saveJson = JSONObject.fromObject(strJson);
		JSONObject jsonList = (JSONObject) saveJson.get("list");
		contactCustomMgrService.batchUpdateOptionAndDes(jsonList, userOrgVO.getOrgId());
	}

	/**
	 * 联系人批量导入模板
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-3
	 * @version 1.0
     */
	public void exportTemplateDemo() throws BaseException, Exception{
		logger.info("导出通讯录联系人模板开始");
		Date date = new Date();
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userName = user.getUsername();
		UserOrgVO userOrgVO = contactService.getOrgByUserId(userName);
		OutputStream os = null;
		List<TbQyUserCustomOptionVO> optionVOs;
		if(VipUtil.isQwVip(userOrgVO.getOrgId())) {
			optionVOs = contactCustomMgrService.getUseingOptionByorgId(userOrgVO.getOrgId());
		}else{
			optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
		}
		if(null == userOrgVO){
			logger.error("获取登录人" + userName + "orgId的信息为空");
			return;
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			os = response.getOutputStream();
			HttpServletRequest e = ServletActionContext.getRequest();
			String fileName = "通讯录联系人模板"  + ".xls";
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentType("application/msexcel");
			HSSFWorkbook writeWB = new HSSFWorkbook();
			Sheet writeSheet;
			Row writeRow;// 行
			Map<String, Integer> titleMap = new HashMap<String, Integer>();

			writeSheet = writeWB.createSheet("人员组织");// 创建页
			writeRow = writeSheet.createRow(0);// 创建行
			//创建第一页的行表头
			UserReportUtil.setFirstHead(titleMap, writeRow, false, optionVOs, false, userOrgVO.getOrgId());
			//设置第二页模板信息
			UserReportUtil.seCondUtil(writeWB,writeSheet, writeRow, optionVOs, userOrgVO);
			//写出Excel
			writeWB.write(os);
		}catch (Exception e){
			logger.error("导出通讯录联系人模板出现异常！"+ e.getMessage(), e);
		}finally {
			try {
				if (os != null) {
					os.close();
				}
				logger.error("导出通讯录联系人模板完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
			} catch (IOException e) {
				logger.error("通讯录联系人模板,关闭io异常！！", e);
			}
		}
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	/**
	 * 判断部门是否存在，把部门id放入对应的list里面
	 * @param d
	 * @param detId
	 * @param dept
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-1-10
	 * @version 1.0
     */
	private void judgeDepartAndAddDept(List<String> d, List<String> detId, String[] dept) throws BaseException, Exception{
		TbDepartmentInfoPO deptPO;
		for (String did : dept) {
			deptPO = departmentService.searchByPk(TbDepartmentInfoPO.class, did.trim());
			if(AssertUtil.isEmpty(deptPO) || AssertUtil.isEmpty(deptPO.getWxId())){
				//没找到对应的部门
				throw new NonePrintException("1001","部门不存在，请重新选择！");
			}
			if(AssertUtil.isEmpty(deptPO.getWxId())){//如果部门的微信部门id为空
				throw new NonePrintException("1001","部门【"+deptPO.getDepartmentName()+"】存在异常，请尝试删掉此部门，重新新增！");
			}
			d.add(deptPO.getWxId());
			detId.add(deptPO.getId());
		}
	}

	/**
	 * 设置用户关注状态
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-1-10
	 * @version 1.0
     */
	private void setUserStatus(WxUser user, TbQyUserInfoPO history) throws BaseException, Exception{
		//设置用户关注状态
		if("1".equals(user.getStatus())){
			if(!"2".equals(history.getUserStatus())){
				history.setUserStatus("2");
				history.setFollowTime(new Date());
			}
		}
		else{
			//取消关注的用户
			if("2".equals(history.getUserStatus())){
				history.setUserStatus("1");
				history.setCancelTime(new Date());
			}
			user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
		}
	}

	/**
	 * 更新微信的用户信息，错误就抛异常
	 * @param wxUserIsNull
	 * @param user
	 * @param org
	 * @param orgId
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-1-10
	 * @version 1.0
     */
	private void updateUser(boolean wxUserIsNull,WxUser user,UserOrgVO org,String orgId) throws BaseException, Exception{
		if(wxUserIsNull){
			//如果更新失败
			if(!WxUserService.addUser(user,org.getCorpId(),orgId)){
				throw new BaseException("1001", "更新用户信息失败");
			}
		}
		else{
			//如果更新失败
			if(!WxUserService.updateUser(user,org.getCorpId(),orgId)){
				throw new BaseException("1001", "更新用户信息失败");
			}
		}
	}

	/**
	 * 保存后台高级搜索的条件
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-10
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "保存成功", faileMsg = "保存失败"))
	public void updateSearchSenior(@InterfaceParam(name = "listJson")@Validation(must = true, name = "listJson")String listJson) throws BaseException, Exception{
		UserOrgVO org = getUser();
		JSONArray jsonArray = JSONArray.fromObject(listJson);
		TbQyUserSearchSeniorPO[] seniorPOs = (TbQyUserSearchSeniorPO[])JSONArray.toArray(jsonArray, TbQyUserSearchSeniorPO.class);
		List<TbQyUserSearchSeniorPO> seniorPOList = new ArrayList<TbQyUserSearchSeniorPO>(seniorPOs.length);
		for(int i = 0; i < seniorPOs.length; i ++){
			seniorPOList.add(seniorPOs[i]);
		}
		contactMgrService.batchUpdateSenior(org, seniorPOList);
	}

	/**
	 *	根据userId获取用户信息
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-3-8
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxGetUserInfoByUserId(@InterfaceParam(name = "userId")@Validation(must = true, name = "userId")String userId) throws BaseException, Exception{
		UserOrgVO org = getUser();
		if(AssertUtil.isEmpty(userId)){
			setActionResult("1001", "userId有误");
			return;
		}
		UserDeptInfoVO userInfo = contactMgrService.findUserDeptInfoByUserId(userId);
		if(!org.getOrgId().equals(userInfo.getOrgId())){
			setActionResult("1999", "非本机构人员不能访问本页面！");
			return;
		}
		addJsonObj("userInfo", userInfo);
	}

	/**
	 *获取离职人员的代办和交接事项
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-3-21
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void ajaxGetWaitTaskAndShift() throws BaseException, Exception{
		UserOrgVO org = getUser();
		TbQyUserInfoView vo = contactMgrService.viewUserById(id);
		int waitTaskCount = 0;
		UserInfoVO user = new UserInfoVO();
		BeanHelper.copyBeanProperties(user, vo);
		//获取设置了可见范围并当前用户拥有权限的应用
		List<TbQyAgentUserRefPO> agentList = experienceapplicationService.getAgentsByUserRef(user.getUserId(), user.getOrgId());
		Map<String, String> agentMap = new HashMap<String, String>();
		for (TbQyAgentUserRefPO agent : agentList) {
			agentMap.put(agent.getAgentCode(), agent.getAgentCode());
		}
		String agentCodes[]= Configuration.WAIT_AGENT_CODES.split("\\|");
		List<WaitingTaskVO> list=new ArrayList<WaitingTaskVO>();
		List<HandoverMatterVO> matterVOs = new ArrayList<HandoverMatterVO>();
		WaitingTaskVO waitingTaskVO=null;
		for(int x=0;x<agentCodes.length;x++){
			String agentCode=agentCodes[x];
			if(WxAgentUtil.isTrustAgent(org.getCorpId(),agentCode)){//是否托管
				IWaitingTask task= WaitingTaskManager.eventMap.get(agentCode);
				IHandoverMatter matter = IhandoverMatterManager.eventMap.get(agentCode);
				if(!AssertUtil.isEmpty(task)){//代办
					AgentCacheInfo agentInfo=WxAgentUtil.getAgentCacheInfo(user.getCorpId(),agentCode);
					//所有人员可见，或者应用可见权限
					if(null != agentInfo && (agentInfo.isAllUserVisible() || !AssertUtil.isEmpty(agentMap.get(agentCode)))){
						Integer unFinishWaitCount = task.loadWaitingTask(user);
						waitingTaskVO=new WaitingTaskVO();
						waitingTaskVO.setAgentCode(agentCode);
						waitingTaskVO.setAgentCodeName(agentInfo.getAppName());
						waitingTaskVO.setWaitCount(unFinishWaitCount);
						waitTaskCount = waitTaskCount + unFinishWaitCount;
						if(unFinishWaitCount > 0){//只显示没有完成的代办
							list.add(waitingTaskVO);
						}
					}
				}
				if (!AssertUtil.isEmpty(matter)) {//交接事项
					matterVOs.addAll(matter.loadHandoverMatter(vo.getUserId(), vo.getOrgId()));
				}
			}
		}
		addJsonObj("matterVOs", matterVOs);
		addJsonObj("matterCount", matterVOs.size());
		addJsonObj("list",list);
		addJsonObj("waitTaskCount", waitTaskCount);
	}

	/**
	 * 删除用户的手写签名图片
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void ajaxDeleteUserHand() throws BaseException, Exception{
		UserOrgVO org = getUser();
		//如果不是金卡vip
		if(!VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			setActionResult(VipUtil.noGoldVipCode, VipUtil.getNoGoldVipMsg(org.getCorpId()));
			return;
		}
		if(AssertUtil.isEmpty(ids)){
			setActionResult(ErrorCodeDesc.USER_NULL.getCode(), ErrorCodeDesc.USER_NULL.getDesc());
			return;
		}
		contactService.batchDel(TbQyUserHandWritPO.class, ids);
	}

	/**
	 * 新增或编辑用户的手写签名图片
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void ajaxUpdateUserHand(@InterfaceParam(name = "handId")@Validation(must = false, name = "handId")String handId,
								   @InterfaceParam(name = "userId")@Validation(must = true, name = "userId")String userId,
								   @InterfaceParam(name = "handWritPic")@Validation(must = true, name = "handWritPic")String handWritPic)throws BaseException, Exception{
		UserOrgVO org = getUser();
		//如果不是金卡vip
		if(!VipUtil.hasGoldPermission(org.getOrgId(),VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			setActionResult(VipUtil.noGoldVipCode, VipUtil.getNoGoldVipMsg(org.getCorpId()));
			return;
		}
		TbQyUserHandWritPO writPO = new TbQyUserHandWritPO();
		writPO.setUserId(userId);
		writPO.setHandWritPic(handWritPic);
		writPO.setOrgId(org.getOrgId());
		writPO.setUpdateTime(new Date());
		if(AssertUtil.isEmpty(handId)){//新增
			TbQyUserHandWritPO oldPO = contactService.getUserHandWritByUserId(userId);
			if(!AssertUtil.isEmpty(oldPO)){
				setActionResult(ErrorCodeDesc.HAS_HAND_WRITE.getDesc(), ErrorCodeDesc.HAS_HAND_WRITE.getDesc());
				return;
			}
			writPO.setId(UUID32.getID());
			contactService.insertPO(writPO, false);
		}else{//编辑
			writPO.setId(handId);
			contactService.updatePO(writPO, true);
		}
		addJsonObj("writPO",contactService.getUserHandAndUserInfoByUserId(userId));
	}

	/**
	 * 分页查询用户的手写签名图片
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void ajaxSearchUserHand() throws BaseException, Exception{
		UserOrgVO org =getUser();
		//如果不是金卡vip
		if(!VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			setActionResult(VipUtil.noGoldVipCode, VipUtil.getNoGoldVipMsg(org.getCorpId()));
			return;
		}
		if (pageSize == null || pageSize < 1) {
			pageSize = getPageSize();
		}
		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		addJsonPager("pager", contactMgrService.getUserHandByOrgId(pager, org));
	}

	/**
	 *获取默认搜索条件
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void getUserDefault() throws BaseException, Exception{
		UserOrgVO orgVO = getUser();
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(orgVO.getOrgId()));
		if(Configuration.IS_QIWEIYUN) {
			if (OrgUtil.getUserTotal(orgVO.getOrgId(), OrgUtil.USER_MEMBER) > Configuration.BIG_ORG_NUMBER ) {
				addJsonObj("superBigOrg", true);
			} else {
				addJsonObj("superBigOrg", false);
			}
		}else {
			addJsonObj("superBigOrg", false);
		}
	}

	/**
	 *插入或更新手机端默认搜索条件
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void insertOrUpdateDefaultSelect(@InterfaceParam(name = "selectPO")@Validation(must = true, name = "selectPO")TbQyUserDefaultSelectPO selectPO) throws BaseException, Exception{
		UserOrgVO orgVO =getUser();
		//如果是金卡用户
		if(VipUtil.hasGoldPermission(orgVO.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)) {
			contactMgrService.insertOrUpdateDefaultSelect(selectPO, orgVO.getOrgId());
		}
	}
}
