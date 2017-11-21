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
public class TbYsjdFileApprovalPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approvalContent", showType = "input", showOrder = 1, showLength = 500)
	private String approvalContent;
	@Validation(must = false, length = 100, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approvalUserName", showType = "input", showOrder = 2, showLength = 100)
	private String approvalUserName;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approvalUserId", showType = "input", showOrder = 3, showLength = 36)
	private String approvalUserId;
	@Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "wxUserId", showType = "input", showOrder = 4, showLength = 50)
	private String wxUserId;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "status", showType = "input", showOrder = 5, showLength = 2)
	private String status;
	@Validation(must = false, length = 11, fieldType = "pattern", regex = "^\\d*$")
	@PageView(showName = "sort", showType = "input", showOrder = 6, showLength = 11)
	private Integer sort;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 7, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "remark", showType = "input", showOrder = 8, showLength = 500)
	private String remark;
	@Validation(must = false, length = 50, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createUser", showType = "input", showOrder = 9, showLength = 50)
	private String createUser;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}


	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}

	public String getApprovalContent() {
		return this.approvalContent;
	}


	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}

	public String getApprovalUserName() {
		return this.approvalUserName;
	}


	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}

	public String getApprovalUserId() {
		return this.approvalUserId;
	}


	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}

	public String getWxUserId() {
		return this.wxUserId;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}


	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return this.sort;
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


	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_APPROVAL";
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
