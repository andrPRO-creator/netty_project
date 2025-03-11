package org.example.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.example.commands.ClientCommands;
import org.example.commands.ServerCommands;
import org.example.models.Topic;
import org.example.models.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    public static final Map<String, Topic> topics = new HashMap<>();
    public static final Map<String, Vote> votes = new HashMap<>();
    public String currentTopic;

    public static final AttributeKey<String> USER_KEY = AttributeKey.newInstance("user");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String command) {
        if (ctx.channel().attr(USER_KEY).get() == null) {
            handleLogin(ctx, command);
        } else {
                handleCommands(ctx, command);
            }
    }

    private void handleLogin(ChannelHandlerContext ctx, String command) {
        if (command.startsWith("login -u=") && command.length() > 9) {
            String username = command.split("=")[1];
            if (UserManager.login(username, ctx.channel())) {
                ctx.channel().attr(USER_KEY).set(username);
                ctx.writeAndFlush("Успешный вход: " + username);
            } else {
                ctx.writeAndFlush("Ошибка: Логин " + username + " уже занят");
            }
        } else {
            ctx.writeAndFlush("Пожалуйста, войдите под своим логином \nКоманда \"login -u=вашЮзернейм\"\n");
        }
    }

    private void handleCommands(ChannelHandlerContext ctx, String command) {
        if (command.startsWith("-t vote params:")){
            createVote(command);
        }
        else if(command.startsWith("save") || command.startsWith("exit") || command.startsWith("load")) {
            String[] parts = command.split(" ");
            String response = processServerCommand(parts);
            ctx.writeAndFlush(response + "\n");
        } else {
            String[] parts = command.split("=");
            String response = processClientCommand(ctx, parts); // Передаем ctx
            ctx.writeAndFlush(response + "\n");
        }
    }

    private String processClientCommand(ChannelHandlerContext ctx, String[] parts) {
        String command = parts[0];

        return switch (command) {
            case "login -u" -> ClientCommands.login();
            case "create topic -n" -> ClientCommands.createTopic(parts);
            case "create vote -t" -> requestCreateVote(ctx, parts); // Передаем ctx
            case "view" -> ClientCommands.view(parts);
            case "vote" -> ClientCommands.vote(parts); // Здесь также нужно передать ctx, если vote требует его
            case "delete" -> ClientCommands.delete(parts);
            default -> "Unknown command";
        };
    }

    private String requestCreateVote(ChannelHandlerContext ctx, String[] parts) {
        String topicName = ClientCommands.findTopic(parts);
        if (topicName!=null) {
            currentTopic=topicName;
            ctx.writeAndFlush("Введите параметры создания голосования:");
            return "Input params";
        }
        return "Topic not found";
    }

    private void createVote(String input){
        // Удаляем префикс и разбиваем по символу ';'
        String[] params = input.replace("-t vote params: ", "").trim().split(";");
        if (params.length < 3) {
            System.out.println("Ошибка при введении параметров. Ожидается минимум 3 параметра.");
            return; // Выход из метода, если параметры некорректные
        }

        List<String> options = new ArrayList<>();
        for (int i = 2; i < params.length; i++) {
            options.add(params[i].trim()); // Удаление лишних пробелов
        }

        ClientCommands.createVote(currentTopic, params[0].trim(), params[1].trim(), options);
        System.out.println("Голосование успешно создано!");
    }



    private String processServerCommand(String[] parts) {
        String command = parts[0];

        return switch (command) {
            case "save" -> ServerCommands.saveDataToFile(parts[1]);
            case "load" -> ClientCommands.createTopic(parts);
            case "exit" -> null;
            default -> "Unknown command";
        };
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}