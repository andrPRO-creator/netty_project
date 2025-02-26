package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.example.handlers.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer {
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // Потоки для принятия соединений
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // Потоки для обработки данных

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // Логирование событий сервера
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().
                                    addLast(new LineBasedFrameDecoder(1024)) // Разбивает поток байтов на строки по \n
                                    .addLast(new StringDecoder(CharsetUtil.UTF_8)) // Декодируем ByteBuf -> String
                                          .addLast(new StringEncoder(CharsetUtil.UTF_8)) // Кодируем String -> ByteBuf
                                          .addLast(new LoggingHandler(LogLevel.INFO)) // Логирование данных
                                          .addLast(new ServerHandler()); // Обработчик данных
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("TCP Server started on port {}", port);

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}