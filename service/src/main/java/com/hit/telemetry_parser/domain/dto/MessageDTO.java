package com.hit.telemetry_parser.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 消息对象
 *
 * @author Yang Haiyue
 * @author 卫星技术研究所
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "消息对象", description = "用于服务端向前端推送消息的封装数据类")
public class MessageDTO {

    @Schema(title = "title", description = "标题")
    private String title;

    @Schema(title = "datetime", description = "时间")
    private String datetime;

    @Schema(title = "description", description = "详情")
    private String description;

    /**
     * 需要与其他Status相关对象做区分, 这里的状态对应前端的消息类型
     * "primary" | "success" | "info" | "warning" | "danger" | "telemetry"
     */
    @Schema(title = "status", description = "状态")
    private String status;

    @Schema(title = "extra", description = "附加属性")
    private String extra;
}
