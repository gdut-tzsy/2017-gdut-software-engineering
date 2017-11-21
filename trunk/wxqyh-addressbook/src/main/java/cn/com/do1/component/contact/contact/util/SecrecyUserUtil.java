package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoForPage;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserDeptInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.dqdp.core.DqdpAppContext;

import java.util.List;
import java.util.Set;

/**
 * Created by TY on 2016/8/22.
 */
public class SecrecyUserUtil {
    private static IContactMgrService contactMgrService = DqdpAppContext.getSpringContext().getBean("contactService", IContactMgrService.class) ;

    /**
     * 排序默认值
     */
    public final static Integer DEFAULT_IS_TOP = 100000;

    /**
     * 判断page中的用户是否需要保密
     *
     * @throws Exception
     * @throws BaseException
     *  @author LiYiXin 2016-8-19
     */
    public static Pager secrecyPage(String orgId, Pager pager) throws Exception, BaseException {
        Set<String> userIds = contactMgrService.getSecrecyByOrgId(orgId);
        //如果该公司有人需要保密
        if(userIds.size()>0) {
            if (null != pager.getPageData()) {
                List<TbQyUserInfoVO> tbQyUserInfoVOs = (List<TbQyUserInfoVO>) pager.getPageData();
                for (TbQyUserInfoVO tbQyUserInfoVO : tbQyUserInfoVOs) {
                    //如果这个人是需要保密的
                    if (userIds.contains(tbQyUserInfoVO.getUserId())) {
                        tbQyUserInfoVO.setWeixinNum("");
                        tbQyUserInfoVO.setMobile("");
                        tbQyUserInfoVO.setEmail("");
                    }
                }
            }
        }
        return  pager;
    }

    /**
     * 判断单个用户是否需要保密
     *
     * @throws Exception
     * @throws BaseException
     *  @author LiYiXin 2016-8-19
     */
    public static UserDeptInfoVO secrecyUserDept(UserDeptInfoVO userInfo) throws Exception, BaseException{
        boolean boo = contactMgrService.getSecrecyByUserId(userInfo.getUserId(),userInfo.getOrgId());
        //如果该用户需要保密
        if(boo){
            userInfo.setEmail("");
            userInfo.setWeixinNum("");
            userInfo.setMobile("");
        }
        return userInfo;
    }

    /**
     * 判断list用户是否需要保密
     *
     * @throws Exception
     * @throws BaseException
     *  @author LiYiXin 2016-8-19
     */
    public static List<TbQyUserInfoVO> secrecylist(UserInfoVO user, List<TbQyUserInfoVO> list)throws Exception,BaseException{
        Set<String> userIds = contactMgrService.getSecrecyByOrgId(user.getOrgId());
        //如果该公司有人需要保密
        if(userIds.size()>0){
            if(list != null) {
                for (TbQyUserInfoVO tbQyUserInfoVO : list) {
                    //如果这个人是需要保密的
                    if (userIds.contains(tbQyUserInfoVO.getUserId())) {
                        tbQyUserInfoVO.setWeixinNum("");
                        tbQyUserInfoVO.setMobile("");
                        tbQyUserInfoVO.setEmail("");
                    }
                }
            }
        }
        return  list;
    }

    /**
     * 判断列表是否有需要保密的信息
     * @param user
     * @param list
     * @return 返回数据
     * @throws Exception     这是一个异常
     * @throws BaseException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-7
     */
    public static void secrecyPageList(UserInfoVO user, List<TbQyUserInfoForPage> list)throws Exception,BaseException{
        if(AssertUtil.isEmpty(list)) {
            return;
        }
        Set<String> userIds = contactMgrService.getSecrecyByOrgId(user.getOrgId());
        //如果该公司有人需要保密
        if(AssertUtil.isEmpty(userIds)){
            return;
        }
        for (TbQyUserInfoForPage tbQyUserInfoVO : list) {
            //如果这个人是需要保密的
            if (userIds.contains(tbQyUserInfoVO.getUserId())) {
                tbQyUserInfoVO.setMobile("");
            }
        }
    }

    /**
     * 判断TbQyUserInfoVO是否需要保密
     *
     * @throws Exception
     * @throws BaseException
     *  @author LiYiXin 2016-9-12
     */
    public static TbQyUserInfoVO secrecyTbUserInfo(TbQyUserInfoVO userInfo) throws Exception, BaseException{
        boolean boo = contactMgrService.getSecrecyByUserId(userInfo.getUserId(),userInfo.getOrgId());
        //如果该用户需要保密
        if(boo){
            userInfo.setEmail("");
            userInfo.setWeixinNum("");
            userInfo.setMobile("");
        }
        return userInfo;
    }
}
