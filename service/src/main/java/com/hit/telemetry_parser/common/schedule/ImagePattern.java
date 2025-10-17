package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 载荷成像模式
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
public enum ImagePattern {
    /** 单景 */
    SINGLE,
    /** 立体 */
    STEREO,
    /** 推扫 */
    SCANNING,
//    /** 凝视 */
//    STARING,
    /** 其他 */
    OTHER;

    public static ImagePattern getByName(String name) {
        return switch (name) {
            case "SINGLE"   -> SINGLE;
            case "STEREO"   -> STEREO;
            case "SCANNING" -> SCANNING;
//            case "STARING"  -> STARING;
            case "OTHER"    -> OTHER;
            default -> throw new IllegalStateException("This imaging pattern {" + name + "} is not implemented.");
        };
    }

    public static ImagePattern getByLabel(String label) {
        return switch (label) {
            case "单景成像"   -> SINGLE;
            case "立体成像"  -> STEREO;
            case "推扫成像" -> SCANNING;
//            case "凝视成像" -> STARING;
            case "其他模式"    -> OTHER;
            default -> throw new IllegalStateException("This imaging pattern {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        ImagePattern byName = getByName(name);
        return getLabel(byName);
    }

    public static String getLabelByValue(Integer value) {
        ImagePattern byValue = getByValue(value);
        return getLabel(byValue);
    }

    public static String getLabel(ImagePattern pattern) {
        return switch (pattern) {
            case SINGLE     -> "单景成像";
            case STEREO     -> "立体成像";
            case SCANNING   -> "推扫成像";
//            case STARING    -> "凝视成像";
            case OTHER      -> "其他模式";
            default -> throw new IllegalStateException("This imaging pattern {" + pattern + "} is not implemented.");
        };
    }

    public static Integer getValue(ImagePattern pattern) {
        return switch (pattern) {
            case OTHER      -> 0;
            case SINGLE     -> 1;
            case STEREO     -> 10;
            case SCANNING   -> 100;
//            case STARING    -> 1000;
            default -> throw new IllegalStateException("This imaging pattern {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String name) {
        return getValue(getByLabel(name));
    }

    public static ImagePattern getByValue(Integer value) {
        return switch (value) {
            case 1      -> SINGLE;
            case 10     -> STEREO;
            case 100    -> SCANNING;
//            case 1000   -> STARING;
            default     -> OTHER;
        };
    }

    public static List<ImagePattern> getMultipleByValue(Integer value) {
        return switch (value) {
            case 1      -> List.of(SINGLE);
            case 10     -> List.of(STEREO);
            case 100    -> List.of(SCANNING);
            case 11     -> List.of(SINGLE, STEREO);
            case 101    -> List.of(SINGLE, SCANNING);
            case 110    -> List.of(STEREO, SCANNING);
            case 111    -> List.of(SINGLE, STEREO, SCANNING);
//            case 1000   -> List.of(STARING);
//            case 1001   -> List.of(SINGLE, STARING);
//            case 1010   -> List.of(STARING, STARING);
//            case 1011   -> List.of(SINGLE, STEREO, STARING);
//            case 1100   -> List.of(SCANNING, STARING);
//            case 1101   -> List.of(SINGLE, SCANNING, STARING);
//            case 1110   -> List.of(STEREO, SCANNING, STARING);
//            case 1111   -> List.of(SINGLE, STEREO, SCANNING, STARING);
            default     -> List.of(OTHER);
        };
    }

    public static Integer getValeByMultiple(List<ImagePattern> patterns) {
        int value = 0;
        for (ImagePattern pattern : patterns) {
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
        for (ImagePattern pattern : ImagePattern.values()) {
            if (pattern != OTHER) {
                patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
            }
        }
        return patternMap;
    }

}
