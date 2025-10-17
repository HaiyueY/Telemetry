package com.hit.telemetry_parser.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
import com.hit.telemetry_parser.domain.vo.SelectItemVO;
import com.hit.telemetry_parser.entity.SatelliteEntity;
import com.hit.telemetry_parser.entity.SubsystemEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-28
 */
public interface ISatelliteService extends IService<SatelliteEntity> {

    /**
     * 分页查询后整合分系统
     *
     * @return 卫星视图分页对象
     */
    Page<SatelliteVO> page(Page<SatelliteEntity> entityPage);

    /**
     * 模糊查询卫星-分系统
     * @param name 名称
     * @return
     */
    List<SelectItemVO> querySelectItemsByName(String name);

    /**
     * 根据分系统id查询卫星视图对象
     * @param subsystemId 分系统id
     * @return 包含分系统的视图对象
     */
    SatelliteVO queryBySubsystemId(Long subsystemId);

    /**
     * 查询所有卫星
     * @return 包含分系统的视图对象(均只查询<启用>status = true)
     */
    List<SatelliteVO> queryAllSatellite();

    SubsystemEntity querySubsystemByNID(String nId);

    /**
     * Save or update a Satellite Entity by its value object.
     *
     * @param satelliteVo Satellite value object
     * @return boolean
     */
    boolean saveOrUpdate(SatelliteVO satelliteVo);
}
