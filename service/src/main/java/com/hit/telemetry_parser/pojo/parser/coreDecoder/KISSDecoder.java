package com.hit.telemetry_parser.pojo.parser.coreDecoder;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * KISS解码器
 * <p>
 *     用于KISS协议数据包的解码功能，针对如下KISS协议：
 *     <p>数据包以C0开头 ，以C0结尾</p>
 *     <p>当数据中有C0时， 以 DB DC替换</p>
 *     <p>当数据中有DB时， 以 DB DD替换</p>
 * </p>
 * @author artisticzhao (original python)
 * @author Haiyue Yang (java translation)
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2025-04-30
 */
public class KISSDecoder {
    // KISS protocol constants
    protected static final byte KISS_FEND = (byte) 0xC0;  // 192
    protected static final byte KISS_FESC = (byte) 0xDB;  // 219
    protected static final byte KISS_TFEND = (byte) 0xDC; // 220
    protected static final byte KISS_TFESC = (byte) 0xDD; // 221

    protected boolean inEscMode;
    protected ByteArrayOutputStream dataBuf;

    /**
     * 解码长度
     */
    protected int decodedLen;

    /**
     * 存储解码出的packet
     */
    @Getter
    protected Queue<byte[]> packets;

    public KISSDecoder() {
        this.inEscMode = false;
        this.dataBuf = new ByteArrayOutputStream();
        this.decodedLen = 0;
        this.packets = new ConcurrentLinkedQueue<>();
    }

    /**
     * Appends stream data to the decoder for KISS protocol decoding.
     * When a complete frame is decoded, it is added to the packets queue.
     *
     * @param streamData The KISS-encoded data as a byte array.
     */
    public void appendStream(byte[] streamData) {
        for (byte b : streamData) {
            if (!inEscMode) {
                if (b == KISS_FEND) {
                    if (decodedLen != 0) {
                        packets.offer(dataBuf.toByteArray()); // Decoded a packet
                        resetKiss();
                    }
                } else if (b == KISS_FESC) {
                    inEscMode = true;
                } else {
                    dataBuf.write(b);
                    decodedLen++;
                }
            } else {
                if (b == KISS_TFEND) {
                    dataBuf.write(KISS_FEND);
                } else if (b == KISS_TFESC) {
                    dataBuf.write(KISS_FESC);
                }
                decodedLen++;
                inEscMode = false;
            }
        }
    }

    /**
     * Resets the decoder state.
     */
    protected void resetKiss() {
        dataBuf = new ByteArrayOutputStream();
        decodedLen = 0;
    }

}

