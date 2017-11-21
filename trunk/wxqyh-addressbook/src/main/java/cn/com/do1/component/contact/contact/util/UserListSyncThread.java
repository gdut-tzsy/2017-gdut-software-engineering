/*
 * Copyright  2014 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.vo.UpdateUserResult;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.WxqyhStringUtil;
import net.sf.json.JSONObject;

import org.apache.commons.collections.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.DataConfictException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoSyncErrorPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserSyncPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.tag.thread.TagThread;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhAuthUtil;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptTree;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 自动接入企业微信同步用户成员
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 *
 * @author Chen Feixiong
 * @version 1.0
 *          修订历史：
 *          日期          作者        参考         描述
 * @since 2014-10-29
 */

public class UserListSyncThread extends Thread {
    private final static transient Logger logger = LoggerFactory.getLogger(UserListSyncThread.class);

    private String corpId;
    private String org_id;
    //private boolean isUpdateDepartment;
    private String createPerson;
    private String syncPOId;   //用于更新用户每天同步通讯录的状态
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    private TbDepartmentInfoPO tbDepartmentInfoPO;
    private TbQyUserInfoPO tbQyUserInfoPO;
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    //微信上的用户部门id，对应的本地创建的部门信息
    private Map<String, TbDepartmentInfoPO> map = new HashMap<String, TbDepartmentInfoPO>(20);
    private Map<String, TbDepartmentInfoPO> delDept = new HashMap<String, TbDepartmentInfoPO>(20);//待删除的部门
    private Set<String> updateDept = new HashSet<String>(20);//所有更新的部门部门
    private List<TbQyUserInfoPO> addUserList = new ArrayList<TbQyUserInfoPO>(100);
    private List<TbQyUserInfoPO> updateUserList = new ArrayList<TbQyUserInfoPO>(100);
    private List<TbQyUserInfoPO> leavUserList = new ArrayList<TbQyUserInfoPO>(100);
    private Map<String, List<String>> userRefDeptMap = new HashMap<String, List<String>>(100);
    private Map<String, List<String>> addDeptRefUserMap = null;
    private Map<String, List<String>> delDeptRefUserMap = null;
    private List<DeptSyncInfoVO> addDeptList = null;//需要新增的部门list
    private List<DeptSyncInfoVO> updateDeptList = null;//需要编辑的部门list
    private List<DeptSyncInfoVO> delDeptList = null;//需要删除的部门list
    private UserOrgVO org;

    public UserListSyncThread(String corpId, String org_id, String createPerson, boolean isUpdateDepartment, String syncPOId) {
        this.corpId = corpId;
        this.org_id = org_id;
        //this.isUpdateDepartment = isUpdateDepartment;
        this.createPerson = createPerson;
        this.syncPOId = syncPOId;
        if (!StringUtil.isNullEmpty(createPerson)) {
            try {
                org = contactService.getOrgByUserId(createPerson);
            } catch (Exception e) {
                logger.error("UserListSyncThread getOrgByUserId " + createPerson, e);
            } catch (BaseException e) {
                logger.error("UserListSyncThread getOrgByUserId " + createPerson, e);
            }
        }
        if (org == null) {
            org = new UserOrgVO();
            org.setOrgId(org_id);
            org.setCorpId(corpId);
        }
    }

