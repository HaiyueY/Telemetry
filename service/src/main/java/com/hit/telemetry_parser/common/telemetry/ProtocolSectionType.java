package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础参数类型
 *
 *  @author Yang Haiyue
 *  @author 卫星技术研究所
 */
@Getter
@AllArgsConstructor
public enum ProtocolSectionType {

    /**
     * 头部
     */
    HEADER("HEADER", "主导头", 0),

    /**
     * 数据区
     */
    BODY("BODY", "数据区", 1),

    /**
     * 校验区
     */
    VALID("VALID", "校验区", 2),

//    /**
//     * NID
//     */
//    NID("NID", "主类别", 3),
//
//    /**
//     * REC
//     */
//    REC("REC", "副类别", 4)
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

    public static ProtocolSectionType proFunType(String str) {
        for (ProtocolSectionType value : values()) {
            if (StringUtils.contains(str, value.name())) {
                return value;
            }
        }
        throw new RuntimeException("'ProFunType' not found By " + str);
    }

    public static ProtocolSectionType getFromLabel(String str) {
        for (ProtocolSectionType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'ProtocolSectionType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(ProtocolSectionType.values())
                .collect(Collectors.toMap(ProtocolSectionType::getValue, ProtocolSectionType::getLabel));
    }
}
