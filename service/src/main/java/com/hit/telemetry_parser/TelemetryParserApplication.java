package com.hit.telemetry_parser;

import com.hit.telemetry_parser.config.OrekitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class TelemetryParserApplication {

    public static void main(String[] args) {
        OrekitConfig.init();
        SpringApplication.run(TelemetryParserApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  遥测解析系统后端服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
