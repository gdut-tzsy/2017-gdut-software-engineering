package cn.com.do1.component.contact.contact.dao;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyMemberConfigResPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface IMemberDAO extends IBaseDAO {

	/**
	 * 根据orgid和审批状态查询邀请人员
	 * @param searchMap
	 * @param pager
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberInfoVO> findUsersByOrgId(Map<String, Object> orgId)throws Exception,BaseException;

	/**
	 * 根据orgid删除部门关联表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	void delConfigDes(String orgId)throws Exception,BaseException;

	/**
	 * 查询设置的部门关联表
	 * @param ids
	 * @param userInfoVO
	 * @throws Exception
	 * @throws BaseException
	 */
	List<TbQyMemberConfigResPO> getDeptList(String orgId)throws Exception,BaseException;

	/**
	 * 同步更新成员邀请设置名称
	 * @param orgId
	 * @param id
	 * @throws Exception
	 * @throws BaseException
	 */
	void updateConfigName(String orgId, String id,String name)throws Exception,BaseException;
}
