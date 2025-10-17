package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.Result;
import com.hit.telemetry_parser.common.schedule.ImagePattern;
import com.hit.telemetry_parser.common.schedule.OrbitPattern;
import com.hit.telemetry_parser.common.schedule.SensorShape;
import com.hit.telemetry_parser.common.schedule.SensorType;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
import com.hit.telemetry_parser.domain.vo.SelectItemVO;
import com.hit.telemetry_parser.entity.SatelliteEntity;
import com.hit.telemetry_parser.service.ISatelliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-05-28
 */
@Slf4j
@RestController
@RequestMapping("/satelliteEntity")
@Tag(name = "SatelliteController", description = "卫星前端控制器")
public class SatelliteController {

    @Resource
    private ISatelliteService satelliteService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody SatelliteVO satelliteVo) {
        return Result.success(satelliteService.saveOrUpdate(satelliteVo));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(satelliteService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(satelliteService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<SatelliteEntity>> findAll() {
        return Result.success(satelliteService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<SatelliteEntity> findOne(@PathVariable Long id) {
        return Result.success(satelliteService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<SatelliteVO>> findPage(@RequestParam Integer pageNum,
                                              @RequestParam Integer pageSize,
                                              @RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "") String noradId) {
        QueryWrapper<SatelliteEntity> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(noradId)) {
            queryWrapper.like("norad_id", noradId);
        }
        queryWrapper.orderByDesc("update_time");
        Page<SatelliteEntity> page = satelliteService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(satelliteService.page(page));
    }

    @Operation(summary = "不分页条件查询")
    @GetMapping("/queryByParams")
    public Result<List<SelectItemVO>> queryByParams(@RequestParam(defaultValue = "") String name) {
        if (!"".equals(name)) {
            return Result.success(satelliteService.querySelectItemsByName(name));
        } else {
            return Result.error(-1, "检索参数缺失, 请完善需求工单");
        }
    }

    @Operation(summary = "轨道加载类型查询")
    @GetMapping("/getOrbitTypes")
    public Result<Map<Integer, String>> getOrbitTypeMap() {
        return Result.success(OrbitPattern.getNameLabelMap());
    }

    @Operation(summary = "载荷类型查询")
    @GetMapping("/getSensorTypes")
    public Result<Map<Integer, String>> getSensorTypeMap() {
        return Result.success(SensorType.getNameLabelMap());
    }

    @Operation(summary = "成像模式查询")
    @GetMapping("/getImagePatterns")
    public Result<Map<Integer, String>> getImagePatternMap() {
        return Result.success(ImagePattern.getNameLabelMap());
    }

    @Operation(summary = "视场形状查询")
    @GetMapping("/getSensorShapes")
    public Result<Map<Integer, String>> getSensorShapesMap() {
        return Result.success(SensorShape.getNameLabelMap());
    }
}

