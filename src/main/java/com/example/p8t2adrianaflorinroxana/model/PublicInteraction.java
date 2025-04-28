package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "public_interaction")
public class PublicInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interaction_id")
    private Long id;

    @Column(name = "citezen_name", length = 50)
    private String citezenName;

    @Column(name = "citezen_email", length = 30)
    private String citezenEmail;

    @Column(name = "citezen_phone")
    private int citezenPhone;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "case_id")
    private CaseFiles caseFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCitezenName() {
        return citezenName;
    }

    public void setCitezenName(String citezenName) {
        this.citezenName = citezenName;
    }

    public String getCitezenEmail() {
        return citezenEmail;
    }

    public void setCitezenEmail(String citezenEmail) {
        this.citezenEmail = citezenEmail;
    }

    public int getCitezenPhone() {
        return citezenPhone;
    }

    public void setCitezenPhone(int citezenPhone) {
        this.citezenPhone = citezenPhone;
    }

    public CaseFiles getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(CaseFiles caseFile) {
        this.caseFile = caseFile;
    }
}
