package cn.com.do1.component.contact.contact.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.*;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.ExperienceAgentStatusUtil;
import cn.com.do1.component.util.memcached.CacheAccessTokenManager;
import cn.com.do1.component.util.memcached.CacheSessionManager;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.annotation.struts.ActionRoles;
import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.annotation.struts.SearchValueType;
import cn.com.do1.common.annotation.struts.SearchValueTypes;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbDqdpOrganizationDelLog;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.DepCompress;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.service.IWxLoginService;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.managesetting.managesetting.service.IManagesettingService;
import cn.com.do1.component.managesetting.managesetting.vo.TbDqdpRoleVO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.sms.sendsms.model.TbQySmsVerCodePO;
import cn.com.do1.component.sms.sendsms.service.ISendsmsService;
import cn.com.do1.component.sms.sendsms.util.SMSType;
import cn.com.do1.component.systemmgr.org.service.IOrgService;
import cn.com.do1.component.systemmgr.person.service.IPersonService;
import cn.com.do1.component.systemmgr.role.service.IRoleService;
import cn.com.do1.component.systemmgr.user.model.BaseUserVO;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.memcached.CacheDqdpOrgManager;
import cn.com.do1.component.util.memcached.CacheDqdpUserManager;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.component.wxcgiutil.message.TextMessage;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class OrgUserAction extends BaseAction {
	private final static transient Logger logger = LoggerFactory
			.getLogger(OrgUserAction.class);
	private IContactService contactService;
	private IContactMgrService contactMgrService;
	private IDepartmentService departmentService;
	private IDepartmentMgrService departmentMgrService;
	private TbQyUserInfoPO tbQyUserInfoPO;
	private IPersonService myPersonService;
	private IOrgService orgService;
	private IExperienceapplicationService experienceapplicationService;
	private IWxLoginService wxLoginService;
	private ISendsmsService sendsmsService;
	private String id;
	private IRoleService roleService;
	private String orgId;

	public IContactService getContactService() {
		return contactService;
	}
	private IManagesettingService managesettingService;
	@Resource(name = "managesettingService")
	public void setManagesettingService(IManagesettingService managesettingService) {
		this.managesettingService = managesettingService;
	}

	@Resource(name = "contactService")
	public void setContactService(IContactService contactService) {
		this.contactService = contactService;
	}
	/**
	 * @param contactMgrService 要设置的 contactMgrService
	 */
	@Resource(name = "contactService")
	public void setContactMgrService(IContactMgrService contactMgrService) {
		this.contactMgrService = contactMgrService;
	}

	@Resource(name="sendsmsService")
	public void setSendsmsService(ISendsmsService sendsmsService) {
		this.sendsmsService = sendsmsService;
	}

	@Resource
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 根据corpId或公司名称查询公司信息
	 * @throws Exception
	 * @throws BaseException
	 * @author Chen Feixiong
	 * 2015-3-12
	 */
	@SearchValueTypes(
			nameFormat="false",value={
					@SearchValueType(name = "orgName", type="string", format = "%%%s%%")
			})  
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	@ActionRoles("orgList")
	public void listOrg() throws Exception, BaseException {
		addJsonArray("orgList", this.wxLoginService.getListOrg(getSearchValue()));
	}

	/**
	 * 根据机构id查询企业管理员数据
	 * @throws Exception
	 * @throws BaseException
	 * @author Sun Qinghai
	 * @2016-3-11
	 * @version 1.0
	 */
	@ActionRoles({"userList"})
	@SearchValueTypes({
		@SearchValueType(name="userName", type="String", format="%%%s%%"),
		@SearchValueType(name="personName", type="String", format="%%%s%%")
	})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	public void listPersonByOrgId() throws Exception, BaseException {
		Pager pager = new Pager(ServletActionContext.getRequest(), super.getPageSize());
		pager = this.wxLoginService.listMyPersonByOrg(orgId, pager, getSearchValue());
		addJsonPager("userPager", pager);
	}

	/**
	 * 删除机构，同时删除机构下通讯录、部门、接入信息、绑定信息、管理员、机构信息
	 * @author Sun Qinghai
	 * @2014-11-6
	 * @version 1.0
	 */
	@ActionRoles({"orgDel"})
	public void delOrganization() {
		StringBuffer msg = new StringBuffer("");
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			String verCode = request.getParameter("code");
			if(Configuration.IS_QIWEIYUN){
				if(AssertUtil.isEmpty(verCode)){
					setActionResult("1001", "请输入验证码");
					doJsonOut();
					return;
				}
				TbQySmsVerCodePO smsVerCode = new TbQySmsVerCodePO();
				smsVerCode.setCodeType(SMSType.DEL_COMPANY);
				smsVerCode.setVerCode(verCode);
				boolean flag = false;	//验证码是否有效
				String [] adminMobiles = Configuration.ADMIN_MOBILE.split(",");
				for(String adminMobile : adminMobiles){
					smsVerCode.setMobile(adminMobile);
					flag = sendsmsService.validationCode(smsVerCode);
					if(flag){
						break;
					}
				}
				
				if(!flag){
					setActionResult("1001", "请输入有效验证码");
					doJsonOut();
					return;
				}
			}
			
			
			String remark=request.getParameter("remark");
			String ip=WxqyhAppContext.getFistSourceIP(request);
			
			String user = DqdpAppContext.getCurrentUser().getUsername();
			UserOrgVO userInfoVO = contactService.getOrgByUserId(user);
			String creatorName="admin";
	        if (userInfoVO != null) {//因为getOrgByUserId查询admin会为空的情况
	        	creatorName=userInfoVO.getPersonName();
	        }
			logger.error("删除机构信息开始,orgId:"+id+",loginUser:"+user);
			if(id==null || id.isEmpty()){
				setActionResult("1001", "机构id为空");
				doJsonOut();
				return;
			}
			if("7bed74d8-1413-4f12-9b38-e491e55c30da".equals(id)|| Configuration.COMPANY_ORG_ID.equals(id)){
				setActionResult("1001", "不能删除此机构接入信息");
				doJsonOut();
				return;
			}
			//数据检测，包括（一天只能删除规定数量的机构）
			if(Configuration.IS_QIWEIYUN){
				int count=contactService.getDelOrgCount(user);
				if(count>=Configuration.DEL_ORG_SUM){
					setActionResult("10012", "你当天删除的机构数已超过"+count+"家");
					doJsonOut();
					return;
				}
			}
			ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, id);
			if(orgPO==null){
				setActionResult("1001", "机构为空");
				doJsonOut();
				return;
			}
			String corpId = orgPO.getCorpId();
			//如果corpId不为空
			if(corpId!=null && !corpId.isEmpty()){
				/**
				 * 1.删除通讯录用户
				 */
				List<TbQyUserInfoPO> userList = contactService.findUsersByOrgId(id);
				if(userList!=null && userList.size()>0){
					String error = null;
					for (TbQyUserInfoPO tbQyUserInfoPO : userList) {
						contactService.delPO(tbQyUserInfoPO);
						if(Configuration.IS_USE_MEMCACHED){
							CacheSessionManager.remove(tbQyUserInfoPO.getUserId());
						}
						//如果是体验号微信通讯录，或者企微体验企业微信
						if(corpId.equals(Configuration.AUTO_CORPID) || ExperienceAgentStatusUtil.REGISTER_TYPE_APPLY==orgPO.getType()){
							try {
								WxUserService.delUser(tbQyUserInfoPO.getUserId(), corpId,orgPO.getOrganizationId());
							} catch (BaseException e) {
								logger.error("删除微信上用户失败,corpId:"+corpId+",orgId:"+id+",userId:"+tbQyUserInfoPO.getUserId(),e);
								error = "部分微信上用户删除失败";
							} catch (Exception e) {
								logger.error("删除微信上用户失败,corpId:"+corpId+",orgId:"+id+",userId:"+tbQyUserInfoPO.getUserId(),e);
								error = "部分微信上用户删除失败";
							}
							if(error != null){
								msg.append("；"+error);
							}
						}
					}
					userList = null;
				}

				/**
				 * 2.删除通讯录部门
				 */
				List<DepCompress> depList = departmentMgrService.getAllDepartCompress(id);
				if(depList!=null && depList.size()>0){
					String error = null;
					Map<String, DepCompress> map = new HashMap<String, DepCompress>(depList.size());
					for (DepCompress depCompress : depList) {
						map.put(depCompress.getId(), depCompress);
					}
					TbDepartmentInfoPO depPO = new TbDepartmentInfoPO();
					WxDept dept = new WxDept();
					while(true){
						Iterator<Entry<String, DepCompress>> itItem = map.entrySet().iterator();
						Map.Entry<String, DepCompress> eItem;
						DepCompress dep;
						DepCompress pardep;
						List<String> removeList = new ArrayList<String>();
						while (itItem.hasNext()) {
							eItem = itItem.next();
							dep = eItem.getValue();
							if(dep.getChildTime()==0){
								depPO.setId(dep.getId());
								contactService.delPO(depPO);
								//如果是体验号微信通讯录
								if(corpId.equals(Configuration.AUTO_CORPID) || ExperienceAgentStatusUtil.REGISTER_TYPE_APPLY==orgPO.getType()){
									if(dep.getWxId()!=null && !dep.getWxId().isEmpty()){
										try {
											WxDeptService.delDept(dep.getWxId(), corpId,orgPO.getOrganizationId());
										} catch (BaseException e) {
											logger.error("删除微信上部门失败,corpId:"+corpId+",orgId:"+id+",depId:"+dep.getWxId(),e);
											error = "部分微信上部门删除失败";
										} catch (Exception e) {
											logger.error("删除微信上部门失败,corpId:"+corpId+",orgId:"+id+",depId:"+dep.getWxId(),e);
											error = "部分微信上部门删除失败";
										}
									}
									//如果是根部门，要将其移到微信根路径下，以方便下次同步
									/*if(dep.getWxId()!=null && !dep.getWxId().isEmpty() && dep.getParentDepart()==null && orgPO.getWxParentid()!=null){
										try {
											dept.setId(dep.getWxId());
											dept.setParentid(orgPO.getWxParentid());
											dept.setName(dep.getDepartmentName());
											WxDeptService.updateDept(dept, orgPO.getCorpId());
										} catch (BaseException e) {
											logger.error("将部门移动到微信根目录失败,corpId:"+corpId+",orgId:"+id+",depId:"+dep.getWxId(),e);
											error = "将部门移动到微信根目录部分失败";
										} catch (Exception e) {
											logger.error("将部门移动到微信根目录失败,corpId:"+corpId+",orgId:"+id+",depId:"+dep.getWxId(),e);
											error = "将部门移动到微信根目录部分失败";
										}
									}*/
								}
								removeList.add(dep.getId());
								if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
									pardep = map.get(dep.getParentDepart());
									if(pardep!=null){
										pardep.setChildTime(pardep.getChildTime()-1);
									}
								}
							}
						}
						//更新个数，防止死循环
						if(removeList.size()==0){
							//如果一个都没更新，说明存在问题，重新初始化一次
							while (itItem.hasNext()) {
								eItem = (Map.Entry<String, DepCompress>) itItem.next();
								dep = eItem.getValue();
								dep.setChildTime(0);
							}
							itItem = map.entrySet().iterator();
							while (itItem.hasNext()) {
								eItem = (Map.Entry<String, DepCompress>) itItem.next();
								dep = eItem.getValue();
								if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
									//获取父部门
									if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
										pardep = map.get(dep.getParentDepart());
										if(pardep!=null){
											pardep.setChildTime(pardep.getChildTime()+1);
										}
									}
								}
							}
						}
						else{
							//清空已删除的部门
							for (String depId : removeList) {
								map.remove(depId);
							}
						}
						if(map.size()==0){
							break;
						}
					}
					if(error != null){
						msg.append("；"+error);
					}
				}

				/**
				 * 3.删除密钥
				 */
				List<ExtOrgPO> orgList = contactService.getOrgPOByCorpId(corpId);
				List<TbDqdpOrganizationInsertPO> orgInsertList = experienceapplicationService.getInsertPOByCorpID(corpId);
				//如果没有corpId为此的机构或者个数小于而且这个机构就是要删除的机构
				if(orgList==null || (orgList.size()<2 && orgList.get(0).getOrganizationId().equals(id))){
					List<TbConfigOrgAesPO> configOrgPoList = contactMgrService.getConfigOrgAesPOByCorpId(corpId);
					if(configOrgPoList!=null && configOrgPoList.size()>0){
						for (TbConfigOrgAesPO tbConfigOrgAesPO : configOrgPoList) {
							departmentService.delPO(tbConfigOrgAesPO);
						}
					}

					/**
					 * 4.删除接入信息
					 */
					if(orgInsertList!=null && orgInsertList.size()>0){
						//如果只剩下最后一个机构需要删除，将这个corpId所有的接入信息都删掉，否则只删除本机构对应的接入信息
						for (TbDqdpOrganizationInsertPO tbDqdpOrganizationInsertPO : orgInsertList) {
							experienceapplicationService.delPO(tbDqdpOrganizationInsertPO);
						}
					}

					/**
					 * 5.删除绑定信息（只有删除corpId下最后一个机构时才会将绑定信息删除）
					 */
                    if(ExperienceAgentStatusUtil.REGISTER_TYPE_EXP !=orgPO.getType()){

                        List<TbQyExperienceAgentPO> agentList = experienceapplicationService.getExperienceAgentList(corpId);
                        if(agentList!=null && agentList.size()>0){
                            for (TbQyExperienceAgentPO po : agentList) {
                                experienceapplicationService.delPO(po);
								if(Configuration.IS_USE_MEMCACHED){
									CacheAccessTokenManager.remove(corpId + "_" + po.getSuiteId());
									WxAgentUtil.delAgentCache(po.getCorpId(),po.getAgentCode(),po.getAgentId());//清掉原有的缓存信息
								}
                            }
                        }
                        TbQyExperienceApplicationPO po = experienceapplicationService.findAccountInfoByOrgId(id);
                        if(po!=null){
                            experienceapplicationService.delPO(po);
                        }
                    }
				}
				else{
					msg.append("；该机构对应的corpId还接入了其它机构，如果希望删除接入信息，请删除corpId为"+corpId+"的所有机构");
					/**
					 * 4.删除接入信息
					 */
					if(orgInsertList!=null && orgInsertList.size()>0){
						for (TbDqdpOrganizationInsertPO tbDqdpOrganizationInsertPO : orgInsertList) {
							if(tbDqdpOrganizationInsertPO.getOrgId().equals(id)){
								experienceapplicationService.delPO(tbDqdpOrganizationInsertPO);
							}
						}
					}
				}
			}

			/**
			 * 4.删除机构管理员
			 */
			//不相信一个机构下有10000个以上的管理员
			Pager pager = new Pager(ServletActionContext.getRequest(), 10000);
			pager = this.myPersonService.listPersonByOrg(this.id, pager, new HashMap<String, Object>());
			Collection<BaseUserVO> comments = pager.getPageData();
			List<BaseUserVO> list;
			//设置明细数据个数
			if(!AssertUtil.isEmpty(comments)){
				list = new ArrayList<BaseUserVO>(comments);
				if(list.size()>0){
					int size = list.size();
					String ids[] = new String[1];
					BaseUserVO baseUserVO;
					List<String> userNames = new ArrayList<String>(list.size());
					for (int i = 0; i < size; i++) {
						baseUserVO = list.get(i);
						ids[0] = baseUserVO.getPersonId();
						userNames.add(baseUserVO.getUserName());
						try {
							roleService.delAllUserRoleRefByUserId(baseUserVO.getUserId());
							this.myPersonService.delBaseUserByPersonId(ids);

							if(Configuration.IS_USE_MEMCACHED){
								CacheDqdpUserManager.remove(baseUserVO.getUserName());
							}
							logger.error("机构管理员删除成功 UserName="+baseUserVO.getUserName());
						} catch (BaseException e) {
							logger.error("机构管理员删除失败 UserName="+baseUserVO.getUserName(),e);
							msg.append("；机构管理员["+baseUserVO.getUserName()+"]删除失败("+e.getMessage()+")");
						} catch (Exception e) {
							logger.error("机构管理员删除失败 UserName="+baseUserVO.getUserName(),e);
							msg.append("；机构管理员["+baseUserVO.getUserName()+"]删除失败("+e.getMessage()+")");
						}
					}
					try {
						experienceapplicationService.delWxLoginInfo(corpId, userNames);
						logger.error("机构管理员绑定的微信账号删除成功 UserName="+userNames.size());
					} catch (Exception e) {
						logger.error("机构管理员删除失败 UserName="+userNames.size(),e);
						msg.append("；机构管理员绑定的微信账号删除失败("+e.getMessage()+")");
					}
				}
			}
			/**
			 * 4.删除角色信息
			 */
			Map<String, Object> searchMap = new HashMap<String, Object>(1);
			searchMap.put("orgId", id);
			String ids[] = new String[1];
			do {//分页获取用户设置的角色信息
				pager = new Pager(ServletActionContext.getRequest(),Configuration.MAX_PAGESIZE);
				pager=managesettingService.getRoleList(pager, searchMap);
				Collection<TbDqdpRoleVO> coll  = pager.getPageData();
				if(pager.getTotalRows()>0){
					for (TbDqdpRoleVO roleVO : coll) {
						ids[0] = roleVO.getRoleId();
						try {
							roleService.deleteRoleByIds(ids);
							logger.error("机构角色删除成功 roleName="+roleVO.getRoleName());
						} catch (BaseException e) {
							logger.error("机构角色删除失败 roleName="+roleVO.getRoleName(),e);
							msg.append("；机构角色["+roleVO.getRoleName()+"]删除失败("+e.getMessage()+")");
						} catch (Exception e) {
							logger.error("机构角色删除失败 UserName="+roleVO.getRoleName(),e);
							msg.append("；机构角色["+roleVO.getRoleName()+"]删除失败("+e.getMessage()+")");
						}
					}
				}
			} while (pager.getCurrentPage()<pager.getTotalPages());
			managesettingService.delRoleByOrgId(id);

			/**
			 * 5.删除机构
			 */
			if(orgPO.getWxId()!=null && !orgPO.getWxId().isEmpty() && !orgPO.getWxId().equals("1")){
				try {
					WxDeptService.delDept(orgPO.getWxId(), corpId,orgPO.getOrganizationId());
				} catch (BaseException e) {
					logger.error("删除微信上机构失败,depId:"+orgPO.getWxId(),e);
					msg.append("；微信上机构【"+orgPO.getOrganizationDescription()+"】删除失败");
				} catch (Exception e) {
					logger.error("删除微信上机构失败,depId:"+orgPO.getWxId(),e);
					msg.append("；微信上机构【"+orgPO.getOrganizationDescription()+"】删除失败");
				}
			}
			orgService.delOrganization(id);
			if(Configuration.IS_USE_MEMCACHED){
				CacheDqdpOrgManager.remove(id);
			}
			logger.error("删除机构信息结束,orgId:"+id);

			if(Configuration.IS_QIWEIYUN){
				//操作完成，作废旧的验证码
				TbQySmsVerCodePO smsVerCode = new TbQySmsVerCodePO();
				smsVerCode.setCodeType(SMSType.DEL_COMPANY);
				smsVerCode.setVerCode(verCode);
				String [] adminMobiles = Configuration.ADMIN_MOBILE.split(",");
				for(String adminMobile : adminMobiles){
					smsVerCode.setMobile(adminMobile);
					sendsmsService.resetSmsCode(smsVerCode);
				}
			}
			//回收企业微信
			if(orgPO.getType() == ExperienceAgentStatusUtil.REGISTER_TYPE_APPLY || orgPO.getType() == ExperienceAgentStatusUtil.REGISTER_TYPE_APPLY_OWN){
				recycleCorpId(orgPO.getCorpId());
			}
			
			//记录删除日志
			TbDqdpOrganizationDelLog delpo=new TbDqdpOrganizationDelLog();
			delpo.setId(UUID32.getID());
			delpo.setCreator(user);
			delpo.setCreateTime(new Date());
			delpo.setOrgId(orgPO.getOrganizationId());
			delpo.setCorpId(orgPO.getCorpId());
			delpo.setOrgName(orgPO.getOrganizationName());
			delpo.setRemark(remark);
			delpo.setCreatorName(creatorName);
			delpo.setResult("删除成功"+msg.toString());
			delpo.setIp(ip);
			orgService.insertPO(delpo, false);
			if(Configuration.IS_QIWEIYUN){
				sendMsgToUser(creatorName+"删除了企业账号"+orgPO.getOrganizationName()+"，删除原因："+remark);
			}
			setActionResult("0", "删除成功"+msg.toString());
		} catch (BaseException e) {
			logger.error("根据机构id删除其管理用户，通讯录等信息失败orgId:"+id, e);
			setActionResult("1001", "根据机构id删除其管理用户，通讯录等信息失败");
			logger.error("删除机构信息结束,orgId:"+id);
		} catch (Exception e) {
			logger.error("根据机构id删除其管理用户，通讯录等信息失败orgId:"+id, e);
			setActionResult("1001", "根据机构id删除其管理用户，通讯录等信息失败");
			logger.error("删除机构信息结束,orgId:"+id);
		}
		doJsonOut();
	}

	/**
	 * 回收申请的企业微信
	 * @param corpId
	 * @throws Exception
     */
	private void recycleCorpId(String corpId) throws Exception {
		TbQyCorpPoolPO po = experienceapplicationService.getCorpPoolPOByCorpId(corpId);
		if(po != null){
			po.setStatus(ExperienceAgentStatusUtil.CORP_POOL_STAtUS_NO);
			po.setRecycleTime(new Date());
			experienceapplicationService.updatePO(po,false);
		}
	}

	/**
	 * 申领企业微信
	 * @throws Exception
	 * @throws BaseException
	 * @author sunqinghai
	 * @date 2016 -7-12
	 */
	@ActionRoles({"orgApply"})
	@JSONOut(catchException = @CatchException(errCode = "-1", successMsg = "申领成功", faileMsg = "申领失败"))
	public void applyOrganization() throws Exception, BaseException {
		String user = DqdpAppContext.getCurrentUser().getUsername();
		logger.error("applyOrganization,orgId:"+id+",loginUser:"+user);

		ExtOrgPO orgPO = contactService.searchByPk(ExtOrgPO.class, id);
		if(orgPO==null){
			throw new NonePrintException("1001", "机构为空");
		}
		//申领企业微信
		experienceapplicationService.applyCorp(user,orgPO);
	}

	/**
	 * 删除机构发送消息给相关人
	 * @author ChenFeixiong
	 * 2016-4-14
	 */
	private void sendMsgToUser(String content) {
        try {
        	TextMessage tm = new TextMessage();
            tm.setTouser(Configuration.DEL_ORG_TOUSER);
            if (AssertUtil.isEmpty(tm.getTouser()))
                return;
			WxMessageUtil.sendTextMessage(tm, content, WxAgentUtil.getTaskCode(), Configuration.AUTO_CORPID, Configuration.COMPANY_ORG_ID);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

	/**
	 * 删除民间体验号的接入信息
	 * 
	 * @author Sun Qinghai
	 * @2015-11-17
	 * @version 1.0
	 */
	@ActionRoles({"orgDel"})
	public void delExperienceOrg() {
		try {
			String user = DqdpAppContext.getCurrentUser().getUsername();
			logger.error("删除民间体验号的体验信息开始,orgId:"+id+",loginUser:"+user);
			if(id==null || id.isEmpty()){
				setActionResult("1001", "机构id为空");
				doJsonOut();
				return;
			}

			/**
			 * 删除绑定信息（只有删除corpId下最后一个机构时才会将绑定信息删除）
			 */
			TbQyExperienceApplicationPO po = experienceapplicationService.searchByPk(TbQyExperienceApplicationPO.class, id);
			if(po!=null){
				if(ExperienceAgentStatusUtil.REGISTER_TYPE_EXP != po.getIsautoregis()){
					setActionResult("1001", "非民间体验号注册用户，不能删除");
					doJsonOut();
					return;
				}
				experienceapplicationService.delPO(po);
			}

			setActionResult("0", "删除成功");
		} catch (BaseException e) {
			logger.error("删除民间体验号的体验信息失败id:"+id, e);
			setActionResult("1001", "删除民间体验号的体验信息失败"+e.getMessage());
			ExceptionCenter.addException(e, this);
		} catch (Exception e) {
			logger.error("删除民间体验号的体验信息失败id:"+id, e);
			setActionResult("1001", "删除民间体验号的体验信息失败"+e.getMessage());
			ExceptionCenter.addException(e, this);
		}
		logger.error("删除民间体验号的体验信息结束,id:"+id);
		doJsonOut();
	}

	/**
	 * 删除机构，同时删除机构下通讯录、部门、接入信息、绑定信息、管理员、机构信息
	 * @author Sun Qinghai
	 * @throws BaseException 
	 * @throws Exception 
	 * @2014-11-6
	 * @version 1.0
	 */
	@ActionRoles({"orgDel"})
	@JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
	private void delRubbishOrg() throws Exception, BaseException {
		StringBuffer msg = new StringBuffer("");
		List<ExtOrgPO> orgs = wxLoginService.getRubbishOrg(Configuration.AUTO_CORPID);
		if(orgs!=null && orgs.size()>0){
			msg.append("删除机构"+orgs.size()+"个");
			int num=0;
			for (ExtOrgPO orgPO : orgs) {
				num++;
				logger.info("删除垃圾体验号，第"+num+"个,机构名称为："+orgPO.getOrganizationName()+"，机构id："+orgPO.getOrganizationId());
				try {
					String id = orgPO.getOrganizationId();
					String corpId = orgPO.getCorpId();
					/**
					 * 1.删除通讯录用户
					 */
					List<TbQyUserInfoPO> userList = contactService.findUsersByOrgId(id);
					if(userList!=null && userList.size()>0){
						msg.append(";机构"+orgPO.getOrganizationId()+"还有用户");
						continue;
					}

					//如果corpId不为空
					if(corpId!=null && !corpId.isEmpty()){
						/**
						 * 2.删除通讯录部门
						 */
						List<DepCompress> depList = departmentMgrService.getAllDepartCompress(id);
						if(depList!=null && depList.size()>0){
							String error = null;
							Map<String, DepCompress> map = new HashMap<String, DepCompress>(depList.size());
							for (DepCompress depCompress : depList) {
								map.put(depCompress.getId(), depCompress);
							}
							TbDepartmentInfoPO depPO = new TbDepartmentInfoPO();
							WxDept dept = new WxDept();
							while(true){
								Iterator<Entry<String, DepCompress>> itItem = map.entrySet().iterator();
								Map.Entry<String, DepCompress> eItem;
								DepCompress dep;
								DepCompress pardep;
								List<String> removeList = new ArrayList<String>();
								while (itItem.hasNext()) {
									eItem = (Map.Entry<String, DepCompress>) itItem.next();
									dep = eItem.getValue();
									if(dep.getChildTime()==0){
										depPO.setId(dep.getId());
										contactService.delPO(depPO);
										//如果是体验号微信通讯录
										if(corpId.equals(Configuration.AUTO_CORPID)){
											if(dep.getWxId()!=null && !dep.getWxId().isEmpty()){
												try {
													WxDeptService.delDept(dep.getWxId(), corpId,orgPO.getOrganizationId());
												} catch (BaseException e) {
													logger.error("删除微信上部门失败,orgId:"+id+",depId:"+dep.getWxId());
													error = "部分微信上部门删除失败";
												} catch (Exception e) {
													logger.error("删除微信上部门失败,orgId:"+id+",depId:"+dep.getWxId());
													error = "部分微信上部门删除失败";
												}
											}
										}
										removeList.add(dep.getId());
										if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
											pardep = map.get(dep.getParentDepart());
											if(pardep!=null){
												pardep.setChildTime(pardep.getChildTime()-1);
											}
										}
									}
								}
								//更新个数，防止死循环
								if(removeList.size()==0){
									//如果一个都没更新，说明存在问题，重新初始化一次
									while (itItem.hasNext()) {
										eItem = (Map.Entry<String, DepCompress>) itItem.next();
										dep = eItem.getValue();
										dep.setChildTime(0);
									}
									itItem = map.entrySet().iterator();
									while (itItem.hasNext()) {
										eItem = (Map.Entry<String, DepCompress>) itItem.next();
										dep = eItem.getValue();
										if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
											//获取父部门
											if(dep.getParentDepart()!=null && !dep.getParentDepart().isEmpty()){
												pardep = map.get(dep.getParentDepart());
												if(pardep!=null){
													pardep.setChildTime(pardep.getChildTime()+1);
												}
											}
										}
									}
								}
								else{
									//清空已删除的部门
									for (String depId : removeList) {
										map.remove(depId);
									}
								}
								if(map.size()==0){
									break;
								}
							}
							if(error != null){
								msg.append("；"+error);
							}
						}
					}

					/**
					 * 4.删除机构管理员
					 */
					//不相信一个机构下有10000个以上的管理员
					Pager pager = new Pager(ServletActionContext.getRequest(), 10000);
					pager = this.myPersonService.listPersonByOrg(id, pager, new HashMap<String, Object>());
					Collection<BaseUserVO> comments = pager.getPageData();
					List<BaseUserVO> list;
					//设置明细数据个数
					if(!AssertUtil.isEmpty(comments)){
						String error = null;
						list = new ArrayList<BaseUserVO>(comments);
						if(list.size()>0){
							int size = list.size();
							String ids[] = new String[1];
							BaseUserVO baseUserVO;
							for (int i = 0; i < size; i++) {
								baseUserVO = list.get(i);
								ids[0] = baseUserVO.getPersonId();
								try {
									this.myPersonService.delBaseUserByPersonId(ids);
								} catch (BaseException e) {
									logger.error("机构,orgId:"+id+"管理员删除失败",e);
									error = "机构,orgId:"+id+"管理员删除失败("+e.getMessage()+")";
								} catch (Exception e) {
									logger.error("机构,orgId:"+id+"管理员删除失败",e);
									error = "机构,orgId:"+id+"管理员删除失败("+e.getMessage()+")";
								}
							}
						}
						if(error != null){
							msg.append("；"+error);
						}
					}

					/**
					 * 4.删除机构
					 */
					if(orgPO.getWxId()!=null && !orgPO.getWxId().isEmpty()){
						try {
							WxDeptService.delDept(orgPO.getWxId(), corpId,orgPO.getOrganizationId());
						} catch (BaseException e) {
							logger.error("删除微信上机构,orgId:"+id+"失败,depId:"+orgPO.getWxId(),e);
							msg.append("；微信上机构【"+orgPO.getOrganizationName()+"】删除失败");
						} catch (Exception e) {
							logger.error("删除微信上机构,orgId:"+id+"失败,depId:"+orgPO.getWxId(),e);
							msg.append("；微信上机构【"+orgPO.getOrganizationName()+"】删除失败");
						}
					}
					orgService.delOrganization(id);
				} catch (BaseException e) {
					logger.error("根据机构id,orgId:"+orgPO.getOrganizationId()+"删除其管理用户，通讯录等信息失败", e);
					msg.append(";根据机构id,orgId:"+orgPO.getOrganizationId()+"删除其管理用户，通讯录等信息失败");
					ExceptionCenter.addException(e, this);
				} catch (Exception e) {
					logger.error("根据机构id,orgId:"+orgPO.getOrganizationId()+"删除其管理用户，通讯录等信息失败", e);
					msg.append(";根据机构id,orgId:"+orgPO.getOrganizationId()+"删除其管理用户，通讯录等信息失败");
					ExceptionCenter.addException(e, this);
				}
			}
			logger.error("删除机构信息结束,orgId:"+id);
			setActionResult("0", "删除成功"+msg.toString());
		}
	}


	public TbQyUserInfoPO getTbQyUserInfoPO() {
		return this.tbQyUserInfoPO;
	}

	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource
	public void setMyPersonService(IPersonService myPersonService) {
		this.myPersonService = myPersonService;
	}
	@Resource
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}
	@Resource(name = "experienceapplicationService")
	public void setExperienceapplicationService(IExperienceapplicationService experienceapplicationService) {
		this.experienceapplicationService = experienceapplicationService;
	}
	/**
	 * @param departmentMgrService 要设置的 departmentMgrService
	 */
	@Resource(name = "departmentService")
	public void setDepartmentMgrService(IDepartmentMgrService departmentMgrService) {
		this.departmentMgrService = departmentMgrService;
	}

	@Resource(name = "wxLoginService")
	public void setWxLoginService(IWxLoginService wxLoginService) {
		this.wxLoginService = wxLoginService;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
