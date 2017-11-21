package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoExtVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.qwinterface.addressbook.IUserInfoChangeInform;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformManager;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserInfoChangeNotifier{
	private final static Logger logger = LoggerFactory.getLogger(UserInfoChangeNotifier.class);
	private final static int ADD_USER = 1;
	private final static int RECOVER_USER = 2;
	private final static int MOVE_END = 8;
	private final static int SHIFT_OUT = 9;
	private final static int SHIFT_IN = 10;

	/**
	 * 新增用户信息
	 * @param user 用户po
	 * @param deptId  用户部门id
	 * @param userInfoExtVO  用户扩展信息
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @author Sun Qinghai
	 * @2015-9-22
	 * @version 1.0
	 */
	public static void addUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userInfoPo, deptId, userInfoExtVO, type, ADD_USER);
		new Thread(thread).start();
	}

	/**
	 * 修改用户信息
	 * @param oldUser 修改前的用户po
	 * @param oldDeptId  修改后的用户部门id
	 * @param user 用户po
	 * @param deptId  用户部门id
	 * @param userInfoExtVO  用户扩展信息
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @author Sun Qinghai
	 * @2015-9-22
	 * @version 1.0
	 */
	public static void updateUser(UserOrgVO user,TbQyUserInfoPO oldUser, List<String> oldDeptId,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, oldUser, oldDeptId, userInfoPo, deptId, userInfoExtVO, type);
		new Thread(thread).start();
	}

	/**
	 * 删除用户信息
	 * @param user 用户po
	 * @param deptId  用户部门id
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @author Sun Qinghai
	 * @2015-9-22
	 * @version 1.0
	 */
	public static void delUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, int type){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userInfoPo, deptId, type);
		new Thread(thread).start();
	}

	/**
	 * 离职用户信息
	 * @param user 用户po
	 * @param deptId  用户部门id
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @param matterMap 离职用户需要交接的事项 key是agentCode,value是需要交接的事项列表
	 * @author Sun Qinghai
	 * @2015-9-22
	 * @version 1.0
	 */
	public static void leaveUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userInfoPo, deptId, type, matterMap);
		new Thread(thread).start();
	}
	/**
	 * 批量离职用户信息
	 * @param user 用户po
	 * @param users  离职用户id
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @author Sun Qinghai
	 * @2016-4-13
	 * @version 1.0
	 */
	public static void batchLeaveUser(UserOrgVO user, List<TbQyUserInfoPO> users, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0) {
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchLeaveUser(user,users, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifier batchLeaveUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifier batchLeaveUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}

	/**
	 * 复职用户信息
	 * @param user 用户po
	 * @param deptId  用户部门id
	 * @param userInfoExtVO  用户扩展信息
	 * @param type  操作类型（新增用户、导入、同步等）
	 * @author Sun Qinghai
	 * @2015-9-22
	 * @version 1.0
	 */
	public static void recoverUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userInfoPo, deptId, userInfoExtVO, type, RECOVER_USER);
		new Thread(thread).start();
	}

	/**
	 * 同步结束提醒
	 * @param user
	 * @param addList
	 * @param delList
	 * @param userRefDeptMap  @author Sun Qinghai
	 * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @2015-9-24
	 * @version 1.0
	 */
	public static void syncEnd(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap){
		if((addList==null || addList.size() ==0) && (updateList==null || updateList.size() ==0) && (delList==null || delList.size() ==0)){
			return;
		}
		if(UserInfoChangeInformManager.eventMap.size()>0) {
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchChanged(user,addList, updateList, delList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap, UserInfoChangeInformType.SYNC_MGR);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifier syncEnd "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifier syncEnd", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}

	/**
	 * 导入结束提醒
	 * @param user
	 * @param addList
	 * @param userRefDeptMap
	 * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @author Sun Qinghai
	 * @2015-9-24
	 * @version 1.0
	 */
	public static void importEnd(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap){
		for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
			try {
				(processerEntry.getValue()).batchChanged(user,addList, updateList, delList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap, UserInfoChangeInformType.IMPORT_MGR);
			} catch (Exception e) {
				logger.error("UserInfoChangeNotifier importEnd "+processerEntry.getKey(), e);
				ExceptionCenter.addException(e, "UserInfoChangeNotifier importEnd", processerEntry.getKey()+"|"+user.getUserId());
			}
		}
	}

	/**
	 * 批量操作人资同步等调用此接口
	 * @param user
	 * @param addList
	 * @param updateList
	 * @param delList
	 * @param userRefDeptMap
	 * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
	 * @param type
	 * @author sunqinghai
	 * @date 2016 -6-30
	 */
	public static void batchChanged(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, addList, updateList, delList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap, type);
		new Thread(thread).start();
	}

	/**
	 * 批量删除结束提醒
	 * @param user
	 * @param userIds
	 * @param type
	 * @author Sun Qinghai
	 * @2015-9-24
	 * @version 1.0
	 */
	public static void batchDelEnd(UserOrgVO user, String[] userIds, int type){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userIds, type);
		new Thread(thread).start();
	}

	/**
	 * 批量移动部门结束提醒
	 * @param user
	 * @param userIds
	 * @author Sun Qinghai
	 * @2015-9-24
	 * @version 1.0
	 */
	public static void batchMoveEnd(UserOrgVO user, List<String> userIds, List<String> deptId){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userIds, deptId, MOVE_END);
		new Thread(thread).start();
	}

	public static void batchShiftOutUser(UserOrgVO user, List<String> userIds, List<String> deptId){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userIds, deptId, SHIFT_OUT);
		new Thread(thread).start();
	}
	public static void batchShiftIntUser(UserOrgVO user, List<String> userIds, List<String> deptId){
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, userIds, deptId, SHIFT_IN);
		new Thread(thread).start();
	}

	public static void updateUserStatusAndHeadPic(TbQyUserInfoPO userPO, int type) {
		//更新用户状态和头像
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(userPO, type);
		new Thread(thread).start();
	}

	/**
	 * 获取操作人员的部门人员关联关系，用于新增和离职用户
	 * @param detIds
	 * @param userId
	 * @param deptRefUserMap
	 * @author sunqinghai
	 * @date 2016 -6-30
	 */
	public static void getDeptUserRef(List<String> detIds,String userId,Map<String, List<String>> deptRefUserMap){
		if(detIds == null || detIds.size()==0){
			return;
		}
		for(String deptId:detIds){
			List<String> addList = deptRefUserMap.get(deptId);
			if(addList==null){
				addList = new ArrayList<String>(100);
				deptRefUserMap.put(deptId,addList);
			}
			addList.add(userId);
		}
	}

	/**
	 * 获取操作人员的部门人员关联关系，用于编辑
	 * @param oldDetIds
	 * @param newDetIds
	 * @param userId
	 * @param addDeptRefUserMap
	 * @param delDeptRefUserMap
	 * @author sunqinghai
	 * @date 2016 -6-30
	 */
	public static void getDeptUserRef(List<String> oldDetIds,List<String> newDetIds,String userId,Map<String, List<String>> addDeptRefUserMap,Map<String, List<String>> delDeptRefUserMap){
		List<List<String>> list = ListUtil.getDiffer(oldDetIds,newDetIds);
		//需要新增的部门人员关系
		getDeptUserRef(list.get(0),userId,addDeptRefUserMap);
		//需要删除的部门人员关系
		getDeptUserRef(list.get(1),userId,delDeptRefUserMap);
	}

	/**
	 * 批量操作部门信息
	 * @param user
	 * @param addList
	 * @param updateList
	 * @param delList
	 * @param type
	 * @author sunqinghai
	 * @date 2016 -7-5
	 */
	public static void batchChangeDept(UserOrgVO user, List<DeptSyncInfoVO> addList, List<DeptSyncInfoVO> updateList, List<DeptSyncInfoVO> delList, int type){
		if((addList==null || addList.size() ==0) && (updateList==null || updateList.size() ==0) && (delList==null || delList.size() ==0)){
			return;
		}
		UserInfoChangeNotifierThread thread = new UserInfoChangeNotifierThread(user, addList, updateList, delList, type);
		new Thread(thread).start();
	}
}
