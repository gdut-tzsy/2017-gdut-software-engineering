package cn.com.do1.component.contact.tag.po;

import cn.com.do1.common.framebase.dqdp.IBaseDBVO;

/**
 * Created by sunqinghai on 17/2/9.
 */
public class TbQyTagSyncTimePO implements IBaseDBVO {

    private String id ;
    private Integer syncDate;
    private Integer syncTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Integer syncDate) {
        this.syncDate = syncDate;
    }

    public Integer getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Integer syncTime) {
        this.syncTime = syncTime;
    }

    /**
     * 获取数据库中对应的表名
     *
     * @return
     */
    public String _getTableName() {
        return "tb_qy_tag_sync_time";
    }

    /**
     * 获取对应表的主键字段名称
     *
     * @return
     */
    public String _getPKColumnName() {
        return "id";
    }

    /**
     * 获取主键值
     *
     * @return
     */
    public String _getPKValue() {
        return String.valueOf(id);
    }

    /**
     * 设置主键的值
     *
     * @return
     */
    public void _setPKValue(Object value) {
        this.id=(String)value;
    }
}
