package cn.com.do1.component.contact.contact.vo;

import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceAgentPO;

import java.util.Set;

/**
 * Created by sunqinghai on 16-9-19.
 */
public class AgentRange {

    private String agentCode;
    private boolean isAllRange = false;//是否全公司可见
    /**
     * 是否所有人可见
     */
    private boolean isAllUserVisible = false;
    private Set<String> deptFullNameSet;//可见范围的部门全称
    private Set<String> wxDeptId;//本地不存在的微信部门id
    private Set<String> wxUserId;//可见范围中的用户微信账号
    private Set<String> wxTagId;//可见范围中的标签
    private String suiteId;
    private TbQyExperienceAgentPO po;

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public boolean isAllRange() {
        return isAllRange;
    }

    public void setAllRange(boolean allRange) {
        isAllRange = allRange;
    }

    public Set<String> getDeptFullNameSet() {
        return deptFullNameSet;
    }

    public void setDeptFullNameSet(Set<String> deptFullNameSet) {
        this.deptFullNameSet = deptFullNameSet;
    }

    public Set<String> getWxDeptId() {
        return wxDeptId;
    }

    public void setWxDeptId(Set<String> wxDeptId) {
        this.wxDeptId = wxDeptId;
    }

    public Set<String> getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Set<String> wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Set<String> getWxTagId() {
        return wxTagId;
    }

    public void setWxTagId(Set<String> wxTagId) {
        this.wxTagId = wxTagId;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public boolean isAllUserVisible() {
        return isAllUserVisible;
    }

    public void setAllUserVisible(boolean allUserVisible) {
        isAllUserVisible = allUserVisible;
    }

    public TbQyExperienceAgentPO getPo() {
        return po;
    }

    public void setPo(TbQyExperienceAgentPO po) {
        this.po = po;
    }
}
