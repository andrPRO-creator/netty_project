//package org.example.handlers;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//
//public class LoginHandler extends SimpleChannelInboundHandler<String> {
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//        if (msg.startsWith("login -u=")) {
//            String username = msg.split("=")[1].trim();
//
//            // Проверка уникальности логина
//            if (UserManager.isUserLoggedIn(username)) {
//                ctx.writeAndFlush("ERROR: User already logged in\n");
//                return;
//            }
//
//            // Создание сессии
//            UserSession session = new UserSession(username);
//            ctx.channel().attr(AuthHandler.USER_SESSION_KEY).set(session);
//            UserManager.addUser(session);
//
//            ctx.writeAndFlush("OK: Logged in as " + username + "\n");
//        }
//    }
//}
