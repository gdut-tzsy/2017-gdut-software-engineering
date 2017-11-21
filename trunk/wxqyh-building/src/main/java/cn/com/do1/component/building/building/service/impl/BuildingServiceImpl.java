package cn.com.do1.component.building.building.service.impl;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.com.do1.component.building.building.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.building.building.dao.IBuildingDAO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.component.building.building.service.IBuildingService;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: cuijianpeng
*/
@Service("buildingService")
public class BuildingServiceImpl extends BaseService implements IBuildingService {
    private final static transient Logger logger = LoggerFactory.getLogger(BuildingServiceImpl .class);

    /**
     * <p>Field buildingDAO: buildingDAO</p>
     */
    private IBuildingDAO buildingDAO;
    
    @Resource
    public void setBuildingDAO(IBuildingDAO buildingDAO) {
        this.buildingDAO = buildingDAO;
        setDAO(buildingDAO);
    }

    /**
     * <p>Title: searchTbYsjdBan</p>
     * <p>Description: 分页查询楼栋信息</p>
     * @param searchMap 条件
     * @param pager 分页
     * @return Pager 楼栋信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#searchTbYsjdBan(java.util.Map, cn.com.do1.common.dac.Pager)
     */
    @Override 
    public Pager searchTbYsjdBan(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
        String light ="";
        if (!AssertUtil.isEmpty(searchMap.get("light"))) {
            light = searchMap.get("light").toString();
        }
        if (!AssertUtil.isEmpty(light)) {
            if ("红灯".indexOf(light)>-1) {
                searchMap.put("light", 2);
            } else if ("黄灯".indexOf(light)>-1) {
                searchMap.put("light", 1);
            } else if ("绿灯".indexOf(light)>-1) {
                searchMap.put("light", 0);
            }
        }
        return this.buildingDAO .searchTbYsjdBan(searchMap, pager);
    }
    
    /**
     * <p>Title: searchTbYsjdBanList</p>
     * <p>Description: 列表查询楼栋信息</p>
     * @param searchValue 条件
     * @return List 楼栋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#searchTbYsjdBanList(java.util.Map)
     */
    @Override
    public List<TbYsjdBanVo> searchTbYsjdBanList(Map<String, Object> searchValue) throws SQLException {
        return this.buildingDAO .searchTbYsjdBanList(searchValue);
    }

    /**
     * <p>Description: 查询社区</p>
     * @param depIdSys 社区节点id
     * @return List 社区名称
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getCommunity(java.lang.String)
     */
    @Override
    public List<CommunityVo> getCommunity(String depIdSys) throws SQLException {
        return this.buildingDAO.getCommunity(depIdSys);
    }

    /**
     * <p>Title: getGrid</p>
     * <p>Description: 查询网格</p>
     * @param depId 社区id
     * @return List 网格名称
     * @throws SQLException SQLException异常
     */
    @Override
    public List<CommunityVo> getGrid(String depId) throws SQLException {
        return this.buildingDAO.getGrid(depId);
    }

    /**
     * <p>Title: batchDeleteBuilding</p>
     * <p>Description: 批量删除楼栋</p>
     * @param ids 楼栋id集合
     * @return String 信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#batchDeleteBuilding(java.lang.String[])
     */
    @Override
    public String batchDeleteBuilding(String[] ids) throws Exception, BaseException {
        StringBuilder rMsg=new StringBuilder("");
        if (ids != null && ids.length > 0){ 
            rMsg.append("");
            List<String> list=Arrays.asList(ids);
            TbYsjdBanPo po;
            int failCount=0;
            for (String id : list) {
                po= this.searchByPk(TbYsjdBanPo.class, id);
                if(po==null){
                    rMsg.append(id+":不存在;");
                    failCount=failCount+1;
                    continue;
                }
                this.buildingDAO.delete(po);
            }
            if(failCount==0){
                rMsg.append("删除成功");
            }
        }else{
            rMsg.append("没有可删除的数据!");
        }
        return rMsg.toString();
    }

    /**
     * <p>Title: getGridOperator</p>
     * <p>Description: 查询网格员</p>
     * @param grid 网格id
     * @return List 网格员
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getGridOperator(java.lang.String)
     */
    @Override
    public List<GridOperatorVo> getGridOperator(String grid) throws SQLException {
        return this.buildingDAO.getGridOperator(grid);
    }

    /**
     * <p>Title: searchTbYsjdBan</p>
     * <p>Description: 分页房屋楼栋信息</p>
     * @param searchMap 条件
     * @param pager 分页
     * @return Pager 房屋信息
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#searchTbYsjdBan(java.util.Map, cn.com.do1.common.dac.Pager)
     */
    @Override
    public Pager searchTbYsjdHouse(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
        return this.buildingDAO.searchTbYsjdHouse(searchMap, pager);
    }

