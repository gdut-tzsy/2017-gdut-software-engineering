package cn.com.do1.component.group.defatgroup.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPersonPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.group.defatgroup.dao.*;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;
import cn.com.do1.component.defatgroup.defatgroup.model.TbQyGivenPO;
import cn.com.do1.component.defatgroup.defatgroup.model.TbQyOldGivenPO;
import cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyGivenVO;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyOldGivenVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.UUID32;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
@Service("defatgroupService")
public class DefatgroupServiceImpl extends BaseService implements IDefatgroupService,IDefatgroupMgrService {
	private final static transient Logger logger = LoggerFactory.getLogger(DefatgroupServiceImpl.class);

	/**
	 * 默认负责人
	 */
	private final static String TYPE_0 = "0";
	/**
	 * 默认相关人
	 */
	private final static String TYPE_1 = "1";
	
	private IDefatgroupDAO defatgroupDAO;
	
	private IContactService contactService;
	@Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	@Resource
	public void setDefatgroupDAO(IDefatgroupDAO defatgroupDAO) {
		this.defatgroupDAO = defatgroupDAO;
		setDAO(defatgroupDAO);
	}
	@Resource(name = "tagService")
	private ITagMgrService tagService;

	public Pager searchDefatgroup(Map searchMap, Pager pager) throws Exception, BaseException {
		return defatgroupDAO.pageSearchByField(IBaseDBVO.class, searchMap, null, pager);
	}
	
	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#ajaxPageSearch(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager ajaxPageSearch(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
		return defatgroupDAO.ajaxPageSearch(searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#ajaxAdd(cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO, java.lang.String)
	 */
	@Override
	public void ajaxAdd(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String userIds) throws Exception, BaseException {
		String temp[] = userIds.split("\\|");
		TbQyUserGroupPersonPO tbQyUserGroupPersonPO = null;
		int count = 0;
		for (String str : temp) {
			tbQyUserGroupPersonPO = new TbQyUserGroupPersonPO();
			tbQyUserGroupPersonPO.setCreateTime(tbQyUserGroupPO.getCreateTime());
			tbQyUserGroupPersonPO.setGroupId(tbQyUserGroupPO.getGroupId());
			tbQyUserGroupPersonPO.setUserId(str.trim());
			this.insertPO(tbQyUserGroupPersonPO, true);
			count++;
		}
		tbQyUserGroupPO.setSum(count);
		tbQyUserGroupPO = this.insertPO(tbQyUserGroupPO, false);
		tagService.addTag(userInfoVO, tbQyUserGroupPO, temp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#getUserGroupPerson(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getUserGroupPerson(String id) throws Exception, BaseException {
		return defatgroupDAO.getUserGroupPerson(id);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#ajaxUpdate(cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO, java.lang.String)
	 */
	@Override
	public void ajaxUpdate(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String userIds) throws Exception, BaseException {
		/**
		 * 更新群组信息
		 * 1、找到群组下的用户数据
		 * 2、比较得到需要新增的用户和需要删除的用户并批量新增或删除
		 * 3、更新群组信息
		 * 4、同时同步更新标签信息
		 */
		String temp[] = userIds.split("\\|");
		tbQyUserGroupPO.setSum(temp.length);
		TbQyUserGroupPersonPO tbQyUserGroupPersonPOClone = new TbQyUserGroupPersonPO();
		List<TbQyUserGroupPersonVO> list = this.getUserGroupPerson(tbQyUserGroupPO.getGroupId());
		List<TbQyUserGroupPersonPO> addList = new ArrayList<TbQyUserGroupPersonPO>(temp.length);
		if (!AssertUtil.isEmpty(list)) {
			Map<String, String> map = new HashMap<String, String>(list.size());
			Set<String> delSet = new HashSet<String>(list.size());
			for (TbQyUserGroupPersonVO vo : list) {
				map.put(vo.getUserId(), vo.getId());
				delSet.add(vo.getId());
			}
			for (String userId : temp) {
				if (!StringUtil.isNullEmpty(userId)) {
					if (map.containsKey(userId)) { //如果人员在原数据内
						delSet.remove(map.get(userId)); //从待删除列表中清掉，剩下的就是需要删除的数据
					}
					else {
						TbQyUserGroupPersonPO tbQyUserGroupPersonPO = (TbQyUserGroupPersonPO) tbQyUserGroupPersonPOClone.clone();
						tbQyUserGroupPersonPO.setCreateTime(tbQyUserGroupPO.getCreateTime());
						tbQyUserGroupPersonPO.setGroupId(tbQyUserGroupPO.getGroupId());
						tbQyUserGroupPersonPO.setUserId(userId.trim());
						tbQyUserGroupPersonPO.setId(UUID32.getID());
						addList.add(tbQyUserGroupPersonPO);
					}
				}
			}
			if (delSet.size() > 0) {
				this.batchDel(TbQyUserGroupPersonPO.class, ListUtil.collToArrays(delSet));
			}
		}
		else {
			for (String str : temp) {
				if (!StringUtil.isNullEmpty(str)) {
					TbQyUserGroupPersonPO tbQyUserGroupPersonPO = (TbQyUserGroupPersonPO) tbQyUserGroupPersonPOClone.clone();
					tbQyUserGroupPersonPO.setCreateTime(tbQyUserGroupPO.getCreateTime());
					tbQyUserGroupPersonPO.setGroupId(tbQyUserGroupPO.getGroupId());
					tbQyUserGroupPersonPO.setUserId(str.trim());
					tbQyUserGroupPersonPO.setId(UUID32.getID());
					addList.add(tbQyUserGroupPersonPO);
				}
			}
		}

		if (addList.size() > 0) {
			QwtoolUtil.addBatchList(addList, true);
		}
		this.updatePO(tbQyUserGroupPO, true);

		tagService.updateTag(userInfoVO, tbQyUserGroupPO, temp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#ajaxBatchDelete(java.lang.String[])
	 */
	@Override
	public void ajaxBatchDelete(UserOrgVO user, String[] ids) throws Exception, BaseException {
		tagService.delTagAndRef(user, ids);
		// 删除群组联系人
		defatgroupDAO.batchDeleteGroup(ids);
		// 删除群组
		this.batchDel(TbQyUserGroupPO.class, ids);
	}


	public IDefatgroupDAO getDefatgroupDAO() {
		return defatgroupDAO;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#updateRoomStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateStatus(String id, String status) throws Exception, BaseException {
		defatgroupDAO.updateStatus(id, status);
		tagService.updateTagStatus(id, 1-Integer.parseInt(status));
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#getGroupList(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> getGroupList(String orgId) throws Exception, BaseException {

		return defatgroupDAO.getGroupList(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#pageGroupUsers(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager pageGroupUsers(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
		if (AssertUtil.isEmpty(searchMap.get("id"))) {
			pager.setTotalPages(0);
			pager.setTotalRows(0);
			return pager;
		}
		return defatgroupDAO.pageGroupUsers(searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#getUserByGroups(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getUserByGroups(String id) throws Exception, BaseException {
		return defatgroupDAO.getUserByGroups(id);
	}

	/**
	 * 任务模版添加默认的负责人
	 * 
	 * @param userIds
	 * @author chenfeixiong 2014-7-28
	 */
	@Override
	public void insertGivenPO(String userIds, String creator, String orgId, String foreignId,String type) throws Exception, BaseException {
		if (!StringUtil.isNullEmpty(userIds)) {
			String users[] = userIds.split("\\|");
			int sort = 0;
			TbQyUserInfoVO userVO = null;
			for (String userId : users) {
				if (!StringUtil.isNullEmpty(userId)) {
					userVO = contactService.findUserInfoByUserId(userId);
					if (null != userVO) {//防止用户离职
						TbQyGivenPO tbQyTaskGivenPO = new TbQyGivenPO();
						tbQyTaskGivenPO.setOrgId(orgId);
						tbQyTaskGivenPO.setCreateTime(new Date());
						tbQyTaskGivenPO.setRecId(userVO.getUserId());
						tbQyTaskGivenPO.setCreatePerson(creator);
						tbQyTaskGivenPO.setTemplateId(foreignId);
						tbQyTaskGivenPO.setSort(sort);
						tbQyTaskGivenPO.setPersonName(userVO.getPersonName());
						tbQyTaskGivenPO.setHeadPic(userVO.getHeadPic());
						tbQyTaskGivenPO.setDepartmentName(userVO.getDepartmentName());
						tbQyTaskGivenPO.setWxUserId(userVO.getWxUserId());
						tbQyTaskGivenPO.setType(type);
						this.insertPO(tbQyTaskGivenPO, true);
						sort++;
					}
				}
			}
		}
	}

	/**
	 * 查询任务模版负责人
	 * 
	 * @author chenfeixiong 2014-7-28
	 */
	@Override
	public List<TbQyGivenVO> getGivenByForeignId(String foreignId, String orgId) throws Exception, BaseException {
		return defatgroupDAO.getGivenByForeignId(foreignId, orgId);
	}

	@Override
	public void updateGivenPO(String userIds, String creator, String orgId, String foreignId,String type) throws Exception, BaseException {
		deleteGivenPO(foreignId, orgId);
		insertGivenPO(userIds, creator, orgId, foreignId,type);
	}

	@Override
	public void deleteGivenPO(String foreignId, String orgId) throws Exception, BaseException {
		defatgroupDAO.deleteGivenPO(foreignId, orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#findDefatgroup(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> findDefatgroup(String groupName, String orgId) throws Exception, BaseException {

		return defatgroupDAO.findDefatgroup(groupName, orgId);
	}

	@Override
	public List<TbQyGivenVO> getGivenByFormId(String foreignId, String orgId,String type) throws Exception, BaseException {
		// TODO Auto-generated method stub
		return this.defatgroupDAO.getGivenByFormId(foreignId, orgId,type);
	}

	@Override
	public List<TbQyUserGroupPO> findQyUserGroupInfoByIds(String[] groupIds) throws Exception, BaseException {
		// TODO Auto-generated method stub
		if (null == groupIds || groupIds.length == 0)
			return new ArrayList<TbQyUserGroupPO>();
		int size = groupIds.length;
		if (size <= Configuration.SQL_IN_MAX)
			return defatgroupDAO.findQyUserGroupInfoByIds(groupIds);

		List<TbQyUserGroupPO> groupList = new ArrayList<TbQyUserGroupPO>(size);
		String[] newGroupIds = new String[200];
		int startIndex = 0;
		while (startIndex + Configuration.SQL_IN_MAX <= size) {
			System.arraycopy(groupIds, startIndex, newGroupIds, 0, Configuration.SQL_IN_MAX);
			groupList.addAll(defatgroupDAO.findQyUserGroupInfoByIds(newGroupIds));
			startIndex += Configuration.SQL_IN_MAX;
		}
		if (startIndex < size) {
			newGroupIds = new String[size - startIndex];
			System.arraycopy(groupIds, startIndex, newGroupIds, 0, size - startIndex);
			groupList.addAll(defatgroupDAO.findQyUserGroupInfoByIds(newGroupIds));
		}
		return groupList;
	}

	@Override
	public List<TbQyOldGivenVO> findTbQyOldGivenVOByUserId(String userId, String orgId,String type,String applyType,String childApplyType) throws Exception, BaseException {
		// TODO Auto-generated method stub
		List<TbQyOldGivenVO> returnList=new ArrayList<TbQyOldGivenVO>();
		//搜索相关人和负责人
		List<TbQyOldGivenVO> givenlist=this.defatgroupDAO.findTbQyOldGivenVOByUserId(userId, orgId,type,applyType,childApplyType);
		//判断人员是否离职或删除
		if(null!=givenlist && givenlist.size()>0){
			String userIds="";
			for(int i=0;i<givenlist.size();i++){
				if(i<givenlist.size()-1){
					userIds=userIds+"'"+givenlist.get(i).getUserId()+"'"+",";
				}else{
					userIds=userIds+"'"+givenlist.get(i).getUserId()+"'";
				}
			}
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("orgId", orgId);
			params.put("leaveStatus", "-1");//查找不属于离职的人员
			List<UserInfoVO> userList=contactService.getUserInfoVOInUserIds(userIds, params);
			//list转map
			Map<String,Object> userMap=new HashMap<String,Object>();
			for(UserInfoVO vo:userList){
				userMap.put(vo.getUserId(), vo);
			}
			//判断对象是否存在
			for(TbQyOldGivenVO vo:givenlist){
				if(!AssertUtil.isEmpty(userMap.get(vo.getUserId()))){
					returnList.add(vo);
				}
			}
		}
		return returnList;
	}

	@Override
	public void addOldGivenInfo(String incharges,String type,String createUserId,String orgId,String applyType,String childApplyType) throws Exception, BaseException {
		//删除原来的上一次记录
		this.delTbQyOldGivenVOByUserId(createUserId, orgId,type, applyType, childApplyType);
		
		Set<String> set = new HashSet<String>();// 过滤重复
		if (!StringUtil.isNullEmpty(incharges)) {
			String[] personlist = incharges.split(",");
			int i = 0;
			TbQyUserInfoVO userVO;
			
			List<TbQyOldGivenPO> receiveList = new ArrayList<TbQyOldGivenPO>();
			TbQyOldGivenPO pocl=new TbQyOldGivenPO();
			for (String person : personlist) {
				if (StringUtil.isNullEmpty(person)) {
					continue;
				}
				person = person.trim();
				if (!set.add(person)) {
					continue;
				}
				userVO = contactService.findUserInfoByUserId(person);
				if (userVO == null || !orgId.equals(userVO.getOrgId())) {
					continue;
				}
				if ("-1".equals(userVO.getUserStatus())) {// 离职
					continue;
				}
				TbQyOldGivenPO po = (TbQyOldGivenPO) pocl.clone();
				po.setGivenId(UUID.randomUUID().toString());
				po.setCreatePerson(createUserId);
				po.setOrgId(orgId);
				po.setRecId(userVO.getUserId());
				po.setCreateTime(new Date());
				po.setType(type);
				po.setSort(i);
				po.setCreateTime(new Date());
				po.setPersonName(userVO.getPersonName());
				po.setHeadPic(userVO.getHeadPic());
				po.setDepartmentName(userVO.getDepartmentName());
				po.setWxUserId(userVO.getWxUserId());
				po.setApplyType(applyType);
				po.setChildApplyType(childApplyType);
				receiveList.add(po);
				i++;
			}
			if (!AssertUtil.isEmpty(receiveList))
				this.defatgroupDAO.execBatchInsert(receiveList);
		}
	}

	@Override
	public void delTbQyOldGivenVOByUserId(String userId, String orgId,String type,String applyType,String childApplyType) throws Exception, BaseException {
		// TODO Auto-generated method stub
		this.defatgroupDAO.delTbQyOldGivenVOByUserId(userId, orgId,type,applyType,childApplyType);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#insertUsersAndGivenPO(java.util.Map)
	 */
	@Override
	public void insertUsersAndGivenPO(Map<String, Object> condationMap)
			throws Exception, BaseException {
		String userIds = (String) condationMap.get("userIds");
		String orgId = (String) condationMap.get("orgId");
		String creator = (String) condationMap.get("creator");
		String foreignId = (String) condationMap.get("foreignId");
		String type = (String) condationMap.get("type");
		String givenUserIds = (String) condationMap.get("givenUserIds");
		TbQyGivenPO tbQyTaskGivenPOPosc = new TbQyGivenPO();
		//默认负责人
		if (!StringUtil.isNullEmpty(userIds)) {
			String users[] = userIds.split("\\|");
			int sort = 0;
			TbQyUserInfoVO userVO = null;
			for (String userId : users) {
				if (!StringUtil.isNullEmpty(userId)) {
					userVO = contactService.findUserInfoByUserId(userId);
					if (null != userVO) {//防止用户离职
						TbQyGivenPO tbQyTaskGivenPO = (TbQyGivenPO) tbQyTaskGivenPOPosc.clone();
						tbQyTaskGivenPO.setOrgId(orgId);
						tbQyTaskGivenPO.setCreateTime(new Date());
						tbQyTaskGivenPO.setRecId(userVO.getUserId());
						tbQyTaskGivenPO.setCreatePerson(creator);
						tbQyTaskGivenPO.setTemplateId(foreignId);
						tbQyTaskGivenPO.setSort(sort);
						tbQyTaskGivenPO.setPersonName(userVO.getPersonName());
						tbQyTaskGivenPO.setHeadPic(userVO.getHeadPic());
						tbQyTaskGivenPO.setDepartmentName(userVO.getDepartmentName());
						tbQyTaskGivenPO.setWxUserId(userVO.getWxUserId());
						tbQyTaskGivenPO.setType(TYPE_0);//负责人
						this.insertPO(tbQyTaskGivenPO, true);
						sort++;
					}
				}
			}
		}
		
		//默认相关人
		if (!StringUtil.isNullEmpty(givenUserIds)) {
			String gusers[] = givenUserIds.split("\\|");
			int sort = 0;
			TbQyUserInfoVO userVO = null;
			for (String userId : gusers) {
				if (!StringUtil.isNullEmpty(userId)) {
					userVO = contactService.findUserInfoByUserId(userId);
					if (null != userVO) {//防止用户离职
						TbQyGivenPO tbQyTaskGivenPO = (TbQyGivenPO) tbQyTaskGivenPOPosc.clone();
						tbQyTaskGivenPO.setOrgId(orgId);
						tbQyTaskGivenPO.setCreateTime(new Date());
						tbQyTaskGivenPO.setRecId(userVO.getUserId());
						tbQyTaskGivenPO.setCreatePerson(creator);
						tbQyTaskGivenPO.setTemplateId(foreignId);
						tbQyTaskGivenPO.setSort(sort);
						tbQyTaskGivenPO.setPersonName(userVO.getPersonName());
						tbQyTaskGivenPO.setHeadPic(userVO.getHeadPic());
						tbQyTaskGivenPO.setDepartmentName(userVO.getDepartmentName());
						tbQyTaskGivenPO.setWxUserId(userVO.getWxUserId());
						tbQyTaskGivenPO.setType(TYPE_1);
						this.insertPO(tbQyTaskGivenPO, true);
						sort++;
					}
				}
			}
		}		
	}
	
	@Override
	public void updateUsersAndGivenPO(Map<String, Object> condationMap) throws Exception, BaseException {
		String orgId = (String) condationMap.get("orgId");
		String foreignId = (String) condationMap.get("foreignId");
		deleteGivenPO(foreignId, orgId);
		insertUsersAndGivenPO(condationMap);
	}

	@Override
	public void updateUserGroupSum(String orgId) throws Exception,
			BaseException {
		defatgroupDAO.updateUserGroupSum(orgId);
	}

	@Override
	public void batchDeleteGroup(String[] ids) throws Exception, BaseException {
		defatgroupDAO.batchDeleteGroup(ids);
	}
	
	@Override
	public List<TbQyUserGroupPO> getUserGroup(Map<String, Object> map) throws Exception, BaseException {
		return defatgroupDAO.getUserGroup(map);
	}

	@Override
	public List<TbQyUserGroupPersonVO> getUserGroupPerson(Map<String, Object> map) throws Exception, BaseException {
		return defatgroupDAO.getUserGroupPerson(map);
	}

	@Override
	public Pager getUserGroup(Pager pager, Map<String, Object> map) throws Exception, BaseException {
		return defatgroupDAO.getUserGroup(pager, map);
	}

	@Override
	public Pager getUserGroupPerson(Pager pager, Map<String, Object> map) throws Exception, BaseException {
		if (AssertUtil.isEmpty(map.get("groupId"))) {
			pager.setTotalPages(0);
			pager.setTotalRows(0);
			return pager;
		}
		return defatgroupDAO.getUserGroupPerson(pager, map);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IContactService#getUserGroup(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception, BaseException {
		return defatgroupDAO.getUserGroup(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IContactService#updateGroupByUserID(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateGroupByUserID(String userId, String type) throws Exception, BaseException {
		defatgroupDAO.updateGroupByUserID(userId, type);

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IContactService#getGroupPersonByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getGroupPersonByUserId(String userId, String type) throws Exception, BaseException {
		return defatgroupDAO.getGroupPersonByUserId(userId, type);
	}
	@Override
	public List<TbQyUserInfoVO> findUserInfoByGroupIds(String[] groupIds) throws Exception, BaseException {
		if (null == groupIds || groupIds.length == 0)
			return new ArrayList<TbQyUserInfoVO>();

		int size = groupIds.length;
		if (size <= Configuration.SQL_IN_MAX)
			return defatgroupDAO.findUserInfoByGroupIds(groupIds);

		List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(size);
		String[] newGroupIds = new String[200];
		int startIndex = 0;
		while (startIndex + Configuration.SQL_IN_MAX <= size) {
			System.arraycopy(groupIds, startIndex, newGroupIds, 0, Configuration.SQL_IN_MAX);
			list.addAll(defatgroupDAO.findUserInfoByGroupIds(newGroupIds));
			startIndex += Configuration.SQL_IN_MAX;
		}
		if (startIndex < size) {
			newGroupIds = new String[size - startIndex];
			System.arraycopy(groupIds, startIndex, newGroupIds, 0, size - startIndex);
			list.addAll(defatgroupDAO.findUserInfoByGroupIds(newGroupIds));
		}
		return list;
	}

	@Override
	public Integer countUserInfoByGroupId(String groupId) throws Exception, BaseException {
		return defatgroupDAO.countUserInfoByGroupId(groupId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService#countUserGroupByUserIdAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer countUserGroupByUserIdAndOrgId(String userId, String orgId)
			throws Exception, BaseException {
		return defatgroupDAO.countUserGroupByUserIdAndOrgId(userId, orgId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService#getNotLevelGivenByForeignId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyGivenVO> getNotLevelGivenByForeignId(String foreignId,
			String orgId, String type) throws Exception, BaseException {
		return defatgroupDAO.getNotLevelGivenByForeignId(foreignId, orgId, type);
	}

	@Override
	public List<String> getUserIdsByGroupId(String groupId) throws SQLException {
		return defatgroupDAO.getUserIdsByGroupId(groupId);
	}
}
