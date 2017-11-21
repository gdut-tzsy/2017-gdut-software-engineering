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
 * <p>ClassName: TbYsjdBanImportVo</p>
 * <p>Description: 楼栋导入vo</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月23日</p>
 */
public class TbYsjdBanImportVo implements Serializable {
    /**
     * <p>Field community: 所属社区</p>
     */
    @TitleAnnotation(
            titleName = "*社区"
    )
    private java.lang.String communityName;
    
    /**
     * <p>Field grid: 所属网格</p>
     */
    @TitleAnnotation(
            titleName = "*所属网格"
    )
    private java.lang.String gridName;
    
    /**
     * <p>Field architectureNo: 建筑编码</p>
     */
    @TitleAnnotation(
            titleName = "*建筑物编码"
    )
    private java.lang.String architectureNo;

    /**
     * <p>Field banAddress: 楼栋地址</p>
     */
    @TitleAnnotation(
            titleName = "*楼栋地址"
    )
    private java.lang.String banAddress;

    /**
     * <p>Field architectureName: 建筑物名称</p>
     */
    @TitleAnnotation(
            titleName = "建筑物名称"
    )
    private java.lang.String architectureName;

    /**
     * <p>Field doorplateAddress: 门牌地址</p>
     */
    @TitleAnnotation(
            titleName = "门牌地址"
    )
    private java.lang.String doorplateAddress;
    
    /**
     * <p>Field builtupArea: 建筑面积</p>
     */
    @TitleAnnotation(
            titleName = "建筑面积"
    )
    private java.lang.String builtupArea;
    
    /**
     * <p>Field totalNum: 总套数</p>
     */
    @TitleAnnotation(
            titleName = "总套数"
    )
    private java.lang.String totalNum;

    /**
     * <p>Field floorNumber: 楼层数</p>
     */
    @TitleAnnotation(
            titleName = "楼层数"
    )
    private java.lang.String floorNumber;
    
    /**
     * <p>Field architectureType: 建筑物类型</p>
     */
    @TitleAnnotation(
            titleName = "建筑物类型"
    )
    private java.lang.String architectureType;

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
     * <p>Field owner: 业主姓名</p>
     */
    @TitleAnnotation(
            titleName = "业主"
    )
    private java.lang.String owner;

    /**
     * <p>Field ownerPhone: 业主手机号</p>
     */
    @TitleAnnotation(
            titleName = "业主手机号"
    )
    private String ownerPhone;

    /**
     * <p>Field banLong: 楼栋长姓名</p>
     */
    @TitleAnnotation(
            titleName = "楼栋长"
    )
    private java.lang.String banLong;

    /**
     * <p>Field banLongPhone: 楼栋长手机号</p>
     */
    @TitleAnnotation(
            titleName = "楼栋长手机号"
    )
    private java.lang.String banLongPhone;
    
    /**
     * <p>Field banLongPhone: 业主固定电话</p>
     */
    @TitleAnnotation(
            titleName = "业主固定电话"
            )
    private java.lang.String ownerFixedPhone;

    /**
     * <p>Field error: 错误提示</p>
     */
    @TitleAnnotation(
            titleName = "错误提示"
    )
    private java.lang.String error;
    
    public java.lang.String getCommunityName() {
        return this.communityName;
    }

    public void setCommunityName(java.lang.String communityName) {
        this.communityName = communityName;
    }

    public java.lang.String getGridName() {
        return this.gridName;
    }

    public void setGridName(java.lang.String gridName) {
        this.gridName = gridName;
    }

    public java.lang.String getArchitectureNo() {
        return this.architectureNo;
    }

    public void setArchitectureNo(java.lang.String architectureNo) {
        this.architectureNo = architectureNo;
    }

    public java.lang.String getBanAddress() {
        return this.banAddress;
    }

    public void setBanAddress(java.lang.String banAddress) {
        this.banAddress = banAddress;
    }

    public java.lang.String getArchitectureName() {
        return this.architectureName;
    }

    public void setArchitectureName(java.lang.String architectureName) {
        this.architectureName = architectureName;
    }

    public java.lang.String getDoorplateAddress() {
        return this.doorplateAddress;
    }

    public void setDoorplateAddress(java.lang.String doorplateAddress) {
        this.doorplateAddress = doorplateAddress;
    }

    public java.lang.String getBuiltupArea() {
        return this.builtupArea;
    }

    public void setBuiltupArea(java.lang.String builtupArea) {
        this.builtupArea = builtupArea;
    }

    public java.lang.String getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(java.lang.String totalNum) {
        this.totalNum = totalNum;
    }

    public java.lang.String getFloorNumber() {
        return this.floorNumber;
    }

    public void setFloorNumber(java.lang.String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public java.lang.String getArchitectureType() {
        return this.architectureType;
    }

    public void setArchitectureType(java.lang.String architectureType) {
        this.architectureType = architectureType;
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

    public java.lang.String getOwner() {
        return this.owner;
    }

    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public String getOwnerPhone() {
        return this.ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public java.lang.String getBanLong() {
        return this.banLong;
    }

    public void setBanLong(java.lang.String banLong) {
        this.banLong = banLong;
    }

    public java.lang.String getBanLongPhone() {
        return this.banLongPhone;
    }

    public void setBanLongPhone(java.lang.String banLongPhone) {
        this.banLongPhone = banLongPhone;
    }

    public java.lang.String getOwnerFixedPhone() {
        return ownerFixedPhone;
    }

    public void setOwnerFixedPhone(java.lang.String ownerFixedPhone) {
        this.ownerFixedPhone = ownerFixedPhone;
    }

    public java.lang.String getError() {
        return this.error;
    }

    public void setError(java.lang.String error) {
        this.error = error;
    }

}
