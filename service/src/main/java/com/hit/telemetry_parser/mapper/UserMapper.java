package com.hit.telemetry_parser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hit.telemetry_parser.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhy
 * @since 2022-04-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}