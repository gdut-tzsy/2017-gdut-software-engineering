package cn.com.do1.component.contact.contact.dao;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.vo.UserDeptInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserDeptZhouNianInfoVO;

public interface IContactInsideReadOnlyDAO extends IBaseDAO{
	/**
	 * 查询按农历发送生日祝福的用户--马权阳 2015-06-01
	 * @param organizationId
	 * @param lunar
	 * @param remindType
	 * @return
	 * @author Chen Feixiong 2015-1-7
	 */
	List<UserDeptInfoVO> findBirthdayUserByDateAndRemindType(Map<String, Object> params) throws Exception, BaseException;
	
	/**
	 * 获取所有的入职日期不为空的人员
	 * @author liweili
	 * 2015-9-21
	 * @param params
	 * @return List<TbQyEntry>
	 * @throws Exception
	 * @throws BaseException
	 */
	List<UserDeptZhouNianInfoVO> findEntryPeople(Map<String, Object> params) throws Exception,BaseException;
}
