package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserInfoPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserSecrecyPO;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.ImportVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.util.TextLength;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by hejinjiao on 2017/1/19.
 */
public class ContactImportUtil {
    private final static transient Logger logger = LoggerFactory.getLogger(ContactImportUtil.class);

    private static IContactService contactService = DqdpAppContext.getSpringContext().getBean("contactService", IContactService.class);
    private static IContactMgrService contactMgrService = DqdpAppContext.getSpringContext().getBean("contactService", IContactMgrService.class);
    /**
     * 本校教师子女:默认为否
     */
    public final static int hasnone_child = 0;
    /**
     * 判断导入数据是否有误
     *
     * @param corpId
     * @param orgId
     * @param vo
     * @param positionSet
     * @return
     * @throws Exception
     * @throws BaseException
     */
    public static String verifyUserInfo(String corpId, String orgId, ImportVO vo, Set<String> positionSet)
            throws Exception, BaseException {
        //验证本机构下是否已存在此用户（机构内手机号不能重复）
        if (StringUtil.isNullEmpty(vo.getWxUserId())) {
            return "账号信息不能为空";
        }
        if (!vo.getWxUserId().matches("^\\w+[\\w\\-_\\.@]*$")) {
            return "账号格式不正确，请重新输入！";
        }
        if (vo.getWxUserId().length() > 64) {
            return "账号长度不能超过64个字符！";
        }
        if (StringUtil.isNullEmpty(vo.getMobile()) && StringUtil.isNullEmpty(vo.getWeixinNum())
                && StringUtil.isNullEmpty(vo.getEmail())) {
            return "微信号、手机和邮箱三种信息不能同时为空";
        }
        //非法手机号
        if (!AssertUtil.isEmpty(vo.getMobile()) && !vo.getMobile().matches("^\\d{4,20}$")) {
            return "手机号错误";
        }
        //非法邮箱
        if (!AssertUtil.isEmpty(vo.getEmail()) && !vo.getEmail().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*.\\w+([-.]\\w+)*$")) {
            return "电子邮箱错误";
        }
        if (null != vo.getPosition() && TextLength.getChineseCountByDB(vo.getPosition()) > 50) {
            return "职位长度不能超过50个字！";
        }
        if(!AssertUtil.isEmpty(vo.getPosition()) && !positionSet.contains(vo.getPosition())){//如果职位不为空而且查询不到该职位
            return "该职位不存在，请重新输入";
        }

        //名字为空，非法数据
        if (AssertUtil.isEmpty(vo.getPersonName())) {
            return "名字为空";
        }
        //部门为空，非法数据
        if (AssertUtil.isEmpty(vo.getDeptFullName())) {
            return "部门为空";
        }
        List<TbQyUserInfoPO> list = contactService.findRepeatUsersByCorpId(corpId, null, vo.getMobile(), vo.getWeixinNum(), vo.getEmail());
        if (list != null && list.size() > 0) {
            TbQyUserInfoPO historUser = null;
            for (int i = 0; i < list.size(); i++) {
                historUser = list.get(i);
                if (!historUser.getWxUserId().equals(vo.getWxUserId()) || !orgId.equals(historUser.getOrgId())) {
                    break;
                }
                historUser = null;
            }
            if (historUser != null) {
                if (vo.getWxUserId().equals(historUser.getWxUserId())) {
                    if ("-1".equals(historUser.getUserStatus())) {
                        return "该账号已在离职人员中";
                    }
                    return "该账号已在通讯录中";
                }
                if (!StringUtil.isNullEmpty(vo.getMobile()) && vo.getMobile().equals(historUser.getMobile())) {
                    if ("-1".equals(historUser.getUserStatus())) {
                        return "该手机号已在离职人员中";
                    }
                    return "该手机号已在通讯录中";
                }
                if (!StringUtil.isNullEmpty(vo.getWeixinNum()) && vo.getWeixinNum().equals(historUser.getWeixinNum())) {
                    if ("-1".equals(historUser.getUserStatus())) {
                        return "该微信号已在离职人员中";
                    }
                    return "该微信号已在通讯录中";
                }
                if (!StringUtil.isNullEmpty(vo.getEmail()) && vo.getEmail().equals(historUser.getEmail())) {
                    if ("-1".equals(historUser.getUserStatus())) {
                        return "该电子邮箱已在离职人员中";
                    }
                    return "该电子邮箱已在通讯录中";
                }
            }
        }
        //maquanyang 2015-6-18 增加农历生日格式（MM-dd）验证
        if (!AssertUtil.isEmpty(vo.getLunarCalendar()) && !vo.getLunarCalendar().matches("^((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31|02-29)$")) {
            return "农历生日格式不正确，请重新输入！";
        }
        //maquanyang 2015-7-13 增加阳历生日和入职时间不是正确的时间格式是的验证
        logger.info("vo.getBirthdayStr():" + vo.getBirthdayStr());
        if (!AssertUtil.isEmpty(vo.getBirthdayStr())) {
            Date date = null;
            try {
                if (vo.getBirthdayStr().matches(ExcelUtil.checkDate2)) {//符合yyyy-MM-dd
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = format.parse(vo.getBirthdayStr() + " 00:00:00");
                } else if (vo.getBirthdayStr().matches(ExcelUtil.checkDate1)) {
                    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    date = format.parse(vo.getBirthdayStr() + " 00:00:00");
                } else {
                    return "【阳历生日】格式不正确，请参考通讯录模板说明！";
                }
            } catch (ParseException e1) {
                return "【阳历生日】格式不正确，请参考通讯录模板说明！";
            } catch (IllegalArgumentException e2) {
                return "【阳历生日】格式不正确，请参考通讯录模板说明！";
            }
        }

        logger.info("vo.getEntryTimeStr():" + vo.getEntryTimeStr());
        if (!AssertUtil.isEmpty(vo.getEntryTimeStr())) {
            Date date = null;
            try {
                if (vo.getEntryTimeStr().matches(ExcelUtil.checkDate2)) {//符合yyyy-MM-dd
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = format.parse(vo.getEntryTimeStr() + " 00:00:00");
                } else if (vo.getEntryTimeStr().matches(ExcelUtil.checkDate1)) {
                    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    date = format.parse(vo.getEntryTimeStr() + " 00:00:00");
                } else {
                    return "【入职时间】格式不正确，请参考通讯录模板说明！";
                }
            } catch (ParseException e1) {
                return "【入职时间】格式不正确，请参考通讯录模板说明！";
            } catch (IllegalArgumentException e2) {
                return "【入职时间】格式不正确，请参考通讯录模板说明！";
            }
        }
        return null;
    }

