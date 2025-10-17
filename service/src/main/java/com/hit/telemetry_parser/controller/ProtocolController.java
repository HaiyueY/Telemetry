package com.hit.telemetry_parser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hit.telemetry_parser.common.telemetry.TTCType;
import com.hit.telemetry_parser.domain.vo.CategoryVO;
import com.hit.telemetry_parser.domain.vo.ProtocolVO;
import com.hit.telemetry_parser.entity.ProtocolTemplateEntity;
import com.hit.telemetry_parser.service.IProtocolTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.hit.telemetry_parser.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.IProtocolService;
import com.hit.telemetry_parser.entity.ProtocolEntity;

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
@RequestMapping("/protocol-entity")
@Tag(name = "ProtocolEntityController" , description = "前端控制器")
public class ProtocolController {

    @Resource
    private IProtocolService protocolEntityService;

    @Resource
    private IProtocolTemplateService templateService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody ProtocolEntity protocolEntity) {
        ProtocolTemplateEntity byId = templateService.getById(protocolEntity.getTemplateId());
        return Result.success(protocolEntityService.saveOrUpdate(protocolEntity.setType(byId.getTemplateType())));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(protocolEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(protocolEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<ProtocolEntity>> findAll() {
        return Result.success(protocolEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<ProtocolEntity> findOne(@PathVariable Long id) {
        return Result.success(protocolEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<ProtocolVO>> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize) {

        QueryWrapper<ProtocolEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<ProtocolEntity> page = protocolEntityService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(protocolEntityService.page(page));
    }

    @Operation(summary = "树结构查询")
    @GetMapping("/tree")
    public Result<List<CategoryVO>> findCategoryTree(@RequestParam(defaultValue = "遥测") String type) {
        TTCType ttc = TTCType.getFromLabel(type);
        return Result.success(protocolEntityService.queryCategory(ttc));
    }
}

