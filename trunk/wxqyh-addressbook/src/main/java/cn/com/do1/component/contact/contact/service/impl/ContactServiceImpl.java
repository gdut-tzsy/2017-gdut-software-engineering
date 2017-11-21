package cn.com.do1.component.contact.contact.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.com.do1.common.exception.*;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.util.ContactDictUtil;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.UserEduUtil;
import cn.com.do1.component.contact.department.util.DepartmentDictUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.log.operationlog.util.BatchAddLoginlogThread;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.managesetting.managesetting.util.ManagesettingCacheUtil;
import cn.com.do1.component.qwapiclient.AddressBookUtil;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.component.util.memcached.*;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.contact.dao.IContactDAO;
import cn.com.do1.component.contact.contact.dao.IContactInsideReadOnlyDAO;
import cn.com.do1.component.contact.contact.dao.IContactReadOnlyDAO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;
import cn.com.do1.component.log.operationlog.model.TbQyOperationLogPO;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbConfigOrgAesPO;
import cn.com.do1.component.systemmgr.user.model.TbDqdpUserPO;
import cn.com.do1.component.util.memcached.CacheDqdpOrgManager;
import cn.com.do1.component.util.memcached.CacheDqdpUserManager;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.util.memcached.CacheWxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.component.wxcgiutil.token.AccessToken;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;

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
@Service("contactService")
public class ContactServiceImpl extends BaseService implements IContactService, IContactMgrService {
	private final static transient Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

	private IContactDAO contactDAO;
	private IDepartmentService departmentService;
	private IDepartmentMgrService departmentMgrService;

	private IContactReadOnlyDAO contactReadOnlyDAO;
	private IContactInsideReadOnlyDAO contactInsideReadOnlyDAO;
	private IDefatgroupMgrService defatgroupMgrService;
	private IContactCustomMgrService contactCustomMgrService;

