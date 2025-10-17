package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态变量类型
 *
 *  @author Yang Haiyue
 *  @author 卫星技术研究所
 */
@Getter
@AllArgsConstructor
public enum THType {

    /**
     * 开
     */
    ON("ON", "on", 1),

    /**
     * 关
     */
    OFF("OFF", "off", 0)
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

    public static THType tHType(String str) {
        for (THType value : values()) {
            if (StringUtils.contains(str, value.getName())) {
                return value;
            }
        }
        throw new RuntimeException("'THType' not found By " + str);
    }
}
