package com.hit.telemetry_parser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2024-05-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("satellite_orbit_kepler")
@Schema(title = "OrbitEntity对象", description = "")
public class OrbitKeplerEntity extends Model<OrbitKeplerEntity> {

    @Schema(title = "id", description = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "satellite_id", description = "卫星id")
    @TableField("satellite_id")
    private Long satelliteId;

    @Schema(title = "epoch", description = "轨道时刻")
    @TableField("epoch")
    private Date epoch;

    @Schema(title = "semi_major_axis", description = "轨道半长轴(km)")
    @TableField("semi_major_axis")
    private Double semiMajorAxis;

    @Schema(title = "eccentricity", description = "偏心率")
    @TableField("eccentricity")
    private Double eccentricity;

    @Schema(title = "inclination", description = "轨道倾角(deg)")
    @TableField("inclination")
    private Double inclination;

    @Schema(title = "raan", description = "升交点赤经(deg)")
    @TableField("raan")
    private Double raan;

    @Schema(title = "periapsis", description = "近地点幅角(deg)")
    @TableField("periapsis")
    private Double periapsis;

    @Schema(title = "anomaly", description = "近点角(deg)")
    @TableField("anomaly")
    private Double anomaly;

    @Schema(title = "anomaly_type", description = "是否为真近点角")
    @TableField("anomaly_type")
    private Boolean anomalyType;

    @Schema(title = "cd", description = "大气阻尼系数")
    @TableField("cd")
    private Double cd;

    @Schema(title = "light", description = "光压反射系数")
    @TableField("light")
    private Double light;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
