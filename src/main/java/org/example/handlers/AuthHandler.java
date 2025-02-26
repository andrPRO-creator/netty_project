//package org.example.handlers;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.util.AttributeKey;
//
//public class AuthHandler extends ChannelInboundHandlerAdapter {
//    // Ключ для хранения сессии пользователя
//    public static final AttributeKey<UserSession> USER_SESSION_KEY =
//            AttributeKey.newInstance("userSession");
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if (!isAuthenticated(ctx) && !isLoginCommand(msg)) {
//            ctx.writeAndFlush("ERROR: Please login first\n");
//            return;
//        }
//
//        // Передать сообщение следующему обработчику
//        super.channelRead(ctx, msg);
//    }
//
//    private boolean isAuthenticated(ChannelHandlerContext ctx) {
//        return ctx.channel().attr(USER_SESSION_KEY).get() != null;
//    }
//
//    private boolean isLoginCommand(Object msg) {
//        return msg.toString().startsWith("login");
//    }
//}