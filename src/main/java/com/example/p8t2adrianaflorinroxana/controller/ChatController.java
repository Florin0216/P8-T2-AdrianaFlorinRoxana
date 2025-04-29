package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.model.Messages;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.MessageRepository;
import com.example.p8t2adrianaflorinroxana.service.ChatServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.UserServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/create")
    public String createForm(Model model, Principal principal) {

        List<Users> allUsers = userService.getAllUsers();
        allUsers.remove(userService.getUserByUsername(principal.getName()));

        model.addAttribute("allUsers", allUsers);
        model.addAttribute("chat", new Chats());
        return "Chat/Create";
    }

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/create")
    public String create(@ModelAttribute Chats chat,
                         Principal principal,
                         @RequestParam(value = "selectedUser", required = false) Long selectedUser,
                         @RequestParam(value = "selectedUsers", required = false) List<Long> selectedUsers) {

        Users loggedUser = userService.getUserByUsername(principal.getName());
        List<Users> users = new ArrayList<>();
        users.add(loggedUser);

        if (chat.getType().equals("single") && selectedUser != null) {
            List<Chats> userChats = chatService.getAllChatsForUser(loggedUser);
            for (Chats existingChat : userChats) {
                if (existingChat.getType().equals("single")
                        && existingChat.getUsers().contains(userService.getUserById(selectedUser))
                        && existingChat.getUsers().size() == 2) {

                    return "redirect:/chat/" + existingChat.getId();
                }
            }
            users.add(userService.getUserById(selectedUser));
        } else if (chat.getType().equals("group") && selectedUsers != null) {
            for (Long selectedUserId : selectedUsers) {
                users.add(userService.getUserById(selectedUserId));
            }
        } else if (chat.getType().equals("conference") && selectedUsers != null) {
            chat.setStatus("scheduled");
            for (Long selectedUserId : selectedUsers) {
                users.add(userService.getUserById(selectedUserId));
            }
        }

        chat.setUsers(users);
        Chats newChat = chatService.create(chat);
        return "redirect:/chat/" + newChat.getId();
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public String showChat(@PathVariable long id, Model model, Principal principal) {

        Chats thisChat = chatService.getById(id);
        List<Messages> pastMessages = messageRepository.findMessagesByChat_Id(id);

        if (!thisChat.getUsers().contains(userService.getUserByUsername(principal.getName()))) {
            return "redirect:/chat/show";
        }

        model.addAttribute("thisChat", thisChat);
        model.addAttribute("username", principal.getName());
        model.addAttribute("pastMessages", pastMessages);
        model.addAttribute("chatId", id);
        return "Chat/View";
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/show")
    public String showChats(Model model,
                            Principal principal,
                            @RequestParam(name = "type", defaultValue = "single")String type) {

        Users loggedUser = userService.getUserByUsername(principal.getName());
        List<Chats> userChats = chatService.getAllChatsExceptConferences(loggedUser);
        model.addAttribute("userChats", userChats);
        model.addAttribute("status", type);
        return "Chat/ListedChats";
    }

    @GetMapping("/{id}/verify")
    public String verifyChat(@PathVariable("id") Long id, Model model) {

        model.addAttribute("chatId", id);
        return "Chat/Verify";
    }

    @PostMapping("/{id}/verify")
    public String verifyChat(@PathVariable("id") Long id, @RequestParam("code") String code) {

        Chats thisChat = chatService.getById(id);
        if (!thisChat.getPersonalCode().equals(code)) {
            return "redirect:/chat/"+ id +"/verify";
        }
        return "redirect:/chat/" + thisChat.getId();
    }
}
