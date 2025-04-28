package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.PublicInteraction;
import com.example.p8t2adrianaflorinroxana.repository.InteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class InteractionServiceImpl {

    private final InteractionRepository interactionRepository;
    private final CaseFileServiceImpl caseFileService;
    private final EmailServiceImpl emailService;

    public InteractionServiceImpl(InteractionRepository interactionRepository, CaseFileServiceImpl caseFileService, EmailServiceImpl emailService) {
        this.interactionRepository = interactionRepository;
        this.caseFileService = caseFileService;
        this.emailService = emailService;
    }

    public void createPublicInteraction(PublicInteraction publicInteraction, MultipartFile file, String evidenceName) throws IOException {

        caseFileService.createCaseFile(publicInteraction.getCaseFile(), file, evidenceName);
        interactionRepository.save(publicInteraction);

        String trackingUrl = "http://localhost:8080/public/cases?email=" + URLEncoder.encode(publicInteraction.getCitezenEmail(), StandardCharsets.UTF_8);

        emailService.sendSimpleMessage(publicInteraction.getCitezenEmail(),
                "Report received",
                "Thanks for reporting a crime.\n\n" +
                        "You can view all your reports at any time using this secure link:\n" +
                        trackingUrl + "\n\n");
    }

    public List<PublicInteraction> getPublicInteractionsBYEmail(String email) {

        return interactionRepository.findAllByCitezenEmail(email);
    }
}
