package com.hit.telemetry_parser.service;

import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.common.telemetry.ValidateType;
import org.orekit.time.AbsoluteDate;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
public interface ITelemetryProcessService {

    /**
     * 按照unit16方式处理从TCP接口接收到的遥测数据，转化为byte数组
     * @param frameStr 原始字符串
     * @return 处理结果
     */
    byte[] process(String frameStr);

    /**
     * 解析遥测数据
     *
     * @param frame      原始帧数组
     * @param protocolId 协议id
     * @param type       类型（TTC）
     * @param date       遥测时刻
     */
    void parse(byte[] frame, Long protocolId, TTCType type, AbsoluteDate date);

    /**
     * 校验
     *
     * @param frame 原始帧数组
     * @param valid 校验值
     * @param type  校验类型
     * @return 验证结果，true表示有效，false表示无效
     */
    boolean validate(byte[] frame, String valid, ValidateType type);

    @Async("asyncServiceExecutor")
    void parseFrame(String frame);

    @Async("asyncServiceExecutor")
    void callClient();

    @Async("asyncServiceExecutor")
    void destroyClient();
}
