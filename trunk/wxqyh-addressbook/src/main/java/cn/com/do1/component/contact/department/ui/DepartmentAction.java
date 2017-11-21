package cn.com.do1.component.contact.department.ui;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.vo.SelectUserVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.vo.*;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.UserInfoChangeNotifier;
import cn.com.do1.component.contact.department.util.CheckDeptVisible;
import cn.com.do1.component.contact.department.util.DepartmentDictUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.ImportResultUtil;
import cn.com.do1.component.util.WxqyhBaseAction;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.FileUtil;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.contact.department.thread.BatchDeptToUserImportThread;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.util.StringVerifyUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class DepartmentAction extends WxqyhBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(DepartmentAction.class);
    private IDepartmentService departmentService;
    private IDepartmentMgrService departmentMgrService;
    private TbDepartmentInfoPO tbDepartmentInfoPO;
	private IContactService contactService;
	private IContactMgrService contactMgrService;
    private String ids[];
    private String id;
    private String userId;
    private String deptIds;
    private String userIds;
    
    private File upFile;
    private String upFileFileName;
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
	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}

	@Resource(name = "departmentService")
    public void setDepartmentService(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	private IExperienceapplicationService experienceapplicationService;

	@Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(
			IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
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

	/**
	* 列表查询时，页面要传递的参数
	*/
    @SearchValueTypes(
        nameFormat="false",value={
        @SearchValueType(name = "createTime", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
        @SearchValueType(name = "departmentName", type="string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException{
            Pager pager = new Pager( ServletActionContext.getRequest(), getPageSize());
            String user = DqdpAppContext.getCurrentUser().getUsername();
    		UserOrgVO org =  contactService.getOrgByUserId(user);
    		if(org!=null){
    			getSearchValue().put("orgId",org!=null?org.getOrgId():"");
    		}
            pager=departmentMgrService.searchDepartment(getSearchValue(),pager);
            addJsonPager("pageData",pager);
    }

	/**
	 * 查询部门
	 * @throws Exception
	 * @throws BaseException
	 * @author Hejinjiao
	 * @2014-9-30
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void searchDepartment() throws Exception, BaseException {
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		TbDepartmentInfoPO vo = departmentMgrService.searchByPk(TbDepartmentInfoPO.class, id);
		if(vo!=null){
			if(org.getOrgId().equals(vo.getOrgId())){
				addJsonObj("department",vo);
			}
			else{
				setActionResult("1001", "该部门不可用");
			}
		}else{
			setActionResult("1002", "该部门不可用");
			//此用户没有机构信息，不查询数据
			return ;
		}
	
	}
	
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
	public void ajaxAdd(@InterfaceParam(name = "selectUserIds") @Validation(must = false, name = "部门负责人") String selectUserIds,
						@InterfaceParam(name = "deptIds")@Validation(must = false, name = "deptIds")String deptIds,
						@InterfaceParam(name = "userIds")@Validation(must = false, name = "userIds")String userIds)
			throws Exception, BaseException {
		UserOrgVO org =  getUser();
		if(StringVerifyUtil.verifyRules(tbDepartmentInfoPO.getDepartmentName())){
    		setActionResult("1", "部门名称不能包括："+StringVerifyUtil.getVerifyMatche());
    		return;
    	}
		//如果部门权限是仅特定对象，而且不是vip用户
		if(!AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission()) && !VipUtil.isQwVip(org.getOrgId())){
			setActionResult("1001","部门权限不允许是仅特定对象");
			return;
		}
    	tbDepartmentInfoPO.setCreateTime(new Date());
    	tbDepartmentInfoPO.setCreatePerson(DqdpAppContext.getCurrentUser().getUsername());
		tbDepartmentInfoPO.setOrgId(org.getOrgId());
		//验证是否重名
		List<TbDepartmentInfoPO> list = departmentService.getDeptByParent(org.getOrgId(),tbDepartmentInfoPO.getParentDepart(),tbDepartmentInfoPO.getDepartmentName());
		if(!AssertUtil.isEmpty(list)){
			setActionResult("2001", "部门[" + tbDepartmentInfoPO.getDepartmentName() + "]已存在");
			return ;
		}
		departmentMgrService.addDepart(deptIds, userIds, org, selectUserIds, tbDepartmentInfoPO);
		List<DeptSyncInfoVO> addDeptList = new ArrayList<DeptSyncInfoVO>(1);
		DeptSyncInfoVO vo = new DeptSyncInfoVO();
		BeanHelper.copyBeanProperties(vo,tbDepartmentInfoPO);
		addDeptList.add(vo);
		UserInfoChangeNotifier.batchChangeDept(org,addDeptList,null,null, UserInfoChangeInformType.USER_MGR);
		//添加部门负责人
		departmentMgrService.updateDeptReceive(tbDepartmentInfoPO, selectUserIds, true);
		addJsonObj("nodeId", tbDepartmentInfoPO.getId());
    	addJsonObj("parentId", tbDepartmentInfoPO.getParentDepart());
    	addJsonObj("nodeName", tbDepartmentInfoPO.getDepartmentName());
    	addJsonObj("nodeTitle", tbDepartmentInfoPO.getDeptFullName());
    }

    @JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void ajaxUpdate(@InterfaceParam(name = "selectUserIds") @Validation(must = false, name = "部门负责人") String selectUserIds,
						   @InterfaceParam(name = "isUseAll") @Validation(must = false, name = "isUseAll") String isUseAll,
						   @InterfaceParam(name = "deptIds")@Validation(must = false, name = "deptIds")String deptIds,
						   @InterfaceParam(name = "userIds")@Validation(must = false, name = "userIds")String userIds) throws Exception, BaseException{
		UserOrgVO org =  getUser();
    	if(StringVerifyUtil.verifyRules(tbDepartmentInfoPO.getDepartmentName())){
    		setActionResult("1", "部门名称不能包括："+StringVerifyUtil.getVerifyMatche());
    		return;
    	}
		//如果部门权限是仅特定对象，而且不是vip用户
		if(!AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission()) && !VipUtil.isQwVip(org.getOrgId())){
			setActionResult("1001","部门权限不允许是仅特定对象");
			return;
		}
		List<DeptSyncInfoVO> list = departmentMgrService.updateDept(tbDepartmentInfoPO,org, isUseAll, deptIds, userIds,true);
		//添加部门负责人
		departmentMgrService.updateDeptReceive(tbDepartmentInfoPO, selectUserIds, false);
		if(list != null && list.size() > 0){
			UserInfoChangeNotifier.batchChangeDept(org,null,list,null, UserInfoChangeInformType.USER_MGR);
		}
		addJsonObj("nodeId", tbDepartmentInfoPO.getId());
    	addJsonObj("parentId", tbDepartmentInfoPO.getParentDepart());
    	addJsonObj("nodeName", tbDepartmentInfoPO.getDepartmentName());
    	addJsonObj("nodeTitle", tbDepartmentInfoPO.getDeptFullName());
    }

    
    /**
	 * 删除部门
	 * @throws Exception
	 * @throws BaseException 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1010", successMsg = "删除成功", faileMsg = "删除失败"))
	public void deleteTbDepartmentInfoPO() throws Exception, BaseException {
		if (AssertUtil.isEmpty(id))
			id = ids[0];
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		/*//此部门下的子部门
		List<TbDepartmentInfoPO> deptList = departmentService.getChildDepart(orgId, id);
		//此部门下的人员
		List<TbQyUserInfoVO> personList = contactService.findUsersByDepartId(orgId,id);
		if(!AssertUtil.isEmpty(deptList)||!AssertUtil.isEmpty(personList)){
			throw new BaseException("没有子部门且部门内没有人员才可删除");
		}*/
		//此部门下的人员
		/*if(contactService.hasUsersByDepartId(id)){
			setActionResult("1001", "部门内没有人员才可删除");
			return ;
		}
		tbDepartmentInfoPO = departmentService.searchByPk(TbDepartmentInfoPO.class, id);
		if(null == tbDepartmentInfoPO){
			setActionResult("1001", "获取不到对应要删除的部门信息");
			return ;
		}
		//添加删除日志
		contactService.insertDel(user, user, "删除部门："+tbDepartmentInfoPO.getDeptFullName()+"WxId"+tbDepartmentInfoPO.getWxId(), "addressBook", orgId);
		deleWxDept(tbDepartmentInfoPO.getWxId(),org.getCorpId(),org.getOrgId());  //同步删除微信后台部门
		contactService.delPO(tbDepartmentInfoPO);				//删除本系统部门*/
		List<TbDepartmentInfoPO> pos = departmentService.delDeptAndChild(org, id);
		List<DeptSyncInfoVO> delList = new ArrayList<DeptSyncInfoVO>(1);
		for(int i = 0; i < pos.size(); i++){
			DeptSyncInfoVO vo = new DeptSyncInfoVO();
			vo.setId(pos.get(i).getId());
			vo.setDepartmentName(pos.get(i).getDepartmentName());
			vo.setDeptFullName(pos.get(i).getDeptFullName());
			vo.setParentDepart(pos.get(i).getParentDepart());
			vo.setPermission(pos.get(i).getPermission());
			vo.setShowOrder(pos.get(i).getShowOrder());
			vo.setWxId(pos.get(i).getWxId());
			vo.setWxParentid(pos.get(i).getWxId());
			delList.add(vo);
		}
		UserInfoChangeNotifier.batchChangeDept(org, null, null, delList, UserInfoChangeInformType.USER_MGR);
	}
	/**
	 * 删除部门的同时，删除微信后台 部门
	 * @throws Exception
	 * @throws BaseException
	 */
	private void deleWxDept(String deptId,String corpId,String orgId) throws Exception, BaseException{
		//String deptId = getWxDeptIdByFullName(fullName,corpId);
		WxDeptService.delDept(deptId,corpId,orgId);
	}
	
	public WxDept getDeptByOrgName(List<WxDept> deptList,String DeptName){
		if(!AssertUtil.isEmpty(deptList)){
			for(WxDept wxDept : deptList){
				//父部门ID为1或0的才是机构(0是最顶层，1是挂在企业微信号下面的一账号多机构)
				if(!AssertUtil.isEmpty(wxDept) && DeptName.equals(wxDept.getName()) && ("1".equals(wxDept.getParentid())||"0".equals(wxDept.getParentid()))){
					return wxDept;
				}
			}
		}
		return null;
	}
    /**
     * 验证部门是否已存在
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void isExist(){
    	HttpServletRequest request = ServletActionContext.getRequest();
    	String name = request.getParameter("name");
    	try {
    		String user = DqdpAppContext.getCurrentUser().getUsername();
    		UserOrgVO org =  contactService.getOrgByUserId(user);
    		String orgId = org!=null?org.getOrgId():"";
			List<TbDepartmentInfoPO> list = departmentMgrService.searchDepartByName(orgId,name);
			addJsonObj("exist", (list!=null&&list.size()>0)?"1":"0");
		} catch (Exception e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获得所有部门数据
     */
    @JSONOut(catchException = @CatchException(errCode = "1006", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getAllDepart(){
    	try {
    		String user = DqdpAppContext.getCurrentUser().getUsername();
    		UserOrgVO org =  contactService.getOrgByUserId(user);
    		String orgId = org!=null?org.getOrgId():"";
			List<TbDepartmentInfoPO> list = departmentService.getAllDepart(orgId);
			addJsonArray("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
    }
	public void setTbDepartmentInfoPO(TbDepartmentInfoPO tbDepartmentInfoPO) {
		this.tbDepartmentInfoPO = tbDepartmentInfoPO;
	}

	public TbDepartmentInfoPO setTbDepartmentInfoPO() {
		return this.tbDepartmentInfoPO;
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxView() throws Exception, BaseException {
		TbDepartmentInfoPO xxPO = departmentService.searchByPk(
				TbDepartmentInfoPO.class, id);
		TbDepartmentInfoVO vo=new TbDepartmentInfoVO();
		if(null == xxPO){
			setActionResult("1001", "获取不到对应部门信息");
			return ;
		}
		List<SelectUserVO> userObjList = new ArrayList<SelectUserVO>();
		List<SelectDeptVO> deptObjList = new ArrayList<SelectDeptVO>();
		//如果该部门权限是仅特定对象
		if(DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(xxPO.getPermission())){
			List<TbDepaSpecificObjPO> objList = departmentMgrService.getspecificObjByDepaId(xxPO.getId(), xxPO.getOrgId());
			List<String> userIds = new ArrayList<String>();
			List<String> deptIds = new ArrayList<String>();
			for(TbDepaSpecificObjPO objPO : objList){
				if(DepartmentUtil.ID_TYPE_USER.equals(objPO.getIdType())){  //如果该对象是用户
					userIds.add(objPO.getObjId());
				}
				if(DepartmentUtil.ID_TYPE_DEPT.equals(objPO.getIdType())){     //如果该对象是部门
					deptIds.add(objPO.getObjId());
				}
			}
			if(deptIds.size() > 0){
				deptObjList.addAll(departmentMgrService.getDeptByIds(deptIds));
			}
			if(userIds.size() > 0){
				userObjList.addAll(contactMgrService.findUserByUserIds(userIds));
			}
		}
		addJsonObj("userObjList", userObjList);
		addJsonObj("deptObjList", deptObjList);
		if (!AssertUtil.isEmpty(xxPO.getParentDepart())) {
			TbDepartmentInfoPO parent = departmentService.searchByPk(TbDepartmentInfoPO.class, xxPO.getParentDepart());
			if (parent!=null) {
				vo.setParentPermission(parent.getPermission());
			}
		}
		vo.setId(xxPO.getId());
		vo.setDepartmentName(xxPO.getDepartmentName());
		vo.setDeptFullName(xxPO.getDeptFullName());
		vo.setOrgId(xxPO.getOrgId());
		vo.setParentDepart(xxPO.getParentDepart());
		vo.setPermission(xxPO.getPermission());
		vo.setShowOrder(xxPO.getShowOrder());
		vo.setLeaveMessage(xxPO.getLeaveMessage());
		vo.setAttribute(xxPO.getAttribute());
		addJsonObj("tbDepartmentInfoPO", vo);

		//加载部门的直接负责人
		List<TbQyUserInfoVO> list=departmentMgrService.getDeptReceiveList(xxPO.getId(),xxPO.getOrgId());
		addJsonArray("deptReceiveList",list);
	}
	/**
	 * 后台查询目标对象
	 * @throws Exception
	 * @throws BaseException
	 * @author Hejinjiao
	 * @2015-1-8
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getDeptsAndUsers() throws Exception, BaseException {
		
		String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO org =  contactService.getOrgByUserId(user);
		if(org==null){
			
		}
		String[] deptArray = deptIds.split("\\|");
		String[] userArray = userIds.split("\\|");
		List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>();
		List<TbQyUserInfoVO> userList = new ArrayList<TbQyUserInfoVO>();
		
		for (String dept : deptArray) {
			
			if (!"".equals(dept)) {
				
				TbDepartmentInfoPO deptPO = contactService.searchByPk(TbDepartmentInfoPO.class, dept);
				if (deptPO != null) {
					deptList.add(deptPO);
				}
			}
		}
		
		for (String id : userArray) {
			
			if (!"".equals(user)) {
				
				TbQyUserInfoVO userVO = contactService.findUserInfoByUserId(id);
				if (userVO != null) {
					
					userList.add(userVO);
				}
			}
		}
		
		addJsonArray("userList", userList);
		addJsonArray("deptList", deptList);
		
	}
	
	/**
	 * @author lishengtao
	 * 2015-8-10
	 * 导出导入部门负责人模板
	 * @throws Exception
	 * @throws BaseException
	 */
	public void exportDeptToUserTmp()throws Exception,BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
    	String userName = user.getUsername();
    	UserOrgVO userOrgVO =  contactService.getOrgByUserId(userName);
    	if(userOrgVO == null){
        	logger.error("获取登录人"+userId+"orgId的信息为空");
        	return;
    	}

		logger.info("导出部门负责人模板开始");
		Date date = new Date();//操作开始时间
		OutputStream os = null;
		HttpServletResponse response;
		try {
			response = ServletActionContext.getResponse();
			os = response.getOutputStream();
			HttpServletRequest request = ServletActionContext.getRequest();

	    	String fileName = "批量导入部门负责人"+DateUtil.format(new Date(), "yyyyMMdd")+".xlsx";
			String agent = request .getHeader("User-Agent");
			boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
			if (isMSIE) {
			    fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
			    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition","attachment; filename=\""+fileName+"\"");
			response.setContentType("application/msexcel");
			Workbook writeWB = new SXSSFWorkbook(500);
			Sheet writeSheet;
			Row writeRow;//行
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Map<String,String> titleMap =new HashMap<String, String>();
			
			int rowIndex=0;//初始化行号
			writeSheet = writeWB.createSheet("部门负责人模板");//创建页
			writeRow = writeSheet.createRow(rowIndex);//创建行
			
			writeRow.createCell(0).setCellValue("部门ID");
			writeRow.createCell(1).setCellValue("部门名称");
			writeRow.createCell(2).setCellValue("部门全称");
			writeRow.createCell(3).setCellValue("负责人名称");
			writeRow.createCell(4).setCellValue("负责人账号");

			String orgId=userOrgVO.getOrgId();
			List<TbDepartmentInfoPO> deptList=null;
			AgentCacheInfo aci = WxAgentUtil.getAgentCache(userOrgVO.getCorpId(),WxAgentUtil.getAddressBookCode());
			if(aci == null || !aci.isTrust()){
				return;
			}
			if(!aci.isAllUserUsable()){
				if(!AssertUtil.isEmpty(aci.getPartys())){
					deptList = departmentService.getDeptByWxIds(orgId, aci.getPartys().split("\\|"));
				}
				if(deptList != null && deptList.size()>0){
					List<TbQyOrganizeInfo> list = departmentMgrService.getListDeptNodeByDeparts(userOrgVO.getOrgId(), null, deptList);
					deptList.clear();
					deptList = corpyDeptFromOrgInfo(deptList,list);
				}
			}
			else {
				deptList=this.departmentMgrService.getAllDepartOrderByDeptName(orgId);//所有部门列表
			}
			
			//获取部门信息
			if(null!=deptList && deptList.size()>0){
				TbDepartmentInfoPO deptInfoPO;
				for(int i=0;i<deptList.size();i++){
					//重置userIds
					userIds = "";
					rowIndex=rowIndex+1;
					writeRow = writeSheet.createRow(rowIndex);//创建行
					deptInfoPO=deptList.get(i);
					writeRow.createCell(0).setCellValue(deptInfoPO.getId());
					writeRow.createCell(1).setCellValue(deptInfoPO.getDepartmentName());
					writeRow.createCell(2).setCellValue(deptInfoPO.getDeptFullName());
					//获取部门负责人信息
					String userNames="";
					List<TbQyUserInfoVO> userReceiveList=this.departmentMgrService.getDeptReceiveList(deptInfoPO.getId(), orgId);
					if(null!=userReceiveList && userReceiveList.size()>0){
						for(int j=0;j<userReceiveList.size();j++){
							userNames=userNames+userReceiveList.get(j).getPersonName();
							userIds=userIds+userReceiveList.get(j).getWxUserId();
							if(j<userReceiveList.size()-1){
								userNames=userNames+"|";
								userIds=userIds+"|";
							}
						}
					}
					writeRow.createCell(3).setCellValue(userNames);
					writeRow.createCell(4).setCellValue(userIds);
				}
			}
			
			//组成帮助页
			writeSheet = writeWB.createSheet("导入负责人说明");//创建页
			writeRow = writeSheet.createRow(0);
			writeRow.createCell(0).setCellValue("字段");
			writeRow.createCell(1).setCellValue("说明");
			writeRow.createCell(2).setCellValue("是否必填");
			writeRow = writeSheet.createRow(1);
			writeRow.createCell(0).setCellValue("部门ID");
			writeRow.createCell(1).setCellValue("ID重下载的模板中有记录，导入部门负责人的标记，必须有效且不为空");
			writeRow.createCell(2).setCellValue("是");
			writeRow = writeSheet.createRow(2);
			writeRow.createCell(0).setCellValue("部门名称");
			writeRow.createCell(1).setCellValue("非必填项，主要用来记录显示");
			writeRow.createCell(2).setCellValue("否");
			writeRow = writeSheet.createRow(3);
			writeRow.createCell(0).setCellValue("部门全称");
			writeRow.createCell(1).setCellValue("非必填项，主要用来显示部门的层级关系");
			writeRow.createCell(2).setCellValue("否");
			writeRow = writeSheet.createRow(4);
			writeRow.createCell(0).setCellValue("负责人名称");
			writeRow.createCell(1).setCellValue("非必填项，主要用来记录显示情况");
			writeRow.createCell(2).setCellValue("否");
			writeRow = writeSheet.createRow(5);
			writeRow.createCell(0).setCellValue("负责人账号");
			writeRow.createCell(1).setCellValue("必填项，注意：1、这里会替换以前所有部门负责人的情况；2、多个负责人用'|'分隔，如果有重复，导入时会作重复验证!;3、若为空的时候，表明清除所有负责人");
			writeRow.createCell(2).setCellValue("是");
			writeRow = writeSheet.createRow(6);
			writeRow.createCell(0).setCellValue("下载模板");
			writeRow.createCell(1).setCellValue("下载的模板包含了当前所有部门以及部门负责人的情况！如果某些部门负责人不需要修改，请删除对应的行。");
			writeRow.createCell(2).setCellValue("");
			
			writeWB.write(os);
		} catch (Exception e) {
			logger.error("导出导入部门负责人模板出现异常！", e);
		} catch (BaseException e) {
			logger.error("导出导入部门负责人模板出现异常！", e);
			e.printStackTrace();
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("导出导入部门负责人,关闭io异常！", e);
				}
			}
			logger.error("导出导入部门负责人模板完成，用时："+(new Date().getTime()-date.getTime())+"ms");
		}
	}

	/**
	 * 将查询部门列表转为部门po
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-28
	 */
	private List<TbDepartmentInfoPO> corpyDeptFromOrgInfo(List<TbDepartmentInfoPO> deptList, List<TbQyOrganizeInfo> list) {
		if(list != null && list.size()>0){
			TbDepartmentInfoPO po;
			for(TbQyOrganizeInfo org : list){
				po = new TbDepartmentInfoPO();
				po.setId(org.getNodeId());
				po.setDepartmentName(org.getNodeName());
				po.setDeptFullName(org.getNodeTitle());
				deptList.add(po);
			}
		}
		return deptList;
	}

	/**
	 * @author lishengtao
	 * 2015-8-10
	 * 批量导入部门负责人
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "导入成功", faileMsg = "导入失败"))
	public void importDeptToUser()throws Exception,BaseException{
		String userName = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO userOrgVO = contactService.getOrgByUserId(userName);
		if(userOrgVO == null){
	    	logger.error("获取登录人"+userName+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}

    	logger.debug("importDeptToUser "+upFile.getPath()+"size"+upFile.length());

		String id=UUID.randomUUID().toString();
		HttpServletRequest request = ServletActionContext.getRequest();
		String type = request.getParameter("type");
		String filePath = DqdpAppContext.getAppRootPath()+File.separator+"uploadFiles";
		File newFile = new File(filePath+File.separator+id+upFileFileName);
		FileUtil.copy(upFile, newFile, true);
		BatchDeptToUserImportThread thread=new BatchDeptToUserImportThread(newFile,upFileFileName,userOrgVO,type,id);
		Thread t = new Thread(thread);
		t.start();
		addJsonObj("start", "0");
		addJsonObj("id",id);
    	logger.debug("importDeptToUser "+upFile.getPath()+"size"+upFile.length());
	}
	
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void viewImportProcess(){
		ResultVO resultvo= ImportResultUtil.getResultObject(id);
		if(resultvo !=null){
			addJsonObj("processNum", resultvo.getProcessNum());
			addJsonObj("totalNum", resultvo.getTotalNum());
			addJsonObj("isFinish", resultvo.isFinish());
			if(resultvo.isFinish()){
				addJsonArray("errorlist", resultvo.getErrorlist());
				addJsonArray("repeatList", resultvo.getRepeatList());
				ImportResultUtil.removeResult(id);
			}
		}
	}
	
	/**
	 * 获取导入部门负责人的导入信息
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getDeptToUserImportLog()throws Exception,BaseException{
		String userName = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO userOrgVO = contactService.getOrgByUserId(userName);
		if(userOrgVO == null){
	    	logger.error("获取登录人"+userName+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("orgId", userOrgVO.getOrgId());
		params1.put("type", "1");//导入部门负责人
		params1.put("status", "1");//导入成功
		List<ImportDeptToUserVO> list1=this.departmentMgrService.getDeptReceiveImportLogList(params1);
		
		Map<String,Object> params2=new HashMap<String,Object>();
		params2.put("orgId", userOrgVO.getOrgId());
		params2.put("type", "1");//导入部门负责人
		params2.put("status", "0");//导入失败
		List<ImportDeptToUserVO> list2=this.departmentMgrService.getDeptReceiveImportLogList(params2);
		
		ImportResultVO vo=new ImportResultVO();
		if(list1!=null && list1.size()>0){
			vo.setSuccessCount(list1.size());
		}else{
			vo.setSuccessCount(0);
		}
		
		if(list2!=null && list2.size()>0){
			vo.setErrorCount(list2.size());
		}else{
			vo.setErrorCount(0);
		}
		vo.setSumCount(vo.getSuccessCount()+vo.getErrorCount());
		
		addJsonObj("ImportResultVO",vo);
	}
	
	/**
	 * 导出部门负责人错误记录
	 * @throws Exception
	 * @throws BaseException
	 */
	public void exportImportDeptReceiveError() throws Exception,BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
    	String userName = user.getUsername();
    	UserOrgVO userOrgVO =  contactService.getOrgByUserId(userName);
    	if(userOrgVO == null){
        	logger.error("获取登录人"+userId+"orgId的信息为空");
        	return;
    	}

		logger.info("导出导入部门负责人模板错误记录");
		Date date = new Date();//操作开始时间
		OutputStream os = null;
		HttpServletResponse response;
		try {
			response = ServletActionContext.getResponse();
			os = response.getOutputStream();
			HttpServletRequest request = ServletActionContext.getRequest();

	    	String fileName = "导入部门负责人错误记录"+DateUtil.format(new Date(), "yyyyMMdd")+".xlsx";
			String agent = request .getHeader("User-Agent");
			boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
			if (isMSIE) {
			    fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
			    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition","attachment; filename=\""+fileName+"\"");
			response.setContentType("application/msexcel");
			Workbook writeWB = new SXSSFWorkbook(500);
			Sheet writeSheet;
			Row writeRow;//行

			int rowIndex=0;//初始化行号
			writeSheet = writeWB.createSheet("导入部门负责人错误错误记录");//创建页
			writeRow = writeSheet.createRow(rowIndex);//创建行
			
			writeRow.createCell(0).setCellValue("部门ID");
			writeRow.createCell(1).setCellValue("部门名称");
			writeRow.createCell(2).setCellValue("部门全称");
			writeRow.createCell(3).setCellValue("负责人名称");
			writeRow.createCell(4).setCellValue("负责人账号");
			writeRow.createCell(5).setCellValue("导入结果");
			
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("orgId", userOrgVO.getOrgId());
			params.put("type", "1");//导入部门负责人
			List<ImportDeptToUserVO> list=this.departmentMgrService.getDeptReceiveImportLogList(params);
			
			ImportDeptToUserVO vo;
			for(int i=0;i<list.size();i++){
				vo=list.get(i);
				rowIndex=rowIndex+1;
				writeRow = writeSheet.createRow(rowIndex);//创建行
				writeRow.createCell(0).setCellValue(vo.getDepartmentId());
				writeRow.createCell(1).setCellValue(vo.getDepartmentName());
				writeRow.createCell(2).setCellValue(vo.getDeptFullName());
				writeRow.createCell(3).setCellValue(vo.getPersonNames());
				writeRow.createCell(4).setCellValue(vo.getWxUserIds());
				writeRow.createCell(5).setCellValue(vo.getMsg());
			}
			writeWB.write(os);
		} catch (Exception e) {
			logger.error("导出导入部门负责人错误记录出现异常！", e);
		} catch (BaseException e) {
			logger.error("导出导入部门负责人错误记录出现异常！", e);
			e.printStackTrace();
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("导出导入部门负责人错误记录,关闭io异常！", e);
				}
			}
			logger.error("导出导入部门负责人错误记录完成，用时："+(new Date().getTime()-date.getTime())+"ms");
		}
	}

	public TbDepartmentInfoPO getTbDepartmentInfoPO() {
		return this.tbDepartmentInfoPO;
	}

    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return deptIds
	 */
	public String getDeptIds() {
		return deptIds;
	}

	/**
	 * @param deptIds 要设置的 deptIds
	 */
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	/**
	 * @return userIds
	 */
	public String getUserIds() {
		return userIds;
	}

	/**
	 * @param userIds 要设置的 userIds
	 */
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
}
