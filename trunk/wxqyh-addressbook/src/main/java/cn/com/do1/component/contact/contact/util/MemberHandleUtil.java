/*
 * Copyright © 2015 广东道一信息技术股份有限公司
 * All rights reserved.
 */
package cn.com.do1.component.contact.contact.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyMemberConfigPO;
import cn.com.do1.component.addressbook.contact.model.TbQyMemberTargetPersonPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.UserRedundancyInfoVO;
import cn.com.do1.component.contact.contact.service.IMemberService;
import cn.com.do1.dqdp.core.DqdpAppContext;

/**
 * <p>Title: 功能/模块</p>
 * <p>Description: 类的描述</p>
 * @author luobowen
 * @2016-6-29
 * @version 1.0
 * 修订历史：
 * 日期       作者  :Luobowen      参考         描述:广州市道一信息技术有限公司
 */
public class MemberHandleUtil {

	/**
	 * 生日祝福提醒方式(0:按农历)
	 */
	public final static String REMIND_TYPE_ZERO = "0";
	/**
	 * 生日祝福提醒方式(1:按阳历)
	 */
	public final static String REMIND_TYPE_ONE = "1";
	
	/**
	 * 新增的邀请单状态0
	 */
	public static String NOT_APPROVED="0";
	
	/**
	 * 已审批通过的邀请单状态1
	 */
	public static String APPROVE_PASSED="1";
	
	/**
	 * 未审批通过的邀请单状态2
	 */
	public static String NOT_APPROVE_PASSED="2";
	private static IMemberService memberService=DqdpAppContext.getSpringContext().getBean("memberService", IMemberService.class);
	private static IContactService contactService=DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
	private final static transient Logger logger = LoggerFactory.getLogger(MemberHandleUtil.class);
	/**
	 * 插入目标通知对象的方法
	 * @return
	 * @throws Exception
	 * @throws BaseException
	 */
	public static void initTargetPerson()throws Exception,BaseException{
		logger.info("初始化目标通知对象开始");
		List<TbQyMemberConfigPO> list=memberService.getAllConfig();
		if(list.size()>0){
			for (TbQyMemberConfigPO config : list) {
				String person=config.getTargetUsers();
				if(StringUtil.isNullEmpty(person)){
					logger.info(config.getName()+"目标通知对象为空");
					continue;
				}
				String[] persons=person.split("\\|");
				//插入可查阅/可编辑
				Map<String, UserRedundancyInfoVO> map=contactService.getUserRedundancyListByUserId(persons);
				insertPerson(config,persons,"0",map);
			}
		}
	}
	
	private static void insertPerson(TbQyMemberConfigPO tbCrmClientTypePO,String[] persons,String userType, Map<String, UserRedundancyInfoVO> map) throws Exception, BaseException {
		if(persons.length>0){
			//查询相关负责人数目
			List<TbQyMemberTargetPersonPO> inlist=new ArrayList<TbQyMemberTargetPersonPO>();
			int temp=1;
			for(String person:persons){
				if (!AssertUtil.isEmpty(person)) {
					if(AssertUtil.isEmpty(map.get(person))){
						continue;
					}
					UserRedundancyInfoVO user=map.get(person);
					TbQyMemberTargetPersonPO po = new TbQyMemberTargetPersonPO();
					po.setId(UUID.randomUUID().toString());
					po.setForeignId(tbCrmClientTypePO.getId());
					po.setType(userType);
					po.setUserId(person.trim());
					po.setPersonName(user.getPersonName());
					po.setHeadPic(user.getHeadPic());
					po.setWxUserId(user.getWxUserId());
					po.setDepartmentName(user.getDeptFullName());
					po.setSortNum(temp);
					po.setOrgId(user.getOrgId());
					po.setCreateTime(new Date());
					//this.insertPO(po, true);
					inlist.add(po);
					temp++;
				}
			}
			if(inlist.size()>0){
				QwtoolUtil.addBatchList(inlist,true);
			}
			logger.info("1043", "插入目标通知对象人数"+inlist.size());
		}
	}
}
