package cn.com.do1.component.contact.department.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.contact.contact.util.ChangeAgentUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoExtVO;
import cn.com.do1.component.addressbook.department.vo.DepTotalUserVO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.qwinterface.addressbook.IUserInfoChangeInform;
import cn.com.do1.component.runtask.runtask.model.TbRunTaskPO;
import cn.com.do1.component.runtask.runtask.service.IRunTaskService;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.runtask.util.TaskType;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 实现用户部门变动操作接口
 * <p>
 * Title: 功能/模块
 * </p>
 * <p>
 * Description: 类的描述
 * </p>
 * 
 * @author Luo Rilang
 * @2015-9-29
 * @version 1.0 修订历史： 日期 作者 参考 描述
 */
@ProcesserUnit(name = "synDepartmentImpl")
public class SynDepartmentImpl implements IUserInfoChangeInform {
	private final static transient Logger logger = LoggerFactory
			.getLogger(SynDepartmentImpl.class);
	private static IRunTaskService runTaskService = DqdpAppContext
			.getSpringContext()
			.getBean("runTaskService", IRunTaskService.class);
	private static IDepartmentMgrService departmentService = DqdpAppContext
			.getSpringContext().getBean("departmentService",
					IDepartmentMgrService.class);

	@Override
	public void addUser(UserOrgVO user,TbQyUserInfoPO userInfoPO, List<String> deptId,
						UserInfoExtVO userInfoExtVO, int type) {
		insertRunTask(userInfoPO.getOrgId());
	}

	@Override
	public void updateUser(UserOrgVO user,TbQyUserInfoPO oldUser, List<String> oldDeptId,
			TbQyUserInfoPO userInfoPO, List<String> deptId,
			UserInfoExtVO userInfoExtVO, int type) {
		if (!ListUtil.strCsStr(oldDeptId, deptId)) {
			insertRunTask(userInfoPO.getOrgId());
		}
	}

	@Override
	public void delUser(UserOrgVO user,TbQyUserInfoPO userInfoPO, List<String> deptId,
			int type) {
		insertRunTask(userInfoPO.getOrgId());
	}

	@Override
	public void leaveUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap) {
		insertRunTask(userInfoPo.getOrgId());
	}

	@Override
	public void batchLeaveUser(UserOrgVO user, List<TbQyUserInfoPO> users, int type){
		insertRunTask(user.getOrgId());
	}

	@Override
	public void batchDelEnd(UserOrgVO user, String[] userIds, int type) {
		insertRunTask(user.getOrgId());
	}

	@Override
	public void batchMoveEnd(UserOrgVO user, List<String> userIds, List<String> deptId) {
		insertRunTask(user.getOrgId());
	}

	/**
	 * 批量从特定部门移出用户
	 *
	 * @param user
	 * @param userIds
	 * @param deptId
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-13
	 */
	@Override
	public void batchShiftOutUser(UserOrgVO user, List<String> userIds, List<String> deptId) {
		insertRunTask(user.getOrgId());
	}

	/**
	 * 批量移入到特定部门用户
	 *
	 * @param user
	 * @param userIds
	 * @param deptId
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-13
	 */
	@Override
	public void batchShiftIntUser(UserOrgVO user, List<String> userIds, List<String> deptId) {
		insertRunTask(user.getOrgId());
	}

	@Override
	public void updateUserStatusAndHeadPic(TbQyUserInfoPO userPO, int type) {

	}

	/**
	 * 批量操作部门信息
	 *
	 * @param user       操作人
	 * @param addList    新增部门list
	 * @param updateList 更新的部门list，当更新了部门名称，父部门，微信部门id，微信父部门id时才会加入到更新部门list中
	 * @param delList    删除的部门list
	 * @param type
	 * @author sunqinghai
	 * @date 2016 -7-5
	 */
	@Override
	public void batchChangeDept(UserOrgVO user, List<DeptSyncInfoVO> addList, List<DeptSyncInfoVO> updateList, List<DeptSyncInfoVO> delList, int type) {

	}

	@Override
	public void recoverUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type) {
		insertRunTask(user.getOrgId());
	}
	/**
	 * 同步结束提醒
	 * @param user
	 * @param addList
	 * @param updateList
	 * @param delList        离职用户列表
	 * @param userRefDeptMap 用户部门管理数据，离职用户的关联数据不在此列
	 * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param type           操作来源类型  @author Sun Qinghai
	 * @2015-9-24
	 * @version 1.0
	 */
	@Override
	public void batchChanged(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type) {
		insertRunTask(user.getOrgId());

		//更新可见范围数据，同步完成后需要更新应用的可见范围人员信息
		try {
			if(!Configuration.IS_QIWEIYUN || Configuration.IS_OLD_TRUST){
				ChangeAgentUserUtil.changeAllAgentUser(user.getCorpId());
			}
		} catch (Exception e) {
			logger.error("synAgentVisibleRange 更新应用可见范围失败：", e);
		}
	}

	/**
	 * 生成计算部门人数任务
	 * 
	 * @param orgId
	 * @author Luo Rilang
	 * @2015-9-29
	 * @version 1.0
	 */
	private void insertRunTask(String orgId) {
		try {
			TbRunTaskVO runTask = runTaskService.getRunTaskByOrgId(orgId, "0", TaskType.SynDepartmentTotalTask);//获取该orgId下未运行的任务
			if(null != runTask){	//如果已经存在未执行的，则跳过，不保存任务
				return;
			}
			TbRunTaskPO task = new TbRunTaskPO();
			task.setId(UUID.randomUUID().toString());
			task.setTaskType(TaskType.SynDepartmentTotalTask);
			task.setOrgId(orgId);
			task.setStatus("0");
			task.setCreateTime(new Date());
			// 验证是否需要执行
			try {
				List<DepTotalUserVO> list = departmentService
						.getDepTotalUserByOrgId(orgId);
				if (null != list && list.size() > Configuration.MAX_DEPT_SYN_SIZE) {
					TbRunTaskVO noRunTask = runTaskService.getRunTaskByOrgId(orgId, "-2", TaskType.SynDepartmentTotalTask);// 获取该orgId下暂不执行的任务
					if(null != noRunTask){
						return;
					}
					task.setStatus("-2"); // -2暂不执行
					task.setResultDesc("部门人数超过"+Configuration.MAX_DEPT_SYN_SIZE+"个，暂不执行。请手动执行。");
//					try {
//						EmailUtil.SendEmail(WxHttpUtil.fromUser,
//								Configuration.ERROR_WARN_EMAIL, "", "",
//								"统计部门人数超额提醒 orgId=" + orgId,
//								"统计部门人数时，企业部门数超过100个，暂不统计，请手工调用定时任务统计。orgId="
//										+ orgId);
//					} catch (Exception e) {
//						logger.error("发送统计部门人数超额提醒邮件失败：" + e.getMessage(), e);
//					}
				}
			} catch (BaseException e) {
				logger.error("验证部门人数出错" + e.getMessage(), e);
			}
			runTaskService.insertRunTask(task);
		} catch (Exception e) {
			logger.error("生成计算部门人数任务保存失败：" + e.getMessage(), e);
		}
	}

}
