package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Resources;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.service.AgentServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.ResourceServiceImpl;
import com.example.p8t2adrianaflorinroxana.service.StationServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceServiceImpl resourceService;
    private final AgentServiceImpl agentService;
    private final StationServiceImpl stationService;

    public ResourceController(ResourceServiceImpl resourceService, AgentServiceImpl agentService, StationServiceImpl stationService) {
        this.resourceService = resourceService;
        this.agentService = agentService;
        this.stationService = stationService;
    }

    @GetMapping("/view")
    public String ResourceView(Model model)
    {
        List<Resources> resources = resourceService.getAllResources();
        model.addAttribute("resources", resources);
        return "Resource/ResourceView";
    }

    @GetMapping("/add")
    public String AddResource(Model model) {
        model.addAttribute("resource", new Resources());
        return "Resource/AddResource";
    }

    @PostMapping("/add")
    public String addResource(@ModelAttribute("resource") Resources resource,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maintenanceDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime maintenanceTime,
                              RedirectAttributes redirectAttributes) {
        resource.setMaintenanceDate(maintenanceDate);
        resource.setMaintenanceTime(maintenanceTime);
        try {
            resourceService.saveResource(resource);
            redirectAttributes.addFlashAttribute("successMessage", "Resource added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding resource: " + e.getMessage());
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditResourceForm(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Resources resource = resourceService.getResourceById(id);
        if (resource == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource not found with ID: " + id);
            return "redirect:/resource/view";
        }
        model.addAttribute("resource", resource);
        return "Resource/EditResource";
    }

    @PostMapping("/{id}/edit")
    public String editResource(@PathVariable long id, @ModelAttribute("resource") Resources resource, RedirectAttributes redirectAttributes) {
        resource.setId(id);
        try {
            resourceService.editResource(resource);
            redirectAttributes.addFlashAttribute("successMessage", "Resource updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating resource: " + e.getMessage());
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/delete")
    public String deleteResource(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            Resources resource = resourceService.getResourceById(id);
            if (resource != null && resource.getAssignedAgent() == null && resource.getAssignedStation() == null) {
                resourceService.deleteResource(id);
                redirectAttributes.addFlashAttribute("successMessage", "Resource deleted successfully!");
            } else if (resource == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Resource not found with ID: " + id);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete resource with ID: " + id + ". It might be assigned.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting resource: " + e.getMessage());
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/assignAgent")
    public String showAssignAgentForm(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Resources resource = resourceService.getResourceById(id);
        if (resource == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource not found with ID: " + id);
            return "redirect:/resource/view";
        }
        if (!"AVAILABLE".equalsIgnoreCase(resource.getStatus())){
            redirectAttributes.addFlashAttribute("errorMessage", "resource "+ id + " is not available for assignment.");
            return "redirect:/resource/view";
        }

        List<Agents> availableAgents = agentService.getAllAgents();
        model.addAttribute("resource", resource);
        model.addAttribute("agents", availableAgents);
        return "Resource/AssignAgent";
    }

    @PostMapping("/{id}/assignAgent")
    public String assignAgent(@PathVariable long id, @RequestParam("agentId") long agentId, RedirectAttributes redirectAttributes) {
        boolean success = resourceService.assignResourceToAgent(id, agentId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Resource assigned to agent successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to assign resource. Resource or agent not found, or resource not available.");
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/assignStation")
    public String showAssignStationForm(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Resources resource = resourceService.getResourceById(id);
        if (resource == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource not found with ID: " + id);
            return "redirect:/resource/view";
        }

        List<Stations> stations = stationService.getAllStations();
        model.addAttribute("resource", resource);
        model.addAttribute("stations", stations);
        return "Resource/AssignStation";
    }

    @PostMapping("/{id}/assignStation")
    public String assignStation(@PathVariable long id, @RequestParam("stationId") long stationId, RedirectAttributes redirectAttributes) {
        boolean success = resourceService.assignResourceToStation(id, stationId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Resource assigned to station successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to assign resource. Resource or station not found.");
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/unassign")
    public String unassign(@PathVariable long id, RedirectAttributes redirectAttributes) {
        boolean success = resourceService.unassignResource(id);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Resource unassigned successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to unassign resource. Resource not found.");
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/scheduleMaintenance")
    public String showScheduleMaintenanceForm(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Resources resource = resourceService.getResourceById(id);
        if (resource == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource not found with ID: " + id);
            return "redirect:/resource/view";
        }
        model.addAttribute("resource", resource);
        return "Resource/ScheduleMaintenance";
    }

    @PostMapping("/{id}/scheduleMaintenance")
    public String scheduleMaintenance(@PathVariable long id,
                                      @RequestParam("maintenanceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @RequestParam("maintenanceTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                      RedirectAttributes redirectAttributes) {
        boolean success = resourceService.scheduleMaintenance(id, date, time);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Maintenance scheduled successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to schedule maintenance. Resource not found.");
        }
        return "redirect:/resource/view";
    }

    @GetMapping("/{id}/completeMaintenance")
    public String completeMaintenance(@PathVariable long id, RedirectAttributes redirectAttributes) {
        boolean success = resourceService.completeMaintenance(id);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Maintenance completed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to complete maintenance. Resource not found.");
        }
        return "redirect:/resource/view";
    }
}