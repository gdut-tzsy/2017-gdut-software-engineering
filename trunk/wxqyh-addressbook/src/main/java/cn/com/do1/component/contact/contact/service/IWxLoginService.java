package cn.com.do1.component.contact.contact.service;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;
import cn.com.do1.component.systemmgr.org.model.OrgVO;

/**
 * <p>Title: 通讯录管理</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-1-20
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public interface IWxLoginService extends IBaseService{

	/**
	 * @param email
	 * @param corpId
	 * @return
	 * @author Sun Qinghai
	 * @2015-1-29
	 * @version 1.0
	 */
	TbQyUserWxloginInfoPO findWxAccountByEmail(String email, String corpId) throws Exception, BaseException;

	/**
	 * 获取垃圾机构的信息
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-2-11
	 * @version 1.0
	 */
	List<ExtOrgPO> getRubbishOrg(String corpId) throws Exception, BaseException;

	/**
	 * 查询绑定扫描登录信息
	 * @param wxUserId
	 * @param email
	 * @param corpId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-9-17
	 * @version 1.0
	 */
	TbQyUserWxloginInfoPO findWxAccountByUserIdAndEmail(String wxUserId, String email,
			String corpId) throws Exception, BaseException;

	/**
	 * 获取机构列表
	 * @param searchMap
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-10
	 * @version 1.0
	 */
	List<OrgVO> getListOrg(Map<String, Object> searchMap) throws Exception, BaseException;

	/**
	 * 获取机构下的管理人员
	 * @param pager
	 * @param searchValue
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-11
	 * @version 1.0
	 */
	Pager listMyPersonByOrg(String orgId, Pager pager, Map searchValue) throws Exception, BaseException;
}
