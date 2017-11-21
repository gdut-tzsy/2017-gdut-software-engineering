package cn.com.do1.component.building.building.model;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;
import cn.com.do1.common.annotation.bean.PageView;
import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.Date;

import oracle.sql.TIMESTAMP;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: cuijianpeng
 */
/**
 * <p>ClassName: TbYsjdBanPo</p>
 * <p>Description: 楼栋po</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月22日</p>
 */
public class TbYsjdBanPo implements IBaseDBVO {

    /**
     * <p>Field id: 主键ID</p>
     */
    private java.lang.String id;

    /**
     * <p>Field architectureNo: 建筑编码</p>
     */
    @Validation(must = false, length = 100, fieldType = "pattern", regex = "^.*$", name = "id")
    @PageView(showType = "input", showOrder = 1, showName = "architectureNo", showLength = 100)
    private java.lang.String architectureNo;

    /**
     * <p>Field banAddress: 楼栋地址</p>
     */
    @Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$", name = "banAddress")
    @PageView(showType = "input", showOrder = 2, showName = "banAddress", showLength = 200)
    private java.lang.String banAddress;

    /**
     * <p>Field architectureName: 建筑物名称</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "architectureName")
    @PageView(showType = "input", showOrder = 3, showName = "architectureName", showLength = 50)
    private java.lang.String architectureName;

    /**
     * <p>Field doorplateAddress: 门牌地址</p>
     */
    @Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$", name = "doorplateAddress")
    @PageView(showType = "input", showOrder = 4, showName = "doorplateAddress", showLength = 200)
    private java.lang.String doorplateAddress;

    /**
     * <p>Field builtupArea: 建筑面积</p>
     */
    @Validation(must = false, length = 12, fieldType = "pattern", regex = "^\\\\d*$", name = "builtupArea")
    @PageView(showType = "input", showOrder = 5, showName = "builtupArea", showLength = 12)
    private java.lang.Float builtupArea;

    /**
     * <p>Field totalNum: 总套数</p>
     */
    @Validation(must = false, length = 11, fieldType = "pattern", regex = "^\\\\d*$", name = "totalNum")
    @PageView(showType = "input", showOrder = 6, showName = "totalNum", showLength = 11)
    private java.lang.Integer totalNum;

    /**
     * <p>Field architectureType: 建筑物类型</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "architectureType")
    @PageView(showType = "input", showOrder = 7, showName = "architectureType", showLength = 20)
    private java.lang.String architectureType;

    /**
     * <p>Field purpose: 使用用途</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "purpose")
    @PageView(showType = "input", showOrder = 8, showName = "purpose", showLength = 20)
    private java.lang.String purpose;

    /**
     * <p>Field situation: 使用情况</p>
     */
    @Validation(must = false, length = 19, fieldType = "datetime", name = "situation")
    @PageView(showType = "datetime", showOrder = 9, showName = "situation", showLength = 19)
    private java.lang.String situation;

    /**
     * <p>Field community: 所属社区</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "community")
    @PageView(showType = "input", showOrder = 10, showName = "community", showLength = 36)
    private java.lang.String community;

    /**
     * <p>Field grid: 所属网格</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "grid")
    @PageView(showType = "input", showOrder = 11, showName = "grid", showLength = 36)
    private java.lang.String grid;

    /**
     * <p>Field floorNumber: 楼层数</p>
     */
    @Validation(must = false, length = 11, fieldType = "pattern", regex = "^\\\\d*$", name = "floorNumber")
    @PageView(showType = "input", showOrder = 12, showName = "floorNumber", showLength = 11)
    private java.lang.Integer floorNumber;

    /**
     * <p>Field owner: 业主姓名</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "owner")
    @PageView(showType = "input", showOrder = 13, showName = "owner", showLength = 20)
    private java.lang.String owner;

    /**
     * <p>Field ownerPhone: 业主手机号</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "ownerPhone")
    @PageView(showType = "input", showOrder = 14, showName = "ownerPhone", showLength = 20)
    private java.lang.String ownerPhone;

    /**
     * <p>Field banLong: 楼栋长姓名</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "banLong")
    @PageView(showType = "input", showOrder = 15, showName = "banLong", showLength = 20)
    private java.lang.String banLong;

    /**
     * <p>Field banLongPhone: 楼栋长手机号</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$", name = "banLongPhone")
    @PageView(showType = "input", showOrder = 16, showName = "banLongPhone", showLength = 20)
    private java.lang.String banLongPhone;

    /**
     * <p>Field light: 楼栋亮灯情况</p>
     */
    @Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$", name = "light")
    @PageView(showType = "input", showOrder = 17, showName = "light", showLength = 2)
    private java.lang.String light;

    /**
     * <p>Field ownerId: 业主ID</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "ownerId")
    @PageView(showType = "input", showOrder = 18, showName = "ownerId", showLength = 36)
    private java.lang.String ownerId;

    /**
     * <p>Field banLongId: 楼栋长ID</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "banLongId")
    @PageView(showType = "input", showOrder = 19, showName = "banLongId", showLength = 36)
    private java.lang.String banLongId;
    
    /**
     * <p>Field banLongId: 社区名称</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "communityName")
    @PageView(showType = "input", showOrder = 20, showName = "communityName", showLength = 50)
    private java.lang.String communityName;
    
    /**
     * <p>Field banLongId: 网格名称</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "gridName")
    @PageView(showType = "input", showOrder = 21, showName = "gridName", showLength = 50)
    private java.lang.String gridName;
    
    /**
     * <p>Field banLongId: 网格员名称</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "gridOperatorName")
    @PageView(showType = "input", showOrder = 22, showName = "gridOperatorName", showLength = 50)
    private java.lang.String gridOperatorName;
    
    /**
     * <p>Field banLongId: 网格员id</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "gridOperatorId")
    @PageView(showType = "input", showOrder = 23, showName = "gridOperatorId", showLength = 36)
    private java.lang.String gridOperatorId;
    
    /**
     * <p>Field creatorTime: 创建时间</p>
     */
    @Validation(must = false, length = 19, fieldType = "datetime", regex = "")
    @PageView(showName = "creatorTime", showType = "datetime", showOrder = 24, showLength = 19)
    private java.util.Date creatorTime;

