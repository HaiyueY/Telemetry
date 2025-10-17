package com.hit.telemetry_parser.common.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 观测需求处理状态
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
public enum RequestStatusType {
    /**
     * 未开始
     */
    NOT_STARTED,
    /**
     * 取消
     */
    CANCELLED,
    /**
     * 成功
     */
    SUCCEED,
    /**
     * 失败
     */
    FAILED,
    /**
     * 轨道外推与可见性计算
     */
    PROPAGATE,
    /**
     * 任务分解
     */
    DECOMPOSITION,
    /**
     * 规划
     */
    SCHEDULE,
    ;

    public static RequestStatusType getByName(String name) {
        return switch (name) {
            case "NOT_STARTED"      -> NOT_STARTED;
            case "CANCELLED"        -> CANCELLED;
            case "SUCCEED"          -> SUCCEED;
            case "FAILED"           -> FAILED;
            case "PROPAGATE"        -> PROPAGATE;
            case "DECOMPOSITION"    -> DECOMPOSITION;
            case "PRE_SCHEDULE"     -> SCHEDULE;
            default -> throw new IllegalStateException("This request status {" + name + "} is not implemented.");
        };
    }

    public static RequestStatusType getByLabel(String label) {
        return switch (label) {
            case "未开始" -> NOT_STARTED;
            case "取消" -> CANCELLED;
            case "成功" -> SUCCEED;
            case "失败" -> FAILED;
            case "可见性计算" -> PROPAGATE;
            case "任务分解" -> DECOMPOSITION;
            case "任务预规划" -> SCHEDULE;
            default -> throw new IllegalStateException("This request status {" + label + "} is not implemented.");
        };
    }

    public static String getLabelByName(String name) {
        return switch (name) {
            case "NOT_STARTED"      -> "未开始";
            case "CANCELLED"        -> "取消";
            case "SUCCEED"          -> "成功";
            case "FAILED"           -> "失败";
            case "PROPAGATE"        -> "可见性计算";
            case "DECOMPOSITION"    -> "任务分解";
            case "SCHEDULE"         -> "任务规划";
            default -> throw new IllegalStateException("This request status {" + name + "} is not implemented.");
        };
    }

    public static Integer getValue(RequestStatusType pattern) {
        return switch (pattern) {
            case FAILED         -> -2;
            case CANCELLED      -> -1;
            case NOT_STARTED    -> 0;
            case PROPAGATE      -> 1;
            case DECOMPOSITION  -> 2;
            case SCHEDULE       -> 3;
            case SUCCEED        -> 5;
            default -> throw new IllegalStateException("This request status {" + pattern.name() + "} is not implemented.");
        };
    }

    public static Integer getValueByName(String name) {
        return getValue(getByName(name));
    }

    public static Integer getValueByLabel(String label) {
        return getValue(getByLabel(label));
    }

    public static RequestStatusType getByValue(Integer value) {
        return switch (value) {
            case -2 -> FAILED;
            case -1 -> CANCELLED;
            case 0  -> NOT_STARTED;
            case 1  -> PROPAGATE;
            case 2  -> DECOMPOSITION;
            case 3  -> SCHEDULE;
            case 5  -> SUCCEED;
            default -> throw new IllegalStateException("This request status value: {" + value + "} is not implemented.");
        };
    }

    public static String getLabelByValue(Integer value) {
        return getLabelByName(getByValue(value).name());
    }

    public static Map<Integer, String> getNameLabelMap() {
        Map<Integer, String> patternMap = new HashMap<>(16);
        for (RequestStatusType pattern : RequestStatusType.values()) {
            patternMap.put(getValue(pattern), getLabelByName(pattern.name()));
        }
        return patternMap;
    }

}
