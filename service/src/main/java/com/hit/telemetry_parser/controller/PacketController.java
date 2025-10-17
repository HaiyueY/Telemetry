package com.hit.telemetry_parser.controller;

import com.hit.telemetry_parser.common.telemetry.ReceiveType;
import com.hit.telemetry_parser.component.netty.NettyServerHandler;
import com.hit.telemetry_parser.service.ITelemetryProcessService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.hit.telemetry_parser.common.Result;
import jakarta.annotation.Resource;

import com.hit.telemetry_parser.service.IPacketService;
import com.hit.telemetry_parser.entity.PacketEntity;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-06-10
 */
@Slf4j
@RestController
@RequestMapping("/packet-entity")
@Tag(name = "PacketEntityController" , description = "前端控制器")
public class PacketController {

    @Resource
    private NettyServerHandler nettyServerHandler;

    @Resource
    private IPacketService packetEntityService;

    @Resource
    private ITelemetryProcessService processService;

    @Operation(summary = "增/改")
    @PostMapping
    public Result<Boolean> save(@RequestBody PacketEntity packetEntity) {
        return Result.success(packetEntityService.saveOrUpdate(packetEntity));
    }

    @Operation(summary = "删")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(packetEntityService.removeById(id));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/del/batch")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.success(packetEntityService.removeBatchByIds(ids));
    }

    @Operation(summary = "查询全部")
    @GetMapping
    public Result<List<PacketEntity>> findAll() {
        return Result.success(packetEntityService.list());
    }

    @Operation(summary = "按id查询")
    @GetMapping("/{id}")
    public Result<PacketEntity> findOne(@PathVariable Long id) {
        return Result.success(packetEntityService.getById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<List<PacketEntity>> findPage(@RequestParam(defaultValue = "") String protocolId,
                                               @RequestParam(defaultValue = "") String mode,
                                               @RequestParam(defaultValue = "") String start,
                                               @RequestParam(defaultValue = "") String end) {
        if (!"".equals(protocolId) && !"".equals(mode)) {
            switch (ReceiveType.receiveType(mode)) {
                case HISTORY -> {
                    //TODO 历史查询模式
                    return Result.success(List.of());
                }
                case REALTIME -> {
                    processService.destroyClient();
                    processService.callClient();
                }
            }
        }
//        QueryWrapper<PacketEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
        return Result.success(List.of());
    }

    @Operation(summary = "TCP接口测试")
    @GetMapping("/tcp-test")
    public Result tcpTest(@RequestParam(defaultValue = "") String frame) {
        nettyServerHandler.broadcast(frame);
        return Result.success();
    }
}

