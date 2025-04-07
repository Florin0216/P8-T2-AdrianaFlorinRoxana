package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;
import org.aspectj.weaver.loadtime.Agent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Resources")
public class Resources {

    @Id
    @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_name", nullable = false, length = 20)
    private String resourceName;

    @Column(name = "resource_type", nullable = false, length = 20)
    private String resourceType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "maintenace_date", nullable = true)
    private LocalDate maintenanceDate;

    @Column(name = "maintenace_time", nullable = true)
    private LocalTime maintenanceTime;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agents agent;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Stations station;

    public Resources() {

    }

    public Resources(String resourceName, String resourceType, String status) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public LocalTime getMaintenanceTime()
    {
        return maintenanceTime;
    }

    public void setMaintenanceTime(LocalTime maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public Agents getAssignedAgent() {
        return agent;
    }

    public void setAssignedAgent(Agents assignedAgent) {
        this.agent = assignedAgent;
    }

    public Stations getAssignedStation() {
        return station;
    }

    public void setAssignedStation(Stations assignedStation) {
        this.station = assignedStation;
    }

}
