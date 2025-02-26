package org.example.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Topic {
    private final String name;
    private final Map<String, Vote> votes = new HashMap<>();

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addVote(Vote vote) {
        votes.put(vote.getName(), vote);
    }

    public Vote getVote(String name) {
        return votes.get(name);
    }

    public Collection<Vote> getVotes() {
        return votes.values();
    }

    public void removeVote(String name) {
        votes.remove(name);
    }
}
