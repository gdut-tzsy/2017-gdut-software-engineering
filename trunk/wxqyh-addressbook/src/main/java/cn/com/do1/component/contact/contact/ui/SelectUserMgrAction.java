package cn.com.do1.component.contact.contact.ui;

import java.util.*;

import javax.annotation.Resource;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.contact.contact.util.UserSeniorSearchUtil;
import cn.com.do1.component.contact.department.util.CheckDeptVisible;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * The type Select user mgr action.
 */
public class SelectUserMgrAction extends BaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(SelectUserMgrAction.class);
	@Resource(name = "selectUserService")
	private ISelectUserMgrService selectUserService;
	@Resource
	private IContactService contactService;
	@Resource
	private IContactMgrService contactMgrService;
	@Resource
	private IDepartmentService departmentService;
	private String agentCode;
    private String isQuit;//是否要显示离职人员
	/**
	 * 是否是设置(0:不是)
	 */
	private final static String IS_SETTING_ZERO = "0";
	/**
	 * 是否是设置(2:是)
	 */
	private final static String IS_SETTING_ONE = "1";
	/**
	 * 登陆对象(0:超级管理员)
	 */
	public final static String IS_ORDINARY_MANAGER_ZERO = "0";
	/**
	 * 登陆对象(1:普通管理员)
	 */
	public final static String IS_ORDINARY_MANAGER_ONE = "1";
	private Integer pageSize;
	private IContactCustomMgrService contactCustomMgrService;

	/**
	 * 列表查询时，页面要传递的参数
	 * 
	 * @SearchValueType(name = "personName", type = "string", format = "%s"),
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "position", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "startEntryTime", type = "date", format = "yyyy-MM-dd :start"),
			@SearchValueType(name = "endEntryTime", type = "date", format = "yyyy-MM-dd :end"),
			@SearchValueType(name = "startTimes", type = "date", format = "yyyy-MM-dd :start"),
			@SearchValueType(name = "endTime", type = "date", format = "yyyy-MM-dd :end"),
			@SearchValueType(name = "reStartFollowTimes", type = "date", format = "yyyy-MM-dd :start"),
			@SearchValueType(name = "reEndFollowTimes", type = "date", format = "yyyy-MM-dd :end")})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxSearch() throws Exception, BaseException {
		if (pageSize == null || pageSize < 1) {
			pageSize = getPageSize();
		}
		if (pageSize > 1000) {
			setActionResult("1100", "每页个数不能超过1000条");
			return;
		}

		Pager pager = new Pager(ServletActionContext.getRequest(), pageSize);
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org = contactService.getOrgByUserId(user);
		Map<String, Object> searchMap = getSearchValue();
		if (searchMap == null) {
			searchMap = new HashMap<String, Object>();
		}
		if (org != null) {
			searchMap.put("orgId", org != null ? org.getOrgId() : "");
		} else {
			// 此用户没有机构信息，不查询数据
			addJsonPager("pageData", pager);
			return;
		}
		//后台高级搜索条件优化
		searchMap = UserSeniorSearchUtil.manageSearchCondition(searchMap);
		String isSettingUser;
		AgentCacheInfo aci = StringUtil.isNullEmpty(agentCode) ? null : WxAgentUtil.getAgentCache(org.getCorpId(), agentCode);
		// 所有人可见
		//如果未托管通讯录应用,需要显示全部部门人员
		if (StringUtil.isNullEmpty(agentCode) || (aci != null && aci.isAllUserUsable())
				|| (aci == null && WxAgentUtil.getAddressBookCode().equals(agentCode))) {
			addJsonObj("isRangeAll", true);
			if(AssertUtil.isEmpty(isQuit))
			   searchMap.put("aliveStatus", "-1");// 非-1表示非离职用户列表
			// 是设置的话，普通管理员也可以看到组织机构下所有人员
			isSettingUser = (String) searchMap.get("isSettingUser");
			if (!AssertUtil.isEmpty(isSettingUser)) {// 去掉条件，因为不需要查询
				searchMap.remove("isSettingUser");
			} else {// 为空则默认不是设置
				isSettingUser = IS_SETTING_ZERO;
			}

		}else{
			addJsonObj("isRangeAll", false);
			searchMap.put("agentCode", agentCode);
			if(AssertUtil.isEmpty(isQuit))
			     searchMap.put("aliveStatus", "-1");// 非-1表示非离职用户列表
			// 是设置的话，普通管理员也可以看到组织机构下所有人员
			isSettingUser = (String) searchMap.get("isSettingUser");
			if (!AssertUtil.isEmpty(isSettingUser)) {// 去掉条件，因为不需要查询
				searchMap.remove("isSettingUser");
			} else {// 为空则默认不是设置
				isSettingUser = IS_SETTING_ZERO;
			}
		}
		if (AssertUtil.isEmpty(searchMap.get("deptId"))) {
			// 普通 管理员
			String isOrdinaryManager = IS_ORDINARY_MANAGER_ZERO;// 超级管理员
			if (!AssertUtil.isEmpty(org) && ManageUtil.superAdmin != org.getAge()
					&& IS_SETTING_ZERO.equals(isSettingUser)) {// 普通管理员并且不是设置
				String[] departIds = null;
				TbManagerPersonVO vo = this.contactService.getManagerPersonByPersonIdAndOrgId(org.getPersonId(), org.getOrgId());
				if(vo == null){
					pager = new Pager(10);
					pager.setTotalRows(0);
					pager.setCurrentPage(1);
					pager.setTotalPages(0);
				}
				else if (!StringUtil.isNullEmpty(vo.getMsgCodeIds()) && ("|" + vo.getMsgCodeIds() + "|").contains("|" + agentCode + "|")){
					if(!ManageUtil.RANGE_ONE_INT.equals(vo.getMsgRange())){//如果消息是全公司可见
						if (StringUtil.isNullEmpty(vo.getMsgDeptIds()) && StringUtil.isNullEmpty(vo.getMsgUserIds())) {
							pager = new Pager(10);
							pager.setTotalRows(0);
							pager.setCurrentPage(1);
							pager.setTotalPages(0);
						}
						else {
							String[] userIds = ContactUtil.getIntersectionByTwo(vo.getMsgUserIds(), searchMap.containsKey("userIds") ? searchMap.get("userIds").toString() : null);
							if (!AssertUtil.isEmpty(userIds)) {
								searchMap.put("userIds", userIds);
							}
							if (!StringUtil.isNullEmpty(vo.getMsgDeptIds())) {
								if (!AssertUtil.isEmpty(searchMap.get("deptIds"))) {
									List<TbDepartmentInfoVO> list = DepartmentUtil.getIntersectionDeptVOByTwo(vo.getMsgDeptIds(), searchMap.get("deptIds").toString());
									searchMap.remove("deptIds");
									pager = selectUserService.searchContactByDepartList(searchMap, pager, list, userIds);
								}
								else {
									departIds = vo.getMsgDeptIds().split("\\|");
									pager = selectUserService.searchContact(searchMap, pager, departIds, userIds);
								}
							}
							else {
								pager = selectUserService.searchContact(searchMap, pager, null, userIds);
							}
						}
						isOrdinaryManager = IS_ORDINARY_MANAGER_ONE;// 普通管理员
					}
					else {
						pager = getAllUserPage(searchMap, pager);
					}
				}
				else if(!ManageUtil.RANGE_ONE.equals(vo.getRanges())) {//如果不是全公司可见
					isOrdinaryManager = IS_ORDINARY_MANAGER_ONE;// 普通管理员
					if (StringUtil.isNullEmpty(vo.getDepartids())) {
						pager = new Pager(10);
						pager.setTotalRows(0);
						pager.setCurrentPage(1);
						pager.setTotalPages(0);
					}
					else {
						if (!AssertUtil.isEmpty(searchMap.get("deptIds"))) {
							List<TbDepartmentInfoVO> list = DepartmentUtil.getIntersectionDeptVOByTwo(vo.getDepartids(), searchMap.get("deptIds").toString());
							searchMap.remove("deptIds");
							pager = selectUserService.searchContactByDepartList(searchMap, pager, list, null);
						}
						else {
							departIds = vo.getDepartids().split("\\|");
							pager = selectUserService.searchContact(searchMap, pager, departIds, null);
						}
					}
				}
				else {
					pager = getAllUserPage(searchMap, pager);
				}
			}
			else {
				pager = getAllUserPage(searchMap, pager);
			}
			addJsonObj("isOrdinaryManager", isOrdinaryManager);
		}
		else {
			searchMap.remove("userIds");
			searchMap.remove("deptIds");
			pager = selectUserService.searchContact(searchMap, pager, null, null);
		}

		addJsonPager("pageData", pager);
		//通讯录应用增加高级搜索数据
		if (WxAgentUtil.getAddressBookCode().equals(agentCode)) {
			List<TbQyUserSearchSeniorVO> seniorVOList = contactMgrService.getSeniorByOrgId(org.getOrgId(), org.getUserId());
			List<TbQyUserCustomOptionVO> optionVOList = contactCustomMgrService.getUseingOptionByorgId(org.getOrgId());
			addJsonObj("seniorVOList", seniorVOList);
			addJsonObj("optionVOList", optionVOList);
		}
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
	}

	/**
	 * 获取全公司管理员人员显示
	 * @param searchMap
	 * @param pager
	 * @return 返回数据
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -2-21
	 */
	private Pager getAllUserPage (Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
		String[] userIds = null;
		if (searchMap.containsKey("userIds")) {
			userIds = searchMap.get("userIds").toString().split("\\|");
			searchMap.put("userIds", userIds);
		}
		String[] departIds = null;
		if (!AssertUtil.isEmpty(searchMap.get("deptIds"))) {
			departIds = searchMap.get("deptIds").toString().split("\\|");
			searchMap.remove("deptIds");
		}
		return selectUserService.searchContact(searchMap, pager, departIds, userIds);
	}


	/**
	 * @param params the params
	 * @return the void
	 * @author 陈春武
	 * @time 2016年06月24日  Get effective users from data.
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "获取成功", faileMsg = "获取失败"))
	public void getEffectiveUsersFromData(@InterfaceParam(name="param")@Validation(must=true,name="param")String params,
										  @InterfaceParam(name="batchNum")@Validation(must=false, name="batchNum")String batchNum) throws Exception, BaseException {
		boolean isMaxAuth = false;
		String[] personNames = params.split("##");
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO orgVo = contactService.getOrgByUserId(user);
		List<UserAccountInfoVO> dataList = new ArrayList<UserAccountInfoVO>(personNames.length);
		List<UserAccountInfoVO> unEffDataList = new ArrayList<UserAccountInfoVO>();
		List<UserAccountInfoVO> wrongDataList = new ArrayList<UserAccountInfoVO>();
		List<TbDepartmentInfoPO> effDepartmentList = new ArrayList<TbDepartmentInfoPO>();
		List<TbDepartmentInfoPO> effRangeDepartmentList = new ArrayList<TbDepartmentInfoPO>();
		List<String> effPersonNameList = Arrays.asList(personNames);
		HashSet<String> hs = new HashSet<String>(effPersonNameList);
		effPersonNameList = new ArrayList<String>(hs);
		Map<String, Object> effDataMap = new HashMap<String, Object>();
		//判断管理员的身份，如果非超级管理员则需要比对所在部门
		if (ManageUtil.superAdmin != orgVo.getAge()) {
			TbManagerPersonVO tbManagerPersonVO = contactService.getManagerPersonByPersonIdAndOrgId(orgVo.getPersonId(), orgVo.getOrgId());
			if (!AssertUtil.isEmpty(tbManagerPersonVO)) {
				//普通管理员目标对象不是所有人
				if (ManageUtil.RANGE_THREE.equals(tbManagerPersonVO.getRanges())) {
					if (!AssertUtil.isEmpty(tbManagerPersonVO.getDepartids()) && (tbManagerPersonVO.getDepartids().length() > 0)) {
						String[] departIds = tbManagerPersonVO.getDepartids().split("\\|");
						effDepartmentList = departmentService.getDeptInfo(departIds);
					}
				} else {
					isMaxAuth = true;
				}
			}
		} else {
			isMaxAuth = true;
		}
		//可见范围的部门
		effRangeDepartmentList = CheckDeptVisible.getDeptVisibleList(orgVo.getCorpId(),orgVo.getOrgId(),agentCode);
		if(VipUtil.isQwVip(orgVo.getOrgId())){//如果是vip
			if(!AssertUtil.isEmpty(batchNum) && "3000".equals(batchNum)){//如果是红包,限制人数为3000
				//分批处理
				selectUserService.doSortAccountListInBatch(1000,3000,effPersonNameList,orgVo, effDataMap, isMaxAuth, effDepartmentList, effRangeDepartmentList,dataList, unEffDataList, wrongDataList);
			}else{//如果不是红包
				//分批处理
				selectUserService.doSortAccountListInBatch(100,100,effPersonNameList,orgVo, effDataMap, isMaxAuth, effDepartmentList, effRangeDepartmentList,dataList, unEffDataList, wrongDataList);
			}
		}else{
			//分批处理
			selectUserService.doSortAccountListInBatch(100,100,effPersonNameList,orgVo, effDataMap, isMaxAuth, effDepartmentList, effRangeDepartmentList,dataList, unEffDataList, wrongDataList);
		}
		addJsonObj("legalList", dataList);
		addJsonObj("wrongList", wrongDataList);
		addJsonObj("illegalList", unEffDataList);

	}

	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getIsQuit() {
		return isQuit;
	}

	public void setIsQuit(String isQuit) {
		this.isQuit = isQuit;
	}

}
