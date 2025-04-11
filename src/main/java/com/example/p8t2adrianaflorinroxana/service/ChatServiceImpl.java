package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.ChatRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatServiceImpl(ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Chats create(Chats chat) {
        return chatRepository.save(chat);
    }

    public Chats getById(long id) {
        return chatRepository.findById(id).orElse(null);
    }

    public List<Chats> getAllChatsForUser(Users user) {
        return chatRepository.findByUsers_Id(user.getId());
    }

    public List<Chats> getAllChatsExceptConferences(Users user) {

        List<Chats> userChats = chatRepository.findByUsers_Id(user.getId());
        userChats.removeIf(chat -> chat.getType().equals("conference"));
        return userChats;
    }

    @Scheduled(cron = "0 * * * * *")
    public void notifyUsersForScheduledConferences() {
        List<Chats> upcomingConferences = chatRepository.findByTypeAndStatus("conference", "scheduled");
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        for (Chats conf : upcomingConferences) {
            LocalTime scheduledTime = conf.getScheduledTime().truncatedTo(ChronoUnit.MINUTES);
            if (nowDate.isEqual(conf.getScheduledDate()) && nowTime.equals(scheduledTime)) {
                for (Users user : conf.getUsers()) {
                    messagingTemplate.convertAndSendToUser(
                            user.getUsername(),
                            "/queue/conference-notifications",
                            Map.of(
                                    "title", conf.getReason(),
                                    "joinUrl", "http://localhost:8080/chat/" + conf.getId() + "/verify"
                            )
                    );
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateRecurringConferences() {
        List<Chats> scheduledConferences = chatRepository.findByTypeAndStatus("conference", "scheduled");
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        for (Chats conf : scheduledConferences) {
            LocalTime scheduledTime = conf.getScheduledTime().truncatedTo(ChronoUnit.MINUTES);

            if (nowDate.isAfter(conf.getScheduledDate()) ||
                    (nowDate.isEqual(conf.getScheduledDate()) && nowTime.isAfter(scheduledTime))) {

                if (conf.getIsRecurring().equals("true")) {
                    switch (conf.getRecurrence()) {
                        case "DAILY":
                            conf.setScheduledDate(conf.getScheduledDate().plusDays(1));
                            break;
                        case "WEEKLY":
                            conf.setScheduledDate(conf.getScheduledDate().plusWeeks(1));
                            break;
                        case "MONTHLY":
                            conf.setScheduledDate(conf.getScheduledDate().plusMonths(1));
                            break;
                    }
                    chatRepository.save(conf);
                }
            }
        }
    }


}
