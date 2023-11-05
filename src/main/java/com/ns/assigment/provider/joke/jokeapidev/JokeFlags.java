package com.ns.assigment.provider.joke.jokeapidev;

import lombok.Data;

@Data
public class JokeFlags {
    private boolean nsfw;
    private boolean religious;
    private boolean political;
    private boolean racist;
    private boolean sexist;
    private boolean explicit;
}
