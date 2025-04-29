package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.EmailServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class UserController {

    private final EmailServiceImpl emailService;
    private final AgentServiceImpl agentService;

    public UserController(EmailServiceImpl emailService, AgentServiceImpl agentService) {
        this.emailService = emailService;
        this.agentService = agentService;
    }

    @GetMapping("/login")
    public String loginForm() {

        return "User/Login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {

        return "User/ForgotPassword";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {

        Agents agent = agentService.getAgentByEmail(email);

        if(agent != null) {
            emailService.sendSimpleMessage(email,
                    "Password Reset Request",
                    "Click the link to reset your password: \n" +
                            "http://localhost:8080/reset-password?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
        }

        return  "redirect:/login";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String email, Model model) {

        model.addAttribute("email", email);
        return "User/ResetPassword";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/reset-password?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
        }

        agentService.changePassword(email, password);

        return "redirect:/login";
    }

}
