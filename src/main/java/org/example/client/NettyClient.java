package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.handlers.ClientHandler;

import java.util.Scanner;

public class NettyClient {
    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            Channel channel = bootstrap.connect(host, port).sync().channel();
            System.out.println("Connected to server at " + host + ":" + port);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter command: ");
                String command = scanner.nextLine();
                channel.writeAndFlush(command + "\n");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}