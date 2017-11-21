package cn.com.do1.component.contact.tag.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefVO;

/**
 * 标签管理
 * <p>
 * Title: 功能/模块
 * </p>
 * <p>
 * Description: 类的描述
 * </p>
 * 
 * @author Luo Rilang
 * @2015-11-2
 * @version 1.0 修订历史： 日期 作者 参考 描述
 */
public interface ITagMgrService extends IBaseService {

	/**
	 * 根据微信标签id获取该标签下的所有用户userId
	 *
	 * @param orgId
	 * @param wxTagId
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author Luo Rilang
	 * @date 2017 -2-28
	 * @2015-11-6
	 * @version 1.0
	 */
	List<String> getUserIdsByWxTagid(String orgId, String wxTagId)throws BaseException, Exception;

	/**
	 * 根据微信标签id获取标签信息
	 * @param orgId
	 * @param wxTagIds 微信标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2016 -10-12
	 */
	List<QyTagRefVO> getTagRefVOByWxTagIds(String orgId, List<String> wxTagIds) throws Exception;

	/**
	 * 新增标签，公共群组调用方法，用于新增公共群组时同时新增标签
	 * @param userInfoVO 管理用户
	 * @param tbQyUserGroupPO 公共群组po
	 * @param userIds 群组成员id
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void addTag(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String[] userIds) throws BaseException, Exception;

	/**
	 * 更新标签，公共群组编辑是调用
	 * @param userInfoVO 管理用户
	 * @param tbQyUserGroupPO 公共群组po
	 * @param userIds 群组成员id
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTag(UserOrgVO userInfoVO, TbQyUserGroupPO tbQyUserGroupPO, String[] userIds) throws Exception, BaseException;

	/**
	 * 新增标签
	 * @param userInfoVO 管理用户
	 * @param po 标签po
	 * @param userIds 全量的用户id
	 * @param deptIds 全量的部门id
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void addTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds) throws BaseException, Exception;

	/**
	 * 全量更新标签，用户和部门传入全量，不在传入的范围内的即表示删掉
	 * @param userInfoVO 管理用户
	 * @param po 标签po
	 * @param userIds 全量的用户id
	 * @param deptIds 全量的部门id
	 * @param isUpdateName 是否更新了标签名称
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateName) throws Exception, BaseException;

	/**
	 * 全量更新用户和部门，不在传入的范围内的即表示删掉
	 * @param userInfoVO 管理用户
	 * @param po 标签po
	 * @param userIds 全量的用户id
	 * @param deptIds 全量的部门id
	 * @param isUpdateWeixin 是否需要更新到微信，当从微信同步时不需要更新到微信
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateWeixin) throws Exception, BaseException;

	/**
	 * 新增标签的成员信息
	 * @param userInfoVO 管理用户
	 * @param po 标签po
	 * @param userIds 全量的用户id
	 * @param deptIds 全量的部门id
	 * @param isUpdateWeixin 是否需要更新到微信，当从微信同步时不需要更新到微信
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void addTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String[] userIds, String[] deptIds, boolean isUpdateWeixin) throws BaseException, Exception;

	/**
	 * 获取企业的所有标签
	 * @param orgId 机构id
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<QyTagPageInfoVO> getTagPageInfoList(String orgId) throws SQLException;

	/**
	 * 分页获取标签的成员信息
	 * @param pager 分页信息
	 * @param tagId 标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	Pager getTagRefPage(Pager pager, String tagId) throws Exception;

	/**
	 * 根据标签名称获取标签信息
	 * @param tagName 标签名称
	 * @param orgId 机构id
	 * @return 返回数据
	 * @throws SQLException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagInfoPO> getTagListByName(String tagName, String orgId) throws SQLException;

	/**
	 * 判断标签名称是否已存在
	 * @param orgId 机构id
	 * @param tagName 标签名称
	 * @param tagId 标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	boolean getIsRepeat(String orgId, String tagName, String tagId) throws Exception;

	/**
	 * 新增标签关联数据，id以|分隔
	 * @param userInfoVO 管理员
	 * @param po 标签po
	 * @param userIds 用户id以|分隔
	 * @param deptIds 部门id以|分隔
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void addTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, String userIds, String deptIds) throws BaseException, Exception;

	/**
	 * 根据成员id获取成员信息
	 * @param tagId 标签id
	 * @param menberIds 成员id集合
	 * @param menberType 成员类型，来源TagStaticUtil.TAG_REF_TYPE_DEPT
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, ArrayList<String> menberIds, int menberType) throws Exception;

	/**
	 * 删除标签成员数据
	 * @param userInfoVO 管理员
	 * @param po 标签po
	 * @param list 标签成员po
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void delTagRef(UserOrgVO userInfoVO, TbQyTagInfoPO po, List<TbQyTagRefPO> list) throws BaseException, Exception;

	/**
	 * 根据标签成员id获取标签成员po
	 * @param tagId 标签id
	 * @param menberIds 成员id集合
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagRefPO> getTagRefListByMenberId(String tagId, String[] menberIds) throws Exception;

	/**
	 * 更新标签的人员和部门数
	 * @param po 标签po
	 * @param addUserSize 新增的用户个数，如果是移除人员，此值为负数
	 * @param addDeptSize 新增的部门个数，规则和新增的用户个数一致
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTagUserAndDeptTotal(TbQyTagInfoPO po, int addUserSize, int addDeptSize) throws Exception;

	/**
	 * 更新标签的人员或部门数
	 * @param tagIds 标签id
	 * @param addSize 新增的个数或者减少的个数
	 * @param menberType 成员类型，来源TagStaticUtil.TAG_REF_TYPE_xxx
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTagUserOrDeptTotal(String[] tagIds, int addSize, int menberType) ;

	/**
	 * 获取企业下符合条件的所有标签
	 * @param orgId 机构id
	 * @param tagRang 标签等级，数据来源TagStaticUtil
	 * @param status 标签状态，数据来源TagStaticUtil
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int tagRang, int status) throws Exception;

	/**
	 * 获取企业下符合条件的所有标签
	 * @param orgId 机构id
	 * @param status 标签状态，数据来源TagStaticUtil
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int status) throws Exception;

	/**
	 * 获取标签成员po
	 * @param tagId 标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagRefPO> getTagRefList(String tagId) throws Exception;

	/**
	 * 更新标签
	 * @param userInfoVO 管理员
	 * @param po 标签po
	 * @param isUpdateName 是否更新名称
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTag(UserOrgVO userInfoVO, TbQyTagInfoPO po, boolean isUpdateName) throws BaseException, Exception;

	/**
	 * 根据标签成员关联id获取标签成员
	 * @param tagId 标签id
	 * @param ids 关联id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagRefPO> getTagRefListByIds(String tagId, String[] ids) throws Exception;

	/**
	 * 根据标签成员id，获取标签的成员id，以此来判断该成员是否在标签内
	 * @param tagId 标签id
	 * @param menberIds 成员id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<String> getTagRefMenberIdListByMenberIdList(String tagId, List<String> menberIds) throws Exception;

	/**
	 * 判断标签是否有成员
	 * @param tagId 标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	boolean getHasTagRef(String tagId) throws Exception;

	/**
	 * 更新标签状态
	 * @param tagId 标签id
	 * @param status 状态，数据来源TagStaticUtil
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	void updateTagStatus(String tagId, int status) throws Exception, BaseException;

	/**
	 * 获取企业的所有标签po
	 * @param orgId 机构id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	List<TbQyTagInfoPO> getTagInfoList(String orgId) throws Exception;

	/**
	 * 根据标签的微信侧id，获取标签po
	 * @param orgId 机构id
	 * @param wxTagId 微信标签id
	 * @return 返回数据
	 * @throws Exception 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-13
	 */
	TbQyTagInfoPO getTagInfoByWxTagId(String orgId, String wxTagId) throws Exception;

