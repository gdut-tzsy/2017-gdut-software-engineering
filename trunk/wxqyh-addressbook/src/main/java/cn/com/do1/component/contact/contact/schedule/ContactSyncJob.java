package cn.com.do1.component.contact.contact.schedule;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.util.ContactSyncStatus;
import cn.com.do1.component.contact.contact.thread.ContactSyncThread;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.EmailUtil;
import cn.com.do1.component.util.ThreadPoolUtils;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 通讯录同步任务
 * @author sunqinghai
 * @date 2016 -10-13
 */
public class ContactSyncJob implements Job {
	/**
	 * The constant logger.
	 */
	private static final transient Logger logger = LoggerFactory.getLogger(ContactSyncJob.class);
	/**
	 * The constant contactService.
	 */
	private static IContactService contactService = null;
	/**
	 * 使用线程池处理任务
	 */
	private static ThreadPoolUtils tpu = null;
	/**
	 * 是否第一次启动
	 */
	private static boolean isFirst = true;

	@Override
	public void execute(JobExecutionContext arg) throws JobExecutionException {
		try {
			if(tpu == null){
				tpu = new ThreadPoolUtils(10, 10, 1000, 2000, "通讯录变更同步队列");
			}
			if(contactService == null){
				contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
			}
			synchronized (ContactSyncStatus.getRunCorpIds()){
				//先获取所有待处理的任务
				List<String> list = contactService.getContactSyncCorpIdList((isFirst ? ContactSyncStatus.STATUS_ING : ContactSyncStatus.STATUS_WAIT));
				if (list.size() > 2000) {
					StringBuilder sb = new StringBuilder();
					String split = ",";
					for (String s : ContactSyncStatus.getRunCorpIds()) {
						sb.append(s).append(split);
					}
					EmailUtil.sendWarnEmail("微信通讯录变更上报待处理超过2000条", "请重启定时任务：" + Configuration.LOCAL_HOST_ADDRESS + "|" + DqdpAppContext.getAppRootPath() + "corpIdList" + sb.toString());
				}
				for (String corpId : list) {
					try {
						if (!ContactSyncStatus.add(corpId)) {
							continue;
						}
						tpu.execute(new ContactSyncThread(corpId));
					} catch (Exception e) {
						logger.error("ContactSyncJob 执行任务失败", e);
					} catch (BaseException e) {
						logger.error("ContactSyncJob 执行任务失败", e);
						//发现运行失败将状态改为待执行
					}
				}
				if(isFirst){
					isFirst = false;
				}
			}
		} catch (Exception e) {
			logger.error("ContactSyncJob 执行任务失败", e);
			ExceptionCenter.addException(e, "ContactSyncJob 执行任务失败", String.valueOf(isFirst));
		}
	}
}
