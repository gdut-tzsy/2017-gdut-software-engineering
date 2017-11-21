package cn.com.do1.component.filehandle.filehandle.model;

import cn.com.do1.common.annotation.bean.PageView;
import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;

import java.util.Date;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class TbYsjdFileCommentPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "fileId", showType = "input", showOrder = 1, showLength = 36)
	private String fileId;
	@Validation(must = false, length = 1024, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "content", showType = "input", showOrder = 2, showLength = 1024)
	private String content;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createPerson", showType = "input", showOrder = 3, showLength = 36)
	private String createPerson;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 4, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "commentStatus", showType = "input", showOrder = 5, showLength = 2)
	private String commentStatus;
	@Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "personName", showType = "input", showOrder = 6, showLength = 200)
	private String personName;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "wxUserId", showType = "input", showOrder = 7, showLength = 64)
	private String wxUserId;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "headPic", showType = "input", showOrder = 8, showLength = 500)
	private String headPic;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "departmentName", showType = "input", showOrder = 9, showLength = 500)
	private String departmentName;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "departmentId", showType = "input", showOrder = 10, showLength = 500)
	private String departmentId;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "type", showType = "input", showOrder = 11, showLength = 2)
	private String type;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "deviceNum", showType = "input", showOrder = 12, showLength = 64)
	private String deviceNum;
	private String status;
	private Integer isCreator;

	public Integer getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Integer isCreator) {
		this.isCreator = isCreator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return this.fileId;
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
		return "TB_YSJD_FILE_COMMENT";
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
