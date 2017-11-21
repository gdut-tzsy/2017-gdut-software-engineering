package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomItemPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionDesPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.vo.ImportVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.dqdp.core.DqdpAppContext;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by liyixin on 2016/10/25.
 */
public class ContactCustomUtil {
    /**
     * The constant contactCustomMgrService.
     */
    private static IContactCustomMgrService contactCustomMgrService = DqdpAppContext
            .getSpringContext().getBean("contactCustomService",
                    IContactCustomMgrService.class);

    /**
     * 初始化容量
     */
    public static final Integer INITIAL_CAPACITY = 30;

    /**
     * 类型是单行
     */
    public static final String TYPE_SINGLE = "1";

    /**
     * 类型是多行
     */
    public static final String TYPE_MANY = "2";

    /**
     * 类型是下拉框
     */
    public static final String TYPE_DROP_DOWN = "3";

    /**
     * 类型是日期类型
     */
    public static final String TYPE_DATE = "4";

    /**
     * 类型是多选
     */
    public static final String TYPE_MULTISELECT = "5";

    /**
     * 不启用
     */
    public static final String STATUS_NO_ENABLE = "0";

    /**
     * 启用
     */
    public static final String STATUS_ENABLE = "1";

    /**
     * 非必需的
     */
    public static final String NO_ISMUST = "0";

    /**
     * 必须的
     */
    public static final String ISMUST = "1";

    /**
     * des表中的has_select 字段，未被人使用
     */
    public static final String DES_NO_HAS_SELECT = "0";

    /**
     * des表中的has_select 字段，已被人使用
     */
    public static final String DES_HAS_SELECT = "1";

    /**
     * option表中的is_show 字段，不允许显示在手机端
     */
    public static final String NO_IS_SHOW = "0";

    /**
     * option表中的is_show 字段，允许显示在手机端
     */
    public static final String IS_SHOW = "1";

    /**
     * option表中的is_edit字段，不允许成员自行修改
     */
    public static final String NO_IS_EDIT = "0";

    /**
     * option表中的is_edit字段，允许成员自行修改
     */
    public static final String IS_EDIT = "1";

    /**
     * 把自定义字段提示语放入对应的自定义字段列表中
     *
     * @param optionVOs 自定义字段列表
     * @param desVOs    自定义字段提示语列表
     * @return 返回数据 des to option
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-25
     */
    public static List<TbQyUserCustomOptionVO> setDesToOption(List<TbQyUserCustomOptionVO> optionVOs,  List<TbQyUserOptionDesVO> desVOs) throws BaseException, Exception {
        Map<String, List<TbQyUserOptionDesVO>> map = new HashMap<String, List<TbQyUserOptionDesVO>>(1);
        List<TbQyUserOptionDesVO> addDesVos = null;
        for(TbQyUserOptionDesVO vo : desVOs) {
            if(AssertUtil.isEmpty(map.get(vo.getOptionId()))) {
                addDesVos = new ArrayList<TbQyUserOptionDesVO>();
                addDesVos.add(vo);
                map.put(vo.getOptionId(), addDesVos);
            }
            else {
                addDesVos = map.get(vo.getOptionId());
                addDesVos.add(vo);
            }
        }
        //把map里的desList放入optionVos里
        for(TbQyUserCustomOptionVO vo : optionVOs) {
            vo.setDesVos(map.get(vo.getId()));
        }
        return optionVOs;
    }

