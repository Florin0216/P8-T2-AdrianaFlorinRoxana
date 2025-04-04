package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.repository.MessageRepository;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class MessageServiceImpl {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Messages saveMessage(Messages message) {
        message.setSentAt(LocalTime.now());
         return messageRepository.save(message);
    }
}
