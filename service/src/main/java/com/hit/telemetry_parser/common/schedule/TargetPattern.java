package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 载荷类型
 */
public enum TargetPattern {
    /** 多边形区域 */
    POLYGON,
    /** 圆形区域 */
    CIRCLE,
    /** SHP区域 */
    SHP,;

    public static TargetPattern getByName(String name) {
        return switch (name) {
            case "POLYGON"  -> POLYGON;
            case "CIRCLE"   -> CIRCLE;
            case "SHP"      -> SHP;
            default -> throw new IllegalStateException("This target pattern {" + name + "} is not implemented.");
        };
    }

    public static TargetPattern getByLabel(String label) {
        return switch (label) {
            case "多边形区域" -> POLYGON;
            case "圆形区域" -> CIRCLE;
            case "SHP区域" -> SHP;
            default -> throw new IllegalStateException("This target pattern {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "POLYGON"  -> "多边形区域";
            case "CIRCLE"   -> "圆形区域";
            case "SHP"      -> "SHP区域";
            default -> throw new IllegalStateException("This target pattern {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(TargetPattern pattern) {
        return switch (pattern) {
            case POLYGON    -> 1;
            case CIRCLE     -> 2;
            case SHP        -> 3;
            default -> throw new IllegalStateException("This target pattern {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static TargetPattern getByValue(Integer value) {
        return switch (value) {
            case 1 -> POLYGON;
            case 2 -> CIRCLE;
            case 3 -> SHP;
            default -> throw new IllegalStateException("This target type pattern: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (TargetPattern pattern : TargetPattern.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
