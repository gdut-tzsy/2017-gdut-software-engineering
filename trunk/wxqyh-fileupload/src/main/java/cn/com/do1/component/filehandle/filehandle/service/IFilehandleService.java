package cn.com.do1.component.filehandle.filehandle.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdDirectorPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileCommentPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFilePO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileFlowVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileVO;

import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public interface IFilehandleService extends IBaseService {
	/**
	 * @param type
	 * @description 获取设置
	 * @method getDirectorApproveSetting
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdDirectorPO getApproveSetting(String type) throws Exception, BaseException;

	/**
	 * @description 获取文件上传
	 * @method getFileHandlePager
	 * @Param map
	 * @Param pager
	 * @Return
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	Pager getFileHandlePager(Map map, Pager pager) throws Exception, BaseException;

	/**
	 * @description 获取所有的审批主任
	 * @method getDirector
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	List<TbQyUserInfoVO> getDirector() throws Exception, BaseException;

	/**
	 * @description 判断当前用户是否有文件上传的权限
	 * @method isFileUploadAuth
	 * @Param userId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isFileUploadAuth(String userId) throws Exception, BaseException;

	/**
	 * @description 获取这些部门的子部门（包含自己）
	 * @method getChildDept
	 * @Param deptIds
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	String getChildDept(String deptIds) throws Exception, BaseException;

	/**
	 * @description 新增文件上传
	 * @method addFile
	 * @Param filePO
	 * @Param userInfo
	 * @Param relatives
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	void addFile(TbYsjdFilePO filePO, UserInfoVO userInfo, String relatives, String[] mediaIds) throws Exception, BaseException;

	/**
	 * @description 获取文件详情
	 * @method getFileDetail
	 * @Param fileId
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdFilePO getFileDetail(String fileId) throws Exception, BaseException;

	/**
	 * @description 获取查看文件当前人员的身份
	 * @method getFileIdentify
	 * @Param fileId
	 * @Param userId
	 * @Return 0：创建人，1：审批主任，2：相关人，3：部门负责人，4：普通工作人员，5：其他人，无权限
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	int getFileIdentify(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description 提交领导的批阅
	 * @method direcotrApprove
	 * @Param filePO
	 * @Param userInfo
	 * @Param imageUrls
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	void directorApprove(TbYsjdFilePO filePO, UserInfoVO userInfo, String[] imageUrls) throws Exception, BaseException;

	/**
	 * @description 转发文件
	 * @method transformFile
	 * @Param filePO
	 * @Param userInfo
	 * @Param handlerList
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	void transformFile(TbYsjdFilePO filePO, UserInfoVO userInfo, List<TbQyUserInfoVO> handlerList) throws Exception, BaseException;

	/**
	 * @description 部门负责人处理
	 * @method incharHandleFile
	 * @Param fileId
	 * @Param handleType 0是结束流程，1是转发
	 * @Param transformPerson 转发的人员
	 * @Param userInfo
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	void inchargeHandleFile(String fileId, String handleType, String transformPerson, UserInfoVO userInfo) throws Exception, BaseException;

	/**
	 * @description 获取详情
	 * @method getDetail
	 * @Param id
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdFileVO getDetail(String id) throws Exception, BaseException;

	/**
	 * @description 分页获取文件列表
	 * @method getFilePage
	 * @Param map
	 * @Param pager
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	Pager getFilePage(Map map, Pager pager) throws Exception, BaseException;

	/**
	 * @description 获取流程
	 * @method getFlowList
	 * @Param fileId
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	List<TbYsjdFileFlowVO> getFlowList(String fileId) throws Exception, BaseException;

	/**
	 * @description 查看该负责人是否已处理文件
	 * @method isInchargeHandleFile
	 * @Param fileId
	 * @Param userId
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isInchargeHandleFile(String fileId, String userId) throws Exception, BaseException;


	/**
	 * @description 获取当前人员能选择的人（当前部门和子部门的人员）
	 * @method findAlluserByUser
	 * @Param pager
	 * @Param userInfo
	 * @Param map
	 * @Return
	 * @time 2017-8-25
	 * @author lwj
	 * @version V1.0.0
	 */
	Pager findAllUserByUser(Pager pager, UserInfoVO userInfo, Map<String, Object> map) throws Exception, BaseException;



	/**
	 * 下面的方法由企微工作日志搬过来的
	 */
	void updateCommentStatus(String commentId) throws BaseException, Exception;

	List<TbYsjdFileCommentPO> getCommentsByUserID(String userId, String diaryId) throws BaseException, Exception;

	void updateCommentCount(String diaryId) throws BaseException, Exception;

	void insertAtRecord(TbYsjdFileCommentPO tbQyDiaryCommentPO, TbYsjdFilePO tbQyDiaryPO, UserInfoVO sendUser, String atPersons) throws BaseException, Exception;

	Pager getDiaryComment(Map map, Pager comments) throws Exception, BaseException;

	void readUser(String userId, String diaryId) throws BaseException, Exception;

}
