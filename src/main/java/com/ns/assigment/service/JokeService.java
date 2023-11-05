package com.ns.assigment.service;

import com.ns.assigment.provider.joke.JokeFilter;
import com.ns.assigment.entity.JokeResponse;
import com.ns.assigment.provider.JokeProvider;
import com.ns.assigment.provider.joke.JokeProviderFactory;
import org.springframework.stereotype.Service;

@Service
public class JokeService {

    public JokeResponse getJoke() {

        JokeFilter jokeFilter = JokeFilter.builder()
            .explicit(false)
            .sexist(false)
            .build();

        JokeProvider jokeProvider = JokeProviderFactory.createJokeProvider();

        return jokeProvider.fetchJoke(jokeFilter);
    }
}
