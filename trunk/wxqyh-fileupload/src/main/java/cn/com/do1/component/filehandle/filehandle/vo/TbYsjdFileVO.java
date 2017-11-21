package cn.com.do1.component.filehandle.filehandle.vo;

import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.List;

/**
 * Description :
 * Created by lwj on 2017-8-16.
 */
public class TbYsjdFileVO {
	private String id;
	private String createUserId;
	@FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
	private String createTime;
	private String createUserName;
	private String createHeadPic;
	private String createWxUserId;
	private String title;
	private String approverUserId;
	private String approverHeadPic;
	private String approver;
	private String approveContent;
	private String relatedPerson;
	private String status;
	private String forwardUser;
	private String orgId;
	private String corpId;
	private String approverWxUserId;
	private String incharges;
	private String commonPerson;
	private Integer commentCount;
	private List<TbYsjdFileRecipientVO> ccPersons;
	private String dataStatus;
	private String statusDesc;
	private String handledUser;

	public String getHandledUser() {
		return handledUser;
	}

	public void setHandledUser(String handledUser) {
		this.handledUser = handledUser;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public List<TbYsjdFileRecipientVO> getCcPersons() {
		return ccPersons;
	}

	public void setCcPersons(List<TbYsjdFileRecipientVO> ccPersons) {
		this.ccPersons = ccPersons;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateHeadPic() {
		return createHeadPic;
	}

	public void setCreateHeadPic(String createHeadPic) {
		this.createHeadPic = createHeadPic;
	}

	public String getCreateWxUserId() {
		return createWxUserId;
	}

	public void setCreateWxUserId(String createWxUserId) {
		this.createWxUserId = createWxUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(String approverUserId) {
		this.approverUserId = approverUserId;
	}

	public String getApproverHeadPic() {
		return approverHeadPic;
	}

	public void setApproverHeadPic(String approverHeadPic) {
		this.approverHeadPic = approverHeadPic;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApproveContent() {
		return approveContent;
	}

	public void setApproveContent(String approveContent) {
		this.approveContent = approveContent;
	}

	public String getRelatedPerson() {
		return relatedPerson;
	}

	public void setRelatedPerson(String relatedPerson) {
		this.relatedPerson = relatedPerson;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getForwardUser() {
		return forwardUser;
	}

	public void setForwardUser(String forwardUser) {
		this.forwardUser = forwardUser;
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

	public String getApproverWxUserId() {
		return approverWxUserId;
	}

	public void setApproverWxUserId(String approverWxUserId) {
		this.approverWxUserId = approverWxUserId;
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

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
}
