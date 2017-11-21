package cn.com.do1.component.contact.department.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.component.qwinterface.runtask.IRunTask;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.runtask.util.TaskStatus;
import cn.com.do1.component.runtask.util.TaskType;

/**
 * 更新部门人数任务实现类
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Luo Rilang
 * @2015-9-28
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
@ProcesserUnit(name = TaskType.SynDepartmentTotalTask)
public class SynDepartmentTotalTask implements IRunTask {
	
	@Override
	public TbRunTaskVO runTask(TbRunTaskVO task) throws Exception {
		//执行任务内容：更新部门人数
		boolean flag = SynDepartment.SynDepartmentTotal(task.getOrgId());
		//更新任务对象
		String status = TaskStatus.TASK_FAILURE;
		if(flag){	//部门人员更新成功
			status = TaskStatus.TASK_SUCCES;
		}
		task.setStatus(status);
		return task;
	}

	@Override
	public String getType() {
		return TaskType.SynDepartmentTotalTask;
	}

}
