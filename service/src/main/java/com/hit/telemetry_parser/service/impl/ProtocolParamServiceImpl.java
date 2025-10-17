package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.ParamType;
import com.hit.telemetry_parser.common.telemetry.ProtocolSectionType;
import com.hit.telemetry_parser.component.netty.NettyClient;
import com.hit.telemetry_parser.domain.vo.ProtocolParamVO;
import com.hit.telemetry_parser.entity.ProtocolParamEntity;
import com.hit.telemetry_parser.mapper.ProtocolParamMapper;
import com.hit.telemetry_parser.service.IProtocolParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Service
public class ProtocolParamServiceImpl extends ServiceImpl<ProtocolParamMapper, ProtocolParamEntity> implements IProtocolParamService {


    @Override
    public List<ProtocolParamEntity> queryByProtocolId(Long protocolId) {
        List<ProtocolParamEntity> header = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.HEADER.getName())
                .orderByAsc("offset_byte", "offset_bit"));
        List<ProtocolParamEntity> body = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.BODY.getName())
                .orderByAsc("offset_byte", "offset_bit"));
        List<ProtocolParamEntity> valid = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.VALID.getName())
                .orderByAsc("offset_byte", "offset_bit"));
        return new ArrayList<>() {{
            addAll(header);
            addAll(body);
            addAll(valid);
        }};
    }

    @Override
    public List<ProtocolParamVO> queryVoByProtocolId(Long protocolId) {
        // 按照包头-数据-校验的顺序查询
        List<ProtocolParamVO> header = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.HEADER.getName())
                .orderByAsc("offset_byte", "offset_bit")).stream()
                .map(ProtocolParamVO::new)
                .toList();
        List<ProtocolParamVO> body = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.BODY.getName())
                .orderByAsc("offset_byte", "offset_bit")).stream()
                .map(ProtocolParamVO::new)
                .toList();
        List<ProtocolParamVO> valid = list(new QueryWrapper<ProtocolParamEntity>()
                .eq("protocol_id", protocolId)
                .eq("`type`", ProtocolSectionType.VALID.getName())
                .orderByAsc("offset_byte", "offset_bit")).stream()
                .map(ProtocolParamVO::new)
                .toList();
        return new ArrayList<>() {{
            addAll(header);
            addAll(body);
            addAll(valid);
        }};
    }

    @Override
    public Page<ProtocolParamVO> page(Page<ProtocolParamEntity> entityPage) {
        Page<ProtocolParamVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(ProtocolParamVO::new).toList());
        return voPage;
    }



}
