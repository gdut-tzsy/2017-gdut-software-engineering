package cn.com.do1.component.filehandle.filehandle.dao.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseDAOImpl;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.filehandle.filehandle.dao.IFilehandleDAO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileCommentPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileFlowPO;
import cn.com.do1.component.filehandle.filehandle.model.TbYsjdFileHandleInfoPO;
import cn.com.do1.component.filehandle.filehandle.util.CommonUtil;
import cn.com.do1.component.filehandle.filehandle.vo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright &copy; 2010 广州市道一信息技术有限公司
 * All rights reserved.
 * User: ${user}
 */
public class FilehandleDAOImpl extends BaseDAOImpl implements IFilehandleDAO {

	static List<String> childDept = new ArrayList<String>();

	@Override
	public Pager getFileHandlePager(Map map, Pager pager) throws Exception, BaseException {
		String condSql = "from tb_ysjd_file where title like :title and create_user_name like :creator and create_time >= :startTimes and create_time <= :endTime and status = :status order by create_time desc";
		String searchSql = "select id,title,create_user_name,create_time,status  " + condSql;
		String countSql = "select count(1) " + condSql;
		return pageSearchByField(TbYsjdFileVO.class, countSql, searchSql, map, pager);
	}

	@Override
	public List<TbQyUserInfoVO> getUserInfoListByUserId(String userId) throws Exception, BaseException {
		String temp = "";
		if (userId != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(userId);
			temp = m.replaceAll("");
		}
		String sql = "select * from tb_qy_user_info where user_id in(@userId) and user_status <> '-1' and user_status <> '-2'".replace("@userId", temp);
		preparedSql(sql);
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public boolean isExistDepts(String deptIds, String userId) throws Exception, BaseException {
		String sql = "SELECT COUNT(1)  FROM tb_qy_user_department_ref WHERE user_id = :userId and department_id in(:deptIds)";
		preparedSql(sql);
		setPreValue("userId", userId);
		setPreValue("deptIds", deptIds);
		return executeCount() > 0;
	}

	@Override
	public List<String> getChildDept(String deptId) throws Exception, BaseException {
		String sql = "select id from tb_department_info where parent_depart in(:deptId)";
		preparedSql(sql);
		setPreValue("deptId", deptId);
		return getList(String.class);
	}

	@Override
	public List<String> getUserDept(String userId) throws Exception, BaseException {
		String sql = "SELECT distinct(department_id) FROM tb_qy_user_department_ref WHERE user_id = :userId";
		preparedSql(sql);
		setPreValue("userId", userId);
		List<String> list = getList(String.class);
		List<String> newList = new ArrayList<String>();
		newList.addAll(list);
		//获取以上部门的子部门
		for (String deptId : newList) {
			list.addAll(getAllChildDept(list, deptId));
		}
		list = CommonUtil.removeRepeatElm(list);
		return list;
	}

	/**
	 * @description 获取所有的子部门
	 * @method getAllChildDept
	 * @Param deptId
	 * @Return
	 * @time 2017-9-7
	 * @author lwj
	 * @version V1.0.0
	 */
	private List<String> getAllChildDept(List<String> allChildDept, String deptId) throws Exception, BaseException {
		String sql = "select id from tb_department_info where parent_depart = :deptId";
		preparedSql(sql);
		setPreValue("deptId", deptId);
		List<String> list = getList(String.class);
		if(list != null && list.size() > 0){
			allChildDept.addAll(list);
			//去重
			allChildDept = CommonUtil.removeRepeatElm(allChildDept);
			for (String id : list) {
				allChildDept.addAll(getAllChildDept(allChildDept, id));
			}
		}
		//去重
		allChildDept = CommonUtil.removeRepeatElm(allChildDept);
		return allChildDept;
	}

	@Override
	public TbYsjdFileHandleInfoPO getInchargeHandleInfo(String handleUserId, String fileId) throws Exception, BaseException {
		String sql = "SELECT * FROM tb_ysjd_file_handle_info WHERE handle_user_id = :handleUserId and file_id = :fileId";
		preparedSql(sql);
		setPreValue("handleUserId", handleUserId);
		setPreValue("fileId", fileId);
		return executeQuery(TbYsjdFileHandleInfoPO.class);
	}

	@Override
	public TbYsjdFileVO getFileDetail(String id) throws Exception, BaseException {
		String sql = "SELECT * FROM tb_ysjd_file WHERE id = :id";
		preparedSql(sql);
		setPreValue("id", id);
		return executeQuery(TbYsjdFileVO.class);
	}

	@Override
	public Pager getFilePage(Map map, Pager pager) throws Exception, BaseException {
		StringBuilder sb = new StringBuilder();
		boolean b = false;
		if (map.containsKey("isLeader")) {
			b = true;
			map.remove("isLeader");
		}
		//类型 0我提交的，1我负责的，2我相关
		int type = Integer.parseInt(map.get("type").toString());
		//按状态搜索（对数据状态） 0全部，1待处理，2处理中，3已结束
		int statusDesc = Integer.parseInt(map.get("statusDesc").toString());

		//按处理状态（对人的审批状态） 0待处理 1已处理 无表示我相关，我相关能看到所有数据
		int status = Integer.parseInt(map.get("status").toString());

		if (type == 0) {
			sb.append(" and create_user_id like :userId ");
			if (status == 0 && (statusDesc == 2 || statusDesc == 0)) {
				sb.append(" and status = '1'");
			} else if (status == 1) {
				if (statusDesc == 0) {
					sb.append(" and status <> '1' ");
				} else if (statusDesc == 1) {//待领导处理
					sb.append(" and status = '0' ");
				} else if (statusDesc == 2) {
					sb.append(" and status = '2' ");
				} else if (statusDesc == 3) {
					sb.append(" and status = '3' ");
				} else {
					sb.append(" and 1 != 1 ");
				}
			} else {
				sb.append(" and 1 != 1 ");
			}
		} else if (type == 1) {    //负责人和领导看到的都是不一样的，如果在同一个页面要展示就会比较难，除非加多一个字段；否则这就是一个bug，当这个人既是审批领导，也是负责人
			sb.append(" and (approver_user_id like :userId or incharges like :userId) ");
			if (b) {//领导
				if (status == 1) {
					if (statusDesc == 0) {
						sb.append(" and status <> '0' ");
					} else if (statusDesc == 2) {
						sb.append(" and (status = '2' or status = '1') ");
					} else if (statusDesc == 3) {
						sb.append(" and status = '3' ");
					} else {
						sb.append(" and 1 != 1 ");
					}
				} else if (status == 0 && (statusDesc == 0 || statusDesc == 1)) {
					sb.append(" and status = '0' ");
				} else {
					sb.append(" and 1 != 1 ");
				}
			} else {//负责人
				if (status == 0 && (statusDesc == 0 || statusDesc == 2)) {
					sb.append(" and status = '2' ");
				} else if (status == 1) {
					sb.append(" and status = '2' and handled_user like :userId ");
				} else {
					sb.append(" and 1 != 1 ");
				}
			}
		} else if (type == 2) {    //我相关
			sb.append(" and (related_person like :userId or common_person like :userId) ");
			//相关人，只有数据的状态
			if (statusDesc == 0) {
				sb.append(" and 1 = 1 ");
			} else if (statusDesc == 1) {
				sb.append(" and status = '0' ");
			} else if (statusDesc == 2) {
				sb.append(" and (status = '1' or status = '2')");
			} else if (statusDesc == 3) {
				sb.append(" and status = '3' ");
			} else {
				sb.append(" and 1 != 1 ");
			}
		} else {
			sb.append(" and 1 != 1 ");
		}

		map.remove("id");
		map.remove("statusDesc");
		map.remove("status");
		map.remove("type");

		sb.append(" and title like :title order by create_time desc");
		String condSql = " from tb_ysjd_file where title like :title and 1 = 1 " + sb.toString();
		String countSql = "select count(1)" + condSql;
		String searchSql = "select id,title,status,create_user_name,create_time" + condSql;
		return pageSearchByField(TbYsjdFileVO.class, countSql, searchSql, map, pager);
	}

	@Override
	public boolean isRelevant(String fileId, String userId) throws Exception, BaseException {
		preparedSql("select count(1) from tb_ysjd_file_recipient where file_id = :fileId and user_id = :userId");
		setPreValue("userId", userId);
		setPreValue("fileId", fileId);
		return executeCount() > 0;
	}

	@Override
	public boolean isHandler(String fileId, String userId) throws Exception, BaseException {
		preparedSql("select count(1) from tb_ysjd_file_handle_info where file_id = :fileId and handle_user_id = :userId");
		setPreValue("userId", userId);
		setPreValue("fileId", fileId);
		return executeCount() > 0;
	}

	@Override
	public List<TbYsjdFileFlowVO> getFileFlow(String fileId) throws Exception, BaseException {
		preparedSql("select * from tb_ysjd_file_flow where file_id = :fileId order by is_handle, create_time desc");
		setPreValue("fileId", fileId);
		return getList(TbYsjdFileFlowVO.class);
	}

	@Override
	public boolean isAllInchargeHandle(String fileId) throws Exception, BaseException {
		preparedSql("select count(1) from tb_ysjd_file_handle_info where file_id = :fileId and is_handle = '0'");
		setPreValue("fileId", fileId);
		return executeCount() > 0;
	}

	@Override
	public boolean isExistIncharge(String fileId, String userId) throws Exception, BaseException {
		preparedSql("select count(1) from tb_ysjd_file_recipient where file_id = :fileId and user_id = :userId");
		setPreValue("fileId", fileId);
		setPreValue("userId", userId);
		return executeCount() > 0;
	}

	@Override
	public TbYsjdFileFlowPO getNextFlow(String fileId) throws Exception, BaseException {
		preparedSql("select * from tb_ysjd_file_flow where file_id = :fileId and is_handle = '0'");
		setPreValue("fileId", fileId);
		return executeQuery(TbYsjdFileFlowPO.class);
	}

	@Override
	public void deleteNextFlow(String fileId) throws Exception, BaseException {
		preparedSql("delete from tb_ysjd_file_flow where file_id = :fileId and is_handle = '0'");
		setPreValue("fileId", fileId);
		executeUpdate();
	}

	@Override
	public boolean isInchargeHandleFile(String fileId, String userId) throws Exception, BaseException {
		preparedSql("select count(1) from tb_ysjd_file_handle_info where is_handle = '1' and file_id = :fileId and handle_user_id = :userId");
		setPreValue("fileId", fileId);
		setPreValue("userId", userId);
		return executeCount() > 0;
	}

	@Override
	public List<TbYsjdFilePicVO> getFlowPic(String fileId, String userId) throws Exception, BaseException {
		preparedSql("select * from tb_ysjd_file_pic where file_id = :fileId and create_person = :userId");
		setPreValue("fileId", fileId);
		setPreValue("userId", userId);
		return getList(TbYsjdFilePicVO.class);
	}

	@Override
	public String getUserAllDeptName(String userId) throws Exception, BaseException {
		preparedSql("SELECT GROUP_CONCAT(d.department_name SEPARATOR'、') FROM tb_department_info d LEFT JOIN tb_qy_user_department_ref r ON r.department_id = d.id WHERE  r.user_id = :userId");
		setPreValue("userId", userId);
		return executeQuery(String.class);
	}

	@Override
	public List<TbQyUserInfoVO> getNotHandleIncharge(String fileId) throws Exception, BaseException {
		preparedSql("SELECT u.user_id,u.person_name FROM tb_qy_user_info u LEFT JOIN tb_ysjd_file_handle_info f ON u.user_id = f.handle_user_id WHERE f.file_id = :fileId AND f.is_handle = '0'");
		setPreValue("fileId", fileId);
		return getList(TbQyUserInfoVO.class);
	}

	@Override
	public void updateCommentStatus(String commentId) throws BaseException, Exception {
		preparedSql("DELETE FROM tb_ysjd_file_comment where ID =:id ");
		setPreValue("id", commentId);
		executeUpdate();
	}

	@Override
	public List<TbYsjdFileCommentPO> getCommentsByUserID(String userId, String diaryId) throws BaseException, Exception {
		preparedSql("select t.* from tb_ysjd_file_comment t where  t.file_id =:diaryId and t.create_person =:userId order by t.create_time desc ");
		setPreValue("diaryId", diaryId);
		setPreValue("userId", userId);
		return getList(TbYsjdFileCommentPO.class);
	}

	@Override
	public List<TbYsjdFileRecipientVO> getDiaryRecipient(String diaryId, String type) throws Exception, BaseException {
		preparedSql("select t.* from tb_ysjd_file_recipient t where t.file_id =:diaryId and t.user_type =:type ORDER BY t.sort_num");
		setPreValue("diaryId", diaryId);
		setPreValue("type", type);
		return getList(TbYsjdFileRecipientVO.class);
	}

	@Override
	public Pager getDiaryComment(Map map, Pager comments) throws Exception, BaseException {
		String countSql = "select count(1) from tb_ysjd_file_comment t  where t.file_id =:fileId and t.create_time < :lastCreateTime and t.status <>:status2  and t.status <>:status3";
		String searchSql = "select t.ID,t.CONTENT, t.type,t.create_time,t.status,t.create_person, t.file_id ,t.person_name,t.head_pic from tb_ysjd_file_comment t where t.file_id =:fileId and t.create_time < :lastCreateTime and t.status <>:status2 and t.status <>:status3 order by t.create_time desc";
		return pageSearchByField(TbYsjdFileCommentVO.class, countSql, searchSql, map, comments);
	}

	@Override
	public int countDiaryComment(String userId, String diaryId) throws BaseException, Exception {
		preparedSql("SELECT count(1) FROM tb_ysjd_file_comment where create_person=:userId and file_id = :diaryId");
		setPreValue("diaryId", diaryId);
		setPreValue("userId", userId);
		return executeCount();
	}

	@Override
	public void updateDiaryRec(String userId, String diaryId) throws Exception, BaseException {
		preparedSql("UPDATE tb_ysjd_file_recipient SET readen_status=1 WHERE file_id=:diaryId AND user_id=:userId ");
		setPreValue("userId", userId);
		setPreValue("diaryId", diaryId);
		executeUpdate();
	}

	@Override
	public Pager findAlluserByDeptId(Map searchMap, Pager pager, List<TbDepartmentInfoVO> depts, String userId) throws Exception, BaseException {
		String sortTop = "";
		if (!AssertUtil.isEmpty(searchMap.get("sortTop"))) {
			sortTop = "t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}

		//获取这个用户的所有部门,让这个用户只能选自己的部门
		String sb = getDept(userId);

		String sql = "select t.id,t.user_id,t.PERSON_NAME,t.head_pic,t.pinyin,t.wx_user_id,t.MOBILE,t.IS_TOP,t.position,t.user_status,t.attribute from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud where  d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus " + sb;
		String count = "select count(1) from(select t.id from TB_QY_USER_INFO t,tb_department_info d,tb_qy_user_department_ref ud where d.id=ud.department_id and ud.user_id = t.user_id and t.org_id =:orgId and d.org_id =:orgId and t.user_status <> :aliveStatus " + sb;
		String depqrtSQL = DepartmentUtil.assembleSql(searchMap, searchMap, depts);
		sql = sql + " " + depqrtSQL + " group by t.id order by " + sortTop + "t.pinyin ";
		count = count + " " + depqrtSQL + " group by t.id)tt ";
		return pageSearchByField(TbQyUserInfoVO.class, count, sql, searchMap, pager);
	}

	/**
	 * @description 获取这个用户的部门及子部门
	 * @method getDept
	 * @Param userId
	 * @Return
	 * @time 2017-9-7
	 * @author lwj
	 * @version V1.0.0
	 */
	private String getDept(String userId) throws Exception, BaseException {
		StringBuilder sb = new StringBuilder();
		List<String> list = getUserDept(userId);
		if (list == null || list.size() <= 0) {
			sb.append(" and 1!=1 ");
		} else {
			for (String s : list) {
				sb.append(" or ud.department_id = '").append(s).append("'");
			}
			sb.delete(0, 4);
			sb.insert(0, " and (");
			sb.append(")");
		}
		return sb.toString();
	}

	@Override
	public Pager searchContactByPy(Map searchMap, Pager pager, String userId) throws Exception, BaseException {
		String sortTop = "";
		if (!AssertUtil.isEmpty(searchMap.get("sortTop"))) {
			sortTop = "t.IS_TOP ASC,";
			searchMap.remove("sortTop");
		}

		//获取这个用户的所有部门,让这个用户只能选自己的部门，及其子部门
		String sb = getDept(userId);

		String search = "select distinct(t.user_id),t.* from TB_QY_USER_INFO t,tb_qy_user_department_ref ud where t.user_id = ud.user_id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName " + sb + " order by " + sortTop + " t.pinyin";
		String count = "select count(1) from TB_QY_USER_INFO t,tb_qy_user_department_ref ud where t.user_id = ud.user_id and t.org_id =:orgId and t.user_status <> :aliveStatus and t.person_name like :personName " + sb;
		return pageSearchByField(TbQyUserInfoVO.class, count, search, searchMap, pager);
	}
}
