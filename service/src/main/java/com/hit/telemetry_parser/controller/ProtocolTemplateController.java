package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.common.telemetry.ValidateType;
import com.hit.telemetry_parser.domain.vo.ProtocolTemplateVO;
import com.hit.telemetry_parser.domain.vo.SelectItemVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.hit.telemetry_parser.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.IProtocolTemplateService;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@RestController
@RequestMapping("/protocol-template-entity")
@Tag(name = "ProtocolTemplateEntityController" , description = "前端控制器")
public class ProtocolTemplateController {

    @Resource
    private IProtocolTemplateService protocolTemplateEntityService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody ProtocolTemplateEntity protocolTemplateEntity) {
        protocolTemplateEntity.setTemplateType(TTCType.getFromLabel(protocolTemplateEntity.getTemplateType()).getName());
        protocolTemplateEntity.setValidType(ValidateType.getFromLabel(protocolTemplateEntity.getValidType()).getName());
        return Result.success(protocolTemplateEntityService.saveOrUpdate(protocolTemplateEntity));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(protocolTemplateEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(protocolTemplateEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<ProtocolTemplateEntity>> findAll() {
        return Result.success(protocolTemplateEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<ProtocolTemplateEntity> findOne(@PathVariable Long id) {
        return Result.success(protocolTemplateEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<ProtocolTemplateVO>> findPage(@RequestParam Integer pageNum,
                                                     @RequestParam Integer pageSize) {

        QueryWrapper<ProtocolTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<ProtocolTemplateEntity> page = protocolTemplateEntityService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(protocolTemplateEntityService.page(page));
    }

    @Operation(summary = "不分页条件查询")
    @GetMapping("/queryByParams")
    public Result<List<SelectItemVO>> queryByParams(@RequestParam(defaultValue = "") String type,
                                                    @RequestParam(defaultValue = "") String name) {
        if (!"".equals(type)) {
            String typeName = TTCType.getFromLabel(type).name();
            QueryWrapper<ProtocolTemplateEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("`name`", name);
            queryWrapper.eq("template_type", typeName);
            queryWrapper.orderByDesc("update_time");
            AtomicInteger key = new AtomicInteger();
            return Result.success(protocolTemplateEntityService.list(queryWrapper)
                    .stream().map(entity -> new SelectItemVO(key.getAndIncrement(), entity.getId(),
                            type + " - " + entity.getName())).toList());
        } else {
            return Result.error(-1, "检索参数缺失, 请完善需求工单");
        }
    }

    @Operation(summary = "测控类型查询")
    @GetMapping("/getTTCType")
    public Result<Map<Integer, String>> getTTCTypeMap() {
        return Result.success(TTCType.getNameLabelMap());
    }

    @Operation(summary = "校验类型查询")
    @GetMapping("/getValidType")
    public Result<Map<Integer, String>> getValidTypeMap() {
        return Result.success(ValidateType.getNameLabelMap());
    }

}

