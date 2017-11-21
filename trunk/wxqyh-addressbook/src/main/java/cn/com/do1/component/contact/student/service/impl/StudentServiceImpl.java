package cn.com.do1.component.contact.student.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.UserInfoChangeNotifier;
import cn.com.do1.component.contact.student.dao.IStudentDAO;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.contact.student.util.StudentRefUtil;
import cn.com.do1.component.contact.student.util.StudentSynUtil;
import cn.com.do1.component.contact.student.util.StudentUitl;
import cn.com.do1.component.contact.student.vo.*;
import cn.com.do1.component.log.operationlog.util.LogOperation;
import cn.com.do1.component.managesetting.managesetting.vo.OrgIndustryVersionVO;
import cn.com.do1.component.qwinterface.addressbook.UserInfoChangeInformType;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hejinjiao on 2016/11/18.
 */
@Service("studentService")
public class StudentServiceImpl extends BaseService implements IStudentService {
    /**
     * The Student dao.
     */
    private IStudentDAO studentDAO;
    /**
     * The Contact service.
     */
    private IContactService contactService;
    /**
     * The Contact custom mgr service.
     */
    private IContactCustomMgrService contactCustomMgrService;

    /**
     * Sets student dao.
     *
     * @param studentDAO the student dao
     */
    @Resource
    public void setStudentDAO(IStudentDAO studentDAO) {
        this.studentDAO = studentDAO;
        setDAO(studentDAO);
    }

