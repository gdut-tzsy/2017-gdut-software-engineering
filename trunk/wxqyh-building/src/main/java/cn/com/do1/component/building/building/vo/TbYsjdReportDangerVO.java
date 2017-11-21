package cn.com.do1.component.building.building.vo;

import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.Date;

/**
 * Created by admin on 2017/8/22.
 */
public class TbYsjdReportDangerVO {
    private String id;
    private String describes;
    @FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
    private String report_time;
    private String status;
    private String lightStatus;

    private String community;
    private java.lang.String theBan;
    @FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
    private String creatorTime;
    private java.lang.String theBanName;
    private java.lang.String address;
    private java.lang.String relatedPerson;
    private java.lang.String creatorUser;
    private java.lang.String overtimeStatus;
    private java.lang.String result;
    private java.lang.String dangerType;
    @FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
    private String handleTime;
    private java.lang.String approvers;
    private java.lang.String currentNode;
    private java.lang.String currentNodeId;
    private java.lang.String remark;
    private java.lang.String handleUser;
    private java.lang.String handleUserId;
    private Integer commentCount;
    private java.lang.String title;
    private java.lang.String reportType;
    private String personName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(String lightStatus) {
        this.lightStatus = lightStatus;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }


    public String getTheBan() {
        return theBan;
    }

    public void setTheBan(String theBan) {
        this.theBan = theBan;
    }

    public String getTheBanName() {
        return theBanName;
    }

    public void setTheBanName(String theBanName) {
        this.theBanName = theBanName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRelatedPerson() {
        return relatedPerson;
    }

    public void setRelatedPerson(String relatedPerson) {
        this.relatedPerson = relatedPerson;
    }

    public String getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getOvertimeStatus() {
        return overtimeStatus;
    }

    public void setOvertimeStatus(String overtimeStatus) {
        this.overtimeStatus = overtimeStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDangerType() {
        return dangerType;
    }

    public void setDangerType(String dangerType) {
        this.dangerType = dangerType;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    public String getHandleUserId() {
        return handleUserId;
    }

    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
