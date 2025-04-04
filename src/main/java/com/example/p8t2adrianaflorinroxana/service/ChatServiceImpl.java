package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chats create(Chats chat){
        return chatRepository.save(chat);
    }

    public Chats getById(long id){
        return chatRepository.findById(id).orElse(null);
    }
}
