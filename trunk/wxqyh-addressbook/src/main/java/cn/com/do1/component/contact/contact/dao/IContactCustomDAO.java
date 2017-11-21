package cn.com.do1.component.contact.contact.dao;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionDesPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;

import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2016/10/25.
 */
public interface IContactCustomDAO extends IBaseDAO {

    /**
     * 返回已启用的自定义字段列表
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomOptionVO> getUseingOptionByorgId(String orgId) throws BaseException, Exception;

    /**
     * 返回该机构所有的自定义字段列表
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomOptionVO> getOptionByorgId(String orgId) throws BaseException, Exception;

    /**
     * 返回自定义字段提示语
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserOptionDesVO> getOptionDesByorgId(String orgId) throws BaseException, Exception;

    /**
     *返回自定义提示语id
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<String> getDesIdByOrgId(String orgId) throws BaseException, Exception;

    /**
     * 返回该机构的item
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomItemVO> getItemByOrgId(List<String> optionIds,String orgId) throws BaseException, Exception;

    /**
     * 机构单个用户的已启用的自定义字段的值
     * @param userId 用户id
     * @param optionIds 自定义字段id
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomItemVO> getItemByUserIdAndOrgId(String userId, List<String> optionIds, String orgId) throws BaseException, Exception;

    /**
     *分页返回该机构的自定义字段值
     * @param searchMap map数据
     * @param pager 分页数据
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-2
     * @version 1.0
     */
    Pager getOrgItemByOrgId(Pager pager, Map searchMap)throws BaseException, Exception;

    /**
     *批量删除用户自定义数据
     * @param userIds 用户id
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-14
     * @version 1.0
     */
    void deleBatchUser(String[] userIds) throws BaseException, Exception;

    /**
     *高级搜索自定义字段条件查询
     * @param searchMap
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-02-07
     * @version 1.0
     */
    List<String> searchCustom(Map searchMap) throws BaseException, Exception;

    /**
     *根据选项id查询不为空的字段值
     * @param optionId 选项id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-05-23
     * @version 1.0
     */
    Pager searchCustomByOptionId(Pager pager,String optionId) throws BaseException, Exception;

    /**
     * 根据用户id列表和选项id进行查询
     * @param optionId 选项id
     * @param userIds 用户id列表
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2017-05-23
     * @version 1.0
     */
    List<TbQyUserCustomItemVO> searchByOptionIdAndUserIds(String optionId, List<String> userIds) throws BaseException, Exception;
}
