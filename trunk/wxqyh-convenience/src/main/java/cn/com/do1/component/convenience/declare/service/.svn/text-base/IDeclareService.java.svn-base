package cn.com.do1.component.convenience.declare.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclareCommentPO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclarePO;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
public interface IDeclareService extends IBaseService{

    /**
     * 业主查询申报消息列表
     * @param searchMap
     * @param pager
     * @return
     * @throws Exception
     * @throws BaseException
     */
    Pager searchDeclare(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException;

    /**
     * 网格员查询申报消息列表
     * @param searchMap
     * @param pager
     * @return
     * @throws Exception
     * @throws BaseException
     */
    Pager searchDeclareByGrider(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException;

    /**
     * 新增申报消息并且发送给相应网格员
     * @param tbYsjdDeclarePO
     * @param sendUser
     * @throws Exception
     * @throws BaseException
     */
    void sendMsg(TbYsjdDeclarePO tbYsjdDeclarePO, UserInfoVO sendUser) throws Exception, BaseException;

    /**
     * 获取评论信息
     * @param userId
     * @param declareId
     * @return
     */
    public List<TbYsjdDeclareCommentPO> getCommentsByUserID(String userId, String declareId) throws SQLException;

    /**
     * 更新评论数量
     * @param declareId
     */
    public void updateCommentNum(String declareId) throws SQLException;

    /**
     * 获取全部评论信息
     * @param searchMap
     * @param pager
     * @return
     */
    public Pager getPageMeetingComments(Map<String, Object> searchMap, Pager pager) throws SQLException;

    /**
     * 删除评论
     * @param id
     */
    void deleteComment(String id) throws SQLException;

    /**
     * 获取申报信息详情
     * @param id
     * @return
     */
    TbYsjdDeclareVO getDeclareInfoDetail(String id) throws SQLException;
}
