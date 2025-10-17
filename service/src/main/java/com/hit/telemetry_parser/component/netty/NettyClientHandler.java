package com.hit.telemetry_parser.component.netty;

import com.hit.telemetry_parser.service.ITelemetryProcessService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// 标记该类实例可以被多个 channel 共享
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ITelemetryProcessService processService;

    /**
     * 接收消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] body = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(body);
        log.info("接收到来自服务端的消息:{}", new String(body));
        processService.parseFrame(new String(body));
        // 释放消息
        ReferenceCountUtil.release(msg);
    }

    /**
     * 和服务器建立连接时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接到服务器!!!");
        // 向服务端发送上线消息
        byte[] bytes = "运管系统已连接!".getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 有异常时触发
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常", cause);
        super.exceptionCaught(ctx, cause);
    }

}
