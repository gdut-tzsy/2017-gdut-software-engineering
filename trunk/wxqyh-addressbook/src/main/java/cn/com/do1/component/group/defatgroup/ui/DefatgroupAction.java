package cn.com.do1.component.group.defatgroup.ui;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPersonPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserGroupPersonVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;

import cn.com.do1.component.util.WxqyhBaseAction;
import org.apache.struts2.ServletActionContext;
import cn.com.do1.common.exception.BaseException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class DefatgroupAction extends WxqyhBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(DefatgroupAction.class);
    private IDefatgroupService defatgroupService;
    private IDefatgroupMgrService defatgroupMgrService;
    private TbQyUserGroupPO tbQyUserGroupPO;
    private TbQyUserGroupPersonPO tbQyUserGroupPersonPO;
    private IContactService contactService;
    private String ids[];
    private String id;
    private String userIds;

    public IDefatgroupService getDefatgroupService() {
        return defatgroupService;
    }

    @Resource(name = "defatgroupService")
    public void setDefatgroupService(IDefatgroupService defatgroupService) {
        this.defatgroupService = defatgroupService;
    }
    @Resource(name = "defatgroupService")
    public void setDefatgroupMgrService(IDefatgroupMgrService defatgroupMgrService) {
        this.defatgroupMgrService = defatgroupMgrService;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/**
* 列表查询时，页面要传递的参数
*/
    @SearchValueTypes(
        nameFormat="false",value={
        @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
        @SearchValueType(name = "testNumber", type = "number"),
        @SearchValueType(name = "title", type="string", format = "%%%s%%")
    })
@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException{
    	Pager pager = new Pager( ServletActionContext.getRequest(), getPageSize());
        pager=defatgroupMgrService.searchDefatgroup(getSearchValue(),pager);
        addJsonPager("pageData",pager);
		
    }
    /**
     * 后台查询列表
     * @throws Exception
     * @throws BaseException
     * @author Chen Feixiong
     * 2014-11-25
     */
    @SearchValueTypes(
			nameFormat="false",value={
					@SearchValueType(name = "title", type="string", format = "%%%s%%")
			})
@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxPageSearch() throws Exception, BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userId = user.getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
		if(userInfoVO == null){
	    	logger.error("获取登录人"+userId+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		HttpServletRequest request=ServletActionContext.getRequest();
		Pager pager = new Pager( ServletActionContext.getRequest(), getPageSize());
        Map<String, Object> searchMap = getSearchValue();
        searchMap.put("orgId", userInfoVO.getOrgId());
        pager=defatgroupMgrService.ajaxPageSearch(searchMap,pager);
        addJsonFormatePager("pageData",pager);
		
    }
    /**
     * 公共群组列表
     * @throws Exception
     * @throws BaseException
     * @author Hejinjiao
     * @2014-12-24
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getGroupList() throws Exception, BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userId = user.getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
		if(userInfoVO == null){
	    	logger.error("获取登录人"+userId+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		
        List<TbQyUserGroupPO> list =defatgroupMgrService.getGroupList(userInfoVO.getOrgId()) ;
        addJsonArray("groupList", list);
    }
    /**
     * 分页查询群组人
     * @throws Exception
     * @throws BaseException
     * @author Hejinjiao
     * @2014-12-31
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void pageGroupUsers() throws Exception, BaseException{
    	 Pager pager = new Pager( ServletActionContext.getRequest(), getPageSize());
    	String user = DqdpAppContext.getCurrentUser().getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(user);
		if (userInfoVO == null) {
			logger.error("获取登录人"+user+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		if (AssertUtil.isEmpty(id)) {
			 List<TbQyUserGroupPO> groupList =defatgroupMgrService.getGroupList(userInfoVO.getOrgId()) ;
			 if (groupList.size()==0) {
				 logger.error("没有设置公共群组");
				 addJsonFormatePager("pageData",pager);
				 return;
			}
			 id=groupList.get(0).getGroupId();
		}
	      Map<String, Object> searchMap = getSearchValue();
	      searchMap.put("id", id);
	      pager=defatgroupMgrService.pageGroupUsers(searchMap,pager);
	      addJsonFormatePager("pageData",pager);
    }
    /**
     * 查询已选择群组的人
     * @throws Exception
     * @throws BaseException
     * @author Hejinjiao
     * @2015-1-4
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void chooseUserByGroups() throws Exception, BaseException{
    	if (AssertUtil.isEmpty(id)) {
			setActionResult("1001","没有选择群组");
			return;
		}
    	List<TbQyUserGroupPersonVO> personList=defatgroupMgrService.getUserByGroups(id);
    	addJsonArray("persons", personList);
    }
    
/**
 * 后台新增数据
 * @throws Exception
 * @throws BaseException
 * @author Chen Feixiong
 * 2014-11-25
 */
@JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void ajaxAdd() throws Exception, BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userId = user.getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
		if(userInfoVO == null){
	    	logger.error("获取登录人"+userId+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		tbQyUserGroupPO.setGroupId(UUID.randomUUID().toString());
		tbQyUserGroupPO.setCreateTime(new Date());
		tbQyUserGroupPO.setCreator(userId);
		tbQyUserGroupPO.setStatus("0");
		tbQyUserGroupPO.setOrgId(userInfoVO.getOrgId());
		if(AssertUtil.isEmpty(userIds)){
			logger.error("登录人"+userId+"创建的"+tbQyUserGroupPO.getGroupName()+"群组人员为空！");
			throw new BaseException("群组人员为空！");
		}
		//判断群众是否重名
		List<TbQyUserGroupPO> list=defatgroupMgrService.findDefatgroup(tbQyUserGroupPO.getGroupName(),userInfoVO.getOrgId());
		if (list.size()==0) {
			defatgroupMgrService.ajaxAdd(userInfoVO, tbQyUserGroupPO, userIds);
		}else {
			throw new BaseException("该群组名称已存在，请使用其它群组名称重试。");
		}
    }
/**
 * 修改数据
 * @throws Exception
 * @throws BaseException
 * @author Chen Feixiong
 * 2014-11-25
 */
@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void ajaxUpdate() throws Exception, BaseException{
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		String userId = user.getUsername();
		UserOrgVO userInfoVO = contactService.getOrgByUserId(userId);
		if(userInfoVO == null){
	    	logger.error("获取登录人"+userId+"orgId的信息为空");
			throw new BaseException("登录人的信息为空");
		}
		tbQyUserGroupPO.setCreator(userId);
		tbQyUserGroupPO.setCreateTime(new Date());
		tbQyUserGroupPO.setStatus("0");
		tbQyUserGroupPO.setOrgId(userInfoVO.getOrgId());
		if(AssertUtil.isEmpty(userIds)){
			logger.error("登录人"+userId+"创建的"+tbQyUserGroupPO.getGroupName()+"群组人员为空！");
			throw new BaseException("群组人员为空！");
		}
		TbQyUserGroupPO po=defatgroupService.searchByPk(TbQyUserGroupPO.class, tbQyUserGroupPO.getGroupId());
		if (po!=null) {
			if (!po.getGroupName().equals(tbQyUserGroupPO.getGroupName())) {
				List<TbQyUserGroupPO> list=defatgroupMgrService.findDefatgroup(tbQyUserGroupPO.getGroupName(),userInfoVO.getOrgId());
				if (list.size()!=0) {
					throw new BaseException("该群组名称已存在，请使用其它群组名称重试。");
				}
			}
		}
		defatgroupMgrService.ajaxUpdate(userInfoVO, tbQyUserGroupPO, userIds);
    }
/**
 * 修改群组状态
 * @throws Exception
 * @throws BaseException
 * @author Chen Feixiong
 * 2014-11-25
 */
@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
	public void updateStatus() throws Exception, BaseException{
		String id = this.getReqeustObj().getParameter("id");
		String status = this.getReqeustObj().getParameter("status");
		defatgroupMgrService.updateStatus(id,status);
	}
/**
 * 删除群组
 * @throws Exception
 * @throws BaseException
 * @author Chen Feixiong
 * 2014-11-25
 */
@JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete() throws Exception, BaseException{
		if(ids != null  && ids.length>0){
			defatgroupMgrService.ajaxBatchDelete(getUser(), ids);
		}
    }

   public void setTbQyUserGroupPO(TbQyUserGroupPO tbQyUserGroupPO){
       this.tbQyUserGroupPO=tbQyUserGroupPO;
    }
   public TbQyUserGroupPO setTbQyUserGroupPO(){
       return this.tbQyUserGroupPO;
    }
@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxView() throws Exception, BaseException{
            TbQyUserGroupPO po = defatgroupService.searchByPk(TbQyUserGroupPO.class, id);
            addJsonFormateObj("tbQyUserGroupPO", po);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
            List<TbQyUserGroupPersonVO> personList=defatgroupMgrService.getUserGroupPerson(id);
            /*String temp="";
            List<String> personNameList=new ArrayList<String>();
            for(TbQyUserGroupPersonVO personVO:personList){
            	temp+=personVO.getUserId()+"|";
            	personNameList.add(personVO.getPersonName());
            }*/
            /*addJsonObj("userIds",temp);
            addJsonObj("personNameList",personNameList);*/
            addJsonArray("persons", personList);
    }
    public TbQyUserGroupPO getTbQyUserGroupPO() {
        return this.tbQyUserGroupPO;
    }

	public TbQyUserGroupPersonPO getTbQyUserGroupPersonPO() {
		return tbQyUserGroupPersonPO;
	}

	public void setTbQyUserGroupPersonPO(TbQyUserGroupPersonPO tbQyUserGroupPersonPO) {
		this.tbQyUserGroupPersonPO = tbQyUserGroupPersonPO;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public IContactService getContactService() {
		return contactService;
	}
    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
}
