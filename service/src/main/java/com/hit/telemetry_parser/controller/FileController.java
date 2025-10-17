package com.hit.telemetry_parser.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hit.telemetry_parser.common.Result;
import com.hit.telemetry_parser.entity.FileEntity;
import com.hit.telemetry_parser.service.IFileService;
import com.hit.telemetry_parser.utils.IpUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文件上传相关接口
 *
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2022-09-25-14:57
 */
@RestController
@RequestMapping("/file")
@Tag(name = "FileController", description = "文件前端控制器")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IFileService filesService;

    /**
     * 仿真文件查询接口
     *
     * @param czmlName czml文件名称
     * @return Result<String>
     */
    @GetMapping("/czml/{czmlName}")
    public Result<String> getCzmlFileByName(@PathVariable String czmlName) {
        QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`name`", czmlName);
        queryWrapper.orderByDesc("update_time");
        List<FileEntity> list = filesService.list(queryWrapper);
        if (list.isEmpty()) {
            return Result.error(-1, "暂无可用仿真场景, 请等待");
        }
        // 返回最新仿真场景文件
        return Result.success("http://" + IpUtil.getLocalIpAddress() +":9095/" + list.get(0).getUrl());
    }

    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件
     * @return S
     * @throws IOException IOException
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) throws IOException {
        String uuid = this.filesService.saveFile(file);
        return Result.success(uuid);
    }

    /**
     * <a href="http://{{IP}}:9095/file/{fileUUID}">
     * 文件下载接口
     * </a>
     *
     * @param fileUUID fileUUID
     * @param response response
     * @throws IOException IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(HttpServletResponse response, @PathVariable String fileUUID) throws IOException {
        // 根据文件唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID,
                StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");
        // 读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }


}
