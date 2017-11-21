package cn.com.do1.component.building.building.vo;

import java.util.List;

/**
 * <p>ClassName: ImportErrorHouseVo</p>
 * <p>Description: 房屋错误vo</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月25日</p>
 */
public class ImportErrorHouseVo {
	private List<TbYsjdHouseImportVo> errorlist;
    private boolean isFinish = false;

    /**
     * @return isFinish
     */
    public boolean isFinish() {
        return isFinish;
    }

    /**
     * @param isFinish 要设置的 isFinish
     */
    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public List<TbYsjdHouseImportVo> getErrorlist() {
        return errorlist;
    }

    public void setErrorlist(List<TbYsjdHouseImportVo> errorlist) {
        this.errorlist = errorlist;
    }

}