	@Resource(name = "contactCustomService")
	public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService){
		this.contactCustomMgrService = contactCustomMgrService;
	}

	@Resource
	public void setContactReadOnlyDAO(IContactReadOnlyDAO contactReadOnlyDAO) {
		this.contactReadOnlyDAO = contactReadOnlyDAO;
	}

	@Resource
	public void setContactInsideReadOnlyDAO(
			IContactInsideReadOnlyDAO contactInsideReadOnlyDAO) {
		this.contactInsideReadOnlyDAO = contactInsideReadOnlyDAO;
	}

	@Resource
	public void setContactDAO(IContactDAO contactDAO) {
		this.contactDAO = contactDAO;
		setDAO(contactDAO);
	}

	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
    @Resource(name = "defatgroupService")
    public void setDefatgroupMgrService(IDefatgroupMgrService defatgroupMgrService) {
        this.defatgroupMgrService = defatgroupMgrService;
    }

	/**
	 * @param departmentMgrService 要设置的 departmentMgrService
	 */
	@Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	public Pager searchContact(Map searchMap, Pager pager) throws Exception, BaseException {
		//
		if (!AssertUtil.isEmpty(searchMap.get("currDept")) && "1".equals(searchMap.get("currDept"))) {
			// 搜索部门成员，【不包括子部门】
			searchMap.remove("currDept");// 去掉判断参数
			return contactDAO.searchContactByDeptId(searchMap, pager);// 增加部门参数
		} else {
			if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
				// 搜索部门成员，【包括子部门】
				TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
				if (department == null) {
					return pager;
				}
				// Map map=new Map();
				searchMap.put("department", department.getDeptFullName() + "->%");
				// searchMap.remove("deptId");
				return contactDAO.searchContact(searchMap, pager);
			} else {
				//搜索所有成员
				return contactDAO.searchContactByOrgId(searchMap, pager);
			}
		}
	}

	@Override
	public List<TbQyUserInfoPO> searchPersonByName(String orgId, String name) throws Exception, BaseException {
		return contactDAO.searchPersonByName(orgId, name);
	}

	@Override
	public List<TbQyUserInfoPO> findUsersByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.findUsersByOrgId(orgId);
	}

	@Override
	public int findUsersCountByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.findUsersCountByOrgId(orgId);
	}

	@Override
	public int findDeptUserCountByUserId(String orgId, String deptFullName) throws Exception, BaseException {
		return contactDAO.findDeptUserCountByUserId(orgId, deptFullName);
	}

	@Override
	public List<TbQyUserInfoVO> findUsersByUserNameOrPhone(UserInfoVO user, Map<String,Object> map) throws Exception, BaseException {
		String orgId = user.getOrgId();
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			return contactDAO.findUsersByUserNameOrPhone(orgId, map, depts);
		} else {
			return contactDAO.findUsersByUserNameOrPhone(orgId, map, null);
		}
	}

	@Override
	public TbQyUserInfoVO findUserInfoByUserId(String userId) throws Exception, BaseException {
		// 如果session中已经有登录人的用户信息，不用再次重新查询
		/*
		 * HttpSession session = null; try { session = ServletActionContext.getRequest().getSession(); } catch (Exception e) { //logger.error("获取session失败",e); } if(session==null){ return
		 * contactDAO.findUserInfoByUserId(userId); } String myUserId = (String)session.getAttribute("userId"); if(!StringUtil.isNullEmpty(myUserId) && userId.equals(myUserId)){ TbQyUserInfoVO userVO
		 * = (TbQyUserInfoVO)session.getAttribute("userInfoVO"); if(userVO==null){ userVO = contactDAO.findUserInfoByUserId(userId); session.setAttribute("userInfoVO", userVO); } return userVO; }
		 * else{ return contactDAO.findUserInfoByUserId(userId); }
		 */
		return contactDAO.findUserInfoByUserId(userId);
	}

	@Override
	public TbQyUserInfoPO findUserInfoPOByUserId(String userId) throws Exception, BaseException {
		return contactDAO.findUserInfoPOByUserId(userId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUserDeptByUserId(java.lang.String)
	 */
	@Override
	public UserDeptInfoVO findUserDeptByUserId(String userId) throws Exception, BaseException {
		return contactDAO.findUserDeptByUserId(userId);
	}

	@Override
	public List<UserDeptInfoExpandVO> findUserDeptByUserIds(String[] userIds) throws Exception, BaseException {
		if (userIds == null || userIds.length == 0) {
			return new ArrayList<UserDeptInfoExpandVO>();
		}
		int size = userIds.length ;
		if (size <= cn.com.do1.component.util.Configuration.SQL_IN_MAX) {
			return  contactDAO.findUserDeptByUserIds(userIds);
		}
		String[] searchUserIds = new String[200];
		int startIndex = 0;
		List<UserDeptInfoExpandVO> values=new ArrayList<UserDeptInfoExpandVO>(size);
		while (startIndex + cn.com.do1.component.util.Configuration.SQL_IN_MAX <= size) {
			System.arraycopy(userIds, startIndex, searchUserIds, 0, cn.com.do1.component.util.Configuration.SQL_IN_MAX);
			values.addAll(contactDAO.findUserDeptByUserIds(searchUserIds));
			startIndex += cn.com.do1.component.util.Configuration.SQL_IN_MAX;
		}
		if (startIndex < size) {
			searchUserIds = new String[size - startIndex];
			System.arraycopy(userIds, startIndex, searchUserIds, 0, size - startIndex);
			values.addAll(contactDAO.findUserDeptByUserIds(searchUserIds));
		}
		return values;
	}

	/*
         * （非 Javadoc）
         * @see cn.com.do1.component.contact.contact.service.IContactService#findUserDeptInfoByUserId(java.lang.String)
         */
	@Override
	public UserDeptInfoVO findUserDeptInfoByUserId(String userId) throws Exception, BaseException {
		return contactDAO.findUserDeptInfoByUserId(userId);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, String deptFullName) throws Exception, BaseException {
		return contactDAO.findDeptUserAllByDept(orgId, deptFullName);
	}

	@Override
	public UserOrgVO getOrgByUserId(String userName) throws Exception, BaseException {
		if (StringUtil.isNullEmpty(userName)) {
			return null;
		}
		if (Configuration.IS_USE_MEMCACHED) {
			UserOrgVO vo = (UserOrgVO) CacheDqdpUserManager.get(userName);
			if (vo == null) {
				vo = contactDAO.getOrgByUserId(userName);
				if(vo!= null && vo.getAge()==null){
					vo.setAge(0);
				}
				CacheDqdpUserManager.set(userName, vo);
			}
			if(vo!=null){
				DqdpOrgVO org = this.getOrgByOrgId(vo.getOrgId());
				if(org!=null){
					vo.setOrgName(org.getOrgName());
					vo.setCorpId(org.getCorpId());
					vo.setWxId(org.getWxId());
					vo.setWxParentid(org.getWxParentid());
				}
			}
			return vo;
		} else {
			// 如果session中已经有登录人的用户信息，不用再次重新查询
			HttpSession session = null;
			try {
				session = ServletActionContext.getRequest().getSession();
			} catch (Exception e) {
				// logger.error("获取session失败",e);
			}
			if (session == null) {
				return contactDAO.getOrgByUserId(userName);
			}
			UserOrgVO vo = (UserOrgVO) session.getAttribute("dqdp_user_org");
			if (null != vo && userName.equals(vo.getUserName())) {
				return vo;
			} else {
				vo = contactDAO.getOrgByUserId(userName);
				if(vo!= null && vo.getAge()==null){
					vo.setAge(0);
				}
				session.setAttribute("dqdp_user_org", vo);
				return vo;
			}
		}
	}

	public DqdpOrgVO getOrgByOrgId(String orgId) throws Exception {
		if (Configuration.IS_USE_MEMCACHED) {
			DqdpOrgVO org = CacheDqdpOrgManager.get(orgId);
			if(org==null){
				org = contactDAO.getOrgById(orgId);
				CacheDqdpOrgManager.set(orgId, org);
			}
			return org;
		}
		else{
			return contactDAO.getOrgById(orgId);
		}
	}

	// 导入数据合法校验
	@Override
	public boolean valid(Object obj) {
		ImportVO vo = (ImportVO) obj;
		if (!AssertUtil.isEmpty(vo) && !AssertUtil.isEmpty(vo.getMobile())) {
			vo.setMobile(vo.getMobile().replaceAll(" ", ""));
		}
		/*
		 * //非法手机号 if(AssertUtil.isEmpty(vo.getMobile())||!vo.getMobile().matches("^\\d{4,20}$")){ return false; }
		 * 
		 * //名字为空，非法数据 if(AssertUtil.isEmpty(vo.getPersonName())){ return false; }
		 */
		return true;
	}

	@Override
	public Pager searchByNameOrPhone(Map<String, Object> map, Pager pager, UserInfoVO user) throws Exception, BaseException {
		String orgId = user.getOrgId();
		String judge = "2";
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		if(!AssertUtil.isEmpty(map.get("keyWord"))){
			map = optimizeByNameOrPhone(map);
		}
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !AssertUtil.isEmpty(depts.get(0)) && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			return UserEduUtil.addChildrenToVO(contactDAO.searchByNameOrPhone(map, pager, depts), depts, user) ;
		}
		return UserEduUtil.addChildrenToVO(contactDAO.searchByNameOrPhone(map, pager, null), null, user);
	}
	
	/**
	 * 根据姓名或者手机号关键字搜索用户信息
	 * 范围：部门集合内的
	 */
	@Override
	public Pager searchByNameOrPhone(Map<String, Object> map, Pager pager, UserInfoVO user, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
		String orgId = user.getOrgId();
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		//姓名首字母
		if(!AssertUtil.isEmpty(map.get("keyWord"))){
			map = optimizeByNameOrPhone(map);
		}
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (deptList != null && deptList.size() > 0 && !AssertUtil.isEmpty(deptList.get(0))) {
			List<TbDepartmentInfoVO> depts = new ArrayList<TbDepartmentInfoVO>();
			TbDepartmentInfoVO vo;
			for (TbDepartmentInfoPO po : deptList) {
				vo = new TbDepartmentInfoVO();
				vo.setPermission("3");//特定对象
				vo.setDeptFullName(po.getDeptFullName());
				depts.add(vo);
			}
			//根据部门和条件查询对应的用户
			return contactDAO.searchByNameOrPhone(map, pager, depts);
		}
		//根据条件查询对应的用户
		return contactDAO.searchByNameOrPhone(map, pager, null);
	}

	@Override
	public boolean updateIsConcerned(String userId, String isConcerned) {
		TbQyUserInfoVO tbQyUserInfoVO = null;
		TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
		try {
			tbQyUserInfoVO = contactDAO.findUserInfoByUserId(userId);
			// BeanHelper.copyBeanProperties(tbQyUserInfoVO, tbQyUserInfoPO);
			tbQyUserInfoPO.setIsConcerned(isConcerned);
			tbQyUserInfoPO.setId(tbQyUserInfoVO.getId());
			contactDAO.update(tbQyUserInfoPO, false);
			return true;
		} catch (Exception e) {
			logger.error(userId + "-------更新状态时抛出了异常-----------------isConcerned:" + isConcerned);
			return false;
		} catch (BaseException e) {
			logger.error(userId + "-------更新状态时抛出了异常-----------------isConcerned:" + isConcerned);
			return false;
		}
	}

	@Override
	public int countByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.countByOrgId(orgId);
	}

	@Override
	public TbQyUserInfoView viewUserById(String id) throws Exception, BaseException {
		return contactDAO.viewUserById(id);
	}

	@Override
	public List<TbQyUserInfoVO> findDeptUserByUserId(String orgId, String userId) throws Exception, BaseException {
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, userId);
		if (depts == null || depts.size() == 0) {
			return null;
		}
		return contactDAO.findDeptUserByDept(orgId, depts);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersByUserNameOrPhoneOrPinyin(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> findUsersByUserNameOrPhoneOrPinyin(String orgId, String keyWord) throws Exception, BaseException {
		return contactDAO.findUsersByUserNameOrPhoneOrPinyin(orgId, keyWord);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getOrgByCorpId(java.lang.String)
	 */
	@Override
	public AccessToken getAccessTokenByCorpId(String corpId) throws Exception, BaseException {
		return contactDAO.getAccessTokenByCorpId(corpId);
	}

	/**
	 * 
	 * @description：获取所有用户
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	public Pager findAll(Pager pager) throws Exception, BaseException {

		return contactDAO.findAll(pager);
	}

	public ExtOrgPO getOrganizationById(String orgId) throws Exception, BaseException {

		return contactDAO.searchByPk(ExtOrgPO.class, orgId);
	}

	@Override
	public List<ExtOrgVO> searchOrgByCode(String orgCode) throws Exception, BaseException {
		return contactDAO.searchOrgByCode(orgCode);
	}

	@Override
	public Pager searchFirstLetter(String keyWord, Pager pager, UserInfoVO user) throws Exception, BaseException {
		String orgId = user.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("keyWord", keyWord.toLowerCase() + "%");
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			pager = contactDAO.searchFirstLetter(map, pager, depts);
			UserEduUtil.addChildrenToVO(pager, depts, user);
		} else {
			pager = contactDAO.searchFirstLetter(map, pager, null);
			UserEduUtil.addChildrenToVO(pager, null, user);
		}
		return pager;
	}
	
	public Pager searchByFirstLetter(String keyWord, Pager pager, UserInfoVO user, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException{
		String orgId = user.getOrgId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("orgId", orgId);
		map.put("keyWord", keyWord.toLowerCase() + "%");
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		
		if (deptList != null && deptList.size() > 0) {
			List<TbDepartmentInfoVO> depts = new ArrayList<TbDepartmentInfoVO>();
			TbDepartmentInfoVO vo;
			for (TbDepartmentInfoPO po : deptList) {
				vo = new TbDepartmentInfoVO();
				vo.setPermission("3");//特定对象
				vo.setDeptFullName(po.getDeptFullName());
				depts.add(vo);
			}
			pager = contactDAO.searchFirstLetter(map, pager, depts);
		} else {
			pager = contactDAO.searchFirstLetter(map, pager, null);
		}
		return pager;
	}

	@Override
	public void updateCommonUser(String userId, List<String> toUserIds, Integer num) throws Exception, BaseException {
		if (userId.equals(toUserIds)) {
			return;
		}
		//根据ids获取到所有已存在的常用联系人信息
		List<TbQyUserCommonPO> list = contactDAO.searchCommonUser(userId, toUserIds);
		//需要批量插入的常用联系人list
		List<TbQyUserCommonPO> insertTbQyUserCommonList=new ArrayList<TbQyUserCommonPO>();
		if(!AssertUtil.isEmpty(list)){
			List<String> existToUserId=new ArrayList<String>(list.size());
			for(TbQyUserCommonPO po:list){
				existToUserId.add(po.getToUserId());
			}
			toUserIds.removeAll(existToUserId);//取得差集
			//批量更新已存在的
			contactDAO.updateTbQyUserCommonByUserIdAndToUserIds(userId,existToUserId,num);
		}
		//对于不是常用用联系人的数据组装po
		if (!AssertUtil.isEmpty(toUserIds)) {
			Date date=new Date();
			TbQyUserCommonPO poClon = new TbQyUserCommonPO();
			for(String toUserId:toUserIds){
				insertTbQyUserCommonList.add(getTbQyUserCommonPO(poClon,userId, toUserId, date, num));
			}
		}
		//批量插入常用联系人数据
		if(!AssertUtil.isEmpty(insertTbQyUserCommonList)){
			this.contactDAO.execBatchInsert(insertTbQyUserCommonList);
		}
	}

	/***不存在常用联系人中的用户就组成po添加进去 by tanwq 2017-5-2
	 * @param userId
	 * @param toUserId
	 * @param date
	 * @param num
     * @return
     */
   private TbQyUserCommonPO getTbQyUserCommonPO(TbQyUserCommonPO poClon,String userId,String toUserId,Date date,int num){
	   TbQyUserCommonPO po =(TbQyUserCommonPO)poClon.clone();
	   po.setId(UUID32.getID());
	   po.setUserId(userId);
	   po.setToUserId(toUserId);
	   po.setRelativeNum(num);
	   po.setCreateTime(date);
	   po.setUpdateTime(date);
	   return po;
   }
	@Override
	public void updateCommonUser(String userId, String toUserId, Integer num) throws Exception, BaseException {
		if (userId.equals(toUserId)) {
			return;
		}
		List<TbQyUserCommonPO> list = contactDAO.searchCommonUser(userId, toUserId);
		if (AssertUtil.isEmpty(list)) {
			TbQyUserCommonPO po = new TbQyUserCommonPO();
			po.setId(UUID.randomUUID().toString());
			po.setUserId(userId);
			po.setToUserId(toUserId);
			po.setRelativeNum(num);
			po.setCreateTime(new Date());
			po.setUpdateTime(new Date());
			this.insertPO(po, true);
		} else {
			TbQyUserCommonPO po = list.get(0);
			po.setRelativeNum(po.getRelativeNum() + num);
			po.setUpdateTime(new Date());
			this.updatePO(po, false);
		}
	}

	@Override
	public List<TbQyUserInfoVO> getCommonUserList(String userId, Integer limit) throws Exception, BaseException {
		return contactDAO.getCommonUserList(userId, limit);
	}

	@Override
	public List<TbQyUserCommonPO> isCommonUser(String userId, String toUserId) throws Exception, BaseException {
		return contactDAO.isCommonUser(userId, toUserId);
	}

	@Override
	public void cancleCommonUser(String userId, String toUserId) throws Exception, BaseException {
		contactDAO.cancleCommonUser(userId, toUserId);
	}

	@Override
	public List<TbQyUserInfoVO> searchCommonUserList(String userId, String keyword) throws Exception, BaseException {
		return contactDAO.searchCommonUserList(userId, keyword);
	}

	private String[] getUserIds(List<TbQyUserInfoVO> list) {

		String persons = "";
		String userIds[] = null;
		if (list != null && list.size() > 0) {
			int length = list.size();
			int max = Configuration.WX_SEND_USER_MAX;// 微信单次发送的最多人员

			if (length < max) {
				userIds = new String[1];
				for (TbQyUserInfoVO tbQyUserInfoPO : list) {
					persons += "|" + tbQyUserInfoPO.getUserId();
				}
				userIds[0] = persons.replaceFirst("\\|", "");
			} else {
				userIds = new String[length / max + 1];
				// 循环发送给所有用户
				for (int i = 0; i < length; i++) {
					persons += "|" + list.get(i).getUserId();
					// 如果已经达到了最大允许发送的用户数，即刻进行发送，并将发送目标人员置为空
					if ((i + 1) % max == 0) {
						userIds[(i + 1) / max - 1] = persons.replaceFirst("\\|", "");
						persons = "";
					}
				}
				// 如果最后一组人员不为空，发送最后一组
				if (persons.length() > 0) {
					userIds[length / max] = persons.replaceFirst("\\|", "");
				}
			}
		}
		return userIds;
	}

	/**
	 * 
	 * @description：获取特定部门（多个）和特定人员Ids
	 * @param map
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng modify 2014-07-25 修改获取部门人员为多级部门 liangjiecheng
	 */
	public String[] findUserIdsByDeptIdsAndUserIds(Map map) throws Exception, BaseException {

		String deptIds = (String) map.get("deptIds");
		String orgId = (String) map.get("orgId");
		String userIds = (String) map.get("userIds");

		String[] arrayDepts = deptIds.split("\\|");
		List<List<TbQyUserInfoVO>> myList = new ArrayList<List<TbQyUserInfoVO>>();
		List<TbDepartmentInfoPO> depts = new ArrayList<TbDepartmentInfoPO>();
		for (String deptId : arrayDepts) {
			TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
			if (po == null) {
				continue;
			}
			depts.add(po);
		}

		// String depts = this.getMyAndChildDeptIds(deptId, orgId);
		List<TbQyUserInfoVO> userList = contactDAO.findDeptUserAllByDept(orgId, depts);
		if (userList.size() > 0) {
			myList.add(userList);
		}

		List<String> idsList = new ArrayList<String>();
		for (List<TbQyUserInfoVO> list : myList) {

			String[] idsArray = this.getUserIds(list);
			for (String ids : idsArray) {

				idsList.add(ids);
			}
		}

		// 去重复
		String[] userIdArray = {};
		if(!AssertUtil.isEmpty(userIds)){
			userIdArray = userIds.split("\\|");
		}
		String userIdsTemp = "";
		int count = 0;
		for (String userId : userIdArray) {

			boolean flag = true;
			for (String str : idsList) {

				if (!"".equals(str) && str != null) {
					if (str.contains(userId)) {

						flag = false;
						break;
					}
				}
			}
			if (flag) {// true为不重复，false为重复
				userIdsTemp += userId + "|";
			}
		}

		if (!"".equals(userIdsTemp)) {
			userIdsTemp = userIdsTemp.substring(0, userIdsTemp.length() - 1);
		}

		// 选择的特定对象人员
		String idArray[] = null;
		userIdArray = userIdsTemp.split("\\|");
		int length = userIdArray.length;
		int max = Configuration.WX_SEND_USER_MAX;
		String person = "";
		if (length < max) {

			count = 1;
			idArray = new String[1];
			idArray[0] = userIdsTemp;
		} else {

			count = length / max + 1;
			idArray = new String[length / max + 1];
			for (int i = 0; i < length; i++) {
				person += "|" + userIdArray[i];
				// 如果已经达到了最大允许发送的用户数，即刻进行发送，并将发送目标人员置为空
				if ((i + 1) % max == 0) {
					idArray[(i + 1) / max - 1] = person.replaceFirst("\\|", "");
					person = "";
				}
			}
			// 如果最后一组人员不为空，发送最后一组
			if (person.length() > 0) {
				idArray[length / max] = person.replaceFirst("\\|", "");
			}
		}

		String[] persons = new String[idsList.size() + count];
		int i = 0;
		for (String str : idsList) {

			persons[i] = str;
			i++;
		}

		// 添加特定人
		for (String ids : idArray) {

			persons[i] = ids;
			i++;
		}

		return persons;
	}
	
	@Override
	public List<String> findWxUserIdsByWxDeptIds(String orgId, String deptIds) throws Exception, BaseException{
		TbDepartmentInfoPO deptPO;
		List<TbDepartmentInfoPO> depts = new ArrayList<TbDepartmentInfoPO>();
		for (String wxId :  deptIds.split("\\|")) {
			deptPO = departmentService.getDeptByWeixin(orgId, wxId);
			if(null != deptPO){
				depts.add(deptPO);
			}
		}
		List<TbQyUserInfoVO> userList = contactDAO.findDeptUserAllByDept(orgId, depts);
		List<String> idsList = new ArrayList<String>();
		for (TbQyUserInfoVO userVo : userList) {
			idsList.add(userVo.getWxUserId());
		}
		//可能出现id重复，返回后需处理
		return idsList;
	}

	public int findPersonCountByDeptIdsAndUserIds(Map map) throws Exception, BaseException {

		String[] persons = this.findUserIdsByDeptIdsAndUserIds(map);
		int count = 0;

		for (String str : persons) {
			if (!"".equals(str) && str != null) {
				String[] users = str.split("\\|");
				count += users.length;
			}
		}

		return count;
	}

	public int getPersonCount(String deptIds, String userIds, String orgId, String range, String userId) throws Exception, BaseException {
		if ("1".equals(range)) {
			return OrgUtil.getUserTotal(orgId,OrgUtil.USER_MEMBER);
			//count = this.findUsersCountByOrgId(orgId);
		} else if ("2".equals(range)) {
			List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, userId);
			if (depts == null || depts.size() == 0) {
				return 0;
			}
			
			//2015-12-18 本部门需要获取的是所有顶级部门
			//需要对权限做判断，如果部门权限为仅本部门，获取顶级部门；如果为仅子部门，获取子部门下的所有部门
			Map<String,TbDepartmentInfoVO> topNameMap = new HashMap<String, TbDepartmentInfoVO>(10);
			List<String> deptFullName = new ArrayList<String>(10);
			int tmpIndex;
			for (TbDepartmentInfoVO userDept : depts) {
				if(!AssertUtil.isEmpty(userDept.getDeptFullName())){
					//部门属于子部门，判断部门的权限
					if("3".equals(userDept.getPermission())){
						//目标对象为仅子部门，获取全名
						if(!topNameMap.containsKey(userDept.getId())){
							topNameMap.put(userDept.getId(),userDept);
							deptFullName.add(userDept.getDeptFullName());
						}
					}else{
						tmpIndex=userDept.getDeptFullName().indexOf("->");
						if(tmpIndex>0){
							userDept = departmentMgrService.getDepartmentVOByName(userDept.getDeptFullName().substring(0, tmpIndex),orgId);
							if(userDept!=null && !topNameMap.containsKey(userDept.getId())){
								topNameMap.put(userDept.getId(),userDept);
								deptFullName.add(userDept.getDeptFullName());
							}
						}else{
							//部门属于顶级部门:不需要判断子部门的权限
							if(!topNameMap.containsKey(userDept.getId())){
								topNameMap.put(userDept.getId(),userDept);
								deptFullName.add(userDept.getDeptFullName());
							}
						}
					}
				}
			}
			if(topNameMap.size()==0){
				return 0;
			}
			if(topNameMap.size()==1){//当只有一个部门的时候，直接去部门的人数即可
				for(Map.Entry<String,TbDepartmentInfoVO> dept:topNameMap.entrySet()){
					if(!StringUtil.isNullEmpty(dept.getValue().getTotalUser())){
						return Integer.parseInt(dept.getValue().getTotalUser());
					}
				}
			}

			//将set转成list
			return getDeptCount(orgId,deptFullName,new ArrayList<String>(topNameMap.keySet()));
			//count = contactDAO.findDeptUserCountByUserId(orgId, topDeptList);
		} else {
			if(StringUtil.isNullEmpty(deptIds) || deptIds.equals("|")){
				return StringUtil.isNullEmpty(userIds) ? 0 : (WxqyhStringUtil.containStrExistCount(userIds,"|"));
			}
			else if(StringUtil.isNullEmpty(userIds) || userIds.equals("|")){
				return getDeptCount(orgId,departmentMgrService.getDeptFullNames(deptIds,"\\|"),ListUtil.toList(deptIds.split("\\|")));
			}

			String[] deptIdSplit = deptIds.split("\\|");
			int deptUserCount;
			List<String> deptIdList = getThisAndChildDeptIds(orgId,departmentMgrService.getDeptFullNames(deptIdSplit),ListUtil.toList(deptIdSplit));
			if(deptIdList==null || deptIdList.size()==0){
				return StringUtil.isNullEmpty(userIds) ? 0 : (WxqyhStringUtil.containStrExistCount(userIds,"|"));
			}
			if(deptIdSplit.length==1){
				TbDepartmentInfoPO po = departmentMgrService.searchByPk(TbDepartmentInfoPO.class,deptIdSplit[0]);
				if(po == null){
					return StringUtil.isNullEmpty(userIds) ? 0 : (WxqyhStringUtil.containStrExistCount(userIds,"|"));
				}
				if(po.getTotalUser() != null){
					deptUserCount = po.getTotalUser().intValue();
				}else{
					deptUserCount = departmentMgrService.getUserCountByDeptIds(deptIdList);
				}
			}
			else{
				deptUserCount = departmentMgrService.getUserCountByDeptIds(deptIdList);
			}
			String[] userIdSplit = userIds.split("\\|");
			int userCount = departmentMgrService.getUserCountByDeptIdsAndUserId(deptIdList,userIdSplit);
			return deptUserCount+userIdSplit.length-userCount;
			//count = this.findPersonCountByDeptIdsAndUserIds(map);
		}
	}

	private int getDeptCount(String orgId,List<String> deptFullName,List<String> deptIds) throws Exception {
		if(deptIds!=null && deptIds.size()==1){
			TbDepartmentInfoPO po = departmentMgrService.searchByPk(TbDepartmentInfoPO.class,deptIds.get(0));
			if(po != null && po.getTotalUser() != null){
				return po.getTotalUser().intValue();
			}
			return 0;
		}
		deptIds = getThisAndChildDeptIds(orgId,deptFullName,deptIds);
		if(deptIds!=null && deptIds.size()>0){
			return departmentMgrService.getUserCountByDeptIds(deptIds);
		}
		return 0;
	}
	private List<String> getThisAndChildDeptIds(String orgId,List<String> deptFullName,List<String> deptIds) throws Exception {
		if(deptFullName==null || deptFullName.size()==0){
			return deptIds;
		}
		List<String> childDeptIds = departmentMgrService.getChildDeptIdsByFullName(orgId,deptFullName);
		if(childDeptIds.size()>0){
			if(deptIds!=null)
				childDeptIds.addAll(deptIds);
			return childDeptIds;
		}
		else if(deptIds!=null && deptIds.size()>0){
			return deptIds;
		}
		return null;
	}

	/**
	 * @description：判断发布人是否在推送列表里
	 * @param persons
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	public boolean isExitsInPersons(String[] persons, String userId) throws Exception, BaseException {

		boolean flag = false;

		for (String user : persons) {

			if (!"".equals(user) && user != null) {

				if (user.contains(userId)) {

					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#isChildrenDept(java.lang.String[])
	 */
	@Override
	public boolean isChildrenDept(String myDeptId, String depIds) throws Exception, BaseException {
		boolean isChildrenDept = false;
		String[] depIdStrings = depIds.split("\\|");
		TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, myDeptId);
		List<TbDepartmentInfoVO> deptList = contactDAO.findDeptFullNamesByIds(depIdStrings);
		if (deptList != null && deptList.size() > 0) {
			int size = deptList.size();
			TbDepartmentInfoVO vo;
			for (int i = 0; i < size; i++) {
				vo = deptList.get(i);
				if (po.getDeptFullName().indexOf(vo.getDeptFullName()) == 0) {
					isChildrenDept = true;
					break;
				}
			}
		}
		return isChildrenDept;
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getMyAndChildDeptIds(java.lang.String, java.lang.String)
	 */
	@Override
	public String getMyAndChildDeptIds(String deptId, String orgId) throws Exception, BaseException {
		return contactDAO.getMyAndChildDeptIds(deptId, orgId);
	}

	/**
	 * 
	 * @description：获取某部门下的用户和子部门的所有人
	 * @param orgId
	 * @param departId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng 2014-07-25
	 */
	public List<TbQyUserInfoVO> getUsersAllByDepartId(String orgId, String departId) throws Exception, BaseException {

		// String deptIds = this.getMyAndChildDeptIds(departId,orgId);
		TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, departId);
		if (po == null || !orgId.equals(po.getOrgId())) {
			return null;
		}

		return contactDAO.findDeptUserAllByDept(orgId, po.getDeptFullName());
	}

	@Override
	public List<TbQyUserInfoVO> getUsersByDepartId(String departId,Map<String,Object> params) throws Exception, BaseException {
		return contactDAO.findDeptUserByDept(departId,params);
	}

	@Override
	public Pager searchUsersByDepartId(Pager pager, Map<String,Object> params) throws Exception, BaseException {
		return contactDAO.searchUsersByDepartId(pager, params);
	}
	
	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getTbAddressbookGuessbookPOByUserId(java.lang.String)
	 */
	@Override
	public Pager getTbAddressbookGuessbookPOByUserId(String userId, Pager pager, Map searchMap) throws Exception, BaseException {
		Pager pagers = contactDAO.getTbAddressbookGuessbookPOByUserId(userId, pager, searchMap);
		return getPageCommentInfo(pagers);
	}

	/**
	 *
	 * @description：获取评论信息
	 * @param pager
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author liangjiecheng
	 */
	private Pager getPageCommentInfo(Pager pager) throws Exception, BaseException {
		Collection<?> data = pager.getPageData();
		if (data == null)
			pager.setPageData(Collections.emptyList());
		else {
			List<Map<String, Object>> commentInfos = new ArrayList<Map<String, Object>>();
			for (Object o : data) {
				commentInfos.add(initCommentInfo((TbaddressbookguessbookVo) o));
			}
			pager.setPageData(commentInfos);
		}

		return pager;
	}

	/**
	 *
	 * @description：评论信息转换
	 * @param vo
	 * @return
	 * @author liangjiecheng
	 */
	private Map<String, Object> initCommentInfo(TbaddressbookguessbookVo vo) {
		Map<String, Object> commentInfo = new HashMap<String, Object>();
		commentInfo.put("userId", vo.getUserId());
		commentInfo.put("personName", vo.getPersonName());
		commentInfo.put("creator", vo.getCreator());
		commentInfo.put("contents", vo.getContents());
		commentInfo.put("createTime", TimeHelper.getFriendlyDesc(vo.getCreateTime()));
		commentInfo.put("id", vo.getId());
		commentInfo.put("remark", vo.getRemark());
		commentInfo.put("guessId", vo.getGuessId());
		commentInfo.put("headPic", vo.getHeadPic());
		commentInfo.put("status", vo.getStatus());
		return commentInfo;
	}

	/*
	 * （非 Javadoc）获取留言回复
	 * 
	 * @scontacto1.component.contact.contact.service.IContactService#searchMoreguess(cn.com.do1.component.contact.contact.model.TbAddressbookGuessbookPO, int, java.util.Map)
	 */
	public Pager getTbAddressbookGuessbookPOReply(Pager pager, Map searchMap) throws Exception, BaseException {

		return contactDAO.getTbAddressbookGuessbookPOReply(pager, searchMap);

	}

	/*
	 * （非 Javadoc）获取未读留言个数
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getStatuesGuessCount(java.util.Map)
	 */
	@Override
	public Integer getStatuesGuessCount(Map<String, Object> searchMap) throws Exception, BaseException {
		return contactDAO.getStatuesGuessCount(searchMap);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#searchOrgByOrgName(java.lang.String)
	 */
	@Override
	public List<ExtOrgVO> searchOrgByOrgName(String orgName, String orgPid) throws Exception, BaseException {
		return contactDAO.searchOrgByOrgName(orgName, orgPid);
	}

	@Override
	public void changeStatus(String userId) throws Exception, BaseException {
		contactDAO.changeStatus(userId);
	}

	@Override
	public Pager findAlluserByUser(Pager pager, UserInfoVO user,Map<String,Object> params) throws Exception, BaseException {
		String orgId = user.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		if(!AssertUtil.isEmpty(params.get("sortTop"))){
			map.put("sortTop", params.get("sortTop").toString());
		}
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			depts = DepartmentUtil.checkDept(depts, map, user);
			// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
			pager = contactDAO.findAlluserByDeptId(map, pager, depts);
			UserEduUtil.addChildrenToVO(pager, depts, user);
		} else {
			pager = contactDAO.searchContactByPy(map, pager);
			UserEduUtil.addChildrenToVO(pager, null, user);
		}
		return pager;
	}

	@Override
	public String getDeptFisrtDetpIdBydeptId(String deptId, String orgId) throws Exception, BaseException {
		TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, deptId);

		String[] spil = po.getDeptFullName().split("->");
		if (spil.length > 1) {
			TbDepartmentInfoPO xxPO = departmentMgrService.getDepartmentInfoByName(spil[0], orgId);
			return xxPO.getId();
		}
		return deptId;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserLoginByUserId(java.lang.String)
	 */
	@Override
	public TbQyUserLoginInfoPO getUserLoginByUserId(String userId) throws Exception, BaseException {
		return contactDAO.getUserLoginByUserId(userId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserLoginByAccount(java.lang.String)
	 */
	@Override
	public TbQyUserLoginInfoPO getUserLoginByAccount(String userAccount) throws Exception, BaseException {
		return contactDAO.getUserLoginByAccount(userAccount);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getDqdpUserByUserName(java.lang.String)
	 */
	@Override
	public TbDqdpUserPO getDqdpUserByUserName(String userName) throws Exception, BaseException {
		return contactDAO.getDqdpUserByUserName(userName);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findRepeatUsersByCorpId(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoPO> findRepeatUsersByCorpId(String corpId, String wxUserId, String mobile, String weixinNum, String email) throws Exception, BaseException {
		List<TbQyUserInfoPO> list = new ArrayList<TbQyUserInfoPO>();
		List<TbQyUserInfoPO> temp;
		Set<String> set = new HashSet<String>();
		if (!StringUtil.isNullEmpty(wxUserId)) {
			temp =  contactReadOnlyDAO.findUsersByWxUserId(corpId, wxUserId);
			if(temp != null && temp.size()>0){
				list.addAll(temp);
				for (TbQyUserInfoPO po : temp) {
					set.add(po.getUserId());
				}
			}
		}
		if (list.size()<2 && !StringUtil.isNullEmpty(mobile)) {//如果前面的都重复了，那就不需要查询其它信息了
			temp = contactReadOnlyDAO.findUsersByMobile(corpId, mobile);
			if(temp != null && temp.size()>0){
				//如果list中已有数据，要先判断数据是否重复，如果重复，不需要加入list
				if(list.size()==0){
					list.addAll(temp);
					for (TbQyUserInfoPO po : temp) {
						set.add(po.getUserId());
					}
				}
				else{
					for (TbQyUserInfoPO po : temp) {
						if(set.add(po.getUserId())){
							list.add(po);
						}
					}
				}
			}
		}
		if (list.size()<2 && !StringUtil.isNullEmpty(weixinNum)) {
			temp = contactReadOnlyDAO.findUsersByWeixinNum(corpId, weixinNum);
			if(temp != null && temp.size()>0){
				//如果list中已有数据，要先判断数据是否重复，如果重复，不需要加入list
				if(list.size()==0){
					list.addAll(temp);
					for (TbQyUserInfoPO po : temp) {
						set.add(po.getUserId());
					}
				}
				else{
					for (TbQyUserInfoPO po : temp) {
						if(set.add(po.getUserId())){
							list.add(po);
						}
					}
				}
			}
		}
		if (list.size()<2 && !StringUtil.isNullEmpty(email)) {
			temp = contactReadOnlyDAO.findUsersByEmail(corpId, email);
			if(temp != null && temp.size()>0){
				//如果list中已有数据，要先判断数据是否重复，如果重复，不需要加入list
				if(list.size()==0){
					list.addAll(temp);
					for (TbQyUserInfoPO po : temp) {
						set.add(po.getUserId());
					}
				}
				else{
					for (TbQyUserInfoPO po : temp) {
						if(set.add(po.getUserId())){
							list.add(po);
						}
					}
				}
			}
		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.form.form.service.IFormService#insertAtRecord(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<List<String>> insertAtRecord(String userId, String atPersons, String content) {
		List<List<String>> insertAtUser = new ArrayList<List<String>>();
		List<String> userIds = null;
		List<String> wxUserIds = null;
		if (!StringUtil.isNullEmpty(atPersons)) {
			userIds = new ArrayList<String>(10);
			wxUserIds = new ArrayList<String>(10);
			String[] persons = atPersons.split("\\|");
			StringBuffer personTemp = new StringBuffer("");
			StringBuffer wxPersonTemp = new StringBuffer("");
			if (persons != null && persons.length > 0) {
				int length = persons.length;
				int max = Configuration.WX_SEND_USER_MAX;// 微信单次发送的最多人员
				TbQyUserInfoVO userVO;
				if (length < max) {
					for (String person : persons) {
						try {
							userVO = contactDAO.findUserInfoByUserId(person);
							if (userVO == null) {// || content.indexOf("@"+userVO.getPersonName())<0
								// 评论里没找到此用户id对应的@人名内容
								continue;
							}
							// 记录常用联系人
							contactDAO.updateCommonUser(userId, person, 1);
							personTemp.append("|" + userVO.getUserId());
							wxPersonTemp.append("|" + userVO.getWxUserId());

						} catch (Exception e) {
							logger.error("表单回复记录常用联系人失败", e);
						} catch (BaseException e) {
							logger.error("表单回复记录常用联系人失败", e);
						}
					}
					if (personTemp.length() > 0) {
						userIds.add(personTemp.deleteCharAt(0).toString());
					}
					if (wxPersonTemp.length() > 0) {
						wxUserIds.add(wxPersonTemp.deleteCharAt(0).toString());
					}
				} else {
					// 循环发送给所有用户
					for (int i = 0; i < length; i++) {
						try {
							userVO = contactDAO.findUserInfoByUserId(persons[i]);
							if (userVO == null) {// || content.indexOf("@"+userVO.getPersonName())<0
								// 评论里没找到此用户id对应的@人名内容
								continue;
							}
							// 记录常用联系人
							contactDAO.updateCommonUser(userId, persons[i], 1);
							personTemp.append("|" + userVO.getUserId());
							wxPersonTemp.append("|" + userVO.getWxUserId());
							// 如果已经达到了最大允许发送的用户数，即刻进行发送，并将发送目标人员置为空
							if ((i + 1) % max == 0) {
								if (personTemp.length() > 0) {
									userIds.add(personTemp.deleteCharAt(0).toString());
								}
								if (wxPersonTemp.length() > 0) {
									wxUserIds.add(wxPersonTemp.deleteCharAt(0).toString());
								}
								personTemp.setLength(0);
								wxPersonTemp.setLength(0);
							}

						} catch (Exception e) {
							logger.error("表单回复记录常用联系人失败", e);
						} catch (BaseException e) {
							logger.error("表单回复记录常用联系人失败", e);
						}
					}
					// 如果最后一组人员不为空，发送最后一组
					if (!personTemp.toString().isEmpty()) {
						if (personTemp.length() > 0) {
							userIds.add(personTemp.deleteCharAt(0).toString());
						}
						if (wxPersonTemp.length() > 0) {
							wxUserIds.add(wxPersonTemp.deleteCharAt(0).toString());
						}
					}
				}
			}
		}
		insertAtUser.add(userIds);
		insertAtUser.add(wxUserIds);
		return insertAtUser;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findAllLoginUserByUserStatus(java.lang.String)
	 */
	@Override
	public List<UserOrganizationVO> findAllLoginUserByUserStatus(String userStatus) throws Exception, BaseException {
		return contactDAO.findAllLoginUserByUserStatus(userStatus);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserSync(java.lang.String)
	 */
	@Override
	public List<TbQyUserSyncPO> getUserSync(String orgid, String newDate) throws Exception, BaseException {
		return contactDAO.getUserSync(orgid, newDate);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getOrgPOByCorpId(java.lang.String)
	 */
	@Override
	public List<ExtOrgPO> getOrgPOByCorpId(String corpId) throws Exception {
		return contactDAO.getOrgPOByCorpId(corpId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getConfigOrgAesPOByCorpId(java.lang.String)
	 */
	@Override
	public List<TbConfigOrgAesPO> getConfigOrgAesPOByCorpId(String corpId) throws Exception, BaseException {
		return contactDAO.getConfigOrgAesPOByCorpId(corpId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#updateCorpSecret(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateCorpSecret(String corpId, String corpSecret) throws Exception, BaseException {
		List<ExtOrgPO> orgList = contactDAO.getOrgPOByCorpId(corpId);
		if (orgList != null && orgList.size() > 0) {
			ExtOrgPO po = new ExtOrgPO();
			for (ExtOrgPO extOrgPO : orgList) {
				po.setOrganizationId(extOrgPO.getOrganizationId());
				po.setCorpSecret(corpSecret);
				contactDAO.update(po, false);
			}
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersByPhone(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserInfoVO findUsersByPhone(String orgId, String mobile) throws Exception, BaseException {
		return contactDAO.findUsersByPhone(orgId, mobile);
	}
	public void insertPicAttachZhouNian(String groupId, String userId, String orgId, String[] imageUrls,String picType) throws Exception, BaseException {
		if (imageUrls != null && imageUrls.length > 0) {
			int i = 1;
			TbQyPicPO po = new TbQyPicPO();
			po.setCreateTime(new Date());
			for (String image : imageUrls) {
				if (image == null || image.isEmpty()) {
					continue;
				}
				po.setGroupId(groupId);
				po.setPicPath(image.trim());
				po.setCreatePerson(userId);
				po.setOrgId(orgId);
				po.setSort(i);
				po.setPicType(picType);
				this.insertPO(po, true);
				i++;
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getPicListByGroupId(java.lang.String)
	 */
	@Override
	public List<TbQyPicPO> getPicListByGroupId(String groupId) throws Exception, BaseException {
		return this.contactDAO.getPicListByGroupId(groupId);
	}
	public List<TbQyPicPO> getPicListByGroupIdZhouNian(String groupId,String type) throws Exception, BaseException {
		return this.contactDAO.getPicListByGroupIdZhouNian(groupId,type);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#delPicListByGroupId(java.lang.String, java.lang.String)
	 */
	@Override
	public void delPicListByGroupId(String groupId, String userId) throws Exception, BaseException {
		this.contactDAO.delPicListByGroupId(groupId, userId);
	}

	@Override
	public void delPicListByGroupIds(String[] groupIds, String orgId) throws Exception, BaseException {
		if (!AssertUtil.isEmpty(groupIds)) {
			this.contactDAO.delPicListByGroupIds(groupIds, orgId);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#insertPicAttach(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public void insertPicAttach(String groupId, String userId, String orgId, String[] imageUrls) throws Exception, BaseException {
		if (imageUrls != null && imageUrls.length > 0) {
			int i = 1;
			TbQyPicPO po = new TbQyPicPO();
			po.setCreateTime(new Date());
			for (String image : imageUrls) {
				if (image == null || image.isEmpty()) {
					continue;
				}
				po.setGroupId(groupId);
				po.setPicPath(image.trim());
				po.setCreatePerson(userId);
				po.setOrgId(orgId);
				po.setSort(i);
				
				this.insertPO(po, true);
				i++;
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUserInfoByWxUserId(java.lang.String)
	 */
	@Override
	public TbQyUserInfoVO findUserInfoByWxUserId(String wxUserId, String corpId) throws Exception, BaseException {
		List<TbQyUserInfoVO> list = contactDAO.findUserInfoByWxUserId(wxUserId, corpId);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new BaseException("用户账号" + wxUserId + "已存在");
		}
		return null;
	}

	@Override
	public TbQyUserInfoVO findUserInfoOneByWxUserId(String wxUserId, String corpId) throws Exception, BaseException {
		List<TbQyUserInfoVO> list = contactDAO.findUserInfoByWxUserId(wxUserId, corpId);
		if (list.size() == 1) {
			return list.get(0);
		}
		if(list.size()>0){
			TbQyUserInfoVO vo = null;
			for (int i = 0; i < list.size(); i++) {
				vo = list.get(i);
				//如果不是离职，就直接返回，离职再查下一个
				if (ContactDictUtil.USER_STAtUS_LEAVE != vo.getUserStatus()){
					return vo;
				}
			}
			return vo;
		}
		return null;
	}

	@Override
	public TbQyUserInfoPO findUserInfoPOByWxUserId(String wxUserId, String corpId) throws Exception {
		List<TbQyUserInfoPO> list = contactDAO.findUserInfoPOByWxUserId(wxUserId, corpId);
		if (list.size() == 1) {
			return list.get(0);
		}
		if(list.size()>0){
			TbQyUserInfoPO po = null;
			for (int i = 0; i < list.size(); i++) {
				po = list.get(i);
				//如果不是离职，就直接返回，离职再查下一个
				if (ContactDictUtil.USER_STAtUS_LEAVE != po.getUserStatus()){
					return po;
				}
			}
			return po;
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#verifyUserInfo(java.lang.String, java.lang.String, cn.com.do1.component.contact.contact.model.TbQyUserInfoPO)
	 */
	@Override
	public String verifyUserInfo(String corpId, String orgId, TbQyUserInfoPO tbQyUserInfoPO,String type) throws Exception, BaseException {
		return verifyUserInfo(corpId, orgId, tbQyUserInfoPO, type, false);
	}

	@Override
	public String verifyUserInfo(String corpId, String orgId, TbQyUserInfoPO tbQyUserInfoPO,String type, boolean isUpdate) throws Exception, BaseException {
		// 验证本机构下是否已存在此用户（机构内手机号不能重复）
		//type  type=0来自成员邀请的验证       type=1通讯录新增验证
		if (StringUtil.isNullEmpty(tbQyUserInfoPO.getWxUserId())) {
			return "账号信息不能为空";
		}
		if (!tbQyUserInfoPO.getWxUserId().matches("^\\w+[\\w\\-_\\.@]*$")) {
			return "账号格式不正确，请重新输入！";
		}
		if (tbQyUserInfoPO.getWxUserId().length() > 64) {
			return "账号长度不能超过64个字符！";
		}
		if (StringUtil.isNullEmpty(tbQyUserInfoPO.getMobile()) && StringUtil.isNullEmpty(tbQyUserInfoPO.getWeixinNum()) && StringUtil.isNullEmpty(tbQyUserInfoPO.getEmail())) {
			return "微信号、手机和邮箱三种信息不能同时为空";
		}
		List<TbQyUserInfoPO> list = this.findRepeatUsersByCorpId(corpId, tbQyUserInfoPO.getWxUserId(), tbQyUserInfoPO.getMobile(), tbQyUserInfoPO.getWeixinNum(), tbQyUserInfoPO.getEmail());
		if (list != null && list.size() > 0) {
			TbQyUserInfoPO historUser = null;
			for (int i = 0; i < list.size(); i++) {
				historUser = list.get(i);
				if (!isUpdate || !historUser.getId().equals(tbQyUserInfoPO.getId())) {
					break;
				}
				if (!historUser.getOrgId().equals(orgId)) {
					if("0".equals(type)){
						return "该账号已在其它体验号中，请重新输入 或长按以上二维码识别关注";
					}else{
						return "该账号已存在其它体验号中，请重新输入";
					}
				}
				historUser = null;
			}
			if (historUser != null) {
				if (!StringUtil.isNullEmpty(tbQyUserInfoPO.getMobile()) && tbQyUserInfoPO.getMobile().equals(historUser.getMobile())) {
					if("-1".equals(historUser.getUserStatus())){
						return "该手机号已在离职人员中";
					}
					if("0".equals(type)){
						return "该手机号已在通讯录中，请重新输入 或长按以上二维码识别关注";
					}else{
						return "该手机号已存在通讯录中，请重新输入";
					}
				}
				if (!StringUtil.isNullEmpty(tbQyUserInfoPO.getEmail()) && tbQyUserInfoPO.getEmail().equals(historUser.getEmail())) {
					if("-1".equals(historUser.getUserStatus())){
						return "该电子邮箱已在离职人员中";
					}
					if("0".equals(type)){
						return "该邮箱已在通讯录中，请重新输入 或长按以上二维码识别关注";
					}else{
						return "该电子邮箱已存在通讯录中，请重新输入";
					}
				}
				if (!StringUtil.isNullEmpty(tbQyUserInfoPO.getWeixinNum()) && tbQyUserInfoPO.getWeixinNum().equals(historUser.getWeixinNum())) {
					if("-1".equals(historUser.getUserStatus())){
						return "该微信号已在离职人员中";
					}
					//type  type=0来自成员邀请的验证       type=1通讯录新增验证
					if("0".equals(type)){
						return "该微信号已在通讯录中，请重新输入 或长按以上二维码识别关注";
					}else{
						return "该微信号已存在通讯录中，请重新输入";
					}
				}
				if (tbQyUserInfoPO.getWxUserId().equals(historUser.getWxUserId())) {
					if("-1".equals(historUser.getUserStatus())){
						return "该账号已在离职人员中";
					}
					if("0".equals(type)){
						return "该账号已在通讯录中，请重新输入 或长按以上二维码识别关注";
					}else{
						return "该账号已存在通讯录中，请重新输入";
					}
				}
			}
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersByOrgIdForExport(java.lang.String)
	 */
	@Override
	public List<ExportUserInfo> findUsersByOrgIdForExport(String orgId) throws Exception, BaseException {
		return contactDAO.findUsersByOrgIdForExport(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#hasUsersByDepartId(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasUsersByDepartId(String deptId) throws Exception {
		return contactDAO.hasUsersByDepartId(deptId);
	}

	@Override
	public boolean hasUsersByDepartIdAndFullName(String organId, String deptId) throws Exception, BaseException {
		if(StringUtil.isNullEmpty(deptId)){
			return false;
		}
		Set<String> set = departmentService.getChildDepartIds(deptId, organId);
		if(set == null){
			set = new HashSet<String>(1);
		}
		set.add(deptId);
		return hasUsersByDepartIds(new ArrayList<String>(set));
	}

	@Override
	public boolean hasUsersByDepartIds(List<String> deptIds) throws Exception, BaseException {
		if(AssertUtil.isEmpty(deptIds)){
			return false;
		}
		List<TbQyUserDepartmentRefPO> userDeptRefs = departmentService.getDeptUserRefPOByDeptIds(deptIds);
		if(AssertUtil.isEmpty(userDeptRefs)){ //如果部门用户关联关系为空
			return false;
		}
		List<String> list = new ArrayList<String>(userDeptRefs.size());
		String[] ids = new String [userDeptRefs.size()];
		int i = 0;
		for (TbQyUserDepartmentRefPO po : userDeptRefs) {
			list.add(po.getUserId());
			ids[i] = po.getId();
			i++;
		}
		if (!AssertUtil.isEmpty(contactReadOnlyDAO.getUserInfoIdByUserIds(list))) { //判断用户是否存在，如果不存在，说明存留了关联数据的脏数据
			return true;
		}
		//删除这些脏数据
		this.batchDel(TbQyUserDepartmentRefPO.class, ids);
		return false;
	}


	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#moveUserByOrgId(java.lang.String)
	 */
	@Override
	public void moveUserByOrgId(String orgId) throws Exception, BaseException {
		String sql = "insert into tb_qy_user_delete select * from tb_qy_user_info q where q.org_id= :orgId ";
		contactDAO.preparedSql(sql);
		contactDAO.setPreValue("orgId", orgId);
		contactDAO.executeUpdate();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersByOrgId(java.util.Map)
	 */
	@Override
	public Pager findUsersByOrgId(Map<String, Object> map, Pager pager) throws Exception, BaseException {
		/**
		 * 已知问题：当操作用户部门权限属于：部门A（本部门或子部门），部门A存在用户1，用户1存在多个部门信息（如部门B），操作用户输入部门B，是无法搜索到对应的用户1
		 */
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(map.get("orgId").toString(), map.get("userId").toString());
		map.remove("userId");
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			return contactDAO.findUsersByOrgId(map, pager, depts);
		} else {
			return contactDAO.findUsersByOrgId(map, pager, null);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#batchDelLocationByUserId(java.lang.String)
	 */
	@Override
	public boolean batchDelLocationByUserId(String userId) {
		try {
			contactDAO.batchDelLocationByUserId(userId);
			return true;
		} catch (Exception e) {
			logger.error("批量删除用户历史定位信息", e);
		} catch (BaseException e) {
			logger.error("批量删除用户历史定位信息", e);
		}
		return false;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getAllUserInfoByIds(java.lang.String[])
	 */
	public List<TbQyUserInfoVO> getAllUserInfoByIds(String[] userIds) throws Exception, BaseException {
		if (userIds == null || userIds.length == 0) {
			return new ArrayList<TbQyUserInfoVO>();
		}
		int size = userIds.length;
		if (size <= Configuration.SQL_IN_MAX) {
			return contactDAO.getAllUserInfoByIds(userIds);
		} else {
			List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(size);
			String[] newUserIds = new String[200];
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				System.arraycopy(userIds, startIndex, newUserIds, 0, Configuration.SQL_IN_MAX);
				list.addAll(contactDAO.getAllUserInfoByIds(newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = new String[size - startIndex];
				System.arraycopy(userIds, startIndex, newUserIds, 0, size - startIndex);
				list.addAll(contactDAO.getAllUserInfoByIds(newUserIds));
			}
			return list;
		}
	}

	public List<TbQyUserInfoVO> getAllUserAndDeptByIds(String[] userIds) throws Exception, BaseException {
		if (userIds == null || userIds.length == 0) {
			return new ArrayList<TbQyUserInfoVO>();
		}
		int size = userIds.length;
		if (size <= Configuration.SQL_IN_MAX) {
			return contactReadOnlyDAO.getAllUserAndDeptByIds(userIds);
		} else {
			List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(size);
			String[] newUserIds = new String[200];
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				System.arraycopy(userIds, startIndex, newUserIds, 0, Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getAllUserAndDeptByIds(newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = new String[size - startIndex];
				System.arraycopy(userIds, startIndex, newUserIds, 0, size - startIndex);
				list.addAll(contactReadOnlyDAO.getAllUserAndDeptByIds(newUserIds));
			}
			return list;
		}
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getAllOrg()
	 */
	@Override
	public List<ExtOrgPO> getAllOrg() throws Exception, BaseException {
		return contactDAO.getAllOrg();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findBirthdayUserByDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<UserDeptInfoVO> findBirthdayUserByDate(String organizationId, String lunar, String type) throws Exception, BaseException {
		return contactDAO.findBirthdayUserByDate(organizationId, lunar, type);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#insertUser(cn.com.do1.component.contact.contact.model.TbQyUserInfoPO, java.util.List)
	 */
	@Override
	public void insertUser(TbQyUserInfoPO tbQyUserInfoPO, List<String> d) throws Exception, BaseException {
		// 插入用户部门关联
		insertUserDeptRef(tbQyUserInfoPO.getOrgId(), tbQyUserInfoPO.getUserId(), d);
		tbQyUserInfoPO.setUpdateTime(new Date());
		contactDAO.insert(tbQyUserInfoPO);
		//更新扩展表的生日记录
		updateUserInfoExt(tbQyUserInfoPO,true);//插入用户的时候，需要更新空字段
	}

	/**
	 * 新增用户
	 *
	 * @param po
	 * @param deptIds
	 * @param wxDeptIds
	 * @return
	 * @author Sun Qinghai
	 * @ 16-4-26
	 */
	@Override
	public void insertUser(TbQyUserInfoPO po, List<String> deptIds, List<String> wxDeptIds) throws Exception, BaseException {
		// 插入用户部门关联
		insertUserDeptRef(po.getOrgId(), po.getUserId(), deptIds);
		po.setUpdateTime(new Date());
		po.setCreateTime(new Date());
		po.setUserStatus("0");
		po.setIsConcerned("0");
		contactDAO.insert(po);
		//更新扩展表的生日记录
		updateUserInfoExt(po,true);//插入用户的时候，需要更新空字段
		publishToWx(po,wxDeptIds);
	}

	//同步数据到微信后台
	private void publishToWx(TbQyUserInfoPO tbQyUserInfoPO,List<String> wxDeptIds) throws Exception, BaseException{
		WxUser user = new WxUser();
		user.setUserid(tbQyUserInfoPO.getWxUserId());
		user.setName(tbQyUserInfoPO.getPersonName());
		user.setEmail(tbQyUserInfoPO.getEmail());
		user.setGender(tbQyUserInfoPO.getSex());
		user.setMobile(tbQyUserInfoPO.getMobile());
		user.setPosition(tbQyUserInfoPO.getPosition());
		user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
		user.setDepartment(wxDeptIds);
		WxUserService.addUser(user,tbQyUserInfoPO.getCorpId(),tbQyUserInfoPO.getOrgId());
	}

	/**
	 * 新增用户部门关联信息
	 * 
	 * @param userId
	 * @param d
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2015-1-15
	 * @version 1.0
	 */
	public void insertUserDeptRef(String orgId, String userId, List<String> d) {
		try {
			if(d==null || d.size()==0){
				return;
			}
			if (d.size() == 1) {
				TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
				po.setUserId(userId);
				po.setOrgId(orgId);
				po.setSort(0);
				po.setDepartmentId(d.get(0));
				po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
				contactDAO.insert(po);
			}
			else {
				List<TbQyUserDepartmentRefPO> list = new ArrayList<TbQyUserDepartmentRefPO>(d.size());
				for (int i = 0; i < d.size(); i++) {
					TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
					po.setUserId(userId);
					po.setOrgId(orgId);
					po.setSort(i);
					po.setDepartmentId(d.get(i));
					po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
					list.add(po);
				}
				QwtoolUtil.addBatchList(list, false);
			}
		} catch (Exception e) {
			ExceptionCenter.addException(e, "ContactServiceImpl insertUserDeptRef @sqh", userId);
		} catch (BaseException e) {
			ExceptionCenter.addException(e, "ContactServiceImpl insertUserDeptRef @sqh", userId);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#deleteUserDeptRef(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteUserDeptRef(String orgId, String[] userIds) throws Exception, BaseException {
		contactDAO.deleteUserDeptRef(orgId, userIds);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#deleteUserDeptRefByUserIdDeptId(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteUserDeptRefByUserIdDeptId(String deptId, String userId)
			throws Exception, BaseException {
		contactDAO.deleteUserDeptRefByUserIdDeptId(deptId, userId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#updateUser(cn.com.do1.component.contact.contact.model.TbQyUserInfoPO, java.util.List)
	 */
	@Override
	public UpdateUserResult updateUser(TbQyUserInfoPO tbQyUserInfoPO, List<String> detId, boolean isUpdateNull) throws Exception, BaseException {
		UpdateUserResult result = new UpdateUserResult();
		List<String> oldDeptId = departmentService.getDeptUserRefByUserId(tbQyUserInfoPO.getUserId());
		if(detId!=null && detId.size()>0 && !ListUtil.strCsStr(detId,oldDeptId)) {
			// 删除用户部门关联信息
			deleteUserDeptRef(tbQyUserInfoPO.getOrgId(), new String[] {tbQyUserInfoPO.getUserId()});
			// 插入用户部门关联
			insertUserDeptRef(tbQyUserInfoPO.getOrgId(), tbQyUserInfoPO.getUserId(), detId);
			result.setUpdateDept(true);
			result.setOldDeptIds(oldDeptId);
		}
		else{
			result.setUpdateDept(false);
		}
		tbQyUserInfoPO.setUpdateTime(new Date());
		contactDAO.update(tbQyUserInfoPO, isUpdateNull);
		//更新扩展表的生日记录
		updateUserInfoExt(tbQyUserInfoPO,isUpdateNull);
		return result;
	}

	/**
	 * 更新用户
	 *  @param tbQyUserInfoPO
	 * @param old
	 * @param deptIds
	 * @param wxDeptIds   @return
	 * @author Sun Qinghai
	 * @ 16-4-26
	 */
	@Override
	public UpdateUserResult updateUser(TbQyUserInfoPO tbQyUserInfoPO, TbQyUserInfoPO old, List<String> deptIds, List<String> wxDeptIds, boolean isUpdateNull) throws Exception, BaseException {
		List<String> oldDeptId = departmentService.getDeptUserRefByUserId(tbQyUserInfoPO.getUserId());
		boolean isUpdateDept = !ListUtil.strCsStr(deptIds,oldDeptId);
		UpdateUserResult result = new UpdateUserResult();
		//如果两个修改前后部门信息不一致
		if(isUpdateDept){
			// 删除用户部门关联信息
			deleteUserDeptRef(tbQyUserInfoPO.getOrgId(), new String[] {tbQyUserInfoPO.getUserId()});
			// 插入用户部门关联
			insertUserDeptRef(tbQyUserInfoPO.getOrgId(), tbQyUserInfoPO.getUserId(), deptIds);
			result.setUpdateDept(true);
			result.setOldDeptIds(oldDeptId);
		}
		else{
			result.setUpdateDept(false);
		}
		tbQyUserInfoPO.setUpdateTime(new Date());
		if(ContactUtil.USER_STAtUS_LEAVE.equals(old.getUserStatus())){//如果用户是离职状态
			tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
			contactDAO.update(tbQyUserInfoPO, isUpdateNull);
			//更新扩展表的生日记录
			updateUserInfoExt(tbQyUserInfoPO,isUpdateNull);
			publishToWx(tbQyUserInfoPO,wxDeptIds);
		}
		else {
			contactDAO.update(tbQyUserInfoPO, isUpdateNull);
			//更新扩展表的生日记录
			updateUserInfoExt(tbQyUserInfoPO,isUpdateNull);
			updateToWx(tbQyUserInfoPO,old,wxDeptIds,isUpdateDept);
		}
		return result;
	}

	//同步数据到微信后台
	private void updateToWx(TbQyUserInfoPO tbQyUserInfoPO, TbQyUserInfoPO old, List<String> wxDeptIds, boolean isUpdateDept) throws Exception, BaseException{
		//如果任何信息都没有更新
		if(!isUpdateDept && StringUtil.strCstr(tbQyUserInfoPO.getPersonName(),old.getPersonName())
				&& StringUtil.strCstr(tbQyUserInfoPO.getMobile(),old.getMobile())
				&& StringUtil.strCstr(tbQyUserInfoPO.getEmail(),old.getEmail())
				&& StringUtil.strCstr(tbQyUserInfoPO.getWeixinNum(),old.getWeixinNum())
				&& StringUtil.strCstr(tbQyUserInfoPO.getSex(),old.getSex())
				&& StringUtil.strCstr(tbQyUserInfoPO.getPosition(),old.getPosition())){
			return;
		}

		WxUser user = new WxUser();
		user.setUserid(tbQyUserInfoPO.getWxUserId());
		user.setName(tbQyUserInfoPO.getPersonName());
		user.setEmail(tbQyUserInfoPO.getEmail());
		user.setGender(tbQyUserInfoPO.getSex());
		user.setMobile(tbQyUserInfoPO.getMobile());
		user.setPosition(tbQyUserInfoPO.getPosition());
		user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
		user.setDepartment(wxDeptIds);
		WxUserService.updateUser(user,tbQyUserInfoPO.getCorpId(),tbQyUserInfoPO.getOrgId());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getDeptInfoByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getDeptInfoByUserId(String orgId, String userId) throws Exception, BaseException {
		return departmentService.getDeptInfoByUserId(orgId,userId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getParentDeptByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getParentDeptByUserId(String orgId, String userId) throws Exception, BaseException {
		List<TbDepartmentInfoVO> userDepts = departmentService.getDeptInfoByUserId(orgId,userId);
		List<TbDepartmentInfoVO> depts = new ArrayList<TbDepartmentInfoVO>();
		if (userDepts != null && userDepts.size() > 0) {
			String[] split;
			TbDepartmentInfoVO vo;
			Set<String> deptFullName = new HashSet<String>();
			for (TbDepartmentInfoVO userDept : userDepts) {
				split = userDept.getDeptFullName().split("->");
				StringBuffer temp = new StringBuffer("");
				// 循环获得所有的父部门
				for (int i = 0; i < split.length; i++) {
					if (i == 0) {
						temp.append(split[i]);
					} else {
						temp.append("->" + split[i]);
					}
					// 用此方法去除重复的部门
					if (deptFullName.add(temp.toString())) {
						vo = departmentMgrService.getDepartmentVOByName(temp.toString(), orgId);
						if (vo != null) {
							depts.add(vo);
						}
					}
				}
				// 用此方法去除重复的部门
				if (deptFullName.add(userDept.getDeptFullName())) {
					depts.add(userDept);
				}
			}
		}
		return depts;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getFirstDeptByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbDepartmentInfoVO> getFirstDeptByUserId(String orgId, String userId) throws Exception, BaseException {
		List<TbDepartmentInfoVO> userDepts = departmentService.getDeptInfoByUserId(orgId,userId);
		List<TbDepartmentInfoVO> depts = new ArrayList<TbDepartmentInfoVO>();
		if (userDepts != null && userDepts.size() > 0) {
			String[] split;
			TbDepartmentInfoVO vo;
			Set<String> deptFullName = new HashSet<String>();
			for (TbDepartmentInfoVO userDept : userDepts) {
				// 如果父部门为空
				if (StringUtil.isNullEmpty(userDept.getParentDepart())) {
					// 用此方法去除重复的部门
					if (deptFullName.add(userDept.getId())) {
						depts.add(userDept);
					}
					continue;
				}
				if ("3".equals(userDept.getPermission())) {
					depts.add(userDept);
					continue;
				}
				split = userDept.getDeptFullName().split("->");
				// 说明是顶级部门
				if (split.length == 1) {
					// 用此方法去除重复的部门
					if (deptFullName.add(userDept.getId())) {
						depts.add(userDept);
					}
					continue;
				}
				// 获取顶级部门
				vo = departmentMgrService.getDepartmentVOByName(split[0], orgId);
				// 用此方法去除重复的部门
				if (vo != null && deptFullName.add(vo.getId())) {
					depts.add(vo);
				}
			}
		}
		return depts;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserIdsByOrg(java.lang.String)
	 */
	@Override
	public List<String> getUserIdsByOrg(String orgId) throws Exception, BaseException {
		return contactDAO.getUserIdsByOrg(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersVOByOrgId(java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> findUsersVOByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.findUsersVOByOrgId(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findDeptUserAllByDept(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<TbQyUserInfoVO> findDeptUserAllByDept(String orgId, String[] depts) throws Exception, BaseException {
		List<TbDepartmentInfoPO> ds = new ArrayList<TbDepartmentInfoPO>();
		for (String deptId : depts) {
			TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
			if (po == null) {
				continue;
			}
			ds.add(po);
		}
		return contactDAO.findDeptUserAllByDept(orgId, ds);
	}
	
	@Override
	public Pager findDeptUserAllByDeptForPage(String orgId, List<TbDepartmentInfoPO> depts, Pager pager) throws Exception, BaseException {
		return contactDAO.findDeptUserAllByDeptForPager(orgId, depts, pager);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#deletErrorUser(java.lang.String)
	 */
	@Override
	public void deletErrorUser(String orgId) throws Exception, BaseException {
		contactDAO.deletErrorUser(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#insertDel(java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void insertDel(String userId, String userName, String content, String type, String orgId) {
		try {
			/*
			 * ServletActionContext.getRequest().getRemoteAddr(); logger.error(ServletActionContext.getRequest().getRemoteAddr());
			 */

			// TbDeleteLogPO del=new TbDeleteLogPO();
			
			
			if(StringUtil.isNullEmpty(userId) || StringUtil.isNullEmpty(userName)){
				//如果存再空的时候，获取当前登录用户的信息
				try{
					IUser user = (IUser) DqdpAppContext.getCurrentUser();
					if(!AssertUtil.isEmpty(user)){
						//后台操作
						userId=user.getUsername();
						userName=user.getPersonName();
					}else{
						//判断手机操作
						UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
						if(!AssertUtil.isEmpty(userInfo)){
							userId=userInfo.getUserId();
							userName=userInfo.getPersonName();
						}
					}
				}catch(BaseException e){
					logger.error("BaseException获取当前登录人的信息失败，不设置删除操作记录的userId和userName");
				}catch(Exception e){
					logger.error("Exception获取当前登录人的信息失败，不设置删除操作记录的userId和userName");
				}
			}
			
			/**
			 * 2015-2-4 李盛滔 因为修改了表名，从新生成一个新的PO
			 */
			TbQyOperationLogPO del = new TbQyOperationLogPO();

			del.setLogId(UUID.randomUUID().toString());
			del.setCreateTime(new Date());
			del.setModelName(type);
			del.setOperationDesc(getOperationDesc(content));
			del.setOperationId(userId);
			del.setOperationName(userName);
			del.setOperationResult("删除成功");
			del.setOperationType("del");
			del.setOrgId(orgId);
			del.setIp(ServletActionContext.getRequest().getRemoteAddr());
			//insertPO(del, true);
			BatchAddLoginlogThread.add(del);
		} catch (Exception e) {
			logger.error(userId + "删除模块" + type + "数据失败", e);
		}
	}
	
	@Override
	public void insertDel(String userId, String userName, String content, String type, String orgId, String operationIp, String operationResult) {
		try {
			/*
			 * ServletActionContext.getRequest().getRemoteAddr(); logger.error(ServletActionContext.getRequest().getRemoteAddr());
			 */

			// TbDeleteLogPO del=new TbDeleteLogPO();
			/**
			 * 2015-2-4 李盛滔 因为修改了表名，从新生成一个新的PO
			 */
			TbQyOperationLogPO del = new TbQyOperationLogPO();

			del.setLogId(UUID.randomUUID().toString());
			del.setCreateTime(new Date());
			del.setModelName(type);
			del.setOperationDesc(getOperationDesc(content));
			del.setOperationId(userId);
			del.setOperationName(userName);
			del.setOperationResult(operationResult);
			del.setOperationType("del");
			del.setOrgId(orgId);
			if(AssertUtil.isEmpty(operationIp)){
				del.setIp(ServletActionContext.getRequest().getRemoteAddr());
			}else{
				del.setIp(operationIp);
			}
			BatchAddLoginlogThread.add(del);
		} catch (Exception e) {
			logger.error(userId + "删除模块" + type + "数据失败", e);
		}
	}


	private String getOperationDesc(String content){
		if(null!=content && content.length()>3999){
			content=content.substring(0, 3999);
		}
		return content;
	}

	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#insertOperationLog(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void insertOperationLog(String userId, String personName, String content, String type, String ModelName, String OrgId, String Result) {
		try {
			TbQyOperationLogPO del = new TbQyOperationLogPO();
			del.setLogId(UUID.randomUUID().toString());
			del.setCreateTime(new Date());
			del.setModelName(ModelName);
			del.setOperationDesc(getOperationDesc(content));
			del.setOperationId(userId);
			del.setOperationName(personName);
			del.setOperationResult(Result);
			del.setOperationType(type);
			del.setOrgId(OrgId);
			try {
				if (ActionContext.getContext() == null) {
					del.setIp("");
				} else {
					//修改获取ip方法
					del.setIp(WxqyhAppContext.getSourceIP(ServletActionContext.getRequest()));
				}
			} catch (Exception e) {
				logger.error("******************************" + userId + "模块" + type + "数据" + Result + "操作失败**********************************", e);
			}
			BatchAddLoginlogThread.add(del);
			//insertPO(del, true);
		} catch (Exception e) {
			logger.error("******************************" + userId + "模块" + type + "数据" + Result + "操作失败**********************************", e);
		}
	}

	@Override
	public int getSyncErrorUserCountByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.getSyncErrorUserCountByOrgId(orgId);
	}

	@Override
	public List<ExportUserSyncVO> getSyncErrorUserByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.getSyncErrorUserByOrgId(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersByOrgIdForInterface(java.lang.String)
	 */
	@Override
	public List<InterfaceUser> findUsersByOrgIdForInterface(String orgId) throws Exception, BaseException {
		return contactDAO.findUsersByOrgIdForInterface(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#batchDeleteUser(java.lang.String[], cn.com.do1.component.contact.contact.model.ExtOrgPO)
	 */
	@Override
	public String[] batchDeleteUser(String[] ids, UserOrgVO orgPO) throws Exception, BaseException {
		List<TbQyUserInfoVO> userList = this.getUserInfoByIds(ids);
		String[] userIds = batchDeleteUserAndRef(userList,orgPO);
		if (!AssertUtil.isEmpty(userIds)) {
			contactCustomMgrService.deleBatchUser(userIds);
            List<String> secrecyIds = new ArrayList<String>();
            for (int i = 0; i < userIds.length; i++) {
                secrecyIds.add(userIds[i]);
            }
            QwtoolUtil.delBatchList(TbQyUserSecrecyPO.class, secrecyIds);
		}
		return userIds;
	}

	@Override
	public void batchDeleteUserByUserIds(List<String> delUserIdList, UserOrgVO orgPO) throws Exception, BaseException {
		List<TbQyUserInfoVO> userList = this.getUserInfoByUserIds(ListUtil.toArrays(delUserIdList));
		batchDeleteUserAndRef(userList,orgPO);
	}

	private String[] batchDeleteUserAndRef(List<TbQyUserInfoVO> userList, UserOrgVO orgPO) throws Exception, BaseException {
		List<String> userIds = new ArrayList<String>();
		List<String> delUserIds = new ArrayList<String>();
		List<String> leaveUserIds = new ArrayList<String>();
		StringBuffer idsTemp = new StringBuffer();
		List<String> wxUserIds = new ArrayList<String>(10);
		String orgId = orgPO.getOrgId();
		for (TbQyUserInfoVO vo : userList) {
			// 如果此人的机构不在登录用户机构下，跳过
			if (null == vo || !orgId.equals(vo.getOrgId())) {
				continue;
			}
			// 已离职用户不需要删除微信上的用户信息
			if (!"-1".equals(vo.getUserStatus())) {
				if(!StringUtil.isNullEmpty(vo.getWxUserId())){
					wxUserIds.add(vo.getWxUserId());
				}
				delUserIds.add(vo.getUserId());
			}
			else{
				leaveUserIds.add(vo.getUserId());
			}
			// contactDAO.delete(po);
			// 插入删除用户日志
			this.insertDel(orgPO.getUserName(), orgPO.getUserName(), "删除用户：" + vo.getPersonName(), "addressBook", orgId, null, "删除成功");
			userIds.add(vo.getUserId());
			idsTemp.append("," + vo.getId());
			// 删除缓存中的人员数据
			if (Configuration.IS_USE_MEMCACHED) {
				CacheSessionManager.remove(vo.getUserId());
				CacheWxUser.remove(vo.getCorpId(), vo.getWxUserId());
			}
		}
		if (idsTemp.length() > 0) {
			idsTemp.deleteCharAt(0);
			this.batchDel(TbQyUserInfoPO.class, idsTemp.toString().split(","));
		}
		if (leaveUserIds.size() > 0) {
			//如果是是删除的离职用户，删掉备份的用户部门关联关系
			contactDAO.deleteUserLeaveDeptRef(orgId, leaveUserIds);
		}
		if (delUserIds.size() > 0) {
			String[] dels = ListUtil.toArrays(delUserIds);
			// 删除用户部门关联关系
			this.deleteUserDeptRef(orgId, dels);
			// 删除个人网页版账号
			contactDAO.deleteUserRelate(dels);
		}
		if(wxUserIds.size()>0){
			for (String wxUserId : wxUserIds){
				if (!WxUserService.delUser(wxUserId, orgPO.getCorpId(), orgId)) {
					throw new NonePrintException("200","删除用户失败");
				}
			}
		}
		return ListUtil.toArrays(userIds);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getAllOrgByCorp()
	 */
	@Override
	public List<ExtOrgPO> getAllOrgByCorp() throws Exception, BaseException {
		return contactDAO.getAllOrgByCorp();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#emptyOrgSecretByCorpId(java.lang.String)
	 */
	@Override
	public void emptyOrgSecretByCorpId(String corpId) throws Exception, BaseException {
		contactDAO.emptyOrgSecretByCorpId(corpId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#leaveUser(cn.com.do1.component.contact.contact.model.TbQyUserInfoPO, java.lang.String, boolean)
	 */
	@Override
	public List<String> leaveUser(TbQyUserInfoPO po, String operation, boolean isDelWeiXin, String operationIp) throws Exception, BaseException {
		// 删除个人网页版账号
		/*
		 * TbQyUserLoginInfoPO loginInfoPO = this.getUserLoginByUserId(po.getUserId()); if(loginInfoPO!=null){ contactDAO.delete(loginInfoPO); }
		 */
		List<String> userIds = new ArrayList<String>(1);
		userIds.add(po.getUserId());
		List<TbQyUserDepartmentRefPO> list = departmentMgrService.getDeptUserRefList(userIds);
		List<String> deptIds = null;
		if(list != null && list.size()>0){
			//备份并删除用户部门关联关系
			deptIds = backUpAndDelUserDeptRef(list);
		}
		contactDAO.deleteUserRelate(new String[]{po.getUserId()});

		TbQyUserInfoPO updatePO = new TbQyUserInfoPO();
		updatePO.setId(po.getId());
		updatePO.setUserStatus("-1");
		updatePO.setUpdateTime(new Date());
		//maquanyang 2015-7-20 新增离职时间和离职原因 --start
		updatePO.setLeaveTime(po.getLeaveTime());
		updatePO.setLeaveRemark(po.getLeaveRemark());
		//updatePO.setWxUserId("");
		//maquanyang 2015-7-20 新增离职时间和离职原因 --end
		contactDAO.update(updatePO, false);

		if (isDelWeiXin && !StringUtil.isNullEmpty(po.getWxUserId())) {
			// 插入删除用户日志
			this.insertDel(operation, operation, "离职用户：" + po.getPersonName(), "addressBook", po.getOrgId(), operationIp, "离职成功");

			if (!WxUserService.delUser(po.getWxUserId(), po.getCorpId(), po.getOrgId())) {
				throw new NonePrintException("200","离职用户失败");
			}
		}
		// 删除缓存中的人员数据
		if (Configuration.IS_USE_MEMCACHED) {
			CacheSessionManager.remove(po.getUserId());
		}
		return deptIds;
	}

	private List<String> backUpAndDelUserDeptRef(List<TbQyUserDepartmentRefPO> list) throws Exception, BaseException {
		String[] ids = new String[list.size()];
		List<String> deptIds = new ArrayList<String>(list.size());
		try {
			List<TbQyUserLeaveDepartmentRefPO> leaveList = new ArrayList<TbQyUserLeaveDepartmentRefPO>(list.size());
			TbQyUserLeaveDepartmentRefPO leave;
			TbQyUserDepartmentRefPO po;
			for(int i=0;i< list.size();i++){
				po = list.get(i);
				leave = new TbQyUserLeaveDepartmentRefPO();
				BeanHelper.copyBeanProperties(leave,po);
				leaveList.add(leave);
				ids[i]=po.getId();
				deptIds.add(po.getDepartmentId());
			}
			contactDAO.execBatchInsert(leaveList);
		} catch (BaseException e) {
			logger.error("backUpUserDeptRef error userId="+list.toString(),e);
			ExceptionCenter.addException(e,"backUpUserDeptRef error @sqh",list.toString());
		} catch (Exception e) {
			logger.error("backUpUserDeptRef error userId="+list.toString(),e);
			ExceptionCenter.addException(e,"backUpUserDeptRef error @sqh",list.toString());
		}
		//删除用户部门管理信息
		this.batchDel(TbQyUserDepartmentRefPO.class,ids);
		return deptIds;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getDQDPUserByCorpId(java.lang.String)
	 */
	@Override
	public UserOrgVO getDQDPUserByCorpId(String corpId) throws Exception, BaseException {
		return contactDAO.getDQDPUserByCorpId(corpId);
	}

	@Override
	public UserRedundancyInfoVO getUserRedundancyInfoByUserId(String userId) throws Exception, BaseException {
		UserInfoVO userInfoVO = this.getUserInfoByUserId(userId);
		if(null!=userInfoVO){//2015-10-26 lishengtao 增加空判断
			UserRedundancyInfoVO vo = new UserRedundancyInfoVO();
			vo.setUserId(userInfoVO.getUserId());
			vo.setOrgId(userInfoVO.getOrgId());
			vo.setPersonName(userInfoVO.getPersonName());
			vo.setWxUserId(userInfoVO.getWxUserId());
			vo.setHeadPic(userInfoVO.getHeadPic());
			vo.setDeptId(userInfoVO.getDeptIdsForRedundancy());
			vo.setDeptFullName(userInfoVO.getDeptFullNamesForRedundancy());
			return vo;
		}
		return null;
	}

	@Override
	public UserInfoVO getUserInfoNoCacheByUserId(String userId) throws Exception, BaseException {
		UserInfoVO userVO = this.contactDAO.getUserInfoByUserId(userId);
		if(userVO!=null){
			DqdpOrgVO vo = this.getOrgByOrgId(userVO.getOrgId());
			if(vo != null){
				userVO.setOrgName(vo.getOrgName());
			}
		}
		return userVO;
	}

	@Override
	public UserInfoVO getUserInfoByUserId(String userId) throws Exception, BaseException {
		if(Configuration.IS_USE_MEMCACHED){
			UserInfoVO userVO = CacheSessionManager.get(userId);
			if(userVO==null){
				if(Configuration.IS_USE_QW_API_ADDRESSBOOK){//如果私有化用的是企微的通讯录
					throw new NonePrintException("2001","登录信息已过期，请重新进入");
				}
				userVO = contactDAO.getUserInfoByUserId(userId);
				if(userVO!=null){
					DqdpOrgVO vo = this.getOrgByOrgId(userVO.getOrgId());
					if(vo != null){
						userVO.setOrgName(vo.getOrgName());
					}
					CacheSessionManager.set(userId, userVO);
				}
			}
			return userVO;
		}
		if(Configuration.IS_USE_QW_API_ADDRESSBOOK){//如果私有化用的是企微的通讯录
			throw new NonePrintException("2001","登录信息已过期，请重新进入");
		}
		UserInfoVO userVO = this.contactDAO.getUserInfoByUserId(userId);
		if(userVO!=null){
			DqdpOrgVO vo = this.getOrgByOrgId(userVO.getOrgId());
			if(vo != null){
				userVO.setOrgName(vo.getOrgName());
			}
		}
		return userVO;
	}

	@Override
	public UserInfoVO getUserInfoByWxUserId(String wxUserId, String corpId) throws Exception, BaseException {
		if(Configuration.IS_USE_MEMCACHED){
			String userId =  CacheWxUser.get(corpId,wxUserId);
			if(StringUtil.isNullEmpty(userId) || Configuration.IS_USE_QW_API_ADDRESSBOOK){//如果userId不空，或者使用企微通讯录
				UserInfoVO userInfoVO;
				if(Configuration.IS_USE_QW_API_ADDRESSBOOK){//如果私有化用的是企微的通讯录
					userInfoVO = AddressBookUtil.getUserInfoByWxUserId(corpId,wxUserId);
				}
				else{
					userInfoVO = this.contactDAO.getUserInfoByWxUserId(wxUserId, corpId);
				}
				if(null != userInfoVO){
					CacheWxUser.set(corpId, wxUserId, userInfoVO.getUserId());
					UserInfoVO cacheUserInfo = CacheSessionManager.get(userInfoVO.getUserId());
					if(cacheUserInfo != null){
						userInfoVO.setDeviceId(cacheUserInfo.getDeviceId());
					}
					CacheSessionManager.set(userInfoVO.getUserId(), userInfoVO);
				}
				return userInfoVO;
			}
			else{
				return this.getUserInfoByUserId(userId);
			}
		}

		if(Configuration.IS_USE_QW_API_ADDRESSBOOK){//如果私有化用的是企微的通讯录
			return AddressBookUtil.getUserInfoByWxUserId(corpId,wxUserId);
		}
		else{
			return this.contactDAO.getUserInfoByWxUserId(wxUserId, corpId);
		}
	}

	@Override
	public Map<String, UserRedundancyInfoVO> getUserRedundancyListByUserId(String[] userIds) throws Exception, BaseException {
		
		if (userIds == null || userIds.length == 0) {
			return new HashMap<String, UserRedundancyInfoVO>(1);
		} else {
			List<UserRedundancyInfoVO> list = getUserRedundancys(userIds);
			if (list.size() == 0) {
				return new HashMap<String, UserRedundancyInfoVO>(1);
			}
			Map<String, UserRedundancyInfoVO> map = new HashMap<String, UserRedundancyInfoVO>(list.size());
			for (UserRedundancyInfoVO vo : list) {
				map.put(vo.getUserId(), vo);
			}
			return map;
		}
	}

	@Override
	public List<UserRedundancyInfoVO> getUserRedundancySortListByUserId(String[] userIds) throws Exception, BaseException {
		if (userIds == null || userIds.length == 0) {
			return new ArrayList<UserRedundancyInfoVO>(1);
		} else {
			List<String> userIdList = getUniqUserId(userIds);
			userIds = new String[userIdList.size()];
			userIds = userIdList.toArray(userIds);
			Map<String, UserRedundancyInfoVO> map = getUserRedundancyListByUserId(userIds);
			List<UserRedundancyInfoVO> list = new ArrayList<UserRedundancyInfoVO>(map.size());
			for (String userId : userIdList) {
				if (map.containsKey(userId)) {
					list.add(map.get(userId));
				}
			}
			return list;
		}
	}

	/**
	 * 获取用户冗余信息
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-16
	 * @version 1.0
	 */
	@Override
	public List<UserRedundancyInfoVO> getUserRedundancys(String[] userIds) throws Exception, BaseException {
		int size = userIds.length;
		List<UserRedundancyInfoVO> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getUserRedundancyListByUserId(userIds);
		} else {
			list = new ArrayList<UserRedundancyInfoVO>(size);
			String[] newUserIds = new String[200];
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				System.arraycopy(userIds, startIndex, newUserIds, 0, Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getUserRedundancyListByUserId(newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = new String[size - startIndex];
				System.arraycopy(userIds, startIndex, newUserIds, 0, size - startIndex);
				list.addAll(contactReadOnlyDAO.getUserRedundancyListByUserId(newUserIds));
			}
		}
		return list;
	}
	@Override
	public List<UserRedundancyInfoVO> getUserRedundancysByWxUserIds(List<String> wxUserIds, String corpId) throws Exception {
		if (AssertUtil.isEmpty(wxUserIds)) {
			return null;
		}
		int size = wxUserIds.size();
		List<UserRedundancyInfoVO> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getUserRedundancysByWxUserIds(wxUserIds, corpId);
		} else {
			list = new ArrayList<UserRedundancyInfoVO>(size);
			List<String> newWxUserIds;
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				newWxUserIds = wxUserIds.subList(startIndex, startIndex + Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getUserRedundancysByWxUserIds(newWxUserIds, corpId));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newWxUserIds = wxUserIds.subList(startIndex, size);
				list.addAll(contactReadOnlyDAO.getUserRedundancysByWxUserIds(newWxUserIds, corpId));
			}
		}
		return list;
	}

	/**
	 * 获取用户冗余信息
	 * @param userIds
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-16
	 * @version 1.0
	 */
	@Override
	public List<UserRedundancyExtInfoVO> getUserRedundancyExts(String[] userIds) throws Exception {
		int size = userIds.length;
		List<UserRedundancyExtInfoVO> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getUserRedundancyExtListByUserId(userIds);
		} else {
			list = new ArrayList<UserRedundancyExtInfoVO>(size);
			String[] newUserIds = new String[200];
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				System.arraycopy(userIds, startIndex, newUserIds, 0, Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getUserRedundancyExtListByUserId(newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = new String[size - startIndex];
				System.arraycopy(userIds, startIndex, newUserIds, 0, size - startIndex);
				list.addAll(contactReadOnlyDAO.getUserRedundancyExtListByUserId(newUserIds));
			}
		}
		return list;
	}

	/**
	 * 得到不重复的userId
	 * @return
	 * @author Sun Qinghai
	 * @2016-3-16
	 * @version 1.0
	 */
	private List<String> getUniqUserId(String[] userIds){
		List<String> list = new ArrayList<String>();
		for (String userId : userIds) {
			if(!list.contains(userId)){
				list.add(userId);
			}
		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#isExitsOrgName(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExitsOrgName(String orgName, String orgPid) throws Exception {
		return this.contactDAO.isExitsOrgName(orgName, orgPid);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#searchContactManager(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager searchContactManager(Map searchMap, Pager pager) throws Exception, BaseException {
		// 选了部门
		if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
			TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
			if (department == null) {
				return pager;
			}
			// Map map=new Map();
			searchMap.put("department", department.getDeptFullName() + "->%");
			// searchMap.remove("deptId");
			return contactDAO.searchContact(searchMap, pager);
		} else {
			return contactDAO.searchContactManagerByOrgId(searchMap, pager);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersManagerByOrgIdForExport(java.lang.String)
	 */
	@Override
	public List<ExportUserInfo> findUsersManagerByOrgIdForExport(String orgId, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
		return this.contactDAO.findUsersManagerByOrgIdForExport(orgId, deptList);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getManagerPersonByUserNameAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbManagerPersonVO getManagerPersonByUserNameAndOrgId(String userName, String orgId) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.contactDAO.getManagerPersonByUserNameAndOrgId(userName, orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getManagerUserCheckResultByAge(java.lang.Integer)
	 */
	@Override
	public ManagerUserCheckResult getManagerUserCheckResultByAge(Integer age, String personId, String orgId) throws Exception, BaseException {
		String departIdsManager = "";
		ManagerUserCheckResult result = new ManagerUserCheckResult();
		if (ManageUtil.superAdmin != age) {
			TbManagerPersonVO tbManagerPersonVO = this.contactDAO.getManagerPersonByPersonIdAndOrgId(personId, orgId);
			if (!AssertUtil.isEmpty(tbManagerPersonVO)) {
				if (ManageUtil.RANGE_THREE.equals(tbManagerPersonVO.getRanges())) {// 普通管理员并存在管理的部门
					// 管理的部门
					if (!AssertUtil.isEmpty(tbManagerPersonVO.getDepartids()) && (tbManagerPersonVO.getDepartids().length() > 0)) {
						departIdsManager = tbManagerPersonVO.getDepartids();
					}
					result.setIsOrdinaryManager(ManageUtil.MANAGER_THREE);
					result.setDepartIds(departIdsManager);
				} else {// 普通管理员但管理的是所有人
					result.setIsOrdinaryManager(ManageUtil.MANAGER_TWO);
				}
			} else {// 普通管理员但管理的是所有人
				result.setIsOrdinaryManager(ManageUtil.MANAGER_TWO);
			}
		} else {// 超级管理员
			result.setIsOrdinaryManager(ManageUtil.MANAGER_ONE);
		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersManagerByOrgId(java.lang.String, java.util.List)
	 */
	@Override
	public List<TbQyUserInfoView> findUsersManagerByOrgId(String orgId, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
		return this.contactDAO.findUsersManagerByOrgId(orgId, deptList);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#ManagerCheck(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public void ManagerCheck(Map searchMap) throws Exception, BaseException {
		logger.info("searchMap:" + searchMap);
		Integer age = (Integer) searchMap.get("age");
		String personId = (String) searchMap.get("personId");
		String orgId = (String) searchMap.get("orgId");
		String deptIds = (String) searchMap.get("deptIds");
		String userIds = (String) searchMap.get("userIds");
		String givenUserIds = (String) searchMap.get("givenUserIds");
		String userId = (String) searchMap.get("userId");
		String rangeManager = ManageUtil.MANAGER_ONE;// 超级管理员
		String departIdsManager = "";
		ManagerUserCheckResult result = new ManagerUserCheckResult();
		// 判断是否 为普通管理员并且有管理 的 部门
		if (!AssertUtil.isEmpty(age)) {
			result = this.getManagerUserCheckResultByAge(age, personId, orgId);
			if (!AssertUtil.isEmpty(result)) {
				rangeManager = result.getIsOrdinaryManager();
				departIdsManager = result.getDepartIds();
			}
		}

		logger.info("rangeManager:" + rangeManager);
		logger.info("departIdsManager:" + departIdsManager);
		Map<String, Object> mapDeparts = new HashMap<String, Object>();
		Map<String, Object> mapUsers = new HashMap<String, Object>();
		if (rangeManager.equals(ManageUtil.MANAGER_THREE)) {
			// 取到普通管理员管理的部门及其子部门的id和部门名称及这些部门下的人员
			if (!AssertUtil.isEmpty(rangeManager)) {
				String[] departIds = departIdsManager.split("\\|");
				List deptList = this.getDeptInfo(departIds);

				List<TbQyOrganizeInfo> lsitAllDep = departmentMgrService.getListDeptNodeByDeparts(orgId, "", deptList);
				for (int x = 0; x < lsitAllDep.size(); ++x) {// 取到管理的的部门
					TbQyOrganizeInfo dPo = lsitAllDep.get(x);
					if (!AssertUtil.isEmpty(dPo) && !StringUtil.isNullEmpty(dPo.getNodeId())) {
						mapDeparts.put(dPo.getNodeId(), dPo.getNodeName());
					}

				}
				// 只有选择了人员或givenUserIds值不为空才取出普通管理员管理的部门的人员
				if (!StringUtil.isNullEmpty(userIds) || !StringUtil.isNullEmpty(givenUserIds)) {
					List<TbQyUserInfoView> listAlluser = this.findUsersManagerByOrgId(orgId, deptList);
					for (int x = 0; x < listAlluser.size(); ++x) {// 取到管理的的部门的人员
						TbQyUserInfoView dinfo = listAlluser.get(x);
						if (!AssertUtil.isEmpty(dinfo) && !StringUtil.isNullEmpty(dinfo.getUserId())) {
							mapUsers.put(dinfo.getUserId(), dinfo.getPersonName());
						}

					}
				}
			}
			// 判断选择的部门是否在取到普通管理员管理的部门及其子部门内，若选择的部门全在则继续保存，否则抛出异常提示
			boolean b = true;
			if (!AssertUtil.isEmpty(deptIds) && (deptIds.length() > 0) && !AssertUtil.isEmpty(mapDeparts)) {
				String[] departIds = deptIds.split("\\|");
				List deptList = this.getDeptInfo(departIds);
				String reMsg = "";
				logger.info("mapDeparts:" + mapDeparts);
				for (int x = 0; x < deptList.size(); ++x) {
					TbDepartmentInfoPO dPo = (TbDepartmentInfoPO) deptList.get(x);
					if (!AssertUtil.isEmpty(dPo) && !AssertUtil.isEmpty(dPo.getId()) && !mapDeparts.containsKey(dPo.getId())) {
						logger.info("dPo.getId():" + dPo.getId());
						b = false;
						reMsg = reMsg + dPo.getDepartmentName() + "、";
					}
				}
				if (!b) {// 存在不属于它管理的部门则抛出异常
					if (!AssertUtil.isEmpty(reMsg)) {
						reMsg = reMsg.substring(0, reMsg.length() - 1);
					}
					logger.error("部门【" + reMsg + "】不是用户【" + userId + "】管理的部门");
					throw new NonePrintException("200","部门【" + reMsg + "】不是您管理的部门");
				}
			}
			// 判断选择的人员是否在取到普通管理员管理的部门及其子部门的人员内，若选择的人员全在则继续保存，否则抛出异常提示
			boolean bu = true;
			if (!AssertUtil.isEmpty(userIds) && (userIds.length() > 0) && !AssertUtil.isEmpty(mapUsers)) {
				String[] users = userIds.split("\\|");
				List<TbQyUserInfoVO> personList = this.getAllUserInfoByIds(users);
				String reMsg = "";
				for (int x = 0; x < personList.size(); ++x) {
					TbQyUserInfoVO vo = personList.get(x);
					// 判断人员是否是公共群组的 人。是则不需要再判断该人员是否在普通管理员管理的范围内,否则需要判断
					int countuser = this.defatgroupMgrService.countUserGroupByUserIdAndOrgId(vo.getUserId(), orgId);
					if (countuser > 0) {// 是公共群组的人员
						logger.info("人员【" + vo.getPersonName() + "】是公共群组的人.");
						continue;
					}
					if (!AssertUtil.isEmpty(vo) && !AssertUtil.isEmpty(vo.getUserId()) && !mapUsers.containsKey(vo.getUserId())) {
						bu = false;
						reMsg = reMsg + vo.getPersonName() + "、";
					}
				}
				if (!bu) {// 存在不属于它管理的部门的人员则抛出异常
					if (!AssertUtil.isEmpty(reMsg)) {
						reMsg = reMsg.substring(0, reMsg.length() - 1);
					}
					logger.error("人员【" + reMsg + "】不是用户【" + userId + "】管理的部门中的人员");
					throw new NonePrintException("200","人员【" + reMsg + "】不是您管理的部门中的人员");
				}
			}

			// 判断选择的人员是否在取到普通管理员管理的部门及其子部门的人员内，若选择的人员全在则继续保存，否则抛出异常提示
			boolean bool = true;
			if (!AssertUtil.isEmpty(givenUserIds) && (givenUserIds.length() > 0) && !AssertUtil.isEmpty(mapUsers)) {
				String[] users = givenUserIds.split("\\|");
				List<TbQyUserInfoVO> personList = this.getAllUserInfoByIds(users);
				String reMsg = "";
				for (int x = 0; x < personList.size(); ++x) {
					TbQyUserInfoVO vo = personList.get(x);
					if (!AssertUtil.isEmpty(vo) && !AssertUtil.isEmpty(vo.getUserId()) && !mapUsers.containsKey(vo.getUserId())) {
						bool = false;
						reMsg = reMsg + vo.getPersonName() + "、";
					}
				}
				if (!bool) {// 存在不属于它管理的部门的人员则抛出异常
					if (!AssertUtil.isEmpty(reMsg)) {
						reMsg = reMsg.substring(0, reMsg.length() - 1);
					}
					logger.error("人员【" + reMsg + "】不是用户【" + userId + "】管理的部门中的人员");
					throw new NonePrintException("200","人员【" + reMsg + "】不是您管理的部门中的人员");
				}
			}
		}

	}

	public List<TbDepartmentInfoPO> getDeptInfo(String[] departIds) throws Exception, BaseException {
		List<TbDepartmentInfoPO> list = new ArrayList<TbDepartmentInfoPO>();
		if (departIds == null || departIds.length == 0) {
			return list;
		}
		String deptId;
		int size = departIds.length;
		for (int i = 0; i < size; i++) {
			deptId = departIds[i].trim();
			if (!StringUtil.isNullEmpty(deptId)) {
				list.add(departmentMgrService.searchByPk(TbDepartmentInfoPO.class, deptId));
			}
		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#searchDqdpUserPersonInfoByUserName(java.lang.String)
	 */
	@Override
	public DqdpUserPersonInfoVO searchDqdpUserPersonInfoByUserName(String userName) throws Exception, BaseException {
		return this.contactDAO.searchDqdpUserPersonInfoByUserName(userName);
	}

	@Override
	public boolean isRange(Map<String, Object> map) throws Exception, BaseException {
		if ("1".equals(map.get("range"))) {
			return true;
		} else if ("2".equals(map.get("range"))) {
			String deptIds = map.get("deptIds").toString();
			List<TbDepartmentInfoVO> depts = this.getParentDeptByUserId(map.get("orgId").toString(), map.get("userId").toString());
			for (TbDepartmentInfoVO d : depts) {
				if (deptIds.contains(d.getId())) {
					return true;
				}
			}
		} else {// 3-特定对象：
			// 判断目标用户是否含有当前用户
			if (!AssertUtil.isEmpty(map.get("userIds"))) {
				String[] userIdList = map.get("userIds").toString().split("\\|");
				for (String userId : userIdList) {
					if (userId.equals(map.get("userId"))) {
						return true;
					}
				}
			}
			// 判断特定部门
			if(!AssertUtil.isEmpty(map.get("deptIds"))){
				String deptIds = map.get("deptIds").toString();
				List<TbDepartmentInfoVO> depts = this.getParentDeptByUserId(map.get("orgId").toString(), map.get("userId").toString());
				for (TbDepartmentInfoVO d : depts) {
					if (deptIds.contains(d.getId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getManagerPersonByPersonIdAndOrgId(java.lang.String, java.lang.String)
	 */
	@Override
	public TbManagerPersonVO getManagerPersonByPersonIdAndOrgId(String personId, String orgId) throws Exception, BaseException {
		TbManagerPersonVO vo = ManagesettingCacheUtil.getManagerPersonCache(orgId, personId);
		if (vo == null) {
			vo = this.contactDAO.getManagerPersonByPersonIdAndOrgId(personId, orgId);
			if (vo != null) {
				ManagesettingCacheUtil.setManagerPersonCache(vo);
			}
		}
		return vo;
	}

	@Override
	public Pager searchAddContact(Map searchMap, Pager pager) throws Exception, BaseException {
		// TODO Auto-generated method stub
		if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
			TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
			if (department == null) {
				return pager;
			}
			// Map map=new Map();
			searchMap.put("department", department.getDeptFullName() + "->%");
			// searchMap.remove("deptId");
			return contactDAO.searchAllContact(searchMap, pager);
		} else {
			return contactDAO.searchAllContactByOrgId(searchMap, pager);
		}
	}

	@Override
	public Pager searchAllContactManager(Map searchMap, Pager pager) throws Exception, BaseException {
		// TODO Auto-generated method stub
		// 选了部门
		if (!AssertUtil.isEmpty(searchMap.get("deptId"))) {
			TbDepartmentInfoPO department = departmentMgrService.searchDepartment(searchMap);
			if (department == null) {
				return pager;
			}
			// Map map=new Map();
			searchMap.put("department", department.getDeptFullName() + "->%");
			// searchMap.remove("deptId");
			return contactDAO.searchAllContact(searchMap, pager);
		} else {
			return contactDAO.searchAllContactManagerByOrgId(searchMap, pager);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findTbQyFieldSettingVOList(java.lang.String)
	 */
	@Override
	public List<TbQyFieldSettingVO> findTbQyFieldSettingVOListByOrgId(String orgId) throws Exception, BaseException {
		return this.contactDAO.findTbQyFieldSettingVOListByOrgId(orgId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#saveField(net.sf.json.JSONObject, net.sf.json.JSONObject, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveField(JSONObject jData, String orgId, String userId) throws Exception, BaseException {
		// 删除旧数据
		this.contactDAO.delTbQyFieldSettingVOListByOrgId(orgId);

		TbQyFieldSettingPO posoc = new TbQyFieldSettingPO();
		int y = 0;
		String id = "";
		List<TbQyFieldSettingPO> listFieldSettingPOs = new ArrayList<TbQyFieldSettingPO>();
		// 设置数据
		if ((jData != null) && (jData.has("list"))) {
			for (JSONObject jo2 : (List<JSONObject>) jData.getJSONArray("list")) {
				++y;
				id = UUID.randomUUID().toString();
				TbQyFieldSettingPO po = (TbQyFieldSettingPO) posoc.clone();
				po.setId(id);
				po.setField(jo2.getString("field"));
				po.setFieldName(jo2.getString("name"));
				po.setIsDisplay(jo2.getString("isDisplay"));
				po.setFieldType("tbQyUserInfo");
				Date nowDate = new Date();
				po.setCreator(userId);
				po.setCreateTime(nowDate);
				po.setEditor(userId);
				po.setEditTime(nowDate);
				po.setSort(y);
				po.setOrgId(orgId);
				po.setExt1("");
				po.setExt2("");
				po.setExt3("");
				po.setIsEdit(jo2.getString("isEdit"));//是否可修改
				listFieldSettingPOs.add(po);
			}
		}
		this.contactDAO.execBatchInsert(listFieldSettingPOs);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.com.do1.component.contact.contact.service.IContactService#udpatePersonInfo(java.util.Map)
	 */
	@Override
	public void addUdpatePersonInfo(Map<String, String> map) throws Exception, BaseException {
		TbUpdateUserInfoPO po = new TbUpdateUserInfoPO();
		po.setUserId(map.get("userId"));
		po.setOrgId(map.get("orgId"));
		po.setCreator(map.get("creator"));
		po.setCreateTime(new Date());
		po.setIsUpdate("0");
		po.setItem1(map.get("item1"));
		po.setItem2(map.get("item2"));
		po.setItem3(map.get("item3"));
		po.setItem4(map.get("item4"));
		po.setItem5(map.get("item5"));
		po.setItem6(map.get("item6"));
		po.setItem7(map.get("item7"));
		po.setItem8(map.get("item8"));
		po.setIs_manager(map.get("is_manager"));
		this.insertPO(po, true);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getSendObject(cn.com.do1.component.contact.contact.vo.UserInfoVO, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SendObjectVO getSendObject(UserInfoVO userInfo, String range,
			String deptIds, String userIds) throws Exception, BaseException {
		return this.getSendObject(userInfo.getOrgId(), userInfo.getCorpId(), userInfo.getUserId(), userInfo.getWxUserId(), null, range, deptIds, userIds);
	}
	/*
	 * （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getSendObject(cn.com.do1.component.contact.contact.vo.UserOrgVO, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SendObjectVO getSendObject(UserOrgVO org, String range,
			String deptIds, String userIds) throws Exception, BaseException {
		return this.getSendObject(org.getOrgId(), org.getCorpId(), org.getUserName(), null, org.getWxId(), range, deptIds, userIds);
	}

	private SendObjectVO getSendObject(String orgId, String corpId, String userId, String wxUserId, String orgWxId, String range,
			String deptIds, String userIds) throws Exception, BaseException {
		StringBuffer wxUserIds = new StringBuffer();
		StringBuffer msgDeptIds = new StringBuffer();
		if ("1".equals(range)) {// 全公司
			// 不是道一的企业微信才可以这样发送全公司
			if (!corpId.equals(Configuration.AUTO_CORPID)) {
				wxUserIds.append("@all");
			}
			else if(StringUtil.isNullEmpty(orgWxId)){
				ExtOrgPO orgPO = contactDAO.searchByPk(ExtOrgPO.class, orgId);
				msgDeptIds.append(orgPO.getWxId());
			}
			else{
				msgDeptIds.append(orgWxId);
			}
		} else if ("2".equals(range)) {// 本部门
			// 获取用户的顶级部门
			List<TbDepartmentInfoVO> depts = this.getFirstDeptByUserId(orgId, userId);
			if (depts != null && depts.size() > 0) {
				for (TbDepartmentInfoVO vo : depts) {
					msgDeptIds.append("|" + vo.getWxId());
				}
				if (msgDeptIds.length() > 0) {
					msgDeptIds.deleteCharAt(0);
				}
			}
		} else {// 特定对象
			if (!AssertUtil.isEmpty(deptIds)) {
				String[] arrayDepts = deptIds.split("\\|");
				TbDepartmentInfoPO department;
				for (String deptId : arrayDepts) {
					department = contactDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
					if (department == null || department.getWxId() == null || !orgId.equals(department.getOrgId())) {
						continue;
					}
					msgDeptIds.append("|" + department.getWxId());
				}
				if (msgDeptIds.length() > 0) {
					msgDeptIds.deleteCharAt(0);
				}
			}
			if (userIds == null || userIds.isEmpty()) {
				wxUserIds.append(wxUserId);
			} else {
				if (!userIds.contains(userId) && !StringUtil.isNullEmpty(wxUserId)) {
					wxUserIds.append("|" + wxUserId);
				}
				String[] commentUsers = userIds.split("\\|");
				TbQyUserInfoVO user;
				for (String commentUser : commentUsers) {
					user = contactDAO.findUserInfoByUserId(commentUser);
					// 如果人员信息有误
					if (user == null || !corpId.equals(user.getCorpId())) {
						continue;
					}
					if (!"2".equals(user.getUserStatus())) {//只发送已关注用户
						continue;
					}
					wxUserIds.append("|" + user.getWxUserId());
				}
				if (wxUserIds.length() > 0) {
					wxUserIds.deleteCharAt(0);// 删掉第一个|线
				}
			}
		}
		SendObjectVO sendObjectVO = new SendObjectVO();
		sendObjectVO.setWxDeptIds(msgDeptIds.toString());
		sendObjectVO.setWxUserIds(wxUserIds.toString());
		return sendObjectVO;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#isTargetUser(cn.com.do1.component.contact.contact.vo.UserInfoVO, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isTargetUser(UserInfoVO userInfo, String range,
			String targetDeptIds, String targetUserIds, String targetOrgId)
					throws Exception, BaseException {
		if ("1".equals(range)) {// 全公司
			// 不是道一的企业微信才可以这样发送全公司
			if (userInfo.getOrgId().equals(targetOrgId)) {
				return true;
			}
		} else if ("2".equals(range)) {// 本部门
			if(null == targetDeptIds){
				return false;
			}
			List<TbDepartmentInfoVO> depts = this.getParentDeptByUserId(userInfo.getOrgId(), userInfo.getUserId());
			for (TbDepartmentInfoVO d : depts) {
				if (targetDeptIds.contains(d.getId())) {
					return true;
				}
			}
		} else {// 特定对象
			// 判断目标用户是否含有当前用户
			if (null != targetUserIds && ("|"+targetUserIds+"|").contains("|"+userInfo.getUserId()+"|")) {
				return true;
			} else {
				if(null != targetDeptIds){
					List<TbDepartmentInfoVO> depts = this.getParentDeptByUserId(userInfo.getOrgId(), userInfo.getUserId());
					for (TbDepartmentInfoVO d : depts) {
						if (targetDeptIds.contains(d.getId())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}


	@Override
	public List<TbQyUserInfoVO> findUserInfoPOByDepartmentId(String departmentId) throws Exception, BaseException {
		// TODO Auto-generated method stub
		return this.contactDAO.findUserInfoPOByDepartmentId(departmentId);
	}


	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findListByNameOrPhone(java.util.Map, cn.com.do1.component.contact.contact.vo.UserInfoVO)
	 */
	@Override
	public List<TbQyUserInfoVO> findListByNameOrPhone(Map<String, Object> map,
			UserInfoVO user) throws Exception, BaseException {
		String orgId = user.getOrgId();
		map.put("aliveStatus", "-1");// 过滤掉离职状态的用户
		String keyWord = (String) map.get("keyWord");
		/*
		 * 何金娇 2014.7.22 修改按拼音查询
		 */
		Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
		Matcher ma = patter.matcher(keyWord);
		StringBuilder sb = new StringBuilder("");
		// 在字母中间添加%
		if (ma.find()) {
			for (char iterable_element : keyWord.toCharArray()) {
				sb.append(iterable_element).append("%");
			}
		}
		if (sb.length() > 0)
			sb = sb.replace(sb.length() - 1, sb.length(), "");

		if (sb.length() > 0)
			keyWord = sb.toString();

		if (!AssertUtil.isEmpty(keyWord)) {
			map.put("keyWord", "%" + keyWord.toLowerCase() + "%");

		}
		// 人员通讯录添加限制 chenfeixiong 2014/08/12 1为全公司 2为一级部门 3为本部门
		List<TbDepartmentInfoVO> depts = departmentService.getDeptPermissionByUserId(orgId, user.getUserId());
		// 如果没有部门或者第一个部门的信息的权限就是全公司，跳过部门权限设置
		if (depts != null && depts.size() > 0 && !AssertUtil.isEmpty(depts.get(0)) && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !"1".equals(depts.get(0).getPermission())) {
			return contactDAO.findListByNameOrPhone(map, depts);
		}
		return contactDAO.findListByNameOrPhone(map, null);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserSearchByTbQyUserSearchVO(cn.com.do1.component.contact.contact.vo.TbQyUserSearchVO)
	 */
	@Override
	public TbQyUserSearchVO getUserSearchByTbQyUserSearchVO(TbQyUserSearchVO vo)
			throws Exception, BaseException {
		return this.contactDAO.getUserSearchByTbQyUserSearchVO(vo);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUsersVOByOrgIdAndUserStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public List<UserInfoVO> findUsersVOByOrgIdAndUserStatus(String orgId,
			String userStatus) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.contactDAO.findUsersVOByOrgIdAndUserStatus(orgId, userStatus);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findUserInfoDepartByUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public UserInfoVO findUserInfoDepartByUserId(String userId,
			String userStatus) throws Exception, BaseException {
		return this.contactDAO.findUserInfoDepartByUserId(userId, userStatus);
	}



	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getSetFiled(java.lang.String)
	 */
	@Override
	public String getSetFiled(String orgId) throws Exception, BaseException {
		String isDisplay = ContactUtil.IS_DISPLAY_0;
		//maquanyang 2015-7-20 新增查询设置通讯录字段是否允许员工可自行修改
		List<TbQyFieldSettingVO> fieldList = this.contactDAO.findTbQyFieldSettingVOListByOrgId(orgId);
		if(!AssertUtil.isEmpty(fieldList) && fieldList.size() > 0){
			for(TbQyFieldSettingVO fieldVO : fieldList){
				//控制那些内容在页面上需要
				if(ContactUtil.FIELD_NAME_3.equals(fieldVO.getField())){//手机号码
					if(ContactUtil.IS_DISPLAY_1.equals(fieldVO.getIsDisplay())){//不显示
						isDisplay = ContactUtil.IS_EDIT_1;
						break;
					}
				}
			}
		}
		return isDisplay;
	}

	@Override
	public List<ExportUserInfo> findUsersForExport(Map<String, Object> params, String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.contactReadOnlyDAO.findUsersForExport(params, IndustryUtil.isEduVersion(orgId));
	}

	@Override
	public List<ExportUserInfo> findUsersManagerForExport(
			Map<String, Object> params, List<TbDepartmentInfoPO> deptList, String orgId)
					throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.contactReadOnlyDAO.findUsersManagerForExport(params, deptList, IndustryUtil.isEduVersion(orgId));
	}

	@Override
	public UserOrgVO getUserOrgVOByUserName(String userName) throws Exception,
	BaseException {
		UserOrgVO vo=contactDAO.getOrgByUserId(userName);
		if(vo!= null && vo.getAge()==null){
			vo.setAge(0);
		}
		return vo;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#findDeptUserAllByDept(java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public List<UserInfoVO> findDeptUserAllByDeptAndSeachType(String orgId,
			String[] depts, String seachType) throws Exception, BaseException {
		List<TbDepartmentInfoPO> ds = new ArrayList<TbDepartmentInfoPO>();
		for (String deptId : depts) {
			TbDepartmentInfoPO po = contactDAO.searchByPk(TbDepartmentInfoPO.class, deptId);
			if (po == null) {
				continue;
			}
			ds.add(po);
		}
		return contactDAO.findDeptUserAllByDeptAndSeachType(orgId, ds, seachType);
	}

	@Override
	public void updateTop(String userId, String type, UserOrgVO userOrgVO)
			throws Exception, BaseException {
		// TODO 自动生成的方法存
		TbQyUserInfoPO po;
		if("0".equals(type)){//取消置顶
			po=contactDAO.searchByPk(TbQyUserInfoPO.class, userId);
			//po.setIsTop(0);// isTop>0或null-置顶;
			po.setIsTop(null);
			contactDAO.update(po, true);
		}else if("1".equals(type)){//置顶
			//置顶排序的最高序号
			po=new TbQyUserInfoPO();
			TbQyUserInfoPO topPO=contactDAO.getTopUserInfoPO(userOrgVO.getOrgId());
			if(topPO!=null){
				if(!AssertUtil.isEmpty(topPO.getIsTop())){
					po.setIsTop(topPO.getIsTop()+1);//设置最高排序号+1
				}else{
					po.setIsTop(1);
				}
				po.setId(userId);
				contactDAO.update(po, false);
			}
		}
	}

	@Override
	public Pager getUserInfoCertificateTypePager(Map<String, Object> params,
			Pager pager) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getUserInfoCertificateTypePager(params, pager);
	}

	@Override
	public List<TbQyUserInfoCertificateTypePO> getUserInfoCertificateTypeList(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getUserInfoCertificateTypeList(params);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#countAllPerson()
	 */
	@Override
	public int countAllPerson() throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.countAllPerson();
	}

	@Override
	public String delCertificateType(String[] ids, UserOrgVO userOrgVO)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		StringBuilder rMsg=new StringBuilder("");
		StringBuilder operateStr=new StringBuilder("");
		if (ids != null && ids.length > 0){ 
			rMsg.append("操作结果：");
			List<String> list=Arrays.asList(ids);
			List<String> successList=new ArrayList<String>();
			Map<String,Object> params;
			TbQyUserInfoCertificateTypePO po;
			int failCount=0;
			for (String id : list) {
				po= this.searchByPk(TbQyUserInfoCertificateTypePO.class, id);
				if(po==null){
					rMsg.append(id+":证件类型不存在;");
					failCount=failCount+1;
					continue;
				}
				if (!userOrgVO.getOrgId().equals(po.getOrgId())) {
					rMsg.append(po.getTitle()+":无法删除不是本机构下的数据");
					failCount=failCount+1;
					continue;
				}
				params=new HashMap<String,Object>();
				params.put("orgId", userOrgVO.getOrgId());
				params.put("id", po.getId());
				if(contactDAO.getCertificateTypeCount(params)>0){
					rMsg.append(po.getTitle()+":该证件类型有用户数据，不能删除。");
					failCount=failCount+1;
					continue;
				}
				operateStr.append(po.getTitle()+";");
				successList.add(id);
			}
			if(successList.size()>0){
				String[] targetIds=new String[successList.size()];
				for(int i=0;i<successList.size();i++){
					targetIds[i]=(String)successList.get(i);
				}
				this.insertOperationLog(userOrgVO.getUserName(), userOrgVO.getPersonName(), "删除证件类型：" +operateStr.toString(), "del", "addressBook", userOrgVO.getOrgId(), "操作成功");
				this.batchDel(TbQyUserInfoCertificateTypePO.class, targetIds);
			}
			if(failCount==0){
				rMsg.append("删除成功！");
			}
		}else{
			rMsg.append("没有可删除的数据!");
		}
		return rMsg.toString();
	}

	@Override
	public List<TbQyUserInfoCertificateTypePO> getCertificateTypeListByTitle(
			String title, String orgId) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getCertificateTypeListByTitle(title, orgId);
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoByRange(Map<String, Object> params)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		List<TbQyUserInfoVO> list = null;

		String range="";//范围:1-所有人;2-本部门;3-特定对象
		if(!AssertUtil.isEmpty(params.get("range"))){
			range=params.get("range").toString();//范围
		}
		String deptIds="";
		if(!AssertUtil.isEmpty(params.get("deptIds"))){
			deptIds=params.get("deptIds").toString();//部门Ids
		}
		String userIds="";//userIds
		if(!AssertUtil.isEmpty(params.get("userIds"))){
			userIds=params.get("userIds").toString();//人员
		}
		String orgId="其它";//默认是搜不出来的orgId
		if(!AssertUtil.isEmpty(params.get("orgId"))){
			orgId=params.get("orgId").toString();//机构ID
		}

		if("1".equals(range)){
			//所有人
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("orgId", orgId);
			paramMap.put("leaveStatus", "-1");//去除离职
			list = contactReadOnlyDAO.findUsersVOByOrgId(paramMap);
		}else if("2".equals(range)){
			//本部门
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("leaveStatus", "-1");//去除离职
			if (!StringUtil.isNullEmpty(deptIds)) {
				Set<String> depts = departmentService.getAllChildDepartIds(deptIds.split("\\|"), orgId);
				paramMap.put("tbDepartmentInfoPOList", new ArrayList<String>(depts));
				list = contactReadOnlyDAO.findUsersVOByOrgId(paramMap);
			}
		}else if("3".equals(range)){
			//特定对象
			//特定部门
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("leaveStatus", "-1");//去除离职
			Map<String, Object> userMap = new HashMap<String, Object>(paramMap);
			if (!StringUtil.isNullEmpty(deptIds)) {
				Set<String> depts = departmentService.getAllChildDepartIds(deptIds.split("\\|"), orgId);
				if (!AssertUtil.isEmpty(depts)) {
					paramMap.put("tbDepartmentInfoPOList", new ArrayList<String>(depts));
					list = contactReadOnlyDAO.findUsersVOByOrgId(paramMap);
				}
			}
			//特定人员
			if(!AssertUtil.isEmpty(userIds)){
				String userArray[]=userIds.split("\\|");
				userMap.put("userIds", userArray);
				if (!AssertUtil.isEmpty(list)) {
					List<TbQyUserInfoVO> temp = contactReadOnlyDAO.findUsersVOByUserIds(userMap);
					if (!AssertUtil.isEmpty(temp)) {
						Set<String> ids = new HashSet<String>(list.size());
						for (TbQyUserInfoVO vo :list) {
							ids.add(vo.getId());
						}
						for (TbQyUserInfoVO vo :temp) {
							if (!ids.contains(vo.getId())) {
								list.add(vo);
							}
						}
					}
				}
				else {
					list = contactReadOnlyDAO.findUsersVOByUserIds(userMap);
				}
			}
		}
		return list;
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#searchUserByPersonName(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public Pager searchUserByPersonName(Map<String, Object> params, Pager pager)
			throws Exception, BaseException {
		
		return contactDAO.searchUserByPersonName(params,  pager);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#searchDeptByDeptName(java.util.Map, cn.com.do1.common.dac.Pager)
	 */
	@Override
	public List<TbDepartmentInfoVO> searchDeptByDeptName(Map<String, Object> params)
			throws Exception, BaseException {
		
		return contactDAO.searchDeptByDeptName( params);
	}

	@Override
	public void updateUserInfoExt(TbQyUserInfoPO tbQyUserInfoPO,boolean isUpdateNull)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		if(null!=tbQyUserInfoPO){
			TbQyUserInfoExtPO extPO=contactDAO.getUserInfoExtPOByUserId(tbQyUserInfoPO.getUserId());
			if(null==extPO){
				extPO=new TbQyUserInfoExtPO();
				extPO.setUserId(tbQyUserInfoPO.getUserId());
			}
			//设置生日
			if(!AssertUtil.isEmpty(tbQyUserInfoPO.getBirthday())){
				extPO.setBirthMonthDay(DateUtil.format(tbQyUserInfoPO.getBirthday(), "MM-dd"));
			}else{
				extPO.setBirthMonthDay(null);
			}
			//农历生日
			if(!AssertUtil.isEmpty(tbQyUserInfoPO.getLunarCalendar())){
				extPO.setBirthLunarMonthDay(tbQyUserInfoPO.getLunarCalendar());
			}else{
				extPO.setBirthLunarMonthDay(null);
			}
			//入职
			if(!AssertUtil.isEmpty(tbQyUserInfoPO.getEntryTime())){
				extPO.setEntryMonthDay(DateUtil.format(tbQyUserInfoPO.getEntryTime(), "MM-dd")); 
			}else{
				extPO.setEntryMonthDay(null);
			}
			//表明已更新过
			extPO.setIsUpdate(1);
			
			if(!AssertUtil.isEmpty(extPO.getId())){
				//更新数据
				this.updatePO(extPO, isUpdateNull);
			}else{
				//插入数据
				this.insertPO(extPO, true);
			}
		}
	}

	@Override
	public void initUserInfoExt(long currentPage,String isUpdate,int pageSize) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		logger.debug("初始化用户扩展表数据，页数:"+currentPage);
		Pager pager=new Pager();
		pager.setPageSize(pageSize);
		pager.setCurrentPage(currentPage);
		Map<String,Object> searchMap=new HashMap<String,Object>();
		searchMap.put("endTime",DateUtil.format(DateUtil.addDays(new Date(), -1), "yyyy-MM-dd 23:59:59"));
		pager=contactReadOnlyDAO.getUserInfPager(searchMap,pager);
		
		if(null!=pager && pager.getTotalPages()>=currentPage){
			List<TbQyUserInfoPO> list=(List<TbQyUserInfoPO>)pager.getPageData();
			updateUserExtList(list,currentPage,isUpdate);
		}else{
			//没有数据，不进行
			logger.debug("初始化用户扩展表数据结束，页数:"+currentPage);
			return;
		}
		
		//递归
		if(pager.getTotalPages()>currentPage){
			currentPage=currentPage+1;
			initUserInfoExt(currentPage,isUpdate,pageSize);
		}else{
			currentPage=currentPage+1;
			logger.debug("递归结束，查找今天的数据初始化:"+currentPage+"信息：");
			Map<String,Object> searchMap1=new HashMap<String,Object>();
			searchMap1.put("startTime", DateUtil.format(new Date(), "yyyy-MM-dd 00:00:00"));
			searchMap1.put("endTime", DateUtil.format(new Date(), "yyyy-MM-dd 23:59:59"));
			List<TbQyUserInfoPO> todayList=contactReadOnlyDAO.getUserInfoList(searchMap1);
			if(null!=todayList && todayList.size()>0){
				updateUserExtList(todayList,currentPage,isUpdate);
			}
			logger.debug("所有初始化执行完成，页数:"+currentPage+"信息：");
		}
	}
	
	/**
	 * 根据list更新数据
	 * @param list
	 * @throws Exception
	 * @throws BaseException
	 */
	public void updateUserExtList(List<TbQyUserInfoPO> list,long currentPage,String isUpdate)throws Exception,BaseException{
		List<TbQyUserInfoExtPO> insertList=new ArrayList<TbQyUserInfoExtPO>();
		List<TbQyUserInfoExtPO> updateList=new ArrayList<TbQyUserInfoExtPO>();
		for(TbQyUserInfoPO po:list){
			try{
				logger.debug("第:"+currentPage+"页：userId:"+po.getUserId()+";orgId:"+po.getOrgId());
				if(null!=po){
					TbQyUserInfoExtPO extPO=contactDAO.getUserInfoExtPOByUserId(po.getUserId());
					if(null==extPO){
						extPO=new TbQyUserInfoExtPO();
						extPO.setUserId(po.getUserId());
					}
					if("1".equals(isUpdate) && !AssertUtil.isEmpty(extPO.getIsUpdate()) && 1==extPO.getIsUpdate()){
						//不执行一更新过的，或标记为已更新过的不执行
						continue;
					}else{
						//设置生日
						if(!AssertUtil.isEmpty(po.getBirthday())){
							extPO.setBirthMonthDay(DateUtil.format(po.getBirthday(), "MM-dd"));
						}else{
							extPO.setBirthMonthDay(null);
						}
						//农历生日
						if(!AssertUtil.isEmpty(po.getLunarCalendar())){
							extPO.setBirthLunarMonthDay(po.getLunarCalendar());
						}else{
							extPO.setBirthLunarMonthDay(null);
						}
						//入职
						if(!AssertUtil.isEmpty(po.getEntryTime())){
							extPO.setEntryMonthDay(DateUtil.format(po.getEntryTime(), "MM-dd")); 
						}else{
							extPO.setEntryMonthDay(null);
						}
						//表明已更新过
						extPO.setIsUpdate(1);
						
						if(!AssertUtil.isEmpty(extPO.getId())){
							//更新数据
							updateList.add(extPO);
						}else{
							//插入数据
							extPO.setId(UUID.randomUUID().toString());
							insertList.add(extPO);
						}
					}
				}
			}catch(BaseException e){
				logger.debug("初始化用户扩展表数据异常，页数:"+currentPage+" 信息：orgId:"+po.getOrgId()+" userId:"+po.getUserId()+" 错误:"+e);
				continue;
			}catch(Exception e){
				logger.debug("初始化用户扩展表数据异常，页数:"+currentPage+" 信息：orgId:"+po.getOrgId()+" userId:"+po.getUserId()+" 错误:"+e);
				continue;
			}
		}
		logger.debug("批量执行开始:currentPage:"+currentPage);
		if(null!=insertList && insertList.size()>0){
			contactDAO.execBatchInsert(insertList);
		}
		if(null!=updateList && updateList.size()>0){
			contactDAO.execBatchUpdate(updateList);
		}
	}

	@Override
	public TbDqdpUserPO getSuperManagerByOrgId(String orgId) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getSuperManagerByOrgId(orgId);
	}

	@Override
	public List<UserOrgVO> getOrgByPerson(Map<String, Object> params)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getOrgByPerson(params);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IContactService#getUserInfoExtByUserId(java.lang.String)
	 */
	@Override
	public TbQyUserInfoExtPO getUserInfoExtByUserId(String userId) throws Exception, BaseException {
		return contactDAO.getUserInfoExtPOByUserId(userId);
	}

	@Override
	public Pager getUserInfPager(Map<String, Object> searchMap, Pager pager)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactReadOnlyDAO.getUserInfPager(searchMap, pager);
	}

	@Override
	public List<TbQyUserInfoPO> getUserInfoList(Map<String, Object> searchMap)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactReadOnlyDAO.getUserInfoList(searchMap);
	}

	@Override
	public String getUserIdByWxUserId(String wxUserId, String orgId)
			throws Exception, BaseException {
		return contactDAO.getUserIdByWxUserId(wxUserId, orgId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getUserInfoVOInUserIds(java.lang.String, java.util.Map)
	 */
	@Override
	public List<UserInfoVO> getUserInfoVOInUserIds(String userIds,
			Map<String, Object> params) throws Exception, BaseException {
		return contactDAO.getUserInfoVOInUserIds(userIds, params);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getUserInfoByOrgIdAndUserIds(java.lang.String, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> getUserInfoByOrgIdAndUserIds(String orgId,
			String userIds) throws Exception, BaseException {
		return contactDAO.getUserInfoByOrgIdAndUserIds(orgId, userIds);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getLeaveUserInfoByUserId(java.lang.String)
	 */
	@Override
	public TbQyUserInfoVO getLeaveUserInfoByUserId(String userId)
			throws Exception, BaseException {
		return contactDAO.getUserPartInfoByUserId(userId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getToUserByDepatIds(java.lang.String[], java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> getToUserByDepatIds(String[] depatIds,
			String orgId) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactDAO.getToUserByDepatIds(depatIds,orgId);
	}

	/**
	 * 根据orgId获取用户信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @version 1.0
	 */
	public List<TbQyUserInfoVO> findUsersAndDeptByOrgId(String orgId)throws Exception, BaseException{
		return contactReadOnlyDAO.findUsersAndDeptByOrgId(orgId);
	}

	/**
	 * 通过手机号获取用户信息（使用于用户登录后获取登录用户信息，用于缓存）
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 * @version 1.0
	 */
	public TbQyUserInfoVO getUserInfoByMobile(String mobile,String corpId) throws Exception, BaseException{
		return contactReadOnlyDAO.getUserInfoByMobile(mobile, corpId);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getCountOrgByCorpId(java.lang.String)
	 */
	@Override
	public int getCountOrgByCorpId(String corpId) throws Exception {
		return contactReadOnlyDAO.getCountOrgByCorpId(corpId);
	}

	@Override
	public Integer countQyUserReceiveByUserId(String userId, String orgId) throws Exception, BaseException {
		// TODO Auto-generated method stub
		return this.contactReadOnlyDAO.countQyUserReceiveByUserId(userId, orgId);
	}

	@Override
	public void updateQyUserReceiveByUserId(Map<String, Object> params) throws Exception, BaseException {
		// TODO Auto-generated method stub
		this.contactDAO.updateQyUserReceiveByUserId(params);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getDelOrgCount(java.lang.String)
	 */
	@Override
	public int getDelOrgCount(String user) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return this.contactReadOnlyDAO.getDelOrgCount(user);
	}

	@Override
	public int getPepleCount(String orgId, String agentCode, int days)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactReadOnlyDAO.getPepleCount(orgId, agentCode, days);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getPicListByGroupIds(java.lang.String[])
	 */
	@Override
	public List<TbQyPicVO> getPicListByGroupIds(String[] groupIds)
			throws Exception, BaseException {
		if (null == groupIds || groupIds.length == 0) {
			return new ArrayList<TbQyPicVO>();
		}
		int size=groupIds.length;
		List<TbQyPicVO> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getPicListByGroupIds(groupIds);
		} else {
			list = new ArrayList<TbQyPicVO>(size);
			String[] newGroupIds = new String[200];
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				System.arraycopy(groupIds, startIndex, newGroupIds, 0, Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getPicListByGroupIds(newGroupIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newGroupIds = new String[size - startIndex];
				System.arraycopy(groupIds, startIndex, newGroupIds, 0, size - startIndex);
				list.addAll(contactReadOnlyDAO.getPicListByGroupIds(newGroupIds));
			}
		}
		return list;
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoByIds(String[] ids) throws Exception {
		if(ids == null || ids.length==0){
			return null;
		}
		return contactDAO.getUserInfoByIds(ids);
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoByUserIds(String[] userIds) throws Exception {
		if(userIds == null || userIds.length==0){
			return null;
		}
		return contactDAO.getUserInfoByUserIds(userIds);
	}

	@Override
	public List<TbDepartmentInfoVO> getDeptInfoByLeaveUserId(String userId) throws Exception {
		return contactDAO.getDeptInfoByLeaveUserId(userId);
	}

	@Override
	public void deleteUserLeaveDeptRef(String orgId, List<String> userIds) throws Exception {
		if(userIds == null || userIds.size()==0){
			return;
		}
		contactDAO.deleteUserLeaveDeptRef(orgId,userIds);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.addressbook.contact.service.IContactService#getToUserByDepartments(java.util.List, java.lang.String)
	 */
	@Override
	public List<TbQyUserInfoVO> getToUserByUserId(
			String userId, String orgId) throws Exception,
			BaseException {
		List<TbDepartmentInfoVO> deptsList =this.getDeptInfoByUserId(orgId, userId);
		List<TbQyUserInfoVO> list=new ArrayList<TbQyUserInfoVO>();
		if (deptsList != null && deptsList.size() > 0) {
			for (TbDepartmentInfoVO infoVO : deptsList) {
				//当前节点负责人
				List<TbQyUserInfoVO> toUserList=departmentMgrService.getDeptReceiveList(infoVO.getId(),orgId);
				if (null!=toUserList&&toUserList.size()>0){
					list.addAll(toUserList);
				}else if (!StringUtil.isNullEmpty(infoVO.getParentDepart())) {
					//上一节点负责人
					toUserList=this.findToUserByDepat(infoVO,orgId);
					if (null!=toUserList&&toUserList.size()>0) {
						list.addAll(toUserList);
					}
				}
			}
			/*if (list.size()>0) {
				//负责人去重
				Set<String> repeatSet = new HashSet<String>();
				List<TbQyUserInfoVO> ilist=new ArrayList<TbQyUserInfoVO>();
				for (TbQyUserInfoVO tbQyUserInfoVO : list) {
					if (repeatSet.add(tbQyUserInfoVO.getUserId())) {
						ilist.add(tbQyUserInfoVO);
					}
				}
				list=ilist;
			}*/
			list = toRepeatDepartUsers(list);
		}
		return list;
	}

	/**
	 * 获取父部门负责人
	 * @param infoVO
	 * @param orgId
	 * @return
	 * @author Hejinjiao
	 * @2016-5-19
	 * @version 1.0
	 * @throws BaseException
	 * @throws Exception
	 */
	private List<TbQyUserInfoVO> findToUserByDepat(TbDepartmentInfoVO infoVO,
			String orgId) throws Exception, BaseException {
		String departId=infoVO.getParentDepart();
		List<TbQyUserInfoVO> toUserList=null;
		while (!StringUtil.isNullEmpty(departId)) {
			toUserList=departmentMgrService.getDeptReceiveList(departId,orgId);
			if (null==toUserList||toUserList.size()==0) {
				TbDepartmentInfoPO infoPO=departmentMgrService.searchByPk(TbDepartmentInfoPO.class, departId);
				if (null!=infoPO&&!StringUtil.isNullEmpty(infoPO.getParentDepart())) {
					departId=infoPO.getParentDepart();
				}else {
					departId=null;
				}
			}else {
				departId=null;
			}
		}
		return toUserList;
	}

	@Override
	public List<UserDeptInfoVO> findBirthdayUserByDateAndRemindTypeIR(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactInsideReadOnlyDAO.findBirthdayUserByDateAndRemindType(params);
	}

	@Override
	public List<UserDeptZhouNianInfoVO> findEntryPeopleIR(
			Map<String, Object> params) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return contactInsideReadOnlyDAO.findEntryPeople(params);
	}

	@Override
	public int countOrgLeavePerson(String orgId) throws Exception {
		return contactReadOnlyDAO.countOrgLeavePerson(orgId);
	}

	@Override
	public int countOrgPerson(String orgId) throws Exception {
		return contactReadOnlyDAO.countOrgPerson(orgId);
	}

	@Override
	public Map<String, List<TbQyPicVO>> getPicMapByGroupIds(String[] groupIds) throws Exception, BaseException {
		List<TbQyPicVO>list=this.getPicListByGroupIds(groupIds);
		if (list==null||list.size()==0) {
			return new HashMap<String, List<TbQyPicVO>>(1);
		}
		Map<String,List<TbQyPicVO>>picMap=new HashMap<String, List<TbQyPicVO>>(groupIds.length);
		List<TbQyPicVO> newList;
		for (TbQyPicVO picVO:list){
			if(picMap.containsKey(picVO.getGroupId())){
				picMap.get(picVO.getGroupId()).add(picVO);
			}else{
				newList=new ArrayList<TbQyPicVO>();
				newList.add(picVO);
				picMap.put(picVO.getGroupId(), newList);
			}
		}
		return picMap;
	}

	/**
	 * 查询用户是否保密
	 * @return
	 * @author LiYiXin
	 * @ 16-8-17
	 */
	@Override
	public boolean getSecrecyByUserId(String userId, String orgId) throws  Exception, BaseException{
		//获取缓存
		Set<String> userIds  = (HashSet<String>) CacheWxqyhObject.get("addressBook", orgId, "userIds");
		//如果有缓存
		if(null != userIds){
			if(userIds.contains(userId)){
				return true;
			}
		}
		else{
			//创建缓存
			userIds = addSecrecyCache(orgId);
			if(userIds.contains(userId)){
				return true;
			}
		}
		//如果该用户不需要保密
		return false;
	}

	/**
	 * 新增用户保密
	 * @param secrecyPO 用户信息
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @ 16-8-17
	 */
	@Override
	public void addSecrecy(TbQyUserSecrecyPO secrecyPO) throws Exception, BaseException{
		contactDAO.insert(secrecyPO);
		addSecrecyCache(secrecyPO.getOrgId());
	}

	/**
	 * 删除用户保密
	 * @param secrecyPO 用户信息
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @ 16-8-17
	 */
	@Override
	public void deleSecrecy(TbQyUserSecrecyPO secrecyPO) throws Exception, BaseException{
		contactDAO.delete(secrecyPO);
		addSecrecyCache(secrecyPO.getOrgId());
	}

	/**
	 * 通过orgid查询公司保密人员
	 * @param orgId
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-17
	 */
	public Set<String> getSecrecyByOrgId(String orgId) throws  Exception, BaseException{
		//获取缓存
		Set<String> userIds  = (HashSet<String>) CacheWxqyhObject.get("addressBook", orgId, "userIds");
		if(userIds != null){
			return userIds;
		}
		//如果没有缓存
		else{
			return addSecrecyCache(orgId);
		}
	}

	/**
	 *创建用户保密缓存
	 * @param orgId 机构Id
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-19
	 */
	public Set<String> addSecrecyCache(String orgId) throws Exception, BaseException{
		List<TbQyUserSecrecyPO> tbQyUserSecrecyPOs = contactDAO.getSecrecyByOrgId(orgId);
		Set<String> userIds = new HashSet<String>();
		//把数据存入set
		for(TbQyUserSecrecyPO tbQyUserSecrecyPO : tbQyUserSecrecyPOs){
			userIds.add(tbQyUserSecrecyPO.getUserId());
		}
		CacheWxqyhObject.set("addressBook", orgId, "userIds", userIds);
		return userIds;

	}

	/**
	 *优化用户按名字、拼音、电话搜索条件
	 * @param map sql的条件
	 * @return
	 * @author LiYiXin
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * 2016-8-25
	 */
	public Map<String, Object>  optimizeByNameOrPhone(Map<String, Object> map) throws Exception, BaseException{
		String keyWord = (String) map.get("keyWord");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(keyWord);
		//如果是纯数字
		if(isNum.matches()){
			map.put("mobile", "%" +keyWord + "%");
			map.put("keyWord", "%" +keyWord + "%");
			return map;
		}
		else{
			pattern = Pattern.compile("[a-zA-Z]+");
			isNum = pattern.matcher(keyWord);
			//如果是纯英文
			if(isNum.matches()){
				map.put("pinyin", keyWord);
				map.put("keyWord", "%" +keyWord + "%");
				String pinyin = (String) map.get("pinyin");
				Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
				Matcher ma = patter.matcher(pinyin);
				StringBuilder sb = new StringBuilder("");
				// 在字母中间添加%
				if (ma.find()) {
					for (char iterable_element : pinyin.toCharArray()) {
						sb.append(iterable_element).append("%");
					}
				}
				if (sb.length() > 0)
					sb = sb.replace(sb.length() - 1, sb.length(), "");

				if (sb.length() > 0)
					pinyin = sb.toString();
				if (!AssertUtil.isEmpty(pinyin)) {
					map.put("pinyin", "%" + pinyin.toLowerCase() + "%");
				}
				return map;
			}
			//其它情况
			else{
				map.put("keyWord", "%" +keyWord + "%");
				return map;
			}
		}
	}

	@Override
	public List<TbQyContactSyncPO> getContactSyncList(String corpId, int status) throws Exception {
		return contactDAO.getContactSyncList(corpId, status);
	}

	@Override
	public List<String> getContactSyncCorpIdList(int status) throws Exception {
		return contactDAO.getContactSyncCorpIdList(status);
	}

	@Override
	public List<String> findDeptUserIdAllByDeptIds(List<String> deptIds) throws SQLException {
		if(deptIds==null || deptIds.size()==0){
			return new ArrayList<String>(0);
		}
		return contactReadOnlyDAO.findDeptUserIdAllByDeptIds(deptIds);
	}

	/**
	 * 根据机构id获分页获取用户信息（用于接口）
	 * @param searchMap sql的条件
	 * @param pager 	分页page
	 * @return
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @author liyixin
	 * @2016-9-22
	 * @version 1.0
	 */
	public Pager findUsersByOrgIdForInterface(Map searchMap, Pager pager) throws Exception, BaseException{
		return contactReadOnlyDAO.findUsersByOrgIdForInterface(searchMap, pager);
	}

	/**
	 * 查询机构的最新版本号
	 * @param orgId 机构orgId
	 * @return
	 * @throws Exception 异常抛出
	 * @throws BaseException 异常抛出
	 * @author liyixin
	 * @2016-10-11
	 * @version 1.0
	 */
	public Integer findVersionOrgByorgId(String orgId) throws Exception, BaseException{
		return contactReadOnlyDAO.findVersionOrgByorgId(orgId);
	}

	/**
	 *判断该用户在不在版本控制表内
	 * @param id 用户id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-11
	 * @version 1.0
	 */
	public boolean findVersionIdById(String id)throws BaseException, Exception{
		TbQyOrgVersionPO versionPO =  contactDAO.searchByPk(TbQyOrgVersionPO.class, id);
		if(null != versionPO){
			return true;
		}
		else return false;
	}

	/**
	 * 批量查询机构版本的id
	 * @param orgId 机构id
	 * @param start 起点
	 * @param end 终点
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-11
	 */
	public List<String> getVersionId(String orgId, int start, int end) throws BaseException, Exception{
		return contactDAO.getVersionId(orgId, start, end);
	}

	/**
	 * 批量新增机构版本的id
	 * @param idType id类型
	 * @param operationType 操作类型
	 * @param orgVesionRecent 机构最新版本
	 * @param orgId 机构Id
	 * @param ids 用户id列表
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-12
	 * @version 1.0
	 */
	public void batchAddVersion(String idType, String operationType, Integer orgVesionRecent, String orgId, List<String> ids) throws BaseException, Exception{
		List<TbQyOrgVersionPO> versionPos = new ArrayList<TbQyOrgVersionPO>();
		for(int i = 0; i<ids.size(); i++){
			TbQyOrgVersionPO tbQyOrgVersionPo = new TbQyOrgVersionPO();
			tbQyOrgVersionPo.setId(ids.get(i));
			tbQyOrgVersionPo.setOrgId(orgId);
			tbQyOrgVersionPo.setOrgVersion(orgVesionRecent);
			tbQyOrgVersionPo.setOperationType(operationType);
			tbQyOrgVersionPo.setIdType(idType);
			tbQyOrgVersionPo.setCreateTime(new Date());
			tbQyOrgVersionPo.setUpdateTime(new Date());
			versionPos.add(tbQyOrgVersionPo);
		}
		contactDAO.execBatchInsert(versionPos);
		CacheWxqyhObject.set("addressBook", orgId, "orgVesionRecent", orgVesionRecent);
	}

	/**
	 * 批量更新机构版本的id
	 * @param operationType 操作类型
	 * @param orgVesionRecent 机构最新版本
	 * @param orgId 机构Id
	 * @param ids 用户id列表
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author liyixin
	 * @2016-10-12
	 * @version 1.0
	 */
	public void batchUpdateVersion(String operationType, Integer orgVesionRecent, String orgId, List<String> ids) throws BaseException, Exception{
		List<TbQyOrgVersionPO> versionPos = new ArrayList<TbQyOrgVersionPO>();
		for(int i = 0; i<ids.size(); i++){
			TbQyOrgVersionPO tbQyOrgVersionPo = new TbQyOrgVersionPO();
			tbQyOrgVersionPo.setId(ids.get(i));
			tbQyOrgVersionPo.setOrgVersion(orgVesionRecent);
			tbQyOrgVersionPo.setOperationType(operationType);
			tbQyOrgVersionPo.setUpdateTime(new Date());
			versionPos.add(tbQyOrgVersionPo);
		}
		contactDAO.execBatchUpdate(versionPos, false);
		CacheWxqyhObject.set("addressBook", orgId, "orgVesionRecent", orgVesionRecent);
	}

	/**
	 * 统计机构版本的企业的id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2016-10-12
	 */
	public int getVersionOrgCount(String orgId) throws BaseException, Exception{
		return contactDAO.getVersionOrgCount(orgId);
	}

	/**
	 *通过机构Id和版本id分页查询
	 * @param searchMap map
	 * @param pager 分页
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-10-14
	 * @version 1.0
	 */
	public Pager findVersionOrg(Map searchMap, Pager pager) throws BaseException, Exception{
		return contactReadOnlyDAO.findVersionOrg(searchMap,pager);
	}

	/**
	 * 通过用户id返回用户列表
	 * @param userIds id列表
	 * @param orgId 机构Id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2016-10-14
	 * @version 1.0
	 */
	public List<InterfaceUser> getUserByIds(List<String> userIds, String orgId) throws BaseException, Exception{
		return contactReadOnlyDAO.getUserByIds(userIds,orgId);
	}

	@Override
	public List<String> getUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws SQLException {
		if (wxUserIdList == null || wxUserIdList.size()==0) {
			return null;
		}
		return contactReadOnlyDAO.getUserIdsByWxUserIds(corpId, wxUserIdList);
	}

	@Override
	public List<String> getWxUserIdsByWxUserIds(String corpId, List<String> wxUserIdList) throws Exception {
		if (AssertUtil.isEmpty(wxUserIdList)) {
			return null;
		}
		int size = wxUserIdList.size();
		List<String> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getWxUserIdsByWxUserIds(corpId, wxUserIdList);
		} else {
			list = new ArrayList<String>(size);
			List<String> newUserIds;
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				newUserIds = wxUserIdList.subList(startIndex, startIndex + Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getWxUserIdsByWxUserIds(corpId, newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = wxUserIdList.subList(startIndex, size);
				list.addAll(contactReadOnlyDAO.getWxUserIdsByWxUserIds(corpId, newUserIds));
			}
		}
		return list;
	}

	@Override
	public TbQyUserWxgzhPO findWxgzhUserById(String openid ,String orgId) throws Exception,BaseException{
		return this.contactReadOnlyDAO.getWxgzhUserById(openid ,orgId);
	}

	@Override
	public TbQyUserWxgzhVO getCacheWxgzhUserVO(String userId, String orgId)throws Exception,BaseException{
		TbQyUserWxgzhVO vo=(TbQyUserWxgzhVO) CacheWxqyhObject.get("activity",orgId,userId);
		if(null==vo){
			vo=this.contactReadOnlyDAO.getWxgzhUserVO(userId, orgId);
			if(null!=vo){
				CacheWxqyhObject.set("activity",orgId,vo.getId(),vo,CacheWxqyhObject.THIRTY_MINUTE);
			}
		}
		return vo;
	}

	@Override
	public List<TbQyUserInfoVO> getToUserByUserIdExclude(String userId, String orgId, Map<String, String> excludeUserIdMap) throws Exception, BaseException {
		List<TbDepartmentInfoVO> deptsList = this.getDeptInfoByUserId(orgId, userId);
		List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(); //可以返回的部门负责人
		if (null != deptsList && deptsList.size() > 0) {
			List<TbQyUserInfoVO> toUserList = null;
			List<TbQyUserInfoVO> toOwnUserList = new ArrayList<TbQyUserInfoVO>(); //本部门的负责人
			boolean isOk = false;//不能返回部门负责人，需要继续查
			for (TbDepartmentInfoVO infoVO : deptsList) {
				//当前节点负责人
				toUserList = departmentMgrService.getDeptReceiveList(infoVO.getId(), orgId);
				isOk = false;//不能返回部门负责人，需要继续查
				if (null != toUserList && toUserList.size() > 0){
					if(1 == toUserList.size() && StringUtil.isNullEmpty(infoVO.getParentDepart())){ //只有一个人并且没有父部门时
						list.addAll(toUserList);
					}else{
						toOwnUserList.addAll(toUserList);
						for(TbQyUserInfoVO uvo : toUserList){
							if(!excludeUserIdMap.containsKey(uvo.getUserId())){ //不是规定的人
								isOk = true;//能返回部门负责人，不需要继续查
								list.add(uvo);
							}
						}
					}
				}
				if(!isOk){ //继续查
					if (!StringUtil.isNullEmpty(infoVO.getParentDepart())) {
						//上一节点负责人
						toUserList = findToUserAndDepartIdByDepat(infoVO, orgId, excludeUserIdMap);
						if (null != toUserList && toUserList.size() > 0){
							list.addAll(toUserList);
							/*if(1 == toUserList.size()){
								list.addAll(toUserList);
							}else{
								for(TbQyUserInfoVO uvo : toUserList){
									if(!excludeUserIdMap.containsKey(uvo.getUserId())){ //不是规定的人
										list.add(uvo);
									}
								}
							}*/
						}else{ //父部门没有负责人时，拿本部门的负责人
							list.addAll(toOwnUserList);
						}
					}
				}
			}
			list = toRepeatDepartUsers(list);
		}
		return list;
	}

	@Override
	public List<TbQyUserInfoVO> getLastDepartToUserByUserId(String userId, String orgId) throws Exception, BaseException {
		List<TbDepartmentInfoVO> deptsList = this.getDeptInfoByUserId(orgId, userId);
		List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(); //可以返回的部门负责人
		if (null != deptsList && deptsList.size() > 0) {
			List<TbQyUserInfoVO> toUserList = null;
			for (TbDepartmentInfoVO infoVO : deptsList) {
				//付部门负责人
				if (!StringUtil.isNullEmpty(infoVO.getParentDepart())) {
					//上一节点负责人
					toUserList = findLastToUserByDepat(infoVO, orgId);
				}else{
					//当前节点负责人
					toUserList = departmentMgrService.getDeptReceiveList(infoVO.getId(), orgId);
				}
				if (null != toUserList && toUserList.size() > 0) {
					list.addAll(toUserList);
				}
			}
			list = toRepeatDepartUsers(list);
		}
		return list;
	}

	/**
	 * 获取父部门负责人(并返回当前部门的id和当前部门下一级部门的id)
	 * 当查询到的部门负责人里面有excludeUserIdMap里面的人时，在往上一级再找，直到没有上一级或没有excludeUserIdMap里面的人
	 * @param infoVO 部门信息
	 * @param orgId 组织id
	 * @param excludeUserIdMap 不能有的人
	 * @author Maquanyang
	 * @2016-12-02
	 * @version 1.0
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	private List<TbQyUserInfoVO> findToUserAndDepartIdByDepat(TbDepartmentInfoVO infoVO, String orgId, Map<String, String> excludeUserIdMap) throws Exception, BaseException {
		String departId = infoVO.getParentDepart();
		List<TbQyUserInfoVO> toUserList = null; //最新的部门负责人
		List<TbQyUserInfoVO> beforeToUserList = null; //前面一个部门有负责人的负责人
		boolean isOk = false;//不能返回部门负责人，需要继续查
		TbDepartmentInfoPO infoPO = null;
		List<TbQyUserInfoVO> list = new ArrayList<TbQyUserInfoVO>(); //可以返回的部门负责人
		while (!StringUtil.isNullEmpty(departId)) {
			isOk = false;//不能返回部门负责人，需要继续查
			toUserList = departmentMgrService.getDeptReceiveList(departId, orgId);
			if (null == toUserList || toUserList.size() == 0) {
				infoPO = departmentMgrService.searchByPk(TbDepartmentInfoPO.class, departId);
				if (null != infoPO && !StringUtil.isNullEmpty(infoPO.getParentDepart())) {
					departId = infoPO.getParentDepart();
				}else {
					departId = null;
				}
			}else {
				beforeToUserList = toUserList;
				for(TbQyUserInfoVO uvo : toUserList){
					if(!excludeUserIdMap.containsKey(uvo.getUserId())){ //不是规定的人
						isOk = true;//能返回部门负责人，不需要继续查
						list.add(uvo);
					}
				}
				if(isOk){ //可以返回当前查到的部门负责人
					departId = null;
				}else{
					infoPO = departmentMgrService.searchByPk(TbDepartmentInfoPO.class, departId);
					if (null != infoPO && !StringUtil.isNullEmpty(infoPO.getParentDepart())) {
						departId = infoPO.getParentDepart();
					}else {
						departId = null;
					}
				}
			}
		}
		if(null == list || list.size() == 0){//最上一级部门没有部门负责人时，拿最上一级有部门负责人的
			list = beforeToUserList;
		}
		return list;
	}

	/**
	 * 部门负责人去重
	 * @param list 部门负责人
	 * @return 去重后的部门负责人
     */
	private List<TbQyUserInfoVO> toRepeatDepartUsers(List<TbQyUserInfoVO> list){
		if (list.size()>0) {
			//负责人去重
			Set<String> repeatSet = new HashSet<String>();
			List<TbQyUserInfoVO> ilist=new ArrayList<TbQyUserInfoVO>();
			for (TbQyUserInfoVO tbQyUserInfoVO : list) {
				if (repeatSet.add(tbQyUserInfoVO.getUserId())) {
					ilist.add(tbQyUserInfoVO);
				}
			}
			list=ilist;
		}
		return list;
	}

	/**
	 * 获取第一级父部门负责人
	 * @param infoVO 部门信息
	 * @param orgId 组织id
	 * @author Maquanyang
	 * @2016-12-07
	 * @version 1.0
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 */
	private List<TbQyUserInfoVO> findLastToUserByDepat(TbDepartmentInfoVO infoVO, String orgId) throws Exception, BaseException {
		String departId = infoVO.getParentDepart();
		List<TbQyUserInfoVO> toUserList = null; //最新的部门负责人
		TbDepartmentInfoPO infoPO = null;
		while (!StringUtil.isNullEmpty(departId)) {
			infoPO = departmentMgrService.searchByPk(TbDepartmentInfoPO.class, departId);
			if (null != infoPO && !StringUtil.isNullEmpty(infoPO.getParentDepart())) {
				departId = infoPO.getParentDepart();
			}else {
				toUserList = departmentMgrService.getDeptReceiveList(departId, orgId);
				departId = null;
			}
		}
		return toUserList;
	}

	/**
	 * 查询高级搜索条件
	 * @param orgId 机构id
	 * @param userId 当前用户
	 * @return
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2017-2-07
	 */
	public 	List<TbQyUserSearchSeniorVO> getSeniorByOrgId(String orgId, String userId) throws BaseException, Exception{
		return contactDAO.getSeniorByOrgId(orgId, userId);
	}

	/**
	 * 批量更新高级搜索条件
	 * @param org
	 * @param seniorPOList
	 * @throws BaseException 异常抛出
	 * @throws Exception 异常抛出
	 * @author LiYiXin
	 * 2017-2-07
	 */
	public void batchUpdateSenior(UserOrgVO org, List<TbQyUserSearchSeniorPO> seniorPOList) throws BaseException, Exception{
		List<TbQyUserSearchSeniorPO> oldList =  contactDAO.getSeniorPOByOrgId(org.getOrgId(), org.getUserId());
		List<TbQyUserSearchSeniorPO> addList = new ArrayList<TbQyUserSearchSeniorPO>();
		List<TbQyUserSearchSeniorPO> updateList = new ArrayList<TbQyUserSearchSeniorPO>();
		if(oldList.size() == 0){//如果原来没有数据
			for(TbQyUserSearchSeniorPO searchSeniorPO : seniorPOList){
				searchSeniorPO.setOrgId(org.getOrgId());
				searchSeniorPO.setId(UUID32.getID());
				searchSeniorPO.setUserId(org.getUserId());
				addList.add(searchSeniorPO);
			}
		}else{//如果原来有数据
			boolean hasOld = false;//原来是有数据的
			for(TbQyUserSearchSeniorPO newSeniorPO : seniorPOList){
				for(TbQyUserSearchSeniorPO oldSeniorPO : oldList){
					if(newSeniorPO.getSelectName().equals(oldSeniorPO.getSelectName()) && "0".equals(newSeniorPO.getIsCustom())){//如果原来有数据，而且不是自定义字段
						newSeniorPO.setId(oldSeniorPO.getId());
						updateList.add(newSeniorPO);
						hasOld = true;
						break;
					}
					//如果原来的自定义字段条件selectid 等于 现在新的自定义字段条件的selectid
					if(!AssertUtil.isEmpty(oldSeniorPO.getSelectId()) && !AssertUtil.isEmpty(newSeniorPO.getSelectId()) && newSeniorPO.getSelectId().equals(oldSeniorPO.getSelectId()) && "1".equals(newSeniorPO.getIsCustom())){
						newSeniorPO.setId(oldSeniorPO.getId());
						updateList.add(newSeniorPO);
						hasOld = true;
						break;
					}
				}
				if(!hasOld){//如果原来没有数据
					newSeniorPO.setOrgId(org.getOrgId());
					newSeniorPO.setId(UUID32.getID());
					newSeniorPO.setUserId(org.getUserId());
					addList.add(newSeniorPO);
				}
				hasOld = false;//初始化数据
			}
		}
		if(addList.size() > 0){
			contactDAO.execBatchInsert(addList);
		}
		if(updateList.size() > 0){
			contactDAO.execBatchUpdate(updateList, false);
		}
	}

	/**
	 * 通过userId列表查询用户
	 * @param userIds
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-02-22
	 * @version 1.0
	 */
	public 	List<SelectUserVO> findUserByUserIds(List<String> userIds) throws BaseException, Exception{
		return contactReadOnlyDAO.findUserByUserIds(userIds);
	}

	@Override
	public List<TbQyUserInfoPO> findUserDepartIdsByUserIds(List<String> userIds) throws BaseException, Exception {
		return contactReadOnlyDAO.findUserDepartIdsByUserIds(userIds);
	}

	@Override
	public Integer countReceiveUserByDepartmentId(String departmentId) throws Exception, BaseException {
		return this.contactReadOnlyDAO.countReceiveUserByDepartmentId(departmentId);
	}

	@Override
	public List<String> getWxUserIdsByUserIds(List<String> userIds) throws SQLException {
		if (AssertUtil.isEmpty(userIds)) {
			return null;
		}
		int size = userIds.size();
		List<String> list;
		if (size <= Configuration.SQL_IN_MAX) {
			list = contactReadOnlyDAO.getWxUserIdsByUserIds(userIds);
		} else {
			list = new ArrayList<String>(size);
			List<String> newUserIds;
			int startIndex = 0;
			while (startIndex + Configuration.SQL_IN_MAX <= size) {
				newUserIds = userIds.subList(startIndex, startIndex + Configuration.SQL_IN_MAX);
				list.addAll(contactReadOnlyDAO.getWxUserIdsByUserIds(newUserIds));
				startIndex += Configuration.SQL_IN_MAX;
			}
			if (startIndex < size) {
				newUserIds = userIds.subList(startIndex, size);
				list.addAll(contactReadOnlyDAO.getWxUserIdsByUserIds(newUserIds));
			}
		}
		return list;
	}

	@Override
	public List<OrgInfoExtVO> getOrgInfoExtVOList(String orgIdArray[]) throws Exception, BaseException {
		return contactDAO.getOrgInfoExtVOList(orgIdArray);
	}

	/**
	 * 根据用户id查询所在的部门的顶级部门
	 * （用于手机端，为了兼容部门新加的权限:仅特定对象）
	 * @param orgId
	 * @param userId 用户id
	 * @param userIds 返回的特定对象用户列表 只需要new 一个传过来就可以了
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-3-7
	 * @version 1.0
	 */
	public List<TbDepartmentInfoVO> getFirstDeptByUserId(String orgId, String userId, List<String> userIds)throws Exception, BaseException{
		List<TbDepartmentInfoVO> userDepts = departmentService.getDeptInfoByUserId(orgId,userId);
		List<TbDepartmentInfoVO> depts = new ArrayList<TbDepartmentInfoVO>();
		if (userDepts != null && userDepts.size() > 0) {
			String[] split;
			TbDepartmentInfoVO vo;
			Set<String> deptFullName = new HashSet<String>();
			for (TbDepartmentInfoVO userDept : userDepts) {
				// 如果父部门为空
				if (StringUtil.isNullEmpty(userDept.getParentDepart())) {
					// 用此方法去除重复的部门
					if (deptFullName.add(userDept.getId())) {
						depts.add(userDept);
						//如果是仅特定对象
						if(!AssertUtil.isEmpty(userDept.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(userDept.getPermission())){
							List<TbDepartmentInfoVO> specificDepts = new ArrayList<TbDepartmentInfoVO>(1);
							specificDepts.add(userDept);
							UserInfoVO user = new UserInfoVO();
							user.setUserId(userId);
							depts = DepartmentUtil.setSpecificDeptPermission(specificDepts, userIds, user);
						}
					}
					continue;
				}
				if (!AssertUtil.isEmpty(userDept.getPermission()) && DepartmentDictUtil.PERMISSION_CHILD.equals(userDept.getPermission())) {//仅子部门
					depts.add(userDept);
					continue;
				}
				//如果是仅特定对象
				if(!AssertUtil.isEmpty(userDept.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(userDept.getPermission())){
					List<TbDepartmentInfoVO> specificDepts = new ArrayList<TbDepartmentInfoVO>(1);
					specificDepts.add(userDept);
					UserInfoVO user = new UserInfoVO();
					user.setUserId(userId);
					depts = DepartmentUtil.setSpecificDeptPermission(specificDepts, userIds, user);
					continue;
				}
				split = userDept.getDeptFullName().split("->");
				// 说明是顶级部门
				if (split.length == 1) {
					// 用此方法去除重复的部门
					if (deptFullName.add(userDept.getId())) {
						depts.add(userDept);
					}
					continue;
				}
				// 获取顶级部门
				vo = departmentMgrService.getDepartmentVOByName(split[0], orgId);
				// 用此方法去除重复的部门
				if (vo != null && deptFullName.add(vo.getId())) {
					depts.add(vo);
				}
			}
		}
		return depts;
	}

	/**
	 * 根据部门id获取直接负责人信息
	 * @param departIds 部门ids
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-3-7
	 * @version 1.0
	 */
	public List<TbQyUserInfoVO> findUserReceiveByDepartIds(List<String> departIds) throws Exception, BaseException{
		List<TbQyUserInfoVO> infoVOs = new ArrayList<TbQyUserInfoVO>();
		if(!AssertUtil.isEmpty(departIds) && departIds.size() > 0){
			infoVOs.addAll(contactDAO.findUserReceiveByDepartIds(departIds));
			DepartmentUtil.checkRepeatUser(infoVOs);
		}
		return infoVOs;
	}


	@Override
	public String getManagerInfoByOrgId(String orgId) throws Exception, BaseException {
		return contactDAO.getManagerInfoByOrgId(orgId);
	}

	/**
	 *批量获取用户信息，key是userId
	 * @param userIds 用户id列表
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-3-23
	 * @version 1.0
	 */
	public Map<String, TbQyUserInfoVO> getUserInfoByUserIdsToMap(List<String> userIds) throws BaseException, Exception{
		List<TbQyUserInfoVO> list =	getUserInfoByUserIds(ListUtil.toArrays(userIds));
		Map<String, TbQyUserInfoVO> map = new HashMap<String, TbQyUserInfoVO>();
		if(!AssertUtil.isEmpty(list)){
			for(TbQyUserInfoVO vo : list){
				map.put(vo.getUserId(), vo);
			}
		}
		return map;
	}

	@Override
	public List<TbQyUserInfoPO> getUserInfoPOByWxUserIds(String corpId, List<String> wxUserIds) throws Exception {
		if (AssertUtil.isEmpty(wxUserIds)) {
			return null;
		}
		return contactDAO.getUserInfoPOByWxUserIds(corpId, wxUserIds);
	}

	/**
	 * 分页获取某部门下的用户
	 * @param departId 部门id
	 * @param params 传过来的查询条件
	 * @param pager 分页数据
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-11
	 * @version 1.0
	 */
	public Pager getUsersByDepartIdToPager(String departId, Map<String,Object> params, Pager pager) throws Exception, BaseException{
		return contactDAO.getUsersByDepartIdToPager(departId, params, pager);
	}

	/**
	 * 通过用户的userId获取手写签名图片
	 * @param userId 用户的id
	 * @return
	 * @throws Exception 这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author liyixin
	 * @2017-4-13
	 * @version 1.0
	 */
	public TbQyUserHandWritPO getUserHandWritByUserId(String userId) throws BaseException, Exception{
		if(AssertUtil.isEmpty(userId)){
			return null;
		}else {
			return contactDAO.getUserHandWritByUserId(userId);
		}
	}

	/**
	 * 分页获取有手写签名图片的用户
	 * @param pager 分页数据
	 * @param org 机构信息
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-17
	 * @version 1.0
	 */
	public Pager getUserHandByOrgId(Pager pager, UserOrgVO org) throws Exception, BaseException{
		if(AssertUtil.isEmpty(org) || AssertUtil.isEmpty(org.getOrgId())){
			return pager;
		}else {
			return contactDAO.getUserHandByOrgId(pager, org.getOrgId());
		}
	}

	/**
	 * 根据userId获取有手写签名图片的用户信息
	 * @param userId 用户信息
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-4-20
	 * @version 1.0
	 */
	public TbQyUserHandWritVO getUserHandAndUserInfoByUserId(String userId) throws Exception, BaseException{
		if(AssertUtil.isEmpty(userId)){
			return null;
		}else{
			return contactDAO.getUserHandAndUserInfoByUserId(userId);
		}
	}

	/**
	 *根据部门id查询用户列表
	 * @param departId 部门id
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-05-11
	 * @version 1.0
	 */
	public List<InterfaceUser> getUserByDepartIds(String departId, String orgId) throws BaseException, Exception{
		if(AssertUtil.isEmpty(departId) || AssertUtil.isEmpty(orgId)){
			return new ArrayList<InterfaceUser>();
		}
		return contactReadOnlyDAO.getUserByDepartIds(departId, orgId);
	}

	/**
	 * 根据机构id查询手机端搜索条件
	 * @param orgId 机构id
	 * @return
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
	 */
	@Override
	public TbQyUserDefaultSelectPO getUserDefaultByOrgId(String orgId) throws BaseException, Exception {
		TbQyUserDefaultSelectPO selectPO = (TbQyUserDefaultSelectPO) CacheWxqyhObject.get(WxAgentUtil.getAddressBookCode(), orgId, "default");
		if(null == selectPO){
			selectPO = contactDAO.getUserDefaultByOrgId(orgId);
			//如果从数据库查出来为空
			if(AssertUtil.isEmpty(selectPO)){
				selectPO = new TbQyUserDefaultSelectPO();
				selectPO.setDefaultSelect(0);
			}
			CacheWxqyhObject.set(WxAgentUtil.getAddressBookCode(), orgId, "default", selectPO);
		}
		//如果不是金卡vip用户
		if(!VipUtil.hasGoldPermission(orgId, VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
			//如果设置的不是默认值
			if(AssertUtil.isEmpty(selectPO.getDefaultSelect()) || 0 != selectPO.getDefaultSelect()){
				selectPO.setDefaultSelect(0);
			}
		}
		return selectPO;
	}

	/**
	 * 插入或更新手机端默认查询条件
	 *
	 * @param selectPO
	 * @param orgId
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-5-15
	 * @version 1.0
	 */
	@Override
	public void insertOrUpdateDefaultSelect(TbQyUserDefaultSelectPO selectPO, String orgId) throws BaseException, Exception {
		if(AssertUtil.isEmpty(selectPO)){
			return;
		}
		selectPO.setUpdateTime(new Date());
		selectPO.setOrgId(orgId);
		//如果原来是没有的
		if(AssertUtil.isEmpty(selectPO.getId())){
			selectPO.setId(UUID32.getID());
			insertPO(selectPO, false);
		}else{
			updatePO(selectPO, false);
		}
		CacheWxqyhObject.set(WxAgentUtil.getAddressBookCode(), orgId, "default", selectPO);
	}

	@Override
	public List<TbQyChangeContactPO> getChangeContactList(String corpId, String suiteId, long startTimeStamp, long startId, int size) throws Exception, BaseException {
		return contactDAO.getChangeContactList(corpId, suiteId, startTimeStamp, startId, size);
	}
}
