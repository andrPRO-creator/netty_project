package org.example.commands;


import org.example.handlers.ServerHandler;
import org.example.models.Topic;
import org.example.models.Vote;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientCommands {
    private static final String TOPIC_COMMAND = "topic -n";
    private static final String VOTE_COMMAND = "vote -t";
    private static final String CREATE_COMMAND = "create";
    private static final String INVALID_CREATE_COMMAND = "Invalid create command";

    public static String login() {
        //ServerHandler.loggedInUsers.put(username, username);
        return "Вы уже авторизованы на сервере!";
    }

    private static String parseStringCommand(String[] parts, String command) {
        if (parts[0].equals(command) && parts.length>1) {
            return String.join("=", Arrays.copyOfRange(parts, 1, parts.length));
        } else {
            return null;
        }
    }

    private static String queryForInput(String message, Scanner scanner) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private static String queryForInput(String message, Object args, Scanner scanner) {
        System.out.println(String.format(message, args));
        return scanner.nextLine();
    }

    public static String createTopic(String[] parts) {
        String topicName = parseStringCommand(parts, CREATE_COMMAND + " " + TOPIC_COMMAND);
        if (topicName!=null){
        ServerHandler.topics.put(topicName, new Topic(topicName));
        return "Topic is created";}else {
            return "Ошибка! Введено нулевое значение параметра";
        }
    }

    public static String createVote(String[] parts){
            String topicName = parseStringCommand(parts, CREATE_COMMAND + " " + VOTE_COMMAND);

            if (!ServerHandler.topics.containsKey(topicName)){
                return (String.format("Topic %S not found.", topicName));
            }
            // Запрос информации для голосования
            Scanner scanner = new Scanner(System.in);

            // Название голосования
            String voteName = queryForInput("Введите название голосования: ", scanner);
            // Описание голосования
            String description = queryForInput("Введите описание голосования: ", scanner);

            // Количество вариантов ответа
            int numberOfOptions = Integer.parseInt(queryForInput("Введите количество вариантов ответа: ", scanner));

            // Варианты ответа
            List<String> options = IntStream.range(0, numberOfOptions)
                .mapToObj(i -> queryForInput("Введите вариант ответа %d: ", i + 1, scanner))
                .collect(Collectors.toList());


            // Создаем новое голосование
            Vote vote = new Vote(voteName, description, options);

            ServerHandler.votes.put(voteName, vote); // Если у вас есть структура для хранения голосований

            return "Vote created: " + voteName;
    }


    public static String view(String[] parts) {
        if (parts.length == 1) {
            return listTopics();
        } else if (parts.length == 3) {
            String topicName = parts[1].split("=")[1];
            return listVotes(topicName);
        }
        return "Invalid view command";
    }

    public static String listTopics() {
        StringBuilder sb = new StringBuilder();
        ServerHandler.topics.forEach((name, topic) -> sb.append(name).append(" (votes in topic=").append(topic.getVotes().size()).append(")\n"));
        return sb.toString();
    }

    public static String listVotes(String topicName) {
        Topic topic = ServerHandler.topics.get(topicName);
        if (topic == null) return "Topic not found";
        StringBuilder sb = new StringBuilder();
        topic.getVotes().forEach(vote -> sb.append(vote.getName()).append("\n"));
        return sb.toString();
    }

    public static String vote(String[] parts) {
        // Логика голосования
        return "Vote recorded";
    }

    public static String delete(String[] parts) {
        // Логика удаления голосования
        return "Vote deleted";
    }

}
