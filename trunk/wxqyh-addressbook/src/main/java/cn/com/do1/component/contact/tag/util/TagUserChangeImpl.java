package cn.com.do1.component.contact.tag.util;

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.HandoverMatterVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoExtVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.qwinterface.addressbook.IUserInfoChangeInform;
import cn.com.do1.component.util.WxqyhStringUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;

import java.util.List;
import java.util.Map;

/**
 * 通讯录变更后标签的处理方式
 * Created by sunqinghai on 17/3/17.
 */
@ProcesserUnit(name = "tagUserChangeImpl")
public class TagUserChangeImpl implements IUserInfoChangeInform {
    private static ITagMgrService tagService = DqdpAppContext.getSpringContext().getBean("tagService", ITagMgrService.class);
    /**
     * 新增用户信息
     *
     * @param user          用户po
     * @param userInfoPO
     * @param deptId        用户部门id
     * @param userInfoExtVO 用户扩展信息
     * @param type          操作类型（新增用户、导入、同步等）    @author Sun Qinghai
     * @2015-9-22
     * @version 1.0
     */
    @Override
    public void addUser(UserOrgVO user, TbQyUserInfoPO userInfoPO, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {

    }

    /**
     * 修改用户信息
     *
     * @param user          用户po
     * @param oldUser       修改前的用户po
     * @param oldDeptId     修改后的用户部门id
     * @param userInfoPo
     * @param deptId        用户部门id
     * @param userInfoExtVO 用户扩展信息
     * @param type          操作类型（新增用户、导入、同步等）    @author Sun Qinghai
     * @2015-9-22
     * @version 1.0
     */
    @Override
    public void updateUser(UserOrgVO user, TbQyUserInfoPO oldUser, List<String> oldDeptId, TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {

    }

    /**
     * 删除用户信息
     *
     * @param user       用户po
     * @param userInfoPo
     * @param deptId     用户部门id
     * @param type       操作类型（新增用户、导入、同步等）   @author Sun Qinghai
     * @2015-9-22
     * @version 1.0
     */
    @Override
    public void delUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type) {
        String[] userIds = {userInfoPo.getUserId()};
        delAllTagRef(user.getOrgId(), userIds, TagStaticUtil.TAG_REF_TYPE_USER);
    }

    /**
     * 离职用户信息
     *
     * @param user       用户po
     * @param userInfoPo
     * @param deptId     用户部门id
     * @param type       操作类型（新增用户、导入、同步等）   @author Sun Qinghai
     * @param matterMap 离职用户需要交接的事项 key是agentCode,value是需要交接的事项列表
     * @param matterMap 离职用户需要交接的事项 key是agentCode,value是需要交接的事项列表
     * @2015-9-22
     * @version 1.0
     */
    @Override
    public void leaveUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, int type, Map<String, List<HandoverMatterVO>> matterMap) {
        String[] userIds = {userInfoPo.getUserId()};
        delAllTagRef(user.getOrgId(), userIds, TagStaticUtil.TAG_REF_TYPE_USER);
    }

    /**
     * 批量离职
     *
     * @param user
     * @param users
     * @param type
     * @return
     * @author Sun Qinghai
     * @ 16-4-13
     */
    @Override
    public void batchLeaveUser(UserOrgVO user, List<TbQyUserInfoPO> users, int type) {
        String[] userIds = new String[users.size()];
        for (int i = 0; i<users.size(); i++) {
            userIds[i] = users.get(i).getUserId();
        }
        delAllTagRef(user.getOrgId(), userIds, TagStaticUtil.TAG_REF_TYPE_USER);
    }

    /**
     * 复职用户信息
     *
     * @param user          用户po
     * @param userInfoPo
     * @param deptId        用户部门id
     * @param userInfoExtVO 用户扩展信息
     * @param type          操作类型（新增用户、导入、同步等）    @author Sun Qinghai
     * @2015-9-22
     * @version 1.0
     */
    @Override
    public void recoverUser(UserOrgVO user, TbQyUserInfoPO userInfoPo, List<String> deptId, UserInfoExtVO userInfoExtVO, int type) {

    }