    /**
     * 把json数据放入list里
     *
     * @param jsonList  前台传来的json数据
     * @param optionPOs list
     * @param desPOs    list
     * @param orgId     机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     */
    public static void setJsonToList(JSONObject jsonList, List<TbQyUserCustomOptionPO> optionPOs, List<TbQyUserCustomOptionDesPO> desPOs, String orgId) throws BaseException, Exception {
        boolean isVip = VipUtil.isQwVip(orgId);
        for (JSONObject optionJson :  (List<JSONObject>)jsonList.getJSONArray("list")) {
            TbQyUserCustomOptionPO optionPO = new TbQyUserCustomOptionPO();
            optionPO.setId(optionJson.getString("id"));
            optionPO.setOptionName(optionJson.getString("optionName"));
            optionPO.setOrgId(orgId);
            optionPO.setType(optionJson.getString("type"));
            if(isVip) {
                optionPO.setStatus(optionJson.getString("status"));
            }else {//如果不是vip，不启用
                optionPO.setStatus(ContactCustomUtil.STATUS_NO_ENABLE);
            }
            optionPO.setIsMust(ContactCustomUtil.NO_ISMUST);
            optionPO.setIsEdit(optionJson.getString("edit"));
            optionPO.setIsShow(optionJson.getString("show"));
            optionPOs.add(optionPO);
            //如果有写提示语
            if (optionJson.has("list")) {
                for(JSONObject desJson :  (List<JSONObject>)optionJson.getJSONArray("list")) {
                    String desId;
                    TbQyUserCustomOptionDesPO desPO = new TbQyUserCustomOptionDesPO();
                    //如果是下拉框
                    if(desJson.has("hasSelect")) {
                        if("".equals(desJson.getString("id"))) {
                            desId = UUID32.getID();
                        }
                        else {
                            desId = desJson.getString("id");}
                        desPO.setId(desId);
                        desPO.setHasSelect(desJson.getString("hasSelect"));
                        desPO.setSort(desJson.getInt("short1"));
                        desPO.setOrgId(orgId);
                        desPO.setName(desJson.getString("name"));
                        desPO.setOptionId(optionJson.getString("id"));
                        desPO.setCreateTime(new Date());
                    }else {//如果不是下拉框
                        desId = UUID32.getID();
                        desPO.setCreateTime(new Date());
                        desPO.setId(desId);
                        desPO.setHasSelect(ContactCustomUtil.DES_HAS_SELECT);
                        desPO.setSort(1);
                        desPO.setOrgId(orgId);
                        desPO.setName(desJson.getString("name"));
                        desPO.setOptionId(optionJson.getString("id"));
                    }
                    desPOs.add(desPO);
                }
            }
        }
    }

    /**
     * 把json里的数据放入po里
     *
     * @param jsonList 前台传来的json数据
     * @param userId   用户id
     * @param orgId    机构id
     * @return the list
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -11-1
     */
    public static List<TbQyUserCustomItemPO> addItemToList(JSONObject jsonList, String userId, String orgId) throws BaseException, Exception {
        List<TbQyUserCustomItemPO> list = new ArrayList<TbQyUserCustomItemPO>();
        for(JSONObject itemJson : (List<JSONObject>)jsonList.getJSONArray("list")) {
            TbQyUserCustomItemPO po = new TbQyUserCustomItemPO();
            po.setId(UUID32.getID());
            po.setUserId(userId);
            po.setCreateTime(new Date());
            po.setOrgId(orgId);
            po.setContent(itemJson.getString("content"));
            po.setOptionId(itemJson.getString("optionId"));
            list.add(po);
        }
        return list;
    }

    /**
     * Item to list.
     *
     * @param jsonList   前台传来的json数据
     * @param userId     用户id
     * @param orgId      机构id
     * @param addList    进行新增的list
     * @param updateList 进行更新的list
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -11-1
     */
    public static void itemToList(JSONObject jsonList, String userId, String orgId, List<TbQyUserCustomItemPO> addList, List<TbQyUserCustomItemPO> updateList) throws BaseException, Exception {
        for(JSONObject itemJson : (List<JSONObject>)jsonList.getJSONArray("list")) {
            TbQyUserCustomItemPO po = new TbQyUserCustomItemPO();
            po.setUserId(userId);
            po.setOrgId(orgId);
            po.setContent(itemJson.getString("content"));
            po.setOptionId(itemJson.getString("optionId"));
            if(itemJson.has("id")) {
                if("".equals(itemJson.getString("id"))) {
                    po.setId(UUID32.getID());
                    po.setCreateTime(new Date());
                    addList.add(po);
                }else {
                    po.setId(itemJson.getString("id"));
                    updateList.add(po);
                }
            }else {
                po.setId(UUID32.getID());
                po.setCreateTime(new Date());
                addList.add(po);
            }

        }
    }

