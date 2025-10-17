package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 进制类型
 * @author 卫星技术研究所_杨海岳
 */
@Getter
@AllArgsConstructor
public enum BaseType {
    /**
     * 未知
     */
    HEX("HEX", "16进制", 6),
    /**
     * 正常
     */
    BIN("BIN", "2进制", 2),
    /**
     * 异常
     */
    DEC("DEC", "10进制", 10);

    private final String name;
    private final String label;
    private final int value;

    public static BaseType baseType(String str) {
        for (BaseType statusType : values()) {
            if (StringUtils.contains(str, statusType.name())) {
                return statusType;
            }
        }
        throw new RuntimeException("'UnixType' not found By " + str);
    }

    public static BaseType getFromLabel(String str) {
        for (BaseType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'BaseType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(BaseType.values())
                .collect(Collectors.toMap(BaseType::getValue, BaseType::getLabel));
    }

}
