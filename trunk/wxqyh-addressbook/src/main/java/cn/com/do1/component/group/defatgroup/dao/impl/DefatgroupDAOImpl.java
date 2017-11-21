package cn.com.do1.component.group.defatgroup.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.group.defatgroup.dao.IDefatgroupDAO;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyGivenVO;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyOldGivenVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class DefatgroupDAOImpl extends BaseDAOImpl implements IDefatgroupDAO {
	private final static transient Logger logger = LoggerFactory.getLogger(DefatgroupDAOImpl.class);

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#ajaxPageSearch(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager ajaxPageSearch(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
		String sql = "select g.* from tb_qy_user_group g where 1=1 and g.group_name like :title and g.org_Id=:orgId order by g.show_num ";
		String countSql = "select count(1) from tb_qy_user_group g where 1=1 and g.group_name like :title and g.org_Id=:orgId ";
		return pageSearchByField(TbQyUserGroupPO.class, countSql, sql, searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#getUserGroupPerson(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getUserGroupPerson(String id) throws Exception, BaseException {
		String sql = "select g.*,u.PERSON_NAME,u.head_pic from tb_qy_user_group_person g,tb_qy_user_info u " + "where g.user_id=u.USER_ID and g.group_id =:id ";
		this.preparedSql(sql);
		this.setPreValue("id", id);
		return getList(TbQyUserGroupPersonVO.class);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#updateStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateStatus(String id, String status) throws Exception, BaseException {
		String sql = "update tb_qy_user_group set status =:status where group_id =:group_id ";
		this.preparedSql(sql);
		this.setPreValue("group_id", id);
		this.setPreValue("status", status);
		this.executeUpdate();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#getGroupList(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> getGroupList(String orgId) throws Exception, BaseException {
		String sql = "select g.* from tb_qy_user_group g where 1=1 and g.org_id=:orgId and g.status ='0' order by g.create_time desc";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return getList(TbQyUserGroupPO.class);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#pageGroupUsers(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager pageGroupUsers(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
		searchMap.put("aliveStatus", "-1");
		String sql = "select g.*,u.PERSON_NAME,u.head_pic,u.MOBILE from tb_qy_user_group_person g,tb_qy_user_info u where 1=1 and g.user_id=u.USER_ID and g.group_id =:id and u.user_status <> :aliveStatus ";
		String countSql = "select count(1) from tb_qy_user_group_person g,tb_qy_user_info u where 1=1 and g.user_id=u.USER_ID  and g.group_id =:id and u.user_status <> :aliveStatus ";
		return pageSearchByField(TbQyUserGroupPersonVO.class, countSql, sql, searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#getUserByGroups(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getUserByGroups(String id) throws Exception, BaseException {
		String[] ids = id.split("\\|");
		String ids1 = "";
		if (ids.length >= 2) {
			for (String id1 : ids) {
				ids1 += "'" + id1 + "',";
			}
			ids1 = ids1.substring(0, ids1.length() - 1);
		} else {
			ids1 = "'" + ids[0] + "'";
		}
		String sql = "select g.*,u.PERSON_NAME,u.head_pic from tb_qy_user_group_person g,tb_qy_user_info u where 1=1 and g.user_id=u.USER_ID and u.user_status <> '-1' and g.group_id in(" + ids1
				+ ") GROUP BY u.USER_ID ";
		super.preparedSql(sql);
		return getList(TbQyUserGroupPersonVO.class);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#deleteGivenPO(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteGivenPO(String foreignId, String orgId) throws Exception {
		String sql = "delete from tb_qy_given where template_id = :foreignId and org_id = :orgId";
		preparedSql(sql);
		this.setPreValue("foreignId", foreignId);
		this.setPreValue("orgId", orgId);
		this.executeUpdate();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#getGivenByForeignId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyGivenVO> getGivenByForeignId(String foreignId, String orgId) throws Exception, BaseException {
		String sql = "select t.person_name,t.rec_id user_id,t.head_pic,t.type from tb_qy_given t where 1=1 and t.template_id =:foreignId  and t.org_id = :orgId" + " order by t.sort asc ";
		this.preparedSql(sql);
		this.setPreValue("foreignId", foreignId);
		this.setPreValue("orgId", orgId);
		return super.getList(TbQyGivenVO.class);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.defatgroup.defatgroup.dao.IDefatgroupDAO#findDefatgroup(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> findDefatgroup(String groupName, String orgId) throws Exception, BaseException {
		String sql = "SELECT g.* from tb_qy_user_group g where 1=1 and g.org_id ='" + orgId + "' and g.group_name = '" + groupName + "' ";
		this.preparedSql(sql);
		return getList(TbQyUserGroupPO.class);
	}

	@Override
	public List<TbQyGivenVO> getGivenByFormId(String foreignId, String orgId, String type) throws Exception, BaseException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer(
				"select t.GIVEN_ID,t.person_name,t.rec_id as user_id,t.head_pic,t.type,t.wx_user_id from tb_qy_given t where 1=1 and t.template_id =:foreignId  and t.org_id = :orgId ");
		if (!AssertUtil.isEmpty(type)) {
			sql.append(" and type=:type");
		}
		sql.append(" order by t.sort asc ");
		this.preparedSql(sql.toString());
		this.setPreValue("foreignId", foreignId);
		this.setPreValue("orgId", orgId);
		if (!AssertUtil.isEmpty(type)) {
			this.setPreValue("type", type);
		}
		return super.getList(TbQyGivenVO.class);
	}

	private final static String findQyUserGroupInfoByIds = "select g.* from tb_qy_user_group g  where g.group_id in";

	@Override
	public List<TbQyUserGroupPO> findQyUserGroupInfoByIds(String[] groupIds) throws Exception, BaseException {
		// TODO Auto-generated method stub
		preparedSql(findQyUserGroupInfoByIds + "('" + StringUtil.uniteArry(groupIds, "','") + "')");
		return super.getList(TbQyUserGroupPO.class);
	}

	@Override
	public List<TbQyOldGivenVO> findTbQyOldGivenVOByUserId(String userId, String orgId, String type, String applyType, String childApplyType) throws Exception, BaseException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("select given_id,person_name,rec_id as user_id,head_pic,type from tb_qy_old_given where create_person=:userId and type=:type and apply_type=:applyType ");
		if (!AssertUtil.isEmpty(childApplyType)) {
			sql.append(" and child_apply_type=:childApplyType ");
		}
		sql.append("   and org_id=:orgId order by sort asc");
		this.preparedSql(sql.toString());
		this.setPreValue("userId", userId);
		this.setPreValue("orgId", orgId);
		this.setPreValue("type", type);
		this.setPreValue("applyType", applyType);
		if (!AssertUtil.isEmpty(childApplyType)) {
			this.setPreValue("childApplyType", childApplyType);
		}
		return this.getList(TbQyOldGivenVO.class);
	}

	@Override
	public void delTbQyOldGivenVOByUserId(String userId, String orgId,String type, String applyType, String childApplyType) throws Exception, BaseException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("delete from tb_qy_old_given where create_person=:userId and apply_type=:applyType ");
		if(!AssertUtil.isEmpty(type)){
			sql.append(" and type=:type ");
		}
		if (!AssertUtil.isEmpty(childApplyType)) {
			sql.append(" and child_apply_type=:childApplyType ");
		}
		sql.append("  and org_id=:orgId");
		this.preparedSql(sql.toString());
		this.setPreValue("userId", userId);
		this.setPreValue("orgId", orgId);
		this.setPreValue("applyType", applyType);
		if (!AssertUtil.isEmpty(childApplyType)) {
			this.setPreValue("childApplyType", childApplyType);
		}
		if (!AssertUtil.isEmpty(type)) {
			this.setPreValue("type",type);
		}
		this.executeUpdate();
	}
	
	private final static String  updateUserGroupSum_sql="UPDATE tb_qy_user_group a SET sum="
								+"(SELECT count(1) FROM tb_qy_user_group_person b WHERE a.group_id=b.group_id)"
								+"WHERE a.org_id=:orgId";
	@Override
	public void updateUserGroupSum(String orgId) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(updateUserGroupSum_sql);
		this.setPreValue("orgId", orgId);
		this.executeUpdate();
	}

	@Override
	public void batchDeleteGroup(String[] ids) throws Exception, BaseException {
		StringBuilder sb = new StringBuilder("");
		for (String str : ids) {
			sb.append("'" + str + "',");
		}
		String sql = "delete from tb_qy_user_group_person where group_id in (" + sb.toString().substring(0, sb.length() - 1) + ")";
		this.executeSql(sql);

	}

	@Override
	public List<TbQyUserGroupPO> getUserGroup(Map<String, Object> map) throws Exception, BaseException {
		String sql = "select * from tb_qy_user_group where creator=:userId";
		if (!AssertUtil.isEmpty(map.get("group_name"))) {
			sql += " and group_name='" + map.get("group_name") + "'";
		}
		preparedSql(sql);
		setPreValue("userId", map.get("user_id"));
		return getList(TbQyUserGroupPO.class);
	}

	@Override
	public List<TbQyUserGroupPersonVO> getUserGroupPerson(Map<String, Object> map) throws Exception, BaseException {
		String sql = "select g.*,u.PERSON_NAME,u.head_pic from tb_qy_user_group_person g,tb_qy_user_info u " + "where g.user_id=u.USER_ID and g.group_id =:id";
		this.preparedSql(sql);
		this.setPreValue("id", map.get("groupId"));
		return getList(TbQyUserGroupPersonVO.class);

	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.dao.IContactDAO#updateGroupByUserID(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateGroupByUserID(String userId, String type) throws Exception, BaseException {
		String sql = "update tb_qy_user_group set ";
		if ("1".equals(type)) {
			sql += "ext1='1'";
		} else {
			sql += "ext2='1'";
		}
		sql += "where creator='" + userId + "'";
		this.executeSql(sql);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.dao.IContactDAO#getGroupPersonByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPersonVO> getGroupPersonByUserId(String userId, String type) throws Exception, BaseException {
		String sql = "select p.user_id,u.person_name,u.head_pic from tb_qy_user_group g ,tb_qy_user_group_person p,tb_qy_user_info u "
				+ "where g.group_id = p.group_id and p.creator=:userId and u.user_id=p.user_id and u.USER_STATUS <> '-1'";
		if ("1".equals(type)) {
			sql += "and g.ext1='0'";
		} else {
			sql += "and g.ext2='0'";
		}
		preparedSql(sql);
		setPreValue("userId", userId);
		return this.getList(TbQyUserGroupPersonVO.class);
	}


	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.dao.IContactDAO#getUserGroup(java.lang.String)
	 */
	@Override
	public List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception, BaseException {
		String sql = "select g.* from tb_qy_user_group g where g.org_Id=:orgId and status='0' ORDER BY show_num ASC ";
		preparedSql(sql);
		setPreValue("orgId", orgId);
		return this.getList(TbQyUserGroupPO.class);
	}


	@Override
	public Pager getUserGroup(Pager pager, Map<String, Object> map) throws Exception, BaseException {
		String paramSql;
		paramSql = " where creator=:user_id";
		paramSql += " and group_name=:group_name";

		String dataSql = "select * from tb_qy_user_group";

		String countSql = "SELECT count(1) from tb_qy_user_group";

		dataSql += paramSql;

		countSql += paramSql;

		return super.pageSearchByField(TbQyUserGroupPO.class, countSql, dataSql, map, pager);
	}

	@Override
	public Pager getUserGroupPerson(Pager pager, Map<String, Object> map) throws Exception, BaseException {
		String paramSql;
		paramSql = " where g.user_id=u.USER_ID and g.group_id =:groupId and u.user_status <> :aliveStatus";

		String dataSql = "select g.*,u.PERSON_NAME,u.head_pic from tb_qy_user_group_person g,tb_qy_user_info u ";

		String countSql = "SELECT count(1) from tb_qy_user_group_person g,tb_qy_user_info u ";

		dataSql += paramSql;

		countSql += paramSql;

		// 因为user_id不需要使用，所以先从map中去掉
		map.remove("user_id");
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户

		return super.pageSearchByField(TbQyUserGroupPersonVO.class, countSql, dataSql, map, pager);
	}


	private final static String findUserInfoByGroupIds_sql = "select t.id,t.USER_ID,t.PERSON_NAME,t.MOBILE," + " t.wx_user_id,t.head_pic,t.ORG_ID,t.corp_id"
			+ " from tb_qy_user_group_person g,tb_qy_user_info t where 1=1 and g.user_id=t.USER_ID and t.USER_STATUS <> '-1' and g.group_id in ";

	@Override
	public List<TbQyUserInfoVO> findUserInfoByGroupIds(String[] groupIds) throws Exception, BaseException {
		preparedSql(findUserInfoByGroupIds_sql + "('" + StringUtil.uniteArry(groupIds, "','") + "')  GROUP BY t.USER_ID");
		return super.getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.qiweipublicity.experienceapplication.dao.IContactDAO#countUserGroupByUserIdAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer countUserGroupByUserIdAndOrgId(String userId, String orgId) throws Exception, BaseException {
		String countSql = "select count(1) from tb_qy_user_group g,tb_qy_user_group_person gu,tb_qy_user_info u where 1=1 and g.group_id=gu.group_id and gu.user_id=u.USER_ID  and g.org_id = :orgId and gu.user_id = :userId and u.user_status <> '-1'";
		preparedSql(countSql);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValue("userId", userId);// 将参数设置进预置语句
		return this.executeQuery(Integer.class);
	}

	private final static String findUserInfoByGroupId_sql = "select count(1) from tb_qy_user_group_person g,tb_qy_user_info t where 1=1 and g.user_id=t.USER_ID and g.group_id=:groupId ";

	@Override
	public Integer countUserInfoByGroupId(String groupId) throws Exception, BaseException {
		preparedSql(findUserInfoByGroupId_sql);
		setPreValue("groupId", groupId);// 将参数设置进预置语句
		return this.executeCount();
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.group.defatgroup.dao.IDefatgroupDAO#getNotLevelGivenByForeignId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyGivenVO> getNotLevelGivenByForeignId(String foreignId,
			String orgId, String type) throws Exception, BaseException {
		String sql = "select t.person_name,t.rec_id user_id,t.head_pic,t.type from tb_qy_given t,tb_qy_user_info u where 1=1 " +
				"and u.user_id=t.REC_ID and u.USER_STATUS <> '-1' and t.template_id =:foreignId  and t.org_id = :orgId and t.type = :type order by t.sort asc ";
		this.preparedSql(sql);
		this.setPreValue("foreignId", foreignId);
		this.setPreValue("orgId", orgId);
		this.setPreValue("type", type);
		return super.getList(TbQyGivenVO.class); 
	}

	private final static String getUserIdsByGroupId_sql = "select g.user_id from tb_qy_user_group_person g "
			+ "where g.group_id =:id ";
	@Override
	public List<String> getUserIdsByGroupId(String groupId) throws SQLException {
		this.preparedSql(getUserIdsByGroupId_sql);
		this.setPreValue("id", groupId);
		return getList(String.class);
	}
}
