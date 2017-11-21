package cn.com.do1.component.contact.department.dao.impl;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.InterfaceDept;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.dao.IDepartmentReadOnlyDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class DepartmentReadOnlyDAOImpl extends BaseDAOImpl implements IDepartmentReadOnlyDAO {

	private static final String getDeptUserRefList_sql = "select t.* from tb_qy_user_department_ref t where t.user_id in ( :userIds ) order by t.user_id,t.sort";
	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefList(List<String> userIds) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(10);
		map.put("userIds", userIds);
		return super.searchByField(TbQyUserDepartmentRefPO.class,getDeptUserRefList_sql,map);
	}

	private static final String getAllDepartToIds_sql = "SELECT id from tb_department_info where org_id=:orgId";
	@Override
	public List<String> getAllDepartToIds(String orgId) throws Exception, BaseException {
		this.preparedSql(getAllDepartToIds_sql);
		this.setPreLargeValue("orgId",orgId);
		return super.getList(String.class);
	}

	private static final String getDepTotalUser_sql = "SELECT COUNT(DISTINCT r.user_id) FROM TB_DEPARTMENT_INFO d, tb_qy_user_department_ref r where r.department_id = d.id and d.org_id = :orgId AND (d.dept_full_name LIKE :deptFullNameLike or d.dept_full_name = :deptFullName) ";
	@Override
	public Integer getDepTotalUser(String orgId, String deptFullName) throws Exception, BaseException {
		this.preparedSql(getDepTotalUser_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("deptFullName", deptFullName);
		this.setPreValue("deptFullNameLike", deptFullName+"->%");
		return executeCount();
	}

	private static final String getChildDeptIdsByFullName_sql = "select t.id from tb_department_info t where t.org_id =:orgId ";
	@Override
	public List<String> getChildDeptIdsByFullName(String orgId, List<String> deptFullName) throws SQLException {
		if(deptFullName == null || deptFullName.size()==0){
			return null;
		}
		StringBuffer sql = new StringBuffer(getChildDeptIdsByFullName_sql);
		int size = deptFullName.size();
		sql.append(" and (");
		Map<String,Object> map = new HashMap<String, Object>(size+1);
		map.put("orgId",orgId);
		for(int i=0; i<size;i++){
			if(i>0){
				sql.append(" or ");
			}
			sql.append("t.dept_full_name like :deptFullName").append(i);
			map.put("deptFullName"+i,deptFullName.get(i)+"->%");
		}
		sql.append(")");
		this.preparedSql(sql.toString());
		this.setPreValues(map);
		return this.getList(String.class);
	}

	private static final String getUserCountByDeptIds_sql = "select count(distinct t.user_id) from tb_qy_user_department_ref t where t.department_id in(:deptId) ";
	@Override
	public int getUserCountByDeptIds(List<String> deptIds) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("deptId",deptIds);
		return this.countByField(getUserCountByDeptIds_sql, map);
	}

	private static final String getUserCountByDeptIdsAndUserId_sql = "select count(distinct t.user_id) from tb_qy_user_department_ref t where t.department_id in(:deptId) and t.user_id in(:userId) ";
	@Override
	public int getUserCountByDeptIdsAndUserId(List<String> deptIdList, String[] userIds) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(2);
		map.put("deptId",deptIdList);
		map.put("userId",userIds);
		return this.countByField(getUserCountByDeptIdsAndUserId_sql, map);
	}

	private static final String getDepaByDepaId_sql = "select id,department_name,parent_depart,org_id,show_order,permission,dept_full_name,wx_id,wx_parentid from tb_department_info where id in ( :departIds ) and org_id = :orgId ";
	@Override
	public List<InterfaceDept> getDepaByDepaId(List<String> departIds, String orgId) throws Exception,BaseException{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("departIds", departIds);
		map.put("orgId", orgId);
		return super.searchByField(InterfaceDept.class, getDepaByDepaId_sql, map);
	}

	private static final String getDepaByOrgId_sql = "select id,department_name,parent_depart,org_id,show_order,permission,dept_full_name,wx_id,wx_parentid from tb_department_info where org_id = :orgId ";
	@Override
	public List<InterfaceDept> getDepaByOrgId(String orgId)throws BaseException, Exception{
		this.preparedSql(getDepaByOrgId_sql);
		this.setPreLargeValue("orgId", orgId);
		return super.getList(InterfaceDept.class);
	}
	private static final String getCountUserAgentRef_sql = "select count(1) from tb_qy_agent_user_ref where org_id = :orgId and user_id is null ";
	@Override
	public int getCountUserAgentRef(String orgId) throws SQLException {
		this.preparedSql(getCountUserAgentRef_sql);
		this.setPreValue("orgId",orgId);
		return this.executeCount();
	}

	private static final String getAllEduDept_sql = "select * from tb_department_info d where d.org_id = :orgId and d.attribute = 1 order by d.show_order";
	@Override
	public List<TbDepartmentInfoVO> getAllEduDept(String orgId) throws BaseException, Exception{
		this.preparedSql(getAllEduDept_sql);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	@Override
	public List<String> getDeptUserRefByDeptId(String id) throws SQLException {
		String sql="select t.id from tb_qy_user_department_ref t" +
				" where t.department_id =:id";
		this.preparedSql(sql);
		this.setPreValue("id", id);
		return super.getList(String.class);
	}

	@Override
	public List<String> getDeptUserRefByDeptIds(List<String> ids) throws SQLException {
		String sql="select t.id from tb_qy_user_department_ref t" +
				" where t.department_id in ( :ids )";
		Map<String, Object> map = new HashMap<String, Object>(ids.size()+1);
		map.put("ids", ids);
		return super.searchByField(String.class, sql, map);
	}

	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefPOByDeptIds(List<String> deptIds) throws SQLException {
		String sql="select t.* from tb_qy_user_department_ref t" +
				" where t.department_id in ( :ids )";
		Map<String, Object> map = new HashMap<String, Object>(deptIds.size()+1);
		map.put("ids", deptIds);
		return super.searchByField(TbQyUserDepartmentRefPO.class, sql, map);
	}

	@Override
	public List<String> getWxDeptIdsByIds(List<String> deptIds) throws SQLException {
		String sql="select t.wx_id from tb_department_info t" +
				" where t.id in (:ids) ";
		Map<String, Object> map = new HashMap<String, Object>(deptIds.size()+1);
		map.put("ids", deptIds);
		return super.searchByField(String.class, sql, map);
	}

	@Override
	public List<String> getDeptIdsByWxIds(String orgId, List<String> wxDeptId) throws SQLException {
		String sql = "select d.id from tb_department_info d where d.wx_id in (:wxIds) and d.org_id =:orgId";
		Map<String, Object> map = new HashMap<String, Object>(wxDeptId.size()+1);
		map.put("wxIds", wxDeptId);
		map.put("orgId", orgId);
		return super.searchByField(String.class, sql, map);
	}
	private static final String getDepartmentByUserIds_sql = "SELECT d.id,d.department_name,d.dept_full_name,d.attribute FROM " +
			" tb_department_info d, tb_qy_user_department_ref r WHERE d.id = r.department_id AND d.org_id = :orgId  " +
			" AND r.user_id in (:userIds) GROUP BY d.id ";
	@Override
	public List<TbDepartmentInfoPO> getDepartmentByUserIds(List<String> userIds, String orgId) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(userIds.size() + 1);
		map.put("userIds", userIds);
		map.put("orgId", orgId);
		return super.searchByField(TbDepartmentInfoPO.class, getDepartmentByUserIds_sql, map);
	}

	private static final String getChildDeptIdsByFullNameToList_sql = "select t.id,t.dept_full_name from tb_department_info t where t.org_id =:orgId ";
	@Override
	public List<TbDepartmentInfoVO> getChildDeptIdsByFullNameToList(String orgId, List<String> deptFullName) throws SQLException {
		if(deptFullName == null || deptFullName.size()==0){
			return new ArrayList<TbDepartmentInfoVO>();
		}
		StringBuffer sql = new StringBuffer(getChildDeptIdsByFullNameToList_sql);
		int size = deptFullName.size();
		sql.append(" and (");
		Map<String,Object> map = new HashMap<String, Object>(size+1);
		map.put("orgId",orgId);
		for(int i=0; i<size;i++){
			if(i>=1){
				sql.append(" or ");
			}
			sql.append("t.dept_full_name like :deptFullName").append(i);
			map.put("deptFullName"+i,deptFullName.get(i)+"->%");
		}
		sql.append(")");
		this.preparedSql(sql.toString());
		this.setPreValues(map);
		return this.getList(TbDepartmentInfoVO.class);
	}

}
