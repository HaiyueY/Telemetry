package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 协议分级类型
 *
 *  @author Yang Haiyue
 *  @author 卫星技术研究所
 */
@Getter
@AllArgsConstructor
public enum CategoryType {

    SATELLITE("SATELLITE", "卫星", 0),

    SUBSYSTEM("SUBSYSTEM", "分系统", 1),

    PROTOCOL("PROTOCOL", "测控协议", 2),

    PARAMETER("PARAMETER", "协议参数", 3)
    ;
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     */
    private final String label;
    /**
     * 值
     */
    private final Integer value;

    public static CategoryType ttcType(String str) {
        for (CategoryType value : values()) {
            if (StringUtils.contains(str, value.getName())) {
                return value;
            }
        }
        throw new RuntimeException("'CategoryType' not found By " + str);
    }

    public static CategoryType getFromLabel(String str) {
        for (CategoryType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'CategoryType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(CategoryType.values())
                .collect(Collectors.toMap(CategoryType::getValue, CategoryType::getLabel));
    }
}
