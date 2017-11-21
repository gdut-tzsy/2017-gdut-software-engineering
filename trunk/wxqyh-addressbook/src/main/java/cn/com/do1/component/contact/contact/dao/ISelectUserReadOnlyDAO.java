package cn.com.do1.component.contact.contact.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;

/**
 * 只读DAO专用
 * 
 * @date 2015-10-28
 */
public interface ISelectUserReadOnlyDAO extends IBaseDAO {

	/**
	 * 查询组织下所有通讯录项目设置字段
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-6-17
	 * @version 1.0
	 */
	public List<TbQyFieldSettingVO> findTbQyFieldSettingVOListByOrgId(
			String orgId) throws Exception, BaseException;

	Pager searchByNameOrPhone(Map searchMap, Pager pager,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

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
	Pager findAlluserByDeptId(Map searchMap, Pager pager,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	Pager searchContactByPy(Map searchMap, Pager pager) throws Exception,
			BaseException;

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
	Pager searchFirstLetter(Map searchMap, Pager pager,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException;

	List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit,
			String agentCode, String corpId) throws Exception, BaseException;

	Pager getUserGroup(Pager pager, Map<String, Object> map) throws Exception,
			BaseException;

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
	TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception,
			BaseException;

	/**
	 * 根据机构ID获取默认群组
	 * 
	 * @param orgId
	 * @return
	 * @author Chen Feixiong 2014-11-25
	 */
	List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception,
			BaseException;

	Pager getUserGroupPerson(Pager pager, Map<String, Object> map)
			throws Exception, BaseException;
	/**
	 * 查询可见范围用户
	 * @param orgId
	 * @param agentCode
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Luo Rilang
	 * @2015-11-24
	 * @version 1.0
	 */
	List<String> getVisibleRangeUsers(String orgId, String agentCode) throws Exception, BaseException;


	Pager searchContact(Map searchMap, Pager pager) throws Exception, BaseException;

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
	 * 根据用户名获取用户部门信息
	 *
	 * @return
	 * @author 陈春武
	 * @version 1.0
	 */
	List<UserDeptInfoVO> findUserDeptInfosByWxUIdndOrgId(String[] wxuid,String orgId) throws Exception, BaseException;

	/**
	 * 查询用户信息，根据可见范围和目标部门和目标用户筛选
	 * @param searchMap 查询条件
	 * @return 返回所有的用户list
	 * @author Sun Qinghai
	 * @2017-3-7
	 */
	List<TbQyUserInfoForPage> searchContactList(Map<String, Object> searchMap) throws Exception, BaseException;

	/**
	 * 查询用户信息，根据可见范围和目标部门和目标用户筛选
	 * @param searchMap 查询条件
	 * @return 返回所有的用户list
	 * @author Sun Qinghai
	 * @2017-3-7
	 */
	List<UserRedundancyInfoVO> searchUserRedundancyList(Map<String, Object> searchMap) throws Exception;

	/**
	 * 查询用户信息，根据可见范围和目标部门和目标用户筛选
	 * @param searchMap 查询条件
	 * @return 返回所有的用户list
	 * @author Sun Qinghai
	 * @2017-4-17
	 */
	List<TbQyUserInfoVO> searchUserInfoVOList(Map<String, Object> searchMap) throws Exception, BaseException;

	/**
	 * 获取应用可见范围内的用户总数
	 * @param searchMap 查询条件
	 * @return
	 * @throws BaseException 这是异常啊，哥
	 * @throws Exception 这是异常啊，哥
	 * @author liyixin
	 * @2017-6-30
	 * @version 1.0
	 */
	int serachUserCount(Map<String, Object> searchMap) throws BaseException, Exception;
}
