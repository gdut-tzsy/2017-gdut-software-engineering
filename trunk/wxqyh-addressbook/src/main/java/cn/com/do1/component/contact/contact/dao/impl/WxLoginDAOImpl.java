package cn.com.do1.component.contact.contact.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.reflation.ClassTypeUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.contact.contact.dao.IWxLoginDAO;
import cn.com.do1.component.systemmgr.org.model.OrgVO;
import cn.com.do1.component.systemmgr.user.model.BaseUserVO;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserWxloginInfoPO;

/**
 * 
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Sun Qinghai
 * @2015-1-29
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class WxLoginDAOImpl extends BaseDAOImpl implements IWxLoginDAO {

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IWxLoginDAO#findWxAccountByEmail(java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserWxloginInfoPO findWxAccountByEmail(String email,
			String corpId) throws Exception, BaseException {
		String sql = "select t.*" +
				" from tb_qy_user_wxlogin_info t" +
				" where t.email = :email and t.corp_id=:corpId";
		preparedSql(sql);
        setPreValue("corpId",corpId);//将参数设置进预置语句
        setPreValue("email",email);
        return this.executeQuery(TbQyUserWxloginInfoPO.class);
	}

	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IWxLoginDAO#getRubbishOrg(java.lang.String)
	 */
	@Override
	public List<ExtOrgPO> getRubbishOrg(String corpId) throws Exception,
			BaseException {
		String sql = "SELECT o.* from tb_dqdp_organization o" +
				" LEFT JOIN tb_qy_user_info u ON o.ORGANIZATION_ID=u.ORG_ID and u.corp_id=:corpId" +
				" LEFT JOIN tb_qy_experience_application e ON o.ORGANIZATION_NAME= e.enterprise_name and e.create_time> :create_time" +
				" where o.corp_id=:corpId" +
				" GROUP BY o.ORGANIZATION_ID" +
				" HAVING count(u.USER_ID) =0";
	    Date date = new Date();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);  
	    calendar.add(Calendar.WEDNESDAY, -1);
		preparedSql(sql);
        setPreValue("corpId",corpId);//将参数设置进预置语句
        setPreValue("create_time",calendar.getTime());
        return super.getList(ExtOrgPO.class);
	}

	private static final String findWxAccountByUserIdAndEmail_sql = "select t.*" +
			" from tb_qy_user_wxlogin_info t" +
			" where t.corp_id=:corpId and ( 1=2 ";
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IWxLoginDAO#findWxAccountByUserIdAndEmail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public TbQyUserWxloginInfoPO findWxAccountByUserIdAndEmail(String wxUserId,
			String email, String corpId) throws Exception, BaseException {
		StringBuffer sql = new StringBuffer(findWxAccountByUserIdAndEmail_sql);
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("corpId", corpId);
		if(!StringUtil.isNullEmpty(wxUserId)){
			sql.append("or t.wx_user_id = :wxUserId ");
			map.put("wxUserId", wxUserId);
		}
		if(!StringUtil.isNullEmpty(email)){
			sql.append("or t.email = :email ");
			map.put("email", email);
		}
		sql.append(")");
		preparedSql(sql.toString());
        setPreValues(map);//将参数设置进预置语句
        return this.executeQuery(TbQyUserWxloginInfoPO.class);
	}

	private final static String GETLISTORG_SQL = "select * from tb_dqdp_organization t where 1=1 and t.ORGANIZATION_DESCRIPTION like :orgName and t.corp_id=:corpId ";
	@Override
	public List<OrgVO> getListOrg(Map<String, Object> searchMap) throws Exception,
			BaseException {
	    return this.searchByField(OrgVO.class, GETLISTORG_SQL, searchMap);
	}
	
	private final static String listMyPersonByOrg = " from tb_dqdp_user u,tb_person_user_ref pur,tb_person_organization_ref por, tb_dqdp_organization o,tb_dqdp_person p" +
			" where u.user_id = pur.user_id and p.person_id = pur.person_id and p.person_id = por.person_id and o.organization_id = por.org_id" +
			" and o.organization_id = :orgId and u.user_name like :userName and p.person_name like :personName";
	private final static String listMyPersonByOrg_Count = "select count(1) "+listMyPersonByOrg;
	private final static String listMyPersonByOrg_SQL = "select u.USER_ID,u.USER_NAME, p.person_id, p.person_name,o.ORGANIZATION_DESCRIPTION as org_name" +listMyPersonByOrg;
	/* （非 Javadoc）
	 * @see cn.com.do1.component.contact.contact.dao.IWxLoginDAO#listMyPersonByOrg(cn.com.do1.common.dac.Pager, java.util.Map)
	 */
	@Override
	public Pager listMyPersonByOrg(Pager pager, Map searchValue)
			throws Exception, BaseException {
		return super.pageSearchByField(ClassTypeUtil.getAutoReplaceBean(BaseUserVO.class), listMyPersonByOrg_Count, listMyPersonByOrg_SQL, searchValue, pager);
	}
    
}
