package cn.com.do1.component.building.building.vo;

/**
 * <p>ClassName: CommunityVo</p>
 * <p>Description: 社区和网格Vo</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月18日</p>
 */
public class CommunityVo {
    /**
     * <p>Field id: 主键</p>
     */
    String id;
    
    /**
     * <p>Field departmentName: 名称</p>
     */
    String departmentName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
}