    public void run() {
        logger.debug("同步用户开始，corpId：" + corpId + ",org_id:" + org_id + ",userId" + createPerson);
        //TbDqdpOrganizationInsertPO insertPO = null;
        try {
            if (corpId != null) {
                if (corpId.equals(Configuration.AUTO_CORPID)) {
                    logger.error("同步用户失败,机构为体验用户--，org_id：" + org_id + "corpId：" + corpId);
                    return;
                }
                //删除历史同步的错误信息
                contactService.deletErrorUser(org_id);
                ExtOrgPO OrgPO = contactService.searchByPk(ExtOrgPO.class, org_id);
                if (StringUtil.isNullEmpty(OrgPO.getWxId()) || StringUtil.isNullEmpty(OrgPO.getParentId())) {
                    addErrorUser(new WxUser(), "机构信息中微信部门id出现异常，请联系企微管理员");
                    return;
                }
                if (!OrgPO.getCorpId().equals(corpId)) {
                    addErrorUser(new WxUser(), "机构信息中corpId出现异常，请联系企微管理员");
                    return;
                }
                String orgWxId = OrgPO.getWxId();
                /**
                 * 同步部门
                 */
                /*if(isUpdateDepartment){
					insertPO=experienceapplicationService.getOrgInsert(org_id);
					if(insertPO==null){
						return;
					}
				}*/
                try {
                    if (Configuration.IS_QIWEIYUN) {
                        if (!WxAgentUtil.isTrustAgent(corpId, WxAgentUtil.getAddressBookCode())) {
                            addErrorUser(new WxUser(), "还未托管通讯录应用，请先托管通讯录应用");
                            return;
                        } else if (!WxAgentUtil.isAllUserUsable(corpId, WxAgentUtil.getAddressBookCode())) {
                            addErrorUser(new WxUser(), "获取token失败，如何解决：在企业微信后台->【应用中心】->【授权的应用】中找到【通讯录】应用->修改【通讯录范围】为最顶级部门，修改后请耐心等待两分钟后重试。");
                            return;
                        }
                    }
                } catch (Exception e) {
                    logger.error("默默同步通讯录isMangerPermission验证机构信息失败", e);
                    addErrorUser(new WxUser(), "验证机构信息失败，" + e.getMessage());
                    return;
                }
                TbDepartmentInfoPO rootDepartment = new TbDepartmentInfoPO();
                rootDepartment.setId("");
                rootDepartment.setWxId(OrgPO.getWxId());
                rootDepartment.setWxParentid(OrgPO.getWxParentid());
                //机构不是对应微信的根节点
                boolean isNotRootOrg = !"1".equals(OrgPO.getWxId());
                WxDeptTree deptTree;
                //如果机构对应的不是根节点
                if (isNotRootOrg) {
                    deptTree = WxDeptService.getDeptTree(corpId, OrgPO.getWxId(), org_id, WxAgentUtil.getAddressBookCode());
                    //子部门为空时，获取根节点的数据
                    if (deptTree == null || deptTree.getChildrenDept().size() == 0) {
                        //将机构对应到微信根节点
                        rootDepartment.setWxId("1");//将机构id设置为根节点
                        rootDepartment.setWxParentid("0");
                        if (deptTree != null) {//如果机构对应的微信上的部门存在
                            //删除微信上的此部门
                            try {
                                WxDeptService.delDept(OrgPO.getWxId(), corpId, org_id);
                            } catch (Exception e) {
                                logger.error("同步联系人出错：删除机构非根节点失败", e);
                            } catch (BaseException e) {
                                logger.error("同步联系人出错：删除机构非根节点失败", e);
                            }
                        }
                        //获取一级部门，将其微信id的父id设为“1”
                        List<TbDepartmentInfoVO> firstDepartList = departmentService.getFirstDepart(org_id);
                        if (firstDepartList != null && firstDepartList.size() > 0) {
                            TbDepartmentInfoPO po = new TbDepartmentInfoPO();
                            for (TbDepartmentInfoVO vo : firstDepartList) {
                                po.setId(vo.getId());
                                po.setWxParentid("1");
                                departmentService.updatePO(po, false);
                            }
                        }
                        ExtOrgPO org = new ExtOrgPO();
                        org.setOrganizationId(OrgPO.getOrganizationId());
                        org.setWxId("1");
                        org.setWxParentid("0");
                        departmentService.updatePO(org, false);
                        OrgPO.setWxId("1");
                        OrgPO.setWxParentid("0");
                        isNotRootOrg = false;
                        orgWxId = "1";

                        deptTree = WxDeptService.getDeptTree(corpId, "1", org_id, WxAgentUtil.getAddressBookCode());
                    }
                } else {
                    deptTree = WxDeptService.getDeptTree(corpId, "1", org_id, WxAgentUtil.getAddressBookCode());
                }
                //加入待删除的本地部门
                List<TbDepartmentInfoPO> depts = departmentService.getAllDepartForSync(org_id);
                if (depts != null && depts.size() > 0) {
                    for (TbDepartmentInfoPO tbDepartmentInfoPO : depts) {
                        delDept.put(tbDepartmentInfoPO.getId(), tbDepartmentInfoPO);
                    }
                }
                depts = null;

                if (deptTree != null && deptTree.getChildrenDept() != null && deptTree.getChildrenDept().size() > 0) {
                    logger.debug("同步部门从微信后台corpId：" + corpId);
                    //1、同步部门,如果机构id为1，证明根节点即为机构节点，因此不需要移动和删除微信上的部门
                    addDepartByWeixin(deptTree, rootDepartment);
                }
                deptTree = null;

                logger.debug("同步人员从微信后台corpId：" + corpId);
                addDeptRefUserMap = new HashMap<String, List<String>>(100);
                delDeptRefUserMap = new HashMap<String, List<String>>(100);
                // 2、同步人员
                List<WxUser> list = WxUserService.getUsers(corpId, rootDepartment.getWxId(), org_id, WxAgentUtil.getAddressBookCode());
                if (!Configuration.IS_QIWEIYUN && WxqyhAuthUtil.getAuthUserCount() < list.size()) {
                    //setActionResult("1022", "企微通信录人数已满，请联系企微管理员!");
                    logger.error("同步人员从微信后台corpId：" + corpId + "，企微通信录人数已满，请联系企微管理员，size=" + list.size() + ",count=" + WxqyhAuthUtil.getAuthUserCount());
                    addErrorUser(new WxUser(), "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
                    return;
                }
                //获取本地机构的所有用户id（待删除列表）
                List<String> orguser = contactService.getUserIdsByOrg(org_id);
                TreeList userDelList;//使用TreeList，在remove的时候效率会高一些
                if (orguser != null) {//防止为空，remove时报错
                    userDelList = new TreeList(orguser);
                } else {
                    userDelList = new TreeList();
                }
                orguser = null;
                //添加部门：企业微信同步用户
                if (list != null && list.size() > 0) {
                    //未知部门用户
                    List<WxUser> undefinedDeptUser = new ArrayList<WxUser>();
                    List<String> detId;
                    for (WxUser user : list) {
                        try {
                            //获取用户的部门
                            //rootDepartment = map.get(user.getDepartment().get(0));
                            rootDepartment = null;
                            detId = new ArrayList<String>();
                            // 处理部门，不存在时自动创建
                            if (user.getDepartment() != null && user.getDepartment().size() > 0) {
                                int size = user.getDepartment().size();
                                // 处理部门，不存在的部门自动新建
                                for (int i = 0; i < size; i++) {
                                    //如果有多部门，并且该用户属于根节点，跳过根节点的部门信息不保存
                                    if (size > 1 && orgWxId.equals(user.getDepartment().get(i).toString())) {
                                        continue;
                                    }
                                    rootDepartment = map.get(user.getDepartment().get(i).toString());
                                    if (rootDepartment == null) {
                                        break;//跳出循环
                                    }
                                    detId.add(rootDepartment.getId());
                                }
                                if (rootDepartment == null) {
                                    //addErrorUser(user, "联系人的所在部门未找到，请手动在企业微信通讯录中创建相应的部门，并将此用户从【未分配部门人员】移动到相关部门");
                                    logger.debug("同步联系人的所在部门不存在corpId：" + corpId + "," + user.toString());
                                    undefinedDeptUser.add(user);
                                    continue;
                                }
                            } else {
                                //addErrorUser(user, "用户没有归属任何部门，请手动在企业微信通讯录中创建相应的部门，并将此用户从【未分配部门人员】移动到相关部门");
                                logger.debug("同步联系人没有归属任何部门corpId：" + corpId + "," + user.toString());
                                undefinedDeptUser.add(user);
                                continue;
                            }
                            TbQyUserInfoVO vo = contactService.findUserInfoOneByWxUserId(user.getUserid(), corpId);
                            if (vo != null) {
                                //找到用户，从待删除列表去掉
                                userDelList.remove(vo.getId());
                                //如果机构id不同
                                if (!org_id.equals(vo.getOrgId())) {
                                    logger.error("同步联系人出错,用户的机构id不同：" + user.toString());
                                    addErrorUser(user, "用户账号已在该企业微信的其它机构中存在");
                                    continue;
                                }
                                boolean[] isChange = WxUserService.verifyChange(vo, user);
                                List<String> oldDeptIds = null;
                                //判断是否需要更新，如果没有修改
                                if (!isChange[0]) {
                                    oldDeptIds = new ArrayList<String>(10);
                                    if(!DepartmentUtil.isChangeDept(vo.getOrgId(), vo.getUserId(),user.getDepartment(), oldDeptIds)){
                                        continue;
                                    }
                                }
                                tbQyUserInfoPO = new TbQyUserInfoPO();
                                tbQyUserInfoPO.setId(vo.getId());
                                if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {//离职
                                    tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
                                }
                                ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, vo);
                                tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                                tbQyUserInfoPO.setOrgId(org_id);
                                tbQyUserInfoPO.setUserId(vo.getUserId());

								/*//同步联系人到微信后台
								WxUserService.updateUser(user,corpId);*/
                                //更新本地用户信息
                                UpdateUserResult uur = contactService.updateUser(tbQyUserInfoPO, detId, false);
                                updateUserList.add(tbQyUserInfoPO);
                                userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                                if (uur.isUpdateDept()) {//更新了部门信息
                                    UserInfoChangeNotifier.getDeptUserRef(uur.getOldDeptIds(), detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap, delDeptRefUserMap);
                                }
                                //修改缓存中的人员数据
                                CacheSessionManager.update(tbQyUserInfoPO.getUserId());
                                //记录用户更新的数据，以便更新冗余字段 chenfeixiong 2015/06/30
                                if (isChange[1]) {
                                    try {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("creator", "admin");
                                        map.put("userId", tbQyUserInfoPO.getUserId());
                                        map.put("orgId", tbQyUserInfoPO.getOrgId());
                                        map.put("item1", tbQyUserInfoPO.getPersonName());
                                        map.put("item2", tbQyUserInfoPO.getHeadPic());
                                        map.put("item3", tbQyUserInfoPO.getWxUserId());
                                        map.put("is_manager", "0");
                                        contactService.addUdpatePersonInfo(map);
                                    } catch (Exception e) {
                                        logger.debug("更新冗余字段", e);
                                    } catch (BaseException e) {
                                        logger.debug("更新冗余字段", e);
                                    }
                                }
                                continue;
                            } else {
                                tbQyUserInfoPO = new TbQyUserInfoPO();
                                tbQyUserInfoPO.setId(ContactUtil.getUserId(corpId, user.getUserid()));
                                ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user);
                                tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
                                tbQyUserInfoPO.setCorpId(corpId);
                                tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                                tbQyUserInfoPO.setCreateTime(new Date());
                                tbQyUserInfoPO.setOrgId(org_id);
                                tbQyUserInfoPO.setCreatePerson(createPerson);
                                tbQyUserInfoPO.setIsConcerned("0");
								/*//同步联系人到微信后台
								WxUserService.updateUser(user,corpId);
								logger.debug("同步联系人到微信后台corpId："+corpId+","+user.toString());*/
                                contactService.insertUser(tbQyUserInfoPO, detId);
                                addUserList.add(tbQyUserInfoPO);
                                userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                                UserInfoChangeNotifier.getDeptUserRef(detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap);
                                logger.debug("添加联系人corpId：" + corpId + "," + user.toString());
                            }
                        } catch (Exception e) {
                            logger.error("同步联系人出错：" + user.toString(), e);
                            addErrorUser(user, e.getMessage());
                        } catch (BaseException e) {
                            logger.error("同步联系人出错：" + user.toString(), e);
                            addErrorUser(user, e.getMessage());
                        }
                    }
                    if (undefinedDeptUser.size() > 0) {
                        detId = new ArrayList<String>();
                        //添加部门：未分配部门人员
                        rootDepartment = getDefaultDept(org_id,orgWxId,corpId);
                        detId.add(rootDepartment.getId());
                        if (rootDepartment.getWxId() != null && !rootDepartment.getWxId().isEmpty()) {
                            //同步部门到微信
                            for (WxUser user : undefinedDeptUser) {
                                try {
                                    TbQyUserInfoVO vo = contactService.findUserInfoOneByWxUserId(user.getUserid(), corpId);
                                    if (vo != null) {
                                        //找到用户，从待删除列表去掉
                                        userDelList.remove(vo.getId());
                                        //如果机构id不同
                                        if (!org_id.equals(vo.getOrgId())) {
                                            logger.error("同步联系人出错,用户的机构id不同：" + user.toString());
                                            addErrorUser(user, "用户账号已在该企业微信的其它机构中存在");
                                            continue;
                                        }
                                        tbQyUserInfoPO = new TbQyUserInfoPO();
                                        tbQyUserInfoPO.setId(vo.getId());

                                        if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {//离职
                                            tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
                                        }
                                        ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, vo);
                                        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                                        tbQyUserInfoPO.setOrgId(org_id);
                                        tbQyUserInfoPO.setUserId(vo.getUserId());

										/*//同步联系人到微信后台
										List<String> d = new ArrayList<String>(1);
										d.add(rootDepartment.getWxId());
										user.setDepartment(d);
										WxUserService.updateUser(user,corpId);*/
                                        //更新本地用户信息
                                        UpdateUserResult uur = contactService.updateUser(tbQyUserInfoPO, detId, false);
                                        updateUserList.add(tbQyUserInfoPO);
                                        userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                                        if (uur.isUpdateDept()) {//更新了部门信息
                                            UserInfoChangeNotifier.getDeptUserRef(uur.getOldDeptIds(), detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap, delDeptRefUserMap);
                                        }
                                        //修改缓存中的人员数据
                                        CacheSessionManager.update(tbQyUserInfoPO.getUserId());
                                        addErrorUser(user, "用户没有归属任何部门，请手动在企业微信通讯录中创建相应的部门，并将此用户从【未分配部门人员】移动到相关部门");
                                        continue;
                                    } else {
                                        tbQyUserInfoPO = new TbQyUserInfoPO();
                                        tbQyUserInfoPO.setId(ContactUtil.getUserId(corpId, user.getUserid()));
                                        ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user);
                                        tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
                                        tbQyUserInfoPO.setCorpId(corpId);
                                        tbQyUserInfoPO.setDeptId(rootDepartment.getId());
                                        //tbQyUserInfoPO.setSex(user.getGender());
                                        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                                        tbQyUserInfoPO.setCreateTime(new Date());
                                        tbQyUserInfoPO.setOrgId(org_id);
                                        tbQyUserInfoPO.setCreatePerson(createPerson);
                                        tbQyUserInfoPO.setIsConcerned("0");
										/*//同步联系人到微信后台
										List<String> d = new ArrayList<String>(1);
										d.add(rootDepartment.getWxId());
										user.setDepartment(d);
										WxUserService.updateUser(user,corpId);
										logger.debug("同步联系人到微信后台corpId："+corpId+","+user.toString());*/
                                        contactService.insertUser(tbQyUserInfoPO, detId);
                                        addUserList.add(tbQyUserInfoPO);
                                        userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                                        UserInfoChangeNotifier.getDeptUserRef(detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap);
                                        addErrorUser(user, "用户没有归属任何部门，请手动在企业微信通讯录中创建相应的部门，并将此用户从【未分配部门人员】移动到相关部门");
                                        //logger.debug("添加联系人corpId："+corpId+","+user.toString());
                                    }
                                } catch (Exception e) {
                                    logger.error("同步联系人到默认部门，corpId：" + corpId + "," + user.toString(), e);
                                    addErrorUser(user, e.getMessage());
                                } catch (BaseException e) {
                                    logger.error("同步联系人到默认部门，corpId：" + corpId + "," + createPerson.toString(), e);
                                    addErrorUser(user, e.getMessage());
                                }
                            }
                        }
                    }
                }
                /**
                 * 同步完成后，将待删除的用户设置为离职
                 */
                if (userDelList.size() > 0) {
                    WxUser user;
                    tbQyUserInfoPO = null;
                    for (Object userId : userDelList) {
                        try {
                            tbQyUserInfoPO = contactService.searchByPk(TbQyUserInfoPO.class, userId.toString());
                            if (tbQyUserInfoPO == null) {
                                continue;
                            }
                            if ("-1".equals(tbQyUserInfoPO.getUserStatus())) {//离职
                                continue;
                            } else {
								/*user = WxUserService.getUser(tbQyUserInfoPO.getWxUserId(), corpId,org_id, WxAgentUtil.getAddressBookCode());
								if(user==null){
									contactService.leaveUser(tbQyUserInfoPO, null, false, null);
								}*/
                                tbQyUserInfoPO.setLeaveTime(new Date());
                                tbQyUserInfoPO.setLeaveRemark("微信通讯录人员中已删除");
                                List<String> deptIds = contactService.leaveUser(tbQyUserInfoPO, null, false, null);
                                leavUserList.add(tbQyUserInfoPO);
                                if (deptIds != null) {
                                    UserInfoChangeNotifier.getDeptUserRef(deptIds, tbQyUserInfoPO.getUserId(), delDeptRefUserMap);
                                }
                            }
                            //历史中直接删除用户数据
							/*user = WxUserService.getUser(tbQyUserInfoPO.getWxUserId(), corpId);
							if(user==null){
								contactService.delPO(tbQyUserInfoPO);
								logger.warn("同步删除该用户，corpId："+corpId+",orgId"+org_id+","+tbQyUserInfoPO.getUserName()+"|"+tbQyUserInfoPO.getMobile()+"|"+tbQyUserInfoPO.getUserId()+"|"+tbQyUserInfoPO.getWxUserId());
								//删除用户部门关联关系
								contactService.deleteUserDeptRef(org_id, tbQyUserInfoPO.getUserId());
								//删除个人网页版账号
								TbQyUserLoginInfoPO loginInfoPO = contactService.getUserLoginByAccount(tbQyUserInfoPO.getUserId());
								if(loginInfoPO!=null){
									contactService.delPO(loginInfoPO);
								}
							}*/
                        } catch (Exception e) {
                            logger.error("删除该用户失败，corpId：" + corpId + "," + userId.toString(), e);
                            if (tbQyUserInfoPO != null) {
                                user = new WxUser();
                                user.setUserid(tbQyUserInfoPO.getWxUserId());
                                user.setName(tbQyUserInfoPO.getPersonName());
                                user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
                                user.setMobile(tbQyUserInfoPO.getMobile());
                                user.setEmail(tbQyUserInfoPO.getEmail());
                                addErrorUser(user, "删除该用户失败，失败原因：" + e.getMessage());
                            }
                        } catch (BaseException e) {
                            logger.error("删除该用户失败，corpId：" + corpId + "," + userId.toString(), e);
                            if (tbQyUserInfoPO != null) {
                                user = new WxUser();
                                user.setUserid(tbQyUserInfoPO.getWxUserId());
                                user.setName(tbQyUserInfoPO.getPersonName());
                                user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
                                user.setMobile(tbQyUserInfoPO.getMobile());
                                user.setEmail(tbQyUserInfoPO.getEmail());
                                addErrorUser(user, "删除该用户失败，失败原因：" + e.getMessage());
                            }
                        }
                    }
                }

