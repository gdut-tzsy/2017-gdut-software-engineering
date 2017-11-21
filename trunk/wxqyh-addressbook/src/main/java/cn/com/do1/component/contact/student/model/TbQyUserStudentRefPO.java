package cn.com.do1.component.contact.student.model;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;

/**
 * Created by hejinjiao on 2016/11/18.
 */
public class TbQyUserStudentRefPO implements IBaseDBVO {
    /**
     * The Id.
     */
    private String id;
    /**
     * The User id.
     */
    private String userId;
    /**
     * The Student id.
     */
    private String studentId;
    /**
     * The Relation.
     */
    private Integer relation;
    /**
     * The Sort.
     */
    private Integer sort;
    /**
     * The Org id.
     */
    private String orgId;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets student id.
     *
     * @return the student id
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Sets student id.
     *
     * @param studentId the student id
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets relation.
     *
     * @return the relation
     */
    public Integer getRelation() {
        return relation;
    }

    /**
     * Sets relation.
     *
     * @param relation the relation
     */
    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    /**
     * Gets sort.
     *
     * @return the sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * Gets org id.
     *
     * @return the org id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * Sets org id.
     *
     * @param orgId the org id
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String _getTableName() {
        return "tb_qy_user_student_ref";
    }

    @Override
    public String _getPKColumnName() {
        return "id";
    }

    @Override
    public Object _getPKValue() {
        return String.valueOf(id);
    }

    @Override
    public void _setPKValue(Object value) {
        this.id = (String) value;
    }
}
