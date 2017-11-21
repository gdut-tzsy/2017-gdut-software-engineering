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
public class TbYsjdFileRecipientPO implements IBaseDBVO {
	private String recipientId;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "fileId", showType = "input", showOrder = 1, showLength = 64)
	private String fileId;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "userId", showType = "input", showOrder = 2, showLength = 64)
	private String userId;
	@Validation(must = false, length = 8, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "userType", showType = "input", showOrder = 3, showLength = 8)
	private String userType;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createPerson", showType = "input", showOrder = 4, showLength = 64)
	private String createPerson;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 5, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 11, fieldType = "pattern", regex = "^\\d*$")
	@PageView(showName = "sortNum", showType = "input", showOrder = 6, showLength = 11)
	private Integer sortNum;
	@Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "personName", showType = "input", showOrder = 7, showLength = 200)
	private String personName;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "wxUserId", showType = "input", showOrder = 8, showLength = 64)
	private String wxUserId;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "headPic", showType = "input", showOrder = 9, showLength = 500)
	private String headPic;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "departmentName", showType = "input", showOrder = 10, showLength = 500)
	private String departmentName;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "departmentId", showType = "input", showOrder = 11, showLength = 500)
	private String departmentId;
	@Validation(must = false, length = 4, fieldType = "pattern", regex = "^\\d*$")
	@PageView(showName = "readenStatus", showType = "input", showOrder = 12, showLength = 4)
	private Integer readenStatus;
	@Validation(must = false, length = 4, fieldType = "pattern", regex = "^\\d*$")
	@PageView(showName = "isCreator", showType = "input", showOrder = 13, showLength = 4)
	private Integer isCreator;

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getRecipientId() {
		return this.recipientId;
	}


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return this.fileId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return this.userType;
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


	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getSortNum() {
		return this.sortNum;
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


	public void setReadenStatus(Integer readenStatus) {
		this.readenStatus = readenStatus;
	}

	public Integer getReadenStatus() {
		return this.readenStatus;
	}


	public void setIsCreator(Integer isCreator) {
		this.isCreator = isCreator;
	}

	public Integer getIsCreator() {
		return this.isCreator;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_RECIPIENT";
	}

	/**
	 * 获取对应表的主键字段名称
	 *
	 * @return
	 */
	public String _getPKColumnName() {
		return "recipientId";
	}

	/**
	 * 获取主键值
	 *
	 * @return
	 */
	public String _getPKValue() {
		return String.valueOf(recipientId);
	}

	/**
	 * 设置主键的值
	 *
	 * @return
	 */
	public void _setPKValue(Object value) {
		this.recipientId = (String) value;
	}
}
