package com.hit.telemetry_parser.component;

import com.alibaba.fastjson.JSONObject;
import com.hit.telemetry_parser.common.Result;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc: WebSocket编码器
 * @author: LiuCh
 * @since: 2021/8/18
 */
public class ServerEncoder implements Encoder.Text<Result<Object>> {


    private static final Logger log = LoggerFactory.getLogger(ServerEncoder.class);

    /**
     * 这里的参数 要和  Encoder.Text<T>保持一致
     * @param result
     * @return
     * @throws EncodeException
     */
    @Override
    public String encode(Result result) throws EncodeException {
        /*
         * 这里是重点，只需要返回Object序列化后的json字符串就行
         * 你也可以使用gosn，fastJson来序列化。
         * 这里我使用fastjson
         */
       try {
           return JSONObject.toJSONString(result);
       }catch (Exception e){
           log.error("",e);
       }
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        //可忽略
    }

    @Override
    public void destroy() {
        //可忽略
    }
}