    /**
     * Sets contact service.
     *
     * @param contactService the contact service
     */
    @Resource(name = "contactService")
    public void setContactService(IContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Sets contact custom mgr service.
     *
     * @param contactCustomMgrService the contact custom mgr service
     */
    @Resource(name = "contactCustomService")
    public void setContactCustomMgrService(IContactCustomMgrService contactCustomMgrService) {
        this.contactCustomMgrService = contactCustomMgrService;
    }

    @Override
    public Pager searchStudent(Map searchValue, Pager pager) throws Exception, BaseException {
        if (searchValue.containsKey("parentName")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orgId", searchValue.get("orgId"));
            if (searchValue.containsKey("parentName")) {
                map.put("parentName", searchValue.get("parentName"));
            }
            List<String> parentIds = studentDAO.seachUserIdsByPersonName(map);
            searchValue.put("parentIds", parentIds);
            searchValue.remove("parentName");
        }
        pager = studentDAO.searchStudent(searchValue, pager);
        return assemblePager(pager);
    }

    /**
     * 查询学生监护人
     *
     * @param pager the pager
     * @return pager pager
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private Pager assemblePager(Pager pager) throws Exception, BaseException {
        List<StudentInfoVO> list = (List<StudentInfoVO>) pager.getPageData();
        if (list != null && list.size() > 0) {
            List<String> stu_ids = new ArrayList<String>(list.size());
            for (StudentInfoVO vo : list) {
                stu_ids.add(vo.getId());
            }
            Map<String, List<UserStudentRefVO>> listMap = StudentSynUtil.getUserStudentRefMap(stu_ids);
            if (!AssertUtil.isEmpty(listMap)) {
                for (StudentInfoVO vo : list) {
                    vo.setParentlist(listMap.get(vo.getId()));
                }
            }
            pager.setPageData(list);
        }
        return pager;
    }


    @Override
    public List<TbDepartmentInfoEduPO> searchSchoolClass(Map<String, Object> map) throws Exception, BaseException {
        return studentDAO.searchSchoolClass(map);
    }

    @Override
    public boolean judgementRepeat(String personName, String classId, String registerPhone, String orgId, String studentId) throws Exception, BaseException {
        return studentDAO.judgementRepeat(personName, classId, registerPhone, orgId, studentId);
    }

    @Override
    public void addStudnet(TbQyStudentInfoPO studentPO, String parents) throws Exception, BaseException {
        if (!StringUtil.isNullEmpty(parents)) {
            List<String> parentIds = new ArrayList<String>();
            List<TbQyUserStudentRefPO> list = StudentRefUtil.addUserStudentRef(parentIds, parents, studentPO.getId(), studentPO.getOrgId(), true);
            if (list.size() > 0) {
                studentDAO.execBatchInsert(list);
                studentPO.setHasParent(StudentUitl.has_parent);
            }
            modifyParentOrStudent(parentIds, studentPO);
        }
        this.insertPO(studentPO, false);
    }

    /**
     * 修改老师类型和学生是否为教师子女
     *
     * @param parentIds
     * @param studentPO
     */
    private void modifyParentOrStudent(List<String> parentIds, TbQyStudentInfoPO studentPO) throws Exception, BaseException {
        if (parentIds.size() > 0) {
            if (StudentUitl.has_teacher == studentPO.getHasTeacher()) {
                //是本教师子女则修改家长（通讯录用户）类型为：教师/职工
                studentDAO.updateParentType(parentIds, studentPO.getOrgId(), StudentUitl.is_teacher);
            } else {
                //非本教师子女则修改家长（通讯录用户）类型为：家长
                studentDAO.updateParentType(parentIds, studentPO.getOrgId(), StudentUitl.is_parent);
            }
            studentDAO.updateParentHasChildren(parentIds, studentPO.getOrgId());
            if (studentDAO.countTeachersByUserIds(parentIds, studentPO.getOrgId()) > 0) {
                studentPO.setHasTeacher(StudentUitl.has_teacher);
            }
        }
    }

    @Override
    public void updateStudent(TbQyStudentInfoPO studentPO, String parents) throws Exception, BaseException {
        List<TbQyUserStudentRefPO> hislist = studentDAO.findUserStudentRef(studentPO.getId());
        List<TbQyUserStudentRefPO> addlist = new ArrayList<TbQyUserStudentRefPO>();
        List<TbQyUserStudentRefPO> uplist = new ArrayList<TbQyUserStudentRefPO>();
        List<String> parentIds = new ArrayList<String>();
        List<String> ids = StudentRefUtil.updateUserStudentRef(parentIds, hislist, addlist, uplist, parents, studentPO.getId(), studentPO.getOrgId(), true);
        if (addlist.size() > 0) {
            studentDAO.execBatchInsert(addlist);
            studentPO.setHasParent(StudentUitl.has_parent);
        }
        if (uplist.size() > 0) {
            studentDAO.execBatchUpdate(uplist, false);
            studentPO.setHasParent(StudentUitl.has_parent);
        }
        //修改家长或老师信息
        modifyParentOrStudent(parentIds, studentPO);
        if (ids != null && ids.size() > 0) {
            this.batchDel(TbQyUserStudentRefPO.class, ids.toArray(new String[ids.size()]));
        }
        this.updatePO(studentPO, false);
    }

    @Override
    public List<TbQyOrganizeInfo> findClassList(Map<String, Object> map, boolean isChooseChild) throws Exception, BaseException {
        List<TbQyOrganizeInfo> list = studentDAO.findClassList(map);
        if (list.size() > 0 && isChooseChild) {
            //获取班级部门的非班级父部门
            Set<String> parentIds = new HashSet<String>(list.size());
            for (TbQyOrganizeInfo info : list) {
                if (!StringUtil.isNullEmpty(info.getParentId())) {
                    parentIds.add(info.getParentId());
                }
            }
            if (parentIds.size() > 0) {
                list.addAll(studentDAO.getDepartByParentIds(parentIds.toArray(new String[parentIds.size()])));
            }
        }
        return SchoolClassUtil.assembleEduDepartList(list);
    }

    @Override
    public Pager findSchoolStudent(Map searchValue, Pager pager) throws Exception, BaseException {
        return studentDAO.findSchoolStudent(searchValue, pager);
    }

    @Override
    public List<TbDepartmentInfoEduPO> seachEduDepartByDepartIds(List<String> departIds) throws Exception, BaseException {
        if (AssertUtil.isEmpty(departIds)) {
            return new ArrayList<TbDepartmentInfoEduPO>(1);
        }
        return studentDAO.seachEduDepartByDepartIds(departIds);
    }

    @Override
    public List<TbQyStudentInfoVO> getExistStudents(Map<String, Object> map) throws Exception, BaseException {
        return studentDAO.getExistStudents(map);
    }

    @Override
    public void batchDeleteByIds(String[] ids, UserOrgVO org) throws Exception, BaseException {
        List<TbQyStudentInfoPO> list = this.searchByPks(TbQyStudentInfoPO.class, ids);
        if (list.size() > 0) {
            StringBuffer stuNames = new StringBuffer();
            for (TbQyStudentInfoPO infoPO : list) {
                if (!org.getOrgId().equals(infoPO.getOrgId())) {
                    throw new NonePrintException("", "不能删除非本机构信息");
                }
                stuNames.append("，" + infoPO.getPersonName());
            }
            studentDAO.delParentRefByIds(ids, org.getOrgId());
            this.batchDel(TbQyStudentInfoPO.class, ids);
            LogOperation.insertOperationLog(org.getUserName(), org.getPersonName(), "删除学生：" + stuNames.deleteCharAt(0).toString(), "del", "student", org.getOrgId(), "删除成功");
        }
    }

    @Override
    public int countDepartByOrgId(String orgId) throws Exception, BaseException {
        return studentDAO.countDepartByOrgId(orgId);
    }

    @Override
    public int countstudentByOrgId(String orgId) throws Exception, BaseException {
        return studentDAO.countstudentByOrgId(orgId);
    }

    @Override
    public int countUserByOrgId(String orgId, String attribute) throws Exception, BaseException {
        return studentDAO.countUserByOrgId(orgId, attribute);
    }

    @Override
    public TbDepartmentInfoEduPO getDepartmentEduPO(String id) throws Exception, BaseException {
        return studentDAO.getDepartmentEduPO(id);
    }

    @Override
    public void addUserStudentRef(String orgId, String userId, String childdren) throws Exception, BaseException {
        TbQyUserInfoVO vo = contactService.findUserInfoByUserId(userId);
        if (vo == null && !orgId.equals(vo.getOrgId())) {
            throw new NonePrintException("", "非本机构数据无法修改");
        }
        List<String> stuIds = new ArrayList<String>();
        List<TbQyUserStudentRefPO> hislist = studentDAO.findUserStuRefByParentId(userId);
        if (hislist.size() > 0) {
            updateUserStudentRef(hislist, childdren, userId, orgId, stuIds);
        } else {
            List<TbQyUserStudentRefPO> list = StudentRefUtil.addUserStudentRef(stuIds, childdren, userId, orgId, false);
            if (list.size() > 0) {
                studentDAO.execBatchInsert(list);
                updateStudentHasParent(list, null, orgId);
            }
        }
        if (StudentUitl.is_teacher == vo.getAttribute() && stuIds.size() > 0) {
            studentDAO.updateStudentHasTeacher(stuIds, vo.getOrgId(), StudentUitl.has_teacher);
        }
    }

    /**
     * 修改关联表数据
     *
     * @param hislist   the hislist
     * @param childdren the childdren
     * @param userId    the user id
     * @param orgId     the org id
     * @param stuIds
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void updateUserStudentRef(List<TbQyUserStudentRefPO> hislist, String childdren, String userId, String orgId, List<String> stuIds)
            throws Exception, BaseException {
        List<TbQyUserStudentRefPO> addlist = new ArrayList<TbQyUserStudentRefPO>();
        List<TbQyUserStudentRefPO> uplist = new ArrayList<TbQyUserStudentRefPO>();
        List<String> ids = StudentRefUtil.updateUserStudentRef(stuIds, hislist, addlist, uplist, childdren, userId, orgId, false);
        if (addlist.size() > 0) {
            studentDAO.execBatchInsert(addlist);
        }
        if (uplist.size() > 0) {
            studentDAO.execBatchUpdate(uplist, false);
        }
        if (ids != null && ids.size() > 0) {
            this.batchDel(TbQyUserStudentRefPO.class, ids.toArray(new String[ids.size()]));
            modifyStudentByRefId(ids, hislist, orgId, userId);
        }
        updateStudentHasParent(addlist, uplist, orgId);
    }

    /**
     * 修改学生是否有家长/监护人信息状态
     *
     * @param ids     the ids
     * @param hislist the hislist
     * @param orgId   the org id
     * @param userId  the user id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void modifyStudentByRefId(List<String> ids, List<TbQyUserStudentRefPO> hislist, String orgId, String userId)
            throws Exception, BaseException {
        Map<String, TbQyUserStudentRefPO> refMap = new HashMap<String, TbQyUserStudentRefPO>(hislist.size());
        //查找学生ID
        List<String> stuIds = new ArrayList<String>(ids.size());
        for (TbQyUserStudentRefPO ref : hislist) {
            refMap.put(ref.getId(), ref);
        }
        for (String id : ids) {
            if (refMap.containsKey(id)) {
                stuIds.add(refMap.get(id).getStudentId());
            }
        }
        if (stuIds.size() > 0) {
            //有家长/监护人的学生id
            List<String> noParentIds = new ArrayList<String>(ids.size());
            //无家长/监护人的学生id
            List<String> hasParentIds = new ArrayList<String>(ids.size());
            Map<String, List<UserStudentRefVO>> listMap = StudentSynUtil.getUserStudentRefMap(stuIds);
            for (String stuId : stuIds) {
                if (listMap.containsKey(stuId)) {
                    if (listMap.get(stuId).size() > 2 || !listMap.get(stuId).get(0).getUserId().equals(userId)) {
                        hasParentIds.add(stuId);
                    } else {
                        noParentIds.add(stuId);
                    }
                } else {
                    noParentIds.add(stuId);
                }
            }
            if (hasParentIds.size() > 0) {
                studentDAO.updateHasParent(hasParentIds.toArray(new String[hasParentIds.size()]), orgId, StudentUitl.has_parent);
            }
            if (noParentIds.size() > 0) {
                studentDAO.updateHasParent(noParentIds.toArray(new String[noParentIds.size()]), orgId, StudentUitl.no_parent);
            }
        }

    }

    /**
     * 更新学生是否有家长/监护人信息
     *
     * @param addlist the addlist
     * @param uplist  the uplist
     * @param orgId   the org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private void updateStudentHasParent(List<TbQyUserStudentRefPO> addlist, List<TbQyUserStudentRefPO> uplist, String orgId)
            throws Exception, BaseException {
        Set<String> children = new HashSet<String>();
        if (addlist != null && addlist.size() > 0) {
            for (TbQyUserStudentRefPO ref : addlist) {
                children.add(ref.getStudentId());
            }
        }
        if (uplist != null && uplist.size() > 0) {
            for (TbQyUserStudentRefPO ref : uplist) {
                children.add(ref.getStudentId());
            }
        }
        if (children.size() > 0) {
            studentDAO.updateHasParent(children.toArray(new String[children.size()]), orgId, StudentUitl.has_parent);
        }
    }

    @Override
    public List<UserStudentRefVO> getStuListByParentId(String userId, String orgId) throws Exception, BaseException {
        return studentDAO.getStuListByParentId(userId, orgId);
    }

    @Override
    public List<UserStudentRefVO> findParentsByStuId(String studentId) throws Exception, BaseException {
        return studentDAO.findParentsByStuId(studentId);
    }

    @Override
    public TbQyStudentInfoVO getStudentDetail(String studentId) throws Exception, BaseException {
        return studentDAO.getStudentDetail(studentId);
    }

    @Override
    public List<TbQyStudentInfoVO> findStudentByNames(List<String> stuNames, String orgId) throws Exception, BaseException {
        return studentDAO.findStudentByNames(stuNames, orgId);
    }

    @Override
    public List<TbQyUserInfoVO> findUsersByWxUserIds(String orgId, List<String> wxUserId) throws Exception, BaseException {
        return studentDAO.findUsersByWxUserIds(orgId, wxUserId);
    }

    @Override
    public void synToAddressBook(String[] studentIds, UserOrgVO org) throws Exception, BaseException {
        List<TbQyStudentInfoPO> list = this.searchByPks(TbQyStudentInfoPO.class, studentIds);
        List<String> depIds = new ArrayList<String>(); // 部门Id
        StringBuffer names = new StringBuffer(); // 学生名字
        //修改同步状态
        List<TbQyStudentInfoPO> uplist = new ArrayList<TbQyStudentInfoPO>(list.size());
        for (TbQyStudentInfoPO stuPO : list) {
            if (!org.getOrgId().equals(stuPO.getOrgId())) {
                throw new NonePrintException("", "非本机构信息，不能同步");
            }
            depIds.add(stuPO.getClassId());
        }
        Map<String, TbDepartmentInfoPO> dapMap = getDepartMap(depIds);
        for (TbQyStudentInfoPO stuPO : list) {
            if (dapMap.containsKey(stuPO.getClassId())) {
                names.append("," + stuPO.getPersonName());
                StudentSynUtil.synStudent(stuPO, dapMap.get(stuPO.getClassId()), org.getCorpId(), uplist);
            } else {
                throw new NonePrintException("", "班级部门不存在，无法同步至通讯录");
            }
        }
        if (uplist.size() > 0) {
            //修改学生同步状态
            studentDAO.execBatchUpdate(uplist, false);
        }
        if (names.length() > 0) {
            LogOperation.insertOperationLog(org.getUserName(), org.getPersonName(), "同步至通讯录的学生：" + names.deleteCharAt(0).toString(),
                    "syn", "student", org.getOrgId(), "同步成功");
        }
    }

    /**
     * 组装同步到通讯录的学生部门Map
     *
     * @param depIds the dep ids
     * @return depart map
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private Map<String, TbDepartmentInfoPO> getDepartMap(List<String> depIds) throws Exception, BaseException {
        List<TbDepartmentInfoPO> list = this.searchByPks(TbDepartmentInfoPO.class, depIds);
        if (list == null || list.size() == 0) {
            return new HashMap<String, TbDepartmentInfoPO>(1);
        }
        Map<String, TbDepartmentInfoPO> map = new HashMap<String, TbDepartmentInfoPO>(list.size());
        for (TbDepartmentInfoPO dapartment : list) {
            map.put(dapartment.getId(), dapartment);
        }
        return map;
    }

    @Override
    public void delSynAddressBook(String[] studentIds, UserOrgVO org) throws Exception, BaseException {
        List<TbQyStudentInfoPO> list = this.searchByPks(TbQyStudentInfoPO.class, studentIds);
        StringBuffer names = new StringBuffer();
        List<String> userIds = new ArrayList<String>();
        for (TbQyStudentInfoPO stuPO : list) {
            if (!org.getOrgId().equals(stuPO.getOrgId())) {
                throw new NonePrintException("", "非本机构信息，不能移除");
            }
            names.append("," + stuPO.getPersonName());
            //从微信移除
            WxUserService.delUser(stuPO.getId(), org.getCorpId(), org.getOrgId());
            userIds.add(stuPO.getId());
        }
        if (userIds.size() > 0) {
            String[] ids = contactService.batchDeleteUser(ListUtil.toArrays(userIds), org);
            //更新部门人数
            UserInfoChangeNotifier.batchDelEnd(org, ids, UserInfoChangeInformType.USER_MGR);
            contactCustomMgrService.deleBatchUser(ids);
            //用户是学生，修改同步信息
            this.updateStudentSyn(ListUtil.toArrays(userIds), org.getOrgId(), StudentUitl.no_syn);
        }
        if (names.length() > 0) {
            LogOperation.insertOperationLog(org.getUserName(), org.getPersonName(), "从通讯录移除的学生：" + names.deleteCharAt(0).toString(),
                    "del", "student", org.getOrgId(), "移除成功");
        }
    }

    @Override
    public void addStudentList(List<TbQyStudentInfoPO> addlist) throws Exception, BaseException {
        studentDAO.execBatchInsert(addlist);
    }

    @Override
    public void updateStudentList(List<TbQyStudentInfoPO> uplist, List<String> stuIds, String orgId) throws Exception, BaseException {
        if (stuIds.size() > 0) {
            studentDAO.delParentRefByIds(stuIds.toArray(new String[stuIds.size()]), orgId);
        }
        studentDAO.execBatchUpdate(uplist, false);
    }

    @Override
    public void addStudnetRef(List<TbQyUserStudentRefPO> reflist) throws Exception, BaseException {
        studentDAO.execBatchInsert(reflist);
    }

    @Override
    public List<SchoolClassVO> searchClassByUserId(String userId, String orgId) throws Exception, BaseException {
        List<SchoolClassVO> list = studentDAO.searchClassByUserId(userId, orgId);
        if (list.size() > 0) {
            Map<String, SchoolClassVO> classMap = this.getClassMap(list);
            for (SchoolClassVO classVO : list) {
                if (classMap.containsKey(classVO.getDepartmentId())) {
                    classVO.setStudentNum(classMap.get(classVO.getDepartmentId()).getStudentNum());
                } else {
                    classVO.setStudentNum(0);
                }
            }
        }
        return list;
    }

    /**
     * 查询班级的学生人数
     *
     * @param list the list
     * @return class map
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private Map<String, SchoolClassVO> getClassMap(List<SchoolClassVO> list) throws Exception, BaseException {
        List<String> classIds = new ArrayList<String>(list.size());
        for (SchoolClassVO classVO : list) {
            classIds.add(classVO.getDepartmentId());
        }
        List<SchoolClassVO> stulist = studentDAO.countStudentsByClassIds(classIds);
        if (stulist.size() == 0) {
            return new HashMap<String, SchoolClassVO>(1);
        }
        Map<String, SchoolClassVO> classMap = new HashMap<String, SchoolClassVO>(stulist.size());
        for (SchoolClassVO vo : stulist) {
            classMap.put(vo.getDepartmentId(), vo);
        }
        return classMap;
    }

    @Override
    public List<TbQyUserInfoVO> findTeachersByClassId(String classId) throws Exception, BaseException {
        return studentDAO.findTeachersByClassId(classId);
    }

    @Override
    public List<UserStudentRefVO> findTeachersByClassIds(String[] classIds) throws Exception, BaseException {
        return studentDAO.findTeachersByClassIds(classIds);
    }

    @Override
    public boolean judgementRation(String userId, String studentId) throws Exception, BaseException {
        return studentDAO.judgementRation(userId, studentId);
    }

    @Override
    public List<ExportStudentVO> exportStudentList(Map map) throws Exception, BaseException {
        if (map.containsKey("parentName")) {
            Map<String, Object> pamas = new HashMap<String, Object>();
            pamas.put("orgId", map.get("orgId"));
            pamas.put("parentName", map.get("parentName"));
            List<String> parentIds = studentDAO.seachUserIdsByPersonName(pamas);
            map.put("parentIds", parentIds);
            map.remove("parentName");
        }
        return studentDAO.exportStudentList(map);
    }

    @Override
    public List<ExportParentVO> exportParentList(List<String> stuIds) throws Exception, BaseException {
        return studentDAO.exportParentList(stuIds);
    }

    @Override
    public TbDepartmentInfoEduPO getDepartEduByClassName(int grade, int className, String orgId) throws Exception, BaseException {
        return studentDAO.getDepartEduByClassName(grade, className, orgId);
    }

    @Override
    public void modifyStudentClassId(String oldDepartId, String newDepartId) throws Exception, BaseException {
        studentDAO.modifyStudentClassId(oldDepartId, newDepartId);
    }

    @Override
    public List<OrgIndustryVersionVO> findEducationOrgs() throws Exception, BaseException {
        return studentDAO.findEducationOrgs();
    }

    @Override
    public List<TbDepartmentInfoEduPO> findAllClassByOrgId(String orgId) throws Exception, BaseException {
        return studentDAO.findAllClassByOrgId(orgId);
    }

    /**
     * 查询监护人的孩子
     *
     * @param searchMap 查询条件
     * @param depts     部门列表
     * @return
     * @throws BaseException 这是异常，懂不
     * @throws Exception     这是异常，懂不
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    public List<ChildrenVO> findChildren(Map searchMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception {
        return studentDAO.findChildren(searchMap, depts);
    }

    @Override
    public void initStudentPinyin() throws Exception, BaseException {
        List<TbQyStudentInfoPO> list = studentDAO.searchAllStudent();
        if (list.size() > 0) {
            for (TbQyStudentInfoPO po : list) {
                po.setPinyin(PingYinUtil.getPingYin(po.getPersonName()));
            }
            studentDAO.execBatchUpdate(list, false);
        }
    }

    @Override
    public List<String> getClassIdsByDepartName(String orgId, String deptName) throws Exception, BaseException {
        return studentDAO.getClassIdsByDepartName(orgId, deptName);
    }

    /**
     * 查询监护人的孩子
     *
     * @param userId
     * @return
     * @throws BaseException 这是异常，懂不
     * @throws Exception     这是异常，懂不
     * @author liyixin
     * @2017-1-17
     * @version 1.0
     */
    public List<ChildrenVO> getChildrenByUserId(String userId) throws BaseException, Exception {
        return studentDAO.getChildrenByUserId(userId);
    }

    @Override
    public List<TbQyUserStudentRefPO> findStudentsByParents(String[] userIds) throws Exception, BaseException {
        return studentDAO.findStudentsByParents(userIds);
    }

    @Override
    public void delStudentRefByParents(String[] userIds) throws Exception, BaseException {
        studentDAO.delStudentRefByParents(userIds);
    }

    @Override
    public List<UserStudentRefVO> getParentsByStuIds(List<String> stuIds) throws Exception, BaseException {
        return studentDAO.getParentsByStuIds(stuIds);
    }

    @Override
    public void updateHasParent(String[] studentIds, String orgId, int type) throws Exception, BaseException {
        studentDAO.updateHasParent(studentIds, orgId, type);
    }

    @Override
    public void updateStudentSyn(String[] studentIds, String orgId, int type) throws Exception, BaseException {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("studentIds", studentIds);
        map.put("orgId", orgId);
        map.put("type", type);
        studentDAO.updateStudentSyn(map);
    }
}
