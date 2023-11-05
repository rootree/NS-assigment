package com.ns.assigment.provider.joke.jokeapidev;

import lombok.Data;

@Data
public class Joke {
    private String category;
    private String type;
    private String joke;
    private JokeFlags flags;
    private boolean safe;
    private int id;
    private String lang;
}
