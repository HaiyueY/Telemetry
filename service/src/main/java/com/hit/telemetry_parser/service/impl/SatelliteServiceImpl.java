package com.hit.telemetry_parser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hit.telemetry_parser.common.schedule.OrbitPattern;
import com.hit.telemetry_parser.domain.bo.OrbitBO;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
import com.hit.telemetry_parser.domain.vo.SelectItemVO;
import com.hit.telemetry_parser.domain.vo.SubsystemVO;
import com.hit.telemetry_parser.entity.OrbitKeplerEntity;
import com.hit.telemetry_parser.entity.OrbitTleEntity;
import com.hit.telemetry_parser.entity.SatelliteEntity;
import com.hit.telemetry_parser.entity.SubsystemEntity;
import com.hit.telemetry_parser.mapper.SatelliteMapper;
import com.hit.telemetry_parser.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-28
 */
@Slf4j
@Service
public class SatelliteServiceImpl extends ServiceImpl<SatelliteMapper, SatelliteEntity> implements ISatelliteService {

    // region 服务类注入
    @Resource
    private IOrbitKeplerService orbitKeplerService;

    @Resource
    private IOrbitTleService orbitTleService;

    @Resource
    private ISubsystemService subsystemService;
    // endregion

    @Override
    public Page<SatelliteVO> page(Page<SatelliteEntity> entityPage) {
        Page<SatelliteVO> page = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<SatelliteVO> voList = new ArrayList<>();

        for (SatelliteEntity satelliteEntity : entityPage.getRecords()) {
            Long satId = satelliteEntity.getId();

            SatelliteVO satelliteVo = new SatelliteVO(satelliteEntity);

            // 根据卫星轨道加载方式查询轨道信息
            switch (OrbitPattern.getByValue(satelliteEntity.getOrbitType())) {
                case OE -> {
                    OrbitKeplerEntity kepler = orbitKeplerService.getBySatelliteId(satId);
                    satelliteVo.setKepler(kepler);
                }
                case TLE -> {
                    OrbitTleEntity tle = orbitTleService.getBySatelliteId(satId);
                    satelliteVo.setTle(tle);
                }
            }

            // 分系统查询
            satelliteVo.setSubsystems(subsystemService.querySubsystemById(satelliteEntity.getId(), Boolean.FALSE));
            voList.add(satelliteVo);
        }

        page.setRecords(voList);
        return page;
    }

    @Override
    public List<SelectItemVO> querySelectItemsByName(String name) {
        AtomicInteger key = new AtomicInteger();
        // 卫星名称模糊查询
        QueryWrapper<SatelliteEntity> satQW = new QueryWrapper<>();
        satQW.like("`name`", name).eq("status", true);
        List<SelectItemVO> selectItemVOS = new ArrayList<>(list(satQW).stream()
                .map(satelliteEntity -> {
                    List<SubsystemVO> voList = subsystemService.querySubsystemById(satelliteEntity.getId(), true);
                    return voList.stream().map(vo -> {
                        String voName = getSelectName(satelliteEntity, vo);
                        return new SelectItemVO(null, vo.getId(), voName);
                    }).toList();
                }).flatMap(List::stream).toList());

        // 分系统名称、nid模糊查询
        QueryWrapper<SubsystemEntity> subQW = new QueryWrapper<>();
        subQW.eq("status", true);
        subQW.and(queryWrapper -> queryWrapper.like("`name_c_n`", name)
                .or().like("name_e_n", name)
                .or().like("n_id", name));
        selectItemVOS.addAll(subsystemService.list(subQW).stream()
                .map(entity -> {
                    SatelliteEntity sat = getById(entity.getSatelliteId());
                    String voName = getSelectName(sat, new SubsystemVO(entity));
                    return new SelectItemVO(null, entity.getId(), voName);
                }).toList());

        // 去掉value重复的
        selectItemVOS = selectItemVOS.stream().distinct().toList();
        selectItemVOS.forEach(selectItemVO -> selectItemVO.setKey(key.getAndIncrement()));
        return selectItemVOS;
    }

    @Override
    public SatelliteVO queryBySubsystemId(Long subsystemId) {
        SubsystemEntity subsystem = subsystemService.getById(subsystemId);
        QueryWrapper<SatelliteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", subsystem.getSatelliteId());
        queryWrapper.eq("status", true);
        SatelliteEntity satellite = getOne(queryWrapper);
        return new SatelliteVO(satellite).setSubsystems(List.of(new SubsystemVO(subsystem)));
    }

    @Override
    public List<SatelliteVO> queryAllSatellite() {
        QueryWrapper<SatelliteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        queryWrapper.orderByDesc("update_time");
        return list(queryWrapper).stream().map(entity -> {
            List<SubsystemVO> subsystemVOList = subsystemService.querySubsystemById(entity.getId(), Boolean.TRUE);
            return new SatelliteVO(entity).setSubsystems(subsystemVOList);
        }).toList();
    }

    @Override
    public SubsystemEntity querySubsystemByNID(String nId) {
        QueryWrapper<SubsystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        queryWrapper.eq("n_id", nId);
        queryWrapper.orderByDesc("update_time");
        return subsystemService.getOne(queryWrapper);
    }

    /**
     * 获取视图名称
     *
     * @param entity 卫星
     * @param vo     分系统
     * @return 卫星名称 + 分系统名称 + NID
     */
    private static String getSelectName(SatelliteEntity entity, SubsystemVO vo) {
        return entity.getName() + " - " + vo.getNameCN() + "( " + vo.getNameEN() + " ) - " + vo.getNId();
    }

    /**
     * 根据卫星id查询卫星轨道信息
     *
     * @param id           卫星id
     * @param orbitPattern 轨道加载类型
     * @return 轨道业务对象, null表示没有
     */
    private OrbitBO queryOrbitBySatelliteId(Long id, OrbitPattern orbitPattern) {
        // 校验轨道是否配置
        switch (orbitPattern) {
            case OE -> {
                OrbitKeplerEntity bySatelliteId = orbitKeplerService.getBySatelliteId(id);
                return bySatelliteId == null ? null : new OrbitBO(bySatelliteId);
            }
            case TLE -> {
                OrbitTleEntity bySatelliteId = orbitTleService.getBySatelliteId(id);
                return bySatelliteId == null ? null : new OrbitBO(bySatelliteId);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public boolean saveOrUpdate(SatelliteVO vo) {
        SatelliteEntity entity = new SatelliteEntity(vo);
        return saveOrUpdate(entity);
    }
}
