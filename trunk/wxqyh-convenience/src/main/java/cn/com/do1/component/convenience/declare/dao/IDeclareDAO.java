package cn.com.do1.component.convenience.declare.dao;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclareCommentPO;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IDeclareDAO extends IBaseDAO {

    Pager getDeclareInfoPageData(Map<String, Object> map, Pager pager) throws SQLException;

    Pager getDeclareInfoPageDataByGrider(Map<String, Object> map, Pager pager) throws SQLException;

    TbQyUserInfoVO getGrider(String ownerId) throws SQLException;

    List<TbYsjdDeclareCommentPO> getCommentsByUserID(String userId, String declareId) throws SQLException;

    void updateCommentNum(String declareId) throws SQLException;

    Pager getPageMeetingComments(Map<String, Object> searchMap, Pager pager) throws SQLException;

    void deleteComment(String id) throws SQLException;

    TbYsjdDeclareVO getDeclareInfoDetail(String id) throws SQLException;
}
