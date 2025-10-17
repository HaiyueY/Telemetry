package com.hit.telemetry_parser.pojo.parser;

import com.hit.telemetry_parser.pojo.parser.coreDecoder.BinaryUtils;
import com.hit.telemetry_parser.pojo.parser.coreDecoder.KISSDecoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 原始帧数据处理器
 * <p>
 *     用于帧的数据解析，包括
 *     1. CRC校验
 *     2. 主包头解析
 *     3. 虚拟信道区分
 *     4. KISS解码
 *     5. 副包头解析
 *     6. 数据区解析计算
 * </p>
 * @author Haiyue Yang
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2025-04-30
 */
@Slf4j
public class Decoder {
    /**
     * KISS解码器
     */
    private final KISSDecoder kissDecoder;

    /**
     * CRC校验器
     * TODO：实现CRC校验
     */

    /**
     * 数据帧整体结构协议字典
     */
    private final Map<String, ProtocolEntry> frameDict;

    /**
     * 原始帧数据
     */
    private final byte[] frame;

    /**
     * 主包头原始帧
     */
    private byte[] headerBytes;

    /**
     * 数据区原始帧
     */
    private byte[] bodyBytes;

    /**
     * 数据区原始帧
     */
    private byte[] dataFrame;

    /**
     * 主包头
     */
    @Getter
    private final Map<String, Integer> header;

    /**
     * 副导头
     */
    @Getter
    private final Map<String, Integer> secHeader;

    /**
     * 有效数据
     */
    @Getter
    private final Map<String, Integer> data;


    /**
     * 通过原始帧传入进行构造
     * @param frameStr  原始帧字符串
     * @param frameDict 数据帧整体结构协议字典
     *                  至少包括：
     *                  <p>1. 包主导头 header</p>
     *                  <p>2. 数据区 body</p>
     *                  <p>3. 包副导头 secHeader</p>
     */
    public Decoder(String frameStr, Map<String, ProtocolEntry> frameDict) {
        // 解码器初始化
        this.kissDecoder = new KISSDecoder();
        this.frameDict = frameDict;
        this.frame = BinaryUtils.parseRawData(frameStr);
        this.header = new HashMap<>(16);
        this.secHeader = new HashMap<>(16);
        this.data = new HashMap<>(16);
        // 校验数据包长度是否合法
        if (this.frame.length != frameDict.get("length").getWidth()) {
            log.error("数据包长度不合法, frame length: {}", this.frame.length);
            // 长度不合法抛出错误终止解包
            throw new IllegalArgumentException();
        }
    }

    /**
     * 解析包头数据
     *
     * @param headerDict 包主导头帧字典
     */
//    public static byte[] decodeHeader(ProtocolEntry headerDict, byte[] frame) {
//        // 包主导头
//        int offset = headerDict.getOffset();
//        int width = headerDict.getWidth();
//        if (width % 8 != 0 && offset % 8 != 0) {
//            log.error("包主导头长度和偏移量必须是8字节的倍数, width: {}, offset: {}", width, offset);
//
//        }
//        int start = offset / 8;
//        int end = (offset + width) / 8;
//        this.headerBytes = Arrays.copyOfRange(frame, start, end);
//        StringBuilder headerStringBuilder = new StringBuilder();
//        for (byte b : this.headerBytes) {
//            headerStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
//        }
//        String headerString = headerStringBuilder.toString();
//        headerDict.forEach((key, protocol) -> {
//            int protocolWidth = protocol.getWidth();
//            int protocolOffset = protocol.getOffset();
//            if (protocolWidth + protocolOffset > width) {
//                log.error("包头数据的位置不得超出包头区, width: {}, offset: {}", protocolWidth, protocolOffset);
//            } else {
//                int value = Integer.parseInt(headerString.substring(protocolOffset, protocolOffset + protocolWidth), 2);
//                this.header.put(key, value);
//            }
//        });
//
//        return this;
//    }

    /**
     * 解析包副导头数据
     * @param packetDict 包副导头协议字典
     */
    public Decoder decodeSecHeader(Map<String, ProtocolEntry> packetDict) {
        int offset = frameDict.get("body").getOffset();
        int width = frameDict.get("body").getWidth();
        if (width % 8 != 0 && offset % 8 != 0) {
            log.error("数据区长度和偏移量必须是8字节的倍数, width: {}, offset: {}", width, offset);
            return this;
        }
        int start = offset / 8;
        int end = (offset + width) / 8;
        byte[] bodyBytes = Arrays.copyOfRange(frame, start, end);
        // KISS解码
        this.kissDecoder.appendStream(bodyBytes);
        this.bodyBytes = this.kissDecoder.getPackets().element();
        StringBuilder bodyStringBuilder = new StringBuilder();
        for (byte b : this.bodyBytes) {
            bodyStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        String bodyString = bodyStringBuilder.toString();
        packetDict.forEach((key, protocol) -> {
            int protocolWidth = protocol.getWidth();
            int protocolOffset = protocol.getOffset();
            if (protocolWidth + protocolOffset > width) {
                log.error("包副导头数据的位置不得超出副导头区, width: {}, offset: {}", protocolWidth, protocolOffset);
            } else {
                int value = Integer.parseInt(bodyString.substring(protocolOffset, protocolOffset + protocolWidth), 2);
                this.header.put(key, value);
            }
        });


        // 校验有效数据长度
        ProtocolEntry hasSecondaryHeaderEntry = packetDict.get("hasSecondaryHeader");


        return this;
    }

    /**
     * 解析指定位置的数据
     * @param offset    便宜位置（开始位置）
     * @param width     数据长度
     * @param radix     返回值进制
     * @return 解析值
     */
    public byte decodeAt(int offset, int width, int radix) {

        return 0;
    }

//    public void decoderEntry(byte[] frameDict, byte[] frameInfo, byte[] frameData) {
//        this.kissDecoder.appendFrame(frameInfo, frameData);
//        Queue<byte[]> packets = this.kissDecoder.getPackets();
//        while (!packets.isEmpty()) {
//            decodePacket(frameDict, packets.element());
//        }
//    }
//
//    public void decodePacket(byte[] frameDict, byte[] packet) {
//        try {
//            packetDecoderHandler(frameDict, packet);
//        } catch (Exception err) {
//            log.error("packet decode error: {}", err.getMessage());
//        }
//    }

//    private void packetDecoderHandler(byte[] frameDict, byte[] packet) {
//        // Implement the packet decoding logic here
//    }
}
