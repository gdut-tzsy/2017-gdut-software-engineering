package cn.com.do1.component.contact.department.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.contact.contact.util.UserEduUtil;
import cn.com.do1.component.contact.department.util.DepartmentDictUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import cn.com.do1.component.util.org.OrgUtil;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;

public class PortalDepartmentAction extends WxqyhPortalBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(DepartmentAction.class);
    private IDepartmentService departmentService;
    private IDepartmentMgrService departmentMgrService;
    private TbDepartmentInfoPO tbDepartmentInfoPO;
	private IContactService contactService;
	private IContactMgrService contactMgrService;
    private String ids[];
    private String id;
    private String deptId;
    private String pId;
    private String keyWord;
    private String userId;
    private String deptIds;
    private String userIds;
    private String searchDept;
    private String range;

    public IDepartmentService getDepartmentService() {
        return departmentService;
    }

    @Resource(name = "departmentService")
    public void setDepartmentService(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    /**
	 * @param departmentMgrService 要设置的 departmentMgrService
	 */
    @Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	public String[] getIds() {
        return ids;
    }

	public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getSearchDept() {
		return searchDept;
	}

	public void setSearchDept(String searchDept) {
		this.searchDept = searchDept;
	}

    public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获得所有部门数据
     * @throws BaseException 
     * @throws Exception 
     */
    @JSONOut(catchException = @CatchException(errCode = "1006", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getAllDepart() throws BaseException, Exception{
    	UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	String orgId = user.getOrgId();
    	List<TbDepartmentInfoPO> list = departmentService.getAllDepart(orgId);
    	addJsonArray("list", list);
    }
    
    /**
     * 查询所有一级部门
     * @throws BaseException 
     * @throws Exception 
     */
    @JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getFirstDepart() throws Exception, BaseException{
    	UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	String orgId = user.getOrgId();
    	List<TbDepartmentInfoVO> list = departmentService.getFirstDepart(orgId);
    	addJsonArray("list", list);
    }
    
    /**
     * 获取子部门及部门人员
     * @throws BaseException 
     * @throws Exception 
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getChildDeptAndPerson() throws Exception, BaseException{
    	HttpServletRequest request = ServletActionContext.getRequest();
    	String deptId = request.getParameter("deptId");
    	String pId = request.getParameter("pId");
		String agentCode = request.getParameter("agentCode");

    	UserInfoVO user = getUser();
    	addJsonObj("deptId", deptId);
    	addJsonObj("pId", pId);
		Pager pager = new Pager(ServletActionContext.getRequest(), 100);
		//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
    	String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
    	addJsonObj("isDisplayMobilel",isDisplayMobilel);
    	
    	Map<String,Object> params=new HashMap<String,Object>();
    	String sortTop=request.getParameter("sortTop");
    	if(!AssertUtil.isEmpty(sortTop)){
    		params.put("sortTop", sortTop);
    	}
    	if(WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
			addJsonObj("showMsgBtn", true);
		}
    	//获取部门列表
    	List<TbDepartmentInfoVO> deptlist = new ArrayList<TbDepartmentInfoVO>();
		departmentMgrService.getFirstDepartAndUser(deptId,user,agentCode,params,deptlist,pager);
		DepartmentUtil.sortDepart(deptlist);
		pager.setPageData( SecrecyUserUtil.secrecylist(user, (List<TbQyUserInfoVO>) pager.getPageData()) );
		UserEduUtil.addChildrenToVO((List<TbQyUserInfoVO>)pager.getPageData(), null, user);
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
		if(AssertUtil.isEmpty(pager.getPageData())){
			pager.setPageData(new ArrayList<TbQyUserInfoVO>());
		}
    	addJsonArray("deptlist", deptlist);
		//为了解决组织选人出错的问题临时解决方案，请及时优化
		addJsonArray("userList", (List<TbQyUserInfoVO>)pager.getPageData());
		addJsonPager("pageData", pager);
		//手机端默认搜索条件
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(user.getOrgId()));
    }
    
    /**
     * 获取子部门
     * @throws BaseException 
     * @throws Exception 
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getChildDept() throws Exception, BaseException{
    	HttpServletRequest request = ServletActionContext.getRequest();
    	String deptId = request.getParameter("deptId");
    	String pId = request.getParameter("pId");

    	UserInfoVO user = WxqyhAppContext.getCurrentUser(request);
    	addJsonObj("deptId", deptId);
    	addJsonObj("pId", pId);
		String agentCode = request.getParameter("agentCode");
		List<TbDepartmentInfoVO> deptlist = departmentMgrService.getChildDepart(deptId,user,agentCode);
		addJsonArray("deptlist", deptlist);
    }
    

    /**
     * 获取用户是否拥有查看全公司的权限
     * @throws Exception
     * @throws BaseException
     * @author Sun Qinghai
     * @2015-7-1
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getVisitAllUserPermission() throws Exception, BaseException{
    	UserInfoVO user = getUser();
    	
    	//人员通讯录添加限制	1为全公司 2为一级部门 3为本部门
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(user.getOrgId(),user.getUserId());
		//如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if(depts==null || depts.size()==0){
	    	addJsonObj("isVisitAll", false);
			return ;
		}
		else {
			for (TbDepartmentInfoVO tbDepartmentInfoVO : depts) {
				if(StringUtil.isNullEmpty(tbDepartmentInfoVO.getPermission()) || "1".equals(tbDepartmentInfoVO.getPermission())){
			    	addJsonObj("isVisitAll", true);
					return ;
				}
			}
		}
		addJsonObj("isVisitAll", false);
    }

    
    /**
     * 获取子部门及部门人员(允许搜索部门或者人员)
     * @throws Exception
     * @throws BaseException
     * @author Sun Qinghai
     * @2014-6-24
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void searchDeptAndPerson() throws Exception, BaseException{
    	UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	String orgId = user.getOrgId();
    	addJsonObj("deptId", deptId);
    	addJsonObj("pId", pId);
    	//获取部门列表
    	List<TbDepartmentInfoVO> deptlist = null;
    	List<TbQyUserInfoVO> personlist = null;
    	if(AssertUtil.isEmpty(keyWord)){
        	if(AssertUtil.isEmpty(deptId)){
        		deptlist = departmentService.getFirstDepart(orgId);
        	}else{
				TbDepartmentInfoPO deptPO = departmentService.searchByPk(TbDepartmentInfoPO.class, deptId);
				//如果不是是首页
				if(deptPO != null && user.getOrgId().equals(deptPO.getOrgId())){
					deptlist = departmentMgrService.getChildDepart(orgId,deptId);
					//获取本部门的人员数据
					Map<String,Object> params=new HashMap<String,Object>();
					personlist = contactMgrService.getUsersByDepartId(deptId,params);
				}
        	}
    	}
    	else{
    		deptlist = departmentMgrService.getSearchDepart(orgId,keyWord);
        	//获取本部门的人员数据
        	personlist = contactMgrService.findUsersByUserNameOrPhoneOrPinyin(orgId,keyWord);
    	}
    	addJsonArray("deptlist", deptlist);
    	addJsonArray("userList", personlist);
    }
    
    /**
	 * 人员搜索
	 * @throws Exception
	 */
	public void getUserByNameOrPhone() throws Exception {
		try {
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId", user.getOrgId());
			HttpServletRequest request = ServletActionContext.getRequest();
			String typeId = request.getParameter("typeId");
			if(!AssertUtil.isEmpty(keyWord)){
				if("1".equals(typeId)){
					map.put("nickName", "%"+keyWord+"%");
				}else if("2".equals(typeId)){
					map.put("position", "%"+keyWord+"%");
				}else{
					map.put("keyWord", keyWord.toLowerCase());
				}
			}
			List<TbQyUserInfoVO> userList = this.contactMgrService.findUsersByUserNameOrPhone(user, map);
			addJsonArray("userList", userList);
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
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

	public TbDepartmentInfoPO setTbDepartmentInfoPO() {
		return this.tbDepartmentInfoPO;
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxView() throws Exception, BaseException {
		TbDepartmentInfoPO xxPO = departmentService.searchByPk(
				TbDepartmentInfoPO.class, id);
		addJsonFormateObj("tbDepartmentInfoPO", xxPO);// 注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getDeptsAndUsers() throws Exception, BaseException {
		WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());

		List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
		List<TbQyUserInfoVO> userList = new ArrayList<TbQyUserInfoVO>();

		if(!StringUtil.isNullEmpty(deptIds)){
			String[] deptArray = deptIds.split("\\|");
			for (String dept : deptArray) {

				if (!"".equals(dept)) {

					TbDepartmentInfoPO deptPO = contactService.searchByPk(TbDepartmentInfoPO.class, dept);
					if (deptPO != null) {
						deptList.add(deptPO);
					}
				}
			}
		}
		if(!StringUtil.isNullEmpty(userIds)){
			String[] userArray = userIds.split("\\|");
			for (String id : userArray) {
				TbQyUserInfoVO userVO = contactService.findUserInfoByUserId(id);
				if (userVO != null) {
					userList.add(userVO);
				}
			}
		}
		addJsonArray("userList", userList);
		addJsonArray("deptList", deptList);

	}
	
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getPersonCount() throws Exception, BaseException {
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());

		int count = contactService.getPersonCount(deptIds, userIds, user.getOrgId(), range, user.getUserId());
		addJsonObj("personCount", String.valueOf(count));
	}
	
    /**
     * 获取部门
     * @throws Exception
     * @throws BaseException
     * @author liangjiecheng
     * @2014-7-14
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void searchDept() throws Exception, BaseException{
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	String orgId = user.getOrgId();
    	if ("undefined".equals(deptId)) {
    		deptId = "";
    	}
    	addJsonObj("deptId", deptId);
    	addJsonObj("pId", pId);
    	//获取部门列表
    	List<TbDepartmentInfoVO> deptlist = null;
    	List<TbQyUserInfoVO> personlist = new ArrayList<TbQyUserInfoVO>();
    	Map<String,Object> params=new HashMap<String,Object>();
    	if(AssertUtil.isEmpty(keyWord)){
        	if(AssertUtil.isEmpty(deptId)){
        		deptlist = departmentService.getFirstDepart(orgId);
        	}else{
				TbDepartmentInfoPO deptPO = departmentService.searchByPk(TbDepartmentInfoPO.class, deptId);
				//如果不是是首页
				if(deptPO != null && user.getOrgId().equals(deptPO.getOrgId())){
					deptlist = departmentMgrService.getChildDepart(orgId,deptId);
					personlist = contactMgrService.getUsersByDepartId(deptId,params);
				}
        	}
    	}
    	else{
    		deptlist = departmentMgrService.getSearchDepart(orgId,keyWord);

    	}
    	addJsonArray("deptlist", deptlist);
    	addJsonArray("userList", personlist);
    }
	
    /**
     * 根据Id获取部门人员
     * @throws Exception
     * @throws BaseException
     * @author liangjiecheng
     * @2014-7-14
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getDeptPersonsById() throws Exception, BaseException{
    	UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	//获取部门列表
    	List<TbQyUserInfoVO> personlist = new ArrayList<TbQyUserInfoVO>();

    	personlist = contactService.getUsersAllByDepartId(user.getOrgId(),deptId);
    	addJsonArray("userList", personlist);
    }
    
    /**
     * 根据Id获取部门人员
     * @throws Exception
     * @throws BaseException
     * @author liangjiecheng
     * @2014-7-14
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1008", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getDeptPersonsAllById() throws Exception, BaseException{
    	UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
    	//获取部门列表
    	List<TbQyUserInfoVO> personlist = new ArrayList<TbQyUserInfoVO>();

    	personlist = contactService.getUsersAllByDepartId(user.getOrgId(),deptId);
    	addJsonArray("userList", personlist);
    }

	/**
	 * 判断部门权限是否是仅特定对象
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-3-10
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void checkDeptIsSpecific() throws Exception, BaseException{
		UserInfoVO user = getUser();
		//是单部门的仅特定对象
		String isSpecific = "1";
		//多部门 或者是 单部门的非特定对象
		String notSpecific = "0";
		List<TbDepartmentInfoVO> deptList = departmentService.getDeptInfoByUserId(user.getOrgId(), user.getUserId());
		if(deptList.size() == 1){
			if(DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(deptList.get(0).getPermission())){
				addJsonObj("deptIsSpecific",isSpecific);
			}else{
				addJsonObj("deptIsSpecific",notSpecific);
			}
		}else {
			addJsonObj("deptIsSpecific", notSpecific);
		}
	}

	public TbDepartmentInfoPO getTbDepartmentInfoPO() {
		return this.tbDepartmentInfoPO;
	}

	public void setTbDepartmentInfoPO(TbDepartmentInfoPO tbDepartmentInfoPO) {
		this.tbDepartmentInfoPO = tbDepartmentInfoPO;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId 要设置的 deptId
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return keyWord
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * @param keyWord 要设置的 keyWord
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * @return pId
	 */
	public String getPId() {
		return pId;
	}

	/**
	 * @param pId 要设置的 pId
	 */
	public void setPId(String pId) {
		this.pId = pId;
	}
}