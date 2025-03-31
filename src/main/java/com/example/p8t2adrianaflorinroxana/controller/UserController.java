package com.example.p8t2adrianaflorinroxana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/login")
    public String loginForm() {

        return "User/Login";
    }
}
