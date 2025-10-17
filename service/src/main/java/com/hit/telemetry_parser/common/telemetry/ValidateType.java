package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 校验类型
 *
 *  @author Yang Haiyue
 *  @author 卫星技术研究所
 */
@Getter
@AllArgsConstructor
public enum ValidateType {

    /**
     * 和校验
     */
    SC("SC", "和校验", 0),

    /**
     * CRC校验
     */
    CRC("CRC", "CRC校验", 1)
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

    public static ValidateType validateType(String str) {
        for (ValidateType value : values()) {
            if (StringUtils.contains(str, value.getName())) {
                return value;
            }
        }
        throw new RuntimeException("'ValidateType' not found By " + str);
    }


    public static ValidateType getFromLabel(String str) {
        for (ValidateType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'ValidateType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(ValidateType.values())
                .collect(Collectors.toMap(ValidateType::getValue, ValidateType::getLabel));
    }
}
