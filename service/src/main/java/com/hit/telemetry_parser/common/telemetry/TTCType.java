package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
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
public enum TTCType  {

    /**
     * 和校验
     */
    COMMAND("COMMAND", "遥控", 0),

    /**
     * CRC校验
     */
    TELEMETRY("TELEMETRY", "遥测", 1),

    /**
     * CRC校验
     */
    TRANSMISSION("TRANSMISSION", "数传", 2)
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

    public static TTCType ttcType(String str) {
        for (TTCType value : values()) {
            if (StringUtils.contains(str, value.getName())) {
                return value;
            }
        }
        throw new RuntimeException("'TTCType' not found By " + str);
    }

    public static TTCType getFromLabel(String str) {
        for (TTCType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'TTCType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(TTCType.values())
                .collect(Collectors.toMap(TTCType::getValue, TTCType::getLabel));
    }
}
