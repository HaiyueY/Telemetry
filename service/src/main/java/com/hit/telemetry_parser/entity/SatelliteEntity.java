package com.hit.telemetry_parser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.hit.telemetry_parser.common.schedule.OrbitPattern;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
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
 * @since 2024-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("satellite_property")
@Schema(title = "PropertyEntity对象", description = "卫星实体对象")
public class SatelliteEntity extends Model<SatelliteEntity> {

    @Schema(title = "id", description = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "constellation_id", description = "星座id")
    @TableField("constellation_id")
    private Long constellationId;

    @Schema(title = "`name`", description = "卫星名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "`norad_id`", description = "卫星编号")
    @TableField("`norad_id`")
    private Integer noradId;

    @Schema(title = "orbit_type", description = "轨道类型")
    @TableField("orbit_type")
    private Integer orbitType;

    @Schema(title = "`memory`", description = "最大存储容量")
    @TableField("`memory`")
    private Double memory;

    @Schema(title = "`rotation_speed`", description = "姿态机动角速度(rad/s)")
    @TableField("`rotation_speed`")
    private Double rotationSpeed;

    @Schema(title = "`plane_num`", description = "轨道面编号")
    @TableField("`plane_num`")
    private Integer planeNum;

    @Schema(title = "`inter_plane_num`", description = "面内编号")
    @TableField("`inter_plane_num`")
    private Integer interPlaneNum;

    @Schema(title = "`status`", description = "是否启用")
    @TableField("`status`")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;

    public SatelliteEntity(SatelliteVO satelliteVo) {
        if (satelliteVo.getId() != null) {
            this.id = satelliteVo.getId();
        }
        this.name       = satelliteVo.getName();
        this.noradId    = satelliteVo.getNoradId();
        this.orbitType  = OrbitPattern.getValueByLabel(satelliteVo.getOrbitType());
        this.memory     = satelliteVo.getMemory();
        this.status     = satelliteVo.getStatus();
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