                /**
                 * 删除待删除的部门信息
                 */
                if (delDept.size() > 0) {
                    Iterator<Entry<String, TbDepartmentInfoPO>> it = delDept.entrySet().iterator();
                    Entry<String, TbDepartmentInfoPO> entry;
                    WxUser user;
                    tbDepartmentInfoPO = null;
                    String fullName = null;
                    String key = null;
                    while (it.hasNext()) {
                        try {
                            entry = it.next();
                            key = entry.getKey();
                            tbDepartmentInfoPO = entry.getValue();
                            fullName = tbDepartmentInfoPO.getDeptFullName();
                            //此部门下的人员，如果有人员，不能删除
                            if (contactService.hasUsersByDepartIdAndFullName(org_id, tbDepartmentInfoPO.getId())) {
                                user = new WxUser();
                                addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：部门下存在用户，请手动删除或移动用户后再删除该部门");
                                continue;
                            }
                            departmentService.delPO(tbDepartmentInfoPO);
                            if (delDeptList == null) {
                                delDeptList = new ArrayList<DeptSyncInfoVO>(delDept.size());
                            }
                            DeptSyncInfoVO vo = new DeptSyncInfoVO();
                            BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                            delDeptList.add(vo);
                            logger.warn("同步删除部门，corpId：" + corpId + ",orgId" + org_id + "," + tbDepartmentInfoPO.getDeptFullName() + "|" + tbDepartmentInfoPO.getId());
                        } catch (Exception e) {
                            logger.error("删除部门失败，corpId：" + corpId + "," + key + "," + fullName, e);
                            user = new WxUser();
                            addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：" + e.getMessage());
                        } catch (BaseException e) {
                            logger.error("删除该用户失败，corpId：" + corpId + "," + key + "," + fullName, e);
                            user = new WxUser();
                            addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("同步用户失败--，corpId：" + corpId, e);
            WxUser user = new WxUser();
            addErrorUser(user, e.getMessage());
        } catch (BaseException ex) {
            logger.error("同步用户失败--，corpId：" + corpId, ex);
            WxUser user = new WxUser();
            addErrorUser(user, ex.getMessage());
        } finally {
            if (!StringUtil.isNullEmpty(syncPOId)) {
                //更新该条同步通讯录已经完成状态
                TbQyUserSyncPO syncPO = new TbQyUserSyncPO();
                syncPO.setId(syncPOId);
                syncPO.setExt1("1");
                try {
                    contactService.updatePO(syncPO, false);
                } catch (SQLException e1) {
                    logger.error("同步用户失败,更新是否同步状态失败--，corpId：" + corpId, e1);
                }
            }

            UserInfoChangeNotifier.batchChangeDept(org, addDeptList, updateDeptList, delDeptList, UserInfoChangeInformType.SYNC_MGR);
            UserInfoChangeNotifier.syncEnd(org, addUserList, updateUserList, leavUserList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap);

            //将用户的同步状态改为未同步
			/*if(isUpdateDepartment && insertPO!=null){
				//更新组织为未同步过通讯录
				TbDqdpOrganizationInsertPO inPO = new TbDqdpOrganizationInsertPO();
				inPO.setId(insertPO.getId());
				inPO.setIs_sync_user(0);
				try {
					contactService.updatePO(inPO, false);
				} catch (SQLException e1) {
					logger.error("同步用户失败,更新是否同步状态失败--，corpId："+corpId,e1);
				}
			}*/
            logger.debug("同步用户结束，corpId：" + corpId);
        }
    }

    private boolean isChangeDeptId(List<String> dep,TbQyUserInfoVO vo) throws Exception, BaseException {
        List<TbDepartmentInfoVO> userDept = departmentService.getDeptPermissionByUserId(vo.getOrgId(), vo.getUserId());
        if (userDept == null || userDept.size() == 0) {
            if (dep.size() == 0) {
                return false;
            }
        } else {
            if (dep.size() == userDept.size()) {
                Set<String> set = new HashSet<String>(dep);
                boolean isChangeDept = false;
                for (TbDepartmentInfoVO d : userDept) {
                    if (set.add(d.getId())) {
                        isChangeDept = true;
                        break;
                    }
                }
                if (!isChangeDept) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 同步套件用户
     * @param agentCode
     * @param updateDepts
     * @param updateUsers
     * @param updateTags
     * @param isAll 套件是否存在全部人可见的应用
     * @author Sun Qinghai
     * @2016-1-25
     * @version 1.0
     */
    public void syncDeptUserTags(String agentCode, Set<String> updateDepts, Set<String> updateUsers, Set<String> updateTags, boolean isAll) {
        logger.debug("syncDeptUserTags同步套件用户开始，corpId：" + corpId + ",org_id:" + org_id + ",userId" + createPerson);
        //TbDqdpOrganizationInsertPO insertPO = null;
        List<JSONObject> tagObjects = new ArrayList<JSONObject>();
        try {
            if (corpId != null) {
                if (corpId.equals(Configuration.AUTO_CORPID)) {
                    logger.error("syncDeptUserTags同步套件用户失败,机构为体验用户--，org_id：" + org_id + "corpId：" + corpId);
                    return;
                }
                ExtOrgPO OrgPO = contactService.searchByPk(ExtOrgPO.class, org_id);
                if (StringUtil.isNullEmpty(OrgPO.getWxId()) || StringUtil.isNullEmpty(OrgPO.getParentId())) {
                    return;
                }
                /**
                 * 将标签中的部门和人员加入到待更新列表
                 */
                if (updateTags != null && updateTags.size() > 0) {
                    for (String wxTagId : updateTags) {
                        JSONObject jsonObject = ChangeAgentUserUtil.addUserAndPartByTag(corpId, agentCode, wxTagId, updateUsers, updateDepts);
                        if(jsonObject != null){
                            tagObjects.add(jsonObject);
                        }
                    }
                }
                addDeptRefUserMap = new HashMap<String, List<String>>(100);
                delDeptRefUserMap = new HashMap<String, List<String>>(100);

                if (updateDepts != null && updateDepts.size() > 0) {
                    List<Integer> wxDeptIds = new ArrayList<Integer>();
                    for (String string : updateDepts) {
                        wxDeptIds.add(Integer.parseInt(string));//转为int型
                    }
                    Collections.sort(wxDeptIds);//排序，保证从小到大更新，小的肯定是部门范围更大的
                    for (Integer wxDeptId : wxDeptIds) {
                        try {
                            if (map.containsKey(wxDeptId.toString())) {//如果已经更新了，就不需要再更新了
                                updateDepts.remove(wxDeptId.toString());
                                continue;
                            }
                            //机构不是对应微信的根节点
                            WxDeptTree deptTree = WxDeptService.getDeptTree(corpId, String.valueOf(wxDeptId), org_id, agentCode);
                            if (deptTree == null) {
                                continue;
                            }
                            logger.debug("syncDeptUserTags同步部门从微信后台corpId：" + corpId);
                            //1、同步部门,如果机构id为1，证明根节点即为机构节点，因此不需要移动和删除微信上的部门
                            addNotOrgRootDepartByWeixin(deptTree);
                        } catch (Exception e) {
                            logger.error("syncDeptUserTags同步套件部门失败从微信后台corpId：" + corpId + ",wxDeptId=" + wxDeptId, e);
                        } catch (BaseException e) {
                            logger.error("syncDeptUserTags同步套件部门失败从微信后台corpId：" + corpId + ",wxDeptId=" + wxDeptId, e);
                        }
                    }

                    // 2、同步人员
                    //同步部门人员
                    for (String wxDeptId : updateDepts) {
                        syncUser(wxDeptId, agentCode, OrgPO.getWxId(),isAll);
                    }
                }
                //同步可见范围内的单个人员
                if (updateUsers != null && updateUsers.size() > 0) {
                    TreeList userDelList = new TreeList();//仅仅是为了防空值
                    for (String wxUserId : updateUsers) {
                        try {
                            WxUser user = WxUserService.getUser(wxUserId, corpId, org_id, agentCode);
                            if (user != null) {
                                sysUser(user, OrgPO.getWxId(), userDelList, isAll);
                            }
                        } catch (Exception e) {
                            logger.error("syncDeptUserTags同步套件人员失败从微信后台corpId：" + corpId + ",wxUserId=" + wxUserId, e);
                        } catch (BaseException e) {
                            logger.error("syncDeptUserTags同步套件人员失败从微信后台corpId：" + corpId + ",wxUserId=" + wxUserId, e);
                        }
                    }
                }

                /**
                 * 删除待删除的部门信息
                 */
                if (delDept.size() > 0) {
                    Iterator<Entry<String, TbDepartmentInfoPO>> it = delDept.entrySet().iterator();
                    Entry<String, TbDepartmentInfoPO> entry;
                    WxUser user;
                    tbDepartmentInfoPO = null;
                    String fullName = null;
                    String key = null;
                    while (it.hasNext()) {
                        try {
                            entry = (Entry<String, TbDepartmentInfoPO>) it.next();
                            key = entry.getKey();
                            tbDepartmentInfoPO = entry.getValue();
                            fullName = tbDepartmentInfoPO.getDeptFullName();
                            //此部门下的人员，如果有人员，不能删除
                            if (contactService.hasUsersByDepartIdAndFullName(org_id, tbDepartmentInfoPO.getId())) {
                                user = new WxUser();
                                addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：部门下存在用户，请手动删除或移动用户后再删除该部门");
                                continue;
                            }
                            departmentService.delPO(tbDepartmentInfoPO);
                            if (delDeptList == null) {
                                delDeptList = new ArrayList<DeptSyncInfoVO>(delDept.size());
                            }
                            DeptSyncInfoVO vo = new DeptSyncInfoVO();
                            BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                            delDeptList.add(vo);
                            logger.warn("syncDeptUserTags同步删除部门，corpId：" + corpId + ",orgId" + org_id + "," + tbDepartmentInfoPO.getDeptFullName() + "|" + tbDepartmentInfoPO.getId());
                        } catch (Exception e) {
                            logger.error("syncDeptUserTags删除部门失败，corpId：" + corpId + "," + key + "," + fullName, e);
                            user = new WxUser();
                            addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：" + e.getMessage());
                        } catch (BaseException e) {
                            logger.error("syncDeptUserTags删除该用户失败，corpId：" + corpId + "," + key + "," + fullName, e);
                            user = new WxUser();
                            addErrorUser(user, "删除部门【" + fullName + "】失败，失败原因：" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("syncDeptUserTags同步套件用户失败--，corpId：" + corpId, e);
            WxUser user = new WxUser();
            addErrorUser(user, e.getMessage());
        } finally {
            if (!StringUtil.isNullEmpty(syncPOId)) {
                //更新该条同步通讯录已经完成状态
                TbQyUserSyncPO syncPO = new TbQyUserSyncPO();
                syncPO.setId(syncPOId);
                syncPO.setExt1("1");
                try {
                    contactService.updatePO(syncPO, false);
                } catch (SQLException e1) {
                    logger.error("syncDeptUserTags同步套件用户失败,更新是否同步状态失败--，corpId：" + corpId, e1);
                }
            }
            //更新标签下的人员信息
            TagThread tt = new TagThread(corpId, org_id);
            tt.synTagInfos(org_id, tagObjects, agentCode);

            UserInfoChangeNotifier.batchChangeDept(org, addDeptList, updateDeptList, delDeptList, UserInfoChangeInformType.SYNC_MGR);
            UserInfoChangeNotifier.syncEnd(org, addUserList, updateUserList, leavUserList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap);

            logger.debug("syncDeptUserTags同步套件用户结束，corpId：" + corpId);
        }
    }

    private void syncUser(String wxDeptId, String agentCode, String orgWxId, boolean isAll) {
        logger.debug("syncUser同步套件人员从微信后台corpId：" + corpId + ",wxDeptId=" + wxDeptId + ",agentCode=" + agentCode);
        try {
            //获取本地机构的所有用户id（待删除列表）
            List<String> orguser;
            TreeList userDelList;//使用TreeList，在remove的时候效率会高一些
            if (wxDeptId.equals(orgWxId)) {
                orguser = contactService.getUserIdsByOrg(org_id);
            } else {
                orguser = contactService.findWxUserIdsByWxDeptIds(org_id, wxDeptId);
            }
            if (orguser != null) {//防止为空，remove时报错
                userDelList = new TreeList(orguser);
            } else {
                userDelList = new TreeList();
            }
            orguser = null;

            List<WxUser> list = WxUserService.getUsers(corpId, wxDeptId, org_id, agentCode);
            if (!Configuration.IS_QIWEIYUN && WxqyhAuthUtil.getAuthUserCount() < list.size()) {
                //setActionResult("1022", "企微通信录人数已满，请联系企微管理员!");
                logger.error("syncUser同步人员从微信后台corpId：" + corpId + "，企微通信录人数已满，请联系企微管理员，size=" + list.size() + ",count=" + WxqyhAuthUtil.getAuthUserCount());
                addErrorUser(new WxUser(), "企微通信录人数已满"+WxqyhAuthUtil.getAuthUserCount()+"，请联系企微管理员!");
                return;
            }
            //添加部门：企业微信同步套件用户
            if (list != null && list.size() > 0) {
                for (WxUser user : list) {
                    sysUser(user, orgWxId, userDelList,isAll);
                }
            }

            /**
             * 同步完成后，将待删除的用户设置为离职
             */
            if (userDelList.size() > 0) {
                WxUser user;
                tbQyUserInfoPO = null;
                List<String> depts = null;
                for (Object userId : userDelList) {
                    try {
                        tbQyUserInfoPO = contactService.searchByPk(TbQyUserInfoPO.class, userId.toString());
                        if (tbQyUserInfoPO == null) {
                            continue;
                        }
                        if ("-1".equals(tbQyUserInfoPO.getUserStatus())) {//离职
                            continue;
                        } else {
                            boolean isAllDel = true;//是否删除了管理的所有部门信息
                            List<TbDepartmentInfoVO> userDept = departmentService.getDeptPermissionByUserId(org_id, tbQyUserInfoPO.getUserId());
                            if (userDept != null && userDept.size() > 0) {
                                for (TbDepartmentInfoVO tbDepartmentInfoVO : userDept) {
                                    if (updateDept.contains(tbDepartmentInfoVO.getId())) {
                                        contactService.deleteUserDeptRefByUserIdDeptId(tbDepartmentInfoVO.getId(), tbQyUserInfoPO.getUserId());
                                    } else {
                                        isAllDel = false;
                                    }
                                }
                            }
                            if (isAllDel) {
                                if (depts == null) {
                                    //TbDepartmentInfoPO nullDepartment = getDefaultDept(org_id,orgWxId,corpId);
                                    depts = new ArrayList<String>(1);
                                    //depts.add(nullDepartment.getId());
                                    getWxEmptyDept(depts);
                                }
                                contactService.insertUserDeptRef(org_id, userId.toString(), depts);
                            }

                        }
                    } catch (Exception e) {
                        logger.error("syncUser删除该用户失败，corpId：" + corpId + "," + userId.toString(), e);
                        if (tbQyUserInfoPO != null) {
                            user = new WxUser();
                            user.setUserid(tbQyUserInfoPO.getWxUserId());
                            user.setName(tbQyUserInfoPO.getPersonName());
                            user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
                            user.setMobile(tbQyUserInfoPO.getMobile());
                            user.setEmail(tbQyUserInfoPO.getEmail());
                            addErrorUser(user, "删除该用户失败，失败原因：" + e.getMessage());
                        }
                    } catch (BaseException e) {
                        logger.error("syncUser删除该用户失败，corpId：" + corpId + "," + userId.toString(), e);
                        if (tbQyUserInfoPO != null) {
                            user = new WxUser();
                            user.setUserid(tbQyUserInfoPO.getWxUserId());
                            user.setName(tbQyUserInfoPO.getPersonName());
                            user.setWeixinid(tbQyUserInfoPO.getWeixinNum());
                            user.setMobile(tbQyUserInfoPO.getMobile());
                            user.setEmail(tbQyUserInfoPO.getEmail());
                            addErrorUser(user, "删除该用户失败，失败原因：" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("syncUser同步套件人员从微信后台corpId：" + corpId + ",wxDeptId=" + wxDeptId, e);
        } catch (BaseException e) {
            logger.error("syncUser同步套件人员从微信后台corpId：" + corpId + ",wxDeptId=" + wxDeptId, e);
        }
    }

    private void sysUser(WxUser user, String orgWxId, TreeList userDelList, boolean isAll) {
        try {
            //获取用户的部门
            //rootDepartment = map.get(user.getDepartment().get(0));
            TbDepartmentInfoPO department;
            List<String> detId = new ArrayList<String>();
            TbQyUserInfoVO vo = contactService.findUserInfoOneByWxUserId(user.getUserid(), corpId);
            // 处理部门，不存在时自动创建
            if (user.getDepartment() != null && user.getDepartment().size() > 0) {
                int size = user.getDepartment().size();
                // 处理部门，不存在的部门自动新建
                for (int i = 0; i < size; i++) {
                    //如果有多部门，并且该用户属于根节点，跳过根节点的部门信息不保存
                    if (size > 1 && orgWxId.equals(user.getDepartment().get(i).toString())) {
                        continue;
                    }
                    department = map.get(user.getDepartment().get(i).toString());
                    if (department == null) {
                        department = departmentService.getDeptByWeixin(org_id, user.getDepartment().get(i).toString());
                        if (department == null) {
                            continue;//如果有部门找不到的，继续
                        }
                        map.put(user.getDepartment().get(i).toString(), department);
                    }
                    detId.add(department.getId());
                }
                if (detId.size() == 0) {
                    //获取无部门的情况
                    if(vo == null){
                        getWxEmptyDept(detId);
                    }
                }
            } else {
                //获取无部门的情况
                if(vo == null){
                    getWxEmptyDept(detId);
                }
            }
            if (vo != null) {
                //找到用户，从待删除列表去掉
                userDelList.remove(vo.getId());
                //如果机构id不同
                if (!org_id.equals(vo.getOrgId())) {
                    logger.error("sysUser同步联系人出错,用户的机构id不同：" + user.toString());
                    addErrorUser(user, "用户账号已在该企业微信的其它机构中存在");
                    return;
                }
                boolean[] isChange = WxUserService.verifyChange(vo, user);
                //判断是否需要更新，如果没有修改
                if (!isChange[0]) {
                    if(detId.size()==0){//如果用户没有部门信息，不需要更新任何信息
                        return;
                    }
                    if(!isChangeDeptId(detId,vo)){
                        return;
                    }
                }
                tbQyUserInfoPO = new TbQyUserInfoPO();
                tbQyUserInfoPO.setId(vo.getId());
                if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {//离职
                    tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
                }
                ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user, vo);
                //tbQyUserInfoPO.setDeptId(rootDepartment.getId());
                tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                tbQyUserInfoPO.setOrgId(org_id);
                tbQyUserInfoPO.setUserId(vo.getUserId());

				/*//同步联系人到微信后台
				WxUserService.updateUser(user,corpId);*/
                //更新本地用户信息
                UpdateUserResult uur = contactService.updateUser(tbQyUserInfoPO, detId, false);
                updateUserList.add(tbQyUserInfoPO);
                userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                if (uur.isUpdateDept()) {//更新了部门信息
                    UserInfoChangeNotifier.getDeptUserRef(uur.getOldDeptIds(), detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap, delDeptRefUserMap);
                }
                //修改缓存中的人员数据
                CacheSessionManager.update(tbQyUserInfoPO.getUserId());
                //记录用户更新的数据，以便更新冗余字段 chenfeixiong 2015/06/30
                if (isChange[1]) {
                    try {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("creator", "admin");
                        map.put("userId", tbQyUserInfoPO.getUserId());
                        map.put("orgId", tbQyUserInfoPO.getOrgId());
                        map.put("item1", tbQyUserInfoPO.getPersonName());
                        map.put("item2", tbQyUserInfoPO.getHeadPic());
                        map.put("item3", tbQyUserInfoPO.getWxUserId());
                        map.put("is_manager", "0");
                        contactService.addUdpatePersonInfo(map);
                    } catch (Exception e) {
                        logger.error("sysUser更新冗余字段", e);
                    } catch (BaseException e) {
                        logger.error("sysUser更新冗余字段", e);
                    }
                }
                return;
            } else {
                tbQyUserInfoPO = new TbQyUserInfoPO();
                tbQyUserInfoPO.setId(ContactUtil.getUserId(corpId, user.getUserid()));
                ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user);
                tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
                tbQyUserInfoPO.setCorpId(corpId);
                //tbQyUserInfoPO.setDeptId(rootDepartment.getId());
                tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
                tbQyUserInfoPO.setCreateTime(new Date());
                tbQyUserInfoPO.setOrgId(org_id);
                tbQyUserInfoPO.setCreatePerson(createPerson);

                tbQyUserInfoPO.setIsConcerned("0");
				/*//同步联系人到微信后台
				WxUserService.updateUser(user,corpId);
				logger.debug("同步联系人到微信后台corpId："+corpId+","+user.toString());*/
                contactService.insertUser(tbQyUserInfoPO, detId);
                addUserList.add(tbQyUserInfoPO);
                userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
                UserInfoChangeNotifier.getDeptUserRef(detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap);
                logger.debug("sysUser添加联系人corpId：" + corpId + "," + user.toString());
            }
        } catch (Exception e) {
            logger.error("sysUser同步联系人出错：" + user.toString(), e);
        } catch (BaseException e) {
            logger.error("sysUser同步联系人出错：" + user.toString(), e);
        }
    }

    //微信接口获取用户部门为空的人员
    public final static String EMPTY_DEPT_NAME = "";

    /**
     * 因为系统必须有所属部门，当用户在无部门id时，默认到相应部门下
     *
     * @param detId
     * @author Sun Qinghai
     * @2016-3-13
     * @version 1.0
     */
    private void getWxEmptyDept(List<String> detId) {
        try {
            TbDepartmentInfoPO department = map.get(EMPTY_DEPT_NAME);
            if (department != null) {
                detId.add(department.getId());
                return;
            }
            List<TbDepartmentInfoPO> deptList = departmentService.getDeptByParent(org_id, null, EMPTY_DEPT_NAME);
            TbDepartmentInfoPO nullDepartment;
            if (deptList == null || deptList.size() == 0) {
                nullDepartment = new TbDepartmentInfoPO();
                nullDepartment.setId(UUID32.getID());
                nullDepartment.setDepartmentName(EMPTY_DEPT_NAME);
                nullDepartment.setDeptFullName(EMPTY_DEPT_NAME);
                nullDepartment.setCreatePerson(createPerson);
                nullDepartment.setCreateTime(new Date());
                nullDepartment.setOrgId(org_id);
                nullDepartment.setShowOrder(0);
                nullDepartment.setParentDepart("");
                contactService.insertPO(nullDepartment, false);
                logger.debug("dpdp添加部门corpId：" + corpId + "," + EMPTY_DEPT_NAME);
            } else {
                nullDepartment = deptList.get(0);
            }
            detId.add(nullDepartment.getId());
            map.put(EMPTY_DEPT_NAME, nullDepartment);
        } catch (Exception e) {
            logger.error("UserListSyncThread getWxEmptyDept error corpId：" + corpId, e);
        } catch (BaseException e) {
            logger.error("UserListSyncThread getWxEmptyDept error corpId：" + corpId, e);
        }
    }

    private TbDepartmentInfoPO getDefaultDept(String orgId,String wxDeptId,String corpId) throws Exception, BaseException {
        String deptName = "未分配部门人员";
        List<TbDepartmentInfoPO> deptList = departmentService.getDeptByParent(org_id, null, deptName);
        TbDepartmentInfoPO rootDepartment;
        if (deptList == null || deptList.size() == 0) {
            rootDepartment = new TbDepartmentInfoPO();
            rootDepartment.setDepartmentName(deptName);
            rootDepartment.setDeptFullName(deptName);
            rootDepartment.setParentDepart("");
            rootDepartment.setCreatePerson(createPerson);
            rootDepartment.setCreateTime(new Date());
            rootDepartment.setOrgId(orgId);
            rootDepartment.setShowOrder(0);
            //同步部门到微信
            WxDept wxDept = new WxDept();
            wxDept.setName(rootDepartment.getDepartmentName());
            wxDept.setParentid(wxDeptId);
            wxDept = WxDeptService.addDept(wxDept, corpId, org_id);
            logger.debug("dpdp同步部门到微信corpId：" + corpId + "," + wxDept.toString());
            rootDepartment.setWxId(wxDept.getId());
            rootDepartment.setWxParentid(wxDept.getParentid());
            rootDepartment.setId(DepartmentUtil.getDeptId(corpId, wxDept.getId()));
            contactService.insertPO(rootDepartment, false);
            logger.debug("dpdp添加部门corpId：" + corpId + ",");
        } else {
            rootDepartment = deptList.get(0);
        }
        return rootDepartment;
    }

    /**
     * 迭代从微信上同步部门，适用于tree非企业根节点开始的情况
     *
     * @param deptTree
     * @throws Exception
     * @throws BaseException
     * @author Sun Qinghai
     * @2016-1-25
     * @version 1.0
     */
    private void addNotOrgRootDepartByWeixin(WxDeptTree deptTree) throws Exception, BaseException {
        TbDepartmentInfoPO parentDepart;
        TbDepartmentInfoPO rootDeptPO = null;
        String wxId = deptTree.getId().toString();
        if ("1".equals(wxId)) {//如果跟节点是机构节点
            rootDeptPO = new TbDepartmentInfoPO();
            rootDeptPO.setId("");
            rootDeptPO.setWxId(wxId);
            //加入待删除的本地部门
            List<TbDepartmentInfoPO> depts = departmentService.getAllDepartForSync(org_id);
            if (depts != null && depts.size() > 0) {
                for (TbDepartmentInfoPO tbDepartmentInfoPO : depts) {
                    delDept.put(tbDepartmentInfoPO.getId(), tbDepartmentInfoPO);
                    updateDept.add(tbDepartmentInfoPO.getId());
                }
            }
        } else {
            //加入待删除的本地部门
            rootDeptPO = departmentService.getDeptByWeixin(org_id, wxId);
            if (rootDeptPO != null) {
                List<TbDepartmentInfoPO> depts = departmentService.getChildDeptByFullName(org_id, rootDeptPO.getDeptFullName());
                if (depts != null && depts.size() > 0) {
                    for (TbDepartmentInfoPO tbDepartmentInfoPO : depts) {
                        delDept.put(tbDepartmentInfoPO.getId(), tbDepartmentInfoPO);
                        updateDept.add(tbDepartmentInfoPO.getId());
                    }
                }
                depts = null;
            }
            String wxParentId = deptTree.getParentid().toString();
            parentDepart = departmentService.getDeptByWeixin(org_id, wxParentId);
            if (parentDepart == null) {
                parentDepart = new TbDepartmentInfoPO();
                parentDepart.setWxId(wxParentId);
                parentDepart.setId("");
            }
            String fullName = StringUtil.isNullEmpty(parentDepart.getDeptFullName()) ? "" : (parentDepart.getDeptFullName() + DepartmentUtil.DEPT_NAME_SPLIT);
            //根据微信部门id和父部门id获取用户部门信息
            if (rootDeptPO == null) {
                rootDeptPO = new TbDepartmentInfoPO();
                rootDeptPO.setId(UUID32.getID());
                rootDeptPO.setParentDepart(parentDepart.getId());
                rootDeptPO.setCreatePerson(createPerson);
                rootDeptPO.setCreateTime(new Date());
                rootDeptPO.setOrgId(org_id);
                rootDeptPO.setShowOrder(deptTree.getOrder());
                rootDeptPO.setDepartmentName(deptTree.getName());
                rootDeptPO.setDeptFullName(fullName + deptTree.getName());
                rootDeptPO.setWxParentid(parentDepart.getWxId());
                rootDeptPO.setWxId(wxId);
                rootDeptPO.setPermission(parentDepart.getPermission());
                departmentService.insertPO(rootDeptPO, false);
                if (addDeptList == null) {
                    addDeptList = new ArrayList<DeptSyncInfoVO>(10);
                }
                DeptSyncInfoVO vo = new DeptSyncInfoVO();
                BeanHelper.copyBeanProperties(vo, rootDeptPO);
                addDeptList.add(vo);
            } else {//如果通过微信id找到了该部门，直接修改部门的名称和全称等信息，并更新到数据库
                delDept.remove(rootDeptPO.getId());
                //如果部门全称和部门微信id等不相等，需要更新
                boolean isUpdateParentDepart = !WxqyhStringUtil.strCstrIgnoreNull(rootDeptPO.getParentDepart(), parentDepart.getId());
                if (isUpdateParentDepart || !StringUtil.strCstr(rootDeptPO.getDeptFullName(), fullName + deptTree.getName())
                        || !StringUtil.strCstr(rootDeptPO.getWxParentid(), wxParentId)) {
                    TbDepartmentInfoPO po = new TbDepartmentInfoPO();
                    po.setId(rootDeptPO.getId());
                    po.setWxParentid(wxParentId);
                    po.setDepartmentName(deptTree.getName());
                    po.setDeptFullName(fullName + deptTree.getName());
                    po.setParentDepart(parentDepart.getId());
                    //设置部门权限
                    if (AssertUtil.isEmpty(rootDeptPO.getPermission())) {
                        rootDeptPO.setPermission(parentDepart.getPermission());
                    } else {
                        if (!AssertUtil.isEmpty(parentDepart.getPermission())) {
                            if (rootDeptPO.getPermission().compareTo(parentDepart.getPermission()) < 0) {//子部门权限不能比父部门权限大
                                rootDeptPO.setPermission(parentDepart.getPermission());
                            }
                        }
                    }
                    po.setPermission(rootDeptPO.getPermission());
                    departmentService.updatePO(po, false);

                    rootDeptPO.setDepartmentName(deptTree.getName());
                    rootDeptPO.setDeptFullName(fullName + deptTree.getName());
                    rootDeptPO.setWxId(po.getWxId());
                    rootDeptPO.setWxParentid(po.getWxParentid());
                    rootDeptPO.setParentDepart(parentDepart.getId());
                    if (updateDeptList == null) {
                        updateDeptList = new ArrayList<DeptSyncInfoVO>(10);
                    }
                    DeptSyncInfoVO vo = new DeptSyncInfoVO();
                    BeanHelper.copyBeanProperties(vo, rootDeptPO);
                    vo.setUpdateParentDepart(isUpdateParentDepart);
                    updateDeptList.add(vo);
                }
            }
            map.put(wxId, rootDeptPO);
        }
        if (deptTree.getChildrenDept().size() > 0) {
            //迭代新增下一级部门
            addDepartByWeixin(deptTree, rootDeptPO);
        }
		/*if(isNotRootOrg){//需要删除部门
				//下级部门都增加完了后，将其增加到待删除列表
				delWeixinDept.add(wxDeptTree.getId().toString());
			}*/
    }

    /**
     * 迭代从微信上同步部门
     *
     * @param deptTree
     * @param parentDepart
     * @throws Exception
     * @throws BaseException
     * @author Sun Qinghai
     * @2014-11-24
     * @version 1.0
     */
    private void addDepartByWeixin(WxDeptTree deptTree, TbDepartmentInfoPO parentDepart) throws Exception, BaseException {
        List<WxDeptTree> list = deptTree.getChildrenDept();
        if (list.size() == 0) {
            return;
        }
        WxDeptTree wxDeptTree;
        int size = list.size();
        String fullName = StringUtil.isNullEmpty(parentDepart.getDeptFullName()) ? "" : (parentDepart.getDeptFullName() + DepartmentUtil.DEPT_NAME_SPLIT);
        for (int i = 0; i < size; i++) {
            wxDeptTree = list.get(i);
            //根据微信部门id和父部门id获取用户部门信息
            tbDepartmentInfoPO = departmentService.getDeptByWeixin(org_id, wxDeptTree.getId() + "");
            if (tbDepartmentInfoPO == null) {
                //验证是否重名
                List<TbDepartmentInfoPO> templist = departmentService.getDeptByParent(org_id, parentDepart.getId(), wxDeptTree.getName());
                if (!AssertUtil.isEmpty(templist)) {
                    tbDepartmentInfoPO = templist.get(0);
                    delDept.remove(tbDepartmentInfoPO.getId());
                    //如果部门全称和部门微信id等不相等，需要更新
                    if (!StringUtil.strCstr(tbDepartmentInfoPO.getDeptFullName(), fullName + wxDeptTree.getName())
                            || !StringUtil.strCstr(tbDepartmentInfoPO.getWxId(), wxDeptTree.getId().toString())
                            || !StringUtil.strCstr(tbDepartmentInfoPO.getWxParentid(), wxDeptTree.getParentid().toString())) {
                        TbDepartmentInfoPO po = new TbDepartmentInfoPO();
                        po.setId(tbDepartmentInfoPO.getId());
                        po.setDepartmentName(wxDeptTree.getName());
                        po.setDeptFullName(fullName + wxDeptTree.getName());
                        tbDepartmentInfoPO.setDepartmentName(wxDeptTree.getName());
                        tbDepartmentInfoPO.setDeptFullName(fullName + wxDeptTree.getName());
                        //设置部门权限
                        if (AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission())) {
                            tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                        } else {
                            if (!AssertUtil.isEmpty(parentDepart.getPermission())) {
                                if (tbDepartmentInfoPO.getPermission().compareTo(parentDepart.getPermission()) < 0) {//子部门权限不能比父部门权限大
                                    tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                                }
                            }
                        }
                        po.setPermission(tbDepartmentInfoPO.getPermission());

                        po.setWxId(wxDeptTree.getId().toString());
                        po.setWxParentid(wxDeptTree.getParentid().toString());
                        departmentService.updatePO(po, false);
                        tbDepartmentInfoPO.setWxId(po.getWxId());
                        tbDepartmentInfoPO.setWxParentid(po.getWxParentid());
                        if (updateDeptList == null) {
                            updateDeptList = new ArrayList<DeptSyncInfoVO>(10);
                        }
                        DeptSyncInfoVO vo = new DeptSyncInfoVO();
                        BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                        vo.setUpdateParentDepart(false);
                        updateDeptList.add(vo);
                    }
                } else {//如果没有找到微信上的部门
                    tbDepartmentInfoPO = new TbDepartmentInfoPO();
                    tbDepartmentInfoPO.setId(UUID32.getID());
                    tbDepartmentInfoPO.setParentDepart(parentDepart.getId());
                    tbDepartmentInfoPO.setCreatePerson(createPerson);
                    tbDepartmentInfoPO.setCreateTime(new Date());
                    tbDepartmentInfoPO.setOrgId(org_id);
                    tbDepartmentInfoPO.setShowOrder(i);
                    tbDepartmentInfoPO.setDepartmentName(wxDeptTree.getName());
                    tbDepartmentInfoPO.setDeptFullName(fullName + wxDeptTree.getName());
                    tbDepartmentInfoPO.setWxParentid(parentDepart.getWxId());
                    tbDepartmentInfoPO.setWxId(wxDeptTree.getId().toString());
                    tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                    departmentService.insertPO(tbDepartmentInfoPO, false);
                    if (addDeptList == null) {
                        addDeptList = new ArrayList<DeptSyncInfoVO>(10);
                    }
                    DeptSyncInfoVO vo = new DeptSyncInfoVO();
                    BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                    addDeptList.add(vo);
                }
            } else {//如果通过微信id找到了该部门，直接修改部门的名称和全称等信息，并更新到数据库
                delDept.remove(tbDepartmentInfoPO.getId());
                //如果部门全称和部门微信id等不相等，需要更新
                boolean isUpdateParentDepart = !WxqyhStringUtil.strCstrIgnoreNull(tbDepartmentInfoPO.getParentDepart(), parentDepart.getId());
                if (isUpdateParentDepart || !StringUtil.strCstr(tbDepartmentInfoPO.getDeptFullName(), fullName + wxDeptTree.getName())
                        || !StringUtil.strCstr(tbDepartmentInfoPO.getWxParentid(), wxDeptTree.getParentid().toString())) {
                    TbDepartmentInfoPO po = new TbDepartmentInfoPO();
                    po.setId(tbDepartmentInfoPO.getId());
                    po.setWxParentid(wxDeptTree.getParentid().toString());
                    po.setDepartmentName(wxDeptTree.getName());
                    po.setDeptFullName(fullName + wxDeptTree.getName());
                    po.setParentDepart(parentDepart.getId());
                    //设置部门权限
                    if (AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission())) {
                        tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                    } else {
                        if (!AssertUtil.isEmpty(parentDepart.getPermission())) {
                            if (tbDepartmentInfoPO.getPermission().compareTo(parentDepart.getPermission()) < 0) {//子部门权限不能比父部门权限大
                                tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                            }
                        }
                    }
                    po.setPermission(tbDepartmentInfoPO.getPermission());
                    departmentService.updatePO(po, false);

                    tbDepartmentInfoPO.setDepartmentName(wxDeptTree.getName());
                    tbDepartmentInfoPO.setDeptFullName(fullName + wxDeptTree.getName());
                    tbDepartmentInfoPO.setWxId(po.getWxId());
                    tbDepartmentInfoPO.setWxParentid(po.getWxParentid());
                    tbDepartmentInfoPO.setParentDepart(parentDepart.getId());
                    if (updateDeptList == null) {
                        updateDeptList = new ArrayList<DeptSyncInfoVO>(10);
                    }
                    DeptSyncInfoVO vo = new DeptSyncInfoVO();
                    BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                    vo.setUpdateParentDepart(isUpdateParentDepart);
                    updateDeptList.add(vo);
                }
            }
            map.put(wxDeptTree.getId().toString(), tbDepartmentInfoPO);
            if (wxDeptTree.getChildrenDept().size() > 0) {
                //迭代新增下一级部门
                addDepartByWeixin(wxDeptTree, tbDepartmentInfoPO);
            }
			/*if(isNotRootOrg){//需要删除部门
				//下级部门都增加完了后，将其增加到待删除列表
				delWeixinDept.add(wxDeptTree.getId().toString());
			}*/
        }
    }

    /**
     * 同步用户异常数据保存
     *
     * @param user
     * @author Sun Qinghai
     * @2014-12-8
     * @version 1.0
     */
    private void addErrorUser(WxUser user, String remark) {
        try {
            TbQyUserInfoSyncErrorPO errorUser = new TbQyUserInfoSyncErrorPO();
            errorUser.setWxUserId(user.getUserid());
            errorUser.setWeixinNum(user.getWeixinid());
            errorUser.setMobile(user.getMobile());
            errorUser.setEmail(user.getEmail());
            errorUser.setPosition(user.getPosition());
            errorUser.setUserStatus(user.getStatus());
            errorUser.setOrgId(org_id);
            errorUser.setCorpId(corpId);
            errorUser.setWxDeptId(user.getDepartment() == null ? null : user.getDepartment().toString());
            errorUser.setRemark(remark);
            errorUser.setCreateTime(new Date());
            errorUser.setUserId(createPerson);
            errorUser.setPersonName(user.getName());
            contactService.insertPO(errorUser, true);
        } catch (DataConfictException e) {
            logger.error("保存同步用户异常数据失败", e);
        } catch (Exception e) {
            logger.error("保存同步用户异常数据失败", e);
        }
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public TbDepartmentInfoPO getTbDepartmentInfoPO() {
        return tbDepartmentInfoPO;
    }

    public void setTbDepartmentInfoPO(TbDepartmentInfoPO tbDepartmentInfoPO) {
        this.tbDepartmentInfoPO = tbDepartmentInfoPO;
    }

    public TbQyUserInfoPO getTbQyUserInfoPO() {
        return tbQyUserInfoPO;
    }

    public void setTbQyUserInfoPO(TbQyUserInfoPO tbQyUserInfoPO) {
        this.tbQyUserInfoPO = tbQyUserInfoPO;
    }
}
