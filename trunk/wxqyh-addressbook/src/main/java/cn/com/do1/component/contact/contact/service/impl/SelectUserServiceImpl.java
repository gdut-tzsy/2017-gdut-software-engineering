package cn.com.do1.component.contact.contact.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.dao.IContactDAO;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.contact.util.UserEduUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.dao.ISelectUserReadOnlyDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.addressbook.contact.service.ISelectUserService;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;

/**
 * 标签管理
 * <p>
 * Title: 功能/模块
 * </p>
 * <p>
 * Description: 类的描述
 * </p>
 *
 * @author Luo Rilang
 * @2015-11-2
 * @version 1.0 修订历史： 日期 作者 参考 描述
 */
@Service("selectUserService")
public class SelectUserServiceImpl extends BaseService implements
		ISelectUserService,ISelectUserMgrService {
	private final static transient Logger logger = LoggerFactory
			.getLogger(SelectUserServiceImpl.class);
	private ISelectUserReadOnlyDAO selectUserReadOnlyDAO;
	private IContactDAO contactDAO;
	@Resource
	private IDepartmentMgrService departmentMgrService;

	private IDepartmentService departmentService;
	private IContactService contactService;
	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	/**
	 * @param selectUserReadOnlyDAO 要设置的 selectUserReadOnlyDAO
	 */
	@Resource
	public void setSelectUserReadOnlyDAO(
			ISelectUserReadOnlyDAO selectUserReadOnlyDAO) {
		this.selectUserReadOnlyDAO = selectUserReadOnlyDAO;
	}

	@Resource
	public void setContactDAO(IContactDAO contactDAO) {
		this.contactDAO = contactDAO;
		setDAO(contactDAO);
	}

	@Override
	public Pager searchByNameOrPhone(Map<String, Object> map, Pager pager,
			UserInfoVO user) throws Exception, BaseException {
		String orgId = user.getOrgId();
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		if(!AssertUtil.isEmpty(map.get("keyWord"))){
			map = optimizeByNameOrPhone(map);
		}
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		List<TbDepartmentInfoVO> depts = departmentService
				.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0
				&& !AssertUtil.isEmpty(depts.get(0))
				&& !StringUtil.isNullEmpty(depts.get(0).getPermission())
				&& !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			return UserEduUtil.addChildrenToVO(selectUserReadOnlyDAO.searchByNameOrPhone(map, pager, depts), depts, user);
		}
		return UserEduUtil.addChildrenToVO(selectUserReadOnlyDAO.searchByNameOrPhone(map, pager, null),null, user);
	}

	@Override
	public String getSetFiled(String orgId) throws Exception, BaseException {
		String isDisplay = ContactUtil.IS_DISPLAY_0;
		// maquanyang 2015-7-20 新增查询设置通讯录字段是否允许员工可自行修改
		List<TbQyFieldSettingVO> fieldList = this.selectUserReadOnlyDAO
				.findTbQyFieldSettingVOListByOrgId(orgId);
		if (!AssertUtil.isEmpty(fieldList) && fieldList.size() > 0) {
			for (TbQyFieldSettingVO fieldVO : fieldList) {
				// 控制那些内容在页面上需要
				if (ContactUtil.FIELD_NAME_3.equals(fieldVO.getField())) {// 手机号码
					if (ContactUtil.IS_DISPLAY_1.equals(fieldVO.getIsDisplay())) {// 不显示
						isDisplay = ContactUtil.IS_EDIT_1;
						break;
					}
				}
			}
		}
		return isDisplay;
	}

	@Override
	public Pager findAlluserByUser(Pager pager, UserInfoVO user,
			Map<String, Object> params) throws Exception, BaseException {
		String orgId = user.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agentCode", params.get("agentCode") + "");
		map.put("orgId", orgId);
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		if (!AssertUtil.isEmpty(params.get("sortTop"))) {
			map.put("sortTop", params.get("sortTop").toString());
		}
		List<TbDepartmentInfoVO> depts = departmentService
				.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0
				&& !StringUtil.isNullEmpty(depts.get(0).getPermission())
				&& !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			pager = selectUserReadOnlyDAO
					.findAlluserByDeptId(map, pager, depts);
			UserEduUtil.addChildrenToVO(pager, depts, user);
		} else {
			pager = selectUserReadOnlyDAO.searchContactByPy(map, pager);
			UserEduUtil.addChildrenToVO(pager, null, user);
		}
		return pager;
	}

	@Override
	public Pager searchFirstLetter(String keyWord, Pager pager,
			UserInfoVO user, String agentCode) throws Exception, BaseException {
		String orgId = user.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("agentCode", agentCode);
		map.put("keyWord", keyWord.toLowerCase() + "%");
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		List<TbDepartmentInfoVO> depts = departmentService
				.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0
				&& !StringUtil.isNullEmpty(depts.get(0).getPermission())
				&& !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			pager = selectUserReadOnlyDAO.searchFirstLetter(map, pager, depts);
			UserEduUtil.addChildrenToVO(pager, depts, user);
		} else {
			pager = selectUserReadOnlyDAO.searchFirstLetter(map, pager, null);
			UserEduUtil.addChildrenToVO(pager, null, user);
		}
		return pager;
	}

	@Override
	public List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit,
			String agentCode, String corpId) throws Exception, BaseException {
		return selectUserReadOnlyDAO
				.getCommonUserList(userId, limit, agentCode, corpId);
	}

	@Override
	public Pager getUserGroup(Pager pager, Map<String, Object> map)
			throws Exception, BaseException {
		return selectUserReadOnlyDAO.getUserGroup(pager, map);
	}

	@Override
	public TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception,
			BaseException {
		return selectUserReadOnlyDAO.findUserInfoByUserId(userId);
	}

	@Override
	public List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception,
			BaseException {
		return selectUserReadOnlyDAO.getUserGroup(orgId);
	}

	@Override
	public Pager getUserGroupPerson(Pager pager, Map<String, Object> map)
			throws Exception, BaseException {
		return selectUserReadOnlyDAO.getUserGroupPerson(pager, map);
	}
	
	@Override
	public List<String> getVisibleRangeUsers(String orgId, String agentCode)
			throws Exception, BaseException {
		return selectUserReadOnlyDAO.getVisibleRangeUsers(orgId, agentCode);
	}

	@Override
	public Pager searchContact(Map searchMap, Pager pager, String[] departIds, String[] userIds) throws Exception,
			BaseException {
		//
		if (!AssertUtil.isEmpty(searchMap.get("currDept")) && "1".equals(searchMap.get("currDept"))) {
			// 搜索部门成员，【不包括子部门】
			searchMap.remove("currDept");// 去掉判断参数
		} else {
			if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
				// 搜索部门成员，【包括子部门】
				TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
				if (department == null || !department.getOrgId().equals(searchMap.get("orgId").toString())) {
					return pager;
				}
				List<String> nameIds = new ArrayList<String>(1);
				nameIds.add(department.getDeptFullName());
				List<String> ids = departmentMgrService.getChildDeptIdsByFullName(searchMap.get("orgId").toString(), nameIds);
				if (AssertUtil.isEmpty(ids)) {
					ids = new ArrayList<String>(1);
				}
				ids.add(department.getId());
				searchMap.remove("deptId");
				searchMap.remove("agentCode");
				searchMap.put("departIds", new ArrayList<String>(ids));
			}
			else {
				Set<String> ids = departmentService.getAllChildDepartIds(departIds, searchMap.get("orgId").toString());
				if (!AssertUtil.isEmpty(ids)) {
					searchMap.put("departIds", new ArrayList<String>(ids));
				}
			}
		}
		return selectUserReadOnlyDAO.searchContact(searchMap, pager);
	}

	@Override
	public Pager searchContactByDepartList(Map searchMap, Pager pager, List<TbDepartmentInfoVO> list, String[] userIds) throws Exception,
			BaseException {
		//
		if (!AssertUtil.isEmpty(searchMap.get("currDept")) && "1".equals(searchMap.get("currDept"))) {
			// 搜索部门成员，【不包括子部门】
			searchMap.remove("currDept");// 去掉判断参数
		} else {
			if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
				// 搜索部门成员，【包括子部门】
				TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
				if (department == null || !department.getOrgId().equals(searchMap.get("orgId").toString())) {
					return pager;
				}
				List<String> nameIds = new ArrayList<String>(1);
				nameIds.add(department.getDeptFullName());
				List<String> ids = departmentMgrService.getChildDeptIdsByFullName(searchMap.get("orgId").toString(), nameIds);
				if (AssertUtil.isEmpty(ids)) {
					ids = new ArrayList<String>(1);
				}
				ids.add(department.getId());
				searchMap.remove("deptId");
				searchMap.remove("agentCode");
				searchMap.put("departIds", new ArrayList<String>(ids));
			}
			else {
				Set<String> ids = departmentService.getAllChildDepartList(list, searchMap.get("orgId").toString());
				if (!AssertUtil.isEmpty(ids)) {
					searchMap.put("departIds", new ArrayList<String>(ids));
				}
			}
		}
		return selectUserReadOnlyDAO.searchContact(searchMap, pager);
	}

	@Override
	public Pager findUsersByOrgId(Map<String, Object> map, Pager pager)
			throws Exception, BaseException {
		/**
		 * 已知问题：当操作用户部门权限属于：部门A（本部门或子部门），部门A存在用户1，用户1存在多个部门信息（如部门B），操作用户输入部门B，是无法搜索到对应的用户1
		 */
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(map.get("orgId").toString(), map.get("userId").toString());
		map.remove("userId");
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			return selectUserReadOnlyDAO.findUsersByOrgId(map, pager, depts);
		} else {
			return selectUserReadOnlyDAO.findUsersByOrgId(map, pager, null);
		}
	}

	@Override
	public List<UserDeptInfoVO> findUserDeptInfosByWxUIdAndOrgId(String[] wxuid, String orgId) throws Exception, BaseException {
		return selectUserReadOnlyDAO.findUserDeptInfosByWxUIdndOrgId(wxuid,orgId);
	}


	@Override
	public void doSortAccountListInBatch(int batLimit, List<String> effPersonNameList, UserOrgVO orgVo,
										 Map<String, Object> effDataMap, boolean isMaxAuth,
										 List<TbDepartmentInfoPO> effDepartmentList, List<TbDepartmentInfoPO> effRangeDepartmentList,
										 List<UserAccountInfoVO> dataList, List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList) throws Exception, BaseException {
		if (null != effPersonNameList && effPersonNameList.size() > 0) {
			int size = effPersonNameList.size();
			if (size <= batLimit) {
				sortUserList2EffListAndUnEffListAndWrongList(effPersonNameList,orgVo,isMaxAuth,
						effDepartmentList,effRangeDepartmentList,dataList,unEffDataList,wrongDataList);
			} else {
				int startIndex = 0;
				while (startIndex + batLimit <= size) {
					List<String> listPage = effPersonNameList.subList(startIndex, startIndex+batLimit);
					sortUserList2EffListAndUnEffListAndWrongList(listPage,orgVo,isMaxAuth,
							effDepartmentList,effRangeDepartmentList,dataList,unEffDataList,wrongDataList);
					startIndex += batLimit;
				}
				if (startIndex < size) {
					List<String> listPage = effPersonNameList.subList(startIndex, size);
					sortUserList2EffListAndUnEffListAndWrongList(listPage,orgVo,isMaxAuth,
							effDepartmentList,effRangeDepartmentList,dataList,unEffDataList,wrongDataList);
				}
			}
		}
	}

	@Override
	public void sortUserList2EffListAndUnEffListAndWrongList(List<String> listPage, UserOrgVO orgVo, boolean isMaxAuth,
															 List<TbDepartmentInfoPO> effDepartmentList,
															 List<TbDepartmentInfoPO> effRangeDepartmentList, List<UserAccountInfoVO> dataList, List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList) throws Exception, BaseException {
		List<String> effWxUidList = new ArrayList<String>();
		String[] wxids = new String[listPage.size()];
		for (int i = 0; i < listPage.size(); i++) {
			wxids[i] = listPage.get(i).trim();
		}
		List<UserDeptInfoVO> deptInfoVOs = findUserDeptInfosByWxUIdAndOrgId(wxids,orgVo.getOrgId());
		Map<String,Object> effDataMap = new HashMap<String, Object>();
		for (UserDeptInfoVO deptInfoVO:deptInfoVOs){
			effWxUidList.add(deptInfoVO.getUserId());
			effDataMap.put(deptInfoVO.getWxUserId(),deptInfoVO);
		}
		for (String wxuid:wxids){
			UserAccountInfoVO userVoData = new UserAccountInfoVO();
			userVoData.setWxUserId(wxuid);
			if (effDataMap.get(wxuid)!=null){
				UserDeptInfoVO vo = (UserDeptInfoVO) effDataMap.get(wxuid);
				userVoData.setId(vo.getUserId());
				userVoData.setUserId(vo.getUserId());
				userVoData.setHeadPic(vo.getHeadPic());
				userVoData.setPersonName(vo.getPersonName());
				if (isMaxAuth){
					dataList.add(userVoData);
				}else {
					boolean isEffByAdmin = false;
					boolean isEffByRange = false;
					//管理员的部门权限
					for (TbDepartmentInfoPO po:effDepartmentList){
						if(judgeUserIsEffectiveForDepartment(vo,po)){
							isEffByAdmin = true;
							break;
						}
					}
					//可见范围
					if(isEffByAdmin) {
						if (effRangeDepartmentList == null) {
							isEffByRange = true;
						} else {
							for (TbDepartmentInfoPO po : effRangeDepartmentList) {
								if (judgeUserIsEffectiveForDepartment(vo, po)) {
									isEffByRange = true;
									break;
								}
							}
						}
					}

					if (isEffByAdmin && isEffByRange){
						dataList.add(userVoData);
					}else {
						unEffDataList.add(userVoData);
					}
				}
			}else{
				wrongDataList.add(userVoData);
			}
		}
	}

	/**
	 * 判断这个用户账号是否合法
	 * @param vo the vo
	 * @param po the po
	 * @return the boolean
	 * @author 陈春武
	 * @time 2016年07月05日  Judge user is effective for department.
	 */
	private boolean judgeUserIsEffectiveForDepartment(UserDeptInfoVO vo,TbDepartmentInfoPO po){
		boolean isEff = false;
		String userDepartmentNames = vo.getDeptFullName();
		String adminDepartmentName = po.getDeptFullName();
		String[] userDepartmentNamess = userDepartmentNames.split(";");
		for (String userDepartmentName:userDepartmentNamess) {
			if(userDepartmentName.equals(adminDepartmentName)){
				isEff = true;
			}else{
				String adminDeparmentNameTemp = adminDepartmentName+"->";
				if (userDepartmentName.startsWith(adminDeparmentNameTemp)){
					isEff = true;
				}
			}
		}
		return isEff;
	}

	/**
	 *优化用户按名字、拼音、电话搜索条件
	 * @return
	 * @author LiYiXin
	 * 2016-8-25
	 */
	public Map<String, Object>  optimizeByNameOrPhone(Map<String, Object> map) throws Exception,BaseException{
		String keyWord = (String) map.get("keyWord");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(keyWord);
		//如果是纯数字
		if(isNum.matches()){
			map.put("mobile","%" +keyWord + "%");
			map.put("keyWord","%" +keyWord + "%");
			return map;
		}
		else{
			pattern = Pattern.compile("[a-zA-Z]+");
			isNum = pattern.matcher(keyWord);
			//如果是纯英文
			if(isNum.matches()){
				map.put("pinyin",keyWord);
				map.put("keyWord","%" +keyWord + "%");
				String pinyin = (String) map.get("pinyin");
				Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
				Matcher ma = patter.matcher(pinyin);
				StringBuilder sb = new StringBuilder("");
				// 在字母中间添加%
				if (ma.find()) {
					for (char iterable_element : pinyin.toCharArray()) {
						sb.append(iterable_element).append("%");
					}
				}
				if (sb.length() > 0)
					sb = sb.replace(sb.length() - 1, sb.length(), "");

				if (sb.length() > 0)
					pinyin = sb.toString();
				if (!AssertUtil.isEmpty(pinyin)) {
					map.put("pinyin", "%" + pinyin.toLowerCase() + "%");
				}
				return map;
			}
			//其它情况
			else{
				map.put("keyWord","%" +keyWord + "%");
				return map;
			}
		}
	}

	/**
	 * 批量处理分类账号
	 * @param batLimit          the bat limit 每次批量的限制数量
	 * @param longLimit         the long limit 最大长度限制
	 * @param effPersonNameList the eff person name list 批量处理的账号
	 * @param orgVo             the org vo 机构vo
	 * @param effDataMap        the eff data map 错误的map
	 * @param isMaxAuth         the is max auth
	 * @param effDepartmentList the eff department list
	 * @param dataList          the data list
	 * @param unEffDataList     the un eff data list
	 * @param wrongDataList     the wrong data list
	 * @return the void
	 * @author liyixin
	 * @time 2017年01月9日
	 * Do sort account list in batch.
	 */
	public void doSortAccountListInBatch(int batLimit, int longLimit,List<String> effPersonNameList, UserOrgVO orgVo,
								  Map<String, Object> effDataMap, boolean isMaxAuth,
								  List<TbDepartmentInfoPO> effDepartmentList, List<TbDepartmentInfoPO> effRangeDepartmentList,
								  List<UserAccountInfoVO> dataList, List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList)throws Exception, BaseException{
		int size = effPersonNameList.size();
		if(size > longLimit){//如果它的长度大于它的最大限制数量
			List<String> overstepList = effPersonNameList.subList(longLimit,effPersonNameList.size());//超出长度的剩余部分
			for (String overstep:overstepList){//超出长度的剩余部分全部放到错误列表里面去
				UserAccountInfoVO userVoData = new UserAccountInfoVO();
				userVoData.setWxUserId(overstep);
				wrongDataList.add(userVoData);
			}
			effPersonNameList = effPersonNameList.subList(0,longLimit);
			this.doSortAccountListInBatch(batLimit,effPersonNameList,orgVo, effDataMap, isMaxAuth, effDepartmentList, effRangeDepartmentList,dataList, unEffDataList, wrongDataList);
		}else {//如果它的长度不大于它的最大限制数量
			this.doSortAccountListInBatch(batLimit,effPersonNameList,orgVo, effDataMap, isMaxAuth, effDepartmentList, effRangeDepartmentList,dataList, unEffDataList, wrongDataList);
		}
	}

	@Override
	public List<TbQyUserInfoForPage> searchContactList(Map<String, Object> searchMap, List<String> deptIdsList, List<String> userIdsList) throws Exception, BaseException {
		boolean allNull = true;
		if (!AssertUtil.isEmpty(deptIdsList)) {
			Set<String> ids = departmentService.getAllChildDepartIds(ListUtil.toArrays(deptIdsList), searchMap.get("orgId").toString());
			if (!AssertUtil.isEmpty(ids)) {
				searchMap.put("departIds", new ArrayList<String>(ids));
			}
			allNull = false;
		}
		if (!AssertUtil.isEmpty(userIdsList)) {
			searchMap.put("userIds", userIdsList);
			allNull = false;
		}
		if (allNull) {
			return null;
		}
		return selectUserReadOnlyDAO.searchContactList(searchMap);
	}

	@Override
	public List<UserRedundancyInfoVO> searchUserRedundancyList(Map<String, Object> searchMap, List<String> deptIdsList, List<String> userIdsList) throws Exception, BaseException {
		boolean allNull = true;
		if (!AssertUtil.isEmpty(deptIdsList)) {
			Set<String> ids = departmentService.getAllChildDepartIds(ListUtil.toArrays(deptIdsList), searchMap.get("orgId").toString());
			if (!AssertUtil.isEmpty(ids)) {
				searchMap.put("departIds", new ArrayList<String>(ids));
			}
			allNull = false;
		}
		if (!AssertUtil.isEmpty(userIdsList)) {
			searchMap.put("userIds", userIdsList);
			allNull = false;
		}
		if (allNull) {
			return null;
		}
		return selectUserReadOnlyDAO.searchUserRedundancyList(searchMap);
	}

	@Override
	public List<TbQyUserInfoVO> searchUserInfoVOList(Map<String, Object> searchMap, ArrayList<String> deptIdsList, ArrayList<String> userIdsList) throws Exception, BaseException {
		boolean allNull = true;
		if (!AssertUtil.isEmpty(deptIdsList)) {
			Set<String> ids = departmentService.getAllChildDepartIds(ListUtil.toArrays(deptIdsList), searchMap.get("orgId").toString());
			if (!AssertUtil.isEmpty(ids)) {
				searchMap.put("departIds", new ArrayList<String>(ids));
			}
			allNull = false;
		}
		if (!AssertUtil.isEmpty(userIdsList)) {
			searchMap.put("userIds", userIdsList);
			allNull = false;
		}
		if (allNull) {
			return null;
		}
		return selectUserReadOnlyDAO.searchUserInfoVOList(searchMap);
	}

	/**
	 *分页获取应用可见范围内的用户
	 * @param agentCode 应用code
	 * @param orgVO 机构vo
	 * @param pager 分页数据 每页最大长度为2000
	 * @return
	 * @throws BaseException 这是异常啊，哥
	 * @throws Exception 这是异常啊，哥
	 * @author liyixin
	 * @2017-3-27
	 * @version 1.0
	 */
	public Pager searchUserByAgentCode(String agentCode, UserOrgVO orgVO, Pager pager) throws BaseException, Exception{
		//判断机构是否为空
		if(AssertUtil.isEmpty(orgVO) || AssertUtil.isEmpty(orgVO.getOrgId())){
			throw new BaseException(ErrorCodeDesc.ORG_IS_NULL.getCode(), ErrorCodeDesc.ORG_IS_NULL.getDesc());
		}
		if(AssertUtil.isEmpty(pager)){
			pager = new Pager();
			pager.setPageSize(2000);
			pager.setCurrentPage(1);
		}
		if(pager.getPageSize() > 2000){
			throw new BaseException(ErrorCodeDesc.BEYOND_MAX_SIZE.getCode(), ErrorCodeDesc.BEYOND_MAX_SIZE.getDesc());
		}
		if(pager.getPageSize() < 1){
			pager.setPageSize(2000);
		}
		int pagSize = pager.getPageSize();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("orgId", orgVO.getOrgId());
		searchMap.put("aliveStatus", "-1");
		searchMap.put("agentCode", agentCode);
		AgentCacheInfo aci = StringUtil.isNullEmpty(agentCode) ? null : WxAgentUtil.getAgentCache(orgVO.getCorpId(), agentCode);
		if(AssertUtil.isEmpty(agentCode) || (aci != null && aci.isAllUserUsable())
				|| (aci == null && WxAgentUtil.getAddressBookCode().equals(agentCode))){
			searchMap.remove("agentCode");
		}
		if(ManageUtil.superAdmin != orgVO.getAge()){//普通管理员
			String[] departIds;
			TbManagerPersonVO vo = contactService.getManagerPersonByPersonIdAndOrgId(orgVO.getPersonId(), orgVO.getOrgId());
			//如果查询不到管理员
			if(AssertUtil.isEmpty(vo)){
				pager = new Pager(pagSize);
				pager.setTotalPages(0);
				return pager;
			}
			//如果应用发送范围不为空,而且该agentcode是在这个发送范围之内
			else if (!AssertUtil.isEmpty(vo.getMsgCodeIds()) && ("|" + vo.getMsgCodeIds() + "|").contains("|" + agentCode + "|")){
				if(!ManageUtil.RANGE_ONE_INT.equals(vo.getMsgRange())){//如果消息不是全公司可见
					if (AssertUtil.isEmpty(vo.getMsgDeptIds()) && AssertUtil.isEmpty(vo.getMsgUserIds())) {//如果特定部门和人员都为空
						pager = new Pager(pagSize);
						pager.setTotalPages(0);
						return pager;
					}
					else {//特定部门和人员
						String[] userIds = ContactUtil.getIntersectionByTwo(vo.getMsgUserIds(), null);
						if (!AssertUtil.isEmpty(userIds)) {
							searchMap.put("userIds", userIds);
						}
						if (!AssertUtil.isEmpty(vo.getMsgDeptIds())) {
							departIds = vo.getMsgDeptIds().split("\\|");
							pager = searchContact(searchMap, pager, departIds, userIds);
							return pager;
						}
						else {
							pager = searchContact(searchMap, pager, null, userIds);
							return pager;
						}
					}
				}
				else {//如果消息发送对象是所有人可见
					pager = searchContact(searchMap, pager, null, null);
					return pager;
				}
			}
			else if(!ManageUtil.RANGE_ONE.equals(vo.getRanges())) {//如果管理对象不是全公司可见
				if (AssertUtil.isEmpty(vo.getDepartids())) {
					pager = new Pager(pagSize);
					pager.setTotalPages(0);
					return pager;
				}
				else {
					departIds = vo.getDepartids().split("\\|");
					pager = searchContact(searchMap, pager, departIds, null);
					return pager;
				}
			}
			else {//如果管理对象是全公司可见
				pager = searchContact(searchMap, pager, null, null);
				return pager;
			}
		}
		else{//超管
			pager = searchContact(searchMap, pager, null, null);
			return pager;
		}
	}

	/**
	 * 获取应用可见范围内的用户总数
	 * @param agentCode 应用code
	 * @param orgVO     机构vo
	 * @return
	 * @throws BaseException 这是异常啊，哥
	 * @throws Exception     这是异常啊，哥
	 * @author liyixin
	 * @2017-6-30
	 * @version 1.0
	 */
	@Override
	public int serachUserCount(String agentCode, UserOrgVO orgVO) throws BaseException, Exception {
		int count ;
		//判断机构是否为空
		if(AssertUtil.isEmpty(orgVO) || AssertUtil.isEmpty(orgVO.getOrgId())){
			throw new BaseException(ErrorCodeDesc.ORG_IS_NULL.getCode(), ErrorCodeDesc.ORG_IS_NULL.getDesc());
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("orgId", orgVO.getOrgId());
		searchMap.put("aliveStatus", "-1");
		searchMap.put("agentCode", agentCode);
		AgentCacheInfo aci = StringUtil.isNullEmpty(agentCode) ? null : WxAgentUtil.getAgentCache(orgVO.getCorpId(), agentCode);
		if(AssertUtil.isEmpty(agentCode) || (aci != null && aci.isAllUserUsable())
				|| (aci == null && WxAgentUtil.getAddressBookCode().equals(agentCode))){
			searchMap.remove("agentCode");
		}
		if(ManageUtil.superAdmin != orgVO.getAge()){//普通管理员
			String[] departIds;
			TbManagerPersonVO vo = contactService.getManagerPersonByPersonIdAndOrgId(orgVO.getPersonId(), orgVO.getOrgId());
			//如果查询不到管理员
			if(AssertUtil.isEmpty(vo)){
				count = 0;
				return count;
			}
			//如果应用发送范围不为空,而且该agentcode是在这个发送范围之内
			else if (!AssertUtil.isEmpty(vo.getMsgCodeIds()) && ("|" + vo.getMsgCodeIds() + "|").contains("|" + agentCode + "|")){
				if(!ManageUtil.RANGE_ONE_INT.equals(vo.getMsgRange())){//如果消息不是全公司可见
					if (AssertUtil.isEmpty(vo.getMsgDeptIds()) && AssertUtil.isEmpty(vo.getMsgUserIds())) {//如果特定部门和人员都为空
						count = 0;
						return count;
					}
					else {//特定部门和人员
						String[] userIds = ContactUtil.getIntersectionByTwo(vo.getMsgUserIds(), null);
						if (!AssertUtil.isEmpty(userIds)) {
							searchMap.put("userIds", userIds);
						}
						if (!AssertUtil.isEmpty(vo.getMsgDeptIds())) {
							departIds = vo.getMsgDeptIds().split("\\|");
							Set<String> ids = departmentService.getAllChildDepartIds(departIds, orgVO.getOrgId());
							if (!AssertUtil.isEmpty(ids)) {
								searchMap.put("departIds", new ArrayList<String>(ids));
							}
							count = selectUserReadOnlyDAO.serachUserCount(searchMap);
							return count;
						}
						else {
							count = selectUserReadOnlyDAO.serachUserCount(searchMap);
							return count;
						}
					}
				}
				else {//如果消息发送对象是所有人可见
					count = selectUserReadOnlyDAO.serachUserCount(searchMap);
					return count;
				}
			}
			else if(!ManageUtil.RANGE_ONE.equals(vo.getRanges())) {//如果管理对象不是全公司可见
				if (AssertUtil.isEmpty(vo.getDepartids())) {
					count = 0;
					return count;
				}
				else {
					departIds = vo.getDepartids().split("\\|");
					Set<String> ids = departmentService.getAllChildDepartIds(departIds, orgVO.getOrgId());
					if (!AssertUtil.isEmpty(ids)) {
						searchMap.put("departIds", new ArrayList<String>(ids));
					}
					count = selectUserReadOnlyDAO.serachUserCount(searchMap);
					return count;
				}
			}
			else {//如果管理对象是全公司可见
				count = selectUserReadOnlyDAO.serachUserCount(searchMap);
				return count;
			}
		}
		else{//超管
			count = selectUserReadOnlyDAO.serachUserCount(searchMap);
			return count;
		}
	}

}
