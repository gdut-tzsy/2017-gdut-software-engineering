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
public class TbYsjdFileCommentExtPO implements IBaseDBVO {
	private String extId;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "commentId", showType = "input", showOrder = 1, showLength = 64)
	private String commentId;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "userId", showType = "input", showOrder = 2, showLength = 64)
	private String userId;
	@Validation(must = false, length = 64, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createPerson", showType = "input", showOrder = 3, showLength = 64)
	private String createPerson;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 4, showLength = 19)
	private Date createTime;

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getExtId() {
		return this.extId;
	}


	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentId() {
		return this.commentId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
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

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE_COMMENT_EXT";
	}

	/**
	 * 获取对应表的主键字段名称
	 *
	 * @return
	 */
	public String _getPKColumnName() {
		return "extId";
	}

	/**
	 * 获取主键值
	 *
	 * @return
	 */
	public String _getPKValue() {
		return String.valueOf(extId);
	}

	/**
	 * 设置主键的值
	 *
	 * @return
	 */
	public void _setPKValue(Object value) {
		this.extId = (String) value;
	}
}
