package cn.com.do1.component.contact.contact.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.contact.util.UserInfoChangeNotifier;
import cn.com.do1.component.util.ImportResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.contact.contact.util.ExcelUtil;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.dqdp.core.DqdpAppContext;

public class BatchDeparturesThread implements Runnable {
	private final static transient Logger logger = LoggerFactory
			.getLogger(BatchDeparturesThread.class);

	private String id;
	private String loginuser;
	private String orgId;
	private String corpId;
	private File upFile;
	private String upFileFileName;
	private String operationIp;
	private IContactService contactService;

	public BatchDeparturesThread(String id, String loginuser, String orgId,
			String orgCode, File upFile, String fileName, String corpId,
			String type, String operationIp) {
		this.id = id;
		this.loginuser = loginuser;
		this.orgId = orgId;
		this.corpId = corpId;
		this.upFile = upFile;
		this.upFileFileName = fileName;
		this.operationIp = operationIp;
		this.contactService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactService.class);
	}

	@Override
	public void run() {
		logger.debug("批量离职开始,orgId:" + orgId + ",corpId：" + corpId);
		TbQyUserInfoPO userPo = new TbQyUserInfoPO();
		int listNum = 1;// 行数
		ResultVO resultvo = new ResultVO();
		ImportResultUtil.putResultObject(id, resultvo);
		List<DeparturesVO> list = null;
		DeparturesErrorVO error = new DeparturesErrorVO();
		ImportResultUtil.putErrorObject(id, error);
		UserOrgVO org = null;
		List<TbQyUserInfoPO> userIds = new ArrayList<TbQyUserInfoPO>(100);
		try {
			org = contactService.getOrgByUserId(loginuser);
			corpId = org != null ? org.getCorpId() : "";
			orgId = org != null ? org.getOrgId() : "";
			userPo.setOrgId(orgId);

			// 错误信息
			List<String> errorlist = resultvo.getErrorlist();
			// 错误数据
			List<DeparturesVO> errlist = error.getErrorlist();
			try {
				// 获得excel数据
				list = ExcelUtil.importForExcel(upFile, upFileFileName,
						DeparturesVO.class, null, id, new ArrayList<TbQyUserCustomOptionVO>());
			} catch (Exception e) {
				resultvo.setTips("导入终止，错误提示：请检查导入数据的格式是否按照模板规范填写。");
			}
			
			// 处理导入的数据
			if (list != null && !list.isEmpty()) {
				resultvo.setTotalNum(list.size());
				int processNum = 0; // 已处理数
				if (errorlist == null) {
					errorlist = new ArrayList<String>();
					resultvo.setErrorlist(errorlist);
				}
				if (errlist == null) {
					errlist = new ArrayList<DeparturesVO>();
					error.setErrorlist(errlist);
				}
				TbQyUserInfoVO po;
				for (DeparturesVO departuresVO : list) {
					listNum++;
					try {
						// 用户名和电话不能同时为空
						if (AssertUtil.isEmpty(departuresVO.getWxUserId())
								&& AssertUtil.isEmpty(departuresVO.getMobile())) {
							errorlist.add("第" + listNum
									+ "行出现错误，错误提示：用户名和电话不能同时为空<br/>");
							departuresVO.setError("账号和电话不能同时为空");
							errlist.add(departuresVO);
							continue;
						}
						if (AssertUtil.isEmpty(departuresVO.getLeaveTime())) {
							errorlist.add("第" + listNum
									+ "行出现错误，错误提示：离职时间不能为空<br/>");
							departuresVO.setError("离职时间不能为空");
							errlist.add(departuresVO);
							continue;
						}
						if (!AssertUtil.isEmpty(departuresVO.getWxUserId())) { // 通过用户名去查询用户
							po = contactService.findUserInfoByWxUserId(
									departuresVO.getWxUserId(), corpId);
							if (null == po) { // 通过用户名查不到用户
								// 如果手机号也为空的话
								if (AssertUtil
										.isEmpty(departuresVO.getMobile())) {
									errorlist.add("第" + listNum
											+ "行出现错误，错误提示：系统中找不到对应用户<br/>");
									departuresVO.setError("系统中找不到对应需要离职的用户");
									errlist.add(departuresVO);
									continue;
								} else { // 如果手机号不为空
									po = contactService.getUserInfoByMobile(
											departuresVO.getMobile(), corpId);
									if (null == po) { // 手机号也查不到对应用户
										errorlist.add("第" + listNum
												+ "行出现错误，错误提示：系统中找不到对应用户<br/>");
										departuresVO
												.setError("系统中找不到对应需要离职的用户");
										errlist.add(departuresVO);
										continue;
									}
								}
							}
							TbQyUserInfoPO leaveUser = new TbQyUserInfoPO();
							leaveUser.setId(po.getId());
							leaveUser.setOrgId(orgId);
							leaveUser.setCorpId(corpId);
							leaveUser.setUserId(po.getUserId());
							leaveUser.setLeaveTime(departuresVO.getLeaveTime());
							leaveUser.setLeaveRemark(departuresVO
									.getLeaveRemark());
							leaveUser.setPersonName(po.getPersonName());
							leaveUser.setWxUserId(po.getWxUserId());
							// 执行离职
							contactService
									.leaveUser(leaveUser, loginuser, true, operationIp);
							userIds.add(leaveUser);
						}else{	//账号为空，通过手机号查询离职用户
							po = contactService.getUserInfoByMobile(
									departuresVO.getMobile(), corpId);
							if (null == po) { // 手机号也查不到对应用户
								errorlist.add("第" + listNum
										+ "行出现错误，错误提示：系统中找不到对应用户<br/>");
								departuresVO
										.setError("系统中找不到对应需要离职的用户");
								errlist.add(departuresVO);
								continue;
							}
							TbQyUserInfoPO leaveUser = new TbQyUserInfoPO();
							leaveUser.setId(po.getId());
							leaveUser.setOrgId(orgId);
							leaveUser.setCorpId(corpId);
							leaveUser.setUserId(po.getUserId());
							leaveUser.setLeaveTime(departuresVO.getLeaveTime());
							leaveUser.setLeaveRemark(departuresVO
									.getLeaveRemark());
							leaveUser.setPersonName(po.getPersonName());
							leaveUser.setWxUserId(po.getWxUserId());
							// 执行离职
							contactService
									.leaveUser(leaveUser, loginuser, true, operationIp);
							userIds.add(leaveUser);
						}
					} catch (Exception e) {
						errorlist.add("第" + listNum
								+ "行出现错误，错误提示：离职操作时出现异常，请重试或手动离职<br/>");
						departuresVO.setError("离职操作时出现异常，请重试或手动离职");
						errlist.add(departuresVO);
						continue;
					} catch (BaseException e) {
						errorlist.add("第" + listNum
								+ "行出现错误，错误提示：离职操作时出现异常，请重试或手动离职<br/>");
						departuresVO.setError("离职操作时出现异常，请重试或手动离职");
						errlist.add(departuresVO);
						continue;
					}
					processNum=processNum+1;
				}
				resultvo.setProcessNum(processNum);
			} else {
				resultvo.setTotalNum(0);
				resultvo.setProcessNum(0);
			}
		} catch (Exception e) {
			List<String> errorlist = new ArrayList<String>();
			errorlist.add("第" + listNum + "行出现错误，导致终止，错误提示：" + e.getMessage()
					+ "<br/>");
			resultvo.setErrorlist(errorlist);
			// 错误数据
			List<DeparturesVO> errlist = new ArrayList<DeparturesVO>();
			for (int i = listNum - 1; i < resultvo.getTotalNum(); i++) {
				DeparturesVO vo = list.get(i);
				if (i == listNum - 1) {
					vo.setError("第" + listNum + "行出现错误，导致终止，错误提示："
							+ e.getMessage() + "<br/>");
				}
				errlist.add(vo);
			}
			error.setErrorlist(errlist);
			logger.info("批量离职失败orgId：" + orgId, e);
		} catch (BaseException e) {
			List<String> errorlist = new ArrayList<String>();
			errorlist.add("第" + listNum + "行出现错误，导致终止，错误提示：" + e.getMessage()
					+ "<br/>");
			resultvo.setErrorlist(errorlist);
			// 错误数据
			List<DeparturesVO> errlist = new ArrayList<DeparturesVO>();
			for (int i = listNum - 1; i < resultvo.getTotalNum(); i++) {
				DeparturesVO vo = list.get(i);
				if (i == listNum - 1) {
					vo.setError("第" + listNum + "行出现错误，导致终止，错误提示："
							+ e.getMessage() + "<br/>");
				}
				errlist.add(vo);
			}
			error.setErrorlist(errlist);
			logger.info("批量离职失败orgId：" + orgId, e);
		} finally {
			resultvo.setFinish(true);
			error.setFinish(true);
			upFile.delete();

			UserInfoChangeNotifier.batchLeaveUser(org, userIds, UserInfoChangeInformType.IMPORT_MGR);
			logger.debug("批量离职用户完成,orgId:"
					+ orgId
					+ ",corpId："
					+ corpId
					+ ",processNum:"
					+ resultvo.getProcessNum()
					+ ",totalNum:"
					+ resultvo.getTotalNum()
					+ ",errorSize:"
					+ (error.getErrorlist() == null ? 0 : error.getErrorlist()
							.size()));
		}

	}

}
