package com.hit.telemetry_parser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.domain.bo.ProtocolBo;
import com.hit.telemetry_parser.domain.bo.ProtocolTemplateBo;
import com.hit.telemetry_parser.domain.vo.CategoryVO;
import com.hit.telemetry_parser.domain.vo.ProtocolVO;
import com.hit.telemetry_parser.entity.ProtocolEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hit.telemetry_parser.entity.ProtocolParamEntity;
import com.hit.telemetry_parser.entity.TelemetryProtocolEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
public interface IProtocolService extends IService<ProtocolEntity> {

    ProtocolTemplateBo queryTemplateByProtocolId(Long protocolId);

    ProtocolBo queryBoById(Long id);

    List<ProtocolEntity> queryBySubsystemId(Long subsystemId, TTCType... types);

    /**
     * 分页查询后整合分系统, 协议模板, 参数
     *
     * @return 协议视图分页对象
     */
    Page<ProtocolVO> page(Page<ProtocolEntity> page);

    List<CategoryVO> queryCategory(TTCType type);

    Map<Long, ProtocolParamEntity> getByCat(String nId, String rec, TTCType type);
}
