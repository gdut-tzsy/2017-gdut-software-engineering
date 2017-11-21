package cn.com.do1.component.contact.contact.dao;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyTargetUserPO;
import cn.com.do1.component.addressbook.contact.vo.TargetUserGroupVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;

/**
 * 目标对象方法接口
 * @author lishengtao
 * 2016-1-6
 */
public interface ITargetDAO extends IBaseDAO{
	
	/**
	 * 根据组Id获取全员信息
	 * @param foreignId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-7
	 */
	List<TbQyTargetUserPO> getTargetUserPOByGroupId(String groupId)throws Exception,BaseException;
	
	/**
	 * 删除目标人员:status=0，增加状态
	 * @param groupId
	 * @param status
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-7
	 */
	void delTargetUserByGroupId(String groupId,int status)throws Exception,BaseException;
	
	/**
	 * 根据groupId和userId查找目标对象
	 * @param groupId
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-11
	 */
	TbQyTargetUserPO getTargetUserPO(String groupId,String userId)throws Exception,BaseException;
	
	
	/**
	 * 查找目标用户列表
	 * @param paramMap
	 * @return groupId必须
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-11
	 */
	 List<TbQyUserInfoVO> getTargetUserList(Map<String,Object> paramMap)throws Exception,BaseException;
	
	/**
	 * 分页查找目标对象
	 * @param paramMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-11
	 */
	Pager getTargetUserPager(Map<String,Object> paramMap,Pager pager)throws Exception,BaseException;
	
	
	/**
	 * 批量修改阅读状态
	 * @param status
	 * @param groupId
	 * @param userIdArray
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-2-25
	 */
	void updateTargetUserStatus(String groupId,String[] userIdArray,int status)throws Exception,BaseException;
	
	/**
	 * 删除目标对象
	 * @param groupId
	 * @param userId
	 * @param status
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-3-7
	 */
	void delTargetUser(String groupId,String userId,int status)throws Exception,BaseException;
	
	/**
	 * 删除目标对象
	 * @param groupId
	 * @param userIdArray
	 * @param status
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-3-7
	 */
	void delTargetUser(String groupId,String[] userIdArray,int status)throws Exception,BaseException;
	
	/**
	 * 搜索目标对象的组
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-3-7
	 */
	List<TargetUserGroupVO> getTargetUserGroupVOList(Map<String,Object> paramMap)throws Exception,BaseException;
	
	/**
	 * 修改目标对象状态
	 * @param groupId
	 * @param agentCode
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-3-7
	 */
	void updateBuildUnreadStatus(String groupId,String agentCode,int status)throws Exception,BaseException;
}
