package cn.com.do1.component.contact.tag.ui;

import javax.annotation.Resource;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.contact.tag.util.TagSyncTask;
import cn.com.do1.component.contact.tag.util.TagSyncUtil;
import cn.com.do1.component.managesetting.managesetting.util.ManageUtil;
import cn.com.do1.component.qwinterface.addressbook.TagChangerManager;
import cn.com.do1.component.runtask.runtask.vo.TbRunTaskVO;
import cn.com.do1.component.util.*;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxTagUtil;

import java.util.*;

/**
 * 标签管理
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 *
 * @author sunqinghai
 * @date 2017 -3-17
 */
public class TagAction extends WxqyhBaseAction {
	/**
	 * The Tag service.
	 */
	@Resource(name = "tagService")
	private ITagMgrService tagService;
	/**
	 * The Department service.
	 */
	@Resource(name = "departmentService")
	protected IDepartmentService departmentService;

	@Resource(name = "selectUserService")
	private ISelectUserMgrService selectUserService;

	/**
	 * 标签新增
	 * @param tbQyTagInfoPO tbQyTagInfoPO.tagName  必填，50个字 tbQyTagInfoPO.showNum  选填，排序号，数字 tbQyTagInfoPO.rang  必填，可见范围，1后台，3手机端+后台，5手机端
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupedit"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void addTag(@InterfaceParam(name="tbQyTagInfoPO")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)TbQyTagInfoPO tbQyTagInfoPO) throws Exception, BaseException{
		UserOrgVO userInfoVO = getUser();
		tbQyTagInfoPO.setId(UUID32.getID());
		tbQyTagInfoPO.setCreateTime(new Date());
		tbQyTagInfoPO.setCreator(userInfoVO.getUserName());
		tbQyTagInfoPO.setStatus(TagStaticUtil.TAG_INFO_STATUS_USING);
		tbQyTagInfoPO.setOrgId(userInfoVO.getOrgId());
		if (tbQyTagInfoPO.getShowNum() == null) {
			tbQyTagInfoPO.setShowNum(TagStaticUtil.TAG_SHOW_NUM_DEFAULT);
		}
		//判断是否重名
		if (tagService.getIsRepeat(userInfoVO.getOrgId(), tbQyTagInfoPO.getTagName(), tbQyTagInfoPO.getId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_IS_REPEAT.getCode(), ErrorCodeDesc.TAG_IS_REPEAT.getDesc());
		}
		tagService.addTag(userInfoVO, tbQyTagInfoPO, null, null);
		addJsonObj("tbQyTagInfoPO", getTagVO(tbQyTagInfoPO));
	}

	/**
	 * 标签编辑
	 * @param tbQyTagInfoPO
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupedit"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void updateTag(@InterfaceParam(name="tbQyTagInfoPO")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)TbQyTagInfoPO tbQyTagInfoPO) throws Exception, BaseException{
		//状态存在异常
		if (TagStaticUtil.TAG_RANG_MGR != tbQyTagInfoPO.getRang() && TagStaticUtil.TAG_RANG_ALL != tbQyTagInfoPO.getRang() && TagStaticUtil.TAG_RANG_APP != tbQyTagInfoPO.getRang()) {
			throw new NonePrintException(ErrorCodeDesc.TAG_RANG_ERROR.getCode(), ErrorCodeDesc.TAG_RANG_ERROR.getDesc());
		}
		UserOrgVO userInfoVO = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tbQyTagInfoPO.getId());
		if (po == null || !po.getOrgId().equals(userInfoVO.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}
		//判断是否重名，如果修改了名称才判断
		boolean isUpdateName = !tbQyTagInfoPO.getTagName().equals(po.getTagName());
		if (isUpdateName && tagService.getIsRepeat(userInfoVO.getOrgId(), tbQyTagInfoPO.getTagName(), tbQyTagInfoPO.getId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_IS_REPEAT.getCode(), ErrorCodeDesc.TAG_IS_REPEAT.getDesc());
		}
		po.setTagName(tbQyTagInfoPO.getTagName());
		po.setRang(tbQyTagInfoPO.getRang());
		po.setShowNum(tbQyTagInfoPO.getShowNum());
		tagService.updateTag(userInfoVO, po, isUpdateName);
		addJsonObj("tbQyTagInfoPO", getTagVO(po));
	}

	/**
	 * 新增成员
	 * @param tagId
	 * @param userIds
	 * @param deptIds
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupedit"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void addTagRef(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
						  @InterfaceParam(name="userIds")@Validation(must=false,name="成员",code= ErrorCodeModule.null_params_error)String userIds,
						  @InterfaceParam(name="deptIds")@Validation(must=false,name="部门",code= ErrorCodeModule.null_params_error)String deptIds) throws Exception, BaseException{
		UserOrgVO userInfoVO = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(userInfoVO.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		tagService.addTagRef(userInfoVO, po, userIds, deptIds);
		addJsonObj("tbQyTagInfoPO", getTagVO(po));
	}

	/**
	 * 标签删除
	 * @param tagId
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupdel"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void delTag(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId) throws Exception, BaseException{
		UserOrgVO userInfoVO = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);

		if (po == null) {
			return;
		}
		if (!po.getOrgId().equals(userInfoVO.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		if (tagService.getHasTagRef(po.getId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_REF_NOT_NULL.getCode(), ErrorCodeDesc.TAG_REF_NOT_NULL.getDesc());
		}
		//先调用前置条件，验证是否允许删除
		TagChangerManager.delTagPre(userInfoVO, po.getId());
		if (!StringUtil.isNullEmpty(po.getWxTagId())) {
			WxTagUtil.deleteTag(userInfoVO.getCorpId(), WxAgentUtil.getAddressBookCode(), po.getWxTagId());
		}
		tagService.delPO(po);
	}

	/**
	 * 移除成员
	 * @param tagId
	 * @param idArray  需要删除成员的id，以,分割
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupdel"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void delTagRef(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
						  @InterfaceParam(name="idArray")@Validation(must=true,name="移除的成员",code= ErrorCodeModule.null_params_error)String idArray) throws Exception, BaseException{
		UserOrgVO userInfoVO = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(userInfoVO.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		List<TbQyTagRefPO> list = tagService.getTagRefListByIds(tagId, idArray.split(","));
		tagService.delTagRef(userInfoVO, po, list);
		addJsonObj("tbQyTagInfoPO", getTagVO(po));
	}

	/**
	 * 启用和禁用
	 * @param tagId
	 * @param status 状态，0禁用，1启用
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"groupedit"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void updateTagStatus(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
						  @InterfaceParam(name="status")@Validation(must=true,name="状态",code= ErrorCodeModule.null_params_error)String status) throws Exception, BaseException{
		UserOrgVO userInfoVO = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(userInfoVO.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		int statusInt = Integer.parseInt(status);
		//状态存在异常
		if (TagStaticUtil.TAG_INFO_STATUS_DISABLE != statusInt && TagStaticUtil.TAG_INFO_STATUS_USING != statusInt) {
			throw new NonePrintException(ErrorCodeDesc.TAG_STATUS_ERROR.getCode(), ErrorCodeDesc.TAG_STATUS_ERROR.getDesc());
		}
		//如果启用标签，并且微信侧不存在此标签，先新增到微信
		if (TagStaticUtil.TAG_INFO_STATUS_USING == statusInt && StringUtil.isNullEmpty(po.getWxTagId())) {
			TagSyncUtil.syncToWeixin(userInfoVO, po);
		}

		TbQyTagInfoPO updatePO = new TbQyTagInfoPO();
		updatePO.setId(tagId);
		updatePO.setStatus(statusInt);
		tagService.updatePO(updatePO, false);
		addJsonObj("tbQyTagInfoPO", getTagVO(po));
	}

	/**
	 * 同步标签
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"tagsync"})
    @JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void synTag() throws Exception, BaseException{
		UserOrgVO org = getUser();
		if (Configuration.AUTO_CORPID.equals(org.getCorpId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NOT_USE.getCode(), ErrorCodeDesc.TAG_NOT_USE.getDesc());
		}
		List<TbRunTaskVO> list = TagSyncTask.getUserTask(org.getOrgId());
		if (!AssertUtil.isEmpty(list)) {
			addJsonObj("taskId", list.get(0).getId());
			setActionResult(ErrorCodeDesc.TAG_SYNC_ING.getCode(), ErrorCodeDesc.TAG_SYNC_ING.getDesc());
			return;
		}
		if (!Configuration.IS_QIWEIYUN) {
			String id = TagSyncTask.insertUserTask(org.getCorpId(), org.getOrgId(), org.getUserName());
			setActionResult(ErrorCodeDesc.TAG_SYNC_BINDING_PRIVATE.getCode(), ErrorCodeDesc.TAG_SYNC_BINDING_PRIVATE.getDesc());
			addJsonObj("taskId", id);
			return;
		}
		int times = tagService.checkSyncTimeAndAdd(org, isVip(org));
		String id = TagSyncTask.insertUserTask(org.getCorpId(), org.getOrgId(), org.getUserName());
		if (ManageUtil.isManageBinDing(org)) {//需要提醒管理员绑定通讯录用户
			setActionResult(ErrorCodeDesc.TAG_SYNC_BINDING.getCode(), String.format(ErrorCodeDesc.TAG_SYNC_BINDING.getDesc(), times));
		} else {
			setActionResult(ErrorCodeDesc.TAG_SYNC_UNBINDING.getCode(), String.format(ErrorCodeDesc.TAG_SYNC_UNBINDING.getDesc(), times));
		}
		addJsonObj("taskId", id);
		//TagSyncUtil.syncAllTag(org.getUserName());
	}

	/**
	 * 获取同步任务是否执行完成
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-31
	 */
	@ActionRoles({"tagsync"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void synTagStatus(@InterfaceParam(name="taskId")@Validation(must=true,name="同步标识",code= ErrorCodeModule.null_params_error)String taskId) throws Exception, BaseException{
		UserOrgVO org = getUser();
		addJsonObj("isSucceed", !TagSyncTask.existUserTask(org.getOrgId(), taskId));
	}

	/**
	 * 标签列表显示，根据排序号从小到大排列
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@ActionRoles({"grouplist"})
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagPage() throws Exception, BaseException {
		UserOrgVO org = getUser();
		List<QyTagPageInfoVO> tagList = tagService.getTagPageInfoList(org.getOrgId());
		addJsonObj("tagList", tagList);
	}

	/**
	 * 原群组列表显示，根据排序号从小到大排列，只会返回启用的标签
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagGroupPage() throws Exception, BaseException {
		UserOrgVO org = getUser();
		List<QyTagPageInfoVO> tagList = tagService.getTagPageInfoList(org.getOrgId(), TagStaticUtil.TAG_INFO_STATUS_USING);
		addJsonObj("tagList", tagList);
	}

	/**
	 * 标签成员分页展示
	 * @param tagId
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
/*@SearchValueTypes(
			nameFormat="false",value={
			@SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
			@SearchValueType(name = "testNumber", type = "number"),
			@SearchValueType(name = "title", type="string", format = "%%%s%%")
	})*/
	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagRefPage(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId
			)throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}

		Pager<QyTagRefPageVO> pager = getPager(); //如果已知总条数，传入pageTotalRows
		pager = tagService.getTagRefPage(pager, tagId);
		getTagRefDetail(po, pager.getPageData());
		if (pager.getCurrentPage() == 1 && pager.getTotalRows() == 0
				&& ((po.getUserCount() != null && po.getUserCount() != 0)
				|| (po.getDeptCount() != null && po.getDeptCount() != 0))) {
			//需要更新部门数和用户数，修复有误的数据
			tagService.updateTagUserAndDeptTotal(po, -po.getUserCount(), -po.getDeptCount());
		}
		addJsonObj("pager", pager);
	}

	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagGroupRefPage(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
									  @InterfaceParam(name="agentCode")@Validation(must=false,name="应用",code= ErrorCodeModule.null_params_error)String agentCode,
									  @InterfaceParam(name="toUser")@Validation(must=false,name="转用户",code= ErrorCodeModule.null_params_error)String toUser)throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}
		if (TagStaticUtil.TAG_INFO_STATUS_USING != po.getStatus()) {
			throw new NonePrintException(ErrorCodeDesc.TAG_STATUS_ERROR.getCode(), ErrorCodeDesc.TAG_STATUS_ERROR.getDesc());
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", org.getOrgId());
		Pager pager = getPager(); //如果已知总条数，传入pageTotalRows
		//不需要将部门转为人员，或者标签的部门数为0
		if (StringUtil.isNullEmpty(toUser) || po.getDeptCount() == 0) {
			pager = tagService.getTagRefPage(pager, tagId);
			getTagGroupRefDetail(po, pager.getPageData());
		}
		else {
			List<TbQyTagRefPO> tagRefs = tagService.getTagRefList(po.getId());

			List<String> userIdsList = null;
			List<String> deptIdsList = null;
			if (!AssertUtil.isEmpty(tagRefs)) {
				for(TbQyTagRefPO refPO : tagRefs){
					if(TagStaticUtil.TAG_REF_TYPE_USER == refPO.getMenberType()){	//用户
						if (null == userIdsList) {
							userIdsList = new ArrayList<String>(tagRefs.size());
						}
						userIdsList.add(refPO.getMenberId());
					} else {	//部门
						if (null == deptIdsList) {
							deptIdsList = new ArrayList<String>(tagRefs.size());
						}
						deptIdsList.add(refPO.getMenberId());
					}
				}
				if (!AssertUtil.isEmpty(userIdsList)){
					params.put("userIds", userIdsList);
				}
				pager = selectUserService.searchContact(params,pager, ListUtil.toArrays(deptIdsList), ListUtil.toArrays(userIdsList));
				userPageToTagRefPageVO(pager);
			}
		}
		addJsonObj("pager", pager);
	}

	/**
	 * 用户信息转为标签成员分页vo
	 * @param pager
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2017 -3-30
	 */
	private void userPageToTagRefPageVO (Pager<TbQyUserInfoForList> pager) throws Exception, BaseException {
		if (AssertUtil.isEmpty(pager.getPageData())) {
			return;
		}
		Collection<TbQyUserInfoForList> pagerData = pager.getPageData();
		QyTagRefPageVO qyTagRefPageVOClone = new QyTagRefPageVO();
		List<QyTagRefPageVO> list = new ArrayList<QyTagRefPageVO>(pagerData.size());
		for (TbQyUserInfoForList vo : pagerData) {
			QyTagRefPageVO pag = qyTagRefPageVOClone.clone();
			pag.setId(vo.getId());
			pag.setMenberId(vo.getUserId());
			pag.setMenberType(TagStaticUtil.TAG_REF_TYPE_USER_STRING);
			pag.setName(vo.getPersonName());
			pag.setHeadPic(vo.getHeadPic());
			pag.setPinyin(vo.getPinyin());
			pag.setWxId(vo.getWxUserId());
			list.add(pag);
		}
		pager.setPageData(list);
	}

	/**
	 * 用户信息转为标签成员分页vo
	 * @param list
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2017 -3-30
	 */
	private List<QyTagRefPageVO> userListToTagRefPageVO (List<TbQyUserInfoForPage> list) {
		if (AssertUtil.isEmpty(list)) {
			return null;
		}
		QyTagRefPageVO qyTagRefPageVOClone = new QyTagRefPageVO();
		List<QyTagRefPageVO> tagRefPageVOList = new ArrayList<QyTagRefPageVO>(list.size());
		for (TbQyUserInfoForPage vo : list) {
			QyTagRefPageVO pag = qyTagRefPageVOClone.clone();
			pag.setId(vo.getId());
			pag.setMenberId(vo.getUserId());
			pag.setMenberType(TagStaticUtil.TAG_REF_TYPE_USER_STRING);
			pag.setName(vo.getPersonName());
			pag.setHeadPic(vo.getHeadPic());
			pag.setPinyin(vo.getPinyin());
			pag.setWxId(vo.getWxUserId());
			tagRefPageVOList.add(pag);
		}
		return tagRefPageVOList;
	}

	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagGroupRefAll(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
									 @InterfaceParam(name="agentCode")@Validation(must=false,name="应用",code= ErrorCodeModule.null_params_error)String agentCode,
									 @InterfaceParam(name="toUser")@Validation(must=false,name="转用户",code= ErrorCodeModule.null_params_error)String toUser)throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}
		if (TagStaticUtil.TAG_INFO_STATUS_USING != po.getStatus()) {
			throw new NonePrintException(ErrorCodeDesc.TAG_STATUS_ERROR.getCode(), ErrorCodeDesc.TAG_STATUS_ERROR.getDesc());
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", org.getOrgId());

		List<QyTagRefPageVO> tagRefs = tagService.getTagRefPageVOList(po.getId());
		//不需要将部门转为人员
		if (StringUtil.isNullEmpty(toUser) || po.getDeptCount() == 0) {
			getTagGroupRefDetail(po, tagRefs);
		}
		else {
			List<String> userIdsList = null;
			List<String> deptIdsList = null;
			List<TbQyUserInfoForPage> list = null;
			if (!AssertUtil.isEmpty(tagRefs)) {
				for(QyTagRefPageVO refPO : tagRefs){
					if(TagStaticUtil.TAG_REF_TYPE_USER_STRING.equals(refPO.getMenberType())){	//用户
						if (null == userIdsList) {
							userIdsList = new ArrayList<String>(tagRefs.size());
						}
						userIdsList.add(refPO.getMenberId());
					} else {	//部门
						if (null == deptIdsList) {
							deptIdsList = new ArrayList<String>(tagRefs.size());
						}
						deptIdsList.add(refPO.getMenberId());
					}
				}
				list = selectUserService.searchContactList(params, deptIdsList, userIdsList);
				tagRefs = userListToTagRefPageVO(list);
			}
		}
		addJsonObj("persons", tagRefs);
	}

	/**
	 * 标签成员分页展示(用于群组选人)，不会展示用户部门信息
	 * @param pagerData
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-30
	 */
	private void getTagGroupRefDetail (TbQyTagInfoPO po, Collection<QyTagRefPageVO> pagerData) throws Exception, BaseException {
		if (AssertUtil.isEmpty(pagerData)) {
			return;
		}
		Map<String, QyTagRefPageVO> map = new HashMap<String, QyTagRefPageVO>(pagerData.size());
		List<String> deptList = null;
		List<String> userList = null;
		for (QyTagRefPageVO vo : pagerData) {
			if (TagStaticUtil.TAG_REF_TYPE_DEPT_STRING.equals(vo.getMenberType())) {
				if (deptList == null) {
					deptList = new ArrayList<String>(pagerData.size());
				}
				deptList.add(vo.getMenberId());
			}
			else {
				if (userList == null) {
					userList = new ArrayList<String>(pagerData.size());
				}
				userList.add(vo.getMenberId());
			}
			map.put(vo.getMenberId(), vo);
		}
		if (deptList != null) {
			List<TbDepartmentInfoPO> deptS = departmentService.getDeptsByIds(deptList);
			if (!AssertUtil.isEmpty(deptS)) {
				QyTagRefPageVO tagRefPageVO;
				for (TbDepartmentInfoPO dept : deptS) {
					tagRefPageVO = map.get(dept.getId());
					tagRefPageVO.setDeptFullName(dept.getDeptFullName());
					tagRefPageVO.setName(dept.getDepartmentName());
					tagRefPageVO.setWxId(dept.getWxId());
					map.remove(dept.getId());
				}
			}
		}
		if (userList != null) {
			List<TbQyUserInfoVO> users = contactService.getUserInfoByUserIds(ListUtil.toArrays(userList));
			if (!AssertUtil.isEmpty(users)) {
				QyTagRefPageVO tagRefPageVO;
				for (TbQyUserInfoVO vo : users) {
					tagRefPageVO = map.get(vo.getUserId());
					tagRefPageVO.setName(vo.getPersonName());
					tagRefPageVO.setWxId(vo.getWxUserId());
					tagRefPageVO.setHeadPic(vo.getHeadPic());
					map.remove(vo.getUserId());
				}
			}
		}
		//删除已经离职或者已经删除的用户
		delInvalidRef(po, map, pagerData);
	}

	/**
	 * 标签成员分页展示
	 * @param pagerData
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-17
	 */
	private void getTagRefDetail (TbQyTagInfoPO po, Collection<QyTagRefPageVO> pagerData) throws Exception, BaseException {
		if (AssertUtil.isEmpty(pagerData)) {
			return;
		}
		Map<String, QyTagRefPageVO> map = new HashMap<String, QyTagRefPageVO>(pagerData.size());
		List<String> deptList = null;
		List<String> userList = null;
		for (QyTagRefPageVO vo : pagerData) {
			if (TagStaticUtil.TAG_REF_TYPE_DEPT_STRING.equals(vo.getMenberType())) {
				if (deptList == null) {
					deptList = new ArrayList<String>(pagerData.size());
				}
				deptList.add(vo.getMenberId());
			}
			else {
				if (userList == null) {
					userList = new ArrayList<String>(pagerData.size());
				}
				userList.add(vo.getMenberId());
			}
			map.put(vo.getMenberId(), vo);
		}
		if (deptList != null) {
			List<TbDepartmentInfoPO> deptS = departmentService.getDeptsByIds(deptList);
			if (!AssertUtil.isEmpty(deptS)) {
				QyTagRefPageVO tagRefPageVO;
				for (TbDepartmentInfoPO dept : deptS) {
					tagRefPageVO = map.get(dept.getId());
					tagRefPageVO.setDeptFullName(dept.getDeptFullName());
					tagRefPageVO.setName(dept.getDepartmentName());
					tagRefPageVO.setWxId(dept.getWxId());
					map.remove(dept.getId());
				}
			}
		}
		if (userList != null) {
			List<UserRedundancyInfoVO> users = contactService.getUserRedundancys(ListUtil.toArrays(userList));
			if (!AssertUtil.isEmpty(users)) {
				QyTagRefPageVO tagRefPageVO;
				for (UserRedundancyInfoVO vo : users) {
					tagRefPageVO = map.get(vo.getUserId());
					tagRefPageVO.setDeptFullName(vo.getDeptFullName());
					tagRefPageVO.setName(vo.getPersonName());
					tagRefPageVO.setWxId(vo.getWxUserId());
					tagRefPageVO.setHeadPic(vo.getHeadPic());
					map.remove(vo.getUserId());
				}
			}
		}
		//删除已经离职或者已经删除的用户
		delInvalidRef(po, map, pagerData);
	}

	/**
	 * 删除无效的关联信息
	 * @param po 标签po
	 * @param map 关联信息
	 * @param pagerData 分页信息
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -3-22
	 */
	private void delInvalidRef (TbQyTagInfoPO po, Map<String, QyTagRefPageVO> map, Collection<QyTagRefPageVO> pagerData) throws Exception, BaseException {
		if (map.size() > 0) {
			int size = map.size();
			String[] ids = new String[size];
			Iterator<QyTagRefPageVO> iterator = map.values().iterator();
			QyTagRefPageVO vo;
			int i = 0;
			int addUserSize = 0;
			int addDeptSize = 0;
			while (iterator.hasNext()) {
				vo = iterator.next();
				ids[i] = vo.getId();
				pagerData.remove(vo);
				if (TagStaticUtil.TAG_REF_TYPE_DEPT_STRING.equals(vo.getMenberType())) {
					addDeptSize --;
				}
				else {
					addUserSize --;
				}
				i++;
			}
			tagService.batchDel(TbQyTagRefPO.class, ids);
			if (addUserSize < 0 || addDeptSize < 0) {
				tagService.updateTagUserAndDeptTotal(po, addUserSize, addDeptSize);
			}
		}
	}

	/**
	 * po转为页面显示的po
	 * @param po
	 * @return 返回数据
	 * @author sunqinghai
	 * @date 2017 -3-22
	 */
	private QyTagPageInfoVO getTagVO (TbQyTagInfoPO po) {
		QyTagPageInfoVO vo = new QyTagPageInfoVO();
		vo.setShowNum(po.getShowNum());
		vo.setRang(po.getRang());
		vo.setTagName(po.getTagName());
		vo.setId(po.getId());
		vo.setStatus(po.getStatus());
		vo.setUserCount(po.getUserCount());
		vo.setDeptCount(po.getDeptCount());
		return vo;
	}
}
