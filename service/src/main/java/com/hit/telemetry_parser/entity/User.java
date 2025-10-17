package com.hit.telemetry_parser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yhy
 * @since 2022-04-05
 */
@Getter
@Setter
@TableName("user")
@Schema(title = "User对象", description = "")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title= "用户名")
    private String username;

    @Schema(title = "密码")
    private String password;

    @Schema(title = "头像", description = "头像链接")
    private String avatarUrl;


}
