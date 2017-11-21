package cn.com.do1.component.contact.contact.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.do1.common.dac.SQLBuilder;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.dao.IContactDAO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbConfigOrgAesPO;

import cn.com.do1.component.systemmgr.user.model.TbDqdpUserPO;
import cn.com.do1.component.wxcgiutil.token.AccessToken;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class ContactDAOImpl extends BaseDAOImpl implements IContactDAO {
	private final static transient Logger logger = LoggerFactory.getLogger(ContactDAOImpl.class);
	private final static String getOrgByUserId_sql = "select u.user_id, u.user_name, u.status," + " p.person_id, p.person_name, p.age,"
			+ " por.org_id as org_id, o.ORGANIZATION_DESCRIPTION as org_name ,o.corp_id,o.wx_id,o.wx_parentid" + " from tb_dqdp_user u, tb_person_user_ref pur, tb_person_organization_ref por, "
			+ " tb_dqdp_person p,tb_dqdp_organization o where u.user_id = pur.user_id " + " and p.person_id = pur.person_id and p.person_id = por.person_id "
			+ " and o.organization_id = por.org_id and u.USER_NAME =:userName";
	private final static String getUserByUserId_sql = "select u.user_id, u.user_name, u.status, p.person_id, p.person_name, p.age,"
			+ " por.org_id as org_id from tb_dqdp_user u, tb_person_user_ref pur, tb_person_organization_ref por, "
			+ " tb_dqdp_person p where u.user_id = pur.user_id and p.person_id = pur.person_id and p.person_id = por.person_id "
			+ " and u.USER_NAME =:userName";
	private final static String getOrgById_sql = "select o.organization_id as org_id, o.ORGANIZATION_DESCRIPTION as org_name ,o.corp_id,o.wx_id,o.wx_parentid,o.type as org_type,o.total_member,o.total_leave,o.create_time"
			+ " from tb_dqdp_organization o where o.organization_id = :id ";
	private static final String findUserInfoByWxUserId_sql = "select t.*  from TB_QY_USER_INFO t  where t.wx_user_id=:wxUserId and t.corp_id=:corpId";
	private static  final String hasUsersByDepartIdAndFullName_sql = "select count(ud.user_id) from tb_department_info d,tb_qy_user_department_ref ud"
			+ " where ud.department_id=d.id and d.org_id=:orgId and (d.id=:departId or d.dept_full_name like :deptFullName)";
	private static  final String hasUsersByDepartId_sql = "select count(ud.user_id) from tb_qy_user_department_ref ud"
			+ " where ud.department_id=:departId";
	private static  final String hasUsersByDepartIds_sql = "select count(ud.user_id) from tb_qy_user_department_ref ud" +
			" where ud.department_id in(:departId)";
	private final static String deleteUserDeptRefByUserIdDeptId_sql = "delete from TB_QY_USER_DEPARTMENT_REF where user_id=:userId  and department_id :deptId";
	private final static String getDQDPUserByCorpId_sql = "select o.ORGANIZATION_ID as org_id,o.ORGANIZATION_DESCRIPTION as org_name,u.USER_ID,u.USER_NAME,p.PERSON_NAME,p.PERSON_ID,o.corp_id,o.wx_id,o.wx_parentid,o.type as org_type from tb_dqdp_organization o, "
			+ "tb_person_organization_ref pre,tb_dqdp_person p,tb_person_user_ref ure,tb_dqdp_user u  "
			+ "where pre.ORG_ID=o.ORGANIZATION_ID and pre.PERSON_ID=p.PERSON_ID and ure.PERSON_ID=p.PERSON_ID and u.USER_ID=ure.USER_ID " + "and p.AGE='0' and o.corp_id=:corpId ";
	private final static String getUserRedundancyInfoByUserId_sql = "select t.user_id,t.PERSON_NAME,t.head_pic,t.wx_user_id,t.org_id"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
			+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id=:userId and ud.user_id=:userId and t.USER_STATUS <> '-1'" + " GROUP BY t.user_id";
	private final static String isExitsOrgName_sql = "select count(1) from tb_dqdp_organization t" + " where t.ORGANIZATION_DESCRIPTION = :orgName and t.PARENT_ID = :orgPid";
	private static final String searchDqdpUserPersonInfoByUserName_sql = "select u.USER_ID,u.USER_NAME,po.ORG_ID,p.PERSON_NAME,p.PERSON_ID"
			+ " from tb_dqdp_user u,tb_person_user_ref pu,tb_person_organization_ref po,tb_dqdp_person p"
			+ " where u.USER_ID=pu.USER_ID and pu.PERSON_ID = po.PERSON_ID and pu.PERSON_ID = p.PERSON_ID and u.USER_NAME=:userName";
	private static final String getUserInfoCertificateType_sql="SELECT * FROM tb_qy_user_info_certificate_type a WHERE a.ORG_ID=:orgId AND a.STATUS=:status AND a.TITLE LIKE :title ORDER BY SORT_NUM ASC,CREATE_TIME DESC";
	private static final String getUserInfoCertificateTypePager_countSql="SELECT count(1) FROM tb_qy_user_info_certificate_type a WHERE a.ORG_ID=:orgId AND a.STATUS=:status AND a.TITLE LIKE :title";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#countAllPerson()
	 */
	private final static String countAllUser="select count(id) as all_user from tb_qy_user_info where USER_STATUS <> '-1'";
	private final static String getCertificateTypeCount_sql="SELECT COUNT(1) FROM tb_qy_user_info WHERE CERTIFICATE_TYPE=:id AND org_id=:orgId";
	private final static String getCertificateTypePOByTitle_sql="SELECT * FROM tb_qy_user_info_certificate_type WHERE ORG_ID=:orgId AND TITLE=:title";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#searchUserByPersonName(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	private final static String USERBYPERSONNAME_SQL="select t.user_id,t.PERSON_NAME,t.head_pic from TB_QY_USER_INFO t where 1=1 " +
			" and t.pinyin like :pinyin and t.org_id =:orgId and t.person_name like :personName order by t.IS_TOP ASC,t.pinyin ";
	private final static String USERBYPERSONNAME_COUNTSQL="select count(1) from TB_QY_USER_INFO t where 1=1 " +
			" and t.pinyin like :pinyin and t.org_id =:orgId and t.person_name like :personName ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#searchDeptByDeptName(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	private final static String DEPTBYDEPT_SQL="select t.department_name,t.id ,t.parent_depart,t.dept_full_name from TB_DEPARTMENT_INFO t where 1=1 " +
			" and t.org_id =:orgId and t.department_name like :deptName order by t.show_order ";
	private final static String DEPTBYDEPTNAME_SQL="select t.department_name,t.id ,t.parent_depart,t.dept_full_name " +
			" from TB_DEPARTMENT_INFO t where 1=1 and t.org_id =:orgId and t.dept_full_name like :deptName and t.id not in(:ids) order by t.show_order ";
	private final static String getUserInfoExtPOByUserId_sql="SELECT * FROM tb_qy_user_info_ext WHERE USER_ID=:userId";
	private final static String getSuperManagerByOrgId_sql="select u.USER_ID,u.USER_NAME"+
						" from tb_person_organization_ref po,tb_dqdp_person p ,tb_person_user_ref pu,tb_dqdp_user u"+
						" where  po.PERSON_ID=p.PERSON_ID and p.PERSON_ID=pu.PERSON_ID and pu.USER_ID = u.USER_ID "+
						" and po.ORG_ID =:orgId and p.AGE=0";
	private final static String getOrgByPerson_sql = "select u.user_id, u.user_name, u.status," + " p.person_id, p.person_name, p.age,"
			+ " por.org_id as org_id, o.ORGANIZATION_DESCRIPTION as org_name ,o.corp_id,o.wx_id,o.wx_parentid" + " from tb_dqdp_user u, tb_person_user_ref pur, tb_person_organization_ref por, "
			+ " tb_dqdp_person p,tb_dqdp_organization o where u.user_id = pur.user_id " + " and p.person_id = pur.person_id and p.person_id = por.person_id "
			+ " and o.organization_id = por.org_id and p.person_name like :personName and por.ORG_ID =:orgId";
	private final static String getUserIdByWxUserId_sql = " select user_id from TB_QY_USER_INFO where wx_user_id= :wxUserId and org_id= :orgId and user_status<>'-1' ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getToUserByDepatIds(java.lang.String[], java.lang.String)
	 */
	private final static String getToUserByDepatIds="SELECT b.* FROM tb_qy_user_receive a LEFT JOIN tb_qy_user_info b on a.user_id=b.USER_ID " +
			" WHERE a.department_id in(:depatIds) and b.user_status <> :aliveStatus and b.org_id=:orgId ";
    private  final static String updateQyUserReceiveByUserId_sql="update tb_qy_user_receive set user_id=:userId,person_name=:personName,head_pic=:headPic,wx_user_id=:wxUserId where user_id=:oldUserId";
	private static  final String getUserInfoByIds_sql = "select t.* from tb_qy_user_info t" +
			" where t.id in(:ids)";
	private static final String deleteUserLeaveDeptRef_sql = "delete from tb_qy_user_leave_department_ref where user_id in (:userIds)";
	private static final String getDeptInfoByLeaveUserId_sql = "select d.id,d.department_name,d.parent_depart,d.org_id,d.dept_full_name,d.wx_id,d.wx_parentid,d.permission"
			+ " from tb_department_info d,tb_qy_user_leave_department_ref ud"
			+ " where d.id=ud.department_id and ud.user_id =:userId"
			+ " order by ud.sort asc";
	/**
	 * 通过orgid查询公司保密人员
	 * @return
	 * @author LiYiXin
	 * 2016-8-18
	 */
	private final static String getSecrecyByOrgId_sql = "select s.* from tb_qy_user_secrecy s where s.org_id = :orgId";
	private static final String getContactSyncList_sql = "select * from tb_qy_contact_sync t"
			+ " where t.corp_id = :corpId and t.status <= :status";
	private static final String getContactSyncCorpIdList_sql = "SELECT DISTINCT(t.corp_id) from tb_qy_contact_sync t"
			+ " where t.status <= :status ";
	/**
	 * sql
	 */
	private final static String GETVERSIONID_SQL = "select v.id from tb_qy_org_version v where v.org_id = :orgId limit :start,:end" ;
	/**
	 *
	 */
	private final static String GET_VERSION_ORG_COUNT = "select count(v.id) from tb_qy_org_version v where v.org_id = :orgId " ;

	private final static String getSeniorByOrgId_sql = "select r.* from tb_qy_user_search_senior r where r.org_id = :orgId and r.user_id = :userId";

	private final static String getSeniorPOByOrgId_sql = "select r.* from tb_qy_user_search_senior r where r.org_id = :orgId and r.user_id = :userId";

	@Override
	public Pager searchContact(Map searchMap, Pager pager) throws Exception, BaseException {
		//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
		String sortType = (String) searchMap.get("sortType");
		String sortSql= "t.pinyin";
		if(!AssertUtil.isEmpty(sortType)){
			if("1".equals(sortType)){//离职人员按离职时间降序查询
				sortSql= "t.leave_time DESC";
			}
		}
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && !AssertUtil.isEmpty(searchMap.get("endBirthday"))){
			hasBirthday = true;
		}
		searchMap.remove("sortType");
		StringBuilder searchSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP,t.leave_time "
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d");
		StringBuilder countSql = new StringBuilder("select count(1) from (select t.user_id from TB_QY_USER_INFO t, tb_qy_user_department_ref ud ,TB_DEPARTMENT_INFO d ");
		//生日关联
		if(hasBirthday){
			searchSql.append(",tb_qy_user_info_ext e ");
			countSql.append(",tb_qy_user_info_ext e ");
		}
		String conditions = " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus and t.sex = :sex "
				+ " and (d.dept_full_name like :department or d.id=:deptId) and t.position like :position and ( t.person_name like :personName or t.pinyin like :pinyinInfo or t.MOBILE like :mobileInfo )"
				+ " and t.pinyin like :pinyin "
				+ " and t.MOBILE like :mobile "
				+ " and  t.PERSON_NAME like :exactName "
				// " and (t.MOBILE like :mobile)" +
				//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
				+ " and t.wx_user_id LIKE :wxUserId and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+"and ( t.cancel_time >=:reStartFollowTimes and t.cancel_time <=:reEndFollowTimes )"
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime ";	//入职时间
		searchSql.append(conditions);
		countSql.append(conditions);
		if(hasBirthday){
			searchSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			countSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
		}
		searchSql.append(" GROUP BY t.user_id ");
		countSql.append(" GROUP BY t.user_id ");
		searchSql.append(" order by t.IS_TOP ASC,"+sortSql);
		countSql.append(") t ");
		return pageSearchByField(TbQyUserInfoForList.class, countSql.toString(), searchSql.toString(), searchMap, pager);
	}

	@Override
	public Pager searchContactByOrgId(Map searchMap, Pager pager) throws Exception, BaseException {
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && !AssertUtil.isEmpty(searchMap.get("endBirthday"))){
			hasBirthday = true;
		}
		//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
		String sortType = (String) searchMap.get("sortType");
		String sortSql= "t.pinyin";
		if(!AssertUtil.isEmpty(sortType)){
			if("1".equals(sortType)){//离职人员按离职时间降序查询
				sortSql= "t.leave_time DESC";
			}
		}
		searchMap.remove("sortType");
		StringBuilder searchSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP, t.leave_time " + " from TB_QY_USER_INFO t");
		StringBuilder countSql =  new StringBuilder("select count(1) from TB_QY_USER_INFO t");
		//生日关联
		if(hasBirthday){
			searchSql.append(",tb_qy_user_info_ext e ");
			countSql.append(",tb_qy_user_info_ext e ");
		}
		String conditions = " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus "
				+ " and t.sex = :sex and t.position like :position and ( t.person_name like :personName or t.pinyin like :pinyinInfo or t.MOBILE like :mobileInfo )"
				+ " and t.pinyin like :pinyin "
				+ " and t.MOBILE like :mobile "
				+ " and  t.PERSON_NAME like :exactName "
				// " and (t.MOBILE like :mobile)" +
				//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
				+" and t.wx_user_id LIKE :wxUserId and (t.create_time>=:startTimes and t.create_time<=:endTime) "
				+"and ( t.cancel_time >=:reStartFollowTimes and t.cancel_time <=:reEndFollowTimes )"
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				+ " and t.leave_time >= :startLeaveTime and t.leave_time <= :endLeaveTime ";//离职时间
		searchSql.append(conditions);
		countSql.append(conditions);
		if(hasBirthday){
			searchSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			countSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
		}
		searchSql.append(" order by t.IS_TOP ASC,").append(sortSql);
		return pageSearchByField(TbQyUserInfoForList.class, countSql.toString(), searchSql.toString(), searchMap, pager);
	}

	@Override
	public Pager searchContactByPy(Map searchMap, Pager pager) throws Exception, BaseException {
		String sortTop="";
		if(!AssertUtil.isEmpty(searchMap.get("sortTop"))){
			sortTop="t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}
		String search = "select t.* from TB_QY_USER_INFO t where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName  order by "+sortTop+" t.pinyin";
		String count = "select count(1) from TB_QY_USER_INFO t where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName ";
		return pageSearchByField(TbQyUserInfoVO.class, count, search, searchMap, pager);
	}

	@Override
	public List<TbQyUserInfoPO> searchPersonByName(String orgId, String name) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t where 1=1 and t.org_id =:orgId and t.PERSON_NAME = :name ";
		this.preparedSql(sql);
		this.setPreValue("name", name);
		this.setPreValue("orgId", orgId);
		return super.getList(TbQyUserInfoPO.class);
	}

	@Override
	public List<TbQyUserInfoPO> findUsersByOrgId(String orgId) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t " + " where t.ORG_ID=:orgId order by t.PINYIN";
		preparedSql(sql);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		return super.getList(TbQyUserInfoPO.class);
	}

	@Override
	public int findUsersCountByOrgId(String orgId) throws Exception, BaseException {
		String sql = "select count(1) from TB_QY_USER_INFO t where t.ORG_ID=:orgId  and t.user_status <> '-1' ";
		preparedSql(sql);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		return super.executeCount();
	}

	@Override
	public int findDeptUserCountByUserId(String orgId, String deptFullName) throws Exception, BaseException {
		String count = "select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId"
				+ " and (d.dept_full_name = :deptFullName or d.dept_full_name like :deptFullNameLike) group by t.id) tt ";
		preparedSql(count);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValue("deptFullName", deptFullName);
		setPreValue("deptFullNameLike", deptFullName + "->%");
		return super.executeCount();
	}

	@Override
	public int findDeptUserCountByUserId(String orgId, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		StringBuffer count = new StringBuffer("select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId" + " and (1=2 ");
		if (depts != null && depts.size() > 0) {
			for (TbDepartmentInfoVO dept : depts) {
				count.append(" or d.dept_full_name = '" + dept.getDeptFullName() + "' or d.dept_full_name like '" + dept.getDeptFullName() + "->%' ");
			}
			count.append(" ) group by t.id) tt  ");
		} else {
			return 0;
		}
		preparedSql(count.toString());
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		return super.executeCount();
	}

	@Override
	public List<TbQyUserInfoVO> findUsersByUserNameOrPhone(String orgId, Map<String,Object> map, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {

		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		StringBuffer sql = new StringBuffer("select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.position,t.user_status from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where  d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus ");
		String keyWord = null;
		if(null != map.get("keyWord")){
			keyWord = map.get("keyWord").toString();
		}

		StringBuilder sb = new StringBuilder("");
		if (!AssertUtil.isEmpty(keyWord)) {
			// 向德均 2014-07-22 修改可以按拼音首字母查询
			Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
			Matcher ma = patter.matcher(keyWord);
			// 在字母中间添加%
			if (ma.find()) {
				for (char iterable_element : keyWord.toCharArray()) {
					sb.append(iterable_element).append("%");
				}
			}
			if (sb.length() > 0)
				sb = sb.replace(sb.length() - 1, sb.length(), "");

			if (sb.length() > 0)
				sql.append(" and (t.MOBILE like :keyWord or t.PERSON_NAME like :keyWord or t.PINYIN like :keyWord or t.PINYIN like :keyWord2");
			else
				sql.append(" and (t.MOBILE like :keyWord or t.PERSON_NAME like :keyWord or t.PINYIN like :keyWord ");
			//			sql.append(" or d.department_name like :keyWord)");//maquanyang 2015-8-17 去掉不按部门搜索
			sql.append(")");
		}
		if(null != map.get("position")){
			sql.append(" and t.position like :position ");
		}
		if(null != map.get("nickName")){
			sql.append(" and t.nick_name like :nickName ");
		}
		StringBuffer deptSql = new StringBuffer();
		StringBuffer likeSql = new StringBuffer();
		String[] split;
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (depts != null && depts.size() > 0) {
			int index=0;
			for (TbDepartmentInfoVO dept : depts) {
				// 1为全公司 2为一级部门 3为本部门
				if ("2".equals(dept.getPermission())) {
					// 获取一级部门
					split = dept.getDeptFullName().split("->");
					searchMap.put("fullName"+index, split[0]);
					searchMap.put("fullNameLike"+index, split[0]+ "->%");
					deptSql.append(", :fullName"+index+" ");
					likeSql.append(" or d.dept_full_name like :fullNameLike"+index);
				} else if ("3".equals(dept.getPermission())) {
					searchMap.put("fullName"+index, dept.getDeptFullName());
					searchMap.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
					deptSql.append(", :fullName"+index+" ");
					likeSql.append(" or d.dept_full_name like :fullNameLike"+index);
				}
				index++;
			}
			if (deptSql.length() > 0) {
				deptSql = deptSql.deleteCharAt(0);
			}
			sql.append(" and (d.dept_full_name in(" + deptSql.toString() + ") " + likeSql.toString() + ") ");
		}
		sql.append(" group by t.id order by t.pinyin ");
		preparedSql(sql.toString());

		if (!AssertUtil.isEmpty(keyWord)) {
			setPreValue("keyWord", "%" + keyWord + "%");// 将参数设置进预置语句orgId
		}
		if (!AssertUtil.isEmpty(map.get("position"))) {
			setPreValue("position", "%" + map.get("position").toString() + "%");// 将参数设置进预置语句orgId
		}
		if (!AssertUtil.isEmpty(map.get("nickName"))) {
			setPreValue("nickName", "%" + map.get("nickName").toString() + "%");// 将参数设置进预置语句orgId
		}

		if (sb.length() > 0)
			setPreValue("keyWord2", "%" + sb + "%");

		setPreValues(searchMap);
		this.setPreValue("orgId", orgId);
		setPreValue("aliveStatus", "-1");// 过滤离职
		return super.getList(TbQyUserInfoVO.class);
	}

	@Override
	public TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t where t.user_id=:userId";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(TbQyUserInfoVO.class);
	}

	@Override
	public TbQyUserInfoPO findUserInfoPOByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t where t.user_id=:userId";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(TbQyUserInfoPO.class);
	}

	@Override
	public UserDeptInfoVO findUserDeptByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.*,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
				+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
				" where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id=:userId and ud.user_id=:userId "
				+ " GROUP BY t.id";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(UserDeptInfoVO.class);
	}
	private final static String findUserDeptByUserIds_sql = "select t.*,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
			" where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id=:userId and ud.user_id in(:userIds) "
			+ " GROUP BY t.id";
	@Override
	public List<UserDeptInfoExpandVO> findUserDeptByUserIds(String[] userIds) throws Exception, BaseException {
		if(!AssertUtil.isEmpty(userIds)){
			Map<String,Object> map=new HashMap<String, Object>(1);
			map.put("userIds",userIds);
			return this.searchByField(UserDeptInfoExpandVO.class,findUserDeptByUserIds_sql,map);
		}
		return null;
	}

	@Override
	public UserDeptInfoVO findUserDeptInfoByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.*,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
				+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
				+ " ,GROUP_CONCAT(d.leave_message ORDER BY ud.sort SEPARATOR ';') as leave_message"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
				" where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id=:userId and ud.user_id=:userId "
				+ " GROUP BY t.id";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(UserDeptInfoVO.class);
	}

	@Override
	public List<TbQyUserInfoVO> findUsersByDepartId(String orgId, String departId) throws Exception, BaseException {
		String sql = "select t.id,t.USER_ID,t.PERSON_NAME,t.MOBILE,t.EMAIL,t.SEX,t.SHOR_MOBILE, t.position,t.wx_user_id,t.weixin_num,t.head_pic"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and d.id=:departId"
				+ " group by t.id order by t.PINYIN";
		preparedSql(sql);
		setPreValue("departId", departId);// 将参数设置进预置语句
		setPreValue("orgId", orgId);
		return super.getList(TbQyUserInfoVO.class);
	}

	@Override
	public UserOrgVO getOrgByUserId(String userName) throws Exception, BaseException {
		this.preparedSql(getOrgByUserId_sql);
		this.setPreValue("userName", userName);
		return super.executeQuery(UserOrgVO.class);
	}

	@Override
	public UserOrgVO getUserByUserId(String userName) throws Exception, BaseException {
		this.preparedSql(getUserByUserId_sql);
		this.setPreValue("userName", userName);
		return super.executeQuery(UserOrgVO.class);
	}

	@Override
	public DqdpOrgVO getOrgById(String id) throws Exception {
		this.preparedSql(getOrgById_sql);
		this.setPreValue("id", id);
		return super.executeQuery(DqdpOrgVO.class);
	}

	@Override
	public Pager searchByNameOrPhone(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		/*
		 * StringBuffer likesql = new StringBuffer(""); String departSql = ""; String departWhereSql = " and "; String departWhereEndSql = ""; boolean hasDepart = false;
		 * if(!AssertUtil.isEmpty(searchMap.get("keyWord"))){ hasDepart = (Boolean) searchMap.get("hasDepart"); searchMap.remove("hasDepart"); if(hasDepart){ departSql =
		 * " ,tb_department_info d,tb_qy_user_department_ref ud "; departWhereSql =
		 * " and d.org_id =:orgId and ud.org_id=:orgId and t.USER_ID=ud.user_id and ud.department_id=d.id and (d.department_name like :keyWord or "; departWhereEndSql = ")"; } String keyWord =
		 * searchMap.get("keyWord").toString(); //判断是否含有中文，如果含有中文，表示搜索部门或者人名 Pattern patter=Pattern.compile("[\u4E00-\u9FA5]+"); Matcher ma=patter.matcher(keyWord); if(ma.find()){
		 * searchMap.put("keyWord", "%"+keyWord+"%"); likesql.append(departWhereSql+" t.PERSON_NAME like :keyWord "+departWhereEndSql); } else{ //是否是数字，如果是数字，证明是手机号码
		 * if(StringUtils.isNumeric(keyWord)){ searchMap.put("keyWord", "%"+keyWord+"%"); likesql.append(departWhereSql+" t.MOBILE like :keyWord "+departWhereEndSql); } else{
		 * patter=Pattern.compile("^[a-zA-Z]*$"); ma=patter.matcher(keyWord); StringBuffer sb = new StringBuffer(""); //在字母中间添加% if(ma.find()){ //如果全是字母，字母在7个以下，拆分查询字符 if(keyWord.length()>1 &&
		 * keyWord.length()<7){ char[] chars = keyWord.toCharArray(); for (char iterable_element : chars) { sb.append(iterable_element).append("%"); } searchMap.put("keyWord", "%"+sb.toString()); }
		 * else{ searchMap.put("keyWord", "%"+keyWord+"%"); } likesql.append(departWhereSql+" t.PINYIN like :keyWord "+departWhereEndSql); }else { searchMap.put("keyWord", "%"+keyWord+"%");
		 * likesql.append(departWhereSql+" t.PERSON_NAME like :keyWord "+departWhereEndSql); } } } } searchMap.put("aliveStatus", "-1");//过滤掉离职状态的用户 String searchSql =
		 * "select t.PERSON_NAME, t.wx_user_id,t.head_pic,t.PINYIN,t.user_id" + " from TB_QY_USER_INFO t"+departSql+" where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus " +likesql+
		 * (hasDepart?" group by t.id":"")+" order by t.pinyin "; String countSql = "select count(*) from(select t.id from TB_QY_USER_INFO t" +departSql+
		 * " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus "+likesql+(hasDepart?" group by t.id":"")+") tt";
		 */

		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud "
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.position like :position and t.nick_name like :nickName "
				+" and t.wx_user_id like :wuid "
				+ " and ( t.MOBILE like :mobile or t.PERSON_NAME like :keyWord or t.pinyin like :pinyin ) "
				+ " and  t.PERSON_NAME like :exactName ";
		String count = "select count(1) from (select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud "
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.position like :position and t.nick_name like :nickName "
				+" and t.wx_user_id like :wuid "
				+ " and ( t.MOBILE like :mobile or t.PERSON_NAME like :keyWord or t.pinyin like :pinyin ) "
				+ " and  t.PERSON_NAME like :exactName ";
		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		sql += " " + depqrtSQL +" group by t.id order by t.pinyin ";
		count += " " + depqrtSQL +" group by t.id) tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql, searchMapNewMap, pager);
	}

	@Override
	public int countByOrgId(String orgId) throws Exception, BaseException {
		String sql = "select count(id) from TB_QY_USER_INFO where org_id =:orgId ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return executeCount();
	}
	
	@Override
	public TbQyUserInfoView viewUserById(String id) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t where 1=1  and t.id=:id";
		this.preparedSql(sql);
		this.setPreValue("id", id);
		return this.executeQuery(TbQyUserInfoView.class);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, List<TbDepartmentInfoPO> depts) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("select  t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.USER_STATUS,t.org_id,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
				+ " from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and (1=2 ");

		Map<String, Object> searchMap = new HashMap<String, Object>();
		int index = 0;
		for (TbDepartmentInfoPO dept : depts) {
			searchMap.put("fullName"+index, dept.getDeptFullName());
			searchMap.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
			sql.append(" or d.dept_full_name = :fullName"+index+" or d.dept_full_name like :fullNameLike"+index);
			index++;
		}
		sql.append(" ) group by t.id ");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValues(searchMap);
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public Pager findDeptUserAllByDeptForPager(String orgId, List<TbDepartmentInfoPO> depts, Pager pager) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer(" from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and (1=2 ");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		int index = 0;
		for (TbDepartmentInfoPO dept : depts) {
			searchMap.put("fullName"+index, dept.getDeptFullName());
			searchMap.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
			sql.append(" or d.dept_full_name = :fullName"+index+" or d.dept_full_name like :fullNameLike"+index);
			index++;
		}
		searchMap.put("orgId", orgId);
		String dataSql = "select  t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.USER_STATUS,t.org_id,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id " + sql + ") group by t.id order by t.pinyin ";
		String countSql = "SELECT  count(1) FROM (select 1 " + sql + ") group by t.id ) t ";
		return pageSearchByField(TbQyUserInfoVO.class, countSql, dataSql, searchMap, pager);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserByDept(String orgId, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("select  t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id" + " from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and d.id in(");
		TbDepartmentInfoVO dept;
		Map<String, Object> searchMap = new HashMap<String, Object>();
		for (int i = 0; i < depts.size(); i++) {
			dept = depts.get(i);
			if (i == 0) {
				sql.append(" :dept"+i);
				searchMap.put("dept"+i, dept.getId());
			} else {
				sql.append(" ,").append(" :dept"+i);
				searchMap.put("dept"+i, dept.getId());
			}
		}
		sql.append(" ) group by t.id ");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValues(searchMap);
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, String deptFullName) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id" + " from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.user_status <> :aliveStatus and t.org_id =:orgId and d.org_id =:orgId"
				+ " and ( d.dept_full_name = :deptFullName or d.dept_full_name like :deptFullNameLike ) group by t.id ");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValue("deptFullName", deptFullName);
		setPreValue("deptFullNameLike", deptFullName + "->%");
		setPreValue("aliveStatus", "-1");// 过滤离职
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserByDept(String deptId,Map<String,Object> params) throws Exception, BaseException {
		String sortTop="";
		if(!AssertUtil.isEmpty(params.get("sortTop"))){
			sortTop="t.IS_TOP ASC,";
			params.remove("sortTop");
		}

		StringBuffer sql = new StringBuffer("select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.IS_TOP,t.position,t.user_status,t.attribute" +
				" from TB_QY_USER_INFO t,tb_qy_user_department_ref ud" +
				" where ud.user_id = t.user_id" +
				" and ud.department_id = :deptId and t.user_status <> :aliveStatus order by "+sortTop+" t.pinyin ");
		preparedSql(sql.toString());
		//setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValue("deptId", deptId);
		setPreValue("aliveStatus", "-1");// 过滤离职
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public Pager searchUsersByDepartId(Pager pager, Map<String,Object> params) throws Exception, BaseException {
		String sql = " from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_user_info_ext e where d.id=ud.department_id and (t.person_name like :personName or t.mobile like :personName or t.pinyin like :personName) and t.position like :position and t.wx_user_id like :wxUserId and t.sex = :sex and t.user_status = :userStatus and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and d.id = :deptId and t.user_status <> :aliveStatus " +
				" and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "
				+ " and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ";	//入职时间;
		String dataSql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.IS_TOP,t.position,t.user_status " + sql + " order by t.pinyin ";
		String countSql = "select count(1) " + sql;
		return pageSearchByField(TbQyUserInfoForList.class, countSql, dataSql, params, pager);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersByUserNameOrPhoneOrPinyin(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> findUsersByUserNameOrPhoneOrPinyin(String orgId, String keyWord) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t where 1=1 and t.org_id =:orgId";
		if (!AssertUtil.isEmpty(keyWord)) {
			sql += " and (t.MOBILE like :keyWord or t.PERSON_NAME like :keyWord or t.PINYIN like :pinyin)";
		}
		sql += " order by t.PINYIN";
		preparedSql(sql);

		if (!AssertUtil.isEmpty(keyWord)) {
			setPreValue("keyWord", "%" + keyWord + "%");// 将参数设置进预置语句orgId
			setPreValue("pinyin", "%" + keyWord.toLowerCase() + "%");// 将参数设置进预置语句orgId
		}
		this.setPreValue("orgId", orgId);
		return super.getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getOrgByCorpId(java.lang.String)
	 */
	@Override
	public AccessToken getAccessTokenByCorpId(String corpId) throws Exception, BaseException {
		String sql = "select t.corp_id,t.corp_secret,t.token,c.aes_key" + " from tb_dqdp_organization t left join tb_config_org_aes c" + " on t.corp_id = c.corp_id" + " where t.corp_id = :corpId"
				+ " order by t.LEFTVALUE";
		preparedSql(sql);
		setPreValue("corpId", corpId);
		return super.executeQuery(AccessToken.class);
	}

	public Pager findAll(Pager pager) throws Exception, BaseException {

		String sql = "select t.id,t.USER_ID,o.corp_id,t.USER_STATUS,t.head_pic,t.weixin_num,t.wx_user_id,t.org_id"
				+ " from TB_QY_USER_INFO t,tb_dqdp_organization o where t.org_id=o.ORGANIZATION_ID and t.USER_STATUS <>:userStatus" + " order by t.org_id";
		String count = "select count(1) from TB_QY_USER_INFO t,tb_dqdp_organization o where t.org_id=o.ORGANIZATION_ID and t.USER_STATUS <>:userStatus";
		Map<String, Object> searchMap = new HashMap<String, Object>(1);
		searchMap.put("userStatus", "0");
		return pageSearchByField(UserOrganizationVO.class, count, sql, searchMap, pager);
	}

	@Override
	public List<ExtOrgVO> searchOrgByCode(String orgCode) throws Exception, BaseException {
		String sql = "select t.* from tb_dqdp_organization t where 1=1 and t.org_code = :orgCode ";
		this.preparedSql(sql);
		this.setPreValue("orgCode", orgCode);
		return getList(ExtOrgVO.class);
	}

	@Override
	public Pager searchFirstLetter(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.mobile,t.position,t.wx_user_id,t.user_status,t.attribute,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where  d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId" + " and t.pinyin like :keyWord and t.user_status <> :aliveStatus ";
		String count = "select count(1)from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId" + " and t.pinyin like :keyWord and t.user_status <> :aliveStatus ";
		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		/*if (depts != null && depts.size() > 0) {
			//maquanyang 2015-8-5 修改使用公用方法拼接部门权限语句
			SeachSqlVO seachVO = new SeachSqlVO();
			seachVO.setDepts(depts);
			seachVO.setSearchMap(searchMap);
			SeachSqlVO retuenSeachVO = DepartmentUtil.getdeptSql(seachVO);
			searchMapNewMap = retuenSeachVO.getSearchMap();
			depqrtSQL = retuenSeachVO.getReturnSql();
		}
		depqrtSQL = DepartmentUtil.addSqlToSelect(depqrtSQL, searchMapNewMap, depts);*/
		sql += " " + depqrtSQL + " group by t.id order by t.pinyin ";
		count += " " + depqrtSQL + " group by t.id) tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql, searchMapNewMap, pager);
	}

	@Override
	public List<TbQyUserCommonPO> searchCommonUser(String userId, String toUserId) throws Exception, BaseException {
		String sql = "SELECT t.* from tb_qy_user_common t where 1=1 and t.user_id = :userId and t.to_user_id = :toUserId ";
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		this.setPreValue("toUserId", toUserId);
		return getList(TbQyUserCommonPO.class);
	}
    private final static String searchCommonUser_sql="SELECT t.* from tb_qy_user_common t where 1=1 and t.user_id = :userId and t.to_user_id in (:toUserIds)";
	@Override
	public List<TbQyUserCommonPO> searchCommonUser(String userId, List<String> toUserIds) throws Exception, BaseException {
		Map<String,Object> map=new HashMap<String, Object>(2);
		map.put("userId",userId);
		map.put("toUserIds",toUserIds);
		return this.searchByField(TbQyUserCommonPO.class,searchCommonUser_sql,map);
	}
    private final static String updateTbQyUserCommonByUserIdAndToUserIds_sql="update tb_qy_user_common set relative_num=relative_num+:num where user_id=:userId and to_user_id in (:toUserIds)";
	@Override
	public void updateTbQyUserCommonByUserIdAndToUserIds(String userId, List<String> toUserIds,int num) throws Exception, BaseException {
		Map<String,Object> map=new HashMap<String, Object>(2);
		map.put("userId",userId);
		map.put("toUserIds",toUserIds);
		map.put("num",num);
		this.updateByField(updateTbQyUserCommonByUserIdAndToUserIds_sql,map,false);
	}

	@Override
	public List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit) throws Exception, BaseException {
		String sql = "SELECT t.to_user_id,q.* from tb_qy_user_common t ,tb_qy_user_info q "
				+ " where t.to_user_id=q.user_id and t.user_id = :userId and q.user_status <> :aliveStatus ORDER BY t.relative_num desc,q.PINYIN asc ";
		if (!AssertUtil.isEmpty(limit)) {
			sql += "limit " + limit;
		}
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		this.setPreLargeValue("aliveStatus", "-1");// 过滤掉离职状态的用户
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public void updateCommonUser(String userId, String toUserId, Integer num) {
		try {
			if (userId.equals(toUserId)) {
				return;
			}
			List<TbQyUserCommonPO> list = this.searchCommonUser(userId, toUserId);
			if (list == null || list.size() == 0) {
				TbQyUserCommonPO po = new TbQyUserCommonPO();
				po.setId(UUID.randomUUID().toString());
				po.setUserId(userId);
				po.setToUserId(toUserId);
				po.setRelativeNum(num);
				po.setCreateTime(new Date());
				po.setUpdateTime(new Date());
				this.insert(po);
			} else {
				TbQyUserCommonPO po = list.get(0);
				po.setRelativeNum(po.getRelativeNum() + num);
				po.setUpdateTime(new Date());
				this.updateData(po, false);
			}
		} catch (Exception e) {
			logger.error("将用户更新到常用联系人失败userId:" + userId + ",toUserId" + toUserId, e);
		} catch (BaseException e) {
			logger.error("将用户更新到常用联系人失败userId:" + userId + ",toUserId" + toUserId, e);
		}
	}

	@Override
	public List<TbQyUserCommonPO> isCommonUser(String userId, String toUserId) throws Exception, BaseException {
		String sql = "select t.user_id,t.to_user_id,t.update_time from tb_qy_user_common t where t.user_id =:userId and t.to_user_id =:toUserId ";
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		this.setPreValue("toUserId", toUserId);
		return getList(TbQyUserCommonPO.class);
	}

	@Override
	public void cancleCommonUser(String userId, String toUserId) throws Exception, BaseException {
		String sql = "delete from tb_qy_user_common where user_id = '" + userId + "' and to_user_id = '" + toUserId + "' ";
		this.executeSql(sql);
	}

	@Override
	public List<TbQyUserInfoVO> searchCommonUserList(String userId, String keyword) throws Exception, BaseException {
		String sql = "SELECT t.to_user_id,q.* from tb_qy_user_common t left join tb_qy_user_info q on t.to_user_id=q.user_id " + " where t.user_id = :userId ";
		if (!AssertUtil.isEmpty(keyword)) {
			sql += " and ( q.MOBILE like :keyWord or q.PERSON_NAME like :keyWord or q.pinyin like :keyWord " + "  ) ";
		}
		sql += " ORDER BY t.relative_num desc,update_time DESC ";
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		if (!AssertUtil.isEmpty(keyword)) {
			this.setPreValue("keyWord", keyword);
		}
		return getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findBirthdayUser(java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> findBirthdayUser(String date) throws Exception, BaseException {
		String sql = "select t.* from tb_qy_user_info t where DATE_FORMAT(birthday, '%m%d') = :date ORDER BY t.ORG_ID";
		preparedSql(sql);
		setPreValue("date", date);
		return getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findDeptFullNamesByIds(java.lang.String[])
	 */
	@Override
	public List<TbDepartmentInfoVO> findDeptFullNamesByIds(String[] depIds) throws Exception, BaseException {
		if (depIds == null || depIds.length == 0) {
			return null;
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("id0", depIds[0]);
		String sql = "select t.dept_full_name from TB_DEPARTMENT_INFO t where t.id in( :id0 ";
		if (depIds.length > 2) {
			int index=1;
			for (String id : depIds) {
				sql += " , :id"+index;
				searchMap.put("id"+index, id );
				index++;
			}
		}
		sql += " )";
		preparedSql(sql);
		setPreValues(searchMap);
		return super.getList(TbDepartmentInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getMyAndChildDeptIds(java.lang.String, java.lang.String)
	 */
	@Override
	public String getMyAndChildDeptIds(String deptId, String orgId) throws Exception, BaseException {
		TbDepartmentInfoPO po = super.searchByPk(TbDepartmentInfoPO.class, deptId);
		String sql = "select t.* from TB_DEPARTMENT_INFO t" + " where (t.dept_full_name = :deptFullName or t.dept_full_name like :deptFullNameLike) and t.org_id=:orgId";

		preparedSql(sql);
		setPreValue("deptFullNameLike", po.getDeptFullName() + "->%");
		setPreValue("deptFullName", po.getDeptFullName());
		setPreValue("orgId", orgId);
		List<TbDepartmentInfoPO> list = super.getList(TbDepartmentInfoPO.class);
		String deptIds = "";
		if (list != null && list.size() > 0) {
			for (TbDepartmentInfoPO tbDepartmentInfoPO : list) {
				deptIds += "|" + tbDepartmentInfoPO.getId();
			}
			// 替换掉第一个分割信息
			deptIds = deptIds.replaceFirst("\\|", "");
		}

		return deptIds;
	}

	/**
	 * 获取留言id
	 *
	 * @param userId
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	public Pager getTbAddressbookGuessbookPOByUserId(String userId, Pager pager, Map searchMap) throws Exception, BaseException {

		String sql = "select t.*,u.person_name,u.head_pic from TB_ADDRESSBOOK_GUESSBOOK t,tb_qy_user_info u where t.creator=u.user_id and t.user_id = :userId order by t.create_time desc";

		String countSql = "select count(1) from TB_ADDRESSBOOK_GUESSBOOK t where t.user_id=:userId order by t.create_time desc";

		return pageSearchByField(TbaddressbookguessbookVo.class, countSql, sql, searchMap, pager);

	}

	/**
	 * 获取留言回复
	 *
	 * @return
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	public Pager getTbAddressbookGuessbookPOReply(Pager pager, Map searchMap) throws Exception, BaseException {

		String sql = "SELECT g.*,u.PERSON_NAME,u.head_pic from tb_addressbook_guessbook g " + "LEFT JOIN tb_qy_user_info u on g.creator=u.USER_ID "
				+ "where creator =:creator and guess_id = :guessId order by create_time desc";

		String countSql = "SELECT count(1) from tb_addressbook_guessbook where creator =:creator and guess_id = :guessId";

		return pageSearchByField(TbaddressbookguessbookVo.class, countSql, sql, searchMap, pager);

	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getStatuesGuessCount(java.util.Map)
	 */
	@Override
	public Integer getStatuesGuessCount(Map<String, Object> searchMap) throws Exception, BaseException {
		// String sql = "  select * from tb_addressbook_guessbook where user_id= :userId and status = '0'";

		String countSql = "select  count(1) from tb_addressbook_guessbook where user_id=:userId and status =:status";
		this.preparedSql(countSql);
		this.setPreValue("userId", searchMap.get("userId"));
		this.setPreValue("status", searchMap.get("status"));
		return executeCount();

	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#searchOrgByOrgName(java.lang.String)
	 */
	@Override
	public List<ExtOrgVO> searchOrgByOrgName(String orgName, String orgPid) throws Exception, BaseException {
		String sql = "select t.* from tb_dqdp_organization t" + " where t.ORGANIZATION_DESCRIPTION = :orgName and t.PARENT_ID = :orgPid ";
		this.preparedSql(sql);
		this.setPreValue("orgName", orgName);
		this.setPreValue("orgPid", orgPid);
		return getList(ExtOrgVO.class);
	}

	@Override
	public void changeStatus(String userId) throws Exception, BaseException {
		String sql = "UPDATE tb_addressbook_guessbook SET STATUS='0' WHERE user_id='" + userId + "'";
		this.executeSql(sql);

	}

	@Override
	public Pager findAlluserByDeptId(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		String sortTop="";
		if(!AssertUtil.isEmpty(searchMap.get("sortTop"))){
			sortTop="t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.MOBILE,t.IS_TOP,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where  d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus ";
		String count = "select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus ";
		//maquanyang 2015-8-5 修改使用公用方法拼接部门权限语句

		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		sql += " "+ depqrtSQL + " group by t.id order by "+sortTop+"t.pinyin ";
		count += " "+ depqrtSQL + " group by t.id)tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql, searchMapNewMap, pager);

	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserLoginByUserId(java.lang.String)
	 */
	@Override
	public TbQyUserLoginInfoPO getUserLoginByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_LOGIN_INFO t where t.USER_ID =:userId";
		preparedSql(sql);
		setPreValue("userId", userId);
		return super.executeQuery(TbQyUserLoginInfoPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserLoginByAccount(java.lang.String)
	 */
	@Override
	public TbQyUserLoginInfoPO getUserLoginByAccount(String userAccount) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_LOGIN_INFO t where t.user_account =:userAccount";

		preparedSql(sql);
		setPreValue("userAccount", userAccount);
		return super.executeQuery(TbQyUserLoginInfoPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getDqdpUserByUserName(java.lang.String)
	 */
	@Override
	public TbDqdpUserPO getDqdpUserByUserName(String userName) throws Exception, BaseException {
		String sql = "select t.* from tb_dqdp_user t where t.USER_NAME =:userName";

		preparedSql(sql);
		setPreValue("userName", userName);
		return super.executeQuery(TbDqdpUserPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findAllLoginUserByUserStatus(java.lang.String)
	 */
	@Override
	public List<UserOrganizationVO> findAllLoginUserByUserStatus(String userStatus) throws Exception, BaseException {
		String sql = "SELECT u.id,u.USER_ID,o.corp_id,u.USER_STATUS,u.head_pic,u.weixin_num,u.org_id,u.wx_user_id" + " FROM tb_login_log l, tb_qy_user_info u,tb_dqdp_organization o "
				+ " where l.USER_ID=u.USER_ID and u.org_id=o.ORGANIZATION_ID" + " and u.USER_STATUS=:userStatus" + " GROUP BY u.USER_ID";
		preparedSql(sql);
		this.setPreValue("userStatus", userStatus);
		return super.getList(UserOrganizationVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserSync(java.lang.String)
	 */
	@Override
	public List<TbQyUserSyncPO> getUserSync(String orgid, String newDate) throws Exception, BaseException {
		// String sql = "select * from tb_qy_user_sync where org_id=:orgid order by create_time DESC";
		String sql = "select * from tb_qy_user_sync where org_id=:orgid and create_time > :startTime and create_time < :endTime " + "order by create_time DESC";
		preparedSql(sql);
		this.setPreValue("orgid", orgid);
		this.setPreValue("startTime", newDate + " 00:00:00");
		this.setPreValue("endTime", newDate + " 23:59:59");
		return super.getList(TbQyUserSyncPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getOrgPOByCorpId(java.lang.String)
	 */
	@Override
	public List<ExtOrgPO> getOrgPOByCorpId(String corpId) throws Exception {
		String sql = "select t.* from tb_dqdp_organization t" + " where t.corp_id = :corpId" + " order by t.LEFTVALUE";
		preparedSql(sql);
		setPreValue("corpId", corpId);
		return super.getList(ExtOrgPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getConfigOrgAesPOByCorpId(java.lang.String)
	 */
	@Override
	public List<TbConfigOrgAesPO> getConfigOrgAesPOByCorpId(String corpId) throws Exception, BaseException {
		String sql = "select c.* from tb_config_org_aes c" + " where c.corp_id = :corpId";
		preparedSql(sql);
		setPreValue("corpId", corpId);
		return super.getList(TbConfigOrgAesPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersByPhone(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserInfoVO findUsersByPhone(String orgId, String mobile) throws Exception, BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t  where t.ORG_ID=:orgId " + "and t.mobile=:mobile order by t.PINYIN";
		preparedSql(sql);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValue("mobile", mobile);
		return this.executeQuery(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getPicListByGroupId(java.lang.String)
	 */
	@Override
	public List<TbQyPicPO> getPicListByGroupId(String groupId) throws Exception, BaseException {
		String sql = "SELECT t.* from tb_qy_pic t" + " where t.group_id = :groupId" + " order by t.sort";
		preparedSql(sql);
		setPreValue("groupId", groupId);// 将参数设置进预置语句
		return this.getList(TbQyPicPO.class);
	}

	public List<TbQyPicPO> getPicListByGroupIdZhouNian(String groupId,String type) throws Exception, BaseException {
		String sql = "SELECT t.* from tb_qy_pic t" + " where t.group_id = :groupId and t.pic_type=:picType " + " order by t.sort";
		preparedSql(sql);
		setPreValue("groupId", groupId);// 将参数设置进预置语句
		setPreValue("picType", type);// 将参数设置进预置语句
		return this.getList(TbQyPicPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#delPicListByGroupId(java.lang.String, java.lang.String)
	 */
	@Override
	public void delPicListByGroupId(String groupId, String userId) throws Exception, BaseException {
		// String sql="delete from tb_qy_pic where group_id = :groupId and create_person = :userId";
		String sql = "delete from tb_qy_pic where group_id = :groupId";
		preparedSql(sql);
		this.setPreValue("groupId", groupId);
		// this.setPreValue("userId", userId);
		this.executeUpdate();
	}

	final static String delPicListByGroupIds_SQL = "delete from tb_qy_pic where org_id = :orgId and group_id in (:groupIds) ";

	@Override
	public void delPicListByGroupIds(String[] groupIds, String orgId) throws Exception, BaseException {
		preparedSql(delPicListByGroupIds_SQL.replace(":groupIds", "'" + StringUtil.uniteArry(groupIds, "','") + "'"));
		//this.setPreValue("groupId", StringUtil.uniteArry(groupIds, "','"));
		this.setPreValue("orgId", orgId);
		this.executeUpdate();

	}

	@Override
	public List<TbQyUserInfoVO> findUserInfoByWxUserId(String wxUserId, String corpId) throws Exception, BaseException {
		preparedSql(findUserInfoByWxUserId_sql);
		setPreValue("wxUserId", wxUserId);// 将参数设置进预置语句
		setPreValue("corpId", corpId);
		return super.getList(TbQyUserInfoVO.class);
	}

	@Override
	public List<TbQyUserInfoPO> findUserInfoPOByWxUserId(String wxUserId, String corpId) throws Exception {
		preparedSql(findUserInfoByWxUserId_sql);
		setPreValue("wxUserId", wxUserId);// 将参数设置进预置语句
		setPreValue("corpId", corpId);
		return super.getList(TbQyUserInfoPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersByOrgIdForExport(java.lang.String)
	 */
	@Override
	public List<ExportUserInfo> findUsersByOrgIdForExport(String orgId) throws Exception, BaseException {
		String sql = "select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
				+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
				+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId group by t.id";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return getList(ExportUserInfo.class);
	}

	@Override
	public boolean hasUsersByDepartIdAndFullName(String organId, String deptId, String deptFullName) throws Exception {
		preparedSql(hasUsersByDepartIdAndFullName_sql);
		setPreValue("departId", deptId);// 将参数设置进预置语句
		setPreValue("orgId", organId);
		setPreValue("deptFullName",deptFullName);
		if (super.executeCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasUsersByDepartId(String deptId) throws Exception {
		preparedSql(hasUsersByDepartId_sql);
		setPreValue("departId", deptId);// 将参数设置进预置语句
		if (super.executeCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasUsersByDepartIds(String[] deptIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("departId",deptIds);
		if (countByField(hasUsersByDepartIds_sql,map) > 0) {
			return true;
		}
		return false;
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersByOrgId(java.util.Map)
	 */
	@Override
	public Pager findUsersByOrgId(Map<String, Object> map, Pager pager, List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		StringBuffer likesql = new StringBuffer("");
		String departSql = "";
		String departWhereSql = " and ";
		String departWhereEndSql = "";
		boolean hasDepart = false;
		if (!AssertUtil.isEmpty(map.get("keyWord"))) {
			hasDepart = (Boolean) map.get("hasDepart");
			map.remove("hasDepart");
			if ((depts != null && depts.size() > 0) || hasDepart) {
				departSql = " ,tb_department_info d,tb_qy_user_department_ref ud ";
				departWhereSql = " and d.org_id =:orgId and t.USER_ID=ud.user_id and ud.department_id=d.id and (d.department_name like :keyWord or ";
				departWhereEndSql = ")";
			}
			String keyWord = map.get("keyWord").toString();
			// 判断是否含有中文，如果含有中文，表示搜索部门或者人名
			Pattern patter = Pattern.compile("[\u4E00-\u9FA5]+");
			Matcher ma = patter.matcher(keyWord);
			if (ma.find()) {
				map.put("keyWord", "%" + keyWord + "%");
				likesql.append(departWhereSql + " t.PERSON_NAME like :keyWord " + departWhereEndSql);
			} else {
				// 是否是数字，如果是数字，证明是手机号码
				if (StringUtils.isNumeric(keyWord)) {
					map.put("keyWord", "%" + keyWord + "%");
					likesql.append(departWhereSql + " t.MOBILE like :keyWord " + departWhereEndSql);
				} else {
					patter = Pattern.compile("^[a-zA-Z]*$");
					ma = patter.matcher(keyWord);
					StringBuffer sb = new StringBuffer("");
					// 在字母中间添加%
					if (ma.find()) {
						// 如果全是字母，字母在7个以下，拆分查询字符
						if (keyWord.length() > 1 && keyWord.length() < 7) {
							char[] chars = keyWord.toCharArray();
							for (char iterable_element : chars) {
								sb.append(iterable_element).append("%");
							}
							map.put("keyWord", "%" + sb.toString());
						} else {
							map.put("keyWord", "%" + keyWord + "%");
						}
						likesql.append(departWhereSql + " t.PINYIN like :keyWord " + departWhereEndSql);
					} else {
						map.put("keyWord", "%" + keyWord + "%");
						likesql.append(departWhereSql + " t.PERSON_NAME like :keyWord " + departWhereEndSql);
					}
				}
			}
		}else{
			if ((depts != null && depts.size() > 0) || hasDepart) {
				departSql = " ,tb_department_info d,tb_qy_user_department_ref ud ";
				likesql.append(" and d.org_id =:orgId and t.USER_ID=ud.user_id and ud.department_id=d.id ");
			}
		}
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		if (depts != null && depts.size() > 0) {
			//maquanyang 2015-8-5 修改使用公用方法拼接部门权限语句
			SeachSqlVO seachVO = new SeachSqlVO();
			seachVO.setDepts(depts);
			seachVO.setSearchMap(map);
			SeachSqlVO retuenSeachVO = DepartmentUtil.getdeptSql(seachVO);
			String depqrtSQL = retuenSeachVO.getReturnSql();
			likesql.append(depqrtSQL);
		}
		String searchSql = "select t.PERSON_NAME, t.wx_user_id,t.head_pic,t.PINYIN,t.user_id,t.mobile" + " from TB_QY_USER_INFO t" + departSql
				+ " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus " + likesql + (hasDepart ? " group by t.id" : "") + " order by t.pinyin ";
		String countSql = "select count(id) from(select t.id from TB_QY_USER_INFO t" + departSql + " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus " + likesql
				+ (hasDepart ? " group by t.id" : "") + ") tt";
		return pageSearchByField(TbQyUserInfoForList.class, countSql, searchSql, map, pager);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#batchDelLocationByUserId(java.lang.String)
	 */
	@Override
	public void batchDelLocationByUserId(String userId) throws Exception, BaseException {
		String sql = "delete from TB_QY_WXUSER_LOCATION where user_id =:userId";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		super.executeUpdate();
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserPartInfoByUserId(java.lang.String)
	 */
	@Override
	public TbQyUserInfoVO getUserPartInfoByUserId(String userId) throws Exception, BaseException {
		String sql = "select t.id,t.user_id,t.person_name,t.head_pic,t.wx_user_id from tb_qy_user_info t where USER_ID = :userId and t.user_status <> :aliveStatus";
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		this.setPreValue("aliveStatus", "-1");
		return super.executeQuery(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getAllOrg()
	 */
	@Override
	public List<ExtOrgPO> getAllOrg() throws Exception, BaseException {
		String sql = "select ORGANIZATION_ID,ORGANIZATION_DESCRIPTION,wx_id,corp_id,remindway,step from tb_dqdp_organization";
		this.preparedSql(sql);
		return getList(ExtOrgPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findBirthdayUserByDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<UserDeptInfoVO> findBirthdayUserByDate(String orgId, String lunar, String type) throws Exception, BaseException {
		String sql = "";
		if ("0".equals(type)) {
			sql = "select t.user_id,t.person_name,t.position,t.sex,t.org_id,t.wx_user_id,t.nick_name" + ",GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
					+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
					+ " from tb_qy_user_info t, tb_qy_user_department_ref ud, tb_department_info d"
					+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id=:orgId and t.lunar_calendar=:lunar and t.user_status <> :aliveStatus" + " group by t.id order by t.user_id";
		} else {
			sql = "select t.user_id,t.person_name,t.position,t.sex,t.org_id,t.wx_user_id,t.nick_name" + ",GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
					+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
					+ " from tb_qy_user_info t, tb_qy_user_department_ref ud, tb_department_info d"
					+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id=:orgId and DATE_FORMAT(t.birthday, '%m%d') = :lunar and t.user_status <> :aliveStatus" + " group by t.id order by t.user_id";
		}
		preparedSql(sql);
		setPreValue("orgId", orgId);
		setPreValue("lunar", lunar);
		setPreValue("aliveStatus", "-1");
		return getList(UserDeptInfoVO.class);
	}

	@Override
	public void deleteUserDeptRef(String orgId, String[] userIds) throws Exception, BaseException {
		String sql = "delete from TB_QY_USER_DEPARTMENT_REF where org_id=:orgId  and user_id in (:userIds)";
		Map<String,Object> map = new HashMap<String, Object>(2);
		map.put("orgId", orgId);
		map.put("userIds", userIds);
		this.updateByField(sql,map,true);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#deleteUserDeptRefByUserIdDeptId(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteUserDeptRefByUserIdDeptId(String deptId, String userId)
			throws Exception, BaseException {
		this.preparedSql(deleteUserDeptRefByUserIdDeptId_sql);
		this.setPreValue("deptId", deptId);
		this.setPreValue("userId", userId);
		this.executeUpdate();
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserIdsByOrg(java.lang.String)
	 */
	@Override
	public List<String> getUserIdsByOrg(String orgId) throws Exception, BaseException {
		String sql = "select t.id from tb_qy_user_info t where t.org_id = :orgId";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(String.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersVOByOrgId(java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> findUsersVOByOrgId(String orgId) throws Exception, BaseException {
		String sql = "select t.id,t.user_id,t.person_name,t.mobile,t.head_pic,t.wx_user_id,t.USER_STATUS from tb_qy_user_info t where t.org_id = :orgId";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return super.getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#deletErrorUser(java.lang.String)
	 */
	@Override
	public void deletErrorUser(String orgId) throws Exception, BaseException {
		String sql = "delete from tb_qy_user_info_sync_error where org_id = '" + orgId + "' ";
		this.executeSql(sql);
	}

	@Override
	public Pager getSyncErrorUser(Map searchValue, Pager pager) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String paramSql;
		paramSql = " where a.org_id=:orgId";

		String dataSql = "SELECT a.ID,a.USER_ID,a.ORG_ID,a.PERSON_NAME,a.MOBILE,a.EMAIL,a.POSITION,a.USER_STATUS,a.weixin_num,a.create_time,a.wx_user_id,a.WX_DEPT_ID,a.corp_id,a.remark";
		dataSql += " FROM tb_qy_user_info_sync_error a";

		String countSql = "SELECT count(1) ";
		countSql += " FROM tb_qy_user_info_sync_error a";

		dataSql += paramSql;
		dataSql += " ORDER BY a.CREATE_TIME DESC";

		countSql += paramSql;

		return pageSearchByField(TbQyUserInfoSyncErrorPO.class, countSql, dataSql, searchValue, pager);
	}

	@Override
	public int getSyncErrorUserCountByOrgId(String orgId) throws Exception, BaseException {
		String sql = "SELECT count(1) FROM tb_qy_user_info_sync_error a where a.org_id=:orgId";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return this.executeCount();
	}

	@Override
	public List<ExportUserSyncVO> getSyncErrorUserByOrgId(String orgId) throws Exception, BaseException {
		String sql = "SELECT a.PERSON_NAME,a.weixin_num,a.wx_user_id,a.MOBILE,a.EMAIL,a.POSITION,a.create_time,a.remark";
		sql = sql + " FROM tb_qy_user_info_sync_error a";
		sql = sql + " where a.org_id=:orgId";
		sql = sql + " ORDER BY a.CREATE_TIME DESC";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return this.getList(ExportUserSyncVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersByOrgIdForInterface(java.lang.String)
	 */
	@Override
	public List<InterfaceUser> findUsersByOrgIdForInterface(String orgId) throws Exception, BaseException {
		String sql = "select a.USER_ID,a.ORG_ID as organ_id,a.PERSON_NAME as name,a.MOBILE,a.SHOR_MOBILE as tel,"
				+ " a.EMAIL,a.POSITION,a.qq_num as qq,a.weixin_num,a.head_pic,a.SEX as gender,a.wx_user_id" + ",GROUP_CONCAT(ud.department_id ORDER BY ud.sort SEPARATOR ',') as department_id"
				+ " from TB_QY_USER_INFO a, tb_qy_user_department_ref ud where a.USER_ID=ud.user_id and ud.org_id=:orgId and a.ORG_ID=:orgId" + " group by a.id order by a.PINYIN";
		preparedSql(sql);
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		return super.getList(InterfaceUser.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getAllOrgByCorp()
	 */
	@Override
	public List<ExtOrgPO> getAllOrgByCorp() throws Exception, BaseException {
		String sql = "select ORGANIZATION_ID,ORGANIZATION_DESCRIPTION,wx_id,corp_id,remindway from tb_dqdp_organization GROUP BY corp_id";
		this.preparedSql(sql);
		return getList(ExtOrgPO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#emptyOrgSecretByCorpId(java.lang.String)
	 */
	@Override
	public void emptyOrgSecretByCorpId(String corpId) throws Exception, BaseException {
		String sql = "UPDATE tb_dqdp_organization SET corp_secret=null WHERE corp_id=:corpId";
		preparedSql(sql);
		this.setPreValue("corpId", corpId);
		this.executeUpdate();
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getDQDPUserByCorpId(java.lang.String)
	 */
	@Override
	public UserOrgVO getDQDPUserByCorpId(String corpId) throws Exception, BaseException {
		this.preparedSql(getDQDPUserByCorpId_sql);
		this.setPreValue("corpId", corpId);
		return super.executeQuery(UserOrgVO.class);
	}

	@Override
	public UserRedundancyInfoVO getUserRedundancyInfoByUserId(String userId) throws Exception, BaseException {
		preparedSql(getUserRedundancyInfoByUserId_sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(UserRedundancyInfoVO.class);
	}

	private final static String getUserInfoByUserId_sql = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.head_pic,t.USER_STATUS,t.wx_user_id,t.org_id,t.corp_id,t.position,t.create_time,t.UPDATE_TIME,t.entry_time,t.leave_time"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_ids"
			+ " ,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_names"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_names"
			+ " ,GROUP_CONCAT(d.wx_id SEPARATOR '|') as wx_dept_ids"
			+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
			+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id=:userId and ud.user_id=:userId and t.USER_STATUS <> '-1'" + " GROUP BY t.user_id";
	@Override
	public UserInfoVO getUserInfoByUserId(String userId) throws Exception, BaseException {
		preparedSql(getUserInfoByUserId_sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(UserInfoVO.class);
	}

	private final static String getUserInfoByWxUserId_sql = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.head_pic,t.USER_STATUS,t.wx_user_id,t.org_id,t.corp_id,t.position,t.create_time,t.UPDATE_TIME,t.entry_time,t.leave_time"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_ids"
			+ " ,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_names"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_names"
			+ " ,GROUP_CONCAT(d.wx_id SEPARATOR '|') as wx_dept_ids" +
			" from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
			" where t.USER_ID=ud.user_id and ud.department_id=d.id and t.wx_user_id=:wxUserId and t.corp_id=:corpId and t.USER_STATUS <> '-1' GROUP BY t.user_id";
	@Override
	public UserInfoVO getUserInfoByWxUserId(String wxUserId, String corpId) throws Exception, BaseException {
		preparedSql(getUserInfoByWxUserId_sql);
		setPreValue("wxUserId", wxUserId);// 将参数设置进预置语句
		setPreValue("corpId", corpId);
		return super.executeQuery(UserInfoVO.class);
	}

	private final static String getAllUserInfoByIds_sql = "select t.id,t.USER_ID,t.PERSON_NAME,t.MOBILE," + " t.wx_user_id,t.head_pic,t.ORG_ID,t.corp_id, t.USER_STATUS" + " from TB_QY_USER_INFO t "
			+ " where t.USER_STATUS <> '-1' and t.user_id in (:userIds) ";
	@Override
	public List<TbQyUserInfoVO> getAllUserInfoByIds(String[] userIds) throws Exception, BaseException {
		if (AssertUtil.isEmpty(userIds)) {
			return new ArrayList<TbQyUserInfoVO>();
		}
		Map<String, Object> map = new HashMap<String, Object>(userIds.length + 1);
		map.put("userIds", userIds);
		return super.searchByField(TbQyUserInfoVO.class, getAllUserInfoByIds_sql, map);
	}

	@Override
	public void deleteUserRelate(String[] userIds) throws Exception, BaseException {
		StringBuffer longinsql = new StringBuffer("delete from TB_QY_USER_LOGIN_INFO where USER_ID in ('");
		StringBuffer groupsql = new StringBuffer("delete from tb_qy_user_group_person where USER_ID in ('");
		StringBuffer commonsql = new StringBuffer("delete from tb_qy_user_common where to_user_id in ('");
		String insql = StringUtil.uniteArry(userIds,"','");
		longinsql.append(insql + "')");
		groupsql.append(insql + "')");
		commonsql.append(insql + "')");

		/**
		 * @author lishengtao
		 * 2015-11-17
		 * 增加处理一些信息
		 */
		//部门负责人
		StringBuffer deptToUserSql = new StringBuffer("delete FROM tb_qy_user_receive WHERE user_id IN ('");
		deptToUserSql.append(insql + "')");
		super.addBatch(deptToUserSql.toString());

		//请假默认相关人
		StringBuffer askDefaultPersonSql = new StringBuffer("delete FROM tb_qy_ask_defaultperson WHERE user_id IN ('");
		askDefaultPersonSql.append(insql + "')");
		super.addBatch(askDefaultPersonSql.toString());

		//工作日志默认相关人
		StringBuffer diaryDefaultPersonSql = new StringBuffer("delete FROM tb_qy_diary_defaultperson WHERE user_id IN ('");
		diaryDefaultPersonSql.append(insql + "')");
		super.addBatch(diaryDefaultPersonSql.toString());

		//移动审批默认相关人
		StringBuffer moveapproveDefaultPersonSql = new StringBuffer("delete FROM tb_qy_moveapprove_defaultperson WHERE user_id IN ('");
		moveapproveDefaultPersonSql.append(insql + "')");
		super.addBatch(moveapproveDefaultPersonSql.toString());

		//移动报销默认相关人
		StringBuffer reimbursementDefaultPersonSql = new StringBuffer("delete FROM tb_qy_reimbursement_defaultperson WHERE user_id IN ('");
		reimbursementDefaultPersonSql.append(insql + "')");
		super.addBatch(reimbursementDefaultPersonSql.toString());

		//表单审批默认相关人
		StringBuffer formDefaultPersonSql = new StringBuffer("delete FROM tb_qy_given WHERE REC_ID IN ('");
		formDefaultPersonSql.append(insql + "')");
		super.addBatch(formDefaultPersonSql.toString());

		//请假默认相关人、负责人
		StringBuffer askDefaultSql = new StringBuffer("delete FROM tb_qy_ask_given WHERE REC_ID IN ('");
		askDefaultSql.append(insql + "')");
		super.addBatch(askDefaultSql.toString());

		//任务默认相关人、负责人
		StringBuffer taskDefaultSql = new StringBuffer("delete FROM tb_qy_task_given WHERE REC_ID IN ('");
		taskDefaultSql.append(insql + "')");
		super.addBatch(taskDefaultSql.toString());

		super.addBatch(longinsql.toString());
		super.addBatch(groupsql.toString());
		super.addBatch(commonsql.toString());
		super.execBatch();
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoByOrgIdAndUserIds(String orgId, String userIds) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("select * from TB_QY_USER_INFO where org_id=:orgId and user_id not in ('");
		String insql = userIds.replace(",", "','");
		sql.append(insql + "')");
		this.preparedSql(sql.toString());
		this.setPreValue("orgId", orgId);

		return this.getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#isExitsOrgName(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExitsOrgName(String orgName, String orgPid) throws Exception {
		super.preparedSql(isExitsOrgName_sql);
		super.setPreValue("orgName", orgName);
		super.setPreValue("orgPid", orgPid);
		return super.executeCount() > 0;
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#searchContactManagerByOrgId(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager searchContactManagerByOrgId(Map searchMap, Pager pager) throws Exception, BaseException {
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && !AssertUtil.isEmpty(searchMap.get("endBirthday"))){
			hasBirthday = true;
		}

		StringBuilder dataSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d ");
		StringBuilder countSql = new StringBuilder("select count(1) from (select t.user_id  from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d ");
		//生日关联
		if(hasBirthday){
			dataSql.append(",tb_qy_user_info_ext e ");
			countSql.append(",tb_qy_user_info_ext e ");
		}
		String conditions = " where t.USER_ID=ud.user_id and d.org_id=:orgId and ud.department_id=d.id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus "
				+ " and t.sex = :sex and (d.dept_full_name like :department or d.id=:deptId) and t.position like :position and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime ";	//入职时间
		dataSql.append(conditions);
		countSql.append(conditions);
		if(hasBirthday){
			dataSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			countSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
		}
		List<TbDepartmentInfoPO> deptList = (List<TbDepartmentInfoPO>) searchMap.get("deptList");
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "2", tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "3", "%;" + tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "4", "%;" + tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "5", "%;" + tbDepartmentInfoPO.getDeptFullName());
					paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or d.dept_full_name like :departs" + i
							+ "2 or d.dept_full_name like :departs" + i + "3 or d.dept_full_name like :departs" + i + "4 or d.dept_full_name like :departs" + i + "5 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				dataSql.append(" and (").append(temp).append(")");
				countSql.append(" and (").append(temp).append(")");
			}
		}
		searchMap.remove("deptList");
		//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
		String sortType = (String) searchMap.get("sortType");
		String sortSql= "t.pinyin";
		if(!AssertUtil.isEmpty(sortType)){
			if("1".equals(sortType)){//离职人员按离职时间降序查询
				sortSql= "t.leave_time DESC";
			}
		}
		searchMap.remove("sortType");
		dataSql.append(" GROUP BY t.user_id order by t.IS_TOP ASC,"+sortSql);
		countSql.append(" GROUP BY t.user_id) tt");
		return pageSearchByField(TbQyUserInfoForList.class, countSql.toString(), dataSql.toString(), searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersManagerByOrgIdForExport(java.lang.String)
	 */
	@Override
	public List<ExportUserInfo> findUsersManagerByOrgIdForExport(String orgId, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
		StringBuilder sql = new StringBuilder("select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
				+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
				+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
				+ " from TB_QY_USER_INFO t,tb_qy_user_department_ref ud, tb_department_info d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId ");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "2", tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "3", "%;" + tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "4", "%;" + tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "5", "%;" + tbDepartmentInfoPO.getDeptFullName());
					paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or d.dept_full_name like :departs" + i
							+ "2 or d.dept_full_name like :departs" + i + "3 or d.dept_full_name like :departs" + i + "4 or d.dept_full_name like :departs" + i + "5 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				sql.append(" and (").append(temp).append(")");
			}
		}
		searchMap.remove("deptList");
		sql.append(" group by t.id");
		searchMap.put("orgId", orgId);
		this.preparedSql(sql.toString());
		this.setPreValues(searchMap);
		return getList(ExportUserInfo.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getManagerPersonByUserNameAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbManagerPersonVO getManagerPersonByUserNameAndOrgId(String userName, String orgId) throws Exception, BaseException {
		String sql = "select * from tb_qy_manager_person where user_name = :userName and org_id = :orgId";
		super.preparedSql(sql);
		super.setPreValue("userName", userName);
		super.setPreValue("orgId", orgId);
		return super.executeQuery(TbManagerPersonVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getManagerPersonByPersonIdAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbManagerPersonVO getManagerPersonByPersonIdAndOrgId(String personId, String orgId) throws Exception, BaseException {
		String sql = "select * from tb_qy_manager_person where person_id = :personId and org_id = :orgId";
		super.preparedSql(sql);
		super.setPreValue("personId", personId);
		super.setPreValue("orgId", orgId);
		return super.executeQuery(TbManagerPersonVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersManagerByOrgId(java.lang.String, java.util.List)
	 */
	@Override
	public List<TbQyUserInfoView> findUsersManagerByOrgId(String orgId, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
		StringBuilder sql = new StringBuilder("select t.USER_ID,t.PERSON_NAME"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId ");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "2", tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "3", "%;" + tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "4", "%;" + tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "5", "%;" + tbDepartmentInfoPO.getDeptFullName());
					paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or d.dept_full_name like :departs" + i
							+ "2 or d.dept_full_name like :departs" + i + "3 or d.dept_full_name like :departs" + i + "4 or d.dept_full_name like :departs" + i + "5 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				sql.append(" and (").append(temp).append(")");
			}
		}
		searchMap.remove("deptList");
		sql.append(" group by t.id");
		searchMap.put("orgId", orgId);
		this.preparedSql(sql.toString());
		this.setPreValues(searchMap);
		return getList(TbQyUserInfoView.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getOrgByUserId(java.lang.String)
	 */
	@Override
	public DqdpUserPersonInfoVO searchDqdpUserPersonInfoByUserName(String userName) throws Exception, BaseException {
		preparedSql(searchDqdpUserPersonInfoByUserName_sql);
		setPreValue("userName", userName);// 将参数设置进预置语句
		return this.executeQuery(DqdpUserPersonInfoVO.class);
	}

	@Override
	public Pager searchAllContact(Map searchMap, Pager pager) throws Exception, BaseException {
		// TODO Auto-generated method stub
		String searchSql = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus" + " and (d.dept_full_name like :department or d.id=:deptId)"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)" + " GROUP BY t.user_id order by t.pinyin ";
		String countSql = "select count(1) from (select t.user_id from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId)" + " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)" + " GROUP BY t.user_id) tt";
		return pageSearchByField(TbQyUserInfoForList.class, countSql, searchSql, searchMap, pager);
	}

	@Override
	public Pager searchAllContactByOrgId(Map searchMap, Pager pager) throws Exception, BaseException {
		// TODO Auto-generated method stub
		String searchSql = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic" + " from TB_QY_USER_INFO t"
				+ " where 1=1 and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)" + " order by t.pinyin ";
		String countSql = "select count(1) from TB_QY_USER_INFO t" + " where 1=1 and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)";
		return pageSearchByField(TbQyUserInfoForList.class, countSql, searchSql, searchMap, pager);
	}

	@Override
	public Pager searchAllContactManagerByOrgId(Map searchMap, Pager pager) throws Exception, BaseException {
		// TODO Auto-generated method stub
		StringBuilder dataSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus" + " and (d.dept_full_name like :department or d.id=:deptId)"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)");
		StringBuilder countSql = new StringBuilder("select count(1) from (select t.user_id from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d"
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus" + " and (d.dept_full_name like :department or d.id=:deptId)"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)");
		List<TbDepartmentInfoPO> deptList = (List<TbDepartmentInfoPO>) searchMap.get("deptList");
		if (!AssertUtil.isEmpty(deptList)) {
			StringBuilder paramDepart = new StringBuilder("");
			for (int i = 0; i < deptList.size(); i++) {
				TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
				if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
					searchMap.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
					searchMap.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "2", tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "3", "%;" + tbDepartmentInfoPO.getDeptFullName() + "->%");
					searchMap.put("departs" + i + "4", "%;" + tbDepartmentInfoPO.getDeptFullName() + ";%");
					searchMap.put("departs" + i + "5", "%;" + tbDepartmentInfoPO.getDeptFullName());
					paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or d.dept_full_name like :departs" + i
							+ "2 or d.dept_full_name like :departs" + i + "3 or d.dept_full_name like :departs" + i + "4 or d.dept_full_name like :departs" + i + "5 or");
				}
			}

			if (!AssertUtil.isEmpty(paramDepart.toString())) {
				String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
				dataSql.append(" and (").append(temp).append(")");
				countSql.append(" and (").append(temp).append(")");
			}
		}
		searchMap.remove("deptList");
		dataSql.append(" GROUP BY t.user_id order by t.pinyin ");
		countSql.append(" GROUP BY t.user_id) tt");
		return pageSearchByField(TbQyUserInfoForList.class, countSql.toString(), dataSql.toString(), searchMap, pager);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findTbQyFieldSettingVOList(java.lang.String)
	 */
	@Override
	public List<TbQyFieldSettingVO> findTbQyFieldSettingVOListByOrgId(String orgId) throws Exception, BaseException {
		StringBuilder sql = new StringBuilder("select a.* from tb_qy_field_setting a where a.org_id= :orgId order by sort asc");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);
		return this.getList(TbQyFieldSettingVO.class);
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#delTbQyFieldSettingVOListByOrgId(java.lang.String)
	 */
	@Override
	public void delTbQyFieldSettingVOListByOrgId(String orgId) throws Exception, BaseException {
		StringBuilder sql = new StringBuilder("delete from tb_qy_field_setting where org_id= :orgId");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);
		this.executeUpdate();
	}

	@Override
	public Pager searchContactByDeptId(Map searchMap, Pager pager) throws Exception, BaseException {
		//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
		String sortType = (String) searchMap.get("sortType");
		String sortSql= "t.pinyin";
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && !AssertUtil.isEmpty(searchMap.get("endBirthday"))){
			hasBirthday = true;
		}
		if(!AssertUtil.isEmpty(sortType)){
			if("1".equals(sortType)){//离职人员按离职时间降序查询
				sortSql= "t.leave_time DESC";
			}
		}
		searchMap.remove("sortType");

		// TODO 自动生成的方法存根
		StringBuilder searchSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP,t.leave_time "
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d");
		StringBuilder countSql = new StringBuilder("select count(1) from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d");
		//生日关联
		if(hasBirthday){
			searchSql.append(",tb_qy_user_info_ext e ");
			countSql.append(",tb_qy_user_info_ext e ");
		}
		String conditions = " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus" + " and d.id=:deptId"
				+ " and t.sex = :sex and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
				" and t.wx_user_id LIKE :wxUserId and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				+ " GROUP BY t.user_id ";
		searchSql.append(conditions);
		countSql.append(conditions);
		if(hasBirthday){
			searchSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			countSql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
		}
		searchSql.append(" order by t.IS_TOP ASC,"+sortSql);
		return pageSearchByField(TbQyUserInfoForList.class, countSql.toString(), searchSql.toString(), searchMap, pager);
	}

	@Override
	public List<TbQyUserInfoVO> findUserInfoPOByDepartmentId(String departmentId) throws Exception, BaseException {
		// TODO Auto-generated method stub
		StringBuffer searchSql = new StringBuffer(
				"select u.user_id,u.org_id,u.person_name,u.user_status,u.head_pic,u.weixin_num,u.wx_user_id,u.corp_id,r.department_name,r.department_id as dept_id from tb_qy_user_receive r,tb_qy_user_info u where u.user_id=r.user_id and r.department_id=:departmentId");
		this.preparedSql(searchSql.toString());
		this.setPreValue("departmentId", departmentId);
		return this.getList(TbQyUserInfoVO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findListByNameOrPhone(java.util.Map, java.util.List)
	 */
	@Override
	public List<TbQyUserInfoVO> findListByNameOrPhone(Map searchMap,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.MOBILE,t.SHOR_MOBILE,t.EMAIL,t.POSITION,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where  d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus"
				+ " and ( t.MOBILE like :keyWord or t.PERSON_NAME like :keyWord or t.pinyin like :keyWord) ";
		Map searchMapNewMap = searchMap;
		if (depts != null && depts.size() > 0) {
			//maquanyang 2015-8-5 修改使用公用方法拼接部门权限语句
			SeachSqlVO seachVO = new SeachSqlVO();
			seachVO.setDepts(depts);
			seachVO.setSearchMap(searchMap);
			SeachSqlVO retuenSeachVO = DepartmentUtil.getdeptSql(seachVO);
			searchMapNewMap = retuenSeachVO.getSearchMap();
			String depqrtSQL = retuenSeachVO.getReturnSql();
			sql += " "+ depqrtSQL + "";
		}

		sql += " group by t.id order by t.pinyin ";
		this.preparedSql(sql);
		this.setPreValues(searchMapNewMap);
		return this.getList(TbQyUserInfoVO.class);
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#getUserSearchByTbQyUserSearchVO(cn.com.do1.component.contact.contact.vo.TbQyUserSearchVO)
	 */
	@Override
	public TbQyUserSearchVO getUserSearchByTbQyUserSearchVO(TbQyUserSearchVO vo)
			throws Exception, BaseException {
		if(!AssertUtil.isEmpty(vo)){
			String sql = "SELECT t.id,t.search_remark FROM tb_qy_user_search t WHERE 1=1 AND t.id = :id AND t.agent_code = :agentCode AND t.org_id = :orgId";
			String id = vo.getId();
			String agentCode = vo.getAgentCode();
			String creater = vo.getCreater();
			String orgId = vo.getOrgId();
			this.preparedSql(sql);
			this.setPreValue("id", id);
			this.setPreValue("agentCode", agentCode);
			//			this.setPreValue("creater", creater);
			this.setPreValue("orgId", orgId);
			return this.executeQuery(TbQyUserSearchVO.class);
		}
		return null;
	}
	
	@Override
	public List<ExportUserInfo> findUsersForExport(Map<String, Object> params)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sortSql= " ORDER BY t.pinyin";
		if(!AssertUtil.isEmpty(params.get("sortType"))){
			if("1".equals(params.get("sortType").toString())){//离职人员按离职时间降序查询
				sortSql= " ORDER BY t.leave_time DESC";
			}
			params.remove("sortType");
		}
		String sql="";
		if(!AssertUtil.isEmpty(params.get("leaveStatus")) && "-1".equals(params.get("leaveStatus"))){
			//搜索离职：离职没有部门，所以没有关联查询，所有没有部门权限管理
			sql = "select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
					+ " ,t.entry_time,t.remind_type"
					+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity"
					+ " from TB_QY_USER_INFO t"
					+ " left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " where t.org_id =:orgId"
					+ " and t.USER_STATUS = :leaveStatus "
					+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
					+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
					+sortSql;
		}else{
			//在职导出
			sql = "select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
				+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
				+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
				+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity"
				+ " from TB_QY_USER_INFO t"
				+ " left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
				+ " ,tb_qy_user_department_ref ud, tb_department_info d "
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId"
				+ " and t.user_status <> :aliveStatus"
				+ " and t.user_status = :leaveStatus "
				+ " and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId)"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
				+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " group by t.id"+sortSql;
		}

		return this.searchByField(ExportUserInfo.class, sql, params);
	}

	@Override
	public List<ExportUserInfo> findUsersManagerForExport(
			Map<String, Object> params, List<TbDepartmentInfoPO> deptList)
					throws Exception, BaseException {
		String sortSql= " ORDER BY t.IS_TOP ASC,t.pinyin";
		if(!AssertUtil.isEmpty(params.get("sortType"))){
			if("1".equals(params.get("sortType").toString())){//离职人员按离职时间降序查询
				sortSql= " ORDER BY t.IS_TOP ASC, t.leave_time DESC";
			}
			params.remove("sortType");
		}

		StringBuilder sql=new StringBuilder("");
		if(!AssertUtil.isEmpty(params.get("leaveStatus")) && "-1".equals(params.get("leaveStatus"))){
			//搜索离职：离职没有部门，所以没有关联查询，所有没有部门权限管理
			sql.append( "select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
					+ " ,t.entry_time,t.remind_type"
					+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity"
					+ " from TB_QY_USER_INFO t"
					+ " left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " where t.org_id =:orgId"
					+ " and t.USER_STATUS = :leaveStatus "
					+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
					+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
					);
		}else{
			sql.append("select t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
					+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
					+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity"
					+ " from TB_QY_USER_INFO t"
					+ " left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " , tb_qy_user_department_ref ud, tb_department_info d"
					+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.org_id =:orgId "
					+ " and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
					+ " and (d.dept_full_name like :department or d.id=:deptId)"
					+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
					+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
					);

			if (!AssertUtil.isEmpty(deptList)) {
				StringBuilder paramDepart = new StringBuilder("");
				for (int i = 0; i < deptList.size(); i++) {
					TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
					if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
						params.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
						params.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
						params.put("departs" + i + "2", tbDepartmentInfoPO.getDeptFullName() + ";%");
						params.put("departs" + i + "3", "%;" + tbDepartmentInfoPO.getDeptFullName() + "->%");
						params.put("departs" + i + "4", "%;" + tbDepartmentInfoPO.getDeptFullName() + ";%");
						params.put("departs" + i + "5", "%;" + tbDepartmentInfoPO.getDeptFullName());
						paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or d.dept_full_name like :departs" + i
								+ "2 or d.dept_full_name like :departs" + i + "3 or d.dept_full_name like :departs" + i + "4 or d.dept_full_name like :departs" + i + "5 or");
					}
				}

				if (!AssertUtil.isEmpty(paramDepart.toString())) {
					String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
					sql.append(" and (").append(temp).append(")");
				}
			}
			sql.append(" group by t.id");
		}
		params.remove("deptList");

		sql.append(sortSql);

		return this.searchByField(ExportUserInfo.class, sql.toString(), params);
	}
	
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findUsersVOByOrgIdAndUserStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public List<UserInfoVO> findUsersVOByOrgIdAndUserStatus(String orgId,
			String userStatus) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("SELECT t.id,t.user_id,t.person_name,t.mobile,t.head_pic,t.wx_user_id,t.org_id,t.position,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') AS dept_full_names,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') AS dept_ids" +
				",t.create_time,t.UPDATE_TIME,t.entry_time,t.leave_time,t.user_status" +
				" FROM TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud" +
				" WHERE  d.id=ud.department_id AND ud.user_id = t.user_id AND t.org_id =:orgId AND d.org_id =:orgId");
		//maquanyang 2015-8-24 存在按用户状态查询条件才加入
		if(!AssertUtil.isEmpty(userStatus)){
			sql.append(" AND t.user_status <> :aliveStatus");
		}
		sql.append(" GROUP BY t.id");
		this.preparedSql(sql.toString());
		this.setPreValue("orgId", orgId);
		if(!AssertUtil.isEmpty(userStatus)){
			this.setPreValue("aliveStatus", userStatus);
		}
		return super.getList(UserInfoVO.class);
	}

	@Override
	public UserInfoVO findUserInfoDepartByUserId(String userId,String userStatus) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer("select t.id,t.user_id,t.person_name,t.mobile,t.head_pic,t.wx_user_id,t.org_id,t.position" +
				",t.create_time,t.UPDATE_TIME,t.entry_time,t.leave_time,t.user_status" +
				",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_names,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_ids" +
				" from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud where d.id=ud.department_id and ud.user_id = t.user_id and t.user_id=:userId limit 1");
		//maquanyang 2015-8-24 存在按用户状态查询条件才加入
		if(!AssertUtil.isEmpty(userStatus)){
			sql.append(" and t.user_status <> :aliveStatus");
		}
		preparedSql(sql.toString());
		setPreValue("userId", userId);// 将参数设置进预置语句
		if(!AssertUtil.isEmpty(userStatus)){
			this.setPreValue("aliveStatus", userStatus);
		}
		/*List<UserInfoVO> list = super.getList(UserInfoVO.class);
		if (!AssertUtil.isEmpty(list)) {
			return list.get(0);
		}*/
		return super.executeQuery(UserInfoVO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactDAO#findDeptUserAllByDept(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public List<UserInfoVO> findDeptUserAllByDeptAndSeachType(String orgId,
			List<TbDepartmentInfoPO> depts, String seachType) throws Exception,
			BaseException {
		StringBuffer sql = new StringBuffer("select  t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.USER_STATUS,t.org_id,t.position,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_names,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_ids");

		if("1".equals(seachType)){
			sql.append(",t.create_time,t.UPDATE_TIME,t.entry_time,t.leave_time,t.user_status");
		}
		sql.append(" from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud"
				+ " where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and (1=2 ");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		int index = 0;
		for (TbDepartmentInfoPO dept : depts) {
			searchMap.put("fullName"+index, dept.getDeptFullName());
			searchMap.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
			sql.append(" or d.dept_full_name = :fullName"+index+" or d.dept_full_name like :fullNameLike"+index);
			index++;
		}
		sql.append(" ) group by t.id ");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);// 将参数设置进预置语句
		setPreValues(searchMap);
		return getList(UserInfoVO.class);
	}

	@Override
	public TbQyUserInfoPO getTopUserInfoPO(String orgId) throws Exception,
	BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT * FROM tb_qy_user_info WHERE ORG_ID=:orgId ORDER BY IS_TOP ASC LIMIT 1";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(TbQyUserInfoPO.class);
	}
	
	@Override
	public Pager getUserInfoCertificateTypePager(Map<String, Object> params,
			Pager pager) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.pageSearchByField(CertificateTypeVO.class, getUserInfoCertificateTypePager_countSql, getUserInfoCertificateType_sql, params, pager);
	}

	@Override
	public List<TbQyUserInfoCertificateTypePO> getUserInfoCertificateTypeList(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.searchByField(TbQyUserInfoCertificateTypePO.class, getUserInfoCertificateType_sql, params);
	}

	@Override
	public int countAllPerson() throws Exception, BaseException {
		this.preparedSql(countAllUser);
		return super.executeCount();
	}

	@Override
	public int getCertificateTypeCount(Map<String, Object> params)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(getCertificateTypeCount_sql);
		this.setPreValue("id", params.get("id"));
		this.setPreValue("orgId", params.get("orgId"));
		return this.executeCount();
	}

	@Override
	public List<TbQyUserInfoCertificateTypePO> getCertificateTypeListByTitle(
			String title,String orgId) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(getCertificateTypePOByTitle_sql);
		this.setPreValue("orgId",orgId);
		this.setPreValue("title", title);
		return this.getList(TbQyUserInfoCertificateTypePO.class);
	}

	/*@Override
	public List<TbQyUserInfoVO> findUsersVOByOrgId(Map<String, Object> params)
			throws Exception, BaseException {
		StringBuilder sql=new StringBuilder();
		sql.append( "SELECT u.ID,u.USER_ID,u.ORG_ID,u.PERSON_NAME,u.mobile,u.head_pic,u.wx_user_id,u.USER_STATUS" +
				" ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"+
				" ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
				" FROM tb_qy_user_info u,tb_qy_user_department_ref ud,tb_department_info d"+
				" WHERE u.USER_ID=ud.user_id AND ud.department_id=d.id"+
				" d.org_id=:orgId" +
				" AND u.USER_STATUS <> :leaveStatus");

		//如果都不为空才执行
		if(!AssertUtil.isEmpty(params.get("tbDepartmentInfoPOList")) || !AssertUtil.isEmpty(params.get("userIds"))){
			sql.append(" AND(1=2");
			//部门包括子部门数据
			if(!AssertUtil.isEmpty(params.get("tbDepartmentInfoPOList"))){
				List<TbDepartmentInfoPO> depts=(List<TbDepartmentInfoPO>)params.get("tbDepartmentInfoPOList");
				int index = 0;
				sql.append(" OR(1=2");
				for (TbDepartmentInfoPO dept : depts) {
					params.put("fullName"+index, dept.getDeptFullName());
					params.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
					sql.append(" or d.dept_full_name = :fullName"+index+" or d.dept_full_name like :fullNameLike"+index);
					index++;
				}
				sql.append(")");
				params.remove("tbDepartmentInfoPOList");
			}
			//特定人员
			if(!AssertUtil.isEmpty(params.get("userIds"))){
				String userIds[]=(String[])params.get("userIds");
				int index = 0;
				sql.append(" OR(1=2");
				for (String userId : userIds) {
					params.put("userId"+index, userId);
					sql.append(" or u.USER_ID = :userId"+index);
					index++;
				}
				sql.append(")");
				params.remove("userIds");
			}
			sql.append(")");
		}

		sql.append(	" GROUP BY u.USER_ID ORDER BY u.PINYIN");
		return this.searchByField(TbQyUserInfoVO.class, sql.toString(), params);
	}*/

	@Override
	public Pager searchUserByPersonName(Map<String, Object> params, Pager pager)
			throws Exception, BaseException {
		if (AssertUtil.isEmpty(params.get("deptList"))) {//超级管理员
			return pageSearchByField(TbQyUserInfoVO.class, USERBYPERSONNAME_COUNTSQL, USERBYPERSONNAME_SQL, params, pager);
		}else {//分级管理员
			StringBuilder dataSql = new StringBuilder("select DISTINCT t.user_id,t.PERSON_NAME,t.head_pic from TB_QY_USER_INFO t, " +
					" tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d where t.USER_ID=ud.user_id and ud.department_id=d.id " +
					" and d.org_id=:orgId and t.org_id =:orgId and t.person_name like :personName and t.pinyin like :pinyin ");
			StringBuilder countSql = new StringBuilder("select COUNT(1) from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, " +
					" TB_DEPARTMENT_INFO d where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and " +
					" t.org_id =:orgId and t.person_name like :personName and t.pinyin like :pinyin ");
			List<TbDepartmentInfoPO> deptList = (List<TbDepartmentInfoPO>) params.get("deptList");
			if (!AssertUtil.isEmpty(deptList)) {
				StringBuilder paramDepart = new StringBuilder("");
				for (int i = 0; i < deptList.size(); i++) {
					TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
					if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
						params.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
						params.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
						paramDepart.append(" d.dept_full_name = :departs" + i + "0 or d.dept_full_name like :departs" + i + "1 or");
					}
				}
				if (!AssertUtil.isEmpty(paramDepart.toString())) {
					String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
					dataSql.append(" and (").append(temp).append(")");
					countSql.append(" and (").append(temp).append(")");
				}
			}
			dataSql.append(" order by t.IS_TOP ASC,t.pinyin");
			params.remove("deptList");
			return pageSearchByField(TbQyUserInfoVO.class, countSql.toString(), dataSql.toString(), params, pager);
		}



	}

	@Override
	public List<TbDepartmentInfoVO> searchDeptByDeptName(Map<String, Object> params)
			throws Exception, BaseException {
		List<TbDepartmentInfoVO>list=new ArrayList<TbDepartmentInfoVO>();
		int munber=(Integer) params.get("pageSize");
		params.remove("pageSize");
		if (AssertUtil.isEmpty(params.get("deptList"))) {//超级管理员
			List<TbDepartmentInfoVO>departlist=this.searchByField(TbDepartmentInfoVO.class, DEPTBYDEPT_SQL, params);
			if (departlist.size()>0) {
				String ids=new String() ;
				if (departlist.size()<munber) {
					for (TbDepartmentInfoVO tbDepartmentInfoVO : departlist) {
						ids=ids+"','"+tbDepartmentInfoVO.getId();
					}
					list.addAll(departlist);
					munber=munber-list.size();
					for (TbDepartmentInfoVO tbDepartmentInfoVO : departlist) {
						params.put("deptName",tbDepartmentInfoVO.getDeptFullName()+"->%");
						List<TbDepartmentInfoVO> departlist1=this.searchByField(TbDepartmentInfoVO.class, DEPTBYDEPTNAME_SQL.replace(":ids", "'"+ids+"'"), params);
						if (departlist1.size()>0) {
							if (departlist1.size()<munber) {
								for (TbDepartmentInfoVO tbDepartmentInfoVO2 : departlist1) {
									ids=ids+"','"+tbDepartmentInfoVO.getId();
								}
								list.addAll(departlist1);
								munber=munber-list.size();
							}else {
								for (int j = 0; j < munber; j++) {
									list.add(departlist1.get(j));
								}
								break;
							}
						}
					}
				}else {
					for (int i = 0; i < munber; i++) {
						list.add(departlist.get(i));
					}
				}
			}
		}else {//分级管理员
			StringBuilder dataSql = new StringBuilder("select t.department_name,t.id ,t.parent_depart,t.dept_full_name from TB_DEPARTMENT_INFO t " +
					" where 1=1 and t.org_id =:orgId and t.department_name like :deptName ");
			StringBuilder dataSql1 = new StringBuilder("select t.department_name,t.id ,t.parent_depart,t.dept_full_name " +
					" from TB_DEPARTMENT_INFO t where 1=1 and t.org_id =:orgId and t.dept_full_name like :deptName and t.id not in(:ids) ");
			List<TbDepartmentInfoPO> deptList = (List<TbDepartmentInfoPO>) params.get("deptList");
			if (!AssertUtil.isEmpty(deptList)) {
				StringBuilder paramDepart = new StringBuilder("");
				for (int i = 0; i < deptList.size(); i++) {
					TbDepartmentInfoPO tbDepartmentInfoPO = deptList.get(i);
					if (!AssertUtil.isEmpty(tbDepartmentInfoPO) && !AssertUtil.isEmpty(tbDepartmentInfoPO.getDeptFullName())) {
						params.put("departs" + i + "0", tbDepartmentInfoPO.getDeptFullName());
						params.put("departs" + i + "1", tbDepartmentInfoPO.getDeptFullName() + "->%");
						paramDepart.append(" t.dept_full_name = :departs" + i + "0 or t.dept_full_name like :departs" + i + "1 or");
					}
				}
				if (!AssertUtil.isEmpty(paramDepart.toString())) {
					String temp = paramDepart.toString().substring(0, paramDepart.toString().length() - 2);
					dataSql.append(" and (").append(temp).append(")");
					dataSql1.append(" and (").append(temp).append(")");
				}
			}
			dataSql.append(" order by t.show_order ");
			dataSql1.append(" order by t.show_order ");
			params.remove("deptList");
			List<TbDepartmentInfoVO>departlist=this.searchByField(TbDepartmentInfoVO.class, dataSql.toString(), params);
			if (departlist.size()>0) {
				if (departlist.size()<munber) {
					String ids=new String() ;
					for (TbDepartmentInfoVO tbDepartmentInfoVO : departlist) {
						ids=ids+"','"+tbDepartmentInfoVO.getId();
					}
					list.addAll(departlist);
					munber=munber-list.size();
					for (TbDepartmentInfoVO tbDepartmentInfoVO : departlist) {
						params.put("deptName",tbDepartmentInfoVO.getDeptFullName()+"->%");
						List<TbDepartmentInfoVO> departlist1=this.searchByField(TbDepartmentInfoVO.class, dataSql1.toString().replace(":ids", "'"+ids+"'"), params);
						if (departlist1.size()>0) {
							if (departlist1.size()<munber) {
								for (TbDepartmentInfoVO infoVO : departlist1) {
									ids=ids+"','"+infoVO.getId();
								}
								list.addAll(departlist1);
								munber=munber-list.size();
							}else {
								for (int j = 0; j < munber; j++) {
									list.add(departlist1.get(j));
								}
								break;
							}
						}
					}
				}else {
					for (int i = 0; i < munber; i++) {
						list.add(departlist.get(i));
					}
				}
			}

		}
		return list;
	}

	@Override
	public List<UserInfoVO> getUserInfoVOInUserIds(String userIds,
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="SELECT * FROM tb_qy_user_info WHERE ORG_ID=:orgId" +
				" AND USER_ID IN("+userIds+")" +
				" AND USER_STATUS =:userStatus" +
				" AND USER_STATUS <> :leaveStatus";
		return this.searchByField(UserInfoVO.class, sql, params);
	}

	@Override
	public TbQyUserInfoExtPO getUserInfoExtPOByUserId(String userId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(getUserInfoExtPOByUserId_sql);
		this.setPreValue("userId", userId);
		return this.executeQuery(TbQyUserInfoExtPO.class);
	}

	@Override
	public TbDqdpUserPO getSuperManagerByOrgId(String orgId) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(getSuperManagerByOrgId_sql);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(TbDqdpUserPO.class);
	}

	@Override
	public List<UserOrgVO> getOrgByPerson(Map<String, Object> params)
			throws Exception, BaseException {
		return this.searchByField(UserOrgVO.class, getOrgByPerson_sql, params);
	}

	@Override
	public String getUserIdByWxUserId(String wxUserId, String orgId)
			throws Exception, BaseException {
		this.preparedSql(getUserIdByWxUserId_sql);
		this.setPreValue("wxUserId", wxUserId);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(String.class);
	}

	@Override
	public List<TbQyUserInfoVO> getToUserByDepatIds(String[] depatIds,
			String orgId) throws Exception, BaseException {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("depatIds", depatIds);
		params.put("orgId", orgId);
		params.put("aliveStatus", "-1");
		return searchByField(TbQyUserInfoVO.class, getToUserByDepatIds, params);
	}

	@Override
	public void updateQyUserReceiveByUserId(Map<String, Object> params) throws Exception, BaseException {
		this.preparedSql(updateQyUserReceiveByUserId_sql);
		this.setPreValues(params);
		this.executeUpdate();
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoByIds(String[] ids) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("ids",ids);
		return searchByField(TbQyUserInfoVO.class,getUserInfoByIds_sql,map);
	}

	private static final String getUserInfoByUserIds_sql = "select t.* from tb_qy_user_info t" +
			" where t.USER_ID in(:userIds)";
	@Override
	public List<TbQyUserInfoVO> getUserInfoByUserIds(String[] userIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userIds",userIds);
		return searchByField(TbQyUserInfoVO.class,getUserInfoByUserIds_sql,map);
	}

	@Override
	public void deleteUserLeaveDeptRef(String orgId, List<String> userIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userIds", userIds);
		this.updateByField(deleteUserLeaveDeptRef_sql,map,true);
	}

	@Override
	public List<TbDepartmentInfoVO> getDeptInfoByLeaveUserId(String userId) throws Exception {
		this.preparedSql(getDeptInfoByLeaveUserId_sql);
		this.setPreValue("userId",userId);
		return this.getList(TbDepartmentInfoVO.class);
	}

	@Override
	public List<TbQyUserSecrecyPO> getSecrecyByOrgId(String orgId) throws  Exception, BaseException{
		this.preparedSql(getSecrecyByOrgId_sql);
		this.setPreValue("orgId", orgId);
		return getList(TbQyUserSecrecyPO.class);
	}

	@Override
	public List<TbQyContactSyncPO> getContactSyncList(String corpId, int status) throws Exception {
		this.preparedSql(getContactSyncList_sql);
		this.setPreValue("corpId",corpId);
		this.setPreValue("status",status);
		return this.getList(TbQyContactSyncPO.class);
	}

	@Override
	public List<String> getContactSyncCorpIdList(int status) throws SQLException {
		this.preparedSql(getContactSyncCorpIdList_sql);
		this.setPreValue("status",status);
		return this.getList(String.class);
	}

	@Override
	public List<String> getVersionId(String orgId, int start, int end) throws BaseException, Exception{
		this.preparedSql(GETVERSIONID_SQL);
		this.setPreValue("orgId", orgId);
		this.setPreValue("start", start);
		this.setPreValue("end",end);
		return this.getList(String.class);
	}

	@Override
	public int getVersionOrgCount(String orgId) throws BaseException, Exception{
		this.preparedSql(GET_VERSION_ORG_COUNT);
		this.setPreValue("orgId", orgId);
		return super.executeCount();
	}


	@Override
	public List<TbQyUserSearchSeniorVO> getSeniorByOrgId(String orgId, String userId) throws BaseException, Exception{
		this.preparedSql(getSeniorByOrgId_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("userId", userId);
		return this.getList(TbQyUserSearchSeniorVO.class);
	}

	@Override
	public List<TbQyUserSearchSeniorPO> getSeniorPOByOrgId(String orgId, String userId) throws BaseException, Exception{
		this.preparedSql(getSeniorPOByOrgId_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("userId", userId);
		return this.getList(TbQyUserSearchSeniorPO.class);
	}

	private final static String getOrgInfoExtVOList_sql="SELECT a.*,b.corp_id FROM tb_dqdp_organization_info_ext a,tb_dqdp_organization b WHERE a.org_id=b.ORGANIZATION_ID AND a.org_id IN (:orgIdArray)";
	@Override
	public List<OrgInfoExtVO> getOrgInfoExtVOList(String orgIdArray[]) throws Exception, BaseException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgIdArray", orgIdArray);
		return searchByField(OrgInfoExtVO.class, getOrgInfoExtVOList_sql, paramMap);
	}

	private final static String findUserReceiveByDepartIds_sql = "select u.user_id,u.org_id,u.person_name,u.user_status,u.head_pic,u.weixin_num,u.wx_user_id,u.corp_id,r.department_name,r.department_id as dept_id from tb_qy_user_receive r,tb_qy_user_info u where u.user_id=r.user_id and r.department_id in(:departIds) ";
	@Override
	public List<TbQyUserInfoVO> findUserReceiveByDepartIds(List<String> departIds) throws Exception, BaseException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("departIds", departIds);
		return  searchByField(TbQyUserInfoVO.class, findUserReceiveByDepartIds_sql, paramMap);
	}

	private final static String getManagerInfoByOrgId_sql="select u.USER_NAME from tb_dqdp_organization o ,tb_person_organization_ref po," +
			" tb_dqdp_person p,tb_person_user_ref re,tb_dqdp_user u" +
			" where po.ORG_ID=o.ORGANIZATION_ID and po.PERSON_ID=p.PERSON_ID" +
			" and re.PERSON_ID=p.PERSON_ID and u.USER_ID=re.USER_ID and p.AGE=0" +
			" and o.ORGANIZATION_ID =:orgId";
	@Override
	public String getManagerInfoByOrgId(String orgId) throws Exception, BaseException {
		this.preparedSql(getManagerInfoByOrgId_sql);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(String.class);
	}

	private final static String getUserInfoPOByWxUserIds_sql = "select u.* from tb_qy_user_info u" +
			" where u.corp_id = :corpId and u.wx_user_id in (:wxUserIds) ";
	@Override
	public List<TbQyUserInfoPO> getUserInfoPOByWxUserIds(String corpId, List<String> wxUserIds) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(wxUserIds.size()+2);
		map.put("corpId",corpId);
		map.put("wxUserIds",wxUserIds);
		return super.searchByField(TbQyUserInfoPO.class, getUserInfoPOByWxUserIds_sql, map);
	}

	@Override
	public Pager getUsersByDepartIdToPager(String departId, Map<String,Object> params, Pager pager) throws Exception, BaseException{
		String sortTop="";
		if(!AssertUtil.isEmpty(params.get("sortTop"))){
			sortTop="t.IS_TOP ASC,";
			params.remove("sortTop");
		}

		String sql ="select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.IS_TOP,t.position,t.user_status,t.attribute" +
				" from TB_QY_USER_INFO t,tb_qy_user_department_ref ud" +
				" where ud.user_id = t.user_id" +
				" and ud.department_id = :deptId and t.user_status <> :aliveStatus order by "+sortTop+" t.pinyin ";
		String count = "select count(1) from TB_QY_USER_INFO t,tb_qy_user_department_ref ud" +
				" where ud.user_id = t.user_id" +
				" and ud.department_id = :deptId and t.user_status <> :aliveStatus";
		params.put("deptId", departId);
		params.put("aliveStatus", "-1");// 过滤离职

		return pageSearchByField(TbQyUserInfoVO.class, count, sql, params, pager);
	}

	private static final String getUserHandWritByUserId_sql = "select w.* from tb_qy_user_hand_writ w where w.user_id = :userId ";
	@Override
	public TbQyUserHandWritPO getUserHandWritByUserId(String userId) throws BaseException, Exception{
		this.preparedSql(getUserHandWritByUserId_sql);
		this.setPreValue("userId", userId);
		return this.executeQuery(TbQyUserHandWritPO.class);
	}

	private static final String getUserHandByOrgId_sql = "SELECT w.*, u.person_name, u.wx_user_id, u.head_pic " +
			"FROM tb_qy_user_hand_writ w " +
			"LEFT JOIN tb_qy_user_info u ON u.user_id = w.user_id " +
			"WHERE w.org_id = :orgId order by w.update_time desc";
	private static final String getUserHandByOrgIdCount_sql = "SELECT COUNT(1) " +
			"FROM tb_qy_user_hand_writ w " +
			"LEFT JOIN tb_qy_user_info u ON u.user_id = w.user_id " +
			"WHERE w.org_id = :orgId";
	@Override
	public Pager getUserHandByOrgId(Pager pager, String orgId) throws Exception, BaseException{
		Map<String, Object > map = new HashMap<String, Object>(1);
		map.put("orgId", orgId);
		return pageSearchByField(TbQyUserHandWritVO.class, getUserHandByOrgIdCount_sql, getUserHandByOrgId_sql, map, pager);
	}

	private static final String getUserHandAndUserInfoByUserId_sql = "SELECT w.*, u.person_name, u.wx_user_id, u.head_pic " +
			"FROM tb_qy_user_hand_writ w " +
			"LEFT JOIN tb_qy_user_info u ON u.user_id = w.user_id " +
			"WHERE w.user_id = :userId ";
	public TbQyUserHandWritVO getUserHandAndUserInfoByUserId(String userId) throws Exception, BaseException{
		this.preparedSql(getUserHandAndUserInfoByUserId_sql);
		this.setPreValue("userId", userId);
		return this.executeQuery(TbQyUserHandWritVO.class);
	}

	private static final String getUserDefaultByOrgId_sql = "select s.* from tb_qy_user_default_select s where s.org_id = :orgId;";
	@Override
	public TbQyUserDefaultSelectPO getUserDefaultByOrgId(String orgId) throws BaseException, Exception {
		this.preparedSql(getUserDefaultByOrgId_sql);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(TbQyUserDefaultSelectPO.class);
	}

	private static final String getChangeContactList_sql = "select * from tb_qy_change_contact t" +
			" where t.corp_id = :corpId and t.suite_id = :suiteId and t.time_stamp >= :startTimeStamp and t.id > :startId" +
			" ORDER BY time_stamp asc, id asc";
	@Override
	public List<TbQyChangeContactPO> getChangeContactList(String corpId, String suiteId, long startTimeStamp, long startId, int size) throws Exception, BaseException {
		Pager<TbQyChangeContactPO> pager = new Pager<TbQyChangeContactPO>(size);
		pager.setCurrentPage(1);
		super.preparedSql(SQLBuilder.getPagerSQL(getChangeContactList_sql, pager, getDbType()));
		this.setPreValue("corpId", corpId);
		this.setPreValue("suiteId", suiteId);
		this.setPreValue("startTimeStamp", startTimeStamp);
		this.setPreValue("startId", startId);
		return this.getList(TbQyChangeContactPO.class);
	}
}
