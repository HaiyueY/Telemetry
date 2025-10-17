package com.hit.telemetry_parser.common;

/**
 * @author Yang_Haiyue
 * @Description 统一定义过程状态
 * @company 卫星技术研究所_哈尔滨工业大学
 * @create 2023/4/12 11:09
 */
public interface ProcessStatus {
	/**
	 * 未开始
	 */
	String NOT_STARTED = "0";

	/**
	 * 未结束
	 */
	String NOT_FINISHED = "1";

	/**
	 * 成功
	 */
	String SUCCEED = "2";

	/**
	 * 失败
	 */
	String FAILED = "3";
}
