package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Suspects")
public class Suspects {

    @Id
    @Column(name = "suspect_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;
}
