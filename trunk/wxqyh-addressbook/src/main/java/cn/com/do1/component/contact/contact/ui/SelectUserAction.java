package cn.com.do1.component.contact.contact.ui;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import cn.com.do1.component.wxcgiutil.login.UserInfo;
import org.apache.kahadb.page.Page;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class SelectUserAction extends BaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(SelectUserAction.class);
	private ISelectUserMgrService selectUserService;
	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IExperienceapplicationService experienceapplicationService;
    private IDefatgroupMgrService defatgroupMgrService;
    private IDepartmentMgrService departmentMgrService;
	private String keyWord; // 搜索关键字：支持姓名&手机号
	private String agentCode;

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
    @Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(
			IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
	}

	/**
	 * @param selectUserService 要设置的 selectUserService
	 */
    @Resource(name = "selectUserService")
	public void setSelectUserService(ISelectUserMgrService selectUserService) {
		this.selectUserService = selectUserService;
	}
    @Resource(name = "defatgroupService")
    public void setDefatgroupMgrService(IDefatgroupMgrService defatgroupMgrService) {
        this.defatgroupMgrService = defatgroupMgrService;
    }
    @Resource(name = "departmentService")
    public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
        this.departmentMgrService = departmentMgrService;
    }
    
	/**
	 * 联系人搜索--分页
	 * 
	 * @throws BaseException
	 * @throws Exception
	 *  sunqinghai
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "根据机构ID获取该机构下的所有用户失败"))
	public void searchByNameOrPhone(@InterfaceParam(name="typeId")@Validation(must=false,name="查询条件的类型ID")String typeId) throws Exception, BaseException {
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		// 是否可所有人可见
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(userInfo.getCorpId(),agentCode);
		Pager pager = new Pager(ServletActionContext.getRequest(), 10);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", userInfo.getOrgId());
		if (!StringUtil.isNullEmpty(agentCode) && !(aci!=null && aci.isAllUserVisible())) {
			//如果用户不在可见范围内，直接提示
			if(aci==null || !VisibleRangeUtil.isUserVisibleAgentByParts(userInfo.getUserId(),userInfo.getDeptFullNames(),agentCode,userInfo.getOrgId(),aci.getPartys())){
				throw new NonePrintException("105", "尊敬的用户，你不在应用的可见范围，请联系管理员！");
			}
			if(!aci.isAllUserUsable()){
				map.put("agentCode", agentCode);
			}
		}
		//根据搜索条件进行控制
		map = judgeSearchCondition(map,typeId);
		if (map.get("agentCode") != null){
			pager = selectUserService.searchByNameOrPhone(map, pager,userInfo);
		}else {
			pager = contactMgrService.searchByNameOrPhone(map, pager,userInfo);
		}
		//用户保密
		pager = SecrecyUserUtil.secrecyPage(userInfo.getOrgId(),pager);
		if(Configuration.IS_QIWEIYUN) {
			//判断是不是超过1W人的超大型企业
			if (Configuration.BIG_ORG_NUMBER < OrgUtil.getUserTotal(userInfo.getOrgId(), OrgUtil.USER_MEMBER)) {
				addJsonObj("superBigOrg", true);
			} else {
				addJsonObj("superBigOrg", false);
			}
		}else {
			addJsonObj("superBigOrg", false);
		}
		addJsonPager("pageData", pager);
		// maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
		addJsonObj("isDisplayMobilel", this.selectUserService.getSetFiled(userInfo.getOrgId()));
		//手机端默认搜索条件
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(userInfo.getOrgId()));
	}

	/**
	 * 根据机构ID获取该机构下的所有用户
	 * @throws Exception
	 * @throws BaseException
	 * sunqinghai
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "根据机构ID获取该机构下的所有用户失败"))
	public void ajaxGetUserListByOrgID() throws Exception, BaseException {
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext
				.getRequest());
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(user.getCorpId(),agentCode);
		Pager pager = new Pager(ServletActionContext.getRequest(), 10);
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> params = new HashMap<String, Object>();
		String sortTop = request.getParameter("sortTop");
		if (!AssertUtil.isEmpty(sortTop)) {
			params.put("sortTop", sortTop);
		}
		if (StringUtil.isNullEmpty(agentCode) || (aci!=null && aci.isAllUserVisible())) {
			addJsonObj("isRangeAll", true);
			pager = contactMgrService.findAlluserByUser(pager, user, params);
		} else {
			//如果用户不在可见范围内，直接提示
			if(aci==null || !VisibleRangeUtil.isUserVisibleAgentByParts(user.getUserId(),user.getDeptFullNames(),agentCode,user.getOrgId(),aci.getPartys())){
				throw new NonePrintException("105", "尊敬的用户，你不在应用的可见范围，请联系管理员！");
			}
			if(aci.isAllUserUsable()){
				addJsonObj("isRangeAll", true);
				pager = contactMgrService.findAlluserByUser(pager, user, params);
			}
			else{
				addJsonObj("isRangeAll", false);
				params.put("agentCode", agentCode);
				pager = selectUserService.findAlluserByUser(pager, user, params);
			}
		}
		//用户保密
		pager =SecrecyUserUtil.secrecyPage(user.getOrgId(),pager);
		if(Configuration.IS_QIWEIYUN) {
			//判断是不是超过1W人的超大型企业
			if (Configuration.BIG_ORG_NUMBER < OrgUtil.getUserTotal(user.getOrgId(), OrgUtil.USER_MEMBER)) {
				addJsonObj("superBigOrg", true);
			} else {
				addJsonObj("superBigOrg", false);
			}
		}else {
			addJsonObj("superBigOrg", false);
		}
		addJsonObj("showMsgBtn", true);
		addJsonPager("pageData", pager);
		// maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
		addJsonObj("isDisplayMobilel", this.selectUserService.getSetFiled(user.getOrgId()));
		//手机端默认搜索条件
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(user.getOrgId()));
	}

	/**
	 * 搜索拼音首字母（输出pager）
	 * @throws BaseException
	 * @throws Exception
	 * sunqinghai
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "根据机构ID获取该机构下的所有用户失败"))
	public void searchFirstLetter() throws Exception, BaseException {
		UserInfoVO userInfo = WxqyhAppContext
				.getCurrentUser(ServletActionContext.getRequest());
		// 是否可所有人可见
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(userInfo.getCorpId(),agentCode);
		Pager pager = new Pager(ServletActionContext.getRequest(), 10);
		if (StringUtil.isNullEmpty(agentCode) || (aci!=null && aci.isAllUserVisible())) {
			pager = contactMgrService.searchFirstLetter(keyWord, pager,userInfo);
		} else {
			//如果用户不在可见范围内，直接提示
			if(aci==null || !VisibleRangeUtil.isUserVisibleAgentByParts(userInfo.getUserId(),userInfo.getDeptFullNames(),agentCode,userInfo.getOrgId(),aci.getPartys())){
				throw new NonePrintException("105", "尊敬的用户，你不在应用的可见范围，请联系管理员！");
			}
			if(aci.isAllUserUsable()){//如果设置可操作所有人
				pager = contactMgrService.searchFirstLetter(keyWord, pager,userInfo);
			}
			else{
				pager = selectUserService.searchFirstLetter(keyWord, pager,userInfo, agentCode);
			}
		}
		//用户保密
		pager = SecrecyUserUtil.secrecyPage(userInfo.getOrgId(),pager);
		if(Configuration.IS_QIWEIYUN) {
			//判断是不是超过1W人的超大型企业
			if (Configuration.BIG_ORG_NUMBER < OrgUtil.getUserTotal(userInfo.getOrgId(), OrgUtil.USER_MEMBER)) {
				addJsonObj("superBigOrg", true);
			} else {
				addJsonObj("superBigOrg", false);
			}
		}else {
			addJsonObj("superBigOrg", false);
		}
		addJsonPager("pageData", pager);
		// maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
		addJsonObj("isDisplayMobilel", this.selectUserService.getSetFiled(userInfo.getOrgId()));
		//手机端默认搜索条件
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(userInfo.getOrgId()));
	}



	/**
	 * 获得常用联系人数据
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "2001", successMsg = "更新成功", faileMsg = "更新失败"))
	public void getCommonList() throws Exception, BaseException {
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext
				.getRequest());
		String userId = user.getUserId();
		// 是否可所有人可见
		if (StringUtil.isNullEmpty(agentCode) || WxAgentUtil.isAllUserVisible(user.getCorpId(), agentCode)) {
			List<TbQyUserInfoVO> list = contactMgrService.getCommonUserList(
					userId, 20);
			addJsonArray("commonList", list);
			// maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(user
					.getOrgId());
			addJsonObj("isDisplayMobilel", isDisplayMobilel);
		} else {
			List<TbQyUserInfoVO> list = selectUserService.getCommonUserList(
					userId, 20, agentCode, user.getCorpId());
			addJsonArray("commonList", list);
			// maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.selectUserService.getSetFiled(user
					.getOrgId());
			addJsonObj("isDisplayMobilel", isDisplayMobilel);
		}
	}

	/**
	 * 获取常用联系人群组
	 * 
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserGroup() throws Exception, BaseException {
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext
				.getRequest());
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = user.getUserId();
		// 是否可所有人可见
		if (StringUtil.isNullEmpty(agentCode) || WxAgentUtil.isAllUserVisible(user.getCorpId(), agentCode)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			Pager pager = new Pager(request, 100);
			/**
			 * 2015-2-12 李盛滔 修改doPager
			 */
			pager = defatgroupMgrService.getUserGroup(pager, map);
			addJsonPager("pageData", pager);
			// 添加公共群组 chenfeixiong 2014/11/25
			String isPerson = request.getParameter("isPerson"); // 个人群组列表不显示默认群组
			if (!"1".equals(isPerson)) {
				TbQyUserInfoVO vo = contactService.findUserInfoByUserId(userId);
				List<TbQyUserGroupPO> list2 = defatgroupMgrService.getUserGroup(vo
						.getOrgId());
				addJsonObj("publicList", list2);
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);

			Pager pager = new Pager(request, 100);
			/**
			 * 2015-2-12 李盛滔 修改doPager
			 */
			pager = selectUserService.getUserGroup(pager, map);

			addJsonPager("pageData", pager);

			// 添加公共群组 chenfeixiong 2014/11/25
			String isPerson = request.getParameter("isPerson"); // 个人群组列表不显示默认群组
			if (!"1".equals(isPerson)) {
				TbQyUserInfoVO vo = selectUserService
						.findUserInfoByUserId(userId);
				List<TbQyUserGroupPO> list2 = selectUserService.getUserGroup(vo
						.getOrgId());
				addJsonObj("publicList", list2);
			}
		}
	}

	/**
	 * 获取常用联系人群组人员
	 * 
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserGroupPerson() throws Exception, BaseException {
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext
				.getRequest());
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = user.getUserId();
		// 是否可所有人可见
		if (StringUtil.isNullEmpty(agentCode) || WxAgentUtil.isAllUserVisible(user.getCorpId(), agentCode)) {
			String groupId = request.getParameter("groupId");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			map.put("groupId", groupId);
			Pager pager = new Pager(request, 100);
			/**
			 * 20150212 李盛滔 修改doPager
			 */
			pager = defatgroupMgrService.getUserGroupPerson(pager, map);

			addJsonPager("pageData", pager);
			TbQyUserGroupPO po = contactService.searchByPk(
					TbQyUserGroupPO.class, groupId);
			addJsonFormateObj("TbQyUserGroupPO", po);
		} else {
			String groupId = request.getParameter("groupId");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			map.put("corpId", user.getCorpId());
			map.put("groupId", groupId);
			map.put("agentCode", agentCode);
			Pager pager = new Pager(request, 100);
			/**
			 * 20150212 李盛滔 修改doPager
			 */
			pager = selectUserService.getUserGroupPerson(pager, map);
			addJsonPager("pageData", pager);
			TbQyUserGroupPO po = contactService.searchByPk(
					TbQyUserGroupPO.class, groupId);
			addJsonFormateObj("TbQyUserGroupPO", po);
		}

	}
	
	/**
	 * @人员列表搜索
	 * 
	 * @throws Exception
	 */
	public void ajaxGetUserListByUserNameOrPhone() throws Exception {
		Pager pager = new Pager(ServletActionContext.getRequest(),10);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			String orgId = user.getOrgId();
			String keyWord = this.keyWord;
			map.put("orgId", orgId);
			map.put("userId", user.getUserId());
			// 是否可所有人可见
			if (StringUtil.isNullEmpty(agentCode) || WxAgentUtil.isAllUserVisible(user.getCorpId(), agentCode)) {
				if(!AssertUtil.isEmpty(keyWord)){
					map.put("keyWord", keyWord);
					map.put("hasDepart", departmentMgrService.hasDepart(orgId,keyWord));
				}
				pager=contactMgrService.findUsersByOrgId(map,pager);
				addJsonPager("pageData", pager);
				String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
				addJsonObj("isDisplayMobilel",isDisplayMobilel);
				setActionResult("0", "查询成功");
			}else{
				map.put("agentCode", agentCode);
				if(!AssertUtil.isEmpty(keyWord)){
					map.put("keyWord", keyWord);
					map.put("hasDepart", departmentMgrService.hasDepart(orgId,keyWord));
				}
				pager=selectUserService.findUsersByOrgId(map,pager);
				addJsonPager("pageData", pager);
				String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
				addJsonObj("isDisplayMobilel",isDisplayMobilel);
				setActionResult("0", "查询成功");
			}
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	
	/**
	 * 搜索拼音首字母（输出Page）
	 * 
	 * @author Sun Qinghai
	 * @2015-3-16
	 * @version 1.0
	 */
	public void searchFirstLetterPage(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			// 是否可所有人可见
			if (StringUtil.isNullEmpty(agentCode) || WxAgentUtil.isAllUserVisible(userInfo.getCorpId(), agentCode)) {
				pager = contactMgrService.searchFirstLetter(keyWord , pager, userInfo);
			}else{
				pager = selectUserService.searchFirstLetter(keyWord , pager, userInfo, agentCode);
			}
			addJsonPager("pageData", pager);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}

	/**
	 *判断微信端左上角搜索条件
	 * @param map
	 * @2016-9-5
	 * @return
     */
	private Map<String, Object> judgeSearchCondition(Map<String, Object> map, String typeId) throws Exception, BaseException{
		//搜索所有
		if(StringUtil.strCstr("1",typeId)){
			map.put("keyWord", keyWord);
		}
		//姓名精确搜索
		else if(StringUtil.strCstr("2",typeId)){
			map.put("exactName", keyWord+"%");
		}
		//姓名模糊搜索
		else if(StringUtil.strCstr("3",typeId)){
			map.put("keyWord", "%"+keyWord+"%");
		}
		//拼音搜索
		else if(StringUtil.strCstr("4",typeId)){
			map.put("pinyin", "%"+keyWord+"%");
		}
		//手机搜索
		else if(StringUtil.strCstr("5",typeId)){
			map.put("mobile", "%"+keyWord+"%");
		}
		//昵称搜索
		else if(StringUtil.strCstr("6",typeId)){
			map.put("nickName", "%"+keyWord+"%");
		}
		//职位搜索
		else  if(StringUtil.strCstr("7",typeId)){
			map.put("position", "%"+keyWord+"%");
		}
		//按账号搜索
		else if(StringUtil.strCstr("8",typeId)){
			map.put("wuid", "%"+keyWord+"%");
		}
		else{
			map.put("keyWord", keyWord);
		}
		return map;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
