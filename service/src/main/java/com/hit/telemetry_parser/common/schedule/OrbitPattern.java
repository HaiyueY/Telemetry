package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 轨道加载类型
 */
public enum OrbitPattern {
    /** 两行根数 */
    TLE,
    /** 轨道根数 */
    OE,;

    public static OrbitPattern getByName(String name) {
        return switch (name) {
            case "TLE" -> TLE;
            case "OE" -> OE;
            default -> throw new IllegalStateException("This orbit pattern {" + name + "} is not implemented.");
        };
    }

    public static OrbitPattern getByLabel(String name) {
        return switch (name) {
            case "两行根数" -> TLE;
            case "轨道根数" -> OE;
            default -> throw new IllegalStateException("This orbit pattern {" + name + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "TLE" -> "两行根数";
            case "OE" -> "轨道根数";
            default -> throw new IllegalStateException("This orbit pattern {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(OrbitPattern pattern) {
        return switch (pattern) {
            case TLE -> 1;
            case OE -> 2;
            default -> throw new IllegalStateException("This orbit pattern {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String name) {
        return getValue(getByLabel(name));
    }

    public static OrbitPattern getByValue(Integer value) {
        return switch (value) {
            case 1 -> TLE;
            case 2 -> OE;
            default -> throw new IllegalStateException("This orbit pattern value: {" + value + "} is not implemented.");
        };
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (OrbitPattern pattern : OrbitPattern.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
