package cn.com.do1.component.contact.contact.dao.impl;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.vo.UserDeptInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserDeptZhouNianInfoVO;
import cn.com.do1.component.contact.contact.dao.IContactInsideReadOnlyDAO;

public class ContactInsideReadOnlyDAOImpl extends BaseDAOImpl implements IContactInsideReadOnlyDAO{
	
	private final static String findBirthdayUserByDateAndRemindType_sql="select t.user_id,t.person_name,t.position,t.sex,t.org_id,t.wx_user_id,t.nick_name,t.head_pic"
			+ ",GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"
			+ ",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
			+",t.USER_ID as target_user_ids"
			+ " from tb_qy_user_info t, tb_qy_user_department_ref ud, tb_department_info d,tb_qy_user_info_ext ue"
			+ " where t.USER_ID=ud.user_id and ud.department_id=d.id AND t.USER_ID=ue.USER_ID"
			+ " and t.org_id=:orgId and t.user_status <> :aliveStatus "
			+ " and ((ue.birth_month_day=:birthMonthDay and t.remind_type=:remindType1) or (birth_lunar_month_day=:birthLunarMonthDay and t.remind_type=:remindType0)) "
			+ " group by t.id order by t.user_id";
	@Override
	public List<UserDeptInfoVO> findBirthdayUserByDateAndRemindType(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.searchByField(UserDeptInfoVO.class, findBirthdayUserByDateAndRemindType_sql, params);
	}
	
	private final static String findEntryPeople_sql="select t.user_id,t.person_name,t.position,t.sex,t.org_id,t.wx_user_id,t.nick_name,t.entry_time,t.head_pic"+
			",GROUP_CONCAT(d.department_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
			",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"+
			",t.USER_ID as target_user_ids"+
			" from tb_qy_user_info t, tb_qy_user_department_ref ud, tb_department_info d,tb_qy_user_info_ext ue"+
			" where t.USER_ID=ud.user_id and ud.department_id=d.id AND t.USER_ID=ue.USER_ID"+
			" AND t.user_status <> :aliveStatus"+
			" AND t.entry_time <= :entryTime"+
			" AND ue.entry_month_day=:entryMonthDay"+
			" AND t.org_id in (:orgId) "+
			" group by t.id"+
			" order by t.org_id";
	@Override
	public List<UserDeptZhouNianInfoVO> findEntryPeople(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.searchByField(UserDeptZhouNianInfoVO.class, findEntryPeople_sql, params);
	}

}
