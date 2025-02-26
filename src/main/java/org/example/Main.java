package org.example;

import org.example.client.NettyClient;
import org.example.server.TcpServer;

public class Main {
    public static void main(String[] args) throws Exception {
// Запускаем сервер в отдельном потоке
        new Thread(() -> {
            try {
                new TcpServer(8080).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Даем серверу время на запуск (1-2 секунды)
        Thread.sleep(2000);

        // Запускаем клиент
        new NettyClient("localhost", 8080).run();
    }
}