package cn.com.do1.component.convenience.house.dao.impl;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.convenience.house.dao.IHouseDAO;
import cn.com.do1.component.convenience.house.vo.TbYsjdHouseVO;
import cn.com.do1.component.convenience.house.vo.TbYsjdReportDangerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class HouseDAOImpl extends BaseDAOImpl implements IHouseDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(HouseDAOImpl.class);

    @Override
    public Pager getHousePageData(Map<String, Object> map, Pager pager) throws SQLException {
        String fromSql=" from tb_ysjd_house t join tb_ysjd_ban b on t.ban_no=b.architecture_no";

        String paramSql=" where 1=1"+
                " and b.owner=:owner "+
                " and t.house_address like :houseAddress"+
                " and b.light=:light";

        String sql="select t.*,b.community_name,b.grid_name,b.grid_operator_name,b.light"+fromSql+paramSql;
        String countsql="select count(1)"+fromSql+paramSql;
        return pageSearchByField(TbYsjdHouseVO.class,countsql,sql,map,pager);
    }

    @Override
    public TbYsjdHouseVO getHouseInfo(String id) throws SQLException {
        String fromSql=" from tb_ysjd_house t join tb_ysjd_ban b on t.ban_no=b.architecture_no";

        String paramSql=" where 1=1"+
                " and t.id=:id ";

        String sql="select t.*,b.community_name,b.grid_name,b.grid_operator_name,b.light,b.id as ban_id"+fromSql+paramSql;
        this.preparedSql(sql);
        this.setPreValue("id",id);
        return this.executeQuery(TbYsjdHouseVO.class);
    }

    @Override
    public List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException {
        String sql="select id,describes,light_status,report_time,status,report_type from tb_ysjd_reportdanger where the_ban=:banId";
        this.preparedSql(sql);
        this.setPreValue("banId",banId);
        return this.getList(TbYsjdReportDangerVO.class);
    }
}
