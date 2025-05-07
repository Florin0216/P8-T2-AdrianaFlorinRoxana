package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Resources;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import com.example.p8t2adrianaflorinroxana.repository.ResourceRepository;
import com.example.p8t2adrianaflorinroxana.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl {

    private final ResourceRepository resourceRepository;
    private final AgentRepository agentRepository;
    private final StationRepository stationRepository;


    public ResourceServiceImpl(ResourceRepository resourceRepository, AgentRepository agentRepository, StationRepository stationRepository)
    {
        this.resourceRepository = resourceRepository;
        this.agentRepository = agentRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void saveResource(Resources resource) {
        if (resource.getResourceName() == null || resource.getResourceType() == null) {
            throw new IllegalArgumentException("Resource name and type cannot be null!");
        }
        if (resource.getStatus() == null) {
            resource.setStatus("AVAILABLE");
        }
        if (resource.getMaintenanceDate() == null)
        {
            resource.setMaintenanceDate(null);
        }
        if (resource.getMaintenanceTime() == null)
        {
            resource.setMaintenanceTime(null);
        }
        resourceRepository.save(resource);
        resource.setAssignedAgent(null);
        resource.setAssignedStation(null);

        resourceRepository.save(resource);
    }

    public List<Resources> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resources getResourceById(long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Transactional
    public void editResource(Resources resource) {
        Resources existingResource = getResourceById(resource.getId());
        if (existingResource != null) {
            existingResource.setResourceName(resource.getResourceName());
            existingResource.setResourceType(resource.getResourceType());
            existingResource.setStatus(resource.getStatus());
            existingResource.setMaintenanceDate(resource.getMaintenanceDate());
            existingResource.setMaintenanceTime(resource.getMaintenanceTime());

            resourceRepository.save(existingResource);
        } else {
            System.err.println("Resource with ID " + resource.getId() + " not found for editing.");
        }
    }

    @Transactional
    public void deleteResource(long id) {
        if (resourceRepository.existsById(id)) {
            resourceRepository.deleteById(id);
        } else {
            System.err.println("Resource with ID " + id + " not found for deletion.");
        }
    }

    @Transactional
    public boolean assignResourceToAgent(long resourceId, long agentId) {
        Optional<Resources> resourceOpt = resourceRepository.findById(resourceId);
        Optional<Agents> agentOpt = agentRepository.findById(agentId);

        if (resourceOpt.isPresent() && agentOpt.isPresent()) {
            Resources resource = resourceOpt.get();
            Agents agent = agentOpt.get();

            if (!"AVAILABLE".equalsIgnoreCase(resource.getStatus())) {
                System.err.println("Resource " + resourceId + " is not available for assignment.");
                return false;
            }

            resource.setStatus("IN_USE");
            resource.setAssignedAgent(agent);
            resource.setAssignedStation(null);
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }


    @Transactional
    public boolean assignResourceToStation(long resourceId, long stationId) {
        Optional<Resources> resourceOpt = resourceRepository.findById(resourceId);
        Optional<Stations> stationOpt = stationRepository.findById(stationId);

        if (resourceOpt.isPresent() && stationOpt.isPresent()) {
            Resources resource = resourceOpt.get();
            Stations station = stationOpt.get();
            resource.setStatus("IN_USE");
            resource.setAssignedStation(station);
            resource.setAssignedAgent(null);
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unassignResource(long resourceId) {
        Optional<Resources> resourceOpt = resourceRepository.findById(resourceId);

        if (resourceOpt.isPresent()) {
            Resources resource = resourceOpt.get();
            resource.setAssignedAgent(null);
            resource.setAssignedStation(null);
            resource.setStatus("AVAILABLE");
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean scheduleMaintenance(long resourceId, LocalDate date, LocalTime time) {
        Optional<Resources> resourceOpt = resourceRepository.findById(resourceId);

        if (resourceOpt.isPresent()) {
            Resources resource = resourceOpt.get();
            resource.setMaintenanceDate(date);
            resource.setMaintenanceTime(time);
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean completeMaintenance(long resourceId) {
        Optional<Resources> resourceOpt = resourceRepository.findById(resourceId);

        if (resourceOpt.isPresent()) {
            Resources resource = resourceOpt.get();
            resource.setMaintenanceDate(null);
            resource.setMaintenanceTime(null);
            if (!"IN_USE".equalsIgnoreCase(resource.getStatus())) {
                resource.setStatus("AVAILABLE");
            }
            resourceRepository.save(resource);
            return true;
        }
        return false;
    }

    public List<Resources> getResourcesByAgent(long agentId){
        return resourceRepository.findByAgentId(agentId);
    }

    public List<Resources> getResourcesByStation(long stationId){
        return resourceRepository.findByStationId(stationId);
    }

}