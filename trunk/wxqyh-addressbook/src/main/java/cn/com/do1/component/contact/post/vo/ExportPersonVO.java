package cn.com.do1.component.contact.post.vo;

import cn.com.do1.component.common.annotation.TitleAnnotation;

public class ExportPersonVO {
	
	@TitleAnnotation(titleName="姓名")
	private String personName;
	@TitleAnnotation(titleName="账号")
	private String pinyin;
	@TitleAnnotation(titleName="电话")
	private String mobile;
	@TitleAnnotation(titleName="职位")
	private String position;
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	
}
