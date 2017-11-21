package cn.com.do1.component.contact.post.vo;

import cn.com.do1.common.annotation.bean.DictDesc;

public class TbQyHrPostVO {
	private String id;

	private String name;

	private String isUse;

	private String description;

	private String creator;

	private String createTime;

	private String orgId;

	private String catalogName;
	
	private String catalogId;
	
	@DictDesc(refField = "isUse", typeName = "enableStatus")
    private String isUseDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getIsUseDesc() {
		return isUseDesc;
	}

	public void setIsUseDesc(String isUseDesc) {
		this.isUseDesc = isUseDesc;
	}

}
