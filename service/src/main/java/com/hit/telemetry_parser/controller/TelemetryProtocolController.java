package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.hit.telemetry_parser.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.ITelemetryProtocolService;
import com.hit.telemetry_parser.entity.TelemetryProtocolEntity;

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
@RequestMapping("/telemetry-protocol-entity")
@Tag(name = "TelemetryProtocolEntityController" , description = "前端控制器")
public class TelemetryProtocolController {

    @Resource
    private ITelemetryProtocolService telemetryProtocolEntityService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody TelemetryProtocolEntity telemetryProtocolEntity) {
        return Result.success(telemetryProtocolEntityService.saveOrUpdate(telemetryProtocolEntity));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(telemetryProtocolEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(telemetryProtocolEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<TelemetryProtocolEntity>> findAll() {
        return Result.success(telemetryProtocolEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<TelemetryProtocolEntity> findOne(@PathVariable Long id) {
        return Result.success(telemetryProtocolEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<TelemetryProtocolEntity>> findPage(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize) {

        QueryWrapper<TelemetryProtocolEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(telemetryProtocolEntityService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

