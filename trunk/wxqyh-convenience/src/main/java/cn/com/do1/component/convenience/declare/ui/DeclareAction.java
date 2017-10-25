package cn.com.do1.component.convenience.declare.ui;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.exception.IllegalParameterException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.declare.model.*;
import cn.com.do1.component.convenience.declare.service.IDeclareService;
import cn.com.do1.component.util.WxqyhBaseAction;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import cn.com.do1.common.exception.BaseException;

import javax.annotation.Resource;
import java.sql.SQLException;

import cn.com.do1.common.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class DeclareAction extends WxqyhBaseAction{
    private final static transient Logger logger = LoggerFactory.getLogger(DeclareAction.class);
    private IDeclareService declareService;
    private String ids[];
    private String id;
    private TbYsjdDeclarePO tbYsjdDeclarePO;

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

    /**
     * 列表查询时，页面要传递的参数
     */
    @SearchValueTypes(
            nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "testNumber", type = "number"),
            @SearchValueType(name = "personName", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = declareService.searchDeclareByGrider(getSearchValue(), pager);
        addJsonPager("pageData", pager);
    }

    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void ajaxAdd() throws Exception, BaseException {
        //完成新增的代码;
    }

    @JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void ajaxUpdate() throws Exception, BaseException {
        //完成更新的代码;
    }

    @JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete() throws Exception, BaseException {
        //完成批量更新的代码
    }

    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询成功", faileMsg = "查询失败"))
    public void listComment(@InterfaceParam(name = "id") @Validation(must = true,name = "申报Id") String id) throws Exception, BaseException {
        this.tbYsjdDeclarePO = this.declareService.searchByPk(TbYsjdDeclarePO.class, id);
        if(null != this.tbYsjdDeclarePO) {
            Map<String, Object> searchMap = new HashMap<String ,Object>();
            searchMap.put("declareId", id);

            Pager pager = this.getMyPager();
            Pager comments = this.declareService.getPageMeetingComments(searchMap, pager);
            boolean hasMore = pager != null && comments.getCurrentPage() < comments.getTotalPages();
            Collection<?> pageData = comments.getPageData();
            if(!AssertUtil.isEmpty(pageData)) {
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
}
