package cn.com.do1.component.contact.department.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.SeachSqlVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.addressbook.department.model.TbDepaSpecificObjPO;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.sms.sendsms.util.MD5Util;
import cn.com.do1.component.util.UUID32;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxDept;
import cn.com.do1.component.wxcgiutil.contacts.WxDeptService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by sunqinghai on 16-7-12.
 */
public class DepartmentUtil {
    private final static transient Logger logger = LoggerFactory.getLogger(DepartmentUtil.class);
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    private static IDepartmentMgrService departmentMgrService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentMgrService.class);
    //教学班级
    public final static int ATTRIBUTE_TEACHER = 1;

    public static final String ID_TYPE_DEPT = "1";

    public static final String ID_TYPE_USER = "2";

    public static final String DEPT_NAME_SPLIT = "->";

    /**
     * 获得部门信息，如果没有新建此部门
     * @param deptName
     * @param orgId
     * @param orgWxId
     * @param corpId
     * @param createPerson
     * @param agentCode
     * @return
     * @throws Exception
     * @throws BaseException
     * @author sunqinghai
     * @date 2016 -7-12
     */
    public static TbDepartmentInfoPO createDeptByDeptName(String deptName, String orgId,String orgWxId, String corpId,String createPerson,String agentCode) throws Exception, BaseException {
        List<TbDepartmentInfoPO> deptList = departmentService.getDeptByParent(orgId, null, deptName);
        TbDepartmentInfoPO rootDepartment;
        if (deptList == null || deptList.size() == 0) {
            rootDepartment = new TbDepartmentInfoPO();
            rootDepartment.setDepartmentName(deptName);
            rootDepartment.setDeptFullName(deptName);
            rootDepartment.setParentDepart("");
            rootDepartment.setCreatePerson(createPerson);
            rootDepartment.setCreateTime(new Date());
            rootDepartment.setOrgId(orgId);
            rootDepartment.setShowOrder(0);
            if(WxAgentUtil.getAddressBookCode().equals(agentCode)){
                //同步部门到微信
                WxDept wxDept = new WxDept();
                wxDept.setName(rootDepartment.getDepartmentName());
                wxDept.setParentid(orgWxId);
                wxDept = WxDeptService.addDept(wxDept, corpId, orgId);
                logger.debug("DepartmentUtil getDefaultDept 同步部门到微信corpId：" + corpId + "," + wxDept.toString());
                rootDepartment.setWxId(wxDept.getId());
                rootDepartment.setWxParentid(wxDept.getParentid());
                rootDepartment.setId(DepartmentUtil.getDeptId(corpId, wxDept.getId()));
            }
            else {
                rootDepartment.setId(UUID32.getID());
            }
            departmentService.insertPO(rootDepartment, false);
            return rootDepartment;
        } else {
            rootDepartment = deptList.get(0);
        }
        return rootDepartment;
    }

    /**
     * 获取机构一级部门id
     * @param orgId
     * @param corpId
     * @return
     * @throws Exception
     * @throws BaseException
     * @author sunqinghai
     * @date 2016 -7-14
     */
    public static List<String> getFirstDeptIds(String orgId,String corpId) throws Exception, BaseException {
        List<TbDepartmentInfoVO> list = departmentService.getFirstDepart(orgId);
        if (list.size()==0){
            return null;
        }
        List<String> deptIds = new ArrayList<String>(list.size());
        for(TbDepartmentInfoVO vo : list){
            deptIds.add(vo.getId());
        }
        return deptIds;
    }

    /**
     * 根据部门id获取部门list，去除属于子部门的情况
     * @param orgId
     * @param deptIds
     * @return
     * @throws Exception
     * @throws BaseException
     * @author sunqinghai
     * @date 2016 -7-14
     */
    public static List<TbDepartmentInfoPO> getDeptRemoveChildByDeptIds(String orgId,List<String> deptIds) throws Exception, BaseException {
        if(deptIds==null || deptIds.size()==0)
            return null;
        List<TbDepartmentInfoPO>  list = departmentService.getDeptsByIds(deptIds);
        if (list.size()==0){
            return list;
        }
        List<TbDepartmentInfoPO> fullNames = new ArrayList<TbDepartmentInfoPO>(list.size());
        String fullName;
        Iterator<TbDepartmentInfoPO> it = fullNames.iterator();
        TbDepartmentInfoPO newPO;
        fordo : for(TbDepartmentInfoPO po : list){
            fullName = po.getDeptFullName();
            if(StringUtil.isNullEmpty(fullName)){
                continue;
            }
            whiledo : while (it.hasNext()){
                newPO = it.next();
                if(fullName.startsWith(newPO.getDeptFullName()+"->")){//如果当前部门名称以在list中的部门名称开头，证明这个部门是在list中的子部门，因此需要去掉
                    it = fullNames.iterator();
                    continue fordo;
                }
                if(newPO.getDeptFullName().startsWith(fullName+"->")){//如果当前部门名称以在list中的部门名称开头，证明这个部门是在list中的子部门，因此需要去掉
                    it.remove();
                    continue whiledo;
                }
            }
            fullNames.add(po);
            it = fullNames.iterator();
        }
        return fullNames;
    }

    /**
     * 部门list中的vo转po
     * @param list
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoPO> getIntersectionDeptPO(List<TbDepartmentInfoVO> list) throws Exception {
        if(list==null || list.size()==0){
            return null;
        }
        TbDepartmentInfoPO po;
        List<TbDepartmentInfoPO> deptList = new ArrayList<TbDepartmentInfoPO>(list.size());
        for(TbDepartmentInfoVO vo :list){
            po = new TbDepartmentInfoPO();
            BeanHelper.copyProperties(po,vo);
            deptList.add(po);
        }
        return deptList;
    }

    /**
     * 获取可见范围和部门id的交集部分部门list
     * @param orgId
     * @param partys
     * @param departids
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getIntersectionDeptVO(String orgId, String partys, String departids) throws Exception {
        if(StringUtil.isNullEmpty(partys)){
            return getDeptVOByDeptId(departids);
        }
        else if(StringUtil.isNullEmpty(departids)){
            return getUsableDept(orgId,partys);
        }

        List<TbDepartmentInfoVO> usableList = getUsableDept(orgId, partys);
        List<TbDepartmentInfoVO> deptList = getDeptVOByDeptId(departids);
        return getIntersectionDeptVO(usableList,deptList);
    }

    /**
     * 获取可见范围和部门id的交集部分部门list
     * @param one
     * @param two
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getIntersectionDeptVOByTwo(String orgId, String partys, String one, String two) throws Exception {
        boolean twoIsNull = StringUtil.isNullEmpty(two);
        if (twoIsNull) {
            return getIntersectionDeptVO(orgId, partys, one);
        }
        boolean oneIsNull = StringUtil.isNullEmpty(one);
        if (oneIsNull) {
            return getIntersectionDeptVO(orgId, partys, two);
        }
        boolean partysIsNull = StringUtil.isNullEmpty(partys);
        if(partysIsNull){
            return getIntersectionDeptVOByTwo(one, two);
        }

        List<TbDepartmentInfoVO> usableList = getUsableDept(orgId, partys);
        List<TbDepartmentInfoVO> deptOneList = getDeptVOByDeptId(one);
        List<TbDepartmentInfoVO> deptTwoList = getDeptVOByDeptId(two);
        return getIntersectionDeptVO(usableList, getIntersectionDeptVO(deptOneList, deptTwoList));
    }
    /**
     * 获取可见范围和部门id的交集部分部门list
     * @param one
     * @param two
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getIntersectionDeptVOByTwo(String one, String two) throws Exception {
        if(StringUtil.isNullEmpty(one)){
            return getDeptVOByDeptId(two);
        }
        else if(StringUtil.isNullEmpty(two)){
            return getDeptVOByDeptId(one);
        }

        List<TbDepartmentInfoVO> deptOneList = getDeptVOByDeptId(one);
        List<TbDepartmentInfoVO> deptTwoList = getDeptVOByDeptId(two);
        return getIntersectionDeptVO(deptOneList, deptTwoList);
    }

    /**
     * 根据可见范围中的部门id，获取部门list
     * @param orgId
     * @param partys
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getUsableDept(String orgId, String partys) throws Exception {
        if(StringUtil.isNullEmpty(partys)){
            return null;
        }
        String [] usable = partys.split("\\|");
        List<String> wxDept = new ArrayList<String>(usable.length);
        for (String wxDeptId : usable) {
            if(!StringUtil.isNullEmpty(wxDeptId)){
                wxDept.add(wxDeptId);
            }
        }
        return departmentService.getDeptVOByWxIds(orgId,wxDept);
    }

    /**
     * 根据部门id获取部门list
     * @param departids
     * @return
     * @throws Exception
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getDeptVOByDeptId(String departids) throws Exception {
        if(StringUtil.isNullEmpty(departids)){
            return null;
        }
        String[] departIds = departids.split("\\|");
        List<String> dept = new ArrayList<String>(departIds.length);
        for (String deptId : departIds) {
            if(!StringUtil.isNullEmpty(deptId)){
                dept.add(deptId);
            }
        }
        return departmentService.getDeptVOsByIds(dept);
    }

    /**
     * 获取两个部门list中的交集部分
     * @param usable
     * @param depts
     * @return
     * @author sunqinghai
     * @date 2016 -9-5
     */
    public static List<TbDepartmentInfoVO> getIntersectionDeptVO(List<TbDepartmentInfoVO> usable,List<TbDepartmentInfoVO> depts){
        if(usable == null || usable.size()==0 || depts==null || depts.size()==0){
            return null;
        }
        int usableSize = usable.size();
        Map<String,TbDepartmentInfoVO> map = new HashMap<String, TbDepartmentInfoVO>(usableSize);
        String[] deptNames = new String[usableSize];
        TbDepartmentInfoVO deptVO;
        for(int i=0;i<usableSize;i++){
            deptVO = usable.get(i);
            map.put(deptVO.getDeptFullName(),deptVO);
            deptNames[i] = deptVO.getDeptFullName();
        }
        Set<TbDepartmentInfoVO> set = new HashSet<TbDepartmentInfoVO>(depts);
        String[] spil;
        goThis : for(TbDepartmentInfoVO vo:set){
            if(map.containsKey(vo.getDeptFullName())){//如果可见范围内有此部门
                continue;
            }
            spil = vo.getDeptFullName().split("->");
            StringBuilder sb = new StringBuilder();
            for(String s : spil){
                if(sb.length()>0){
                    sb.append("->");
                }
                sb.append(s);
                if(map.containsKey(sb.toString())){
                    continue goThis;//跳到最外层for循环
                }
            }
            //如果在可见范围内未找到此部门，直接去掉此部门
            depts.remove(vo);
            String fullName = sb.append("->").toString();
            for(String deptFullName:deptNames){
                //寻找可见范围包含在用户部门下的部门信息
                if(deptFullName.startsWith(fullName)){
                    depts.add(map.get(deptFullName));
                }
            }
        }
        return depts;
    }

    //微信接口获取用户部门为空的人员
    public final static String EMPTY_DEPT_NAME = "";
    /**
     * 因为系统必须有所属部门，当用户在无部门id时，默认到相应部门下
     * @author Sun Qinghai
     * @2016-3-13
     * @version 1.0
     */
    public static TbDepartmentInfoPO getWxEmptyDept(String orgId) throws Exception, BaseException {
        List<TbDepartmentInfoPO> deptList = departmentService.getDeptByParent(orgId, null, EMPTY_DEPT_NAME);
        if (deptList == null || deptList.size() == 0) {
            TbDepartmentInfoPO department = new TbDepartmentInfoPO();
            department.setId(UUID32.getID());
            department.setDepartmentName(EMPTY_DEPT_NAME);
            department.setDeptFullName(EMPTY_DEPT_NAME);
            department.setCreateTime(new Date());
            department.setOrgId(orgId);
            department.setShowOrder(0);
            department.setParentDepart("");
            departmentService.insertPO(department, false);
            logger.debug("dpdp添加部门corpId：" + orgId + "," + EMPTY_DEPT_NAME);
            return department;
        } else {
            return deptList.get(0);
        }
    }

    /**
     * 部门权限判断拼接语句
     * @param seachVO
     * @return
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    public static SeachSqlVO getdeptSql(SeachSqlVO seachVO){
        Map searchMap = seachVO.getSearchMap();
        List<TbDepartmentInfoVO> depts = seachVO.getDepts();
        String returnSql = "";
        StringBuffer deptSql = new StringBuffer();
        StringBuffer likeSql = new StringBuffer();
        String[] split;
        int index=0;
        for (TbDepartmentInfoVO dept : depts) {
            // 1为全公司 2为一级部门 3为本部门
            if (AssertUtil.isEmpty(dept.getPermission())) {
                dept.setPermission("1");
            }
            if ("2".equals(dept.getPermission())) {
                split = dept.getDeptFullName().split("->");
                searchMap.put("fullName"+index, split[0]);
                searchMap.put("fullNameLike"+index, split[0]+ "->%");
                deptSql.append(", :fullName"+index+" ");
                likeSql.append(" or d.dept_full_name like :fullNameLike"+index);
            } else if ("3".equals(dept.getPermission())) {
                searchMap.put("fullName"+index, dept.getDeptFullName());
                searchMap.put("fullNameLike"+index, dept.getDeptFullName()+ "->%");
                deptSql.append(", :fullName"+index+" ");
                likeSql.append(" or d.dept_full_name like :fullNameLike"+index);
            }
            index++;
        }
        if (deptSql.length() > 0) {
            deptSql = deptSql.deleteCharAt(0);
        }
        returnSql += "and (d.dept_full_name in(" + deptSql.toString() + ") " + likeSql.toString() + ")";
        logger.info("returnSql:"+returnSql);
        logger.info("searchMap:"+searchMap);
        SeachSqlVO returnSeachVO = new SeachSqlVO();
        returnSeachVO.setReturnSql(returnSql);
        returnSeachVO.setSearchMap(searchMap);
        return returnSeachVO;
    }

    /**
     * 新增部门时同步到微信
     * @param user
     * @param deptPO
     * @param org
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-21
     * @version 1.0
     */
    public static WxDept publishToWx(String user,TbDepartmentInfoPO deptPO,UserOrgVO org) throws Exception, BaseException{
        WxDept dept = new WxDept();
        dept.setName(deptPO.getDepartmentName());
        dept.setParentid(deptPO.getWxParentid());
        dept.setOrder(deptPO.getShowOrder());
        return WxDeptService.addDept(dept, org.getCorpId(),org.getOrgId());
    }

    /**
     * 设置部门权限
     * @param tbDepartmentInfoPO 当前部门
     * @param parent 父部门
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-21
     * @version 1.0
     */
    public static void setPermission(TbDepartmentInfoPO tbDepartmentInfoPO, TbDepartmentInfoPO parent) throws BaseException, Exception{
        if(!AssertUtil.isEmpty(parent.getPermission()) && !DepartmentDictUtil.PERMISSION_ALL.equals(parent.getPermission())) {//如果父部门有权限,而且权限不是所有人
            if (AssertUtil.isEmpty(tbDepartmentInfoPO.getPermission())){//如果没设权限
                throw new NonePrintException("2002", "请选择部门权限");
            } else {//如果设置的部门权限不是仅特定对象,而且父部门的权限是仅本部门或者是仅子部门，而且子部门权限比父部门大
                if (!DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission()) && (DepartmentDictUtil.PERMISSION_OWN.equals(parent.getPermission()) || DepartmentDictUtil.PERMISSION_CHILD.equals(parent.getPermission())) && tbDepartmentInfoPO.getPermission().compareTo(parent.getPermission()) < 0) {
                        tbDepartmentInfoPO.setPermission(parent.getPermission());
                }
            }
        }
    }

    /**
     * 新建仅特定对象List
     * @param ids objId列表
     * @param idType objId的类型
     * @param org 机构
     * @param departId 部门id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-21
     * @version 1.0
     */
    public static List<TbDepaSpecificObjPO> setSpecificList(String ids, String idType,  UserOrgVO org, String departId) throws BaseException, Exception{
        List<TbDepaSpecificObjPO> objList = new ArrayList<TbDepaSpecificObjPO>();
        if(!AssertUtil.isEmpty(ids)) {
            String objIds[] = ids.split("\\|");
            TbDepaSpecificObjPO po = null;
            for(int i = 0; i < objIds.length; i++){
                po = new TbDepaSpecificObjPO();
                po.setId(UUID32.getID());
                po.setObjId(objIds[i]);
                po.setCreateTime(new Date());
                po.setOrgId(org.getOrgId());
                po.setIdType(idType);
                po.setDepartId(departId);
                objList.add(po);
            }
        }
        return objList;
    }

    /**
     *更新子部门的权限
     * @param tbDepartmentInfoPO 当前要修改的部门的po
     * @param history 当前要修改的部门的历史po
     * @param org 机构
     * @param clist 当前要修改的部门的子部门的列表
     * @param isUseAll 是否应用到所有部门
     * @param deptIds 仅特定对象的部门
     * @param userIds  仅特定对象的用户
     * @param addObjList 新增的特定对象的列表
     * @param deleObjList 需要删除的特定对象的列表
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-22
     * @version 1.0
     */
    public static void updateChildrenDeptPermission(TbDepartmentInfoPO tbDepartmentInfoPO, TbDepartmentInfoPO history,  UserOrgVO org,List<TbDepartmentInfoPO> clist,String isUseAll, String deptIds, String userIds, List<TbDepaSpecificObjPO> addObjList, List<String> deleObjList ) throws BaseException, Exception{
        //如果原来的当前部门的权限是仅特定对象的
        if(!AssertUtil.isEmpty(history.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(history.getPermission())){
            List<TbDepaSpecificObjPO> temporaryList = departmentMgrService.getspecificObjByDepaId(history.getId(), org.getOrgId());
            for(int i = 0; i < temporaryList.size(); i++){
                deleObjList.add(temporaryList.get(i).getId());
            }
        }
        //如果要把当前部门的权限应用到所有的子部门
        if(!AssertUtil.isEmpty(isUseAll) && "1".equals(isUseAll) ){
            for(TbDepartmentInfoPO childrenPO : clist){
                //设置子部门权限
                childrenPO.setPermission(tbDepartmentInfoPO.getPermission());
                //如果原来子部门的是仅特定对象的
                if(!AssertUtil.isEmpty(childrenPO.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(childrenPO.getPermission())){
                    List<TbDepaSpecificObjPO> temporaryList = departmentMgrService.getspecificObjByDepaId(childrenPO.getId(), org.getOrgId());
                    for(int i = 0; i < temporaryList.size(); i++){
                        deleObjList.add(temporaryList.get(i).getId());
                    }
                }
            }
            //如果现在的是仅特定对象
            if(DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission())){
                addObjList.addAll(DepartmentUtil.setSpecificList(deptIds, DepartmentUtil.ID_TYPE_DEPT, org, history.getId()));
                addObjList.addAll(DepartmentUtil.setSpecificList(userIds, DepartmentUtil.ID_TYPE_USER, org, history.getId()));
                for(TbDepartmentInfoPO childrenPO : clist){
                    addObjList.addAll(DepartmentUtil.setSpecificList(deptIds, DepartmentUtil.ID_TYPE_DEPT, org, childrenPO.getId()));
                    addObjList.addAll(DepartmentUtil.setSpecificList(userIds, DepartmentUtil.ID_TYPE_USER, org, childrenPO.getId()));
                }
            }
        }else{//不用应用到所有的子部门,就需要检测子部门的权限
            //如果现在的是仅特定对象
            if(DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission())) {
                addObjList.addAll(DepartmentUtil.setSpecificList(deptIds, DepartmentUtil.ID_TYPE_DEPT, org, history.getId()));
                addObjList.addAll(DepartmentUtil.setSpecificList(userIds, DepartmentUtil.ID_TYPE_USER, org, history.getId()));
                for (TbDepartmentInfoPO childrenPO : clist) {
                    //如果原来的子部门的权限是空，或者是所有人，或者是仅本部门
                    if( AssertUtil.isEmpty(childrenPO.getPermission()) || DepartmentDictUtil.PERMISSION_ALL.equals(childrenPO.getPermission()) || DepartmentDictUtil.PERMISSION_OWN.equals(childrenPO.getPermission())) {
                        childrenPO.setPermission(DepartmentDictUtil.PERMISSION_CHILD);
                    }
                }
            }else { //如果现在设置的不是仅特定对象
                //如果当前部门设置的是仅子部门权限或者是仅本部门，就需要判断子部门权限是否大于当前部门
                if (DepartmentDictUtil.PERMISSION_OWN.equals(tbDepartmentInfoPO.getPermission()) || DepartmentDictUtil.PERMISSION_CHILD.equals(tbDepartmentInfoPO.getPermission())) {
                    for (TbDepartmentInfoPO childrenPO : clist) {
                        //如果原来没设置权限
                        if (AssertUtil.isEmpty(childrenPO.getPermission())) {
                            childrenPO.setPermission(tbDepartmentInfoPO.getPermission());
                        } else {//如果原来有设置权限，就判断子部门的权限是否大于当前部门
                            //如果子部门的权限不是仅特定对象，而且比当前部门权限要大
                            if (!DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(tbDepartmentInfoPO.getPermission()) && childrenPO.getPermission().compareTo(tbDepartmentInfoPO.getPermission()) < 0) {
                                childrenPO.setPermission(tbDepartmentInfoPO.getPermission());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *手机端查询的时候，有仅特定对象的部门，需要进行sql的拼装
     * @param depqrtSQL
     * @param searchMapNewMap
     * @param depts
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-23
     * @version 1.0
     */
    public static String addSqlToSelect(String depqrtSQL, Map searchMapNewMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception{
        //如果需要仅特定对象
        if(!AssertUtil.isEmpty(searchMapNewMap.get("userIds"))){
            //如果这部门的权限是仅特定部门，而且这个人是单部门，就只能仅自己可见
            if(depts.size() == 0){
                depqrtSQL = depqrtSQL + " and t.user_id in(:userIds) ";
            }else{
                depqrtSQL = depqrtSQL.substring(0, depqrtSQL.lastIndexOf(")"));
                depqrtSQL = depqrtSQL + " or t.user_id in(:userIds)) ";
            }
        }
        return depqrtSQL;
    }

    /**
     * 检查部门有没有是仅特定对象可见的
     * @param depts 部门列表
     * @param map 查询的map
     * @param user 登录的用户
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-24
     * @version 1.0
     */
    public static List<TbDepartmentInfoVO> checkDept(List<TbDepartmentInfoVO> depts, Map<String, Object> map, UserInfoVO user) throws BaseException, Exception{
        if(depts.size() > 0 ) {
            //放置特定对象的列表用户
            List<String> userIds = new ArrayList<String>();
            depts = DepartmentUtil.setSpecificDeptPermission(depts, userIds, user);
            if(userIds.size() > 0){
                map.put("userIds", userIds);
            }
        }
        return depts;
    }

    /**
     * 拼装有部门权限的查询sql
     * @param searchMapNewMap
     * @param searchMap
     * @param depts
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-24
     * @version 1.0
     */
    public static String assembleSql(Map searchMapNewMap, Map searchMap, List<TbDepartmentInfoVO> depts) throws BaseException, Exception{
        String depqrtSQL = "";
        if (depts != null && depts.size() > 0) {
            SeachSqlVO seachVO = new SeachSqlVO();
            seachVO.setDepts(depts);
            seachVO.setSearchMap(searchMap);
            SeachSqlVO retuenSeachVO = DepartmentUtil.getdeptSql(seachVO);
            searchMapNewMap = retuenSeachVO.getSearchMap();
            depqrtSQL = retuenSeachVO.getReturnSql();
        }
        depqrtSQL = DepartmentUtil.addSqlToSelect(depqrtSQL, searchMapNewMap, depts);
        return depqrtSQL;
    }

    /**
     * 设置特定部门的权限
     * @param depts 部门列表
     * @param userIds 特定对象用户列表
     * @param user 当前用户
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-27
     * @version 1.0
     */
    public static List<TbDepartmentInfoVO> setSpecificDeptPermission(List<TbDepartmentInfoVO> depts, List<String> userIds, UserInfoVO user) throws BaseException, Exception{
        //如果部门不为空，而且部门大于0，而且部门权限不为空，而且不是全公司
        if(depts != null && depts.size() > 0 && !StringUtil.isNullEmpty(depts.get(0).getPermission()) && !DepartmentDictUtil.PERMISSION_ALL.equals(depts.get(0).getPermission())){
            List<String> objDeptList = new ArrayList<String>();
            Iterator<TbDepartmentInfoVO> iterator = depts.iterator();
            while (iterator.hasNext()){
                TbDepartmentInfoVO deptVO = iterator.next();
                //如果部门权限不为空,而且权限是仅特定对象可见
                if (!AssertUtil.isEmpty(deptVO.getPermission()) && DepartmentDictUtil.PERMISSION_SPECIFIC_OBJECT.equals(deptVO.getPermission())) {
                    objDeptList.add(deptVO.getId());
                    //移除掉权限是特定对象的部门
                    iterator.remove();
                }
            }
            //如果有特定对象的部门
            if(objDeptList.size() > 0) {
                //放置原来权限是非特定对象的部门，和特定对象的列表部门
                Set<String> deptIds = new HashSet<String>();
                //放置特定对象的列表部门,用于后面，特定对象的列表部门改权限
                Set<String> objDeptIds = new HashSet<String>();
                //放置原来权限是非特定对象的部门
                Set<String> oldDeptIds = new HashSet<String>();
                List<TbDepaSpecificObjPO> objPOs = new ArrayList<TbDepaSpecificObjPO>();
                //查询特定对象的列表
                List<TbDepaSpecificObjPO> temporaryObjPOs = departmentMgrService.getspecificObjByIds(objDeptList);
                if (temporaryObjPOs.size() > 0) {//如果该部门的特定对象的列表大于0
                    objPOs.addAll(temporaryObjPOs);
                } else {//如果该部门的特定对象的列表等于0，就是仅自己可见
                    userIds.add(user.getUserId());
                }
                //遍历特定对象的列表，把相应的数据插入到相应的集合里面去
                for (TbDepaSpecificObjPO objPO : objPOs) {
                    if (DepartmentUtil.ID_TYPE_DEPT.equals(objPO.getIdType())) {//如果该类型是部门
                        deptIds.add(objPO.getObjId());
                        objDeptIds.add(objPO.getObjId());
                    } else if (DepartmentUtil.ID_TYPE_USER.equals(objPO.getIdType())) {//如果该类型是用户
                        userIds.add(objPO.getObjId());
                    }
                }
                //遍历剩下的非特定对象的部门列表，加入到集合里去
                for (TbDepartmentInfoVO dept : depts) {
                    deptIds.add(dept.getId());
                    oldDeptIds.add(dept.getId());
                }
                if (deptIds.size() > 0) {
                    List<String> deptIdlist = new ArrayList<String>(deptIds.size());
                    deptIdlist.addAll(deptIds);
                    depts.removeAll(depts);
                    depts.addAll(departmentService.getDeptVOsByIds(deptIdlist));
                    for (TbDepartmentInfoVO dept : depts) {
                        //判断该部门是不是部门里特定对象的部门
                        if (objDeptIds.contains(dept.getId()) && !oldDeptIds.contains(dept.getId())) {
                            //如果该部门是特定对象的列表部门中的一个,而且不是原来部门的，权限改为仅子部门
                            dept.setPermission(DepartmentDictUtil.PERMISSION_CHILD);
                        }
                    }
                }
            }
        }
        return depts;
    }

    /**
     * 检查是否有重复的用户，有就移除掉
     * @param personlist 返回前台的用户列表
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-27
     * @version 1.0
     */
    public static void checkRepeatUser(List<TbQyUserInfoVO> personlist) throws BaseException, Exception{
        Set<String> userIds = new HashSet<String>();
        Iterator<TbQyUserInfoVO> iterator = personlist.iterator();
        while (iterator.hasNext()){
            TbQyUserInfoVO user = iterator.next();
            //如果set里面有这个userid，移除掉
            if(userIds.contains(user.getUserId())){
                iterator.remove();
            }else{
                userIds.add(user.getUserId());
            }
        }
    }

    /**
     * 用户id分隔符
     */
    private final static String DEPT_ID_SPLIT = "_";
    public static String getDeptId(String corpId, String wxDeptId) throws UnsupportedEncodingException {
        return MD5Util.encrypt(corpId + DEPT_ID_SPLIT + wxDeptId);
    }

    /**
     * 对部门进行排序
     * @param deptlist 部门list
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-03-31
     * @version 1.0
     */
    public static List<TbDepartmentInfoVO> sortDepart(List<TbDepartmentInfoVO> deptlist) throws BaseException, Exception{
        int size = deptlist.size();
        TbDepartmentInfoVO vo;
        for(int i = 0 ; i < size; i++){
            for(int j = size - 1; j  > i; j--){
                if(!AssertUtil.isEmpty(deptlist.get(i).getShowOrder())) {
                    if (AssertUtil.isEmpty(deptlist.get(j).getShowOrder()) || (deptlist.get(i).getShowOrder() > deptlist.get(j).getShowOrder())) {
                        vo = deptlist.get(i);
                        deptlist.set(i, deptlist.get(j));
                        deptlist.set(j, vo);
                    }
                }
            }
        }
        return deptlist;
    }

    /**
     * 判断用户的部门是否变更了
     * @param orgId 机构id
     * @param userId 用户id
     * @param weixinDeps 微信的用户部门id
     * @param oldDeptIds 用于存放久部门id，避免重复查询
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -4-26
     */
    public static boolean isChangeDept(String orgId, String userId, List<Integer> weixinDeps, List<String> oldDeptIds) throws Exception, BaseException {
        //判断用户部门是否已修改，如果没有修改
        List<TbDepartmentInfoVO> userDept = departmentService.getDeptPermissionByUserId(orgId, userId);
        if (AssertUtil.isEmpty(userDept)) {
            if (weixinDeps == null && weixinDeps.size() == 0) {
                return false;
            }
        } else {
            if (weixinDeps != null && weixinDeps.size() == userDept.size()) {
                Set<String> set = new HashSet<String>(weixinDeps.size()+5);
                for (Integer e : weixinDeps)
                    set.add(e.toString());
                boolean isChangeDept = false;
                for (TbDepartmentInfoVO d : userDept) {
                    oldDeptIds.add(d.getId());
                    if (StringUtil.isNullEmpty(d.getWxId())) {
                        isChangeDept = true;
                    }
                    if (set.add(d.getWxId())) {
                        isChangeDept = true;
                    }
                }
                return isChangeDept;
            }
            else {
                for (TbDepartmentInfoVO d : userDept) {
                    oldDeptIds.add(d.getId());
                }
            }
        }
        return true;
    }

    /**
     *判断是否有部门列表，有就用部门的d.orgId，没有就用user的u.orgId
     * @param depts 部门列表
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-5-8
     * @version 1.0
     */
    public static String appOrgSql(List<TbDepartmentInfoVO> depts) throws BaseException, Exception{
        String tOrgString = " and t.org_id =:orgId  ";
        String dOrgString = " and d.org_id =:orgId  ";
        if(AssertUtil.isEmpty(depts)){//如果没有部门
            return tOrgString;
        }else{
            return dOrgString;
        }
    }
}
