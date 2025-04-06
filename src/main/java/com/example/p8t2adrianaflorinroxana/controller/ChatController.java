package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.MessageRepository;
import com.example.p8t2adrianaflorinroxana.service.ChatServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatServiceImpl chatService;
    private final UserServiceImpl userService;
    private final MessageRepository messageRepository;

    public ChatController(ChatServiceImpl chatService, UserServiceImpl userService, MessageRepository messageRepository) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageRepository = messageRepository;
    }


    @GetMapping("/create")
    public String createForm(Model model, Principal principal) {

        List<Users> allUsers = userService.getAllUsers();
        allUsers.remove(userService.getUserByUsername(principal.getName()));

        model.addAttribute("allUsers", allUsers);
        model.addAttribute("chat", new Chats());
        return "Chat/Create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Chats chat,
                         Principal principal,
                         @RequestParam(value = "selectedUser", required = false) Long selectedUser,
                         @RequestParam(value = "selectedUsers", required = false) List<Long> selectedUsers) {

        Users loggedUser = userService.getUserByUsername(principal.getName());
        List<Users> users = new ArrayList<>();
        users.add(loggedUser);

        if(chat.getType().equals("single") && selectedUser != null) {
            users.add(userService.getUserById(selectedUser));
        }else if(chat.getType().equals("group") && selectedUsers != null) {
            for(Long selectedUserId : selectedUsers) {
                users.add(userService.getUserById(selectedUserId));
            }
        }

        chat.setUsers(users);
        Chats newChat = chatService.create(chat);
        return "redirect:/chat/" + newChat.getId();
    }

    @GetMapping("/{id}")
    public String showChat(@PathVariable long id, Model model, Principal principal) {

        Chats thisChat = chatService.getById(id);
        List<Messages> pastMessages = messageRepository.findMessagesByChat_Id(id);

        if(!thisChat.getUsers().contains(userService.getUserByUsername(principal.getName()))) {
            return "redirect:/chat/show";
        }

        model.addAttribute("username", principal.getName());
        model.addAttribute("pastMessages", pastMessages);
        model.addAttribute("chatId", id);
        return "Chat/View";
    }

    @GetMapping("/show")
    public String showChats(Model model, Principal principal) {

        Users loggedUser = userService.getUserByUsername(principal.getName());
        List<Chats> userChats = chatService.getAllChatsForUser(loggedUser);
        model.addAttribute("userChats", userChats);
        return "Chat/ListedChats";
    }
}
