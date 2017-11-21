package cn.com.do1.component.contact.contact.service;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;

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
public interface ISelectUserMgrService extends IBaseService {
	/**
	 * 根据姓名或者手机号关键字搜索用户信息
	 * 
	 * @param map
	 * @param pager
	 * @param user
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-19
	 * @version 1.0
	 */
	Pager searchByNameOrPhone(Map<String, Object> map, Pager pager,
			UserInfoVO user) throws Exception, BaseException;

	/**
	 * 查询通讯录信息时处理右边的电话是否可拨打
	 * 
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Ma Quanyang
	 * @2015-8-12
	 * @version 1.0
	 */
	public String getSetFiled(String orgId) throws Exception, BaseException;

	/**
	 * 根据用户信息获取该用户可见的所有人员
	 * 
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong 2014-8-12
	 * @param user
	 */
	Pager findAlluserByUser(Pager pager, UserInfoVO user,
			Map<String, Object> params) throws Exception, BaseException;

	/**
	 * 按名字首写字母查询用户列表
	 * 
	 * @param keyWord
	 * @param user
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	Pager searchFirstLetter(String keyWord, Pager pager, UserInfoVO user,
			String agentCode) throws Exception, BaseException;

	/**
	 * 查询指定用户的常用联系人数据
	 * 
	 * @param userId
	 *            当前登录用户
	 * @param limit
	 *            要获取的记录数
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Xiang Dejun
	 * @2014-12-26
	 * @version 1.0
	 */
	List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit,
			String agentCode, String corpId) throws Exception, BaseException;

	Pager getUserGroup(Pager pager, Map<String, Object> map) throws Exception,
			BaseException;

	/**
	 * 获取指定用户Id的信息
	 * 
	 * @param userId
	 *            用户编号
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception,
			BaseException;

	/**
	 * 根据机构ID获取默认群组
	 * 
	 * @param orgId
	 * @return
	 * @author Chen Feixiong 2014-11-25
	 */
	List<TbQyUserGroupPO> getUserGroup(String orgId) throws Exception,
			BaseException;

	Pager getUserGroupPerson(Pager pager, Map<String, Object> map)
			throws Exception, BaseException;

	/**
	 * 分页查询--按创建时间倒序
	 *  @param searchMap
	 * @param pager
	 * @param departIds
	 * @param userIds @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchContact(Map searchMap, Pager pager, String[] departIds, String[] userIds) throws Exception,
			BaseException;

	/**
	 * 分页查询--按创建时间倒序
	 *  @param searchMap
	 * @param pager
	 * @param list
	 * @param userIds @return
	 * @throws Exception
	 * @throws BaseException
	 */
	Pager searchContactByDepartList(Map searchMap, Pager pager, List<TbDepartmentInfoVO> list, String[] userIds) throws Exception,BaseException;
	/**
	 * 按OrgId分页查询通讯录
	 * @param map
	 * @return
	 * @author Hejinjiao
	 * @2014-12-19
	 * @version 1.0
	 */
	Pager findUsersByOrgId(Map<String, Object> map,Pager pager)throws Exception, BaseException;

	/**
	 * 根据用户名获取用户部门信息
	 *
	 * @return
	 * @time 2016-06-24 
	 * @author 陈春武
	 */
	public List<UserDeptInfoVO> findUserDeptInfosByWxUIdAndOrgId(String[] wxuid, String orgId) throws Exception, BaseException;

	/**
	 * 批量处理分类账号
	 * @param batLimit          the bat limit
	 * @param effPersonNameList the eff person name list
	 * @param orgVo             the org vo
	 * @param effDataMap        the eff data map
	 * @param isMaxAuth         the is max auth
	 * @param effDepartmentList the eff department list
	 * @param dataList          the data list
	 * @param unEffDataList     the un eff data list
	 * @param wrongDataList     the wrong data list
	 * @return the void
	 * @author 陈春武
	 * @time 2016年06月24日
	 * Do sort account list in batch.
	 */
	 void doSortAccountListInBatch(int batLimit, List<String> effPersonNameList, UserOrgVO orgVo,
										 Map<String, Object> effDataMap, boolean isMaxAuth,
										 List<TbDepartmentInfoPO> effDepartmentList, List<TbDepartmentInfoPO> effRangeDepartmentList,
										 List<UserAccountInfoVO> dataList, List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList)throws Exception, BaseException;

	/**
	 * 将账号分成3类（合法、不合法、错误）
	 * @param listPage          the list page
	 * @param orgVo             the org vo
	 * @param isMaxAuth         the is max auth
	 * @param effDepartmentList the eff department list
	 * @param dataList          the data list
	 * @param unEffDataList     the un eff data list
	 * @param wrongDataList     the wrong data list
	 * @return the void
	 * @author 陈春武
	 * @time 2016年06月24日
	 * Sort 3 list.
	 */
	public void sortUserList2EffListAndUnEffListAndWrongList(List<String> listPage, UserOrgVO orgVo, boolean isMaxAuth,
															 List<TbDepartmentInfoPO> effDepartmentList, List<TbDepartmentInfoPO> effRangeDepartmentList, List<UserAccountInfoVO> dataList,
															 List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList)throws Exception, BaseException;

	/**
	 *优化用户按名字、拼音、电话搜索条件
	 * @return
	 * @author LiYiXin
	 * 2016-8-25
	 */
	Map<String, Object>   optimizeByNameOrPhone(Map<String, Object> map) throws Exception,BaseException;

	/**
	 * 批量处理分类账号
	 * @param batLimit          the bat limit 每次批量的限制数量
	 * @param longLimit         the long limit 最大长度限制
	 * @param effPersonNameList the eff person name list
	 * @param orgVo             the org vo
	 * @param effDataMap        the eff data map
	 * @param isMaxAuth         the is max auth
	 * @param effDepartmentList the eff department list
	 * @param dataList          the data list
	 * @param unEffDataList     the un eff data list
	 * @param wrongDataList     the wrong data list
	 * @return the void
	 * @author liyixin
	 * @time 2017年01月9日
	 * Do sort account list in batch.
	 */
	void doSortAccountListInBatch(int batLimit, int longLimit,List<String> effPersonNameList, UserOrgVO orgVo,
								  Map<String, Object> effDataMap, boolean isMaxAuth,
								  List<TbDepartmentInfoPO> effDepartmentList, List<TbDepartmentInfoPO> effRangeDepartmentList,
								  List<UserAccountInfoVO> dataList, List<UserAccountInfoVO> unEffDataList, List<UserAccountInfoVO> wrongDataList)throws Exception, BaseException;

	/**
	 * 查询用户信息，根据可见范围和目标部门和目标用户筛选
	 * @param searchMap 查询条件
	 * @param deptIdsList 目标部门
	 * @param userIdsList 目标用户
     * @return 返回所有的用户list
	 * @author Sun Qinghai
	 * @2017-3-7
     */
	List<TbQyUserInfoForPage> searchContactList(Map<String, Object> searchMap, List<String> deptIdsList, List<String> userIdsList) throws Exception, BaseException;
}
