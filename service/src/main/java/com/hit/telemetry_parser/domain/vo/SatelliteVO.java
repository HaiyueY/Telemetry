package com.hit.telemetry_parser.domain.vo;

import com.hit.telemetry_parser.common.schedule.OrbitPattern;
import com.hit.telemetry_parser.entity.OrbitKeplerEntity;
import com.hit.telemetry_parser.entity.OrbitTleEntity;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;
import com.hit.telemetry_parser.entity.SatelliteEntity;
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
 * 卫星视图对象
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Satellite视图对象", description = "")
public class SatelliteVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "id")
    private Long id;

    @Schema(title = "`name`", description = "卫星名称")
    private String name;

    @Schema(title = "`norad_id`", description = "卫星编号")
    private Integer noradId;

    /*private List<String> sensorTypes;

    private List<Double> resolutions;

    private List<String> imagePatterns;*/

    @Schema(title = "orbit_type", description = "轨道类型")
    private String orbitType;

    @Schema(title = "memory", description = "内存大小")
    private Double memory;

    @Schema(title = "`status`", description = "是否启用")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    private LocalDateTime updateTime;

    private OrbitTleEntity tle;

    private OrbitKeplerEntity kepler;

    private List<SubsystemVO> subsystems;

    public SatelliteVO(SatelliteEntity entity) {
        this.id         = entity.getId();
        this.name       = entity.getName();
        this.noradId    = entity.getNoradId();
        this.orbitType  = OrbitPattern.getLabelByName(OrbitPattern.getByValue(entity.getOrbitType()).name());
        this.memory     = entity.getMemory();
        this.status     = entity.getStatus();
        this.updateTime = entity.getUpdateTime();
    }
}
