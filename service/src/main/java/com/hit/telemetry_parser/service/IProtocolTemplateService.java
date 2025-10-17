package com.hit.telemetry_parser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.domain.bo.ProtocolTemplateBo;
import com.hit.telemetry_parser.domain.vo.ProtocolTemplateVO;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
public interface IProtocolTemplateService extends IService<ProtocolTemplateEntity> {

    /**
     * 分页查询
     * @param entityPage
     * @return
     */
    Page<ProtocolTemplateVO> page(Page<ProtocolTemplateEntity> entityPage);

    /**
     * 根据id查询遥测模板
     *
     * @param id
     * @return
     */
    ProtocolTemplateBo queryBoById(Long id);


}
