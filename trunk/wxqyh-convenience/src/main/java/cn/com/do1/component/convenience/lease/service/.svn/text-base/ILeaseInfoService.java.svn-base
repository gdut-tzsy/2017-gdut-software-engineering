package cn.com.do1.component.convenience.lease.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;
import cn.com.do1.component.convenience.lease.vo.TbYsjdLeaseInfoVO;

import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public interface ILeaseInfoService extends IBaseService {

    Pager searchLeaseInfo(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException;

    /**
     * PO转VO
     */
    public TbYsjdLeaseInfoVO getLeaseInfoVOByPO(TbYsjdLeaseInfoPO tbYsjdLeaseInfoPO) throws Exception,BaseException;
    
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
