package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 载荷类型
 */
public enum SensorType {
    /** 全色 */
    PANCHROMATIC,
    /** 彩色 */
    COLOR,
    /** 多光谱 */
    MULTISPECTRAL,
    /** 高光谱 */
    HYPERSPECTRAL,
    /** 红外 */
    INFRARED,
    /** 合成孔径雷达 */
    SAR,;

    public static SensorType getByName(String name) {
        return switch (name) {
            case "PANCHROMATIC"     -> PANCHROMATIC;
            case "COLOR"            -> COLOR;
            case "MULTISPECTRAL"    -> MULTISPECTRAL;
            case "HYPERSPECTRAL"    -> HYPERSPECTRAL;
            case "INFRARED"         -> INFRARED;
            case "SAR"              -> SAR;
            default -> throw new IllegalStateException("This sensor type {" + name + "} is not implemented.");
        };
    }

    public static SensorType getByLabel(String label) {
        return switch (label) {
            case "全色" -> PANCHROMATIC;
            case "彩色" -> COLOR;
            case "多光谱" -> MULTISPECTRAL;
            case "高光谱" -> HYPERSPECTRAL;
            case "红外" -> INFRARED;
            case "SAR" -> SAR;
            default -> throw new IllegalStateException("This sensor type {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "PANCHROMATIC"     -> "全色";
            case "COLOR"            -> "彩色";
            case "MULTISPECTRAL"    -> "多光谱";
            case "HYPERSPECTRAL"    -> "高光谱";
            case "INFRARED"         -> "红外";
            case "SAR"              -> "SAR";
            default -> throw new IllegalStateException("This sensor type {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(SensorType pattern) {
        return switch (pattern) {
            case PANCHROMATIC     -> 1;
            case COLOR            -> 2;
            case MULTISPECTRAL    -> 3;
            case HYPERSPECTRAL    -> 4;
            case INFRARED         -> 5;
            case SAR              -> 6;
            default -> throw new IllegalStateException("This sensor type {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static SensorType getByValue(Integer value) {
        return switch (value) {
            case 1 -> PANCHROMATIC;
            case 2 -> COLOR;
            case 3 -> MULTISPECTRAL;
            case 4 -> HYPERSPECTRAL;
            case 5 -> INFRARED;
            case 6 -> SAR;
            default -> throw new IllegalStateException("This sensor type value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (SensorType pattern : SensorType.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
