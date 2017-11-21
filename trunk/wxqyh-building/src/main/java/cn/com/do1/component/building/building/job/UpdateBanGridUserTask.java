package cn.com.do1.component.building.building.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 
 * <p>
 * ClassName: 个人主页定时任务
 * </p>
 * <p>
 * Description: 定时发布等调用的类
 * </p>
 * <p>
 * Author: cuijianpeng
 * </p>
 * <p>
 * Date: 2016年12月5日
 * </p>
 */
public class UpdateBanGridUserTask implements Job {
    private static final transient Logger logger = LoggerFactory.getLogger(UpdateBanGridUserTask.class);
    private IBuildingService buildingService = (IBuildingService) DqdpAppContext.getSpringContext().getBean(
            "buildingService", IBuildingService.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            logger.info("##楼栋网格员信息定时任务开始");

            buildingService.updateBanGridUser();

            logger.info("##楼栋网格员信息定时任务结束");
        } catch (Exception e) {
            logger.error("楼栋网格员信息定时任务失败", e);
        }
    }
}
