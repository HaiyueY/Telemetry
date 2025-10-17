package com.hit.telemetry_parser.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *     遥测数据视图对象
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Telemetry视图对象", description = "")
public class TelemetryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "epoch", description = "时间")
    private LocalDateTime epoch;

    @Schema(title = "`name`", description = "数据项")
    private String name;

    @Schema(title = "`origin`", description = "原始值")
    private String origin;

    @Schema(title = "value", description = "处理值")
    private String value;

    @Schema(title = "type", description = "状态")
    private String type;

}
