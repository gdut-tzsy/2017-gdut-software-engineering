package cn.com.do1.component.contact.contact.service.impl;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.framebase.dqdp.BaseService;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomItemPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionDesPO;
import cn.com.do1.component.addressbook.contact.model.TbQyUserCustomOptionPO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;
import cn.com.do1.component.contact.contact.dao.IContactCustomDAO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.util.ContactCustomUtil;
import cn.com.do1.component.util.UUID32;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by liyixin on 2016/10/25.
 */
@Service("contactCustomService")
public class ContactCustomServiceimpl extends BaseService implements IContactCustomMgrService {
    /**
     * 这是一个日志
     */
    private final static transient Logger LOGGER = LoggerFactory.getLogger(ContactCustomServiceimpl.class);

    /**
     * 这是一个私有的变量
     */
    private IContactCustomDAO contactCustomDAO;

    /**
     *
     *
     */
    @Resource
    public void setContactCustomDAO(IContactCustomDAO contactCustomDAO) {
        this.contactCustomDAO = contactCustomDAO;
        setDAO(contactCustomDAO);
    }

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
    @Override
    public List<TbQyUserCustomOptionVO> getUseingOptionByorgId(String orgId) throws BaseException, Exception {
        List<TbQyUserCustomOptionVO> optionVOs = contactCustomDAO.getUseingOptionByorgId(orgId);
        List<TbQyUserOptionDesVO> desVOs = contactCustomDAO.getOptionDesByorgId(orgId);
        optionVOs = ContactCustomUtil.setDesToOption(optionVOs, desVOs);
        return optionVOs;
    }

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
    @Override
    public List<TbQyUserCustomOptionVO> getOptionByorgId(String orgId) throws BaseException, Exception {
        List<TbQyUserCustomOptionVO> optionVOs = contactCustomDAO.getOptionByorgId(orgId);
        if(null != optionVOs && 0 != optionVOs.size()) {
            List<TbQyUserOptionDesVO> desVOs = this.getOptionDesByorgId(orgId);
            if (null != desVOs || 0 != desVOs.size()) {
                optionVOs = ContactCustomUtil.setDesToOption(optionVOs, desVOs);
            }
        }
        return optionVOs;
    }

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
    @Override
    public List<TbQyUserOptionDesVO> getOptionDesByorgId(String orgId) throws BaseException, Exception {
        return contactCustomDAO.getOptionDesByorgId(orgId);
    }

    /**
     * 批量插入该机构的初始化自定义字段数据
     * @param orgId 机构Id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-10-25
     * @version 1.0
     */
    public List<TbQyUserCustomOptionVO> batchAddOption(String orgId)throws BaseException, Exception {
        List<TbQyUserCustomOptionVO> optionVOs = new ArrayList<TbQyUserCustomOptionVO>(ContactCustomUtil.INITIAL_CAPACITY);
        List<TbQyUserCustomOptionPO> optionPOs = new ArrayList<TbQyUserCustomOptionPO>(ContactCustomUtil.INITIAL_CAPACITY);
        for (int i = 1; i<= ContactCustomUtil.INITIAL_CAPACITY; i++) {
            TbQyUserCustomOptionPO po = new TbQyUserCustomOptionPO();
            po.setId(UUID32.getID());
            po.setItemId(i);
            po.setType(ContactCustomUtil.TYPE_SINGLE);
            po.setCreateTime(new Date());
            po.setOrgId(orgId);
            po.setIsMust(ContactCustomUtil.NO_ISMUST);
            po.setStatus(ContactCustomUtil.STATUS_NO_ENABLE);
            po.setIsEdit(ContactCustomUtil.NO_IS_EDIT);
            po.setIsShow(ContactCustomUtil.NO_IS_SHOW);
            optionPOs.add(po);
        }
        //初始化插入该机构的初始自定义数据
        contactCustomDAO.execBatchInsert(optionPOs);
        //把optionPOs里的po数据放入optionVOs里
        for (TbQyUserCustomOptionPO po : optionPOs) {
            TbQyUserCustomOptionVO vo = new TbQyUserCustomOptionVO();
            vo.setId(po.getId());
            vo.setItemId(po.getItemId());
            vo.setType(po.getType());
            vo.setOrgId(po.getOrgId());
            vo.setIsMust(po.getIsMust());
            vo.setStatus(po.getStatus());
            optionVOs.add(vo);
        }
        return optionVOs;
    }

    /**
     * 批量更新联系人自定义字段和自定义字段提示语
     * @param jsonList 前台传来的json数据
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-26
     */
    public void batchUpdateOptionAndDes(JSONObject jsonList, String orgId) throws BaseException, Exception {
        List<String> desIds = contactCustomDAO.getDesIdByOrgId(orgId);
        List<TbQyUserCustomOptionPO> optionPOs = new ArrayList<TbQyUserCustomOptionPO>(ContactCustomUtil.INITIAL_CAPACITY);
        List<TbQyUserCustomOptionDesPO> desPOs = new ArrayList<TbQyUserCustomOptionDesPO>();
        ContactCustomUtil.setJsonToList(jsonList, optionPOs, desPOs, orgId);
        //如果des表中没数据
        if(null == desIds || 0 == desIds.size()) {
            if(optionPOs.size() > 0) {
                contactCustomDAO.execBatchUpdate(optionPOs, false);
            }
            if(desPOs.size()>0) {
                contactCustomDAO.execBatchInsert(desPOs);
            }
        }
        else {
            contactCustomDAO.deleteByPks(TbQyUserCustomOptionDesPO.class, desIds);
            if(optionPOs.size() > 0) {
                contactCustomDAO.execBatchUpdate(optionPOs, false);
            }
            if(desPOs.size()>0) {
                contactCustomDAO.execBatchInsert(desPOs);
            }
        }
    }

