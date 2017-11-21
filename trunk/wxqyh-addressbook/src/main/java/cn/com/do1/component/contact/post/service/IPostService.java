package cn.com.do1.component.contact.post.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogRefPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostPO;
import cn.com.do1.component.contact.post.vo.ExportPersonVO;
import cn.com.do1.component.contact.post.vo.TbQyHrPostVO;

/**
 * Created by liuzhibin on 2017/5/15.
 */
public interface IPostService extends IBaseService {
	/**
	 * 查询职位列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	Pager queryPositionList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;
	/**
	 * 查询职位分类列表
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	List<TbQyHrPostCatalogPO> getAllPositionType(String type, UserOrgVO user) throws BaseException, Exception;
	/**
	 * 新增职位信息
	 * @throws BaseException
	 * @throws Exception
	 */
	void addPositionDetail(TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO, TbQyHrPostPO tbQyHrPostPO) throws BaseException, Exception;
	/**
	 * 判断职位是否存在
	 * @param name
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	boolean isExistPosition(String name, String orgId)throws BaseException, Exception;
	/**
	 * 根据id查询职位信息
	 * @param id
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	TbQyHrPostVO queryPositionDetail(String id, UserOrgVO user) throws BaseException, Exception;
	/**
	 * 更新职位信息
	 * @param id
	 * @throws BaseException
	 * @throws Exception
	 */
	void updatePositionDetail(TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO, TbQyHrPostPO tbQyHrPostPO, String id, UserOrgVO user) throws BaseException, Exception;
	/**
	 * 批量删除职位
	 * @param ids
	 * @throws BaseException
	 * @throws Exception
	 */
	void batchDeletePosition(String[] ids, UserOrgVO user) throws BaseException, Exception;
	/**
	 * 查询职位人员列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	Pager queryPositionPersonList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;
	/**
	 * 查询职位分类列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	Pager queryPositionTypeList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;
	/**
	 * 批量删除分类信息
	 * @param ids
	 * @throws BaseException
	 * @throws Exception
	 */
	void batchDeletePositionType(String[] ids, UserOrgVO user) throws BaseException, Exception;
	/**
	 * 判断职位类型是否存在
	 * @param name
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	boolean isExistPositionType(String name, String orgId) throws BaseException, Exception;
	/**
	 * 查询职位人员列表
	 * @param params
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	List<ExportPersonVO> searchPositionPersonList(Map<String, Object> params) throws BaseException, Exception;

	/**
	 * 初始化职位
	 * @param user
	 * @throws BaseException
	 * @throws Exception
	 */
	void initPostion(UserOrgVO user) throws BaseException, Exception ;

	/**
	 * 模糊搜索职位名称
	 * @param searchMap 模糊搜索条件
	 * @param orgId 机构id
	 * @return 返回list
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
	 */
	List<TbQyHrPostVO> getPositionByVagueSearch(Map searchMap, String orgId) throws BaseException, Exception;

	/**
	 * 获取该机构的所有已启用的职位
	 * @param orgId 机构id
	 * @return 返回set
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
	 */
	Set<String> getPositionSetByOrgId(String orgId) throws BaseException, Exception;

	/**
	 * 获取该机构的所有已启用的职位
	 * @param orgId 机构id
	 * @return 返回set
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
	 */
	List<String> getPositionListByOrgId(String orgId) throws BaseException, Exception;

	/**
	 * 同步从企业微信过来的用户的职位
	 * @param orgVO 机构vo
	 * @param userPO 用户po
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-6
	 * @version 1.0
     */
	void synUserPosition(List<TbQyUserInfoPO> addList, UserOrgVO orgVO) throws BaseException, Exception;

	/**
	 * 批量新增职位
	 * @param map 要新增的map
	 * @param orgVO 机构vo
	 * @param cataId 分类id
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-6
	 * @version 1.0
	 */
	void batchAddPosition(Map<String, String> map, UserOrgVO orgVO, String cataId) throws BaseException, Exception;
}
