package cn.com.do1.component.contact.student.vo;

import java.util.List;

/**
 * Created by hejinjiao on 2016/11/28.
 */
public class ResultVO {
    /**
     * The Error list.
     */
    private List<ImportErrorStudentVO> errorList;

    /**
     * The Total num.
     */
    private int totalNum = 0;
    /**
     * The Process num.
     */
    private int processNum = 0;
    /**
     * The Is finish.
     */
    private boolean isFinish = false;

    /**
     * The Error num.
     */
    private int errorNum = 0;

    /**
     * Gets error list.
     *
     * @return the error list
     */
    public List<ImportErrorStudentVO> getErrorList() {
        return errorList;
    }

    /**
     * Sets error list.
     *
     * @param errorList the error list
     */
    public void setErrorList(List<ImportErrorStudentVO> errorList) {
        this.errorList = errorList;
    }

    /**
     * Gets total num.
     *
     * @return the total num
     */
    public int getTotalNum() {
        return totalNum;
    }

    /**
     * Sets total num.
     *
     * @param totalNum the total num
     */
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * Gets process num.
     *
     * @return the process num
     */
    public int getProcessNum() {
        return processNum;
    }

    /**
     * Sets process num.
     *
     * @param processNum the process num
     */
    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }

    /**
     * Is finish boolean.
     *
     * @return the boolean
     */
    public boolean isFinish() {
        return isFinish;
    }

    /**
     * Sets finish.
     *
     * @param finish the finish
     */
    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    /**
     * Gets error num.
     *
     * @return the error num
     */
    public int getErrorNum() {
        return errorNum;
    }

    /**
     * Sets error num.
     *
     * @param errorNum the error num
     */
    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }
}
