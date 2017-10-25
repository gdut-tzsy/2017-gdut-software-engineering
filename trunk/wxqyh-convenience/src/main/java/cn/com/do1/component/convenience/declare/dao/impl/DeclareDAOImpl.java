package cn.com.do1.component.convenience.declare.dao.impl;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.convenience.declare.dao.IDeclareDAO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclareCommentPO;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareCommentVO;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public class DeclareDAOImpl extends BaseDAOImpl implements IDeclareDAO {
    private final static transient Logger logger = LoggerFactory.getLogger(DeclareDAOImpl.class);

    @Override
    public Pager getDeclareInfoPageData(Map<String, Object> map, Pager pager) throws SQLException {
        String fromSql=" from tb_ysjd_declare t";

        String paramSql=" where 1=1"+
                " and t.status=:status "+
                " and t.create_user=:userId "+
                " and t.card_no=:cardNo "+
                " and (t.user_name like :personName"+
                " or t.grid_operator_name like :personName) ";

        if (map.get("startTime")!=null){
            paramSql+=" and t.checkin_time between :startTime and :endTime ";
        }

        String orderbySql="order by t.create_time desc";
        String sql="select t.*"+fromSql+paramSql+orderbySql;
        String countsql="select count(1)"+fromSql+paramSql;
        return pageSearchByField(TbYsjdDeclareVO.class,countsql,sql,map,pager);
    }

    @Override
    public Pager getDeclareInfoPageDataByGrider(Map<String, Object> map, Pager pager) throws SQLException {
        String fromSql=" from tb_ysjd_declare t";

        String paramSql=" where 1=1"+
                " and t.status=:status "+
                " and t.grid_operator=:userId "+
                " and t.card_no=:cardNo "+
                " and (t.user_name like :personName"+
                " or t.create_name like :personName) ";

        if (map.get("startTime")!=null){
            paramSql+=" and t.checkin_time between :startTime and :endTime ";
        }

        String orderbySql="order by t.create_time desc";
        String sql="select t.*"+fromSql+paramSql+orderbySql;
        String countsql="select count(1)"+fromSql+paramSql;
        return pageSearchByField(TbYsjdDeclareVO.class,countsql,sql,map,pager);
    }

    @Override
    public TbQyUserInfoVO getGrider(String ownerId) throws SQLException {
        String sql="SELECT u.* FROM tb_ysjd_ban b JOIN tb_qy_user_info u ON u.user_id=b.grid_operator_id"
                +" WHERE b.owner=:owner";
        this.preparedSql(sql);
        this.setPreValue("owner", ownerId);
        return this.executeQuery(TbQyUserInfoVO.class);
    }

    @Override
    public List<TbYsjdDeclareCommentPO> getCommentsByUserID(String userId, String declareId) throws SQLException {
        this.preparedSql("select t.* from tb_ysjd_declare_comment t where 1=1 and t.declare_id =:declareId  and t.create_person =:userId order by t.create_time desc ");
        this.setPreValue("declareId", declareId);
        this.setPreValue("userId", userId);
        return this.getList(TbYsjdDeclareCommentPO.class);
    }

    @Override
    public void updateCommentNum(String declareId) throws SQLException {
        this.preparedSql("update tb_ysjd_declare t set t.comment_count=t.comment_count+1 where t.id =:declareId");
        this.setPreValue("declareId", declareId);
        this.executeUpdate();
    }

    @Override
    public Pager getPageMeetingComments(Map<String, Object> searchMap, Pager pager) throws SQLException {
        String sql="select a.id,a.declare_id,a.type, a.create_person, a.person_name,a.head_pic, a.CONTENT, a.create_time as time,a.comment_status,   a.department_name,a.device_num,a.wx_user_id from tb_ysjd_declare_comment a where  a.declare_id = :declareId and comment_status= :commentStatus order by a.create_time desc";
        String countSql="select count(1) from tb_ysjd_declare_comment where declare_id = :declareId and comment_status= :commentStatus ";
        return this.pageSearchByField(TbYsjdDeclareCommentVO.class, countSql, sql, searchMap, pager);
    }

    @Override
    public void deleteComment(String id) throws SQLException {
        this.preparedSql("UPDATE tb_ysjd_declare_comment c,tb_ysjd_declare t set t.comment_count=t.comment_count-1  where t.id =c.declare_id and c.id=:id ");
        this.setPreValue("id", id);
        this.executeUpdate();
        this.preparedSql("DELETE FROM tb_ysjd_declare_comment where id=:id ");
        this.setPreValue("id", id);
        this.executeUpdate();
    }

    @Override
    public TbYsjdDeclareVO getDeclareInfoDetail(String id) throws SQLException {
        String sql="select * from tb_ysjd_declare t where t.id=:id";
        this.preparedSql(sql);
        this.setPreValue("id", id);
        return this.executeQuery(TbYsjdDeclareVO.class);
    }
}
