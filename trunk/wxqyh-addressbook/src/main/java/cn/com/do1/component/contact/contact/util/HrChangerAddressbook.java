package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UpdateUserResult;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.qwinterface.addressbook.HrEmployeeInf;
import cn.com.do1.component.qwinterface.addressbook.IHrBaseInfoChanger;
import cn.com.do1.component.qwinterface.addressbook.SimpleLeaveInf;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sunqinghai on 16-4-25.
 */
@ProcesserUnit(name = "hrChangerAddressbook")
public class HrChangerAddressbook implements IHrBaseInfoChanger {
    private final static transient Logger logger = LoggerFactory.getLogger(HrChangerAddressbook.class);
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    private static IContactMgrService contactMgrService = DqdpAppContext.getSpringContext().getBean("contactService", IContactMgrService.class);
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);

    /**
     * 批量对员工进行同步
     *
     * @param user       操作此次变动的操作人
     * @param addList
     * @param updateList
     * @param delList
     * @注意 因为员工可以编辑自己的信息，所以user可能为空
     */
    @Override
    public Map<String,String> baseInfoChanged(UserOrgVO user, List<HrEmployeeInf> addList, List<HrEmployeeInf> updateList, List<String> delList) throws Exception, BaseException {
        if(Configuration.IS_OLD_SEND_WX_MESSAGE){
            return null;
        }
        List<TbQyUserInfoPO> addUserList = null;
        List<TbQyUserInfoPO> updateUserList = null;
        Map<String, List<String>> userRefDeptMap = null;
        Map<String, List<String>> addDeptRefUserMap = null;
        Map<String, List<String>> delDeptRefUserMap = null;
        Map<String,String> errorMap = new HashMap<String, String>();
        try {
            Map<String,TbDepartmentInfoPO> map = null;
            if((addList!=null && addList.size()>1)
                    || (updateList != null && updateList.size()>1)){
                List<TbDepartmentInfoPO> departList = departmentService.getAllDepart(user.getOrgId());
                if(departList == null || departList.size()==0){
                    getErrorMap(addList,updateList,errorMap,"部门为空，请先新增部门信息");
                    return errorMap;
                }
                map = new HashMap<String, TbDepartmentInfoPO>(departList.size());
                for(TbDepartmentInfoPO po : departList){
                    map.put(po.getId(),po);
                }
            }
            //判断是否新增和是否更新
            int totalSize = 0;
            boolean isAdd = false;
            boolean isUpdate = false;
            if(addList != null && addList.size()>0){
                totalSize += addList.size();
                isAdd = true;
            }
            if(updateList != null && updateList.size()>0){
                totalSize += updateList.size();
                isUpdate = true;
            }
            if(isAdd || isUpdate){
                addUserList = new ArrayList<TbQyUserInfoPO>(totalSize);
                userRefDeptMap = new HashMap<String, List<String>>(totalSize);
                addDeptRefUserMap = new HashMap<String, List<String>>(100);
            }
            if(isAdd){
                addUserList(user,addList,map,errorMap,addUserList,userRefDeptMap,addDeptRefUserMap);
            }
            if(isUpdate){
                updateUserList = new ArrayList<TbQyUserInfoPO>(updateList.size());
                delDeptRefUserMap = new HashMap<String, List<String>>(100);
                updateUserList(user,updateList,map,errorMap,addUserList,updateUserList,userRefDeptMap,addDeptRefUserMap,delDeptRefUserMap);
            }
            if(delList != null && delList.size()>0){
                contactService.batchDeleteUserByUserIds(delList,user);
                UserInfoChangeNotifier.batchDelEnd(user, ListUtil.toArrays(delList), UserInfoChangeInformType.HR_MGR);
            }
        } catch (BaseException e) {
            ExceptionCenter.addException(e,"HrChangerAddressbook baseInfoChanged",delList.toString());
            throw e;
        } catch (Exception e) {
            ExceptionCenter.addException(e,"HrChangerAddressbook baseInfoChanged",delList.toString());
            throw e;
        }
        if((addUserList != null && addUserList.size()>0) || (updateUserList!=null && updateUserList.size()>0)){
            UserInfoChangeNotifier.batchChanged(user,addUserList,updateUserList,null,userRefDeptMap,addDeptRefUserMap,delDeptRefUserMap,UserInfoChangeInformType.HR_MGR);
        }
        //如果操作的是单个用户
        if((addList!=null && addList.size()==1 && (updateList == null || updateList.size()==0))
                || (updateList != null && updateList.size()==1 && (addList==null || addList.size()==0))){
            if(errorMap.size()==1){
                Iterator it = errorMap.keySet().iterator();
                throw new BaseException("100",errorMap.get(it.next()));
            }
        }
        return errorMap;
    }

    /**
     * 操作所有用户失败，返回所有用户的失败信息
     * @return
     * @author Sun Qinghai
     * @ 16-5-13
     */
    private void getErrorMap(List<HrEmployeeInf> addList, List<HrEmployeeInf> updateList, Map<String, String> errorMap, String desc){
        getErrorMap(addList,errorMap,desc);
        getErrorMap(updateList,errorMap,desc);
    }

    private void getErrorMap(List<HrEmployeeInf> list,Map<String,String> errorMap,String desc){
        if(list!=null && list.size()>0){
            for(HrEmployeeInf he : list){
                errorMap.put(he.getAccountName(),desc);
            }
        }
    }

    private void addUserList(UserOrgVO user, List<HrEmployeeInf> addList, Map<String, TbDepartmentInfoPO> departAll, Map<String, String> errorMap, List<TbQyUserInfoPO> addUserList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap) {
        List<String> wxDeptIds = new ArrayList<String>(10);
        for(HrEmployeeInf hei : addList){
            Map<String,TbDepartmentInfoPO> map = departAll;
            if(map == null){
                try {
                    List<TbDepartmentInfoPO> list = departmentService.getDeptsByIds(hei.getDeptIds());
                    map = new HashMap<String, TbDepartmentInfoPO>(list.size());
                    for(TbDepartmentInfoPO po : list){
                        map.put(po.getId(),po);
                    }
                } catch (Exception e) {
                    logger.error("HrChangerAddressbook addUserList", e);
                    ExceptionCenter.addException(e,"HrChangerAddressbook addUserList",hei.getDeptIds().toString());
                    errorMap.put(hei.getAccountName(),"新增失败：用户所在部门不存在，请先创建部门");
                    continue;
                }
            }
            addUser(user,hei,map,wxDeptIds,errorMap,addUserList,userRefDeptMap,addDeptRefUserMap);
        }
    }

    private void addUser(UserOrgVO user, HrEmployeeInf hei, Map<String, TbDepartmentInfoPO> departAll, List<String> wxDeptIds, Map<String, String> errorMap, List<TbQyUserInfoPO> addUserList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap){
        TbQyUserInfoPO po = null;
        try {
            po = new TbQyUserInfoPO();
            copyUserFromHr(po,hei);
            getWxDeptIds(hei.getDeptIds(),departAll,wxDeptIds);
            if(wxDeptIds.size()==0){
                logger.error("HrChangerAddressbook addUser wxDeptIds is null "+hei.getDeptIds().toString());
                ExceptionCenter.addException(new BaseException("HrChangerAddressbook addUser wxDeptIds is null"),"HrChangerAddressbook addUser wxDeptIds is null",hei.getDeptIds().toString());
                errorMap.put(hei.getAccountName(),"新增失败：用户所在部门异常，请先创建部门");
                return;
            }

            po.setOrgId(user.getOrgId());
            po.setCorpId(user.getCorpId());
            po.setCreatePerson(user.getUserName());
            po.setId(hei.getEmployeeId());
            po.setHeadPic("0");
            contactMgrService.insertUser(po, hei.getDeptIds(), wxDeptIds);
            addUserList.add(po);
            userRefDeptMap.put(po.getUserId(),hei.getDeptIds());
            UserInfoChangeNotifier.getDeptUserRef(hei.getDeptIds(),po.getUserId(),addDeptRefUserMap);
        } catch (BaseException e) {
            logger.error("HrChangerAddressbook addUser", e);
            ExceptionCenter.addException(e,"HrChangerAddressbook addUser",po.toString());
            errorMap.put(hei.getAccountName(),"新增失败："+e.getMessage());
        } catch (Exception e) {
            logger.error("HrChangerAddressbook addUser", e);
            ExceptionCenter.addException(e,"HrChangerAddressbook addUser",po.toString());
            errorMap.put(hei.getAccountName(),"新增失败：请通过企微官网联系客服人员");
        }
    }

    private void updateUser(UserOrgVO user, HrEmployeeInf hei, TbQyUserInfoPO po, Map<String, TbDepartmentInfoPO> departAll, List<String> wxDeptIds, Map<String, String> errorMap, List<TbQyUserInfoPO> updateUserList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap){
        try {
            TbQyUserInfoPO old = new TbQyUserInfoPO();
            BeanHelper.copyProperties(old, po);
            copyUserFromHr(po,hei);
            getWxDeptIds(hei.getDeptIds(),departAll,wxDeptIds);
            if(wxDeptIds.size()==0){
                logger.error("HrChangerAddressbook updateUser wxDeptIds is null "+hei.getDeptIds().toString());
                ExceptionCenter.addException(new BaseException("HrChangerAddressbook updateUser wxDeptIds is null"),"HrChangerAddressbook updateUser wxDeptIds is null",hei.getDeptIds().toString());
                errorMap.put(hei.getAccountName(),"更新失败：用户所在部门异常，请先创建部门");
                return;
            }

            CacheSessionManager.update(po.getUserId());
            UpdateUserResult uur = contactMgrService.updateUser(po, old, hei.getDeptIds(), wxDeptIds, true);
            updateUserList.add(po);
            userRefDeptMap.put(po.getUserId(),hei.getDeptIds());
            if(uur.isUpdateDept()){//更新了部门信息
                UserInfoChangeNotifier.getDeptUserRef(uur.getOldDeptIds(),hei.getDeptIds(),po.getUserId(),addDeptRefUserMap,delDeptRefUserMap);
            }

            CacheSessionManager.update(po.getUserId());
        } catch (BaseException e) {
            logger.error("HrChangerAddressbook updateUser", e);
            ExceptionCenter.addException(e,"HrChangerAddressbook updateUser",po.toString());
            errorMap.put(hei.getAccountName(),"更新失败："+e.getMessage());
        } catch (Exception e) {
            logger.error("HrChangerAddressbook updateUser", e);
            ExceptionCenter.addException(e,"HrChangerAddressbook updateUser",po.toString());
            errorMap.put(hei.getAccountName(),"更新失败：请通过企微官网联系客服人员");
        }
    }

    private void updateUserList(UserOrgVO user, List<HrEmployeeInf> updateList, Map<String, TbDepartmentInfoPO> departAll, Map<String, String> errorMap, List<TbQyUserInfoPO> addUserList, List<TbQyUserInfoPO> updateUserList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap) {
        TbQyUserInfoPO po = null;
        List<String> wxDeptIds = new ArrayList<String>(10);
        for(HrEmployeeInf hei : updateList){
            Map<String,TbDepartmentInfoPO> map = departAll;
            if(map == null){
                try {
                    List<TbDepartmentInfoPO> list = departmentService.getDeptsByIds(hei.getDeptIds());
                    map = new HashMap<String, TbDepartmentInfoPO>(list.size());
                    for(TbDepartmentInfoPO dept : list){
                        map.put(dept.getId(),dept);
                    }
                } catch (Exception e) {
                    logger.error("HrChangerAddressbook updateUserList", e);
                    ExceptionCenter.addException(e,"HrChangerAddressbook updateUserList",hei.getDeptIds().toString());
                    errorMap.put(hei.getAccountName(),"更新失败：用户所在部门不存在，请先创建部门");
                    continue;
                }
            }
            try {
                po = contactService.findUserInfoPOByUserId(hei.getUserId());
                if(po==null){
                    addUser(user,hei,map,wxDeptIds, errorMap, addUserList, userRefDeptMap, addDeptRefUserMap);
                }
                else{
                    updateUser(user,hei,po,map,wxDeptIds, errorMap,updateUserList,userRefDeptMap,addDeptRefUserMap,delDeptRefUserMap);
                }
            } catch (BaseException e) {
                logger.error("HrChangerAddressbook updateUserList", e);
                ExceptionCenter.addException(e,"HrChangerAddressbook updateUserList",po.toString());
                errorMap.put(hei.getAccountName(),"更新失败："+e.getMessage());
            } catch (Exception e) {
                logger.error("HrChangerAddressbook updateUserList", e);
                ExceptionCenter.addException(e,"HrChangerAddressbook updateUserList",po.toString());
                errorMap.put(hei.getAccountName(),"更新失败：请通过企微官网联系客服人员");
            }
        }
    }

    private void getWxDeptIds(List<String> deptIds, Map<String,TbDepartmentInfoPO> departAll, List<String> wxDeptIds) {
        wxDeptIds.clear();
        if(deptIds==null && deptIds.size()==0){
            return;
        }
        for (String id : deptIds){
            TbDepartmentInfoPO vo = departAll.get(id);
            if(vo!= null){
                wxDeptIds.add(vo.getWxId());
            }
        }
    }

    /**
     * 对员工进行离职操作
     *
     * @param user 操作此次变动的操作人
     * @param leaveInf
     */
    @Override
    public void leave(UserOrgVO user, SimpleLeaveInf leaveInf) throws Exception, BaseException {
        if(Configuration.IS_OLD_SEND_WX_MESSAGE){
            return;
        }
        TbQyUserInfoPO po = null;
        try {
            po = toUserPo(leaveInf);
            TbQyUserInfoPO userPO = contactService.findUserInfoPOByUserId(leaveInf.getUserId());
            if(userPO == null){
                po.setOrgId(user.getOrgId());
                return;
            }
            po.setOrgId(userPO.getOrgId());
            po.setCorpId(userPO.getCorpId());
            po.setId(userPO.getId());
            po.setWxUserId(userPO.getWxUserId());
            po.setPersonName(userPO.getPersonName());
            contactService.leaveUser(po,user.getUserName(),true,null);
        } finally {
            UserInfoChangeNotifier.leaveUser(user, po, null, UserInfoChangeInformType.HR_MGR,new HashMap<String, List<HandoverMatterVO>>());
        }
    }

    /**
     * 重构某部门下的所有用户与人员的映射关系
     *
     * @param user    操作此次变动的操作人
     * @param deptId  被变动的部门ID
     * @param userIds 被变动的部门变动后所包含的人员
     */
    @Override
    public void bindDeptRef(UserOrgVO user, String deptId, List<String> userIds) throws Exception, BaseException {
        if(Configuration.IS_OLD_SEND_WX_MESSAGE){
            return;
        }
    }

    /**
     * 重构某用户与其所属部门间的映射关系
     *
     * @param user    操作此次变动的操作人
     * @param userId  被变动的人员ID
     * @param deptIds 变动后人员的所属部门（全部）
     */
    @Override
    public void bindUserRef(UserOrgVO user, String userId, List<String> deptIds) throws Exception, BaseException {
        if(Configuration.IS_OLD_SEND_WX_MESSAGE){
            return;
        }
    }

    /**
     * 完全重整整个机构的部门人员关系
     *
     * @param orgId
     */
    @Override
    public void wholeRebind(String orgId) throws Exception, BaseException {
    }

    private TbQyUserInfoPO toUserPo(SimpleLeaveInf sli){
        TbQyUserInfoPO po = new TbQyUserInfoPO();
        po.setUserId(sli.getUserId());
        po.setLeaveTime(sli.getDay());
        po.setLeaveRemark(sli.getReason());
        return po;
    }

    private TbQyUserInfoPO copyUserFromHr(TbQyUserInfoPO po,HrEmployeeInf hei){
        po.setUserId(hei.getUserId());
        po.setPersonName(hei.getPersonName());
        po.setPinyin(hei.getPinyin());
        po.setSex((hei.getSex() == null) ? null : hei.getSex().toString());
        po.setWxUserId(hei.getAccountName());
        po.setNickName(hei.getNickName());
        po.setWeixinNum(hei.getWeixin());
        po.setMobile(hei.getMobile());
        po.setEmail(hei.getEmail());
        po.setPosition(hei.getPositionName());
        po.setEntryTime(hei.getJoinDay());
        po.setIdentity(hei.getCardId());
        po.setAddress(hei.getAddressNow());
        po.setBirthday(hei.getBirthDay());
        po.setLunarCalendar(hei.getBirthDayMoon());
        po.setRemindType((hei.getBirthNotifyType() == null) ? null : hei.getBirthNotifyType().toString());
        //po.setHeadPic((hei.getHeadPic() == null) ? "0" : hei.getHeadPic());
        return po;
    }
}
