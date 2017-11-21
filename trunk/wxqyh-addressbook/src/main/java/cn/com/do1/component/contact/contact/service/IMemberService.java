package cn.com.do1.component.contact.contact.service;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberMsgVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.common.service.IExcelService;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;

/**
 * <p>Title: 成员邀请</p>
 * <p>Description: 类的描述</p>
 * @author luobowen
 * @2015-11-10
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public interface IMemberService extends IBaseService,IExcelService{

	/**
	 * 查询历史邀请设置信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyMemberConfigVO getHistotryData(String id,String orgId)throws Exception, BaseException;

	/**
	 * 保存邀请设置信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	void saveConfig(String orgId,TbQyMemberConfigPO po,String deptId,String dept, List<TbQyMemberCustomConfigPO> customConfigPOList, List<TbQyMemberBaseConfigPO> baseConfigPOList)throws Exception, BaseException;

	/**
	 * 查询邀请人员列表信息
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchMemberInfo(Map searchMap, Pager pager)throws Exception, BaseException;

	/**
	 * 审批通过把邀请成员插入到通讯录
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	String insertContact(TbQyMemberInfoPO po, UserOrgVO org,TbQyUserInfoPO userInfo,String deptId)throws Exception, BaseException;

	/**
	 * 验证是否存在待审批的成员
	 * @param tbQyMemberInfoPO
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyMemberMsgVO verifyMemberInfo(TbQyMemberInfoPO tbQyMemberInfoPO)throws Exception, BaseException;

	/**
	 * 删除邀请成员
	 * @param ids
	 * @param userInfoVO
	 * @throws Exception
	 * @throws BaseException
	 */
	void delMember(String[] ids, UserOrgVO userInfoVO)throws Exception, BaseException;

	/**
	 * 查询设置的部门关联表
	 * @param id
	 * @param ids
	 * @param userInfoVO
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigResPO> getDeptList(String orgId, String id)throws Exception, BaseException;

	/**
	 * 查询成员邀请列表
	 * @param ids
	 * @param userInfoVO
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager seachMemberList(Map map, Pager pager)throws Exception, BaseException;

	/**
	 * 根据orgId查询所有的人员邀请设置单
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigVO> getConfigList(String orgId)throws Exception, BaseException;

	/**
	 * 删除邀请单部门关联信息
	 * @param ids
	 * @throws Exception
	 * @throws BaseException
	 */
	void delMemberConfigDes(String ids)throws Exception, BaseException;

	/**
	 * 查询所有的邀请单设置
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigPO> getAllConfig()throws Exception, BaseException;

	/**
	 * 查询邀请单的通知对象
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberTargetPersonPO> getTargetPerson(String id)throws Exception, BaseException;

	/**
	 * 把孩子添加到学生表中
	 *@param tbQyMemberInfoPO 父母
	 * @param infoPOs 孩子列表
	 *@param userInfo 父母信息
	 * @throws Exception
	 * @throws BaseException
     */
	void addChildrenEdu(List<TbQyMemberInfoPO> infoPOs, TbQyMemberInfoPO tbQyMemberInfoPO,TbQyUserInfoPO userInfo, UserOrgVO org)throws Exception, BaseException;

	/**
	 * 通过父母id查找po
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	TbQyMemberInfoPO searchByParentId(String parentId) throws Exception, BaseException;

	/**
	 *新增或者修改学生到学生表中
	 * @param po po
	 * @param org 机构信息
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	void addOrUpdateStudent(TbQyMemberInfoPO po, UserOrgVO org,TbQyUserInfoPO userInfo) throws BaseException, Exception;

	/**
	 *新增或者修改学生到学生表中
	 * @param po po
	 * @param org 机构信息
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	void addOrUpdateStudent(TbQyMemberInfoPO po, UserOrgVO org, String deptId, List<TbQyStudentInfoPO> addStud, List<TbQyStudentInfoPO> updateStud, List<TbQyUserStudentRefPO> addRef) throws BaseException, Exception;

	/**
	 * 批量审批通过
	 * @param list 邀请列表
	 * @param deptId 部门id
	 * @param orgVO 机构id
	 * @param map 自定义字段
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-04-25
	 * @version 1.0
	 */
	void batchApproveEdu(List<TbQyMemberInfoPO> list, String deptId, UserOrgVO orgVO, Map<String, List<TbQyMemberUserCustomPO>> map) throws BaseException, Exception;

	/**
	 * 查找该监护人填写的孩子
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	List<TbQyMemberInfoPO> searchChildren(String parentId) throws Exception, BaseException;

	/**
	 * 查找该监护人填写的孩子
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	List<TbQyMemberInfoVO> searchChildrenToVO(String parentId) throws Exception, BaseException;

	/**
	 *教育版邀请详情编辑
	 * @param btnType 点击的事件
	 * @param po 审批单填写的用户的po
	 * @param org 机构信息
	 * @param list 孩子列表
	 *  @param customPOList 自定义字段列表
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-7
	 * @version 1.0
     */
	void updateMemberEdu(String btnType, TbQyMemberInfoPO po, UserOrgVO org, List<TbQyMemberInfoPO> list, List<TbQyMemberUserCustomPO> customPOList) throws Exception, BaseException;

	/**
	 * 批量查询邀请列表成员
	 * @param ids 信息表id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-7
	 * @version 1.0
	 */
	List<TbQyMemberInfoPO> batchPObyId(String ids[]) throws Exception, BaseException;

	/**
	 * 批量不通过邀请单
	 * @param po
	 * @param org
	 * @param list
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-15
	 * @version 1.0
     */
	void batchDisagree(TbQyMemberInfoPO po,UserOrgVO org, List<TbQyMemberInfoPO> list) throws BaseException, Exception;

	/**
	 *查询已启用的展示在首页的邀请单
	 * @param orgId
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
	 */
	List<TbQyMemberConfigVO> showIndex(String orgId) throws Exception, BaseException;

	/**
	 * 创建默认的邀请单
	 * @param orgVO
	 * @param isEdu 是否是教育版
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
     */
	List<TbQyMemberConfigVO> addDefaultConfig(UserOrgVO orgVO, boolean isEdu) throws BaseException, Exception;

	/**
	 *查询所有展示在首页的邀请单
	 * @param orgId
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
	 */
	List<TbQyMemberConfigPO> selectAllIndex(String orgId) throws Exception, BaseException;

	/**
	 * 通过邀请单id查询对应自定义字段的设置
	 * @param memberId 邀请单id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	List<TbQyMemberCustomConfigPO> getMemberCustomConfigByMeberId(String memberId) throws BaseException, Exception;

	/**
	 * 通过邀请单id查询对应基础字段的设置
	 * @param memberId 邀请单id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	List<TbQyMemberBaseConfigPO> getMemberBaseConfigByMeberId(String memberId) throws BaseException, Exception;

	/**
	 * 通过用户填写的邀请单的id获取填写的自定义的列表
	 * @param memberInfoId 用户填写的邀请单的id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	List<TbQyMemberUserCustomPO> getMemberUserCustom(String memberInfoId) throws BaseException, Exception;

	/**
	 *邀请详情编辑
	 * @param btnType 点击的事件
	 * @param po 审批单填写的用户的po
	 * @param org 机构信息
	 * @param customPOList 自定义字段列表
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-4-14
	 * @version 1.0
	 */
	void updateMember(String btnType, TbQyMemberInfoPO po, UserOrgVO org, List<TbQyMemberUserCustomPO> customPOList) throws Exception, BaseException;

	/**
	 * 批量通过用户填写的邀请单的id获取填写的自定义的列表
	 * @param memberInfoIds 用户填写的邀请单的ids
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-14
	 * @version 1.0
	 */
	Map<String, List<TbQyMemberUserCustomPO>> batchMemberUserCustom(List<String> memberInfoIds) throws BaseException, Exception;

	/**
	 * 批量审批通过
	 * @param list 邀请列表
	 * @param deptId 部门id
	 * @param orgVO 机构id
	 * @param map 自定义字段
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-14
	 * @version 1.0
	 */
	void batchApprove(List<TbQyMemberInfoPO> list, String deptId, UserOrgVO orgVO, Map<String, List<TbQyMemberUserCustomPO>> map ) throws BaseException, Exception;
}
