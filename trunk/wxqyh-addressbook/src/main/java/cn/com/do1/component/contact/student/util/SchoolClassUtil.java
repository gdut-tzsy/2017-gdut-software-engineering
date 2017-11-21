package cn.com.do1.component.contact.student.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyOrganizeInfo;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoEduPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.managesetting.managesetting.vo.OrgIndustryVersionVO;
import cn.com.do1.component.qwtool.qwtool.util.QwtoolUtil;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.util.UserHelper;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hejinjiao on 2016/11/19.
 */
public class SchoolClassUtil {
    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(SchoolClassUtil.class);
    /**
     * 教学班级
     */
    public final static String DEPART_CLASS = "1";
    /**
     * 正则表达式：匹配班级部门 (学校)
     */
    public final static String RegExp_SchoolDepart = "(\\d{4}级\\d{1,2}班\\((|一|二|三|四|五|六|七|八|九|高中一|高中二|高中三)年级\\d{1,2}班\\))";
    /**
     * 正则表达式：匹配班级部门（幼儿园）
     */
    public final static String RegExp_NurseryDepart = "(\\d{4}级\\d{1,2}班\\((|大|中|小)\\d{1,2}班\\))";
    /**
     * 正则表达式：匹配班级名称(学校)
     */
    public final static String RegExp_SchoolClass = "(|一|二|三|四|五|六|七|八|九|高中一|高中二|高中三)年级\\d{1,2}班";
    /**
     * 正则表达式：匹配班级名称(幼儿园)
     */
    public final static String RegExp_Nurseryclass = "(大|中|小)\\d{1,2}班";
    /**
     * 学校班级
     */
    final static String[] classArray = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "高中一", "高中二", "高中三"};
    /**
     * 幼儿园班级
     */
    final static String[] NurseryArray = {"小", "中", "大"};

    /**
     * The constant studentService.
     */
    private static IStudentService studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);

    /**
     * The constant departmentService.
     */
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    ;

    /**
     * 教学班级部门组装(通讯录部门列表：TbQyOrganizeInfo)
     *
     * @param list the list
     * @return list list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static List<TbQyOrganizeInfo> assembleEduDepartList(List<TbQyOrganizeInfo> list) throws Exception, BaseException {
        List<String> departIds = new ArrayList<String>();
        for (TbQyOrganizeInfo info : list) {
            if (DEPART_CLASS.equals(info.getAttribute())) { //教学班级
                departIds.add(info.getNodeId());
            }
        }
        if (departIds.size() > 0) {
            //获取班级信息
            Map<String, String> eduMap = assembleClassName(departIds);
            for (TbQyOrganizeInfo info : list) {
                if (eduMap.containsKey(info.getNodeId())) {
                    info.setNodeName(info.getNodeName() + eduMap.get(info.getNodeId()));
                    info.setNodeTitle(info.getNodeTitle() + eduMap.get(info.getNodeId()));
                }
            }
        }
        return list;
    }

    /**
     * 班级部门（年级）
     *
     * @param departIds the depart ids
     * @return map map
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private static Map<String, String> assembleClassName(List<String> departIds) throws Exception, BaseException {
        List<TbDepartmentInfoEduPO> edulist = studentService.seachEduDepartByDepartIds(departIds);
        if (AssertUtil.isEmpty(edulist)) {
            return new HashMap<String, String>(1);
        }
        Map<String, String> className = new HashMap<String, String>(edulist.size());
        for (TbDepartmentInfoEduPO eduPo : edulist) {
            className.put(eduPo.getDepartmentId(), "(" + eduPo.getClassFullName() + ")");
        }
        return className;
    }

    /**
     * 组装班级部门（导入：TbDepartmentInfoPO）
     *
     * @param list the list
     * @param deparmentIdMap
     * @return list
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static List<TbDepartmentInfoPO> assembleDepartmentEdu(List<TbDepartmentInfoPO> list, Map<String, TbDepartmentInfoPO> deparmentIdMap) throws Exception, BaseException {
        List<String> departIds = new ArrayList<String>();
        for (TbDepartmentInfoPO departmentPO : list) {
            deparmentIdMap.put(departmentPO.getId(), departmentPO);
            if (!AssertUtil.isEmpty(departmentPO.getAttribute()) && Integer.parseInt(DEPART_CLASS) == departmentPO.getAttribute()) { //教学班级
                departIds.add(departmentPO.getId());
            }
        }
        if (departIds.size() > 0) {
            //获取班级信息
            Map<String, String> eduMap = assembleClassName(departIds);
            for (TbDepartmentInfoPO departmentPO : list) {
                if (eduMap.containsKey(departmentPO.getId())) {
                    departmentPO.setDepartmentName(departmentPO.getDepartmentName() + eduMap.get(departmentPO.getId()));
                    departmentPO.setDeptFullName(departmentPO.getDeptFullName() + eduMap.get(departmentPO.getId()));
                }
            }
        }
        return list;
    }

    /**
     * 更新班级部门信息
     *
     * @param orgId 机构Id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static void updatSchoolClass(String orgId) throws Exception, BaseException {
        //机构行业版信息
        OrgIndustryVersionVO eduVO = IndustryUtil.getOrgEdu(orgId);
        if (eduVO != null && IndustryUtil.verEdu == eduVO.getIndustryVer()) {
            // 更新班级信息
            updateSchoolClassByOrgId(getNowYear(), eduVO.getSchoolYear(), orgId, eduVO.getSystemName());
        }
    }

    /**
     * 根据机构Id更新班级部门信息
     *
     * @param nowYear       (今年年份)
     * @param shoolYear （学校几年制）
     * @param orgId         （机构Id）
     * @param systemName    （学校学年制度）
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static void updateSchoolClassByOrgId(int nowYear, int shoolYear, String orgId, String systemName) throws Exception, BaseException {
        //获取班级部门信息
        List<TbDepartmentInfoEduPO> list = studentService.findAllClassByOrgId(orgId);
        if (list != null && list.size() > 0) {
            List<TbDepartmentInfoEduPO> newlist = new ArrayList<TbDepartmentInfoEduPO>();
            String classFullName = null;
            int grade = 0;
            for (TbDepartmentInfoEduPO eduPO : list) {
                grade = eduPO.getGrade();
                //重组班级部门信息
                if (nowYear - grade >= shoolYear) {
                    classFullName = "已毕业";
                } else if ("幼儿园".equals(systemName)) {
                    classFullName = NurseryArray[nowYear - grade] + eduPO.getClassName() + "班";
                } else {
                    classFullName = classArray[nowYear - grade] + "年级" + eduPO.getClassName() + "班";
                }
                //判断班级部门是否需要更新
                if (!classFullName.equals(eduPO.getClassFullName())) {
                    eduPO.setClassFullName(classFullName);
                    newlist.add(eduPO);
                }
            }
            //修改班级部门信息
            if (newlist.size() > 0) {
                logger.debug("********************机构Id：" + orgId + "需要升级" + newlist.size() + "个班级部门");
                QwtoolUtil.updateBatchList(newlist, false);
            }
        }
    }


    /**
     * 获取班级部门ids
     *
     * @param departIds the depart ids
     * @param orgId     the org id
     * @return class depart ids
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static List<String> getClassDepartIds(List<String> departIds, String orgId) throws Exception, BaseException {
        if (AssertUtil.isEmpty(departIds)) {
            return null;
        } else {
            List<TbDepartmentInfoPO> list = departmentService.getDeptInfo(departIds.toArray(new String[departIds.size()]));
            List<String> classIds = new ArrayList<String>();
            for (TbDepartmentInfoPO infoPO : list) {
                if ("1".equals(infoPO.getAttribute())) {
                    classIds.add(infoPO.getId());
                }
                List<String> ids = studentService.getClassIdsByDepartName(orgId, infoPO.getDeptFullName() + "->%");
                if (ids.size() > 0) {
                    classIds.addAll(ids);
                }
            }
            return classIds;
        }
    }

    /**
     * 判断该部门是否为班级部门
     *
     * @param className the class name
     * @return boolean
     */
    public static boolean judgementSchoolClass(String className) {
        boolean isSchoolClass = false;
        if (!StringUtil.isNullEmpty(className)) {
            String[] deptList = className.split("->");
            String departName = deptList[deptList.length - 1];
            String depart_name = getContentByRegExp(departName, RegExp_SchoolDepart); //学年制学校
            if (StringUtil.isNullEmpty(depart_name)) {
                depart_name = getContentByRegExp(departName, RegExp_NurseryDepart); //幼儿园
            }
            if (StringUtil.isNullEmpty(depart_name)) {
                isSchoolClass = false;
            } else if (depart_name.equals(departName)) {
                int year = Integer.parseInt(getContentByRegExp(depart_name, "\\d{4}"));
                int class_name = Integer.parseInt(getContentByRegExp(getSchoolClassName(depart_name), "\\d{1,2}"));
                int nowYear = getNowYear();
                if (nowYear - year < 0 || nowYear - year > classArray.length) {
                    return isSchoolClass;
                }
                String newDepart = year + "级" + class_name + "班(" + classArray[nowYear - year] + "年级" + class_name + "班)";
                if (newDepart.equals(departName)) {
                    isSchoolClass = true;
                } else {
                    if (nowYear - year > NurseryArray.length) {
                        return isSchoolClass;
                    } else {
                        newDepart = year + "级" + class_name + "班(" + NurseryArray[nowYear - year] + class_name + "班)";
                        if (newDepart.equals(departName)) {
                            isSchoolClass = true;
                        }
                    }
                }
            }
        }
        return isSchoolClass;
    }

    /**
     * 用正则表达式获取字符串
     *
     * @param departName the depart name
     * @param regExp     the reg exp
     * @return content by reg exp
     */
    private static String getContentByRegExp(String departName, String regExp) {
        String content = null;
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(departName);
        while (matcher.find()) {
            content = matcher.group();
        }
        return content;
    }

    /**
     * 获取班级部门的班级信息
     *
     * @param departName the depart name
     * @return school class name
     */
    public static String getSchoolClassName(String departName) {
        String classFullName = null;
        if (!StringUtil.isNullEmpty(departName)) {
            //学年制学校
            classFullName = getContentByRegExp(departName, RegExp_SchoolClass);
            if (StringUtil.isNullEmpty(classFullName)) {
                //幼儿园
                classFullName = getContentByRegExp(departName, RegExp_Nurseryclass);
            }
        }
        return classFullName;
    }

    /**
     * 班级信息map（key：grade+"_"+className, value:tbDepartmentInfoEduPO）
     * @param orgId
     * @return
     * @throws Exception
     * @throws BaseException
     */
    public static Map<String, TbDepartmentInfoEduPO> getClassMapByOrgId(String orgId) throws Exception, BaseException {
        Map<String, TbDepartmentInfoEduPO> classMap = new HashMap<String, TbDepartmentInfoEduPO>();
        List<TbDepartmentInfoEduPO> eduList = studentService.findAllClassByOrgId(orgId);
        if (eduList.size() > 0) {
            for (TbDepartmentInfoEduPO eduPO : eduList) {
                classMap.put(eduPO.getGrade() + "_" + eduPO.getClassName(), eduPO);
            }
        }
        return classMap;
    }

    /**
     * 导入新增班级信息
     *
     * @param dept     the dept
     * @param classMap
     * @param deparmentIdMap
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static String addDeptEdu(TbDepartmentInfoPO dept, Map<String, TbDepartmentInfoEduPO> classMap, Map<String, TbDepartmentInfoPO> deparmentIdMap) throws Exception, BaseException {
        //班级部门信息
        String classFullName = getSchoolClassName(dept.getDepartmentName());
        //修改部门名称和全称
        dept.setDepartmentName(dept.getDepartmentName().replace("(" + classFullName + ")", ""));
        dept.setDeptFullName(dept.getDeptFullName().replace("(" + classFullName + ")", ""));
        int grade = Integer.parseInt(dept.getDepartmentName().substring(0, 4));
        int className = 0;
        if (dept.getDepartmentName().length() == 7) {
            className = Integer.parseInt(dept.getDepartmentName().substring(5, 6));
        } else {
            className = Integer.parseInt(dept.getDepartmentName().substring(5, 7));
        }
        if (classMap.containsKey(grade + "_" + className)) {
            TbDepartmentInfoEduPO po = classMap.get(grade + "_" + className);
            //班级部门是否存在
            if (deparmentIdMap.containsKey(po.getDepartmentId()) && !dept.getId().equals(po.getDepartmentId())) {
                return "班级部门：" + po.getGrade() + "级" + po.getClassName() + "班已存在";
                //throw new BaseException("机构orgId=" + po.getOrgId() + "班级部门：" + po.getGrade() + "级" + po.getGrade() + "班已存在");
            } else {
                //修改学生班级Id
                studentService.modifyStudentClassId(po.getDepartmentId(), dept.getId());
                po.setClassFullName(classFullName);
                po.setDepartmentId(dept.getId());
                studentService.updatePO(po, false);
                classMap.put(grade + "_" + className, po);
            }
        } else {
            //组装班级部门扩展信息po
            TbDepartmentInfoEduPO po = new TbDepartmentInfoEduPO();
            po.setId(UUID32.getID());
            po.setDepartmentId(dept.getId());
            po.setClassFullName(classFullName);
            po.setOrgId(dept.getOrgId());
            po.setShowOrder(po.getShowOrder());
            po.setGrade(grade);
            po.setClassName(className);
            po.setCreatePerson(dept.getCreatePerson());
            po.setCreateTime(dept.getCreateTime());
            po.setShowOrder(dept.getShowOrder());
            studentService.insertPO(po, false);
            classMap.put(grade + "_" + className, po);
        }
        return null;
    }

    /**
     * 判断子账对象是否存在班级部门并获取班级部门ids
     *
     * @param org
     * @param classIds
     * @return
     */
    public static boolean hasClassAndGetClassIds(UserOrgVO org, List<String> classIds) throws Exception, BaseException {
        boolean hasClass;
        //获取普通管理员管理部门
        List<String> depatIds = UserHelper.listChildManagerDepts(org);
        if (depatIds != null && depatIds.size() > 0) {
            //获取普通管理员的教学班级部门ids
            List<String> ids = SchoolClassUtil.getClassDepartIds(depatIds, org.getOrgId());
            if (ids != null && ids.size() > 0) {
                classIds.addAll(ids);
                hasClass = true;
            } else {
                hasClass = false;
            }
        } else {
            hasClass = true;
        }
        return hasClass;
    }

    /**
     * 教学年份
     *
     * @return
     */
    public static int getNowYear() {
        int nowYear = Integer.parseInt(DateUtil.format(new Date(), "yyyy"));
        Date dateTime = DateUtil.parse(nowYear + "/" + Configuration.SCHOOL_UPGRADE_DATE, "yyyy/MM/dd"); //每年8月1号升级班级
        if (new Date().before(dateTime)) {
            nowYear = nowYear - 1;
        }
        return nowYear;
    }

    /**
     * 组装导出教育版部门
     *
     * @param departlist
     * @return
     */
    public static Map<String, TbDepartmentInfoPO> assembleExportDepartment(List<TbDepartmentInfoPO> departlist) throws Exception, BaseException {
        Map<String, TbDepartmentInfoPO> map = new HashMap<String, TbDepartmentInfoPO>(departlist.size());
        List<String> departIds = new ArrayList<String>(departlist.size());
        for (TbDepartmentInfoPO info : departlist) {
            if (!AssertUtil.isEmpty(info.getAttribute()) && Integer.parseInt(DEPART_CLASS) == info.getAttribute()) { //教学班级
                departIds.add(info.getId());
            }
        }
        //获取班级信息
        Map<String, String> eduMap = assembleClassName(departIds);
        for (TbDepartmentInfoPO info : departlist) {
            if (eduMap.containsKey(info.getId())) {
                info.setDepartmentName(info.getDepartmentName() + eduMap.get(info.getId()));
                info.setDeptFullName(info.getDeptFullName() + eduMap.get(info.getId()));
            }
            map.put(info.getId(), info);
        }
        return map;
    }
}
