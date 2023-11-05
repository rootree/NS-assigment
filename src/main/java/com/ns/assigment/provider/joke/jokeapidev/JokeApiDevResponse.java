package com.ns.assigment.provider.joke.jokeapidev;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class JokeApiDevResponse {

    @JsonProperty("error")
    private boolean error;

    @JsonProperty("jokes")
    private List<Joke> jokes;

}
