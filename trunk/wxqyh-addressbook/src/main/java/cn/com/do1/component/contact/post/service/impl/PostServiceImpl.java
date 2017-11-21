package cn.com.do1.component.contact.post.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.contact.contact.util.ErrorCodeDesc;
import cn.com.do1.component.contact.post.dao.IPostReadonlyDAO;
import cn.com.do1.component.contact.post.dao.IContactPostDAO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostCatalogRefPO;
import cn.com.do1.component.contact.post.model.TbQyHrPostPO;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.post.vo.ExportPersonVO;
import cn.com.do1.component.contact.post.vo.TbQyHrPostVO;
import cn.com.do1.component.log.operationlog.util.LogOperation;
import cn.com.do1.component.util.UUID32;

import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.annotation.Resource;

/**
 * Created by liuzhibin on 2017/5/15.
 */
@Service("postService")
public class PostServiceImpl extends BaseService implements IPostService {
	private IContactPostDAO contactPostDAO;
	private IPostReadonlyDAO postReadonlyDAO;
	@Resource(name = "contactService")
	private IContactService contactService;

	@Resource
	public void setContactPostDAO(IContactPostDAO contactPostDAO) {
		this.contactPostDAO = contactPostDAO;
		setDAO(contactPostDAO);
	}

	@Resource
	public void setPostReadonlyDAO(IPostReadonlyDAO postReadonlyDAO) {
		this.postReadonlyDAO = postReadonlyDAO;
	}