    /**
     * 用户成员性别转换：男-1，女-2
     *
     * @param sex
     * @return
     */
    public static String getUserSex(String sex) {
        if (!AssertUtil.isEmpty(sex)) {
            if ("男".equals(sex)) {
                return "1";
            } else if ("女".equals(sex)) {
                return "2";
            }
        }
        return null;
    }

    /**
     * 用户保密信息
     *
     * @param secrecy
     * @param orgId
     * @param userId
     * @throws Exception
     * @throws BaseException
     */
    public static void saveSecrecy(String secrecy, String orgId, String userId) throws Exception, BaseException {
        if (!AssertUtil.isEmpty(secrecy) && "是".equals(secrecy)) {
            TbQyUserSecrecyPO po = new TbQyUserSecrecyPO();
            po.setUserId(userId);
            po.setOrgId(orgId);
            contactMgrService.addSecrecy(po);
        }
    }

    /**
     * 成员类型：家长-1，教师/职工-2，默认-0
     *
     * @param attribute
     * @return
     * @throws Exception
     * @throws BaseException
     */
    public static Integer getUesrAttribute(String attribute) throws Exception, BaseException {
        if (!StringUtil.isNullEmpty(attribute)) {
            if ("家长".equals(attribute)) {
                return 1;
            } else if ("教师/职工".equals(attribute)) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * execl表格行头
     * @param orgId 机构id
     * @param optionVOs 自定义选项
     * @param isNeedError 是否需要错误信息
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @date 2017 -2-16
     */
    public static String[] setHead(String orgId, List<TbQyUserCustomOptionVO> optionVOs, boolean isNeedError) throws BaseException, Exception{
        //临时string
        String[] temporaryString = new String[]{"姓名", "账号", "微信号", "手机号码", "邮箱", "电话", "工作部门", "性别", "工作职位", "阳历生日", "地址", "QQ号", "身份证", "证件类型", "证件内容", "备注", "农历生日", "昵称", "电话2", "入职时间", "生日提醒","排序","保密"};
        if (IndustryUtil.isEduVersion(orgId)) {
            temporaryString = new String[]{"成员类型", "姓名", "账号", "微信号", "手机号码", "邮箱", "电话", "工作部门", "性别", "工作职位", "阳历生日", "地址", "QQ号", "身份证", "证件类型", "证件内容", "备注", "农历生日", "昵称", "电话2", "入职时间", "生日提醒", "排序", "保密"};
        }
        String[] titleTemplate = new String[temporaryString.length + optionVOs.size()];
        if(isNeedError){//如果需要错误提示
            titleTemplate = new String[temporaryString.length + optionVOs.size() + 1];
        }
        if (temporaryString.length == titleTemplate.length && !isNeedError) {//如果没有启用自定义字段 ,而且不需要错误提示
            titleTemplate = temporaryString;
        }else{
            for(int i =  0; i < temporaryString.length + optionVOs.size(); i ++){
                if (i < temporaryString.length) {//是默认字段
                    titleTemplate[i] = temporaryString[i];
                }else  {//是自定义字段
                    titleTemplate[i] = optionVOs.get(i - temporaryString.length).getOptionName();
                }
            }
        }
        if(isNeedError){
            titleTemplate[titleTemplate.length - 1] = "错误提示";
        }
        return titleTemplate;
    }

    /**
     *设置导入用户的排序号
     * @param isVip 是否是vip
     * @param userPO 要插入数据库的用户
     * @param vo 导入的vo
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @date 2017 -5-22
     */
    public static void setUpUserTop(boolean isVip, TbQyUserInfoPO userPO, ImportVO vo) throws BaseException, Exception{
        if(isVip) {//如果是vip
            if (AssertUtil.isEmpty(vo.getIsTop())) {//如果没有填排序值
                userPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
            } else {
                userPO.setIsTop(vo.getIsTop());
            }
        }else{//如果不是vip
            userPO.setIsTop(SecrecyUserUtil.DEFAULT_IS_TOP);
        }
    }
}
