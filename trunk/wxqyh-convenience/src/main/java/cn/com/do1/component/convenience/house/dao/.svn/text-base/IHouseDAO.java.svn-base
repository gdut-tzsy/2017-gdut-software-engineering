package cn.com.do1.component.convenience.house.dao;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
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
public interface IHouseDAO extends IBaseDAO {

    Pager getHousePageData(Map<String, Object> map, Pager pager) throws SQLException;

    TbYsjdHouseVO getHouseInfo(String id) throws SQLException;

    List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException;
}
