package cn.com.do1.component.contact.department.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.*;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IDepartmentMgrService extends IBaseService{
	/**
	 * 后台列表--分页查询
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchDepartment(Map searchMap, Pager pager) throws Exception, BaseException;

	/**
	 * 根据部门名称搜索部门
	 * @param orgId 组织编号
	 * @param name 部门名称
	 * @return 部门列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-27
	 * @version 1.0
	 */
	List<TbDepartmentInfoPO> searchDepartByName(String orgId,String name) throws Exception,BaseException;

	/**
	 * 获得某部门下的子部门
	 * @param orgId 组织编号
	 * @param departId 部门编号
	 * @return 部门列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-27
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> getChildDepart(String orgId,String departId) throws Exception,BaseException;
	/**
	 *  获得某部门下的子部门PO
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
	 * @return 部门列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-6-24
	 * @version 1.0
	 */
	List<TbDepartmentInfoVO> getSearchDepart(String orgId, String keyWord)throws Exception, BaseException;

	/**
	 * 按部门名称查询部门
	 * @param name 格式：部门名称,父部门微信编号
	 * @return 部门列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-27
	 * @version 1.0
	 */
	List<TbDepartmentInfoPO> searchDepartByName(String name)throws Exception,BaseException;

	/**
	 * 获取缩略部门，用户删除机构时同时删除所有部门
	 * @param id
	 * @return 微信部门信息 列表
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-11-5
	 * @version 1.0
	 */
	List<DepCompress> getAllDepartCompress(String id)throws Exception,BaseException;

	/**
	 * 查询部门表中是否存在有该关键字的部门
	 * @param orgId 组织编号
	 * @param keyWord  关键字
	 * @return true|false
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2014-12-24
	 * @version 1.0
	 */
	boolean hasDepart(String orgId, String keyWord)throws Exception, BaseException;

	/**
	 *
	 * 根据部门ids查询出所有部门全称的名称list
	 * @param departids 部门id集合
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-11
	 * @version 1.0
	 */
	public List<String> getDeptFullNames(String[] departIds)throws Exception, BaseException;

	/**
	 * 根据部门ids查询出所有部门全称的名称list
	 * @param departIds 部门id集合
	 * @param regex 部门id的分割字符，用于split(regex)
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-8-11
	 * @version 1.0
	 */
	public List<String> getDeptFullNames(String departIds, String regex)throws Exception, BaseException;

	/**
	 * 更新部门
	 * @param tbDepartmentInfoPO
	 * @param user
	 * @param updateAll
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-3-13
	 * @version 1.0
	 */
	List<DeptSyncInfoVO> updateDept(TbDepartmentInfoPO tbDepartmentInfoPO, UserOrgVO user, String isUseAll, String deptIds, String userIds,
									boolean updateAll)throws Exception, BaseException;

	/**
	 * @author lishengtao
	 * 2015-7-14
	 * 修改部门默认负责人
	 * @param deptId
	 * @param userIds
	 * @param isAdd
	 * @throws Exception
	 * @throws BaseException
	 */
	void updateDeptReceive(TbDepartmentInfoPO deptPO, String userIds, boolean isAdd) throws Exception, BaseException;

	/**
	 * 查询部门的直接负责人列表
	 * @param deptId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> getDeptReceiveList(String deptId,String orgId)throws Exception,BaseException;

	/**
	 * @author lishengtao
	 * 2015-8-10
	 * 获取机构下所有部门负责人
	 * @param deptId
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> getAllDeptReceiveList(String orgId)throws Exception,BaseException;



	/**
	 * @author lishengtao
	 * 2015-7-18
	 * 对userList进行离职，部门检查等判断
	 * @param list
	 * @param orgId
	 * @param deptId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyUserInfoVO> getCheckDeptReceiveList(List<TbQyUserInfoVO> list,String orgId,String deptId)throws Exception,BaseException;

	/**
	 * @author lishengtao
	 * 2015-8-10
	 * 导入部门负责人
	 * @param file
	 * @param fileName
	 * @param userOrgVO
	 * @param id
	 * @throws Exception
	 * @throws BaseException
	 */
	public void importDeptToUserExcel(File file,String fileName,UserOrgVO userOrgVO,String id)throws Exception,BaseException;

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
	 * 通过父部门名称和orgId获取所有子部门人数
	 * @param orgId
	 * @param deptFullName
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Luo Rilang
	 * @2015-9-25
	 * @version 1.0
	 */
	public Integer getDepTotalUser(String orgId, String deptFullName) throws Exception, BaseException;

	/**
	 * 根据部门名称获取一级部门的信息
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
	 * @param map
	 * @return
	 * @author Hejinjiao
	 * @2014-9-30
	 * @version 1.0
	 */
	TbDepartmentInfoPO searchDepartment(Map map)throws Exception, BaseException;

	/**
	 * 手机端部门列表查询
	 * @param searchValue
	 * @param pager
	 * @return
	 */
	Pager searchPagerDept(Map searchValue, Pager pager)throws Exception, BaseException;

	/**
	 * 获取企业的所有部门
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-2-26
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> getlistDeptNodeByOrgid(String orgId, String nodeId) throws Exception, BaseException;

	/**
	 * 普通管理员能看到的部门--马权阳
	 * @param orgId
	 * @param nodeId
	 * @param deptList
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-5-6
	 * @version 1.0
	 */
	List<TbQyOrganizeInfo> getListDeptNodeByDeparts(String orgId, String nodeId, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException;

	/**
	 * 获取一级部门和用户信息
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-3
	 */
	void getFirstDepartAndUser(String deptId, UserInfoVO user, String agentCode, Map<String, Object> params, List<TbDepartmentInfoVO> deptlist, Pager pager) throws Exception, BaseException;
	/**
	 * 获取子部门部门，如果deptId为空，获取用户可显示的一级部门
	 * @return
	 * @author Sun Qinghai
	 * @ 16-8-29
	 */
	List<TbDepartmentInfoVO> getChildDepart(String deptId, UserInfoVO user, String agentCode) throws Exception, BaseException;

	/**
	 * 获取用户部门关联数据
	 * @return
	 * @author Sun Qinghai
	 * @ 16-5-5
	 */
	List<TbQyUserDepartmentRefPO> getDeptUserRefList(List<String> userIds) throws Exception;

	/**
	 * 根据部门全称获
	 * @return
	 * @author Sun Qinghai
	 * @ 16-8-25
	 * @param orgId
	 * @param deptFullName
	 */
	List<String> getChildDeptIdsByFullName(String orgId, List<String> deptFullName) throws Exception;

	/**
	 * 根据部门id获取部门下的人员总数（去重后），不包括子部门的人员
	 * @return
	 * @author Sun Qinghai
	 * @ 16-8-25
	 * @param deptIds
	 */
	int getUserCountByDeptIds(List<String> deptIds) throws Exception;

	int getUserCountByDeptIdsAndUserId(List<String> deptIdList, String[] userIds) throws Exception;

	/**
	 * 根据部门id返回部门信息
	 * @param orgId
	 * @param departIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 *  @2016-9-23
	 * @version 1.0
	 */
	List<InterfaceDept> getDepaByDepaId(List<String> departIds, String orgId) throws Exception, BaseException;

	/**
	 * 根据orgId返回该机构的全部部门
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-17
	 */
	List<InterfaceDept> getDepaByOrgId(String orgId)throws BaseException, Exception;
	int getCountUserAgentRef(String orgId) throws SQLException;

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
	 * 新增部门
	 * @param deptIds 特定对象的部门id列表
	 * @param userIds 特定对象的用户列表
	 * @param org 机构
	 * @param selectUserIds 通知对象
	 * @param tbDepartmentInfoPO 部门po
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-21
	 * @version 1.0
     */
	void addDepart(String deptIds, String userIds, UserOrgVO org, String selectUserIds, TbDepartmentInfoPO tbDepartmentInfoPO) throws BaseException, Exception;

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

	/**
	 * 根据userId查询部门
	 * @param userIds
	 * @param orgId
	 * @return
	 * @throws BaseException
     * @throws Exception
     */
	List<TbDepartmentInfoPO> getDepartmentByUserIds(List<String> userIds, String orgId)throws BaseException, Exception;
}