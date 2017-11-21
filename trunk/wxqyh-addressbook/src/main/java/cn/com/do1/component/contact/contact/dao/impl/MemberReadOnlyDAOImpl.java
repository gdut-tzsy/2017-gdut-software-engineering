package cn.com.do1.component.contact.contact.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.component.addressbook.contact.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.contact.contact.dao.IMemberReadOnlyDAO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class MemberReadOnlyDAOImpl extends BaseDAOImpl implements IMemberReadOnlyDAO {
	private final static transient Logger logger = LoggerFactory.getLogger(MemberReadOnlyDAOImpl.class);

	@Override
	public List<TbQyMemberConfigVO> getConfigList(String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql=" SELECT * FROM tb_qy_member_config where ORG_ID =:orgId order by sort asc";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return getList(TbQyMemberConfigVO.class);
	}

	@Override
	public List<TbQyMemberConfigResPO> getDeptList(String orgId,String id)
			throws Exception, BaseException {
		String sql=" SELECT * FROM tb_qy_member_config_res where ORG_ID =:orgId and con_id =:id order by sort";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("id", id);
		return getList(TbQyMemberConfigResPO.class);
	}

	@Override
	public TbQyMemberConfigVO getHistotryData(String id,String orgId) throws Exception,
			BaseException {
		String sql=" SELECT c.*,COUNT(t.id) as all_person_num FROM tb_qy_member_config c,tb_qy_member_info t where c.id =:id and t.ref_id=:id and c.org_id=:orgId and t.parent_id IS NULL ";
		this.preparedSql(sql);
		this.setPreValue("id", id);
		this.setPreValue("orgId", orgId);
		return executeQuery(TbQyMemberConfigVO.class);
	}

	@Override
	public List<TbQyMemberConfigPO> getAllConfig() throws Exception,
			BaseException {
		String sql=" SELECT * FROM tb_qy_member_config ";
		this.preparedSql(sql);
		return getList(TbQyMemberConfigPO.class);
	}

	private static String get_sql=" select * from tb_qy_member_target_person where foreign_id =:id and type =:type ";
	@Override
	public List<TbQyMemberTargetPersonPO> getRecipient(String id, String type)
			throws Exception, BaseException {
		this.preparedSql(get_sql);
		this.setPreValue("id",id);
		this.setPreValue("type",type);
		return getList(TbQyMemberTargetPersonPO.class);
	}
	
	@Override
	public Pager seachMemberList(Map map, Pager pager) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		String searchSQL1 = "select t.* from tb_qy_member_info t,tb_qy_member_target_person p where t.ref_id=p.foreign_id and p.user_id =:userId and t.org_id= :orgId and t.person_name like :keyWord and t.parent_id IS NULL ";
		if(!AssertUtil.isEmpty(map.get("status"))){//搜索所有
			if(!"3".equals(map.get("status"))){
				searchSQL1+=" and t.status= :status ";
			}else{
				map.remove("status");
			}
		}
		searchSQL1+="order by t.create_time desc";
		String countSQL1 = "select count(1) from (" + searchSQL1.replaceAll("(?i)\\basc\\b|\\bdesc\\b", "").replaceAll("(?i)order\\s+by\\s+\\S+(\\s*[,\\s*\\S+])*", "")+"  ) a ";
		return pageSearchByField(TbQyMemberInfoVO.class, countSQL1, searchSQL1, map, pager);
	}

	private static String cont_num=" SELECT COUNT(1) FROM tb_qy_member_info t where t.ref_id=:id and t.org_id=:orgId and t.status=:type and t.parent_id IS NULL ";
	@Override
	public int countPersonNum(String id, String orgId, String type)
			throws Exception, BaseException {
		this.preparedSql(cont_num);
		this.setPreValue("id",id);
		this.setPreValue("orgId",orgId);
		this.setPreValue("type",type);
		return executeCount();
	}
	
	@Override
	public Pager searchMemberInfo(Map searchMap, Pager pager) throws Exception,
			BaseException {

		String searchSQL1 = "select * from tb_qy_member_info where org_id= :orgId and person_name like :title and person_name like :personName and wx_user_id =:wxUserId and mobile =:mobile and email like :email " +
				" and CREATE_TIME >=:startTimes and CREATE_TIME<=:endTime and status =:status and APPROVE_TIME >=:startApproveTime and APPROVE_TIME<=:endApproveTime and approve_user_id=:userId  and parent_id IS NULL order by create_time desc ";
	    String countSQL1 = "select count(1) from (" + searchSQL1.replaceAll("(?i)\\basc\\b|\\bdesc\\b", "").replaceAll("(?i)order\\s+by\\s+\\S+(\\s*[,\\s*\\S+])*", "")+"  ) a ";
	    
        return super.pageSearchByField(TbQyMemberInfoVO.class, countSQL1, searchSQL1, searchMap, pager);
	}

	private static final String searchByParentId_sql = "select t.* from tb_qy_member_info t where  t.id = :parentId ";
	@Override
	public TbQyMemberInfoPO searchByParentId(String parentId) throws Exception, BaseException{
		this.preparedSql(searchByParentId_sql);
		this.setPreValue("parentId",parentId);
		return executeQuery(TbQyMemberInfoPO.class);
	}

	private static final String searchChildren_sql = "select t.* from tb_qy_member_info t where  t.parent_id = :parentId";
	@Override
	public List<TbQyMemberInfoPO> searchChildren(String parentId) throws Exception, BaseException{
		this.preparedSql(searchChildren_sql);
		this.setPreValue("parentId", parentId);
		return getList(TbQyMemberInfoPO.class);
	}

	private static final String searchChildrenToVO_sql = "select t.* from tb_qy_member_info t where  t.parent_id = :parentId";
	@Override
	public List<TbQyMemberInfoVO> searchChildrenToVO(String parentId) throws Exception, BaseException{
		this.preparedSql(searchChildrenToVO_sql);
		this.setPreValue("parentId", parentId);
		return getList(TbQyMemberInfoVO.class);
	}

	private static final String batchPObyId_sql = "select t.* from tb_qy_member_info t where t.id in (:ids)";
	@Override
	public	List<TbQyMemberInfoPO> batchPObyId(String ids[]) throws Exception, BaseException{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("ids", ids);
		return this.searchByField(TbQyMemberInfoPO.class, batchPObyId_sql, map);
	}

	private static final String showIndex_sql = "select g.* from tb_qy_member_config g where g.org_id = :orgId and g.status = 1 and g.show_index = 1 order by g.sort ";
	@Override
	public List<TbQyMemberConfigVO> showIndex(String orgId) throws Exception, BaseException{
		this.preparedSql(showIndex_sql);
		this.setPreValue("orgId", orgId);
		return this.getList(TbQyMemberConfigVO.class);
	}

	private static final String selectAllIndex_sql = "select g.* from tb_qy_member_config g where g.org_id = :orgId and g.show_index = 1 order by g.sort ";
	@Override
	public List<TbQyMemberConfigPO> selectAllIndex(String orgId) throws Exception, BaseException{
		this.preparedSql(selectAllIndex_sql);
		this.setPreValue("orgId", orgId);
		return this.getList(TbQyMemberConfigPO.class);
	}

	@Override
	public int countUsersByMobile(String orgId, String mobile) throws Exception, BaseException {
		String sql=" SELECT count(1) FROM tb_qy_member_info where ORG_ID =:orgId and status='0' and mobile =:mobile";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("mobile", mobile);
		return countByField(sql, map);
	}

	@Override
	public int countUsersByWeixinNum(String orgId, String weixinNum) throws Exception, BaseException {
		String sql=" SELECT count(1) FROM tb_qy_member_info where ORG_ID =:orgId and status='0' and weixin_num =:weixinNum";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("weixinNum", weixinNum);
		return countByField(sql, map);
	}

	@Override
	public int countUsersByEmail(String orgId, String email) throws Exception, BaseException {
		String sql=" SELECT count(1) FROM tb_qy_member_info where ORG_ID =:orgId and status='0' and email =:email";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("email", email);
		return countByField(sql, map);
	}

	@Override
	public int countUsersByWxUserId(String orgId, String wxUserId) throws Exception, BaseException {
		String sql=" SELECT count(1) FROM tb_qy_member_info where ORG_ID =:orgId and status='0' and wx_user_id =:wxUserId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("wxUserId", wxUserId);
		return countByField(sql, map);
	}

	private static final String getMemberCustomConfigByMeberId_sql = "select * from tb_qy_member_custom_config c where c.member_id = :memberId";
	@Override
	public List<TbQyMemberCustomConfigPO> getMemberCustomConfigByMeberId(String memberId) throws BaseException, Exception{
		this.preparedSql(getMemberCustomConfigByMeberId_sql);
		this.setPreValue("memberId", memberId);
		return this.getList(TbQyMemberCustomConfigPO.class);
	}

	private static final String getMemberBaseConfigByMeberId_sql = "select * from tb_qy_member_base_config c where c.member_id = :memberId";
	@Override
	public List<TbQyMemberBaseConfigPO> getMemberBaseConfigByMeberId(String memberId) throws BaseException, Exception{
		this.preparedSql(getMemberBaseConfigByMeberId_sql);
		this.setPreValue("memberId", memberId);
		return this.getList(TbQyMemberBaseConfigPO.class);
	}

	private static final String getMemberUserCustom_sql = "select c.* from tb_qy_member_user_custom c where c.member_info_id = :memberInfoId ";
	@Override
	public List<TbQyMemberUserCustomPO> getMemberUserCustom(String memberInfoId) throws BaseException, Exception{
		this.preparedSql(getMemberUserCustom_sql);
		this.setPreValue("memberInfoId", memberInfoId);
		return this.getList(TbQyMemberUserCustomPO.class);
	}

	private static final String batchMemberUserCustom_sql = "select c.* from tb_qy_member_user_custom c where c.member_info_id in(:memberInfoIds) ";
	@Override
	public List<TbQyMemberUserCustomPO> batchMemberUserCustom(List<String> memberInfoIds) throws BaseException, Exception{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("memberInfoIds", memberInfoIds);
		return searchByField(TbQyMemberUserCustomPO.class, batchMemberUserCustom_sql, map);
	}
}
