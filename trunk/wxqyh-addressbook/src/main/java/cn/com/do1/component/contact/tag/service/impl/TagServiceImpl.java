package cn.com.do1.component.contact.tag.service.impl;

import java.sql.SQLException;
import java.util.*;

import javax.annotation.Resource;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.tag.service.ITagService;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.tag.dao.ITagReadOnlyDAO;
import cn.com.do1.component.contact.tag.po.TbQyTagSyncTimePO;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.contact.tag.util.TagUtil;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.HashUtil;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.WxqyhStringUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxTagUtil;
import org.springframework.stereotype.Service;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.tag.dao.ITagDAO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefVO;

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
@Service("tagService")
public class TagServiceImpl extends BaseService implements ITagMgrService, ITagService {
	private ITagDAO tagDAO;
	@Resource(name = "contactService")
	private IContactService contactService;
	@Resource(name = "departmentService")
	private IDepartmentService departmentService;

	@Resource
	private ITagReadOnlyDAO tagReadOnlyDAO;

	@Resource
	public void setTagDAO(ITagDAO tagDAO) {
		this.tagDAO = tagDAO;
		setDAO(tagDAO);
	}

	@Override
	public List<String> getUserIdsByWxTagid(String orgId, String wxTagId)
			throws BaseException, Exception {
		Set<String> idsList = new HashSet<String>(); //方便去重
		if(StringUtil.isNullEmpty(wxTagId)){
			return null;
		}
		//TbQyTagInfoPO po = getTagInfoByWxTagId(orgId, wxTagId);
		List<QyTagRefVO> tagRefs = tagDAO.getTagMenbersByWxTagId(orgId, wxTagId);
		List<String> deptIdsList = new ArrayList<String>(tagRefs.size());
		for(QyTagRefVO vo : tagRefs){
			if(TagStaticUtil.TAG_REF_TYPE_USER_STRING.equals(vo.getMenberType())){	//用户
				idsList.add(vo.getMenberId());
			} else {	//部门
				deptIdsList.add(vo.getMenberId());
			}
		}
		if (deptIdsList.size() > 0) { //获取部门下的用户id
			Set<String> deptIdsSet = departmentService.getAllChildDepartIds(ListUtil.toArrays(deptIdsList), orgId);
			List<String> userList = contactService.findDeptUserIdAllByDeptIds(new ArrayList<String>(deptIdsSet));
			if (!AssertUtil.isEmpty(userList)) {
				idsList.addAll(userList);
			}
		}
		//可能出现id重复，返回后需处理
		return new ArrayList<String>(idsList);
	}

	@Override
	public List<QyTagRefVO> getTagRefVOByWxTagIds(String orgId, List<String> wxTagIds) throws Exception {
		if(wxTagIds == null || wxTagIds.size() == 0){
			return null;
		}
		return tagDAO.getTagRefVOByWxTagIds(orgId, wxTagIds);
	}

	@Override
	public void addTag(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String[] userIds) throws BaseException, Exception {
		TbQyTagInfoPO po = new TbQyTagInfoPO();
		po.setCreateTime(tbQyUserGroupPO.getCreateTime());
		po.setCreator(tbQyUserGroupPO.getCreator());
		po.setOrgId(tbQyUserGroupPO.getOrgId());
		po.setStatus(1-Integer.parseInt(tbQyUserGroupPO.getStatus())); //群组0为启用，1为禁用
		po.setTagName(tbQyUserGroupPO.getGroupName());
		po.setRang(TagStaticUtil.TAG_RANG_ALL);
		po.setShowNum(tbQyUserGroupPO.getShowNum() == null ? TagStaticUtil.TAG_SHOW_NUM_DEFAULT : tbQyUserGroupPO.getShowNum());
		po.setId(tbQyUserGroupPO.getGroupId());
		addTag(userInfoVO, po, userIds, null);
	}

