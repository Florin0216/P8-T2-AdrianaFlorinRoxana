package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Resources;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import com.example.p8t2adrianaflorinroxana.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;


    @Test
    void saveResource() {
        Resources res = new Resources();
        res.setResourceName("Patrol Car");
        res.setResourceType("Vehicle");

        resourceService.saveResource(res);

        assertEquals("AVAILABLE", res.getStatus());
        assertNull(res.getMaintenanceDate());
        assertNull(res.getMaintenanceTime());

        verify(resourceRepository, times(2)).save(res);
    }

    @Test
    void getResourceById() {
        Resources res = new Resources();
        res.setId(1L);
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(res));

        Resources result = resourceService.getResourceById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void editResource() {
        Resources existing = new Resources();
        existing.setId(1L);
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(existing));

        Resources updated = new Resources();
        updated.setId(1L);
        updated.setResourceName("Patrol Car");
        updated.setResourceType("Vehicle");
        updated.setStatus("IN_USE");
        updated.setMaintenanceDate(LocalDate.now());
        updated.setMaintenanceTime(LocalTime.NOON);

        resourceService.editResource(updated);

        assertEquals("Patrol Car", existing.getResourceName());
        verify(resourceRepository).save(existing);
    }

    @Test
    void deleteResource() {
        when(resourceRepository.existsById(1L)).thenReturn(true);

        resourceService.deleteResource(1L);

        verify(resourceRepository).deleteById(1L);
    }

    @Test
    void assignResourceToAgent() {
        Resources res = new Resources();
        res.setId(1L);
        res.setStatus("AVAILABLE");

        Agents agent = new Agents();
        agent.setId(1L);

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(res));
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));

        boolean success = resourceService.assignResourceToAgent(1L, 1L);

        assertTrue(success);
        assertEquals("IN_USE", res.getStatus());
        assertEquals(agent, res.getAssignedAgent());
        verify(resourceRepository).save(res);
    }

    @Test
    void scheduleMaintenance() {
        Resources res = new Resources();
        res.setId(1L);

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(res));

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.NOON;

        boolean scheduled = resourceService.scheduleMaintenance(1L, date, time);

        assertTrue(scheduled);
        assertEquals(date, res.getMaintenanceDate());
        assertEquals(time, res.getMaintenanceTime());
    }

    @Test
    void completeMaintenance() {
        Resources res = new Resources();
        res.setId(1L);
        res.setStatus("AVAILABLE");
        res.setMaintenanceDate(LocalDate.now());
        res.setMaintenanceTime(LocalTime.NOON);

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(res));

        boolean completed = resourceService.completeMaintenance(1L);

        assertTrue(completed);
        assertNull(res.getMaintenanceDate());
        assertNull(res.getMaintenanceTime());
        assertEquals("AVAILABLE", res.getStatus());
    }
}
