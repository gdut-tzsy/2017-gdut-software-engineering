package cn.com.do1.component.contact.contact.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.ContactCustomUtil;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbAddressbookGuessbookPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCommonPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.defatgroup.defatgroup.service.IDefatgroupService;
import cn.com.do1.component.errcodedictionary.ErrorTip;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.component.wxcgiutil.im.ChatManager;
import cn.com.do1.component.wxcgiutil.im.MsgType;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;

public class PortalContactAction extends WxqyhPortalBaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(ContactAction.class);
	private IContactService contactService;
	private IContactMgrService contactMgrService;
    private IDepartmentMgrService departmentMgrService;
    private IDefatgroupService defatgroupService;
	private IContactCustomMgrService contactCustomMgrService;
	private TbQyUserInfoPO tbQyUserInfoPO;
	private String ids[];
	private String id;
	private String keyWord; // 搜索关键字：支持姓名&手机号
	private String userId;
	private Integer size;
	private String incharges; //添加常用群组人员

	public String getIncharges() {
		return incharges;
	}

	public void setIncharges(String incharges) {
		this.incharges = incharges;
	}

	public IContactService getContactService() {
		return contactService;
	}

    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    @Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}
	/**
	 * @param departmentMgrService 要设置的 departmentMgrService
	 */
    @Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}
    @Resource(name = "defatgroupService")
    public void setDefatgroupService(IDefatgroupService defatgroupService) {
        this.defatgroupService = defatgroupService;
    }
	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
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
	 * 根据机构ID获取该机构下的所有用户
	 * 
	 * @throws Exception
	 */
	public void ajaxGetUserListByOrgID() throws Exception {
		try {
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			HttpServletRequest request = ServletActionContext.getRequest();
			Map<String,Object> params=new HashMap<String,Object>();
			String sortTop=request.getParameter("sortTop");
			if(!AssertUtil.isEmpty(sortTop)){
				params.put("sortTop", sortTop);
			}
			pager = contactMgrService.findAlluserByUser(pager,user,params);
			addJsonPager("pageData", pager);
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
			setActionResult("0", "查询成功");
			//if(WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
				addJsonObj("showMsgBtn", true);
			//}
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	/**
	 * 获取所有用户
	 * 
	 * @throws Exception
	 */
	/*public void ajaxGetUserList() throws Exception {

		try {
//			TbQyUserInfoVO user = contactService.findUserInfoByUserId(userId);
//			String orgId = user.getOrgId();
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			Map<String,String> map = new HashMap<String,String>();
			//map.put("orgId", orgId);
			pager = contactService.searchContact(map, pager);
			addJsonPager("pageData", pager);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}*/

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @throws Exception
	 */
	public void ajaxGetUserInfoByID() throws Exception {
		try {
			//检测查看的用户是否是所发布的机构 	chenfeixiong 20150429
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			UserDeptInfoVO userInfo = contactMgrService.findUserDeptInfoByUserId(userId);
			DqdpOrgVO org = contactMgrService.getOrgByOrgId(user.getOrgId());
			addJsonObj("org", org);
			if(userInfo ==null || !userInfo.getOrgId().equals(user.getOrgId())){
    			setActionResult("1999", "非本机构人员不能访问本页面！");
    			doJsonOut();
                return;
    		}
			userInfo = SecrecyUserUtil.secrecyUserDept(userInfo);
			addJsonObj("userInfo", userInfo);
			if(!AssertUtil.isEmpty(userInfo) && !AssertUtil.isEmpty(userInfo.getLeaveMessage()) && userInfo.getLeaveMessage().indexOf("0") > -1){
				addJsonObj("leaveMessage", false);
			}else{
				addJsonObj("leaveMessage", true);
			}
			//lrl 2016-1-21 是否有开通消息服务 
			if(WxqyhAppContext.isVerified(user.getOrgId()) && WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
				addJsonObj("showMsgBtn", true);
			}
			//maquanyang 2015-6-17 新增查询设置通讯录字段是否显示
			List<TbQyFieldSettingVO> fieldList = this.contactService.findTbQyFieldSettingVOListByOrgId(userInfo.getOrgId());
			addJsonArray("fieldList", fieldList);
			List<TbQyUserCustomOptionVO> optionVOs;
			if(VipUtil.isQwVip(userInfo.getOrgId())) {
				optionVOs = contactCustomMgrService.getUserItemList(userInfo.getUserId(), userInfo.getOrgId());

			}else{
				optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
			}
			addJsonObj("optionVOs", optionVOs);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	
	/**
	 * 查询at人信息
	 * @throws Exception
	 * @author Hejinjiao
	 * @2016-6-8
	 * @version 1.0
	 */
	public void ajaxGetAtuserInfoByID() throws Exception {
		try {
			//检测查看的用户是否是所发布的机构 	chenfeixiong 20150429
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			UserDeptInfoVO userInfo = contactMgrService.findUserDeptInfoByUserId(userId);
			if(userInfo ==null || !userInfo.getOrgId().equals(user.getOrgId())){
    			setActionResult("1999", "非本机构人员不能访问本页面！");
    			doJsonOut();
                return;
    		}
			if (!VisibleRangeUtil.isUserVisibleAgent(user.getUserId(), user.getDeptFullNames(), WxAgentUtil.getAddressBookCode(), user.getOrgId(), user.getCorpId())) {
				//userInfo在通信录不可见
				addJsonObj("Invisible", true);
				doJsonOut();
                return;
			}
			userInfo = SecrecyUserUtil.secrecyUserDept(userInfo);
			addJsonObj("userInfo", userInfo);
			//lrl 2016-1-21 是否有开通消息服务 
			if(WxqyhAppContext.isVerified(user.getOrgId()) && WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
				addJsonObj("showMsgBtn", true);
			}
			//maquanyang 2015-6-17 新增查询设置通讯录字段是否显示
			List<TbQyFieldSettingVO> fieldList = this.contactService.findTbQyFieldSettingVOListByOrgId(userInfo.getOrgId());
			addJsonArray("fieldList",fieldList);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetAtuserInfoByID 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetAtuserInfoByID 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}

	/**
	 * 判断某用户是否为常用联系人
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = "2001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void isCommonUser() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String toUserId = request.getParameter("toUserId");
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		List<TbQyUserCommonPO> list = contactMgrService.isCommonUser(user.getUserId(), toUserId);
		if(AssertUtil.isEmpty(list)){
			addJsonObj("common", "0");
		}else{
			addJsonObj("common", "1");
		}
	}
	/**
	 * 手动添加常用联系人
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "操作成功", faileMsg = "操作失败"))
	public void addCommonUser() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String toUserId = request.getParameter("toUserId");
		String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();
		if(userId.equals(toUserId)){
			return ;
		}
		//手动添加为常用，相关度+10000
		contactService.updateCommonUser(userId, toUserId,10000);
	}
	/**
	 * 取消常用联系人
	 */
	@JSONOut(catchException = @CatchException(errCode = "2003", successMsg = "操作成功", faileMsg = "操作失败"))
	public void cancleCommonUser() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String toUserId = request.getParameter("toUserId");
		String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();
		contactMgrService.cancleCommonUser(userId, toUserId);
	}
	/**
	 * 查看我的个人资料
	 * @throws Exception
	 */
	public void viewMyInfo()throws Exception {
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			UserDeptInfoVO userDeptInfo = contactService.findUserDeptByUserId(userInfo.getUserId());
			Map<String, Object> searchMap = new HashMap<String, Object>(2);
			searchMap.put("userId", userInfo.getUserId());
			searchMap.put("status", "1");
			Integer count = contactMgrService.getStatuesGuessCount(searchMap);
			addJsonObj("count", count);
			
			//maquanyang 2015-7-20 新增查询设置通讯录字段是否允许员工可自行修改
			List<TbQyFieldSettingVO> fieldList = this.contactService.findTbQyFieldSettingVOListByOrgId(userInfo.getOrgId());
			addJsonArray("fieldList",fieldList);
			addJsonObj("userInfo", userDeptInfo);
			List<TbQyUserCustomOptionVO> optionVOs;
			if(VipUtil.isQwVip(userInfo.getOrgId() )) {
				optionVOs = contactCustomMgrService.getUserItemList(userInfo.getUserId(), userInfo.getOrgId());
				List<TbQyUserCustomOptionVO> removeVos = new ArrayList<TbQyUserCustomOptionVO>();
				for (TbQyUserCustomOptionVO optionVO : optionVOs) {
					if (ContactCustomUtil.NO_IS_SHOW.equals(optionVO.getIsShow())) {
						removeVos.add(optionVO);
					}
				}
				if (removeVos.size() > 0) {
					optionVOs.removeAll(removeVos);
				}

			}else{
				optionVOs = new ArrayList<TbQyUserCustomOptionVO>();
			}
			addJsonObj("optionVOs", optionVOs);
            setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	/**
	 * 联系人搜索--分页
	 */
	public void searchByNameOrPhone (){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId", userInfo.getOrgId());

			String typeId = request.getParameter("typeId");
			
			if(!AssertUtil.isEmpty(keyWord)){
				if("1".equals(typeId)){
					map.put("nickName", "%"+keyWord+"%");
				}else if("2".equals(typeId)){
					map.put("position", "%"+keyWord+"%");
				}else{
					map.put("keyWord", keyWord);
				}
				//map.put("hasDepart", departmentService.hasDepart(userInfo.getOrgId(),keyWord));
			}
			pager = contactMgrService.searchByNameOrPhone(map, pager,userInfo);
			addJsonPager("pageData", pager);
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(userInfo.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}

	/**
	 * 搜索拼音首字母（输出pager）
	 */
	public void searchFirstLetter(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			
			pager = contactMgrService.searchFirstLetter(keyWord , pager, userInfo);
			addJsonPager("pageData", pager);
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(userInfo.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	/**
	 * 搜索拼音首字母（输出list）
	 * 
	 * @author Sun Qinghai
	 * @2015-3-16
	 * @version 1.0
	 */
	public void searchFirstLetterList(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			Pager pager = new Pager(ServletActionContext.getRequest(),Integer.MAX_VALUE);//获取全部 
			pager = contactMgrService.searchFirstLetter(keyWord , pager, userInfo);
			addJsonArray("userList", pager.getPageData()==null?new ArrayList<TbQyUserInfoVO>():new ArrayList<TbQyUserInfoVO>(pager.getPageData()));
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(userInfo.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	/**
	 * 搜索拼音首字母（输出Page）
	 * 
	 * @author Sun Qinghai
	 * @2015-3-16
	 * @version 1.0
	 */
	public void searchFirstLetterPage(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			Pager pager = new Pager(ServletActionContext.getRequest(),10);
			pager = contactMgrService.searchFirstLetter(keyWord , pager, userInfo);
			addJsonPager("pageData", pager);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}
	/**
	 * @人员列表搜索
	 * 
	 * @throws Exception
	 */
	public void ajaxGetUserListByUserNameOrPhone() throws Exception {
		Pager pager = new Pager(ServletActionContext.getRequest(),10);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			String orgId = user.getOrgId();
			String keyWord = this.keyWord;
			map.put("orgId", orgId);
			if(!AssertUtil.isEmpty(keyWord)){
				map.put("keyWord", keyWord);
				map.put("hasDepart", departmentMgrService.hasDepart(orgId,keyWord));
			}
			pager=contactMgrService.findUsersByOrgId(map,pager);
			addJsonPager("pageData", pager);
			//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
			String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
			addJsonObj("isDisplayMobilel",isDisplayMobilel);
			setActionResult("0", "查询成功");
		} catch (Exception e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		} catch (BaseException e) {
			logger.error("ajaxGetOrganite 根据机构ID获取该机构下的所有用户失败", e);
			setActionResult("100", "根据机构ID获取该机构下的所有用户失败！");
		}
		doJsonOut();
	}

	public void findUserInfoByUserId(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			TbQyUserInfoVO user = contactService.findUserInfoByUserId(userInfo.getUserId());
			if(user == null){
				throw new NonePrintException("200",ErrorTip.USER_NULL.toString());
			}
			addJsonObj("user", user);
			doJsonOut();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新个人资料
	 * @throws Exception,BaseException 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
	public void editUserInfo( @InterfaceParam(name = "listJson")@Validation(must = false, name = "listJson")String listJson) throws Exception,BaseException{
		TbQyUserInfoPO history = contactService.searchByPk(TbQyUserInfoPO.class, tbQyUserInfoPO.getId());
		UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		if(null == history){
			setActionResult("1001", "找不到要修改的用户信息！");
			return;
		}
		if(!userInfo.getUserId().equals(history.getUserId())){
			setActionResult("1001", "只能修改本人的通讯录信息！");
			return;
		}
		if("-1".equals(history.getUserStatus())){
			setActionResult("1001", "你已离职");
			return;
		}
		boolean isUpdate=false;//是否修改过冗余字段，以便记录数据更新
		if(!history.getPersonName().equals(tbQyUserInfoPO.getPersonName())){
			isUpdate=true;
		}
		if(!AssertUtil.isEmpty(history.getHeadPic()) && !history.getHeadPic().equals(tbQyUserInfoPO.getHeadPic())){
			isUpdate=true;
		}

		WxUser user = WxUserService.getUser(history.getWxUserId(),userInfo.getCorpId(),userInfo.getOrgId(),WxAgentUtil.getAddressBookCode());
		if(user == null){
			setActionResult("1001", "用户账号有误，请联系管理人员！");
			return;
		}
		user.setWeixinid(history.getWeixinNum());
		//maquanyang 2015-7-21 修改按照后台配置的哪些字段可以修改
		history = isEditTbQyUserInfoPOInfo(history,tbQyUserInfoPO, user);
		//如果更新失败
		try {
			if(!WxUserService.updateUser(user,userInfo.getCorpId(),userInfo.getOrgId())){
				setActionResult("1001", "更新用户信息失败");
				return;
			}
		} catch (Exception e) {
			logger.error("更新用户信息失败",e);
			setActionResult("1001", "更新用户信息失败");
			return;
		}
		//history.setPosition(tbQyUserInfoPO.getPosition());
		history.setWeixinNum(user.getWeixinid());
		history.setUserStatus("2");
		//设置用户关注状态
		if("1".equals(user.getStatus())){
			if(!"2".equals(history.getUserStatus())){
				history.setUserStatus("2");
				history.setFollowTime(new Date());
			}
		}
		else{
			//取消关注的用户
			if("2".equals(history.getUserStatus())){
				history.setUserStatus("1");
				history.setCancelTime(new Date());
			}
		}
		history.setUpdateTime(new Date());
		history.setHeadPic(ContactUtil.getWeixinUserHeadPic(user.getAvatar()));
    	//新增生日提醒方式（马权阳 2015-06）
    	//history.setRemindType(tbQyUserInfoPO.getRemindType());
		contactService.updatePO(history, true);
		//更新扩展表的生日记录
		contactService.updateUserInfoExt(history,true);
		
		//修改缓存中的人员数据
		UserInfoVO infovo=contactService.getUserInfoNoCacheByUserId(history.getUserId());
		if(Configuration.IS_USE_MEMCACHED){
			UserInfoVO cacheUserInfo = CacheSessionManager.get(history.getUserId());
			if(cacheUserInfo != null){
				infovo.setDeviceId(cacheUserInfo.getDeviceId());
				CacheSessionManager.set(history.getUserId(), infovo);
			}
		}
		if(null != listJson) {
			JSONObject jsonList = JSONObject.fromObject(listJson);
			contactCustomMgrService.updateUserItem(jsonList, history.getUserId(), history.getOrgId());
		}
		//记录用户更新的数据，以便更新冗余字段 chenfeixiong 2015/06/30
		try {
			if(isUpdate){
				Map<String,String> map=new HashMap<String,String>();
				map.put("creator", infovo.getUserId());
				map.put("userId", infovo.getUserId());
				map.put("orgId", infovo.getOrgId());
				map.put("item1", infovo.getPersonName());
				map.put("item2", infovo.getHeadPic());
				map.put("item3", infovo.getWxUserId());
				map.put("is_manager","0");
				contactService.addUdpatePersonInfo(map);
			}
		} catch (Exception e) {
			logger.debug("更新冗余字段",e);
		}catch (BaseException e) {
			logger.debug("更新冗余字段",e);
		}
	}

	/**
	 * 获得常用联系人数据
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "2001", successMsg = "更新成功", faileMsg = "更新失败"))
	public void getCommonList()throws Exception,BaseException{
		UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		String userId = user.getUserId();
		List<TbQyUserInfoVO> list = contactMgrService.getCommonUserList(userId, 20);
		list = SecrecyUserUtil.secrecylist(user,list);
		addJsonArray("commonList", list);
		//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
		String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
		addJsonObj("isDisplayMobilel",isDisplayMobilel);
//		if(WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
			addJsonObj("showMsgBtn", true);
//		}
	}
	/**
	 * 常用联系人列表搜索
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode = "2002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void searchCommonList()throws Exception,BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		UserInfoVO user = WxqyhAppContext.getCurrentUser(request);
		String userId = user.getUserId();
		String type = request.getParameter("type");
		String keyword = this.keyWord;
		if("firstLetter".equals(type)){
			keyword = keyword+"%";
		}else{
			keyword = "%"+keyword+"%";
		}
		List<TbQyUserInfoVO> list = contactMgrService.searchCommonUserList(userId, keyword);
		//用户保密
		list =SecrecyUserUtil.secrecylist(user,list);
		addJsonArray("commonList", list);
		//maquanyang 2015-8-12 判断列表右边的快捷电话是否可拨打
		String isDisplayMobilel = this.contactService.getSetFiled(user.getOrgId());
		addJsonObj("isDisplayMobilel",isDisplayMobilel);
//		if(WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
			addJsonObj("showMsgBtn", true);
//		}
	}
	
	/**
	 *  新增 保存留言
	 * @throws BaseException
	 * @author libo
	 * @2014-7-30
	 * @version 1.0
	 */
	public void addguess(){
		try {
	        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			TbQyUserInfoPO tbQyUserInfoPO=contactService.searchByPk(TbQyUserInfoPO.class, id);
			if(tbQyUserInfoPO==null || !user.getOrgId().equals(tbQyUserInfoPO.getOrgId()) || "-1".equals(tbQyUserInfoPO.getUserStatus())){
				setActionResult("1888", "用户已离职或者已被删除");
				doJsonOut();
				return;
			}
			TbAddressbookGuessbookPO po = new TbAddressbookGuessbookPO();
			po.setStatus("1");//留言默认为未读状态
			po.setContents(keyWord);
			po.setCreator(user.getUserId());
			po.setUserId(tbQyUserInfoPO.getUserId());
			po.setCreateTime(new Date());//获取当前时间
			po.setGuess_id(id);
            contactService.insertPO(po, true);
            NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
			newsMessageVO.setTouser(tbQyUserInfoPO.getWxUserId());
			newsMessageVO.setDuration("0");
			newsMessageVO.setTitle("留言通知");
			newsMessageVO.setDescription("留言人:"+user.getPersonName()+"\r\n"+ po.getContents());
			newsMessageVO.setUrl(Configuration.WX_PORT + "/jsp/wap/addressbook/guess_detail.jsp?id="+po.getId());
			newsMessageVO.setCorpId(user.getCorpId());
			newsMessageVO.setAgentCode(WxAgentUtil.getAddressBookCode());
			newsMessageVO.setOrgId(user.getOrgId());
			WxMessageUtil.sendNewsMessage(newsMessageVO);
			/*WxMessageUtil.sendNewsMessage("留言通知", "留言人:"+user.getPersonName()+"\r\n"+ po.getContents(), "", 
					Configuration.WX_PORT + "/jsp/wap/addressbook/guess_detail.jsp?id="+po.getId()+"&corp_id="+orgPO.getCorpId(), tbQyUserInfoPO.getWxUserId(),null, "0",
					WxAgentUtil.getAddressBookCode(),orgPO.getCorpId());*/

			//已经托管企业消息服务的才执行
			if(WxAgentUtil.isTrustAgent(user.getCorpId(),WxAgentUtil.getChatIMCode())){
				ChatManager.msgToSignlChat(user.getCorpId(), tbQyUserInfoPO.getWxUserId(), user.getWxUserId(), MsgType.text, po.getContents());
			}
			
			setActionResult("0", "留言成功");
		} catch (Exception e) {
			logger.error("留言失败",e);
			setActionResult("1005", "留言失败");
		} catch (BaseException e) {
			logger.error("留言失败",e);
			setActionResult("1005", "留言失败");
		}
		doJsonOut();
	}
	/**
	 * 
	 * @description：所有留言
	 * @throws Exception
	 * @throws BaseException
	 * @author libo
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void searchAllguess() throws Exception, BaseException{
		String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();
		Map<String,Object> map = new HashMap<String, Object>(1);
		map.put("userId", userId);
		Pager pager = new Pager( ServletActionContext.getRequest(), getPageSize());	
		pager =contactMgrService.getTbAddressbookGuessbookPOByUserId(userId,pager,map);
		changeStatus(userId);		//全部留言标记已读
		Collection<?> pageData = pager.getPageData();
		//addJsonArray("comments", pageData != null ? new ArrayList<Object>(pageData) : Collections.emptyList());
		addJsonPager("comments",pager);
		addJsonObj("hasMore", pageData == null ? false : pager.getTotalRows() > pageData.size());
	}
	/**
	 * 改变留言状态  0为已读 1为未读
	 * @throws Exception
	 * @throws BaseException
	 * @author chenfeixiong
	 * 2014-8-7
	 */
	public void changeStatus(String userId)  throws Exception, BaseException{
		contactMgrService.changeStatus(userId);
	}
	/**
	 * 加载更多留言
	 * @throws Exception
	 * @throws BaseException
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void listOtherGuess() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), size);
        String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();
        Map map = getSearchValue();
		map.put("userId", userId);
        pager = contactMgrService.getTbAddressbookGuessbookPOByUserId(userId,pager,map);
        Collection<?> comments = pager.getPageData();
        //addJsonArray("comments", comments != null ? new ArrayList<Object>(comments) : Collections.emptyList());
        boolean hasMore = pager == null ? false : pager.getCurrentPage() < pager.getTotalPages();
        addJsonObj("hasMore", hasMore);
        addJsonPager("comments",pager);
        addJsonObj("currentPage", pager.getCurrentPage());
        addJsonObj("totalRows", pager.getTotalRows());
    }
	
	/**
	 * 查看留言详情
	 * @throws Exception
	 * @throws BaseException
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void guessDetail() throws Exception, BaseException {
		WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		TbAddressbookGuessbookPO po =contactService.searchByPk(TbAddressbookGuessbookPO.class, id);
		if(AssertUtil.isEmpty(po)){
			addJsonObj("userInfo",new TbQyUserInfoVO());
			return;
		}
		po.setStatus("0");//点击查看的更新状态
		contactService.updatePO(po,false);
		addJsonFormateObj("guessPO", po);//注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
		TbQyUserInfoVO user=contactService.findUserInfoByUserId(po.getCreator());
		addJsonObj("userInfo",user);
	}
	
	
	
	//删除回复
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "删除成功", faileMsg = "删除失败"))
    public void deleteGuess() throws Exception, BaseException {
		
		TbAddressbookGuessbookPO po = new TbAddressbookGuessbookPO();
		po.setId(id);
		contactService.delPO(po);
		
    }
	/**
	 * 加载更多回复
	 * @throws Exception
	 * @throws BaseException
	 * @author libo
	 * @2014-7-31
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void listOtherReply() throws Exception, BaseException {
	
		String userId = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest()).getUserId();

		if(AssertUtil.isEmpty(userId)){
			throw new NonePrintException("200",ErrorTip.USER_ID_NULL.toString());
		}
        Pager pager = new Pager(ServletActionContext.getRequest(), size);
		Map searchMap = getSearchValue();
		searchMap.put("creator", userId);
		searchMap.put("guessId", id);
		Pager comments = contactMgrService.getTbAddressbookGuessbookPOReply(pager, searchMap);

		Collection<?> pageData = comments.getPageData();
		addJsonArray("comments", pageData != null ? new ArrayList<Object>(pageData) : Collections.emptyList());
		addJsonObj("hasMore", pageData == null ? false : pageData.size() > size);
    }
	
	/**
	 *	获取通讯录里设置的默认负责人
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2014-7-31
	 * @version 1.0
	 */
	public void getPersonInfoByUserId(){
		try {
			UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
			addJsonObj("user", userInfo);
			
			List<TbQyUserGroupPersonVO> inchargesList = defatgroupService.getGroupPersonByUserId(userInfo.getUserId(), "1");
			Map<String, Object> map=new HashMap<String, Object>();
			logger.info("负责人-------------"+inchargesList.size());
			if(!AssertUtil.isEmpty(inchargesList)){
				for (TbQyUserGroupPersonVO tbQyUserGroupPersonVO : inchargesList) {
					if(tbQyUserGroupPersonVO.getUserId().equals(userInfo.getUserId())){
						map.put(userInfo.getUserId(), tbQyUserGroupPersonVO);
					}
				}
				if(!AssertUtil.isEmpty(map.get(userInfo.getUserId()))){
					inchargesList.remove(map.get(userInfo.getUserId()));
				}
				addJsonObj("inchargesList", inchargesList);
			}
			
			List<TbQyUserGroupPersonVO> receiveList = defatgroupService.getGroupPersonByUserId(userInfo.getUserId(), "2");
			if(!AssertUtil.isEmpty(receiveList)){
				addJsonObj("receiveList", receiveList);
			}
			logger.info("相关人==="+receiveList.size());
		} catch (Exception e) {
			logger.error("获取默认群组负责人失败!",e);
		} catch (BaseException e) {
			logger.error("获取默认群组负责人失败!",e);
		}
		doJsonOut();
	}
	
	public void setTbQyUserInfoPO(TbQyUserInfoPO tbQyUserInfoPO) {
		this.tbQyUserInfoPO = tbQyUserInfoPO;
	}

	public TbQyUserInfoPO setTbQyUserInfoPO() {
		return this.tbQyUserInfoPO;
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxView() throws Exception, BaseException {
		TbQyUserInfoPO xxPO = contactService.searchByPk(TbQyUserInfoPO.class,
				id);
		addJsonFormateObj("tbQyUserInfoPO", xxPO);// 注意，PO才用addJsonFormateObj，如果是VO，要采用addJsonObj
	}

	public TbQyUserInfoPO getTbQyUserInfoPO() {
		return this.tbQyUserInfoPO;
	}
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size 要设置的 size
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	
	public TbQyUserInfoPO isEditTbQyUserInfoPOInfo(TbQyUserInfoPO history,TbQyUserInfoPO tbQyUserInfoPO, WxUser user) throws Exception, BaseException{
		List<TbQyFieldSettingVO> fieldList = this.contactService.findTbQyFieldSettingVOListByOrgId(history.getOrgId());
		String nickNameIsEdit = ContactUtil.IS_EDIT_0;
		String addressIsEdit = ContactUtil.IS_EDIT_0;
		String mobileIsEdit = ContactUtil.IS_EDIT_0;
		String shorMobileIsEdit = ContactUtil.IS_EDIT_0;
		String phoneIsEdit = ContactUtil.IS_EDIT_0;
		String emailIsEdit = ContactUtil.IS_EDIT_0;
		String qqNumIsEdit = ContactUtil.IS_EDIT_0;
		String birthdayIsEdit = ContactUtil.IS_EDIT_0;
		String lunarCalendarIsEdit = ContactUtil.IS_EDIT_0;
		String remindTypeIsEdit = ContactUtil.IS_EDIT_0;
		String positionIsEdit = ContactUtil.IS_EDIT_0;
		if(!AssertUtil.isEmpty(fieldList) && fieldList.size() > 0){
			for(TbQyFieldSettingVO fieldVO : fieldList){
				//控制那些内容在页面上需要
				if(ContactUtil.FIELD_NAME_1.equals(fieldVO.getField())){//昵称
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						nickNameIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_2.equals(fieldVO.getField())){//地址
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						addressIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_3.equals(fieldVO.getField())){//手机号码
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						mobileIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_4.equals(fieldVO.getField())){//电话1
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						shorMobileIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_5.equals(fieldVO.getField())){//电话2
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						phoneIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_6.equals(fieldVO.getField())){//邮箱可编辑
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						emailIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_7.equals(fieldVO.getField())){//qq号码可编辑
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						qqNumIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_8.equals(fieldVO.getField())){//阳历生日可编辑
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						birthdayIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_9.equals(fieldVO.getField())){//农历生日
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						lunarCalendarIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_10.equals(fieldVO.getField())){//生日提醒
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						remindTypeIsEdit = ContactUtil.IS_EDIT_1;
					}
				}else if(ContactUtil.FIELD_NAME_13.equals(fieldVO.getField())){//职位
					if(ContactUtil.IS_EDIT_1.equals(fieldVO.getIsEdit())){//不可编辑
						positionIsEdit = ContactUtil.IS_EDIT_1;
					}
				}
			}
		}
		
		if(ContactUtil.IS_EDIT_0.equals(nickNameIsEdit)){//昵称
			history.setNickName(tbQyUserInfoPO.getNickName());
		}
		if(ContactUtil.IS_EDIT_0.equals(addressIsEdit)){//地址
			history.setAddress(tbQyUserInfoPO.getAddress());
		}
		if(ContactUtil.IS_EDIT_0.equals(mobileIsEdit)){//手机号码
			history.setMobile(tbQyUserInfoPO.getMobile());
			user.setMobile(tbQyUserInfoPO.getMobile());
		}
		else{
			user.setMobile(history.getMobile());
		}
		if(ContactUtil.IS_EDIT_0.equals(shorMobileIsEdit)){//电话1
			history.setShorMobile(tbQyUserInfoPO.getShorMobile());
		}
		if(ContactUtil.IS_EDIT_0.equals(phoneIsEdit)){//电话2
			history.setPhone(tbQyUserInfoPO.getPhone());
		}
		if(ContactUtil.IS_EDIT_0.equals(emailIsEdit)){//邮箱
			history.setEmail(tbQyUserInfoPO.getEmail());
			user.setEmail(tbQyUserInfoPO.getEmail());
		}
		else {
			user.setEmail(history.getEmail());
		}
		if(ContactUtil.IS_EDIT_0.equals(qqNumIsEdit)){//qq号码
			history.setQqNum(tbQyUserInfoPO.getQqNum());
		}
		if(ContactUtil.IS_EDIT_0.equals(birthdayIsEdit)){//阳历生日
			history.setBirthday(tbQyUserInfoPO.getBirthday());
		}
		if(ContactUtil.IS_EDIT_0.equals(lunarCalendarIsEdit)){//农历生日
			history.setLunarCalendar(tbQyUserInfoPO.getLunarCalendar());
		}
		if(ContactUtil.IS_EDIT_0.equals(remindTypeIsEdit)){//生日提醒
			history.setRemindType(tbQyUserInfoPO.getRemindType());
		}
		if(ContactUtil.IS_EDIT_0.equals(positionIsEdit)){//职位
			history.setPosition(tbQyUserInfoPO.getPosition());
			user.setPosition(tbQyUserInfoPO.getPosition());
		}
		else {
			user.setPosition(history.getPosition());
		}
		return history;
	}
	
	/**
	 * 查询通讯录查询的条件
	 * @throws BaseException 
	 * @throws Exception 
	 */
	@JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getSearch() throws Exception, BaseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String searchId = request.getParameter("searchId");
		String agentCode = request.getParameter("agentCode");
		UserInfoVO userVO = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		TbQyUserSearchVO searchVO = new TbQyUserSearchVO();
		searchVO.setId(searchId);
		searchVO.setAgentCode(agentCode);
		searchVO.setCreater(userVO.getUserId());
		searchVO.setOrgId(userVO.getOrgId());
		TbQyUserSearchVO searchResult = this.contactMgrService.getUserSearchByTbQyUserSearchVO(searchVO);
		addJsonObj("searchResult", searchResult);
	}
	
	/**
	 * 查询一级部门列表
	 * @throws BaseException 
	 * @throws Exception 
	 */

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getDeptList() throws Exception, BaseException {
		UserInfoVO userVO = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		if(userVO==null){
			throw new NonePrintException("200",ErrorTip.USER_NULL.toString());
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		String parentId = request.getParameter("pId");
		if (AssertUtil.isEmpty(parentId)) {
			parentId="";
		}
		if (!AssertUtil.isEmpty(keyWord)) {
			getSearchValue().put("title", "%"+keyWord+"%");
		}
		addJsonObj("isType", parentId);
		getSearchValue().put("orgId", userVO.getOrgId());
		getSearchValue().put("parentId", parentId);
		Pager pager = new Pager(ServletActionContext.getRequest(),10);
		pager=departmentMgrService.searchPagerDept(getSearchValue(),pager);
		addJsonPager("pageData", pager);
	}

	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getUserHandWrit() throws BaseException, Exception{
		UserInfoVO userVO = getUser();
		addJsonObj("handWritPO", contactService.getUserHandWritByUserId(userVO.getUserId()));
	}


}
