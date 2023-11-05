package com.ns.assigment.provider.joke;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.assigment.entity.JokeResponse;
import com.ns.assigment.provider.JokeProvider;
import com.ns.assigment.provider.joke.jokeapidev.Joke;
import com.ns.assigment.provider.joke.jokeapidev.JokeApiDevResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JokeApiDevProvider implements JokeProvider {

    private static final Logger LOG = LoggerFactory
        .getLogger(JokeApiDevProvider.class);

    // But also can be as part of the configuration, e.g.: ${joke-provider.joke-api-dev.url}
    private final String API_URL = "https://v2.jokeapi.dev/joke/Any?type=single&amount=16";

    public static final String DEFAULT_JOKE = "No jokes today, sorry :("; // Can be also part of the config

    HttpClient client;

    HttpRequest request;

    public JokeApiDevProvider(HttpClient client) {
        this.client = client;
        request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .build();
    }

    public JokeResponse fetchJoke(JokeFilter jokeFilter) {

        JokeResponse jokeToReturn = new JokeResponse(0, DEFAULT_JOKE);

        try {
            String responseBody = sendHttpRequest();
            if (responseBody == null) {
                return jokeToReturn;
            }

            JokeApiDevResponse jokeApiDevResponse = parseJokeResponse(responseBody);
            if (jokeApiDevResponse == null || jokeApiDevResponse.isError()) {
                return jokeToReturn;
            }

            List<Joke> safeJokes = filterSafeJokes(jokeApiDevResponse, jokeFilter);
            if (safeJokes.isEmpty()) {
                return jokeToReturn;
            }

            Joke shortestJoke = findShortestJoke(safeJokes);
            if (shortestJoke == null) {
                return jokeToReturn;
            }

            if (!shortestJoke.isSafe()) {
                makeJokeSave(shortestJoke);
            }
            
            jokeToReturn = new JokeResponse(shortestJoke.getId(), shortestJoke.getJoke());

        } catch (IOException | InterruptedException e) {
            LOG.warn("Error while fetching a joke: " + e.getMessage());
        }

        return jokeToReturn;
    }

    private String sendHttpRequest() throws IOException, InterruptedException {

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        }

        return response.body();
    }

    private JokeApiDevResponse parseJokeResponse(String responseBody) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(responseBody, JokeApiDevResponse.class);
    }

    private List<Joke> filterSafeJokes(JokeApiDevResponse jokeApiDevResponse, JokeFilter jokeFilter) {
        return jokeApiDevResponse.getJokes().stream()
            .filter(joke -> joke.getFlags().isExplicit() == jokeFilter.isExplicit())
            .filter(joke -> joke.getFlags().isSexist() == jokeFilter.isSexist())
            .collect(Collectors.toList());
    }

    private Joke findShortestJoke(List<Joke> safeJokes) {
        return safeJokes.stream()
            .min(Comparator.comparingInt(joke -> joke.getJoke().length()))
            .orElse(null);
    }

    private void makeJokeSave(Joke unSafeJoke) {

        String joke = unSafeJoke.getJoke();

        // joke might have " or \n, or might be something else
        // here supposed to be some filter or modifications, but it's better know a bit more about it
        // and maybe a client should handle escaping

        unSafeJoke.setJoke(joke);
    }
}
