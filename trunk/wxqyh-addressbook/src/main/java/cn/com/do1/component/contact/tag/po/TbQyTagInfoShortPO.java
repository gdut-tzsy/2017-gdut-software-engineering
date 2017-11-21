package cn.com.do1.component.contact.tag.po;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class TbQyTagInfoShortPO implements IBaseDBVO, Cloneable {
	private String id;
	private String wxTagId;
	/**
	 * 使用范围，1后台，3手机端+后台，5手机端
	 */
	private Integer status;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getWxTagId() {
		return wxTagId;
	}

	public void setWxTagId(String wxTagId) {
		this.wxTagId = wxTagId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "tb_qy_tag_info";
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

	/**
	 * 重写默认的toString方法，使其调用输出的内容更有意义
	 */
	public String toString() {
		return new org.apache.commons.lang3.builder.ReflectionToStringBuilder(
				this,
				org.apache.commons.lang3.builder.ToStringStyle.SIMPLE_STYLE)
				.toString();
	}

	/**
	 * maquanyang 2015-6-29 添加克隆方法
	 */
	public TbQyTagInfoShortPO clone() {
		try {
			return (TbQyTagInfoShortPO) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
