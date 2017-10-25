package cn.com.do1.component.convenience.house.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.convenience.house.vo.TbYsjdHouseVO;
import cn.com.do1.component.convenience.house.vo.TbYsjdReportDangerVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IHouseService extends IBaseService{

    /**
     * 查询房屋信息列表
     * @param searchMap
     * @param pager
     * @return
     * @throws Exception
     * @throws BaseException
     */
    Pager searchHouse(Map<String, Object> searchMap,Pager pager) throws Exception, BaseException;

    /**
     * 根据id获取房屋信息
     * @param id
     * @return
     */
    TbYsjdHouseVO getHouseInfo(String id) throws SQLException;

    /**
     * 根据楼栋Id得到隐患信息
     * @param banId
     * @return
     */
    List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException;
}
