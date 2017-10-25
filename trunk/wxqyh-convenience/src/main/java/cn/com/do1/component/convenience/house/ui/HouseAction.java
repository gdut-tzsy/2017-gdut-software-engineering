package cn.com.do1.component.convenience.house.ui;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.exception.IllegalParameterException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.component.convenience.house.model.*;
import cn.com.do1.component.convenience.house.service.IHouseService;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import cn.com.do1.common.exception.BaseException;

import javax.annotation.Resource;
import java.sql.SQLException;

import cn.com.do1.common.util.AssertUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class HouseAction extends BaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(HouseAction.class);
    private IHouseService houseService;
    private String ids[];
    private String id;

    public IHouseService getHouseService() {
        return houseService;
    }

    @Resource
    public void setHouseService(IHouseService houseService) {
        this.houseService = houseService;
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

    /**
     * 列表查询时，页面要传递的参数
     */
    @SearchValueTypes(
            nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "testNumber", type = "number"),
            @SearchValueType(name = "testString", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = houseService.searchHouse(getSearchValue(), pager);
        addJsonPager("pageData", pager);
    }

    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void ajaxAdd() throws Exception, BaseException {

    }

    @JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void ajaxUpdate() throws Exception, BaseException {

    }

    @JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete() throws Exception, BaseException {
        //完成批量更新的代码
    }
}
