package cn.com.do1.component.contact.department.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.department.model.TbDepartmentInfoPO;
import cn.com.do1.component.addressbook.department.service.IDepartmentService;
import cn.com.do1.component.wxcgiutil.WxAgentUtil;
import cn.com.do1.component.wxcgiutil.agent.AgentCacheInfo;
import cn.com.do1.dqdp.core.DqdpAppContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证应用可见范围是否合法
 * Created by sunqinghai on 16-4-28.
 *
 * @author sunqinghai
 * @date 2016 -7-4
 */
public class CheckDeptVisible {
    //private static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
    private static IDepartmentService departmentService = DqdpAppContext.getSpringContext().getBean("departmentService", IDepartmentService.class);
    private CheckDeptVisible(){

    }

    private static final String MSG = "亲爱的用户，不能对未开放权限的通讯录进行管理和同步，建议开放完整通讯录权限。" +
            "<br/><br/>指引：请在企业微信后台->【应用中心】->【授权的应用】中找到【通讯录】应用->修改【通讯录范围】为最顶级部门，修改后请耐心等待两分钟左右即可使用通讯录管理。<a target=\"_blank\" href=\"https://qy.weixin.qq.com/cgi-bin/home?lang=zh_CN#app/list\" style=\"color: #ff9600;\">马上设置</a>";

    /**
     * 验证应用可见范围
     * @return
     * @author Sun Qinghai
     * @ 16-4-28
     */
    public static String checkByAgent(TbDepartmentInfoPO parent, String corpId, String orgId, String agentCode) throws Exception, BaseException {
        if(StringUtil.isNullEmpty(agentCode)){
            return null;
        }
        if(parent == null){
            if(WxAgentUtil.isAllUserUsable(corpId, agentCode)){
                return null;
            }
            else{
                throw new NonePrintException("11",MSG);
            }
        }
        /*TbQyExperienceAgentAllVO agent = experienceapplicationService.getAgentByAgentCode(corpId, agentCode);
        if(!AssertUtil.isEmpty(agent.getPartys())){
            String [] partys = agent.getPartys().split("\\|");
            TbDepartmentInfoPO depart;
            for (String wxPId : partys) {
                if(!AssertUtil.isEmpty(wxPId)){
                    depart = departmentService.getDeptByWeixin(orgId, wxPId);
                    if(!AssertUtil.isEmpty(depart))
                        deptList.add(depart);
                }
            }
        }*/
        return null;
    }

    /**
     * 获取应用可见范围部门数据
     * @return
     * @author Sun Qinghai
     * @ 16-4-28
     */
    public static Map<String, TbDepartmentInfoPO> getDeptVisibleMap(String corpId, String orgId, String agentCode) throws Exception, BaseException {
        Map<String, TbDepartmentInfoPO> map = new HashMap<String, TbDepartmentInfoPO>();
        List<TbDepartmentInfoPO> list = getDeptVisibleList(corpId,orgId,agentCode);
        if(list !=null && list.size()>0){
            for(TbDepartmentInfoPO po:list){
                map.put(po.getDeptFullName(),po);
            }
        }
        return map;
    }

    /**
     * 获取可见范围内的部门list，返回部门list
     * @param corpId
     * @param orgId
     * @param agentCode
     * @return 只有null表示全公司可见，list的size==0表示可见部门为空
     * @throws Exception
     * @throws BaseException
     * @author sunqinghai
     * @date 2016 -7-4
     */
    public static List<TbDepartmentInfoPO> getDeptVisibleList(String corpId, String orgId, String agentCode) throws Exception, BaseException {
        if (StringUtil.isNullEmpty(agentCode)) {
            return null;
        }
        AgentCacheInfo aci = WxAgentUtil.getAgentCache(corpId,agentCode);
        if(aci == null || !aci.isTrust()){
            return null;
        }
        if (aci.isAllUserUsable()) {
            return null;
        }

        if(!AssertUtil.isEmpty(aci.getPartys())){
            return departmentService.getDeptByWxIds(orgId, aci.getPartys().split("\\|"));
        }
        return new ArrayList<TbDepartmentInfoPO>(0);
    }
}
