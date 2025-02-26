package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.example.commands.ClientCommands;
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
    private String currentUser;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String command) {
        System.out.println("Received command: " + command);
        String[] parts = command.split(" ");
        String response = processCommand(parts);
        ctx.writeAndFlush(response + "\n");
    }

    private String processCommand(String[] parts) {
        String command = parts[0];
        if (currentUser==null){
            return switch (command) {
                case "login" -> ClientCommands.login(parts[1]);
                default -> "Вы не вошли под своим username";
            };
        }
        return switch (command) {
            case "login" -> ClientCommands.login(parts[1]);
            case "create" -> ClientCommands.create(parts);
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
