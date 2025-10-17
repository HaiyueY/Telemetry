package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.domain.bo.ProtocolTemplateBo;
import com.hit.telemetry_parser.domain.vo.ProtocolTemplateVO;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;
import com.hit.telemetry_parser.mapper.ProtocolTemplateMapper;
import com.hit.telemetry_parser.service.IProtocolParamService;
import com.hit.telemetry_parser.service.IProtocolTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@Service
public class ProtocolTemplateServiceImpl extends ServiceImpl<ProtocolTemplateMapper, ProtocolTemplateEntity> implements IProtocolTemplateService {

    @Resource
    private IProtocolParamService paramService;

    @Override
    public Page<ProtocolTemplateVO> page(Page<ProtocolTemplateEntity> entityPage) {
        Page<ProtocolTemplateVO> page = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<ProtocolTemplateVO> voList = new ArrayList<>();

        for (ProtocolTemplateEntity entity : entityPage.getRecords()) {
            ProtocolTemplateVO vo = new ProtocolTemplateVO(entity);
            // 协议参数查询
            vo.setParams(paramService.queryVoByProtocolId(entity.getId()));
            voList.add(vo);
        }

        page.setRecords(voList);
        return page;
    }

    @Override
    public ProtocolTemplateBo queryBoById(Long id) {
        ProtocolTemplateEntity byId = this.getById(id);
        if (byId != null) {
            ProtocolTemplateBo bo = new ProtocolTemplateBo(byId);
            bo.setParams(paramService.queryByProtocolId(id));
            return bo;
        }
        return null;
    }
}
