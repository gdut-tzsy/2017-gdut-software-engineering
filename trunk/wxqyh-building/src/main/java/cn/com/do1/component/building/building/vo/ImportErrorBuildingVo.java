package cn.com.do1.component.building.building.vo;

import java.util.List;

/**
 * <p>ClassName: ImportErrorBuildingVo</p>
 * <p>Description: 楼栋错误vo</p>
 * <p>Author: cuijianpeng</p>
 * <p>Date: 2017年8月25日</p>
 */
public class ImportErrorBuildingVo {
	private List<TbYsjdBanImportVo> errorlist;
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

    public List<TbYsjdBanImportVo> getErrorlist() {
        return errorlist;
    }

    public void setErrorlist(List<TbYsjdBanImportVo> errorlist) {
        this.errorlist = errorlist;
    }

}
