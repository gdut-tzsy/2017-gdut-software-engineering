package cn.com.do1.component.building.building.util;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.vo.BanStatisticsVo;
import cn.com.do1.component.log.operationlog.service.IOperationlogService;
import cn.com.do1.component.report.report.model.TbReportTaskPO;
import cn.com.do1.component.report.report.vo.ReportTaskVO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.HttpUploadFileUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;


public class StatisticsReportUtil {

    private static final transient Logger logger = LoggerFactory.getLogger(StatisticsReportUtil.class);


    private static IBuildingService buildingService = (IBuildingService) DqdpAppContext.getSpringContext().getBean("buildingService", IBuildingService.class);

    private static IOperationlogService operationlogService = (IOperationlogService) DqdpAppContext.getSpringContext().getBean("operationlogService", IOperationlogService.class);


    public static void export(ReportTaskVO report) {
        logger.info("导出出租屋分类统计开始");
        OutputStream os = null;
        String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        File file = new File(Configuration.REPORT_FILE_TEMP_PATH + fileName + ".xlsx");
        try {
            os = new FileOutputStream(file);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            Workbook writeWB = new SXSSFWorkbook(500);

            Sheet writeSheet = writeWB.createSheet("出租屋分类统计");
            int rowIndex = 0;
            Row writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("社区");
            writeRow.createCell(1).setCellValue("红类出租屋(栋)");
            writeRow.createCell(2).setCellValue("占比");
            writeRow.createCell(3).setCellValue("黄类出租屋(栋)");
            writeRow.createCell(4).setCellValue("占比");
            writeRow.createCell(5).setCellValue("绿类出租屋(栋)");
            writeRow.createCell(6).setCellValue("占比");


            List<BanStatisticsVo> list = buildingService.getBanByCommunityAndLight();
            if (list.size() > 0) {
                for (BanStatisticsVo vo : list) {
                    rowIndex++;
                    writeRow = writeSheet.createRow(rowIndex);
                    writeRow.createCell(0).setCellValue(vo.getCommunityName());
                    writeRow.createCell(1).setCellValue(vo.getRedNum());
                    writeRow.createCell(2).setCellValue(vo.getRedPercent()+"%");
                    writeRow.createCell(3).setCellValue(vo.getYellowNum());
                    writeRow.createCell(4).setCellValue(vo.getYellowPercent()+"%");
                    writeRow.createCell(5).setCellValue(vo.getGreenNum());
                    writeRow.createCell(6).setCellValue(vo.getGreenPercent()+"%");
                }
            }

            writeWB.write(os);
            updateReport(report, file, fileName);
        } catch (Exception e) {
            logger.error("导出出租屋分类统计出错：" + e.getMessage(), e);
            report.setState("-1");
            report.setResultDesc(e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                updateReportTask(report);
                if ((!AssertUtil.isEmpty(file)) && (file.exists()))
                    file.delete();
            } catch (IOException e) {
                logger.error("导出出租屋分类统计,关闭io异常！！", e);
            } catch (SQLException e) {
                logger.error("导出出租屋分类统计：" + e.getMessage(), e);
            }
        }

    }

    public static void updateReportTask(ReportTaskVO report) throws SQLException {
        TbReportTaskPO po = new TbReportTaskPO();
        po.setId(report.getId());
        po.setState(report.getState());
        po.setFilePath(report.getFilePath());
        po.setFileSize(report.getFileSize());
        po.setResultDesc(report.getResultDesc());
        po.setFileType(report.getFileType());
        operationlogService.updatePO(po, false);
    }

    public static void updateReport(ReportTaskVO report, File file, String fileName) {
        String urlString = HttpUploadFileUtil.uploadFile(file, "xlsx", false, report.getOrgId(), "file", fileName);
        if (!AssertUtil.isEmpty(urlString)) {
            report.setFilePath(urlString);
            report.setState("1");
            report.setResultDesc("导出成功");
            report.setFileType("xlsx");
            report.setFileSize(file.length());
        }
    }
}


