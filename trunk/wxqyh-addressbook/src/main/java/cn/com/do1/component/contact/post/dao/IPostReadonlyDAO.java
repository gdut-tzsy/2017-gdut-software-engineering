package cn.com.do1.component.contact.post.dao;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogRefPO;
import cn.com.do1.component.contact.post.vo.TbQyHrPostVO;
import cn.com.do1.component.contact.post.vo.ExportPersonVO;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhibin on 2017/5/22.
 */
public interface IPostReadonlyDAO extends IBaseDAO {
	/**
	 * 查询职位列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	Pager queryPositionList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;

	/**
	 * 查询职位分类列表
	 * @param orgId 机构id
	 * @param isUse 是否被使用
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	List<TbQyHrPostCatalogPO> getAllPositionType(String orgId, String isUse) throws BaseException, Exception;

	/**
	 * 判断职位是否存在
	 * @param postName 职位名称
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	boolean isExistPosition(String postName, String orgId) throws BaseException, Exception;

	/**
	 * 根据id查询职位信息
	 * @param id 主键id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	TbQyHrPostVO queryPositionDetail(String id, String orgId) throws BaseException, Exception;

	/**
	 * 根据职位id获取关联数据
	 * @param id 主键id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	TbQyHrPostCatalogRefPO updatePositionByPostId(String id) throws BaseException, Exception;

	/**
	 * 查询职位人员列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	Pager queryPositionPersonList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;

	/**
	 * 查询职位分类列表
	 * @param searchMap
	 * @param pager
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	Pager queryPositionTypeList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception;

	/**
	 * 判断职位类型是否存在
	 * @param name 名称
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	boolean isExistPositionType(String name, String orgId) throws BaseException, Exception;

	/**
	 *根据类型名称获取分类po
	 * @param catalog 类型名称
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	TbQyHrPostCatalogPO getCatalogPOByName(String catalog) throws BaseException, Exception;

	/**
	 * 获取职位人员列表
	 * @param params
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	List<ExportPersonVO> searchPositionPersonList(Map<String, Object> params) throws BaseException, Exception;

	/**
	 * 根据职位id获取关联表po
	 * @param id 主键id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	TbQyHrPostCatalogRefPO getCatalogRefPOByPostId(String id) throws BaseException, Exception;

	/**
	 * 根据类型ids获取关联表po
	 * @param ids id列表
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
     */
	List<TbQyHrPostCatalogRefPO> getRefPOByCatalogIds(String[] ids) throws BaseException, Exception;
	/**
	 * 判断职位下是否存在职位人员
	 * @param name 职位名称
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	boolean isExistPerson(String name, String orgId) throws BaseException, Exception;
	/**
	 * 根据orgId获取职位名称
	 * @param pager
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	Pager getPositionByOrgId(Pager pager, String orgId) throws BaseException, Exception;
	/**
	 * 根据orgId获取已存在职位
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	List<String> getExistPositionByOrgId(String orgId) throws BaseException, Exception;

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
	 * @return 返回list
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
     */
	List<String> getPositionListByOrgId(String orgId) throws BaseException, Exception;
	
}
