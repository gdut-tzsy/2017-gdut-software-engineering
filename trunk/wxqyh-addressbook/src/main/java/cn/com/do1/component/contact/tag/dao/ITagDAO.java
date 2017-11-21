package cn.com.do1.component.contact.tag.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefVO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public interface ITagDAO extends IBaseDAO {
	/**
	 * 根据orgId删除该机构下的所有标签
	 *
	 * @param orgId
	 * @throws BaseException
	 * @throws Exception
	 * @author Luo Rilang
	 * @2015-11-4
	 * @version 1.0
	 */
	void delTagByOrgId(String orgId) throws BaseException, Exception;

	/**
	 * 根据orgId删除该机构下的所有标签成员
	 *
	 * @param orgId
	 * @throws BaseException
	 * @throws Exception
	 * @author Luo Rilang
	 * @2015-11-4
	 * @version 1.0
	 */
	void delTagMenberByOrgId(String orgId) throws BaseException,
			Exception;

	/**
	 * 根据微信标签id获取所有标签成员，包括用户和部门
	 * 
	 * @param orgId
	 * @param tagId
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 * @author Luo Rilang
	 * @2015-11-6
	 * @version 1.0
	 */
	List<QyTagRefVO> getTagMenbersByWxTagId(String orgId, String tagId)
			throws BaseException, Exception;

	/**
	 * 根据微信tagId获取标签信息
	 * @param orgId
	 * @param wxTagId
	 * @return
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	TbQyTagInfoPO getTagInfoByWxTagId(String orgId, String wxTagId) throws Exception;

	/**
	 * 根据tagId获取标签管理信息
	 * @param tagId
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	List<TbQyTagRefPO> getTagRefList(String tagId) throws Exception;

	/**
	 * 获取企业所有的标签信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	List<TbQyTagInfoPO> getTagInfoList(String orgId) throws Exception;

	List<QyTagRefVO> getTagRefVOByWxTagIds(String orgId, List<String> wxTagIds) throws SQLException;

	List<QyTagPageInfoVO> getTagPageInfoList(String orgId) throws SQLException;

	Pager getTagRefPage(Pager pager, Map<String, Object> searchMap) throws SQLException;

	List<TbQyTagInfoPO> getTagListByName(String tagName, String orgId) throws SQLException;

	List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, ArrayList<String> menberIds, int menberType) throws SQLException;

	List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, String[] menberIds) throws SQLException;

	void updateTagUserAndDeptTotal(String tagId, int addUserSize, int addDeptSize) throws SQLException;

	void updateTagUserTotal(String[] tagIds, int addSize) throws SQLException;

	void updateTagDeptTotal(String[] tagIds, int addSize) throws SQLException;

	List<TbQyTagRefPO> getTagRefListByIds(String tagId, String[] ids) throws SQLException;

	List<String> getTagRefMenberIdListByMenberIdList(String tagId, List<String> menberIds) throws SQLException;

	int getHasTagRef(String tagId) throws SQLException;

	List<String> getTagrefIdByMenberIds(String[] menberIds, int menberType) throws SQLException;

	List<TbQyTagRefPO> getTagRefPOByMenberIds(String[] menberIds, int menberType) throws SQLException;

	List<TbQyTagRefPO> getTagRefListByTagIds(List<String> tagIdList) throws SQLException;

	/**
	 * 同步次数++
	 * @param orgId 机构id
	 * @author sunqinghai
	 * @date 2017 -3-28
	 */
	void addSyncTime(String orgId) throws SQLException;

	List<String> getTagRefMenberIdListByTagId(String tagId, int menberType) throws SQLException;

}
