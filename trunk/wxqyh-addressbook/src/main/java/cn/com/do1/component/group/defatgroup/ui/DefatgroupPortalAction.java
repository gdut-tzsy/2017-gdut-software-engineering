package cn.com.do1.component.group.defatgroup.ui;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.util.string.StringUtil;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPersonPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService;
import cn.com.do1.component.defatgroup.defatgroup.vo.TbQyOldGivenVO;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;
import cn.com.do1.component.qiweipublicity.experienceapplication.vo.QyAgentUserRefVO;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;

public class DefatgroupPortalAction extends BaseAction{
	private final static transient Logger logger = LoggerFactory.getLogger(DefatgroupAction.class);

	private TbQyUserGroupPO tbQyUserGroupPO;
	private TbQyUserGroupPersonPO tbQyUserGroupPersonPO;
	private String ids[];
	private String id;
	
    private IDefatgroupService defatgroupService;
    private IDefatgroupMgrService defatgroupMgrService;
    private IContactService contactService;
    
    @Resource(name = "defatgroupService")
    public void setDefatgroupService(IDefatgroupService defatgroupService) {
        this.defatgroupService = defatgroupService;
    }
    
    /**
	 * @param defatgroupMgrService 要设置的 defatgroupMgrService
	 */
    @Resource(name = "defatgroupService")
	public void setDefatgroupMgrService(IDefatgroupMgrService defatgroupMgrService) {
		this.defatgroupMgrService = defatgroupMgrService;
	}
	public IContactService getContactService() {
		return contactService;
	}
    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    
    /**
	 * @author lishengtao
	 * 2015-10-27
	 * 加载上一次的相关人和负责人
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询成功", faileMsg = "查询失败"))
	public void findTbQyOldGivenVOList() throws Exception, BaseException{
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String type = request.getParameter("type");//类型:0-负责人;1-相关人
		if(!"1".equals(type) && !"0".equals(type)){
			setActionResult("1001","类型超出范围");
			return;
		}
		String applyType = request.getParameter("applyType");
		String childApplyType = request.getParameter("childApplyType");
		if(AssertUtil.isEmpty(childApplyType)){
			childApplyType="";
		}
		String agentCode = request.getParameter("agentCode");
		List<TbQyOldGivenVO> oldGivenList;
		//验证可见范围
		if(!StringUtil.isNullEmpty(agentCode) && !VisibleRangeUtil.isAllVisibleRange(userInfo.getCorpId(), userInfo.getOrgId(), agentCode)){
			Set<String> users = VisibleRangeUtil.getVisibleRangeUserList(userInfo.getOrgId(), agentCode);
			if(users == null || users.size() == 0){
				oldGivenList = new ArrayList<TbQyOldGivenVO>(0);
			}
			else{
				oldGivenList = defatgroupService.findTbQyOldGivenVOByUserId(userInfo.getUserId(), userInfo.getOrgId(), type,applyType,null);
				List<TbQyOldGivenVO> newList = new ArrayList<TbQyOldGivenVO>();
				for (TbQyOldGivenVO moveapp : oldGivenList) {
					if(!AssertUtil.isEmpty(moveapp.getUserId()) && users.contains(moveapp.getUserId())){
						newList.add(moveapp);
					}
				}
				oldGivenList = newList;
			}
		}
		else{
			oldGivenList = defatgroupService.findTbQyOldGivenVOByUserId(userInfo.getUserId(), userInfo.getOrgId(), type,applyType,null);
		}
		addJsonObj("ccOrTolist", oldGivenList);
	}
	/**
	 * 获取常用联系人群组
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserGroup()throws Exception,BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = WxqyhAppContext.getCurrentUser(request).getUserId();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("user_id", userId);
		
		Pager pager= new Pager(request,100);
		
/*		List<TbQyUserGroupPO> list = contactService.getUserGroup(map);			
		pager.doPager(list);*/
		
		/**
		 * 2015-2-12 李盛滔
		 * 修改doPager
		 */
		pager=defatgroupMgrService.getUserGroup(pager, map);
		
		addJsonPager("pageData",pager);
		
