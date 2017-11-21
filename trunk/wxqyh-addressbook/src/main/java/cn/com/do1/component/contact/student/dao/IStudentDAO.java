package cn.com.do1.component.contact.student.dao;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
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
public interface IStudentDAO extends IBaseDAO {

    /**
     * 根据personName查询用户userId
     *
     * @param map the map
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<String> seachUserIdsByPersonName(Map<String, Object> map) throws Exception, BaseException;

    /**
     * 查询分页学生
     *
     * @param searchValue the search value
     * @param pager       the pager
     * @return the pager
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    Pager searchStudent(Map searchValue, Pager pager) throws Exception, BaseException;

    /**
     * 查询学校班级
     *
     * @param map the map
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> searchSchoolClass(Map<String, Object> map) throws Exception, BaseException;

    /**
     * 判断重名.
     *
     * @param personName    the person name
     * @param classId       the class id
     * @param registerPhone the register phone
     * @param orgId         the org id
     * @param studentId     the student id
     * @return the boolean
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    boolean judgementRepeat(String personName, String classId, String registerPhone, String orgId, String studentId) throws Exception, BaseException;

    /**
     * Find class list list.
     *
     * @param map the org id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyOrganizeInfo> findClassList(Map<String, Object> map) throws Exception, BaseException;

    /**
     * Find school student pager.
     *
     * @param searchValue the search value
     * @param pager       the pager
     * @return the pager
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    Pager findSchoolStudent(Map searchValue, Pager pager) throws Exception, BaseException;

    /**
     * 根据部门ids获取班级信息
     *
     * @param departIds the depart ids
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> seachEduDepartByDepartIds(List<String> departIds) throws Exception, BaseException;

    /**
     * 获取已存在的学生列表
     *
     * @param map the map
     * @return the exist students
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyStudentInfoVO> getExistStudents(Map<String, Object> map) throws Exception, BaseException;

    /**
     * 更加学生ids删除父母-学生关联表
     *
     * @param ids   the ids
     * @param orgId the org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void delParentRefByIds(String[] ids, String orgId) throws Exception, BaseException;

    /**
     * 根据学生id查询关联表信息
     *
     * @param userId the user id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserStudentRefPO> findUserStudentRef(String userId) throws Exception, BaseException;

    /**
     * 根据学生ids查询家长/监护人列表
     *
     * @param stuIds the stu ids
     * @return the parents by stu ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> getParentsByStuIds(List<String> stuIds) throws Exception, BaseException;

    /**
     * 统计部门个数
     *
     * @param orgId the org id
     * @return the int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countDepartByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 统计学生人数
     *
     * @param orgId the org id
     * @return the int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countstudentByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 统计教师/家长人数
     *
     * @param orgId     the org id
     * @param attribute the attribute
     * @return the int
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    int countUserByOrgId(String orgId, String attribute) throws Exception, BaseException;

    /**
     * 根据父母userId查询关联表信息
     *
     * @param userId the user id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserStudentRefPO> findUserStuRefByParentId(String userId) throws Exception, BaseException;

    /**
     * 根据父母id查询学生列表
     *
     * @param userId the user id
     * @param orgId  the org id
     * @return the stu list by parent id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> getStuListByParentId(String userId, String orgId) throws Exception, BaseException;

    /**
     * 根据学生id查询父母信息
     *
     * @param studentId the student id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> findParentsByStuId(String studentId) throws Exception, BaseException;

    /**
     * 查询学生详情
     *
     * @param studentId the student id
     * @return the student detail
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    TbQyStudentInfoVO getStudentDetail(String studentId) throws Exception, BaseException;

    /**
     * 根据学生名字查询学生列表
     *
     * @param stuNames the stu names
     * @param orgId    the org id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyStudentInfoVO> findStudentByNames(List<String> stuNames, String orgId) throws Exception, BaseException;

    /**
     * 根据账号wxUserIds查询用户userIds
     *
     * @param orgId    the org id
     * @param wxUserId the wx user id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserInfoVO> findUsersByWxUserIds(String orgId, List<String> wxUserId) throws Exception, BaseException;

    /**
     * 根据id获取班级信息
     *
     * @param id the id
     * @return the department edu po
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    TbDepartmentInfoEduPO getDepartmentEduPO(String id) throws Exception, BaseException;

    /**
     * 根据userId查询班级信息
     *
     * @param userId the user id
     * @param orgId  the org id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<SchoolClassVO> searchClassByUserId(String userId, String orgId) throws Exception, BaseException;

    /**
     * 统计班级学生人数
     *
     * @param classIds the class ids
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<SchoolClassVO> countStudentsByClassIds(List<String> classIds) throws Exception, BaseException;

    /**
     * 根据班级id查询老师列表
     *
     * @param classId the class id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserInfoVO> findTeachersByClassId(String classId) throws Exception, BaseException;

    /**
     * 根据班级ids查询老师列表
     *
     * @param classIds the class ids
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<UserStudentRefVO> findTeachersByClassIds(String[] classIds) throws Exception, BaseException;

    /**
     * 判断关系是否存在
     *
     * @param userId    the user id
     * @param studentId the student id
     * @return the boolean
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    boolean judgementRation(String userId, String studentId) throws Exception, BaseException;

    /**
     * 查询需要导出的学生列表
     *
     * @param map the map
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<ExportStudentVO> exportStudentList(Map map) throws Exception, BaseException;

    /**
     * 需要导出的家长/监护人信息
     *
     * @param stuIds the stu ids
     * @return the list
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
     * 查询班级部门的父部门
     *
     * @param parentIds the parent ids
     * @return depart by parent ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyOrganizeInfo> getDepartByParentIds(String[] parentIds) throws Exception, BaseException;

    /**
     * 查询教育版机构
     *
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<OrgIndustryVersionVO> findEducationOrgs() throws Exception, BaseException;

    /**
     * 根据班级Id获取所用的班级信息.
     *
     * @param orgId the org id
     * @return the list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbDepartmentInfoEduPO> findAllClassByOrgId(String orgId) throws Exception, BaseException;

    /**
     * 查询监护人的孩子
     *
     * @param searchMap 查询条件
     * @param depts     部门列表
     * @return list
     * @throws BaseException 这是异常，懂不
     * @throws Exception     这是异常，懂不
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    List<ChildrenVO> findChildren(Map searchMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception;

    /**
     * 修改学生是否存在家长信息字段
     *
     * @param studentIds the student ids
     * @param orgId      @throws Exception
     * @param type       the type
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void updateHasParent(String[] studentIds, String orgId, int type) throws Exception, BaseException;

    /**
     * 查询所有的学生
     *
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyStudentInfoPO> searchAllStudent() throws Exception, BaseException;

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
     * 根据父母ids查询学生列表
     *
     * @param userIds the user ids
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    List<TbQyUserStudentRefPO> findStudentsByParents(String[] userIds) throws Exception, BaseException;

    /**
     * 根据父母ids删除关联表信息
     *
     * @param userIds the user ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void delStudentRefByParents(String[] userIds) throws Exception, BaseException;

    /**
     * 修改学生信息同步状态
     *
     * @param map the map
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    void updateStudentSyn(Map<String, Object> map) throws Exception, BaseException;

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
     * 更新家长属性类型
     *
     * @param userIds
     * @param orgId
     * @param type
     * @throws BaseException
     * @throws Exception
     */
    void updateParentType(List<String> userIds, String orgId, int type) throws BaseException, Exception;

    /**
     * 更新是否为教师子女状态
     *
     * @param stuIds
     * @param orgId
     * @param type   @throws BaseException
     * @throws Exception
     */
    void updateStudentHasTeacher(List<String> stuIds, String orgId, int type) throws BaseException, Exception;

    /**
     * 统计老师个数
     *
     * @param userIds
     * @param orgId
     * @return
     * @throws BaseException
     * @throws Exception
     */
    int countTeachersByUserIds(List<String> userIds, String orgId) throws BaseException, Exception;

    /**
     * 更新老师存在孩子
     * @param userIds
     * @param orgId
     * @throws BaseException
     * @throws Exception
     */
    void updateParentHasChildren(List<String> userIds, String orgId)throws BaseException, Exception;
}
