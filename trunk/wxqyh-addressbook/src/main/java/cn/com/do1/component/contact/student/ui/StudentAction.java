package cn.com.do1.component.contact.student.ui;

import cn.com.do1.common.annotation.bean.Validation;
import cn.com.do1.common.annotation.struts.*;
import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.FileUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.contact.util.ContactCustomUtil;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.thread.BatchImportStudentThread;
import cn.com.do1.component.contact.student.util.SchoolClassUtil;
import cn.com.do1.component.contact.student.util.StudentReportUtil;
import cn.com.do1.component.contact.student.util.StudentUitl;
import cn.com.do1.component.contact.student.vo.*;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.util.*;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by hejinjiao on 2016/11/18.
 */
public class StudentAction extends WxqyhBaseAction {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(StudentAction.class);
    /**
     * The Student service.
     */
    private IStudentService studentService;

    /**
     * The Ids.
     */
    private String ids[];

    /**
     * The Up file.
     */
    private File upFile;
    /**
     * The Up file file name.
     */
    private String upFileFileName;

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
     * Get ids string [ ].
     *
     * @return the string [ ]
     */
    public String[] getIds() {
        return ids;
    }

    /**
     * Sets ids.
     *
     * @param ids the ids
     */
    public void setIds(String[] ids) {
        this.ids = ids;
    }

    /**
     * Gets up file.
     *
     * @return the up file
     */
    public File getUpFile() {
        return upFile;
    }

    /**
     * Sets up file.
     *
     * @param upFile the up file
     */
    public void setUpFile(File upFile) {
        this.upFile = upFile;
    }

    /**
     * Gets up file file name.
     *
     * @return the up file file name
     */
    public String getUpFileFileName() {
        return upFileFileName;
    }

    /**
     * Sets up file file name.
     *
     * @param upFileFileName the up file file name
     */
    public void setUpFileFileName(String upFileFileName) {
        this.upFileFileName = upFileFileName;
    }

