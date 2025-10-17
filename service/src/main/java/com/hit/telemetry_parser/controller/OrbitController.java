package com.hit.telemetry_parser.controller;


import com.hit.telemetry_parser.common.Result;
import com.hit.telemetry_parser.common.schedule.OrbitPattern;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
import com.hit.telemetry_parser.service.IOrbitKeplerService;
import com.hit.telemetry_parser.service.IOrbitTleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-08
 */
@Slf4j
@RestController
@RequestMapping("/orbitEntity")
@Tag(name = "OrbitController", description = "轨道前端控制器")
public class OrbitController {

    @Resource
    private IOrbitKeplerService kepService;

    @Resource
    private IOrbitTleService telService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody SatelliteVO vo) {
        Long satelliteId = vo.getId();
        boolean b;

        switch (OrbitPattern.getByLabel(vo.getOrbitType())) {
            case OE -> b = kepService.saveOrUpdate(vo.getKepler().setSatelliteId(satelliteId));
            case TLE -> b = telService.saveOrUpdate(vo.getTle().setSatelliteId(satelliteId));
            default -> throw new IllegalArgumentException("Invalid orbit type: " + vo.getOrbitType());
        }

        return Result.success(b);
    }
}