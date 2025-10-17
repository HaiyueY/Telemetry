package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.BaseType;
import com.hit.telemetry_parser.common.telemetry.ParamType;
import com.hit.telemetry_parser.common.telemetry.ProtocolSectionType;
import com.hit.telemetry_parser.domain.vo.ProtocolParamVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.hit.telemetry_parser.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.IProtocolParamService;
import com.hit.telemetry_parser.entity.ProtocolParamEntity;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-28
 */
@RestController
@RequestMapping("/protocol-param-entity")
@Tag(name = "ProtocolParamEntityController", description = "前端控制器")
public class ProtocolParamController {

    @Resource
    private IProtocolParamService protocolParamEntityService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody ProtocolParamEntity protocolParamEntity) {
        protocolParamEntity.setBase(BaseType.getFromLabel(protocolParamEntity.getBase()).getName());
        protocolParamEntity.setType(ProtocolSectionType.getFromLabel(protocolParamEntity.getType()).getName());
        protocolParamEntity.setDataType(ParamType.getFromLabel(protocolParamEntity.getDataType()).getName());
        return Result.success(protocolParamEntityService.saveOrUpdate(protocolParamEntity));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(protocolParamEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(protocolParamEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<ProtocolParamEntity>> findAll() {
        return Result.success(protocolParamEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<ProtocolParamEntity> findOne(@PathVariable Long id) {
        return Result.success(protocolParamEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<ProtocolParamVO>> findPage(@RequestParam Integer pageNum,
                                                  @RequestParam Integer pageSize,
                                                  @RequestParam(defaultValue = "") String id,
                                                  @RequestParam(defaultValue = "") String mode,
                                                  @RequestParam(defaultValue = "") String start,
                                                  @RequestParam(defaultValue = "") String end) {
        QueryWrapper<ProtocolParamEntity> queryWrapper = new QueryWrapper<>();
        // 无id则返回空, 无论哪种模式必须输入id
        if (!"".equals(id)) {
            queryWrapper.eq("protocol_id", id);
            queryWrapper.orderByAsc("offset_byte", "offset_bit");
            Page<ProtocolParamEntity> page = protocolParamEntityService.page(new Page<>(pageNum, pageSize), queryWrapper);

            return Result.success(protocolParamEntityService.page(page));
        } else {
            return Result.success(new Page<>());
        }
    }

    @Operation(summary = "参数类型查询")
    @GetMapping("/getParamType")
    public Result<Map<Integer, String>> getParamTypeMap() {
        return Result.success(ParamType.getNameLabelMap());
    }


    @Operation(summary = "区域类型查询")
    @GetMapping("/getSectionType")
    public Result<Map<Integer, String>> getSectionTypeMap() {
        return Result.success(ProtocolSectionType.getNameLabelMap());
    }

    @Operation(summary = "进制类型查询")
    @GetMapping("/getBaseType")
    public Result<Map<Integer, String>> getBaseTypeMap() {
        return Result.success(BaseType.getNameLabelMap());
    }
}

