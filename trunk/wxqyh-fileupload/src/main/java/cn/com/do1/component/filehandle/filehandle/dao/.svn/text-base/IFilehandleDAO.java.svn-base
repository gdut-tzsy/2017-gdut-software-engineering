package cn.com.do1.component.filehandle.filehandle.dao;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileCommentPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileFlowPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileHandleInfoPO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileFlowVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFilePicVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileRecipientVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileVO;

import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public interface IFilehandleDAO extends IBaseDAO {
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
	 * @description 获取通讯录人员信息
	 * @method getUserInfoByUserId
	 * @Param userId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	List<TbQyUserInfoVO> getUserInfoListByUserId(String userId) throws Exception, BaseException;

	/**
	 * @description 判断这个userId是否存在这些部门中
	 * @method isExistDepts
	 * @Param deptIds
	 * @Param userId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isExistDepts(String deptIds, String userId) throws Exception, BaseException;

	/**
	 * @description 获取当前部门的子部门
	 * @method getChildDept
	 * @Param deptId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	List<String> getChildDept(String deptId) throws Exception, BaseException;

	/**
	 * @description 获取用户所有的部门
	 * @method getUserDept
	 * @Param userId
	 * @Return
	 * @time 2017-8-17
	 * @author lwj
	 * @version V1.0.0
	 */
	List<String> getUserDept(String userId) throws Exception, BaseException;

	/**
	 * @description 获取负责人处理信息
	 * @method getInchargeHandleInfo
	 * @Param handleUserId
	 * @Param fileId
	 * @Return
	 * @time 2017-8-18
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdFileHandleInfoPO getInchargeHandleInfo(String handleUserId, String fileId) throws Exception, BaseException;

	/**
	 * @description 获取详情
	 * @method getFileDetail
	 * @Param id
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdFileVO getFileDetail(String id) throws Exception, BaseException;

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
	 * @description 判断是否相关人
	 * @method isRelevant
	 * @Param fileId
	 * @Param userId
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isRelevant(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description 判断是否负责人
	 * @method isHandler
	 * @Param fileId
	 * @Param userId
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isHandler(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description 获取文件的流程
	 * @method getFileFlow
	 * @Param fileId
	 * @Return
	 * @time 2017-8-21
	 * @author lwj
	 * @version V1.0.0
	 */
	List<TbYsjdFileFlowVO> getFileFlow(String fileId) throws Exception, BaseException;

	/**
	 * @description 判断是否所有负责人已处理完毕
	 * @method isAllInchargeHandle
	 * @Param fileId
	 * @Return
	 * @time 2017-8-22
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isAllInchargeHandle(String fileId) throws Exception, BaseException;

	/**
	 * @description 查看相关人是否已存在
	 * @method isExistIncharge
	 * @Param fileId
	 * @Param userId
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isExistIncharge(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description 获取等待完成的流程提示
	 * @method getNextFlow
	 * @Param fileId
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	TbYsjdFileFlowPO getNextFlow(String fileId) throws Exception, BaseException;

	/**
	 * @description 删除等待完成的流程提醒
	 * @method deleteNextFlow
	 * @Param fileId
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	void deleteNextFlow(String fileId) throws Exception, BaseException;

	/**
	 * @description 查看该负责人是否已处理文件
	 * @method isInchargeHandleFile
	 * @Param fileId
	 * @Param userId
	 * @Return true为已处理，false为未处理
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	boolean isInchargeHandleFile(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description 获取流程上传的图片
	 * @method getFlowPic
	 * @Param fileId
	 * @Param userId
	 * @Return
	 * @time 2017-8-23
	 * @author lwj
	 * @version V1.0.0
	 */
	List<TbYsjdFilePicVO> getFlowPic(String fileId, String userId) throws Exception, BaseException;

	/**
	 * @description    获取这个用户所在部门名称，多个用“、”隔开
	 * @method          getUserAllDeptName
	 * @Param           userId
	 * @Return
	 * @time            2017-8-25
	 * @author          lwj
	 * @version         V1.0.0
	 */
	String getUserAllDeptName(String userId) throws Exception, BaseException;

	/**
	 * @description    获取还没处理的部门负责人
	 * @method          getNotHandleIncharge
	 * @Param           fileId
	 * @Return
	 * @time            2017-9-4
	 * @author          lwj
	 * @version         V1.0.0
	 */
	List<TbQyUserInfoVO> getNotHandleIncharge(String fileId) throws Exception, BaseException;

	/**
	 * 下面的方法由企微工作日志搬过来的
	 */
	void updateCommentStatus(String commentId) throws BaseException, Exception;

	List<TbYsjdFileCommentPO> getCommentsByUserID(String userId, String diaryId) throws BaseException, Exception;

	List<TbYsjdFileRecipientVO> getDiaryRecipient(String diaryId, String type) throws Exception, BaseException;

	Pager getDiaryComment(Map map, Pager comments) throws Exception, BaseException;

	int countDiaryComment(String userId, String diaryId) throws BaseException, Exception;

	void updateDiaryRec(String userId, String diaryId) throws Exception, BaseException;

	/**
	 * 以下是从ContactDAO复制过来的，因为需要只显示本部门及其子部门的人员，所以需要做人员的筛选改造
	 */
	Pager findAlluserByDeptId(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts, String userId) throws Exception, BaseException;

	Pager searchContactByPy(Map searchMap, Pager pager, String userId) throws Exception, BaseException;
}
