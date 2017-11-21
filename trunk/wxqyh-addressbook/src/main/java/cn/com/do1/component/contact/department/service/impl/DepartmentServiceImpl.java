package cn.com.do1.component.contact.department.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserReceivePO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.vo.*;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.UserListSyncThread;
import cn.com.do1.component.contact.department.dao.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.model.TbQyDeptReceiveImportLogPO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.contact.department.util.CheckDeptVisible;
import cn.com.do1.component.contact.department.util.DepartmentDictUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.department.util.DeptToUserExcelUtil;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ImportResultUtil;
import cn.com.do1.component.util.IntegerUtil;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
@Service("departmentService")
public class DepartmentServiceImpl extends BaseService implements IDepartmentService, IDepartmentMgrService {
	private final static transient Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	private IDepartmentDAO departmentDAO;
	private IDepartmentReadOnlyDAO departmentReadOnlyDAO;
	private IContactService contactService;
	private IContactMgrService contactMgrService;

	@Resource
	public void setDepartmentDAO(IDepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
		setDAO(departmentDAO);
	}

	@Resource
	public void setDepartmentReadOnlyDAO(IDepartmentReadOnlyDAO departmentReadOnlyDAO) {
		this.departmentReadOnlyDAO = departmentReadOnlyDAO;
	}

