package cn.com.do1.component.building.building.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.util.BuildingReportUtil;
import cn.com.do1.component.building.building.util.CommonExcelUtil;
import cn.com.do1.component.building.building.vo.ImportErrorBuildingVo;
import cn.com.do1.component.building.building.vo.TbYsjdBanImportVo;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.util.ImportResultUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * Created by apple on 2017/5/8.
 */
public class BuildingImportThread implements Runnable {
    private static final transient Logger logger = LoggerFactory.getLogger(BuildingImportThread.class);
    String[] titleTemplate = {"*社区","*所属网格","*建筑物编码","*楼栋地址","建筑物名称","门牌地址","建筑面积","总套数","楼层数","建筑物类型","使用用途","使用情况","业主","业主手机号","楼栋长","楼栋长手机号","业主固定电话"};
    private File upFile;
    private String upFileFileName;
    private IBuildingService buildingService;
    private String id;
    private String orgId;
    private String userId;
    private String type;
    private List<TbYsjdBanImportVo> list;
    private Map<String, TbQyUserInfoPO> usersMap = new HashMap();
    private Map<String, String> detpMap = new HashMap();
    private static final String IS_USERING = "1";
    private static final String IS_MUST = "1";
    public static final String DATE_TYPE = "4";
    private IContactService contactService;

    public BuildingImportThread(String id, File upFile, String fileName, String type,List<TbYsjdBanImportVo> list ) {
        this.id = id;
        this.upFile = upFile;
        this.upFileFileName = fileName;
        this.type = type;
        this.list = list;
        this.buildingService = (IBuildingService) DqdpAppContext.getSpringContext().getBean("buildingService", IBuildingService.class);
        this.contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    }

    public void run() {
        logger.info("======================:" + this.userId + "导入楼栋信息开始");

        //创建导入结果对象,目的: 在导入线程过程中获取进度
        ResultVO resultvo = new ResultVO();
        ImportResultUtil.putResultObject(id, resultvo);

        //创建导入错误结果对象,目的: 在导入完成后获取导入错误信息
        ImportErrorBuildingVo error = new ImportErrorBuildingVo();
        ImportResultUtil.putErrorObject(id, error);

        int listNum = 1;//行数
        int processNum = 0;//已处理数
        try {
            String[] title = CommonExcelUtil.getExcelTitle(upFile, upFileFileName);
            if(false == judgeTitleIsTrue( title, resultvo)){//判断默认的模板跟导入的模板是否一致
                return;
            }

            if(!list.isEmpty() || list.size() > 0) {
                resultvo.setTotalNum(list.size());
                //错误数据
                List<TbYsjdBanImportVo> errlist = error.getErrorlist();
                if (null == errlist) {
                    errlist = new ArrayList<TbYsjdBanImportVo>();
                    error.setErrorlist(errlist);
                }

                //errorList最终会显示到页面
                List<String> errorlist = resultvo.getErrorlist();
                if (null == errorlist) {
                    errorlist = new ArrayList<String>();
                    resultvo.setErrorlist(errorlist);
                }

                List<TbYsjdBanPo> poList = new ArrayList<TbYsjdBanPo>();

                //检查并构造
                if (BuildingReportUtil.checkLegalPost(contactService, buildingService, list, errlist, errorlist,poList,type,userId)) {
                    //return;
                }

                //保存数据到数据库
                if (!AssertUtil.isEmpty(poList)) {
                    this.buildingService.batchSaveList(poList);
                    resultvo.setProcessNum(poList.size());
                }else {
                    resultvo.setTotalNum(list.size());
                    resultvo.setProcessNum(0);
                }
            }
        } catch (Exception e) {
            List<String> errorlist = new ArrayList<String>();
            errorlist.add("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
            resultvo.setErrorlist(errorlist);
            //错误数据
            List<TbYsjdBanImportVo> errlist = new ArrayList<TbYsjdBanImportVo>();
            for (int i =listNum-1; i < resultvo.getTotalNum(); i++) {
                TbYsjdBanImportVo vo = list.get(i);
                if (i ==listNum-1) {
                    vo.setError("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
                }
                errlist.add(vo);
            }
            error.setErrorlist(errlist);
            logger.info("导入楼栋信息失败", e);
        } catch (BaseException e) {
            List<String> errorlist = new ArrayList<String>();
            errorlist.add("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
            resultvo.setErrorlist(errorlist);
            //错误数据
            List<TbYsjdBanImportVo> errlist=new ArrayList<TbYsjdBanImportVo>();
            for (int i =listNum-1; i < resultvo.getTotalNum(); i++) {
                TbYsjdBanImportVo vo = list.get(i);
                if (i ==listNum-1) {
                    vo.setError("第"+listNum+"行出现错误，导致导入终止，错误提示："+e.getMessage()+"<br/>");
                }
                errlist.add(vo);
            }
            error.setErrorlist(errlist);
            logger.info("导入楼栋信息失败", e);
        } finally {
            resultvo.setFinish(true);
            error.setFinish(true);
            upFile.delete();
            logger.debug("导入楼栋信息完成,processNum:"+resultvo.getProcessNum()+",totalNum:"+resultvo.getTotalNum()+",errorSize:"+(error.getErrorlist()==null?0:error.getErrorlist().size()));
        }
    }
    /**
     * 判断默认的系统模板和excel中的title是否一致
     * @param title excel中的title
     * @param resultvo
     * @return
     * @author liyixin
     * @2016-11-7
     * @version 1.0
     */
    private boolean judgeTitleIsTrue( String[] title, ResultVO resultvo) {
        boolean boo = true;
        if(AssertUtil.isEmpty(title) && titleTemplate.length != title.length){
            resultvo.setTips("你的模板出现了问题，请使用最新的模板导入数据。");
            resultvo.setTotalNum(1);
            resultvo.setProcessNum(0);
            logger.info("你的模板出现了问题，请使用最新的模板导入数据。");
            boo = false;
            return boo;
        }
        for (int i = 0; i < titleTemplate.length; i++) {
            if(!title[i].equals(titleTemplate[i])){
                resultvo.setTips("你的模板出现了问题，请使用最新的模板导入数据。");
                resultvo.setTotalNum(1);
                resultvo.setProcessNum(0);
                logger.info("你的模板出现了问题，请使用最新的模板导入数据。");
                boo = false;
                return boo;
            }
        }
        return boo;
    }
}
