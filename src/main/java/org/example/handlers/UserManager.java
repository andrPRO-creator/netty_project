package org.example.handlers;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private static final Map<String, Channel> onlineUsers = new ConcurrentHashMap<>();

    public static boolean login(String username, Channel channel) {
        if (onlineUsers.containsKey(username)) {
            return false; // Логин уже занят
        }
        onlineUsers.put(username, channel);
        return true;
    }

    public static void logout(String username) {
        onlineUsers.remove(username);
    }
}
