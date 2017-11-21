package cn.com.do1.component.contact.contact.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.dao.IWxLoginDAO;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;
import cn.com.do1.component.contact.contact.service.IWxLoginService;
import cn.com.do1.component.systemmgr.org.model.OrgVO;
import cn.com.do1.component.systemmgr.org.service.IOrgService;

/**
 * <p>Title: 通讯录管理</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-1-20
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
@Service("wxLoginService")
public class WxLoginServiceImpl extends BaseService implements IWxLoginService {
    private final static transient Logger logger = LoggerFactory.getLogger(WxLoginServiceImpl.class);

    private IWxLoginDAO wxLoginDAO;
    @Resource
	public void setWxLoginDAO(IWxLoginDAO wxLoginDAO) {
		this.wxLoginDAO = wxLoginDAO;
        setDAO(wxLoginDAO);
	}
    private IOrgService orgService;

    @Resource
    public void setOrgService(IOrgService orgService) {
      this.orgService = orgService;
    }
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IWxLoginService#findWxAccountByEmail(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserWxloginInfoPO findWxAccountByEmail(String email,
			String corpId) throws Exception, BaseException {
		return wxLoginDAO.findWxAccountByEmail(email,corpId);
	}
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IWxLoginService#getRubbishOrg(java.lang.String)
	 */
	@Override
	public List<ExtOrgPO> getRubbishOrg(String corpId) throws Exception,
			BaseException {
		return wxLoginDAO.getRubbishOrg(corpId);
	}
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IWxLoginService#findWxAccountByUserIdAndEmail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserWxloginInfoPO findWxAccountByUserIdAndEmail(String wxUserId,
			String email, String corpId) throws Exception, BaseException {
		return wxLoginDAO.findWxAccountByUserIdAndEmail(wxUserId, email,corpId);
	}
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IWxLoginService#getListOrg(java.util.Map)
	 */
	@Override
	public List<OrgVO> getListOrg(Map<String, Object> searchMap)
			throws Exception, BaseException {
		if(searchMap==null){
			return orgService.listOrgRoot();
		}
		return wxLoginDAO.getListOrg(searchMap);
	}
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.service.IWxLoginService#listMyPersonByOrg(java.lang.String, cn.com.do1.common.dac.Pager, java.util.Map)
	 */
	@Override
	public Pager listMyPersonByOrg(String orgId, Pager pager, Map searchValue)
			throws Exception, BaseException {
		/*if(searchValue == null){
			searchValue = new HashMap<String, Object>();
		}
		searchValue.put("orgId", orgId);*/
		return wxLoginDAO.listMyPersonByOrg(pager, searchValue);
	}
    
}
