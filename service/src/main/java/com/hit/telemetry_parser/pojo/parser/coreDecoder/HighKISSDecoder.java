package com.hit.telemetry_parser.pojo.parser.coreDecoder;

public class HighKISSDecoder extends KISSDecoder {

    /**
     * 下一帧数据的帧计数
     */
    private Integer nextFrameCount;

    public HighKISSDecoder() {
        super();
        this.nextFrameCount = null;
    }

    /**
     * Appends a frame with header information and data.
     * Checks frame continuity based on frame_info[5] and processes the frame data.
     *
     * @param frameInfo The frame header information as a byte array.
     * @param frameData The frame data to be decoded.
     */
    public void appendFrame(byte[] frameInfo, byte[] frameData) {
        if (frameInfo.length < 6) {
            // Invalid frame info, reset and return
            resetKiss();
            nextFrameCount = null;
            return;
        }

        byte frameCount = frameInfo[5];
        if (nextFrameCount != null && frameCount == nextFrameCount) {
            // Continuous frame
            nextFrameCount++;
            appendStream(frameData);
        } else {
            // Discontinuous frame or first frame
            resetKiss();
            nextFrameCount = (frameCount & 0xFF) + 1; // Convert to unsigned and increment
            appendStream(frameData);
        }
    }
}
