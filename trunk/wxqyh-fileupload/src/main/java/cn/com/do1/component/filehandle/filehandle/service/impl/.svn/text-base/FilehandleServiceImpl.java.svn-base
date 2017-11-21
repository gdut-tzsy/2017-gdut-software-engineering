package cn.com.do1.component.filehandle.filehandle.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserRedundancyInfoVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.filehandle.filehandle.dao.IFilehandleDAO;
import cn.com.do1.component.filehandle.filehandle.model.*;
import cn.com.do1.component.filehandle.filehandle.service.IFilehandleService;
import cn.com.do1.component.filehandle.filehandle.util.ConstantUtil;
import cn.com.do1.component.filehandle.filehandle.util.InitMap;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileFlowVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileRecipientVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileVO;
import cn.com.do1.component.uploadfile.imageupload.service.IFileMediaService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.EmojiUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
@Service("filehandleService")
class FilehandleServiceImpl extends BaseService implements IFilehandleService {
	private final static transient Logger logger = LoggerFactory.getLogger(FilehandleServiceImpl.class);

	private IFilehandleDAO filehandleDAO;
	private IContactService contactService;
	private IFileMediaService fileMediaService;
	private IDepartmentService departmentService;


	@Override
	public TbYsjdDirectorPO getApproveSetting(String type) throws Exception, BaseException {
		String sql = "select * from tb_ysjd_director where type = :type";
		filehandleDAO.preparedSql(sql);
		filehandleDAO.setPreValue("type", type);
		return filehandleDAO.executeQuery(TbYsjdDirectorPO.class);
	}

	@Override
	public Pager getFileHandlePager(Map map, Pager pager) throws Exception, BaseException {
		pager = filehandleDAO.getFileHandlePager(map, pager);
		Collection pageData = pager.getPageData();
		if (!AssertUtil.isEmpty(pageData)) {
			List list = new ArrayList(pageData);
			Iterator i$;
			if (list.size() > 0) {
				for (i$ = list.iterator(); i$.hasNext(); ) {
					Object obj = i$.next();
					TbYsjdFileVO vo = (TbYsjdFileVO) obj;
					vo.setStatusDesc(InitMap.getStatusDescMapValue(vo.getStatus()));
				}
			}
		}
		return pager;
	}

