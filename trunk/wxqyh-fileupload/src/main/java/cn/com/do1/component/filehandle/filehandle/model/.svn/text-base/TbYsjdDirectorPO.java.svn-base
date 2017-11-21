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
public class TbYsjdDirectorPO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "creatorUser", showType = "input", showOrder = 1, showLength = 36)
	private String creatorUser;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "creatorTime", showType = "datetime", showOrder = 2, showLength = 19)
	private Date creatorTime;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "type", showType = "input", showOrder = 3, showLength = 2)
	private String type;
	@Validation(must = false, length = 21845, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "target", showType = "input", showOrder = 4, showLength = 21845)
	private String target;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "ranges", showType = "input", showOrder = 5, showLength = 2)
	private String ranges;
	@Validation(must = false, length = 21845, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "dept", showType = "input", showOrder = 6, showLength = 21845)
	private String dept;
	private String childDept;

	public String getChildDept() {
		return childDept;
	}

	public void setChildDept(String childDept) {
		this.childDept = childDept;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}


	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}

	public String getCreatorUser() {
		return this.creatorUser;
	}


	public void setCreatorTime(Date creatorTime) {
		this.creatorTime = creatorTime;
	}

	public void setCreatorTime(String creatorTime) {
		this.creatorTime = ConvertUtil.cvStUtildate(creatorTime);
	}

	public Date getCreatorTime() {
		return this.creatorTime;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}


	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	public String getRanges() {
		return ranges;
	}

	public void setRanges(String ranges) {
		this.ranges = ranges;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDept() {
		return this.dept;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_DIRECTOR";
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
