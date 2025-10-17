package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.CategoryType;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.domain.bo.ProtocolBo;
import com.hit.telemetry_parser.domain.bo.ProtocolTemplateBo;
import com.hit.telemetry_parser.domain.vo.*;
import com.hit.telemetry_parser.entity.*;
import com.hit.telemetry_parser.mapper.ProtocolMapper;
import com.hit.telemetry_parser.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.orekit.time.AbsoluteDate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Slf4j
@Service
public class ProtocolServiceImpl extends ServiceImpl<ProtocolMapper, ProtocolEntity> implements IProtocolService {

    @Resource
    private ISatelliteService satelliteService;

    @Resource
    private IProtocolTemplateService templateService;

    @Resource
    private IProtocolParamService protocolParamService;

    @Override
    public ProtocolTemplateBo queryTemplateByProtocolId(Long protocolId) {
        ProtocolEntity protocol = this.getById(protocolId);
        return templateService.queryBoById(protocol.getTemplateId());
    }

    @Override
    public ProtocolBo queryBoById(Long id) {
        ProtocolEntity protocol = this.getById(id);
        return new ProtocolBo(protocol)
                .setParams(protocolParamService.queryByProtocolId(protocol.getId()));
    }

    @Override
    public List<ProtocolEntity> queryBySubsystemId(Long subsystemId, TTCType... types) {
        QueryWrapper<ProtocolEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subsystem_id", subsystemId);
        if (types != null && types.length > 0) {
            queryWrapper.in("`type`", Arrays.stream(types).map(TTCType::name).toList());
        }
        return this.list(queryWrapper);
    }

    @Override
    public Page<ProtocolVO> page(Page<ProtocolEntity> entityPage) {
        Page<ProtocolVO> page = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<ProtocolVO> voList = new ArrayList<>();

        for (ProtocolEntity entity : entityPage.getRecords()) {

            ProtocolTemplateVO templateVO = new ProtocolTemplateVO(templateService.getById(entity.getTemplateId()));
            ProtocolVO vo = new ProtocolVO(entity)
                    .setSatellite(satelliteService.queryBySubsystemId(entity.getSubsystemId()))
                    .setTemplate(templateVO)
                    .setParams(protocolParamService.queryVoByProtocolId(entity.getId()));
            voList.add(vo);
        }

        page.setRecords(voList);
        return page;
    }

    @Override
    public List<CategoryVO> queryCategory(TTCType type) {
        return satelliteService.queryAllSatellite().stream()
                // 第一级：卫星
                .map(satellite -> new CategoryVO(satellite.getId(), null, satellite.getName(),
                        CategoryType.SATELLITE.getLabel(), satellite.getSubsystems().stream()
                        // 第二级：分系统
                        .map(subsystem -> new CategoryVO(subsystem.getId(), satellite.getId(),
                                subsystem.getNameCN() + " - " + subsystem.getNameEN(), CategoryType.SUBSYSTEM.getLabel(),
                                queryBySubsystemId(subsystem.getId(), type).stream()
                                        // 第三级：协议
                                        .map(protocol -> new CategoryVO(protocol.getId(), subsystem.getId(),
                                                protocol.getName(), CategoryType.PROTOCOL.getLabel(), List.of()))
                                        .toList()))
                        .toList()))
                .toList();
    }

    @Override
    public Map<Long, ProtocolParamEntity> getByCat(String nId, String rec, TTCType type) {
        // 查询分系统
        SubsystemEntity subsystem = satelliteService.querySubsystemByNID(nId);
        AbsoluteDate absoluteDate = new AbsoluteDate();
        absoluteDate.toString();
        // 查询协议
        QueryWrapper<ProtocolEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        queryWrapper.eq("subsystem_id", subsystem.getId());
        queryWrapper.eq("rec", rec);
        ProtocolEntity protocol = this.getOne(queryWrapper);
        // 查询模板
        //TODO: 查询模板参数
        ProtocolTemplateEntity template = templateService.getById(protocol.getTemplateId());
        // 查询参数
        List<ProtocolParamEntity> params = protocolParamService.queryByProtocolId(protocol.getId());
        Map<Long, ProtocolParamEntity> map = new HashMap<>(16);
        params.forEach(param -> map.put(param.getId(), param));
        return map;
    }
}
