package com.hit.telemetry_parser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hit.telemetry_parser.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Yang_Haiyue
 * @company 卫星技术研究所_哈尔滨工业大学
 * @create 2022-09-25-19:02
 */
@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
}
