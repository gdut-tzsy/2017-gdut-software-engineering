package cn.com.do1.component.building.building.vo;

import java.util.List;

/**
 * Created by admin on 2017/8/29.
 */
public class BanStatisticsVo {
    private String communityName;
    private Integer redNum;
    private Integer yellowNum;
    private Integer greenNum;

    private String redPercent;
    private String yellowPercent;
    private String greenPercent;

    private List<DataVo> dateList;
    
    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Integer getRedNum() {
        return redNum;
    }

    public void setRedNum(Integer redNum) {
        this.redNum = redNum;
    }

    public Integer getYellowNum() {
        return yellowNum;
    }

    public void setYellowNum(Integer yellowNum) {
        this.yellowNum = yellowNum;
    }

    public Integer getGreenNum() {
        return greenNum;
    }

    public void setGreenNum(Integer greenNum) {
        this.greenNum = greenNum;
    }

    public String getRedPercent() {
        return redPercent;
    }

    public void setRedPercent(String redPercent) {
        this.redPercent = redPercent;
    }

    public String getYellowPercent() {
        return yellowPercent;
    }

    public void setYellowPercent(String yellowPercent) {
        this.yellowPercent = yellowPercent;
    }

    public String getGreenPercent() {
        return greenPercent;
    }

    public void setGreenPercent(String greenPercent) {
        this.greenPercent = greenPercent;
    }

    public List<DataVo> getDateList() {
        return dateList;
    }

    public void setDateList(List<DataVo> dateList) {
        this.dateList = dateList;
    }
}
