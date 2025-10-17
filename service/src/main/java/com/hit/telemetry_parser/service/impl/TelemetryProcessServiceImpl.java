package com.hit.telemetry_parser.service.impl;

import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hit.telemetry_parser.common.telemetry.*;
import com.hit.telemetry_parser.component.WebSocketServer;
import com.hit.telemetry_parser.component.netty.NettyClient;
import com.hit.telemetry_parser.domain.bo.ProtocolBo;
import com.hit.telemetry_parser.domain.bo.ProtocolTemplateBo;
import com.hit.telemetry_parser.entity.PacketEntity;
import com.hit.telemetry_parser.service.IPacketService;
import com.hit.telemetry_parser.service.IProtocolService;
import com.hit.telemetry_parser.service.ITelemetryProcessService;
import com.hit.telemetry_parser.utils.TimeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 卫星技术研究所_杨海岳
 * @since 2025-05-23
 */
@Slf4j
@Service
public class TelemetryProcessServiceImpl implements ITelemetryProcessService {

    @Resource
    private NettyClient nettyClient;

    /**
     * 协议服务类
     */
    @Resource
    private IProtocolService protocolService;

    /**
     * 遥测解析数据服务类
     */
    @Resource
    private IPacketService packetService;


    @Resource
    private WebSocketServer webSocketServer;

    @Async("asyncServiceExecutor")
    @Override
    public void parseFrame(String frameStr) {
        // 解析JSON
        JSONObject jsonFrame = JSONUtil.parseObj(frameStr);
        Long receiveTime = (Long) jsonFrame.get("proxy_receive_time");
        byte[] frame = process(frameStr);
//        log.info("开始遥测解析：{}", frameStr);
        if (frame.length != 1024) {
            //TODO: 测试环境
            return;
        }
//        log.info("完成初始解析：{}", frame);
        parse(frame, 1929465652152266753L, TTCType.TELEMETRY,
                TimeUtils.getDateFromMillis(receiveTime, TimeScalesFactory.getUTC()));
    }

    @Async("asyncServiceExecutor")
    @Override
    public void callClient() {
        try {
            nettyClient.connect();
        } catch (InterruptedException e) {
            log.error("客户端连接失败");
            throw new RuntimeException(e);
        }
    }

