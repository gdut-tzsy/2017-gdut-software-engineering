package cn.com.do1.component.contact.contact.ui;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.contact.util.MemberUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.vo.TbQyStudentInfoVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberMsgVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.IMemberService;
import cn.com.do1.component.contact.contact.util.MemberHandleUtil;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.vip.vip.service.IVipService;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;

public class PortalMemberAction extends WxqyhPortalBaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(ContactAction.class);
	private IContactService contactService;
	private IMemberService memberService;
	private TbQyMemberInfoPO tbQyMemberInfoPO;
	private IExperienceapplicationService experienceapplicationService;
	private IDepartmentService departmentService;
	private IContactMgrService contactMgrService;
	private String keyWord;
	/**
	 * The Student service.
	 */
	private IStudentService studentService;
	private IVipService vipService;
	/**
	 * 自定义字段service
	 */
	private IContactCustomMgrService contactCustomMgrService;

	private IPostService postService;
    @Resource(name = "vipService")
	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}
	
	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}
	
    @Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
    @Resource(name = "memberService")
    public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}
    @Resource(name = "departmentService")
    public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource(name = "postService")
	public void setPostService(IPostService postService) {
		this.postService = postService;
	}

	@Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(
			IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
	}
	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
	}
	/**
	 * Sets student service.
	 *
	 * @param studentService the student service
	 */
	@Resource(name = "studentService")
	public void setStudentService(IStudentService studentService) {
		this.studentService = studentService;
	}
	public TbQyMemberInfoPO getTbQyMemberInfoPO() {
		return tbQyMemberInfoPO;
	}
	public void setTbQyMemberInfoPO(TbQyMemberInfoPO tbQyMemberInfoPO) {
		this.tbQyMemberInfoPO = tbQyMemberInfoPO;
	}
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * 邀请单禁用状态  1 启用     0禁用
	 */
	private static String close_status="0";
	/**
	 *	外部成员邀请单获取设置信息
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询邀请详情设置信息失败"))
	public void getMemberSet() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(config)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
	    	return;
		}
		if(close_status.equals(config.getStatus())){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DISABLE.getCode(), ErrorCodeDesc.MEMBER_IS_DISABLE.getDesc());
	    	return;
		}
		UserOrgVO org=contactService.getOrgByUserId(config.getCreator());
		if(AssertUtil.isEmpty(org)){
			setActionResult(ErrorCodeDesc.ORG_IS_NO_FIND.getCode(), ErrorCodeDesc.ORG_IS_NO_FIND.getDesc());
	    	return;
		}
		boolean isVip=VipUtil.isQwVip(org.getOrgId());
		if(!isVip && !MemberUtil.SHOW_INDEX.equals(config.getShowIndex())){
			setActionResult(ErrorCodeDesc.MEMBER_IS_ONLY_VIP.getCode(), ErrorCodeDesc.MEMBER_IS_ONLY_VIP.getDesc());
	    	return;
		}
		List<TbQyMemberConfigResPO> list=memberService.getDeptList(org.getOrgId(),config.getId());
		if(AssertUtil.isEmpty(list)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
	    	return;
		}
		if(!AssertUtil.isEmpty(org.getCorpId())){
			//增加输出corpId
			if(org.getCorpId().equals(Configuration.AUTO_CORPID) && !org.getOrgId().equals(Configuration.COMPANY_ORG_ID)){
				addJsonObj("wxqrcode","http://wbg.do1.com.cn/templets/default/images/emailWX.jpg");
			}else{
				addJsonObj("wxqrcode",experienceapplicationService.findWxqrcodeByOrgId(org.getOrgId()));
			}
		}else{
			addJsonObj("wxqrcode","");
		}
		
		//是否在有效期内
		if(config.getStopTime().before(new Date())){
			addJsonObj("isValid","0");//过期
		}else{
			addJsonObj("isValid","1");//有效
		}
		
		addJsonObj("startTime",DateUtil.format(config.getStartTime(), "yyyy-MM-dd HH:mm"));//过期
		addJsonObj("stopTime",DateUtil.format(config.getStopTime(), "yyyy-MM-dd HH:mm"));//过期
		
		DqdpOrgVO orgVO = WxqyhAppContext.getOrgExtInfo(org.getOrgId());
		addJsonObj("logo", orgVO.getLogo());
		addJsonObj("config", config);
		addJsonArray("list", list);
		addJsonObj("optionVOs", contactCustomMgrService.getUseingOptionByorgId(org.getOrgId()));
		addJsonObj("memberCustomOptionVOs", memberService.getMemberCustomConfigByMeberId(id));
		addJsonObj("memberBaseOptionVOs", memberService.getMemberBaseConfigByMeberId(id));
		addJsonObj("postVOs", postService.getPositionListByOrgId(org.getOrgId()));
		//输出是否访问footer
		addJsonObj("isShowFooter",vipService.isShowOpenFooter(org.getOrgId()));
	}
	
	/**
	 *	保存成员邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "保存成员邀请失败"))
	public void ajaxAdd(@InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(config)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
	    	return;
		}
		List<TbQyMemberConfigResPO> res= memberService.getDeptList(config.getOrgId(),config.getId());
		if(AssertUtil.isEmpty(res)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
	    	return;
		}
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getSelectDeptId())){
			tbQyMemberInfoPO.setSelectDeptId(res.get(0).getDeptId());
			tbQyMemberInfoPO.setSelectDept(res.get(0).getDept());
		}
		tbQyMemberInfoPO.setId(UUID.randomUUID().toString());
		tbQyMemberInfoPO.setOrgId(config.getOrgId());
		tbQyMemberInfoPO.setCreateTime(new Date());
		tbQyMemberInfoPO.setRefId(config.getId());
		tbQyMemberInfoPO.setConfigName(config.getName());
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getWxUserId())){
			if(!StringUtil.isNullEmpty(tbQyMemberInfoPO.getMobile())) {
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getMobile());
			}else{
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getWeixinNum());
			}
		}
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//批量新增自定义字段
		MemberUtil.batchAddCustom(customPOList, customUserList, config, tbQyMemberInfoPO);
		//验证用户信息
		TbQyMemberMsgVO msg=this.memberService.verifyMemberInfo(tbQyMemberInfoPO);
		if(!StringUtil.isNullEmpty(msg.getCode())){
			setActionResult(msg.getCode(), msg.getMsg());
	    	return;
		}
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, config.getOrgId());
		TbQyUserInfoPO userInfo=new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(orgPO.getCorpId(), tbQyMemberInfoPO.getWxUserId()));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(tbQyMemberInfoPO.getWeixinNum());
		userInfo.setMobile(tbQyMemberInfoPO.getMobile());
		userInfo.setEmail(tbQyMemberInfoPO.getEmail());
		userInfo.setWxUserId(tbQyMemberInfoPO.getWxUserId());
		//验证通讯录中是否存在该成员，存在不予提交
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(),orgPO.getOrganizationId(),userInfo,"0");
		if(usermsg!=null){
			setActionResult("2011", usermsg);
			return;
		}
		if(MemberUtil.NO_WAY.equals(config.getRequestWay())){//不用审批的
			tbQyMemberInfoPO.setStatus(MemberUtil.STATUS);
			UserOrgVO org=contactService.getOrgByUserId(config.getCreator());
			tbQyMemberInfoPO.setApprovePerson(org.getPersonName());
			tbQyMemberInfoPO.setApproveUserId(org.getUserName());
			tbQyMemberInfoPO.setApproveTime(new Date());
			String message=memberService.insertContact(tbQyMemberInfoPO, org,userInfo,tbQyMemberInfoPO.getSelectDeptId());
			if(!StringUtil.isNullEmpty(message)){
				addJsonObj("message", message);
			}
			List<TbQyUserCustomItemPO> list = MemberUtil.setMemberToCustom(customPOList, userInfo);
			QwtoolUtil.addBatchList(list, true);
		}else if(MemberUtil.NEED_WAY.equals(config.getRequestWay())){//审批邀请
			tbQyMemberInfoPO.setStatus(MemberUtil.NO_STATUS);
		}
		//给接收成员通知的对象发送消息
		memberService.insertPO(tbQyMemberInfoPO, false);
		List<TbQyMemberTargetPersonPO> list=memberService.getTargetPerson(config.getId());
		this.sendMsg(list, orgPO, tbQyMemberInfoPO,config.getRequestWay());
	}
	
	 /**
	 * 获取邀请详情
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询邀请详情失败"))
	public void getDetail() throws Exception, BaseException {
		UserInfoVO userInfo = getUser();
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getId())){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
	    	return;
		}
		TbQyMemberInfoVO vo=memberService.searchByPk(TbQyMemberInfoVO.class, tbQyMemberInfoPO.getId());
		if(AssertUtil.isEmpty(vo)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
	    	return;
		}
		List<TbQyMemberInfoVO> list = memberService.searchChildrenToVO(vo.getId());
		addJsonObj("list", list);
		MemberUtil.addTypeToVO(vo);
		addJsonObj("tbQyMemberInfoPO", vo);
		 //查询是否有邀请设置信息
		TbQyMemberConfigVO config=memberService.getHistotryData(vo.getRefId(),userInfo.getOrgId());
		List<TbQyMemberUserCustomPO> customList = new ArrayList<TbQyMemberUserCustomPO>();
		//判断是否有权限
		if(VipUtil.hasGoldPermission(userInfo.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			customList.addAll(memberService.getMemberUserCustom(vo.getId()));
		}
		addJsonObj("customUserList", customList);
		addJsonObj("corpId", userInfo.getCorpId());
		addJsonObj("corpIdStr", Configuration.ALUMNI_ASSOCIATION_CI);
    	addJsonObj("config", config);
		addJsonObj("optionVOs", contactCustomMgrService.getUseingOptionByorgId(userInfo.getOrgId()));
		addJsonObj("postVOs", postService.getPositionListByOrgId(userInfo.getOrgId()));
	}
	
	
	/**
	 * @param list
	 * @param orgPO
	 * @param tbQyMemberInfoPO
	 * @param requestWay
	 * @author Hejinjiao
	 * @2015-4-21
	 * @version 1.0
	 * @param requestWay 
	 * @throws Exception 
	 * @throws BaseException 
	 */
	public void sendMsg(List<TbQyMemberTargetPersonPO> list, ExtOrgPO orgPO,
			TbQyMemberInfoPO tbQyMemberInfoPO, String requestWay) throws BaseException, Exception {
		StringBuffer send = MemberUtil.toSend(list);
		if (send.length()>0) {
			send.deleteCharAt(0);
			String content = "";
			String title="";
			if("1".equals(requestWay)){//如果是直接邀请
				title=tbQyMemberInfoPO.getPersonName()+" 加入了企业微信";
				content="["+tbQyMemberInfoPO.getPersonName()+"]通过外部邀请功能，直接加入了企业微信，点击查看详情。";
			}else{//如果是间接邀请
				title=tbQyMemberInfoPO.getPersonName()+" 申请加入企业微信";
				content="["+tbQyMemberInfoPO.getPersonName()+"]提交了申请单，点击查看或审批。";
			}
			NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
			newsMessageVO.setTouser(send.toString());
			newsMessageVO.setDuration("0");
			newsMessageVO.setTitle(title);
			newsMessageVO.setDescription(content);
			String corpIdStr=Configuration.ALUMNI_ASSOCIATION_CI;
			if(corpIdStr.indexOf(orgPO.getCorpId())<0){
				newsMessageVO.setUrl(Configuration.WX_PORT+ "/jsp/wap/member/member_edit.jsp?id="+ tbQyMemberInfoPO.getId());
			}else{
				newsMessageVO.setUrl(Configuration.WX_PORT+ "/jsp/wap/member/member_edit1.jsp?id="+ tbQyMemberInfoPO.getId());
			}
			newsMessageVO.setCorpId(orgPO.getCorpId());
			newsMessageVO.setAgentCode(WxAgentUtil.getAddressBookCode());
			newsMessageVO.setOrgId(orgPO.getOrganizationId());
			WxMessageUtil.sendNewsMessage(newsMessageVO);
		}
	}
	
	/**
	 *	邀请详情编辑
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2015-11-12
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "保存成功", faileMsg = "修改并审批邀请失败"))
	public void updateMember(@InterfaceParam(name = "btnType")@Validation(must = true, name = "btnType")String btnType,
							 @InterfaceParam(name = "deptNo")@Validation(must = false, name = "deptNo")String deptNo,
							 @InterfaceParam(name = "deptN")@Validation(must = false, name = "deptN")String deptN,
							 @InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		
		UserInfoVO userInfovo = getUser();
		TbQyMemberInfoPO po=memberService.searchByPk(TbQyMemberInfoPO.class, tbQyMemberInfoPO.getId());
		if(AssertUtil.isEmpty(po)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
	    	return;
		}
		if(!StringUtil.isNullEmpty(po.getStatus())){
			if(MemberHandleUtil.APPROVE_PASSED.equals(po.getStatus())){
				setActionResult("2000", "该邀请人员已被"+po.getApprovePerson()+"于"+DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss")+"审批通过！");
		    	return;
			}else if(MemberHandleUtil.NOT_APPROVE_PASSED.equals(po.getStatus())){
				setActionResult("2001", "该邀请人员已被"+po.getApprovePerson()+"于"+DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss")+"审批不通过！");
		    	return;
			}
		}
		UserOrgVO org=new UserOrgVO();
		org.setUserId(userInfovo.getUserId());
		org.setOrgId(userInfovo.getOrgId());
		org.setCorpId(userInfovo.getCorpId());
		org.setPersonName(userInfovo.getPersonName());
		MemberUtil.checkStatus(po);
		//把更新的数据放入po里面
		MemberUtil.setUpdateToPO(po, tbQyMemberInfoPO, deptNo, deptN);
		po.setSelectDeptId(tbQyMemberInfoPO.getSelectDeptId());
		po.setSelectDept(tbQyMemberInfoPO.getSelectDept());
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//如果从页面传来的自定义字段设置不为空,而且是金卡vip用户
		if(!AssertUtil.isEmpty(customUserList) && VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			JSONArray jsonArray = JSONArray.fromObject(customUserList);
			//从jsp页面传来的base转换成的数组
			TbQyMemberUserCustomPO[] customJspVOs = (TbQyMemberUserCustomPO[]) JSONArray.toArray(jsonArray, TbQyMemberUserCustomPO.class);
			customPOList.addAll(Arrays.asList(customJspVOs));
		}
		memberService.updateMember(btnType, po, org, customPOList );
	}
	
	/**
	 * 查看成员邀请列表
	 * @throws Exception
	 * @throws BaseException
	 * @author luobowen
	 * @2016-1-5
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询成员列表失败"))
	public void seachMemberList() throws Exception, BaseException {
		UserInfoVO userInfovo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		HttpServletRequest request = ServletActionContext.getRequest();
		String typeId = request.getParameter("typeId");
		Map<String, String> map = new HashMap<String, String>();
		if(!StringUtil.isNullEmpty(typeId)){
			map.put("status", typeId);
		}
		map.put("userId", userInfovo.getUserId());
		map.put("orgId", userInfovo.getOrgId());
		if (!StringUtil.isNullEmpty(keyWord)) {
			map.put("keyWord", "%" + keyWord + "%");
		}
		Pager pager = new Pager(ServletActionContext.getRequest(),
				getPageSize());
		pager=memberService.seachMemberList(map,pager);
		List<TbQyMemberInfoVO> voList=(List<TbQyMemberInfoVO>) pager.getPageData();
        if(!AssertUtil.isEmpty(voList)){
        	for (TbQyMemberInfoVO tbQyMemberInfoVO : voList) {
				MemberUtil.addTypeToVO(tbQyMemberInfoVO);
        	}
        }
        addJsonObj("corpId", userInfovo.getCorpId());
		addJsonObj("corpIdStr", Configuration.ALUMNI_ASSOCIATION_CI);
		addJsonPager("pageData", pager);

	}

	/**
	 *	教育版保存成员邀请
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-22
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "保存成功", faileMsg = "保存成员邀请失败"))
	public void ajaxAddEdu(@InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(config)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
			return;
		}
		List<TbQyMemberConfigResPO> res= memberService.getDeptList(config.getOrgId(),config.getId());
		if(AssertUtil.isEmpty(res)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
			return;
		}
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getSelectDeptId())){
			tbQyMemberInfoPO.setSelectDeptId(res.get(0).getDeptId());
			tbQyMemberInfoPO.setSelectDept(res.get(0).getDept());
		}
		tbQyMemberInfoPO.setId(UUID.randomUUID().toString());
		tbQyMemberInfoPO.setOrgId(config.getOrgId());
		tbQyMemberInfoPO.setCreateTime(new Date());
		tbQyMemberInfoPO.setRefId(config.getId());
		tbQyMemberInfoPO.setConfigName(config.getName());
		tbQyMemberInfoPO.setType(config.getTargetType());
		//直接邀请
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getWxUserId())){
			if(!StringUtil.isNullEmpty(tbQyMemberInfoPO.getMobile())) {
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getMobile());
			}else{
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getWeixinNum());
			}
		}
		//验证用户信息
		TbQyMemberMsgVO msg=this.memberService.verifyMemberInfo(tbQyMemberInfoPO);
		if(!StringUtil.isNullEmpty(msg.getCode())){
			setActionResult(msg.getCode(), msg.getMsg());
			return;
		}
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//批量新增自定义字段
		MemberUtil.batchAddCustom(customPOList, customUserList, config, tbQyMemberInfoPO);
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, config.getOrgId());
		TbQyUserInfoPO userInfo=new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(orgPO.getCorpId(), tbQyMemberInfoPO.getWxUserId()));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(tbQyMemberInfoPO.getWeixinNum());
		userInfo.setMobile(tbQyMemberInfoPO.getMobile());
		userInfo.setEmail(tbQyMemberInfoPO.getEmail());
		userInfo.setWxUserId(tbQyMemberInfoPO.getWxUserId());
		MemberUtil.setAttributeToUser(userInfo, tbQyMemberInfoPO);
		//验证通讯录中是否存在该成员，存在不予提交
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(),orgPO.getOrganizationId(),userInfo,"0");
		if(usermsg!=null){
			setActionResult("2011", usermsg);
			return;
		}
		if(MemberUtil.REQUEST_WAY_DIRECT.equals(config.getRequestWay())){//不用审批
			tbQyMemberInfoPO.setStatus(MemberUtil.STATUS);
			UserOrgVO org=contactService.getOrgByUserId(config.getCreator());
			tbQyMemberInfoPO.setApprovePerson(org.getPersonName());
			tbQyMemberInfoPO.setApproveUserId(org.getUserName());
			tbQyMemberInfoPO.setApproveTime(new Date());
			String message=memberService.insertContact(tbQyMemberInfoPO, org,userInfo,tbQyMemberInfoPO.getSelectDeptId());
			if(MemberUtil.TARGER_TYPE_STU.equals(tbQyMemberInfoPO.getType())){//如果类型是学生
				TbDepartmentInfoPO departPO = departmentService.searchByPk(TbDepartmentInfoPO.class, tbQyMemberInfoPO.getSelectDeptId());
				if(DepartmentUtil.ATTRIBUTE_TEACHER == departPO.getAttribute()){//如果是教学班级
					memberService.addOrUpdateStudent(tbQyMemberInfoPO, org,userInfo);//新增或者修改学生到学生表中
				}
			}
			if(!StringUtil.isNullEmpty(message)){
				addJsonObj("message", message);
			}
			QwtoolUtil.addBatchList(MemberUtil.setMemberToCustom(customPOList, userInfo), true);
		}else if(MemberUtil.REQUEST_WAY_CHECK.equals(config.getRequestWay())){//审批邀请
			tbQyMemberInfoPO.setStatus(MemberUtil.NO_STATUS);
		}
		addJsonObj("parentId", tbQyMemberInfoPO.getId());
		//给接收成员通知的对象发送消息
		memberService.insertPO(tbQyMemberInfoPO, false);
		List<TbQyMemberTargetPersonPO> list=memberService.getTargetPerson(config.getId());
		MemberUtil.sendMsgEdu(list, orgPO, tbQyMemberInfoPO,config.getRequestWay());
	}

	/**
	 *	教育版检查孩子
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-23
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "保存成功", faileMsg = "保存成员邀请失败"))
	public void checkStudent( @InterfaceParam(name = "id")@Validation(must = false, name = "id")String id,
			                 @InterfaceParam(name = "personName")@Validation(must = false, name = "personName")String personName,
							 @InterfaceParam(name = "classId")@Validation(must = false, name = "classId")String classId,
							 @InterfaceParam(name = "registerPhone")@Validation(must = false, name = "registerPhone")String registerPhone) throws Exception, BaseException{
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(config)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(),ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("personName", personName);
		map.put("classId", classId);
		map.put("orgId", config.getOrgId());
		if(!AssertUtil.isEmpty(registerPhone)){
			map.put("registerPhone", registerPhone);
		}
		List<TbQyStudentInfoVO> infoVOs = studentService.getExistStudents(map);
		if(MemberUtil.IS_CHECK.equals(config.getIsCheck())){
			if(infoVOs.size() > 0){
				addJsonObj("info","1");
			}else {
				addJsonObj("info", "您输入的"+personName+"不在系统中，请重新输入");
				return;
			}
		}else if(MemberUtil.NO_IS_CHECK.equals(config.getIsCheck())){
			if(AssertUtil.isEmpty(registerPhone)){
				if(infoVOs.size() == 0){
					addJsonObj("info","1");
				}else{
					addJsonObj("info","出现重名，请检查入学登记手机号唯一性\n" +
							"确保其他亲人没有填写此项");
				}
			}else {
				if(infoVOs.size() == 0 || infoVOs.size() == 1){
					addJsonObj("info","1");
				}else{
					addJsonObj("info","出现重名，请检查入学登记手机号唯一性\n" +
							"确保其他亲人没有填写此项");
				}
			}
			return;
		}
	}

	/**
	 *	教育版新增孩子和亲人
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-11-23
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "保存成功", faileMsg = "保存成员邀请失败"))
	public void ajaxAddChildrenAndParents(@InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList,
										  @InterfaceParam(name = "listJson")@Validation(must = true, name = "listJson")String listJson) throws BaseException, Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		JSONObject json = JSONObject.fromObject(listJson);
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		if(AssertUtil.isEmpty(config)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
			return;
		}
		List<TbQyMemberConfigResPO> res= memberService.getDeptList(config.getOrgId(),config.getId());
		if(AssertUtil.isEmpty(res)){
			setActionResult(ErrorCodeDesc.MEMEBER_IS_NO_FIND.getCode(), ErrorCodeDesc.MEMEBER_IS_NO_FIND.getDesc());
			return;
		}
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getSelectDeptId())){
			tbQyMemberInfoPO.setSelectDeptId(res.get(0).getDeptId());
			tbQyMemberInfoPO.setSelectDept(res.get(0).getDept());
		}
		tbQyMemberInfoPO.setId(UUID.randomUUID().toString());
		tbQyMemberInfoPO.setOrgId(config.getOrgId());
		tbQyMemberInfoPO.setCreateTime(new Date());
		tbQyMemberInfoPO.setRefId(config.getId());
		tbQyMemberInfoPO.setConfigName(config.getName());
		tbQyMemberInfoPO.setType(config.getTargetType());
		//直接邀请
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getWxUserId())){
			if(!StringUtil.isNullEmpty(tbQyMemberInfoPO.getMobile())) {
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getMobile());
			}else{
				tbQyMemberInfoPO.setWxUserId(tbQyMemberInfoPO.getWeixinNum());
			}
		}
		//验证用户信息
		TbQyMemberMsgVO msg=this.memberService.verifyMemberInfo(tbQyMemberInfoPO);
		if(!StringUtil.isNullEmpty(msg.getCode())){
			setActionResult(msg.getCode(), msg.getMsg());
			return;
		}
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//批量新增自定义字段
		MemberUtil.batchAddCustom(customPOList, customUserList, config, tbQyMemberInfoPO);
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, config.getOrgId());
		TbQyUserInfoPO userInfo=new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(orgPO.getCorpId(), tbQyMemberInfoPO.getWxUserId()));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(tbQyMemberInfoPO.getWeixinNum());
		userInfo.setMobile(tbQyMemberInfoPO.getMobile());
		userInfo.setEmail(tbQyMemberInfoPO.getEmail());
		userInfo.setWxUserId(tbQyMemberInfoPO.getWxUserId());
		MemberUtil.setAttributeToUser(userInfo, tbQyMemberInfoPO);
		//验证通讯录中是否存在该成员，存在不予提交
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(),orgPO.getOrganizationId(),userInfo,"0");
		UserOrgVO org=contactService.getOrgByUserId(config.getCreator());
		if(usermsg!=null){
			setActionResult("2011", usermsg);
			return;
		}
		//孩子列表
		List<TbQyMemberInfoPO> infoPOs = MemberUtil.jsonToList(json, org, tbQyMemberInfoPO, config);
		if(MemberUtil.REQUEST_WAY_DIRECT.equals(config.getRequestWay())){//不用审批
			tbQyMemberInfoPO.setStatus(MemberUtil.STATUS);
			tbQyMemberInfoPO.setApprovePerson(org.getPersonName());
			tbQyMemberInfoPO.setApproveUserId(org.getUserName());
			tbQyMemberInfoPO.setApproveTime(new Date());
			String message=memberService.insertContact(tbQyMemberInfoPO, org,userInfo,tbQyMemberInfoPO.getSelectDeptId());
			if(MemberUtil.TARGER_TYPE_STU.equals(tbQyMemberInfoPO.getType())){//如果类型是学生
				TbDepartmentInfoPO departPO = departmentService.searchByPk(TbDepartmentInfoPO.class, tbQyMemberInfoPO.getSelectDeptId());
				if(DepartmentUtil.ATTRIBUTE_TEACHER == departPO.getAttribute()){//如果是教学班级
					memberService.addOrUpdateStudent(tbQyMemberInfoPO, org,userInfo);//新增或者修改学生到学生表中
				}
			}
			if(!StringUtil.isNullEmpty(message)){
				addJsonObj("message", message);
			}
			memberService.addChildrenEdu(infoPOs, tbQyMemberInfoPO, userInfo, org);
			QwtoolUtil.addBatchList(MemberUtil.setMemberToCustom(customPOList, userInfo), true);
		}else if(MemberUtil.REQUEST_WAY_CHECK.equals(config.getRequestWay())){//审批邀请
			tbQyMemberInfoPO.setStatus(MemberUtil.NO_STATUS);
		}
		addJsonObj("parentId", tbQyMemberInfoPO.getId());
		//给接收成员通知的对象发送消息
		memberService.insertPO(tbQyMemberInfoPO, false);
		QwtoolUtil.addBatchList(infoPOs, false);
		List<TbQyMemberTargetPersonPO> list=memberService.getTargetPerson(config.getId());
		MemberUtil.sendMsgEdu(list, orgPO, tbQyMemberInfoPO,config.getRequestWay());
	}

	/**
	 *	教育版编辑邀请详情
	 * @throws Exception
	 * @throws BaseException
	 * @author liyixin
	 * @2016-12-15
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "保存成功", faileMsg = "修改并审批邀请失败"))
	public void updateMemberEdu(@InterfaceParam(name = "listJson")@Validation(must = false, name = "listJson")String listJson,
								@InterfaceParam(name = "btnType")@Validation(must = false, name = "btnType")String btnType,
								@InterfaceParam(name = "customUserList")@Validation(must = false, name = "customUserList")String customUserList) throws Exception, BaseException {
		TbQyMemberInfoPO po=memberService.searchByPk(TbQyMemberInfoPO.class, tbQyMemberInfoPO.getId());
		if(AssertUtil.isEmpty(po)){
			setActionResult(ErrorCodeDesc.MEMBER_IS_DELE.getCode(), ErrorCodeDesc.MEMBER_IS_DELE.getDesc());
			return;
		}
		UserInfoVO userInfovo = getUser();
		List<TbQyMemberConfigResPO> res= memberService.getDeptList(userInfovo.getOrgId(),po.getRefId());
		//检查状态是否正确
		checkStatus(userInfovo, po, res);
		UserOrgVO org=new UserOrgVO();
		org.setUserId(userInfovo.getUserId());
		org.setOrgId(userInfovo.getOrgId());
		org.setCorpId(userInfovo.getCorpId());
		org.setPersonName(userInfovo.getPersonName());
		if(StringUtil.isNullEmpty(tbQyMemberInfoPO.getSelectDeptId())){//如果部门为空
			//把更新的数据放入po里面
			MemberUtil.setUpdateToPO(po, tbQyMemberInfoPO, res.get(0).getDeptId(), res.get(0).getDept());
		}else{
			//把更新的数据放入po里面
			MemberUtil.setUpdateToPO(po, tbQyMemberInfoPO, tbQyMemberInfoPO.getSelectDeptId(), tbQyMemberInfoPO.getSelectDept());
		}
		List<TbQyMemberInfoPO> list = new ArrayList<TbQyMemberInfoPO>();
		if(!AssertUtil.isEmpty(listJson) ){
			JSONArray jsonArray = JSONArray.fromObject(listJson);
			list = MemberUtil.updateJsonToList(jsonArray, po);
		}
		List<TbQyMemberUserCustomPO> customPOList = new ArrayList<TbQyMemberUserCustomPO>();
		//如果从页面传来的自定义字段设置不为空,而且是金卡vip用户
		if(!AssertUtil.isEmpty(customUserList) && VipUtil.hasGoldPermission(org.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			JSONArray jsonArray = JSONArray.fromObject(customUserList);
			//从jsp页面传来的base转换成的数组
			TbQyMemberUserCustomPO[] customJspVOs = (TbQyMemberUserCustomPO[]) JSONArray.toArray(jsonArray, TbQyMemberUserCustomPO.class);
			customPOList.addAll(Arrays.asList(customJspVOs));
		}
		memberService.updateMemberEdu(btnType, po, org, list, customPOList);
	}

	/**
	 * 检查状态
	 * @param po
	 * @param userInfovo
	 * @throws BaseException
	 * @throws Exception
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	private void checkStatus(UserInfoVO userInfovo,TbQyMemberInfoPO po, List<TbQyMemberConfigResPO> res) throws  BaseException, Exception{
		MemberUtil.checkStatus(po);
		if(AssertUtil.isEmpty(res)){
			throw new BaseException("2003", "对不起,您还没有成员邀请设置！");
		}
	}

	/**
	 * 检查用户是否存在
	 * @throws BaseException
	 * @throws Exception
	 * @author liyixin
	 * @2017-1-4
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1007", successMsg = "查询成功", faileMsg = "查询失败"))
	public void checkUser(@InterfaceParam(name = "id")@Validation(must = true, name = "id")String id,
						  @InterfaceParam(name = "weixinNum")@Validation(must = false, name = "weixinNum")String weixinNum,
						  @InterfaceParam(name = "mobile")@Validation(must = false, name = "mobile")String mobile,
						  @InterfaceParam(name = "email")@Validation(must = false, name = "email")String email,
						  @InterfaceParam(name = "wxUserId")@Validation(must = false, name = "wxUserId")String wxUserId) throws BaseException, Exception{
		TbQyMemberConfigPO config=memberService.searchByPk(TbQyMemberConfigPO.class, id);
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, config.getOrgId());
		TbQyUserInfoPO userInfo=new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(orgPO.getCorpId(), wxUserId));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(weixinNum);
		userInfo.setMobile(mobile);
		userInfo.setEmail(email);
		userInfo.setWxUserId(wxUserId);
		//验证通讯录中是否存在该成员
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(),orgPO.getOrganizationId(),userInfo,"0");
		if(usermsg!=null){
			setActionResult("2011", usermsg);
			return;
		}
	}
}
