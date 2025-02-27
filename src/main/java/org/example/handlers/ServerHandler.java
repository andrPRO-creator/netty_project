package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.example.commands.ClientCommands;
import org.example.commands.ServerCommands;
import org.example.exceptions.InvalidCreateCommandException;
import org.example.exceptions.TopicNotFoundException;
import org.example.models.Topic;
import org.example.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.nio.file.Files.delete;


public class ServerHandler extends SimpleChannelInboundHandler<String> {

    public static final Map<String, Topic> topics = new HashMap<>();
    public static final Map<String, String> loggedInUsers = new HashMap<>();
    public static final Map<String, Vote> votes = new HashMap<>();

    public static final AttributeKey<String> USER_KEY =
            AttributeKey.newInstance("user");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String command) {
        if (ctx.channel().attr(USER_KEY).get()==null){

            if (command.startsWith("login -u=") && command.length()>9 ) {
                String username = command.split("=")[1];

                if (UserManager.login(username, ctx.channel())) {
                    ctx.channel().attr(USER_KEY).set(username);
                    ctx.writeAndFlush("Успешный вход: " + username);
                } else {
                    ctx.writeAndFlush("Ошибка: Логин " + username + " уже занят");
                }
            } else {ctx.writeAndFlush("Пожалуйста, войдите под своим логином \nКоманда \"login -u=вашЮзернейм\"\n");}
        } else {
            System.out.println("Received command: " + command);
            String[] parts = command.split("=");
            String response = processCommand(parts);
            ctx.writeAndFlush(response + "\n");
        }
    }

    private String processCommand(String[] parts) throws InvalidCreateCommandException,
            TopicNotFoundException {
        String command = parts[0];

        return switch (command) {
            case "login -u" -> ClientCommands.login();
            case "create topic -n" -> ClientCommands.createTopic(parts);
            case "create vote -t" -> ClientCommands.createVote(parts);
            case "view" -> ClientCommands.view(parts);
            case "vote" -> ClientCommands.vote(parts);
            case "delete" -> ClientCommands.delete(parts);
            default -> "Unknown command";
        };
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("EEXEEEXEXEEXEEXE");
        cause.printStackTrace();
        ctx.close();
    }
}
