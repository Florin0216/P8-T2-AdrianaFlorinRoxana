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

    @Column(name = "maintenace_date", nullable = false)
    private LocalDate maintenanceDate;

    @Column(name = "maintenace_time", nullable = false)
    private LocalTime maintenaceTime;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agents agent;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Stations station;
}
