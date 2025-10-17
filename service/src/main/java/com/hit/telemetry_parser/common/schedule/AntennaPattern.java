package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地面站天线类型
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
public enum AntennaPattern {
    /** 遥控 */
    TELECONTROL,
    /** 遥测 */
    TELEMETRY,
    /** 数传 */
    TRANSMISSION,
    /** 其他 */
    OTHER;

    public static AntennaPattern getByName(String name) {
        return switch (name) {
            case "TELECONTROL"      -> TELECONTROL;
            case "TELEMETRY"        -> TELEMETRY;
            case "TRANSMISSION"     -> TRANSMISSION;
            case "OTHER"            -> OTHER;
            default -> throw new IllegalStateException("This antenna type {" + name + "} is not implemented.");
        };
    }

    public static AntennaPattern getByLabel(String label) {
        return switch (label) {
            case "遥控" -> TELECONTROL;
            case "遥测" -> TELEMETRY;
            case "数传" -> TRANSMISSION;
            case "其他" -> OTHER;
            default -> throw new IllegalStateException("This antenna type {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        AntennaPattern byName = getByName(name);
        return getLabel(byName);
    }

    public static String getLabel(AntennaPattern pattern) {
        return switch (pattern) {
            case TELECONTROL    -> "遥控";
            case TELEMETRY      -> "遥测";
            case TRANSMISSION   -> "数传";
            case OTHER          -> "其他";
            default -> throw new IllegalStateException("This antenna type {" + pattern + "} is not implemented.");
        };
    }

    public static Integer getValue(AntennaPattern pattern) {
        return switch (pattern) {
            case OTHER          -> 0;
            case TELECONTROL    -> 1;
            case TELEMETRY      -> 10;
            case TRANSMISSION   -> 100;
            default -> throw new IllegalStateException("This antenna type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String name) {
        return getValue(getByLabel(name));
    }

    public static AntennaPattern getByValue(Integer value) {
        return switch (value) {
            case 1      -> TELECONTROL;
            case 10     -> TELEMETRY;
            case 100    -> TRANSMISSION;
            default     -> OTHER;
        };
    }

    public static List<AntennaPattern> getMultipleByValue(Integer value) {
        return switch (value) {
            case 1      -> List.of(TELECONTROL);
            case 10     -> List.of(TELEMETRY);
            case 11     -> List.of(TELECONTROL, TELEMETRY);
            case 100    -> List.of(TRANSMISSION);
            case 101    -> List.of(TELECONTROL, TRANSMISSION);
            case 110    -> List.of(TELEMETRY, TRANSMISSION);
            case 111    -> List.of(TELECONTROL, TELEMETRY, TRANSMISSION);
            default     -> List.of(OTHER);
        };
    }

    public static Integer getValeByMultiple(List<AntennaPattern> patterns) {
        int value = 0;
        for (AntennaPattern pattern : patterns) {
            value += getValue(pattern);
        }
        return value;
    }

    public static Integer getValeByMultipleName(List<String> names) {
        int value = 0;
        for (String name : names) {
            value += getValueByName(name);
        }
        return value;
    }

    public static Integer getValeByMultipleLabel(List<String> labels) {
        int value = 0;
        for (String name : labels) {
            value += getValueByLabel(name);
        }
        return value;
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (AntennaPattern pattern : AntennaPattern.values()) {
            if (pattern != OTHER) {
                patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
            }
        }
        return patternMap;
    }

}
