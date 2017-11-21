package cn.com.do1.component.contact.tag.ui;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserGroupPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoForPage;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagInfoPO;
import cn.com.do1.component.addressbook.tag.model.TbQyTagRefPO;
import cn.com.do1.component.addressbook.tag.util.TagStaticUtil;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.contact.contact.service.ISelectUserMgrService;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.contact.util.SecrecyUserUtil;
import cn.com.do1.component.contact.tag.service.ITagMgrService;
import cn.com.do1.component.group.defatgroup.service.IDefatgroupMgrService;
import cn.com.do1.component.util.ErrorCodeModule;
import cn.com.do1.component.util.VisibleRangeUtil;
import cn.com.do1.component.util.WxqyhPortalBaseAction;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import java.util.*;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class TagPortalAction extends WxqyhPortalBaseAction {
	@Resource(name = "tagService")
	private ITagMgrService tagService;

	@Resource(name = "selectUserService")
	private ISelectUserMgrService selectUserService;

	@Resource(name = "defatgroupService")
	private IDefatgroupMgrService defatgroupMgrService;

	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagPage() throws Exception, BaseException {
		UserInfoVO user = getUser();
		List<QyTagPageInfoVO> tagList = tagService.getTagPageInfoList(user.getOrgId(), TagStaticUtil.TAG_RANG_ALL, TagStaticUtil.TAG_INFO_STATUS_USING);
		addJsonObj("tagList", tagList);

		/**
		 * 常用群组
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user.getUserId());
		Pager<TbQyUserGroupPO> pager = new Pager<TbQyUserGroupPO>(ServletActionContext.getRequest(), 100);
		pager.setTotalRows(200);//设置200为了不用执行count语句
		pager = defatgroupMgrService.getUserGroup(pager, map);
		addJsonObj("pageData", getTagPageInfoFromGroup(pager.getPageData()));
	}

	/**
	 * 常用群组数据转为标签的对象数据
	 * @param pageData
	 * @return 返回数据
	 * @throws Exception     这是一个异常
	 * @throws BaseException 这是一个异常
	 * @author sunqinghai
	 * @date 2017 -4-14
	 */
	private List<QyTagPageInfoVO> getTagPageInfoFromGroup(Collection<TbQyUserGroupPO> pageData) throws Exception, BaseException {
		if (AssertUtil.isEmpty(pageData)) {
			return null;
		}
		QyTagPageInfoVO tagPageInfoVO;
		List<QyTagPageInfoVO> list = new ArrayList<QyTagPageInfoVO>(pageData.size());
		for (TbQyUserGroupPO po : pageData) {
			tagPageInfoVO = new QyTagPageInfoVO();
			tagPageInfoVO.setId(po.getGroupId());
			tagPageInfoVO.setTagName(po.getGroupName());
			tagPageInfoVO.setUserCount(po.getSum());
			tagPageInfoVO.setDeptCount(0);
			list.add(tagPageInfoVO);
		}
		return list;
	}

	@JSONOut(catchException = @CatchException(errCode = ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void searchTagGroupRefPage(@InterfaceParam(name="tagId")@Validation(must=true,name="标签",code= ErrorCodeModule.null_params_error)String tagId,
									  @InterfaceParam(name="agentCode")@Validation(must=false,name="应用",code= ErrorCodeModule.null_params_error)String agentCode
			)throws Exception, BaseException {
		UserInfoVO user = getUser();
		TbQyTagInfoPO po = tagService.searchByPk(TbQyTagInfoPO.class, tagId);
		if (po == null || !po.getOrgId().equals(user.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.TAG_NULL.getCode(), ErrorCodeDesc.TAG_NULL.getDesc());
		}
		if (TagStaticUtil.TAG_INFO_STATUS_USING != po.getStatus() || TagStaticUtil.TAG_RANG_ALL != po.getRang()) {
			throw new NonePrintException(ErrorCodeDesc.TAG_STATUS_ERROR.getCode(), ErrorCodeDesc.TAG_STATUS_ERROR.getDesc());
		}
		AgentCacheInfo aci = WxAgentUtil.getAgentCache(user.getCorpId(), agentCode);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", user.getOrgId());
		if (!StringUtil.isNullEmpty(agentCode) && (null == aci || !aci.isAllUserVisible())) { //不是全公司可见
			//如果用户不在可见范围内，直接提示
			if(aci==null || !VisibleRangeUtil.isUserVisibleAgentByParts(user.getUserId(),user.getDeptFullNames(),agentCode,user.getOrgId(),aci.getPartys())){
				throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(), ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
			}
			params.put("agentCode", agentCode);
		}

		List<TbQyTagRefPO> tagRefs = tagService.getTagRefList(po.getId());

		List<String> userIdsList = null;
		List<String> deptIdsList = null;
		List<TbQyUserInfoForPage> list = null;
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
			list = selectUserService.searchContactList(params, deptIdsList, userIdsList);
			SecrecyUserUtil.secrecyPageList(user, list);
		}
		addJsonObj("list", list);
	}
}
