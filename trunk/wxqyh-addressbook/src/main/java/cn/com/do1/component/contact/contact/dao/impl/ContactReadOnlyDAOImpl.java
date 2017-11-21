package cn.com.do1.component.contact.contact.dao.impl;

import java.sql.SQLException;

import java.util.*;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxgzhPO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO;
import cn.com.do1.component.contact.contact.util.ContactCustomUtil;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.util.CalendarChangeLunar;

public class ContactReadOnlyDAOImpl extends BaseDAOImpl implements IContactReadOnlyDAO{

	//private final static String getUserInfo_sql="SELECT * FROM tb_qy_user_info t WHERE t.create_time>=:startTime AND t.create_time<=:endTime ORDER BY t.create_time ASC";
	//private final static String getUserInfo_countSql="SELECT count(1) FROM tb_qy_user_info t WHERE t.create_time>=:startTime AND t.create_time<=:endTime ORDER BY t.create_time ASC";
	@Override
	public Pager getUserInfPager(Map<String, Object> searchMap, Pager pager)
			throws Exception, BaseException {
		String getUserInfo_sql="";
		String getUserInfo_countSql="";
		if(!AssertUtil.isEmpty(searchMap.get("isUpdate")) || "1".equals(searchMap.get("isUpdate"))){
			getUserInfo_sql="SELECT t.* FROM tb_qy_user_info t LEFT JOIN tb_qy_user_info_ext ue ON t.USER_ID=ue.USER_ID WHERE  t.create_time>=:startTime AND t.create_time<=:endTime AND ue.is_update is NULL ORDER BY t.create_time ASC";
			getUserInfo_countSql="SELECT count(1) FROM tb_qy_user_info t LEFT JOIN tb_qy_user_info_ext ue ON t.USER_ID=ue.USER_ID WHERE t.create_time>=:startTime AND t.create_time<=:endTime AND ue.is_update is NULL ORDER BY t.create_time ASC";
			searchMap.remove("isUpdate");
		}else{
			getUserInfo_sql="SELECT * FROM tb_qy_user_info t WHERE t.create_time>=:startTime AND t.create_time<=:endTime" +
					" AND ((t.birthday is not null) or(t.lunar_calendar is not null) or (t.entry_time is not null))" +
					" ORDER BY t.create_time ASC";
			getUserInfo_countSql="SELECT count(1) FROM tb_qy_user_info t WHERE t.create_time>=:startTime AND t.create_time<=:endTime ORDER BY t.create_time ASC";
		}
		return this.pageSearchByField(TbQyUserInfoPO.class, getUserInfo_countSql, getUserInfo_sql, searchMap, pager);
	}

