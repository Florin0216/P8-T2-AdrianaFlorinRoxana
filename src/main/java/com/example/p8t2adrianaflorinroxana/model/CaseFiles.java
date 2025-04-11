package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "case_files")
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
    private Agents lastAgentAccess;

    @OneToMany(mappedBy = "caseFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseEvidences> caseEvidences;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Stations getStation() {
        return station;
    }

    public void setStation(Stations station) {
        this.station = station;
    }

    public Agents getAgent() {
        return lastAgentAccess;
    }

    public void setAgent(Agents agent) {
        this.lastAgentAccess = agent;
    }
}
