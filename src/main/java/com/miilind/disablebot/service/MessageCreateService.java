package com.miilind.disablebot.service;

import com.miilind.disablebot.listeners.EventListener;
import com.miilind.disablebot.listeners.MessageListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateService extends MessageListener implements EventListener<MessageCreateEvent> {
    @Override
    public  Class<MessageCreateEvent> getEventType(){
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(final MessageCreateEvent event) {
        return processMessage(event.getMessage());
    }
}
