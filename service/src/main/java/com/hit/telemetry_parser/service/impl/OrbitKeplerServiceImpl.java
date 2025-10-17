package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hit.telemetry_parser.entity.OrbitKeplerEntity;
import com.hit.telemetry_parser.mapper.OrbitKeplerMapper;
import com.hit.telemetry_parser.service.IOrbitKeplerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-08
 */
@Service
public class OrbitKeplerServiceImpl extends ServiceImpl<OrbitKeplerMapper, OrbitKeplerEntity> implements IOrbitKeplerService {

    @Override
    public OrbitKeplerEntity getBySatelliteId(Long satelliteId) {
        QueryWrapper<OrbitKeplerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("satellite_id", satelliteId);
        queryWrapper.orderByDesc("update_time");
        return this.getOne(queryWrapper);
    }
}
