package cn.com.do1.component.contact.contact.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.contact.util.ContactSqlUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.contact.contact.dao.ISelectUserReadOnlyDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;

public class SelectUserReadOnlyDAOImpl extends BaseDAOImpl implements
		ISelectUserReadOnlyDAO {
	private final static transient Logger logger = LoggerFactory
			.getLogger(SelectUserReadOnlyDAOImpl.class);

	@Override
	public List<TbQyFieldSettingVO> findTbQyFieldSettingVOListByOrgId(
			String orgId) throws Exception, BaseException {
		StringBuilder sql = new StringBuilder(
				"select a.* from tb_qy_field_setting a where a.org_id= :orgId order by sort asc");
		preparedSql(sql.toString());
		setPreValue("orgId", orgId);
		return this.getList(TbQyFieldSettingVO.class);
	}

	@Override
	public Pager searchByNameOrPhone(Map searchMap, Pager pager,
									 List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.mobile,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and  d.id=ud.department_id and ud.user_id = t.user_id and t.user_status <> :aliveStatus "
				+" and t.wx_user_id like :wuid and t.position like :position "
				+ " and ( t.MOBILE like :mobile or t.PERSON_NAME like :keyWord or t.pinyin like :pinyin ) "
				+ " and  t.PERSON_NAME like :exactName ";
		String count = "select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and d.id=ud.department_id and ud.user_id = t.user_id and t.user_status <> :aliveStatus "
				+" and t.wx_user_id like :wuid and t.position like :position "
				+ " and ( t.MOBILE like :mobile or t.PERSON_NAME like :keyWord or t.pinyin like :pinyin ) "
				+ " and  t.PERSON_NAME like :exactName ";
		String orgString = DepartmentUtil.appOrgSql(depts);
		sql = sql + orgString ;
		count = count + orgString;
		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		sql += " " + depqrtSQL +" group by t.id order by t.pinyin ";
		count +=" " + depqrtSQL + " group by t.id) tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql,
				searchMapNewMap, pager);
	}



	@Override
	public Pager findAlluserByDeptId(Map searchMap, Pager pager,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		String sortTop = "";
		if (!AssertUtil.isEmpty(searchMap.get("sortTop"))) {
			sortTop = "t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.MOBILE,t.IS_TOP,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud, tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and  d.id=ud.department_id and ud.user_id = t.user_id and t.user_status <> :aliveStatus  ";
		String count = "select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and d.id=ud.department_id and ud.user_id = t.user_id and t.user_status <> :aliveStatus ";
		String orgString = DepartmentUtil.appOrgSql(depts);
		sql = sql + orgString ;
		count = count + orgString;
		// maquanyang 2015-8-5 修改使用公用方法拼接部门权限语句
		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		sql += " " + depqrtSQL + " group by t.id order by " + sortTop
				+ "t.pinyin ";
		count += " " + depqrtSQL + " group by t.id)tt ";
		// this.preparedSql(sql);
		// this.setPreValue("orgId",searchMap.get("orgId"));
		// Map<String, Object> searchMap2 = new HashMap<String, Object>(1);
		return pageSearchByField(TbQyUserInfoVO.class, count, sql,
				searchMapNewMap, pager);

	}

	@Override
	public Pager searchContactByPy(Map searchMap, Pager pager)
			throws Exception, BaseException {
		String sortTop = "";
		if (!AssertUtil.isEmpty(searchMap.get("sortTop"))) {
			sortTop = "t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}
		String search = "select t.* from TB_QY_USER_INFO t,tb_qy_agent_user_ref r where r.agent_code = :agentCode AND r.user_id = t.user_id and r.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName  order by "
				+ sortTop + " t.pinyin";
		String count = "select count(1) from TB_QY_USER_INFO t,tb_qy_agent_user_ref r where r.agent_code = :agentCode AND r.user_id = t.user_id and r.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName ";
		return pageSearchByField(TbQyUserInfoVO.class, count, search,
				searchMap, pager);
	}

	@Override
	public Pager searchFirstLetter(Map searchMap, Pager pager,
			List<TbDepartmentInfoVO> depts) throws Exception, BaseException {
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.mobile,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and  d.id=ud.department_id and ud.user_id = t.user_id "
				+ " and t.pinyin like :keyWord and t.user_status <> :aliveStatus ";
		String count = "select count(1)from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = t.user_id "
				+ " and d.id=ud.department_id and ud.user_id = t.user_id "
				+ " and t.pinyin like :keyWord and t.user_status <> :aliveStatus ";
		String orgString = DepartmentUtil.appOrgSql(depts);
		sql = sql + orgString ;
		count = count + orgString;
		Map searchMapNewMap = searchMap;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMapNewMap, searchMap, depts);
		sql += " " + depqrtSQL +" group by t.id order by t.pinyin ";
		count += " " + depqrtSQL +" group by t.id) tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql,
				searchMapNewMap, pager);
	}

	@Override
	public List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit, String agentCode, String corpId)
			throws Exception, BaseException {
		String sql = "SELECT t.to_user_id,q.* from tb_qy_user_common t ,tb_qy_user_info q,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = q.user_id "
				+ " and r.corp_id = :corpId and t.to_user_id=q.user_id and t.user_id = :userId and q.user_status <> :aliveStatus ORDER BY t.relative_num desc,q.PINYIN asc ";
		if (!AssertUtil.isEmpty(limit)) {
			sql += "limit " + limit;
		}
		this.preparedSql(sql);
		this.setPreValue("userId", userId);
		this.setPreValue("corpId", corpId);
		this.setPreValue("agentCode", agentCode);
		this.setPreLargeValue("aliveStatus", "-1");// 过滤掉离职状态的用户
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public Pager getUserGroup(Pager pager, Map<String, Object> map)
			throws Exception, BaseException {
		String paramSql;
		paramSql = " where creator=:user_id";
		paramSql += " and group_name=:group_name";
		String dataSql = "select * from tb_qy_user_group";
		String countSql = "SELECT count(1) from tb_qy_user_group";
		dataSql += paramSql;
		countSql += paramSql;
		return super.pageSearchByField(TbQyUserGroupPO.class, countSql,
				dataSql, map, pager);
	}

	@Override
	public TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception,
			BaseException {
		String sql = "select t.* from TB_QY_USER_INFO t"
				+ " where t.user_id=:userId";
		preparedSql(sql);
		setPreValue("userId", userId);// 将参数设置进预置语句
		return super.executeQuery(TbQyUserInfoVO.class);
	}

	@Override
	public List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception,
			BaseException {
		String sql = "select g.* from tb_qy_user_group g where g.org_Id=:orgId and status='0' ORDER BY show_num ASC ";
		preparedSql(sql);
		setPreValue("orgId", orgId);
		return this.getList(TbQyUserGroupPO.class);
	}

	@Override
	public Pager getUserGroupPerson(Pager pager, Map<String, Object> map)
			throws Exception, BaseException {
		String paramSql;
		paramSql = " and r.corp_id = :corpId and g.user_id=u.USER_ID and g.group_id =:groupId and u.user_status <> :aliveStatus";
		String dataSql = "select g.*,u.PERSON_NAME,u.head_pic from tb_qy_user_group_person g,tb_qy_user_info u,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = u.user_id  ";
		String countSql = "SELECT count(1) from tb_qy_user_group_person g,tb_qy_user_info u,tb_qy_agent_user_ref r WHERE r.agent_code = :agentCode AND r.user_id = u.user_id  ";
		dataSql += paramSql;
		countSql += paramSql;
		// 因为user_id不需要使用，所以先从map中去掉
		map.remove("user_id");
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		return super.pageSearchByField(TbQyUserGroupPersonVO.class, countSql,
				dataSql, map, pager);
	}
	
	private static final String getVisibleRangeUsers_sql = "select user_id from tb_qy_agent_user_ref t where t.org_id = :orgId and t.agent_code = :agentCode ";
	@Override
	public List<String> getVisibleRangeUsers(String orgId, String agentCode)
			throws Exception, BaseException {
		preparedSql(getVisibleRangeUsers_sql);
		setPreValue("orgId", orgId);
		setPreValue("agentCode", agentCode);
		return this.getList(String.class);
	}

	@Override
	public Pager searchContact(Map searchMap, Pager pager) throws Exception,
			BaseException {
		String[] sql = ContactSqlUtil.getSearchContact(searchMap);
		return pageSearchByField(TbQyUserInfoForList.class, sql[1], sql[0], searchMap, pager);
		/*String searchSql = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP from TB_QY_USER_INFO t"
				+ ", tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d,tb_qy_agent_user_ref au "
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId)" + " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" 
				// " and (t.MOBILE like :mobile)" +
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
				+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode GROUP BY t.user_id order by t.IS_TOP DESC,"+sortSql;
		String countSql = "select count(1) from (select t.user_id from TB_QY_USER_INFO t, tb_qy_user_department_ref ud"
				+ ", TB_DEPARTMENT_INFO d ,tb_qy_agent_user_ref au "
				+ " where t.USER_ID=ud.user_id and ud.department_id=d.id and d.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId)" + " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" 
				// " and (t.MOBILE like :mobile)" +
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode GROUP BY t.user_id) tt";
		*/
	}

	@Override
	public Pager searchContactManagerByOrgId(Map searchMap, Pager pager)
			throws Exception, BaseException {
		StringBuilder dataSql = new StringBuilder("select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP"
				+ " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d,tb_qy_agent_user_ref au  "
				+ " where t.USER_ID=ud.user_id and d.org_id=:orgId and ud.department_id=d.id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	);//入职时间
		StringBuilder countSql = new StringBuilder("select count(1) from (select t.user_id  from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d,tb_qy_agent_user_ref au "
				+ " where t.USER_ID=ud.user_id and d.org_id=:orgId and ud.department_id=d.id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (d.dept_full_name like :department or d.id=:deptId) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName)" +
				// " and (t.MOBILE like :mobile)" +
				" and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)"
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	);//入职时间
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

	@Override
	public Pager findUsersByOrgId(Map<String, Object> map, Pager pager, List<TbDepartmentInfoVO> depts)
			throws Exception, BaseException {
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
				departWhereSql = " and d.org_id =:orgId and d.org_id=:orgId and t.USER_ID=ud.user_id and ud.department_id=d.id and (d.department_name like :keyWord or ";
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
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		String searchSql = "select t.PERSON_NAME, t.wx_user_id,t.head_pic,t.PINYIN,t.user_id,t.mobile" + " from TB_QY_USER_INFO t,tb_qy_agent_user_ref au" + departSql
				+ " where 1=1 and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode and t.org_id =:orgId and t.user_status <> :aliveStatus " + likesql + (hasDepart ? " group by t.id" : "") + " order by t.pinyin ";
		String countSql = "select count(id) from(select t.id from TB_QY_USER_INFO t,tb_qy_agent_user_ref au" + departSql + " where 1=1 and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode and t.org_id =:orgId and t.user_status <> :aliveStatus " + likesql
				+ (hasDepart ? " group by t.id" : "") + ") tt";
		return pageSearchByField(TbQyUserInfoForList.class, countSql, searchSql, map, pager);
	}

	@Override
	public List<UserDeptInfoVO> findUserDeptInfosByWxUIdndOrgId(String[] wxuid,String orgId) throws Exception, BaseException {
		String sql = "select t.*,GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name " +
				",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name " +
				",GROUP_CONCAT(d.leave_message ORDER BY ud.sort SEPARATOR ';') as leave_message " +
				" from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d " +
				"where t.USER_ID=ud.user_id and ud.department_id=d.id and t.wx_user_id in( '"+ StringUtil.uniteArry(wxuid, "','")+"' )" +
				"and d.ORG_ID=:orgid " +
				"GROUP BY t.id";
		preparedSql(sql);
		setPreValue("orgid", orgId);
		return getList(UserDeptInfoVO.class);
	}

	@Override
	public List<TbQyUserInfoForPage> searchContactList(Map<String, Object> searchMap) throws Exception, BaseException {
		String[] sql = ContactSqlUtil.getSearchContact(searchMap);
		return searchByField(TbQyUserInfoForPage.class, sql[0], searchMap);
	}

	@Override
	public List<UserRedundancyInfoVO> searchUserRedundancyList(Map<String, Object> searchMap) throws Exception {
		return super.searchByField(UserRedundancyInfoVO.class, ContactSqlUtil.getSearchUserRedundancy(searchMap), searchMap);
	}

	@Override
	public List<TbQyUserInfoVO> searchUserInfoVOList(Map<String, Object> searchMap) throws Exception, BaseException {
		String[] sql = ContactSqlUtil.getSearchContact(searchMap);
		return searchByField(TbQyUserInfoVO.class, sql[0], searchMap);
	}


	@Override
	public int serachUserCount(Map<String, Object> searchMap) throws BaseException, Exception {
		String[] sql = ContactSqlUtil.getSearchContact(searchMap);
		return countByField(sql[1], searchMap);
	}

}
