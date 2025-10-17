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
 * @since 2025-05-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("telemetry_protocol")
@Schema(title = "TelemetryProtocolEntity对象", description = "")
public class TelemetryProtocolEntity extends Model<TelemetryProtocolEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "参数id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "subsystem_id", description = "分系统id")
    @TableField("subsystem_id")
    private Long subsystemId;

    @Schema(title = "`rec`", description = "协议代号")
    @TableField("`rec`")
    private String rec;

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

    @Schema(title = "length_byte", description = "字节长度")
    @TableField("length_byte")
    private Integer lengthByte;

    @Schema(title = "length_bit", description = "字节类型")
    @TableField("length_bit")
    private Integer lengthBit;

    @Schema(title = "data_type", description = "数据类型")
    @TableField("data_type")
    private Integer dataType;

    @Schema(title = "base", description = "显示进制")
    @TableField("base")
    private String base;

    @Schema(title = "formula", description = "计算公式")
    @TableField("formula")
    private String formula;

    @Schema(title = "`status`", description = "是否启用")
    @TableField("`status`")
    private Integer status;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
