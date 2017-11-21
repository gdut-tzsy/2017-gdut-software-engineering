package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoExtVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.qwinterface.addressbook.IUserInfoChangeInform;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class UserInfoChangeNotifierThread implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(UserInfoChangeNotifierThread.class);
	private int method;
	private UserOrgVO user;
	TbQyUserInfoPO userInfoPo;
	List<String> deptId;
	UserInfoExtVO userInfoExtVO;
	int type;
	TbQyUserInfoPO oldUser;
	List<String> oldDeptId;
	List<DeptSyncInfoVO> addDeptList;
	List<DeptSyncInfoVO> updateDeptList;
	List<DeptSyncInfoVO> delDeptList;
	List<TbQyUserInfoPO> addList;
	List<TbQyUserInfoPO> updateList;
	List<TbQyUserInfoPO> delList;
	Map<String, List<String>> userRefDeptMap;
	Map<String, List<String>> addDeptRefUserMap;
	Map<String, List<String>> delDeptRefUserMap;
	String[] userIds;
	List<String> userIdList;
	Map<String, List<HandoverMatterVO>> matterMap;

	public UserInfoChangeNotifierThread(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type, int method){
		this.method = method;//1addUser,2recoverUser
		this.user = user;
		this.userInfoPo = userInfoPo;
		this.deptId = deptId;
		this.userInfoExtVO = userInfoExtVO;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user,TbQyUserInfoPO oldUser, List<String> oldDeptId,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type){
		this.method = 3;//updateUser
		this.user = user;
		this.oldUser = oldUser;
		this.oldDeptId = oldDeptId;
		this.userInfoPo = userInfoPo;
		this.deptId = deptId;
		this.userInfoExtVO = userInfoExtVO;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, int type){
		this.method = 4;//delUser
		this.user = user;
		this.userInfoPo = userInfoPo;
		this.deptId = deptId;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap){
		this.method = 5;//leaveUser
		this.user = user;
		this.userInfoPo = userInfoPo;
		this.deptId = deptId;
		this.matterMap = matterMap;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type){
		this.method = 6;//batchChanged
		this.user = user;
		this.addList = addList;
		this.updateList = updateList;
		this.delList = delList;
		this.userRefDeptMap = userRefDeptMap;
		this.addDeptRefUserMap = addDeptRefUserMap;
		this.delDeptRefUserMap = delDeptRefUserMap;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user, String[] userIds, int type){
		this.method = 7;//batchDelEnd
		this.user = user;
		this.userIds = userIds;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user, List<String> userIds, List<String> deptId, int method){
		this.method = method;//8 batchMoveEnd,9 batchShiftOutUser,10 batchShiftIntUser
		this.user = user;
		this.userIdList = userIds;
		this.deptId = deptId;
	}
	public UserInfoChangeNotifierThread(TbQyUserInfoPO userPO, int type){
		this.method = 11;//updateUserStatusAndHeadPic
		this.userInfoPo = userPO;
		this.type = type;
	}
	public UserInfoChangeNotifierThread(UserOrgVO user, List<DeptSyncInfoVO> addList, List<DeptSyncInfoVO> updateList, List<DeptSyncInfoVO> delList, int type){
		this.method = 12;//batchChangeDept
		this.user = user;
		this.addDeptList = addList;
		this.updateDeptList = updateList;
		this.delDeptList = delList;
		this.type = type;
	}


	@Override
	public void run() {
		switch (method){
			case 1 :addUser(user, userInfoPo, deptId, userInfoExtVO, type); break;
			case 2 :recoverUser(user, userInfoPo, deptId, userInfoExtVO, type); break;
			case 3 :updateUser(user, oldUser, oldDeptId, userInfoPo, deptId, userInfoExtVO, type); break;
			case 4 :delUser(user, userInfoPo, deptId, type); break;
			case 5 :leaveUser(user, userInfoPo, deptId, type, matterMap); break;
			case 6 :batchChanged(user, addList, updateList, delList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap, type); break;
			case 7 :batchDelEnd(user, userIds, type); break;
			case 8 :batchMoveEnd(user, userIdList, deptId); break;
			case 9 :batchShiftOutUser(user, userIdList, deptId); break;
			case 10 :batchShiftIntUser(user, userIdList, deptId); break;
			case 11 :updateUserStatusAndHeadPic(userInfoPo, type); break;
			case 12 :batchChangeDept(user, addDeptList, updateDeptList, delDeptList, type); break;
			default:ExceptionCenter.addException(new BaseException("不能识别的类型"), "UserInfoChangeNotifierThread run 不能识别的类型", String.valueOf(method));
		}
	}
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
	private void addUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).addUser(user,userInfoPo, deptId, userInfoExtVO, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread addUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread addUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
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
	private void updateUser(UserOrgVO user,TbQyUserInfoPO oldUser, List<String> oldDeptId,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).updateUser(user,oldUser, oldDeptId, userInfoPo, deptId, userInfoExtVO, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread updateUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread updateUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
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
	private void delUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).delUser(user,userInfoPo, deptId, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread delUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread delUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
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
	private void leaveUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap){
		if(UserInfoChangeInformManager.eventMap.size()>0) {
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).leaveUser(user,userInfoPo, deptId, type, matterMap);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread leaveUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread leaveUser", processerEntry.getKey()+"|"+user.getUserId());
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
	private void recoverUser(UserOrgVO user,TbQyUserInfoPO userInfoPo, List<String> deptId,UserInfoExtVO userInfoExtVO, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).recoverUser(user,userInfoPo, deptId, userInfoExtVO, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread recoverUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread recoverUser", processerEntry.getKey()+"|"+user.getUserId());
				}
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
	private void batchChanged(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0) {
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchChanged(user,addList, updateList, delList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchChanged "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchChanged", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
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
	private void batchDelEnd(UserOrgVO user, String[] userIds, int type){
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchDelEnd(user, userIds, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchDelEnd "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchDelEnd", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}

	/**
	 * 批量移动部门结束提醒
	 * @param user
	 * @param userIds
	 * @author Sun Qinghai
	 * @2015-9-24
	 * @version 1.0
	 */
	private void batchMoveEnd(UserOrgVO user, List<String> userIds, List<String> deptId){
		//更新部门人数
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchMoveEnd(user, userIds, deptId);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchMoveEnd "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchMoveEnd", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}

	private void batchShiftOutUser(UserOrgVO user, List<String> userIds, List<String> deptId){
		//更新部门人数
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchShiftOutUser(user, userIds, deptId);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchShiftOutUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchShiftOutUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}
	private void batchShiftIntUser(UserOrgVO user, List<String> userIds, List<String> deptId){
		//更新部门人数
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchShiftIntUser(user, userIds, deptId);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchShiftIntUser "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchShiftIntUser", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}

	private void updateUserStatusAndHeadPic(TbQyUserInfoPO userPO, int type) {
		//更新用户状态和头像
		if(UserInfoChangeInformManager.eventMap.size()>0){
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).updateUserStatusAndHeadPic(userPO, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread updateUserStatusAndHeadPic "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread updateUserStatusAndHeadPic", processerEntry.getKey()+"|"+userPO.getUserId());
				}
			}
		}
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
		if(UserInfoChangeInformManager.eventMap.size()>0) {
			for (Map.Entry<String, IUserInfoChangeInform> processerEntry : UserInfoChangeInformManager.eventMap.entrySet()) {
				try {
					(processerEntry.getValue()).batchChangeDept(user,addList, updateList, delList, type);
				} catch (Exception e) {
					logger.error("UserInfoChangeNotifierThread batchChangeDept "+processerEntry.getKey(), e);
					ExceptionCenter.addException(e, "UserInfoChangeNotifierThread batchChangeDept", processerEntry.getKey()+"|"+user.getUserId());
				}
			}
		}
	}
}
