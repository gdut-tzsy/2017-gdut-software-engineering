package cn.com.do1.component.building.building.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.component.report.report.vo.ReportTaskVO;
import cn.com.do1.component.wxqyh.task.IReportExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ProcesserUnit(name = "banStatistics")
public class BanStatisticsReport implements IReportExt {
    private static final transient Logger logger = LoggerFactory.getLogger(BanStatisticsReport.class);

    @Override
    public void doSome(ReportTaskVO reportTaskVO) {
        //如下内容是直接拷贝新闻公告的导出，自己的导出，在这里实现。（实现可以参考企微）
        String type = reportTaskVO.getReportType();

        logger.info("======================================="+type);

        //出租屋分类统计导出
        if("ban_statistics".equals(type)){//这个必须要判断，因为type不一定是你自己的，所以要判读是你自己的才处理
            StatisticsReportUtil.export(reportTaskVO);
        }
    }
}
