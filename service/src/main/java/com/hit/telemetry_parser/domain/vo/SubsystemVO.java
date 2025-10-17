package com.hit.telemetry_parser.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hit.telemetry_parser.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分系统视图对象
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Subsystem视图对象", description = "")
public class SubsystemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "分系统id")
    private Long id;

    @Schema(title = "satellite_id", description = "卫星id")
    private Long satelliteId;

    @Schema(title = "name_c_n", description = "分系统中文名称")
    private String nameCN;

    @Schema(title = "name_e_n", description = "分系统英文代号")
    private String nameEN;

    @Schema(title = "n_id", description = "nid")
    private String nId;

    @Schema(title = "`status`", description = "是否启用")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(title = "telemetryProtocols", description = "遥测协议版本列表")
    private List<String> telemetryProtocols;

    @Schema(title = "commandProtocols", description = "指令协议版本列表")
    private List<String> commandProtocols;

    public SubsystemVO(SubsystemEntity entity) {
        this.id             = entity.getId();
        this.satelliteId    = entity.getSatelliteId();
        this.nameCN         = entity.getNameCN();
        this.nameEN         = entity.getNameEN();
        this.nId            = entity.getNId();
        this.status         = entity.getStatus();
        this.updateTime     = entity.getUpdateTime();
    }
}
