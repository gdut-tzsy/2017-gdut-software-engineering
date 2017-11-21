package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  通讯录sql拼接方法
 *  Created by sunqinghai on 16-4-27.
 */
public class ContactSqlUtil {
    private ContactSqlUtil(){}

    /**
     * 拼接查询sql，返回第一个是查询sql，第二个是统计sql
     * @return
     * @author Sun Qinghai
     * @ 16-4-27
     */
    private static final String user_select = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP, t.leave_time,t.attribute,t.org_id ";
    private static final String user_where = " and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus and t.attribute = :attribute "
            + " and ( t.person_name like :personName or t.pinyin like :pinyinInfo or t.MOBILE like :mobileInfo )"
            + " and t.pinyin like :pinyin "
            + " and t.MOBILE like :mobile "
            + " and  t.PERSON_NAME like :exactName "
            // " and (t.MOBILE like :mobile)" +
            + " and t.sex = :sex and t.position like :position"
            //maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
            + " and t.wx_user_id LIKE :wxUserId and t.create_time>=:startTimes and t.create_time<=:endTime "
            +"and t.cancel_time >=:reStartFollowTimes and t.cancel_time <=:reEndFollowTimes "
            + " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
            + " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime ";
    private static final String agent_user_ref_where = " and au.user_id = t.user_id and au.org_id = :orgId and au.agent_code = :agentCode";
    private static final String user_ext_where = " and e.user_id = t.user_id and e.birth_month_day >= :startBirthday and e.birth_month_day <= :endBirthday";
    private static final String department_where = " and t.USER_ID=ud.user_id";
    private static final String department_id_where = " and ( ud.department_id in (:departIds) or ud.department_id=:deptId or t.user_id in (:userIds) )";
    private static final String default_where = " and t.org_id =:orgId";

