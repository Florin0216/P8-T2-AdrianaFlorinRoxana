package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.service.StationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/station")
public class StationController {

    private final StationServiceImpl stationService;

    public StationController(StationServiceImpl stationService) {
        this.stationService = stationService;
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
}
