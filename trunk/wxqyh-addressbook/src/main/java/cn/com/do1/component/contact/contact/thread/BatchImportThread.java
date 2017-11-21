package cn.com.do1.component.contact.contact.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.util.ContactDictUtil;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.*;
import cn.com.do1.component.contact.department.util.CheckDeptVisible;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;

public class BatchImportThread implements Runnable {

	private final static transient Logger logger = LoggerFactory
			.getLogger(BatchImportThread.class);

	private String loginuser;
	private String orgId;
	//private String orgCode;
	private String corpId;
	private String type;
	
	private File upFile;
	private String upFileFileName;
	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IDepartmentService departmentService;
	private IPostService postService;

	// 本地部门缓存数据
	private List<TbDepartmentInfoPO> localDeptList;
	//本地证件类型缓存
	private List<TbQyUserInfoCertificateTypePO> localCertificateTypeList;
	private Map<String,String> certificateTypeMap=new HashMap<String,String>();//记录证件类型map<名称，ID>
	// 微信部门缓存数据
	private List<WxDept> wxDeptList;
	// 微信部门缓存数据
	private Map<String, WxDept> wxDeptMap = new HashMap<String, WxDept>(10);
	// 微信部门缓存数据
	private Map<String, WxDept> wxDeptMapId = new HashMap<String, WxDept>(10);
	/**
	 * 本地班级信息缓存
	 */
	private Map<String, TbDepartmentInfoEduPO> classMap = new HashMap<String, TbDepartmentInfoEduPO>(10);

	/**
	 * 本地部门缓存数据 key:departId
	 */
	private Map<String, TbDepartmentInfoPO> deparmentIdMap = new HashMap<String, TbDepartmentInfoPO>(10);
	private String id;
	private boolean isNotAutoCorpid = true;//是否不是本企业微信用户
	private List<TbQyUserInfoPO> addUserList = new ArrayList<TbQyUserInfoPO>(100);
	private List<TbQyUserInfoPO> updateUserList = new ArrayList<TbQyUserInfoPO>(100);
	private Map<String, List<String>> userRefDeptMap = new HashMap<String, List<String>>(100);
	private Map<String, List<String>> addDeptRefUserMap = null;
	private Map<String, List<String>> delDeptRefUserMap = null;
	private List<DeptSyncInfoVO> addDeptList = null;//需要新增的部门list
	private UserOrgVO org;
	private boolean isAllUserUsable = true;//是否所有人可见
	private Map<String, TbDepartmentInfoPO> userUsableDeptMap = null;//可见范围的部门信息
	private List<TbQyUserCustomOptionVO> optionVOs = null;
	private IContactCustomMgrService contactCustomMgrService ;
	private Set<String> secrecySet;

