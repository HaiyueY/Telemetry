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
@TableName("protocol_property")
@Schema(title = "ProtocolEntity对象", description = "")
public class ProtocolEntity extends Model<ProtocolEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "协议id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "template_id", description = "模板id")
    @TableField("template_id")
    private Long templateId;

    @Schema(title = "subsystem_id", description = "分系统id")
    @TableField("subsystem_id")
    private Long subsystemId;

    @Schema(title = "`name`", description = "协议名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "type", description = "协议类型")
    @TableField("`type`")
    private String type;

    @Schema(title = "rec", description = "协议标识（副类型）")
    @TableField("rec")
    private String rec;

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
