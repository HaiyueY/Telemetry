package com.hit.telemetry_parser.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.common.telemetry.ValidateType;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *      协议模板视图对象
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@TableName("protocol_template")
@Schema(title = "ProtocolTemplateEntity对象", description = "")
public class ProtocolTemplateVO extends Model<ProtocolTemplateVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "模板id")
    private Long id;

    @Schema(title = "`name`", description = "模板名称")
    private String name;

    @Schema(title = "template_type", description = "模板类型")
    private String templateType;

    @Schema(title = "header_offset", description = "导头偏移（byte）")
    private Integer headerOffset;

    @Schema(title = "header_length", description = "导头长度（byte）")
    private Integer headerLength;

    @Schema(title = "body_offset", description = "数据区偏移（byte）")
    private Integer bodyOffset;

    @Schema(title = "body_length", description = "数据区长度（byte）")
    private Integer bodyLength;

    @Schema(title = "valid_offset", description = "校验区偏移（byte）")
    private Integer validOffset;

    @Schema(title = "valid_length", description = "校验区长度（byte）")
    private Integer validLength;

    @Schema(title = "valid_type", description = "校验方式")
    private String validType;

    @Schema(title = "params", description = "参数列表")
    private List<ProtocolParamVO> params;

    @Schema(title = "update_time", description = "更新时间")
    private LocalDateTime updateTime;

    public ProtocolTemplateVO(ProtocolTemplateEntity entity) {
        this.id             = entity.getId();
        this.name           = entity.getName();
        this.templateType   = TTCType.ttcType(entity.getTemplateType()).getLabel();
        this.headerOffset   = entity.getHeaderOffset();
        this.headerLength   = entity.getHeaderLength();
        this.bodyOffset     = entity.getBodyOffset();
        this.bodyLength     = entity.getBodyLength();
        this.validOffset    = entity.getValidOffset();
        this.validLength    = entity.getValidLength();
        this.validType      = ValidateType.validateType(entity.getValidType()).getLabel();
        this.updateTime     = entity.getUpdateTime();
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
