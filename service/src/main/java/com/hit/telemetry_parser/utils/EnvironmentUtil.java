package com.hit.telemetry_parser.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil implements ApplicationContextAware {
    private static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        env = applicationContext.getEnvironment();
    }

    public static String getProperty(String key) {
        return env.getProperty(key);
    }
}
