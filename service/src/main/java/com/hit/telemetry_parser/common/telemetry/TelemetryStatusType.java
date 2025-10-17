package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 卫星技术研究所_杨海岳
 */
@Getter
@AllArgsConstructor
public enum TelemetryStatusType {
    /**
     * 未知
     */
    UNKNOWN("UNKNOWN", "未知"),
    /**
     * 正常
     */
    NORMAL("NORMAL", "正常"),
    /**
     * 异常
     */
    ERROR("ERROR", "异常");

    private final String name;
    private final String label;

    public static TelemetryStatusType telStatusType(String str) {
        for (TelemetryStatusType statusType : values()) {
            if (StringUtils.contains(str, statusType.name())) {
                return statusType;
            }
        }
        throw new RuntimeException("'getStatusType' not found By " + str);
    }

}
