package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Chats;
import com.example.p8t2adrianaflorinroxana.service.ChatServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatServiceImpl chatService;

    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }


    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("chat", new Chats());
        return "Chat/Create";
    }

    @PostMapping("/create")
    public String create(Chats chat){

        Chats newChat = chatService.create(chat);
        return "redirect:/chat/" + newChat.getId();
    }
}
