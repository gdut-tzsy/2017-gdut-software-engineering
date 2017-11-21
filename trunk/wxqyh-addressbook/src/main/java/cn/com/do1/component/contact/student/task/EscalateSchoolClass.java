package cn.com.do1.component.contact.student.task;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.managesetting.managesetting.vo.OrgIndustryVersionVO;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 每年7月15号升级学校班级
 * Created by hejinjiao on 2016/12/23.
 */
public class EscalateSchoolClass implements Job {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(EscalateSchoolClass.class);
    /**
     * The constant studentService.
     */
    private static IStudentService studentService = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("********************升级教育版班级部门开始：");
        try {
            if (studentService == null) {
                studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);
            }
            //查询教育版机构
            List<OrgIndustryVersionVO> list = studentService.findEducationOrgs();
            if (list.size() > 0) {
                logger.debug("********************一共有" + list.size() + "个机构需要升级班级部门");
                int nowYear = Integer.parseInt(DateUtil.format(new Date(), "yyyy"));
                for (OrgIndustryVersionVO verVO : list) {
                    logger.debug("********************升级班级部门的机构Id：" + verVO.getOrgId());
                    SchoolClassUtil.updateSchoolClassByOrgId(nowYear, verVO.getSchoolYear(), verVO.getOrgId(), verVO.getSystemName());
                }
            }
        } catch (Exception e) {
            logger.error("升级教育版班级部门错误", e);
        } catch (BaseException e) {
            logger.error("升级教育版班级部门错误", e);
        } finally {
            logger.debug("********************升级教育版班级部门结束");
        }

    }
}
