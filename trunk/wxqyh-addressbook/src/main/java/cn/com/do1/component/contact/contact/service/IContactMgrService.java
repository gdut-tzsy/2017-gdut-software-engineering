package cn.com.do1.component.contact.contact.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.*;
import net.sf.json.JSONObject;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.common.service.IExcelService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbConfigOrgAesPO;

/**
 * <p>Title: 通讯录管理</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-1-20
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public interface IContactMgrService extends IBaseService,IExcelService{
	/**
	 * 分页查询--按创建时间倒序
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchContact(Map searchMap, Pager pager) throws Exception, BaseException;
	
	/**
	 * 分页查询--按创建时间倒序
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchAddContact(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 搜索人名
	 * @param orgId 组织编号
	 * @param name 中文名称
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoPO> searchPersonByName(String orgId,String name) throws Exception,BaseException;

	/**
	 * 根据机构id获取机构下的用户数
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	int findUsersCountByOrgId(String orgId)throws Exception, BaseException;
	/**
	 * 根据部门全称，获取部门下的所有人员数
	 * @param orgId
	 * @param deptFullName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	int findDeptUserCountByUserId(String orgId,String deptFullName)throws Exception, BaseException;
	
	/**
	 * 根据姓名或者手机号关键字搜索用户信息
	 * 如果用户所在的部门存在查看权限限制，使用此权限限制
	 * @param user 登录用户信息
	 * @param keyWord 关键字
	 * @return 用户列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-27
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUsersByUserNameOrPhone(UserInfoVO user,Map<String,Object> map)
			throws Exception, BaseException;

	/**
	 * 获取用户以及部门信息，部门名称以,隔开(包括设置是否可以留言信息)
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-9-16
	 * @version 1.0
	 */
	UserDeptInfoVO findUserDeptInfoByUserId(String userId) throws Exception, BaseException;
	
	/**
	 * 根据机构id获取机构信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-17
	 * @version 1.0
	 */
	DqdpOrgVO getOrgByOrgId(String orgId)throws Exception, BaseException;

	/**
	 * 根据姓名或者手机号关键字搜索用户信息
	 * @param keyWord
	 * @param pager
	 * @param user
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	Pager searchByNameOrPhone(Map<String,Object> map, Pager pager, UserInfoVO user) throws Exception,BaseException ;

	/**
	 * 按名字首写字母查询用户列表
	 * @param keyWord
	 * @param user
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	Pager searchFirstLetter(String keyWord, Pager pager, UserInfoVO user) throws Exception,BaseException ;

	/**
	 * 更改用户的状态
	 * @param userId 用户编号
	 * @param isConcerned  是否关注
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	boolean updateIsConcerned(String userId,String isConcerned)throws Exception, BaseException;

	/**
	 * 查询本机构总人数
	 * @param orgId 机构ID
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	int countByOrgId(String orgId) throws Exception, BaseException;

	/**
	 * 通过ID查询人员信息
	 * @param id 用户编号
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	TbQyUserInfoView viewUserById(String id) throws Exception, BaseException;

	/**
	 * 通过用户名、电话号码、拼音搜索用户
	 * @param orgId 组织Id
	 * @param keyWord 关键字
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-6-24
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> findUsersByUserNameOrPhoneOrPinyin(String orgId,
			String keyWord)throws Exception, BaseException;

	/**
	 * 根据组织代码获取组织的信息，主要为微信相关信息
	 * @param orgCode 机构代码
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	List<ExtOrgVO> searchOrgByCode(String orgCode)throws Exception, BaseException;

	/**
	 * 取消常用联系人数据
	 * @param userId
	 * @param toUserId
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	void cancleCommonUser(String userId, String toUserId)throws Exception, BaseException;

	/**
	 * 查询指定用户的常用联系人数据
	 * @param userId 当前登录用户
	 * @param limit 要获取的记录数
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> getCommonUserList(String userId,Integer limit)throws Exception, BaseException;

	/**
	 * 搜索常用联系人
	 * @param userId 当前登录的用户
	 * @param keyword 关键字
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> searchCommonUserList(String userId,String keyword)throws Exception, BaseException;
	/**
	 * 是否为常用联系人
	 * @param userId 当前用户
	 * @param toUserId 要查询的用户
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */	
	List<TbQyUserCommonPO> isCommonUser(String userId,String toUserId)throws Exception, BaseException;

	/**
	 * 
	 * @description：获取特定部门（多个）和特定人员Ids
	 * @param deptIds
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	public String[] findUserIdsByDeptIdsAndUserIds(Map map) throws Exception, BaseException;

	/**
	 * 
	 * @description：获取特定部门和特定人员的总人数
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	public int findPersonCountByDeptIdsAndUserIds(Map map) throws Exception, BaseException;

	/**
	 * 
	 * @description：判断发布人是否在推送列表里
	 * @param persons
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	public boolean isExitsInPersons(String[] persons, String userId) throws Exception, BaseException;

	/**
	 * 判断是否为子部门，或本部门
	 * @param myDeptId
	 * @param depIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-17
	 * @version 1.0
	 */
	boolean isChildrenDept(String myDeptId,String depIds) throws Exception, BaseException;

	/**
	 * 获得本部门及其子部门的部门id，以, 隔开
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
	 * 获取某部门下的用户
	 * @param departId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-21
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> getUsersByDepartId(String departId,Map<String,Object> params) throws Exception, BaseException;

	/**
	 * 获取某部门下的用户
	 */
	Pager searchUsersByDepartId(Pager pager, Map<String,Object> params) throws Exception, BaseException;
	
	/**
	 * @param userId
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	public Pager getTbAddressbookGuessbookPOByUserId(
			String userId, Pager pager, Map searchMap) throws Exception, BaseException;

	/**获取留言回复
	 * @param 
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	public Pager getTbAddressbookGuessbookPOReply(Pager pager, Map searchMap) throws Exception, BaseException;

	/**
	 * 获取未读留言个数
	 * @param searchMap
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author libo
	 * @2014-8-5
	 * @version 1.0
	 */
	Integer getStatuesGuessCount(Map<String, Object> searchMap) throws Exception, BaseException;


	/**
	 *根据机构名称获取机构列表
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
	 * 改变留言状态  0为已读 1为未读
	 * @param userId
	 * @author chenfeixiong
	 * 2014-8-7
	 */
	void changeStatus(String userId) throws Exception, BaseException;
	/**
	 * 处理子部门，由子部门获取到一级部门
	 * @param deptId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-12
	 */
	String getDeptFisrtDetpIdBydeptId(String deptId,String orgId)throws Exception, BaseException;
	/**
	 * 根据用户信息获取该用户可见的所有人员
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-12
	 * @param user 
	 */
	Pager findAlluserByUser(Pager pager, UserInfoVO user,Map<String,Object> params)throws Exception, BaseException;

	/**
	 * 根据userId获取登录用户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-8-15
	 * @version 1.0
	 */
	TbQyUserLoginInfoPO getUserLoginByUserId(String userId)throws Exception, BaseException;
	
	/**
	 * 获取用户同步微信通讯录情况
	 * @param orgid
	 * @return
	 * @author Chen Feixiong
	 * 2014-11-3
	 * @param newDate 
	 */
	List<TbQyUserSyncPO> getUserSync(String orgid, String newDate)throws Exception,BaseException;

	/**
	 * 根据corpId获取密钥信息
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-6
	 * @version 1.0
	 */
	List<TbConfigOrgAesPO> getConfigOrgAesPOByCorpId(String corpId)throws Exception,BaseException;

	/**
	 * 验证填写的用户信息是否合法
	 * @param corpId
	 * @param tbQyUserInfoPO
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-1
	 * @version 1.0
	 */
	String verifyUserInfo(String corpId,String orgId, TbQyUserInfoPO tbQyUserInfoPO,String type)throws Exception, BaseException;

	/**
	 * 验证填写的用户信息是否合法
	 * @param corpId
	 * @param tbQyUserInfoPO
	 * @param orgId
	 * @param isUpdate 是否是更新用户
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2017-1-4
	 * @version 1.0
	 */
	String verifyUserInfo(String corpId,String orgId, TbQyUserInfoPO tbQyUserInfoPO,String type, boolean isUpdate)throws Exception, BaseException;
	/**
	 * 根据机构id获取所有人员，用户导出
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-9
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersByOrgIdForExport(String orgId)throws Exception, BaseException;
	
	/**
	 * 删除全部通讯录
	 * @param orgId
	 * @author Hejinjiao
	 * @2014-12-9
	 * @version 1.0
	 */
	void moveUserByOrgId(String orgId)throws Exception, BaseException;

	/**
	 * 按OrgId分页查询通讯录
	 * @param map
	 * @return
	 * @author Hejinjiao
	 * @2014-12-19
	 * @version 1.0
	 */
	Pager findUsersByOrgId(Map<String, Object> map,Pager pager)throws Exception, BaseException;
	/**
	 * @param organizationId
	 * @param lunar
	 * @param type
	 * @return
	 * @author Chen Feixiong
	 * 2015-1-7
	 */
	List<UserDeptInfoVO> findBirthdayUserByDate(String organizationId,
			String lunar, String type)throws Exception, BaseException;

	/**
	 * 删除用户部门关联信息
	 * @param orgId
	 * @param userIds
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-16
	 * @version 1.0
	 */
	void deleteUserDeptRef(String orgId, String[] userIds) throws Exception, BaseException;

	int getSyncErrorUserCountByOrgId(String orgId)throws Exception,BaseException;
	
	List<ExportUserSyncVO> getSyncErrorUserByOrgId(String orgId) throws Exception,BaseException;

	/**
	 * 分页查询--按创建时间倒序--马权阳--普通管理员能看到的人员
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchContactManager(Map searchMap, Pager pager) throws Exception, BaseException;
	
	/**
	 * 分页查询--按创建时间倒序--tanwq--普通管理员能看到的人员含离职人员
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchAllContactManager(Map searchMap, Pager pager) throws Exception, BaseException;
	
	/**
	 * 根据机构id获取所有人员，用户导出--马权阳--普通管理员只能看到的人员
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-9
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersManagerByOrgIdForExport(String orgId,List<TbDepartmentInfoPO> deptList)throws Exception, BaseException;
	
	/**
     * 根据户账号和组织id获取管理员管理的对象
     * @param userName
     * @param ranges
     * @return
     * @throws Exception
     * @throws BaseException
     * @author Sun Qinghai
     * @2015-4-29
     * @version 1.0
     */
    public TbManagerPersonVO getManagerPersonByUserNameAndOrgId(String userName,String orgId)throws Exception, BaseException;
    
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
	 * 新增组织设置字段
	 * @param bData
	 * @param jData
	 * @param orgId
	 * @param userId
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-6-17
	 * @version 1.0
	 */
	public void saveField(JSONObject jData, String orgId, String userId)
		    throws Exception, BaseException;
	
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
	 * @author lishengtao
	 * 2015-8-31
	 * 置顶通讯录成员
	 * @param userId
	 * @param type
	 * @param userOrgVO
	 * @throws Exception
	 * @throws BaseException
	 */
	void updateTop(String userId,String type,UserOrgVO userOrgVO)throws Exception,BaseException;
	
	/**
	 * 分页查找证件类型
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
	 * @author lishengtao
	 * 2015-9-6
	 * 批量删除证件类型
	 * @param ids
	 * @param userOrgVo
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	String delCertificateType(String[] ids,UserOrgVO userOrgVO)throws Exception,BaseException;
	
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
	 * 根据部门名称查询部门
	 * @param params
	 * @return
	 * @author Hejinjiao
	 * @2015-9-29
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> searchDeptByDeptName(Map<String, Object> params)throws Exception,BaseException;
	
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
	 * 新增用户
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-26
	 */
	void insertUser(TbQyUserInfoPO po, List<String> deptIds, List<String> wxDeptIds)throws Exception,BaseException;

	/**
	 * 更新用户
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-26
	 */
	UpdateUserResult updateUser(TbQyUserInfoPO po, TbQyUserInfoPO old, List<String> deptIds, List<String> wxDeptIds, boolean isUpdateNull)throws Exception,BaseException;

	/**
	 * 查询用户是否保密
	 * @param userId 用户id
	 * @param orgId 机构Id
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @ 16-8-17
	 */
	boolean getSecrecyByUserId(String userId, String orgId) throws  Exception, BaseException;

	/**
	 * 新增用户保密
	 * @param secrecyPO 用户信息
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @ 16-8-17
	 */
	void addSecrecy(TbQyUserSecrecyPO secrecyPO) throws Exception, BaseException;

	/**
	 * 删除用户保密
	 * @param secrecyPo 用户信息
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @ 16-8-17
	 */
	void deleSecrecy(TbQyUserSecrecyPO secrecyPo) throws Exception, BaseException;

	/**
	 * 通过orgid查询公司保密人员
	 * @param orgId
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-17
	 */
	Set<String> getSecrecyByOrgId(String orgId) throws  Exception, BaseException;

	/**
	 *创建用户保密缓存
	 * @param orgId 机构Id
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-19
	 */
	Set<String> addSecrecyCache(String orgId) throws Exception, BaseException;

	/**
	 *优化用户按名字、拼音、电话搜索条件
	 * @param map sql的条件
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-25
	 */
	Map<String, Object>   optimizeByNameOrPhone(Map<String, Object> map) throws Exception, BaseException;

	/**
	 * 根据机构id获分页获取用户信息（用于接口）
	 * @param searchMap sql的条件
	 * @param pager 	分页page
	 * @return
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @author liyixin
	 * @2016-9-22
	 * @version 1.0
	 */
	Pager findUsersByOrgIdForInterface(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 查询机构的最新版本号
	 * @param orgId 机构orgId
	 * @return
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @author liyixin
	 * @2016-10-11
	 * @version 1.0
	 */
	Integer findVersionOrgByorgId(String orgId)  throws Exception, BaseException;

	/**
	 *判断该用户在不在版本控制表内
	 * @param id 用户id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-11
	 * @version 1.0
     */
	boolean findVersionIdById(String id) throws BaseException, Exception;

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
	 * 批量新增机构版本的id
	 * @param idType id类型
	 * @param operationType 操作类型
	 * @param orgVesionRecent 机构最新版本
	 * @param orgId 机构Id
	 * @param ids 用户id列表
	 * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-12
	 * @version 1.0
     */
	void batchAddVersion(String idType, String operationType, Integer orgVesionRecent, String orgId, List<String> ids) throws BaseException, Exception;

	/**
	 * 批量更新机构版本的id
	 * @param operationType 操作类型
	 * @param orgVesionRecent 机构最新版本
	 * @param orgId 机构Id
	 * @param ids 用户id列表
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-12
	 * @version 1.0
	 */
	void batchUpdateVersion(String operationType, Integer orgVesionRecent, String orgId, List<String> ids) throws BaseException, Exception;

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
	 *通过机构Id和版本id分页查询
	 * @param searchMap map
	 * @param pager 分页
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-10-14
	 * @version 1.0
	 */
	Pager findVersionOrg(Map searchMap, Pager pager) throws BaseException, Exception;

	/**
	 * 通过用户id返回用户列表
	 * @param userIds id列表
	 * @param orgId 机构Id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-10-14
	 * @version 1.0
	 */
	List<InterfaceUser> getUserByIds(List<String> userIds, String orgId) throws BaseException, Exception;

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
	 * 批量更新高级搜索条件
	 * @param org
	 * @param seniorPOList
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2017-2-07
     */
	void batchUpdateSenior(UserOrgVO org, List<TbQyUserSearchSeniorPO> seniorPOList) throws BaseException, Exception;

	/**
	 * 通过userId列表查询用户
	 * @param userIds
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-22
	 * @version 1.0
	 */
	List<SelectUserVO> findUserByUserIds(List<String> userIds) throws BaseException, Exception;

	/**
	 * 通过userIds用户的部门ids
	 *
	 * @param userIds
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	List<TbQyUserInfoPO> findUserDepartIdsByUserIds(List<String> userIds) throws BaseException, Exception;

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
	 * 分页获取有手写签名图片的用户
	 * @param pager 分页数据
	 * @param org 机构信息
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
	 */
	Pager getUserHandByOrgId(Pager pager, UserOrgVO org) throws Exception, BaseException;

	/**
	 *根据部门id查询用户列表
	 * @param departId 部门id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-05-11
	 * @version 1.0
	 */
	List<InterfaceUser> getUserByDepartIds(String departId, String orgId) throws BaseException, Exception;

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
	 *插入或更新手机端默认查询条件
	 * @param selectPO
	 * @param orgId
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
     */
	void insertOrUpdateDefaultSelect(TbQyUserDefaultSelectPO selectPO, String orgId) throws BaseException, Exception;
}
