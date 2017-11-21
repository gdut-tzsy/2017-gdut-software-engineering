package cn.com.do1.component.contact.contact.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyTargetUserPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.service.ITargetService;
import cn.com.do1.component.addressbook.contact.vo.TargetUserGroupVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserRedundancyInfoVO;
import cn.com.do1.component.contact.contact.dao.ITargetDAO;

/**
 * 
 * @author lishengtao
 * @date 2016-1-6
 */
@Service("targetService")
public class TargetServiceImpl extends BaseService implements ITargetService{
	private ITargetDAO targetDAO;
	private IContactService contactService;
	
	@Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	@Resource
	public void setTargetDAO(ITargetDAO targetDAO) {
		this.targetDAO = targetDAO;
		setDAO(targetDAO);
	}
	
	/**
	 * 获取组中的成员，并转化成map
	 * @param groupId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * lishengtao
	 * 2016-1-7
	 */
	private Map<String,TbQyTargetUserPO> convertTargetUserListToMap(List<TbQyTargetUserPO> list)throws Exception,BaseException{
		Map<String,TbQyTargetUserPO> map=new HashMap<String,TbQyTargetUserPO>();
		for(TbQyTargetUserPO po:list){
			if(AssertUtil.isEmpty(map.get(po.getUserId()))){
				map.put(po.getUserId(), po);
			}
		}
		return map;
	}