    /**
     * <p>Field banLongId: 业主固定电话</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "ownerFixedPhone")
    @PageView(showType = "input", showOrder = 25, showName = "ownerFixedPhone", showLength = 50)
    private java.lang.String ownerFixedPhone;
    
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getId() {
        return this.id;
    }

    public void setArchitectureNo(java.lang.String architectureNo) {
        this.architectureNo = architectureNo;
    }

    public java.lang.String getArchitectureNo() {
        return this.architectureNo;
    }

    public void setBanAddress(java.lang.String banAddress) {
        this.banAddress = banAddress;
    }

    public java.lang.String getBanAddress() {
        return this.banAddress;
    }

    public void setArchitectureName(java.lang.String architectureName) {
        this.architectureName = architectureName;
    }

    public java.lang.String getArchitectureName() {
        return this.architectureName;
    }

    public void setDoorplateAddress(java.lang.String doorplateAddress) {
        this.doorplateAddress = doorplateAddress;
    }

    public java.lang.String getDoorplateAddress() {
        return this.doorplateAddress;
    }

    public void setBuiltupArea(java.lang.Float builtupArea) {
        this.builtupArea = builtupArea;
    }

    public void setBuiltupArea(java.lang.String builtupArea) {
        this.builtupArea = ConvertUtil.Str2Float(builtupArea);
    }

    public java.lang.Float getBuiltupArea() {
        return this.builtupArea;
    }

    public void setTotalNum(java.lang.Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setTotalNum(java.lang.String totalNum) {
        this.totalNum = ConvertUtil.cvStIntg(totalNum);
    }

    public java.lang.Integer getTotalNum() {
        return this.totalNum;
    }

    public void setArchitectureType(java.lang.String architectureType) {
        this.architectureType = architectureType;
    }

    public java.lang.String getArchitectureType() {
        return this.architectureType;
    }

    public void setPurpose(java.lang.String purpose) {
        this.purpose = purpose;
    }

    public java.lang.String getPurpose() {
        return this.purpose;
    }

    public void setSituation(java.lang.String situation) {
        this.situation = situation;
    }

    public java.lang.String getSituation() {
        return this.situation;
    }

    public void setCommunity(java.lang.String community) {
        this.community = community;
    }

    public java.lang.String getCommunity() {
        return this.community;
    }

    public void setGrid(java.lang.String grid) {
        this.grid = grid;
    }

    public java.lang.String getGrid() {
        return this.grid;
    }

    public void setFloorNumber(java.lang.Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setFloorNumber(java.lang.String floorNumber) {
        this.floorNumber = ConvertUtil.cvStIntg(floorNumber);
    }

    public java.lang.Integer getFloorNumber() {
        return this.floorNumber;
    }

    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public java.lang.String getOwner() {
        return this.owner;
    }

    public void setOwnerPhone(java.lang.String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public java.lang.String getOwnerPhone() {
        return this.ownerPhone;
    }

    public void setBanLong(java.lang.String banLong) {
        this.banLong = banLong;
    }

    public java.lang.String getBanLong() {
        return this.banLong;
    }

    public void setBanLongPhone(java.lang.String banLongPhone) {
        this.banLongPhone = banLongPhone;
    }

    public java.lang.String getBanLongPhone() {
        return this.banLongPhone;
    }

    public void setLight(java.lang.String light) {
        this.light = light;
    }

    public java.lang.String getLight() {
        return this.light;
    }

    public void setOwnerId(java.lang.String ownerId) {
        this.ownerId = ownerId;
    }

    public java.lang.String getOwnerId() {
        return this.ownerId;
    }

    public void setBanLongId(java.lang.String banLongId) {
        this.banLongId = banLongId;
    }

    public java.lang.String getBanLongId() {
        return this.banLongId;
    }

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

    public java.lang.String getGridOperatorName() {
        return this.gridOperatorName;
    }

    public void setGridOperatorName(java.lang.String gridOperatorName) {
        this.gridOperatorName = gridOperatorName;
    }

    public java.lang.String getGridOperatorId() {
        return this.gridOperatorId;
    }

    public void setGridOperatorId(java.lang.String gridOperatorId) {
        this.gridOperatorId = gridOperatorId;
    }

    public java.util.Date getCreatorTime() {
        return this.creatorTime;
    }

    public void setCreatorTime(java.util.Date creatorTime) {
        this.creatorTime = creatorTime;
    }
    
    public void setCreatorTime(String creatorTime) {
        this.creatorTime = ConvertUtil.cvStUtildate(creatorTime);
    }
    
    public java.lang.String getOwnerFixedPhone() {
        return ownerFixedPhone;
    }

    public void setOwnerFixedPhone(java.lang.String ownerFixedPhone) {
        this.ownerFixedPhone = ownerFixedPhone;
    }

    /**
     * 获取数据库中对应的表名
     * 
     * @return
     */
    public String _getTableName() {
        return "tb_ysjd_ban";
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
        return String.valueOf(this.id);
    }

    /**
     * 设置主键的值
     * 
     * @return
     */
    public void _setPKValue(Object value) {
        this.id = (java.lang.String) value;
    }
}
