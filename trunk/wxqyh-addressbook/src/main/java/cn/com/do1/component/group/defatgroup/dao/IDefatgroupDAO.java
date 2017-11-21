package cn.com.do1.component.group.defatgroup.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyGivenVO;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyOldGivenVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface IDefatgroupDAO extends IBaseDAO {

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
	 * 根据群组ID获取群组人员
	 *
	 * @param id
	 * @return
	 * @author Chen Feixiong 2014-11-25
	 */
	List<TbQyUserGroupPersonVO> getUserGroupPerson(String id) throws Exception, BaseException;

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
	 * @param foreignId
	 * @param orgId
	 * @author Sun Qinghai
	 * @2015-1-7
	 * @version 1.0
	 */
	void deleteGivenPO(String foreignId, String orgId) throws Exception;

	/**
	 * @param foreignId
	 * @param orgId
	 * @return
	 * @author Sun Qinghai
	 * @2015-1-7
	 * @version 1.0
	 */
	List<TbQyGivenVO> getGivenByForeignId(String foreignId, String orgId) throws Exception, BaseException;

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
	 * 优化获取默认负责人方法，由关联查询改成单表查询
	 *
	 * @param formId
	 * @param orgId
	 * @return
	 * @author tanwq
	 * @2015-4-20
	 * @version 1.0
	 */
	List<TbQyGivenVO> getGivenByFormId(String foreignId, String orgId,String type) throws Exception, BaseException;

	/**根据用户组ids查询信息 by tanwq 2015-5-26**/
	List<TbQyUserGroupPO> findQyUserGroupInfoByIds(String [] groupIds)throws Exception, BaseException;

	/**根据用户id以及orgId,type,应用类型获取上次填写的相关负责人 by tanwq 2015-7-1***/
	List<TbQyOldGivenVO> findTbQyOldGivenVOByUserId(String userId,String orgId,String type,String applyType,String childApplyType)throws Exception, BaseException;
	/**根据用户id以及orgId,应用类型 删除相关负责人 by tanwq 2015-7-1***/
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
	 * 删除常用联系人群组人员
	 *
	 * @param ids
	 * @author chenfeixiong 2014-8-21
	 */
	void batchDeleteGroup(String[] ids) throws Exception, BaseException;

	/**
	 * 更新用户的默认负责人群组或相关人群组
	 *
	 * @param userId
	 * @param type
	 * @return
	 * @author Chen Feixiong 2014-11-14
	 */
	void updateGroupByUserID(String userId, String type) throws Exception, BaseException;

	/**
	 * 根据userId获取用户的默认负责人群组或相关人群组
	 *
	 * @param userId
	 * @param type
	 * @return
	 * @author Chen Feixiong 2014-11-14
	 */
	List<TbQyUserGroupPersonVO> getGroupPersonByUserId(String userId, String type) throws Exception, BaseException;

	/**
	 * 查询常用联系人群组
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-20
	 */
	List<TbQyUserGroupPO> getUserGroup(Map<String, Object> map) throws Exception, BaseException;

	Pager getUserGroup(Pager pager, Map<String, Object> map) throws Exception, BaseException;

	/**
	 * 获取常用联系人群组人员
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-21
	 */
	List<TbQyUserGroupPersonVO> getUserGroupPerson(Map<String, Object> map) throws Exception, BaseException;

	Pager getUserGroupPerson(Pager pager, Map<String, Object> map) throws Exception, BaseException;

	/**
	 * 根据机构ID获取默认群组
	 *
	 * @param orgId
	 * @return
	 * @author Chen Feixiong 2014-11-25
	 */
	List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception, BaseException;

	/**根据用户组获取用户信息
	 * @param String[] groupids
	 * @throws Exception
	 * @throws BaseException
	 * @author tanwq
	 * @2015-5-26
	 * @version 1.0
	 */

	List<TbQyUserInfoVO> findUserInfoByGroupIds(String[] groupIds) throws Exception, BaseException;

	/**
	 * 根据机构和用户id查询群组的用户数量
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-5-27
	 * @version 1.0
	 */
	Integer countUserGroupByUserIdAndOrgId(String userId,String orgId) throws Exception, BaseException;

	/**根据用户组统计用户信息
	 * @param String groupid
	 * @throws Exception
	 * @return Integer
	 * @throws BaseException
	 * @author tanwq
	 * @2015-5-26
	 * @version 1.0
	 */

	Integer countUserInfoByGroupId(String groupId) throws Exception, BaseException;

	/**
	 * 获取未离职的默认相关负责人
	 * @param foreignId
	 * @param orgId
	 * @param type 0默认负责人 1默认相关人
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-12-21
	 * @version 1.0
	 */
	List<TbQyGivenVO> getNotLevelGivenByForeignId(String foreignId, String orgId, String type) throws Exception, BaseException;

	List<String> getUserIdsByGroupId(String groupId) throws SQLException;
}
