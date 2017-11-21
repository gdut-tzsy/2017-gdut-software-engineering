package cn.com.do1.component.contact.post.dao.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.contact.post.dao.IPostReadonlyDAO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogRefPO;
import cn.com.do1.component.contact.post.vo.ExportPersonVO;
import cn.com.do1.component.contact.post.vo.TbQyHrPostCatalogVO;
import cn.com.do1.component.contact.post.vo.TbQyHrPostVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhibin on 2017/5/22.
 */
public class PostReadonlyDAOImpl extends BaseDAOImpl implements IPostReadonlyDAO {

	final private static String queryPosition = "SELECT p.*,c.`name` as catalog_name from tb_qy_hr_post p "
			+ "LEFT JOIN tb_qy_hr_post_catalog_ref r ON p.id=r.post_id "
			+ "LEFT JOIN tb_qy_hr_post_catalog c ON  r.catalog_id=c.id "
			+ "where p.org_id= :orgId AND p.name like :name AND c.id like :catalog ";
	final private static String countPosition = "SELECT count(1) from tb_qy_hr_post p "
			+ "LEFT JOIN tb_qy_hr_post_catalog_ref r ON p.id=r.post_id "
			+ "LEFT JOIN tb_qy_hr_post_catalog c ON  r.catalog_id=c.id "
			+ "where p.org_id= :orgId AND p.name like :name AND c.id like :catalog";

