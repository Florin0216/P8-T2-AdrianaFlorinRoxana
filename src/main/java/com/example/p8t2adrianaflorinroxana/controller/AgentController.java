package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.StationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private final AgentServiceImpl agentService;
    private final StationServiceImpl stationService;

    public AgentController(AgentServiceImpl agentService, StationServiceImpl stationService) {
        this.agentService = agentService;
        this.stationService = stationService;
    }

    @GetMapping("/{stationId}/create")
    public String createAgent(@PathVariable long stationId, Model model) {

        model.addAttribute("stationId", stationId);
        model.addAttribute("agent", new Agents());
        return "Agent/createAgent";
    }

    @PostMapping("/{stationId}/create")
    public String createAgent(@PathVariable long stationId, @ModelAttribute("agent") Agents agent) {

        Stations station = stationService.getStationById(stationId);
        agent.setStation(station);
        agentService.addAgent(agent);
        return "redirect:/";
    }

    @GetMapping("/{id}/delete")
    public String deleteAgent(@PathVariable long id) {
        agentService.deleteAgent(id);
        return "redirect:/station/view";
    }

}
