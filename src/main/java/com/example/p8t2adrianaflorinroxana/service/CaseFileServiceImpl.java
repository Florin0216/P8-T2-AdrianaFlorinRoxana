package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.CaseFiles;
import com.example.p8t2adrianaflorinroxana.repository.CaseFileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaseFileServiceImpl {

    private final CaseFileRepository caseFileRepository;

    public CaseFileServiceImpl(CaseFileRepository caseFileRepository) {
        this.caseFileRepository = caseFileRepository;
    }

    public void createCaseFile(CaseFiles caseFiles) {

        caseFiles.setCreatedAt(LocalDateTime.now());
        caseFiles.setStatus("ACTIVE");
        caseFileRepository.save(caseFiles);
    }
}
