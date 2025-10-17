package com.hit.telemetry_parser.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

public class CodeGenerator {
    public static void main(String[] args) {
        // 1.数据库配置
        new AutoGenerator(new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/sat_com_db",
                "root", "123456")
                .dbQuery(new MySqlQuery())
                .schema("sat_com_db")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build())
                // 2.全局配置
                .global(new GlobalConfig.Builder()
                        .outputDir("D:\\projects\\Telemetry\\service\\src\\main\\java\\")
//                        .outputDir("C:\\Users\\23537\\Downloads\\test\\")
                        .author("卫星技术研究所_杨海岳")
                        .enableSwagger()
                        .dateType(DateType.TIME_PACK)
                        .commentDate("yyyy-MM-dd")
                        .build())
                // 3.包配置
                .packageInfo(new PackageConfig.Builder()
                        .parent("com.hit.telemetry_parser")
//                .moduleName("sys")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .controller("controller")
//                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://"))
                        .build())
                // 4.策略配置
                .strategy(new StrategyConfig.Builder()
                        .enableCapitalMode()
                        .enableSkipView()
                        .disableSqlFilter()
                        .addInclude("packet_property")
//                .addInclude("t_simple")
                        .addTablePrefix("satellite_", "c_")
                        .addTableSuffix("_property")
//                        .addFieldSuffix("_property")
                        // 数据实体层策略
                        .entityBuilder()
//                        .disableSerialVersionUID()
                        .enableChainModel()
                        .enableLombok()
                        .enableRemoveIsPrefix()
                        .enableTableFieldAnnotation()
                        .enableActiveRecord()
                        .versionColumnName("version")
                        .versionPropertyName("version")
                        .logicDeleteColumnName("deleted")
                        .logicDeletePropertyName("deleteFlag")
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
//                        .addSuperEntityColumns("id", "created_by", "created_time", "updated_by", "updated_time")
//                        .addIgnoreColumns("age")
                        .addTableFills(new Column("create_time", FieldFill.DEFAULT))
                        .addTableFills(new Property("updateTime", FieldFill.DEFAULT))
                        .idType(IdType.ASSIGN_ID)
                        .formatFileName("%sEntity")
                        .controllerBuilder().enableRestStyle().enableHyphenStyle().enableFileOverride()
                        .entityBuilder().enableFileOverride()
                        .serviceBuilder().enableFileOverride()
                        .mapperBuilder().enableFileOverride()
                        .build())
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
