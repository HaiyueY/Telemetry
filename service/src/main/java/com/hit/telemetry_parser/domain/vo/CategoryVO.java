package com.hit.telemetry_parser.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "协议分类id")
    private Long id;

    @Schema(title = "parentId", description = "父级id")
    private Long parentId;

    @Schema(title = "`label`", description = "协议分类名称")
    private String label;

    @Schema(title = "`categoryName`", description = "级别名称")
    private String categoryName;

    @Schema(title = "`children`", description = "子对象")
    private List<CategoryVO> children;
}
