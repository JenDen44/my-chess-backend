package com.chess.jnd.listener;

import com.chess.jnd.client.ChatClient;
import com.chess.jnd.event.RemoveChatEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoveChatListener {

    private final ChatClient chatClient;

    @Autowired
    public RemoveChatListener(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @EventListener
    public void listen(RemoveChatEvent event){
        log.debug("Call to chat service to remove chats by tokens");

        if (chatClient.deleteChat(event.getTokens())) {
            log.debug("Successfully removed chats by tokens");
        } else {
            log.error("Failed to remove chats by tokens");
        };
    }
}
