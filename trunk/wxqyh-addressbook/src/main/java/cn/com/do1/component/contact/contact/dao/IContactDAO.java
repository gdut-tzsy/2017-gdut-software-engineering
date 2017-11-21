package cn.com.do1.component.contact.contact.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbConfigOrgAesPO;

import cn.com.do1.component.systemmgr.user.model.TbDqdpUserPO;
import cn.com.do1.component.wxcgiutil.token.AccessToken;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface IContactDAO extends IBaseDAO {
	Pager searchContact(Map searchMap, Pager pager) throws Exception, BaseException;
	/**获取所有用户信息 包含离职用户   by tanwq 2015-6-1***/
	Pager searchAllContact(Map searchMap, Pager pager) throws Exception, BaseException;
	/**
	 * 通过机构查询所有的用户信息
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-16
	 * @version 1.0
	 */
	Pager searchContactByOrgId(Map searchMap, Pager pager) throws Exception, BaseException;
	/**
	 * 通过机构查询所有的用户信息  包含离职用户
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-16
	 * @version 1.0
	 */
	Pager searchAllContactByOrgId(Map searchMap, Pager pager) throws Exception, BaseException;

	Pager searchContactByPy(Map searchMap, Pager pager) throws Exception, BaseException;

	List<TbQyUserInfoPO> searchPersonByName(String orgId, String name) throws Exception, BaseException;

	List<TbQyUserInfoPO> findUsersByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 根据机构id获取机构下的用户数
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	int findUsersCountByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 根据部门全称，获取部门下的所有人员数
	 * 
	 * @param orgId
	 * @param deptFullName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	int findDeptUserCountByUserId(String orgId, String deptFullName) throws Exception, BaseException;

	/**
	 * 根据部门全称，获取部门下的所有人员数
	 * 
	 * @param orgId
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	int findDeptUserCountByUserId(String orgId, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * 根据姓名或者手机号关键字搜索用户信息 如果用户所在的部门存在查看权限限制，使用此权限限制
	 * 
	 * @param orgId
	 * @param map
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUsersByUserNameOrPhone(String orgId, Map<String,Object> map, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	Pager searchByNameOrPhone(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * 获取指定用户Id的信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-17
	 * @version 1.0
	 */
	TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception, BaseException;

	/**
	 * 获取指定用户Id的信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-17
	 * @version 1.0
	 */
	TbQyUserInfoPO findUserInfoPOByUserId(String userId) throws Exception, BaseException;

	/**
	 * 获取用户以及部门信息，部门名称以,隔开
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-17
	 * @version 1.0
	 */
	UserDeptInfoVO findUserDeptByUserId(String userId) throws Exception, BaseException;

	/**
	 * 获取用户以及部门信息，部门名称以,隔开
	 *
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author tanwq
	 * @2017-5-24
	 * @version 1.0
	 */
	List<UserDeptInfoExpandVO> findUserDeptByUserIds(String[] userIds) throws Exception, BaseException;

	/**
	 *  获取用户以及部门信息，部门名称以,隔开(包括设置是否可以留言信息)
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-9-16
	 * @version 1.0
	 */
	UserDeptInfoVO findUserDeptInfoByUserId(String userId) throws Exception, BaseException;

	List<TbQyUserInfoVO> findUsersByDepartId(String orgId, String departId) throws Exception, BaseException;

	/**
	 * 获得后台登录用户和机构信息
	 * @param userName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-17
	 * @version 1.0
	 */
	UserOrgVO getOrgByUserId(String userName) throws Exception, BaseException;

	/**
	 * 获得后台登录的用户信息，不包括机构信息
	 * @param userName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-17
	 * @version 1.0
	 */
	UserOrgVO getUserByUserId(String userName) throws Exception, BaseException;

	/**
	 * 根据id获取机构的信息，不包括登录用户信息
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-17
	 * @version 1.0
	 */
	DqdpOrgVO getOrgById(String id) throws Exception;

	int countByOrgId(String orgId) throws Exception, BaseException;

	TbQyUserInfoView viewUserById(String id) throws Exception, BaseException;

	/**
	 * 获取所选部门下的所有用户
	 * 
	 * @param orgId
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, List<TbDepartmentInfoPO> depts) throws Exception, BaseException;

	/**
	 * 分页获取所选部门下的所有用户
	 * 
	 * @param orgId
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	Pager findDeptUserAllByDeptForPager(String orgId, List<TbDepartmentInfoPO> depts, Pager pager) throws Exception, BaseException;

	/**
	 * 获取所选部门下的用户
	 * 
	 * @param orgId
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findDeptUserByDept(String orgId, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * 获取所选部门下的所有用户(包括子部门)
	 * 
	 * @param orgId
	 * @param deptFullName
	 *            部门全称
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, String deptFullName) throws Exception, BaseException;

	/**
	 * 获取所选部门下的所有用户
	 * @param deptId
	 *            部门id
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findDeptUserByDept(String deptId,Map<String,Object> params) throws Exception, BaseException;

	/**
	 * 获取所选部门下的所有用户
	 */
	Pager searchUsersByDepartId(Pager pager, Map<String,Object> params) throws Exception, BaseException;
	
	/**
	 * 通过用户名、电话号码、拼音搜索用户
	 * 
	 * @param orgId
	 * @param keyWord
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-6-24
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUsersByUserNameOrPhoneOrPinyin(String orgId, String keyWord) throws Exception, BaseException;
	/**
	 * 通过corpId获取org的信息
	 * 
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-1
	 * @version 1.0
	 */
	AccessToken getAccessTokenByCorpId(String corpId) throws Exception, BaseException;

	Pager findAll(Pager pager) throws Exception, BaseException;

	List<ExtOrgVO> searchOrgByCode(String orgCode) throws Exception, BaseException;

	/**
	 * 按名字首写字母查询用户列表
	 * 
	 * @param searchMap
	 * @param pager
	 * @param depts
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	Pager searchFirstLetter(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	void updateCommonUser(String userId, String toUserId, Integer num);

	List<TbQyUserCommonPO> searchCommonUser(String userId, String toUserId) throws Exception, BaseException;

	/**
	 * 根据用户id获取常用联系人列表 by tanwq
	 * @param userId
	 * @param toUserIds
	 * @return
	 * @throws Exception
     * @throws BaseException
     */
	List<TbQyUserCommonPO> searchCommonUser(String userId, List<String> toUserIds) throws Exception, BaseException;

	/***
	 * 批量修改联系人数 by tanwq 2017-5-2
	 * @param userId
	 * @param toUserIds
	 * @throws Exception
	 * @throws BaseException
     */
	void updateTbQyUserCommonByUserIdAndToUserIds(String userId, List<String> toUserIds,int num)throws Exception, BaseException;
	List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit) throws Exception, BaseException;

	List<TbQyUserCommonPO> isCommonUser(String userId, String toUserId) throws Exception, BaseException;

	void cancleCommonUser(String userId, String toUserId) throws Exception, BaseException;

	List<TbQyUserInfoVO> searchCommonUserList(String userId, String keyword) throws Exception, BaseException;

	/**
	 * 查询今日生日用户
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-9
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findBirthdayUser(String date) throws Exception, BaseException;

	/**
	 * 根据部门Id获取部门的全路径
	 * 
	 * @param depIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-17
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> findDeptFullNamesByIds(String[] depIds) throws Exception, BaseException;

	/**
	 * 获得本部门及其子部门的部门id，以, 隔开
	 * 
	 * @param deptId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-21
	 * @version 1.0
	 */
	String getMyAndChildDeptIds(String deptId, String orgId) throws Exception, BaseException;

	/**
	 * 获取userId
	 * 
	 * @param userId
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	Pager getTbAddressbookGuessbookPOByUserId(String userId, Pager pager, Map searchMap) throws Exception, BaseException;

	/**
	 * 获取留言回复
	 * 
	 * @param pager
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	public Pager getTbAddressbookGuessbookPOReply(Pager pager, Map searchMap) throws Exception, BaseException;

	/**
	 * 获取未读留言个数
	 * 
	 * @param searchMap
	 * @return
	 * @author libo
	 * @throws SQLException
	 * @2014-8-5
	 * @version 1.0
	 */
	Integer getStatuesGuessCount(Map<String, Object> searchMap) throws Exception, BaseException;

	/**
	 * 根据机构名称获取机构列表
	 * 
	 * @param orgName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @param orgPid
	 * @2014-8-5
	 * @version 1.0
	 */
	public List<ExtOrgVO> searchOrgByOrgName(String orgName, String orgPid) throws Exception, BaseException;

	/**
	 * 改变留言状态 0为已读 1为未读
	 * 
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-7
	 */
	void changeStatus(String userId) throws Exception, BaseException;

	/**
	 * 获取顶级部门下所有人员信息
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-12
	 * @param depts
	 */
	Pager findAlluserByDeptId(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * 根据userId获取登录用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-15
	 * @version 1.0
	 */
	TbQyUserLoginInfoPO getUserLoginByUserId(String userId) throws Exception, BaseException;

	/**
	 * 根据账号获取登录用户信息
	 * 
	 * @param userAccount
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-19
	 * @version 1.0
	 */
	TbQyUserLoginInfoPO getUserLoginByAccount(String userAccount) throws Exception, BaseException;

	/**
	 * 根据userName获取dqdp登录用户信息
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-20
	 * @version 1.0
	 */
	TbDqdpUserPO getDqdpUserByUserName(String userName) throws Exception, BaseException;

	/**
	 * 根据现有的用户状态获取所有的登录用户信息
	 * 
	 * @param userStatus
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-10-28
	 * @version 1.0
	 */
	List<UserOrganizationVO> findAllLoginUserByUserStatus(String userStatus) throws Exception, BaseException;

	/**
	 * 获取用户同步微信通讯录情况
	 * 
	 * @param orgid
	 * @return
	 * @author Chen Feixiong 2014-11-3
	 * @param newDate
	 */
	List<TbQyUserSyncPO> getUserSync(String orgid, String newDate) throws Exception, BaseException;

	/**
	 * 根据corpId获取机构信息
	 * 
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-6
	 * @version 1.0
	 */
	List<ExtOrgPO> getOrgPOByCorpId(String corpId) throws Exception;

	/**
	 * 根据corpId获取密钥信息
	 * 
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-6
	 * @version 1.0
	 */
	List<TbConfigOrgAesPO> getConfigOrgAesPOByCorpId(String corpId) throws Exception, BaseException;

	/**
	 * 根据手机号码和机构ID查询用户信息
	 * 
	 * @param orgId
	 * @param mobile
	 * @return
	 * @author Chen Feixiong 2014-11-7
	 */
	TbQyUserInfoVO findUsersByPhone(String orgId, String mobile) throws Exception, BaseException;

	/**
	 * 根据关联的id获取图片列表
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-19
	 * @version 1.0
	 */
	List<TbQyPicPO> getPicListByGroupId(String groupId) throws Exception, BaseException;

	/**
	 * 根据关联的id删除图片
	 * 
	 * @param groupId
	 *            用于关联的id
	 * @param userId
	 *            约束只能删除本人创建的，避免误操作
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-19
	 * @version 1.0
	 */
	void delPicListByGroupId(String groupId, String userId) throws Exception, BaseException;

	/**
	 * 批量删除图片
	 * @param groupIds
	 * @param orgId
	 * @throws Exception
	 * @throws BaseException
	 */
	void delPicListByGroupIds(String[] groupIds, String orgId) throws Exception, BaseException;

	/**
	 * 根据用户微信id,corpId获取用户信息
	 * 
	 * @param wxUserId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @param corpId
	 * @2014-12-1
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUserInfoByWxUserId(String wxUserId, String corpId) throws Exception, BaseException;
	/**
	 * 根据用户微信id,corpId获取用户信息
	 *
	 * @param wxUserId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @param corpId
	 * @2014-12-1
	 * @version 1.0
	 */
	List<TbQyUserInfoPO> findUserInfoPOByWxUserId(String wxUserId, String corpId) throws Exception;

	/**
	 * 根据机构id获取所有人员，用户导出
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-9
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersByOrgIdForExport(String orgId) throws Exception, BaseException;

	/**
	 * @author lishengtao
	 * 2015-8-12
	 * 按参数搜索导出
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<ExportUserInfo> findUsersForExport(Map<String,Object> params) throws Exception, BaseException;

	/**
	 * 部门下是否含有人员
	 * 
	 * @param organId
	 * @param deptId
	 * @param deptFullName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-10
	 * @version 1.0
	 */
	boolean hasUsersByDepartIdAndFullName(String organId, String deptId, String deptFullName) throws Exception;

	/**
	 * 部门下是否含有人员
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-6
	 */
	boolean hasUsersByDepartId(String deptId) throws Exception;
	/**
	 * 部门下是否含有人员
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-6
	 */
	boolean hasUsersByDepartIds(String[] deptIds) throws Exception;

	/**
	 * 按OrgId分页查询通讯录
	 * 
	 * @param map
	 * @return
	 * @author Hejinjiao
	 * @2014-12-19
	 * @version 1.0
	 */
	Pager findUsersByOrgId(Map<String, Object> map, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * @param userId
	 * @return
	 * @author Sun Qinghai
	 * @2014-12-22
	 * @version 1.0
	 */
	void batchDelLocationByUserId(String userId) throws Exception, BaseException;

	/**
	 * 批量查询用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-29
	 * @version 1.0
	 */
	TbQyUserInfoVO getUserPartInfoByUserId(String userId) throws Exception, BaseException;

	/**
	 * 查询所有机构
	 * 
	 * @return
	 * @author Chen Feixiong 2015-1-7
	 */
	List<ExtOrgPO> getAllOrg() throws Exception, BaseException;

	/**
	 * @param organizationId
	 * @param lunar
	 * @param type
	 * @return
	 * @author Chen Feixiong 2015-1-7
	 */
	List<UserDeptInfoVO> findBirthdayUserByDate(String organizationId, String lunar, String type) throws Exception, BaseException;

	/**
	 * 删除用户的部门关联信息
	 * 
	 * @param orgId
	 * @param userIds
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-15
	 * @version 1.0
	 */
	void deleteUserDeptRef(String orgId, String[] userIds) throws Exception, BaseException;
	
	/**
	 * 根据部门和用户信息删除用户和部门的关联关系
	 * @param deptId
	 * @param userId
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-1-28
	 * @version 1.0
	 */
	void deleteUserDeptRefByUserIdDeptId(String deptId, String userId) throws Exception, BaseException;

	/**
	 * 获取机构下的所有用户id信息
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-21
	 * @version 1.0
	 */
	List<String> getUserIdsByOrg(String orgId) throws Exception, BaseException;

	/**
	 * 根据orgId获取用户信息
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-22
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUsersVOByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 删除同步错误的用户信息
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-22
	 * @version 1.0
	 */
	void deletErrorUser(String orgId) throws Exception, BaseException;

	/**
	 * 分页查找同步错误的人员
	 * 
	 * @param searchValue
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager getSyncErrorUser(Map searchValue, Pager pager) throws Exception, BaseException;

	/**
	 * 根据orgId获取同步错误条数
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int getSyncErrorUserCountByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 根据orgId查找同步错误的人员信息
	 * 
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<ExportUserSyncVO> getSyncErrorUserByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 根据机构id获取所有的用户信息（用于接口）
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-3-3
	 * @version 1.0
	 */
	List<InterfaceUser> findUsersByOrgIdForInterface(String orgId) throws Exception, BaseException;

	/**
	 * 获取所有的机构（去除corp重复的）
	 * 
	 * @return
	 * @author Chen Feixiong 2015-3-3
	 */
	List<ExtOrgPO> getAllOrgByCorp() throws Exception, BaseException;

	/**
	 * 根据 corpId清空机构中secret信息
	 * 
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-3-9
	 * @version 1.0
	 */
	void emptyOrgSecretByCorpId(String corpId) throws Exception, BaseException;

	/**
	 * 根据CorpId获取管理员的信息
	 * 
	 * @param corpId
	 * @return
	 * @author Chen Feixiong 2015-4-9
	 */
	UserOrgVO getDQDPUserByCorpId(String corpId) throws Exception, BaseException;

	/*** 根据用户id获取用户 信息 by tanwq 2015-4-20 ***/
	UserRedundancyInfoVO getUserRedundancyInfoByUserId(String userId) throws Exception, BaseException;

	/**
	 * 获取用户信息（使用于用户登录后获取登录用户信息，用于缓存）
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-6-17
	 * @version 1.0
	 */
	UserInfoVO getUserInfoByUserId(String userId) throws Exception, BaseException;

	/**
	 * 获取用户信息（使用于用户登录后获取登录用户信息，用于缓存）
	 * @param wxUserId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-6-17
	 * @version 1.0
	 */
	UserInfoVO getUserInfoByWxUserId(String wxUserId,String corpId) throws Exception, BaseException;
	/**
	 * 根据用户ids获取所有的用户信息
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-4-13
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> getAllUserInfoByIds(String[] userIds)throws Exception,BaseException;

	/**
	 * 删除用户关联数据
	 * @param userIds
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-4-20
	 */
	void deleteUserRelate(String[] userIds)throws Exception,BaseException;

	/** 根据orgId 用户ids查询用户信息 ***/
	List<TbQyUserInfoVO> getUserInfoByOrgIdAndUserIds(String orgId, String userIds) throws Exception, BaseException;

	/**
	 * 机构名称是否已存在
	 * @param orgName
	 * @param orgPid
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2015-5-4
	 * @version 1.0
	 */
	boolean isExitsOrgName(String orgName, String orgPid)throws Exception;

	/**
	 * 通过机构查询所有的用户信息--马权阳--普通管理员能看到的人员
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-16
	 * @version 1.0
	 */
	Pager searchContactManagerByOrgId(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 通过机构查询所有的用户信息--tanwq--普通管理员能看到的人员含离职人员
	 * 
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-16
	 * @version 1.0
	 */
	Pager searchAllContactManagerByOrgId(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 根据机构id获取所有人员，用户导出--马权阳--普通管理员能看到的人员
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-9
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersManagerByOrgIdForExport(String orgId,List<TbDepartmentInfoPO> deptList) throws Exception, BaseException;

	/**
	 * @author lishengtao
	 * 2015-8-12
	 * @param params
	 * @param deptList
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<ExportUserInfo> findUsersManagerForExport(Map<String,Object> params,List<TbDepartmentInfoPO> deptList) throws Exception, BaseException;

	/**
	 * 根据用户账号和组织id获取管理员管理的对象
	 * @param userName
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-4-29
	 * @version 1.0
	 */
	public TbManagerPersonVO getManagerPersonByUserNameAndOrgId(String userName,String orgId)throws Exception, BaseException;

	/**
	 * 获取管理员管理的对象
	 * @param personId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-4-29
	 * @version 1.0
	 */
	public TbManagerPersonVO getManagerPersonByPersonIdAndOrgId(String personId,String orgId)throws Exception, BaseException;

	/**
	 * 根据机构id获取所有人员--马权阳--普通管理员能看到的人员
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-9
	 * @version 1.0
	 */
	List<TbQyUserInfoView> findUsersManagerByOrgId(String orgId,List<TbDepartmentInfoPO> deptList) throws Exception, BaseException;

	/**
	 * 根据用户userName获取用户信息(用户名,机构,personId等)
	 * 
	 * @param userName
	 *            dqdp登录用户账号
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-29
	 * @version 1.0
	 */
	DqdpUserPersonInfoVO searchDqdpUserPersonInfoByUserName(String userName) throws Exception, BaseException;

	/**
	 * 查询组织下所有通讯录项目设置字段
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-6-17
	 * @version 1.0
	 */
	public List<TbQyFieldSettingVO> findTbQyFieldSettingVOListByOrgId(String orgId)throws Exception,BaseException;

	/**
	 * 删除组织下的设置字段
	 * @param orgId
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-6-17
	 * @version 1.0
	 */
	public void delTbQyFieldSettingVOListByOrgId(String orgId)throws Exception,BaseException;

	/**
	 * 根据部门id获取直接负责人信息 2015-8-26
	 * @author tanwq
	 * @param departmentId
	 * @return TbQyUserInfoVO
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> findUserInfoPOByDepartmentId(String departmentId) throws Exception, BaseException;

	/**
	 * @author lishengtao
	 * 2015-7-17
	 * 分页查询部门成员(不包括子部门)
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchContactByDeptId(Map searchMap, Pager pager) throws Exception, BaseException;

	List<TbQyUserInfoVO> findListByNameOrPhone(Map searchMap, List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	/**
	 * 查询人员搜索通讯录的条件
	 * @param vo(id、创建人、agentCode、组织id查询)
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-7-29
	 * @version 1.0
	 */
	TbQyUserSearchVO getUserSearchByTbQyUserSearchVO(TbQyUserSearchVO vo)throws Exception, BaseException;

	/**
	 * 根据orgId、userStatus获取用户信息
	 * @param orgId
	 * @param userStatus
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-8-6
	 * @version 1.0
	 */
	List<UserInfoVO> findUsersVOByOrgIdAndUserStatus(String orgId,String userStatus)throws Exception, BaseException;

	/**
	 * 查询用户信息包括部门id和部门全称
	 * @param userId
	 * @param userStatus
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-8-6
	 * @version 1.0
	 */
	public UserInfoVO findUserInfoDepartByUserId(String userId,String userStatus) throws Exception, BaseException;

	/**
	 * 获取所选部门下的所有用户
	 * @param orgId
	 * @param depts
	 * @param seachType 可以根据该条件决定查出那些数据
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-8-25
	 * @version 1.0
	 */
	List<UserInfoVO> findDeptUserAllByDeptAndSeachType(String orgId, List<TbDepartmentInfoPO> depts,String seachType) throws Exception, BaseException;


	/**
	 * @author lishengtao
	 * 2015-8-31
	 * 获取机构下通讯录的最高排序号的人员
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyUserInfoPO getTopUserInfoPO(String orgId)throws Exception,BaseException;

	/**
	 * 分页查找证件类型
	 * @author lishengtao
	 * 2015-9-2
	 * @param params
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager getUserInfoCertificateTypePager(Map<String,Object> params,Pager pager)throws Exception,BaseException;

	/**
	 * 查找类型列表
	 * @author lishengtao
	 * 2015-9-2
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoCertificateTypePO> getUserInfoCertificateTypeList(Map<String,Object> params)throws Exception,BaseException;
	/**
	 * 统计通信录总人数
	 * @return
	 * @author Hejinjiao
	 * @2015-9-6
	 * @version 1.0
	 */
	int countAllPerson()throws Exception, BaseException;

	/**
	 * @author lishengtao
	 * 2015-9-6
	 * 获取证件引用的类型
	 * @param params:证件id;orgId;
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int getCertificateTypeCount(Map<String, Object> params)throws Exception,BaseException;

	/**
	 * 根据证件类型标题获取证件类型
	 * @author lishengtao
	 * 2015-9-6
	 * @param title
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoCertificateTypePO> getCertificateTypeListByTitle(String title,String orgId)throws Exception,BaseException;


	/**
	 * 按姓名、机构Id 查询通信录
	 * @param params
	 * @param pager
	 * @return
	 * @author Hejinjiao
	 * @2015-9-23
	 * @version 1.0
	 */
	Pager searchUserByPersonName(Map<String, Object> params, Pager pager)throws Exception,BaseException;
	
	/**
	 * @param params
	 * @return
	 * @author Hejinjiao
	 * @2015-9-29
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> searchDeptByDeptName(Map<String, Object> params)throws Exception,BaseException;
	
	List<TbQyPicPO> getPicListByGroupIdZhouNian(String groupId, String type)throws Exception,BaseException;
	
	/**
	 * 根绝userIds查找账户
	 * @author lishengtao
	 * @2015-10-26
	 * @param userIds id1,id2,id3
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<UserInfoVO> getUserInfoVOInUserIds(String userIds,Map<String,Object> params)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 根据userId获取用户信息扩展字段
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyUserInfoExtPO getUserInfoExtPOByUserId(String userId)throws Exception,BaseException;

	/**
	 * @author lishengtao
	 * 查询超级管理员
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbDqdpUserPO getSuperManagerByOrgId(String orgId)throws Exception,BaseException;
	
	/**
	 * 根据名称查询机构下的管理员
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<UserOrgVO> getOrgByPerson(Map<String, Object> params)throws Exception,BaseException;
	

	/**2015-11-18
	 * @author Huhao
	 * @param wxUserId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	String getUserIdByWxUserId(String wxUserId,String orgId)throws Exception,BaseException;
	/**
	 * 获取部门的负责人
	 * @param depatIds
	 * @param orgId
	 * @return
	 * @author Hejinjiao
	 * @2015-12-31
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> getToUserByDepatIds(String[] depatIds, String orgId)throws Exception,BaseException;
	
	/***修改直接负责人用户信息   by tanwq 2016-3-16****/
	void updateQyUserReceiveByUserId(Map<String,Object> params)throws Exception,BaseException;

	/**
	 * 根据id获取用户信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-11
	 */
	List<TbQyUserInfoVO> getUserInfoByIds(String[] ids)throws Exception;
	/**
	 * 根据userId获取用户信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-11
	 */
	List<TbQyUserInfoVO> getUserInfoByUserIds(String[] userIds)throws Exception;

	/**
	 * 根据userId删除备份的离职用户和部门关联关系
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-11
	 */
	void deleteUserLeaveDeptRef(String orgId, List<String> userIds) throws Exception;

	/**
	 * 根据用户id获取用户部门关联关系
	 * @return
	 * @author Sun Qinghai
	 * @throws Exception 异常抛出
	 * @ 16-5-11
	 */
	List<TbDepartmentInfoVO> getDeptInfoByLeaveUserId(String userId)  throws Exception;
	/**
	 * 通过orgid查询公司保密人员
	 * @param orgId 机构id
	 * @return
	 * @author LiYiXin
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * 2016-8-17
	 */
	List<TbQyUserSecrecyPO> getSecrecyByOrgId(String orgId) throws  Exception, BaseException;

	List<TbQyContactSyncPO> getContactSyncList(String corpId, int status) throws Exception;

	List<String> getContactSyncCorpIdList(int status) throws SQLException;

	/**
	 * 批量查询机构版本的id
	 * @param orgId 机构id
	 * @param start 起点
	 * @param end 终点
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-11
     */
	List<String> getVersionId(String orgId, int start, int end) throws BaseException, Exception;

	/**
	 * 统计机构版本的企业的id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-12
     */
	int getVersionOrgCount(String orgId) throws BaseException, Exception;

	/**
	 * 查询高级搜索条件
	 * @param orgId 机构id
	 * @param userId 当前用户
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2017-2-07
	 */
	List<TbQyUserSearchSeniorVO> getSeniorByOrgId(String orgId, String userId) throws BaseException, Exception;

	/**
	 * 查询高级搜索条件的PO
	 * @param orgId 机构id
	 * @param userId 当前用户
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2017-02-08
	 */
	List<TbQyUserSearchSeniorPO> getSeniorPOByOrgId(String orgId, String userId) throws BaseException, Exception;

	/**
	 * 搜索机构的发票信息
	 * @param orgIdArray
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<OrgInfoExtVO> getOrgInfoExtVOList(String orgIdArray[]) throws Exception, BaseException;

	/**
	 * 根据部门id获取直接负责人信息
	 * @param departIds 部门ids
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-3-7
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUserReceiveByDepartIds(List<String> departIds) throws Exception, BaseException;
	/**
	 * 根据机构id查询超管账号
	 * @param orgId     the orgId
	 * @return the String
	 * @throws Exception     the exception
	 * @throws BaseException the base exception
	 * @author Chen Feixiong
	 * @ 2017/3/8
	 */
	String getManagerInfoByOrgId(String orgId)throws Exception, BaseException;

	/**
	 * 根据wxUserIds获取用户信息
	 * @param corpId
	 * @param wxUserIds
	 * @return
	 * @author sunqinghai
	 * @2017-4-6
	 */
	List<TbQyUserInfoPO> getUserInfoPOByWxUserIds(String corpId, List<String> wxUserIds) throws SQLException;

	/**
	 * 分页获取某部门下的用户
	 * @param departId 部门id
	 * @param params 传过来的查询条件
	 * @param pager 分页数据
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-11
	 * @version 1.0
	 */
	Pager getUsersByDepartIdToPager(String departId, Map<String,Object> params, Pager pager) throws Exception, BaseException;

	/**
	 * 通过用户的userId获取手写签名图片
	 * @param userId 用户的id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-4-13
	 * @version 1.0
	 */
	TbQyUserHandWritPO getUserHandWritByUserId(String userId) throws BaseException, Exception;

	/**
	 * 分页获取有手写签名图片的用户
	 * @param pager 分页数据
	 * @param orgId 机构Id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
	 */
	Pager getUserHandByOrgId(Pager pager, String orgId) throws Exception, BaseException;

	/**
	 * 根据userId获取有手写签名图片的用户信息
	 * @param userId 用户信息
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-20
	 * @version 1.0
	 */
	TbQyUserHandWritVO getUserHandAndUserInfoByUserId(String userId) throws Exception, BaseException;

	/**
	 * 根据机构id查询手机端搜索条件
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
     */
	TbQyUserDefaultSelectPO getUserDefaultByOrgId(String orgId) throws BaseException, Exception;

	/**
	 * 获取用户的更新记录
	 * @param corpId 机构corpid
	 * @param suiteId 套件id
	 * @param startTimeStamp 查询开始的时间戳
	 * @param startId 查询开始的id
	 * @param size 查询的条数
	 * @return
	 * @author sunqinghai
	 * @2017-6-16
	 */
	List<TbQyChangeContactPO> getChangeContactList(String corpId, String suiteId, long startTimeStamp, long startId, int size) throws Exception, BaseException;
}
