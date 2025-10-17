package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接收模式
 * @author 卫星技术研究所_杨海岳
 */
@Getter
@AllArgsConstructor
public enum ReceiveType {
    /**
     * 实时
     */
    REALTIME("REALTIME", "实时接收", 0),
    /**
     * 查询
     */
    HISTORY("HISTORY", "历史查询", 1)
    ;

    private final String name;
    private final String label;
    /**
     * 值
     */
    private final Integer value;

    public static ReceiveType receiveType(String str) {
        for (ReceiveType value : values()) {
            if (StringUtils.contains(str, value.name())) {
                return value;
            }
        }
        throw new RuntimeException("'receiveType' not found By " + str);
    }

    public static ReceiveType getFromLabel(String str) {
        for (ReceiveType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'ReceiveType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(ReceiveType.values())
                .collect(Collectors.toMap(ReceiveType::getValue, ReceiveType::getLabel));
    }

}
