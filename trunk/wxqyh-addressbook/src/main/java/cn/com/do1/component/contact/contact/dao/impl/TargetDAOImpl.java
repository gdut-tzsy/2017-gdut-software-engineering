package cn.com.do1.component.contact.contact.dao.impl;

import java.util.List;
import java.util.Map;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyTargetUserPO;
import cn.com.do1.component.addressbook.contact.vo.TargetUserGroupVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.contact.contact.dao.ITargetDAO;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;

public class TargetDAOImpl extends BaseDAOImpl implements ITargetDAO{

	private final static String getTargetUserPOByGroupId_sql="SELECT * FROM tb_qy_target_user WHERE group_id=:groupId ORDER BY sort_num ASC";
	@Override
	public List<TbQyTargetUserPO> getTargetUserPOByGroupId(String groupId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		preparedSql(getTargetUserPOByGroupId_sql);
		this.setPreValue("groupId", groupId);
		return getList(TbQyTargetUserPO.class);
	}
	
	private final static String delTargetUserByGroupId_sql="DELETE FROM tb_qy_target_user WHERE group_id=:groupId AND STATUS=:status";
	@Override
	public void delTargetUserByGroupId(String groupId, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		preparedSql(delTargetUserByGroupId_sql);
		this.setPreValue("groupId", groupId);
		this.setPreValue("status", status);
		this.executeUpdate();
	}
	
	private final static String getTargetUserPO_sql="SELECT * FROM tb_qy_target_user WHERE group_id=:groupId and user_id=:userId";
	@Override
	public TbQyTargetUserPO getTargetUserPO(String groupId, String userId)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		preparedSql(getTargetUserPO_sql);
		this.setPreValue("groupId", groupId);
		this.setPreValue("userId", userId);
		return this.executeQuery(TbQyTargetUserPO.class);
	}
	
	@Override
	public List<TbQyUserInfoVO> getTargetUserList(Map<String, Object> paramMap)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.USER_ID,t.PERSON_NAME,t.wx_user_id,t.head_pic,t.mobile" +
				",GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"+
				",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
				//",GROUP_CONCAT(d.wx_id SEPARATOR '|') as wx_dept_ids"+
				" FROM tb_qy_target_user a,TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
				" WHERE a.user_id=t.user_id AND t.USER_ID=ud.user_id and ud.department_id=d.id"+
				" AND a.org_id=:orgId AND a.group_id=:groupId AND a.status=:status AND t.USER_STATUS<>:leaveStatus" +
				" AND t.person_name like :keyWord"+
				" GROUP BY t.user_id"+
				" ORDER BY a.sort_num ASC");
		return searchByField(TbQyUserInfoVO.class, sql.toString(), paramMap);
	}
	@Override
	public Pager getTargetUserPager(Map<String, Object> paramMap, Pager pager)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		StringBuilder sql=new StringBuilder();
		StringBuilder countSql=new StringBuilder();
		
		sql.append("SELECT t.USER_ID,t.PERSON_NAME,t.wx_user_id,t.head_pic,t.mobile" +
				",GROUP_CONCAT(d.id ORDER BY ud.sort SEPARATOR ';') as dept_id"+
				",GROUP_CONCAT(d.dept_full_name ORDER BY ud.sort SEPARATOR ';') as department_name"+
				//",GROUP_CONCAT(d.wx_id SEPARATOR '|') as wx_dept_ids"+
				" FROM tb_qy_target_user a,TB_QY_USER_INFO t, tb_qy_user_department_ref ud, tb_department_info d" +
				" WHERE a.user_id=t.user_id AND t.USER_ID=ud.user_id and ud.department_id=d.id"+
				" AND a.org_id=:orgId AND a.group_id=:groupId AND a.status=:status AND t.USER_STATUS<>:leaveStatus" +
				" AND t.person_name like :keyWord"+
				" GROUP BY t.user_id"+
				" ORDER BY a.sort_num ASC");
		countSql.append("SELECT count(1)" +
				" FROM tb_qy_target_user a,TB_QY_USER_INFO t"+
				" WHERE a.user_id=t.user_id"+
				" AND a.org_id=:orgId AND a.group_id=:groupId AND a.status=:status AND t.USER_STATUS<>:leaveStatus" +
				" AND t.person_name like :keyWord");
		return pageSearchByField(TbQyUserInfoVO.class, countSql.toString(), sql.toString(), paramMap, pager);
	}
	
	
	@Override
	public void updateTargetUserStatus(String groupId, String[] userIdArray, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="UPDATE tb_qy_target_user SET status=:status WHERE group_id=:groupId and user_id in ('" + StringUtil.uniteArry(userIdArray, "','") + "')";
		this.preparedSql(sql);
		this.setPreValue("status", status);
		this.setPreValue("groupId", groupId);
		this.executeUpdate();
	}

	@Override
	public void delTargetUser(String groupId, String userId, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="DELETE FROM tb_qy_target_user WHERE group_id=:groupId and status=:status and user_id=:userId";
		this.preparedSql(sql);
		this.setPreValue("status", status);
		this.setPreValue("groupId", groupId);
		this.setPreValue("userId", userId);
		this.executeUpdate();
	}

	@Override
	public void delTargetUser(String groupId, String[] userIdArray, int status)
			throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String sql="DELETE FROM tb_qy_target_user WHERE group_id=:groupId and status=:status and user_id in ('" + StringUtil.uniteArry(userIdArray, "','") + "')";
		this.preparedSql(sql);
		this.setPreValue("status", status);
		this.setPreValue("groupId", groupId);
		this.executeUpdate();
	}
	
	private final static String getTargetUserGroupVOList="SELECT group_id,agent_code FROM tb_qy_target_user WHERE create_time<=:limitTime GROUP BY group_id";
	@Override
	public List<TargetUserGroupVO> getTargetUserGroupVOList(
			Map<String, Object> paramMap) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		return searchByField(TargetUserGroupVO.class, getTargetUserGroupVOList, paramMap);
	}
	
	
	@Override
	public void updateBuildUnreadStatus(String groupId, String agentCode,
			int status) throws Exception, BaseException {
		// TODO 自动生成的方法存根
		String updateBuildUnreadStatus_sql="";
		if(WxAgentUtil.getDynamicCode().equals(agentCode)){
			//新闻公告未阅
			updateBuildUnreadStatus_sql="UPDATE tb_xyh_dynamicinfo SET BUILD_UNREAD_STATUS=:status WHERE DYNAMIC_INFO_ID=:groupId";
		}else if(WxAgentUtil.getTopicCode().equals(agentCode)){
			//同事社区未阅
			updateBuildUnreadStatus_sql="UPDATE tb_xyh_topic SET BUILD_UNREAD_STATUS=:status WHERE TOPIC_ID=:groupId";
		}else if(WxAgentUtil.getSurveyCode().equals(agentCode)){
			//调查问卷未答
			updateBuildUnreadStatus_sql="UPDATE tb_questionnaire_info SET BUILD_UNANSWER_STATUS=:status WHERE id=:groupId";
		}else if(WxAgentUtil.getActivityCode().equals(agentCode)){
			updateBuildUnreadStatus_sql="UPDATE tb_qy_activity SET BUILD_UNREGISTRY_STATUS=:status WHERE ACTIVITY_ID=:groupId";
		}else{
			throw new NonePrintException("2001","agentCode不合法agentCode:"+agentCode);
		}
		this.preparedSql(updateBuildUnreadStatus_sql);
		this.setPreValue("groupId", groupId);
		this.setPreValue("status", status);
		this.executeUpdate();
	}
	
}