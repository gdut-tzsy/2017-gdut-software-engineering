package cn.com.do1.component.contact.tag.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.contact.tag.dao.ITagDAO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class TagDAOImpl extends BaseDAOImpl implements ITagDAO {

	private static final String delTagByOrgId_sql = "DELETE FROM tb_qy_tag_info WHERE org_id = :orgId ";
	@Override
	public void delTagByOrgId(String orgId) throws BaseException, Exception {
		preparedSql(delTagByOrgId_sql);
		setPreValue("orgId", orgId);
		executeUpdate();
	}

	private static final String delTagMenberByOrgId_sql = "DELETE FROM tb_qy_tag_ref WHERE org_id = :orgId ";
	@Override
	public void delTagMenberByOrgId(String orgId) throws BaseException,
			Exception {
		preparedSql(delTagMenberByOrgId_sql);
		setPreValue("orgId", orgId);
		executeUpdate();
	}
	private static final String getTagMenbersByWxTagId_sql = "SELECT r.* FROM tb_qy_tag_info t LEFT JOIN tb_qy_tag_ref r ON r.tag_id = t.id WHERE t.org_id = :orgId AND t.wx_tag_id = :tagId  ";
	@Override
	public List<QyTagRefVO> getTagMenbersByWxTagId(String orgId, String tagId)
			throws BaseException, Exception {
		preparedSql(getTagMenbersByWxTagId_sql);
		setPreValue("orgId", orgId);
		setPreValue("tagId", tagId);
		return getList(QyTagRefVO.class);
	}
	
	private static final String getTagInfoByWxTagId_sql = "select * FROM tb_qy_tag_info WHERE org_id = :orgId and wx_tag_id=:wxTagId";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.dao.ITagDAO#getTagInfoByWxTagId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyTagInfoPO getTagInfoByWxTagId(String orgId, String wxTagId)
			throws Exception {
		preparedSql(getTagInfoByWxTagId_sql);
		setPreValue("orgId", orgId);
		setPreValue("wxTagId", wxTagId);
		return executeQuery(TbQyTagInfoPO.class);
	}

	private static final String getTagRefList_sql = "SELECT r.* FROM tb_qy_tag_ref r WHERE r.tag_id = :tagId ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.dao.ITagDAO#getTagRefList(java.lang.String)
	 */
	@Override
	public List<TbQyTagRefPO> getTagRefList(String tagId)
			throws Exception {
		preparedSql(getTagRefList_sql);
		setPreValue("tagId", tagId);
		return getList(TbQyTagRefPO.class);
	}
	
	private static final String getTagInfoList_sql = "SELECT t.* FROM tb_qy_tag_info t WHERE t.org_id = :orgId ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.tag.dao.ITagDAO#getTagInfoList(java.lang.String)
	 */
	@Override
	public List<TbQyTagInfoPO> getTagInfoList(String orgId) throws Exception {
		preparedSql(getTagInfoList_sql);
		setPreValue("orgId", orgId);
		return getList(TbQyTagInfoPO.class);
	}

	private static final String getTagRefVOByWxTagIds_sql = "select * FROM tb_qy_tag_info WHERE org_id = :orgId and wx_tag_id in (:wxTagIds)";
	@Override
	public List<QyTagRefVO> getTagRefVOByWxTagIds(String orgId, List<String> wxTagIds) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("wxTagIds", wxTagIds);
		return searchByField(QyTagRefVO.class,getTagRefVOByWxTagIds_sql,map);
	}

	private static final String getTagPageInfoList_sql = "SELECT t.id,t.tag_name,t.show_num,t.status,t.rang,t.user_count,t.dept_count" +
			" FROM tb_qy_tag_info t WHERE t.org_id = :orgId order by t.show_num asc";
	@Override
	public List<QyTagPageInfoVO> getTagPageInfoList(String orgId) throws SQLException {
		preparedSql(getTagPageInfoList_sql);
		setPreValue("orgId", orgId);
		return getList(QyTagPageInfoVO.class);
	}

	private static final String getTagRefPage_sql = "select tr.id,tr.menber_type,tr.menber_id from tb_qy_tag_ref tr where tr.tag_id = :tagId order by tr.menber_type desc, tr.create_time desc, tr.id asc ";
	private static final String getTagRefPage_countSql = "select count(1) from tb_qy_tag_ref tr where tr.tag_id = :tagId ";
	@Override
	public Pager getTagRefPage(Pager pager, Map<String, Object> searchMap) throws SQLException {
		return pageSearchByField(QyTagRefPageVO.class, getTagRefPage_countSql, getTagRefPage_sql, searchMap, pager);
	}

	private static final String getTagListByName_sql = "SELECT t.id,t.tag_name,t.show_num,t.status,t.rang" +
			" FROM tb_qy_tag_info t WHERE t.org_id = :orgId and t.tag_name = :tagName  ";
	@Override
	public List<TbQyTagInfoPO> getTagListByName(String tagName, String orgId) throws SQLException {
		preparedSql(getTagListByName_sql);
		setPreValue("orgId", orgId);
		setPreValue("tagName", tagName);
		return getList(TbQyTagInfoPO.class);
	}
	private static final String getTagRefListByMenberId_sql = "select tr.id,tr.menber_type,tr.menber_id" +
			" FROM tb_qy_tag_ref tr WHERE tr.tag_id = :tagId and tr.menber_type = :menberType and tr.menber_id in (:menberIds) ";
	@Override
	public List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, ArrayList<String> menberIds, int menberType) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(menberIds.size() + 3);
		map.put("tagId", tagId);
		map.put("menberType", menberType);
		map.put("menberIds", menberIds);
		return searchByField(TbQyTagRefPO.class, getTagRefListByMenberId_sql, map);
	}

	private static final String getTagRefListByMenberId1_sql = "select tr.id,tr.menber_type,tr.menber_id" +
			" FROM tb_qy_tag_ref tr WHERE tr.tag_id = :tagId and tr.menber_id in (:menberIds) ";
	@Override
	public List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, String[] menberIds) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(menberIds.length + 3);
		map.put("tagId", tagId);
		map.put("menberIds", menberIds);
		return searchByField(TbQyTagRefPO.class, getTagRefListByMenberId1_sql, map);
	}
	private static final String updateTagUserAndDeptTotal_sql = "UPDATE tb_qy_tag_info SET user_count = user_count + :addUserSize," +
			" dept_count = dept_count + :addDeptSize" +
			" WHERE id = :tagId ";
	@Override
	public void updateTagUserAndDeptTotal(String tagId, int addUserSize, int addDeptSize) throws SQLException {
		preparedSql(updateTagUserAndDeptTotal_sql);
		setPreValue("tagId", tagId);
		setPreValue("addUserSize", addUserSize);
		setPreValue("addDeptSize", addDeptSize);
		executeUpdate();
	}

	private static final String updateTagUserTotal_sql = "UPDATE tb_qy_tag_info SET user_count = user_count + :addSize" +
			" WHERE id in (:tagIds) ";
	@Override
	public void updateTagUserTotal(String[] tagIds, int addSize) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(tagIds.length + 2);
		map.put("tagIds", tagIds);
		map.put("addSize", addSize);
		updateByField(updateTagUserTotal_sql, map, true);
	}

	private static final String updateTagDeptTotal_sql = "UPDATE tb_qy_tag_info SET dept_count = dept_count + :addSize" +
			" WHERE id in (:tagIds) ";
	@Override
	public void updateTagDeptTotal(String[] tagIds, int addSize) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(tagIds.length + 2);
		map.put("tagIds", tagIds);
		map.put("addSize", addSize);
		updateByField(updateTagDeptTotal_sql, map, true);
	}

	private static final String getTagRefListByIds_sql = "select tr.id,tr.menber_type,tr.menber_id" +
			" FROM tb_qy_tag_ref tr WHERE tr.tag_id = :tagId and tr.id in (:ids) ";
	@Override
	public List<TbQyTagRefPO> getTagRefListByIds(String tagId, String[] ids) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(ids.length + 3);
		map.put("tagId", tagId);
		map.put("ids", ids);
		return searchByField(TbQyTagRefPO.class, getTagRefListByIds_sql, map);
	}

	private static final String getTagRefMenberIdListByMenberIdList_sql = "select tr.menber_id" +
			" FROM tb_qy_tag_ref tr WHERE tr.tag_id = :tagId and tr.menber_id in (:ids) ";
	@Override
	public List<String> getTagRefMenberIdListByMenberIdList(String tagId, List<String> menberIds) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(menberIds.size() + 3);
		map.put("tagId", tagId);
		map.put("ids", menberIds);
		return searchByField(String.class, getTagRefMenberIdListByMenberIdList_sql, map);
	}

	private static final String getHasTagRef_sql = "select count(1) FROM tb_qy_tag_ref tr WHERE tr.tag_id = :tagId ";
	@Override
	public int getHasTagRef(String tagId) throws SQLException {
		preparedSql(getHasTagRef_sql);
		setPreValue("tagId", tagId);
		return executeCount();
	}

	private static final String getTagrefIdByMenberIds_sql = "select tr.id" +
			" FROM tb_qy_tag_ref tr WHERE tr.menber_type = :menberType and tr.menber_id in (:menberIds) ";
	@Override
	public List<String> getTagrefIdByMenberIds(String[] menberIds, int menberType) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(menberIds.length + 2);
		map.put("menberType", menberType);
		map.put("menberIds", menberIds);
		return searchByField(String.class, getTagrefIdByMenberIds_sql, map);
	}

	private static final String getTagRefPOByMenberIds_sql = "select tr.*" +
			" FROM tb_qy_tag_ref tr WHERE tr.menber_type = :menberType and tr.menber_id in (:menberIds) ";
	@Override
	public List<TbQyTagRefPO> getTagRefPOByMenberIds(String[] menberIds, int menberType) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(menberIds.length + 2);
		map.put("menberType", menberType);
		map.put("menberIds", menberIds);
		return searchByField(TbQyTagRefPO.class, getTagRefPOByMenberIds_sql, map);
	}

	private static final String getTagRefListByTagIds_sql = "select tr.* FROM tb_qy_tag_ref tr WHERE tr.tag_id in (:tagIdList) ";
	@Override
	public List<TbQyTagRefPO> getTagRefListByTagIds(List<String> tagIdList) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>(tagIdList.size() + 1);
		map.put("tagIdList", tagIdList);
		return searchByField(TbQyTagRefPO.class, getTagRefListByTagIds_sql, map);
	}

	@Override
	public void addSyncTime(String orgId) throws SQLException {
		String sql = "UPDATE tb_qy_tag_sync_time set sync_time = sync_time + 1 where id = :id";
		this.preparedSql(sql);
		this.setPreValue("id", orgId);
		this.executeUpdate();
	}

	@Override
	public List<String> getTagRefMenberIdListByTagId(String tagId, int menberType) throws SQLException {
		String sql = "select tr.menber_id FROM tb_qy_tag_ref tr WHERE tr.menber_type = :menberType and tr.tag_id = :tagId ";
		this.preparedSql(sql);
		this.setPreValue("tagId", tagId);
		this.setPreValue("menberType", menberType);
		return getList(String.class);
	}
}
