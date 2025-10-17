package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hit.telemetry_parser.entity.OrbitTleEntity;
import com.hit.telemetry_parser.mapper.OrbitTleMapper;
import com.hit.telemetry_parser.service.IOrbitTleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-28
 */
@Service
public class OrbitTleServiceImpl extends ServiceImpl<OrbitTleMapper, OrbitTleEntity> implements IOrbitTleService {

    @Override
    public OrbitTleEntity getBySatelliteId(Long satelliteId) {
        QueryWrapper<OrbitTleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("satellite_id", satelliteId);
        queryWrapper.orderByDesc("update_time");
        return this.getOne(queryWrapper);
    }
}
