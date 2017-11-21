package cn.com.do1.component.contact.tag.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.tag.po.TbQyTagInfoShortPO;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxTagUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 标签同步工具类
 * Created by sunqinghai on 17/3/1.
 *
 * @author sunqinghai
 * @date 2017 -3-2
 */
public class TagSyncUtil {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(TagSyncUtil.class);
    /**
     * The constant tagService.
     */
    private static ITagMgrService tagService = DqdpAppContext.getSpringContext().getBean("tagService", ITagMgrService.class);
    /**
     * The constant contactService.
     */
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    /**
     * The constant departmentService.
     */
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);

    /**
     * 同步所有标签
     * @param userId 管理员账号
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static void syncAllTag(String userId) throws Exception, BaseException {
        UserOrgVO userInfoVO = contactService.getUserOrgVOByUserName(userId);
        syncAllTag(userInfoVO);
    }

    /**
     * 同步所有标签
     * @param userInfoVO 管理员信息
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static void syncAllTag(UserOrgVO userInfoVO) throws BaseException, Exception {
        if (Configuration.AUTO_CORPID.equals(userInfoVO.getCorpId())) {
            throw new NonePrintException(ErrorCodeDesc.TAG_NOT_USE.getCode(), ErrorCodeDesc.TAG_NOT_USE.getDesc());
        }
        try {
            /**
             * 1、获取微信上通讯录应用能管理的所有标签
             * 2、判断本地有无此标签，如无插入，如有更新
             * 3、对本地有微信没有的标签处理方式：状态为启用，并且标签id为空的新增到微信，其它的情况将标签id置为空
             */
            logger.debug("TagSyncUtil syncAllTag start corpId="+userInfoVO.getCorpId()+",orgId="+userInfoVO.getOrgId());
            JSONObject jsonObject = WxTagUtil.getTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode());
            if(!jsonObject.has("taglist") ){
                return;
            }
            // 遍历接口数据，保存新的标签数据
            JSONArray jsonArray = jsonObject.getJSONArray("taglist");
            Map<String, TbQyTagInfoPO> tagInfoMap = new HashMap<String, TbQyTagInfoPO>(10);
            List<TbQyTagInfoPO> groupTags = null;
            //获取标签信息
            List<TbQyTagInfoPO> tagInfoPOs = tagService.getTagInfoList(userInfoVO.getOrgId());
            if(tagInfoPOs!=null && tagInfoPOs.size()>0){
                for (TbQyTagInfoPO tbQyTagInfoPO : tagInfoPOs) {
                    if (StringUtil.isNullEmpty(tbQyTagInfoPO.getWxTagId())) { //如果标签id为空，表示从群组同步过来的标签，需要初始化到微信
                        if (TagStaticUtil.TAG_INFO_STATUS_USING != tbQyTagInfoPO.getStatus()) { //如果禁用的标签默默跳过
                            continue;
                        }
                        if (groupTags == null) {
                            groupTags = new ArrayList<TbQyTagInfoPO>();
                        }
                        groupTags.add(tbQyTagInfoPO);
                    }
                    else {
                        tagInfoMap.put(tbQyTagInfoPO.getWxTagId(), tbQyTagInfoPO);
                    }
                }
            }

            if(jsonArray.size() > 0){
                int length = jsonArray.size();
                //同步标签
                JSONObject tag;
                String tagId = null;
                TbQyTagInfoPO tagInfoPO;
                for (int i = 0; i < length; i++) {
                    try {
                        tag = jsonArray.getJSONObject(i);
                        tagId = tag.getString("tagid");
                        if (tagInfoMap.containsKey(tagId)) {
                            tagInfoPO = tagInfoMap.get(tagId);
                            //更新本地标签数据
                            syncTagUserAndDept(userInfoVO, tagInfoPO, tagId, tag.getString("tagname"), false);
                            //删掉已经更新完成的标签
                            tagInfoMap.remove(tagId);
                        }
                        else {
                            tagInfoPO = new TbQyTagInfoPO();
                            tagInfoPO.setCreator(userInfoVO.getUserName());
                            tagInfoPO.setOrgId(userInfoVO.getOrgId());
                            tagInfoPO.setStatus(TagStaticUtil.TAG_INFO_STATUS_USING);
                            tagInfoPO.setTagName(tag.getString("tagname"));
                            tagInfoPO.setRang(TagStaticUtil.TAG_RANG_MGR);
                            tagInfoPO.setShowNum(TagStaticUtil.TAG_SHOW_NUM_DEFAULT);
                            tagInfoPO.setCreateTime(new Date());
                            tagInfoPO.setCreator(userInfoVO.getUserName());
                            tagInfoPO.setWxTagId(tagId);
                            tagInfoPO.setId(UUID32.getID());
                            tagInfoPO.setSource(TagStaticUtil.TAG_INFO_SOURCE_QIYEWEIXIN);
                            //新增本地标签数据
                            syncTagUserAndDept(userInfoVO, tagInfoPO, tagId, tagInfoPO.getTagName(), true);
                        }
                    } catch (Exception e) {
                        logger.error("TagSyncUtil syncTagUserAndDept " + tagId, e);
                        ExceptionCenter.addException(e, "TagSyncUtil syncTagUserAndDept", userInfoVO.toString()+ "====" + tagId);
                    } catch (BaseException e) {
                        logger.error("TagSyncUtil syncTagUserAndDept " + tagId, e);
                        ExceptionCenter.addException(e, "TagSyncUtil syncTagUserAndDept", userInfoVO.toString()+ "====" + tagId);
                    }
                }
            }

            //从本地上传到微信
            if (!AssertUtil.isEmpty(groupTags)) {
                TbQyTagInfoShortPO updateCloneablePO = new TbQyTagInfoShortPO();
                List<TbQyTagInfoShortPO> updateList = new ArrayList<TbQyTagInfoShortPO>(groupTags.size());
                for (TbQyTagInfoPO tbQyTagInfoPO : groupTags) {
                    try {
                        syncToWeixin(userInfoVO, tbQyTagInfoPO);
                        TbQyTagInfoShortPO updatePO = updateCloneablePO.clone();
                        updatePO.setId(tbQyTagInfoPO.getId());
                        updatePO.setWxTagId(tbQyTagInfoPO.getWxTagId());
                        updatePO.setStatus(tbQyTagInfoPO.getStatus());
                        updateList.add(updatePO);
                    } catch (BaseException e) {
                        logger.error("TagSyncUtil syncToWeixin " + tbQyTagInfoPO.getTagName(), e);
                        ExceptionCenter.addException(e, "TagSyncUtil syncToWeixin", userInfoVO.toString()+ "====" + tbQyTagInfoPO.getTagName());
                        if ("40071".equals(e.getErrCode()) && TagStaticUtil.TAG_INFO_STATUS_DISABLE != tbQyTagInfoPO.getStatus()) { //不合法的标签名字，标签名字已经存在
                            //禁用标签
                            tbQyTagInfoPO.setStatus(TagStaticUtil.TAG_INFO_STATUS_DISABLE);
                            contactService.updatePO(tbQyTagInfoPO, false);
                        }
                    } catch (Exception e) {
                        logger.error("TagSyncUtil syncToWeixin " + tbQyTagInfoPO.getTagName(), e);
                        ExceptionCenter.addException(e, "TagSyncUtil syncToWeixin", userInfoVO.toString()+ "====" + tbQyTagInfoPO.getTagName());
                    }
                }
                if (!AssertUtil.isEmpty(updateList)) {
                    updateList = QwtoolUtil.updateBatchList(updateList, false, false);
                    if (!AssertUtil.isEmpty(updateList)) {
                        ExceptionCenter.addException(new BaseException("从本地上传到微信"), "TagSyncUtil syncAllTag 1 " + userInfoVO.getCorpId() , String.valueOf(updateList.size()));
                    }
                }
            }

            //微信上已删除了标签的处理方式，改为禁用状态
            if (tagInfoMap.size() > 0) {
                List<TbQyTagInfoPO> list = new ArrayList<TbQyTagInfoPO>(tagInfoMap.values());
                disableTag(list);
            }
        } finally {
            logger.debug("TagSyncUtil syncAllTag end corpId="+userInfoVO.getCorpId());
        }
    }
    /**
     * 微信上已删除了标签的处理方式，改为禁用状态
     * @param list
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author sunqinghai
     * @date 2017 -4-14
     */
    private static void disableTag(List<TbQyTagInfoPO> list) throws BaseException, Exception {
        TbQyTagInfoShortPO updateCloneablePO = new TbQyTagInfoShortPO();
        List<TbQyTagInfoShortPO> updateList = new ArrayList<TbQyTagInfoShortPO>(list.size());
        for (TbQyTagInfoPO po : list) {
            TbQyTagInfoShortPO updatePO = updateCloneablePO.clone();
            updatePO.setId(po.getId());
            updatePO.setStatus(TagStaticUtil.TAG_INFO_STATUS_DISABLE); //标签状态置为禁用
            updatePO.setWxTagId(null); //标签对应的微信id设置成空
            updateList.add(updatePO);
        }
        //需要更新为空的字段
        if (!AssertUtil.isEmpty(updateList)) {
            updateList = QwtoolUtil.updateBatchList(updateList, false, true);
            if (!AssertUtil.isEmpty(updateList)) {
                ExceptionCenter.addException(new BaseException("微信上已删除了标签的处理方式，改为禁用状态"), "TagSyncUtil disableTag" , String.valueOf(updateList.size()));
            }
        }
    }

    /**
     * 同步到微信
     * @param userInfoVO
     * @param tbQyTagInfoPO
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static void syncToWeixin (UserOrgVO userInfoVO, TbQyTagInfoPO tbQyTagInfoPO) throws BaseException, Exception {
        String tagId = WxTagUtil.createTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), tbQyTagInfoPO.getTagName());
        tbQyTagInfoPO.setWxTagId(tagId);
        tbQyTagInfoPO.setSource(TagStaticUtil.TAG_INFO_SOURCE_QIWEI);
        List<TbQyTagRefPO> list = tagService.getTagRefList(tbQyTagInfoPO.getId());
        if (AssertUtil.isEmpty(list)) {
            return;
        }
        List<String> userList = new ArrayList<String>(list.size());
        List<String> deptList = new ArrayList<String>(10);
        for (TbQyTagRefPO refPO : list) {
            if (TagStaticUtil.TAG_REF_TYPE_USER == refPO.getMenberType()) {
                userList.add(refPO.getMenberId());
            }
            else {
                deptList.add(refPO.getMenberId());
            }
        }
        //如果需要更新人数和部门数
        if (userList.size() != tbQyTagInfoPO.getUserCount() || deptList.size() != tbQyTagInfoPO.getDeptCount()) {
            tagService.updateTagUserAndDeptTotal(tbQyTagInfoPO, userList.size() - tbQyTagInfoPO.getUserCount(), deptList.size() - tbQyTagInfoPO.getDeptCount());
        }

        //获取新增的用户或部门wxUserId
        List<String> addWxUserIds = null;
        if (!AssertUtil.isEmpty(userList)) {
            addWxUserIds = contactService.getWxUserIdsByUserIds(userList);
        }
        List<String> addWxDeptIds = null;
        if (!AssertUtil.isEmpty(deptList)) {
            addWxDeptIds = departmentService.getWxDeptIdsByIds(deptList);
        }
        //调用微信接口新增或者删除用户及部门
        WxTagUtil.addUserTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), tbQyTagInfoPO.getWxTagId(), ListUtil.collToArrays(addWxUserIds), ListUtil.collToArrays(addWxDeptIds));
    }

    /**
     * 同步用户和部门到本地
     * @param userInfoVO
     * @param tbQyTagInfoPO
     * @param tagId
     * @param tagName
     * @param isAdd 是否是新增
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static void syncTagUserAndDept (UserOrgVO userInfoVO, TbQyTagInfoPO tbQyTagInfoPO, String tagId, String tagName, boolean isAdd) throws Exception, BaseException {
        JSONObject jsonObject = WxTagUtil.getTagMembers(userInfoVO.getCorpId(), tagId, WxAgentUtil.getAddressBookCode());
        JSONArray jsonArray = jsonObject.getJSONArray("userlist");
        int length = jsonArray.size();
        List<String> userId = null;
        if (length > 0) {
            JSONObject user;
            List<String> wxUserId = new ArrayList<String>(length);
            for (int i = 0; i < length; i++) {
                user = jsonArray.getJSONObject(i);
                wxUserId.add(user.getString("userid"));
            }
            userId = contactService.getUserIdsByWxUserIds(userInfoVO.getCorpId(), wxUserId);
        }
        jsonArray = jsonObject.getJSONArray("partylist");
        length = jsonArray.size();
        List<String> deptId = null;
        if (length > 0) {
            List<String> wxDeptId = new ArrayList<String>(length);
            for (int i = 0; i < length; i++) {
                wxDeptId.add(jsonArray.getString(i));
            }
            deptId = departmentService.getDeptIdsByWxIds(userInfoVO.getOrgId(), wxDeptId);
        }
        if (isAdd) {
            tagService.insertPO(tbQyTagInfoPO, false);
            tagService.addTagRef(userInfoVO, tbQyTagInfoPO,  ListUtil.toArrays(userId), ListUtil.toArrays(deptId), false);
        }
        else {
            int deptCount = tbQyTagInfoPO.getDeptCount();
            int userCount = tbQyTagInfoPO.getUserCount();
            tagService.updateTagRef(userInfoVO, tbQyTagInfoPO, ListUtil.toArrays(userId), ListUtil.toArrays(deptId), false);
            if (!tagName.equals(tbQyTagInfoPO.getTagName()) || userCount != tbQyTagInfoPO.getUserCount() || deptCount != tbQyTagInfoPO.getDeptCount()) {
                TbQyTagInfoPO update = new TbQyTagInfoPO();
                update.setId(tbQyTagInfoPO.getId());
                update.setTagName(tagName);
                update.setDeptCount(tbQyTagInfoPO.getDeptCount());
                update.setUserCount(tbQyTagInfoPO.getUserCount());
                tagService.updatePO(update, false);
            }
        }
    }


    /**
     * The constant set.
     */
    private static Set<String> set = new HashSet<String>(100);

    /**
     * @param corpId
     * @return 返回数据
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static boolean add(String corpId){
        return set.add(corpId);
    }

    /**
     * @return 返回数据
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static Set<String> getRunCorpIds(){
        return set;
    }

    /**
     * @param corpId
     * @author sunqinghai
     * @date 2017 -3-2
     */
    public static void remove(String corpId) {
        set.remove(corpId);
    }
}
