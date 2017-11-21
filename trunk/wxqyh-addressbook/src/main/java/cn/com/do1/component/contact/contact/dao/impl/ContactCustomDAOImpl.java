package cn.com.do1.component.contact.contact.dao.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;
import cn.com.do1.component.contact.contact.dao.IContactCustomDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2016/10/25.
 */
public class ContactCustomDAOImpl extends BaseDAOImpl implements IContactCustomDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(ContactCustomDAOImpl.class);
    /**
     *
     */
    private static final String getUseingOptionByorgId_sql = "select o.id, o.item_id,o.option_name,o.type,o.status,o.org_id,o.is_must,o.is_show,o.is_edit from tb_qy_user_custom_option o where o.org_id = :orgId and o.status = 1 order by o.item_id asc ";
    @Override
    public List<TbQyUserCustomOptionVO> getUseingOptionByorgId(String orgId) throws BaseException, Exception{
        preparedSql(getUseingOptionByorgId_sql);
        setPreValue("orgId", orgId);
        return super.getList(TbQyUserCustomOptionVO.class);
    }

    /**
     *
     */
    private static final String getOptionByorgId_sql = "select o.id, o.item_id,o.option_name,o.type,o.status,o.org_id,o.is_must,o.is_show,o.is_edit from tb_qy_user_custom_option o where o.org_id = :orgId  order by o.item_id asc ";
    @Override
    public List<TbQyUserCustomOptionVO> getOptionByorgId(String orgId) throws BaseException, Exception{
        preparedSql(getOptionByorgId_sql);
        setPreValue("orgId", orgId);
        return super.getList(TbQyUserCustomOptionVO.class);
    }

    /**
     *
     */
    private static final String getOptionDesByorgId_sql = "select d.id,d.option_id,d.name,d.sort,d.org_id,d.has_select from tb_qy_user_custom_option_des d where d.org_id = :orgId order by d.option_id,d.sort asc";
    @Override
    public List<TbQyUserOptionDesVO> getOptionDesByorgId(String orgId) throws BaseException, Exception{
        preparedSql(getOptionDesByorgId_sql);
        setPreValue("orgId", orgId);
        return super.getList(TbQyUserOptionDesVO.class);
    }

    private static final String getDesIdByOrgId_sql = "select d.id from tb_qy_user_custom_option_des d where d.org_id = :orgId";
    @Override
    public List<String> getDesIdByOrgId(String orgId) throws BaseException, Exception {
        preparedSql(getDesIdByOrgId_sql);
        setPreValue("orgId", orgId);
        return super.getList(String.class);
    }

    private static final String getItemByOrgId_sql = "select i.id, i.user_id, i.org_id, i.option_id, i.content from tb_qy_user_custom_item i where i.option_id in (:optionIds) and i.org_id = :orgId ";
    @Override
    public List<TbQyUserCustomItemVO> getItemByOrgId(List<String> optionIds,String orgId) throws BaseException, Exception{
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("optionIds", optionIds);
        map.put("orgId", orgId);
        return searchByField(TbQyUserCustomItemVO.class, getItemByOrgId_sql, map);
    }

    private static final String getItemByUserIdAndOrgId_sql = "select i.id, i.user_id, i.org_id, i.option_id, i.content from tb_qy_user_custom_item i where i.user_id = :userId and i.option_id in(:optionIds) and i.org_id = :orgId";
    @Override
   public List<TbQyUserCustomItemVO> getItemByUserIdAndOrgId(String userId, List<String> optionIds, String orgId) throws BaseException, Exception {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("optionIds", optionIds);
        map.put("userId", userId);
        map.put("orgId", orgId);
        return searchByField(TbQyUserCustomItemVO.class, getItemByUserIdAndOrgId_sql, map);
    }

    private static final String getOrgItemByOrgId_sql = "select i.id, i.user_id, i.org_id, i.option_id, i.content from tb_qy_user_custom_item i where i.org_id = :orgId ";
    private static final String getOrgItemByOrgId_sql_count = "select count(1) from tb_qy_user_custom_item i where i.org_id = :orgId ";
    @Override
    public Pager getOrgItemByOrgId(Pager pager, Map searchMap)throws BaseException, Exception {
        return pageSearchByField(TbQyUserCustomItemVO.class, getOrgItemByOrgId_sql_count, getOrgItemByOrgId_sql, searchMap, pager);
    }

    private static final String deleBatchUser_sql = "delete from tb_qy_user_custom_item where user_id in (:userIds)";
    @Override
    public void deleBatchUser(String[] userIds) throws BaseException, Exception{
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("userIds", userIds);
        updateByField(deleBatchUser_sql, map, true);
    }

    @Override
    public List<String> searchCustom(Map searchMap) throws BaseException, Exception{
        String searchCustom_sql = "select i.user_id from tb_qy_user_custom_item i where i.org_id = :orgId and i.option_id = :optionId and i.content like :content ";
        List<String> customList = (List<String>)searchMap.get("customList");
        if(customList.size() > 0 ){//如果上一个查询条件有id列表
            searchCustom_sql = "select i.user_id from tb_qy_user_custom_item i where i.org_id = :orgId and i.option_id = :optionId and i.content like :content and i.user_id in (:customList) ";
        }else{
            searchMap.remove("customList");
        }
        return searchByField(String.class, searchCustom_sql, searchMap);
    }

    private static final String searchCustomByOptionId_sql = "SELECT o.id,o.user_id,o.content FROM tb_qy_user_custom_item o WHERE o.option_id = :optionId ";
    private static final String searchCustomByOptionId_sql_count = "SELECT count(1) FROM tb_qy_user_custom_item o WHERE o.option_id = :optionId ";
    @Override
    public Pager searchCustomByOptionId(Pager pager,String optionId) throws BaseException, Exception{
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("optionId", optionId);
        return pageSearchByField(TbQyUserCustomItemVO.class, searchCustomByOptionId_sql_count, searchCustomByOptionId_sql, map, pager);
    }

    private static final String searchByOptionIdAndUserIds_sql = "SELECT o.id,o.user_id,o.content FROM tb_qy_user_custom_item o WHERE o.option_id = :optionId AND o.user_id in (:userIds)";
    @Override
    public List<TbQyUserCustomItemVO> searchByOptionIdAndUserIds(String optionId, List<String> userIds) throws BaseException, Exception{
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("userIds", userIds);
        map.put("optionId",optionId);
        return searchByField(TbQyUserCustomItemVO.class, searchByOptionIdAndUserIds_sql, map);
    }
}
