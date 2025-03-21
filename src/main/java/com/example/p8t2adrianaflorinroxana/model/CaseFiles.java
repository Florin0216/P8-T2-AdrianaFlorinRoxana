package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
public class CaseFiles {

    @Id
    @Column(name = "case_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_name", nullable = false, length = 40)
    private String caseName;

    @Column(name = "case_category", nullable = false, length = 20)
    private String caseCategory;

    @Column(name = "case_description", nullable = false, length = 150)
    private String caseDescription;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Stations station;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agents agent;

    @OneToMany(mappedBy = "caseFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseEvidences> caseEvidences;
}