    @Async("asyncServiceExecutor")
    @Override
    public void destroyClient() {
        try {
            nettyClient.close();
        } catch (InterruptedException e) {
            log.error("客户端关闭失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] process(String frameStr) {
        // 解析JSON
        JSONObject jsonFrame = JSONUtil.parseObj(frameStr);
        // 获取raw data
        String rawData = (String) jsonFrame.get("raw_data");
        // 格式为b'xxxxxx'
        String[] split = rawData.split("'");
        return HexUtil.decodeHex(split[1]);
//        return BinaryUtils.parseRawData((String) rawData);
    }

    @Override
    public void parse(byte[] frame, Long protocolId, TTCType type, AbsoluteDate date) {
        // 获取遥测包模板及参数字典
        ProtocolTemplateBo templateBo = protocolService.queryTemplateByProtocolId(protocolId);
        // 获取遥测包协议及参数字典
        ProtocolBo protocolBo = protocolService.queryBoById(protocolId);

        // 获取包头
        byte[] header = Arrays.copyOfRange(frame, templateBo.getHeaderOffset(), templateBo.getHeaderLength() + templateBo.getHeaderOffset());

        templateBo.getParams().forEach((protocolParam) -> {
            // 检查包头
            if (Objects.equals(protocolParam.getName(), "源nID(主类别)")) {
                byte[] nidBytes = Arrays.copyOfRange(header, protocolParam.getOffsetByte(),
                        protocolParam.getOffsetByte() + protocolParam.getLengthByte());
                String nid = byteArray2HexWith0x(nidBytes);
                log.info(nid);
                if (!nid.equals("0xf0")) {
                    // 结束
                } else {
                    // 获取数据区
                    byte[] body = Arrays.copyOfRange(frame, templateBo.getBodyOffset(), templateBo.getBodyLength() + templateBo.getBodyOffset());

                    // 获取校验区
                    byte[] valid = Arrays.copyOfRange(frame, templateBo.getValidOffset(), templateBo.getValidOffset() + templateBo.getValidOffset());

                    // 数据包校验
                    // TODO: 查询校验模式
                    boolean validate = this.validate(frame, byteArray2String(valid),
                            ValidateType.validateType(templateBo.getValidType()));

                    if (validate) {
                        List<PacketEntity> list = new ArrayList<>();
                        protocolBo.getParams().forEach((param) -> {
                            PacketEntity packet = new PacketEntity();
                            byte[] originalBytes;
                            if (param.getLengthByte() == 0) {
                                // 长度不足1Byte
                                originalBytes = Arrays.copyOfRange(body, param.getOffsetByte(), param.getOffsetByte() + 1);
                            } else {
                                // 长度超过1Byte
                                //TODO: 考虑超过nByte但不足n+1Byte
                                originalBytes = Arrays.copyOfRange(body, param.getOffsetByte(), param.getLengthByte() + param.getOffsetByte());
                            }
                            packet.setOrigin(byteArray2HexWith0x(originalBytes));
                            packet.setUpdateTime(LocalDateTime.ofInstant(date.toDate(TimeScalesFactory.getUTC()).toInstant(), ZoneId.of("UTC")));

                            switch (ParamType.paramType(param.getDataType())) {
                                case AN, DS_BUS, TH -> {
                                    switch (BaseType.baseType(param.getBase())) {
                                        case BIN -> {
                                            String s = byteArray2Bin(originalBytes);
                                            packet.setValue(s.substring(param.getOffsetBit(), param.getLengthBit() + param.getOffsetBit()));
                                        }
                                        case HEX -> packet.setValue(byteArray2HexWith0x(originalBytes));
                                        case DEC -> packet.setValue(byteArray2Dec(originalBytes));
                                    }
                                }
                                case BL -> {
                                    String s = byteArray2Bin(originalBytes);
                                    packet.setValue(s.substring(param.getOffsetBit(), param.getLengthBit() + param.getOffsetBit()));
                                    packet.setLabel(Integer.parseInt(s, BaseType.BIN.getValue()) != 0
                                            ? THType.ON.getLabel()
                                            : THType.OFF.getLabel());
                                }
                            }
                            if (packet.getLabel() == null) {
                                packet.setLabel(packet.getValue());
                            }
                            list.add(packet
                                    .setName(param.getName())
                                    .setParamId(param.getId())
                                    .setStatus(TelemetryStatusType.NORMAL.getName()));
                        });

                        // TODO：消息推送
                        webSocketServer.sendTelemetryMessage(list, 1929465652152266753L);
                        // 存入
                        packetService.saveBatch(list);
                    } else {
                        // 存在误码，校验不通过
                        log.warn("{} 数据校验未通过,该帧 {} 存在误码/丢包", type.getLabel(), byteArray2String(frame));
                        return;
                    }
                }
            }
        });

    }

    @Override
    public boolean validate(byte[] frame, String valid, ValidateType type) {
        switch (type) {
            case SC -> {
                // TODO: 和校验
                log.info("和校验通过");
                return true;
            }
            case CRC -> {
                log.error("CRC校验错误");
                return false;
            }
            default -> {
                log.error("请检查校验类型");
                return false;
            }
        }
    }

    /**
     * @param bytes 字节数组(hex)
     * @return 字符串(binary)
     */
    private static String byteArray2String(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
//            stringBuilder.append(Integer.toBinaryString(b));
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return stringBuilder.toString();
    }

    public static String byteArray2HexWith0x(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        byte[] bytes = HexUtil.decodeHex("1acffc1d");
        String s = byteArray2HexWith0x(bytes);
        System.out.println(s);
    }

    public static String byteArray2Bin(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);
        String binaryOverall = bigInt.toString(2);
        return String.format("%" + (bytes.length * 8) + "s", binaryOverall).replace(' ', '0');
    }

    public static String byteArray2Dec(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.position(8 - bytes.length);
        buffer.put(bytes);
        buffer.position(0);
        long decimalOverall = buffer.getLong();
        return Long.toString(decimalOverall);
    }


}
