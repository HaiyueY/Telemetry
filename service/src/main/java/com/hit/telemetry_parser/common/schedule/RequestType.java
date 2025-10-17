package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 需求类型
 *
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
public enum RequestType {
    /** 点目标观测 */
    POINT,
    /** 区域目标观测 */
    AREA,
    /** 其他 */
    OTHER;

    public static RequestType getByName(String name) {
        return switch (name) {
            case "POINT"    -> POINT;
            case "AREA"     -> AREA;
            case "OTHER"    -> OTHER;
            default -> throw new IllegalStateException("This request type {" + name + "} is not implemented.");
        };
    }

    public static RequestType getByLabel(String label) {
        return switch (label) {
            case "点目标观测" -> POINT;
            case "区域目标观测" -> AREA;
            case "其他" -> OTHER;
            default -> throw new IllegalStateException("This request type {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        RequestType byName = getByName(name);
        return getLabel(byName);
    }

    public static String getLabel(RequestType pattern) {
        return switch (pattern) {
            case POINT  -> "点目标观测";
            case AREA   -> "区域目标观测";
            case OTHER  -> "其他";
            default -> throw new IllegalStateException("This request type {" + pattern + "} is not implemented.");
        };
    }

    public static Integer getValue(RequestType pattern) {
        return switch (pattern) {
            case POINT  -> 1;
            case AREA   -> 2;
            case OTHER  -> 3;
            default ->
                    throw new IllegalStateException("This request type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static RequestType getByValue(Integer value) {
        return switch (value) {
            case 1 -> POINT;
            case 2 -> AREA;
            case 3 -> OTHER;
            default -> throw new IllegalStateException("This target type value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (RequestType pattern : RequestType.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
