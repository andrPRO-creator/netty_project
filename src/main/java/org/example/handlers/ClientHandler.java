package org.example.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.example.commands.ClientCommands;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String response) {
        System.out.println("Server response: " + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    private static void createVote(String topic) {
//        // Запрос информации для голосования
//        Scanner scanner = new Scanner(System.in);
//
//        // Название голосования
//        String voteName = queryForInput("Введите название голосования: ", scanner);
//        // Описание голосования
//        String description = queryForInput("Введите описание голосования: ", scanner);
//
//        // Количество вариантов ответа
//        int numberOfOptions = Integer.parseInt(queryForInput("Введите количество вариантов ответа: ", scanner));
//
//        // Варианты ответа
//        List<String> options = IntStream.range(0, numberOfOptions)
//                .mapToObj(i -> queryForInput("Введите вариант ответа %d: ", i + 1, scanner))
//                .collect(Collectors.toList());
//    }
}
