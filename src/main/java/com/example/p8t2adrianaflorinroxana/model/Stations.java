package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Stations")
public class Stations {

    @Id
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "station_name", nullable = false, length = 30)
    private String stationName;

    @Column(name = "station_email", nullable = false, length = 30)
    private String stationEmail;

    @Column(name = "station_phone", nullable = false)
    private int stationPhone;

    @Column(name = "station_address", nullable = false, length = 40)
    private String stationAddress;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agents> agents;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseFiles> caseFiles;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resources> resources;
}
