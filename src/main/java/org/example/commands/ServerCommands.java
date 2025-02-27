package org.example.commands;

import org.example.handlers.ServerHandler;
import org.example.models.Topic;
import org.example.models.Vote;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ServerCommands {
    public static String saveDataToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Topic topic : ServerHandler.topics.values()) {
                writer.write("Раздел: " + topic.getName());
                writer.newLine();
                for (Vote vote : topic.getVotes()) {
                    writer.write("  Голосование: " + vote.getName());
                    writer.newLine();
                    writer.write("    Описание: " + vote.getDescription());
                    writer.newLine();
                    writer.write("    Варианты ответа: " + String.join(", ", vote.getOptions()));
                    writer.newLine();
                    writer.write("    Результаты: " + vote.getResults().toString());
                    writer.newLine();
                }
                writer.newLine(); // Добавляем пустую строку между разделами
            }
            return "Данные успешно сохранены в " + filename;
        } catch (IOException e) {
            return "Ошибка при сохранении данных: " + e.getMessage();
        }
    }

}
