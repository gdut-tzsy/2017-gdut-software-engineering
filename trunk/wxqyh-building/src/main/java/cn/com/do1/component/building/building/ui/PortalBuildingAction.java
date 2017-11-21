package cn.com.do1.component.building.building.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.vo.*;
import cn.com.do1.component.crm.contacts.service.IContactsService;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import cn.com.do1.dqdp.core.ConfigMgr;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.*;

public class PortalBuildingAction extends WxqyhPortalBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(PortalBuildingAction.class);
    /**
     * <p>Field ids: 接收的id集合</p>
     */
    private String[] ids;
    /**
     * <p>Field id: 接收的id</p>
     */
    private String id;

    /**
     * <p>Field tbYsjdBanPo: 楼栋po</p>
     */
    private TbYsjdBanPo tbYsjdBanPo;

    /**
     * <p>Field tbYsjdHousePo: 房屋po</p>
     */
    private TbYsjdHousePo tbYsjdHousePo;

    @Resource
    IContactsService contactsService;

    @Resource
    private IBuildingService buildingService;

    @Resource
    private IContactService contactService;

    public String[] getIds() {
        return this.ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TbYsjdBanPo getTbYsjdBanPo() {
        return this.tbYsjdBanPo;
    }

    public void setTbYsjdBanPo(TbYsjdBanPo tbYsjdBanPo) {
        this.tbYsjdBanPo = tbYsjdBanPo;
    }

    public TbYsjdHousePo getTbYsjdHousePo() {
        return this.tbYsjdHousePo;
    }

    public void setTbYsjdHousePo(TbYsjdHousePo tbYsjdHousePo) {
        this.tbYsjdHousePo = tbYsjdHousePo;
    }


    /**
     * 出租屋安全分类管理主页
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        //根据职位判断显示内容
        List<String> leaderList=new ArrayList<String>();
        leaderList.add("主任");
        leaderList.add("书记");
        if(leaderList.contains(userInfoVO.getPosition())){
            String depIdSys = ConfigMgr.get("building", "depIdSys", "");
            List<CommunityVo> list = buildingService.getCommunity(depIdSys);
            addJsonArray("communityList", list);
        }
        if ("站长".equals(userInfoVO.getPosition())){
            String dept="";
            String[] deptIds=userInfoVO.getDeptIds().split(";");
            if (deptIds.length>1){
                String[] deptNames=userInfoVO.getDepartmentNames().split(";");
                for (int i=0;i<deptIds.length;i++){
                    if (deptNames[i].equals("网格站")){
                        dept=deptIds[i];
                        break;
                    }
                }
            }else if (deptIds.length==1){
                dept=deptIds[0];
            }
            List<CommunityVo> gridList = buildingService.getGridByDeptId(dept);
            addJsonArray("gridList", gridList);
        }
        if("网格员".equals(userInfoVO.getPosition())) {
            String[] deptNames=userInfoVO.getDeptFullNames().split(";");
            String dept="";
            if (deptNames.length>1){
                for (int i=0;i<deptNames.length;i++){
                    if (deptNames[i].contains("网格站")){
                        dept=userInfoVO.getDeptIds().split(";")[i];
                        break;
                    }
                }
            }else {
                dept=userInfoVO.getDeptIds();
            }
            List<TbYsjdBanPo> list=buildingService.getBanList(dept);
            addJsonFormateArray("pageData",list);
        }
    }


    /**
     * <p>Description: 查询社区</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getCommunity() throws Exception, BaseException {
        String depIdSys = ConfigMgr.get("building", "depIdSys", "");
        List<CommunityVo> list = buildingService.getCommunity(depIdSys);
        addJsonArray("communityList", list);
    }

    /**
     * <p>Description: 查询网格</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getGrid() throws Exception, BaseException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String depId = request.getParameter("communityId");
        List<CommunityVo> list = buildingService.getGrid(depId);
        addJsonArray("gridList", list);
    }

    /**
     * <p>Description: 查询楼栋</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getBuilding() throws Exception, BaseException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String gridId = request.getParameter("gridId");
        List<TbYsjdBanPo> list=buildingService.getBanList(gridId);
        addJsonFormateArray("pageData",list);
    }


    /**
     * <p>Description: 查询楼栋具体信息</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxView() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        addJsonObj("position",userInfoVO.getPosition());
        TbYsjdBanPo xxPO = this.buildingService.searchByPk(TbYsjdBanPo.class, id);
        addJsonFormateObj("TbYsjdBanPo", xxPO);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
        List<TbYsjdHousePo> list=buildingService.getHouseList(xxPO.getArchitectureNo());
        addJsonObj("number",list.size());
        List<TbYsjdReportDangerVO> dangerVOList=buildingService.getDangerInfo(xxPO.getId());
        addJsonArray("dangerVOList",dangerVOList);
    }

    /**
     * <p>Description: 查询房屋列表</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxHouseSearch() throws Exception, BaseException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String banNo = request.getParameter("banNo");
        List<TbYsjdHousePo> list=buildingService.getHouseList(banNo);
        addJsonArray("list",list);
    }


    /**
     * <p>Description: 查询房屋信息</p>
     *
     * @throws Exception     Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxViewHouse() throws Exception, BaseException {
        TbYsjdHousePo xxPO = this.buildingService.searchByPk(TbYsjdHousePo.class, id);
        addJsonFormateObj("tbYsjdHousePo", xxPO);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
    }
    
    /**
     * <p>Description: 楼栋统计</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void benStatistics() throws Exception, BaseException {
        List<BanStatisticsVo> voList = this.buildingService.getBanByCommunityAndLight();
        addJsonArray("list",voList);
    }

    /**
     * <p>Description: 更新楼栋亮灯情况</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "操作成功", faileMsg = "操作失败"))
    public void updateBanLight() throws Exception, BaseException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String uplight = request.getParameter("uplight");
        TbYsjdBanPo po=buildingService.searchByPk(TbYsjdBanPo.class,id);
        po.setLight(uplight);
        buildingService.updatePO(po,false);
    }
}
