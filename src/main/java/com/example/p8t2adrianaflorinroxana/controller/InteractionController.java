package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.PublicInteraction;
import com.example.p8t2adrianaflorinroxana.service.InteractionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/public")
public class InteractionController {

    private final InteractionServiceImpl interactionService;

    public InteractionController(InteractionServiceImpl interactionService) {
        this.interactionService = interactionService;
    }

    @GetMapping("/report")
    public String reportForm(Model model) {

        model.addAttribute("interaction",new PublicInteraction());
        return "Interaction/ReportForm";
    }

    @PostMapping("/report")
    public String reportForm(@ModelAttribute("interaction") PublicInteraction interaction,
                             @RequestParam(value = "evidenceName", required = false) String evidenceName,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        interactionService.createPublicInteraction(interaction,file,evidenceName);
        return "redirect:/";
    }

    @GetMapping("/cases")
    public String citizenCases(Model model, @RequestParam(name = "email")String email) {

        List<PublicInteraction> citizenInteractions= interactionService.getPublicInteractionsBYEmail(email);
        model.addAttribute("interactions",citizenInteractions);
        return "Interaction/Cases";
    }
}
