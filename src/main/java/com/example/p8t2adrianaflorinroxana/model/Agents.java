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

    @Column(name = "rank", nullable = false, length = 20)
    private String rank;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private int phoneNumber;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Stations station;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resources> resources;


}
