package com.hit.telemetry_parser.service;

import com.hit.telemetry_parser.entity.TelemetryProtocolEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
public interface ITelemetryProtocolService extends IService<TelemetryProtocolEntity> {

    /**
     * 根据分系统id查询协议参数
     * @param id 分系统id
     * @return
     */
    List<TelemetryProtocolEntity> queryProtocolBySubsystemId(Long id);

    /**
     * 根据分系统id和副类型查询协议参数
     * @param id    分系统id
     * @param rec   副类型（协议标识）
     * @return
     */
    List<TelemetryProtocolEntity> queryProtocolBySubsystemId(Long id, String rec);


}
