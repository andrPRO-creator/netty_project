package org.example.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Scanner;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String response) {
        System.out.println("Server response: " + response);

        // Проверка, начинается ли ответ со слова "Введите"
        if (response.startsWith("Введите параметры создания голосования:")) {
            String userInput = "-t vote params: " + scanner.nextLine(); // Чтение ввода от пользователя
            ctx.writeAndFlush(userInput + "\n"); // Отправка введенного сообщения на сервер
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