    /**
     * 把自定义的数据组装成一个map
     *
     * @param orgId 机构Id
     * @return item map
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -11-2
     */
    public static Map<String, Map<String, TbQyUserCustomItemVO>> getItemMap(String orgId) throws BaseException, Exception {
        Map<String, Object> searchMap  = new HashMap<String, Object>(1);
        searchMap.put("orgId", orgId);
        Pager pager = new Pager();
        //当前页数
        pager.setCurrentPage(1);
        //每页显示数量
        pager.setPageSize(1000);
        pager = contactCustomMgrService.getOrgItemByOrgId(pager, searchMap);
        List<TbQyUserCustomItemVO> itemVOs = new ArrayList<TbQyUserCustomItemVO>((int)pager.getTotalRows());
        int size = (int)pager.getTotalPages();
        for(int i = 1; i <= size; i ++){
            itemVOs.addAll(pager.getPageData());
            pager.setCurrentPage(i+1);
            pager = contactCustomMgrService.getOrgItemByOrgId(pager, searchMap);
        }
        Map<String, Map<String, TbQyUserCustomItemVO>> map = new HashMap<String, Map<String, TbQyUserCustomItemVO>>();
        Map<String, TbQyUserCustomItemVO> itemMap;
        if(itemVOs.size() > 0) {
            for(TbQyUserCustomItemVO itemVO : itemVOs) {
                if(AssertUtil.isEmpty(map.get(itemVO.getUserId()))) {
                    itemMap = new HashMap<String, TbQyUserCustomItemVO>(1);
                    itemMap.put(itemVO.getOptionId(), itemVO);
                    map.put(itemVO.getUserId(), itemMap);
                }
                else {
                    itemMap = map.get(itemVO.getUserId());
                    itemMap.put(itemVO.getOptionId(), itemVO);
                }
            }
        }
        return map;
    }

    /**
     * 把数据放入要批量更新的po
     *
     * @param vo        导入的vo
     * @param userPO    用户po
     * @param itemPOs   批量更新的po
     * @param optionVOs 已启用的自定义字段
     */
    public static void setUserToItem(ImportVO vo, TbQyUserInfoPO userPO, List<TbQyUserCustomItemPO> itemPOs, List<TbQyUserCustomOptionVO> optionVOs) {
        if(!AssertUtil.isEmpty(vo.getItemVOs())) {
            for (int i = 0; i < optionVOs.size(); i++) {//自定义字段的值
                if (!AssertUtil.isEmpty(vo.getItemVOs().get(optionVOs.get(i).getId()))) {
                    TbQyUserCustomItemPO itemPO = new TbQyUserCustomItemPO();
                    itemPO.setId(UUID32.getID());
                    itemPO.setCreateTime(new Date());
                    itemPO.setUserId(userPO.getUserId());
                    itemPO.setOrgId(userPO.getOrgId());
                    itemPO.setOptionId(optionVOs.get(i).getId());
                    itemPO.setContent(vo.getItemVOs().get(optionVOs.get(i).getId()));
                    itemPOs.add(itemPO);
                }
            }
        }
    }

    /**
     * vip用户的自定义字段
     *
     * @param orgId the org id
     * @return user custom by org id
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    public static List<TbQyUserCustomOptionVO> getUserCustomByOrgId(String orgId) throws BaseException, Exception {
        if (VipUtil.isQwVip(orgId)) {
            return contactCustomMgrService.getUseingOptionByorgId(orgId);
        } else {
            return new ArrayList<TbQyUserCustomOptionVO>();
        }
    }

    /**
     * 判断查询条件是否有自定义字段查询
     * @param searchMap
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @date 2017 -2-6
     */
    public static boolean hasCustom(Map searchMap) throws BaseException, Exception{
        boolean boo = false;
        Iterator iterator = searchMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            if(entry.getKey().toString().indexOf("custom_") != -1){
                boo = true;
                break;
            }
        }
        return boo;
    }

    /**
     *查询高级搜索自定义字段条件的id列表
     * @param searchMap
     * @param customList
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @date 2017 -2-6
     */
    public static List getCustomByMap(Map searchMap, List<String> customList) throws BaseException, Exception{
        Iterator iterator = searchMap.entrySet().iterator();
        List<String> list = new ArrayList<String>();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            if(entry.getKey().toString().indexOf("custom_") != -1){//如果是自定义字段
                list.add((String) entry.getKey());
            }
        }
        for(int i = 0; i < list.size(); i ++){
            String optionId = list.get(i).substring(7,list.get(i).length());
            Map searchCustomMap = new HashMap<String,Object>(3);
            searchCustomMap.put("optionId", optionId);
            searchCustomMap.put("orgId", searchMap.get("orgId"));
            searchCustomMap.put("content", "%"+searchMap.get(list.get(i))+"%");
            searchCustomMap.put("customList", customList);
            //如果是第一次查询或者之前查询出来的长度大于0
            if(customList.size() != 0 || i==0){
                customList = contactCustomMgrService.searchCustom(searchCustomMap);
            }
            searchMap.remove(list.get(i));
        }
        return customList;
    }

}