	@Override
	public void addTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds) throws BaseException, Exception {
		String tagId = WxTagUtil.createTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getTagName());
		po.setWxTagId(tagId);
		po.setSource(TagStaticUtil.TAG_INFO_SOURCE_QIWEI);
		this.insertPO(po, false);
		this.addTagRef(userInfoVO, po, userIds, deptIds, true);
	}

	@Override
	public void addTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateWeixin) throws BaseException, Exception {
		int length = 0;
		if (!AssertUtil.isEmpty(userIds)) {
			length += userIds.length;
		}
		if (!AssertUtil.isEmpty(deptIds)) {
			length += deptIds.length;
		}
		if (length == 0) {
			return;
		}
		/**
		 * 新增标签信息
		 * 1、根据标签下的用户部门生成需要插入的关联数据
		 * 2、批量插入关联及标签信息
		 */
		List<TbQyTagRefPO> addList = new ArrayList<TbQyTagRefPO>(length);
		List<List<String>> resultUserIds = getUpdateTagRefUser(po, null, userIds, null, addList);
		List<List<String>> resultDeptIds = getUpdateTagRefDept(po, null, deptIds, null, addList);
		int addUserSize = 0;
		int addDeptSize = 0;
		if (null != resultUserIds.get(0)) {
			addUserSize = resultUserIds.get(0).size();
		}
		if (null != resultDeptIds.get(0)) {
			addDeptSize = resultDeptIds.get(0).size();
		}
		if (isUpdateWeixin) {
			addRefToWeiXin(userInfoVO, po, resultUserIds.get(0), resultDeptIds.get(0));
		}
		if (addList.size() > 0) {
			addList = QwtoolUtil.addBatchList(addList, true);
		}
		this.updateTagUserAndDeptTotal(po, addUserSize, addDeptSize, addList);
	}

	@Override
	public List<QyTagPageInfoVO> getTagPageInfoList(String orgId) throws SQLException {
		return tagDAO.getTagPageInfoList(orgId);
	}

	@Override
	public Pager getTagRefPage(Pager pager, String tagId) throws Exception {
		Map<String, Object> searchMap = new HashMap<String, Object>(1);
		searchMap.put("tagId", tagId);
		return tagDAO.getTagRefPage(pager, searchMap);
	}

	@Override
	public List<TbQyTagInfoPO> getTagListByName(String tagName, String orgId) throws SQLException {
		return tagDAO.getTagListByName(tagName, orgId);
	}

	@Override
	public boolean getIsRepeat(String orgId, String tagName, String tagId) throws Exception {
		List<TbQyTagInfoPO> list = getTagListByName(tagName, orgId);
		if (AssertUtil.isEmpty(list)) {
			return false;
		}
		for (TbQyTagInfoPO po : list) {
			if (!tagId.equals(po.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String userIds, String deptIds) throws BaseException, Exception {
		int length = 0;
		String[] userIdArray = null;
		String[] deptIdArray = null;
		Set<String> menberIdSet = new HashSet<String>(100);
		if (!AssertUtil.isEmpty(userIds)) {
			userIdArray = userIds.split("\\|");
			for(String s : userIdArray){
				menberIdSet.add(s);
			}
			length += userIdArray.length;
		}
		if (!AssertUtil.isEmpty(deptIds)) {
			deptIdArray = deptIds.split("\\|");
			for(String s : deptIdArray){
				menberIdSet.add(s);
			}
			length += deptIdArray.length;
		}
		if (length == 0) {
			return;
		}

		/**
		 * 新增标签信息
		 * 1、根据标签下的用户部门生成需要插入的关联数据
		 * 2、批量插入关联及标签信息
		 */
		Set<String> addUserSet = HashUtil.toHashSet(userIdArray);
		Set<String> addDeptSet = HashUtil.toHashSet(deptIdArray);
		List<String> existList = this.getTagRefMenberIdListByMenberIdList(po.getId(), new ArrayList<String>(menberIdSet));
		if (!AssertUtil.isEmpty(existList)) {
			for (String s : existList) {
				if (addUserSet == null || !addUserSet.remove(s)) {
					if (addDeptSet != null) {
						addDeptSet.remove(s);
					}
				}
			}
		}
		//如果新增人员为空
		if (AssertUtil.isEmpty(addUserSet) && AssertUtil.isEmpty(addDeptSet)) {
			return;
		}
		List<TbQyTagRefPO> addList = new ArrayList<TbQyTagRefPO>(length);
		getAddTagRefUser(po, addUserSet, addList);
		getAddTagRefDept(po, addDeptSet, addList);
		List<String> addUserList = null;
		List<String> addDeptList = null;
		int addUserSize = 0;
		int addDeptSize = 0;
		if (null != addUserSet) {
			addUserList = new ArrayList<String>(addUserSet);
			addUserSize = addUserSet.size();
		}
		if (null != addDeptSet) {
			addDeptList = new ArrayList<String>(addDeptSet);
			addDeptSize = addDeptSet.size();
		}
		addRefToWeiXin(userInfoVO, po, addUserList, addDeptList);
		if (addList.size() > 0) {
			addList = QwtoolUtil.addBatchList(addList, false);
		}
		updateTagUserAndDeptTotal(po, addUserSize, addDeptSize, addList);
	}

	/**
	 * 新增到微信
	 * @param userInfoVO
	 * @param po
	 * @param addUserIds
	 * @param addDeptIds
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-7
	 */
	private void addRefToWeiXin(UserOrgVO userInfoVO, TbQyTagInfoPO po, List<String> addUserIds, List<String> addDeptIds) throws BaseException, Exception {
		//获取新增的用户或部门wxUserId
		List<String> addWxUserIds = null;
		if (!AssertUtil.isEmpty(addUserIds)) {
			addWxUserIds = contactService.getWxUserIdsByUserIds(addUserIds);
		}
		List<String> addWxDeptIds = null;
		if (!AssertUtil.isEmpty(addDeptIds)) {
			addWxDeptIds = departmentService.getWxDeptIdsByIds(addDeptIds);
		}
		//调用微信接口新增或者删除用户及部门
		WxTagUtil.addUserTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getWxTagId(), ListUtil.collToArrays(addWxUserIds), ListUtil.collToArrays(addWxDeptIds));
	}

	/**
	 * 将本地的成员信息新增到微信
	 * @param userInfoVO
	 * @param po
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-7
	 */
	private void addRefToWeiXin(UserOrgVO userInfoVO, TbQyTagInfoPO po) throws BaseException, Exception {
		List<TbQyTagRefPO> tagRefs = this.getTagRefList(po.getId());
		List<String> userIdsList = null;
		List<String> deptIdsList = null;
		if (!AssertUtil.isEmpty(tagRefs)) {
			for(TbQyTagRefPO refPO : tagRefs){
				if(TagStaticUtil.TAG_REF_TYPE_USER == refPO.getMenberType()){	//用户
					if (null == userIdsList) {
						userIdsList = new ArrayList<String>(tagRefs.size());
					}
					userIdsList.add(refPO.getMenberId());
				} else {	//部门
					if (null == deptIdsList) {
						deptIdsList = new ArrayList<String>(tagRefs.size());
					}
					deptIdsList.add(refPO.getMenberId());
				}
			}
		}
		addRefToWeiXin(userInfoVO, po, userIdsList, deptIdsList);
	}

	private void delRefToWeiXin(UserOrgVO userInfoVO, TbQyTagInfoPO po, List<String> delUserIds, List<String> delDeptIds) throws BaseException, Exception {
		if (StringUtil.isNullEmpty(po.getWxTagId())) {
			return;
		}
		//获取删除的用户或部门wxUserId
		List<String> delWxUserIds = null;
		if (!AssertUtil.isEmpty(delUserIds)) {
			delWxUserIds = contactService.getWxUserIdsByUserIds(delUserIds);
		}
		List<String> delWxDeptIds = null;
		if (!AssertUtil.isEmpty(delDeptIds)) {
			delWxDeptIds = departmentService.getWxDeptIdsByIds(delDeptIds);
		}

		//调用微信接口新增或者删除用户及部门
		WxTagUtil.delUserTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getWxTagId(), ListUtil.collToArrays(delWxUserIds), ListUtil.collToArrays(delWxDeptIds));
	}

	@Override
	public List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, ArrayList<String> menberIds, int menberType) throws Exception {
		if (AssertUtil.isEmpty(menberIds)) {
			return null;
		}
		return tagDAO.getTagRefListByMenberId(tagId, menberIds, menberType);
	}

	@Override
	public void delTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, List<TbQyTagRefPO> list) throws BaseException, Exception {
		if (AssertUtil.isEmpty(list)) {
			return;
		}
		List<String> delUserIds = null;
		List<String> delDeptIds = null;
		String[] delIds =  new String[list.size()];
		int i = 0;
		for (TbQyTagRefPO ref : list) {
			if (TagStaticUtil.TAG_REF_TYPE_DEPT == ref.getMenberType()) {
				if (null == delDeptIds) {
					delDeptIds = new ArrayList<String>(list.size());
				}
				delDeptIds.add(ref.getMenberId());
			}
			else {
				if (null == delUserIds) {
					delUserIds = new ArrayList<String>(list.size());
				}
				delUserIds.add(ref.getMenberId());
			}
			delIds[i++] = ref.getId();
		}
		int addUserSize = null == delUserIds ? 0 : -delUserIds.size();
		int addDeptSize = null == delDeptIds ? 0 : -delDeptIds.size();
		delRefToWeiXin(userInfoVO, po, delUserIds, delDeptIds);

		this.batchDel(TbQyTagRefPO.class, delIds);

		this.updateTagUserAndDeptTotal(po, addUserSize, addDeptSize);
	}

	@Override
	public List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, String[] menberIds) throws Exception {
		if (AssertUtil.isEmpty(menberIds)) {
			return null;
		}
		return tagDAO.getTagRefListByMenberId(tagId, menberIds);
	}

	private void updateTagUserAndDeptTotal(TbQyTagInfoPO po, int addUserSize, int addDeptSize, List<TbQyTagRefPO> addList) throws Exception {
		if (!AssertUtil.isEmpty(addList)) {
			for (TbQyTagRefPO ref : addList) {
				if (TagStaticUtil.TAG_REF_TYPE_USER == ref.getMenberType()) {
					addUserSize --;
				}
				else {
					addDeptSize --;
				}
			}
		}
		updateTagUserAndDeptTotal(po, addUserSize, addDeptSize);
	}

	@Override
	public void updateTagUserAndDeptTotal(TbQyTagInfoPO po, int addUserSize, int addDeptSize) throws Exception {
		if (addUserSize != 0 || addDeptSize != 0) {
			int userCount = null == po.getUserCount() ? 0 : po.getUserCount();
			//最后的总数不能小于零
			if (addUserSize + userCount < 0) {
				addUserSize = -userCount;
			}
			int deptCount = null == po.getDeptCount() ? 0 : po.getDeptCount();
			if (addDeptSize + deptCount < 0) {
				addDeptSize = -deptCount;
			}
			tagDAO.updateTagUserAndDeptTotal(po.getId(), addUserSize, addDeptSize);
			po.setUserCount(userCount + addUserSize);
			po.setDeptCount(deptCount + addDeptSize);
		}
	}

	@Override
	public void updateTagUserOrDeptTotal(String[] tagIds, int addSize, int menberType) {
		if (AssertUtil.isEmpty(tagIds)) {
			return;
		}
		try {
			if (TagStaticUtil.TAG_REF_TYPE_USER == menberType) {
				tagDAO.updateTagUserTotal(tagIds, addSize);
			}
			else {
				tagDAO.updateTagDeptTotal(tagIds, addSize);
			}
		} catch (SQLException e) {
			ExceptionCenter.addException(e, "TagServiceImpl updateTagUserOrDeptTotal @sqh", WxqyhStringUtil.toString(tagIds));
		}
	}

	@Override
	public List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int tagRang, int status) throws Exception {
		return tagReadOnlyDAO.getTagPageInfoList(orgId, tagRang, status);
	}

	@Override
	public List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int status) throws Exception {
		return tagReadOnlyDAO.getTagPageInfoList(orgId, status);
	}

	private void getAddTagRefUser(TbQyTagInfoPO po, Set<String> addUserSet, List<TbQyTagRefPO> addList) throws Exception {
		if (AssertUtil.isEmpty(addUserSet)) {
			return;
		}
		List<TbQyTagRefPO> list = this.getTagRefListByMenberId(po.getId(), new ArrayList<String>(addUserSet), TagStaticUtil.TAG_REF_TYPE_USER);
		if (AssertUtil.isEmpty(list)) {
			for (TbQyTagRefPO ref : list) {
				addUserSet.remove(ref.getMenberId());
			}
		}
		TbQyTagRefPO tbQyTagRefPOClone = new TbQyTagRefPO();
		//先验证userId是否正确


		for (String userId : addUserSet) {
			if (!StringUtil.isNullEmpty(userId)) {
				TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
				tbQyTagRefPO.setCreateTime(po.getCreateTime());
				tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
				tbQyTagRefPO.setTagId(po.getId());
				tbQyTagRefPO.setMenberId(userId);
				tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_USER);
				addList.add(tbQyTagRefPO);
			}
		}
	}

	private void getAddTagRefDept(TbQyTagInfoPO po, Set<String> addDeptSet, List<TbQyTagRefPO> addList) throws Exception {
		if (AssertUtil.isEmpty(addDeptSet)) {
			return;
		}
		List<TbQyTagRefPO> list = this.getTagRefListByMenberId(po.getId(), new ArrayList<String>(addDeptSet), TagStaticUtil.TAG_REF_TYPE_DEPT);
		if (AssertUtil.isEmpty(list)) {
			for (TbQyTagRefPO ref : list) {
				addDeptSet.remove(ref.getMenberId());
			}
		}
		TbQyTagRefPO tbQyTagRefPOClone = new TbQyTagRefPO();
		if (AssertUtil.isEmpty(list)) {
			for (String userId : addDeptSet) {
				if (!StringUtil.isNullEmpty(userId)) {
					TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
					tbQyTagRefPO.setCreateTime(po.getCreateTime());
					tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
					tbQyTagRefPO.setTagId(po.getId());
					tbQyTagRefPO.setMenberId(userId);
					tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_DEPT);
					addList.add(tbQyTagRefPO);
				}
			}
		}
	}

	@Override
	public void updateTag(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String[] userIds) throws Exception, BaseException {
		TbQyTagInfoPO po = this.searchByPk(TbQyTagInfoPO.class, tbQyUserGroupPO.getGroupId());
		if (po == null) {
			this.addTag(userInfoVO, tbQyUserGroupPO, userIds);
		}
		else {
			boolean isUpdateName = !tbQyUserGroupPO.getGroupName().equals(po.getTagName());
			po.setTagName(tbQyUserGroupPO.getGroupName());
			po.setRang(TagStaticUtil.TAG_RANG_ALL);
			po.setStatus(1-Integer.parseInt(tbQyUserGroupPO.getStatus())); //群组0为启用，1为禁用
			po.setShowNum(tbQyUserGroupPO.getShowNum() == null ? TagStaticUtil.TAG_SHOW_NUM_DEFAULT : tbQyUserGroupPO.getShowNum());
			List<String> list = this.getTagRefMenberIdListByTagId(po.getId(), TagStaticUtil.TAG_REF_TYPE_DEPT);
			this.updateTag(userInfoVO, po, userIds, ListUtil.toArrays(list), isUpdateName);
		}
	}

	@Override
	public void updateTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateName) throws Exception, BaseException {
		if (StringUtil.isNullEmpty(po.getWxTagId())) {
			String tagId = WxTagUtil.createTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getTagName());
			po.setWxTagId(tagId);
			po.setSource(TagStaticUtil.TAG_INFO_SOURCE_QIWEI);

			//获取新增的用户或部门wxUserId
			List<String> addWxUserIds = null;
			if (!AssertUtil.isEmpty(userIds)) {
				addWxUserIds = contactService.getWxUserIdsByUserIds(ListUtil.toList(userIds));
			}
			List<String> addWxDeptIds = null;
			if (!AssertUtil.isEmpty(deptIds)) {
				addWxDeptIds = departmentService.getWxDeptIdsByIds(ListUtil.toList(deptIds));
			}
			//调用微信接口新增或者删除用户及部门
			WxTagUtil.addUserTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getWxTagId(), ListUtil.collToArrays(addWxUserIds), ListUtil.collToArrays(addWxDeptIds));

			this.updateTagRef(userInfoVO, po, userIds, deptIds, false);//需要更新到微信
		}
		else {
			this.updateTagRef(userInfoVO, po, userIds, deptIds, true);//需要更新到微信
			po.setDeptCount(null);
			po.setUserCount(null);
			if (isUpdateName) {
				WxTagUtil.updateTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getTagName(), po.getWxTagId());
			}
		}
		this.updatePO(po, false);
	}

	@Override
	public void updateTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateWeixin) throws Exception, BaseException {
		int length = 0;
		if (!AssertUtil.isEmpty(userIds)) {
			length += userIds.length;
		}
		if (!AssertUtil.isEmpty(deptIds)) {
			length += deptIds.length;
		}
		/**
		 * 更新标签信息
		 * 1、找到标签下的用户和部门数据
		 * 2、比较得到需要新增的用户部门和需要删除的用户部门并批量新增或删除
		 * 3、更新标签信息
		 */
		List<TbQyTagRefPO> list = this.getTagRefList(po.getId());
		List<TbQyTagRefPO> addList = new ArrayList<TbQyTagRefPO>(length);
		Map<String, String> userMap = null;
		Set<String> delUserSet = new HashSet<String>(0);
		Map<String, String> deptMap = null;
		Set<String> delDeptSet = new HashSet<String>(0);
		//已有用户数和部门数
		int hasUserNum = 0;
		int hasDeptNum = 0;
		if (!AssertUtil.isEmpty(list)) {
			userMap = new HashMap<String, String>(list.size());
			deptMap = new HashMap<String, String>(list.size());
			for (TbQyTagRefPO refPO : list) {
				if (TagStaticUtil.TAG_REF_TYPE_USER == refPO.getMenberType()) {
					userMap.put(refPO.getMenberId(), refPO.getId());
				}
				else {
					deptMap.put(refPO.getMenberId(), refPO.getId());
				}
			}
			hasUserNum = userMap.size();
			hasDeptNum = deptMap.size();
		}
		List<List<String>> resultUserIds = getUpdateTagRefUser(po, userMap, userIds, delUserSet, addList);
		List<List<String>> resultDeptIds = getUpdateTagRefDept(po, deptMap, deptIds, delDeptSet, addList);
		int addUserSize = 0;
		int addDeptSize = 0;
		if (null != resultUserIds.get(0)) {
			addUserSize = resultUserIds.get(0).size();
		}
		if (null != resultDeptIds.get(0)) {
			addDeptSize = resultDeptIds.get(0).size();
		}
		if (null != resultUserIds.get(1)) {
			addUserSize = addUserSize -resultUserIds.get(1).size();
		}
		if (null != resultDeptIds.get(1)) {
			addDeptSize = addDeptSize - resultDeptIds.get(1).size();
		}
		if (isUpdateWeixin) {
			addRefToWeiXin(userInfoVO, po, resultUserIds.get(0), resultDeptIds.get(0));
			delRefToWeiXin(userInfoVO, po, resultUserIds.get(1), resultDeptIds.get(1));
		}
		if (delUserSet.size() > 0) {
			this.batchDel(TbQyTagRefPO.class, ListUtil.collToArrays(delUserSet));
		}
		if (delDeptSet.size() > 0) {
			this.batchDel(TbQyTagRefPO.class, ListUtil.collToArrays(delDeptSet));
		}

		if (addList.size() > 0) {
			addList = QwtoolUtil.addBatchList(addList, false);
		}
		if (isUpdateWeixin) {
			this.updateTagUserAndDeptTotal(po, addUserSize, addDeptSize, addList);
		}
		else { //如果是微信同步，重新设置一下用户和部门总数
			po.setUserCount(hasUserNum + addUserSize);
			po.setDeptCount(hasDeptNum + addDeptSize);
		}
	}

	private List<List<String>> getUpdateTagRefUser (TbQyTagInfoPO po, Map<String, String> userMap, String[] userIds, Set<String> delUserSet, List<TbQyTagRefPO> addList) throws Exception {
		TbQyTagRefPO tbQyTagRefPOClone = new TbQyTagRefPO();
		List<List<String>> resultIds = new ArrayList<List<String>>(2);
		List<String> delUserIds = null;
		List<String> addUserIds = null;
		if (AssertUtil.isEmpty(userMap)) {
			if (!AssertUtil.isEmpty(userIds)) {
				addUserIds = new ArrayList<String>(userIds.length);
				for (String userId : userIds) {
					if (!StringUtil.isNullEmpty(userId)) {
						TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
						tbQyTagRefPO.setCreateTime(new Date());
						tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
						tbQyTagRefPO.setTagId(po.getId());
						tbQyTagRefPO.setMenberId(userId);
						tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_USER);
						addList.add(tbQyTagRefPO);
						addUserIds.add(userId);
					}
				}
			}
		}
		else {
			delUserSet.addAll(userMap.values());
			Set<String> delUserIdSet = userMap.keySet();
			if (!AssertUtil.isEmpty(userIds)) {
				addUserIds = new ArrayList<String>(userIds.length);
				for (String userId : userIds) {
					if (!StringUtil.isNullEmpty(userId)) {
						if (userMap.containsKey(userId)) { //如果人员在原数据内
							delUserSet.remove(userMap.get(userId)); //从待删除列表中清掉，剩下的就是需要删除的数据
							delUserIdSet.remove(userId);
						}
						else {
							TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
							tbQyTagRefPO.setCreateTime(new Date());
							tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
							tbQyTagRefPO.setTagId(po.getId());
							tbQyTagRefPO.setMenberId(userId);
							tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_USER);
							addList.add(tbQyTagRefPO);
							addUserIds.add(userId);
						}
					}
				}
			}
			delUserIds = new ArrayList<String>(delUserIdSet);
		}
		resultIds.add(addUserIds);
		resultIds.add(delUserIds);
		return resultIds;
	}
	private List<List<String>> getUpdateTagRefDept (TbQyTagInfoPO po, Map<String, String> userMap, String[] deptIds, Set<String> delDeptSet, List<TbQyTagRefPO> addList) throws Exception {
		TbQyTagRefPO tbQyTagRefPOClone = new TbQyTagRefPO();
		List<List<String>> resultIds = new ArrayList<List<String>>(2);
		List<String> delDeptIds = null;
		List<String> addDeptIds = null;
		if (AssertUtil.isEmpty(userMap)) {
			if (!AssertUtil.isEmpty(deptIds)) {
				addDeptIds = new ArrayList<String>(deptIds.length);
				for (String userId : deptIds) {
					if (!StringUtil.isNullEmpty(userId)) {
						TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
						tbQyTagRefPO.setCreateTime(new Date());
						tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
						tbQyTagRefPO.setTagId(po.getId());
						tbQyTagRefPO.setMenberId(userId);
						tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_DEPT);
						addList.add(tbQyTagRefPO);
						addDeptIds.add(userId);
					}
				}
			}
		}
		else {
			delDeptSet.addAll(userMap.values());
			Set<String> delDeptIdSet = userMap.keySet();
			if (!AssertUtil.isEmpty(deptIds)) {
				addDeptIds = new ArrayList<String>(deptIds.length);
				for (String userId : deptIds) {
					if (!StringUtil.isNullEmpty(userId)) {
						if (userMap.containsKey(userId)) { //如果人员在原数据内
							delDeptSet.remove(userMap.get(userId)); //从待删除列表中清掉，剩下的就是需要删除的数据
							delDeptIdSet.remove(userId);
						}
						else {
							TbQyTagRefPO tbQyTagRefPO = (TbQyTagRefPO) tbQyTagRefPOClone.clone();
							tbQyTagRefPO.setCreateTime(new Date());
							tbQyTagRefPO.setId(TagUtil.getTagRefMD5Id(po.getId(), userId));
							tbQyTagRefPO.setTagId(po.getId());
							tbQyTagRefPO.setMenberId(userId);
							tbQyTagRefPO.setMenberType(TagStaticUtil.TAG_REF_TYPE_DEPT);
							addList.add(tbQyTagRefPO);
							addDeptIds.add(userId);
						}
					}
				}
			}
			delDeptIds = new ArrayList<String>(delDeptIdSet);
		}
		resultIds.add(addDeptIds);
		resultIds.add(delDeptIds);
		return resultIds;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.service.ITagMgrService#getTagInfoByWxTagId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyTagInfoPO getTagInfoByWxTagId(String orgId, String wxTagId)
			throws Exception {
		return tagDAO.getTagInfoByWxTagId(orgId, wxTagId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.service.ITagMgrService#getTagRefList(java.lang.String)
	 */
	@Override
	public List<TbQyTagRefPO> getTagRefList(String tagId)
			throws Exception {
		return tagDAO.getTagRefList(tagId);
	}

	@Override
	public void updateTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, boolean isUpdateName) throws BaseException, Exception {
		if (StringUtil.isNullEmpty(po.getWxTagId())) { //如果还没创建到微信，需要创建，原有公共群组转过来的用户即存在这种情况
			String tagId = WxTagUtil.createTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getTagName());
			po.setWxTagId(tagId);
			po.setSource(TagStaticUtil.TAG_INFO_SOURCE_QIWEI);
			addRefToWeiXin(userInfoVO, po);
		}
		else if (isUpdateName) {
			WxTagUtil.updateTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getTagName(), po.getWxTagId());
		}
		this.updatePO(po, false);
	}

	@Override
	public List<TbQyTagRefPO> getTagRefListByIds(String tagId, String[] ids) throws Exception {
		return tagDAO.getTagRefListByIds(tagId, ids);
	}

	@Override
	public List<String> getTagRefMenberIdListByMenberIdList(String tagId, List<String> menberIds) throws Exception {
		if (AssertUtil.isEmpty(menberIds)) {
			return null;
		}
		return tagDAO.getTagRefMenberIdListByMenberIdList(tagId, menberIds);
	}

	@Override
	public boolean getHasTagRef(String tagId) throws Exception {
		return tagDAO.getHasTagRef(tagId) > 0;
	}

	@Override
	public void updateTagStatus(String tagId, int status) throws Exception, BaseException {
		TbQyTagInfoPO po = this.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		po = new TbQyTagInfoPO();
		po.setId(tagId);
		//状态存在异常
		if (TagStaticUtil.TAG_INFO_STATUS_DISABLE != status && TagStaticUtil.TAG_INFO_STATUS_USING != status) {
			throw new NonePrintException(ErrorCodeDesc.TAG_STATUS_ERROR.getCode(), ErrorCodeDesc.TAG_STATUS_ERROR.getDesc());
		}

		po.setStatus(status);
		this.updatePO(po, false);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.service.ITagMgrService#getTagInfoList(java.lang.String)
	 */
	@Override
	public List<TbQyTagInfoPO> getTagInfoList(String orgId) throws Exception {
		return tagDAO.getTagInfoList(orgId);
	}

	@Override
	public void delAllTagRef(String orgId, String[] menberIds, int menberType) throws Exception, BaseException {
		List<TbQyTagRefPO> list = this.getTagRefPOByMenberIds(orgId, menberIds, menberType);
		if (!AssertUtil.isEmpty(list)) {
			String[] ids = new String[list.size()];
			String[] tagIds = new String[list.size()];
			TbQyTagRefPO po;
			for (int i=0; i<list.size(); i++) {
				po = list.get(i);
				ids[i] = po.getId();
				tagIds[i] = po.getTagId();
			}
			this.updateTagUserOrDeptTotal(tagIds, -1, menberType);
			super.batchDel(TbQyTagRefPO.class, ids);
		}
	}



	@Override
	public List<String> getTagrefIdByMenberIds(String orgId, String[] menberIds, int menberType) throws Exception {
		if (AssertUtil.isEmpty(menberIds)) {
			return null;
		}
		return tagDAO.getTagrefIdByMenberIds(menberIds, menberType);
	}

	@Override
	public void delTagAndRef(UserOrgVO user, String[] ids) throws BaseException, Exception {
		List<TbQyTagInfoPO> list = this.searchByPks(TbQyTagInfoPO.class, ids);
		if (AssertUtil.isEmpty(list)) {
			return;
		}
		for (TbQyTagInfoPO po : list) {
			delTagRef(user, po, getTagRefList(po.getId()));
			if (!StringUtil.isNullEmpty(po.getWxTagId())) {
				WxTagUtil.deleteTag(user.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getWxTagId());
			}
		}
		batchDel(TbQyTagInfoPO.class, ids);
	}

	@Override
	public List<TbQyTagRefPO> getTagRefPOByMenberIds(String orgId, String[] menberIds, int menberType) throws Exception {
		if (AssertUtil.isEmpty(menberIds)) {
			return null;
		}
		return tagDAO.getTagRefPOByMenberIds(menberIds, menberType);
	}

	@Override
	public List<TbQyTagRefPO> getTagRefListByTagIds(List<String> tagIdList) throws Exception {
		if (AssertUtil.isEmpty(tagIdList)) {
			return null;
		}
		return tagDAO.getTagRefListByTagIds(tagIdList);
	}

	@Override
	public int checkSyncTimeAndAdd(UserOrgVO org, boolean vip) throws Exception, BaseException {
		int currentDateInt = Integer.parseInt(DateUtil.formartCurrentDate().replace("-",""));
		TbQyTagSyncTimePO po = tagReadOnlyDAO.searchByPk(TbQyTagSyncTimePO.class, org.getOrgId());
		if (po == null) {
			po = new TbQyTagSyncTimePO();
			po.setId(org.getOrgId());
			po.setSyncDate(currentDateInt);
			po.setSyncTime(1);
			tagDAO.insert(po);
		}
		else if (po.getSyncDate().intValue() != currentDateInt) {
			po.setSyncDate(currentDateInt);
			po.setSyncTime(1);
			tagDAO.update(po, false);
		}
		else if (po.getSyncTime().intValue() < TagStaticUtil.TAG_SYNC_TIME) {//未超过次数限制
			tagDAO.addSyncTime(org.getOrgId());
			po.setSyncTime(po.getSyncTime() + 1);
		}
		else if (!vip) { //超过了限制
			throw new NonePrintException(ErrorCodeDesc.TAG_SYNC_OUT.getCode(), ErrorCodeDesc.TAG_SYNC_OUT.getDesc() + VipUtil.getHowToBeVipMsg(org.getCorpId()));
		}
		else if (po.getSyncTime().intValue() >= TagStaticUtil.TAG_SYNC_TIME_VIP) { //vip用户超过了限制
			throw new NonePrintException(ErrorCodeDesc.TAG_SYNC_OUT_VIP.getCode(), ErrorCodeDesc.TAG_SYNC_OUT_VIP.getDesc());
		}
		else {
			//vip用户未超过限制
			tagDAO.addSyncTime(org.getOrgId());
			po.setSyncTime(po.getSyncTime() + 1);
		}
		if (vip) {
			return TagStaticUtil.TAG_SYNC_TIME_VIP - po.getSyncTime();
		}
		else {
			return TagStaticUtil.TAG_SYNC_TIME - po.getSyncTime();
		}
	}

	@Override
	public List<QyTagRefPageVO> getTagRefPageVOList(String tagId) throws Exception {
		return tagReadOnlyDAO.getTagRefPageVOList(tagId);
	}

	@Override
	public List<String> getTagRefMenberIdListByTagId(String tagId, int menberType) throws Exception {
		return tagDAO.getTagRefMenberIdListByTagId(tagId, menberType);
	}

	@Override
	public List<String> getTagRefMenberIdListByTagIds(List<String> tagIds, int menberType) throws Exception {
		if (AssertUtil.isEmpty(tagIds)) {
			return null;
		}
		return tagReadOnlyDAO.getTagRefMenberIdListByTagIds(tagIds, menberType);
	}

	@Override
	public List<String> getTagIdsByWxTagIds(String orgId, String[] wxTagIds) throws Exception {
		if (AssertUtil.isEmpty(wxTagIds)) {
			return null;
		}
		return tagReadOnlyDAO.getTagIdsByWxTagIds(orgId, wxTagIds);
	}
}
