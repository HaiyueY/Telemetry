package com.hit.telemetry_parser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.domain.vo.ProtocolParamVO;
import com.hit.telemetry_parser.entity.ProtocolParamEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

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
public interface IProtocolParamService extends IService<ProtocolParamEntity> {

    /**
     * 根据协议id查询协议参数
     *
     * @param protocolId 协议id
     * @return
     */
    List<ProtocolParamEntity> queryByProtocolId(Long protocolId);

    /**
     * 根据协议id查询协议参数视图对象
     *
     * @param protocolId 协议id
     * @return voList
     */
    List<ProtocolParamVO> queryVoByProtocolId(Long protocolId);

    /**
     * 查询视图对象
     * @param entityPage 实体
     * @return
     */
    Page<ProtocolParamVO> page(Page<ProtocolParamEntity> entityPage);

}
