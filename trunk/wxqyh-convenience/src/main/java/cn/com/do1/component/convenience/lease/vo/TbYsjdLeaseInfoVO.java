package cn.com.do1.component.convenience.lease.vo;

import cn.com.do1.common.annotation.bean.FormatMask;
import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${tempDataMap.user}
 */
public class TbYsjdLeaseInfoVO{

    private String id;


    private String title;


    private String room;


    private Double rent;


    private String community;


    private String floor;


    private String type;


    private String address;


    private String paymentMethod;


    private Float area;


    private String phone;


    private String renovation;


    private String rentalMode;


    private String houseLight;


    private String periphery;


    private String status;


    private String creatorUser;

    private java.lang.String creatorUserName;


    @FormatMask(type = "date", value = "yyyy-MM-dd")
    private String creatorTime;


    private Integer browser;

    private String image;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }


    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return this.room;
    }


    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Double getRent() {
        return this.rent;
    }


    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCommunity() {
        return this.community;
    }


    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloor() {
        return this.floor;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }


    public void setArea(Float area) {
        this.area = area;
    }

    public void setArea(String area) {
        this.area = ConvertUtil.Str2Float(area);
    }

    public Float getArea() {
        return this.area;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }


    public void setRenovation(String renovation) {
        this.renovation = renovation;
    }

    public String getRenovation() {
        return this.renovation;
    }


    public void setRentalMode(String rentalMode) {
        this.rentalMode = rentalMode;
    }

    public String getRentalMode() {
        return this.rentalMode;
    }


    public void setHouseLight(String houseLight) {
        this.houseLight = houseLight;
    }

    public String getHouseLight() {
        return this.houseLight;
    }


    public void setPeriphery(String periphery) {
        this.periphery = periphery;
    }

    public String getPeriphery() {
        return this.periphery;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getCreatorUser() {
        return this.creatorUser;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public void setBrowser(Integer browser) {
        this.browser = browser;
    }

    public void setBrowser(String browser) {
        this.browser = ConvertUtil.cvStIntg(browser);
    }

    public Integer getBrowser() {
        return this.browser;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

}
