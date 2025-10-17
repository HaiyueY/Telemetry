package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 元任务类型
 */
public enum MetaTaskType {
    /** 成像 */
    COLLECTION,
    /** 遥控 */
    TELECONTROL,
    /** 回放 */
    TRANSMISSION,;

    public static MetaTaskType getByName(String name) {
        return switch (name) {
            case "COLLECTION"   -> COLLECTION;
            case "TELECONTROL"  -> TELECONTROL;
            case "TRANSMISSION" -> TRANSMISSION;
            default -> throw new IllegalStateException("This meta task type {" + name + "} is not implemented.");
        };
    }

    public static MetaTaskType getByLabel(String label) {
        return switch (label) {
            case "成像" -> COLLECTION;
            case "遥控" -> TELECONTROL;
            case "回放" -> TRANSMISSION;
            default -> throw new IllegalStateException("This meta task type {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "COLLECTION"   -> "成像";
            case "TELECONTROL"  -> "遥控";
            case "TRANSMISSION" -> "回放";
            default -> throw new IllegalStateException("This meta task type {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(MetaTaskType pattern) {
        return switch (pattern) {
            case COLLECTION     -> 1;
            case TELECONTROL    -> 2;
            case TRANSMISSION   -> 3;
            default -> throw new IllegalStateException("This meta task type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static MetaTaskType getByValue(Integer value) {
        return switch (value) {
            case 1 -> COLLECTION;
            case 2 -> TELECONTROL;
            case 3 -> TRANSMISSION;
            default -> throw new IllegalStateException("This meta task type value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (MetaTaskType pattern : MetaTaskType.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
