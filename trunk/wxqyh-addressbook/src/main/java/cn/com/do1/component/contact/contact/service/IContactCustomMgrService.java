package cn.com.do1.component.contact.contact.service;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.IBaseService;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomItemPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2016/10/25.
 */
public interface IContactCustomMgrService extends IBaseService {
    /**
     * 返回已启用的自定义字段列表
     *
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomOptionVO> getUseingOptionByorgId(String orgId) throws BaseException, Exception;

    /**
     * 返回该机构所有的自定义字段列表
     *
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserCustomOptionVO> getOptionByorgId(String orgId) throws BaseException, Exception;

    /**
     * 返回自定义字段提示语
     *
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     * @2016-10-24
     * @version 1.0
     */
    List<TbQyUserOptionDesVO> getOptionDesByorgId(String orgId) throws BaseException, Exception;

    /**
     * 批量插入该机构的初始化自定义字段数据
     *
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     * @2016-10-25
     * @version 1.0
     */
    List<TbQyUserCustomOptionVO> batchAddOption(String orgId)throws BaseException, Exception;


    /**
     * 批量更新联系人自定义字段和自定义字段提示语
     * @param jsonList 前台传来的json数据
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     */
    void batchUpdateOptionAndDes(JSONObject jsonList, String orgId) throws BaseException, Exception;

    /**
     * 返回联系人已启用的自定义字段值
     * @param userId 联系人id
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-28
     */
    List<TbQyUserCustomOptionVO> getUserItemList(String userId, String orgId)throws BaseException, Exception;

    /**
     * 新增用户自定义数据
     *@param jsonList 前台传来的json数据
     * @param userId 联系人id
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -11-1
     */
    void addUserItem(JSONObject jsonList, String userId, String orgId)throws BaseException, Exception;

    /**
     *更新用户自定义数据
     *@param jsonList 前台传来的json数据
     * @param userId 联系人id
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -11-1
     */
    void updateUserItem(JSONObject jsonList, String userId, String orgId) throws BaseException, Exception;

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
     * 批量新增自定义字段的值
     * @param itemPOs 批量新增的数据
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-7
     * @version 1.0
     */
    void batchAddItem(List<TbQyUserCustomItemPO> itemPOs) throws BaseException, Exception;

    /**
     * 批量更新自定义字段的值
     * @param itemPOs 批量新增的数据
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-7
     * @version 1.0
     */
    void batchUpdateItem(List<TbQyUserCustomItemPO> itemPOs) throws BaseException, Exception;

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
