package cn.com.do1.component.contact.tag.dao.impl;

import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;
import cn.com.do1.component.contact.tag.dao.ITagReadOnlyDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagReadOnlyDAOImpl extends BaseDAOImpl implements ITagReadOnlyDAO {

    private static final String getTagPageInfoList_sql = "SELECT t.id,t.tag_name,t.show_num,t.status,t.rang" +
            " FROM tb_qy_tag_info t WHERE t.org_id = :orgId and t.status = :status and t.rang = :tagRang order by t.show_num asc";
    @Override
    public List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int tagRang, int status) throws SQLException {
        preparedSql(getTagPageInfoList_sql);
        setPreValue("orgId", orgId);
        setPreValue("tagRang", tagRang);
        setPreValue("status", status);
        return getList(QyTagPageInfoVO.class);
    }

    private static final String getTagPageInfoList1_sql = "SELECT t.id,t.tag_name,t.show_num,t.status,t.rang" +
            " FROM tb_qy_tag_info t WHERE t.org_id = :orgId and t.status = :status order by t.show_num asc";
    @Override
    public List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int status) throws SQLException {
        preparedSql(getTagPageInfoList1_sql);
        setPreValue("orgId", orgId);
        setPreValue("status", status);
        return getList(QyTagPageInfoVO.class);
    }

    private static final String getTagRefPageVOList_sql = "select tr.id,tr.menber_type,tr.menber_id from tb_qy_tag_ref tr where tr.tag_id = :tagId order by tr.menber_type desc";
    @Override
    public List<QyTagRefPageVO> getTagRefPageVOList(String tagId) throws SQLException {
        preparedSql(getTagRefPageVOList_sql);
        setPreValue("tagId", tagId);
        return getList(QyTagRefPageVO.class);
    }

    @Override
    public List<String> getTagRefMenberIdListByTagIds(List<String> tagIds, int menberType) throws SQLException {
        String sql = "select tr.menber_id FROM tb_qy_tag_ref tr WHERE tr.menber_type = :menberType and tr.tag_id in (:tagIds) ";
        Map<String, Object> map = new HashMap<String, Object>(tagIds.size() + 2);
        map.put("tagIds", tagIds);
        map.put("menberType", menberType);
        return searchByField(String.class, sql, map);
    }

    private static final String getTagIdsByWxTagIds_sql = "select id FROM tb_qy_tag_info WHERE org_id = :orgId and wx_tag_id in (:wxTagIds)";
    @Override
    public List<String> getTagIdsByWxTagIds(String orgId, String[] wxTagIds) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>(wxTagIds.length + 2);
        map.put("wxTagIds", wxTagIds);
        map.put("orgId", orgId);
        return searchByField(String.class, getTagIdsByWxTagIds_sql, map);
    }
}