    /**
     * 组装用户查询语句，String[0]为输出数据语句，String[1]为输出条数sql语句
     * @param searchMap
     * @return 返回数据
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author sunqinghai
     * @date 2017 -3-7
     */
    public static String[] getSearchContact(Map searchMap) throws BaseException, Exception{
        boolean isNeedUniq = false;
        StringBuilder tempSql = new StringBuilder(" from TB_QY_USER_INFO t");
        StringBuilder ref = new StringBuilder();
        boolean isNotRef = true;
        boolean isNeedOrgId = false; //sql条件是否需要orgid

        //拼接部门数据
        boolean deptIdIsEmpty = !AssertUtil.isEmpty(searchMap.get("deptId"));
        if (deptIdIsEmpty || searchMap.containsKey("departIds")) {//如果有部门id，不需要拼接应用可见范围数据,部门下的所有人肯定对该应用可见
            tempSql.append(" ,tb_qy_user_department_ref ud ");
            ref.append(department_where).append(department_id_where);
            if (deptIdIsEmpty) {
                searchMap.remove("agentCode");
            }
            isNeedUniq = true;
            isNotRef = false;
        }
        else if (searchMap.containsKey("userIds")) {
            ref.append(" and t.user_id in ( :userIds )");
        }
        /*else if(searchMap.containsKey("deptList")){//当部门id为空时，才拼接传入的部门数据
            List<TbDepartmentInfoPO> deptList = (List<TbDepartmentInfoPO>) searchMap.get("deptList");
            if (!AssertUtil.isEmpty(deptList)) {
                isNeedUniq = true;
                tempSql.append(",tb_qy_user_department_ref ud, TB_DEPARTMENT_INFO d ");
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
                    ref.append(department_where).append(" and (").append(temp).append(")");
                    isNotRef = false;
                }
            }
        }
        searchMap.remove("deptList");//清除部门列表
        */

        if(searchMap.containsKey("agentCode")){//当应用code不为空时拼接应用可见范围
            tempSql.append(",tb_qy_agent_user_ref au ");
            ref.append(agent_user_ref_where);
            isNotRef = false;
            isNeedOrgId = true;
        }
        //生日关联，拼接用户扩展表
        if(!AssertUtil.isEmpty(searchMap.get("startBirthday")) && !AssertUtil.isEmpty(searchMap.get("endBirthday"))){
            tempSql.append(",tb_qy_user_info_ext e ");
            ref.append(user_ext_where);
        }
        if(isNotRef){
            ref.append(default_where);
            isNeedOrgId = true;
        }
        if (!isNeedOrgId) {
            searchMap.remove("orgId");
        }
        ref.append(user_where);
        boolean hasCustom = ContactCustomUtil.hasCustom(searchMap);
        List<String> customList = new ArrayList<String>();
        if(hasCustom){//如果查询有自定义字段条件
            customList = ContactCustomUtil.getCustomByMap(searchMap, customList);
            if(customList.size() > 0){
                ref.append(" and t.user_id in (:customList) ");
                searchMap.put("customList", customList);
            }else{//如果从自定义字段表查询不出来数据
                ref.append("and false ");
            }
        }
        tempSql.append(" where 1=1 ").append(ref);

        String sortType = (String) searchMap.get("sortType");
        String sortSql= ",t.pinyin";
        if(!AssertUtil.isEmpty(sortType)){
            if("1".equals(sortType)){//离职人员按离职时间降序查询
                sortSql= ",t.leave_time DESC";
            }
        }
        searchMap.remove("sortType");

        String[] sql = new String[2];
        if(isNeedUniq){
            sql[0] = user_select+tempSql.toString()+" GROUP BY t.user_id order by t.IS_TOP ASC"+sortSql;
            sql[1] = "select count(1) from (select t.user_id"+tempSql.toString()+" GROUP BY t.user_id) tt";
        }
        else{
            sql[0] = user_select+tempSql.toString()+" order by t.IS_TOP ASC"+sortSql;
            sql[1] = "select count(1) "+tempSql.toString();
        }
        return sql;
		/*String searchSql1 = "select t.id,t.user_id,t.PERSON_NAME,t.MOBILE,t.position,t.wx_user_id,t.USER_STATUS,t.head_pic,t.IS_TOP"
				+ " from TB_QY_USER_INFO t,tb_qy_agent_user_ref au "
				+ " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode "
				// " and (t.MOBILE like :mobile)" +
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				//maquanyang 2015-7-20 修改按离职时间降序查询;2015-8-13 修改通讯录列表和离职人员列表使用同一接口，排序字段也不一样
				+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)" + " order by t.IS_TOP DESC,"+sortSql;
		String countSql1 = "select count(1) from TB_QY_USER_INFO t,tb_qy_agent_user_ref au "
				+ " where 1=1 and t.org_id =:orgId and t.user_status <> :aliveStatus and t.user_status = :leaveStatus and t.USER_STATUS = :userStatus"
				+ " and (t.person_name like :personName or t.pinyin like :personName or t.MOBILE like :personName) and au.org_id = :orgId and au.user_id = t.user_id and au.agent_code = :agentCode "
				// " and (t.MOBILE like :mobile)" +
				+ " and t.sex = :sex and t.position like :position "
				+ " and t.lunar_calendar >= :startLunarCalendar and t.lunar_calendar <= :endLunarCalendar "	//农历生日
				+ " and t.entry_time >= :startEntryTime and t.entry_time <= :endEntryTime "	//入职时间
				+ " and t.wx_user_id LIKE :wxUserId" + " and (t.create_time>=:startTimes and t.create_time<=:endTime)";
		*/
    }

    private final static String user_redundancy = "select t.user_id,t.PERSON_NAME,t.head_pic,t.wx_user_id,t.org_id"
            + " ,GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"
            + " ,GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as dept_full_name"
            + " from TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d";
    private final static String user_redundancy_where = " where t.USER_ID=ud.user_id and ud.department_id=d.id and ( t.user_id in (:userIds) or d.id in (:departIds) ) ";

    /**
     * 拼接用户信息查询sql，用于用户缓存信息的查询
     * @param searchMap
     * @return 返回数据
     * @author sunqinghai
     * @date 2017 -3-20
     */
    public static String getSearchUserRedundancy(Map<String, Object> searchMap) {
        StringBuilder ref = new StringBuilder(user_redundancy);

        //拼接部门数据
        if (searchMap.containsKey("agentCode")) {//如果有部门id，不需要拼接应用可见范围数据,部门下的所有人肯定对该应用可见
            ref.append(",tb_qy_agent_user_ref au ").append(user_redundancy_where).append(agent_user_ref_where);
        }
        else {
            ref.append(user_redundancy_where);
            searchMap.remove("orgId");
        }
        ref.append(" GROUP BY t.user_id order by t.IS_TOP ASC");
        return ref.toString();
    }
}
