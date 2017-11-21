package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.*;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.service.IMemberService;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.util.StudentUitl;
import cn.com.do1.component.contact.student.vo.TbQyStudentInfoVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.component.wxcgiutil.message.NewsMessageVO;
import cn.com.do1.dqdp.core.DqdpAppContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by liyixin on 2016/11/19.
 */
public class MemberUtil {
    private static IMemberService memberService = DqdpAppContext.getSpringContext().getBean("memberService", IMemberService.class);
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    private static IStudentService studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);
    /**
     * tb_qy_member_config 中的type 启用的状态
     */
    public static final String STATUS = "1" ;
    /**
     * tb_qy_member_config 中的type 不启用的状态
     */
    public static final String NO_STATUS = "0";

    /**
     * tb_qy_member_config_res 中的type
     */
    public static final String RES_TYPE = "0";
    /**
     * tb_qy_member_config 中的request_way 直接邀请
     */
    public static final String REQUEST_WAY_DIRECT = "1";

    /**
     * tb_qy_member_config 中的request_way 审批邀请
     */
    public static final String REQUEST_WAY_CHECK = "2";

    /**
     * tb_qy_member_config 中的is_check 启用的检查
     */
    public static final String IS_CHECK = "1";

    /**
     * tb_qy_member_config 中的is_check 不启用的检查
     */
    public static final String NO_IS_CHECK = "0";

    /**
     * tb_qy_member_config 中的targetType 学生类型
     */
    public static final String TARGER_TYPE_STU = "3";

    /**
     * tb_qy_member_config 中的targetType 教师类型
     */
    public static final String TYPE_TEACHER = "2";

    /**
     * tb_qy_member_config 中的targetType 家长类型
     */
    public static final String TYPE_PARENTS = "1";

    /**
     * tb_qy_member_config is_allow 允许家长填写孩子信息
     */
    public static final String IS_ALLOW = "1";

    /**
     * tb_qy_member_config is_defalut 把微信号手机号作为账号
     */
    public static final String IS_DEFALUT = "1";

    /**
     * tb_qy_member_config show_index 展示在首页
     */
    public static final String SHOW_INDEX = "1";

    /**
     * tb_qy_member_config show_index 不展示在首页
     */
    public static final String NO_SHOW_INDEX = "0";
    /**
     * 无需审批的
     */
    public static final String NO_WAY = "1";
    /**
     * 审批邀请
     */
    public static final String NEED_WAY = "2";

    /**
     * 不通过
     */
    public static final String UN_PASS = "3";
    /**
     * 通过
     */
    public static final String PASS = "2";

    /**
     *	教育版把孩子的信息从json里面拿出来，然后组成list
     *  @param jsonList 前台传来的json数据
     *  @param tbQyMemberInfoPO infoPO
     * @param org 机构信息
     * @param config configPO
     * @throws Exception
     * @throws BaseException
     * @author liyixin
     * @2016-11-23
     * @version 1.0
     */
    public static List<TbQyMemberInfoPO> jsonToList(JSONObject jsonList, UserOrgVO org, TbQyMemberInfoPO tbQyMemberInfoPO, TbQyMemberConfigPO config) throws BaseException, Exception{
        List<TbQyMemberInfoPO> list = new ArrayList<TbQyMemberInfoPO>(jsonList.size());
        for(JSONObject json : (List<JSONObject>)jsonList.getJSONArray("list")){
            TbQyMemberInfoPO infoPO=(TbQyMemberInfoPO) JSONObject.toBean(json, TbQyMemberInfoPO.class);//孩子po
            infoPO.setId(UUID32.getID());
            infoPO.setOrgId(config.getOrgId());
            infoPO.setCreateTime(new Date());
            if(json.has("birthday")) {
                infoPO.setBirthday(json.getString("birthday"));
            }else{
                infoPO.setBirthday("");
            }
            infoPO.setRefId(config.getId());
            infoPO.setConfigName(config.getName());
            infoPO.setType(MemberUtil.TARGER_TYPE_STU);
            infoPO.setParentId(tbQyMemberInfoPO.getId());//设置父亲的po
            if(MemberUtil.REQUEST_WAY_DIRECT.equals(config.getRequestWay())){
                infoPO.setStatus(MemberUtil.STATUS);
                infoPO.setApprovePerson(org.getPersonName());
                infoPO.setApproveUserId(org.getUserName());
                infoPO.setApproveTime(new Date());
            }else if(MemberUtil.REQUEST_WAY_CHECK.equals(config.getRequestWay())){
                infoPO.setStatus(MemberUtil.NO_STATUS);
            }
            list.add(infoPO);
        }
        return list;
    }

    /**
     * 把状态放入vo里
     * @param tbQyMemberInfoVO 这是一个vo
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-23
     * @version 1.0
     */
    public static void addTypeToVO(TbQyMemberInfoVO tbQyMemberInfoVO) throws BaseException, Exception{
        String status=tbQyMemberInfoVO.getStatus();
        if("0".equals(status)){
            tbQyMemberInfoVO.setStatusName("待审批");
        }else if("1".equals(status)){
            tbQyMemberInfoVO.setStatusName("已通过");
        }else{
            tbQyMemberInfoVO.setStatusName("未通过");
        }
        String type = tbQyMemberInfoVO.getType();
        if(!AssertUtil.isEmpty(tbQyMemberInfoVO.getType())){
            if(MemberUtil.TYPE_PARENTS.equals(type)){
                tbQyMemberInfoVO.setType("家长");
            }else if(MemberUtil.TYPE_TEACHER.equals(type)){
                tbQyMemberInfoVO.setType("教师/职工");
            }else if(MemberUtil.TARGER_TYPE_STU.equals(type)){
                tbQyMemberInfoVO.setType("学生");
            }
        }
    }

    /**
     *更新po状态
     * @param po 这是一个po
     * @param org 机构org
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-23
     * @version 1.0
     */
    public static void updateStatus(TbQyMemberInfoPO po, UserOrgVO org) throws BaseException, Exception{
        po.setStatus("1");
        po.setApprovePerson(org.getPersonName());
        po.setApproveUserId(org.getUserId());
        po.setApproveTime(new Date());
    }

    /**
     * 发送的对象
     * @param list 通知对象列表
     * @return
     * @throws BaseException
     * @throws Exception
     */
    public static StringBuffer toSend(List<TbQyMemberTargetPersonPO> list) throws BaseException, Exception{
        StringBuffer send = new StringBuffer();//已发送人员记录，避免负责人相关人创建人有重复
        String str="";
        if (!AssertUtil.isEmpty(list) && list.size()>0) {//通知对象大于0
            for (TbQyMemberTargetPersonPO toPersonId : list) {
                str+=","+toPersonId.getUserId();
            }
            String[] userIds=str.split(",");
            Map<String, UserRedundancyInfoVO> map=contactService.getUserRedundancyListByUserId(userIds);
            for (int i = 0; i < userIds.length; i++) {
                if(map.containsKey(userIds[i])){
                    send.append("|"+map.get(userIds[i]).getWxUserId());
                }
            }
        }
        return send;
    }

    /**
     * 发送消息
     * @param list 通知列表
     * @param orgPO 机构po
     * @param tbQyMemberInfoPO 外部邀请对象po
     * @param requestWay 请求方式
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-5
     * @version 1.0
     */
    public static void sendMsgEdu(List<TbQyMemberTargetPersonPO> list, ExtOrgPO orgPO,
                        TbQyMemberInfoPO tbQyMemberInfoPO, String requestWay) throws BaseException, Exception {
        StringBuffer send = MemberUtil.toSend(list);
        if (send.length()>0) {
            send.deleteCharAt(0);
            String content = "";
            String title="";
            if(MemberUtil.REQUEST_WAY_DIRECT.equals(requestWay)){
                title=tbQyMemberInfoPO.getPersonName()+" 加入了企业微信";
                content="["+tbQyMemberInfoPO.getPersonName()+"]通过外部邀请功能，直接加入了企业微信，点击查看详情。";
            }else{
                title=tbQyMemberInfoPO.getPersonName()+" 申请加入企业微信";
                content="["+tbQyMemberInfoPO.getPersonName()+"]提交了申请单，点击查看或审批。";
            }
            NewsMessageVO newsMessageVO = WxMessageUtil.newsMessageVO.clone();
            newsMessageVO.setTouser(send.toString());
            newsMessageVO.setDuration("0");
            newsMessageVO.setTitle(title);
            newsMessageVO.setDescription(content);
            newsMessageVO.setUrl(Configuration.WX_PORT+ "/jsp/wap/member/edu_member_edit.jsp?id="+ tbQyMemberInfoPO.getId());
            newsMessageVO.setCorpId(orgPO.getCorpId());
            newsMessageVO.setAgentCode(WxAgentUtil.getAddressBookCode());
            newsMessageVO.setOrgId(orgPO.getOrganizationId());
            WxMessageUtil.sendNewsMessage(newsMessageVO);
        }
    }

    /**
     * 把更新的jsonlist放入到数据库查询出来的list里面
     * @param jsonArray 前台传来的json列表
     * @param po 监护人po
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-6
     * @version 1.0
     */
    public static  List<TbQyMemberInfoPO> updateJsonToList(JSONArray jsonArray,TbQyMemberInfoPO po ) throws BaseException, Exception{
      TbQyMemberChildrenVO[] childrenVOs = (TbQyMemberChildrenVO[]) JSONArray.toArray(jsonArray, TbQyMemberChildrenVO.class);
      List<TbQyMemberChildrenVO> updateList = Arrays.asList(childrenVOs);
      List<TbQyMemberInfoPO> oldList =  memberService.searchChildren(po.getId());
      if(updateList.size() != oldList.size()){//如果前台传来的list与查询出来的list不相同
          throw new Exception();
      }
      for(TbQyMemberInfoPO oldPO : oldList){
            for(TbQyMemberChildrenVO updatePO : updateList){
                if(updatePO.getId().equals(oldPO.getId())){//如果这2个po是同一个po，把更新的数据放入到老的里面去
                    oldPO.setPersonName(updatePO.getPersonName());
                    oldPO.setEnrolTel(updatePO.getEnrolTel());
                    oldPO.setSex(updatePO.getSex());
                    oldPO.setRelation(updatePO.getRelation());
                    oldPO.setIdentity(updatePO.getIdentity());
                    oldPO.setBirthday(updatePO.getBirthday());
                    oldPO.setMobile(updatePO.getMobile());
                    oldPO.setWeixinNum(updatePO.getWeixinNum());
                    oldPO.setEmail(updatePO.getEmail());
                    oldPO.setSelectDeptId(updatePO.getSelectDeptId());
                    oldPO.setSelectDept(updatePO.getSelectDept());
                }
            }
      }
      return oldList;
    }

    /**
     * 把信息放入孩子里面
     * @param student 要放进数据库的学生po
     * @param refPO 要放进数据库的关联po
     * @param po
     * @param org 机构信息
     * @param infoVOs
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-13
     * @version 1.0
     */
    public static void setInfoToStu(TbQyStudentInfoPO student, TbQyUserStudentRefPO refPO, TbQyMemberInfoPO po, UserOrgVO org, List<TbQyStudentInfoVO> infoVOs) throws BaseException, Exception{
        student.setClassId(po.getSelectDeptId());
        student.setSex(po.getSex());
        student.setMobile(po.getMobile());
        student.setBirthday(po.getBirthday());
        student.setIdentity(po.getIdentity());
        student.setWeixinNum(po.getWeixinNum());
        if(!AssertUtil.isEmpty(po.getParentId())) {//如果父母id不为空
            TbQyMemberInfoPO infoPO = memberService.searchByParentId(po.getParentId());
            refPO.setId(UUID32.getID());
            if(!AssertUtil.isEmpty(infoPO)) {//如果有这父母的id
                refPO.setUserId(ContactUtil.getUserId(org.getCorpId(), infoPO.getWxUserId()));
            }
            refPO.setOrgId(po.getOrgId());
            refPO.setRelation(po.getRelation());
        }
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("personName", po.getPersonName());
        map.put("classId", po.getSelectDeptId());
        map.put("orgId", po.getOrgId());
        map.put("registerPhone", po.getEnrolTel());
        infoVOs = studentService.getExistStudents(map);
        if(infoVOs.size() == 1){//如果原来有这个学生
            student.setId(infoVOs.get(0).getId());
            refPO.setStudentId(infoVOs.get(0).getId());
        }else{
            student.setId(UUID32.getID());
            student.setOrgId(po.getOrgId());
            student.setCreateTime(new Date());
            student.setIsSyn(0);
            student.setRegisterPhone(po.getEnrolTel());
            student.setPersonName(po.getPersonName());
            refPO.setStudentId(student.getId());
        }
        student.setHasParent(StudentUitl.has_parent);
        if(!AssertUtil.isEmpty(student.getPersonName())) {
            student.setPinyin(PingYinUtil.getPingYin(student.getPersonName()));
        }
    }

    /**
     * 把更新的数据放入po里面
     * @param po
     * @param tbQyMemberInfoPO
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-15
     * @version 1.0
     */
    public static void setUpdateToPO(TbQyMemberInfoPO po, TbQyMemberInfoPO tbQyMemberInfoPO, String deptId, String deptName) throws BaseException, Exception{
        po.setCompanyName(tbQyMemberInfoPO.getCompanyName());
        po.setRemark(tbQyMemberInfoPO.getRemark());
        po.setPersonName(tbQyMemberInfoPO.getPersonName());
        po.setSex(tbQyMemberInfoPO.getSex());
        po.setWeixinNum(tbQyMemberInfoPO.getWeixinNum());
        po.setMobile(tbQyMemberInfoPO.getMobile());
        po.setEmail(tbQyMemberInfoPO.getEmail());
        po.setIdentity(tbQyMemberInfoPO.getIdentity());
        po.setWxUserId(tbQyMemberInfoPO.getWxUserId());
        po.setNickName(tbQyMemberInfoPO.getNickName());
        po.setPosition(tbQyMemberInfoPO.getPosition());
        po.setQqNum(tbQyMemberInfoPO.getQqNum());
        po.setPhone(tbQyMemberInfoPO.getPhone());
        po.setShorMobile(tbQyMemberInfoPO.getShorMobile());
        po.setAddress(tbQyMemberInfoPO.getAddress());
        po.setLunarCalendar(tbQyMemberInfoPO.getLunarCalendar());
        po.setBirthday(tbQyMemberInfoPO.getBirthday());
        po.setSelectDeptId(deptId);
        po.setSelectDept(deptName);
        po.setEnrolTel(tbQyMemberInfoPO.getEnrolTel());
    }

    /**
     *显示在首页的邀请单的缓存
     * @param orgVO
     * @param po
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-28
     * @version 1.0
     */
    public static void addMemberCache(UserOrgVO orgVO, TbQyMemberConfigPO po) throws BaseException, Exception{
        if(MemberUtil.SHOW_INDEX.equals(po.getShowIndex())) {//如果是显示在首页的单发生了变化
            List<TbQyMemberConfigVO> configVOs = new ArrayList<TbQyMemberConfigVO>(1);
            TbQyMemberConfigVO vo = new TbQyMemberConfigVO();
            BeanHelper.copyBeanProperties(vo, po);
            configVOs.add(vo);
            CacheWxqyhObject.set("member", orgVO.getOrgId(), "configVOs", configVOs);
        }else {
            return;
        }
    }

    /**
     * 设置用户类型
     * @param userInfo
     * @param tbQyMemberInfoPO
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-28
     * @version 1.0
     */
    public static void setAttributeToUser(TbQyUserInfoPO userInfo,TbQyMemberInfoPO tbQyMemberInfoPO) throws BaseException, Exception{
        if(MemberUtil.TYPE_PARENTS.equals(tbQyMemberInfoPO.getType()) ){//如果是家长类型
            userInfo.setAttribute(1);
        }else if(MemberUtil.TYPE_TEACHER.equals(tbQyMemberInfoPO.getType())){
            userInfo.setAttribute(2);
        }else{
            userInfo.setAttribute(3);
        }
    }

    /**
     * 检查po状态
     * @param po
     * @throws BaseException
     * @throws Exception
     * @author liyixin
     * @2016-12-6
     * @version 1.0
     */
    public static void checkStatus(TbQyMemberInfoPO po) throws  BaseException, Exception{
        if(!StringUtil.isNullEmpty(po.getStatus())){
            if(MemberHandleUtil.APPROVE_PASSED.equals(po.getStatus())){
                throw new BaseException("2000", "该邀请人员已被"+po.getApprovePerson()+"于"+ DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss")+"审批通过！");
            }else if(MemberHandleUtil.NOT_APPROVE_PASSED.equals(po.getStatus())){
                throw new BaseException("2001", "该邀请人员已被"+po.getApprovePerson()+"于"+DateUtil.formatCurrent("yyyy-MM-dd HH:mm:ss")+"审批不通过！");
            }
        }
    }

    /**
     *  把外部人员填写的自定义字段信息，填写到用户的自定义字段列表中
     * @param customPOList 外部用户的自定义字段
     * @param userInfo 用户信息
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-4-14
     * @version 1.0
     */
    public static List<TbQyUserCustomItemPO> setMemberToCustom(List<TbQyMemberUserCustomPO> customPOList, TbQyUserInfoPO userInfo) throws BaseException, Exception{
        List<TbQyUserCustomItemPO> list = new ArrayList<TbQyUserCustomItemPO>();
        if(!AssertUtil.isEmpty(customPOList)){
            TbQyUserCustomItemPO itemPO;
            for(TbQyMemberUserCustomPO customPO : customPOList){
                itemPO = new TbQyUserCustomItemPO();
                itemPO.setId(UUID32.getID());
                itemPO.setContent(customPO.getContent());
                itemPO.setCreateTime(new Date());
                itemPO.setOrgId(customPO.getOrgId());
                itemPO.setOptionId(customPO.getCustomId());
                itemPO.setUserId(userInfo.getUserId());
                list.add(itemPO);
            }
        }
        return list;
    }

    /**
     *批量新增自定义字段
     * @param customPOList 需要批量新增的自定义字段list
     * @param customUserList 前台传来的自定义字段的数据
     * @param config 设置表
     * @param tbQyMemberInfoPO 用户填写的表
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-4-26
     * @version 1.0
     */
    public static void batchAddCustom(List<TbQyMemberUserCustomPO> customPOList, String customUserList, TbQyMemberConfigPO config, TbQyMemberInfoPO tbQyMemberInfoPO) throws BaseException, Exception{
        //如果从页面传来的自定义字段不为空,而且是金卡vip用户
        if(!AssertUtil.isEmpty(customUserList) && VipUtil.hasGoldPermission(config.getOrgId(), VipUtil.INTERFACE_CODE_ADDRESSBOOK)){
            JSONArray jsonArray = JSONArray.fromObject(customUserList);
            TbQyMemberUserCustomPO[] customJspVOs = (TbQyMemberUserCustomPO[]) JSONArray.toArray(jsonArray, TbQyMemberUserCustomPO.class);
            customPOList.addAll(Arrays.asList(customJspVOs));
            //遍历传来的customPOList
            for(TbQyMemberUserCustomPO customPO : customPOList){
                customPO.setId(UUID32.getID());
                customPO.setMemberInfoId(tbQyMemberInfoPO.getId());
                customPO.setOrgId(config.getOrgId());
            }
            QwtoolUtil.addBatchList(customPOList, true);
        }
    }
}
