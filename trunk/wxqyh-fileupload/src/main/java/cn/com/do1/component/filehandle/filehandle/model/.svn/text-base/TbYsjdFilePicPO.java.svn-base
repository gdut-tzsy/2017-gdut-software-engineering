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
public class TbYsjdFilePicPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "fileId", showType = "input", showOrder = 1, showLength = 64)
	private String fileId;
	@Validation(must = false, length = 255, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "picPath", showType = "input", showOrder = 2, showLength = 255)
	private String picPath;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createPerson", showType = "input", showOrder = 3, showLength = 36)
	private String createPerson;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 4, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 8, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "status", showType = "input", showOrder = 5, showLength = 8)
	private String status;
	@Validation(must = false, length = 11, fieldType = "pattern", regex = "^\\d*$")
	@PageView(showName = "sortNum", showType = "input", showOrder = 6, showLength = 11)
	private Integer sortNum;

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


	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPicPath() {
		return this.picPath;
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


	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}


	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_PIC";
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
