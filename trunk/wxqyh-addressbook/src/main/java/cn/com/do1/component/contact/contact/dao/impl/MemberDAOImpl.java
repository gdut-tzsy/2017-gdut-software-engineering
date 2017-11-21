package cn.com.do1.component.contact.contact.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.component.addressbook.contact.model.TbQyMemberConfigResPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyMemberInfoVO;
import cn.com.do1.component.contact.contact.dao.IMemberDAO;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司 All rights reserved. User: ${user}
 */
public class MemberDAOImpl extends BaseDAOImpl implements IMemberDAO {
	private final static transient Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);

	@Override
	public List<TbQyMemberInfoVO> findUsersByOrgId(Map<String, Object> map)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql=" SELECT * FROM tb_qy_member_info where ORG_ID =:orgId and status='0' " +
				"and (mobile =:mobile or weixin_num =:weixinNum or email =:email or wx_user_id =:wxUserId)";
		return searchByField(TbQyMemberInfoVO.class, sql, map);
	}

	@Override
	public void delConfigDes(String id) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="delete from tb_qy_member_config_res where con_id =:id";
		this.preparedSql(sql);
		this.setPreValue("id", id);
		this.executeUpdate();
	}

	@Override
	public List<TbQyMemberConfigResPO> getDeptList(String orgId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql=" SELECT * FROM tb_qy_member_config_res where ORG_ID =:orgId order by sort ";
		this.preparedSql(sql);
		this.setPreValue("orgId", orgId);
		return getList(TbQyMemberConfigResPO.class);
	}

	private static String update_sql=" update tb_qy_member_info set config_name =:name where org_id =:orgId and ref_id=:id ";
	@Override
	public void updateConfigName(String orgId, String id,String name) throws Exception,
			BaseException {
		// TODO 自动生成的方法存根
		this.preparedSql(update_sql);
		this.setPreValue("orgId", orgId);
		this.setPreValue("id", id);
		this.setPreValue("name", name);
		this.executeUpdate();
	}

}
