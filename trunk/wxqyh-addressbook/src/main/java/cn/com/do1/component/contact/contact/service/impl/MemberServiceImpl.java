package cn.com.do1.component.contact.contact.service.impl;

import java.util.*;

import javax.annotation.Resource;

import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.*;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.util.StudentUitl;
import cn.com.do1.component.contact.student.vo.TbQyStudentInfoVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ShortUrlByBaiduUitl;
import cn.com.do1.component.util.UUID32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberConfigVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberMsgVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.contact.vo.UserRedundancyInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.dao.IMemberDAO;
import cn.com.do1.component.contact.contact.dao.IMemberReadOnlyDAO;
import cn.com.do1.component.contact.contact.service.IMemberService;
import cn.com.do1.component.log.operationlog.util.LogOperation;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.memcached.CacheCrmTypeManager;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;

/**
 * <p>
 * Title: 通讯录管理
 * </p>
 * <p>
 * Description: 类的描述
 * </p>
 * 
 * @author Sun Qinghai
 * @2015-1-20
 * @version 1.0 修订历史： 日期 作者 参考 描述
 */
@Service("memberService")
public class MemberServiceImpl extends BaseService implements IMemberService {
	private final static transient Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	private IMemberDAO memberDAO;
	
	private IDepartmentService departmentService;
	private IContactService contactService;

	private IContactMgrService contactMgrService;