	@Override
	public List<TbQyUserInfoVO> getDirector() throws Exception, BaseException {
		TbYsjdDirectorPO po = getApproveSetting("0");
		if (po == null) {
			throw new NonePrintException("1001", "还没有设置任何主任审批，请先联系管理员设置！");
		}
		String[] userIds = po.getTarget().split("\\|");
		StringBuilder sb = new StringBuilder();
		for (String userId : userIds) {
			sb.append(",'").append(userId).append("'");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		List<TbQyUserInfoVO> list = filehandleDAO.getUserInfoListByUserId(sb.toString());
		if (list == null || list.size() <= 0) {
			throw new NonePrintException("1001", "设置的批阅领导已离职或不在通讯录中，请联系管理员！");
		}
		return list;
	}

	@Override
	public boolean isFileUploadAuth(String userId) throws Exception, BaseException {
		TbYsjdDirectorPO po = getApproveSetting("1");
		if (po == null) {
			//如果没有设置，默认所有人都能上传
			return true;
		}
		if ("1".equals(po.getRanges())) {
			return true;
		}
		if (!AssertUtil.isEmpty(po.getTarget()) && po.getTarget().contains(userId)) {
			return true;
		}
		//校验这个用户是否在选择的部门
		if (!AssertUtil.isEmpty(po.getDept())) {
			//获取用户的部门
			List<String> deptList = filehandleDAO.getUserDept(userId);
			if (deptList != null && deptList.size() > 0) {
				//获取所有权限的部门，判断用户当前部门是否包含在其中
				String dept = getChildDept(po.getDept());
				for (String s : deptList) {
					if (dept.contains(s)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getChildDept(String deptIds) throws Exception, BaseException {
		if (AssertUtil.isEmpty(deptIds)) {
			throw new BaseException("1001", "传入的部门id为空");
		}
		if (deptIds.startsWith("|")) {
			deptIds = deptIds.substring(1, deptIds.length());
		}
		if (!deptIds.endsWith("|")) {
			deptIds += "|";
		}
		StringBuilder deptStr = new StringBuilder(deptIds);
		//先拼装这些id用来查询数据库
		String str = deptIds.replace("\\|", "',");
		str = str.substring(0, str.length() - 1);
		str = "'" + str + "'";

		//递归获取这些部门的子部门
		List<String> list = recursiveChildDept(str);
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String s : list) {
				sb.append(s).append("|");
			}
			deptStr.append(sb);
		}
		return deptStr.toString();
	}

	@Override
	public void addFile(TbYsjdFilePO filePO, UserInfoVO userInfo, String relatives, String[] mediaIds) throws Exception, BaseException {
		filePO.setId(getUUID());
		filePO.setCreateHeadPic(userInfo.getHeadPic());
		filePO.setCreateTime(new Date());
		filePO.setCreateUserId(userInfo.getUserId());
		filePO.setCreateWxUserId(userInfo.getWxUserId());
		filePO.setCreateUserName(userInfo.getPersonName());
		filePO.setStatus("0");
		filePO.setOrgId(userInfo.getOrgId());
		filePO.setCorpId(userInfo.getCorpId());
		filePO.setRelatedPerson(relatives);
		filePO.setCommentCount(0);
		insertPO(filePO, false);
		//保存相关人
		if (!AssertUtil.isEmpty(relatives)) {
			relatives = insertRecipient(filePO, relatives, "1");
		}

		//保存流程
		String flowDesc = userInfo.getPersonName() + InitMap.getFlowStatusMapValue(filePO.getStatus());
		recordFlow(filePO, userInfo, flowDesc);

		//增加下一步处理人的流程
		flowDesc = "等待" + filePO.getApprover() + "批阅。";
		recordNextHandleFlow(filePO, userInfo, flowDesc);

		//保存附件
		if (mediaIds != null && mediaIds.length > 0) {
			this.fileMediaService.startMediaStatus(mediaIds, filePO.getId(), filePO.getOrgId(), 1);
		}

		//发送给领导审批
		String url = ConstantUtil.FILE_HANDLE_DETAIL_URL + filePO.getId();
		String desc = userInfo.getPersonName() + "上传了「" + filePO.getTitle() + "」，需要您的批示，请尽快处理！";
		String title = "文件批示通知";
		sendMessage(filePO.getApproverWxUserId(), title, desc, url, userInfo.getCorpId(), userInfo.getOrgId());

		//发给相关人
		if (!AssertUtil.isEmpty(relatives)) {
			sendMsgToCC(relatives, filePO, url);
		}
	}

	@Override
	public TbYsjdFilePO getFileDetail(String fileId) throws Exception, BaseException {
		return null;
	}

	@Override
	public int getFileIdentify(String fileId, String userId) throws Exception, BaseException {
		if (AssertUtil.isEmpty(fileId) || AssertUtil.isEmpty(userId)) {
			throw new BaseException("1001", "传入的参数为空！");
		}
		TbYsjdFilePO filePO = searchByPk(TbYsjdFilePO.class, fileId);
		if (filePO == null) {
			throw new NonePrintException("1001", "该数据已被删除！");
		}
		if (userId.equals(filePO.getCreateUserId())) {
			return 0;
		}
		if (userId.equals(filePO.getApproverUserId())) {
			return 1;
		}
		if (filehandleDAO.isHandler(fileId, userId)) {
			return 3;
		}
		if (filehandleDAO.isRelevant(fileId, userId)) {
			return 2;
		}
		if (!AssertUtil.isEmpty(filePO.getCommonPerson()) && filePO.getCommonPerson().contains(userId)) {
			return 4;
		}
		return 5;
	}

	@Override
	public void directorApprove(TbYsjdFilePO filePO, UserInfoVO userInfo, String[] imageUrls) throws Exception, BaseException {
		//更新主表
		updatePO(filePO, false);

		if (!AssertUtil.isEmpty(imageUrls)) {
			//保存上传的图片
			insertPicAttach(filePO.getId(), userInfo.getUserId(), imageUrls);
		}

		//保存流程信息
		String flowDesc = userInfo.getPersonName() + InitMap.getFlowStatusMapValue(filePO.getStatus()) + filePO.getApproveContent();
		recordFlow(filePO, userInfo, flowDesc);

		//更新未完成的流程
		flowDesc = "等待" + filePO.getCreateUserName() + "处理。";
		recordNextHandleFlow(filePO, userInfo, flowDesc);

		//发送提醒消息
		String url = ConstantUtil.FILE_HANDLE_DETAIL_URL + filePO.getId();
		String desc = "您上传的「" + filePO.getTitle() + "」有了批示结果，请进行下一步操作！";
		String title = "文件批示结果通知";
		sendMessage(filePO.getCreateWxUserId(), title, desc, url, userInfo.getCorpId(), userInfo.getOrgId());
	}

	@Override
	public void transformFile(TbYsjdFilePO filePO, UserInfoVO userInfo, List<TbQyUserInfoVO> handlerList) throws Exception, BaseException {
		StringBuilder inchargesUserId = new StringBuilder();

		//流程描述
		StringBuilder inchargeUserName = new StringBuilder();
		for (TbQyUserInfoVO userInfoVO : handlerList) {
			//获取这个用户的部门
			String allDeptName = filehandleDAO.getUserAllDeptName(userInfoVO.getUserId());
			String temp = "";
			if (!AssertUtil.isEmpty(allDeptName)) {
				temp = "（" + allDeptName + "）";
			}
			inchargeUserName.append("、").append(userInfoVO.getPersonName()).append(temp);
			inchargesUserId.append(userInfoVO.getUserId()).append("|");
		}
		inchargeUserName.deleteCharAt(0);
		String flowDesc = userInfo.getPersonName() + InitMap.getFlowStatusMapValue(filePO.getStatus()) + inchargeUserName.toString() + "。";

		//更新主表
		filePO.setIncharges(inchargesUserId.toString());
		updatePO(filePO, false);

		filePO = searchByPk(TbYsjdFilePO.class, filePO.getId());

		//保存流程信息
		recordFlow(filePO, userInfo, flowDesc);

		//更新下一步等待未完成的信息
		flowDesc = "等待" + inchargeUserName.toString() + "处理。";
		recordNextHandleFlow(filePO, userInfo, flowDesc);

		//保存部门负责人
		String wxUserIds = recordIncharge(filePO.getId(), handlerList);

		//发送消息提醒
		String title = "文件处理通知";
		String desc = filePO.getCreateUserName() + "上传的「" + filePO.getTitle() + "」需要您去处理，点击查看详情！";
		String url = ConstantUtil.FILE_HANDLE_DETAIL_URL + filePO.getId();
		sendMessage(wxUserIds, title, desc, url, filePO.getCorpId(), filePO.getOrgId());
	}

	@Override
	public void inchargeHandleFile(String fileId, String handleType, String transformPerson, UserInfoVO userInfo) throws Exception, BaseException {
		//获取文件信息
		TbYsjdFilePO filePO = searchByPk(TbYsjdFilePO.class, fileId);
		if (filePO == null) {
			throw new BaseException("1001", "该文件处理已被删除！");
		}

		//获取负责人的详情
		TbYsjdFileHandleInfoPO handlePO = filehandleDAO.getInchargeHandleInfo(userInfo.getUserId(), fileId);
		if (handlePO == null) {
			throw new BaseException("1001", "没有需要您处理的文件！");
		}

		StringBuilder wxUserId = new StringBuilder();    //推送消息的wxUserId
		StringBuilder userId = new StringBuilder();        //保存到主表和负责人处理信息表的转发给普通人员的userId

		//获取这个用户的部门
		String allDeptName = filehandleDAO.getUserAllDeptName(userInfo.getUserId());
		String temp = "";
		if (!AssertUtil.isEmpty(allDeptName)) {
			temp = "（" + allDeptName + "）";
		}

		//获取流程信息描述
		String flowDesc;
		if ("1".equals(handleType)) {//转发
			StringBuilder sb = new StringBuilder();
			String[] users = transformPerson.split(",");
			//获取转发的普通人员信息
			for (String incharge : users) {
				sb.append(",'").append(incharge).append("'");
			}
			if (sb.length() <= 0) {
				throw new BaseException("1001", "所需要转发的人员为空！");
			}
			sb.deleteCharAt(0);
			//转发给普通人员的list
			List<TbQyUserInfoVO> userInfoVOList = filehandleDAO.getUserInfoListByUserId(sb.toString());
			if (userInfoVOList == null || userInfoVOList.size() <= 0) {
				throw new BaseException("1001", "所需要转发的人员已离职或不在通讯录中，请检查！");
			}
			StringBuilder userName = new StringBuilder();    //拼装流程描述
			for (TbQyUserInfoVO userInfoVO : userInfoVOList) {
				//获取这个用户的部门
				String allDeptName1 = filehandleDAO.getUserAllDeptName(userInfoVO.getUserId());
				String temp1 = "";
				if (!AssertUtil.isEmpty(allDeptName1)) {
					temp1 = "（" + allDeptName1 + "）";
				}

				userName.append("、").append(userInfoVO.getPersonName()).append(temp1);
				wxUserId.append(userInfoVO.getWxUserId()).append("|");
				userId.append(userInfoVO.getUserId()).append("|");
			}
			userName.deleteCharAt(0);
			flowDesc = userInfo.getPersonName() + temp + "转发了文件给：" + userName + "。";

			//保存转发给普通人员的信息
			filePO.setCommonPerson(filePO.getCommonPerson() + userId);
		} else {//直接结束
			flowDesc = userInfo.getPersonName() + temp + "没有转发，并结束了流程。";
		}

		//保存流程信息
		recordFlow(filePO, userInfo, flowDesc);


		//更新负责人的处理状态
		handlePO.setIsHandle("1");
		handlePO.setFlowDesc(flowDesc);
		handlePO.setForwardUser(userId.toString());
		handlePO.setHandleTime(new Date());
		handlePO.setIsEnd(handleType);
		updatePO(handlePO, false);

		//更新主表
		filePO.setHandledUser(userInfo.getUserId() + "|");
		if (!filehandleDAO.isAllInchargeHandle(fileId)) {
			filePO.setStatus("3");
			//删掉未完成流程
			filehandleDAO.deleteNextFlow(fileId);
		} else {
			//更新未完成的流程提示
			TbYsjdFileFlowPO flowPO = filehandleDAO.getNextFlow(fileId);
			//获取还未完成的人员
			List<TbQyUserInfoVO> notHandleUserList = filehandleDAO.getNotHandleIncharge(fileId);
			if(notHandleUserList == null || notHandleUserList.size() <= 0){
				filePO.setStatus("3");
				//删掉未完成流程
				filehandleDAO.deleteNextFlow(fileId);
			}else{
				StringBuilder userName = new StringBuilder();    //拼装流程描述
				for (TbQyUserInfoVO userInfoVO : notHandleUserList) {
					//获取这个用户的部门
					String allDeptName1 = filehandleDAO.getUserAllDeptName(userInfoVO.getUserId());
					String temp1 = "";
					if (!AssertUtil.isEmpty(allDeptName1)) {
						temp1 = "（" + allDeptName1 + "）";
					}
					userName.append("、").append(userInfoVO.getPersonName()).append(temp1);
				}
				userName.deleteCharAt(0);
				userName.insert(0, "等待").append("处理。");
				flowPO.setFlowDesc(userName.toString());
				updatePO(flowPO, false);
			}
		}
		updatePO(filePO, false);

		//发送消息提醒
		if (!AssertUtil.isEmpty(wxUserId) && "1".equals(handleType)) {
			//发送消息提醒
			String title = "文件处理通知";
			String desc = "您收到一个文件处理「" + filePO.getTitle() + "」，请点击查看详情！";
			String url = ConstantUtil.FILE_HANDLE_DETAIL_URL + filePO.getId();
			sendMessage(wxUserId.toString(), title, desc, url, filePO.getCorpId(), filePO.getOrgId());
		}
	}

	@Override
	public TbYsjdFileVO getDetail(String id) throws Exception, BaseException {
		TbYsjdFileVO fileVO = filehandleDAO.getFileDetail(id);
		if (fileVO == null) {
			throw new NonePrintException("1001", "该数据已被删除！");
		}
		//相关人
		List<TbYsjdFileRecipientVO> cclist = this.filehandleDAO.getDiaryRecipient(id, "1");
		if (!AssertUtil.isEmpty(cclist)) {
			fileVO.setCcPersons(cclist);
		}
		fileVO.setStatusDesc(InitMap.getStatusDescMapValue(fileVO.getStatus()));
		fileVO.setDataStatus(InitMap.getDataStatusMapValue(fileVO.getStatus()));
		return fileVO;
	}

	@Override
	public Pager getFilePage(Map map, Pager pager) throws Exception, BaseException {
		TbYsjdDirectorPO po = getApproveSetting("0");
		if (po != null && po.getTarget().contains(map.get("userId").toString().replace("%", ""))) {
			map.put("isLeader", true);
		}
		pager = filehandleDAO.getFilePage(map, pager);
		Collection pageData = pager.getPageData();
		if (!AssertUtil.isEmpty(pageData)) {
			List list = new ArrayList(pageData);
			Iterator i$;
			if (list.size() > 0) {
				for (i$ = list.iterator(); i$.hasNext(); ) {
					Object obj = i$.next();
					TbYsjdFileVO cvo = (TbYsjdFileVO) obj;
					cvo.setStatusDesc(InitMap.getStatusDescMapValue(cvo.getStatus()));
					cvo.setDataStatus(InitMap.getDataStatusMapValue(cvo.getStatus()));
				}
			}
		}
		return pager;
	}

	@Override
	public List<TbYsjdFileFlowVO> getFlowList(String fileId) throws Exception, BaseException {
		List<TbYsjdFileFlowVO> flowList = filehandleDAO.getFileFlow(fileId);
		for (TbYsjdFileFlowVO tbYsjdFileFlowVO : flowList) {
			if ("1".equals(tbYsjdFileFlowVO.getExistImages())) {//获取图片
				tbYsjdFileFlowVO.setCommentPic(filehandleDAO.getFlowPic(tbYsjdFileFlowVO.getFileId(), tbYsjdFileFlowVO.getCreateUserId()));
			}
		}
		return flowList;
	}

	@Override
	public boolean isInchargeHandleFile(String fileId, String userId) throws Exception, BaseException {
		return filehandleDAO.isInchargeHandleFile(fileId, userId);
	}

	@Override
	public Pager findAllUserByUser(Pager pager, UserInfoVO user, Map<String, Object> params) throws Exception, BaseException {
		String orgId = user.getOrgId();
		HashMap map = new HashMap();
		map.put("orgId", orgId);
		map.put("aliveStatus", "-1");
		if (!AssertUtil.isEmpty(params.get("sortTop"))) {
			map.put("sortTop", params.get("sortTop").toString());
		}

		List<TbDepartmentInfoVO> depts = this.departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			pager = filehandleDAO.findAlluserByDeptId(map, pager, depts, user.getUserId());
//			UserEduUtil.addChildrenToVO(pager, depts, user);
		} else {
			pager = filehandleDAO.searchContactByPy(map, pager, user.getUserId());
//			UserEduUtil.addChildrenToVO(pager, null, user);
		}

		return pager;
	}

	@Override
	public void updateCommentStatus(String commentId) throws BaseException, Exception {
		filehandleDAO.updateCommentStatus(commentId);
	}

	@Override
	public List<TbYsjdFileCommentPO> getCommentsByUserID(String userId, String diaryId) throws BaseException, Exception {
		return filehandleDAO.getCommentsByUserID(userId, diaryId);
	}

	@Override
	public void updateCommentCount(String diaryId) throws BaseException, Exception {
		String sql = "UPDATE tb_ysjd_file set comment_count = comment_count+1 where ID=:diaryId";
		filehandleDAO.preparedSql(sql);
		filehandleDAO.setPreValue("diaryId", diaryId);
		filehandleDAO.executeUpdate();
	}

	@Override
	public void insertAtRecord(TbYsjdFileCommentPO tbQyDiaryCommentPO, TbYsjdFilePO tbQyDiaryPO, UserInfoVO sendUser, String atPersons) throws BaseException, Exception {
		String diaryId = tbQyDiaryCommentPO.getFileId();
		String userId = sendUser.getUserId();
		StringBuilder send = new StringBuilder();
		if (!StringUtil.isNullEmpty(atPersons)) {
			String[] creatorUser = atPersons.split("\\|");
			List myContent = filehandleDAO.getDiaryRecipient(diaryId, "0");
			HashSet<String> newsMessageVO = new HashSet<String>();
			int temp;
			if (!AssertUtil.isEmpty(myContent)) {
				temp = myContent.size();

				for (Object aMyContent : myContent) {
					TbYsjdFileRecipientVO userMap = (TbYsjdFileRecipientVO) aMyContent;
					newsMessageVO.add(userMap.getUserId());
				}
			} else {
				temp = 0;
			}

			List var25 = filehandleDAO.getDiaryRecipient(diaryId, "1");
			if (!AssertUtil.isEmpty(var25)) {

				for (Object aVar25 : var25) {
					TbYsjdFileRecipientVO myContent1 = (TbYsjdFileRecipientVO) aVar25;
					newsMessageVO.add(myContent1.getWxUserId());
				}
			}

			newsMessageVO.add(tbQyDiaryPO.getCreateWxUserId());
			Map var27 = contactService.getUserRedundancyListByUserId(creatorUser);

			StringBuilder userIds = new StringBuilder();

			for (String person : creatorUser) {
				if (!"".equals(person)) {
					UserRedundancyInfoVO userVO = (UserRedundancyInfoVO) var27.get(person);
					if (userVO != null && sendUser.getOrgId().equals(userVO.getOrgId()) && tbQyDiaryCommentPO.getContent().contains("@" + userVO.getPersonName())) {
						send.append("|").append(userVO.getWxUserId());
						TbYsjdFileCommentExtPO po = new TbYsjdFileCommentExtPO();
						po.setCreatePerson(userId);
						po.setCommentId(tbQyDiaryCommentPO.getId());
						po.setCreateTime(new Date());
						po.setUserId(person);
						insertPO(po, true);
						contactService.updateCommonUser(userId, person, 1);
						boolean flag = newsMessageVO.add(person);
						if (flag) {
							//查看相关人是否已在里面
							if (filehandleDAO.isExistIncharge(tbQyDiaryPO.getId(), person.trim())) {
								continue;
							}
							TbYsjdFileRecipientPO per = new TbYsjdFileRecipientPO();
							per.setRecipientId(UUID.randomUUID().toString());
							per.setFileId(diaryId);
							per.setUserId(person.trim());
							per.setUserType("1");
							per.setCreatePerson(userId);
							per.setCreateTime(new Date());
							per.setSortNum(temp);
							per.setReadenStatus(0);
							per.setIsCreator(0);
							per.setPersonName(userVO.getPersonName());
							per.setHeadPic(userVO.getHeadPic());
							per.setWxUserId(userVO.getWxUserId());
							per.setDepartmentId(userVO.getDeptId());
							per.setDepartmentName(userVO.getDeptFullName());
							insertPO(per, true);

							userIds.append(person.trim()).append("|");

							++temp;
						}
					}
				}
			}


			//更新相关人到主表
			TbYsjdFilePO filePO = searchByPk(TbYsjdFilePO.class, tbQyDiaryPO.getId());
			if (filePO == null) {
				throw new NonePrintException("1001", "数据已被删除！");
			}
			filePO.setIncharges(filePO.getIncharges() + userIds);
			updatePO(filePO, false);

			if (send.length() > 0) {
				send.deleteCharAt(0);
				String var29 = tbQyDiaryCommentPO.getContent();
				var29 = EmojiUtil.replaceEmoji(var29, true, 30);
				NewsMessageVO var30 = WxMessageUtil.newsMessageVO.clone();
				var30.setTouser(send.toString());
				var30.setDuration("0");
				var30.setTitle(sendUser.getPersonName() + "评论了[" + tbQyDiaryPO.getTitle() + "]");
				var30.setDescription("「" + tbQyDiaryPO.getTitle() + "」有新评论啦:" + var29);
				var30.setUrl(Configuration.WX_PORT + ConstantUtil.FILE_HANDLE_DETAIL_URL + tbQyDiaryPO.getId());
				var30.setCorpId(sendUser.getCorpId());
				var30.setAgentCode(ConstantUtil.SEND_MSG_AGENT_CODE);
				var30.setOrgId(sendUser.getOrgId());
				WxMessageUtil.sendNewsMessage(var30);
			}
		}

		if (!userId.equals(tbQyDiaryPO.getCreateUserId())) {
			TbQyUserInfoVO var22 = contactService.findUserInfoByUserId(tbQyDiaryPO.getCreateUserId());
			if (send.indexOf(var22.getWxUserId()) < 0 && !"-1".equals(var22.getUserStatus())) {
				String var23 = tbQyDiaryCommentPO.getContent();
				if ("2".equals(tbQyDiaryCommentPO.getType())) {
					var23 = "发布了一张图片";
				}

				var23 = EmojiUtil.replaceEmoji(var23, true, 30);
				NewsMessageVO var24 = WxMessageUtil.newsMessageVO.clone();
				var24.setTouser(var22.getWxUserId());
				var24.setDuration("0");
				var24.setTitle(sendUser.getPersonName() + "评论了[" + tbQyDiaryPO.getTitle() + "]");
				var24.setDescription("「" + tbQyDiaryPO.getTitle() + "」有新评论啦:" + var23);
				var24.setUrl(Configuration.WX_PORT + ConstantUtil.FILE_HANDLE_DETAIL_URL + tbQyDiaryPO.getId());
				var24.setCorpId(sendUser.getCorpId());
				var24.setAgentCode(ConstantUtil.SEND_MSG_AGENT_CODE);
				var24.setOrgId(sendUser.getOrgId());
				WxMessageUtil.sendNewsMessage(var24);
			}
		}
	}

	@Override
	public Pager getDiaryComment(Map map, Pager comments) throws Exception, BaseException {
		return filehandleDAO.getDiaryComment(map, comments);
	}

	@Override
	public void readUser(String userId, String diaryId) throws BaseException, Exception {
		if (filehandleDAO.countDiaryComment(userId, diaryId) == 0) {
			UserRedundancyInfoVO userVO = this.contactService.getUserRedundancyInfoByUserId(userId);
			TbYsjdFileCommentPO po = new TbYsjdFileCommentPO();
			po.setId(getUUID());
			po.setContent("已阅");
			po.setCreateTime(new Date());
			po.setFileId(diaryId);
			po.setCreatePerson(userId);
			po.setPersonName(userVO.getPersonName());
			po.setHeadPic(userVO.getHeadPic());
			po.setWxUserId(userVO.getWxUserId());
			po.setDepartmentId(userVO.getDeptId());
			po.setDepartmentName(userVO.getDeptFullName());
			po.setStatus("2");
			po.setIsCreator(0);
			insertPO(po, false);
			updateCommentCount(diaryId);
			filehandleDAO.updateDiaryRec(userId, diaryId);
		}
	}

	/**
	 * @description 去掉、
	 * @method subStr
	 * @Param str
	 * @Param str1
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	private static String subStr(String str, String str1) {
		if (str.contains(str1 + "、")) {
			str = str.replace(str1 + "、", "");
		} else if (str.contains("、" + str1)) {
			str = str.replace("、" + str1, "");
		}
		return str;
	}

	/**
	 * @description 保存部门负责人
	 * @method recordIncharge
	 * @Param fileId
	 * @Param userInfoVOList
	 * @Return 返回wxUserId
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private String recordIncharge(String fileId, List<TbQyUserInfoVO> userInfoVOList) throws Exception, BaseException {
		if (userInfoVOList.size() <= 0) {
			throw new BaseException("1001", "负责人为空！");
		}
		StringBuilder sb = new StringBuilder();
		List<TbYsjdFileHandleInfoPO> list = new ArrayList<TbYsjdFileHandleInfoPO>();
		for (TbQyUserInfoVO userInfoVO : userInfoVOList) {
			sb.append("|").append(userInfoVO.getWxUserId());
			TbYsjdFileHandleInfoPO po = new TbYsjdFileHandleInfoPO();
			po.setId(getUUID());
			po.setIsHandle("0");
			po.setHandleUserName(userInfoVO.getPersonName());
			po.setHandleUserId(userInfoVO.getUserId());
			po.setCreateTime(new Date());
			po.setFileId(fileId);
			list.add(po);
		}
		filehandleDAO.execBatchInsert(list);
		sb.deleteCharAt(0);
		return sb.toString();
	}

	/**
	 * @description 保存流程
	 * @method recordFlow
	 * @Param filePO
	 * @Param userInfo
	 * @Param flowStatus
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private void recordFlow(TbYsjdFilePO filePO, UserInfoVO userInfo, String desc) throws Exception, BaseException {
		TbYsjdFileFlowPO po = new TbYsjdFileFlowPO();
		po.setId(getUUID());
		po.setFileId(filePO.getId());
		po.setCreateTime(new Date());
		po.setCreateUserId(userInfo.getUserId());
		po.setFlowStatus(filePO.getStatus());
		po.setUpdateTime(new Date());
		po.setUpdateUserId(userInfo.getUserId());
		po.setFlowDesc(desc);
		if ("1".equals(filePO.getStatus())) {
			po.setExistImages("1");
		} else {
			po.setExistImages("0");
		}
		po.setIsHandle("1");
		insertPO(po, false);
	}

	/**
	 * @description 保存下一步等待处理的流程
	 * @method recordFlow
	 * @Param filePO
	 * @Param userInfo
	 * @Param flowStatus
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private void recordNextHandleFlow(TbYsjdFilePO filePO, UserInfoVO userInfo, String desc) throws Exception, BaseException {
		if (!"0".equals(filePO.getStatus())) {
			TbYsjdFileFlowPO po = filehandleDAO.getNextFlow(filePO.getId());
			po.setFlowDesc(desc);
			po.setUpdateTime(new Date());
			po.setUpdateUserId(userInfo.getUserId());
			updatePO(po, false);
		} else {
			TbYsjdFileFlowPO po = new TbYsjdFileFlowPO();
			po.setId(getUUID());
			po.setFileId(filePO.getId());
			po.setCreateTime(DateUtil.addMinutes(new Date(), 1));
			po.setCreateUserId(userInfo.getUserId());
			po.setFlowStatus(filePO.getStatus());
			po.setUpdateTime(new Date());
			po.setUpdateUserId(userInfo.getUserId());
			po.setFlowDesc(desc);
			po.setIsHandle("0");
			insertPO(po, false);
		}
	}

	/**
	 * @description 发送消息给相关人
	 * @method relatives
	 * @Param filePO
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private void sendMsgToCC(String relatives, TbYsjdFilePO filePO, String url) throws Exception, BaseException {
		String title = "文件管理通知";
		String desc = filePO.getCreateUserName() + "给您发了一个「" + filePO.getTitle() + "」，快去看看吧~";
		sendMessage(relatives, title, desc, url, filePO.getCorpId(), filePO.getOrgId());
	}


	/**
	 * @description 保存领导批阅上传的图片
	 * @method insertPicAttach
	 * @Param id
	 * @Param userId
	 * @Param imageUrls
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private void insertPicAttach(String id, String userId, String[] imageUrls) throws BaseException, Exception {
		int temp = 0;
		for (String image : imageUrls) {
			TbYsjdFilePicPO po = new TbYsjdFilePicPO();
			po.setId(UUID.randomUUID().toString());
			po.setFileId(id);
			po.setPicPath(image.trim());
			po.setCreatePerson(userId);
			po.setCreateTime(new Date());
			po.setStatus("0");
			po.setSortNum(temp);
			insertPO(po, true);
		}
	}

	/**
	 * @description 保存相关人
	 * @method insertRecipient
	 * @Param tbQyDiaryPO
	 * @Param persons
	 * @Param type
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	private String insertRecipient(TbYsjdFilePO tbQyDiaryPO, String persons, String type) throws BaseException, Exception {
		if (!StringUtil.isNullEmpty(persons)) {
			int temp = 0;
			StringBuilder users = new StringBuilder();
			String[] personlist = persons.split(",");
			ArrayList<TbYsjdFileRecipientPO> inList = new ArrayList<TbYsjdFileRecipientPO>();

			for (String person : personlist) {
				UserRedundancyInfoVO userVO = contactService.getUserRedundancyInfoByUserId(person.trim());
				if (null != userVO) {
					users.append(",").append(userVO.getWxUserId());
					TbYsjdFileRecipientPO po = new TbYsjdFileRecipientPO();
					po.setRecipientId(getUUID());
					po.setFileId(tbQyDiaryPO.getId());
					po.setUserId(person.trim());
					po.setPersonName(userVO.getPersonName());
					po.setHeadPic(userVO.getHeadPic());
					po.setWxUserId(userVO.getWxUserId());
					po.setDepartmentId(userVO.getDeptId());
					po.setDepartmentName(userVO.getDeptFullName());
					po.setUserType(type);
					po.setReadenStatus(0);
					po.setIsCreator(tbQyDiaryPO.getCreateUserId().equals(userVO.getUserId()) ? 1 : 0);
					po.setCreatePerson(tbQyDiaryPO.getCreateUserId());
					po.setCreateTime(new Date());
					po.setSortNum(temp);
					inList.add(po);
					contactService.updateCommonUser(tbQyDiaryPO.getCreateUserId(), userVO.getUserId(), 1);
					++temp;
				}
			}

			if (!AssertUtil.isEmpty(inList) && inList.size() > 0) {
				filehandleDAO.execBatchInsert(inList);
			}
			if (users.length() > 0) {
				users.deleteCharAt(0);
				return users.toString();
			}
		}
		return "";
	}

	/**
	 * @description 微信消息推送
	 * @method sendMessage
	 * @Param sendUser
	 * @Param title
	 * @Param desc
	 * @Param url
	 * @Param corpId
	 * @Param orgId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	private void sendMessage(String sendUser, String title, String desc, String url, String corpId, String orgId) {
		//发送消息
		NewsMessageVO newsMessageVO = new NewsMessageVO();
		newsMessageVO.setTouser(sendUser);
		newsMessageVO.setTitle(title);
		newsMessageVO.setDescription(desc);
		if (!AssertUtil.isEmpty(url)) {
			newsMessageVO.setUrl(Configuration.WX_PORT + url);
		}
		newsMessageVO.setCorpId(corpId);
		newsMessageVO.setAgentCode(ConstantUtil.SEND_MSG_AGENT_CODE);
		newsMessageVO.setOrgId(orgId);
		WxMessageUtil.sendNewsMessage(newsMessageVO);
	}

	/**
	 * @description 递归获取子部门
	 * @method recursiveChildDept
	 * @Param deptIds
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	private List<String> recursiveChildDept(String deptIds) throws Exception, BaseException {
		//先获取传进来的部门id 的子部门
		List<String> list = filehandleDAO.getChildDept(deptIds);
		List<String> deptList = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			deptList.addAll(list);
			StringBuilder sb = new StringBuilder();
			for (String s : list) {
				sb.append(",'").append(s).append("'");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(0);
				recursiveChildDept(sb.toString());
			}
		}
		return deptList;
	}

	/**
	 * @description 获取主键
	 * @method getUUID
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	private String getUUID() throws Exception, BaseException {
		return UUID.randomUUID().toString().replace("-", "");
	}

	//get set
	@Resource
	public void setFilehandleDAO(IFilehandleDAO filehandleDAO) {
		this.filehandleDAO = filehandleDAO;
		setDAO(filehandleDAO);
	}

	@Resource
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	@Resource
	public void setFileMediaService(IFileMediaService fileMediaService) {
		this.fileMediaService = fileMediaService;
	}

	@Resource
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
}
