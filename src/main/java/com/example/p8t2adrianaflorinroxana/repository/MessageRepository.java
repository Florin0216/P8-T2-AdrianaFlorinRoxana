package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {
    List<Messages> findMessagesByChat_Id(Long chatId);

    List<Messages> findByChat_Id(Long chatId);

    List<Messages> findByChatIdAndSenderNot(Long chatId, Users sender);
}
