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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("packet_property")
@Schema(title = "PacketEntity对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class PacketEntity extends Model<PacketEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "param_id", description = "参数id")
    @TableField("param_id")
    private Long paramId;

    @Schema(title = "epoch", description = "星上时刻")
    @TableField("epoch")
    private String epoch;

    @Schema(title = "`name`", description = "参数名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "origin", description = "原始值")
    @TableField("origin")
    private String origin;

    @Schema(title = "`value`", description = "解析值")
    @TableField("`value`")
    private String value;

    @Schema(title = "`label`", description = "显示值")
    @TableField("`label`")
    private String label;

    @Schema(title = "`status`", description = "状态")
    @TableField("`status`")
    private String status;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
