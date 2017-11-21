package cn.com.do1.component.building.building.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.do1.component.building.building.vo.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.FileUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.thread.BuildingImportThread;
import cn.com.do1.component.building.building.thread.HouseImportThread;
import cn.com.do1.component.building.building.util.BuildingReportUtil;
import cn.com.do1.component.building.building.util.CommonExcelUtil;
import cn.com.do1.component.building.building.util.HouseReportUtil;
import cn.com.do1.component.common.util.ExcelUtil;
import cn.com.do1.component.crm.contacts.service.IContactsService;
import cn.com.do1.component.util.ImportResultUtil;
import cn.com.do1.dqdp.core.ConfigMgr;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

import java.net.URLEncoder;

public class BuildingManagerAction extends BaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(BuildingManagerAction.class);
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
    
    private String upFileFileName;
    private File upFile;
    
    @Resource
    IContactsService contactsService;
    
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

    public String getUpFileFileName() {
        return upFileFileName;
    }
    public void setUpFileFileName(String upFileFileName) {
        this.upFileFileName = upFileFileName;
    }
    public File getUpFile() {
        return upFile;
    }
    public void setUpFile(File upFile) {
        this.upFile = upFile;
    }

    /**
     * <p>Field buildingService: buildingService接口</p>
     */
    @Resource
    private IBuildingService buildingService;
    
    /**
     * <p>Field contactService: contactService接口</p>
     */
    @Resource
    private IContactService contactService;
    
    /**
     * <p>Description: 分页查询楼栋信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @SearchValueTypes(nameFormat = "false", value = {
            @SearchValueType(name = "communityName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "architectureNo", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "owner", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "gridOperatorName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "banAddress", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "gridName", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxBuildingSearch() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userId = user.getUsername();
        UserOrgVO userInfoVo = this.contactService.getOrgByUserId(userId);
        if (userInfoVo == null) {
            throw new BaseException("登录人的信息为空");
        }
        Map<String, Object> searchMap = getSearchValue();
        pager = this.buildingService.searchTbYsjdBan(searchMap, pager);
        addJsonPager("pageData", pager);
    }
    
    /**
     * <p>Description: 批量删除楼栋</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "删除成功", faileMsg = "删除失败"))
    public void batchDeleteBuilding() throws Exception, BaseException {
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userName = user.getUsername();
        UserOrgVO userOrgVO = this.contactService.getOrgByUserId(userName);
        if(userOrgVO == null){
            setActionResult("1001","登录人信息为空，请重新登录！");
            return ;
        }
        String msg= this.buildingService.batchDeleteBuilding(ids);
        setActionResult("0",msg);
    }
    
    /**
     * <p>Description: 添加修改楼栋</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "保存成功", faileMsg = "保存失败"))
    public void addUpdateBan() throws Exception, BaseException {
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userId = user.getUsername();
        UserOrgVO userInfoVo = this.contactService.getOrgByUserId(userId);
        if (userInfoVo == null) {
            throw new BaseException("登录人的信息为空");
        }
        String grid = tbYsjdBanPo.getGrid();
        List<GridOperatorVo> GridOperatorList = this.buildingService.getGridOperator(grid);
        if (!AssertUtil.isEmpty(GridOperatorList)) {
            if (GridOperatorList.size() > 1) {
                String personNames = "";
                String personIds = "";
                for (int i = 0; i < GridOperatorList.size(); i++) {
                    if (i==0) {
                        personNames = GridOperatorList.get(i).getPersonName();
                        personIds = GridOperatorList.get(i).getUserId();
                    } else {
                        personNames = personNames + "," + GridOperatorList.get(i).getPersonName();
                        personIds = personIds + "," + GridOperatorList.get(i).getUserId();
                    }
                }
                tbYsjdBanPo.setGridOperatorName(personNames);
                tbYsjdBanPo.setGridOperatorId(personIds);
            } else {
                tbYsjdBanPo.setGridOperatorName(GridOperatorList.get(0).getPersonName());
                tbYsjdBanPo.setGridOperatorId(GridOperatorList.get(0).getUserId());
            }
        } else {
            if (AssertUtil.isEmpty(GridOperatorList)) {
                throw new BaseException("找不到" + tbYsjdBanPo.getGridName() + "的网格员，请在通讯录录入网格员" );
            }
        }
        if (!AssertUtil.isEmpty(tbYsjdBanPo.getId())) {
            this.buildingService.updatePO(tbYsjdBanPo, false);
        } else {
            TbYsjdBanPo xxPo = this.buildingService.getbanByarchitectureNo(tbYsjdBanPo.getArchitectureNo());
            if (!AssertUtil.isEmpty(xxPo)) {
                throw new BaseException("建筑编码已存在");
            }
            tbYsjdBanPo.setLight("0");
            tbYsjdBanPo.setId(UUID.randomUUID().toString());
            tbYsjdBanPo.setCreatorTime(new Date());
            this.buildingService.insertPO(this.tbYsjdBanPo,false);
        }
    }
    
    /**
     * <p>Description: 查询楼栋信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */ 
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxView() throws Exception, BaseException {
        TbYsjdBanPo xxPO = this.buildingService.searchByPk(TbYsjdBanPo.class, id);
        addJsonFormateObj("TbYsjdBanPo", xxPO);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
    }
    
    
    /**
     * <p>Description: 查询社区</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getCommunity() throws Exception, BaseException {
        String depIdSys = ConfigMgr.get("building", "depIdSys", "");
        List<CommunityVo> list = buildingService.getCommunity(depIdSys);
        addJsonArray("communityList", list);
    }
    
    /**
     * <p>Description: 查询社区</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getGrid() throws Exception, BaseException {
        HttpServletRequest request=ServletActionContext.getRequest();
        String depId = request.getParameter("gridId");
        List<CommunityVo> list = buildingService.getGrid(depId);
        addJsonArray("gridList", list);
    }
    
    /**
     * <p>Description: 分页查询房屋信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @SearchValueTypes(nameFormat = "false", value = {
            @SearchValueType(name = "houseNo", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "houseAddress", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "architectureNo", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "propertyOwner", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxHouseSearch() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userId = user.getUsername();
        UserOrgVO userInfoVo = this.contactService.getOrgByUserId(userId);
        if (userInfoVo == null) {
            throw new BaseException("登录人的信息为空");
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        String banNoId = request.getParameter("banNoId");
        Map<String, Object> searchMap = getSearchValue();
        if (!AssertUtil.isEmpty(banNoId)) {
            TbYsjdBanVo vo = this.buildingService.getBanVoBybanNoId(banNoId);
            searchMap.put("architectureNo", "%"+vo.getArchitectureNo()+"%");
        }
        pager = this.buildingService.searchTbYsjdHouse(searchMap, pager);
        addJsonPager("pageData", pager);
    }
    
    /**
     * <p>Description: 添加修改房屋</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "保存成功", faileMsg = "保存失败"))
    public void addUpdateHouse() throws Exception, BaseException {
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userId = user.getUsername();
        UserOrgVO userInfoVo = this.contactService.getOrgByUserId(userId);
        if (userInfoVo == null) {
            throw new BaseException("登录人的信息为空");
        }
        TbYsjdBanPo po = this.buildingService.isHavebanNo(tbYsjdHousePo.getBanNo());
        if (AssertUtil.isEmpty(po)) {
            throw new BaseException("楼栋编码错误或没有该楼栋");
        }
        if (!AssertUtil.isEmpty(tbYsjdHousePo.getId())) {
            this.buildingService.updatePO(tbYsjdHousePo, false);
        } else {
            tbYsjdHousePo.setId(UUID.randomUUID().toString());
            tbYsjdHousePo.setCreatorTime(new Date());
            this.buildingService.insertPO(this.tbYsjdHousePo,false);
        }
    }
    
    /**
     * <p>Description: 批量删除房屋</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "删除成功", faileMsg = "删除失败"))
    public void batchDeleteHouse() throws Exception, BaseException {
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userName = user.getUsername();
        UserOrgVO userOrgVO = this.contactService.getOrgByUserId(userName);
        if(userOrgVO == null){
            setActionResult("1001","登录人信息为空，请重新登录！");
            return ;
        }
        this.buildingService.batchDel(TbYsjdHousePo.class,ids);
    }
    
    /**
     * <p>Description: 查询楼栋信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */ 
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxViewHouse() throws Exception, BaseException {
        TbYsjdHousePo xxPO = this.buildingService.searchByPk(TbYsjdHousePo.class, id);
        addJsonFormateObj("tbYsjdHousePo", xxPO);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
    }
    
    /**
     * <p>Description: 导出楼栋信息导入模板</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    public void exportTemplateBan() throws Exception, BaseException {
        logger.info("导出楼栋信息导入模板开始");
        Date date = new Date();
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userName = user.getUsername();
        UserOrgVO userOrgVO = this.contactService.getOrgByUserId(userName);
        if(userOrgVO == null){
            setActionResult("1001","登录人信息为空，请重新登录！");
            return ;
        }
        ServletOutputStream os = null;
        Object optionVOs = new ArrayList();
        try {
            HttpServletResponse e = ServletActionContext.getResponse();
            os = e.getOutputStream();
            HttpServletRequest e1 = ServletActionContext.getRequest();
            String fileName = "楼栋信息导入模板.xls";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            e.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            e.setContentType("application/msexcel");
            HSSFWorkbook writeWB = new HSSFWorkbook();
            HashMap titleMap = new HashMap();
            HSSFSheet writeSheet = writeWB.createSheet("楼栋信息导入");
            String[] architectureTypeList = { "楼房","特殊类建筑","平房","别墅 " };
            String[] purposeList = { "住宅 ","厂房","商业","商住","办公","公共设施","仓库 ","综合","其他" };
            String[] situationList = { "待租","空置","出租","部分出租","自用","其他" };
            writeSheet = BuildingReportUtil.setHSSFValidation(writeSheet, architectureTypeList, 1, 1000, 9, 9);
            writeSheet = BuildingReportUtil.setHSSFValidation(writeSheet, purposeList, 1, 1000, 10, 10);
            writeSheet = BuildingReportUtil.setHSSFValidation(writeSheet, situationList, 1, 1000, 11, 11);
            Row writeRow = writeSheet.createRow(0);
            //创建第一页的行表头
            BuildingReportUtil.setFirstHead(titleMap, writeRow, false, (List) optionVOs, false);
            //设置第二页模板信息
            BuildingReportUtil.seCondUtil(writeWB,writeSheet, writeRow, optionVOs, userOrgVO);
            writeWB.write(os);
        } catch (Exception var22) {
            logger.error("导出楼栋信息导入模板出现异常！" + var22.getMessage(), var22);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                logger.error("导出楼栋信息导入模板完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
            } catch (IOException var21) {
                logger.error("楼栋信息导入模板,关闭io异常！！", var21);
            }

        }
    }
    
    /**
     * <p>Description: 导入楼栋信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException( errCode = "1002", successMsg = "导入成功", faileMsg = "导入失败" ))
    public void batchImportBan() throws Exception, BaseException {
        logger.info("导入楼栋信息开始");
        Date date = new Date();
        String filePath = DqdpAppContext.getAppRootPath() + "/uploadFiles";
        (new File(filePath)).mkdirs();
        File newFile = new File(filePath + "/" + this.upFileFileName);
        FileUtil.copy(this.upFile, newFile, true);
        String id = UUID.randomUUID().toString();
        HttpServletRequest request = ServletActionContext.getRequest();
        String type = request.getParameter("type");
        
        List list = CommonExcelUtil.importForExcel(this.upFile, this.upFileFileName, TbYsjdBanImportVo.class, this.contactsService,id);

        BuildingImportThread thread1 = new BuildingImportThread(id, newFile, this.upFileFileName, type,  list);
        Thread t1 = new Thread(thread1);
        t1.start();
        this.addJsonObj("start", "0");
        this.addJsonObj("id", id);
        logger.error("导入楼栋信息完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
    }

    /**
     * <p>Description: 导出楼栋错误信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    public void exportBanError() throws Exception, BaseException {
        ImportErrorBuildingVo error=(ImportErrorBuildingVo) ImportResultUtil.getErrorObject(id);
        List<TbYsjdBanImportVo> list=new ArrayList<TbYsjdBanImportVo>();
        if(!AssertUtil.isEmpty(error)){
            list = error.getErrorlist();
        }
        String[] header = {"*社区","*所属网格","*建筑物编码","*楼栋地址","建筑物名称","门牌地址","建筑面积","总套数","楼层数","建筑物类型","使用用途","使用情况","业主","业主手机号","楼栋长","楼栋长手机号","业主固定电话","错误提示"};
        File file = ExcelUtil.exportForExcel(header, list,TbYsjdBanImportVo.class, "导出错误数据.xls");
        HttpServletResponse response = ServletActionContext.getResponse();
        fileOut(file, response);

    }
    
    
    
    
    
    
    
    //TODO
    public void exportTemplateHouse() throws BaseException, Exception {
        logger.info("导出房屋信息导入模板开始");
        Date date = new Date();
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userName = user.getUsername();
        UserOrgVO userOrgVO = this.contactService.getOrgByUserId(userName);
        if(userOrgVO == null){
            setActionResult("1001","登录人信息为空，请重新登录！");
            return ;
        }
        ServletOutputStream os = null;
        Object optionVOs = new ArrayList();
        try {
            HttpServletResponse e = ServletActionContext.getResponse();
            os = e.getOutputStream();
            HttpServletRequest e1 = ServletActionContext.getRequest();
            String fileName = "房屋信息导入模板.xls";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            e.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            e.setContentType("application/msexcel");
            HSSFWorkbook writeWB = new HSSFWorkbook();
            HashMap titleMap = new HashMap();
            HSSFSheet writeSheet = writeWB.createSheet("房屋信息导入");
            String[] structureList = { "单房","一房一厅","两房一厅","两房两厅","三房以上","其他 " };
            String[] purposeList = { "住宅 ","厂房","商业","商住","办公","公共设施","仓库 ","综合","其他" };
            String[] situationList = { "待租","空置","出租","部分出租","自用","其他" };
            writeSheet = HouseReportUtil.setHSSFValidation(writeSheet, structureList, 1, 1000, 9, 9);
            writeSheet = HouseReportUtil.setHSSFValidation(writeSheet, purposeList, 1, 1000, 7, 7);
            writeSheet = HouseReportUtil.setHSSFValidation(writeSheet, situationList, 1, 1000, 8, 8);
            Row writeRow = writeSheet.createRow(0);
            //创建第一页的行表头
            HouseReportUtil.setFirstHead(titleMap, writeRow, false, (List) optionVOs, false);
            //设置第二页模板信息
            HouseReportUtil.seCondUtil(writeWB,writeSheet, writeRow, optionVOs, userOrgVO);
            writeWB.write(os);
        } catch (Exception var22) {
            logger.error("导出房屋信息导入模板出现异常！" + var22.getMessage(), var22);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                
                logger.error("导出房屋信息导入模板完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
            } catch (IOException var21) {
                logger.error("房屋信息导入模板,关闭io异常！！", var21);
            }
            
        }
    }
    
    @JSONOut(
            catchException = @CatchException(
                    errCode = "1002",
                    successMsg = "导入成功",
                    faileMsg = "导入失败"
                    )
            )
    
    public void batchImportHouse() throws Exception, BaseException {
        logger.info("导入房屋信息开始");
        Date date = new Date();
        String filePath = DqdpAppContext.getAppRootPath() + "/uploadFiles";
        (new File(filePath)).mkdirs();
        File newFile = new File(filePath + "/" + this.upFileFileName);
        FileUtil.copy(this.upFile, newFile, true);
        String id = UUID.randomUUID().toString();
        HttpServletRequest request = ServletActionContext.getRequest();
        String type = request.getParameter("type");
        
        List list = CommonExcelUtil.importForExcel(this.upFile, this.upFileFileName, TbYsjdHouseImportVo.class, this.contactsService,id);
        
        HouseImportThread thread1 = new HouseImportThread(id, newFile, this.upFileFileName, type,  list);
        Thread t1 = new Thread(thread1);
        t1.start();
        this.addJsonObj("start", "0");
        this.addJsonObj("id", id);
        logger.error("导入房屋信息完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
    }
    
    public void exportHouseError() throws BaseException, Exception {
        ImportErrorHouseVo error=(ImportErrorHouseVo) ImportResultUtil.getErrorObject(id);
        List<TbYsjdHouseImportVo> list=new ArrayList<TbYsjdHouseImportVo>();
        if(!AssertUtil.isEmpty(error)){
            list = error.getErrorlist();
        }
        String[] header = {"*房屋编号","*房屋地址","*楼栋编号","楼栋名称","房号","产权人","房屋面积","使用用途","使用情况","房屋结构","所属楼栋","错误提示"};
        File file = ExcelUtil.exportForExcel(header, list,TbYsjdHouseImportVo.class, "导出错误数据.xls");
        HttpServletResponse response = ServletActionContext.getResponse();
        fileOut(file, response);
        
    }

    /**
     * 导出错误信息
     * @param file
     * @param response
     */
    private void fileOut(File file, HttpServletResponse response) throws BaseException {
        if (file.exists()) {
            String fileName = file.getName();
            InputStream is = null;
            OutputStream os = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                is=new FileInputStream(file);

                os=response.getOutputStream();
                bis=new BufferedInputStream(is);
                bos=new BufferedOutputStream(os);
                HttpServletRequest request = ServletActionContext.getRequest();
                String agent = request .getHeader("User-Agent");
                boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
                if (isMSIE) {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/ vnd.ms-excel");// 不同类型的文件对应不同的MIME类型

                response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
                }
            } catch (Exception e) {
                throw new BaseException("文件流出错！");
            } finally {
                try {
                    if(bos!=null){
                        bos.flush();
                    }
                } catch (Exception e2) {
                    logger.error("exportErrorAuditor关闭流失败",e2);
                }
                try {
                    if(bis!=null){
                        bis.close();
                    }
                } catch (Exception e2) {
                    logger.error("exportErrorAuditor关闭流失败",e2);
                }
                try {
                    if(bos!=null){
                        bos.close();
                    }
                } catch (Exception e2) {
                    logger.error("exportErrorAuditor关闭流失败",e2);
                }
                try {
                    if(is!=null){
                        is.close();
                    }
                } catch (Exception e2) {
                    logger.error("exportErrorAuditor关闭流失败",e2);
                }
            }
        }
    }

    /**
     * 出租屋分类统计
     * @throws Exception
     * @throws BaseException
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getStatisticsList() throws Exception, BaseException {
        List<BanStatisticsVo> voList=buildingService.getBanByCommunityAndLight();
        addJsonArray("list",voList);
    }

    /**
     * 相关隐患
     * @throws Exception
     * @throws BaseException
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getReportDanger() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = buildingService.searchReportDanger(getSearchValue(), pager);
        addJsonPager("pageData", pager);
    }
    
    /**
     * <p>Description: 批量更新楼栋网格员信息</p>
     * @throws Exception Exception异常
     * @throws BaseException BaseException异常
     */
    @JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "更新网格员信息成功", faileMsg = "更新网格员信息失败"))
    public void updateBanGridUser() throws Exception, BaseException {
        IUser user = (IUser) DqdpAppContext.getCurrentUser();
        String userName = user.getUsername();
        UserOrgVO userOrgVO = this.contactService.getOrgByUserId(userName);
        if(userOrgVO == null){
            setActionResult("1001","登录人信息为空，请重新登录！");
            return ;
        }
        this.buildingService.updateBanGridUser();
    }
}