	@Override
	public Pager queryPositionList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		return pageSearchByField(TbQyHrPostVO.class, countPosition, queryPosition, searchMap, pager);
	}

	final private static String getCatalog = "SELECT c.id,c.name from tb_qy_hr_post_catalog c where c.org_id= :orgId AND c.is_use=:isUse order by c.sort asc";

	@Override
	public List<TbQyHrPostCatalogPO> getAllPositionType(String orgId, String isUse) throws BaseException, Exception {
		this.preparedSql(getCatalog);
		this.setPreValue("orgId", orgId);
		this.setPreValue("isUse", isUse);
		return getList(TbQyHrPostCatalogPO.class);
	}

	final private static String getPosition = "SELECT count(1) from tb_qy_hr_post p where p.name=:postName AND p.org_id=:orgId ";

	@Override
	public boolean isExistPosition(String postName, String orgId) throws BaseException, Exception {
		super.preparedSql(getPosition);
		super.setPreValue("postName", postName);
		super.setPreValue("orgId", orgId);
		return super.executeCount() > 0;
	}

	final private static String queryPositionInfo = "SELECT p.*,c.`name` as catalog_name,c.`id` as catalog_id from tb_qy_hr_post p "
			+ "LEFT JOIN tb_qy_hr_post_catalog_ref r ON p.id=r.post_id "
			+ "LEFT JOIN tb_qy_hr_post_catalog c ON  r.catalog_id=c.id " + "where p.id= :id AND p.org_id= :orgId ";

	@Override
	public TbQyHrPostVO queryPositionDetail(String id, String orgId) throws BaseException, Exception {
		this.preparedSql(queryPositionInfo);
		this.setPreValue("id", id);
		this.setPreValue("orgId", orgId);
		return executeQuery(TbQyHrPostVO.class);
	}

	final private static String getPositionRef = "SELECT * from tb_qy_hr_post_catalog_ref where post_id= :id ";

	@Override
	public TbQyHrPostCatalogRefPO updatePositionByPostId(String id) throws BaseException, Exception {
		this.preparedSql(getPositionRef);
		setPreValue("id", id);
		return executeQuery(TbQyHrPostCatalogRefPO.class);
	}

	final private static String queryPositionPerson = "SELECT u.`person_name`,u.`PINYIN`,u.`MOBILE`,u.`POSITION`,u.wx_user_id FROM tb_qy_user_info u "
			+ "WHERE u.position= :postionName AND u.org_id= :orgId AND u.person_name like :name and u.USER_STATUS <> :userStatus ";
	final private static String countPositionPerson = "SELECT count(1) FROM tb_qy_user_info u "
			+ "WHERE u.position= :postionName AND u.org_id= :orgId AND u.person_name like :name and u.USER_STATUS <> :userStatus ";

	@Override
	public Pager queryPositionPersonList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		searchMap.put("userStatus", -1);
		return pageSearchByField(UserInfoVO.class, countPositionPerson, queryPositionPerson, searchMap, pager);
	}

	final private static String queryCataLog = "SELECT c.`id`,c.`name`,c.`create_time`,c.`sort`,c.`is_use` FROM tb_qy_hr_post_catalog c "
			+ "WHERE c.org_id= :orgId AND c.name like :name order by c.sort asc";
	final private static String countCataLog = "SELECT count(1) FROM tb_qy_hr_post_catalog c "
			+ "WHERE c.org_id= :orgId AND c.name like :name";

	@Override
	public Pager queryPositionTypeList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		return pageSearchByField(TbQyHrPostCatalogVO.class, countCataLog, queryCataLog, searchMap, pager);
	}

	final private static String getType="SELECT count(1) from tb_qy_hr_post_catalog c where c.name=:name AND c.org_id=:orgId";
	@Override
	public boolean isExistPositionType(String name,String orgId) throws BaseException, Exception {
		super.preparedSql(getType);
		super.setPreValue("name", name);
		super.setPreValue("orgId", orgId);
		return super.executeCount() > 0;
	}
	final private static String getCataPO="SELECT c.`id`,c.`name` from tb_qy_hr_post_catalog c where c.name=:name";
	@Override
	public TbQyHrPostCatalogPO getCatalogPOByName(String catalog) throws BaseException, Exception {
		this.preparedSql(getCataPO);
        setPreValue("name", catalog);
		return executeQuery(TbQyHrPostCatalogPO.class);
	}
	
	final private static String queryPerson="SELECT u.`person_name`,u.`PINYIN`,u.`MOBILE`,u.`POSITION` FROM tb_qy_user_info u "
			+ "WHERE u.position= :position AND u.org_id= :orgId  and u.USER_STATUS <> :userStatus AND u.person_name like :name ";
	@Override
	public List<ExportPersonVO> searchPositionPersonList(Map<String, Object> params) throws BaseException, Exception {
		params.put("userStatus", -1);
		return searchByField(ExportPersonVO.class, queryPerson, params);
	}
	
	final private static String getRefPO="SELECT r.* from tb_qy_hr_post_catalog_ref r where r.post_id=:id ";
	@Override
	public TbQyHrPostCatalogRefPO getCatalogRefPOByPostId(String id) throws BaseException, Exception {
		this.preparedSql(getRefPO);
        setPreValue("id", id);
		return executeQuery(TbQyHrPostCatalogRefPO.class);
	}
	
	final private static String getRefPOS="SELECT r.* from tb_qy_hr_post_catalog_ref r where r.catalog_id in (:ids) ";
	@Override
	public List<TbQyHrPostCatalogRefPO> getRefPOByCatalogIds(String[] ids) throws BaseException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		return searchByField(TbQyHrPostCatalogRefPO.class, getRefPOS, map);
	}
	
	final private static String getPerson="SELECT count(1) from tb_qy_user_info u where u.position=:name AND u.org_id=:orgId and u.USER_STATUS <> :userStatus ";
	@Override
	public boolean isExistPerson(String name, String orgId) throws BaseException, Exception {
		super.preparedSql(getPerson);
		super.setPreValue("name", name);
		super.setPreValue("orgId", orgId);
		super.setPreValue("userStatus", -1);
		return super.executeCount() > 0;
	}
	
	private final static String getPositionByOrgId_sql = "SELECT DISTINCT u.`POSITION` FROM tb_qy_user_info u "
			+ "WHERE u.org_id= :orgId AND u.POSITION is NOT NULL AND u.USER_STATUS <> :userStatus ";
	private final static String getPositionByOrgId_count = "SELECT count(DISTINCT u.`POSITION`) FROM tb_qy_user_info u "
			+ "WHERE u.org_id= :orgId and u.USER_STATUS <> :userStatus ";
	@Override
	public Pager getPositionByOrgId(Pager pager,String orgId) throws BaseException, Exception {
		Map<String, Object> searchMap = new HashMap<String, Object>(1);
		searchMap.put("orgId", orgId);
		searchMap.put("userStatus", -1);
		return pageSearchByField(String.class, getPositionByOrgId_count, getPositionByOrgId_sql, searchMap, pager);
	}
	final private static String getExsitPositionByOrgId="SELECT p.name FROM tb_qy_hr_post p "
			+ "WHERE p.org_id= :orgId";
	@Override
	public List<String> getExistPositionByOrgId(String orgId) throws BaseException, Exception {
		this.preparedSql(getExsitPositionByOrgId);
		this.setPreValue("orgId",orgId);
		return getList(String.class);
	}

	private static final String getPositionByVagueSearch_sql = "select p.name from tb_qy_hr_post p where p.org_id = :orgId and p.name like :name and p.is_use = :isUse limit 0,15 ";
	@Override
	public List<TbQyHrPostVO> getPositionByVagueSearch(Map searchMap, String orgId) throws BaseException, Exception{
		searchMap.put("orgId", orgId);
		searchMap.put("isUse", 1);
		return searchByField(TbQyHrPostVO.class, getPositionByVagueSearch_sql, searchMap);
	}

	private static final String getPositionListByOrgId_sql = "select p.name from tb_qy_hr_post p where p.org_id = :orgId and p.is_use = :isUse  ";
	@Override
	public List<String> getPositionListByOrgId(String orgId) throws BaseException, Exception{
		this.preparedSql(getPositionListByOrgId_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("isUse", 1);
		return getList(String.class);
	}
}
