package com.ns.assigment.provider.joke;

import com.ns.assigment.provider.JokeProvider;

import java.net.http.HttpClient;

public class JokeProviderFactory {

    public static JokeProvider createJokeProvider() {
        // For now, return the JokeApiDevProvider (the only provider)
        return new JokeApiDevProvider(HttpClient.newHttpClient());
    }
}
