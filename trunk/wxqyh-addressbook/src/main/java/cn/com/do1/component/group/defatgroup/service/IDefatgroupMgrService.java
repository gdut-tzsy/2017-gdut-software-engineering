package cn.com.do1.component.group.defatgroup.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;

import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface IDefatgroupMgrService extends IBaseService {
	
	Pager searchDefatgroup(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 列表查询
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @author Chen Feixiong 2014-11-24
	 */
	Pager ajaxPageSearch(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 新增
	 * 
	 *
	 * @param userInfoVO
	 * @param tbQyUserGroupPO
	 * @param userIds
	 * @author Chen Feixiong 2014-11-25
	 */
	void ajaxAdd(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String userIds) throws Exception, BaseException;

	/**
	 * 根据Id查询群组人员
	 * 
	 * @param id
	 * @return
	 * @author Chen Feixiong 2014-11-25
	 */
	List<TbQyUserGroupPersonVO> getUserGroupPerson(String id) throws Exception, BaseException;

	/**
	 * 更新群组人员
	 * 
	 *
	 * @param userInfoVO
	 * @param tbQyUserGroupPO
	 * @param userIds
	 * @author Chen Feixiong 2014-11-25
	 */
	void ajaxUpdate(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String userIds) throws Exception, BaseException;

	/**
	 * 删除群组
	 * 
	 *
	 * @param user
	 * @param ids
	 * @author Chen Feixiong 2014-11-25
	 */
	void ajaxBatchDelete(UserOrgVO user, String[] ids) throws Exception, BaseException;

	/**
	 * 更新群组状态
	 * 
	 * @param id
	 * @param status
	 * @author Chen Feixiong 2014-11-25
	 */
	void updateStatus(String id, String status) throws Exception, BaseException;

	/**
	 * 查询群组列表
	 * 
	 * @param orgId
	 * @return
	 * @author Hejinjiao
	 * @2014-12-23
	 * @version 1.0
	 */
	List<TbQyUserGroupPO> getGroupList(String orgId) throws Exception, BaseException;

	/**
	 * 分页查询组成员
	 * 
	 * @param searchMap
	 *            包括：群组ID
	 * @param pager
	 * @return
	 * @author Hejinjiao
	 * @2014-12-31
	 * @version 1.0
	 */
	Pager pageGroupUsers(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 选择群租的人
	 * 
	 * @param id
	 *            格式：选择群租的ID
	 * @return
	 * @author Hejinjiao
	 * @2015-1-4
	 * @version 1.0
	 */
	List<TbQyUserGroupPersonVO> getUserByGroups(String id) throws Exception, BaseException;


	/**
	 * 更新任务模版默认的负责人
	 * 
	 * @param userIds
	 *            以|隔开
	 * @param creator
	 *            创建人
	 * @param orgId
	 *            创建机构
	 * @param type类型
	 *            0负责 1相关
	 * @param foreignId
	 *            关联id
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-7
	 * @version 1.0
	 */
	void updateGivenPO(String userIds, String creator, String orgId, String foreignId, String type) throws Exception, BaseException;


	/**
	 * 判断群组是否重名
	 * 
	 * @param groupName
	 *            群组名称
	 * @param orgId
	 *            机构Id
	 * @return
	 * @author Hejinjiao
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserGroupPO> findDefatgroup(String groupName, String orgId) throws Exception, BaseException;
	
	/** 
	 * 根据用户id以及orgId,应用类型删除相关负责人
	 * @param userId   用户id
	 * @param orgId   机构id
	 * @param applyType   应用code
	 * @param childApplyType   子应用code 没有自应用可为null
	 * @return
	 * @author tanwq
	 * @2015-7-1
	 * @version 1.0
	 */
	void delTbQyOldGivenVOByUserId(String userId,String orgId,String type,String applyType,String childApplyType)throws Exception, BaseException;
	
	/**
	 * 重新统计机构的公共群组成员总数
	 * @param orgId
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2015-11-25
	 */
	void updateUserGroupSum(String orgId)throws Exception,BaseException;
	/**
	 * 删除常用联系人群组的联系人
	 * @param ids
	 * @author chenfeixiong
	 * 2014-8-21
	 */
	void batchDeleteGroup(String[] ids)throws Exception, BaseException;/**
	 * 查询常用联系人群组
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	List<TbQyUserGroupPO> getUserGroup(Map<String,Object> map)throws Exception, BaseException;
	
	Pager  getUserGroup(Pager pager,Map<String,Object> map)throws Exception,BaseException;
	
	/**
	 * 获取常用联系人群组人员
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-21
	 */
	List<TbQyUserGroupPersonVO> getUserGroupPerson(Map<String, Object> map)throws Exception, BaseException;
	
	Pager getUserGroupPerson(Pager pager,Map<String,Object> map)throws Exception,BaseException;

	/**
	 * 更新用户的默认负责人群组或相关人群组
	 * @param userId
	 * @param type
	 * @author Chen Feixiong
	 * 2014-11-14
	 */
	void updateGroupByUserID(String userId, String type)throws Exception, BaseException;

	/**
	 * 根据机构ID获取默认群组
	 * @param orgId
	 * @return
	 * @author Chen Feixiong
	 * 2014-11-25
	 */
	List<TbQyUserGroupPO> getUserGroup(String orgId)throws Exception, BaseException;
	
	Integer countUserGroupByUserIdAndOrgId(String userId,String orgId) throws Exception, BaseException;

}