	@Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}


	public Pager searchDepartment(Map searchMap, Pager pager) throws Exception, BaseException {
		return departmentDAO.searchDepartment(searchMap, pager);
	}

	@Override
	public List<TbDepartmentInfoPO> searchDepartByName(String orgId, String name) throws Exception, BaseException {
		return departmentDAO.searchDepartByName(orgId, name);
	}

	@Override
	public List<TbDepartmentInfoPO> getAllDepartForSync(String orgId) throws Exception, BaseException {
		return departmentDAO.getAllDepart(orgId);
	}

	@Override
	public List<TbDepartmentInfoPO> getAllDepart(String orgId) throws Exception, BaseException {
		return getRemoveTempDeptPO(departmentDAO.getAllDepart(orgId));
	}

	@Override
	public List<TbDepartmentInfoVO> getFirstDepart(String orgId) throws Exception, BaseException {
		return getRemoveTempDeptVO(departmentDAO.getFirstDepart(orgId));
	}



	@Override
	public List<TbDepartmentInfoVO> getChildDepart(String orgId, String departId) throws Exception, BaseException {
		return departmentDAO.getChildDepart(orgId, departId);
	}

	@Override
	public List<TbDepartmentInfoPO> getChildDepartPO(String orgId, String departId) throws Exception, BaseException {
		return departmentDAO.getChildDepartPO(orgId, departId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#getSearchDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getSearchDepart(String orgId, String keyWord) throws Exception, BaseException {
		return departmentDAO.getSearchDepart(orgId, keyWord);
	}

	@Override
	public List<TbDepartmentInfoPO> getDeptByParent(String orgId, String parenttId, String deptName) throws Exception, BaseException {
		return departmentDAO.getDeptByParent(orgId, parenttId, deptName);
	}

	@Override
	public List<TbDepartmentInfoPO> getChildDeptByFullName(String orgId, String deptFullName) throws Exception, BaseException {
		return departmentDAO.getChildDeptByFullName(orgId, deptFullName);
	}

	@Override
	public List<TbDepartmentInfoPO> searchDepartByName(String name) throws Exception, BaseException {
		return departmentDAO.searchDepartByName(name);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#getAllDepartCompress(java.lang.String)
	 */
	@Override
	public List<DepCompress> getAllDepartCompress(String id) throws Exception, BaseException {
		return departmentDAO.getAllDepartCompress(id);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#hasChildDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasChildDepart(String organId, String deptId) throws Exception, BaseException {
		return departmentDAO.hasChildDepart(organId, deptId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#hasDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasDepart(String orgId, String keyWord) throws Exception, BaseException {
		return departmentDAO.hasDepart(orgId, keyWord);
	}

	@Override
	public List<TbDepartmentInfoPO> getDeptInfo(String[] departIds) throws Exception, BaseException {
		if (departIds == null || departIds.length == 0) {
			return new ArrayList<TbDepartmentInfoPO>();
		}
		/*String deptId;
		int size = departIds.length;
		TbDepartmentInfoPO po;
		for (int i = 0; i < size; i++) {
			deptId = departIds[i].trim();
			if (!StringUtil.isNullEmpty(deptId)) {
				po = departmentDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
				if (null != po) {
					list.add(po);
				}
			}
		}*/
		return departmentDAO.getDeptInfo(departIds);
	}
	
	@Override
	public List<String> getDeptFullNames(String[] departIds) throws Exception, BaseException {
		if (departIds == null || departIds.length == 0) {
			return null;
		}
		return departmentDAO.getDeptFullNames(departIds);
	}
	
	@Override
	public List<String> getDeptFullNames(String departIds, String regex) throws Exception, BaseException {
		return this.getDeptFullNames(departIds.split(regex));
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#getDeptPermissionByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getDeptPermissionByUserId(String orgId, String userId) throws Exception, BaseException {
		List<TbDepartmentInfoVO> list = departmentDAO.getDeptPermissionByUserId(userId);
		if (!AssertUtil.isEmpty(list)) {
			Iterator<TbDepartmentInfoVO> iterator = list.iterator();
			while (iterator.hasNext()){
				if (!orgId.equals(iterator.next().getOrgId())) {
					iterator.remove();
				}
			}
		}
		return  list;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#getDeptByWeixin(java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoPO getDeptByWeixin(String orgId, String wxId) throws Exception, BaseException {
		return departmentDAO.getDeptByWeixin(orgId, wxId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#getDeptUserRefByUserId(java.lang.String)
	 */
	@Override
	public List<String> getDeptUserRefByUserId(String userId) throws Exception, BaseException {
		return departmentDAO.getDeptUserRefByUserId(userId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.department.service.IDepartmentService#updateDept(cn.com.do1.component.contact.department.model.TbDepartmentInfoPO,
	 * cn.com.do1.component.contact.contact.vo.UserOrgVO, boolean)
	 */
	@Override
	public List<DeptSyncInfoVO> updateDept(TbDepartmentInfoPO tbDepartmentInfoPO, UserOrgVO org, String isUseAll, String deptIds, String userIds, boolean updateAll) throws Exception, BaseException {
		String orgId =org.getOrgId();
		TbDepartmentInfoPO history = departmentDAO.searchByPk(TbDepartmentInfoPO.class, tbDepartmentInfoPO.getId());
		List<TbDepaSpecificObjPO> addObjList = new ArrayList<TbDepaSpecificObjPO>();
		List<String> deleObjList = new ArrayList<String>();
		if (history == null || !orgId.equals(history.getOrgId())) {
			throw new NonePrintException("3001", "用户出现异常或者部门已被删除");
		}
		List<DeptSyncInfoVO> updateList = new ArrayList<DeptSyncInfoVO>();
		List<TbDepartmentInfoPO> clist;
		if (WxAgentUtil.isTrustAgent(org.getCorpId(), WxAgentUtil.getAddressBookCode())) {
			if (!history.getDepartmentName().equals(tbDepartmentInfoPO.getDepartmentName())) {
				// 验证是否重名（同一部门下不能有同名子部门）
				List<TbDepartmentInfoPO> list = departmentDAO.getDeptByParent(tbDepartmentInfoPO.getOrgId(), tbDepartmentInfoPO.getParentDepart(), tbDepartmentInfoPO.getDepartmentName());
				if (!AssertUtil.isEmpty(list)) {
					throw new NonePrintException("3003", "部门名称重复，请重新输入");
				}
			}
			if (AssertUtil.isEmpty(history.getWxId())) {
				throw new NonePrintException("3004", "部门有误，请联系管理员");
			}
			// 更新此部门下的子部门的部门全称
			clist = departmentDAO.getChildDeptByFullName(history.getOrgId(), history.getDeptFullName() + "->%");
			if (!history.getDepartmentName().equals(tbDepartmentInfoPO.getDepartmentName())) { // 如果部门名称发生改变
				// 修改名称后需要同步到微信
				WxDept dept = new WxDept();
				dept.setId(history.getWxId());
				dept.setName(tbDepartmentInfoPO.getDepartmentName());
				dept.setParentid(history.getWxParentid());
				dept.setOrder(tbDepartmentInfoPO.getShowOrder());
				dept = WxDeptService.updateDept(dept, org.getCorpId(), org.getOrgId());
				history.setWxId(dept.getId());
				history.setWxParentid(dept.getParentid());
				// 组装部门全称（自己的部门名称+父部门的部门全称）
				String newFullName = "";
				if (StringUtil.isNullEmpty(history.getParentDepart())) {
					// parent为空表示此部门为机构下的第一层部门
					newFullName = tbDepartmentInfoPO.getDepartmentName();
				} else {
					TbDepartmentInfoPO parent = departmentDAO.searchByPk(TbDepartmentInfoPO.class, history.getParentDepart());
					if (parent != null) {
						newFullName = parent.getDeptFullName() + "->" + tbDepartmentInfoPO.getDepartmentName();
						DepartmentUtil.setPermission(tbDepartmentInfoPO, parent);
					} else {
						// parent为空表示此部门为机构下的第一层部门
						newFullName = tbDepartmentInfoPO.getDepartmentName();
					}
				}
				if (!AssertUtil.isEmpty(clist)) {
					StringBuffer sb = new StringBuffer();
					for (TbDepartmentInfoPO po : clist) {
						sb.setLength(0);
						sb.append(po.getDeptFullName());
						// 只处理子部门
						po.setDeptFullName(sb.replace(0, history.getDeptFullName().length(), newFullName).toString());
					}
				}
				tbDepartmentInfoPO.setDeptFullName(newFullName);
			}else{// 如果部门名称没改变
				if ( !IntegerUtil.equals(history.getShowOrder(),tbDepartmentInfoPO.getShowOrder()) ) {
					// 修改排序号后需要同步到微信
					WxDept dept = new WxDept();
					dept.setId(history.getWxId());
					dept.setName(tbDepartmentInfoPO.getDepartmentName());
					dept.setParentid(history.getWxParentid());
					dept.setOrder(tbDepartmentInfoPO.getShowOrder());
					dept = WxDeptService.updateDept(dept, org.getCorpId(), org.getOrgId());
					history.setWxId(dept.getId());
					history.setWxParentid(dept.getParentid());
				}
				tbDepartmentInfoPO.setDeptFullName(history.getDeptFullName());
			}
		}
		else {
			clist = departmentDAO.getChildDeptByFullName(history.getOrgId(), history.getDeptFullName() + "->%");
		}
		//更新子部门的权限
		DepartmentUtil.updateChildrenDeptPermission(tbDepartmentInfoPO, history, org, clist, isUseAll, deptIds, userIds, addObjList, deleObjList);
		if(addObjList.size() > 0){
			departmentDAO.execBatchInsert(addObjList);
		}
		if (deleObjList.size() > 0){
			departmentDAO.deleteByPks(TbDepaSpecificObjPO.class, deleObjList);
		}
		if(clist.size() > 0){
			departmentDAO.execBatchUpdate(clist);
		}
		DeptSyncInfoVO vo;
		for(TbDepartmentInfoPO po : clist){
			vo = new DeptSyncInfoVO();
			BeanHelper.copyBeanProperties(vo,po);
			vo.setUpdateParentDepart(false);
			vo.setDepartmentName(po.getDepartmentName());
			updateList.add(vo);
		}
		// 同步修改微信后台
		tbDepartmentInfoPO.setWxId(history.getWxId());
		tbDepartmentInfoPO.setWxParentid(history.getWxParentid());
		tbDepartmentInfoPO.setId(history.getId());
		tbDepartmentInfoPO.setPinyin(history.getPinyin());
		tbDepartmentInfoPO.setOrgId(history.getOrgId());
		tbDepartmentInfoPO.setCreatePerson(history.getCreatePerson());
		tbDepartmentInfoPO.setCreateTime(history.getCreateTime());
		tbDepartmentInfoPO.setParentDepart(history.getParentDepart());
		tbDepartmentInfoPO.setTotalUser(history.getTotalUser());
		this.updatePO(tbDepartmentInfoPO, updateAll);
		if(!StringUtil.strCstr(history.getDepartmentName(),tbDepartmentInfoPO.getDepartmentName())){
			vo = new DeptSyncInfoVO();
			BeanHelper.copyBeanProperties(vo,tbDepartmentInfoPO);
			vo.setUpdateParentDepart(false);
			if(updateList == null){
				updateList = new ArrayList<DeptSyncInfoVO>(1);
			}
			updateList.add(vo);
		}
		return updateList;
	}

	@Override
	public Set<String> getParentDepartIds(String departIds, String orgId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departIds))
			return null;
		String[] pepartIdArray = departIds.split(";");
		Set<String> result = new HashSet<String>();
		TbDepartmentInfoPO departmentInfo = null;
		for (int i = 0; i < pepartIdArray.length; i++) {
			departmentInfo = this.searchByPk(TbDepartmentInfoPO.class, pepartIdArray[i]);
			if (null != departmentInfo) {
				result.add(departmentInfo.getId());
				if (!AssertUtil.isEmpty(departmentInfo.getParentDepart())) {
					// 递归获取父部门
					getParentDepartIds(departmentInfo.getParentDepart(), orgId, result);
				}

			}
		}
		return result;
	}

	private Set<String> getParentDepartIds(String parentId, String orgId, Set<String> result) throws Exception, BaseException {
		TbDepartmentInfoVO departmentInfo = this.departmentDAO.getParentDepart(orgId, parentId);
		if(AssertUtil.isEmpty(departmentInfo)){
			return result;
		}
		result.add(parentId);
		if (null!=departmentInfo&&!AssertUtil.isEmpty(departmentInfo.getParentDepart())) {
			getParentDepartIds(departmentInfo.getParentDepart(), orgId, result);
		}
		return result;
	}

	@Override
	public Set<String> getChildDepartIds(String departIds, String orgId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departIds))
			return null;
		Set<String> result = new HashSet<String>();
		String[] departIdArray = departIds.split("\\|");
		for (int i = 0; i < departIdArray.length; i++) {
			// 递归获取子部门
			getChildDepartIds(departIdArray[i], orgId, result);
		}
		return result;
	}

	private Set<String> getChildDepartIds(String departId, String orgId, Set<String> result) throws Exception, BaseException {
		List<TbDepartmentInfoVO> departmentInfoList = this.departmentDAO.getChildDepart(orgId, departId);
		result.add(departId);
		if (!AssertUtil.isEmpty(departmentInfoList) && departmentInfoList.size() > 0) {
			for (TbDepartmentInfoVO departVO : departmentInfoList) {
				if (null != departVO) {
					getChildDepartIds(departVO.getId(), orgId, result);
				}
			}
		}
		return result;
	}

	@Override
	public Set<String> getChildDepartNames(String departIds, String orgId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departIds))
			return null;
		Set<String> result = new HashSet<String>();
		String[] departIdArray = departIds.split("\\|");
		for (int i = 0; i < departIdArray.length; i++) {
			// 递归获取子部门
			getChildDepartNames(departIdArray[i], orgId, result);
		}
		return result;
	}

	private Set<String> getChildDepartNames(String departId, String orgId, Set<String> result) throws Exception, BaseException {
		TbDepartmentInfoPO departmentPo = this.searchByPk(TbDepartmentInfoPO.class, departId);
		if (!AssertUtil.isEmpty(departmentPo)) {
			result.add(departmentPo.getDeptFullName());
			List<TbDepartmentInfoVO> departmentInfoList = this.departmentDAO.getChildDepart(orgId, departId);
			if (!AssertUtil.isEmpty(departmentInfoList) && departmentInfoList.size() > 0) {
				for (TbDepartmentInfoVO departVO : departmentInfoList) {
					if (null != departVO) {
						getChildDepartNames(departVO.getId(), orgId, result);
					}
				}
			}
		}
		return result;
	}

	@Override
	public TbDepartmentInfoPO getDeptInfoPOByName(String deptName, String orgId) throws Exception, BaseException {
		return departmentDAO.getDeptInfoPOByName(deptName, orgId);
	}

	@Override
	public void updateDeptReceive(TbDepartmentInfoPO deptPO, String userIds, boolean isAdd) throws Exception, BaseException {
		if (!isAdd) { //编辑部门时
			departmentDAO.delDeptReceiveByDeptId(deptPO.getId());// 删除默认负责人
		}
		// 插入默认负责人
		String userIdArray[] = userIds.split("\\|");
		UserRedundancyInfoVO userRedundancyInfoVO;
		TbQyUserReceivePO po;
		String orgId="";
		for (String userId : userIdArray) {
			userRedundancyInfoVO = this.contactService.getUserRedundancyInfoByUserId(userId);
			if (null != userRedundancyInfoVO) {
				po = new TbQyUserReceivePO();
				po.setId(UUID.randomUUID().toString());
				po.setUserId(userRedundancyInfoVO.getUserId());
				po.setDepartmentId(deptPO.getId());
				po.setDepartmentName(deptPO.getDepartmentName());
				po.setPersonName(userRedundancyInfoVO.getPersonName());
				po.setHeadPic(userRedundancyInfoVO.getHeadPic());
				po.setWxUserId(userRedundancyInfoVO.getWxUserId());
				po.setOrgId(deptPO.getOrgId());
				po.setCreateTime(new Date());
				this.insertPO(po, false);
				//删除人资缓存的可管理部门
				orgId=deptPO.getOrgId();
				CacheWxqyhObject.remove(WxAgentUtil.getHrmanagementCode(),deptPO.getOrgId(),userId+"_deptRef");
				CacheWxqyhObject.remove(WxAgentUtil.getHrmanagementCode(),orgId,userId+"_firstDeptRef");
			}
		}
		CacheWxqyhObject.remove(WxAgentUtil.getHrmanagementCode(),orgId,orgId+"_rule");
	}

	@Override
	public List<TbQyUserInfoVO> getDeptReceiveList(String deptId, String orgId)

	throws Exception, BaseException {
		return departmentDAO.getDeptReceiveList(deptId);
		// 检查信息并返回
		//return getCheckDeptReceiveList(list, orgId, deptId);
	}

	@Override
	public List<TbQyUserInfoVO> getCheckDeptReceiveList(List<TbQyUserInfoVO> list, String orgId, String deptId) throws Exception, BaseException {
		List<TbQyUserInfoVO> deptReceiveList = new ArrayList<TbQyUserInfoVO>();
		// 获取部门的成员，不包括子部门
//		List<TbQyUserInfoVO> deptUserInfoVOList = contactService.getUsersByDepartId(orgId, deptId);
//		Map<String, String> deptUserInfoMap = new HashMap<String, String>();
//		// list转map：为了判断不需要循环多次
//		for (TbQyUserInfoVO userInfoVO : deptUserInfoVOList) {
//			if (AssertUtil.isEmpty(deptUserInfoMap.get(userInfoVO.getUserId()))) {
//				deptUserInfoMap.put(userInfoVO.getUserId(), userInfoVO.getUserId());
//			}
//		}
		// 判断人员是否合法
		for (TbQyUserInfoVO userInfoVO : list) {
			// 离职用户不加载
			if ("-1".equals(userInfoVO.getUserStatus())) {
				departmentDAO.delDeptReceiveByDeptIdAndUserId(deptId, userInfoVO.getUserId());// 随带删除不合法的数据
				continue;
			}
//			// 判断人员是否还在部门中
//			if (AssertUtil.isEmpty(deptUserInfoMap.get(userInfoVO.getUserId()))) {
//				departmentDAO.delDeptReceiveByDeptIdAndUserId(deptId, userInfoVO.getUserId());// 随带删除不合法的数据
//				continue;
//			}
			deptReceiveList.add(userInfoVO);
		}
		return deptReceiveList;
	}

	@Override
	public List<TbQyUserInfoVO> getAllDeptReceiveList(String orgId)
			throws Exception, BaseException {
		return departmentDAO.getAllDeptReceiveList(orgId);
	}

	@Override
	public void importDeptToUserExcel(File file, String fileName,
			UserOrgVO userOrgVO, String id) throws Exception, BaseException {
		List<ImportDeptToUserVO> list = DeptToUserExcelUtil.importForExcel(file, fileName,id);//获取excel
		//先删除原来的导入部门负责人记录
		departmentDAO.delDeptReceiveImportLog(userOrgVO.getOrgId(),1);

		ResultVO resultvo= ImportResultUtil.getResultObject(id);
		if (list != null && !list.isEmpty()) {
			resultvo.setTotalNum(list.size());
			String result="操作成功!";

			boolean isAllUserUsable = true;
			Map<String, TbDepartmentInfoPO> userUsableDeptMap = null;
			if(!WxAgentUtil.isAllUserUsable(userOrgVO.getCorpId(), WxAgentUtil.getAddressBookCode())){
				userUsableDeptMap = CheckDeptVisible.getDeptVisibleMap(userOrgVO.getCorpId(),userOrgVO.getOrgId(),WxAgentUtil.getAddressBookCode());
				isAllUserUsable = false;
			}
			//StringBuilder deptIds=new StringBuilder();
			List<TbQyUserReceivePO> userReceiveList=new ArrayList<TbQyUserReceivePO>();
			TbQyDeptReceiveImportLogPO importLogPO;
			List<String> errorlist;
			int processNum=0;
			deptLoop:for(ImportDeptToUserVO vo:list){
				processNum=processNum+1;
				resultvo.setProcessNum(processNum);
				//创建导入记录
				importLogPO=new TbQyDeptReceiveImportLogPO();
				importLogPO.setCreaotr(userOrgVO.getUserName());
				importLogPO.setCreateTime(new Date());
				importLogPO.setOrgId(userOrgVO.getOrgId());
				importLogPO.setDeptId(vo.getDepartmentId());
				importLogPO.setDeptName(vo.getDepartmentName());
				importLogPO.setDeptFullName(vo.getDeptFullName());
				importLogPO.setPersonNames(vo.getPersonNames());
				importLogPO.setWxUserIds(vo.getWxUserIds());
				importLogPO.setType("1");//表明是导入部门负责人

				if(!isAllUserUsable){
					if(StringUtil.isNullEmpty(vo.getDeptFullName())){
						//记录错误信息：部门全称为空
						importLogPO.setStatus(0);
						importLogPO.setMsg("部门全称为空");
						this.insertPO(importLogPO, true);

						//记录进度
						errorlist = resultvo.getErrorlist();
						if(errorlist == null){
							errorlist = new ArrayList<String>();
						}
						errorlist.add(vo.getDepartmentId());//插入错误行的id
						resultvo.setErrorlist(errorlist);

						continue deptLoop;
					}
					String fistDeptName = vo.getDeptFullName().split("->")[0];
					if(!userUsableDeptMap.containsKey(fistDeptName)){
						//记录错误信息，无权限
						importLogPO.setStatus(0);
						importLogPO.setMsg("你的通讯录应用未开放完整通讯录权限，无法管理此部门，请在企业微信后台->【应用中心】->【授权的应用】中找到【通讯录】应用->修改【通讯录范围】为最顶级部门。");
						this.insertPO(importLogPO, true);

						//记录进度
						errorlist = resultvo.getErrorlist();
						if(errorlist == null){
							errorlist = new ArrayList<String>();
						}
						errorlist.add(vo.getDepartmentId());//插入错误行的id
						resultvo.setErrorlist(errorlist);

						continue deptLoop;
					}
				}

				//判断部门的合法性
				TbDepartmentInfoPO deptPO=departmentDAO.searchByPk(TbDepartmentInfoPO.class,vo.getDepartmentId());
				if(deptPO==null){
					//记录错误信息：部门ID有误
					importLogPO.setStatus(0);
					importLogPO.setMsg("部门ID有误");
					this.insertPO(importLogPO, true);

					//记录进度
					errorlist = resultvo.getErrorlist();
					if(errorlist == null){
						errorlist = new ArrayList<String>();
					}
					errorlist.add(vo.getDepartmentId());//插入错误行的id
					resultvo.setErrorlist(errorlist);

					continue deptLoop;
				}

				/**
				 * 1、部门负责人为空时，删除部门负责人
				 * 2、部门负责人不为空时，先删除部门负责人，再插入负责人
				 */
				if(!AssertUtil.isEmpty(vo.getWxUserIds())){
					String wxUserIds[]=vo.getWxUserIds().split("\\|");
					TbQyUserReceivePO userReceivePO=null;
					Map<String,String> wxUserIdMap=new HashMap<String,String>();
					userLoop:for(String wxUserId:wxUserIds){
						//记录判断是否有重复的wxUserId
						if(AssertUtil.isEmpty(wxUserIdMap.get(wxUserId))){
							wxUserIdMap.put(wxUserId, wxUserId);
						}else{
							continue userLoop;
						}
						TbQyUserInfoVO userInfoVO=contactService.findUserInfoByWxUserId(wxUserId, userOrgVO.getCorpId());
						if(userInfoVO==null){
							//记录错误信息:用户账号有误,
							importLogPO.setStatus(0);
							importLogPO.setMsg("用户账号有误");
							this.insertPO(importLogPO, true);

							//记录进度
							errorlist = resultvo.getErrorlist();
							if(errorlist == null){
								errorlist = new ArrayList<String>();
							}
							errorlist.add(vo.getDepartmentId());//插入错误行的id
							resultvo.setErrorlist(errorlist);

							continue deptLoop;
						}
						if("-1".equals(userInfoVO.getUserStatus())){
							//记录错误信息，用户已离职
							importLogPO.setStatus(0);
							importLogPO.setMsg("用户已离职");
							this.insertPO(importLogPO, true);

							//记录进度
							errorlist = resultvo.getErrorlist();
							if(errorlist == null){
								errorlist = new ArrayList<String>();
							}
							errorlist.add(vo.getDepartmentId());//插入错误行的id
							resultvo.setErrorlist(errorlist);

							continue deptLoop;
						}
						userReceivePO=new TbQyUserReceivePO();
						userReceivePO.setId(UUID.randomUUID().toString());
						userReceivePO.setUserId(userInfoVO.getUserId());
						userReceivePO.setDepartmentId(deptPO.getId());
						userReceivePO.setDepartmentName(deptPO.getDepartmentName());
						userReceivePO.setPersonName(userInfoVO.getPersonName());
						userReceivePO.setHeadPic(userInfoVO.getHeadPic());
						userReceivePO.setWxUserId(userInfoVO.getWxUserId());
						userReceivePO.setOrgId(userInfoVO.getOrgId());
						userReceivePO.setCreateTime(new Date());
						userReceiveList.add(userReceivePO);
					}
				}
				importLogPO.setStatus(1);
				importLogPO.setMsg("插入成功!");
				this.insertPO(importLogPO, true);

				//删除需要修改的部门负责人
				departmentDAO.delDeptReceiveByDeptId(deptPO.getId());
				//deptIds.append("|"+deptPO.getId());//记录部门，批量操作
			}

			//批量操作
/*			if(deptIds.length()>0){
				deptIds.deleteCharAt(0);//去掉前面的"|"
				this.batchDel(TbQyUserReceivePO.class, deptIds.toString().split("\\|"));//批量删除部门负责人
			}*/

			if(userReceiveList!=null && userReceiveList.size()>0){
				departmentDAO.execBatchInsert(userReceiveList);
			}
			//记录批量导入部门直接负责人
			contactService.insertOperationLog(userOrgVO.getUserName(), userOrgVO.getPersonName(), "批量插入部门负责人", "edit", "addressBook", userOrgVO.getOrgId(), result);
		}
	}

	@Override
	public List<ImportDeptToUserVO> getDeptReceiveImportLogList(
			Map<String, Object> params) throws Exception, BaseException {
		return this.departmentDAO.getDeptReceiveImportLogList(params);
	}

	@Override
	public List<TbDepartmentInfoPO> getAllDepartOrderByDeptName(String orgId)
			throws Exception, BaseException {
		return this.departmentDAO.getAllDepartOrderByDeptName(orgId);
	}

	@Override
	public List<TbDepartmentInfoPO> getExistAuditUserDepartmentInfoByDpids(String departmentIds,String terminationDeptIds) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departmentIds))
			return null;
		String[] departmentIdArry = departmentIds.split(";");
		List<TbDepartmentInfoPO> departments = new ArrayList<TbDepartmentInfoPO>();
		TbDepartmentInfoPO dp = new TbDepartmentInfoPO();
		Set<String> departmentIdList=new HashSet<String>();//防止存在重复部门，例如当前人父子部门都存在，子部门没有负责人时会有重复部门的情况
		for (int i = 0; i < departmentIdArry.length; i++) {
			dp = getParentDepartmentReceiveUser(departmentIdArry[i],null,terminationDeptIds);
			if (null != dp &&departmentIdList.add(dp.getId())) {
				departments.add(dp);
			}
		}
		return departments;
	}
	/****
	 * 获取存在直接负责人的部门信息，直到找到，或者直到顶级为止  by tanwq  2015-8-20
	 * @param departmentId 部门id
	 * @param terminationDeptIds 终于部门ids
	 * @param userInfoList 部门负责人信息
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	private TbDepartmentInfoPO getParentDepartmentReceiveUser(String departmentId, List<TbQyUserInfoVO> userInfoList,String terminationDeptIds)throws Exception, BaseException {
		List<TbQyUserInfoVO> userList=contactService.findUserInfoPOByDepartmentId(departmentId);//获取当前部门的直接负责人
		TbDepartmentInfoPO departmentInfo=null;
		if(AssertUtil.isEmpty(userList)){//如果没有找到该部门的直接负责人并且（不存在有终止部门或者还没达到终止部门）的情况下则查找上级
			if(!AssertUtil.isEmpty(terminationDeptIds)&&("|"+terminationDeptIds+"|").indexOf(departmentId)>-1){
				return null;
			}
			departmentInfo=new TbDepartmentInfoPO();
			departmentInfo = this.searchByPk(TbDepartmentInfoPO.class,departmentId);
			if(!AssertUtil.isEmpty(departmentInfo)){
				departmentInfo=getParentDepartmentReceiveUser(departmentInfo.getParentDepart(),userInfoList,terminationDeptIds);
			}
		}else{
			if(null!=userInfoList){//获取到用户信息
				userInfoList.addAll(userList);
			}
			departmentInfo=new TbDepartmentInfoPO();
			departmentInfo = this.searchByPk(TbDepartmentInfoPO.class,departmentId);
		}
		return departmentInfo;
	}
	
	public List<DepTotalUserVO> getDepTotalUserByOrgId(String orgId)throws Exception, BaseException{
		return departmentDAO.getDepTotalUserByOrgId(orgId);
	}

	@Override
	public Integer getDepTotalUser(String orgId,
			String deptFullName) throws Exception, BaseException {
		return departmentReadOnlyDAO.getDepTotalUser(orgId, deptFullName);
	}
	
	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.service.IDepartmentMgrService#getDepartmentInfoByName(java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoPO getDepartmentInfoByName(String deptName, String orgId) throws Exception, BaseException {
		return departmentDAO.getDepartmentInfoByName(deptName, orgId);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.service.IDepartmentMgrService#getDepartmentVOByName(java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoVO getDepartmentVOByName(String deptName, String orgId) throws Exception, BaseException {
		return departmentDAO.getDepartmentVOByName(deptName, orgId);
	}

	/**
	 * 根据部门ids获取部门信息
	 *
	 * @param deptIds
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-27
	 */
	@Override
	public List<TbDepartmentInfoPO> getDeptsByIds(List<String> deptIds) throws Exception {
		return departmentDAO.getDeptsByIds(deptIds);
	}

	@Override
	public List<TbDepartmentInfoVO> getDeptVOsByIds(List<String> deptIds) throws Exception {
		if(deptIds == null || deptIds.size()==0){
			return new ArrayList<TbDepartmentInfoVO>(0);
		}
		return departmentDAO.getDeptVOsByIds(deptIds);
	}

	/**
	 * 获取用户的部门信息
	 * @param orgId
	 * @param userId
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-3
	 */
	@Override
	public List<TbDepartmentInfoVO> getDeptInfoByUserId(String orgId, String userId) throws SQLException {
		List<TbDepartmentInfoVO> list = departmentDAO.getDeptInfoByUserId(orgId, userId);
		return getRemoveTempDeptVO(list);
	}


	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.service.IDepartmentMgrService#searchDepartment(java.util.Map)
	 */
	@Override
	public TbDepartmentInfoPO searchDepartment(Map map) throws Exception, BaseException {

		return departmentDAO.searchDepartment(map);
	}

	@Override
	public Pager searchPagerDept(Map searchValue, Pager pager)throws Exception, BaseException {
		return departmentDAO.searchPagerDept(searchValue, pager);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.service.IDepartmentMgrService#getlistDeptNodeByOrgid(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyOrganizeInfo> getlistDeptNodeByOrgid(String orgId, String nodeId)
			throws Exception, BaseException {
		if(StringUtil.isNullEmpty(nodeId)){
			return getRemoveTempDeptOrg(departmentDAO.listDeptNodeByOrgid(orgId));
		}
		else{
			TbDepartmentInfoPO departmentPo = this.searchByPk(TbDepartmentInfoPO.class, nodeId);
			return getRemoveTempDeptOrg(departmentDAO.getlistDeptNodeByFullName(orgId, departmentPo.getDeptFullName() + "->%"));
		}
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.service.IDepartmentMgrService#getListDeptNodeByDeparts(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<TbQyOrganizeInfo> getListDeptNodeByDeparts(String orgId,
			String nodeId, List<TbDepartmentInfoPO> deptList) throws Exception,
			BaseException {
		if(deptList==null || deptList.size()==0){
			return new ArrayList<TbQyOrganizeInfo>(0);
		}
		List<TbQyOrganizeInfo> list;
		if(StringUtil.isNullEmpty(nodeId)){
			list = departmentDAO.listDeptNodeByDeparts(orgId, deptList);
		}
		else{
			TbDepartmentInfoPO departmentPo = this.searchByPk(TbDepartmentInfoPO.class, nodeId);
			list = departmentDAO.getlistDeptNodeByDepartsAndFullName(orgId, departmentPo.getDeptFullName() + "->%", deptList);
		}
		//清掉部门名称为空的部门信息这个部门
		getRemoveTempDeptOrg(list);
		return list;
	}

	@Override
	public void getFirstDepartAndUser(String deptId, UserInfoVO user, String agentCode, Map<String, Object> params, List<TbDepartmentInfoVO> deptlist, Pager pager) throws Exception, BaseException {
		String orgId = user.getOrgId();
		if(!StringUtil.isNullEmpty(deptId) && !"0".equals(deptId)){
			TbDepartmentInfoPO deptPO = departmentDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
			//如果不是是首页
			if(deptPO!=null && user.getOrgId().equals(deptPO.getOrgId())){
				List<TbDepartmentInfoVO> deptList = this.getChildDepart(orgId,deptId);
				if (!AssertUtil.isEmpty(deptList)) {
					deptlist.addAll(deptList);
				}
				//获取本部门的人员数据
				if(pager != null) {
					pager = contactMgrService.getUsersByDepartIdToPager(deptId, params, pager);
				}
				/*if(personlist!=null){
					List<TbQyUserInfoVO> userList = contactMgrService.getUsersByDepartId(deptId,params);
					if (!AssertUtil.isEmpty(userList)) {
						personlist.addAll(userList);
					}
				}*/
			}
			return;
    	}
		//人员通讯录添加限制 chenfeixiong 2014/08/12	1为全公司 2为一级部门 3为本部门
		List<TbDepartmentInfoVO> depts = this.getDeptPermissionByUserId(orgId,user.getUserId());
		if(depts == null || depts.size()==0){
			return;
		}
		List<String> userIds = new ArrayList<String>();
		//设置特定部门的权限
		depts = DepartmentUtil.setSpecificDeptPermission(depts, userIds, user);
		if(userIds.size() > 0 && null != pager){
			if(AssertUtil.isEmpty(pager.getPageData())){
				List<TbQyUserInfoVO> personList = new ArrayList<TbQyUserInfoVO>();
				personList.addAll(contactService.getAllUserInfoByIds(ListUtil.toArrays(userIds)));
				pager.setPageData(personList);
			}else{
				pager.getPageData().addAll(contactService.getAllUserInfoByIds(ListUtil.toArrays(userIds)));
			}
		}
		//根据可见范围重置用户的部门信息
		getUsableDept(user,agentCode,depts);
		//如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if(depts==null || depts.size()==0){
			return;
		}
		//如果部门权限为空，或者第一个部门的信息的权限就是全公司
		if(StringUtil.isNullEmpty(depts.get(0).getPermission()) || "1".equals(depts.get(0).getPermission())){
			List<TbDepartmentInfoVO> deptList = this.getFirstDepart(orgId);
			if (!AssertUtil.isEmpty(deptList)) {
				deptlist.addAll(deptList);
			}
			return;
		}
		//单部门
		else if(depts.size()==1){
			if(DepartmentDictUtil.PERMISSION_OWN.equals(depts.get(0).getPermission())){
				//获取一级部门
				String[] spil;
				spil = depts.get(0).getDeptFullName().split("->");
				TbDepartmentInfoVO vo = this.getDepartmentVOByName(spil[0], orgId);
				if(AssertUtil.isEmpty(vo)){
					return;
				}
				deptlist.add(vo);
				return ;
			}
			if(DepartmentDictUtil.PERMISSION_CHILD.equals(depts.get(0).getPermission())){
				//为了让页面返回上一步到最顶层的时候可以正常显示所有的部门信息
				depts.get(0).setParentDepart("");
				deptlist.add(depts.get(0));
				return ;
			}
		}
		else{//多部门
			Set<String> set = new HashSet<String>();
			String[] spil;
			for (TbDepartmentInfoVO dept : depts) {
				//1为全公司 2为一级部门 3为本部门
				if(DepartmentDictUtil.PERMISSION_OWN.equals(dept.getPermission())){
					//获取一级部门
					spil = dept.getDeptFullName().split("->");
					TbDepartmentInfoVO vo = this.getDepartmentVOByName(spil[0], orgId);
					if(set.add(vo.getId())){
						deptlist.add(vo);
					}
				}
				else if(DepartmentDictUtil.PERMISSION_CHILD.equals(dept.getPermission())){
					if(set.add(dept.getId())){
				    	//为了让页面返回上一步到最顶层的时候可以正常显示所有的部门信息
						dept.setParentDepart("");
						deptlist.add(dept);
					}
				}
			}
		}
	}

	@Override
	public List<TbDepartmentInfoVO> getChildDepart(String deptId, UserInfoVO user, String agentCode) throws Exception, BaseException {
		List<TbDepartmentInfoVO> deptlist = new ArrayList<TbDepartmentInfoVO>(10);
		this.getFirstDepartAndUser(deptId,user,agentCode,null,deptlist,null);
		return deptlist;
	}

	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefList(List<String> userIds) throws Exception {
		if(userIds == null || userIds.size() == 0){
			return new ArrayList<TbQyUserDepartmentRefPO>(0);
		}
		return departmentReadOnlyDAO.getDeptUserRefList(userIds);
	}

	/**
	 * 根据可见范围重置用户的部门信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-4
	 */
	private void getUsableDept(UserInfoVO user,String agentCode,List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		if(StringUtil.isNullEmpty(agentCode)){
			return;
		}
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(user.getCorpId(),agentCode);
		if(aci == null || !aci.isTrust()){
			throw new NonePrintException("105","尊敬的用户，暂时还未托管此应用，请联系管理员！");
		}
		if(aci.isAllUserVisible()){//全公司可见
			return;
		}
		//用户在可见范围内
		if(!VisibleRangeUtil.isUserVisibleAgentByParts(user.getUserId(),user.getDeptFullNames(),agentCode,user.getOrgId(),aci.getPartys())){
			throw new NonePrintException("105", "尊敬的用户，你不在应用的可见范围，请联系管理员！");
		}
		if(aci.isAllUserUsable()){//可操作全公司
			return;
		}
		//不是全公司可见时
		List<TbDepartmentInfoPO> usable = null;
		if(!AssertUtil.isEmpty(aci.getPartys())){
			usable = this.getDeptByWxIds(user.getOrgId(), aci.getPartys().substring(1,aci.getPartys().length()-1).split("\\|"));
		}
		if(usable == null || usable.size()==0){
			depts.clear();
			return;
		}
		int usableSize = usable.size();
		Map<String,TbDepartmentInfoPO> map = new HashMap<String, TbDepartmentInfoPO>(usableSize);
		String[] deptNames = new String[usableSize];
		TbDepartmentInfoPO po;
		for(int i=0;i<usableSize;i++){
			po = usable.get(i);
			map.put(po.getDeptFullName(),po);
			deptNames[i] = po.getDeptFullName();
		}
		Set<TbDepartmentInfoVO> set = new HashSet<TbDepartmentInfoVO>(depts);
		String[] spil;
		goThis : for(TbDepartmentInfoVO vo:set){
			//如果是仅子部门可见
			if(DepartmentDictUtil.PERMISSION_CHILD.equals(vo.getPermission())){
				if(map.containsKey(vo.getDeptFullName())){//如果可见范围内有此部门
					continue;
				}
				spil = vo.getDeptFullName().split("->");
				StringBuilder sb = new StringBuilder();
				for(String s : spil){
					if(sb.length()>0){
						sb.append("->");
					}
					sb.append(s);
					if(map.containsKey(sb.toString())){
						continue goThis;//跳到最外层for循环
					}
				}
				//如果在可见范围内未找到此部门，直接去掉此部门
				depts.remove(vo);
				String fullName = sb.append("->").toString();
				for(String deptFullName:deptNames){
					//寻找可见范围包含在用户部门下的部门信息
					if(deptFullName.startsWith(fullName)){
						TbDepartmentInfoVO deptVO = new TbDepartmentInfoVO();
						BeanHelper.copyBeanProperties(deptVO,map.get(deptFullName));
						//将部门权限设置为仅子部门
						deptVO.setPermission(DepartmentDictUtil.PERMISSION_CHILD);
						depts.add(deptVO);
					}
				}
			}//如果权限是本部门
			else if(DepartmentDictUtil.PERMISSION_OWN.equals(vo.getPermission())){
				spil = vo.getDeptFullName().split("->");
				if(map.containsKey(spil[0])){
					continue;
				}
				//如果在可见范围内未找到顶级部门，直接去掉此部门
				depts.remove(vo);
				String fullName = spil[0]+"->";
				for(String deptFullName:deptNames){
					//寻找可见范围包含在用户顶级部门下的部门信息
					if(deptFullName.startsWith(fullName)){
						TbDepartmentInfoVO deptVO = new TbDepartmentInfoVO();
						BeanHelper.copyBeanProperties(deptVO,map.get(deptFullName));
						//将部门权限设置为仅子部门
						deptVO.setPermission(DepartmentDictUtil.PERMISSION_CHILD);
						depts.add(deptVO);
					}
				}
			}
			else{
				//先判断用户所在的部门是否在可见范围内，在可见范围内显示全部，否则全部不显示
				if(map.containsKey(vo.getDeptFullName())){//如果可见范围内有此部门
					getAllUsable(depts,usable);
					break;
				}
				//如果用户在应用的可见范围内
				Set<String> userSet = VisibleRangeUtil.getVisibleRangeUserList(user.getOrgId(), agentCode);
				if(userSet != null && userSet.contains(user.getUserId())){
					getAllUsable(depts,usable);
					break;
				}
				spil = vo.getDeptFullName().split("->");
				StringBuilder sb = new StringBuilder();
				for(String s : spil){
					if(sb.length()>0){
						sb.append("->");
					}
					sb.append(s);
					if(map.containsKey(sb.toString())){
						getAllUsable(depts,usable);
						break;
					}
				}
				//如果该部门不在可见范围内，删掉
				depts.remove(vo);
			}
		}
	}

	/**
	 * 获取用户可见部门信息
	 * @param depts
	 * @param usable
     */
	private void getAllUsable(List<TbDepartmentInfoVO> depts, List<TbDepartmentInfoPO> usable){
		depts.clear();
		for(TbDepartmentInfoPO deptPO:usable){
			TbDepartmentInfoVO deptVO = new TbDepartmentInfoVO();
			BeanHelper.copyBeanProperties(deptVO,deptPO);
			//将部门权限设置为仅子部门
			deptVO.setPermission(DepartmentDictUtil.PERMISSION_CHILD);
			depts.add(deptVO);
		}
	}

	@Override
	public List<TbDepartmentInfoPO> getDeptByWxIds(String orgId, String[] wxIds) throws Exception {
		if(wxIds==null || wxIds.length==0){
			return new ArrayList<TbDepartmentInfoPO>(0);
		}
		return departmentDAO.getDeptByWxIds(orgId,wxIds);
	}

	@Override
	public List<TbDepartmentInfoVO> getDeptVOByWxIds(String orgId, String[] wxIds) throws Exception {
		List<TbDepartmentInfoPO> list = departmentDAO.getDeptByWxIds(orgId,wxIds);
		List<TbDepartmentInfoVO> listVo;
		if(list != null && list.size()>0){
			listVo = new ArrayList<TbDepartmentInfoVO>(list.size());
			TbDepartmentInfoVO vo;
			for(TbDepartmentInfoPO po : list){
				vo = new TbDepartmentInfoVO();
				BeanHelper.copyBeanProperties(vo,po);
				listVo.add(vo);
			}
		}
		else{
			listVo = new ArrayList<TbDepartmentInfoVO>(0);
		}
		return listVo;
	}

	@Override
	public List<TbDepartmentInfoVO> getDeptVOByWxIds(String orgId, List<String> wxIds) throws Exception {
		if(wxIds==null || wxIds.size()==0){
			return new ArrayList<TbDepartmentInfoVO>(0);
		}
		return departmentDAO.getDeptByWxIds(orgId,wxIds);
	}

	@Override
	public List<TbDepartmentInfoPO> delDeptAndChild(UserOrgVO user, String id) throws Exception, BaseException {
		TbDepartmentInfoPO po = departmentDAO.searchByPk(TbDepartmentInfoPO.class, id);
		if(po == null || !po.getOrgId().equals(user.getOrgId())){
			throw new NonePrintException("1001", "请刷新后再试");
		}

		List<TbDepartmentInfoPO> list = getChildDeptByFullName(user.getOrgId(), po.getDeptFullName()+"->%");
		Set<String> set = new HashSet<String>(10);
		set.add(id);
		if (!AssertUtil.isEmpty(list)) {
			for (TbDepartmentInfoPO child : list) {
				set.add(child.getId());
			}
		}
		if(contactService.hasUsersByDepartIds(new ArrayList(set))){
			throw new NonePrintException("1002", "部门内没有人员才可删除");
		}
		List<TbDepartmentInfoPO> returnList = new ArrayList<TbDepartmentInfoPO>();
		String[] deptIds;
		if(list == null || list.size()==0){
			deptIds = new String[1];
			deptIds[0] = id;
			returnList.add(po);
		}
		else{
			deptIds = new String[list.size()+1];
			TbDepartmentInfoPO dept;
			for(int i=0; i<list.size(); i++){
				dept = list.get(i);
				deptIds[i] = dept.getId();
			}
			deptIds[list.size()]=id;
			returnList.addAll(list);
			returnList.add(po);
		}
		//删除微信部门
		delWeixinDept(user, po, list);
		super.batchDel(TbDepartmentInfoPO.class, deptIds);
		contactService.insertDel(user.getUserName(), user.getUserName(), "删除部门："+po.getDeptFullName()+"WxId"+po.getWxId(), "addressBook", user.getOrgId());
		//同步删除微信后台部门
		return returnList;
	}

	@Override
	public List<String> getAllDepartToIds(String orgId) throws Exception, BaseException {
		return departmentReadOnlyDAO.getAllDepartToIds(orgId);
	}

	/**
	 * 删除微信上的部门以及子部门
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-6
	 */
	private void delWeixinDept(UserOrgVO user,TbDepartmentInfoPO po,List<TbDepartmentInfoPO> list) throws Exception, BaseException {
		if(list == null || list.size()==0){
			WxDeptService.delDept(po.getWxId(),user.getCorpId(),user.getOrgId());
			return;
		}
		//标注部门下的子部门个数
		Map<String,Integer> map = new HashMap<String, Integer>(list.size());
		for(TbDepartmentInfoPO dept : list){
			if(map.containsKey(dept.getParentDepart())){
				map.put(dept.getParentDepart(),map.get(dept.getParentDepart())+1);
			}
			else{
				map.put(dept.getParentDepart(),1);
			}
		}
		int i = 0;
		int size = list.size();
		TbDepartmentInfoPO dept;
		while(true){
			Iterator<TbDepartmentInfoPO> itItem = list.iterator();
			deptFor:while(itItem.hasNext()){
				dept = itItem.next();
				if(map.containsKey(dept.getId())){
					continue deptFor;
				}
				WxDeptService.delDept(dept.getWxId(),user.getCorpId(),user.getOrgId());
				if(map.containsKey(dept.getParentDepart())){
					int time = map.get(dept.getParentDepart());
					if(time>1){
						map.put(dept.getParentDepart(),time-1);
					}
					else{
						map.remove(dept.getParentDepart());
					}
				}
				itItem.remove();
			}
			if(list.size()==0){
				break;
			}
			i++;
			if(size<i){//如果循环的次数超过了部门总数
				throw new BaseException("1003", "删除部门出现异常，可以先同步通讯录后再试，如仍无法解决，请联系管理人员");
			}
		}
		WxDeptService.delDept(po.getWxId(),user.getCorpId(),user.getOrgId());
	}

	/** 删除部门名称为空的部门，这些部门仅是临时部门
	 * @parm
	 * @author Sun Qinghai
	 * @Date 16-3-21 11:48
	 * @version 1.0
	 */
	private List<TbQyOrganizeInfo> getRemoveTempDeptOrg(List<TbQyOrganizeInfo> list) {
		//清掉部门名称为空的部门信息这个部门
		if (list.size() > 0) {
			int i = 0;
			while (i < list.size()) {
				if (UserListSyncThread.EMPTY_DEPT_NAME.equals(list.get(i).getNodeName())) {
					list.remove(i);
				} else {
					i++;
				}
			}
		}
		return list;
	}
	/**
	 * 删除部门名称为空的部门，这些部门仅是临时部门
	 * @author Sun Qinghai
	 * @Date 16-3-24 19:23
	 * @version 1.0
	 */
	private List<TbDepartmentInfoPO> getRemoveTempDeptPO(List<TbDepartmentInfoPO> list){
		//清掉部门名称为空的部门信息这个部门
		if(list.size()>0){
			int i = 0;
			while(i<list.size()){
				if(UserListSyncThread.EMPTY_DEPT_NAME.equals(list.get(i).getDepartmentName())){
					list.remove(i);
				}
				else{
					i++;
				}
			}
		}
		return list;
	}
	/**
	 * 删除部门名称为空的部门，这些部门仅是临时部门
	 * @parm
	 * @author Sun Qinghai
	 * @Date 16-3-24 19:19
	 * @version 1.0
	 */
	private List<TbDepartmentInfoVO> getRemoveTempDeptVO(List<TbDepartmentInfoVO> list) {
		//清掉部门名称为空的部门信息这个部门
		if(list.size()>0){
			int i = 0;
			while(i<list.size()){
				if(UserListSyncThread.EMPTY_DEPT_NAME.equals(list.get(i).getDepartmentName())){
					list.remove(i);
				}
				else{
					i++;
				}
			}
		}
		return list;
	}

	@Override
	public List<String> getChildDeptIdsByFullName(String orgId, List<String> deptFullName) throws Exception {
		return departmentReadOnlyDAO.getChildDeptIdsByFullName(orgId,deptFullName);
	}

	@Override
	public int getUserCountByDeptIds(List<String> deptIds) throws Exception {
		if(deptIds==null || deptIds.size()==0){
			return 0;
		}
		return departmentReadOnlyDAO.getUserCountByDeptIds(deptIds);
	}

	@Override
	public int getUserCountByDeptIdsAndUserId(List<String> deptIdList, String[] userIds) throws Exception {
		if(deptIdList==null || deptIdList.size()==0){
			return 0;
		}
		if(userIds==null || userIds.length==0){
			return 0;
		}
		return departmentReadOnlyDAO.getUserCountByDeptIdsAndUserId(deptIdList,userIds);
	}

	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefPOByUserId(String userId) throws SQLException {
		return departmentDAO.getDeptUserRefPOByUserId(userId);
	}

	@Override
	public List<String> getDeptUserRefByDeptId(String id) throws SQLException {
		return departmentReadOnlyDAO.getDeptUserRefByDeptId(id);
	}

	@Override
	public List<String> getDeptUserRefByDeptIds(List<String> ids) throws SQLException {
		return departmentReadOnlyDAO.getDeptUserRefByDeptIds(ids);
	}

	/**
	 * 根据部门id返回部门信息
	 * @param departIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 *  @2016-9-23
	 * @version 1.0
	 */
	public List<InterfaceDept> getDepaByDepaId(List<String> departIds, String orgId) throws Exception, BaseException{
		return departmentReadOnlyDAO.getDepaByDepaId(departIds,orgId);
	}

	/**
	 * 根据orgId返回该机构的全部部门
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-17
	 */
	public List<InterfaceDept> getDepaByOrgId(String orgId)throws BaseException, Exception{
		return departmentReadOnlyDAO.getDepaByOrgId(orgId);
	}

	@Override
	public int getCountUserAgentRef(String orgId) throws SQLException {
		return departmentReadOnlyDAO.getCountUserAgentRef(orgId);
	}

	/**
	 *返回教育版所有的教学班级部门
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-12-26
	 */
	public List<TbDepartmentInfoVO> getAllEduDept(String orgId) throws BaseException, Exception{
		return departmentReadOnlyDAO.getAllEduDept(orgId);
	}

	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefPOByDeptIds(List<String> deptIds) throws SQLException {
		return departmentReadOnlyDAO.getDeptUserRefPOByDeptIds(deptIds);
	}

	@Override
	public Set<String> getAllChildDepartIds(String[] departIds, String orgId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departIds)){
			return null;
		}
		List<String> list = getDeptFullNames(departIds);
		Set<String> result = null;
		List<String> ids = getChildDeptIdsByFullName(orgId, list);
		if (!AssertUtil.isEmpty(ids)) {
			result = new HashSet<String>(ids);
		}
		if (result == null) {
			result = new HashSet<String>(departIds.length);
		}
		for(String s : departIds){
			result.add(s);
		}
		return result;
	}

	@Override
	public Set<String> getAllChildDepartList(List<TbDepartmentInfoVO> deptList, String orgId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(deptList)){
			return null;
		}
		List<String> list = new ArrayList<String>(deptList.size());
		Set<String> result = new HashSet<String>(deptList.size());
		for (TbDepartmentInfoVO vo : deptList) {
			list.add(vo.getDeptFullName());
			result.add(vo.getId());
		}
		List<String> ids = getChildDeptIdsByFullName(orgId, list);
		if (!AssertUtil.isEmpty(ids)) {
			result.addAll(ids);
		}
		return result;
	}

	/**
	 * 返回该部门的特定对象
	 * @param departId 部门id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-20
	 * @version 1.0
	 */
	public 	List<TbDepaSpecificObjPO> getspecificObjByDepaId(String departId, String orgId) throws BaseException, Exception{
		return departmentDAO.getspecificObjByDepaId(departId, orgId);
	}

	/**
	 * 新增部门
	 * @param deptIds 特定对象的部门id列表
	 * @param userIds 特定对象的用户列表
	 * @param org 机构
	 * @param selectUserIds 通知对象
	 * @param tbDepartmentInfoPO 部门po
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-21
	 * @version 1.0
	 */
	public void addDepart(String deptIds, String userIds, UserOrgVO org, String selectUserIds, TbDepartmentInfoPO tbDepartmentInfoPO) throws BaseException, Exception{
		if(!AssertUtil.isEmpty(tbDepartmentInfoPO.getParentDepart())){//有父部门的
			//组装部门全称
			TbDepartmentInfoPO parent = this.searchByPk(TbDepartmentInfoPO.class, tbDepartmentInfoPO.getParentDepart());
			if(parent == null){
				throw new NonePrintException("2002", "父级部门为空");
			}
			if(AssertUtil.isEmpty(parent.getWxId())){
				throw new NonePrintException("2003", "父级部门有误，请尝试删掉父部门，重新新增！");
			}
			tbDepartmentInfoPO.setDeptFullName(parent.getDeptFullName()+"->"+tbDepartmentInfoPO.getDepartmentName());
			tbDepartmentInfoPO.setWxParentid(parent.getWxId());
			//设置部门权限
			DepartmentUtil.setPermission(tbDepartmentInfoPO, parent);
		}else{//无父部门
			CheckDeptVisible.checkByAgent(null,org.getCorpId(),org.getOrgId(), WxAgentUtil.getAddressBookCode());
			//parent为空表示此部门为机构下的第一层部门
			tbDepartmentInfoPO.setDeptFullName(tbDepartmentInfoPO.getDepartmentName());
			tbDepartmentInfoPO.setWxParentid(org.getWxId());
		}
		//同步部门到微信
		WxDept wxDept= DepartmentUtil.publishToWx(org.getUserName(),tbDepartmentInfoPO,org);
		if(wxDept==null || AssertUtil.isEmpty(wxDept.getId()) || AssertUtil.isEmpty(wxDept.getParentid())){
			throw new NonePrintException("2004", "新增部门到微信失败");
		}
		tbDepartmentInfoPO.setWxId(wxDept.getId());
		tbDepartmentInfoPO.setWxParentid(wxDept.getParentid());
		tbDepartmentInfoPO.setId(DepartmentUtil.getDeptId(org.getCorpId(), wxDept.getId()));
		if(AssertUtil.isEmpty(tbDepartmentInfoPO.getLeaveMessage()))
			tbDepartmentInfoPO.setLeaveMessage("1");//如果为空默认可以留言
		tbDepartmentInfoPO = this.insertPO(tbDepartmentInfoPO, false);
		if(DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission())){//如果是仅特定对象
			List<TbDepaSpecificObjPO>  deptObjList= DepartmentUtil.setSpecificList(deptIds, DepartmentUtil.ID_TYPE_DEPT, org, tbDepartmentInfoPO.getId());
			List<TbDepaSpecificObjPO>  userObjList= DepartmentUtil.setSpecificList(userIds, DepartmentUtil.ID_TYPE_USER, org, tbDepartmentInfoPO.getId());
			if(deptObjList.size() > 0){
				departmentDAO.execBatchInsert(deptObjList);
			}
			if(userObjList.size() > 0){
				departmentDAO.execBatchInsert(userObjList);
			}
		}
	}

	/**
	 * 批量查询部门信息
	 * @param ids
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-22
	 * @version 1.0
	 */
	public 	List<SelectDeptVO> getDeptByIds(List<String> ids)throws BaseException, Exception{
		return departmentDAO.getDeptByIds(ids);
	}

	/**
	 *批量查询部门的特定对象
	 * @param deptIds 部门id列表
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-27
	 * @version 1.0
	 */
	public 	List<TbDepaSpecificObjPO> getspecificObjByIds(List<String> deptIds) throws BaseException, Exception{
		return departmentDAO.getspecificObjByIds(deptIds);
	}

	@Override
	public List<TbDepartmentInfoPO> getDepartmentByUserIds(List<String> userIds, String orgId) throws BaseException, Exception {

		return departmentReadOnlyDAO.getDepartmentByUserIds(userIds, orgId);
	}

	@Override
	public List<String> getWxDeptIdsByIds(List<String> deptIds) throws SQLException {
		if (AssertUtil.isEmpty(deptIds)) {
			return null;
		}
		int size = deptIds.size();
		List<String> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = departmentReadOnlyDAO.getWxDeptIdsByIds(deptIds);
		} else {
			list = new ArrayList<String>(size);
			List<String> newDeptIds;
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				newDeptIds = deptIds.subList(startIndex, startIndex + Configuration.SQL_IN_MAX);
				list.addAll(departmentReadOnlyDAO.getWxDeptIdsByIds(newDeptIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newDeptIds = deptIds.subList(startIndex, size);
				list.addAll(departmentReadOnlyDAO.getWxDeptIdsByIds(newDeptIds));
			}
		}
		return list;
	}

	@Override
	public List<String> getDeptIdsByWxIds(String orgId, List<String> wxDeptId) throws SQLException {
		if(AssertUtil.isEmpty(wxDeptId)){
			return new ArrayList<String>(0);
		}
		return departmentReadOnlyDAO.getDeptIdsByWxIds(orgId, wxDeptId);
	}
	/**
	 *获取所有子部门及本部门的id, key是传进来的部门id，value是对应的本和子部门
	 * @param departIds 部门id列表
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-04-11
	 * @version 1.0
	 */
	public Map<String, Set<String>> getAllChildDepartMap(List<String> departIds, String orgId) throws BaseException, Exception {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		if (AssertUtil.isEmpty(departIds)){
			return map;
		}
		Map<String, String> deFullNameMap = new HashMap<String, String>();
		List<TbDepartmentInfoPO> departPOs = searchByPks(TbDepartmentInfoPO.class, departIds);
		List<String> departFullNames = new ArrayList<String>(departPOs.size());
		Set<String> set;
		for(TbDepartmentInfoPO departPO : departPOs){
			if(!AssertUtil.isEmpty(departPO.getDeptFullName())) {//如果部门全称不为空
				departFullNames.add(departPO.getDeptFullName());
				deFullNameMap.put(departPO.getId(), departPO.getDeptFullName());
				//初始化要返回的map
				set = new HashSet<String>();
				set.add(departPO.getId());
				map.put(departPO.getId(), set);
			}
		}
		//根据部门全称去like部门，获取该部门和相应的子部门
		List<TbDepartmentInfoVO> deptVOs = departmentReadOnlyDAO.getChildDeptIdsByFullNameToList(orgId, departFullNames);
		Iterator<Map.Entry<String, String>> it = deFullNameMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			for(TbDepartmentInfoVO deptVO : deptVOs){
				if(deptVO.getDeptFullName().indexOf(entry.getValue()) != -1){
					map.get(entry.getKey()).add(deptVO.getId());
				}
			}
		}
		return map;
	}

	@Override
	public List<TbDepartmentInfoPO> findDeptInfoAndAuditUserByDpids(String departmentIds, List<TbQyUserInfoVO> userList, String terminationDeptIds) throws Exception, BaseException {
		if (AssertUtil.isEmpty(departmentIds))
			return null;
		String[] departmentIdArry = departmentIds.split(";");
		List<TbDepartmentInfoPO> departments = new ArrayList<TbDepartmentInfoPO>();
		TbDepartmentInfoPO dp = new TbDepartmentInfoPO();
		Set<String> departmentIdList=new HashSet<String>();//防止存在重复部门，例如当前人父子部门都存在，子部门没有负责人时会有重复部门的情况
		for (int i = 0; i < departmentIdArry.length; i++) {
			dp = getParentDepartmentReceiveUser(departmentIdArry[i],userList,terminationDeptIds);
			if (null != dp &&departmentIdList.add(dp.getId())) {
				departments.add(dp);
			}
		}
		return departments;
	}
}
