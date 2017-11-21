package cn.com.do1.component.contact.department.util;

import java.util.List;

import cn.com.do1.component.runtask.util.TaskStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.runtask.runtask.service.IRunTaskService;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.runtask.thread.RunTaskThread;
import cn.com.do1.component.util.ThreadPoolUtils;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 定时执行任务
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Luo Rilang
 * @2015-9-28
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class NotRunTaskJob implements Job {
	private final static transient Logger logger = LoggerFactory.getLogger(NotRunTaskJob.class);
	private IRunTaskService runTaskService = DqdpAppContext.getSpringContext().getBean("runTaskService", IRunTaskService.class);
	//使用线程池处理任务
	private static ThreadPoolUtils tpu = null;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<TbRunTaskVO> taskList;
		try {
			if(tpu==null){
				tpu = new ThreadPoolUtils("尚未处理的任务队列");
			}
			//先获取所有尚未处理的任务
			taskList = runTaskService.getNotRunReport();
			for (TbRunTaskVO task : taskList) {
				try {
					tpu.execute(new RunTaskThread(task));
				} catch (Exception e) {
					logger.error("执行后台任务失败："+e.getMessage(), e);
					//发现运行失败将状态改为待执行
					runTaskService.updateRunTaskStatus(task.getId(), TaskStatus.TASK_INIT, null);
				} catch (BaseException e) {
					logger.error("执行后台任务失败："+e.getMessage(), e);
					//发现运行失败将状态改为待执行
					runTaskService.updateRunTaskStatus(task.getId(), TaskStatus.TASK_INIT, null);
				}
			}
		} catch (Exception e) {
			logger.error("执行后台任务失败："+e.getMessage(), e);
		} catch (BaseException e) {
			logger.error("执行后台任务失败："+e.getMessage(), e);
		}
		
	}

}
