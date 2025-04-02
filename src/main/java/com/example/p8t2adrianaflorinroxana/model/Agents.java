package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Agents")
public class Agents {

    @Id
    @Column(name = "agent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "gender", nullable = false, length = 1)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "corps", nullable = false, length = 30)
    private AgentCorps agentCorps;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false, length = 30)
    private AgentRank agentRank;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private int phoneNumber;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "address", length = 30)
    private String address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Stations station;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resources> resources;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Stations getStation() {
        return station;
    }

    public void setStation(Stations station) {
        this.station = station;
    }

    public AgentCorps getCorps() {
        return agentCorps;
    }

    public void setCorps(AgentCorps agentCorps) {
        this.agentCorps = agentCorps;
    }

    public AgentRank getRank() {
        return agentRank;
    }

    public void setRank(AgentRank agentRank) {
        this.agentRank = agentRank;
    }
}
