package cn.com.do1.component.contact.student.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.reflation.BeanHelper;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.common.util.PingYinUtil;
import cn.com.do1.component.contact.student.model.TbQyStudentInfoPO;
import cn.com.do1.component.contact.student.model.TbQyUserStudentRefPO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.contact.student.vo.UserStudentRefVO;
import cn.com.do1.component.util.ListUtil;
import cn.com.do1.component.wxcgiutil.contacts.WxUser;
import cn.com.do1.component.wxcgiutil.contacts.WxUserService;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by hejinjiao on 2017/1/13.
 */
public class StudentSynUtil {

    /**
     * The constant logger.
     */
    private final static transient Logger logger = LoggerFactory.getLogger(StudentSynUtil.class);

    /**
     * The constant studentService.
     */
    private static IStudentService studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);

    /**
     * The constant contactService.
     */
    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);

    /**
     * 组装学生信息同步到通讯录
     *
     * @param stuPO    the stu po
     * @param departPO the depart po
     * @param corpId   the corp id
     * @param uplist   the uplist
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static void synStudent(TbQyStudentInfoPO stuPO, TbDepartmentInfoPO departPO, String corpId,
                                  List<TbQyStudentInfoPO> uplist) throws Exception, BaseException {
        TbQyUserInfoPO userInfo = new TbQyUserInfoPO();
        BeanHelper.copyFormatProperties(userInfo, stuPO, true);
        //学生手机号、微信号、邮箱不能同时为空
        if (!AssertUtil.isEmpty(userInfo.getMobile())) {
            userInfo.setWxUserId(userInfo.getMobile());
        }
        if (AssertUtil.isEmpty(userInfo.getWxUserId()) && !AssertUtil.isEmpty(userInfo.getWeixinNum())) {
            userInfo.setWxUserId(userInfo.getWeixinNum());
        }
        if (AssertUtil.isEmpty(userInfo.getWxUserId()) && !AssertUtil.isEmpty(userInfo.getEmail())) {
            userInfo.setWxUserId(userInfo.getEmail());
        }
        if (AssertUtil.isEmpty(userInfo.getWxUserId())) {
            throw new NonePrintException("", "学生手机号、微信号、邮箱必须填写一个");
        }
        userInfo.setUserId(stuPO.getId());
        userInfo.setPinyin(PingYinUtil.getPingYin(userInfo.getPersonName()));
        userInfo.setCorpId(corpId);
        userInfo.setUserStatus("0");
        userInfo.setIsConcerned("0");
        userInfo.setHeadPic("0");
        userInfo.setRemindType("1");
        //同步到企业微信
        publishToWx(userInfo, departPO.getWxId(), corpId);
        List<String> detId = new ArrayList<String>();
        detId.add(departPO.getId());
        //同步到企微通讯录
        contactService.insertUser(userInfo, detId);
        stuPO.setIsSyn(StudentUitl.is_syn);
        uplist.add(stuPO);
    }

    /**
     * 同步到微信
     *
     * @param userInfo the user info
     * @param wxId     the wx id
     * @param corpId   the corp id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    private static void publishToWx(TbQyUserInfoPO userInfo, String wxId, String corpId) throws Exception, BaseException {
        WxUser user = new WxUser();
        user.setUserid(userInfo.getWxUserId());
        user.setName(userInfo.getPersonName());
        user.setEmail(userInfo.getEmail());
        user.setGender(userInfo.getSex());
        user.setMobile(userInfo.getMobile());
        user.setPosition(userInfo.getPosition());
        user.setWeixinid(userInfo.getWeixinNum());
        List<String> str = new ArrayList<String>();
        str.add(wxId);
        user.setDepartment(str);
        WxUserService.addUser(user, corpId, userInfo.getOrgId());
    }

    /**
     * 删除用户后更新学生信息
     *
     * @param userIds the user ids
     * @param orgId   the org id
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static void updateStudentByUserIds(String[] userIds, String orgId) throws Exception, BaseException {
        if (!AssertUtil.isEmpty(userIds)) {
            //学生-父母关联表信息
            List<TbQyUserStudentRefPO> reflist = studentService.findStudentsByParents(userIds);
            if (reflist.size() > 0) {
                //删除关联表信息
                studentService.delStudentRefByParents(userIds);
                Set<String> stuIdSet = new HashSet<String>();
                for (TbQyUserStudentRefPO refPO : reflist) {
                    stuIdSet.add(refPO.getStudentId());
                }
                List<String> stuIds = new ArrayList<String>(stuIdSet);
                //修改学生是否存在父母
                Map<String, List<UserStudentRefVO>> listMap = getUserStudentRefMap(stuIds);
                if (!AssertUtil.isEmpty(listMap)) {
                    for (String stuId : stuIds) {
                        if (listMap.containsKey(stuId)) {
                            stuIdSet.remove(stuId);
                        }
                    }
                }
                if (stuIdSet.size() > 0) {
                    studentService.updateHasParent(ListUtil.toArrays(new ArrayList<String>(stuIdSet)), orgId, StudentUitl.no_parent);
                }
            }
            //用户是否为学生，修改同步信息
            studentService.updateStudentSyn(userIds, orgId, StudentUitl.no_syn);
        }
    }

    /**
     * 组装父母监护人map.
     *
     * @param stuIds the stu ids
     * @return user student ref map
     * @throws Exception     the exception
     * @throws BaseException the base exception
     */
    public static Map<String, List<UserStudentRefVO>> getUserStudentRefMap(List<String> stuIds) throws Exception, BaseException {
        List<UserStudentRefVO> list = studentService.getParentsByStuIds(stuIds);
        if (list.size() == 0) {
            return new HashMap<String, List<UserStudentRefVO>>(1);
        }
        Map<String, List<UserStudentRefVO>> map = new HashMap<String, List<UserStudentRefVO>>(stuIds.size());
        List<UserStudentRefVO> refList;
        for (UserStudentRefVO vo : list) {
            if (map.containsKey(vo.getStudentId())) {
                map.get(vo.getStudentId()).add(vo);
            } else {
                refList = new ArrayList<UserStudentRefVO>();
                refList.add(vo);
                map.put(vo.getStudentId(), refList);
            }
        }
        return map;
    }

}