	@Override
	public List<TbQyUserInfoPO> getUserInfoList(Map<String, Object> searchMap)
			throws Exception, BaseException {
		String getUserInfo_sql="";
		if(!AssertUtil.isEmpty(searchMap.get("isUpdate")) || "1".equals(searchMap.get("isUpdate"))){
			getUserInfo_sql="SELECT t.* FROM tb_qy_user_info t LEFT JOIN tb_qy_user_info_ext ue ON t.USER_ID=ue.USER_ID WHERE t.create_time>=:startTime AND t.create_time<=:endTime AND ue.is_update is NULL ORDER BY t.create_time ASC";
			searchMap.remove("isUpdate");
		}else{
			getUserInfo_sql="SELECT * FROM tb_qy_user_info t WHERE t.create_time>=:startTime AND t.create_time<=:endTime" +
					" AND ((t.birthday is not null) or(t.lunar_calendar is not null) or (t.entry_time is not null))" +
					" ORDER BY t.create_time ASC";
		}
		return this.searchByField(TbQyUserInfoPO.class, getUserInfo_sql, searchMap);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.dao.IContactReadOnlyDAO#findUsersForExport(java.util.Map)
	 */
	@Override
	public List<ExportUserInfo> findUsersForExport(Map<String, Object> params, boolean isEduVerson)
			throws Exception, BaseException {
		String sortSql= " ORDER BY t.IS_TOP ASC,t.pinyin";
		if(!AssertUtil.isEmpty(params.get("sortType"))){
			if("1".equals(params.get("sortType").toString())){//离职人员按离职时间降序查询
				sortSql= " ORDER BY t.leave_time DESC";
			}
			params.remove("sortType");
		}
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(params.get("startBirthday")) && !AssertUtil.isEmpty(params.get("endBirthday"))){
			hasBirthday = true;
		}
		boolean hasAgentCode = false;
		if(!AssertUtil.isEmpty(params.get("agentCode"))){
			hasAgentCode = true;
		}
		StringBuilder sql= new StringBuilder();
		if(!AssertUtil.isEmpty(params.get("leaveStatus")) && "-1".equals(params.get("leaveStatus"))){
			//搜索离职：离职没有部门，所以没有关联查询，所有没有部门权限管理
			sql.append("select t.USER_ID,t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
					+ " ,t.entry_time,t.remind_type,t.attribute "
					+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity,t.leave_time,t.leave_remark from ");
			//生日关联
			if(hasBirthday){
				sql.append(" tb_qy_user_info_ext e ,");
			}//关联应用可见范围
			if(hasAgentCode){
				sql.append(" tb_qy_agent_user_ref au ,");
			}
			sql.append(" TB_QY_USER_INFO t left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " where t.org_id =:orgId and t.sex = :sex and t.attribute = :attribute "
					+ " and t.USER_STATUS = :leaveStatus and t.position like :position "
					+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
					+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
					+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
					+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
					+ " and t.leave_time >= :startLeaveTime and t.leave_time <= :endLeaveTime ");//离职时间
			if(hasBirthday){
				sql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			}
			if(hasAgentCode){
				sql.append(" and au.user_id = t.user_id and au.org_id = :orgId and au.agent_code = :agentCode");
			}
			sql.append(sortSql);
		}else{
			//在职导出
			sql.append("select t.USER_ID,t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone,t.is_top");
					//+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
					//+ " ,t.attribute,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity from ");
			if(!isEduVerson){
				sql.append(",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name");
			}
			sql.append(",t.entry_time,t.remind_type,t.attribute,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity from ");
			//生日关联
			if(hasBirthday){
				sql.append(" tb_qy_user_info_ext e ,");
			}//关联应用可见范围
			if(hasAgentCode){
				sql.append(" tb_qy_agent_user_ref au ,");
			}
			sql.append(" TB_QY_USER_INFO t left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " ,tb_qy_user_department_ref ud, tb_department_info d "
					+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.sex = :sex and t.attribute = :attribute "
					+ " and t.pinyin like :pinyin "
					+ " and t.MOBILE like :mobile "
					+ " and  t.PERSON_NAME like :exactName "
					+ " and t.user_status <> :aliveStatus"
					+ " and t.user_status = :leaveStatus "
					+ " and t.USER_STATUS = :userStatus and t.position like :position"
					+ " and (d.dept_full_name like :department or d.id=:deptId)"
					+ " and ( t.person_name like :personName or t.pinyin like :pinyinInfo or t.MOBILE like :mobileInfo )"
					+ " and t.wx_user_id LIKE :wxUserId" + " and t.create_time>=:startTimes and t.create_time<=:endTime "
					+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
					+" and t.cancel_time >=:reStartFollowTimes and t.cancel_time <=:reEndFollowTimes "
					+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime ");	//入职时间
			boolean hasCustom = ContactCustomUtil.hasCustom(params);
			List<String> customList = new ArrayList<String>();
			if(hasCustom){//如果查询有自定义字段条件
				customList = ContactCustomUtil.getCustomByMap(params, customList);
				if(customList.size() > 0){
					sql.append(" and t.user_id in (:customList) ");
					params.put("customList", customList);
				}else{//如果从自定义字段表查询不出来数据
					return new ArrayList<ExportUserInfo>();
				}
			}
			if(hasBirthday){
				sql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			}
			if(hasAgentCode){
				sql.append(" and au.user_id = t.user_id and au.org_id = :orgId and au.agent_code = :agentCode");
			}
			sql.append(" group by t.id"+sortSql);
		}

		return this.searchByField(ExportUserInfo.class, sql.toString(), params);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.dao.IContactReadOnlyDAO#findUsersManagerForExport(java.util.Map, java.util.List)
	 */
	@Override
	public List<ExportUserInfo> findUsersManagerForExport(
			Map<String, Object> params, List<TbDepartmentInfoPO> deptList, boolean isEduVerson)
			throws Exception, BaseException {
		String sortSql= " ORDER BY t.IS_TOP ASC,t.pinyin";
		if(!AssertUtil.isEmpty(params.get("sortType"))){
			if("1".equals(params.get("sortType").toString())){//离职人员按离职时间降序查询
				sortSql= " ORDER BY t.IS_TOP ASC, t.leave_time DESC";
			}
			params.remove("sortType");
		}
		boolean hasBirthday = false;
		if(!AssertUtil.isEmpty(params.get("startBirthday")) && !AssertUtil.isEmpty(params.get("endBirthday"))){
			hasBirthday = true;
		}
		boolean hasAgentCode = false;
		if(!AssertUtil.isEmpty(params.get("agentCode"))){
			hasAgentCode = true;
		}
		StringBuilder sql=new StringBuilder("");
		if(!AssertUtil.isEmpty(params.get("leaveStatus")) && "-1".equals(params.get("leaveStatus"))){
			//搜索离职：离职没有部门，所以没有关联查询，所有没有部门权限管理
			sql.append( "select t.USER_ID,t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone"
					+ " ,t.entry_time,t.remind_type,t.attribute "
					+ " ,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity,t.leave_time,t.leave_remark from ");
			//生日关联
			if(hasBirthday){
				sql.append(" tb_qy_user_info_ext e , ");
			}//关联应用可见范围
			if(hasAgentCode){
				sql.append(" tb_qy_agent_user_ref au ,");
			}
			sql.append(" TB_QY_USER_INFO t left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " where t.org_id =:orgId and t.sex = :sex and t.attribute = :attribute "
					+ " and t.USER_STATUS = :leaveStatus and t.position like :position "
					+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)"
					+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
					+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
					+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
					+ " and t.leave_time >= :startLeaveTime and t.leave_time <= :endLeaveTime "); //离职时间
			if(hasBirthday){
				sql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			}
			if(hasAgentCode){
				sql.append(" and au.user_id = t.user_id and au.org_id = :orgId and au.agent_code = :agentCode");
			}
		}else{
			sql.append("select t.USER_ID,t.PERSON_NAME,t.MOBILE,t.EMAIL,t.birthday,t.SEX,t.mark,t.address,t.lunar_calendar,"
					+ " t.SHOR_MOBILE,t.position,t.qq_num,t.wx_user_id,t.weixin_num,t.USER_STATUS,t.nick_name,t.phone,t.is_top");
					//+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name,t.entry_time,t.remind_type"
					//+ " ,t.attribute,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity from ");
			if (!isEduVerson) { //非教育版查询部门
				sql.append(",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name");
			}
			sql.append(",t.entry_time,t.remind_type,t.attribute,ct.TITLE as CERTIFICATE_TYPE_TITLE,t.CERTIFICATE_CONTENT,t.identity from");
			//生日关联
			if(hasBirthday){
				sql.append(" tb_qy_user_info_ext e ,");
			}//关联应用可见范围
			if(hasAgentCode){
				sql.append(" tb_qy_agent_user_ref au ,");
			}
			sql.append(" TB_QY_USER_INFO t left join tb_qy_user_info_certificate_type ct on t.CERTIFICATE_TYPE=ct.ID"
					+ " , tb_qy_user_department_ref ud, tb_department_info d"
					+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id=:orgId and t.sex = :sex "
					+ " and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus and t.position like :position "
					+ " and (d.dept_full_name like :department or d.id=:deptId)"
					+ " and ( t.person_name like :personName or t.pinyin like :pinyinInfo or t.MOBILE like :mobileInfo )"
					+ " and t.pinyin like :pinyin "
					+ " and t.MOBILE like :mobile "
					+ " and  t.PERSON_NAME like :exactName "
					+ " and t.wx_user_id LIKE :wxUserId" + " and t.create_time>=:startTimes and t.create_time<=:endTime "
					+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
					+" and t.cancel_time >=:reStartFollowTimes and t.cancel_time <=:reEndFollowTimes "
					+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime ");	//入职时间
			if(hasBirthday){
				sql.append(" and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday ");
			}
			if(hasAgentCode){
				sql.append(" and au.user_id = t.user_id and au.org_id = :orgId and au.agent_code = :agentCode");
			}
			boolean hasCustom = ContactCustomUtil.hasCustom(params);
			List<String> customList = new ArrayList<String>();
			if(hasCustom){//如果查询有自定义字段条件
				customList = ContactCustomUtil.getCustomByMap(params, customList);
				if(customList.size() > 0){
					sql.append(" and t.user_id in (:customList) ");
					params.put("customList", customList);
				}else{//如果从自定义字段表查询不出来数据
					return new ArrayList<ExportUserInfo>();
				}
			}
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

	private static final String findUsersByCorpId_sql = "SELECT q.id,q.USER_ID,q.MOBILE,q.EMAIL,q.weixin_num,q.wx_user_id,q.ORG_ID,q.corp_id,q.USER_STATUS"
			+ " FROM tb_qy_user_info q WHERE q.corp_id = :corpId and q.wx_user_id =:wxUserId";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#findUsersByCorpId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoPO> findUsersByWxUserId(String corpId, String wxUserId)
			throws Exception, BaseException {
		preparedSql(findUsersByCorpId_sql);
		this.setPreValue("corpId", corpId);
		this.setPreValue("wxUserId", wxUserId);
		return super.getList(TbQyUserInfoPO.class);
	}

	private static final String findUsersByMobile_sql = "SELECT q.id,q.USER_ID,q.MOBILE,q.EMAIL,q.weixin_num,q.wx_user_id,q.ORG_ID,q.corp_id,q.USER_STATUS"
			+ " FROM tb_qy_user_info q WHERE q.corp_id = :corpId and q.MOBILE = :mobile";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#findUsersByMobile(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoPO> findUsersByMobile(String corpId, String mobile)
			throws Exception, BaseException {
		preparedSql(findUsersByMobile_sql);
		this.setPreValue("corpId", corpId);
		this.setPreValue("mobile", mobile);
		return super.getList(TbQyUserInfoPO.class);
	}

	private static final String findUsersByWeixinNum_sql = "SELECT q.id,q.USER_ID,q.MOBILE,q.EMAIL,q.weixin_num,q.wx_user_id,q.ORG_ID,q.corp_id,q.USER_STATUS"
			+ " FROM tb_qy_user_info q WHERE q.corp_id = :corpId and q.weixin_num =:weixinNum";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#findUsersByWeixinNum(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoPO> findUsersByWeixinNum(String corpId,
			String weixinNum) throws Exception, BaseException {
		preparedSql(findUsersByWeixinNum_sql);
		this.setPreValue("corpId", corpId);
		this.setPreValue("weixinNum", weixinNum);
		return super.getList(TbQyUserInfoPO.class);
	}

	private static final String findUsersByEmail_sql = "SELECT q.id,q.USER_ID,q.MOBILE,q.EMAIL,q.weixin_num,q.wx_user_id,q.ORG_ID,q.corp_id,q.USER_STATUS"
			+ " FROM tb_qy_user_info q WHERE q.corp_id = :corpId and q.EMAIL =:email";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#findUsersByEmail(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoPO> findUsersByEmail(String corpId, String email)
			throws Exception, BaseException {
		preparedSql(findUsersByEmail_sql);
		this.setPreValue("corpId", corpId);
		this.setPreValue("email", email);
		return super.getList(TbQyUserInfoPO.class);
	}

	private static final String findUsersAndDeptByOrgId_sql = "SELECT GROUP_CONCAT(d.department_name ORDER BY r.sort SEPARATOR ';') as department_name,d.id dept_id,t.* from	tb_qy_user_info t ,tb_department_info d,tb_qy_user_department_ref r WHERE r.department_id = d.id AND t.USER_ID = r.user_id and d.org_id = :orgId and t.USER_STATUS <> :userStatus group by t.id order by t.user_id";
	@Override
	public List<TbQyUserInfoVO> findUsersAndDeptByOrgId(String orgId)
			throws Exception, BaseException {
		//不需要用户状态？
		this.preparedSql(findUsersAndDeptByOrgId_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("userStatus", "-1");
		return super.getList(TbQyUserInfoVO.class);
	}

	private static final String getAllUserAndDeptByIds_sql = "SELECT GROUP_CONCAT(d.department_name ORDER BY r.sort SEPARATOR ';') as department_name,GROUP_CONCAT(d.id ORDER BY r.sort SEPARATOR ';') as dept_id,t.id,t.USER_ID,t.PERSON_NAME,t.MOBILE,t.wx_user_id,t.head_pic,t.weixin_num,t.ORG_ID,t.corp_id from tb_qy_user_info t ,tb_department_info d,tb_qy_user_department_ref r WHERE r.department_id = d.id AND t.USER_ID = r.user_id and t.USER_STATUS <> '-1' and t.user_id in ";
	@Override
	public List<TbQyUserInfoVO> getAllUserAndDeptByIds(String[] userIds)throws Exception,BaseException{
		preparedSql(getAllUserAndDeptByIds_sql + "('" + StringUtil.uniteArry(userIds, "','") + "') group by t.id order by t.user_id");
		return super.getList(TbQyUserInfoVO.class);
	}

	private static final String getUserInfoByWxMobile_sql = "SELECT q.id,q.USER_ID,q.MOBILE,q.EMAIL,q.weixin_num,q.wx_user_id,q.ORG_ID,q.corp_id,q.USER_STATUS"
			+ " FROM tb_qy_user_info q WHERE q.corp_id = :corpId and q.mobile =:mobile";
	@Override
	public TbQyUserInfoVO getUserInfoByMobile(String mobile, String corpId)
			throws Exception, BaseException {
		preparedSql(getUserInfoByWxMobile_sql);
		this.setPreValue("mobile", mobile);
		this.setPreValue("corpId", corpId);
		return executeQuery(TbQyUserInfoVO.class);
	}

	private final static String getCountOrgByCorpId_sql = "select count(1) from tb_dqdp_organization t where t.corp_id = :corpId";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#getCountOrgByCorpId(java.lang.String)
	 */
	@Override
	public int getCountOrgByCorpId(String corpId) throws Exception {
		preparedSql(getCountOrgByCorpId_sql);
		setPreValue("corpId", corpId);
		return executeCount();
	}
	private final static String countQyUserReceiveByUserId_sql="select count(1) from tb_qy_user_receive where user_id=:userId and org_id=:orgId";
	@Override
	public Integer countQyUserReceiveByUserId(String userId, String orgId) throws Exception, BaseException {
		// TODO Auto-generated method stub
		this.preparedSql(countQyUserReceiveByUserId_sql);
		this.setPreValue("userId",userId);
		this.setPreValue("orgId", orgId);
		return this.executeCount();
	}

	private final static String getDelOrgCount_sql="select count(1) from tb_dqdp_organization_del_log where creator=:userId and DATE_FORMAT(create_time,'%Y-%m-%d')=:date";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#getDelOrgCount(java.lang.String)
	 */
	@Override
	public int getDelOrgCount(String userId) throws Exception, BaseException {
		this.preparedSql(getDelOrgCount_sql);
		this.setPreValue("userId",userId);
		this.setPreValue("date",DateUtil.format(new Date(), "yyyy-MM-dd"));
		return this.executeCount();
	}

	@Override
	public int getPepleCount(String orgId, String agentCode, int days)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		if("bless".equals(agentCode)){
			//生日祝福
			Date startDate=new Date();
			Date endDate=DateUtil.addDays(startDate, 30);
			//阳历时间
			String startDateStr = DateUtil.format(startDate, "MM-dd");
			String endDateStr = DateUtil.format(endDate, "MM-dd");
			//阴历时间
			String startLunarDateStr=CalendarChangeLunar.getLunars(startDate);
			String endLunarDateStr=CalendarChangeLunar.getLunars(endDate);

			String sql="SELECT count(1) FROM tb_qy_user_info a,tb_qy_user_info_ext b WHERE a.USER_ID=b.USER_ID"
					+" AND a.ORG_ID=:orgId AND a.USER_STATUS<> :leaveStatus"
					+" AND ((a.remind_type=:remindType1 AND b.birth_month_day >=:startDateStr AND b.birth_month_day<=:endDateStr)"
					+" or (a.remind_type=:remindType0 AND b.birth_lunar_month_day>=:startLunarDateStr AND b.birth_lunar_month_day<=:endLunarDateStr))";
			preparedSql(sql);
			setPreValue("orgId", orgId);
			setPreValue("leaveStatus", -1);
			setPreValue("remindType1", ContactUtil.REMIND_TYPE_ONE);
			setPreValue("startDateStr", startDateStr);
			setPreValue("endDateStr", endDateStr);
			setPreValue("remindType0", ContactUtil.REMIND_TYPE_ZERO);
			setPreValue("startLunarDateStr", startLunarDateStr);
			setPreValue("endLunarDateStr", endLunarDateStr);
			return executeCount();
		}else if("entry".equals(agentCode)){
			//周年祝福
			Date startDate=new Date();
			Date endDate=DateUtil.addDays(startDate, 30);
			//阳历时间
			String startDateStr = DateUtil.format(startDate, "MM-dd");
			String endDateStr = DateUtil.format(endDate, "MM-dd");
			String sql="SELECT count(1) FROM tb_qy_user_info a,tb_qy_user_info_ext b WHERE a.USER_ID=b.USER_ID"
					+" and a.ORG_ID=:orgId and a.USER_STATUS<>:leaveStatus"
					+" and b.entry_month_day>=:startDateStr and b.entry_month_day<=:endDateStr";
			preparedSql(sql);
			setPreValue("orgId", orgId);
			setPreValue("leaveStatus", "-1");
			setPreValue("startDateStr", startDateStr);
			setPreValue("endDateStr", endDateStr);
			return this.executeCount();
		}else{
			return 0;
		}
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO#getPicListByGroupIds(java.lang.String[])
	 */
	@Override
	public List<TbQyPicVO> getPicListByGroupIds(String[] groupIds)
			throws Exception, BaseException {
		String sql = "SELECT t.* from tb_qy_pic t" + " where t.group_id in ( :groupIds ) order by t.sort";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupIds", groupIds);
		return this.searchByField(TbQyPicVO.class, sql, map);
	}

	private final static String countOrgLeavePerson_sql="select count(id) as all_user from tb_qy_user_info where ORG_ID= :orgId and USER_STATUS=:status";
	@Override
	public int countOrgLeavePerson(String orgId) throws Exception {
		this.preparedSql(countOrgLeavePerson_sql);
		this.setPreValue("orgId",orgId);
		this.setPreValue("status", ContactUtil.USER_STAtUS_LEAVE);//离职用户
		return super.executeCount();
	}

	private final static String countOrgPerson_sql="select count(id) as all_user from tb_qy_user_info where ORG_ID= :orgId";
	@Override
	public int countOrgPerson(String orgId) throws Exception {
		this.preparedSql(countOrgPerson_sql);
		this.setPreValue("orgId",orgId);
		return super.executeCount();
	}

	private final static String getUserRedundancyListByUserId_sql = "select t.user_id,t.PERSON_NAME,t.head_pic,t.wx_user_id,t.org_id"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
			+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and t.user_id in(:userIds) and t.USER_STATUS <> '-1'"
			+ " GROUP BY t.user_id";
	@Override
	public List<UserRedundancyInfoVO> getUserRedundancyListByUserId(String[] userIds) throws Exception, BaseException {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userIds",userIds);
		return super.searchByField(UserRedundancyInfoVO.class,getUserRedundancyListByUserId_sql,map);
	}

	private final static String getUserRedundancysByWxUserIds_sql = "select t.user_id,t.PERSON_NAME,t.head_pic,t.wx_user_id,t.org_id"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d"
			+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and t.wx_user_id in (:wxUserIds) and t.corp_id = :corpId"
			+ " GROUP BY t.user_id";
	@Override
	public List<UserRedundancyInfoVO> getUserRedundancysByWxUserIds(List<String> wxUserIds, String corpId) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("wxUserIds", wxUserIds);
		map.put("corpId", corpId);
		return super.searchByField(UserRedundancyInfoVO.class, getUserRedundancysByWxUserIds_sql, map);
	}

	private final static String getUserRedundancyExtListByUserId_sql = "select t.user_id,t.PERSON_NAME,t.head_pic,t.wx_user_id,t.org_id,t.create_time,t.leave_time,t.entry_time,t.USER_STATUS"
			+ " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
			+ " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+ " from TB_QY_USER_INFO t" +
			" LEFT JOIN tb_qy_user_department_ref ud ON t.USER_ID=ud.user_id" +
			" LEFT JOIN tb_department_info d ON ud.department_id=d.id"
			+ " where t.user_id in(:userIds)"
			+ " GROUP BY t.user_id";
	@Override
	public List<UserRedundancyExtInfoVO> getUserRedundancyExtListByUserId(String[] userIds) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userIds",userIds);
		return super.searchByField(UserRedundancyExtInfoVO.class,getUserRedundancyExtListByUserId_sql,map);
	}

	private final static String findDeptUserIdAllByDeptIds_sql = "select ud.user_id from tb_qy_user_department_ref ud" +
			" where ud.department_id in(:deptIds)";
	@Override
	public List<String> findDeptUserIdAllByDeptIds(List<String> deptIds) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(deptIds.size() + 1);
		map.put("deptIds",deptIds);
		return super.searchByField(String.class,findDeptUserIdAllByDeptIds_sql,map);
	}

	private static final  String findUsersByOrgIdForInterface_sql = "select a.USER_ID,a.ORG_ID as organ_id,a.PERSON_NAME as name,a.MOBILE,a.SHOR_MOBILE as tel,"
			+ " a.EMAIL,a.POSITION,a.qq_num as qq,a.weixin_num,a.head_pic,a.SEX as gender,a.wx_user_id,a.USER_STATUS,a.follow_time" + ",GROUP_CONCAT(ud.department_id ORDER BY ud.sort SEPARATOR ',') as department_id"
			+ " from TB_QY_USER_INFO a, tb_qy_user_department_ref ud where a.USER_ID = ud.user_id and ud.org_id=:orgId and a.ORG_ID=:orgId and a.user_status <> :aliveStatus  " + " group by a.id order by a.IS_TOP ASC, a.PINYIN";
	private static final String findUsersByOrgIdForInterface_sql_count = "select count(1)"
			+ " from TB_QY_USER_INFO a where 1=1 and a.user_status <> :aliveStatus and a.ORG_ID=:orgId ";
	@Override
	public Pager findUsersByOrgIdForInterface(Map searchMap,Pager pager) throws Exception, BaseException{
		return pageSearchByField(InterfaceUser.class, findUsersByOrgIdForInterface_sql_count, findUsersByOrgIdForInterface_sql, searchMap, pager);
	}

	private static final String findVersionOrgByorgId_sql = "select v.org_version from tb_qy_org_version v where v.org_id = :orgId  order by v.org_version desc limit 1 ";
	@Override
	public Integer findVersionOrgByorgId(String orgId) throws Exception, BaseException {
		preparedSql(findVersionOrgByorgId_sql);
		setPreValue("orgId", orgId);
		return this.executeQuery(Integer.class);
	}
	private static final String findWxgzhUserById_sql="select t.* from tb_qy_user_wxgzh t where t.openid=:openid and t.org_id=:orgId";
	@Override
	public TbQyUserWxgzhPO getWxgzhUserById(String openid ,String orgId) throws Exception,BaseException{
		this.preparedSql(findWxgzhUserById_sql);
		this.setPreValue("openid" ,openid);
		this.setPreValue("orgId" ,orgId);
		return this.executeQuery(TbQyUserWxgzhPO.class);
	}

	private static final String findVersionOrg_sql = " select v.id, v.org_id, v.id_type, v.operation_type, v.org_version from tb_qy_org_version v where v.org_id = :orgId and v.org_version = :orgVersion ";
	private static final String findVersionOrg_sql_count = "select count(1) from tb_qy_org_version v where v.org_id = :orgId and v.org_version = :orgVersion " ;
	@Override
	public Pager findVersionOrg(Map searchMap, Pager pager) throws BaseException, Exception{
		return pageSearchByField(TbQyOrgVersionVO.class, findVersionOrg_sql_count, findVersionOrg_sql, searchMap, pager);
	}

	private static final String getUserByIds_sql =  "select a.USER_ID,a.ORG_ID as organ_id,a.PERSON_NAME as name,a.MOBILE,a.SHOR_MOBILE as tel,"
			+ " a.EMAIL,a.POSITION,a.qq_num as qq,a.weixin_num,a.head_pic,a.SEX as gender,a.wx_user_id,a.USER_STATUS,a.follow_time" + ",GROUP_CONCAT(ud.department_id ORDER BY ud.sort SEPARATOR ',') as department_id"
			+ " from TB_QY_USER_INFO a, tb_qy_user_department_ref ud where a.USER_ID = ud.user_id and ud.org_id=:orgId and a.ORG_ID=:orgId and a.USER_ID in(:userIds)  " + " group by a.id order by a.IS_TOP ASC, a.PINYIN";;
	@Override
	public List<InterfaceUser> getUserByIds(List<String> userIds, String orgId) throws BaseException, Exception{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("userIds", userIds);
		map.put("orgId", orgId);
		return super.searchByField(InterfaceUser.class,getUserByIds_sql,map);
	}

	private final static String getUserIdsByWxUserIds_sql = "select u.user_id from tb_qy_user_info u" +
			" where u.corp_id = :corpId and u.wx_user_id in(:wxUserIdList)";
	@Override
	public List<String> getUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(wxUserIdList.size()+2);
		map.put("corpId",corpId);
		map.put("wxUserIdList",wxUserIdList);
		return super.searchByField(String.class,getUserIdsByWxUserIds_sql,map);
	}
	private static final String getWxgzhUserVO_sql="select t.* from tb_qy_user_wxgzh t where t.openid=:userId and t.org_id=:orgId";
	@Override
	public TbQyUserWxgzhVO getWxgzhUserVO(String userId, String orgId)throws Exception,BaseException{
		this.preparedSql(getWxgzhUserVO_sql);
		this.setPreValue("userId", userId);
		this.setPreValue("orgId", orgId);
		return this.executeQuery(TbQyUserWxgzhVO.class);
	}

	@Override
	public List<String> getUserInfoIdByUserIds(List<String> userIds) throws SQLException {
		String sql = "select id from tb_qy_user_info where user_id in (:ids)";
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("ids",userIds);
		return super.searchByField(String.class, sql, map);
	}

	@Override
	public List<TbQyUserInfoVO> findUsersVOByOrgId(Map<String, Object> params) throws SQLException {
		String sql = "SELECT u.ID,u.USER_ID,u.ORG_ID,u.PERSON_NAME,u.mobile,u.head_pic,u.wx_user_id,u.USER_STATUS" +
				" ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"+
				" ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
				" FROM tb_qy_user_info u,tb_qy_user_department_ref ud,tb_department_info d"+
				" WHERE u.USER_ID=ud.user_id AND ud.department_id=d.id AND d.org_id=:orgId" +
				" AND u.USER_STATUS <> :leaveStatus and d.id in (:tbDepartmentInfoPOList)" +
				" GROUP BY u.USER_ID ORDER BY u.PINYIN";
		return this.searchByField(TbQyUserInfoVO.class, sql, params);
	}

	@Override
	public List<TbQyUserInfoVO> findUsersVOByUserIds(Map<String, Object> paramMap) throws SQLException {
		String sql = "SELECT u.ID,u.USER_ID,u.ORG_ID,u.PERSON_NAME,u.mobile,u.head_pic,u.wx_user_id,u.USER_STATUS" +
				" ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"+
				" ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
				" FROM tb_qy_user_info u,tb_qy_user_department_ref ud,tb_department_info d"+
				" WHERE u.USER_ID=ud.user_id AND ud.department_id=d.id" +
				" AND u.USER_STATUS <> :leaveStatus and u.USER_ID in (:userIds)" +
				" GROUP BY u.USER_ID ORDER BY u.PINYIN";
		return this.searchByField(TbQyUserInfoVO.class, sql.toString(), paramMap);
	}
  private final static String countReceiveUserByDepartmentId_sql="select count(1) from tb_qy_user_receive where department_id=:departmentId";
	@Override
	public Integer countReceiveUserByDepartmentId(String departmentId) throws Exception, BaseException {
		if(!AssertUtil.isEmpty(departmentId)){
			this.preparedSql(countReceiveUserByDepartmentId_sql);
			this.setPreValue("departmentId", departmentId);
			return this.executeCount();
		}
		return null;
	}

	private static final String findUserByUserIds_sql = "select u.user_id, u.person_name, u.head_pic,u.wx_user_id, u.USER_STATUS from tb_qy_user_info u where u.user_id in (:userIds)";
	@Override
	public List<SelectUserVO> findUserByUserIds(List<String> userIds) throws BaseException, Exception{
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("userIds", userIds);
		return super.searchByField(SelectUserVO.class,findUserByUserIds_sql,map);
	}

	@Override
	public List<String> getWxUserIdsByUserIds(List<String> userIds) throws SQLException {
		String sql = "select wx_user_id from tb_qy_user_info where user_id in (:ids)";
		Map<String,Object> map = new HashMap<String, Object>(userIds.size() + 1);
		map.put("ids",userIds);
		return super.searchByField(String.class, sql, map);
	}

	@Override
	public List<String> getWxUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws SQLException {
		String sql = "select wx_user_id from tb_qy_user_info where corp_id = :corpId and wx_user_id in (:ids)";
		Map<String,Object> map = new HashMap<String, Object>(wxUserIdList.size() + 2);
		map.put("ids", wxUserIdList);
		map.put("corpId", corpId);
		return super.searchByField(String.class, sql, map);
	}
	private static final String findUserDepartIdsByUserIds_sql = "SELECT user_id," +
			" GROUP_CONCAT(department_id ORDER BY sort SEPARATOR ';') as DEPT_ID FROM tb_qy_user_department_ref " +
			" WHERE user_id in (:userIds) GROUP BY user_id";
	@Override
	public List<TbQyUserInfoPO> findUserDepartIdsByUserIds(List<String> userIds) throws SQLException {
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userIds", userIds);
		return searchByField(TbQyUserInfoPO.class, findUserDepartIdsByUserIds_sql, map);
	}

	private static final String getUserByDepartIds_sql =  "select a.USER_ID,a.ORG_ID as organ_id,a.PERSON_NAME as name,a.MOBILE,a.SHOR_MOBILE as tel,"
			+ " a.EMAIL,a.POSITION,a.qq_num as qq,a.weixin_num,a.head_pic,a.SEX as gender,a.wx_user_id,a.USER_STATUS,a.follow_time" + ",GROUP_CONCAT(ud.department_id ORDER BY ud.sort SEPARATOR ',') as department_id"
			+ " from TB_QY_USER_INFO a, tb_qy_user_department_ref ud where a.USER_ID = ud.user_id and ud.org_id=:orgId and a.ORG_ID=:orgId and ud.department_id = :departId  " + " group by a.id order by a.IS_TOP ASC, a.PINYIN";
	@Override
	public List<InterfaceUser> getUserByDepartIds(String departId, String orgId) throws BaseException, Exception{
		this.preparedSql(getUserByDepartIds_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("departId", departId);
		return super.getList(InterfaceUser.class);
	}
}
