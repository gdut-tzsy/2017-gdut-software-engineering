package cn.com.do1.component.contact.post.dao;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;

/**
 * Created by liuzhibin on 2017/5/15.
 */
public interface IContactPostDAO extends IBaseDAO {
	
	
	/**
	 * 根据职位id删除关联表信息
	 * @param postIds 职位id
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	void deletePositionRefByPostIds(String[] postIds) throws BaseException, Exception;
	
	/**
	 * 批量删除分类信息
	 * @param ids 分类id
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	void deletePositionRefByCatalogIds(String[] ids) throws BaseException, Exception;
	
	
	
}