    /**
     * 欢迎页查询数据
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxCount() throws Exception, BaseException {
        UserOrgVO org = getUser();
        addJsonObj("depart_num", studentService.countDepartByOrgId(org.getOrgId()));
        addJsonObj("teacher_num", studentService.countUserByOrgId(org.getOrgId(), "2"));
        addJsonObj("student_num", studentService.countstudentByOrgId(org.getOrgId()));
        addJsonObj("parent_num", studentService.countUserByOrgId(org.getOrgId(), "1"));
    }

    /**
     * 查询学生信息
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @SearchValueTypes(nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "title", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "parentName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "birthday", type = "date", format = "yyyy-MM :start"),
            @SearchValueType(name = "endLeaveTime", type = "date", format = "yyyy-MM-dd :end")
    })
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
    public void searchStudent() throws Exception, BaseException {
        UserOrgVO org = getUser();
        Pager pager = getMyPager();
        getSearchValue().put("orgId", org.getOrgId());
        //判断是否为超级管理员
        if (!imSuperManager()) {
            //获取普通管理员管理的部门ids
            List<String> depatIds = UserHelper.listChildManagerDepts(org);
            if (depatIds != null) {
                //获取普通管理员的教学班级部门ids
                List<String> classIds = SchoolClassUtil.getClassDepartIds(depatIds, org.getOrgId());
                if (classIds.size() > 0) {
                    getSearchValue().put("classIds", classIds);
                } else {
                    addJsonPager("pageData", pager);
                    return;
                }
            }
        }
        pager = studentService.searchStudent(getSearchValue(), pager);
        addJsonPager("pageData", pager);
    }

    /**
     * 更新班级信息
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "更新班级成功", faileMsg = "更新班级失败"))
    public void updatDepartEdu() throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            SchoolClassUtil.updatSchoolClass(org.getOrgId());
        }
    }

    /**
     * 判断该班级部门是否已存在
     *
     * @param gradeName the grade name
     * @param className the class name
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "更新班级成功", faileMsg = "更新班级失败"))
    public void isExistClassDepart(@InterfaceParam(name = "gradeName") @Validation(must = true, name = "年级") String gradeName,
                                   @InterfaceParam(name = "className") @Validation(must = true, name = "班级") String className) throws Exception, BaseException {
        UserOrgVO org = getUser();
        TbDepartmentInfoEduPO eduPO = studentService.getDepartEduByClassName(Integer.parseInt(gradeName), Integer.parseInt(className), org.getOrgId());
        if (eduPO != null) {
            TbDepartmentInfoPO po = studentService.searchByPk(TbDepartmentInfoPO.class, eduPO.getDepartmentId());
            if (po != null && SchoolClassUtil.DEPART_CLASS.equals(po.getAttribute())) {
                addJsonObj("isExist", true);
                addJsonObj("departId", po.getId());
            } else {
                addJsonObj("isExist", false);
            }
        } else {
            addJsonObj("isExist", false);
        }
    }

    /**
     * 保存班级部门信息
     *
     * @param id          the id
     * @param departEduPO the depart edu po
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "保存班级成功", faileMsg = "保存班级失败"))
    public void addDepartEdu(@InterfaceParam(name = "id") @Validation(must = true, name = "部门id") String id,
                             @InterfaceParam(name = "departEduPO") @Validation(must = true, name = "班级") TbDepartmentInfoEduPO departEduPO)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            TbDepartmentInfoPO po = studentService.searchByPk(TbDepartmentInfoPO.class, id);
            if (po == null) {
                throw new NonePrintException("", "该班级部门已被删除");
            }
            departEduPO.setDepartmentId(id);
            departEduPO.setShowOrder(po.getShowOrder());
            departEduPO.setCreateTime(new Date());
            departEduPO.setCreatePerson(org.getUserName());
            departEduPO.setOrgId(org.getOrgId());
            //根据部门id查询班级信息
            TbDepartmentInfoEduPO eduPO = studentService.getDepartmentEduPO(id);
            if (eduPO == null) {
                //根据班级查询班级信息
                eduPO = studentService.getDepartEduByClassName(departEduPO.getGrade(), departEduPO.getClassName(), org.getOrgId());
                if (eduPO == null) {
                    departEduPO.setId(UUID32.getID());
                    studentService.insertPO(departEduPO, false);
                    return;
                }
                //修改学生班级Id
                studentService.modifyStudentClassId(eduPO.getDepartmentId(), departEduPO.getDepartmentId());
            }
            departEduPO.setId(eduPO.getId());
            studentService.updatePO(departEduPO, false);
        }
    }

    /**
     * 获取班级部门信息
     *
     * @param id the id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询班级成功", faileMsg = "查询班级失败"))
    public void getDepartEdu(@InterfaceParam(name = "id") @Validation(must = true, name = "部门id") String id) throws Exception, BaseException {
        UserOrgVO org = getUser();
        TbDepartmentInfoEduPO eduPO = studentService.getDepartmentEduPO(id);
        if (eduPO != null) {
            if (!org.getOrgId().equals(eduPO.getOrgId())) {
                throw new NonePrintException("", "非本机构信息不能查看");
            }
            addJsonObj("eduPO", eduPO);
        }
    }

    /**
     * 查询班级信息
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询成功", faileMsg = "查询失败"))
    public void searchSchoolClass() throws Exception, BaseException {
        UserOrgVO org = getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgId", org.getOrgId());
        //判断是否为超级管理员
        if (!imSuperManager()) {
            List<String> classIds = new ArrayList<String>();
            //判断普通管理员是否管理班级部门并获取班级部门ids
            if (!SchoolClassUtil.hasClassAndGetClassIds(org, classIds)) {
                addJsonArray("classList", new ArrayList());
                return;
            } else if (classIds.size() > 0) {
                map.put("classIds", classIds);
            }
        }
        List<TbDepartmentInfoEduPO> list = studentService.searchSchoolClass(map);
        addJsonArray("classList", list);
    }

    /**
     * 查询选择页面班级部门
     *
     * @param nodeId the node id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void listSchoolClass(@InterfaceParam(name = "nodeId") @Validation(must = false, name = "部门Id") String nodeId) throws Exception, BaseException {
        UserOrgVO org = getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgId", org.getOrgId());
        //判断是否为超级管理员
        if (!imSuperManager()) {
            List<String> classIds = new ArrayList<String>();
            //判断普通管理员是否管理班级部门并获取班级部门ids
            if (!SchoolClassUtil.hasClassAndGetClassIds(org, classIds)) {
                addJsonArray("orgList", new ArrayList());
                return;
            } else if (classIds.size() > 0) {
                map.put("classIds", classIds);
            }
        }
        if (!StringUtil.isNullEmpty(nodeId)) {
            map.put("nodeId", nodeId);
            addJsonArray("orgList", studentService.findClassList(map, false));
        } else {
            addJsonArray("orgList", studentService.findClassList(map, true));
        }
    }

    /**
     * 查询学生
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @SearchValueTypes(nameFormat = "false", value = {
            @SearchValueType(name = "testDate", type = "date", format = "yyyy-MM-dd HH:mm:ss"),
            @SearchValueType(name = "mobile", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "position", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "personName", type = "string", format = "%%%s%%"),
            @SearchValueType(name = "wxUserId", type = "string", format = "%%%s%%")})
    @JSONOut(catchException = @CatchException(errCode = "1001", successMsg = "查询成功", faileMsg = "查询失败"))
    public void ajaxSearch() throws Exception, BaseException {
        UserOrgVO org = getUser();
        Pager pager = getMyPager();
        getSearchValue().put("orgId", org.getOrgId());
        pager = studentService.findSchoolStudent(getSearchValue(), pager);
        addJsonPager("pageData", pager);
    }

    /**
     * 家长老师添加学生信息
     *
     * @param userId   the user id
     * @param children the children
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void addUserStudentRef(@InterfaceParam(name = "userId") @Validation(must = true, name = "家长/老师") String userId,
                                  @InterfaceParam(name = "children") @Validation(must = false, name = "孩子") String children)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            studentService.addUserStudentRef(org.getOrgId(), userId, children);
        }
    }

    /**
     * 获取孩子的信息
     *
     * @param userId the user id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void getStudentList(@InterfaceParam(name = "userId") @Validation(must = true, name = "家长/老师") String userId)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            addJsonObj("isEdu", true);
            addJsonArray("children", studentService.getStuListByParentId(userId, org.getOrgId()));
        } else {
            addJsonObj("isEdu", false);
        }
    }

    /**
     * 新增学生
     *
     * @param studentPO the student po
     * @param parents   the parents
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "新增成功", faileMsg = "新增失败"))
    public void ajaxAdd(@InterfaceParam(name = "studentPO") @Validation(must = true, name = "学生信息") TbQyStudentInfoPO studentPO,
                        @InterfaceParam(name = "parents") @Validation(must = false, name = "父母") String parents)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            boolean isRepeat = studentService.judgementRepeat(studentPO.getPersonName(), studentPO.getClassId(), studentPO.getRegisterPhone(), org.getOrgId(), null);
            if (isRepeat) {
                setActionResult("2", "你新增的学生信息已存在");
                return;
            }
            studentPO.setId(UUID32.getID());
            studentPO.setCreator(org.getUserName());
            studentPO.setOrgId(org.getOrgId());
            studentPO.setCreateTime(new Date());
            studentPO.setPinyin(PingYinUtil.getPingYin(studentPO.getPersonName()));
            studentPO.setHasParent(StudentUitl.no_parent);
            studentService.addStudnet(studentPO, parents);
        }
    }

    /**
     * 编辑学生
     *
     * @param studentPO the student po
     * @param parents   the parents
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "编辑成功", faileMsg = "编辑失败"))
    public void ajaxUpdate(@InterfaceParam(name = "studentPO") @Validation(must = true, name = "学生信息") TbQyStudentInfoPO studentPO,
                           @InterfaceParam(name = "parents") @Validation(must = false, name = "父母") String parents)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            TbQyStudentInfoPO historyPO = checkStudentViewPerm(org.getOrgId(), studentPO.getId());
            boolean isRepeat = studentService.judgementRepeat(studentPO.getPersonName(), studentPO.getClassId(), studentPO.getRegisterPhone(), org.getOrgId(), historyPO.getId());
            if (isRepeat) {
                setActionResult("1", "本班级已有学生：" + studentPO.getPersonName() + "登记了注册手机号：" + studentPO.getRegisterPhone());
                return;
            }
            studentPO.setOrgId(org.getOrgId());
            studentPO.setPinyin(PingYinUtil.getPingYin(studentPO.getPersonName()));
            studentPO.setHasParent(StudentUitl.no_parent);
            studentService.updateStudent(studentPO, parents);
        }
    }


    /**
     * 查询学生详情
     *
     * @param studentId the student id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "查询详情成功", faileMsg = "查询详情失败"))
    public void ajaxView(@InterfaceParam(name = "studentId") @Validation(must = true, name = "学生信息") String studentId)
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            TbQyStudentInfoVO infoVO = studentService.getStudentDetail(studentId);
            if (infoVO == null || !org.getOrgId().equals(infoVO.getOrgId())) {
                throw new NonePrintException("", "非本机构信息，你无权访问");
            }
            addJsonObj("studentPO", infoVO);
            addJsonArray("parentlist", studentService.findParentsByStuId(studentId));
        }
    }

    /**
     * 删除学生信息
     *
     * @param studentIds the student ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "删除成功", faileMsg = "删除失败"))
    public void ajaxBatchDelete(@InterfaceParam(name = "studentIds") @Validation(must = true, name = "学生") String studentIds[])
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId()) && studentIds != null) { //  教育行业版
            studentService.batchDeleteByIds(studentIds, org);
        }
    }

    /**
     * 同步到通讯录
     *
     * @param studentIds the student ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "同步到通讯录成功", faileMsg = "同步到通讯录失败"))
    public void ajaxSyn(@InterfaceParam(name = "studentIds") @Validation(must = true, name = "学生") String studentIds[])
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            studentService.synToAddressBook(studentIds, org);
        } else {
            setActionResult("3", "非企业教育版不能同步至通讯录");
        }
    }

    /**
     * 从通讯录移除
     *
     * @param studentIds the student ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1002", successMsg = "从通讯录移除成功", faileMsg = "从通讯录移除失败"))
    public void delSyn(@InterfaceParam(name = "studentIds") @Validation(must = true, name = "学生") String studentIds[])
            throws Exception, BaseException {
        UserOrgVO org = getUser();
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //  教育行业版
            studentService.delSynAddressBook(studentIds, org);
        } else {
            setActionResult("4", "非企业教育版不能从通讯录移除");
        }
    }

    /**
     * 导出模板（暂不用）
     *
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    public void exportTemplate() throws BaseException, Exception {
        UserOrgVO org = getUser();
        Date date = new Date();
        OutputStream os = null;
        List<TbQyUserCustomOptionVO> optionVOs = ContactCustomUtil.getUserCustomByOrgId(org.getOrgId());
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            os = response.getOutputStream();
            HttpServletRequest e = ServletActionContext.getRequest();
            String fileName = "学生信息模板" + ".xls";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/msexcel");
            HSSFWorkbook writeWB = new HSSFWorkbook();
            Sheet writeSheet;
            Row writeRow;// 行
            writeSheet = writeWB.createSheet("学生信息");// 创建页
            writeRow = writeSheet.createRow(0);// 创建行
            Map<String, Integer> titleMap = new HashMap<String, Integer>();
            StudentReportUtil.setFiledHead(writeSheet, writeRow, true, false, false, optionVOs, titleMap);
            StudentReportUtil.setFieldMark(writeWB, optionVOs, org.getOrgId(), true, false);
            writeWB.write(os);

        } catch (Exception e) {
            logger.error("导出学生模板出现异常！" + e.getMessage(), e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                logger.error("导出学生信息模板完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
            } catch (IOException e) {
                logger.error("学生信息模板,关闭io异常！！", e);
            }
        }

    }

    /**
     * 导入学生信息
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "导入成功", faileMsg = "导入失败"))
    public void batchImport() throws Exception, BaseException {
        UserOrgVO org = getUser();
        String filePath = DqdpAppContext.getAppRootPath() + File.separator + "/uploadFiles";
        File newFile = new File(filePath + File.separator + upFileFileName);
        FileUtil.copy(upFile, newFile, true);
        String id = UUID32.getID();
        //List<TbQyUserCustomOptionVO> optionVOs = ContactCustomUtil.getUserCustomByOrgId(org.getOrgId());
        Map<String, String> customMap = StudentReportUtil.getContactField(null);
        List<ImportStudentVO> list = ExcelUitl.importForExcel(upFile, upFileFileName, ImportStudentVO.class, 0, 1, customMap);
        BatchImportStudentThread thread = new BatchImportStudentThread(id, org, list, upFile, null);
        Thread t = new Thread(thread);
        t.start();
        addJsonObj("start", "0");
        addJsonObj("id", id);
    }

    /**
     * 查询导入进程
     *
     * @param id the id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "查询导入成功", faileMsg = "查询导入失败"))
    public void viewImportProcess(@InterfaceParam(name = "id") @Validation(must = true, name = "导入编码") String id) throws Exception, BaseException {
        UserOrgVO org = getUser();
        ResultVO resultVO = (ResultVO) ImportResultUtil.getErrorObject(id);
        addJsonObj("totalNum", resultVO.getTotalNum());
        addJsonObj("processNum", resultVO.getProcessNum());
        addJsonObj("errorNum", resultVO.getErrorNum());
        addJsonObj("isFinish", resultVO.isFinish());
    }

    /**
     * 导出错误学生信息
     *
     * @param id the id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "导出错误成功", faileMsg = "导出错误失败"))
    public void exportErrorStudent(@InterfaceParam(name = "id") @Validation(must = true, name = "导入编码") String id) throws Exception, BaseException {
        logger.info("***************导出学生错误信息开始");
        Date date = new Date();
        UserOrgVO org = getUser();
        ResultVO resultVO = (ResultVO) ImportResultUtil.getErrorObject(id);
        List<ImportErrorStudentVO> list = resultVO.getErrorList();
        OutputStream os = null;
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            os = response.getOutputStream();
            HttpServletRequest e = ServletActionContext.getRequest();
            String fileName = "错误数据.xls";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/msexcel");
            HSSFWorkbook writeWB = new HSSFWorkbook();
            Sheet writeSheet;
            Row writeRow;// 行
            writeSheet = writeWB.createSheet("学生错误信息");// 创建页
            writeRow = writeSheet.createRow(0);// 创建行
            Map<String, Integer> titleMap = new HashMap<String, Integer>();
            StudentReportUtil.setFiledHead(writeSheet, writeRow, true, false, true, null, titleMap);
            StudentReportUtil.setErrorExcel(writeSheet, writeRow, list, 2, titleMap);
            //写出Excel
            writeWB.write(os);
        } catch (Exception e) {
            logger.error("***************导出学生错误信息出现异常！" + e.getMessage(), e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                logger.error("***************导出学生错误信息完成，用时：" + ((new Date()).getTime() - date.getTime()) + "ms");
            } catch (IOException e) {
                logger.error("***************导出学生错误信息,关闭io异常！！", e);
            }
        }

    }

    /**
     * 检测是否有权限查看学生信息
     *
     * @param orgId     the org id
     * @param StudentId the student id
     * @return tb qy student info po
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private TbQyStudentInfoPO checkStudentViewPerm(String orgId, String StudentId) throws Exception, BaseException {
        TbQyStudentInfoPO studentPO = studentService.searchByPk(TbQyStudentInfoPO.class, StudentId);
        if (null == studentPO) {
            throw new NonePrintException("", "该学生已被删除");
        }
        if (!orgId.equals(studentPO.getOrgId())) {
            throw new NonePrintException("", "非本机构信息，你无权访问");
        }
        return studentPO;
    }

    /**
     * 初始化学生名字拼音
     *
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    @ActionRoles("systemManage")
    @JSONOut(catchException = @CatchException(errCode = "1005", successMsg = "初始化成功", faileMsg = "初始化失败"))
    public void initStudentPinyin() throws Exception, BaseException {
        studentService.initStudentPinyin();
    }
}
