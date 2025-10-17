package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.domain.vo.SubsystemVO;
import com.hit.telemetry_parser.entity.ProtocolEntity;
import com.hit.telemetry_parser.entity.SubsystemEntity;
import com.hit.telemetry_parser.mapper.SubsystemMapper;
import com.hit.telemetry_parser.service.IProtocolService;
import com.hit.telemetry_parser.service.ISubsystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
@Service
public class SubsystemServiceImpl extends ServiceImpl<SubsystemMapper, SubsystemEntity> implements ISubsystemService {

    @Resource
    @Lazy
    private IProtocolService protocolService;

    @Override
    public SubsystemEntity querySubsystemById(Long satId, String nID) {
        QueryWrapper<SubsystemEntity> subsystemQW = new QueryWrapper<>();
        subsystemQW.eq("satellite_id", satId);
        subsystemQW.eq("n_id", nID);
        subsystemQW.eq("`status`", true);
        return this.getOne(subsystemQW);
    }

    @Override
    public List<SubsystemVO> querySubsystemById(Long satId, Boolean status) {
        QueryWrapper<SubsystemEntity> subsystemQW = new QueryWrapper<>();
        subsystemQW.eq("satellite_id", satId);
        if (status) {
            subsystemQW.eq("`status`", true);
        }
        List<SubsystemEntity> enetityList = list(subsystemQW);
        List<SubsystemVO> voList = new ArrayList<>();
        if (enetityList.isEmpty()) {
            return List.of();
        } else {
            // 区分遥控遥测协议

            enetityList.forEach(entity -> voList
                    .add(new SubsystemVO(entity)
                            .setTelemetryProtocols(protocolService
                                    .queryBySubsystemId(entity.getId(), TTCType.TELEMETRY)
                                    .stream()
                                    .map(ProtocolEntity::getRec)
                                    .toList())
                            .setCommandProtocols(protocolService
                                    .queryBySubsystemId(entity.getId(), TTCType.COMMAND)
                                    .stream()
                                    .map(ProtocolEntity::getRec)
                                    .toList())));
        }

        return voList;
    }
}
