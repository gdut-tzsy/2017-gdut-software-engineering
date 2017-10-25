package cn.com.do1.component.convenience.house.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.component.convenience.house.dao.*;
import cn.com.do1.component.convenience.house.service.IHouseService;
import cn.com.do1.component.convenience.house.vo.TbYsjdHouseVO;
import cn.com.do1.component.convenience.house.vo.TbYsjdReportDangerVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
@Service("houseService")
public class HouseServiceImpl extends BaseService implements IHouseService {
    private final static transient Logger logger = LoggerFactory.getLogger(HouseServiceImpl.class);

    private IHouseDAO houseDAO;
    @Resource
    public void setHouseDAO(IHouseDAO houseDAO) {
        this.houseDAO = houseDAO;
        setDAO(houseDAO);
    }

    public Pager searchHouse(Map<String, Object> searchMap,Pager pager) throws Exception, BaseException {
        return houseDAO.getHousePageData(searchMap,pager);
    }

    @Override
    public TbYsjdHouseVO getHouseInfo(String id) throws SQLException {
        return houseDAO.getHouseInfo(id);
    }

    @Override
    public List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException {
        return houseDAO.getDangerInfo(banId);
    }
}
