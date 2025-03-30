package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private final AgentServiceImpl agentService;

    public AgentController(AgentServiceImpl agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/create")
    public String createAgent(Model model) {

        model.addAttribute("agent", new Agents());
        return "Agent/createAgent";
    }

    @PostMapping("/create")
    public String createAgent(@ModelAttribute("agent") Agents agent) {
        agentService.addAgent(agent);
        return "redirect:/";
    }

}
