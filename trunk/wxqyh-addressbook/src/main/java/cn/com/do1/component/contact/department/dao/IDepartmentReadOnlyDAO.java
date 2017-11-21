package cn.com.do1.component.contact.department.dao;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.InterfaceDept;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;

import java.sql.SQLException;
import java.util.List;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IDepartmentReadOnlyDAO extends IBaseDAO {

	/**
	 * 获取用户部门关联数据
	 *
	 * @param userIds
	 * @return
	 * @throws Exception 这是一个异常
	 * @author Sun Qinghai @ 16-5-5
	 * @date 2016 -12-28
	 */
	List<TbQyUserDepartmentRefPO> getDeptUserRefList(List<String> userIds) throws Exception;

	/**
	 * 根据机构id获取所有的部门id集合
	 *
	 * @param orgId
	 * @return
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author Chen Feixiong @ 2016/6/21
	 * @date 2016 -12-28
	 */
	List<String> getAllDepartToIds(String orgId)throws Exception, BaseException;


	/**
	 * 通过父部门名称和orgId获取所有子部门人数
	 *
	 * @param orgId
	 * @param deptFullName
	 * @return
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author Luo Rilang
	 * @date 2016 -12-28
	 * @2015-9-25
	 * @version 1.0
	 */
	public Integer getDepTotalUser(String orgId, String deptFullName) throws Exception, BaseException;

	/**
	 * @param orgId
	 * @param deptFullName
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	List<String> getChildDeptIdsByFullName(String orgId, List<String> deptFullName) throws SQLException;

	/**
	 * @param deptIds
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	int getUserCountByDeptIds(List<String> deptIds) throws SQLException;

	/**
	 * @param deptIdList
	 * @param userIds
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	int getUserCountByDeptIdsAndUserId(List<String> deptIdList, String[] userIds) throws SQLException;

	/**
	 * 根据部门id返回部门信息
	 *
	 * @param departIds 部门id列表
	 * @param orgId     机构列表
	 * @return
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @date 2016 -12-28
	 * @2016-9-23
	 * @version 1.0
	 */
	List<InterfaceDept> getDepaByDepaId(List<String> departIds, String orgId) throws Exception,BaseException;

	/**
	 * 根据orgId返回该机构的全部部门
	 *
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception     异常抛出
	 * @author LiYiXin  2016-10-17
	 * @date 2016 -12-28
	 */
	List<InterfaceDept> getDepaByOrgId(String orgId)throws BaseException, Exception;

	/**
	 * @param orgId
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	int getCountUserAgentRef(String orgId) throws SQLException;

	/**
	 * 返回教育版所有的教学班级部门
	 *
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception     异常抛出
	 * @author LiYiXin  2016-12-26
	 * @date 2016 -12-28
	 */
	List<TbDepartmentInfoVO> getAllEduDept(String orgId) throws BaseException, Exception;

	/**
	 * 根据部门id获取部门用户关联信息
	 * @param id
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	List<String> getDeptUserRefByDeptId(String id) throws SQLException;

	/**
	 * 根据部门id获取部门用户关联信息
	 * @param ids
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2016 -12-28
	 */
	List<String> getDeptUserRefByDeptIds(List<String> ids) throws SQLException;

	List<TbQyUserDepartmentRefPO> getDeptUserRefPOByDeptIds(List<String> deptIds) throws SQLException;

	List<String> getWxDeptIdsByIds(List<String> deptIds) throws SQLException;

	List<String> getDeptIdsByWxIds(String orgId, List<String> wxDeptId) throws SQLException;

	/**
	 * 通过userIds查询部门信息
	 * @param userIds
	 * @param orgId
	 * @return
	 * @throws SQLException
     */
	List<TbDepartmentInfoPO> getDepartmentByUserIds(List<String> userIds, String orgId)throws SQLException;

	/**
	 * 根据部门全称获取部门列表
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-04-11
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> getChildDeptIdsByFullNameToList(String orgId, List<String> deptFullName) throws Exception;
}
