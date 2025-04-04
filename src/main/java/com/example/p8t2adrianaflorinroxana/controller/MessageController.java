package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.service.ChatServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.MessageServiceImpl;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageServiceImpl messageService;
    private final ChatServiceImpl chatService;

    public MessageController(MessageServiceImpl messageService, ChatServiceImpl chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/chat/{id}")
    public Messages sendMessage(@Payload Messages messages, @DestinationVariable("id") Long id) {

        Chats chat = chatService.getById(id);
        messages.setChat(chat);
        return messageService.saveMessage(messages);
    }
}
