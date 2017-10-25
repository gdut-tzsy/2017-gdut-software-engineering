package cn.com.do1.component.convenience.lease.dao.impl;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.convenience.lease.dao.ILeaseInfoDAO;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;
import cn.com.do1.component.convenience.lease.vo.TbYsjdLeaseInfoVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class LeaseInfoDAOImpl extends BaseDAOImpl implements ILeaseInfoDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(LeaseInfoDAOImpl.class);

    @Override
    public Pager getLeaseInfo(Map<String, Object> searchMap, Pager pager) throws SQLException {
        String fromSql=" from tb_ysjd_lease_info t";

        String paramSql=" where 1=1 "+
                " and t.rent>=:rentStart and t.rent<=:rentEnd "+
                " and t.Room=:room " +
                " and t.rental_mode=:rentalMode "+
                " and t.status=:status "+
                " and t.type=:type "+
                " and t.creator_user_name like :creatorUserName "+
                " and t.TITLE like :title "+
                " and t.creator_user =:creator_user ";

        if (searchMap.get("startTime")!=null){
            paramSql+=" and t.creator_time between :startTime and :endTime ";
        }

        String orderbySql="order by t.creator_time desc";
        String sql="select *"+fromSql+paramSql+orderbySql;
        String countsql="select count(1)"+fromSql+paramSql;
        return pageSearchByField(TbYsjdLeaseInfoVO.class,countsql,sql,searchMap,pager);
    }
    
    
    public void increaseViewCount(TbYsjdLeaseInfoPO po,int increaseCount)throws Exception,BaseException{
    	// TODO 自动生成的方法存根
    	String sql = "update tb_ysjd_lease_info set browser = browser + :increaseCount where id = :id";
    	this.preparedSql(sql);
    	this.setPreValue("increaseCount", increaseCount);
    	this.setPreValue("id", po.getId());
    	this.executeUpdate();
    }
}
