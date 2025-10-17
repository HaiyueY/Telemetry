package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.domain.vo.SatelliteVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.hit.telemetry_parser.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.ISubsystemService;
import com.hit.telemetry_parser.entity.SubsystemEntity;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
@RestController
@RequestMapping("/subsystemEntity")
@Tag(name = "SubsystemEntityController" , description = "前端控制器")
public class SubsystemController {

    @Resource
    private ISubsystemService subsystemEntityService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody SatelliteVO satelliteVO) {
        SubsystemEntity entity = new SubsystemEntity(satelliteVO.getSubsystems().get(0));
        entity.setSatelliteId(satelliteVO.getId());
        return Result.success(subsystemEntityService.saveOrUpdate(entity));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(subsystemEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(subsystemEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<SubsystemEntity>> findAll() {
        return Result.success(subsystemEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<SubsystemEntity> findOne(@PathVariable Long id) {
        return Result.success(subsystemEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<SubsystemEntity>> findPage(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize) {

        QueryWrapper<SubsystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(subsystemEntityService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

