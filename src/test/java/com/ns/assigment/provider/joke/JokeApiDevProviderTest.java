package com.ns.assigment.provider.joke;

import com.ns.assigment.entity.JokeResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class JokeApiDevProviderTest {

    @ParameterizedTest(name = "{index} => mockJsonResponse={0}, expectedJoke={1}")
    @MethodSource("differentJokes")
    void testFetchJoke(String mockJsonResponse, JokeResponse expectedJoke) throws IOException, InterruptedException {

        JokeFilter jokeFilter = JokeFilter.builder().explicit(false).sexist(false).build();

        HttpClient mockClient = Mockito.mock(HttpClient.class);

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(mockJsonResponse);

        JokeApiDevProvider jokeService = new JokeApiDevProvider(mockClient);

        JokeResponse result = jokeService.fetchJoke(jokeFilter);

        assertEquals(expectedJoke.getId(), result.getId());
        assertEquals(expectedJoke.getRandomJoke(), result.getRandomJoke());
    }

    private static Stream<Arguments> differentJokes() {
        return Stream.of(
            // Everything is OK
            Arguments.of("""
                {
                "error": false,
                "amount": 1,
                "jokes": [
                    {
                        "category": "Misc",
                        "type": "single",
                        "joke": "Yo mama is so old, she knew Burger King while he was still a prince.",
                        "flags": {
                            "nsfw": false,
                            "religious": false,
                            "political": false,
                            "racist": false,
                            "sexist": false,
                            "explicit": false
                        },
                        "safe": true,
                        "id": 306,
                        "lang": "en"
                    }
                ]}""", new JokeResponse(306, "Yo mama is so old, she knew Burger King while he was still a prince.")),
            // No jokes
            Arguments.of("""
                {
                "error": false,
                "amount": 0,
                "jokes": []
                }""", new JokeResponse(0, JokeApiDevProvider.DEFAULT_JOKE)),
            // sexist = true
            Arguments.of("""
                {
                "error": false,
                "amount": 1,
                "jokes": [
                    {
                        "category": "Misc",
                        "type": "single",
                        "joke": "Yo mama is so old, she knew Burger King while he was still a prince.",
                        "flags": {
                            "nsfw": false,
                            "religious": false,
                            "political": false,
                            "racist": false,
                            "sexist": true,
                            "explicit": false
                        },
                        "safe": true,
                        "id": 306,
                        "lang": "en"
                    }
                ]}""", new JokeResponse(0, JokeApiDevProvider.DEFAULT_JOKE)),
            // explicit = true
            Arguments.of("""
                {
                "error": false,
                "amount": 1,
                "jokes": [
                    {
                        "category": "Misc",
                        "type": "single",
                        "joke": "Yo mama is so old, she knew Burger King while he was still a prince.",
                        "flags": {
                            "nsfw": false,
                            "religious": false,
                            "political": false,
                            "racist": false,
                            "sexist": false,
                            "explicit": true
                        },
                        "safe": true,
                        "id": 306,
                        "lang": "en"
                    }
                ]}""", new JokeResponse(0, JokeApiDevProvider.DEFAULT_JOKE)),
            // error = true
            Arguments.of("""
                {
                "error": true,
                "amount": 1,
                "jokes": [
                    {
                        "category": "Misc",
                        "type": "single",
                        "joke": "Yo mama is so old, she knew Burger King while he was still a prince.",
                        "flags": {
                            "nsfw": false,
                            "religious": false,
                            "political": false,
                            "racist": false,
                            "sexist": false,
                            "explicit": false
                        },
                        "safe": true,
                        "id": 306,
                        "lang": "en"
                    }
                ]}""", new JokeResponse(0, JokeApiDevProvider.DEFAULT_JOKE))
        );
    }

}