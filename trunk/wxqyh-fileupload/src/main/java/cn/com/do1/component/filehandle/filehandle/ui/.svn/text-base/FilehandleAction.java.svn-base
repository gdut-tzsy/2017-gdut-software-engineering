package cn.com.do1.component.filehandle.filehandle.ui;

import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileCommentPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFilePO;
import cn.com.do1.component.filehandle.filehandle.service.IFilehandleService;
import cn.com.do1.component.filehandle.filehandle.util.ConstantUtil;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileFlowVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileVO;
import cn.com.do1.component.uploadfile.imageupload.service.IFileMediaService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class FilehandleAction extends BaseAction {
	private IFilehandleService filehandleService;
	private TbYsjdFilePO filePO;
	private String relatives;
	private String[] imageUrls;
	private String[] incharges;
	private TbYsjdFileCommentPO tbQyDiaryCommentPO;
	private String userIds;
	private String[] mediaIds;
	private IFileMediaService fileMediaService;
	private String approverUserId;
	private String approverHeadPic;
	private String approver;
	private String approverWxUserId;
	private String lastCommentId;
	private String fileId;
	private String content;
	private String handleType;
	private IDepartmentMgrService departmentMgrService;
	private String[] deptIds;
	private IContactMgrService contactMgrService;
	private ISelectUserMgrService selectUserService;


	/**
	 * @description 文件上传时加载相关数据，并判断权限
	 * @method ajaxLoadInfo
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxLoadInfo() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		boolean b = filehandleService.isFileUploadAuth(userInfo.getUserId());
		if (!b) {
			setActionResult("1001", "您没有上传文件的权限，请联系管理员！");
			return;
		}
		List<TbQyUserInfoVO> list = filehandleService.getDirector();
		addJsonArray("directorList", list);
	}

	/**
	 * @description 分页获取列表数据
	 * @method ajaxGetPage
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "title", type = "string", format = "%%%s%%")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxGetPage() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		if (!getSearchValue().containsKey("type") || !getSearchValue().containsKey("status")) {
			setActionResult("1001", "需要传入的参数为空！");
			return;
		}
		if (!getSearchValue().containsKey("statusDesc")) {
			getSearchValue().put("statusDesc", "0");
		}
		getSearchValue().put("userId", "%" + userInfo.getUserId() + "%");
		Pager pager = new Pager(request, getPageSize());
		pager = filehandleService.getFilePage(getSearchValue(), pager);
		addJsonPager("pageData", pager);
	}

	/**
	 * @description 新增文件管理
	 * @method ajaxAdd
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "上传成功", faileMsg = "操作失败"))
	public void ajaxAdd() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		filePO.setApprover(approver);
		filePO.setApproverHeadPic(approverHeadPic);
		filePO.setApproverUserId(approverUserId);
		filePO.setApproverWxUserId(approverWxUserId);
		filehandleService.addFile(filePO, userInfo, relatives, mediaIds);
	}

	/**
	 * @description 提交领导的批阅
	 * @method ajaxSubmitDirectorApprove
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxSubmitDirectorApprove() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		filehandleService.directorApprove(filePO, userInfo, imageUrls);
	}

	/**
	 * @description 检查部门是否已有负责人
	 * @method ajaxCheckDept
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxCheckDept() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		String ids = request.getParameter("deptIds");
		if (AssertUtil.isEmpty(ids)) {
			setActionResult("1001", "需要上传的参数为空！");
			return;
		}
		deptIds = ids.split(",");
		//判断所选择的部门是否已有负责人
		for (String deptId : deptIds) {
			String dept[] = deptId.split("\\|");
			List<TbQyUserInfoVO> list = this.departmentMgrService.getDeptReceiveList(dept[0], userInfo.getOrgId());
			if (list == null || list.size() <= 0) {
				setActionResult("1001", "部门「" + dept[1] + "」还没有设置负责人，请先到管理后台设置该部门的负责人！");
				return;
			}
		}
	}


	/**
	 * @description 文件上传人转发文件
	 * @method ajaxRepostFile
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxRepostFile() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		if (AssertUtil.isEmpty(deptIds)) {
			setActionResult("1001", "需要上传的参数为空！");
			return;
		}
		deptIds = deptIds[0].split(",");
		List<TbQyUserInfoVO> handlerList = new ArrayList<TbQyUserInfoVO>();
		//判断所选择的部门是否已有负责人
		for (String deptId : deptIds) {
			String dept[] = deptId.split("\\|");
			List<TbQyUserInfoVO> list = this.departmentMgrService.getDeptReceiveList(dept[0], userInfo.getOrgId());
			if (list == null || list.size() <= 0) {
				setActionResult("1001", "部门「" + dept[1] + "」还没有设置负责人，请先到管理后台设置该部门的负责人！");
				return;
			}
			handlerList.addAll(list);
		}
		filehandleService.transformFile(filePO, userInfo, handlerList);
	}

	/**
	 * @description 部门负责人操作流程
	 * @method ajaxInchargeHandleFile
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxInchargeHandleFile() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		if (AssertUtil.isEmpty(fileId) || AssertUtil.isEmpty(handleType) || (AssertUtil.isEmpty(relatives) && "1".equals(handleType))) {
			setActionResult("1001", "需要传入的参数为空！");
			return;
		}
		filehandleService.inchargeHandleFile(fileId, handleType, relatives, userInfo);
	}

	/**
	 * @description 根据身份获取详情
	 * @method ajaxGetDetail
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxGetDetail() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String fileId = request.getParameter("fileId");
		if (AssertUtil.isEmpty(fileId)) {
			setActionResult("1001", "需要传入的参数为空！");
			return;
		}
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(request);
		//获取当前身份
		int i = filehandleService.getFileIdentify(fileId, userInfo.getUserId());
		if (i == 5) {
			setActionResult("1001", "您无权限查看此数据！");
			return;
		}
		addJsonObj("identify", i);
		//获取详情
		TbYsjdFileVO fileVO = filehandleService.getDetail(fileId);
		addJsonObj("fileVO", fileVO);
		//如果是负责人，则看他是否已处理
		if ("2".equals(fileVO.getStatus()) && i == 3) {
			boolean b = filehandleService.isInchargeHandleFile(fileId, userInfo.getUserId());
			addJsonObj("b", b);
			//获取他所有的部门
		}
		//获取流程
		List<TbYsjdFileFlowVO> flowList = filehandleService.getFlowList(fileId);
		addJsonArray("flowList", flowList);
		//获取评论
		HashMap map = new HashMap();
		map.put("fileId", fileId);
		Pager comments = new Pager(ServletActionContext.getRequest(), getPageSize());
		comments = filehandleService.getDiaryComment(map, comments);
		Collection pageData = comments.getPageData();
		if (!AssertUtil.isEmpty(pageData)) {
			addJsonArray("comments", new ArrayList(pageData));
			addJsonObj("hasMore", !pageData.isEmpty() && comments.getTotalRows() > (long) pageData.size());
		}
		//获取附件
		HashMap paramMap = new HashMap();
		paramMap.put("orgId", userInfo.getOrgId());
		paramMap.put("groupId", fileId);
		paramMap.put("groupType", 1);
		addJsonArray("mediaList", fileMediaService.getMediaByGroupId(paramMap));
		//增加已阅
		if(!userInfo.getUserId().equals(fileVO.getCreateUserId())){
			filehandleService.readUser(userInfo.getUserId(), fileId);
		}
	}


	/**
	 * @description 获取这个人部门，及其子部门下的人员
	 * @method ajaxGetUserListByOrgID
	 * @time 2017-8-25
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "根据机构ID获取该机构下的所有用户失败"))
	public void ajaxGetUserListByOrgID() throws Exception, BaseException {
		String agentCode = ConstantUtil.SEND_MSG_AGENT_CODE;
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(user.getCorpId(), agentCode);
		Pager pager = new Pager(ServletActionContext.getRequest(), 10);
		HttpServletRequest request = ServletActionContext.getRequest();
		HashMap params = new HashMap();
		String sortTop = request.getParameter("sortTop");
		if (!AssertUtil.isEmpty(sortTop)) {
			params.put("sortTop", sortTop);
		}

		if (aci != null && aci.isAllUserVisible()) {
			addJsonObj("isRangeAll", Boolean.TRUE);
			pager = filehandleService.findAllUserByUser(pager, user, params);
		} else {
			if (aci == null || !VisibleRangeUtil.isUserVisibleAgentByParts(user.getUserId(), user.getDeptFullNames(), agentCode, user.getOrgId(), aci.getPartys())) {
				throw new NonePrintException("105", "尊敬的用户，你不在应用的可见范围，请联系管理员！");
			}

			if (aci.isAllUserUsable()) {
				addJsonObj("isRangeAll", Boolean.TRUE);
				pager = filehandleService.findAllUserByUser(pager, user, params);
			} else {
				addJsonObj("isRangeAll", Boolean.FALSE);
				params.put("agentCode", agentCode);
				pager = filehandleService.findAllUserByUser(pager, user, params);
			}
		}

		pager = SecrecyUserUtil.secrecyPage(user.getOrgId(), pager);
		if (Configuration.IS_QIWEIYUN) {
			if (Configuration.BIG_ORG_NUMBER < OrgUtil.getUserTotal(user.getOrgId(), 0)) {
				addJsonObj("superBigOrg", Boolean.TRUE);
			} else {
				addJsonObj("superBigOrg", Boolean.FALSE);
			}
		} else {
			addJsonObj("superBigOrg", Boolean.FALSE);
		}

		addJsonObj("showMsgBtn", Boolean.TRUE);
		addJsonPager("pageData", pager);
		addJsonObj("isDisplayMobilel", selectUserService.getSetFiled(user.getOrgId()));
		addJsonObj("selectPO", contactMgrService.getUserDefaultByOrgId(user.getOrgId()));
	}


	/**
	 * @description 删除评论
	 * @method deleteComment
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "删除成功", faileMsg = "删除失败"))
	public void deleteComment() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String commentId = request.getParameter("commentId");
		TbYsjdFileCommentPO po = filehandleService.searchByPk(TbYsjdFileCommentPO.class, commentId);
		if (null != po) {
			TbYsjdFilePO xxPO = filehandleService.searchByPk(TbYsjdFilePO.class, po.getId());
			if (null == xxPO) {
				setActionResult("1001", "该文件处理已被删除！");
			} else {
				xxPO.setCommentCount(xxPO.getCommentCount() - 1);
				filehandleService.updatePO(xxPO, false);
				filehandleService.updateCommentStatus(commentId);
			}
		}
	}

	/**
	 * @description 提交评论
	 * @method commitComment
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "提交成功", faileMsg = "提交失败"))
	public void commitComment(@InterfaceParam(name = "type") String type) throws Exception, BaseException {
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		if (null == tbQyDiaryCommentPO || AssertUtil.isEmpty(content) || AssertUtil.isEmpty(tbQyDiaryCommentPO.getFileId())) {
			setActionResult("1001", "需要传入的参数为空！");
			return;
		}
		tbQyDiaryCommentPO.setContent(content);
		filePO = filehandleService.searchByPk(TbYsjdFilePO.class, tbQyDiaryCommentPO.getFileId());
		if (null == filePO) {
			setActionResult("1001", "该数据已被删除！");
			return;
		}
		List list = filehandleService.getCommentsByUserID(userInfo.getUserId(), tbQyDiaryCommentPO.getId());
		if (!AssertUtil.isEmpty(list)) {
			TbYsjdFileCommentPO commentId = (TbYsjdFileCommentPO) list.get(0);
			if ((new Date()).getTime() - commentId.getCreateTime().getTime() <= (long) (Configuration.COMMENT_INTERVAL * 1000)) {
				return;
			}
		}

		String commentId1 = UUID.randomUUID().toString();
		tbQyDiaryCommentPO.setId(commentId1);
		tbQyDiaryCommentPO.setCreatePerson(userInfo.getUserId());
		tbQyDiaryCommentPO.setPersonName(userInfo.getPersonName());
		tbQyDiaryCommentPO.setHeadPic(userInfo.getHeadPic());
		tbQyDiaryCommentPO.setWxUserId(userInfo.getWxUserId());
		tbQyDiaryCommentPO.setDepartmentId(userInfo.getDeptIdsForRedundancy());
		tbQyDiaryCommentPO.setDepartmentName(userInfo.getDeptFullNames());
		tbQyDiaryCommentPO.setCreateTime(new Date());
		tbQyDiaryCommentPO.setStatus("0");
		tbQyDiaryCommentPO.setType(type);
		tbQyDiaryCommentPO.setIsCreator(filePO.getCreateUserId().equals(userInfo.getUserId()) ? 1 : 0);
		filehandleService.insertPO(tbQyDiaryCommentPO, false);
		filehandleService.updateCommentCount(tbQyDiaryCommentPO.getFileId());
		filehandleService.insertAtRecord(tbQyDiaryCommentPO, filePO, userInfo, userIds);
		addJsonObj("commentId", commentId1);
	}

	/**
	 * @description 查看更多评论
	 * @method listMoreComment
	 * @time 2017-8-22
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listMoreComment(@InterfaceParam(name = "commentStatus") String commentStatus) throws Exception, BaseException {
		HashMap searchMap = new HashMap();
		if (!StringUtil.isNullEmpty(commentStatus) && "2".equals(commentStatus)) {
			searchMap.put("status2", "2");
			searchMap.put("status3", "3");
		}

		TbYsjdFileCommentPO lastComment = filehandleService.searchByPk(TbYsjdFileCommentPO.class, lastCommentId);
		if (null != lastComment) {
			searchMap.put("fileId", lastComment.getFileId());
			searchMap.put("lastCreateTime", lastComment.getCreateTime());
			Pager comments = new Pager(ServletActionContext.getRequest(), getPageSize());
			comments = filehandleService.getDiaryComment(searchMap, comments);
			Collection pageData = comments.getPageData();
			if (!AssertUtil.isEmpty(pageData)) {
				addJsonArray("comments", new ArrayList(pageData));
				addJsonObj("hasMore", !pageData.isEmpty() && comments.getTotalRows() > (long) pageData.size());
			}
		}
	}


	/**
	 * @description    只看评论
	 * @method          listComment
	 * @time            2017-8-31
	 * @author          lwj
	 * @version         V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listComment(@InterfaceParam(name = "commentStatus") String commentStatus) throws Exception, BaseException {
		HashMap searchMap = new HashMap();
		searchMap.put("fileId", fileId);
		if (!StringUtil.isNullEmpty(commentStatus) && "2".equals(commentStatus)) {
			searchMap.put("status2", "2");
			searchMap.put("status3", "3");
		}

		Pager comments = new Pager(ServletActionContext.getRequest(), getPageSize());
		comments = filehandleService.getDiaryComment(searchMap, comments);
		Collection pageData = comments.getPageData();
		if (!AssertUtil.isEmpty(pageData)) {
			addJsonArray("comments", new ArrayList(pageData));
			addJsonObj("hasMore", !pageData.isEmpty() && comments.getTotalRows() > (long) pageData.size());
		}

		if (null == filePO) {
			filePO = new TbYsjdFilePO();
		}

		filePO.setCommentCount((int) comments.getTotalRows());
		addJsonFormateObj("tbTaskinfo", filePO);
	}

	//get set
	@Resource
	public void setFilehandleService(IFilehandleService filehandleService) {
		this.filehandleService = filehandleService;
	}

	public void setFilePO(TbYsjdFilePO filePO) {
		this.filePO = filePO;
	}

	public void setRelatives(String relatives) {
		this.relatives = relatives;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

	public void setIncharges(String[] incharges) {
		this.incharges = incharges;
	}

	public void setTbQyDiaryCommentPO(TbYsjdFileCommentPO tbQyDiaryCommentPO) {
		this.tbQyDiaryCommentPO = tbQyDiaryCommentPO;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public void setMediaIds(String[] mediaIds) {
		this.mediaIds = mediaIds;
	}

	@Resource
	public void setFileMediaService(IFileMediaService fileMediaService) {
		this.fileMediaService = fileMediaService;
	}

	public void setApproverUserId(String approverUserId) {
		this.approverUserId = approverUserId;
	}

	public void setApproverHeadPic(String approverHeadPic) {
		this.approverHeadPic = approverHeadPic;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public void setApproverWxUserId(String approverWxUserId) {
		this.approverWxUserId = approverWxUserId;
	}

	public void setLastCommentId(String lastCommentId) {
		this.lastCommentId = lastCommentId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TbYsjdFilePO getFilePO() {
		return filePO;
	}

	public String getRelatives() {
		return relatives;
	}

	public String[] getImageUrls() {
		return imageUrls;
	}

	public String[] getIncharges() {
		return incharges;
	}

	public TbYsjdFileCommentPO getTbQyDiaryCommentPO() {
		return tbQyDiaryCommentPO;
	}

	public String getUserIds() {
		return userIds;
	}

	public String[] getMediaIds() {
		return mediaIds;
	}

	public String getApproverUserId() {
		return approverUserId;
	}

	public String getApproverHeadPic() {
		return approverHeadPic;
	}

	public String getApprover() {
		return approver;
	}

	public String getApproverWxUserId() {
		return approverWxUserId;
	}

	public String getLastCommentId() {
		return lastCommentId;
	}

	public String getFileId() {
		return fileId;
	}

	public String getContent() {
		return content;
	}

	@Resource
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	public String[] getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String[] deptIds) {
		this.deptIds = deptIds;
	}

	public String getHandleType() {
		return handleType;
	}

	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	@Resource
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}

	@Resource
	public void setSelectUserService(ISelectUserMgrService selectUserService) {
		this.selectUserService = selectUserService;
	}
}
