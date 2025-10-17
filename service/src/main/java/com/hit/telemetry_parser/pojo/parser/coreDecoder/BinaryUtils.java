package com.hit.telemetry_parser.pojo.parser.coreDecoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 遥测处理工具类
 * @author Haiyue Yang
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2025-04-30
 */
public class BinaryUtils {
    /**
     * 功能：对二进制数据的分割，并转换成可计算的数据
     * @param offset 偏移量
     * @param width 位宽
     * @param bData 二进制数据流（字节数组）
     * @return int
     */
    public static int readBinData(int offset, int width, byte[] bData) {
        int hByte = offset / 8; // 起始 byte 位置
        int hBit = offset % 8;
        int lByte = (offset + width) / 8; // 终止 byte 位置
        int lBit = (offset + width) % 8;

        // 提取所需字节
        byte[] data;
        if (lBit != 0) {
            // 如果数据跨字节且偏移量不是 8 的整数倍，包含终止 byte
            data = Arrays.copyOfRange(bData, hByte, lByte + 1);
        } else {
            data = Arrays.copyOfRange(bData, hByte, lByte);
        }

        // 去头
        data[0] = (byte) ((data[0] << hBit) & 0xFF); // 左移后保留低 8 位
        data[0] = (byte) (data[0] >>> hBit); // 无符号右移

        if (lBit != 0) {
            // 去尾
            data[data.length - 1] = (byte) (data[data.length - 1] >>> (8 - lBit));
            data[data.length - 1] = (byte) (data[data.length - 1] << (8 - lBit));
        }

        // 转 int
        int ans = 0;
        for (byte b : data) {
            ans = ans << 8;
            ans += (b & 0xFF); // 转换为无符号整数
        }

        if (lBit != 0) {
            ans = ans >>> (8 - lBit); // 无符号右移
        }

        return ans;
    }

    /**
     * 初步处理：将16进制raw_data转换为byte数组
     * @param rawData 原始数据
     * @return 初步处理后的byte数组
     */
    public static byte[] parseRawData(String rawData) {
        List<Byte> bytes = new ArrayList<>();

        String[] split = rawData.split(" ");
        int length = split.length;

        for (String s : split) {
            if (!s.startsWith("x")) {
                // TODO 现在数传开头不是x了
                String hex = s.substring(0, 2);
                bytes.add((byte) Integer.parseInt(hex, 16));
//                // ASCII
//                for (byte b : s.getBytes(StandardCharsets.US_ASCII)) {
//                    bytes.add(b);
//                }
            } else if (s.length() > 3) {
                // 16进制与ASCII混合
                String hex = s.substring(1, 3);
                bytes.add((byte) Integer.parseInt(hex, 16));
                String s1 = s.substring(3);
                for (byte b : s1.getBytes(StandardCharsets.US_ASCII)) {
                    bytes.add(b);
                }
            } else {
                // 普通16进制
//                String hex = s.substring(1, 3);
                String hex = s.substring(0, 2);
                bytes.add((byte) Integer.parseInt(hex, 16));
            }
        }

        // 转换为 byte 数组
        byte[] result = new byte[bytes.size()];
        for (int j = 0; j < bytes.size(); j++) {
            result[j] = bytes.get(j);
        }
        return result;
    }


}
