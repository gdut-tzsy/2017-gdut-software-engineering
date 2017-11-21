package cn.com.do1.component.contact.student.thread;


import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.ExtOrgPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomItemPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.*;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.vo.DeptSyncInfoVO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.thread.BatchImportThread;
import cn.com.do1.component.contact.contact.util.*;
import cn.com.do1.component.contact.department.util.DepartmentUtil;
import cn.com.do1.component.contact.post.service.IPostService;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.contact.student.util.StudentReportUtil;
import cn.com.do1.component.contact.student.util.StudentUitl;
import cn.com.do1.component.contact.student.vo.ImportErrorStudentVO;
import cn.com.do1.component.contact.student.vo.ImportStudentVO;
import cn.com.do1.component.contact.student.vo.ResultVO;
import cn.com.do1.component.contact.student.vo.TbQyStudentInfoVO;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.trade.model.VipUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Created by hejinjiao on 2016/11/21.
 */
public class BatchImportStudentThread implements Runnable {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory
            .getLogger(BatchImportStudentThread.class);
    /**
     * The Id.
     */
    private String id;
    /**
     * The Org.
     */
    private UserOrgVO org;
    /**
     * The Up file.
     */
    private File upFile;
    /**
     * 新增家长list
     */
    private List<TbQyUserInfoPO> addUserList = new ArrayList<TbQyUserInfoPO>(100);
    /**
     * The Update user list.
     */
    private List<TbQyUserInfoPO> updateUserList = new ArrayList<TbQyUserInfoPO>(100);
    /**
     * The User ref dept map.
     */
    private Map<String, List<String>> userRefDeptMap = new HashMap<String, List<String>>(100);
    /**
     * The Add dept ref user map.
     */
    private Map<String, List<String>> addDeptRefUserMap = null;
    /**
     * The Del dept ref user map.
     */
    private Map<String, List<String>> delDeptRefUserMap = null;
    /**
     * 需要新增的部门list
     */
    private List<DeptSyncInfoVO> addDeptList = null;
    /**
     * The List.
     */
    private List<ImportStudentVO> list;
    /**
     * 微信部门缓存数据
     */
    private List<WxDept> wxDeptList;
    /**
     * 微信部门缓存数据
     */
    private Map<String, WxDept> wxDeptMap = new HashMap<String, WxDept>(10);
    /**
     * 微信部门缓存数据
     */
    private Map<String, WxDept> wxDeptMapId = new HashMap<String, WxDept>(10);
    /**
     * 本地部门缓存数据 key:departFullName
     */
    private Map<String, TbDepartmentInfoPO> deparmentMap = new HashMap<String, TbDepartmentInfoPO>(10);
    /**
     * 本地部门缓存数据 key:departId
     */
    private Map<String, TbDepartmentInfoPO> deparmentIdMap = new HashMap<String, TbDepartmentInfoPO>(10);
    /**
     * 本地班级信息缓存
     */
    private Map<String, TbDepartmentInfoEduPO> classMap = new HashMap<String, TbDepartmentInfoEduPO>(10);

    /**
     * The Student service.
     */
    private IStudentService studentService;

    /**
     * The Contact service.
     */
    private IContactService contactService;

    /**
     * The Contact custom mgr service.
     */
    private IContactCustomMgrService contactCustomMgrService;

    /**
     * The Department service.
     */
    private IDepartmentService departmentService;

    /**
     * The Option v os.
     */
    private List<TbQyUserCustomOptionVO> optionVOs;
    /**
     *
     */
    private IPostService postService;

