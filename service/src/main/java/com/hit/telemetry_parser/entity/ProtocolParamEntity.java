package com.hit.telemetry_parser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("protocol_param")
@Schema(title = "ProtocolParamEntity对象", description = "")
public class ProtocolParamEntity extends Model<ProtocolParamEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "参数id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "protocol_id", description = "协议id")
    @TableField("protocol_id")
    private Long protocolId;

    @Schema(title = "`name`", description = "参数名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "`type`", description = "参数类型")
    @TableField("`type`")
    private String type;

    @Schema(title = "offset_byte", description = "起始字节")
    @TableField("offset_byte")
    private Integer offsetByte;

    @Schema(title = "offset_bit", description = "起始位")
    @TableField("offset_bit")
    private Integer offsetBit;

    @Schema(title = "length_byte", description = "长度字节")
    @TableField("length_byte")
    private Integer lengthByte;

    @Schema(title = "length_bit", description = "长度位")
    @TableField("length_bit")
    private Integer lengthBit;

    @Schema(title = "data_type", description = "数据类型")
    @TableField("data_type")
    private String dataType;

    @Schema(title = "base", description = "显示进制")
    @TableField("base")
    private String base;

    @Schema(title = "formula", description = "计算公式")
    @TableField("formula")
    private String formula;

    @Schema(title = "default", description = "默认值")
    @TableField("`default`")
    private String defaultValue;

    @Schema(title = "scale_max", description = "上限")
    @TableField("`scale_max`")
    private Double scaleMax;

    @Schema(title = "scale_min", description = "下限")
    @TableField("`scale_min`")
    private Double scaleMin;

    @Schema(title = "`status`", description = "是否启用")
    @TableField("`status`")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
