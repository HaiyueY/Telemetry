package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 优先级
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2023/11/20 18:23
 */
public enum Priority {
    /** 较低优先级 */
    MINOR,
    /** 低优先级 */
    NORMAL,
    /** 中优先级 */
    MAJOR,
    /** 高优先级 */
    CRITICAL,
    /** 应急优先级 */
    EMERGENCY;

    public String getPriority() {
        return switch (this) {
            case MINOR      -> "MINOR";
            case NORMAL     -> "NORMAL";
            case MAJOR      -> "MAJOR";
            case CRITICAL   -> "CRITICAL";
            case EMERGENCY  -> "EMERGENCY";
            default -> throw new IllegalStateException("The priority (" + this + ") is not implemented.");
        };
    }

    public static Priority getByName(String name) {
        return switch (name) {
            case "MINOR"       -> MINOR;
            case "NORMAL"      -> NORMAL;
            case "MAJOR"       -> MAJOR;
            case "CRITICAL"    -> CRITICAL;
            case "EMERGENCY"   -> EMERGENCY;
            default -> throw new IllegalStateException("This priority {" + name + "} is not implemented.");
        };
    }

    public static Priority getByLabel(String label) {
        return switch (label) {
            case "较低" -> MINOR;
            case "低" -> NORMAL;
            case "中" -> MAJOR;
            case "高" -> CRITICAL;
            case "应急" -> EMERGENCY;
            default -> throw new IllegalStateException("This priority {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "MINOR"     -> "较低";
            case "NORMAL"    -> "低";
            case "MAJOR"     -> "中";
            case "CRITICAL"  -> "高";
            case "EMERGENCY" -> "应急";
            default -> throw new IllegalStateException("This priority {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(Priority pattern) {
        return switch (pattern) {
            case MINOR      -> 1;
            case NORMAL     -> 2;
            case MAJOR      -> 3;
            case CRITICAL   -> 4;
            case EMERGENCY  -> 5;
            default -> throw new IllegalStateException("This target type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static Priority getByValue(Integer value) {
        return switch (value) {
            case 1 -> MINOR;
            case 2 -> NORMAL;
            case 3 -> MAJOR;
            case 4 -> CRITICAL;
            case 5 -> EMERGENCY;
            default -> throw new IllegalStateException("This target type value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (Priority pattern : Priority.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
