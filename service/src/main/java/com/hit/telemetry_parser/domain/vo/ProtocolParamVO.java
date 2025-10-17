package com.hit.telemetry_parser.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hit.telemetry_parser.common.telemetry.BaseType;
import com.hit.telemetry_parser.common.telemetry.ParamType;
import com.hit.telemetry_parser.common.telemetry.ProtocolSectionType;
import com.hit.telemetry_parser.entity.ProtocolParamEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *      协议参数视图对象
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@TableName("protocol_param")
@Schema(title = "ProtocolParam视图对象", description = "")
public class ProtocolParamVO extends Model<ProtocolParamVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "参数id")
    private Long id;

    @Schema(title = "protocol_id", description = "协议id")
    private Long protocolId;

    @Schema(title = "`name`", description = "参数名称")
    private String name;

    @Schema(title = "`type`", description = "参数类型")
    private String type;

    @Schema(title = "offset_byte", description = "起始字节")
    private Integer offsetByte;

    @Schema(title = "offset_bit", description = "起始位")
    private Integer offsetBit;

    @Schema(title = "length_byte", description = "长度字节")
    private Integer lengthByte;

    @Schema(title = "length_bit", description = "长度位")
    private Integer lengthBit;

    @Schema(title = "data_type", description = "数据类型")
    private String dataType;

    @Schema(title = "base", description = "显示进制")
    private String base;

    @Schema(title = "formula", description = "计算公式")
    private String formula;

    @Schema(title = "defaultValue", description = "默认值")
    private String defaultValue;

    @Schema(title = "scale_max", description = "上限")
    private Double scaleMax;

    @Schema(title = "scale_min", description = "下限")
    private Double scaleMin;

    @Schema(title = "`status`", description = "是否启用")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    private LocalDateTime updateTime;

    public ProtocolParamVO(ProtocolParamEntity entity) {
        this.id = entity.getId();
        this.protocolId = entity.getProtocolId();
        this.name = entity.getName();
        this.type = ProtocolSectionType.proFunType(entity.getType()).getLabel();
        this.offsetByte = entity.getOffsetByte();
        this.offsetBit = entity.getOffsetBit();
        this.lengthByte = entity.getLengthByte();
        this.lengthBit = entity.getLengthBit();
        this.dataType = ParamType.paramType(entity.getDataType()).getLabel();
        this.base = BaseType.baseType(entity.getBase()).getLabel();
        this.formula = entity.getFormula();
        this.defaultValue = entity.getDefaultValue();
        this.scaleMax = entity.getScaleMax();
        this.scaleMin = entity.getScaleMin();
        this.status = entity.getStatus();
        this.updateTime = entity.getUpdateTime();
    }


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
