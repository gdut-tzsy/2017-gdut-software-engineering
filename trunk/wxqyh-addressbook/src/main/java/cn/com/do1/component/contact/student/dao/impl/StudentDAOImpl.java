package cn.com.do1.component.contact.student.dao.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.addressbook.contact.vo.SeachSqlVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.student.dao.IStudentDAO;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.util.StudentSqlUitl;
import cn.com.do1.component.contact.student.vo.*;
import cn.com.do1.component.managesetting.managesetting.vo.OrgIndustryVersionVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejinjiao on 2016/11/18.
 */
public class StudentDAOImpl extends BaseDAOImpl implements IStudentDAO {
    /**
     * The constant seachUserIdsByPersonName.
     */
    private static final String seachUserIdsByPersonName = "SELECT user_id FROM tb_qy_user_info WHERE ORG_ID = :orgId " +
            " AND PERSON_NAME LIKE :parentName ";

    @Override
    public List<String> seachUserIdsByPersonName(Map<String, Object> map) throws Exception, BaseException {
        return searchByField(String.class, seachUserIdsByPersonName, map);
    }

    @Override
    public Pager searchStudent(Map searchValue, Pager pager) throws Exception, BaseException {
        String[] sql = StudentSqlUitl.getSearchStudentSql(searchValue);
        return pageSearchByField(StudentInfoVO.class, sql[1], sql[0], searchValue, pager);
    }

    /**
     * The constant findSchoolClassByOrgId.
     */
    private static final String findSchoolClassByOrgId = "SELECT e.department_id,e.class_full_name,e.grade,e.class_name " +
            " FROM tb_department_info_edu e,tb_department_info d WHERE d.id = e.department_id and e.org_id = :orgId " +
            " and d.attribute = 1 and e.department_id in (:classIds) ORDER BY e.grade DESC, e.class_name ASC ";

    @Override
    public List<TbDepartmentInfoEduPO> searchSchoolClass(Map<String, Object> map) throws Exception, BaseException {
        return searchByField(TbDepartmentInfoEduPO.class, findSchoolClassByOrgId, map);
    }

    /**
     * The constant judgementRepeat_sql.
     */
    private static final String judgementRepeat_sql = "SELECT id FROM tb_qy_student_info WHERE org_id = :orgId " +
            " and person_name = :personName AND class_id = :classId AND  register_phone = :registerPhone ";

