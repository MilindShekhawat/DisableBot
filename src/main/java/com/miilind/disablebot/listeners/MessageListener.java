package com.miilind.disablebot.listeners;

import reactor.core.scheduler.Schedulers;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

public class MessageListener {
    @Autowired
    private RestTemplate restTemplate;
    private String author = "UNKNOWN";
    public Mono<Void> processMessage(final Message eventMessage){
        return Mono.just(eventMessage)
                .filter(message -> {
                    final Boolean isNotBot = message.getAuthor()
                            .map(user -> !user.isBot())
                            .orElse(false);
                    if (isNotBot) {
                        message.getAuthor().ifPresent(user -> author = user.getUsername());
                    }
                    return isNotBot;
                })
                .flatMap(message -> {
                    return Mono.fromCallable(() -> message.getContent())
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(prompt -> {
                                String apiUrl = "http://localhost:8080/bot/chat?prompt=You are an indian person. You are currently chatting on discord. Reply to this message ->" + prompt;
                                return Mono.fromCallable(() -> restTemplate.getForObject(apiUrl, String.class))
                                        .subscribeOn(Schedulers.boundedElastic());
                            });
                })
                .flatMap(response -> eventMessage.getChannel()
                        .flatMap(channel -> channel.createMessage(response)))
                .then();
    }
}
