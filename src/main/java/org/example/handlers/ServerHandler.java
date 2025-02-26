package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.example.models.Topic;
import org.example.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Map<String, Topic> topics = new HashMap<>();
    private static final Map<String, String> loggedInUsers = new HashMap<>();
    private static final Map<String, Vote> votes = new HashMap<>();
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
                case "login" -> login(parts[1]);
                        currentUser=login(parts[1]);
                default -> "Вы не вошли под своим username";
            };
        }
        return switch (command) {
            case "login" -> login(parts[1]);
            case "create" -> create(parts);
            case "view" -> view(parts);
            case "vote" -> vote(parts);
            case "delete" -> delete(parts);
            default -> "Unknown command";
        };
    }

    private String login(String username) {
        loggedInUsers.put(username, username);
        return "Logged in as " + username;
    }

    private String create(String[] parts) {
        if (parts[1].equals("topic") && parts.length>1) {
            String topicName = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
            topics.put(topicName, new Topic(topicName));
            return "Topic created: " + topicName;
        } else if (parts[1].equals("vote") && parts.length>1) {
            String topicName = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
            if (!topics.containsKey(topicName)){
                return "Topic not found: " + topicName;
            }
            // Запрос информации для голосования
            Scanner scanner = new Scanner(System.in);

            // Название голосования
            System.out.print("Введите уникальное имя голосования: ");
            String voteName = scanner.nextLine();

            // Описание голосования
            System.out.print("Введите тему голосования (описание): ");
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
            Vote vote = new Vote(voteName, description, options); // Замените "creator" на реальное имя создателя

            votes.put(voteName, vote); // Если у вас есть структура для хранения голосований

            return "Vote created: " + voteName;
        }
        return "Invalid create command";
    }

    private String view(String[] parts) {
        if (parts.length == 1) {
            return listTopics();
        } else if (parts.length == 3) {
            String topicName = parts[1].split("=")[1];
            return listVotes(topicName);
        }
        return "Invalid view command";
    }

    private String listTopics() {
        StringBuilder sb = new StringBuilder();
        topics.forEach((name, topic) -> sb.append(name).append(" (votes in topic=").append(topic.getVotes().size()).append(")\n"));
        return sb.toString();
    }

    private String listVotes(String topicName) {
        Topic topic = topics.get(topicName);
        if (topic == null) return "Topic not found";
        StringBuilder sb = new StringBuilder();
        topic.getVotes().forEach(vote -> sb.append(vote.getName()).append("\n"));
        return sb.toString();
    }

    private String vote(String[] parts) {
        // Логика голосования
        return "Vote recorded";
    }

    private String delete(String[] parts) {
        // Логика удаления голосования
        return "Vote deleted";
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("EEXEEEXEXEEXEEXE");
        cause.printStackTrace();
        ctx.close();
    }
}
