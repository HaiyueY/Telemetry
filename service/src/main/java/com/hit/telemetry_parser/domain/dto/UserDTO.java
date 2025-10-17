package com.hit.telemetry_parser.domain.dto;

import lombok.Data;

/**
 * 接收前端登陆请求参数
 */
@Data
public class UserDTO {

    private String username;

    private String password;

    private String avatarUrl;

    private String token;
}
