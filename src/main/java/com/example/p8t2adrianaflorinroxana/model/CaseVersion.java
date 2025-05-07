package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "case_version")
public class CaseVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private CaseFiles caseFile;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String versionData;

    @Column(nullable = false)
    private Integer versionNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CaseFiles getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(CaseFiles caseFile) {
        this.caseFile = caseFile;
    }

    public String getVersionData() {
        return versionData;
    }

    public void setVersionData(String versionData) {
        this.versionData = versionData;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }
}
