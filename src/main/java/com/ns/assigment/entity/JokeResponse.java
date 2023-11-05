package com.ns.assigment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JokeResponse {
    private int id;
    private String randomJoke;

    @Override
    public String toString() {
        return "Joke{" +
            "id=" + id +
            ", randomJoke='" + randomJoke + '\'' +
            '}';
    }
}
