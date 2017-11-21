package cn.com.do1.component.contact.post.dao.impl;

import java.util.HashMap;
import java.util.Map;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.contact.post.dao.IContactPostDAO;

/**
 * Created by liuzhibin on 2017/5/15.
 */
public class ContactPostDAOImpl extends BaseDAOImpl implements IContactPostDAO {
	final private static String deletePositionRef = "delete from tb_qy_hr_post_catalog_ref where post_id in (:ids) ";

	@Override
	public void deletePositionRefByPostIds(String[] ids) throws BaseException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		updateByField(deletePositionRef, map, true);
	}

	final private static String deleteTypeRef = "delete from tb_qy_hr_post_catalog_ref where catalog_id in (:ids) ";

	@Override
	public void deletePositionRefByCatalogIds(String[] ids) throws BaseException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		updateByField(deleteTypeRef, map, true);
	}

}
