package cn.com.do1.component.building.building.dao.impl;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.component.building.building.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.po.Security;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.building.building.dao.IBuildingDAO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.dqdp.core.ConfigMgr;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: cuijianpeng
*/
public class BuildingDAOImpl extends BaseDAOImpl implements IBuildingDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(BuildingDAOImpl .class);
    
    final static String searchSQL = "SELECT * FROM tb_ysjd_ban "
            + " WHERE 1=1 AND community_name LIKE :communityName AND architecture_no LIKE :architectureNo "
            + " AND owner LIKE :owner AND grid_operator_name LIKE :gridOperatorName "
            + " AND ban_address LIKE :banAddress AND grid_name LIKE :gridName AND light = :light "
            + " ORDER BY creator_time DESC ";
    final static String countSQL = "select count(1) from (" + searchSQL.replaceAll("(?i)\\basc\\b|\\bdesc\\b", "").replaceAll("(?i)order\\s+by\\s+\\S+(\\s*[,\\s*\\S+])*", "")+"  ) a ";
    
    /**
     * <p>Title: searchTbYsjdBan</p>
     * <p>Description: 分页查询楼栋信息</p>
     * @param searchValue 条件
     * @param pager 分页
     * @return Pager 楼栋信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#searchTbYsjdBan(java.util.Map, cn.com.do1.common.dac.Pager)
     */
    @Override
    public Pager searchTbYsjdBan(Map searchValue, @Security(encode = "")Pager pager) throws Exception, BaseException {
        /**
         * 自己写好查询总页数以及查询语句后调用框架方法
         * TbDqdpUserPO 查询结果封装类
         * countSQL统计总条数的语句
         * searchSQL 查询数据的语句
         * searchValue 查询条件
         * pager分页信息
         */
        return super.pageSearchByField(TbYsjdBanVo.class, countSQL, searchSQL, searchValue, pager);
    }

    /**
     * <p>Title: searchTbYsjdBanList</p>
     * <p>Description: 列表查询楼栋信息</p>
     * @param searchValue 条件
     * @return List 楼栋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#searchTbYsjdBanList(java.util.Map)
     */
    @Override
    public List<TbYsjdBanVo> searchTbYsjdBanList(Map<String, Object> searchValue) throws SQLException {
        this.preparedSql(searchSQL);
        this.setPreValues(searchValue); //将参数设置进预置语句
        return getList(TbYsjdBanVo.class);
    }

    /**
     * <p>Title: getCommunity</p>
     * <p>Description: 查询社区</p>
     * @param depIdSys 社区节点id
     * @return List 社区名称
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getCommunity(java.lang.String)
     */
    @Override
    public List<CommunityVo> getCommunity(String depIdSys) throws SQLException {
        String sql = "SELECT a.id,a.department_name FROM tb_department_info a WHERE a.parent_depart = :depIdSys ORDER BY a.show_order ";
        this.preparedSql(sql);
        this.setPreValue("depIdSys", depIdSys);
        return getList(CommunityVo.class);
    }

    /**
     * <p>Title: getGrid</p>
     * <p>Description: 查询网格</p>
     * @param depId 社区id
     * @return List 网格名称
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getGrid(java.lang.String)
     */
    @Override
    public List<CommunityVo> getGrid(String depId) throws SQLException {
        String sql = "SELECT aa.id,aa.department_name "
                + " FROM tb_department_info aa ,(SELECT * FROM tb_department_info a WHERE a.parent_depart = :depId AND a.department_name = '网格站') b " 
                + " WHERE aa.parent_depart = b.id ORDER BY aa.show_order ";
        this.preparedSql(sql);
        this.setPreValue("depId", depId);
        return getList(CommunityVo.class);
    }

    /**
     * <p>Title: getGridOperator</p>
     * <p>Description: 查询网格员</p>
     * @param grid 网格id
     * @return List 网格员
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getGridOperator(java.lang.String)
     */
    @Override
    public List<GridOperatorVo> getGridOperator(String grid) throws SQLException {
        String sql = " SELECT a.USER_ID, a.PERSON_NAME "
                + " FROM tb_qy_user_info a , tb_qy_user_department_ref b " 
                + " WHERE a.ID = b.USER_ID AND b.department_id = :grid ";
        this.preparedSql(sql);
        this.setPreValue("grid", grid);
        return getList(GridOperatorVo.class);
    }

    /**
     * <p>Title: searchTbYsjdHouse</p>
     * <p>Description: 分页查询房屋信息</p>
     * @param searchValue 条件
     * @param pager 分页
     * @return Pager 房屋信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#searchTbYsjdHouse(java.util.Map, cn.com.do1.common.dac.Pager)
     */
    @Override
    public Pager searchTbYsjdHouse(Map<String, Object> searchValue, Pager pager) throws Exception, BaseException {
        String sql = "SELECT * FROM tb_ysjd_house "
                 + " WHERE 1=1 AND ban_no LIKE :architectureNo AND ban_address LIKE :banAddress "
                 + " AND house_no LIKE :houseNo AND property_owner LIKE :propertyOwner ORDER BY creator_time DESC ";
        String countSql = "select count(1) from (" + sql.replaceAll("(?i)\\basc\\b|\\bdesc\\b", "").replaceAll("(?i)order\\s+by\\s+\\S+(\\s*[,\\s*\\S+])*", "")+"  ) a ";
        return super.pageSearchByField(TbYsjdHouseVo.class, countSql, sql, searchValue, pager);
    }

    /**
     * <p>Description: 查询是否有楼栋</p>
     * @param banNo 楼栋编码
     * @return TbYsjdBanPo 楼栋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#isHavebanNo(java.lang.String)
     */
    @Override
    public TbYsjdBanPo isHavebanNo(String banNo) throws SQLException {
        String sql = "SELECT * FROM tb_ysjd_ban WHERE architecture_no = :banNo ";
        this.preparedSql(sql);
        this.setPreValue("banNo", banNo);
        return this.executeQuery(TbYsjdBanPo.class);
    }

    /**
     * <p>Title: getCommunityByCommunityName</p>
     * <p>Description: 根据社区名称查询社区</p>
     * @param communityName 社区名称
     * @return CommunityVo
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getCommunityByCommunityName(java.lang.String)
     */
    @Override
    public CommunityVo getCommunityByCommunityName(String communityName) throws SQLException {
        String depIdSys = ConfigMgr.get("building", "depIdSys", "");
        String sql = "SELECT a.id,a.department_name FROM tb_department_info a WHERE a.parent_depart = :depIdSys AND a.department_name = :communityName ORDER BY a.show_order";
        this.preparedSql(sql);
        this.setPreValue("depIdSys", depIdSys);
        this.setPreValue("communityName", communityName);
        return this.executeQuery(CommunityVo.class);
    }

    /**
     * <p>Title: getCommunityByGridName</p>
     * <p>Description: 根据网格名称查询网格</p>
     * @param gridName 网格名称
     * @param communityId 社区id
     * @return CommunityVo
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getCommunityByGridName(java.lang.String)
     */
    @Override
    public CommunityVo getCommunityByGridName(String gridName, String communityId) throws SQLException {
        String sql = "SELECT aa.id,aa.department_name "
                + " FROM tb_department_info aa ,(SELECT * FROM tb_department_info a WHERE a.parent_depart = :communityId AND a.department_name = '网格站') b " 
                + " WHERE aa.parent_depart = b.id AND aa.department_name =:gridName ORDER BY aa.show_order ";
        this.preparedSql(sql);
        this.setPreValue("communityId", communityId);
        this.setPreValue("gridName", gridName);
        return this.executeQuery(CommunityVo.class);
    }

    /**
     * <p>Title: getbanByarchitectureNo</p>
     * <p>Description: 根据建筑编码查询楼栋</p>
     * @param architectureNo 建筑编码
     * @return TbYsjdBanPo 楼栋信息 
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getbanByarchitectureNo(java.lang.String)
     */
    @Override
    public TbYsjdBanPo getbanByarchitectureNo(String architectureNo) throws SQLException {
        String sql ="SELECT * FROM tb_ysjd_ban WHERE architecture_no = :architectureNo ";
        this.preparedSql(sql);
        this.setPreValue("architectureNo", architectureNo);
        return this.executeQuery(TbYsjdBanPo.class);
    }

    /**
     * <p>Title: delPoByarchitectureNo</p>
     * <p>Description: 根据建筑编码删除楼栋</p>
     * @param architectureNo 建筑编码
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#delPOByarchitectureNo(java.lang.String)
     */
    @Override
    public void delPoByArchitectureNo(String architectureNo) throws SQLException {
        String sql ="DELETE FROM tb_ysjd_ban WHERE architecture_no = :architectureNo ";
        this.preparedSql(sql);
        this.setPreValue("architectureNo", architectureNo);
        this.executeUpdate();
    }

    /**
     * <p>Title: delPoByHouseNo</p>
     * <p>Description: 根据房屋编码删除房屋</p>
     * @param houseNo 房屋编码
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#delPOByHouseNo(java.lang.String)
     */
    @Override
    public void delPoByHouseNo(String houseNo) throws SQLException {
        String sql ="DELETE FROM tb_ysjd_house WHERE house_no = :houseNo ";
        this.preparedSql(sql);
        this.setPreValue("houseNo", houseNo);
        this.executeUpdate();
    }

    /**
     * <p>Title: getbanByHouseNo</p>
     * <p>Description: 根据房屋编码查询房屋</p>
     * @param architectureNo 房屋编码
     * @return TbYsjdBanPo 房屋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.dao.IBuildingDAO#getbanByHouseNo(java.lang.String)
     */
    @Override
    public TbYsjdHousePo getbanByHouseNo(String houseNo) throws SQLException {
        String sql ="SELECT * FROM tb_ysjd_house WHERE house_no = :houseNo ";
        this.preparedSql(sql);
        this.setPreValue("houseNo", houseNo);
        return this.executeQuery(TbYsjdHousePo.class);
    }

    @Override
    public List<CommunityVo> getGridByDeptId(String deptId) throws SQLException {
        String sql="select id,department_name from tb_department_info where parent_depart = :deptId ORDER BY show_order";
        this.preparedSql(sql);
        this.setPreValue("deptId",deptId);
        return this.getList(CommunityVo.class);
    }

    @Override
    public Pager searchTbYsjdBanByGrider(Map<String, Object> searchMap, Pager pager) throws SQLException {
        String sql="select * from tb_ysjd_ban where grid=:gridId";
        String countSql="select count(1) from tb_ysjd_ban where grid=:gridId";
        return pageSearchByField(TbYsjdBanPo.class, countSql, sql, searchMap, pager);
    }

    @Override
    public TbYsjdBanVo getBanVoBybanNoId(String banNoId) throws SQLException {
        String sql ="SELECT * FROM tb_ysjd_ban WHERE id = :banNoId ";
        this.preparedSql(sql);
        this.setPreValue("banNoId", banNoId);
        return this.executeQuery(TbYsjdBanVo.class);
    }

    @Override
    public List<TbYsjdHousePo> getHouseList(String banId) throws SQLException {
        String sql ="SELECT * FROM tb_ysjd_house WHERE ban_no = :banNo ";
        this.preparedSql(sql);
        this.setPreValue("banNo",banId);
        return this.getList(TbYsjdHousePo.class);
    }

    @Override
    public List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException {
        String sql="select id,describes,light_status,report_time,status,report_type from tb_ysjd_reportdanger where the_ban=:banId";
        this.preparedSql(sql);
        this.setPreValue("banId",banId);
        return this.getList(TbYsjdReportDangerVO.class);
    }

    @Override
    public List<BanStatisticsVo> getNumberOfBanByCommunityAndLight() throws SQLException {
        String sql="SELECT community_name,SUM(green) as green_num,SUM(yellow) as yellow_num,SUM(red) as red_num " +
                "FROM (  " +
                "   SELECT    " +
                "          CASE WHEN light='0' THEN 1 ELSE 0 END AS green," +
                "          CASE WHEN light='1' THEN 1 ELSE 0 END AS yellow," +
                "          CASE WHEN light='2' THEN 1 ELSE 0 END AS red," +
                "          community_name" +
                "   FROM tb_ysjd_ban  " +
                ")AS T  " +
                "GROUP BY T.community_name";
        this.preparedSql(sql);
        return this.getList(BanStatisticsVo.class);
    }

    @Override
    public Pager searchReportDanger(Map<String, Object> searchMap, Pager pager) throws SQLException {
        String fromSql=" from tb_ysjd_reportdanger t join tb_qy_user_info u on t.creator_user=u.USER_ID";

        String paramSql=" where 1=1"+
                " and t.the_ban=:banId ";

        String orderbySql="order by t.creator_time desc";
        String sql="select t.*,u.PERSON_NAME"+fromSql+paramSql+orderbySql;
        String countsql="select count(1)"+fromSql+paramSql;

        return pageSearchByField(TbYsjdReportDangerVO.class,countsql,sql,searchMap,pager);
    }

    @Override
    public List<TbYsjdBanPo> getBanList(String gridId) throws SQLException {
        String sql ="select * from tb_ysjd_ban where 1=1 and grid=:gridId";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(!AssertUtil.isEmpty(gridId)){
            paramMap.put("gridId",gridId);
        }
        return this.searchByField(TbYsjdBanPo.class, sql, paramMap);
    }
    
    /**
     * 查询网格员信息
     * <p>Description: TODO</p>
     * @return
     * @throws SQLException
     * <p>Author: 邹乐乐  </p>
     * <p>Date: 2017年11月13日</p>
     */
    @Override
    public List<GridUserVo> getGridUserVoList() throws SQLException {
        String sql = "SELECT u.id,u.PERSON_NAME,d.department_name,d.id AS department_id FROM tb_qy_user_info u ";
        sql += "LEFT JOIN tb_qy_user_department_ref ref ON u.user_id = ref.user_id ";
        sql += "LEFT JOIN tb_department_info d ON ref.department_id = d.id ";
        this.preparedSql(sql);
        return getList(GridUserVo.class);
    }
}
