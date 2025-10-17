package com.hit.telemetry_parser.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hit.telemetry_parser.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService extends IService<FileEntity> {

    /**
     * 存入文件
     *
     * @param file 文件
     * @return file url
     * @throws IOException 存入错误
     */
    String saveFile(MultipartFile file) throws IOException;

    /**
     * 存入CZML文件
     *
     * @param czmlJson  czml json
     * @param id        czml文件名称
     * @return file url
     * @throws IOException 存入错误
     */
    String saveCZMLFile(JSONArray czmlJson, Long id) throws IOException;

    /**
     * 查询场景仿真CZML文件
     *
     * @return file url
     */
    String queryScenarioCzmlFile();
}
