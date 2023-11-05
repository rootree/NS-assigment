package com.ns.assigment.controller;

import com.ns.assigment.entity.JokeResponse;
import com.ns.assigment.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @GetMapping("/joke")
    public Mono<JokeResponse> getUser() {
        return Mono.just(jokeService.getJoke()); // Return a Mono with a JokeResponse object
    }

}
