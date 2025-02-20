package com.chess.jnd.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoveChatEvent extends ApplicationEvent {

    public RemoveChatEvent(List<String> source) {
        super(source);
    }

    public List<String> getTokens(){
        return (List<String>) this.source;
    }
}