    /**
     * 批量操作完成提醒
     *
     * @param user
     * @param addList
     * @param updateList
     * @param delList           离职用户列表
     * @param userRefDeptMap    用户部门管理数据，离职用户的关联数据不在此列
     * @param addDeptRefUserMap 更新的这批人中需要新增的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
     * @param delDeptRefUserMap 更新的这批人中需要删除的部门人员关联关系（如果人员的原部门和现有部门相同，此对象为null）
     * @param type              操作来源类型  @author Sun Qinghai
     * @2015-9-24
     * @version 1.0
     */
    @Override
    public void batchChanged(UserOrgVO user, List<TbQyUserInfoPO> addList, List<TbQyUserInfoPO> updateList, List<TbQyUserInfoPO> delList, Map<String, List<String>> userRefDeptMap, Map<String, List<String>> addDeptRefUserMap, Map<String, List<String>> delDeptRefUserMap, int type) {
        if (!AssertUtil.isEmpty(delList)) {
            String[] userIds = new String[delList.size()];
            for (int i = 0; i < delList.size(); i++) {
                userIds[i] = delList.get(i).getUserId();
            }
            delAllTagRef(user.getOrgId(), userIds, TagStaticUtil.TAG_REF_TYPE_USER);
        }
    }

    /**
     * 批量删除结束提醒
     *
     * @param user
     * @param userIds
     * @param type    @author Sun Qinghai
     * @2015-9-24
     * @version 1.0
     */
    @Override
    public void batchDelEnd(UserOrgVO user, String[] userIds, int type) {
        delAllTagRef(user.getOrgId(), userIds, TagStaticUtil.TAG_REF_TYPE_USER);
    }

    /**
     * 批量移动部门结束提醒，清掉原有部门，只保留移入的部门
     *
     * @param user
     * @param userIds
     * @param deptId  @author Sun Qinghai
     * @2015-9-24
     * @version 1.0
     */
    @Override
    public void batchMoveEnd(UserOrgVO user, List<String> userIds, List<String> deptId) {

    }

    /**
     * 批量从特定部门移出用户
     *
     * @param user
     * @param userIds
     * @param deptId
     * @return
     * @author Sun Qinghai
     * @ 16-4-13
     */
    @Override
    public void batchShiftOutUser(UserOrgVO user, List<String> userIds, List<String> deptId) {

    }

    /**
     * 批量移入到特定部门用户，保留用户的原有部门
     *
     * @param user
     * @param userIds
     * @param deptId
     * @return
     * @author Sun Qinghai
     * @ 16-4-13
     */
    @Override
    public void batchShiftIntUser(UserOrgVO user, List<String> userIds, List<String> deptId) {

    }

    /**
     * 更新用户状态
     *
     * @param userPO
     * @param type
     * @return
     * @author Sun Qinghai
     * @ 16-5-3
     */
    @Override
    public void updateUserStatusAndHeadPic(TbQyUserInfoPO userPO, int type) {

    }

    /**
     * 批量操作部门信息
     *
     * @param user       操作人
     * @param addList    新增部门list
     * @param updateList 更新的部门list，当更新了部门名称，父部门，微信部门id，微信父部门id时才会加入到更新部门list中
     * @param delList    删除的部门list
     * @param type
     * @author sunqinghai
     * @date 2016 -7-5
     */
    @Override
    public void batchChangeDept(UserOrgVO user, List<DeptSyncInfoVO> addList, List<DeptSyncInfoVO> updateList, List<DeptSyncInfoVO> delList, int type) {
        if (!AssertUtil.isEmpty(delList)) {
            String[] deptIds = new String[delList.size()];
            for (int i = 0; i < delList.size(); i++) {
                deptIds[i] = delList.get(i).getId();
            }
            delAllTagRef(user.getOrgId(), deptIds, TagStaticUtil.TAG_REF_TYPE_DEPT);
        }
    }

    /**
     * 删除所有标签相应的关联成员信息
     * @param orgId 机构id
     * @param menberIds 成员id
     * @param menberType 成员类型，来源TagStaticUtil.TAG_REF_TYPE_xxx
     * @author sunqinghai
     * @date 2017 -3-17
     */
    private void delAllTagRef(String orgId, String[] menberIds, int menberType) {
        try {
            tagService.delAllTagRef(orgId, menberIds, menberType);
        } catch (Exception e) {
            ExceptionCenter.addException(e, "addressbook TagUserChangeImpl delAllTagRef @sqh " + orgId, WxqyhStringUtil.toString(menberIds) + "===" + menberType);
        } catch (BaseException e) {
            ExceptionCenter.addException(e, "addressbook TagUserChangeImpl delAllTagRef @sqh " + orgId, WxqyhStringUtil.toString(menberIds) + "===" + menberType);
        }
    }
}
