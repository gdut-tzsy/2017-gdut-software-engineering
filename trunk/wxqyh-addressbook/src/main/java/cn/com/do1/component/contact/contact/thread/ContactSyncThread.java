package cn.com.do1.component.contact.contact.thread;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyChangeContactPO;
import cn.com.do1.component.addressbook.contact.model.TbQyContactSyncPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserDepartmentRefPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.util.ContactDictUtil;
import cn.com.do1.component.addressbook.contact.util.ContactSyncStatus;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.util.ChangeAgentUserUtil;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.contact.contact.util.UserInfoChangeNotifier;
import cn.com.do1.component.contact.contact.vo.AgentRange;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import cn.com.do1.component.wxcgiutil.contacts.*;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

/**
 * 微信变更通讯录通知同步方案
 * @author sunqinghai
 * @date 2016 -10-13
 */
public class ContactSyncThread extends Thread {
    /**
     * The constant logger.
     */
    private static final transient Logger logger = LoggerFactory.getLogger(ContactSyncThread.class);
    /**
     * The constant contactService.
     */
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    /**
     * 通讯录应用对应的套件id
     */
    //private static final String CONTACT_SUIT_ID = "tj0663c8afd643ba9e"; //tje1ae4974cc797489,tj0663c8afd643ba9e
    /**
     * The constant departmentService.
     */
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    /**
     * The constant departmentMgrService.
     */
    private static IDepartmentMgrService departmentMgrService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentMgrService.class);
    /**
     * The constant experienceapplicationService.
     */
    private static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
    /**
     * The constant tagService.
     */
    private static ITagMgrService tagService = DqdpAppContext.getSpringContext().getBean("tagService", ITagMgrService.class);
    /**
     * The constant deptSplit.
     */
    private static String deptSplit = "->";
    /**
     * The Corp id.
     */
    private String corpId;
    /**
     * 微信上的用户id，对应的本地用户userId
     */
    private Map<String, TbQyUserInfoPO> syncUserMap = new HashMap<String, TbQyUserInfoPO>(1000);
    /**
     * 微信上的用户部门id，对应的本地创建的部门信息
     */
    private Map<String, TbDepartmentInfoPO> deptWxIdMap = null;
    /**
     * The Add user list.
     */
    private List<TbQyUserInfoPO> addUserList = null;
    /**
     * 新增的用户id信息
     */
    private Set<String> addUserSet = null;
    /**
     * The Update user list.
     */
    private List<TbQyUserInfoPO> updateUserList = null;
    /**
     * The Leav user list.
     */
    private List<TbQyUserInfoPO> leavUserList = null;
    /**
     * The User ref dept map.
     */
    private Map<String, List<String>> userRefDeptMap = null;
    /**
     * The Add dept ref user map.
     */
    private Map<String, List<String>> addDeptRefUserMap = null;
    /**
     * The Del dept ref user map.
     */
    private Map<String, List<String>> delDeptRefUserMap = null;
    /**
     * 需要新增的部门list
     */
    private List<DeptSyncInfoVO> addDeptList = null;
    /**
     * 需要编辑的部门list
     */
    private List<DeptSyncInfoVO> updateDeptList = null;
    /**
     * 需要删除的部门list
     */
    private List<DeptSyncInfoVO> delDeptList = null;
    /**
     * The Add user dept ref list.
     */
    private List<TbQyUserDepartmentRefPO> addUserDeptRefList = null;
    /**
     * 进入可见范围的部门信息
     */
    private List<TbDepartmentInfoPO> inDeptList = null;
    /**
     * The Del user dept ref list.
     */
    private List<String> delUserDeptRefList = null;
    /**
     * The Update user set.
     */
    private Set<String> updateUserSet = new HashSet<String>(100);
    /**
     * The Update dept set.
     */
    private Set<String> updateDeptSet = new HashSet<String>(20);
    /**
     * The Agent user ref po list.
     */
    private List<TbQyAgentUserRefPO> agentUserRefPOList = null;
    /**
     * The Agent user ref id list.
     */
    private List<String> agentUserRefIdList = null;
    /**
     * The Update agent code set.
     */
    private Set<String> updateAgentCodeSet = new HashSet<String>(20);
    /**
     * 待更新可见范围的
     */
    private Map<String, TbQyExperienceAgentPO> updateAgentCodeRangeMap = null;
    /**
     * The Org.
     */
    private DqdpOrgVO org;
    /**
     * 是否是这个企业的第一次同步
     */
    private boolean isFirst = false;
    /**
     * 是否已经同步了所有人可见的套件
     */
    private boolean isSyncAllRange = false;
    /**
     * 是否有所有人可见的套件
     */
    private boolean isHasAllRange = false;
    /**
     * 是否处理企业微信变更事件
     */
    private boolean isQiyeweixin = false;
    /**
     * 套件的可见范围
     */
    private Map<String,Boolean> suiteRangeAll = new HashMap<String, Boolean>(5);
    private List<AgentRange> agentRangeList = null;

    /**
     * 构造方法
     * @param corpId 机构信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    public ContactSyncThread(String corpId) {
        this.corpId = corpId;
        try {
            List<TbQyContactSyncPO> list = contactService.getContactSyncList(corpId, ContactSyncStatus.STATUS_WAIT);
            for(TbQyContactSyncPO po : list){
                updateSyncPOStatus(po, ContactSyncStatus.STATUS_ING);
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread ContactSyncThread()", e);
            ExceptionCenter.addException(e, "ContactSyncThread ContactSyncThread() @sqh", corpId);
        }
    }

    /**
     * run方法
     * @author sunqinghai
     * @date 2016 -10-13
     */
    public void run() {
        logger.info("ContactSyncThread run start " + corpId);
        List<TbQyContactSyncPO> list = null;
        try {
            list = contactService.getContactSyncList(corpId, ContactSyncStatus.STATUS_ING);
            if (list.size() > 0) {
                if(Configuration.AUTO_CORPID.equals(corpId)){
                    for(TbQyContactSyncPO po : list){
                        updateSyncPOError(po);
                    }
                    return;
                }
                org = OrgUtil.getOrgByCorpId(corpId);
                if (org == null) {
                    for(TbQyContactSyncPO po : list){
                        //更新此状态为未托管
                        updateSyncPONoneTrust(po);
                    }
                    return;
                }
                //初始化部门信息到map中
                initAllDept();

                Iterator<TbQyContactSyncPO> it = list.iterator();
                TbQyContactSyncPO po;
                //boolean isTrustAgent = WxAgentUtil.isTrustAgent(org.getCorpId(), WxAgentUtil.getAddressBookCode());//是否托管了通讯录
                boolean isSyncSuitOne = true;
                Map<String, List<AgentRange>> arMap = new HashMap<String, List<AgentRange>>(list.size());
                AgentCacheInfo aci = WxAgentUtil.getAgentCache(corpId, WxAgentUtil.getAddressBookCode());
                while (it.hasNext()) {
                    po = it.next();
                    if (po.getStatus() > ContactSyncStatus.STATUS_ERROR) {//如果已经更新完成
                        it.remove();
                        continue;
                    }
                    if (aci != null && aci.isTrust()) {//如果有通讯录应用，先同步通讯录应用所在的套件
                        if( po.getSuiteId().equals(aci.getSuiteId())){
                            //直接调用通讯录对应的suitId
                            it.remove();
                            List<AgentRange> arList = getAgentRange(po);
                            if(arList == null){
                                //更新此状态为未托管
                                updateSyncPONoneTrust(po);
                                continue;
                            }
                            arMap.put(po.getSuiteId(), arList);
                            if (po.getNextSeq() == 0) {
                                isFirst = true;
                            }
                            isHasAllRange = isRangAll(arList);
                            syncContact(po, arList);
                            isSyncAllRange = isHasAllRange;
                        }
                    }
                    else if(isSyncSuitOne){
                        List<AgentRange> arList = getAgentRange(po);
                        if(arList == null){
                            //更新此状态为未托管
                            it.remove();
                            updateSyncPONoneTrust(po);
                            continue;
                        }
                        arMap.put(po.getSuiteId(), arList);
                        if (isRangAll(arList)) { //如果是全公司可见，先同步一次全公司可见的，这样避免不必要的重复更新
                            if (po.getNextSeq() == 0) {
                                isFirst = true;
                            }
                            isHasAllRange = true;
                            syncContact(po, arList);
                            it.remove();
                            isSyncSuitOne = false; //只需要更新一次
                            isSyncAllRange = true;
                        }
                    }
                }
                if (list.size() > 0) { //循环将所有套件，更新应用可见范围
                    it = list.iterator();
                    boolean isAllFirstSync = isFirst;//是否已经同步了全公司
                    while (it.hasNext()) {
                        po = it.next();
                        if (!isAllFirstSync && po.getNextSeq() == 0) {
                            isFirst = true;
                        }
                        else{
                            isFirst = false;
                        }
                        if(arMap.containsKey(po.getSuiteId())){
                            syncContact(po, arMap.get(po.getSuiteId()));
                        }
                        else {
                            List<AgentRange> arList = getAgentRange(po);
                            if(arList == null){
                                //更新此状态为未托管
                                updateSyncPONoneTrust(po);
                                continue;
                            }
                            syncContact(po, arList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread run", e);
            ExceptionCenter.addException(e, "ContactSyncThread run @sqh", corpId);
            updateSyncPOError(list);
        } catch (BaseException e) {
            logger.error("ContactSyncThread run", e);
            ExceptionCenter.addException(e, "ContactSyncThread run @sqh", corpId);
            updateSyncPOError(list);
        } finally {
            //清理需要清理的应用可见范围
            if (!AssertUtil.isEmpty(updateAgentCodeRangeMap)) {
                for (TbQyExperienceAgentPO po : updateAgentCodeRangeMap.values()) {
                    ChangeAgentUserUtil.resetAgentUser(po);
                }
            }
            logger.info("ContactSyncThread run end " + corpId);
            ContactSyncStatus.remove(corpId);
        }
    }

    /**
     * 更新同步错误的信息
     * @param po 同步po
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void updateSyncPOError(TbQyContactSyncPO po) {
        updateSyncPOStatus(po, ContactSyncStatus.STATUS_ERROR);
    }

    /**
     * 更新同步错误的信息
     * @param list
     * @author sunqinghai
     * @date 2017 -4-6
     */
    private void updateSyncPOError(List<TbQyContactSyncPO> list) {
        if (AssertUtil.isEmpty(list)) {
            return;
        }
        for (TbQyContactSyncPO po : list) {
            updateSyncPOStatus(po, ContactSyncStatus.STATUS_ERROR);
        }
    }

    /**
     * 更新同步错误的信息
     * @param po 同步po
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void updateSyncPOStatus(TbQyContactSyncPO po, int status) {
        try {
            TbQyContactSyncPO updatePO = new TbQyContactSyncPO();
            updatePO.setId(po.getId());
            updatePO.setFinishTime(new Date());
            updatePO.setStatus(status);
            contactService.updatePO(updatePO, false);
        } catch (SQLException e) {
            logger.error("ContactSyncThread updateSyncPOStatus " + corpId+",po="+po.getId(), e);
            ExceptionCenter.addException(e, "ContactSyncThread updateSyncPOStatus @sqh", corpId+",po="+po.getId());
        }
    }

    /**
     * 更新未托管的情况的同步数据，
     * @param po 同步信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void updateSyncPONoneTrust(TbQyContactSyncPO po) {//未托管此套件
        try {
            if(po.getUpdateTime().getTime() < TimeMillisUtil.addMinute(-30)){//30分钟前的还未托管，就改成异常状态，避免一直重复同步
                updateSyncPOError(po);
            }
            else{
                TbQyContactSyncPO updatePO = new TbQyContactSyncPO();
                updatePO.setId(po.getId());
                updatePO.setFinishTime(new Date());
                updatePO.setStatus(ContactSyncStatus.STATUS_INIT); //改成初始状态，下次同步
                contactService.updatePO(updatePO,false);
            }
        } catch (SQLException e) {
            logger.error("ContactSyncThread updateSyncPONoneTrust " + corpId+",po="+po.getId(), e);
            ExceptionCenter.addException(e, "ContactSyncThread updateSyncPONoneTrust @sqh", corpId+",po="+po.getId()+"|"+po.getNextSeq()+"|"+po.getNextOffset());
        }
    }

    /**
     * 同步通讯录
     * @param po 同步数据
     * @param agentList  应用list
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void syncContact(TbQyContactSyncPO po, List<AgentRange> agentList) {
        WxSyncVO syncVO = null;
        try {
            if (po.getCurrentSeq() == -1) {//表示企业微信
                isQiyeweixin = true;
                long thisTime = System.currentTimeMillis()/1000 - 10;//只执行当前时间10s前的数据，防止因为数据上报顺序问题导致漏执行一些数据
                //有可能10s内的数据微信还未上报，10s的时间只是大概评估给出的值
                int size = 2000;
                List<TbQyChangeContactPO> list = contactService.getChangeContactList(corpId, po.getSuiteId(), po.getNextSeq(), po.getNextOffset(), size);
                syncVO = changeContactToWxSyncData(list, po, thisTime, size, agentList);
            }
            else {
                isQiyeweixin = false;
                syncVO = WxSyncUtil.getPage(org.getOrgId(), corpId, po.getSuiteId(), po.getNextSeq(), po.getNextOffset());
                if(po.getNextSeq()==0){//表示初始开始，先删掉
                    List<String> angentCodes = getAgentCodes(agentList, true);
                    if(angentCodes != null){
                        List<String> list = experienceapplicationService.getAgentUserRefByUserId(org.getOrgId(), null, angentCodes);
                        //加入待删除列表
                        delAgentUserRef(list);
                    }
                }
            }
            if (syncVO == null) {
                return;
            }
            WxSyncData[] datas = syncVO.getData();
            if(datas != null && datas.length>0){
                //初始化部门信息到map中
                initAllDept();
                //去掉重复更新导致的重复数据
                dealWxSyncData(datas);
                for(WxSyncData date : datas){
                    if (date == null) {
                        continue;
                    }
                    switch (date.getType()){
                        case ContactSyncStatus.SYNC_TYPE_USER_IN://操作用户进入信息
                            userIn(agentList, date.getUser());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_USER_OUT://用户退出管理范围
                            //不处理用户退出的情况，交由应用可见范围变更通知处理
                            //userOut(agentList, date.getUserid());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_USER_DEL://删除用户
                            userDel(agentList, date.getUserid());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_USER_UPDATE://修改用户
                            userUpdate(agentList, date.getUser());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_DEPT_IN://部门进入管理范围
                            deptIn(agentList, date.getDepartment());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_DEPT_OUT://部门退出管理范围
                            //不处理部门退出的情况，交由应用可见范围变更通知处理
                            //deptOut(agentList, date.getDepartment_id());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_DEPT_DEL://删除部门
                            deptDel(date.getDepartment_id());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_DEPT_UPDATE://修改部门
                            deptUpdate(agentList, date.getDepartment());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_DEPT_ADD://新增部门
                            deptIn(agentList, date.getDepartment());
                            break;
                        case ContactSyncStatus.SYNC_TYPE_TAG_PARTY_IN://部门进入标签管理范围
                            tagDeptIn(agentList, date.getDepartment_id(), date.getTagid());
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (BaseException e) {
            logger.error("ContactSyncThread syncContact " + corpId+",po="+po.getId(), e);
            ExceptionCenter.addException(e, "ContactSyncThread syncContactContactSyncThread syncContact @sqh", corpId+",po="+po.getId()+"|"+po.toString());
            /*if ("50005".equals(e.getErrCode())) { //企业已禁用
                updateSyncPOError(po);
            }*/
            updateSyncPOError(po);
        } catch (Exception e) {
            logger.error("ContactSyncThread syncContact " + corpId+",po="+po.getId(), e);
            ExceptionCenter.addException(e, "ContactSyncThread syncContact @sqh", corpId+",po="+po.getId()+"|"+po.getNextSeq()+"|"+po.toString());
            updateSyncPOError(po);
        } finally {
            updateUserSet.clear(); //下一个循环后需要重新更新
            batchSubmit(agentList); //批量提交需要更新的用户和用户关联部门信息
            updateContactSyncPO(po, syncVO);
        }
        if(syncVO != null && syncVO.getIs_last() == 0){
            po.setNextSeq(syncVO.getNext_seq());
            po.setNextOffset(syncVO.getNext_offset());
            syncContact(po, agentList); //迭代下一页
        }
    }

    /**
     * 企业微信变更数据转为企业号数据
     * @param list
     * @param po
     * @param thisTime
     * @param size
     * @param agentList
     * @author sunqinghai
     * @date 2017 -6-15
     */
    private WxSyncVO changeContactToWxSyncData(List<TbQyChangeContactPO> list, TbQyContactSyncPO po, long thisTime, int size, List<AgentRange> agentList) throws Exception{
        WxSyncVO syncVO = new WxSyncVO();
        if (AssertUtil.isEmpty(list)) {
            syncVO.setNext_seq(po.getNextSeq());
            //是否最后一个
            syncVO.setIs_last(1);
            syncVO.setExecuteAll(true);
            syncVO.setNext_offset(po.getNextOffset());
            return syncVO;
        }
        int listSize = list.size();
        WxSyncData[] datas = new WxSyncData[listSize];
        WxSyncData wxSyncDataCloneable = new WxSyncData();
        WxSyncData wxSyncData;
        WxUser userCloneable = new WxUser();
        WxUser user;
        WxDept departmentCloneable = new WxDept();
        WxDept department;
        boolean isRunAllData = true;//是否所有数据都可以执行
        long nextSeq = po.getNextSeq();
        long nextOffset = po.getNextOffset();
        Map<String, WxUser> userMap = new HashMap<String, WxUser>(listSize);
        Map<String, WxDept> deptMap = new HashMap<String, WxDept>(listSize);
        String agentCode = agentList.get(0).getAgentCode();
        for(int i = 0; i<listSize; i++){
            TbQyChangeContactPO contact = list.get(i);
            //如果是最近的数据，下次再执行
            if (thisTime < contact.getTimeStamp()) {
                isRunAllData = false;
                continue;
            }
            nextSeq = contact.getTimeStamp();
            nextOffset = contact.getId();
            wxSyncData = wxSyncDataCloneable.clone();
            if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_CREATE_USER){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_USER_IN);
                user = userCloneable.clone();
                user.setUserid(contact.getUserid());
                user.setMobile(contact.getMobile());
                user.setEmail(contact.getEmail());
                user.setGender(contact.getGender());
                user.setName(contact.getName());
                user.setAvatar(contact.getAvatar());
                user.setPosition(contact.getPosition());
                List<String> departments = null;
                if (!StringUtil.isNullEmpty(contact.getDepartment())) {
                    departments = ListUtil.toList(contact.getDepartment().split(","));
                }
                user.setDepartment(departments);
                if (userMap.containsKey(user.getUserid())) {
                    WxUser wxUser = userMap.get(user.getUserid());
                    if (user.getMobile() == null) {
                        user.setMobile(wxUser.getMobile());
                    }
                    if (user.getEmail() == null) {
                        user.setEmail(wxUser.getEmail());
                    }
                    if (user.getGender() == null) {
                        user.setGender(wxUser.getGender());
                    }
                    if (user.getName() == null) {
                        user.setName(wxUser.getName());
                    }
                    if (user.getAvatar() == null) {
                        user.setAvatar(wxUser.getAvatar());
                    }
                    if (user.getPosition() == null) {
                        user.setPosition(wxUser.getPosition());
                    }
                    if (user.getDepartment() == null) {
                        user.setDepartment(wxUser.getDepartment());
                    }
                }
                userMap.put(user.getUserid(), user);
                wxSyncData.setUser(user);
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_UPDATE_USER){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_USER_UPDATE);
                user = userCloneable.clone();
                user.setUserid(contact.getUserid());
                user.setMobile(contact.getMobile());
                user.setEmail(contact.getEmail());
                user.setGender(contact.getGender());
                user.setName(contact.getName());
                user.setAvatar(contact.getAvatar());
                user.setPosition(contact.getPosition());
                List<String> departments = null;
                if (!StringUtil.isNullEmpty(contact.getDepartment())) {
                    departments = ListUtil.toList(contact.getDepartment().split(","));
                }
                user.setDepartment(departments);
                if (userMap.containsKey(user.getUserid())) {
                    WxUser wxUser = userMap.get(user.getUserid());
                    if (user.getMobile() == null) {
                        user.setMobile(wxUser.getMobile());
                    }
                    if (user.getEmail() == null) {
                        user.setEmail(wxUser.getEmail());
                    }
                    if (user.getGender() == null) {
                        user.setGender(wxUser.getGender());
                    }
                    if (user.getName() == null) {
                        user.setName(wxUser.getName());
                    }
                    if (user.getAvatar() == null) {
                        user.setAvatar(wxUser.getAvatar());
                    }
                    if (user.getPosition() == null) {
                        user.setPosition(wxUser.getPosition());
                    }
                    if (user.getDepartment() == null) {
                        user.setDepartment(wxUser.getDepartment());
                    }
                }
                userMap.put(user.getUserid(), user);
                wxSyncData.setUser(user);
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_DELETE_USER){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_USER_DEL);
                wxSyncData.setUserid(contact.getUserid());
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_UPDATE_PARTY){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_DEPT_UPDATE);
                department = departmentCloneable.clone();
                department.setId(contact.getDepartment());
                department.setName(contact.getName());
                department.setOrder(contact.getOrder());
                department.setParentid(contact.getParentId());
                if (deptMap.containsKey(department.getId())) {
                    WxDept wxDept = deptMap.get(department.getId());
                    if (department.getName() == null) {
                        department.setName(wxDept.getName());
                    }
                    if (department.getOrder() == null) {
                        department.setOrder(wxDept.getOrder());
                    }
                    if (department.getParentid() == null) {
                        department.setParentid(wxDept.getParentid());
                    }
                }
                deptMap.put(department.getId(), department);
                wxSyncData.setDepartment(department);
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_CREATE_PARTY){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_DEPT_ADD);
                department = departmentCloneable.clone();
                department.setId(contact.getDepartment());
                department.setName(contact.getName());
                department.setOrder(contact.getOrder());
                department.setParentid(contact.getParentId());
                if (deptMap.containsKey(department.getId())) {
                    WxDept wxDept = deptMap.get(department.getId());
                    if (department.getName() == null) {
                        department.setName(wxDept.getName());
                    }
                    if (department.getOrder() == null) {
                        department.setOrder(wxDept.getOrder());
                    }
                    if (department.getParentid() == null) {
                        department.setParentid(wxDept.getParentid());
                    }
                }
                deptMap.put(department.getId(), department);
                wxSyncData.setDepartment(department);
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_DELETE_PARTY){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_DEPT_DEL);
                wxSyncData.setDepartment_id(contact.getDepartment());
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_TAG_USER_IN){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_USER_IN);
                try {
                    WxUser wxUser = WxUserService.getUser(contact.getUserid(), corpId, null, agentCode);
                    if (wxUser == null) {
                        datas[i] = null;
                        continue;
                    }
                    wxSyncData.setUser(wxUser);
                } catch (Exception e) {
                    logger.error("ContactSyncThread getUser " + corpId, e);
                    ExceptionCenter.addException(e, "ContactSyncThread getUser @sqh", corpId+",wxUserIds=" + contact.getUserid());
                    wxSyncData.setType(0);
                } catch (BaseException e) {
                    logger.error("ContactSyncThread getUser " + corpId, e);
                    ExceptionCenter.addException(e, "ContactSyncThread getUser @sqh", corpId+",wxUserIds=" + contact.getUserid());
                    wxSyncData.setType(0);
                }
            }
            else if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_TAG_PARTY_IN){
                wxSyncData.setType(ContactSyncStatus.SYNC_TYPE_TAG_PARTY_IN);
                wxSyncData.setDepartment_id(contact.getDepartment());
                wxSyncData.setTagid(contact.getParentId());
            }
            else {
                if(contact.getChangeType() == ContactDictUtil.CHANGE_CONTACT_TAG_USER_OUT){
                    if (updateAgentCodeRangeMap == null) {
                        updateAgentCodeRangeMap = new HashMap<String, TbQyExperienceAgentPO>(20);
                    }
                    for (AgentRange agentRange : agentList) {
                        //判断哪个agentCode，如果这个应用的可见范围有这个标签，加入到待重置标签队列
                        if (!updateAgentCodeRangeMap.containsKey(agentRange.getAgentCode())
                                && !agentRange.isAllRange() && agentRange.getWxTagId() != null
                                && agentRange.getWxTagId().contains(contact.getParentId())) {
                            updateAgentCodeRangeMap.put(agentRange.getAgentCode(), agentRange.getPo());
                        }
                    }
                }
                datas[i] = null;
                continue;
            }
            datas[i] = wxSyncData;
        }
        syncVO.setData(datas);
        syncVO.setNext_seq(nextSeq);
        //是否最后一个
        syncVO.setIs_last((size > listSize || !isRunAllData) ? 1 : 0);
        syncVO.setExecuteAll(isRunAllData);
        syncVO.setNext_offset(nextOffset);
        return syncVO;
    }
    /**
     * 去掉重复更新导致的重复数据
     * @param datas
     * @author sunqinghai
     * @date 2016 -10-14
     */
    private void dealWxSyncData(WxSyncData[] datas){
        int size = datas.length;
        Set<String> userIdSet = new HashSet<String>(100);
        Set<String> deptIdSet = new HashSet<String>(20);
        //Set<String> delUserIdSet = new HashSet<String>(20);
        Set<String> delDeptIdSet = new HashSet<String>(20); //删除部门的id
        Set<String> wxUserIds = new HashSet<String>(datas.length);
        for(int i = size-1; i>=0; i--){//从最后一个开始，保留最新的
            WxSyncData date = datas[i];
            if (date == null) {
                continue;
            }
            if(date.getType() == ContactSyncStatus.SYNC_TYPE_USER_IN){
                //后面会被删除，或者后面会更新，此处就去掉，避免重复操作
                if (!userIdSet.add(date.getUser().getUserid())) { //如果后面有新增或者用户进入可见范围，之前的就都去掉，防止重复操作
                    datas[i] = null;
                    continue;
                }
            }
            else if(date.getType() == ContactSyncStatus.SYNC_TYPE_USER_UPDATE){
                //如果有现在已经删除了，或者后面有更新，之前的更新记录就都去掉
                if (!userIdSet.add(date.getUser().getUserid())) {
                    datas[i] = null;
                    continue;
                }
            }
            else if(date.getType() == ContactSyncStatus.SYNC_TYPE_USER_DEL){
                if(!userIdSet.add(date.getUserid())){ //如果后面有新增，前面的删除就去掉，现象是先删除，再新增用户
                    datas[i] = null;
                    continue;
                }
            }
            else if(date.getType() == ContactSyncStatus.SYNC_TYPE_DEPT_UPDATE){
                if(delDeptIdSet.contains(date.getDepartment().getId()) || !deptIdSet.add(date.getDepartment().getId())){
                    datas[i] = null;
                    continue;
                }
            }
            else if(date.getType() == ContactSyncStatus.SYNC_TYPE_DEPT_ADD || date.getType() == ContactSyncStatus.SYNC_TYPE_DEPT_IN){
                if(delDeptIdSet.contains(date.getDepartment().getId())){
                    datas[i] = null;
                    continue;
                }
            }
            else if(date.getType() == ContactSyncStatus.SYNC_TYPE_DEPT_DEL){
                delDeptIdSet.add(date.getDepartment_id());
                continue;
            }
            /*else if (date.getType() == ContactSyncStatus.SYNC_TYPE_TAG_PARTY_IN) {
                if (wxTagDeptIdSet == null) {
                    wxTagDeptIdSet = new HashSet<String>(10);
                }
                if (!wxTagDeptIdSet.add(date.getDepartment_id())) {
                    datas[i] = null;
                    continue;
                }
            }*/
            if (!StringUtil.isNullEmpty(date.getUserid())) {
                if (!syncUserMap.containsKey(date.getUserid())) {
                    wxUserIds.add(date.getUserid());
                }
            }
            else if (null != date.getUser()) {
                if (!syncUserMap.containsKey(date.getUser().getUserid())) {
                    wxUserIds.add(date.getUser().getUserid());
                }
            }
        }
        try {
            List<TbQyUserInfoPO> list = contactService.getUserInfoPOByWxUserIds(corpId, new ArrayList<String>(wxUserIds));
            if (!AssertUtil.isEmpty(list)) {
                TbQyUserInfoPO user;
                for (TbQyUserInfoPO po : list) {
                    user = syncUserMap.get(po.getWxUserId());
                    //原有不存在或者用户为离职状态
                    if (user == null || ContactDictUtil.USER_STAtUS_LEAVE == user.getUserStatus() ) {
                        syncUserMap.put(po.getWxUserId(), po);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread getUserInfoPOByWxUserIds " + corpId, e);
            ExceptionCenter.addException(e, "ContactSyncThread getUserInfoPOByWxUserIds @sqh", corpId+",wxUserIds=" + wxUserIds.size());
        }
    }

    /**
     * 用户进入
     * @param agentList 应用list
     * @param user 微信用户信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void userIn(List<AgentRange> agentList, WxUser user) {
        try {
            //如果没有同步过所有，或者用户不存在，或者用户是离职状态，重新新增或者更新
            if (!isSyncAllRange &&
                    (!syncUserMap.containsKey(user.getUserid()) || ContactUtil.USER_STAtUS_LEAVE.equals(syncUserMap.get(user.getUserid()).getUserStatus()))) {//如果用户已存在
                TbQyUserInfoPO vo = contactService.findUserInfoPOByWxUserId(user.getUserid(), corpId);
                if(vo == null) { //需要新增用户
                    if (addUserSet == null || !addUserSet.contains(user.getUserid())) {
                        syncUserMap.put(user.getUserid(), userAdd(agentList, user));
                    }
                } else { //如果已存在，不操作
                    syncUserMap.put(user.getUserid(), vo);
                    if (isFirst) {
                        userUpdate(agentList, user);
                    }
                }
            }
            addAgentRange(agentList, user);
        } catch (BaseException e) {
            logger.error("ContactSyncThread userIn " + user.toString(), e);
            ExceptionCenter.addException(e, "ContactSyncThread userIn @sqh", corpId+",user="+user.toString());
        } catch (Exception e) {
            logger.error("ContactSyncThread userIn" + user.toString(), e);
            ExceptionCenter.addException(e, "ContactSyncThread userIn @sqh", corpId+",user="+user.toString());
        }
    }

    /**
     * 用户更新
     * @param agentList 应用list
     * @param user 微信用户信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void userUpdate(List<AgentRange> agentList, WxUser user){
        if (isSyncAllRange) {
            return;
        }
        try {
            TbQyUserInfoPO vo = syncUserMap.get(user.getUserid());
            if(vo == null){//如果用户不存在
                vo = contactService.findUserInfoPOByWxUserId(user.getUserid(), corpId);
                if(vo != null){//如果已存在，执行更新
                    syncUserMap.put(user.getUserid(), vo);
                }
            }
            if(vo == null){//需要新增用户
                syncUserMap.put(user.getUserid(), userAdd(agentList, user));
                addAgentRange(agentList, user);
            }
            else if(updateUserSet.add(vo.getUserId())){ //如果已经更新过，就不需要再次更新了
                updateUser(agentList, user, vo);
            }
            //企业微信修改了部门后需要变更应用的可见范围
            if (isQiyeweixin && user.getDepartment() != null) {
                addAgentRange(agentList, user);
            }
        } catch (BaseException e) {
            logger.error("ContactSyncThread userUpdate"+user.toString(), e);
            ExceptionCenter.addException(e, "ContactSyncThread userUpdate @sqh", corpId+",user="+user.toString());
        } catch (Exception e) {
            logger.error("ContactSyncThread userUpdate"+user.toString(), e);
            ExceptionCenter.addException(e, "ContactSyncThread userUpdate @sqh", corpId+",user="+user.toString());
        }
    }

    /**
     * 是否全部分可见
     * @param agentList 应用list
     * @return 返回数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private boolean isRangAll(List<AgentRange> agentList){
        if (agentList == null) {
            return false;
        }
        Boolean b = suiteRangeAll.get(agentList.get(0).getSuiteId());
        if(null != b){
            return b;
        }
        for(AgentRange ar : agentList){
            if(ar.isAllRange()){
                suiteRangeAll.put(ar.getSuiteId(),true);
                return true;
            }
        }
        suiteRangeAll.put(agentList.get(0).getSuiteId(),false);
        return false;
    }

    /**
     * 获取微信用户的部门信息
     * @param agentList 应用list
     * @param dep 部门id
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private List<String> getWxUserDeptIds(List<AgentRange> agentList, List dep) {
        if (dep == null || dep.size() == 0) {
            return null;
        }
        int size = dep.size();
        TbDepartmentInfoPO rootDepartment;
        List<String> detId = new ArrayList<String>(10);
        // 处理部门，不存在的部门自动新建
        for (int i = 0; i < size; i++) {
            String wxDeptId = dep.get(i).toString();
            if (org.getWxId().equals(wxDeptId)) {
                continue;
            }
            rootDepartment = deptWxIdMap.get(wxDeptId);
            if (rootDepartment == null) {//如果部门不存在迭代新增
                try {
                    rootDepartment = deptAdd(agentList,wxDeptId);//迭代新增父部门
                } catch (BaseException e) {
                    logger.error("ContactSyncThread getWxUserDeptIds "+wxDeptId, e);
                    ExceptionCenter.addException(e, "ContactSyncThread getWxUserDeptIds @sqh", corpId+",wxDeptId="+wxDeptId);
                } catch (Exception e) {
                    logger.error("ContactSyncThread getWxUserDeptIds "+wxDeptId, e);
                    ExceptionCenter.addException(e, "ContactSyncThread getWxUserDeptIds @sqh", corpId+",wxDeptId="+wxDeptId);
                }
            }
            if (rootDepartment != null) {
                detId.add(rootDepartment.getId());
            }
        }
        return detId;
    }

    /**
     * 用户删除
     * @param agentList 应用list
     * @param wxUserId 微信userid
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void userDel(List<AgentRange> agentList, String wxUserId){
        if (isSyncAllRange) {
            return;
        }
        try {
            TbQyUserInfoPO po = syncUserMap.get(wxUserId);
            if(po == null){ //如果用户已存在
                po = contactService.findUserInfoPOByWxUserId(wxUserId, corpId);
                if (po == null) {
                    return;
                }
                syncUserMap.put(wxUserId, po);
            }
            if(po != null && !ContactUtil.USER_STAtUS_LEAVE.equals(po.getUserStatus())){
                po.setLeaveTime(new Date());
                po.setLeaveRemark("微信通讯录人员中已删除");
                List<String> deptIds = contactService.leaveUser(po, null, false, null);
                po.setUserStatus(ContactUtil.USER_STAtUS_LEAVE);
                if(leavUserList == null){
                    leavUserList = new ArrayList<TbQyUserInfoPO>(20);
                }
                leavUserList.add(po);
                if (deptIds != null) {
                    if(delDeptRefUserMap == null){
                        delDeptRefUserMap = new HashMap<String, List<String>>(100);
                    }
                    UserInfoChangeNotifier.getDeptUserRef(deptIds, po.getUserId(), delDeptRefUserMap);
                }
            }
            userOut(agentList,wxUserId);
        } catch (BaseException e) {
            logger.error("ContactSyncThread userDel "+wxUserId, e);
            ExceptionCenter.addException(e, "ContactSyncThread userDel @sqh", corpId+",wxUserId="+wxUserId);
        } catch (Exception e) {
            logger.error("ContactSyncThread userDel "+wxUserId, e);
            ExceptionCenter.addException(e, "ContactSyncThread userDel @sqh", corpId+",wxUserId="+wxUserId);
        }
    }

    /**
     * 部门进入
     * @param agentList  应用list
     * @param department 微信部门信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void deptIn(List<AgentRange> agentList, WxDept department){
        if (isSyncAllRange) {
            return;
        }
        try {
            if(org.getWxId().equals(department.getId())){
                return;
            }
            TbDepartmentInfoPO po = null;
            if(!deptWxIdMap.containsKey(department.getId())){//如果部门不存在
                po = deptAdd(agentList,department);
            }
            else if(isFirst){
                po = deptUpdate(agentList, department);
            }
            if (po != null) {
                if (inDeptList == null) {
                    inDeptList = new ArrayList<TbDepartmentInfoPO>();
                }
                inDeptList.add(po);
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread deptIn" + corpId, e);
            ExceptionCenter.addException(e, "ContactSyncThread deptIn @sqh", corpId+",department="+department.toString());
        } catch (BaseException e) {
            logger.error("ContactSyncThread deptIn" + corpId, e);
            ExceptionCenter.addException(e, "ContactSyncThread deptIn @sqh", corpId+",department="+department.toString());
        }
    }

    /**
     * 部门更新
     * @param agentList 应用list
     * @param department 微信部门信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private TbDepartmentInfoPO deptUpdate(List<AgentRange> agentList, WxDept department){
        if (isSyncAllRange) {
            return null;
        }
        try {
            if(org.getWxId().equals(department.getId())){
                return null;
            }
            if(!deptWxIdMap.containsKey(department.getId())){//如果部门不存在
                return deptAdd(agentList,department);
            }
            else if(updateDeptSet.add(department.getId())){//需要更新
                TbDepartmentInfoPO tbDepartmentInfoPO = deptWxIdMap.get(department.getId());
                if (StringUtil.isNullEmpty(tbDepartmentInfoPO.getDepartmentName())
                        || StringUtil.isNullEmpty(tbDepartmentInfoPO.getWxParentid())) {
                    getDeptInfo(agentList, department);
                }
                if (!WxqyhStringUtil.strCstrIgnoreSecondEmpty(tbDepartmentInfoPO.getDepartmentName(), department.getName())
                        || !WxqyhStringUtil.strCstrIgnoreSecondEmpty(tbDepartmentInfoPO.getWxParentid(), department.getParentid())
                        || !IntegerUtil.equalsIgnoreSecondEmpty(tbDepartmentInfoPO.getShowOrder(), department.getOrder())) {
                    TbDepartmentInfoPO po = new TbDepartmentInfoPO();
                    String fullName;
                    boolean parentIdIsNotNull = !StringUtil.isNullEmpty(department.getParentid());
                    if(WxqyhStringUtil.strCstrIgnoreSecondEmpty(tbDepartmentInfoPO.getWxParentid(), department.getParentid())){
                        if (parentIdIsNotNull) {
                            TbDepartmentInfoPO parentDepart = deptWxIdMap.get(department.getParentid());
                            if(parentDepart == null) {//如果部门不存在
                                fullName = "";
                                po.setParentDepart("");
                                tbDepartmentInfoPO.setParentDepart(po.getParentDepart());
                            }
                            else {
                                fullName = StringUtil.isNullEmpty(parentDepart.getDeptFullName()) ? "" : (parentDepart.getDeptFullName()+DepartmentUtil.DEPT_NAME_SPLIT);
                            }
                        }
                        else {
                            int index = tbDepartmentInfoPO.getDeptFullName().lastIndexOf("->");
                            fullName = index<0?"":tbDepartmentInfoPO.getDeptFullName().substring(0,index+2);//自带箭头
                        }
                    }
                    else { //如果部门的父部门有变
                        TbDepartmentInfoPO parentDepart = deptWxIdMap.get(department.getParentid());
                        if(parentDepart == null){//如果部门不存在
                            try {
                                parentDepart = deptAdd(agentList,department.getParentid());//迭代新增父部门
                            } catch (BaseException e) {
                                logger.error("ContactSyncThread deptAdd "+department.getParentid(),e);
                                ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",wxDeptParentId="+department.getParentid());
                            } catch (Exception e) {
                                logger.error("ContactSyncThread deptAdd "+department.getParentid(),e);
                                ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",wxDeptParentId="+department.getParentid());
                            }
                        }
                        if(parentDepart == null){
                            fullName = "";
                            po.setParentDepart("");
                            tbDepartmentInfoPO.setParentDepart(po.getParentDepart());
                        }
                        else{
                            fullName = StringUtil.isNullEmpty(parentDepart.getDeptFullName()) ? "" : (parentDepart.getDeptFullName()+DepartmentUtil.DEPT_NAME_SPLIT);
                            po.setParentDepart(parentDepart.getId());
                            tbDepartmentInfoPO.setParentDepart(po.getParentDepart());
                            if (AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission())) {//判断权限是否超过父部门的权限
                                tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                                po.setPermission(parentDepart.getPermission());
                            } else {
                                if (!AssertUtil.isEmpty(parentDepart.getPermission())) {
                                    if (tbDepartmentInfoPO.getPermission().compareTo(parentDepart.getPermission()) < 0) {//子部门权限不能比父部门权限大
                                        tbDepartmentInfoPO.setPermission(parentDepart.getPermission());
                                        po.setPermission(parentDepart.getPermission());
                                    }
                                }
                            }
                        }
                    }
                    po.setId(tbDepartmentInfoPO.getId());
                    if (!StringUtil.isNullEmpty(department.getName())) {
                        po.setDepartmentName(department.getName());
                        po.setDeptFullName(fullName+department.getName());
                    }
                    if (parentIdIsNotNull) {
                        po.setWxParentid(department.getParentid());
                    }
                    boolean orderIsNotNull = department.getOrder() != null;
                    if (orderIsNotNull) {
                        po.setShowOrder(department.getOrder());
                    }
                    departmentService.updatePO(po, false);
                    if(po.getDeptFullName() != null && !po.getDeptFullName().equals(tbDepartmentInfoPO.getDeptFullName())){
                        deptUpdateChild(po.getDeptFullName(), tbDepartmentInfoPO.getDeptFullName(), po.getPermission());
                        tbDepartmentInfoPO.setDeptFullName(po.getDeptFullName());
                        tbDepartmentInfoPO.setDepartmentName(po.getDepartmentName());
                    }
                    if (parentIdIsNotNull) {
                        tbDepartmentInfoPO.setWxParentid(po.getWxParentid());
                    }
                    if (orderIsNotNull) {
                        tbDepartmentInfoPO.setShowOrder(po.getShowOrder());
                    }
                    DeptSyncInfoVO vo = new DeptSyncInfoVO();
                    BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                    vo.setUpdateParentDepart(false);
                    if (updateDeptList == null) {
                        updateDeptList = new ArrayList<DeptSyncInfoVO>(10);
                    }
                    updateDeptList.add(vo);
                    deptWxIdMap.put(tbDepartmentInfoPO.getWxId(),tbDepartmentInfoPO);
                }
                return tbDepartmentInfoPO;
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread deptUpdate "+department.toString(),e);
            ExceptionCenter.addException(e,"ContactSyncThread deptUpdate @sqh",corpId+",department="+department.toString());
        } catch (BaseException e) {
            logger.error("ContactSyncThread deptUpdate "+department.toString(),e);
            ExceptionCenter.addException(e,"ContactSyncThread deptUpdate @sqh",corpId+",department="+department.toString());
        }
        return null;
    }

    /**
     * 更新部门的子部门名称
     * @param newFullName 新的部门全称
     * @param oldFullName 旧的部门全称
     * @param permission 权限
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-14
     */
    private void deptUpdateChild(String newFullName, String oldFullName, String permission) throws Exception, BaseException {
        // 更新此部门下的子部门的部门全称
        List<TbDepartmentInfoPO> clist = departmentService.getChildDeptByFullName(org.getOrgId(), oldFullName + "->%");
        if (clist != null && clist.size() > 0) {
            if (updateDeptList == null) {
                updateDeptList = new ArrayList<DeptSyncInfoVO>(10);
            }
            TbDepartmentInfoPO permissionDept;
            StringBuffer sb = new StringBuffer();
            DeptSyncInfoVO vo;
            for (TbDepartmentInfoPO po : clist) {
                permissionDept = new TbDepartmentInfoPO();
                permissionDept.setId(po.getId());
                sb.setLength(0);
                sb.append(po.getDeptFullName());
                // 只处理子部门
                permissionDept.setDeptFullName(sb.replace(0, oldFullName.length(), newFullName).toString());
                if (AssertUtil.isEmpty(po.getPermission())) {//判断权限是否超过父部门的权限
                    permissionDept.setPermission(permission);
                    po.setPermission(permission);
                } else {
                    if (!AssertUtil.isEmpty(permission)) {
                        if (po.getPermission().compareTo(permission) < 0) {//子部门权限不能比父部门权限大
                            permissionDept.setPermission(permission);
                            po.setPermission(permission);
                        }
                    }
                }
                departmentService.updatePO(permissionDept, false);
                po.setDeptFullName(permissionDept.getDeptFullName());
                vo = new DeptSyncInfoVO();
                BeanHelper.copyBeanProperties(vo, permissionDept);
                vo.setUpdateParentDepart(false);
                vo.setDepartmentName(po.getDepartmentName());
                updateDeptList.add(vo);
                deptWxIdMap.put(po.getWxId(),po);
            }
        }
    }

    /**
     * 部门退出可见范围
     * @param agentList 应用list
     * @param wxDeptId 微信部门id
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void deptOut(List<AgentRange> agentList, String wxDeptId){
        try {
            TbDepartmentInfoPO tbDepartmentInfoPO = deptWxIdMap.get(wxDeptId);
            if(tbDepartmentInfoPO == null){
                tbDepartmentInfoPO = departmentService.getDeptByWeixin(org.getOrgId(),wxDeptId);
            }

            if(tbDepartmentInfoPO != null){
                //获取部门下的所有人员，并移出可见范围
                List<String> fullNames = new ArrayList<String>(1);
                fullNames.add(tbDepartmentInfoPO.getDeptFullName());
                List<String> deptIds = departmentMgrService.getChildDeptIdsByFullName(org.getOrgId(),fullNames);
                deptIds.add(tbDepartmentInfoPO.getId());
                List<String> userIds = contactService.findDeptUserIdAllByDeptIds(deptIds);

            }
        } catch (BaseException e) {
            logger.error("ContactSyncThread deptOut "+wxDeptId,e);
            ExceptionCenter.addException(e,"ContactSyncThread deptOut @sqh",corpId+",wxDeptId="+wxDeptId);
        } catch (Exception e) {
            logger.error("ContactSyncThread deptOut "+wxDeptId,e);
            ExceptionCenter.addException(e,"ContactSyncThread deptOut @sqh",corpId+",wxDeptId="+wxDeptId);
        }
    }

    /**
     * 部门删除
     * @param wxDeptId 微信部门id
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void deptDel(String wxDeptId){
        if (isSyncAllRange) {
            return;
        }
        try {
            TbDepartmentInfoPO tbDepartmentInfoPO = deptWxIdMap.get(wxDeptId);
            if(tbDepartmentInfoPO == null){
                tbDepartmentInfoPO = departmentService.getDeptByWeixin(org.getOrgId(),wxDeptId);
            }

            if(tbDepartmentInfoPO != null){
                /*String fullName = tbDepartmentInfoPO.getDeptFullName();
                //此部门下的人员，如果有人员，不能删除
                if (contactService.hasUsersByDepartIdAndFullName(org.getOrgId(), tbDepartmentInfoPO.getId(), tbDepartmentInfoPO.getDeptFullName())) {
                    logger.warn("deptDel删除部门【" + fullName + "】失败，失败原因：部门下存在用户，请手动删除或移动用户后再删除该部门");
                    return;
                }*/
                departmentService.delPO(tbDepartmentInfoPO);
                DeptSyncInfoVO vo = new DeptSyncInfoVO();
                BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
                if (delDeptList == null) {
                    delDeptList = new ArrayList<DeptSyncInfoVO>(20);
                }
                delDeptList.add(vo);

                List<String> userDept = departmentService.getDeptUserRefByDeptId(tbDepartmentInfoPO.getId());
                if (userDept!=null) {
                    if(delUserDeptRefList == null){
                        delUserDeptRefList = new ArrayList<String>(100);
                    }
                    delUserDeptRefList.addAll(userDept);
                }
                logger.warn("deptDel同步删除部门，corpId：" + corpId + ",orgId" + org.getOrgId() + "," + tbDepartmentInfoPO.getDeptFullName() + "|" + tbDepartmentInfoPO.getId());
            }
        } catch (BaseException e) {
            logger.error("ContactSyncThread deptDel "+wxDeptId,e);
            ExceptionCenter.addException(e,"ContactSyncThread deptDel @sqh",corpId+",wxDeptId="+wxDeptId);
        } catch (Exception e) {
            logger.error("ContactSyncThread deptDel "+wxDeptId,e);
            ExceptionCenter.addException(e,"ContactSyncThread deptDel @sqh",corpId+",wxDeptId="+wxDeptId);
        }
    }

    /**
     * 部门新增
     * @param agentList 应用list
     * @param wxDeptId 部门id
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private TbDepartmentInfoPO deptAdd(List<AgentRange> agentList, String wxDeptId) throws Exception, BaseException {
        if(wxDeptId.equals(org.getWxId())){
            return null;
        }
        List<WxDept> list = WxDeptService.getDept(org.getCorpId(),wxDeptId,org.getOrgId(),agentList.get(0).getAgentCode());
        if(list!=null && list.size()>0){
            for(WxDept wxDept: list){
                if(wxDept.getId().equals(wxDeptId)){
                    return deptAdd(agentList,wxDept);//迭代新增父部门
                }
            }
        }
        return null;
    }

    /**
     * 部门进入标签管理范围
     * @param agentList
     * @param wxDeptId
     * @param wxTagId
     * @author sunqinghai
     * @date 2017 -6-28
     */
    private void tagDeptIn(List<AgentRange> agentList, String wxDeptId, String wxTagId) {
        try {
            //首先初始化部门数据
            String agentCode = agentList.get(0).getAgentCode();
            WxDept wxDept = WxDeptService.getWxDept(corpId, wxDeptId, null, agentCode);
            if (wxDept != null) {
                deptIn(agentList, wxDept);
                for (AgentRange agentRange : agentList) {
                    //判断哪个agentCode，如果这个应用的可见范围有这个标签，将
                    if (!agentRange.isAllRange() && agentRange.getWxTagId() != null
                            && agentRange.getWxTagId().contains(wxTagId)) {
                        if (AssertUtil.isEmpty(agentRange.getWxDeptId())) {
                            agentRange.setWxDeptId(new HashSet<String>(10));
                        }
                        agentRange.getWxDeptId().add(wxDeptId);
                        TbDepartmentInfoPO dept = deptWxIdMap.get(wxDeptId);
                        if (dept != null) {
                            if (AssertUtil.isEmpty(agentRange.getDeptFullNameSet())) {
                                agentRange.setDeptFullNameSet(new HashSet<String>(10));
                            }
                            agentRange.getDeptFullNameSet().add(dept.getDeptFullName());
                        }
                    }
                }
            }
            //获取部门内的用户信息，并初始化
            List<WxUser> list = WxUserService.getUsers(corpId, wxDeptId, null, agentCode);
            if (AssertUtil.isEmpty(list)) {
                return;
            }
            for (WxUser wxUser : list) {
                userIn(agentList, wxUser);
            }
        } catch (Exception e) {
            logger.error("ContactSyncThread tagDeptIn " + wxDeptId, e);
            ExceptionCenter.addException(e,"ContactSyncThread tagDeptIn @sqh", corpId+",wxDeptId="+wxDeptId);
        } catch (BaseException e) {
            logger.error("ContactSyncThread tagDeptIn " + wxDeptId, e);
            ExceptionCenter.addException(e,"ContactSyncThread tagDeptIn @sqh", corpId+",wxDeptId="+wxDeptId);
        }
    }

    /**
     * 部门新增
     * @param agentList 应用list
     * @param department 微信部门信息
     * @return 返回数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private TbDepartmentInfoPO deptAdd(List<AgentRange> agentList, WxDept department) throws Exception, BaseException {
        TbDepartmentInfoPO po;
        if(org.getWxId().equals(department.getParentid())){
            po = new TbDepartmentInfoPO();
            po.setId("");
            po.setWxId(org.getWxId());
        }
        else{
            getDeptInfo(agentList, department);
            po = deptWxIdMap.get(department.getParentid());
            if(po == null){//如果用户已存在
                try {
                    po = deptAdd(agentList,department.getParentid());//迭代新增父部门
                } catch (BaseException e) {
                    logger.error("ContactSyncThread deptAdd "+department.getParentid(),e);
                    ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",wxDeptParentId="+department.getParentid());
                } catch (Exception e) {
                    logger.error("ContactSyncThread deptAdd "+department.getParentid(),e);
                    ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",wxDeptParentId="+department.getParentid());
                }
                if(po==null){
                    po = new TbDepartmentInfoPO();
                    po.setId("");
                    po.setWxId(org.getWxId());
                }
            }
        }
        try {
            TbDepartmentInfoPO tbDepartmentInfoPO = new TbDepartmentInfoPO();
            tbDepartmentInfoPO.setId(UUID32.getID());
            tbDepartmentInfoPO.setParentDepart(po.getId());
            tbDepartmentInfoPO.setCreateTime(new Date());
            tbDepartmentInfoPO.setOrgId(org.getOrgId());
            tbDepartmentInfoPO.setShowOrder(department.getOrder());
            tbDepartmentInfoPO.setDepartmentName(department.getName());
            tbDepartmentInfoPO.setDeptFullName(StringUtil.isNullEmpty(po.getDeptFullName())? department.getName():(po.getDeptFullName() + "->"+department.getName()));
            tbDepartmentInfoPO.setWxParentid(department.getParentid());
            tbDepartmentInfoPO.setWxId(department.getId());
            tbDepartmentInfoPO.setPermission(po.getPermission());
            departmentService.insertPO(tbDepartmentInfoPO, false);
            deptWxIdMap.put(department.getId(),tbDepartmentInfoPO);
            DeptSyncInfoVO vo = new DeptSyncInfoVO();
            BeanHelper.copyBeanProperties(vo, tbDepartmentInfoPO);
            if (addDeptList == null) {
                addDeptList = new ArrayList<DeptSyncInfoVO>(20);
            }
            addDeptList.add(vo);
            for(AgentRange ar : agentList){
                if(!ar.isAllUserVisible() && ar.getWxDeptId() != null && ar.getWxDeptId().contains(tbDepartmentInfoPO.getWxId())){//如果可见范围有此部门，将其增加到全称中
                    ar.getDeptFullNameSet().add(tbDepartmentInfoPO.getDeptFullName());
                }
            }
            return tbDepartmentInfoPO;
        } catch (BaseException e) {
            logger.error("ContactSyncThread deptAdd "+department.toString(),e);
            ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",department="+department.toString());
        } catch (Exception e) {
            logger.error("ContactSyncThread deptAdd "+department.toString(),e);
            ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh",corpId+",department="+department.toString());
        }
        return null;
    }

    /**
     * 补充部门的信息
     * @param agentList
     * @param department
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -6-23
     */
    private void getDeptInfo(List<AgentRange> agentList, WxDept department) throws Exception, BaseException {
        if (isQiyeweixin && (StringUtil.isNullEmpty(department.getName())
                || StringUtil.isNullEmpty(department.getParentid()))) {
            WxDept wxDept = WxDeptService.getWxDept(org.getCorpId(), department.getId(), org.getOrgId(), agentList.get(0).getAgentCode());
            if (wxDept != null) {
                department.setName(wxDept.getName());
                department.setParentid(wxDept.getParentid());
                department.setOrder(wxDept.getOrder());
            }
        }
    }

    /**
     * 加入应用可见范围
     * @param agentList 应用list
     * @param user 微信用户信息
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void addAgentRange(List<AgentRange> agentList, WxUser user) throws Exception, BaseException {
        if(!syncUserMap.containsKey(user.getUserid())){
            return;
        }
        agentFor:for(AgentRange ar : agentList){
            if(!ar.isAllUserVisible()){//如果不是所有人可见
                if(ar.getWxUserId() != null && ar.getWxUserId().contains(user.getUserid())){//如果用户在可见范围内
                    //加入应用可见范围list
                    insterAgentUserRef(ar,user.getUserid());
                    continue;
                }
                if((ar.isAllUserVisible() || ar.getDeptFullNameSet() != null) && user.getDepartment() !=null && user.getDepartment().size()>0){
                    TbDepartmentInfoPO po;
                    for(Object wxDeptId : user.getDepartment()){
                        String wxDeptIdStr = wxDeptId.toString();
                        if(wxDeptIdStr.equals(org.getWxId())){
                            continue;
                        }
                        po = deptWxIdMap.get(wxDeptIdStr);
                        if(po==null){
                            po = departmentService.getDeptByWeixin(org.getOrgId(), wxDeptIdStr);
                        }
                        if(po==null){//如果本地没有找到，迭代新增部门信息
                            po = deptAdd(agentList,wxDeptIdStr);
                        }
                        else{
                            deptWxIdMap.put(wxDeptIdStr,po);
                        }
                        if(po==null){
                            continue;
                        }
                        if(!ar.isAllUserVisible() && ar.getDeptFullNameSet().contains(po.getDeptFullName())){
                            //加入应用可见范围list
                            insterAgentUserRef(ar,user.getUserid());
                            continue agentFor;
                        }
                        String[] split = po.getDeptFullName().split(deptSplit);
                        StringBuilder sb = new StringBuilder(split[0]);
                        if(!ar.isAllUserVisible() && ar.getDeptFullNameSet().contains(sb.toString())){
                            //加入应用可见范围list
                            insterAgentUserRef(ar,user.getUserid());
                            continue agentFor;
                        }
                        int i=1;
                        while(i<split.length){
                            sb.append(deptSplit).append(split[i]);
                            if(!ar.isAllUserVisible() && ar.getDeptFullNameSet().contains(sb.toString())){
                                //加入应用可见范围list
                                insterAgentUserRef(ar,user.getUserid());
                                continue agentFor;
                            }
                            i++;
                        }
                    }
                }
            }
        }
    }

    private void addAgentAllRange(List<AgentRange> thisAgentList, List<TbDepartmentInfoPO> deptList) throws Exception, BaseException {
        List<AgentRange> agentList;
        if (isHasAllRange) {
            agentList = getAllAgentRange();
        }
        else {
            agentList = thisAgentList;
        }
        if (agentList == null || agentList.size()==0) {
            return;
        }
        if (deptList == null || deptList.size()==0) {
            return;
        }
        Map<String, Set<String>> parentWxIds = new HashMap<String, Set<String>>(deptList.size());
        for(TbDepartmentInfoPO po : deptList){
            Set<String> wxIdSet = new HashSet<String>();
            getParentPO(po.getWxId(), wxIdSet);
            parentWxIds.put(po.getId(), wxIdSet);
        }
        List<String> deptFullNameList = new ArrayList<String>(deptList.size());
        List<String> deptIdList = new ArrayList<String>(deptList.size());
        for(AgentRange ar : agentList){
            try {
                deptFullNameList.clear();
                deptIdList.clear();
                Set<String> wxDeptIdSet = ar.getWxDeptId();
                if(wxDeptIdSet == null || wxDeptIdSet.size() == 0){
                    continue;
                }
                deptFor : for(TbDepartmentInfoPO po : deptList){
                    Set<String> wxIdSet = parentWxIds.get(po.getId());
                    for (String wxId : wxIdSet) {
                        if (wxDeptIdSet.contains(wxId)) { //如果可见范围包括这个部门，将部门下的人员加到应用可见范围表
                            deptFullNameList.add(po.getDeptFullName());
                            deptIdList.add(po.getId());
                            continue deptFor;
                        }
                    }
                }
                if (deptFullNameList.size() >0 ) { //如果这个应用有需要加入可见范围的部门
                    List<String> childDeptIdList = departmentMgrService.getChildDeptIdsByFullName(org.getOrgId(), deptFullNameList);
                    if(childDeptIdList != null && childDeptIdList.size()>0){
                        deptIdList.addAll(childDeptIdList);
                    }

                    //获取这些部门下的人员id
                    List<String> userIdList = contactService.findDeptUserIdAllByDeptIds(deptIdList);
                    if(userIdList != null && userIdList.size()>0){
                        Set<String> userIdSet = new HashSet<String>();
                        userIdSet.addAll(deptIdList);
                        addAgentRange(ar, userIdSet);
                    }
                }
            } catch (BaseException e) {
                logger.error("ContactSyncThread addAgentAllRange " + ar.getAgentCode(), e);
                ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh", corpId + ",department=" +ar.getAgentCode());
            } catch (Exception e) {
                logger.error("ContactSyncThread addAgentAllRange "+ ar.getAgentCode(), e);
                ExceptionCenter.addException(e,"ContactSyncThread deptAdd @sqh", corpId + ",department=" +ar.getAgentCode());
            }
        }
    }

    private void getParentPO(String wxId, Set<String> wxIdSet){
        if (org.getWxId().equals(wxId)) {
            return;
        }
        TbDepartmentInfoPO po = deptWxIdMap.get(wxId);
        if (po == null) {
            return;
        }
        wxIdSet.add(wxId);
        getParentPO(po.getWxParentid(), wxIdSet);
    }

    /**
     * 加入应用可见范围
     * @param ar 应用信息
     * @param userIds 用户信息
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void addAgentRange(AgentRange ar, Set<String> userIds) throws Exception, BaseException {
        if(userIds !=null && userIds.size()>0){
            List<String> userIdList = experienceapplicationService.getAgentUserIdByAgentCode(org.getOrgId(), ar.getAgentCode());
            if(userIdList == null || userIdList.size()==0){
                for(String userId : userIds){
                    insterAgentUserRefByUserId(ar, userId);
                }
            }
            else {
                Set<String> userIdSet = new HashSet<String>(userIdList);
                for(String userId : userIds){
                    if(userIdSet.contains(userId)){//如果已在现有的可见范围内，跳过
                        continue;
                    }
                    insterAgentUserRefByUserId(ar, userId);
                }
            }
        }
    }

    /**
     * 用户离开可见范围
     * @param agentList 应用list
     * @param wxUserId wxUserId
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void userOut(List<AgentRange> agentList, String wxUserId) throws Exception, BaseException {
        if(!syncUserMap.containsKey(wxUserId)) {//如果用户已存在
            TbQyUserInfoPO vo = contactService.findUserInfoPOByWxUserId(wxUserId, corpId);
            if(vo==null){
                return;
            }
            else{
                syncUserMap.put(wxUserId,vo);
            }
        }

        List<String> angentCodes = getAgentCodes(agentList, false);
        if(angentCodes == null){
            return;
        }
        List<String> list = experienceapplicationService.getAgentUserRefByUserId(org.getOrgId(),syncUserMap.get(wxUserId).getUserId(),angentCodes);
        //加入待删除列表
        delAgentUserRef(list);

    }

    /**
     * 插入应用可见用户
     * @param ar 应用信息
     * @param wxUserId wxuserId
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void insterAgentUserRef(AgentRange ar, String wxUserId){
        insterAgentUserRefByUserId(ar, syncUserMap.get(wxUserId).getUserId());
    }

    /**
     * 插入应用可见用户
     * @param ar 应用信息
     * @param userId 用户id
     * @author sunqinghai
     * @date 2016 -11-1
     */
    private void insterAgentUserRefByUserId(AgentRange ar,String userId){
        if (StringUtil.isNullEmpty(userId)) {
            return;
        }
        TbQyAgentUserRefPO po = new TbQyAgentUserRefPO();
        po.setId(UUID32.getID());
        po.setCorpId(corpId);
        po.setAgentCode(ar.getAgentCode());
        po.setOrgId(org.getOrgId());
        po.setCreateTime(new Date());
        po.setUserId(userId);
        //po.setWxUserId(wxUserId);
        if(agentUserRefPOList == null){
            agentUserRefPOList = new ArrayList<TbQyAgentUserRefPO>(100);
        }
        agentUserRefPOList.add(po);
        updateAgentCodeSet.add(ar.getAgentCode());
    }

    /**
     * 删除应用的可见用户
     * @param list userid
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void delAgentUserRef(List<String> list){
        if(list == null || list.size()==0){
            return;
        }
        if(agentUserRefIdList == null){
            agentUserRefIdList = new ArrayList<String>(100);
        }
        agentUserRefIdList.addAll(list);
    }

    /**
     * 用户新增
     * @param agentList 应用list
     * @param user 微信用户信息
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private TbQyUserInfoPO userAdd(List<AgentRange> agentList, WxUser user) throws Exception, BaseException {
        getUserInfo(agentList, user);
        TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
        tbQyUserInfoPO.setId(ContactUtil.getUserId(corpId, user.getUserid()));
        tbQyUserInfoPO.setUserId(tbQyUserInfoPO.getId());
        ContactUtil.setUserInfoByWxUser(tbQyUserInfoPO, user);
        tbQyUserInfoPO.setCorpId(corpId);
        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
        tbQyUserInfoPO.setCreateTime(new Date());
        tbQyUserInfoPO.setOrgId(org.getOrgId());
        tbQyUserInfoPO.setIsConcerned("0");
        tbQyUserInfoPO.setUpdateTime(new Date());
        tbQyUserInfoPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
        //获取用户的部门
        List<String> detId = getWxUserDeptIds(agentList,user.getDepartment());
        if(AssertUtil.isEmpty(detId)){
            detId = getWxEmptyDept();
        }
        //contactService.insertUser(tbQyUserInfoPO, detId);
        if(addUserDeptRefList == null){
            addUserDeptRefList = new ArrayList<TbQyUserDepartmentRefPO>(100);
        }
        for(int i=0;i<detId.size();i++){
            String id = detId.get(i);
            TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
            po.setUserId(tbQyUserInfoPO.getUserId());
            po.setOrgId(tbQyUserInfoPO.getOrgId());
            po.setSort(i);
            po.setDepartmentId(id);
            po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
            addUserDeptRefList.add(po);
        }
        if (addUserList == null) {
            addUserList = new ArrayList<TbQyUserInfoPO>(100);
        }
        addUserList.add(tbQyUserInfoPO);
        if (addUserSet == null) {
            addUserSet = new HashSet<String>(100);
        }
        addUserSet.add(user.getUserid());
        if(userRefDeptMap == null){
            userRefDeptMap = new HashMap<String, List<String>>(100);
        }
        userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
        if(addDeptRefUserMap == null){
            addDeptRefUserMap = new HashMap<String, List<String>>(100);
        }
        UserInfoChangeNotifier.getDeptUserRef(detId, tbQyUserInfoPO.getUserId(), addDeptRefUserMap);
        logger.debug("sysUser添加联系人corpId：" + corpId + "," + user.toString());
        TbQyUserInfoPO po = new TbQyUserInfoPO();
        BeanHelper.copyProperties(po, tbQyUserInfoPO);
        return po;
    }

    /**
     * 更新用户信息
     * @param agentList 应用list
     * @param user 微信用户信息
     * @param vo 本地用户信息
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void updateUser(List<AgentRange> agentList, WxUser user, TbQyUserInfoPO vo) throws Exception, BaseException {
        if (StringUtil.isNullEmpty(vo.getPersonName())) {
            getUserInfo(agentList, user);
        }
        if (!StringUtil.isNullEmpty(user.getGender()) && !"1".equals(user.getGender()) && !"2".equals(user.getGender())) {
            user.setGender(null);
        }
        boolean[] isChange = WxUserService.verifyChangeIgnoreEmpty(vo, user);
        List<String> detId = getWxUserDeptIds(agentList,user.getDepartment());
        List<TbQyUserDepartmentRefPO> userDept = null;
        //判断是否需要更新，如果没有修改
        if (!isChange[0]) {
            userDept = departmentService.getDeptUserRefPOByUserId(vo.getUserId());
            if(!isChangeDept(detId,userDept,isRangAll(agentList))){
                return;
            }
        }
        TbQyUserInfoPO tbQyUserInfoPO = new TbQyUserInfoPO();
        tbQyUserInfoPO.setId(vo.getId());
        if (ContactUtil.USER_STAtUS_LEAVE.equals(vo.getUserStatus())) {//离职
            tbQyUserInfoPO.setUserStatus(ContactUtil.USER_STAtUS_INIT);
        }
        ContactUtil.setUserInfoIgnoreEmptyByWxUser(tbQyUserInfoPO, user, vo);
        tbQyUserInfoPO.setPinyin(PingYinUtil.getPingYin(tbQyUserInfoPO.getPersonName()));
        tbQyUserInfoPO.setOrgId(org.getOrgId());
        tbQyUserInfoPO.setUserId(vo.getUserId());
        //更新本地用户信息
        if(detId!=null && detId.size()>0) {
            boolean isRangAll = isRangAll(agentList);
            if(userDept == null){
                userDept = departmentService.getDeptUserRefPOByUserId(vo.getUserId());
            }
            Set<String> userDeptIdSet = new HashSet<String>(userDept.size()+5);
            for(TbQyUserDepartmentRefPO po : userDept){
                userDeptIdSet.add(po.getDepartmentId());
            }

            if(addUserDeptRefList == null){
                addUserDeptRefList = new ArrayList<TbQyUserDepartmentRefPO>(100);
            }
            if(delUserDeptRefList == null){
                delUserDeptRefList = new ArrayList<String>(100);
            }
            List<String> refsAdd = new ArrayList<String>(detId.size());
            List<String> refsDel = new ArrayList<String>(userDept.size());
            for(int i=0;i<detId.size();i++){
                String id = detId.get(i);
                if(isRangAll){
                    if(userDept.size()<=i){//如果超过了原来的大小
                        TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
                        po.setUserId(vo.getUserId());
                        po.setOrgId(vo.getOrgId());
                        po.setSort(i);
                        po.setDepartmentId(id);
                        po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
                        addUserDeptRefList.add(po);
                        refsAdd.add(id);
                        userDeptIdSet.add(id);
                    }
                    else if(!id.equals(userDept.get(i).getDepartmentId())){
                        refsDel.add(userDept.get(i).getDepartmentId());
                        delUserDeptRefList.add(userDept.get(i).getId());
                        userDeptIdSet.remove(userDept.get(i).getDepartmentId());
                        TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
                        po.setUserId(vo.getUserId());
                        po.setOrgId(vo.getOrgId());
                        po.setSort(i);
                        po.setDepartmentId(id);
                        po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
                        addUserDeptRefList.add(po);
                        refsAdd.add(id);
                        userDeptIdSet.add(id);
                    }
                }
                else{
                    if(userDeptIdSet.add(id)){
                        TbQyUserDepartmentRefPO po = new TbQyUserDepartmentRefPO();
                        po.setUserId(vo.getUserId());
                        po.setOrgId(vo.getOrgId());
                        po.setSort(i);
                        po.setDepartmentId(id);
                        po.setId(ContactUtil.getUserDeptRefId(po.getUserId(), po.getDepartmentId()));
                        addUserDeptRefList.add(po);
                        refsAdd.add(id);
                    }
                }
            }
            if(isRangAll && userDept.size()>detId.size()){//将超过现有部门长度的部门全都删掉
                for(int i= detId.size();i< userDept.size();i++){
                    refsDel.add(userDept.get(i).getDepartmentId());
                    delUserDeptRefList.add(userDept.get(i).getId());
                }
            }
            if(refsAdd.size()>0){
                if(addDeptRefUserMap == null){
                    addDeptRefUserMap = new HashMap<String, List<String>>(100);
                }
                UserInfoChangeNotifier.getDeptUserRef(refsAdd, vo.getUserId(), addDeptRefUserMap);
            }
            if(refsDel.size()>0){
                if(delDeptRefUserMap == null){
                    delDeptRefUserMap = new HashMap<String, List<String>>(100);
                }
                UserInfoChangeNotifier.getDeptUserRef(refsDel, vo.getUserId(), delDeptRefUserMap);
            }
            detId = new ArrayList<String>(userDeptIdSet);
        }
        else{
            if(userDept != null){
                detId = new ArrayList<String>(userDept.size());
                for(TbQyUserDepartmentRefPO po : userDept){
                    detId.add(po.getDepartmentId());
                }
            }
            else {
                detId = departmentService.getDeptUserRefByUserId(vo.getUserId());
            }
        }
        tbQyUserInfoPO.setUpdateTime(new Date());
        tbQyUserInfoPO.setCorpId(corpId);
        if(updateUserList == null){
            updateUserList = new ArrayList<TbQyUserInfoPO>(100);
        }
        updateUserList.add(tbQyUserInfoPO);
        if(userRefDeptMap == null){
            userRefDeptMap = new HashMap<String, List<String>>(100);
        }
        userRefDeptMap.put(tbQyUserInfoPO.getUserId(), detId);
        BeanHelper.copyProperties(vo, tbQyUserInfoPO);
    }

    /**
     * 补充用户的信息
     * @param agentList
     * @param user
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -6-23
     */
    private void getUserInfo(List<AgentRange> agentList, WxUser user) throws Exception, BaseException {
        if (isQiyeweixin && (StringUtil.isNullEmpty(user.getName())
                || user.getDepartment() == null)) {
            WxUser wxUser = WxUserService.getUser(user.getUserid(), org.getCorpId(), org.getOrgId(), agentList.get(0).getAgentCode());
            if (wxUser != null) {
                user.setMobile(wxUser.getMobile());
                user.setEmail(wxUser.getEmail());
                user.setGender(wxUser.getGender());
                user.setName(wxUser.getName());
                user.setAvatar(wxUser.getAvatar());
                user.setPosition(wxUser.getPosition());
                user.setDepartment(wxUser.getDepartment());
            }
        }
    }

    /**
     * 批量提交
     * @param agentList 应用list
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void batchSubmit(List<AgentRange> agentList){
        if(delUserDeptRefList != null && delUserDeptRefList.size()>0){
            try {
                QwtoolUtil.delBatchList(TbQyUserDepartmentRefPO.class, delUserDeptRefList);
            } catch (Exception e) {
                logger.error("ContactSyncThread delUserDeptRefList "+delUserDeptRefList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread delUserDeptRefList @sqh",corpId+",size="+delUserDeptRefList.size());
                delDeptRefUserMap.clear();
            } catch (BaseException e) {
                logger.error("ContactSyncThread delUserDeptRefList "+delUserDeptRefList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread delUserDeptRefList @sqh",corpId+",size="+delUserDeptRefList.size());
                delDeptRefUserMap.clear();
            }
            delUserDeptRefList.clear();
        }
        if(addUserDeptRefList!=null && addUserDeptRefList.size()>0){
            try {
                List<TbQyUserDepartmentRefPO> errorList = QwtoolUtil.addBatchList(addUserDeptRefList,false);
                if (!AssertUtil.isEmpty(errorList)) {
                    //如果有错误数据，清除错误数据
                    for (TbQyUserDepartmentRefPO po : errorList) {
                        addDeptRefUserMap.get(po.getDepartmentId()).remove(po.getUserId());
                    }
                }
            } catch (BaseException e) {
                logger.error("ContactSyncThread addUserDeptRefList "+addUserDeptRefList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread addUserDeptRefList @sqh",corpId+",size="+addUserDeptRefList.size());
                addDeptRefUserMap.clear();
            } catch (Exception e) {
                logger.error("ContactSyncThread addUserDeptRefList "+addUserDeptRefList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread addUserDeptRefList @sqh",corpId+",size="+addUserDeptRefList.size());
                addDeptRefUserMap.clear();
            }
            addUserDeptRefList.clear();
        }
        if(addUserList != null && addUserList.size() > 0){
            try {
                QwtoolUtil.addBatchList(addUserList,false);
            } catch (BaseException e) {
                logger.error("ContactSyncThread addUserList "+addUserList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread addUserList @sqh",corpId+",size="+addUserList.size());
            } catch (Exception e) {
                logger.error("ContactSyncThread addUserList "+addUserList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread addUserList @sqh",corpId+",size="+addUserList.size());
            }
        }
        if(updateUserList!=null && updateUserList.size()>0){
            try {
                QwtoolUtil.updateBatchList(updateUserList, false,false);
                for(TbQyUserInfoPO po : updateUserList){
                    //修改缓存中的人员数据
                    CacheSessionManager.update(po.getUserId());
                }
            } catch (BaseException e) {
                logger.error("ContactSyncThread updateUserList "+updateUserList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread updateUserList @sqh",corpId+",size="+updateUserList.size());
            } catch (Exception e) {
                logger.error("ContactSyncThread updateUserList "+updateUserList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread updateUserList @sqh",corpId+",size="+updateUserList.size());
            }
        }
        if (inDeptList != null && inDeptList.size()>0) {
            try {
                addAgentAllRange(agentList, inDeptList);
            } catch (BaseException e) {
                logger.error("ContactSyncThread inDeptList " + inDeptList.size(), e);
                ExceptionCenter.addException(e,"ContactSyncThread inDeptList @sqh", corpId + ",size=" + inDeptList.size());
            } catch (Exception e) {
                logger.error("ContactSyncThread inDeptList " + inDeptList.size(), e);
                ExceptionCenter.addException(e,"ContactSyncThread inDeptList @sqh", corpId + ",size=" + inDeptList.size());
            }
            inDeptList.clear();
        }
        if(agentUserRefIdList != null && agentUserRefIdList.size()>0){
            try {
                QwtoolUtil.delBatchList(TbQyAgentUserRefPO.class,agentUserRefIdList);
            } catch (Exception e) {
                logger.error("ContactSyncThread agentUserRefIdList "+agentUserRefIdList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread agentUserRefIdList @sqh",corpId+",size="+agentUserRefIdList.size());
            } catch (BaseException e) {
                logger.error("ContactSyncThread agentUserRefIdList "+agentUserRefIdList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread agentUserRefIdList @sqh",corpId+",size="+agentUserRefIdList.size());
            }
            agentUserRefIdList.clear();
            //清除缓存
            List<String> angentCodes = getAgentCodes(agentList, true);
            VisibleRangeUtil.refreshVisibleRange(org.getOrgId(),angentCodes.iterator());
        }
        if(agentUserRefPOList != null && agentUserRefPOList.size()>0){
            try {
                List<String> angentCodes = getAgentCodes(agentList, false);
                delAgentUserRefRepeat(agentUserRefPOList, angentCodes);
                QwtoolUtil.addBatchList(agentUserRefPOList,false);
            } catch (BaseException e) {
                logger.error("ContactSyncThread agentUserRefPOList "+agentUserRefPOList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread agentUserRefPOList @sqh",corpId+",size="+agentUserRefPOList.size());
            } catch (Exception e) {
                logger.error("ContactSyncThread agentUserRefPOList "+agentUserRefPOList.size(),e);
                ExceptionCenter.addException(e,"ContactSyncThread agentUserRefPOList @sqh",corpId+",size="+agentUserRefPOList.size());
            }
            agentUserRefPOList.clear();
            // 清除缓存
            VisibleRangeUtil.refreshVisibleRange(org.getOrgId(),updateAgentCodeSet.iterator());
        }
        UserOrgVO user = new UserOrgVO();
        user.setOrgId(org.getOrgId());
        user.setCorpId(org.getCorpId());
        user.setWxId(org.getWxId());
        user.setWxParentid(org.getWxParentid());
        user.setOrgName(org.getOrgName());
        UserInfoChangeNotifier.batchChangeDept(user, addDeptList, updateDeptList, delDeptList, UserInfoChangeInformType.SYNC_MGR);
        UserInfoChangeNotifier.syncEnd(user, addUserList, updateUserList, leavUserList, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap);
        if(addDeptList != null && addDeptList.size()>0){
            addDeptList.clear();
        }
        if(updateDeptList != null && updateDeptList.size()>0){
            updateDeptList.clear();
        }
        if(delDeptList != null && delDeptList.size()>0){
            delDeptList.clear();
        }
        if(addUserList != null && addUserList.size()>0){
            addUserList.clear();
        }
        if (addUserSet != null) {
            addUserSet.clear();
        }
        if(updateUserList != null && updateUserList.size()>0){
            updateUserList.clear();
        }
        if(leavUserList != null && leavUserList.size()>0){
            leavUserList.clear();
        }
        if(userRefDeptMap != null && userRefDeptMap.size()>0){
            userRefDeptMap.clear();
        }
        if(addDeptRefUserMap != null && addDeptRefUserMap.size()>0){
            addDeptRefUserMap.clear();
        }
        if(delDeptRefUserMap != null && delDeptRefUserMap.size()>0){
            delDeptRefUserMap.clear();
        }
    }

    private void delAgentUserRefRepeat(List<TbQyAgentUserRefPO> agentUserRefPOList, List<String> angentCodes){
        Set<String> userIds = new HashSet<String>(agentUserRefPOList.size());
        Map<String, TbQyAgentUserRefPO> map = new HashMap<String, TbQyAgentUserRefPO>(agentUserRefPOList.size());
        for (TbQyAgentUserRefPO po : agentUserRefPOList) {
            userIds.add(po.getUserId());
            map.put(po.getUserId() + "_" + po.getAgentCode(), po);
        }
        try {
            List<TbQyAgentUserRefPO> list = experienceapplicationService.getAgentUserRefPOByUserIds(new ArrayList(userIds));
            if (list != null) {
                for (TbQyAgentUserRefPO po : list) {
                    map.remove(po.getUserId() + "_" + po.getAgentCode());
                }
                agentUserRefPOList.clear();
                agentUserRefPOList.addAll(map.values());
            }
        } catch (SQLException e) {
            logger.error("ContactSyncThread delAgentUserRefRepeat " + corpId, e);
            ExceptionCenter.addException(e, "ContactSyncThread delAgentUserRefRepeat @sqh", corpId);
        }
    }

    /**
     * 更新po
     * @param po 同步信息po
     * @param syncVO 同步数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void updateContactSyncPO(TbQyContactSyncPO po, WxSyncVO syncVO) {
        if (syncVO == null) {
            return;
        }
        try {
            TbQyContactSyncPO updatePO = new TbQyContactSyncPO();
            if(syncVO.getIs_last()==0){//不是最后一个
                updatePO.setId(po.getId());
                updatePO.setFinishTime(new Date());
                updatePO.setNextSeq(syncVO.getNext_seq());
                updatePO.setNextOffset(syncVO.getNext_offset());
                updatePO.setStatus(ContactSyncStatus.STATUS_ING);
                updatePO.setIsLast(syncVO.getIs_last());
            }
            else{
                updatePO.setId(po.getId());
                updatePO.setFinishTime(new Date());
                updatePO.setNextSeq(syncVO.getNext_seq());
                updatePO.setNextOffset(syncVO.getNext_offset());
                updatePO.setFinishSeq(updatePO.getNextSeq());
                updatePO.setIsLast(syncVO.getIs_last());
                if (syncVO.isExecuteAll()) { //如果执行了所有
                    updatePO.setStatus(ContactSyncStatus.STATUS_FINISH);
                }
                else {
                    //设置为待执行，下次继续执行
                    updatePO.setStatus(ContactSyncStatus.STATUS_WAIT);
                }
            }
            contactService.updatePO(updatePO,false);
        } catch (Exception e) {
            logger.error("ContactSyncThread updateContactSyncPO "+syncVO.toString(),e);
            ExceptionCenter.addException(e,"ContactSyncThread updateContactSyncPO @sqh",corpId+",syncVO="+syncVO.toString());
        }
    }

    /**
     * 是否改变了部门
     * @param dep 微信部门id
     * @param userDept 用户部门po
     * @param isRangAll 是否所有人可见
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private boolean isChangeDept(List<String> dep, List<TbQyUserDepartmentRefPO> userDept, boolean isRangAll) throws Exception, BaseException {
        //判断用户部门是否已修改，如果没有修改
        if (dep == null || dep.size() == 0) {
            return false;
        } else {
            if(userDept == null || userDept.size()==0){
                return true;
            }
            if(isRangAll && dep.size() != userDept.size()){//如果应用不是全公司可见，部门数又不一致，需要更新
                return true;
            }
            Set<String> set = new HashSet<String>(dep.size()+5);
            for (String d : dep)
                set.add(d);
            for (TbQyUserDepartmentRefPO d : userDept) {
                if (set.add(d.getDepartmentId())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 因为系统必须有所属部门，当用户在无部门id时，默认到相应部门下
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author Sun Qinghai
     * @date 2016 -10-13
     * @2016-3-13
     * @version 1.0
     */
    private List<String> getWxEmptyDept() throws Exception, BaseException {
        TbDepartmentInfoPO department;
        department = deptWxIdMap.get(null);
        if (department == null) {
            department = DepartmentUtil.getWxEmptyDept(org.getOrgId());
            deptWxIdMap.put(null, department);
        }
        List<String> detId = new ArrayList<String>(1);
        detId.add(department.getId());
        return detId;
    }

    /**
     * 获取所有的应用托管情况
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -11-1
     */
    private List<AgentRange> getAllAgentRange() throws Exception, BaseException {
        if (agentRangeList == null) {
            agentRangeList = new ArrayList<AgentRange>(20);
            List<TbQyExperienceAgentPO> list = experienceapplicationService.findSuiteAgentByCorpid(corpId);
            if (list == null || list.size() == 0) {
                return null;
            }
            agentRangeList = new ArrayList<AgentRange>();
            for(TbQyExperienceAgentPO agent : list){
                AgentRange ar = new AgentRange();
                ar.setSuiteId(agent.getSuiteId());
                ar.setAgentCode(agent.getAgentCode());
                ar.setPo(agent);
                if("1".equals(agent.getIsRangeAll())){
                    ar.setAllUserVisible(true);
                }
                if(ar.isAllUserVisible()
                        || (!StringUtil.isNullEmpty(agent.getExtraPartys()) && agent.getExtraPartys().contains("|"+org.getWxId()+"|"))){
                    ar.setAllRange(true);
                }
                else{
                    String[] tags;
                    Set<String> deptId = new HashSet<String>(50);
                    if(!StringUtil.isNullEmpty(agent.getPartys())){
                        tags = agent.getPartys().substring(1,agent.getPartys().length()).split("\\|");
                        Collections.addAll(deptId, tags);
                    }
                    /*if(!StringUtil.isNullEmpty(agent.getExtraPartys())){
                        tags = agent.getExtraPartys().substring(1,agent.getExtraPartys().length()).split("\\|");
                        Collections.addAll(deptId, tags);
                    }*/
                    Set<String> userIdSet = new HashSet<String>(50);
                    if(!StringUtil.isNullEmpty(agent.getUserinfos())){
                        tags = agent.getUserinfos().substring(1,agent.getUserinfos().length()).split("\\|");
                        Collections.addAll(userIdSet, tags);
                    }
                    /*if(!StringUtil.isNullEmpty(agent.getExtraUserinfos())){
                        tags = agent.getExtraUserinfos().substring(1,agent.getExtraUserinfos().length()).split("\\|");
                        Collections.addAll(userIdSet, tags);
                    }*/
                    /*if(!StringUtil.isNullEmpty(agent.getExtraTags())){
                        tags = agent.getExtraTags().substring(1,agent.getExtraTags().length()).split("\\|");
                        List<String> wxDeptId = tagService.getWxDeptIdsByWxTagids(org.getOrgId(),tags);
                        if(wxDeptId != null && wxDeptId.size()>0){
                            deptId.addAll(wxDeptId);
                        }
                        List<String> wxUserId = tagService.getWxUserIdsByWxTagids(org.getOrgId(),tags);
                        if(wxUserId != null && wxUserId.size()>0){
                            userIdSet.addAll(wxUserId);
                        }
                    }*/
                    if(deptId.contains(org.getWxId())){//如果是全公司可见
                        ar.setAllRange(true);
                        ar.setAllUserVisible(true);
                    }
                    else{
                        TbDepartmentInfoPO dept;
                        Set<String> nameList = new HashSet<String>(deptId.size());
                        for(String id:deptId){
                            dept = deptWxIdMap.get(id);
                            if(dept!=null){
                                nameList.add(dept.getDeptFullName());
                            }
                        }
                        if(!StringUtil.isNullEmpty(agent.getTags())){
                            tags = agent.getTags().substring(1,agent.getTags().length()).split("\\|");
                            ar.setWxTagId(HashUtil.toHashSet(tags));
                            List<String> tagIds = tagService.getTagIdsByWxTagIds(org.getOrgId(), tags);
                            List<String> deptIds = tagService.getTagRefMenberIdListByTagIds(tagIds, TagStaticUtil.TAG_REF_TYPE_DEPT);
                            if (!AssertUtil.isEmpty(tagIds)) {
                                for(String id : deptIds){
                                    dept = deptWxIdMap.get(id);
                                    if(dept!=null && deptId.add(dept.getWxId())){ //表示为新增部门id
                                        nameList.add(dept.getDeptFullName());
                                    }
                                }
                            }
                            List<String> userIds = tagService.getTagRefMenberIdListByTagIds(tagIds, TagStaticUtil.TAG_REF_TYPE_USER);
                            List<String> wxUserIds = contactService.getWxUserIdsByUserIds(userIds);
                            if (!AssertUtil.isEmpty(wxUserIds)) {
                                userIdSet.addAll(wxUserIds);
                            }
                        }
                        if(deptId.contains(org.getWxId())){//如果是全公司可见
                            ar.setAllRange(true);
                            ar.setAllUserVisible(true);
                        }
                        else {
                            ar.setWxDeptId(deptId);
                            ar.setDeptFullNameSet(nameList);
                            ar.setWxUserId(userIdSet);
                        }
                    }
                }
                agentRangeList.add(ar);
            }
        }
        return agentRangeList;
    }
    /**
     * 获取应用可见范围
     * @param po 同步的数据
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private List<AgentRange> getAgentRange(TbQyContactSyncPO po) throws Exception, BaseException {
        if (agentRangeList == null) {
            getAllAgentRange();
        }
        if (agentRangeList.size() == 0) {
            return null;
        }
        String suiteId = po.getSuiteId();
        List<AgentRange> arList = new ArrayList<AgentRange>(10);
        for (AgentRange ar : agentRangeList) {
            if (suiteId.equals(ar.getSuiteId())) {
                arList.add(ar);
            }
        }
        if (arList.size() == 0) {
            return null;
        }
        return arList;
    }

    /**
     * 初始化所有部门信息
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private void initAllDept() throws Exception, BaseException {
        if (deptWxIdMap == null) {
            deptWxIdMap = new HashMap<String, TbDepartmentInfoPO>(50);
        }
        if (isSyncAllRange) {
            return;
        }
        List<TbDepartmentInfoPO> depts = departmentService.getAllDepartForSync(org.getOrgId());
        if (depts != null && depts.size() > 0) {
            TbDepartmentInfoPO po = new TbDepartmentInfoPO();
            po.setId("");
            po.setWxId(org.getWxId());
            po.setWxParentid(org.getWxParentid());
            deptWxIdMap.put(po.getWxId(),po);
            for (TbDepartmentInfoPO tbDepartmentInfoPO : depts) {
                deptWxIdMap.put(tbDepartmentInfoPO.getWxId(), tbDepartmentInfoPO);
            }
        }
    }

    /**
     * 获取用于code
     * @param agentList 应用列表
     * @param isAll 是否获取所有，否的话只获取非全公司可见的
     * @return 返回数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private List<String> getAgentCodes(List<AgentRange> agentList, boolean isAll){//是否需要所有的agentCode，如果为false，排除全公司可见的项
        List<String> angentCodes = null;
        for(AgentRange ar : agentList){
            if(isAll || !ar.isAllUserVisible()) {//如果不是所有人可见
                if(angentCodes == null){
                    angentCodes = new ArrayList<String>(5);
                }
                angentCodes.add(ar.getAgentCode());
            }
        }
        return angentCodes;
    }
}
