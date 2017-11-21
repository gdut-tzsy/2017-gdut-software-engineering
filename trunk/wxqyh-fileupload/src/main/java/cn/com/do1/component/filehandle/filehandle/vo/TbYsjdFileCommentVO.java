package cn.com.do1.component.filehandle.filehandle.vo;

import cn.com.do1.common.annotation.bean.FormatMask;

/**
 * Description :
 * Created by lwj on 2017-8-21.
 */
public class TbYsjdFileCommentVO {
	private String id;
	private String fileId;
	private String content;
	private String createPerson;
	@FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
	private String createTime;
	private String commentStatus;
	private String personName;
	private String wxUserId;
	private String headPic;
	private String departmentName;
	private String departmentId;
	private String type;
	private String deviceNum;
	private String status;
	private Integer isCreator;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getWxUserId() {
		return wxUserId;
	}

	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Integer isCreator) {
		this.isCreator = isCreator;
	}
}
