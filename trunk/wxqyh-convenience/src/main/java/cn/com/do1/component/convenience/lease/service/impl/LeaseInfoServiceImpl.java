package cn.com.do1.component.convenience.lease.service.impl;

import cn.com.do1.common.annotation.bean.FormatMask;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.component.convenience.lease.dao.*;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;
import cn.com.do1.component.convenience.lease.service.ILeaseInfoService;
import cn.com.do1.component.convenience.lease.vo.TbYsjdLeaseInfoVO;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
@Service("leaseInfoService")
public class LeaseInfoServiceImpl extends BaseService implements ILeaseInfoService {
    private final static transient Logger logger = LoggerFactory.getLogger(LeaseInfoServiceImpl.class);

    private ILeaseInfoDAO leaseInfoDAO;
    @Resource
    public void setLeaseDAO(ILeaseInfoDAO leaseInfoDAO) {
        this.leaseInfoDAO = leaseInfoDAO;
        setDAO(leaseInfoDAO);
    }

    
    private ILeaseInfoService leaseInfoService; 
    public ILeaseInfoService getLeaseInfoService() {
		return leaseInfoService;
	}
    @Resource(name = "leaseInfoService")
	public void setLeaseInfoService(ILeaseInfoService leaseInfoService) {
		this.leaseInfoService = leaseInfoService;
	}
    
    public Pager searchLeaseInfo(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
        return leaseInfoDAO.getLeaseInfo(searchMap,pager);
    }
    @Override
    public TbYsjdLeaseInfoVO getLeaseInfoVOByPO(TbYsjdLeaseInfoPO po) throws Exception,BaseException{
    	TbYsjdLeaseInfoVO vo = new TbYsjdLeaseInfoVO();
    	vo.setId(po.getId());
    	vo.setTitle(po.getTitle());
    	vo.setRoom(po.getRoom());
        vo.setRent(po.getRent());
        vo.setCommunity(po.getCommunity());
        vo.setFloor(po.getFloor());
        vo.setType(po.getType());
        vo.setAddress(po.getAddress());
        vo.setPaymentMethod(po.getPaymentMethod());
        vo.setArea(po.getArea());
        vo.setPhone(po.getPhone());
        vo.setRenovation(po.getRenovation());
        vo.setRentalMode(po.getRentalMode());
        vo.setHouseLight(po.getHouseLight());
        vo.setPeriphery(po.getPeriphery());
        vo.setStatus(po.getStatus());
        vo.setCreatorUser(po.getCreatorUser());
        vo.setCreatorUserName(po.getCreatorUserName());
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        vo.setCreatorTime(format.format(po.getCreatorTime()));
        
        vo.setBrowser(po.getBrowser());
        vo.setImage(po.getImage());
        return vo;
    }
    @Override
	public void increaseViewCount(TbYsjdLeaseInfoPO po, int increaseCount)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		leaseInfoDAO.increaseViewCount(po,increaseCount);
	}

}
