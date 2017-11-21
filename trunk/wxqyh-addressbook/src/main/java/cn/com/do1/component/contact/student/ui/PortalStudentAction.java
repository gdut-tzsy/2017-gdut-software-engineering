package cn.com.do1.component.contact.student.ui;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.CatchException;
import cn.com.do1.common.annotation.struts.InterfaceParam;
import cn.com.do1.common.annotation.struts.JSONOut;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.framebase.struts.BaseAction;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.vo.TbQyStudentInfoVO;
import cn.com.do1.component.contact.student.vo.UserStudentRefVO;
import cn.com.do1.component.core.WxqyhAppContext;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hejinjiao on 2016/11/27.
 */
public class PortalStudentAction extends BaseAction {
    /**
     * The Student service.
     */
    private IStudentService studentService;

    /**
     * Sets student service.
     *
     * @param studentService the student service
     */
    @Resource(name = "studentService")
    public void setStudentService(IStudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 查询学生列表
     *
     * @param userId the user id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询孩子成功", faileMsg = "查询孩子失败"))
    public void seachChildren(@InterfaceParam(name = "userId") @Validation(must = true, name = "用户") String userId) throws Exception, BaseException {
        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        if (IndustryUtil.isEduVersion(user.getOrgId())) { //教育行业版判断
            List<UserStudentRefVO> list = studentService.getStuListByParentId(userId, user.getOrgId());
            list = findClassTeachers(list);
            addJsonArray("children", list);
        }
    }

    /**
     * 查询学生班主任或老师的电话
     * @param list
     * @return
     * @throws Exception
     * @throws BaseException
     */
    private List<UserStudentRefVO> findClassTeachers(List<UserStudentRefVO> list) throws Exception, BaseException{
        if(list.size()>0){
            Set<String> classSet = new HashSet<String>();
            for(UserStudentRefVO refVO : list){
                classSet.add(refVO.getClassId());
            }
            Map<String, UserStudentRefVO> map = getTeachersByClassIds(classSet.toArray(new String[classSet.size()]));
            if(map.size()>0){
                for(UserStudentRefVO refVO : list){
                    if(map.containsKey(refVO.getClassId())){
                        refVO.setTeacherMobile(map.get(refVO.getClassId()).getMobile());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 组装老师或者班主任map
     * @param classIds
     * @return
     * @throws Exception
     * @throws BaseException
     */
    private Map<String,UserStudentRefVO> getTeachersByClassIds(String[] classIds) throws Exception, BaseException{
        List<UserStudentRefVO> teacherList = studentService.findTeachersByClassIds(classIds);
        if(teacherList != null && teacherList.size() > 0){
            Map<String, UserStudentRefVO> map = new HashMap<String, UserStudentRefVO>(classIds.length);
            for(UserStudentRefVO refVO : teacherList){
                if(map.containsKey(refVO.getClassId())){
                    if("班主任".equals(refVO.getPosition()) && !AssertUtil.isEmpty(refVO.getMobile())){
                        map.put(refVO.getClassId(), refVO);
                    }
                }else if(!AssertUtil.isEmpty(refVO.getMobile())){
                    map.put(refVO.getClassId(), refVO);
                }
            }
            return map;
        }
        return new HashMap<String, UserStudentRefVO>(1);
    }

    /**
     * 查询老师负责的班级
     *
     * @param userId the user id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询班级成功", faileMsg = "查询班级失败"))
    public void seachSchoolClass(@InterfaceParam(name = "userId") @Validation(must = true, name = "用户") String userId) throws Exception, BaseException {
        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        if (IndustryUtil.isEduVersion(user.getOrgId())) { //教育行业版判断
            addJsonArray("classlist", studentService.searchClassByUserId(userId, user.getOrgId()));
        }
    }

    /**
     * 学生信息详情
     *
     * @param studentId the student id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询学生信息成功", faileMsg = "查询学生信息失败"))
    public void ajaxView(@InterfaceParam(name = "studentId") @Validation(must = true, name = "学生ID") String studentId)
            throws Exception, BaseException {
        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        if (IndustryUtil.isEduVersion(user.getOrgId())) { //教育行业版判断
            TbQyStudentInfoVO infoVO = studentService.getStudentDetail(studentId);
            if (infoVO == null || !user.getOrgId().equals(infoVO.getOrgId())) {
                throw new NonePrintException("", "非本机构信息，你无权访问");
            }
            addJsonObj("studentPO", infoVO);
            List<UserStudentRefVO> parentlist = studentService.findParentsByStuId(studentId);
            addJsonArray("parentlist", parentlist);
            List<TbQyUserInfoVO> teacherList = studentService.findTeachersByClassId(infoVO.getClassId());
            addJsonArray("teacherList", teacherList);
        }
    }

    /**
     * 手机端家长编辑孩子信息
     *
     * @param studentPO the student po
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "修改学生信息成功", faileMsg = "修改学生信息失败"))
    public void editStudent(@InterfaceParam(name = "studentPO") @Validation(must = true, name = "学生信息") TbQyStudentInfoPO studentPO)
            throws Exception, BaseException {
        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        if (IndustryUtil.isEduVersion(user.getOrgId())) { //教育行业版判断
            TbQyStudentInfoPO history = studentService.searchByPk(TbQyStudentInfoPO.class, studentPO.getId());
            if (history == null || !user.getOrgId().equals(history.getOrgId())) {
                throw new NonePrintException("", "非本机构信息，你无权访问");
            }
            boolean isParent = studentService.judgementRation(user.getUserId(), studentPO.getId());
            if (!isParent) {
                throw new NonePrintException("", "你不是该学生的家长/监护人,无权修改其信息");
            }
            studentService.updatePO(studentPO, false);
        }
    }

    /**
     *查询用户的孩子信息
     * @param userId
     * @throws BaseException 这是异常啊，哥
     * @throws Exception 这是异常啊，哥
     * @author liyixin
     * @2017-1-17
     * @version 1.0
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询孩子信息成功", faileMsg = "查询孩子信息失败"))
    public void sheachChildren(@InterfaceParam(name = "userId") @Validation(must =  true, name = "userId") String userId) throws BaseException, Exception{
        UserInfoVO user = WxqyhAppContext.getCurrentUser(ServletActionContext.getRequest());
        List<ChildrenVO> childrenList = new ArrayList<ChildrenVO>();
        if (IndustryUtil.isEduVersion(user.getOrgId())) { //教育行业版判断
            childrenList = studentService.getChildrenByUserId(userId);
        }
        addJsonObj("children", childrenList);
    }
}
