package cn.com.do1.component.contact.student.vo;

import cn.com.do1.common.annotation.bean.DictDesc;

import java.util.List;

/**
 * Created by hejinjiao on 2016/11/19.
 */
public class StudentInfoVO {
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
     * The Class full name.
     */
    private String classFullName;
    /**
     * The Sex.
     */
    private String sex;
    /**
     * The Sex desc.
     */
    @DictDesc(refField = "sex", typeName = "qwUserSex")
    private String sexDesc;
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
     * The Is syn.
     */
    private Integer isSyn;
    /**
     * The Register phone.
     */
    private String registerPhone;

    /**
     * The Parentlist.
     */
    private List<UserStudentRefVO> parentlist;

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

    /**
     * Gets sex desc.
     *
     * @return the sex desc
     */
    public String getSexDesc() {
        return sexDesc;
    }

    /**
     * Sets sex desc.
     *
     * @param sexDesc the sex desc
     */
    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
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
     * Gets is syn.
     *
     * @return the is syn
     */
    public Integer getIsSyn() {
        return isSyn;
    }

    /**
     * Sets is syn.
     *
     * @param isSyn the is syn
     */
    public void setIsSyn(Integer isSyn) {
        this.isSyn = isSyn;
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
     * Gets parentlist.
     *
     * @return the parentlist
     */
    public List<UserStudentRefVO> getParentlist() {
        return parentlist;
    }

    /**
     * Sets parentlist.
     *
     * @param parentlist the parentlist
     */
    public void setParentlist(List<UserStudentRefVO> parentlist) {
        this.parentlist = parentlist;
    }
}
