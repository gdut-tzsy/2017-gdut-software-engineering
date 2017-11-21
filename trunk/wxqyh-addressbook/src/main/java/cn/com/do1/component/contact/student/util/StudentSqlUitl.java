package cn.com.do1.component.contact.student.util;

import java.util.Map;

/**
 * Created by hejinjiao on 2017/1/4.
 */
public class StudentSqlUitl {
    /**
     * 学生列表查询字段
     */
    private static final String student_select = "SELECT s.id,s.person_name,s.sex,s.mobile,s.weixin_num,s.email," +
            " s.is_syn,e.class_full_name,s.register_phone ";
    /**
     * 学生信息查询表
     */
    private static final String student_from = " FROM tb_qy_student_info s,tb_department_info_edu e ";
    /**
     * 查询条件
     */
    private static final String student_where = " WHERE s.class_id = e.department_id and s.org_id = :orgId ";
    /**
     * 搜索
     */
    private static final String student_search = "and (e.class_full_name like :title or s.person_name like :title " +
            " or s.register_phone like :title)";
    /**
     * 高级搜索
     */
    private static final String advanced_search = "and s.person_name like :personName and s.class_id = :classId " +
            " and s.register_phone like :mobile and s.has_teacher = :hasTeacher and s.birthday =:birthday " +
            " and s.sex = :sex and s.has_parent = :hasParent ";
    /**
     * 排序
     */
    private static final String student_order = " ORDER BY s.pinyin ";
    /**
     * 关联父母查询
     */
    private static final String parent_from = " ,tb_qy_user_student_ref r ";
    /**
     * 关联父母查询条件
     */
    private static final String parent_where = " and s.id = r.student_id and r.user_id in (:parentIds) ";
    /**
     * 学生信息导出查询字段
     */
    private static final String student_export = "SELECT s.id,s.class_id,s.person_name,s.sex,s.mobile,s.weixin_num,s.email," +
            " s.has_teacher,s.register_phone,s.identity,s.birthday,s.mark,e.class_full_name ";

    /**
     * 学生列表查询sql
     *
     * @param searchValue
     * @return
     */
    public static String[] getSearchStudentSql(Map searchValue) {
        String search_sql = assemblySQL(searchValue);
        String[] sql = new String[2];
        sql[0] = student_select + search_sql + student_order;
        sql[1] = "SELECT count(1) " + search_sql;
        return sql;
    }

    /**
     * 组装sql
     *
     * @param searchValue
     * @return
     */
    private static String assemblySQL(Map searchValue) {
        StringBuilder sql_table = new StringBuilder(student_from);
        StringBuilder sql_where = new StringBuilder(student_where);
        //家长id
        if (searchValue.containsKey("parentIds")) {
            sql_table.append(parent_from);
            sql_where.append(parent_where);
        }
        sql_where.append(advanced_search);
        if (searchValue.containsKey("classIds")) {
            sql_where.append(" and s.class_id in (:classIds) ");
        }
        if (searchValue.containsKey("title")) {
            sql_where.append(student_search);
        }
        return sql_table.toString() + sql_where.toString();
    }

    /**
     * 导出学生信息查询sql
     *
     * @param map
     * @return
     */
    public static String getExportStudentSql(Map map) {
        return student_export + assemblySQL(map) + student_order;
    }
}
