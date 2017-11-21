package cn.com.do1.component.contact.tag.thread;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagThread implements Runnable {
	private final static transient Logger logger = LoggerFactory
			.getLogger(TagThread.class);
	private String corpId;
	private String orgId;
	public static int count = 0;

	public TagThread(String corpId, String orgId) {
		this.corpId = corpId;
		this.orgId = orgId;
	}

	@Override
	public void run() {
		/*logger.debug("=======================开始同步标签============================:corpId="+corpId+",orgId="+orgId);
		if(!AssertUtil.isEmpty(corpId)){
			JSONObject jsonObject = WxTagUtil.getTag(corpId, WxAgentUtil.getAddressBookCode());
			if(AssertUtil.isEmpty(jsonObject)){	//报文能为空直接跳过
				return;
			}
			if(jsonObject.getInt("errcode") != 0 || !jsonObject.has("taglist") ){
				return;
			}
			*//*logger.debug("taglistStr:" + taglistStr);
			try {
				// 删除机构下的所有标签
				tagService.delTagByOrgId(orgId);
			} catch (BaseException e1) {
				logger.error("删除机构下的所有标签出错：orgId=" + orgId + e1.getMessage(), e1);
			} catch (Exception e1) {
				logger.error("删除机构下的所有标签出错：orgId=" + orgId + e1.getMessage(), e1);
			}*//*
			logger.debug("============开始同步标签成员===========:corpId="+corpId+",orgId="+orgId);
			// 遍历接口数据，保存新的标签数据
			TbQyTagInfoPO tagInfo;
			JSONArray jsonArray = jsonObject.getJSONArray("taglist");
			int length = jsonArray.size();
			try {
				if(length==0){
					try {
						// 删除机构下的所有标签
						tagService.delTagByOrgId(orgId);
					} catch (BaseException e1) {
						logger.error("删除机构下的所有标签出错：orgId=" + orgId + e1.getMessage(), e1);
					} catch (Exception e1) {
						logger.error("删除机构下的所有标签出错：orgId=" + orgId + e1.getMessage(), e1);
					}
					return;
				}
				JSONObject tag = null;
				Map<String, TbQyTagInfoPO> taginfos = new HashMap<String, TbQyTagInfoPO>(10);
				try {
					//获取标签信息
					List<TbQyTagInfoPO> tagInfoPOs = tagService.getTagInfoList(orgId);
					if(tagInfoPOs!=null && tagInfoPOs.size()>0){
						for (TbQyTagInfoPO tbQyTagInfoPO : tagInfoPOs) {
							taginfos.put(tbQyTagInfoPO.getWxTagId(), tbQyTagInfoPO);
						}
					}
				} catch (Exception e1) {
					logger.error("TagThread run getTagInfoList error orgId=" + orgId, e1);
					ExceptionCenter.addException(e1, "TagThread run getTagInfoList error orgId=" + orgId, "orgId=" + orgId);
				}
				String tagid;
				for (int i = 0; i < length; i++) {
					try {
						tag = jsonArray.getJSONObject(i);
						tagid = tag.getString("tagid");
						tagInfo = taginfos.get(tagid);
						if (tagInfo == null) {
							tagInfo = new TbQyTagInfoPO();
							tagInfo.setId(UUID.randomUUID().toString());
							tagInfo.setOrgId(orgId);
							tagInfo.setWxTagId(tagid);
							tagInfo.setTagName(tag.getString("tagname"));
							tagInfo.setCreateTime(new Date());
							tagService.insertTagPO(tagInfo);
							// 同步标签成员
							synTagMenbers(corpId, orgId, tagInfo.getWxTagId() ,tagInfo.getId(), true);
						}
						else {
							taginfos.remove(tagid);
							//更新部门名称
							if(StringUtil.isNullEmpty(tagInfo.getTagName()) || !tagInfo.getTagName().equals(tag.getString("tagname"))){
								tagInfo.setTagName(tag.getString("tagname"));
								tagService.updatePO(tagInfo, true);
							}
							// 同步标签成员
							synTagMenbers(corpId, orgId, tagInfo.getWxTagId() ,tagInfo.getId(), false);
						}
					} catch (BaseException e) {
						logger.error(
								"TagThread run保存标签出错：orgId=" + orgId + ",tagid="
										+ tag.getString("tagid") + ",tagname="
										+ tag.getString("tagname") + e.getMessage(), e);
					} catch (Exception e) {
						logger.error(
								"TagThread run保存标签出错：orgId=" + orgId + ",tagid="
										+ tag.getString("tagid") + ",tagname="
										+ tag.getString("tagname") + e.getMessage(), e);
					}
				}
				//删除需要删除的标签
				if(taginfos.size()>0){
					Collection<TbQyTagInfoPO> collections = taginfos.values();
					List<String> idsList = new ArrayList<String>();
					for (TbQyTagInfoPO tbQyTagInfoPO : collections) {
						try {
							idsList.add(tbQyTagInfoPO.getId());
							tagService.delTagRefByTagId(tbQyTagInfoPO.getId());
						} catch (Exception e) {
							logger.error("TagThread run删除标签关联数据出错：orgId=" + orgId + ",idsList="+ idsList.toString(), e);
						} catch (BaseException e) {
							logger.error("TagThread run删除标签出错：orgId=" + orgId + ",idsList="+ idsList.toString(), e);
						}
					}
					try {
						String[] ids = new String[idsList.size()];
						ids = idsList.toArray(ids);
						tagService.batchDel(TbQyTagInfoPO.class, ids);
					} catch (Exception e) {
						logger.error("TagThread run删除标签出错：orgId=" + orgId + ",idsList="+ idsList.toString(), e);
					} catch (BaseException e) {
						logger.error("TagThread run删除标签出错：orgId=" + orgId + ",idsList="+ idsList.toString(), e);
					}
				}
			} finally {
				try {
					//同步标签关联成员的ID
					tagService.updateTagMenber(orgId);
				} catch (BaseException e) {
					logger.error("TagThread run更新标签成员关联出错：orgId=" + orgId + e.getMessage(),
							e);
				} catch (Exception e) {
					logger.error("TagThread run更新标签成员关联出错：orgId=" + orgId + e.getMessage(),
							e);
				}
				logger.debug("==========开始同步可见范围==========:corpId="+corpId+",orgId="+orgId);
				try {
					// 更新应用可见范围用户
					ChangeAgentUserUtil.changeAllAgentUser(corpId);
					//experienceapplicationService.synAgentVisibleRange(orgId, corpId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.debug("=======================结束同步标签==============================:corpId="+corpId+",orgId="+orgId);
			}
		}
		if(count > 0){
			//执行完之后数量减一
			count --;
		}
		logger.debug("=======================当前还需初始化的机构数为:"+count);*/
	}


	
	/**
	 * 同步企业标签信息
	 * @param orgId
	 * @param tags
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	public void synTagInfos(String orgId, List<JSONObject> tags, String agentCode){
		/*try {
			if(tags!=null && tags.size()>0){
				Map<String, String> tagNames = null;
				TbQyTagInfoPO tagInfoPO = null;
				for (JSONObject tag : tags) {
					try {
						tagInfoPO = tagService.getTagInfoByWxTagId(orgId, tag.getString("wxTagId"));
						if(tagInfoPO==null){
							if(tagNames == null){
								tagNames = new HashMap<String, String>();
								JSONObject jsonObject = WxTagUtil.getTag(corpId, agentCode);
								if(jsonObject != null && jsonObject.getInt("errcode")==0){//errcode为0表示接口成功
									JSONArray jsonArray = jsonObject.getJSONArray("taglist");
									for (int i = 0; i < jsonArray.size(); i++) {
										JSONObject object = jsonArray.getJSONObject(i);
										tagNames.put(object.getString("tagid"), object.getString("tagname"));
									}
								}
								if(tagNames.size()==0){
									return;
								}
							}
							tagInfoPO = new TbQyTagInfoPO();
							tagInfoPO.setId(UUID.randomUUID().toString());
							tagInfoPO.setOrgId(orgId);
							tagInfoPO.setCorpId(corpId);
							tagInfoPO.setWxTagId(tag.getString("wxTagId"));
							tagInfoPO.setTagName(tagNames.get(tagInfoPO.getWxTagId()));
							tagInfoPO.setCreateTime(new Date());
							tagService.insertTagPO(tagInfoPO);
							// 同步标签成员
							synTagMenbers(orgId, tagInfoPO.getId(), tag, true);
						}
						else{
							//更新部门名称，暂不更新标签名称，需要时再更新
							*//*if(StringUtil.isNullEmpty(tagInfoPO.getTagName()) || !tagInfoPO.getTagName().equals(tagNames.get(tagInfoPO.getWxTagId()))){
								tagInfoPO.setTagName(tagNames.get(tagInfoPO.getWxTagId()));
								tagService.updatePO(tagInfoPO, true);
							}*//*
							// 同步标签成员
							synTagMenbers(orgId, tagInfoPO.getId(), tag, false);
						}
					} catch (BaseException e) {
						logger.error(
								"TagThread synTagInfos orgId=" + orgId + ",tagid="
										+ tag.getString("tagid") + ",tagname="
										+ tag.getString("tagname"), e);
						ExceptionCenter.addException(e, "TagThread synTagInfos orgId=" + orgId, "orgId=" + orgId + ",tagid="
								+ tag.getString("tagid") + ",tagname="
								+ tag.getString("tagname"));
					} catch (Exception e) {
						logger.error(
								"TagThread synTagInfos orgId=" + orgId + ",tagid="
										+ tag.getString("tagid") + ",tagname="
										+ tag.getString("tagname"), e);
						ExceptionCenter.addException(e, "TagThread synTagInfos orgId=" + orgId, "orgId=" + orgId + ",tagid="
								+ tag.getString("tagid") + ",tagname="
								+ tag.getString("tagname"));
					} finally{
						try {
							if(tagInfoPO != null){
								//同步标签关联成员的ID
								tagService.updateTagMenberByTagId(tagInfoPO.getId());
							}
						} catch (BaseException e) {
							logger.error("更新标签成员关联出错：orgId=" + orgId + e.getMessage(),
									e);
						} catch (Exception e) {
							logger.error("更新标签成员关联出错：orgId=" + orgId + e.getMessage(),
									e);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"TagThread synTagInfos orgId=" + orgId + ",tags="
							+ tags.toString(), e);
		}*/
	}

	/**
	 * 同步标签成员
	 * @param orgId
	 * @param tagId
	 * @param tag
	 * @param isAdd  是否是新增标签
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	private void synTagMenbers(String orgId, String tagId, JSONObject tag, boolean isAdd) {
		/*try {
			Map<String, String> tagRefMap = new HashMap<String, String>();
			if(!isAdd){//表示编辑标签，获取历史标签数据
				List<TbQyTagRefPO> tagRefList = tagService.getTagRefList(tagId);
				if(tagRefList != null && tagRefList.size()>0){
					for (TbQyTagRefPO tbQyTagRefPO : tagRefList) {
						tagRefMap.put(tbQyTagRefPO.getMenberType()+"_"+tbQyTagRefPO.getWxId(), tbQyTagRefPO.getId());
					}
				}
			}
			
			// 遍历接口数据，保存新的标签数据(用户)
			JSONArray jsonArray = tag.getJSONArray("userlist");
			int length = jsonArray.size();
			JSONObject user;
			List<TbQyTagRefPO> tagList = new ArrayList<TbQyTagRefPO>(length+10);
			if(isAdd){
				for (int i = 0; i < length; i++) {
					user = jsonArray.getJSONObject(i);
					tagList.add(getUserTagRefPO(orgId, tagId, user.getString("userid"), user.getString("name")));
				}
				// 遍历接口数据，保存新的标签数据(部门)
				jsonArray = tag.getJSONArray("partylist");
				length = jsonArray.size();
				for (int i = 0; i < length; i++) {
					tagList.add(getDeptTagRefPO(orgId, tagId, String.valueOf(jsonArray.getInt(i))));
				}
			}
			else{
				String userId;
				for (int i = 0; i < length; i++) {
					user = jsonArray.getJSONObject(i);
					userId = user.getString("userid");
					//如果系统中已经存在这个标签的用户信息
					if(tagRefMap.containsKey("1_"+userId)){
						tagRefMap.remove("1_"+userId);//删除已存在的数据，tagRefMap中剩下的数据即为需要删除的数据
						continue;
					}
					tagList.add(getUserTagRefPO(orgId, tagId, userId, user.getString("name")));
				}
				// 遍历接口数据，保存新的标签数据(部门)
				jsonArray = tag.getJSONArray("partylist");
				length = jsonArray.size();
				String deptId;
				for (int i = 0; i < length; i++) {
					deptId = String.valueOf(jsonArray.getInt(i));
					//如果系统中已经存在这个标签的用户信息
					if(tagRefMap.containsKey("1_"+deptId)){
						tagRefMap.remove("1_"+deptId);//删除已存在的数据，tagRefMap中剩下的数据即为需要删除的数据
						continue;
					}
					tagList.add(getDeptTagRefPO(orgId, tagId, deptId));
				}
			}

			QwtoolUtil.addBatchList(tagList, false);
			if(tagRefMap.size()>0){
				String[] ids = new String[tagRefMap.size()];
				ids = tagRefMap.values().toArray(ids);
				tagService.batchDel(TbQyTagRefPO.class, ids);
			}
		} catch (BaseException e) {
			logger.error(
					"TagThread synTagMenbers保存标签成员出错（用户）：orgId=" + orgId + ",tagId="
							+ tagId, e);
		} catch (Exception e) {
			logger.error(
					"TagThread synTagMenbers保存标签成员出错（用户）：orgId=" + orgId + ",tagId="
							+ tagId, e);
		}*/
	}
	
	/**
	 * 获取标签关联的用户信息
	 * @param orgId
	 * @param tagId
	 * @param userId
	 * @param userName
	 * @return
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	/*private TbQyTagRefPO getUserTagRefPO(String orgId, String tagId, String userId, String userName){
		TbQyTagRefPO tagRref = new TbQyTagRefPO();
		tagRref.setId(UUID.randomUUID().toString());
		tagRref.setOrgId(orgId);
		tagRref.setTagId(tagId);
		tagRref.setMenberType("1");
		tagRref.setWxId(userId);
		tagRref.setMenberName(userName);
		tagRref.setCreateTime(new Date());
		return tagRref;
	}*/
	
	/**
	 * 获得用户管理的部门信息
	 * @param orgId
	 * @param tagId
	 * @param deptId
	 * @return
	 * @author Sun Qinghai
	 * @2016-3-14
	 * @version 1.0
	 */
	/*private TbQyTagRefPO getDeptTagRefPO(String orgId, String tagId, String deptId){
		TbQyTagRefPO tagRref = new TbQyTagRefPO();
		tagRref.setId(UUID.randomUUID().toString());
		tagRref.setOrgId(orgId);
		tagRref.setTagId(tagId);
		tagRref.setMenberType("2");
		tagRref.setWxId(deptId);
		tagRref.setCreateTime(new Date());
		return tagRref;
	}*/

	/**
	 * 同步标签成员表
	 * 
	 * @param corpId
	 * @param orgId
	 * @param tagId
	 * @author Luo Rilang
	 * @2015-11-5
	 * @version 1.0
	 */
	private void synTagMenbers(String corpId, String orgId, String wxTagId, String tagId, boolean isAdd) {
		/*JSONObject jsonObject = WxTagUtil.getTagMembers(corpId, wxTagId, WxAgentUtil.getAddressBookCode());
		logger.debug("userlist:" + jsonObject.getString("userlist"));
		logger.debug("partylist:" + jsonObject.getString("partylist"));
		synTagMenbers(orgId, tagId, jsonObject, isAdd);*/
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
