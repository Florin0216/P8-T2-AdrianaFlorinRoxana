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

    @Column(name = "station_name", nullable = false, length = 50)
    private String stationName;

    @Column(name = "station_email", length = 50)
    private String stationEmail;

    @Column(name = "station_phone", nullable = false)
    private int stationPhone;

    @Column(name = "station_address", nullable = false, length = 100)
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationEmail() {
        return stationEmail;
    }

    public void setStationEmail(String stationEmail) {
        this.stationEmail = stationEmail;
    }

    public int getStationPhone() {
        return stationPhone;
    }

    public void setStationPhone(int stationPhone) {
        this.stationPhone = stationPhone;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