    @Override
    public boolean judgementRepeat(String personName, String classId, String registerPhone, String orgId, String studentId) throws Exception, BaseException {
        this.preparedSql(judgementRepeat_sql);
        this.setPreValue("orgId", orgId);
        this.setPreValue("personName", personName);
        this.setPreValue("classId", classId);
        this.setPreValue("registerPhone", registerPhone);
        List<String> list = getList(String.class);
        if (list.size() > 0) {
            if (list.size() == 1 && studentId != null && studentId.equals(list.get(0))) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * The constant findClassList_sql.
     */
    private static final String findClassList_sql = "select t.department_name as node_name,t.id as node_id," +
            " t.parent_depart as parent_id,t.dept_full_name as node_title,t.permission,attribute " +
            " from TB_DEPARTMENT_INFO t where t.org_id =:orgId and attribute= '1' and t.id = :nodeId " +
            " and t.id in (:classIds) order by t.show_order ";

    @Override
    public List<TbQyOrganizeInfo> findClassList(Map<String, Object> map) throws Exception, BaseException {
        return searchByField(TbQyOrganizeInfo.class, findClassList_sql, map);
    }

    /**
     * The constant findSchoolStudent_sql.
     */
    private static final String findSchoolStudent_sql = "SELECT id,id as user_id,person_name FROM tb_qy_student_info " +
            " WHERE org_id = :orgId AND class_id = :deptId and person_name like :personName ORDER BY create_time DESC ";
    /**
     * The constant findSchoolStudent_countsql.
     */
    private static final String findSchoolStudent_countsql = "SELECT COUNT(1) FROM tb_qy_student_info " +
            " WHERE org_id = :orgId AND class_id = :deptId and person_name like :personName ";

    @Override
    public Pager findSchoolStudent(Map searchValue, Pager pager) throws Exception, BaseException {
        return pageSearchByField(SelectStudentVO.class, findSchoolStudent_countsql, findSchoolStudent_sql, searchValue, pager);
    }

    /**
     * The constant seachEduDepartByDepartIds.
     */
    private static final String seachEduDepartByDepartIds = "SELECT * FROM tb_department_info_edu " +
            " WHERE department_id in (:departIds) ";

    @Override
    public List<TbDepartmentInfoEduPO> seachEduDepartByDepartIds(List<String> departIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("departIds", departIds);
        return searchByField(TbDepartmentInfoEduPO.class, seachEduDepartByDepartIds, map);
    }

    /**
     * The constant getExistStudents_sql.
     */
    private static final String getExistStudents_sql = "SELECT id,person_name,class_id,register_phone " +
            " FROM tb_qy_student_info WHERE person_name = :personName AND class_id = :classId AND register_phone = :registerPhone " +
            " and org_id = :orgId ";

    @Override
    public List<TbQyStudentInfoVO> getExistStudents(Map<String, Object> map) throws Exception, BaseException {
        return searchByField(TbQyStudentInfoVO.class, getExistStudents_sql, map);
    }

    /**
     * The constant delParentRefByIds.
     */
    private static final String delParentRefByIds = "DELETE FROM tb_qy_user_student_ref WHERE org_id =:orgId and student_id in(:ids)";

    @Override
    public void delParentRefByIds(String[] ids, String orgId) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("ids", ids);
        map.put("orgId", orgId);
        updateByField(delParentRefByIds, map, true);
    }

    /**
     * The constant findUserStudentRef_sql.
     */
    private static final String findUserStudentRef_sql = "SELECT * from tb_qy_user_student_ref where student_id = :userId";

    @Override
    public List<TbQyUserStudentRefPO> findUserStudentRef(String userId) throws Exception, BaseException {
        this.preparedSql(findUserStudentRef_sql);
        this.setPreValue("userId", userId);
        return getList(TbQyUserStudentRefPO.class);
    }

    /**
     * The constant getParentsByStuIds_sql.
     */
    private static final String getParentsByStuIds_sql = "SELECT r.student_id,r.relation, u.USER_ID,u.PERSON_NAME,u.MOBILE," +
            " u.head_pic  FROM tb_qy_user_info u ,tb_qy_user_student_ref r WHERE r.user_id = u.USER_ID AND r.student_id in(:stuIds) " +
            " ORDER BY r.sort ";

    @Override
    public List<UserStudentRefVO> getParentsByStuIds(List<String> stuIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("stuIds", stuIds);
        return searchByField(UserStudentRefVO.class, getParentsByStuIds_sql, map);
    }

    /**
     * The constant countDepartByOrgId.
     */
    private static final String countDepartByOrgId = "SELECT COUNT(1) FROM tb_department_info WHERE org_id = :orgId";

    @Override
    public int countDepartByOrgId(String orgId) throws Exception, BaseException {
        this.preparedSql(countDepartByOrgId);
        this.setPreValue("orgId", orgId);
        return executeCount();
    }

    /**
     * The constant countstudentByOrgId.
     */
    private static final String countstudentByOrgId = "SELECT COUNT(1) FROM tb_qy_student_info WHERE org_id = :orgId";

    @Override
    public int countstudentByOrgId(String orgId) throws Exception, BaseException {
        this.preparedSql(countstudentByOrgId);
        this.setPreValue("orgId", orgId);
        return executeCount();
    }

    /**
     * The constant countUserByOrgId.
     */
    private static final String countUserByOrgId = "SELECT COUNT(1) FROM tb_qy_user_info WHERE org_id = :orgId and attribute = :attribute";

    @Override
    public int countUserByOrgId(String orgId, String attribute) throws Exception, BaseException {
        this.preparedSql(countUserByOrgId);
        this.setPreValue("orgId", orgId);
        this.setPreValue("attribute", attribute);
        return executeCount();
    }

    /**
     * The constant findUserStuRefByParentId.
     */
    private static final String findUserStuRefByParentId = "SELECT * from tb_qy_user_student_ref where user_id = :userId";

    @Override
    public List<TbQyUserStudentRefPO> findUserStuRefByParentId(String userId) throws Exception, BaseException {
        this.preparedSql(findUserStuRefByParentId);
        this.setPreValue("userId", userId);
        return getList(TbQyUserStudentRefPO.class);
    }

    /**
     * The constant getStuListByParentId.
     */
    private static final String getStuListByParentId = "SELECT r.student_id,r.relation,s.person_name,e.class_full_name," +
            " r.student_id as user_id,s.class_id,r.sort FROM tb_qy_user_student_ref r,tb_qy_student_info s,tb_department_info_edu e " +
            " WHERE r.student_id = s.id AND s.class_id = e.department_id AND r.user_id = :userId and s.org_id = :orgId " +
            " ORDER BY r.sort,r.relation ";

    @Override
    public List<UserStudentRefVO> getStuListByParentId(String userId, String orgId) throws Exception, BaseException {
        this.preparedSql(getStuListByParentId);
        this.setPreValue("userId", userId);
        this.setPreValue("orgId", orgId);
        return getList(UserStudentRefVO.class);
    }

    /**
     * The constant findParentsByStuId.
     */
    private static final String findParentsByStuId = "SELECT r.student_id,r.relation, u.USER_ID,u.PERSON_NAME,u.MOBILE,r.sort," +
            " u.head_pic,u.position,u.wx_user_id FROM tb_qy_user_info u ,tb_qy_user_student_ref r WHERE r.user_id = u.USER_ID " +
            " AND r.student_id = :stuId ORDER BY r.sort,r.relation ";

    @Override
    public List<UserStudentRefVO> findParentsByStuId(String studentId) throws Exception, BaseException {
        this.preparedSql(findParentsByStuId);
        this.setPreValue("stuId", studentId);
        return getList(UserStudentRefVO.class);
    }

    /**
     * The constant getStudentDetail.
     */
    private static final String getStudentDetail = "SELECT s.*,d.class_full_name FROM tb_qy_student_info s," +
            " tb_department_info_edu d WHERE s.class_id = d.department_id AND s.id = :studentId";

    @Override
    public TbQyStudentInfoVO getStudentDetail(String studentId) throws Exception, BaseException {
        this.preparedSql(getStudentDetail);
        this.setPreValue("studentId", studentId);
        return executeQuery(TbQyStudentInfoVO.class);
    }

    /**
     * The constant findStudentByNames_sql.
     */
    private static final String findStudentByNames_sql = "SELECT id,person_name,register_phone,class_id FROM tb_qy_student_info " +
            " WHERE org_id =:orgId AND person_name in(:stuNames) ";

    @Override
    public List<TbQyStudentInfoVO> findStudentByNames(List<String> stuNames, String orgId) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("stuNames", stuNames);
        map.put("orgId", orgId);
        return searchByField(TbQyStudentInfoVO.class, findStudentByNames_sql, map);
    }

