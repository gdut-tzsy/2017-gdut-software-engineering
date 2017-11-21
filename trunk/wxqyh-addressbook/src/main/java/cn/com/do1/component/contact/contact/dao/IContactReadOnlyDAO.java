package cn.com.do1.component.contact.contact.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxgzhPO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;

/**
 * 只读DAO专用
 * @author lishengtao
 * @date 2015-10-28
 */
public interface IContactReadOnlyDAO extends IBaseDAO{

	/**
	 * @author lishengtao
	 * 2015-11-2
	 * 分页查找用户信息(初始化的时候用，用完请废弃)
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager getUserInfPager(Map<String,Object> searchMap,Pager pager)throws Exception,BaseException;

	/**
	 * @author lishengtao
	 * 2015-11-2
	 * 查找用户信息(初始化的时候用，用完请废弃)
	 * @param searchMap
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoPO> getUserInfoList(Map<String,Object> searchMap)throws Exception,BaseException;

	/**
	 * 按参数搜索导出
	 * @param params
	 * @param isEduVerson
	 * @return
	 * @author Hejinjiao
	 * @2015-11-3
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersForExport(Map<String, Object> params, boolean isEduVerson)throws Exception,BaseException;

	/**
	 * 分级管理员导出
	 * @param params
	 * @param deptList
	 * @param isEduVerson
	 * @return
	 * @author Hejinjiao
	 * @2015-11-3
	 * @version 1.0
	 */
	List<ExportUserInfo> findUsersManagerForExport(Map<String, Object> params,
												   List<TbDepartmentInfoPO> deptList, boolean isEduVerson)throws Exception,BaseException;

	/**
	 * 根据corpId、微信id，获取用户
	 * @param corpId
	 * @param wxUserId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-12-7
	 * @version 1.0
	 */
	List<TbQyUserInfoPO> findUsersByWxUserId(String corpId, String wxUserId) throws Exception, BaseException;
	
	/**
	 * 根据corpId、手机号码获取用户
	 * @param corpId
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-12-7
	 * @version 1.0
	 */
	List<TbQyUserInfoPO> findUsersByMobile(String corpId, String mobile) throws Exception, BaseException;
	
	/**
	 * 根据corpId、微信号，获取用户
	 * @param corpId
	 * @param weixinNum
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-12-7
	 * @version 1.0
	 */
	List<TbQyUserInfoPO> findUsersByWeixinNum(String corpId, String weixinNum) throws Exception, BaseException;
	
	/**
	 * 根据corpId、corpId邮箱，获取用户
	 * @param corpId
	 * @param email
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-12-7
	 * @version 1.0
	 */
	List<TbQyUserInfoPO> findUsersByEmail(String corpId, String email) throws Exception, BaseException;
	/**
	 * 根据orgId获取用户信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @version 1.0
	 */
	public List<TbQyUserInfoVO> findUsersAndDeptByOrgId(String orgId)throws Exception, BaseException;
	
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
	List<TbQyUserInfoVO> getAllUserAndDeptByIds(String[] userIds)throws Exception,BaseException;

	/**
	 * 通过手机号获取用户信息（使用于用户登录后获取登录用户信息，用于缓存）
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @version 1.0
	 */
	TbQyUserInfoVO getUserInfoByMobile(String mobile,String corpId) throws Exception, BaseException;

	/**
	 * 通过手机号获取用户信息（使用于用户登录后获取登录用户信息，用于缓存）
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2016-2-25
	 * @version 1.0
	 */
	int getCountOrgByCorpId(String corpId) throws Exception;
	
	/***根据userId判断是否部门直接负责人里面是否有该用户  by tanwq 2016-3-16***/
	Integer countQyUserReceiveByUserId(String userId,String orgId)throws Exception,BaseException;

	/**
	 * 获取某个管理员当天删除的机构数
	 * @param user
	 * @return
	 * @author ChenFeixiong
	 * 2016-4-14
	 */
	int getDelOrgCount(String user)throws Exception,BaseException;
	
	/**
	 * 获取days天内生日/周年的人的人数
	 * @param orgId
	 * @param agentCode
	 * @param days
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-4-25
	 */
	public int getPepleCount(String orgId, String agentCode, int days)throws Exception,BaseException;
	
	/**
	 * 根据关联的ids获取图片列表
	 * @param groupIds 用于关联的ids
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2016-4-26
	 * @version 1.0
	 */
	List<TbQyPicVO> getPicListByGroupIds(String[] groupIds)throws Exception, BaseException;

	/**
	 * 获取离职用户数
	 * @return
	 * @throws Exception
	 * sunqinghai
	 * 2016-06-21
	 * @param orgId
	 */
	int countOrgLeavePerson(String orgId)throws Exception;

	/**
	 * 获取企业用户数
	 * @return
	 * @throws Exception
	 * sunqinghai
	 * 2016-06-21
	 * @param orgId
	 */
	int countOrgPerson(String orgId)throws Exception;

	/**
	 * 根据用户ids获取用户 信息
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<UserRedundancyInfoVO> getUserRedundancyListByUserId(String[] userIds) throws Exception, BaseException;
	/**
	 * 根据用户ids获取用户 信息
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<UserRedundancyExtInfoVO> getUserRedundancyExtListByUserId(String[] userIds) throws Exception;

	List<String> findDeptUserIdAllByDeptIds(List<String> deptIds) throws SQLException;

	/**
	 * 根据机构id获分页获取用户信息（用于接口）
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-9-22
	 * @version 1.0
	 */
	Pager findUsersByOrgIdForInterface(Map searchMap,Pager pager) throws Exception,BaseException;

	/**
	 * 查询机构的最新版本号
	 * @param orgId
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-10-11
	 * @version 1.0
     */
	Integer findVersionOrgByorgId(String orgId) throws Exception,BaseException;

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

	List<String> getUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws SQLException;

	/**
	 * 获取公众号用户信息
	 * @param openid
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * sufeng
	 * 2016-10-14
     */
	TbQyUserWxgzhPO getWxgzhUserById(String openid ,String orgId) throws Exception,BaseException;

	/**
	 * 获取公众号用户信息用于缓存
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * sufeng
	 * 2016-10-14
     */
	TbQyUserWxgzhVO getWxgzhUserVO(String userId, String orgId)throws Exception,BaseException;

	List<String> getUserInfoIdByUserIds(List<String> userIds) throws SQLException;

	List<TbQyUserInfoVO> findUsersVOByOrgId(Map<String, Object> paramMap) throws SQLException;

	List<TbQyUserInfoVO> findUsersVOByUserIds(Map<String, Object> paramMap) throws SQLException;

	List<UserRedundancyInfoVO> getUserRedundancysByWxUserIds(List<String> wxUserIds, String corpId) throws SQLException;

	/**
	 * 根据部门id获取统计负责人数量 2017-2-26
	 * @author tanwq
	 * @param departmentId 部门id
	 * @return int
	 * @throws Exception
	 * @throws BaseException
	 */
	Integer countReceiveUserByDepartmentId(String departmentId) throws Exception, BaseException;

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
	 * 根据userId转为wxUserid
	 * @param userIds
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -10-12
	 */
	List<String> getWxUserIdsByUserIds(List<String> userIds) throws SQLException;

	/**
	 * 根据wxUserId查询存在的wxUserid
	 * @param corpId
	 * @param wxUserIdList
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -10-12
	 */
	List<String> getWxUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws SQLException;

	List<TbQyUserInfoPO> findUserDepartIdsByUserIds(List<String> userIds) throws SQLException;

	/**
	 *根据部门id查询用户列表（接口专用）
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
}