    /**
     * <p>Title: isHavebanNo</p>
     * <p>Description: 查询是否有楼栋</p>
     * @param banNo 楼栋编码
     * @return TbYsjdBanPo 楼栋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#isHavebanNo(java.lang.String)
     */
    @Override
    public TbYsjdBanPo isHavebanNo(String banNo) throws SQLException {
        return this.buildingDAO.isHavebanNo(banNo);
    }

    /**
     * <p>Title: getCommunityByCommunityName</p>
     * <p>Description: 根据社区名称查询社区</p>
     * @param communityName 社区名称
     * @return CommunityVo
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getCommunityByCommunityName(java.lang.String)
     */
    @Override
    public CommunityVo getCommunityByCommunityName(String communityName) throws SQLException {
        return this.buildingDAO.getCommunityByCommunityName(communityName);
    }

    /**
     * <p>Description: 根据网格名称查询网格</p>
     * @param gridName 网格名称
     * @param communityId 社区id
     * @return CommunityVo
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getCommunityByGridName(java.lang.String)
     */
    @Override
    public CommunityVo getCommunityByGridName(String gridName, String communityId) throws SQLException {
        return this.buildingDAO.getCommunityByGridName(gridName, communityId);
    }

    /**
     * <p>Title: getbanByarchitectureNo</p>
     * <p>Description: 根据建筑编码查询楼栋</p>
     * @param architectureNo 建筑编码
     * @return TbYsjdBanPo 楼栋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getbanByarchitectureNo(java.lang.String)
     */
    @Override
    public TbYsjdBanPo getbanByarchitectureNo(String architectureNo) throws SQLException {
        return this.buildingDAO.getbanByarchitectureNo(architectureNo);
    }

    /**
     * <p>Title: delPOByArchitectureNo</p>
     * <p>Description: 根据建筑编码删除楼栋</p>
     * @param architectureNo 建筑编码
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#delPOByarchitectureNo(java.lang.String)
     */
    @Override
    public void delPoByArchitectureNo(String architectureNo) throws SQLException {
        this.buildingDAO.delPoByArchitectureNo(architectureNo);
    }

    /**
     * <p>Title: batchSaveList</p>
     * <p>Description: 批量保存楼栋</p>
     * @param poList 楼栋po
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#batchSaveList(java.util.List)
     */
    @Override
    public void batchSaveList(List<TbYsjdBanPo> poList) throws Exception, BaseException {
        if(AssertUtil.isEmpty(poList)){
            return;
        }
        this.buildingDAO.execBatchInsert(poList);
    }

    /**
     * <p>Title: delPoByHouseNo</p>
     * <p>Description: 根据房屋编码删除房屋</p>
     * @param houseNo 房屋编码
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#delPOByHouseNo(java.lang.String)
     */
    @Override
    public void delPoByHouseNo(String houseNo) throws SQLException {
        this.buildingDAO.delPoByHouseNo(houseNo);
    }

    /**
     * <p>Title: getbanByHouseNo</p>
     * <p>Description: 根据房屋编码查询房屋</p>
     * @param architectureNo 房屋编码
     * @return TbYsjdBanPo 房屋信息
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getbanByHouseNo(java.lang.String)
     */
    @Override
    public TbYsjdHousePo getbanByHouseNo(String houseNo) throws SQLException {
        return this.buildingDAO.getbanByHouseNo(houseNo);
    }

    /**
     * <p>Title: batchSaveListHouse</p>
     * <p>Description: 批量保存房屋</p>
     * @param poList 房屋po
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#batchSaveListHouse(java.util.List)
     */
    @Override
    public void batchSaveListHouse(List<TbYsjdHousePo> poList) throws Exception, BaseException {
        if(AssertUtil.isEmpty(poList)){
            return;
        }
        this.buildingDAO.execBatchInsert(poList);
    }

    /**
     * <p>Title: getBanVoBybanNoId</p>
     * <p>Description: 根据楼栋id查询楼栋</p>
     * @param banNoId 楼栋id
     * @return TbYsjdBanVo 楼栋vo
     * @throws SQLException SQLException异常
     * @see cn.com.do1.component.building.building.service.IBuildingService#getBanVoBybanNoId(java.lang.String)
     */
    @Override
    public TbYsjdBanVo getBanVoBybanNoId(String banNoId) throws SQLException {
        return this.buildingDAO.getBanVoBybanNoId(banNoId);
    }

    @Override
    public List<TbYsjdHousePo> getHouseList(String banId) throws SQLException {
        return buildingDAO.getHouseList(banId);
    }

    @Override
    public List<TbYsjdReportDangerVO> getDangerInfo(String banId) throws SQLException {
        return buildingDAO.getDangerInfo(banId);
    }

