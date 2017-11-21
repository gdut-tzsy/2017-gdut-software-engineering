package cn.com.do1.component.contact.student.vo;

import cn.com.do1.common.annotation.bean.DictDesc;

/**
 * Created by hejinjiao on 2016/11/23.
 */
public class UserStudentRefVO {
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
    private String relation;
    /**
     * The Relation desc.
     */
    @DictDesc(refField = "relation", typeName = "relationType")
    private String relationDesc;
    /**
     * The Org id.
     */
    private String orgId;
    /**
     * The Person name.
     */
    private String personName;
    /**
     * The Class full name.
     */
    private String classFullName;
    /**
     * The Mobile.
     */
    private String mobile;
    /**
     * The Head pic.
     */
    private String headPic;
    /**
     * The Sort.
     */
    private String sort;
    /**
     * The Wx user id.
     */
    private String wxUserId;
    /**
     * The Class id.
     */
    private String classId;
    /**
     * The Teacher mobile.
     */
    private String teacherMobile;
    /**
     * The Position.
     */
    private String position;

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

    /**
     * Gets person name.
     *
     * @return the person name
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Sets person name.
     *
     * @param personName the person name
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * Gets class full name.
     *
     * @return the class full name
     */
    public String getClassFullName() {
        return classFullName;
    }

    /**
     * Sets class full name.
     *
     * @param classFullName the class full name
     */
    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    /**
     * Gets mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile.
     *
     * @param mobile the mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets head pic.
     *
     * @return the head pic
     */
    public String getHeadPic() {
        return headPic;
    }

    /**
     * Sets head pic.
     *
     * @param headPic the head pic
     */
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    /**
     * Gets sort.
     *
     * @return the sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     */
    public void setSort(String sort) {
        this.sort = sort;
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
     * Gets class id.
     *
     * @return the class id
     */
    public String getClassId() {
        return classId;
    }

    /**
     * Sets class id.
     *
     * @param classId the class id
     */
    public void setClassId(String classId) {
        this.classId = classId;
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
     * Gets teacher mobile.
     *
     * @return the teacher mobile
     */
    public String getTeacherMobile() {
        return teacherMobile;
    }

    /**
     * Sets teacher mobile.
     *
     * @param teacherMobile the teacher mobile
     */
    public void setTeacherMobile(String teacherMobile) {
        this.teacherMobile = teacherMobile;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(String position) {
        this.position = position;
    }
}
