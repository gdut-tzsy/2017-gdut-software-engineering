package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyOrgVersionPO;
import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoExtVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.qwinterface.addressbook.IUserInfoChangeInform;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2016/10/10.
 */
@ProcesserUnit(name = "orgChangeVersion")
public class OrgChangeVersion implements IUserInfoChangeInform {
    /**
     * 日志信息
     */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(OrgChangeVersion.class);
    /**
     * service
     */
    private static IContactMgrService contactMgrService = DqdpAppContext.getSpringContext().getBean("contactService", IContactMgrService.class);

    /**
     *
     */
    private static IDepartmentMgrService departmentMgrService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentMgrService.class);

    /**
     * 新增用户信息
     * @param user 操作管理员
     * @param userInfoPo 用户信息
     * @param deptId  用户部门id
     * @param userInfoExtVo  用户扩展信息
     * @param type  操作类型（新增用户、导入、同步等）
     * @author liyixin
     * @2016-10-10
     * @version 1.0
     */
    @Override
    public void addUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVo, int type)  {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userInfoPo.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.ADD_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
            }
        } catch (BaseException e) {
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        } catch (Exception e) {
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }

    }

    /**
     * 修改用户信息
     * @param user 用户po
     * @param oldUser 修改前的用户po
     * @param oldDeptId  修改后的用户部门id
     * @param userInfoPo
     * @param deptId  用户部门id
     * @param userInfoExtVO  用户扩展信息
     * @param type  操作类型（新增用户、导入、同步等）
     * @author liyixin
     * @2016-10-11
     * @version 1.0
     */
    @Override
    public void updateUser(UserOrgVO user, TbQyUserInfoPO oldUser, List<String> oldDeptId, TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userInfoPo.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent+1;
                //用户在版本控制表内，进行更新
                if(contactMgrService.findVersionIdById(userInfoPo.getUserId())){
                    updateOrgVersion(OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
                //不在，进行新增
                else {
                    addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
            }

        }catch (BaseException e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     *删除用户信息
     * @param user 用户po
     * @param userInfoPo
     * @param deptId  用户部门id
     * @param type  操作类型（新增用户、导入、同步等）
     * @author liyixin
     * @2016-10-11
     * @version 1.0
     */
    @Override
    public void delUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type) {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userInfoPo.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent+1;
                //用户在版本控制表内，进行更新
                if(contactMgrService.findVersionIdById(userInfoPo.getUserId())){
                    updateOrgVersion(OrgVersionType.DELE_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
                //不在，进行新增
                else {
                   addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.DELE_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
            }
        }catch (Exception e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (BaseException e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     *离职用户信息
     * @param user 用户po
     * @param userInfoPo
     * @param deptId  用户部门id
     * @param type  操作类型（新增用户、导入、同步等）
     * @param matterMap 离职用户需要交接的事项 key是agentCode,value是需要交接的事项列表
     * @author liyixin
     * @2016-10-11
     * @version 1.0
     */
    @Override
    public void leaveUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap) {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userInfoPo.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent + 1;
                //用户在版本控制表内，进行更新
                if(contactMgrService.findVersionIdById(userInfoPo.getUserId())){
                    updateOrgVersion(OrgVersionType.LEAVER_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
                //不在，进行新增
                else {
                    addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.LEAVER_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }

            }
        }catch (BaseException e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错s误", e);
        }
    }

    /**
     *批量离职
     * @param user 管理员
     * @param users 用户列表
     * @param type
     * @author liyixin
     * @2016-10-1
     * @version 1.0
     */
    @Override
    public void batchLeaveUser(UserOrgVO user, List<TbQyUserInfoPO> users, int type) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(user.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent + 1;
                batchOrgUserVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.LEAVER_USER_TYPE, orgVesionRecent, user.getOrgId(), users);
            }
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     *复职用户信息
     * @param user 管理员
     * @param userInfoPo 用户信息
     * @param deptId  用户部门id
     * @param userInfoExtVo
     * @param type  操作类型（新增用户、导入、同步等）
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
    @Override
    public void recoverUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVo, int type) {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userInfoPo.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent + 1;
                //用户在版本控制表内，进行更新
                if(contactMgrService.findVersionIdById(userInfoPo.getUserId())){
                    updateOrgVersion(OrgVersionType.RECOVER_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
                //不在，进行新增
                else {
                    addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.RECOVER_USER_TYPE, orgVesionRecent, userInfoPo, versionPo);
                }
            }
        }catch (BaseException e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            ExceptionCenter.addException(e, "控制机构版本用户同步错误", versionPo.toString());
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     * 同步结束提醒
     * @param user
     * @param addList
     * @param updateList
     * @param delList 离职用户列表
     * @param userRefDeptMap 用户部门管理数据，离职用户的关联数据不在此列
     * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
     * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
     * @param type 操作来源类型
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
    @Override
    public void batchChanged(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                batchOrgUserVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.ADD_USER_TYPE, orgVesionRecent, user.getOrgId(), addList);
                batchOrgUserVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, user.getOrgId(), updateList);
                batchOrgUserVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.LEAVER_USER_TYPE, orgVesionRecent, user.getOrgId(), delList);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     * 批量删除结束提醒
     * @param user
     * @param userIds
     * @param type
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
    @Override
    public void batchDelEnd(UserOrgVO user, String[] userIds, int type) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            List<String> userList = new ArrayList<String>();
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                //把userIds放入列表中
                for(int i = 0; i < userIds.length; i++){
                    userList.add(userIds[i]);
                }
                this.batchVersionId(OrgVersionType.USER_ID_TYPE, OrgVersionType.DELE_USER_TYPE, orgVesionRecent, user.getOrgId(), userList);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }


    }

    /**
     * 批量移动部门结束提醒
     * @param user
     * @param userIds
     * @param deptId
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    @Override
    public void batchMoveEnd(UserOrgVO user, List<String> userIds, List<String> deptId) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                batchVersionId(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, user.getOrgId(), userIds);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     * 批量从特定部门移出用户
     * @param user
     * @param userIds
     * @param deptId
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    @Override
    public void batchShiftOutUser(UserOrgVO user, List<String> userIds, List<String> deptId) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                batchVersionId(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, user.getOrgId(), userIds);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     *批量移入到特定部门用户
     * @param user
     * @param userIds
     * @param deptId
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    @Override
    public void batchShiftIntUser(UserOrgVO user, List<String> userIds, List<String> deptId) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                batchVersionId(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, user.getOrgId(), userIds);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     * 更新用户状态
     * @param userPo
     * @param type
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    @Override
    public void updateUserStatusAndHeadPic(TbQyUserInfoPO userPo, int type) {
        TbQyOrgVersionPO versionPo = new TbQyOrgVersionPO();
        try {
            //该机构的最新版本号
            Integer orgVesionRecent =  checkVesion(userPo.getOrgId());
            //需要版本控制
            if(0 != orgVesionRecent){
                orgVesionRecent = orgVesionRecent+1;
                //用户在版本控制表内，进行更新
                if(contactMgrService.findVersionIdById(userPo.getUserId())){
                    updateOrgVersion(OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, userPo, versionPo);
                }
                //不在，进行新增
                else {
                    addOrgVersion(OrgVersionType.USER_ID_TYPE, OrgVersionType.UPDATE_USER_TYPE, orgVesionRecent, userPo, versionPo);
                }
            }

        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     * 批量操作部门信息
     * @param user 操作人
     * @param addList 新增部门list
     * @param updateList 更新的部门list，当更新了部门名称，父部门，微信部门id，微信父部门id时才会加入到更新部门list中
     * @param delList 删除的部门list
     * @param type
     */
    @Override
    public void batchChangeDept(UserOrgVO user, List<DeptSyncInfoVO> addList, List<DeptSyncInfoVO> updateList, List<DeptSyncInfoVO> delList, int type) {
        try {
            //该机构的最新版本号
            Integer orgVesionRecent = checkVesion(user.getOrgId());
            //需要版本控制
            if (0 != orgVesionRecent) {
                orgVesionRecent = orgVesionRecent + 1;
                batchOrgDeptVersion(OrgVersionType.DEPA_ID_TYPE, OrgVersionType.ADD_DEPA_TYPE, orgVesionRecent, user.getOrgId(), addList);
                batchOrgDeptVersion(OrgVersionType.DEPA_ID_TYPE, OrgVersionType.UPDATE_DEPA_TYPE, orgVesionRecent, user.getOrgId(), updateList);
                batchOrgDeptVersion(OrgVersionType.DEPA_ID_TYPE, OrgVersionType.DELE_DEPA_TYPE, orgVesionRecent, user.getOrgId(), delList);
            }
        }catch (BaseException e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }catch (Exception e){
            LOGGER.error("控制机构版本用户同步错误", e);
        }
    }

    /**
     *检查这家企业需不需要进行版本控制
     * @param orgId 机构Id
     * @return
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-10
     * @version 1.0
     */
    private Integer checkVesion(String orgId) throws BaseException, Exception{
        Integer orgVesionRecent = (Integer) CacheWxqyhObject.get("addressBook", orgId, "orgVesionRecent");
        //创建一个新缓存
        if(orgVesionRecent == null){
            orgVesionRecent = addVesionCache(orgId);
        }
        return orgVesionRecent;
    }

    /**
     * 创建机构版本的缓存
     * @param orgId 机构Id
     * @return
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-10
     * @version 1.0
     */
    private Integer addVesionCache(String orgId) throws BaseException, Exception{
        Integer orgVesionRecent = contactMgrService.findVersionOrgByorgId(orgId);
        //0表示该机构user不需要版本控制
        if(null == orgVesionRecent){
            orgVesionRecent = 0;
        }
        CacheWxqyhObject.set("addressBook", orgId, "orgVesionRecent", orgVesionRecent);
        return orgVesionRecent;
    }

    /**
     * 新增机构版本的用户或者部门id
     * @param idType id类型
     * @param operationType 操作类型
     * @param orgVesionRecent 机构最新版本
     * @param userInfoPo 用户信息
     * @param versionPo 机构版本
     * @return
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-10
     * @version 1.0
     */
    private void  addOrgVersion(String idType, String operationType, Integer orgVesionRecent, TbQyUserInfoPO userInfoPo, TbQyOrgVersionPO versionPo) throws BaseException, Exception{
        versionPo.setId(userInfoPo.getUserId());
        versionPo.setOrgId(userInfoPo.getOrgId());
        versionPo.setCreateTime(new Date());
        versionPo.setUpdateTime(new Date());
        versionPo.setIdType(idType);
        versionPo.setOperationType(operationType);
        versionPo.setOrgVersion(orgVesionRecent);
        contactMgrService.insertPO(versionPo, false);
        CacheWxqyhObject.set("addressBook", userInfoPo.getOrgId(), "orgVesionRecent", orgVesionRecent);
    }

    /**
     *更新机构版本的用户或者部门id状态
     * @param operationType 操作类型
     * @param orgVesionRecent 机构最新版本
     * @param userInfoPo 用户信息
     * @param versionPo 机构版本
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
     private void updateOrgVersion(String operationType, Integer orgVesionRecent, TbQyUserInfoPO userInfoPo, TbQyOrgVersionPO versionPo) throws BaseException, Exception{
        versionPo.setId(userInfoPo.getUserId());
        versionPo.setUpdateTime(new Date());
        versionPo.setOperationType(operationType);
        versionPo.setOrgVersion(orgVesionRecent);
        contactMgrService.updatePO(versionPo, false);
        CacheWxqyhObject.set("addressBook", userInfoPo.getOrgId(), "orgVesionRecent", orgVesionRecent);
    }

    /**
     * 批量修改用户的版本状态
     * @param idType id类型
     * @param operationType 操作类型
     * @param orgVesionRecent 机构最新版本
     * @param orgId 机构Id
     * @param users 用户列表
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
    private void batchOrgUserVersion(String idType, String operationType, Integer orgVesionRecent, String orgId, List<TbQyUserInfoPO> users)throws BaseException, Exception{
        //批量的用户
        List<String> ids = new ArrayList<String>();
        if(null != users && users.size()>0) {
            for (int i = 0; i < users.size(); i++) {
                ids.add(users.get(i).getUserId());
            }
        }
        batchVersionId(idType, operationType, orgVesionRecent, orgId, ids);
    }

    /**
     * 批量查询机构版本的Id
     * @param orgId 机构id
     * @param ids 用户列表
     * @return
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-12
     * @version 1.0
     */
    private List<String> batchIdVersion(String orgId, List<String> ids) throws  BaseException, Exception{
        int count = contactMgrService.getVersionOrgCount(orgId);
        int start = 0;
        int end = 1000;
        int  pageCount = count%1000 == 0 ? count/1000 :count/1000+1;
        //机构版本表中该机构的id
        List<String> versionIds = new ArrayList<String>(count);
        List<String> haveIds = new ArrayList<String>(ids.size());
        //如果数量在1000以下
        if(count <= 1000){
            versionIds = contactMgrService.getVersionId(orgId, start, count);
        } else {
            //如果页数大于0
            while (pageCount > 0) {
                versionIds.addAll(contactMgrService.getVersionId(orgId, start, end));
                start = end;
                end = end + 1000;
                pageCount --;
            }
        }
        //如果这次修改的，有在机构版本表中，就放入到haveids里面去
        for(int i = 0; i < ids.size(); i++){
            if(versionIds.contains(ids.get(i))){
                haveIds.add(ids.get(i));
            }
        }
        return haveIds;
    }

    /**
     * 批量修改用户的版本状态
     * @param idType id类型
     * @param operationType 操作类型
     * @param orgVesionRecent 机构最新版本
     * @param orgId 机构Id
     * @param ids id列表
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    private void batchVersionId(String idType, String operationType, Integer orgVesionRecent, String orgId, List<String> ids) throws BaseException, Exception {
        //批量数量大于0
        if(ids.size()>0) {
            //机构版本控制表中有的id
            List<String> haveIds = batchIdVersion(orgId, ids);
            if (null != haveIds && haveIds.size() > 0) {
                //移除掉机构版本表中有的id
                ids.removeAll(haveIds);
                contactMgrService.batchUpdateVersion(operationType, orgVesionRecent, orgId, haveIds);
                if(ids.size()>0){
                    contactMgrService.batchAddVersion(idType, operationType, orgVesionRecent, orgId, ids);
                }
            }
            //如果全部的id都没有在机构版本控制表中
            else {
                contactMgrService.batchAddVersion(idType, operationType, orgVesionRecent, orgId, ids);
            }
        }
    }

    /**
     * 批量修改部门的版本状态
     * @param idType id类型
     * @param operationType 操作类型
     * @param orgVesionRecent 机构最新版本
     * @param orgId 机构Id
     * @param deList 部门列表
     * @throws BaseException 异常抛出
     * @throws Exception 异常抛出
     * @author liyixin
     * @2016-10-13
     * @version 1.0
     */
    private void batchOrgDeptVersion(String idType, String operationType, Integer orgVesionRecent, String orgId, List<DeptSyncInfoVO> deList)throws BaseException, Exception{
        //批量的部门
        List<String> ids = new ArrayList<String>();
        if(null != deList && deList.size()>0) {
            for (int i = 0; i < deList.size(); i++) {
                ids.add(deList.get(i).getId());
            }
        }
        batchVersionId(idType, operationType, orgVesionRecent, orgId, ids);
    }
}
