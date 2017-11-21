package cn.com.do1.component.contact.tag.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.qwinterface.runtask.IRunTask;
import cn.com.do1.component.runtask.runtask.model.TbRunTaskPO;
import cn.com.do1.component.runtask.runtask.service.IRunTaskService;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.runtask.util.TaskStatus;
import cn.com.do1.component.runtask.util.TaskType;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.WeixinMsgUtil;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by sunqinghai on 17/3/2.
 *
 * @author sunqinghai
 * @date 2017 -3-2
 */
@ProcesserUnit(name = TaskType.TAG_SYNC_TASK)
public class TagSyncTask implements IRunTask {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(TagSyncTask.class);
    /**
     * The constant runTaskService.
     */
    private static IRunTaskService runTaskService = DqdpAppContext.getSpringContext().getBean("runTaskService", IRunTaskService.class);


    @Override
    public TbRunTaskVO runTask(TbRunTaskVO task) throws Exception {
        //如果改corpId正在同步中，直接将同步状态改为初始状态，下次再同步，以防两个任务同时运行导致通讯录出现重复问题
        synchronized (TagSyncUtil.getRunCorpIds()){
            if(!TagSyncUtil.add(task.getCorpId())){
                logger.debug("TagSyncTask 同步标签推迟，corpId：" + task.getCorpId() + ",org_id:" + task.getOrgId() + ",userId" +task.getCreatePerson());
                task.setStatus(TaskStatus.TASK_INIT);
                return task;
            }
        }
        try {
            TagSyncUtil.syncAllTag(task.getCreatePerson());

            WeixinMsgUtil.sendTxtMsgToManager(task.getCreatePerson(), task.getOrgId(), task.getCorpId(), ErrorCodeDesc.TAG_SYNC_SUCCEED.getDesc(), WxAgentUtil.getAddressBookCode());
            return null;
        } catch (BaseException e) {
            logger.error("TagSyncTask runTask error corpId：" + task.getCorpId());
            ExceptionCenter.addException(e, "TagSyncTask runTask error ", task.toString());
            task.setStatus(TaskStatus.TASK_FAILURE);
            task.setResultDesc("同步出现异常：" + e.getMessage());
            WeixinMsgUtil.sendTxtMsgToManager(task.getCreatePerson(), task.getOrgId(), task.getCorpId(), ErrorCodeDesc.TAG_SYNC_ERROR.getDesc(), WxAgentUtil.getAddressBookCode());
            return task;
        } catch (Exception e) {
            logger.error("TagSyncTask runTask error corpId：" + task.getCorpId());
            ExceptionCenter.addException(e, "TagSyncTask runTask error ", task.toString());
            task.setStatus(TaskStatus.TASK_FAILURE);
            task.setResultDesc("同步出现异常：" + e.getMessage());
            WeixinMsgUtil.sendTxtMsgToManager(task.getCreatePerson(), task.getOrgId(), task.getCorpId(), ErrorCodeDesc.TAG_SYNC_ERROR.getDesc(), WxAgentUtil.getAddressBookCode());
            return task;
        } finally {
            TagSyncUtil.remove(task.getCorpId());
            CacheWxqyhObject.set("synctask", task.getOrgId(), task.getId(), true, 600);
        }
    }

    /**
     * 新增同步任务
     *
     * @param corpId
     * @param orgId
     * @param optUser
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static String insertUserTask(String corpId, String orgId, String optUser) throws Exception, BaseException {
        TbRunTaskPO task = new TbRunTaskPO();
        task.setId(UUID32.getID());
        task.setTaskType(TaskType.TAG_SYNC_TASK);
        task.setOrgId(orgId);
        task.setStatus(TaskType.TASK_STATUS_WAIT);
        task.setCreateTime(new Date());
        task.setCorpId(corpId);
        //task.setOnlyId(corpId);
        task.setCreatePerson(optUser);
        runTaskService.insertRunTask(task);
        CacheWxqyhObject.set("synctask", orgId, task.getId(), false, 600);
        return task.getId();
    }
    /**
     * 判断是否有正在等待执行的任务
     * @param orgId
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static boolean existUserTask(String orgId, String taskId) throws Exception, BaseException {
        //判断没有在等待执行的数据
        if (!StringUtil.isNullEmpty(taskId)) {
            Object object = CacheWxqyhObject.get("synctask", orgId, taskId);
            if (object != null) {
                if ((Boolean) object) {
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        boolean isWait = runTaskService.getIsExist(orgId, TaskType.TASK_STATUS_WAIT, TaskType.TAG_SYNC_TASK);
        if (!StringUtil.isNullEmpty(taskId)) {
            CacheWxqyhObject.set("synctask", orgId, taskId, !isWait);
        }
        return isWait;
    }

    /**
     * 获取正在执行的
     * @param orgId
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-31
     */
    public static List<TbRunTaskVO> getUserTask(String orgId) throws Exception, BaseException {
        //判断没有在等待执行的数据
        return runTaskService.getRunTaskListByOrgId(orgId, TaskType.TASK_STATUS_WAIT, TaskType.TAG_SYNC_TASK);
    }

    /**
     * 任务类型
     *
     * @return
     * @author Luo Rilang
     * @2015-9-7
     * @version 1.0
     */
    @Override
    public String getType() {
        return null;
    }
}
