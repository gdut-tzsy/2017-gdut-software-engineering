package cn.com.do1.component.filehandle.filehandle.ui;

import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.util.SelectDeptUserUtil;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdDirectorPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFilePO;
import cn.com.do1.component.filehandle.filehandle.service.IFilehandleService;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileFlowVO;
import cn.com.do1.component.filehandle.filehandle.vo.TbYsjdFileVO;
import cn.com.do1.component.uploadfile.imageupload.service.IFileMediaService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import cn.com.do1.dqdp.core.permission.IUser;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Description :
 * Created by lwj on 2017-8-14.
 */
public class FilehandleMgrAction extends BaseAction {
	private IFilehandleService filehandleService;
	private String relevantDefalutUserIds;
	private IContactService contactService;
	private String range;
	private String deptIds;
	private String userIds;
	private String[] ids;
	private IFileMediaService fileMediaService;


	/**
	 * @description 设置批阅文件的主任
	 * @method directorSetting
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void directorSetting() throws Exception, BaseException {
		String userId = DqdpAppContext.getCurrentUser().getUsername();
		TbYsjdDirectorPO po = filehandleService.getApproveSetting("0");
		if (po == null) {
			po = new TbYsjdDirectorPO();
			po.setId(getUUId());
			po.setType("0");
			po.setCreatorTime(new Date());
			po.setCreatorUser(userId);
			po.setTarget(relevantDefalutUserIds);
			po.setRanges("3");
			filehandleService.insertPO(po, false);
		} else {
			po.setTarget(relevantDefalutUserIds);
			filehandleService.updatePO(po, false);
		}
	}

	/**
	 * @description 获取批阅文件主任的设置
	 * @method getDirectorSetting
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getDirectorSetting() throws Exception, BaseException {
		TbYsjdDirectorPO po = filehandleService.getApproveSetting("0");
		if (po != null) {
			List defaultPersonList;
			String target = po.getTarget();
			if (!AssertUtil.isEmpty(target)) {
				defaultPersonList = SelectDeptUserUtil.getSelectUserVOList(target);
				addJsonArray("personList", defaultPersonList);
			}
		}
		addJsonObj("po", po);
	}

	/**
	 * @description 设置文件上传权限
	 * @method directorSetting
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void fileUploadAuthSetting() throws Exception, BaseException {
		String userId = DqdpAppContext.getCurrentUser().getUsername();
		TbYsjdDirectorPO po = filehandleService.getApproveSetting("1");
		if (po == null) {
			po = new TbYsjdDirectorPO();
			po.setId(getUUId());
			po.setType("1");
			po.setCreatorTime(new Date());
			po.setCreatorUser(userId);
			po.setTarget(userIds);
			po.setDept(deptIds);
			po.setRanges(range);
			po.setChildDept(filehandleService.getChildDept(deptIds));
			filehandleService.insertPO(po, false);
		} else {
			if (!AssertUtil.isEmpty(deptIds) && !deptIds.equals(po.getDept())) {//避免再重复的递归查询降低数据库性能，先判断
				po.setChildDept(filehandleService.getChildDept(deptIds));
			}
			po.setTarget(userIds);
			po.setDept(deptIds);
			po.setRanges(range);
			filehandleService.updatePO(po, false);
		}
	}

	/**
	 * @description 获取文件上传权限的设置
	 * @method getDirectorSetting
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void getFileUploadAuthSetting() throws Exception, BaseException {
		TbYsjdDirectorPO po = filehandleService.getApproveSetting("1");
		if (po != null) {
			List defaultPersonList;
			if ("3".equals(po.getRanges())) {
				if (!AssertUtil.isEmpty(po.getDept())) {
					defaultPersonList = SelectDeptUserUtil.getSelectDeptVOList(po.getDept());
					addJsonObj("departList", defaultPersonList);
				}

				if (!AssertUtil.isEmpty(po.getTarget())) {
					defaultPersonList = SelectDeptUserUtil.getSelectUserVOList(po.getTarget());
					addJsonArray("personList", defaultPersonList);
				}
			}
		}
		addJsonObj("po", po);
	}

	/**
	 * @description 获取文件管理列表
	 * @method ajaxGetFileHandleList
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "title", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "creator", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "status", type = "string", format = "%%%s%%"),
			@SearchValueType(name = "startTimes", type = "date", format = "yyyy-MM-dd"),
			@SearchValueType(name = "endTime", type = "date", format = "yyyy-MM-dd")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void ajaxGetFileHandleList() throws Exception, BaseException {

		HttpServletRequest request = ServletActionContext.getRequest();
		Pager pager = new Pager(request, getPageSize());
		pager = filehandleService.getFileHandlePager(getSearchValue(), pager);
		addJsonPager("pageData", pager);

	}

	/**
	 * @description 批量删除
	 * @method ajaxGetFileHandleDetail
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxBatchDel() throws Exception, BaseException {
		filehandleService.batchDel(TbYsjdFilePO.class, ids.length == 1 ? ids[0].split(",") : ids);
	}

	/**
	 * @description 获取详情
	 * @method ajaxGetDetail
	 * @time 2017-8-16
	 * @author lwj
	 * @version V1.0.0
	 */
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "操作成功", faileMsg = "操作失败"))
	public void ajaxGetDetail() throws Exception, BaseException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String fileId = request.getParameter("fileId");
		if (AssertUtil.isEmpty(fileId)) {
			setActionResult("1001", "需要传入的参数为空！");
			return;
		}
		IUser user = (IUser) DqdpAppContext.getCurrentUser();
		UserOrgVO userOrgVo = this.contactService.getOrgByUserId(user.getUsername());

		//获取详情
		TbYsjdFileVO fileVO = filehandleService.getDetail(fileId);
		addJsonObj("fileVO", fileVO);
		//获取流程
		List<TbYsjdFileFlowVO> flowList = filehandleService.getFlowList(fileId);
		addJsonArray("flowList", flowList);
		//获取附件
		HashMap paramMap = new HashMap();
		paramMap.put("orgId", userOrgVo.getOrgId());
		paramMap.put("groupId", fileId);
		paramMap.put("groupType", 1);
		addJsonArray("mediaList", fileMediaService.getMediaByGroupId(paramMap));
		//获取评论
		HashMap map = new HashMap();
		map.put("fileId", fileId);
		Pager comments = new Pager(ServletActionContext.getRequest(), getPageSize());
		comments = filehandleService.getDiaryComment(map, comments);
		Collection pageData = comments.getPageData();
		if (!AssertUtil.isEmpty(pageData)) {
			addJsonArray("comments", new ArrayList(pageData));
			addJsonObj("hasMore", !pageData.isEmpty() && comments.getTotalRows() > (long) pageData.size());
		}
	}

	/**
	 * @description 获取随机id
	 * @method getUUId
	 * @Param
	 * @Return
	 * @time 2017/7/21
	 * @author lwj
	 * @version V1.0.0
	 */
	private String getUUId() throws Exception, BaseException {
		return UUID.randomUUID().toString().replace("-", "");
	}

	//get set
	@Resource
	public void setFilehandleService(IFilehandleService filehandleService) {
		this.filehandleService = filehandleService;
	}

	public void setRelevantDefalutUserIds(String relevantDefalutUserIds) {
		this.relevantDefalutUserIds = relevantDefalutUserIds;
	}

	@Resource
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Resource
	public void setFileMediaService(IFileMediaService fileMediaService) {
		this.fileMediaService = fileMediaService;
	}

}
