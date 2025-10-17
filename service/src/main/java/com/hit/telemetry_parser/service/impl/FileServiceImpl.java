package com.hit.telemetry_parser.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hit.telemetry_parser.entity.FileEntity;
import com.hit.telemetry_parser.mapper.FileMapper;
import com.hit.telemetry_parser.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements IFileService {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
        }

        // 上传文件到磁盘
        file.transferTo(uploadFile);
        String url;
        // 获取MD5
        String md5 = SecureUtil.md5(uploadFile);
        // 从数据库查询是否存在相同的记录
        FileEntity dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            // 由于文件已存在，所以删除刚才上传的重复文件
            boolean delete = uploadFile.delete();
        } else {
            // 数据库若不存在重复文件，则不删除刚才上传的重复文件
            url = "file/" + fileUUID;
        }

        // 存储数据库
        FileEntity saveFile = new FileEntity();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        this.save(saveFile);
        return url;
    }

    @Override
    public String saveCZMLFile(JSONArray czmlJson, Long id) {
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + "czml";
        File outputFile = new File(fileUploadPath + fileUUID);

        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
        }
        File file = FileUtil.writeUtf8String(czmlJson.toJSONString(2), outputFile);
        String url = "file/" + fileUUID;
        // 获取MD5
        String md5 = SecureUtil.md5(file);

        // 存储数据库
        FileEntity saveFile = new FileEntity();
        saveFile.setName(String.valueOf(id));
        saveFile.setType("czml");
        saveFile.setSize(file.getTotalSpace() / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        this.save(saveFile);
        return url;
    }

    @Override
    public String queryScenarioCzmlFile() {
        return "";
    }

    /**
     * 通过文件MD5查询文件
     *
     * @param md5 MD5
     * @return FilesEntity
     */
    private FileEntity getFileByMd5(String md5) {
        // 查询文件的MD5是否存在
        QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<FileEntity> filesList = this.list(queryWrapper);

        return filesList.isEmpty() ? null : filesList.get(0);
    }
}