	@Override
	public Pager queryPositionList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		return postReadonlyDAO.queryPositionList(searchMap, pager);
	}

	@Override
	public List<TbQyHrPostCatalogPO> getAllPositionType(String type, UserOrgVO user) throws BaseException, Exception {
		String isUse;
		if(type=="0"){
			 isUse="";
		}else {
			isUse="1";
		}
		return postReadonlyDAO.getAllPositionType(user.getOrgId(),isUse);
	}

	@Override
	public void addPositionDetail(TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO, TbQyHrPostPO tbQyHrPostPO)
			throws BaseException, Exception {
		tbQyHrPostPO.setId(UUID32.getID());
		tbQyHrPostPO.setCreateTime(new Date());
		this.insertPO(tbQyHrPostPO, false);
		if(AssertUtil.isEmpty(tbQyHrPostCatalogRefPO.getCatalogId())){
			return;
		}
		tbQyHrPostCatalogRefPO.setId(UUID32.getID());
		tbQyHrPostCatalogRefPO.setPostId(tbQyHrPostPO.getId());
		this.insertPO(tbQyHrPostCatalogRefPO, false);
	}

	@Override
	public boolean isExistPosition(String name, String orgId) throws BaseException, Exception {
		return postReadonlyDAO.isExistPosition(name, orgId);
	}

	@Override
	public TbQyHrPostVO queryPositionDetail(String id, UserOrgVO user) throws BaseException, Exception {
		return postReadonlyDAO.queryPositionDetail(id, user.getOrgId());
	}

	@Override
	public void updatePositionDetail(TbQyHrPostCatalogRefPO tbQyHrPostCatalogRefPO, TbQyHrPostPO tbQyHrPostPO,
			String id, UserOrgVO user) throws BaseException, Exception {
		TbQyHrPostPO postpo = this.searchByPk(TbQyHrPostPO.class, id);
		if(AssertUtil.isEmpty(postpo)){
			throw new NonePrintException(ErrorCodeDesc.POSITION_IS_NULL.getCode(),ErrorCodeDesc.POSITION_IS_NULL.getDesc());
		}
		if (!postpo.getOrgId().equals(user.getOrgId())) {
			throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
		}
		postpo.setDescription(tbQyHrPostPO.getDescription());
		postpo.setIsUse(tbQyHrPostPO.getIsUse());
		this.updatePO(postpo, false);
		if(!AssertUtil.isEmpty(tbQyHrPostCatalogRefPO.getCatalogId())){
			TbQyHrPostCatalogRefPO refpo = postReadonlyDAO.getCatalogRefPOByPostId(id);
			if(!AssertUtil.isEmpty(refpo)){
				if(!refpo.getCatalogId().equals(tbQyHrPostCatalogRefPO.getCatalogId())){
					refpo.setCatalogId(tbQyHrPostCatalogRefPO.getCatalogId());
					this.updatePO(refpo, false);
				}
			}else{
				TbQyHrPostCatalogRefPO po=new TbQyHrPostCatalogRefPO();
				po.setId(UUID32.getID());
				po.setCatalogId(tbQyHrPostCatalogRefPO.getCatalogId());
				po.setPostId(id);
				po.setOrgId(user.getOrgId());
				this.insertPO(po, false);
			}
		}
	}

	@Override
	public void batchDeletePosition(String[] ids, UserOrgVO user) throws BaseException, Exception {
		if(AssertUtil.isEmpty(ids)){
			return;
		}
		List<TbQyHrPostPO> postlist = this.searchByPks(TbQyHrPostPO.class, ids);
		String[] delIds=new String[postlist.size()];
		TbQyHrPostPO po;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < postlist.size(); i++) {
			po = postlist.get(i);
			if (!po.getOrgId().equals(user.getOrgId())) {
				throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
			} else {
				LogOperation.insertOperationLog(user.getUserName(), user.getPersonName(), "删除职位：" + po.getName(),  LogOperation.DEL_TYPE, WxAgentUtil.ADDRESS_BOOK_CODE, user.getOrgId(), "删除成功");
				delIds[i]=po.getId();
			}
			if(postReadonlyDAO.isExistPerson(po.getName(), user.getOrgId())){
				sb.append(po.getName()).append("、");
			}
		}
		if(sb.length() > 0){
			throw new NonePrintException(ErrorCodeDesc.POSITION_HAS_EXIST_PERSON.getCode(),sb.substring(0, sb.length()-1).toString() + ErrorCodeDesc.POSITION_HAS_EXIST_PERSON.getDesc());
		}
		if(!AssertUtil.isEmpty(delIds)){
			this.batchDel(TbQyHrPostPO.class, delIds);
			contactPostDAO.deletePositionRefByPostIds(delIds);
		}
	}

	@Override
	public Pager queryPositionPersonList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		return postReadonlyDAO.queryPositionPersonList(searchMap, pager);
	}

	@Override
	public Pager queryPositionTypeList(Map<String, Object> searchMap, Pager pager) throws BaseException, Exception {
		return postReadonlyDAO.queryPositionTypeList(searchMap, pager);
	}

	@Override
	public void batchDeletePositionType(String[] ids,UserOrgVO user) throws BaseException, Exception {
		if(AssertUtil.isEmpty(ids)){
			return;
		}
		List<TbQyHrPostCatalogPO> typelist = this.searchByPks(TbQyHrPostCatalogPO.class, ids);
		String[] delIds=new String[typelist.size()];
		TbQyHrPostCatalogPO po;
		for (int i = 0; i < typelist.size(); i++) {
			po=typelist.get(i);
			if (!po.getOrgId().equals(user.getOrgId())) {
				throw new NonePrintException(ErrorCodeDesc.USER_UN_VISIBLE.getCode(),ErrorCodeDesc.USER_UN_VISIBLE.getDesc());
			} else {
				LogOperation.insertOperationLog(user.getUserName(), user.getPersonName(), "删除职位类型：" + po.getName(),  LogOperation.DEL_TYPE, WxAgentUtil.ADDRESS_BOOK_CODE, user.getOrgId(), "删除成功");
				delIds[i]=po.getId();
			}
		}
		if(!AssertUtil.isEmpty(delIds)){
			this.batchDel(TbQyHrPostCatalogPO.class, delIds);
			contactPostDAO.deletePositionRefByCatalogIds(delIds);
		}
	}

	@Override
	public boolean isExistPositionType(String name,String orgId) throws BaseException, Exception {
		return postReadonlyDAO.isExistPositionType(name,orgId);
	}

	@Override
	public List<ExportPersonVO> searchPositionPersonList(Map<String, Object> params) throws BaseException, Exception {
		return postReadonlyDAO.searchPositionPersonList(params);
	}

	@Override
	public void initPostion(UserOrgVO user) throws BaseException, Exception {
		Pager pager = new Pager();
		pager.setPageSize(500);
		pager.setCurrentPage(1);
		pager = postReadonlyDAO.getPositionByOrgId(pager, user.getOrgId());
		List<String> postlist= new ArrayList<String>((int) pager.getTotalRows());
		postlist.addAll(pager.getPageData());
		//获取该机构的全部职位
		for(int i = 2; i <= pager.getTotalPages(); i++){
			pager.setCurrentPage(i);
			pager = postReadonlyDAO.getPositionByOrgId(pager, user.getOrgId());
			postlist.addAll(pager.getPageData());
		}
	    List<String> list = postReadonlyDAO.getExistPositionByOrgId(user.getOrgId());
        Map<String,Object> map = new HashMap<String,Object>();
         for(String position : list){
         	map.put(position,position);
         }
		List<TbQyHrPostPO> addlist = new ArrayList<TbQyHrPostPO>();
		TbQyHrPostPO po = new TbQyHrPostPO();
		for(String position :postlist){
			if(AssertUtil.isEmpty(position) || map.containsKey(position)){
				continue;
			}
			po = po.clone();
			po.setId(UUID32.getID());
			po.setName(position);
			po.setCreator(user.getUserName());
			po.setCreateTime(new Date());
			po.setIsUse(1);
			po.setDescription("");
			po.setOrgId(user.getOrgId());
			addlist.add(po);
		}
		if(!AssertUtil.isEmpty(addlist)){
			contactPostDAO.execBatchInsert(addlist);
		}
	}

	/**
	 * 模糊搜索职位名称
	 *
	 * @param searchMap 模糊搜索条件
	 * @param orgId     机构id
	 * @return 返回list
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
	 */
	@Override
	public List<TbQyHrPostVO> getPositionByVagueSearch(Map searchMap, String orgId) throws BaseException, Exception {
		if(AssertUtil.isEmpty(orgId)){
			return new ArrayList<TbQyHrPostVO>();
		}
		return postReadonlyDAO.getPositionByVagueSearch(searchMap, orgId);
	}

	/**
	 * 获取该机构的所有已启用的职位
	 *
	 * @param orgId 机构id
	 * @return 返回set
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-6-1
	 * @version 1.0
	 */
	@Override
	public Set<String> getPositionSetByOrgId(String orgId) throws BaseException, Exception {
		Set<String> set = new HashSet<String>();
		if(AssertUtil.isEmpty(orgId)){
			return set;
		}
		set.addAll(postReadonlyDAO.getPositionListByOrgId(orgId));
		return set;
	}

	/**
	 * 获取该机构的所有已启用的职位
	 * @param orgId 机构id
	 * @return 返回set
	 * @throws BaseException 这是一个异常
	 * @throws Exception 这是一个异常
	 * @author liyixin
	 * @2017-6-6
	 * @version 1.0
	 */
	public List<String> getPositionListByOrgId(String orgId) throws BaseException, Exception{
		List<String> list = new ArrayList<String>();
		if(AssertUtil.isEmpty(orgId)){
			return list;
		}
		list.addAll(postReadonlyDAO.getPositionListByOrgId(orgId));
		return list;
	}

	/**
	 * 同步从企业微信过来的用户的职位
	 *
	 * @param addList    用户po列表
	 * @param orgVO 机构vo
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-6-6
	 * @version 1.0
	 */
	@Override
	public void synUserPosition(List<TbQyUserInfoPO> addList, UserOrgVO orgVO) throws BaseException, Exception {
		if(AssertUtil.isEmpty(addList)){
			return;
		}
		Set<String> postSet = getPositionSetByOrgId(orgVO.getOrgId());
		TbQyHrPostPO po = new TbQyHrPostPO();
		List<TbQyHrPostPO> addPostList = new ArrayList<TbQyHrPostPO>();
		for(int i = 0; i < addList.size(); i++){
			TbQyUserInfoPO userPO = addList.get(i);
			//如果没填职位或该职位已存在
			if(AssertUtil.isEmpty(userPO.getPosition()) || postSet.contains(userPO.getPosition())){
				continue;
			}
			po = po.clone();
			po.setId(UUID32.getID());
			po.setName(userPO.getPosition());
			po.setCreator(orgVO.getUserName());
			po.setCreateTime(new Date());
			po.setIsUse(1);
			po.setDescription("");
			po.setOrgId(orgVO.getOrgId());
			addPostList.add(po);
		}
		if(!AssertUtil.isEmpty(addPostList)){
			contactPostDAO.execBatchInsert(addPostList);
		}
	}

	/**
	 * 批量新增职位
	 * @param map   要新增的map
	 * @param orgVO 机构vo
	 * @param cataId 分类id
	 * @throws BaseException 这是一个异常
	 * @throws Exception     这是一个异常
	 * @author liyixin
	 * @2017-6-19
	 * @version 1.0
	 */
	@Override
	public void batchAddPosition(Map<String, String> map, UserOrgVO orgVO, String cataId) throws BaseException, Exception {
		Set<String> postTitles = getPositionSetByOrgId(orgVO.getOrgId());
		Iterator iterator = map.entrySet().iterator();
		TbQyHrPostPO po = new TbQyHrPostPO();
		TbQyHrPostCatalogRefPO refPO = new TbQyHrPostCatalogRefPO();
		List<TbQyHrPostPO> postPOList = new ArrayList<TbQyHrPostPO>(map.size());
		List<TbQyHrPostCatalogRefPO> refPOList = new ArrayList<TbQyHrPostCatalogRefPO>(map.size());
		while (iterator.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			//如果职位名称已存在
			if(postTitles.contains(entry.getKey())){
				throw new NonePrintException(ErrorCodeDesc.POSITION_EXIST.getCode(), entry.getKey() + ErrorCodeDesc.POSITION_EXIST.getDesc());
			}
			po = po.clone();
			po.setId(UUID32.getID());
			po.setOrgId(orgVO.getOrgId());
			po.setCreateTime(new Date());
			po.setCreator(orgVO.getPersonName());
			po.setDescription(entry.getValue());
			po.setIsUse(1);
			po.setName(entry.getKey());
			postPOList.add(po);
			//如果分类id不为空
			if(!AssertUtil.isEmpty(cataId)){
				refPO = refPO.clone();
				refPO.setId(UUID32.getID());
				refPO.setOrgId(orgVO.getOrgId());
				refPO.setCatalogId(cataId);
				refPO.setPostId(po.getId());
				refPOList.add(refPO);
			}
		}
		if(postPOList.size() > 0){
			contactPostDAO.execBatchInsert(postPOList);
		}
		if(refPOList.size() > 0){
			contactPostDAO.execBatchInsert(refPOList);
		}
	}
}
