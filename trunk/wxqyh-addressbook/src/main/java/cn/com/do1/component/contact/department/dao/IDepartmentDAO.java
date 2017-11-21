package cn.com.do1.component.contact.department.dao;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.*;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IDepartmentDAO extends IBaseDAO {

	Pager searchDepartment(Map searchMap, Pager pager) throws Exception, BaseException;
	
	List<TbDepartmentInfoPO> getAllDepart(String orgId) throws Exception,BaseException;
	
	List<TbDepartmentInfoPO> searchDepartByName(String orgId,String name) throws Exception,BaseException;
	
	List<TbDepartmentInfoVO> getFirstDepart(String orgId) throws Exception,BaseException;
	
	List<TbDepartmentInfoVO> getChildDepart(String orgId,String departId) throws Exception,BaseException;
	
	/**根据部门id获取父部门信息 by tanwq 2015-6-10***/
	TbDepartmentInfoVO getParentDepart(String orgId,String departId) throws Exception,BaseException;
	/**
	 * 获得某部门下的子部门PO
	 * @param orgId
	 * @param departId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-20
	 * @version 1.0
	 */
	List<TbDepartmentInfoPO> getChildDepartPO(String orgId,String departId) throws Exception,BaseException;

	/**
	 * 根据关键字搜索部门
	 * @param orgId
	 * @param keyWord
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-6-24
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> getSearchDepart(String orgId, String keyWord) throws Exception,BaseException;
	
	List<TbDepartmentInfoPO> getDeptByParent(String orgId,String parenttId,String deptName) throws Exception,BaseException;
	
	List<TbDepartmentInfoPO> getChildDeptByFullName(String orgId, String deptFullName) throws Exception,BaseException;

	List<TbDepartmentInfoPO> searchDepartByName(String name) throws Exception,BaseException;

	/**
	 * 获取缩略部门，用户删除机构时同时删除所有部门
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-5
	 * @version 1.0
	 */
	List<DepCompress> getAllDepartCompress(String id) throws Exception,BaseException;

	/**
	 * 是否含有子部门
	 * @param organId
	 * @param deptId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-10
	 * @version 1.0
	 */
	boolean hasChildDepart(String organId, String deptId)throws Exception,BaseException;

	/**
	 * 是否存在该部门
	 * @param orgId
	 * @param keyWord
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-24
	 * @version 1.0
	 */
	boolean hasDepart(String orgId, String keyWord)throws Exception,BaseException;

	/**
	 * 查询部门信息
	 * @param departIds 部门id集合
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 * @author Sun Qinghai
	 * @2014-12-29
	 * @version 1.0
	 */
	List<TbDepartmentInfoPO> getDeptInfo(String[] departIds) throws Exception,SQLException;
	
	/**
	 * 
	 * 根据部门ids查询出所有部门全称的名称list
	 * @param departIds 部门id集合
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 * @author Sun Qinghai
	 * @2015-8-11
	 * @version 1.0
	 */
	List<String> getDeptFullNames(String[] departIds) throws Exception,SQLException;

	/**
	 * 获取用户的部门权限，排序按照权限从大到小
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-17
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> getDeptPermissionByUserId(String userId)throws Exception,BaseException;

	/**
	 * 根据微信id获取部门信息
	 * @param orgId
	 * @param wxId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-21
	 * @version 1.0
	 */
	TbDepartmentInfoPO getDeptByWeixin(String orgId, String wxId) throws Exception, BaseException;

	/**
	 * 根据用户id获取所属的所有部门id
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-3-10
	 * @version 1.0
	 */
	List<String> getDeptUserRefByUserId(String userId) throws Exception, BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-7-1
	 * 根据部门名称获取部门信息：【这里只是对外接口可用，理论上只有一个匹配的:若要其它地方使用，请慎重检查！】
	 * @param deptName
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbDepartmentInfoPO getDeptInfoPOByName(String deptName,String orgId)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-7-14
	 * 删除部门的默认负责人
	 * @param deptId
	 * @throws Exception
	 * @throws BaseException
	 */
	void delDeptReceiveByDeptId(String deptId)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-7-18
	 * 删除单个部门负责人
	 * @param deptId
	 * @param userId
	 * @throws Exception
	 * @throws BaseException
	 */
	void delDeptReceiveByDeptIdAndUserId(String deptId,String userId)throws Exception,BaseException;
	
	/**
	 * 查询部门的直接负责人列表
	 * @param deptId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> getDeptReceiveList(String deptId)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-8-10
	 * 获取机构下所有部门负责人
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> getAllDeptReceiveList(String orgId)throws Exception,BaseException;
	

	
	/**
	 * @author lishengtao
	 * 2015-8-13
	 * 删除导入部门负责人日志
	 * @param orgId
	 * @param type
	 * @throws Exception
	 * @throws BaseException
	 */
	void delDeptReceiveImportLog(String orgId,int type)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-8-13
	 * 查找部门负责人导入信息记录
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<ImportDeptToUserVO> getDeptReceiveImportLogList(Map<String,Object>params)throws Exception,BaseException;
	
	/**
	 * @author lishengtao
	 * 2015-8-14
	 * 搜索所有部门，按部门名称排序，为了方便显示层级关系
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbDepartmentInfoPO> getAllDepartOrderByDeptName(String orgId) throws Exception, BaseException;
	
	/**
	 * 根据orgID获取所有部门人数
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Luo Rilang
	 * @2015-9-23
	 * @version 1.0
	 */
	List<DepTotalUserVO> getDepTotalUserByOrgId(String orgId)throws Exception, BaseException;

	/**
	 * 根据部门名称获取一级部门的信息
	 * 
	 * @param deptName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-21
	 * @version 1.0
	 * @param orgId
	 */
	TbDepartmentInfoPO getDepartmentInfoByName(String deptName, String orgId) throws Exception, BaseException;

	/**
	 * 根据部门名称获取一级部门的VO
	 * 
	 * @param deptName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-7-21
	 * @version 1.0
	 * @param orgId
	 */
	TbDepartmentInfoVO getDepartmentVOByName(String deptName, String orgId) throws Exception, BaseException;

	/**
	 * 查询部门
	 * 
	 * @param map
	 * @return
	 * @author Hejinjiao
	 * @2014-9-30
	 * @version 1.0
	 */
	TbDepartmentInfoPO searchDepartment(Map map) throws Exception, BaseException;

	
	Pager searchPagerDept(Map searchValue, Pager pager)throws Exception, BaseException;

	/**
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2016-2-26
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> listDeptNodeByOrgid(String orgId) throws Exception;

	/**
	 * @param orgId
	 * @param deptFullName
	 * @return
	 * @author Sun Qinghai
	 * @2016-2-26
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> getlistDeptNodeByFullName(String orgId, String deptFullName) throws Exception;

	/**
	 * @param orgId
	 * @param deptList
	 * @return
	 * @author Sun Qinghai
	 * @2016-2-26
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> listDeptNodeByDeparts(String orgId,
			List<TbDepartmentInfoPO> deptList) throws Exception;

	/**
	 * @param orgId
	 * @param deptFullName
	 * @param deptList
	 * @return
	 * @author Sun Qinghai
	 * @2016-2-26
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> getlistDeptNodeByDepartsAndFullName(String orgId,
			String deptFullName, List<TbDepartmentInfoPO> deptList) throws Exception;

	/**
	 * 根据部门ids获取部门信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-27
	 */
	List<TbDepartmentInfoPO> getDeptsByIds(List<String> deptIds) throws Exception;

	/**
	 * 获取用户的部门信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-3
	 */
	List<TbDepartmentInfoVO> getDeptInfoByUserId(String orgId, String userId) throws SQLException;

	/**
	 * 获取一级部门和用户信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-3
	 */
	List<TbDepartmentInfoPO> getDeptByWxIds(String orgId, String[] wxIds) throws Exception;

	/**
	 * 获取一级部门和用户信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-3
	 */
	List<TbDepartmentInfoVO> getDeptByWxIds(String orgId, List<String> wxIds) throws Exception;

	List<TbDepartmentInfoVO> getDeptVOsByIds(List<String> deptIds) throws SQLException;

	List<TbQyUserDepartmentRefPO> getDeptUserRefPOByUserId(String userId) throws SQLException;

	/**
	 * 返回该部门的特定对象
	 * @param departId 部门id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-20
	 * @version 1.0
	 */
	List<TbDepaSpecificObjPO> getspecificObjByDepaId(String departId, String orgId) throws BaseException, Exception;

	/**
	 * 批量查询部门信息
	 * @param ids
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-22
	 * @version 1.0
     */
	List<SelectDeptVO> getDeptByIds(List<String> ids)throws BaseException, Exception;

	/**
	 *批量查询部门的特定对象
	 * @param deptIds 部门id列表
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-27
	 * @version 1.0
     */
	List<TbDepaSpecificObjPO> getspecificObjByIds(List<String> deptIds) throws BaseException, Exception;
}
