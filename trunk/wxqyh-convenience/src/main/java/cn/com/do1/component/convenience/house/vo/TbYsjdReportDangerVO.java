package cn.com.do1.component.convenience.house.vo;

import cn.com.do1.common.annotation.bean.FormatMask;

/**
 * Created by admin on 2017/8/22.
 */
public class TbYsjdReportDangerVO {
    private String id;
    private String describes;
    @FormatMask(type = "date", value = "yyyy-MM-dd HH:mm")
    private String report_time;
    private String status;
    private String lightStatus;
    private String reportType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(String lightStatus) {
        this.lightStatus = lightStatus;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
