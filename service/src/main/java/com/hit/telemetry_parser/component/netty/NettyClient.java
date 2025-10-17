package com.hit.telemetry_parser.component.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class NettyClient {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    private EventLoopGroup mainGroup = new NioEventLoopGroup();

    private Bootstrap bootstrap = new Bootstrap();

    @Getter
    private Channel channel;

    /**
     * 创建连接
     */
    public void connect() throws InterruptedException {
        this.mainGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(mainGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 从 Spring 容器获取 NettyClientHandler
                        NettyClientHandler handler = applicationContext.getBean(NettyClientHandler.class);
                        socketChannel.pipeline()
                                .addLast(new StringEncoder())
                                .addLast(new ByteArrayEncoder())
                                .addLast(handler);
                    }
                });
        ChannelFuture future = bootstrap.connect().sync();
        this.channel = future.channel();
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        log.info("客户端待发送消息:{}", message);
        Channel channel = this.getChannel();
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        channel.writeAndFlush(byteBuf);
    }

    public void close() throws InterruptedException {
        log.info("关闭客户端");
        mainGroup.shutdownGracefully().sync();
    }

}