	public BatchImportThread(String loginuser, String orgId, String corpId) {
		this.loginuser = loginuser;
		this.orgId = orgId;
		this.corpId = corpId;
		this.contactService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactService.class);
		this.departmentService = DqdpAppContext.getSpringContext().getBean(
				"departmentService", IDepartmentService.class);
		this.contactMgrService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactMgrService.class);
		this.contactCustomMgrService = DqdpAppContext
				.getSpringContext().getBean("contactCustomService",
						IContactCustomMgrService.class);
		this.postService = DqdpAppContext
				.getSpringContext().getBean("postService", IPostService.class);
		if(!StringUtil.isNullEmpty(loginuser)){
			try {
				org =  contactService.getOrgByUserId(loginuser);
			} catch (Exception e) {
				logger.error("BatchImportThread getOrgByUserId "+loginuser,e);
			} catch (BaseException e) {
				logger.error("BatchImportThread getOrgByUserId "+loginuser,e);
			}
		}
		if(org == null){
			org = new UserOrgVO();
			org.setOrgId(orgId);
			org.setCorpId(corpId);
		}
	}

	public BatchImportThread(String id,String loginuser, String orgId, String orgCode,
			File upFile, String fileName, String corpId, String type, List<TbQyUserCustomOptionVO> optionVOs) {
		this.id=id;
		this.loginuser = loginuser;
		this.orgId = orgId;
		this.corpId = corpId;
		this.upFile = upFile;
		this.upFileFileName = fileName;
		this.type = type;
		this.optionVOs = optionVOs;
		this.contactService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactService.class);
		this.departmentService = DqdpAppContext.getSpringContext().getBean(
				"departmentService", IDepartmentService.class);
		this.contactMgrService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactMgrService.class);
		this.contactCustomMgrService = DqdpAppContext
				.getSpringContext().getBean("contactCustomService",
						IContactCustomMgrService.class);
		this.postService = DqdpAppContext
				.getSpringContext().getBean("postService", IPostService.class);
		if(!StringUtil.isNullEmpty(loginuser)){
			try {
				org =  contactService.getOrgByUserId(loginuser);
			} catch (Exception e) {
				logger.error("BatchImportThread getOrgByUserId "+loginuser,e);
			} catch (BaseException e) {
				logger.error("BatchImportThread getOrgByUserId "+loginuser,e);
			}
		}
		if(org == null){
			org = new UserOrgVO();
			org.setOrgId(orgId);
			org.setCorpId(corpId);
		}
	}

	@Override
	public void run() {
		logger.debug("导入通讯录开始,orgId:"+orgId+",corpId："+corpId);
		List<ImportVO> list = null;
		ResultVO resultvo=new ResultVO();
		ImportResultUtil.putResultObject(id, resultvo);
		ImportErrorVO error=new ImportErrorVO();
		ImportResultUtil.putErrorObject(id, error);
		int listNum = 1;//行数
		try {
			String[] title = ExcelUtil.getExcelTitle(upFile, upFileFileName,ImportVO.class, contactService,id);
			if (false == judgeTitleIsTrue(title, resultvo, optionVOs)) {//判断默认的模板跟导入的模板是否一致
				return;
			}
			// 获得excel数据
			list = ExcelUtil.importForExcel(upFile, upFileFileName, ImportVO.class, contactService, id, optionVOs);
			secrecySet = contactMgrService.getSecrecyByOrgId(orgId);
			Set<String> positionSet = postService.getPositionSetByOrgId(orgId);
			// 处理导入的数据
			if (list != null && !list.isEmpty()) {
				resultvo.setTotalNum(list.size());
				ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, orgId);
				// 获取本机构部门数据
				localDeptList = departmentService.getAllDepart(orgId);
				if (IndustryUtil.isEduVersion(orgId)) {//教育版班级部门组装
					localDeptList = SchoolClassUtil.assembleDepartmentEdu(localDeptList, deparmentIdMap);
					classMap = SchoolClassUtil.getClassMapByOrgId(orgId);
				}
				if(!WxAgentUtil.isAllUserUsable(corpId, WxAgentUtil.getAddressBookCode())){
					userUsableDeptMap = CheckDeptVisible.getDeptVisibleMap(corpId,orgId,WxAgentUtil.getAddressBookCode());
					isAllUserUsable = false;
				}
				//获取本机构的证件类型数据
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("orgId", orgPO.getOrganizationId());
				localCertificateTypeList=contactMgrService.getUserInfoCertificateTypeList(params);
				//转成map
				for(TbQyUserInfoCertificateTypePO po:localCertificateTypeList){
					//理论上title唯一，这里不做判断
					certificateTypeMap.put(po.getTitle(), po.getId());
				}
				
				String wxOrgId;
				//如果是道一企业微信，不能获取所有机构，防止部门过多导致内存溢出
				if(!corpId.equals(Configuration.AUTO_CORPID)){
					isNotAutoCorpid = false;
					// 获取微信的部门数据
					wxDeptList = WxDeptService.getDept(corpId,orgPO.getWxId(),orgId, WxAgentUtil.getAddressBookCode());
					if (AssertUtil.isEmpty(wxDeptList)) {
						WxDept wd = new WxDept();
						wd.setName("企业微信");
						wd.setParentid("0");
						wd = WxDeptService.addDept(wd, corpId,orgId);
						wxDeptList = new ArrayList<WxDept>();
						wxDeptList.add(wd);
					}
					// 通过迭代，将所有的父机构在微信中建立部门,获取到机构对应微信中的部门id
					wxOrgId = iterationAddDept(orgId);
				}
				else {
					wxOrgId = orgPO.getWxId();
				}
				int processNum=0;										//已处理数
				List<String> errorlist = resultvo.getErrorlist();
				if(errorlist == null){
					errorlist = new ArrayList<String>();
					resultvo.setErrorlist(errorlist);
				}
				List<String> repeatList = new ArrayList<String>();		// 重复账号数
				//错误数据  何金娇	2014/11/24
				List<ImportVO> errlist = error.getErrorlist();
				if(errlist == null){
					errlist = new ArrayList<ImportVO>();
					error.setErrorlist(errlist);
				}
				List<TbQyUserInfoPO> tempUserList = null;
				addDeptRefUserMap = new HashMap<String, List<String>>(100);
				delDeptRefUserMap = new HashMap<String, List<String>>(100);
				if ("1".equals(type)) {//需要更新用户信息
					for (ImportVO vo : list) {
						//匹配证件类型
						if(!AssertUtil.isEmpty(vo.getCertificateTypeTitle()) && !AssertUtil.isEmpty(certificateTypeMap.get(vo.getCertificateTypeTitle()))){
							vo.setCertificateType(certificateTypeMap.get(vo.getCertificateTypeTitle()));
						}
						try {
							listNum++;
							tempUserList = contactService.findRepeatUsersByCorpId(corpId,vo.getWxUserId(), null,null,null);
							//判断此用户账号是否在企业微信下存在
							if(tempUserList==null || tempUserList.size()==0){
								//如果账号不存在，更新账号
								//验证用户信息
								String msg = ContactImportUtil.verifyUserInfo(corpId, orgId,vo, positionSet);
								if(msg!=null){
									errorlist.add("第"+listNum+"行出现错误，错误提示："+msg+"<br/>");
									vo.setError(msg);
									errlist.add(vo);
									continue;
								}
								insertDataBase(vo, loginuser, wxOrgId, optionVOs);
							}
							else if(tempUserList.size()>1){
								errorlist.add("第"+listNum+"行出现错误，错误提示：账号存在异常，请联系管理员<br/>");
								vo.setError("账号存在异常，请联系管理员");
								errlist.add(vo);
								continue;
							}
							else{
								if(!tempUserList.get(0).getOrgId().equals(orgId)){
									errorlist.add("第"+listNum+"行出现错误，错误提示：账号已存在<br/>");
									vo.setError("账号已存在");
									errlist.add(vo);
									continue;
								}
								if("-1".equals(tempUserList.get(0).getUserStatus())){//离职
									errorlist.add("第"+listNum+"行出现错误，错误提示：用户已离职<br/>");
									vo.setError("用户已离职");
									errlist.add(vo);
									continue;
								}
								//验证用户信息
								String msg = ContactImportUtil.verifyUserInfo(corpId, orgId, vo, positionSet);
								if(msg!=null){
									errorlist.add("第"+listNum+"行出现错误，错误提示："+msg+"<br/>");
									vo.setError(msg);
									errlist.add(vo);
									continue;
								}
								//修改数据
								updateDateBase(vo, tempUserList.get(0).getId(), wxOrgId, optionVOs);
							}
							processNum=processNum+1;
							resultvo.setProcessNum(processNum);
						} catch (Exception e) {
							errorlist.add("第"+listNum+"行出现错误，错误提示："+e.getMessage()+"<br/>");
							vo.setError("系统异常："+e.getMessage());
							errlist.add(vo);
							logger.info("导入通讯录失败orgId："+orgId+"wxuserid:" + vo.getWxUserId(), e);
							continue;
						} catch (BaseException e) {
							errorlist.add("第"+listNum+"行出现错误，错误提示："+e.getMessage()+"<br/>");
							vo.setError("系统异常："+e.getMessage());
							errlist.add(vo);
							logger.info("导入通讯录失败orgId："+orgId+"wxuserid:" + vo.getWxUserId(), e);
							continue;
						}
					}
				}
				else{//不更新用户
					for (ImportVO vo : list) {
						//匹配证件类型
						if(!AssertUtil.isEmpty(vo.getCertificateTypeTitle()) && !AssertUtil.isEmpty(certificateTypeMap.get(vo.getCertificateTypeTitle()))){
							vo.setCertificateType(certificateTypeMap.get(vo.getCertificateTypeTitle()));
						}
						try {
							listNum++;
							tempUserList = contactService.findRepeatUsersByCorpId(corpId,vo.getWxUserId(), null,null,null);
							//判断此用户账号是否在企业微信下存在
							if(tempUserList==null || tempUserList.size()==0){
								//如果账号不存在，更新账号
								//验证用户信息
								String msg = ContactImportUtil.verifyUserInfo(corpId, orgId, vo, positionSet);
								if(msg!=null){
									errorlist.add("第"+listNum+"行出现错误，错误提示："+msg+"<br/>");
									vo.setError(msg);
									errlist.add(vo);
									continue;
								}
								insertDataBase(vo, loginuser, wxOrgId, optionVOs);
								//maquanyang 2015-7-21 导入成功后才更新成功数量
								processNum=processNum+1;
							}
							else if(tempUserList.size()>1){
								errorlist.add("第"+listNum+"行出现错误，错误提示：账号存在异常，请联系管理员<br/>");
								vo.setError("账号存在异常，请联系管理员");
								errlist.add(vo);
								continue;
							}
							else{
								if(!tempUserList.get(0).getOrgId().equals(orgId)){
									errorlist.add("第"+listNum+"行出现错误，错误提示：账号已存在<br/>");
									vo.setError("账号已存在");
									errlist.add(vo);
									continue;
								}
								if("-1".equals(tempUserList.get(0).getUserStatus())){//离职
									errorlist.add("第"+listNum+"行出现错误，错误提示：用户已离职<br/>");
									vo.setError("用户已离职");
									errlist.add(vo);
									continue;
								}
								else{
									//maquanyang 2015-7-21 同企业微信下存在不能导入同一账号
									errorlist.add("第"+listNum+"行出现错误，错误提示：账号已存在<br/>");
									vo.setError("账号已存在");
									errlist.add(vo);
									repeatList.add(tempUserList.get(0).getWxUserId());
								}
							}
							resultvo.setProcessNum(processNum);
						} catch (Exception e) {
							errorlist.add("第"+listNum+"行出现错误，错误提示："+e.getMessage()+"<br/>");
							vo.setError("系统异常："+e.getMessage());
							errlist.add(vo);
							logger.info("导入通讯录失败orgId："+orgId+"wxuserid:" + vo.getWxUserId(), e);
							continue;
						} catch (BaseException e) {
							errorlist.add("第"+listNum+"行出现错误，错误提示："+e.getMessage()+"<br/>");
							vo.setError("系统异常："+e.getMessage());
							errlist.add(vo);
							logger.info("导入通讯录失败orgId："+orgId+"wxuserid:" + vo.getWxUserId(), e);
							continue;
						}
					}
				}
				
				resultvo.setRepeatList(repeatList);
				
			}
			else{
				resultvo.setTotalNum(0);
				resultvo.setProcessNum(0);
			}
		} catch (Exception e) {
			List<String> errorlist = new ArrayList<String>();
			errorlist.add("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
			resultvo.setErrorlist(errorlist);
			//错误数据
			List<ImportVO> errlist=new ArrayList<ImportVO>();
			for (int i =listNum-1; i < resultvo.getTotalNum(); i++) {
				ImportVO vo=list.get(i);
				if (i ==listNum-1) {
					vo.setError("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
				}
				errlist.add(vo);
			}
			error.setErrorlist(errlist);
			logger.info("导入通讯录失败orgId："+orgId, e);
		} catch (BaseException e) {
			List<String> errorlist = new ArrayList<String>();
			errorlist.add("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
			resultvo.setErrorlist(errorlist);
			//错误数据
			List<ImportVO> errlist=new ArrayList<ImportVO>();
			for (int i =listNum-1; i < resultvo.getTotalNum(); i++) {
				ImportVO vo=list.get(i);
				if (i ==listNum-1) {
					vo.setError("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
				}
				errlist.add(vo);
			}
			error.setErrorlist(errlist);
			logger.info("导入通讯录失败orgId："+orgId, e);
		} finally {
			resultvo.setFinish(true);
			error.setFinish(true);
			upFile.delete();
			UserInfoChangeNotifier.batchChangeDept(org,addDeptList,null,null, UserInfoChangeInformType.IMPORT_MGR);
			UserInfoChangeNotifier.importEnd(org,addUserList,updateUserList,null,userRefDeptMap,addDeptRefUserMap,delDeptRefUserMap);
			logger.debug("导入通讯录完成,orgId:"+orgId+",corpId："+corpId+",processNum:"+resultvo.getProcessNum()+",totalNum:"+resultvo.getTotalNum()+",errorSize:"+(error.getErrorlist()==null?0:error.getErrorlist().size()));
		}
	}

	/**
	 * 迭代新增机构作为微信的部门
	 * 
	 * @param parentId 父部门id
	 * @throws Exception 这是一个异常
	 * @author Sun Qinghai
	 * @throws BaseException 这是一个异常
	 * @2014-8-1
	 * @version 1.0
	 */
	public String iterationAddDept(String parentId) throws Exception,
			BaseException {
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, parentId);
		if (!AssertUtil.isEmpty(orgPO.getWxId())) {
			// 如果机构的微信id不为空
			return orgPO.getWxId();
		}
		else{
			// 如果该机构的父机构为顶级机构，将该机构对应到微信的根部门下
			if (Configuration.TOP_ORG_ID.equals(orgPO.getParentId())) {
				// 获取组织机构的微信部门
				WxDept orgDept = getDeptByOrgName(orgPO.getOrganizationDescription(),orgPO.getWxId());
				if (AssertUtil.isEmpty(orgDept)) {
					orgDept = new WxDept();
					orgDept.setName(orgPO.getOrganizationName());
					orgDept.setParentid("1");
					orgDept = WxDeptService.addDept(orgDept, corpId,orgId);

					// 更新组织机构的微信部门id
					ExtOrgPO orgPOUpdate = new ExtOrgPO();
					orgPOUpdate._setPKValue(orgPO._getPKValue());
					orgPOUpdate.setWxId(orgDept.getId());
					orgPOUpdate.setWxParentid("1");
					departmentService.updatePO(orgPOUpdate, false);
				}
				return orgDept.getId();
			} else {
				// 迭代获取父机构的
				String wxDeptId = iterationAddDept(orgPO.getParentId());

				WxDept orgDept = getDeptByNameAndParentId(
						orgPO.getOrganizationName(), wxDeptId,orgPO.getWxId());
				if (orgDept==null) {
					orgDept = new WxDept();
					orgDept.setName(orgPO.getOrganizationName());
					orgDept.setParentid(wxDeptId);
					orgDept = WxDeptService.addDept(orgDept, corpId,orgId);
				}

				// 更新组织机构的微信部门id
				ExtOrgPO orgPOUpdate = new ExtOrgPO();
				orgPOUpdate._setPKValue(orgPO._getPKValue());
				orgPOUpdate.setWxId(orgDept.getId());
				orgPOUpdate.setWxParentid(wxDeptId);
				departmentService.updatePO(orgPOUpdate, false);
				return orgDept.getId();
			}
		}
	}

	/**
	 * 插入本系统数据库并同步到微信
	 * @param vo
	 * @param loginuser
	 * @param wxOrgDeptId
	 *            机构对于的微信部门id
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-1
	 * @version 1.0
	 */
	public String insertDataBase(ImportVO vo, String loginuser, String wxOrgDeptId, List<TbQyUserCustomOptionVO> optionVOs)
			throws Exception, BaseException {
		TbQyUserInfoPO userPO = new TbQyUserInfoPO();
		// 插入用户
		userPO.setId(ContactUtil.getUserId(corpId, vo.getWxUserId()));
		userPO.setUserId(userPO.getId());
		String department=vo.getDeptFullName().replace("；", ";");
		String[] split = department.split(";");
		int length = split.length;
		TbDepartmentInfoPO deptPO;
		List<String> d = new ArrayList<String>(length);
		List<String> detId = new ArrayList<String>(length);
		// 处理部门，不存在的部门自动新建
		for (int i = 0; i < length; i++) {
			deptPO = doLocalDept(split[i].trim(),wxOrgDeptId);
			d.add(deptPO.getWxId());
			detId.add(deptPO.getId());
		}
		
		// 先同步用户到微信
		WxUser user = new WxUser();
		user.setDepartment(d);
		user.setUserid(vo.getWxUserId());
		user.setName(vo.getPersonName().trim());
		user.setEmail(vo.getEmail());
		user.setMobile(vo.getMobile());
		user.setPosition(vo.getPosition());
		user.setWeixinid(vo.getWeixinNum());
		WxUserService.addUser(user, corpId,orgId);
		userPO.setOrgId(orgId);
		userPO.setPersonName(vo.getPersonName().trim());
		userPO.setPinyin(PingYinUtil.getPingYin(userPO.getPersonName()));
		userPO.setEmail(vo.getEmail());
		userPO.setMobile(vo.getMobile());
		userPO.setShorMobile(vo.getShorMobile());
		userPO.setWeixinNum(vo.getWeixinNum());
		userPO.setQqNum(vo.getQqNum());
		userPO.setPosition(vo.getPosition());
		userPO.setBirthday(vo.getBirthday());
		userPO.setAddress(vo.getAddress());
		userPO.setMark(vo.getMark());
		userPO.setNickName(vo.getNickName());
		userPO.setPhone(vo.getPhone());
		userPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
		userPO.setIsConcerned("0");
		userPO.setCreatePerson(loginuser);
		userPO.setCreateTime(new Date());
		userPO.setCorpId(corpId);
		userPO.setWxUserId(vo.getWxUserId());
		userPO.setHeadPic(ContactUtil.DEFAULT_HEAD_PIC);
		userPO.setLunarCalendar(vo.getLunarCalendar());
		userPO.setAttribute(ContactImportUtil.getUesrAttribute(vo.getAttribute()));
		userPO.setHasChild(ContactImportUtil.hasnone_child);
		boolean isVip = VipUtil.isQwVip(orgId);
		ContactImportUtil.setUpUserTop(isVip, userPO, vo);
		userPO.setSex(ContactImportUtil.getUserSex(vo.getSex()));
		//加入入职时间  maquanyang 2015-6-9
		userPO.setEntryTime(vo.getEntryTime());
		//修改生日提醒  maquanyang 2015-6-17
		if(!StringUtil.isNullEmpty(vo.getRemindType())){
			userPO.setRemindType("按农历".equals(vo.getRemindType().replaceAll("\\s*", "")) ? ContactUtil.REMIND_TYPE_ZERO : ContactUtil.REMIND_TYPE_ONE);
		}else{
			userPO.setRemindType(ContactUtil.REMIND_TYPE_ONE);
		}
		//lishegntao 2015-9-6 增加证件类型的导入
		userPO.setCertificateType(vo.getCertificateType());
		userPO.setCertificateContent(vo.getCertificateContent());
		userPO.setIdentity(vo.getIdentity());
		contactService.insertUser(userPO, detId);
		List<TbQyUserCustomItemPO> itemPOs = new ArrayList<TbQyUserCustomItemPO>();
		ContactCustomUtil.setUserToItem(vo,userPO, itemPOs, optionVOs);
		contactCustomMgrService.batchAddItem(itemPOs);
		addUserList.add(userPO);
		userRefDeptMap.put(userPO.getUserId(),detId);
		UserInfoChangeNotifier.getDeptUserRef(detId,userPO.getUserId(),addDeptRefUserMap);
		if(isVip) {
			ContactImportUtil.saveSecrecy(vo.getSecrecy(), orgId, userPO.getUserId());
		}
		return id;
	}

	/**
	 * 解析用户导入的部门数据，返回最底层部门，父级部门没有会自动创建
	 * 
	 * @param deptFullName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	public TbDepartmentInfoPO doLocalDept(String deptFullName,
			String wxOrgDeptId) throws Exception, BaseException {
		if(!isAllUserUsable){
			String[] deptList = deptFullName.split("->");
			if(!userUsableDeptMap.containsKey(deptList[0])){
				throw new NonePrintException("-1",String.format("亲爱的用户，你的通讯录应用未开放完整通讯录权限，无法管理部门[%s]，请在企业微信后台->【应用中心】->【授权的应用】中找到【通讯录】应用->修改【通讯录范围】为最顶级部门。",deptList[0]));
			}
		}
		// 导入数据中的所属部门为：一级部门->二级部门->三级部门
		TbDepartmentInfoPO dept = getDeptByFullName(deptFullName);
		if (dept != null) {
			return dept;
		}
		String[] deptList = deptFullName.split("->");
		String fullName = "";
		String parentId = "";
		//初始化一级部门
		TbDepartmentInfoPO parentDept = new TbDepartmentInfoPO();
		parentDept.setWxId(wxOrgDeptId);
		WxDept wxdept = null;
		for (int i = 0; i < deptList.length; i++) {
			if (i == 0)
				fullName += deptList[i];
			else
				fullName += "->" + deptList[i];
			// 根据部门名称以及父部门ID唯一定位部门
			dept = getDeptByDeptName(deptList[i], parentId);
			if (dept == null) {
		    	if(StringVerifyUtil.verifyRules(deptList[i])){
		    		throw new BaseException("部门名称不能包括："+StringVerifyUtil.getVerifyMatche());
		    	}
				dept = new TbDepartmentInfoPO();
				dept.setId(UUID32.getID());
				dept.setDepartmentName(deptList[i]);
				dept.setDeptFullName(fullName);
				dept.setParentDepart(parentId);
				dept.setOrgId(orgId);
				dept.setCreatePerson(loginuser);
				dept.setCreateTime(new Date());
				dept.setShowOrder(1000);
				dept.setWxParentid(parentDept.getWxId());
				//设置部门权限
				if (AssertUtil.isEmpty(dept.getPermission())) {
					dept.setPermission(parentDept.getPermission());
				}else {
					if (!AssertUtil.isEmpty(parentDept.getPermission())) {
						if (dept.getPermission().compareTo(parentDept.getPermission())<0) {//子部门权限不能比父部门权限大
							dept.setPermission(parentDept.getPermission());
						}
					}
				}
				//教育版部门导入
				if (IndustryUtil.isEduVersion(orgId)) {
					//最低级部门是否为班级部门
					if (i == deptList.length - 1 && SchoolClassUtil.judgementSchoolClass(deptFullName)) {
						TbDepartmentInfoPO classDepart = new TbDepartmentInfoPO();
						BeanHelper.copyBeanProperties(classDepart, dept);
						localDeptList.add(classDepart);
						dept.setAttribute(1);
						SchoolClassUtil.addDeptEdu(dept, classMap, deparmentIdMap);
					} else {
						localDeptList.add(dept);
						dept.setAttribute(2);
					}
				} else {
					localDeptList.add(dept);
					dept.setAttribute(0);
				}
				deparmentIdMap.put(dept.getId(), dept);
				wxdept = doWxDept(dept);
				dept.setWxId(wxdept.getId());
				dept.setId(DepartmentUtil.getDeptId(corpId, wxdept.getId()));
				departmentService.insertPO(dept, false);
				if(addDeptList == null){
					addDeptList = new ArrayList<DeptSyncInfoVO>(10);
				}
				DeptSyncInfoVO vo = new DeptSyncInfoVO();
				BeanHelper.copyBeanProperties(vo,dept);
				addDeptList.add(vo);
			}
			// 将本部门的id作为下一级部门的父节点Id
			parentId = dept.getId();
			parentDept = dept;
		}
		return dept;
	}

	public String getParentIdByFullName(String fullName) {
		String parentId = "";
		String[] deptName = fullName.split("->");
		if (deptName.length == 1)
			parentId = "";
		else {
			String fName = "";
			for (int i = 0; i < (deptName.length - 1); i++) {
				if (i == 0)
					fName += deptName[i];
				else
					fName += "->" + deptName[i];
				TbDepartmentInfoPO po = getDeptByFullName(fName);
				parentId = po.getId();
			}
		}
		return parentId;
	}

	public TbDepartmentInfoPO getDeptByFullName(String deptFullName) {
		if (!AssertUtil.isEmpty(localDeptList)) {
			for (TbDepartmentInfoPO po : localDeptList) {
				// 部门全称完全匹配
				if (po.getDeptFullName().equals(deptFullName))
					return po;
			}
		}
		return null;
	}

	public TbDepartmentInfoPO getDeptByDeptName(String deptName, String parentId) {
		if (!AssertUtil.isEmpty(localDeptList)) {
			for (TbDepartmentInfoPO po : localDeptList) {
				// 同一个父部门下不能存在同名子部门，当且仅当名称相同，归属同一父部门时才是同一部门
				if (po.getDepartmentName().equals(deptName)
						&& po.getParentDepart().equals(parentId))
					return po;
			}
		}
		return null;
	}

	/**
	 * 新增部门信息到微信
	 * 
	 * @param deptPO
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-4
	 * @version 1.0
	 */
	public WxDept doWxDept(TbDepartmentInfoPO deptPO) throws Exception,
			BaseException {
		WxDept wxDept = getDeptByNameAndParentId(deptPO.getDepartmentName(),
				deptPO.getWxParentid(),deptPO.getWxId());
		if (wxDept == null) {
			// 不存在此部门，调接口创建
			wxDept = new WxDept();
			wxDept.setName(deptPO.getDepartmentName());
			wxDept.setParentid(deptPO.getWxParentid());
			wxDept = WxDeptService.addDept(wxDept, corpId,orgId);
			if(isNotAutoCorpid){//如果不是本企业微信的人员
				if (wxDeptMap == null) {
					wxDeptMap = new HashMap<String, WxDept>(10);
				}
				wxDeptMap.put(wxDept.getName() + "_" + wxDept.getParentid(), wxDept);
				wxDeptMapId.put(wxDept.getId(), wxDept);
			}
		}
		return wxDept;
	}

	/**
	 * 用于后台管理中的新增部门
	 * 
	 * @param deptPO
	 * @param wxOrgid
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-4
	 * @version 1.0
	 */
	public WxDept doWxDept(TbDepartmentInfoPO deptPO, String wxOrgid)
			throws Exception, BaseException {
		WxDept wxDept = getDeptByNameAndParentId(deptPO.getDepartmentName(),
				deptPO.getWxParentid(),deptPO.getWxId());
		if (wxDept == null) {
			// 不存在此部门，调接口创建
			wxDept = new WxDept();
			wxDept.setName(deptPO.getDepartmentName());
			wxDept.setParentid("1".equals(deptPO.getWxParentid()) ? wxOrgid
					: deptPO.getWxParentid());
			wxDept = WxDeptService.addDept(wxDept, corpId,orgId);
			if(isNotAutoCorpid){//如果不是本企业微信的人员
				if (wxDeptMap == null) {
					wxDeptMap = new HashMap<String, WxDept>(10);
				}
				wxDeptMap.put(wxDept.getName() + "_" + wxDept.getParentid(), wxDept);
				wxDeptMapId.put(wxDept.getId(), wxDept);
			}
		}
		return wxDept;
	}

	/**
	 * 获得机构在微信后台的部门对象
	 * 
	 * @param wxId
	 * @param deptName
	 * @return
	 */
	public WxDept getDeptByOrgName(String deptName, String wxId) {// 获取组织机构的微信部门
		WxDept orgDept = getDeptByNameAndParentId(deptName, "1",wxId);
		return orgDept;
	}

	/**
	 * 通过部门名称及父部门ID获得唯一部门
	 * 
	 * @param dName
	 * @param parentId
	 * @return
	 */
	public WxDept getDeptByNameAndParentId(String dName, String parentId, String wxId) {
		handleDeptToMap();
		if (wxDeptMap != null) {
			if(wxId!=null){
				return wxDeptMapId.get(wxId);
			}
			return wxDeptMap.get(dName + "_" + parentId);
		}
		return null;
	}

	/**
	 * 将部门转为map
	 * 
	 * @author Sun Qinghai
	 * @2014-8-1
	 * @version 1.0
	 */
	private void handleDeptToMap() {
		if (wxDeptList != null && wxDeptList.size() > 0 && wxDeptMap == null) {
			wxDeptMap = new HashMap<String, WxDept>(wxDeptList.size());
			wxDeptMapId = new HashMap<String, WxDept>(wxDeptList.size());
			for (WxDept dept : wxDeptList) {
				if (dept != null) {
					wxDeptMap.put(dept.getName() + "_" + dept.getParentid(),
							dept);
					wxDeptMapId.put(dept.getId(), dept);
				}
			}
			wxDeptList = null;
		}
	}

	public static void main0(String[] s) {
		WxUser user = new WxUser();
		String id = UUID32.getID();
		System.out.println(id);
		user.setUserid("qy_15802658040");
		user.setName("test");
		user.setEmail("");
		//user.setGender("1");
		user.setMobile("15802658040");
		user.setPosition("test");
		user.setWeixinid("");
		List d = new ArrayList();
		d.add("28");
		d.add("29");
		user.setDepartment(d);
		/*try {
			WxUserService.addUser(user, "","");
		} catch (Exception e) {
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
	}

	public static void main(String[] s) {
		// WxDept wd = new WxDept();
		// wd.setName("test");
		// wd.setParentid("1");
		// System.out.println(WxDeptService.addDept(wd));

		// System.out.println(WxDeptService.getDept());

		// System.out.println("1@a.com".matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"));

		// wxUser.setUserid("qy_15802658000");
		/*try {
			WxUser wxUser = WxUserService.getUser("qy_15802658045", "");
			wxUser.setName("欧阳磊1");
			wxUser.setMobile("15802658000");
			WxUserService.updateUser(wxUser, "");
		} catch (Exception e) {
		} catch (BaseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
	}

	public static void main3(String[] s) {
		Set set = new HashSet();
		System.out.println(set.add("1"));
		System.out.println(set.add("1"));
		System.out.println(set);
	}

	public void setWxDeptList(List<WxDept> wxDeptList) {
		this.wxDeptList = wxDeptList;
	}

	/**
	 * 更新已有的用户
	 * 
	 * @param vo
	 * @param wxOrgDeptId
	 * @param personId
	 * @throws Exception
	 * @throws BaseException
	 * @author Hejinjiao
	 * @2014-9-26
	 * @version 1.0
	 */
	public void updateDateBase(ImportVO vo, String personId, String wxOrgDeptId, List<TbQyUserCustomOptionVO> optionVOs)
			throws Exception, BaseException {
		List<String> d = new ArrayList<String>();
		List<String> detId = new ArrayList<String>();
		// 处理部门，不存在时自动创建
		if (AssertUtil.isEmpty(vo.getDeptFullName())) {
			d.add(wxOrgDeptId);
		} else {
			String department=vo.getDeptFullName().replace("；", ";");
			String[] split = department.split(";");
			int length = split.length;
			TbDepartmentInfoPO deptPO;
			// 处理部门，不存在的部门自动新建
			for (int i = 0; i < length; i++) {
				deptPO = doLocalDept(split[i].trim(),wxOrgDeptId);
				d.add(deptPO.getWxId());
				detId.add(deptPO.getId());
			}
		}
		TbQyUserInfoPO history = contactService.searchByPk(TbQyUserInfoPO.class, personId);
		boolean isUpdate=false; //判断是已经有更新冗余字段，暂定名称、头像
		if(history!=null && !AssertUtil.isEmpty(history.getPersonName()) && !history.getPersonName().equals(vo.getPersonName())){
			isUpdate=true;
		}
		history.setPersonName(vo.getPersonName().trim());
		history.setPinyin(PingYinUtil.getPingYin(history.getPersonName()));
		WxUser user = WxUserService.getUser(history.getWxUserId(), corpId,orgId, WxAgentUtil.getAddressBookCode());
		boolean wxUserIsNull = false;
		if (user == null) {
			wxUserIsNull = true;
			user = new WxUser();
			user.setWeixinid(vo.getWeixinNum());
		}
		else{
			logger.info ("-----------微信返回的历史数据-----------:"+user.toString());
		}
		//设置用户关注状态
		if(ContactDictUtil.WX_USER_STAtUS_FOLLOW.equals(user.getStatus())){
			if(!ContactDictUtil.USER_STAtUS_FOLLOW.equals(history.getUserStatus())){
				history.setUserStatus(ContactDictUtil.USER_STAtUS_FOLLOW);
				history.setFollowTime(new Date());
			}
			//同步微信的微信号	Hejinjiao	2014/10/22
			history.setWeixinNum(user.getWeixinid());
		}
		else{
			//取消关注的用户
			if(ContactDictUtil.USER_STAtUS_FOLLOW.equals(history.getUserStatus())){
				history.setUserStatus(ContactDictUtil.USER_STAtUS_CANCEL);
				history.setCancelTime(new Date());
			}
			user.setWeixinid(history.getWeixinNum());
		}
		history.setHeadPic(ContactUtil.getWeixinUserHeadPic(user.getAvatar()));
		user.setMobile(vo.getMobile());
		user.setEmail(vo.getEmail());
		user.setName(history.getPersonName());
		if (AssertUtil.isEmpty(user.getWeixinid())) {
			user.setWeixinid(vo.getWeixinNum());
		}else {
			if (ContactUtil.WX_USER_STAtUS_CANCEL.equals(user.getStatus())) {
				user.setWeixinid(vo.getWeixinNum());
			}
		}
		history.setSex(ContactImportUtil.getUserSex(vo.getSex()));
		user.setGender(history.getSex());
		user.setPosition(vo.getPosition());
		user.setDepartment(d);
		if(wxUserIsNull){
			user.setUserid(history.getWxUserId());
			//如果更新失败
			if(!WxUserService.addUser(user,corpId,orgId)){
				throw new BaseException("更新用户信息失败");
			}
		}else{
			//如果更新失败
			if(!WxUserService.updateUser(user,corpId,orgId)){
				throw new BaseException("更新用户信息失败");
			}
		}

		history.setEmail(vo.getEmail());
		history.setMobile(vo.getMobile());
		history.setQqNum(vo.getQqNum());
		history.setPosition(vo.getPosition());
		history.setBirthday(vo.getBirthday());
		history.setAddress(vo.getAddress());
		history.setShorMobile(vo.getShorMobile());
		history.setMark(vo.getMark());
		history.setNickName(vo.getNickName());
		history.setPhone(vo.getPhone());
		history.setIsConcerned("0");
		history.setUpdateTime(new Date());
		history.setLunarCalendar(vo.getLunarCalendar());
		history.setWeixinNum(vo.getWeixinNum());
		history.setAttribute(ContactImportUtil.getUesrAttribute(vo.getAttribute()));
		//加入入职时间  maquanyang 2015-6-9
		history.setEntryTime(vo.getEntryTime());
		//修改生日提醒  maquanyang 2015-6-17
		if(!StringUtil.isNullEmpty(vo.getRemindType())){
			history.setRemindType("按农历".equals(vo.getRemindType().replaceAll("\\s*", "")) ? ContactUtil.REMIND_TYPE_ZERO : ContactUtil.REMIND_TYPE_ONE);
		}else{
			history.setRemindType(ContactUtil.REMIND_TYPE_ONE);
		}
		//lishengtao 2015-9-6 增加证件信息导入
		history.setCertificateType(vo.getCertificateType());
		history.setCertificateContent(vo.getCertificateContent());
		history.setIdentity(vo.getIdentity());
		boolean isVip = VipUtil.isQwVip(orgId);
		ContactImportUtil.setUpUserTop(isVip, history, vo);
		if(isVip){
			if(secrecySet.contains(history.getUserId())){//如果原来是有开启保密的
				if("否".equals(vo.getSecrecy())){//如果用户不再需要保密
					TbQyUserSecrecyPO po = new TbQyUserSecrecyPO();
					po.setUserId(history.getUserId());
					po.setOrgId(orgId);
					contactMgrService.deleSecrecy(po);
				}
			} else {
				ContactImportUtil.saveSecrecy(vo.getSecrecy(), orgId, history.getUserId());
			}
		}
		//本教师子女：默认为否
		if (AssertUtil.isEmpty(history.getHasChild())) {
			history.setHasChild(ContactImportUtil.hasnone_child);
		}
		UpdateUserResult uur = contactService.updateUser(history,detId, true);
		//从数据库中获取该用户已填写的自定义字段的值
		List<TbQyUserCustomOptionVO> sqlOptionVOs = contactCustomMgrService.getUserItemList(history.getUserId(), history.getOrgId());
		if(sqlOptionVOs.size() == 0){//如果原来数据库没有自定义字段
			List<TbQyUserCustomItemPO> itemPOs = new ArrayList<TbQyUserCustomItemPO>();
			ContactCustomUtil.setUserToItem(vo, history, itemPOs, optionVOs);
			contactCustomMgrService.batchAddItem(itemPOs);
		}else{
			Map<String, TbQyUserCustomOptionVO> optionVOMap = new HashMap<String, TbQyUserCustomOptionVO>(sqlOptionVOs.size());
			for(int i = 0; i < sqlOptionVOs.size(); i ++){
				if(!AssertUtil.isEmpty(sqlOptionVOs.get(i).getItemVO())) {
					optionVOMap.put(sqlOptionVOs.get(i).getId(), sqlOptionVOs.get(i));
				}
			}
			List<TbQyUserCustomItemPO> addItemPOs = new ArrayList<TbQyUserCustomItemPO>();
			List<TbQyUserCustomItemPO> updateItemPOs = new ArrayList<TbQyUserCustomItemPO>();
			if(!AssertUtil.isEmpty(vo.getItemVOs())) {
				for(int i = 0; i < optionVOs.size(); i++){//自定义字段的值
					String value = vo.getItemVOs().get(optionVOs.get(i).getId());
					if(!AssertUtil.isEmpty(value)) {//如果值不为空
						TbQyUserCustomItemPO itemPO = new TbQyUserCustomItemPO();
						if(optionVOMap.containsKey(optionVOs.get(i).getId())){//如果原来是有值的
							itemPO.setId(optionVOMap.get(optionVOs.get(i).getId()).getItemVO().getId());
							itemPO.setOrgId(history.getOrgId());
							itemPO.setContent(value);
							updateItemPOs.add(itemPO);
						}else {//如果是没有值的
							itemPO.setId(UUID32.getID());
							itemPO.setCreateTime(new Date());
							itemPO.setUserId(history.getUserId());
							itemPO.setOrgId(history.getOrgId());
							itemPO.setOptionId(optionVOs.get(i).getId());
							itemPO.setContent(vo.getItemVOs().get(optionVOs.get(i).getId()));
							addItemPOs.add(itemPO);
						}
					}
				}
			}
			contactCustomMgrService.batchAddItem(addItemPOs);
			contactCustomMgrService.batchUpdateItem(updateItemPOs);
		}
		updateUserList.add(history);
		userRefDeptMap.put(history.getUserId(),detId);
		if(uur.isUpdateDept()){//更新了部门信息
			UserInfoChangeNotifier.getDeptUserRef(uur.getOldDeptIds(),detId,history.getUserId(),addDeptRefUserMap,delDeptRefUserMap);
		}
		//修改缓存中的人员数据
		CacheSessionManager.update(history.getUserId());
		try {
			//记录用户更新的数据，以便更新冗余字段 chenfeixiong 2015/06/30
			if(isUpdate){
				Map<String,String> map=new HashMap<String,String>();
				map.put("creator", "admin");
				map.put("userId", history.getUserId());
				map.put("orgId", history.getOrgId());
				map.put("item1", history.getPersonName());
				map.put("item2", history.getHeadPic());
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
	 * 判断默认的系统模板和excel中的title是否一致
	 * @param title excel中的title
	 * @param resultvo
	 * @param optionVOs 已启用的自定义字段列表
     * @return
	 * @author liyixin
	 * @2016-11-7
	 * @version 1.0
     */
	private boolean judgeTitleIsTrue(String[] title, ResultVO resultvo, List<TbQyUserCustomOptionVO> optionVOs) throws Exception, BaseException {
		String[] titleTemplate = ContactImportUtil.setHead(orgId, optionVOs, false);
		boolean boo = true;
		if(AssertUtil.isEmpty(title) || (title.length != titleTemplate.length && !(title.length == titleTemplate.length+1 && (AssertUtil.isEmpty(title[title.length-1]) || "状态".equals(title[title.length-1]) || "错误提示".equals(title[title.length-1]))))){//状态，错误提示可有可无
			resultvo.setTips("你的通讯录模板出现了问题，请使用最新的模板导入数据。");
			resultvo.setTotalNum(1);
			resultvo.setProcessNum(0);
			logger.info("你的通讯录模板出现了问题，请使用最新的模板导入数据。orgId："+orgId);
			boo = false;
			return boo;
		}
		for (int i = 0; i < titleTemplate.length; i++) {
			if(!title[i].equals(titleTemplate[i])){
				resultvo.setTips("你的通讯录模板出现了问题，请使用最新的模板导入数据。");
				resultvo.setTotalNum(1);
				resultvo.setProcessNum(0);
				logger.info("你的通讯录模板出现了问题，请使用最新的模板导入数据。orgId："+orgId);
				boo = false;
				return boo;
			}
		}
		return boo;
	}
}