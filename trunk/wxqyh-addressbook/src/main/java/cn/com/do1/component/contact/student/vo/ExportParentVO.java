package cn.com.do1.component.contact.student.vo;

import cn.com.do1.common.annotation.bean.DictDesc;

/**
 * Created by hejinjiao on 2016/12/12.
 */
public class ExportParentVO {
    /**
     * The Student id.
     */
    private String studentId;
    /**
     * The User id.
     */
    private String userId;
    /**
     * The Wx user id.
     */
    private String wxUserId;
    /**
     * The Relation.
     */
    private String relation;
    /**
     * The Relation desc.
     */
    @DictDesc(refField = "relation", typeName = "relationType")
    private String relationDesc;

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
     * Gets wx user id.
     *
     * @return the wx user id
     */
    public String getWxUserId() {
        return wxUserId;
    }

    /**
     * Sets wx user id.
     *
     * @param wxUserId the wx user id
     */
    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    /**
     * Gets relation.
     *
     * @return the relation
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Sets relation.
     *
     * @param relation the relation
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * Gets relation desc.
     *
     * @return the relation desc
     */
    public String getRelationDesc() {
        return relationDesc;
    }

    /**
     * Sets relation desc.
     *
     * @param relationDesc the relation desc
     */
    public void setRelationDesc(String relationDesc) {
        this.relationDesc = relationDesc;
    }
}
