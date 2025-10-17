package com.hit.telemetry_parser.common.telemetry;

import com.hit.telemetry_parser.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 遥测参数类型
 *
 *  @author Yang Haiyue
 *  @author 卫星技术研究所
 */
@Getter
@AllArgsConstructor
public enum ParamType {

    /**
     * AN量参数遥测电压量的测量量程为0～5.1V；每个模拟量参数量化后变换为一字节，即8bit数字量，每量化分层为20mV。
     * 根据不同AN量参数所表达的物理量的性质，则具有不同的数据处理要求：
     * (1)	一般电信号监测参数：
     * 仅需将原始二进制分层值（8bit）D乘以分层电压值0.02即可，即：
     * V=D×0.02；
     * 将此方法定义为公式1。
     * (2)	电源电压或电流参数：
     * 由二进制分层值（8bit）D电源电压（流）值V的一般处理公式为：
     * V=D×0.02×K+b
     * 其中K，b为变换系数。
     * 将此方法定义为公式2。
     * (3)	其它物理量参数：根据所给出的数据处理公式处理。
     */
    AN("AN", "AN量参数", "[\"Add\", [\"Multiply\", 2, \"x\"], 4]",0),

    /**
     * BL量也称状态量参数,参数取值“0”或“1”，BL量参数值在遥测数据格式中只占据1比特空间。根据要求处理成表达式或直接显示0/1。
     */
    BL("BL", "状态量参数", "",1),
    /**
     * TH量在遥测数据格式中占据一个字节，即8bit，用下列公式处理，定义为公式3：
     * T=2c/(-b+[b^2-4c(a-ln⁡〖R_t 〗 )]^(1⁄2) )-273.15(℃)
     * 其中：
     * Ns是校准源实测分层值，Nt是温度实测分层值；
     * 对于不同的热敏电阻有常数a、b、c相应的值，具体数值见对应TH量描述；
     * 12bit温度量处理时Rt=7500xNt/（Ns*16-Nt）。Ns是校准源实测分层值（8bit），Nt是温度实测分层值(12bit)。
     */
    TH("TH", "TH量参数", "",2),
    /**
     * 为便于地面站进行识别和处理，对数字量遥测（包括DS量、BUS量）可分解部分进一步分解为子参数，并赋予明确的参数代号和名称，具体处理要求参见各分系统数字量遥测参数处理。
     * 定义公式4为直接将遥测原码直接显示为整数，可选择二进制、十进制、十六进制显示。
     */
    DS_BUS("DS_BUS", "数字量参数", "",3),
    ;


    /**
     * 类型
     */
    private final String name;
    /**
     * 名称
     */
    private final String label;
    /**
     * 默认公式
     */
    private final String defaultFormula;

    private final Integer value;

    public static ParamType paramType(String str) {
        for (ParamType value : values()) {
            if (StringUtils.contains(str, value.getName())) {
                return value;
            }
        }
        throw new RuntimeException("'ParamType' not found By " + str);
    }

    public static ParamType getFromLabel(String str) {
        for (ParamType value : values()) {
            if (StringUtils.contains(str, value.getLabel())) {
                return value;
            }
        }
        throw new RuntimeException("'ParamType' not found By " + str);
    }

    public static Map<Integer, String> getNameLabelMap() {
        return Arrays.stream(ParamType.values())
                .collect(Collectors.toMap(ParamType::getValue, ParamType::getLabel));
    }
}