    /**
     * The constant findUsersByWxUserIds_sql.
     */
    private static final String findUsersByWxUserIds_sql = "SELECT USER_ID,wx_user_id FROM tb_qy_user_info " +
            " WHERE ORG_ID = :orgId AND wx_user_id in(:wxUserId)";

    @Override
    public List<TbQyUserInfoVO> findUsersByWxUserIds(String orgId, List<String> wxUserId) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("wxUserId", wxUserId);
        map.put("orgId", orgId);
        return searchByField(TbQyUserInfoVO.class, findUsersByWxUserIds_sql, map);
    }
    /**
     * The constant getDepartmentEduPO_sql.
     */
    private static final String getDepartmentEduPO_sql = "SELECT * FROM tb_department_info_edu WHERE department_id = :id";

    @Override
    public TbDepartmentInfoEduPO getDepartmentEduPO(String id) throws Exception, BaseException {
        this.preparedSql(getDepartmentEduPO_sql);
        this.setPreValue("id", id);
        return executeQuery(TbDepartmentInfoEduPO.class);
    }

    /**
     * The constant searchClassByUserId.
     */
    private static final String searchClassByUserId = "SELECT e.department_id,e.class_full_name FROM tb_qy_user_department_ref r, " +
            " tb_department_info_edu e WHERE r.department_id = e.department_id AND r.user_id = :userId AND r.org_id = :orgId " +
            " ORDER BY e.grade DESC, e.class_name ASC ";

    @Override
    public List<SchoolClassVO> searchClassByUserId(String userId, String orgId) throws Exception, BaseException {
        this.preparedSql(searchClassByUserId);
        this.setPreValue("userId", userId);
        this.setPreValue("orgId", orgId);
        return getList(SchoolClassVO.class);
    }

    /**
     * The constant countStudentsByClassIds.
     */
    private static final String countStudentsByClassIds = "SELECT count(1) as student_num,class_id as department_id " +
            " FROM tb_qy_student_info WHERE class_id in(:classIds) GROUP BY class_id";

    @Override
    public List<SchoolClassVO> countStudentsByClassIds(List<String> classIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("classIds", classIds);
        return searchByField(SchoolClassVO.class, countStudentsByClassIds, map);
    }

    /**
     * The constant findTeachersByClassId.
     */
    private static final String findTeachersByClassId = "SELECT u.PERSON_NAME,u.head_pic,u.POSITION,u.weixin_num,u.MOBILE," +
            " u.SHOR_MOBILE,u.phone,u.wx_user_id FROM tb_qy_user_info u,tb_qy_user_department_ref r WHERE u.USER_ID = r.user_id " +
            " AND u.attribute = '2' AND r.department_id = :classId ORDER BY u.PINYIN ";

    @Override
    public List<TbQyUserInfoVO> findTeachersByClassId(String classId) throws Exception, BaseException {
        this.preparedSql(findTeachersByClassId);
        this.setPreValue("classId", classId);
        return getList(TbQyUserInfoVO.class);
    }

    /**
     * The constant findTeachersByClassIds.
     */
    private static final String findTeachersByClassIds = "SELECT u.PERSON_NAME,u.head_pic,u.POSITION,u.weixin_num,u.MOBILE," +
            " r.department_id as class_id FROM tb_qy_user_info u,tb_qy_user_department_ref r WHERE u.USER_ID = r.user_id " +
            " AND u.attribute = '2' AND r.department_id in( :classIds ) ORDER BY u.PINYIN ";

    @Override
    public List<UserStudentRefVO> findTeachersByClassIds(String[] classIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("classIds", classIds);
        return searchByField(UserStudentRefVO.class, findTeachersByClassIds, map);
    }

    /**
     * The constant judgementRation_sql.
     */
    private static final String judgementRation_sql = "SELECT COUNT(1) FROM tb_qy_user_student_ref " +
            " WHERE user_id = :userId and student_id = :studentId ";

    @Override
    public boolean judgementRation(String userId, String studentId) throws Exception, BaseException {
        this.preparedSql(judgementRation_sql);
        this.setPreValue("userId", userId);
        this.setPreValue("studentId", studentId);
        if (executeCount() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<ExportStudentVO> exportStudentList(Map map) throws Exception, BaseException {
        return searchByField(ExportStudentVO.class, StudentSqlUitl.getExportStudentSql(map), map);
    }

    /**
     * The constant exportParentList_sql.
     */
    private static final String exportParentList_sql = "SELECT r.student_id,r.relation,u.wx_user_id " +
            " FROM tb_qy_user_info u,tb_qy_user_student_ref r WHERE u.USER_ID = r.user_id and r.student_id in (:stuIds)";

    @Override
    public List<ExportParentVO> exportParentList(List<String> stuIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("stuIds", stuIds);
        return searchByField(ExportParentVO.class, exportParentList_sql, map);
    }

    /**
     * The constant getDepartEduByClassName.
     */
    private static final String getDepartEduByClassName = "SELECT * FROM tb_department_info_edu WHERE grade = :grade " +
            " AND class_name = :className AND org_id = :orgId ";

    @Override
    public TbDepartmentInfoEduPO getDepartEduByClassName(int grade, int className, String orgId) throws Exception, BaseException {
        this.preparedSql(getDepartEduByClassName);
        this.setPreValue("grade", grade);
        this.setPreValue("className", className);
        this.setPreValue("orgId", orgId);
        return executeQuery(TbDepartmentInfoEduPO.class);
    }

    /**
     * The constant modifyStudentClassId.
     */
    private static final String modifyStudentClassId = "UPDATE tb_qy_student_info set class_id = :newDepartId WHERE class_id = :oldDepartId";

    @Override
    public void modifyStudentClassId(String oldDepartId, String newDepartId) throws Exception, BaseException {
        this.preparedSql(modifyStudentClassId);
        this.setPreValue("oldDepartId", oldDepartId);
        this.setPreValue("newDepartId", newDepartId);
        this.executeUpdate();
    }

    /**
     * The constant getDepartByParentIds.
     */
    private static final String getDepartByParentIds = "SELECT t.department_name as node_name,t.id as node_id," +
            " t.parent_depart as parent_id,t.dept_full_name as node_title,t.permission,attribute FROM tb_department_info t " +
            " WHERE t.attribute = 2 and t.id in(:parentIds) ";

    @Override
    public List<TbQyOrganizeInfo> getDepartByParentIds(String[] parentIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("parentIds", parentIds);
        return searchByField(TbQyOrganizeInfo.class, getDepartByParentIds, map);
    }

    /**
     * The constant findEducationOrgs.
     */
    private static final String findEducationOrgs = "SELECT e.org_id,e.industry_ver,e.sch_year_system,s.system_name," +
            " s.school_year FROM tb_dqdp_organization_edu e,st_school_system_info s WHERE e.sch_year_system = s.id ";

    @Override
    public List<OrgIndustryVersionVO> findEducationOrgs() throws Exception, BaseException {
        this.preparedSql(findEducationOrgs);
        return getList(OrgIndustryVersionVO.class);
    }

    /**
     * The constant findAllClassByOrgId.
     */
    private static final String findAllClassByOrgId = "SELECT * FROM tb_department_info_edu WHERE org_id = :orgId";

    @Override
    public List<TbDepartmentInfoEduPO> findAllClassByOrgId(String orgId) throws Exception, BaseException {
        this.preparedSql(findAllClassByOrgId);
        this.setPreValue("orgId", orgId);
        return getList(TbDepartmentInfoEduPO.class);
    }

    /**
     * The constant updateHasParent.
     */
    private static final String updateHasParent = "UPDATE tb_qy_student_info SET has_parent = :type WHERE org_id = :orgId and id in(:studentIds) ";

    @Override
    public void updateHasParent(String[] studentIds, String orgId, int type) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("studentIds", studentIds);
        map.put("orgId", orgId);
        map.put("type", type);
        updateByField(updateHasParent, map, false);
    }

    /**
     * The constant searchAllStudent.
     */
    private static final String searchAllStudent = "SELECT id,person_name FROM tb_qy_student_info";

    @Override
    public List<TbQyStudentInfoPO> searchAllStudent() throws Exception, BaseException {
        this.preparedSql(searchAllStudent);
        return getList(TbQyStudentInfoPO.class);
    }

    /**
     * The constant getClassIdsByDepartName.
     */
    private static final String getClassIdsByDepartName = "SELECT id FROM tb_department_info WHERE org_id = :orgId " +
            " and dept_full_name LIKE :deptName and attribute = 1  ";

    @Override
    public List<String> getClassIdsByDepartName(String orgId, String deptName) throws Exception, BaseException {
        this.preparedSql(getClassIdsByDepartName);
        this.setPreValue("orgId", orgId);
        this.setPreValue("deptName", deptName);
        return getList(String.class);
    }

    /**
     * The constant findStudentsByParents.
     */
    private static final String findStudentsByParents = "SELECT * FROM tb_qy_user_student_ref WHERE user_id in (:userIds)";

    @Override
    public List<TbQyUserStudentRefPO> findStudentsByParents(String[] userIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("userIds", userIds);
        return searchByField(TbQyUserStudentRefPO.class, findStudentsByParents, map);
    }

    /**
     * The constant delStudentRefByParents.
     */
    private static final String delStudentRefByParents = "DELETE FROM tb_qy_user_student_ref where user_id in (:userIds)";

    @Override
    public void delStudentRefByParents(String[] userIds) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("userIds", userIds);
        updateByField(delStudentRefByParents, map, false);
    }

    /**
     * The constant updateStudentSyn.
     */
    private static final String updateStudentSyn = "UPDATE tb_qy_student_info SET is_syn = :type WHERE org_id = :orgId and id in(:studentIds) ";

    @Override
    public void updateStudentSyn(Map<String, Object> map) throws Exception, BaseException {
        updateByField(updateStudentSyn, map, false);
    }

    public List<ChildrenVO> findChildren(Map searchMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception {
        String findChildren_sql = "SELECT o.id, o.person_name, o.class_id, f.user_id, f.relation " +
                "FROM tb_qy_student_info o, tb_qy_user_student_ref f, tb_department_info d " +
                "WHERE o.id = f.student_id " +
                "AND o.org_id = :orgId " +
                "AND o.class_id = d.id " +
                "AND f.user_id IN (:userIds) ";
        if (depts != null && depts.size() > 0) {
            SeachSqlVO seachVO = new SeachSqlVO();
            seachVO.setDepts(depts);
            seachVO.setSearchMap(searchMap);
            SeachSqlVO retuenSeachVO = DepartmentUtil.getdeptSql(seachVO);
            searchMap = retuenSeachVO.getSearchMap();
            String depqrtSQL = retuenSeachVO.getReturnSql();
            findChildren_sql += " " + depqrtSQL;
        }
        findChildren_sql += "GROUP BY o.id";
        return searchByField(ChildrenVO.class, findChildren_sql, searchMap);
    }

    /**
     * The constant getChildrenByUserId_sql.
     */
    private static final String getChildrenByUserId_sql = " select i.id, i.person_name, i.org_id,e.class_full_name " +
            " from tb_qy_student_info i, tb_qy_user_student_ref r, tb_department_info_edu e " +
            "where " +
            "i.id = r.student_id and " +
            "r.user_id = :userId and " +
            "i.class_id = e.department_id " +
            "order by i.pinyin";

    public List<ChildrenVO> getChildrenByUserId(String userId) throws BaseException, Exception {
        this.preparedSql(getChildrenByUserId_sql);
        this.setPreValue("userId", userId);
        return getList(ChildrenVO.class);
    }

    private static final String updateParentType_sql = "UPDATE tb_qy_user_info SET attribute = :type " +
            " WHERE (attribute = 0 or attribute is null) and ORG_ID = :orgId and  USER_ID in(:userIds) ";

    @Override
    public void updateParentType(List<String> userIds, String orgId, int type) throws BaseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(userIds.size() + 2);
        map.put("userIds", userIds);
        map.put("orgId", orgId);
        map.put("type", type);
        updateByField(updateParentType_sql, map, false);
    }
    private static final String updateStudentHasTeacher_sql = "UPDATE tb_qy_student_info SET has_teacher = :type " +
            " WHERE org_id = :orgId AND id in (:stuIds) ";
    @Override
    public void updateStudentHasTeacher(List<String> stuIds, String orgId, int type) throws BaseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(stuIds.size() + 2);
        map.put("stuIds", stuIds);
        map.put("orgId", orgId);
        map.put("type", type);
        updateByField(updateStudentHasTeacher_sql, map, false);
    }
    private static final String countTeachersByUserIds_sql = "SELECT COUNT(1) FROM tb_qy_user_info WHERE attribute = 2 " +
            " and ORG_ID = :orgId and USER_ID in (:userIds) ";
    @Override
    public int countTeachersByUserIds(List<String> userIds, String orgId) throws BaseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(userIds.size() + 2);
        map.put("userIds", userIds);
        map.put("orgId", orgId);
        return countByField(countTeachersByUserIds_sql, map);
    }
    private static final String updateParentHasChildren_sql = "UPDATE tb_qy_user_info u set u.has_child = 1 " +
            " WHERE u.attribute =2 AND u.ORG_ID = :orgId AND u.USER_ID in (:userIds) ";
    @Override
    public void updateParentHasChildren(List<String> userIds, String orgId) throws BaseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(userIds.size() + 1);
        map.put("userIds", userIds);
        map.put("orgId", orgId);
        updateByField(updateParentHasChildren_sql, map, false);
    }
}
