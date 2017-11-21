package cn.com.do1.component.contact.contact.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.service.ITargetService;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.core.WxqyhAppContext;

public class TargetPortalAction extends BaseAction{
	private final static transient Logger logger = LoggerFactory
			.getLogger(TargetPortalAction.class);
	@Resource
	private ITargetService targetService;
	@Resource
	private IContactService contactService;
	
	 @JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "查询目标列表成功", faileMsg = "查询目标列表失败"))
	public void getTargetPager(
			@InterfaceParam(name="groupId")@Validation(must = true, name = "发单ID") String groupId,
			@InterfaceParam(name="pageSize")@Validation(must = true, name = "每页大小") Integer pageSize,
			@InterfaceParam(name="currentPage")@Validation(must = true, name = "页码") Long currentPage,
			@InterfaceParam(name="keyWord")@Validation(must = false, name = "关键字") String keyWord
			)throws Exception,BaseException{
		 UserInfoVO userInfo = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
		 Map<String,Object> paramMap=new HashMap<String,Object>();
		 paramMap.put("groupId", groupId);
		 paramMap.put("orgId", userInfo.getOrgId());
		 if(!AssertUtil.isEmpty(keyWord)){
			 paramMap.put("keyWord", "%"+keyWord+"%");
		 }
		 paramMap.put("status", 0);
		 Pager pager=new Pager();
		 if(!AssertUtil.isEmpty(pageSize)){
			 pager.setPageSize(pageSize);
		 }
		 if(!AssertUtil.isEmpty(currentPage)){
			 pager.setCurrentPage(currentPage);
		 }
		 pager=targetService.getTargetUserPager(paramMap, pager);
		 addJsonPager("pageData", pager);
	}
}