    /**
     * 构造方法
     *
     * @param id        the id
     * @param org       the org
     * @param list      the list
     * @param upFile    the up file
     * @param optionVOs the option v os
     */
    public BatchImportStudentThread(String id, UserOrgVO org, List<ImportStudentVO> list, File upFile, List<TbQyUserCustomOptionVO> optionVOs) {
        this.id = id;
        this.org = org;
        this.optionVOs = optionVOs;
        this.list = list;
        this.upFile = upFile;
        this.studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);
        this.departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
        this.contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
        this.contactCustomMgrService = DqdpAppContext.getSpringContext().getBean("contactCustomService", IContactCustomMgrService.class);
        this.postService = DqdpAppContext.getSpringContext().getBean("postService", IPostService.class);
    }


    @Override
    public void run() {
        logger.debug("导入学生信息开始,orgId:" + org.getOrgId() + ",corpId：" + org.getCorpId());
        Date date = new Date();
        ResultVO resultvo = (ResultVO) ImportResultUtil.getErrorObject(id);
        if (null == resultvo) {
            resultvo = new ResultVO();
            ImportResultUtil.putErrorObject(id, resultvo);
        }
        Map<String, TbQyStudentInfoVO> stuMap = new HashMap<String, TbQyStudentInfoVO>();
        Map<String, String> parMap = new HashMap<String, String>();
        int listNum = 1;//行数
        TbQyStudentInfoPO po;
        ImportErrorStudentVO error;
        String wxOrgId = null;
        try {
            Set<String> positionSet = postService.getPositionSetByOrgId(org.getOrgId());
            List<TbQyStudentInfoPO> addlist = new ArrayList<TbQyStudentInfoPO>();
            List<TbQyStudentInfoPO> uplist = new ArrayList<TbQyStudentInfoPO>();
            List<TbQyUserStudentRefPO> reflist = new ArrayList<TbQyUserStudentRefPO>();
            List<String> upIds = new ArrayList<String>();
            List<ImportErrorStudentVO> errorlist = resultvo.getErrorList();
            if (null == errorlist) {
                errorlist = new ArrayList<ImportErrorStudentVO>();
                resultvo.setErrorList(errorlist);
            }
            if (list == null || list.size() == 0) {
                resultvo.setTotalNum(0);
            } else {
                resultvo.setTotalNum(list.size());
                getHistoryMap(stuMap, parMap, list);
                wxOrgId = getWxOrgId();
                getClassMap();
                addDeptRefUserMap = new HashMap<String, List<String>>(100);
                delDeptRefUserMap = new HashMap<String, List<String>>(100);
            }
            Date datetime = DateUtil.addSeconds(new Date(), list.size());
            //exlce表学生信息判重
            Map<String, TbQyStudentInfoPO> repeatPOMap = new HashMap<String, TbQyStudentInfoPO>();
            Set<String> repeatIdSet = new HashSet<String>();
            TbQyStudentInfoPO repeatPO = null;
            for (ImportStudentVO stuVO : list) {
                //组装获取到学生信息和家长信息
                stuVO = StudentReportUtil.assembleStudentVO(stuVO);
                error = new ImportErrorStudentVO();
                BeanHelper.copyFormatProperties(error, stuVO, true);

                // 检查数据
                if (checkImportStudentVO(stuVO, error, parMap, wxOrgId, positionSet)) {
                    po = new TbQyStudentInfoPO();
                    BeanHelper.copyFormatProperties(po, stuVO, true);
                    po.setPinyin(PingYinUtil.getPingYin(po.getPersonName()));
                    po.setOrgId(org.getOrgId());
                    if (!AssertUtil.isEmpty(stuVO.getSexDesc())) {
                        po.setSex("女".equals(stuVO.getSexDesc().trim()) ? "2" : "1");
                    }
                    if (!AssertUtil.isEmpty(stuVO.getHasTeacher())) {
                        po.setHasTeacher("是".equals(stuVO.getHasTeacher().trim()) ? StudentUitl.has_teacher : StudentUitl.no_teacher);
                    } else {
                        po.setHasTeacher(StudentUitl.no_teacher);
                    }
                    //获取班级部门Id
                    TbDepartmentInfoPO deptPO = new TbDepartmentInfoPO();
                    String msg = doLocalDeptEdu(deptPO, stuVO.getClassName(), wxOrgId, true);
                    if(msg != null){
                        error.setErrMsg(msg);
                        errorlist.add(error);
                        resultvo.setErrorNum(errorlist.size());
                        continue;
                    }
                    po.setClassId(deptPO.getId());
                    po.setCreator(org.getUserName());
                    Date createTime = DateUtil.addSeconds(datetime, -listNum);
                    po.setCreateTime(createTime);
                    //排除excel表学生信息重复
                    if (repeatPOMap.containsKey(po.getClassId() + "_" + po.getRegisterPhone() + "_" + po.getPersonName())) {
                        repeatPO = repeatPOMap.get(po.getClassId() + "_" + po.getRegisterPhone() + "_" + po.getPersonName());
                        if (addlist.contains(repeatPO)) {
                            addlist.remove(repeatPO);
                        }
                        if (uplist.contains(repeatPO)) {
                            uplist.remove(repeatPO);
                        }
                        if (repeatIdSet.contains(repeatPO.getId())) {
                            reflist = reMoveParents(repeatPO.getId(), reflist);
                        }
                    }
                    //判断学生是否存在
                    if (stuMap.containsKey(po.getClassId() + "_" + po.getRegisterPhone() + "_" + po.getPersonName())) {
                        TbQyStudentInfoVO vo = stuMap.get(po.getClassId() + "_" + po.getRegisterPhone() + "_" + po.getPersonName());
                        po.setId(vo.getId());
                        upIds.add(vo.getId());
                        uplist.add(po);
                    } else {
                        po.setId(UUID32.getID());
                        po.setIsSyn(StudentUitl.no_syn);
                        addlist.add(po);
                    }
                    if (!AssertUtil.isEmpty(stuVO.getParents())) {
                        po.setHasParent(StudentUitl.has_parent);
                        addParentList(stuVO.getParents(), po, reflist, parMap);
                        repeatIdSet.add(po.getId());
                    } else {
                        po.setHasParent(StudentUitl.no_parent);
                    }
                    repeatPOMap.put(po.getClassId() + "_" + po.getRegisterPhone() + "_" + po.getPersonName(), po);
                } else {
                    errorlist.add(error);
                    resultvo.setErrorNum(errorlist.size());
                    continue;
                }
                resultvo.setProcessNum(listNum);
                listNum++;
            }
            //修改
            if (uplist.size() > 0) {
                studentService.updateStudentList(uplist, upIds, org.getOrgId());
            }
            //新增
            if (addlist.size() > 0) {
                studentService.addStudentList(addlist);
            }
            //插入家长/监护人关联表
            if (reflist.size() > 0) {
                studentService.addStudnetRef(reflist);
            }
        } catch (Exception e) {
            logger.error("导入学生信息失败; orgId=" + org.getOrgId());
            ExceptionCenter.addException(e, "导入学生信息失败", "corpId:" + org.getCorpId());
        } catch (BaseException e) {
            logger.error("导入学生信息失败; orgId=" + org.getOrgId());
            ExceptionCenter.addException(e, "导入学生信息失败", "corpId:" + org.getCorpId());
        } finally {
            resultvo.setFinish(true);
            upFile.delete();
            UserInfoChangeNotifier.batchChangeDept(org, addDeptList, null, null, UserInfoChangeInformType.IMPORT_MGR);
            UserInfoChangeNotifier.importEnd(org, addUserList, updateUserList, null, userRefDeptMap, addDeptRefUserMap, delDeptRefUserMap);
            logger.error("机构orgId:" + org.getOrgId() + "导入学生信息完成，用时：" + (new Date().getTime() - date.getTime()) + "ms");
        }
    }

    /**
     * 去除重复学生的家长/监护人信息
     *
     * @param studentId the student id
     * @param reflist   the reflist
     * @return the list
     */
    private List<TbQyUserStudentRefPO> reMoveParents(String studentId, List<TbQyUserStudentRefPO> reflist) {
        List<TbQyUserStudentRefPO> newlist = new ArrayList<TbQyUserStudentRefPO>();
        for (TbQyUserStudentRefPO po : reflist) {
            if (!studentId.equals(po.getStudentId())) {
                newlist.add(po);
            }
        }
        return newlist;
    }

    /**
     * 学生-家长/监护人关系
     */
    public static Map<String, Integer> relationType = new HashMap<String, Integer>();

    static {
        relationType.put("父亲", 0);
        relationType.put("母亲", 1);
        relationType.put("爷爷", 2);
        relationType.put("奶奶", 3);
        relationType.put("外公", 4);
        relationType.put("外婆", 5);
        relationType.put("其他监护人", 6);
    }

    /**
     * 组装家长监护人list
     *
     * @param parents the parents
     * @param po      the po
     * @param reflist the reflist
     * @param parMap  the par map
     */
    private void addParentList(String parents, TbQyStudentInfoPO po, List<TbQyUserStudentRefPO> reflist, Map<String, String> parMap) {
        String[] parentlist = parents.split(";");
        TbQyUserStudentRefPO refPO;
        int sort = 0;
        for (String parent : parentlist) {
            if (!AssertUtil.isEmpty(parent)) {
                String[] user = parent.split(",");
                refPO = new TbQyUserStudentRefPO();
                refPO.setOrgId(org.getOrgId());
                refPO.setId(UUID32.getID());
                refPO.setStudentId(po.getId());
                refPO.setUserId(parMap.get(user[1].trim()));
                refPO.setRelation(relationType.get(user[0]));
                refPO.setSort(sort);
                reflist.add(refPO);
                sort++;
            }
        }
    }

    /**
     * 检查数据
     *
     * @param stuVO   the stu vo
     * @param error   the error
     * @param parMap  @return
     * @param wxOrgId the wx org id
     * @return the boolean
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private boolean checkImportStudentVO(ImportStudentVO stuVO, ImportErrorStudentVO error, Map<String, String> parMap, String wxOrgId, Set<String> positionSet)
            throws Exception, BaseException {
        boolean isTrue = true;
        StringBuffer errMsg = new StringBuffer();
        //姓名
        if (AssertUtil.isEmpty(stuVO.getPersonName())) {
            errMsg.append(",学生姓名不能为空");
            isTrue = false;
        }
        //班级
        if (AssertUtil.isEmpty(stuVO.getClassName())) {
            errMsg.append(",班级不能为空");
            isTrue = false;
        } else if (!SchoolClassUtil.judgementSchoolClass(stuVO.getClassName())) {
            errMsg.append(",班级输入错误");
            isTrue = false;
        }
        //注册登记手机号
        if (AssertUtil.isEmpty(stuVO.getRegisterPhone()) || !stuVO.getRegisterPhone().matches("^\\d{4,20}$")) {
            errMsg.append(",注册登记手机号错误");
            isTrue = false;
        }
        if (!AssertUtil.isEmpty(stuVO.getHasTeacher()) && !"是".equals(stuVO.getHasTeacher()) && !"否".equals(stuVO.getHasTeacher())) {
            errMsg.append(",本教师子女填写错误");
            isTrue = false;
        }
        //性别
        if (!AssertUtil.isEmpty(stuVO.getSexDesc()) && !"女".equals(stuVO.getSexDesc()) && !"男".equals(stuVO.getSexDesc())) {
            errMsg.append(",性别填写错误");
            isTrue = false;
        }
        //身份证
        if (!AssertUtil.isEmpty(stuVO.getIdentity()) && !IdcardUtils.validateCard(stuVO.getIdentity().trim())) {
            errMsg.append(",身份证填写错误");
            isTrue = false;
        }
        //学生手机号
        if (!AssertUtil.isEmpty(stuVO.getMobile()) && !stuVO.getMobile().matches("^\\d{4,20}$")) {
            errMsg.append(",学生手机号错误");
            isTrue = false;
        }
        //电子邮箱
        if (!AssertUtil.isEmpty(stuVO.getEmail()) && !stuVO.getEmail().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*.\\w+([-.]\\w+)*$")) {
            errMsg.append(",电子邮箱错误");
            isTrue = false;
        }
        //家长、监护人
        if (!AssertUtil.isEmpty(stuVO.getParents())) {
            String[] parents = stuVO.getParents().split(";");
            StringBuffer wxUserIds = new StringBuffer();
            StringBuffer relations = new StringBuffer();
            for (String parent : parents) {
                if (!AssertUtil.isEmpty(parent)) {
                    String[] user = parent.split(",");
                    if (user.length == 2) {
                        if (!parMap.containsKey(user[1].trim())) {
                            //导入父母信息不为空
                            if (isTrue && !AssertUtil.isEmpty(stuVO.getParentVO()) && user[1].trim().equals(stuVO.getParentVO().getWxUserId())) {
                                String msg = judgeParent(stuVO.getParentVO(), stuVO.getClassName(), parMap, wxOrgId, positionSet);
                                if (msg != null) {
                                    errMsg.append(",家长/监护人信息填写错误：" + msg);
                                    isTrue = false;
                                    break;
                                }
                            } else {
                                wxUserIds.append("|" + user[1].trim());
                            }
                        }
                        if (!relationType.containsKey(user[0].trim())) {
                            relations.append("|" + user[0].trim());
                        }
                    } else {
                        errMsg.append(",家长/监护人：" + parent + "填写错误");
                        isTrue = false;
                        break;
                    }
                }
            }
            if (wxUserIds.length() > 0) {
                errMsg.append(",不存在家长/监护人账号：" + wxUserIds.deleteCharAt(0).toString());
                isTrue = false;
            }
            if (relations.length() > 0) {
                errMsg.append(",不存在家长/监护人关系：" + relations.deleteCharAt(0).toString());
                isTrue = false;
            }
        }
        if (!isTrue) {
            error.setErrMsg(errMsg.deleteCharAt(0).toString());
        }
        return isTrue;
    }

    /**
     * 判断父母信息
     *
     * @param vo      the vo
     * @param parMap  the par map
     * @param wxOrgId the wx org id
     * @param className 学生的班级部门
     * @return string string
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private String judgeParent(ImportVO vo, String className, Map<String, String> parMap, String wxOrgId, Set<String> positionSet) throws Exception, BaseException {
        List<TbQyUserInfoPO> tempUserList = contactService.findRepeatUsersByCorpId(org.getCorpId(), vo.getWxUserId(), null, null, null);
        if (tempUserList == null || tempUserList.size() == 0) {
            if (AssertUtil.isEmpty(vo.getDeptFullName())) {
                vo.setDeptFullName(className);
            }
            String msg = ContactImportUtil.verifyUserInfo(org.getCorpId(), org.getOrgId(), vo, positionSet);
            if (msg != null) {
                return msg;
            } else {
                msg = insertParent(vo, parMap, wxOrgId);
                if(msg != null){
                    return msg;
                }
            }
        } else if (tempUserList.size() > 1) {
            return "账号存在异常";
        } else {
            if (!tempUserList.get(0).getOrgId().equals(org.getOrgId())) {
                return "账号已存在";
            }
            if ("-1".equals(tempUserList.get(0).getUserStatus())) {//离职
                return "用户已离职";
            }
            parMap.put(tempUserList.get(0).getWxUserId(), tempUserList.get(0).getUserId());
        }
        return null;
    }

    /**
     * 插入父母信息
     *
     * @param vo      the vo
     * @param parMap  the par map
     * @param wxOrgId the wx org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private String insertParent(ImportVO vo, Map<String, String> parMap, String wxOrgId) throws Exception, BaseException {
        TbQyUserInfoPO userPO = new TbQyUserInfoPO();
        // 插入用户
        userPO.setId(ContactUtil.getUserId(org.getCorpId(), vo.getWxUserId()));
        userPO.setUserId(userPO.getId());
        String department = vo.getDeptFullName().replace("；", ";");
        String[] split = department.split(";");
        int length = split.length;
        TbDepartmentInfoPO deptPO;
        List<String> d = new ArrayList<String>(length);
        List<String> detId = new ArrayList<String>(length);
        // 处理部门，不存在的部门自动新建
        for (int i = 0; i < length; i++) {
            deptPO = new TbDepartmentInfoPO();
            String msg = doLocalDeptEdu(deptPO, split[i].trim(), wxOrgId, false); //doLocalDept(split[i].trim(),wxOrgDeptId);
            if(msg != null){
                return msg;
            }
            d.add(deptPO.getWxId());
            detId.add(deptPO.getId());
        }
        // 先同步用户到微信
        WxUser user = new WxUser();
        user.setDepartment(d);
        user.setUserid(vo.getWxUserId());
        user.setName(vo.getPersonName().trim());
        user.setEmail(vo.getEmail());
        user.setMobile(vo.getMobile());
        user.setPosition(vo.getPosition());
        user.setWeixinid(vo.getWeixinNum());
        WxUserService.addUser(user, org.getCorpId(), org.getOrgId());
        userPO.setOrgId(org.getOrgId());
        userPO.setPersonName(vo.getPersonName().trim());
        userPO.setPinyin(PingYinUtil.getPingYin(userPO.getPersonName()));
        userPO.setEmail(vo.getEmail());
        userPO.setMobile(vo.getMobile());
        userPO.setShorMobile(vo.getShorMobile());
        userPO.setWeixinNum(vo.getWeixinNum());
        userPO.setQqNum(vo.getQqNum());
        userPO.setPosition(vo.getPosition());
        userPO.setBirthday(vo.getBirthday());
        userPO.setAddress(vo.getAddress());
        userPO.setMark(vo.getMark());
        userPO.setNickName(vo.getNickName());
        userPO.setPhone(vo.getPhone());
        userPO.setUserStatus("0");
        userPO.setIsConcerned("0");
        userPO.setCreatePerson(org.getUserName());
        userPO.setCreateTime(new Date());
        userPO.setCorpId(org.getCorpId());
        userPO.setWxUserId(vo.getWxUserId());
        userPO.setHeadPic("0");
        userPO.setLunarCalendar(vo.getLunarCalendar());
        userPO.setAttribute(ContactImportUtil.getUesrAttribute(vo.getAttribute()));
        boolean isVip = VipUtil.isQwVip(org.getOrgId());
        if (isVip) {//如果是vip
            if (AssertUtil.isEmpty(vo.getIsTop())) {//如果没有填排序值
                userPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
            } else {
                userPO.setIsTop(vo.getIsTop());
            }
            //是否保密
            ContactImportUtil.saveSecrecy(vo.getSecrecy(), org.getOrgId(), userPO.getUserId());
        } else {//如果不是vip
            userPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
        }

        userPO.setSex(ContactImportUtil.getUserSex(vo.getSex()));
        //新增的人员默认按阳历发送生日提醒（马权阳 2015-06-01）
//		userPO.setRemindType(ContactUtil.REMIND_TYPE_ONE);
        //加入入职时间  maquanyang 2015-6-9
        userPO.setEntryTime(vo.getEntryTime());
        //修改生日提醒  maquanyang 2015-6-17
        if (!StringUtil.isNullEmpty(vo.getRemindType())) {
            userPO.setRemindType("按农历".equals(vo.getRemindType().replaceAll("\\s*", "")) ? ContactUtil.REMIND_TYPE_ZERO : ContactUtil.REMIND_TYPE_ONE);
        } else {
            userPO.setRemindType(ContactUtil.REMIND_TYPE_ONE);
        }
        //lishegntao 2015-9-6 增加证件类型的导入
        userPO.setCertificateType(vo.getCertificateType());
        userPO.setCertificateContent(vo.getCertificateContent());
        userPO.setIdentity(vo.getIdentity());

        contactService.insertUser(userPO, detId);
        List<TbQyUserCustomItemPO> itemPOs = new ArrayList<TbQyUserCustomItemPO>();
        ContactCustomUtil.setUserToItem(vo, userPO, itemPOs, optionVOs);
        contactCustomMgrService.batchAddItem(itemPOs);
        parMap.put(userPO.getWxUserId(), userPO.getUserId());
        addUserList.add(userPO);
        userRefDeptMap.put(userPO.getUserId(), detId);
        UserInfoChangeNotifier.getDeptUserRef(detId, userPO.getUserId(), addDeptRefUserMap);
        return null;
    }

    /**
     * 查询学生map、家长map
     *
     * @param stuMap the stu map
     * @param parMap the par map
     * @param list   the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void getHistoryMap(Map<String, TbQyStudentInfoVO> stuMap, Map<String, String> parMap, List<ImportStudentVO> list)
            throws Exception, BaseException {
        // 家长/监护人账号
        List<String> wxUserId = new ArrayList<String>();
        //学生名字
        List<String> stuNames = new ArrayList<String>(list.size());
        for (ImportStudentVO vo : list) {
            if (AssertUtil.isEmpty(vo.getPersonName())) {
                continue;
            }
            vo.setPersonName(vo.getPersonName().trim());
            if (!stuNames.contains(vo.getPersonName())) {
                stuNames.add(vo.getPersonName());
            }
            if (!AssertUtil.isEmpty(vo.getParents())) {
                String[] parents = vo.getParents().split(";");
                for (String user : parents) {
                    if (!AssertUtil.isEmpty(user)) {
                        String[] parent = user.split(",");
                        if (parent.length == 2) {
                            wxUserId.add(parent[1].trim());
                        }
                    }
                }
            }
        }
        if (stuNames.size() > 0) {
            getStudentMap(stuNames, stuMap);
        }
        if (wxUserId.size() > 0) {
            getParentMap(wxUserId, parMap);
        }
    }

    /**
     * 学生map
     *
     * @param stuNames the stu names
     * @param stuMap   the stu map
     * @return
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void getStudentMap(List<String> stuNames, Map<String, TbQyStudentInfoVO> stuMap)
            throws Exception, BaseException {
        List<TbQyStudentInfoVO> list = studentService.findStudentByNames(stuNames, org.getOrgId());
        if (list.size() > 0) {
            for (TbQyStudentInfoVO infoVo : list) {
                //stuMap 的key为：班级Id_注册手机号_学生姓名
                stuMap.put(infoVo.getClassId() + "_" + infoVo.getRegisterPhone() + "_" + infoVo.getPersonName(), infoVo);
            }
        }
    }

    /**
     * 家长/监护人Map
     *
     * @param wxUserId the wx user id
     * @param parMap   the par map
     * @return
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void getParentMap(List<String> wxUserId, Map<String, String> parMap) throws Exception, BaseException {
        List<TbQyUserInfoVO> users = studentService.findUsersByWxUserIds(org.getOrgId(), wxUserId);
        if (users.size() > 0) {
            for (TbQyUserInfoVO infoVO : users) {
                parMap.put(infoVO.getWxUserId(), infoVO.getUserId());
            }
        }
    }

    /**
     * 部门map.
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public void getClassMap() throws Exception, BaseException {
        // 获取本机构部门数据
        List<TbDepartmentInfoPO> localDeptList = departmentService.getAllDepart(org.getOrgId());
        //组装班级部门信息
        localDeptList = SchoolClassUtil.assembleDepartmentEdu(localDeptList, deparmentIdMap);
        for (TbDepartmentInfoPO po : localDeptList) {
            deparmentMap.put(po.getDeptFullName(), po);
            deparmentIdMap.put(po.getId(), po);
        }
        classMap = SchoolClassUtil.getClassMapByOrgId(org.getOrgId());
    }

    /**
     * 获取机构wxId
     *
     * @return wx org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private String getWxOrgId() throws Exception, BaseException {
        ExtOrgPO orgPO = studentService.searchByPk(ExtOrgPO.class, org.getOrgId());
        //如果是道一企业微信，不能获取所有机构，防止部门过多导致内存溢出
        if (!org.getCorpId().equals(Configuration.AUTO_CORPID)) {
            wxDeptList = WxDeptService.getDept(org.getCorpId(), orgPO.getWxId(), org.getOrgId(), WxAgentUtil.getAddressBookCode());
            if (AssertUtil.isEmpty(wxDeptList)) {
                WxDept wd = new WxDept();
                wd.setName("企业微信");
                wd.setParentid("0");
                wd = WxDeptService.addDept(wd, org.getCorpId(), org.getOrgId());
                wxDeptList = new ArrayList<WxDept>();
                wxDeptList.add(wd);
            }
            BatchImportThread c = new BatchImportThread(org.getUserName(), org.getOrgId(), org.getCorpId());
            c.setWxDeptList(wxDeptList);
            return c.iterationAddDept(org.getOrgId());
        } else {
            return orgPO.getWxId();
        }
    }

    /**
     * 查询部门是否存在，不存在则新增部门
     *
     * @param deptPO
     * @param className   the class name
     * @param wxOrgDeptId the wx org dept id
     * @param isStudent   the is student
     * @return string string
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private String doLocalDeptEdu(TbDepartmentInfoPO deptPO, String className, String wxOrgDeptId, boolean isStudent) throws Exception, BaseException {
        //班级部门是否存在
        if (deparmentMap.containsKey(className)) {
            if (!isStudent) {
                //return deparmentMap.get(className);
                BeanHelper.copyFormatProperties(deptPO, deparmentMap.get(className), true);
                return null;
            } else if (!AssertUtil.isEmpty(deparmentMap.get(className).getAttribute())
                    && DepartmentUtil.ATTRIBUTE_TEACHER == deparmentMap.get(className).getAttribute()) {
                //return deparmentMap.get(className);
                BeanHelper.copyFormatProperties(deptPO, deparmentMap.get(className), true);
                return null;
            }
        }
        boolean isSchoolClass = SchoolClassUtil.judgementSchoolClass(className);
        String[] deptList = className.split("->");
        TbDepartmentInfoPO parentDept = new TbDepartmentInfoPO();
        parentDept.setWxId(wxOrgDeptId);
        String fullName = "";
        String parentId = "";
        TbDepartmentInfoPO dept = null;
        WxDept wxdept = null;
        //查询多级部门，不存在则新增部门
        for (int i = 0; i < deptList.length; i++) {
            if (i == 0) {
                fullName = deptList[i];
            } else {
                fullName += "->" + deptList[i];
            }
            //部门存在
            if (deparmentMap.containsKey(fullName)) {
                dept = deparmentMap.get(fullName);
            } else {
                //部门不存在
                if (StringVerifyUtil.verifyRules(deptList[i])) {
                    return "部门名称不能包括：" + StringVerifyUtil.getVerifyMatche();
                    //throw new BaseException("机构orgId=" + org.getOrgId() + "部门名称不能包括：" + StringVerifyUtil.getVerifyMatche());
                }
                dept = new TbDepartmentInfoPO();
                dept.setId(UUID32.getID());
                dept.setDepartmentName(deptList[i]);
                dept.setDeptFullName(fullName);
                dept.setParentDepart(parentId);
                dept.setOrgId(org.getOrgId());
                dept.setCreatePerson(org.getUserName());
                dept.setCreateTime(new Date());
                dept.setShowOrder(0);
                dept.setWxParentid(parentDept.getWxId());
                //部门权限
                if (AssertUtil.isEmpty(dept.getPermission())) {
                    dept.setPermission(parentDept.getPermission());
                } else {
                    if (!AssertUtil.isEmpty(parentDept.getPermission())) {
                        if (dept.getPermission().compareTo(parentDept.getPermission()) < 0) {//子部门权限不能比父部门权限大
                            dept.setPermission(parentDept.getPermission());
                        }
                    }
                }
                //新增部门
                deparmentMap.put(dept.getDeptFullName(), dept);
                deparmentIdMap.put(dept.getId(), dept);
                //新建部门为班级部门则最低级部门为班级部门
                if (isSchoolClass && i == deptList.length - 1) {
                    dept.setAttribute(1);
                    //新增班级部门信息
                    String msg = SchoolClassUtil.addDeptEdu(dept, classMap, deparmentIdMap);
                    if(msg != null){
                        return msg;
                    }
                } else {
                    dept.setAttribute(0);
                }
                wxdept = doWxDept(dept);
                dept.setWxId(wxdept.getId());
                dept.setId(DepartmentUtil.getDeptId(org.getCorpId(), wxdept.getId()));
                departmentService.insertPO(dept, false);
                if (addDeptList == null) {
                    addDeptList = new ArrayList<DeptSyncInfoVO>(10);
                }
                DeptSyncInfoVO vo = new DeptSyncInfoVO();
                BeanHelper.copyBeanProperties(vo, dept);
                addDeptList.add(vo);
            }
            parentId = dept.getId();
            parentDept = dept;
        }
        BeanHelper.copyFormatProperties(deptPO, dept, true);
        //return dept;
        return null;
    }


    /**
     * 新增部门信息到微信
     *
     * @param deptPO the dept po
     * @return wx dept
     * @throws Exception     the exception
     * @throws BaseException the base exception
     * @author Sun Qinghai
     * @2014-8-4
     * @version 1.0
     */
    public WxDept doWxDept(TbDepartmentInfoPO deptPO) throws Exception, BaseException {
        WxDept wxDept = getDeptByNameAndParentId(deptPO.getDepartmentName(), deptPO.getWxParentid(), deptPO.getWxId());
        if (wxDept == null) {
            // 不存在此部门，调接口创建
            wxDept = new WxDept();
            wxDept.setName(deptPO.getDepartmentName());
            wxDept.setParentid(deptPO.getWxParentid());
            wxDept = WxDeptService.addDept(wxDept, org.getCorpId(), org.getOrgId());
        }
        return wxDept;
    }

    /**
     * 通过部门名称及父部门ID获得唯一部门
     *
     * @param dName    the d name
     * @param parentId the parent id
     * @param wxId     the wx id
     * @return dept by name and parent id
     */
    public WxDept getDeptByNameAndParentId(String dName, String parentId, String wxId) {
        handleDeptToMap();
        if (wxDeptMap != null) {
            if (wxId != null) {
                return wxDeptMapId.get(wxId);
            }
            return wxDeptMap.get(dName + "_" + parentId);
        }
        return null;
    }

    /**
     * 将部门转为map
     *
     * @author Sun Qinghai
     * @2014-8-1
     * @version 1.0
     */
    private void handleDeptToMap() {
        if (wxDeptList != null && wxDeptList.size() > 0 && wxDeptMap == null) {
            wxDeptMap = new HashMap<String, WxDept>(wxDeptList.size());
            wxDeptMapId = new HashMap<String, WxDept>(wxDeptList.size());
            for (WxDept dept : wxDeptList) {
                if (dept != null) {
                    wxDeptMap.put(dept.getName() + "_" + dept.getParentid(),
                            dept);
                    wxDeptMapId.put(dept.getId(), dept);
                }
            }
            wxDeptList = null;
        }
    }
}
