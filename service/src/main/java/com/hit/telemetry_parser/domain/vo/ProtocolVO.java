package com.hit.telemetry_parser.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.entity.ProtocolEntity;
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
 *      协议视图对象
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@TableName("protocol")
@Schema(title = "ProtocolEntity视图对象", description = "")
public class ProtocolVO extends Model<ProtocolVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "协议id")
    private Long id;

    @Schema(title = "type", description = "类型")
    private String type;

    @Schema(title = "template_id", description = "模板id")
    private Long templateId;

    @Schema(title = "subsystem_id", description = "分系统id")
    private Long subsystemId;

    @Schema(title = "`name`", description = "协议名称")
    private String name;

    @Schema(title = "`template`", description = "协议模板")
    private ProtocolTemplateVO template;

    @Schema(title = "`satellite`", description = "所属分系统")
    private SatelliteVO satellite;

    @Schema(title = "rec", description = "协议标识（副类型）")
    private String rec;

    @Schema(title = "`status`", description = "是否启用")
    private Boolean status;

    @Schema(title = "params", description = "参数列表")
    private List<ProtocolParamVO> params;

    @Schema(title = "update_time", description = "更新时间")
    private LocalDateTime updateTime;

    public ProtocolVO(ProtocolEntity entity) {
        this.id             = entity.getId();
        this.type           = TTCType.ttcType(entity.getType()).getLabel();
        this.templateId     = entity.getTemplateId();
        this.subsystemId    = entity.getSubsystemId();
        this.name           = entity.getName();
        this.rec            = entity.getRec();
        this.status         = entity.getStatus();
        this.updateTime     = entity.getUpdateTime();
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
