package cn.com.do1.component.contact.student.vo;

import cn.com.do1.component.addressbook.contact.vo.ImportVO;
import cn.com.do1.component.common.annotation.TitleAnnotation;

import java.util.Map;

/**
 * Created by hejinjiao on 2016/11/30.
 */
public class ImportErrorStudentVO {
    /**
     * The Person name.
     */
    @TitleAnnotation(titleName = "学生姓名")
    private String personName;
    /**
     * The Class name.
     */
    @TitleAnnotation(titleName = "班级")
    private String className;
    /**
     * The Register phone.
     */
    @TitleAnnotation(titleName = "入学登记手机号")
    private String registerPhone;
    /**
     * The Has teacher.
     */
    @TitleAnnotation(titleName = "本校教师子女")
    private String hasTeacher;
    /**
     * The Sex desc.
     */
    @TitleAnnotation(titleName = "性别")
    private String sexDesc;
    /**
     * The Identity.
     */
    @TitleAnnotation(titleName = "身份证号码")
    private String identity;
    /**
     * The Birthday.
     */
    @TitleAnnotation(titleName = "出生年月")
    private String birthday;
    /**
     * The Weixin num.
     */
    @TitleAnnotation(titleName = "学生微信号")
    private String weixinNum;
    /**
     * The Mobile.
     */
    @TitleAnnotation(titleName = "学生手机号")
    private String mobile;
    /**
     * The Email.
     */
    @TitleAnnotation(titleName = "学生邮箱")
    private String email;
    /**
     * The Parents.
     */
    @TitleAnnotation(titleName = "家长/监护人")
    private String parents;
    /**
     * The Mark.
     */
    @TitleAnnotation(titleName = "备注")
    private String mark;
    /**
     * The Err msg.
     */
    @TitleAnnotation(titleName = "错误提示")
    private String errMsg;


    @TitleAnnotation(titleName = "自定义")
    private Map<String, String> itemVOs;

    private ImportVO parentVO;

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
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
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
    public String getHasTeacher() {
        return hasTeacher;
    }

    /**
     * Sets has teacher.
     *
     * @param hasTeacher the has teacher
     */
    public void setHasTeacher(String hasTeacher) {
        this.hasTeacher = hasTeacher;
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
     * Gets parents.
     *
     * @return the parents
     */
    public String getParents() {
        return parents;
    }

    /**
     * Sets parents.
     *
     * @param parents the parents
     */
    public void setParents(String parents) {
        this.parents = parents;
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
     * Gets err msg.
     *
     * @return the err msg
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * Sets err msg.
     *
     * @param errMsg the err msg
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Map<String, String> getItemVOs() {
        return itemVOs;
    }

    public void setItemVOs(Map<String, String> itemVOs) {
        this.itemVOs = itemVOs;
    }

    public ImportVO getParentVO() {
        return parentVO;
    }

    public void setParentVO(ImportVO parentVO) {
        this.parentVO = parentVO;
    }
}
