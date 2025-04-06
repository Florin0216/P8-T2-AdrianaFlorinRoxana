package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import com.example.p8t2adrianaflorinroxana.service.ChatServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.MessageServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.UserServiceImpl;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    private final MessageServiceImpl messageService;
    private final ChatServiceImpl chatService;
    private final UserServiceImpl userService;

    public MessageController(MessageServiceImpl messageService, ChatServiceImpl chatService, UserServiceImpl userService) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
    }

    @MessageMapping("/chat/sendMessage/{id}")
    @SendTo("/topic/messages")
    public Messages sendMessage(@Payload Messages messages, @DestinationVariable("id") Long id, Principal principal) {

        Chats chat = chatService.getById(id);
        Users user = userService.getUserByUsername(principal.getName());

        messages.setSender(user);
        messages.setChat(chat);
        return messageService.saveMessage(messages);
    }
}
