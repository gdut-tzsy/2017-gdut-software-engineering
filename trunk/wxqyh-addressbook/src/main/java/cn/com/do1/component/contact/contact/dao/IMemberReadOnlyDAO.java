package cn.com.do1.component.contact.contact.dao;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface IMemberReadOnlyDAO extends IBaseDAO {

	/**
	 * 根据orgId查询所有的人员邀请设置
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigVO> getConfigList(String orgId)throws Exception, BaseException;

	/**
	 * 查询设置的部门关联表
	 * @param orgId
	 * @param Id
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigResPO> getDeptList(String orgId,String Id)throws Exception,BaseException;

	/**
	 * 根据Id查询邀请单信息
	 * @param id
	 * @param orgId 
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyMemberConfigVO getHistotryData(String id, String orgId)throws Exception,BaseException;

	/**
	 * 查询所有的邀请单设置信息
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigPO> getAllConfig()throws Exception,BaseException;

	/**
	 * 根据邀请单设置Id查询通知对象信息
	 * @param id
	 * @param type
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberTargetPersonPO> getRecipient(String id, String type)throws Exception,BaseException;
	
	/**
	 * 查询成员邀请列表
	 * @param map
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager seachMemberList(Map map, Pager pager)throws Exception,BaseException;

	/**
	 * 查询待审批、已通过或未通过的邀请成员
	 * @param id
	 * @param orgId
	 * @param type
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int countPersonNum(String id, String orgId, String type)throws Exception,BaseException;

	/**
	 * 查询邀请人员列表信息
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchMemberInfo(Map searchMap, Pager pager)throws Exception,BaseException;

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
	 * 根据手机号查询邀请信息
	 * @param orgId
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int countUsersByMobile(String orgId, String mobile)throws Exception, BaseException;

	/**
	 * 根据微信号查询邀请信息
	 * @param orgId
	 * @param weixinNum
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int countUsersByWeixinNum(String orgId, String weixinNum)throws Exception, BaseException;

	/**
	 * 根据邮箱查询邀请信息
	 * @param orgId
	 * @param email
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int countUsersByEmail(String orgId, String email)throws Exception, BaseException;

	/**
	 * 根据账号查询邀请信息
	 * @param orgId
	 * @param wxUserId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	int countUsersByWxUserId(String orgId, String wxUserId)throws Exception, BaseException;

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
	 * 批量通过用户填写的邀请单的id获取填写的自定义的列表
	 * @param memberInfoIds 用户填写的邀请单的ids
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-14
	 * @version 1.0
	 */
	List<TbQyMemberUserCustomPO> batchMemberUserCustom(List<String> memberInfoIds) throws BaseException, Exception;
}
