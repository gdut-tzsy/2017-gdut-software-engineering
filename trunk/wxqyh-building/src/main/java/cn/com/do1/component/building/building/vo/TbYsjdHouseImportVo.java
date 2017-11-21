package cn.com.do1.component.building.building.vo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.PageView;
import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.component.common.annotation.TitleAnnotation;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: cuijianpeng
 */
/**
 * <p>ClassName: TbYsjdHouseImportVo</p>
 * <p>Description: 房屋导入vo</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月23日</p>
 */
public class TbYsjdHouseImportVo implements Serializable {
    /**
     * <p>Field houseNo: 房屋编号</p>
     */
    @TitleAnnotation(
            titleName = "*房屋编号"
    )
    private java.lang.String houseNo;
    
    /**
     * <p>Field houseAddress: 房屋地址</p>
     */
    @TitleAnnotation(
            titleName = "*房屋地址"
    )
    private java.lang.String houseAddress;

    /**
     * <p>Field banNo: 楼栋编号</p>
     */
    @TitleAnnotation(
            titleName = "*楼栋编号"
    )
    private java.lang.String banNo;

    /**
     * <p>Field banName: 楼栋名称</p>
     */
    @TitleAnnotation(
            titleName = "楼栋名称"
    )
    private java.lang.String banName;

    /**
     * <p>Field houseNumber: 房号</p>
     */
    @TitleAnnotation(
            titleName = "房号"
    )
    private java.lang.String houseNumber;
    
    /**
     * <p>Field propertyOwner: 产权人</p>
     */
    @TitleAnnotation(
            titleName = "产权人"
    )
    private java.lang.String propertyOwner;
    
    /**
     * <p>Field houseArea: 房屋面积</p>
     */
    @TitleAnnotation(
            titleName = "房屋面积"
    )
    private java.lang.String houseArea;

    /**
     * <p>Field purpose: 使用用途</p>
     */
    @TitleAnnotation(
            titleName = "使用用途"
    )
    private java.lang.String purpose;
    
    /**
     * <p>Field situation: 使用情况</p>
     */
    @TitleAnnotation(
            titleName = "使用情况"
    )
    private java.lang.String situation;

    /**
     * <p>Field structure: 房屋结构</p>
     */
    @TitleAnnotation(
            titleName = "房屋结构"
    )
    private java.lang.String structure;

    /**
     * <p>Field banAddress: 所属楼栋</p>
     */
    @TitleAnnotation(
            titleName = "所属楼栋"
    )
    private String banAddress;

    /**
     * <p>Field error: 错误提示</p>
     */
    @TitleAnnotation(
            titleName = "错误提示"
    )
    private java.lang.String error;
    
    public java.lang.String getHouseNo() {
        return this.houseNo;
    }

    public void setHouseNo(java.lang.String houseNo) {
        this.houseNo = houseNo;
    }

    public java.lang.String getHouseAddress() {
        return this.houseAddress;
    }

    public void setHouseAddress(java.lang.String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public java.lang.String getBanNo() {
        return this.banNo;
    }

    public void setBanNo(java.lang.String banNo) {
        this.banNo = banNo;
    }

    public java.lang.String getBanName() {
        return this.banName;
    }

    public void setBanName(java.lang.String banName) {
        this.banName = banName;
    }

    public java.lang.String getHouseNumber() {
        return this.houseNumber;
    }

    public void setHouseNumber(java.lang.String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public java.lang.String getPropertyOwner() {
        return this.propertyOwner;
    }

    public void setPropertyOwner(java.lang.String propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public java.lang.String getHouseArea() {
        return this.houseArea;
    }

    public void setHouseArea(java.lang.String houseArea) {
        this.houseArea = houseArea;
    }

    public java.lang.String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(java.lang.String purpose) {
        this.purpose = purpose;
    }

    public java.lang.String getSituation() {
        return this.situation;
    }

    public void setSituation(java.lang.String situation) {
        this.situation = situation;
    }

    public java.lang.String getStructure() {
        return this.structure;
    }

    public void setStructure(java.lang.String structure) {
        this.structure = structure;
    }

    public String getBanAddress() {
        return this.banAddress;
    }

    public void setBanAddress(String banAddress) {
        this.banAddress = banAddress;
    }

    public java.lang.String getError() {
        return this.error;
    }

    public void setError(java.lang.String error) {
        this.error = error;
    }

}