	/**
	 * The Student service.
	 */
	private IStudentService studentService;
	private IPostService postService;

	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}
	@Resource(name = "postService")
	public void setPostService(IPostService postService) {
		this.postService = postService;
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
	
	@Resource
	public void setMemberDao(IMemberDAO memberDAO) {
		this.memberDAO = memberDAO;
		setDAO(memberDAO);
	}
	
	private IMemberReadOnlyDAO memberReadOnlyDAO;
	@Resource
	public void setMemberReadOnlyDAO(IMemberReadOnlyDAO memberReadOnlyDAO) {
		this.memberReadOnlyDAO = memberReadOnlyDAO;
	}

	@Override
	public boolean valid(Object obj) {
		// TODO 自动生成的方法存根
		return false;
	}
	
	@Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	@Resource(name = "departmentService")
    public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Override
	public TbQyMemberConfigVO getHistotryData(String id,String orgId) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		TbQyMemberConfigVO config=memberReadOnlyDAO.getHistotryData(id,orgId);
		if(config != null){
			//待审批 、已通过和未通过的邀请人数
			int notApprove=memberReadOnlyDAO.countPersonNum(id,orgId,MemberHandleUtil.NOT_APPROVED);
			int notPassPerson=memberReadOnlyDAO.countPersonNum(id,orgId,MemberHandleUtil.NOT_APPROVE_PASSED);
			int passPerson=memberReadOnlyDAO.countPersonNum(id,orgId,MemberHandleUtil.APPROVE_PASSED);
			config.setNotApproveNum(notApprove);
			config.setPassPersonNum(passPerson);
			config.setNotPassPersonNum(notPassPerson);
			//获取设置的部门
			List<TbQyMemberConfigResPO> list=this.getDeptList(orgId,id);
			//获取目标通知对象
			List<TbQyMemberTargetPersonPO> userList=this.getTargetPerson(id);
			if(userList.size()>0){
				config.setUsers(userList);
			}
			if(list!=null && list.size()>0){
				config.setList(list);
			}
		}else{
			config=new TbQyMemberConfigVO();
		}
		return config;
	}

	@Override
	public void saveConfig(String orgId,TbQyMemberConfigPO po,String deptId,String dept, List<TbQyMemberCustomConfigPO> customConfigPOList, List<TbQyMemberBaseConfigPO> baseConfigPOList) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		memberDAO.delConfigDes(po.getId());
		TbQyMemberConfigVO config=memberReadOnlyDAO.getHistotryData(po.getId(),orgId);
		if(!AssertUtil.isEmpty(config) && !StringUtil.isNullEmpty(config.getId())){//更新
			po.setId(config.getId());
			po.setCreateTime(config.getCreateTime());
			//如果邀请单名称修改就去更新成员邀请保存的名称
			if(!config.getName().equals(po.getName())){
				memberDAO.updateConfigName(orgId,po.getId(),po.getName());
			}
			updateRecipient(po.getId(), po.getTargetUsers(), "0");
			this.updatePO(po, false);
			List<TbQyMemberCustomConfigPO> oldCusomList = getMemberCustomConfigByMeberId(config.getId());
			List<TbQyMemberBaseConfigPO> oldBaseList = getMemberBaseConfigByMeberId(config.getId());
			List<String> delBaseList = new ArrayList<String>(oldBaseList.size());
			List<String> delCustomList = new ArrayList<String>(oldCusomList.size());
			for(TbQyMemberCustomConfigPO customPO : oldCusomList){
				delCustomList.add(customPO.getId());
			}
			for(TbQyMemberBaseConfigPO basePO : oldBaseList){
				delBaseList.add(basePO.getId());
			}
			QwtoolUtil.delBatchList(TbQyMemberBaseConfigPO.class, delBaseList);
			QwtoolUtil.delBatchList(TbQyMemberCustomConfigPO.class, delCustomList);

		}else{//插入
			po.setStatus(MemberUtil.STATUS);
			updateRecipient(po.getId(), po.getTargetUsers(), "0");
			this.insertPO(po,false);
		}
		List<TbQyMemberConfigResPO> list=new ArrayList<TbQyMemberConfigResPO>();
		if(!StringUtil.isNullEmpty(deptId)){
			TbQyMemberConfigResPO resPO=null;
			String[] ids=deptId.split(",");
			String[] names=dept.split(",");
			for (int i = 0; i < ids.length; i++) {
				resPO=new TbQyMemberConfigResPO();
				resPO.setId(UUID.randomUUID().toString());
				resPO.setConId(po.getId());
				resPO.setDept(names[i]);
				resPO.setDeptId(ids[i]);
				resPO.setOrgId(orgId);
				resPO.setSort(i);
				list.add(resPO);
			}
			if(list.size()>0){
				this.memberDAO.execBatchInsert(list);
			}else{
				throw new NonePrintException("1001", "请选择默认部门！");
			}
		}
		if(!AssertUtil.isEmpty(customConfigPOList)){//如果自定义设置不为空
			for(TbQyMemberCustomConfigPO customPO : customConfigPOList){
				customPO.setId(UUID32.getID());
				customPO.setOrgId(po.getOrgId());
				customPO.setMemberId(po.getId());
			}
			QwtoolUtil.addBatchList(customConfigPOList, true);
		}
		if(!AssertUtil.isEmpty(baseConfigPOList)){//如果基础设置不为空
			for(TbQyMemberBaseConfigPO basePO : baseConfigPOList){
				basePO.setId(UUID32.getID());
				basePO.setOrg_id(po.getOrgId());
				basePO.setMember_id(po.getId());
			}
			QwtoolUtil.addBatchList(baseConfigPOList, true);
		}
	}
	
	/**
	 * 插入通知对象
	 * @param id
	 * @param userIds
	 * @param type
	 * @throws Exception
	 * @throws BaseException
	 */
	public void updateRecipient(String id, String userIds, String type)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		List<TbQyMemberTargetPersonPO> list = memberReadOnlyDAO.getRecipient(id,type);
		if(list!=null&&list.size()>0){
			String[] ids = new String[list.size()];
			for(int i=0;i<list.size();i++){
				ids[i] = list.get(i).getId();
			}
			this.batchDel(TbQyMemberTargetPersonPO.class, ids);
		}
		//插入新数据
		this.insertRecipient(id, userIds, type);
	}

	private void insertRecipient(String id, String userIds, String type) throws Exception, BaseException {
		if(!StringUtil.isNullEmpty(userIds)){
			String[] personlist = userIds.split("\\|");
			Map<String, UserRedundancyInfoVO> map=contactService.getUserRedundancyListByUserId(personlist);
			List<TbQyMemberTargetPersonPO> inlist=new ArrayList<TbQyMemberTargetPersonPO>();
			int temp=1;
			for(String person:personlist){
				CacheCrmTypeManager.remove(person);
				if(AssertUtil.isEmpty(map.get(person))){
					continue;
				}
				UserRedundancyInfoVO user=map.get(person);
				TbQyMemberTargetPersonPO po = new TbQyMemberTargetPersonPO();
				po.setId(UUID.randomUUID().toString());
				po.setForeignId(id);
				po.setType(type);
				po.setUserId(person.trim());
				po.setPersonName(user.getPersonName());
				po.setHeadPic(user.getHeadPic());
				po.setWxUserId(user.getWxUserId());
				po.setDepartmentName(user.getDeptFullName());
				po.setSortNum(temp);
				po.setOrgId(user.getOrgId());
				po.setCreateTime(new Date());
				//this.insertPO(po, true);
				inlist.add(po);
				temp++;
			}
			if(inlist.size()>0){
				memberDAO.execBatchInsert(inlist);
			}
			logger.info("1043", "插入目标通知对象人数"+inlist.size());
		}
	}

	@Override
	public Pager searchMemberInfo(Map searchMap, Pager pager)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.searchMemberInfo(searchMap,pager);
	}

	@Override
	public String insertContact(TbQyMemberInfoPO po,  UserOrgVO org,TbQyUserInfoPO userInfo,String deptId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		userInfo.setPersonName(po.getPersonName().replace(" ",""));
		userInfo.setPinyin(PingYinUtil.getPingYin(po.getPersonName()));
		userInfo.setCreateTime(new Date());
		userInfo.setOrgId(org.getOrgId());
		userInfo.setCreatePerson(org.getUserId());
		userInfo.setUserStatus("0");
		userInfo.setIsConcerned("0");
		userInfo.setCorpId(org.getCorpId());
		userInfo.setHeadPic("0");
		userInfo.setAddress(po.getAddress());
		userInfo.setBirthday(po.getBirthday());
		if(!AssertUtil.isEmpty(po.getBirthday())){
			userInfo.setRemindType(ContactUtil.REMIND_TYPE_ONE);
		}else{
			if(!AssertUtil.isEmpty(po.getLunarCalendar())){
				userInfo.setRemindType(ContactUtil.REMIND_TYPE_ZERO);
			}
		}
		if(AssertUtil.isEmpty(userInfo.getRemindType())){
			userInfo.setRemindType(ContactUtil.REMIND_TYPE_ONE);
		}
		userInfo.setIdentity(po.getIdentity());
		userInfo.setLunarCalendar(po.getLunarCalendar());
		userInfo.setMark(po.getMark());
		userInfo.setNickName(po.getNickName());
		userInfo.setPhone(po.getPhone());
		userInfo.setPosition(po.getPosition());
		userInfo.setQqNum(po.getQqNum());
		userInfo.setSex(po.getSex());
		userInfo.setShorMobile(po.getShorMobile());
		if(!StringUtil.isNullEmpty(po.getRemark())){
			if(!StringUtil.isNullEmpty(po.getCompanyName())){
				userInfo.setMark(po.getRemark()+"-"+po.getCompanyName());
			}else{
				userInfo.setMark(po.getRemark());
			}
		}else{
			userInfo.setMark(po.getCompanyName());
		}
		TbDepartmentInfoPO deptPO= departmentService.searchByPk(TbDepartmentInfoPO.class, deptId.trim());
		if(AssertUtil.isEmpty(deptPO) || AssertUtil.isEmpty(deptPO.getWxId())){
			//没找到对应的部门
			throw new NonePrintException("200","没有选择默认部门或部门已不存在，不能审批通过！");
		}
		if(AssertUtil.isEmpty(deptPO.getWxId())){//如果部门的微信部门id为空
			throw new NonePrintException("200", "部门【"+deptPO.getDepartmentName()+"】存在异常，请尝试删掉此部门，重新新增！");
		}
		List<String> detId = new ArrayList<String>();
		detId.add(deptPO.getId());
		//同步到微信
		String msg= publishToWx(userInfo,deptPO.getWxId(),org.getCorpId());
		contactService.insertUser(userInfo, detId);
		//更新部门人数
		try {
			UserInfoChangeNotifier.addUser(org, userInfo, detId,null, UserInfoChangeInformType.USER_MGR);
		}catch (Exception e){
			logger.error("更新部门人数或者群聊出错"+e);
		}
		return msg;
	}

	//同步数据到微信后台并邀请关注
	public String publishToWx(TbQyUserInfoPO tbQyUserInfoPO,String d,String corpId) throws Exception, BaseException{
		WxUser user = new WxUser();
		user.setUserid(tbQyUserInfoPO.getWxUserId());
		user.setName(tbQyUserInfoPO.getPersonName());
		user.setEmail(tbQyUserInfoPO.getEmail());
		user.setGender(tbQyUserInfoPO.getSex());
		user.setMobile(tbQyUserInfoPO.getMobile());
		user.setPosition(tbQyUserInfoPO.getPosition());
		user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
		List<String> str = new ArrayList<String>();
		str.add(d);
		user.setDepartment(str);
		WxUserService.addUser(user,corpId,tbQyUserInfoPO.getOrgId());
		//邀请用户关注
		String msg="";
		//String msg=WxUserService.inviteUsers(user,corpId);
		return msg;
	}

	@Override
	public TbQyMemberMsgVO verifyMemberInfo(TbQyMemberInfoPO tbQyMemberInfoPO)
			throws Exception, BaseException {
		TbQyMemberMsgVO msgVO=new TbQyMemberMsgVO();
		// TODO 自动生成的方法存根
		// 验证本机构下是否已存在此用户（机构内手机号不能重复）
		if (StringUtil.isNullEmpty(tbQyMemberInfoPO.getWxUserId())) {
			msgVO.setCode("2001");
			msgVO.setMsg("账号信息不能为空");
			return msgVO;
		}
		if (!tbQyMemberInfoPO.getWxUserId().matches("^\\w+[\\w\\-_\\.@]*$")) {
			msgVO.setCode("2001");
			msgVO.setMsg("账号格式有误，请重新输入！");
			return msgVO;
		}
		if (tbQyMemberInfoPO.getWxUserId().length() > 64) {
			msgVO.setCode("2001");
			msgVO.setMsg("账号长度不能超过64个字符！");
			return msgVO;
		}
		if (StringUtil.isNullEmpty(tbQyMemberInfoPO.getMobile()) && StringUtil.isNullEmpty(tbQyMemberInfoPO.getWeixinNum()) && StringUtil.isNullEmpty(tbQyMemberInfoPO.getEmail())) {
			msgVO.setCode("2001");
			msgVO.setMsg("微信号、手机和邮箱三种信息不能同时为空");
			return msgVO;
		}
		if (!StringUtil.isNullEmpty(tbQyMemberInfoPO.getWeixinNum())) {
			int num = memberReadOnlyDAO.countUsersByWeixinNum(tbQyMemberInfoPO.getOrgId(), tbQyMemberInfoPO.getWeixinNum());
			if(num>0){
				msgVO.setCode("1999");
				msgVO.setMsg("该微信号已登记（等待审核中），可与该组织管理员确认或长按以上二维码识别提前关注");
				return msgVO;
			}
		}
		if (!StringUtil.isNullEmpty(tbQyMemberInfoPO.getMobile())) {
			int num = memberReadOnlyDAO.countUsersByMobile(tbQyMemberInfoPO.getOrgId(), tbQyMemberInfoPO.getMobile());
			if(num>0){
				msgVO.setCode("1999");
				msgVO.setMsg("该手机号已登记（等待审核中），可与该组织管理员确认或长按以上二维码识别提前关注");
				return msgVO;
			}
		}
		if (!StringUtil.isNullEmpty(tbQyMemberInfoPO.getEmail())) {
			int num = memberReadOnlyDAO.countUsersByEmail(tbQyMemberInfoPO.getOrgId(), tbQyMemberInfoPO.getEmail());
			if (num>0) {
				msgVO.setCode("1999");
				msgVO.setMsg("该邮箱已登记（等待审核中），可与该组织管理员确认或长按以上二维码识别提前关注");
				return msgVO;
			}
		}
		if (!StringUtil.isNullEmpty(tbQyMemberInfoPO.getWxUserId())) {
			int num = memberReadOnlyDAO.countUsersByWxUserId(tbQyMemberInfoPO.getOrgId(), tbQyMemberInfoPO.getWxUserId());
			if(num>0){
				msgVO.setCode("1999");
				msgVO.setMsg("该账号已登记（等待审核中），可与该组织管理员确认更换或长按以上二维码识别提前关注");
				return msgVO;
			}
		}
		return msgVO;
	}

	@Override
	public void delMember(String[] ids, UserOrgVO userInfoVO) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		for(int i=0;i<ids.length;i++) {
			if (!AssertUtil.isEmpty(ids[i])) {
				TbQyMemberInfoPO po = memberDAO.searchByPk(TbQyMemberInfoPO.class,ids[i]);
				if(AssertUtil.isEmpty(po)){
					break;
				}
				if (!userInfoVO.getOrgId().equals(po.getOrgId())) {
					break;
				}
				List<TbQyMemberInfoPO> list =  memberReadOnlyDAO.searchChildren(po.getId());
				list.add(po);
				List<String> deleList = new ArrayList<String>(list.size());
				for(int j = 0; j < list.size(); j++){
					deleList.add(list.get(j).getId());
				}
				memberDAO.deleteByPks(TbQyMemberInfoPO.class, deleList);
				LogOperation.insertOperationLog(userInfoVO.getUserName(), userInfoVO.getPersonName(), "删除邀请成员"+po.getPersonName()+"成功", "del", "addressBook", userInfoVO.getOrgId(), "删除成功");
			}
		}
	}

	@Override
	public List<TbQyMemberConfigResPO> getDeptList(String orgId,String id)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.getDeptList(orgId,id);
	}

	@Override
	public Pager seachMemberList(Map map, Pager pager)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.seachMemberList(map,pager);
	}

	@Override
	public List<TbQyMemberConfigVO> getConfigList(String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.getConfigList(orgId);
	}

	@Override
	public void delMemberConfigDes(String ids) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		memberDAO.delConfigDes(ids);
	}

	@Override
	public List<TbQyMemberConfigPO> getAllConfig() throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.getAllConfig();
	}

	@Override
	public List<TbQyMemberTargetPersonPO> getTargetPerson(String id)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return memberReadOnlyDAO.getRecipient(id, "0");
	}

	/**
	 * 把孩子添加到学生表中
	 *@param tbQyMemberInfoPO 父母
	 * @param userInfo 父母信息
	 * @param infoPOs 孩子列表
	 * @throws Exception
	 * @throws BaseException
	 */
	public	void addChildrenEdu(List<TbQyMemberInfoPO> infoPOs, TbQyMemberInfoPO tbQyMemberInfoPO,TbQyUserInfoPO userInfo, UserOrgVO org)throws Exception, BaseException{
		List<TbQyStudentInfoPO> addStud = new ArrayList<TbQyStudentInfoPO>();
		List<TbQyStudentInfoPO> updateStud = new ArrayList<TbQyStudentInfoPO>();
		List<TbQyUserStudentRefPO> addRef = new ArrayList<TbQyUserStudentRefPO>();
		for(int i = 0; i < infoPOs.size(); i ++){
			TbQyStudentInfoPO student = new TbQyStudentInfoPO();
			TbQyUserStudentRefPO refPO = new TbQyUserStudentRefPO();
			List<TbQyStudentInfoVO> infoVOs = new ArrayList<TbQyStudentInfoVO>();
			MemberUtil.setInfoToStu(student, refPO, infoPOs.get(i), org, infoVOs);
			refPO.setUserId(userInfo.getUserId());
			if(infoVOs.size() == 1){//如果原来有这个学生
				updateStud.add(student);
				addRef.add(refPO);
			}else{
				addStud.add(student);
				addRef.add(refPO);
			}
		}
		if(addStud.size() > 0) {
			memberDAO.execBatchInsert(addStud);
		}
		if(addRef.size() > 0) {
			memberDAO.execBatchInsert(addRef);
		}
		if(updateStud.size() > 0) {
			memberDAO.execBatchUpdate(updateStud, false);
		}
	}

	/**
	 * 通过父母id查找po
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	 public TbQyMemberInfoPO searchByParentId(String parentId) throws Exception, BaseException{
		return memberReadOnlyDAO.searchByParentId(parentId);
	}

	/**
	 *新增或者修改学生到学生表中
	 * @param po po
	 * @param org 机构信息
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	public void addOrUpdateStudent(TbQyMemberInfoPO po, UserOrgVO org, TbQyUserInfoPO userInfo) throws BaseException, Exception{
		TbQyStudentInfoPO student = new TbQyStudentInfoPO();
		TbQyUserStudentRefPO refPO = new TbQyUserStudentRefPO();
		MemberUtil.updateStatus(po, org);//更新邀请单状态
		List<TbQyStudentInfoVO> infoVOs = new ArrayList<TbQyStudentInfoVO>();
		MemberUtil.setInfoToStu(student, refPO, po,org, infoVOs);
		student.setHasParent(StudentUitl.no_parent);
		student.setIsSyn(1);
		student.setId(userInfo.getUserId());
		student.setRegisterPhone(userInfo.getMobile());
		memberDAO.insert(student);
		memberDAO.update(po, false);
	}

	/**
	 *新增或者修改学生到学生表中
	 * @param po po
	 * @param org 机构信息
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-11-28
	 * @version 1.0
	 */
	public void addOrUpdateStudent(TbQyMemberInfoPO po, UserOrgVO org, String deptId, List<TbQyStudentInfoPO> addStud, List<TbQyStudentInfoPO> updateStud, List<TbQyUserStudentRefPO> addRef) throws BaseException, Exception{
		if(AssertUtil.isEmpty(deptId)){
			deptId = po.getSelectDeptId();
		}
		TbQyStudentInfoPO student = new TbQyStudentInfoPO();
		TbQyUserStudentRefPO refPO = new TbQyUserStudentRefPO();
		MemberUtil.updateStatus(po, org);//更新邀请单状态
		List<TbQyStudentInfoVO> infoVOs = new ArrayList<TbQyStudentInfoVO>();
		MemberUtil.setInfoToStu(student, refPO, po,org, infoVOs);
		if(infoVOs.size() == 1){//如果原来有这个学生
			updateStud.add(student);
			addRef.add(refPO);
		}else{
			addStud.add(student);
			addRef.add(refPO);
		}
	}

	/**
	 * 批量审批通过
	 * @param list 邀请列表
	 * @param deptId 部门id
	 * @param orgVO 机构id
	 * @throws BaseException
     * @throws Exception
     */
	public void batchApproveEdu(List<TbQyMemberInfoPO> list, String deptId, UserOrgVO orgVO, Map<String, List<TbQyMemberUserCustomPO>> map) throws BaseException, Exception{
		List<TbQyStudentInfoPO> addStud = new ArrayList<TbQyStudentInfoPO>();
		List<TbQyStudentInfoPO> updateStud = new ArrayList<TbQyStudentInfoPO>();
		List<TbQyUserStudentRefPO> addRef = new ArrayList<TbQyUserStudentRefPO>();
		List<TbQyMemberInfoPO> updateMem = new ArrayList<TbQyMemberInfoPO>(list.size());
		List<TbQyUserCustomItemPO> customList = new ArrayList<TbQyUserCustomItemPO>();
		for(TbQyMemberInfoPO po : list) {
			String departId = deptId;
			if(AssertUtil.isEmpty(deptId)){//如果是空
				departId = po.getSelectDeptId();
			}
			if(AssertUtil.isEmpty(po)){
				break;
			}
			if(!"0".equals(po.getStatus())){
				break;
			}
			if(MemberUtil.TARGER_TYPE_STU.equals(po.getType()) && !AssertUtil.isEmpty(po.getRelation())){
				this.addOrUpdateStudent(po, orgVO, departId, addStud, updateStud, addRef);//新增或者修改学生到学生表中
			}else {
				List<TbQyMemberInfoPO> childrenList = searchChildren(po.getId());//获取父母填写的孩子
				for (TbQyMemberInfoPO childrenPO : childrenList) {
					this.addOrUpdateStudent(childrenPO, orgVO, null, addStud, updateStud, addRef);//新增或者修改孩子到学生表中
					MemberUtil.updateStatus(childrenPO, orgVO);
				}
				if (childrenList.size() > 0) {
					updateMem.addAll(childrenList);
				}
				TbQyUserInfoPO userInfo = new TbQyUserInfoPO();
				userInfo.setId(ContactUtil.getUserId(orgVO.getCorpId(), po.getWxUserId()));
				userInfo.setUserId(userInfo.getId());
				userInfo.setWeixinNum(po.getWeixinNum());
				userInfo.setMobile(po.getMobile());
				userInfo.setEmail(po.getEmail());
				userInfo.setWxUserId(po.getWxUserId());
				MemberUtil.setAttributeToUser(userInfo, po);
				//验证通讯录中是否存在该成员，存在不予提交
				ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, orgVO.getOrgId());
				//成员邀请的调用验证通讯录中是否存在
				String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(), orgPO.getOrganizationId(), userInfo, "1");
				if (usermsg != null) {
					break;
				}
				//通过审批把邀请成员插入到通讯录
				this.insertContact(po, orgVO, userInfo, departId);
				if (MemberUtil.TARGER_TYPE_STU.equals(po.getType())) {//如果类型是学生
					TbDepartmentInfoPO departPO = departmentService.searchByPk(TbDepartmentInfoPO.class, deptId);
					if (DepartmentUtil.ATTRIBUTE_TEACHER == departPO.getAttribute()) {//如果是教学班级
						this.addOrUpdateStudent(po, orgVO, userInfo);//新增或者修改学生到学生表中
					}
				}
				customList.addAll(MemberUtil.setMemberToCustom(map.get(po.getId()), userInfo));
			}
			MemberUtil.updateStatus(po, orgVO);
			updateMem.add(po);
		}
		if(addStud.size() > 0) {
			memberDAO.execBatchInsert(addStud);
		}
		if(addRef.size() > 0) {
			memberDAO.execBatchInsert(addRef);
		}
		if(updateMem.size() > 0) {
			memberDAO.execBatchUpdate(updateMem, false);
		}
		if(updateStud.size() > 0) {
			memberDAO.execBatchUpdate(updateStud, false);
		}
		QwtoolUtil.addBatchList(customList, true);
	}

	/**
	 * 查找该监护人填写的孩子
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	public List<TbQyMemberInfoPO> searchChildren(String parentId) throws Exception, BaseException{
		return memberReadOnlyDAO.searchChildren(parentId);
	}

	/**
	 * 查找该监护人填写的孩子
	 * @param parentId 父母id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-6
	 * @version 1.0
	 */
	public List<TbQyMemberInfoVO> searchChildrenToVO(String parentId) throws Exception, BaseException{
		return memberReadOnlyDAO.searchChildrenToVO(parentId);
	}

	/**
	 *教育版邀请详情编辑
	 * @param btnType 点击的事件
	 * @param po 审批单填写的用户的po
	 * @param org 机构信息
	 * @param list 孩子列表
	 *  @param customPOList 自定义字段列表
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-7
	 * @version 1.0
	 */
	public void updateMemberEdu(String btnType, TbQyMemberInfoPO po, UserOrgVO org, List<TbQyMemberInfoPO> list, List<TbQyMemberUserCustomPO> customPOList) throws Exception, BaseException{
		List<TbQyMemberUserCustomPO> oldCustomList = getMemberUserCustom(po.getId());
		if(!AssertUtil.isEmpty(oldCustomList)){
			List<String> delList = new ArrayList<String>(oldCustomList.size());
			for(TbQyMemberUserCustomPO customPO : oldCustomList){
				delList.add(customPO.getId());
			}
			QwtoolUtil.delBatchList(TbQyMemberUserCustomPO.class, delList);
		}
		if(!AssertUtil.isEmpty(customPOList)){
			for(TbQyMemberUserCustomPO customPO : customPOList){
				customPO.setMemberInfoId(po.getId());
				customPO.setOrgId(po.getOrgId());
				customPO.setId(UUID32.getID());
			}
			QwtoolUtil.addBatchList(customPOList, true);
		}
		if(MemberUtil.UN_PASS.equals(btnType)){//如果是不通过
			this.batchDisagree(po, org, list);
			return;
		}
		TbQyUserInfoPO userInfo = new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(org.getCorpId(), po.getWxUserId()));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(po.getWeixinNum());
		userInfo.setMobile(po.getMobile());
		userInfo.setEmail(po.getEmail());
		userInfo.setWxUserId(po.getWxUserId());
		MemberUtil.setAttributeToUser(userInfo, po);
		Set<String> postSet = postService.getPositionSetByOrgId(org.getOrgId());
		if(!AssertUtil.isEmpty(po.getPosition())){
			if(!postSet.contains(po.getPosition())){//如果该职位不存在
				po.setPosition("");
			}
		}
		//验证通讯录中是否存在该成员，存在不予提交
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, po.getOrgId());
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(), orgPO.getOrganizationId(), userInfo, "1");
		if (null != usermsg) {
			throw new BaseException("2011", usermsg);
		}
		if (!StringUtil.isNullEmpty(btnType) && MemberUtil.PASS.equals(btnType)) {//如果是保存并通过
			//通过审批把邀请成员插入到通讯录
			//验证通讯录中是否存在该成员，存在不予提交
			insertContact(po, org, userInfo, po.getSelectDeptId());
			if (MemberUtil.TARGER_TYPE_STU.equals(po.getType())) {//如果类型是学生
				TbDepartmentInfoPO departPO = departmentService.searchByPk(TbDepartmentInfoPO.class, po.getSelectDeptId());
				if (DepartmentUtil.ATTRIBUTE_TEACHER == departPO.getAttribute()) {//如果是教学班级
					addOrUpdateStudent(po, org, userInfo);//新增或者修改学生到学生表中
				}
			}
			MemberUtil.updateStatus(po, org);
			this.batchApproveEdu(list, null, org, new HashMap<String, List<TbQyMemberUserCustomPO>>());
			for (TbQyMemberInfoPO childrenPo : list) {//批量修改
				MemberUtil.updateStatus(childrenPo, org);
			}
			QwtoolUtil.addBatchList(MemberUtil.setMemberToCustom(customPOList, userInfo), true);
		}
		memberDAO.update(po, false);
		if (list.size() > 0) {
			memberDAO.execBatchUpdate(list);
		}
	}

	/**
	 * 批量查询邀请列表成员
	 * @param ids 信息表id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-7
	 * @version 1.0
	 */
	public 	List<TbQyMemberInfoPO> batchPObyId(String ids[]) throws Exception, BaseException{
		return memberReadOnlyDAO.batchPObyId(ids);
	}

	/**
	 * 批量不通过邀请单
	 * @param po
	 * @param org
	 * @param list
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-15
	 * @version 1.0
	 */
	public void batchDisagree(TbQyMemberInfoPO po,UserOrgVO org, List<TbQyMemberInfoPO> list) throws BaseException, Exception{
		list.add(po);
		for(TbQyMemberInfoPO updatePO : list){
			updatePO.setStatus("2");
			updatePO.setApprovePerson(org.getPersonName());
			updatePO.setApproveUserId(org.getUserId());
			updatePO.setApproveTime(new Date());
		}
		if(list.size() > 0){//批量更新
			memberDAO.execBatchUpdate(list, false);
		}
	}

	/**
	 *查询展示在首页的邀请单
	 * @param orgId
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
	 */
	 public List<TbQyMemberConfigVO> showIndex(String orgId) throws Exception, BaseException{
		return memberReadOnlyDAO.showIndex(orgId);
	}

	/**
	 * 创建默认的邀请单
	 * @param orgVO
	 * @param isEdu 是否是教育版
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
	 */
	public List<TbQyMemberConfigVO> addDefaultConfig(UserOrgVO orgVO, boolean isEdu) throws BaseException, Exception{
		List<TbQyMemberConfigVO> configVOs = new ArrayList<TbQyMemberConfigVO>(1);
		List<TbDepartmentInfoVO> departVOs ;
		TbQyMemberConfigPO tbQyMemberConfigPO = new TbQyMemberConfigPO();
		tbQyMemberConfigPO.setId(UUID.randomUUID().toString());
		String url;
		if(isEdu){//如果是教育版
			departVOs = departmentService.getAllEduDept(orgVO.getOrgId());//所有班级部门
			if(departVOs.size() == 0){//如果所有班级部门为空
				departVOs = departmentService.getFirstDepart(orgVO.getOrgId());//所有一级部门
			}
			url = Configuration.OPEN_PORT + "/open/memberEdu/add.jsp?id=" + tbQyMemberConfigPO.getId();
			tbQyMemberConfigPO.setIsAllow(MemberUtil.IS_ALLOW);
			tbQyMemberConfigPO.setTargetType(MemberUtil.TYPE_PARENTS);
			tbQyMemberConfigPO.setIsCheck(MemberUtil.NO_IS_CHECK);
		}else{//普通版
			departVOs = departmentService.getFirstDepart(orgVO.getOrgId());//所有一级部门
			url = Configuration.OPEN_PORT + "/open/member/add.jsp?id=" + tbQyMemberConfigPO.getId();
		}
		tbQyMemberConfigPO.setOrgId(orgVO.getOrgId());
		tbQyMemberConfigPO.setOrgName(orgVO.getOrgName());
		tbQyMemberConfigPO.setCreateTime(new Date());
		tbQyMemberConfigPO.setUpdateTime(new Date());
		tbQyMemberConfigPO.setCreator(orgVO.getUserName());
		tbQyMemberConfigPO.setWebsite(ShortUrlByBaiduUitl.getShortUrl(url,"addressBook",orgVO.getCorpId()));
		tbQyMemberConfigPO.setRequestWay(MemberUtil.REQUEST_WAY_DIRECT);
		//勾选中部门
		tbQyMemberConfigPO.setCustom("a4");
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		cal.add(Calendar.YEAR, 2);//过期时间为2年
		tbQyMemberConfigPO.setStartTime(new Date());
		tbQyMemberConfigPO.setStopTime(cal.getTime());
		tbQyMemberConfigPO.setName("邀请单");
		tbQyMemberConfigPO.setSort(0);
		tbQyMemberConfigPO.setStatus(MemberUtil.STATUS);
		tbQyMemberConfigPO.setIsDefault(MemberUtil.IS_DEFALUT);
		tbQyMemberConfigPO.setShowIndex(MemberUtil.SHOW_INDEX);
		List<TbQyMemberConfigResPO> list=new ArrayList<TbQyMemberConfigResPO>();
		//插入邀请单的部门到res表中
		for (int i = 0; i < departVOs.size(); i++) {
			TbQyMemberConfigResPO resPO=new TbQyMemberConfigResPO();
			resPO.setId(UUID.randomUUID().toString());
			resPO.setConId(tbQyMemberConfigPO.getId());
			resPO.setDept(departVOs.get(i).getDepartmentName());
			resPO.setDeptId(departVOs.get(i).getId());
			resPO.setOrgId(orgVO.getOrgId());
			resPO.setSort(i);
			list.add(resPO);
		}
		if(list.size()>0){
			this.memberDAO.execBatchInsert(list);
		}else{
			return configVOs;
		}
		this.insertPO(tbQyMemberConfigPO,false);
		//把po的信息复制到vo里面去，然后返回回去
		TbQyMemberConfigVO configVO = new TbQyMemberConfigVO();
		BeanHelper.copyBeanProperties(configVO, tbQyMemberConfigPO);
		configVOs.add(configVO);
		return configVOs;
	}

	/**
	 *查询所有展示在首页的邀请单
	 * @param orgId
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2016-12-27
	 * @version 1.0
	 */
	public List<TbQyMemberConfigPO> selectAllIndex(String orgId) throws Exception, BaseException{
		return memberReadOnlyDAO.selectAllIndex(orgId);
	}

	/**
	 * 通过邀请单id查询对应自定义字段的设置
	 * @param memberId 邀请单id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	public List<TbQyMemberCustomConfigPO> getMemberCustomConfigByMeberId(String memberId) throws BaseException, Exception{
		if(AssertUtil.isEmpty(memberId)){
			return new ArrayList<TbQyMemberCustomConfigPO>();
		}else{
			return memberReadOnlyDAO.getMemberCustomConfigByMeberId(memberId);
		}
	}

	/**
	 * 通过邀请单id查询对应基础字段的设置
	 * @param memberId 邀请单id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	public List<TbQyMemberBaseConfigPO> getMemberBaseConfigByMeberId(String memberId) throws BaseException, Exception{
		if(AssertUtil.isEmpty(memberId)){
			return new ArrayList<TbQyMemberBaseConfigPO>();
		}else{
			return memberReadOnlyDAO.getMemberBaseConfigByMeberId(memberId);
		}
	}

	/**
	 * 通过用户填写的邀请单的id获取填写的自定义的列表
	 * @param memberInfoId 用户填写的邀请单的id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-12
	 * @version 1.0
	 */
	public List<TbQyMemberUserCustomPO> getMemberUserCustom(String memberInfoId) throws BaseException, Exception{
		if(AssertUtil.isEmpty(memberInfoId)){
			return new ArrayList<TbQyMemberUserCustomPO>();
		}else{
			return memberReadOnlyDAO.getMemberUserCustom(memberInfoId);
		}
	}

	/**
	 *邀请详情编辑
	 * @param btnType 点击的事件
	 * @param po 审批单填写的用户的po
	 * @param org 机构信息
	 * @param customPOList 自定义字段列表
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-4-14
	 * @version 1.0
	 */
	public void updateMember(String btnType, TbQyMemberInfoPO po, UserOrgVO org, List<TbQyMemberUserCustomPO> customPOList) throws Exception, BaseException{
		List<TbQyMemberUserCustomPO> oldCustomList = getMemberUserCustom(po.getId());
		if(oldCustomList.size() > 0){
			List<String> delList = new ArrayList<String>(oldCustomList.size());
			for(TbQyMemberUserCustomPO customPO : oldCustomList){
				delList.add(customPO.getId());
			}
			QwtoolUtil.delBatchList(TbQyMemberUserCustomPO.class, delList);
		}
		if(!AssertUtil.isEmpty(customPOList)){
			for(TbQyMemberUserCustomPO customPO : customPOList){
				customPO.setId(UUID32.getID());
				customPO.setOrgId(po.getOrgId());
				customPO.setMemberInfoId(po.getId());
			}
			QwtoolUtil.addBatchList(customPOList, true);
		}
		if(MemberUtil.UN_PASS.equals(btnType)){//如果是不通过
			this.batchDisagree(po, org, new ArrayList<TbQyMemberInfoPO>());
			return;
		}
		TbQyUserInfoPO userInfo = new TbQyUserInfoPO();
		userInfo.setId(ContactUtil.getUserId(org.getCorpId(), po.getWxUserId()));
		userInfo.setUserId(userInfo.getId());
		userInfo.setWeixinNum(po.getWeixinNum());
		userInfo.setMobile(po.getMobile());
		userInfo.setEmail(po.getEmail());
		userInfo.setWxUserId(po.getWxUserId());
		Set<String> postSet = postService.getPositionSetByOrgId(org.getOrgId());
		if(!AssertUtil.isEmpty(po.getPosition())){
			if(!postSet.contains(po.getPosition())){//如果该职位不存在
				po.setPosition("");
			}
		}
		//验证通讯录中是否存在该成员，存在不予提交
		ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, po.getOrgId());
		String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(), orgPO.getOrganizationId(), userInfo, "1");
		if (null != usermsg) {
			throw new BaseException(ErrorCodeDesc.system_error.getCode(), usermsg);
		}
		if (!StringUtil.isNullEmpty(btnType) && MemberUtil.PASS.equals(btnType)) {//如果是保存并通过
			//通过审批把邀请成员插入到通讯录
			//验证通讯录中是否存在该成员，存在不予提交
			insertContact(po, org, userInfo, po.getSelectDeptId());
			MemberUtil.updateStatus(po, org);
			List<TbQyUserCustomItemPO> list = MemberUtil.setMemberToCustom(customPOList, userInfo);
			QwtoolUtil.addBatchList(list, true);
		}
		memberDAO.update(po, false);
	}

	/**
	 * 批量通过用户填写的邀请单的id获取填写的自定义的列表
	 * @param memberInfoIds 用户填写的邀请单的ids
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-14
	 * @version 1.0
	 */
	public Map<String, List<TbQyMemberUserCustomPO>> batchMemberUserCustom(List<String> memberInfoIds) throws BaseException, Exception{
		Map<String, List<TbQyMemberUserCustomPO>> map = new HashMap<String, List<TbQyMemberUserCustomPO>>();
		if(!AssertUtil.isEmpty(memberInfoIds)){
			List<TbQyMemberUserCustomPO> customPOs = memberReadOnlyDAO.batchMemberUserCustom(memberInfoIds);
			List<TbQyMemberUserCustomPO> mapList;
			for(TbQyMemberUserCustomPO customPO : customPOs){
				//如果map原来没有
				if( !map.containsKey(customPO.getMemberInfoId()) ){
					mapList = new ArrayList<TbQyMemberUserCustomPO>();
					mapList.add(customPO);
					map.put(customPO.getMemberInfoId(), mapList);
				}else{
					mapList = map.get(customPO.getMemberInfoId());
					mapList.add(customPO);
					map.put(customPO.getMemberInfoId(), mapList);
				}
			}
		}
		return map;
	}

	/**
	 * 批量审批通过
	 * @param list 邀请列表
	 * @param deptId 部门id
	 * @param orgVO 机构id
	 * @param map 自定义字段
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-04-14
	 * @version 1.0
	 */
	public void batchApprove(List<TbQyMemberInfoPO> list, String deptId, UserOrgVO orgVO, Map<String, List<TbQyMemberUserCustomPO>> map ) throws BaseException, Exception{
		List<TbQyMemberInfoPO> updateMem = new ArrayList<TbQyMemberInfoPO>(list.size());
		List<TbQyUserCustomItemPO> customList = new ArrayList<TbQyUserCustomItemPO>();
		for(TbQyMemberInfoPO po : list) {
			String departId = deptId;
			if(AssertUtil.isEmpty(deptId)){//如果是空
				departId = po.getSelectDeptId();
			}
			if(AssertUtil.isEmpty(po)){
				break;
			}
			TbQyUserInfoPO userInfo = new TbQyUserInfoPO();
			userInfo.setId(ContactUtil.getUserId(orgVO.getCorpId(), po.getWxUserId()));
			userInfo.setUserId(userInfo.getId());
			userInfo.setWeixinNum(po.getWeixinNum());
			userInfo.setMobile(po.getMobile());
			userInfo.setEmail(po.getEmail());
			userInfo.setWxUserId(po.getWxUserId());
			//验证通讯录中是否存在该成员，存在不予提交
			ExtOrgPO orgPO = departmentService.searchByPk(ExtOrgPO.class, orgVO.getOrgId());
			//成员邀请的调用验证通讯录中是否存在
			String usermsg = contactMgrService.verifyUserInfo(orgPO.getCorpId(), orgPO.getOrganizationId(), userInfo, "1");
			if (usermsg != null) {
				break;
			}
			//通过审批把邀请成员插入到通讯录
			this.insertContact(po, orgVO, userInfo, departId);
			MemberUtil.updateStatus(po, orgVO);
			updateMem.add(po);
			customList.addAll(MemberUtil.setMemberToCustom(map.get(po.getId()), userInfo));
		}
		QwtoolUtil.updateBatchList(updateMem, true);
		QwtoolUtil.addBatchList(customList, true);
	}
}

