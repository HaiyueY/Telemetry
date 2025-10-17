package com.hit.telemetry_parser.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
// 标记该类实例可以被多个 channel 共享
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 存储所有活跃的客户端 Channel
    private static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端连接时，将 Channel 添加到 clients 集合
        clients.add(ctx.channel());
        log.info("客户端连接：{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 客户端断开时，从 clients 集合移除 Channel
        clients.remove(ctx.channel());
        log.info("客户端断开：{}", ctx.channel().remoteAddress());
    }
    /**
     * 每个传入的消息都会调用该方法
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] body = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(body);
        log.info("服务端接收到数据:{}", new String(body));
//        byte[] responseBytes = "hi,客户端,我是服务端".getBytes();
//        ctx.writeAndFlush(Unpooled.wrappedBuffer(responseBytes));
    }

    /**
     * 在读取期间，有异常抛出时会调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常栈跟踪
        log.error("netty server error",cause);
        //关闭该channel
        ctx.close();
    }

    /**
     * 向所有客户端广播消息
     * @param message 要发送的消息
     */
    public void broadcast(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        clients.writeAndFlush(byteBuf);
        log.info("广播消息：{}", message);
    }
}
