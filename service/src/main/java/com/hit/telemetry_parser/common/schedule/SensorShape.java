package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 载荷类型
 */
public enum SensorShape {
    /** 矩形视场 */
    RECTANGULAR,
    /** 圆形视场 */
    CIRCULAR,;

    public static SensorShape getByName(String name) {
        return switch (name) {
            case "RECTANGULAR"  -> RECTANGULAR;
            case "CIRCULAR"     -> CIRCULAR;
            default -> throw new IllegalStateException("This sensor shape {" + name + "} is not implemented.");
        };
    }
    public static SensorShape getByLabel(String name) {
        return switch (name) {
            case "矩形视场" -> RECTANGULAR;
            case "圆形视场" -> CIRCULAR;
            default -> throw new IllegalStateException("This sensor shape {" + name + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "RECTANGULAR"  -> "矩形视场";
            case "CIRCULAR"     -> "圆形视场";
            default -> throw new IllegalStateException("This sensor shape {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(SensorShape pattern) {
        return switch (pattern) {
            case RECTANGULAR    -> 1;
            case CIRCULAR       -> 2;
            default -> throw new IllegalStateException("This sensor shape {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static SensorShape getByValue(Integer value) {
        return switch (value) {
            case 1 -> RECTANGULAR;
            case 2 -> CIRCULAR;
            default -> throw new IllegalStateException("This sensor shape value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (SensorShape pattern : SensorShape.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