	/**
	 * 删除所有标签相应的关联成员信息
	 * @param orgId 机构id
	 * @param menberIds 成员id
	 * @param menberType 成员类型，来源TagStaticUtil.TAG_REF_TYPE_xxx
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	void delAllTagRef(String orgId, String[] menberIds, int menberType) throws Exception, BaseException;

	/**
	 * 根据成员信息获取标签关联成员数据的id
	 * @param orgId 机构id
	 * @param menberIds 成员id
	 * @param menberType 成员类型，来源TagStaticUtil.TAG_REF_TYPE_xxx
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	List<String> getTagrefIdByMenberIds(String orgId, String[] menberIds, int menberType) throws Exception;

	/**
	 * 根据成员信息获取标签关联成员数据的id
	 *
	 * @param user
	 * @param ids 机构id
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	void delTagAndRef(UserOrgVO user, String[] ids) throws BaseException, Exception;

	/**
	 * 根据成员信息获取标签关联成员数据的id
	 * @param orgId 机构id
	 * @param menberIds 成员id
	 * @param menberType 成员类型
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	List<TbQyTagRefPO> getTagRefPOByMenberIds(String orgId, String[] menberIds, int menberType) throws Exception;

	/**
	 * 获取企业今天标签同步的次数
	 * @param org 当前用户
	 * @param vip 是否vip
	 * @author sunqinghai
	 * @date 2017 -3-28
	 */
	int checkSyncTimeAndAdd(UserOrgVO org, boolean vip) throws Exception, BaseException;

	/**
	 * 获取当前标签下的成员
	 * @param tagId 标签id
	 * @author sunqinghai
	 * @date 2017 -3-28
	 */
	List<QyTagRefPageVO> getTagRefPageVOList(String tagId) throws Exception;

	/**
	 * 获取当前标签下的成员id
	 * @param tagId 标签id
	 * @param menberType 成员
	 * @author sunqinghai
	 * @date 2017 -3-28
	 */
	List<String> getTagRefMenberIdListByTagId(String tagId, int menberType) throws Exception;

	/**
	 * 获取当前标签下的成员id
	 * @param tagIds 标签ids
	 * @param menberType 成员
	 * @author sunqinghai
	 * @date 2017 -4-6
	 */
	List<String> getTagRefMenberIdListByTagIds(List<String> tagIds, int menberType) throws Exception;

	/**
	 * 根据微信标签id获取本地的标签id
	 * @param orgId 机构
	 * @param wxTagIds 微信标签id
	 * @author sunqinghai
	 * @date 2017 -4-6
	 */
	List<String> getTagIdsByWxTagIds(String orgId, String[] wxTagIds) throws Exception;
}
