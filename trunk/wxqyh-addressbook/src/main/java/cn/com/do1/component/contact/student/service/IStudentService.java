package cn.com.do1.component.contact.student.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.vo.*;
import cn.com.do1.component.managesetting.managesetting.vo.OrgIndustryVersionVO;

import java.util.List;
import java.util.Map;

/**
 * Created by hejinjiao on 2016/11/18.
 */
public interface IStudentService extends IBaseService {

    /**
     * 分页查询学生信息
     *
     * @param searchValue the search value
     * @param pager       the pager
     * @return pager pager
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    Pager searchStudent(Map searchValue, Pager pager) throws Exception, BaseException;

    /**
     * 查询班级
     *
     * @param map the map
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> searchSchoolClass(Map<String, Object> map) throws Exception, BaseException;

    /**
     * 验证重复学生信息
     *
     * @param personName    the person name
     * @param classId       the class id
     * @param registerPhone the register phone
     * @param orgId         the org id
     * @param studentId     the student id
     * @return boolean boolean
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    boolean judgementRepeat(String personName, String classId, String registerPhone, String orgId, String studentId) throws Exception, BaseException;

    /**
     * 新增学生信息
     *
     * @param studentPO the student po
     * @param parents   the parents
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void addStudnet(TbQyStudentInfoPO studentPO, String parents) throws Exception, BaseException;

    /**
     * 修改学生信息
     *
     * @param studentPO the student po
     * @param parents   the parents
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void updateStudent(TbQyStudentInfoPO studentPO, String parents) throws Exception, BaseException;

    /**
     * 选择孩子页面接口
     *
     * @param map           the org id
     * @param isChooseChild the is choose child
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyOrganizeInfo> findClassList(Map<String, Object> map, boolean isChooseChild) throws Exception, BaseException;

    /**
     * 选择孩子页面接口
     *
     * @param searchValue the search value
     * @param pager       the pager
     * @return pager pager
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    Pager findSchoolStudent(Map searchValue, Pager pager) throws Exception, BaseException;

    /**
     * 根据部门Ids查班级信息
     *
     * @param departIds the depart ids
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> seachEduDepartByDepartIds(List<String> departIds) throws Exception, BaseException;

    /**
     * 已存在的学生信息
     *
     * @param map the map
     * @return exist students
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyStudentInfoVO> getExistStudents(Map<String, Object> map) throws Exception, BaseException;

    /**
     * 删除学生信息
     *
     * @param ids the ids
     * @param org the org
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void batchDeleteByIds(String[] ids, UserOrgVO org) throws Exception, BaseException;

    /**
     * 统计班级部门总数
     *
     * @param orgId the org id
     * @return the int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countDepartByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 统计学生总数.
     *
     * @param orgId the org id
     * @return the int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countstudentByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 统计家长或老师职工数
     *
     * @param orgId     the org id
     * @param attribute the attribute
     * @return int int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countUserByOrgId(String orgId, String attribute) throws Exception, BaseException;

    /**
     * 查询班级部门扩展信息
     *
     * @param id the id
     * @return department edu po
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    TbDepartmentInfoEduPO getDepartmentEduPO(String id) throws Exception, BaseException;

    /**
     * 家长老师添加学生信息
     *
     * @param orgId     the org id
     * @param userId    the user id
     * @param childdren the childdren
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void addUserStudentRef(String orgId, String userId, String childdren) throws Exception, BaseException;

    /**
     * 根据父母Id查询学生
     *
     * @param userId the user id
     * @param orgId  the org id
     * @return stu list by parent id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> getStuListByParentId(String userId, String orgId) throws Exception, BaseException;

    /**
     * 查询家长/监护人信息
     *
     * @param studentId the student id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> findParentsByStuId(String studentId) throws Exception, BaseException;

    /**
     * 查询学生详情
     *
     * @param studentId the student id
     * @return student detail
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    TbQyStudentInfoVO getStudentDetail(String studentId) throws Exception, BaseException;

    /**
     * Find student by names list.
     *
     * @param stuNames the stu names
     * @param orgId    the org id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyStudentInfoVO> findStudentByNames(List<String> stuNames, String orgId) throws Exception, BaseException;

    /**
     * 根据账号查询用户
     *
     * @param orgId    the org id
     * @param wxUserId the wx user id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserInfoVO> findUsersByWxUserIds(String orgId, List<String> wxUserId) throws Exception, BaseException;

    /**
     * 同步至通讯录
     *
     * @param studentIds the student ids
     * @param org        the org
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void synToAddressBook(String[] studentIds, UserOrgVO org) throws Exception, BaseException;

    /**
     * 从通讯录移除
     *
     * @param studentIds the student ids
     * @param org        the org
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void delSynAddressBook(String[] studentIds, UserOrgVO org) throws Exception, BaseException;

    /**
     * 导入插入新增学生信息
     *
     * @param addlist the addlist
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void addStudentList(List<TbQyStudentInfoPO> addlist) throws Exception, BaseException;

    /**
     * 导入修改学生信息
     *
     * @param uplist the uplist
     * @param upIds  the up ids
     * @param orgId  the org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void updateStudentList(List<TbQyStudentInfoPO> uplist, List<String> upIds, String orgId) throws Exception, BaseException;

    /**
     * 导入学生-家长关联信息
     *
     * @param reflist the reflist
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void addStudnetRef(List<TbQyUserStudentRefPO> reflist) throws Exception, BaseException;

    /**
     * 查询老师负责的班级
     *
     * @param userId the user id
     * @param orgId  the org id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<SchoolClassVO> searchClassByUserId(String userId, String orgId) throws Exception, BaseException;

    /**
     * 根据班级查询老师
     *
     * @param classId the class id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserInfoVO> findTeachersByClassId(String classId) throws Exception, BaseException;

    /**
     * 根据班级Ids查询老师
     *
     * @param classIds the class ids
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> findTeachersByClassIds(String[] classIds) throws Exception, BaseException;

    /**
     * 是否存在学生-监护人关系
     *
     * @param userId    the user id
     * @param studentId the student id
     * @return boolean boolean
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    boolean judgementRation(String userId, String studentId) throws Exception, BaseException;

    /**
     * 需要导出的学生列表
     *
     * @param map the map
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<ExportStudentVO> exportStudentList(Map map) throws Exception, BaseException;

    /**
     * 需要导出的家长/监护人信息
     *
     * @param stuIds the stu ids
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<ExportParentVO> exportParentList(List<String> stuIds) throws Exception, BaseException;

    /**
     * 根据班级查询班级信息
     *
     * @param grade     the grade
     * @param className the class name
     * @param orgId     the org id
     * @return depart edu by class name
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    TbDepartmentInfoEduPO getDepartEduByClassName(int grade, int className, String orgId) throws Exception, BaseException;

    /**
     * 修改学生班级Id
     *
     * @param oldDepartId the old depart id
     * @param newDepartId the new depart id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void modifyStudentClassId(String oldDepartId, String newDepartId) throws Exception, BaseException;

    /**
     * 查询教育版机构
     *
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<OrgIndustryVersionVO> findEducationOrgs() throws Exception, BaseException;

    /**
     * 根据机构获取所有班级部门信息
     *
     * @param orgId the org id
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> findAllClassByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 初始化学生名字拼音
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void initStudentPinyin() throws Exception, BaseException;

    /**
     * 根据部门名称查询所有属于教学班级的子部门ID
     *
     * @param orgId    the org id
     * @param deptName the dept name
     * @return class ids by depart name
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<String> getClassIdsByDepartName(String orgId, String deptName) throws Exception, BaseException;

    /**
     * 批量查询监护人的孩子
     *
     * @param searchMap 查询条件
     * @param depts     部门列表
     * @return list list
     * @throws BaseException 这是异常，懂不
     * @throws Exception     这是异常，懂不
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    List<ChildrenVO> findChildren(Map searchMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception;

    /**
     * 查询监护人的孩子
     *
     * @param userId the user id
     * @return children by user id
     * @throws BaseException 这是异常，懂不
     * @throws Exception     这是异常，懂不
     * @author liyixin
     * @2017-1-17
     * @version 1.0
     */
    List<ChildrenVO> getChildrenByUserId(String userId) throws BaseException, Exception;

    /**
     * 根据父母ids查询学生列表
     *
     * @param userIds the user ids
     * @return list list
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    List<TbQyUserStudentRefPO> findStudentsByParents(String[] userIds) throws BaseException, Exception;

    /**
     * 根据父母ids删除关联表信息
     *
     * @param userIds the user ids
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    void delStudentRefByParents(String[] userIds) throws BaseException, Exception;

    /**
     * 修改学生是否存在父母信息
     *
     * @param strings the strings
     * @param orgId   the org id
     * @param type    the type
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    void updateHasParent(String[] strings, String orgId, int type) throws BaseException, Exception;

    /**
     * 修改学生同步状态
     *
     * @param userIds the user ids
     * @param orgId   the org id
     * @param type    the type
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    void updateStudentSyn(String[] userIds, String orgId, int type) throws BaseException, Exception;

    /**
     * 根据学生ids查询父母信息
     *
     * @param stuIds the stu ids
     * @return parents by stu ids
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    List<UserStudentRefVO> getParentsByStuIds(List<String> stuIds) throws BaseException, Exception;
}
