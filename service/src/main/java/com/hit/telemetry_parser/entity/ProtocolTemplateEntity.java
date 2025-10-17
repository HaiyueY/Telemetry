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
@TableName("protocol_template")
@Schema(title = "ProtocolTemplateEntity对象", description = "")
public class ProtocolTemplateEntity extends Model<ProtocolTemplateEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "模板id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "`name`", description = "模板名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "template_type", description = "模板类型")
    @TableField("template_type")
    private String templateType;

    @Schema(title = "header_offset", description = "导头偏移（byte）")
    @TableField("header_offset")
    private Integer headerOffset;

    @Schema(title = "header_length", description = "导头长度（byte）")
    @TableField("header_length")
    private Integer headerLength;

    @Schema(title = "body_offset", description = "数据区偏移（byte）")
    @TableField("body_offset")
    private Integer bodyOffset;

    @Schema(title = "body_length", description = "数据区长度（byte）")
    @TableField("body_length")
    private Integer bodyLength;

    @Schema(title = "valid_offset", description = "校验区偏移（byte）")
    @TableField("valid_offset")
    private Integer validOffset;

    @Schema(title = "valid_length", description = "校验区长度（byte）")
    @TableField("valid_length")
    private Integer validLength;

    @Schema(title = "valid_type", description = "校验方式")
    @TableField("valid_type")
    private String validType;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
