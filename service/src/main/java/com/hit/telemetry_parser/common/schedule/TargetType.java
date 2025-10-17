package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 目标类型
 */
public enum TargetType {
    /** 点目标 */
    POINT,
    /** 区域目标 */
    AREA,
    /** 条带目标 */
    STRIP,;

    public static TargetType getByName(String name) {
        return switch (name) {
            case "POINT" -> POINT;
            case "AREA"  -> AREA;
            case "STRIP" -> STRIP;
            default -> throw new IllegalStateException("This target type {" + name + "} is not implemented.");
        };
    }

    public static TargetType getByLabel(String label) {
        return switch (label) {
            case "点目标" -> POINT;
            case "区域目标" -> AREA;
            case "条带目标" -> STRIP;
            default -> throw new IllegalStateException("This target type {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "POINT"    -> "点目标";
            case "AREA"     -> "区域目标";
            case "STRIP"    -> "条带目标";
            default -> throw new IllegalStateException("This target type {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(TargetType pattern) {
        return switch (pattern) {
            case POINT  -> 1;
            case AREA   -> 2;
            case STRIP  -> 3;
            default -> throw new IllegalStateException("This target type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static TargetType getByValue(Integer value) {
        return switch (value) {
            case 1 -> POINT;
            case 2 -> AREA;
            case 3 -> STRIP;
            default -> throw new IllegalStateException("This target type value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (TargetType pattern : TargetType.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
