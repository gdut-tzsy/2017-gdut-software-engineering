package cn.com.do1.component.contact.contact.schedule;

import cn.com.do1.common.dac.Pager;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.component.addressbook.contact.vo.SelectUserVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomItemVO;
import cn.com.do1.component.contact.contact.service.IContactCustomMgrService;
import cn.com.do1.component.contact.contact.service.IContactMgrService;
import cn.com.do1.component.contact.contact.util.ContactUtil;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.WxMessageUtil;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liyixin on 2017/5/23.
 * 用于巴东县公安局企业微信定制化需求。专门提醒自定义字段 车辆年检日期和驾驶证年检日期
 */
public class BARemindJob implements Job {
    /**
     * The constant logger.
     */
    private static final transient Logger logger = LoggerFactory.getLogger(BARemindJob.class);
    /**
     *
     */
    private static IContactCustomMgrService contactCustomService = null;
    /**
     *
     */
    private static IContactMgrService contactMgrService = null;
    /**
     * 车辆号码  0931ed98fe164876a30b37e41d917160
     * a8d9949e534746daa6017cd8c0134a1d
     */
    private static final String PLATE_NUMBER = "0931ed98fe164876a30b37e41d917160";

    /**
     * 车辆年检日期 85754a4e9d5b425a95e2ae39b54f6dc2
     * aa542d4c16b1440fa928d3bef26bef90
     */
    private static final String CAR_CHECK = "85754a4e9d5b425a95e2ae39b54f6dc2";

    /**
     * 驾驶证年检日期 dfca95f7896240c2b005b5a6df56e498
     * 50fece9f59c745e8a18846000ad8d1b2
     */
    private static final String USE_CHECK = "dfca95f7896240c2b005b5a6df56e498";
    /**
     * wx4542295a37bbcf5b
     * wx05ab253814890c89
     */
    private static final String CORP_ID = "wx4542295a37bbcf5b";
    /**
     * 48da1dde19e04d96a2b490e2d70d9e80
     * b17efb43-292e-4cc9-ac5d-0b46bce059c4
     */
    private static final String ORG_ID = "48da1dde19e04d96a2b490e2d70d9e80";

