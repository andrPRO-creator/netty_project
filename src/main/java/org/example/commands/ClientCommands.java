package org.example.commands;

import org.example.exceptions.TopicNotFoundException;
import org.example.handlers.ServerHandler;
import org.example.models.Topic;
import org.example.models.Vote;

import java.util.*;

public class ClientCommands {
    public static String login(String username) {
        ServerHandler.loggedInUsers.put(username, username);
        return "Logged in as " + username;
    }

    public static String createTopic(String[] parts) {
        if (parts[0].equals("create topic -n") && parts.length>1) {
            String topicName = String.join("=", Arrays.copyOfRange(parts, 1, parts.length));
            ServerHandler.topics.put(topicName, new Topic(topicName));
            return "Topic created: " + topicName;
        }
        return "Invalid create topic command";
    }

    public static String createVote(String[] parts){
        if (parts[0].equals("create vote -t") && parts.length>1) {
            String topicName = String.join("=", Arrays.copyOfRange(parts, 1, parts.length));
            if (!ServerHandler.topics.containsKey(topicName)){
                throw new TopicNotFoundException(String.format("Topic %S not found.", topicName));
            }
            // Запрос информации для голосования
            Scanner scanner = new Scanner(System.in);

            // Название голосования
            System.out.print("Введите название голосования: ");
            String voteName = scanner.nextLine();

            // Описание голосования
            System.out.print("Введите описание голосования: ");
            String description = scanner.nextLine();

            // Количество вариантов ответа
            System.out.print("Введите количество вариантов ответа: ");
            int numberOfOptions = Integer.parseInt(scanner.nextLine());

            // Варианты ответа
            List<String> options = new ArrayList<>();
            for (int i = 0; i < numberOfOptions; i++) {
                System.out.print("Введите вариант ответа " + (i + 1) + ": ");
                String option = scanner.nextLine();
                options.add(option);
            }

            // Создаем новое голосование
            Vote vote = new Vote(voteName, description, options);

            ServerHandler.votes.put(voteName, vote); // Если у вас есть структура для хранения голосований

            return "Vote created: " + voteName;
        }
        return "Invalid create vote command";
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
