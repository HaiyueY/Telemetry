package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hit.telemetry_parser.entity.TelemetryProtocolEntity;
import com.hit.telemetry_parser.mapper.TelemetryProtocolMapper;
import com.hit.telemetry_parser.service.ITelemetryProtocolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
@Slf4j
@Service
public class TelemetryProtocolServiceImpl extends ServiceImpl<TelemetryProtocolMapper, TelemetryProtocolEntity> implements ITelemetryProtocolService {

    @Override
    public List<TelemetryProtocolEntity> queryProtocolBySubsystemId(Long id) {
        QueryWrapper<TelemetryProtocolEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("subsystem_id", id);
        queryWrapper1.eq("`status`", true);
        return this.list(queryWrapper1);
    }

    @Override
    public List<TelemetryProtocolEntity> queryProtocolBySubsystemId(Long id, String rec) {
        QueryWrapper<TelemetryProtocolEntity> protocolQW = new QueryWrapper<>();
        protocolQW.eq("subsystem_id", id);
        protocolQW.eq("rec", rec);
        protocolQW.eq("`status`", true);
        return this.list(protocolQW);
    }
}
