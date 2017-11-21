package cn.com.do1.component.contact.post.model;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;
import cn.com.do1.common.util.reflation.ConvertUtil;

import java.util.Date;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 *  All rights reserved. 
 *  User: chenlinhuan 
 *  职位表
 */

public class TbQyHrPostPO implements IBaseDBVO,Cloneable {

	private String id;

	private String name;

	private Integer isUse;

	private String description;

	private String creator;

	private Date createTime;

	private String orgId;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return this.creator;
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

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "tb_qy_hr_post";
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


	@Override
	public TbQyHrPostPO clone() throws CloneNotSupportedException {
		try {
			return (TbQyHrPostPO) super.clone();
		}catch (CloneNotSupportedException e){
			return new TbQyHrPostPO();
		}
	}

}
