package cn.com.do1.component.convenience.lease.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;
import cn.com.do1.component.convenience.lease.service.ILeaseInfoService;
import cn.com.do1.component.convenience.lease.vo.TbYsjdLeaseInfoVO;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class OpenLeaseInfoAction extends BaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(OpenLeaseInfoAction.class);

    private ILeaseInfoService leaseInfoService;
    private String ids[];
    private String id;
    private TbYsjdLeaseInfoPO tbYsjdLeaseInfoPO;

    public ILeaseInfoService getLeaseInfoService() {
        return leaseInfoService;
    }

    @Resource
    public void setLeaseInfoService(ILeaseInfoService leaseInfoService) {
        this.leaseInfoService = leaseInfoService;
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

    public TbYsjdLeaseInfoPO getTbYsjdLeaseInfoPO() {
        return tbYsjdLeaseInfoPO;
    }

    public void setTbYsjdLeaseInfoPO(TbYsjdLeaseInfoPO tbYsjdLeaseInfoPO) {
        this.tbYsjdLeaseInfoPO = tbYsjdLeaseInfoPO;
    }

    /**
     * 列表查询时，页面要传递的参数
     */
    @SearchValueTypes(
            nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "testNumber", type = "number"),
            @SearchValueType(name = "creatorUserName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "title", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = leaseInfoService.searchLeaseInfo(getSearchValue(), pager);
        Collection<?> pageData = pager.getPageData();
        addJsonPager("pageData", pager);
        addJsonObj("hasMore", pageData != null && pager.getCurrentPage() < pager.getTotalPages());
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getLeaseInfo() throws Exception, BaseException{
        HttpServletRequest request = ServletActionContext.getRequest();
        String id = request.getParameter("id");

        //获取轮播图
        TbYsjdLeaseInfoPO tbYsjdLeaseInfoPO=leaseInfoService.searchByPk(TbYsjdLeaseInfoPO.class,id);
        String images = tbYsjdLeaseInfoPO.getImage();
        String[] imageList = images.split(",");
        addJsonObj("hottestInfos",imageList);
        //这里增加浏览数
        leaseInfoService.increaseViewCount(tbYsjdLeaseInfoPO, 1);

        TbYsjdLeaseInfoVO vo = leaseInfoService.getLeaseInfoVOByPO(tbYsjdLeaseInfoPO);
        //这里加1是因为上面的PO是在增加浏览次数之前创建的，PO是旧的PO，所以查到的浏览次数并没有加1
        vo.setBrowser(vo.getBrowser()+1);

        addJsonObj("tbYsjdLeaseInfoPO",vo);
    }
}
