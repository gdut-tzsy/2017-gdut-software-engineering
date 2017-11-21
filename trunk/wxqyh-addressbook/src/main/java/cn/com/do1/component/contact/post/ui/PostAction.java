package cn.com.do1.component.contact.post.ui;

import java.util.*;

import javax.annotation.Resource;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogRefPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostPO;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.post.vo.TbQyHrPostVO;
import cn.com.do1.component.util.ErrorCodeModule;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.WxqyhBaseAction;
/**
 * Created by liuzhibin on 2017/5/15.
 */
public class PostAction extends WxqyhBaseAction {
	private final static transient Logger logger = LoggerFactory.getLogger(PostAction.class);

	@Resource(name = "postService")
	private IPostService postService;

	private String ids[];
	/**
	 * 查询职位列表
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "name", type = "string", format = "%%%s%%")})
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void queryPositionList() throws Exception, BaseException {
		UserOrgVO org = getUser();
		Map<String, Object> searchMap = getSearchValue();
		searchMap.put("orgId", getOrgId());
		addJsonPager("pageData",  postService.queryPositionList(searchMap, getPager()));
	}

	/**
	 * 查询职位分类列表
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void getAllPositionType(@InterfaceParam(name = "type") @Validation(must = true, name = "查询类型") String type) throws Exception, BaseException {
		UserOrgVO org = getUser();
		List<TbQyHrPostCatalogPO> catelogList = postService.getAllPositionType(type,org);
		addJsonArray("catelogList", catelogList);
	}

	/**
	 * 新增职位信息
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "新增成功", faileMsg = "新增失败"))
	public void addPositionDetail(
			@InterfaceParam(name = "tbQyHrPostPO") @Validation(must = true, name = "职位信息") TbQyHrPostPO tbQyHrPostPO,
			@InterfaceParam(name = "tbQyHrPostCatalogRefPO") @Validation(must = false, name = "职位关联信息") TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO)
			throws Exception, BaseException {
		UserOrgVO user = getUser();
		if(postService.isExistPosition(tbQyHrPostPO.getName(), user.getOrgId())){
			setActionResult(ErrorCodeDesc.POSITION_EXIST.getCode(), ErrorCodeDesc.POSITION_EXIST.getDesc());
			return;
		}
		tbQyHrPostPO.setOrgId(user.getOrgId());
		tbQyHrPostPO.setCreator(user.getUserName());
		tbQyHrPostCatalogRefPO.setOrgId(user.getOrgId());
		postService.addPositionDetail(tbQyHrPostCatalogRefPO, tbQyHrPostPO);
	}

	/**
	 * 根据id查询职位信息
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void queryPositionDetail(@InterfaceParam(name = "id") @Validation(must = true, name = "职位ID") String id)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostVO postInfo = postService.queryPositionDetail(id, org);
		addJsonObj("postInfo", postInfo);
	}

	/**
	 * 更新职位信息
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "修改成功", faileMsg = "修改失败"))
	public void updatePositionDetail(@InterfaceParam(name = "id") @Validation(must = true, name = "职位ID") String id,
			@InterfaceParam(name = "tbQyHrPostPO") @Validation(must = true, name = "职位信息") TbQyHrPostPO tbQyHrPostPO,
			@InterfaceParam(name = "tbQyHrPostCatalogRefPO") @Validation(must = false, name = "职位关联信息") TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		postService.updatePositionDetail(tbQyHrPostCatalogRefPO, tbQyHrPostPO, id, org);
	}

	/**
	 * 启用、禁用职位
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void doPositionUse(@InterfaceParam(name = "id") @Validation(must = true, name = "职位ID") String id,
			@InterfaceParam(name = "isUse") @Validation(must = true, name = "职位启用/禁用") String isUse)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostPO tbQyHrPostPO = postService.searchByPk(TbQyHrPostPO.class, id);
		if (!tbQyHrPostPO.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		tbQyHrPostPO.setIsUse(tbQyHrPostPO.getIsUse() == 0 ? 1 : 0);
		postService.updatePO(tbQyHrPostPO, false);
	}

	/**
	 * 批量删除职位
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */

	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "删除成功", faileMsg = "删除失败"))
	public void batchDeletePosition()
			throws Exception, BaseException {
		//@InterfaceParam(name = "ids") @Validation(must = true, name = "职位ID") String[] ids
		UserOrgVO org = getUser();
		postService.batchDeletePosition(ids, org);
	}

	/**
	 * 查询职位人员列表
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */

	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "name", type = "string", format = "%%%s%%") })
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void queryPositionPersonList(@InterfaceParam(name = "id") @Validation(must = true, name = "职位ID") String id)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostPO po = postService.searchByPk(TbQyHrPostPO.class, id);
		if (!po.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		Map<String, Object> searchMap = getSearchValue();
		searchMap.put("orgId", org.getOrgId());
		searchMap.put("postionName",  po.getName());
		addJsonPager("pageData", postService.queryPositionPersonList(searchMap, getPager()));
		addJsonObj("position",  po.getName());

	}

	/**
	 * 查询职位分类列表
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@SearchValueTypes(nameFormat = "false", value = {
			@SearchValueType(name = "name", type = "string", format = "%%%s%%") })
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void queryPositionTypeList() throws Exception, BaseException {
		UserOrgVO org = getUser();
		Map<String, Object> searchMap = getSearchValue();
		searchMap.put("orgId", org.getOrgId());
		addJsonPager("pageData", postService.queryPositionTypeList(searchMap, getPager()));
	}

	/**
	 * 批量删除分类信息
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "删除成功", faileMsg = "删除失败"))
	public void batchDeletePositionType()
			throws Exception, BaseException {
		//@InterfaceParam(name = "ids") @Validation(must = true, name = "类型ID") String[] ids
		UserOrgVO org = getUser();
		postService.batchDeletePositionType(ids, org);
	}

	/**
	 * 启用、禁用类型
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void doTypeUse(@InterfaceParam(name = "id") @Validation(must = true, name = "类型ID") String id,
			@InterfaceParam(name = "isUse") @Validation(must = true, name = "类型启用/禁用") String isUse)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostCatalogPO tbQyHrPostCatalogPO = postService.searchByPk(TbQyHrPostCatalogPO.class, id);
		if (!tbQyHrPostCatalogPO.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),
					ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		tbQyHrPostCatalogPO.setIsUse(tbQyHrPostCatalogPO.getIsUse() == 0 ? 1 : 0);
		postService.updatePO(tbQyHrPostCatalogPO, false);
	}

	/**
	 * 新增职位类型
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "新增成功", faileMsg = "新增失败"))
	public void addPositionType(
			@InterfaceParam(name = "tbQyHrPostCatalogPO") @Validation(must = true, name = "职位类型信息") TbQyHrPostCatalogPO tbQyHrPostCatalogPO)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		if (postService.isExistPositionType(tbQyHrPostCatalogPO.getName(), org.getOrgId())) {
			setActionResult(ErrorCodeDesc.POSITION_TYPE_EXIST.getCode(), ErrorCodeDesc.POSITION_TYPE_EXIST.getDesc());
			return;
		}
		tbQyHrPostCatalogPO.setId(UUID32.getID());
		tbQyHrPostCatalogPO.setOrgId(getUser().getOrgId());
		tbQyHrPostCatalogPO.setCreator(getUser().getPersonName());
		tbQyHrPostCatalogPO.setCreateTime(new Date());
		postService.insertPO(tbQyHrPostCatalogPO, false);
	}

	/**
	 * 根据id查询职位类型详情
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void getPositionTypeDetail(@InterfaceParam(name = "id") @Validation(must = true, name = "类型ID") String id)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostCatalogPO typeInfo = postService.searchByPk(TbQyHrPostCatalogPO.class, id);
		if (!typeInfo.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		addJsonObj("typeInfo", typeInfo);
	}

	/**
	 * 更新分类信息
	 * 
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "修改成功", faileMsg = "修改失败"))
	public void updatePositionType(@InterfaceParam(name = "id") @Validation(must = true, name = "类型ID") String id,
			@InterfaceParam(name = "tbQyHrPostCatalogPO") @Validation(must = true, name = "职位类型信息") TbQyHrPostCatalogPO tbQyHrPostCatalogPO)
			throws Exception, BaseException {
		UserOrgVO org = getUser();
		TbQyHrPostCatalogPO po = postService.searchByPk(TbQyHrPostCatalogPO.class, id);
		if (!po.getOrgId().equals(org.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		po.setSort(tbQyHrPostCatalogPO.getSort());
		po.setIsUse(tbQyHrPostCatalogPO.getIsUse());
		po.setDescription(tbQyHrPostCatalogPO.getDescription());
		postService.updatePO(po, false);
	}

	/**
	 * 初始化职位
	 * @throws Exception
	 * @throws BaseException
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void initPostion() throws Exception, BaseException {
		UserOrgVO org = getUser();
		postService.initPostion(org);
	}

	/**
	 *模糊搜索职位名称
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-5
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void getPositionByVagueSearch(@InterfaceParam(name = "keyWord") @Validation(must = false, name = "类型ID") String keyWord)throws Exception, BaseException{
		UserOrgVO org = getUser();
		List<TbQyHrPostVO> postVOs = new ArrayList<TbQyHrPostVO>();
		if(AssertUtil.isEmpty(keyWord)){
			addJsonObj("postVOs", postVOs);
			return;
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("name", "%"+keyWord+"%");
		postVOs = postService.getPositionByVagueSearch(params, org.getOrgId());
		addJsonObj("postVOs", postVOs);
	}

	/**
	 *查询所有已启用的职位
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-5
	 * @version 1.0
	 */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = ErrorCodeModule.system_succeed_msg, faileMsg = ErrorCodeModule.system_error_msg))
	public void getPositionList() throws BaseException, Exception{
		UserOrgVO org = getUser();
		addJsonObj("postVOs", postService.getPositionListByOrgId(org.getOrgId()));
	}

	/**
	 *批量新增岗位
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-19
	 * @version 1.0
     */
	@JSONOut(catchException = @CatchException(errCode= ErrorCodeModule.system_error, successMsg = "新增成功", faileMsg = "新增失败"))
	public void ajaxBatchAdd(@InterfaceParam(name = "postInfo") @Validation(must = true, name = "职位信息") String postInfo,
							 @InterfaceParam(name = "cataId") @Validation(name = "分类id")String cataId) throws BaseException, Exception{
		UserOrgVO orgVO = getUser();
		String[] posts = postInfo.split("##");
		Map<String, String> map = new HashMap<String, String>(posts.length);
		String[] postInfos ;
		StringBuffer errorInfo = new StringBuffer();
		for(int i = 0; i < posts.length; i ++){
			postInfos = posts[i].split("\\|");
			//如果有一样的职位
			if(map.containsKey(postInfos[0])){
				errorInfo.append(postInfos[0]).append(",");
			}
			if(postInfos.length >= 2) {//如果有填职位介绍
				//如果职位名称超过最大长度
				if(postInfos[0].length() > 30){
					throw new NonePrintException(ErrorCodeDesc.POSITION_REPEAT_MAX_LENGTH.getCode(), postInfos[0] + ErrorCodeDesc.POSITION_REPEAT_MAX_LENGTH.getDesc());
				}
				//如果职位描述超过最大长度
				if(postInfos[1].length() > 200){
					throw new NonePrintException(ErrorCodeDesc.POSITION_DESCRIBE_MAX_LENGTH.getCode(), postInfos[0] + ErrorCodeDesc.POSITION_DESCRIBE_MAX_LENGTH.getDesc());
				}
				//key是职位名称，value是职位介绍
				map.put(postInfos[0], postInfos[1]);
			}else{
				//如果职位名称超过最大长度
				if(postInfos[0].length() > 30){
					throw new NonePrintException(ErrorCodeDesc.POSITION_REPEAT_MAX_LENGTH.getCode(), postInfos[0] + ErrorCodeDesc.POSITION_REPEAT_MAX_LENGTH.getDesc());
				}
				map.put(postInfos[0],"");
			}
		}
		if(errorInfo.length() > 0){
			throw new NonePrintException(ErrorCodeDesc.POSITION_REPEAT.getCode(),  errorInfo.substring(0, errorInfo.length()-1).toString() + ErrorCodeDesc.POSITION_REPEAT.getDesc());
		}
		postService.batchAddPosition(map, orgVO, cataId);
	}



	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
