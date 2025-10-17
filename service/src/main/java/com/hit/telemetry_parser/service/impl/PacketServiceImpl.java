package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.component.netty.NettyClient;
import com.hit.telemetry_parser.entity.PacketEntity;
import com.hit.telemetry_parser.mapper.PacketMapper;
import com.hit.telemetry_parser.service.IPacketService;
import com.hit.telemetry_parser.service.ITelemetryProcessService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-06-10
 */
@Slf4j
@Service
public class PacketServiceImpl extends ServiceImpl<PacketMapper, PacketEntity> implements IPacketService {
}
