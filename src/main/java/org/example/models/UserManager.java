//package org.example.models;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//public class UserManager {
//    private static final Map<String, UserSession> loggedUsers =
//            new ConcurrentHashMap<>();
//
//    public static boolean isUserLoggedIn(String username) {
//        return loggedUsers.containsKey(username);
//    }
//
//    public static void addUser(UserSession session) {
//        loggedUsers.put(session.getUsername(), session);
//    }
//
//    public static void removeUser(UserSession session) {
//        loggedUsers.remove(session.getUsername());
//    }
//}
