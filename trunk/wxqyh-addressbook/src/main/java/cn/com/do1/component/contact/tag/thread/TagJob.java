package cn.com.do1.component.contact.tag.thread;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.dqdp.core.DqdpAppContext;

public class TagJob implements Job {
	private final static transient Logger logger = LoggerFactory
			.getLogger(TagJob.class);
	private IContactService contactService = DqdpAppContext.getSpringContext()
			.getBean("contactService", IContactService.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			// 获取所有机构
			List<ExtOrgPO> orgList = contactService.getAllOrgByCorp();
			//设置还剩多少需要执行
			TagThread.count = orgList.size();
			for (ExtOrgPO org : orgList) {
				new TagThread(org.getCorpId(), org.getOrganizationId()).run();
			}
		} catch (Exception e) {
			logger.error("同步标签失败：" + e.getMessage(), e);
		} catch (BaseException e) {
			logger.error("同步标签失败：" + e.getMessage(), e);
		}
	}

}
