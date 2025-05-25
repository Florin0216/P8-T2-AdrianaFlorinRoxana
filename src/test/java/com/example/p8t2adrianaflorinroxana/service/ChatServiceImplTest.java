package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Chats chat = new Chats();
        when(chatRepository.save(chat)).thenReturn(chat);

        Chats saved = chatService.create(chat);

        assertNotNull(saved);
        verify(chatRepository).save(chat);
    }

    @Test
    void getById_returnsChat() {
        Chats chat = new Chats();
        chat.setId(1L);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        Chats result = chatService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void GetById_returnsNullIfNotFound() {
        when(chatRepository.findById(99L)).thenReturn(Optional.empty());

        Chats result = chatService.getById(99L);

        assertNull(result);
    }

    @Test
    void getAllChatsForUser() {
        Users user = new Users();
        user.setId(1L);
        List<Chats> chats = Arrays.asList(new Chats(), new Chats());
        when(chatRepository.findByUsers_Id(1L)).thenReturn(chats);

        List<Chats> result = chatService.getAllChatsForUser(user);

        assertEquals(2, result.size());
    }

    @Test
    void GetAllChatsExceptConferences() {
        Users user = new Users();
        user.setId(1L);

        Chats chat1 = new Chats();
        chat1.setType("chat");
        Chats chat2 = new Chats();
        chat2.setType("conference");

        when(chatRepository.findByUsers_Id(1L)).thenReturn(new java.util.ArrayList<>(Arrays.asList(chat1, chat2)));

        List<Chats> result = chatService.getAllChatsExceptConferences(user);

        assertEquals(1, result.size());
        assertEquals("chat", result.get(0).getType());
    }

    @Test
    void updateRecurringConferences_dailyInPast_isRescheduled() {
        Chats conf = new Chats();
        conf.setId(1L);
        conf.setType("conference");
        conf.setStatus("scheduled");
        conf.setScheduledDate(LocalDate.now().minusDays(1));
        conf.setScheduledTime(LocalTime.now().minusMinutes(5));
        conf.setIsRecurring("true");
        conf.setRecurrence("DAILY");

        when(chatRepository.findByTypeAndStatus("conference", "scheduled")).thenReturn(List.of(conf));

        chatService.updateRecurringConferences();

        verify(chatRepository, times(1)).save(conf);
        assertEquals(LocalDate.now(), conf.getScheduledDate());
    }

    @Test
    void updateRecurringConferences_nonRecurring_notSaved() {
        Chats conf = new Chats();
        conf.setId(1L);
        conf.setType("conference");
        conf.setStatus("scheduled");
        conf.setScheduledDate(LocalDate.now().minusDays(1));
        conf.setScheduledTime(LocalTime.now().minusMinutes(5));
        conf.setIsRecurring("false");

        when(chatRepository.findByTypeAndStatus("conference", "scheduled")).thenReturn(List.of(conf));

        chatService.updateRecurringConferences();

        verify(chatRepository, never()).save(any());
    }

    @Test
    void notifyUsersForScheduledConferences_sendsNotification() {
        Users user = new Users();
        user.setUsername("user");

        Chats chat = new Chats();
        chat.setId(5L);
        chat.setReason("Important Meeting");
        chat.setType("conference");
        chat.setStatus("scheduled");
        chat.setScheduledDate(LocalDate.now());
        chat.setScheduledTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
        chat.setUsers(List.of(user));

        when(chatRepository.findByTypeAndStatus("conference", "scheduled")).thenReturn(List.of(chat));

        chatService.notifyUsersForScheduledConferences();

        verify(messagingTemplate).convertAndSendToUser(eq("user"), eq("/queue/conference-notifications"), anyMap());
    }

    @Test
    void notifyUsersForScheduledConferences_noConferences_noNotification() {
        when(chatRepository.findByTypeAndStatus("conference", "scheduled"))
                .thenReturn(Collections.emptyList());

        chatService.notifyUsersForScheduledConferences();

        verifyNoInteractions(messagingTemplate);
    }
}