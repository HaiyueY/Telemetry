package com.hit.telemetry_parser.service;

import com.hit.telemetry_parser.domain.vo.SubsystemVO;
import com.hit.telemetry_parser.entity.ProtocolEntity;
import com.hit.telemetry_parser.entity.SubsystemEntity;
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
public interface ISubsystemService extends IService<SubsystemEntity> {

    /**
     * 根据卫星id和分系统标识查询分系统
     * @param satId 卫星id
     * @param nID   分系统标识
     * @return
     */
    SubsystemEntity querySubsystemById(Long satId, String nID);

    /**
     * 根据卫星id查询分系统
     * @param satId     卫星id
     * @param status    状态(true: 查询启用的, false: 查询所有)
     * @return
     */
    List<SubsystemVO> querySubsystemById(Long satId, Boolean status);



}
