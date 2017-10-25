package cn.com.do1.component.convenience.declare.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.declare.dao.*;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclareCommentPO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclarePO;
import cn.com.do1.component.convenience.declare.service.IDeclareService;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareVO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Copyright &copy; 2010 广州市道一信息技术有限公司
* All rights reserved.
* User: ${user}
*/
@Service("declareService")
public class DeclareServiceImpl extends BaseService implements IDeclareService {
    private final static transient Logger logger = LoggerFactory.getLogger(DeclareServiceImpl.class);

    private IDeclareDAO declareDAO;
    @Resource
    public void setDeclareDAO(IDeclareDAO declareDAO) {
        this.declareDAO = declareDAO;
        setDAO(declareDAO);
    }

    public Pager searchDeclare(Map<String, Object> searchMap,Pager pager) throws Exception, BaseException {
        return declareDAO.getDeclareInfoPageData(searchMap,pager);
    }

    @Override
    public Pager searchDeclareByGrider(Map<String, Object> searchMap, Pager pager) throws Exception, BaseException {
        return declareDAO.getDeclareInfoPageDataByGrider(searchMap,pager);
    }

    public void sendMsg(TbYsjdDeclarePO tbYsjdDeclarePO, UserInfoVO sendUser) throws Exception, BaseException {
        //通过业主找到对应的网格员
        TbQyUserInfoVO grider=declareDAO.getGrider(sendUser.getPersonName());
        tbYsjdDeclarePO.setGridOperator(grider.getUserId());
        tbYsjdDeclarePO.setGridOperatorName(grider.getPersonName());
        String id = UUID.randomUUID().toString();
        tbYsjdDeclarePO.setId(id);
        this.insertPO(tbYsjdDeclarePO,false);

        //发送消息
        String title="新的申报信息通知";
        String content=sendUser.getPersonName()+"发布了一条申报信息，请处理！";
        sendMsgToUser(title,content,grider,id);
    }

    @Override
    public List<TbYsjdDeclareCommentPO> getCommentsByUserID(String userId, String declareId) throws SQLException {
        return declareDAO.getCommentsByUserID(userId,declareId);
    }

    @Override
    public void updateCommentNum(String declareId) throws SQLException {
        this.declareDAO.updateCommentNum(declareId);
    }

    @Override
    public Pager getPageMeetingComments(Map<String, Object> searchMap, Pager pager) throws SQLException {
        return this.declareDAO.getPageMeetingComments(searchMap,pager);
    }

    @Override
    public void deleteComment(String id) throws SQLException {
        this.declareDAO.deleteComment(id);
    }

    @Override
    public TbYsjdDeclareVO getDeclareInfoDetail(String id) throws SQLException {
        return declareDAO.getDeclareInfoDetail(id);
    }

    /**
     * 发送消息
     *
     * @param title
     * @param content
     * @param toUser
     */
    private void sendMsgToUser(String title, String content, TbQyUserInfoVO toUser,String id) {
        NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
        newsMessageVO.setTouser(toUser.getWxUserId());
        newsMessageVO.setDuration("0");
        newsMessageVO.setTitle(title);
        newsMessageVO.setDescription(content);
        newsMessageVO.setUrl(Configuration.WX_PORT +"/jsp/xentwap/declarehandle/declare_detail.jsp?id="+id);
        newsMessageVO.setCorpId(toUser.getCorpId());
        newsMessageVO.setAgentCode("declare");
        newsMessageVO.setOrgId(toUser.getOrgId());
        WxMessageUtil.sendNewsMessage(newsMessageVO);
    }
}
