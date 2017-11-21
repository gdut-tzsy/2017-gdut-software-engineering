package cn.com.do1.component.filehandle.filehandle.vo;

import cn.com.do1.common.annotation.bean.FormatMask;

import java.util.List;

/**
 * Description :
 * Created by lwj on 2017-8-21.
 */
public class TbYsjdFileFlowVO {
	private String id;
	private String fileId;
	private String createUserId;
	@FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
	private String createTime;
	private String flowStatus;
	private String flowDesc;
	private String updateUserId;
	@FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
	private String updateTime;
	private String handleUserId;
	private String isHandle;
	private String handleUserName;
	private String sortNum;
	private String existImages;

	private List<TbYsjdFilePicVO> commentPic;

	public List<TbYsjdFilePicVO> getCommentPic() {
		return commentPic;
	}

	public void setCommentPic(List<TbYsjdFilePicVO> commentPic) {
		this.commentPic = commentPic;
	}

	public String getExistImages() {
		return existImages;
	}

	public void setExistImages(String existImages) {
		this.existImages = existImages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getFlowDesc() {
		return flowDesc;
	}

	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getHandleUserId() {
		return handleUserId;
	}

	public void setHandleUserId(String handleUserId) {
		this.handleUserId = handleUserId;
	}

	public String getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
}
