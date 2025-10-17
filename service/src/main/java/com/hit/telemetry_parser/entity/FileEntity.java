package com.hit.telemetry_parser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2024-09-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("file_property")
@Schema(title = "FileEntity对象" , description = "")
public class FileEntity extends Model<FileEntity> {

    @Schema(title = "id" , description = "id")
    @TableId(value = "id" , type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(title = "`name`" , description = "名称")
    @TableField("`name`")
    private String name;

    @Schema(title = "`type`" , description = "文件类型")
    @TableField("`type`")
    private String type;

    @Schema(title = "size" , description = "文件大小")
    @TableField("size")
    private Long size;

    @Schema(title = "url" , description = "链接")
    @TableField("url")
    private String url;

    @Schema(title = "md5" , description = "md5码")
    @TableField("md5")
    private String md5;

    @Schema(title = "`enable`" , description = "是否启用")
    @TableField("`enable`")
    private Boolean enable;

    @Schema(title = "update_time" , description = "更新时间")
    @TableField(value = "update_time" , fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}