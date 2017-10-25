package cn.com.do1.component.convenience.house.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.house.service.IHouseService;
import cn.com.do1.component.convenience.house.vo.TbYsjdHouseVO;
import cn.com.do1.component.convenience.house.vo.TbYsjdReportDangerVO;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class PortalHouseAction extends WxqyhPortalBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(PortalHouseAction.class);
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
            @SearchValueType(name = "houseAddress", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        Map<String, Object> map = this.getSearchValue();
        map.put("owner", userInfoVO.getPersonName());
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = houseService.searchHouse(map, pager);
        Collection<?> pageData = pager.getPageData();
        addJsonPager("pageData", pager);
        addJsonObj("hasMore", pageData != null && pager.getCurrentPage() < pager.getTotalPages());
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getHouseInfo() throws Exception, BaseException {
        TbYsjdHouseVO vo=houseService.getHouseInfo(id);
        addJsonObj("vo",vo);
        if (vo!=null&&!vo.getLight().equals("0")){
            List<TbYsjdReportDangerVO> dangerVOList=houseService.getDangerInfo(vo.getBanId());
            addJsonArray("dangerVOList",dangerVOList);
        }
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getDangerInfo() throws Exception, BaseException {
        List<TbYsjdReportDangerVO> dangerVOList=houseService.getDangerInfo(id);
        addJsonArray("dangerVOList",dangerVOList);
    }
}