    /**
     * 提醒时间
     */
    private static final Integer remindTime = 15;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(AssertUtil.isEmpty(contactCustomService)){
            contactCustomService = DqdpAppContext.getSpringContext().getBean("contactCustomService", IContactCustomMgrService.class);
        }
        if(AssertUtil.isEmpty(contactMgrService)){
            contactMgrService = DqdpAppContext.getSpringContext().getBean("contactService", IContactMgrService.class);
        }
        Pager carPager = new Pager();
        carPager.setPageSize(500);
        carPager.setCurrentPage(1);
        Pager usePager = new Pager();
        usePager.setPageSize(500);
        usePager.setCurrentPage(1);
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        //当前时间
        Date nowDate = new Date();
        int count ;
        try {
            carPager = contactCustomService.searchCustomByOptionId(carPager, CAR_CHECK);
            List<TbQyUserCustomItemVO> carList = new ArrayList<TbQyUserCustomItemVO>((int) carPager.getTotalRows());
            count = (int) carPager.getTotalPages();
            carList.addAll(carPager.getPageData());
            //从数据库中获取非空的车检日期的自定义字段的值
            for(int i = 2; i <= count; i++){
                carPager.setCurrentPage(i);
                carPager = contactCustomService.searchCustomByOptionId(carPager, CAR_CHECK);
                carList.addAll(carPager.getPageData());
            }
            List<String> userList = new ArrayList<String>();
            for(int i= 0; i < carList.size(); i++){
                //如果用户填写的值不为空
                if(!AssertUtil.isEmpty(carList.get(i).getContent())){
                    //如果相隔时间与提醒时间一致
                    if(remindTime == DateUtil.daysBetween(nowDate, sim.parse(carList.get(i).getContent()))){
                        userList.add(carList.get(i).getUserId());
                    }
                }
            }
            //推送车辆年审日期提示信息
            if(userList.size() > 0){
                List<TbQyUserCustomItemVO> carRemindList = contactCustomService.searchByOptionIdAndUserIds(PLATE_NUMBER, userList);
                List<SelectUserVO> voList = contactMgrService.findUserByUserIds(userList);
                Map<String, SelectUserVO> map = new HashMap<String, SelectUserVO>(voList.size());
                for(int i = 0; i < voList.size(); i++){
                    map.put(voList.get(i).getUserId(), voList.get(i));
                }
                for(int i = 0; i < carRemindList.size(); i++){
                    TbQyUserCustomItemVO itemVO = carRemindList.get(i);
                    //如果车牌号码不为空
                    if(!AssertUtil.isEmpty(itemVO.getContent())){
                        String message = "巴东交警温馨提示：您所驾驶的车辆（"+itemVO.getContent()+"）即将到年检日期，请尽快到相关机构办理保险续保和车辆年检手续。违规驾驶未年检或保险过期车辆，公安机关交通管理部门将依法进行处罚。";
                        SelectUserVO vo = map.get(itemVO.getUserId());
                        //如果wxuserId不为空，而且关注状态为已关注
                        if(!AssertUtil.isEmpty(vo) && !AssertUtil.isEmpty(vo.getWxUserId()) && ContactUtil.USER_STAtUS_FOLLOW.equals(vo.getUserStatus())) {
                            WxMessageUtil.sendTextMessage(vo.getWxUserId(), message, WxAgentUtil.getAddressBookCode(), CORP_ID, ORG_ID);
                        }
                    }
                }
            }
            //初始化值
            userList.clear();
            usePager = contactCustomService.searchCustomByOptionId(usePager, USE_CHECK);
            List<TbQyUserCustomItemVO> useList = new ArrayList<TbQyUserCustomItemVO>((int) usePager.getTotalRows());
            count = (int) usePager.getTotalPages();
            useList.addAll(usePager.getPageData());
            //从数据库中获取非空的驾驶证年检日期的自定义字段的值
            for(int i = 2; i <= count; i++){
                usePager.setCurrentPage(i);
                usePager = contactCustomService.searchCustomByOptionId(usePager, USE_CHECK);
                useList.addAll(usePager.getPageData());
            }
            for(int i= 0; i < useList.size(); i++){
                //如果用户填写的值不为空
                if(!AssertUtil.isEmpty(useList.get(i).getContent())){
                    //如果相隔时间与提醒时间一致
                    if(remindTime == DateUtil.daysBetween(nowDate, sim.parse(useList.get(i).getContent()))){
                        userList.add(useList.get(i).getUserId());
                    }
                }
            }
            //推送驾驶证年检日期提示信息
            if(userList.size() > 0){
                List<SelectUserVO> voList = contactMgrService.findUserByUserIds(userList);
                for(int i = 0; i < voList.size(); i++){
                    SelectUserVO vo = voList.get(i);
                    String message = " 巴东交警温馨提示：驾驶员（"+vo.getPersonName()+"），若您持有的准驾车型是A1、A2、B1、B2驾驶证、在本记分周期内存在扣分情况的，请在记分周期到期后十五天内到驾驶登记住址所在地辖区交警大队进行审验。";
                    //如果wxuserId不为空，而且关注状态为已关注
                    if(!AssertUtil.isEmpty(vo.getWxUserId()) && ContactUtil.USER_STAtUS_FOLLOW.equals(vo.getUserStatus())) {
                        WxMessageUtil.sendTextMessage(vo.getWxUserId(), message, WxAgentUtil.getAddressBookCode(), CORP_ID, ORG_ID);
                    }
                }
            }

        } catch (BaseException e) {
            logger.error("BARemindJob 执行任务失败", e);
            ExceptionCenter.addException(e, "BARemindJob 执行任务失败", e.getMessage());
        } catch (Exception e) {
            logger.error("BARemindJob 执行任务失败", e);
            ExceptionCenter.addException(e, "BARemindJob 执行任务失败", e.getMessage());
        }
    }

}
