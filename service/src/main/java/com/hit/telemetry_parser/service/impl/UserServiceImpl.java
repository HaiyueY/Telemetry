package com.hit.telemetry_parser.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hit.telemetry_parser.common.Constant;
import com.hit.telemetry_parser.component.WebSocketServer;
import com.hit.telemetry_parser.domain.dto.UserDTO;
import com.hit.telemetry_parser.domain.dto.MessageDTO;
import com.hit.telemetry_parser.entity.User;
import com.hit.telemetry_parser.exception.ServiceException;
import com.hit.telemetry_parser.mapper.UserMapper;
import com.hit.telemetry_parser.service.IUserService;
import com.hit.telemetry_parser.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhy
 * @since 2022-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private WebSocketServer webSocketServer;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
//            // 推送消息
            webSocketServer.sendAllMessage(new MessageDTO()
                    .setDatetime(LocalDateTime.now(ZoneId.of("UTC+8")).toString())
                    .setTitle("登录成功").setStatus("warning").setExtra("登录成功")
                    .setDescription("星座任务规划系统用户: " + userDTO.getUsername() + " 登录成功"));
            return userDTO;
        } else {
            throw new ServiceException(Constant.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constant.CODE_600, "用户已存在");
        }
        return one;
    }

    @Override
    public User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constant.CODE_500, "系统错误");
        }
        return one;
    }

}