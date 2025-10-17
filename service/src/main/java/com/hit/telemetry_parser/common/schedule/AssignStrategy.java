package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 载荷类型
 */
public enum AssignStrategy {
    /**
     * 手动指定
     */
    MANUAL,
    /**
     * 自动指定
     */
    AUTO,
    ;

    public static AssignStrategy getByName(String name) {
        return switch (name) {
            case "MANUAL"   -> MANUAL;
            case "AUTO"     -> AUTO;
            default -> throw new IllegalStateException("This strategy {" + name + "} is not implemented.");
        };
    }

    public static AssignStrategy getByLabel(String label) {
        return switch (label) {
            case "手动指定" -> MANUAL;
            case "自动指定" -> AUTO;
            default -> throw new IllegalStateException("This strategy {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "MANUAL"   -> "手动指定";
            case "AUTO"     -> "自动指定";
            default -> throw new IllegalStateException("This strategy {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(AssignStrategy pattern) {
        return switch (pattern) {
            case MANUAL     -> 1;
            case AUTO       -> 2;
            default -> throw new IllegalStateException("This strategy {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static AssignStrategy getByValue(Integer value) {
        return switch (value) {
            case 1 -> MANUAL;
            case 2 -> AUTO;
            default -> throw new IllegalStateException("This strategy value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (AssignStrategy pattern : AssignStrategy.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
