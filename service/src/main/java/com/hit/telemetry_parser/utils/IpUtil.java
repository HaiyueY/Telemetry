package com.hit.telemetry_parser.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IpUtil {

    private static final String LOCAL_IP = "localhost";

    public static String getLocalIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
//            return inetAddress.getHostAddress();
            return LOCAL_IP;
        } catch (UnknownHostException e) {
            log.error("获取服务端ip失败", e);
            return null;
        }
    }
}
