package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.department.vo.TbDepartmentInfoVO;
import cn.com.do1.component.contact.student.service.IStudentService;
import cn.com.do1.component.addressbook.contact.vo.ChildrenVO;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2017/1/5.
 */
public class UserEduUtil {
    private final static transient Logger logger = LoggerFactory.getLogger(UserEduUtil.class);
    private static IStudentService studentService = DqdpAppContext.getSpringContext().getBean("studentService", IStudentService.class);
    private static final int ATTRIBUTE_PARENT = 1;

    /**
     * 教育版给家长添加孩子到家长信息中
     * @param pager 已查询出来的用户数据
     * @param depts 部门信息
     * @param user 用户信息
     * @return
     * @throws BaseException 这是异常啊，哥
     * @throws Exception 这是异常啊，哥
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    public static Pager addChildrenToVO(Pager pager, List<TbDepartmentInfoVO> depts, UserInfoVO user) throws BaseException, Exception{
        List<TbQyUserInfoVO> list = (List<TbQyUserInfoVO>) pager.getPageData();
        list = addChildrenToVO(list, depts, user);
        pager.setPageData(list);
        return  pager;
    }

    /**
     * 教育版给家长添加孩子到家长信息中
     * @param list 已查询出来的用户数据
     * @param depts 部门信息
     * @param user 用户信息
     * @return
     * @throws BaseException 这是异常啊，哥
     * @throws Exception 这是异常啊，哥
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    public static List addChildrenToVO(List<TbQyUserInfoVO> list, List<TbDepartmentInfoVO> depts, UserInfoVO user) throws BaseException, Exception{
        if(!AssertUtil.isEmpty(list) && IndustryUtil.isEduVersion(user.getOrgId()) ){//如果是教育版，而且查询出来的数据不为0
            List<String> userIds = new ArrayList<String>();
            for(TbQyUserInfoVO userInfo : list ){
                if( !AssertUtil.isEmpty(userInfo.getAttribute()) && UserEduUtil.ATTRIBUTE_PARENT == userInfo.getAttribute() ){//如果该用户是家长
                    userIds.add(userInfo.getUserId());
                }
            }
            if(userIds.size() > 0 ){//如果家长数量大于0
                Map<String, Object> searchMap = new HashMap<String, Object>();
                searchMap.put("userIds", userIds);
                searchMap.put("orgId", user.getOrgId());
                List<ChildrenVO> childrenList = studentService.findChildren(searchMap, null);
                if(childrenList.size() > 0){//如果有孩子
                    list = addChildrenToParents(childrenList, list);
                }
            }
        }
        return  list;
    }

    /**
     * 把对应的孩子放入到对应的监护人的vo里面
     * @param childrenList 孩子列表
     * @param list 查询出来的通讯录的数据（父母数量不确定）
     * @return
     * @throws BaseException 这是异常啊，哥
     * @throws Exception 这是异常啊，哥
     * @author liyixin
     * @2017-1-5
     * @version 1.0
     */
    public static List addChildrenToParents(List<ChildrenVO> childrenList, List<TbQyUserInfoVO> list) throws BaseException, Exception{
        Map<String, List<ChildrenVO>> map = new HashMap<String, List<ChildrenVO>>();
        List<ChildrenVO> addChildrenList = null;
        for(ChildrenVO childrenVO : childrenList){//把孩子信息放入到对应的监护人的id的key里面去
            String userId = childrenVO.getUserId();
            if(AssertUtil.isEmpty(map.get(userId))){//如果原来map里面没有
                addChildrenList = new ArrayList<ChildrenVO>(1);
                addChildrenList.add(childrenVO);
                map.put(userId, addChildrenList);
            }else {//如果原来map里面有
                addChildrenList = map.get(userId);
                addChildrenList.add(childrenVO);
            }
        }
        for(TbQyUserInfoVO infoVO : list){
            if( !AssertUtil.isEmpty(infoVO.getAttribute()) && UserEduUtil.ATTRIBUTE_PARENT == infoVO.getAttribute() ){//如果该用户是家长
                if(!AssertUtil.isEmpty(map.get(infoVO.getUserId()))) {//如果map中的孩子信息不为空
                    infoVO.setChildrenList(map.get(infoVO.getUserId()));
                }
            }
        }
        return list;
    }

}
