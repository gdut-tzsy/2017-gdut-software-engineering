package cn.com.do1.component.building.building.dao;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.component.building.building.vo.*;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: cuijianpeng
*/
public interface IBuildingDAO extends IBaseDAO {
    /**
     * <p>Description: 分页查询楼栋信息</p>
     * @param searchValue 条件
     * @param pager 分页
     * @return Pager 楼栋信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    Pager searchTbYsjdBan (Map searchValue, Pager pager) throws Exception, BaseException;

    /**
     * <p>Description: 列表查询楼栋信息</p>
     * @param searchValue 条件
     * @return List 楼栋信息
     * @throws SQLException SQLException异常
     */
    List<TbYsjdBanVo> searchTbYsjdBanList(Map<String, Object> searchValue) throws SQLException;

    /**
     * <p>Description: 查询社区</p>
     * @param depIdSys 社区节点id
     * @return List 社区名称
     * @throws SQLException SQLException异常
     */
    List<CommunityVo> getCommunity(String depIdSys) throws SQLException;

    /**
     * <p>Description: 查询网格</p>
     * @param depId 社区id
     * @return List 网格名称
     * @throws SQLException SQLException异常
     */
    List<CommunityVo> getGrid(String depId) throws SQLException;

    /**
     * <p>Description: 查询网格员</p>
     * @param grid 网格id
     * @return List 网格员
     * @throws SQLException SQLException异常
     */
    List<GridOperatorVo> getGridOperator(String grid) throws SQLException;
    
    /**
     * <p>Description: 分页查询房屋信息</p>
     * @param searchValue 条件
     * @param pager 分页
     * @return Pager 房屋信息* <p>Description: 根据网格名称查询网格</p>
     * @param gridName 网格名称
     * @return CommunityVo
     * @throws SQLException SQLException异常
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    Pager searchTbYsjdHouse(Map<String, Object> searchValue, Pager pager) throws Exception, BaseException;
    
    /**
     * <p>Description: 查询是否有楼栋</p>
     * @param banNo 楼栋编码
     * @return TbYsjdBanPo 楼栋信息
     * @throws SQLException SQLException异常
     */
    TbYsjdBanPo isHavebanNo(String banNo) throws SQLException;

    /**
     * <p>Description: 根据社区名称查询社区</p>
     * @param communityName 社区名称
     * @return CommunityVo
     * @throws SQLException SQLException异常
     */
    CommunityVo getCommunityByCommunityName(String communityName) throws SQLException;

    /**
     * <p>Description: 根据网格名称查询网格</p>
     * @param gridName 网格名称
     * @param communityId 社区id
     * @return CommunityVo
     * @throws SQLException SQLException异常
     */
    CommunityVo getCommunityByGridName(String gridName, String communityId) throws SQLException;

    /**
     * <p>Description: 根据建筑编码查询楼栋</p>
     * @param architectureNo 建筑编码
     * @return TbYsjdBanPo 楼栋信息 
     * @throws SQLException SQLException异常
     */
    TbYsjdBanPo getbanByarchitectureNo(String architectureNo) throws SQLException;

    /**
     * <p>Description: 根据建筑编码删除楼栋</p>
     * @param architectureNo 建筑编码
     * @throws SQLException SQLException异常
     */
    public void delPoByArchitectureNo(String architectureNo) throws SQLException;

    /**
     * <p>Description: 根据房屋编码删除房屋</p>
     * @param houseNo 房屋编码
     * @throws SQLException SQLException异常
     */
    public void delPoByHouseNo(String houseNo) throws SQLException;

    /**
     * <p>Description: 根据房屋编码查询房屋</p>
     * @param architectureNo 房屋编码
     * @return TbYsjdBanPo 房屋信息
     * @throws SQLException SQLException异常
     */
    TbYsjdHousePo getbanByHouseNo(String houseNo) throws SQLException;

    List<CommunityVo> getGridByDeptId(String deptId) throws SQLException;

    Pager searchTbYsjdBanByGrider(Map<String, Object> searchMap, Pager pager) throws SQLException;



    /**
     * <p>Description: 根据楼栋id查询楼栋</p>
     * @param banNoId 楼栋id
     * @return TbYsjdBanVo 楼栋vo
     * @throws SQLException SQLException异常
     */
    TbYsjdBanVo getBanVoBybanNoId(String banNoId) throws SQLException;

    List<TbYsjdHousePo> getHouseList(String banId) throws SQLException;

    public List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException;

    List<BanStatisticsVo> getNumberOfBanByCommunityAndLight() throws SQLException;

    Pager searchReportDanger(Map<String, Object> searchMap,Pager pager) throws SQLException;

    List<TbYsjdBanPo> getBanList(String gridId) throws SQLException;
    /**
     * 查询网格员信息
     * <p>Description: TODO</p>
     * @return
     * @throws SQLException
     * <p>Author: 邹乐乐  </p>
     * <p>Date: 2017年11月13日</p>
     */
    public List<GridUserVo> getGridUserVoList() throws SQLException;
}
