package com.ns.assigment.provider;

import com.ns.assigment.provider.joke.JokeFilter;
import com.ns.assigment.entity.JokeResponse;

public interface JokeProvider {

    JokeResponse fetchJoke(JokeFilter jokeFilter);
}
