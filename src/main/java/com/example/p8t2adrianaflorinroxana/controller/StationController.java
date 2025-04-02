package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.AgentRank;
import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.StationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/station")
public class StationController {

    private final StationServiceImpl stationService;
    private final AgentServiceImpl agentService;

    public StationController(StationServiceImpl stationService, AgentServiceImpl agentService) {
        this.stationService = stationService;
        this.agentService = agentService;
    }

    @GetMapping("/view")
    public String viewStations(Model model) {
        List<Stations> stations = stationService.getAllStations();
        model.addAttribute("stations", stations);
        return "Station/StationView";
    }

    @GetMapping("/add")
    public String showAddStationForm(Model model) {
        model.addAttribute("station", new Stations());
        return "Station/AddStation";
    }

    @PostMapping("/add")
    public String addStation(@ModelAttribute("station") Stations station) {
        stationService.saveStation(station);
        return "redirect:/station/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditStationForm(@PathVariable long id, Model model) {
        Stations station = stationService.getStationById(id);
        model.addAttribute("station", station);
        return "Station/EditStation";
    }

    @PostMapping("/{id}/edit")
    public String editStation(@ModelAttribute("station") Stations station) {

        stationService.editStation(station);
        return "redirect:/station/view";
    }

    @GetMapping("/{id}/delete")
    public String deleteStation(@PathVariable long id) {

        stationService.deleteStation(id);
        return "redirect:/station/view";
    }

    @GetMapping("/{id}/hierarchy")
    public String showHierarchy(@PathVariable long id,
                                @RequestParam(value = "corps", defaultValue = "Officer") String corps,
                                Model model) {

        Stations station = stationService.getStationById(id);
        List<Agents> agents = agentService.getAllAgentsForStation(id);

        List<Agents> filteredAgents = agents.stream()
                .filter(agent -> agent.getRank().getCorps().name().equalsIgnoreCase(corps)).sorted(Comparator.comparing(agent -> agent.getRank().ordinal())).collect(Collectors.toList());

        Map<AgentRank, List<Agents>> agentsByRank = new TreeMap<>(Comparator.comparingInt(Enum::ordinal));
        filteredAgents.forEach(agent ->
                agentsByRank.computeIfAbsent(agent.getRank(), k -> new ArrayList<>()).add(agent)
        );

        model.addAttribute("station", station);
        model.addAttribute("agentsByRank", agentsByRank);
        model.addAttribute("selectedCorps", corps);
        model.addAttribute("allRanks", AgentRank.values());

        return "Station/Hierarchy";
    }
}
