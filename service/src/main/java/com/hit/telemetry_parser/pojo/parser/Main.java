package com.hit.telemetry_parser.pojo.parser;

import com.hit.telemetry_parser.pojo.parser.coreDecoder.BinaryUtils;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String frameStr = "\\x03$QQ\\x01\\xcb\\xc0\\xc0\\x08\\xaa\\xdf\\xaa\\x00\\xc6\\x00\\xaa\\x00\\x00\\x1a\\xe2\\xb4\\x03\\x91\\x0f\\x04\\x96\\n\\xdc\\x03\\xba\\xf9\\xd5FE\\x01\\xd4eA[,\\x16\\'u\\x03:\\x84if\\xc5\\x0c\\xff\\xa7\\xc1K\\x1a\\x93R\\xdf\\x03:\\xea\\x11\\x8eD(\\xfd\\xca@\\xe9\\x07\\x1b@\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\xc3V\\x1c\\x17\\xb3\\xff\\xff\\xff=\\xed}\\x9f\\xbf\\x8eJz\\x14\\x0f\\x00@&\\xff\\xff\\xf3\\xff\\xff\\xa8\\xff\\xff\\xf76_:\\xed8\\xa1\\x00\\x96\\x0f\\x02\\xf9{\\xff\\xc4\\x1cw\\x00\\xd7\\xb9\\x02\\x03\\xfd$\\xcd\\x00\\tS\\x97\\xff\\xf9%\\x93\\x00\\x01\\xfe\\xdb\\xdd\\x04\\x16\\xa7\\'\\x033|#\\\"\\x97\\xa5\\'O\\x85YH\\x1bm\\xa1D\\x01\\x11\\xbd\\x08\\x96\\x00\\xf9\\xa7\\xeev\\xfd\\x97\\r\\xaaU\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00U\\x00U\\x00\\xaa@\\xf9\\xdb\\xdd\\xa61\\xc0\\xc0\\xc0\\xc0\\xc0\\xc0\\xc0\\xc0";
        byte[] frame = BinaryUtils.parseRawData(frameStr);
        Map<String, ProtocolEntry> frameDict = new HashMap<>();
        frameDict.put("header", new ProtocolEntry(0, 6, 0));

        Map<String, ProtocolEntry> headerDict = new HashMap<>();

        Map<String, ProtocolEntry> bodyDict = new HashMap<>();
//        Decoder decoder = new Decoder(frameStr, frameDict).decodeHeader(headerDict).decodeBody(bodyDict);


        System.out.println("Byte array length: " + frame.length);
        // 打印前几个字节
        for (byte b : frame) {
            System.out.printf("%02X ", b);
        }

        // 校验长度

        // 获取Header
        byte[] header = Arrays.copyOfRange(frame, 0, 6);
        StringBuilder headerStringBuilder = new StringBuilder();
        for (byte b : header) {
            headerStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        String headerString = headerStringBuilder.toString();

        String substring = headerString.substring(0, 2);
        int version = Integer.parseInt(headerString.substring(0, 2), 2);     // 版本编号
        String id = Integer.toHexString(Integer.parseInt(headerString.substring(2, 12), 2)).toUpperCase();    // 卫星id（16进制）
        int channel = Integer.parseInt(headerString.substring(12, 15), 2);   // 虚拟信道id
        int spare = Integer.parseInt(headerString.substring(15, 16), 2);   // spare
        int masterCount = Integer.parseInt(headerString.substring(16, 24), 2);   // master_frame_count
        int channelCount = Integer.parseInt(headerString.substring(24, 32), 2);   // virtual_channel_frame_count
        int pointer = Integer.parseInt(headerString.substring(32, 48), 2);   // first_header_pointer

        // CRC校验
        byte[] body = Arrays.copyOfRange(frame, 6, frame.length);


        // 解析数据区（KISS解码）
        byte[] data = Arrays.copyOfRange(body, 7, body.length);

//        KISSDecoder decoder = new KISSDecoder();
//        byte[] streamData = {(byte) 0xC0, (byte) 0x01, (byte) 0xDB, (byte) 0xDC, (byte) 0x02, (byte) 0xC0};
//        decoder.appendStream(streamData);
//        Queue<byte[]> packets = decoder.getPackets();
//        for (byte[] packet : packets) {
//            System.out.println("Decoded packet: " + Arrays.toString(packet));
//        }
//
//        HighKISSDecoder highDecoder = new HighKISSDecoder();
//        byte[] frameInfo = {0x00, 0x00, 0x00, 0x00, 0x00, 0x01}; // Frame count = 1
//        byte[] frameData = {(byte) 0xC0, (byte) 0x03, (byte) 0x04, (byte) 0xC0};
//        highDecoder.appendFrame(frameInfo, frameData);
//        for (byte[] packet : highDecoder.getPackets()) {
//            System.out.println("HighDecoded packet: " + Arrays.toString(packet));
//        }
    }
}
