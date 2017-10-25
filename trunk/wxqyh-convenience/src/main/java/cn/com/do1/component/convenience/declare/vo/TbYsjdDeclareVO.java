package cn.com.do1.component.convenience.declare.vo;

import cn.com.do1.common.annotation.bean.FormatMask;
import cn.com.do1.common.framebase.dqdp.IBaseVO;

import java.util.List;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${tempDataMap.user}
 */
public class TbYsjdDeclareVO{

    private String id;


    private String houseNo;


    private String userName;


    private String sex;


    private String cardNo;


    private String address;


    @FormatMask(type = "date", value = "yyyy-MM-dd")
    private String checkinTime;


    private String workUnit;


    private String createUser;


    @FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
    private String createTime;


    private String status;


    private String headPic;


    private String createName;


    private String wxUserId;


    private String gridOperator;

    private String gridOperatorName;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }


    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHouseNo() {
        return this.houseNo;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return this.sex;
    }


    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return this.cardNo;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }


    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String  getCheckinTime() {
        return this.checkinTime;
    }


    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkUnit() {
        return this.workUnit;
    }


    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUser() {
        return this.createUser;
    }


    public void setCreateTime(String  createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return this.headPic;
    }


    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateName() {
        return this.createName;
    }


    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getWxUserId() {
        return this.wxUserId;
    }


    public void setGridOperator(String gridOperator) {
        this.gridOperator = gridOperator;
    }

    public String getGridOperator() {
        return this.gridOperator;
    }

    public String getGridOperatorName() {
        return gridOperatorName;
    }

    public void setGridOperatorName(String gridOperatorName) {
        this.gridOperatorName = gridOperatorName;
    }
}
