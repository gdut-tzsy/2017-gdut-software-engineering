package cn.com.do1.component.contact.student.util;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.vo.UserStudentRefData;
import cn.com.do1.component.util.UUID32;
import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * Created by hejinjiao on 2017/1/16.
 */
public class StudentRefUtil {
    /**
     * 新增时添加父母-学生关联表
     *
     * @param datas     the datas
     * @param userId    the user id
     * @param orgId     the org id
     * @param isStudent the is student
     * @return list
     */
    public static List<TbQyUserStudentRefPO> addUserStudentRef(List<String> personIds, String datas, String userId, String orgId, boolean isStudent) {
        List<UserStudentRefData> list = new ArrayList<UserStudentRefData>();
        if (!StringUtil.isNullEmpty(datas)) {
            list = JSON.parseArray(datas, UserStudentRefData.class);
        }
        List<TbQyUserStudentRefPO> refList = new ArrayList<TbQyUserStudentRefPO>();
        TbQyUserStudentRefPO refPO;
        int sort = 0;
        for (UserStudentRefData refData : list) {
            String[] userIds = refData.getUserIds().split("\\|");
            for (String id : userIds) {
                refPO = new TbQyUserStudentRefPO();
                personIds.add(id);
                if (isStudent) {
                    refPO.setUserId(id);
                    refPO.setStudentId(userId);
                } else {
                    refPO.setStudentId(id);
                    refPO.setUserId(userId);
                }
                refPO.setId(UUID32.getID());
                refPO.setOrgId(orgId);
                refPO.setRelation(refData.getRelation());
                refPO.setSort(sort);
                refList.add(refPO);
            }
            sort = sort + 1;
        }
        return refList;
    }

    /**
     * 编辑时修改父母-学生关联表
     *
     *
     * @param parentIds
     * @param hislist   the hislist
     * @param addlist   the addlist
     * @param uplist    the uplist
     * @param datas     the datas
     * @param userId    the user id
     * @param orgId     the org id
     * @param isStudent the is student
     * @return the list
     */
    public static List<String> updateUserStudentRef(List<String> parentIds, List<TbQyUserStudentRefPO> hislist, List<TbQyUserStudentRefPO> addlist,
                                                    List<TbQyUserStudentRefPO> uplist, String datas, String userId, String orgId, boolean isStudent) {
        List<UserStudentRefData> list = null;
        if (!AssertUtil.isEmpty(datas)) {
            list = JSON.parseArray(datas, UserStudentRefData.class);
        }
        List<String> ids;
        Map<String, TbQyUserStudentRefPO> map;
        if (isStudent) {
            map = getParentMap(hislist);
        } else {
            map = getChildMap(hislist);
        }
        ids = updateParentChildRef(parentIds, map, addlist, uplist, list, userId, orgId, isStudent);
        return ids;
    }

    /**
     * 组装需要修改的list、新增的list、删除ids
     *
     *
     * @param parentIds
     * @param map       the map
     * @param addlist   the addlist
     * @param uplist    the uplist
     * @param list      the list
     * @param userId    the user id
     * @param orgId     the org id
     * @param isStudent the is student
     * @return list list
     */
    private static List<String> updateParentChildRef(List<String> parentIds, Map<String, TbQyUserStudentRefPO> map, List<TbQyUserStudentRefPO> addlist,
                                                     List<TbQyUserStudentRefPO> uplist, List<UserStudentRefData> list, String userId,
                                                     String orgId, boolean isStudent) {
        if (list != null) {
            TbQyUserStudentRefPO po;
            int sort = 0;
            for (UserStudentRefData refData : list) {
                String[] userIds = refData.getUserIds().split("\\|");
                for (String id : userIds) {
                    parentIds.add(id);
                    if (map.containsKey(id)) {
                        po = map.get(id);
                        po.setRelation(refData.getRelation());
                        po.setSort(sort);
                        uplist.add(po);
                        map.remove(id);
                    } else {
                        po = new TbQyUserStudentRefPO();
                        po.setId(UUID32.getID());
                        if (isStudent) {
                            po.setStudentId(userId);
                            po.setUserId(id);
                        } else {
                            po.setStudentId(id);
                            po.setUserId(userId);
                        }
                        po.setOrgId(orgId);
                        po.setRelation(refData.getRelation());
                        po.setSort(sort);
                        addlist.add(po);
                    }
                }
                sort = sort + 1;
            }
        }
        if (map.size() > 0) {
            return getUserStudentRefIds(map);
        }
        return null;
    }

    /**
     * 返回需要删除关联表的ids
     *
     * @param map the map
     * @return user student ref ids
     */
    private static List<String> getUserStudentRefIds(Map<String, TbQyUserStudentRefPO> map) {
        List<String> ids = new ArrayList<String>();
        Iterator it = map.entrySet().iterator();
        Map.Entry e;
        TbQyUserStudentRefPO value;
        while (it.hasNext()) {
            e = (Map.Entry) it.next();
            value = (TbQyUserStudentRefPO) e.getValue();
            ids.add(value.getId());
        }
        return ids;
    }

    /**
     * 获取key为父母Id的map
     *
     * @param hislist the hislist
     * @return parent map
     */
    private static Map<String, TbQyUserStudentRefPO> getParentMap(List<TbQyUserStudentRefPO> hislist) {
        Map<String, TbQyUserStudentRefPO> map = new HashMap<String, TbQyUserStudentRefPO>();
        for (TbQyUserStudentRefPO refPO : hislist) {
            map.put(refPO.getUserId(), refPO);
        }
        return map;
    }

    /**
     * 获取key为学生Id的map
     *
     * @param hislist the hislist
     * @return child map
     */
    private static Map<String, TbQyUserStudentRefPO> getChildMap(List<TbQyUserStudentRefPO> hislist) {
        Map<String, TbQyUserStudentRefPO> map = new HashMap<String, TbQyUserStudentRefPO>();
        for (TbQyUserStudentRefPO refPO : hislist) {
            map.put(refPO.getStudentId(), refPO);
        }
        return map;
    }
}
