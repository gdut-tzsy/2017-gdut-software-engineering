package cn.com.do1.component.convenience.declare.model;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;
import cn.com.do1.common.annotation.bean.PageView;
import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.Date;

import oracle.sql.TIMESTAMP;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${tempDataMap.user}
 */
public class TbYsjdDeclareCommentPO implements IBaseDBVO {

    private String id;


    private String declareId;


    private String content;


    private String createPerson;


    private Date createTime;


    private String commentStatus;


    private String personName;


    private String wxUserId;


    private String headPic;


    private String departmentName;


    private String departmentId;


    private String type;


    private String deviceNum;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }


    public String getDeclareId() {
        return declareId;
    }

    public void setDeclareId(String declareId) {
        this.declareId = declareId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }


    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreatePerson() {
        return this.createPerson;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = ConvertUtil.cvStUtildate(createTime);
    }

    public Date getCreateTime() {
        return this.createTime;
    }


    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getCommentStatus() {
        return this.commentStatus;
    }


    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return this.personName;
    }


    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getWxUserId() {
        return this.wxUserId;
    }


    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return this.headPic;
    }


    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }


    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentId() {
        return this.departmentId;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }


    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getDeviceNum() {
        return this.deviceNum;
    }


    /**
     * 获取数据库中对应的表名
     *
     * @return
     */
    public String _getTableName() {
        return "tb_ysjd_declare_comment";
    }

    /**
     * 获取对应表的主键字段名称
     *
     * @return
     */
    public String _getPKColumnName() {
        return "id";
    }

    /**
     * 获取主键值
     *
     * @return
     */
    public String _getPKValue() {
        return String.valueOf(id);
    }

    /**
     * 设置主键的值
     *
     * @return
     */
    public void _setPKValue(Object value) {
        this.id = (String) value;
    }
}
