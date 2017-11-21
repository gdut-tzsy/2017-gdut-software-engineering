package cn.com.do1.component.contact.post.model;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 *  All rights reserved. 
 *  User: chenlinhuan 
 *  职位-类别关联表
 */

public class TbQyHrPostCatalogRefPO implements IBaseDBVO,Cloneable {

	private String id;

	private String postId;

	private String catalogId;

	private String orgId;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostId() {
		return this.postId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogId() {
		return this.catalogId;
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
		return "tb_qy_hr_post_catalog_ref";
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
	public TbQyHrPostCatalogRefPO clone() throws CloneNotSupportedException {
		try {
			return (TbQyHrPostCatalogRefPO) super.clone();
		}catch (CloneNotSupportedException e){
			return new TbQyHrPostCatalogRefPO();
		}
	}
}
