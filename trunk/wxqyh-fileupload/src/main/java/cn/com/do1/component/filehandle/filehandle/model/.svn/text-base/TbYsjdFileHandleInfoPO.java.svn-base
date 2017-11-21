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
public class TbYsjdFileHandleInfoPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "fileId", showType = "input", showOrder = 1, showLength = 36)
	private String fileId;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 2, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "handleTime", showType = "datetime", showOrder = 3, showLength = 19)
	private Date handleTime;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "handleUserId", showType = "input", showOrder = 4, showLength = 36)
	private String handleUserId;
	@Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "handleUserName", showType = "input", showOrder = 5, showLength = 20)
	private String handleUserName;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "isEnd", showType = "input", showOrder = 6, showLength = 2)
	private String isEnd;
	@Validation(must = false, length = 21845, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "forwardUser", showType = "input", showOrder = 7, showLength = 21845)
	private String forwardUser;
	@Validation(must = false, length = 200, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "flowDesc", showType = "input", showOrder = 8, showLength = 200)
	private String flowDesc;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "isHandle", showType = "input", showOrder = 9, showLength = 2)
	private String isHandle;

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


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = ConvertUtil.cvStUtildate(createTime);
	}

	public Date getCreateTime() {
		return this.createTime;
	}


	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = ConvertUtil.cvStUtildate(handleTime);
	}

	public Date getHandleTime() {
		return this.handleTime;
	}


	public void setHandleUserId(String handleUserId) {
		this.handleUserId = handleUserId;
	}

	public String getHandleUserId() {
		return this.handleUserId;
	}


	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public String getHandleUserName() {
		return this.handleUserName;
	}


	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getIsEnd() {
		return this.isEnd;
	}


	public void setForwardUser(String forwardUser) {
		this.forwardUser = forwardUser;
	}

	public String getForwardUser() {
		return this.forwardUser;
	}


	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}

	public String getFlowDesc() {
		return this.flowDesc;
	}


	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}

	public String getIsHandle() {
		return this.isHandle;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_HANDLE_INFO";
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