	@Override
	public void saveTargetUser(String groupId, String userIds, String strSplit,
			String orgId, String agentCode, String creator) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		if(!AssertUtil.isEmpty(groupId) && userIds.length()>0){
			targetDAO.delTargetUserByGroupId(groupId, 0);//先删除以前没有操作过的数据
			List<TbQyTargetUserPO> list=targetDAO.getTargetUserPOByGroupId(groupId); //获取有状态的目标数据：删除了状态为0的数据，只剩下有状态的数据
			Map<String,TbQyTargetUserPO> map=convertTargetUserListToMap(list);//转化为map
			
			List<TbQyTargetUserPO> insertList=new ArrayList<TbQyTargetUserPO>();//新插入的数据列表
			List<TbQyTargetUserPO> updateList=new ArrayList<TbQyTargetUserPO>();//需要编辑的数据列表
			List<TbQyTargetUserPO> noTargetMapList=new ArrayList<TbQyTargetUserPO>();//不是目标对象的Map中值
			Set<String> userInMapSet=new HashSet<String>();//记录在map中的目标对象的userId
			Set<String> userIdSet=new HashSet<String>();//判断是否会有重复目标对象的userId
			
			TbQyTargetUserPO targetUserPO;
			if("|".equals(strSplit)){
				strSplit="\\|";
			}
			String userArray[]=userIds.split(strSplit);
			int sortNum=1;
			for(String userId:userArray){
				if(StringUtil.isNullEmpty(userId)){
					continue;
				}
				//判断是否有重复，重复的直接跳过
				if(userIdSet.contains(userId)){
					continue;
				}
				//没有重复的继续判断
				userIdSet.add(userId);
				if(AssertUtil.isEmpty(map.get(userId))){
					//如果为空的话，插入
					targetUserPO=new TbQyTargetUserPO();
					targetUserPO.setId(UUID.randomUUID().toString());
					targetUserPO.setGroupId(groupId);
					targetUserPO.setUserId(userId);
					targetUserPO.setSortNum(sortNum);
					targetUserPO.setOrgId(orgId);
					targetUserPO.setAgentCode(agentCode);
					targetUserPO.setCreator(creator);
					targetUserPO.setCreateTime(new Date());
					targetUserPO.setUpdateTime(new Date());
					targetUserPO.setStatus(0);
					
					//先不处理冗余字段：这样处理太慢，可能改成1次搜索出来再设置
					
					//设置冗余字段
/*					UserRedundancyInfoVO userRedundancyInfoVO=this.contactService.getUserRedundancyInfoByUserId(userId);
					targetUserPO.setPersonName(userRedundancyInfoVO.getPersonName());
					targetUserPO.setHeadPic(userRedundancyInfoVO.getHeadPic());
					targetUserPO.setDepartmentId(userRedundancyInfoVO.getDeptId());
					targetUserPO.setDepartmentName(userRedundancyInfoVO.getDeptFullName());
					targetUserPO.setWxUserId(userRedundancyInfoVO.getWxUserId());*/
					
					//插入到list中
					insertList.add(targetUserPO);
				}else{
					userInMapSet.add(userId);
					targetUserPO=map.get(userId);
					targetUserPO.setUpdateTime(new Date());
					targetUserPO.setSortNum(sortNum);
					updateList.add(targetUserPO);
				}
				sortNum=sortNum+1;
			}
			if(null!=insertList && insertList.size()>0){
				targetDAO.execBatchInsert(insertList);//批量插入新数据
			}
			if(null!=updateList && updateList.size()>0){
				targetDAO.execBatchUpdate(updateList);//批量更新旧数据
			}
			
			//修改不属于目标对象而又不能编辑的数据：再次编辑的时候会出现
			for (Map.Entry<String,TbQyTargetUserPO> entry : map.entrySet()) {
				if(!userInMapSet.contains(entry.getKey())){
					targetUserPO=entry.getValue();
					targetUserPO.setUpdateTime(new Date());
					targetUserPO.setSortNum(sortNum);
					noTargetMapList.add(targetUserPO);
					sortNum=sortNum+1;
				}
			}
			if(null!=noTargetMapList && noTargetMapList.size()>0){
				targetDAO.execBatchUpdate(noTargetMapList);//批量更新旧map中的排序号
			}
		}
	}

	@Override
	public void saveTargetUser(String groupId,List<TbQyUserInfoVO> userList, String orgId,
			String agentCode, String creator) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		StringBuilder userIds=new StringBuilder("");
		TbQyUserInfoVO vo;
		if(!AssertUtil.isEmpty(userList)){
			for(int i=0;i<userList.size();i++){
				vo=userList.get(i);
				if(0==i){
					userIds.append(vo.getUserId());
				}else{
					userIds.append("|"+vo.getUserId());
				}
			}
			saveTargetUser(groupId, userIds.toString(), "|", orgId, agentCode, creator);
		}
	}
	
	@Override
	public void saveTargetUser(String groupId, String range, String userIds,
			String deptIds, String orgId, String agentCode, String creator)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("range", range);
		params.put("deptIds", deptIds);
		params.put("userIds", userIds);
		params.put("orgId", orgId);
		List<TbQyUserInfoVO> userList=contactService.getUserInfoByRange(params);
		saveTargetUser(groupId, userList, orgId, agentCode, creator);
	}
	
	@Override
	public void saveTargetUser(Map<String, Object> params) throws Exception,
			BaseException {
		
		String orgId=(String) params.get("orgId");
		String agentCode=(String) params.get("agentCode");
		String creator=(String) params.get("creator");
		String groupId=(String) params.get("groupId");
		List<TbQyUserInfoVO> userList=contactService.getUserInfoByRange(params);
		saveTargetUser(groupId, userList, orgId, agentCode, creator);
		
	}

	@Override
	public void updateTargetUserStatus(String userId, String groupId,int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		TbQyTargetUserPO po=targetDAO.getTargetUserPO(groupId, userId);
		if(null!=po){
			po.setStatus(status);
			po.setUpdateTime(new Date());
			targetDAO.update(po, true);
		}
	}

	@Override
	public List<TbQyUserInfoVO> getTargetUserList(Map<String, Object> paramMap)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		if((!AssertUtil.isEmpty(paramMap.get("status")) ||
				(!AssertUtil.isEmpty(paramMap.get("status")) && !"0".equals(paramMap.get("status"))))){
			//如果状态类型为空或者状态类型等于0(从来未操作过),需要做离职判断
			paramMap.put("leaveStatus", "-1");
		}
		return targetDAO.getTargetUserList(paramMap);
	}

	@Override
	public Pager getTargetUserPager(Map<String, Object> paramMap, Pager pager)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		if((AssertUtil.isEmpty(paramMap.get("status")) ||
				(!AssertUtil.isEmpty(paramMap.get("status")) && !"0".equals(paramMap.get("status"))))){
			//如果状态类型为空或者状态类型等于0(重来未操作过),需要做离职判断
			paramMap.put("leaveStatus", "-1");
		}
		return targetDAO.getTargetUserPager(paramMap, pager);
	}

	@Override
	public void updateTargetUserStatus(String groupId, String[] userIdArray, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		targetDAO.updateTargetUserStatus(groupId, userIdArray, status);
	}

	@Override
	public void delTargetUser(String groupId, String userId, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		targetDAO.delTargetUser(groupId, userId, status);
	}

	@Override
	public void delTargetUser(String groupId, String[] userIdArray, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		targetDAO.delTargetUser(groupId, userIdArray, status);
	}

	@Override
	public List<TargetUserGroupVO> getTargetUserGroupVOList(
			Map<String, Object> paramMap) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return targetDAO.getTargetUserGroupVOList(paramMap);
	}

	@Override
	public void cleanTargetUser(String groupId, String agentCode)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		targetDAO.delTargetUserByGroupId(groupId, 0);//删除数据
		targetDAO.updateBuildUnreadStatus(groupId, agentCode, 0);
	}

	@Override
	public void delTargetUserByGroupId(String groupId, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		targetDAO.delTargetUserByGroupId(groupId, status);
	}
	
}
