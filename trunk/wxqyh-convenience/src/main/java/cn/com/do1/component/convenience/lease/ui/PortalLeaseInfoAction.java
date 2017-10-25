package cn.com.do1.component.convenience.lease.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.convenience.lease.model.TbYsjdLeaseInfoPO;
import cn.com.do1.component.convenience.lease.service.ILeaseInfoService;
import cn.com.do1.component.convenience.lease.vo.TbYsjdLeaseInfoVO;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.*;
/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class PortalLeaseInfoAction extends WxqyhPortalBaseAction {
    private final static transient Logger logger = LoggerFactory.getLogger(PortalLeaseInfoAction.class);
    
    private String ids[];
    private String id;
    private String[] imageUrls;
    private TbYsjdLeaseInfoPO tbYsjdLeaseInfoPO;
    private ILeaseInfoService leaseInfoService; 

    public ILeaseInfoService getLeaseInfoService() {
		return leaseInfoService;
	}
    
    @Resource(name = "leaseInfoService")
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
    
    public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

    public TbYsjdLeaseInfoPO getTbYsjdLeaseInfoPO() {
        return tbYsjdLeaseInfoPO;
    }
    public TbYsjdLeaseInfoPO setTbYsjdLeaseInfoPO() {
        return this.tbYsjdLeaseInfoPO;
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
            @SearchValueType(name = "testString", type = "string", format = "%%%s%%")
    })
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        UserInfoVO userInfoVO = getUser();
        Map<String, Object> map = this.getSearchValue();
        map.put("creator_user", userInfoVO.getUserId());
        Pager pager = new Pager(ServletActionContext.getRequest(), getPageSize());
        pager = leaseInfoService.searchLeaseInfo(map, pager);
        Collection<?> pageData = pager.getPageData();
        addJsonPager("pageData", pager);
        addJsonObj("hasMore", pageData != null && pager.getCurrentPage() < pager.getTotalPages());
    }

    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void getLeaseInfoDetail() throws Exception, BaseException {
        TbYsjdLeaseInfoPO po=leaseInfoService.searchByPk(TbYsjdLeaseInfoPO.class,id);
        addJsonObj("tbYsjdLeaseInfoPO",po);
    }

    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void ajaxAdd() throws Exception, BaseException {
    	UserInfoVO userInfoVO = getUser();
    	if (AssertUtil.isEmpty(userInfoVO)) {
			throw new BaseException("1001", "登录人信息为空，请重新登录！");
		}
    	
    	String images[] = imageUrls;
    	if(!AssertUtil.isEmpty(images)){
    		StringBuffer sb = new StringBuffer();
        	for (String string : images) {
    			sb.append(string+",");
    		}
        	//将图片路径以逗号为分割存入(此方法是去掉最后一个链接的逗号)
        	tbYsjdLeaseInfoPO.setImage(sb.toString().substring(0, sb.toString().lastIndexOf(",")));
    	}
    	
        String userId = userInfoVO.getUserId();
        String userIdName = userInfoVO.getPersonName();
        tbYsjdLeaseInfoPO.setId(UUID.randomUUID().toString());
        tbYsjdLeaseInfoPO.setCreatorUser(userId);
        tbYsjdLeaseInfoPO.setCreatorUserName(userIdName);
        tbYsjdLeaseInfoPO.setStatus("1");
        tbYsjdLeaseInfoPO.setBrowser("0");
    	tbYsjdLeaseInfoPO.setCreatorTime(new Date());
    	
		leaseInfoService.insertPO(tbYsjdLeaseInfoPO,true);
    }

    @JSONOut(catchException = @CatchException(errCode = "1003", successMsg = "更新成功", faileMsg = "更新失败"))
    public void ajaxUpdate() throws Exception, BaseException {
        //完成更新的代码;
    }

    @JSONOut(catchException = @CatchException(errCode = "1004", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete() throws Exception, BaseException {
        //完成批量更新的代码
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
