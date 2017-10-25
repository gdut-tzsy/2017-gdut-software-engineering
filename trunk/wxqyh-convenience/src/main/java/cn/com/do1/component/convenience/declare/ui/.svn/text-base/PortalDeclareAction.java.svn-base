package cn.com.do1.component.convenience.declare.ui;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclareCommentPO;
import cn.com.do1.component.convenience.declare.model.TbYsjdDeclarePO;
import cn.com.do1.component.convenience.declare.service.IDeclareService;
import cn.com.do1.component.convenience.declare.vo.TbYsjdDeclareVO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class PortalDeclareAction extends WxqyhPortalBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(PortalDeclareAction.class);
    private IDeclareService declareService;
    private String ids[];
    private String id;
    private TbYsjdDeclarePO tbYsjdDeclarePO;
    private TbYsjdDeclareCommentPO tbYsjdDeclareCommentPO;

    public IDeclareService getDeclareService() {
        return declareService;
    }

    @Resource
    public void setDeclareService(IDeclareService declareService) {
        this.declareService = declareService;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TbYsjdDeclarePO getTbYsjdDeclarePO() {
        return tbYsjdDeclarePO;
    }

    public void setTbYsjdDeclarePO(TbYsjdDeclarePO tbYsjdDeclarePO) {
        this.tbYsjdDeclarePO = tbYsjdDeclarePO;
    }

    public TbYsjdDeclareCommentPO getTbYsjdDeclareCommentPO() {
        return tbYsjdDeclareCommentPO;
    }

    public void setTbYsjdDeclareCommentPO(TbYsjdDeclareCommentPO tbYsjdDeclareCommentPO) {
        this.tbYsjdDeclareCommentPO = tbYsjdDeclareCommentPO;
    }

    /**
     * 业主查询申报消息
     */
    @SearchValueTypes(
            nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "testNumber", type = "number"),
            @SearchValueType(name = "personName", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        Map<String, Object> map = this.getSearchValue();
        map.put("userId", userInfoVO.getUserId());
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = declareService.searchDeclare(map, pager);
        Collection<?> pageData = pager.getPageData();
        addJsonPager("pageData", pager);
        addJsonObj("hasMore", pageData != null && pager.getCurrentPage() < pager.getTotalPages());
    }


    /**
     * 网格员查询申报消息
     */
    @SearchValueTypes(
            nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "testNumber", type = "number"),
            @SearchValueType(name = "personName", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearchGrider() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        Map<String, Object> map = this.getSearchValue();
        map.put("userId", userInfoVO.getUserId());
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = declareService.searchDeclareByGrider(map, pager);
        Collection<?> pageData = pager.getPageData();
        addJsonPager("pageData", pager);
        addJsonObj("hasMore", pageData != null && pager.getCurrentPage() < pager.getTotalPages());
    }


    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "操作成功", faileMsg = "操作失败"))
    public void ajaxAdd() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        if (!AssertUtil.isEmpty(tbYsjdDeclarePO.getId())){
            //重新编辑
            declareService.updatePO(tbYsjdDeclarePO,false);
        }else {
            tbYsjdDeclarePO.setCreateName(userInfoVO.getPersonName());
            tbYsjdDeclarePO.setCreateUser(userInfoVO.getUserId());
            tbYsjdDeclarePO.setCreateTime(new Date());
            tbYsjdDeclarePO.setHeadPic(userInfoVO.getHeadPic());
            tbYsjdDeclarePO.setWxUserId(userInfoVO.getWxUserId());
            tbYsjdDeclarePO.setStatus("0");
            declareService.sendMsg(tbYsjdDeclarePO, userInfoVO);
        }
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查找成功", faileMsg = "查找失败"))
    public void getDeclareInfoDetail() throws Exception, BaseException {
        TbYsjdDeclareVO declarePO = declareService.getDeclareInfoDetail(id);
        addJsonObj("declarePO", declarePO);
    }

    @JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void updateByGrider() throws Exception, BaseException {
        TbYsjdDeclarePO declarePO = declareService.searchByPk(TbYsjdDeclarePO.class, id);
        declarePO.setStatus("1");
        declareService.updatePO(declarePO, false);
    }

    @JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete() throws Exception, BaseException {
        //完成批量更新的代码
    }

    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "评论成功", faileMsg = "评论失败"))
    public void commitComment(@InterfaceParam(name = "type") String type,@InterfaceParam(name = "commentStatus") String commentStatus) throws Exception, BaseException {
        UserInfoVO sendUser = this.getUser();
        String userId = sendUser.getUserId();
        boolean flag=false;
        List<TbYsjdDeclareCommentPO> list = this.declareService.getCommentsByUserID(userId, this.tbYsjdDeclareCommentPO.getDeclareId());
        if (!AssertUtil.isEmpty(list)) {
            TbYsjdDeclareCommentPO po = (TbYsjdDeclareCommentPO) list.get(0);
            if ((new Date()).getTime() - po.getCreateTime().getTime() <= (long) (Configuration.COMMENT_INTERVAL * 1000)) {
                return;
            }
            for (TbYsjdDeclareCommentPO commentPO:list){
                if (commentPO.getCommentStatus().equals("6")){
                    flag=true;
                }
            }
        }
        if (flag&&"6".equals(commentStatus)){
            return;
        }
        //确认处理
        if ("7".equals(commentStatus)){
            TbYsjdDeclarePO declarePO = declareService.searchByPk(TbYsjdDeclarePO.class, this.tbYsjdDeclareCommentPO.getDeclareId());
            declarePO.setStatus("1");
            declareService.updatePO(declarePO, false);
        }
        String commentId = UUID.randomUUID().toString();
        this.tbYsjdDeclareCommentPO.setId(commentId);
        this.tbYsjdDeclareCommentPO.setCreatePerson(userId);
        this.tbYsjdDeclareCommentPO.setPersonName(sendUser.getPersonName());
        this.tbYsjdDeclareCommentPO.setHeadPic(sendUser.getHeadPic());
        this.tbYsjdDeclareCommentPO.setWxUserId(sendUser.getWxUserId());
        this.tbYsjdDeclareCommentPO.setDepartmentId(sendUser.getDeptIdsForRedundancy());
        this.tbYsjdDeclareCommentPO.setDepartmentName(sendUser.getDeptFullNamesForRedundancy());
        this.tbYsjdDeclareCommentPO.setCreateTime(new Date());
        if (!AssertUtil.isEmpty(commentStatus)){
            this.tbYsjdDeclareCommentPO.setCommentStatus(commentStatus);
        }else {
            this.tbYsjdDeclareCommentPO.setCommentStatus("0");
        }
        this.tbYsjdDeclareCommentPO.setType(type);

        if ("4".equals(type)) {
            this.tbYsjdDeclareCommentPO.setDeviceNum(sendUser.getDeviceId());
        }

        this.declareService.insertPO(this.tbYsjdDeclareCommentPO, false);
        this.declareService.updateCommentNum(this.tbYsjdDeclareCommentPO.getDeclareId());
        this.addJsonObj("id", this.tbYsjdDeclareCommentPO.getId());
    }

    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void listComment(@InterfaceParam(name = "id") @Validation(must = true,name = "申报Id") String id, @InterfaceParam(name = "commentId") @Validation(must = false,name = "最后一条评论Id") String commentId, @InterfaceParam(name = "commentStatus") @Validation(must = false,name = "评论类型") String commentStatus) throws Exception, BaseException {
        UserInfoVO userVO = this.getUser();
        this.tbYsjdDeclarePO = (TbYsjdDeclarePO) this.declareService.searchByPk(TbYsjdDeclarePO.class, id);
        if(null != this.tbYsjdDeclarePO) {
            Map<String, Object> searchMap = new HashMap();
            searchMap.put("declareId", id);
            if(!AssertUtil.isEmpty(commentStatus) && "2".equals(commentStatus)) {
                searchMap.put("commentStatus", "0");
            }

            Pager pager = this.getMyPager();
            Pager comments = this.declareService.getPageMeetingComments(searchMap, pager);
            boolean hasMore = pager != null && comments.getCurrentPage() < comments.getTotalPages();
            Collection<?> pageData = comments.getPageData();
            if(!AssertUtil.isEmpty(pageData)) {
//                MeetingUtil.initCommentTime(pageData, commentId);
                this.addJsonArray("comments", new ArrayList(pageData));
            }

            this.addJsonObj("hasMore", hasMore);
//            this.tbMeetingInfoPO.setCommentCount(Integer.valueOf((int)comments.getTotalRows()));
            this.addJsonFormateObj("tbYsjdDeclarePO", this.tbYsjdDeclarePO);
        }
//        else {
//            this.setActionResult(ErrorCodeDesc.meeting_is_null.getCode(), ErrorCodeDesc.meeting_is_null.getDesc());
//        }
    }

    @JSONOut(catchException = @CatchException(errCode = "2001", successMsg = "删除成功", faileMsg = "删除失败"))
    public void deleteComment() throws Exception, BaseException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String commentId = request.getParameter("commentId");
        this.declareService.deleteComment(commentId);
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getUserInfo() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        addJsonObj("user",userInfoVO.getPersonName());
    }
}
