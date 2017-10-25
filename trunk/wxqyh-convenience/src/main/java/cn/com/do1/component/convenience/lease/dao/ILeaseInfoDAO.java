package cn.com.do1.component.convenience.lease.dao;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface ILeaseInfoDAO extends IBaseDAO {
    /**
     * 获取租赁信息列表
     * @param searchMap
     * @param pager
     * @return
     */
    public Pager getLeaseInfo(Map<String, Object> searchMap, Pager pager) throws SQLException;
    
    /**
	 * @author zhongzhenze
	 * 2017-8-25
	 * 增加浏览数
	 * @param po
	 * @param increaseCount
	 * @throws Exception
	 * @throws BaseException
	 */
	public void increaseViewCount(TbYsjdLeaseInfoPO po,int increaseCount)throws Exception,BaseException;

}
