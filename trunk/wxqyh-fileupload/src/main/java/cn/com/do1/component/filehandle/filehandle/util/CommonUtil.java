package cn.com.do1.component.filehandle.filehandle.util;

import cn.com.do1.common.exception.BaseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created by lwj on 2017-9-7.
 */
public class CommonUtil {

	/**
	 * @description 去掉数组重复的元素
	 * @method removeRepeatElm
	 * @Param list
	 * @Return
	 * @time 2017-9-7
	 * @author lwj
	 * @version V1.0.0
	 */
	public static List<String> removeRepeatElm(List<String> list) throws Exception, BaseException {
		if (list == null || list.size() <= 0) {
			return list;
		}
		List<String> newList = new ArrayList<String>();
		for (String cd : list) {
			if (!newList.contains(cd)) {
				newList.add(cd);
			}
		}
		return newList;
	}
}
