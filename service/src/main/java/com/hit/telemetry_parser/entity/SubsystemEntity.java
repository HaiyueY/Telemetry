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

import com.hit.telemetry_parser.domain.vo.SubsystemVO;
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
 * @since 2025-05-23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("satellite_subsystem")
@Schema(title = "SubsystemEntity对象", description = "")
public class SubsystemEntity extends Model<SubsystemEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "分系统id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "satellite_id", description = "卫星id")
    @TableField("satellite_id")
    private Long satelliteId;

    @Schema(title = "name_c_n", description = "分系统中文名称")
    @TableField("name_c_n")
    private String nameCN;

    @Schema(title = "name_e_n", description = "分系统英文代号")
    @TableField("name_e_n")
    private String nameEN;

    @Schema(title = "n_id", description = "nid")
    @TableField("n_id")
    private String nId;

    @Schema(title = "`status`", description = "是否启用")
    @TableField("`status`")
    private Boolean status;

    @Schema(title = "update_time", description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;

    public SubsystemEntity(SubsystemVO vo) {
        this.id             = vo.getId();
        this.satelliteId    = vo.getSatelliteId();
        this.nameCN         = vo.getNameCN();
        this.nameEN         = vo.getNameEN();
        this.nId            = vo.getNId();
        this.status         = vo.getStatus();
        this.updateTime     = vo.getUpdateTime();
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
