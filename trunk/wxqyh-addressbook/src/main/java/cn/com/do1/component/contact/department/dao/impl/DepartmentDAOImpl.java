package cn.com.do1.component.contact.department.dao.impl;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.vo.*;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.contact.department.dao.IDepartmentDAO;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class DepartmentDAOImpl extends BaseDAOImpl implements IDepartmentDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);
    final static String searchSQL = "select * from TB_DEPARTMENT_INFO where 1=1 and  org_id =:orgId and department_name like :departmentName  order by create_time desc"; 
    final static String countSQL = "select count(1) from TB_DEPARTMENT_INFO where 1=1 and org_id =:orgId and department_name like :departmentName ";
    
	@Override
	public Pager searchDepartment(Map searchMap, Pager pager) throws Exception,
			BaseException {
		return pageSearchByField( TbDepartmentInfoVO.class,countSQL,searchSQL,searchMap,pager);
	}

	public List<TbDepartmentInfoPO> searchDepartByName(String orgId,String name) throws Exception,BaseException{
		String sql = "select t.* from TB_DEPARTMENT_INFO t where 1=1 and t.org_id =:orgId and t.department_name = :departName order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("departName", name);
		return super.getList(TbDepartmentInfoPO.class);
	}

	@Override
	public List<TbDepartmentInfoPO> getAllDepart(String orgId) throws Exception,
			BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where 1=1 and t.org_id =:orgId order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoPO.class);
	}

	@Override
	public List<TbDepartmentInfoVO> getFirstDepart(String orgId) throws Exception,
			BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where t.parent_depart = '' and t.org_id =:orgId order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	@Override
	public List<TbDepartmentInfoVO> getChildDepart(String orgId,String departId)
			throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where t.parent_depart =:departId and t.org_id =:orgId order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("departId", departId);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	@Override
	public List<TbDepartmentInfoPO> getChildDepartPO(String orgId,String departId)
			throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where t.parent_depart =:departId and t.org_id =:orgId order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("departId", departId);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoPO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getSearchDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getSearchDepart(String orgId, String keyWord)
			throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t" +
				" where t.org_id =:orgId and t.department_name like :keyWord order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("keyWord", "%"+keyWord+"%");
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	@Override
	public List<TbDepartmentInfoPO> getDeptByParent(String orgId,
			String parenttId, String deptName) throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t" +
				" where t.org_id=:orgId and t.department_name =:deptName" +
				" and t.parent_depart =:parenttId" +
				" order by t.create_time desc";
		this.preparedSql(sql.toString());
		if(parenttId==null || parenttId.isEmpty()){
			parenttId = "";
		}
		this.setPreValue("parenttId", parenttId);
		this.setPreValue("orgId", orgId);
		this.setPreValue("deptName", deptName);
		return super.getList(TbDepartmentInfoPO.class);
	}

	@Override
	public List<TbDepartmentInfoPO> getChildDeptByFullName(String orgId,
			String deptFullName) throws Exception, BaseException {
		String sql = "select t.* from tb_department_info t where t.dept_full_name like :deptFullName and t.org_id =:orgId ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("deptFullName", deptFullName);
		return getList(TbDepartmentInfoPO.class);
	}

	@Override
	public List<TbDepartmentInfoPO> searchDepartByName(String name)
			throws Exception, BaseException {
		String sql;
		String[] temp = name.split(",");
		if(temp.length==3){
			sql = "select t.* from TB_DEPARTMENT_INFO t,tb_dqdp_organization o" +
				" where t.org_id= o.ORGANIZATION_ID and t.department_name = :departName and o.ORGANIZATION_NAME =:parentwxid";
			temp[1] = temp[2];
		}
		else{
			sql = "select t.* from TB_DEPARTMENT_INFO t,TB_DEPARTMENT_INFO p" +
				" where t.parent_depart= p.id and t.department_name = :departName and p.wx_id =:parentwxid";
		}
		this.preparedSql(sql);
		this.setPreValue("departName", temp[0]);
		this.setPreValue("parentwxid", temp[1]);
		return super.getList(TbDepartmentInfoPO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getAllDepartCompress(java.lang.String)
	 */
	@Override
	public List<DepCompress> getAllDepartCompress(String id) throws Exception,
			BaseException {
		String sql = "SELECT t.id,t.wx_id,t.wx_parentid,t.department_name,t.parent_depart, count(d.id) as child_Time" +
				" from tb_department_info t LEFT JOIN tb_department_info d ON t.id=d.parent_depart" +
				" WHERE t.org_id=:orgId GROUP BY t.id";
		this.preparedSql(sql);
		this.setPreValue("orgId", id);
		return super.getList(DepCompress.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#hasChildDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasChildDepart(String organId, String deptId)
			throws Exception, BaseException {
		String sql = "select count(t.id) from TB_DEPARTMENT_INFO t where t.parent_depart =:departId and t.org_id =:orgId order by t.show_order ";
		this.preparedSql(sql);
		this.setPreValue("departId", deptId);
		this.setPreValue("orgId", organId);
		if(super.executeCount()>0){
			return true;
		}
		return false;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#hasDepart(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasDepart(String orgId, String keyWord) throws Exception,
			BaseException {
		String sql = "select count(t.id) from TB_DEPARTMENT_INFO t where t.org_id =:orgId and t.department_name like :keyWord";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("keyWord", "%"+keyWord+"%");
		if(super.executeCount()>0){
			return true;
		}
		return false;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDeptInfo(java.lang.String[])
	 */
	@Override
	public List<TbDepartmentInfoPO> getDeptInfo(String[] departIds) throws Exception,SQLException {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		StringBuffer s = new StringBuffer("select * from tb_department_info where id in ( ");
		getSearchDeptSqlByIds(departIds, s, searchMap);
		s.append(" )");
		this.preparedSql(s.toString());
		this.setPreValues(searchMap);
		return super.getList(TbDepartmentInfoPO.class);
	}
	
	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDeptFullNames(java.lang.String[])
	 */
	@Override
	public List<String> getDeptFullNames(String[] departIds) throws Exception,SQLException {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		StringBuffer s = new StringBuffer("select dept_full_name from tb_department_info where id in ( ");
		getSearchDeptSqlByIds(departIds, s, searchMap);
		s.append(" )");
		this.preparedSql(s.toString());
		this.setPreValues(searchMap);
		return super.getList(String.class);
	}
	
	/**
	 * 组装部门表的查询sql和条件，将sql和查询参数存放到传入的参数中
	 * @param departIds
	 * @param sql
	 * @param searchMap
	 * @author Sun Qinghai
	 * @2015-8-11
	 * @version 1.0
	 */
	private void getSearchDeptSqlByIds(String[] departIds, StringBuffer sql, Map<String, Object> searchMap) {
		for(int x=0;x<departIds.length;x++){
			if(x==0)
				sql.append(" :id").append(x);
			else
				sql.append(" , :id").append(x);
			searchMap.put("id"+x, departIds[x]);
		}
	}

	private static final String getDeptPermissionByUserId_sql="select d.id,d.department_name,d.parent_depart,d.org_id,d.dept_full_name,d.wx_id,d.wx_parentid,d.permission,d.total_user,d.show_order" +
			" from tb_department_info d,tb_qy_user_department_ref ud" +
			" where d.id=ud.department_id and ud.user_id =:userId" +
			" order by d.permission asc";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDeptPermissionByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getDeptPermissionByUserId(String userId) throws Exception, BaseException {
		this.preparedSql(getDeptPermissionByUserId_sql);
		this.setPreValue("userId", userId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDeptByWeixin(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoPO getDeptByWeixin(String orgId, String wxId) throws Exception, BaseException {
		String sql = "select t.* from tb_department_info t where t.wx_id = :wxId and t.org_id =:orgId ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("wxId", wxId);
		return super.executeQuery(TbDepartmentInfoPO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDeptUserRefByUserId(java.lang.String)
	 */
	@Override
	public List<String> getDeptUserRefByUserId(String userId)
			throws Exception, BaseException {
		String sql="select t.department_id from tb_qy_user_department_ref t" +
				" where t.user_id =:userId order by t.sort";
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		return super.getList(String.class);
	}

	@Override
	public TbDepartmentInfoVO getParentDepart(String orgId, String departId) throws Exception, BaseException {
		// TODO Auto-generated method stub
		String sql="select * from tb_department_info where id=:departId and org_id=:orgId";
		this.preparedSql(sql);
		this.setPreValue("departId", departId);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(TbDepartmentInfoVO.class);
	}

	@Override
	public TbDepartmentInfoPO getDeptInfoPOByName(String deptName, String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT * FROM tb_department_info WHERE org_id=:orgId and department_name=:deptName";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("deptName", deptName);
		return this.executeQuery(TbDepartmentInfoPO.class);
	}

	@Override
	public void delDeptReceiveByDeptId(String deptId) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		String sql="DELETE FROM tb_qy_user_receive WHERE department_id=:deptId";
		this.preparedSql(sql);
		this.setPreValue("deptId", deptId);
		this.executeUpdate();
	}

	@Override
	public List<TbQyUserInfoVO> getDeptReceiveList(String deptId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT b.* FROM tb_qy_user_receive a LEFT JOIN tb_qy_user_info b on a.user_id=b.USER_ID WHERE a.department_id=:deptId and b.user_status <> :aliveStatus";
		this.preparedSql(sql);
		this.setPreValue("deptId", deptId);
		this.setPreValue("aliveStatus", "-1");
		return this.getList(TbQyUserInfoVO.class);
	}

	@Override
	public void delDeptReceiveByDeptIdAndUserId(String deptId, String userId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="DELETE FROM tb_qy_user_receive WHERE department_id=:deptId and user_id=:userId";
		this.preparedSql(sql);
		this.setPreValue("deptId", deptId);
		this.setPreValue("userId", userId);
		this.executeUpdate();
	}

	@Override
	public List<TbQyUserInfoVO> getAllDeptReceiveList(String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT b.* FROM tb_qy_user_receive a LEFT JOIN tb_qy_user_info b on a.user_id=b.USER_ID WHERE a.org_id=:orgId and b.user_status <> :aliveStatus";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("aliveStatus", "-1");
		return this.getList(TbQyUserInfoVO.class);
	}

	@Override
	public void delDeptReceiveImportLog(String orgId,int type) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		String sql="DELETE FROM tb_qy_dept_receive_import_log WHERE org_id=:orgId AND type=:type";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("type", type);
		this.executeUpdate();
	}

	@Override
	public List<ImportDeptToUserVO> getDeptReceiveImportLogList(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT dept_id as department_id,dept_name as department_name,dept_full_name,person_names,wx_user_ids,msg" +
				" FROM tb_qy_dept_receive_import_log WHERE org_id=:orgId AND type=:type AND status=:status";
		return this.searchByField(ImportDeptToUserVO.class, sql, params);
	}

	@Override
	public List<TbDepartmentInfoPO> getAllDepartOrderByDeptName(String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql = "select t.* from TB_DEPARTMENT_INFO t where 1=1 and t.org_id =:orgId order by t.dept_full_name,t.wx_id";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepartmentInfoPO.class);
	}

	@Override
	public List<DepTotalUserVO> getDepTotalUserByOrgId(String orgId)
			throws Exception, BaseException {
		String sql = "select d.id,d.dept_full_name,d.parent_depart from TB_DEPARTMENT_INFO d where d.org_id = :orgId ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(DepTotalUserVO.class);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDepartmentInfoByName(java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoPO getDepartmentInfoByName(String deptName, String orgId) throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where t.dept_full_name =:deptName and t.org_id=:orgId";

		preparedSql(sql);
		setPreValue("deptName", deptName);
		setPreValue("orgId", orgId);
		return super.executeQuery(TbDepartmentInfoPO.class);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getDepartmentVOByName(java.lang.String, java.lang.String)
	 */
	@Override
	public TbDepartmentInfoVO getDepartmentVOByName(String deptName, String orgId) throws Exception, BaseException {
		String sql = "select t.* from TB_DEPARTMENT_INFO t where t.dept_full_name =:deptName and t.org_id=:orgId";

		preparedSql(sql);
		setPreValue("deptName", deptName);
		setPreValue("orgId", orgId);
		List<TbDepartmentInfoVO> list = super.getList(TbDepartmentInfoVO.class);

		return list == null || list.size() == 0 ? null : list.get(0);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#searchDepartment(java.util.Map)
	 */
	@Override
	public TbDepartmentInfoPO searchDepartment(Map map) throws Exception, BaseException {
		String sql = "SELECT * FROM tb_department_info where id = :id ";
		this.preparedSql(sql);
		this.setPreValue("id", map.get("deptId"));
		return this.executeQuery(TbDepartmentInfoPO.class);
	}

	@Override
	public Pager searchPagerDept(Map searchValue, Pager pager)
			throws Exception, BaseException {
		String str=(String) searchValue.get("parentId");
		if(AssertUtil.isEmpty(str)){
			str="parent_depart='' ";
		}else{
			str="parent_depart=:parentId ";
		}
		// TODO 自动生成的方法存根
		String sql ="select * from tb_department_info where 1=1 and org_id = :orgId and "+str+
				" and department_name like :title order by create_time asc ";
		String countSql =" select count(1) from tb_department_info where 1=1 and org_id = :orgId and "+str+
				" and department_name like :title ";
		return pageSearchByField(TbDepartmentInfoPO.class, countSql, sql, searchValue, pager);
	}

	private static final String listDeptNodeByOrgid_sql = "select t.department_name as node_name,t.id as node_id," +
			" t.parent_depart as parent_id,t.dept_full_name as node_title,t.permission,t.attribute "
			+ " from TB_DEPARTMENT_INFO t where t.org_id =:orgId order by t.show_order ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#listDeptNodeByOrgid(java.lang.String)
	 */
	@Override
	public List<TbQyOrganizeInfo> listDeptNodeByOrgid(String orgId)
			throws Exception {
		this.preparedSql(listDeptNodeByOrgid_sql);
		this.setPreValue("orgId", orgId);
		// this.setPreValue("nodeId", nodeId);
		return getList(TbQyOrganizeInfo.class);
	}

	private static final String getlistDeptNodeByFullName_sql = "select t.department_name as node_name,t.id as node_id,"
			+ "t.parent_depart as parent_id,t.dept_full_name as node_title,t.permission,t.attribute "
			+ " from TB_DEPARTMENT_INFO t where t.org_id =:orgId and t.dept_full_name like :deptFullName order by t.show_order ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getlistDeptNodeByFullName(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyOrganizeInfo> getlistDeptNodeByFullName(String orgId,
			String deptFullName) throws Exception {
		this.preparedSql(getlistDeptNodeByFullName_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("deptFullName", deptFullName);
		return getList(TbQyOrganizeInfo.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#listDeptNodeByDeparts(java.lang.String, java.util.List)
	 */
	@Override
	public List<TbQyOrganizeInfo> listDeptNodeByDeparts(String orgId,
			List<TbDepartmentInfoPO> deptList) throws Exception {
		StringBuilder dataSql = new StringBuilder("select t.department_name as node_name,t.id as node_id,t.parent_depart as parent_id"
				+ ",t.dept_full_name as node_title,t.permission,t.attribute"
				+ " from TB_DEPARTMENT_INFO t where t.org_id =:orgId");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getId());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					paramDepart.append(" t.id = :departs" + i + "0 or t.dept_full_name like :departs" + i + "1 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				dataSql.append(" and (").append(temp).append(")");
			}
		}
		dataSql.append(" order by t.show_order ");
		searchMap.put("orgId", orgId);
		this.preparedSql(dataSql.toString());
		// this.setPreValue("orgId", orgId);
		// this.setPreValue("nodeId", nodeId);
		this.setPreValues(searchMap);
		return getList(TbQyOrganizeInfo.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.department.dao.IDepartmentDAO#getlistDeptNodeByDepartsAndFullName(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<TbQyOrganizeInfo> getlistDeptNodeByDepartsAndFullName(
			String orgId, String deptFullName, List<TbDepartmentInfoPO> deptList)
			throws Exception {
		StringBuilder dataSql = new StringBuilder("select t.department_name as node_name,t.id as node_id,t.parent_depart as parent_id"
				+ ",t.dept_full_name as node_title,t.permission,t.attribute "
				+ " from TB_DEPARTMENT_INFO t where t.org_id =:orgId and t.dept_full_name like :deptFullName ");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getId());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					paramDepart.append(" t.id = :departs" + i + "0 or t.dept_full_name like :departs" + i + "1 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				dataSql.append(" and (").append(temp).append(")");
			}
		}
		dataSql.append(" order by t.show_order ");
		searchMap.put("orgId", orgId);
		searchMap.put("deptFullName", deptFullName);
		this.preparedSql(dataSql.toString());
		// this.setPreValue("orgId", orgId);
		// this.setPreValue("nodeId", nodeId);
		this.setPreValues(searchMap);
		return getList(TbQyOrganizeInfo.class);
	}

	/**
	 * 根据部门ids获取部门信息
	 *
	 * @param deptIds
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-27
	 */
	private static final String getDeptsByIds_sql = "select t.* from TB_DEPARTMENT_INFO t where t.id in ( :id )";
	@Override
	public List<TbDepartmentInfoPO> getDeptsByIds(List<String> deptIds) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("id", deptIds);
		return super.searchByField(TbDepartmentInfoPO.class,getDeptsByIds_sql,map);
	}
	private static final String getDeptInfoByUserId_sql = "select d.id,d.department_name,d.parent_depart,d.org_id,d.dept_full_name,d.wx_id,d.wx_parentid,d.permission"
			+ " from tb_department_info d,tb_qy_user_department_ref ud"
			+ " where d.id=ud.department_id and ud.user_id =:userId and d.org_id =:orgId"
			+ " order by ud.sort asc";
	@Override
	public List<TbDepartmentInfoVO> getDeptInfoByUserId(String orgId, String userId) throws SQLException {
		this.preparedSql(getDeptInfoByUserId_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("userId", userId);
		return super.getList(TbDepartmentInfoVO.class);
	}

	private static final String getDeptByWxIds_sql = "select t.* from tb_department_info t where t.wx_id in(:wxIds) and t.org_id =:orgId order by t.show_order";
	@Override
	public List<TbDepartmentInfoPO> getDeptByWxIds(String orgId, String[] wxIds) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("orgId",orgId);
		map.put("wxIds",wxIds);
		return this.searchByField(TbDepartmentInfoPO.class,getDeptByWxIds_sql,map);
	}

	private static final String getDeptByWxIds_list_sql = "select d.id,d.department_name,d.parent_depart,d.org_id,d.dept_full_name,d.wx_id,d.wx_parentid,d.permission" +
			" from tb_department_info d where d.wx_id in(:wxIds) and d.org_id =:orgId";
	@Override
	public List<TbDepartmentInfoVO> getDeptByWxIds(String orgId, List<String> wxIds) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("orgId",orgId);
		map.put("wxIds",wxIds);
		return this.searchByField(TbDepartmentInfoVO.class,getDeptByWxIds_list_sql,map);
	}

	private static final String getDeptVOsByIds_sql = "select d.id,d.department_name,d.parent_depart,d.org_id,d.dept_full_name,d.wx_id,d.wx_parentid,d.permission,d.total_user,d.show_order" +
			" from tb_department_info d where d.id in(:id)";
	@Override
	public List<TbDepartmentInfoVO> getDeptVOsByIds(List<String> deptIds) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("id", deptIds);
		return super.searchByField(TbDepartmentInfoVO.class,getDeptVOsByIds_sql,map);
	}

	private static final String getDeptUserRefPOByUserId_sql = "select * from tb_qy_user_department_ref t where t.user_id = :userId order by t.sort";
	@Override
	public List<TbQyUserDepartmentRefPO> getDeptUserRefPOByUserId(String userId) throws SQLException {
		this.preparedSql(getDeptUserRefPOByUserId_sql);
		this.setPreValue("userId",userId);
		return this.getList(TbQyUserDepartmentRefPO.class);
	}

	private static final String getspecificObjByDepaId_sql = "select o.* from tb_depa_specific_obj o where o.depart_id = :departId and o.org_id = :orgId";
	@Override
	public List<TbDepaSpecificObjPO> getspecificObjByDepaId(String departId, String orgId) throws BaseException, Exception{
		this.preparedSql(getspecificObjByDepaId_sql);
		this.setPreValue("departId", departId);
		this.setPreValue("orgId", orgId);
		return super.getList(TbDepaSpecificObjPO.class);
	}

	private static final String getDeptByIds_sql = "select d.id as dept_id,d.department_name as dept_name from tb_department_info d where d.id in (:ids)";
	@Override
	public  List<SelectDeptVO> getDeptByIds(List<String> ids)throws BaseException, Exception{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("ids", ids);
		return super.searchByField(SelectDeptVO.class,getDeptByIds_sql,map);
	}

	private static final String getspecificObjByIds_sql = "select o.* from tb_depa_specific_obj o where o.depart_id in(:deptIds) group by o.obj_id";
	@Override
	public List<TbDepaSpecificObjPO> getspecificObjByIds(List<String> deptIds) throws BaseException, Exception{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("deptIds", deptIds);
		return super.searchByField(TbDepaSpecificObjPO.class,getspecificObjByIds_sql,map);
	}
} 
