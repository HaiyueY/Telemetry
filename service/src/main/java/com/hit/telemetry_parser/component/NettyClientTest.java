package com.hit.telemetry_parser.component;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class NettyClientTest {
    public static void main(String[] args) throws InterruptedException {
        // 客户端的线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            // 创建Netty客户端端的启动对象
            Bootstrap bootstrap = new Bootstrap();
            // 使用链式编程来配置参数
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //对workerGroup的SocketChannel设置处理器
                            ChannelPipeline pipeline = ch.pipeline();
                            // 对于通道加入解码器
                            pipeline.addLast("decoder", new StringDecoder());

                            // 对于通道加入加码器
                            pipeline.addLast("encoder", new StringDecoder());

                            // 加入事件回调处理器
//                            pipeline.addLast(new ServerListenerHandler());
                        }
                    });
            System.out.println("基于Netty的客户端接入启动完成....");
            ChannelFuture cf = bootstrap.connect("192.168.43.251", 3070).sync();
            // 获取连接通道
            Channel channel = cf.channel();
            System.out.println("+++++++" + channel.localAddress() + "=======");
            // 客户端输入扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String next = scanner.next();
                // 发送到服务端
                channel.writeAndFlush(Unpooled.buffer().writeBytes(next.getBytes(CharsetUtil.UTF_8)));
            }

            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成，内部调用的是Object的wait()方法
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
