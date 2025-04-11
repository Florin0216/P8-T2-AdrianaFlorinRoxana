package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class MessageServiceImpl {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Messages saveMessage(Users user, Chats chat, Messages message) {

        message.setSentAt(LocalTime.now());
        message.setSender(user);
        message.setChat(chat);
        return messageRepository.save(message);
    }

    public void markAsRead(Long chatId, Users user) {
        List<Messages> unreadMessages = messageRepository.findByChatIdAndSenderNot(chatId, user);

        LocalTime now = LocalTime.now();
        for (Messages unreadMessage : unreadMessages) {
            if(unreadMessage.getReadAt() == null){
                unreadMessage.setReadAt(now);
            }
        }

        messageRepository.saveAll(unreadMessages);
    }
}
