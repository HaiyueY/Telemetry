package com.hit.telemetry_parser.service;

import com.hit.telemetry_parser.entity.OrbitTleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-28
 */
public interface IOrbitTleService extends IService<OrbitTleEntity> {

    OrbitTleEntity getBySatelliteId(Long satelliteId);

}
