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
public class TbYsjdFileFlowPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "fileId", showType = "input", showOrder = 1, showLength = 36)
	private String fileId;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createUserId", showType = "input", showOrder = 2, showLength = 36)
	private String createUserId;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 3, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "flowStatus", showType = "input", showOrder = 4, showLength = 2)
	private String flowStatus;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "flowDesc", showType = "input", showOrder = 5, showLength = 500)
	private String flowDesc;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "updateUserId", showType = "input", showOrder = 6, showLength = 36)
	private String updateUserId;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "updateTime", showType = "datetime", showOrder = 7, showLength = 19)
	private Date updateTime;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "handleUserId", showType = "input", showOrder = 8, showLength = 36)
	private String handleUserId;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "isHandle", showType = "input", showOrder = 9, showLength = 2)
	private String isHandle;
	private String handleUserName;
	private String sortNum;
	private String existImages;

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
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


	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserId() {
		return this.createUserId;
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


	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getFlowStatus() {
		return this.flowStatus;
	}


	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}

	public String getFlowDesc() {
		return this.flowDesc;
	}


	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserId() {
		return this.updateUserId;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = ConvertUtil.cvStUtildate(updateTime);
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}


	public void setHandleUserId(String handleUserId) {
		this.handleUserId = handleUserId;
	}

	public String getHandleUserId() {
		return this.handleUserId;
	}


	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}

	public String getIsHandle() {
		return this.isHandle;
	}

	public String getExistImages() {
		return existImages;
	}

	public void setExistImages(String existImages) {
		this.existImages = existImages;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_FLOW";
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
