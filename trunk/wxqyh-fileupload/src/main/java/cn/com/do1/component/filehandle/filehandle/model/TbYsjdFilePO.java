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
public class TbYsjdFilePO implements IBaseDBVO {
	private String id;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createUserId", showType = "input", showOrder = 1, showLength = 36)
	private String createUserId;
	@Validation(must = false, length = 19, fieldType = "datetime", regex = "")
	@PageView(showName = "createTime", showType = "datetime", showOrder = 2, showLength = 19)
	private Date createTime;
	@Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createUserName", showType = "input", showOrder = 3, showLength = 20)
	private String createUserName;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createHeadPic", showType = "input", showOrder = 4, showLength = 500)
	private String createHeadPic;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "createWxUserId", showType = "input", showOrder = 5, showLength = 36)
	private String createWxUserId;
	@Validation(must = false, length = 100, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "title", showType = "input", showOrder = 6, showLength = 100)
	private String title;
	@Validation(must = false, length = 36, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approverUserId", showType = "input", showOrder = 7, showLength = 36)
	private String approverUserId;
	@Validation(must = false, length = 500, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approverHeadPic", showType = "input", showOrder = 8, showLength = 500)
	private String approverHeadPic;
	@Validation(must = false, length = 20, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approver", showType = "input", showOrder = 9, showLength = 20)
	private String approver;
	@Validation(must = false, length = 800, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "approveContent", showType = "input", showOrder = 10, showLength = 800)
	private String approveContent;
	@Validation(must = false, length = 21845, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "relatedPerson", showType = "input", showOrder = 11, showLength = 21845)
	private String relatedPerson;
	@Validation(must = false, length = 2, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "status", showType = "input", showOrder = 12, showLength = 2)
	private String status;
	@Validation(must = false, length = 21845, fieldType = "pattern", regex = "^.*$")
	@PageView(showName = "forwardUser", showType = "input", showOrder = 13, showLength = 21845)
	private String forwardUser;
	private String orgId;
	private String corpId;
	private String approverWxUserId;
	private String incharges;
	private String commonPerson;
	private Integer commentCount;
	private String handledUser;

	public String getHandledUser() {
		return handledUser;
	}

	public void setHandledUser(String handledUser) {
		this.handledUser = handledUser;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getIncharges() {
		return incharges;
	}

	public void setIncharges(String incharges) {
		this.incharges = incharges;
	}

	public String getCommonPerson() {
		return commonPerson;
	}

	public void setCommonPerson(String commonPerson) {
		this.commonPerson = commonPerson;
	}

	public String getApproverWxUserId() {
		return approverWxUserId;
	}

	public void setApproverWxUserId(String approverWxUserId) {
		this.approverWxUserId = approverWxUserId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}


	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserId() {
		return this.createUserId;
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


	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserName() {
		return this.createUserName;
	}


	public void setCreateHeadPic(String createHeadPic) {
		this.createHeadPic = createHeadPic;
	}

	public String getCreateHeadPic() {
		return this.createHeadPic;
	}


	public void setCreateWxUserId(String createWxUserId) {
		this.createWxUserId = createWxUserId;
	}

	public String getCreateWxUserId() {
		return this.createWxUserId;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}


	public void setApproverUserId(String approverUserId) {
		this.approverUserId = approverUserId;
	}

	public String getApproverUserId() {
		return this.approverUserId;
	}


	public void setApproverHeadPic(String approverHeadPic) {
		this.approverHeadPic = approverHeadPic;
	}

	public String getApproverHeadPic() {
		return this.approverHeadPic;
	}


	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApprover() {
		return this.approver;
	}


	public void setApproveContent(String approveContent) {
		this.approveContent = approveContent;
	}

	public String getApproveContent() {
		return this.approveContent;
	}


	public void setRelatedPerson(String relatedPerson) {
		this.relatedPerson = relatedPerson;
	}

	public String getRelatedPerson() {
		return this.relatedPerson;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}


	public void setForwardUser(String forwardUser) {
		this.forwardUser = forwardUser;
	}

	public String getForwardUser() {
		return this.forwardUser;
	}

	/**
	 * 获取数据库中对应的表名
	 *
	 * @return
	 */
	public String _getTableName() {
		return "TB_YSJD_FILE";
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