    @Override
    public List<BanStatisticsVo> getBanByCommunityAndLight() throws SQLException {
        List<BanStatisticsVo> list=buildingDAO.getNumberOfBanByCommunityAndLight();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        int red_total=0;
        int yellow_total=0;
        int green_total=0;
        int all_total=0;
        for (BanStatisticsVo vo:list){
            int greenNum=vo.getGreenNum();
            int yellowNum=vo.getYellowNum();
            int redNum=vo.getRedNum();
            red_total+=redNum;
            yellow_total+=yellowNum;
            green_total+=greenNum;
            int total=greenNum+yellowNum+redNum;
            all_total+=total;
            String redPercent=numberFormat.format(((double) redNum/(double) total)*100);
            String yellowPercent=numberFormat.format(((double)yellowNum/(double)total)*100);
            String greenPercent=numberFormat.format(((double)greenNum/(double)total)*100);
            vo.setRedPercent(redPercent);
            vo.setYellowPercent(yellowPercent);
            vo.setGreenPercent(greenPercent);
            DataVo data = new DataVo();
            List<DataVo> dateList = new ArrayList<DataVo>();
            data.setName("绿类出租屋");
            data.setValue(vo.getGreenNum().toString());
            dateList.add(data);
            data = new DataVo();
            data.setName("黄类出租屋");
            data.setValue(vo.getYellowNum().toString());
            dateList.add(data);
            data = new DataVo();
            data.setName("红类出租屋");
            data.setValue(vo.getRedNum().toString());
            dateList.add(data);
            vo.setDateList(dateList);
        }
        //合计
        BanStatisticsVo newvo=new BanStatisticsVo();
        newvo.setCommunityName("合计");
        newvo.setGreenNum(green_total);
        newvo.setYellowNum(yellow_total);
        newvo.setRedNum(red_total);
        String redPercent=numberFormat.format(((double) red_total/(double) all_total)*100);
        String yellowPercent=numberFormat.format(((double)yellow_total/(double)all_total)*100);
        String greenPercent=numberFormat.format(((double)green_total/(double)all_total)*100);
        newvo.setRedPercent(redPercent);
        newvo.setYellowPercent(yellowPercent);
        newvo.setGreenPercent(greenPercent);
        DataVo data = new DataVo();
        List<DataVo> dateList = new ArrayList<DataVo>();
        data.setName("绿类出租屋");
        data.setValue(newvo.getGreenNum().toString());
        dateList.add(data);
        data = new DataVo();
        data.setName("黄类出租屋");
        data.setValue(newvo.getYellowNum().toString());
        dateList.add(data);
        data = new DataVo();
        data.setName("红类出租屋");
        data.setValue(newvo.getRedNum().toString());
        dateList.add(data);
        newvo.setDateList(dateList);
        list.add(newvo);
        return list;
    }
    
    /**
     * 更新房屋网格员信息
     * <p>Description: TODO</p>
     * @return
     * @throws SQLException
     * <p>Author: 邹乐乐  </p>
     * <p>Date: 2017年11月13日</p>
     */
    @Override
    public void updateBanGridUser() throws SQLException{
        List<GridUserVo> list = buildingDAO.getGridUserVoList();
        Map<String, String> gridUserMap = new HashMap<String, String>();
        if(!AssertUtil.isEmpty(list)&&list.size()>0){
            for (GridUserVo gridUserVo : list) {
                gridUserMap.put(gridUserVo.getDepartmentId(), gridUserVo.getId()+","+gridUserVo.getPersonName());
            }
        }
        
        List<TbYsjdBanPo> banList = buildingDAO.getBanList("");
        for (TbYsjdBanPo banPo : banList) {
            String grid = banPo.getGrid();
            if(!AssertUtil.isEmpty(grid)){
                String userStr = gridUserMap.get(grid);
                if(!AssertUtil.isEmpty(userStr)){
                    String array[] = userStr.split(",");
                    String userId = array[0].trim();
                    String personName = array[1].trim();
                    if(!userId.equals(banPo.getGridOperatorId())){
                        banPo.setGridOperatorName(personName);
                        banPo.setGridOperatorId(userId);
                        updatePO(banPo,false);
                    }
                }else{
                    banPo.setGridOperatorName("");
                    banPo.setGridOperatorId("");
                    updatePO(banPo,false);
                }
            }
        }
    }

    @Override
    public Pager searchReportDanger(Map<String, Object> searchMap, Pager pager) throws SQLException {
        return buildingDAO.searchReportDanger(searchMap,pager);
    }

    @Override
    public List<TbYsjdBanPo> getBanList(String gridId) throws SQLException {
        return buildingDAO.getBanList(gridId);
    }

    @Override
    public List<CommunityVo> getGridByDeptId(String deptId) throws SQLException {
        return buildingDAO.getGridByDeptId(deptId);
    }

    @Override
    public Pager searchTbYsjdBanByGrider(Map<String, Object> searchMap, Pager pager) throws SQLException {
        return buildingDAO.searchTbYsjdBanByGrider(searchMap,pager);
    }


}
