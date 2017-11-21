package cn.com.do1.component.contact.student.vo;

import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.Date;

/**
 * Created by hejinjiao on 2016/11/21.
 */
public class TbQyStudentInfoVO {
    /**
     * The Id.
     */
    private String id;
    /**
     * The Person name.
     */
    private String personName;
    /**
     * The Class id.
     */
    private String classId;
    /**
     * The Class name.
     */
    private String classFullName;
    /**
     * The Register phone.
     */
    private String registerPhone;
    /**
     * The Has teacher.
     */
    private Integer hasTeacher;
    /**
     * The Sex.
     */
    private String sex;
    @DictDesc(refField = "sex", typeName = "qwUserSex")
    private String sexDesc;
    /**
     * The Identity.
     */
    private String identity;
    /**
     * The Birthday.
     */
    @FormatMask(type = "date", value = "yyyy-MM")
    private String birthday;
    /**
     * The Mobile.
     */
    private String mobile;
    /**
     * The Weixin num.
     */
    private String weixinNum;
    /**
     * The Email.
     */
    private String email;
    /**
     * The Mark.
     */
    private String mark;
    /**
     * The Org id.
     */
    private String orgId;
    /**
     * The Creator.
     */
    private String creator;
    /**
     * The Create time.
     */
    private java.util.Date createTime;

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
     * Gets register phone.
     *
     * @return the register phone
     */
    public String getRegisterPhone() {
        return registerPhone;
    }

    /**
     * Sets register phone.
     *
     * @param registerPhone the register phone
     */
    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    /**
     * Gets has teacher.
     *
     * @return the has teacher
     */
    public Integer getHasTeacher() {
        return hasTeacher;
    }

    /**
     * Sets has teacher.
     *
     * @param hasTeacher the has teacher
     */
    public void setHasTeacher(Integer hasTeacher) {
        this.hasTeacher = hasTeacher;
    }

    /**
     * Gets sex.
     *
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets sex.
     *
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexDesc() {
        return sexDesc;
    }

    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
    }

    /**
     * Gets identity.
     *
     * @return the identity
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Sets identity.
     *
     * @param identity the identity
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
     * Gets weixin num.
     *
     * @return the weixin num
     */
    public String getWeixinNum() {
        return weixinNum;
    }

    /**
     * Sets weixin num.
     *
     * @param weixinNum the weixin num
     */
    public void setWeixinNum(String weixinNum) {
        this.weixinNum = weixinNum;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets mark.
     *
     * @return the mark
     */
    public String getMark() {
        return mark;
    }

    /**
     * Sets mark.
     *
     * @param mark the mark
     */
    public void setMark(String mark) {
        this.mark = mark;
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
     * Gets creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets creator.
     *
     * @param creator the creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