		//添加公共群组 chenfeixiong 2014/11/25
		String isPerson=request.getParameter("isPerson");		//个人群组列表不显示默认群组
		if(!"1".equals(isPerson)){
			TbQyUserInfoVO vo=contactService.findUserInfoByUserId(userId);
			List<TbQyUserGroupPO> list2 = defatgroupMgrService.getUserGroup(vo.getOrgId());
			addJsonObj("publicList",list2);
		}
	}
	/**
	 * 获取常用联系人群组人员
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserGroupPerson()throws Exception,BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = WxqyhAppContext.getCurrentUser(request).getUserId();
		String groupId=request.getParameter("groupId");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("user_id", userId);
		map.put("groupId", groupId);
		Pager pager= new Pager(request,100);
		
/*		List<TbQyUserGroupPersonVO> list = contactService.getUserGroupPerson(map);
		pager.doPager(list);*/
		
		/**
		 * 20150212 李盛滔
		 * 修改doPager
		 */
		pager=defatgroupMgrService.getUserGroupPerson(pager, map);
		
		addJsonPager("pageData",pager);
		TbQyUserGroupPO po=contactService.searchByPk(TbQyUserGroupPO.class, groupId);
		addJsonFormateObj("TbQyUserGroupPO", po);
	}
	/**
	 * 添加常用联系人群组
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void addUserGroup()throws Exception,BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = WxqyhAppContext.getCurrentUser(request).getUserId();
		String name = request.getParameter("name");
		//检测是否存在该群组名称
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("user_id", userId);
		map.put("group_name", name);
		List<TbQyUserGroupPO> list = defatgroupMgrService.getUserGroup(map);
		if(list.size()>0){
			setActionResult("1", "已经存在该群组");
			return ;
		}
		//添加默认负责人群组 chenfeixiong 2014/11/14
		String isCharge=request.getParameter("isCharge");
		String isRec=request.getParameter("isRec");
		if("0".equals(isCharge)){
			defatgroupMgrService.updateGroupByUserID(userId,"1");
		}
		if("0".equals(isRec)){
			defatgroupMgrService.updateGroupByUserID(userId,"2");
		}
		tbQyUserGroupPO=new TbQyUserGroupPO();
		tbQyUserGroupPO.setGroupName(name);
		tbQyUserGroupPO.setCreator(userId);
		tbQyUserGroupPO.setCreateTime(new Date());
		tbQyUserGroupPO.setExt1(isCharge);
		tbQyUserGroupPO.setExt2(isRec);
		contactService.insertPO(tbQyUserGroupPO, true);
		//添加群组人员
		String userids = request.getParameter("userids");
		String strtemp[]=null;
		if(userids.indexOf(",")>0){
			strtemp=userids.split("\\,");
		}else{
			strtemp=new String[1];
			strtemp[0]=userids;
		}
		for(String str: strtemp){
			tbQyUserGroupPersonPO = new TbQyUserGroupPersonPO();
			tbQyUserGroupPersonPO.setGroupId(tbQyUserGroupPO.getGroupId());
			tbQyUserGroupPersonPO.setCreator(userId);
			tbQyUserGroupPersonPO.setCreateTime(new Date());
			tbQyUserGroupPersonPO.setUserId(str);
			contactService.insertPO(tbQyUserGroupPersonPO, true);
		}
		setActionResult("0", "新增成功");
	}
	/**
	 * 添加常用联系人群组人员
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-22
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void addUserGroupPerson()throws Exception,BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = WxqyhAppContext.getCurrentUser(request).getUserId();
		String userids = request.getParameter("userids");
		String groupid = request.getParameter("groupid");
		//去除重复人员
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("user_id", userId);
		map.put("groupId", groupid);
		TbQyUserGroupPersonPO tbQyUserGroupPersonPO=null;
		List<TbQyUserGroupPersonVO> list = defatgroupMgrService.getUserGroupPerson(map);
		StringBuilder sb=new StringBuilder("");
		for(TbQyUserGroupPersonVO vo : list){
			sb.append(vo.getUserId());
		}
		String strtemp[]=null;
		
		if(userids.indexOf(",")>0){
			strtemp=userids.split("\\,");
		}else{
			strtemp=new String[1];
			strtemp[0]=userids;
		}
		for(String str: strtemp){
				if(sb.indexOf(str.trim())<0){
					tbQyUserGroupPersonPO = new TbQyUserGroupPersonPO();
					tbQyUserGroupPersonPO.setGroupId(groupid);
					tbQyUserGroupPersonPO.setCreator(userId);
					tbQyUserGroupPersonPO.setCreateTime(new Date());
					tbQyUserGroupPersonPO.setUserId(str);
					contactService.insertPO(tbQyUserGroupPersonPO, true);
				}
		}
		setActionResult("0", "新增成功");
	}
	/**
	 * 更新群组名称
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-21
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "更新成功", faileMsg = "更新失败"))
	public void updateUserGroup()throws Exception,BaseException{
		HttpServletRequest request=ServletActionContext.getRequest();
		String userId = WxqyhAppContext.getCurrentUser(request).getUserId();
		//添加默认负责人群组 chenfeixiong 2014/11/14
		String isCharge=request.getParameter("isCharge");
		String isRec=request.getParameter("isRec");
		if("0".equals(isCharge)){
			defatgroupMgrService.updateGroupByUserID(userId,"1");
		}
		if("0".equals(isRec)){
			defatgroupMgrService.updateGroupByUserID(userId,"2");
		}
		String name = request.getParameter("name");
		String groupid = request.getParameter("groupid");
		tbQyUserGroupPO=contactService.searchByPk(TbQyUserGroupPO.class, groupid);

		//检测修改的群组是否包含管理员创建的群组	个人不允许修改	chenfeixiong 2014/11/25
		if(!tbQyUserGroupPO.getCreator().equals(userId)){
			throw new BaseException("不可以修改默认群组！");
		}
		tbQyUserGroupPO.setGroupName(name);
		tbQyUserGroupPO.setExt1(isCharge);
		tbQyUserGroupPO.setExt2(isRec);
		defatgroupMgrService.updatePO(tbQyUserGroupPO, false);
		//删除旧数据
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("user_id", userId);
		map.put("groupId", groupid);
		TbQyUserGroupPersonPO tbQyUserGroupPersonPO=null;
		List<TbQyUserGroupPersonVO> list = defatgroupMgrService.getUserGroupPerson(map);
		String personids[]=new String[list.size()];
		int x=0;
		for(TbQyUserGroupPersonVO vo:list){
			personids[x]=vo.getId();
			x++;
		}
		defatgroupMgrService.batchDel(TbQyUserGroupPersonPO.class, personids);
		//添加群组人员
		String userids = request.getParameter("userids");
		String strtemp[]=null;
		if(userids.indexOf(",")>0){
			strtemp=userids.split("\\,");
		}else{
			strtemp=new String[1];
			strtemp[0]=userids;
		}
		for(String str: strtemp){
			tbQyUserGroupPersonPO = new TbQyUserGroupPersonPO();
			tbQyUserGroupPersonPO.setGroupId(tbQyUserGroupPO.getGroupId());
			tbQyUserGroupPersonPO.setCreator(userId);
			tbQyUserGroupPersonPO.setCreateTime(new Date());
			tbQyUserGroupPersonPO.setUserId(str);
			contactService.insertPO(tbQyUserGroupPersonPO, true);
		}
	}
	/**
	 * 删除常用联系人群组
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void BatchDeleteGroup()throws Exception,BaseException{
		String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();
		boolean isTrue=false;	//检测删除的群组是否包含管理员创建的群组	个人不允许删除
		for(String str:ids){
			TbQyUserGroupPO po=contactService.searchByPk(TbQyUserGroupPO.class, str);
			if(!AssertUtil.isEmpty(po) && !po.getCreator().equals(userId)){
				isTrue=true;
			}
		}
		if(isTrue){
			setActionResult("3","不可以删除默认群组！");
			return;
		}
		if(ids.length==1){
			defatgroupMgrService.batchDeleteGroup(ids);		//删除常用联系人群组的联系人
			tbQyUserGroupPO=contactService.searchByPk(TbQyUserGroupPO.class, ids[0]);
			this.ajaxDelete(tbQyUserGroupPO);
		}else{
			defatgroupMgrService.batchDeleteGroup(ids);		//删除常用联系人群组的联系人
			this.ajaxBatchDelete(TbQyUserGroupPO.class, ids);
		}
	}
	/**
	 * 删除常用联系人群组人员
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-20
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void BatchDeleteGroupPerson()throws Exception,BaseException{
		WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		this.ajaxBatchDelete(TbQyUserGroupPersonPO.class, ids);
	}
	/**
	 * @return tbQyUserGroupPO
	 */
	public TbQyUserGroupPO getTbQyUserGroupPO() {
		return tbQyUserGroupPO;
	}
	/**
	 * @param tbQyUserGroupPO 要设置的 tbQyUserGroupPO
	 */
	public void setTbQyUserGroupPO(TbQyUserGroupPO tbQyUserGroupPO) {
		this.tbQyUserGroupPO = tbQyUserGroupPO;
	}
	/**
	 * @return tbQyUserGroupPersonPO
	 */
	public TbQyUserGroupPersonPO getTbQyUserGroupPersonPO() {
		return tbQyUserGroupPersonPO;
	}
	/**
	 * @param tbQyUserGroupPersonPO 要设置的 tbQyUserGroupPersonPO
	 */
	public void setTbQyUserGroupPersonPO(TbQyUserGroupPersonPO tbQyUserGroupPersonPO) {
		this.tbQyUserGroupPersonPO = tbQyUserGroupPersonPO;
	}
	/**
	 * @return ids
	 */
	public String[] getIds() {
		return ids;
	}
	/**
	 * @param ids 要设置的 ids
	 */
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id 要设置的 id
	 */
	public void setId(String id) {
		this.id = id;
	}
}
