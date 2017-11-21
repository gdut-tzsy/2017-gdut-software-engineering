package cn.com.do1.component.contact.department.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.DqdpOrgVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.memcached.CacheDqdpOrgManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.department.model.TbSynDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.DepTotalUserVO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * 统计部门人数
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author Luo Rilang
 * @2015-9-29
 * @version 1.0
 * 修订历史：
 * 日期          作者        参考         描述
 */
public class SynDepartment {
	private final static transient Logger logger = LoggerFactory.getLogger(SynDepartment.class);
	private static IDepartmentMgrService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentMgrService.class);
	private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);

	/**
	 * 更新部门人数，父部门为子部门和当前部门人数总和
	 * @param orgId
	 * @2015-9-24
	 * @version 1.0
	 */
	public static void SynDepartmentTotalUser(String orgId) {
		try {
			//获取org下所有的部门人数列表包含子部门
			List<DepTotalUserVO> list = departmentService.getDepTotalUserByOrgId(orgId);
			if(AssertUtil.isEmpty(list)){//没有部门的机构直接结束
				return;
			}
			Map<String, DepTotalUserVO> map = new HashMap<String, DepTotalUserVO>();
			DepTotalUserVO totalUserVO;
			//把所有部门放进map里面，方便通过部门id取到对应部门
			for (int i = 0; i < list.size(); i++) {
				totalUserVO = list.get(i);
				map.put(totalUserVO.getId(), totalUserVO);
			}
			
			List<DepTotalUserVO> newList = new ArrayList<DepTotalUserVO>();//用于存在暂时未找到父节点的部门，用于统一放入？
			DepTotalUserVO root = new DepTotalUserVO();
			DepTotalUserVO node;
			//组装成部门树
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) != null){
					DepTotalUserVO dep =  list.get(i);
					if(AssertUtil.isEmpty(dep.getParentDepart())){//如果没有父部门，直接添加
						root.addChildrenDept(dep);
						continue;
					}
					node = map.get(dep.getParentDepart());
					if(node != null){
						node.addChildrenDept(dep);
					}else{
						newList.add(dep);
					}
				}
			}
			List<TbSynDepartmentInfoPO> deptList = new ArrayList<TbSynDepartmentInfoPO>();
			//统计子部门和父部门人数，把整理好的部门数据放回deptList里
			calculateTotalUser(root,deptList);
			//批量执行更新deptList
			deptList = QwtoolUtil.updateBatchList(deptList, false, true);
			if(!AssertUtil.isEmpty(deptList)){
				for (int i = 0; i < deptList.size(); i++) {
					logger.error("部门人数批量更新失败："+deptList.get(i).getId());
				}
			}
		} catch (Exception e) {
			logger.error("根据orgId更新部门用户数出错：" + e.getMessage(), e);
		} catch (BaseException e) {
			logger.error("根据orgId更新部门用户数出错：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 统计人数
	 * @param root
	 * @return
	 * @author Luo Rilang
	 * @2015-9-23
	 * @version 1.0
	 */
	private static DepTotalUserVO calculateTotalUser(DepTotalUserVO root, List<TbSynDepartmentInfoPO> deptList){
		Integer total = AssertUtil.isEmpty(root.getTotalUser()) ? 0 : root.getTotalUser();
		List<DepTotalUserVO> tempList = root.getChildrenDept();	//该节点的子部门
		if(AssertUtil.isEmpty(tempList) || tempList.size() == 0){	//如果没有子部门，直接添加当前部门并退出
			root.setTotalAllUser(total);
			TbSynDepartmentInfoPO po = new TbSynDepartmentInfoPO();
			po.setId(root.getId());
			po.setTotalUser(total);
			deptList.add(po);
			return root;
		}
		//如果存在子部门，则迭代所有子部门
		DepTotalUserVO node;
		for (int i = 0; i < tempList.size(); i++) {
			node = tempList.get(i);
			//如果子部门没有子部门了，则添加当前部门，否则继续迭代
			if(AssertUtil.isEmpty(node.getChildrenDept()) || node.getChildrenDept().size() == 0){
				node.setTotalAllUser(node.getTotalUser());
				total += node.getTotalUser();
				TbSynDepartmentInfoPO po = new TbSynDepartmentInfoPO();
				po.setId(node.getId());
				po.setTotalUser(node.getTotalUser());
				deptList.add(po);
			}else{
				node = calculateTotalUser(node,deptList);//继续迭代子部门
				total += node.getTotalAllUser();
			}
		}
		//迭代完子部门后将所有人数总和设置到当前部门总人数中
		root.setTotalAllUser(total);
		if(!AssertUtil.isEmpty(root.getId())){//往部门总集合里面添加当前部门信息（部门人数已经更新）
			TbSynDepartmentInfoPO po = new TbSynDepartmentInfoPO();
			po.setId(root.getId());
			po.setTotalUser(total);
			deptList.add(po);
		}
		return root;
	}
	
	
	/**
	 * 更新部门人数，父部门为子部门和当前部门人数总和
	 * @param orgId
	 * @2015-9-24
	 * @version 1.0
	 */
	public static boolean SynDepartmentTotal(String orgId) {
		try {
			//获取org下所有的部门人数列表包含子部门
			List<DepTotalUserVO> list = departmentService.getDepTotalUserByOrgId(orgId);
			List<TbSynDepartmentInfoPO> updateList = new ArrayList<TbSynDepartmentInfoPO>();
			TbSynDepartmentInfoPO po;
			//遍历查出来的所有部门，包括父部门和子部门
			for (DepTotalUserVO total : list) {
				po = new TbSynDepartmentInfoPO();
				//根据父部门名称和orgId获取该父部门下的所有子部门信息，包括人数
				int childrenTotal = departmentService.getDepTotalUser(orgId, total.getDeptFullName());
				po.setId(total.getId());
				po.setTotalUser(childrenTotal);	//所有子部门人数总和
				updateList.add(po);//添加到要更新的部门集合里面
			}
			//批量更新部门
			updateList = QwtoolUtil.updateBatchList(updateList, false, true);
			if(!AssertUtil.isEmpty(updateList)){
				for (int i = 0; i < updateList.size(); i++) {
					logger.error("部门人数批量更新失败："+updateList.get(i).getId());
				}
			}

			int total = contactService.countOrgPerson(orgId);
			int totalLeave = contactService.countOrgLeavePerson(orgId);

			ExtOrgPO orgPO = new ExtOrgPO();
			orgPO.setOrganizationId(orgId);
			orgPO.setTotalLeave(totalLeave);
			orgPO.setTotalMember(total-totalLeave);//总数减去离职用户数即为通讯录人数
			contactService.updatePO(orgPO,false);
			if(Configuration.IS_USE_MEMCACHED){
				DqdpOrgVO org = contactService.getOrgByOrgId(orgId);
				if (org != null) {
					org.setTotalMember(orgPO.getTotalMember());
					org.setTotalLeave(orgPO.getTotalLeave());
					CacheDqdpOrgManager.set(orgId, org);
				}
			}

			return true;
		} catch (Exception e) {
			logger.error("根据orgId更新部门用户数出错：" + e.getMessage(), e);
			return false;
		} catch (BaseException e) {
			logger.error("根据orgId更新部门用户数出错：" + e.getMessage(), e);
			return false;
		}
	}
}
