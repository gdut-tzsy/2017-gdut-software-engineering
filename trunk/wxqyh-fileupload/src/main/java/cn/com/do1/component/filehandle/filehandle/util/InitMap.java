package cn.com.do1.component.filehandle.filehandle.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Description : map初始化工具
 * Created by lwj on 2017/6/12.
 */
public class InitMap {
	private final static transient Logger logger = LoggerFactory.getLogger(InitMap.class);
	private static Map<String, String> FlowStatusMap = new HashMap<String, String>();
	private static Map<String, String> dataStatusMap = new HashMap<String, String>();
	private static Map<String, String> statusDescMap = new HashMap<String, String>();
//	private static Map<String, String> WaitFlowMap = new HashMap<String, String>();



	static {
		FlowStatusMap.put("0", "上传了文件。");
		FlowStatusMap.put("1", "批阅了文件，批阅意见为：");
		FlowStatusMap.put("2", "转发了文件，转发给了：");
//		FlowStatusMap.put("3", "所有负责人已处理。"); //不必描述此状态
		FlowStatusMap.put("4_0", "转发了文件，转发给了：");
		FlowStatusMap.put("4_1", "没有转发，直接结束了流程。");

		//<!-- data-status="0"时表示待处理，字体为红色；data-status="1"时表示已完结，字体颜色为绿色；data-status="2"时表示处理中，字体颜色为橙色；data-status=“”时为灰色 -->
		dataStatusMap.put("0", "1");
		dataStatusMap.put("1", "2");
		dataStatusMap.put("2", "2");
		dataStatusMap.put("3", "");

		statusDescMap.put("0", "待处理");
		statusDescMap.put("1", "处理中");
		statusDescMap.put("2", "处理中");
		statusDescMap.put("3", "已结束");
/*
		WaitFlowMap.put("0", "等待领导批阅。");
		WaitFlowMap.put("1", "等待提单人处理。");
		WaitFlowMap.put("2", "等待部门负责人处理");
		WaitFlowMap.put("3", "已结束");*/
	}

	public static String getFlowStatusMapValue(String key) throws Exception, BaseException {
		if (AssertUtil.isEmpty(key)) {
			throw new BaseException("1001", "流程状态为空！");
		}
		String val = FlowStatusMap.get(key);
		if (AssertUtil.isEmpty(val)) {
			logger.error("暂无当前流程描述！当前流程状态为：" + key);
			throw new BaseException("1001", "暂无当前流程描述，请联系管理员！");
		}
		return val;
	}

	public static String getDataStatusMapValue(String key) throws Exception, BaseException {
		if (AssertUtil.isEmpty(key)) {
			throw new BaseException("1001", "流程状态为空！");
		}
		String val = dataStatusMap.get(key);
		if (!"3".equals(key) && AssertUtil.isEmpty(val)) {
			logger.error("暂无当前流程描述！当前流程状态为：" + key);
			throw new BaseException("1001", "暂无当前流程描述，请联系管理员！");
		}
		return val;
	}

	public static String getStatusDescMapValue(String key) throws Exception, BaseException {
		if (AssertUtil.isEmpty(key)) {
			throw new BaseException("1001", "流程状态为空！");
		}
		String val = statusDescMap.get(key);
		if (AssertUtil.isEmpty(val)) {
			logger.error("暂无当前流程描述！当前流程状态为：" + key);
			throw new BaseException("1001", "暂无当前流程描述，请联系管理员！");
		}
		return val;
	}
}
