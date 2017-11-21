package cn.com.do1.component.building.building.model;

import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.PageView;
import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: cuijianpeng
 */
/**
 * <p>ClassName: TbYsjdHousePo</p>
 * <p>Description: 房屋po</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月22日</p>
 */
public class TbYsjdHousePo implements IBaseDBVO {

    /**
     * <p>Field id: 主键ID</p>
     */
    private java.lang.String id;

    /**
     * <p>Field houseNo: 房屋编码</p>
     */
    @Validation(must = false, length = 100, fieldType = "pattern", regex = "^.*$", name = "houseNo")
    @PageView(showType = "input", showOrder = 1, showName = "houseNo", showLength = 100)
    private java.lang.String houseNo;

    /**
     * <p>Field houseAddress: 房屋地址</p>
     */
    @Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$", name = "houseAddress")
    @PageView(showType = "input", showOrder = 2, showName = "houseAddress", showLength = 200)
    private java.lang.String houseAddress;

    /**
     * <p>Field banName: 楼栋名称</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "banName")
    @PageView(showType = "input", showOrder = 3, showName = "banName", showLength = 50)
    private java.lang.String banName;

    /**
     * <p>Field houseNumber: 房号</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$", name = "houseNumber")
    @PageView(showType = "input", showOrder = 4, showName = "houseNumber", showLength = 50)
    private java.lang.String houseNumber;

    /**
     * <p>Field propertyOwner: 产权人</p>
     */
    @Validation(must = false, length = 20, fieldType = "pattern", regex = "^\\\\d*$", name = "propertyOwner")
    @PageView(showType = "input", showOrder = 5, showName = "propertyOwner", showLength = 20)
    private java.lang.String propertyOwner;

    /**
     * <p>Field houseArea: 房屋面积</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^\\\\d*$", name = "houseArea")
    @PageView(showType = "input", showOrder = 6, showName = "houseArea", showLength = 50)
    private java.lang.Double houseArea;

    /**
     * <p>Field purpose: 使用用途 0:住宅 1:厂房 2:商业 3:商住 4:办公 5:公共设施 6:仓库 7:综合 8:其他</p>
     */
    @Validation(must = false, length = 100, fieldType = "pattern", regex = "^.*$", name = "purpose")
    @PageView(showType = "input", showOrder = 7, showName = "purpose", showLength = 100)
    private java.lang.String purpose;

    /**
     * <p>Field situation: 使用情况 0:待租 1:空置 2:出租 3:部分出租 4:自用 5:其他</p>
     */
    @Validation(must = false, length = 100, fieldType = "datetime", name = "situation")
    @PageView(showType = "datetime", showOrder = 8, showName = "situation", showLength = 100)
    private java.lang.String situation;

    /**
     * <p>Field structure: 房屋结构 0:单房 1:一房一厅 2:两房一厅 3:两房两厅 4:三房以上 5:其他</p>
     */
    @Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$", name = "structure")
    @PageView(showType = "input", showOrder = 9, showName = "structure", showLength = 36)
    private java.lang.String structure;

    /**
     * <p>Field banAddress: 所属楼栋</p>
     */
    @Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$", name = "banId")
    @PageView(showType = "input", showOrder = 10, showName = "banId", showLength = 200)
    private java.lang.String banAddress;

    /**
     * <p>Field banNo: 楼栋编号</p>
     */
    @Validation(must = false, length = 50, fieldType = "pattern", regex = "^\\\\d*$", name = "banNo")
    @PageView(showType = "input", showOrder = 11, showName = "banNo", showLength = 50)
    private java.lang.String banNo;

    /**
     * <p>Field creatorTime: 创建时间</p>
     */
    @Validation(must = false, length = 19, fieldType = "datetime", regex = "")
    @PageView(showName = "creatorTime", showType = "datetime", showOrder = 12, showLength = 19)
    private java.util.Date creatorTime;
    
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

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

    public java.lang.Double getHouseArea() {
        return this.houseArea;
    }
    
    public void setHouseArea(java.lang.String houseArea) {
        this.houseArea = ConvertUtil.Str2Double(houseArea);
    }
    
    public void setHouseArea(java.lang.Double houseArea) {
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

    public java.lang.String getBanAddress() {
        return this.banAddress;
    }

    public void setBanAddress(java.lang.String banAddress) {
        this.banAddress = banAddress;
    }

    public java.lang.String getBanNo() {
        return this.banNo;
    }

    public void setBanNo(java.lang.String banNo) {
        this.banNo = banNo;
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
    
    /**
     * 获取数据库中对应的表名
     * 
     * @return
     */
    public String _getTableName() {
        return "tb_ysjd_house";
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
