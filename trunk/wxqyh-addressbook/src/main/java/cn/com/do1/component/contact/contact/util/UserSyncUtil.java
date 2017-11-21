package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.*;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 单个用户的同步
 * Created by sunqinghai on 17/3/30.
 */
public class UserSyncUtil {
    private final static transient Logger logger = LoggerFactory.getLogger(UserSyncUtil.class);
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);

    /**
     * 同步单个用户
     * @param agentCode
     * @param wxUserId
     * @param corpId
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-30
     */
    public static boolean syncUser(String agentCode, String wxUserId, String corpId) throws Exception, BaseException {
        if (Configuration.AUTO_CORPID.equals(corpId)) {
            return false;
        }
        DqdpOrgVO dqdpOrgVO = OrgUtil.getOrgByCorpId(corpId);
        WxUser user = WxUserService.getUser(wxUserId, corpId, dqdpOrgVO.getOrgId(), agentCode);
        if (user == null) {
            return false;
        }
        //新增部门信息
        List<DeptSyncInfoVO> addDeptList = new ArrayList<DeptSyncInfoVO>(10);//需要新增的部门list
        List<String> detId = new ArrayList<String>();
        // 处理部门，不存在时自动创建
        getDepartByWeixin(dqdpOrgVO, user, addDeptList, detId, agentCode);
        //新增用户
        addUser(dqdpOrgVO, user, addDeptList, detId);
        return true;
    }

    /**
     * 新增用户
     * @param dqdpOrgVO
     * @param user
     * @param addDeptList
     * @param detId
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -4-12
     */
    private static void addUser (DqdpOrgVO dqdpOrgVO, WxUser user, List<DeptSyncInfoVO> addDeptList, List<String> detId) throws Exception, BaseException {
        //新增用户
        TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
        tbQyUserInfoPO.setId(ContactUtil.getUserId(dqdpOrgVO.getCorpId(), user.getUserid()));
        tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
        tbQyUserInfoPO.setCorpId(dqdpOrgVO.getCorpId());
        ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user);
        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
        tbQyUserInfoPO.setCreateTime(new Date());
        tbQyUserInfoPO.setOrgId(dqdpOrgVO.getOrgId());
        tbQyUserInfoPO.setIsConcerned("0");
        contactService.insertUser(tbQyUserInfoPO, detId);
        /*List<TbQyUserInfoPO> addUserList = new ArrayList<TbQyUserInfoPO>(1);
        addUserList.add(tbQyUserInfoPO);
        Map<String, List<String>> userRefDeptMap = new HashMap<String, List<String>>(detId.size());
        userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
        Map<String, List<String>> addDeptRefUserMap = new HashMap<String, List<String>>(detId.size());
        UserInfoChangeNotifier.getDeptUserRef(detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap);*/

        UserOrgVO userOrgVO = new UserOrgVO();
        userOrgVO.setOrgId(dqdpOrgVO.getOrgId());
        userOrgVO.setCorpId(dqdpOrgVO.getCorpId());
        userOrgVO.setWxId(dqdpOrgVO.getWxId());
        userOrgVO.setWxParentid(dqdpOrgVO.getWxParentid());
        userOrgVO.setOrgName(dqdpOrgVO.getOrgName());
        UserInfoChangeNotifier.batchChangeDept(userOrgVO, addDeptList, null, null, UserInfoChangeInformType.SYNC_MGR);
        UserInfoChangeNotifier.addUser(userOrgVO, tbQyUserInfoPO, detId, null, UserInfoChangeInformType.SYNC_MGR);
    }

    /**
     * 同步单个人员信息
     * @param corpId
     * @param user
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -4-12
     */
    public static void syncUser(String corpId, WxUser user) throws Exception, BaseException {
        if (Configuration.AUTO_CORPID.equals(corpId)) {
            return;
        }
        DqdpOrgVO dqdpOrgVO = OrgUtil.getOrgByCorpId(corpId);

        TbQyUserInfoPO po = contactService.findUserInfoPOByWxUserId(user.getUserid(), corpId);
        if (po == null) {
            //新增用户
            //新增部门信息
            List<DeptSyncInfoVO> addDeptList = new ArrayList<DeptSyncInfoVO>(10);//需要新增的部门list
            List<String> detId = new ArrayList<String>();
            getDepartByWeixin(dqdpOrgVO, user, addDeptList, detId, WxAgentUtil.getAddressBookCode());
            addUser(dqdpOrgVO, user, addDeptList, detId);
        }
        else {
            //更新用户
            updateUser(dqdpOrgVO, po, user);
        }
    }

    /**
     * 新增用户
     * @param dqdpOrgVO
     * @param vo
     * @param user
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -4-12
     */
    private static void updateUser (DqdpOrgVO dqdpOrgVO, TbQyUserInfoPO vo, WxUser user) throws Exception, BaseException {
        boolean[] isChange = WxUserService.verifyChange(vo, user);
        //新增部门信息
        List<DeptSyncInfoVO> addDeptList;//需要新增的部门list
        List<String> detId;
        List<String> userDept;
        //判断是否需要更新，如果没有修改
        if (!isChange[0]) {
            addDeptList = new ArrayList<DeptSyncInfoVO>(10);//需要新增的部门list
            detId = new ArrayList<String>();
            getDepartByWeixin(dqdpOrgVO, user, addDeptList, detId, WxAgentUtil.getAddressBookCode());
            userDept = departmentService.getDeptUserRefByUserId(vo.getUserId());
            if(ListUtil.strCsStr(detId, userDept)){
                return;
            }
        }
        else {
            addDeptList = new ArrayList<DeptSyncInfoVO>(10);//需要新增的部门list
            detId = new ArrayList<String>();
            getDepartByWeixin(dqdpOrgVO, user, addDeptList, detId, WxAgentUtil.getAddressBookCode());
            userDept = departmentService.getDeptUserRefByUserId(vo.getUserId());
        }
        TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
        tbQyUserInfoPO.setId(vo.getId());
        ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, vo);
        if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {//离职
            tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
        }
        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
        tbQyUserInfoPO.setOrgId(dqdpOrgVO.getOrgId());
        tbQyUserInfoPO.setUserId(vo.getUserId());

        tbQyUserInfoPO.setUpdateTime(new Date());
        contactService.updateUser(tbQyUserInfoPO, detId, false);

        UserOrgVO userOrgVO = new UserOrgVO();
        userOrgVO.setOrgId(dqdpOrgVO.getOrgId());
        userOrgVO.setCorpId(dqdpOrgVO.getCorpId());
        userOrgVO.setWxId(dqdpOrgVO.getWxId());
        userOrgVO.setWxParentid(dqdpOrgVO.getWxParentid());
        userOrgVO.setOrgName(dqdpOrgVO.getOrgName());
        UserInfoChangeNotifier.batchChangeDept(userOrgVO, addDeptList, null, null, UserInfoChangeInformType.SYNC_MGR);
        tbQyUserInfoPO.setCreateTime(vo.getCreateTime());
        tbQyUserInfoPO.setOrgId(vo.getOrgId());
        tbQyUserInfoPO.setCorpId(vo.getCorpId());
        tbQyUserInfoPO.setEntryTime(vo.getEntryTime());
        tbQyUserInfoPO.setLeaveTime(vo.getLeaveTime());
        UserInfoChangeNotifier.updateUser(userOrgVO, vo, userDept, tbQyUserInfoPO, detId, null, UserInfoChangeInformType.SYNC_MGR);
    }

    /**
     * 处理部门，不存在时自动创建
     * @param dqdpOrgVO
     * @param user
     * @param addDeptList
     * @param detId
     * @param agentCode
     * @author sunqinghai
     * @date 2017 -4-12
     */
    private static void getDepartByWeixin(DqdpOrgVO dqdpOrgVO, WxUser user, List<DeptSyncInfoVO> addDeptList, List<String> detId, String agentCode) throws Exception, BaseException {
        TbDepartmentInfoPO department;
        // 处理部门，不存在时自动创建
        if (user.getDepartment() != null && user.getDepartment().size() > 0) {
            int size = user.getDepartment().size();
            // 处理部门，不存在的部门自动新建
            for (int i = 0; i < size; i++) {
                //如果有多部门，并且该用户属于根节点，跳过根节点的部门信息不保存
                if (size > 1 && dqdpOrgVO.getWxId().equals(user.getDepartment().get(i).toString())) {
                    continue;
                }
                department = addDepartByWeixin(user.getDepartment().get(i).toString(), dqdpOrgVO.getOrgId(), dqdpOrgVO.getCorpId(), agentCode, addDeptList);
                if (department == null) {
                    continue;//如果有部门找不到的，继续
                }
                detId.add(department.getId());
            }
        }
        if (detId.size() == 0) {
            //获取无部门的情况
            department = DepartmentUtil.getWxEmptyDept(dqdpOrgVO.getOrgId());
            detId.add(department.getId());
        }
    }


    /**
     * 从微信上的部门新增到本地
     * @param wxDeptId
     * @param orgId
     * @param corpId
     * @param agentCode
     * @param addDeptList
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-30
     */
    private static TbDepartmentInfoPO addDepartByWeixin(String wxDeptId, String orgId, String corpId, String agentCode, List<DeptSyncInfoVO> addDeptList) {
        try {
            TbDepartmentInfoPO department = departmentService.getDeptByWeixin(orgId, wxDeptId);
            if (department != null) {
                return department;
            }
            List<WxDept> list = WxDeptService.getDept(corpId, wxDeptId, orgId, agentCode);
            if (list.size() == 0) {
                return null;
            }
            for (WxDept wxDept : list) {
                if (wxDeptId.equals(wxDept.getId())) {
                    TbDepartmentInfoPO parentDepart = departmentService.getDeptByWeixin(orgId, wxDept.getParentid());
                    TbDepartmentInfoPO tbDepartmentInfoPO = new TbDepartmentInfoPO();
                    tbDepartmentInfoPO.setId(DepartmentUtil.getDeptId(corpId, wxDeptId));
                    tbDepartmentInfoPO.setCreateTime(new Date());
                    tbDepartmentInfoPO.setOrgId(orgId);
                    tbDepartmentInfoPO.setShowOrder(wxDept.getOrder());
                    tbDepartmentInfoPO.setDepartmentName(wxDept.getName());
                    tbDepartmentInfoPO.setWxParentid(wxDept.getParentid());
                    tbDepartmentInfoPO.setWxId(wxDeptId);
                    if (parentDepart == null) {
                        tbDepartmentInfoPO.setDeptFullName(wxDept.getName());
                        tbDepartmentInfoPO.setParentDepart("");
                    }
                    else {
                        tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                        tbDepartmentInfoPO.setDeptFullName(parentDepart.getDeptFullName()+DepartmentUtil.DEPT_NAME_SPLIT+wxDept.getName());
                        tbDepartmentInfoPO.setParentDepart(parentDepart.getId());
                    }
                    departmentService.insertPO(tbDepartmentInfoPO, false);
                    DeptSyncInfoVO vo = new DeptSyncInfoVO();
                    BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                    addDeptList.add(vo);
                    return tbDepartmentInfoPO;
                }
            }
        } catch (BaseException e) {
            logger.error("UserSyncUtil addDepartByWeixin " +corpId + wxDeptId, e);
            ExceptionCenter.addException(e, "UserSyncUtil addDepartByWeixin @sqh", corpId + "," +wxDeptId);
        } catch (Exception e) {
            logger.error("UserSyncUtil addDepartByWeixin " +corpId + wxDeptId, e);
            ExceptionCenter.addException(e, "UserSyncUtil addDepartByWeixin @sqh", corpId + "," +wxDeptId);
        }
        return null;
    }
}
