package cn.com.do1.component.contact.post.vo;


import cn.com.do1.common.annotation.bean.DictDesc;
import cn.com.do1.common.annotation.bean.FormatMask;

public class TbQyHrPostCatalogVO   {
	
	private String id;
	
	private String name;

	private String isUse;
	
	@FormatMask(type = "date", value = "yyyy-MM-dd HH:mm:ss")
	private String createTime;
	
	private String sort;
	
	@DictDesc(refField = "isUse", typeName = "enableStatus")
    private String isUseDesc;

	public String getName() {
		return name;
	}

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}


	public String getIsUseDesc() {
		return isUseDesc;
	}


	public void setIsUseDesc(String isUseDesc) {
		this.isUseDesc = isUseDesc;
	}


	
	

}
