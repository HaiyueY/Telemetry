package com.hit.telemetry_parser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hit.telemetry_parser.domain.dto.UserDTO;
import com.hit.telemetry_parser.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhy
 * @since 2022-04-05
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    User getUserInfo(UserDTO userDTO);
}
