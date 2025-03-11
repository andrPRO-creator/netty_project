package org.example.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vote {
    private final String name;
    private final String description;
    private final List<String> options;
    private final Map<String, Integer> results = new HashMap<>();

    public Vote(String name, String description, List<String> options) {
        this.name = name;
        this.description = description;
        this.options = options;
        options.forEach(option -> results.put(option, 0));
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getOptions() {
        return options;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public void vote(String name, String description, String options){};

    public void vote(String option) {
        results.put(option, results.getOrDefault(option, 0) + 1);
    }
}
