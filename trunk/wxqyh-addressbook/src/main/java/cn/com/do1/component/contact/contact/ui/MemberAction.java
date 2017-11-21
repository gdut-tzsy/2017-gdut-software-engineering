package cn.com.do1.component.contact.contact.ui;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.contact.util.MemberUtil;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.ActionRoles;
import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.IMemberService;
import cn.com.do1.component.contact.contact.util.MemberHandleUtil;
import cn.com.do1.component.errcodedictionary.ErrorTip;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhBaseAction;
import cn.com.do1.component.vip.vip.service.IVipService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class MemberAction extends WxqyhBaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(MemberAction.class);
	
	
	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IMemberService memberService;
	private TbQyMemberConfigPO tbQyMemberConfigPO;
	private IDepartmentService departmentService;
	private TbQyMemberInfoPO tbQyMemberInfoPO;
	private IVipService vipService;
	
	private String ids[];
	private Integer pageSize;
	/**
	 * The Student service.
	 */
	private IStudentService studentService;
	/**
	 * 自定义字段service
	 */
	private IContactCustomMgrService contactCustomMgrService;
	
	/**
	 * 邀请单的启用状态
	 */
	private static String open_status="1";
	
	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}
    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    @Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource(name = "memberService")
    public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}
	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
	}
	/**
	 * Sets student service.
	 *
	 * @param studentService the student service
	 */
	@Resource(name = "studentService")
	public void setStudentService(IStudentService studentService) {
		this.studentService = studentService;
	}
    
	public TbQyMemberConfigPO getTbQyMemberConfigPO() {
		return tbQyMemberConfigPO;
	}
	public void setTbQyMemberConfigPO(TbQyMemberConfigPO tbQyMemberConfigPO) {
		this.tbQyMemberConfigPO = tbQyMemberConfigPO;
	}
	@Resource(name = "vipService")
	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public TbQyMemberInfoPO getTbQyMemberInfoPO() {
		return tbQyMemberInfoPO;
	}
	public void setTbQyMemberInfoPO(TbQyMemberInfoPO tbQyMemberInfoPO) {
		this.tbQyMemberInfoPO = tbQyMemberInfoPO;
	}
	/**
	 * 查询成员邀请设置信息
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询邀请设置信息失败"))
	public void ajaxView(@InterfaceParam(name="id")String id) throws Exception, BaseException{
		UserOrgVO org = getUser();
		boolean isVip=isVip(org);
		if(!isVip){
			addJsonObj("isVip", false);
		}else{
			addJsonObj("isVip", true);
		}
		addJsonObj("optionVOs", contactCustomMgrService.getUseingOptionByorgId(org.getOrgId()));
		addJsonObj("memberCustomOptionVOs", memberService.getMemberCustomConfigByMeberId(id));
		addJsonObj("memberBaseOptionVOs", memberService.getMemberBaseConfigByMeberId(id));
		if(StringUtil.isNullEmpty(id)){
			addJsonObj("tbQyMemberConfigPO",new TbQyMemberConfigVO());
			addJsonObj("org", org);
			return;
		}else {
			TbQyMemberConfigVO config = memberService.getHistotryData(id, org.getOrgId());
			addJsonObj("tbQyMemberConfigPO", config);
			addJsonObj("org", org);
		}
	}
	
	
	/**
	 * 查询成员邀请设置信息
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "保存成功", faileMsg = "保存邀请设置信息失败"))
	public void ajaxUpdate(@InterfaceParam(name = "deptId")@Validation(must = true, name = "deptId")String deptId,
						   @InterfaceParam(name = "dept")@Validation(must = true, name = "dept")String dept,
						   @InterfaceParam(name = "memberCustomOptionVOs")@Validation(must = false, name = "memberCustomOptionVOs")String memberCustomOptionVOs,
						   @InterfaceParam(name = "memberBaseOptionVOs")@Validation(must = false, name = "memberBaseOptionVOs")String memberBaseOptionVOs) throws Exception, BaseException{
		UserOrgVO org = getUser();
		boolean isVip=isVip(org);
		if(!isVip){
			setActionResult("","");
			return;
		}
		if(AssertUtil.isEmpty(tbQyMemberConfigPO.getId())){
			throw new NonePrintException("1011",ErrorTip.longin_info_error.toString());
		}
		tbQyMemberConfigPO.setCreateTime(new Date());
		tbQyMemberConfigPO.setUpdateTime(new Date());
		tbQyMemberConfigPO.setOrgId(org.getOrgId());
		tbQyMemberConfigPO.setOrgName(org.getOrgName());
		tbQyMemberConfigPO.setCreator(org.getUserName());
		if(StringUtil.isNullEmpty(tbQyMemberConfigPO.getCustom())){
			tbQyMemberConfigPO.setCustom("-1");
		}
		List<TbQyMemberCustomConfigPO> customConfigPOList = new ArrayList<TbQyMemberCustomConfigPO>();
		List<TbQyMemberBaseConfigPO> baseConfigPOList = new ArrayList<TbQyMemberBaseConfigPO>();
		if(!AssertUtil.isEmpty(memberBaseOptionVOs)){//如果从页面传来的基础字段设置不为空
			JSONArray jsonArray = JSONArray.fromObject(memberBaseOptionVOs);
			//从jsp页面传来的base转换成的数组
			TbQyMemberBaseConfigPO[] baseJspVOs = (TbQyMemberBaseConfigPO[]) JSONArray.toArray(jsonArray, TbQyMemberBaseConfigPO.class);
			baseConfigPOList.addAll(Arrays.asList(baseJspVOs));
		}
		//如果该用户是金卡，而且自定义设置不为空
		if(VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK) && !AssertUtil.isEmpty(memberCustomOptionVOs)){
			JSONArray jsonArray = JSONArray.fromObject(memberCustomOptionVOs);
			//从jsp页面传来的custom转换成的数组
			TbQyMemberCustomConfigPO[] customJspVOs = (TbQyMemberCustomConfigPO[]) JSONArray.toArray(jsonArray, TbQyMemberCustomConfigPO.class);
			customConfigPOList.addAll(Arrays.asList(customJspVOs));
		}
		this.memberService.saveConfig(org.getOrgId(),tbQyMemberConfigPO,deptId,dept, customConfigPOList, baseConfigPOList);
		MemberUtil.addMemberCache(org, tbQyMemberConfigPO);
		addJsonObj("corpId", org.getCorpId());
		addJsonObj("corpIdStr", Configuration.ALUMNI_ASSOCIATION_CI);
	}
	
	/**
	    * 列表查询时，页面要传递的参数
	    */
	   
    @SearchValueTypes(nameFormat = "false", value = { 
    		@SearchValueType(name = "startTimes", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "endTime", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "startApproveTime", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "endApproveTime", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "email", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "title", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "personName", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxPageSearch() throws Exception, BaseException{
			UserOrgVO user=getUser();

			boolean isVip=isVip(user);
			if(!isVip){
				addJsonObj("isVip", false);
			}else{
				addJsonObj("isVip", true);
			}
			if(pageSize == null || pageSize<1){
			     pageSize = getPageSize();
			}else if (pageSize>Configuration.MAX_PAGESIZE) {
			     setActionResult("1002", "每页显示的条数不能超过"+Configuration.MAX_PAGESIZE+"!");
			     return;
			}
            Pager pager = new Pager( ServletActionContext.getRequest(), pageSize);
            Map<String, Object> searchMap = getSearchValue();
            searchMap.put("orgId", user.getOrgId());
            pager=memberService .searchMemberInfo(searchMap,pager);
            List<TbQyMemberInfoVO> voList=(List<TbQyMemberInfoVO>) pager.getPageData();
            //TbQyMemberConfigPO config=memberService.getHistotryData(org.getOrgId());
            if(!AssertUtil.isEmpty(voList)){
            	for (TbQyMemberInfoVO tbQyMemberInfoVO : voList) {
					MemberUtil.addTypeToVO(tbQyMemberInfoVO);
            	}
            }
            addJsonPager("pageData",pager);
    }
    
    
    /**
	 * 获取邀请详情
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询邀请详情失败"))
	public void getDetail(@InterfaceParam(name = "askId")@Validation(must = true, name = "askId")String askId) throws Exception, BaseException {
		UserOrgVO org =  getUser();
		TbQyMemberInfoVO vo=memberService.searchByPk(TbQyMemberInfoVO.class, askId);
		if(AssertUtil.isEmpty(vo)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
	    	return;
		}
		MemberUtil.addTypeToVO(vo);
		List<TbQyMemberUserCustomPO> customList = new ArrayList<TbQyMemberUserCustomPO>();
		//判断是否有权限
		if(VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			customList.addAll(memberService.getMemberUserCustom(vo.getId()));
		}
		addJsonObj("customUserList", customList);
		addJsonObj("tbQyMemberInfoPO", vo);
		 //查询是否有邀请设置信息
		addJsonObj("corpIdStr", Configuration.ALUMNI_ASSOCIATION_CI);
    	addJsonObj("corpId", org.getCorpId());
		addJsonObj("optionVOs", contactCustomMgrService.getUseingOptionByorgId(org.getOrgId()));
	}
	
	/**
	 * 人员邀请审批
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "审批成员邀请失败"))
	public void updateStatus() throws Exception, BaseException {
		IUser iUser=(IUser) DqdpAppContext.getCurrentUser();
		UserOrgVO org =  contactService.getOrgByUserId(iUser.getUsername());
		HttpServletRequest request = ServletActionContext.getRequest();
		String askId = request.getParameter("askId");
		//String deptId=request.getParameter("deptId");
		String type=request.getParameter("type");//审批      1通过         2未通过
		TbQyMemberInfoPO po=memberService.searchByPk(TbQyMemberInfoPO.class, askId);
		if(AssertUtil.isEmpty(po)){
			setActionResult("1999", "对不起，该邀请已删除");
	    	return;
		}
		MemberUtil.checkStatus(po);
		if(MemberHandleUtil.NOT_APPROVE_PASSED.equals(type)){
			po.setApprovePerson(org.getPersonName());
			po.setApproveUserId(iUser.getUsername());
			po.setApproveTime(new Date());
			po.setStatus("2");
			memberService.updatePO(po, false);
		}
	}
	
	/**
	 * 删除成员邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2014-11-3
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
	public void ajaxBatchDelete() throws Exception, BaseException {
		UserOrgVO userInfoVO = getUser();
		if(ids != null  && ids.length>0){
			memberService.delMember(ids,userInfoVO);
		}
	}
	
	/**
	 * 批量审批成员邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2014-11-3
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "审批成功", faileMsg = "审批成员邀请失败"))
	public void ajaxBatchApprove(@InterfaceParam(name = "deptId")@Validation(must = true, name = "deptId")String deptId) throws Exception, BaseException {
		UserOrgVO orgVO = getUser();
		if(ids != null  && ids.length>0){
			List<TbQyMemberInfoPO> list = memberService.batchPObyId(ids);
			Map<String, List<TbQyMemberUserCustomPO>> map = memberService.batchMemberUserCustom(ListUtil.toList(ids));
			memberService.batchApprove(list, deptId, orgVO,map);
		}
	}
	
	/**
	 *	邀请详情编辑
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "修改并审批邀请失败"))
	public void updateMember(@InterfaceParam(name = "btnType")@Validation(must = true, name = "btnType")String btnType,
							 @InterfaceParam(name = "deptNo")@Validation(must = false, name = "deptNo")String deptNo,
							 @InterfaceParam(name = "deptN")@Validation(must = false, name = "deptN")String deptN,
							 @InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyMemberInfoPO po=memberService.searchByPk(TbQyMemberInfoPO.class, tbQyMemberInfoPO.getId());
		if(AssertUtil.isEmpty(po)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
	    	return;
		}
		MemberUtil.checkStatus(po);
		//把更新的数据放入po里面
		MemberUtil.setUpdateToPO(po, tbQyMemberInfoPO, deptNo, deptN);
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//如果从页面传来的自定义字段设置不为空,而且是金卡vip用户
		if(!AssertUtil.isEmpty(customUserList) && VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			JSONArray jsonArray = JSONArray.fromObject(customUserList);
			//从jsp页面传来的base转换成的数组
			TbQyMemberUserCustomPO[] customJspVOs = (TbQyMemberUserCustomPO[]) JSONArray.toArray(jsonArray, TbQyMemberUserCustomPO.class);
			customPOList.addAll(Arrays.asList(customJspVOs));
		}
		memberService.updateMember(btnType, po, org, customPOList );
	}
	
	/**
	 *	邀请详情编辑
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询corpid失败"))
	public void getPagerCorpId() throws Exception, BaseException {
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org = contactService.getOrgByUserId(user);
		addJsonObj("corpIdStr", Configuration.ALUMNI_ASSOCIATION_CI);
		addJsonObj("corpId", org.getCorpId());
	}
	
	
	  /**
	   * 查询所有的设置的邀请单
	   * @author luobowen
	   * @throws Exception
	   * @throws BaseException
	   * @2016-5-24
	   */
	 @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	 public void ajaxSearchConfig() throws Exception, BaseException{
	    	IUser iUser=(IUser) DqdpAppContext.getCurrentUser();
			String user = DqdpAppContext.getCurrentUser().getUsername();
			UserOrgVO org =  contactService.getOrgByUserId(user);
			List<TbQyMemberConfigVO> configList=memberService.getConfigList(org.getOrgId());
	        addJsonObj("configList", configList);
	        boolean isVip=isVip(org);
			addJsonObj("isVip", isVip);
			//判断是否是vip
			/*boolean isVip=false;
			if(!Configuration.AUTO_CORPID.equals(org.getCorpId())){
				DqdpOrgVO orgVO=WxqyhAppContext.getOrgExtInfo(org.getOrgId());
				if(orgVO.isVip()){
					isVip=true;
				}
			}else{
				isVip=true;
			}
			if(isVip){
				List<TbQyMemberConfigVO> configList=memberService.getConfigList(org.getOrgId());
		        addJsonObj("configList", configList);
			}*/
			//addJsonObj("isVip", isVip);
	 }
	 
	 /**
		 * 更新邀请单状态
		 * @throws Exception
		 * @throws BaseException
		 * @author luobowen
		 * @2015-11-12
		 * @version 1.0
		 */
		@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "更新成功", faileMsg = "更新邀请单状态失败"))
		public void updateConfigStatus(@InterfaceParam(name="id")String id,@InterfaceParam(name="status")String status) throws Exception, BaseException {
			IUser iUser=(IUser) DqdpAppContext.getCurrentUser();
			UserOrgVO org =  contactService.getOrgByUserId(iUser.getUsername());
			HttpServletRequest request = ServletActionContext.getRequest();
			if(StringUtil.isNullEmpty(id)){
				logger.info("更新邀请单启用状态参数id="+id+",status"+status);
				setActionResult("1001", "邀请单不存在或已被删除！");
				return;
			}
			TbQyMemberConfigPO po=memberService.searchByPk(TbQyMemberConfigPO.class, id);
			if(AssertUtil.isEmpty(po)){
				setActionResult("1999", "对不起，该邀请单已删除");
		    	return;
			}
			if(StringUtil.isNullEmpty(status)){
				if(open_status.equals(po.getStatus())){
					status="0";
				}else{
					status="1";
				}
			}
			po.setStatus(status);
			memberService.updatePO(po, false);
		}
		
		/**
		 * 更新邀请单状态
		 * @throws Exception
		 * @throws BaseException
		 * @author luobowen
		 * @2015-11-12
		 * @version 1.0
		 */
		@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "删除成功", faileMsg = "删除邀请单失败"))
		public void delMemberConfig(@InterfaceParam(name="ids")String ids) throws Exception, BaseException {
			IUser iUser=(IUser) DqdpAppContext.getCurrentUser();
			UserOrgVO org =  contactService.getOrgByUserId(iUser.getUsername());
			HttpServletRequest request = ServletActionContext.getRequest();
			if(StringUtil.isNullEmpty(ids)){
				logger.info("更新邀请单启用状态参数id="+ids);
				setActionResult("1001", "邀请单不存在或已被删除！");
				return;
			}
			TbQyMemberConfigPO po=memberService.searchByPk(TbQyMemberConfigPO.class, ids);
			if(AssertUtil.isEmpty(po)){
				setActionResult("1999", "对不起，该邀请单已删除！");
		    	return;
			}
			//同步删除部门的关联表
			memberService.delMemberConfigDes(ids);
			memberService.delPO(po);
			if(MemberUtil.SHOW_INDEX.equals(po.getShowIndex())){//如果是展示在首页的单
				CacheWxqyhObject.remove("member", org.getOrgId(), "configVOs");
			}
		}
		
		/**
		 * 初始化人员邀请通知对象表
		 * @throws Exception
		 * @throws BaseException
		 * @author luobowen
		 * @2016-6-29
		 * @version 1.0
		 */
		@ActionRoles({"sysmgrMenu"})
		@JSONOut(catchException = @CatchException(errCode = "1110", successMsg = "初始化成功", faileMsg = "初始化失败"))
		public void initClientType() throws Exception, BaseException {
			//util.initType();
			MemberHandleUtil.initTargetPerson();
		}


	/**
	 * 教育版后台保存外部邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-19
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "保存成功", faileMsg = "保存邀请设置信息失败"))
	public void ajaxUpdateDdu(@InterfaceParam(name = "deptId")@Validation(must = true, name = "deptId")String deptId,
							  @InterfaceParam(name = "dept")@Validation(must = true, name = "dept")String dept,
							  @InterfaceParam(name = "memberCustomOptionVOs")@Validation(must = false, name = "memberCustomOptionVOs")String memberCustomOptionVOs,
							  @InterfaceParam(name = "memberBaseOptionVOs")@Validation(must = false, name = "memberBaseOptionVOs")String memberBaseOptionVOs) throws Exception, BaseException{
		UserOrgVO org = getUser();
		if(AssertUtil.isEmpty(tbQyMemberConfigPO.getId())){
			throw new NonePrintException("1011",ErrorTip.longin_info_error.toString());
		}
		tbQyMemberConfigPO.setCreateTime(new Date());
		tbQyMemberConfigPO.setUpdateTime(new Date());
		tbQyMemberConfigPO.setOrgId(org.getOrgId());
		tbQyMemberConfigPO.setOrgName(org.getOrgName());
		tbQyMemberConfigPO.setCreator(org.getUserName());
		boolean isVip=isVip(org);
		if(!isVip){
			setActionResult("","");
			return;
		}
		if(StringUtil.isNullEmpty(tbQyMemberConfigPO.getCustom())){
			tbQyMemberConfigPO.setCustom("-1");
		}
		List<TbQyMemberCustomConfigPO> customConfigPOList = new ArrayList<TbQyMemberCustomConfigPO>();
		List<TbQyMemberBaseConfigPO> baseConfigPOList = new ArrayList<TbQyMemberBaseConfigPO>();
		if(!AssertUtil.isEmpty(memberBaseOptionVOs)){//如果从页面传来的基础字段设置不为空
			JSONArray jsonArray = JSONArray.fromObject(memberBaseOptionVOs);
			//从jsp页面传来的base转换成的数组
			TbQyMemberBaseConfigPO[] baseJspVOs = (TbQyMemberBaseConfigPO[]) JSONArray.toArray(jsonArray, TbQyMemberBaseConfigPO.class);
			baseConfigPOList.addAll(Arrays.asList(baseJspVOs));
		}
		//如果该用户是金卡，而且自定义设置不为空
		if(VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK) && !AssertUtil.isEmpty(memberCustomOptionVOs)){
			JSONArray jsonArray = JSONArray.fromObject(memberCustomOptionVOs);
			//从jsp页面传来的custom转换成的数组
			TbQyMemberCustomConfigPO[] customJspVOs = (TbQyMemberCustomConfigPO[]) JSONArray.toArray(jsonArray, TbQyMemberCustomConfigPO.class);
			customConfigPOList.addAll(Arrays.asList(customJspVOs));
		}
		this.memberService.saveConfig(org.getOrgId(),tbQyMemberConfigPO,deptId,dept, customConfigPOList, baseConfigPOList);
		MemberUtil.addMemberCache(org, tbQyMemberConfigPO);
		addJsonObj("corpId", org.getCorpId());
	}

	/**
	 * 检查该机构是否有学生
	 * @throws BaseException
	 * @throws Exception
	 * @author liyixin
	 * @2016-11-25
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void checkHasStudent() throws BaseException, Exception{
		UserOrgVO org = getUser();
		int number = studentService.countstudentByOrgId(org.getOrgId());
		if(number > 0){
			addJsonObj("info","1");
		}else {
			addJsonObj("info","0");
		}
	}

	/**
	 *	教育版邀请详情编辑
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "修改并审批邀请失败"))
	public void updateMemberEdu(@InterfaceParam(name = "listJson")@Validation(must = false, name = "listJson")String listJson,
								@InterfaceParam(name = "btnType")@Validation(must = false, name = "btnType")String btnType,
								@InterfaceParam(name = "deptNo")@Validation(must = false, name = "deptNo")String deptId,
								@InterfaceParam(name = "deptN")@Validation(must = false, name = "deptN")String deptName,
								@InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyMemberInfoPO po=memberService.searchByPk(TbQyMemberInfoPO.class, tbQyMemberInfoPO.getId());
		if(AssertUtil.isEmpty(po)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
			return;
		}
		//检查po的状态
		MemberUtil.checkStatus(po);
		//把更新的数据放入po里面
		MemberUtil.setUpdateToPO(po, tbQyMemberInfoPO, deptId, deptName);
		List<TbQyMemberInfoPO> list = new ArrayList<TbQyMemberInfoPO>();
		if(!AssertUtil.isEmpty(listJson)){
			JSONArray jsonArray = JSONArray.fromObject(listJson);
			list = MemberUtil.updateJsonToList(jsonArray, po);
		}
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//如果从页面传来的自定义字段设置不为空,而且是金卡vip用户
		if(!AssertUtil.isEmpty(customUserList) && VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			JSONArray jsonArray = JSONArray.fromObject(customUserList);
			//从jsp页面传来的base转换成的数组
			TbQyMemberUserCustomPO[] customJspVOs = (TbQyMemberUserCustomPO[]) JSONArray.toArray(jsonArray, TbQyMemberUserCustomPO.class);
			customPOList.addAll(Arrays.asList(customJspVOs));
		}
		memberService.updateMemberEdu(btnType, po, org, list, customPOList);
	}

	/**
	 * 教育版批量审批成员邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-29
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "审批成功", faileMsg = "审批成员邀请失败"))
	public void ajaxBatchApproveEdu(@InterfaceParam(name = "deptId")@Validation(must = true, name = "deptId")String deptId) throws Exception, BaseException {
		UserOrgVO orgVO = getUser();
		if(ids != null  && ids.length>0){
			List<TbQyMemberInfoPO> list = memberService.batchPObyId(ids);
			Map<String, List<TbQyMemberUserCustomPO>> map = memberService.batchMemberUserCustom(ListUtil.toList(ids));
			memberService.batchApproveEdu(list, deptId, orgVO, map);
		}
	}

	/**
	 * 教育版获取邀请详情
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询邀请详情失败"))
	public void getDetailEdu(@InterfaceParam(name = "askId")@Validation(must = true, name = "askId")String askId) throws Exception, BaseException {
		UserOrgVO org =  getUser();
		TbQyMemberInfoVO vo=memberService.searchByPk(TbQyMemberInfoVO.class, askId);
		if(AssertUtil.isEmpty(vo)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
			return;
		}
		MemberUtil.addTypeToVO(vo);
		//获取监护人填写的孩子信息
		List<TbQyMemberInfoVO> list = memberService.searchChildrenToVO(askId);
		addJsonObj("list", list);
		addJsonObj("tbQyMemberInfoPO", vo);
		addJsonObj("corpId", org.getCorpId());
		List<TbQyMemberUserCustomPO> customList = new ArrayList<TbQyMemberUserCustomPO>();
		//判断是否有权限
		if(VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			customList.addAll(memberService.getMemberUserCustom(vo.getId()));
		}
		addJsonObj("customUserList", customList);
		addJsonObj("optionVOs", contactCustomMgrService.getUseingOptionByorgId(org.getOrgId()));
	}

	/**
	 * 返回显示在首页的邀请单
	 * @throws BaseException
	 * @throws Exception
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getShowIndex() throws BaseException, Exception{
		UserOrgVO orgVO = getUser();
		List<TbQyMemberConfigVO> configVOs = (List<TbQyMemberConfigVO>) CacheWxqyhObject.get("member", orgVO.getOrgId(), "configVOs");
		if(AssertUtil.isEmpty(configVOs) || configVOs.size() ==0){//如果缓存为空,或者邀请单长度为0
			configVOs = memberService.showIndex(orgVO.getOrgId());
			if(configVOs.size() > 0){//如果有邀请单
				for(int i = 0; i < configVOs.size(); i ++){//检查一下邀请单是否过期，过期结束时间加2年
					int result = configVOs.get(i).getStartTime().compareTo(configVOs.get(i).getStopTime());
					if(result > 0){//已过期
						Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
						cal.add(Calendar.YEAR, 2);//过期时间为2年
						TbQyMemberConfigPO po = new TbQyMemberConfigPO();
						po.setId(configVOs.get(i).getId());
						po.setStopTime(cal.getTime());
						memberService.updatePO(po, false);
					}
				}
				addJsonObj("configVOs", configVOs);
			}else {//如果没有邀请单
				if(IndustryUtil.isEduVersion(orgVO.getOrgId())){//教育版
					configVOs = memberService.addDefaultConfig(orgVO, true);
					addJsonObj("configVOs",configVOs);
				}else{//普通版
					configVOs = memberService.addDefaultConfig(orgVO, false);
					addJsonObj("configVOs", configVOs);
				}
			}
			CacheWxqyhObject.set("member", orgVO.getOrgId(), "configVOs", configVOs);
		}else{//如果缓存不为空
			addJsonObj("configVOs", configVOs);
		}
		if(WxAgentUtil.isTrustAgent(orgVO.getCorpId(), WxAgentUtil.getAddressBookCode())){//如果有托管通讯录
			addJsonObj("isAgentAddressBook", "1");
		}else{//如果没有托管
			addJsonObj("isAgentAddressBook", "0");
		}
		if(11 > OrgUtil.getUserTotal(orgVO.getOrgId(), OrgUtil.USER_MEMBER)){//如果需要显示邀请提示。用于新用户进来
			addJsonObj("needShowHint","1");
		}else{
			addJsonObj("needShowHint","0");
		}
	}

	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "更新", faileMsg = "更新失败"))
	public void updateShowIndex(@InterfaceParam(name = "id")@Validation(must = true, name = "id")String id,
								@InterfaceParam(name = "btnType")@Validation(must = true, name = "btnType")String btnType) throws BaseException, Exception{
		UserOrgVO orgVO = getUser();
		TbQyMemberConfigPO po=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(id) || AssertUtil.isEmpty(po)){
			setActionResult("1999", "对不起，该邀请已删除");
		}
		if("1".equals(btnType)) {//如果是显示在首页
			List<TbQyMemberConfigPO> oldIndexPOs = memberService.selectAllIndex(orgVO.getOrgId());
			if (oldIndexPOs.size() > 0) {//如果原来有默认单
				for (TbQyMemberConfigPO oldIndexPO : oldIndexPOs) {
					oldIndexPO.setShowIndex(MemberUtil.NO_SHOW_INDEX);
				}
				po.setShowIndex(MemberUtil.SHOW_INDEX);
				oldIndexPOs.add(po);
				QwtoolUtil.updateBatchList(oldIndexPOs, false);
			} else {//如果原来没有
				po.setShowIndex(MemberUtil.SHOW_INDEX);
				memberService.updatePO(po, false);
			}
			MemberUtil.addMemberCache(orgVO, po);
		}else if("0".equals(btnType)){//如果是取消显示
			po.setShowIndex(MemberUtil.NO_SHOW_INDEX);
			memberService.updatePO(po, false);
			CacheWxqyhObject.remove("member", orgVO.getOrgId(), "configVOs");
		}
		addJsonObj("memberName",po.getName());
	}
}
