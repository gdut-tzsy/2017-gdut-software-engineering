package cn.com.do1.component.contact.contact.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.model.TbQyUserSyncPO;
import cn.com.do1.component.addressbook.contact.util.ContactSyncStatus;
import cn.com.do1.component.managesetting.managesetting.service.IManagesettingService;
import cn.com.do1.component.managesetting.managesetting.vo.UserInfoMgrRefCache;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.runtask.util.TaskStatus;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentHistoryPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qwinterface.runtask.IRunTask;
import cn.com.do1.component.runtask.runtask.model.TbRunTaskPO;
import cn.com.do1.component.runtask.runtask.service.IRunTaskService;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.runtask.util.TaskType;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 
 * <p>Title: 更新应用信息，包括可见范围</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-6-11
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
@ProcesserUnit(name = TaskType.AddressbookSyncTask)
public class AddressbookSyncTask implements IRunTask{
	private final static transient Logger logger = LoggerFactory.getLogger(AddressbookSyncTask.class);
	private static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
	private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
	private static IRunTaskService runTaskService = DqdpAppContext.getSpringContext().getBean("runTaskService", IRunTaskService.class);
	private static IManagesettingService managesettingService = DqdpAppContext.getSpringContext().getBean("managesettingService", IManagesettingService.class);
	/**
	 * 同步初始化其它服务器的agentId
	 * @return
	 * @throws Exception
	 * @author Sun Qinghai
	 * @2014-10-24
	 * @version 1.0
	 */
	/* （非 Javadoc）
	 * @see cn.com.do1.component.qwinterface.runtask.IRunTask#runTask(cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO)
	 */
	@Override
	public TbRunTaskVO runTask(TbRunTaskVO task){
		//如果改corpId正在同步中，直接将同步状态改为初始状态，下次再同步，以防两个任务同时运行导致通讯录出现重复问题
		synchronized (ContactSyncStatus.getRunCorpIds()){
			if(!ContactSyncStatus.add(task.getCorpId())){
				logger.debug("AddressbookSyncTask 同步用户推迟，corpId：" + task.getCorpId() + ",org_id:" + task.getOrgId() + ",userId" +task.getCreatePerson());
				task.setStatus(TaskStatus.TASK_INIT);
				return task;
			}
		}
		/*task.setCorpId("wx05ab253814890c89");
		task.setAgentCode("recruit");
		task.setOrgId("b17efb43-292e-4cc9-ac5d-0b46bce059c4");
		task.setCreatePerson("test1");*/
		//task.setParam("{\"agentCode\":\"recruit\"}");
		//微信上的用户部门id，对应的本地创建的部门信息
		Set<String> updateDepts = new HashSet<String>();//待更新的部门
		Set<String> updateUsers = new HashSet<String>();//待更新的人员
		Set<String> updateTags = new HashSet<String>();//待更新的标签

		List<TbQyExperienceAgentHistoryPO> addHistoryList = new ArrayList<TbQyExperienceAgentHistoryPO>(10);
		List<TbQyExperienceAgentHistoryPO> updateHistoryList = new ArrayList<TbQyExperienceAgentHistoryPO>(10);
		
		String corpId = task.getCorpId();
		String org_id = task.getOrgId();
		String userId = task.getCreatePerson();
		logger.debug("AddressbookSyncTask 同步用户开始，corpId："+corpId+",org_id:"+org_id +",userId"+userId);
		//TbDqdpOrganizationInsertPO insertPO = null;
		boolean isAll = false;//是否更新所有用户
		String syncPOId = null;
		try{
			String agentCode = null;
			String suiteId = null;
			boolean isSuite = false;
			boolean isAddressBookSync = true;//是否同步通讯录

			if(!StringUtil.isNullEmpty(task.getParam())){
				JSONObject jsonObject = JSONObject.fromObject(task.getParam());
				if(jsonObject.has("suiteId")){
					suiteId = jsonObject.getString("suiteId");
					isSuite = true;
					isAddressBookSync = false;
				}
				if(jsonObject.has("agentCode")){
					agentCode = jsonObject.getString("agentCode");
					isAddressBookSync = false;
				}
				if(jsonObject.has("syncPOId")){
					syncPOId = jsonObject.getString("syncPOId");
				}
			}
			if(corpId==null){
				logger.error("AddressbookSyncTask 同步用户失败,机构为体验用户--，org_id："+org_id+"corpId："+corpId);
				task.setStatus(TaskStatus.TASK_FAILURE);
				task.setResultDesc("corpId为空");
				return task;
			}
			if(corpId.equals(Configuration.AUTO_CORPID)){
				logger.error("AddressbookSyncTask 同步用户失败,机构为体验用户--，org_id："+org_id+"corpId："+corpId);
				task.setStatus(TaskStatus.TASK_FAILURE);
				task.setResultDesc("同步用户失败,机构为体验用户");
				return task;
			}
			ExtOrgPO orgPO;
			if(StringUtil.isNullEmpty(org_id)){
				List<ExtOrgPO> list = contactService.getOrgPOByCorpId(corpId);
				if(list == null || list.size()==0){
					logger.error("AddressbookSyncTask同步用户失败,机构为体验用户--，org_id："+org_id+"corpId："+corpId);
					task.setStatus(TaskStatus.TASK_FAILURE);
					task.setResultDesc("同步用户失败,机构不存在");
					return task;
				}
				orgPO = list.get(0);
				org_id = orgPO.getOrganizationId();
				
			}
			else {
				orgPO = contactService.searchByPk(ExtOrgPO.class, org_id);
			}
			if(StringUtil.isNullEmpty(orgPO.getWxId()) || StringUtil.isNullEmpty(orgPO.getWxParentid())){
				logger.error("AddressbookSyncTask 机构信息中微信部门id出现异常，请联系企微管理员 org_id："+org_id+"corpId："+corpId);
				task.setStatus(TaskStatus.TASK_FAILURE);
				task.setResultDesc("机构信息中微信部门id出现异常");
				return task;
			}
			if(!orgPO.getCorpId().equals(corpId)){
				logger.error("AddressbookSyncTask 机构信息中corpId出现异常，请联系企微管理员 org_id："+org_id+"corpId："+corpId);
				task.setStatus(TaskStatus.TASK_FAILURE);
				task.setResultDesc("机构信息中corpId出现异常");
				return task;
			}
			//如果是同步通讯录，并且通讯录的可见范围不是全公司，那就只同步通讯录
			if(isAddressBookSync && !StringUtil.isNullEmpty(syncPOId) && !WxAgentUtil.isAllUserUsable(corpId, WxAgentUtil.getAddressBookCode())){
				agentCode = WxAgentUtil.getAddressBookCode();
				isAddressBookSync = false;
				//清楚同步历史，重新全量同步
				TbQyExperienceAgentHistoryPO po = new TbQyExperienceAgentHistoryPO();
				po.setId(corpId+"_"+agentCode);
				experienceapplicationService.delPO(po);
			}
			if(isAddressBookSync){
				UserListSyncThread ulst = new UserListSyncThread(corpId, org_id, userId, false, syncPOId);
				if(WxAgentUtil.isAllUserUsable(corpId, WxAgentUtil.getAddressBookCode())){
					isAll = true;
					ulst.run();
					return null;
				}
				else {
					//查看是否有可见范围为全公司的应用
					TbQyExperienceAgentPO rangeAllAgent = experienceapplicationService.getRangeAllAgentByCorpid(corpId);
					if(rangeAllAgent == null){
						List<TbQyExperienceAgentPO> agents = experienceapplicationService.findSuiteAgentByCorpid(corpId);
						if(agents != null && agents.size()>0){
							String tempSuiteid = agents.get(0).getSuiteId();
							String syncAgentCode = null;
							boolean isAllUser = false;//套件是否存在全部人可见的应用
							for (TbQyExperienceAgentPO agentPO : agents) {
								//如果不是同一个套件的应用，需要一个套件一个套件的更新
								if(!tempSuiteid.equals(agentPO.getSuiteId())){
									tempSuiteid = agentPO.getSuiteId();
									//开始同步此应用下的人员
									if(updateDepts.size()>0 || updateUsers.size()>0 || updateTags.size()>0){
										ulst.syncDeptUserTags(syncAgentCode,updateDepts,updateUsers,updateTags, isAllUser);
										updateDepts.clear();
										updateUsers.clear();
										updateTags.clear();
									}
									isAllUser = false;
								}
								syncAgentCode = agentPO.getAgentCode();
								//获取需要同步的部门人员等信息
								isAll = syncAgentPrivilege(agentPO, org_id, updateDepts, updateUsers, updateTags, addHistoryList, updateHistoryList);
								if(isAll){
									updateDepts.clear();
									updateUsers.clear();
									updateTags.clear();
									updateDepts.add("1");
									//开始同步此应用下的人员
									ulst.syncDeptUserTags(syncAgentCode,updateDepts,updateUsers,updateTags, isAll);
									return null;
								}
								//应用是否全公司可见
								if(!isAllUser){
									isAllUser = "1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"));
								}
							}
							//开始同步此应用下的人员
							if(updateDepts.size()>0 || updateUsers.size()>0 || updateTags.size()>0){
								ulst.syncDeptUserTags(syncAgentCode,updateDepts,updateUsers,updateTags, isAll);
							}
						}
						return null;
					}
					else{
						updateDepts.clear();
						updateUsers.clear();
						updateTags.clear();
						updateDepts.add("1");
						//开始同步此应用下的人员
						ulst.syncDeptUserTags(rangeAllAgent.getAgentCode(),updateDepts,updateUsers,updateTags, isAll);
						return null;
					}
				}
			}
			else if(isSuite){
				List<TbQyExperienceAgentPO> agents = experienceapplicationService.findSuiteAgentByCorpidSuiteid(corpId, suiteId);
				if(agents != null && agents.size()>0){
					String syncAgentCode = null;
					boolean isAllUser = false;//套件是否存在全部人可见的应用
					for (TbQyExperienceAgentPO agentPO : agents) {
						syncAgentCode = agentPO.getAgentCode();
						//获取需要同步的部门人员等信息
						isAll = syncAgentPrivilege(agentPO, org_id, updateDepts, updateUsers, updateTags, addHistoryList, updateHistoryList);
						if(isAll){
							UserListSyncThread ulst = new UserListSyncThread(corpId, org_id, userId, false, syncPOId);
							if(WxAgentUtil.isTrustAgent(agentPO.getCorpId(), WxAgentUtil.getAddressBookCode())){
								ulst.run();
								return null;
							}
							else{
								updateDepts.clear();
								updateUsers.clear();
								updateTags.clear();
								updateDepts.add("1");
								//开始同步此应用下的人员
								ulst.syncDeptUserTags(agentPO.getAgentCode(),updateDepts,updateUsers,updateTags, isAll);
								return null;
							}
						}
						//应用是否全公司可见
						if(!isAllUser){
							isAllUser = "1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"));
						}
					}
					//开始同步此应用下的人员
					if(updateDepts.size()>0 || updateUsers.size()>0 || updateTags.size()>0){
						UserListSyncThread ulst = new UserListSyncThread(corpId, org_id, userId, false, syncPOId);
						ulst.syncDeptUserTags(syncAgentCode,updateDepts,updateUsers,updateTags, isAllUser);
						return null;
					}
				}
			}
			else{
				TbQyExperienceAgentPO agentPO = experienceapplicationService.getAppByAgentCodeAndCorpId(agentCode, corpId);
				if(agentPO == null){
					return null;
				}
				isAll = syncAgentPrivilege(agentPO, org_id, updateDepts, updateUsers, updateTags, addHistoryList, updateHistoryList);
				if(isAll){
					UserListSyncThread ulst = new UserListSyncThread(corpId, org_id, userId, false, syncPOId);
					if(WxAgentUtil.isTrustAgent(agentPO.getCorpId(), WxAgentUtil.getAddressBookCode())){
						ulst.run();
						return null;
					}
					else{
						updateDepts.clear();
						updateUsers.clear();
						updateTags.clear();
						updateDepts.add("1");
						//开始同步此应用下的人员
						ulst.syncDeptUserTags(agentPO.getAgentCode(),updateDepts,updateUsers,updateTags, isAll);
						return null;
					}
				}
				else if(updateDepts.size()>0 || updateUsers.size()>0 || updateTags.size()>0){
					//开始同步此应用下的人员
					UserListSyncThread ulst = new UserListSyncThread(corpId, org_id, userId, false, syncPOId);
					//应用是否全公司可见
					boolean isAllUser = "1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"));
					ulst.syncDeptUserTags(agentPO.getAgentCode(),updateDepts,updateUsers,updateTags,isAllUser);
					return null;
				}
			}
		}catch(Exception e){
			logger.error("AddressbookSyncTask 同步用户失败--，corpId："+corpId,e);
		}
		catch (BaseException ex) {
			logger.error("AddressbookSyncTask 同步用户失败--，corpId："+corpId,ex);
		}
		finally{
			ChangeAgentUserUtil.changeAllAgentUser(corpId);

			ContactSyncStatus.remove(corpId);
			if(isAll){
				addHistoryList.clear();
				updateHistoryList.clear();
				getUpdateHistoryInfo(corpId, addHistoryList, updateHistoryList);
			}
			try {
				if(addHistoryList.size()>0){
					QwtoolUtil.addBatchList(addHistoryList, false);
				}
			} catch (BaseException e) {
				logger.error("AddressbookSyncTask 新增应用可见范围历史记录失败--，corpId："+corpId,e);
			} catch (Exception e) {
				logger.error("AddressbookSyncTask 新增应用可见范围历史记录失败--，corpId："+corpId,e);
			}
			try {
				if(updateHistoryList.size()>0){
					QwtoolUtil.updateBatchList(updateHistoryList, false, true);
				}
			} catch (BaseException e) {
				logger.error("AddressbookSyncTask 更新应用可见范围历史记录失败--，corpId："+corpId,e);
			} catch (Exception e) {
				logger.error("AddressbookSyncTask 更新应用可见范围历史记录失败--，corpId："+corpId,e);
			}
			if (!StringUtil.isNullEmpty(syncPOId)) {
				//更新该条同步通讯录已经完成状态
				TbQyUserSyncPO syncPO =new TbQyUserSyncPO();
				syncPO.setId(syncPOId);
				syncPO.setExt1("1");
				try {
					contactService.updatePO(syncPO, false);
				} catch (SQLException e1) {
					logger.error("同步用户失败,更新是否同步状态失败--，corpId："+corpId,e1);
				}

				try {
					CacheWxqyhObject.set("synctask", org_id, syncPOId, true, 600);
					UserInfoMgrRefCache vo = managesettingService.getMgrAndUserIdRef(userId,org_id);
					if(vo!=null){
						WxMessageUtil.sendTextMessage(vo.getWxUserId(),"通讯录已同步完成",WxAgentUtil.getAddressBookCode(),
								corpId,org_id);
					}
				} catch (Exception e) {
					logger.error("同步用户失败,推送同步完成消息失败--，corpId："+corpId,e);
				} catch (BaseException e) {
					logger.error("同步用户失败,推送同步完成消息失败--，corpId："+corpId,e);
				}
			}
			logger.debug("AddressbookSyncTask 同步用户结束，corpId："+corpId);
		}
		return null;
	}
	
	/**
	 * 当更新全部通讯录时，获取需要重置的历史数据
	 * @param corpId
	 * @author Sun Qinghai
	 * @2016-2-23
	 * @version 1.0
	 */
	private void getUpdateHistoryInfo(String corpId, List<TbQyExperienceAgentHistoryPO> addHistoryList, List<TbQyExperienceAgentHistoryPO> updateHistoryList){
		try {
			List<TbQyExperienceAgentPO> list = experienceapplicationService.getExperienceAgentList(corpId);
			if(list != null && list.size()>0){
				for (TbQyExperienceAgentPO agentPO : list) {
					TbQyExperienceAgentHistoryPO history = experienceapplicationService.getAgentHistory(agentPO.getCorpId(), agentPO.getAgentCode());
					if(history == null){
						history = new TbQyExperienceAgentHistoryPO();
						history.setId(agentPO.getCorpId()+"_"+agentPO.getAgentCode());
						history.setPartys(agentPO.getPartys());
						history.setUserinfos(agentPO.getUserinfos());
						history.setTags(agentPO.getTags());
						history.setExtraPartys(agentPO.getExtraPartys());
						history.setExtraTags(agentPO.getExtraTags());
						history.setExtraUserinfos(agentPO.getExtraUserinfos());
						history.setLevel(agentPO.getLevel());
						//本次不同步了，下次再同步
						if("1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"))){
							history.setIsRangeAll(1);
						}
						else {
							history.setIsRangeAll(0);
						}
						addHistoryList.add(history);
					}
					else{
						history.setPartys(agentPO.getPartys());
						history.setUserinfos(agentPO.getUserinfos());
						history.setTags(agentPO.getTags());
						history.setExtraPartys(agentPO.getExtraPartys());
						history.setExtraTags(agentPO.getExtraTags());
						history.setExtraUserinfos(agentPO.getExtraUserinfos());
						history.setLevel(agentPO.getLevel());
						//本次不同步了，下次再同步
						if("1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"))){
							history.setIsRangeAll(1);
						}
						else {
							history.setIsRangeAll(0);
						}
						updateHistoryList.add(history);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getUpdateHistoryInfo更新全部通讯录更新历史数据  corpId"+corpId,e);
		} catch (BaseException e) {
			logger.error("getUpdateHistoryInfo更新全部通讯录更新历史数据  corpId"+corpId,e);
		}
	}
	
	/**
	 * 同步用户权限
	 * 
	 * @author Sun Qinghai
	 * @2016-1-12
	 * @version 1.0
	 * @throws Exception 
	 */
	private boolean syncAgentPrivilege(TbQyExperienceAgentPO agentPO, String orgId,
			Set<String> updateDepts, Set<String> updateUsers, Set<String> updateTags,
			List<TbQyExperienceAgentHistoryPO> addHistoryList, List<TbQyExperienceAgentHistoryPO> updateHistoryList) throws Exception{
		TbQyExperienceAgentHistoryPO history = experienceapplicationService.getAgentHistory(agentPO.getCorpId(), agentPO.getAgentCode());
		boolean isUpdateAll = false;
		if(history != null){
			//如果可见范围没有改变
			if(1 == history.getIsRangeAll()){
				history.setPartys(agentPO.getPartys());
				history.setUserinfos(agentPO.getUserinfos());
				history.setTags(agentPO.getTags());
				history.setExtraPartys(agentPO.getExtraPartys());
				history.setExtraTags(agentPO.getExtraTags());
				history.setExtraUserinfos(agentPO.getExtraUserinfos());
				history.setLevel(agentPO.getLevel());
				//本次不同步了，下次再同步
				if("1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"))){
					history.setIsRangeAll(1);
					if(agentPO.getLevel()!=null && history.getLevel()!=null && agentPO.getLevel()>history.getLevel()){//如果提高了通讯录等级
						isUpdateAll = true;
					}
				}
				else {
					history.setIsRangeAll(0);
					if(agentPO.getLevel()!=null && history.getLevel()!=null && agentPO.getLevel()>history.getLevel() && !WxAgentUtil.isTrustAgent(agentPO.getCorpId(), WxAgentUtil.getAddressBookCode())){
						createUpdateInfo(agentPO, updateDepts, updateUsers, updateTags);
					}
				}
				updateHistoryList.add(history);
				return isUpdateAll;
			}
			else{
				if("1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"))){
					//同步全公司
					history.setPartys(agentPO.getPartys());
					history.setUserinfos(agentPO.getUserinfos());
					history.setTags(agentPO.getTags());
					history.setExtraPartys(agentPO.getExtraPartys());
					history.setExtraTags(agentPO.getExtraTags());
					history.setExtraUserinfos(agentPO.getExtraUserinfos());
					history.setLevel(agentPO.getLevel());
					history.setIsRangeAll(1);
					updateDepts.add("1");
					updateHistoryList.add(history);
					isUpdateAll = true;
					return isUpdateAll;
				}
				Set<String> tempSet;
				String[] tempArray;
				boolean isNeedUpdate = false;
				if(!StringUtil.isNullEmpty(agentPO.getPartys()) && !StringUtil.strCstr(agentPO.getPartys(), history.getPartys())){
					//检查需要同步的部门
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getPartys().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getPartys())){
						tempArray = history.getPartys().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateDepts.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(!StringUtil.isNullEmpty(agentPO.getUserinfos()) && !StringUtil.strCstr(agentPO.getUserinfos(), history.getUserinfos())){
					//检查需要同步的人员
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getUserinfos().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getUserinfos())){
						tempArray = history.getUserinfos().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateUsers.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(!StringUtil.isNullEmpty(agentPO.getTags()) && !StringUtil.strCstr(agentPO.getTags(), history.getTags())){
					//检查需要同步的标签
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getTags().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getTags())){
						tempArray = history.getTags().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateTags.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && !StringUtil.strCstr(agentPO.getExtraPartys(), history.getExtraPartys())){
					//检查需要同步的通讯录部门
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraPartys().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getExtraPartys())){
						tempArray = history.getExtraPartys().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateDepts.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(!StringUtil.isNullEmpty(agentPO.getExtraUserinfos()) && !StringUtil.strCstr(agentPO.getExtraUserinfos(), history.getExtraUserinfos())){
					//检查需要同步的通讯录用户
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraUserinfos().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getExtraUserinfos())){
						tempArray = history.getExtraUserinfos().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateUsers.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(!StringUtil.isNullEmpty(agentPO.getExtraTags()) && !StringUtil.strCstr(agentPO.getExtraTags(), history.getExtraTags())){
					//检查需要同步的通讯录标签
					tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraTags().split("\\|")));
					if(!StringUtil.isNullEmpty(history.getExtraTags())){
						tempArray = history.getExtraTags().split("\\|");
						for (String temp : tempArray) {
							tempSet.remove(temp);
						}
					}
					tempSet.remove("");//删掉空项
					updateTags.addAll(tempSet);
					isNeedUpdate = true;
				}
				if(isNeedUpdate){
					history.setPartys(agentPO.getPartys());
					history.setUserinfos(agentPO.getUserinfos());
					history.setTags(agentPO.getTags());
					history.setExtraPartys(agentPO.getExtraPartys());
					history.setExtraTags(agentPO.getExtraTags());
					history.setExtraUserinfos(agentPO.getExtraUserinfos());
					history.setLevel(agentPO.getLevel());
					history.setIsRangeAll(0);
					updateHistoryList.add(history);
				}
				return isUpdateAll;

			}
		}
		else{
			createUpdateInfo(agentPO, updateDepts, updateUsers, updateTags);
			history = new TbQyExperienceAgentHistoryPO();
			history.setId(agentPO.getCorpId()+"_"+agentPO.getAgentCode());
			history.setPartys(agentPO.getPartys());
			history.setUserinfos(agentPO.getUserinfos());
			history.setTags(agentPO.getTags());
			history.setExtraPartys(agentPO.getExtraPartys());
			history.setExtraTags(agentPO.getExtraTags());
			history.setExtraUserinfos(agentPO.getExtraUserinfos());
			history.setLevel(agentPO.getLevel());
			//本次不同步了，下次再同步
			if("1".equals(agentPO.getIsRangeAll()) || (!StringUtil.isNullEmpty(agentPO.getExtraPartys()) && agentPO.getExtraPartys().contains("|1|"))){
				history.setIsRangeAll(1);
				isUpdateAll = true;
			}
			else {
				history.setIsRangeAll(0);
			}
			addHistoryList.add(history);
			return isUpdateAll;
		}
	}
	
	/**
	 * 生成需要更新的数据
	 * @param agentPO
	 * @param updateDepts
	 * @param updateUsers
	 * @param updateTags
	 * @author Sun Qinghai
	 * @2016-2-23
	 * @version 1.0
	 */
	private void createUpdateInfo(TbQyExperienceAgentPO agentPO,
			Set<String> updateDepts, Set<String> updateUsers, Set<String> updateTags) {
		Set<String> tempSet;
		if(!StringUtil.isNullEmpty(agentPO.getPartys())){
			//检查需要同步的部门
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getPartys().split("\\|")));
			tempSet.remove("");//删掉空项
			updateDepts.addAll(tempSet);
		}
		if(!StringUtil.isNullEmpty(agentPO.getUserinfos())){
			//检查需要同步的人员
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getUserinfos().split("\\|")));
			tempSet.remove("");//删掉空项
			updateUsers.addAll(tempSet);
		}
		if(!StringUtil.isNullEmpty(agentPO.getTags())){
			//检查需要同步的标签
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getTags().split("\\|")));
			tempSet.remove("");//删掉空项
			updateTags.addAll(tempSet);
		}
		if(!StringUtil.isNullEmpty(agentPO.getExtraPartys())){
			//检查需要同步的通讯录部门
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraPartys().split("\\|")));
			tempSet.remove("");//删掉空项
			updateDepts.addAll(tempSet);
		}
		if(!StringUtil.isNullEmpty(agentPO.getExtraUserinfos())){
			//检查需要同步的通讯录用户
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraUserinfos().split("\\|")));
			tempSet.remove("");//删掉空项
			updateUsers.addAll(tempSet);
		}
		if(!StringUtil.isNullEmpty(agentPO.getExtraTags())){
			//检查需要同步的通讯录标签
			tempSet = new HashSet<String>(Arrays.asList(agentPO.getExtraTags().split("\\|")));
			tempSet.remove("");//删掉空项
			updateTags.addAll(tempSet);
		}
	}
	
	public static void insertTask(String corpId, String orgId, String suiteId, String agentCode) {
		String[] agentCodes = {agentCode};
		insertTask(corpId, orgId, suiteId, agentCodes);
	}
	
	public static void insertTask(String corpId, String orgId, String suiteId, String[] agentCodes) {
    	if(Configuration.IS_OLD_TRUST){
    		return;
    	}
		TbRunTaskVO runTask;
		try {
			runTask = runTaskService.getRunTaskByOnlyId(corpId, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
			if(null != runTask){//如果已在同步全公司
				return;
			}
			runTask = runTaskService.getRunTaskByOnlyId(corpId+"_"+suiteId, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
			if(null != runTask){//如果已在同步套件
				return;
			}
			if(agentCodes != null && agentCodes.length>0){
				for (String agentCode : agentCodes) {
					runTask = runTaskService.getRunTaskByOnlyId(corpId+"_"+agentCode, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
					//获取该orgId下未运行的任务
					if(null == runTask){//如果已经存在未执行的，则跳过，不保存任务
						TbRunTaskPO task = new TbRunTaskPO();
						task.setId(UUID.randomUUID().toString());
						task.setTaskType(TaskType.AddressbookSyncTask);
						task.setOrgId(orgId);
						task.setStatus(TaskType.TASK_STATUS_WAIT);
						task.setCreateTime(new Date());
						task.setParam("{\"agentCode\":\""+agentCode+"\"}");
						task.setAgentCode(agentCode);
						task.setCorpId(corpId);
						task.setOnlyId(corpId+"_"+agentCode);
						runTaskService.insertRunTask(task);
					}
				}
			}
		} catch (Exception e) {
        	logger.error("添加更新通讯录任务异常  orgId"+orgId,e);
			ExceptionCenter.addException(e,"AddressbookSyncTask insertTask @sqh", corpId+"_"+suiteId+"_"+agentCodes);
		}
	}
	
	public static void insertTaskForFirstRegister(String corpId, String orgId) {
		try {
			List<TbRunTaskPO> list = runTaskService.getRunTaskByCorpId(corpId, TaskStatus.TASK_FAILURE, TaskType.AddressbookSyncTask);
			//删除已有的更新数据
			if(list!=null && list.size()>0){
				TbRunTaskPO update = new TbRunTaskPO();
				//将所有错误的任务重新设置为待执行
				for (TbRunTaskPO po : list) {
					update.setId(po.getId());
					update.setStatus(TaskStatus.TASK_INIT);
					runTaskService.updatePO(update,false);
				}
				//QwtoolUtil.updateBatchList(list,false);
			}
		} catch (Exception e) {
			logger.error("添加更新通讯录任务异常  orgId"+orgId,e);
			ExceptionCenter.addException(e,"AddressbookSyncTask insertTaskForFirstRegister @sqh",corpId);
		}
	}

	/**
	 * 当接入完成后，同步应用人员
	 * @return 
	 * @author Sun Qinghai
	 * @ 16-5-31
	 */
	public static void insertTask(String corpId, String orgId) {
		insertUserTask(corpId, orgId, null, null);
	}

	public static void insertUserTask(String corpId, String orgId, String optUser, String syncPOId) {
		try {
			List<TbRunTaskVO> list = runTaskService.getRunTaskListByOrgId(orgId, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
			//删除已有的更新数据
			if(list!=null && list.size()>0){
				int siz = list.size();
				String[] ids = new String[siz];
				for (int i = 0; i < siz; i++) {
					ids[i] = list.get(i).getId();
				}
				runTaskService.batchDel(TbRunTaskPO.class, ids);
			}
			TbRunTaskPO task = new TbRunTaskPO();
			task.setId(UUID.randomUUID().toString());
			task.setTaskType(TaskType.AddressbookSyncTask);
			task.setOrgId(orgId);
			task.setStatus(TaskType.TASK_STATUS_WAIT);
			task.setCreateTime(new Date());
			task.setParam("{\"syncPOId\":"+(syncPOId==null ? "null" : "\""+syncPOId+"\"")+"}");
			task.setCorpId(corpId);
			task.setOnlyId(corpId);
			task.setCreatePerson(optUser);
			runTaskService.insertRunTask(task);
		} catch (Exception e) {
			logger.error("添加更新通讯录任务异常  orgId"+orgId,e);
			ExceptionCenter.addException(e, "添加更新通讯录任务异常 @sqh", corpId);
		} catch (BaseException e) {
			logger.error("添加更新通讯录任务异常  orgId"+orgId,e);
			ExceptionCenter.addException(e, "添加更新通讯录任务异常 @sqh", corpId);
		}
	}
	
	public static void insertTask(String corpId, String orgId, String suiteId) {
		TbRunTaskVO runTask;
		try {
			runTask = runTaskService.getRunTaskByOnlyId(corpId, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
			if(null != runTask){//如果已在同步全公司
				return;
			}
			runTask = runTaskService.getRunTaskByOnlyId(corpId+"_"+suiteId, TaskType.TASK_STATUS_WAIT, TaskType.AddressbookSyncTask);
			//获取该orgId下未运行的任务
			if(null == runTask){//如果已经存在未执行的，则跳过，不保存任务
				TbRunTaskPO task = new TbRunTaskPO();
				task.setId(UUID.randomUUID().toString());
				task.setTaskType(TaskType.AddressbookSyncTask);
				task.setOrgId(orgId);
				task.setStatus(TaskType.TASK_STATUS_WAIT);
				task.setCreateTime(new Date());
				task.setParam("{\"suiteId\":\""+suiteId+"\"}");
				task.setCorpId(corpId);
				task.setOnlyId(corpId+"_"+suiteId);
				runTaskService.insertRunTask(task);
			}
		} catch (Exception e) {
        	logger.error("添加更新通讯录任务异常  orgId"+orgId,e);
			ExceptionCenter.addException(e, "添加更新通讯录任务异常 @sqh", corpId+"_"+suiteId);
		}
	}
	/* （非 Javadoc）
	 * @see cn.com.do1.component.qwinterface.runtask.IRunTask#getType()
	 */
	@Override
	public String getType() {
		return TaskType.AddressbookSyncTask;
	}
}
