package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "case_evidences")
public class CaseEvidences {

    @Id
    @Column(name = "evidence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evidence_name", nullable = false, length = 20)
    private String evidenceName;

    @Column(name = "evidence_path",nullable = false)
    private String evidencePath;

    @Column(name = "uploaded_at")
    private LocalTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private CaseFiles caseFile;

}
