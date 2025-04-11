package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.CaseFiles;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.CaseFileServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.StationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/case")
public class CaseFileController {

    private final CaseFileServiceImpl caseFileService;
    private final StationServiceImpl stationService;
    private final AgentServiceImpl agentService;

    public CaseFileController(CaseFileServiceImpl caseFileService, StationServiceImpl stationService, AgentServiceImpl agentService) {
        this.caseFileService = caseFileService;
        this.stationService = stationService;
        this.agentService = agentService;
    }

    @GetMapping("/view")
    public String viewCases(Model model) {

        return "CaseFile/ViewCases";
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
    public String createCase(@ModelAttribute("case") CaseFiles caseFiles) {

        caseFileService.createCaseFile(caseFiles);
        return "redirect:/case/view";
    }

}
