package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyAgentUserRefPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.WxqyhStringUtil;
import cn.com.do1.component.util.org.OrgUtil;
import cn.com.do1.component.wxcgiutil.WxTagUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 可见范围更新工具类
 * @author sunqinghai
 * 2016 -10-13
 */
public class ChangeAgentUserUtil {
    /**
     * 必须带注释
     */
    private static transient Logger logger = LoggerFactory.getLogger(ChangeAgentUserUtil.class);
    /**
     * 必须带注释
     */
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    /**
     * 必须带注释
     */
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    /**
     * 必须带注释
     */
    private static IDepartmentMgrService departmentMgrService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentMgrService.class);
    /**
     * 必须带注释
     */
    private static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);

    /**
     * 重置应用的可见范围
     * @param agent
     * @author sunqinghai
     * @date 2017 -2-28
     */
    public static void resetAgentUser(TbQyExperienceAgentPO agent) {
        TbQyExperienceAgentPO old = (TbQyExperienceAgentPO) agent.clone();
        old.setUserinfos(null);
        old.setExtraUserinfos(null);
        old.setPartys(null);
        old.setExtraPartys(null);
        old.setTags(null);
        old.setExtraTags(null);
        old.setIsRangeAll(null);
        changeAgentUser(old, agent);
    }
    /**
     * 改变应用的可见范围
     * @param oldAgent 旧的托管数据
     * @param newAgent 新的托管数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    public static void changeAgentUser(TbQyExperienceAgentPO oldAgent, TbQyExperienceAgentPO newAgent){
        try {
            DqdpOrgVO org = OrgUtil.getOrgByCorpId(oldAgent.getCorpId());
            if (org == null) {
                return;
            }
            changeAgentUser(org, oldAgent, newAgent);
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil changeAgentUser "+oldAgent.toString(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil changeAgentUser @sqh", oldAgent.toString());
        }
    }
    /**
     * 改变应用的可见范围
     * @param oldAgent 旧的托管数据
     * @param newAgent 新的托管数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    public static void changeAgentUser(DqdpOrgVO org, TbQyExperienceAgentPO oldAgent, TbQyExperienceAgentPO newAgent){
        try {
            boolean newIsAll = "1".equals(newAgent.getIsRangeAll());
            if(newIsAll && "1".equals(oldAgent.getIsRangeAll())){
                return;
            }
            else if (newIsAll){
                //删除原有的可见范围
                List<String> userIdList = experienceapplicationService.getAgentUserRefIdByAgentCode(org.getOrgId(), newAgent.getAgentCode());
                QwtoolUtil.delBatchList(TbQyAgentUserRefPO.class, userIdList);
                return;
            }
            //如果新的比旧的长度要少，说明对原有的可见范围有删除行为，直接重置
            //如果原有的应用可见范围为空，也直接重置
            if (WxqyhStringUtil.compareLength(newAgent.getUserinfos(), oldAgent.getUserinfos()) < 0
                    || WxqyhStringUtil.compareLength(newAgent.getPartys(), oldAgent.getPartys()) < 0
                    || WxqyhStringUtil.compareLength(newAgent.getTags(), oldAgent.getTags()) < 0
                    || (StringUtil.isNullEmpty(oldAgent.getUserinfos())
                    && StringUtil.isNullEmpty(oldAgent.getPartys())
                    && StringUtil.isNullEmpty(oldAgent.getTags()))) {
                updateAll(org, newAgent);
                return;
            }
            Set<String> addUserSet = null;
            //Set<String> temp;
            if (!StringUtil.strCstr(oldAgent.getUserinfos(), newAgent.getUserinfos())) {
                addUserSet = getAddInfo(oldAgent.getUserinfos(), newAgent.getUserinfos());
                if (addUserSet == null) {
                    updateAll(org, newAgent);
                    return;
                }
            }
            /*if (!StringUtil.strCstr(oldAgent.getExtraUserinfos(), newAgent.getExtraUserinfos())) {
                temp = getAddInfo(oldAgent.getExtraUserinfos(), newAgent.getExtraUserinfos());
                if(temp == null){
                    updateAll(org, newAgent);
                    return;
                }
                if(addUserSet == null){
                    addUserSet = temp;
                }
                else{
                    addUserSet.addAll(temp);
                }
            }*/
            Set<String> addPartySet = null;
            if (!StringUtil.strCstr(oldAgent.getPartys(), newAgent.getPartys())) {
                addPartySet = getAddInfo(oldAgent.getPartys(), newAgent.getPartys());
                if(addPartySet == null){
                    updateAll(org, newAgent);
                    return;
                }
            }
            /*if (!StringUtil.strCstr(oldAgent.getExtraPartys(), newAgent.getExtraPartys())) {
                temp = getAddInfo(oldAgent.getExtraPartys(), newAgent.getExtraPartys());
                if (temp == null) {
                    updateAll(org, newAgent);
                    return;
                }
                if (addPartySet == null) {
                    addPartySet = temp;
                }
                else {
                    addPartySet.addAll(temp);
                }
            }*/
            Set<String> addTagSet = null;
            if (!StringUtil.strCstr(oldAgent.getTags(), newAgent.getTags())) {
                addTagSet = getAddInfo(oldAgent.getTags(), newAgent.getTags());
                if (addTagSet == null) {
                    updateAll(org, newAgent);
                    return;
                }
            }
            /*if (!StringUtil.strCstr(oldAgent.getExtraTags(), newAgent.getExtraTags())) {
                temp = getAddInfo(oldAgent.getExtraTags(), newAgent.getExtraTags());
                if (temp == null) {
                    updateAll(org, newAgent);
                    return;
                }
                if (addTagSet == null) {
                    addTagSet = temp;
                }
                else {
                    addTagSet.addAll(temp);
                }
            }*/

            addUserAndPartByTags(org, newAgent.getAgentCode(), addTagSet, addUserSet, addPartySet, false);
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil changeAgentUser "+oldAgent.toString(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil changeAgentUser @sqh", oldAgent.toString());
        } catch (BaseException e) {
            logger.error("ChangeAgentUserUtil changeAgentUser "+oldAgent.toString(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil changeAgentUser @sqh", oldAgent.toString());
        }
    }

    /**
     * 更新所有应用的可见范围
     * @param corpId 机构id
     * @author sunqinghai
     * @date 2016 -10-24
     */
    public static void changeAllAgentUser(String corpId){
        try {
            DqdpOrgVO org = OrgUtil.getOrgByCorpId(corpId);
            if (org == null) {
                return;
            }
            List<TbQyExperienceAgentPO> list = experienceapplicationService.findSuiteAgentByCorpid(corpId);
            if (list == null || list.size() == 0){
                return;
            }
            List<String> allList = experienceapplicationService.getAgentUserRefId(org.getOrgId());
            if (allList != null && allList.size() > 0) {
                QwtoolUtil.delBatchList(TbQyAgentUserRefPO.class,allList);
            }
            for (TbQyExperienceAgentPO po : list){
                TbQyExperienceAgentPO old = (TbQyExperienceAgentPO) po.clone();
                old.setUserinfos(null);
                old.setExtraUserinfos(null);
                old.setPartys(null);
                old.setExtraPartys(null);
                old.setTags(null);
                old.setExtraTags(null);
                old.setIsRangeAll(null);
                changeAgentUser(org, old, po);
            }
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil changeAllAgentUser "+corpId, e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil changeAllAgentUser @sqh", corpId);
        } catch (BaseException e) {
            logger.error("ChangeAgentUserUtil changeAllAgentUser "+corpId, e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil changeAllAgentUser @sqh", corpId);
        }
    }

    /**
     * 把标签的数据转为人员和部门
     * @param org         机构信息
     * @param agentCode   应用
     * @param addTagSet   新增的标签
     * @param addUserSet  新增的用户
     * @param addPartySet 新增的部门
     * @param isAll       是否需要更新所有的可见范围内的人员，如果需要，删除已经不在可见范围内的
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private static void addUserAndPartByTags(DqdpOrgVO org, String agentCode, Set<String> addTagSet, Set<String> addUserSet, Set<String> addPartySet, boolean isAll){
        if(addTagSet != null && addTagSet.size()>0){
            if(addTagSet != null && addTagSet.size()>0){
                if(addUserSet == null){
                    addUserSet = new HashSet<String>(50);
                }
                if(addPartySet == null){
                    addPartySet = new HashSet<String>(10);
                }
            }
            for (String wxTagId : addTagSet) {
                addUserAndPartByTag(org.getCorpId(), agentCode, wxTagId, addUserSet, addPartySet);
            }
        }
        updateUser(org, agentCode, addUserSet, addPartySet, isAll);
    }

    /**
     * 把标签转为用户和部门
     * @param corpId      机构corpid
     * @param agentCode   应用
     * @param wxTagId     标签id
     * @param addUserSet  用户set
     * @param addPartySet 部门set
     * @return 返回数据
     * @author sunqinghai
     * @date 2016 -10-13
     */
    public static JSONObject addUserAndPartByTag(String corpId, String agentCode, String wxTagId, Set<String> addUserSet, Set<String> addPartySet){
        try {
            JSONObject jsonObject = WxTagUtil.getTagMembers(corpId, wxTagId, agentCode);
            if (jsonObject == null) {
                return null;
            }
            logger.debug("UserListSyncThread syncDeptUserTags userlist:" + jsonObject.getString("userlist")+" partylist:" + jsonObject.getString("partylist"));
            jsonObject.accumulate("wxTagId", wxTagId);
            // 遍历接口数据，保存新的标签数据(用户)
            JSONArray jsonArray = jsonObject.getJSONArray("userlist");
            int length = jsonArray.size();
            JSONObject tag;
            for (int i = 0; i < length; i++) {
                tag = jsonArray.getJSONObject(i);
                addUserSet.add(tag.getString("userid"));
            }
            // 遍历接口数据，保存新的标签数据(部门)
            jsonArray = jsonObject.getJSONArray("partylist");
            length = jsonArray.size();
            for (int i = 0; i < length; i++) {
                addPartySet.add(jsonArray.getString(i));
            }
            return jsonObject;
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil addUserAndPartByTag 将标签中的部门和人员加入到待更新列表corpId" + corpId + ",wxTagId=" + wxTagId, e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil addUserAndPartByTag 将标签中的部门和人员加入到待更新列表 @sqh", "corpId" + corpId + ",wxTagId=" + wxTagId);
            return null;
        } catch (BaseException e) {
            logger.error("ChangeAgentUserUtil addUserAndPartByTag 将标签中的部门和人员加入到待更新列表corpId" + corpId + ",wxTagId=" + wxTagId, e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil addUserAndPartByTag 将标签中的部门和人员加入到待更新列表 @sqh", "corpId" + corpId + ",wxTagId=" + wxTagId);
            return null;
        }
    }

    /**
     * 获取新增的部门
     * @param oldStr 旧的数据
     * @param newStr 新的数据
     * @return 返回数据，如果范围null表示存在删除就的数据的问题
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private static Set<String> getAddInfo(String oldStr, String newStr){
        String empty = "";
        if (WxqyhStringUtil.isNullEmpty(oldStr)) {//返回整个newStr
            String[] oldStrs = newStr.split("\\|");
            //新增的数据
            Set<String> addSet = new HashSet<String>(oldStrs.length);
            Collections.addAll(addSet, oldStrs);
            addSet.remove(empty);
            return addSet;
        }
        else {
            String[] oldStrs = oldStr.split("\\|");
            Set<String> oldSet = new HashSet<String>(oldStrs.length);
            Collections.addAll(oldSet, oldStrs);
            oldStrs = newStr.split("\\|");
            //新增的数据
            Set<String> addSet = new HashSet<String>(oldStrs.length);
            for (String s : oldStrs) {
                if (oldSet.contains(s)){
                    addSet.add(s);
                }
                else {
                    oldSet.remove(s); //如果在新的里找到了对应的，从set中删除，这样就剩下的如果为0就表示没有删除原有的
                }
            }
            if(oldSet.size()==0){
                addSet.remove(empty);
                return addSet;
            }
        }
        return null;
    }

    /**
     * 将部门转为用户，并更新
     * @param org 机构
     * @param agentCode 应用
     * @param addUserSet 新增的用户
     * @param addPartySet 新增的部门
     * @param isAll 是否更新所有
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private static void updateUser(DqdpOrgVO org, String agentCode, Set<String> addUserSet, Set<String> addPartySet, boolean isAll){
        try {
            if (addUserSet != null && addUserSet.size()>0) {
                List<String> wxUserIdList = new ArrayList<String>(addUserSet);
                List<String> list = contactService.getUserIdsByWxUserIds(org.getCorpId(), wxUserIdList);
                addUserSet.clear();
                if (list != null && list.size()>0) {
                    addUserSet.addAll(list);
                }
            }
            if (addPartySet != null && addPartySet.size()>0) {
                List<String> partyList = new ArrayList<String>(addPartySet);
                List<TbDepartmentInfoVO> deptList = departmentService.getDeptVOByWxIds(org.getOrgId(), partyList);
                if(deptList != null && deptList.size()>0){
                    Set<String> deptIdSet = new HashSet<String>(deptList.size());
                    List<String> deptFullNameList = new ArrayList<String>(deptList.size());
                    for(TbDepartmentInfoVO vo : deptList){
                        deptFullNameList.add(vo.getDeptFullName());
                        deptIdSet.add(vo.getId());
                    }
                    List<String> deptIdList = departmentMgrService.getChildDeptIdsByFullName(org.getOrgId(), deptFullNameList);
                    if(deptIdList != null && deptIdList.size()>0){
                        deptIdSet.addAll(deptIdList);
                    }
                    deptIdList = new ArrayList<String>(deptIdSet);

                    //获取这些部门下的人员id
                    deptIdList = contactService.findDeptUserIdAllByDeptIds(deptIdList);

                    if(deptIdList != null && deptIdList.size()>0){
                        if(addUserSet == null){
                            addUserSet = new HashSet<String>();
                        }
                        addUserSet.addAll(deptIdList);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil updateUser "+org.getCorpId(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil updateUser @sqh", org.getCorpId()+",agentCode"+agentCode);
        }
        insterAgentUserRef(org, agentCode, addUserSet, isAll);
    }

    /**
     * 插入应用的人员信息
     * @param org 机构
     * @param agentCode 应用
     * @param addUserSet 新增用户
     * @param isAll 是否更新所有，如果是会比较新旧人员，删除已经被删除的人员
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private static void insterAgentUserRef(DqdpOrgVO org, String agentCode, Set<String> addUserSet, boolean isAll){
        try {
            if(addUserSet == null || addUserSet.size()==0){
                return;
            }
            List<TbQyAgentUserRefPO> agentUserRefPOList = new ArrayList<TbQyAgentUserRefPO>(addUserSet.size());
            //获取原有的数据
            List<String> userIdList = experienceapplicationService.getAgentUserIdByAgentCode(org.getOrgId(), agentCode);
            Set<String> userIdSet = null;
            if(userIdList == null || userIdList.size()==0){
                for(String userId : addUserSet){
                    TbQyAgentUserRefPO po = new TbQyAgentUserRefPO();
                    po.setId(UUID32.getID());
                    po.setCorpId(org.getCorpId());
                    po.setAgentCode(agentCode);
                    po.setOrgId(org.getOrgId());
                    po.setCreateTime(new Date());
                    po.setUserId(userId);
                    agentUserRefPOList.add(po);
                }
            }
            else{
                userIdSet = new HashSet<String>(userIdList);
                for(String userId : addUserSet){
                    if(userIdSet.contains(userId)){//如果已在现有的可见范围内，跳过
                        if(isAll){
                            userIdSet.remove(userId);
                        }
                        continue;
                    }
                    TbQyAgentUserRefPO po = new TbQyAgentUserRefPO();
                    po.setId(UUID32.getID());
                    po.setCorpId(org.getCorpId());
                    po.setAgentCode(agentCode);
                    po.setOrgId(org.getOrgId());
                    po.setCreateTime(new Date());
                    po.setUserId(userId);
                    agentUserRefPOList.add(po);
                }
            }
            QwtoolUtil.addBatchList(agentUserRefPOList, false);
            if(isAll && userIdSet != null && userIdSet.size()>0){
                userIdList = new ArrayList<String>(userIdSet);
                userIdList = experienceapplicationService.getAgentUserRefIdByUserIds(org.getOrgId(), agentCode, userIdList);
                QwtoolUtil.delBatchList(TbQyAgentUserRefPO.class, userIdList);
            }
            // 清除缓存
            VisibleRangeUtil.refreshVisibleRangeByAgentCode(agentCode, org.getOrgId());
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil insterAgentUserRef "+org.getCorpId(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil insterAgentUserRef @sqh", org.getCorpId()+",agentCode"+agentCode);
        } catch (BaseException e) {
            logger.error("ChangeAgentUserUtil insterAgentUserRef "+org.getCorpId(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil insterAgentUserRef @sqh", org.getCorpId()+",agentCode"+agentCode);
        }
    }

    /**
     * 更新应用的所有可见人员
     * @param org 机构
     * @param newAgent 新的应用信息
     * @author sunqinghai
     * @date 2016 -10-13
     */
    private static void updateAll(DqdpOrgVO org, TbQyExperienceAgentPO newAgent){
        try {
            //如果用户、部门、标签都为空，默认所有人都可以看到
            if(WxqyhStringUtil.isNullEmpty(newAgent.getUserinfos()) && WxqyhStringUtil.isNullEmpty(newAgent.getPartys())
                    && WxqyhStringUtil.isNullEmpty(newAgent.getTags())){
                List<String> userIdList = experienceapplicationService.getAgentUserRefIdByAgentCode(org.getOrgId(), newAgent.getAgentCode());
                QwtoolUtil.delBatchList(TbQyAgentUserRefPO.class, userIdList);
            }else {
                String[] temp;
                String empty = "";
                //用户列表
                Set<String> addUserSet = null;
                if (!WxqyhStringUtil.isNullEmpty(newAgent.getUserinfos())) {
                    temp = (newAgent.getUserinfos()).split("\\|");
                    addUserSet = new HashSet<String>(temp.length);
                    //将所有用户列表里面的数据加到map里面
                    Collections.addAll(addUserSet, temp);
                    addUserSet.remove(empty);
                }
                //部门列表
                Set<String> addPartySet = null;
                if (!WxqyhStringUtil.isNullEmpty(newAgent.getPartys())) {
                    temp = (newAgent.getPartys()).split("\\|");
                    addPartySet = new HashSet<String>(temp.length);
                    //将所有用户列表里面的数据加到map里面
                    Collections.addAll(addPartySet, temp);
                    addPartySet.remove(empty);
                }
                //标签列表
                Set<String> addTagSet = null;
                if (!WxqyhStringUtil.isNullEmpty(newAgent.getTags())) {
                    temp = (newAgent.getTags()).split("\\|");
                    addTagSet = new HashSet<String>(temp.length);
                    //将所有用户列表里面的数据加到map里面
                    Collections.addAll(addTagSet, temp);
                    addTagSet.remove(empty);
                }
                //保存用户信息到数据库
                addUserAndPartByTags(org, newAgent.getAgentCode(), addTagSet, addUserSet, addPartySet, true);
            }
        } catch (Exception e) {
            logger.error("ChangeAgentUserUtil updateAll "+org.getCorpId(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil updateAll @sqh", org.getCorpId()+",newAgent="+newAgent.toString());
        } catch (BaseException e) {
            logger.error("ChangeAgentUserUtil updateAll "+org.getCorpId(), e);
            ExceptionCenter.addException(e, "ChangeAgentUserUtil updateAll @sqh", org.getCorpId()+",newAgent="+newAgent.toString());
        }
    }
}
