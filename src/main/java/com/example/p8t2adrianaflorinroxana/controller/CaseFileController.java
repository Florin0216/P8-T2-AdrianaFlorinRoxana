package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.*;
import com.example.p8t2adrianaflorinroxana.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/case")
public class CaseFileController {

    private final CaseFileServiceImpl caseFileService;
    private final StationServiceImpl stationService;
    private final AgentServiceImpl agentService;
    private final UserServiceImpl userService;

    public CaseFileController(CaseFileServiceImpl caseFileService, StationServiceImpl stationService, AgentServiceImpl agentService, UserServiceImpl userService) {
        this.caseFileService = caseFileService;
        this.stationService = stationService;
        this.agentService = agentService;
        this.userService = userService;
    }

    @GetMapping("/listing")
    public String viewCases(Model model,
                            @RequestParam(name = "category", defaultValue = "all") String category,
                            @RequestParam(name = "status", defaultValue = "all") String status,
                            @RequestParam(name = "searchTerm", required = false) String searchTerm) {

        List<CaseFiles> allCases = caseFileService.getAllCaseFiles();

        if(!category.equals("all")) {
            allCases = allCases.stream()
                    .filter(c -> c.getCaseCategory().equals(category))
                    .collect(Collectors.toList());
        }

        if(!status.equals("all")) {
            allCases = allCases.stream()
                    .filter(c -> c.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if(searchTerm != null && !searchTerm.isEmpty()) {
            allCases = caseFileService.getCaseFilesByNameOrAgentName(searchTerm,
                    agentService.getAgentByName(searchTerm, searchTerm));
        }

        model.addAttribute("cases", allCases);
        return "CaseFile/ListCases";
    }

    @GetMapping("/create")
    public String createCase(Model model) {

        List<Agents> agents = agentService.getAllAgents();
        List<Stations> stations = stationService.getAllStations();

        model.addAttribute("case", new CaseFiles());
        model.addAttribute("agents", agents);
        model.addAttribute("stations", stations);
        return "CaseFile/CreateCase";
    }

    @PostMapping("/create")
    public String createCase(@ModelAttribute("case") CaseFiles caseFiles,
                             @RequestParam(value = "evidenceName", required = false) String evidenceName,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        caseFileService.createCaseFile(caseFiles,file,evidenceName);

        return "redirect:/case/listing";
    }

    @GetMapping("/{id}/view")
    public String viewCase(@PathVariable Long id, Model model) {

        CaseFiles caseFile = caseFileService.findCaseFileById(id);
        model.addAttribute("case", caseFile);
        return "CaseFile/ViewCase";
    }


    @GetMapping("/{id}/update")
    public String updateCase(@PathVariable long id, Model model) {

        CaseFiles caseFile = caseFileService.getCaseFileById(id);
        List<Agents> agents = agentService.getAllAgents();
        List<Stations> stations = stationService.getAllStations();

        model.addAttribute("case", caseFile);
        model.addAttribute("agents", agents);
        model.addAttribute("stations", stations);
        return "CaseFile/UpdateCase";
    }

    @PostMapping("/{id}/update")
    public String updateCase(@ModelAttribute("case") CaseFiles caseFiles, Principal principal) {
        Users loggedUser = userService.getUserByUsername(principal.getName());

        caseFileService.updateCaseFile(caseFiles, loggedUser);
        return "redirect:/case/listing";
    }
}