    /**
     * 返回联系人已启用的自定义字段值
     * @param userId 联系人id
     * @param orgId 机构id
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @date 2016 -10-28
     */
     public List<TbQyUserCustomOptionVO> getUserItemList(String userId, String orgId)throws BaseException, Exception {
         List<TbQyUserCustomOptionVO> optionVOs =  this.getUseingOptionByorgId(orgId);
         List<String> optionIds = new ArrayList<String>(optionVOs.size());
         List<TbQyUserCustomItemVO> itemVOs = new ArrayList<TbQyUserCustomItemVO>() ;
         //把已启用的自定义id放入list里面
         for(TbQyUserCustomOptionVO optionVO : optionVOs) {
            optionIds.add(optionVO.getId());
         }
         //查询用户已启用的自定义字段的填写的值
         if(optionIds.size() > 0) {
            itemVOs = contactCustomDAO.getItemByUserIdAndOrgId(userId, optionIds, orgId);
         }
         for(TbQyUserCustomItemVO itemVO : itemVOs) {
            for(TbQyUserCustomOptionVO optionVO : optionVOs) {
                //如果该用户的自定义字段的值属于该自定义字段，放进去
                if(itemVO.getOptionId().equals(optionVO.getId())) {
                    optionVO.setItemVO(itemVO);
                    break;
                }
            }
         }
         return optionVOs;
    }

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
     public void addUserItem(JSONObject jsonList, String userId, String orgId)throws BaseException, Exception {
         List<TbQyUserCustomItemPO> itemPOs = ContactCustomUtil.addItemToList(jsonList, userId, orgId);
         if(itemPOs.size() > 0) {
             contactCustomDAO.execBatchInsert(itemPOs);
         }
     }

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
    public void updateUserItem(JSONObject jsonList, String userId, String orgId) throws BaseException, Exception {
        List<TbQyUserCustomItemPO> addItemPOs = new ArrayList<TbQyUserCustomItemPO>();
        List<TbQyUserCustomItemPO> updateItemPOs = new ArrayList<TbQyUserCustomItemPO>();
        ContactCustomUtil.itemToList(jsonList, userId, orgId, addItemPOs, updateItemPOs);
        if(addItemPOs.size() > 0) {
            contactCustomDAO.execBatchInsert(addItemPOs);
        }
        if(updateItemPOs.size() > 0) {
            contactCustomDAO.execBatchUpdate(updateItemPOs, false);
        }
    }

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
    public Pager getOrgItemByOrgId(Pager pager, Map searchMap)throws BaseException, Exception {
        return contactCustomDAO.getOrgItemByOrgId(pager, searchMap);
    }

    /**
     * 批量新增自定义字段的值
     * @param itemPOs 批量新增的数据
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-7
     * @version 1.0
     */
    public void batchAddItem(List<TbQyUserCustomItemPO> itemPOs) throws BaseException, Exception {
        if(itemPOs.size() > 0) {
            contactCustomDAO.execBatchInsert(itemPOs);
        }
    }

    /**
     * 批量更新自定义字段的值
     * @param itemPOs 批量新增的数据
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-7
     * @version 1.0
     */
    public void batchUpdateItem(List<TbQyUserCustomItemPO> itemPOs) throws BaseException, Exception {
        if(itemPOs.size() > 0) {
            contactCustomDAO.execBatchUpdate(itemPOs, false);
        }
    }

    /**
     *批量删除用户自定义数据
     * @param userIds 用户id
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-11-14
     * @version 1.0
     */
    public void deleBatchUser(String[] userIds) throws BaseException, Exception {
        if (!AssertUtil.isEmpty(userIds) && userIds.length > 0) {
            contactCustomDAO.deleBatchUser(userIds);
        }
    }

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
    public List<String> searchCustom(Map searchMap) throws BaseException, Exception{
        return contactCustomDAO.searchCustom(searchMap);
    }

    /**
     * 根据选项id查询不为空的字段值
     *
     * @param pager
     * @param optionId 选项id
     * @return
     * @throws BaseException 这是一个异常
     * @throws Exception     这是一个异常
     * @author liyixin
     * @2017-05-23
     * @version 1.0
     */
    @Override
    public Pager searchCustomByOptionId(Pager pager, String optionId) throws BaseException, Exception {
        if(!AssertUtil.isEmpty(optionId)){
            return contactCustomDAO.searchCustomByOptionId(pager, optionId);
        }
        return pager;
    }

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
    public List<TbQyUserCustomItemVO> searchByOptionIdAndUserIds(String optionId, List<String> userIds) throws BaseException, Exception{
        if(AssertUtil.isEmpty(optionId) || AssertUtil.isEmpty(userIds)){
            return new ArrayList<TbQyUserCustomItemVO>();
        }
        return contactCustomDAO.searchByOptionIdAndUserIds(optionId, userIds);
    }
}
